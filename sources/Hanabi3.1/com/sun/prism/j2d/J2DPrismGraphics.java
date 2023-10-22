package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.font.CompositeStrike;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.j2d.paint.RadialGradientPaint;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class J2DPrismGraphics implements ReadbackGraphics, MaskTextureGraphics {
   static final MultipleGradientPaint.CycleMethod[] LGP_CYCLE_METHODS;
   static final com.sun.prism.j2d.paint.MultipleGradientPaint.CycleMethod[] RGP_CYCLE_METHODS;
   private static final BasicStroke DEFAULT_STROKE;
   private static final Paint DEFAULT_PAINT;
   static AffineTransform J2D_IDENTITY;
   private int clipRectIndex;
   private boolean hasPreCullingBits;
   private float pixelScaleX;
   private float pixelScaleY;
   private static ConcurrentHashMap fontMap;
   private static volatile int cleared;
   private static AffineTransform tmpAT;
   private static Path2D tmpQuadShape;
   private static Rectangle2D.Float tmpRect;
   private static Ellipse2D tmpEllipse;
   private static RoundRectangle2D tmpRRect;
   private static Line2D tmpLine;
   private static AdaptorShape tmpAdaptor;
   private boolean antialiasedShape;
   J2DPresentable target;
   Graphics2D g2d;
   Affine2D transform;
   Rectangle clipRect;
   RectBounds devClipRect;
   RectBounds finalClipRect;
   Paint paint;
   boolean paintWasProportional;
   BasicStroke stroke;
   boolean cull;
   Rectangle2D nodeBounds;
   private NodePath renderRoot;

   static Color toJ2DColor(com.sun.prism.paint.Color var0) {
      return new Color(var0.getRed(), var0.getGreen(), var0.getBlue(), var0.getAlpha());
   }

   static int fixFractions(float[] var0, Color[] var1) {
      float var2 = var0[0];
      int var3 = 1;

      int var4;
      Color var6;
      for(var4 = 1; var3 < var0.length; var1[var4++] = var6) {
         float var5 = var0[var3];
         var6 = var1[var3++];
         if (var5 <= var2) {
            if (var5 >= 1.0F) {
               break;
            }

            for(var5 = var2 + Math.ulp(var2); var3 < var0.length && !(var0[var3] > var5); var6 = var1[var3++]) {
            }
         }

         var2 = var5;
         var0[var4] = var5;
      }

      return var4;
   }

   java.awt.Paint toJ2DPaint(Paint var1, Rectangle2D var2) {
      if (var1 instanceof com.sun.prism.paint.Color) {
         return toJ2DColor((com.sun.prism.paint.Color)var1);
      } else {
         float var8;
         float var38;
         if (var1 instanceof Gradient) {
            Gradient var3 = (Gradient)var1;
            if (var3.isProportional() && var2 == null) {
               return null;
            }

            List var4 = var3.getStops();
            int var5 = var4.size();
            float[] var6 = new float[var5];
            Color[] var7 = new Color[var5];
            var8 = -1.0F;
            boolean var9 = false;

            float var12;
            for(int var10 = 0; var10 < var5; ++var10) {
               Stop var11 = (Stop)var4.get(var10);
               var12 = var11.getOffset();
               var9 = var9 || var12 <= var8;
               var8 = var12;
               var6[var10] = var12;
               var7[var10] = toJ2DColor(var11.getColor());
            }

            if (var9) {
               var5 = fixFractions(var6, var7);
               if (var5 < var6.length) {
                  float[] var33 = new float[var5];
                  System.arraycopy(var6, 0, var33, 0, var5);
                  var6 = var33;
                  Color[] var35 = new Color[var5];
                  System.arraycopy(var7, 0, var35, 0, var5);
                  var7 = var35;
               }
            }

            float var13;
            float var16;
            float var18;
            if (var3 instanceof LinearGradient) {
               LinearGradient var36 = (LinearGradient)var1;
               var38 = var36.getX1();
               var12 = var36.getY1();
               var13 = var36.getX2();
               float var39 = var36.getY2();
               if (var3.isProportional()) {
                  float var15 = (float)var2.getX();
                  var16 = (float)var2.getY();
                  float var42 = (float)var2.getWidth();
                  var18 = (float)var2.getHeight();
                  var38 = var15 + var42 * var38;
                  var12 = var16 + var18 * var12;
                  var13 = var15 + var42 * var13;
                  var39 = var16 + var18 * var39;
               }

               if (var38 == var13 && var12 == var12) {
                  return var7[0];
               }

               Point2D.Float var40 = new Point2D.Float(var38, var12);
               Point2D.Float var41 = new Point2D.Float(var13, var39);
               MultipleGradientPaint.CycleMethod var43 = LGP_CYCLE_METHODS[var3.getSpreadMethod()];
               return new LinearGradientPaint(var40, var41, var6, var7, var43);
            }

            if (var3 instanceof RadialGradient) {
               RadialGradient var34 = (RadialGradient)var3;
               var38 = var34.getCenterX();
               var12 = var34.getCenterY();
               var13 = var34.getRadius();
               double var14 = Math.toRadians((double)var34.getFocusAngle());
               var16 = var34.getFocusDistance();
               AffineTransform var17 = J2D_IDENTITY;
               float var19;
               float var20;
               if (var3.isProportional()) {
                  var18 = (float)var2.getX();
                  var19 = (float)var2.getY();
                  var20 = (float)var2.getWidth();
                  float var21 = (float)var2.getHeight();
                  float var22 = Math.min(var20, var21);
                  float var23 = var18 + var20 * 0.5F;
                  float var24 = var19 + var21 * 0.5F;
                  var38 = var23 + (var38 - 0.5F) * var22;
                  var12 = var24 + (var12 - 0.5F) * var22;
                  var13 *= var22;
                  if (var20 != var21 && (double)var20 != 0.0 && (double)var21 != 0.0) {
                     var17 = AffineTransform.getTranslateInstance((double)var23, (double)var24);
                     var17.scale((double)(var20 / var22), (double)(var21 / var22));
                     var17.translate((double)(-var23), (double)(-var24));
                  }
               }

               Point2D.Float var44 = new Point2D.Float(var38, var12);
               var19 = (float)((double)var38 + (double)(var16 * var13) * Math.cos(var14));
               var20 = (float)((double)var12 + (double)(var16 * var13) * Math.sin(var14));
               Point2D.Float var45 = new Point2D.Float(var19, var20);
               com.sun.prism.j2d.paint.MultipleGradientPaint.CycleMethod var46 = RGP_CYCLE_METHODS[var3.getSpreadMethod()];
               return new RadialGradientPaint(var44, var13, var45, var6, var7, var46, com.sun.prism.j2d.paint.MultipleGradientPaint.ColorSpaceType.SRGB, var17);
            }
         } else if (var1 instanceof ImagePattern) {
            ImagePattern var25 = (ImagePattern)var1;
            float var26 = var25.getX();
            float var27 = var25.getY();
            float var28 = var25.getWidth();
            float var29 = var25.getHeight();
            if (var1.isProportional()) {
               if (var2 == null) {
                  return null;
               }

               var8 = (float)var2.getX();
               float var31 = (float)var2.getY();
               float var37 = (float)var2.getWidth();
               var38 = (float)var2.getHeight();
               var28 += var26;
               var29 += var27;
               var26 = var8 + var26 * var37;
               var27 = var31 + var27 * var38;
               var28 = var8 + var28 * var37;
               var29 = var31 + var29 * var38;
               var28 -= var26;
               var29 -= var27;
            }

            Texture var30 = this.getResourceFactory().getCachedTexture(var25.getImage(), Texture.WrapMode.REPEAT);
            BufferedImage var32 = ((J2DTexture)var30).getBufferedImage();
            var30.unlock();
            return new TexturePaint(var32, tmpRect(var26, var27, var28, var29));
         }

         throw new UnsupportedOperationException("Paint " + var1 + " not supported yet.");
      }
   }

   static Stroke toJ2DStroke(BasicStroke var0) {
      float var1 = var0.getLineWidth();
      int var2 = var0.getType();
      if (var2 != 0) {
         var1 *= 2.0F;
      }

      java.awt.BasicStroke var3 = new java.awt.BasicStroke(var1, var0.getEndCap(), var0.getLineJoin(), var0.getMiterLimit(), var0.getDashArray(), var0.getDashPhase());
      if (var2 == 1) {
         return new InnerStroke(var3);
      } else {
         return (Stroke)(var2 == 2 ? new OuterStroke(var3) : var3);
      }
   }

   private static Font toJ2DFont(FontStrike var0) {
      FontResource var1 = var0.getFontResource();
      Object var3 = var1.getPeer();
      if (var3 == null && var1.isEmbeddedFont()) {
         J2DFontFactory.registerFont(var1);
         var3 = var1.getPeer();
      }

      Font var2;
      if (var3 != null && var3 instanceof Font) {
         var2 = (Font)var3;
      } else {
         if (PlatformUtil.isMac()) {
            String var4 = var1.getPSName();
            var2 = new Font(var4, 0, 12);
            if (!var2.getPSName().equals(var4)) {
               int var5 = var1.isBold() ? 1 : 0;
               var5 |= var1.isItalic() ? 2 : 0;
               var2 = new Font(var1.getFamilyName(), var5, 12);
               if (!var2.getPSName().equals(var4)) {
                  Font[] var6 = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
                  Font[] var7 = var6;
                  int var8 = var6.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     Font var10 = var7[var9];
                     if (var10.getPSName().equals(var4)) {
                        var2 = var10;
                        break;
                     }
                  }
               }
            }
         } else {
            var2 = new Font(var1.getFullName(), 0, 12);
         }

         var1.setPeer(var2);
      }

      var2 = var2.deriveFont(var0.getSize());
      Font var11 = null;
      WeakReference var12 = (WeakReference)fontMap.get(var2);
      if (var12 != null) {
         var11 = (Font)var12.get();
         if (var11 == null) {
            ++cleared;
         }
      }

      if (var11 == null) {
         if (fontMap.size() > 100 && cleared > 10) {
            Iterator var13 = fontMap.keySet().iterator();

            label62:
            while(true) {
               Font var14;
               do {
                  if (!var13.hasNext()) {
                     cleared = 0;
                     break label62;
                  }

                  var14 = (Font)var13.next();
                  var12 = (WeakReference)fontMap.get(var14);
               } while(var12 != null && var12.get() != null);

               fontMap.remove(var14);
            }
         }

         var11 = J2DFontFactory.getCompositeFont(var2);
         var12 = new WeakReference(var11);
         fontMap.put(var2, var12);
      }

      return var11;
   }

   public static AffineTransform toJ2DTransform(BaseTransform var0) {
      return new AffineTransform(var0.getMxx(), var0.getMyx(), var0.getMxy(), var0.getMyy(), var0.getMxt(), var0.getMyt());
   }

   static AffineTransform tmpJ2DTransform(BaseTransform var0) {
      tmpAT.setTransform(var0.getMxx(), var0.getMyx(), var0.getMxy(), var0.getMyy(), var0.getMxt(), var0.getMyt());
      return tmpAT;
   }

   static BaseTransform toPrTransform(AffineTransform var0) {
      return BaseTransform.getInstance(var0.getScaleX(), var0.getShearY(), var0.getShearX(), var0.getScaleY(), var0.getTranslateX(), var0.getTranslateY());
   }

   static Rectangle toPrRect(java.awt.Rectangle var0) {
      return new Rectangle(var0.x, var0.y, var0.width, var0.height);
   }

   private static Shape tmpQuad(float var0, float var1, float var2, float var3) {
      tmpQuadShape.reset();
      tmpQuadShape.moveTo((double)var0, (double)var1);
      tmpQuadShape.lineTo((double)var2, (double)var1);
      tmpQuadShape.lineTo((double)var2, (double)var3);
      tmpQuadShape.lineTo((double)var0, (double)var3);
      tmpQuadShape.closePath();
      return tmpQuadShape;
   }

   private static Rectangle2D tmpRect(float var0, float var1, float var2, float var3) {
      tmpRect.setRect(var0, var1, var2, var3);
      return tmpRect;
   }

   private static Shape tmpEllipse(float var0, float var1, float var2, float var3) {
      tmpEllipse.setFrame((double)var0, (double)var1, (double)var2, (double)var3);
      return tmpEllipse;
   }

   private static Shape tmpRRect(float var0, float var1, float var2, float var3, float var4, float var5) {
      tmpRRect.setRoundRect((double)var0, (double)var1, (double)var2, (double)var3, (double)var4, (double)var5);
      return tmpRRect;
   }

   private static Shape tmpLine(float var0, float var1, float var2, float var3) {
      tmpLine.setLine((double)var0, (double)var1, (double)var2, (double)var3);
      return tmpLine;
   }

   private static Shape tmpShape(com.sun.javafx.geom.Shape var0) {
      tmpAdaptor.setShape(var0);
      return tmpAdaptor;
   }

   J2DPrismGraphics(J2DPresentable var1, Graphics2D var2) {
      this(var2, var1.getContentWidth(), var1.getContentHeight());
      this.target = var1;
   }

   J2DPrismGraphics(Graphics2D var1, int var2, int var3) {
      this.hasPreCullingBits = false;
      this.pixelScaleX = 1.0F;
      this.pixelScaleY = 1.0F;
      this.antialiasedShape = true;
      this.nodeBounds = null;
      this.g2d = var1;
      this.captureTransform(var1);
      this.transform = new Affine2D();
      this.devClipRect = new RectBounds(0.0F, 0.0F, (float)var2, (float)var3);
      this.finalClipRect = new RectBounds(0.0F, 0.0F, (float)var2, (float)var3);
      this.cull = true;
      var1.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      var1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      var1.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      var1.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      var1.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      this.setTransform(BaseTransform.IDENTITY_TRANSFORM);
      this.setPaint(DEFAULT_PAINT);
      this.setStroke(DEFAULT_STROKE);
   }

   public RenderTarget getRenderTarget() {
      return this.target;
   }

   public Screen getAssociatedScreen() {
      return this.target.getAssociatedScreen();
   }

   public ResourceFactory getResourceFactory() {
      return this.target.getResourceFactory();
   }

   public void reset() {
   }

   public Rectangle getClipRect() {
      return this.clipRect == null ? null : new Rectangle(this.clipRect);
   }

   public Rectangle getClipRectNoClone() {
      return this.clipRect;
   }

   public RectBounds getFinalClipNoClone() {
      return this.finalClipRect;
   }

   public void setClipRect(Rectangle var1) {
      this.finalClipRect.setBounds(this.devClipRect);
      if (var1 == null) {
         this.clipRect = null;
         this.g2d.setClip((Shape)null);
      } else {
         this.clipRect = new Rectangle(var1);
         this.finalClipRect.intersectWith(var1);
         this.setTransformG2D(J2D_IDENTITY);
         this.g2d.setClip(var1.x, var1.y, var1.width, var1.height);
         this.setTransformG2D(tmpJ2DTransform(this.transform));
      }

   }

   private AlphaComposite getAWTComposite() {
      return (AlphaComposite)this.g2d.getComposite();
   }

   public float getExtraAlpha() {
      return this.getAWTComposite().getAlpha();
   }

   public void setExtraAlpha(float var1) {
      this.g2d.setComposite(this.getAWTComposite().derive(var1));
   }

   public CompositeMode getCompositeMode() {
      int var1 = this.getAWTComposite().getRule();
      switch (var1) {
         case 1:
            return CompositeMode.CLEAR;
         case 2:
            return CompositeMode.SRC;
         case 3:
            return CompositeMode.SRC_OVER;
         default:
            throw new InternalError("Unrecognized AlphaCompsite rule: " + var1);
      }
   }

   public void setCompositeMode(CompositeMode var1) {
      AlphaComposite var2 = this.getAWTComposite();
      switch (var1) {
         case CLEAR:
            var2 = var2.derive(1);
            break;
         case SRC:
            var2 = var2.derive(2);
            break;
         case SRC_OVER:
            var2 = var2.derive(3);
            break;
         default:
            throw new InternalError("Unrecognized composite mode: " + var1);
      }

      this.g2d.setComposite(var2);
   }

   public Paint getPaint() {
      return this.paint;
   }

   public void setPaint(Paint var1) {
      this.paint = var1;
      java.awt.Paint var2 = this.toJ2DPaint(var1, (Rectangle2D)null);
      if (var2 == null) {
         this.paintWasProportional = true;
      } else {
         this.paintWasProportional = false;
         this.g2d.setPaint(var2);
      }

   }

   public BasicStroke getStroke() {
      return this.stroke;
   }

   public void setStroke(BasicStroke var1) {
      this.stroke = var1;
      this.g2d.setStroke(toJ2DStroke(var1));
   }

   public BaseTransform getTransformNoClone() {
      return this.transform;
   }

   public void translate(float var1, float var2) {
      this.transform.translate((double)var1, (double)var2);
      this.g2d.translate((double)var1, (double)var2);
   }

   public void scale(float var1, float var2) {
      this.transform.scale((double)var1, (double)var2);
      this.g2d.scale((double)var1, (double)var2);
   }

   public void transform(BaseTransform var1) {
      if (var1.is2D()) {
         this.transform.concatenate(var1);
         this.setTransformG2D(tmpJ2DTransform(this.transform));
      }
   }

   public void setTransform(BaseTransform var1) {
      if (var1 == null) {
         var1 = BaseTransform.IDENTITY_TRANSFORM;
      }

      this.transform.setTransform(var1);
      this.setTransformG2D(tmpJ2DTransform(this.transform));
   }

   public void setTransform(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.transform.setTransform(var1, var3, var5, var7, var9, var11);
      this.setTransformG2D(tmpJ2DTransform(this.transform));
   }

   public void clear() {
      this.clear(com.sun.prism.paint.Color.TRANSPARENT);
   }

   public void clear(com.sun.prism.paint.Color var1) {
      this.getRenderTarget().setOpaque(var1.isOpaque());
      this.clear(toJ2DColor(var1));
   }

   void clear(Color var1) {
      Graphics2D var2 = (Graphics2D)this.g2d.create();
      var2.setTransform(J2D_IDENTITY);
      var2.setComposite(AlphaComposite.Src);
      var2.setColor(var1);
      var2.fillRect(0, 0, this.target.getContentWidth(), this.target.getContentHeight());
      var2.dispose();
   }

   public void clearQuad(float var1, float var2, float var3, float var4) {
      this.g2d.setComposite(AlphaComposite.Clear);
      this.g2d.fill(tmpQuad(var1, var2, var3, var4));
   }

   void fill(Shape var1) {
      if (this.paintWasProportional) {
         if (this.nodeBounds != null) {
            this.g2d.setPaint(this.toJ2DPaint(this.paint, this.nodeBounds));
         } else {
            this.g2d.setPaint(this.toJ2DPaint(this.paint, var1.getBounds2D()));
         }
      }

      if (this.paint.getType() == Paint.Type.IMAGE_PATTERN) {
         ImagePattern var2 = (ImagePattern)this.paint;
         AffineTransform var3 = toJ2DTransform(var2.getPatternTransformNoClone());
         if (!var3.isIdentity()) {
            this.g2d.setClip(var1);
            this.g2d.transform(var3);
            tmpAT.setTransform(var3);

            try {
               tmpAT.invert();
               this.g2d.fill(tmpAT.createTransformedShape(var1));
            } catch (NoninvertibleTransformException var5) {
            }

            this.setTransform(this.transform);
            this.setClipRect(this.clipRect);
            return;
         }
      }

      this.g2d.fill(var1);
   }

   public void fill(com.sun.javafx.geom.Shape var1) {
      this.fill(tmpShape(var1));
   }

   public void fillRect(float var1, float var2, float var3, float var4) {
      this.fill((Shape)tmpRect(var1, var2, var3, var4));
   }

   public void fillRoundRect(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.fill(tmpRRect(var1, var2, var3, var4, var5, var6));
   }

   public void fillEllipse(float var1, float var2, float var3, float var4) {
      this.fill(tmpEllipse(var1, var2, var3, var4));
   }

   public void fillQuad(float var1, float var2, float var3, float var4) {
      this.fill(tmpQuad(var1, var2, var3, var4));
   }

   void draw(Shape var1) {
      if (this.paintWasProportional) {
         if (this.nodeBounds != null) {
            this.g2d.setPaint(this.toJ2DPaint(this.paint, this.nodeBounds));
         } else {
            this.g2d.setPaint(this.toJ2DPaint(this.paint, var1.getBounds2D()));
         }
      }

      try {
         this.g2d.draw(var1);
      } catch (Throwable var3) {
      }

   }

   public void draw(com.sun.javafx.geom.Shape var1) {
      this.draw(tmpShape(var1));
   }

   public void drawLine(float var1, float var2, float var3, float var4) {
      this.draw(tmpLine(var1, var2, var3, var4));
   }

   public void drawRect(float var1, float var2, float var3, float var4) {
      this.draw((Shape)tmpRect(var1, var2, var3, var4));
   }

   public void drawRoundRect(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.draw(tmpRRect(var1, var2, var3, var4, var5, var6));
   }

   public void drawEllipse(float var1, float var2, float var3, float var4) {
      this.draw(tmpEllipse(var1, var2, var3, var4));
   }

   public void setNodeBounds(RectBounds var1) {
      this.nodeBounds = var1 != null ? new Rectangle2D.Float(var1.getMinX(), var1.getMinY(), var1.getWidth(), var1.getHeight()) : null;
   }

   private void drawString(GlyphList var1, int var2, int var3, FontStrike var4, float var5, float var6) {
      if (var2 != var3) {
         int var7 = var3 - var2;
         int[] var8 = new int[var7];

         for(int var9 = 0; var9 < var7; ++var9) {
            var8[var9] = var1.getGlyphCode(var2 + var9) & 16777215;
         }

         Font var13 = toJ2DFont(var4);
         GlyphVector var10 = var13.createGlyphVector(this.g2d.getFontRenderContext(), var8);
         Point2D.Float var11 = new Point2D.Float();

         for(int var12 = 0; var12 < var7; ++var12) {
            var11.setLocation((double)var1.getPosX(var2 + var12), (double)var1.getPosY(var2 + var12));
            var10.setGlyphPosition(var12, var11);
         }

         this.g2d.drawGlyphVector(var10, var5, var6);
      }
   }

   public void drawString(GlyphList var1, FontStrike var2, float var3, float var4, com.sun.prism.paint.Color var5, int var6, int var7) {
      int var8 = var1.getGlyphCount();
      if (var8 != 0) {
         this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
         if (var2.getAAMode() == 1) {
            this.g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
         }

         if (this.paintWasProportional) {
            Object var9 = this.nodeBounds;
            if (var9 == null) {
               Metrics var10 = var2.getMetrics();
               var9 = new Rectangle2D.Float(0.0F, var10.getAscent(), var1.getWidth(), var10.getLineHeight());
            }

            this.g2d.setPaint(this.toJ2DPaint(this.paint, (Rectangle2D)var9));
         }

         CompositeStrike var18 = null;
         int var19 = 0;
         if (var2 instanceof CompositeStrike) {
            var18 = (CompositeStrike)var2;
            int var11 = var1.getGlyphCode(0);
            var19 = var18.getStrikeSlotForGlyph(var11);
         }

         Color var20 = null;
         Color var12 = null;
         boolean var13 = false;
         int var14;
         if (var5 != null) {
            var20 = toJ2DColor(var5);
            var12 = this.g2d.getColor();
            var14 = var1.getCharOffset(0);
            var13 = var6 <= var14 && var14 < var7;
         }

         var14 = 0;
         if (var20 != null || var18 != null) {
            for(int var15 = 1; var15 < var8; ++var15) {
               int var16;
               if (var20 != null) {
                  var16 = var1.getCharOffset(var15);
                  boolean var17 = var6 <= var16 && var16 < var7;
                  if (var13 != var17) {
                     if (var18 != null) {
                        var2 = var18.getStrikeSlot(var19);
                     }

                     this.g2d.setColor(var13 ? var20 : var12);
                     this.drawString(var1, var14, var15, var2, var3, var4);
                     var14 = var15;
                     var13 = var17;
                  }
               }

               if (var18 != null) {
                  var16 = var1.getGlyphCode(var15);
                  int var21 = var18.getStrikeSlotForGlyph(var16);
                  if (var19 != var21) {
                     var2 = var18.getStrikeSlot(var19);
                     if (var20 != null) {
                        this.g2d.setColor(var13 ? var20 : var12);
                     }

                     this.drawString(var1, var14, var15, var2, var3, var4);
                     var14 = var15;
                     var19 = var21;
                  }
               }
            }

            if (var18 != null) {
               var2 = var18.getStrikeSlot(var19);
            }

            if (var20 != null) {
               this.g2d.setColor(var13 ? var20 : var12);
            }
         }

         this.drawString(var1, var14, var8, var2, var3, var4);
         if (var5 != null) {
            this.g2d.setColor(var12);
         }

         this.g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
         this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      }
   }

   protected void setTransformG2D(AffineTransform var1) {
      this.g2d.setTransform(var1);
   }

   protected void captureTransform(Graphics2D var1) {
   }

   public void drawMappedTextureRaw(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      BufferedImage var14 = ((J2DTexture)var1).getBufferedImage();
      float var15 = var8 - var6;
      float var16 = var9 - var7;
      float var17 = var10 - var6;
      float var18 = var11 - var7;
      this.setTransformG2D(J2D_IDENTITY);
      tmpAT.setTransform((double)var15, (double)var16, (double)var17, (double)var18, (double)var6, (double)var7);

      try {
         tmpAT.invert();
         this.g2d.translate((double)var2, (double)var3);
         this.g2d.scale((double)(var4 - var2), (double)(var5 - var3));
         this.g2d.transform(tmpAT);
         this.g2d.drawImage(var14, 0, 0, 1, 1, (ImageObserver)null);
      } catch (NoninvertibleTransformException var20) {
      }

      this.setTransform(this.transform);
   }

   public void drawTexture(Texture var1, float var2, float var3, float var4, float var5) {
      BufferedImage var6 = ((J2DTexture)var1).getBufferedImage();
      this.g2d.drawImage(var6, (int)var2, (int)var3, (int)(var2 + var4), (int)(var3 + var5), 0, 0, (int)var4, (int)var5, (ImageObserver)null);
   }

   public void drawTexture(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      BufferedImage var10 = ((J2DTexture)var1).getBufferedImage();
      this.g2d.drawImage(var10, (int)var2, (int)var3, (int)var4, (int)var5, (int)var6, (int)var7, (int)var8, (int)var9, (ImageObserver)null);
   }

   public void drawTexture3SliceH(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      if (var12 + 0.1F > var13) {
         ++var13;
      }

      this.drawTexture(var1, var2, var3, var10, var5, var6, var7, var12, var9);
      this.drawTexture(var1, var10, var3, var11, var5, var12, var7, var13, var9);
      this.drawTexture(var1, var11, var3, var4, var5, var13, var7, var8, var9);
   }

   public void drawTexture3SliceV(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      if (var12 + 0.1F > var13) {
         ++var13;
      }

      this.drawTexture(var1, var2, var3, var4, var10, var6, var7, var8, var12);
      this.drawTexture(var1, var2, var10, var4, var11, var6, var12, var8, var13);
      this.drawTexture(var1, var2, var11, var4, var5, var6, var13, var8, var9);
   }

   public void drawTexture9Slice(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17) {
      if (var14 + 0.1F > var16) {
         ++var16;
      }

      if (var15 + 0.1F > var17) {
         ++var17;
      }

      this.drawTexture(var1, var2, var3, var10, var11, var6, var7, var14, var15);
      this.drawTexture(var1, var10, var3, var12, var11, var14, var7, var16, var15);
      this.drawTexture(var1, var12, var3, var4, var11, var16, var7, var8, var15);
      this.drawTexture(var1, var2, var11, var10, var13, var6, var15, var14, var17);
      this.drawTexture(var1, var10, var11, var12, var13, var14, var15, var16, var17);
      this.drawTexture(var1, var12, var11, var4, var13, var16, var15, var8, var17);
      this.drawTexture(var1, var2, var13, var10, var5, var6, var17, var14, var9);
      this.drawTexture(var1, var10, var13, var12, var5, var14, var17, var16, var9);
      this.drawTexture(var1, var12, var13, var4, var5, var16, var17, var8, var9);
   }

   public void drawTextureRaw(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      int var10 = var1.getContentWidth();
      int var11 = var1.getContentHeight();
      var6 *= (float)var10;
      var7 *= (float)var11;
      var8 *= (float)var10;
      var9 *= (float)var11;
      this.drawTexture(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public void drawTextureVO(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11) {
      java.awt.Paint var12 = this.g2d.getPaint();
      Composite var13 = this.g2d.getComposite();
      Color var14 = new Color(1.0F, 1.0F, 1.0F, var2);
      Color var15 = new Color(1.0F, 1.0F, 1.0F, var3);
      this.g2d.setPaint(new GradientPaint(0.0F, var5, var14, 0.0F, var7, var15, true));
      this.g2d.setComposite(AlphaComposite.Src);
      int var16 = (int)Math.floor((double)Math.min(var4, var6));
      int var17 = (int)Math.floor((double)Math.min(var5, var7));
      int var18 = (int)Math.ceil((double)Math.max(var4, var6)) - var16;
      int var19 = (int)Math.ceil((double)Math.max(var5, var7)) - var17;
      this.g2d.fillRect(var16, var17, var18, var19);
      this.g2d.setComposite(AlphaComposite.SrcIn);
      this.drawTexture(var1, var4, var5, var6, var7, var8, var9, var10, var11);
      this.g2d.setComposite(var13);
      this.g2d.setPaint(var12);
   }

   public void drawPixelsMasked(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      this.doDrawMaskTexture((J2DRTTexture)var1, (J2DRTTexture)var2, var3, var4, var5, var6, var7, var8, var9, var10, true);
   }

   public void maskInterpolatePixels(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      this.doDrawMaskTexture((J2DRTTexture)var1, (J2DRTTexture)var2, var3, var4, var5, var6, var7, var8, var9, var10, false);
   }

   private void doDrawMaskTexture(J2DRTTexture var1, J2DRTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11) {
      int var12 = this.clipRect.x;
      int var13 = this.clipRect.y;
      int var14 = var12 + this.clipRect.width;
      int var15 = var13 + this.clipRect.height;
      if (var5 > 0 && var6 > 0) {
         int var16;
         if (var3 < var12) {
            var16 = var12 - var3;
            if ((var5 -= var16) <= 0) {
               return;
            }

            var7 += var16;
            var9 += var16;
            var3 = var12;
         }

         if (var4 < var13) {
            var16 = var13 - var4;
            if ((var6 -= var16) <= 0) {
               return;
            }

            var8 += var16;
            var10 += var16;
            var4 = var13;
         }

         if (var3 + var5 <= var14 || (var5 = var14 - var3) > 0) {
            if (var4 + var6 <= var15 || (var6 = var15 - var4) > 0) {
               var16 = var1.getContentWidth();
               int var17 = var1.getContentHeight();
               if (var7 < 0) {
                  if ((var5 += var7) <= 0) {
                     return;
                  }

                  var3 -= var7;
                  var9 -= var7;
                  var7 = 0;
               }

               if (var8 < 0) {
                  if ((var6 += var8) <= 0) {
                     return;
                  }

                  var4 -= var8;
                  var10 -= var8;
                  var8 = 0;
               }

               if (var7 + var5 <= var16 || (var5 = var16 - var7) > 0) {
                  if (var8 + var6 <= var17 || (var6 = var17 - var8) > 0) {
                     int var18 = var2.getContentWidth();
                     int var19 = var2.getContentHeight();
                     if (var9 < 0) {
                        if ((var5 += var9) <= 0) {
                           return;
                        }

                        var3 -= var9;
                        var7 -= var9;
                        var9 = 0;
                     }

                     if (var10 < 0) {
                        if ((var6 += var10) <= 0) {
                           return;
                        }

                        var4 -= var10;
                        var8 -= var10;
                        var10 = 0;
                     }

                     if (var9 + var5 <= var18 || (var5 = var18 - var9) > 0) {
                        if (var10 + var6 <= var19 || (var6 = var19 - var10) > 0) {
                           int[] var20 = var1.getPixels();
                           int[] var21 = var2.getPixels();
                           DataBuffer var22 = this.target.getBackBuffer().getRaster().getDataBuffer();
                           int[] var23 = ((DataBufferInt)var22).getData();
                           int var24 = var1.getBufferedImage().getWidth();
                           int var25 = var2.getBufferedImage().getWidth();
                           int var26 = this.target.getBackBuffer().getWidth();
                           int var27 = var8 * var24 + var7;
                           int var28 = var10 * var25 + var9;
                           int var29 = var4 * var26 + var3;
                           int var30;
                           int var31;
                           int var32;
                           int var33;
                           int var34;
                           int var35;
                           int var36;
                           int var37;
                           if (var11) {
                              for(var30 = 0; var30 < var6; ++var30) {
                                 for(var31 = 0; var31 < var5; ++var31) {
                                    var36 = var21[var28 + var31] >>> 24;
                                    if (var36 != 0) {
                                       var37 = var20[var27 + var31];
                                       var32 = var37 >>> 24;
                                       if (var32 != 0) {
                                          if (var36 < 255) {
                                             var36 += var36 >> 7;
                                             var32 *= var36;
                                             var33 = (var37 >> 16 & 255) * var36;
                                             var34 = (var37 >> 8 & 255) * var36;
                                             var35 = (var37 & 255) * var36;
                                          } else {
                                             if (var32 >= 255) {
                                                var23[var29 + var31] = var37;
                                                continue;
                                             }

                                             var32 <<= 8;
                                             var33 = (var37 >> 16 & 255) << 8;
                                             var34 = (var37 >> 8 & 255) << 8;
                                             var35 = (var37 & 255) << 8;
                                          }

                                          var36 = var32 + 128 >> 8;
                                          var36 += var36 >> 7;
                                          var36 = 256 - var36;
                                          var37 = var23[var29 + var31];
                                          var32 += (var37 >>> 24) * var36 + 128;
                                          var33 += (var37 >> 16 & 255) * var36 + 128;
                                          var34 += (var37 >> 8 & 255) * var36 + 128;
                                          var35 += (var37 & 255) * var36 + 128;
                                          var37 = (var32 >> 8 << 24) + (var33 >> 8 << 16) + (var34 >> 8 << 8) + (var35 >> 8);
                                          var23[var29 + var31] = var37;
                                       }
                                    }
                                 }

                                 var27 += var24;
                                 var28 += var25;
                                 var29 += var26;
                              }
                           } else {
                              for(var30 = 0; var30 < var6; ++var30) {
                                 for(var31 = 0; var31 < var5; ++var31) {
                                    var32 = var21[var28 + var31] >>> 24;
                                    if (var32 != 0) {
                                       var33 = var20[var27 + var31];
                                       if (var32 < 255) {
                                          var32 += var32 >> 7;
                                          var34 = (var33 >>> 24) * var32;
                                          var35 = (var33 >> 16 & 255) * var32;
                                          var36 = (var33 >> 8 & 255) * var32;
                                          var37 = (var33 & 255) * var32;
                                          var32 = 256 - var32;
                                          var33 = var23[var29 + var31];
                                          var34 += (var33 >>> 24) * var32 + 128;
                                          var35 += (var33 >> 16 & 255) * var32 + 128;
                                          var36 += (var33 >> 8 & 255) * var32 + 128;
                                          var37 += (var33 & 255) * var32 + 128;
                                          var33 = (var34 >> 8 << 24) + (var35 >> 8 << 16) + (var36 >> 8 << 8) + (var37 >> 8);
                                       }

                                       var23[var29 + var31] = var33;
                                    }
                                 }

                                 var27 += var24;
                                 var28 += var25;
                                 var29 += var26;
                              }
                           }

                        }
                     }
                  }
               }
            }
         }
      }
   }

   public boolean canReadBack() {
      return true;
   }

   public RTTexture readBack(Rectangle var1) {
      J2DRTTexture var2 = this.target.getReadbackBuffer();
      Graphics2D var3 = var2.createAWTGraphics2D();
      var3.setComposite(AlphaComposite.Src);
      int var4 = var1.x;
      int var5 = var1.y;
      int var6 = var1.width;
      int var7 = var1.height;
      int var8 = var4 + var6;
      int var9 = var5 + var7;
      var3.drawImage(this.target.getBackBuffer(), 0, 0, var6, var7, var4, var5, var8, var9, (ImageObserver)null);
      var3.dispose();
      return var2;
   }

   public void releaseReadBackBuffer(RTTexture var1) {
   }

   public NGCamera getCameraNoClone() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void setPerspectiveTransform(GeneralTransform3D var1) {
   }

   public boolean isDepthBuffer() {
      return false;
   }

   public boolean isDepthTest() {
      return false;
   }

   public boolean isAlphaTestShader() {
      if (PrismSettings.verbose && PrismSettings.forceAlphaTestShader) {
         System.out.println("J2D pipe doesn't support shader with alpha testing");
      }

      return false;
   }

   public void setAntialiasedShape(boolean var1) {
      this.antialiasedShape = var1;
      this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antialiasedShape ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
   }

   public boolean isAntialiasedShape() {
      return this.antialiasedShape;
   }

   public void scale(float var1, float var2, float var3) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void setTransform3D(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      if (var5 == 0.0 && var13 == 0.0 && var17 == 0.0 && var19 == 0.0 && var21 == 1.0 && var23 == 0.0) {
         this.setTransform(var1, var9, var3, var11, var7, var15);
      } else {
         throw new UnsupportedOperationException("3D transforms not supported.");
      }
   }

   public void setCamera(NGCamera var1) {
   }

   public void setDepthBuffer(boolean var1) {
   }

   public void setDepthTest(boolean var1) {
   }

   public void sync() {
   }

   public void translate(float var1, float var2, float var3) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void setCulling(boolean var1) {
      this.cull = var1;
   }

   public boolean isCulling() {
      return this.cull;
   }

   public void setClipRectIndex(int var1) {
      this.clipRectIndex = var1;
   }

   public int getClipRectIndex() {
      return this.clipRectIndex;
   }

   public void setHasPreCullingBits(boolean var1) {
      this.hasPreCullingBits = var1;
   }

   public boolean hasPreCullingBits() {
      return this.hasPreCullingBits;
   }

   public void setRenderRoot(NodePath var1) {
      this.renderRoot = var1;
   }

   public NodePath getRenderRoot() {
      return this.renderRoot;
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
      this.pixelScaleY = var2;
   }

   public float getPixelScaleFactorX() {
      return this.pixelScaleX;
   }

   public float getPixelScaleFactorY() {
      return this.pixelScaleY;
   }

   public void blit(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void setLights(NGLightBase[] var1) {
   }

   public NGLightBase[] getLights() {
      return null;
   }

   static {
      LGP_CYCLE_METHODS = new MultipleGradientPaint.CycleMethod[]{CycleMethod.NO_CYCLE, CycleMethod.REFLECT, CycleMethod.REPEAT};
      RGP_CYCLE_METHODS = new com.sun.prism.j2d.paint.MultipleGradientPaint.CycleMethod[]{com.sun.prism.j2d.paint.MultipleGradientPaint.CycleMethod.NO_CYCLE, com.sun.prism.j2d.paint.MultipleGradientPaint.CycleMethod.REFLECT, com.sun.prism.j2d.paint.MultipleGradientPaint.CycleMethod.REPEAT};
      DEFAULT_STROKE = new BasicStroke(1.0F, 2, 0, 10.0F);
      DEFAULT_PAINT = com.sun.prism.paint.Color.WHITE;
      J2D_IDENTITY = new AffineTransform();
      fontMap = new ConcurrentHashMap();
      cleared = 0;
      tmpAT = new AffineTransform();
      tmpQuadShape = new Path2D.Float();
      tmpRect = new Rectangle2D.Float();
      tmpEllipse = new Ellipse2D.Float();
      tmpRRect = new RoundRectangle2D.Float();
      tmpLine = new Line2D.Float();
      tmpAdaptor = new AdaptorShape();
   }

   static class OuterStroke extends FilterStroke {
      static double SQRT_2 = Math.sqrt(2.0);

      OuterStroke(java.awt.BasicStroke var1) {
         super(var1);
      }

      protected Shape makeStrokedRect(Rectangle2D var1) {
         if (this.stroke.getDashArray() != null) {
            return null;
         } else {
            float var2 = this.stroke.getLineWidth() / 2.0F;
            float var3 = (float)var1.getX();
            float var4 = (float)var1.getY();
            float var5 = var3 + (float)var1.getWidth();
            float var6 = var4 + (float)var1.getHeight();
            GeneralPath var7 = new GeneralPath();
            var7.moveTo(var3, var4);
            var7.lineTo(var5, var4);
            var7.lineTo(var5, var6);
            var7.lineTo(var3, var6);
            var7.closePath();
            float var8 = var3 - var2;
            float var9 = var4 - var2;
            float var10 = var5 + var2;
            float var11 = var6 + var2;
            switch (this.stroke.getLineJoin()) {
               case 0:
                  if ((double)this.stroke.getMiterLimit() >= SQRT_2) {
                     var7.moveTo(var8, var9);
                     var7.lineTo(var8, var11);
                     var7.lineTo(var10, var11);
                     var7.lineTo(var10, var9);
                     var7.closePath();
                     break;
                  }
               case 2:
                  var7.moveTo(var8, var4);
                  var7.lineTo(var8, var6);
                  var7.lineTo(var3, var11);
                  var7.lineTo(var5, var11);
                  var7.lineTo(var10, var6);
                  var7.lineTo(var10, var4);
                  var7.lineTo(var5, var9);
                  var7.lineTo(var3, var9);
                  var7.closePath();
                  break;
               case 1:
                  var7.moveTo(var8, var4);
                  var7.lineTo(var8, var6);
                  cornerArc(var7, var8, var6, var8, var11, var3, var11);
                  var7.lineTo(var5, var11);
                  cornerArc(var7, var5, var11, var10, var11, var10, var6);
                  var7.lineTo(var10, var4);
                  cornerArc(var7, var10, var4, var10, var9, var5, var9);
                  var7.lineTo(var3, var9);
                  cornerArc(var7, var3, var9, var8, var9, var8, var4);
                  var7.closePath();
                  break;
               default:
                  throw new InternalError("Unrecognized line join style");
            }

            return var7;
         }
      }

      protected Shape makeStrokedEllipse(Ellipse2D var1) {
         if (this.stroke.getDashArray() != null) {
            return null;
         } else {
            float var2 = this.stroke.getLineWidth() / 2.0F;
            float var3 = (float)var1.getWidth();
            float var4 = (float)var1.getHeight();
            if (!(var3 > var4 * 2.0F) && !(var4 > var3 * 2.0F)) {
               float var5 = (float)var1.getX();
               float var6 = (float)var1.getY();
               float var7 = var5 + var3 / 2.0F;
               float var8 = var6 + var4 / 2.0F;
               float var9 = var5 + var3;
               float var10 = var6 + var4;
               GeneralPath var11 = new GeneralPath();
               var11.moveTo(var7, var6);
               cornerArc(var11, var7, var6, var9, var6, var9, var8);
               cornerArc(var11, var9, var8, var9, var10, var7, var10);
               cornerArc(var11, var7, var10, var5, var10, var5, var8);
               cornerArc(var11, var5, var8, var5, var6, var7, var6);
               var11.closePath();
               var5 -= var2;
               var6 -= var2;
               var9 += var2;
               var10 += var2;
               var11.moveTo(var7, var6);
               cornerArc(var11, var7, var6, var5, var6, var5, var8);
               cornerArc(var11, var5, var8, var5, var10, var7, var10);
               cornerArc(var11, var7, var10, var9, var10, var9, var8);
               cornerArc(var11, var9, var8, var9, var6, var7, var6);
               var11.closePath();
               return var11;
            } else {
               return null;
            }
         }
      }

      protected Shape makeStrokedShape(Shape var1) {
         Shape var2 = this.stroke.createStrokedShape(var1);
         Area var3 = new Area(var2);
         var3.subtract(new Area(var1));
         return var3;
      }
   }

   static class InnerStroke extends FilterStroke {
      InnerStroke(java.awt.BasicStroke var1) {
         super(var1);
      }

      protected Shape makeStrokedRect(Rectangle2D var1) {
         if (this.stroke.getDashArray() != null) {
            return null;
         } else {
            float var2 = this.stroke.getLineWidth() / 2.0F;
            if (!((double)var2 >= var1.getWidth()) && !((double)var2 >= var1.getHeight())) {
               float var3 = (float)var1.getX();
               float var4 = (float)var1.getY();
               float var5 = var3 + (float)var1.getWidth();
               float var6 = var4 + (float)var1.getHeight();
               GeneralPath var7 = new GeneralPath();
               var7.moveTo(var3, var4);
               var7.lineTo(var5, var4);
               var7.lineTo(var5, var6);
               var7.lineTo(var3, var6);
               var7.closePath();
               var3 += var2;
               var4 += var2;
               var5 -= var2;
               var6 -= var2;
               var7.moveTo(var3, var4);
               var7.lineTo(var3, var6);
               var7.lineTo(var5, var6);
               var7.lineTo(var5, var4);
               var7.closePath();
               return var7;
            } else {
               return var1;
            }
         }
      }

      protected Shape makeStrokedEllipse(Ellipse2D var1) {
         if (this.stroke.getDashArray() != null) {
            return null;
         } else {
            float var2 = this.stroke.getLineWidth() / 2.0F;
            float var3 = (float)var1.getWidth();
            float var4 = (float)var1.getHeight();
            if (!(var3 - 2.0F * var2 > var4 * 2.0F) && !(var4 - 2.0F * var2 > var3 * 2.0F)) {
               if (!(var2 >= var3) && !(var2 >= var4)) {
                  float var5 = (float)var1.getX();
                  float var6 = (float)var1.getY();
                  float var7 = var5 + var3 / 2.0F;
                  float var8 = var6 + var4 / 2.0F;
                  float var9 = var5 + var3;
                  float var10 = var6 + var4;
                  GeneralPath var11 = new GeneralPath();
                  var11.moveTo(var7, var6);
                  cornerArc(var11, var7, var6, var9, var6, var9, var8);
                  cornerArc(var11, var9, var8, var9, var10, var7, var10);
                  cornerArc(var11, var7, var10, var5, var10, var5, var8);
                  cornerArc(var11, var5, var8, var5, var6, var7, var6);
                  var11.closePath();
                  var5 += var2;
                  var6 += var2;
                  var9 -= var2;
                  var10 -= var2;
                  var11.moveTo(var7, var6);
                  cornerArc(var11, var7, var6, var5, var6, var5, var8);
                  cornerArc(var11, var5, var8, var5, var10, var7, var10);
                  cornerArc(var11, var7, var10, var9, var10, var9, var8);
                  cornerArc(var11, var9, var8, var9, var6, var7, var6);
                  var11.closePath();
                  return var11;
               } else {
                  return var1;
               }
            } else {
               return null;
            }
         }
      }

      protected Shape makeStrokedShape(Shape var1) {
         Shape var2 = this.stroke.createStrokedShape(var1);
         Area var3 = new Area(var2);
         var3.intersect(new Area(var1));
         return var3;
      }
   }

   abstract static class FilterStroke implements Stroke {
      protected java.awt.BasicStroke stroke;
      static final double CtrlVal = 0.5522847498307933;

      FilterStroke(java.awt.BasicStroke var1) {
         this.stroke = var1;
      }

      protected abstract Shape makeStrokedRect(Rectangle2D var1);

      protected abstract Shape makeStrokedShape(Shape var1);

      public Shape createStrokedShape(Shape var1) {
         if (var1 instanceof Rectangle2D) {
            Shape var2 = this.makeStrokedRect((Rectangle2D)var1);
            if (var2 != null) {
               return var2;
            }
         }

         return this.makeStrokedShape(var1);
      }

      static Point2D cornerArc(GeneralPath var0, float var1, float var2, float var3, float var4, float var5, float var6) {
         return cornerArc(var0, var1, var2, var3, var4, var5, var6, 0.5F);
      }

      static Point2D cornerArc(GeneralPath var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
         float var8 = (float)((double)var1 + 0.5522847498307933 * (double)(var3 - var1));
         float var9 = (float)((double)var2 + 0.5522847498307933 * (double)(var4 - var2));
         float var10 = (float)((double)var5 + 0.5522847498307933 * (double)(var3 - var5));
         float var11 = (float)((double)var6 + 0.5522847498307933 * (double)(var4 - var6));
         var0.curveTo(var8, var9, var10, var11, var5, var6);
         return new Point2D.Float(eval(var1, var8, var10, var5, var7), eval(var2, var9, var11, var6, var7));
      }

      static float eval(float var0, float var1, float var2, float var3, float var4) {
         var0 += (var1 - var0) * var4;
         var1 += (var2 - var1) * var4;
         var2 += (var3 - var2) * var4;
         var0 += (var1 - var0) * var4;
         var1 += (var2 - var1) * var4;
         return var0 + (var1 - var0) * var4;
      }
   }

   private static class AdaptorPathIterator implements PathIterator {
      private static int[] NUM_COORDS = new int[]{2, 2, 4, 6, 0};
      com.sun.javafx.geom.PathIterator priterator;
      float[] tmpcoords;

      private AdaptorPathIterator() {
      }

      public void setIterator(com.sun.javafx.geom.PathIterator var1) {
         this.priterator = var1;
      }

      public int currentSegment(float[] var1) {
         return this.priterator.currentSegment(var1);
      }

      public int currentSegment(double[] var1) {
         if (this.tmpcoords == null) {
            this.tmpcoords = new float[6];
         }

         int var2 = this.priterator.currentSegment(this.tmpcoords);

         for(int var3 = 0; var3 < NUM_COORDS[var2]; ++var3) {
            var1[var3] = (double)this.tmpcoords[var3];
         }

         return var2;
      }

      public int getWindingRule() {
         return this.priterator.getWindingRule();
      }

      public boolean isDone() {
         return this.priterator.isDone();
      }

      public void next() {
         this.priterator.next();
      }

      // $FF: synthetic method
      AdaptorPathIterator(Object var1) {
         this();
      }
   }

   private static class AdaptorShape implements Shape {
      private com.sun.javafx.geom.Shape prshape;
      private static AdaptorPathIterator tmpAdaptor = new AdaptorPathIterator();

      private AdaptorShape() {
      }

      public void setShape(com.sun.javafx.geom.Shape var1) {
         this.prshape = var1;
      }

      public boolean contains(double var1, double var3) {
         return this.prshape.contains((float)var1, (float)var3);
      }

      public boolean contains(Point2D var1) {
         return this.contains(var1.getX(), var1.getY());
      }

      public boolean contains(double var1, double var3, double var5, double var7) {
         return this.prshape.contains((float)var1, (float)var3, (float)var5, (float)var7);
      }

      public boolean contains(Rectangle2D var1) {
         return this.contains(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
      }

      public boolean intersects(double var1, double var3, double var5, double var7) {
         return this.prshape.intersects((float)var1, (float)var3, (float)var5, (float)var7);
      }

      public boolean intersects(Rectangle2D var1) {
         return this.intersects(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
      }

      public java.awt.Rectangle getBounds() {
         return this.getBounds2D().getBounds();
      }

      public Rectangle2D getBounds2D() {
         RectBounds var1 = this.prshape.getBounds();
         Rectangle2D.Float var2 = new Rectangle2D.Float();
         var2.setFrameFromDiagonal((double)var1.getMinX(), (double)var1.getMinY(), (double)var1.getMaxX(), (double)var1.getMaxY());
         return var2;
      }

      private static PathIterator tmpAdaptor(com.sun.javafx.geom.PathIterator var0) {
         tmpAdaptor.setIterator(var0);
         return tmpAdaptor;
      }

      public PathIterator getPathIterator(AffineTransform var1) {
         BaseTransform var2 = var1 == null ? null : J2DPrismGraphics.toPrTransform(var1);
         return tmpAdaptor(this.prshape.getPathIterator(var2));
      }

      public PathIterator getPathIterator(AffineTransform var1, double var2) {
         BaseTransform var4 = var1 == null ? null : J2DPrismGraphics.toPrTransform(var1);
         return tmpAdaptor(this.prshape.getPathIterator(var4, (float)var2));
      }

      // $FF: synthetic method
      AdaptorShape(Object var1) {
         this();
      }
   }
}
