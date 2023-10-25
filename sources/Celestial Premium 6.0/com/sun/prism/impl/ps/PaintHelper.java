/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.AffineBase;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BufferUtil;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.WeakHashMap;

class PaintHelper {
    static final int MULTI_MAX_FRACTIONS = 12;
    private static final int MULTI_TEXTURE_SIZE = 16;
    private static final int MULTI_CACHE_SIZE = 256;
    private static final int GTEX_CLR_TABLE_SIZE = 101;
    private static final int GTEX_CLR_TABLE_MIRRORED_SIZE = 201;
    private static final float FULL_TEXEL_Y = 0.00390625f;
    private static final float HALF_TEXEL_Y = 0.001953125f;
    private static final FloatBuffer stopVals = BufferUtil.newFloatBuffer(48);
    private static final ByteBuffer bgraColors = BufferUtil.newByteBuffer(64);
    private static final Image colorsImg = Image.fromByteBgraPreData(bgraColors, 16, 1);
    private static final int[] previousColors = new int[16];
    private static final byte[] gtexColors = new byte[804];
    private static final Image gtexImg = Image.fromByteBgraPreData(ByteBuffer.wrap(gtexColors), 201, 1);
    private static long cacheOffset = -1L;
    private static Texture gradientCacheTexture = null;
    private static Texture gtexCacheTexture = null;
    private static final WeakHashMap<Gradient, Void> gradientMap = new WeakHashMap();
    private static final Affine2D scratchXform2D = new Affine2D();
    private static final Affine3D scratchXform3D = new Affine3D();
    private static Color PINK = new Color(1.0f, 0.078431375f, 0.5764706f, 1.0f);

    PaintHelper() {
    }

    private static float len(float f, float f2) {
        return f == 0.0f ? Math.abs(f2) : (f2 == 0.0f ? Math.abs(f) : (float)Math.sqrt(f * f + f2 * f2));
    }

