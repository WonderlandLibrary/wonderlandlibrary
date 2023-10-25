/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import com.sun.prism.Graphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.ImagePool;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrRenderInfo;
import com.sun.scenario.effect.impl.prism.PrTexture;

public class PrEffectHelper {
    public static void render(Effect effect, Graphics graphics, float f, float f2, Effect effect2) {
        boolean bl;
        PrFilterContext prFilterContext;
        NGCamera nGCamera;
        BaseTransform baseTransform;
        BaseTransform baseTransform2;
        Rectangle rectangle = PrEffectHelper.getGraphicsClipNoClone(graphics);
        BaseTransform baseTransform3 = graphics.getTransformNoClone().copy();
        if (baseTransform3.is2D()) {
            if (f != 0.0f || f2 != 0.0f || !baseTransform3.isIdentity()) {
                baseTransform2 = new Affine2D(baseTransform3);
                baseTransform2.translate(f, f2);
            } else {
                baseTransform2 = BaseTransform.IDENTITY_TRANSFORM;
            }
            graphics.setTransform(null);
            baseTransform = null;
        } else {
            BaseTransform baseTransform4;
            double d;
            double d2 = Math.hypot(baseTransform3.getMxx(), baseTransform3.getMyx());
            double d3 = Math.max(d2, d = Math.hypot(baseTransform3.getMxy(), baseTransform3.getMyy()));
            if (d3 <= 1.0) {
                baseTransform2 = BaseTransform.IDENTITY_TRANSFORM;
                baseTransform = baseTransform3;
            } else {
                baseTransform2 = BaseTransform.getScaleInstance(d3, d3);
                baseTransform = new Affine3D(baseTransform3);
                d3 = 1.0 / d3;
                ((Affine3D)baseTransform).scale(d3, d3);
            }
            nGCamera = graphics.getCameraNoClone();
            try {
                baseTransform4 = baseTransform.createInverse();
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                return;
            }
            PickRay pickRay = new PickRay();
            Vec3d vec3d = new Vec3d();
            float f3 = (float)rectangle.x + 0.5f;
            float f4 = (float)rectangle.y + 0.5f;
            float f5 = (float)(rectangle.x + rectangle.width) - 0.5f;
            float f6 = (float)(rectangle.y + rectangle.height) - 0.5f;
            double d4 = graphics.getRenderTarget().getContentWidth();
            double d5 = graphics.getRenderTarget().getContentHeight();
            Point2D point2D = PrEffectHelper.project(f3, f4, d4, d5, nGCamera, baseTransform4, pickRay, vec3d, null);
            Point2D point2D2 = PrEffectHelper.project(f5, f4, d4, d5, nGCamera, baseTransform4, pickRay, vec3d, null);
            Point2D point2D3 = PrEffectHelper.project(f3, f6, d4, d5, nGCamera, baseTransform4, pickRay, vec3d, null);
            Point2D point2D4 = PrEffectHelper.project(f5, f6, d4, d5, nGCamera, baseTransform4, pickRay, vec3d, null);
            rectangle = PrEffectHelper.clipbounds(point2D, point2D2, point2D3, point2D4);
        }
        Screen screen = graphics.getAssociatedScreen();
        if (screen == null) {
            ResourceFactory resourceFactory = graphics.getResourceFactory();
            prFilterContext = PrFilterContext.getPrinterContext(resourceFactory);
        } else {
            prFilterContext = PrFilterContext.getInstance(screen);
        }
        PrRenderInfo prRenderInfo = baseTransform != null ? null : (graphics.isDepthBuffer() && graphics.isDepthTest() ? null : new PrRenderInfo(graphics));
        ++ImagePool.numEffects;
        do {
            ImageData imageData;
            if ((imageData = effect.filter(prFilterContext, baseTransform2, rectangle, prRenderInfo, effect2)) == null) {
                return;
            }
            bl = imageData.validate(prFilterContext);
            if (bl) {
                Rectangle rectangle2 = imageData.getUntransformedBounds();
                nGCamera = ((PrTexture)((Object)imageData.getUntransformedImage())).getTextureObject();
                graphics.setTransform(baseTransform);
                graphics.transform(imageData.getTransform());
                graphics.drawTexture((Texture)((Object)nGCamera), rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height);
            }
            imageData.unref();
        } while (!bl);
        graphics.setTransform(baseTransform3);
    }

