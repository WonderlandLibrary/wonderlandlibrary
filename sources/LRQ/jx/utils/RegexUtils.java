/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 */
package jx.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.TypeCastException;

public final class RegexUtils {
    public static final RegexUtils INSTANCE;

    public final String[] match(Matcher matcher) {
        boolean bl = false;
        List result = new ArrayList();
        while (matcher.find()) {
            result.add(matcher.group());
        }
        Collection $this$toTypedArray$iv = result;
        boolean $i$f$toTypedArray = false;
        Collection thisCollection$iv = $this$toTypedArray$iv;
        String[] stringArray = thisCollection$iv.toArray(new String[0]);
        if (stringArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return stringArray;
    }

    public final String[] match(String text, Pattern pattern) {
        return this.match(pattern.matcher(text));
    }

    public final String[] match(String text, String pattern) {
        return this.match(text, Pattern.compile(pattern));
    }

    public final double round(double value, int places) {
        boolean bl = places >= 0;
        boolean bl2 = false;
        boolean bl3 = false;
        bl3 = false;
        boolean bl4 = false;
        if (!bl) {
            boolean bl5 = false;
            String string = "Failed requirement.";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        return BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    private RegexUtils() {
    }

    static {
        RegexUtils regexUtils;
        INSTANCE = regexUtils = new RegexUtils();
    }
}

