// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.examination.string;

import java.util.function.Consumer;
import java.util.Objects;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import java.util.function.LongFunction;
import java.util.stream.LongStream;
import java.util.function.DoubleFunction;
import java.util.stream.DoubleStream;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.function.IntFunction;
import java.util.Map;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import java.util.stream.Stream;
import com.viaversion.viaversion.libs.kyori.examination.AbstractExaminer;

public class MultiLineStringExaminer extends AbstractExaminer<Stream<String>>
{
    private static final String INDENT_2 = "  ";
    private final StringExaminer examiner;
    
    @NotNull
    public static MultiLineStringExaminer simpleEscaping() {
        return Instances.SIMPLE_ESCAPING;
    }
    
    public MultiLineStringExaminer(@NotNull final StringExaminer examiner) {
        this.examiner = examiner;
    }
    
    @NotNull
    @Override
    protected <E> Stream<String> array(final E[] array, @NotNull final Stream<Stream<String>> elements) {
        return this.arrayLike(elements);
    }
    
    @NotNull
    @Override
    protected <E> Stream<String> collection(@NotNull final Collection<E> collection, @NotNull final Stream<Stream<String>> elements) {
        return this.arrayLike(elements);
    }
    
    @NotNull
    @Override
    protected Stream<String> examinable(@NotNull final String name, @NotNull final Stream<Map.Entry<String, Stream<String>>> properties) {
        final Stream<String> flattened = flatten(",", properties.map(entry -> association(this.examine(entry.getKey()), " = ", (Stream<String>)entry.getValue())));
        final Stream<String> indented = indent(flattened);
        return enclose(indented, name + "{", "}");
    }
    
    @NotNull
    @Override
    protected <K, V> Stream<String> map(@NotNull final Map<K, V> map, @NotNull final Stream<Map.Entry<Stream<String>, Stream<String>>> entries) {
        final Stream<String> flattened = flatten(",", entries.map(entry -> association(entry.getKey(), " = ", (Stream<String>)entry.getValue())));
        final Stream<String> indented = indent(flattened);
        return enclose(indented, "{", "}");
    }
    
    @NotNull
    @Override
    protected Stream<String> nil() {
        return Stream.of(this.examiner.nil());
    }
    
    @NotNull
    @Override
    protected Stream<String> scalar(@NotNull final Object value) {
        return Stream.of(this.examiner.scalar(value));
    }
    
    @NotNull
    @Override
    public Stream<String> examine(final boolean value) {
        return Stream.of(this.examiner.examine(value));
    }
    
    @NotNull
    @Override
    public Stream<String> examine(final byte value) {
        return Stream.of(this.examiner.examine(value));
    }
    
    @NotNull
    @Override
    public Stream<String> examine(final char value) {
        return Stream.of(this.examiner.examine(value));
    }
    
    @NotNull
    @Override
    public Stream<String> examine(final double value) {
        return Stream.of(this.examiner.examine(value));
    }
    
    @NotNull
    @Override
    public Stream<String> examine(final float value) {
        return Stream.of(this.examiner.examine(value));
    }
    
    @NotNull
    @Override
    public Stream<String> examine(final int value) {
        return Stream.of(this.examiner.examine(value));
    }
    
    @NotNull
    @Override
    public Stream<String> examine(final long value) {
        return Stream.of(this.examiner.examine(value));
    }
    
    @NotNull
    @Override
    public Stream<String> examine(final short value) {
        return Stream.of(this.examiner.examine(value));
    }
    
    @NotNull
    @Override
    protected Stream<String> array(final int length, final IntFunction<Stream<String>> value) {
        return this.arrayLike((length == 0) ? Stream.empty() : IntStream.range(0, length).mapToObj((IntFunction<? extends Stream<String>>)value));
    }
    
    @NotNull
    @Override
    protected <T> Stream<String> stream(@NotNull final Stream<T> stream) {
        return this.arrayLike(stream.map((Function<? super T, ? extends Stream<String>>)this::examine));
    }
    
    @NotNull
    @Override
    protected Stream<String> stream(@NotNull final DoubleStream stream) {
        return this.arrayLike(stream.mapToObj((DoubleFunction<? extends Stream<String>>)this::examine));
    }
    
    @NotNull
    @Override
    protected Stream<String> stream(@NotNull final IntStream stream) {
        return this.arrayLike(stream.mapToObj((IntFunction<? extends Stream<String>>)this::examine));
    }
    
    @NotNull
    @Override
    protected Stream<String> stream(@NotNull final LongStream stream) {
        return this.arrayLike(stream.mapToObj((LongFunction<? extends Stream<String>>)this::examine));
    }
    
    @NotNull
    @Override
    public Stream<String> examine(@Nullable final String value) {
        return Stream.of(this.examiner.examine(value));
    }
    
    private Stream<String> arrayLike(final Stream<Stream<String>> streams) {
        final Stream<String> flattened = flatten(",", streams);
        final Stream<String> indented = indent(flattened);
        return enclose(indented, "[", "]");
    }
    
    private static Stream<String> enclose(final Stream<String> lines, final String open, final String close) {
        return enclose((List<String>)lines.collect((Collector<? super String, ?, List<? super String>>)Collectors.toList()), open, close);
    }
    
    private static Stream<String> enclose(final List<String> lines, final String open, final String close) {
        if (lines.isEmpty()) {
            return Stream.of(open + close);
        }
        return Stream.of(Stream.of(open), indent(lines.stream()), Stream.of(close)).reduce(Stream.empty(), Stream::concat);
    }
    
    private static Stream<String> flatten(final String delimiter, final Stream<Stream<String>> bumpy) {
        final List<String> flat = new ArrayList<String>();
        final List<String> obj;
        int last;
        bumpy.forEachOrdered(lines -> {
            if (!obj.isEmpty()) {
                last = obj.size() - 1;
                obj.set(last, obj.get(last) + delimiter);
            }
            Objects.requireNonNull((List<Object>)obj);
            lines.forEachOrdered(obj::add);
            return;
        });
        return flat.stream();
    }
    
    private static Stream<String> association(final Stream<String> left, final String middle, final Stream<String> right) {
        return association((List<String>)left.collect((Collector<? super String, ?, List<? super String>>)Collectors.toList()), middle, (List<String>)right.collect((Collector<? super String, ?, List<? super String>>)Collectors.toList()));
    }
    
    private static Stream<String> association(final List<String> left, final String middle, final List<String> right) {
        final int lefts = left.size();
        final int rights = right.size();
        final int height = Math.max(lefts, rights);
        final int leftWidth = Strings.maxLength(left.stream());
        final String leftPad = (lefts < 2) ? "" : Strings.repeat(" ", leftWidth);
        final String middlePad = (lefts < 2) ? "" : Strings.repeat(" ", middle.length());
        final List<String> result = new ArrayList<String>(height);
        for (int i = 0; i < height; ++i) {
            final String l = (i < lefts) ? Strings.padEnd(left.get(i), leftWidth, ' ') : leftPad;
            final String m = (i == 0) ? middle : middlePad;
            final String r = (i < rights) ? right.get(i) : "";
            result.add(l + m + r);
        }
        return result.stream();
    }
    
    private static Stream<String> indent(final Stream<String> lines) {
        return lines.map(line -> "  " + line);
    }
    
    private static final class Instances
    {
        static final MultiLineStringExaminer SIMPLE_ESCAPING;
        
        static {
            SIMPLE_ESCAPING = new MultiLineStringExaminer(StringExaminer.simpleEscaping());
        }
    }
}
