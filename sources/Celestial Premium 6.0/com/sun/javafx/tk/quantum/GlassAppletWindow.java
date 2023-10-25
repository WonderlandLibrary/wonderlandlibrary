/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.stage.Stage
 *  javafx.stage.Window
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.View;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.tk.AppletWindow;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.WindowStage;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javafx.stage.Stage;
import javafx.stage.Window;

class GlassAppletWindow
implements AppletWindow {
    private final com.sun.glass.ui.Window glassWindow;
    private WeakReference<Stage> topStage;
    private String serverName;

    GlassAppletWindow(long l, String string) {
        if (0L == l) {
            if (string != null) {
                throw new RuntimeException("GlassAppletWindow constructor used incorrectly.");
            }
            this.glassWindow = Application.GetApplication().createWindow(null, 0);
        } else {
            this.serverName = string;
            this.glassWindow = Application.GetApplication().createWindow(l);
        }
        this.glassWindow.setAppletMode(true);
    }

    com.sun.glass.ui.Window getGlassWindow() {
        return this.glassWindow;
    }

    @Override
    public void setBackgroundColor(int n) {
        Application.invokeLater(() -> {
            float f = (float)(n >> 16 & 0xFF) / 255.0f;
            float f2 = (float)(n >> 8 & 0xFF) / 255.0f;
            float f3 = (float)(n & 0xFF) / 255.0f;
            this.glassWindow.setBackground(f, f2, f3);
        });
    }

    @Override
    public void setForegroundColor(int n) {
    }

    @Override
    public void setVisible(boolean bl) {
        Application.invokeLater(() -> this.glassWindow.setVisible(bl));
    }

    @Override
    public void setSize(int n, int n2) {
        Application.invokeLater(() -> this.glassWindow.setSize(n, n2));
    }

    @Override
    public int getWidth() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Application.invokeAndWait(() -> atomicInteger.set(this.glassWindow.getWidth()));
        return atomicInteger.get();
    }

    @Override
    public int getHeight() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Application.invokeAndWait(() -> atomicInteger.set(this.glassWindow.getHeight()));
        return atomicInteger.get();
    }

    @Override
    public void setPosition(int n, int n2) {
        Application.invokeLater(() -> this.glassWindow.setPosition(n, n2));
    }

    @Override
    public int getPositionX() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Application.invokeAndWait(() -> atomicInteger.set(this.glassWindow.getX()));
        return atomicInteger.get();
    }

    @Override
    public int getPositionY() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Application.invokeAndWait(() -> atomicInteger.set(this.glassWindow.getY()));
        return atomicInteger.get();
    }

    @Override
    public float getPlatformScaleX() {
        AtomicReference<Float> atomicReference = new AtomicReference<Float>(Float.valueOf(0.0f));
        Application.invokeAndWait(() -> atomicReference.set(Float.valueOf(this.glassWindow.getPlatformScaleX())));
        return atomicReference.get().floatValue();
    }

    @Override
    public float getPlatformScaleY() {
        AtomicReference<Float> atomicReference = new AtomicReference<Float>(Float.valueOf(0.0f));
        Application.invokeAndWait(() -> atomicReference.set(Float.valueOf(this.glassWindow.getPlatformScaleY())));
        return atomicReference.get().floatValue();
    }

    void dispose() {
        QuantumToolkit.runWithRenderLock(() -> {
            this.glassWindow.close();
            return null;
        });
    }

    @Override
    public void setStageOnTop(Stage stage) {
        this.topStage = null != stage ? new WeakReference<Stage>(stage) : null;
    }

    @Override
    public int getRemoteLayerId() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        Application.invokeAndWait(() -> {
            View view = this.glassWindow.getView();
            if (view != null) {
                atomicInteger.set(view.getNativeRemoteLayerId(this.serverName));
            }
        });
        return atomicInteger.get();
    }

    @Override
    public void dispatchEvent(Map map) {
        Application.invokeAndWait(() -> this.glassWindow.dispatchNpapiEvent(map));
    }

    void assertStageOrder() {
        com.sun.glass.ui.Window window;
        TKStage tKStage;
        Stage stage;
        if (null != this.topStage && null != (stage = (Stage)this.topStage.get()) && (tKStage = WindowHelper.getPeer((Window)stage)) instanceof WindowStage && ((WindowStage)tKStage).isVisible() && null != (window = ((WindowStage)tKStage).getPlatformWindow())) {
            window.toFront();
        }
    }
}

