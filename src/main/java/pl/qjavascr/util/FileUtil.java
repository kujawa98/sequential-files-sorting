package pl.qjavascr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static BufferedReader bufferedReader(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        return Files.newBufferedReader(path);
    }
}
