package com.sun.javafx.webkit.theme;

import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCSize;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;

public final class ScrollBarThemeImpl extends ScrollBarTheme {
   private static final Logger log = Logger.getLogger(ScrollBarThemeImpl.class.getName());
   private WeakReference testSBRef = new WeakReference((Object)null);
   private boolean thicknessInitialized = false;
   private final Accessor accessor;
   private final RenderThemeImpl.Pool pool;

   public ScrollBarThemeImpl(final Accessor var1) {
      this.accessor = var1;
      this.pool = new RenderThemeImpl.Pool((var1x) -> {
         var1.removeChild(var1x);
      }, ScrollBarWidget.class);
      var1.addViewListener(new RenderThemeImpl.ViewListener(this.pool, var1) {
         public void invalidated(Observable var1x) {
            super.invalidated(var1x);
            ScrollBarWidget var2 = ScrollBarThemeImpl.this.new ScrollBarWidget();
            var1.addChild(var2);
            ScrollBarThemeImpl.this.testSBRef = new WeakReference(var2);
         }
      });
   }

   private static Orientation convertOrientation(int var0) {
      return var0 == 1 ? Orientation.VERTICAL : Orientation.HORIZONTAL;
   }

   private void adjustScrollBar(ScrollBar var1, int var2, int var3, int var4) {
      Orientation var5 = convertOrientation(var4);
      if (var5 != var1.getOrientation()) {
         var1.setOrientation(var5);
      }

      if (var5 == Orientation.VERTICAL) {
         var2 = ScrollBarTheme.getThickness();
      } else {
         var3 = ScrollBarTheme.getThickness();
      }

      if ((double)var2 != var1.getWidth() || (double)var3 != var1.getHeight()) {
         var1.resize((double)var2, (double)var3);
      }

   }

   private void adjustScrollBar(ScrollBar var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.adjustScrollBar(var1, var2, var3, var4);
      boolean var8 = var7 <= var6;
      var1.setDisable(var8);
      if (!var8) {
         if (var5 < 0) {
            var5 = 0;
         } else if (var5 > var7 - var6) {
            var5 = var7 - var6;
         }

         if (var1.getMax() != (double)var7 || var1.getVisibleAmount() != (double)var6) {
            var1.setValue(0.0);
            var1.setMax((double)var7);
            var1.setVisibleAmount((double)var6);
         }

         if (var7 > var6) {
            float var9 = (float)var7 / (float)(var7 - var6);
            if (var1.getValue() != (double)((float)var5 * var9)) {
               var1.setValue((double)((float)var5 * var9));
            }
         }

      }
   }

   protected Ref createWidget(long var1, int var3, int var4, int var5, int var6, int var7, int var8) {
      ScrollBarWidget var9 = (ScrollBarWidget)this.pool.get(var1);
      if (var9 == null) {
         var9 = new ScrollBarWidget();
         this.pool.put(var1, var9, this.accessor.getPage().getUpdateContentCycleID());
         this.accessor.addChild(var9);
      }

      this.adjustScrollBar(var9, var3, var4, var5, var6, var7, var8);
      return new ScrollBarRef(var9);
   }

   public void paint(WCGraphicsContext var1, Ref var2, int var3, int var4, int var5, int var6) {
      ScrollBar var7 = (ScrollBar)((ScrollBarRef)var2).asControl();
      if (var7 != null) {
         if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, "[{0}, {1} {2}x{3}], {4}", new Object[]{var3, var4, var7.getWidth(), var7.getHeight(), var7.getOrientation() == Orientation.VERTICAL ? "VERTICAL" : "HORIZONTAL"});
         }

         var1.saveState();
         var1.translate((float)var3, (float)var4);
         Renderer.getRenderer().render(var7, var1);
         var1.restoreState();
      }
   }

   public WCSize getWidgetSize(Ref var1) {
      ScrollBar var2 = (ScrollBar)((ScrollBarRef)var1).asControl();
      return var2 != null ? new WCSize((float)var2.getWidth(), (float)var2.getHeight()) : new WCSize(0.0F, 0.0F);
   }

   protected void getScrollBarPartRect(long var1, int var3, int[] var4) {
      ScrollBar var5 = (ScrollBar)this.pool.get(var1);
      if (var5 != null) {
         Node var6 = null;
         if (var3 == 2) {
            var6 = getIncButton(var5);
         } else if (var3 == 1) {
            var6 = getDecButton(var5);
         } else if (var3 == 256) {
            var6 = getTrack(var5);
         }

         assert var4.length >= 4;

         if (var6 != null) {
            Bounds var7 = var6.getBoundsInParent();
            var4[0] = (int)var7.getMinX();
            var4[1] = (int)var7.getMinY();
            var4[2] = (int)var7.getWidth();
            var4[3] = (int)var7.getHeight();
         } else {
            var4[0] = var4[1] = var4[2] = var4[3] = 0;
         }

         log.log(Level.FINEST, "id {0} part {1} bounds {2},{3} {4}x{5}", new Object[]{String.valueOf(var1), String.valueOf(var3), var4[0], var4[1], var4[2], var4[3]});
      }
   }

   private void initializeThickness() {
      if (!this.thicknessInitialized) {
         ScrollBar var1 = (ScrollBar)this.testSBRef.get();
         if (var1 == null) {
            return;
         }

         int var2 = (int)var1.prefWidth(-1.0);
         if (var2 != 0 && ScrollBarTheme.getThickness() != var2) {
            ScrollBarTheme.setThickness(var2);
         }

         this.thicknessInitialized = true;
      }

   }

   private static Node getThumb(ScrollBar var0) {
      return findNode(var0, "thumb");
   }

   private static Node getTrack(ScrollBar var0) {
      return findNode(var0, "track");
   }

   private static Node getIncButton(ScrollBar var0) {
      return findNode(var0, "increment-button");
   }

   private static Node getDecButton(ScrollBar var0) {
      return findNode(var0, "decrement-button");
   }

   private static Node findNode(ScrollBar var0, String var1) {
      Iterator var2 = var0.getChildrenUnmodifiable().iterator();

      Node var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Node)var2.next();
      } while(!var3.getStyleClass().contains(var1));

      return var3;
   }

   private static final class ScrollBarRef extends Ref {
      private final WeakReference sbRef;

      private ScrollBarRef(ScrollBarWidget var1) {
         this.sbRef = new WeakReference(var1);
      }

      private Control asControl() {
         return (Control)this.sbRef.get();
      }

      // $FF: synthetic method
      ScrollBarRef(ScrollBarWidget var1, Object var2) {
         this(var1);
      }
   }

   private final class ScrollBarWidget extends ScrollBar implements RenderThemeImpl.Widget {
      private ScrollBarWidget() {
         this.setOrientation(Orientation.VERTICAL);
         this.setMin(0.0);
         this.setManaged(false);
      }

      public void impl_updatePeer() {
         super.impl_updatePeer();
         ScrollBarThemeImpl.this.initializeThickness();
      }

      public RenderThemeImpl.WidgetType getType() {
         return RenderThemeImpl.WidgetType.SCROLLBAR;
      }

      protected void layoutChildren() {
         super.layoutChildren();
         ScrollBarThemeImpl.this.initializeThickness();
      }

      // $FF: synthetic method
      ScrollBarWidget(Object var2) {
         this();
      }
   }
}
