package pipeline;

import pipeline.config.Config;
import pipeline.email.ConfiguredEmailer;
import pipeline.email.Emailer;
import pipeline.log.Log;
import pipeline.log.Logged;
import pipeline.log.LoggedFunction;
import pipeline.log.Logger;
import pipeline.project.Project;
import pipeline.test.TestStepResult;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Pipeline {
    private final ConfiguredEmailer configuredEmailer;
    private final Logger logger;

    private Pipeline(ConfiguredEmailer configuredEmailer, Logger logger) {
        this.configuredEmailer = configuredEmailer;
        this.logger = logger;
    }

    public static Pipeline create(Config config, Emailer emailer, Logger logger) {
        return new Pipeline(
                ConfiguredEmailer.createConfiguredEmailerWith(config, emailer),
                logger);
    }

    public void run(Project project) {
        runTests()
                .andThen(ifNotFailed(deploy(project)))
                .thenFinally(sendSummaryByEmail())
                .accept(project, logger);
    }

    private BiConsumer<PipelineResultSummary, Logger> sendSummaryByEmail() {
        return configuredEmailer::sendSummary;
    }

    private Function<Logger, Logged<PipelineResultSummary>> deploy(Project project) {
        return logger -> project.deploy(
                success -> logger.createLogWith(Emailer.successEmailer(), Log.info("Deployment successful")),
                failure -> logger.createLogWith(Emailer.failureEmailer(), Log.error("Deployment failed"))
        );
    }

    private LoggedFunction<TestStepResult, PipelineResultSummary> ifNotFailed(Function<Logger, Logged<PipelineResultSummary>> notFailedCase) {
        return (testStepResult, logger) -> testStepResult.match(
                notFailedTests -> notFailedCase.apply(logger),
                testsFailed -> logger.withoutLogs(emailer -> emailer.send("Tests failed")));
    }

    private LoggedFunction<Project, TestStepResult> runTests() {
        return (project, logger) -> project.runTests(
                noTests -> logger.createLogWith(TestStepResult.noTests(), Log.info("No tests")),
                passingTests -> logger.createLogWith(TestStepResult.testsSuccessful(), Log.info("Tests passed")),
                failingTests -> logger.createLogWith(TestStepResult.testsFailed(), Log.error("Tests failed")));
    }
}
