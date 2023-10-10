/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public final class Preconditions {
    private Preconditions() {
    }

    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    public static void checkArgument(boolean expression, @Nullable String errorMessageTemplate, Object ... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(Preconditions.format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    public static void checkState(boolean expression, @Nullable String errorMessageTemplate, Object ... errorMessageArgs) {
        if (!expression) {
            throw new IllegalStateException(Preconditions.format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, @Nullable String errorMessageTemplate, Object ... errorMessageArgs) {
        if (reference == null) {
            throw new NullPointerException(Preconditions.format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }

    public static int checkElementIndex(int index, int size) {
        return Preconditions.checkElementIndex(index, size, "index");
    }

    public static int checkElementIndex(int index, int size, @Nullable String desc) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(Preconditions.badElementIndex(index, size, desc));
        }
        return index;
    }

    private static String badElementIndex(int index, int size, String desc) {
        if (index < 0) {
            return Preconditions.format("%s (%s) must not be negative", desc, index);
        }
        if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        }
        return Preconditions.format("%s (%s) must be less than size (%s)", desc, index, size);
    }

    public static int checkPositionIndex(int index, int size) {
        return Preconditions.checkPositionIndex(index, size, "index");
    }

    public static int checkPositionIndex(int index, int size, @Nullable String desc) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(Preconditions.badPositionIndex(index, size, desc));
        }
        return index;
    }

    private static String badPositionIndex(int index, int size, String desc) {
        if (index < 0) {
            return Preconditions.format("%s (%s) must not be negative", desc, index);
        }
        if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        }
        return Preconditions.format("%s (%s) must not be greater than size (%s)", desc, index, size);
    }

    public static void checkPositionIndexes(int start, int end, int size) {
        if (start < 0 || end < start || end > size) {
            throw new IndexOutOfBoundsException(Preconditions.badPositionIndexes(start, end, size));
        }
    }

    private static String badPositionIndexes(int start, int end, int size) {
        if (start < 0 || start > size) {
            return Preconditions.badPositionIndex(start, size, "start index");
        }
        if (end < 0 || end > size) {
            return Preconditions.badPositionIndex(end, size, "end index");
        }
        return Preconditions.format("end index (%s) must not be less than start index (%s)", end, start);
    }

    static String format(String template, Object ... args) {
        int placeholderStart;
        template = String.valueOf(template);
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i2 = 0;
        while (i2 < args.length && (placeholderStart = template.indexOf("%s", templateStart)) != -1) {
            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i2++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));
        if (i2 < args.length) {
            builder.append(" [");
            builder.append(args[i2++]);
            while (i2 < args.length) {
                builder.append(", ");
                builder.append(args[i2++]);
            }
            builder.append(']');
        }
        return builder.toString();
    }
}

