package javafx.embed.swing;

import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.ImageCursorFrame;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;

class SwingCursors {
   private static Cursor createCustomCursor(ImageCursorFrame var0) {
      Toolkit var1 = Toolkit.getDefaultToolkit();
      double var2 = var0.getWidth();
      double var4 = var0.getHeight();
      Dimension var6 = var1.getBestCursorSize((int)var2, (int)var4);
      double var7 = var0.getHotspotX() * var6.getWidth() / var2;
      double var9 = var0.getHotspotY() * var6.getHeight() / var4;
      Point var11 = new Point((int)var7, (int)var9);
      BufferedImage var12 = SwingFXUtils.fromFXImage(Image.impl_fromPlatformImage(var0.getPlatformImage()), (BufferedImage)null);
      return var1.createCustomCursor(var12, var11, (String)null);
   }

   static Cursor embedCursorToCursor(CursorFrame var0) {
      switch (var0.getCursorType()) {
         case DEFAULT:
            return Cursor.getPredefinedCursor(0);
         case CROSSHAIR:
            return Cursor.getPredefinedCursor(1);
         case TEXT:
            return Cursor.getPredefinedCursor(2);
         case WAIT:
            return Cursor.getPredefinedCursor(3);
         case SW_RESIZE:
            return Cursor.getPredefinedCursor(4);
         case SE_RESIZE:
            return Cursor.getPredefinedCursor(5);
         case NW_RESIZE:
            return Cursor.getPredefinedCursor(6);
         case NE_RESIZE:
            return Cursor.getPredefinedCursor(7);
         case N_RESIZE:
         case V_RESIZE:
            return Cursor.getPredefinedCursor(8);
         case S_RESIZE:
            return Cursor.getPredefinedCursor(9);
         case W_RESIZE:
         case H_RESIZE:
            return Cursor.getPredefinedCursor(10);
         case E_RESIZE:
            return Cursor.getPredefinedCursor(11);
         case OPEN_HAND:
         case CLOSED_HAND:
         case HAND:
            return Cursor.getPredefinedCursor(12);
         case MOVE:
            return Cursor.getPredefinedCursor(13);
         case DISAPPEAR:
            return Cursor.getPredefinedCursor(0);
         case NONE:
            return null;
         case IMAGE:
            return createCustomCursor((ImageCursorFrame)var0);
         default:
            return Cursor.getPredefinedCursor(0);
      }
   }

   static javafx.scene.Cursor embedCursorToCursor(Cursor var0) {
      if (var0 == null) {
         return javafx.scene.Cursor.DEFAULT;
      } else {
         switch (var0.getType()) {
            case 0:
               return javafx.scene.Cursor.DEFAULT;
            case 1:
               return javafx.scene.Cursor.CROSSHAIR;
            case 2:
               return javafx.scene.Cursor.TEXT;
            case 3:
               return javafx.scene.Cursor.WAIT;
            case 4:
               return javafx.scene.Cursor.SW_RESIZE;
            case 5:
               return javafx.scene.Cursor.SE_RESIZE;
            case 6:
               return javafx.scene.Cursor.NW_RESIZE;
            case 7:
               return javafx.scene.Cursor.NE_RESIZE;
            case 8:
               return javafx.scene.Cursor.N_RESIZE;
            case 9:
               return javafx.scene.Cursor.S_RESIZE;
            case 10:
               return javafx.scene.Cursor.W_RESIZE;
            case 11:
               return javafx.scene.Cursor.E_RESIZE;
            case 12:
               return javafx.scene.Cursor.HAND;
            case 13:
               return javafx.scene.Cursor.MOVE;
            default:
               return javafx.scene.Cursor.DEFAULT;
         }
      }
   }
}
