/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckReturnValue
 *  javax.annotation.Nullable
 */
package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible
public class Joiner {
    private final String separator;

    public static Joiner on(String separator) {
        return new Joiner(separator);
    }

    public static Joiner on(char separator) {
        return new Joiner(String.valueOf(separator));
    }

    private Joiner(String separator) {
        this.separator = Preconditions.checkNotNull(separator);
    }

    private Joiner(Joiner prototype) {
        this.separator = prototype.separator;
    }

    public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
        return this.appendTo(appendable, parts.iterator());
    }

    public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
        Preconditions.checkNotNull(appendable);
        if (parts.hasNext()) {
            appendable.append(this.toString(parts.next()));
            while (parts.hasNext()) {
                appendable.append(this.separator);
                appendable.append(this.toString(parts.next()));
            }
        }
        return appendable;
    }

    public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
        return this.appendTo(appendable, Arrays.asList(parts));
    }

    public final <A extends Appendable> A appendTo(A appendable, @Nullable Object first, @Nullable Object second, Object ... rest) throws IOException {
        return this.appendTo(appendable, Joiner.iterable(first, second, rest));
    }

    public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
        return this.appendTo(builder, parts.iterator());
    }

    public final StringBuilder appendTo(StringBuilder builder, Iterator<?> parts) {
        try {
            this.appendTo((Appendable)builder, parts);
        }
        catch (IOException impossible) {
            throw new AssertionError((Object)impossible);
        }
        return builder;
    }

    public final StringBuilder appendTo(StringBuilder builder, Object[] parts) {
        return this.appendTo(builder, (Iterable<?>)Arrays.asList(parts));
    }

    public final StringBuilder appendTo(StringBuilder builder, @Nullable Object first, @Nullable Object second, Object ... rest) {
        return this.appendTo(builder, Joiner.iterable(first, second, rest));
    }

    public final String join(Iterable<?> parts) {
        return this.join(parts.iterator());
    }

    public final String join(Iterator<?> parts) {
        return this.appendTo(new StringBuilder(), parts).toString();
    }

    public final String join(Object[] parts) {
        return this.join(Arrays.asList(parts));
    }

    public final String join(@Nullable Object first, @Nullable Object second, Object ... rest) {
        return this.join(Joiner.iterable(first, second, rest));
    }

    @CheckReturnValue
    public Joiner useForNull(final String nullText) {
        Preconditions.checkNotNull(nullText);
        return new Joiner(this){

            @Override
            CharSequence toString(@Nullable Object part) {
                return part == null ? nullText : Joiner.this.toString(part);
            }

            @Override
            public Joiner useForNull(String nullText2) {
                throw new UnsupportedOperationException("already specified useForNull");
            }

            @Override
            public Joiner skipNulls() {
                throw new UnsupportedOperationException("already specified useForNull");
            }
        };
    }

    @CheckReturnValue
    public Joiner skipNulls() {
        return new Joiner(this){

            @Override
            public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
                Object part;
                Preconditions.checkNotNull(appendable, "appendable");
                Preconditions.checkNotNull(parts, "parts");
                while (parts.hasNext()) {
                    part = parts.next();
                    if (part == null) continue;
                    appendable.append(Joiner.this.toString(part));
                    break;
                }
                while (parts.hasNext()) {
                    part = parts.next();
                    if (part == null) continue;
                    appendable.append(Joiner.this.separator);
                    appendable.append(Joiner.this.toString(part));
                }
                return appendable;
            }

            @Override
            public Joiner useForNull(String nullText) {
                throw new UnsupportedOperationException("already specified skipNulls");
            }

            @Override
            public MapJoiner withKeyValueSeparator(String kvs) {
                throw new UnsupportedOperationException("can't use .skipNulls() with maps");
            }
        };
    }

    @CheckReturnValue
    public MapJoiner withKeyValueSeparator(String keyValueSeparator) {
        return new MapJoiner(this, keyValueSeparator);
    }

    CharSequence toString(Object part) {
        Preconditions.checkNotNull(part);
        return part instanceof CharSequence ? (CharSequence)part : part.toString();
    }

    private static Iterable<Object> iterable(final Object first, final Object second, final Object[] rest) {
        Preconditions.checkNotNull(rest);
        return new AbstractList<Object>(){

            @Override
            public int size() {
                return rest.length + 2;
            }

            @Override
            public Object get(int index) {
                switch (index) {
                    case 0: {
                        return first;
                    }
                    case 1: {
                        return second;
                    }
                }
                return rest[index - 2];
            }
        };
    }

    public static final class MapJoiner {
        private final Joiner joiner;
        private final String keyValueSeparator;

        private MapJoiner(Joiner joiner, String keyValueSeparator) {
            this.joiner = joiner;
            this.keyValueSeparator = Preconditions.checkNotNull(keyValueSeparator);
        }

        public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException {
            return this.appendTo(appendable, map.entrySet());
        }

        public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) {
            return this.appendTo(builder, (Iterable<? extends Map.Entry<?, ?>>)map.entrySet());
        }

        public String join(Map<?, ?> map) {
            return this.join(map.entrySet());
        }

        @Beta
        public <A extends Appendable> A appendTo(A appendable, Iterable<? extends Map.Entry<?, ?>> entries) throws IOException {
            return this.appendTo(appendable, entries.iterator());
        }

        @Beta
        public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Map.Entry<?, ?>> parts) throws IOException {
            Preconditions.checkNotNull(appendable);
            if (parts.hasNext()) {
                Map.Entry<?, ?> entry = parts.next();
                appendable.append(this.joiner.toString(entry.getKey()));
                appendable.append(this.keyValueSeparator);
                appendable.append(this.joiner.toString(entry.getValue()));
                while (parts.hasNext()) {
                    appendable.append(this.joiner.separator);
                    Map.Entry<?, ?> e2 = parts.next();
                    appendable.append(this.joiner.toString(e2.getKey()));
                    appendable.append(this.keyValueSeparator);
                    appendable.append(this.joiner.toString(e2.getValue()));
                }
            }
            return appendable;
        }

        @Beta
        public StringBuilder appendTo(StringBuilder builder, Iterable<? extends Map.Entry<?, ?>> entries) {
            return this.appendTo(builder, entries.iterator());
        }

        @Beta
        public StringBuilder appendTo(StringBuilder builder, Iterator<? extends Map.Entry<?, ?>> entries) {
            try {
                this.appendTo((Appendable)builder, entries);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
            return builder;
        }

        @Beta
        public String join(Iterable<? extends Map.Entry<?, ?>> entries) {
            return this.join(entries.iterator());
        }

        @Beta
        public String join(Iterator<? extends Map.Entry<?, ?>> entries) {
            return this.appendTo(new StringBuilder(), entries).toString();
        }

        @CheckReturnValue
        public MapJoiner useForNull(String nullText) {
            return new MapJoiner(this.joiner.useForNull(nullText), this.keyValueSeparator);
        }
    }
}

