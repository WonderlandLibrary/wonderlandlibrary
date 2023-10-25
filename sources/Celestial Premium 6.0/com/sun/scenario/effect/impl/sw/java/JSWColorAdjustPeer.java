/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.ColorAdjust;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWColorAdjustPeer
extends JSWEffectPeer {
    public JSWColorAdjustPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final ColorAdjust getEffect() {
        return (ColorAdjust)super.getEffect();
    }

    private float getHue() {
        return this.getEffect().getHue() / 2.0f;
    }

    private float getSaturation() {
        return this.getEffect().getSaturation() + 1.0f;
    }

    private float getBrightness() {
        return this.getEffect().getBrightness() + 1.0f;
    }

    private float getContrast() {
        float f = this.getEffect().getContrast();
        if (f > 0.0f) {
            f *= 3.0f;
        }
        return f + 1.0f;
    }

    @Override
    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setEffect(effect);
        Rectangle rectangle2 = this.getResultBounds(baseTransform, rectangle, arrimageData);
        this.setDestBounds(rectangle2);
        HeapImage heapImage = (HeapImage)arrimageData[0].getTransformedImage(rectangle2);
        int n = 0;
        int n2 = 0;
        int n3 = heapImage.getPhysicalWidth();
        int n4 = heapImage.getPhysicalHeight();
        int n5 = heapImage.getScanlineStride();
        int[] arrn = heapImage.getPixelArray();
        Rectangle rectangle3 = new Rectangle(n, n2, n3, n4);
        Rectangle rectangle4 = arrimageData[0].getTransformedBounds(rectangle2);
        BaseTransform baseTransform2 = BaseTransform.IDENTITY_TRANSFORM;
        this.setInputBounds(0, rectangle4);
        this.setInputNativeBounds(0, rectangle3);
        float[] arrf = new float[4];
        this.getTextureCoordinates(0, arrf, rectangle4.x, rectangle4.y, n3, n4, rectangle2, baseTransform2);
        int n6 = rectangle2.width;
        int n7 = rectangle2.height;
        HeapImage heapImage2 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n6, n7));
        this.setDestNativeBounds(heapImage2.getPhysicalWidth(), heapImage2.getPhysicalHeight());
        int n8 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        float f = this.getSaturation();
        float f2 = this.getBrightness();
        float f3 = this.getContrast();
        float f4 = this.getHue();
        float f5 = (arrf[2] - arrf[0]) / (float)n6;
        float f6 = (arrf[3] - arrf[1]) / (float)n7;
        float f7 = arrf[1] + f6 * 0.5f;
        for (int i = 0; i < 0 + n7; ++i) {
            float f8 = i;
            int n9 = i * n8;
            float f9 = arrf[0] + f5 * 0.5f;
            for (int j = 0; j < 0 + n6; ++j) {
                float f10;
                float f11;
                float f12;
                float f13;
                int n10;
                float f14 = j;
                float f15 = f9;
                float f16 = f7;
                if (f15 >= 0.0f && f16 >= 0.0f) {
                    int n11 = (int)(f15 * (float)n3);
                    int n12 = (int)(f16 * (float)n4);
                    boolean bl = n11 >= n3 || n12 >= n4;
                    n10 = bl ? 0 : arrn[n12 * n5 + n11];
                } else {
                    n10 = 0;
                }
                float f17 = (float)(n10 >> 16 & 0xFF) / 255.0f;
                float f18 = (float)(n10 >> 8 & 0xFF) / 255.0f;
                float f19 = (float)(n10 & 0xFF) / 255.0f;
                float f20 = (float)(n10 >>> 24) / 255.0f;
                f15 = f17;
                f16 = f18;
                float f21 = f19;
                float f22 = f20;
                if (f22 > 0.0f) {
                    f15 /= f22;
                    f16 /= f22;
                    f21 /= f22;
                }
                f15 = (f15 - 0.5f) * f3 + 0.5f;
                f16 = (f16 - 0.5f) * f3 + 0.5f;
                f21 = (f21 - 0.5f) * f3 + 0.5f;
                float f23 = f15;
                float f24 = f16;
                float f25 = f21;
                float f26 = f23;
                float f27 = f24;
                f27 = f26 = (f13 = f26 > f27 ? f26 : f27);
                float f28 = f25;
                f26 = f13 = f27 > f28 ? f27 : f28;
                f28 = f23;
                float f29 = f24;
                f29 = f28 = (f27 = f28 < f29 ? f28 : f29);
                float f30 = f25;
                f27 = f29 < f30 ? f29 : f30;
                f28 = f27;
                if (f26 > f28) {
                    f29 = (f26 - f23) / (f26 - f28);
                    f30 = (f26 - f24) / (f26 - f28);
                    f12 = (f26 - f25) / (f26 - f28);
                    f11 = f23 == f26 ? f12 - f30 : (f24 == f26 ? 2.0f + f29 - f12 : 4.0f + f30 - f29);
                    if ((f11 /= 6.0f) < 0.0f) {
                        f11 += 1.0f;
                    }
                    f10 = (f26 - f28) / f26;
                } else {
                    f11 = 0.0f;
                    f10 = 0.0f;
                }
                float f31 = f26;
                float f32 = f11;
                float f33 = f10;
                float f34 = f31;
                f23 = f32;
                f24 = f33;
                f25 = f34;
                f23 += f4;
                if (f23 < 0.0f) {
                    f23 += 1.0f;
                } else if (f23 > 1.0f) {
                    f23 -= 1.0f;
                }
                if (f > 1.0f) {
                    f11 = f - 1.0f;
                    f24 += (1.0f - f24) * f11;
                } else {
                    f24 *= f;
                }
                if (f2 > 1.0f) {
                    f11 = f2 - 1.0f;
                    f24 *= 1.0f - f11;
                    f25 += (1.0f - f25) * f11;
                } else {
                    f25 *= f2;
                }
                f31 = f24;
                f13 = f25;
                f26 = 0.0f;
                f27 = 1.0f;
                float f35 = f31 < f26 ? f26 : (f11 = f31 > f27 ? f27 : f31);
                f10 = f13 < f26 ? f26 : (f13 > f27 ? f27 : f13);
                f24 = f11;
                f25 = f10;
                f27 = f23;
                f28 = f24;
                f29 = f25;
                f30 = 0.0f;
                f12 = 0.0f;
                float f36 = 0.0f;
                float f37 = f27;
                float f38 = f28;
                float f39 = f29;
                float f40 = f37;
                float f41 = (float)Math.floor(f40);
                f40 = f37 = (f37 - f41) * 6.0f;
                f41 = (float)Math.floor(f40);
                f40 = f37 - f41;
                float f42 = f39 * (1.0f - f38);
                float f43 = f39 * (1.0f - f38 * f40);
                float f44 = f39 * (1.0f - f38 * (1.0f - f40));
                float f45 = f37;
                f37 = f41 = (float)Math.floor(f45);
                if (f37 < 1.0f) {
                    f30 = f39;
                    f12 = f44;
                    f36 = f42;
                } else if (f37 < 2.0f) {
                    f30 = f43;
                    f12 = f39;
                    f36 = f42;
                } else if (f37 < 3.0f) {
                    f30 = f42;
                    f12 = f39;
                    f36 = f44;
                } else if (f37 < 4.0f) {
                    f30 = f42;
                    f12 = f43;
                    f36 = f39;
                } else if (f37 < 5.0f) {
                    f30 = f44;
                    f12 = f42;
                    f36 = f39;
                } else {
                    f30 = f39;
                    f12 = f42;
                    f36 = f43;
                }
                f31 = f30;
                f13 = f12;
                f26 = f36;
                float f46 = f22 * f31;
                float f47 = f22 * f13;
                float f48 = f22 * f26;
                float f49 = f22;
                if (f49 < 0.0f) {
                    f49 = 0.0f;
                } else if (f49 > 1.0f) {
                    f49 = 1.0f;
                }
                if (f46 < 0.0f) {
                    f46 = 0.0f;
                } else if (f46 > f49) {
                    f46 = f49;
                }
                if (f47 < 0.0f) {
                    f47 = 0.0f;
                } else if (f47 > f49) {
                    f47 = f49;
                }
                if (f48 < 0.0f) {
                    f48 = 0.0f;
                } else if (f48 > f49) {
                    f48 = f49;
                }
                arrn2[n9 + j] = (int)(f46 * 255.0f) << 16 | (int)(f47 * 255.0f) << 8 | (int)(f48 * 255.0f) << 0 | (int)(f49 * 255.0f) << 24;
                f9 += f5;
            }
            f7 += f6;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle2);
    }
}

