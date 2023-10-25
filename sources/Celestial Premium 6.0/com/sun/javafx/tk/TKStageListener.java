/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk;

import com.sun.javafx.tk.FocusCause;

public interface TKStageListener {
    public void changedLocation(float var1, float var2);

    public void changedSize(float var1, float var2);

    public void changedScale(float var1, float var2);

    public void changedFocused(boolean var1, FocusCause var2);

    public void changedIconified(boolean var1);

    public void changedMaximized(boolean var1);

    public void changedAlwaysOnTop(boolean var1);

    public void changedResizable(boolean var1);

    public void changedFullscreen(boolean var1);

    public void changedScreen(Object var1, Object var2);

    public void closing();

    public void closed();

    public void focusUngrab();
}

