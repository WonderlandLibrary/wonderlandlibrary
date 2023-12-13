package com.sun.javafx.tk;

public interface TKStageListener {
   void changedLocation(float var1, float var2);

   void changedSize(float var1, float var2);

   void changedScale(float var1, float var2);

   void changedFocused(boolean var1, FocusCause var2);

   void changedIconified(boolean var1);

   void changedMaximized(boolean var1);

   void changedAlwaysOnTop(boolean var1);

   void changedResizable(boolean var1);

   void changedFullscreen(boolean var1);

   void changedScreen(Object var1, Object var2);

   void closing();

   void closed();

   void focusUngrab();
}
