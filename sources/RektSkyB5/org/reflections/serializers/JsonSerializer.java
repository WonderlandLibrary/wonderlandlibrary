/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import org.reflections.Reflections;
import org.reflections.serializers.Serializer;
import org.reflections.util.Utils;

public class JsonSerializer
implements Serializer {
    private Gson gson;

    @Override
    public Reflections read(InputStream inputStream) {
        return this.getGson().fromJson((Reader)new InputStreamReader(inputStream), Reflections.class);
    }

    @Override
    public File save(Reflections reflections, String filename) {
        try {
            File file = Utils.prepareFile(filename);
            Files.write(file.toPath(), this.toString(reflections).getBytes(Charset.defaultCharset()), new OpenOption[0]);
            return file;
        }
        catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override
    public String toString(Reflections reflections) {
        return this.getGson().toJson(reflections);
    }

    private Gson getGson() {
        if (this.gson == null) {
            this.gson = new GsonBuilder().setPrettyPrinting().create();
        }
        return this.gson;
    }
}

