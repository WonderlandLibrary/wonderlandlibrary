package graphite.api.management.file;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import graphite.impl.Graphite;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public final class FileManagerImpl implements FileManager {

    private File directory;

    private final Map<String, File> aliasMap = new HashMap<>();

    private static final Gson GSON = new Gson();
    private static final JsonParser PARSER = new JsonParser();

    public FileManagerImpl() {
        final File directory = this.getClientDirectory();

        // TODO :: Add files here

        this.createFile("configs", new File(directory, "configs"));
    }

    @Override
    public void writeJson(final File file, final JsonElement json) {
        final Thread writeThread = new Thread(() -> {
            try {
                final String jsonS = GSON.toJson(json);
                final byte[] input = jsonS.getBytes(StandardCharsets.UTF_8);

                Files.write(file.toPath(), input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writeThread.start();
    }

    @Override
    public JsonElement parse(final File file) {
        try {
            final byte[] jsonBytes = Files.readAllBytes(file.toPath());
            final String toString = new String(jsonBytes, StandardCharsets.UTF_8);

            return PARSER.parse(toString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public File getFile(final String alias) {
        return this.aliasMap.get(alias);
    }

    @Override
    public void createFile(final String alias, final File file) {
        try {
            boolean ignored = FilenameUtils.getExtension(file.getName()).equals("") ? file.mkdirs() : file.createNewFile();

            this.aliasMap.put(alias, file);
        } catch (IOException ignored) {
            // TODO :: Error log
        }
    }

    @Override
    public File getClientDirectory() {
        if (this.directory == null) {
            this.directory = new File(Graphite.NAME);

            if (!this.directory.mkdir()) {
                // TODO :: Error log
            }
        }

        return this.directory;
    }
}