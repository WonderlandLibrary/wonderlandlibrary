/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.scene.input.InputMethodRequests
 *  javafx.stage.StageStyle
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.View;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.TKDragGestureListener;
import com.sun.javafx.tk.TKDragSourceListener;
import com.sun.javafx.tk.TKDropTargetListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKSceneListener;
import com.sun.javafx.tk.TKScenePaintListener;
import com.sun.javafx.tk.quantum.GlassStage;
import com.sun.javafx.tk.quantum.PaintCollector;
import com.sun.javafx.tk.quantum.QuantumClipboard;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.SceneState;
import com.sun.javafx.tk.quantum.ViewPainter;
import com.sun.javafx.tk.quantum.WindowStage;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.input.InputMethodRequests;
import javafx.stage.StageStyle;

abstract class GlassScene
implements TKScene {
    private GlassStage stage;
    protected TKSceneListener sceneListener;
    protected TKDragGestureListener dragGestureListener;
    protected TKDragSourceListener dragSourceListener;
    protected TKDropTargetListener dropTargetListener;
    protected InputMethodRequests inputMethodRequests;
    private TKScenePaintListener scenePaintListener;
    private NGNode root;
    private NGCamera camera;
    protected Paint fillPaint;
    private volatile boolean entireSceneDirty = true;
    private boolean doPresent = true;
    private final AtomicBoolean painting = new AtomicBoolean(false);
    private final boolean depthBuffer;
    private final boolean msaa;
    SceneState sceneState;
    private AccessControlContext accessCtrlCtx = null;
    private NGLightBase[] lights;

    protected GlassScene(boolean bl, boolean bl2) {
        this.msaa = bl2;
        this.depthBuffer = bl;
        this.sceneState = new SceneState(this);
    }

    @Override
    public void dispose() {
        assert (this.stage == null);
        this.setTKScenePaintListener(null);
        this.root = null;
        this.camera = null;
        this.fillPaint = null;
        this.sceneListener = null;
        this.dragGestureListener = null;
        this.dragSourceListener = null;
        this.dropTargetListener = null;
        this.inputMethodRequests = null;
        this.sceneState = null;
    }

    @Override
    public final AccessControlContext getAccessControlContext() {
        if (this.accessCtrlCtx == null) {
            throw new RuntimeException("Scene security context has not been set!");
        }
        return this.accessCtrlCtx;
    }

    public final void setSecurityContext(AccessControlContext accessControlContext) {
        if (this.accessCtrlCtx != null) {
            throw new RuntimeException("Scene security context has been already set!");
        }
        AccessControlContext accessControlContext2 = AccessController.getContext();
        this.accessCtrlCtx = GlassStage.doIntersectionPrivilege(() -> AccessController.getContext(), accessControlContext2, accessControlContext);
    }

    @Override
    public void waitForRenderingToComplete() {
        PaintCollector.getInstance().waitForRenderingToComplete();
    }

    @Override
    public void waitForSynchronization() {
        ViewPainter.renderLock.lock();
    }

    @Override
    public void releaseSynchronization(boolean bl) {
        if (bl) {
            this.updateSceneState();
        }
        ViewPainter.renderLock.unlock();
    }

    boolean getDepthBuffer() {
        return this.depthBuffer;
    }

    boolean isMSAA() {
        return this.msaa;
    }

    protected abstract boolean isSynchronous();

    @Override
    public void setTKSceneListener(TKSceneListener tKSceneListener) {
        this.sceneListener = tKSceneListener;
    }

    @Override
    public synchronized void setTKScenePaintListener(TKScenePaintListener tKScenePaintListener) {
        this.scenePaintListener = tKScenePaintListener;
    }

    public void setTKDropTargetListener(TKDropTargetListener tKDropTargetListener) {
        this.dropTargetListener = tKDropTargetListener;
    }

    public void setTKDragSourceListener(TKDragSourceListener tKDragSourceListener) {
        this.dragSourceListener = tKDragSourceListener;
    }

    public void setTKDragGestureListener(TKDragGestureListener tKDragGestureListener) {
        this.dragGestureListener = tKDragGestureListener;
    }

    public void setInputMethodRequests(InputMethodRequests inputMethodRequests) {
        this.inputMethodRequests = inputMethodRequests;
    }

    @Override
    public void setRoot(NGNode nGNode) {
        this.root = nGNode;
        this.entireSceneNeedsRepaint();
    }

    protected NGNode getRoot() {
        return this.root;
    }

    NGCamera getCamera() {
        return this.camera;
    }

    @Override
    public NGLightBase[] getLights() {
        return this.lights;
    }

    @Override
    public void setLights(NGLightBase[] arrnGLightBase) {
        this.lights = arrnGLightBase;
    }

    @Override
    public void setCamera(NGCamera nGCamera) {
        this.camera = nGCamera == null ? NGCamera.INSTANCE : nGCamera;
        this.entireSceneNeedsRepaint();
    }

    @Override
    public void setFillPaint(Object object) {
        this.fillPaint = (Paint)object;
        this.entireSceneNeedsRepaint();
    }

    @Override
    public void setCursor(Object object) {
    }

    @Override
    public final void markDirty() {
        this.sceneChanged();
    }

    @Override
    public void entireSceneNeedsRepaint() {
        if (Platform.isFxApplicationThread()) {
            this.entireSceneDirty = true;
            this.sceneChanged();
        } else {
            Platform.runLater(() -> {
                this.entireSceneDirty = true;
                this.sceneChanged();
            });
        }
    }

    public boolean isEntireSceneDirty() {
        return this.entireSceneDirty;
    }

    public void clearEntireSceneDirty() {
        this.entireSceneDirty = false;
    }

    @Override
    public TKClipboard createDragboard(boolean bl) {
        ClipboardAssistance clipboardAssistance = new ClipboardAssistance("DND"){

            @Override
            public void actionPerformed(int n) {
                super.actionPerformed(n);
                AccessController.doPrivileged(() -> {
                    try {
                        if (GlassScene.this.dragSourceListener != null) {
                            GlassScene.this.dragSourceListener.dragDropEnd(0.0, 0.0, 0.0, 0.0, QuantumToolkit.clipboardActionToTransferMode(n));
                        }
                    }
                    finally {
                        QuantumClipboard.releaseCurrentDragboard();
                    }
                    return null;
                }, GlassScene.this.getAccessControlContext());
            }
        };
        return QuantumClipboard.getDragboardInstance(clipboardAssistance, bl);
    }

    protected final GlassStage getStage() {
        return this.stage;
    }

    void setStage(GlassStage glassStage) {
        this.stage = glassStage;
        this.sceneChanged();
    }

    final SceneState getSceneState() {
        return this.sceneState;
    }

    final void updateSceneState() {
        this.sceneState.update();
    }

    protected View getPlatformView() {
        return null;
    }

    boolean setPainting(boolean bl) {
        return this.painting.getAndSet(bl);
    }

    void repaint() {
    }

    final void stageVisible(boolean bl) {
        if (!bl && PrismSettings.forceRepaint) {
            PaintCollector.getInstance().removeDirtyScene(this);
        }
        if (bl) {
            PaintCollector.getInstance().addDirtyScene(this);
        }
    }

    public void sceneChanged() {
        if (this.stage != null) {
            PaintCollector.getInstance().addDirtyScene(this);
        } else {
            PaintCollector.getInstance().removeDirtyScene(this);
        }
    }

    public final synchronized void frameRendered() {
        if (this.scenePaintListener != null) {
            this.scenePaintListener.frameRendered();
        }
    }

    public final synchronized void setDoPresent(boolean bl) {
        this.doPresent = bl;
    }

    public final synchronized boolean getDoPresent() {
        return this.doPresent;
    }

    protected Color getClearColor() {
        WindowStage windowStage;
        WindowStage windowStage2 = windowStage = this.stage instanceof WindowStage ? (WindowStage)this.stage : null;
        if (windowStage != null && windowStage.getPlatformWindow().isTransparentWindow()) {
            return Color.TRANSPARENT;
        }
        if (this.fillPaint == null) {
            return Color.WHITE;
        }
        if (this.fillPaint.isOpaque() || windowStage != null && windowStage.getPlatformWindow().isUnifiedWindow()) {
            if (this.fillPaint.getType() == Paint.Type.COLOR) {
                return (Color)this.fillPaint;
            }
            if (this.depthBuffer) {
                return Color.TRANSPARENT;
            }
            return null;
        }
        return Color.WHITE;
    }

    final Paint getCurrentPaint() {
        WindowStage windowStage;
        WindowStage windowStage2 = windowStage = this.stage instanceof WindowStage ? (WindowStage)this.stage : null;
        if (windowStage != null && windowStage.getStyle() == StageStyle.TRANSPARENT) {
            return Color.TRANSPARENT.equals(this.fillPaint) ? null : this.fillPaint;
        }
        if (this.fillPaint != null && this.fillPaint.isOpaque() && this.fillPaint.getType() == Paint.Type.COLOR) {
            return null;
        }
        return this.fillPaint;
    }

    public String toString() {
        return " scene: " + this.hashCode() + ")";
    }
}

