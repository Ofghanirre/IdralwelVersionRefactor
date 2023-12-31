package fr.idralwel.versionrefactor;

import static fr.idralwel.versionrefactor.utils.log.AnsiColor.*;

public class FileComparatorStats {
    public int fileCompared = 0;
    public int fileMatched = 0;
    public int fileUnMatched = 0;
    public int fileDifferent = 0;
    public int fileAbsentInSource = 0;
    public int fileAbsentInRegister = 0;
    public int fileExclusiveToSolution = 0;
    public int fileDeletedInSolution = 0;

    @Override
    public String toString() {
        return  "Statistics :" + ANSI_WHITE +
                "\n\t" + ANSI_BLUE + "- Files Compared:\t" + ANSI_CYAN + fileCompared +
                "\n\t" + ANSI_BLUE + "- Files Matched: \t" + ANSI_CYAN + fileMatched +
                "\n\t" + ANSI_BLUE + "- Files Ignored:\t" + ANSI_CYAN + fileDeletedInSolution +
                "\n\t" + ANSI_BLUE + "- Files UnMatched:\t" + ANSI_CYAN + fileUnMatched +
                "\n\t" + ANSI_BLUE + "- Files Migrated:\t" + ANSI_YELLOW + "{" + (fileAbsentInRegister + fileExclusiveToSolution + fileDifferent) + "}" +
                "\n\t\t" + ANSI_GREEN + "+" + ANSI_BLUE + " Different in Source/Register Folder:\t" + ANSI_CYAN + fileDifferent +
                "\n\t\t" + ANSI_GREEN + "+" + ANSI_BLUE + " Absents in Register Folder:\t" + ANSI_CYAN + fileAbsentInRegister +
                "\n\t\t" + ANSI_GREEN + "+" + ANSI_BLUE + " Exclusives to Solution Folder:\t" + ANSI_CYAN + fileExclusiveToSolution +
                "\n\t" + ANSI_BLUE + "- Files Lost:\t\t" + ANSI_YELLOW + "{" + (fileAbsentInSource + fileDeletedInSolution) + "}" +
                "\n\t\t" + ANSI_RED + "-" + ANSI_BLUE + " Files Absents in Source:\t" + ANSI_CYAN + fileAbsentInSource +
                "\n\t\t" + ANSI_RED + "-" + ANSI_BLUE + " Files Deleted in Solution:\t" + ANSI_CYAN + fileDeletedInSolution +
                ANSI_WHITE;
    }
}
