package fr.idralwel.versionrefactor;

import fr.idralwel.versionrefactor.utils.file.FileCopier;
import fr.idralwel.versionrefactor.utils.file.FileQuery;
import fr.idralwel.versionrefactor.utils.log.LogFormatter;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import static fr.idralwel.versionrefactor.utils.log.AnsiColor.*;

public class FileComparator {
    private final Path sourcePath;
    private final Path registerPath;
    private final Path solutionPath;
    private final Path resultPath;
    private final FileComparatorStats stats = new FileComparatorStats();
    private final List<FileUnmatchedReason> filesUnmatched = new ArrayList<>();
    protected static final Logger LOGGER = Logger.getLogger("IdralwelVersionRefactor");

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new LogFormatter());
        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(handler);
    }

    public FileComparator(Path sourcePath, Path registerPath, Path solutionPath, Path resultPath) {
        this.sourcePath = sourcePath;
        this.registerPath = registerPath;
        this.solutionPath = solutionPath;
        this.resultPath = resultPath;
    }

    public FileComparatorStats compare() {
        LOGGER.info(ANSI_GREEN + "Starting Refactor Operation !");
        LOGGER.info(ANSI_GREEN + "You will get logs about unmatched files here:");
        LOGGER.info("");
        this.compareRecursively(this.sourcePath, this.registerPath, this.solutionPath, this.resultPath);
        LOGGER.info("");
        LOGGER.info(ANSI_GREEN + "End of Refactor Operation !" + ANSI_RESET);
        List.of(this.stats.toString().split("\n")).forEach(LOGGER::info);
        return this.stats;
    }

    private void compareRecursively(Path sourcePath, Path registerPath, Path solutionPath, Path resultPath) {
        if (Files.isRegularFile(sourcePath) || Files.isRegularFile(registerPath) || Files.isRegularFile(solutionPath)) {
            Optional<Path> copiedPath = getCopiedPath(sourcePath, registerPath, solutionPath);
            if (copiedPath.isEmpty()) return;
            FileCopier.copyFile(copiedPath.get(), resultPath);
        } else if (Files.isDirectory(sourcePath) || Files.isDirectory(registerPath) || Files.isDirectory(solutionPath)) {
            List<Path> files = FileQuery.getFilesFromFolder(sourcePath, registerPath, solutionPath);
//            System.out.println("Files of " + sourcePath + ":" + files);
            files.forEach(path -> compareRecursively(sourcePath.resolve(path), registerPath.resolve(path),
                            solutionPath.resolve(path), resultPath.resolve(path)));

            List<Path> folders = FileQuery.getFoldersFromFolder(sourcePath, registerPath, solutionPath);
//            System.out.println("Folders of " + sourcePath + ":" + folders);
            folders.forEach(path -> compareRecursively(sourcePath.resolve(path), registerPath.resolve(path),
                    solutionPath.resolve(path), resultPath.resolve(path)));
        } else {
            Main.DEBUG_LOGGER.warning("Unhandled file type (not a dir nor a file): '" + sourcePath +"', ignoring...");
        }
    }

    private Optional<Path> getCopiedPath(Path sourcePath, Path registerPath, Path solutionPath) {
        this.stats.fileCompared++;
        if (!Files.exists(sourcePath) || !Files.exists(registerPath)) {
            this.stats.fileUnMatched++;
            if (!Files.exists(sourcePath)) {
                if (Files.exists(registerPath)) {
                    this.stats.fileAbsentInSource++;
                    this.filesUnmatched.add(FileUnmatchedReason.createAndLog(sourcePath, EFileUnmatchedReason.ABSENT_IN_SOURCE));
                    return Optional.empty();
                } else {
                    this.stats.fileExclusiveToSolution++;
                    this.filesUnmatched.add(FileUnmatchedReason.createAndLog(sourcePath, EFileUnmatchedReason.FILE_FROM_SOLUTION));
                    return Optional.of(solutionPath);
                }
            } else {
                this.stats.fileAbsentInRegister++;
                this.filesUnmatched.add(FileUnmatchedReason.createAndLog(sourcePath, EFileUnmatchedReason.ABSENT_IN_REGISTER));
                return Optional.of(sourcePath);
            }
        }
        try {
            if (FileUtils.contentEquals(sourcePath.toFile(), registerPath.toFile())) {
                this.stats.fileMatched++;
                return Optional.of(solutionPath);
            } else {
                this.stats.fileUnMatched++;
                this.filesUnmatched.add(FileUnmatchedReason.createAndLog(sourcePath, EFileUnmatchedReason.DIFFERENT));
            }
        } catch (IOException e) {
            this.stats.fileUnMatched++;
            this.filesUnmatched.add(FileUnmatchedReason.createAndLog(sourcePath, EFileUnmatchedReason.UNKNOWN));
        }
        return Optional.of(sourcePath);
    }
}
