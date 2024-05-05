/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ!\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ!\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/utils/RegexUtils;", "", "()V", "match", "", "", "matcher", "Ljava/util/regex/Matcher;", "(Ljava/util/regex/Matcher;)[Ljava/lang/String;", "text", "pattern", "Ljava/util/regex/Pattern;", "(Ljava/lang/String;Ljava/util/regex/Pattern;)[Ljava/lang/String;", "(Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;", "round", "", "value", "places", "", "KyinoClient"})
public final class RegexUtils {
    public static final RegexUtils INSTANCE;

    @NotNull
    public final String[] match(@NotNull Matcher matcher) {
        Intrinsics.checkParameterIsNotNull(matcher, "matcher");
        boolean bl = false;
        List result = new ArrayList();
        while (matcher.find()) {
            String string = matcher.group();
            Intrinsics.checkExpressionValueIsNotNull(string, "matcher.group()");
            result.add(string);
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

    @NotNull
    public final String[] match(@NotNull String text, @NotNull Pattern pattern) {
        Intrinsics.checkParameterIsNotNull(text, "text");
        Intrinsics.checkParameterIsNotNull(pattern, "pattern");
        Matcher matcher = pattern.matcher(text);
        Intrinsics.checkExpressionValueIsNotNull(matcher, "pattern.matcher(text)");
        return this.match(matcher);
    }

    @NotNull
    public final String[] match(@NotNull String text, @NotNull String pattern) {
        Intrinsics.checkParameterIsNotNull(text, "text");
        Intrinsics.checkParameterIsNotNull(pattern, "pattern");
        Pattern pattern2 = Pattern.compile(pattern);
        Intrinsics.checkExpressionValueIsNotNull(pattern2, "Pattern.compile(pattern)");
        return this.match(text, pattern2);
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

