package pipeline.log;

import java.util.Objects;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface LoggedFunction<T, R> {

    Logged<R> apply(T t, Logger logger);

    default <V> LoggedFunction<T, V> andThen(LoggedFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t, logger) -> this.apply(t, logger).flatMap(after);
    }

    default BiConsumer<T, Logger> thenFinally(BiConsumer<? super R, ? super Logger> after) {
        Objects.requireNonNull(after);
        return (t, logger) -> after.accept(this.apply(t, logger).log(), logger);
    }
}
