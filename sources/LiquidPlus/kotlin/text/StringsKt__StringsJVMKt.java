/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.text;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.WasExperimental;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.IntIterator;
import kotlin.internal.InlineOnly;
import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__StringNumberConversionsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000~\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\r\n\u0002\b\n\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\n\n\u0002\u0010\f\n\u0002\b\u0011\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u000bH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\u0019\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a)\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a\f\u0010\u0017\u001a\u00020\u0002*\u00020\u0002H\u0007\u001a\u0014\u0010\u0017\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\u0015\u0010\u001a\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0011H\u0087\b\u001a\u0015\u0010\u001c\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0011H\u0087\b\u001a\u001d\u0010\u001d\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a\u001c\u0010 \u001a\u00020\u0011*\u00020\u00022\u0006\u0010!\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a\f\u0010$\u001a\u00020\u0002*\u00020\u0014H\u0007\u001a \u0010$\u001a\u00020\u0002*\u00020\u00142\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0007\u001a\u0019\u0010&\u001a\u00020#*\u0004\u0018\u00010'2\b\u0010!\u001a\u0004\u0018\u00010'H\u0087\u0004\u001a \u0010&\u001a\u00020#*\u0004\u0018\u00010'2\b\u0010!\u001a\u0004\u0018\u00010'2\u0006\u0010\"\u001a\u00020#H\u0007\u001a\u0015\u0010&\u001a\u00020#*\u00020\u00022\u0006\u0010\n\u001a\u00020\tH\u0087\b\u001a\u0015\u0010&\u001a\u00020#*\u00020\u00022\u0006\u0010(\u001a\u00020'H\u0087\b\u001a\f\u0010)\u001a\u00020\u0002*\u00020\u0002H\u0007\u001a\u0014\u0010)\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\f\u0010*\u001a\u00020\u0002*\u00020\rH\u0007\u001a*\u0010*\u001a\u00020\u0002*\u00020\r2\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010+\u001a\u00020#H\u0007\u001a\f\u0010,\u001a\u00020\r*\u00020\u0002H\u0007\u001a*\u0010,\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010+\u001a\u00020#H\u0007\u001a\u001c\u0010-\u001a\u00020#*\u00020\u00022\u0006\u0010.\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a \u0010/\u001a\u00020#*\u0004\u0018\u00010\u00022\b\u0010!\u001a\u0004\u0018\u00010\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a2\u00100\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u00192\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00104\u001a6\u00100\u001a\u00020\u0002*\u00020\u00022\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0004\b5\u00104\u001a*\u00100\u001a\u00020\u0002*\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00106\u001a:\u00100\u001a\u00020\u0002*\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00107\u001a>\u00100\u001a\u00020\u0002*\u00020\u00042\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0004\b5\u00107\u001a2\u00100\u001a\u00020\u0002*\u00020\u00042\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00108\u001a\r\u00109\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\n\u0010:\u001a\u00020#*\u00020'\u001a\r\u0010;\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010;\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\u001d\u0010<\u001a\u00020\u0011*\u00020\u00022\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010<\u001a\u00020\u0011*\u00020\u00022\u0006\u0010@\u001a\u00020\u00022\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010A\u001a\u00020\u0011*\u00020\u00022\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010A\u001a\u00020\u0011*\u00020\u00022\u0006\u0010@\u001a\u00020\u00022\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010B\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u00112\u0006\u0010C\u001a\u00020\u0011H\u0087\b\u001a4\u0010D\u001a\u00020#*\u00020'2\u0006\u0010E\u001a\u00020\u00112\u0006\u0010!\u001a\u00020'2\u0006\u0010F\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a4\u0010D\u001a\u00020#*\u00020\u00022\u0006\u0010E\u001a\u00020\u00112\u0006\u0010!\u001a\u00020\u00022\u0006\u0010F\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a\u0012\u0010G\u001a\u00020\u0002*\u00020'2\u0006\u0010H\u001a\u00020\u0011\u001a$\u0010I\u001a\u00020\u0002*\u00020\u00022\u0006\u0010J\u001a\u00020>2\u0006\u0010K\u001a\u00020>2\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010I\u001a\u00020\u0002*\u00020\u00022\u0006\u0010L\u001a\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010J\u001a\u00020>2\u0006\u0010K\u001a\u00020>2\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010L\u001a\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a\"\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00020P*\u00020'2\u0006\u0010Q\u001a\u00020R2\b\b\u0002\u0010S\u001a\u00020\u0011\u001a\u001c\u0010T\u001a\u00020#*\u00020\u00022\u0006\u0010U\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010T\u001a\u00020#*\u00020\u00022\u0006\u0010U\u001a\u00020\u00022\u0006\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a\u0015\u0010V\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0011H\u0087\b\u001a\u001d\u0010V\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a\u0017\u0010W\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a\r\u0010X\u001a\u00020\u0014*\u00020\u0002H\u0087\b\u001a3\u0010X\u001a\u00020\u0014*\u00020\u00022\u0006\u0010Y\u001a\u00020\u00142\b\b\u0002\u0010Z\u001a\u00020\u00112\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a \u0010X\u001a\u00020\u0014*\u00020\u00022\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0007\u001a\r\u0010[\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010[\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\u0017\u0010\\\u001a\u00020R*\u00020\u00022\b\b\u0002\u0010]\u001a\u00020\u0011H\u0087\b\u001a\r\u0010^\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010^\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\r\u0010_\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010_\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\"%\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u00020\u00020\u0001j\b\u0012\u0004\u0012\u00020\u0002`\u0003*\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006`"}, d2={"CASE_INSENSITIVE_ORDER", "Ljava/util/Comparator;", "", "Lkotlin/Comparator;", "Lkotlin/String$Companion;", "getCASE_INSENSITIVE_ORDER", "(Lkotlin/jvm/internal/StringCompanionObject;)Ljava/util/Comparator;", "String", "stringBuffer", "Ljava/lang/StringBuffer;", "stringBuilder", "Ljava/lang/StringBuilder;", "bytes", "", "charset", "Ljava/nio/charset/Charset;", "offset", "", "length", "chars", "", "codePoints", "", "capitalize", "locale", "Ljava/util/Locale;", "codePointAt", "index", "codePointBefore", "codePointCount", "beginIndex", "endIndex", "compareTo", "other", "ignoreCase", "", "concatToString", "startIndex", "contentEquals", "", "charSequence", "decapitalize", "decodeToString", "throwOnInvalidSequence", "encodeToByteArray", "endsWith", "suffix", "equals", "format", "args", "", "", "(Ljava/lang/String;Ljava/util/Locale;[Ljava/lang/Object;)Ljava/lang/String;", "formatNullable", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "intern", "isBlank", "lowercase", "nativeIndexOf", "ch", "", "fromIndex", "str", "nativeLastIndexOf", "offsetByCodePoints", "codePointOffset", "regionMatches", "thisOffset", "otherOffset", "repeat", "n", "replace", "oldChar", "newChar", "oldValue", "newValue", "replaceFirst", "split", "", "regex", "Ljava/util/regex/Pattern;", "limit", "startsWith", "prefix", "substring", "toByteArray", "toCharArray", "destination", "destinationOffset", "toLowerCase", "toPattern", "flags", "toUpperCase", "uppercase", "kotlin-stdlib"}, xs="kotlin/text/StringsKt")
class StringsKt__StringsJVMKt
extends StringsKt__StringNumberConversionsKt {
    @InlineOnly
    private static final int nativeIndexOf(String $this$nativeIndexOf, char ch, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$nativeIndexOf, "<this>");
        return $this$nativeIndexOf.indexOf(ch, fromIndex);
    }

    @InlineOnly
    private static final int nativeIndexOf(String $this$nativeIndexOf, String str, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$nativeIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(str, "str");
        return $this$nativeIndexOf.indexOf(str, fromIndex);
    }

    @InlineOnly
    private static final int nativeLastIndexOf(String $this$nativeLastIndexOf, char ch, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$nativeLastIndexOf, "<this>");
        return $this$nativeLastIndexOf.lastIndexOf(ch, fromIndex);
    }

    @InlineOnly
    private static final int nativeLastIndexOf(String $this$nativeLastIndexOf, String str, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$nativeLastIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(str, "str");
        return $this$nativeLastIndexOf.lastIndexOf(str, fromIndex);
    }

    public static final boolean equals(@Nullable String $this$equals, @Nullable String other, boolean ignoreCase) {
        if ($this$equals == null) {
            return other == null;
        }
        return !ignoreCase ? $this$equals.equals(other) : $this$equals.equalsIgnoreCase(other);
    }

    public static /* synthetic */ boolean equals$default(String string, String string2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.equals(string, string2, bl);
    }

    @NotNull
    public static final String replace(@NotNull String $this$replace, char oldChar, char newChar, boolean ignoreCase) {
        StringBuilder stringBuilder;
        Intrinsics.checkNotNullParameter($this$replace, "<this>");
        if (!ignoreCase) {
            String string = $this$replace.replace(oldChar, newChar);
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String\u2026replace(oldChar, newChar)");
            return string;
        }
        int n = $this$replace.length();
        StringBuilder $this$replace_u24lambda_u2d1 = stringBuilder = new StringBuilder(n);
        boolean bl = false;
        CharSequence $this$forEach$iv = $this$replace;
        boolean $i$f$forEach = false;
        CharSequence charSequence = $this$forEach$iv;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element$iv = charSequence.charAt(i);
            char c = element$iv;
            boolean bl2 = false;
            $this$replace_u24lambda_u2d1.append(CharsKt.equals(c, oldChar, ignoreCase) ? newChar : c);
        }
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "StringBuilder(capacity).\u2026builderAction).toString()");
        return string;
    }

    public static /* synthetic */ String replace$default(String string, char c, char c2, boolean bl, int n, Object object) {
        if ((n & 4) != 0) {
            bl = false;
        }
        return StringsKt.replace(string, c, c2, bl);
    }

    @NotNull
    public static final String replace(@NotNull String $this$replace, @NotNull String oldValue, @NotNull String newValue, boolean ignoreCase) {
        String string;
        Intrinsics.checkNotNullParameter($this$replace, "<this>");
        Intrinsics.checkNotNullParameter(oldValue, "oldValue");
        Intrinsics.checkNotNullParameter(newValue, "newValue");
        String $this$replace_u24lambda_u2d2 = string = $this$replace;
        boolean bl = false;
        int occurrenceIndex = StringsKt.indexOf((CharSequence)$this$replace_u24lambda_u2d2, oldValue, 0, ignoreCase);
        if (occurrenceIndex < 0) {
            return $this$replace_u24lambda_u2d2;
        }
        int oldValueLength = oldValue.length();
        int searchStep = RangesKt.coerceAtLeast(oldValueLength, 1);
        int newLengthHint = $this$replace_u24lambda_u2d2.length() - oldValueLength + newValue.length();
        if (newLengthHint < 0) {
            throw new OutOfMemoryError();
        }
        StringBuilder stringBuilder = new StringBuilder(newLengthHint);
        int i = 0;
        do {
            stringBuilder.append($this$replace_u24lambda_u2d2, i, occurrenceIndex).append(newValue);
            i = occurrenceIndex + oldValueLength;
        } while (occurrenceIndex < $this$replace_u24lambda_u2d2.length() && (occurrenceIndex = StringsKt.indexOf((CharSequence)$this$replace_u24lambda_u2d2, oldValue, occurrenceIndex + searchStep, ignoreCase)) > 0);
        String string2 = stringBuilder.append($this$replace_u24lambda_u2d2, i, $this$replace_u24lambda_u2d2.length()).toString();
        Intrinsics.checkNotNullExpressionValue(string2, "stringBuilder.append(this, i, length).toString()");
        return string2;
    }

    public static /* synthetic */ String replace$default(String string, String string2, String string3, boolean bl, int n, Object object) {
        if ((n & 4) != 0) {
            bl = false;
        }
        return StringsKt.replace(string, string2, string3, bl);
    }

    @NotNull
    public static final String replaceFirst(@NotNull String $this$replaceFirst, char oldChar, char newChar, boolean ignoreCase) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceFirst, "<this>");
        int index = StringsKt.indexOf$default((CharSequence)$this$replaceFirst, oldChar, 0, ignoreCase, 2, null);
        if (index < 0) {
            string = $this$replaceFirst;
        } else {
            String string2 = $this$replaceFirst;
            int n = index + 1;
            CharSequence charSequence = String.valueOf(newChar);
            string = ((Object)StringsKt.replaceRange((CharSequence)string2, index, n, charSequence)).toString();
        }
        return string;
    }

    public static /* synthetic */ String replaceFirst$default(String string, char c, char c2, boolean bl, int n, Object object) {
        if ((n & 4) != 0) {
            bl = false;
        }
        return StringsKt.replaceFirst(string, c, c2, bl);
    }

    @NotNull
    public static final String replaceFirst(@NotNull String $this$replaceFirst, @NotNull String oldValue, @NotNull String newValue, boolean ignoreCase) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceFirst, "<this>");
        Intrinsics.checkNotNullParameter(oldValue, "oldValue");
        Intrinsics.checkNotNullParameter(newValue, "newValue");
        int index = StringsKt.indexOf$default((CharSequence)$this$replaceFirst, oldValue, 0, ignoreCase, 2, null);
        if (index < 0) {
            string = $this$replaceFirst;
        } else {
            String string2 = $this$replaceFirst;
            int n = index + oldValue.length();
            string = ((Object)StringsKt.replaceRange((CharSequence)string2, index, n, (CharSequence)newValue)).toString();
        }
        return string;
    }

    public static /* synthetic */ String replaceFirst$default(String string, String string2, String string3, boolean bl, int n, Object object) {
        if ((n & 4) != 0) {
            bl = false;
        }
        return StringsKt.replaceFirst(string, string2, string3, bl);
    }

    @Deprecated(message="Use uppercase() instead.", replaceWith=@ReplaceWith(expression="uppercase(Locale.getDefault())", imports={"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @InlineOnly
    private static final String toUpperCase(String $this$toUpperCase) {
        Intrinsics.checkNotNullParameter($this$toUpperCase, "<this>");
        String string = $this$toUpperCase.toUpperCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toUpperCase()");
        return string;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final String uppercase(String $this$uppercase) {
        Intrinsics.checkNotNullParameter($this$uppercase, "<this>");
        String string = $this$uppercase.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toUpperCase(Locale.ROOT)");
        return string;
    }

    @Deprecated(message="Use lowercase() instead.", replaceWith=@ReplaceWith(expression="lowercase(Locale.getDefault())", imports={"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @InlineOnly
    private static final String toLowerCase(String $this$toLowerCase) {
        Intrinsics.checkNotNullParameter($this$toLowerCase, "<this>");
        String string = $this$toLowerCase.toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        return string;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final String lowercase(String $this$lowercase) {
        Intrinsics.checkNotNullParameter($this$lowercase, "<this>");
        String string = $this$lowercase.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase(Locale.ROOT)");
        return string;
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final String concatToString(@NotNull char[] $this$concatToString) {
        Intrinsics.checkNotNullParameter($this$concatToString, "<this>");
        return new String($this$concatToString);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final String concatToString(@NotNull char[] $this$concatToString, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$concatToString, "<this>");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(startIndex, endIndex, $this$concatToString.length);
        int n = endIndex - startIndex;
        return new String($this$concatToString, startIndex, n);
    }

    public static /* synthetic */ String concatToString$default(char[] cArray, int n, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = cArray.length;
        }
        return StringsKt.concatToString(cArray, n, n2);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final char[] toCharArray(@NotNull String $this$toCharArray, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$toCharArray, "<this>");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(startIndex, endIndex, $this$toCharArray.length());
        String string = $this$toCharArray;
        char[] cArray = new char[endIndex - startIndex];
        int n = 0;
        string.getChars(startIndex, endIndex, cArray, n);
        return cArray;
    }

    public static /* synthetic */ char[] toCharArray$default(String string, int n, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = string.length();
        }
        return StringsKt.toCharArray(string, n, n2);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final String decodeToString(@NotNull byte[] $this$decodeToString) {
        Intrinsics.checkNotNullParameter($this$decodeToString, "<this>");
        return new String($this$decodeToString, Charsets.UTF_8);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final String decodeToString(@NotNull byte[] $this$decodeToString, int startIndex, int endIndex, boolean throwOnInvalidSequence) {
        Intrinsics.checkNotNullParameter($this$decodeToString, "<this>");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(startIndex, endIndex, $this$decodeToString.length);
        if (!throwOnInvalidSequence) {
            int n = endIndex - startIndex;
            return new String($this$decodeToString, startIndex, n, Charsets.UTF_8);
        }
        CharsetDecoder decoder = Charsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        String string = decoder.decode(ByteBuffer.wrap($this$decodeToString, startIndex, endIndex - startIndex)).toString();
        Intrinsics.checkNotNullExpressionValue(string, "decoder.decode(ByteBuffe\u2026- startIndex)).toString()");
        return string;
    }

    public static /* synthetic */ String decodeToString$default(byte[] byArray, int n, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = byArray.length;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.decodeToString(byArray, n, n2, bl);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final byte[] encodeToByteArray(@NotNull String $this$encodeToByteArray) {
        Intrinsics.checkNotNullParameter($this$encodeToByteArray, "<this>");
        String string = $this$encodeToByteArray;
        Charset charset = Charsets.UTF_8;
        byte[] byArray = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "this as java.lang.String).getBytes(charset)");
        return byArray;
    }

    /*
     * Enabled aggressive block sorting
     */
    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final byte[] encodeToByteArray(@NotNull String $this$encodeToByteArray, int startIndex, int endIndex, boolean throwOnInvalidSequence) {
        byte[] byArray;
        byte[] byArray2;
        Intrinsics.checkNotNullParameter($this$encodeToByteArray, "<this>");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(startIndex, endIndex, $this$encodeToByteArray.length());
        if (!throwOnInvalidSequence) {
            String string = $this$encodeToByteArray;
            Object object = string.substring(startIndex, endIndex);
            Intrinsics.checkNotNullExpressionValue(object, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            string = object;
            object = Charsets.UTF_8;
            byte[] byArray3 = string.getBytes((Charset)object);
            Intrinsics.checkNotNullExpressionValue(byArray3, "this as java.lang.String).getBytes(charset)");
            return byArray3;
        }
        CharsetEncoder encoder = Charsets.UTF_8.newEncoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        ByteBuffer byteBuffer = encoder.encode(CharBuffer.wrap($this$encodeToByteArray, startIndex, endIndex));
        if (byteBuffer.hasArray() && byteBuffer.arrayOffset() == 0) {
            int n = byteBuffer.remaining();
            byte[] byArray4 = byteBuffer.array();
            Intrinsics.checkNotNull(byArray4);
            if (n == byArray4.length) {
                byte[] byArray5 = byteBuffer.array();
                Intrinsics.checkNotNullExpressionValue(byArray5, "{\n        byteBuffer.array()\n    }");
                byArray2 = byArray5;
                return byArray2;
            }
        }
        byte[] it = byArray = new byte[byteBuffer.remaining()];
        boolean bl = false;
        byteBuffer.get(it);
        byArray2 = byArray;
        return byArray2;
    }

    public static /* synthetic */ byte[] encodeToByteArray$default(String string, int n, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = string.length();
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.encodeToByteArray(string, n, n2, bl);
    }

    @InlineOnly
    private static final char[] toCharArray(String $this$toCharArray) {
        Intrinsics.checkNotNullParameter($this$toCharArray, "<this>");
        char[] cArray = $this$toCharArray.toCharArray();
        Intrinsics.checkNotNullExpressionValue(cArray, "this as java.lang.String).toCharArray()");
        return cArray;
    }

    @InlineOnly
    private static final char[] toCharArray(String $this$toCharArray, char[] destination, int destinationOffset, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$toCharArray, "<this>");
        Intrinsics.checkNotNullParameter(destination, "destination");
        $this$toCharArray.getChars(startIndex, endIndex, destination, destinationOffset);
        return destination;
    }

    static /* synthetic */ char[] toCharArray$default(String $this$toCharArray_u24default, char[] destination, int destinationOffset, int startIndex, int endIndex, int n, Object object) {
        if ((n & 2) != 0) {
            destinationOffset = 0;
        }
        if ((n & 4) != 0) {
            startIndex = 0;
        }
        if ((n & 8) != 0) {
            endIndex = $this$toCharArray_u24default.length();
        }
        Intrinsics.checkNotNullParameter($this$toCharArray_u24default, "<this>");
        Intrinsics.checkNotNullParameter(destination, "destination");
        $this$toCharArray_u24default.getChars(startIndex, endIndex, destination, destinationOffset);
        return destination;
    }

    @InlineOnly
    private static final String format(String $this$format, Object ... args2) {
        Intrinsics.checkNotNullParameter($this$format, "<this>");
        Intrinsics.checkNotNullParameter(args2, "args");
        String string = String.format($this$format, Arrays.copyOf(args2, args2.length));
        Intrinsics.checkNotNullExpressionValue(string, "format(this, *args)");
        return string;
    }

    @InlineOnly
    private static final String format(StringCompanionObject $this$format, String format, Object ... args2) {
        Intrinsics.checkNotNullParameter($this$format, "<this>");
        Intrinsics.checkNotNullParameter(format, "format");
        Intrinsics.checkNotNullParameter(args2, "args");
        String string = String.format(format, Arrays.copyOf(args2, args2.length));
        Intrinsics.checkNotNullExpressionValue(string, "format(format, *args)");
        return string;
    }

    @Deprecated(message="Use Kotlin compiler 1.4 to avoid deprecation warning.")
    @DeprecatedSinceKotlin(hiddenSince="1.4")
    @InlineOnly
    private static final /* synthetic */ String format(String $this$format, Locale locale, Object ... args2) {
        Intrinsics.checkNotNullParameter($this$format, "<this>");
        Intrinsics.checkNotNullParameter(locale, "locale");
        Intrinsics.checkNotNullParameter(args2, "args");
        String string = String.format(locale, $this$format, Arrays.copyOf(args2, args2.length));
        Intrinsics.checkNotNullExpressionValue(string, "format(locale, this, *args)");
        return string;
    }

    @SinceKotlin(version="1.4")
    @JvmName(name="formatNullable")
    @InlineOnly
    private static final String formatNullable(String $this$format, Locale locale, Object ... args2) {
        Intrinsics.checkNotNullParameter($this$format, "<this>");
        Intrinsics.checkNotNullParameter(args2, "args");
        String string = String.format(locale, $this$format, Arrays.copyOf(args2, args2.length));
        Intrinsics.checkNotNullExpressionValue(string, "format(locale, this, *args)");
        return string;
    }

    @Deprecated(message="Use Kotlin compiler 1.4 to avoid deprecation warning.")
    @DeprecatedSinceKotlin(hiddenSince="1.4")
    @InlineOnly
    private static final /* synthetic */ String format(StringCompanionObject $this$format, Locale locale, String format, Object ... args2) {
        Intrinsics.checkNotNullParameter($this$format, "<this>");
        Intrinsics.checkNotNullParameter(locale, "locale");
        Intrinsics.checkNotNullParameter(format, "format");
        Intrinsics.checkNotNullParameter(args2, "args");
        String string = String.format(locale, format, Arrays.copyOf(args2, args2.length));
        Intrinsics.checkNotNullExpressionValue(string, "format(locale, format, *args)");
        return string;
    }

    @SinceKotlin(version="1.4")
    @JvmName(name="formatNullable")
    @InlineOnly
    private static final String formatNullable(StringCompanionObject $this$format, Locale locale, String format, Object ... args2) {
        Intrinsics.checkNotNullParameter($this$format, "<this>");
        Intrinsics.checkNotNullParameter(format, "format");
        Intrinsics.checkNotNullParameter(args2, "args");
        String string = String.format(locale, format, Arrays.copyOf(args2, args2.length));
        Intrinsics.checkNotNullExpressionValue(string, "format(locale, format, *args)");
        return string;
    }

    @NotNull
    public static final List<String> split(@NotNull CharSequence $this$split, @NotNull Pattern regex, int limit) {
        Intrinsics.checkNotNullParameter($this$split, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        StringsKt.requireNonNegativeLimit(limit);
        String[] stringArray = regex.split($this$split, limit == 0 ? -1 : limit);
        Intrinsics.checkNotNullExpressionValue(stringArray, "regex.split(this, if (limit == 0) -1 else limit)");
        return ArraysKt.asList((Object[])stringArray);
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, Pattern pattern, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        return StringsKt.split(charSequence, pattern, n);
    }

    @InlineOnly
    private static final String substring(String $this$substring, int startIndex) {
        Intrinsics.checkNotNullParameter($this$substring, "<this>");
        String string = $this$substring.substring(startIndex);
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).substring(startIndex)");
        return string;
    }

    @InlineOnly
    private static final String substring(String $this$substring, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$substring, "<this>");
        String string = $this$substring.substring(startIndex, endIndex);
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String\u2026ing(startIndex, endIndex)");
        return string;
    }

    public static final boolean startsWith(@NotNull String $this$startsWith, @NotNull String prefix, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!ignoreCase) {
            return $this$startsWith.startsWith(prefix);
        }
        return StringsKt.regionMatches($this$startsWith, 0, prefix, 0, prefix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(String string, String string2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(string, string2, bl);
    }

    public static final boolean startsWith(@NotNull String $this$startsWith, @NotNull String prefix, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!ignoreCase) {
            return $this$startsWith.startsWith(prefix, startIndex);
        }
        return StringsKt.regionMatches($this$startsWith, startIndex, prefix, 0, prefix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(String string, String string2, int n, boolean bl, int n2, Object object) {
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(string, string2, n, bl);
    }

    public static final boolean endsWith(@NotNull String $this$endsWith, @NotNull String suffix, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        if (!ignoreCase) {
            return $this$endsWith.endsWith(suffix);
        }
        return StringsKt.regionMatches($this$endsWith, $this$endsWith.length() - suffix.length(), suffix, 0, suffix.length(), true);
    }

    public static /* synthetic */ boolean endsWith$default(String string, String string2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.endsWith(string, string2, bl);
    }

    @InlineOnly
    private static final String String(byte[] bytes, int offset, int length, Charset charset) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        Intrinsics.checkNotNullParameter(charset, "charset");
        return new String(bytes, offset, length, charset);
    }

    @InlineOnly
    private static final String String(byte[] bytes, Charset charset) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        Intrinsics.checkNotNullParameter(charset, "charset");
        return new String(bytes, charset);
    }

    @InlineOnly
    private static final String String(byte[] bytes, int offset, int length) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return new String(bytes, offset, length, Charsets.UTF_8);
    }

    @InlineOnly
    private static final String String(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return new String(bytes, Charsets.UTF_8);
    }

    @InlineOnly
    private static final String String(char[] chars) {
        Intrinsics.checkNotNullParameter(chars, "chars");
        return new String(chars);
    }

    @InlineOnly
    private static final String String(char[] chars, int offset, int length) {
        Intrinsics.checkNotNullParameter(chars, "chars");
        return new String(chars, offset, length);
    }

    @InlineOnly
    private static final String String(int[] codePoints, int offset, int length) {
        Intrinsics.checkNotNullParameter(codePoints, "codePoints");
        return new String(codePoints, offset, length);
    }

    @InlineOnly
    private static final String String(StringBuffer stringBuffer) {
        Intrinsics.checkNotNullParameter(stringBuffer, "stringBuffer");
        return new String(stringBuffer);
    }

    @InlineOnly
    private static final String String(StringBuilder stringBuilder) {
        Intrinsics.checkNotNullParameter(stringBuilder, "stringBuilder");
        return new String(stringBuilder);
    }

    @InlineOnly
    private static final int codePointAt(String $this$codePointAt, int index) {
        Intrinsics.checkNotNullParameter($this$codePointAt, "<this>");
        return $this$codePointAt.codePointAt(index);
    }

    @InlineOnly
    private static final int codePointBefore(String $this$codePointBefore, int index) {
        Intrinsics.checkNotNullParameter($this$codePointBefore, "<this>");
        return $this$codePointBefore.codePointBefore(index);
    }

    @InlineOnly
    private static final int codePointCount(String $this$codePointCount, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$codePointCount, "<this>");
        return $this$codePointCount.codePointCount(beginIndex, endIndex);
    }

    public static final int compareTo(@NotNull String $this$compareTo, @NotNull String other, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$compareTo, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        if (ignoreCase) {
            return $this$compareTo.compareToIgnoreCase(other);
        }
        return $this$compareTo.compareTo(other);
    }

    public static /* synthetic */ int compareTo$default(String string, String string2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.compareTo(string, string2, bl);
    }

    @InlineOnly
    private static final boolean contentEquals(String $this$contentEquals, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter($this$contentEquals, "<this>");
        Intrinsics.checkNotNullParameter(charSequence, "charSequence");
        return $this$contentEquals.contentEquals(charSequence);
    }

    @InlineOnly
    private static final boolean contentEquals(String $this$contentEquals, StringBuffer stringBuilder) {
        Intrinsics.checkNotNullParameter($this$contentEquals, "<this>");
        Intrinsics.checkNotNullParameter(stringBuilder, "stringBuilder");
        return $this$contentEquals.contentEquals(stringBuilder);
    }

    @SinceKotlin(version="1.5")
    public static final boolean contentEquals(@Nullable CharSequence $this$contentEquals, @Nullable CharSequence other) {
        boolean bl;
        if ($this$contentEquals instanceof String && other != null) {
            String string = (String)$this$contentEquals;
            bl = string.contentEquals(other);
        } else {
            bl = StringsKt.contentEqualsImpl($this$contentEquals, other);
        }
        return bl;
    }

    @SinceKotlin(version="1.5")
    public static final boolean contentEquals(@Nullable CharSequence $this$contentEquals, @Nullable CharSequence other, boolean ignoreCase) {
        return ignoreCase ? StringsKt.contentEqualsIgnoreCaseImpl($this$contentEquals, other) : StringsKt.contentEquals($this$contentEquals, other);
    }

    @InlineOnly
    private static final String intern(String $this$intern) {
        Intrinsics.checkNotNullParameter($this$intern, "<this>");
        String string = $this$intern.intern();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).intern()");
        return string;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final boolean isBlank(@NotNull CharSequence $this$isBlank) {
        int it;
        Intrinsics.checkNotNullParameter($this$isBlank, "<this>");
        if ($this$isBlank.length() == 0) return true;
        Iterable $this$all$iv = StringsKt.getIndices($this$isBlank);
        boolean $i$f$all = false;
        if ($this$all$iv instanceof Collection && ((Collection)$this$all$iv).isEmpty()) {
            return true;
        }
        Iterator iterator2 = $this$all$iv.iterator();
        do {
            int element$iv;
            if (!iterator2.hasNext()) return true;
            it = element$iv = ((IntIterator)iterator2).nextInt();
            boolean bl = false;
        } while (CharsKt.isWhitespace($this$isBlank.charAt(it)));
        return false;
    }

    @InlineOnly
    private static final int offsetByCodePoints(String $this$offsetByCodePoints, int index, int codePointOffset) {
        Intrinsics.checkNotNullParameter($this$offsetByCodePoints, "<this>");
        return $this$offsetByCodePoints.offsetByCodePoints(index, codePointOffset);
    }

    public static final boolean regionMatches(@NotNull CharSequence $this$regionMatches, int thisOffset, @NotNull CharSequence other, int otherOffset, int length, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$regionMatches, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        if ($this$regionMatches instanceof String && other instanceof String) {
            return StringsKt.regionMatches((String)$this$regionMatches, thisOffset, (String)other, otherOffset, length, ignoreCase);
        }
        return StringsKt.regionMatchesImpl($this$regionMatches, thisOffset, other, otherOffset, length, ignoreCase);
    }

    public static /* synthetic */ boolean regionMatches$default(CharSequence charSequence, int n, CharSequence charSequence2, int n2, int n3, boolean bl, int n4, Object object) {
        if ((n4 & 0x10) != 0) {
            bl = false;
        }
        return StringsKt.regionMatches(charSequence, n, charSequence2, n2, n3, bl);
    }

    public static final boolean regionMatches(@NotNull String $this$regionMatches, int thisOffset, @NotNull String other, int otherOffset, int length, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$regionMatches, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        return !ignoreCase ? $this$regionMatches.regionMatches(thisOffset, other, otherOffset, length) : $this$regionMatches.regionMatches(ignoreCase, thisOffset, other, otherOffset, length);
    }

    public static /* synthetic */ boolean regionMatches$default(String string, int n, String string2, int n2, int n3, boolean bl, int n4, Object object) {
        if ((n4 & 0x10) != 0) {
            bl = false;
        }
        return StringsKt.regionMatches(string, n, string2, n2, n3, bl);
    }

    @Deprecated(message="Use lowercase() instead.", replaceWith=@ReplaceWith(expression="lowercase(locale)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @InlineOnly
    private static final String toLowerCase(String $this$toLowerCase, Locale locale) {
        Intrinsics.checkNotNullParameter($this$toLowerCase, "<this>");
        Intrinsics.checkNotNullParameter(locale, "locale");
        String string = $this$toLowerCase;
        String string2 = string.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String).toLowerCase(locale)");
        return string2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final String lowercase(String $this$lowercase, Locale locale) {
        Intrinsics.checkNotNullParameter($this$lowercase, "<this>");
        Intrinsics.checkNotNullParameter(locale, "locale");
        String string = $this$lowercase.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase(locale)");
        return string;
    }

    @Deprecated(message="Use uppercase() instead.", replaceWith=@ReplaceWith(expression="uppercase(locale)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @InlineOnly
    private static final String toUpperCase(String $this$toUpperCase, Locale locale) {
        Intrinsics.checkNotNullParameter($this$toUpperCase, "<this>");
        Intrinsics.checkNotNullParameter(locale, "locale");
        String string = $this$toUpperCase;
        String string2 = string.toUpperCase(locale);
        Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String).toUpperCase(locale)");
        return string2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final String uppercase(String $this$uppercase, Locale locale) {
        Intrinsics.checkNotNullParameter($this$uppercase, "<this>");
        Intrinsics.checkNotNullParameter(locale, "locale");
        String string = $this$uppercase.toUpperCase(locale);
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toUpperCase(locale)");
        return string;
    }

    @InlineOnly
    private static final byte[] toByteArray(String $this$toByteArray, Charset charset) {
        Intrinsics.checkNotNullParameter($this$toByteArray, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        byte[] byArray = $this$toByteArray.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "this as java.lang.String).getBytes(charset)");
        return byArray;
    }

    static /* synthetic */ byte[] toByteArray$default(String $this$toByteArray_u24default, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter($this$toByteArray_u24default, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        byte[] byArray = $this$toByteArray_u24default.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "this as java.lang.String).getBytes(charset)");
        return byArray;
    }

    @InlineOnly
    private static final Pattern toPattern(String $this$toPattern, int flags) {
        Intrinsics.checkNotNullParameter($this$toPattern, "<this>");
        Pattern pattern = Pattern.compile($this$toPattern, flags);
        Intrinsics.checkNotNullExpressionValue(pattern, "compile(this, flags)");
        return pattern;
    }

    static /* synthetic */ Pattern toPattern$default(String $this$toPattern_u24default, int flags, int n, Object object) {
        if ((n & 1) != 0) {
            flags = 0;
        }
        Intrinsics.checkNotNullParameter($this$toPattern_u24default, "<this>");
        Pattern pattern = Pattern.compile($this$toPattern_u24default, flags);
        Intrinsics.checkNotNullExpressionValue(pattern, "compile(this, flags)");
        return pattern;
    }

    @Deprecated(message="Use replaceFirstChar instead.", replaceWith=@ReplaceWith(expression="replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }", imports={"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @NotNull
    public static final String capitalize(@NotNull String $this$capitalize) {
        Intrinsics.checkNotNullParameter($this$capitalize, "<this>");
        Locale locale = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
        return StringsKt.capitalize($this$capitalize, locale);
    }

    @Deprecated(message="Use replaceFirstChar instead.", replaceWith=@ReplaceWith(expression="replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @LowPriorityInOverloadResolution
    @NotNull
    public static final String capitalize(@NotNull String $this$capitalize, @NotNull Locale locale) {
        char firstChar;
        Intrinsics.checkNotNullParameter($this$capitalize, "<this>");
        Intrinsics.checkNotNullParameter(locale, "locale");
        if (((CharSequence)$this$capitalize).length() > 0 && Character.isLowerCase(firstChar = $this$capitalize.charAt(0))) {
            String string;
            StringBuilder stringBuilder;
            StringBuilder $this$capitalize_u24lambda_u2d5 = stringBuilder = new StringBuilder();
            boolean bl = false;
            char titleChar = Character.toTitleCase(firstChar);
            if (titleChar != Character.toUpperCase(firstChar)) {
                $this$capitalize_u24lambda_u2d5.append(titleChar);
            } else {
                string = $this$capitalize;
                int n = 0;
                int n2 = 1;
                String string2 = string.substring(n, n2);
                Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                string = string2;
                String string3 = string.toUpperCase(locale);
                Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String).toUpperCase(locale)");
                $this$capitalize_u24lambda_u2d5.append(string3);
            }
            string = $this$capitalize;
            int n = 1;
            String string4 = string.substring(n);
            Intrinsics.checkNotNullExpressionValue(string4, "this as java.lang.String).substring(startIndex)");
            $this$capitalize_u24lambda_u2d5.append(string4);
            String string5 = stringBuilder.toString();
            Intrinsics.checkNotNullExpressionValue(string5, "StringBuilder().apply(builderAction).toString()");
            return string5;
        }
        return $this$capitalize;
    }

    @Deprecated(message="Use replaceFirstChar instead.", replaceWith=@ReplaceWith(expression="replaceFirstChar { it.lowercase(Locale.getDefault()) }", imports={"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @NotNull
    public static final String decapitalize(@NotNull String $this$decapitalize) {
        String string;
        Intrinsics.checkNotNullParameter($this$decapitalize, "<this>");
        if (((CharSequence)$this$decapitalize).length() > 0 && !Character.isLowerCase($this$decapitalize.charAt(0))) {
            String string2 = $this$decapitalize;
            int n = 0;
            int n2 = 1;
            String string3 = string2.substring(n, n2);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            String string4 = string3.toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string4, "this as java.lang.String).toLowerCase()");
            string2 = $this$decapitalize;
            int n3 = 1;
            String string5 = string2.substring(n3);
            Intrinsics.checkNotNullExpressionValue(string5, "this as java.lang.String).substring(startIndex)");
            string = Intrinsics.stringPlus(string4, string5);
        } else {
            string = $this$decapitalize;
        }
        return string;
    }

    @Deprecated(message="Use replaceFirstChar instead.", replaceWith=@ReplaceWith(expression="replaceFirstChar { it.lowercase(locale) }", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @LowPriorityInOverloadResolution
    @NotNull
    public static final String decapitalize(@NotNull String $this$decapitalize, @NotNull Locale locale) {
        String string;
        Intrinsics.checkNotNullParameter($this$decapitalize, "<this>");
        Intrinsics.checkNotNullParameter(locale, "locale");
        if (((CharSequence)$this$decapitalize).length() > 0 && !Character.isLowerCase($this$decapitalize.charAt(0))) {
            String string2 = $this$decapitalize;
            int n = 0;
            int n2 = 1;
            String string3 = string2.substring(n, n2);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            string2 = string3;
            String string4 = string2.toLowerCase(locale);
            Intrinsics.checkNotNullExpressionValue(string4, "this as java.lang.String).toLowerCase(locale)");
            string2 = $this$decapitalize;
            int n3 = 1;
            String string5 = string2.substring(n3);
            Intrinsics.checkNotNullExpressionValue(string5, "this as java.lang.String).substring(startIndex)");
            string = Intrinsics.stringPlus(string4, string5);
        } else {
            string = $this$decapitalize;
        }
        return string;
    }

    @NotNull
    public static final String repeat(@NotNull CharSequence $this$repeat, int n) {
        String string;
        int n2;
        Intrinsics.checkNotNullParameter($this$repeat, "<this>");
        int n3 = n2 = n >= 0 ? 1 : 0;
        if (n2 == 0) {
            boolean bl = false;
            String string2 = "Count 'n' must be non-negative, but was " + n + '.';
            throw new IllegalArgumentException(string2.toString());
        }
        n2 = n;
        block0 : switch (n2) {
            case 0: {
                string = "";
                break;
            }
            case 1: {
                string = ((Object)$this$repeat).toString();
                break;
            }
            default: {
                int n4 = $this$repeat.length();
                switch (n4) {
                    case 0: {
                        string = "";
                        break block0;
                    }
                    case 1: {
                        char c;
                        char c2 = c = $this$repeat.charAt(0);
                        boolean bl = false;
                        int n5 = 0;
                        int n6 = n;
                        char[] cArray = new char[n6];
                        while (n5 < n6) {
                            int n7 = n5++;
                            cArray[n7] = c2;
                        }
                        char[] cArray2 = cArray;
                        string = new String(cArray2);
                        break block0;
                    }
                }
                StringBuilder sb = new StringBuilder(n * $this$repeat.length());
                int n8 = 1;
                if (n8 <= n) {
                    int i;
                    do {
                        i = n8++;
                        sb.append($this$repeat);
                    } while (i != n);
                }
                String string3 = sb.toString();
                Intrinsics.checkNotNullExpressionValue(string3, "{\n                    va\u2026tring()\n                }");
                string = string3;
            }
        }
        return string;
    }

    @NotNull
    public static final Comparator<String> getCASE_INSENSITIVE_ORDER(@NotNull StringCompanionObject $this$CASE_INSENSITIVE_ORDER) {
        Intrinsics.checkNotNullParameter($this$CASE_INSENSITIVE_ORDER, "<this>");
        Comparator comparator = String.CASE_INSENSITIVE_ORDER;
        Intrinsics.checkNotNullExpressionValue(comparator, "CASE_INSENSITIVE_ORDER");
        return comparator;
    }
}

