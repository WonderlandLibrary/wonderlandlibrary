/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.text;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001BY\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012:\u0010\b\u001a6\u0012\u0004\u0012\u00020\u0004\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0018\u00010\r0\t\u00a2\u0006\u0002\b\u000e\u00a2\u0006\u0002\u0010\u000fJ\u000f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00020\u0011H\u0096\u0002RB\u0010\b\u001a6\u0012\u0004\u0012\u00020\u0004\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0018\u00010\r0\t\u00a2\u0006\u0002\b\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lkotlin/text/DelimitedRangesSequence;", "Lkotlin/sequences/Sequence;", "Lkotlin/ranges/IntRange;", "input", "", "startIndex", "", "limit", "getNextMatch", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "currentIndex", "Lkotlin/Pair;", "Lkotlin/ExtensionFunctionType;", "(Ljava/lang/CharSequence;IILkotlin/jvm/functions/Function2;)V", "iterator", "", "kotlin-stdlib"})
final class DelimitedRangesSequence
implements Sequence<IntRange> {
    @NotNull
    private final CharSequence input;
    private final int startIndex;
    private final int limit;
    @NotNull
    private final Function2<CharSequence, Integer, Pair<Integer, Integer>> getNextMatch;

    public DelimitedRangesSequence(@NotNull CharSequence input, int startIndex, int limit, @NotNull Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>> getNextMatch) {
        Intrinsics.checkNotNullParameter(input, "input");
        Intrinsics.checkNotNullParameter(getNextMatch, "getNextMatch");
        this.input = input;
        this.startIndex = startIndex;
        this.limit = limit;
        this.getNextMatch = getNextMatch;
    }

    @Override
    @NotNull
    public Iterator<IntRange> iterator() {
        return new Iterator<IntRange>(this){
            private int nextState;
            private int currentStartIndex;
            private int nextSearchIndex;
            @Nullable
            private IntRange nextItem;
            private int counter;
            final /* synthetic */ DelimitedRangesSequence this$0;
            {
                this.this$0 = $receiver;
                this.nextState = -1;
                this.nextSearchIndex = this.currentStartIndex = RangesKt.coerceIn(DelimitedRangesSequence.access$getStartIndex$p(this.this$0), 0, DelimitedRangesSequence.access$getInput$p(this.this$0).length());
            }

            public final int getNextState() {
                return this.nextState;
            }

            public final void setNextState(int n) {
                this.nextState = n;
            }

            public final int getCurrentStartIndex() {
                return this.currentStartIndex;
            }

            public final void setCurrentStartIndex(int n) {
                this.currentStartIndex = n;
            }

            public final int getNextSearchIndex() {
                return this.nextSearchIndex;
            }

            public final void setNextSearchIndex(int n) {
                this.nextSearchIndex = n;
            }

            @Nullable
            public final IntRange getNextItem() {
                return this.nextItem;
            }

            public final void setNextItem(@Nullable IntRange intRange) {
                this.nextItem = intRange;
            }

            public final int getCounter() {
                return this.counter;
            }

            public final void setCounter(int n) {
                this.counter = n;
            }

            /*
             * Unable to fully structure code
             */
            private final void calcNext() {
                block5: {
                    block6: {
                        block4: {
                            if (this.nextSearchIndex >= 0) break block4;
                            this.nextState = 0;
                            this.nextItem = null;
                            break block5;
                        }
                        if (DelimitedRangesSequence.access$getLimit$p(this.this$0) <= 0) break block6;
                        var1_1 = this;
                        ++var1_1.counter;
                        if (var1_1.counter >= DelimitedRangesSequence.access$getLimit$p(this.this$0)) ** GOTO lbl-1000
                    }
                    if (this.nextSearchIndex > DelimitedRangesSequence.access$getInput$p(this.this$0).length()) lbl-1000:
                    // 2 sources

                    {
                        this.nextItem = new IntRange(this.currentStartIndex, StringsKt.getLastIndex(DelimitedRangesSequence.access$getInput$p(this.this$0)));
                        this.nextSearchIndex = -1;
                    } else {
                        match = (Pair)DelimitedRangesSequence.access$getGetNextMatch$p(this.this$0).invoke(DelimitedRangesSequence.access$getInput$p(this.this$0), this.nextSearchIndex);
                        if (match == null) {
                            this.nextItem = new IntRange(this.currentStartIndex, StringsKt.getLastIndex(DelimitedRangesSequence.access$getInput$p(this.this$0)));
                            this.nextSearchIndex = -1;
                        } else {
                            var2_2 = match;
                            index = ((Number)var2_2.component1()).intValue();
                            length = ((Number)var2_2.component2()).intValue();
                            this.nextItem = RangesKt.until(this.currentStartIndex, index);
                            this.currentStartIndex = index + length;
                            this.nextSearchIndex = this.currentStartIndex + (length == 0 ? 1 : 0);
                        }
                    }
                    this.nextState = 1;
                }
            }

            @NotNull
            public IntRange next() {
                if (this.nextState == -1) {
                    this.calcNext();
                }
                if (this.nextState == 0) {
                    throw new NoSuchElementException();
                }
                IntRange intRange = this.nextItem;
                if (intRange == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.ranges.IntRange");
                }
                IntRange result = intRange;
                this.nextItem = null;
                this.nextState = -1;
                return result;
            }

            public boolean hasNext() {
                if (this.nextState == -1) {
                    this.calcNext();
                }
                return this.nextState == 1;
            }

            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }
        };
    }

    public static final /* synthetic */ int access$getStartIndex$p(DelimitedRangesSequence $this) {
        return $this.startIndex;
    }

    public static final /* synthetic */ CharSequence access$getInput$p(DelimitedRangesSequence $this) {
        return $this.input;
    }

    public static final /* synthetic */ int access$getLimit$p(DelimitedRangesSequence $this) {
        return $this.limit;
    }

    public static final /* synthetic */ Function2 access$getGetNextMatch$p(DelimitedRangesSequence $this) {
        return $this.getNextMatch;
    }
}

