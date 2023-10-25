/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.embed.AbstractEvents;
import com.sun.javafx.embed.EmbeddedStageInterface;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.EmbeddedScene;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.GlassStage;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.List;
import javafx.application.Platform;

final class EmbeddedStage
extends GlassStage
implements EmbeddedStageInterface {
    private HostInterface host;

    public EmbeddedStage(HostInterface hostInterface) {
        this.host = hostInterface;
    }

    @Override
    public TKScene createTKScene(boolean bl, boolean bl2, AccessControlContext accessControlContext) {
        EmbeddedScene embeddedScene = new EmbeddedScene(this.host, bl, bl2);
        embeddedScene.setSecurityContext(accessControlContext);
        return embeddedScene;
    }

    @Override
    public void setScene(TKScene tKScene) {
        if (tKScene != null && !$assertionsDisabled && !(tKScene instanceof EmbeddedScene)) {
            throw new AssertionError();
        }
        super.setScene(tKScene);
    }

    @Override
    public void setBounds(float f, float f2, boolean bl, boolean bl2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        float f11;
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setBounds: x=" + f + " y=" + f2 + " xSet=" + bl + " ySet=" + bl2 + " w=" + f3 + " h= cw=" + f5 + " ch=" + f6);
        }
        float f12 = f3 > 0.0f ? f3 : f5;
        float f13 = f11 = f4 > 0.0f ? f4 : f6;
        if (f12 > 0.0f && f11 > 0.0f) {
            this.host.setPreferredSize((int)f12, (int)f11);
        }
        GlassScene glassScene = this.getScene();
        if ((f9 > 0.0f || f10 > 0.0f) && glassScene instanceof EmbeddedScene) {
            EmbeddedScene embeddedScene = (EmbeddedScene)glassScene;
            if ((double)f9 <= 0.0) {
                f9 = embeddedScene.getRenderScaleX();
            }
            if ((double)f10 <= 0.0) {
                f10 = embeddedScene.getRenderScaleY();
            }
            embeddedScene.setPixelScaleFactors(f9, f10);
        }
    }

    @Override
    public float getPlatformScaleX() {
        return 1.0f;
    }

    @Override
    public float getPlatformScaleY() {
        return 1.0f;
    }

    @Override
    public float getOutputScaleX() {
        GlassScene glassScene = this.getScene();
        if (glassScene instanceof EmbeddedScene) {
            return ((EmbeddedScene)glassScene).getRenderScaleX();
        }
        return 1.0f;
    }

    @Override
    public float getOutputScaleY() {
        GlassScene glassScene = this.getScene();
        if (glassScene instanceof EmbeddedScene) {
            return ((EmbeddedScene)glassScene).getRenderScaleY();
        }
        return 1.0f;
    }

    @Override
    public void setMinimumSize(int n, int n2) {
    }

    @Override
    public void setMaximumSize(int n, int n2) {
    }

    @Override
    protected void setPlatformEnabled(boolean bl) {
        super.setPlatformEnabled(bl);
        this.host.setEnabled(bl);
    }

    @Override
    public void setIcons(List list) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setIcons");
        }
    }

    @Override
    public void setTitle(String string) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setTitle " + string);
        }
    }

    @Override
    public void setVisible(boolean bl) {
        this.host.setEmbeddedStage(bl ? this : null);
        super.setVisible(bl);
    }

    @Override
    public void setOpacity(float f) {
    }

    @Override
    public void setIconified(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.setIconified " + bl);
        }
    }

    @Override
    public void setMaximized(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.setMaximized " + bl);
        }
    }

    @Override
    public void setAlwaysOnTop(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.setAlwaysOnTop " + bl);
        }
    }

    @Override
    public void setResizable(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setResizable " + bl);
        }
    }

    @Override
    public void setFullScreen(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setFullScreen " + bl);
        }
    }

    @Override
    public void requestFocus() {
        if (!this.host.requestFocus()) {
            return;
        }
        super.requestFocus();
    }

    @Override
    public void toBack() {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.toBack");
        }
    }

    @Override
    public void toFront() {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.toFront");
        }
    }

    @Override
    public boolean grabFocus() {
        return this.host.grabFocus();
    }

    @Override
    public void ungrabFocus() {
        this.host.ungrabFocus();
    }

    private void notifyStageListener(Runnable runnable) {
        AccessControlContext accessControlContext = this.getAccessControlContext();
        AccessController.doPrivileged(() -> {
            runnable.run();
            return null;
        }, accessControlContext);
    }

    private void notifyStageListenerLater(Runnable runnable) {
        Platform.runLater(() -> this.notifyStageListener(runnable));
    }

    @Override
    public void setLocation(int n, int n2) {
        Runnable runnable = () -> {
            if (this.stageListener != null) {
                this.stageListener.changedLocation(n, n2);
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            this.notifyStageListener(runnable);
        } else {
            this.notifyStageListenerLater(runnable);
        }
    }

    @Override
    public void setSize(int n, int n2) {
        Runnable runnable = () -> {
            if (this.stageListener != null) {
                this.stageListener.changedSize(n, n2);
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            this.notifyStageListener(runnable);
        } else {
            this.notifyStageListenerLater(runnable);
        }
    }

    @Override
    public void setFocused(boolean bl, int n) {
        Runnable runnable = () -> {
            if (this.stageListener != null) {
                this.stageListener.changedFocused(bl, AbstractEvents.focusCauseToPeerFocusCause(n));
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            this.notifyStageListener(runnable);
        } else {
            this.notifyStageListenerLater(runnable);
        }
    }

    @Override
    public void focusUngrab() {
        Runnable runnable = () -> {
            if (this.stageListener != null) {
                this.stageListener.focusUngrab();
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            this.notifyStageListener(runnable);
        } else {
            this.notifyStageListenerLater(runnable);
        }
    }

    @Override
    public void requestInput(String string, int n, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void releaseInput() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRTL(boolean bl) {
    }

    @Override
    public void setEnabled(boolean bl) {
    }

    @Override
    public long getRawHandle() {
        return 0L;
    }
}

