package com.sun.javafx.webkit.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.sg.prism.NGImageView;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPath;
import com.sun.javafx.sg.prism.NGRectangle;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.sg.prism.NGText;
import com.sun.javafx.text.TextRun;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.Paint;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.DropShadow;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCIcon;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCSize;
import com.sun.webkit.graphics.WCTransform;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class WCGraphicsPrismContext extends WCGraphicsContext {
   private static final Logger log = Logger.getLogger(WCGraphicsPrismContext.class.getName());
   private static final boolean DEBUG_DRAW_CLIP_SHAPE = Boolean.valueOf((String)AccessController.doPrivileged(() -> {
      return System.getProperty("com.sun.webkit.debugDrawClipShape", "false");
   }));
   Graphics baseGraphics;
   private BaseTransform baseTransform;
   private final List states = new ArrayList();
   private ContextState state = new ContextState();
   private Graphics cachedGraphics = null;
   private int fontSmoothingType;
   private boolean isRootLayerValid = false;
   private static final BasicStroke focusRingStroke = new BasicStroke(1.1F, 0, 1, 0.0F, new float[]{1.0F}, 0.0F);

   WCGraphicsPrismContext(Graphics var1) {
      this.state.setClip(var1.getClipRect());
      this.state.setAlpha(var1.getExtraAlpha());
      this.baseGraphics = var1;
      this.initBaseTransform(var1.getTransformNoClone());
   }

   WCGraphicsPrismContext() {
   }

   public Type type() {
      return WCGraphicsPrismContext.Type.PRIMARY;
   }

   final void initBaseTransform(BaseTransform var1) {
      this.baseTransform = new Affine3D(var1);
      this.state.setTransform((Affine3D)this.baseTransform);
   }

   private void resetCachedGraphics() {
      this.cachedGraphics = null;
   }

   public Object getPlatformGraphics() {
      return this.getGraphics(false);
   }

   Graphics getGraphics(boolean var1) {
      if (this.cachedGraphics == null) {
         Layer var2 = this.state.getLayerNoClone();
         this.cachedGraphics = var2 != null ? var2.getGraphics() : this.baseGraphics;
         this.state.apply(this.cachedGraphics);
         if (log.isLoggable(Level.FINE)) {
            log.fine("getPlatformGraphics for " + this + " : " + this.cachedGraphics);
         }
      }

      Rectangle var3 = this.cachedGraphics.getClipRectNoClone();
      return var1 && var3 != null && var3.isEmpty() ? null : this.cachedGraphics;
   }

   public void saveState() {
      this.state.markAsRestorePoint();
      this.saveStateInternal();
   }

   private void saveStateInternal() {
      this.states.add(this.state);
      this.state = this.state.clone();
   }

   private void startNewLayer(Layer var1) {
      this.saveStateInternal();
      Rectangle var2 = this.state.getClipNoClone();
      Affine3D var3 = new Affine3D(BaseTransform.getTranslateInstance((double)(-var2.x), (double)(-var2.y)));
      var3.concatenate(this.state.getTransformNoClone());
      var2.x = 0;
      var2.y = 0;
      Graphics var4 = this.getGraphics(true);
      if (var4 != null && var4 != this.baseGraphics) {
         var1.init(var4);
      }

      this.state.setTransform(var3);
      this.state.setLayer(var1);
      this.resetCachedGraphics();
   }

   private void renderLayer(Layer var1) {
      WCTransform var2 = this.getTransform();
      this.setTransform(new WCTransform(1.0, 0.0, 0.0, 1.0, var1.getX(), var1.getY()));
      Graphics var3 = this.getGraphics(true);
      if (var3 != null) {
         var1.render(var3);
      }

      this.setTransform(var2);
   }

   private void restoreStateInternal() {
      int var1 = this.states.size();
      if (var1 == 0) {
         assert false : "Unbalanced restoreState";

      } else {
         Layer var2 = this.state.getLayerNoClone();
         this.state = (ContextState)this.states.remove(var1 - 1);
         if (var2 != this.state.getLayerNoClone()) {
            this.renderLayer(var2);
            var2.dispose();
            if (log.isLoggable(Level.FINE)) {
               log.fine("Popped layer " + var2);
            }
         } else {
            this.resetCachedGraphics();
         }

      }
   }

   public void restoreState() {
      log.fine("restoring state");

      do {
         this.restoreStateInternal();
      } while(!this.state.isRestorePoint());

   }

   private void flushAllLayers() {
      if (this.state != null) {
         if (this.isRootLayerValid) {
            log.fine("FlushAllLayers: root layer is valid, skipping");
         } else {
            if (log.isLoggable(Level.FINE)) {
               log.fine("FlushAllLayers");
            }

            ContextState var1 = this.state;

            for(int var2 = this.states.size() - 1; var2 >= 0; --var2) {
               Layer var3 = this.state.getLayerNoClone();
               this.state = (ContextState)this.states.get(var2);
               if (var3 != this.state.getLayerNoClone()) {
                  this.renderLayer(var3);
               } else {
                  this.resetCachedGraphics();
               }
            }

            Layer var4 = this.state.getLayerNoClone();
            if (var4 != null) {
               this.renderLayer(var4);
            }

            this.state = var1;
            this.isRootLayerValid = true;
         }
      }
   }

   public void dispose() {
      if (!this.states.isEmpty()) {
         log.fine("Unbalanced saveState/restoreState");
      }

      Iterator var1 = this.states.iterator();

      while(var1.hasNext()) {
         ContextState var2 = (ContextState)var1.next();
         if (var2.getLayerNoClone() != null) {
            var2.getLayerNoClone().dispose();
         }
      }

      this.states.clear();
      if (this.state != null && this.state.getLayerNoClone() != null) {
         this.state.getLayerNoClone().dispose();
      }

      this.state = null;
   }

   public void setClip(WCPath var1, boolean var2) {
      Affine3D var3 = new Affine3D(this.state.getTransformNoClone());
      var1.transform(var3.getMxx(), var3.getMyx(), var3.getMxy(), var3.getMyy(), var3.getMxt(), var3.getMyt());
      if (!var2) {
         WCRectangle var4 = var1.getBounds();
         int var5 = (int)Math.floor((double)var4.getX());
         int var6 = (int)Math.floor((double)var4.getY());
         int var7 = (int)Math.ceil((double)var4.getMaxX()) - var5;
         int var8 = (int)Math.ceil((double)var4.getMaxY()) - var6;
         this.state.clip(new Rectangle(var5, var6, var7, var8));
      }

      Rectangle var9 = this.state.getClipNoClone();
      if (var2) {
         var1.addRect((double)var9.x, (double)var9.y, (double)var9.width, (double)var9.height);
      }

      var1.translate((double)(-var9.x), (double)(-var9.y));
      ClipLayer var10 = new ClipLayer(this.getGraphics(false), var9, var1, this.type() == WCGraphicsPrismContext.Type.DEDICATED);
      this.startNewLayer(var10);
      if (log.isLoggable(Level.FINE)) {
         log.fine("setClip(WCPath " + var1.getID() + ")");
         log.fine("Pushed layer " + var10);
      }

   }

   private Rectangle transformClip(Rectangle var1) {
      if (var1 == null) {
         return null;
      } else {
         float[] var2 = new float[]{(float)var1.x, (float)var1.y, (float)(var1.x + var1.width), (float)var1.y, (float)var1.x, (float)(var1.y + var1.height), (float)(var1.x + var1.width), (float)(var1.y + var1.height)};
         this.state.getTransformNoClone().transform(var2, 0, var2, 0, 4);
         float var3 = Math.min(var2[0], Math.min(var2[2], Math.min(var2[4], var2[6])));
         float var4 = Math.max(var2[0], Math.max(var2[2], Math.max(var2[4], var2[6])));
         float var5 = Math.min(var2[1], Math.min(var2[3], Math.min(var2[5], var2[7])));
         float var6 = Math.max(var2[1], Math.max(var2[3], Math.max(var2[5], var2[7])));
         return new Rectangle(new RectBounds(var3, var5, var4, var6));
      }
   }

   private void setClip(Rectangle var1) {
      Affine3D var2 = this.state.getTransformNoClone();
      if (var2.getMxy() == 0.0 && var2.getMxz() == 0.0 && var2.getMyx() == 0.0 && var2.getMyz() == 0.0 && var2.getMzx() == 0.0 && var2.getMzy() == 0.0) {
         this.state.clip(this.transformClip(var1));
         if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "setClip({0})", var1);
         }

         if (DEBUG_DRAW_CLIP_SHAPE) {
            Rectangle var7 = this.state.getClipNoClone();
            if (var7 != null && var7.width >= 2 && var7.height >= 2) {
               WCTransform var4 = this.getTransform();
               this.setTransform(new WCTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0));
               Graphics var5 = this.getGraphics(true);
               if (var5 != null) {
                  float var6 = (float)Math.random();
                  var5.setPaint(new Color(var6, 1.0F - var6, 0.5F, 0.1F));
                  var5.setStroke(new BasicStroke());
                  var5.fillRect((float)var7.x, (float)var7.y, (float)var7.width, (float)var7.height);
                  var5.setPaint(new Color(1.0F - var6, var6, 0.5F, 1.0F));
                  var5.drawRect((float)var7.x, (float)var7.y, (float)var7.width, (float)var7.height);
               }

               this.setTransform(var4);
               this.state.clip(new Rectangle(var7.x + 1, var7.y + 1, var7.width - 2, var7.height - 2));
            }
         }

         if (this.cachedGraphics != null) {
            this.cachedGraphics.setClipRect(this.state.getClipNoClone());
         }
      } else {
         WCPathImpl var3 = new WCPathImpl();
         var3.addRect((double)var1.x, (double)var1.y, (double)var1.width, (double)var1.height);
         this.setClip(var3, false);
      }

   }

   public void setClip(int var1, int var2, int var3, int var4) {
      this.setClip(new Rectangle(var1, var2, var3, var4));
   }

   public void setClip(WCRectangle var1) {
      this.setClip(new Rectangle((int)var1.getX(), (int)var1.getY(), (int)var1.getWidth(), (int)var1.getHeight()));
   }

   public WCRectangle getClip() {
      Rectangle var1 = this.state.getClipNoClone();
      return var1 == null ? null : new WCRectangle((float)var1.x, (float)var1.y, (float)var1.width, (float)var1.height);
   }

   protected Rectangle getClipRectNoClone() {
      return this.state.getClipNoClone();
   }

   protected Affine3D getTransformNoClone() {
      return this.state.getTransformNoClone();
   }

   public void translate(float var1, float var2) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "translate({0},{1})", new Object[]{var1, var2});
      }

      this.state.translate((double)var1, (double)var2);
      if (this.cachedGraphics != null) {
         this.cachedGraphics.translate(var1, var2);
      }

   }

   public void scale(float var1, float var2) {
      if (log.isLoggable(Level.FINE)) {
         log.fine("scale(" + var1 + " " + var2 + ")");
      }

      this.state.scale((double)var1, (double)var2);
      if (this.cachedGraphics != null) {
         this.cachedGraphics.scale(var1, var2);
      }

   }

   public void rotate(float var1) {
      if (log.isLoggable(Level.FINE)) {
         log.fine("rotate(" + var1 + ")");
      }

      this.state.rotate((double)var1);
      if (this.cachedGraphics != null) {
         this.cachedGraphics.setTransform(this.state.getTransformNoClone());
      }

   }

   protected boolean shouldRenderRect(float var1, float var2, float var3, float var4, DropShadow var5, BasicStroke var6) {
      return true;
   }

   protected boolean shouldRenderShape(Shape var1, DropShadow var2, BasicStroke var3) {
      return true;
   }

   protected boolean shouldCalculateIntersection() {
      return false;
   }

   public void fillRect(final float var1, final float var2, final float var3, final float var4, final Color var5) {
      if (log.isLoggable(Level.FINE)) {
         String var6 = "fillRect(%f, %f, %f, %f, %s)";
         log.fine(String.format(var6, var1, var2, var3, var4, var5));
      }

      if (this.shouldRenderRect(var1, var2, var3, var4, this.state.getShadowNoClone(), (BasicStroke)null)) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               Object var2x = var5 != null ? var5 : WCGraphicsPrismContext.this.state.getPaintNoClone();
               DropShadow var3x = WCGraphicsPrismContext.this.state.getShadowNoClone();
               if (var3x == null && WCGraphicsPrismContext.this.state.getPerspectiveTransformNoClone().isIdentity()) {
                  var1x.setPaint((Paint)var2x);
                  var1x.fillRect(var1, var2, var3, var4);
               } else {
                  NGRectangle var4x = new NGRectangle();
                  var4x.updateRectangle(var1, var2, var3, var4, 0.0F, 0.0F);
                  WCGraphicsPrismContext.this.render(var1x, var3x, (Paint)var2x, (BasicStroke)null, var4x);
               }

            }
         }).paint();
      }
   }

   public void fillRoundedRect(final float var1, final float var2, final float var3, final float var4, final float var5, final float var6, final float var7, final float var8, final float var9, final float var10, final float var11, final float var12, final Color var13) {
      if (log.isLoggable(Level.FINE)) {
         log.fine(String.format("fillRoundedRect(%f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %s)", var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13));
      }

      if (this.shouldRenderRect(var1, var2, var3, var4, this.state.getShadowNoClone(), (BasicStroke)null)) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               float var2x = (var5 + var7 + var9 + var11) / 2.0F;
               float var3x = (var6 + var8 + var10 + var12) / 2.0F;
               DropShadow var4x = WCGraphicsPrismContext.this.state.getShadowNoClone();
               if (var4x != null) {
                  NGRectangle var5x = new NGRectangle();
                  var5x.updateRectangle(var1, var2, var3, var4, var2x, var3x);
                  WCGraphicsPrismContext.this.render(var1x, var4x, var13, (BasicStroke)null, var5x);
               } else {
                  var1x.setPaint(var13);
                  var1x.fillRoundRect(var1, var2, var3, var4, var2x, var3x);
               }

            }
         }).paint();
      }
   }

   public void clearRect(final float var1, final float var2, final float var3, final float var4) {
      if (log.isLoggable(Level.FINE)) {
         log.fine(String.format("clearRect(%f, %f, %f, %f)", var1, var2, var3, var4));
      }

      if (!this.shouldCalculateIntersection()) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               var1x.clearQuad(var1, var2, var1 + var3, var2 + var4);
            }
         }).paint();
      }
   }

   public void setFillColor(Color var1) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, String.format("setFillColor(%s)", var1));
      }

      this.state.setPaint(var1);
   }

   public void setFillGradient(WCGradient var1) {
      if (log.isLoggable(Level.FINE)) {
         log.fine("setFillGradient(" + var1 + ")");
      }

      this.state.setPaint((Gradient)var1.getPlatformGradient());
   }

   public void setTextMode(boolean var1, boolean var2, boolean var3) {
      if (log.isLoggable(Level.FINE)) {
         log.fine("setTextMode(fill:" + var1 + ",stroke:" + var2 + ",clip:" + var3 + ")");
      }

      this.state.setTextMode(var1, var2, var3);
   }

   public void setFontSmoothingType(int var1) {
      this.fontSmoothingType = var1;
   }

   public int getFontSmoothingType() {
      return this.fontSmoothingType;
   }

   public void setStrokeStyle(int var1) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "setStrokeStyle({0})", var1);
      }

      this.state.getStrokeNoClone().setStyle(var1);
   }

   public void setStrokeColor(Color var1) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, String.format("setStrokeColor(%s)", var1));
      }

      this.state.getStrokeNoClone().setPaint(var1);
   }

   public void setStrokeWidth(float var1) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "setStrokeWidth({0})", new Object[]{var1});
      }

      this.state.getStrokeNoClone().setThickness(var1);
   }

   public void setStrokeGradient(WCGradient var1) {
      if (log.isLoggable(Level.FINE)) {
         log.fine("setStrokeGradient(" + var1 + ")");
      }

      this.state.getStrokeNoClone().setPaint((Gradient)var1.getPlatformGradient());
   }

   public void setLineDash(float var1, float... var2) {
      int var4;
      if (log.isLoggable(Level.FINE)) {
         StringBuilder var3 = new StringBuilder("[");

         for(var4 = 0; var4 < var2.length; ++var4) {
            var3.append(var2[var4]).append(',');
         }

         var3.append(']');
         log.log(Level.FINE, "setLineDash({0},{1}", new Object[]{var1, var3});
      }

      this.state.getStrokeNoClone().setDashOffset(var1);
      if (var2 != null) {
         boolean var5 = true;

         for(var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4] != 0.0F) {
               var5 = false;
               break;
            }
         }

         if (var5) {
            var2 = null;
         }
      }

      this.state.getStrokeNoClone().setDashSizes(var2);
   }

   public void setLineCap(int var1) {
      if (log.isLoggable(Level.FINE)) {
         log.fine("setLineCap(" + var1 + ")");
      }

      this.state.getStrokeNoClone().setLineCap(var1);
   }

   public void setLineJoin(int var1) {
      if (log.isLoggable(Level.FINE)) {
         log.fine("setLineJoin(" + var1 + ")");
      }

      this.state.getStrokeNoClone().setLineJoin(var1);
   }

   public void setMiterLimit(float var1) {
      if (log.isLoggable(Level.FINE)) {
         log.fine("setMiterLimit(" + var1 + ")");
      }

      this.state.getStrokeNoClone().setMiterLimit(var1);
   }

   public void setShadow(float var1, float var2, float var3, Color var4) {
      if (log.isLoggable(Level.FINE)) {
         String var5 = "setShadow(%f, %f, %f, %s)";
         log.fine(String.format(var5, var1, var2, var3, var4));
      }

      this.state.setShadow(this.createShadow(var1, var2, var3, var4));
   }

   public void drawPolygon(final WCPath var1, boolean var2) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "drawPolygon({0})", new Object[]{var2});
      }

      if (this.shouldRenderShape(((WCPathImpl)var1).getPlatformPath(), (DropShadow)null, this.state.getStrokeNoClone().getPlatformStroke())) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               Path2D var2 = (Path2D)var1.getPlatformPath();
               var1x.setPaint(WCGraphicsPrismContext.this.state.getPaintNoClone());
               var1x.fill(var2);
               if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(var1x)) {
                  var1x.draw(var2);
               }

            }
         }).paint();
      }
   }

   public void drawLine(final int var1, final int var2, final int var3, final int var4) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "drawLine({0}, {1}, {2}, {3})", new Object[]{var1, var2, var3, var4});
      }

      Line2D var5 = new Line2D((float)var1, (float)var2, (float)var3, (float)var4);
      if (this.shouldRenderShape(var5, (DropShadow)null, this.state.getStrokeNoClone().getPlatformStroke())) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(var1x)) {
                  var1x.drawLine((float)var1, (float)var2, (float)var3, (float)var4);
               }

            }
         }).paint();
      }
   }

   public void drawPattern(final WCImage var1, final WCRectangle var2, final WCTransform var3, final WCPoint var4, final WCRectangle var5) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "drawPattern({0}, {1}, {2}, {3})", new Object[]{var5.getIntX(), var5.getIntY(), var5.getIntWidth(), var5.getIntHeight()});
      }

      if (this.shouldRenderRect(var5.getX(), var5.getY(), var5.getWidth(), var5.getHeight(), (DropShadow)null, (BasicStroke)null)) {
         if (var1 != null) {
            (new Composite() {
               void doPaint(Graphics var1x) {
                  Image var2x = ((PrismImage)var1).getImage();
                  if (!var2.contains(new WCRectangle(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight()))) {
                     var2x = var2x.createSubImage(var2.getIntX(), var2.getIntY(), (int)Math.ceil((double)var2.getWidth()), (int)Math.ceil((double)var2.getHeight()));
                  }

                  double[] var3x = var3.getMatrix();
                  Affine3D var4x = new Affine3D();
                  var4x.translate((double)var4.getX(), (double)var4.getY());
                  var4x.concatenate(var3x[0], var3x[2], var3x[4], var3x[1], var3x[3], var3x[5]);
                  var1x.setPaint(new ImagePattern(var2x, var2.getX(), var2.getY(), var2.getWidth(), var2.getHeight(), var4x, false, false));
                  var1x.fillRect(var5.getX(), var5.getY(), var5.getWidth(), var5.getHeight());
               }
            }).paint();
         }

      }
   }

   public void drawImage(final WCImage var1, final float var2, final float var3, final float var4, final float var5, final float var6, final float var7, final float var8, final float var9) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "drawImage(img, dst({0},{1},{2},{3}), src({4},{5},{6},{7}))", new Object[]{var2, var3, var4, var5, var6, var7, var8, var9});
      }

      if (this.shouldRenderRect(var2, var3, var4, var5, this.state.getShadowNoClone(), (BasicStroke)null)) {
         if (var1 instanceof PrismImage) {
            (new Composite() {
               void doPaint(Graphics var1x) {
                  PrismImage var2x = (PrismImage)var1;
                  DropShadow var3x = WCGraphicsPrismContext.this.state.getShadowNoClone();
                  if (var3x != null) {
                     NGImageView var4x = new NGImageView();
                     var4x.setImage(var2x.getImage());
                     var4x.setX(var2);
                     var4x.setY(var3);
                     var4x.setViewport(var6, var7, var8, var9, var4, var5);
                     var4x.setContentBounds(new RectBounds(var2, var3, var2 + var4, var3 + var5));
                     WCGraphicsPrismContext.this.render(var1x, var3x, (Paint)null, (BasicStroke)null, var4x);
                  } else {
                     var2x.draw(var1x, (int)var2, (int)var3, (int)(var2 + var4), (int)(var3 + var5), (int)var6, (int)var7, (int)(var6 + var8), (int)(var7 + var9));
                  }

               }
            }).paint();
         }

      }
   }

   public void drawBitmapImage(final ByteBuffer var1, final int var2, final int var3, final int var4, final int var5) {
      if (this.shouldRenderRect((float)var2, (float)var3, (float)var4, (float)var5, (DropShadow)null, (BasicStroke)null)) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               var1.order(ByteOrder.nativeOrder());
               Image var2x = Image.fromByteBgraPreData(var1, var4, var5);
               ResourceFactory var3x = var1x.getResourceFactory();
               Texture var4x = var3x.createTexture(var2x, Texture.Usage.STATIC, Texture.WrapMode.REPEAT);
               var1x.drawTexture(var4x, (float)var2, (float)var3, (float)(var2 + var4), (float)(var3 + var5), 0.0F, 0.0F, (float)var4, (float)var5);
               var4x.dispose();
            }
         }).paint();
      }
   }

   public void drawIcon(WCIcon var1, int var2, int var3) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "UNIMPLEMENTED drawIcon ({0}, {1})", new Object[]{var2, var3});
      }

   }

   public void drawRect(final int var1, final int var2, final int var3, final int var4) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "drawRect({0}, {1}, {2}, {3})", new Object[]{var1, var2, var3, var4});
      }

      if (this.shouldRenderRect((float)var1, (float)var2, (float)var3, (float)var4, (DropShadow)null, this.state.getStrokeNoClone().getPlatformStroke())) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               Paint var2x = WCGraphicsPrismContext.this.state.getPaintNoClone();
               if (var2x != null && var2x.isOpaque()) {
                  var1x.setPaint(var2x);
                  var1x.fillRect((float)var1, (float)var2, (float)var3, (float)var4);
               }

               if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(var1x)) {
                  var1x.drawRect((float)var1, (float)var2, (float)var3, (float)var4);
               }

            }
         }).paint();
      }
   }

   public void drawString(WCFont var1, int[] var2, float[] var3, final float var4, final float var5) {
      if (log.isLoggable(Level.FINE)) {
         log.fine(String.format("Drawing %d glyphs @(%.1f, %.1f)", var2.length, var4, var5));
      }

      final PGFont var6 = (PGFont)var1.getPlatformFont();
      final TextRun var7 = TextUtilities.createGlyphList(var2, var3, var4, var5);
      final DropShadow var8 = this.state.getShadowNoClone();
      final BasicStroke var9 = this.state.isTextStroke() ? this.state.getStrokeNoClone().getPlatformStroke() : null;
      final FontStrike var10 = var6.getStrike(this.getTransformNoClone(), this.getFontSmoothingType());
      if (this.shouldCalculateIntersection()) {
         Metrics var11 = var10.getMetrics();
         var7.setMetrics(var11.getAscent(), var11.getDescent(), var11.getLineGap());
         if (!this.shouldRenderRect(var4, var5, var7.getWidth(), var7.getHeight(), var8, var9)) {
            return;
         }
      }

      (new Composite() {
         void doPaint(Graphics var1) {
            Paint var2 = WCGraphicsPrismContext.this.state.isTextFill() ? WCGraphicsPrismContext.this.state.getPaintNoClone() : null;
            if (var8 != null) {
               NGText var3 = new NGText();
               var3.setGlyphs(new GlyphList[]{var7});
               var3.setFont(var6);
               var3.setFontSmoothingType(WCGraphicsPrismContext.this.fontSmoothingType);
               WCGraphicsPrismContext.this.render(var1, var8, var2, var9, var3);
            } else {
               if (var2 != null) {
                  var1.setPaint(var2);
                  var1.drawString(var7, var10, var4, var5, (Color)null, 0, 0);
               }

               if (var9 != null) {
                  var2 = (Paint)WCGraphicsPrismContext.this.state.getStrokeNoClone().getPaint();
                  if (var2 != null) {
                     var1.setPaint(var2);
                     var1.setStroke(var9);
                     var1.draw(var10.getOutline(var7, BaseTransform.getTranslateInstance((double)var4, (double)var5)));
                  }
               }
            }

         }
      }).paint();
   }

   public void drawString(WCFont var1, String var2, boolean var3, int var4, int var5, float var6, float var7) {
      if (log.isLoggable(Level.FINE)) {
         log.fine(String.format("str='%s' (length=%d), from=%d, to=%d, rtl=%b, @(%.1f, %.1f)", var2, var2.length(), var4, var5, var3, var6, var7));
      }

      TextLayout var8 = TextUtilities.createLayout(var2.substring(var4, var5), var1.getPlatformFont());
      int var9 = 0;
      GlyphList[] var10 = var8.getRuns();
      GlyphList[] var11 = var10;
      int var12 = var10.length;

      for(int var13 = 0; var13 < var12; ++var13) {
         GlyphList var14 = var11[var13];
         var9 += var14.getGlyphCount();
      }

      int[] var19 = new int[var9];
      float[] var20 = new float[var9];
      var9 = 0;
      GlyphList[] var21 = var8.getRuns();
      int var22 = var21.length;

      for(int var15 = 0; var15 < var22; ++var15) {
         GlyphList var16 = var21[var15];
         int var17 = var16.getGlyphCount();

         for(int var18 = 0; var18 < var17; ++var18) {
            var19[var9] = var16.getGlyphCode(var18);
            var20[var9] = var16.getPosX(var18 + 1) - var16.getPosX(var18);
            ++var9;
         }
      }

      if (var3) {
         var6 += TextUtilities.getLayoutWidth(var2.substring(var4), var1.getPlatformFont()) - var8.getBounds().getWidth();
      } else {
         var6 += TextUtilities.getLayoutWidth(var2.substring(0, var4), var1.getPlatformFont());
      }

      this.drawString(var1, var19, var20, var6, var7);
   }

   public void setComposite(int var1) {
      log.log(Level.FINE, "setComposite({0})", var1);
      this.state.setCompositeOperation(var1);
   }

   public void drawEllipse(final int var1, final int var2, final int var3, final int var4) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "drawEllipse({0}, {1}, {2}, {3})", new Object[]{var1, var2, var3, var4});
      }

      if (this.shouldRenderRect((float)var1, (float)var2, (float)var3, (float)var4, (DropShadow)null, this.state.getStrokeNoClone().getPlatformStroke())) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               var1x.setPaint(WCGraphicsPrismContext.this.state.getPaintNoClone());
               var1x.fillEllipse((float)var1, (float)var2, (float)var3, (float)var4);
               if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(var1x)) {
                  var1x.drawEllipse((float)var1, (float)var2, (float)var3, (float)var4);
               }

            }
         }).paint();
      }
   }

   public void drawFocusRing(final int var1, final int var2, final int var3, final int var4, final Color var5) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, String.format("drawFocusRing: %d, %d, %d, %d, %s", var1, var2, var3, var4, var5));
      }

      if (this.shouldRenderRect((float)var1, (float)var2, (float)var3, (float)var4, (DropShadow)null, focusRingStroke)) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               var1x.setPaint(var5);
               BasicStroke var2x = var1x.getStroke();
               var1x.setStroke(WCGraphicsPrismContext.focusRingStroke);
               var1x.drawRoundRect((float)var1, (float)var2, (float)var3, (float)var4, 4.0F, 4.0F);
               var1x.setStroke(var2x);
            }
         }).paint();
      }
   }

   public void setAlpha(float var1) {
      log.log(Level.FINE, "setAlpha({0})", var1);
      this.state.setAlpha(var1);
      if (null != this.cachedGraphics) {
         this.cachedGraphics.setExtraAlpha(this.state.getAlpha());
      }

   }

   public float getAlpha() {
      return this.state.getAlpha();
   }

   public void beginTransparencyLayer(float var1) {
      TransparencyLayer var2 = new TransparencyLayer(this.getGraphics(false), this.state.getClipNoClone(), var1);
      if (log.isLoggable(Level.FINE)) {
         log.fine(String.format("beginTransparencyLayer(%s)", var2));
      }

      this.state.markAsRestorePoint();
      this.startNewLayer(var2);
   }

   public void endTransparencyLayer() {
      if (log.isLoggable(Level.FINE)) {
         log.fine(String.format("endTransparencyLayer(%s)", this.state.getLayerNoClone()));
      }

      this.restoreState();
   }

   public void drawWidget(final RenderTheme var1, final Ref var2, final int var3, final int var4) {
      WCSize var5 = var1.getWidgetSize(var2);
      if (this.shouldRenderRect((float)var3, (float)var4, var5.getWidth(), var5.getHeight(), (DropShadow)null, (BasicStroke)null)) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               var1.drawWidget(WCGraphicsPrismContext.this, var2, var3, var4);
            }
         }).paint();
      }
   }

   public void drawScrollbar(final ScrollBarTheme var1, final Ref var2, final int var3, final int var4, final int var5, final int var6) {
      if (log.isLoggable(Level.FINE)) {
         log.fine(String.format("drawScrollbar(%s, %s, x = %d, y = %d)", var1, var2, var3, var4));
      }

      WCSize var7 = var1.getWidgetSize(var2);
      if (this.shouldRenderRect((float)var3, (float)var4, var7.getWidth(), var7.getHeight(), (DropShadow)null, (BasicStroke)null)) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               var1.paint(WCGraphicsPrismContext.this, var2, var3, var4, var5, var6);
            }
         }).paint();
      }
   }

   private static Rectangle intersect(Rectangle var0, Rectangle var1) {
      if (var0 == null) {
         return var1;
      } else {
         RectBounds var2 = var0.toRectBounds();
         var2.intersectWith(var1);
         var0.setBounds((BaseBounds)var2);
         return var0;
      }
   }

   private static Color4f createColor4f(Color var0) {
      return new Color4f(var0.getRed(), var0.getGreen(), var0.getBlue(), var0.getAlpha());
   }

   private DropShadow createShadow(float var1, float var2, float var3, Color var4) {
      if (var1 == 0.0F && var2 == 0.0F && var3 == 0.0F) {
         return null;
      } else {
         DropShadow var5 = new DropShadow();
         var5.setOffsetX((int)var1);
         var5.setOffsetY((int)var2);
         var5.setRadius(var3 < 0.0F ? 0.0F : (var3 > 127.0F ? 127.0F : var3));
         var5.setColor(createColor4f(var4));
         return var5;
      }
   }

   private void render(Graphics var1, Effect var2, Paint var3, BasicStroke var4, NGNode var5) {
      if (var5 instanceof NGShape) {
         NGShape var6 = (NGShape)var5;
         Shape var7 = var6.getShape();
         Paint var8 = (Paint)this.state.getStrokeNoClone().getPaint();
         if (var4 != null && var8 != null) {
            var7 = var4.createStrokedShape(var7);
            var6.setDrawStroke(var4);
            var6.setDrawPaint(var8);
            var6.setMode(var3 == null ? NGShape.Mode.STROKE : NGShape.Mode.STROKE_FILL);
         } else {
            var6.setMode(var3 == null ? NGShape.Mode.EMPTY : NGShape.Mode.FILL);
         }

         var6.setFillPaint(var3);
         var6.setContentBounds(var7.getBounds());
      }

      boolean var9 = var1.hasPreCullingBits();
      var1.setHasPreCullingBits(false);
      var5.setEffect(var2);
      var5.render(var1);
      var1.setHasPreCullingBits(var9);
   }

   private static FilterContext getFilterContext(Graphics var0) {
      Screen var1 = var0.getAssociatedScreen();
      if (var1 == null) {
         ResourceFactory var2 = var0.getResourceFactory();
         return PrFilterContext.getPrinterContext(var2);
      } else {
         return PrFilterContext.getInstance(var1);
      }
   }

   public void strokeArc(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (log.isLoggable(Level.FINE)) {
         log.fine(String.format("strokeArc(%d, %d, %d, %d, %d, %d)", var1, var2, var3, var4, var5, var6));
      }

      final Arc2D var7 = new Arc2D((float)var1, (float)var2, (float)var3, (float)var4, (float)var5, (float)var6, 0);
      if (!this.state.getStrokeNoClone().isApplicable() || this.shouldRenderShape(var7, (DropShadow)null, this.state.getStrokeNoClone().getPlatformStroke())) {
         (new Composite() {
            void doPaint(Graphics var1) {
               if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(var1)) {
                  var1.draw(var7);
               }

            }
         }).paint();
      }
   }

   public WCImage getImage() {
      return null;
   }

   public void strokeRect(final float var1, final float var2, final float var3, final float var4, float var5) {
      if (log.isLoggable(Level.FINE)) {
         log.fine(String.format("strokeRect_FFFFF(%f, %f, %f, %f, %f)", var1, var2, var3, var4, var5));
      }

      final BasicStroke var6 = new BasicStroke(var5, 0, 0, Math.max(1.0F, var5), this.state.getStrokeNoClone().getDashSizes(), this.state.getStrokeNoClone().getDashOffset());
      if (this.shouldRenderRect(var1, var2, var3, var4, (DropShadow)null, var6)) {
         (new Composite() {
            void doPaint(Graphics var1x) {
               var1x.setStroke(var6);
               Paint var2x = (Paint)WCGraphicsPrismContext.this.state.getStrokeNoClone().getPaint();
               if (var2x == null) {
                  var2x = WCGraphicsPrismContext.this.state.getPaintNoClone();
               }

               var1x.setPaint(var2x);
               var1x.drawRect(var1, var2, var3, var4);
            }
         }).paint();
      }
   }

   public void strokePath(WCPath var1) {
      log.fine("strokePath");
      if (var1 != null) {
         final BasicStroke var2 = this.state.getStrokeNoClone().getPlatformStroke();
         final DropShadow var3 = this.state.getShadowNoClone();
         final Path2D var4 = (Path2D)var1.getPlatformPath();
         if (var2 == null && var3 == null || !this.shouldRenderShape(var4, var3, var2)) {
            return;
         }

         (new Composite() {
            void doPaint(Graphics var1) {
               if (var3 != null) {
                  NGPath var2x = new NGPath();
                  var2x.updateWithPath2d(var4);
                  WCGraphicsPrismContext.this.render(var1, var3, (Paint)null, var2, var2x);
               } else if (var2 != null) {
                  Paint var3x = (Paint)WCGraphicsPrismContext.this.state.getStrokeNoClone().getPaint();
                  if (var3x == null) {
                     var3x = WCGraphicsPrismContext.this.state.getPaintNoClone();
                  }

                  var1.setPaint(var3x);
                  var1.setStroke(var2);
                  var1.draw(var4);
               }

            }
         }).paint();
      }

   }

   public void fillPath(final WCPath var1) {
      log.fine("fillPath");
      if (var1 != null) {
         if (!this.shouldRenderShape(((WCPathImpl)var1).getPlatformPath(), this.state.getShadowNoClone(), (BasicStroke)null)) {
            return;
         }

         (new Composite() {
            void doPaint(Graphics var1x) {
               Path2D var2 = (Path2D)var1.getPlatformPath();
               Paint var3 = WCGraphicsPrismContext.this.state.getPaintNoClone();
               DropShadow var4 = WCGraphicsPrismContext.this.state.getShadowNoClone();
               if (var4 != null) {
                  NGPath var5 = new NGPath();
                  var5.updateWithPath2d(var2);
                  WCGraphicsPrismContext.this.render(var1x, var4, var3, (BasicStroke)null, var5);
               } else {
                  var1x.setPaint(var3);
                  var1x.fill(var2);
               }

            }
         }).paint();
      }

   }

   public void setPerspectiveTransform(WCTransform var1) {
      GeneralTransform3D var2 = (new GeneralTransform3D()).set(var1.getMatrix());
      this.state.setPerspectiveTransform(var2);
      this.resetCachedGraphics();
   }

   public void setTransform(WCTransform var1) {
      double[] var2 = var1.getMatrix();
      Affine3D var3 = new Affine3D(new Affine2D(var2[0], var2[1], var2[2], var2[3], var2[4], var2[5]));
      if (this.state.getLayerNoClone() == null) {
         var3.preConcatenate(this.baseTransform);
      }

      this.state.setTransform(var3);
      this.resetCachedGraphics();
   }

   public WCTransform getTransform() {
      Affine3D var1 = this.state.getTransformNoClone();
      return new WCTransform(var1.getMxx(), var1.getMyx(), var1.getMxy(), var1.getMyy(), var1.getMxt(), var1.getMyt());
   }

   public void concatTransform(WCTransform var1) {
      double[] var2 = var1.getMatrix();
      Affine3D var3 = new Affine3D(new Affine2D(var2[0], var2[1], var2[2], var2[3], var2[4], var2[5]));
      this.state.concatTransform(var3);
      this.resetCachedGraphics();
   }

   public void flush() {
      this.flushAllLayers();
   }

   public WCGradient createLinearGradient(WCPoint var1, WCPoint var2) {
      return new WCLinearGradient(var1, var2);
   }

   public WCGradient createRadialGradient(WCPoint var1, float var2, WCPoint var3, float var4) {
      return new WCRadialGradient(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   static boolean access$4002(WCGraphicsPrismContext var0, boolean var1) {
      return var0.isRootLayerValid = var1;
   }

   private static final class PassThrough extends Effect {
      private final PrDrawable img;
      private final int width;
      private final int height;

      private PassThrough(PrDrawable var1, int var2, int var3) {
         this.img = var1;
         this.width = var2;
         this.height = var3;
      }

      public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
         this.img.lock();
         ImageData var6 = new ImageData(var1, this.img, new Rectangle((int)var2.getMxt(), (int)var2.getMyt(), this.width, this.height));
         var6.setReusable(true);
         return var6;
      }

      public RectBounds getBounds(BaseTransform var1, Effect var2) {
         return null;
      }

      public Effect.AccelType getAccelType(FilterContext var1) {
         return Effect.AccelType.INTRINSIC;
      }

      public boolean reducesOpaquePixels() {
         return false;
      }

      public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
         return null;
      }

      // $FF: synthetic method
      PassThrough(PrDrawable var1, int var2, int var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private abstract class Composite {
      private Composite() {
      }

      abstract void doPaint(Graphics var1);

      void paint() {
         this.paint(WCGraphicsPrismContext.this.getGraphics(true));
      }

      void paint(Graphics var1) {
         // $FF: Couldn't be decompiled
      }

      private void blend(Graphics var1) {
         FilterContext var2 = WCGraphicsPrismContext.getFilterContext(var1);
         PrDrawable var3 = null;
         PrDrawable var4 = null;
         ReadbackGraphics var5 = null;
         RTTexture var6 = null;
         Rectangle var7 = WCGraphicsPrismContext.this.state.getClipNoClone();
         WCImage var8 = WCGraphicsPrismContext.this.getImage();

         try {
            Graphics var9;
            if (var8 != null && var8 instanceof PrismImage) {
               var3 = (PrDrawable)Effect.getCompatibleImage(var2, var7.width, var7.height);
               var9 = var3.createGraphics();
               WCGraphicsPrismContext.this.state.apply(var9);
               ((PrismImage)var8).draw(var9, 0, 0, var7.width, var7.height, var7.x, var7.y, var7.width, var7.height);
            } else {
               var5 = (ReadbackGraphics)var1;
               var6 = var5.readBack(var7);
               var3 = PrDrawable.create(var2, var6);
            }

            var4 = (PrDrawable)Effect.getCompatibleImage(var2, var7.width, var7.height);
            var9 = var4.createGraphics();
            WCGraphicsPrismContext.this.state.apply(var9);
            this.doPaint(var9);
            var1.clear();
            PrEffectHelper.render(this.createEffect(var3, var4, var7.width, var7.height), var1, 0.0F, 0.0F, (Effect)null);
         } finally {
            if (var4 != null) {
               Effect.releaseCompatibleImage(var2, var4);
            }

            if (var3 != null) {
               if (var5 != null && var6 != null) {
                  var5.releaseReadBackBuffer(var6);
               } else {
                  Effect.releaseCompatibleImage(var2, var3);
               }
            }

         }

      }

      private Effect createBlend(Blend.Mode var1, PrDrawable var2, PrDrawable var3, int var4, int var5) {
         return new Blend(var1, new PassThrough(var2, var4, var5), new PassThrough(var3, var4, var5));
      }

      private Effect createEffect(PrDrawable var1, PrDrawable var2, int var3, int var4) {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      Composite(Object var2) {
         this();
      }
   }

   private static final class ClipLayer extends Layer {
      private final WCPath normalizedToClipPath;
      private boolean srcover;

      private ClipLayer(Graphics var1, Rectangle var2, WCPath var3, boolean var4) {
         super(var1, var2, var4);
         this.normalizedToClipPath = var3;
         this.srcover = true;
      }

      void init(Graphics var1) {
         RTTexture var2 = null;
         ReadbackGraphics var3 = null;

         try {
            var3 = (ReadbackGraphics)var1;
            var2 = var3.readBack(this.bounds);
            this.getGraphics().drawTexture(var2, 0.0F, 0.0F, (float)this.bounds.width, (float)this.bounds.height);
         } finally {
            if (var3 != null && var2 != null) {
               var3.releaseReadBackBuffer(var2);
            }

         }

         this.srcover = false;
      }

      void render(Graphics var1) {
         Path2D var2 = ((WCPathImpl)this.normalizedToClipPath).getPlatformPath();
         PrDrawable var3 = (PrDrawable)Effect.getCompatibleImage(this.fctx, this.bounds.width, this.bounds.height);
         Graphics var4 = var3.createGraphics();
         var4.setPaint(Color.BLACK);
         var4.fill(var2);
         if (var1 instanceof MaskTextureGraphics && !(var1 instanceof PrinterGraphics)) {
            MaskTextureGraphics var7 = (MaskTextureGraphics)var1;
            if (this.srcover) {
               var7.drawPixelsMasked((RTTexture)this.buffer.getTextureObject(), (RTTexture)var3.getTextureObject(), this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, 0, 0, 0, 0);
            } else {
               var7.maskInterpolatePixels((RTTexture)this.buffer.getTextureObject(), (RTTexture)var3.getTextureObject(), this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, 0, 0, 0, 0);
            }
         } else {
            Blend var5 = new Blend(Blend.Mode.SRC_IN, new PassThrough(var3, this.bounds.width, this.bounds.height), new PassThrough(this.buffer, this.bounds.width, this.bounds.height));
            Affine3D var6 = new Affine3D(var1.getTransformNoClone());
            var1.setTransform(BaseTransform.IDENTITY_TRANSFORM);
            PrEffectHelper.render(var5, var1, (float)this.bounds.x, (float)this.bounds.y, (Effect)null);
            var1.setTransform(var6);
         }

         Effect.releaseCompatibleImage(this.fctx, var3);
      }

      public String toString() {
         return String.format("ClipLayer[%d,%d + %dx%d, path %s]", this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, this.normalizedToClipPath);
      }

      // $FF: synthetic method
      ClipLayer(Graphics var1, Rectangle var2, WCPath var3, boolean var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }

   private final class TransparencyLayer extends Layer {
      private final float opacity;

      private TransparencyLayer(Graphics var2, Rectangle var3, float var4) {
         super(var2, var3, false);
         this.opacity = var4;
      }

      void init(Graphics var1) {
         WCGraphicsPrismContext.this.state.setCompositeOperation(2);
      }

      void render(Graphics var1) {
         (new Composite() {
            void doPaint(Graphics var1) {
               float var2 = var1.getExtraAlpha();
               var1.setExtraAlpha(TransparencyLayer.this.opacity);
               Affine3D var3 = new Affine3D(var1.getTransformNoClone());
               var1.setTransform(BaseTransform.IDENTITY_TRANSFORM);
               var1.drawTexture(TransparencyLayer.this.buffer.getTextureObject(), (float)TransparencyLayer.this.bounds.x, (float)TransparencyLayer.this.bounds.y, (float)TransparencyLayer.this.bounds.width, (float)TransparencyLayer.this.bounds.height);
               var1.setTransform(var3);
               var1.setExtraAlpha(var2);
            }
         }).paint(var1);
      }

      public String toString() {
         return String.format("TransparencyLayer[%d,%d + %dx%d, opacity %.2f]", this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, this.opacity);
      }

      // $FF: synthetic method
      TransparencyLayer(Graphics var2, Rectangle var3, float var4, Object var5) {
         this(var2, var3, var4);
      }
   }

   private abstract static class Layer {
      FilterContext fctx;
      PrDrawable buffer;
      Graphics graphics;
      final Rectangle bounds;
      boolean permanent;

      Layer(Graphics var1, Rectangle var2, boolean var3) {
         this.bounds = new Rectangle(var2);
         this.permanent = var3;
         int var4 = Math.max(var2.width, 1);
         int var5 = Math.max(var2.height, 1);
         this.fctx = WCGraphicsPrismContext.getFilterContext(var1);
         if (var3) {
            ResourceFactory var6 = GraphicsPipeline.getDefaultResourceFactory();
            RTTexture var7 = var6.createRTTexture(var4, var5, Texture.WrapMode.CLAMP_NOT_NEEDED);
            var7.makePermanent();
            this.buffer = ((PrRenderer)Renderer.getRenderer(this.fctx)).createDrawable(var7);
         } else {
            this.buffer = (PrDrawable)Effect.getCompatibleImage(this.fctx, var4, var5);
         }

      }

      Graphics getGraphics() {
         if (this.graphics == null) {
            this.graphics = this.buffer.createGraphics();
         }

         return this.graphics;
      }

      abstract void init(Graphics var1);

      abstract void render(Graphics var1);

      private void dispose() {
         if (this.buffer != null) {
            if (this.permanent) {
               this.buffer.flush();
            } else {
               Effect.releaseCompatibleImage(this.fctx, this.buffer);
            }

            this.fctx = null;
            this.buffer = null;
         }

      }

      private double getX() {
         return (double)this.bounds.x;
      }

      private double getY() {
         return (double)this.bounds.y;
      }
   }

   private static final class ContextState {
      private final WCStrokeImpl stroke;
      private Rectangle clip;
      private Paint paint;
      private float alpha;
      private boolean textFill;
      private boolean textStroke;
      private boolean textClip;
      private boolean restorePoint;
      private DropShadow shadow;
      private Affine3D xform;
      private GeneralTransform3D perspectiveTransform;
      private Layer layer;
      private int compositeOperation;

      private ContextState() {
         this.stroke = new WCStrokeImpl();
         this.textFill = true;
         this.textStroke = false;
         this.textClip = false;
         this.restorePoint = false;
         this.clip = null;
         this.paint = Color.BLACK;
         this.stroke.setPaint(Color.BLACK);
         this.alpha = 1.0F;
         this.xform = new Affine3D();
         this.perspectiveTransform = new GeneralTransform3D();
         this.compositeOperation = 2;
      }

      private ContextState(ContextState var1) {
         this.stroke = new WCStrokeImpl();
         this.textFill = true;
         this.textStroke = false;
         this.textClip = false;
         this.restorePoint = false;
         this.stroke.copyFrom(var1.getStrokeNoClone());
         this.setPaint(var1.getPaintNoClone());
         this.clip = var1.getClipNoClone();
         if (this.clip != null) {
            this.clip = new Rectangle(this.clip);
         }

         this.xform = new Affine3D(var1.getTransformNoClone());
         this.perspectiveTransform = (new GeneralTransform3D()).set(var1.getPerspectiveTransformNoClone());
         this.setShadow(var1.getShadowNoClone());
         this.setLayer(var1.getLayerNoClone());
         this.setAlpha(var1.getAlpha());
         this.setTextMode(var1.isTextFill(), var1.isTextStroke(), var1.isTextClip());
         this.setCompositeOperation(var1.getCompositeOperation());
      }

      protected ContextState clone() {
         return new ContextState(this);
      }

      private void apply(Graphics var1) {
         var1.setTransform(this.getTransformNoClone());
         var1.setPerspectiveTransform(this.getPerspectiveTransformNoClone());
         var1.setClipRect(this.getClipNoClone());
         var1.setExtraAlpha(this.getAlpha());
      }

      private int getCompositeOperation() {
         return this.compositeOperation;
      }

      private void setCompositeOperation(int var1) {
         this.compositeOperation = var1;
      }

      private WCStrokeImpl getStrokeNoClone() {
         return this.stroke;
      }

      private Paint getPaintNoClone() {
         return this.paint;
      }

      private void setPaint(Paint var1) {
         this.paint = var1;
      }

      private Rectangle getClipNoClone() {
         return this.clip;
      }

      private Layer getLayerNoClone() {
         return this.layer;
      }

      private void setLayer(Layer var1) {
         this.layer = var1;
      }

      private void setClip(Rectangle var1) {
         this.clip = var1;
      }

      private void clip(Rectangle var1) {
         if (null == this.clip) {
            this.clip = var1;
         } else {
            this.clip.intersectWith(var1);
         }

      }

      private void setAlpha(float var1) {
         this.alpha = var1;
      }

      private float getAlpha() {
         return this.alpha;
      }

      private void setTextMode(boolean var1, boolean var2, boolean var3) {
         this.textFill = var1;
         this.textStroke = var2;
         this.textClip = var3;
      }

      private boolean isTextFill() {
         return this.textFill;
      }

      private boolean isTextStroke() {
         return this.textStroke;
      }

      private boolean isTextClip() {
         return this.textClip;
      }

      private void markAsRestorePoint() {
         this.restorePoint = true;
      }

      private boolean isRestorePoint() {
         return this.restorePoint;
      }

      private void setShadow(DropShadow var1) {
         this.shadow = var1;
      }

      private DropShadow getShadowNoClone() {
         return this.shadow;
      }

      private Affine3D getTransformNoClone() {
         return this.xform;
      }

      private GeneralTransform3D getPerspectiveTransformNoClone() {
         return this.perspectiveTransform;
      }

      private void setTransform(Affine3D var1) {
         this.xform.setTransform(var1);
      }

      private void setPerspectiveTransform(GeneralTransform3D var1) {
         this.perspectiveTransform.set(var1);
      }

      private void concatTransform(Affine3D var1) {
         this.xform.concatenate(var1);
      }

      private void translate(double var1, double var3) {
         this.xform.translate(var1, var3);
      }

      private void scale(double var1, double var3) {
         this.xform.scale(var1, var3);
      }

      private void rotate(double var1) {
         this.xform.rotate(var1);
      }

      // $FF: synthetic method
      ContextState(Object var1) {
         this();
      }

      // $FF: synthetic method
      static int access$3900(ContextState var0) {
         return var0.getCompositeOperation();
      }
   }

   public static enum Type {
      PRIMARY,
      DEDICATED;
   }
}
