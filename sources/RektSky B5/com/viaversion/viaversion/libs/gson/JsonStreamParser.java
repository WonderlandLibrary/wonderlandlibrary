/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.gson;

import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonIOException;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.internal.Streams;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class JsonStreamParser
implements Iterator<JsonElement> {
    private final JsonReader parser;
    private final Object lock;

    public JsonStreamParser(String json) {
        this(new StringReader(json));
    }

    public JsonStreamParser(Reader reader) {
        this.parser = new JsonReader(reader);
        this.parser.setLenient(true);
        this.lock = new Object();
    }

    @Override
    public JsonElement next() throws JsonParseException {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return Streams.parse(this.parser);
        }
        catch (StackOverflowError e2) {
            throw new JsonParseException("Failed parsing JSON source to Json", e2);
        }
        catch (OutOfMemoryError e3) {
            throw new JsonParseException("Failed parsing JSON source to Json", e3);
        }
        catch (JsonParseException e4) {
            throw e4.getCause() instanceof EOFException ? new NoSuchElementException() : e4;
        }
    }

    @Override
    public boolean hasNext() {
        Object object = this.lock;
        synchronized (object) {
            try {
                return this.parser.peek() != JsonToken.END_DOCUMENT;
            }
            catch (MalformedJsonException e2) {
                throw new JsonSyntaxException(e2);
            }
            catch (IOException e3) {
                throw new JsonIOException(e3);
            }
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

