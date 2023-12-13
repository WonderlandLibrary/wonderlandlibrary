/*
 * Decompiled with CFR 0.152.
 */
package a.a.a;

import a.a.a.c;
import a.a.a.e;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Kernel;

public class a
extends c {
    public static int a = 1;
    public static int b = 2;
    private Kernel e = null;
    protected boolean c = true;
    protected boolean d = true;
    private int f = a;

    public a() {
        this(new float[9]);
    }

    private a(float[] fArray) {
        this(new Kernel(3, 3, fArray));
    }

    private a(Kernel kernel) {
        this.e = kernel;
    }

    @Override
    public BufferedImage filter(BufferedImage object, BufferedImage bufferedImage) {
        int n2;
        int n3;
        int n4;
        int[] nArray;
        int n6;
        int n7;
        block37: {
            int n5;
            int n8;
            int n9;
            int n10;
            float f2;
            float f3;
            int n11;
            int n12;
            float[] fArray;
            int[] nArray2;
            int[] nArray3;
            int n14;
            Object object2;
            Object object3;
            int[] nArray4;
            int n42;
            int n13;
            Object object4;
            block38: {
                block36: {
                    n7 = ((BufferedImage)object).getWidth();
                    n6 = ((BufferedImage)object).getHeight();
                    if (bufferedImage == null) {
                        bufferedImage = this.createCompatibleDestImage((BufferedImage)object, null);
                    }
                    int[] nArray5 = new int[n7 * n6];
                    nArray = new int[n7 * n6];
                    int[] nArray6 = nArray5;
                    object4 = n6;
                    n4 = n7;
                    int n15 = 0;
                    int n16 = 0;
                    n13 = ((BufferedImage)object).getType();
                    int[] nArray7 = n13 == 2 || n13 == 1 ? (int[])((BufferedImage)object).getRaster().getDataElements(0, 0, n4, (int)object4, nArray6) : ((BufferedImage)object).getRGB(0, 0, n4, (int)object4, nArray6, 0, n4);
                    if (this.d) {
                        n15 = nArray5.length;
                        n16 = 0;
                        object = nArray5;
                        n4 = 0;
                        while (n4 < n15) {
                            Object object5 = object[n4];
                            object4 = object5;
                            reference var11_12 = object5 >>> 24;
                            n13 = object4 >> 16 & 0xFF;
                            n16 = object4 >> 8 & 0xFF;
                            object4 &= 0xFF;
                            float f4 = (float)var11_12 * 0.003921569f;
                            n13 = (int)((float)n13 * f4);
                            n16 = (int)((float)n16 * f4);
                            object4 = (int)((float)object4 * f4);
                            object[n4] = var11_12 << 24 | n13 << 16 | n16 << 8 | object4;
                            ++n4;
                        }
                    }
                    n13 = this.f;
                    n42 = this.c;
                    object4 = n6;
                    n4 = n7;
                    nArray4 = nArray;
                    object3 = nArray5;
                    object = this.e;
                    if (((Kernel)object).getHeight() != 1) break block36;
                    int n17 = n4;
                    n4 = n13;
                    n3 = n42;
                    object2 = object4;
                    n14 = n17;
                    nArray3 = nArray4;
                    nArray2 = object3;
                    object3 = object;
                    boolean nArray9 = false;
                    fArray = ((Kernel)object3).getKernelData(null);
                    n12 = ((Kernel)object3).getWidth();
                    n2 = n12 / 2;
                    n12 = 0;
                    while (n12 < object2) {
                        n11 = n12 * n14;
                        int n18 = 0;
                        while (n18 < n14) {
                            float f5 = 0.0f;
                            f3 = 0.0f;
                            f2 = 0.0f;
                            float f6 = 0.0f;
                            n10 = -n2;
                            while (n10 <= n2) {
                                float f7 = fArray[n2 + n10];
                                if (f7 != 0.0f) {
                                    n9 = n18 + n10;
                                    if (n9 < 0) {
                                        if (n4 == a) {
                                            n9 = 0;
                                        } else if (n4 == b) {
                                            n9 = (n18 + n14) % n14;
                                        }
                                    } else if (n9 >= n14) {
                                        if (n4 == a) {
                                            n9 = n14 - 1;
                                        } else if (n4 == b) {
                                            n9 = (n18 + n14) % n14;
                                        }
                                    }
                                    n8 = nArray2[n11 + n9];
                                    f6 += f7 * (float)(n8 >>> 24);
                                    f5 += f7 * (float)(n8 >> 16 & 0xFF);
                                    f3 += f7 * (float)(n8 >> 8 & 0xFF);
                                    f2 += f7 * (float)(n8 & 0xFF);
                                }
                                ++n10;
                            }
                            n10 = n3 != 0 ? a.a.a.e.a((int)((double)f6 + 0.5)) : 255;
                            n5 = a.a.a.e.a((int)((double)f5 + 0.5));
                            n9 = a.a.a.e.a((int)((double)f3 + 0.5));
                            n8 = a.a.a.e.a((int)((double)f2 + 0.5));
                            nArray3[++var11_15] = n10 << 24 | n5 << 16 | n9 << 8 | n8;
                            ++n18;
                        }
                        ++n12;
                    }
                    break block37;
                }
                if (((Kernel)object).getWidth() != 1) break block38;
                int n19 = n4;
                n4 = n13;
                n3 = n42;
                object2 = object4;
                n14 = n19;
                nArray3 = nArray4;
                nArray2 = object3;
                object3 = object;
                boolean bl = false;
                fArray = ((Kernel)object3).getKernelData(null);
                n12 = ((Kernel)object3).getHeight();
                n2 = n12 / 2;
                n12 = 0;
                while (n12 < object2) {
                    n11 = 0;
                    while (n11 < n14) {
                        int n20;
                        float f8 = 0.0f;
                        float f9 = 0.0f;
                        f3 = 0.0f;
                        f2 = 0.0f;
                        int n21 = -n2;
                        while (n21 <= n2) {
                            n20 = n12 + n21;
                            n10 = n20 < 0 ? (n4 == a ? 0 : (n4 == b ? (n12 + object2) % object2 * n14 : n20 * n14)) : (n20 >= object2 ? (n4 == a ? (object2 - 1) * n14 : (n4 == b ? (n12 + object2) % object2 * n14 : n20 * n14)) : n20 * n14);
                            float f10 = fArray[n21 + n2];
                            if (f10 != 0.0f) {
                                n9 = nArray2[n10 + n11];
                                f2 += f10 * (float)(n9 >>> 24);
                                f8 += f10 * (float)(n9 >> 16 & 0xFF);
                                f9 += f10 * (float)(n9 >> 8 & 0xFF);
                                f3 += f10 * (float)(n9 & 0xFF);
                            }
                            ++n21;
                        }
                        n21 = n3 != 0 ? a.a.a.e.a((int)((double)f2 + 0.5)) : 255;
                        n20 = a.a.a.e.a((int)((double)f8 + 0.5));
                        n10 = a.a.a.e.a((int)((double)f9 + 0.5));
                        n5 = a.a.a.e.a((int)((double)f3 + 0.5));
                        nArray3[++var11_17] = n21 << 24 | n20 << 16 | n10 << 8 | n5;
                        ++n11;
                    }
                    ++n12;
                }
                break block37;
            }
            int n22 = n4;
            n4 = n13;
            n3 = n42;
            object2 = object4;
            n14 = n22;
            nArray3 = nArray4;
            nArray2 = object3;
            object3 = object;
            boolean bl = false;
            fArray = ((Kernel)object3).getKernelData(null);
            n12 = ((Kernel)object3).getHeight();
            n2 = ((Kernel)object3).getWidth();
            n12 /= 2;
            n11 = n2 / 2;
            Object object6 = 0;
            while (object6 < object2) {
                int n23 = 0;
                while (n23 < n14) {
                    f3 = 0.0f;
                    f2 = 0.0f;
                    float f11 = 0.0f;
                    float f12 = 0.0f;
                    n10 = -n12;
                    while (n10 <= n12) {
                        block42: {
                            block40: {
                                block41: {
                                    block39: {
                                        n5 = object6 + n10;
                                        if (n5 < 0 || n5 >= object2) break block39;
                                        n9 = n5 * n14;
                                        break block40;
                                    }
                                    if (n4 != a) break block41;
                                    n9 = object6 * n14;
                                    break block40;
                                }
                                if (n4 != b) break block42;
                                n9 = (n5 + object2) % object2 * n14;
                            }
                            n8 = n2 * (n10 + n12) + n11;
                            n5 = -n11;
                            while (n5 <= n11) {
                                block43: {
                                    int n24;
                                    float f13;
                                    block44: {
                                        block45: {
                                            f13 = fArray[n8 + n5];
                                            if (f13 == 0.0f) break block43;
                                            n24 = n23 + n5;
                                            if (n24 >= 0 && n24 < n14) break block44;
                                            if (n4 != a) break block45;
                                            n24 = n23;
                                            break block44;
                                        }
                                        if (n4 != b) break block43;
                                        n24 = (n23 + n14) % n14;
                                    }
                                    n24 = nArray2[n9 + n24];
                                    f12 += f13 * (float)(n24 >>> 24);
                                    f3 += f13 * (float)(n24 >> 16 & 0xFF);
                                    f2 += f13 * (float)(n24 >> 8 & 0xFF);
                                    f11 += f13 * (float)(n24 & 0xFF);
                                }
                                ++n5;
                            }
                        }
                        ++n10;
                    }
                    n10 = n3 != 0 ? a.a.a.e.a((int)((double)f12 + 0.5)) : 255;
                    n5 = a.a.a.e.a((int)((double)f3 + 0.5));
                    n9 = a.a.a.e.a((int)((double)f2 + 0.5));
                    n8 = a.a.a.e.a((int)((double)f11 + 0.5));
                    nArray3[++var11_19] = n10 << 24 | n5 << 16 | n9 << 8 | n8;
                    ++n23;
                }
                ++object6;
            }
        }
        if (this.d) {
            n3 = nArray.length;
            n2 = 0;
            int[] nArray8 = nArray;
            n4 = 0;
            while (n4 < n3) {
                int n25 = nArray8[n4];
                int n26 = n25 >>> 24;
                int n27 = n25 >> 16 & 0xFF;
                n2 = n25 >> 8 & 0xFF;
                n25 &= 0xFF;
                if (n26 != 0 && n26 != 255) {
                    float f14 = 255.0f / (float)n26;
                    n27 = (int)((float)n27 * f14);
                    n2 = (int)((float)n2 * f14);
                    n25 = (int)((float)n25 * f14);
                    if (n27 > 255) {
                        n27 = 255;
                    }
                    if (n2 > 255) {
                        n2 = 255;
                    }
                    if (n25 > 255) {
                        n25 = 255;
                    }
                    nArray8[n4] = n26 << 24 | n27 << 16 | n2 << 8 | n25;
                }
                ++n4;
            }
        }
        int[] nArray8 = nArray;
        int n28 = n6;
        n4 = n7;
        n3 = 0;
        n2 = 0;
        BufferedImage bufferedImage2 = bufferedImage;
        int n29 = bufferedImage2.getType();
        if (n29 == 2 || n29 == 1) {
            bufferedImage2.getRaster().setDataElements(0, 0, n4, n28, nArray8);
        } else {
            bufferedImage2.setRGB(0, 0, n4, n28, nArray8, 0, n4);
        }
        return bufferedImage;
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage bufferedImage, ColorModel colorModel) {
        if (colorModel == null) {
            colorModel = bufferedImage.getColorModel();
        }
        return new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(bufferedImage.getWidth(), bufferedImage.getHeight()), colorModel.isAlphaPremultiplied(), null);
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage bufferedImage) {
        return new Rectangle(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    @Override
    public Point2D getPoint2D(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D.Double();
        }
        point2D2.setLocation(point2D.getX(), point2D.getY());
        return point2D2;
    }

    @Override
    public RenderingHints getRenderingHints() {
        return null;
    }

    public String toString() {
        return "Blur/Convolve...";
    }
}

