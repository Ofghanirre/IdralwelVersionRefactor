package fr.idralwel.versionrefactor.utils.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class CleanMessageConverter extends ClassicConverter {
    private static final String COLOR_PATTERN = "\\u001B\\[[;\\d]*[ -/]*[@-~]";

    @Override
    public String convert(ILoggingEvent event) {
        String originalMessage = event.getFormattedMessage();
        return removeColor(originalMessage);
    }

    private String removeColor(String message) {
        return message.replaceAll(COLOR_PATTERN, "");
    }
}