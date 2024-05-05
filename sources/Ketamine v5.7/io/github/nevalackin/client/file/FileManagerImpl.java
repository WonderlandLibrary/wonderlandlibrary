package io.github.nevalackin.client.file;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.nevalackin.client.file.FileManager;
import io.github.nevalackin.client.core.KetamineClient;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static java.util.zip.Deflater.BEST_COMPRESSION;

public final class FileManagerImpl implements FileManager {

    private File directory;

    private final Map<String, File> aliasMap = new HashMap<>();

    private static final Gson GSON = new Gson();
    private static final JsonParser PARSER = new JsonParser();

    public FileManagerImpl() {
        final File directory = this.getClientDirectory();

        // TODO :: Add files here

        this.createFile("configs", new File(directory, "configs"));
        this.createFile("scripts", new File(directory, "scripts"));
        this.createFile("alts", new File(directory, "accounts.keta"));
        this.createFile("binds", new File(directory, "binds.keta"));
    }

    @Override
    public void writeJson(final File file, final JsonElement json) {
        final Thread writeThread = new Thread(() -> {
            try {
                final String jsonS = GSON.toJson(json);
                final byte[] input = jsonS.getBytes(StandardCharsets.UTF_8);
                final byte[] compressed = compress(input);

                Files.write(file.toPath(), compressed);
            } catch (IOException e) {
                // TODO :: Error log
                e.printStackTrace();
            }
        });

        writeThread.start();
    }

    @Override
    public JsonElement parse(final File file) {
        try {
            final byte[] compressed = Files.readAllBytes(file.toPath());
            final byte[] decompressed = decompress(compressed);
            final String toString = new String(decompressed, 0, decompressed.length, StandardCharsets.UTF_8);

            return PARSER.parse(toString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] compress(final byte[] input) throws IOException {
        if (input.length == 0) return input;
        // Setup compressor
        Deflater compressor = new Deflater();
        compressor.setLevel(BEST_COMPRESSION);
        compressor.setInput(input);
        compressor.finish();

        final ByteArrayOutputStream stream = new ByteArrayOutputStream(input.length);

        byte[] buffer = new byte[1024];
        while (!compressor.finished()) {
            final int compressedLen = compressor.deflate(buffer);
            stream.write(buffer, 0, compressedLen);
        }

        stream.close();
        compressor.end();

        return stream.toByteArray();
    }

    private static byte[] decompress(final byte[] input) throws DataFormatException, IOException {
        if (input.length == 0) return input;
        // Decompress the input
        Inflater decompressor = new Inflater();
        decompressor.setInput(input);

        final ByteArrayOutputStream stream = new ByteArrayOutputStream(input.length);

        // Allocate a buffer
        byte[] buffer = new byte[1024];
        while (!decompressor.finished()) {
            int decompressedLen = decompressor.inflate(buffer);
            stream.write(buffer, 0, decompressedLen);
        }

        // Close stream & cleanup decompressor
        stream.close();
        decompressor.end();

        return stream.toByteArray();
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
            this.directory = new File(KetamineClient.getInstance().getName());

            if (!this.directory.mkdir()) {
                // TODO :: Error log
            }
        }

        return this.directory;
    }
}
