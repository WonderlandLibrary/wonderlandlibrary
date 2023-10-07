/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.io;

import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.internal.InlineOnly;
import kotlin.io.ConsoleKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference0Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0005\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\rH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000eH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000fH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0010H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0011H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0012H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0013H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0014H\u0087\b\u001a\t\u0010\u0015\u001a\u00020\nH\u0087\b\u001a\u0013\u0010\u0015\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\rH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000eH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000fH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0010H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0011H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0012H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0013H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0014H\u0087\b\u001a\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017\u001a\u001a\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0003\u001a\u00020\u0004H\u0000\u001a\f\u0010\u001a\u001a\u00020\r*\u00020\u001bH\u0002\u001a\f\u0010\u001c\u001a\u00020\n*\u00020\u001dH\u0002\u001a\u0018\u0010\u001e\u001a\u00020\n*\u00020\u001b2\n\u0010\u001f\u001a\u00060 j\u0002`!H\u0002\u001a$\u0010\"\u001a\u00020\r*\u00020\u00042\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u001b2\u0006\u0010&\u001a\u00020\rH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006'"}, d2={"BUFFER_SIZE", "", "LINE_SEPARATOR_MAX_LENGTH", "decoder", "Ljava/nio/charset/CharsetDecoder;", "getDecoder", "()Ljava/nio/charset/CharsetDecoder;", "decoder$delegate", "Lkotlin/Lazy;", "print", "", "message", "", "", "", "", "", "", "", "", "", "println", "readLine", "", "inputStream", "Ljava/io/InputStream;", "endsWithLineSeparator", "Ljava/nio/CharBuffer;", "flipBack", "Ljava/nio/Buffer;", "offloadPrefixTo", "builder", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "tryDecode", "byteBuffer", "Ljava/nio/ByteBuffer;", "charBuffer", "isEndOfStream", "kotlin-stdlib"})
@JvmName(name="ConsoleKt")
public final class ConsoleKt {
    static final /* synthetic */ KProperty[] $$delegatedProperties;
    private static final int BUFFER_SIZE = 32;
    private static final int LINE_SEPARATOR_MAX_LENGTH = 2;
    private static final Lazy decoder$delegate;

    static {
        $$delegatedProperties = new KProperty[]{Reflection.property0(new PropertyReference0Impl(Reflection.getOrCreateKotlinPackage(ConsoleKt.class, "kotlin-stdlib"), "decoder", "getDecoder()Ljava/nio/charset/CharsetDecoder;"))};
        decoder$delegate = LazyKt.lazy(decoder.2.INSTANCE);
    }

    @InlineOnly
    private static final void print(Object message) {
        int $i$f$print = 0;
        System.out.print(message);
    }

    @InlineOnly
    private static final void print(int message) {
        int $i$f$print = 0;
        System.out.print(message);
    }

    @InlineOnly
    private static final void print(long message) {
        int $i$f$print = 0;
        System.out.print(message);
    }

    @InlineOnly
    private static final void print(byte message) {
        int $i$f$print = 0;
        System.out.print((Object)message);
    }

    @InlineOnly
    private static final void print(short message) {
        int $i$f$print = 0;
        System.out.print((Object)message);
    }

    @InlineOnly
    private static final void print(char message) {
        int $i$f$print = 0;
        System.out.print(message);
    }

    @InlineOnly
    private static final void print(boolean message) {
        int $i$f$print = 0;
        System.out.print(message);
    }

    @InlineOnly
    private static final void print(float message) {
        int $i$f$print = 0;
        System.out.print(message);
    }

    @InlineOnly
    private static final void print(double message) {
        int $i$f$print = 0;
        System.out.print(message);
    }

    @InlineOnly
    private static final void print(char[] message) {
        int $i$f$print = 0;
        System.out.print(message);
    }

    @InlineOnly
    private static final void println(Object message) {
        int $i$f$println = 0;
        System.out.println(message);
    }

    @InlineOnly
    private static final void println(int message) {
        int $i$f$println = 0;
        System.out.println(message);
    }

    @InlineOnly
    private static final void println(long message) {
        int $i$f$println = 0;
        System.out.println(message);
    }

    @InlineOnly
    private static final void println(byte message) {
        int $i$f$println = 0;
        System.out.println((Object)message);
    }

    @InlineOnly
    private static final void println(short message) {
        int $i$f$println = 0;
        System.out.println((Object)message);
    }

    @InlineOnly
    private static final void println(char message) {
        int $i$f$println = 0;
        System.out.println(message);
    }

    @InlineOnly
    private static final void println(boolean message) {
        int $i$f$println = 0;
        System.out.println(message);
    }

    @InlineOnly
    private static final void println(float message) {
        int $i$f$println = 0;
        System.out.println(message);
    }

    @InlineOnly
    private static final void println(double message) {
        int $i$f$println = 0;
        System.out.println(message);
    }

    @InlineOnly
    private static final void println(char[] message) {
        int $i$f$println = 0;
        System.out.println(message);
    }

    @InlineOnly
    private static final void println() {
        int $i$f$println = 0;
        System.out.println();
    }

    private static final CharsetDecoder getDecoder() {
        Lazy lazy = decoder$delegate;
        Object var1_1 = null;
        KProperty kProperty = $$delegatedProperties[0];
        boolean bl = false;
        return (CharsetDecoder)lazy.getValue();
    }

    @Nullable
    public static final String readLine() {
        InputStream inputStream = System.in;
        Intrinsics.checkExpressionValueIsNotNull(inputStream, "System.`in`");
        return ConsoleKt.readLine(inputStream, ConsoleKt.getDecoder());
    }

    @Nullable
    public static final String readLine(@NotNull InputStream inputStream, @NotNull CharsetDecoder decoder2) {
        Intrinsics.checkParameterIsNotNull(inputStream, "inputStream");
        Intrinsics.checkParameterIsNotNull(decoder2, "decoder");
        boolean bl = decoder2.maxCharsPerByte() <= 1.0f;
        boolean bl2 = false;
        boolean bl3 = false;
        if (!bl) {
            boolean bl4 = false;
            String string = "Encodings with multiple chars per byte are not supported";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        CharBuffer charBuffer = CharBuffer.allocate(4);
        StringBuilder stringBuilder = new StringBuilder();
        int read = inputStream.read();
        if (read == -1) {
            return null;
        }
        do {
            byteBuffer.put((byte)read);
            ByteBuffer byteBuffer2 = byteBuffer;
            Intrinsics.checkExpressionValueIsNotNull(byteBuffer2, "byteBuffer");
            CharBuffer charBuffer2 = charBuffer;
            Intrinsics.checkExpressionValueIsNotNull(charBuffer2, "charBuffer");
            if (!ConsoleKt.tryDecode(decoder2, byteBuffer2, charBuffer2, false)) continue;
            if (ConsoleKt.endsWithLineSeparator(charBuffer)) break;
            if (charBuffer.remaining() >= 2) continue;
            ConsoleKt.offloadPrefixTo(charBuffer, stringBuilder);
        } while ((read = inputStream.read()) != -1);
        boolean bl5 = false;
        boolean bl6 = false;
        Object $this$with = decoder2;
        boolean bl7 = false;
        ConsoleKt.tryDecode((CharsetDecoder)$this$with, byteBuffer, charBuffer, true);
        ((CharsetDecoder)$this$with).reset();
        bl5 = false;
        bl6 = false;
        $this$with = charBuffer;
        boolean bl8 = false;
        int length = ((Buffer)$this$with).position();
        if (length > 0 && ((CharBuffer)$this$with).get(length - 1) == '\n' && --length > 0 && ((CharBuffer)$this$with).get(length - 1) == '\r') {
            --length;
        }
        ((CharBuffer)$this$with).flip();
        boolean bl9 = false;
        int n = 0;
        n = 0;
        int n2 = length;
        while (n < n2) {
            int it = n++;
            boolean bl10 = false;
            stringBuilder.append(((CharBuffer)$this$with).get());
        }
        return stringBuilder.toString();
    }

    private static final boolean tryDecode(@NotNull CharsetDecoder $this$tryDecode, ByteBuffer byteBuffer, CharBuffer charBuffer, boolean isEndOfStream) {
        int positionBefore = charBuffer.position();
        byteBuffer.flip();
        CoderResult coderResult = $this$tryDecode.decode(byteBuffer, charBuffer, isEndOfStream);
        boolean bl = false;
        boolean bl2 = false;
        CoderResult $this$with = coderResult;
        boolean bl3 = false;
        if ($this$with.isError()) {
            $this$with.throwException();
        }
        boolean bl4 = charBuffer.position() > positionBefore;
        bl = false;
        bl2 = false;
        boolean isDecoded = bl4;
        boolean bl5 = false;
        if (isDecoded) {
            byteBuffer.clear();
        } else {
            ConsoleKt.flipBack(byteBuffer);
        }
        return bl4;
    }

    private static final boolean endsWithLineSeparator(@NotNull CharBuffer $this$endsWithLineSeparator) {
        int p = $this$endsWithLineSeparator.position();
        return p > 0 && $this$endsWithLineSeparator.get(p - 1) == '\n';
    }

    private static final void flipBack(@NotNull Buffer $this$flipBack) {
        $this$flipBack.position($this$flipBack.limit());
        $this$flipBack.limit($this$flipBack.capacity());
    }

    private static final void offloadPrefixTo(@NotNull CharBuffer $this$offloadPrefixTo, StringBuilder builder) {
        $this$offloadPrefixTo.flip();
        int n = $this$offloadPrefixTo.limit() - 1;
        boolean bl = false;
        int n2 = 0;
        n2 = 0;
        int n3 = n;
        while (n2 < n3) {
            int it = n2++;
            boolean bl2 = false;
            builder.append($this$offloadPrefixTo.get());
        }
        $this$offloadPrefixTo.compact();
    }
}

