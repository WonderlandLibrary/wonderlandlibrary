/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.CursorUtils;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.GlassStage;
import com.sun.javafx.tk.quantum.GlassViewEventHandler;
import com.sun.javafx.tk.quantum.OverlayWarning;
import com.sun.javafx.tk.quantum.PaintCollector;
import com.sun.javafx.tk.quantum.PaintRenderJob;
import com.sun.javafx.tk.quantum.PresentingPainter;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.UploadingPainter;
import com.sun.javafx.tk.quantum.ViewPainter;
import com.sun.javafx.tk.quantum.WindowStage;
import com.sun.prism.GraphicsPipeline;
import java.nio.ByteOrder;
import javafx.scene.Node;

class ViewScene
extends GlassScene {
    private static final String UNSUPPORTED_FORMAT = "Transparent windows only supported for BYTE_BGRA_PRE format on LITTLE_ENDIAN machines";
    private View platformView = Application.GetApplication().createView();
    private ViewPainter painter;
    private PaintRenderJob paintRenderJob;

    public ViewScene(boolean bl, boolean bl2) {
        super(bl, bl2);
        this.platformView.setEventHandler(new GlassViewEventHandler(this));
    }

    @Override
    protected boolean isSynchronous() {
        return this.painter != null && this.painter instanceof PresentingPainter;
    }

    @Override
    protected View getPlatformView() {
        return this.platformView;
    }

    ViewPainter getPainter() {
        return this.painter;
    }

    @Override
    public void setStage(GlassStage glassStage) {
        super.setStage(glassStage);
        if (glassStage != null) {
            WindowStage windowStage = (WindowStage)glassStage;
            if (windowStage.needsUpdateWindow() || GraphicsPipeline.getPipeline().isUploading()) {
                if (Pixels.getNativeFormat() != 1 || ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN) {
                    throw new UnsupportedOperationException(UNSUPPORTED_FORMAT);
                }
                this.painter = new UploadingPainter(this);
            } else {
                this.painter = new PresentingPainter(this);
            }
            this.painter.setRoot(this.getRoot());
            this.paintRenderJob = new PaintRenderJob(this, PaintCollector.getInstance().getRendered(), this.painter);
        }
    }

    WindowStage getWindowStage() {
        return (WindowStage)this.getStage();
    }

    @Override
    public void dispose() {
        if (this.platformView != null) {
            QuantumToolkit.runWithRenderLock(() -> {
                this.platformView.close();
                this.platformView = null;
                this.updateSceneState();
                this.painter = null;
                this.paintRenderJob = null;
                return null;
            });
        }
        super.dispose();
    }

    @Override
    public void setRoot(NGNode nGNode) {
        super.setRoot(nGNode);
        if (this.painter != null) {
            this.painter.setRoot(nGNode);
        }
    }

    @Override
    public void setCursor(Object object) {
        super.setCursor(object);
        Application.invokeLater(() -> {
            Window window;
            CursorFrame cursorFrame = (CursorFrame)object;
            Cursor cursor = CursorUtils.getPlatformCursor(cursorFrame);
            if (this.platformView != null && (window = this.platformView.getWindow()) != null) {
                window.setCursor(cursor);
            }
        });
    }

    @Override
    void repaint() {
        if (this.platformView == null) {
            return;
        }
        if (!this.setPainting(true)) {
            Toolkit toolkit = Toolkit.getToolkit();
            toolkit.addRenderJob(this.paintRenderJob);
        }
    }

    @Override
    public void enableInputMethodEvents(boolean bl) {
        this.platformView.enableInputMethodEvents(bl);
    }

    @Override
    public void finishInputMethodComposition() {
        this.platformView.finishInputMethodComposition();
    }

    @Override
    public String toString() {
        View view = this.getPlatformView();
        return " scene: " + this.hashCode() + " @ (" + view.getWidth() + "," + view.getHeight() + ")";
    }

    void synchroniseOverlayWarning() {
        try {
            this.waitForSynchronization();
            OverlayWarning overlayWarning = this.getWindowStage().getWarning();
            if (overlayWarning == null) {
                this.painter.setOverlayRoot(null);
            } else {
                this.painter.setOverlayRoot((NGNode)NodeHelper.getPeer((Node)overlayWarning));
                overlayWarning.updateBounds();
                NodeHelper.updatePeer((Node)overlayWarning);
            }
        }
        finally {
            this.releaseSynchronization(true);
            this.entireSceneNeedsRepaint();
        }
    }
}

