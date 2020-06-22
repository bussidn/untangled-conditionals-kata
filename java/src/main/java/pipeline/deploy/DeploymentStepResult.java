package pipeline.deploy;

import java.util.function.Function;

public interface DeploymentStepResult {

    static DeploymentStepResult success() {
        return Success.INSTANCE;
    }

    static DeploymentStepResult failure() {
        return Failure.INSTANCE;
    }

    <R> R match(
            Function<Success, R> successCase,
            Function<Failure, R> failureCase
    );

    enum Success implements DeploymentStepResult {
        INSTANCE;

        @Override
        public <R> R match(Function<Success, R> successCase, Function<Failure, R> failureCase) {
            return successCase.apply(this);
        }
    }

    enum Failure implements DeploymentStepResult {
        INSTANCE;

        @Override
        public <R> R match(Function<Success, R> successCase, Function<Failure, R> failureCase) {
            return failureCase.apply(this);
        }
    }

}
