package dev.tenacity.util.io;

import java.io.*;

public final class FileUtil {

    private FileUtil() {
    }

    public static String readFile(final File file) {
        final StringBuilder stringBuilder = new StringBuilder();

        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void writeFile(final File file, final String content) {
        try {
            final FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static String readInputStream(final InputStream inputStream) {
        final StringBuilder stringBuilder = new StringBuilder();

        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
