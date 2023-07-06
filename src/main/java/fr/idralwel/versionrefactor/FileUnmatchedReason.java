package fr.idralwel.versionrefactor;

import fr.idralwel.versionrefactor.utils.log.AnsiColor;
import org.slf4j.Logger;

import java.nio.file.Path;

import static fr.idralwel.versionrefactor.utils.log.AnsiColor.*;

enum EFileUnmatchedReason {
    UNKNOWN,
    DIFFERENT,
    ABSENT_IN_SOURCE,
    ABSENT_IN_REGISTER,
    FILE_FROM_SOLUTION,
    FILE_DELETED_IN_SOLUTION;

    AnsiColor getColor() {
        return switch (this) {
            case DIFFERENT -> ANSI_YELLOW;
            case ABSENT_IN_SOURCE -> ANSI_RED;
            case ABSENT_IN_REGISTER -> ANSI_PURPLE;
            case FILE_FROM_SOLUTION -> ANSI_BLUE;
            default -> ANSI_RESET;
        };
    }
}

public record FileUnmatchedReason(Path file, EFileUnmatchedReason reason) {

    public static FileUnmatchedReason createAndLog(Path file, EFileUnmatchedReason reason) {
        FileUnmatchedReason result = new FileUnmatchedReason(file, reason);
        getLogger(reason).warn(result.toString());
        return result;
    }
    
    private String getSign() {
        return switch (this.reason) {
            case ABSENT_IN_SOURCE -> ANSI_RED + "-" + ANSI_WHITE;
            default -> ANSI_GREEN + "+" + ANSI_WHITE;
        };
    }

    private static Logger getLogger(EFileUnmatchedReason reason) {
        return switch (reason) {
            case DIFFERENT -> Main.FILES_DIFFERENT_LOGGER;
            case ABSENT_IN_SOURCE -> Main.FILES_ABSENT_IN_SOURCE_LOGGER;
            case ABSENT_IN_REGISTER -> Main.FILES_ABSENT_IN_REGISTER_LOGGER;
            case FILE_FROM_SOLUTION -> Main.FILES_FROM_SOLUTION_LOGGER;
            case FILE_DELETED_IN_SOLUTION -> Main.FILE_DELETED_IN_SOLUTION_LOGGER;
            default -> Main.CONSOLE_LOGGER;
        };
    }

    @Override
    public String toString() {
        return "File Unmatched [" + this.getSign() + "] '" + file.toString() + "', reason: " +  reason.getColor() + reason.name() + ANSI_RESET;
    }
}
