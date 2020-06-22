package pipeline;

import pipeline.email.Emailer;

@FunctionalInterface
public interface PipelineResultSummary {

    void sendResultWith(Emailer emailer);

}
