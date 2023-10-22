package com.sun.javafx.tk;

import com.sun.glass.ui.Accessible;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TouchPoint;

public interface TKSceneListener {
   void changedLocation(float var1, float var2);

   void changedSize(float var1, float var2);

   void mouseEvent(EventType var1, double var2, double var4, double var6, double var8, MouseButton var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19);

   void keyEvent(KeyEvent var1);

   void inputMethodEvent(EventType var1, ObservableList var2, String var3, int var4);

   void scrollEvent(EventType var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14, int var15, int var16, int var17, int var18, double var19, double var21, double var23, double var25, boolean var27, boolean var28, boolean var29, boolean var30, boolean var31, boolean var32);

   void menuEvent(double var1, double var3, double var5, double var7, boolean var9);

   void zoomEvent(EventType var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19);

   void rotateEvent(EventType var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19);

   void swipeEvent(EventType var1, int var2, double var3, double var5, double var7, double var9, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15);

   void touchEventBegin(long var1, int var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8);

   void touchEventNext(TouchPoint.State var1, long var2, double var4, double var6, double var8, double var10);

   void touchEventEnd();

   Accessible getSceneAccessible();
}
