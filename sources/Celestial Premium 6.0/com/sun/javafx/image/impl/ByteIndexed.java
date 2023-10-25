/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.image.PixelFormat
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.image.impl.BaseByteToByteConverter;
import com.sun.javafx.image.impl.BaseByteToIntConverter;
import com.sun.javafx.tk.Toolkit;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;

public class ByteIndexed {
    public static BytePixelGetter createGetter(PixelFormat<ByteBuffer> pixelFormat) {
        return new Getter(pixelFormat);
    }

    public static ByteToBytePixelConverter createToByteBgraAny(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter) {
        return new ToByteBgraAnyConverter(bytePixelGetter, bytePixelSetter);
    }

    public static ByteToIntPixelConverter createToIntArgbAny(BytePixelGetter bytePixelGetter, IntPixelSetter intPixelSetter) {
        return new ToIntArgbAnyConverter(bytePixelGetter, intPixelSetter);
    }

    static int[] getColors(BytePixelGetter bytePixelGetter, PixelSetter pixelSetter) {
        Getter getter = (Getter)bytePixelGetter;
        return pixelSetter.getAlphaType() == AlphaType.PREMULTIPLIED ? getter.getPreColors() : getter.getNonPreColors();
    }

    public static class Getter
    implements BytePixelGetter {
        PixelFormat<ByteBuffer> theFormat;
        private int[] precolors;
        private int[] nonprecolors;

        Getter(PixelFormat<ByteBuffer> pixelFormat) {
            this.theFormat = pixelFormat;
        }

        int[] getPreColors() {
            if (this.precolors == null) {
                this.precolors = Toolkit.getImageAccessor().getPreColors(this.theFormat);
            }
            return this.precolors;
        }

        int[] getNonPreColors() {
            if (this.nonprecolors == null) {
                this.nonprecolors = Toolkit.getImageAccessor().getNonPreColors(this.theFormat);
            }
            return this.nonprecolors;
        }

        @Override
        public AlphaType getAlphaType() {
            return this.theFormat.isPremultiplied() ? AlphaType.PREMULTIPLIED : AlphaType.NONPREMULTIPLIED;
        }

        @Override
        public int getNumElements() {
            return 1;
        }

        @Override
        public int getArgb(byte[] arrby, int n) {
            return this.getNonPreColors()[arrby[n] & 0xFF];
        }

        @Override
        public int getArgbPre(byte[] arrby, int n) {
            return this.getPreColors()[arrby[n] & 0xFF];
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n) {
            return this.getNonPreColors()[byteBuffer.get(n) & 0xFF];
        }

        @Override
        public int getArgbPre(ByteBuffer byteBuffer, int n) {
            return this.getPreColors()[byteBuffer.get(n) & 0xFF];
        }
    }

    public static class ToByteBgraAnyConverter
    extends BaseByteToByteConverter {
        public ToByteBgraAnyConverter(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter) {
            super(bytePixelGetter, bytePixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, byte[] arrby2, int n3, int n4, int n5, int n6) {
            int[] arrn = ByteIndexed.getColors(this.getGetter(), this.getSetter());
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7 = arrn[arrby[n + i] & 0xFF];
                    arrby2[n3++] = (byte)n7;
                    arrby2[n3++] = (byte)(n7 >> 8);
                    arrby2[n3++] = (byte)(n7 >> 16);
                    arrby2[n3++] = (byte)(n7 >> 24);
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, ByteBuffer byteBuffer2, int n3, int n4, int n5, int n6) {
            int[] arrn = ByteIndexed.getColors(this.getGetter(), this.getSetter());
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    int n7 = arrn[byteBuffer.get(n + i) & 0xFF];
                    byteBuffer2.put(n3, (byte)n7);
                    byteBuffer2.put(n3 + 1, (byte)(n7 >> 8));
                    byteBuffer2.put(n3 + 2, (byte)(n7 >> 16));
                    byteBuffer2.put(n3 + 3, (byte)(n7 >> 24));
                    n3 += 4;
                }
                n += n2;
                n3 += n4;
            }
        }
    }

    public static class ToIntArgbAnyConverter
    extends BaseByteToIntConverter {
        public ToIntArgbAnyConverter(BytePixelGetter bytePixelGetter, IntPixelSetter intPixelSetter) {
            super(bytePixelGetter, intPixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, int[] arrn, int n3, int n4, int n5, int n6) {
            int[] arrn2 = ByteIndexed.getColors(this.getGetter(), this.getSetter());
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    arrn[n3 + i] = arrn2[arrby[n + i] & 0xFF];
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, IntBuffer intBuffer, int n3, int n4, int n5, int n6) {
            int[] arrn = ByteIndexed.getColors(this.getGetter(), this.getSetter());
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    intBuffer.put(n3 + i, arrn[byteBuffer.get(n + i) & 0xFF]);
                }
                n += n2;
                n3 += n4;
            }
        }
    }
}

