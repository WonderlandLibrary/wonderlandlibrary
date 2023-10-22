package javafx.scene;

import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.CursorType;
import com.sun.javafx.cursor.StandardCursorFrame;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import javafx.scene.image.Image;

public abstract class Cursor {
   public static final Cursor DEFAULT;
   public static final Cursor CROSSHAIR;
   public static final Cursor TEXT;
   public static final Cursor WAIT;
   public static final Cursor SW_RESIZE;
   public static final Cursor SE_RESIZE;
   public static final Cursor NW_RESIZE;
   public static final Cursor NE_RESIZE;
   public static final Cursor N_RESIZE;
   public static final Cursor S_RESIZE;
   public static final Cursor W_RESIZE;
   public static final Cursor E_RESIZE;
   public static final Cursor OPEN_HAND;
   public static final Cursor CLOSED_HAND;
   public static final Cursor HAND;
   public static final Cursor MOVE;
   public static final Cursor DISAPPEAR;
   public static final Cursor H_RESIZE;
   public static final Cursor V_RESIZE;
   public static final Cursor NONE;
   private String name = "CUSTOM";

   Cursor() {
   }

   Cursor(String var1) {
      this.name = var1;
   }

   abstract CursorFrame getCurrentFrame();

   void activate() {
   }

   void deactivate() {
   }

   public String toString() {
      return this.name;
   }

   public static Cursor cursor(String var0) {
      if (var0 == null) {
         throw new NullPointerException("The cursor identifier must not be null");
      } else if (isUrl(var0)) {
         return new ImageCursor(new Image(var0));
      } else {
         String var1 = var0.toUpperCase(Locale.ROOT);
         if (var1.equals(DEFAULT.name)) {
            return DEFAULT;
         } else if (var1.equals(CROSSHAIR.name)) {
            return CROSSHAIR;
         } else if (var1.equals(TEXT.name)) {
            return TEXT;
         } else if (var1.equals(WAIT.name)) {
            return WAIT;
         } else if (var1.equals(MOVE.name)) {
            return MOVE;
         } else if (var1.equals(SW_RESIZE.name)) {
            return SW_RESIZE;
         } else if (var1.equals(SE_RESIZE.name)) {
            return SE_RESIZE;
         } else if (var1.equals(NW_RESIZE.name)) {
            return NW_RESIZE;
         } else if (var1.equals(NE_RESIZE.name)) {
            return NE_RESIZE;
         } else if (var1.equals(N_RESIZE.name)) {
            return N_RESIZE;
         } else if (var1.equals(S_RESIZE.name)) {
            return S_RESIZE;
         } else if (var1.equals(W_RESIZE.name)) {
            return W_RESIZE;
         } else if (var1.equals(E_RESIZE.name)) {
            return E_RESIZE;
         } else if (var1.equals(OPEN_HAND.name)) {
            return OPEN_HAND;
         } else if (var1.equals(CLOSED_HAND.name)) {
            return CLOSED_HAND;
         } else if (var1.equals(HAND.name)) {
            return HAND;
         } else if (var1.equals(H_RESIZE.name)) {
            return H_RESIZE;
         } else if (var1.equals(V_RESIZE.name)) {
            return V_RESIZE;
         } else if (var1.equals(DISAPPEAR.name)) {
            return DISAPPEAR;
         } else if (var1.equals(NONE.name)) {
            return NONE;
         } else {
            throw new IllegalArgumentException("Invalid cursor specification");
         }
      }
   }

   private static boolean isUrl(String var0) {
      try {
         new URL(var0);
         return true;
      } catch (MalformedURLException var2) {
         return false;
      }
   }

   static {
      DEFAULT = new StandardCursor("DEFAULT", CursorType.DEFAULT);
      CROSSHAIR = new StandardCursor("CROSSHAIR", CursorType.CROSSHAIR);
      TEXT = new StandardCursor("TEXT", CursorType.TEXT);
      WAIT = new StandardCursor("WAIT", CursorType.WAIT);
      SW_RESIZE = new StandardCursor("SW_RESIZE", CursorType.SW_RESIZE);
      SE_RESIZE = new StandardCursor("SE_RESIZE", CursorType.SE_RESIZE);
      NW_RESIZE = new StandardCursor("NW_RESIZE", CursorType.NW_RESIZE);
      NE_RESIZE = new StandardCursor("NE_RESIZE", CursorType.NE_RESIZE);
      N_RESIZE = new StandardCursor("N_RESIZE", CursorType.N_RESIZE);
      S_RESIZE = new StandardCursor("S_RESIZE", CursorType.S_RESIZE);
      W_RESIZE = new StandardCursor("W_RESIZE", CursorType.W_RESIZE);
      E_RESIZE = new StandardCursor("E_RESIZE", CursorType.E_RESIZE);
      OPEN_HAND = new StandardCursor("OPEN_HAND", CursorType.OPEN_HAND);
      CLOSED_HAND = new StandardCursor("CLOSED_HAND", CursorType.CLOSED_HAND);
      HAND = new StandardCursor("HAND", CursorType.HAND);
      MOVE = new StandardCursor("MOVE", CursorType.MOVE);
      DISAPPEAR = new StandardCursor("DISAPPEAR", CursorType.DISAPPEAR);
      H_RESIZE = new StandardCursor("H_RESIZE", CursorType.H_RESIZE);
      V_RESIZE = new StandardCursor("V_RESIZE", CursorType.V_RESIZE);
      NONE = new StandardCursor("NONE", CursorType.NONE);
   }

   private static final class StandardCursor extends Cursor {
      private final CursorFrame singleFrame;

      public StandardCursor(String var1, CursorType var2) {
         super(var1);
         this.singleFrame = new StandardCursorFrame(var2);
      }

      CursorFrame getCurrentFrame() {
         return this.singleFrame;
      }
   }
}
