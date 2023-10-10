// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
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
    
    public static void checkArgument(final boolean expression, @CheckForNull final Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }
    
    public static void checkArgument(final boolean expression, final String errorMessageTemplate, @CheckForNull final Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final char p1) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final int p1) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final long p1) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final char p1, final char p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final char p1, final int p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final char p1, final long p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final char p1, @CheckForNull final Object p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final int p1, final char p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final int p1, final int p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final int p1, final long p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final int p1, @CheckForNull final Object p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final long p1, final char p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final long p1, final int p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final long p1, final long p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, final long p1, @CheckForNull final Object p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, final char p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, final int p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, final long p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2, @CheckForNull final Object p3) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3));
        }
    }
    
    public static void checkArgument(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2, @CheckForNull final Object p3, @CheckForNull final Object p4) {
        if (!b) {
            throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3, p4));
        }
    }
    
    public static void checkState(final boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }
    
    public static void checkState(final boolean expression, @CheckForNull final Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }
    
    public static void checkState(final boolean expression, @CheckForNull final String errorMessageTemplate, @CheckForNull final Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final char p1) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final int p1) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final long p1) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final char p1, final char p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final char p1, final int p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final char p1, final long p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final char p1, @CheckForNull final Object p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final int p1, final char p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final int p1, final int p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final int p1, final long p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final int p1, @CheckForNull final Object p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final long p1, final char p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final long p1, final int p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final long p1, final long p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, final long p1, @CheckForNull final Object p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, final char p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, final int p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, final long p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2, @CheckForNull final Object p3) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3));
        }
    }
    
    public static void checkState(final boolean b, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2, @CheckForNull final Object p3, @CheckForNull final Object p4) {
        if (!b) {
            throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3, p4));
        }
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T reference, @CheckForNull final Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T reference, final String errorMessageTemplate, @CheckForNull final Object... errorMessageArgs) {
        if (reference == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final char p1) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final int p1) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final long p1) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, @CheckForNull final Object p1) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final char p1, final char p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final char p1, final int p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final char p1, final long p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final char p1, @CheckForNull final Object p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final int p1, final char p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final int p1, final int p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final int p1, final long p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final int p1, @CheckForNull final Object p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final long p1, final char p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final long p1, final int p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final long p1, final long p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, final long p1, @CheckForNull final Object p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, @CheckForNull final Object p1, final char p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, @CheckForNull final Object p1, final int p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, @CheckForNull final Object p1, final long p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2, @CheckForNull final Object p3) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(@CheckForNull final T obj, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2, @CheckForNull final Object p3, @CheckForNull final Object p4) {
        if (obj == null) {
            throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3, p4));
        }
        return obj;
    }
    
    @CanIgnoreReturnValue
    public static int checkElementIndex(final int index, final int size) {
        return checkElementIndex(index, size, "index");
    }
    
    @CanIgnoreReturnValue
    public static int checkElementIndex(final int index, final int size, final String desc) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
        }
        return index;
    }
    
    private static String badElementIndex(final int index, final int size, final String desc) {
        if (index < 0) {
            return Strings.lenientFormat("%s (%s) must not be negative", desc, index);
        }
        if (size < 0) {
            throw new IllegalArgumentException(new StringBuilder(26).append("negative size: ").append(size).toString());
        }
        return Strings.lenientFormat("%s (%s) must be less than size (%s)", desc, index, size);
    }
    
    @CanIgnoreReturnValue
    public static int checkPositionIndex(final int index, final int size) {
        return checkPositionIndex(index, size, "index");
    }
    
    @CanIgnoreReturnValue
    public static int checkPositionIndex(final int index, final int size, final String desc) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
        }
        return index;
    }
    
    private static String badPositionIndex(final int index, final int size, final String desc) {
        if (index < 0) {
            return Strings.lenientFormat("%s (%s) must not be negative", desc, index);
        }
        if (size < 0) {
            throw new IllegalArgumentException(new StringBuilder(26).append("negative size: ").append(size).toString());
        }
        return Strings.lenientFormat("%s (%s) must not be greater than size (%s)", desc, index, size);
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
        return Strings.lenientFormat("end index (%s) must not be less than start index (%s)", end, start);
    }
}
