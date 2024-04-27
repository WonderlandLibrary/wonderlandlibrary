/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.OverloadResolutionByLambdaReturnType;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.internal.HidesMembers;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000\u0082\u0001\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000f\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001aJ\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u00f8\u0001\u0000\u001a$\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001aJ\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u00f8\u0001\u0000\u001a9\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070\n\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004H\u0087\b\u001a6\u0010\u000b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070\f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001a'\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004H\u0087\b\u001aJ\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u00f8\u0001\u0000\u001a[\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013\u001a]\u0010\u0014\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013\u001a\\\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\n0\u0006H\u0086\b\u00f8\u0001\u0000\u001aa\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\f0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b\u0017\u001au\u0010\u0018\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\n0\u0006H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001c\u001aw\u0010\u0018\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\f0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001d\u0010\u001c\u001aJ\u0010\u001e\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010 \u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u001f0\u0006H\u0087\b\u00f8\u0001\u0000\u001aV\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0012\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0086\b\u00f8\u0001\u0000\u001a\\\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0086\b\u00f8\u0001\u0000\u001au\u0010#\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001c\u001ao\u0010$\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192\u001e\u0010\u0012\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001c\u001ah\u0010%\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u001ah\u0010(\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u001a_\u0010)\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*\u001aJ\u0010)\u001a\u00020+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020+0\u0006H\u0087\b\u00f8\u0001\u0000\u001aJ\u0010)\u001a\u00020,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\b\u00f8\u0001\u0000\u001aa\u0010-\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*\u001aQ\u0010-\u001a\u0004\u0018\u00010+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020+0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010.\u001aQ\u0010-\u001a\u0004\u0018\u00010,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010/\u001aq\u00100\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00101\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001002j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`32\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00104\u001as\u00105\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00101\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001002j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`32\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00104\u001ai\u00106\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00101\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000702j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`3H\u0087\b\u001ai\u00107\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00101\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000702j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`3H\u0087\b\u001ah\u00108\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u001ah\u00109\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u001a_\u0010:\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*\u001aJ\u0010:\u001a\u00020+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020+0\u0006H\u0087\b\u00f8\u0001\u0000\u001aJ\u0010:\u001a\u00020,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\b\u00f8\u0001\u0000\u001aa\u0010;\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*\u001aQ\u0010;\u001a\u0004\u0018\u00010+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020+0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010.\u001aQ\u0010;\u001a\u0004\u0018\u00010,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010/\u001aq\u0010<\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00101\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001002j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`32\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00104\u001as\u0010=\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00101\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001002j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`32\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00104\u001ah\u0010>\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00101\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000702j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`3H\u0007\u001ai\u0010?\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00101\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000702j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`3H\u0087\b\u001a$\u0010@\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001aJ\u0010@\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u00f8\u0001\u0000\u001aY\u0010A\u001a\u0002HB\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0016\b\u0002\u0010B*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004*\u0002HB2\u001e\u0010 \u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u001f0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010C\u001an\u0010D\u001a\u0002HB\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0016\b\u0002\u0010B*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004*\u0002HB23\u0010 \u001a/\u0012\u0013\u0012\u00110\u000e\u00a2\u0006\f\bF\u0012\b\bG\u0012\u0004\b\b(H\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u001f0EH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010I\u001a6\u0010J\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030K0\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006L"}, d2={"all", "", "K", "V", "", "predicate", "Lkotlin/Function1;", "", "any", "asIterable", "", "asSequence", "Lkotlin/sequences/Sequence;", "count", "", "firstNotNullOf", "R", "", "transform", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "firstNotNullOfOrNull", "flatMap", "", "flatMapSequence", "flatMapTo", "C", "", "destination", "(Ljava/util/Map;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;)Ljava/util/Collection;", "flatMapSequenceTo", "forEach", "", "action", "map", "mapNotNull", "mapNotNullTo", "mapTo", "maxBy", "", "selector", "maxByOrNull", "maxOf", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Comparable;", "", "", "maxOfOrNull", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Double;", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Float;", "maxOfWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/util/Map;Ljava/util/Comparator;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "maxOfWithOrNull", "maxWith", "maxWithOrNull", "minBy", "minByOrNull", "minOf", "minOfOrNull", "minOfWith", "minOfWithOrNull", "minWith", "minWithOrNull", "none", "onEach", "M", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "onEachIndexed", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "index", "(Ljava/util/Map;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "toList", "Lkotlin/Pair;", "kotlin-stdlib"}, xs="kotlin/collections/MapsKt")
class MapsKt___MapsKt
extends MapsKt__MapsKt {
    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final <K, V, R> R firstNotNullOf(Map<? extends K, ? extends V> $this$firstNotNullOf, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        R r;
        block2: {
            R r2;
            Intrinsics.checkNotNullParameter($this$firstNotNullOf, "<this>");
            Intrinsics.checkNotNullParameter(transform, "transform");
            Map<K, V> map = $this$firstNotNullOf;
            for (Map.Entry<K, V> entry : map.entrySet()) {
                R r3 = transform.invoke(entry);
                if (r3 == null) continue;
                r2 = r3;
                break block2;
            }
            r2 = r = null;
        }
        if (r == null) {
            throw new NoSuchElementException("No element of the map was transformed to a non-null value.");
        }
        return r;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final <K, V, R> R firstNotNullOfOrNull(Map<? extends K, ? extends V> $this$firstNotNullOfOrNull, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        Intrinsics.checkNotNullParameter($this$firstNotNullOfOrNull, "<this>");
        Intrinsics.checkNotNullParameter(transform, "transform");
        for (Map.Entry<K, V> entry : $this$firstNotNullOfOrNull.entrySet()) {
            R result = transform.invoke(entry);
            if (result == null) continue;
            return result;
        }
        return null;
    }

    @NotNull
    public static final <K, V> List<Pair<K, V>> toList(@NotNull Map<? extends K, ? extends V> $this$toList) {
        Intrinsics.checkNotNullParameter($this$toList, "<this>");
        if ($this$toList.size() == 0) {
            return CollectionsKt.emptyList();
        }
        Iterator<Map.Entry<K, V>> iterator2 = $this$toList.entrySet().iterator();
        if (!iterator2.hasNext()) {
            return CollectionsKt.emptyList();
        }
        Map.Entry<K, V> first = iterator2.next();
        if (!iterator2.hasNext()) {
            Map.Entry<K, V> entry = first;
            return CollectionsKt.listOf(new Pair<K, V>(entry.getKey(), entry.getValue()));
        }
        ArrayList<Pair<K, V>> result = new ArrayList<Pair<K, V>>($this$toList.size());
        Map.Entry<K, V> entry = first;
        result.add(new Pair<K, V>(entry.getKey(), entry.getValue()));
        do {
            entry = iterator2.next();
            result.add(new Pair<K, V>(entry.getKey(), entry.getValue()));
        } while (iterator2.hasNext());
        return result;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <K, V, R> List<R> flatMap(@NotNull Map<? extends K, ? extends V> $this$flatMap, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Iterable<? extends R>> transform) {
        void $this$flatMapTo$iv;
        Intrinsics.checkNotNullParameter($this$flatMap, "<this>");
        Intrinsics.checkNotNullParameter(transform, "transform");
        boolean $i$f$flatMap = false;
        Map<? extends K, ? extends V> map = $this$flatMap;
        Collection destination$iv = new ArrayList();
        boolean $i$f$flatMapTo = false;
        for (Map.Entry element$iv : $this$flatMapTo$iv.entrySet()) {
            Iterable<? extends R> list$iv = transform.invoke(element$iv);
            CollectionsKt.addAll(destination$iv, list$iv);
        }
        return (List)destination$iv;
    }

    /*
     * WARNING - void declaration
     */
    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="flatMapSequence")
    @NotNull
    public static final <K, V, R> List<R> flatMapSequence(@NotNull Map<? extends K, ? extends V> $this$flatMap, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Sequence<? extends R>> transform) {
        void $this$flatMapTo$iv;
        Intrinsics.checkNotNullParameter($this$flatMap, "<this>");
        Intrinsics.checkNotNullParameter(transform, "transform");
        boolean $i$f$flatMapSequence = false;
        Map<? extends K, ? extends V> map = $this$flatMap;
        Collection destination$iv = new ArrayList();
        boolean $i$f$flatMapSequenceTo = false;
        for (Map.Entry element$iv : $this$flatMapTo$iv.entrySet()) {
            Sequence<? extends R> list$iv = transform.invoke(element$iv);
            CollectionsKt.addAll(destination$iv, list$iv);
        }
        return (List)destination$iv;
    }

    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C flatMapTo(@NotNull Map<? extends K, ? extends V> $this$flatMapTo, @NotNull C destination, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Iterable<? extends R>> transform) {
        Intrinsics.checkNotNullParameter($this$flatMapTo, "<this>");
        Intrinsics.checkNotNullParameter(destination, "destination");
        Intrinsics.checkNotNullParameter(transform, "transform");
        boolean $i$f$flatMapTo = false;
        for (Map.Entry<K, V> entry : $this$flatMapTo.entrySet()) {
            Iterable<R> list = transform.invoke(entry);
            CollectionsKt.addAll(destination, list);
        }
        return destination;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="flatMapSequenceTo")
    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C flatMapSequenceTo(@NotNull Map<? extends K, ? extends V> $this$flatMapTo, @NotNull C destination, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Sequence<? extends R>> transform) {
        Intrinsics.checkNotNullParameter($this$flatMapTo, "<this>");
        Intrinsics.checkNotNullParameter(destination, "destination");
        Intrinsics.checkNotNullParameter(transform, "transform");
        boolean $i$f$flatMapSequenceTo = false;
        for (Map.Entry<K, V> entry : $this$flatMapTo.entrySet()) {
            Sequence<R> list = transform.invoke(entry);
            CollectionsKt.addAll(destination, list);
        }
        return destination;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <K, V, R> List<R> map(@NotNull Map<? extends K, ? extends V> $this$map, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        void $this$mapTo$iv;
        Intrinsics.checkNotNullParameter($this$map, "<this>");
        Intrinsics.checkNotNullParameter(transform, "transform");
        boolean $i$f$map = false;
        Map<K, V> map = $this$map;
        Collection destination$iv = new ArrayList($this$map.size());
        boolean $i$f$mapTo = false;
        for (Map.Entry item$iv : $this$mapTo$iv.entrySet()) {
            destination$iv.add(transform.invoke(item$iv));
        }
        return (List)destination$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <K, V, R> List<R> mapNotNull(@NotNull Map<? extends K, ? extends V> $this$mapNotNull, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        void $this$mapNotNullTo$iv;
        Intrinsics.checkNotNullParameter($this$mapNotNull, "<this>");
        Intrinsics.checkNotNullParameter(transform, "transform");
        boolean $i$f$mapNotNull = false;
        Map<? extends K, ? extends V> map = $this$mapNotNull;
        Collection destination$iv = new ArrayList();
        boolean $i$f$mapNotNullTo = false;
        void $this$forEach$iv$iv = $this$mapNotNullTo$iv;
        boolean $i$f$forEach = false;
        Iterator iterator2 = $this$forEach$iv$iv.entrySet().iterator();
        while (iterator2.hasNext()) {
            R r;
            Map.Entry element$iv$iv;
            Map.Entry element$iv = element$iv$iv = iterator2.next();
            boolean bl = false;
            R r2 = transform.invoke(element$iv);
            if (r2 == null) continue;
            R it$iv = r = r2;
            boolean bl2 = false;
            destination$iv.add(it$iv);
        }
        return (List)destination$iv;
    }

    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C mapNotNullTo(@NotNull Map<? extends K, ? extends V> $this$mapNotNullTo, @NotNull C destination, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        Intrinsics.checkNotNullParameter($this$mapNotNullTo, "<this>");
        Intrinsics.checkNotNullParameter(destination, "destination");
        Intrinsics.checkNotNullParameter(transform, "transform");
        boolean $i$f$mapNotNullTo = false;
        Map<K, V> $this$forEach$iv = $this$mapNotNullTo;
        boolean $i$f$forEach = false;
        Iterator<Map.Entry<K, V>> iterator2 = $this$forEach$iv.entrySet().iterator();
        while (iterator2.hasNext()) {
            R r;
            Map.Entry<? extends K, ? extends V> element$iv;
            Map.Entry<? extends K, ? extends V> element = element$iv = iterator2.next();
            boolean bl = false;
            R r2 = transform.invoke(element);
            if (r2 == null) continue;
            R it = r = r2;
            boolean bl2 = false;
            destination.add(it);
        }
        return destination;
    }

    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C mapTo(@NotNull Map<? extends K, ? extends V> $this$mapTo, @NotNull C destination, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        Intrinsics.checkNotNullParameter($this$mapTo, "<this>");
        Intrinsics.checkNotNullParameter(destination, "destination");
        Intrinsics.checkNotNullParameter(transform, "transform");
        boolean $i$f$mapTo = false;
        for (Map.Entry<K, V> entry : $this$mapTo.entrySet()) {
            destination.add(transform.invoke(entry));
        }
        return destination;
    }

    public static final <K, V> boolean all(@NotNull Map<? extends K, ? extends V> $this$all, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$all, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        boolean $i$f$all = false;
        if ($this$all.isEmpty()) {
            return true;
        }
        for (Map.Entry<K, V> entry : $this$all.entrySet()) {
            if (predicate.invoke(entry).booleanValue()) continue;
            return false;
        }
        return true;
    }

    public static final <K, V> boolean any(@NotNull Map<? extends K, ? extends V> $this$any) {
        Intrinsics.checkNotNullParameter($this$any, "<this>");
        return !$this$any.isEmpty();
    }

    public static final <K, V> boolean any(@NotNull Map<? extends K, ? extends V> $this$any, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$any, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        boolean $i$f$any = false;
        if ($this$any.isEmpty()) {
            return false;
        }
        for (Map.Entry<K, V> entry : $this$any.entrySet()) {
            if (!predicate.invoke(entry).booleanValue()) continue;
            return true;
        }
        return false;
    }

    @InlineOnly
    private static final <K, V> int count(Map<? extends K, ? extends V> $this$count) {
        Intrinsics.checkNotNullParameter($this$count, "<this>");
        return $this$count.size();
    }

    public static final <K, V> int count(@NotNull Map<? extends K, ? extends V> $this$count, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$count, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        boolean $i$f$count = false;
        if ($this$count.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Map.Entry<K, V> entry : $this$count.entrySet()) {
            if (!predicate.invoke(entry).booleanValue()) continue;
            ++count;
        }
        return count;
    }

    @HidesMembers
    public static final <K, V> void forEach(@NotNull Map<? extends K, ? extends V> $this$forEach, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Unit> action) {
        Intrinsics.checkNotNullParameter($this$forEach, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        boolean $i$f$forEach = false;
        for (Map.Entry<K, V> entry : $this$forEach.entrySet()) {
            action.invoke(entry);
        }
    }

    @Deprecated(message="Use maxByOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @InlineOnly
    private static final /* synthetic */ <K, V, R extends Comparable<? super R>> Map.Entry<K, V> maxBy(Map<? extends K, ? extends V> $this$maxBy, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Object v0;
        Intrinsics.checkNotNullParameter($this$maxBy, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Map<K, V> map = $this$maxBy;
        Iterable iterable = map.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            v0 = null;
        } else {
            Object t = iterator2.next();
            if (!iterator2.hasNext()) {
                v0 = t;
            } else {
                Comparable comparable = (Comparable)selector.invoke((Map.Entry<K, V>)t);
                do {
                    Object t2;
                    Comparable comparable2;
                    if (comparable.compareTo(comparable2 = (Comparable)selector.invoke((Map.Entry<K, V>)(t2 = iterator2.next()))) >= 0) continue;
                    t = t2;
                    comparable = comparable2;
                } while (iterator2.hasNext());
                v0 = t;
            }
        }
        return v0;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> Map.Entry<K, V> maxByOrNull(Map<? extends K, ? extends V> $this$maxByOrNull, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Object v0;
        Intrinsics.checkNotNullParameter($this$maxByOrNull, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable $this$maxByOrNull$iv = $this$maxByOrNull.entrySet();
        boolean $i$f$maxByOrNull = false;
        Iterator iterator$iv = $this$maxByOrNull$iv.iterator();
        if (!iterator$iv.hasNext()) {
            v0 = null;
        } else {
            Object maxElem$iv = iterator$iv.next();
            if (!iterator$iv.hasNext()) {
                v0 = maxElem$iv;
            } else {
                Comparable maxValue$iv = (Comparable)selector.invoke((Map.Entry<K, V>)maxElem$iv);
                do {
                    Object e$iv;
                    Comparable v$iv;
                    if (maxValue$iv.compareTo(v$iv = (Comparable)selector.invoke((Map.Entry<K, V>)(e$iv = iterator$iv.next()))) >= 0) continue;
                    maxElem$iv = e$iv;
                    maxValue$iv = v$iv;
                } while (iterator$iv.hasNext());
                v0 = maxElem$iv;
            }
        }
        return v0;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> double maxOf(Map<? extends K, ? extends V> $this$maxOf, Function1<? super Map.Entry<? extends K, ? extends V>, Double> selector) {
        Intrinsics.checkNotNullParameter($this$maxOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$maxOf.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            throw new NoSuchElementException();
        }
        double d = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).doubleValue();
        while (iterator2.hasNext()) {
            double d2 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).doubleValue();
            d = Math.max(d, d2);
        }
        return d;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> float maxOf(Map<? extends K, ? extends V> $this$maxOf, Function1<? super Map.Entry<? extends K, ? extends V>, Float> selector) {
        Intrinsics.checkNotNullParameter($this$maxOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$maxOf.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            throw new NoSuchElementException();
        }
        float f = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).floatValue();
        while (iterator2.hasNext()) {
            float f2 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).floatValue();
            f = Math.max(f, f2);
        }
        return f;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> R maxOf(Map<? extends K, ? extends V> $this$maxOf, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$maxOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$maxOf.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            throw new NoSuchElementException();
        }
        Comparable comparable = (Comparable)selector.invoke((Map.Entry<K, V>)iterator2.next());
        while (iterator2.hasNext()) {
            Comparable comparable2 = (Comparable)selector.invoke((Map.Entry<K, V>)iterator2.next());
            if (comparable.compareTo(comparable2) >= 0) continue;
            comparable = comparable2;
        }
        return (R)comparable;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> Double maxOfOrNull(Map<? extends K, ? extends V> $this$maxOfOrNull, Function1<? super Map.Entry<? extends K, ? extends V>, Double> selector) {
        Double d;
        Intrinsics.checkNotNullParameter($this$maxOfOrNull, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$maxOfOrNull.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            d = null;
        } else {
            double d2 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).doubleValue();
            while (iterator2.hasNext()) {
                double d3 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).doubleValue();
                d2 = Math.max(d2, d3);
            }
            d = d2;
        }
        return d;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> Float maxOfOrNull(Map<? extends K, ? extends V> $this$maxOfOrNull, Function1<? super Map.Entry<? extends K, ? extends V>, Float> selector) {
        Float f;
        Intrinsics.checkNotNullParameter($this$maxOfOrNull, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$maxOfOrNull.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            f = null;
        } else {
            float f2 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).floatValue();
            while (iterator2.hasNext()) {
                float f3 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).floatValue();
                f2 = Math.max(f2, f3);
            }
            f = Float.valueOf(f2);
        }
        return f;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> R maxOfOrNull(Map<? extends K, ? extends V> $this$maxOfOrNull, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Comparable comparable;
        Intrinsics.checkNotNullParameter($this$maxOfOrNull, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$maxOfOrNull.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            comparable = null;
        } else {
            Comparable comparable2 = (Comparable)selector.invoke((Map.Entry<K, V>)iterator2.next());
            while (iterator2.hasNext()) {
                Comparable comparable3 = (Comparable)selector.invoke((Map.Entry<K, V>)iterator2.next());
                if (comparable2.compareTo(comparable3) >= 0) continue;
                comparable2 = comparable3;
            }
            comparable = comparable2;
        }
        return (R)comparable;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R> R maxOfWith(Map<? extends K, ? extends V> $this$maxOfWith, Comparator<? super R> comparator, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$maxOfWith, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$maxOfWith.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            throw new NoSuchElementException();
        }
        R r = selector.invoke((Map.Entry<K, V>)iterator2.next());
        while (iterator2.hasNext()) {
            R r2 = selector.invoke((Map.Entry<K, V>)iterator2.next());
            if (comparator.compare(r, r2) >= 0) continue;
            r = r2;
        }
        return r;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R> R maxOfWithOrNull(Map<? extends K, ? extends V> $this$maxOfWithOrNull, Comparator<? super R> comparator, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        R r;
        Intrinsics.checkNotNullParameter($this$maxOfWithOrNull, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$maxOfWithOrNull.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            r = null;
        } else {
            R r2 = selector.invoke((Map.Entry<K, V>)iterator2.next());
            while (iterator2.hasNext()) {
                R r3 = selector.invoke((Map.Entry<K, V>)iterator2.next());
                if (comparator.compare(r2, r3) >= 0) continue;
                r2 = r3;
            }
            r = r2;
        }
        return r;
    }

    @Deprecated(message="Use maxWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @InlineOnly
    private static final /* synthetic */ <K, V> Map.Entry<K, V> maxWith(Map<? extends K, ? extends V> $this$maxWith, Comparator<? super Map.Entry<? extends K, ? extends V>> comparator) {
        Intrinsics.checkNotNullParameter($this$maxWith, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        Map<K, V> map = $this$maxWith;
        return CollectionsKt.maxWithOrNull((Iterable)map.entrySet(), comparator);
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final <K, V> Map.Entry<K, V> maxWithOrNull(Map<? extends K, ? extends V> $this$maxWithOrNull, Comparator<? super Map.Entry<? extends K, ? extends V>> comparator) {
        Intrinsics.checkNotNullParameter($this$maxWithOrNull, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return CollectionsKt.maxWithOrNull((Iterable)$this$maxWithOrNull.entrySet(), comparator);
    }

    @Deprecated(message="Use minByOrNull instead.", replaceWith=@ReplaceWith(expression="this.minByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ <K, V, R extends Comparable<? super R>> Map.Entry<K, V> minBy(Map<? extends K, ? extends V> $this$minBy, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Object v0;
        Intrinsics.checkNotNullParameter($this$minBy, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        boolean $i$f$minBy = false;
        Map<K, V> map = $this$minBy;
        Iterable iterable = map.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            v0 = null;
        } else {
            Object t = iterator2.next();
            if (!iterator2.hasNext()) {
                v0 = t;
            } else {
                Comparable comparable = (Comparable)selector.invoke((Map.Entry<K, V>)t);
                do {
                    Object t2;
                    Comparable comparable2;
                    if (comparable.compareTo(comparable2 = (Comparable)selector.invoke((Map.Entry<K, V>)(t2 = iterator2.next()))) <= 0) continue;
                    t = t2;
                    comparable = comparable2;
                } while (iterator2.hasNext());
                v0 = t;
            }
        }
        return v0;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> Map.Entry<K, V> minByOrNull(Map<? extends K, ? extends V> $this$minByOrNull, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Object v0;
        Intrinsics.checkNotNullParameter($this$minByOrNull, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable $this$minByOrNull$iv = $this$minByOrNull.entrySet();
        boolean $i$f$minByOrNull = false;
        Iterator iterator$iv = $this$minByOrNull$iv.iterator();
        if (!iterator$iv.hasNext()) {
            v0 = null;
        } else {
            Object minElem$iv = iterator$iv.next();
            if (!iterator$iv.hasNext()) {
                v0 = minElem$iv;
            } else {
                Comparable minValue$iv = (Comparable)selector.invoke((Map.Entry<K, V>)minElem$iv);
                do {
                    Object e$iv;
                    Comparable v$iv;
                    if (minValue$iv.compareTo(v$iv = (Comparable)selector.invoke((Map.Entry<K, V>)(e$iv = iterator$iv.next()))) <= 0) continue;
                    minElem$iv = e$iv;
                    minValue$iv = v$iv;
                } while (iterator$iv.hasNext());
                v0 = minElem$iv;
            }
        }
        return v0;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> double minOf(Map<? extends K, ? extends V> $this$minOf, Function1<? super Map.Entry<? extends K, ? extends V>, Double> selector) {
        Intrinsics.checkNotNullParameter($this$minOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$minOf.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            throw new NoSuchElementException();
        }
        double d = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).doubleValue();
        while (iterator2.hasNext()) {
            double d2 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).doubleValue();
            d = Math.min(d, d2);
        }
        return d;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> float minOf(Map<? extends K, ? extends V> $this$minOf, Function1<? super Map.Entry<? extends K, ? extends V>, Float> selector) {
        Intrinsics.checkNotNullParameter($this$minOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$minOf.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            throw new NoSuchElementException();
        }
        float f = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).floatValue();
        while (iterator2.hasNext()) {
            float f2 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).floatValue();
            f = Math.min(f, f2);
        }
        return f;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> R minOf(Map<? extends K, ? extends V> $this$minOf, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$minOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$minOf.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            throw new NoSuchElementException();
        }
        Comparable comparable = (Comparable)selector.invoke((Map.Entry<K, V>)iterator2.next());
        while (iterator2.hasNext()) {
            Comparable comparable2 = (Comparable)selector.invoke((Map.Entry<K, V>)iterator2.next());
            if (comparable.compareTo(comparable2) <= 0) continue;
            comparable = comparable2;
        }
        return (R)comparable;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> Double minOfOrNull(Map<? extends K, ? extends V> $this$minOfOrNull, Function1<? super Map.Entry<? extends K, ? extends V>, Double> selector) {
        Double d;
        Intrinsics.checkNotNullParameter($this$minOfOrNull, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$minOfOrNull.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            d = null;
        } else {
            double d2 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).doubleValue();
            while (iterator2.hasNext()) {
                double d3 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).doubleValue();
                d2 = Math.min(d2, d3);
            }
            d = d2;
        }
        return d;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> Float minOfOrNull(Map<? extends K, ? extends V> $this$minOfOrNull, Function1<? super Map.Entry<? extends K, ? extends V>, Float> selector) {
        Float f;
        Intrinsics.checkNotNullParameter($this$minOfOrNull, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$minOfOrNull.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            f = null;
        } else {
            float f2 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).floatValue();
            while (iterator2.hasNext()) {
                float f3 = ((Number)selector.invoke((Map.Entry<K, V>)iterator2.next())).floatValue();
                f2 = Math.min(f2, f3);
            }
            f = Float.valueOf(f2);
        }
        return f;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> R minOfOrNull(Map<? extends K, ? extends V> $this$minOfOrNull, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Comparable comparable;
        Intrinsics.checkNotNullParameter($this$minOfOrNull, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$minOfOrNull.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            comparable = null;
        } else {
            Comparable comparable2 = (Comparable)selector.invoke((Map.Entry<K, V>)iterator2.next());
            while (iterator2.hasNext()) {
                Comparable comparable3 = (Comparable)selector.invoke((Map.Entry<K, V>)iterator2.next());
                if (comparable2.compareTo(comparable3) <= 0) continue;
                comparable2 = comparable3;
            }
            comparable = comparable2;
        }
        return (R)comparable;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R> R minOfWith(Map<? extends K, ? extends V> $this$minOfWith, Comparator<? super R> comparator, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$minOfWith, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$minOfWith.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            throw new NoSuchElementException();
        }
        R r = selector.invoke((Map.Entry<K, V>)iterator2.next());
        while (iterator2.hasNext()) {
            R r2 = selector.invoke((Map.Entry<K, V>)iterator2.next());
            if (comparator.compare(r, r2) <= 0) continue;
            r = r2;
        }
        return r;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R> R minOfWithOrNull(Map<? extends K, ? extends V> $this$minOfWithOrNull, Comparator<? super R> comparator, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        R r;
        Intrinsics.checkNotNullParameter($this$minOfWithOrNull, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterable iterable = $this$minOfWithOrNull.entrySet();
        Iterator iterator2 = iterable.iterator();
        if (!iterator2.hasNext()) {
            r = null;
        } else {
            R r2 = selector.invoke((Map.Entry<K, V>)iterator2.next());
            while (iterator2.hasNext()) {
                R r3 = selector.invoke((Map.Entry<K, V>)iterator2.next());
                if (comparator.compare(r2, r3) <= 0) continue;
                r2 = r3;
            }
            r = r2;
        }
        return r;
    }

    @Deprecated(message="Use minWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.minWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ Map.Entry minWith(Map $this$minWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$minWith, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        Map map = $this$minWith;
        return (Map.Entry)CollectionsKt.minWithOrNull(map.entrySet(), comparator);
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final <K, V> Map.Entry<K, V> minWithOrNull(Map<? extends K, ? extends V> $this$minWithOrNull, Comparator<? super Map.Entry<? extends K, ? extends V>> comparator) {
        Intrinsics.checkNotNullParameter($this$minWithOrNull, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return CollectionsKt.minWithOrNull((Iterable)$this$minWithOrNull.entrySet(), comparator);
    }

    public static final <K, V> boolean none(@NotNull Map<? extends K, ? extends V> $this$none) {
        Intrinsics.checkNotNullParameter($this$none, "<this>");
        return $this$none.isEmpty();
    }

    public static final <K, V> boolean none(@NotNull Map<? extends K, ? extends V> $this$none, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$none, "<this>");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        boolean $i$f$none = false;
        if ($this$none.isEmpty()) {
            return true;
        }
        for (Map.Entry<K, V> entry : $this$none.entrySet()) {
            if (!predicate.invoke(entry).booleanValue()) continue;
            return false;
        }
        return true;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <K, V, M extends Map<? extends K, ? extends V>> M onEach(@NotNull M $this$onEach, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Unit> action) {
        M m;
        Intrinsics.checkNotNullParameter($this$onEach, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        boolean $i$f$onEach = false;
        M $this$onEach_u24lambda_u2d2 = m = $this$onEach;
        boolean bl = false;
        for (Map.Entry<? extends K, ? extends V> entry : $this$onEach_u24lambda_u2d2.entrySet()) {
            action.invoke(entry);
        }
        return m;
    }

    @SinceKotlin(version="1.4")
    @NotNull
    public static final <K, V, M extends Map<? extends K, ? extends V>> M onEachIndexed(@NotNull M $this$onEachIndexed, @NotNull Function2<? super Integer, ? super Map.Entry<? extends K, ? extends V>, Unit> action) {
        M m;
        Intrinsics.checkNotNullParameter($this$onEachIndexed, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        boolean $i$f$onEachIndexed = false;
        M $this$onEachIndexed_u24lambda_u2d3 = m = $this$onEachIndexed;
        boolean bl = false;
        Iterable $this$forEachIndexed$iv = $this$onEachIndexed_u24lambda_u2d3.entrySet();
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            int n = index$iv;
            index$iv = n + 1;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            action.invoke((Integer)((Integer)Integer.valueOf(n)), (Map.Entry<K, V>)item$iv);
        }
        return m;
    }

    @InlineOnly
    private static final <K, V> Iterable<Map.Entry<K, V>> asIterable(Map<? extends K, ? extends V> $this$asIterable) {
        Intrinsics.checkNotNullParameter($this$asIterable, "<this>");
        return $this$asIterable.entrySet();
    }

    @NotNull
    public static final <K, V> Sequence<Map.Entry<K, V>> asSequence(@NotNull Map<? extends K, ? extends V> $this$asSequence) {
        Intrinsics.checkNotNullParameter($this$asSequence, "<this>");
        return CollectionsKt.asSequence((Iterable)$this$asSequence.entrySet());
    }
}

