// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.common.annotations.VisibleForTesting;
import com.google.errorprone.annotations.InlineMeValidationDisabled;
import com.google.errorprone.annotations.InlineMe;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Strings
{
    private Strings() {
    }
    
    public static String nullToEmpty(@CheckForNull final String string) {
        return Platform.nullToEmpty(string);
    }
    
    @CheckForNull
    public static String emptyToNull(@CheckForNull final String string) {
        return Platform.emptyToNull(string);
    }
    
    public static boolean isNullOrEmpty(@CheckForNull final String string) {
        return Platform.stringIsNullOrEmpty(string);
    }
    
    public static String padStart(final String string, final int minLength, final char padChar) {
        Preconditions.checkNotNull(string);
        if (string.length() >= minLength) {
            return string;
        }
        final StringBuilder sb = new StringBuilder(minLength);
        for (int i = string.length(); i < minLength; ++i) {
            sb.append(padChar);
        }
        sb.append(string);
        return sb.toString();
    }
    
    public static String padEnd(final String string, final int minLength, final char padChar) {
        Preconditions.checkNotNull(string);
        if (string.length() >= minLength) {
            return string;
        }
        final StringBuilder sb = new StringBuilder(minLength);
        sb.append(string);
        for (int i = string.length(); i < minLength; ++i) {
            sb.append(padChar);
        }
        return sb.toString();
    }
    
    @InlineMe(replacement = "string.repeat(count)")
    @InlineMeValidationDisabled("Java 11+ API only")
    public static String repeat(final String string, final int count) {
        Preconditions.checkNotNull(string);
        if (count <= 1) {
            Preconditions.checkArgument(count >= 0, "invalid count: %s", count);
            return (count == 0) ? "" : string;
        }
        final int len = string.length();
        final long longSize = len * (long)count;
        final int size = (int)longSize;
        if (size != longSize) {
            throw new ArrayIndexOutOfBoundsException(new StringBuilder(51).append("Required array size too large: ").append(longSize).toString());
        }
        final char[] array = new char[size];
        string.getChars(0, len, array, 0);
        int n;
        for (n = len; n < size - n; n <<= 1) {
            System.arraycopy(array, 0, array, n, n);
        }
        System.arraycopy(array, 0, array, n, size - n);
        return new String(array);
    }
    
    public static String commonPrefix(final CharSequence a, final CharSequence b) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        int maxPrefixLength;
        int p;
        for (maxPrefixLength = Math.min(a.length(), b.length()), p = 0; p < maxPrefixLength && a.charAt(p) == b.charAt(p); ++p) {}
        if (validSurrogatePairAt(a, p - 1) || validSurrogatePairAt(b, p - 1)) {
            --p;
        }
        return a.subSequence(0, p).toString();
    }
    
    public static String commonSuffix(final CharSequence a, final CharSequence b) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        int maxSuffixLength;
        int s;
        for (maxSuffixLength = Math.min(a.length(), b.length()), s = 0; s < maxSuffixLength && a.charAt(a.length() - s - 1) == b.charAt(b.length() - s - 1); ++s) {}
        if (validSurrogatePairAt(a, a.length() - s - 1) || validSurrogatePairAt(b, b.length() - s - 1)) {
            --s;
        }
        return a.subSequence(a.length() - s, a.length()).toString();
    }
    
    @VisibleForTesting
    static boolean validSurrogatePairAt(final CharSequence string, final int index) {
        return index >= 0 && index <= string.length() - 2 && Character.isHighSurrogate(string.charAt(index)) && Character.isLowSurrogate(string.charAt(index + 1));
    }
    
    public static String lenientFormat(@CheckForNull String template, @CheckForNull Object... args) {
        template = String.valueOf(template);
        if (args == null) {
            args = new Object[] { "(Object[])null" };
        }
        else {
            for (int i = 0; i < args.length; ++i) {
                args[i] = lenientToString(args[i]);
            }
        }
        final StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int j = 0;
        while (j < args.length) {
            final int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[j++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template, templateStart, template.length());
        if (j < args.length) {
            builder.append(" [");
            builder.append(args[j++]);
            while (j < args.length) {
                builder.append(", ");
                builder.append(args[j++]);
            }
            builder.append(']');
        }
        return builder.toString();
    }
    
    private static String lenientToString(@CheckForNull final Object o) {
        if (o == null) {
            return "null";
        }
        try {
            return o.toString();
        }
        catch (Exception e) {
            final String name = o.getClass().getName();
            final String hexString = Integer.toHexString(System.identityHashCode(o));
            final String objectToString = new StringBuilder(1 + String.valueOf(name).length() + String.valueOf(hexString).length()).append(name).append('@').append(hexString).toString();
            final Logger logger = Logger.getLogger("com.google.common.base.Strings");
            final Level warning = Level.WARNING;
            final String original = "Exception during lenientFormat for ";
            final String value = String.valueOf(objectToString);
            logger.log(warning, (value.length() != 0) ? original.concat(value) : new String(original), e);
            final String name2 = e.getClass().getName();
            return new StringBuilder(9 + String.valueOf(objectToString).length() + String.valueOf(name2).length()).append("<").append(objectToString).append(" threw ").append(name2).append(">").toString();
        }
    }
}
