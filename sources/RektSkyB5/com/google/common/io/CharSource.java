/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.CharSequenceReader;
import com.google.common.io.CharSink;
import com.google.common.io.CharStreams;
import com.google.common.io.Closer;
import com.google.common.io.InputSupplier;
import com.google.common.io.LineProcessor;
import com.google.common.io.MultiReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public abstract class CharSource
implements InputSupplier<Reader> {
    protected CharSource() {
    }

    public abstract Reader openStream() throws IOException;

    @Override
    @Deprecated
    public final Reader getInput() throws IOException {
        return this.openStream();
    }

    public BufferedReader openBufferedStream() throws IOException {
        Reader reader = this.openStream();
        return reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
    }

    public long copyTo(Appendable appendable) throws IOException {
        Preconditions.checkNotNull(appendable);
        Closer closer = Closer.create();
        try {
            Reader reader = closer.register(this.openStream());
            long l2 = CharStreams.copy(reader, appendable);
            return l2;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public long copyTo(CharSink sink) throws IOException {
        Preconditions.checkNotNull(sink);
        Closer closer = Closer.create();
        try {
            Reader reader = closer.register(this.openStream());
            Writer writer = closer.register(sink.openStream());
            long l2 = CharStreams.copy(reader, (Appendable)writer);
            return l2;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public String read() throws IOException {
        Closer closer = Closer.create();
        try {
            Reader reader = closer.register(this.openStream());
            String string = CharStreams.toString(reader);
            return string;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    @Nullable
    public String readFirstLine() throws IOException {
        Closer closer = Closer.create();
        try {
            BufferedReader reader = closer.register(this.openBufferedStream());
            String string = reader.readLine();
            return string;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public ImmutableList<String> readLines() throws IOException {
        Closer closer = Closer.create();
        try {
            String line;
            BufferedReader reader = closer.register(this.openBufferedStream());
            ArrayList<String> result = Lists.newArrayList();
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            ImmutableList<String> immutableList = ImmutableList.copyOf(result);
            return immutableList;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    @Beta
    public <T> T readLines(LineProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(processor);
        Closer closer = Closer.create();
        try {
            Reader reader = closer.register(this.openStream());
            T t2 = CharStreams.readLines(reader, processor);
            return t2;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public boolean isEmpty() throws IOException {
        Closer closer = Closer.create();
        try {
            Reader reader = closer.register(this.openStream());
            boolean bl = reader.read() == -1;
            return bl;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public static CharSource concat(Iterable<? extends CharSource> sources) {
        return new ConcatenatedCharSource(sources);
    }

    public static CharSource concat(Iterator<? extends CharSource> sources) {
        return CharSource.concat(ImmutableList.copyOf(sources));
    }

    public static CharSource concat(CharSource ... sources) {
        return CharSource.concat(ImmutableList.copyOf(sources));
    }

    public static CharSource wrap(CharSequence charSequence) {
        return new CharSequenceCharSource(charSequence);
    }

    public static CharSource empty() {
        return EmptyCharSource.INSTANCE;
    }

    private static final class ConcatenatedCharSource
    extends CharSource {
        private final Iterable<? extends CharSource> sources;

        ConcatenatedCharSource(Iterable<? extends CharSource> sources) {
            this.sources = Preconditions.checkNotNull(sources);
        }

        @Override
        public Reader openStream() throws IOException {
            return new MultiReader(this.sources.iterator());
        }

        @Override
        public boolean isEmpty() throws IOException {
            for (CharSource charSource : this.sources) {
                if (charSource.isEmpty()) continue;
                return false;
            }
            return true;
        }

        public String toString() {
            return "CharSource.concat(" + this.sources + ")";
        }
    }

    private static final class EmptyCharSource
    extends CharSequenceCharSource {
        private static final EmptyCharSource INSTANCE = new EmptyCharSource();

        private EmptyCharSource() {
            super("");
        }

        @Override
        public String toString() {
            return "CharSource.empty()";
        }
    }

    private static class CharSequenceCharSource
    extends CharSource {
        private static final Splitter LINE_SPLITTER = Splitter.on(Pattern.compile("\r\n|\n|\r"));
        private final CharSequence seq;

        protected CharSequenceCharSource(CharSequence seq) {
            this.seq = Preconditions.checkNotNull(seq);
        }

        @Override
        public Reader openStream() {
            return new CharSequenceReader(this.seq);
        }

        @Override
        public String read() {
            return this.seq.toString();
        }

        @Override
        public boolean isEmpty() {
            return this.seq.length() == 0;
        }

        private Iterable<String> lines() {
            return new Iterable<String>(){

                @Override
                public Iterator<String> iterator() {
                    return new AbstractIterator<String>(){
                        Iterator<String> lines;
                        {
                            this.lines = LINE_SPLITTER.split(CharSequenceCharSource.this.seq).iterator();
                        }

                        @Override
                        protected String computeNext() {
                            if (this.lines.hasNext()) {
                                String next = this.lines.next();
                                if (this.lines.hasNext() || !next.isEmpty()) {
                                    return next;
                                }
                            }
                            return (String)this.endOfData();
                        }
                    };
                }
            };
        }

        @Override
        public String readFirstLine() {
            Iterator<String> lines = this.lines().iterator();
            return lines.hasNext() ? lines.next() : null;
        }

        @Override
        public ImmutableList<String> readLines() {
            return ImmutableList.copyOf(this.lines());
        }

        @Override
        public <T> T readLines(LineProcessor<T> processor) throws IOException {
            for (String line : this.lines()) {
                if (!processor.processLine(line)) break;
            }
            return processor.getResult();
        }

        public String toString() {
            return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")";
        }
    }
}

