// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.base;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Preconditions
{
    private Preconditions() {
    }
    
    public static void checkArgument(final boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }
    
    public static void checkArgument(final boolean expression, @Nullable final Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }
    
    public static void checkArgument(final boolean expression, @Nullable final String errorMessageTemplate, @Nullable final Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final char p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final int p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final long p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final char p1, final char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final char p1, final int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final char p1, final long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final char p1, @Nullable final Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final int p1, final char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final int p1, final int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final int p1, final long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final int p1, @Nullable final Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final long p1, final char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final long p1, final int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final long p1, final long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, final long p1, @Nullable final Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, final char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, final int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, final long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, @Nullable final Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2, p3));
        }
    }
    
    public static void checkArgument(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3, @Nullable final Object p4) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2, p3, p4));
        }
    }
    
    public static void checkState(final boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }
    
    public static void checkState(final boolean expression, @Nullable final Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }
    
    public static void checkState(final boolean expression, @Nullable final String errorMessageTemplate, @Nullable final Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final char p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final int p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final long p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final char p1, final char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final char p1, final int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final char p1, final long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final char p1, @Nullable final Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final int p1, final char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final int p1, final int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final int p1, final long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final int p1, @Nullable final Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final long p1, final char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final long p1, final int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final long p1, final long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, final long p1, @Nullable final Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, final char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, final int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, final long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, @Nullable final Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2, p3));
        }
    }
    
    public static void checkState(final boolean b, @Nullable final String errorMessageTemplate, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3, @Nullable final Object p4) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2, p3, p4));
        }
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T reference, @Nullable final Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T reference, @Nullable final String errorMessageTemplate, @Nullable final Object... errorMessageArgs) {
        if (reference == null) {
            throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final char p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final int p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final long p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, @Nullable final Object p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final char p1, final char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final char p1, final int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final char p1, final long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final char p1, @Nullable final Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final int p1, final char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final int p1, final int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final int p1, final long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final int p1, @Nullable final Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final long p1, final char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final long p1, final int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final long p1, final long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, final long p1, @Nullable final Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, @Nullable final Object p1, final char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, @Nullable final Object p1, final int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, @Nullable final Object p1, final long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, @Nullable final Object p1, @Nullable final Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2, p3));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T obj, @Nullable final String errorMessageTemplate, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3, @Nullable final Object p4) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2, p3, p4));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static int checkElementIndex(final int index, final int size) {
        return checkElementIndex(index, size, "index");
    }
    
    @CanIgnoreReturnValue
    public static int checkElementIndex(final int index, final int size, @Nullable final String desc) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
        }
        return index;
    }
    
    private static String badElementIndex(final int index, final int size, final String desc) {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, index);
        }
        if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        }
        return format("%s (%s) must be less than size (%s)", desc, index, size);
    }
    
    @CanIgnoreReturnValue
    public static int checkPositionIndex(final int index, final int size) {
        return checkPositionIndex(index, size, "index");
    }
    
    @CanIgnoreReturnValue
    public static int checkPositionIndex(final int index, final int size, @Nullable final String desc) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
        }
        return index;
    }
    
    private static String badPositionIndex(final int index, final int size, final String desc) {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, index);
        }
        if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        }
        return format("%s (%s) must not be greater than size (%s)", desc, index, size);
    }
    
    public static void checkPositionIndexes(final int start, final int end, final int size) {
        if (start < 0 || end < start || end > size) {
            throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
        }
    }
    
    private static String badPositionIndexes(final int start, final int end, final int size) {
        if (start < 0 || start > size) {
            return badPositionIndex(start, size, "start index");
        }
        if (end < 0 || end > size) {
            return badPositionIndex(end, size, "end index");
        }
        return format("end index (%s) must not be less than start index (%s)", end, start);
    }
    
    static String format(String template, @Nullable final Object... args) {
        template = String.valueOf(template);
        final StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            final int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template, templateStart, template.length());
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }
        return builder.toString();
    }
}
