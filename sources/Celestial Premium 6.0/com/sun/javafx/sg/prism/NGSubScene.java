/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

public class NGSubScene
extends NGNode {
    private float slWidth;
    private float slHeight;
    private double lastScaledW;
    private double lastScaledH;
    private RTTexture rtt;
    private RTTexture resolveRTT = null;
    private NGNode root = null;
    private boolean renderSG = true;
    private final boolean depthBuffer;
    private final boolean msaa;
    private Paint fillPaint;
    private NGCamera camera;
    private NGLightBase[] lights;
    private boolean isOpaque = false;
    static final double THRESHOLD = 0.00390625;

    public NGSubScene(boolean bl, boolean bl2) {
        this.depthBuffer = bl;
        this.msaa = bl2;
    }

    private NGSubScene() {
        this(false, false);
    }

    public void setRoot(NGNode nGNode) {
        this.root = nGNode;
    }

    public void setFillPaint(Object object) {
        this.fillPaint = (Paint)object;
    }

    public void setCamera(NGCamera nGCamera) {
        this.camera = nGCamera == null ? NGCamera.INSTANCE : nGCamera;
    }

    public void setWidth(float f) {
        if (this.slWidth != f) {
            this.slWidth = f;
            this.geometryChanged();
            this.invalidateRTT();
        }
    }

    public void setHeight(float f) {
        if (this.slHeight != f) {
            this.slHeight = f;
            this.geometryChanged();
            this.invalidateRTT();
        }
    }

    public NGLightBase[] getLights() {
        return this.lights;
    }

    public void setLights(NGLightBase[] arrnGLightBase) {
        this.lights = arrnGLightBase;
    }

    public void markContentDirty() {
        this.visualsChanged();
    }

    @Override
    protected void visualsChanged() {
        this.renderSG = true;
        super.visualsChanged();
    }

    @Override
    protected void geometryChanged() {
        this.renderSG = true;
        super.geometryChanged();
    }

    private void invalidateRTT() {
        if (this.rtt != null) {
            this.rtt.dispose();
            this.rtt = null;
        }
    }

    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }

    private void applyBackgroundFillPaint(Graphics graphics) {
        this.isOpaque = true;
        if (this.fillPaint != null) {
            if (this.fillPaint instanceof Color) {
                Color color = (Color)this.fillPaint;
                this.isOpaque = (double)color.getAlpha() >= 1.0;
                graphics.clear(color);
            } else {
                if (!this.fillPaint.isOpaque()) {
                    graphics.clear();
                    this.isOpaque = false;
                }
                graphics.setPaint(this.fillPaint);
                graphics.fillRect(0.0f, 0.0f, this.rtt.getContentWidth(), this.rtt.getContentHeight());
            }
        } else {
            this.isOpaque = false;
            graphics.clear();
        }
    }

    @Override
    public void renderForcedContent(Graphics graphics) {
        this.root.renderForcedContent(graphics);
    }

    private static double hypot(double d, double d2, double d3) {
        return Math.sqrt(d * d + d2 * d2 + d3 * d3);
    }

    @Override
    protected void renderContent(Graphics graphics) {
        if ((double)this.slWidth <= 0.0 || (double)this.slHeight <= 0.0) {
            return;
        }
        BaseTransform baseTransform = graphics.getTransformNoClone();
        double d = NGSubScene.hypot(baseTransform.getMxx(), baseTransform.getMyx(), baseTransform.getMzx());
        double d2 = NGSubScene.hypot(baseTransform.getMxy(), baseTransform.getMyy(), baseTransform.getMzy());
        double d3 = (double)this.slWidth * d;
        double d4 = (double)this.slHeight * d2;
        int n = (int)Math.ceil(d3 - 0.00390625);
        int n2 = (int)Math.ceil(d4 - 0.00390625);
        if (Math.max(Math.abs(d3 - this.lastScaledW), Math.abs(d4 - this.lastScaledH)) > 0.00390625) {
            if (this.rtt != null && (n != this.rtt.getContentWidth() || n2 != this.rtt.getContentHeight())) {
                this.invalidateRTT();
            }
            this.renderSG = true;
            this.lastScaledW = d3;
            this.lastScaledH = d4;
        }
        if (this.rtt != null) {
            this.rtt.lock();
            if (this.rtt.isSurfaceLost()) {
                this.renderSG = true;
                this.rtt = null;
            }
        }
        if (this.renderSG || !this.root.isClean()) {
            Object object;
            if (this.rtt == null) {
                object = graphics.getResourceFactory();
                this.rtt = object.createRTTexture(n, n2, Texture.WrapMode.CLAMP_TO_ZERO, this.msaa);
            }
            object = this.rtt.createGraphics();
            object.scale((float)d, (float)d2);
            object.setLights(this.lights);
            object.setDepthBuffer(this.depthBuffer);
            if (this.camera != null) {
                object.setCamera(this.camera);
            }
            this.applyBackgroundFillPaint((Graphics)object);
            this.root.render((Graphics)object);
            this.root.clearDirtyTree();
            this.renderSG = false;
        }
        if (this.msaa) {
            int n3 = this.rtt.getContentX();
            int n4 = this.rtt.getContentY();
            int n5 = n3 + n;
            int n6 = n4 + n2;
            if ((this.isOpaque || graphics.getCompositeMode() == CompositeMode.SRC) && NGSubScene.isDirectBlitTransform(baseTransform, d, d2) && !graphics.isDepthTest()) {
                int n7 = (int)(baseTransform.getMxt() + 0.5);
                int n8 = (int)(baseTransform.getMyt() + 0.5);
                RenderTarget renderTarget = graphics.getRenderTarget();
                int n9 = renderTarget.getContentX() + n7;
                int n10 = renderTarget.getContentY() + n8;
                int n11 = n9 + n;
                int n12 = n10 + n2;
                int n13 = renderTarget.getContentWidth();
                int n14 = renderTarget.getContentHeight();
                int n15 = n11 > n13 ? n13 - n11 : 0;
                int n16 = n12 > n14 ? n14 - n12 : 0;
                graphics.blit(this.rtt, null, n3, n4, n5 + n15, n6 + n16, n9, n10, n11 + n15, n12 + n16);
            } else {
                if (this.resolveRTT != null && (this.resolveRTT.getContentWidth() < n || this.resolveRTT.getContentHeight() < n2)) {
                    this.resolveRTT.dispose();
                    this.resolveRTT = null;
                }
                if (this.resolveRTT != null) {
                    this.resolveRTT.lock();
                    if (this.resolveRTT.isSurfaceLost()) {
                        this.resolveRTT = null;
                    }
                }
                if (this.resolveRTT == null) {
                    this.resolveRTT = graphics.getResourceFactory().createRTTexture(n, n2, Texture.WrapMode.CLAMP_TO_ZERO, false);
                }
                this.resolveRTT.createGraphics().blit(this.rtt, this.resolveRTT, n3, n4, n5, n6, n3, n4, n5, n6);
                graphics.drawTexture(this.resolveRTT, 0.0f, 0.0f, (float)((double)n / d), (float)((double)n2 / d2), 0.0f, 0.0f, n, n2);
                this.resolveRTT.unlock();
            }
        } else {
            graphics.drawTexture(this.rtt, 0.0f, 0.0f, (float)((double)n / d), (float)((double)n2 / d2), 0.0f, 0.0f, n, n2);
        }
        this.rtt.unlock();
    }

    private static boolean isDirectBlitTransform(BaseTransform baseTransform, double d, double d2) {
        if (d == 1.0 && d2 == 1.0) {
            return baseTransform.isTranslateOrIdentity();
        }
        if (!baseTransform.is2D()) {
            return false;
        }
        return baseTransform.getMxx() == d && baseTransform.getMxy() == 0.0 && baseTransform.getMyx() == 0.0 && baseTransform.getMyy() == d2;
    }

    public NGCamera getCamera() {
        return this.camera;
    }
}

