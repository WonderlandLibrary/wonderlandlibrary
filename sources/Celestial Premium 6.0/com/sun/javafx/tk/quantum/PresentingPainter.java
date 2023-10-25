/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PulseLogger
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.ViewPainter;
import com.sun.javafx.tk.quantum.ViewScene;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.impl.Disposer;

final class PresentingPainter
extends ViewPainter {
    PresentingPainter(ViewScene viewScene) {
        super(viewScene);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        renderLock.lock();
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        try {
            bl2 = this.validateStageGraphics();
            if (!bl2) {
                if (QuantumToolkit.verbose) {
                    System.err.println("PresentingPainter: validateStageGraphics failed");
                }
                this.paintImpl(null);
                return;
            }
            this.sceneState.lock();
            bl = true;
            if (this.factory == null) {
                this.factory = GraphicsPipeline.getDefaultResourceFactory();
            }
            if (this.factory == null || !this.factory.isDeviceReady()) {
                this.sceneState.getScene().entireSceneNeedsRepaint();
                this.factory = null;
                return;
            }
            if (this.presentable != null && this.presentable.lockResources(this.sceneState)) {
                this.disposePresentable();
            }
            if (this.presentable == null) {
                this.presentable = this.factory.createPresentable(this.sceneState);
                this.penWidth = this.viewWidth;
                this.penHeight = this.viewHeight;
                this.freshBackBuffer = true;
            }
            if (this.presentable != null) {
                Graphics graphics = this.presentable.createGraphics();
                ViewScene viewScene = (ViewScene)this.sceneState.getScene();
                if (graphics != null) {
                    this.paintImpl(graphics);
                    this.freshBackBuffer = false;
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newPhase((String)"Presenting");
                }
                if (!this.presentable.prepare(null)) {
                    this.disposePresentable();
                    this.sceneState.getScene().entireSceneNeedsRepaint();
                    return;
                }
                if (viewScene.getDoPresent() && !this.presentable.present()) {
                    this.disposePresentable();
                    this.sceneState.getScene().entireSceneNeedsRepaint();
                }
            }
        }
        catch (Throwable throwable) {
            bl3 = true;
            throwable.printStackTrace(System.err);
        }
        finally {
            Disposer.cleanUp();
            if (bl) {
                this.sceneState.unlock();
            }
            ViewScene viewScene = (ViewScene)this.sceneState.getScene();
            viewScene.setPainting(false);
            if (this.factory != null) {
                this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(bl3);
            }
            renderLock.unlock();
        }
    }
}

