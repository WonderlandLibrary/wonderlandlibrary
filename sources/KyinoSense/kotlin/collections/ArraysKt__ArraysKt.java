/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShortArray;
import kotlin.collections.ArraysKt;
import kotlin.collections.ArraysKt__ArraysJVMKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.unsigned.UArraysKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000H\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a1\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0001\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001a!\u0010\u0007\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0001\u00a2\u0006\u0004\b\t\u0010\n\u001a?\u0010\u000b\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\n\u0010\r\u001a\u00060\u000ej\u0002`\u000f2\u0010\u0010\u0010\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00030\u0011H\u0002\u00a2\u0006\u0004\b\u0012\u0010\u0013\u001a+\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0015\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00030\u0003\u00a2\u0006\u0002\u0010\u0016\u001a8\u0010\u0017\u001a\u0002H\u0018\"\u0010\b\u0000\u0010\u0019*\u0006\u0012\u0002\b\u00030\u0003*\u0002H\u0018\"\u0004\b\u0001\u0010\u0018*\u0002H\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00180\u001bH\u0087\b\u00a2\u0006\u0002\u0010\u001c\u001a)\u0010\u001d\u001a\u00020\u0001*\b\u0012\u0002\b\u0003\u0018\u00010\u0003H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u00a2\u0006\u0002\u0010\u001e\u001aG\u0010\u001f\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0015\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00180\u00150 \"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0018*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00180 0\u0003\u00a2\u0006\u0002\u0010!\u00a8\u0006\""}, d2={"contentDeepEqualsImpl", "", "T", "", "other", "contentDeepEquals", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", "contentDeepToStringImpl", "", "contentDeepToString", "([Ljava/lang/Object;)Ljava/lang/String;", "contentDeepToStringInternal", "", "result", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "processed", "", "contentDeepToStringInternal$ArraysKt__ArraysKt", "([Ljava/lang/Object;Ljava/lang/StringBuilder;Ljava/util/List;)V", "flatten", "", "([[Ljava/lang/Object;)Ljava/util/List;", "ifEmpty", "R", "C", "defaultValue", "Lkotlin/Function0;", "([Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isNullOrEmpty", "([Ljava/lang/Object;)Z", "unzip", "Lkotlin/Pair;", "([Lkotlin/Pair;)Lkotlin/Pair;", "kotlin-stdlib"}, xs="kotlin/collections/ArraysKt")
class ArraysKt__ArraysKt
extends ArraysKt__ArraysJVMKt {
    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <T> List<T> flatten(@NotNull T[][] $this$flatten) {
        int n;
        Intrinsics.checkParameterIsNotNull($this$flatten, "$this$flatten");
        Object[] $this$sumBy$iv = (Object[])$this$flatten;
        boolean $i$f$sumBy = false;
        int sum$iv = 0;
        for (Object element$iv : $this$sumBy$iv) {
            void it;
            Object[] objectArray = (Object[])element$iv;
            n = sum$iv;
            boolean bl = false;
            int n2 = ((void)it).length;
            sum$iv = n + n2;
        }
        int n3 = n = sum$iv;
        ArrayList result = new ArrayList(n3);
        for (T[] element : $this$flatten) {
            CollectionsKt.addAll((Collection)result, element);
        }
        return result;
    }

    @NotNull
    public static final <T, R> Pair<List<T>, List<R>> unzip(@NotNull Pair<? extends T, ? extends R>[] $this$unzip) {
        Intrinsics.checkParameterIsNotNull($this$unzip, "$this$unzip");
        ArrayList<T> listT = new ArrayList<T>($this$unzip.length);
        ArrayList<R> listR = new ArrayList<R>($this$unzip.length);
        for (Pair<T, R> pair : $this$unzip) {
            listT.add(pair.getFirst());
            listR.add(pair.getSecond());
        }
        return TuplesKt.to(listT, listR);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final boolean isNullOrEmpty(@Nullable Object[] $this$isNullOrEmpty) {
        int $i$f$isNullOrEmpty = 0;
        boolean bl = false;
        if ($this$isNullOrEmpty == null) return true;
        Object[] objectArray = $this$isNullOrEmpty;
        boolean bl2 = false;
        if (objectArray.length != 0) return false;
        return true;
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <C extends Object[], R> R ifEmpty(C $this$ifEmpty, Function0<? extends R> defaultValue) {
        int $i$f$ifEmpty = 0;
        C c = $this$ifEmpty;
        boolean bl = false;
        return (R)(((C)c).length == 0 ? defaultValue.invoke() : $this$ifEmpty);
    }

    /*
     * WARNING - void declaration
     */
    @SinceKotlin(version="1.3")
    @PublishedApi
    @JvmName(name="contentDeepEquals")
    public static final <T> boolean contentDeepEquals(@NotNull T[] $this$contentDeepEqualsImpl, @NotNull T[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentDeepEqualsImpl, "$this$contentDeepEqualsImpl");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if ($this$contentDeepEqualsImpl == other) {
            return true;
        }
        if ($this$contentDeepEqualsImpl.length != other.length) {
            return false;
        }
        int n = 0;
        int n2 = $this$contentDeepEqualsImpl.length;
        while (n < n2) {
            void i;
            T v1 = $this$contentDeepEqualsImpl[i];
            T v2 = other[i];
            if (v1 != v2) {
                boolean bl;
                Object[] objectArray;
                if (v1 == null || v2 == null) {
                    return false;
                }
                if (v1 instanceof Object[] && v2 instanceof Object[]) {
                    objectArray = (Object[])v1;
                    bl = false;
                    if (!ArraysKt.contentDeepEquals(objectArray, (Object[])v2)) {
                        return false;
                    }
                } else if (v1 instanceof byte[] && v2 instanceof byte[]) {
                    objectArray = (byte[])v1;
                    bl = false;
                    if (!Arrays.equals((byte[])objectArray, (byte[])v2)) {
                        return false;
                    }
                } else if (v1 instanceof short[] && v2 instanceof short[]) {
                    objectArray = (short[])v1;
                    bl = false;
                    if (!Arrays.equals((short[])objectArray, (short[])v2)) {
                        return false;
                    }
                } else if (v1 instanceof int[] && v2 instanceof int[]) {
                    objectArray = (int[])v1;
                    bl = false;
                    if (!Arrays.equals((int[])objectArray, (int[])v2)) {
                        return false;
                    }
                } else if (v1 instanceof long[] && v2 instanceof long[]) {
                    objectArray = (long[])v1;
                    bl = false;
                    if (!Arrays.equals((long[])objectArray, (long[])v2)) {
                        return false;
                    }
                } else if (v1 instanceof float[] && v2 instanceof float[]) {
                    objectArray = (float[])v1;
                    bl = false;
                    if (!Arrays.equals((float[])objectArray, (float[])v2)) {
                        return false;
                    }
                } else if (v1 instanceof double[] && v2 instanceof double[]) {
                    objectArray = (double[])v1;
                    bl = false;
                    if (!Arrays.equals((double[])objectArray, (double[])v2)) {
                        return false;
                    }
                } else if (v1 instanceof char[] && v2 instanceof char[]) {
                    objectArray = (char[])v1;
                    bl = false;
                    if (!Arrays.equals((char[])objectArray, (char[])v2)) {
                        return false;
                    }
                } else if (v1 instanceof boolean[] && v2 instanceof boolean[]) {
                    objectArray = (boolean[])v1;
                    bl = false;
                    if (!Arrays.equals((boolean[])objectArray, (boolean[])v2)) {
                        return false;
                    }
                } else if (v1 instanceof UByteArray && v2 instanceof UByteArray ? !UArraysKt.contentEquals-kdPth3s(((UByteArray)v1).unbox-impl(), ((UByteArray)v2).unbox-impl()) : (v1 instanceof UShortArray && v2 instanceof UShortArray ? !UArraysKt.contentEquals-mazbYpA(((UShortArray)v1).unbox-impl(), ((UShortArray)v2).unbox-impl()) : (v1 instanceof UIntArray && v2 instanceof UIntArray ? !UArraysKt.contentEquals-ctEhBpI(((UIntArray)v1).unbox-impl(), ((UIntArray)v2).unbox-impl()) : (v1 instanceof ULongArray && v2 instanceof ULongArray ? !UArraysKt.contentEquals-us8wMrg(((ULongArray)v1).unbox-impl(), ((ULongArray)v2).unbox-impl()) : Intrinsics.areEqual(v1, v2) ^ true)))) {
                    return false;
                }
            }
            ++i;
        }
        return true;
    }

    @SinceKotlin(version="1.3")
    @PublishedApi
    @JvmName(name="contentDeepToString")
    @NotNull
    public static final <T> String contentDeepToString(@NotNull T[] $this$contentDeepToStringImpl) {
        Intrinsics.checkParameterIsNotNull($this$contentDeepToStringImpl, "$this$contentDeepToStringImpl");
        int length = RangesKt.coerceAtMost($this$contentDeepToStringImpl.length, 0x19999999) * 5 + 2;
        boolean bl = false;
        StringBuilder stringBuilder = new StringBuilder(length);
        boolean bl2 = false;
        boolean bl3 = false;
        StringBuilder $this$buildString = stringBuilder;
        boolean bl4 = false;
        StringBuilder stringBuilder2 = $this$buildString;
        T[] TArray = $this$contentDeepToStringImpl;
        boolean bl5 = false;
        List list = new ArrayList();
        ArraysKt__ArraysKt.contentDeepToStringInternal$ArraysKt__ArraysKt(TArray, stringBuilder2, list);
        String string = stringBuilder.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "StringBuilder(capacity).\u2026builderAction).toString()");
        return string;
    }

    /*
     * WARNING - void declaration
     */
    private static final <T> void contentDeepToStringInternal$ArraysKt__ArraysKt(@NotNull T[] $this$contentDeepToStringInternal, StringBuilder result, List<Object[]> processed) {
        if (processed.contains($this$contentDeepToStringInternal)) {
            result.append("[...]");
            return;
        }
        processed.add($this$contentDeepToStringInternal);
        result.append('[');
        int n = 0;
        int n2 = $this$contentDeepToStringInternal.length;
        while (n < n2) {
            String string;
            boolean bl;
            StringBuilder stringBuilder;
            Object[] objectArray;
            T element;
            T t;
            void i;
            if (i != false) {
                result.append(", ");
            }
            if ((t = (element = $this$contentDeepToStringInternal[i])) == null) {
                result.append("null");
            } else if (t instanceof Object[]) {
                ArraysKt__ArraysKt.contentDeepToStringInternal$ArraysKt__ArraysKt((Object[])element, result, processed);
            } else if (t instanceof byte[]) {
                objectArray = (byte[])element;
                stringBuilder = result;
                bl = false;
                Intrinsics.checkExpressionValueIsNotNull(Arrays.toString(objectArray), "java.util.Arrays.toString(this)");
                stringBuilder.append(string);
            } else if (t instanceof short[]) {
                objectArray = (short[])element;
                stringBuilder = result;
                bl = false;
                Intrinsics.checkExpressionValueIsNotNull(Arrays.toString((short[])objectArray), "java.util.Arrays.toString(this)");
                stringBuilder.append(string);
            } else if (t instanceof int[]) {
                objectArray = (int[])element;
                stringBuilder = result;
                bl = false;
                Intrinsics.checkExpressionValueIsNotNull(Arrays.toString((int[])objectArray), "java.util.Arrays.toString(this)");
                stringBuilder.append(string);
            } else if (t instanceof long[]) {
                objectArray = (long[])element;
                stringBuilder = result;
                bl = false;
                Intrinsics.checkExpressionValueIsNotNull(Arrays.toString((long[])objectArray), "java.util.Arrays.toString(this)");
                stringBuilder.append(string);
            } else if (t instanceof float[]) {
                objectArray = (float[])element;
                stringBuilder = result;
                bl = false;
                Intrinsics.checkExpressionValueIsNotNull(Arrays.toString((float[])objectArray), "java.util.Arrays.toString(this)");
                stringBuilder.append(string);
            } else if (t instanceof double[]) {
                objectArray = (double[])element;
                stringBuilder = result;
                bl = false;
                Intrinsics.checkExpressionValueIsNotNull(Arrays.toString((double[])objectArray), "java.util.Arrays.toString(this)");
                stringBuilder.append(string);
            } else if (t instanceof char[]) {
                objectArray = (char[])element;
                stringBuilder = result;
                bl = false;
                Intrinsics.checkExpressionValueIsNotNull(Arrays.toString((char[])objectArray), "java.util.Arrays.toString(this)");
                stringBuilder.append(string);
            } else if (t instanceof boolean[]) {
                objectArray = (boolean[])element;
                stringBuilder = result;
                bl = false;
                Intrinsics.checkExpressionValueIsNotNull(Arrays.toString((boolean[])objectArray), "java.util.Arrays.toString(this)");
                stringBuilder.append(string);
            } else if (t instanceof UByteArray) {
                result.append(UArraysKt.contentToString-GBYM_sE(((UByteArray)element).unbox-impl()));
            } else if (t instanceof UShortArray) {
                result.append(UArraysKt.contentToString-rL5Bavg(((UShortArray)element).unbox-impl()));
            } else if (t instanceof UIntArray) {
                result.append(UArraysKt.contentToString--ajY-9A(((UIntArray)element).unbox-impl()));
            } else if (t instanceof ULongArray) {
                result.append(UArraysKt.contentToString-QwZRm1k(((ULongArray)element).unbox-impl()));
            } else {
                result.append(element.toString());
            }
            ++i;
        }
        result.append(']');
        processed.remove(CollectionsKt.getLastIndex(processed));
    }
}