    static void initGradientTextures(ShaderGraphics shaderGraphics) {
        cacheOffset = -1L;
        gradientMap.clear();
        gradientCacheTexture = shaderGraphics.getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE, 16, 256);
        gradientCacheTexture.setLinearFiltering(true);
        gradientCacheTexture.contentsUseful();
        gradientCacheTexture.makePermanent();
        gtexCacheTexture = shaderGraphics.getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_NOT_NEEDED, 201, 256);
        gtexCacheTexture.setLinearFiltering(true);
        gtexCacheTexture.contentsUseful();
        gtexCacheTexture.makePermanent();
    }

    static Texture getGradientTexture(ShaderGraphics shaderGraphics, Gradient gradient) {
        if (gradientCacheTexture == null || gradientCacheTexture.isSurfaceLost()) {
            PaintHelper.initGradientTextures(shaderGraphics);
        }
        gradientCacheTexture.lock();
        return gradientCacheTexture;
    }

    static Texture getWrapGradientTexture(ShaderGraphics shaderGraphics) {
        if (gtexCacheTexture == null || gtexCacheTexture.isSurfaceLost()) {
            PaintHelper.initGradientTextures(shaderGraphics);
        }
        gtexCacheTexture.lock();
        return gtexCacheTexture;
    }

    private static void stopsToImage(List<Stop> list, int n) {
        if (n > 12) {
            throw new RuntimeException("Maximum number of gradient stops exceeded (paint uses " + n + " stops, but max is 12)");
        }
        bgraColors.clear();
        Color color = null;
        for (int i = 0; i < 16; ++i) {
            Color color2;
            if (i < n) {
                color = color2 = list.get(i).getColor();
            } else {
                color2 = color;
            }
            color2.putBgraPreBytes(bgraColors);
            int n2 = color2.getIntArgbPre();
            if (n2 == previousColors[i]) continue;
            PaintHelper.previousColors[i] = n2;
        }
        bgraColors.rewind();
    }

    private static void insertInterpColor(byte[] arrby, int n, Color color, Color color2, float f) {
        float f2 = 255.0f - (f *= 255.0f);
        arrby[(n *= 4) + 0] = (byte)(color.getBluePremult() * f2 + color2.getBluePremult() * f + 0.5f);
        arrby[n + 1] = (byte)(color.getGreenPremult() * f2 + color2.getGreenPremult() * f + 0.5f);
        arrby[n + 2] = (byte)(color.getRedPremult() * f2 + color2.getRedPremult() * f + 0.5f);
        arrby[n + 3] = (byte)(color.getAlpha() * f2 + color2.getAlpha() * f + 0.5f);
    }

    private static void stopsToGtexImage(List<Stop> list, int n) {
        int n2;
        int n3;
        Color color = list.get(0).getColor();
        float f = list.get(0).getOffset();
        int n4 = (int)(f * 100.0f + 0.5f);
        PaintHelper.insertInterpColor(gtexColors, 0, color, color, 0.0f);
        for (n3 = 1; n3 < n; ++n3) {
            Color color2 = list.get(n3).getColor();
            f = list.get(n3).getOffset();
            n2 = (int)(f * 100.0f + 0.5f);
            if (n2 == n4) {
                PaintHelper.insertInterpColor(gtexColors, n2, color, color2, 0.5f);
            } else {
                for (int i = n4 + 1; i <= n2; ++i) {
                    float f2 = i - n4;
                    PaintHelper.insertInterpColor(gtexColors, i, color, color2, f2 /= (float)(n2 - n4));
                }
            }
            n4 = n2;
            color = color2;
        }
        for (n3 = 1; n3 < 101; ++n3) {
            int n5 = (100 + n3) * 4;
            n2 = (100 - n3) * 4;
            PaintHelper.gtexColors[n5 + 0] = gtexColors[n2 + 0];
            PaintHelper.gtexColors[n5 + 1] = gtexColors[n2 + 1];
            PaintHelper.gtexColors[n5 + 2] = gtexColors[n2 + 2];
            PaintHelper.gtexColors[n5 + 3] = gtexColors[n2 + 3];
        }
    }

    public static int initGradient(Gradient gradient) {
        long l = gradient.getGradientOffset();
        if (gradientMap.containsKey(gradient) && l >= 0L && l > cacheOffset - 256L) {
            return (int)(l % 256L);
        }
        List<Stop> list = gradient.getStops();
        int n = gradient.getNumStops();
        PaintHelper.stopsToImage(list, n);
        PaintHelper.stopsToGtexImage(list, n);
        long l2 = ++cacheOffset;
        gradient.setGradientOffset(l2);
        int n2 = (int)(l2 % 256L);
        gradientCacheTexture.update(colorsImg, 0, n2);
        gtexCacheTexture.update(gtexImg, 0, n2);
        gradientMap.put(gradient, null);
        return n2;
    }

    private static void setMultiGradient(Shader shader, Gradient gradient) {
        List<Stop> list = gradient.getStops();
        int n = gradient.getNumStops();
        stopVals.clear();
        for (int i = 0; i < 12; ++i) {
            stopVals.put(i < n ? list.get(i).getOffset() : 0.0f);
            stopVals.put(i < n - 1 ? 1.0f / (list.get(i + 1).getOffset() - list.get(i).getOffset()) : 0.0f);
            stopVals.put(0.0f);
            stopVals.put(0.0f);
        }
        stopVals.rewind();
        shader.setConstants("fractions", stopVals, 0, 12);
        float f = PaintHelper.initGradient(gradient);
        shader.setConstant("offset", f / 256.0f + 0.001953125f);
    }

    private static void setTextureGradient(Shader shader, Gradient gradient) {
        float f = (float)PaintHelper.initGradient(gradient) + 0.5f;
        float f2 = 0.5f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        switch (gradient.getSpreadMethod()) {
            case 0: {
                f4 = 100.0f;
                break;
            }
            case 2: {
                f3 = 100.0f;
                break;
            }
            case 1: {
                f3 = 200.0f;
            }
        }
        float f5 = 1.0f / (float)gtexCacheTexture.getPhysicalWidth();
        float f6 = 1.0f / (float)gtexCacheTexture.getPhysicalHeight();
        shader.setConstant("content", f2 *= f5, f *= f6, f3 *= f5, f4 *= f5);
    }

    static void setLinearGradient(ShaderGraphics shaderGraphics, Shader shader, LinearGradient linearGradient, float f, float f2, float f3, float f4) {
        BaseTransform baseTransform = linearGradient.getGradientTransformNoClone();
        Affine3D affine3D = scratchXform3D;
        shaderGraphics.getPaintShaderTransform(affine3D);
        if (baseTransform != null) {
            affine3D.concatenate(baseTransform);
        }
        float f5 = f + linearGradient.getX1() * f3;
        float f6 = f2 + linearGradient.getY1() * f4;
        float f7 = f + linearGradient.getX2() * f3;
        float f8 = f2 + linearGradient.getY2() * f4;
        float f9 = f5;
        float f10 = f6;
        affine3D.translate(f9, f10);
        f9 = f7 - f9;
        f10 = f8 - f10;
        double d = PaintHelper.len(f9, f10);
        affine3D.rotate(Math.atan2(f10, f9));
        affine3D.scale(d, 1.0);
        if (!affine3D.is2D()) {
            BaseTransform baseTransform2;
            try {
                baseTransform2 = affine3D.createInverse();
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                affine3D.setToScale(0.0, 0.0, 0.0);
                baseTransform2 = affine3D;
            }
            NGCamera nGCamera = shaderGraphics.getCameraNoClone();
            Vec3d vec3d = new Vec3d();
            PickRay pickRay = new PickRay();
            PickRay pickRay2 = PaintHelper.project(0.0f, 0.0f, nGCamera, baseTransform2, pickRay, vec3d, null);
            PickRay pickRay3 = PaintHelper.project(1.0f, 0.0f, nGCamera, baseTransform2, pickRay, vec3d, null);
            PickRay pickRay4 = PaintHelper.project(0.0f, 1.0f, nGCamera, baseTransform2, pickRay, vec3d, null);
            double d2 = pickRay3.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d3 = pickRay4.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d4 = pickRay2.getDirectionNoClone().x;
            double d5 = pickRay3.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d6 = pickRay4.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d7 = pickRay2.getDirectionNoClone().z;
            shader.setConstant("gradParams", (float)(d2 *= -pickRay2.getOriginNoClone().z), (float)(d3 *= -pickRay2.getOriginNoClone().z), (float)(d4 *= -pickRay2.getOriginNoClone().z), (float)pickRay2.getOriginNoClone().x);
            shader.setConstant("perspVec", (float)d5, (float)d6, (float)d7);
        } else {
            try {
                affine3D.invert();
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                affine3D.setToScale(0.0, 0.0, 0.0);
            }
            double d8 = (float)affine3D.getMxx();
            double d9 = (float)affine3D.getMxy();
            double d10 = (float)affine3D.getMxt();
            shader.setConstant("gradParams", (float)d8, (float)d9, (float)d10, 0.0f);
            shader.setConstant("perspVec", 0.0f, 0.0f, 1.0f);
        }
        PaintHelper.setMultiGradient(shader, linearGradient);
    }

    static AffineBase getLinearGradientTx(LinearGradient linearGradient, Shader shader, BaseTransform baseTransform, float f, float f2, float f3, float f4) {
        AffineBase affineBase;
        BaseTransform baseTransform2;
        float f5 = linearGradient.getX1();
        float f6 = linearGradient.getY1();
        float f7 = linearGradient.getX2();
        float f8 = linearGradient.getY2();
        if (linearGradient.isProportional()) {
            f5 = f + f5 * f3;
            f6 = f2 + f6 * f4;
            f7 = f + f7 * f3;
            f8 = f2 + f8 * f4;
        }
        float f9 = f7 - f5;
        float f10 = f8 - f6;
        float f11 = PaintHelper.len(f9, f10);
        if (linearGradient.getSpreadMethod() == 1) {
            f11 *= 2.0f;
        }
        if ((baseTransform2 = linearGradient.getGradientTransformNoClone()).isIdentity() && baseTransform.isIdentity()) {
            Affine2D affine2D = scratchXform2D;
            affine2D.setToTranslation(f5, f6);
            affine2D.rotate(f9, f10);
            affine2D.scale(f11, 1.0);
            affineBase = affine2D;
        } else {
            Affine3D affine3D = scratchXform3D;
            affine3D.setTransform(baseTransform);
            affine3D.concatenate(baseTransform2);
            affine3D.translate(f5, f6);
            affine3D.rotate(Math.atan2(f10, f9));
            affine3D.scale(f11, 1.0);
            affineBase = affine3D;
        }
        try {
            affineBase.invert();
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            scratchXform2D.setToScale(0.0, 0.0);
            affineBase = scratchXform2D;
        }
        PaintHelper.setTextureGradient(shader, linearGradient);
        return affineBase;
    }

    static void setRadialGradient(ShaderGraphics shaderGraphics, Shader shader, RadialGradient radialGradient, float f, float f2, float f3, float f4) {
        BaseTransform baseTransform;
        float f5;
        Affine3D affine3D = scratchXform3D;
        shaderGraphics.getPaintShaderTransform(affine3D);
        float f6 = radialGradient.getRadius();
        float f7 = radialGradient.getCenterX();
        float f8 = radialGradient.getCenterY();
        float f9 = radialGradient.getFocusAngle();
        float f10 = radialGradient.getFocusDistance();
        if (f10 < 0.0f) {
            f10 = -f10;
            f9 += 180.0f;
        }
        f9 = (float)Math.toRadians(f9);
        if (radialGradient.isProportional()) {
            float f11 = f + f3 / 2.0f;
            float f12 = f2 + f4 / 2.0f;
            f5 = Math.min(f3, f4);
            f7 = (f7 - 0.5f) * f5 + f11;
            f8 = (f8 - 0.5f) * f5 + f12;
            if (f3 != f4 && f3 != 0.0f && f4 != 0.0f) {
                affine3D.translate(f11, f12);
                affine3D.scale(f3 / f5, f4 / f5);
                affine3D.translate(-f11, -f12);
            }
            f6 *= f5;
        }
        if ((baseTransform = radialGradient.getGradientTransformNoClone()) != null) {
            affine3D.concatenate(baseTransform);
        }
        affine3D.translate(f7, f8);
        affine3D.rotate(f9);
        affine3D.scale(f6, f6);
        try {
            affine3D.invert();
        }
        catch (Exception exception) {
            affine3D.setToScale(0.0, 0.0, 0.0);
        }
        if (!affine3D.is2D()) {
            NGCamera nGCamera = shaderGraphics.getCameraNoClone();
            Vec3d vec3d = new Vec3d();
            PickRay pickRay = new PickRay();
            PickRay pickRay2 = PaintHelper.project(0.0f, 0.0f, nGCamera, affine3D, pickRay, vec3d, null);
            PickRay pickRay3 = PaintHelper.project(1.0f, 0.0f, nGCamera, affine3D, pickRay, vec3d, null);
            PickRay pickRay4 = PaintHelper.project(0.0f, 1.0f, nGCamera, affine3D, pickRay, vec3d, null);
            double d = pickRay3.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d2 = pickRay4.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d3 = pickRay2.getDirectionNoClone().x;
            double d4 = pickRay3.getDirectionNoClone().y - pickRay2.getDirectionNoClone().y;
            double d5 = pickRay4.getDirectionNoClone().y - pickRay2.getDirectionNoClone().y;
            double d6 = pickRay2.getDirectionNoClone().y;
            double d7 = pickRay3.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d8 = pickRay4.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d9 = pickRay2.getDirectionNoClone().z;
            shader.setConstant("perspVec", (float)d7, (float)d8, (float)d9);
            shader.setConstant("m0", (float)(d *= -pickRay2.getOriginNoClone().z), (float)(d2 *= -pickRay2.getOriginNoClone().z), (float)(d3 *= -pickRay2.getOriginNoClone().z), (float)pickRay2.getOriginNoClone().x);
            shader.setConstant("m1", (float)(d4 *= -pickRay2.getOriginNoClone().z), (float)(d5 *= -pickRay2.getOriginNoClone().z), (float)(d6 *= -pickRay2.getOriginNoClone().z), (float)pickRay2.getOriginNoClone().y);
        } else {
            float f13 = (float)affine3D.getMxx();
            f5 = (float)affine3D.getMxy();
            float f14 = (float)affine3D.getMxt();
            shader.setConstant("m0", f13, f5, f14, 0.0f);
            float f15 = (float)affine3D.getMyx();
            float f16 = (float)affine3D.getMyy();
            float f17 = (float)affine3D.getMyt();
            shader.setConstant("m1", f15, f16, f17, 0.0f);
            shader.setConstant("perspVec", 0.0f, 0.0f, 1.0f);
        }
        f10 = Math.min(f10, 0.99f);
        float f18 = 1.0f - f10 * f10;
        float f19 = 1.0f / f18;
        shader.setConstant("precalc", f10, f18, f19);
        PaintHelper.setMultiGradient(shader, radialGradient);
    }

    static AffineBase getRadialGradientTx(RadialGradient radialGradient, Shader shader, BaseTransform baseTransform, float f, float f2, float f3, float f4) {
        BaseTransform baseTransform2;
        float f5;
        Affine3D affine3D = scratchXform3D;
        affine3D.setTransform(baseTransform);
        float f6 = radialGradient.getRadius();
        float f7 = radialGradient.getCenterX();
        float f8 = radialGradient.getCenterY();
        float f9 = radialGradient.getFocusAngle();
        float f10 = radialGradient.getFocusDistance();
        if (f10 < 0.0f) {
            f10 = -f10;
            f9 += 180.0f;
        }
        f9 = (float)Math.toRadians(f9);
        if (radialGradient.isProportional()) {
            float f11 = f + f3 / 2.0f;
            float f12 = f2 + f4 / 2.0f;
            f5 = Math.min(f3, f4);
            f7 = (f7 - 0.5f) * f5 + f11;
            f8 = (f8 - 0.5f) * f5 + f12;
            if (f3 != f4 && f3 != 0.0f && f4 != 0.0f) {
                affine3D.translate(f11, f12);
                affine3D.scale(f3 / f5, f4 / f5);
                affine3D.translate(-f11, -f12);
            }
            f6 *= f5;
        }
        if (radialGradient.getSpreadMethod() == 1) {
            f6 *= 2.0f;
        }
        if ((baseTransform2 = radialGradient.getGradientTransformNoClone()) != null) {
            affine3D.concatenate(baseTransform2);
        }
        affine3D.translate(f7, f8);
        affine3D.rotate(f9);
        affine3D.scale(f6, f6);
        try {
            affine3D.invert();
        }
        catch (Exception exception) {
            affine3D.setToScale(0.0, 0.0, 0.0);
        }
        f10 = Math.min(f10, 0.99f);
        float f13 = 1.0f - f10 * f10;
        f5 = 1.0f / f13;
        shader.setConstant("precalc", f10, f13, f5);
        PaintHelper.setTextureGradient(shader, radialGradient);
        return affine3D;
    }

    static void setImagePattern(ShaderGraphics shaderGraphics, Shader shader, ImagePattern imagePattern, float f, float f2, float f3, float f4) {
        float f5 = f + imagePattern.getX() * f3;
        float f6 = f2 + imagePattern.getY() * f4;
        float f7 = f5 + imagePattern.getWidth() * f3;
        float f8 = f6 + imagePattern.getHeight() * f4;
        ResourceFactory resourceFactory = shaderGraphics.getResourceFactory();
        Image image = imagePattern.getImage();
        Texture texture = resourceFactory.getCachedTexture(image, Texture.WrapMode.REPEAT);
        float f9 = texture.getContentX();
        float f10 = texture.getContentY();
        float f11 = texture.getContentWidth();
        float f12 = texture.getContentHeight();
        float f13 = texture.getPhysicalWidth();
        float f14 = texture.getPhysicalHeight();
        texture.unlock();
        Affine3D affine3D = scratchXform3D;
        shaderGraphics.getPaintShaderTransform(affine3D);
        BaseTransform baseTransform = imagePattern.getPatternTransformNoClone();
        if (baseTransform != null) {
            affine3D.concatenate(baseTransform);
        }
        affine3D.translate(f5, f6);
        affine3D.scale(f7 - f5, f8 - f6);
        if (f11 < f13) {
            affine3D.translate(0.5 / (double)f11, 0.0);
            f9 += 0.5f;
        }
        if (f12 < f14) {
            affine3D.translate(0.0, 0.5 / (double)f12);
            f10 += 0.5f;
        }
        try {
            affine3D.invert();
        }
        catch (Exception exception) {
            affine3D.setToScale(0.0, 0.0, 0.0);
        }
        if (!affine3D.is2D()) {
            NGCamera nGCamera = shaderGraphics.getCameraNoClone();
            Vec3d vec3d = new Vec3d();
            PickRay pickRay = new PickRay();
            PickRay pickRay2 = PaintHelper.project(0.0f, 0.0f, nGCamera, affine3D, pickRay, vec3d, null);
            PickRay pickRay3 = PaintHelper.project(1.0f, 0.0f, nGCamera, affine3D, pickRay, vec3d, null);
            PickRay pickRay4 = PaintHelper.project(0.0f, 1.0f, nGCamera, affine3D, pickRay, vec3d, null);
            double d = pickRay3.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d2 = pickRay4.getDirectionNoClone().x - pickRay2.getDirectionNoClone().x;
            double d3 = pickRay2.getDirectionNoClone().x;
            double d4 = pickRay3.getDirectionNoClone().y - pickRay2.getDirectionNoClone().y;
            double d5 = pickRay4.getDirectionNoClone().y - pickRay2.getDirectionNoClone().y;
            double d6 = pickRay2.getDirectionNoClone().y;
            double d7 = pickRay3.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d8 = pickRay4.getDirectionNoClone().z - pickRay2.getDirectionNoClone().z;
            double d9 = pickRay2.getDirectionNoClone().z;
            shader.setConstant("perspVec", (float)d7, (float)d8, (float)d9);
            shader.setConstant("xParams", (float)(d *= -pickRay2.getOriginNoClone().z), (float)(d2 *= -pickRay2.getOriginNoClone().z), (float)(d3 *= -pickRay2.getOriginNoClone().z), (float)pickRay2.getOriginNoClone().x);
            shader.setConstant("yParams", (float)(d4 *= -pickRay2.getOriginNoClone().z), (float)(d5 *= -pickRay2.getOriginNoClone().z), (float)(d6 *= -pickRay2.getOriginNoClone().z), (float)pickRay2.getOriginNoClone().y);
        } else {
            float f15 = (float)affine3D.getMxx();
            float f16 = (float)affine3D.getMxy();
            float f17 = (float)affine3D.getMxt();
            shader.setConstant("xParams", f15, f16, f17, 0.0f);
            float f18 = (float)affine3D.getMyx();
            float f19 = (float)affine3D.getMyy();
            float f20 = (float)affine3D.getMyt();
            shader.setConstant("yParams", f18, f19, f20, 0.0f);
            shader.setConstant("perspVec", 0.0f, 0.0f, 1.0f);
        }
        shader.setConstant("content", f9 /= f13, f10 /= f14, f11 /= f13, f12 /= f14);
    }

    static AffineBase getImagePatternTx(ShaderGraphics shaderGraphics, ImagePattern imagePattern, Shader shader, BaseTransform baseTransform, float f, float f2, float f3, float f4) {
        AffineBase affineBase;
        float f5 = imagePattern.getX();
        float f6 = imagePattern.getY();
        float f7 = imagePattern.getWidth();
        float f8 = imagePattern.getHeight();
        if (imagePattern.isProportional()) {
            f5 = f + f5 * f3;
            f6 = f2 + f6 * f4;
            f7 *= f3;
            f8 *= f4;
        }
        ResourceFactory resourceFactory = shaderGraphics.getResourceFactory();
        Image image = imagePattern.getImage();
        Texture texture = resourceFactory.getCachedTexture(image, Texture.WrapMode.REPEAT);
        float f9 = texture.getContentX();
        float f10 = texture.getContentY();
        float f11 = texture.getContentWidth();
        float f12 = texture.getContentHeight();
        float f13 = texture.getPhysicalWidth();
        float f14 = texture.getPhysicalHeight();
        texture.unlock();
        BaseTransform baseTransform2 = imagePattern.getPatternTransformNoClone();
        if (baseTransform2.isIdentity() && baseTransform.isIdentity()) {
            Affine2D affine2D = scratchXform2D;
            affine2D.setToTranslation(f5, f6);
            affine2D.scale(f7, f8);
            affineBase = affine2D;
        } else {
            Affine3D affine3D = scratchXform3D;
            affine3D.setTransform(baseTransform);
            affine3D.concatenate(baseTransform2);
            affine3D.translate(f5, f6);
            affine3D.scale(f7, f8);
            affineBase = affine3D;
        }
        if (f11 < f13) {
            affineBase.translate(0.5 / (double)f11, 0.0);
            f9 += 0.5f;
        }
        if (f12 < f14) {
            affineBase.translate(0.0, 0.5 / (double)f12);
            f10 += 0.5f;
        }
        try {
            affineBase.invert();
        }
        catch (Exception exception) {
            affineBase = scratchXform2D;
            scratchXform2D.setToScale(0.0, 0.0);
        }
        shader.setConstant("content", f9 /= f13, f10 /= f14, f11 /= f13, f12 /= f14);
        return affineBase;
    }

    static PickRay project(float f, float f2, NGCamera nGCamera, BaseTransform baseTransform, PickRay pickRay, Vec3d vec3d, Point2D point2D) {
        pickRay = nGCamera.computePickRay(f, f2, pickRay);
        return pickRay.project(baseTransform, nGCamera instanceof NGPerspectiveCamera, vec3d, point2D);
    }
}

