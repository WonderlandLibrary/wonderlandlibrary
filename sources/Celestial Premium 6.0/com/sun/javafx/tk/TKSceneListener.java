/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.event.EventType
 *  javafx.scene.input.InputMethodEvent
 *  javafx.scene.input.InputMethodTextRun
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseButton
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.input.RotateEvent
 *  javafx.scene.input.ScrollEvent
 *  javafx.scene.input.SwipeEvent
 *  javafx.scene.input.TouchPoint$State
 *  javafx.scene.input.ZoomEvent
 */
package com.sun.javafx.tk;

import com.sun.glass.ui.Accessible;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.input.ZoomEvent;

public interface TKSceneListener {
    public void changedLocation(float var1, float var2);

    public void changedSize(float var1, float var2);

    public void mouseEvent(EventType<MouseEvent> var1, double var2, double var4, double var6, double var8, MouseButton var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19, boolean var20, boolean var21);

    public void keyEvent(KeyEvent var1);

    public void inputMethodEvent(EventType<InputMethodEvent> var1, ObservableList<InputMethodTextRun> var2, String var3, int var4);

    public void scrollEvent(EventType<ScrollEvent> var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14, int var15, int var16, int var17, int var18, double var19, double var21, double var23, double var25, boolean var27, boolean var28, boolean var29, boolean var30, boolean var31, boolean var32);

    public void menuEvent(double var1, double var3, double var5, double var7, boolean var9);

    public void zoomEvent(EventType<ZoomEvent> var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19);

    public void rotateEvent(EventType<RotateEvent> var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19);

    public void swipeEvent(EventType<SwipeEvent> var1, int var2, double var3, double var5, double var7, double var9, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15);

    public void touchEventBegin(long var1, int var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8);

    public void touchEventNext(TouchPoint.State var1, long var2, double var4, double var6, double var8, double var10);

    public void touchEventEnd();

    public Accessible getSceneAccessible();
}

