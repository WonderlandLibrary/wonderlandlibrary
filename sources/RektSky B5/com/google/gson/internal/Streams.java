/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;

public final class Streams {
    private Streams() {
        throw new UnsupportedOperationException();
    }

    public static JsonElement parse(JsonReader reader) throws JsonParseException {
        boolean isEmpty = true;
        try {
            reader.peek();
            isEmpty = false;
            return TypeAdapters.JSON_ELEMENT.read(reader);
        }
        catch (EOFException e2) {
            if (isEmpty) {
                return JsonNull.INSTANCE;
            }
            throw new JsonSyntaxException(e2);
        }
        catch (MalformedJsonException e3) {
            throw new JsonSyntaxException(e3);
        }
        catch (IOException e4) {
            throw new JsonIOException(e4);
        }
        catch (NumberFormatException e5) {
            throw new JsonSyntaxException(e5);
        }
    }

    public static void write(JsonElement element, JsonWriter writer) throws IOException {
        TypeAdapters.JSON_ELEMENT.write(writer, element);
    }

    public static Writer writerForAppendable(Appendable appendable) {
        return appendable instanceof Writer ? (Writer)appendable : new AppendableWriter(appendable);
    }

    private static final class AppendableWriter
    extends Writer {
        private final Appendable appendable;
        private final CurrentWrite currentWrite = new CurrentWrite();

        AppendableWriter(Appendable appendable) {
            this.appendable = appendable;
        }

        @Override
        public void write(char[] chars, int offset, int length) throws IOException {
            this.currentWrite.chars = chars;
            this.appendable.append(this.currentWrite, offset, offset + length);
        }

        @Override
        public void write(int i2) throws IOException {
            this.appendable.append((char)i2);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }

        static class CurrentWrite
        implements CharSequence {
            char[] chars;

            CurrentWrite() {
            }

            @Override
            public int length() {
                return this.chars.length;
            }

            @Override
            public char charAt(int i2) {
                return this.chars[i2];
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(this.chars, start, end - start);
            }
        }
    }
}

