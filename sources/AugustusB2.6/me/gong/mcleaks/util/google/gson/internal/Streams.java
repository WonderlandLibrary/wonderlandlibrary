// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.gson.internal;

import java.io.Writer;
import me.gong.mcleaks.util.google.gson.stream.JsonWriter;
import me.gong.mcleaks.util.google.gson.JsonParseException;
import java.io.IOException;
import me.gong.mcleaks.util.google.gson.JsonIOException;
import me.gong.mcleaks.util.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import me.gong.mcleaks.util.google.gson.JsonSyntaxException;
import me.gong.mcleaks.util.google.gson.JsonNull;
import me.gong.mcleaks.util.google.gson.internal.bind.TypeAdapters;
import me.gong.mcleaks.util.google.gson.JsonElement;
import me.gong.mcleaks.util.google.gson.stream.JsonReader;

public final class Streams
{
    private Streams() {
        throw new UnsupportedOperationException();
    }
    
    public static JsonElement parse(final JsonReader reader) throws JsonParseException {
        boolean isEmpty = true;
        try {
            reader.peek();
            isEmpty = false;
            return TypeAdapters.JSON_ELEMENT.read(reader);
        }
        catch (EOFException e) {
            if (isEmpty) {
                return JsonNull.INSTANCE;
            }
            throw new JsonSyntaxException(e);
        }
        catch (MalformedJsonException e2) {
            throw new JsonSyntaxException(e2);
        }
        catch (IOException e3) {
            throw new JsonIOException(e3);
        }
        catch (NumberFormatException e4) {
            throw new JsonSyntaxException(e4);
        }
    }
    
    public static void write(final JsonElement element, final JsonWriter writer) throws IOException {
        TypeAdapters.JSON_ELEMENT.write(writer, element);
    }
    
    public static Writer writerForAppendable(final Appendable appendable) {
        return (appendable instanceof Writer) ? ((Writer)appendable) : new AppendableWriter(appendable);
    }
    
    private static final class AppendableWriter extends Writer
    {
        private final Appendable appendable;
        private final CurrentWrite currentWrite;
        
        AppendableWriter(final Appendable appendable) {
            this.currentWrite = new CurrentWrite();
            this.appendable = appendable;
        }
        
        @Override
        public void write(final char[] chars, final int offset, final int length) throws IOException {
            this.currentWrite.chars = chars;
            this.appendable.append(this.currentWrite, offset, offset + length);
        }
        
        @Override
        public void write(final int i) throws IOException {
            this.appendable.append((char)i);
        }
        
        @Override
        public void flush() {
        }
        
        @Override
        public void close() {
        }
        
        static class CurrentWrite implements CharSequence
        {
            char[] chars;
            
            @Override
            public int length() {
                return this.chars.length;
            }
            
            @Override
            public char charAt(final int i) {
                return this.chars[i];
            }
            
            @Override
            public CharSequence subSequence(final int start, final int end) {
                return new String(this.chars, start, end - start);
            }
        }
    }
}
