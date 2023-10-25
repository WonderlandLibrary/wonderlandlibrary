/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.IntPixelAccessor;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.IntToIntPixelConverter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.BaseIntToByteConverter;
import com.sun.javafx.image.impl.BaseIntToIntConverter;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.javafx.image.impl.IntTo4ByteSameConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class IntArgb {
    public static final IntPixelGetter getter = Accessor.instance;
    public static final IntPixelSetter setter = Accessor.instance;
    public static final IntPixelAccessor accessor = Accessor.instance;
    private static IntToBytePixelConverter ToByteBgraObj;
    private static IntToIntPixelConverter ToIntArgbObj;

    public static IntToBytePixelConverter ToByteBgraConverter() {
        if (ToByteBgraObj == null) {
            ToByteBgraObj = new IntTo4ByteSameConverter(getter, ByteBgra.setter);
        }
        return ToByteBgraObj;
    }

    public static IntToBytePixelConverter ToByteBgraPreConverter() {
        return ToByteBgraPreConv.instance;
    }

    public static IntToIntPixelConverter ToIntArgbConverter() {
        if (ToIntArgbObj == null) {
            ToIntArgbObj = BaseIntToIntConverter.create(accessor);
        }
        return ToIntArgbObj;
    }

    public static IntToIntPixelConverter ToIntArgbPreConverter() {
        return ToIntArgbPreConv.instance;
    }

    static class ToByteBgraPreConv
    extends BaseIntToByteConverter {
        public static final IntToBytePixelConverter instance = new ToByteBgraPreConv();

        private ToByteBgraPreConv() {
            super(getter, ByteBgraPre.setter);
        }

        @Override
        void doConvert(int[] arrn, int n, int n2, byte[] arrby, int n3, int n4, int n5, int n6) {
            n2 -= n5;
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7 = arrn[n++];
                    int n8 = n7 >>> 24;
                    int n9 = n7 >> 16;
                    int n10 = n7 >> 8;
                    int n11 = n7;
                    if (n8 < 255) {
                        if (n8 == 0) {
                            n9 = 0;
                            n10 = 0;
                            n11 = 0;
                        } else {
                            n11 = ((n11 & 0xFF) * n8 + 127) / 255;
                            n10 = ((n10 & 0xFF) * n8 + 127) / 255;
                            n9 = ((n9 & 0xFF) * n8 + 127) / 255;
                        }
                    }
                    arrby[n3++] = (byte)n11;
                    arrby[n3++] = (byte)n10;
                    arrby[n3++] = (byte)n9;
                    arrby[n3++] = (byte)n8;
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(IntBuffer intBuffer, int n, int n2, ByteBuffer byteBuffer, int n3, int n4, int n5, int n6) {
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7 = intBuffer.get(n + i);
                    int n8 = n7 >>> 24;
                    int n9 = n7 >> 16;
                    int n10 = n7 >> 8;
                    int n11 = n7;
                    if (n8 < 255) {
                        if (n8 == 0) {
                            n9 = 0;
                            n10 = 0;
                            n11 = 0;
                        } else {
                            n11 = ((n11 & 0xFF) * n8 + 127) / 255;
                            n10 = ((n10 & 0xFF) * n8 + 127) / 255;
                            n9 = ((n9 & 0xFF) * n8 + 127) / 255;
                        }
                    }
                    byteBuffer.put(n3, (byte)n11);
                    byteBuffer.put(n3 + 1, (byte)n10);
                    byteBuffer.put(n3 + 2, (byte)n9);
                    byteBuffer.put(n3 + 3, (byte)n8);
                    n3 += 4;
                }
                n += n2;
                n3 += n4;
            }
        }
    }

    public static class ToIntArgbPreConv
    extends BaseIntToIntConverter {
        public static final IntToIntPixelConverter instance = new ToIntArgbPreConv();

        private ToIntArgbPreConv() {
            super(getter, IntArgbPre.setter);
        }

        @Override
        void doConvert(int[] arrn, int n, int n2, int[] arrn2, int n3, int n4, int n5, int n6) {
            n2 -= n5;
            n4 -= n5;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7;
                    int n8;
                    if ((n8 = (n7 = arrn[n++]) >>> 24) < 255) {
                        if (n8 == 0) {
                            n7 = 0;
                        } else {
                            int n9 = ((n7 >> 16 & 0xFF) * n8 + 127) / 255;
                            int n10 = ((n7 >> 8 & 0xFF) * n8 + 127) / 255;
                            int n11 = ((n7 & 0xFF) * n8 + 127) / 255;
                            n7 = n8 << 24 | n9 << 16 | n10 << 8 | n11;
                        }
                    }
                    arrn2[n3++] = n7;
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(IntBuffer intBuffer, int n, int n2, IntBuffer intBuffer2, int n3, int n4, int n5, int n6) {
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7 = intBuffer.get(n + i);
                    int n8 = n7 >>> 24;
                    if (n8 < 255) {
                        if (n8 == 0) {
                            n7 = 0;
                        } else {
                            int n9 = ((n7 >> 16 & 0xFF) * n8 + 127) / 255;
                            int n10 = ((n7 >> 8 & 0xFF) * n8 + 127) / 255;
                            int n11 = ((n7 & 0xFF) * n8 + 127) / 255;
                            n7 = n8 << 24 | n9 << 16 | n10 << 8 | n11;
                        }
                    }
                    intBuffer2.put(n3 + i, n7);
                }
                n += n2;
                n3 += n4;
            }
        }
    }

    static class Accessor
    implements IntPixelAccessor {
        static final IntPixelAccessor instance = new Accessor();

        private Accessor() {
        }

        @Override
        public AlphaType getAlphaType() {
            return AlphaType.NONPREMULTIPLIED;
        }

        @Override
        public int getNumElements() {
            return 1;
        }

        @Override
        public int getArgb(int[] arrn, int n) {
            return arrn[n];
        }

        @Override
        public int getArgbPre(int[] arrn, int n) {
            return PixelUtils.NonPretoPre(arrn[n]);
        }

        @Override
        public int getArgb(IntBuffer intBuffer, int n) {
            return intBuffer.get(n);
        }

        @Override
        public int getArgbPre(IntBuffer intBuffer, int n) {
            return PixelUtils.NonPretoPre(intBuffer.get(n));
        }

        @Override
        public void setArgb(int[] arrn, int n, int n2) {
            arrn[n] = n2;
        }

        @Override
        public void setArgbPre(int[] arrn, int n, int n2) {
            arrn[n] = PixelUtils.PretoNonPre(n2);
        }

        @Override
        public void setArgb(IntBuffer intBuffer, int n, int n2) {
            intBuffer.put(n, n2);
        }

        @Override
        public void setArgbPre(IntBuffer intBuffer, int n, int n2) {
            intBuffer.put(n, PixelUtils.PretoNonPre(n2));
        }
    }
}

