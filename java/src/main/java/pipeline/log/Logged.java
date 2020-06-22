package pipeline.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Logged<T> {
    private final Logs logs;
    private final Logger logger;
    private final T value;

    public Logged(Logs logs, Logger logger, T value) {
        this.logs = logs;
        this.logger = logger;
        this.value = value;
    }

    public <R> Logged<R> flatMap(LoggedFunction<? super T, ? extends R> mapper) {
        Logged<? extends R> rLogged = mapper.apply(value, logger);
        return new Logged<>(logs.concat(rLogged.logs), logger, rLogged.value);
    }

    public T log() {
        logs.flushWith(logger);
        return value;
    }

    public static class Logs {
        private final List<Log> values;

        public Logs(List<Log> values) {
            this.values = values;
        }

        public static Logs of(Log log) {
            return new Logs(Collections.singletonList(log));
        }

        public static Logs empty() {
            return new Logs(new ArrayList<>());
        }

        public void flushWith(Logger logger) {
            values.forEach(log -> log.accept(logger));
        }

        public Logs concat(Logs logs) {
            ArrayList<Log> newList = new ArrayList<>(values);
            newList.addAll(logs.values);
            return new Logs(newList);
        }
    }

}
