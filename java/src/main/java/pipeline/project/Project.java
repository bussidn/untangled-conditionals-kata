package pipeline.project;

import pipeline.deploy.Deployer;
import pipeline.deploy.DeploymentStepResult;
import pipeline.test.TestRunner;

import java.util.function.Function;

import static pipeline.test.TestRunner.*;

public class Project {
    private final Deployer deployer;
    private final TestRunner testRunner;

    public static ProjectBuilder builder() {
        return new ProjectBuilder();
    }

    private Project(Deployer deployer, TestRunner testRunner) {
        this.deployer = deployer;
        this.testRunner = testRunner;
    }

    public <R> R deploy(
            Function<DeploymentStepResult.Success, R> successCase,
            Function<DeploymentStepResult.Failure, R> failureCase
    ) {
        return deployer.deploy().match(successCase, failureCase);
    }

    public <R> R runTests(
            Function<NoTests, R> noTestsCase,
            Function<PassingTests, R> passingTestsCase,
            Function<FailingTests, R> failingTestsCase) {
        return testRunner.match(
                noTestsCase, passingTestsCase, failingTestsCase
        );
    }

    public static class ProjectBuilder {
        private Deployer deployer;
        private TestRunner testRunner;

        public ProjectBuilder withoutTests() {
            this.testRunner = noTests();
            return this;
        }

        public ProjectBuilder withFailingTests() {
            this.testRunner = failingTests();
            return this;
        }

        public ProjectBuilder withSuccessfulTests() {
            this.testRunner = passingTests();
            return this;
        }

        public ProjectBuilder withSuccessfulDeployment() {
            this.deployer = DeploymentStepResult::success;
            return this;
        }

        public ProjectBuilder withFailingDeployment() {
            this.deployer = DeploymentStepResult::failure;
            return this;
        }

        public Project build() {
            return new Project(deployer, testRunner);
        }
    }
}
