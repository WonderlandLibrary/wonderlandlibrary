/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Pixels;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.ViewPainter;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.QueuedPixelSource;
import java.nio.IntBuffer;

final class UploadingPainter
extends ViewPainter
implements Runnable {
    private RTTexture rttexture;
    private RTTexture resolveRTT = null;
    private QueuedPixelSource pixelSource = new QueuedPixelSource(true);
    private float penScaleX;
    private float penScaleY;

    UploadingPainter(GlassScene glassScene) {
        super(glassScene);
    }

    void disposeRTTexture() {
        if (this.rttexture != null) {
            this.rttexture.dispose();
            this.rttexture = null;
        }
        if (this.resolveRTT != null) {
            this.resolveRTT.dispose();
            this.resolveRTT = null;
        }
    }

    @Override
    public float getPixelScaleFactorX() {
        return this.sceneState.getRenderScaleX();
    }

    @Override
    public float getPixelScaleFactorY() {
        return this.sceneState.getRenderScaleY();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        renderLock.lock();
        boolean bl = false;
        try {
            Graphics graphics;
            boolean bl2;
            if (!this.validateStageGraphics()) {
                if (QuantumToolkit.verbose) {
                    System.err.println("UploadingPainter: validateStageGraphics failed");
                }
                this.paintImpl(null);
                return;
            }
            if (this.factory == null) {
                this.factory = GraphicsPipeline.getDefaultResourceFactory();
            }
            if (this.factory == null || !this.factory.isDeviceReady()) {
                this.factory = null;
                return;
            }
            float f = this.getPixelScaleFactorX();
            float f2 = this.getPixelScaleFactorY();
            int n = this.sceneState.getRenderWidth();
            int n2 = this.sceneState.getRenderHeight();
            boolean bl3 = bl2 = this.penScaleX != f || this.penScaleY != f2 || this.penWidth != this.viewWidth || this.penHeight != this.viewHeight || this.rttexture == null || this.rttexture.getContentWidth() != n || this.rttexture.getContentHeight() != n2;
            if (!bl2) {
                this.rttexture.lock();
                if (this.rttexture.isSurfaceLost()) {
                    this.rttexture.unlock();
                    this.sceneState.getScene().entireSceneNeedsRepaint();
                    bl2 = true;
                }
            }
            if (bl2) {
                this.disposeRTTexture();
                this.rttexture = this.factory.createRTTexture(n, n2, Texture.WrapMode.CLAMP_NOT_NEEDED, this.sceneState.isMSAA());
                if (this.rttexture == null) {
                    return;
                }
                this.penScaleX = f;
                this.penScaleY = f2;
                this.penWidth = this.viewWidth;
                this.penHeight = this.viewHeight;
                this.freshBackBuffer = true;
            }
            if ((graphics = this.rttexture.createGraphics()) == null) {
                this.disposeRTTexture();
                this.sceneState.getScene().entireSceneNeedsRepaint();
                return;
            }
            graphics.scale(f, f2);
            this.paintImpl(graphics);
            this.freshBackBuffer = false;
            int n3 = this.sceneState.getOutputWidth();
            int n4 = this.sceneState.getOutputHeight();
            float f3 = this.sceneState.getOutputScaleX();
            float f4 = this.sceneState.getOutputScaleY();
            RTTexture rTTexture = this.rttexture.isMSAA() || n3 != n || n4 != n2 ? this.resolveRenderTarget(graphics, n3, n4) : this.rttexture;
            Pixels pixels = this.pixelSource.getUnusedPixels(n3, n4, f3, f4);
            IntBuffer intBuffer = (IntBuffer)pixels.getPixels();
            int[] arrn = rTTexture.getPixels();
            if (arrn != null) {
                intBuffer.put(arrn, 0, n3 * n4);
            } else if (!rTTexture.readPixels(intBuffer)) {
                this.sceneState.getScene().entireSceneNeedsRepaint();
                this.disposeRTTexture();
                pixels = null;
            }
            if (this.rttexture != null) {
                this.rttexture.unlock();
            }
            if (pixels != null) {
                this.pixelSource.enqueuePixels(pixels);
                this.sceneState.uploadPixels(this.pixelSource);
            }
        }
        catch (Throwable throwable) {
            bl = true;
            throwable.printStackTrace(System.err);
        }
        finally {
            if (this.rttexture != null && this.rttexture.isLocked()) {
                this.rttexture.unlock();
            }
            if (this.resolveRTT != null && this.resolveRTT.isLocked()) {
                this.resolveRTT.unlock();
            }
            Disposer.cleanUp();
            this.sceneState.getScene().setPainting(false);
            if (this.factory != null) {
                this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(bl);
            }
            renderLock.unlock();
        }
    }

    private RTTexture resolveRenderTarget(Graphics graphics, int n, int n2) {
        if (this.resolveRTT != null) {
            this.resolveRTT.lock();
            if (this.resolveRTT.isSurfaceLost() || this.resolveRTT.getContentWidth() != n || this.resolveRTT.getContentHeight() != n2) {
                this.resolveRTT.unlock();
                this.resolveRTT.dispose();
                this.resolveRTT = null;
            }
        }
        if (this.resolveRTT == null) {
            this.resolveRTT = graphics.getResourceFactory().createRTTexture(n, n2, Texture.WrapMode.CLAMP_NOT_NEEDED, false);
        }
        int n3 = this.rttexture.getContentWidth();
        int n4 = this.rttexture.getContentHeight();
        graphics.blit(this.rttexture, this.resolveRTT, 0, 0, n3, n4, 0, 0, n, n2);
        return this.resolveRTT;
    }
}

