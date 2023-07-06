package fr.idralwel.versionrefactor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Main {
    public static final Logger DEBUG_LOGGER = Logger.getLogger("Idralwel Version Refactor");
    private static final String USAGE = "REQUIRED: SourcePath RegisterPath SolutionPath ResultPath\n"
            + "DESCRIPTION:\n"
            + "- SourcePath : The Path to the folder that contains the files that you want to translate\n"
            + "- RegisterPath : The Path to the folder that contains the files to be compared with\n"
            + "- SolutionPath : The Path to the folder that contains the files that are solutions if the recognition was successful\n"
            + "- ResultPath : The Path to the folder that will contain the Output, it needs to be empty\n";
    private static final Consumer<String> ERROR_FILE_NOT_FOUND = (fileName) -> DEBUG_LOGGER.warning("The Following Path wasn't recognized: " + fileName);

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
            DEBUG_LOGGER.warning(USAGE);
            DEBUG_LOGGER.warning("Exiting...");
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
                    DEBUG_LOGGER.severe("ResultPath : " + resultPath + " needs to be an empty directory, exiting...");
                    return Optional.empty();
                }
            }
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.of(resultPath);
    }
}