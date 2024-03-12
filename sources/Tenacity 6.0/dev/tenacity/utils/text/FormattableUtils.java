// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text;

import java.util.Formatter;
import java.util.Formattable;

public class FormattableUtils
{
    private static final String SIMPLEST_FORMAT = "%s";
    
    public static String toString(final Formattable formattable) {
        return String.format("%s", formattable);
    }
    
    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision) {
        return append(seq, formatter, flags, width, precision, ' ', null);
    }
    
    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final char padChar) {
        return append(seq, formatter, flags, width, precision, padChar, null);
    }
    
    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final CharSequence ellipsis) {
        return append(seq, formatter, flags, width, precision, ' ', ellipsis);
    }
    
    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final char padChar, final CharSequence ellipsis) {
        if (ellipsis != null && precision >= 0 && ellipsis.length() > precision) {
            throw new IllegalArgumentException(String.format("Specified ellipsis '%s' exceeds precision of %s", ellipsis, precision));
        }
        final StringBuilder buf = new StringBuilder(seq);
        if (precision >= 0 && precision < seq.length()) {
            CharSequence _ellipsis;
            if (ellipsis == null) {
                _ellipsis = "";
            }
            else {
                _ellipsis = ellipsis;
            }
            buf.replace(precision - _ellipsis.length(), seq.length(), _ellipsis.toString());
        }
        final boolean leftJustify = (flags & 0x1) == 0x1;
        for (int i = buf.length(); i < width; ++i) {
            buf.insert(leftJustify ? i : 0, padChar);
        }
        formatter.format(buf.toString(), new Object[0]);
        return formatter;
    }
}
