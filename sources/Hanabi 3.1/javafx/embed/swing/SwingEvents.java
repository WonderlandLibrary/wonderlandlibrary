package javafx.embed.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

class SwingEvents {
   static int mouseIDToEmbedMouseType(int var0) {
      switch (var0) {
         case 500:
            return 2;
         case 501:
            return 0;
         case 502:
            return 1;
         case 503:
            return 5;
         case 504:
            return 3;
         case 505:
            return 4;
         case 506:
            return 6;
         case 507:
            return 7;
         default:
            return 0;
      }
   }

   static int mouseButtonToEmbedMouseButton(int var0, int var1) {
      byte var2 = 0;
      switch (var0) {
         case 1:
            var2 = 1;
            break;
         case 2:
            var2 = 4;
            break;
         case 3:
            var2 = 2;
      }

      if ((var1 & 1024) != 0) {
         var2 = 1;
      } else if ((var1 & 2048) != 0) {
         var2 = 4;
      } else if ((var1 & 4096) != 0) {
         var2 = 2;
      }

      return var2;
   }

   static int getWheelRotation(MouseEvent var0) {
      return var0 instanceof MouseWheelEvent ? ((MouseWheelEvent)var0).getWheelRotation() : 0;
   }

   static int keyIDToEmbedKeyType(int var0) {
      switch (var0) {
         case 400:
            return 2;
         case 401:
            return 0;
         case 402:
            return 1;
         default:
            return 0;
      }
   }

   static int keyModifiersToEmbedKeyModifiers(int var0) {
      int var1 = 0;
      if ((var0 & 64) != 0) {
         var1 |= 1;
      }

      if ((var0 & 128) != 0) {
         var1 |= 2;
      }

      if ((var0 & 512) != 0) {
         var1 |= 4;
      }

      if ((var0 & 256) != 0) {
         var1 |= 8;
      }

      return var1;
   }

   static char keyCharToEmbedKeyChar(char var0) {
      return var0 == '\n' ? '\r' : var0;
   }

   static int fxMouseEventTypeToMouseID(javafx.scene.input.MouseEvent var0) {
      EventType var1 = var0.getEventType();
      if (var1 == javafx.scene.input.MouseEvent.MOUSE_MOVED) {
         return 503;
      } else if (var1 == javafx.scene.input.MouseEvent.MOUSE_PRESSED) {
         return 501;
      } else if (var1 == javafx.scene.input.MouseEvent.MOUSE_RELEASED) {
         return 502;
      } else if (var1 == javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
         return 500;
      } else if (var1 == javafx.scene.input.MouseEvent.MOUSE_ENTERED) {
         return 504;
      } else if (var1 == javafx.scene.input.MouseEvent.MOUSE_EXITED) {
         return 505;
      } else if (var1 == javafx.scene.input.MouseEvent.MOUSE_DRAGGED) {
         return 506;
      } else if (var1 == javafx.scene.input.MouseEvent.DRAG_DETECTED) {
         return -1;
      } else {
         throw new RuntimeException("Unknown MouseEvent type: " + var1);
      }
   }

   static int fxMouseModsToMouseMods(javafx.scene.input.MouseEvent var0) {
      int var1 = 0;
      if (var0.isAltDown()) {
         var1 |= 512;
      }

      if (var0.isControlDown()) {
         var1 |= 128;
      }

      if (var0.isMetaDown()) {
         var1 |= 256;
      }

      if (var0.isShiftDown()) {
         var1 |= 64;
      }

      if (var0.isPrimaryButtonDown()) {
         var1 |= 1024;
      }

      if (var0.isSecondaryButtonDown()) {
         var1 |= 4096;
      }

      if (var0.isMiddleButtonDown()) {
         var1 |= 2048;
      }

      return var1;
   }

   static int fxMouseButtonToMouseButton(javafx.scene.input.MouseEvent var0) {
      switch (var0.getButton()) {
         case PRIMARY:
            return 1;
         case SECONDARY:
            return 3;
         case MIDDLE:
            return 2;
         default:
            return 0;
      }
   }

   static int fxKeyEventTypeToKeyID(KeyEvent var0) {
      EventType var1 = var0.getEventType();
      if (var1 == KeyEvent.KEY_PRESSED) {
         return 401;
      } else if (var1 == KeyEvent.KEY_RELEASED) {
         return 402;
      } else if (var1 == KeyEvent.KEY_TYPED) {
         return 400;
      } else {
         throw new RuntimeException("Unknown KeyEvent type: " + var1);
      }
   }

   static int fxKeyModsToKeyMods(KeyEvent var0) {
      int var1 = 0;
      if (var0.isAltDown()) {
         var1 |= 512;
      }

      if (var0.isControlDown()) {
         var1 |= 128;
      }

      if (var0.isMetaDown()) {
         var1 |= 256;
      }

      if (var0.isShiftDown()) {
         var1 |= 64;
      }

      return var1;
   }

   static int fxScrollModsToMouseWheelMods(ScrollEvent var0) {
      int var1 = 0;
      if (var0.isAltDown()) {
         var1 |= 512;
      }

      if (var0.isControlDown()) {
         var1 |= 128;
      }

      if (var0.isMetaDown()) {
         var1 |= 256;
      }

      if (var0.isShiftDown()) {
         var1 |= 64;
      }

      return var1;
   }
}
