package com.sun.javafx.stage;

import com.sun.javafx.tk.FocusCause;
import com.sun.javafx.tk.TKStageListener;
import javafx.event.Event;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class WindowPeerListener implements TKStageListener {
   private final Window window;

   public WindowPeerListener(Window var1) {
      this.window = var1;
   }

   public void changedLocation(float var1, float var2) {
      WindowHelper.notifyLocationChanged(this.window, (double)var1, (double)var2);
   }

   public void changedSize(float var1, float var2) {
      WindowHelper.notifySizeChanged(this.window, (double)var1, (double)var2);
   }

   public void changedScale(float var1, float var2) {
      WindowHelper.notifyScaleChanged(this.window, (double)var1, (double)var2);
   }

   public void changedFocused(boolean var1, FocusCause var2) {
      this.window.setFocused(var1);
   }

   public void changedIconified(boolean var1) {
   }

   public void changedMaximized(boolean var1) {
   }

   public void changedResizable(boolean var1) {
   }

   public void changedFullscreen(boolean var1) {
   }

   public void changedAlwaysOnTop(boolean var1) {
   }

   public void changedScreen(Object var1, Object var2) {
      WindowHelper.getWindowAccessor().notifyScreenChanged(this.window, var1, var2);
   }

   public void closing() {
      Event.fireEvent(this.window, new WindowEvent(this.window, WindowEvent.WINDOW_CLOSE_REQUEST));
   }

   public void closed() {
      if (this.window.isShowing()) {
         this.window.hide();
      }

   }

   public void focusUngrab() {
      Event.fireEvent(this.window, new FocusUngrabEvent());
   }
}
