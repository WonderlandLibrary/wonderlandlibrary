/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.base64;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64Dialect;

public final class Base64 {
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte EQUALS_SIGN = 61;
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final byte WHITE_SPACE_ENC = -5;
    private static final byte NEW_LINE = 10;

    public static ByteBuf encode(ByteBuf byteBuf, int n, int n2) {
        return Base64.encode(byteBuf, n, n2, Base64Dialect.STANDARD);
    }

    private Base64() {
    }

    public static ByteBuf encode(ByteBuf byteBuf, Base64Dialect base64Dialect) {
        return Base64.encode(byteBuf, Base64.breakLines(base64Dialect), base64Dialect);
    }

    public static ByteBuf decode(ByteBuf byteBuf, Base64Dialect base64Dialect) {
        if (byteBuf == null) {
            throw new NullPointerException("src");
        }
        ByteBuf byteBuf2 = Base64.decode(byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes(), base64Dialect);
        byteBuf.readerIndex(byteBuf.writerIndex());
        return byteBuf2;
    }

    public static ByteBuf encode(ByteBuf byteBuf) {
        return Base64.encode(byteBuf, Base64Dialect.STANDARD);
    }

    private static boolean breakLines(Base64Dialect base64Dialect) {
        if (base64Dialect == null) {
            throw new NullPointerException("dialect");
        }
        return base64Dialect.breakLinesByDefault;
    }

    public static ByteBuf decode(ByteBuf byteBuf, int n, int n2) {
        return Base64.decode(byteBuf, n, n2, Base64Dialect.STANDARD);
    }

    public static ByteBuf decode(ByteBuf byteBuf) {
        return Base64.decode(byteBuf, Base64Dialect.STANDARD);
    }

    private static byte[] decodabet(Base64Dialect base64Dialect) {
        if (base64Dialect == null) {
            throw new NullPointerException("dialect");
        }
        return base64Dialect.decodabet;
    }

    private static void encode3to4(ByteBuf byteBuf, int n, int n2, ByteBuf byteBuf2, int n3, Base64Dialect base64Dialect) {
        byte[] byArray = Base64.alphabet(base64Dialect);
        int n4 = (n2 > 0 ? byteBuf.getByte(n) << 24 >>> 8 : 0) | (n2 > 1 ? byteBuf.getByte(n + 1) << 24 >>> 16 : 0) | (n2 > 2 ? byteBuf.getByte(n + 2) << 24 >>> 24 : 0);
        switch (n2) {
            case 3: {
                byteBuf2.setByte(n3, byArray[n4 >>> 18]);
                byteBuf2.setByte(n3 + 1, byArray[n4 >>> 12 & 0x3F]);
                byteBuf2.setByte(n3 + 2, byArray[n4 >>> 6 & 0x3F]);
                byteBuf2.setByte(n3 + 3, byArray[n4 & 0x3F]);
                break;
            }
            case 2: {
                byteBuf2.setByte(n3, byArray[n4 >>> 18]);
                byteBuf2.setByte(n3 + 1, byArray[n4 >>> 12 & 0x3F]);
                byteBuf2.setByte(n3 + 2, byArray[n4 >>> 6 & 0x3F]);
                byteBuf2.setByte(n3 + 3, 61);
                break;
            }
            case 1: {
                byteBuf2.setByte(n3, byArray[n4 >>> 18]);
                byteBuf2.setByte(n3 + 1, byArray[n4 >>> 12 & 0x3F]);
                byteBuf2.setByte(n3 + 2, 61);
                byteBuf2.setByte(n3 + 3, 61);
            }
        }
    }

    private static byte[] alphabet(Base64Dialect base64Dialect) {
        if (base64Dialect == null) {
            throw new NullPointerException("dialect");
        }
        return base64Dialect.alphabet;
    }

