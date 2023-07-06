package fr.idralwel.versionrefactor.utils.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FileQuery {

    private FileQuery() {
        throw new UnsupportedOperationException();
    }
    private static List<Path> openFolder(Path path) {
        if (Files.exists(path)) {
            try (Stream<Path> result = Files.list(path)) {
                return result.collect(Collectors.toList());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new ArrayList<>();
    }
    public static List<Path> getDataFromFolder(Path sourcePath, Path registerPath, Path solutionPath, Predicate<Path> predicate) {
        List<Path> result = new ArrayList<>();
        result.addAll(openFolder(sourcePath).stream().filter(predicate).map(Path::getFileName).toList());
        result.addAll(openFolder(registerPath).stream().filter(predicate).map(Path::getFileName)
                .filter(p -> !result.contains(p)).toList());
        result.addAll(openFolder(solutionPath).stream().filter(predicate).map(Path::getFileName)
                .filter(p -> !result.contains(p)).toList());
        return result;
    }

    public static List<Path> getFilesFromFolder(Path sourcePath, Path registerPath, Path solutionPath) {
        return getDataFromFolder(sourcePath, registerPath, solutionPath, Files::isRegularFile);
    }
    public static List<Path> getFoldersFromFolder(Path sourcePath, Path registerPath, Path solutionPath) {
        return getDataFromFolder(sourcePath, registerPath, solutionPath, Files::isDirectory);
    }
}
