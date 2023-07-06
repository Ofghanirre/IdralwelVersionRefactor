package fr.idralwel.versionrefactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Consumer;

public class Main {
    public static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger("CONSOLE_LOGGER");
    public static final Logger FILES_DIFFERENT_LOGGER = LoggerFactory.getLogger("FILES_DIFFERENT_LOGGER");
    public static final Logger FILES_ABSENT_IN_SOURCE_LOGGER = LoggerFactory.getLogger("FILES_ABSENT_IN_SOURCE_LOGGER");
    public static final Logger FILES_ABSENT_IN_REGISTER_LOGGER = LoggerFactory.getLogger("FILES_ABSENT_IN_REGISTER_LOGGER");
    public static final Logger FILES_FROM_SOLUTION_LOGGER = LoggerFactory.getLogger("FILES_FROM_SOLUTION_LOGGER");
    public static final Logger FILE_DELETED_IN_SOLUTION_LOGGER = LoggerFactory.getLogger("FILE_DELETED_IN_SOLUTION_LOGGER");


    private static final String USAGE = "REQUIRED: SourcePath RegisterPath SolutionPath ResultPath\n"
            + "DESCRIPTION:\n"
            + "- SourcePath : The Path to the folder that contains the files that you want to translate\n"
            + "- RegisterPath : The Path to the folder that contains the files to be compared with\n"
            + "- SolutionPath : The Path to the folder that contains the files that are solutions if the recognition was successful\n"
            + "- ResultPath : The Path to the folder that will contain the Output, it needs to be empty\n";
    private static final Consumer<String> ERROR_FILE_NOT_FOUND = (fileName) -> CONSOLE_LOGGER.warn("The Following Path wasn't recognized: " + fileName);

    private static Optional<Path> assertPath(String stringPath) {
        Path result = Paths.get(stringPath);
        if (!Files.exists(result)) {
            ERROR_FILE_NOT_FOUND.accept(stringPath);
            return Optional.empty();
        }
        return Optional.of(result);
    }


    public static void main(String[] args) {
        if (args.length != 4) {
            CONSOLE_LOGGER.warn(USAGE);
            CONSOLE_LOGGER.warn("Exiting...");
        }
        Path sourcePath = assertPath(args[0]).orElseThrow();
        Path registerPath = assertPath(args[1]).orElseThrow();
        Path solutionPath = assertPath(args[2]).orElseThrow();
        Optional<Path> resultPathOptional = createResultPath(Paths.get(args[3]));
        if (resultPathOptional.isEmpty()) return;
        Path resultPath = resultPathOptional.get();

        FileComparator fileComparator = new FileComparator(sourcePath, registerPath, solutionPath, resultPath);
        FileComparatorStats stats = fileComparator.compare();
    }

    private static Optional<Path> createResultPath(Path resultPath) {
        try {
            if (!Files.exists(resultPath)) {
                Files.createDirectories(resultPath);
            } else {
                if (!Files.list(resultPath).toList().isEmpty()) {
                    CONSOLE_LOGGER.error("ResultPath : " + resultPath + " needs to be an empty directory, exiting...");
                    return Optional.empty();
                }
            }
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.of(resultPath);
    }
}