    static Point2D project(float f, float f2, double d, double d2, NGCamera nGCamera, BaseTransform baseTransform, PickRay pickRay, Vec3d vec3d, Point2D point2D) {
        double d3 = nGCamera.getViewWidth() / d;
        double d4 = nGCamera.getViewHeight() / d2;
        f = (float)((double)f * d3);
        f2 = (float)((double)f2 * d4);
        pickRay = nGCamera.computePickRay(f, f2, pickRay);
        PrEffectHelper.unscale(pickRay.getOriginNoClone(), d3, d4);
        PrEffectHelper.unscale(pickRay.getDirectionNoClone(), d3, d4);
        return pickRay.projectToZeroPlane(baseTransform, nGCamera instanceof NGPerspectiveCamera, vec3d, point2D);
    }

    private static void unscale(Vec3d vec3d, double d, double d2) {
        vec3d.x /= d;
        vec3d.y /= d2;
    }

    static Rectangle clipbounds(Point2D point2D, Point2D point2D2, Point2D point2D3, Point2D point2D4) {
        if (point2D != null && point2D2 != null && point2D3 != null && point2D4 != null) {
            double d;
            double d2;
            double d3;
            double d4;
            if (point2D.x < point2D2.x) {
                d4 = point2D.x;
                d3 = point2D2.x;
            } else {
                d4 = point2D2.x;
                d3 = point2D.x;
            }
            if (point2D.y < point2D2.y) {
                d2 = point2D.y;
                d = point2D2.y;
            } else {
                d2 = point2D2.y;
                d = point2D.y;
            }
            if (point2D3.x < point2D4.x) {
                d4 = Math.min(d4, (double)point2D3.x);
                d3 = Math.max(d3, (double)point2D4.x);
            } else {
                d4 = Math.min(d4, (double)point2D4.x);
                d3 = Math.max(d3, (double)point2D3.x);
            }
            if (point2D3.y < point2D4.y) {
                d2 = Math.min(d2, (double)point2D3.y);
                d = Math.max(d, (double)point2D4.y);
            } else {
                d2 = Math.min(d2, (double)point2D4.y);
                d = Math.max(d, (double)point2D3.y);
            }
            d4 = Math.floor(d4 - 0.5);
            d2 = Math.floor(d2 - 0.5);
            d3 = Math.ceil(d3 + 0.5) - d4;
            d = Math.ceil(d + 0.5) - d2;
            int n = (int)d4;
            int n2 = (int)d2;
            int n3 = (int)d3;
            int n4 = (int)d;
            if ((double)n == d4 && (double)n2 == d2 && (double)n3 == d3 && (double)n4 == d) {
                return new Rectangle(n, n2, n3, n4);
            }
        }
        return null;
    }

    public static Rectangle getGraphicsClipNoClone(Graphics graphics) {
        Rectangle rectangle = graphics.getClipRectNoClone();
        if (rectangle == null) {
            RenderTarget renderTarget = graphics.getRenderTarget();
            rectangle = new Rectangle(renderTarget.getContentWidth(), renderTarget.getContentHeight());
        }
        return rectangle;
    }

    public static void renderImageData(Graphics graphics, ImageData imageData, Rectangle rectangle) {
        int n = rectangle.width;
        int n2 = rectangle.height;
        PrDrawable prDrawable = (PrDrawable)imageData.getUntransformedImage();
        BaseTransform baseTransform = imageData.getTransform();
        Rectangle rectangle2 = imageData.getUntransformedBounds();
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = f + (float)n;
        float f4 = f2 + (float)n2;
        if (baseTransform.isTranslateOrIdentity()) {
            float f5 = (float)baseTransform.getMxt();
            float f6 = (float)baseTransform.getMyt();
            float f7 = (float)rectangle.x - ((float)rectangle2.x + f5);
            float f8 = (float)rectangle.y - ((float)rectangle2.y + f6);
            float f9 = f7 + (float)n;
            float f10 = f8 + (float)n2;
            graphics.drawTexture((Texture)prDrawable.getTextureObject(), f, f2, f3, f4, f7, f8, f9, f10);
        } else {
            float[] arrf = new float[8];
            int n3 = EffectPeer.getTextureCoordinates(arrf, rectangle2.x, rectangle2.y, prDrawable.getPhysicalWidth(), prDrawable.getPhysicalHeight(), rectangle, baseTransform);
            if (n3 < 8) {
                graphics.drawTextureRaw((Texture)prDrawable.getTextureObject(), f, f2, f3, f4, arrf[0], arrf[1], arrf[2], arrf[3]);
            } else {
                graphics.drawMappedTextureRaw((Texture)prDrawable.getTextureObject(), f, f2, f3, f4, arrf[0], arrf[1], arrf[4], arrf[5], arrf[6], arrf[7], arrf[2], arrf[3]);
            }
        }
    }
}

