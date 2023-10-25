/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.composite;

import java.awt.Color;
import java.awt.CompositeContext;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class MiscCompositeContext
implements CompositeContext {
    private int rule;
    private float alpha;
    private ColorModel srcColorModel;
    private ColorModel dstColorModel;
    private ColorSpace srcColorSpace;
    private ColorSpace dstColorSpace;
    private boolean srcNeedsConverting;
    private boolean dstNeedsConverting;

    public MiscCompositeContext(int rule, float alpha, ColorModel srcColorModel, ColorModel dstColorModel) {
        this.rule = rule;
        this.alpha = alpha;
        this.srcColorModel = srcColorModel;
        this.dstColorModel = dstColorModel;
        this.srcColorSpace = srcColorModel.getColorSpace();
        this.dstColorSpace = dstColorModel.getColorSpace();
        ColorModel srgbCM = ColorModel.getRGBdefault();
    }

    @Override
    public void dispose() {
    }

    static int multiply255(int a, int b) {
        int t = a * b + 128;
        return (t >> 8) + t >> 8;
    }

    static int clamp(int a) {
        return a < 0 ? 0 : (a > 255 ? 255 : a);
    }

    @Override
    public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
        float a = 0.0f;
        float ac = 0.0f;
        float alpha = this.alpha;
        float[] sHsv = null;
        float[] diHsv = null;
        float[] doHsv = null;
        switch (this.rule) {
            case 12: 
            case 13: 
            case 14: 
            case 15: {
                sHsv = new float[3];
                diHsv = new float[3];
                doHsv = new float[3];
            }
        }
        int[] srcPix = null;
        int[] dstPix = null;
        int x = dstOut.getMinX();
        int w = dstOut.getWidth();
        int y0 = dstOut.getMinY();
        int y1 = y0 + dstOut.getHeight();
        for (int y = y0; y < y1; ++y) {
            srcPix = src.getPixels(x, y, w, 1, srcPix);
            dstPix = dstIn.getPixels(x, y, w, 1, dstPix);
            int end = w * 4;
            for (int i = 0; i < end; i += 4) {
                int dob;
                int dog;
                int dor;
                int sr = srcPix[i];
                int dir = dstPix[i];
                int sg = srcPix[i + 1];
                int dig = dstPix[i + 1];
                int sb = srcPix[i + 2];
                int dib = dstPix[i + 2];
                int sa = srcPix[i + 3];
                int dia = dstPix[i + 3];
                switch (this.rule) {
                    default: {
                        dor = dir + sr;
                        if (dor > 255) {
                            dor = 255;
                        }
                        if ((dog = dig + sg) > 255) {
                            dog = 255;
                        }
                        if ((dob = dib + sb) <= 255) break;
                        dob = 255;
                        break;
                    }
                    case 2: {
                        dor = dir - sr;
                        if (dor < 0) {
                            dor = 0;
                        }
                        if ((dog = dig - sg) < 0) {
                            dog = 0;
                        }
                        if ((dob = dib - sb) >= 0) break;
                        dob = 0;
                        break;
                    }
                    case 3: {
                        dor = dir - sr;
                        if (dor < 0) {
                            dor = -dor;
                        }
                        if ((dog = dig - sg) < 0) {
                            dog = -dog;
                        }
                        if ((dob = dib - sb) >= 0) break;
                        dob = -dob;
                        break;
                    }
                    case 4: {
                        int t = dir * sr + 128;
                        dor = (t >> 8) + t >> 8;
                        t = dig * sg + 128;
                        dog = (t >> 8) + t >> 8;
                        t = dib * sb + 128;
                        dob = (t >> 8) + t >> 8;
                        break;
                    }
                    case 8: {
                        int t = (255 - dir) * (255 - sr) + 128;
                        dor = 255 - ((t >> 8) + t >> 8);
                        t = (255 - dig) * (255 - sg) + 128;
                        dog = 255 - ((t >> 8) + t >> 8);
                        t = (255 - dib) * (255 - sb) + 128;
                        dob = 255 - ((t >> 8) + t >> 8);
                        break;
                    }
                    case 16: {
                        int t;
                        if (dir < 128) {
                            t = dir * sr + 128;
                            dor = 2 * ((t >> 8) + t >> 8);
                        } else {
                            t = (255 - dir) * (255 - sr) + 128;
                            dor = 2 * (255 - ((t >> 8) + t >> 8));
                        }
                        if (dig < 128) {
                            t = dig * sg + 128;
                            dog = 2 * ((t >> 8) + t >> 8);
                        } else {
                            t = (255 - dig) * (255 - sg) + 128;
                            dog = 2 * (255 - ((t >> 8) + t >> 8));
                        }
                        if (dib < 128) {
                            t = dib * sb + 128;
                            dob = 2 * ((t >> 8) + t >> 8);
                            break;
                        }
                        t = (255 - dib) * (255 - sb) + 128;
                        dob = 2 * (255 - ((t >> 8) + t >> 8));
                        break;
                    }
                    case 5: {
                        dor = dir < sr ? dir : sr;
                        dog = dig < sg ? dig : sg;
                        dob = dib < sb ? dib : sb;
                        break;
                    }
                    case 9: {
                        dor = dir > sr ? dir : sr;
                        dog = dig > sg ? dig : sg;
                        dob = dib > sb ? dib : sb;
                        break;
                    }
                    case 22: {
                        dor = (dir + sr) / 2;
                        dog = (dig + sg) / 2;
                        dob = (dib + sb) / 2;
                        break;
                    }
                    case 12: 
                    case 13: 
                    case 14: 
                    case 15: {
                        Color.RGBtoHSB(sr, sg, sb, sHsv);
                        Color.RGBtoHSB(dir, dig, dib, diHsv);
                        switch (this.rule) {
                            case 12: {
                                doHsv[0] = sHsv[0];
                                doHsv[1] = diHsv[1];
                                doHsv[2] = diHsv[2];
                                break;
                            }
                            case 13: {
                                doHsv[0] = diHsv[0];
                                doHsv[1] = sHsv[1];
                                doHsv[2] = diHsv[2];
                                break;
                            }
                            case 14: {
                                doHsv[0] = diHsv[0];
                                doHsv[1] = diHsv[1];
                                doHsv[2] = sHsv[2];
                                break;
                            }
                            case 15: {
                                doHsv[0] = sHsv[0];
                                doHsv[1] = sHsv[1];
                                doHsv[2] = diHsv[2];
                            }
                        }
                        int doRGB = Color.HSBtoRGB(doHsv[0], doHsv[1], doHsv[2]);
                        dor = (doRGB & 0xFF0000) >> 16;
                        dog = (doRGB & 0xFF00) >> 8;
                        dob = doRGB & 0xFF;
                        break;
                    }
                    case 6: {
                        dor = dir != 255 ? MiscCompositeContext.clamp(255 - (255 - sr << 8) / (dir + 1)) : sr;
                        dog = dig != 255 ? MiscCompositeContext.clamp(255 - (255 - sg << 8) / (dig + 1)) : sg;
                        if (dib != 255) {
                            dob = MiscCompositeContext.clamp(255 - (255 - sb << 8) / (dib + 1));
                            break;
                        }
                        dob = sb;
                        break;
                    }
                    case 7: {
                        dor = sr != 0 ? Math.max(255 - (255 - dir << 8) / sr, 0) : sr;
                        dog = sg != 0 ? Math.max(255 - (255 - dig << 8) / sg, 0) : sg;
                        if (sb != 0) {
                            dob = Math.max(255 - (255 - dib << 8) / sb, 0);
                            break;
                        }
                        dob = sb;
                        break;
                    }
                    case 10: {
                        dor = MiscCompositeContext.clamp((sr << 8) / (256 - dir));
                        dog = MiscCompositeContext.clamp((sg << 8) / (256 - dig));
                        dob = MiscCompositeContext.clamp((sb << 8) / (256 - dib));
                        break;
                    }
                    case 11: {
                        dor = sr != 255 ? Math.min((dir << 8) / (255 - sr), 255) : sr;
                        dog = sg != 255 ? Math.min((dig << 8) / (255 - sg), 255) : sg;
                        if (sb != 255) {
                            dob = Math.min((dib << 8) / (255 - sb), 255);
                            break;
                        }
                        dob = sb;
                        break;
                    }
                    case 17: {
                        int d = MiscCompositeContext.multiply255(sr, dir);
                        dor = d + MiscCompositeContext.multiply255(dir, 255 - MiscCompositeContext.multiply255(255 - dir, 255 - sr) - d);
                        d = MiscCompositeContext.multiply255(sg, dig);
                        dog = d + MiscCompositeContext.multiply255(dig, 255 - MiscCompositeContext.multiply255(255 - dig, 255 - sg) - d);
                        d = MiscCompositeContext.multiply255(sb, dib);
                        dob = d + MiscCompositeContext.multiply255(dib, 255 - MiscCompositeContext.multiply255(255 - dib, 255 - sb) - d);
                        break;
                    }
                    case 18: {
                        dor = sr > 127 ? 255 - 2 * MiscCompositeContext.multiply255(255 - sr, 255 - dir) : 2 * MiscCompositeContext.multiply255(sr, dir);
                        dog = sg > 127 ? 255 - 2 * MiscCompositeContext.multiply255(255 - sg, 255 - dig) : 2 * MiscCompositeContext.multiply255(sg, dig);
                        if (sb > 127) {
                            dob = 255 - 2 * MiscCompositeContext.multiply255(255 - sb, 255 - dib);
                            break;
                        }
                        dob = 2 * MiscCompositeContext.multiply255(sb, dib);
                        break;
                    }
                    case 19: {
                        dor = sr > 127 ? Math.max(sr, dir) : Math.min(sr, dir);
                        dog = sg > 127 ? Math.max(sg, dig) : Math.min(sg, dig);
                        dob = sb > 127 ? Math.max(sb, dib) : Math.min(sb, dib);
                        break;
                    }
                    case 20: {
                        dor = dir + MiscCompositeContext.multiply255(sr, 255 - dir - dir);
                        dog = dig + MiscCompositeContext.multiply255(sg, 255 - dig - dig);
                        dob = dib + MiscCompositeContext.multiply255(sb, 255 - dib - dib);
                        break;
                    }
                    case 21: {
                        dor = 255 - Math.abs(255 - sr - dir);
                        dog = 255 - Math.abs(255 - sg - dig);
                        dob = 255 - Math.abs(255 - sb - dib);
                    }
                }
                a = alpha * (float)sa / 255.0f;
                ac = 1.0f - a;
                dstPix[i] = (int)(a * (float)dor + ac * (float)dir);
                dstPix[i + 1] = (int)(a * (float)dog + ac * (float)dig);
                dstPix[i + 2] = (int)(a * (float)dob + ac * (float)dib);
                dstPix[i + 3] = (int)((float)sa * alpha + (float)dia * ac);
            }
            dstOut.setPixels(x, y, w, 1, dstPix);
        }
    }
}

