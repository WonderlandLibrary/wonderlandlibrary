/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.collections;

import java.util.Arrays;
import java.util.NoSuchElementException;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Deprecated(message="Provided for binary compatibility", level=DeprecationLevel.HIDDEN)
@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\t\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0007\u0010\bJ\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\t2\u0006\u0010\u0006\u001a\u00020\tH\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b\n\u0010\u000bJ\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\f2\u0006\u0010\u0006\u001a\u00020\fH\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b\r\u0010\u000eJ\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u000fH\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0010\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\u0005H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0014\u0010\u0015J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\tH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0016\u0010\u0017J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0018\u0010\u0019J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\u0005H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\tH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b \u0010!J\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\"\u0010#J\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b$\u0010%J\u001e\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010&\u001a\u00020(H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b)\u0010*J\u001e\u0010&\u001a\u00020+*\u00020\t2\u0006\u0010&\u001a\u00020(H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010-J\u001e\u0010&\u001a\u00020.*\u00020\f2\u0006\u0010&\u001a\u00020(H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b/\u00100J\u001e\u0010&\u001a\u000201*\u00020\u000f2\u0006\u0010&\u001a\u00020(H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b2\u00103J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020'05*\u00020\u0005H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b6\u00107J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020+05*\u00020\tH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b8\u00109J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020.05*\u00020\fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b:\u0010;J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020105*\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b<\u0010=\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006>"}, d2={"Lkotlin/collections/UArraysKt;", "", "()V", "contentEquals", "", "Lkotlin/UByteArray;", "other", "contentEquals-kdPth3s", "([B[B)Z", "Lkotlin/UIntArray;", "contentEquals-ctEhBpI", "([I[I)Z", "Lkotlin/ULongArray;", "contentEquals-us8wMrg", "([J[J)Z", "Lkotlin/UShortArray;", "contentEquals-mazbYpA", "([S[S)Z", "contentHashCode", "", "contentHashCode-GBYM_sE", "([B)I", "contentHashCode--ajY-9A", "([I)I", "contentHashCode-QwZRm1k", "([J)I", "contentHashCode-rL5Bavg", "([S)I", "contentToString", "", "contentToString-GBYM_sE", "([B)Ljava/lang/String;", "contentToString--ajY-9A", "([I)Ljava/lang/String;", "contentToString-QwZRm1k", "([J)Ljava/lang/String;", "contentToString-rL5Bavg", "([S)Ljava/lang/String;", "random", "Lkotlin/UByte;", "Lkotlin/random/Random;", "random-oSF2wD8", "([BLkotlin/random/Random;)B", "Lkotlin/UInt;", "random-2D5oskM", "([ILkotlin/random/Random;)I", "Lkotlin/ULong;", "random-JzugnMA", "([JLkotlin/random/Random;)J", "Lkotlin/UShort;", "random-s5X_as8", "([SLkotlin/random/Random;)S", "toTypedArray", "", "toTypedArray-GBYM_sE", "([B)[Lkotlin/UByte;", "toTypedArray--ajY-9A", "([I)[Lkotlin/UInt;", "toTypedArray-QwZRm1k", "([J)[Lkotlin/ULong;", "toTypedArray-rL5Bavg", "([S)[Lkotlin/UShort;", "kotlin-stdlib"})
public final class UArraysKt {
    public static final UArraysKt INSTANCE;

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final int random-2D5oskM(@NotNull int[] $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (UIntArray.isEmpty-impl($this$random)) {
            throw (Throwable)new NoSuchElementException("Array is empty.");
        }
        return UIntArray.get-impl($this$random, random.nextInt(UIntArray.getSize-impl($this$random)));
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final long random-JzugnMA(@NotNull long[] $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (ULongArray.isEmpty-impl($this$random)) {
            throw (Throwable)new NoSuchElementException("Array is empty.");
        }
        return ULongArray.get-impl($this$random, random.nextInt(ULongArray.getSize-impl($this$random)));
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final byte random-oSF2wD8(@NotNull byte[] $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (UByteArray.isEmpty-impl($this$random)) {
            throw (Throwable)new NoSuchElementException("Array is empty.");
        }
        return UByteArray.get-impl($this$random, random.nextInt(UByteArray.getSize-impl($this$random)));
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final short random-s5X_as8(@NotNull short[] $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (UShortArray.isEmpty-impl($this$random)) {
            throw (Throwable)new NoSuchElementException("Array is empty.");
        }
        return UShortArray.get-impl($this$random, random.nextInt(UShortArray.getSize-impl($this$random)));
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final boolean contentEquals-ctEhBpI(@NotNull int[] $this$contentEquals, @NotNull int[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        int[] nArray = $this$contentEquals;
        int[] nArray2 = other;
        boolean bl = false;
        return Arrays.equals(nArray, nArray2);
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final boolean contentEquals-us8wMrg(@NotNull long[] $this$contentEquals, @NotNull long[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        long[] lArray = $this$contentEquals;
        long[] lArray2 = other;
        boolean bl = false;
        return Arrays.equals(lArray, lArray2);
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final boolean contentEquals-kdPth3s(@NotNull byte[] $this$contentEquals, @NotNull byte[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        byte[] byArray = $this$contentEquals;
        byte[] byArray2 = other;
        boolean bl = false;
        return Arrays.equals(byArray, byArray2);
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final boolean contentEquals-mazbYpA(@NotNull short[] $this$contentEquals, @NotNull short[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        short[] sArray = $this$contentEquals;
        short[] sArray2 = other;
        boolean bl = false;
        return Arrays.equals(sArray, sArray2);
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final int contentHashCode--ajY-9A(@NotNull int[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        int[] nArray = $this$contentHashCode;
        boolean bl = false;
        return Arrays.hashCode(nArray);
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final int contentHashCode-QwZRm1k(@NotNull long[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        long[] lArray = $this$contentHashCode;
        boolean bl = false;
        return Arrays.hashCode(lArray);
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final int contentHashCode-GBYM_sE(@NotNull byte[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        byte[] byArray = $this$contentHashCode;
        boolean bl = false;
        return Arrays.hashCode(byArray);
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    public static final int contentHashCode-rL5Bavg(@NotNull short[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        short[] sArray = $this$contentHashCode;
        boolean bl = false;
        return Arrays.hashCode(sArray);
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String contentToString--ajY-9A(@NotNull int[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(UIntArray.box-impl($this$contentToString), ", ", "[", "]", 0, null, null, 56, null);
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String contentToString-QwZRm1k(@NotNull long[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(ULongArray.box-impl($this$contentToString), ", ", "[", "]", 0, null, null, 56, null);
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String contentToString-GBYM_sE(@NotNull byte[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(UByteArray.box-impl($this$contentToString), ", ", "[", "]", 0, null, null, 56, null);
    }

    @JvmStatic
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String contentToString-rL5Bavg(@NotNull short[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(UShortArray.box-impl($this$contentToString), ", ", "[", "]", 0, null, null, 56, null);
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UInt[] toTypedArray--ajY-9A(@NotNull int[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int n = UIntArray.getSize-impl($this$toTypedArray);
        UInt[] uIntArray = new UInt[n];
        int n2 = 0;
        while (n2 < n) {
            void index;
            UInt uInt;
            int n3 = n2;
            int n4 = n2++;
            UInt[] uIntArray2 = uIntArray;
            boolean bl = false;
            uIntArray2[n4] = uInt = UInt.box-impl(UIntArray.get-impl($this$toTypedArray, (int)index));
        }
        return uIntArray;
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @ExperimentalUnsignedTypes
    @NotNull
    public static final ULong[] toTypedArray-QwZRm1k(@NotNull long[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int n = ULongArray.getSize-impl($this$toTypedArray);
        ULong[] uLongArray = new ULong[n];
        int n2 = 0;
        while (n2 < n) {
            void index;
            ULong uLong;
            int n3 = n2;
            int n4 = n2++;
            ULong[] uLongArray2 = uLongArray;
            boolean bl = false;
            uLongArray2[n4] = uLong = ULong.box-impl(ULongArray.get-impl($this$toTypedArray, (int)index));
        }
        return uLongArray;
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UByte[] toTypedArray-GBYM_sE(@NotNull byte[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int n = UByteArray.getSize-impl($this$toTypedArray);
        UByte[] uByteArray = new UByte[n];
        int n2 = 0;
        while (n2 < n) {
            void index;
            UByte uByte;
            int n3 = n2;
            int n4 = n2++;
            UByte[] uByteArray2 = uByteArray;
            boolean bl = false;
            uByteArray2[n4] = uByte = UByte.box-impl(UByteArray.get-impl($this$toTypedArray, (int)index));
        }
        return uByteArray;
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UShort[] toTypedArray-rL5Bavg(@NotNull short[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int n = UShortArray.getSize-impl($this$toTypedArray);
        UShort[] uShortArray = new UShort[n];
        int n2 = 0;
        while (n2 < n) {
            void index;
            UShort uShort;
            int n3 = n2;
            int n4 = n2++;
            UShort[] uShortArray2 = uShortArray;
            boolean bl = false;
            uShortArray2[n4] = uShort = UShort.box-impl(UShortArray.get-impl($this$toTypedArray, (int)index));
        }
        return uShortArray;
    }

    private UArraysKt() {
    }

    static {
        UArraysKt uArraysKt;
        INSTANCE = uArraysKt = new UArraysKt();
    }
}

