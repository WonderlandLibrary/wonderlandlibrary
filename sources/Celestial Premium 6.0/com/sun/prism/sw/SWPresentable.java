/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.glass.ui.Pixels;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.impl.QueuedPixelSource;
import com.sun.prism.sw.SWRTTexture;
import com.sun.prism.sw.SWResourceFactory;
import java.nio.IntBuffer;

final class SWPresentable
extends SWRTTexture
implements Presentable {
    private final PresentableState pState;
    private Pixels pixels;
    private QueuedPixelSource pixelSource = new QueuedPixelSource(false);

    public SWPresentable(PresentableState presentableState, SWResourceFactory sWResourceFactory) {
        super(sWResourceFactory, presentableState.getRenderWidth(), presentableState.getRenderHeight());
        this.pState = presentableState;
    }

    @Override
    public boolean lockResources(PresentableState presentableState) {
        return this.getPhysicalWidth() != presentableState.getRenderWidth() || this.getPhysicalHeight() != presentableState.getRenderHeight();
    }

    @Override
    public boolean prepare(Rectangle rectangle) {
        if (!this.pState.isViewClosed()) {
            int n = this.getPhysicalWidth();
            int n2 = this.getPhysicalHeight();
            this.pixels = this.pixelSource.getUnusedPixels(n, n2, 1.0f, 1.0f);
            IntBuffer intBuffer = (IntBuffer)this.pixels.getPixels();
            IntBuffer intBuffer2 = this.getSurface().getDataIntBuffer();
            assert (intBuffer2.hasArray());
            System.arraycopy(intBuffer2.array(), 0, intBuffer.array(), 0, n * n2);
            return true;
        }
        return false;
    }

    @Override
    public boolean present() {
        this.pixelSource.enqueuePixels(this.pixels);
        this.pState.uploadPixels(this.pixelSource);
        return true;
    }

    @Override
    public float getPixelScaleFactorX() {
        return this.pState.getRenderScaleX();
    }

    @Override
    public float getPixelScaleFactorY() {
        return this.pState.getRenderScaleY();
    }

    @Override
    public int getContentWidth() {
        return this.pState.getOutputWidth();
    }

    @Override
    public int getContentHeight() {
        return this.pState.getOutputHeight();
    }

    @Override
    public boolean isMSAA() {
        return super.isMSAA();
    }
}

