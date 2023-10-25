/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.BaseByteToByteConverter;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGrayAlphaPre;
import java.nio.ByteBuffer;

public class ByteGrayAlpha {
    public static final BytePixelGetter getter = Accessor.nonpremul;
    public static final BytePixelSetter setter = Accessor.nonpremul;
    public static final BytePixelAccessor accessor = Accessor.nonpremul;

    public static ByteToBytePixelConverter ToByteGrayAlphaPreConverter() {
        return ToByteGrayAlphaPreConv.instance;
    }

    public static ByteToBytePixelConverter ToByteBgraConverter() {
        return ToByteBgraSameConv.nonpremul;
    }

    static class ToByteGrayAlphaPreConv
    extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter instance = new ToByteGrayAlphaPreConv();

        private ToByteGrayAlphaPreConv() {
            super(getter, ByteGrayAlphaPre.setter);
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, byte[] arrby2, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 2;
            n4 -= n5 * 2;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    byte by;
                    int n7 = arrby[n++] & 0xFF;
                    if ((by = arrby[n++]) != -1) {
                        n7 = by == 0 ? 0 : (n7 * (by & 0xFF) + 127) / 255;
                    }
                    arrby2[n3++] = (byte)n7;
                    arrby2[n3++] = by;
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, ByteBuffer byteBuffer2, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 2;
            n4 -= n5 * 2;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    byte by;
                    int n7 = byteBuffer.get(n++) & 0xFF;
                    if ((by = byteBuffer.get(n++)) != -1) {
                        n7 = by == 0 ? 0 : (n7 * (by & 0xFF) + 127) / 255;
                    }
                    byteBuffer2.put(n3++, (byte)n7);
                    byteBuffer2.put(n3++, by);
                }
                n += n2;
                n3 += n4;
            }
        }
    }

    static class ToByteBgraSameConv
    extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter nonpremul = new ToByteBgraSameConv(false);
        static final ByteToBytePixelConverter premul = new ToByteBgraSameConv(true);

        private ToByteBgraSameConv(boolean bl) {
            super(bl ? ByteGrayAlphaPre.getter : getter, bl ? ByteBgraPre.setter : ByteBgra.setter);
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, byte[] arrby2, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 2;
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    byte by = arrby[n++];
                    byte by2 = arrby[n++];
                    arrby2[n3++] = by;
                    arrby2[n3++] = by;
                    arrby2[n3++] = by;
                    arrby2[n3++] = by2;
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, ByteBuffer byteBuffer2, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 2;
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    byte by = byteBuffer.get(n++);
                    byte by2 = byteBuffer.get(n++);
                    byteBuffer2.put(n3++, by);
                    byteBuffer2.put(n3++, by);
                    byteBuffer2.put(n3++, by);
                    byteBuffer2.put(n3++, by2);
                }
                n += n2;
                n3 += n4;
            }
        }
    }

    static class Accessor
    implements BytePixelAccessor {
        static final BytePixelAccessor nonpremul = new Accessor(false);
        static final BytePixelAccessor premul = new Accessor(true);
        private boolean isPremult;

        private Accessor(boolean bl) {
            this.isPremult = bl;
        }

        @Override
        public AlphaType getAlphaType() {
            return this.isPremult ? AlphaType.PREMULTIPLIED : AlphaType.NONPREMULTIPLIED;
        }

        @Override
        public int getNumElements() {
            return 2;
        }

        @Override
        public int getArgb(byte[] arrby, int n) {
            int n2 = arrby[n] & 0xFF;
            int n3 = arrby[n + 1] & 0xFF;
            if (this.isPremult) {
                n2 = PixelUtils.PreToNonPre(n2, n3);
            }
            return n3 << 24 | n2 << 16 | n2 << 8 | n2;
        }

        @Override
        public int getArgbPre(byte[] arrby, int n) {
            int n2 = arrby[n] & 0xFF;
            int n3 = arrby[n + 1] & 0xFF;
            if (!this.isPremult) {
                n2 = PixelUtils.NonPretoPre(n2, n3);
            }
            return n3 << 24 | n2 << 16 | n2 << 8 | n2;
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n) {
            int n2 = byteBuffer.get(n) & 0xFF;
            int n3 = byteBuffer.get(n + 1) & 0xFF;
            if (this.isPremult) {
                n2 = PixelUtils.PreToNonPre(n2, n3);
            }
            return n3 << 24 | n2 << 16 | n2 << 8 | n2;
        }

        @Override
        public int getArgbPre(ByteBuffer byteBuffer, int n) {
            int n2 = byteBuffer.get(n) & 0xFF;
            int n3 = byteBuffer.get(n + 1) & 0xFF;
            if (!this.isPremult) {
                n2 = PixelUtils.NonPretoPre(n2, n3);
            }
            return n3 << 24 | n2 << 16 | n2 << 8 | n2;
        }

        @Override
        public void setArgb(byte[] arrby, int n, int n2) {
            int n3 = PixelUtils.RgbToGray(n2);
            int n4 = n2 >>> 24;
            if (this.isPremult) {
                n3 = PixelUtils.NonPretoPre(n3, n4);
            }
            arrby[n] = (byte)n3;
            arrby[n + 1] = (byte)n4;
        }

        @Override
        public void setArgbPre(byte[] arrby, int n, int n2) {
            int n3 = PixelUtils.RgbToGray(n2);
            int n4 = n2 >>> 24;
            if (!this.isPremult) {
                n3 = PixelUtils.PreToNonPre(n3, n4);
            }
            arrby[n] = (byte)n3;
            arrby[n + 1] = (byte)n4;
        }

        @Override
        public void setArgb(ByteBuffer byteBuffer, int n, int n2) {
            int n3 = PixelUtils.RgbToGray(n2);
            int n4 = n2 >>> 24;
            if (this.isPremult) {
                n3 = PixelUtils.NonPretoPre(n3, n4);
            }
            byteBuffer.put(n, (byte)n3);
            byteBuffer.put(n + 1, (byte)n4);
        }

        @Override
        public void setArgbPre(ByteBuffer byteBuffer, int n, int n2) {
            int n3 = PixelUtils.RgbToGray(n2);
            int n4 = n2 >>> 24;
            if (!this.isPremult) {
                n3 = PixelUtils.PreToNonPre(n3, n4);
            }
            byteBuffer.put(n, (byte)n3);
            byteBuffer.put(n + 1, (byte)n4);
        }
    }
}

