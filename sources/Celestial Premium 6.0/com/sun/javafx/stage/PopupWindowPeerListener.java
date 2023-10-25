/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.stage.PopupWindow
 *  javafx.stage.Window
 */
package com.sun.javafx.stage;

import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.stage.WindowPeerListener;
import com.sun.javafx.tk.FocusCause;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

public class PopupWindowPeerListener
extends WindowPeerListener {
    private final PopupWindow popupWindow;

    public PopupWindowPeerListener(PopupWindow popupWindow) {
        super((Window)popupWindow);
        this.popupWindow = popupWindow;
    }

    @Override
    public void changedFocused(boolean bl, FocusCause focusCause) {
        WindowHelper.setFocused((Window)this.popupWindow, bl);
    }

    @Override
    public void closing() {
    }

    @Override
    public void changedLocation(float f, float f2) {
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
    public void focusUngrab() {
    }
}

