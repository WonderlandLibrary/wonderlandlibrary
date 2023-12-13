// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.io;

import java.io.Closeable;
import java.io.Writer;
import java.io.EOFException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.CharBuffer;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;

@Beta
public final class CharStreams
{
    private static final int BUF_SIZE = 2048;
    
    private CharStreams() {
    }
    
    public static long copy(final Readable from, final Appendable to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        final CharBuffer buf = CharBuffer.allocate(2048);
        long total = 0L;
        while (from.read(buf) != -1) {
            buf.flip();
            to.append(buf);
            total += buf.remaining();
            buf.clear();
        }
        return total;
    }
    
    public static String toString(final Readable r) throws IOException {
        return toStringBuilder(r).toString();
    }
    
    private static StringBuilder toStringBuilder(final Readable r) throws IOException {
        final StringBuilder sb = new StringBuilder();
        copy(r, sb);
        return sb;
    }
    
    public static List<String> readLines(final Readable r) throws IOException {
        final List<String> result = new ArrayList<String>();
        final LineReader lineReader = new LineReader(r);
        String line;
        while ((line = lineReader.readLine()) != null) {
            result.add(line);
        }
        return result;
    }
    
    public static <T> T readLines(final Readable readable, final LineProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(readable);
        Preconditions.checkNotNull(processor);
        final LineReader lineReader = new LineReader(readable);
        String line;
        while ((line = lineReader.readLine()) != null && processor.processLine(line)) {}
        return processor.getResult();
    }
    
    public static void skipFully(final Reader reader, long n) throws IOException {
        Preconditions.checkNotNull(reader);
        while (n > 0L) {
            final long amt = reader.skip(n);
            if (amt == 0L) {
                if (reader.read() == -1) {
                    throw new EOFException();
                }
                --n;
            }
            else {
                n -= amt;
            }
        }
    }
    
    public static Writer nullWriter() {
        return NullWriter.INSTANCE;
    }
    
    public static Writer asWriter(final Appendable target) {
        if (target instanceof Writer) {
            return (Writer)target;
        }
        return new AppendableWriter(target);
    }
    
    static Reader asReader(final Readable readable) {
        Preconditions.checkNotNull(readable);
        if (readable instanceof Reader) {
            return (Reader)readable;
        }
        return new Reader() {
            @Override
            public int read(final char[] cbuf, final int off, final int len) throws IOException {
                return this.read(CharBuffer.wrap(cbuf, off, len));
            }
            
            @Override
            public int read(final CharBuffer target) throws IOException {
                return readable.read(target);
            }
            
            @Override
            public void close() throws IOException {
                if (readable instanceof Closeable) {
                    ((Closeable)readable).close();
                }
            }
        };
    }
    
    private static final class NullWriter extends Writer
    {
        private static final NullWriter INSTANCE;
        
        @Override
        public void write(final int c) {
        }
        
        @Override
        public void write(final char[] cbuf) {
            Preconditions.checkNotNull(cbuf);
        }
        
        @Override
        public void write(final char[] cbuf, final int off, final int len) {
            Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
        }
        
        @Override
        public void write(final String str) {
            Preconditions.checkNotNull(str);
        }
        
        @Override
        public void write(final String str, final int off, final int len) {
            Preconditions.checkPositionIndexes(off, off + len, str.length());
        }
        
        @Override
        public Writer append(final CharSequence csq) {
            Preconditions.checkNotNull(csq);
            return this;
        }
        
        @Override
        public Writer append(final CharSequence csq, final int start, final int end) {
            Preconditions.checkPositionIndexes(start, end, csq.length());
            return this;
        }
        
        @Override
        public Writer append(final char c) {
            return this;
        }
        
        @Override
        public void flush() {
        }
        
        @Override
        public void close() {
        }
        
        @Override
        public String toString() {
            return "CharStreams.nullWriter()";
        }
        
        static {
            INSTANCE = new NullWriter();
        }
    }
}
