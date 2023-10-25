/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.FocusCause;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.TKStageListener;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.GlassScene;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

abstract class GlassStage
implements TKStage {
    private static final List<GlassStage> windows = new ArrayList<GlassStage>();
    private static List<TKStage> importantWindows = new ArrayList<TKStage>();
    private GlassScene scene;
    protected TKStageListener stageListener;
    private boolean visible;
    private boolean important = true;
    private AccessControlContext accessCtrlCtx = null;
    protected static final AtomicReference<GlassStage> activeFSWindow = new AtomicReference();

    protected GlassStage() {
        windows.add(this);
    }

    @Override
    public void close() {
        assert (this.scene == null);
        windows.remove(this);
        importantWindows.remove(this);
        GlassStage.notifyWindowListeners();
    }

    @Override
    public void setTKStageListener(TKStageListener tKStageListener) {
        this.stageListener = tKStageListener;
    }

    protected final GlassScene getScene() {
        return this.scene;
    }

    @Override
    public void setScene(TKScene tKScene) {
        if (this.scene != null) {
            this.scene.setStage(null);
        }
        this.scene = (GlassScene)tKScene;
        if (this.scene != null) {
            this.scene.setStage(this);
        }
    }

    final AccessControlContext getAccessControlContext() {
        if (this.accessCtrlCtx == null) {
            throw new RuntimeException("Stage security context has not been set!");
        }
        return this.accessCtrlCtx;
    }

    static AccessControlContext doIntersectionPrivilege(PrivilegedAction<AccessControlContext> privilegedAction, AccessControlContext accessControlContext, AccessControlContext accessControlContext2) {
        return AccessController.doPrivileged(() -> AccessController.doPrivilegedWithCombiner(() -> AccessController.getContext(), accessControlContext, new Permission[0]), accessControlContext2);
    }

    public final void setSecurityContext(AccessControlContext accessControlContext) {
        if (this.accessCtrlCtx != null) {
            throw new RuntimeException("Stage security context has been already set!");
        }
        AccessControlContext accessControlContext2 = AccessController.getContext();
        this.accessCtrlCtx = GlassStage.doIntersectionPrivilege(() -> AccessController.getContext(), accessControlContext2, accessControlContext);
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public void requestFocus(FocusCause focusCause) {
    }

    @Override
    public void setVisible(boolean bl) {
        this.visible = bl;
        if (bl) {
            if (this.important) {
                importantWindows.add(this);
                GlassStage.notifyWindowListeners();
            }
        } else if (this.important) {
            importantWindows.remove(this);
            GlassStage.notifyWindowListeners();
        }
        if (this.scene != null) {
            this.scene.stageVisible(bl);
        }
    }

    boolean isVisible() {
        return this.visible;
    }

    protected void setPlatformEnabled(boolean bl) {
    }

    void windowsSetEnabled(boolean bl) {
        for (GlassStage glassStage : windows.toArray(new GlassStage[windows.size()])) {
            if (glassStage == this || !windows.contains(glassStage)) continue;
            glassStage.setPlatformEnabled(bl);
        }
    }

    @Override
    public void setImportant(boolean bl) {
        this.important = bl;
    }

    private static void notifyWindowListeners() {
        Toolkit.getToolkit().notifyWindowListeners(importantWindows);
    }

    static void requestClosingAllWindows() {
        GlassStage glassStage = activeFSWindow.get();
        if (glassStage != null) {
            glassStage.setFullScreen(false);
        }
        for (GlassStage glassStage2 : windows.toArray(new GlassStage[windows.size()])) {
            if (!windows.contains(glassStage2) || !glassStage2.isVisible() || glassStage2.stageListener == null) continue;
            AccessController.doPrivileged(() -> {
                glassStage.stageListener.closing();
                return null;
            }, glassStage2.getAccessControlContext());
        }
    }
}

