/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyCodeCombination
 *  javafx.scene.input.KeyCombination$ModifierValue
 */
package com.sun.javafx.tk;

import com.sun.javafx.tk.FocusCause;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKStageListener;
import java.security.AccessControlContext;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public interface TKStage {
    public static final KeyCodeCombination defaultFullScreenExitKeycombo = new KeyCodeCombination(KeyCode.ESCAPE, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP);

    public void setTKStageListener(TKStageListener var1);

    public TKScene createTKScene(boolean var1, boolean var2, AccessControlContext var3);

    public void setScene(TKScene var1);

    public void setBounds(float var1, float var2, boolean var3, boolean var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12);

    public float getPlatformScaleX();

    public float getPlatformScaleY();

    public float getOutputScaleX();

    public float getOutputScaleY();

    public void setIcons(List var1);

    public void setTitle(String var1);

    public void setVisible(boolean var1);

    public void setOpacity(float var1);

    public void setIconified(boolean var1);

    public void setMaximized(boolean var1);

    public void setAlwaysOnTop(boolean var1);

    public void setResizable(boolean var1);

    public void setImportant(boolean var1);

    public void setMinimumSize(int var1, int var2);

    public void setMaximumSize(int var1, int var2);

    public void setFullScreen(boolean var1);

    public void requestFocus();

    public void toBack();

    public void toFront();

    public void close();

    default public void postponeClose() {
    }

    default public void closePostponed() {
    }

    public void requestFocus(FocusCause var1);

    public boolean grabFocus();

    public void ungrabFocus();

    public void requestInput(String var1, int var2, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29);

    public void releaseInput();

    public void setRTL(boolean var1);

    public void setEnabled(boolean var1);

    public long getRawHandle();
}

