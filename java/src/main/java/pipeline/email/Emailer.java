package pipeline.email;

import pipeline.PipelineResultSummary;

public interface Emailer {

    void send(String message);

    static PipelineResultSummary successEmailer() {
        return emailer -> emailer.send("Deployment completed successfully");
    }

    static PipelineResultSummary failureEmailer() {
        return emailer -> emailer.send("Deployment failed");
    }
}
