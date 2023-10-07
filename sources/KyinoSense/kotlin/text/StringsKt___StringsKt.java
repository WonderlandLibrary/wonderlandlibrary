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
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CharIterator;
import kotlin.collections.CollectionsKt;
import kotlin.collections.Grouping;
import kotlin.collections.IndexedValue;
import kotlin.collections.IndexingIterable;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.collections.SlidingWindowKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt___StringsJvmKt;
import kotlin.text.StringsKt___StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000\u00dc\u0001\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u001f\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\u000f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a!\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0010\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b*\u00020\u0002\u001a\u0010\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\n*\u00020\u0002\u001aE\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\u0086\b\u001a3\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u00020\u00050\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u001aM\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u001aN\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0018\b\u0001\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u00020\u00050\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u00a2\u0006\u0002\u0010\u0018\u001ah\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u00a2\u0006\u0002\u0010\u0019\u001a`\u0010\u001a\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\u0086\b\u00a2\u0006\u0002\u0010\u0018\u001a3\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\u000e*\u00020\u00022\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0087\b\u001aN\u0010\u001d\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\u000e\"\u0018\b\u0001\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u00020\u0005\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0087\b\u00a2\u0006\u0002\u0010\u0018\u001a\u001a\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001f*\u00020\u00022\u0006\u0010!\u001a\u00020\"H\u0007\u001a4\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\u001a\u0010$\u001a\b\u0012\u0004\u0012\u00020 0\n*\u00020\u00022\u0006\u0010!\u001a\u00020\"H\u0007\u001a4\u0010$\u001a\b\u0012\u0004\u0012\u0002H#0\n\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\r\u0010%\u001a\u00020\"*\u00020\u0002H\u0087\b\u001a!\u0010%\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010&\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010(\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010(\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a!\u0010)\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010)\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010*\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010*\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a)\u0010+\u001a\u00020\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"2\u0012\u0010-\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00050\u0004H\u0087\b\u001a\u001c\u0010.\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"H\u0087\b\u00a2\u0006\u0002\u0010/\u001a!\u00100\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u00100\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a6\u00101\u001a\u00020\u0002*\u00020\u00022'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000102H\u0086\b\u001a6\u00101\u001a\u00020 *\u00020 2'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000102H\u0086\b\u001aQ\u00105\u001a\u0002H6\"\f\b\u0000\u00106*\u000607j\u0002`8*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000102H\u0086\b\u00a2\u0006\u0002\u00109\u001a!\u0010:\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010:\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a<\u0010;\u001a\u0002H6\"\f\b\u0000\u00106*\u000607j\u0002`8*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u00a2\u0006\u0002\u0010<\u001a<\u0010=\u001a\u0002H6\"\f\b\u0000\u00106*\u000607j\u0002`8*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u00a2\u0006\u0002\u0010<\u001a(\u0010>\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0087\b\u00a2\u0006\u0002\u0010?\u001a(\u0010@\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0087\b\u00a2\u0006\u0002\u0010?\u001a\n\u0010A\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010A\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0011\u0010B\u001a\u0004\u0018\u00010\u0005*\u00020\u0002\u00a2\u0006\u0002\u0010C\u001a(\u0010B\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u00a2\u0006\u0002\u0010?\u001a3\u0010D\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H#0\b0\u0004H\u0086\b\u001aL\u0010E\u001a\u0002H6\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H#0\b0\u0004H\u0086\b\u00a2\u0006\u0002\u0010G\u001aI\u0010H\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2'\u0010J\u001a#\u0012\u0013\u0012\u0011H#\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#02H\u0086\b\u00a2\u0006\u0002\u0010L\u001a^\u0010M\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2<\u0010J\u001a8\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0013\u0012\u0011H#\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0NH\u0086\b\u00a2\u0006\u0002\u0010O\u001aI\u0010P\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2'\u0010J\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H#\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u0002H#02H\u0086\b\u00a2\u0006\u0002\u0010L\u001a^\u0010Q\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2<\u0010J\u001a8\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H#\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u0002H#0NH\u0086\b\u00a2\u0006\u0002\u0010O\u001a!\u0010R\u001a\u00020S*\u00020\u00022\u0012\u0010T\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020S0\u0004H\u0086\b\u001a6\u0010U\u001a\u00020S*\u00020\u00022'\u0010T\u001a#\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020S02H\u0086\b\u001a)\u0010V\u001a\u00020\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"2\u0012\u0010-\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00050\u0004H\u0087\b\u001a\u0019\u0010W\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"\u00a2\u0006\u0002\u0010/\u001a9\u0010X\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001f0\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u001aS\u0010X\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u001f0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u001aR\u0010Y\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u001c\b\u0001\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050Z0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u00a2\u0006\u0002\u0010\u0018\u001al\u0010Y\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u001c\b\u0002\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0Z0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u00a2\u0006\u0002\u0010\u0019\u001a5\u0010[\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\\\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0014\b\u0004\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0087\b\u001a!\u0010]\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010^\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\n\u0010_\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010_\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0011\u0010`\u001a\u0004\u0018\u00010\u0005*\u00020\u0002\u00a2\u0006\u0002\u0010C\u001a(\u0010`\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u00a2\u0006\u0002\u0010?\u001a-\u0010a\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b\u001aB\u0010b\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#02H\u0086\b\u001aH\u0010c\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\b\b\u0000\u0010#*\u00020d*\u00020\u00022)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#02H\u0086\b\u001aa\u0010e\u001a\u0002H6\"\b\b\u0000\u0010#*\u00020d\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#02H\u0086\b\u00a2\u0006\u0002\u0010f\u001a[\u0010g\u001a\u0002H6\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#02H\u0086\b\u00a2\u0006\u0002\u0010f\u001a3\u0010h\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\b\b\u0000\u0010#*\u00020d*\u00020\u00022\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#0\u0004H\u0086\b\u001aL\u0010i\u001a\u0002H6\"\b\b\u0000\u0010#*\u00020d\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#0\u0004H\u0086\b\u00a2\u0006\u0002\u0010G\u001aF\u0010j\u001a\u0002H6\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b\u00a2\u0006\u0002\u0010G\u001a\u0011\u0010k\u001a\u0004\u0018\u00010\u0005*\u00020\u0002\u00a2\u0006\u0002\u0010C\u001a8\u0010l\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010#*\b\u0012\u0004\u0012\u0002H#0m*\u00020\u00022\u0012\u0010n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b\u00a2\u0006\u0002\u0010?\u001a-\u0010o\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010p\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050qj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`r\u00a2\u0006\u0002\u0010s\u001a\u0011\u0010t\u001a\u0004\u0018\u00010\u0005*\u00020\u0002\u00a2\u0006\u0002\u0010C\u001a8\u0010u\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010#*\b\u0012\u0004\u0012\u0002H#0m*\u00020\u00022\u0012\u0010n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b\u00a2\u0006\u0002\u0010?\u001a-\u0010v\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010p\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050qj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`r\u00a2\u0006\u0002\u0010s\u001a\n\u0010w\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010w\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a0\u0010x\u001a\u0002Hy\"\b\b\u0000\u0010y*\u00020\u0002*\u0002Hy2\u0012\u0010T\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020S0\u0004H\u0087\b\u00a2\u0006\u0002\u0010z\u001a-\u0010{\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a-\u0010{\u001a\u000e\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020 0\u0010*\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\r\u0010|\u001a\u00020\u0005*\u00020\u0002H\u0087\b\u001a\u0014\u0010|\u001a\u00020\u0005*\u00020\u00022\u0006\u0010|\u001a\u00020}H\u0007\u001a6\u0010~\u001a\u00020\u0005*\u00020\u00022'\u0010J\u001a#\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000502H\u0086\b\u001aK\u0010\u007f\u001a\u00020\u0005*\u00020\u00022<\u0010J\u001a8\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050NH\u0086\b\u001a7\u0010\u0080\u0001\u001a\u00020\u0005*\u00020\u00022'\u0010J\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u000502H\u0086\b\u001aL\u0010\u0081\u0001\u001a\u00020\u0005*\u00020\u00022<\u0010J\u001a8\u0012\u0013\u0012\u00110\"\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u00050NH\u0086\b\u001a\u000b\u0010\u0082\u0001\u001a\u00020\u0002*\u00020\u0002\u001a\u000e\u0010\u0082\u0001\u001a\u00020 *\u00020 H\u0087\b\u001a\u000b\u0010\u0083\u0001\u001a\u00020\u0005*\u00020\u0002\u001a\"\u0010\u0083\u0001\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0012\u0010\u0084\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u0002\u00a2\u0006\u0002\u0010C\u001a)\u0010\u0084\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u00a2\u0006\u0002\u0010?\u001a\u001a\u0010\u0085\u0001\u001a\u00020\u0002*\u00020\u00022\r\u0010\u0086\u0001\u001a\b\u0012\u0004\u0012\u00020\"0\b\u001a\u0015\u0010\u0085\u0001\u001a\u00020\u0002*\u00020\u00022\b\u0010\u0086\u0001\u001a\u00030\u0087\u0001\u001a\u001d\u0010\u0085\u0001\u001a\u00020 *\u00020 2\r\u0010\u0086\u0001\u001a\b\u0012\u0004\u0012\u00020\"0\bH\u0087\b\u001a\u0015\u0010\u0085\u0001\u001a\u00020 *\u00020 2\b\u0010\u0086\u0001\u001a\u00030\u0087\u0001\u001a\"\u0010\u0088\u0001\u001a\u00020\"*\u00020\u00022\u0012\u0010n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\"0\u0004H\u0086\b\u001a$\u0010\u0089\u0001\u001a\u00030\u008a\u0001*\u00020\u00022\u0013\u0010n\u001a\u000f\u0012\u0004\u0012\u00020\u0005\u0012\u0005\u0012\u00030\u008a\u00010\u0004H\u0086\b\u001a\u0013\u0010\u008b\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u008b\u0001\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u008c\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u008c\u0001\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\"\u0010\u008d\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u008d\u0001\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u008e\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u008e\u0001\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a+\u0010\u008f\u0001\u001a\u0002H6\"\u0010\b\u0000\u00106*\n\u0012\u0006\b\u0000\u0012\u00020\u00050F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H6\u00a2\u0006\u0003\u0010\u0090\u0001\u001a\u001d\u0010\u0091\u0001\u001a\u0014\u0012\u0004\u0012\u00020\u00050\u0092\u0001j\t\u0012\u0004\u0012\u00020\u0005`\u0093\u0001*\u00020\u0002\u001a\u0011\u0010\u0094\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050\u001f*\u00020\u0002\u001a\u0011\u0010\u0095\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050Z*\u00020\u0002\u001a\u0012\u0010\u0096\u0001\u001a\t\u0012\u0004\u0012\u00020\u00050\u0097\u0001*\u00020\u0002\u001a1\u0010\u0098\u0001\u001a\b\u0012\u0004\u0012\u00020 0\u001f*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u0099\u0001\u001a\u00020\"2\t\b\u0002\u0010\u009a\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010\u0098\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u0099\u0001\u001a\u00020\"2\t\b\u0002\u0010\u009a\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a1\u0010\u009b\u0001\u001a\b\u0012\u0004\u0012\u00020 0\n*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u0099\u0001\u001a\u00020\"2\t\b\u0002\u0010\u009a\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010\u009b\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\n\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u0099\u0001\u001a\u00020\"2\t\b\u0002\u0010\u009a\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\u0018\u0010\u009c\u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0004\u0012\u00020\u00050\u009d\u00010\b*\u00020\u0002\u001a)\u0010\u009e\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001f*\u00020\u00022\u0007\u0010\u009f\u0001\u001a\u00020\u0002H\u0086\u0004\u001a]\u0010\u009e\u0001\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u001f\"\u0004\b\u0000\u0010\u000e*\u00020\u00022\u0007\u0010\u009f\u0001\u001a\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005\u00a2\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b(\u00a0\u0001\u0012\u0014\u0012\u00120\u0005\u00a2\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b(\u00a1\u0001\u0012\u0004\u0012\u0002H\u000e02H\u0086\b\u001a\u001f\u0010\u00a2\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001f*\u00020\u0002H\u0007\u001aT\u0010\u00a2\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005\u00a2\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b(\u00a0\u0001\u0012\u0014\u0012\u00120\u0005\u00a2\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b(\u00a1\u0001\u0012\u0004\u0012\u0002H#02H\u0087\b\u00a8\u0006\u00a3\u0001"}, d2={"all", "", "", "predicate", "Lkotlin/Function1;", "", "any", "asIterable", "", "asSequence", "Lkotlin/sequences/Sequence;", "associate", "", "K", "V", "transform", "Lkotlin/Pair;", "associateBy", "keySelector", "valueTransform", "associateByTo", "M", "", "destination", "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "associateTo", "associateWith", "valueSelector", "associateWithTo", "chunked", "", "", "size", "", "R", "chunkedSequence", "count", "drop", "n", "dropLast", "dropLastWhile", "dropWhile", "elementAtOrElse", "index", "defaultValue", "elementAtOrNull", "(Ljava/lang/CharSequence;I)Ljava/lang/Character;", "filter", "filterIndexed", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "filterIndexedTo", "C", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function2;)Ljava/lang/Appendable;", "filterNot", "filterNotTo", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Appendable;", "filterTo", "find", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Character;", "findLast", "first", "firstOrNull", "(Ljava/lang/CharSequence;)Ljava/lang/Character;", "flatMap", "flatMapTo", "", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;)Ljava/util/Collection;", "fold", "initial", "operation", "acc", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "foldIndexed", "Lkotlin/Function3;", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "foldRight", "foldRightIndexed", "forEach", "", "action", "forEachIndexed", "getOrElse", "getOrNull", "groupBy", "groupByTo", "", "groupingBy", "Lkotlin/collections/Grouping;", "indexOfFirst", "indexOfLast", "last", "lastOrNull", "map", "mapIndexed", "mapIndexedNotNull", "", "mapIndexedNotNullTo", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function2;)Ljava/util/Collection;", "mapIndexedTo", "mapNotNull", "mapNotNullTo", "mapTo", "max", "maxBy", "", "selector", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/lang/CharSequence;Ljava/util/Comparator;)Ljava/lang/Character;", "min", "minBy", "minWith", "none", "onEach", "S", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/CharSequence;", "partition", "random", "Lkotlin/random/Random;", "reduce", "reduceIndexed", "reduceRight", "reduceRightIndexed", "reversed", "single", "singleOrNull", "slice", "indices", "Lkotlin/ranges/IntRange;", "sumBy", "sumByDouble", "", "take", "takeLast", "takeLastWhile", "takeWhile", "toCollection", "(Ljava/lang/CharSequence;Ljava/util/Collection;)Ljava/util/Collection;", "toHashSet", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "toList", "toMutableList", "toSet", "", "windowed", "step", "partialWindows", "windowedSequence", "withIndex", "Lkotlin/collections/IndexedValue;", "zip", "other", "a", "b", "zipWithNext", "kotlin-stdlib"}, xs="kotlin/text/StringsKt")
class StringsKt___StringsKt
extends StringsKt___StringsJvmKt {
    @InlineOnly
    private static final char elementAtOrElse(@NotNull CharSequence $this$elementAtOrElse, int index, Function1<? super Integer, Character> defaultValue) {
        int $i$f$elementAtOrElse = 0;
        return index >= 0 && index <= StringsKt.getLastIndex($this$elementAtOrElse) ? $this$elementAtOrElse.charAt(index) : defaultValue.invoke((Integer)index).charValue();
    }

    @InlineOnly
    private static final Character elementAtOrNull(@NotNull CharSequence $this$elementAtOrNull, int index) {
        int $i$f$elementAtOrNull = 0;
        return StringsKt.getOrNull($this$elementAtOrNull, index);
    }

    @InlineOnly
    private static final Character find(@NotNull CharSequence $this$find, Function1<? super Character, Boolean> predicate) {
        Character c;
        block1: {
            int $i$f$find = 0;
            CharSequence $this$firstOrNull$iv = $this$find;
            boolean $i$f$firstOrNull = false;
            CharSequence charSequence = $this$firstOrNull$iv;
            for (int i = 0; i < charSequence.length(); ++i) {
                char element$iv = charSequence.charAt(i);
                if (!predicate.invoke(Character.valueOf(element$iv)).booleanValue()) continue;
                c = Character.valueOf(element$iv);
                break block1;
            }
            c = null;
        }
        return c;
    }

    /*
     * WARNING - void declaration
     */
    @InlineOnly
    private static final Character findLast(@NotNull CharSequence $this$findLast, Function1<? super Character, Boolean> predicate) {
        Character c;
        block2: {
            int $i$f$findLast = 0;
            CharSequence $this$lastOrNull$iv = $this$findLast;
            boolean $i$f$lastOrNull = false;
            int n = $this$lastOrNull$iv.length();
            --n;
            boolean bl = false;
            while (n >= 0) {
                void index$iv;
                char element$iv = $this$lastOrNull$iv.charAt((int)index$iv);
                if (predicate.invoke(Character.valueOf(element$iv)).booleanValue()) {
                    c = Character.valueOf(element$iv);
                    break block2;
                }
                --index$iv;
            }
            c = null;
        }
        return c;
    }

    public static final char first(@NotNull CharSequence $this$first) {
        Intrinsics.checkParameterIsNotNull($this$first, "$this$first");
        CharSequence charSequence = $this$first;
        boolean bl = false;
        if (charSequence.length() == 0) {
            throw (Throwable)new NoSuchElementException("Char sequence is empty.");
        }
        return $this$first.charAt(0);
    }

    public static final char first(@NotNull CharSequence $this$first, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$first = 0;
        Intrinsics.checkParameterIsNotNull($this$first, "$this$first");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$first;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            if (!predicate.invoke(Character.valueOf(element)).booleanValue()) continue;
            return element;
        }
        throw (Throwable)new NoSuchElementException("Char sequence contains no character matching the predicate.");
    }

    @Nullable
    public static final Character firstOrNull(@NotNull CharSequence $this$firstOrNull) {
        Intrinsics.checkParameterIsNotNull($this$firstOrNull, "$this$firstOrNull");
        CharSequence charSequence = $this$firstOrNull;
        boolean bl = false;
        return charSequence.length() == 0 ? null : Character.valueOf($this$firstOrNull.charAt(0));
    }

    @Nullable
    public static final Character firstOrNull(@NotNull CharSequence $this$firstOrNull, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$firstOrNull = 0;
        Intrinsics.checkParameterIsNotNull($this$firstOrNull, "$this$firstOrNull");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$firstOrNull;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            if (!predicate.invoke(Character.valueOf(element)).booleanValue()) continue;
            return Character.valueOf(element);
        }
        return null;
    }

    @InlineOnly
    private static final char getOrElse(@NotNull CharSequence $this$getOrElse, int index, Function1<? super Integer, Character> defaultValue) {
        int $i$f$getOrElse = 0;
        return index >= 0 && index <= StringsKt.getLastIndex($this$getOrElse) ? $this$getOrElse.charAt(index) : defaultValue.invoke((Integer)index).charValue();
    }

    @Nullable
    public static final Character getOrNull(@NotNull CharSequence $this$getOrNull, int index) {
        Intrinsics.checkParameterIsNotNull($this$getOrNull, "$this$getOrNull");
        return index >= 0 && index <= StringsKt.getLastIndex($this$getOrNull) ? Character.valueOf($this$getOrNull.charAt(index)) : null;
    }

    /*
     * WARNING - void declaration
     */
    public static final int indexOfFirst(@NotNull CharSequence $this$indexOfFirst, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$indexOfFirst = 0;
        Intrinsics.checkParameterIsNotNull($this$indexOfFirst, "$this$indexOfFirst");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = 0;
        int n2 = $this$indexOfFirst.length();
        while (n < n2) {
            void index;
            if (predicate.invoke(Character.valueOf($this$indexOfFirst.charAt((int)index))).booleanValue()) {
                return (int)index;
            }
            ++index;
        }
        return -1;
    }

    /*
     * WARNING - void declaration
     */
    public static final int indexOfLast(@NotNull CharSequence $this$indexOfLast, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$indexOfLast = 0;
        Intrinsics.checkParameterIsNotNull($this$indexOfLast, "$this$indexOfLast");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = $this$indexOfLast.length();
        --n;
        boolean bl = false;
        while (n >= 0) {
            void index;
            if (predicate.invoke(Character.valueOf($this$indexOfLast.charAt((int)index))).booleanValue()) {
                return (int)index;
            }
            --index;
        }
        return -1;
    }

    public static final char last(@NotNull CharSequence $this$last) {
        Intrinsics.checkParameterIsNotNull($this$last, "$this$last");
        CharSequence charSequence = $this$last;
        boolean bl = false;
        if (charSequence.length() == 0) {
            throw (Throwable)new NoSuchElementException("Char sequence is empty.");
        }
        return $this$last.charAt(StringsKt.getLastIndex($this$last));
    }

    /*
     * WARNING - void declaration
     */
    public static final char last(@NotNull CharSequence $this$last, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$last = 0;
        Intrinsics.checkParameterIsNotNull($this$last, "$this$last");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = $this$last.length();
        --n;
        boolean bl = false;
        while (n >= 0) {
            void index;
            char element = $this$last.charAt((int)index);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                return element;
            }
            --index;
        }
        throw (Throwable)new NoSuchElementException("Char sequence contains no character matching the predicate.");
    }

    @Nullable
    public static final Character lastOrNull(@NotNull CharSequence $this$lastOrNull) {
        Intrinsics.checkParameterIsNotNull($this$lastOrNull, "$this$lastOrNull");
        CharSequence charSequence = $this$lastOrNull;
        boolean bl = false;
        return charSequence.length() == 0 ? null : Character.valueOf($this$lastOrNull.charAt($this$lastOrNull.length() - 1));
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public static final Character lastOrNull(@NotNull CharSequence $this$lastOrNull, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$lastOrNull = 0;
        Intrinsics.checkParameterIsNotNull($this$lastOrNull, "$this$lastOrNull");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = $this$lastOrNull.length();
        --n;
        boolean bl = false;
        while (n >= 0) {
            void index;
            char element = $this$lastOrNull.charAt((int)index);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                return Character.valueOf(element);
            }
            --index;
        }
        return null;
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final char random(@NotNull CharSequence $this$random) {
        int $i$f$random = 0;
        return StringsKt.random($this$random, Random.Default);
    }

    @SinceKotlin(version="1.3")
    public static final char random(@NotNull CharSequence $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        CharSequence charSequence = $this$random;
        boolean bl = false;
        if (charSequence.length() == 0) {
            throw (Throwable)new NoSuchElementException("Char sequence is empty.");
        }
        return $this$random.charAt(random.nextInt($this$random.length()));
    }

    public static final char single(@NotNull CharSequence $this$single) {
        Intrinsics.checkParameterIsNotNull($this$single, "$this$single");
        switch ($this$single.length()) {
            case 0: {
                throw (Throwable)new NoSuchElementException("Char sequence is empty.");
            }
            case 1: {
                break;
            }
            default: {
                throw (Throwable)new IllegalArgumentException("Char sequence has more than one element.");
            }
        }
        return $this$single.charAt(0);
    }

    public static final char single(@NotNull CharSequence $this$single, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$single = 0;
        Intrinsics.checkParameterIsNotNull($this$single, "$this$single");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        Character single = null;
        boolean found = false;
        CharSequence charSequence = $this$single;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            if (!predicate.invoke(Character.valueOf(element)).booleanValue()) continue;
            if (found) {
                throw (Throwable)new IllegalArgumentException("Char sequence contains more than one matching element.");
            }
            single = Character.valueOf(element);
            found = true;
        }
        if (!found) {
            throw (Throwable)new NoSuchElementException("Char sequence contains no character matching the predicate.");
        }
        Character c = single;
        if (c == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Char");
        }
        return c.charValue();
    }

    @Nullable
    public static final Character singleOrNull(@NotNull CharSequence $this$singleOrNull) {
        Intrinsics.checkParameterIsNotNull($this$singleOrNull, "$this$singleOrNull");
        return $this$singleOrNull.length() == 1 ? Character.valueOf($this$singleOrNull.charAt(0)) : null;
    }

    @Nullable
    public static final Character singleOrNull(@NotNull CharSequence $this$singleOrNull, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$singleOrNull = 0;
        Intrinsics.checkParameterIsNotNull($this$singleOrNull, "$this$singleOrNull");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        Character single = null;
        boolean found = false;
        CharSequence charSequence = $this$singleOrNull;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            if (!predicate.invoke(Character.valueOf(element)).booleanValue()) continue;
            if (found) {
                return null;
            }
            single = Character.valueOf(element);
            found = true;
        }
        if (!found) {
            return null;
        }
        return single;
    }

    @NotNull
    public static final CharSequence drop(@NotNull CharSequence $this$drop, int n) {
        Intrinsics.checkParameterIsNotNull($this$drop, "$this$drop");
        boolean bl = n >= 0;
        boolean bl2 = false;
        boolean bl3 = false;
        if (!bl) {
            boolean bl4 = false;
            String string = "Requested character count " + n + " is less than zero.";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        return $this$drop.subSequence(RangesKt.coerceAtMost(n, $this$drop.length()), $this$drop.length());
    }

    @NotNull
    public static final String drop(@NotNull String $this$drop, int n) {
        Intrinsics.checkParameterIsNotNull($this$drop, "$this$drop");
        boolean bl = n >= 0;
        int n2 = 0;
        boolean bl2 = false;
        if (!bl) {
            boolean bl3 = false;
            String string = "Requested character count " + n + " is less than zero.";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        String string = $this$drop;
        n2 = RangesKt.coerceAtMost(n, $this$drop.length());
        bl2 = false;
        String string2 = string.substring(n2);
        Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).substring(startIndex)");
        return string2;
    }

    @NotNull
    public static final CharSequence dropLast(@NotNull CharSequence $this$dropLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$dropLast, "$this$dropLast");
        boolean bl = n >= 0;
        boolean bl2 = false;
        boolean bl3 = false;
        if (!bl) {
            boolean bl4 = false;
            String string = "Requested character count " + n + " is less than zero.";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        return StringsKt.take($this$dropLast, RangesKt.coerceAtLeast($this$dropLast.length() - n, 0));
    }

    @NotNull
    public static final String dropLast(@NotNull String $this$dropLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$dropLast, "$this$dropLast");
        boolean bl = n >= 0;
        boolean bl2 = false;
        boolean bl3 = false;
        if (!bl) {
            boolean bl4 = false;
            String string = "Requested character count " + n + " is less than zero.";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        return StringsKt.take($this$dropLast, RangesKt.coerceAtLeast($this$dropLast.length() - n, 0));
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final CharSequence dropLastWhile(@NotNull CharSequence $this$dropLastWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$dropLastWhile = 0;
        Intrinsics.checkParameterIsNotNull($this$dropLastWhile, "$this$dropLastWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = StringsKt.getLastIndex($this$dropLastWhile);
        boolean bl = false;
        while (n >= 0) {
            void index;
            if (!predicate.invoke(Character.valueOf($this$dropLastWhile.charAt((int)index))).booleanValue()) {
                return $this$dropLastWhile.subSequence(0, (int)(index + true));
            }
            --index;
        }
        return "";
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String dropLastWhile(@NotNull String $this$dropLastWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$dropLastWhile = 0;
        Intrinsics.checkParameterIsNotNull($this$dropLastWhile, "$this$dropLastWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = StringsKt.getLastIndex($this$dropLastWhile);
        boolean bl = false;
        while (n >= 0) {
            void index;
            if (!predicate.invoke(Character.valueOf($this$dropLastWhile.charAt((int)index))).booleanValue()) {
                String string = $this$dropLastWhile;
                int n2 = 0;
                void var7_7 = index + true;
                boolean bl2 = false;
                String string2 = string.substring(n2, (int)var7_7);
                Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                return string2;
            }
            --index;
        }
        return "";
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final CharSequence dropWhile(@NotNull CharSequence $this$dropWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$dropWhile = 0;
        Intrinsics.checkParameterIsNotNull($this$dropWhile, "$this$dropWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = 0;
        int n2 = $this$dropWhile.length();
        while (n < n2) {
            void index;
            if (!predicate.invoke(Character.valueOf($this$dropWhile.charAt((int)index))).booleanValue()) {
                return $this$dropWhile.subSequence((int)index, $this$dropWhile.length());
            }
            ++index;
        }
        return "";
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String dropWhile(@NotNull String $this$dropWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$dropWhile = 0;
        Intrinsics.checkParameterIsNotNull($this$dropWhile, "$this$dropWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = 0;
        int n2 = ((CharSequence)$this$dropWhile).length();
        while (n < n2) {
            void index;
            if (!predicate.invoke(Character.valueOf($this$dropWhile.charAt((int)index))).booleanValue()) {
                String string = $this$dropWhile;
                boolean bl = false;
                String string2 = string.substring((int)index);
                Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).substring(startIndex)");
                return string2;
            }
            ++index;
        }
        return "";
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final CharSequence filter(@NotNull CharSequence $this$filter, @NotNull Function1<? super Character, Boolean> predicate) {
        void $this$filterTo$iv;
        int $i$f$filter = 0;
        Intrinsics.checkParameterIsNotNull($this$filter, "$this$filter");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$filter;
        Appendable destination$iv = new StringBuilder();
        boolean $i$f$filterTo = false;
        int n = 0;
        int n2 = $this$filterTo$iv.length();
        while (n < n2) {
            void index$iv;
            char element$iv = $this$filterTo$iv.charAt((int)index$iv);
            if (predicate.invoke(Character.valueOf(element$iv)).booleanValue()) {
                destination$iv.append(element$iv);
            }
            ++index$iv;
        }
        return (CharSequence)((Object)destination$iv);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String filter(@NotNull String $this$filter, @NotNull Function1<? super Character, Boolean> predicate) {
        void $this$filterTo$iv;
        int $i$f$filter = 0;
        Intrinsics.checkParameterIsNotNull($this$filter, "$this$filter");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$filter;
        Appendable destination$iv = new StringBuilder();
        boolean $i$f$filterTo = false;
        int n = 0;
        int n2 = $this$filterTo$iv.length();
        while (n < n2) {
            void index$iv;
            char element$iv = $this$filterTo$iv.charAt((int)index$iv);
            if (predicate.invoke(Character.valueOf(element$iv)).booleanValue()) {
                destination$iv.append(element$iv);
            }
            ++index$iv;
        }
        String string = ((StringBuilder)destination$iv).toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "filterTo(StringBuilder(), predicate).toString()");
        return string;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final CharSequence filterIndexed(@NotNull CharSequence $this$filterIndexed, @NotNull Function2<? super Integer, ? super Character, Boolean> predicate) {
        void $this$filterIndexedTo$iv;
        int $i$f$filterIndexed = 0;
        Intrinsics.checkParameterIsNotNull($this$filterIndexed, "$this$filterIndexed");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$filterIndexed;
        Appendable destination$iv = new StringBuilder();
        boolean $i$f$filterIndexedTo = false;
        void $this$forEachIndexed$iv$iv = $this$filterIndexedTo$iv;
        boolean $i$f$forEachIndexed = false;
        int index$iv$iv = 0;
        void var9_9 = $this$forEachIndexed$iv$iv;
        for (int i = 0; i < var9_9.length(); ++i) {
            void element$iv;
            char item$iv$iv = var9_9.charAt(i);
            int n = index$iv$iv++;
            char c = item$iv$iv;
            int index$iv = n;
            boolean bl = false;
            if (!predicate.invoke((Integer)index$iv, Character.valueOf((char)element$iv)).booleanValue()) continue;
            destination$iv.append((char)element$iv);
        }
        return (CharSequence)((Object)destination$iv);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String filterIndexed(@NotNull String $this$filterIndexed, @NotNull Function2<? super Integer, ? super Character, Boolean> predicate) {
        void $this$filterIndexedTo$iv;
        int $i$f$filterIndexed = 0;
        Intrinsics.checkParameterIsNotNull($this$filterIndexed, "$this$filterIndexed");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$filterIndexed;
        Appendable destination$iv = new StringBuilder();
        boolean $i$f$filterIndexedTo = false;
        void $this$forEachIndexed$iv$iv = $this$filterIndexedTo$iv;
        boolean $i$f$forEachIndexed = false;
        int index$iv$iv = 0;
        void var9_9 = $this$forEachIndexed$iv$iv;
        for (int i = 0; i < var9_9.length(); ++i) {
            void element$iv;
            char item$iv$iv = var9_9.charAt(i);
            int n = index$iv$iv++;
            char c = item$iv$iv;
            int index$iv = n;
            boolean bl = false;
            if (!predicate.invoke((Integer)index$iv, Character.valueOf((char)element$iv)).booleanValue()) continue;
            destination$iv.append((char)element$iv);
        }
        String string = ((StringBuilder)destination$iv).toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "filterIndexedTo(StringBu\u2026(), predicate).toString()");
        return string;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <C extends Appendable> C filterIndexedTo(@NotNull CharSequence $this$filterIndexedTo, @NotNull C destination, @NotNull Function2<? super Integer, ? super Character, Boolean> predicate) {
        int $i$f$filterIndexedTo = 0;
        Intrinsics.checkParameterIsNotNull($this$filterIndexedTo, "$this$filterIndexedTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence $this$forEachIndexed$iv = $this$filterIndexedTo;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        CharSequence charSequence = $this$forEachIndexed$iv;
        for (int i = 0; i < charSequence.length(); ++i) {
            void element;
            char item$iv = charSequence.charAt(i);
            int n = index$iv++;
            char c = item$iv;
            int index = n;
            boolean bl = false;
            if (!predicate.invoke((Integer)index, Character.valueOf((char)element)).booleanValue()) continue;
            destination.append((char)element);
        }
        return destination;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final CharSequence filterNot(@NotNull CharSequence $this$filterNot, @NotNull Function1<? super Character, Boolean> predicate) {
        void $this$filterNotTo$iv;
        int $i$f$filterNot = 0;
        Intrinsics.checkParameterIsNotNull($this$filterNot, "$this$filterNot");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$filterNot;
        Appendable destination$iv = new StringBuilder();
        boolean $i$f$filterNotTo = false;
        void var6_6 = $this$filterNotTo$iv;
        for (int i = 0; i < var6_6.length(); ++i) {
            char element$iv = var6_6.charAt(i);
            if (predicate.invoke(Character.valueOf(element$iv)).booleanValue()) continue;
            destination$iv.append(element$iv);
        }
        return (CharSequence)((Object)destination$iv);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String filterNot(@NotNull String $this$filterNot, @NotNull Function1<? super Character, Boolean> predicate) {
        void $this$filterNotTo$iv;
        int $i$f$filterNot = 0;
        Intrinsics.checkParameterIsNotNull($this$filterNot, "$this$filterNot");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$filterNot;
        Appendable destination$iv = new StringBuilder();
        boolean $i$f$filterNotTo = false;
        void var6_6 = $this$filterNotTo$iv;
        for (int i = 0; i < var6_6.length(); ++i) {
            char element$iv = var6_6.charAt(i);
            if (predicate.invoke(Character.valueOf(element$iv)).booleanValue()) continue;
            destination$iv.append(element$iv);
        }
        String string = ((StringBuilder)destination$iv).toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "filterNotTo(StringBuilder(), predicate).toString()");
        return string;
    }

    @NotNull
    public static final <C extends Appendable> C filterNotTo(@NotNull CharSequence $this$filterNotTo, @NotNull C destination, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$filterNotTo = 0;
        Intrinsics.checkParameterIsNotNull($this$filterNotTo, "$this$filterNotTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$filterNotTo;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) continue;
            destination.append(element);
        }
        return destination;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <C extends Appendable> C filterTo(@NotNull CharSequence $this$filterTo, @NotNull C destination, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$filterTo = 0;
        Intrinsics.checkParameterIsNotNull($this$filterTo, "$this$filterTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = 0;
        int n2 = $this$filterTo.length();
        while (n < n2) {
            void index;
            char element = $this$filterTo.charAt((int)index);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                destination.append(element);
            }
            ++index;
        }
        return destination;
    }

    @NotNull
    public static final CharSequence slice(@NotNull CharSequence $this$slice, @NotNull IntRange indices) {
        Intrinsics.checkParameterIsNotNull($this$slice, "$this$slice");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        if (indices.isEmpty()) {
            return "";
        }
        return StringsKt.subSequence($this$slice, indices);
    }

    @NotNull
    public static final String slice(@NotNull String $this$slice, @NotNull IntRange indices) {
        Intrinsics.checkParameterIsNotNull($this$slice, "$this$slice");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        if (indices.isEmpty()) {
            return "";
        }
        return StringsKt.substring($this$slice, indices);
    }

    @NotNull
    public static final CharSequence slice(@NotNull CharSequence $this$slice, @NotNull Iterable<Integer> indices) {
        Intrinsics.checkParameterIsNotNull($this$slice, "$this$slice");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        int size = CollectionsKt.collectionSizeOrDefault(indices, 10);
        if (size == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder(size);
        Iterator<Integer> iterator2 = indices.iterator();
        while (iterator2.hasNext()) {
            int i = ((Number)iterator2.next()).intValue();
            result.append($this$slice.charAt(i));
        }
        return result;
    }

    @InlineOnly
    private static final String slice(@NotNull String $this$slice, Iterable<Integer> indices) {
        int $i$f$slice = 0;
        String string = $this$slice;
        if (string == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return ((Object)StringsKt.slice((CharSequence)string, indices)).toString();
    }

    @NotNull
    public static final CharSequence take(@NotNull CharSequence $this$take, int n) {
        Intrinsics.checkParameterIsNotNull($this$take, "$this$take");
        boolean bl = n >= 0;
        boolean bl2 = false;
        boolean bl3 = false;
        if (!bl) {
            boolean bl4 = false;
            String string = "Requested character count " + n + " is less than zero.";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        return $this$take.subSequence(0, RangesKt.coerceAtMost(n, $this$take.length()));
    }

    @NotNull
    public static final String take(@NotNull String $this$take, int n) {
        Intrinsics.checkParameterIsNotNull($this$take, "$this$take");
        boolean bl = n >= 0;
        int n2 = 0;
        int n3 = 0;
        if (!bl) {
            boolean bl2 = false;
            String string = "Requested character count " + n + " is less than zero.";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        String string = $this$take;
        n2 = 0;
        n3 = RangesKt.coerceAtMost(n, $this$take.length());
        boolean bl3 = false;
        String string2 = string.substring(n2, n3);
        Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        return string2;
    }

    @NotNull
    public static final CharSequence takeLast(@NotNull CharSequence $this$takeLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$takeLast, "$this$takeLast");
        boolean bl = n >= 0;
        boolean bl2 = false;
        boolean bl3 = false;
        if (!bl) {
            boolean bl4 = false;
            String string = "Requested character count " + n + " is less than zero.";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        int length = $this$takeLast.length();
        return $this$takeLast.subSequence(length - RangesKt.coerceAtMost(n, length), length);
    }

    @NotNull
    public static final String takeLast(@NotNull String $this$takeLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$takeLast, "$this$takeLast");
        boolean bl = n >= 0;
        boolean bl2 = false;
        int n2 = 0;
        if (!bl) {
            boolean bl3 = false;
            String string = "Requested character count " + n + " is less than zero.";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        int length = $this$takeLast.length();
        String string = $this$takeLast;
        n2 = length - RangesKt.coerceAtMost(n, length);
        boolean bl4 = false;
        String string2 = string.substring(n2);
        Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).substring(startIndex)");
        return string2;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final CharSequence takeLastWhile(@NotNull CharSequence $this$takeLastWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$takeLastWhile = 0;
        Intrinsics.checkParameterIsNotNull($this$takeLastWhile, "$this$takeLastWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = StringsKt.getLastIndex($this$takeLastWhile);
        boolean bl = false;
        while (n >= 0) {
            void index;
            if (!predicate.invoke(Character.valueOf($this$takeLastWhile.charAt((int)index))).booleanValue()) {
                return $this$takeLastWhile.subSequence((int)(index + true), $this$takeLastWhile.length());
            }
            --index;
        }
        return $this$takeLastWhile.subSequence(0, $this$takeLastWhile.length());
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String takeLastWhile(@NotNull String $this$takeLastWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$takeLastWhile = 0;
        Intrinsics.checkParameterIsNotNull($this$takeLastWhile, "$this$takeLastWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = StringsKt.getLastIndex($this$takeLastWhile);
        boolean bl = false;
        while (n >= 0) {
            void index;
            if (!predicate.invoke(Character.valueOf($this$takeLastWhile.charAt((int)index))).booleanValue()) {
                String string = $this$takeLastWhile;
                void var6_6 = index + true;
                boolean bl2 = false;
                String string2 = string.substring((int)var6_6);
                Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).substring(startIndex)");
                return string2;
            }
            --index;
        }
        return $this$takeLastWhile;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final CharSequence takeWhile(@NotNull CharSequence $this$takeWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$takeWhile = 0;
        Intrinsics.checkParameterIsNotNull($this$takeWhile, "$this$takeWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = 0;
        int n2 = $this$takeWhile.length();
        while (n < n2) {
            void index;
            if (!predicate.invoke(Character.valueOf($this$takeWhile.charAt((int)index))).booleanValue()) {
                return $this$takeWhile.subSequence(0, (int)index);
            }
            ++index;
        }
        return $this$takeWhile.subSequence(0, $this$takeWhile.length());
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String takeWhile(@NotNull String $this$takeWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$takeWhile = 0;
        Intrinsics.checkParameterIsNotNull($this$takeWhile, "$this$takeWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int n = 0;
        int n2 = $this$takeWhile.length();
        while (n < n2) {
            void index;
            if (!predicate.invoke(Character.valueOf($this$takeWhile.charAt((int)index))).booleanValue()) {
                String string = $this$takeWhile;
                int n3 = 0;
                boolean bl = false;
                String string2 = string.substring(n3, (int)index);
                Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                return string2;
            }
            ++index;
        }
        return $this$takeWhile;
    }

    @NotNull
    public static final CharSequence reversed(@NotNull CharSequence $this$reversed) {
        Intrinsics.checkParameterIsNotNull($this$reversed, "$this$reversed");
        StringBuilder stringBuilder = new StringBuilder($this$reversed).reverse();
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "StringBuilder(this).reverse()");
        return stringBuilder;
    }

    @InlineOnly
    private static final String reversed(@NotNull String $this$reversed) {
        int $i$f$reversed = 0;
        String string = $this$reversed;
        if (string == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return ((Object)StringsKt.reversed((CharSequence)string)).toString();
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <K, V> Map<K, V> associate(@NotNull CharSequence $this$associate, @NotNull Function1<? super Character, ? extends Pair<? extends K, ? extends V>> transform) {
        void $this$associateTo$iv;
        int $i$f$associate = 0;
        Intrinsics.checkParameterIsNotNull($this$associate, "$this$associate");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int capacity = RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associate.length()), 16);
        CharSequence charSequence = $this$associate;
        Map destination$iv = new LinkedHashMap(capacity);
        boolean $i$f$associateTo = false;
        void var7_7 = $this$associateTo$iv;
        for (int i = 0; i < var7_7.length(); ++i) {
            char element$iv = var7_7.charAt(i);
            Map map = destination$iv;
            Pair<K, V> pair = transform.invoke(Character.valueOf(element$iv));
            boolean bl = false;
            map.put(pair.getFirst(), pair.getSecond());
        }
        return destination$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <K> Map<K, Character> associateBy(@NotNull CharSequence $this$associateBy, @NotNull Function1<? super Character, ? extends K> keySelector) {
        void $this$associateByTo$iv;
        int $i$f$associateBy = 0;
        Intrinsics.checkParameterIsNotNull($this$associateBy, "$this$associateBy");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        int capacity = RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associateBy.length()), 16);
        CharSequence charSequence = $this$associateBy;
        Map destination$iv = new LinkedHashMap(capacity);
        boolean $i$f$associateByTo = false;
        void var7_7 = $this$associateByTo$iv;
        for (int i = 0; i < var7_7.length(); ++i) {
            char element$iv = var7_7.charAt(i);
            destination$iv.put(keySelector.invoke(Character.valueOf(element$iv)), Character.valueOf(element$iv));
        }
        return destination$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <K, V> Map<K, V> associateBy(@NotNull CharSequence $this$associateBy, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        void $this$associateByTo$iv;
        int $i$f$associateBy = 0;
        Intrinsics.checkParameterIsNotNull($this$associateBy, "$this$associateBy");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        int capacity = RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associateBy.length()), 16);
        CharSequence charSequence = $this$associateBy;
        Map destination$iv = new LinkedHashMap(capacity);
        boolean $i$f$associateByTo = false;
        void var8_8 = $this$associateByTo$iv;
        for (int i = 0; i < var8_8.length(); ++i) {
            char element$iv = var8_8.charAt(i);
            destination$iv.put(keySelector.invoke(Character.valueOf(element$iv)), valueTransform.invoke(Character.valueOf(element$iv)));
        }
        return destination$iv;
    }

    @NotNull
    public static final <K, M extends Map<? super K, ? super Character>> M associateByTo(@NotNull CharSequence $this$associateByTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector) {
        int $i$f$associateByTo = 0;
        Intrinsics.checkParameterIsNotNull($this$associateByTo, "$this$associateByTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        CharSequence charSequence = $this$associateByTo;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            destination.put(keySelector.invoke(Character.valueOf(element)), (Character)Character.valueOf(element));
        }
        return destination;
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M associateByTo(@NotNull CharSequence $this$associateByTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        int $i$f$associateByTo = 0;
        Intrinsics.checkParameterIsNotNull($this$associateByTo, "$this$associateByTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        CharSequence charSequence = $this$associateByTo;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            destination.put(keySelector.invoke(Character.valueOf(element)), valueTransform.invoke(Character.valueOf(element)));
        }
        return destination;
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M associateTo(@NotNull CharSequence $this$associateTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends Pair<? extends K, ? extends V>> transform) {
        int $i$f$associateTo = 0;
        Intrinsics.checkParameterIsNotNull($this$associateTo, "$this$associateTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence charSequence = $this$associateTo;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            M m = destination;
            Pair<K, V> pair = transform.invoke(Character.valueOf(element));
            boolean bl = false;
            m.put(pair.getFirst(), pair.getSecond());
        }
        return destination;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <V> Map<Character, V> associateWith(@NotNull CharSequence $this$associateWith, @NotNull Function1<? super Character, ? extends V> valueSelector) {
        int $i$f$associateWith = 0;
        Intrinsics.checkParameterIsNotNull($this$associateWith, "$this$associateWith");
        Intrinsics.checkParameterIsNotNull(valueSelector, "valueSelector");
        LinkedHashMap result = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associateWith.length()), 16));
        CharSequence $this$associateWithTo$iv = $this$associateWith;
        boolean $i$f$associateWithTo = false;
        CharSequence charSequence = $this$associateWithTo$iv;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element$iv = charSequence.charAt(i);
            ((Map)result).put(Character.valueOf(element$iv), valueSelector.invoke(Character.valueOf(element$iv)));
        }
        return result;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <V, M extends Map<? super Character, ? super V>> M associateWithTo(@NotNull CharSequence $this$associateWithTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends V> valueSelector) {
        int $i$f$associateWithTo = 0;
        Intrinsics.checkParameterIsNotNull($this$associateWithTo, "$this$associateWithTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(valueSelector, "valueSelector");
        CharSequence charSequence = $this$associateWithTo;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            destination.put((Character)Character.valueOf(element), valueSelector.invoke(Character.valueOf(element)));
        }
        return destination;
    }

    @NotNull
    public static final <C extends Collection<? super Character>> C toCollection(@NotNull CharSequence $this$toCollection, @NotNull C destination) {
        Intrinsics.checkParameterIsNotNull($this$toCollection, "$this$toCollection");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        CharSequence charSequence = $this$toCollection;
        for (int i = 0; i < charSequence.length(); ++i) {
            char item = charSequence.charAt(i);
            destination.add((Character)Character.valueOf(item));
        }
        return destination;
    }

    @NotNull
    public static final HashSet<Character> toHashSet(@NotNull CharSequence $this$toHashSet) {
        Intrinsics.checkParameterIsNotNull($this$toHashSet, "$this$toHashSet");
        return (HashSet)StringsKt.toCollection($this$toHashSet, (Collection)new HashSet(MapsKt.mapCapacity($this$toHashSet.length())));
    }

    @NotNull
    public static final List<Character> toList(@NotNull CharSequence $this$toList) {
        List<Character> list;
        Intrinsics.checkParameterIsNotNull($this$toList, "$this$toList");
        switch ($this$toList.length()) {
            case 0: {
                list = CollectionsKt.emptyList();
                break;
            }
            case 1: {
                list = CollectionsKt.listOf(Character.valueOf($this$toList.charAt(0)));
                break;
            }
            default: {
                list = StringsKt.toMutableList($this$toList);
            }
        }
        return list;
    }

    @NotNull
    public static final List<Character> toMutableList(@NotNull CharSequence $this$toMutableList) {
        Intrinsics.checkParameterIsNotNull($this$toMutableList, "$this$toMutableList");
        return (List)StringsKt.toCollection($this$toMutableList, (Collection)new ArrayList($this$toMutableList.length()));
    }

    @NotNull
    public static final Set<Character> toSet(@NotNull CharSequence $this$toSet) {
        Set set;
        Intrinsics.checkParameterIsNotNull($this$toSet, "$this$toSet");
        switch ($this$toSet.length()) {
            case 0: {
                set = SetsKt.emptySet();
                break;
            }
            case 1: {
                set = SetsKt.setOf(Character.valueOf($this$toSet.charAt(0)));
                break;
            }
            default: {
                set = (Set)StringsKt.toCollection($this$toSet, (Collection)new LinkedHashSet(MapsKt.mapCapacity($this$toSet.length())));
            }
        }
        return set;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <R> List<R> flatMap(@NotNull CharSequence $this$flatMap, @NotNull Function1<? super Character, ? extends Iterable<? extends R>> transform) {
        void $this$flatMapTo$iv;
        int $i$f$flatMap = 0;
        Intrinsics.checkParameterIsNotNull($this$flatMap, "$this$flatMap");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence charSequence = $this$flatMap;
        Collection destination$iv = new ArrayList();
        boolean $i$f$flatMapTo = false;
        void var6_6 = $this$flatMapTo$iv;
        for (int i = 0; i < var6_6.length(); ++i) {
            char element$iv = var6_6.charAt(i);
            Iterable<? extends R> list$iv = transform.invoke(Character.valueOf(element$iv));
            CollectionsKt.addAll(destination$iv, list$iv);
        }
        return (List)destination$iv;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C flatMapTo(@NotNull CharSequence $this$flatMapTo, @NotNull C destination, @NotNull Function1<? super Character, ? extends Iterable<? extends R>> transform) {
        int $i$f$flatMapTo = 0;
        Intrinsics.checkParameterIsNotNull($this$flatMapTo, "$this$flatMapTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence charSequence = $this$flatMapTo;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            Iterable<? extends R> list = transform.invoke(Character.valueOf(element));
            CollectionsKt.addAll(destination, list);
        }
        return destination;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <K> Map<K, List<Character>> groupBy(@NotNull CharSequence $this$groupBy, @NotNull Function1<? super Character, ? extends K> keySelector) {
        void $this$groupByTo$iv;
        int $i$f$groupBy = 0;
        Intrinsics.checkParameterIsNotNull($this$groupBy, "$this$groupBy");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        CharSequence charSequence = $this$groupBy;
        Map destination$iv = new LinkedHashMap();
        boolean $i$f$groupByTo = false;
        void var6_6 = $this$groupByTo$iv;
        for (int i = 0; i < var6_6.length(); ++i) {
            Object object;
            char element$iv = var6_6.charAt(i);
            K key$iv = keySelector.invoke(Character.valueOf(element$iv));
            Map $this$getOrPut$iv$iv = destination$iv;
            boolean $i$f$getOrPut = false;
            Object value$iv$iv = $this$getOrPut$iv$iv.get(key$iv);
            if (value$iv$iv == null) {
                boolean bl = false;
                ArrayList answer$iv$iv = new ArrayList();
                $this$getOrPut$iv$iv.put(key$iv, answer$iv$iv);
                object = answer$iv$iv;
            } else {
                object = value$iv$iv;
            }
            List list$iv = (List)object;
            list$iv.add(Character.valueOf(element$iv));
        }
        return destination$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <K, V> Map<K, List<V>> groupBy(@NotNull CharSequence $this$groupBy, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        void $this$groupByTo$iv;
        int $i$f$groupBy = 0;
        Intrinsics.checkParameterIsNotNull($this$groupBy, "$this$groupBy");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        CharSequence charSequence = $this$groupBy;
        Map destination$iv = new LinkedHashMap();
        boolean $i$f$groupByTo = false;
        void var7_7 = $this$groupByTo$iv;
        for (int i = 0; i < var7_7.length(); ++i) {
            Object object;
            char element$iv = var7_7.charAt(i);
            K key$iv = keySelector.invoke(Character.valueOf(element$iv));
            Map $this$getOrPut$iv$iv = destination$iv;
            boolean $i$f$getOrPut = false;
            Object value$iv$iv = $this$getOrPut$iv$iv.get(key$iv);
            if (value$iv$iv == null) {
                boolean bl = false;
                ArrayList answer$iv$iv = new ArrayList();
                $this$getOrPut$iv$iv.put(key$iv, answer$iv$iv);
                object = answer$iv$iv;
            } else {
                object = value$iv$iv;
            }
            List list$iv = (List)object;
            list$iv.add(valueTransform.invoke(Character.valueOf(element$iv)));
        }
        return destination$iv;
    }

    @NotNull
    public static final <K, M extends Map<? super K, List<Character>>> M groupByTo(@NotNull CharSequence $this$groupByTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector) {
        int $i$f$groupByTo = 0;
        Intrinsics.checkParameterIsNotNull($this$groupByTo, "$this$groupByTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        CharSequence charSequence = $this$groupByTo;
        for (int i = 0; i < charSequence.length(); ++i) {
            List<Character> list;
            char element = charSequence.charAt(i);
            K key = keySelector.invoke(Character.valueOf(element));
            M $this$getOrPut$iv = destination;
            boolean $i$f$getOrPut = false;
            List<Character> value$iv = $this$getOrPut$iv.get(key);
            if (value$iv == null) {
                boolean bl = false;
                ArrayList<Character> answer$iv = new ArrayList<Character>();
                $this$getOrPut$iv.put(key, answer$iv);
                list = answer$iv;
            } else {
                list = value$iv;
            }
            List<Character> list2 = list;
            list2.add(Character.valueOf(element));
        }
        return destination;
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, List<V>>> M groupByTo(@NotNull CharSequence $this$groupByTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        int $i$f$groupByTo = 0;
        Intrinsics.checkParameterIsNotNull($this$groupByTo, "$this$groupByTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        CharSequence charSequence = $this$groupByTo;
        for (int i = 0; i < charSequence.length(); ++i) {
            List<V> list;
            char element = charSequence.charAt(i);
            K key = keySelector.invoke(Character.valueOf(element));
            M $this$getOrPut$iv = destination;
            boolean $i$f$getOrPut = false;
            List<V> value$iv = $this$getOrPut$iv.get(key);
            if (value$iv == null) {
                boolean bl = false;
                ArrayList<V> answer$iv = new ArrayList<V>();
                $this$getOrPut$iv.put(key, answer$iv);
                list = answer$iv;
            } else {
                list = value$iv;
            }
            List<V> list2 = list;
            list2.add(valueTransform.invoke(Character.valueOf(element)));
        }
        return destination;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <K> Grouping<Character, K> groupingBy(@NotNull CharSequence $this$groupingBy, @NotNull Function1<? super Character, ? extends K> keySelector) {
        int $i$f$groupingBy = 0;
        Intrinsics.checkParameterIsNotNull($this$groupingBy, "$this$groupingBy");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        return new Grouping<Character, K>($this$groupingBy, keySelector){
            final /* synthetic */ CharSequence $this_groupingBy;
            final /* synthetic */ Function1 $keySelector;

            @NotNull
            public Iterator<Character> sourceIterator() {
                return StringsKt.iterator(this.$this_groupingBy);
            }

            public K keyOf(char element) {
                return (K)this.$keySelector.invoke(Character.valueOf(element));
            }
            {
                this.$this_groupingBy = $receiver;
                this.$keySelector = $captured_local_variable$1;
            }
        };
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <R> List<R> map(@NotNull CharSequence $this$map, @NotNull Function1<? super Character, ? extends R> transform) {
        void $this$mapTo$iv;
        int $i$f$map = 0;
        Intrinsics.checkParameterIsNotNull($this$map, "$this$map");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence charSequence = $this$map;
        Collection destination$iv = new ArrayList($this$map.length());
        boolean $i$f$mapTo = false;
        void var6_6 = $this$mapTo$iv;
        for (int i = 0; i < var6_6.length(); ++i) {
            char item$iv = var6_6.charAt(i);
            destination$iv.add(transform.invoke(Character.valueOf(item$iv)));
        }
        return (List)destination$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <R> List<R> mapIndexed(@NotNull CharSequence $this$mapIndexed, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        void $this$mapIndexedTo$iv;
        int $i$f$mapIndexed = 0;
        Intrinsics.checkParameterIsNotNull($this$mapIndexed, "$this$mapIndexed");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence charSequence = $this$mapIndexed;
        Collection destination$iv = new ArrayList($this$mapIndexed.length());
        boolean $i$f$mapIndexedTo = false;
        int index$iv = 0;
        void var7_7 = $this$mapIndexedTo$iv;
        for (int i = 0; i < var7_7.length(); ++i) {
            char item$iv = var7_7.charAt(i);
            Integer n = index$iv;
            ++index$iv;
            destination$iv.add(transform.invoke(n, Character.valueOf(item$iv)));
        }
        return (List)destination$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <R> List<R> mapIndexedNotNull(@NotNull CharSequence $this$mapIndexedNotNull, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        void $this$mapIndexedNotNullTo$iv;
        int $i$f$mapIndexedNotNull = 0;
        Intrinsics.checkParameterIsNotNull($this$mapIndexedNotNull, "$this$mapIndexedNotNull");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence charSequence = $this$mapIndexedNotNull;
        Collection destination$iv = new ArrayList();
        boolean $i$f$mapIndexedNotNullTo = false;
        void $this$forEachIndexed$iv$iv = $this$mapIndexedNotNullTo$iv;
        boolean $i$f$forEachIndexed = false;
        int index$iv$iv = 0;
        void var9_9 = $this$forEachIndexed$iv$iv;
        for (int i = 0; i < var9_9.length(); ++i) {
            R r;
            void element$iv;
            char item$iv$iv = var9_9.charAt(i);
            int n = index$iv$iv++;
            char c = item$iv$iv;
            int index$iv = n;
            boolean bl = false;
            if (transform.invoke(index$iv, Character.valueOf((char)element$iv)) == null) continue;
            boolean bl2 = false;
            boolean bl3 = false;
            R it$iv = r;
            boolean bl4 = false;
            destination$iv.add(it$iv);
        }
        return (List)destination$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <R, C extends Collection<? super R>> C mapIndexedNotNullTo(@NotNull CharSequence $this$mapIndexedNotNullTo, @NotNull C destination, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        int $i$f$mapIndexedNotNullTo = 0;
        Intrinsics.checkParameterIsNotNull($this$mapIndexedNotNullTo, "$this$mapIndexedNotNullTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence $this$forEachIndexed$iv = $this$mapIndexedNotNullTo;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        CharSequence charSequence = $this$forEachIndexed$iv;
        for (int i = 0; i < charSequence.length(); ++i) {
            R r;
            void element;
            char item$iv = charSequence.charAt(i);
            int n = index$iv++;
            char c = item$iv;
            int index = n;
            boolean bl = false;
            if (transform.invoke(index, Character.valueOf((char)element)) == null) continue;
            boolean bl2 = false;
            boolean bl3 = false;
            R it = r;
            boolean bl4 = false;
            destination.add(it);
        }
        return destination;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C mapIndexedTo(@NotNull CharSequence $this$mapIndexedTo, @NotNull C destination, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        int $i$f$mapIndexedTo = 0;
        Intrinsics.checkParameterIsNotNull($this$mapIndexedTo, "$this$mapIndexedTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int index = 0;
        CharSequence charSequence = $this$mapIndexedTo;
        for (int i = 0; i < charSequence.length(); ++i) {
            char item = charSequence.charAt(i);
            Integer n = index;
            ++index;
            destination.add(transform.invoke(n, Character.valueOf(item)));
        }
        return destination;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <R> List<R> mapNotNull(@NotNull CharSequence $this$mapNotNull, @NotNull Function1<? super Character, ? extends R> transform) {
        void $this$mapNotNullTo$iv;
        int $i$f$mapNotNull = 0;
        Intrinsics.checkParameterIsNotNull($this$mapNotNull, "$this$mapNotNull");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence charSequence = $this$mapNotNull;
        Collection destination$iv = new ArrayList();
        boolean $i$f$mapNotNullTo = false;
        void $this$forEach$iv$iv = $this$mapNotNullTo$iv;
        boolean $i$f$forEach = false;
        void var8_8 = $this$forEach$iv$iv;
        for (int i = 0; i < var8_8.length(); ++i) {
            R r;
            char element$iv$iv;
            char element$iv = element$iv$iv = var8_8.charAt(i);
            boolean bl = false;
            if (transform.invoke(Character.valueOf(element$iv)) == null) continue;
            boolean bl2 = false;
            boolean bl3 = false;
            R it$iv = r;
            boolean bl4 = false;
            destination$iv.add(it$iv);
        }
        return (List)destination$iv;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C mapNotNullTo(@NotNull CharSequence $this$mapNotNullTo, @NotNull C destination, @NotNull Function1<? super Character, ? extends R> transform) {
        int $i$f$mapNotNullTo = 0;
        Intrinsics.checkParameterIsNotNull($this$mapNotNullTo, "$this$mapNotNullTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence $this$forEach$iv = $this$mapNotNullTo;
        boolean $i$f$forEach = false;
        CharSequence charSequence = $this$forEach$iv;
        for (int i = 0; i < charSequence.length(); ++i) {
            R r;
            char element$iv;
            char element = element$iv = charSequence.charAt(i);
            boolean bl = false;
            if (transform.invoke(Character.valueOf(element)) == null) continue;
            boolean bl2 = false;
            boolean bl3 = false;
            R it = r;
            boolean bl4 = false;
            destination.add(it);
        }
        return destination;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C mapTo(@NotNull CharSequence $this$mapTo, @NotNull C destination, @NotNull Function1<? super Character, ? extends R> transform) {
        int $i$f$mapTo = 0;
        Intrinsics.checkParameterIsNotNull($this$mapTo, "$this$mapTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence charSequence = $this$mapTo;
        for (int i = 0; i < charSequence.length(); ++i) {
            char item = charSequence.charAt(i);
            destination.add(transform.invoke(Character.valueOf(item)));
        }
        return destination;
    }

    @NotNull
    public static final Iterable<IndexedValue<Character>> withIndex(@NotNull CharSequence $this$withIndex) {
        Intrinsics.checkParameterIsNotNull($this$withIndex, "$this$withIndex");
        return new IndexingIterable((Function0)new Function0<CharIterator>($this$withIndex){
            final /* synthetic */ CharSequence $this_withIndex;

            @NotNull
            public final CharIterator invoke() {
                return StringsKt.iterator(this.$this_withIndex);
            }
            {
                this.$this_withIndex = charSequence;
                super(0);
            }
        });
    }

    public static final boolean all(@NotNull CharSequence $this$all, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$all = 0;
        Intrinsics.checkParameterIsNotNull($this$all, "$this$all");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$all;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) continue;
            return false;
        }
        return true;
    }

    public static final boolean any(@NotNull CharSequence $this$any) {
        Intrinsics.checkParameterIsNotNull($this$any, "$this$any");
        CharSequence charSequence = $this$any;
        boolean bl = false;
        return !(charSequence.length() == 0);
    }

    public static final boolean any(@NotNull CharSequence $this$any, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$any = 0;
        Intrinsics.checkParameterIsNotNull($this$any, "$this$any");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$any;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            if (!predicate.invoke(Character.valueOf(element)).booleanValue()) continue;
            return true;
        }
        return false;
    }

    @InlineOnly
    private static final int count(@NotNull CharSequence $this$count) {
        int $i$f$count = 0;
        return $this$count.length();
    }

    public static final int count(@NotNull CharSequence $this$count, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$count = 0;
        Intrinsics.checkParameterIsNotNull($this$count, "$this$count");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int count = 0;
        CharSequence charSequence = $this$count;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            if (!predicate.invoke(Character.valueOf(element)).booleanValue()) continue;
            ++count;
        }
        return count;
    }

    public static final <R> R fold(@NotNull CharSequence $this$fold, R initial, @NotNull Function2<? super R, ? super Character, ? extends R> operation) {
        int $i$f$fold = 0;
        Intrinsics.checkParameterIsNotNull($this$fold, "$this$fold");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        R accumulator = initial;
        CharSequence charSequence = $this$fold;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            accumulator = operation.invoke(accumulator, Character.valueOf(element));
        }
        return accumulator;
    }

    public static final <R> R foldIndexed(@NotNull CharSequence $this$foldIndexed, R initial, @NotNull Function3<? super Integer, ? super R, ? super Character, ? extends R> operation) {
        int $i$f$foldIndexed = 0;
        Intrinsics.checkParameterIsNotNull($this$foldIndexed, "$this$foldIndexed");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index = 0;
        R accumulator = initial;
        CharSequence charSequence = $this$foldIndexed;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            Integer n = index;
            ++index;
            accumulator = operation.invoke(n, accumulator, Character.valueOf(element));
        }
        return accumulator;
    }

    public static final <R> R foldRight(@NotNull CharSequence $this$foldRight, R initial, @NotNull Function2<? super Character, ? super R, ? extends R> operation) {
        int $i$f$foldRight = 0;
        Intrinsics.checkParameterIsNotNull($this$foldRight, "$this$foldRight");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index = StringsKt.getLastIndex($this$foldRight);
        R accumulator = initial;
        while (index >= 0) {
            accumulator = operation.invoke(Character.valueOf($this$foldRight.charAt(index--)), accumulator);
        }
        return accumulator;
    }

    public static final <R> R foldRightIndexed(@NotNull CharSequence $this$foldRightIndexed, R initial, @NotNull Function3<? super Integer, ? super Character, ? super R, ? extends R> operation) {
        int $i$f$foldRightIndexed = 0;
        Intrinsics.checkParameterIsNotNull($this$foldRightIndexed, "$this$foldRightIndexed");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        R accumulator = initial;
        for (int index = StringsKt.getLastIndex($this$foldRightIndexed); index >= 0; --index) {
            accumulator = operation.invoke(index, Character.valueOf($this$foldRightIndexed.charAt(index)), accumulator);
        }
        return accumulator;
    }

    public static final void forEach(@NotNull CharSequence $this$forEach, @NotNull Function1<? super Character, Unit> action) {
        int $i$f$forEach = 0;
        Intrinsics.checkParameterIsNotNull($this$forEach, "$this$forEach");
        Intrinsics.checkParameterIsNotNull(action, "action");
        CharSequence charSequence = $this$forEach;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            action.invoke(Character.valueOf(element));
        }
    }

    public static final void forEachIndexed(@NotNull CharSequence $this$forEachIndexed, @NotNull Function2<? super Integer, ? super Character, Unit> action) {
        int $i$f$forEachIndexed = 0;
        Intrinsics.checkParameterIsNotNull($this$forEachIndexed, "$this$forEachIndexed");
        Intrinsics.checkParameterIsNotNull(action, "action");
        int index = 0;
        CharSequence charSequence = $this$forEachIndexed;
        for (int i = 0; i < charSequence.length(); ++i) {
            char item = charSequence.charAt(i);
            Integer n = index;
            ++index;
            action.invoke(n, Character.valueOf(item));
        }
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public static final Character max(@NotNull CharSequence $this$max) {
        Intrinsics.checkParameterIsNotNull($this$max, "$this$max");
        CharSequence charSequence = $this$max;
        int n = 0;
        if (charSequence.length() == 0) {
            return null;
        }
        char max = $this$max.charAt(0);
        n = 1;
        int n2 = StringsKt.getLastIndex($this$max);
        if (n <= n2) {
            while (true) {
                void i;
                char e;
                if (max < (e = $this$max.charAt((int)i))) {
                    max = e;
                }
                if (i == n2) break;
                ++i;
            }
        }
        return Character.valueOf(max);
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public static final <R extends Comparable<? super R>> Character maxBy(@NotNull CharSequence $this$maxBy, @NotNull Function1<? super Character, ? extends R> selector) {
        int $i$f$maxBy = 0;
        Intrinsics.checkParameterIsNotNull($this$maxBy, "$this$maxBy");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        CharSequence charSequence = $this$maxBy;
        boolean bl = false;
        if (charSequence.length() == 0) {
            return null;
        }
        char maxElem = $this$maxBy.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$maxBy);
        if (lastIndex == 0) {
            return Character.valueOf(maxElem);
        }
        Comparable maxValue = (Comparable)selector.invoke(Character.valueOf(maxElem));
        int n = 1;
        int n2 = lastIndex;
        if (n <= n2) {
            while (true) {
                void i;
                char e;
                Comparable v;
                if (maxValue.compareTo(v = (Comparable)selector.invoke(Character.valueOf(e = $this$maxBy.charAt((int)i)))) < 0) {
                    maxElem = e;
                    maxValue = v;
                }
                if (i == n2) break;
                ++i;
            }
        }
        return Character.valueOf(maxElem);
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public static final Character maxWith(@NotNull CharSequence $this$maxWith, @NotNull Comparator<? super Character> comparator) {
        Intrinsics.checkParameterIsNotNull($this$maxWith, "$this$maxWith");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        CharSequence charSequence = $this$maxWith;
        int n = 0;
        if (charSequence.length() == 0) {
            return null;
        }
        char max = $this$maxWith.charAt(0);
        n = 1;
        int n2 = StringsKt.getLastIndex($this$maxWith);
        if (n <= n2) {
            while (true) {
                void i;
                char e = $this$maxWith.charAt((int)i);
                if (comparator.compare(Character.valueOf(max), Character.valueOf(e)) < 0) {
                    max = e;
                }
                if (i == n2) break;
                ++i;
            }
        }
        return Character.valueOf(max);
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public static final Character min(@NotNull CharSequence $this$min) {
        Intrinsics.checkParameterIsNotNull($this$min, "$this$min");
        CharSequence charSequence = $this$min;
        int n = 0;
        if (charSequence.length() == 0) {
            return null;
        }
        char min = $this$min.charAt(0);
        n = 1;
        int n2 = StringsKt.getLastIndex($this$min);
        if (n <= n2) {
            while (true) {
                void i;
                char e;
                if (min > (e = $this$min.charAt((int)i))) {
                    min = e;
                }
                if (i == n2) break;
                ++i;
            }
        }
        return Character.valueOf(min);
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public static final <R extends Comparable<? super R>> Character minBy(@NotNull CharSequence $this$minBy, @NotNull Function1<? super Character, ? extends R> selector) {
        int $i$f$minBy = 0;
        Intrinsics.checkParameterIsNotNull($this$minBy, "$this$minBy");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        CharSequence charSequence = $this$minBy;
        boolean bl = false;
        if (charSequence.length() == 0) {
            return null;
        }
        char minElem = $this$minBy.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$minBy);
        if (lastIndex == 0) {
            return Character.valueOf(minElem);
        }
        Comparable minValue = (Comparable)selector.invoke(Character.valueOf(minElem));
        int n = 1;
        int n2 = lastIndex;
        if (n <= n2) {
            while (true) {
                void i;
                char e;
                Comparable v;
                if (minValue.compareTo(v = (Comparable)selector.invoke(Character.valueOf(e = $this$minBy.charAt((int)i)))) > 0) {
                    minElem = e;
                    minValue = v;
                }
                if (i == n2) break;
                ++i;
            }
        }
        return Character.valueOf(minElem);
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public static final Character minWith(@NotNull CharSequence $this$minWith, @NotNull Comparator<? super Character> comparator) {
        Intrinsics.checkParameterIsNotNull($this$minWith, "$this$minWith");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        CharSequence charSequence = $this$minWith;
        int n = 0;
        if (charSequence.length() == 0) {
            return null;
        }
        char min = $this$minWith.charAt(0);
        n = 1;
        int n2 = StringsKt.getLastIndex($this$minWith);
        if (n <= n2) {
            while (true) {
                void i;
                char e = $this$minWith.charAt((int)i);
                if (comparator.compare(Character.valueOf(min), Character.valueOf(e)) > 0) {
                    min = e;
                }
                if (i == n2) break;
                ++i;
            }
        }
        return Character.valueOf(min);
    }

    public static final boolean none(@NotNull CharSequence $this$none) {
        Intrinsics.checkParameterIsNotNull($this$none, "$this$none");
        CharSequence charSequence = $this$none;
        boolean bl = false;
        return charSequence.length() == 0;
    }

    public static final boolean none(@NotNull CharSequence $this$none, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$none = 0;
        Intrinsics.checkParameterIsNotNull($this$none, "$this$none");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$none;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            if (!predicate.invoke(Character.valueOf(element)).booleanValue()) continue;
            return false;
        }
        return true;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <S extends CharSequence> S onEach(@NotNull S $this$onEach, @NotNull Function1<? super Character, Unit> action) {
        int $i$f$onEach = 0;
        Intrinsics.checkParameterIsNotNull($this$onEach, "$this$onEach");
        Intrinsics.checkParameterIsNotNull(action, "action");
        S s = $this$onEach;
        boolean bl = false;
        boolean bl2 = false;
        S $this$apply = s;
        boolean bl3 = false;
        S s2 = $this$apply;
        for (int i = 0; i < s2.length(); ++i) {
            char element = s2.charAt(i);
            action.invoke(Character.valueOf(element));
        }
        return s;
    }

    /*
     * WARNING - void declaration
     */
    public static final char reduce(@NotNull CharSequence $this$reduce, @NotNull Function2<? super Character, ? super Character, Character> operation) {
        int $i$f$reduce = 0;
        Intrinsics.checkParameterIsNotNull($this$reduce, "$this$reduce");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        CharSequence charSequence = $this$reduce;
        int n = 0;
        if (charSequence.length() == 0) {
            throw (Throwable)new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char accumulator = $this$reduce.charAt(0);
        n = 1;
        int n2 = StringsKt.getLastIndex($this$reduce);
        if (n <= n2) {
            while (true) {
                void index;
                accumulator = operation.invoke(Character.valueOf(accumulator), Character.valueOf($this$reduce.charAt((int)index))).charValue();
                if (index == n2) break;
                ++index;
            }
        }
        return accumulator;
    }

    /*
     * WARNING - void declaration
     */
    public static final char reduceIndexed(@NotNull CharSequence $this$reduceIndexed, @NotNull Function3<? super Integer, ? super Character, ? super Character, Character> operation) {
        int $i$f$reduceIndexed = 0;
        Intrinsics.checkParameterIsNotNull($this$reduceIndexed, "$this$reduceIndexed");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        CharSequence charSequence = $this$reduceIndexed;
        int n = 0;
        if (charSequence.length() == 0) {
            throw (Throwable)new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char accumulator = $this$reduceIndexed.charAt(0);
        n = 1;
        int n2 = StringsKt.getLastIndex($this$reduceIndexed);
        if (n <= n2) {
            while (true) {
                void index;
                accumulator = operation.invoke((Integer)((int)index), Character.valueOf(accumulator), Character.valueOf($this$reduceIndexed.charAt((int)index))).charValue();
                if (index == n2) break;
                ++index;
            }
        }
        return accumulator;
    }

    public static final char reduceRight(@NotNull CharSequence $this$reduceRight, @NotNull Function2<? super Character, ? super Character, Character> operation) {
        int $i$f$reduceRight = 0;
        Intrinsics.checkParameterIsNotNull($this$reduceRight, "$this$reduceRight");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index = StringsKt.getLastIndex($this$reduceRight);
        if (index < 0) {
            throw (Throwable)new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char accumulator = $this$reduceRight.charAt(index--);
        while (index >= 0) {
            accumulator = operation.invoke(Character.valueOf($this$reduceRight.charAt(index--)), Character.valueOf(accumulator)).charValue();
        }
        return accumulator;
    }

    public static final char reduceRightIndexed(@NotNull CharSequence $this$reduceRightIndexed, @NotNull Function3<? super Integer, ? super Character, ? super Character, Character> operation) {
        int $i$f$reduceRightIndexed = 0;
        Intrinsics.checkParameterIsNotNull($this$reduceRightIndexed, "$this$reduceRightIndexed");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index = StringsKt.getLastIndex($this$reduceRightIndexed);
        if (index < 0) {
            throw (Throwable)new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char accumulator = $this$reduceRightIndexed.charAt(index--);
        while (index >= 0) {
            accumulator = operation.invoke((Integer)index, Character.valueOf($this$reduceRightIndexed.charAt(index)), Character.valueOf(accumulator)).charValue();
            --index;
        }
        return accumulator;
    }

    public static final int sumBy(@NotNull CharSequence $this$sumBy, @NotNull Function1<? super Character, Integer> selector) {
        int $i$f$sumBy = 0;
        Intrinsics.checkParameterIsNotNull($this$sumBy, "$this$sumBy");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        int sum = 0;
        CharSequence charSequence = $this$sumBy;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            sum += ((Number)selector.invoke(Character.valueOf(element))).intValue();
        }
        return sum;
    }

    public static final double sumByDouble(@NotNull CharSequence $this$sumByDouble, @NotNull Function1<? super Character, Double> selector) {
        int $i$f$sumByDouble = 0;
        Intrinsics.checkParameterIsNotNull($this$sumByDouble, "$this$sumByDouble");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        double sum = 0.0;
        CharSequence charSequence = $this$sumByDouble;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            sum += ((Number)selector.invoke(Character.valueOf(element))).doubleValue();
        }
        return sum;
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final List<String> chunked(@NotNull CharSequence $this$chunked, int size) {
        Intrinsics.checkParameterIsNotNull($this$chunked, "$this$chunked");
        return StringsKt.windowed($this$chunked, size, size, true);
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final <R> List<R> chunked(@NotNull CharSequence $this$chunked, int size, @NotNull Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$chunked, "$this$chunked");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return StringsKt.windowed($this$chunked, size, size, true, transform);
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final Sequence<String> chunkedSequence(@NotNull CharSequence $this$chunkedSequence, int size) {
        Intrinsics.checkParameterIsNotNull($this$chunkedSequence, "$this$chunkedSequence");
        return StringsKt.chunkedSequence($this$chunkedSequence, size, chunkedSequence.1.INSTANCE);
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final <R> Sequence<R> chunkedSequence(@NotNull CharSequence $this$chunkedSequence, int size, @NotNull Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$chunkedSequence, "$this$chunkedSequence");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return StringsKt.windowedSequence($this$chunkedSequence, size, size, true, transform);
    }

    @NotNull
    public static final Pair<CharSequence, CharSequence> partition(@NotNull CharSequence $this$partition, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$partition = 0;
        Intrinsics.checkParameterIsNotNull($this$partition, "$this$partition");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();
        CharSequence charSequence = $this$partition;
        for (int i = 0; i < charSequence.length(); ++i) {
            char element = charSequence.charAt(i);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                first.append(element);
                continue;
            }
            second.append(element);
        }
        return new Pair<CharSequence, CharSequence>(first, second);
    }

    @NotNull
    public static final Pair<String, String> partition(@NotNull String $this$partition, @NotNull Function1<? super Character, Boolean> predicate) {
        int $i$f$partition = 0;
        Intrinsics.checkParameterIsNotNull($this$partition, "$this$partition");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();
        String string = $this$partition;
        int n = string.length();
        for (int i = 0; i < n; ++i) {
            char element = string.charAt(i);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                first.append(element);
                continue;
            }
            second.append(element);
        }
        return new Pair<String, String>(first.toString(), second.toString());
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final List<String> windowed(@NotNull CharSequence $this$windowed, int size, int step, boolean partialWindows) {
        Intrinsics.checkParameterIsNotNull($this$windowed, "$this$windowed");
        return StringsKt.windowed($this$windowed, size, step, partialWindows, windowed.1.INSTANCE);
    }

    public static /* synthetic */ List windowed$default(CharSequence charSequence, int n, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 1;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.windowed(charSequence, n, n2, bl);
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final <R> List<R> windowed(@NotNull CharSequence $this$windowed, int size, int step, boolean partialWindows, @NotNull Function1<? super CharSequence, ? extends R> transform) {
        int n;
        Intrinsics.checkParameterIsNotNull($this$windowed, "$this$windowed");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        SlidingWindowKt.checkWindowSizeStep(size, step);
        int thisSize = $this$windowed.length();
        int resultCapacity = thisSize / step + (thisSize % step == 0 ? 0 : 1);
        ArrayList<R> result = new ArrayList<R>(resultCapacity);
        int index = 0;
        while (0 <= (n = index) && thisSize > n) {
            int n2;
            int end = index + size;
            if (end < 0 || end > thisSize) {
                if (!partialWindows) break;
                n2 = thisSize;
            } else {
                n2 = end;
            }
            int coercedEnd = n2;
            result.add(transform.invoke($this$windowed.subSequence(index, coercedEnd)));
            index += step;
        }
        return result;
    }

    public static /* synthetic */ List windowed$default(CharSequence charSequence, int n, int n2, boolean bl, Function1 function1, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 1;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.windowed(charSequence, n, n2, bl, function1);
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final Sequence<String> windowedSequence(@NotNull CharSequence $this$windowedSequence, int size, int step, boolean partialWindows) {
        Intrinsics.checkParameterIsNotNull($this$windowedSequence, "$this$windowedSequence");
        return StringsKt.windowedSequence($this$windowedSequence, size, step, partialWindows, windowedSequence.1.INSTANCE);
    }

    public static /* synthetic */ Sequence windowedSequence$default(CharSequence charSequence, int n, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 1;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.windowedSequence(charSequence, n, n2, bl);
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final <R> Sequence<R> windowedSequence(@NotNull CharSequence $this$windowedSequence, int size, int step, boolean partialWindows, @NotNull Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$windowedSequence, "$this$windowedSequence");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        SlidingWindowKt.checkWindowSizeStep(size, step);
        IntProgression windows = RangesKt.step(partialWindows ? StringsKt.getIndices($this$windowedSequence) : RangesKt.until(0, $this$windowedSequence.length() - size + 1), step);
        return SequencesKt.map(CollectionsKt.asSequence(windows), new Function1<Integer, R>($this$windowedSequence, size, transform){
            final /* synthetic */ CharSequence $this_windowedSequence;
            final /* synthetic */ int $size;
            final /* synthetic */ Function1 $transform;

            public final R invoke(int index) {
                int end = index + this.$size;
                int coercedEnd = end < 0 || end > this.$this_windowedSequence.length() ? this.$this_windowedSequence.length() : end;
                return this.$transform.invoke(this.$this_windowedSequence.subSequence(index, coercedEnd));
            }
            {
                this.$this_windowedSequence = charSequence;
                this.$size = n;
                this.$transform = function1;
                super(1);
            }
        });
    }

    public static /* synthetic */ Sequence windowedSequence$default(CharSequence charSequence, int n, int n2, boolean bl, Function1 function1, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 1;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.windowedSequence(charSequence, n, n2, bl, function1);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final List<Pair<Character, Character>> zip(@NotNull CharSequence $this$zip, @NotNull CharSequence other) {
        Intrinsics.checkParameterIsNotNull($this$zip, "$this$zip");
        Intrinsics.checkParameterIsNotNull(other, "other");
        CharSequence $this$zip$iv = $this$zip;
        boolean $i$f$zip = false;
        int n = $this$zip$iv.length();
        int n2 = other.length();
        int n3 = 0;
        int length$iv = Math.min(n, n2);
        ArrayList<Pair<Character, Character>> list$iv = new ArrayList<Pair<Character, Character>>(length$iv);
        n2 = 0;
        n3 = length$iv;
        while (n2 < n3) {
            void c2;
            void c1;
            void i$iv;
            char c = other.charAt((int)i$iv);
            char c3 = $this$zip$iv.charAt((int)i$iv);
            ArrayList<Pair<Character, Character>> arrayList = list$iv;
            boolean bl = false;
            Pair<Character, Character> pair = TuplesKt.to(Character.valueOf((char)c1), Character.valueOf((char)c2));
            arrayList.add(pair);
            ++i$iv;
        }
        return list$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <V> List<V> zip(@NotNull CharSequence $this$zip, @NotNull CharSequence other, @NotNull Function2<? super Character, ? super Character, ? extends V> transform) {
        int $i$f$zip = 0;
        Intrinsics.checkParameterIsNotNull($this$zip, "$this$zip");
        Intrinsics.checkParameterIsNotNull(other, "other");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int n = $this$zip.length();
        int n2 = other.length();
        int n3 = 0;
        int length = Math.min(n, n2);
        ArrayList<V> list = new ArrayList<V>(length);
        n2 = 0;
        n3 = length;
        while (n2 < n3) {
            void i;
            list.add(transform.invoke(Character.valueOf($this$zip.charAt((int)i)), Character.valueOf(other.charAt((int)i))));
            ++i;
        }
        return list;
    }

    /*
     * WARNING - void declaration
     */
    @SinceKotlin(version="1.2")
    @NotNull
    public static final List<Pair<Character, Character>> zipWithNext(@NotNull CharSequence $this$zipWithNext) {
        List list;
        Intrinsics.checkParameterIsNotNull($this$zipWithNext, "$this$zipWithNext");
        CharSequence $this$zipWithNext$iv = $this$zipWithNext;
        boolean $i$f$zipWithNext = false;
        int size$iv = $this$zipWithNext$iv.length() - 1;
        if (size$iv < 1) {
            list = CollectionsKt.emptyList();
        } else {
            ArrayList<Pair<Character, Character>> result$iv = new ArrayList<Pair<Character, Character>>(size$iv);
            int n = 0;
            int n2 = size$iv;
            while (n < n2) {
                void b;
                void a;
                void index$iv;
                char c = $this$zipWithNext$iv.charAt((int)(index$iv + true));
                char c2 = $this$zipWithNext$iv.charAt((int)index$iv);
                ArrayList<Pair<Character, Character>> arrayList = result$iv;
                boolean bl = false;
                Pair<Character, Character> pair = TuplesKt.to(Character.valueOf((char)a), Character.valueOf((char)b));
                arrayList.add(pair);
                ++index$iv;
            }
            list = result$iv;
        }
        return list;
    }

    /*
     * WARNING - void declaration
     */
    @SinceKotlin(version="1.2")
    @NotNull
    public static final <R> List<R> zipWithNext(@NotNull CharSequence $this$zipWithNext, @NotNull Function2<? super Character, ? super Character, ? extends R> transform) {
        int $i$f$zipWithNext = 0;
        Intrinsics.checkParameterIsNotNull($this$zipWithNext, "$this$zipWithNext");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int size = $this$zipWithNext.length() - 1;
        if (size < 1) {
            return CollectionsKt.emptyList();
        }
        ArrayList<R> result = new ArrayList<R>(size);
        int n = 0;
        int n2 = size;
        while (n < n2) {
            void index;
            result.add(transform.invoke(Character.valueOf($this$zipWithNext.charAt((int)index)), Character.valueOf($this$zipWithNext.charAt((int)(index + true)))));
            ++index;
        }
        return result;
    }

    @NotNull
    public static final Iterable<Character> asIterable(@NotNull CharSequence $this$asIterable) {
        Intrinsics.checkParameterIsNotNull($this$asIterable, "$this$asIterable");
        if ($this$asIterable instanceof String) {
            CharSequence charSequence = $this$asIterable;
            boolean bl = false;
            if (charSequence.length() == 0) {
                return CollectionsKt.emptyList();
            }
        }
        boolean bl = false;
        return new Iterable<Character>($this$asIterable){
            final /* synthetic */ CharSequence $this_asIterable$inlined;
            {
                this.$this_asIterable$inlined = charSequence;
            }

            @NotNull
            public Iterator<Character> iterator() {
                boolean bl = false;
                return StringsKt.iterator(this.$this_asIterable$inlined);
            }
        };
    }

    @NotNull
    public static final Sequence<Character> asSequence(@NotNull CharSequence $this$asSequence) {
        Intrinsics.checkParameterIsNotNull($this$asSequence, "$this$asSequence");
        if ($this$asSequence instanceof String) {
            CharSequence charSequence = $this$asSequence;
            boolean bl = false;
            if (charSequence.length() == 0) {
                return SequencesKt.emptySequence();
            }
        }
        boolean bl = false;
        return new Sequence<Character>($this$asSequence){
            final /* synthetic */ CharSequence $this_asSequence$inlined;
            {
                this.$this_asSequence$inlined = charSequence;
            }

            @NotNull
            public Iterator<Character> iterator() {
                boolean bl = false;
                return StringsKt.iterator(this.$this_asSequence$inlined);
            }
        };
    }
}

