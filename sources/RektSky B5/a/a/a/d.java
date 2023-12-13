/*
 * Decompiled with CFR 0.152.
 */
package a.a.a;

import a.a.a.a;
import a.a.a.e;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

public class d
extends a {
    protected float e;
    protected Kernel f;

    public d() {
        this(2.0f);
    }

    private d(float f2) {
        v0.e = f2 = 2.0f;
        float f3 = f2;
        int n2 = (int)Math.ceil(f3);
        int n3 = (n2 << 1) + 1;
        float[] fArray = new float[n3];
        float f4 = f3 / 3.0f;
        float f5 = f4 * 2.0f * f4;
        f4 = (float)Math.PI * 2 * f4;
        f4 = (float)Math.sqrt(f4);
        f3 *= f3;
        float f6 = 0.0f;
        int n4 = 0;
        int n5 = -n2;
        while (n5 <= n2) {
            float f7;
            float f8 = n5 * n5;
            fArray[n4] = f7 > f3 ? 0.0f : (float)Math.exp(-f8 / f5) / f4;
            f6 += fArray[n4];
            ++n4;
            ++n5;
        }
        n5 = 0;
        while (n5 < n3) {
            int n6 = n5++;
            fArray[n6] = fArray[n6] / f6;
        }
        this.f = new Kernel(n3, 1, fArray);
    }

    @Override
    public BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        int n2 = bufferedImage.getWidth();
        int n3 = bufferedImage.getHeight();
        if (bufferedImage2 == null) {
            bufferedImage2 = this.createCompatibleDestImage(bufferedImage, null);
        }
        int[] nArray = new int[n2 * n3];
        int[] nArray2 = new int[n2 * n3];
        bufferedImage.getRGB(0, 0, n2, n3, nArray, 0, n2);
        if (this.e > 0.0f) {
            a.a.a.d.a(this.f, nArray, nArray2, n2, n3, this.c, this.c && this.d, false, a);
            a.a.a.d.a(this.f, nArray2, nArray, n3, n2, this.c, false, this.c && this.d, a);
        }
        bufferedImage2.setRGB(0, 0, n2, n3, nArray, 0, n2);
        return bufferedImage2;
    }

    public static void a(Kernel kernel, int[] nArray, int[] nArray2, int n2, int n3, boolean bl, boolean bl2, boolean bl3, int n4) {
        float[] fArray = kernel.getKernelData(null);
        int n5 = kernel.getWidth();
        n5 /= 2;
        int n6 = 0;
        while (n6 < n3) {
            int n7 = n6;
            int n8 = n6 * n2;
            int n9 = 0;
            while (n9 < n2) {
                int n10;
                int n11;
                float f2 = 0.0f;
                float f3 = 0.0f;
                float f4 = 0.0f;
                float f5 = 0.0f;
                int n12 = -n5;
                while (n12 <= n5) {
                    float f6 = fArray[n5 + n12];
                    if (f6 != 0.0f) {
                        n11 = n9 + n12;
                        if (n11 < 0) {
                            if (n4 == a) {
                                n11 = 0;
                            } else if (n4 == b) {
                                n11 = (n9 + n2) % n2;
                            }
                        } else if (n11 >= n2) {
                            if (n4 == a) {
                                n11 = n2 - 1;
                            } else if (n4 == b) {
                                n11 = (n9 + n2) % n2;
                            }
                        }
                        n10 = nArray[n8 + n11];
                        n11 = n10 >>> 24;
                        int n13 = n10 >> 16 & 0xFF;
                        int n14 = n10 >> 8 & 0xFF;
                        n10 &= 0xFF;
                        if (bl2) {
                            float f7 = (float)n11 * 0.003921569f;
                            n13 = (int)((float)n13 * f7);
                            n14 = (int)((float)n14 * f7);
                            n10 = (int)((float)n10 * f7);
                        }
                        f5 += f6 * (float)n11;
                        f2 += f6 * (float)n13;
                        f3 += f6 * (float)n14;
                        f4 += f6 * (float)n10;
                    }
                    ++n12;
                }
                if (bl3 && f5 != 0.0f && f5 != 255.0f) {
                    float f8 = 255.0f / f5;
                    f2 *= f8;
                    f3 *= f8;
                    f4 *= f8;
                }
                n12 = bl ? a.a.a.e.a((int)((double)f5 + 0.5)) : 255;
                int n15 = a.a.a.e.a((int)((double)f2 + 0.5));
                n11 = a.a.a.e.a((int)((double)f3 + 0.5));
                n10 = a.a.a.e.a((int)((double)f4 + 0.5));
                nArray2[n7] = n12 << 24 | n15 << 16 | n11 << 8 | n10;
                n7 += n3;
                ++n9;
            }
            ++n6;
        }
    }

    @Override
    public String toString() {
        return "Blur/Gaussian Blur...";
    }
}

