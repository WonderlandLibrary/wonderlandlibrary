/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package viaversion.viaversion.api.minecraft.nbt;

import com.github.steveice10.opennbt.tag.builtin.ByteArrayTag;
import com.github.steveice10.opennbt.tag.builtin.ByteTag;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.DoubleTag;
import com.github.steveice10.opennbt.tag.builtin.FloatTag;
import com.github.steveice10.opennbt.tag.builtin.IntArrayTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.LongArrayTag;
import com.github.steveice10.opennbt.tag.builtin.LongTag;
import com.github.steveice10.opennbt.tag.builtin.ShortTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;

import java.io.IOException;
import java.io.Writer;

/**
 * See https://github.com/KyoriPowered/adventure.
 */
/* package */ final class TagStringWriter implements AutoCloseable {
    private final Appendable out;
    private final String indent = "  "; // TODO: pretty-printing
    private int level;
    /**
     * Whether a {@link Tokens#VALUE_SEPARATOR} needs to be printed before the beginning of the next object.
     */
    private boolean needsSeparator;

    public TagStringWriter(Appendable out) {
        this.out = out;
    }

    // NBT-specific

    public TagStringWriter writeTag(Tag tag) throws IOException {
        if (tag instanceof CompoundTag) {
            return writeCompound((CompoundTag) tag);
        } else if (tag instanceof ListTag) {
            return writeList((ListTag) tag);
        } else if (tag instanceof ByteArrayTag) {
            return writeByteArray((ByteArrayTag) tag);
        } else if (tag instanceof IntArrayTag) {
            return writeIntArray((IntArrayTag) tag);
        } else if (tag instanceof LongArrayTag) {
            return writeLongArray((LongArrayTag) tag);
        } else if (tag instanceof StringTag) {
            return value(((StringTag) tag).getValue(), Tokens.EOF);
        } else if (tag instanceof ByteTag) {
            return value(Byte.toString(((ByteTag) tag).getValue()), Tokens.TYPE_BYTE);
        } else if (tag instanceof ShortTag) {
            return value(Short.toString(((ShortTag) tag).getValue()), Tokens.TYPE_SHORT);
        } else if (tag instanceof IntTag) {
            return value(Integer.toString(((IntTag) tag).getValue()), Tokens.TYPE_INT);
        } else if (tag instanceof LongTag) {
            return value(Long.toString(((LongTag) tag).getValue()), Tokens.TYPE_LONG);
        } else if (tag instanceof FloatTag) {
            return value(Float.toString(((FloatTag) tag).getValue()), Tokens.TYPE_FLOAT);
        } else if (tag instanceof DoubleTag) {
            return value(Double.toString(((DoubleTag) tag).getValue()), Tokens.TYPE_DOUBLE);
        } else {
            throw new IOException("Unknown tag type: " + tag.getClass().getCanonicalName());
            // unknown!
        }
    }

    private TagStringWriter writeCompound(CompoundTag tag) throws IOException {
        beginCompound();
        for (Tag t : tag) {
            key(t.getName());
            writeTag(t);
        }
        endCompound();
        return this;
    }

    private TagStringWriter writeList(ListTag tag) throws IOException {
        beginList();
        for (Tag el : tag) {
            printAndResetSeparator();
            writeTag(el);
        }
        endList();
        return this;
    }

    private TagStringWriter writeByteArray(ByteArrayTag tag) throws IOException {
        beginArray(Tokens.TYPE_BYTE);

        byte[] value = tag.getValue();
        for (int i = 0, length = value.length; i < length; i++) {
            printAndResetSeparator();
            value(Byte.toString(value[i]), Tokens.TYPE_BYTE);
        }
        endArray();
        return this;
    }

    private TagStringWriter writeIntArray(IntArrayTag tag) throws IOException {
        beginArray(Tokens.TYPE_INT);

        int[] value = tag.getValue();
        for (int i = 0, length = value.length; i < length; i++) {
            printAndResetSeparator();
            value(Integer.toString(value[i]), Tokens.TYPE_INT);
        }
        endArray();
        return this;
    }

    private TagStringWriter writeLongArray(LongArrayTag tag) throws IOException {
        beginArray(Tokens.TYPE_LONG);

        long[] value = tag.getValue();
        for (int i = 0, length = value.length; i < length; i++) {
            printAndResetSeparator();
            value(Long.toString(value[i]), Tokens.TYPE_LONG);
        }
        endArray();
        return this;
    }

    // Value types

    public TagStringWriter beginCompound() throws IOException {
        printAndResetSeparator();
        this.level++;
        out.append(Tokens.COMPOUND_BEGIN);
        return this;
    }

    public TagStringWriter endCompound() throws IOException {
        out.append(Tokens.COMPOUND_END);
        this.level--;
        this.needsSeparator = true;
        return this;
    }

    public TagStringWriter key(String key) throws IOException {
        printAndResetSeparator();
        writeMaybeQuoted(key, false);
        out.append(Tokens.COMPOUND_KEY_TERMINATOR); // TODO: spacing/pretty-printing
        return this;
    }

    public TagStringWriter value(String value, char valueType) throws IOException {
        if (valueType == Tokens.EOF) { // string doesn't have its type
            writeMaybeQuoted(value, true);
        } else {
            out.append(value);
            if (valueType != Tokens.TYPE_INT) {
                out.append(valueType);
            }
        }
        this.needsSeparator = true;
        return this;
    }

    public TagStringWriter beginList() throws IOException {
        printAndResetSeparator();
        this.level++;
        out.append(Tokens.ARRAY_BEGIN);
        return this;
    }

    public TagStringWriter endList() throws IOException {
        out.append(Tokens.ARRAY_END);
        this.level--;
        this.needsSeparator = true;
        return this;
    }

    private TagStringWriter beginArray(char type) throws IOException {
        beginList()
                .out.append(type)
                .append(Tokens.ARRAY_SIGNATURE_SEPARATOR);
        return this;
    }

    private TagStringWriter endArray() throws IOException {
        return endList();
    }

    private void writeMaybeQuoted(String content, boolean requireQuotes) throws IOException {
        if (!requireQuotes) {
            for (int i = 0; i < content.length(); ++i) {
                if (!Tokens.id(content.charAt(i))) {
                    requireQuotes = true;
                    break;
                }
            }
        }
        if (requireQuotes) { // TODO: single quotes
            out.append(Tokens.DOUBLE_QUOTE);
            out.append(escape(content, Tokens.DOUBLE_QUOTE));
            out.append(Tokens.DOUBLE_QUOTE);
        } else {
            out.append(content);
        }
    }

    private static String escape(String content, char quoteChar) {
        StringBuilder output = new StringBuilder(content.length());
        for (int i = 0; i < content.length(); ++i) {
            char c = content.charAt(i);
            if (c == quoteChar || c == '\\') {
                output.append(Tokens.ESCAPE_MARKER);
            }
            output.append(c);
        }
        return output.toString();
    }

    private void printAndResetSeparator() throws IOException {
        if (needsSeparator) {
            out.append(Tokens.VALUE_SEPARATOR);
            this.needsSeparator = false;
        }
    }


    @Override
    public void close() throws IOException {
        if (level != 0) {
            throw new IllegalStateException("Document finished with unbalanced start and end objects");
        }
        if (out instanceof Writer) {
            ((Writer) out).flush();
        }
    }
}
