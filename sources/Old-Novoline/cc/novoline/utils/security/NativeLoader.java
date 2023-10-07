package cc.novoline.utils.security;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Random;

public class NativeLoader {

    private static final int MIN_PREFIX_LENGTH = 3;
    public static final String NATIVE_FOLDER_PATH_PREFIX = "novoline-temp";

    private static File temporaryDir;

    public static void loadLibraryFromJar(String path) throws IOException {
        String[] parts = path.split("/");
        String filename = parts.length > 1 ? parts[parts.length - 1] : null;

        // Prepare temporary file
        if (temporaryDir == null) {
            temporaryDir = createTempDirectory();
            temporaryDir.deleteOnExit();
        }

        assert filename != null;

        File temp = appendNonceToFile(new File(temporaryDir, filename));

        try (InputStream is = NativeLoader.class.getResourceAsStream(path)) {
            Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            FileUtils.deleteQuietly(temp);
            throw e;
        } catch (NullPointerException e) {
            FileUtils.deleteQuietly(temp);
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }

        try {
            System.load(temp.getAbsolutePath());
        } finally {
            if (isPosixCompliant()) {
                FileUtils.deleteQuietly(temp);
            } else {
                temp.deleteOnExit();
            }
        }
    }

    private static File appendNonceToFile(File file) {
        return new File(file.getParent(),
                file.getName().substring(0, file.getName().lastIndexOf('.'))
                        + "-" + new Random().nextInt()
                        + file.getName().substring(file.getName().lastIndexOf('.')));
    }

    private static boolean isPosixCompliant() {
        try {
            return FileSystems.getDefault()
                    .supportedFileAttributeViews()
                    .contains("posix");
        } catch (FileSystemNotFoundException
                | ProviderNotFoundException
                | SecurityException e) {
            return false;
        }
    }

    private static File createTempDirectory() {
        String tempDir = System.getProperty("java.io.tmpdir");
        File generatedDir = new File(tempDir, NATIVE_FOLDER_PATH_PREFIX);

        generatedDir.mkdir();
        return generatedDir;
    }
}
