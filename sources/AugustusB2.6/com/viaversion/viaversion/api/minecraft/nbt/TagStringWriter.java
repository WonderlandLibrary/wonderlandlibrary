// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.nbt;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.FloatTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ShortTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;

final class TagStringWriter implements AutoCloseable
{
    private final Appendable out;
    private int level;
    private boolean needsSeparator;
    
    public TagStringWriter(final Appendable out) {
        this.out = out;
    }
    
    public TagStringWriter writeTag(final Tag tag) throws IOException {
        if (tag instanceof CompoundTag) {
            return this.writeCompound((CompoundTag)tag);
        }
        if (tag instanceof ListTag) {
            return this.writeList((ListTag)tag);
        }
        if (tag instanceof ByteArrayTag) {
            return this.writeByteArray((ByteArrayTag)tag);
        }
        if (tag instanceof IntArrayTag) {
            return this.writeIntArray((IntArrayTag)tag);
        }
        if (tag instanceof LongArrayTag) {
            return this.writeLongArray((LongArrayTag)tag);
        }
        if (tag instanceof StringTag) {
            return this.value(((StringTag)tag).getValue(), '\0');
        }
        if (tag instanceof ByteTag) {
            return this.value(Byte.toString(((NumberTag)tag).asByte()), 'b');
        }
        if (tag instanceof ShortTag) {
            return this.value(Short.toString(((NumberTag)tag).asShort()), 's');
        }
        if (tag instanceof IntTag) {
            return this.value(Integer.toString(((NumberTag)tag).asInt()), 'i');
        }
        if (tag instanceof LongTag) {
            return this.value(Long.toString(((NumberTag)tag).asLong()), Character.toUpperCase('l'));
        }
        if (tag instanceof FloatTag) {
            return this.value(Float.toString(((NumberTag)tag).asFloat()), 'f');
        }
        if (tag instanceof DoubleTag) {
            return this.value(Double.toString(((NumberTag)tag).asDouble()), 'd');
        }
        throw new IOException("Unknown tag type: " + tag.getClass().getSimpleName());
    }
    
    private TagStringWriter writeCompound(final CompoundTag tag) throws IOException {
        this.beginCompound();
        for (final Map.Entry<String, Tag> entry : tag.entrySet()) {
            this.key(entry.getKey());
            this.writeTag(entry.getValue());
        }
        this.endCompound();
        return this;
    }
    
    private TagStringWriter writeList(final ListTag tag) throws IOException {
        this.beginList();
        for (final Tag el : tag) {
            this.printAndResetSeparator();
            this.writeTag(el);
        }
        this.endList();
        return this;
    }
    
    private TagStringWriter writeByteArray(final ByteArrayTag tag) throws IOException {
        this.beginArray('b');
        final byte[] value = tag.getValue();
        for (int i = 0, length = value.length; i < length; ++i) {
            this.printAndResetSeparator();
            this.value(Byte.toString(value[i]), 'b');
        }
        this.endArray();
        return this;
    }
    
    private TagStringWriter writeIntArray(final IntArrayTag tag) throws IOException {
        this.beginArray('i');
        final int[] value = tag.getValue();
        for (int i = 0, length = value.length; i < length; ++i) {
            this.printAndResetSeparator();
            this.value(Integer.toString(value[i]), 'i');
        }
        this.endArray();
        return this;
    }
    
    private TagStringWriter writeLongArray(final LongArrayTag tag) throws IOException {
        this.beginArray('l');
        final long[] value = tag.getValue();
        for (int i = 0, length = value.length; i < length; ++i) {
            this.printAndResetSeparator();
            this.value(Long.toString(value[i]), 'l');
        }
        this.endArray();
        return this;
    }
    
    public TagStringWriter beginCompound() throws IOException {
        this.printAndResetSeparator();
        ++this.level;
        this.out.append('{');
        return this;
    }
    
    public TagStringWriter endCompound() throws IOException {
        this.out.append('}');
        --this.level;
        this.needsSeparator = true;
        return this;
    }
    
    public TagStringWriter key(final String key) throws IOException {
        this.printAndResetSeparator();
        this.writeMaybeQuoted(key, false);
        this.out.append(':');
        return this;
    }
    
    public TagStringWriter value(final String value, final char valueType) throws IOException {
        if (valueType == '\0') {
            this.writeMaybeQuoted(value, true);
        }
        else {
            this.out.append(value);
            if (valueType != 'i') {
                this.out.append(valueType);
            }
        }
        this.needsSeparator = true;
        return this;
    }
    
    public TagStringWriter beginList() throws IOException {
        this.printAndResetSeparator();
        ++this.level;
        this.out.append('[');
        return this;
    }
    
    public TagStringWriter endList() throws IOException {
        this.out.append(']');
        --this.level;
        this.needsSeparator = true;
        return this;
    }
    
    private TagStringWriter beginArray(final char type) throws IOException {
        this.beginList().out.append(type).append(';');
        return this;
    }
    
    private TagStringWriter endArray() throws IOException {
        return this.endList();
    }
    
    private void writeMaybeQuoted(final String content, boolean requireQuotes) throws IOException {
        if (!requireQuotes) {
            for (int i = 0; i < content.length(); ++i) {
                if (!Tokens.id(content.charAt(i))) {
                    requireQuotes = true;
                    break;
                }
            }
        }
        if (requireQuotes) {
            this.out.append('\"');
            this.out.append(escape(content, '\"'));
            this.out.append('\"');
        }
        else {
            this.out.append(content);
        }
    }
    
    private static String escape(final String content, final char quoteChar) {
        final StringBuilder output = new StringBuilder(content.length());
        for (int i = 0; i < content.length(); ++i) {
            final char c = content.charAt(i);
            if (c == quoteChar || c == '\\') {
                output.append('\\');
            }
            output.append(c);
        }
        return output.toString();
    }
    
    private void printAndResetSeparator() throws IOException {
        if (this.needsSeparator) {
            this.out.append(',');
            this.needsSeparator = false;
        }
    }
    
    @Override
    public void close() throws IOException {
        if (this.level != 0) {
            throw new IllegalStateException("Document finished with unbalanced start and end objects");
        }
        if (this.out instanceof Writer) {
            ((Writer)this.out).flush();
        }
    }
}
