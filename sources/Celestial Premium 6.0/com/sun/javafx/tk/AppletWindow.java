/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.stage.Stage
 */
package com.sun.javafx.tk;

import java.util.Map;
import javafx.stage.Stage;

public interface AppletWindow {
    public void setStageOnTop(Stage var1);

    public void setBackgroundColor(int var1);

    public void setForegroundColor(int var1);

    public void setVisible(boolean var1);

    public void setSize(int var1, int var2);

    public int getWidth();

    public int getHeight();

    public void setPosition(int var1, int var2);

    public int getPositionX();

    public int getPositionY();

    public float getPlatformScaleX();

    public float getPlatformScaleY();

    public int getRemoteLayerId();

    public void dispatchEvent(Map var1);
}

