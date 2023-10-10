// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.io.StringReader;
import com.google.common.base.Ascii;
import com.google.common.collect.Streams;
import com.google.common.collect.AbstractIterator;
import com.google.common.base.Splitter;
import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.List;
import java.util.Collection;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;
import javax.annotation.CheckForNull;
import java.io.Writer;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import com.google.errorprone.annotations.MustBeClosed;
import java.io.UncheckedIOException;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import com.google.common.annotations.Beta;
import java.nio.charset.Charset;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class CharSource
{
    protected CharSource() {
    }
    
    @Beta
    public ByteSource asByteSource(final Charset charset) {
        return new AsByteSource(charset);
    }
    
    public abstract Reader openStream() throws IOException;
    
    public BufferedReader openBufferedStream() throws IOException {
        final Reader reader = this.openStream();
        return (BufferedReader)((reader instanceof BufferedReader) ? reader : new BufferedReader(reader));
    }
    
    @Beta
    @MustBeClosed
    public Stream<String> lines() throws IOException {
        final BufferedReader reader = this.openBufferedStream();
        final BufferedReader bufferedReader;
        return reader.lines().onClose(() -> {
            try {
                bufferedReader.close();
            }
            catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
    
    @Beta
    public Optional<Long> lengthIfKnown() {
        return Optional.absent();
    }
    
    @Beta
    public long length() throws IOException {
        final Optional<Long> lengthIfKnown = this.lengthIfKnown();
        if (lengthIfKnown.isPresent()) {
            return lengthIfKnown.get();
        }
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.register(this.openStream());
            return this.countBySkipping(reader);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    private long countBySkipping(final Reader reader) throws IOException {
        long count = 0L;
        long read;
        while ((read = reader.skip(Long.MAX_VALUE)) != 0L) {
            count += read;
        }
        return count;
    }
    
    @CanIgnoreReturnValue
    public long copyTo(final Appendable appendable) throws IOException {
        Preconditions.checkNotNull(appendable);
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.register(this.openStream());
            return CharStreams.copy(reader, appendable);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @CanIgnoreReturnValue
    public long copyTo(final CharSink sink) throws IOException {
        Preconditions.checkNotNull(sink);
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.register(this.openStream());
            final Writer writer = closer.register(sink.openStream());
            return CharStreams.copy(reader, writer);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public String read() throws IOException {
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.register(this.openStream());
            return CharStreams.toString(reader);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @CheckForNull
    public String readFirstLine() throws IOException {
        final Closer closer = Closer.create();
        try {
            final BufferedReader reader = closer.register(this.openBufferedStream());
            return reader.readLine();
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public ImmutableList<String> readLines() throws IOException {
        final Closer closer = Closer.create();
        try {
            final BufferedReader reader = closer.register(this.openBufferedStream());
            final List<String> result = (List<String>)Lists.newArrayList();
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            return ImmutableList.copyOf((Collection<? extends String>)result);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @ParametricNullness
    @Beta
    @CanIgnoreReturnValue
    public <T> T readLines(final LineProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(processor);
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.register(this.openStream());
            return CharStreams.readLines(reader, processor);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @Beta
    public void forEachLine(final Consumer<? super String> action) throws IOException {
        try {
            final Stream<String> lines = this.lines();
            try {
                lines.forEachOrdered(action);
                if (lines != null) {
                    lines.close();
                }
            }
            catch (Throwable t) {
                if (lines != null) {
                    try {
                        lines.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }
    
    public boolean isEmpty() throws IOException {
        final Optional<Long> lengthIfKnown = this.lengthIfKnown();
        if (lengthIfKnown.isPresent()) {
            return lengthIfKnown.get() == 0L;
        }
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.register(this.openStream());
            return reader.read() == -1;
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public static CharSource concat(final Iterable<? extends CharSource> sources) {
        return new ConcatenatedCharSource(sources);
    }
    
    public static CharSource concat(final Iterator<? extends CharSource> sources) {
        return concat((Iterable<? extends CharSource>)ImmutableList.copyOf((Iterator<?>)sources));
    }
    
    public static CharSource concat(final CharSource... sources) {
        return concat(ImmutableList.copyOf(sources));
    }
    
    public static CharSource wrap(final CharSequence charSequence) {
        return (charSequence instanceof String) ? new StringCharSource((String)charSequence) : new CharSequenceCharSource(charSequence);
    }
    
    public static CharSource empty() {
        return EmptyCharSource.INSTANCE;
    }
    
    private final class AsByteSource extends ByteSource
    {
        final Charset charset;
        
        AsByteSource(final Charset charset) {
            this.charset = Preconditions.checkNotNull(charset);
        }
        
        @Override
        public CharSource asCharSource(final Charset charset) {
            if (charset.equals(this.charset)) {
                return CharSource.this;
            }
            return super.asCharSource(charset);
        }
        
        @Override
        public InputStream openStream() throws IOException {
            return new ReaderInputStream(CharSource.this.openStream(), this.charset, 8192);
        }
        
        @Override
        public String toString() {
            final String string = CharSource.this.toString();
            final String value = String.valueOf(this.charset);
            return new StringBuilder(15 + String.valueOf(string).length() + String.valueOf(value).length()).append(string).append(".asByteSource(").append(value).append(")").toString();
        }
    }
    
    private static class CharSequenceCharSource extends CharSource
    {
        private static final Splitter LINE_SPLITTER;
        protected final CharSequence seq;
        
        protected CharSequenceCharSource(final CharSequence seq) {
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
        
        @Override
        public long length() {
            return this.seq.length();
        }
        
        @Override
        public Optional<Long> lengthIfKnown() {
            return Optional.of((long)this.seq.length());
        }
        
        private Iterator<String> linesIterator() {
            return new AbstractIterator<String>() {
                Iterator<String> lines = CharSequenceCharSource.LINE_SPLITTER.split(CharSequenceCharSource.this.seq).iterator();
                
                @CheckForNull
                @Override
                protected String computeNext() {
                    if (this.lines.hasNext()) {
                        final String next = this.lines.next();
                        if (this.lines.hasNext() || !next.isEmpty()) {
                            return next;
                        }
                    }
                    return this.endOfData();
                }
            };
        }
        
        @Override
        public Stream<String> lines() {
            return Streams.stream(this.linesIterator());
        }
        
        @CheckForNull
        @Override
        public String readFirstLine() {
            final Iterator<String> lines = this.linesIterator();
            return lines.hasNext() ? lines.next() : null;
        }
        
        @Override
        public ImmutableList<String> readLines() {
            return ImmutableList.copyOf((Iterator<? extends String>)this.linesIterator());
        }
        
        @ParametricNullness
        @Override
        public <T> T readLines(final LineProcessor<T> processor) throws IOException {
            final Iterator<String> lines = this.linesIterator();
            while (lines.hasNext() && processor.processLine(lines.next())) {}
            return processor.getResult();
        }
        
        @Override
        public String toString() {
            final String truncate = Ascii.truncate(this.seq, 30, "...");
            return new StringBuilder(17 + String.valueOf(truncate).length()).append("CharSource.wrap(").append(truncate).append(")").toString();
        }
        
        static {
            LINE_SPLITTER = Splitter.onPattern("\r\n|\n|\r");
        }
    }
    
    private static class StringCharSource extends CharSequenceCharSource
    {
        protected StringCharSource(final String seq) {
            super(seq);
        }
        
        @Override
        public Reader openStream() {
            return new StringReader((String)this.seq);
        }
        
        @Override
        public long copyTo(final Appendable appendable) throws IOException {
            appendable.append(this.seq);
            return this.seq.length();
        }
        
        @Override
        public long copyTo(final CharSink sink) throws IOException {
            Preconditions.checkNotNull(sink);
            final Closer closer = Closer.create();
            try {
                final Writer writer = closer.register(sink.openStream());
                writer.write((String)this.seq);
                return this.seq.length();
            }
            catch (Throwable e) {
                throw closer.rethrow(e);
            }
            finally {
                closer.close();
            }
        }
    }
    
    private static final class EmptyCharSource extends StringCharSource
    {
        private static final EmptyCharSource INSTANCE;
        
        private EmptyCharSource() {
            super("");
        }
        
        @Override
        public String toString() {
            return "CharSource.empty()";
        }
        
        static {
            INSTANCE = new EmptyCharSource();
        }
    }
    
    private static final class ConcatenatedCharSource extends CharSource
    {
        private final Iterable<? extends CharSource> sources;
        
        ConcatenatedCharSource(final Iterable<? extends CharSource> sources) {
            this.sources = Preconditions.checkNotNull(sources);
        }
        
        @Override
        public Reader openStream() throws IOException {
            return new MultiReader(this.sources.iterator());
        }
        
        @Override
        public boolean isEmpty() throws IOException {
            for (final CharSource source : this.sources) {
                if (!source.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public Optional<Long> lengthIfKnown() {
            long result = 0L;
            for (final CharSource source : this.sources) {
                final Optional<Long> lengthIfKnown = source.lengthIfKnown();
                if (!lengthIfKnown.isPresent()) {
                    return Optional.absent();
                }
                result += lengthIfKnown.get();
            }
            return Optional.of(result);
        }
        
        @Override
        public long length() throws IOException {
            long result = 0L;
            for (final CharSource source : this.sources) {
                result += source.length();
            }
            return result;
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.sources);
            return new StringBuilder(19 + String.valueOf(value).length()).append("CharSource.concat(").append(value).append(")").toString();
        }
    }
}
