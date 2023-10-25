/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viaversion.libs.kyori.examination.string;

import com.viaversion.viaversion.libs.kyori.examination.AbstractExaminer;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class StringExaminer
extends AbstractExaminer<String> {
    private static final Function<String, String> DEFAULT_ESCAPER = string -> string.replace("\"", "\\\"").replace("\\", "\\\\").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    private static final Collector<CharSequence, ?, String> COMMA_CURLY = Collectors.joining(", ", "{", "}");
    private static final Collector<CharSequence, ?, String> COMMA_SQUARE = Collectors.joining(", ", "[", "]");
    private final Function<String, String> escaper;

    public static @NonNull StringExaminer simpleEscaping() {
        return Instances.SIMPLE_ESCAPING;
    }

    public StringExaminer(@NonNull Function<String, String> escaper) {
        this.escaper = escaper;
    }

    @Override
    protected <E> @NonNull String array(@NonNull E[] array, @NonNull Stream<String> elements) {
        return elements.collect(COMMA_SQUARE);
    }

    @Override
    protected <E> @NonNull String collection(@NonNull Collection<E> collection, @NonNull Stream<String> elements) {
        return elements.collect(COMMA_SQUARE);
    }

    @Override
    protected @NonNull String examinable(@NonNull String name, @NonNull Stream<Map.Entry<String, String>> properties) {
        return name + properties.map(property -> (String)property.getKey() + '=' + (String)property.getValue()).collect(COMMA_CURLY);
    }

    @Override
    protected <K, V> @NonNull String map(@NonNull Map<K, V> map, @NonNull Stream<Map.Entry<String, String>> entries) {
        return entries.map(entry -> (String)entry.getKey() + '=' + (String)entry.getValue()).collect(COMMA_CURLY);
    }

    @Override
    protected @NonNull String nil() {
        return "null";
    }

    @Override
    protected @NonNull String scalar(@NonNull Object value) {
        return String.valueOf(value);
    }

    @Override
    public @NonNull String examine(boolean value) {
        return String.valueOf(value);
    }

    @Override
    public @NonNull String examine(boolean @Nullable [] values) {
        if (values == null) {
            return this.nil();
        }
        return StringExaminer.array(values.length, index -> this.examine(values[index]));
    }

    @Override
    public @NonNull String examine(byte value) {
        return String.valueOf(value);
    }

    @Override
    public @NonNull String examine(byte @Nullable [] values) {
        if (values == null) {
            return this.nil();
        }
        return StringExaminer.array(values.length, index -> this.examine(values[index]));
    }

    @Override
    public @NonNull String examine(char value) {
        return '\'' + this.escaper.apply(String.valueOf(value)) + '\'';
    }

    @Override
    public @NonNull String examine(char @Nullable [] values) {
        if (values == null) {
            return this.nil();
        }
        return StringExaminer.array(values.length, index -> this.examine(values[index]));
    }

    @Override
    public @NonNull String examine(double value) {
        return StringExaminer.withSuffix(String.valueOf(value), 'd');
    }

    @Override
    public @NonNull String examine(double @Nullable [] values) {
        if (values == null) {
            return this.nil();
        }
        return StringExaminer.array(values.length, index -> this.examine(values[index]));
    }

    @Override
    public @NonNull String examine(float value) {
        return StringExaminer.withSuffix(String.valueOf(value), 'f');
    }

    @Override
    public @NonNull String examine(float @Nullable [] values) {
        if (values == null) {
            return this.nil();
        }
        return StringExaminer.array(values.length, index -> this.examine(values[index]));
    }

    @Override
    public @NonNull String examine(int value) {
        return String.valueOf(value);
    }

    @Override
    public @NonNull String examine(int @Nullable [] values) {
        if (values == null) {
            return this.nil();
        }
        return StringExaminer.array(values.length, index -> this.examine(values[index]));
    }

    @Override
    public @NonNull String examine(long value) {
        return String.valueOf(value);
    }

    @Override
    public @NonNull String examine(long @Nullable [] values) {
        if (values == null) {
            return this.nil();
        }
        return StringExaminer.array(values.length, index -> this.examine(values[index]));
    }

    @Override
    public @NonNull String examine(short value) {
        return String.valueOf(value);
    }

    @Override
    public @NonNull String examine(short @Nullable [] values) {
        if (values == null) {
            return this.nil();
        }
        return StringExaminer.array(values.length, index -> this.examine(values[index]));
    }

    @Override
    protected <T> @NonNull String stream(@NonNull Stream<T> stream) {
        return stream.map(this::examine).collect(COMMA_SQUARE);
    }

    @Override
    protected @NonNull String stream(@NonNull DoubleStream stream) {
        return stream.mapToObj(this::examine).collect(COMMA_SQUARE);
    }

    @Override
    protected @NonNull String stream(@NonNull IntStream stream) {
        return stream.mapToObj(this::examine).collect(COMMA_SQUARE);
    }

    @Override
    protected @NonNull String stream(@NonNull LongStream stream) {
        return stream.mapToObj(this::examine).collect(COMMA_SQUARE);
    }

    @Override
    public @NonNull String examine(@Nullable String value) {
        if (value == null) {
            return this.nil();
        }
        return '\"' + this.escaper.apply(value) + '\"';
    }

    private static @NonNull String withSuffix(String string, char suffix) {
        return string + suffix;
    }

    private static @NonNull String array(int length, IntFunction<String> value) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < length; ++i) {
            sb.append(value.apply(i));
            if (i + 1 >= length) continue;
            sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    static /* synthetic */ Function access$000() {
        return DEFAULT_ESCAPER;
    }

    private static final class Instances {
        static final StringExaminer SIMPLE_ESCAPING = new StringExaminer(StringExaminer.access$000());

        private Instances() {
        }
    }
}

