/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.io.AppendableWriter;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.Closer;
import com.google.common.io.InputSupplier;
import com.google.common.io.LineProcessor;
import com.google.common.io.LineReader;
import com.google.common.io.OutputSupplier;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Beta
public final class CharStreams {
    private static final int BUF_SIZE = 2048;

    private CharStreams() {
    }

    @Deprecated
    public static InputSupplier<StringReader> newReaderSupplier(String value) {
        return CharStreams.asInputSupplier(CharSource.wrap(value));
    }

    @Deprecated
    public static InputSupplier<InputStreamReader> newReaderSupplier(InputSupplier<? extends InputStream> in, Charset charset) {
        return CharStreams.asInputSupplier(ByteStreams.asByteSource(in).asCharSource(charset));
    }

    @Deprecated
    public static OutputSupplier<OutputStreamWriter> newWriterSupplier(OutputSupplier<? extends OutputStream> out, Charset charset) {
        return CharStreams.asOutputSupplier(ByteStreams.asByteSink(out).asCharSink(charset));
    }

    @Deprecated
    public static <W extends Appendable & Closeable> void write(CharSequence from, OutputSupplier<W> to) throws IOException {
        CharStreams.asCharSink(to).write(from);
    }

    @Deprecated
    public static <R extends Readable & Closeable, W extends Appendable & Closeable> long copy(InputSupplier<R> from, OutputSupplier<W> to) throws IOException {
        return CharStreams.asCharSource(from).copyTo(CharStreams.asCharSink(to));
    }

    @Deprecated
    public static <R extends Readable & Closeable> long copy(InputSupplier<R> from, Appendable to) throws IOException {
        return CharStreams.asCharSource(from).copyTo(to);
    }

    public static long copy(Readable from, Appendable to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        CharBuffer buf = CharBuffer.allocate(2048);
        long total = 0L;
        while (from.read(buf) != -1) {
            buf.flip();
            to.append(buf);
            total += (long)buf.remaining();
            buf.clear();
        }
        return total;
    }

    public static String toString(Readable r2) throws IOException {
        return CharStreams.toStringBuilder(r2).toString();
    }

    @Deprecated
    public static <R extends Readable & Closeable> String toString(InputSupplier<R> supplier) throws IOException {
        return CharStreams.asCharSource(supplier).read();
    }

    private static StringBuilder toStringBuilder(Readable r2) throws IOException {
        StringBuilder sb = new StringBuilder();
        CharStreams.copy(r2, (Appendable)sb);
        return sb;
    }

    @Deprecated
    public static <R extends Readable & Closeable> String readFirstLine(InputSupplier<R> supplier) throws IOException {
        return CharStreams.asCharSource(supplier).readFirstLine();
    }

