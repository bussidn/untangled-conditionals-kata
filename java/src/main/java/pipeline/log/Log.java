package pipeline.log;

import java.util.function.BiConsumer;

public class Log implements java.util.function.Consumer<Logger> {
    private final Level level;
    private final String message;

    public Log(Level level, String message) {
        this.level = level;
        this.message = message;
    }

    public static Log info(String message) {
        return new Log(Level.INFO, message);
    }

    public static Log error(String message) {
        return new Log(Level.ERROR, message);
    }

    @Override
    public void accept(Logger logger) {
        level.accept(logger, message);
    }

    public enum Level implements BiConsumer<Logger, String> {
        INFO(Logger::info),
        ERROR(Logger::error);

        private final BiConsumer<Logger, String> biConsumer;

        Level(BiConsumer<Logger, String> biConsumer) {
            this.biConsumer = biConsumer;
        }

        @Override
        public void accept(Logger logger, String message) {
            biConsumer.accept(logger, message);
        }
    }
}
