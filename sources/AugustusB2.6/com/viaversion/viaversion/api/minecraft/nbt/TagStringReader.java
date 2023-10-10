// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.nbt;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.FloatTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ShortTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import java.util.stream.LongStream;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import java.util.stream.IntStream;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;

final class TagStringReader
{
    private static final int MAX_DEPTH = 512;
    private static final byte[] EMPTY_BYTE_ARRAY;
    private static final int[] EMPTY_INT_ARRAY;
    private static final long[] EMPTY_LONG_ARRAY;
    private final CharBuffer buffer;
    private boolean acceptLegacy;
    private int depth;
    
    TagStringReader(final CharBuffer buffer) {
        this.acceptLegacy = true;
        this.buffer = buffer;
    }
    
    public CompoundTag compound() throws StringTagParseException {
        this.buffer.expect('{');
        final CompoundTag compoundTag = new CompoundTag();
        if (this.buffer.takeIf('}')) {
            return compoundTag;
        }
        while (this.buffer.hasMore()) {
            compoundTag.put(this.key(), this.tag());
            if (this.separatorOrCompleteWith('}')) {
                return compoundTag;
            }
        }
        throw this.buffer.makeError("Unterminated compound tag!");
    }
    
    public ListTag list() throws StringTagParseException {
        final ListTag listTag = new ListTag();
        this.buffer.expect('[');
        final boolean prefixedIndex = this.acceptLegacy && this.buffer.peek() == '0' && this.buffer.peek(1) == ':';
        if (!prefixedIndex && this.buffer.takeIf(']')) {
            return listTag;
        }
        while (this.buffer.hasMore()) {
            if (prefixedIndex) {
                this.buffer.takeUntil(':');
            }
            final Tag next = this.tag();
            listTag.add(next);
            if (this.separatorOrCompleteWith(']')) {
                return listTag;
            }
        }
        throw this.buffer.makeError("Reached end of file without end of list tag!");
    }
    
    public Tag array(char elementType) throws StringTagParseException {
        this.buffer.expect('[').expect(elementType).expect(';');
        elementType = Character.toLowerCase(elementType);
        if (elementType == 'b') {
            return new ByteArrayTag(this.byteArray());
        }
        if (elementType == 'i') {
            return new IntArrayTag(this.intArray());
        }
        if (elementType == 'l') {
            return new LongArrayTag(this.longArray());
        }
        throw this.buffer.makeError("Type " + elementType + " is not a valid element type in an array!");
    }
    
    private byte[] byteArray() throws StringTagParseException {
        if (this.buffer.takeIf(']')) {
            return TagStringReader.EMPTY_BYTE_ARRAY;
        }
        final IntList bytes = new IntArrayList();
        while (this.buffer.hasMore()) {
            final CharSequence value = this.buffer.skipWhitespace().takeUntil('b');
            try {
                bytes.add(Byte.parseByte(value.toString()));
            }
            catch (NumberFormatException ex) {
                throw this.buffer.makeError("All elements of a byte array must be bytes!");
            }
            if (this.separatorOrCompleteWith(']')) {
                final byte[] result = new byte[bytes.size()];
                for (int i = 0; i < bytes.size(); ++i) {
                    result[i] = (byte)bytes.getInt(i);
                }
                return result;
            }
        }
        throw this.buffer.makeError("Reached end of document without array close");
    }
    
    private int[] intArray() throws StringTagParseException {
        if (this.buffer.takeIf(']')) {
            return TagStringReader.EMPTY_INT_ARRAY;
        }
        final IntStream.Builder builder = IntStream.builder();
        while (this.buffer.hasMore()) {
            final Tag value = this.tag();
            if (!(value instanceof IntTag)) {
                throw this.buffer.makeError("All elements of an int array must be ints!");
            }
            builder.add(((NumberTag)value).asInt());
            if (this.separatorOrCompleteWith(']')) {
                return builder.build().toArray();
            }
        }
        throw this.buffer.makeError("Reached end of document without array close");
    }
    
    private long[] longArray() throws StringTagParseException {
        if (this.buffer.takeIf(']')) {
            return TagStringReader.EMPTY_LONG_ARRAY;
        }
        final LongStream.Builder longs = LongStream.builder();
        while (this.buffer.hasMore()) {
            final CharSequence value = this.buffer.skipWhitespace().takeUntil('l');
            try {
                longs.add(Long.parseLong(value.toString()));
            }
            catch (NumberFormatException ex) {
                throw this.buffer.makeError("All elements of a long array must be longs!");
            }
            if (this.separatorOrCompleteWith(']')) {
                return longs.build().toArray();
            }
        }
        throw this.buffer.makeError("Reached end of document without array close");
    }
    
