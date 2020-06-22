package pipeline.test;

import java.util.function.Function;

public interface TestRunner {

    static NoTests noTests() {
        return NoTests.INSTANCE;
    }

    static PassingTests passingTests() {
        return PassingTests.INSTANCE;
    }

    static FailingTests failingTests() {
        return FailingTests.INSTANCE;
    }

    <R> R match(
            Function<NoTests, R> noTestsCase,
            Function<PassingTests, R> passingTestsCase,
            Function<FailingTests, R> failingTestsCase
    );

    enum NoTests implements TestRunner {
        INSTANCE;

        @Override
        public <R> R match(Function<NoTests, R> noTestsCase, Function<PassingTests, R> passingTestsCase, Function<FailingTests, R> failingTestsCase) {
            return noTestsCase.apply(this);
        }
    }

    enum PassingTests implements TestRunner {
        INSTANCE;

        @Override
        public <R> R match(Function<NoTests, R> noTestsCase, Function<PassingTests, R> passingTestsCase, Function<FailingTests, R> failingTestsCase) {
            return passingTestsCase.apply(this);
        }
    }

    enum FailingTests implements TestRunner {
        INSTANCE;

        @Override
        public <R> R match(Function<NoTests, R> noTestsCase, Function<PassingTests, R> passingTestsCase, Function<FailingTests, R> failingTestsCase) {
            return failingTestsCase.apply(this);
        }
    }
}
