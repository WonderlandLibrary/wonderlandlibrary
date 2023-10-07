/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.text;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__RegexExtensionsKt;
import kotlin.text.SystemProperties;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000T\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\u0010\r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0005\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u0002\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0005H\u0087\b\u001a\u0012\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u0007\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\bH\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\tH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\nH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u000bH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\fH\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\u0005H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\rH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u000eH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u0010H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u0011H\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\u0012H\u0087\b\u001a%\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u000e\u0010\u0003\u001a\n\u0018\u00010\u0006j\u0004\u0018\u0001`\u0007H\u0087\b\u001a\u0014\u0010\u0013\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u0007H\u0007\u001a!\u0010\u0014\u001a\u00020\u0015*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\n\u00a8\u0006\u0017"}, d2={"appendln", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "value", "", "", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "Ljava/lang/StringBuffer;", "", "", "", "", "", "", "", "", "", "", "clear", "set", "", "index", "kotlin-stdlib"}, xs="kotlin/text/StringsKt")
class StringsKt__StringBuilderJVMKt
extends StringsKt__RegexExtensionsKt {
    @InlineOnly
    private static final void set(@NotNull StringBuilder $this$set, int index, char value) {
        int $i$f$set = 0;
        Intrinsics.checkParameterIsNotNull($this$set, "$this$set");
        $this$set.setCharAt(index, value);
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final StringBuilder clear(@NotNull StringBuilder $this$clear) {
        Intrinsics.checkParameterIsNotNull($this$clear, "$this$clear");
        StringBuilder stringBuilder = $this$clear;
        boolean bl = false;
        boolean bl2 = false;
        StringBuilder $this$apply = stringBuilder;
        boolean bl3 = false;
        $this$apply.setLength(0);
        return stringBuilder;
    }

    @NotNull
    public static final Appendable appendln(@NotNull Appendable $this$appendln) {
        Intrinsics.checkParameterIsNotNull($this$appendln, "$this$appendln");
        Appendable appendable = $this$appendln.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkExpressionValueIsNotNull(appendable, "append(SystemProperties.LINE_SEPARATOR)");
        return appendable;
    }

    @InlineOnly
    private static final Appendable appendln(@NotNull Appendable $this$appendln, CharSequence value) {
        int $i$f$appendln = 0;
        Appendable appendable = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(appendable, "append(value)");
        return StringsKt.appendln(appendable);
    }

    @InlineOnly
    private static final Appendable appendln(@NotNull Appendable $this$appendln, char value) {
        int $i$f$appendln = 0;
        Appendable appendable = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(appendable, "append(value)");
        return StringsKt.appendln(appendable);
    }

    @NotNull
    public static final StringBuilder appendln(@NotNull StringBuilder $this$appendln) {
        Intrinsics.checkParameterIsNotNull($this$appendln, "$this$appendln");
        StringBuilder stringBuilder = $this$appendln.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(SystemProperties.LINE_SEPARATOR)");
        return stringBuilder;
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, StringBuffer value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, CharSequence value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, String value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, Object value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, StringBuilder value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append((CharSequence)value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, char[] value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, char value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, boolean value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, int value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, short value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value.toInt())");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, byte value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value.toInt())");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, long value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, float value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder $this$appendln, double value) {
        int $i$f$appendln = 0;
        StringBuilder stringBuilder = $this$appendln.append(value);
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "append(value)");
        return StringsKt.appendln(stringBuilder);
    }
}

