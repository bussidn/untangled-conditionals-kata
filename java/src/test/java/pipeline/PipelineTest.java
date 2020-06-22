package pipeline;

import org.junit.jupiter.api.Test;
import pipeline.config.Config;
import pipeline.email.Emailer;
import pipeline.project.Project;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PipelineTest {
    private final Config config = mock(Config.class);
    private final CapturingLogger log = new CapturingLogger();
    private final Emailer emailer = mock(Emailer.class);

    @Test
    void project_with_tests_that_deploys_successfully_with_email_notification() {
        Pipeline pipeline = pipelineSendingSummary();

        Project project = Project.builder()
                .withSuccessfulTests()
                .withSuccessfulDeployment()
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList(
                "INFO: Tests passed",
                "INFO: Deployment successful",
                "INFO: Sending email"
        ), log.getLoggedLines());

        verify(emailer).send("Deployment completed successfully");
    }

    private Pipeline pipelineSendingSummary() {
        when(config.sendEmailSummary()).thenReturn(true);
        return Pipeline.create(config, emailer, log);
    }

    @Test
    void project_with_tests_that_deploys_successfully_without_email_notification() {
        Pipeline pipeline = pipelineNotSendingSummary();

        Project project = Project.builder()
                .withSuccessfulTests()
                .withSuccessfulDeployment()
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList(
                "INFO: Tests passed",
                "INFO: Deployment successful",
                "INFO: Email disabled"
        ), log.getLoggedLines());

        verify(emailer, never()).send(any());
    }

    private Pipeline pipelineNotSendingSummary() {
        when(config.sendEmailSummary()).thenReturn(false);
        return Pipeline.create(config, emailer, log);
    }

    @Test
    void project_without_tests_that_deploys_successfully_with_email_notification() {
        Pipeline pipeline = pipelineSendingSummary();

        Project project = Project.builder()
                .withoutTests()
                .withSuccessfulDeployment()
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList(
                "INFO: No tests",
                "INFO: Deployment successful",
                "INFO: Sending email"
        ), log.getLoggedLines());

        verify(emailer).send("Deployment completed successfully");
    }

    @Test
    void project_without_tests_that_deploys_successfully_without_email_notification() {
        Pipeline pipeline = pipelineNotSendingSummary();

        Project project = Project.builder()
                .withoutTests()
                .withSuccessfulDeployment()
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList(
                "INFO: No tests",
                "INFO: Deployment successful",
                "INFO: Email disabled"
        ), log.getLoggedLines());

        verify(emailer, never()).send(any());
    }

    @Test
    void project_with_tests_that_fail_with_email_notification() {
        Pipeline pipeline = pipelineSendingSummary();

        Project project = Project.builder()
                .withFailingTests()
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList(
                "ERROR: Tests failed",
                "INFO: Sending email"
        ), log.getLoggedLines());

        verify(emailer).send("Tests failed");
    }

    @Test
    void project_with_tests_that_fail_without_email_notification() {
        Pipeline pipeline = pipelineNotSendingSummary();

        Project project = Project.builder()
                .withFailingTests()
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList(
                "ERROR: Tests failed",
                "INFO: Email disabled"
        ), log.getLoggedLines());

        verify(emailer, never()).send(any());
    }

    @Test
    void project_with_tests_and_failing_build_with_email_notification() {
        Pipeline pipeline = pipelineSendingSummary();

        Project project = Project.builder()
                .withSuccessfulTests()
                .withFailingDeployment()
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList(
                "INFO: Tests passed",
                "ERROR: Deployment failed",
                "INFO: Sending email"
        ), log.getLoggedLines());

        verify(emailer).send("Deployment failed");
    }

    @Test
    void project_with_tests_and_failing_build_without_email_notification() {
        Pipeline pipeline = pipelineNotSendingSummary();

        Project project = Project.builder()
                .withSuccessfulTests()
                .withFailingDeployment()
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList(
                "INFO: Tests passed",
                "ERROR: Deployment failed",
                "INFO: Email disabled"
        ), log.getLoggedLines());

        verify(emailer, never()).send(any());
    }

    @Test
    void project_without_tests_and_failing_build_with_email_notification() {
        Pipeline pipeline = pipelineSendingSummary();

        Project project = Project.builder()
                .withoutTests()
                .withFailingDeployment()
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList(
                "INFO: No tests",
                "ERROR: Deployment failed",
                "INFO: Sending email"
        ), log.getLoggedLines());

        verify(emailer).send("Deployment failed");
    }

    @Test
    void project_without_tests_and_failing_build_without_email_notification() {
        Pipeline pipeline = pipelineNotSendingSummary();

        Project project = Project.builder()
                .withoutTests()
                .withFailingDeployment()
                .build();

        pipeline.run(project);

        assertEquals(Arrays.asList(
                "INFO: No tests",
                "ERROR: Deployment failed",
                "INFO: Email disabled"
        ), log.getLoggedLines());

        verify(emailer, never()).send(any());
    }
}