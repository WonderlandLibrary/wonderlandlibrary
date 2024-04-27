/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.text;

import kotlin.ExperimentalUnsignedTypes;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.WasExperimental;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000,\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\f\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000e\u0010\u000f\u001a\u0014\u0010\u0010\u001a\u00020\u0002*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0010\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u001a\u0011\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u0014\u001a\u00020\u0007*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015\u001a\u001c\u0010\u0014\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016\u001a\u0011\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u0018\u001a\u00020\n*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019\u001a\u001c\u0010\u0018\u001a\u00020\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001a\u001a\u0011\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u001c\u001a\u00020\r*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001d\u001a\u001c\u0010\u001c\u001a\u00020\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001e\u001a\u0011\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006 "}, d2={"toString", "", "Lkotlin/UByte;", "radix", "", "toString-LxnNnR4", "(BI)Ljava/lang/String;", "Lkotlin/UInt;", "toString-V7xB4Y4", "(II)Ljava/lang/String;", "Lkotlin/ULong;", "toString-JSWoG40", "(JI)Ljava/lang/String;", "Lkotlin/UShort;", "toString-olVBNx4", "(SI)Ljava/lang/String;", "toUByte", "(Ljava/lang/String;)B", "(Ljava/lang/String;I)B", "toUByteOrNull", "toUInt", "(Ljava/lang/String;)I", "(Ljava/lang/String;I)I", "toUIntOrNull", "toULong", "(Ljava/lang/String;)J", "(Ljava/lang/String;I)J", "toULongOrNull", "toUShort", "(Ljava/lang/String;)S", "(Ljava/lang/String;I)S", "toUShortOrNull", "kotlin-stdlib"})
@JvmName(name="UStringsKt")
public final class UStringsKt {
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @NotNull
    public static final String toString-LxnNnR4(byte $this$toString, int radix) {
        int n = $this$toString & 0xFF;
        String string = Integer.toString(n, CharsKt.checkRadix(radix));
        Intrinsics.checkNotNullExpressionValue(string, "toString(this, checkRadix(radix))");
        return string;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @NotNull
    public static final String toString-olVBNx4(short $this$toString, int radix) {
        int n = $this$toString & 0xFFFF;
        String string = Integer.toString(n, CharsKt.checkRadix(radix));
        Intrinsics.checkNotNullExpressionValue(string, "toString(this, checkRadix(radix))");
        return string;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @NotNull
    public static final String toString-V7xB4Y4(int $this$toString, int radix) {
        long l = (long)$this$toString & 0xFFFFFFFFL;
        String string = Long.toString(l, CharsKt.checkRadix(radix));
        Intrinsics.checkNotNullExpressionValue(string, "toString(this, checkRadix(radix))");
        return string;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @NotNull
    public static final String toString-JSWoG40(long $this$toString, int radix) {
        return UnsignedKt.ulongToString($this$toString, CharsKt.checkRadix(radix));
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final byte toUByte(@NotNull String $this$toUByte) {
        Intrinsics.checkNotNullParameter($this$toUByte, "<this>");
        UByte uByte = UStringsKt.toUByteOrNull($this$toUByte);
        if (uByte == null) {
            StringsKt.numberFormatError($this$toUByte);
            throw new KotlinNothingValueException();
        }
        return uByte.unbox-impl();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final byte toUByte(@NotNull String $this$toUByte, int radix) {
        Intrinsics.checkNotNullParameter($this$toUByte, "<this>");
        UByte uByte = UStringsKt.toUByteOrNull($this$toUByte, radix);
        if (uByte == null) {
            StringsKt.numberFormatError($this$toUByte);
            throw new KotlinNothingValueException();
        }
        return uByte.unbox-impl();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final short toUShort(@NotNull String $this$toUShort) {
        Intrinsics.checkNotNullParameter($this$toUShort, "<this>");
        UShort uShort = UStringsKt.toUShortOrNull($this$toUShort);
        if (uShort == null) {
            StringsKt.numberFormatError($this$toUShort);
            throw new KotlinNothingValueException();
        }
        return uShort.unbox-impl();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final short toUShort(@NotNull String $this$toUShort, int radix) {
        Intrinsics.checkNotNullParameter($this$toUShort, "<this>");
        UShort uShort = UStringsKt.toUShortOrNull($this$toUShort, radix);
        if (uShort == null) {
            StringsKt.numberFormatError($this$toUShort);
            throw new KotlinNothingValueException();
        }
        return uShort.unbox-impl();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int toUInt(@NotNull String $this$toUInt) {
        Intrinsics.checkNotNullParameter($this$toUInt, "<this>");
        UInt uInt = UStringsKt.toUIntOrNull($this$toUInt);
        if (uInt == null) {
            StringsKt.numberFormatError($this$toUInt);
            throw new KotlinNothingValueException();
        }
        return uInt.unbox-impl();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int toUInt(@NotNull String $this$toUInt, int radix) {
        Intrinsics.checkNotNullParameter($this$toUInt, "<this>");
        UInt uInt = UStringsKt.toUIntOrNull($this$toUInt, radix);
        if (uInt == null) {
            StringsKt.numberFormatError($this$toUInt);
            throw new KotlinNothingValueException();
        }
        return uInt.unbox-impl();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long toULong(@NotNull String $this$toULong) {
        Intrinsics.checkNotNullParameter($this$toULong, "<this>");
        ULong uLong = UStringsKt.toULongOrNull($this$toULong);
        if (uLong == null) {
            StringsKt.numberFormatError($this$toULong);
            throw new KotlinNothingValueException();
        }
        return uLong.unbox-impl();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long toULong(@NotNull String $this$toULong, int radix) {
        Intrinsics.checkNotNullParameter($this$toULong, "<this>");
        ULong uLong = UStringsKt.toULongOrNull($this$toULong, radix);
        if (uLong == null) {
            StringsKt.numberFormatError($this$toULong);
            throw new KotlinNothingValueException();
        }
        return uLong.unbox-impl();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UByte toUByteOrNull(@NotNull String $this$toUByteOrNull) {
        Intrinsics.checkNotNullParameter($this$toUByteOrNull, "<this>");
        return UStringsKt.toUByteOrNull($this$toUByteOrNull, 10);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UByte toUByteOrNull(@NotNull String $this$toUByteOrNull, int radix) {
        int n;
        int n2;
        Intrinsics.checkNotNullParameter($this$toUByteOrNull, "<this>");
        UInt uInt = UStringsKt.toUIntOrNull($this$toUByteOrNull, radix);
        if (uInt == null) {
            return null;
        }
        int n3 = uInt.unbox-impl();
        if (UnsignedKt.uintCompare(n3, n2 = UInt.constructor-impl((n = -1) & 0xFF)) > 0) {
            return null;
        }
        return UByte.box-impl(UByte.constructor-impl((byte)n3));
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UShort toUShortOrNull(@NotNull String $this$toUShortOrNull) {
        Intrinsics.checkNotNullParameter($this$toUShortOrNull, "<this>");
        return UStringsKt.toUShortOrNull($this$toUShortOrNull, 10);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UShort toUShortOrNull(@NotNull String $this$toUShortOrNull, int radix) {
        int n;
        int n2;
        Intrinsics.checkNotNullParameter($this$toUShortOrNull, "<this>");
        UInt uInt = UStringsKt.toUIntOrNull($this$toUShortOrNull, radix);
        if (uInt == null) {
            return null;
        }
        int n3 = uInt.unbox-impl();
        if (UnsignedKt.uintCompare(n3, n2 = UInt.constructor-impl((n = -1) & 0xFFFF)) > 0) {
            return null;
        }
        return UShort.box-impl(UShort.constructor-impl((short)n3));
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UInt toUIntOrNull(@NotNull String $this$toUIntOrNull) {
        Intrinsics.checkNotNullParameter($this$toUIntOrNull, "<this>");
        return UStringsKt.toUIntOrNull($this$toUIntOrNull, 10);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UInt toUIntOrNull(@NotNull String $this$toUIntOrNull, int radix) {
        int limitForMaxRadix;
        Intrinsics.checkNotNullParameter($this$toUIntOrNull, "<this>");
        CharsKt.checkRadix(radix);
        int length = $this$toUIntOrNull.length();
        if (length == 0) {
            return null;
        }
        int limit = -1;
        int start = 0;
        char firstChar = $this$toUIntOrNull.charAt(0);
        if (Intrinsics.compare(firstChar, 48) < 0) {
            if (length == 1 || firstChar != '+') {
                return null;
            }
            start = 1;
        } else {
            start = 0;
        }
        int limitBeforeMul = limitForMaxRadix = 0x71C71C7;
        int n = radix;
        int uradix = UInt.constructor-impl(n);
        int result = 0;
        int n2 = start;
        while (n2 < length) {
            int i;
            int digit;
            if ((digit = CharsKt.digitOf($this$toUIntOrNull.charAt(i = n2++), radix)) < 0) {
                return null;
            }
            if (UnsignedKt.uintCompare(result, limitBeforeMul) > 0) {
                if (limitBeforeMul == limitForMaxRadix) {
                    limitBeforeMul = UnsignedKt.uintDivide-J1ME1BU(limit, uradix);
                    if (UnsignedKt.uintCompare(result, limitBeforeMul) > 0) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            int beforeAdding = result = UInt.constructor-impl(result * uradix);
            int n3 = digit;
            if (UnsignedKt.uintCompare(result = UInt.constructor-impl(result + (n3 = UInt.constructor-impl(n3))), beforeAdding) >= 0) continue;
            return null;
        }
        return UInt.box-impl(result);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final ULong toULongOrNull(@NotNull String $this$toULongOrNull) {
        Intrinsics.checkNotNullParameter($this$toULongOrNull, "<this>");
        return UStringsKt.toULongOrNull($this$toULongOrNull, 10);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final ULong toULongOrNull(@NotNull String $this$toULongOrNull, int radix) {
        long limitForMaxRadix;
        Intrinsics.checkNotNullParameter($this$toULongOrNull, "<this>");
        CharsKt.checkRadix(radix);
        int length = $this$toULongOrNull.length();
        if (length == 0) {
            return null;
        }
        long limit = -1L;
        int start = 0;
        char firstChar = $this$toULongOrNull.charAt(0);
        if (Intrinsics.compare(firstChar, 48) < 0) {
            if (length == 1 || firstChar != '+') {
                return null;
            }
            start = 1;
        } else {
            start = 0;
        }
        long limitBeforeMul = limitForMaxRadix = 0x71C71C71C71C71CL;
        int n = radix;
        long uradix = ULong.constructor-impl(n);
        long result = 0L;
        int n2 = start;
        while (n2 < length) {
            int i;
            int digit;
            if ((digit = CharsKt.digitOf($this$toULongOrNull.charAt(i = n2++), radix)) < 0) {
                return null;
            }
            if (UnsignedKt.ulongCompare(result, limitBeforeMul) > 0) {
                if (limitBeforeMul == limitForMaxRadix) {
                    limitBeforeMul = UnsignedKt.ulongDivide-eb3DHEI(limit, uradix);
                    if (UnsignedKt.ulongCompare(result, limitBeforeMul) > 0) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            long beforeAdding = result = ULong.constructor-impl(result * uradix);
            int n3 = digit;
            long l = ULong.constructor-impl((long)(n3 = UInt.constructor-impl(n3)) & 0xFFFFFFFFL);
            if (UnsignedKt.ulongCompare(result = ULong.constructor-impl(result + l), beforeAdding) >= 0) continue;
            return null;
        }
        return ULong.box-impl(result);
    }
}

