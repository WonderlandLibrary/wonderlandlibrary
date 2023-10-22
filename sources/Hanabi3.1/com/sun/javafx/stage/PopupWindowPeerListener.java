package com.sun.javafx.stage;

import com.sun.javafx.tk.FocusCause;
import javafx.stage.PopupWindow;

public class PopupWindowPeerListener extends WindowPeerListener {
   private final PopupWindow popupWindow;

   public PopupWindowPeerListener(PopupWindow var1) {
      super(var1);
      this.popupWindow = var1;
   }

   public void changedFocused(boolean var1, FocusCause var2) {
      this.popupWindow.setFocused(var1);
   }

   public void closing() {
   }

   public void changedLocation(float var1, float var2) {
   }

   public void changedIconified(boolean var1) {
   }

   public void changedMaximized(boolean var1) {
   }

   public void changedResizable(boolean var1) {
   }

   public void changedFullscreen(boolean var1) {
   }

   public void focusUngrab() {
   }
}
