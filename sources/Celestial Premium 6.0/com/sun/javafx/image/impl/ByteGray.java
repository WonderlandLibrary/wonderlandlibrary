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
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.BaseByteToByteConverter;
import com.sun.javafx.image.impl.BaseByteToIntConverter;
import com.sun.javafx.image.impl.ByteBgr;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.IntArgb;
import com.sun.javafx.image.impl.IntArgbPre;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ByteGray {
    public static final BytePixelGetter getter = Accessor.instance;
    public static final BytePixelSetter setter = Accessor.instance;
    public static final BytePixelAccessor accessor = Accessor.instance;
    private static ByteToBytePixelConverter ToByteGrayObj;

    public static ByteToBytePixelConverter ToByteGrayConverter() {
        if (ToByteGrayObj == null) {
            ToByteGrayObj = BaseByteToByteConverter.create(accessor);
        }
        return ToByteGrayObj;
    }

    public static ByteToBytePixelConverter ToByteBgraConverter() {
        return ToByteBgrfConv.nonpremult;
    }

    public static ByteToBytePixelConverter ToByteBgraPreConverter() {
        return ToByteBgrfConv.premult;
    }

    public static ByteToIntPixelConverter ToIntArgbConverter() {
        return ToIntFrgbConv.nonpremult;
    }

    public static ByteToIntPixelConverter ToIntArgbPreConverter() {
        return ToIntFrgbConv.premult;
    }

    public static ByteToBytePixelConverter ToByteBgrConverter() {
        return ToByteRgbAnyConv.bgr;
    }

    static class ToByteBgrfConv
    extends BaseByteToByteConverter {
        public static final ByteToBytePixelConverter nonpremult = new ToByteBgrfConv(ByteBgra.setter);
        public static final ByteToBytePixelConverter premult = new ToByteBgrfConv(ByteBgraPre.setter);

        ToByteBgrfConv(BytePixelSetter bytePixelSetter) {
            super(getter, bytePixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, byte[] arrby2, int n3, int n4, int n5, int n6) {
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    byte by = arrby[n + i];
                    arrby2[n3++] = by;
                    arrby2[n3++] = by;
                    arrby2[n3++] = by;
                    arrby2[n3++] = -1;
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, ByteBuffer byteBuffer2, int n3, int n4, int n5, int n6) {
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    byte by = byteBuffer.get(n + i);
                    byteBuffer2.put(n3, by);
                    byteBuffer2.put(n3 + 1, by);
                    byteBuffer2.put(n3 + 2, by);
                    byteBuffer2.put(n3 + 3, (byte)-1);
                    n3 += 4;
                }
                n += n2;
                n3 += n4;
            }
        }
    }

    static class ToIntFrgbConv
    extends BaseByteToIntConverter {
        public static final ByteToIntPixelConverter nonpremult = new ToIntFrgbConv(IntArgb.setter);
        public static final ByteToIntPixelConverter premult = new ToIntFrgbConv(IntArgbPre.setter);

        private ToIntFrgbConv(IntPixelSetter intPixelSetter) {
            super(getter, intPixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, int[] arrn, int n3, int n4, int n5, int n6) {
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7 = arrby[n + i] & 0xFF;
                    arrn[n3 + i] = 0xFF000000 | n7 << 16 | n7 << 8 | n7;
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, IntBuffer intBuffer, int n3, int n4, int n5, int n6) {
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7 = byteBuffer.get(n + i) & 0xFF;
                    intBuffer.put(n3 + i, 0xFF000000 | n7 << 16 | n7 << 8 | n7);
                }
                n += n2;
                n3 += n4;
            }
        }
    }

    static class ToByteRgbAnyConv
    extends BaseByteToByteConverter {
        static ToByteRgbAnyConv bgr = new ToByteRgbAnyConv(ByteBgr.setter);

        private ToByteRgbAnyConv(BytePixelSetter bytePixelSetter) {
            super(getter, bytePixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, byte[] arrby2, int n3, int n4, int n5, int n6) {
            n4 -= n5 * 3;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7 = arrby[n + i] & 0xFF;
                    arrby2[n3++] = (byte)n7;
                    arrby2[n3++] = (byte)n7;
                    arrby2[n3++] = (byte)n7;
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, ByteBuffer byteBuffer2, int n3, int n4, int n5, int n6) {
            n4 -= n5 * 3;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7 = byteBuffer.get(n + i) & 0xFF;
                    byteBuffer2.put(n3++, (byte)n7);
                    byteBuffer2.put(n3++, (byte)n7);
                    byteBuffer2.put(n3++, (byte)n7);
                }
                n += n2;
                n3 += n4;
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
            return AlphaType.OPAQUE;
        }

        @Override
        public int getNumElements() {
            return 1;
        }

        @Override
        public int getArgb(byte[] arrby, int n) {
            int n2 = arrby[n] & 0xFF;
            return 0xFF000000 | n2 << 16 | n2 << 8 | n2;
        }

        @Override
        public int getArgbPre(byte[] arrby, int n) {
            int n2 = arrby[n] & 0xFF;
            return 0xFF000000 | n2 << 16 | n2 << 8 | n2;
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n) {
            int n2 = byteBuffer.get(n) & 0xFF;
            return 0xFF000000 | n2 << 16 | n2 << 8 | n2;
        }

        @Override
        public int getArgbPre(ByteBuffer byteBuffer, int n) {
            int n2 = byteBuffer.get(n) & 0xFF;
            return 0xFF000000 | n2 << 16 | n2 << 8 | n2;
        }

        @Override
        public void setArgb(byte[] arrby, int n, int n2) {
            arrby[n] = (byte)PixelUtils.RgbToGray(n2);
        }

        @Override
        public void setArgbPre(byte[] arrby, int n, int n2) {
            this.setArgb(arrby, n, PixelUtils.PretoNonPre(n2));
        }

        @Override
        public void setArgb(ByteBuffer byteBuffer, int n, int n2) {
            byteBuffer.put(n, (byte)PixelUtils.RgbToGray(n2));
        }

        @Override
        public void setArgbPre(ByteBuffer byteBuffer, int n, int n2) {
            this.setArgb(byteBuffer, n, PixelUtils.PretoNonPre(n2));
        }
    }
}

