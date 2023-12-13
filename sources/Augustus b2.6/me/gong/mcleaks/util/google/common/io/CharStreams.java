// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

import java.io.Writer;
import java.io.EOFException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.nio.CharBuffer;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public final class CharStreams
{
    static CharBuffer createBuffer() {
        return CharBuffer.allocate(2048);
    }
    
    private CharStreams() {
    }
    
    @CanIgnoreReturnValue
    public static long copy(final Readable from, final Appendable to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        final CharBuffer buf = createBuffer();
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
    
    @CanIgnoreReturnValue
    public static <T> T readLines(final Readable readable, final LineProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(readable);
        Preconditions.checkNotNull(processor);
        final LineReader lineReader = new LineReader(readable);
        String line;
        while ((line = lineReader.readLine()) != null && processor.processLine(line)) {}
        return processor.getResult();
    }
    
    @CanIgnoreReturnValue
    public static long exhaust(final Readable readable) throws IOException {
        long total = 0L;
        final CharBuffer buf = createBuffer();
        long read;
        while ((read = readable.read(buf)) != -1L) {
            total += read;
            buf.clear();
        }
        return total;
    }
    
    public static void skipFully(final Reader reader, long n) throws IOException {
        Preconditions.checkNotNull(reader);
        while (n > 0L) {
            final long amt = reader.skip(n);
            if (amt == 0L) {
                throw new EOFException();
            }
            n -= amt;
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
