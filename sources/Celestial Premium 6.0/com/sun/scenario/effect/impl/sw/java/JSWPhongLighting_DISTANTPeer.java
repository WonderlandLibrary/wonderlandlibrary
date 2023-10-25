/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.PhongLighting;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;
import com.sun.scenario.effect.light.PointLight;
import com.sun.scenario.effect.light.SpotLight;
import java.nio.FloatBuffer;

public class JSWPhongLighting_DISTANTPeer
extends JSWEffectPeer {
    private FloatBuffer kvals;

    public JSWPhongLighting_DISTANTPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final PhongLighting getEffect() {
        return (PhongLighting)super.getEffect();
    }

    private float getSurfaceScale() {
        return this.getEffect().getSurfaceScale();
    }

    private float getDiffuseConstant() {
        return this.getEffect().getDiffuseConstant();
    }

    private float getSpecularConstant() {
        return this.getEffect().getSpecularConstant();
    }

    private float getSpecularExponent() {
        return this.getEffect().getSpecularExponent();
    }

    private float[] getNormalizedLightPosition() {
        return this.getEffect().getLight().getNormalizedLightPosition();
    }

    private float[] getLightPosition() {
        PointLight pointLight = (PointLight)this.getEffect().getLight();
        return new float[]{pointLight.getX(), pointLight.getY(), pointLight.getZ()};
    }

    private float[] getLightColor() {
        return this.getEffect().getLight().getColor().getPremultipliedRGBComponents();
    }

    private float getLightSpecularExponent() {
        return ((SpotLight)this.getEffect().getLight()).getSpecularExponent();
    }

    private float[] getNormalizedLightDirection() {
        return ((SpotLight)this.getEffect().getLight()).getNormalizedLightDirection();
    }

    private FloatBuffer getKvals() {
        Rectangle rectangle = this.getInputNativeBounds(0);
        float f = 1.0f / (float)rectangle.width;
        float f2 = 1.0f / (float)rectangle.height;
        float[] arrf = new float[]{-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f};
        float[] arrf2 = new float[]{-1.0f, -2.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f};
        if (this.kvals == null) {
            this.kvals = BufferUtil.newFloatBuffer(32);
        }
        this.kvals.clear();
        int n = 0;
        float f3 = -this.getSurfaceScale() * 0.25f;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i != 0 || j != 0) {
                    this.kvals.put((float)j * f);
                    this.kvals.put((float)i * f2);
                    this.kvals.put(arrf[n] * f3);
                    this.kvals.put(arrf2[n] * f3);
                }
                ++n;
            }
        }
        this.kvals.rewind();
        return this.kvals;
    }

    private int getKvalsArrayLength() {
        return 8;
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
        HeapImage heapImage2 = (HeapImage)arrimageData[1].getTransformedImage(rectangle2);
        int n6 = 0;
        int n7 = 0;
        int n8 = heapImage2.getPhysicalWidth();
        int n9 = heapImage2.getPhysicalHeight();
        int n10 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        Rectangle rectangle5 = new Rectangle(n6, n7, n8, n9);
        Rectangle rectangle6 = arrimageData[1].getTransformedBounds(rectangle2);
        BaseTransform baseTransform3 = BaseTransform.IDENTITY_TRANSFORM;
        this.setInputBounds(1, rectangle6);
        this.setInputNativeBounds(1, rectangle5);
        float[] arrf = new float[4];
        this.getTextureCoordinates(0, arrf, rectangle4.x, rectangle4.y, n3, n4, rectangle2, baseTransform2);
        float[] arrf2 = new float[4];
        this.getTextureCoordinates(1, arrf2, rectangle6.x, rectangle6.y, n8, n9, rectangle2, baseTransform3);
        int n11 = rectangle2.width;
        int n12 = rectangle2.height;
        HeapImage heapImage3 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n11, n12));
        this.setDestNativeBounds(heapImage3.getPhysicalWidth(), heapImage3.getPhysicalHeight());
        int n13 = heapImage3.getScanlineStride();
        int[] arrn3 = heapImage3.getPixelArray();
        float[] arrf3 = this.getNormalizedLightPosition();
        float f = arrf3[0];
        float f2 = arrf3[1];
        float f3 = arrf3[2];
        float f4 = this.getSpecularExponent();
        FloatBuffer floatBuffer = this.getKvals();
        float[] arrf4 = new float[floatBuffer.capacity()];
        floatBuffer.get(arrf4);
        float f5 = this.getDiffuseConstant();
        float[] arrf5 = this.getLightColor();
        float f6 = arrf5[0];
        float f7 = arrf5[1];
        float f8 = arrf5[2];
        float f9 = this.getSpecularConstant();
        float f10 = (arrf[2] - arrf[0]) / (float)n11;
        float f11 = (arrf[3] - arrf[1]) / (float)n12;
        float f12 = (arrf2[2] - arrf2[0]) / (float)n11;
        float f13 = (arrf2[3] - arrf2[1]) / (float)n12;
        float f14 = arrf[1] + f11 * 0.5f;
        float f15 = arrf2[1] + f13 * 0.5f;
        for (int i = 0; i < 0 + n12; ++i) {
            float f16 = i;
            int n14 = i * n13;
            float f17 = arrf[0] + f10 * 0.5f;
            float f18 = arrf2[0] + f12 * 0.5f;
            for (int j = 0; j < 0 + n11; ++j) {
                float f19;
                float f20;
                int n15;
                int n16;
                float f21 = j;
                float f22 = f18;
                float f23 = f15;
                if (f22 >= 0.0f && f23 >= 0.0f) {
                    int n17 = (int)(f22 * (float)n8);
                    n16 = (int)(f23 * (float)n9);
                    boolean bl = n17 >= n8 || n16 >= n9;
                    n15 = bl ? 0 : arrn2[n16 * n10 + n17];
                } else {
                    n15 = 0;
                }
                float f24 = (float)(n15 >> 16 & 0xFF) / 255.0f;
                float f25 = (float)(n15 >> 8 & 0xFF) / 255.0f;
                float f26 = (float)(n15 & 0xFF) / 255.0f;
                float f27 = (float)(n15 >>> 24) / 255.0f;
                f22 = f24;
                f23 = f25;
                float f28 = f26;
                float f29 = f27;
                float f30 = 0.0f;
                float f31 = 0.0f;
                float f32 = 1.0f;
                for (n16 = 0; n16 < 8; ++n16) {
                    int n18;
                    f20 = f17 + arrf4[n16 * 4 + 0];
                    f19 = f14 + arrf4[n16 * 4 + 1];
                    if (f20 >= 0.0f && f19 >= 0.0f) {
                        int n19 = (int)(f20 * (float)n3);
                        int n20 = (int)(f19 * (float)n4);
                        boolean bl = n19 >= n3 || n20 >= n4;
                        n18 = bl ? 0 : arrn[n20 * n5 + n19];
                    } else {
                        n18 = 0;
                    }
                    f27 = (float)(n18 >>> 24) / 255.0f;
                    f30 += arrf4[n16 * 4 + 2] * f27;
                    f31 += arrf4[n16 * 4 + 3] * f27;
                }
                float f33 = f30;
                float f34 = f31;
                float f35 = f32;
                float f36 = (float)Math.sqrt(f33 * f33 + f34 * f34 + f35 * f35);
                f20 = f33 / f36;
                f19 = f34 / f36;
                float f37 = f35 / f36;
                f33 = f20;
                f34 = f19;
                f35 = f37;
                f36 = f;
                float f38 = f2;
                float f39 = f3;
                float f40 = f6;
                float f41 = f7;
                float f42 = f8;
                float f43 = 0.0f;
                float f44 = 0.0f;
                float f45 = 1.0f;
                float f46 = f36 + f43;
                float f47 = f38 + f44;
                float f48 = f39 + f45;
                float f49 = (float)Math.sqrt(f46 * f46 + f47 * f47 + f48 * f48);
                f20 = f46 / f49;
                f19 = f47 / f49;
                f37 = f48 / f49;
                f46 = f20;
                f47 = f19;
                f48 = f37;
                float f50 = f33;
                float f51 = f34;
                float f52 = f35;
                float f53 = f36;
                float f54 = f38;
                float f55 = f39;
                float f56 = f50 * f53 + f51 * f54 + f52 * f55;
                f49 = f5 * f56 * f40;
                float f57 = f5 * f56 * f41;
                float f58 = f5 * f56 * f42;
                f53 = f49;
                f54 = f57;
                f55 = f58;
                float f59 = 0.0f;
                float f60 = 1.0f;
                float f61 = f53 < f59 ? f59 : (f50 = f53 > f60 ? f60 : f53);
                float f62 = f54 < f59 ? f59 : (f51 = f54 > f60 ? f60 : f54);
                f52 = f55 < f59 ? f59 : (f55 > f60 ? f60 : f55);
                f49 = f50;
                f57 = f51;
                f58 = f52;
                float f63 = 1.0f;
                f60 = f33;
                float f64 = f34;
                float f65 = f35;
                float f66 = f46;
                float f67 = f47;
                float f68 = f48;
                f65 = f60 = (f56 = f60 * f66 + f64 * f67 + f65 * f68);
                f66 = f4;
                f64 = (float)Math.pow(f65, f66);
                f53 = f9 * f64 * f40;
                f54 = f9 * f64 * f41;
                f55 = f9 * f64 * f42;
                f66 = f53;
                f67 = f54;
                f66 = f59 = (f65 = f66 > f67 ? f66 : f67);
                f67 = f55;
                f59 = f65 = f66 > f67 ? f66 : f67;
                float f69 = (f53 *= (f29 *= f63)) + (f22 *= f49) * (1.0f - (f59 *= f29));
                float f70 = (f54 *= f29) + (f23 *= f57) * (1.0f - f59);
                float f71 = (f55 *= f29) + (f28 *= f58) * (1.0f - f59);
                float f72 = f59 + f29 * (1.0f - f59);
                if (f72 < 0.0f) {
                    f72 = 0.0f;
                } else if (f72 > 1.0f) {
                    f72 = 1.0f;
                }
                if (f69 < 0.0f) {
                    f69 = 0.0f;
                } else if (f69 > f72) {
                    f69 = f72;
                }
                if (f70 < 0.0f) {
                    f70 = 0.0f;
                } else if (f70 > f72) {
                    f70 = f72;
                }
                if (f71 < 0.0f) {
                    f71 = 0.0f;
                } else if (f71 > f72) {
                    f71 = f72;
                }
                arrn3[n14 + j] = (int)(f69 * 255.0f) << 16 | (int)(f70 * 255.0f) << 8 | (int)(f71 * 255.0f) << 0 | (int)(f72 * 255.0f) << 24;
                f17 += f10;
                f18 += f12;
            }
            f14 += f11;
            f15 += f13;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        arrimageData[1].releaseTransformedImage(heapImage2);
        return new ImageData(this.getFilterContext(), heapImage3, rectangle2);
    }
}

