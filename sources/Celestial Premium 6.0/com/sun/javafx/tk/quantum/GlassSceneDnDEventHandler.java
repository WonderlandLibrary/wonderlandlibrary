/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.scene.input.TransferMode
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.QuantumClipboard;
import java.security.AccessController;
import javafx.application.Platform;
import javafx.scene.input.TransferMode;

class GlassSceneDnDEventHandler {
    private final GlassScene scene;

    public GlassSceneDnDEventHandler(GlassScene glassScene) {
        this.scene = glassScene;
    }

    private double getPlatformScaleX() {
        Window window;
        View view = this.scene.getPlatformView();
        if (view != null && (window = view.getWindow()) != null) {
            return window.getPlatformScaleX();
        }
        return 1.0;
    }

    private double getPlatformScaleY() {
        Window window;
        View view = this.scene.getPlatformView();
        if (view != null && (window = view.getWindow()) != null) {
            return window.getPlatformScaleY();
        }
        return 1.0;
    }

    public TransferMode handleDragEnter(int n, int n2, int n3, int n4, TransferMode transferMode, ClipboardAssistance clipboardAssistance) {
        assert (Platform.isFxApplicationThread());
        return AccessController.doPrivileged(() -> {
            if (this.scene.dropTargetListener != null) {
                double d = this.getPlatformScaleX();
                double d2 = this.getPlatformScaleY();
                QuantumClipboard quantumClipboard = QuantumClipboard.getDragboardInstance(clipboardAssistance, false);
                return this.scene.dropTargetListener.dragEnter((double)n / d, (double)n2 / d2, (double)n3 / d, (double)n4 / d2, transferMode, quantumClipboard);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    public void handleDragLeave(ClipboardAssistance clipboardAssistance) {
        assert (Platform.isFxApplicationThread());
        AccessController.doPrivileged(() -> {
            if (this.scene.dropTargetListener != null) {
                this.scene.dropTargetListener.dragExit(0.0, 0.0, 0.0, 0.0);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    public TransferMode handleDragDrop(int n, int n2, int n3, int n4, TransferMode transferMode, ClipboardAssistance clipboardAssistance) {
        assert (Platform.isFxApplicationThread());
        return AccessController.doPrivileged(() -> {
            if (this.scene.dropTargetListener != null) {
                double d = this.getPlatformScaleX();
                double d2 = this.getPlatformScaleY();
                return this.scene.dropTargetListener.drop((double)n / d, (double)n2 / d2, (double)n3 / d, (double)n4 / d2, transferMode);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    public TransferMode handleDragOver(int n, int n2, int n3, int n4, TransferMode transferMode, ClipboardAssistance clipboardAssistance) {
        assert (Platform.isFxApplicationThread());
        return AccessController.doPrivileged(() -> {
            if (this.scene.dropTargetListener != null) {
                double d = this.getPlatformScaleX();
                double d2 = this.getPlatformScaleY();
                return this.scene.dropTargetListener.dragOver((double)n / d, (double)n2 / d2, (double)n3 / d, (double)n4 / d2, transferMode);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    public void handleDragStart(int n, int n2, int n3, int n4, int n5, ClipboardAssistance clipboardAssistance) {
        assert (Platform.isFxApplicationThread());
        AccessController.doPrivileged(() -> {
            if (this.scene.dragGestureListener != null) {
                double d = this.getPlatformScaleX();
                double d2 = this.getPlatformScaleY();
                QuantumClipboard quantumClipboard = QuantumClipboard.getDragboardInstance(clipboardAssistance, true);
                this.scene.dragGestureListener.dragGestureRecognized((double)n2 / d, (double)n3 / d2, (double)n4 / d, (double)n5 / d2, n, quantumClipboard);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    public void handleDragEnd(TransferMode transferMode, ClipboardAssistance clipboardAssistance) {
        assert (Platform.isFxApplicationThread());
        AccessController.doPrivileged(() -> {
            try {
                if (this.scene.dragSourceListener != null) {
                    this.scene.dragSourceListener.dragDropEnd(0.0, 0.0, 0.0, 0.0, transferMode);
                }
            }
            finally {
                QuantumClipboard.releaseCurrentDragboard();
            }
            return null;
        }, this.scene.getAccessControlContext());
    }
}

