package pipeline.log;

public interface Logger {

    void info(String message);

    void error(String message);

    default <T> Logged<T> createLogWith(T t, Log log) {
        return new Logged<>(Logged.Logs.of(log), this, t);
    }

    default <T> Logged<T> withoutLogs(T t) {
        return new Logged<>(Logged.Logs.empty(), this, t);
    }
}
