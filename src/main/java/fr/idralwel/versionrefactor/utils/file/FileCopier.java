package fr.idralwel.versionrefactor.utils.file;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

public class FileCopier {
    public static void copyFile(Path copiedPath, Path destinationPath) {
        try {
            FileUtils.copyFile(copiedPath.toFile(), destinationPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Could not copy file " + copiedPath + " to " + destinationPath + "\n", e);
        }
    }
}
