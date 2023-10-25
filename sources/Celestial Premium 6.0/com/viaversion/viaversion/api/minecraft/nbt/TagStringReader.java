/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api.minecraft.nbt;

import com.viaversion.viaversion.api.minecraft.nbt.CharBuffer;
import com.viaversion.viaversion.api.minecraft.nbt.StringTagParseException;
import com.viaversion.viaversion.api.minecraft.nbt.Tokens;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.FloatTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ShortTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.ArrayList;
import java.util.stream.IntStream;

final class TagStringReader {
    private final CharBuffer buffer;

    public TagStringReader(CharBuffer buffer) {
        this.buffer = buffer;
    }

    public CompoundTag compound() throws StringTagParseException {
        this.buffer.expect('{');
        CompoundTag compoundTag = new CompoundTag();
        if (this.buffer.peek() == '}') {
            this.buffer.take();
            return compoundTag;
        }
        while (this.buffer.hasMore()) {
            String key = this.key();
            Tag tag = this.tag();
            compoundTag.put(key, tag);
            if (!this.separatorOrCompleteWith('}')) continue;
            return compoundTag;
        }
        throw this.buffer.makeError("Unterminated compound tag!");
    }

    public ListTag list() throws StringTagParseException {
        boolean prefixedIndex;
        ListTag listTag = new ListTag();
        this.buffer.expect('[');
        boolean bl = prefixedIndex = this.buffer.peek() == '0' && this.buffer.peek(1) == ':';
        while (this.buffer.hasMore()) {
            if (this.buffer.peek() == ']') {
                this.buffer.advance();
                return listTag;
            }
            if (prefixedIndex) {
                this.buffer.takeUntil(':');
            }
            Tag next = this.tag();
            listTag.add(next);
            if (!this.separatorOrCompleteWith(']')) continue;
            return listTag;
        }
        throw this.buffer.makeError("Reached end of file without end of list tag!");
    }

    public Tag array(char elementType) throws StringTagParseException {
        this.buffer.expect('[').expect(elementType).expect(';');
        if (elementType == 'B') {
            return new ByteArrayTag(this.byteArray());
        }
        if (elementType == 'I') {
            return new IntArrayTag(this.intArray());
        }
        if (elementType == 'L') {
            return new LongArrayTag(this.longArray());
        }
        throw this.buffer.makeError("Type " + elementType + " is not a valid element type in an array!");
    }

    private byte[] byteArray() throws StringTagParseException {
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        while (this.buffer.hasMore()) {
            CharSequence value = this.buffer.skipWhitespace().takeUntil('B');
            try {
                bytes.add(Byte.valueOf(value.toString()));
            }
            catch (NumberFormatException ex) {
                throw this.buffer.makeError("All elements of a byte array must be bytes!");
            }
            if (!this.separatorOrCompleteWith(']')) continue;
            byte[] result = new byte[bytes.size()];
            for (int i = 0; i < bytes.size(); ++i) {
                result[i] = (Byte)bytes.get(i);
            }
            return result;
        }
        throw this.buffer.makeError("Reached end of document without array close");
    }

    private int[] intArray() throws StringTagParseException {
        IntStream.Builder builder = IntStream.builder();
        while (this.buffer.hasMore()) {
            Tag value = this.tag();
            if (!(value instanceof IntTag)) {
                throw this.buffer.makeError("All elements of an int array must be ints!");
            }
            builder.add(((NumberTag)value).asInt());
            if (!this.separatorOrCompleteWith(']')) continue;
            return builder.build().toArray();
        }
        throw this.buffer.makeError("Reached end of document without array close");
    }

