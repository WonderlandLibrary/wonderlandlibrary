/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Deprecated;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.OverloadResolutionByLambdaReturnType;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.WasExperimental;
import kotlin.collections.ArraysKt;
import kotlin.collections.CharIterator;
import kotlin.collections.CollectionsKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.CharsKt;
import kotlin.text.DelimitedRangesSequence;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__StringsJVMKt;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000\u0084\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0019\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b!\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006H\u0000\u001a\u001c\u0010\f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010\u0011\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001f\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0086\u0002\u001a\u001f\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0086\u0002\u001a\u0015\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0016H\u0087\n\u001a\u0018\u0010\u0017\u001a\u00020\u0010*\u0004\u0018\u00010\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002H\u0000\u001a\u0018\u0010\u0018\u001a\u00020\u0010*\u0004\u0018\u00010\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002H\u0000\u001a\u001c\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a:\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001aE\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u0010H\u0002\u00a2\u0006\u0002\b!\u001a:\u0010\"\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0012\u0010#\u001a\u00020\u0010*\u00020\u00022\u0006\u0010$\u001a\u00020\u0006\u001a7\u0010%\u001a\u0002H&\"\f\b\u0000\u0010'*\u00020\u0002*\u0002H&\"\u0004\b\u0001\u0010&*\u0002H'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H&0)H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*\u001a7\u0010+\u001a\u0002H&\"\f\b\u0000\u0010'*\u00020\u0002*\u0002H&\"\u0004\b\u0001\u0010&*\u0002H'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H&0)H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*\u001a&\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a;\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010 \u001a\u00020\u0010H\u0002\u00a2\u0006\u0002\b.\u001a&\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010/\u001a\u00020\r2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u00100\u001a\u00020\u0006*\u00020\u00022\u0006\u00101\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a,\u00100\u001a\u00020\u0006*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\r\u00103\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a\r\u00104\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a\r\u00105\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a \u00106\u001a\u00020\u0010*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a \u00107\u001a\u00020\u0010*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\r\u00108\u001a\u000209*\u00020\u0002H\u0086\u0002\u001a&\u0010:\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u0010:\u001a\u00020\u0006*\u00020\u00022\u0006\u0010/\u001a\u00020\r2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u0010;\u001a\u00020\u0006*\u00020\u00022\u0006\u00101\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a,\u0010;\u001a\u00020\u0006*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0010\u0010<\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u0002\u001a\u0010\u0010>\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u0002\u001a\u0015\u0010@\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0016H\u0087\f\u001a\u000f\u0010A\u001a\u00020\r*\u0004\u0018\u00010\rH\u0087\b\u001a\u001c\u0010B\u001a\u00020\u0002*\u00020\u00022\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010B\u001a\u00020\r*\u00020\r2\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010E\u001a\u00020\u0002*\u00020\u00022\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010E\u001a\u00020\r*\u00020\r2\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001aG\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010=*\u00020\u00022\u000e\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0002\u00a2\u0006\u0004\bI\u0010J\u001a=\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010=*\u00020\u00022\u0006\u0010G\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\bI\u001a4\u0010K\u001a\u00020\u0010*\u00020\u00022\u0006\u0010L\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010M\u001a\u00020\u00062\u0006\u0010C\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0010H\u0000\u001a\u0012\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010O\u001a\u00020\u0002\u001a\u0012\u0010N\u001a\u00020\r*\u00020\r2\u0006\u0010O\u001a\u00020\u0002\u001a\u001a\u0010P\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u0006\u001a\u0012\u0010P\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u001d\u0010P\u001a\u00020\r*\u00020\r2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010P\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u0001H\u0087\b\u001a\u0012\u0010R\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010R\u001a\u00020\r*\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010S\u001a\u00020\u0002*\u00020\u00022\u0006\u0010T\u001a\u00020\u0002\u001a\u001a\u0010S\u001a\u00020\u0002*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010S\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u0002\u001a\u001a\u0010S\u001a\u00020\r*\u00020\r2\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a.\u0010U\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0014\b\b\u0010V\u001a\u000e\u0012\u0004\u0012\u00020X\u0012\u0004\u0012\u00020\u00020WH\u0087\b\u00f8\u0001\u0000\u001a\u001d\u0010U\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010Y\u001a\u00020\rH\u0087\b\u001a$\u0010Z\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010Z\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010\\\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010\\\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010]\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010]\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010^\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010^\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001d\u0010_\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010Y\u001a\u00020\rH\u0087\b\u001a)\u0010`\u001a\u00020\r*\u00020\r2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140WH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\ba\u001a)\u0010`\u001a\u00020\r*\u00020\r2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00020WH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\bb\u001a\"\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010Y\u001a\u00020\u0002\u001a\u001a\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u00012\u0006\u0010Y\u001a\u00020\u0002\u001a%\u0010c\u001a\u00020\r*\u00020\r2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010Y\u001a\u00020\u0002H\u0087\b\u001a\u001d\u0010c\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u00012\u0006\u0010Y\u001a\u00020\u0002H\u0087\b\u001a=\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0012\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H\"\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\u0002\u0010e\u001a0\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\n\u0010G\u001a\u000202\"\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u001a/\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0006\u0010T\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\bf\u001a%\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0087\b\u001a=\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\u0012\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H\"\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\u0002\u0010h\u001a0\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\n\u0010G\u001a\u000202\"\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u001a%\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0087\b\u001a\u001c\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a$\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0012\u0010j\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u001d\u0010j\u001a\u00020\u0002*\u00020\r2\u0006\u0010k\u001a\u00020\u00062\u0006\u0010l\u001a\u00020\u0006H\u0087\b\u001a\u001f\u0010m\u001a\u00020\r*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010-\u001a\u00020\u0006H\u0087\b\u001a\u0012\u0010m\u001a\u00020\r*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u0012\u0010m\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u0001\u001a\u001c\u0010n\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010n\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010o\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010o\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010p\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010p\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010q\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010q\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\f\u0010r\u001a\u00020\u0010*\u00020\rH\u0007\u001a\u0013\u0010s\u001a\u0004\u0018\u00010\u0010*\u00020\rH\u0007\u00a2\u0006\u0002\u0010t\u001a\n\u0010u\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010u\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010u\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010u\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010u\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010u\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\u001a\n\u0010w\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010w\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010w\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010w\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010w\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010w\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\u001a\n\u0010x\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010x\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010x\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010x\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010x\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010x\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006y"}, d2={"indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/lang/CharSequence;)Lkotlin/ranges/IntRange;", "lastIndex", "", "getLastIndex", "(Ljava/lang/CharSequence;)I", "requireNonNegativeLimit", "", "limit", "commonPrefixWith", "", "other", "ignoreCase", "", "commonSuffixWith", "contains", "char", "", "regex", "Lkotlin/text/Regex;", "contentEqualsIgnoreCaseImpl", "contentEqualsImpl", "endsWith", "suffix", "findAnyOf", "Lkotlin/Pair;", "strings", "", "startIndex", "last", "findAnyOf$StringsKt__StringsKt", "findLastAnyOf", "hasSurrogatePairAt", "index", "ifBlank", "R", "C", "defaultValue", "Lkotlin/Function0;", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "ifEmpty", "indexOf", "endIndex", "indexOf$StringsKt__StringsKt", "string", "indexOfAny", "chars", "", "isEmpty", "isNotBlank", "isNotEmpty", "isNullOrBlank", "isNullOrEmpty", "iterator", "Lkotlin/collections/CharIterator;", "lastIndexOf", "lastIndexOfAny", "lineSequence", "Lkotlin/sequences/Sequence;", "lines", "", "matches", "orEmpty", "padEnd", "length", "padChar", "padStart", "rangesDelimitedBy", "delimiters", "", "rangesDelimitedBy$StringsKt__StringsKt", "(Ljava/lang/CharSequence;[Ljava/lang/String;IZI)Lkotlin/sequences/Sequence;", "regionMatchesImpl", "thisOffset", "otherOffset", "removePrefix", "prefix", "removeRange", "range", "removeSuffix", "removeSurrounding", "delimiter", "replace", "transform", "Lkotlin/Function1;", "Lkotlin/text/MatchResult;", "replacement", "replaceAfter", "missingDelimiterValue", "replaceAfterLast", "replaceBefore", "replaceBeforeLast", "replaceFirst", "replaceFirstChar", "replaceFirstCharWithChar", "replaceFirstCharWithCharSequence", "replaceRange", "split", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Ljava/util/List;", "split$StringsKt__StringsKt", "splitToSequence", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Lkotlin/sequences/Sequence;", "startsWith", "subSequence", "start", "end", "substring", "substringAfter", "substringAfterLast", "substringBefore", "substringBeforeLast", "toBooleanStrict", "toBooleanStrictOrNull", "(Ljava/lang/String;)Ljava/lang/Boolean;", "trim", "predicate", "trimEnd", "trimStart", "kotlin-stdlib"}, xs="kotlin/text/StringsKt")
class StringsKt__StringsKt
extends StringsKt__StringsJVMKt {
    @NotNull
    public static final CharSequence trim(@NotNull CharSequence $this$trim, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        boolean $i$f$trim = false;
        int startIndex = 0;
        int endIndex = $this$trim.length() - 1;
        boolean startFound = false;
        while (startIndex <= endIndex) {
            int index = !startFound ? startIndex : endIndex;
            boolean match = predicate.invoke(Character.valueOf($this$trim.charAt(index)));
            if (!startFound) {
                if (!match) {
                    startFound = true;
                    continue;
                }
                ++startIndex;
                continue;
            }
            if (!match) break;
            --endIndex;
        }
        return $this$trim.subSequence(startIndex, endIndex + 1);
    }

    @NotNull
    public static final String trim(@NotNull String $this$trim, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        boolean $i$f$trim = false;
        CharSequence $this$trim$iv = $this$trim;
        boolean $i$f$trim2 = false;
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim$iv.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            int index$iv = !startFound$iv ? startIndex$iv : endIndex$iv;
            boolean match$iv = predicate.invoke(Character.valueOf($this$trim$iv.charAt(index$iv)));
            if (!startFound$iv) {
                if (!match$iv) {
                    startFound$iv = true;
                    continue;
                }
                ++startIndex$iv;
                continue;
            }
            if (!match$iv) break;
            --endIndex$iv;
        }
        return ((Object)$this$trim$iv.subSequence(startIndex$iv, endIndex$iv + 1)).toString();
    }

    @NotNull
    public static final CharSequence trimStart(@NotNull CharSequence $this$trimStart, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        boolean $i$f$trimStart = false;
        int n = 0;
        int n2 = $this$trimStart.length();
        while (n < n2) {
            int index;
            if (predicate.invoke(Character.valueOf($this$trimStart.charAt(index = n++))).booleanValue()) continue;
            return $this$trimStart.subSequence(index, $this$trimStart.length());
        }
        return "";
    }

    @NotNull
    public static final String trimStart(@NotNull String $this$trimStart, @NotNull Function1<? super Character, Boolean> predicate) {
        CharSequence charSequence;
        block1: {
            Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
            Intrinsics.checkNotNullParameter(predicate, "predicate");
            boolean $i$f$trimStart = false;
            CharSequence $this$trimStart$iv = $this$trimStart;
            boolean $i$f$trimStart2 = false;
            int n = 0;
            int n2 = $this$trimStart$iv.length();
            while (n < n2) {
                int index$iv;
                if (predicate.invoke(Character.valueOf($this$trimStart$iv.charAt(index$iv = n++))).booleanValue()) continue;
                charSequence = $this$trimStart$iv.subSequence(index$iv, $this$trimStart$iv.length());
                break block1;
            }
            charSequence = "";
        }
        return ((Object)charSequence).toString();
    }

    @NotNull
    public static final CharSequence trimEnd(@NotNull CharSequence $this$trimEnd, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        boolean $i$f$trimEnd = false;
        int n = $this$trimEnd.length() + -1;
        if (0 <= n) {
            do {
                int index;
                if (predicate.invoke(Character.valueOf($this$trimEnd.charAt(index = n--))).booleanValue()) continue;
                return $this$trimEnd.subSequence(0, index + 1);
            } while (0 <= n);
        }
        return "";
    }

    @NotNull
    public static final String trimEnd(@NotNull String $this$trimEnd, @NotNull Function1<? super Character, Boolean> predicate) {
        CharSequence charSequence;
        block2: {
            Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
            Intrinsics.checkNotNullParameter(predicate, "predicate");
            boolean $i$f$trimEnd = false;
            CharSequence $this$trimEnd$iv = $this$trimEnd;
            boolean $i$f$trimEnd2 = false;
            int n = $this$trimEnd$iv.length() + -1;
            if (0 <= n) {
                do {
                    int index$iv;
                    if (predicate.invoke(Character.valueOf($this$trimEnd$iv.charAt(index$iv = n--))).booleanValue()) continue;
                    charSequence = $this$trimEnd$iv.subSequence(0, index$iv + 1);
                    break block2;
                } while (0 <= n);
            }
            charSequence = "";
        }
        return ((Object)charSequence).toString();
    }

    @NotNull
    public static final CharSequence trim(@NotNull CharSequence $this$trim, char ... chars) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        CharSequence $this$trim$iv = $this$trim;
        boolean $i$f$trim = false;
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim$iv.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            int index$iv = !startFound$iv ? startIndex$iv : endIndex$iv;
            char it = $this$trim$iv.charAt(index$iv);
            boolean bl = false;
            boolean match$iv = ArraysKt.contains(chars, it);
            if (!startFound$iv) {
                if (!match$iv) {
                    startFound$iv = true;
                    continue;
                }
                ++startIndex$iv;
                continue;
            }
            if (!match$iv) break;
            --endIndex$iv;
        }
        return $this$trim$iv.subSequence(startIndex$iv, endIndex$iv + 1);
    }

    @NotNull
    public static final String trim(@NotNull String $this$trim, char ... chars) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        String $this$trim$iv = $this$trim;
        boolean $i$f$trim = false;
        CharSequence $this$trim$iv$iv = $this$trim$iv;
        boolean $i$f$trim2 = false;
        int startIndex$iv$iv = 0;
        int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
        boolean startFound$iv$iv = false;
        while (startIndex$iv$iv <= endIndex$iv$iv) {
            int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
            char it = $this$trim$iv$iv.charAt(index$iv$iv);
            boolean bl = false;
            boolean match$iv$iv = ArraysKt.contains(chars, it);
            if (!startFound$iv$iv) {
                if (!match$iv$iv) {
                    startFound$iv$iv = true;
                    continue;
                }
                ++startIndex$iv$iv;
                continue;
            }
            if (!match$iv$iv) break;
            --endIndex$iv$iv;
        }
        return ((Object)$this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1)).toString();
    }

    @NotNull
    public static final CharSequence trimStart(@NotNull CharSequence $this$trimStart, char ... chars) {
        CharSequence charSequence;
        block1: {
            Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
            Intrinsics.checkNotNullParameter(chars, "chars");
            CharSequence $this$trimStart$iv = $this$trimStart;
            boolean $i$f$trimStart = false;
            int n = 0;
            int n2 = $this$trimStart$iv.length();
            while (n < n2) {
                int index$iv = n++;
                char it = $this$trimStart$iv.charAt(index$iv);
                boolean bl = false;
                if (ArraysKt.contains(chars, it)) continue;
                charSequence = $this$trimStart$iv.subSequence(index$iv, $this$trimStart$iv.length());
                break block1;
            }
            charSequence = "";
        }
        return charSequence;
    }

    @NotNull
    public static final String trimStart(@NotNull String $this$trimStart, char ... chars) {
        CharSequence charSequence;
        block1: {
            Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
            Intrinsics.checkNotNullParameter(chars, "chars");
            String $this$trimStart$iv = $this$trimStart;
            boolean $i$f$trimStart = false;
            CharSequence $this$trimStart$iv$iv = $this$trimStart$iv;
            boolean $i$f$trimStart2 = false;
            int n = 0;
            int n2 = $this$trimStart$iv$iv.length();
            while (n < n2) {
                int index$iv$iv = n++;
                char it = $this$trimStart$iv$iv.charAt(index$iv$iv);
                boolean bl = false;
                if (ArraysKt.contains(chars, it)) continue;
                charSequence = $this$trimStart$iv$iv.subSequence(index$iv$iv, $this$trimStart$iv$iv.length());
                break block1;
            }
            charSequence = "";
        }
        return ((Object)charSequence).toString();
    }

    @NotNull
    public static final CharSequence trimEnd(@NotNull CharSequence $this$trimEnd, char ... chars) {
        CharSequence charSequence;
        block2: {
            Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
            Intrinsics.checkNotNullParameter(chars, "chars");
            CharSequence $this$trimEnd$iv = $this$trimEnd;
            boolean $i$f$trimEnd = false;
            int n = $this$trimEnd$iv.length() + -1;
            if (0 <= n) {
                do {
                    int index$iv = n--;
                    char it = $this$trimEnd$iv.charAt(index$iv);
                    boolean bl = false;
                    if (ArraysKt.contains(chars, it)) continue;
                    charSequence = $this$trimEnd$iv.subSequence(0, index$iv + 1);
                    break block2;
                } while (0 <= n);
            }
            charSequence = "";
        }
        return charSequence;
    }

    @NotNull
    public static final String trimEnd(@NotNull String $this$trimEnd, char ... chars) {
        CharSequence charSequence;
        block2: {
            Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
            Intrinsics.checkNotNullParameter(chars, "chars");
            String $this$trimEnd$iv = $this$trimEnd;
            boolean $i$f$trimEnd = false;
            CharSequence $this$trimEnd$iv$iv = $this$trimEnd$iv;
            boolean $i$f$trimEnd2 = false;
            int n = $this$trimEnd$iv$iv.length() + -1;
            if (0 <= n) {
                do {
                    int index$iv$iv = n--;
                    char it = $this$trimEnd$iv$iv.charAt(index$iv$iv);
                    boolean bl = false;
                    if (ArraysKt.contains(chars, it)) continue;
                    charSequence = $this$trimEnd$iv$iv.subSequence(0, index$iv$iv + 1);
                    break block2;
                } while (0 <= n);
            }
            charSequence = "";
        }
        return ((Object)charSequence).toString();
    }

    @NotNull
    public static final CharSequence trim(@NotNull CharSequence $this$trim) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        CharSequence $this$trim$iv = $this$trim;
        boolean $i$f$trim = false;
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim$iv.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            int index$iv = !startFound$iv ? startIndex$iv : endIndex$iv;
            char p0 = $this$trim$iv.charAt(index$iv);
            boolean bl = false;
            boolean match$iv = CharsKt.isWhitespace(p0);
            if (!startFound$iv) {
                if (!match$iv) {
                    startFound$iv = true;
                    continue;
                }
                ++startIndex$iv;
                continue;
            }
            if (!match$iv) break;
            --endIndex$iv;
        }
        return $this$trim$iv.subSequence(startIndex$iv, endIndex$iv + 1);
    }

    @InlineOnly
    private static final String trim(String $this$trim) {
        Intrinsics.checkNotNullParameter($this$trim, "<this>");
        return ((Object)StringsKt.trim((CharSequence)$this$trim)).toString();
    }

    @NotNull
    public static final CharSequence trimStart(@NotNull CharSequence $this$trimStart) {
        CharSequence charSequence;
        block1: {
            Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
            CharSequence $this$trimStart$iv = $this$trimStart;
            boolean $i$f$trimStart = false;
            int n = 0;
            int n2 = $this$trimStart$iv.length();
            while (n < n2) {
                int index$iv = n++;
                char p0 = $this$trimStart$iv.charAt(index$iv);
                boolean bl = false;
                if (CharsKt.isWhitespace(p0)) continue;
                charSequence = $this$trimStart$iv.subSequence(index$iv, $this$trimStart$iv.length());
                break block1;
            }
            charSequence = "";
        }
        return charSequence;
    }

    @InlineOnly
    private static final String trimStart(String $this$trimStart) {
        Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        return ((Object)StringsKt.trimStart((CharSequence)$this$trimStart)).toString();
    }

    @NotNull
    public static final CharSequence trimEnd(@NotNull CharSequence $this$trimEnd) {
        CharSequence charSequence;
        block2: {
            Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
            CharSequence $this$trimEnd$iv = $this$trimEnd;
            boolean $i$f$trimEnd = false;
            int n = $this$trimEnd$iv.length() + -1;
            if (0 <= n) {
                do {
                    int index$iv = n--;
                    char p0 = $this$trimEnd$iv.charAt(index$iv);
                    boolean bl = false;
                    if (CharsKt.isWhitespace(p0)) continue;
                    charSequence = $this$trimEnd$iv.subSequence(0, index$iv + 1);
                    break block2;
                } while (0 <= n);
            }
            charSequence = "";
        }
        return charSequence;
    }

    @InlineOnly
    private static final String trimEnd(String $this$trimEnd) {
        Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        return ((Object)StringsKt.trimEnd((CharSequence)$this$trimEnd)).toString();
    }

    @NotNull
    public static final CharSequence padStart(@NotNull CharSequence $this$padStart, int length, char padChar) {
        Intrinsics.checkNotNullParameter($this$padStart, "<this>");
        if (length < 0) {
            throw new IllegalArgumentException("Desired length " + length + " is less than zero.");
        }
        if (length <= $this$padStart.length()) {
            return $this$padStart.subSequence(0, $this$padStart.length());
        }
        StringBuilder sb = new StringBuilder(length);
        int n = 1;
        int n2 = length - $this$padStart.length();
        if (n <= n2) {
            int i;
            do {
                i = n++;
                sb.append(padChar);
            } while (i != n2);
        }
        sb.append($this$padStart);
        return sb;
    }

    public static /* synthetic */ CharSequence padStart$default(CharSequence charSequence, int n, char c, int n2, Object object) {
        if ((n2 & 2) != 0) {
            c = (char)32;
        }
        return StringsKt.padStart(charSequence, n, c);
    }

    @NotNull
    public static final String padStart(@NotNull String $this$padStart, int length, char padChar) {
        Intrinsics.checkNotNullParameter($this$padStart, "<this>");
        return ((Object)StringsKt.padStart((CharSequence)$this$padStart, length, padChar)).toString();
    }

    public static /* synthetic */ String padStart$default(String string, int n, char c, int n2, Object object) {
        if ((n2 & 2) != 0) {
            c = (char)32;
        }
        return StringsKt.padStart(string, n, c);
    }

    @NotNull
    public static final CharSequence padEnd(@NotNull CharSequence $this$padEnd, int length, char padChar) {
        Intrinsics.checkNotNullParameter($this$padEnd, "<this>");
        if (length < 0) {
            throw new IllegalArgumentException("Desired length " + length + " is less than zero.");
        }
        if (length <= $this$padEnd.length()) {
            return $this$padEnd.subSequence(0, $this$padEnd.length());
        }
        StringBuilder sb = new StringBuilder(length);
        sb.append($this$padEnd);
        int n = 1;
        int n2 = length - $this$padEnd.length();
        if (n <= n2) {
            int i;
            do {
                i = n++;
                sb.append(padChar);
            } while (i != n2);
        }
        return sb;
    }

    public static /* synthetic */ CharSequence padEnd$default(CharSequence charSequence, int n, char c, int n2, Object object) {
        if ((n2 & 2) != 0) {
            c = (char)32;
        }
        return StringsKt.padEnd(charSequence, n, c);
    }

    @NotNull
    public static final String padEnd(@NotNull String $this$padEnd, int length, char padChar) {
        Intrinsics.checkNotNullParameter($this$padEnd, "<this>");
        return ((Object)StringsKt.padEnd((CharSequence)$this$padEnd, length, padChar)).toString();
    }

    public static /* synthetic */ String padEnd$default(String string, int n, char c, int n2, Object object) {
        if ((n2 & 2) != 0) {
            c = (char)32;
        }
        return StringsKt.padEnd(string, n, c);
    }

    @InlineOnly
    private static final boolean isNullOrEmpty(CharSequence $this$isNullOrEmpty) {
        return $this$isNullOrEmpty == null || $this$isNullOrEmpty.length() == 0;
    }

    @InlineOnly
    private static final boolean isEmpty(CharSequence $this$isEmpty) {
        Intrinsics.checkNotNullParameter($this$isEmpty, "<this>");
        return $this$isEmpty.length() == 0;
    }

    @InlineOnly
    private static final boolean isNotEmpty(CharSequence $this$isNotEmpty) {
        Intrinsics.checkNotNullParameter($this$isNotEmpty, "<this>");
        return $this$isNotEmpty.length() > 0;
    }

    @InlineOnly
    private static final boolean isNotBlank(CharSequence $this$isNotBlank) {
        Intrinsics.checkNotNullParameter($this$isNotBlank, "<this>");
        return !StringsKt.isBlank($this$isNotBlank);
    }

    @InlineOnly
    private static final boolean isNullOrBlank(CharSequence $this$isNullOrBlank) {
        return $this$isNullOrBlank == null || StringsKt.isBlank($this$isNullOrBlank);
    }

    @NotNull
    public static final CharIterator iterator(@NotNull CharSequence $this$iterator) {
        Intrinsics.checkNotNullParameter($this$iterator, "<this>");
        return new CharIterator($this$iterator){
            private int index;
            final /* synthetic */ CharSequence $this_iterator;
            {
                this.$this_iterator = $receiver;
            }

            public char nextChar() {
                iterator.1 var1_1 = this;
                int n = var1_1.index;
                var1_1.index = n + 1;
                return this.$this_iterator.charAt(n);
            }

            public boolean hasNext() {
                return this.index < this.$this_iterator.length();
            }
        };
    }

    @InlineOnly
    private static final String orEmpty(String $this$orEmpty) {
        String string = $this$orEmpty;
        return string == null ? "" : string;
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <C extends CharSequence & R, R> R ifEmpty(C $this$ifEmpty, Function0<? extends R> defaultValue) {
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return (R)($this$ifEmpty.length() == 0 ? defaultValue.invoke() : $this$ifEmpty);
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <C extends CharSequence & R, R> R ifBlank(C $this$ifBlank, Function0<? extends R> defaultValue) {
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return (R)(StringsKt.isBlank($this$ifBlank) ? defaultValue.invoke() : $this$ifBlank);
    }

    @NotNull
    public static final IntRange getIndices(@NotNull CharSequence $this$indices) {
        Intrinsics.checkNotNullParameter($this$indices, "<this>");
        return new IntRange(0, $this$indices.length() - 1);
    }

    public static final int getLastIndex(@NotNull CharSequence $this$lastIndex) {
        Intrinsics.checkNotNullParameter($this$lastIndex, "<this>");
        return $this$lastIndex.length() - 1;
    }

    public static final boolean hasSurrogatePairAt(@NotNull CharSequence $this$hasSurrogatePairAt, int index) {
        Intrinsics.checkNotNullParameter($this$hasSurrogatePairAt, "<this>");
        return (0 <= index ? index <= $this$hasSurrogatePairAt.length() - 2 : false) && Character.isHighSurrogate($this$hasSurrogatePairAt.charAt(index)) && Character.isLowSurrogate($this$hasSurrogatePairAt.charAt(index + 1));
    }

    @NotNull
    public static final String substring(@NotNull String $this$substring, @NotNull IntRange range) {
        Intrinsics.checkNotNullParameter($this$substring, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        String string = $this$substring;
        int n = range.getStart();
        int n2 = range.getEndInclusive() + 1;
        String string2 = string.substring(n, n2);
        Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String\u2026ing(startIndex, endIndex)");
        return string2;
    }

    @NotNull
    public static final CharSequence subSequence(@NotNull CharSequence $this$subSequence, @NotNull IntRange range) {
        Intrinsics.checkNotNullParameter($this$subSequence, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        return $this$subSequence.subSequence(range.getStart(), range.getEndInclusive() + 1);
    }

    @Deprecated(message="Use parameters named startIndex and endIndex.", replaceWith=@ReplaceWith(expression="subSequence(startIndex = start, endIndex = end)", imports={}))
    @InlineOnly
    private static final CharSequence subSequence(String $this$subSequence, int start, int end) {
        Intrinsics.checkNotNullParameter($this$subSequence, "<this>");
        return $this$subSequence.subSequence(start, end);
    }

    @InlineOnly
    private static final String substring(CharSequence $this$substring, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$substring, "<this>");
        return ((Object)$this$substring.subSequence(startIndex, endIndex)).toString();
    }

    static /* synthetic */ String substring$default(CharSequence $this$substring_u24default, int startIndex, int endIndex, int n, Object object) {
        if ((n & 2) != 0) {
            endIndex = $this$substring_u24default.length();
        }
        Intrinsics.checkNotNullParameter($this$substring_u24default, "<this>");
        return ((Object)$this$substring_u24default.subSequence(startIndex, endIndex)).toString();
    }

    @NotNull
    public static final String substring(@NotNull CharSequence $this$substring, @NotNull IntRange range) {
        Intrinsics.checkNotNullParameter($this$substring, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        return ((Object)$this$substring.subSequence(range.getStart(), range.getEndInclusive() + 1)).toString();
    }

    @NotNull
    public static final String substringBefore(@NotNull String $this$substringBefore, char delimiter, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$substringBefore, "<this>");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence)$this$substringBefore, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$substringBefore;
            int n = 0;
            String string3 = string2.substring(n, index);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            string = string3;
        }
        return string;
    }

    public static /* synthetic */ String substringBefore$default(String string, char c, String string2, int n, Object object) {
        if ((n & 2) != 0) {
            string2 = string;
        }
        return StringsKt.substringBefore(string, c, string2);
    }

    @NotNull
    public static final String substringBefore(@NotNull String $this$substringBefore, @NotNull String delimiter, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$substringBefore, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence)$this$substringBefore, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$substringBefore;
            int n = 0;
            String string3 = string2.substring(n, index);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            string = string3;
        }
        return string;
    }

    public static /* synthetic */ String substringBefore$default(String string, String string2, String string3, int n, Object object) {
        if ((n & 2) != 0) {
            string3 = string;
        }
        return StringsKt.substringBefore(string, string2, string3);
    }

    @NotNull
    public static final String substringAfter(@NotNull String $this$substringAfter, char delimiter, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$substringAfter, "<this>");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence)$this$substringAfter, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$substringAfter;
            int n = index + 1;
            int n2 = $this$substringAfter.length();
            String string3 = string2.substring(n, n2);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            string = string3;
        }
        return string;
    }

    public static /* synthetic */ String substringAfter$default(String string, char c, String string2, int n, Object object) {
        if ((n & 2) != 0) {
            string2 = string;
        }
        return StringsKt.substringAfter(string, c, string2);
    }

    @NotNull
    public static final String substringAfter(@NotNull String $this$substringAfter, @NotNull String delimiter, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$substringAfter, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence)$this$substringAfter, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$substringAfter;
            int n = index + delimiter.length();
            int n2 = $this$substringAfter.length();
            String string3 = string2.substring(n, n2);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            string = string3;
        }
        return string;
    }

    public static /* synthetic */ String substringAfter$default(String string, String string2, String string3, int n, Object object) {
        if ((n & 2) != 0) {
            string3 = string;
        }
        return StringsKt.substringAfter(string, string2, string3);
    }

    @NotNull
    public static final String substringBeforeLast(@NotNull String $this$substringBeforeLast, char delimiter, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$substringBeforeLast, "<this>");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence)$this$substringBeforeLast, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$substringBeforeLast;
            int n = 0;
            String string3 = string2.substring(n, index);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            string = string3;
        }
        return string;
    }

    public static /* synthetic */ String substringBeforeLast$default(String string, char c, String string2, int n, Object object) {
        if ((n & 2) != 0) {
            string2 = string;
        }
        return StringsKt.substringBeforeLast(string, c, string2);
    }

    @NotNull
    public static final String substringBeforeLast(@NotNull String $this$substringBeforeLast, @NotNull String delimiter, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$substringBeforeLast, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence)$this$substringBeforeLast, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$substringBeforeLast;
            int n = 0;
            String string3 = string2.substring(n, index);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            string = string3;
        }
        return string;
    }

    public static /* synthetic */ String substringBeforeLast$default(String string, String string2, String string3, int n, Object object) {
        if ((n & 2) != 0) {
            string3 = string;
        }
        return StringsKt.substringBeforeLast(string, string2, string3);
    }

    @NotNull
    public static final String substringAfterLast(@NotNull String $this$substringAfterLast, char delimiter, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$substringAfterLast, "<this>");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence)$this$substringAfterLast, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$substringAfterLast;
            int n = index + 1;
            int n2 = $this$substringAfterLast.length();
            String string3 = string2.substring(n, n2);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            string = string3;
        }
        return string;
    }

    public static /* synthetic */ String substringAfterLast$default(String string, char c, String string2, int n, Object object) {
        if ((n & 2) != 0) {
            string2 = string;
        }
        return StringsKt.substringAfterLast(string, c, string2);
    }

    @NotNull
    public static final String substringAfterLast(@NotNull String $this$substringAfterLast, @NotNull String delimiter, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$substringAfterLast, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence)$this$substringAfterLast, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$substringAfterLast;
            int n = index + delimiter.length();
            int n2 = $this$substringAfterLast.length();
            String string3 = string2.substring(n, n2);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            string = string3;
        }
        return string;
    }

    public static /* synthetic */ String substringAfterLast$default(String string, String string2, String string3, int n, Object object) {
        if ((n & 2) != 0) {
            string3 = string;
        }
        return StringsKt.substringAfterLast(string, string2, string3);
    }

    @NotNull
    public static final CharSequence replaceRange(@NotNull CharSequence $this$replaceRange, int startIndex, int endIndex, @NotNull CharSequence replacement) {
        StringBuilder sb;
        Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        if (endIndex < startIndex) {
            throw new IndexOutOfBoundsException("End index (" + endIndex + ") is less than start index (" + startIndex + ").");
        }
        StringBuilder stringBuilder = sb = new StringBuilder();
        int n = 0;
        StringBuilder stringBuilder2 = stringBuilder.append($this$replaceRange, n, startIndex);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "this.append(value, startIndex, endIndex)");
        sb.append(replacement);
        stringBuilder = sb;
        n = $this$replaceRange.length();
        stringBuilder2 = stringBuilder.append($this$replaceRange, endIndex, n);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "this.append(value, startIndex, endIndex)");
        return sb;
    }

    @InlineOnly
    private static final String replaceRange(String $this$replaceRange, int startIndex, int endIndex, CharSequence replacement) {
        Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        return ((Object)StringsKt.replaceRange((CharSequence)$this$replaceRange, startIndex, endIndex, replacement)).toString();
    }

    @NotNull
    public static final CharSequence replaceRange(@NotNull CharSequence $this$replaceRange, @NotNull IntRange range, @NotNull CharSequence replacement) {
        Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        return StringsKt.replaceRange($this$replaceRange, (int)range.getStart(), range.getEndInclusive() + 1, replacement);
    }

    @InlineOnly
    private static final String replaceRange(String $this$replaceRange, IntRange range, CharSequence replacement) {
        Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        return ((Object)StringsKt.replaceRange((CharSequence)$this$replaceRange, range, replacement)).toString();
    }

    @NotNull
    public static final CharSequence removeRange(@NotNull CharSequence $this$removeRange, int startIndex, int endIndex) {
        StringBuilder sb;
        Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        if (endIndex < startIndex) {
            throw new IndexOutOfBoundsException("End index (" + endIndex + ") is less than start index (" + startIndex + ").");
        }
        if (endIndex == startIndex) {
            return $this$removeRange.subSequence(0, $this$removeRange.length());
        }
        StringBuilder stringBuilder = sb = new StringBuilder($this$removeRange.length() - (endIndex - startIndex));
        int n = 0;
        StringBuilder stringBuilder2 = stringBuilder.append($this$removeRange, n, startIndex);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "this.append(value, startIndex, endIndex)");
        stringBuilder = sb;
        n = $this$removeRange.length();
        stringBuilder2 = stringBuilder.append($this$removeRange, endIndex, n);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "this.append(value, startIndex, endIndex)");
        return sb;
    }

    @InlineOnly
    private static final String removeRange(String $this$removeRange, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        return ((Object)StringsKt.removeRange((CharSequence)$this$removeRange, startIndex, endIndex)).toString();
    }

    @NotNull
    public static final CharSequence removeRange(@NotNull CharSequence $this$removeRange, @NotNull IntRange range) {
        Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        return StringsKt.removeRange($this$removeRange, (int)range.getStart(), range.getEndInclusive() + 1);
    }

    @InlineOnly
    private static final String removeRange(String $this$removeRange, IntRange range) {
        Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        return ((Object)StringsKt.removeRange((CharSequence)$this$removeRange, range)).toString();
    }

    @NotNull
    public static final CharSequence removePrefix(@NotNull CharSequence $this$removePrefix, @NotNull CharSequence prefix) {
        Intrinsics.checkNotNullParameter($this$removePrefix, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (StringsKt.startsWith$default($this$removePrefix, prefix, false, 2, null)) {
            return $this$removePrefix.subSequence(prefix.length(), $this$removePrefix.length());
        }
        return $this$removePrefix.subSequence(0, $this$removePrefix.length());
    }

    @NotNull
    public static final String removePrefix(@NotNull String $this$removePrefix, @NotNull CharSequence prefix) {
        Intrinsics.checkNotNullParameter($this$removePrefix, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (StringsKt.startsWith$default((CharSequence)$this$removePrefix, prefix, false, 2, null)) {
            String string = $this$removePrefix;
            int n = prefix.length();
            String string2 = string.substring(n);
            Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String).substring(startIndex)");
            return string2;
        }
        return $this$removePrefix;
    }

    @NotNull
    public static final CharSequence removeSuffix(@NotNull CharSequence $this$removeSuffix, @NotNull CharSequence suffix) {
        Intrinsics.checkNotNullParameter($this$removeSuffix, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        if (StringsKt.endsWith$default($this$removeSuffix, suffix, false, 2, null)) {
            return $this$removeSuffix.subSequence(0, $this$removeSuffix.length() - suffix.length());
        }
        return $this$removeSuffix.subSequence(0, $this$removeSuffix.length());
    }

    @NotNull
    public static final String removeSuffix(@NotNull String $this$removeSuffix, @NotNull CharSequence suffix) {
        Intrinsics.checkNotNullParameter($this$removeSuffix, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        if (StringsKt.endsWith$default((CharSequence)$this$removeSuffix, suffix, false, 2, null)) {
            String string = $this$removeSuffix;
            int n = 0;
            int n2 = $this$removeSuffix.length() - suffix.length();
            String string2 = string.substring(n, n2);
            Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            return string2;
        }
        return $this$removeSuffix;
    }

    @NotNull
    public static final CharSequence removeSurrounding(@NotNull CharSequence $this$removeSurrounding, @NotNull CharSequence prefix, @NotNull CharSequence suffix) {
        Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        if ($this$removeSurrounding.length() >= prefix.length() + suffix.length() && StringsKt.startsWith$default($this$removeSurrounding, prefix, false, 2, null) && StringsKt.endsWith$default($this$removeSurrounding, suffix, false, 2, null)) {
            return $this$removeSurrounding.subSequence(prefix.length(), $this$removeSurrounding.length() - suffix.length());
        }
        return $this$removeSurrounding.subSequence(0, $this$removeSurrounding.length());
    }

    @NotNull
    public static final String removeSurrounding(@NotNull String $this$removeSurrounding, @NotNull CharSequence prefix, @NotNull CharSequence suffix) {
        Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        if ($this$removeSurrounding.length() >= prefix.length() + suffix.length() && StringsKt.startsWith$default((CharSequence)$this$removeSurrounding, prefix, false, 2, null) && StringsKt.endsWith$default((CharSequence)$this$removeSurrounding, suffix, false, 2, null)) {
            String string = $this$removeSurrounding;
            int n = prefix.length();
            int n2 = $this$removeSurrounding.length() - suffix.length();
            String string2 = string.substring(n, n2);
            Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            return string2;
        }
        return $this$removeSurrounding;
    }

    @NotNull
    public static final CharSequence removeSurrounding(@NotNull CharSequence $this$removeSurrounding, @NotNull CharSequence delimiter) {
        Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        return StringsKt.removeSurrounding($this$removeSurrounding, delimiter, delimiter);
    }

    @NotNull
    public static final String removeSurrounding(@NotNull String $this$removeSurrounding, @NotNull CharSequence delimiter) {
        Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        return StringsKt.removeSurrounding($this$removeSurrounding, delimiter, delimiter);
    }

    @NotNull
    public static final String replaceBefore(@NotNull String $this$replaceBefore, char delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceBefore, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence)$this$replaceBefore, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$replaceBefore;
            int n = 0;
            string = ((Object)StringsKt.replaceRange((CharSequence)string2, n, index, (CharSequence)replacement)).toString();
        }
        return string;
    }

    public static /* synthetic */ String replaceBefore$default(String string, char c, String string2, String string3, int n, Object object) {
        if ((n & 4) != 0) {
            string3 = string;
        }
        return StringsKt.replaceBefore(string, c, string2, string3);
    }

    @NotNull
    public static final String replaceBefore(@NotNull String $this$replaceBefore, @NotNull String delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceBefore, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence)$this$replaceBefore, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$replaceBefore;
            int n = 0;
            string = ((Object)StringsKt.replaceRange((CharSequence)string2, n, index, (CharSequence)replacement)).toString();
        }
        return string;
    }

    public static /* synthetic */ String replaceBefore$default(String string, String string2, String string3, String string4, int n, Object object) {
        if ((n & 4) != 0) {
            string4 = string;
        }
        return StringsKt.replaceBefore(string, string2, string3, string4);
    }

    @NotNull
    public static final String replaceAfter(@NotNull String $this$replaceAfter, char delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceAfter, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence)$this$replaceAfter, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$replaceAfter;
            int n = index + 1;
            int n2 = $this$replaceAfter.length();
            string = ((Object)StringsKt.replaceRange((CharSequence)string2, n, n2, (CharSequence)replacement)).toString();
        }
        return string;
    }

    public static /* synthetic */ String replaceAfter$default(String string, char c, String string2, String string3, int n, Object object) {
        if ((n & 4) != 0) {
            string3 = string;
        }
        return StringsKt.replaceAfter(string, c, string2, string3);
    }

    @NotNull
    public static final String replaceAfter(@NotNull String $this$replaceAfter, @NotNull String delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceAfter, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence)$this$replaceAfter, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$replaceAfter;
            int n = index + delimiter.length();
            int n2 = $this$replaceAfter.length();
            string = ((Object)StringsKt.replaceRange((CharSequence)string2, n, n2, (CharSequence)replacement)).toString();
        }
        return string;
    }

    public static /* synthetic */ String replaceAfter$default(String string, String string2, String string3, String string4, int n, Object object) {
        if ((n & 4) != 0) {
            string4 = string;
        }
        return StringsKt.replaceAfter(string, string2, string3, string4);
    }

    @NotNull
    public static final String replaceAfterLast(@NotNull String $this$replaceAfterLast, @NotNull String delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceAfterLast, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence)$this$replaceAfterLast, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$replaceAfterLast;
            int n = index + delimiter.length();
            int n2 = $this$replaceAfterLast.length();
            string = ((Object)StringsKt.replaceRange((CharSequence)string2, n, n2, (CharSequence)replacement)).toString();
        }
        return string;
    }

    public static /* synthetic */ String replaceAfterLast$default(String string, String string2, String string3, String string4, int n, Object object) {
        if ((n & 4) != 0) {
            string4 = string;
        }
        return StringsKt.replaceAfterLast(string, string2, string3, string4);
    }

    @NotNull
    public static final String replaceAfterLast(@NotNull String $this$replaceAfterLast, char delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceAfterLast, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence)$this$replaceAfterLast, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$replaceAfterLast;
            int n = index + 1;
            int n2 = $this$replaceAfterLast.length();
            string = ((Object)StringsKt.replaceRange((CharSequence)string2, n, n2, (CharSequence)replacement)).toString();
        }
        return string;
    }

    public static /* synthetic */ String replaceAfterLast$default(String string, char c, String string2, String string3, int n, Object object) {
        if ((n & 4) != 0) {
            string3 = string;
        }
        return StringsKt.replaceAfterLast(string, c, string2, string3);
    }

    @NotNull
    public static final String replaceBeforeLast(@NotNull String $this$replaceBeforeLast, char delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceBeforeLast, "<this>");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence)$this$replaceBeforeLast, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$replaceBeforeLast;
            int n = 0;
            string = ((Object)StringsKt.replaceRange((CharSequence)string2, n, index, (CharSequence)replacement)).toString();
        }
        return string;
    }

    public static /* synthetic */ String replaceBeforeLast$default(String string, char c, String string2, String string3, int n, Object object) {
        if ((n & 4) != 0) {
            string3 = string;
        }
        return StringsKt.replaceBeforeLast(string, c, string2, string3);
    }

    @NotNull
    public static final String replaceBeforeLast(@NotNull String $this$replaceBeforeLast, @NotNull String delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceBeforeLast, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence)$this$replaceBeforeLast, delimiter, 0, false, 6, null);
        if (index == -1) {
            string = missingDelimiterValue;
        } else {
            String string2 = $this$replaceBeforeLast;
            int n = 0;
            string = ((Object)StringsKt.replaceRange((CharSequence)string2, n, index, (CharSequence)replacement)).toString();
        }
        return string;
    }

    public static /* synthetic */ String replaceBeforeLast$default(String string, String string2, String string3, String string4, int n, Object object) {
        if ((n & 4) != 0) {
            string4 = string;
        }
        return StringsKt.replaceBeforeLast(string, string2, string3, string4);
    }

    @InlineOnly
    private static final String replace(CharSequence $this$replace, Regex regex, String replacement) {
        Intrinsics.checkNotNullParameter($this$replace, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        return regex.replace($this$replace, replacement);
    }

    @InlineOnly
    private static final String replace(CharSequence $this$replace, Regex regex, Function1<? super MatchResult, ? extends CharSequence> transform) {
        Intrinsics.checkNotNullParameter($this$replace, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        Intrinsics.checkNotNullParameter(transform, "transform");
        return regex.replace($this$replace, transform);
    }

    @InlineOnly
    private static final String replaceFirst(CharSequence $this$replaceFirst, Regex regex, String replacement) {
        Intrinsics.checkNotNullParameter($this$replaceFirst, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        return regex.replaceFirst($this$replaceFirst, replacement);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="replaceFirstCharWithChar")
    @InlineOnly
    private static final String replaceFirstCharWithChar(String $this$replaceFirstChar, Function1<? super Character, Character> transform) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceFirstChar, "<this>");
        Intrinsics.checkNotNullParameter(transform, "transform");
        if (((CharSequence)$this$replaceFirstChar).length() > 0) {
            char c = transform.invoke(Character.valueOf($this$replaceFirstChar.charAt(0))).charValue();
            String string2 = $this$replaceFirstChar;
            int n = 1;
            String string3 = string2.substring(n);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String).substring(startIndex)");
            string2 = string3;
            string = c + string2;
        } else {
            string = $this$replaceFirstChar;
        }
        return string;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="replaceFirstCharWithCharSequence")
    @InlineOnly
    private static final String replaceFirstCharWithCharSequence(String $this$replaceFirstChar, Function1<? super Character, ? extends CharSequence> transform) {
        String string;
        Intrinsics.checkNotNullParameter($this$replaceFirstChar, "<this>");
        Intrinsics.checkNotNullParameter(transform, "transform");
        if (((CharSequence)$this$replaceFirstChar).length() > 0) {
            StringBuilder stringBuilder = new StringBuilder().append((Object)transform.invoke(Character.valueOf($this$replaceFirstChar.charAt(0))));
            String string2 = $this$replaceFirstChar;
            int n = 1;
            String string3 = string2.substring(n);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String).substring(startIndex)");
            string = stringBuilder.append(string3).toString();
        } else {
            string = $this$replaceFirstChar;
        }
        return string;
    }

    @InlineOnly
    private static final boolean matches(CharSequence $this$matches, Regex regex) {
        Intrinsics.checkNotNullParameter($this$matches, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.matches($this$matches);
    }

    public static final boolean regionMatchesImpl(@NotNull CharSequence $this$regionMatchesImpl, int thisOffset, @NotNull CharSequence other, int otherOffset, int length, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$regionMatchesImpl, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        if (otherOffset < 0 || thisOffset < 0 || thisOffset > $this$regionMatchesImpl.length() - length || otherOffset > other.length() - length) {
            return false;
        }
        int n = 0;
        while (n < length) {
            int index;
            if (CharsKt.equals($this$regionMatchesImpl.charAt(thisOffset + (index = n++)), other.charAt(otherOffset + index), ignoreCase)) continue;
            return false;
        }
        return true;
    }

    public static final boolean startsWith(@NotNull CharSequence $this$startsWith, char c, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        return $this$startsWith.length() > 0 && CharsKt.equals($this$startsWith.charAt(0), c, ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, char c, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(charSequence, c, bl);
    }

    public static final boolean endsWith(@NotNull CharSequence $this$endsWith, char c, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        return $this$endsWith.length() > 0 && CharsKt.equals($this$endsWith.charAt(StringsKt.getLastIndex($this$endsWith)), c, ignoreCase);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, char c, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.endsWith(charSequence, c, bl);
    }

    public static final boolean startsWith(@NotNull CharSequence $this$startsWith, @NotNull CharSequence prefix, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!ignoreCase && $this$startsWith instanceof String && prefix instanceof String) {
            return StringsKt.startsWith$default((String)$this$startsWith, (String)prefix, false, 2, null);
        }
        return StringsKt.regionMatchesImpl($this$startsWith, 0, prefix, 0, prefix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, bl);
    }

    public static final boolean startsWith(@NotNull CharSequence $this$startsWith, @NotNull CharSequence prefix, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!ignoreCase && $this$startsWith instanceof String && prefix instanceof String) {
            return StringsKt.startsWith$default((String)$this$startsWith, (String)prefix, startIndex, false, 4, null);
        }
        return StringsKt.regionMatchesImpl($this$startsWith, startIndex, prefix, 0, prefix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, int n, boolean bl, int n2, Object object) {
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, n, bl);
    }

    public static final boolean endsWith(@NotNull CharSequence $this$endsWith, @NotNull CharSequence suffix, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        if (!ignoreCase && $this$endsWith instanceof String && suffix instanceof String) {
            return StringsKt.endsWith$default((String)$this$endsWith, (String)suffix, false, 2, null);
        }
        return StringsKt.regionMatchesImpl($this$endsWith, $this$endsWith.length() - suffix.length(), suffix, 0, suffix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.endsWith(charSequence, charSequence2, bl);
    }

    @NotNull
    public static final String commonPrefixWith(@NotNull CharSequence $this$commonPrefixWith, @NotNull CharSequence other, boolean ignoreCase) {
        int n;
        Intrinsics.checkNotNullParameter($this$commonPrefixWith, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        int shortestLength = Math.min($this$commonPrefixWith.length(), other.length());
        int i = 0;
        while (i < shortestLength && CharsKt.equals($this$commonPrefixWith.charAt(i), other.charAt(i), ignoreCase)) {
            n = i;
            i = n + 1;
        }
        if (StringsKt.hasSurrogatePairAt($this$commonPrefixWith, i - 1) || StringsKt.hasSurrogatePairAt(other, i - 1)) {
            n = i;
            i = n + -1;
        }
        return ((Object)$this$commonPrefixWith.subSequence(0, i)).toString();
    }

    public static /* synthetic */ String commonPrefixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.commonPrefixWith(charSequence, charSequence2, bl);
    }

    @NotNull
    public static final String commonSuffixWith(@NotNull CharSequence $this$commonSuffixWith, @NotNull CharSequence other, boolean ignoreCase) {
        int n;
        Intrinsics.checkNotNullParameter($this$commonSuffixWith, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        int thisLength = $this$commonSuffixWith.length();
        int otherLength = other.length();
        int shortestLength = Math.min(thisLength, otherLength);
        int i = 0;
        while (i < shortestLength && CharsKt.equals($this$commonSuffixWith.charAt(thisLength - i - 1), other.charAt(otherLength - i - 1), ignoreCase)) {
            n = i;
            i = n + 1;
        }
        if (StringsKt.hasSurrogatePairAt($this$commonSuffixWith, thisLength - i - 1) || StringsKt.hasSurrogatePairAt(other, otherLength - i - 1)) {
            n = i;
            i = n + -1;
        }
        return ((Object)$this$commonSuffixWith.subSequence(thisLength - i, thisLength)).toString();
    }

    public static /* synthetic */ String commonSuffixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.commonSuffixWith(charSequence, charSequence2, bl);
    }

    public static final int indexOfAny(@NotNull CharSequence $this$indexOfAny, @NotNull char[] chars, int startIndex, boolean ignoreCase) {
        int n;
        Intrinsics.checkNotNullParameter($this$indexOfAny, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        if (!ignoreCase && chars.length == 1 && $this$indexOfAny instanceof String) {
            char c = ArraysKt.single(chars);
            String string = (String)$this$indexOfAny;
            return string.indexOf(c, startIndex);
        }
        int n2 = RangesKt.coerceAtLeast(startIndex, 0);
        if (n2 <= (n = StringsKt.getLastIndex($this$indexOfAny))) {
            int index;
            do {
                boolean bl;
                block4: {
                    index = n2++;
                    char charAtIndex = $this$indexOfAny.charAt(index);
                    char[] $this$any$iv = chars;
                    boolean $i$f$any = false;
                    for (char element$iv : $this$any$iv) {
                        char it = element$iv;
                        boolean bl2 = false;
                        if (!CharsKt.equals(it, charAtIndex, ignoreCase)) continue;
                        bl = true;
                        break block4;
                    }
                    bl = false;
                }
                if (!bl) continue;
                return index;
            } while (index != n);
        }
        return -1;
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, char[] cArray, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOfAny(charSequence, cArray, n, bl);
    }

    public static final int lastIndexOfAny(@NotNull CharSequence $this$lastIndexOfAny, @NotNull char[] chars, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$lastIndexOfAny, "<this>");
        Intrinsics.checkNotNullParameter(chars, "chars");
        if (!ignoreCase && chars.length == 1 && $this$lastIndexOfAny instanceof String) {
            char c = ArraysKt.single(chars);
            String string = (String)$this$lastIndexOfAny;
            return string.lastIndexOf(c, startIndex);
        }
        int n = RangesKt.coerceAtMost(startIndex, StringsKt.getLastIndex($this$lastIndexOfAny));
        if (0 <= n) {
            do {
                boolean bl;
                int index;
                block4: {
                    index = n--;
                    char charAtIndex = $this$lastIndexOfAny.charAt(index);
                    char[] $this$any$iv = chars;
                    boolean $i$f$any = false;
                    for (char element$iv : $this$any$iv) {
                        char it = element$iv;
                        boolean bl2 = false;
                        if (!CharsKt.equals(it, charAtIndex, ignoreCase)) continue;
                        bl = true;
                        break block4;
                    }
                    bl = false;
                }
                if (!bl) continue;
                return index;
            } while (0 <= n);
        }
        return -1;
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, char[] cArray, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = StringsKt.getLastIndex(charSequence);
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, cArray, n, bl);
    }

    private static final int indexOf$StringsKt__StringsKt(CharSequence $this$indexOf, CharSequence other, int startIndex, int endIndex, boolean ignoreCase, boolean last) {
        IntProgression indices;
        IntProgression intProgression = indices = !last ? (IntProgression)new IntRange(RangesKt.coerceAtLeast(startIndex, 0), RangesKt.coerceAtMost(endIndex, $this$indexOf.length())) : RangesKt.downTo(RangesKt.coerceAtMost(startIndex, StringsKt.getLastIndex($this$indexOf)), RangesKt.coerceAtLeast(endIndex, 0));
        if ($this$indexOf instanceof String && other instanceof String) {
            int n = indices.getFirst();
            int n2 = indices.getLast();
            int n3 = indices.getStep();
            if (n3 > 0 && n <= n2 || n3 < 0 && n2 <= n) {
                int index;
                do {
                    index = n;
                    n += n3;
                    if (!StringsKt.regionMatches((String)other, 0, (String)$this$indexOf, index, other.length(), ignoreCase)) continue;
                    return index;
                } while (index != n2);
            }
        } else {
            int n = indices.getFirst();
            int n4 = indices.getLast();
            int n5 = indices.getStep();
            if (n5 > 0 && n <= n4 || n5 < 0 && n4 <= n) {
                int index;
                do {
                    index = n;
                    n += n5;
                    if (!StringsKt.regionMatchesImpl(other, 0, $this$indexOf, index, other.length(), ignoreCase)) continue;
                    return index;
                } while (index != n4);
            }
        }
        return -1;
    }

    static /* synthetic */ int indexOf$StringsKt__StringsKt$default(CharSequence charSequence, CharSequence charSequence2, int n, int n2, boolean bl, boolean bl2, int n3, Object object) {
        if ((n3 & 0x10) != 0) {
            bl2 = false;
        }
        return StringsKt__StringsKt.indexOf$StringsKt__StringsKt(charSequence, charSequence2, n, n2, bl, bl2);
    }

    private static final Pair<Integer, String> findAnyOf$StringsKt__StringsKt(CharSequence $this$findAnyOf, Collection<String> strings, int startIndex, boolean ignoreCase, boolean last) {
        IntProgression indices;
        if (!ignoreCase && strings.size() == 1) {
            String string = (String)CollectionsKt.single((Iterable)strings);
            int index = !last ? StringsKt.indexOf$default($this$findAnyOf, string, startIndex, false, 4, null) : StringsKt.lastIndexOf$default($this$findAnyOf, string, startIndex, false, 4, null);
            return index < 0 ? null : TuplesKt.to(index, string);
        }
        IntProgression intProgression = indices = !last ? (IntProgression)new IntRange(RangesKt.coerceAtLeast(startIndex, 0), $this$findAnyOf.length()) : RangesKt.downTo(RangesKt.coerceAtMost(startIndex, StringsKt.getLastIndex($this$findAnyOf)), 0);
        if ($this$findAnyOf instanceof String) {
            int n = indices.getFirst();
            int n2 = indices.getLast();
            int n3 = indices.getStep();
            if (n3 > 0 && n <= n2 || n3 < 0 && n2 <= n) {
                int index;
                do {
                    Object v1;
                    block9: {
                        index = n;
                        n += n3;
                        Iterable $this$firstOrNull$iv = strings;
                        boolean $i$f$firstOrNull = false;
                        for (Object element$iv : $this$firstOrNull$iv) {
                            String it = (String)element$iv;
                            boolean bl = false;
                            if (!StringsKt.regionMatches(it, 0, (String)$this$findAnyOf, index, it.length(), ignoreCase)) continue;
                            v1 = element$iv;
                            break block9;
                        }
                        v1 = null;
                    }
                    String matchingString = v1;
                    if (matchingString == null) continue;
                    return TuplesKt.to(index, matchingString);
                } while (index != n2);
            }
        } else {
            int n = indices.getFirst();
            int n4 = indices.getLast();
            int n5 = indices.getStep();
            if (n5 > 0 && n <= n4 || n5 < 0 && n4 <= n) {
                int index;
                do {
                    Object v2;
                    block10: {
                        index = n;
                        n += n5;
                        Iterable $this$firstOrNull$iv = strings;
                        boolean $i$f$firstOrNull = false;
                        for (Object element$iv : $this$firstOrNull$iv) {
                            String it = (String)element$iv;
                            boolean bl = false;
                            if (!StringsKt.regionMatchesImpl(it, 0, $this$findAnyOf, index, it.length(), ignoreCase)) continue;
                            v2 = element$iv;
                            break block10;
                        }
                        v2 = null;
                    }
                    String matchingString = v2;
                    if (matchingString == null) continue;
                    return TuplesKt.to(index, matchingString);
                } while (index != n4);
            }
        }
        return null;
    }

    @Nullable
    public static final Pair<Integer, String> findAnyOf(@NotNull CharSequence $this$findAnyOf, @NotNull Collection<String> strings, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$findAnyOf, "<this>");
        Intrinsics.checkNotNullParameter(strings, "strings");
        return StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt($this$findAnyOf, strings, startIndex, ignoreCase, false);
    }

    public static /* synthetic */ Pair findAnyOf$default(CharSequence charSequence, Collection collection, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.findAnyOf(charSequence, collection, n, bl);
    }

    @Nullable
    public static final Pair<Integer, String> findLastAnyOf(@NotNull CharSequence $this$findLastAnyOf, @NotNull Collection<String> strings, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$findLastAnyOf, "<this>");
        Intrinsics.checkNotNullParameter(strings, "strings");
        return StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt($this$findLastAnyOf, strings, startIndex, ignoreCase, true);
    }

    public static /* synthetic */ Pair findLastAnyOf$default(CharSequence charSequence, Collection collection, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = StringsKt.getLastIndex(charSequence);
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.findLastAnyOf(charSequence, collection, n, bl);
    }

    public static final int indexOfAny(@NotNull CharSequence $this$indexOfAny, @NotNull Collection<String> strings, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$indexOfAny, "<this>");
        Intrinsics.checkNotNullParameter(strings, "strings");
        Pair<Integer, String> pair = StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt($this$indexOfAny, strings, startIndex, ignoreCase, false);
        return pair == null ? -1 : ((Number)pair.getFirst()).intValue();
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, Collection collection, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOfAny(charSequence, collection, n, bl);
    }

    public static final int lastIndexOfAny(@NotNull CharSequence $this$lastIndexOfAny, @NotNull Collection<String> strings, int startIndex, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$lastIndexOfAny, "<this>");
        Intrinsics.checkNotNullParameter(strings, "strings");
        Pair<Integer, String> pair = StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt($this$lastIndexOfAny, strings, startIndex, ignoreCase, true);
        return pair == null ? -1 : ((Number)pair.getFirst()).intValue();
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, Collection collection, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = StringsKt.getLastIndex(charSequence);
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, collection, n, bl);
    }

    public static final int indexOf(@NotNull CharSequence $this$indexOf, char c, int startIndex, boolean ignoreCase) {
        int n;
        Intrinsics.checkNotNullParameter($this$indexOf, "<this>");
        if (ignoreCase || !($this$indexOf instanceof String)) {
            char[] cArray = new char[]{c};
            n = StringsKt.indexOfAny($this$indexOf, cArray, startIndex, ignoreCase);
        } else {
            String string = (String)$this$indexOf;
            n = string.indexOf(c, startIndex);
        }
        return n;
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, char c, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOf(charSequence, c, n, bl);
    }

    public static final int indexOf(@NotNull CharSequence $this$indexOf, @NotNull String string, int startIndex, boolean ignoreCase) {
        int n;
        Intrinsics.checkNotNullParameter($this$indexOf, "<this>");
        Intrinsics.checkNotNullParameter(string, "string");
        if (ignoreCase || !($this$indexOf instanceof String)) {
            n = StringsKt__StringsKt.indexOf$StringsKt__StringsKt$default($this$indexOf, string, startIndex, $this$indexOf.length(), ignoreCase, false, 16, null);
        } else {
            String string2 = (String)$this$indexOf;
            n = string2.indexOf(string, startIndex);
        }
        return n;
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, String string, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOf(charSequence, string, n, bl);
    }

    public static final int lastIndexOf(@NotNull CharSequence $this$lastIndexOf, char c, int startIndex, boolean ignoreCase) {
        int n;
        Intrinsics.checkNotNullParameter($this$lastIndexOf, "<this>");
        if (ignoreCase || !($this$lastIndexOf instanceof String)) {
            char[] cArray = new char[]{c};
            n = StringsKt.lastIndexOfAny($this$lastIndexOf, cArray, startIndex, ignoreCase);
        } else {
            String string = (String)$this$lastIndexOf;
            n = string.lastIndexOf(c, startIndex);
        }
        return n;
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, char c, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = StringsKt.getLastIndex(charSequence);
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOf(charSequence, c, n, bl);
    }

    public static final int lastIndexOf(@NotNull CharSequence $this$lastIndexOf, @NotNull String string, int startIndex, boolean ignoreCase) {
        int n;
        Intrinsics.checkNotNullParameter($this$lastIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(string, "string");
        if (ignoreCase || !($this$lastIndexOf instanceof String)) {
            n = StringsKt__StringsKt.indexOf$StringsKt__StringsKt($this$lastIndexOf, string, startIndex, 0, ignoreCase, true);
        } else {
            String string2 = (String)$this$lastIndexOf;
            n = string2.lastIndexOf(string, startIndex);
        }
        return n;
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, String string, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = StringsKt.getLastIndex(charSequence);
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOf(charSequence, string, n, bl);
    }

    public static final boolean contains(@NotNull CharSequence $this$contains, @NotNull CharSequence other, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        return other instanceof String ? StringsKt.indexOf$default($this$contains, (String)other, 0, ignoreCase, 2, null) >= 0 : StringsKt__StringsKt.indexOf$StringsKt__StringsKt$default($this$contains, other, 0, $this$contains.length(), ignoreCase, false, 16, null) >= 0;
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.contains(charSequence, charSequence2, bl);
    }

    public static final boolean contains(@NotNull CharSequence $this$contains, char c, boolean ignoreCase) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return StringsKt.indexOf$default($this$contains, c, 0, ignoreCase, 2, null) >= 0;
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, char c, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.contains(charSequence, c, bl);
    }

    @InlineOnly
    private static final boolean contains(CharSequence $this$contains, Regex regex) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.containsMatchIn($this$contains);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence $this$rangesDelimitedBy, char[] delimiters, int startIndex, boolean ignoreCase, int limit) {
        StringsKt.requireNonNegativeLimit(limit);
        return new DelimitedRangesSequence($this$rangesDelimitedBy, startIndex, limit, (Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>>)new Function2<CharSequence, Integer, Pair<? extends Integer, ? extends Integer>>(delimiters, ignoreCase){
            final /* synthetic */ char[] $delimiters;
            final /* synthetic */ boolean $ignoreCase;
            {
                this.$delimiters = $delimiters;
                this.$ignoreCase = $ignoreCase;
                super(2);
            }

            @Nullable
            public final Pair<Integer, Integer> invoke(@NotNull CharSequence $this$$receiver, int currentIndex) {
                int n;
                Intrinsics.checkNotNullParameter($this$$receiver, "$this$$receiver");
                int it = n = StringsKt.indexOfAny($this$$receiver, this.$delimiters, currentIndex, this.$ignoreCase);
                boolean bl = false;
                return it < 0 ? null : TuplesKt.to(it, 1);
            }
        });
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, char[] cArray, int n, boolean bl, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        if ((n3 & 8) != 0) {
            n2 = 0;
        }
        return StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt(charSequence, cArray, n, bl, n2);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence $this$rangesDelimitedBy, String[] delimiters, int startIndex, boolean ignoreCase, int limit) {
        StringsKt.requireNonNegativeLimit(limit);
        List<String> delimitersList = ArraysKt.asList(delimiters);
        return new DelimitedRangesSequence($this$rangesDelimitedBy, startIndex, limit, (Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>>)new Function2<CharSequence, Integer, Pair<? extends Integer, ? extends Integer>>(delimitersList, ignoreCase){
            final /* synthetic */ List<String> $delimitersList;
            final /* synthetic */ boolean $ignoreCase;
            {
                this.$delimitersList = $delimitersList;
                this.$ignoreCase = $ignoreCase;
                super(2);
            }

            @Nullable
            public final Pair<Integer, Integer> invoke(@NotNull CharSequence $this$$receiver, int currentIndex) {
                Pair<A, Integer> pair;
                Intrinsics.checkNotNullParameter($this$$receiver, "$this$$receiver");
                Pair pair2 = StringsKt__StringsKt.access$findAnyOf($this$$receiver, this.$delimitersList, currentIndex, this.$ignoreCase, false);
                if (pair2 == null) {
                    pair = null;
                } else {
                    Pair pair3;
                    Pair it = pair3 = pair2;
                    boolean bl = false;
                    pair = TuplesKt.to(it.getFirst(), ((String)it.getSecond()).length());
                }
                return pair;
            }
        });
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, String[] stringArray, int n, boolean bl, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        if ((n3 & 8) != 0) {
            n2 = 0;
        }
        return StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt(charSequence, stringArray, n, bl, n2);
    }

    public static final void requireNonNegativeLimit(int limit) {
        boolean bl;
        boolean bl2 = bl = limit >= 0;
        if (!bl) {
            boolean bl3 = false;
            String string = Intrinsics.stringPlus("Limit must be non-negative, but was ", limit);
            throw new IllegalArgumentException(string.toString());
        }
    }

    @NotNull
    public static final Sequence<String> splitToSequence(@NotNull CharSequence $this$splitToSequence, @NotNull String[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkNotNullParameter($this$splitToSequence, "<this>");
        Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        return SequencesKt.map(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default($this$splitToSequence, delimiters, 0, ignoreCase, limit, 2, null), (Function1)new Function1<IntRange, String>($this$splitToSequence){
            final /* synthetic */ CharSequence $this_splitToSequence;
            {
                this.$this_splitToSequence = $receiver;
                super(1);
            }

            @NotNull
            public final String invoke(@NotNull IntRange it) {
                Intrinsics.checkNotNullParameter(it, "it");
                return StringsKt.substring(this.$this_splitToSequence, it);
            }
        });
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, String[] stringArray, boolean bl, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            n = 0;
        }
        return StringsKt.splitToSequence(charSequence, stringArray, bl, n);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final List<String> split(@NotNull CharSequence $this$split, @NotNull String[] delimiters, boolean ignoreCase, int limit) {
        void $this$mapTo$iv$iv;
        String delimiter;
        Intrinsics.checkNotNullParameter($this$split, "<this>");
        Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        if (delimiters.length == 1 && !(((CharSequence)(delimiter = delimiters[0])).length() == 0)) {
            return StringsKt__StringsKt.split$StringsKt__StringsKt($this$split, delimiter, ignoreCase, limit);
        }
        Iterable $this$map$iv = SequencesKt.asIterable(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default($this$split, delimiters, 0, ignoreCase, limit, 2, null));
        boolean $i$f$map = false;
        Iterable iterable = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void it;
            IntRange intRange = (IntRange)item$iv$iv;
            Collection collection = destination$iv$iv;
            boolean bl = false;
            String string = StringsKt.substring($this$split, (IntRange)it);
            collection.add(string);
        }
        return (List)destination$iv$iv;
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, String[] stringArray, boolean bl, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            n = 0;
        }
        return StringsKt.split(charSequence, stringArray, bl, n);
    }

    @NotNull
    public static final Sequence<String> splitToSequence(@NotNull CharSequence $this$splitToSequence, @NotNull char[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkNotNullParameter($this$splitToSequence, "<this>");
        Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        return SequencesKt.map(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default($this$splitToSequence, delimiters, 0, ignoreCase, limit, 2, null), (Function1)new Function1<IntRange, String>($this$splitToSequence){
            final /* synthetic */ CharSequence $this_splitToSequence;
            {
                this.$this_splitToSequence = $receiver;
                super(1);
            }

            @NotNull
            public final String invoke(@NotNull IntRange it) {
                Intrinsics.checkNotNullParameter(it, "it");
                return StringsKt.substring(this.$this_splitToSequence, it);
            }
        });
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, char[] cArray, boolean bl, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            n = 0;
        }
        return StringsKt.splitToSequence(charSequence, cArray, bl, n);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final List<String> split(@NotNull CharSequence $this$split, @NotNull char[] delimiters, boolean ignoreCase, int limit) {
        void $this$mapTo$iv$iv;
        Intrinsics.checkNotNullParameter($this$split, "<this>");
        Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        if (delimiters.length == 1) {
            return StringsKt__StringsKt.split$StringsKt__StringsKt($this$split, String.valueOf(delimiters[0]), ignoreCase, limit);
        }
        Iterable $this$map$iv = SequencesKt.asIterable(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default($this$split, delimiters, 0, ignoreCase, limit, 2, null));
        boolean $i$f$map = false;
        Iterable iterable = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void it;
            IntRange intRange = (IntRange)item$iv$iv;
            Collection collection = destination$iv$iv;
            boolean bl = false;
            String string = StringsKt.substring($this$split, (IntRange)it);
            collection.add(string);
        }
        return (List)destination$iv$iv;
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, char[] cArray, boolean bl, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            n = 0;
        }
        return StringsKt.split(charSequence, cArray, bl, n);
    }

    private static final List<String> split$StringsKt__StringsKt(CharSequence $this$split, String delimiter, boolean ignoreCase, int limit) {
        CharSequence charSequence;
        StringsKt.requireNonNegativeLimit(limit);
        int currentOffset = 0;
        int nextIndex = StringsKt.indexOf($this$split, delimiter, currentOffset, ignoreCase);
        if (nextIndex == -1 || limit == 1) {
            return CollectionsKt.listOf(((Object)$this$split).toString());
        }
        boolean isLimited = limit > 0;
        ArrayList<String> result = new ArrayList<String>(isLimited ? RangesKt.coerceAtMost(limit, 10) : 10);
        do {
            charSequence = $this$split;
            result.add(((Object)charSequence.subSequence(currentOffset, nextIndex)).toString());
            currentOffset = nextIndex + delimiter.length();
        } while ((!isLimited || result.size() != limit - 1) && (nextIndex = StringsKt.indexOf($this$split, delimiter, currentOffset, ignoreCase)) != -1);
        charSequence = $this$split;
        int n = $this$split.length();
        result.add(((Object)charSequence.subSequence(currentOffset, n)).toString());
        return result;
    }

    @InlineOnly
    private static final List<String> split(CharSequence $this$split, Regex regex, int limit) {
        Intrinsics.checkNotNullParameter($this$split, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.split($this$split, limit);
    }

    static /* synthetic */ List split$default(CharSequence $this$split_u24default, Regex regex, int limit, int n, Object object) {
        if ((n & 2) != 0) {
            limit = 0;
        }
        Intrinsics.checkNotNullParameter($this$split_u24default, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.split($this$split_u24default, limit);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final Sequence<String> splitToSequence(CharSequence $this$splitToSequence, Regex regex, int limit) {
        Intrinsics.checkNotNullParameter($this$splitToSequence, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.splitToSequence($this$splitToSequence, limit);
    }

    static /* synthetic */ Sequence splitToSequence$default(CharSequence $this$splitToSequence_u24default, Regex regex, int limit, int n, Object object) {
        if ((n & 2) != 0) {
            limit = 0;
        }
        Intrinsics.checkNotNullParameter($this$splitToSequence_u24default, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.splitToSequence($this$splitToSequence_u24default, limit);
    }

    @NotNull
    public static final Sequence<String> lineSequence(@NotNull CharSequence $this$lineSequence) {
        Intrinsics.checkNotNullParameter($this$lineSequence, "<this>");
        String[] stringArray = new String[]{"\r\n", "\n", "\r"};
        return StringsKt.splitToSequence$default($this$lineSequence, stringArray, false, 0, 6, null);
    }

    @NotNull
    public static final List<String> lines(@NotNull CharSequence $this$lines) {
        Intrinsics.checkNotNullParameter($this$lines, "<this>");
        return SequencesKt.toList(StringsKt.lineSequence($this$lines));
    }

    public static final boolean contentEqualsIgnoreCaseImpl(@Nullable CharSequence $this$contentEqualsIgnoreCaseImpl, @Nullable CharSequence other) {
        if ($this$contentEqualsIgnoreCaseImpl instanceof String && other instanceof String) {
            return StringsKt.equals((String)$this$contentEqualsIgnoreCaseImpl, (String)other, true);
        }
        if ($this$contentEqualsIgnoreCaseImpl == other) {
            return true;
        }
        if ($this$contentEqualsIgnoreCaseImpl == null || other == null || $this$contentEqualsIgnoreCaseImpl.length() != other.length()) {
            return false;
        }
        int n = 0;
        int n2 = $this$contentEqualsIgnoreCaseImpl.length();
        while (n < n2) {
            int i;
            if (CharsKt.equals($this$contentEqualsIgnoreCaseImpl.charAt(i = n++), other.charAt(i), true)) continue;
            return false;
        }
        return true;
    }

    public static final boolean contentEqualsImpl(@Nullable CharSequence $this$contentEqualsImpl, @Nullable CharSequence other) {
        if ($this$contentEqualsImpl instanceof String && other instanceof String) {
            return Intrinsics.areEqual($this$contentEqualsImpl, other);
        }
        if ($this$contentEqualsImpl == other) {
            return true;
        }
        if ($this$contentEqualsImpl == null || other == null || $this$contentEqualsImpl.length() != other.length()) {
            return false;
        }
        int n = 0;
        int n2 = $this$contentEqualsImpl.length();
        while (n < n2) {
            int i;
            if ($this$contentEqualsImpl.charAt(i = n++) == other.charAt(i)) continue;
            return false;
        }
        return true;
    }

    @SinceKotlin(version="1.5")
    public static final boolean toBooleanStrict(@NotNull String $this$toBooleanStrict) {
        boolean bl;
        Intrinsics.checkNotNullParameter($this$toBooleanStrict, "<this>");
        String string = $this$toBooleanStrict;
        if (Intrinsics.areEqual(string, "true")) {
            bl = true;
        } else if (Intrinsics.areEqual(string, "false")) {
            bl = false;
        } else {
            throw new IllegalArgumentException(Intrinsics.stringPlus("The string doesn't represent a boolean value: ", $this$toBooleanStrict));
        }
        return bl;
    }

    @SinceKotlin(version="1.5")
    @Nullable
    public static final Boolean toBooleanStrictOrNull(@NotNull String $this$toBooleanStrictOrNull) {
        Intrinsics.checkNotNullParameter($this$toBooleanStrictOrNull, "<this>");
        String string = $this$toBooleanStrictOrNull;
        return Intrinsics.areEqual(string, "true") ? Boolean.valueOf(true) : (Intrinsics.areEqual(string, "false") ? Boolean.valueOf(false) : null);
    }

    public static final /* synthetic */ Pair access$findAnyOf(CharSequence $receiver, Collection strings, int startIndex, boolean ignoreCase, boolean last) {
        return StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt($receiver, strings, startIndex, ignoreCase, last);
    }
}

