// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.util.List;
import java.util.ArrayList;

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
        this.buffer = buffer;
    }
    
    public CompoundBinaryTag compound() throws StringTagParseException {
        this.buffer.expect('{');
        if (this.buffer.takeIf('}')) {
            return CompoundBinaryTag.empty();
        }
        final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
        while (this.buffer.hasMore()) {
            builder.put(this.key(), this.tag());
            if (this.separatorOrCompleteWith('}')) {
                return builder.build();
            }
        }
        throw this.buffer.makeError("Unterminated compound tag!");
    }
    
    public ListBinaryTag list() throws StringTagParseException {
        final ListBinaryTag.Builder<BinaryTag> builder = ListBinaryTag.builder();
        this.buffer.expect('[');
        final boolean prefixedIndex = this.acceptLegacy && this.buffer.peek() == '0' && this.buffer.peek(1) == ':';
        if (!prefixedIndex && this.buffer.takeIf(']')) {
            return ListBinaryTag.empty();
        }
        while (this.buffer.hasMore()) {
            if (prefixedIndex) {
                this.buffer.takeUntil(':');
            }
            final BinaryTag next = this.tag();
            builder.add(next);
            if (this.separatorOrCompleteWith(']')) {
                return builder.build();
            }
        }
        throw this.buffer.makeError("Reached end of file without end of list tag!");
    }
    
    public BinaryTag array(char elementType) throws StringTagParseException {
        this.buffer.expect('[').expect(elementType).expect(';');
        elementType = Character.toLowerCase(elementType);
        if (elementType == 'b') {
            return ByteArrayBinaryTag.of(this.byteArray());
        }
        if (elementType == 'i') {
            return IntArrayBinaryTag.of(this.intArray());
        }
        if (elementType == 'l') {
            return LongArrayBinaryTag.of(this.longArray());
        }
        throw this.buffer.makeError("Type " + elementType + " is not a valid element type in an array!");
    }
    
    private byte[] byteArray() throws StringTagParseException {
        if (this.buffer.takeIf(']')) {
            return TagStringReader.EMPTY_BYTE_ARRAY;
        }
        final List<Byte> bytes = new ArrayList<Byte>();
        while (this.buffer.hasMore()) {
            final CharSequence value = this.buffer.skipWhitespace().takeUntil('b');
            try {
                bytes.add(Byte.valueOf(value.toString()));
            }
            catch (NumberFormatException ex) {
                throw this.buffer.makeError("All elements of a byte array must be bytes!");
            }
            if (this.separatorOrCompleteWith(']')) {
                final byte[] result = new byte[bytes.size()];
                for (int i = 0; i < bytes.size(); ++i) {
                    result[i] = bytes.get(i);
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
            final BinaryTag value = this.tag();
            if (!(value instanceof IntBinaryTag)) {
                throw this.buffer.makeError("All elements of an int array must be ints!");
            }
            builder.add(((IntBinaryTag)value).intValue());
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
    
    public BinaryTag tag() throws StringTagParseException {
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
                    return StringBinaryTag.of(unescape(this.buffer.takeUntil(startToken).toString()));
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
    
    private BinaryTag scalar() {
        final StringBuilder builder = new StringBuilder();
        boolean possiblyNumeric = true;
        while (this.buffer.hasMore()) {
            final char current = this.buffer.peek();
            if (possiblyNumeric && !Tokens.numeric(current) && builder.length() != 0) {
                BinaryTag result = null;
                try {
                    switch (Character.toLowerCase(current)) {
                        case 'b': {
                            result = ByteBinaryTag.of(Byte.parseByte(builder.toString()));
                            break;
                        }
                        case 's': {
                            result = ShortBinaryTag.of(Short.parseShort(builder.toString()));
                            break;
                        }
                        case 'i': {
                            result = IntBinaryTag.of(Integer.parseInt(builder.toString()));
                            break;
                        }
                        case 'l': {
                            result = LongBinaryTag.of(Long.parseLong(builder.toString()));
                            break;
                        }
                        case 'f': {
                            result = FloatBinaryTag.of(Float.parseFloat(builder.toString()));
                            break;
                        }
                        case 'd': {
                            result = DoubleBinaryTag.of(Double.parseDouble(builder.toString()));
                            break;
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
            }
            else {
                if (!Tokens.id(current)) {
                    break;
                }
                builder.append(this.buffer.take());
            }
        }
        final String built = builder.toString();
        if (possiblyNumeric) {
            try {
                return IntBinaryTag.of(Integer.parseInt(built));
            }
            catch (NumberFormatException ex2) {
                try {
                    return DoubleBinaryTag.of(Double.parseDouble(built));
                }
                catch (NumberFormatException ex3) {}
            }
        }
        if (built.equalsIgnoreCase("true")) {
            return ByteBinaryTag.ONE;
        }
        if (built.equalsIgnoreCase("false")) {
            return ByteBinaryTag.ZERO;
        }
        return StringBinaryTag.of(built);
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