    @Deprecated
    public static <R extends Readable & Closeable> List<String> readLines(InputSupplier<R> supplier) throws IOException {
        Closer closer = Closer.create();
        try {
            Readable r2 = (Readable)((Object)closer.register((Closeable)supplier.getInput()));
            List<String> list = CharStreams.readLines(r2);
            return list;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public static List<String> readLines(Readable r2) throws IOException {
        String line;
        ArrayList<String> result = new ArrayList<String>();
        LineReader lineReader = new LineReader(r2);
        while ((line = lineReader.readLine()) != null) {
            result.add(line);
        }
        return result;
    }

    public static <T> T readLines(Readable readable, LineProcessor<T> processor) throws IOException {
        String line;
        Preconditions.checkNotNull(readable);
        Preconditions.checkNotNull(processor);
        LineReader lineReader = new LineReader(readable);
        while ((line = lineReader.readLine()) != null && processor.processLine(line)) {
        }
        return processor.getResult();
    }

    @Deprecated
    public static <R extends Readable & Closeable, T> T readLines(InputSupplier<R> supplier, LineProcessor<T> callback) throws IOException {
        Preconditions.checkNotNull(supplier);
        Preconditions.checkNotNull(callback);
        Closer closer = Closer.create();
        try {
            Readable r2 = (Readable)((Object)closer.register((Closeable)supplier.getInput()));
            T t2 = CharStreams.readLines(r2, callback);
            return t2;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    @Deprecated
    public static InputSupplier<Reader> join(Iterable<? extends InputSupplier<? extends Reader>> suppliers) {
        Preconditions.checkNotNull(suppliers);
        Iterable<CharSource> sources = Iterables.transform(suppliers, new Function<InputSupplier<? extends Reader>, CharSource>(){

            @Override
            public CharSource apply(InputSupplier<? extends Reader> input) {
                return CharStreams.asCharSource(input);
            }
        });
        return CharStreams.asInputSupplier(CharSource.concat(sources));
    }

    @Deprecated
    public static InputSupplier<Reader> join(InputSupplier<? extends Reader> ... suppliers) {
        return CharStreams.join(Arrays.asList(suppliers));
    }

    public static void skipFully(Reader reader, long n2) throws IOException {
        Preconditions.checkNotNull(reader);
        while (n2 > 0L) {
            long amt = reader.skip(n2);
            if (amt == 0L) {
                if (reader.read() == -1) {
                    throw new EOFException();
                }
                --n2;
                continue;
            }
            n2 -= amt;
        }
    }

    public static Writer nullWriter() {
        return NullWriter.INSTANCE;
    }

    public static Writer asWriter(Appendable target) {
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
        return new Reader(){

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return this.read(CharBuffer.wrap(cbuf, off, len));
            }

            @Override
            public int read(CharBuffer target) throws IOException {
                return readable.read(target);
            }

            @Override
            public void close() throws IOException {
                if (readable instanceof Closeable) {
                    ((Closeable)((Object)readable)).close();
                }
            }
        };
    }

    @Deprecated
    public static CharSource asCharSource(final InputSupplier<? extends Readable> supplier) {
        Preconditions.checkNotNull(supplier);
        return new CharSource(){

            @Override
            public Reader openStream() throws IOException {
                return CharStreams.asReader((Readable)supplier.getInput());
            }

            public String toString() {
                return "CharStreams.asCharSource(" + supplier + ")";
            }
        };
    }

    @Deprecated
    public static CharSink asCharSink(final OutputSupplier<? extends Appendable> supplier) {
        Preconditions.checkNotNull(supplier);
        return new CharSink(){

            @Override
            public Writer openStream() throws IOException {
                return CharStreams.asWriter((Appendable)supplier.getOutput());
            }

            public String toString() {
                return "CharStreams.asCharSink(" + supplier + ")";
            }
        };
    }

    static <R extends Reader> InputSupplier<R> asInputSupplier(CharSource source) {
        return Preconditions.checkNotNull(source);
    }

    static <W extends Writer> OutputSupplier<W> asOutputSupplier(CharSink sink) {
        return Preconditions.checkNotNull(sink);
    }

    private static final class NullWriter
    extends Writer {
        private static final NullWriter INSTANCE = new NullWriter();

        private NullWriter() {
        }

        @Override
        public void write(int c2) {
        }

        @Override
        public void write(char[] cbuf) {
            Preconditions.checkNotNull(cbuf);
        }

        @Override
        public void write(char[] cbuf, int off, int len) {
            Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
        }

        @Override
        public void write(String str) {
            Preconditions.checkNotNull(str);
        }

        @Override
        public void write(String str, int off, int len) {
            Preconditions.checkPositionIndexes(off, off + len, str.length());
        }

        @Override
        public Writer append(CharSequence csq) {
            Preconditions.checkNotNull(csq);
            return this;
        }

        @Override
        public Writer append(CharSequence csq, int start, int end) {
            Preconditions.checkPositionIndexes(start, end, csq.length());
            return this;
        }

        @Override
        public Writer append(char c2) {
            return this;
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }

        public String toString() {
            return "CharStreams.nullWriter()";
        }
    }
}

