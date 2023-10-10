// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.examination.string;

import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import java.util.function.LongFunction;
import java.util.stream.LongStream;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.function.DoubleFunction;
import java.util.stream.DoubleStream;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import java.util.stream.Collector;
import java.util.function.Function;
import com.viaversion.viaversion.libs.kyori.examination.AbstractExaminer;

public class StringExaminer extends AbstractExaminer<String>
{
    private static final Function<String, String> DEFAULT_ESCAPER;
    private static final Collector<CharSequence, ?, String> COMMA_CURLY;
    private static final Collector<CharSequence, ?, String> COMMA_SQUARE;
    private final Function<String, String> escaper;
    
    @NotNull
    public static StringExaminer simpleEscaping() {
        return Instances.SIMPLE_ESCAPING;
    }
    
    public StringExaminer(@NotNull final Function<String, String> escaper) {
        this.escaper = escaper;
    }
    
    @NotNull
    @Override
    protected <E> String array(final E[] array, @NotNull final Stream<String> elements) {
        return elements.collect(StringExaminer.COMMA_SQUARE);
    }
    
    @NotNull
    @Override
    protected <E> String collection(@NotNull final Collection<E> collection, @NotNull final Stream<String> elements) {
        return elements.collect(StringExaminer.COMMA_SQUARE);
    }
    
    @NotNull
    @Override
    protected String examinable(@NotNull final String name, @NotNull final Stream<Map.Entry<String, String>> properties) {
        return name + properties.map(property -> property.getKey() + '=' + (String)property.getValue()).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_CURLY);
    }
    
    @NotNull
    @Override
    protected <K, V> String map(@NotNull final Map<K, V> map, @NotNull final Stream<Map.Entry<String, String>> entries) {
        return entries.map(entry -> entry.getKey() + '=' + (String)entry.getValue()).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_CURLY);
    }
    
    @NotNull
    @Override
    protected String nil() {
        return "null";
    }
    
    @NotNull
    @Override
    protected String scalar(@NotNull final Object value) {
        return String.valueOf(value);
    }
    
    @NotNull
    @Override
    public String examine(final boolean value) {
        return String.valueOf(value);
    }
    
    @NotNull
    @Override
    public String examine(final byte value) {
        return String.valueOf(value);
    }
    
    @NotNull
    @Override
    public String examine(final char value) {
        return Strings.wrapIn(this.escaper.apply(String.valueOf(value)), '\'');
    }
    
    @NotNull
    @Override
    public String examine(final double value) {
        return Strings.withSuffix(String.valueOf(value), 'd');
    }
    
    @NotNull
    @Override
    public String examine(final float value) {
        return Strings.withSuffix(String.valueOf(value), 'f');
    }
    
    @NotNull
    @Override
    public String examine(final int value) {
        return String.valueOf(value);
    }
    
    @NotNull
    @Override
    public String examine(final long value) {
        return String.valueOf(value);
    }
    
    @NotNull
    @Override
    public String examine(final short value) {
        return String.valueOf(value);
    }
    
    @NotNull
    @Override
    protected <T> String stream(@NotNull final Stream<T> stream) {
        return stream.map((Function<? super T, ?>)this::examine).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_SQUARE);
    }
    
    @NotNull
    @Override
    protected String stream(@NotNull final DoubleStream stream) {
        return stream.mapToObj((DoubleFunction<?>)this::examine).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_SQUARE);
    }
    
    @NotNull
    @Override
    protected String stream(@NotNull final IntStream stream) {
        return stream.mapToObj((IntFunction<?>)this::examine).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_SQUARE);
    }
    
    @NotNull
    @Override
    protected String stream(@NotNull final LongStream stream) {
        return stream.mapToObj((LongFunction<?>)this::examine).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_SQUARE);
    }
    
    @NotNull
    @Override
    public String examine(@Nullable final String value) {
        if (value == null) {
            return this.nil();
        }
        return Strings.wrapIn(this.escaper.apply(value), '\"');
    }
    
    @NotNull
    @Override
    protected String array(final int length, final IntFunction<String> value) {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < length; ++i) {
            sb.append(value.apply(i));
            if (i + 1 < length) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }
    
    static {
        DEFAULT_ESCAPER = (string -> string.replace("\"", "\\\"").replace("\\", "\\\\").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t"));
        COMMA_CURLY = Collectors.joining(", ", "{", "}");
        COMMA_SQUARE = Collectors.joining(", ", "[", "]");
    }
    
    private static final class Instances
    {
        static final StringExaminer SIMPLE_ESCAPING;
        
        static {
            SIMPLE_ESCAPING = new StringExaminer(StringExaminer.DEFAULT_ESCAPER);
        }
    }
}
