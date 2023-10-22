package com.sun.javafx.tk;

import java.security.AccessControlContext;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public interface TKStage {
   KeyCodeCombination defaultFullScreenExitKeycombo = new KeyCodeCombination(KeyCode.ESCAPE, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP);

   void setTKStageListener(TKStageListener var1);

   TKScene createTKScene(boolean var1, boolean var2, AccessControlContext var3);

   void setScene(TKScene var1);

   void setBounds(float var1, float var2, boolean var3, boolean var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12);

   float getPlatformScaleX();

   float getPlatformScaleY();

   float getOutputScaleX();

   float getOutputScaleY();

   void setIcons(List var1);

   void setTitle(String var1);

   void setVisible(boolean var1);

   void setOpacity(float var1);

   void setIconified(boolean var1);

   void setMaximized(boolean var1);

   void setAlwaysOnTop(boolean var1);

   void setResizable(boolean var1);

   void setImportant(boolean var1);

   void setMinimumSize(int var1, int var2);

   void setMaximumSize(int var1, int var2);

   void setFullScreen(boolean var1);

   void requestFocus();

   void toBack();

   void toFront();

   void close();

   default void postponeClose() {
   }

   default void closePostponed() {
   }

   void requestFocus(FocusCause var1);

   boolean grabFocus();

   void ungrabFocus();

   void requestInput(String var1, int var2, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29);

   void releaseInput();

   void setRTL(boolean var1);

   void setEnabled(boolean var1);

   long getRawHandle();
}