    private long[] longArray() throws StringTagParseException {
        ArrayList<Long> longs = new ArrayList<Long>();
        while (this.buffer.hasMore()) {
            CharSequence value = this.buffer.skipWhitespace().takeUntil('L');
            try {
                longs.add(Long.valueOf(value.toString()));
            }
            catch (NumberFormatException ex) {
                throw this.buffer.makeError("All elements of a long array must be longs!");
            }
            if (!this.separatorOrCompleteWith(']')) continue;
            long[] result = new long[longs.size()];
            for (int i = 0; i < longs.size(); ++i) {
                result[i] = (Long)longs.get(i);
            }
            return result;
        }
        throw this.buffer.makeError("Reached end of document without array close");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String key() throws StringTagParseException {
        this.buffer.skipWhitespace();
        char starChar = this.buffer.peek();
        try {
            if (starChar == '\'' || starChar == '\"') {
                String string = TagStringReader.unescape(this.buffer.takeUntil(this.buffer.take()).toString());
                return string;
            }
            StringBuilder builder = new StringBuilder();
            while (this.buffer.peek() != ':') {
                builder.append(this.buffer.take());
            }
            String string = builder.toString();
            return string;
        }
        finally {
            this.buffer.expect(':');
        }
    }

    public Tag tag() throws StringTagParseException {
        char startToken = this.buffer.skipWhitespace().peek();
        switch (startToken) {
            case '{': {
                return this.compound();
            }
            case '[': {
                if (this.buffer.peek(2) == ';') {
                    return this.array(this.buffer.peek(1));
                }
                return this.list();
            }
            case '\"': 
            case '\'': {
                this.buffer.advance();
                return new StringTag(TagStringReader.unescape(this.buffer.takeUntil(startToken).toString()));
            }
        }
        return this.scalar();
    }

    private Tag scalar() {
        StringBuilder builder = new StringBuilder();
        boolean possiblyNumeric = true;
        while (this.buffer.hasMore()) {
            char current = this.buffer.peek();
            if (possiblyNumeric && !Tokens.numeric(current) && builder.length() != 0) {
                NumberTag result = null;
                try {
                    switch (Character.toUpperCase(current)) {
                        case 'B': {
                            result = new ByteTag(Byte.parseByte(builder.toString()));
                            break;
                        }
                        case 'S': {
                            result = new ShortTag(Short.parseShort(builder.toString()));
                            break;
                        }
                        case 'L': {
                            result = new LongTag(Long.parseLong(builder.toString()));
                            break;
                        }
                        case 'F': {
                            result = new FloatTag(Float.parseFloat(builder.toString()));
                            break;
                        }
                        case 'D': {
                            result = new DoubleTag(Double.parseDouble(builder.toString()));
                        }
                    }
                }
                catch (NumberFormatException ex) {
                    possiblyNumeric = false;
                }
                if (result != null) {
                    this.buffer.take();
                    return result;
                }
            }
            if (current == '\\') {
                this.buffer.advance();
                builder.append(this.buffer.take());
                continue;
            }
            if (!Tokens.id(current)) break;
            builder.append(this.buffer.take());
        }
        String built = builder.toString();
        if (possiblyNumeric) {
            try {
                return new IntTag(Integer.parseInt(built));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return new StringTag(built);
    }

    private boolean separatorOrCompleteWith(char endCharacter) throws StringTagParseException {
        if (this.buffer.skipWhitespace().peek() == endCharacter) {
            this.buffer.take();
            return true;
        }
        this.buffer.expect(',');
        if (this.buffer.skipWhitespace().peek() == endCharacter) {
            this.buffer.take();
            return true;
        }
        return false;
    }

    private static String unescape(String withEscapes) {
        int escapeIdx = withEscapes.indexOf(92);
        if (escapeIdx == -1) {
            return withEscapes;
        }
        int lastEscape = 0;
        StringBuilder output = new StringBuilder(withEscapes.length());
        do {
            output.append(withEscapes, lastEscape, escapeIdx);
        } while ((escapeIdx = withEscapes.indexOf(92, (lastEscape = escapeIdx + 1) + 1)) != -1);
        output.append(withEscapes.substring(lastEscape));
        return output.toString();
    }
}

