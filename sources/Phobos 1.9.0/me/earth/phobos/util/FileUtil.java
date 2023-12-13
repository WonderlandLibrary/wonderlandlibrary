package me.earth.phobos.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;

public class FileUtil {
    public static boolean appendTextFile(String data, String file) {
        try {
            Path path = Paths.get(file);
            Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("WARNING: Unable to write file: " + file);
            return false;
        }
        return true;
    }

    public static List<String> readTextFileAllLines(String file) {
        try {
            Path path = Paths.get(file);
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("WARNING: Unable to read file, creating new file: " + file);
            FileUtil.appendTextFile("", file);
            return Collections.emptyList();
        }
    }
}

