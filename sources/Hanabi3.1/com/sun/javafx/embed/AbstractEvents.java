package com.sun.javafx.embed;

import com.sun.javafx.tk.FocusCause;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class AbstractEvents {
   public static final int MOUSEEVENT_PRESSED = 0;
   public static final int MOUSEEVENT_RELEASED = 1;
   public static final int MOUSEEVENT_CLICKED = 2;
   public static final int MOUSEEVENT_ENTERED = 3;
   public static final int MOUSEEVENT_EXITED = 4;
   public static final int MOUSEEVENT_MOVED = 5;
   public static final int MOUSEEVENT_DRAGGED = 6;
   public static final int MOUSEEVENT_WHEEL = 7;
   public static final int MOUSEEVENT_NONE_BUTTON = 0;
   public static final int MOUSEEVENT_PRIMARY_BUTTON = 1;
   public static final int MOUSEEVENT_SECONDARY_BUTTON = 2;
   public static final int MOUSEEVENT_MIDDLE_BUTTON = 4;
   public static final int KEYEVENT_PRESSED = 0;
   public static final int KEYEVENT_RELEASED = 1;
   public static final int KEYEVENT_TYPED = 2;
   public static final int FOCUSEVENT_ACTIVATED = 0;
   public static final int FOCUSEVENT_TRAVERSED_FORWARD = 1;
   public static final int FOCUSEVENT_TRAVERSED_BACKWARD = 2;
   public static final int FOCUSEVENT_DEACTIVATED = 3;
   public static final int MODIFIER_SHIFT = 1;
   public static final int MODIFIER_CONTROL = 2;
   public static final int MODIFIER_ALT = 4;
   public static final int MODIFIER_META = 8;

   public static EventType mouseIDToFXEventID(int var0) {
      switch (var0) {
         case 0:
            return MouseEvent.MOUSE_PRESSED;
         case 1:
            return MouseEvent.MOUSE_RELEASED;
         case 2:
            return MouseEvent.MOUSE_CLICKED;
         case 3:
            return MouseEvent.MOUSE_ENTERED;
         case 4:
            return MouseEvent.MOUSE_EXITED;
         case 5:
            return MouseEvent.MOUSE_MOVED;
         case 6:
            return MouseEvent.MOUSE_DRAGGED;
         default:
            return MouseEvent.MOUSE_MOVED;
      }
   }

   public static MouseButton mouseButtonToFXMouseButton(int var0) {
      switch (var0) {
         case 1:
            return MouseButton.PRIMARY;
         case 2:
            return MouseButton.SECONDARY;
         case 3:
         default:
            return MouseButton.NONE;
         case 4:
            return MouseButton.MIDDLE;
      }
   }

   public static EventType keyIDToFXEventType(int var0) {
      switch (var0) {
         case 0:
            return KeyEvent.KEY_PRESSED;
         case 1:
            return KeyEvent.KEY_RELEASED;
         case 2:
            return KeyEvent.KEY_TYPED;
         default:
            return KeyEvent.KEY_TYPED;
      }
   }

   public static FocusCause focusCauseToPeerFocusCause(int var0) {
      switch (var0) {
         case 0:
            return FocusCause.ACTIVATED;
         case 1:
            return FocusCause.TRAVERSED_FORWARD;
         case 2:
            return FocusCause.TRAVERSED_BACKWARD;
         case 3:
            return FocusCause.DEACTIVATED;
         default:
            return FocusCause.ACTIVATED;
      }
   }
}
