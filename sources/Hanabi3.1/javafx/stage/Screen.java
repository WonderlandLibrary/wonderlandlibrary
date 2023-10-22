package javafx.stage;

import com.sun.javafx.tk.ScreenConfigurationAccessor;
import com.sun.javafx.tk.Toolkit;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;

public final class Screen {
   private static final AtomicBoolean configurationDirty = new AtomicBoolean(true);
   private static final ScreenConfigurationAccessor accessor;
   private static Screen primary;
   private static final ObservableList screens = FXCollections.observableArrayList();
   private static final ObservableList unmodifiableScreens;
   private Rectangle2D bounds;
   private Rectangle2D visualBounds;
   private double dpi;
   private float outputScaleX;
   private float outputScaleY;

   private Screen() {
      this.bounds = Rectangle2D.EMPTY;
      this.visualBounds = Rectangle2D.EMPTY;
   }

   private static void checkDirty() {
      if (configurationDirty.compareAndSet(true, false)) {
         updateConfiguration();
      }

   }

   private static void updateConfiguration() {
      Object var0 = Toolkit.getToolkit().getPrimaryScreen();
      Screen var1 = nativeToScreen(var0, primary);
      if (var1 != null) {
         primary = var1;
      }

      List var2 = Toolkit.getToolkit().getScreens();
      ObservableList var3 = FXCollections.observableArrayList();
      boolean var4 = screens.size() == var2.size();

      for(int var5 = 0; var5 < var2.size(); ++var5) {
         Object var6 = var2.get(var5);
         Screen var7 = null;
         if (var4) {
            var7 = (Screen)screens.get(var5);
         }

         Screen var8 = nativeToScreen(var6, var7);
         if (var8 != null) {
            if (var4) {
               var4 = false;
               var3.setAll((Collection)screens.subList(0, var5));
            }

            var3.add(var8);
         }
      }

      if (!var4) {
         screens.setAll((Collection)var3);
      }

      configurationDirty.set(false);
   }

   private static Screen nativeToScreen(Object var0, Screen var1) {
      int var2 = accessor.getMinX(var0);
      int var3 = accessor.getMinY(var0);
      int var4 = accessor.getWidth(var0);
      int var5 = accessor.getHeight(var0);
      int var6 = accessor.getVisualMinX(var0);
      int var7 = accessor.getVisualMinY(var0);
      int var8 = accessor.getVisualWidth(var0);
      int var9 = accessor.getVisualHeight(var0);
      double var10 = (double)accessor.getDPI(var0);
      float var12 = accessor.getRecommendedOutputScaleX(var0);
      float var13 = accessor.getRecommendedOutputScaleY(var0);
      if (var1 != null && var1.bounds.getMinX() == (double)var2 && var1.bounds.getMinY() == (double)var3 && var1.bounds.getWidth() == (double)var4 && var1.bounds.getHeight() == (double)var5 && var1.visualBounds.getMinX() == (double)var6 && var1.visualBounds.getMinY() == (double)var7 && var1.visualBounds.getWidth() == (double)var8 && var1.visualBounds.getHeight() == (double)var9 && var1.dpi == var10 && var1.outputScaleX == var12 && var1.outputScaleY == var13) {
         return null;
      } else {
         Screen var14 = new Screen();
         var14.bounds = new Rectangle2D((double)var2, (double)var3, (double)var4, (double)var5);
         var14.visualBounds = new Rectangle2D((double)var6, (double)var7, (double)var8, (double)var9);
         var14.dpi = var10;
         var14.outputScaleX = var12;
         var14.outputScaleY = var13;
         return var14;
      }
   }

   static Screen getScreenForNative(Object var0) {
      double var1 = (double)accessor.getMinX(var0);
      double var3 = (double)accessor.getMinY(var0);
      double var5 = (double)accessor.getWidth(var0);
      double var7 = (double)accessor.getHeight(var0);
      Screen var9 = null;

      for(int var10 = 0; var10 < screens.size(); ++var10) {
         Screen var11 = (Screen)screens.get(var10);
         if (var11.bounds.contains(var1, var3, var5, var7)) {
            return var11;
         }

         if (var9 == null && var11.bounds.intersects(var1, var3, var5, var7)) {
            var9 = var11;
         }
      }

      return var9 == null ? getPrimary() : var9;
   }

   public static Screen getPrimary() {
      checkDirty();
      return primary;
   }

   public static ObservableList getScreens() {
      checkDirty();
      return unmodifiableScreens;
   }

   public static ObservableList getScreensForRectangle(double var0, double var2, double var4, double var6) {
      checkDirty();
      ObservableList var8 = FXCollections.observableArrayList();
      Iterator var9 = screens.iterator();

      while(var9.hasNext()) {
         Screen var10 = (Screen)var9.next();
         if (var10.bounds.intersects(var0, var2, var4, var6)) {
            var8.add(var10);
         }
      }

      return var8;
   }

   public static ObservableList getScreensForRectangle(Rectangle2D var0) {
      checkDirty();
      return getScreensForRectangle(var0.getMinX(), var0.getMinY(), var0.getWidth(), var0.getHeight());
   }

   public final Rectangle2D getBounds() {
      return this.bounds;
   }

   public final Rectangle2D getVisualBounds() {
      return this.visualBounds;
   }

   public final double getDpi() {
      return this.dpi;
   }

   private double getOutputScaleX() {
      return (double)this.outputScaleX;
   }

   private double getOutputScaleY() {
      return (double)this.outputScaleY;
   }

   public int hashCode() {
      long var1 = 7L;
      var1 = 37L * var1 + (long)this.bounds.hashCode();
      var1 = 37L * var1 + (long)this.visualBounds.hashCode();
      var1 = 37L * var1 + Double.doubleToLongBits(this.dpi);
      var1 = 37L * var1 + (long)Float.floatToIntBits(this.outputScaleX);
      var1 = 37L * var1 + (long)Float.floatToIntBits(this.outputScaleY);
      return (int)(var1 ^ var1 >> 32);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Screen)) {
         return false;
      } else {
         boolean var10000;
         label43: {
            Screen var2 = (Screen)var1;
            if (this.bounds == null) {
               if (var2.bounds != null) {
                  break label43;
               }
            } else if (!this.bounds.equals(var2.bounds)) {
               break label43;
            }

            if (this.visualBounds == null) {
               if (var2.visualBounds != null) {
                  break label43;
               }
            } else if (!this.visualBounds.equals(var2.visualBounds)) {
               break label43;
            }

            if (var2.dpi == this.dpi && var2.outputScaleX == this.outputScaleX && var2.outputScaleY == this.outputScaleY) {
               var10000 = true;
               return var10000;
            }
         }

         var10000 = false;
         return var10000;
      }
   }

   public String toString() {
      return super.toString() + " bounds:" + this.bounds + " visualBounds:" + this.visualBounds + " dpi:" + this.dpi + " outputScale:(" + this.outputScaleX + "," + this.outputScaleY + ")";
   }

   static {
      unmodifiableScreens = FXCollections.unmodifiableObservableList(screens);
      accessor = Toolkit.getToolkit().setScreenConfigurationListener(() -> {
         updateConfiguration();
      });
   }
}
