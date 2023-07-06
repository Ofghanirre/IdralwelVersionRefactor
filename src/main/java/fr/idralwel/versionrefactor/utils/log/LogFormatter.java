package fr.idralwel.versionrefactor.utils.log;

import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static fr.idralwel.versionrefactor.utils.log.AnsiColor.*;

public class LogFormatter extends Formatter {
    /**
     * Format the given log record and return the formatted string.
     * <p>
     * The resulting formatted String will normally include a
     * localized and formatted version of the LogRecord's message field.
     * It is recommended to use the {@link Formatter#formatMessage}
     * convenience method to localize and format the message field.
     *
     * @param record the log record to be formatted.
     * @return the formatted log record
     */
    private static final Map<Level, AnsiColor> levelColor = Map.of(Level.FINE, ANSI_GREEN, Level.INFO, ANSI_WHITE, Level.WARNING, ANSI_YELLOW, Level.SEVERE, ANSI_RED);
    @Override
    public String format(LogRecord record) {
        return ANSI_CYAN + "[" + record.getLoggerName() + "]" + ANSI_PURPLE + " | " + ANSI_WHITE + levelColor.getOrDefault(record.getLevel(), ANSI_WHITE) + record.getLevel() + "\t" + ANSI_WHITE + ": " + record.getMessage() + "\n";
    }
}
