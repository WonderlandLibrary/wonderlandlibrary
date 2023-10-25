/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Window;
import com.sun.javafx.tk.FocusCause;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.WindowStage;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

class GlassWindowEventHandler
extends Window.EventHandler
implements PrivilegedAction<Void> {
    private final WindowStage stage;
    private Window window;
    private int type;

    public GlassWindowEventHandler(WindowStage windowStage) {
        this.stage = windowStage;
    }

    @Override
    public Void run() {
        if (this.stage == null || this.stage.stageListener == null) {
            return null;
        }
        switch (this.type) {
            case 531: {
                this.stage.stageListener.changedIconified(true);
                break;
            }
            case 532: {
                this.stage.stageListener.changedIconified(false);
                this.stage.stageListener.changedMaximized(true);
                break;
            }
            case 533: {
                this.stage.stageListener.changedIconified(false);
                this.stage.stageListener.changedMaximized(false);
                break;
            }
            case 512: {
                float f;
                float f2;
                float f3 = this.window.getX();
                float f4 = this.window.getY();
                Screen screen = this.window.getScreen();
                if (screen != null) {
                    float f5 = screen.getPlatformScaleX();
                    float f6 = screen.getPlatformScaleY();
                    float f7 = screen.getX();
                    float f8 = screen.getY();
                    float f9 = screen.getPlatformX();
                    float f10 = screen.getPlatformY();
                    f2 = f7 + (f3 - f9) / f5;
                    f = f8 + (f4 - f10) / f6;
                } else {
                    f2 = f3;
                    f = f4;
                }
                this.stage.stageListener.changedLocation(f2, f);
                if (Application.GetApplication().hasWindowManager()) break;
                QuantumToolkit.runWithRenderLock(() -> {
                    GlassScene glassScene = this.stage.getScene();
                    if (glassScene != null) {
                        glassScene.updateSceneState();
                    }
                    return null;
                });
                break;
            }
            case 511: {
                float f = this.window.getPlatformScaleX();
                float f11 = this.window.getPlatformScaleY();
                this.stage.stageListener.changedSize((float)this.window.getWidth() / f, (float)this.window.getHeight() / f11);
                break;
            }
            case 513: {
                float f = this.window.getOutputScaleX();
                float f12 = this.window.getOutputScaleY();
                this.stage.stageListener.changedScale(f, f12);
                QuantumToolkit.runWithRenderLock(() -> {
                    GlassScene glassScene = this.stage.getScene();
                    if (glassScene != null) {
                        glassScene.entireSceneNeedsRepaint();
                        glassScene.updateSceneState();
                    }
                    return null;
                });
                break;
            }
            case 542: {
                WindowStage.addActiveWindow(this.stage);
                this.stage.stageListener.changedFocused(true, FocusCause.ACTIVATED);
                break;
            }
            case 541: {
                this.stage.stageListener.changedFocused(false, FocusCause.DEACTIVATED);
                break;
            }
            case 546: {
                this.stage.stageListener.focusUngrab();
                break;
            }
            case 543: {
                WindowStage.addActiveWindow(this.stage);
                this.stage.stageListener.changedFocused(true, FocusCause.TRAVERSED_FORWARD);
                break;
            }
            case 544: {
                WindowStage.addActiveWindow(this.stage);
                this.stage.stageListener.changedFocused(true, FocusCause.TRAVERSED_BACKWARD);
                break;
            }
            case 545: {
                this.stage.handleFocusDisabled();
                break;
            }
            case 522: {
                this.stage.setPlatformWindowClosed();
                this.stage.stageListener.closed();
                break;
            }
            case 521: {
                this.stage.stageListener.closing();
                break;
            }
            default: {
                if (!QuantumToolkit.verbose) break;
                System.err.println("GlassWindowEventHandler: unknown type: " + this.type);
            }
        }
        return null;
    }

    @Override
    public void handleLevelEvent(int n) {
        QuantumToolkit.runWithoutRenderLock(() -> {
            AccessControlContext accessControlContext = this.stage.getAccessControlContext();
            return AccessController.doPrivileged(() -> {
                this.stage.stageListener.changedAlwaysOnTop(n != 1);
                return null;
            }, accessControlContext);
        });
    }

    @Override
    public void handleWindowEvent(Window window, long l, int n) {
        this.window = window;
        this.type = n;
        QuantumToolkit.runWithoutRenderLock(() -> {
            AccessControlContext accessControlContext = this.stage.getAccessControlContext();
            return AccessController.doPrivileged(this, accessControlContext);
        });
    }

    @Override
    public void handleScreenChangedEvent(Window window, long l, Screen screen, Screen screen2) {
        GlassScene glassScene = this.stage.getScene();
        if (glassScene != null) {
            QuantumToolkit.runWithRenderLock(() -> {
                glassScene.entireSceneNeedsRepaint();
                glassScene.updateSceneState();
                return null;
            });
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            AccessControlContext accessControlContext = this.stage.getAccessControlContext();
            return AccessController.doPrivileged(() -> {
                this.stage.stageListener.changedScreen(screen, screen2);
                return null;
            }, accessControlContext);
        });
    }
}

