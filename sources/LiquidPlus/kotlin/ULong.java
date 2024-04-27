/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin;

import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.WasExperimental;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmInline;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.ULongRange;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@JvmInline
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\"\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 |2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001|B\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000\u00a2\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0010J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0014\u0010\u0015J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001a\u0010\u0005J\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001c\u0010\u001dJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b \u0010\u000bJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\"J\u001a\u0010#\u001a\u00020$2\b\u0010\t\u001a\u0004\u0018\u00010%H\u00d6\u0003\u00a2\u0006\u0004\b&\u0010'J\u001b\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b)\u0010\u001dJ\u001b\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b*\u0010\u001fJ\u001b\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b+\u0010\u000bJ\u001b\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010\"J\u0010\u0010-\u001a\u00020\rH\u00d6\u0001\u00a2\u0006\u0004\b.\u0010/J\u0016\u00100\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b1\u0010\u0005J\u0016\u00102\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b3\u0010\u0005J\u001b\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b5\u0010\u001dJ\u001b\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b6\u0010\u001fJ\u001b\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b7\u0010\u000bJ\u001b\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b8\u0010\"J\u001b\u00109\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000eH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b:\u0010;J\u001b\u00109\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b<\u0010\u0013J\u001b\u00109\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b=\u0010\u000bJ\u001b\u00109\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\u0016H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b>\u0010?J\u001b\u0010@\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000\u00a2\u0006\u0004\bA\u0010\u000bJ\u001b\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bC\u0010\u001dJ\u001b\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bD\u0010\u001fJ\u001b\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bE\u0010\u000bJ\u001b\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bF\u0010\"J\u001b\u0010G\u001a\u00020H2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bI\u0010JJ\u001b\u0010K\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bL\u0010\u001dJ\u001b\u0010K\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bM\u0010\u001fJ\u001b\u0010K\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bN\u0010\u000bJ\u001b\u0010K\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bO\u0010\"J\u001e\u0010P\u001a\u00020\u00002\u0006\u0010Q\u001a\u00020\rH\u0087\f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bR\u0010\u001fJ\u001e\u0010S\u001a\u00020\u00002\u0006\u0010Q\u001a\u00020\rH\u0087\f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bT\u0010\u001fJ\u001b\u0010U\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bV\u0010\u001dJ\u001b\u0010U\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bW\u0010\u001fJ\u001b\u0010U\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bX\u0010\u000bJ\u001b\u0010U\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bY\u0010\"J\u0010\u0010Z\u001a\u00020[H\u0087\b\u00a2\u0006\u0004\b\\\u0010]J\u0010\u0010^\u001a\u00020_H\u0087\b\u00a2\u0006\u0004\b`\u0010aJ\u0010\u0010b\u001a\u00020cH\u0087\b\u00a2\u0006\u0004\bd\u0010eJ\u0010\u0010f\u001a\u00020\rH\u0087\b\u00a2\u0006\u0004\bg\u0010/J\u0010\u0010h\u001a\u00020\u0003H\u0087\b\u00a2\u0006\u0004\bi\u0010\u0005J\u0010\u0010j\u001a\u00020kH\u0087\b\u00a2\u0006\u0004\bl\u0010mJ\u000f\u0010n\u001a\u00020oH\u0016\u00a2\u0006\u0004\bp\u0010qJ\u0016\u0010r\u001a\u00020\u000eH\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bs\u0010]J\u0016\u0010t\u001a\u00020\u0011H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bu\u0010/J\u0016\u0010v\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bw\u0010\u0005J\u0016\u0010x\u001a\u00020\u0016H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\by\u0010mJ\u001b\u0010z\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000\u00a2\u0006\u0004\b{\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006}"}, d2={"Lkotlin/ULong;", "", "data", "", "constructor-impl", "(J)J", "getData$annotations", "()V", "and", "other", "and-VKZWuLQ", "(JJ)J", "compareTo", "", "Lkotlin/UByte;", "compareTo-7apg3OU", "(JB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(JI)I", "compareTo-VKZWuLQ", "(JJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(JS)I", "dec", "dec-s-VKNKU", "div", "div-7apg3OU", "(JB)J", "div-WZ4Q5Ns", "(JI)J", "div-VKZWuLQ", "div-xj2QHRw", "(JS)J", "equals", "", "", "equals-impl", "(JLjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", "hashCode", "hashCode-impl", "(J)I", "inc", "inc-s-VKNKU", "inv", "inv-s-VKNKU", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "(JB)B", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "(JS)S", "or", "or-VKZWuLQ", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/ULongRange;", "rangeTo-VKZWuLQ", "(JJ)Lkotlin/ranges/ULongRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "shl", "bitCount", "shl-s-VKNKU", "shr", "shr-s-VKNKU", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(J)B", "toDouble", "", "toDouble-impl", "(J)D", "toFloat", "", "toFloat-impl", "(J)F", "toInt", "toInt-impl", "toLong", "toLong-impl", "toShort", "", "toShort-impl", "(J)S", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-VKZWuLQ", "Companion", "kotlin-stdlib"})
@SinceKotlin(version="1.5")
@WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
public final class ULong
implements Comparable<ULong> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private final long data;
    public static final long MIN_VALUE = 0L;
    public static final long MAX_VALUE = -1L;
    public static final int SIZE_BYTES = 8;
    public static final int SIZE_BITS = 64;

    @PublishedApi
    public static /* synthetic */ void getData$annotations() {
    }

    @InlineOnly
    private static final int compareTo-7apg3OU(long arg0, byte other) {
        long l = ULong.constructor-impl((long)other & 0xFFL);
        return UnsignedKt.ulongCompare(arg0, l);
    }

    @InlineOnly
    private static final int compareTo-xj2QHRw(long arg0, short other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFL);
        return UnsignedKt.ulongCompare(arg0, l);
    }

    @InlineOnly
    private static final int compareTo-WZ4Q5Ns(long arg0, int other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFFFFFL);
        return UnsignedKt.ulongCompare(arg0, l);
    }

    @InlineOnly
    private static int compareTo-VKZWuLQ(long arg0, long other) {
        return UnsignedKt.ulongCompare(arg0, other);
    }

    @InlineOnly
    private int compareTo-VKZWuLQ(long other) {
        long l = this.unbox-impl();
        return UnsignedKt.ulongCompare(l, other);
    }

    @InlineOnly
    private static final long plus-7apg3OU(long arg0, byte other) {
        long l = ULong.constructor-impl((long)other & 0xFFL);
        return ULong.constructor-impl(arg0 + l);
    }

    @InlineOnly
    private static final long plus-xj2QHRw(long arg0, short other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFL);
        return ULong.constructor-impl(arg0 + l);
    }

    @InlineOnly
    private static final long plus-WZ4Q5Ns(long arg0, int other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFFFFFL);
        return ULong.constructor-impl(arg0 + l);
    }

    @InlineOnly
    private static final long plus-VKZWuLQ(long arg0, long other) {
        return ULong.constructor-impl(arg0 + other);
    }

    @InlineOnly
    private static final long minus-7apg3OU(long arg0, byte other) {
        long l = ULong.constructor-impl((long)other & 0xFFL);
        return ULong.constructor-impl(arg0 - l);
    }

    @InlineOnly
    private static final long minus-xj2QHRw(long arg0, short other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFL);
        return ULong.constructor-impl(arg0 - l);
    }

    @InlineOnly
    private static final long minus-WZ4Q5Ns(long arg0, int other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFFFFFL);
        return ULong.constructor-impl(arg0 - l);
    }

    @InlineOnly
    private static final long minus-VKZWuLQ(long arg0, long other) {
        return ULong.constructor-impl(arg0 - other);
    }

    @InlineOnly
    private static final long times-7apg3OU(long arg0, byte other) {
        long l = ULong.constructor-impl((long)other & 0xFFL);
        return ULong.constructor-impl(arg0 * l);
    }

    @InlineOnly
    private static final long times-xj2QHRw(long arg0, short other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFL);
        return ULong.constructor-impl(arg0 * l);
    }

    @InlineOnly
    private static final long times-WZ4Q5Ns(long arg0, int other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFFFFFL);
        return ULong.constructor-impl(arg0 * l);
    }

    @InlineOnly
    private static final long times-VKZWuLQ(long arg0, long other) {
        return ULong.constructor-impl(arg0 * other);
    }

    @InlineOnly
    private static final long div-7apg3OU(long arg0, byte other) {
        long l = ULong.constructor-impl((long)other & 0xFFL);
        return UnsignedKt.ulongDivide-eb3DHEI(arg0, l);
    }

    @InlineOnly
    private static final long div-xj2QHRw(long arg0, short other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFL);
        return UnsignedKt.ulongDivide-eb3DHEI(arg0, l);
    }

    @InlineOnly
    private static final long div-WZ4Q5Ns(long arg0, int other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFFFFFL);
        return UnsignedKt.ulongDivide-eb3DHEI(arg0, l);
    }

    @InlineOnly
    private static final long div-VKZWuLQ(long arg0, long other) {
        return UnsignedKt.ulongDivide-eb3DHEI(arg0, other);
    }

    @InlineOnly
    private static final long rem-7apg3OU(long arg0, byte other) {
        long l = ULong.constructor-impl((long)other & 0xFFL);
        return UnsignedKt.ulongRemainder-eb3DHEI(arg0, l);
    }

    @InlineOnly
    private static final long rem-xj2QHRw(long arg0, short other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFL);
        return UnsignedKt.ulongRemainder-eb3DHEI(arg0, l);
    }

    @InlineOnly
    private static final long rem-WZ4Q5Ns(long arg0, int other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFFFFFL);
        return UnsignedKt.ulongRemainder-eb3DHEI(arg0, l);
    }

    @InlineOnly
    private static final long rem-VKZWuLQ(long arg0, long other) {
        return UnsignedKt.ulongRemainder-eb3DHEI(arg0, other);
    }

    @InlineOnly
    private static final long floorDiv-7apg3OU(long arg0, byte other) {
        long l = ULong.constructor-impl((long)other & 0xFFL);
        return UnsignedKt.ulongDivide-eb3DHEI(arg0, l);
    }

    @InlineOnly
    private static final long floorDiv-xj2QHRw(long arg0, short other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFL);
        return UnsignedKt.ulongDivide-eb3DHEI(arg0, l);
    }

    @InlineOnly
    private static final long floorDiv-WZ4Q5Ns(long arg0, int other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFFFFFL);
        return UnsignedKt.ulongDivide-eb3DHEI(arg0, l);
    }

    @InlineOnly
    private static final long floorDiv-VKZWuLQ(long arg0, long other) {
        return UnsignedKt.ulongDivide-eb3DHEI(arg0, other);
    }

    @InlineOnly
    private static final byte mod-7apg3OU(long arg0, byte other) {
        long l = ULong.constructor-impl((long)other & 0xFFL);
        l = UnsignedKt.ulongRemainder-eb3DHEI(arg0, l);
        return UByte.constructor-impl((byte)l);
    }

    @InlineOnly
    private static final short mod-xj2QHRw(long arg0, short other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFL);
        l = UnsignedKt.ulongRemainder-eb3DHEI(arg0, l);
        return UShort.constructor-impl((short)l);
    }

    @InlineOnly
    private static final int mod-WZ4Q5Ns(long arg0, int other) {
        long l = ULong.constructor-impl((long)other & 0xFFFFFFFFL);
        l = UnsignedKt.ulongRemainder-eb3DHEI(arg0, l);
        return UInt.constructor-impl((int)l);
    }

    @InlineOnly
    private static final long mod-VKZWuLQ(long arg0, long other) {
        return UnsignedKt.ulongRemainder-eb3DHEI(arg0, other);
    }

    @InlineOnly
    private static final long inc-s-VKNKU(long arg0) {
        return ULong.constructor-impl(arg0 + 1L);
    }

    @InlineOnly
    private static final long dec-s-VKNKU(long arg0) {
        return ULong.constructor-impl(arg0 + -1L);
    }

    @InlineOnly
    private static final ULongRange rangeTo-VKZWuLQ(long arg0, long other) {
        return new ULongRange(arg0, other, null);
    }

    @InlineOnly
    private static final long shl-s-VKNKU(long arg0, int bitCount) {
        return ULong.constructor-impl(arg0 << bitCount);
    }

    @InlineOnly
    private static final long shr-s-VKNKU(long arg0, int bitCount) {
        return ULong.constructor-impl(arg0 >>> bitCount);
    }

    @InlineOnly
    private static final long and-VKZWuLQ(long arg0, long other) {
        return ULong.constructor-impl(arg0 & other);
    }

    @InlineOnly
    private static final long or-VKZWuLQ(long arg0, long other) {
        return ULong.constructor-impl(arg0 | other);
    }

    @InlineOnly
    private static final long xor-VKZWuLQ(long arg0, long other) {
        return ULong.constructor-impl(arg0 ^ other);
    }

    @InlineOnly
    private static final long inv-s-VKNKU(long arg0) {
        return ULong.constructor-impl(arg0 ^ 0xFFFFFFFFFFFFFFFFL);
    }

    @InlineOnly
    private static final byte toByte-impl(long arg0) {
        return (byte)arg0;
    }

    @InlineOnly
    private static final short toShort-impl(long arg0) {
        return (short)arg0;
    }

    @InlineOnly
    private static final int toInt-impl(long arg0) {
        return (int)arg0;
    }

    @InlineOnly
    private static final long toLong-impl(long arg0) {
        return arg0;
    }

    @InlineOnly
    private static final byte toUByte-w2LRezQ(long arg0) {
        return UByte.constructor-impl((byte)arg0);
    }

    @InlineOnly
    private static final short toUShort-Mh2AYeg(long arg0) {
        return UShort.constructor-impl((short)arg0);
    }

    @InlineOnly
    private static final int toUInt-pVg5ArA(long arg0) {
        return UInt.constructor-impl((int)arg0);
    }

    @InlineOnly
    private static final long toULong-s-VKNKU(long arg0) {
        return arg0;
    }

    @InlineOnly
    private static final float toFloat-impl(long arg0) {
        return (float)UnsignedKt.ulongToDouble(arg0);
    }

    @InlineOnly
    private static final double toDouble-impl(long arg0) {
        return UnsignedKt.ulongToDouble(arg0);
    }

    @NotNull
    public static String toString-impl(long arg0) {
        return UnsignedKt.ulongToString(arg0);
    }

    @NotNull
    public String toString() {
        return ULong.toString-impl(this.data);
    }

    public static int hashCode-impl(long arg0) {
        long l = arg0;
        return (int)(l ^ l >>> 32);
    }

    public int hashCode() {
        return ULong.hashCode-impl(this.data);
    }

    public static boolean equals-impl(long arg0, Object other) {
        if (!(other instanceof ULong)) {
            return false;
        }
        long l = ((ULong)other).unbox-impl();
        return arg0 == l;
    }

    public boolean equals(Object other) {
        return ULong.equals-impl(this.data, other);
    }

    @PublishedApi
    private /* synthetic */ ULong(long data) {
        this.data = data;
    }

    @PublishedApi
    public static long constructor-impl(long data) {
        long l = data;
        return l;
    }

    public static final /* synthetic */ ULong box-impl(long v) {
        return new ULong(v);
    }

    public final /* synthetic */ long unbox-impl() {
        return this.data;
    }

    public static final boolean equals-impl0(long p1, long p2) {
        return p1 == p2;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\u00020\u0004X\u0086T\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u0016\u0010\u0006\u001a\u00020\u0004X\u0086T\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\n"}, d2={"Lkotlin/ULong$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/ULong;", "J", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

