package pipeline.email;

import pipeline.PipelineResultSummary;
import pipeline.config.Config;
import pipeline.log.Logger;

@FunctionalInterface
public interface ConfiguredEmailer {

    void sendSummary(PipelineResultSummary pipelineResultSummary, Logger logger);

    static ConfiguredEmailer createConfiguredEmailerWith(Config config, Emailer emailer) {
        return config.sendEmailSummary() ?
                createEmailer(emailer) :
                noEmail();
    }

    static ConfiguredEmailer noEmail() {
        return (pipelineResultSummary, logger) -> logger.info("Email disabled");
    }

    static ConfiguredEmailer createEmailer(Emailer emailer) {
        return (pipelineResultSummary, logger) -> {
            logger.info("Sending email");
            pipelineResultSummary.sendResultWith(emailer);
        };
    }

}
