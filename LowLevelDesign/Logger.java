// CustomLoggerDemo.java

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Demonstrates the usage of a custom logger in Java.
 * 
 * This class contains the main function that serves as the entry point of the application.
 */
public class CustomLoggerDemo {
    public static void main(String[] args) {
        // Initialize the logger instance
        Logger logger = Logger.getInstance();

        // Add a FileAppender to log messages to a file named "application.log"
        logger.addAppender(new FileAppender("application.log"));

        // Optionally, set the minimum logging level (default is DEBUG)
        // logger.setLogLevel(LogLevel.INFO);

        // Log messages at various levels
        logger.debug("This is a DEBUG message.");
        logger.info("This is an INFO message.");
        logger.warn("This is a WARN message.");
        logger.error("This is an ERROR message.");
        logger.fatal("This is a FATAL message.");
    }
}

/**
 * Defines various logging levels.
 */
enum LogLevel {
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4),
    FATAL(5);

    private final int level;

    LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

/**
 * Specifies the contract for different appenders.
 */
interface Appender {
    void append(String formattedMessage);
}

/**
 * Appender implementation that logs messages to the console.
 */
class ConsoleAppender implements Appender {
    @Override
    public void append(String formattedMessage) {
        System.out.println(formattedMessage);
    }
}

/**
 * Appender implementation that logs messages to a specified file.
 */
class FileAppender implements Appender {
    private final String filePath;
    private BufferedWriter writer;

    public FileAppender(String filePath) {
        this.filePath = filePath;
        try {
            // Initialize BufferedWriter in append mode
            this.writer = new BufferedWriter(new FileWriter(this.filePath, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void append(String formattedMessage) {
        try {
            writer.write(formattedMessage);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Specifies how log messages are formatted.
 */
interface Formatter {
    String format(LogLevel level, String message);
}

/**
 * A simple formatter that includes timestamp, log level, and message.
 */
class SimpleFormatter implements Formatter {
    private final SimpleDateFormat dateFormat;

    public SimpleFormatter() {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Override
    public String format(LogLevel level, String message) {
        String timestamp = dateFormat.format(new Date());
        return String.format("[%s] [%s] %s", timestamp, level.name(), message);
    }
}

/**
 * The core logger that manages logging operations.
 */
class Logger {
    private LogLevel currentLevel;
    private final Formatter formatter;
    private final List<Appender> appenders;

    // Singleton instance
    private static Logger instance;

    /**
     * Private constructor to enforce singleton pattern.
     *
     * @param level     The minimum logging level.
     * @param formatter The formatter to use for log messages.
     */
    private Logger(LogLevel level, Formatter formatter) {
        this.currentLevel = level;
        this.formatter = formatter;
        this.appenders = new ArrayList<>();
    }

    /**
     * Retrieves the singleton instance of the Logger.
     *
     * @return The Logger instance.
     */
    public static synchronized Logger getInstance() {
        if (instance == null) {
            // Default configuration: DEBUG level with SimpleFormatter
            instance = new Logger(LogLevel.DEBUG, new SimpleFormatter());
            // Add default ConsoleAppender
            instance.addAppender(new ConsoleAppender());
        }
        return instance;
    }

    // or
    /**
    * public static Logger getLogger(){
    *     if(logger == null){
    *        synchronized(Logger.class)
    *        {
    *            if(logger == null) logger = new Logger();
    *        }
    *        return logger;
    *     }
    *}
    */

    /**
     * Adds an appender to the logger.
     *
     * @param appender The appender to add.
     */
    public void addAppender(Appender appender) {
        this.appenders.add(appender);
    }

    /**
     * Sets the minimum logging level.
     *
     * @param level The logging level to set.
     */
    public void setLogLevel(LogLevel level) {
        this.currentLevel = level;
    }

    // Logging methods for various levels
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void fatal(String message) {
        log(LogLevel.FATAL, message);
    }

    /**
     * Logs a message at the specified logging level.
     *
     * @param level   The severity level of the log.
     * @param message The message to log.
     */
    private void log(LogLevel level, String message) {
        if (level.getLevel() >= this.currentLevel.getLevel()) {
            String formattedMessage = formatter.format(level, message);
            for (Appender appender : appenders) {
                appender.append(formattedMessage);
            }
        }
    }
}
