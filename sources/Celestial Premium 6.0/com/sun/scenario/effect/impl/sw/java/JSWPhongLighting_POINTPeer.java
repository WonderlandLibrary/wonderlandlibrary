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

public class JSWPhongLighting_POINTPeer
extends JSWEffectPeer {
    private FloatBuffer kvals;

    public JSWPhongLighting_POINTPeer(FilterContext filterContext, Renderer renderer, String string) {
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
        float[] arrf3 = this.getLightPosition();
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
        float f10 = this.getSurfaceScale();
        float f11 = (arrf[2] - arrf[0]) / (float)n11;
        float f12 = (arrf[3] - arrf[1]) / (float)n12;
        float f13 = (arrf2[2] - arrf2[0]) / (float)n11;
        float f14 = (arrf2[3] - arrf2[1]) / (float)n12;
        float f15 = arrf[1] + f12 * 0.5f;
        float f16 = arrf2[1] + f14 * 0.5f;
        for (int i = 0; i < 0 + n12; ++i) {
            float f17 = i;
            int n14 = i * n13;
            float f18 = arrf[0] + f11 * 0.5f;
            float f19 = arrf2[0] + f13 * 0.5f;
            for (int j = 0; j < 0 + n11; ++j) {
                int n15;
                float f20;
                float f21;
                int n16;
                int n17;
                float f22 = j;
                float f23 = f19;
                float f24 = f16;
                if (f23 >= 0.0f && f24 >= 0.0f) {
                    int n18 = (int)(f23 * (float)n8);
                    n17 = (int)(f24 * (float)n9);
                    boolean bl = n18 >= n8 || n17 >= n9;
                    n16 = bl ? 0 : arrn2[n17 * n10 + n18];
                } else {
                    n16 = 0;
                }
                float f25 = (float)(n16 >> 16 & 0xFF) / 255.0f;
                float f26 = (float)(n16 >> 8 & 0xFF) / 255.0f;
                float f27 = (float)(n16 & 0xFF) / 255.0f;
                float f28 = (float)(n16 >>> 24) / 255.0f;
                f23 = f25;
                f24 = f26;
                float f29 = f27;
                float f30 = f28;
                float f31 = 0.0f;
                float f32 = 0.0f;
                float f33 = 1.0f;
                for (n17 = 0; n17 < 8; ++n17) {
                    int n19;
                    f21 = f18 + arrf4[n17 * 4 + 0];
                    f20 = f15 + arrf4[n17 * 4 + 1];
                    if (f21 >= 0.0f && f20 >= 0.0f) {
                        int n20 = (int)(f21 * (float)n3);
                        int n21 = (int)(f20 * (float)n4);
                        boolean bl = n20 >= n3 || n21 >= n4;
                        n19 = bl ? 0 : arrn[n21 * n5 + n20];
                    } else {
                        n19 = 0;
                    }
                    f28 = (float)(n19 >>> 24) / 255.0f;
                    f31 += arrf4[n17 * 4 + 2] * f28;
                    f32 += arrf4[n17 * 4 + 3] * f28;
                }
                float f34 = f31;
                float f35 = f32;
                float f36 = f33;
                float f37 = (float)Math.sqrt(f34 * f34 + f35 * f35 + f36 * f36);
                f21 = f34 / f37;
                f20 = f35 / f37;
                float f38 = f36 / f37;
                f34 = f21;
                f35 = f20;
                f36 = f38;
                f37 = f18;
                float f39 = f15;
                if (f37 >= 0.0f && f39 >= 0.0f) {
                    int n22 = (int)(f37 * (float)n3);
                    int n23 = (int)(f39 * (float)n4);
                    boolean bl = n22 >= n3 || n23 >= n4;
                    n15 = bl ? 0 : arrn[n23 * n5 + n22];
                } else {
                    n15 = 0;
                }
                f37 = f28 = (float)(n15 >>> 24) / 255.0f;
                f39 = f22;
                float f40 = f17;
                float f41 = f10 * f37;
                float f42 = f - f39;
                float f43 = f2 - f40;
                float f44 = f3 - f41;
                float f45 = (float)Math.sqrt(f42 * f42 + f43 * f43 + f44 * f44);
                f21 = f42 / f45;
                f20 = f43 / f45;
                f38 = f44 / f45;
                f42 = f21;
                f43 = f20;
                f44 = f38;
                f45 = f6;
                float f46 = f7;
                float f47 = f8;
                float f48 = 0.0f;
                float f49 = 0.0f;
                float f50 = 1.0f;
                float f51 = f42 + f48;
                float f52 = f43 + f49;
                float f53 = f44 + f50;
                float f54 = (float)Math.sqrt(f51 * f51 + f52 * f52 + f53 * f53);
                f21 = f51 / f54;
                f20 = f52 / f54;
                f38 = f53 / f54;
                f51 = f21;
                f52 = f20;
                f53 = f38;
                float f55 = f34;
                float f56 = f35;
                float f57 = f36;
                float f58 = f42;
                float f59 = f43;
                float f60 = f44;
                float f61 = f55 * f58 + f56 * f59 + f57 * f60;
                f54 = f5 * f61 * f45;
                float f62 = f5 * f61 * f46;
                float f63 = f5 * f61 * f47;
                f58 = f54;
                f59 = f62;
                f60 = f63;
                float f64 = 0.0f;
                float f65 = 1.0f;
                float f66 = f58 < f64 ? f64 : (f55 = f58 > f65 ? f65 : f58);
                float f67 = f59 < f64 ? f64 : (f56 = f59 > f65 ? f65 : f59);
                f57 = f60 < f64 ? f64 : (f60 > f65 ? f65 : f60);
                f54 = f55;
                f62 = f56;
                f63 = f57;
                float f68 = 1.0f;
                f65 = f34;
                float f69 = f35;
                float f70 = f36;
                float f71 = f51;
                float f72 = f52;
                float f73 = f53;
                f70 = f65 = (f61 = f65 * f71 + f69 * f72 + f70 * f73);
                f71 = f4;
                f69 = (float)Math.pow(f70, f71);
                f58 = f9 * f69 * f45;
                f59 = f9 * f69 * f46;
                f60 = f9 * f69 * f47;
                f71 = f58;
                f72 = f59;
                f71 = f64 = (f70 = f71 > f72 ? f71 : f72);
                f72 = f60;
                f64 = f70 = f71 > f72 ? f71 : f72;
                float f74 = (f58 *= (f30 *= f68)) + (f23 *= f54) * (1.0f - (f64 *= f30));
                float f75 = (f59 *= f30) + (f24 *= f62) * (1.0f - f64);
                float f76 = (f60 *= f30) + (f29 *= f63) * (1.0f - f64);
                float f77 = f64 + f30 * (1.0f - f64);
                if (f77 < 0.0f) {
                    f77 = 0.0f;
                } else if (f77 > 1.0f) {
                    f77 = 1.0f;
                }
                if (f74 < 0.0f) {
                    f74 = 0.0f;
                } else if (f74 > f77) {
                    f74 = f77;
                }
                if (f75 < 0.0f) {
                    f75 = 0.0f;
                } else if (f75 > f77) {
                    f75 = f77;
                }
                if (f76 < 0.0f) {
                    f76 = 0.0f;
                } else if (f76 > f77) {
                    f76 = f77;
                }
                arrn3[n14 + j] = (int)(f74 * 255.0f) << 16 | (int)(f75 * 255.0f) << 8 | (int)(f76 * 255.0f) << 0 | (int)(f77 * 255.0f) << 24;
                f18 += f11;
                f19 += f13;
            }
            f15 += f12;
            f16 += f14;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        arrimageData[1].releaseTransformedImage(heapImage2);
        return new ImageData(this.getFilterContext(), heapImage3, rectangle2);
    }
}

