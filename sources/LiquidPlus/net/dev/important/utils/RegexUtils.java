/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ!\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ!\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012\u00a8\u0006\u0013"}, d2={"Lnet/dev/important/utils/RegexUtils;", "", "()V", "match", "", "", "matcher", "Ljava/util/regex/Matcher;", "(Ljava/util/regex/Matcher;)[Ljava/lang/String;", "text", "pattern", "Ljava/util/regex/Pattern;", "(Ljava/lang/String;Ljava/util/regex/Pattern;)[Ljava/lang/String;", "(Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;", "round", "", "value", "places", "", "LiquidBounce"})
public final class RegexUtils {
    @NotNull
    public static final RegexUtils INSTANCE = new RegexUtils();

    private RegexUtils() {
    }

    @NotNull
    public final String[] match(@NotNull Matcher matcher) {
        Intrinsics.checkNotNullParameter(matcher, "matcher");
        List result = new ArrayList();
        while (matcher.find()) {
            String string = matcher.group();
            Intrinsics.checkNotNullExpressionValue(string, "matcher.group()");
            result.add(string);
        }
        Collection $this$toTypedArray$iv = result;
        boolean $i$f$toTypedArray = false;
        Collection thisCollection$iv = $this$toTypedArray$iv;
        String[] stringArray = thisCollection$iv.toArray(new String[0]);
        if (stringArray == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }
        return stringArray;
    }

    @NotNull
    public final String[] match(@NotNull String text, @NotNull Pattern pattern) {
        Intrinsics.checkNotNullParameter(text, "text");
        Intrinsics.checkNotNullParameter(pattern, "pattern");
        Matcher matcher = pattern.matcher(text);
        Intrinsics.checkNotNullExpressionValue(matcher, "pattern.matcher(text)");
        return this.match(matcher);
    }

    @NotNull
    public final String[] match(@NotNull String text, @NotNull String pattern) {
        Intrinsics.checkNotNullParameter(text, "text");
        Intrinsics.checkNotNullParameter(pattern, "pattern");
        Pattern pattern2 = Pattern.compile(pattern);
        Intrinsics.checkNotNullExpressionValue(pattern2, "compile(pattern)");
        return this.match(text, pattern2);
    }

    public final double round(double value, int places) {
        if (!(places >= 0)) {
            String string = "Failed requirement.";
            throw new IllegalArgumentException(string.toString());
        }
        return BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
}

