package io.github.nevalackin.client.file;

import com.google.gson.JsonElement;

import java.io.File;

public interface FileManager {

    void writeJson(final File file, final JsonElement json);

    JsonElement parse(final File file);

    File getFile(final String alias);

    void createFile(final String alias, final File file);

    File getClientDirectory();

}