    private static int decode4to3(byte[] byArray, int n, ByteBuf byteBuf, int n2, Base64Dialect base64Dialect) {
        int n3;
        byte[] byArray2 = Base64.decodabet(base64Dialect);
        if (byArray[n + 2] == 61) {
            int n4 = (byArray2[byArray[n]] & 0xFF) << 18 | (byArray2[byArray[n + 1]] & 0xFF) << 12;
            byteBuf.setByte(n2, (byte)(n4 >>> 16));
            return 1;
        }
        if (byArray[n + 3] == 61) {
            int n5 = (byArray2[byArray[n]] & 0xFF) << 18 | (byArray2[byArray[n + 1]] & 0xFF) << 12 | (byArray2[byArray[n + 2]] & 0xFF) << 6;
            byteBuf.setByte(n2, (byte)(n5 >>> 16));
            byteBuf.setByte(n2 + 1, (byte)(n5 >>> 8));
            return 2;
        }
        try {
            n3 = (byArray2[byArray[n]] & 0xFF) << 18 | (byArray2[byArray[n + 1]] & 0xFF) << 12 | (byArray2[byArray[n + 2]] & 0xFF) << 6 | byArray2[byArray[n + 3]] & 0xFF;
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw new IllegalArgumentException("not encoded in Base64");
        }
        byteBuf.setByte(n2, (byte)(n3 >> 16));
        byteBuf.setByte(n2 + 1, (byte)(n3 >> 8));
        byteBuf.setByte(n2 + 2, (byte)n3);
        return 3;
    }

    public static ByteBuf encode(ByteBuf byteBuf, boolean bl, Base64Dialect base64Dialect) {
        if (byteBuf == null) {
            throw new NullPointerException("src");
        }
        ByteBuf byteBuf2 = Base64.encode(byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes(), bl, base64Dialect);
        byteBuf.readerIndex(byteBuf.writerIndex());
        return byteBuf2;
    }

    public static ByteBuf encode(ByteBuf byteBuf, int n, int n2, boolean bl) {
        return Base64.encode(byteBuf, n, n2, bl, Base64Dialect.STANDARD);
    }

    public static ByteBuf encode(ByteBuf byteBuf, int n, int n2, boolean bl, Base64Dialect base64Dialect) {
        if (byteBuf == null) {
            throw new NullPointerException("src");
        }
        if (base64Dialect == null) {
            throw new NullPointerException("dialect");
        }
        int n3 = n2 * 4 / 3;
        ByteBuf byteBuf2 = Unpooled.buffer(n3 + (n2 % 3 > 0 ? 4 : 0) + (bl ? n3 / 76 : 0)).order(byteBuf.order());
        int n4 = 0;
        int n5 = 0;
        int n6 = n2 - 2;
        int n7 = 0;
        while (n4 < n6) {
            Base64.encode3to4(byteBuf, n4 + n, 3, byteBuf2, n5, base64Dialect);
            if (bl && (n7 += 4) == 76) {
                byteBuf2.setByte(n5 + 4, 10);
                ++n5;
                n7 = 0;
            }
            n4 += 3;
            n5 += 4;
        }
        if (n4 < n2) {
            Base64.encode3to4(byteBuf, n4 + n, n2 - n4, byteBuf2, n5, base64Dialect);
            n5 += 4;
        }
        return byteBuf2.slice(0, n5);
    }

    public static ByteBuf decode(ByteBuf byteBuf, int n, int n2, Base64Dialect base64Dialect) {
        if (byteBuf == null) {
            throw new NullPointerException("src");
        }
        if (base64Dialect == null) {
            throw new NullPointerException("dialect");
        }
        byte[] byArray = Base64.decodabet(base64Dialect);
        int n3 = n2 * 3 / 4;
        ByteBuf byteBuf2 = byteBuf.alloc().buffer(n3).order(byteBuf.order());
        int n4 = 0;
        byte[] byArray2 = new byte[4];
        int n5 = 0;
        for (int i = n; i < n + n2; ++i) {
            byte by = (byte)(byteBuf.getByte(i) & 0x7F);
            byte by2 = byArray[by];
            if (by2 >= -5) {
                if (by2 < -1) continue;
                byArray2[n5++] = by;
                if (n5 <= 3) continue;
                n4 += Base64.decode4to3(byArray2, 0, byteBuf2, n4, base64Dialect);
                n5 = 0;
                if (by != 61) continue;
                break;
            }
            throw new IllegalArgumentException("bad Base64 input character at " + i + ": " + byteBuf.getUnsignedByte(i) + " (decimal)");
        }
        return byteBuf2.slice(0, n4);
    }

    public static ByteBuf encode(ByteBuf byteBuf, int n, int n2, Base64Dialect base64Dialect) {
        return Base64.encode(byteBuf, n, n2, Base64.breakLines(base64Dialect), base64Dialect);
    }

    public static ByteBuf encode(ByteBuf byteBuf, boolean bl) {
        return Base64.encode(byteBuf, bl, Base64Dialect.STANDARD);
    }
}

