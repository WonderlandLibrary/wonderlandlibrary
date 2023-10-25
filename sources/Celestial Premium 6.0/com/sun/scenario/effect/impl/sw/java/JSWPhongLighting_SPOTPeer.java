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

public class JSWPhongLighting_SPOTPeer
extends JSWEffectPeer {
    private FloatBuffer kvals;

    public JSWPhongLighting_SPOTPeer(FilterContext filterContext, Renderer renderer, String string) {
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
        float f4 = this.getLightSpecularExponent();
        float f5 = this.getSpecularExponent();
        FloatBuffer floatBuffer = this.getKvals();
        float[] arrf4 = new float[floatBuffer.capacity()];
        floatBuffer.get(arrf4);
        float f6 = this.getDiffuseConstant();
        float[] arrf5 = this.getLightColor();
        float f7 = arrf5[0];
        float f8 = arrf5[1];
        float f9 = arrf5[2];
        float[] arrf6 = this.getNormalizedLightDirection();
        float f10 = arrf6[0];
        float f11 = arrf6[1];
        float f12 = arrf6[2];
        float f13 = this.getSpecularConstant();
        float f14 = this.getSurfaceScale();
        float f15 = (arrf[2] - arrf[0]) / (float)n11;
        float f16 = (arrf[3] - arrf[1]) / (float)n12;
        float f17 = (arrf2[2] - arrf2[0]) / (float)n11;
        float f18 = (arrf2[3] - arrf2[1]) / (float)n12;
        float f19 = arrf[1] + f16 * 0.5f;
        float f20 = arrf2[1] + f18 * 0.5f;
        for (int i = 0; i < 0 + n12; ++i) {
            float f21 = i;
            int n14 = i * n13;
            float f22 = arrf[0] + f15 * 0.5f;
            float f23 = arrf2[0] + f17 * 0.5f;
            for (int j = 0; j < 0 + n11; ++j) {
                int n15;
                float f24;
                float f25;
                int n16;
                int n17;
                float f26 = j;
                float f27 = f23;
                float f28 = f20;
                if (f27 >= 0.0f && f28 >= 0.0f) {
                    int n18 = (int)(f27 * (float)n8);
                    n17 = (int)(f28 * (float)n9);
                    boolean bl = n18 >= n8 || n17 >= n9;
                    n16 = bl ? 0 : arrn2[n17 * n10 + n18];
                } else {
                    n16 = 0;
                }
                float f29 = (float)(n16 >> 16 & 0xFF) / 255.0f;
                float f30 = (float)(n16 >> 8 & 0xFF) / 255.0f;
                float f31 = (float)(n16 & 0xFF) / 255.0f;
                float f32 = (float)(n16 >>> 24) / 255.0f;
                f27 = f29;
                f28 = f30;
                float f33 = f31;
                float f34 = f32;
                float f35 = 0.0f;
                float f36 = 0.0f;
                float f37 = 1.0f;
                for (n17 = 0; n17 < 8; ++n17) {
                    int n19;
                    f25 = f22 + arrf4[n17 * 4 + 0];
                    f24 = f19 + arrf4[n17 * 4 + 1];
                    if (f25 >= 0.0f && f24 >= 0.0f) {
                        int n20 = (int)(f25 * (float)n3);
                        int n21 = (int)(f24 * (float)n4);
                        boolean bl = n20 >= n3 || n21 >= n4;
                        n19 = bl ? 0 : arrn[n21 * n5 + n20];
                    } else {
                        n19 = 0;
                    }
                    f32 = (float)(n19 >>> 24) / 255.0f;
                    f35 += arrf4[n17 * 4 + 2] * f32;
                    f36 += arrf4[n17 * 4 + 3] * f32;
                }
                float f38 = f35;
                float f39 = f36;
                float f40 = f37;
                float f41 = (float)Math.sqrt(f38 * f38 + f39 * f39 + f40 * f40);
                f25 = f38 / f41;
                f24 = f39 / f41;
                float f42 = f40 / f41;
                f38 = f25;
                f39 = f24;
                f40 = f42;
                f41 = f22;
                float f43 = f19;
                if (f41 >= 0.0f && f43 >= 0.0f) {
                    int n22 = (int)(f41 * (float)n3);
                    int n23 = (int)(f43 * (float)n4);
                    boolean bl = n22 >= n3 || n23 >= n4;
                    n15 = bl ? 0 : arrn[n23 * n5 + n22];
                } else {
                    n15 = 0;
                }
                f41 = f32 = (float)(n15 >>> 24) / 255.0f;
                f43 = f26;
                float f44 = f21;
                float f45 = f14 * f41;
                float f46 = f - f43;
                float f47 = f2 - f44;
                float f48 = f3 - f45;
                float f49 = (float)Math.sqrt(f46 * f46 + f47 * f47 + f48 * f48);
                f25 = f46 / f49;
                f24 = f47 / f49;
                f42 = f48 / f49;
                f46 = f25;
                f47 = f24;
                f48 = f42;
                float f50 = f46;
                float f51 = f47;
                float f52 = f48;
                float f53 = f10;
                float f54 = f11;
                float f55 = f12;
                f52 = f50 = (f49 = f50 * f53 + f51 * f54 + f52 * f55);
                f53 = 0.0f;
                f50 = f51 = f52 < f53 ? f52 : f53;
                f53 = -f50;
                f54 = f4;
                f52 = (float)Math.pow(f53, f54);
                f53 = f7 * f52;
                f54 = f8 * f52;
                f55 = f9 * f52;
                float f56 = 0.0f;
                float f57 = 0.0f;
                float f58 = 1.0f;
                float f59 = f46 + f56;
                float f60 = f47 + f57;
                float f61 = f48 + f58;
                float f62 = (float)Math.sqrt(f59 * f59 + f60 * f60 + f61 * f61);
                f25 = f59 / f62;
                f24 = f60 / f62;
                f42 = f61 / f62;
                f59 = f25;
                f60 = f24;
                f61 = f42;
                float f63 = f38;
                float f64 = f39;
                float f65 = f40;
                float f66 = f46;
                float f67 = f47;
                float f68 = f48;
                f49 = f63 * f66 + f64 * f67 + f65 * f68;
                f62 = f6 * f49 * f53;
                float f69 = f6 * f49 * f54;
                float f70 = f6 * f49 * f55;
                f66 = f62;
                f67 = f69;
                f68 = f70;
                float f71 = 0.0f;
                float f72 = 1.0f;
                float f73 = f66 < f71 ? f71 : (f63 = f66 > f72 ? f72 : f66);
                float f74 = f67 < f71 ? f71 : (f64 = f67 > f72 ? f72 : f67);
                f65 = f68 < f71 ? f71 : (f68 > f72 ? f72 : f68);
                f62 = f63;
                f69 = f64;
                f70 = f65;
                float f75 = 1.0f;
                f72 = f38;
                float f76 = f39;
                float f77 = f40;
                float f78 = f59;
                float f79 = f60;
                float f80 = f61;
                f76 = f72 = (f49 = f72 * f78 + f76 * f79 + f77 * f80);
                f77 = f5;
                f52 = (float)Math.pow(f76, f77);
                f66 = f13 * f52 * f53;
                f67 = f13 * f52 * f54;
                f68 = f13 * f52 * f55;
                f77 = f66;
                f78 = f67;
                f77 = f71 = (f76 = f77 > f78 ? f77 : f78);
                f78 = f68;
                f71 = f76 = f77 > f78 ? f77 : f78;
                float f81 = (f66 *= (f34 *= f75)) + (f27 *= f62) * (1.0f - (f71 *= f34));
                float f82 = (f67 *= f34) + (f28 *= f69) * (1.0f - f71);
                float f83 = (f68 *= f34) + (f33 *= f70) * (1.0f - f71);
                float f84 = f71 + f34 * (1.0f - f71);
                if (f84 < 0.0f) {
                    f84 = 0.0f;
                } else if (f84 > 1.0f) {
                    f84 = 1.0f;
                }
                if (f81 < 0.0f) {
                    f81 = 0.0f;
                } else if (f81 > f84) {
                    f81 = f84;
                }
                if (f82 < 0.0f) {
                    f82 = 0.0f;
                } else if (f82 > f84) {
                    f82 = f84;
                }
                if (f83 < 0.0f) {
                    f83 = 0.0f;
                } else if (f83 > f84) {
                    f83 = f84;
                }
                arrn3[n14 + j] = (int)(f81 * 255.0f) << 16 | (int)(f82 * 255.0f) << 8 | (int)(f83 * 255.0f) << 0 | (int)(f84 * 255.0f) << 24;
                f22 += f15;
                f23 += f17;
            }
            f19 += f16;
            f20 += f18;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        arrimageData[1].releaseTransformedImage(heapImage2);
        return new ImageData(this.getFilterContext(), heapImage3, rectangle2);
    }
}

