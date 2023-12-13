package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.pisces.PiscesRenderer;
import com.sun.pisces.Transform6;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.Paint;

final class SWGraphics implements ReadbackGraphics {
   private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1.0F, 2, 0, 10.0F);
   private static final Paint DEFAULT_PAINT;
   private final PiscesRenderer pr;
   private final SWContext context;
   private final SWRTTexture target;
   private final SWPaint swPaint;
   private final BaseTransform tx = new Affine2D();
   private CompositeMode compositeMode;
   private Rectangle clip;
   private final Rectangle finalClip;
   private RectBounds nodeBounds;
   private int clipRectIndex;
   private Paint paint;
   private BasicStroke stroke;
   private Ellipse2D ellipse2d;
   private Line2D line2d;
   private RoundRectangle2D rect2d;
   private boolean antialiasedShape;
   private boolean hasPreCullingBits;
   private float pixelScaleX;
   private float pixelScaleY;
   private NodePath renderRoot;

   public void setRenderRoot(NodePath var1) {
      this.renderRoot = var1;
   }

   public NodePath getRenderRoot() {
      return this.renderRoot;
   }

   public SWGraphics(SWRTTexture var1, SWContext var2, PiscesRenderer var3) {
      this.compositeMode = CompositeMode.SRC_OVER;
      this.finalClip = new Rectangle();
      this.paint = DEFAULT_PAINT;
      this.stroke = DEFAULT_STROKE;
      this.antialiasedShape = true;
      this.hasPreCullingBits = false;
      this.pixelScaleX = 1.0F;
      this.pixelScaleY = 1.0F;
      this.target = var1;
      this.context = var2;
      this.pr = var3;
      this.swPaint = new SWPaint(var2, var3);
      this.setClipRect((Rectangle)null);
   }

   public RenderTarget getRenderTarget() {
      return this.target;
   }

   public SWResourceFactory getResourceFactory() {
      return this.target.getResourceFactory();
   }

   public Screen getAssociatedScreen() {
      return this.target.getAssociatedScreen();
   }

   public void sync() {
   }

   public BaseTransform getTransformNoClone() {
      if (PrismSettings.debug) {
         System.out.println("+ getTransformNoClone " + this + "; tr: " + this.tx);
      }

      return this.tx;
   }

   public void setTransform(BaseTransform var1) {
      if (var1 == null) {
         var1 = BaseTransform.IDENTITY_TRANSFORM;
      }

      if (PrismSettings.debug) {
         System.out.println("+ setTransform " + this + "; tr: " + var1);
      }

      this.tx.setTransform(var1);
   }

   public void setTransform(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.tx.restoreTransform(var1, var3, var5, var7, var9, var11);
      if (PrismSettings.debug) {
         System.out.println("+ restoreTransform " + this + "; tr: " + this.tx);
      }

   }

   public void setTransform3D(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      if (var5 == 0.0 && var13 == 0.0 && var17 == 0.0 && var19 == 0.0 && var21 == 1.0 && var23 == 0.0) {
         this.setTransform(var1, var9, var3, var11, var7, var15);
      } else {
         throw new UnsupportedOperationException("3D transforms not supported.");
      }
   }

   public void transform(BaseTransform var1) {
      if (PrismSettings.debug) {
         System.out.println("+ concatTransform " + this + "; tr: " + var1);
      }

      this.tx.deriveWithConcatenation(var1);
   }

   public void translate(float var1, float var2) {
      if (PrismSettings.debug) {
         System.out.println("+ concat translate " + this + "; tx: " + var1 + "; ty: " + var2);
      }

      this.tx.deriveWithTranslation((double)var1, (double)var2);
   }

   public void translate(float var1, float var2, float var3) {
      throw new UnsupportedOperationException("translate3D: unimp");
   }

   public void scale(float var1, float var2) {
      if (PrismSettings.debug) {
         System.out.println("+ concat scale " + this + "; sx: " + var1 + "; sy: " + var2);
      }

      this.tx.deriveWithConcatenation((double)var1, 0.0, 0.0, (double)var2, 0.0, 0.0);
   }

   public void scale(float var1, float var2, float var3) {
      throw new UnsupportedOperationException("scale3D: unimp");
   }

   public void setCamera(NGCamera var1) {
   }

   public void setPerspectiveTransform(GeneralTransform3D var1) {
   }

   public NGCamera getCameraNoClone() {
      throw new UnsupportedOperationException("getCameraNoClone: unimp");
   }

   public void setDepthTest(boolean var1) {
   }

   public boolean isDepthTest() {
      return false;
   }

   public void setDepthBuffer(boolean var1) {
   }

   public boolean isDepthBuffer() {
      return false;
   }

   public boolean isAlphaTestShader() {
      if (PrismSettings.verbose && PrismSettings.forceAlphaTestShader) {
         System.out.println("SW pipe doesn't support shader with alpha testing");
      }

      return false;
   }

   public void setAntialiasedShape(boolean var1) {
      this.antialiasedShape = var1;
   }

   public boolean isAntialiasedShape() {
      return this.antialiasedShape;
   }

   public Rectangle getClipRect() {
      return this.clip == null ? null : new Rectangle(this.clip);
   }

   public Rectangle getClipRectNoClone() {
      return this.clip;
   }

   public RectBounds getFinalClipNoClone() {
      return this.finalClip.toRectBounds();
   }

   public void setClipRect(Rectangle var1) {
      this.finalClip.setBounds(this.target.getDimensions());
      if (var1 == null) {
         if (PrismSettings.debug) {
            System.out.println("+ PR.resetClip");
         }

         this.clip = null;
      } else {
         if (PrismSettings.debug) {
            System.out.println("+ PR.setClip: " + var1);
         }

         this.finalClip.intersectWith(var1);
         this.clip = new Rectangle(var1);
      }

      this.pr.setClip(this.finalClip.x, this.finalClip.y, this.finalClip.width, this.finalClip.height);
   }

   public void setHasPreCullingBits(boolean var1) {
      this.hasPreCullingBits = var1;
   }

   public boolean hasPreCullingBits() {
      return this.hasPreCullingBits;
   }

   public int getClipRectIndex() {
      return this.clipRectIndex;
   }

   public void setClipRectIndex(int var1) {
      if (PrismSettings.debug) {
         System.out.println("+ PR.setClipRectIndex: " + var1);
      }

      this.clipRectIndex = var1;
   }

   public float getExtraAlpha() {
      return this.swPaint.getCompositeAlpha();
   }

   public void setExtraAlpha(float var1) {
      if (PrismSettings.debug) {
         System.out.println("PR.setCompositeAlpha, value: " + var1);
      }

      this.swPaint.setCompositeAlpha(var1);
   }

   public Paint getPaint() {
      return this.paint;
   }

   public void setPaint(Paint var1) {
      this.paint = var1;
   }

   public BasicStroke getStroke() {
      return this.stroke;
   }

   public void setStroke(BasicStroke var1) {
      this.stroke = var1;
   }

   public CompositeMode getCompositeMode() {
      return this.compositeMode;
   }

   public void setCompositeMode(CompositeMode var1) {
      this.compositeMode = var1;
      byte var2;
      switch (var1) {
         case CLEAR:
            var2 = 0;
            if (PrismSettings.debug) {
               System.out.println("PR.setCompositeRule - CLEAR");
            }
            break;
         case SRC:
            var2 = 1;
            if (PrismSettings.debug) {
               System.out.println("PR.setCompositeRule - SRC");
            }
            break;
         case SRC_OVER:
            var2 = 2;
            if (PrismSettings.debug) {
               System.out.println("PR.setCompositeRule - SRC_OVER");
            }
            break;
         default:
            throw new InternalError("Unrecognized composite mode: " + var1);
      }

      this.pr.setCompositeRule(var2);
   }

   public void setNodeBounds(RectBounds var1) {
      if (PrismSettings.debug) {
         System.out.println("+ SWG.setNodeBounds: " + var1);
      }

      this.nodeBounds = var1;
   }

   public void clear() {
      this.clear(Color.TRANSPARENT);
   }

   public void clear(Color var1) {
      if (PrismSettings.debug) {
         System.out.println("+ PR.clear: " + var1);
      }

      this.swPaint.setColor(var1, 1.0F);
      this.pr.clearRect(0, 0, this.target.getPhysicalWidth(), this.target.getPhysicalHeight());
      this.getRenderTarget().setOpaque(var1.isOpaque());
   }

   public void clearQuad(float var1, float var2, float var3, float var4) {
      CompositeMode var5 = this.compositeMode;
      Paint var6 = this.paint;
      this.setCompositeMode(CompositeMode.SRC);
      this.setPaint(Color.TRANSPARENT);
      this.fillQuad(var1, var2, var3, var4);
      this.setCompositeMode(var5);
      this.setPaint(var6);
   }

   public void fill(Shape var1) {
      if (PrismSettings.debug) {
         System.out.println("+ fill(Shape)");
      }

      this.paintShape(var1, (BasicStroke)null, this.tx);
   }

   public void fillQuad(float var1, float var2, float var3, float var4) {
      if (PrismSettings.debug) {
         System.out.println("+ SWG.fillQuad");
      }

      this.fillRect(Math.min(var1, var3), Math.min(var2, var4), Math.abs(var3 - var1), Math.abs(var4 - var2));
   }

   public void fillRect(float var1, float var2, float var3, float var4) {
      if (PrismSettings.debug) {
         System.out.printf("+ SWG.fillRect, x: %f, y: %f, w: %f, h: %f\n", var1, var2, var3, var4);
      }

      if (this.tx.getMxy() == 0.0 && this.tx.getMyx() == 0.0) {
         if (PrismSettings.debug) {
            System.out.println("GR: " + this);
            System.out.println("target: " + this.target + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + this.target.getDimensions());
            System.out.println("Tx: " + this.tx);
            System.out.println("Clip: " + this.finalClip);
            System.out.println("Composite rule: " + this.compositeMode);
         }

         Point2D var5 = new Point2D(var1, var2);
         Point2D var6 = new Point2D(var1 + var3, var2 + var4);
         this.tx.transform(var5, var5);
         this.tx.transform(var6, var6);
         if (this.paint.getType() == Paint.Type.IMAGE_PATTERN) {
            ImagePattern var7 = (ImagePattern)this.paint;
            if (var7.getImage().getPixelFormat() == PixelFormat.BYTE_ALPHA) {
               throw new UnsupportedOperationException("Alpha image is not supported as an image pattern.");
            }

            Transform6 var8 = this.swPaint.computeSetTexturePaintTransform(this.paint, this.tx, this.nodeBounds, var1, var2, var3, var4);
            SWArgbPreTexture var9 = this.context.validateImagePaintTexture(var7.getImage().getWidth(), var7.getImage().getHeight());
            var9.update(var7.getImage());
            float var10 = this.swPaint.getCompositeAlpha();
            byte var11;
            if (var10 == 1.0F) {
               var11 = 1;
            } else {
               var11 = 2;
               this.pr.setColor(255, 255, 255, (int)(255.0F * var10));
            }

            this.pr.drawImage(1, var11, var9.getDataNoClone(), var9.getContentWidth(), var9.getContentHeight(), var9.getOffset(), var9.getPhysicalWidth(), var8, var9.getWrapMode() == Texture.WrapMode.REPEAT, var9.getLinearFiltering(), (int)(Math.min(var5.x, var6.x) * 65536.0F), (int)(Math.min(var5.y, var6.y) * 65536.0F), (int)(Math.abs(var6.x - var5.x) * 65536.0F), (int)(Math.abs(var6.y - var5.y) * 65536.0F), 0, 0, 0, 0, 0, 0, var9.getContentWidth() - 1, var9.getContentHeight() - 1, var9.hasAlpha());
         } else {
            this.swPaint.setPaintFromShape(this.paint, this.tx, (Shape)null, this.nodeBounds, var1, var2, var3, var4);
            this.pr.fillRect((int)(Math.min(var5.x, var6.x) * 65536.0F), (int)(Math.min(var5.y, var6.y) * 65536.0F), (int)(Math.abs(var6.x - var5.x) * 65536.0F), (int)(Math.abs(var6.y - var5.y) * 65536.0F));
         }
      } else {
         this.fillRoundRect(var1, var2, var3, var4, 0.0F, 0.0F);
      }

   }

   public void fillRoundRect(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (PrismSettings.debug) {
         System.out.println("+ SWG.fillRoundRect");
      }

      this.paintRoundRect(var1, var2, var3, var4, var5, var6, (BasicStroke)null);
   }

   public void fillEllipse(float var1, float var2, float var3, float var4) {
      if (PrismSettings.debug) {
         System.out.println("+ SWG.fillEllipse");
      }

      this.paintEllipse(var1, var2, var3, var4, (BasicStroke)null);
   }

   public void draw(Shape var1) {
      if (PrismSettings.debug) {
         System.out.println("+ draw(Shape)");
      }

      this.paintShape(var1, this.stroke, this.tx);
   }

   private void paintShape(Shape var1, BasicStroke var2, BaseTransform var3) {
      if (this.finalClip.isEmpty()) {
         if (PrismSettings.debug) {
            System.out.println("Final clip is empty: not rendering the shape: " + var1);
         }

      } else {
         this.swPaint.setPaintFromShape(this.paint, this.tx, var1, this.nodeBounds, 0.0F, 0.0F, 0.0F, 0.0F);
         this.paintShapePaintAlreadySet(var1, var2, var3);
      }
   }

   private void paintShapePaintAlreadySet(Shape var1, BasicStroke var2, BaseTransform var3) {
      if (this.finalClip.isEmpty()) {
         if (PrismSettings.debug) {
            System.out.println("Final clip is empty: not rendering the shape: " + var1);
         }

      } else {
         if (PrismSettings.debug) {
            System.out.println("GR: " + this);
            System.out.println("target: " + this.target + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + this.target.getDimensions());
            System.out.println("Shape: " + var1);
            System.out.println("Stroke: " + var2);
            System.out.println("Tx: " + var3);
            System.out.println("Clip: " + this.finalClip);
            System.out.println("Composite rule: " + this.compositeMode);
         }

         this.context.renderShape(this.pr, var1, var2, var3, this.finalClip, this.isAntialiasedShape());
      }
   }

   private void paintRoundRect(float var1, float var2, float var3, float var4, float var5, float var6, BasicStroke var7) {
      if (this.rect2d == null) {
         this.rect2d = new RoundRectangle2D(var1, var2, var3, var4, var5, var6);
      } else {
         this.rect2d.setRoundRect(var1, var2, var3, var4, var5, var6);
      }

      this.paintShape(this.rect2d, var7, this.tx);
   }

   private void paintEllipse(float var1, float var2, float var3, float var4, BasicStroke var5) {
      if (this.ellipse2d == null) {
         this.ellipse2d = new Ellipse2D(var1, var2, var3, var4);
      } else {
         this.ellipse2d.setFrame(var1, var2, var3, var4);
      }

      this.paintShape(this.ellipse2d, var5, this.tx);
   }

   public void drawLine(float var1, float var2, float var3, float var4) {
      if (PrismSettings.debug) {
         System.out.println("+ drawLine");
      }

      if (this.line2d == null) {
         this.line2d = new Line2D(var1, var2, var3, var4);
      } else {
         this.line2d.setLine(var1, var2, var3, var4);
      }

      this.paintShape(this.line2d, this.stroke, this.tx);
   }

   public void drawRect(float var1, float var2, float var3, float var4) {
      if (PrismSettings.debug) {
         System.out.println("+ SWG.drawRect");
      }

      this.drawRoundRect(var1, var2, var3, var4, 0.0F, 0.0F);
   }

   public void drawRoundRect(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (PrismSettings.debug) {
         System.out.println("+ SWG.drawRoundRect");
      }

      this.paintRoundRect(var1, var2, var3, var4, var5, var6, this.stroke);
   }

   public void drawEllipse(float var1, float var2, float var3, float var4) {
      if (PrismSettings.debug) {
         System.out.println("+ SWG.drawEllipse");
      }

      this.paintEllipse(var1, var2, var3, var4, this.stroke);
   }

   public void drawString(GlyphList var1, FontStrike var2, float var3, float var4, Color var5, int var6, int var7) {
      if (PrismSettings.debug) {
         System.out.println("+ SWG.drawGlyphList, gl.Count: " + var1.getGlyphCount() + ", x: " + var3 + ", y: " + var4 + ", selectStart: " + var6 + ", selectEnd: " + var7);
      }

      float var8;
      float var9;
      float var10;
      float var11;
      if (this.paint.isProportional()) {
         if (this.nodeBounds != null) {
            var8 = this.nodeBounds.getMinX();
            var9 = this.nodeBounds.getMinY();
            var10 = this.nodeBounds.getWidth();
            var11 = this.nodeBounds.getHeight();
         } else {
            Metrics var12 = var2.getMetrics();
            var8 = 0.0F;
            var9 = var12.getAscent();
            var10 = var1.getWidth();
            var11 = var12.getLineHeight();
         }
      } else {
         var11 = 0.0F;
         var10 = 0.0F;
         var9 = 0.0F;
         var8 = 0.0F;
      }

      boolean var18 = this.tx.isTranslateOrIdentity() && !var2.drawAsShapes();
      boolean var13 = var18 && var2.getAAMode() == 1 && this.getRenderTarget().isOpaque() && this.paint.getType() == Paint.Type.COLOR && this.tx.is2D();
      Affine2D var14 = null;
      if (var13) {
         this.pr.setLCDGammaCorrection(1.0F / PrismFontFactory.getLCDContrast());
      } else if (var18) {
         FontResource var15 = var2.getFontResource();
         float var16 = var2.getSize();
         BaseTransform var17 = var2.getTransform();
         var2 = var15.getStrike(var16, var17, 0);
      } else {
         var14 = new Affine2D();
      }

      int var19;
      if (var5 == null) {
         this.swPaint.setPaintBeforeDraw(this.paint, this.tx, var8, var9, var10, var11);

         for(var19 = 0; var19 < var1.getGlyphCount(); ++var19) {
            this.drawGlyph(var2, var1, var19, var14, var18, var3, var4);
         }
      } else {
         for(var19 = 0; var19 < var1.getGlyphCount(); ++var19) {
            int var20 = var1.getCharOffset(var19);
            boolean var21 = var6 <= var20 && var20 < var7;
            this.swPaint.setPaintBeforeDraw((Paint)(var21 ? var5 : this.paint), this.tx, var8, var9, var10, var11);
            this.drawGlyph(var2, var1, var19, var14, var18, var3, var4);
         }
      }

   }

   private void drawGlyph(FontStrike var1, GlyphList var2, int var3, BaseTransform var4, boolean var5, float var6, float var7) {
      Glyph var8 = var1.getGlyph(var2.getGlyphCode(var3));
      if (var5) {
         Point2D var9 = new Point2D((float)((double)var6 + this.tx.getMxt() + (double)var2.getPosX(var3)), (float)((double)var7 + this.tx.getMyt() + (double)var2.getPosY(var3)));
         int var10 = var1.getQuantizedPosition(var9);
         byte[] var11 = var8.getPixelData(var10);
         if (var11 != null) {
            int var12 = var8.getOriginX() + (int)var9.x;
            int var13 = var8.getOriginY() + (int)var9.y;
            if (var8.isLCDGlyph()) {
               this.pr.fillLCDAlphaMask(var11, var12, var13, var8.getWidth(), var8.getHeight(), 0, var8.getWidth());
            } else {
               this.pr.fillAlphaMask(var11, var12, var13, var8.getWidth(), var8.getHeight(), 0, var8.getWidth());
            }
         }
      } else {
         Shape var14 = var8.getShape();
         if (var14 != null) {
            var4.setTransform(this.tx);
            var4.deriveWithTranslation((double)(var6 + var2.getPosX(var3)), (double)(var7 + var2.getPosY(var3)));
            this.paintShapePaintAlreadySet(var14, (BasicStroke)null, var4);
         }
      }

   }

   public void drawTexture(Texture var1, float var2, float var3, float var4, float var5) {
      if (PrismSettings.debug) {
         System.out.printf("+ drawTexture1, x: %f, y: %f, w: %f, h: %f\n", var2, var3, var4, var5);
      }

      this.drawTexture(var1, var2, var3, var2 + var4, var3 + var5, 0.0F, 0.0F, var4, var5);
   }

   public void drawTexture(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      this.drawTexture(var1, var2, var3, var4, var5, var6, var7, var8, var9, 0, 0, 0, 0);
   }

   private void drawTexture(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int var11, int var12, int var13) {
      float var15 = this.swPaint.getCompositeAlpha();
      byte var14;
      if (var15 == 1.0F) {
         var14 = 1;
      } else {
         var14 = 2;
         this.pr.setColor(255, 255, 255, (int)(255.0F * var15));
      }

      this.drawTexture(var1, var14, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
   }

   private void drawTexture(Texture var1, int var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, int var11, int var12, int var13, int var14) {
      if (PrismSettings.debug) {
         System.out.println("+ drawTexture: " + var1 + ", imageMode: " + var2 + ", tex.w: " + var1.getPhysicalWidth() + ", tex.h: " + var1.getPhysicalHeight() + ", tex.cw: " + var1.getContentWidth() + ", tex.ch: " + var1.getContentHeight());
         System.out.println("target: " + this.target + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + this.target.getDimensions());
         System.out.println("GR: " + this);
         System.out.println("dx1:" + var3 + " dy1:" + var4 + " dx2:" + var5 + " dy2:" + var6);
         System.out.println("sx1:" + var7 + " sy1:" + var8 + " sx2:" + var9 + " sy2:" + var10);
         System.out.println("Clip: " + this.finalClip);
         System.out.println("Composite rule: " + this.compositeMode);
      }

      SWArgbPreTexture var15 = (SWArgbPreTexture)var1;
      int[] var16 = var15.getDataNoClone();
      RectBounds var17 = new RectBounds(Math.min(var3, var5), Math.min(var4, var6), Math.max(var3, var5), Math.max(var4, var6));
      RectBounds var18 = new RectBounds();
      this.tx.transform((BaseBounds)var17, (BaseBounds)var18);
      Transform6 var19 = this.swPaint.computeDrawTexturePaintTransform(this.tx, var3, var4, var5, var6, var7, var8, var9, var10);
      if (PrismSettings.debug) {
         System.out.println("tx: " + this.tx);
         System.out.println("piscesTx: " + var19);
         System.out.println("srcBBox: " + var17);
         System.out.println("dstBBox: " + var18);
      }

      int var20 = Math.max(0, SWUtils.fastFloor(Math.min(var7, var9)));
      int var21 = Math.max(0, SWUtils.fastFloor(Math.min(var8, var10)));
      int var22 = Math.min(var1.getContentWidth() - 1, SWUtils.fastCeil(Math.max(var7, var9)) - 1);
      int var23 = Math.min(var1.getContentHeight() - 1, SWUtils.fastCeil(Math.max(var8, var10)) - 1);
      this.pr.drawImage(1, var2, var16, var1.getContentWidth(), var1.getContentHeight(), var15.getOffset(), var1.getPhysicalWidth(), var19, var1.getWrapMode() == Texture.WrapMode.REPEAT, var1.getLinearFiltering(), (int)(65536.0F * var18.getMinX()), (int)(65536.0F * var18.getMinY()), (int)(65536.0F * var18.getWidth()), (int)(65536.0F * var18.getHeight()), var11, var12, var13, var14, var20, var21, var22, var23, var15.hasAlpha());
      if (PrismSettings.debug) {
         System.out.println("* drawTexture, DONE");
      }

   }

   public void drawTexture3SliceH(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      this.drawTexture(var1, var2, var3, var10, var5, var6, var7, var12, var9, 0, 1, 0, 0);
      this.drawTexture(var1, var10, var3, var11, var5, var12, var7, var13, var9, 2, 1, 0, 0);
      this.drawTexture(var1, var11, var3, var4, var5, var13, var7, var8, var9, 2, 0, 0, 0);
   }

   public void drawTexture3SliceV(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      this.drawTexture(var1, var2, var3, var4, var10, var6, var7, var8, var12, 0, 0, 0, 1);
      this.drawTexture(var1, var2, var10, var4, var11, var6, var12, var8, var13, 0, 0, 2, 1);
      this.drawTexture(var1, var2, var11, var4, var5, var6, var13, var8, var9, 0, 0, 2, 0);
   }

   public void drawTexture9Slice(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17) {
      this.drawTexture(var1, var2, var3, var10, var11, var6, var7, var14, var15, 0, 1, 0, 1);
      this.drawTexture(var1, var10, var3, var12, var11, var14, var7, var16, var15, 2, 1, 0, 1);
      this.drawTexture(var1, var12, var3, var4, var11, var16, var7, var8, var15, 2, 0, 0, 1);
      this.drawTexture(var1, var2, var11, var10, var13, var6, var15, var14, var17, 0, 1, 2, 1);
      this.drawTexture(var1, var10, var11, var12, var13, var14, var15, var16, var17, 2, 1, 2, 1);
      this.drawTexture(var1, var12, var11, var4, var13, var16, var15, var8, var17, 2, 0, 2, 1);
      this.drawTexture(var1, var2, var13, var10, var5, var6, var17, var14, var9, 0, 1, 2, 0);
      this.drawTexture(var1, var10, var13, var12, var5, var14, var17, var16, var9, 2, 1, 2, 0);
      this.drawTexture(var1, var12, var13, var4, var5, var16, var17, var8, var9, 2, 0, 2, 0);
   }

   public void drawTextureVO(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11) {
      if (PrismSettings.debug) {
         System.out.println("* drawTextureVO");
      }

      int[] var12 = new int[]{0, 65536};
      int[] var13 = new int[]{16777215 | (int)(var2 * 255.0F) << 24, 16777215 | (int)(var3 * 255.0F) << 24};
      Transform6 var14 = new Transform6();
      SWUtils.convertToPiscesTransform(this.tx, var14);
      this.pr.setLinearGradient(0, (int)(65536.0F * var5), 0, (int)(65536.0F * var7), var12, var13, 0, var14);
      this.drawTexture(var1, 2, var4, var5, var6, var7, var8, var9, var10, var11, 0, 0, 0, 0);
   }

   public void drawTextureRaw(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      if (PrismSettings.debug) {
         System.out.println("+ drawTextureRaw");
      }

      int var10 = var1.getContentWidth();
      int var11 = var1.getContentHeight();
      var6 *= (float)var10;
      var7 *= (float)var11;
      var8 *= (float)var10;
      var9 *= (float)var11;
      this.drawTexture(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public void drawMappedTextureRaw(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      if (PrismSettings.debug) {
         System.out.println("+ drawMappedTextureRaw");
      }

      double var14 = this.tx.getMxx();
      double var16 = this.tx.getMyx();
      double var18 = this.tx.getMxy();
      double var20 = this.tx.getMyy();
      double var22 = this.tx.getMxt();
      double var24 = this.tx.getMyt();

      try {
         float var26 = var8 - var6;
         float var27 = var9 - var7;
         float var28 = var10 - var6;
         float var29 = var11 - var7;
         Affine2D var30 = new Affine2D(var26, var27, var28, var29, var6, var7);
         var30.invert();
         this.tx.setToIdentity();
         this.tx.deriveWithTranslation((double)var2, (double)var3);
         this.tx.deriveWithConcatenation((double)(var4 - var2), 0.0, 0.0, (double)(var5 - var5), 0.0, 0.0);
         this.tx.deriveWithConcatenation(var30);
         this.drawTexture(var1, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, (float)var1.getContentWidth(), (float)var1.getContentHeight());
      } catch (NoninvertibleTransformException var31) {
      }

      this.tx.restoreTransform(var14, var16, var18, var20, var22, var24);
   }

   public boolean canReadBack() {
      return true;
   }

   public RTTexture readBack(Rectangle var1) {
      if (PrismSettings.debug) {
         System.out.println("+ readBack, rect: " + var1 + ", target.dims: " + this.target.getDimensions());
      }

      int var2 = Math.max(1, var1.width);
      int var3 = Math.max(1, var1.height);
      SWRTTexture var4 = this.context.validateRBBuffer(var2, var3);
      if (var1.isEmpty()) {
         return var4;
      } else {
         int[] var5 = var4.getDataNoClone();
         this.target.getSurface().getRGB(var5, 0, var4.getPhysicalWidth(), var1.x, var1.y, var2, var3);
         return var4;
      }
   }

   public void releaseReadBackBuffer(RTTexture var1) {
   }

   public void setState3D(boolean var1) {
   }

   public boolean isState3D() {
      return false;
   }

   public void setup3DRendering() {
   }

   public void setPixelScaleFactors(float var1, float var2) {
      this.pixelScaleX = var1;
   }

   public float getPixelScaleFactorX() {
      return this.pixelScaleX;
   }

   public float getPixelScaleFactorY() {
      return this.pixelScaleY;
   }

   public void setLights(NGLightBase[] var1) {
   }

   public NGLightBase[] getLights() {
      return null;
   }

   public void blit(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      Graphics var11 = var2.createGraphics();
      var11.drawTexture(var1, (float)var7, (float)var8, (float)var9, (float)var10, (float)var3, (float)var4, (float)var5, (float)var6);
   }

   static {
      DEFAULT_PAINT = Color.WHITE;
   }
}
