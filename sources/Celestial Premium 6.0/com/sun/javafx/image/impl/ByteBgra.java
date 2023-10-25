/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.BaseByteToByteConverter;
import com.sun.javafx.image.impl.BaseByteToIntConverter;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.IntArgb;
import com.sun.javafx.image.impl.IntArgbPre;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ByteBgra {
    public static final BytePixelGetter getter = Accessor.instance;
    public static final BytePixelSetter setter = Accessor.instance;
    public static final BytePixelAccessor accessor = Accessor.instance;
    private static ByteToBytePixelConverter ToByteBgraConv;

    public static ByteToBytePixelConverter ToByteBgraConverter() {
        if (ToByteBgraConv == null) {
            ToByteBgraConv = BaseByteToByteConverter.create(accessor);
        }
        return ToByteBgraConv;
    }

    public static ByteToBytePixelConverter ToByteBgraPreConverter() {
        return ToByteBgraPreConv.instance;
    }

    public static ByteToIntPixelConverter ToIntArgbConverter() {
        return ToIntArgbSameConv.nonpremul;
    }

    public static ByteToIntPixelConverter ToIntArgbPreConverter() {
        return ToIntArgbPreConv.instance;
    }

    static class ToByteBgraPreConv
    extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter instance = new ToByteBgraPreConv();

        private ToByteBgraPreConv() {
            super(getter, ByteBgraPre.setter);
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, byte[] arrby2, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 4;
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7;
                    byte by = arrby[n++];
                    byte by2 = arrby[n++];
                    byte by3 = arrby[n++];
                    if ((n7 = arrby[n++] & 0xFF) < 255) {
                        if (n7 == 0) {
                            by3 = 0;
                            by2 = 0;
                            by = 0;
                        } else {
                            by = (byte)(((by & 0xFF) * n7 + 127) / 255);
                            by2 = (byte)(((by2 & 0xFF) * n7 + 127) / 255);
                            by3 = (byte)(((by3 & 0xFF) * n7 + 127) / 255);
                        }
                    }
                    arrby2[n3++] = by;
                    arrby2[n3++] = by2;
                    arrby2[n3++] = by3;
                    arrby2[n3++] = (byte)n7;
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, ByteBuffer byteBuffer2, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 4;
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    byte by = byteBuffer.get(n);
                    byte by2 = byteBuffer.get(n + 1);
                    byte by3 = byteBuffer.get(n + 2);
                    int n7 = byteBuffer.get(n + 3) & 0xFF;
                    n += 4;
                    if (n7 < 255) {
                        if (n7 == 0) {
                            by3 = 0;
                            by2 = 0;
                            by = 0;
                        } else {
                            by = (byte)(((by & 0xFF) * n7 + 127) / 255);
                            by2 = (byte)(((by2 & 0xFF) * n7 + 127) / 255);
                            by3 = (byte)(((by3 & 0xFF) * n7 + 127) / 255);
                        }
                    }
                    byteBuffer2.put(n3, by);
                    byteBuffer2.put(n3 + 1, by2);
                    byteBuffer2.put(n3 + 2, by3);
                    byteBuffer2.put(n3 + 3, (byte)n7);
                    n3 += 4;
                }
                n += n2;
                n3 += n4;
            }
        }
    }

    static class ToIntArgbSameConv
    extends BaseByteToIntConverter {
        static final ByteToIntPixelConverter nonpremul = new ToIntArgbSameConv(false);
        static final ByteToIntPixelConverter premul = new ToIntArgbSameConv(true);

        private ToIntArgbSameConv(boolean bl) {
            super(bl ? ByteBgraPre.getter : getter, bl ? IntArgbPre.setter : IntArgb.setter);
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, int[] arrn, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 4;
            n4 -= n5;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    arrn[n3++] = arrby[n++] & 0xFF | (arrby[n++] & 0xFF) << 8 | (arrby[n++] & 0xFF) << 16 | arrby[n++] << 24;
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, IntBuffer intBuffer, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    intBuffer.put(n3 + i, byteBuffer.get(n) & 0xFF | (byteBuffer.get(n + 1) & 0xFF) << 8 | (byteBuffer.get(n + 2) & 0xFF) << 16 | byteBuffer.get(n + 3) << 24);
                    n += 4;
                }
                n += n2;
                n3 += n4;
            }
        }
    }

    static class ToIntArgbPreConv
    extends BaseByteToIntConverter {
        public static final ByteToIntPixelConverter instance = new ToIntArgbPreConv();

        private ToIntArgbPreConv() {
            super(getter, IntArgbPre.setter);
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, int[] arrn, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 4;
            n4 -= n5;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7;
                    int n8 = arrby[n++] & 0xFF;
                    int n9 = arrby[n++] & 0xFF;
                    int n10 = arrby[n++] & 0xFF;
                    if ((n7 = arrby[n++] & 0xFF) < 255) {
                        if (n7 == 0) {
                            n10 = 0;
                            n9 = 0;
                            n8 = 0;
                        } else {
                            n8 = (n8 * n7 + 127) / 255;
                            n9 = (n9 * n7 + 127) / 255;
                            n10 = (n10 * n7 + 127) / 255;
                        }
                    }
                    arrn[n3++] = n7 << 24 | n10 << 16 | n9 << 8 | n8;
                }
                n3 += n4;
                n += n2;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, IntBuffer intBuffer, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7 = byteBuffer.get(n) & 0xFF;
                    int n8 = byteBuffer.get(n + 1) & 0xFF;
                    int n9 = byteBuffer.get(n + 2) & 0xFF;
                    int n10 = byteBuffer.get(n + 3) & 0xFF;
                    n += 4;
                    if (n10 < 255) {
                        if (n10 == 0) {
                            n9 = 0;
                            n8 = 0;
                            n7 = 0;
                        } else {
                            n7 = (n7 * n10 + 127) / 255;
                            n8 = (n8 * n10 + 127) / 255;
                            n9 = (n9 * n10 + 127) / 255;
                        }
                    }
                    intBuffer.put(n3 + i, n10 << 24 | n9 << 16 | n8 << 8 | n7);
                }
                n3 += n4;
                n += n2;
            }
        }
    }

    static class Accessor
    implements BytePixelAccessor {
        static final BytePixelAccessor instance = new Accessor();

        private Accessor() {
        }

        @Override
        public AlphaType getAlphaType() {
            return AlphaType.NONPREMULTIPLIED;
        }

        @Override
        public int getNumElements() {
            return 4;
        }

        @Override
        public int getArgb(byte[] arrby, int n) {
            return arrby[n] & 0xFF | (arrby[n + 1] & 0xFF) << 8 | (arrby[n + 2] & 0xFF) << 16 | arrby[n + 3] << 24;
        }

        @Override
        public int getArgbPre(byte[] arrby, int n) {
            return PixelUtils.NonPretoPre(this.getArgb(arrby, n));
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n) {
            return byteBuffer.get(n) & 0xFF | (byteBuffer.get(n + 1) & 0xFF) << 8 | (byteBuffer.get(n + 2) & 0xFF) << 16 | byteBuffer.get(n + 3) << 24;
        }

        @Override
        public int getArgbPre(ByteBuffer byteBuffer, int n) {
            return PixelUtils.NonPretoPre(this.getArgb(byteBuffer, n));
        }

        @Override
        public void setArgb(byte[] arrby, int n, int n2) {
            arrby[n] = (byte)n2;
            arrby[n + 1] = (byte)(n2 >> 8);
            arrby[n + 2] = (byte)(n2 >> 16);
            arrby[n + 3] = (byte)(n2 >> 24);
        }

        @Override
        public void setArgbPre(byte[] arrby, int n, int n2) {
            this.setArgb(arrby, n, PixelUtils.PretoNonPre(n2));
        }

        @Override
        public void setArgb(ByteBuffer byteBuffer, int n, int n2) {
            byteBuffer.put(n, (byte)n2);
            byteBuffer.put(n + 1, (byte)(n2 >> 8));
            byteBuffer.put(n + 2, (byte)(n2 >> 16));
            byteBuffer.put(n + 3, (byte)(n2 >> 24));
        }

        @Override
        public void setArgbPre(ByteBuffer byteBuffer, int n, int n2) {
            this.setArgb(byteBuffer, n, PixelUtils.PretoNonPre(n2));
        }
    }
}

