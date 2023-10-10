/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.io;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

@GwtCompatible(emulated=true)
final class GwtWorkarounds {
    private GwtWorkarounds() {
    }

    @GwtIncompatible(value="Reader")
    static CharInput asCharInput(final Reader reader) {
        Preconditions.checkNotNull(reader);
        return new CharInput(){

            @Override
            public int read() throws IOException {
                return reader.read();
            }

            @Override
            public void close() throws IOException {
                reader.close();
            }
        };
    }

    static CharInput asCharInput(final CharSequence chars) {
        Preconditions.checkNotNull(chars);
        return new CharInput(){
            int index = 0;

            @Override
            public int read() {
                if (this.index < chars.length()) {
                    return chars.charAt(this.index++);
                }
                return -1;
            }

            @Override
            public void close() {
                this.index = chars.length();
            }
        };
    }

    @GwtIncompatible(value="InputStream")
    static InputStream asInputStream(final ByteInput input) {
        Preconditions.checkNotNull(input);
        return new InputStream(){

            @Override
            public int read() throws IOException {
                return input.read();
            }

            @Override
            public int read(byte[] b2, int off, int len) throws IOException {
                Preconditions.checkNotNull(b2);
                Preconditions.checkPositionIndexes(off, off + len, b2.length);
                if (len == 0) {
                    return 0;
                }
                int firstByte = this.read();
                if (firstByte == -1) {
                    return -1;
                }
                b2[off] = (byte)firstByte;
                for (int dst = 1; dst < len; ++dst) {
                    int readByte = this.read();
                    if (readByte == -1) {
                        return dst;
                    }
                    b2[off + dst] = (byte)readByte;
                }
                return len;
            }

            @Override
            public void close() throws IOException {
                input.close();
            }
        };
    }

    @GwtIncompatible(value="OutputStream")
    static OutputStream asOutputStream(final ByteOutput output) {
        Preconditions.checkNotNull(output);
        return new OutputStream(){

            @Override
            public void write(int b2) throws IOException {
                output.write((byte)b2);
            }

            @Override
            public void flush() throws IOException {
                output.flush();
            }

            @Override
            public void close() throws IOException {
                output.close();
            }
        };
    }

    @GwtIncompatible(value="Writer")
    static CharOutput asCharOutput(final Writer writer) {
        Preconditions.checkNotNull(writer);
        return new CharOutput(){

            @Override
            public void write(char c2) throws IOException {
                writer.append(c2);
            }

            @Override
            public void flush() throws IOException {
                writer.flush();
            }

            @Override
            public void close() throws IOException {
                writer.close();
            }
        };
    }

    static CharOutput stringBuilderOutput(int initialSize) {
        final StringBuilder builder = new StringBuilder(initialSize);
        return new CharOutput(){

            @Override
            public void write(char c2) {
                builder.append(c2);
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }

            public String toString() {
                return builder.toString();
            }
        };
    }

    static interface CharOutput {
        public void write(char var1) throws IOException;

        public void flush() throws IOException;

        public void close() throws IOException;
    }

    static interface ByteOutput {
        public void write(byte var1) throws IOException;

        public void flush() throws IOException;

        public void close() throws IOException;
    }

    static interface ByteInput {
        public int read() throws IOException;

        public void close() throws IOException;
    }

    static interface CharInput {
        public int read() throws IOException;

        public void close() throws IOException;
    }
}

