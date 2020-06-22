package pipeline.test;

import java.util.function.Function;

public interface TestStepResult {

    static TestStepResult noTests() {
        return NoTests.INSTANCE;
    }

    static TestStepResult testsFailed() {
        return TestsFailed.INSTANCE;
    }

    static TestStepResult testsSuccessful() {
        return TestsSuccessful.INSTANCE;
    }

    <R> R match(
            Function<NotFailedTests, R> notFailedTestsCase,
            Function<TestsFailed, R> testsFailedCase
    );

    enum TestsFailed implements TestStepResult {
        INSTANCE;

        @Override
        public <R> R match(Function<NotFailedTests, R> notFailedTestsCase, Function<TestsFailed, R> testsFailedCase) {
            return testsFailedCase.apply(this);
        }
    }

    interface NotFailedTests extends TestStepResult {

        @Override
        default <R> R match(Function<NotFailedTests, R> notFailedTestsCase, Function<TestsFailed, R> testsFailedCase) {
            return notFailedTestsCase.apply(this);
        }
    }

    enum NoTests implements NotFailedTests {
        INSTANCE
    }

    enum TestsSuccessful implements NotFailedTests {
        INSTANCE
    }

}
