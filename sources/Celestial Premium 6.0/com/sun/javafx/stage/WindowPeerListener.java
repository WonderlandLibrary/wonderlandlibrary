/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventTarget
 *  javafx.stage.Window
 *  javafx.stage.WindowEvent
 */
package com.sun.javafx.stage;

import com.sun.javafx.stage.FocusUngrabEvent;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.tk.FocusCause;
import com.sun.javafx.tk.TKStageListener;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class WindowPeerListener
implements TKStageListener {
    private final Window window;

    public WindowPeerListener(Window window) {
        this.window = window;
    }

    @Override
    public void changedLocation(float f, float f2) {
        WindowHelper.notifyLocationChanged(this.window, f, f2);
    }

    @Override
    public void changedSize(float f, float f2) {
        WindowHelper.notifySizeChanged(this.window, f, f2);
    }

    @Override
    public void changedScale(float f, float f2) {
        WindowHelper.notifyScaleChanged(this.window, f, f2);
    }

    @Override
    public void changedFocused(boolean bl, FocusCause focusCause) {
        WindowHelper.setFocused(this.window, bl);
    }

    @Override
    public void changedIconified(boolean bl) {
    }

    @Override
    public void changedMaximized(boolean bl) {
    }

    @Override
    public void changedResizable(boolean bl) {
    }

    @Override
    public void changedFullscreen(boolean bl) {
    }

    @Override
    public void changedAlwaysOnTop(boolean bl) {
    }

    @Override
    public void changedScreen(Object object, Object object2) {
        WindowHelper.getWindowAccessor().notifyScreenChanged(this.window, object, object2);
    }

    @Override
    public void closing() {
        Event.fireEvent((EventTarget)this.window, (Event)new WindowEvent(this.window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @Override
    public void closed() {
        if (this.window.isShowing()) {
            this.window.hide();
        }
    }

    @Override
    public void focusUngrab() {
        Event.fireEvent((EventTarget)this.window, (Event)new FocusUngrabEvent());
    }
}

