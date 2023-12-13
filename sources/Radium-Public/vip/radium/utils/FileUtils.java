package vip.radium.utils;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {

    public static List<String> getLines(File file) {
        try {
            InputStream fis = new FileInputStream(file);
            return IOUtils.readLines(fis, Charsets.UTF_8);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
