/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.text;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Matcher;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.FlagEnum;
import kotlin.text.MatchResult;
import kotlin.text.MatcherMatchResult;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000>\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0014\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0082\b\u001a\u001e\u0010\u0007\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\u0016\u0010\r\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\f\u0010\u000e\u001a\u00020\u000f*\u00020\u0010H\u0002\u001a\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0002\u001a\u0012\u0010\u0012\u001a\u00020\u0006*\b\u0012\u0004\u0012\u00020\u00030\u0013H\u0002\u00a8\u0006\u0014"}, d2={"fromInt", "", "T", "Lkotlin/text/FlagEnum;", "", "value", "", "findNext", "Lkotlin/text/MatchResult;", "Ljava/util/regex/Matcher;", "from", "input", "", "matchEntire", "range", "Lkotlin/ranges/IntRange;", "Ljava/util/regex/MatchResult;", "groupIndex", "toInt", "", "kotlin-stdlib"})
public final class RegexKt {
    /*
     * WARNING - void declaration
     */
    private static final int toInt(Iterable<? extends FlagEnum> $this$toInt) {
        void $this$fold$iv;
        Iterable<? extends FlagEnum> iterable = $this$toInt;
        int initial$iv = 0;
        boolean $i$f$fold = false;
        int accumulator$iv = initial$iv;
        for (Object element$iv : $this$fold$iv) {
            void option;
            FlagEnum flagEnum = (FlagEnum)element$iv;
            int value = accumulator$iv;
            boolean bl = false;
            accumulator$iv = value | option.getValue();
        }
        return accumulator$iv;
    }

    private static final /* synthetic */ <T extends Enum<T>> Set<T> fromInt(int value) {
        EnumSet<Enum> enumSet;
        boolean $i$f$fromInt = false;
        Intrinsics.reifiedOperationMarker(4, "T");
        EnumSet<Enum> $this$fromInt_u24lambda_u2d1 = enumSet = EnumSet.allOf(Enum.class);
        boolean bl = false;
        Intrinsics.checkNotNullExpressionValue($this$fromInt_u24lambda_u2d1, "");
        Iterable iterable = $this$fromInt_u24lambda_u2d1;
        Intrinsics.needClassReification();
        CollectionsKt.retainAll(iterable, (Function1)new Function1<T, Boolean>(value){
            final /* synthetic */ int $value;
            {
                this.$value = $value;
                super(1);
            }

            @NotNull
            public final Boolean invoke(T it) {
                return (this.$value & ((FlagEnum)it).getMask()) == ((FlagEnum)it).getValue();
            }
        });
        Set set = Collections.unmodifiableSet((Set)enumSet);
        Intrinsics.checkNotNullExpressionValue(set, "unmodifiableSet(EnumSet.\u2026mask == it.value }\n    })");
        return set;
    }

    private static final MatchResult findNext(Matcher $this$findNext, int from, CharSequence input) {
        return !$this$findNext.find(from) ? null : (MatchResult)new MatcherMatchResult($this$findNext, input);
    }

    private static final MatchResult matchEntire(Matcher $this$matchEntire, CharSequence input) {
        return !$this$matchEntire.matches() ? null : (MatchResult)new MatcherMatchResult($this$matchEntire, input);
    }

    private static final IntRange range(java.util.regex.MatchResult $this$range) {
        return RangesKt.until($this$range.start(), $this$range.end());
    }

    private static final IntRange range(java.util.regex.MatchResult $this$range, int groupIndex) {
        return RangesKt.until($this$range.start(groupIndex), $this$range.end(groupIndex));
    }

    public static final /* synthetic */ int access$toInt(Iterable $receiver) {
        return RegexKt.toInt($receiver);
    }

    public static final /* synthetic */ MatchResult access$findNext(Matcher $receiver, int from, CharSequence input) {
        return RegexKt.findNext($receiver, from, input);
    }

    public static final /* synthetic */ MatchResult access$matchEntire(Matcher $receiver, CharSequence input) {
        return RegexKt.matchEntire($receiver, input);
    }

    public static final /* synthetic */ IntRange access$range(java.util.regex.MatchResult $receiver) {
        return RegexKt.range($receiver);
    }

    public static final /* synthetic */ IntRange access$range(java.util.regex.MatchResult $receiver, int groupIndex) {
        return RegexKt.range($receiver, groupIndex);
    }
}

