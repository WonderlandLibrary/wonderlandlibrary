package com.sun.javafx.webkit;

import com.sun.webkit.CursorManager;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

public final class CursorManagerImpl extends CursorManager {
   private final Map map = new HashMap();
   private ResourceBundle bundle;

   protected Cursor getCustomCursor(WCImage var1, int var2, int var3) {
      return new ImageCursor(Image.impl_fromPlatformImage(WCGraphicsManager.getGraphicsManager().toPlatformImage(var1)), (double)var2, (double)var3);
   }

   protected Cursor getPredefinedCursor(int var1) {
      switch (var1) {
         case 0:
         default:
            return Cursor.DEFAULT;
         case 1:
            return Cursor.CROSSHAIR;
         case 2:
            return Cursor.HAND;
         case 3:
            return Cursor.MOVE;
         case 4:
            return Cursor.TEXT;
         case 5:
            return Cursor.WAIT;
         case 6:
            return this.getCustomCursor("help", Cursor.DEFAULT);
         case 7:
            return Cursor.E_RESIZE;
         case 8:
            return Cursor.N_RESIZE;
         case 9:
            return Cursor.NE_RESIZE;
         case 10:
            return Cursor.NW_RESIZE;
         case 11:
            return Cursor.S_RESIZE;
         case 12:
            return Cursor.SE_RESIZE;
         case 13:
            return Cursor.SW_RESIZE;
         case 14:
            return Cursor.W_RESIZE;
         case 15:
            return Cursor.V_RESIZE;
         case 16:
            return Cursor.H_RESIZE;
         case 17:
            return this.getCustomCursor("resize.nesw", Cursor.DEFAULT);
         case 18:
            return this.getCustomCursor("resize.nwse", Cursor.DEFAULT);
         case 19:
            return this.getCustomCursor("resize.column", Cursor.H_RESIZE);
         case 20:
            return this.getCustomCursor("resize.row", Cursor.V_RESIZE);
         case 21:
            return this.getCustomCursor("panning.middle", Cursor.DEFAULT);
         case 22:
            return this.getCustomCursor("panning.east", Cursor.DEFAULT);
         case 23:
            return this.getCustomCursor("panning.north", Cursor.DEFAULT);
         case 24:
            return this.getCustomCursor("panning.ne", Cursor.DEFAULT);
         case 25:
            return this.getCustomCursor("panning.nw", Cursor.DEFAULT);
         case 26:
            return this.getCustomCursor("panning.south", Cursor.DEFAULT);
         case 27:
            return this.getCustomCursor("panning.se", Cursor.DEFAULT);
         case 28:
            return this.getCustomCursor("panning.sw", Cursor.DEFAULT);
         case 29:
            return this.getCustomCursor("panning.west", Cursor.DEFAULT);
         case 30:
            return this.getCustomCursor("vertical.text", Cursor.DEFAULT);
         case 31:
            return this.getCustomCursor("cell", Cursor.DEFAULT);
         case 32:
            return this.getCustomCursor("context.menu", Cursor.DEFAULT);
         case 33:
            return this.getCustomCursor("no.drop", Cursor.DEFAULT);
         case 34:
            return this.getCustomCursor("not.allowed", Cursor.DEFAULT);
         case 35:
            return this.getCustomCursor("progress", Cursor.WAIT);
         case 36:
            return this.getCustomCursor("alias", Cursor.DEFAULT);
         case 37:
            return this.getCustomCursor("zoom.in", Cursor.DEFAULT);
         case 38:
            return this.getCustomCursor("zoom.out", Cursor.DEFAULT);
         case 39:
            return this.getCustomCursor("copy", Cursor.DEFAULT);
         case 40:
            return Cursor.NONE;
         case 41:
            return this.getCustomCursor("grab", Cursor.OPEN_HAND);
         case 42:
            return this.getCustomCursor("grabbing", Cursor.CLOSED_HAND);
      }
   }

   private Cursor getCustomCursor(String var1, Cursor var2) {
      Object var3 = (Cursor)this.map.get(var1);
      if (var3 == null) {
         try {
            if (this.bundle == null) {
               this.bundle = ResourceBundle.getBundle("com.sun.javafx.webkit.Cursors", Locale.getDefault());
            }

            if (this.bundle != null) {
               String var4 = this.bundle.getString(var1 + ".file");
               Image var5 = new Image(CursorManagerImpl.class.getResourceAsStream(var4));
               var4 = this.bundle.getString(var1 + ".hotspotX");
               int var6 = Integer.parseInt(var4);
               var4 = this.bundle.getString(var1 + ".hotspotY");
               int var7 = Integer.parseInt(var4);
               var3 = new ImageCursor(var5, (double)var6, (double)var7);
            }
         } catch (MissingResourceException var8) {
         }

         if (var3 == null) {
            var3 = var2;
         }

         this.map.put(var1, var3);
      }

      return (Cursor)var3;
   }
}