    public String key() throws StringTagParseException {
        this.buffer.skipWhitespace();
        final char starChar = this.buffer.peek();
        try {
            if (starChar == '\'' || starChar == '\"') {
                return unescape(this.buffer.takeUntil(this.buffer.take()).toString());
            }
            final StringBuilder builder = new StringBuilder();
            while (this.buffer.hasMore()) {
                final char peek = this.buffer.peek();
                if (!Tokens.id(peek)) {
                    if (!this.acceptLegacy) {
                        break;
                    }
                    if (peek == '\\') {
                        this.buffer.take();
                    }
                    else {
                        if (peek == ':') {
                            break;
                        }
                        builder.append(this.buffer.take());
                    }
                }
                else {
                    builder.append(this.buffer.take());
                }
            }
            return builder.toString();
        }
        finally {
            this.buffer.expect(':');
        }
    }
    
    public Tag tag() throws StringTagParseException {
        if (this.depth++ > 512) {
            throw this.buffer.makeError("Exceeded maximum allowed depth of 512 when reading tag");
        }
        try {
            final char startToken = this.buffer.skipWhitespace().peek();
            switch (startToken) {
                case '{': {
                    return this.compound();
                }
                case '[': {
                    if (this.buffer.hasMore(2) && this.buffer.peek(2) == ';') {
                        return this.array(this.buffer.peek(1));
                    }
                    return this.list();
                }
                case '\"':
                case '\'': {
                    this.buffer.advance();
                    return new StringTag(unescape(this.buffer.takeUntil(startToken).toString()));
                }
                default: {
                    return this.scalar();
                }
            }
        }
        finally {
            --this.depth;
        }
    }
    
    private Tag scalar() {
        final StringBuilder builder = new StringBuilder();
        int noLongerNumericAt = -1;
        while (this.buffer.hasMore()) {
            char current = this.buffer.peek();
            if (current == '\\') {
                this.buffer.advance();
                current = this.buffer.take();
            }
            else {
                if (!Tokens.id(current)) {
                    break;
                }
                this.buffer.advance();
            }
            builder.append(current);
            if (noLongerNumericAt == -1 && !Tokens.numeric(current)) {
                noLongerNumericAt = builder.length();
            }
        }
        final int length = builder.length();
        final String built = builder.toString();
        if (noLongerNumericAt == length) {
            final char last = built.charAt(length - 1);
            try {
                switch (Character.toLowerCase(last)) {
                    case 'b': {
                        return new ByteTag(Byte.parseByte(built.substring(0, length - 1)));
                    }
                    case 's': {
                        return new ShortTag(Short.parseShort(built.substring(0, length - 1)));
                    }
                    case 'i': {
                        return new IntTag(Integer.parseInt(built.substring(0, length - 1)));
                    }
                    case 'l': {
                        return new LongTag(Long.parseLong(built.substring(0, length - 1)));
                    }
                    case 'f': {
                        final float floatValue = Float.parseFloat(built.substring(0, length - 1));
                        if (Float.isFinite(floatValue)) {
                            return new FloatTag(floatValue);
                        }
                        break;
                    }
                    case 'd': {
                        final double doubleValue = Double.parseDouble(built.substring(0, length - 1));
                        if (Double.isFinite(doubleValue)) {
                            return new DoubleTag(doubleValue);
                        }
                        break;
                    }
                }
            }
            catch (NumberFormatException ex2) {}
        }
        else if (noLongerNumericAt == -1) {
            try {
                return new IntTag(Integer.parseInt(built));
            }
            catch (NumberFormatException ex) {
                if (built.indexOf(46) != -1) {
                    try {
                        return new DoubleTag(Double.parseDouble(built));
                    }
                    catch (NumberFormatException ex3) {}
                }
            }
        }
        if (built.equalsIgnoreCase("true")) {
            return new ByteTag((byte)1);
        }
        if (built.equalsIgnoreCase("false")) {
            return new ByteTag((byte)0);
        }
        return new StringTag(built);
    }
    
    private boolean separatorOrCompleteWith(final char endCharacter) throws StringTagParseException {
        if (this.buffer.takeIf(endCharacter)) {
            return true;
        }
        this.buffer.expect(',');
        return this.buffer.takeIf(endCharacter);
    }
    
    private static String unescape(final String withEscapes) {
        int escapeIdx = withEscapes.indexOf(92);
        if (escapeIdx == -1) {
            return withEscapes;
        }
        int lastEscape = 0;
        final StringBuilder output = new StringBuilder(withEscapes.length());
        do {
            output.append(withEscapes, lastEscape, escapeIdx);
            lastEscape = escapeIdx + 1;
        } while ((escapeIdx = withEscapes.indexOf(92, lastEscape + 1)) != -1);
        output.append(withEscapes.substring(lastEscape));
        return output.toString();
    }
    
    public void legacy(final boolean acceptLegacy) {
        this.acceptLegacy = acceptLegacy;
    }
    
    static {
        EMPTY_BYTE_ARRAY = new byte[0];
        EMPTY_INT_ARRAY = new int[0];
        EMPTY_LONG_ARRAY = new long[0];
    }
}
