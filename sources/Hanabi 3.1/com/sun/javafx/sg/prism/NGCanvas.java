package com.sun.javafx.sg.prism;

import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.PrismTextLayout;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.ScreenConfigurationAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrTexture;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;

public class NGCanvas extends NGNode {
   public static final byte ATTR_BASE = 0;
   public static final byte GLOBAL_ALPHA = 0;
   public static final byte COMP_MODE = 1;
   public static final byte FILL_PAINT = 2;
   public static final byte STROKE_PAINT = 3;
   public static final byte LINE_WIDTH = 4;
   public static final byte LINE_CAP = 5;
   public static final byte LINE_JOIN = 6;
   public static final byte MITER_LIMIT = 7;
   public static final byte FONT = 8;
   public static final byte TEXT_ALIGN = 9;
   public static final byte TEXT_BASELINE = 10;
   public static final byte TRANSFORM = 11;
   public static final byte EFFECT = 12;
   public static final byte PUSH_CLIP = 13;
   public static final byte POP_CLIP = 14;
   public static final byte ARC_TYPE = 15;
   public static final byte FILL_RULE = 16;
   public static final byte DASH_ARRAY = 17;
   public static final byte DASH_OFFSET = 18;
   public static final byte FONT_SMOOTH = 19;
   public static final byte IMAGE_SMOOTH = 20;
   public static final byte OP_BASE = 25;
   public static final byte FILL_RECT = 25;
   public static final byte STROKE_RECT = 26;
   public static final byte CLEAR_RECT = 27;
   public static final byte STROKE_LINE = 28;
   public static final byte FILL_OVAL = 29;
   public static final byte STROKE_OVAL = 30;
   public static final byte FILL_ROUND_RECT = 31;
   public static final byte STROKE_ROUND_RECT = 32;
   public static final byte FILL_ARC = 33;
   public static final byte STROKE_ARC = 34;
   public static final byte FILL_TEXT = 35;
   public static final byte STROKE_TEXT = 36;
   public static final byte PATH_BASE = 40;
   public static final byte PATHSTART = 40;
   public static final byte MOVETO = 41;
   public static final byte LINETO = 42;
   public static final byte QUADTO = 43;
   public static final byte CUBICTO = 44;
   public static final byte CLOSEPATH = 45;
   public static final byte PATHEND = 46;
   public static final byte FILL_PATH = 47;
   public static final byte STROKE_PATH = 48;
   public static final byte IMG_BASE = 50;
   public static final byte DRAW_IMAGE = 50;
   public static final byte DRAW_SUBIMAGE = 51;
   public static final byte PUT_ARGB = 52;
   public static final byte PUT_ARGBPRE_BUF = 53;
   public static final byte FX_BASE = 60;
   public static final byte FX_APPLY_EFFECT = 60;
   public static final byte UTIL_BASE = 70;
   public static final byte RESET = 70;
   public static final byte SET_DIMS = 71;
   public static final byte CAP_BUTT = 0;
   public static final byte CAP_ROUND = 1;
   public static final byte CAP_SQUARE = 2;
   public static final byte JOIN_MITER = 0;
   public static final byte JOIN_ROUND = 1;
   public static final byte JOIN_BEVEL = 2;
   public static final byte ARC_OPEN = 0;
   public static final byte ARC_CHORD = 1;
   public static final byte ARC_PIE = 2;
   public static final byte SMOOTH_GRAY;
   public static final byte SMOOTH_LCD;
   public static final byte ALIGN_LEFT = 0;
   public static final byte ALIGN_CENTER = 1;
   public static final byte ALIGN_RIGHT = 2;
   public static final byte ALIGN_JUSTIFY = 3;
   public static final byte BASE_TOP = 0;
   public static final byte BASE_MIDDLE = 1;
   public static final byte BASE_ALPHABETIC = 2;
   public static final byte BASE_BOTTOM = 3;
   public static final byte FILL_RULE_NON_ZERO = 0;
   public static final byte FILL_RULE_EVEN_ODD = 1;
   private static Blend BLENDER;
   private GrowableDataBuffer thebuf;
   private final float highestPixelScale;
   private int tw;
   private int th;
   private int cw;
   private int ch;
   private RenderBuf cv;
   private RenderBuf temp;
   private RenderBuf clip;
   private float globalAlpha;
   private Blend.Mode blendmode;
   private Paint fillPaint;
   private Paint strokePaint;
   private float linewidth;
   private int linecap;
   private int linejoin;
   private float miterlimit;
   private double[] dashes;
   private float dashOffset;
   private BasicStroke stroke;
   private Path2D path;
   private NGText ngtext;
   private PrismTextLayout textLayout;
   private PGFont pgfont;
   private int smoothing;
   private boolean imageSmoothing;
   private int align;
   private int baseline;
   private Affine2D transform;
   private Affine2D inverseTransform;
   private boolean inversedirty;
   private LinkedList clipStack;
   private int clipsRendered;
   private boolean clipIsRect;
   private Rectangle clipRect;
   private Effect effect;
   private int arctype;
   static float[] TEMP_COORDS;
   private static Arc2D TEMP_ARC;
   private static RectBounds TEMP_RECTBOUNDS;
   static final Affine2D TEMP_PATH_TX;
   static final int[] numCoords;
   Shape untransformedPath = new Shape() {
      public RectBounds getBounds() {
         float var2;
         float var3;
         if (NGCanvas.this.transform.isTranslateOrIdentity()) {
            RectBounds var8 = NGCanvas.this.path.getBounds();
            if (NGCanvas.this.transform.isIdentity()) {
               return var8;
            } else {
               var2 = (float)NGCanvas.this.transform.getMxt();
               var3 = (float)NGCanvas.this.transform.getMyt();
               return new RectBounds(var8.getMinX() - var2, var8.getMinY() - var3, var8.getMaxX() - var2, var8.getMaxY() - var3);
            }
         } else {
            float var1 = Float.POSITIVE_INFINITY;
            var2 = Float.POSITIVE_INFINITY;
            var3 = Float.NEGATIVE_INFINITY;
            float var4 = Float.NEGATIVE_INFINITY;
            PathIterator var5 = NGCanvas.this.path.getPathIterator(NGCanvas.this.getInverseTransform());

            while(!var5.isDone()) {
               int var6 = NGCanvas.numCoords[var5.currentSegment(NGCanvas.TEMP_COORDS)];

               for(int var7 = 0; var7 < var6; var7 += 2) {
                  if (var1 > NGCanvas.TEMP_COORDS[var7 + 0]) {
                     var1 = NGCanvas.TEMP_COORDS[var7 + 0];
                  }

                  if (var3 < NGCanvas.TEMP_COORDS[var7 + 0]) {
                     var3 = NGCanvas.TEMP_COORDS[var7 + 0];
                  }

                  if (var2 > NGCanvas.TEMP_COORDS[var7 + 1]) {
                     var2 = NGCanvas.TEMP_COORDS[var7 + 1];
                  }

                  if (var4 < NGCanvas.TEMP_COORDS[var7 + 1]) {
                     var4 = NGCanvas.TEMP_COORDS[var7 + 1];
                  }
               }

               var5.next();
            }

            return new RectBounds(var1, var2, var3, var4);
         }
      }

      public boolean contains(float var1, float var2) {
         NGCanvas.TEMP_COORDS[0] = var1;
         NGCanvas.TEMP_COORDS[1] = var2;
         NGCanvas.this.transform.transform(NGCanvas.TEMP_COORDS, 0, NGCanvas.TEMP_COORDS, 0, 1);
         var1 = NGCanvas.TEMP_COORDS[0];
         var2 = NGCanvas.TEMP_COORDS[1];
         return NGCanvas.this.path.contains(var1, var2);
      }

      public boolean intersects(float var1, float var2, float var3, float var4) {
         if (NGCanvas.this.transform.isTranslateOrIdentity()) {
            var1 = (float)((double)var1 + NGCanvas.this.transform.getMxt());
            var2 = (float)((double)var2 + NGCanvas.this.transform.getMyt());
            return NGCanvas.this.path.intersects(var1, var2, var3, var4);
         } else {
            PathIterator var5 = NGCanvas.this.path.getPathIterator(NGCanvas.this.getInverseTransform());
            int var6 = Shape.rectCrossingsForPath(var5, var1, var2, var1 + var3, var2 + var4);
            return var6 != 0;
         }
      }

      public boolean contains(float var1, float var2, float var3, float var4) {
         if (NGCanvas.this.transform.isTranslateOrIdentity()) {
            var1 = (float)((double)var1 + NGCanvas.this.transform.getMxt());
            var2 = (float)((double)var2 + NGCanvas.this.transform.getMyt());
            return NGCanvas.this.path.contains(var1, var2, var3, var4);
         } else {
            PathIterator var5 = NGCanvas.this.path.getPathIterator(NGCanvas.this.getInverseTransform());
            int var6 = Shape.rectCrossingsForPath(var5, var1, var2, var1 + var3, var2 + var4);
            return var6 != Integer.MIN_VALUE && var6 != 0;
         }
      }

      public BaseTransform getCombinedTransform(BaseTransform var1) {
         if (NGCanvas.this.transform.isIdentity()) {
            return var1;
         } else if (NGCanvas.this.transform.equals(var1)) {
            return null;
         } else {
            Affine2D var2 = NGCanvas.this.getInverseTransform();
            if (var1 != null && !var1.isIdentity()) {
               NGCanvas.TEMP_PATH_TX.setTransform(var1);
               NGCanvas.TEMP_PATH_TX.concatenate(var2);
               return NGCanvas.TEMP_PATH_TX;
            } else {
               return var2;
            }
         }
      }

      public PathIterator getPathIterator(BaseTransform var1) {
         return NGCanvas.this.path.getPathIterator(this.getCombinedTransform(var1));
      }

      public PathIterator getPathIterator(BaseTransform var1, float var2) {
         return NGCanvas.this.path.getPathIterator(this.getCombinedTransform(var1), var2);
      }

      public Shape copy() {
         throw new UnsupportedOperationException("Not supported yet.");
      }
   };
   private static final float CLIPRECT_TOLERANCE = 0.00390625F;
   private static final Rectangle TEMP_RECT;
   private static final int[] prcaps;
   private static final int[] prjoins;
   private static final int[] prbases;
   private static final Affine2D TEMP_TX;

   public NGCanvas() {
      Toolkit var1 = Toolkit.getToolkit();
      ScreenConfigurationAccessor var2 = var1.getScreenConfigurationAccessor();
      float var3 = 1.0F;

      Object var5;
      for(Iterator var4 = var1.getScreens().iterator(); var4.hasNext(); var3 = Math.max(var2.getRecommendedOutputScaleY(var5), var3)) {
         var5 = var4.next();
         var3 = Math.max(var2.getRecommendedOutputScaleX(var5), var3);
      }

      this.highestPixelScale = (float)Math.ceil((double)var3);
      this.cv = new RenderBuf(NGCanvas.InitType.PRESERVE_UPPER_LEFT);
      this.temp = new RenderBuf(NGCanvas.InitType.CLEAR);
      this.clip = new RenderBuf(NGCanvas.InitType.FILL_WHITE);
      this.path = new Path2D();
      this.ngtext = new NGText();
      this.textLayout = new PrismTextLayout();
      this.transform = new Affine2D();
      this.clipStack = new LinkedList();
      this.initAttributes();
   }

   private void initAttributes() {
      this.globalAlpha = 1.0F;
      this.blendmode = Blend.Mode.SRC_OVER;
      this.fillPaint = Color.BLACK;
      this.strokePaint = Color.BLACK;
      this.linewidth = 1.0F;
      this.linecap = 2;
      this.linejoin = 0;
      this.miterlimit = 10.0F;
      this.dashes = null;
      this.dashOffset = 0.0F;
      this.stroke = null;
      this.path.setWindingRule(1);
      this.pgfont = (PGFont)Font.getDefault().impl_getNativeFont();
      this.smoothing = SMOOTH_GRAY;
      this.imageSmoothing = true;
      this.align = 0;
      this.baseline = VPos.BASELINE.ordinal();
      this.transform.setToScale((double)this.highestPixelScale, (double)this.highestPixelScale);
      this.clipStack.clear();
      this.resetClip(false);
   }

   private Affine2D getInverseTransform() {
      if (this.inverseTransform == null) {
         this.inverseTransform = new Affine2D();
         this.inversedirty = true;
      }

      if (this.inversedirty) {
         this.inverseTransform.setTransform(this.transform);

         try {
            this.inverseTransform.invert();
         } catch (NoninvertibleTransformException var2) {
            this.inverseTransform.setToScale(0.0, 0.0);
         }

         this.inversedirty = false;
      }

      return this.inverseTransform;
   }

   protected boolean hasOverlappingContents() {
      return true;
   }

   private static void shapebounds(Shape var0, RectBounds var1, BaseTransform var2) {
      TEMP_COORDS[0] = TEMP_COORDS[1] = Float.POSITIVE_INFINITY;
      TEMP_COORDS[2] = TEMP_COORDS[3] = Float.NEGATIVE_INFINITY;
      Shape.accumulate(TEMP_COORDS, var0, var2);
      var1.setBounds(TEMP_COORDS[0], TEMP_COORDS[1], TEMP_COORDS[2], TEMP_COORDS[3]);
   }

   private static void strokebounds(BasicStroke var0, Shape var1, RectBounds var2, BaseTransform var3) {
      TEMP_COORDS[0] = TEMP_COORDS[1] = Float.POSITIVE_INFINITY;
      TEMP_COORDS[2] = TEMP_COORDS[3] = Float.NEGATIVE_INFINITY;
      var0.accumulateShapeBounds(TEMP_COORDS, var1, var3);
      var2.setBounds(TEMP_COORDS[0], TEMP_COORDS[1], TEMP_COORDS[2], TEMP_COORDS[3]);
   }

   private static void runOnRenderThread(Runnable var0) {
      if (Thread.currentThread().getName().startsWith("QuantumRenderer")) {
         var0.run();
      } else {
         FutureTask var1 = new FutureTask(var0, (Object)null);
         Toolkit.getToolkit().addRenderJob(new RenderJob(var1));

         try {
            var1.get();
         } catch (ExecutionException var3) {
            throw new AssertionError(var3);
         } catch (InterruptedException var4) {
         }
      }

   }

   private boolean printedCanvas(Graphics var1) {
      RTTexture var2 = this.cv.tex;
      if (var1 instanceof PrinterGraphics && var2 != null) {
         ResourceFactory var3 = var1.getResourceFactory();
         boolean var4 = var3.isCompatibleTexture(var2);
         if (var4) {
            return false;
         } else {
            int var5 = var2.getContentWidth();
            int var6 = var2.getContentHeight();
            RTTexture var7 = var3.createRTTexture(var5, var6, Texture.WrapMode.CLAMP_TO_ZERO);
            Graphics var8 = var7.createGraphics();
            var8.setCompositeMode(CompositeMode.SRC);
            if (this.cv.savedPixelData == null) {
               PixelData var9 = new PixelData(this.cw, this.ch);
               runOnRenderThread(() -> {
                  var9.save(var2);
                  var9.restore(var8, var5, var6);
               });
            } else {
               this.cv.savedPixelData.restore(var8, var5, var6);
            }

            var1.drawTexture(var7, 0.0F, 0.0F, (float)var5, (float)var6);
            var7.unlock();
            var7.dispose();
            return true;
         }
      } else {
         return false;
      }
   }

   protected void renderContent(Graphics var1) {
      if (!this.printedCanvas(var1)) {
         this.initCanvas(var1);
         if (this.cv.tex != null) {
            if (this.thebuf != null) {
               this.renderStream(this.thebuf);
               GrowableDataBuffer.returnBuffer(this.thebuf);
               this.thebuf = null;
            }

            float var2 = (float)this.tw / this.highestPixelScale;
            float var3 = (float)this.th / this.highestPixelScale;
            var1.drawTexture(this.cv.tex, 0.0F, 0.0F, var2, var3, 0.0F, 0.0F, (float)this.tw, (float)this.th);
            this.cv.save(this.tw, this.th);
         }

         this.temp.g = this.clip.g = this.cv.g = null;
      }
   }

   public void renderForcedContent(Graphics var1) {
      if (this.thebuf != null) {
         this.initCanvas(var1);
         if (this.cv.tex != null) {
            this.renderStream(this.thebuf);
            GrowableDataBuffer.returnBuffer(this.thebuf);
            this.thebuf = null;
            this.cv.save(this.tw, this.th);
         }

         this.temp.g = this.clip.g = this.cv.g = null;
      }

   }

   private void initCanvas(Graphics var1) {
      if (this.tw > 0 && this.th > 0) {
         if (this.cv.validate(var1, this.tw, this.th)) {
            this.cv.tex.contentsUseful();
            this.cv.tex.makePermanent();
            this.cv.tex.lock();
         }

      } else {
         this.cv.dispose();
      }
   }

   private void clearCanvas(int var1, int var2, int var3, int var4) {
      this.cv.g.setCompositeMode(CompositeMode.CLEAR);
      this.cv.g.setTransform(BaseTransform.IDENTITY_TRANSFORM);
      this.cv.g.fillQuad((float)var1, (float)var2, (float)(var1 + var3), (float)(var2 + var4));
      this.cv.g.setCompositeMode(CompositeMode.SRC_OVER);
   }

   private void resetClip(boolean var1) {
      if (var1) {
         this.clip.dispose();
      }

      this.clipsRendered = 0;
      this.clipIsRect = true;
      this.clipRect = null;
   }

   private boolean initClip() {
      boolean var1;
      if (this.clipIsRect) {
         var1 = false;
      } else {
         var1 = true;
         if (this.clip.validate(this.cv.g, this.tw, this.th)) {
            this.clip.tex.contentsUseful();
            this.resetClip(false);
         }
      }

      int var2 = this.clipStack.size();

      while(true) {
         while(this.clipsRendered < var2) {
            Path2D var3 = (Path2D)this.clipStack.get(this.clipsRendered++);
            if (this.clipIsRect) {
               if (var3.checkAndGetIntRect(TEMP_RECT, 0.00390625F)) {
                  if (this.clipRect == null) {
                     this.clipRect = new Rectangle(TEMP_RECT);
                  } else {
                     this.clipRect.intersectWith(TEMP_RECT);
                  }
                  continue;
               }

               this.clipIsRect = false;
               if (!var1) {
                  var1 = true;
                  if (this.clip.validate(this.cv.g, this.tw, this.th)) {
                     this.clip.tex.contentsUseful();
                  }
               }

               if (this.clipRect != null) {
                  this.renderClip(new RoundRectangle2D((float)this.clipRect.x, (float)this.clipRect.y, (float)this.clipRect.width, (float)this.clipRect.height, 0.0F, 0.0F));
               }
            }

            shapebounds(var3, TEMP_RECTBOUNDS, BaseTransform.IDENTITY_TRANSFORM);
            TEMP_RECT.setBounds((BaseBounds)TEMP_RECTBOUNDS);
            if (this.clipRect == null) {
               this.clipRect = new Rectangle(TEMP_RECT);
            } else {
               this.clipRect.intersectWith(TEMP_RECT);
            }

            this.renderClip(var3);
         }

         if (var1 && this.clipIsRect) {
            this.clip.tex.unlock();
         }

         return !this.clipIsRect;
      }
   }

   private void renderClip(Shape var1) {
      this.temp.validate(this.cv.g, this.tw, this.th);
      this.temp.g.setPaint(Color.WHITE);
      this.temp.g.setTransform(BaseTransform.IDENTITY_TRANSFORM);
      this.temp.g.fill(var1);
      this.blendAthruBintoC(this.temp, Blend.Mode.SRC_IN, this.clip, (RectBounds)null, CompositeMode.SRC, this.clip);
      this.temp.tex.unlock();
   }

   private Rectangle applyEffectOnAintoC(Effect var1, Effect var2, BaseTransform var3, Rectangle var4, CompositeMode var5, RenderBuf var6) {
      PrFilterContext var7 = PrFilterContext.getInstance(var6.tex.getAssociatedScreen());
      ImageData var8 = var2.filter(var7, var3, var4, (Object)null, var1);
      Rectangle var9 = var8.getUntransformedBounds();
      Filterable var10 = var8.getUntransformedImage();
      Texture var11 = ((PrTexture)var10).getTextureObject();
      var6.g.setTransform(var8.getTransform());
      var6.g.setCompositeMode(var5);
      var6.g.drawTexture(var11, (float)var9.x, (float)var9.y, (float)var9.width, (float)var9.height);
      var6.g.setTransform(BaseTransform.IDENTITY_TRANSFORM);
      var6.g.setCompositeMode(CompositeMode.SRC_OVER);
      Rectangle var12 = var8.getTransformedBounds(var4);
      var8.unref();
      return var12;
   }

   private void blendAthruBintoC(RenderBuf var1, Blend.Mode var2, RenderBuf var3, RectBounds var4, CompositeMode var5, RenderBuf var6) {
      BLENDER.setTopInput(var1.input);
      BLENDER.setBottomInput(var3.input);
      BLENDER.setMode(var2);
      Rectangle var7;
      if (var4 != null) {
         var7 = new Rectangle(var4);
      } else {
         var7 = null;
      }

      this.applyEffectOnAintoC((Effect)null, BLENDER, BaseTransform.IDENTITY_TRANSFORM, var7, var5, var6);
   }

   private void setupFill(Graphics var1) {
      var1.setPaint(this.fillPaint);
   }

   private BasicStroke getStroke() {
      if (this.stroke == null) {
         this.stroke = new BasicStroke(this.linewidth, this.linecap, this.linejoin, this.miterlimit, this.dashes, this.dashOffset);
      }

      return this.stroke;
   }

   private void setupStroke(Graphics var1) {
      var1.setStroke(this.getStroke());
      var1.setPaint(this.strokePaint);
   }

   private void renderStream(GrowableDataBuffer var1) {
      while(var1.hasValues()) {
         byte var2 = var1.getByte();
         float var19;
         float var20;
         int var23;
         Graphics var24;
         switch (var2) {
            case 0:
               this.globalAlpha = var1.getFloat();
               break;
            case 1:
               this.blendmode = (Blend.Mode)var1.getObject();
               break;
            case 2:
               this.fillPaint = (Paint)var1.getObject();
               break;
            case 3:
               this.strokePaint = (Paint)var1.getObject();
               break;
            case 4:
               this.linewidth = var1.getFloat();
               this.stroke = null;
               break;
            case 5:
               this.linecap = prcaps[var1.getUByte()];
               this.stroke = null;
               break;
            case 6:
               this.linejoin = prjoins[var1.getUByte()];
               this.stroke = null;
               break;
            case 7:
               this.miterlimit = var1.getFloat();
               this.stroke = null;
               break;
            case 8:
               this.pgfont = (PGFont)var1.getObject();
               break;
            case 9:
               this.align = var1.getUByte();
               break;
            case 10:
               this.baseline = prbases[var1.getUByte()];
               break;
            case 11:
               double var32 = var1.getDouble() * (double)this.highestPixelScale;
               double var27 = var1.getDouble() * (double)this.highestPixelScale;
               double var37 = var1.getDouble() * (double)this.highestPixelScale;
               double var41 = var1.getDouble() * (double)this.highestPixelScale;
               double var45 = var1.getDouble() * (double)this.highestPixelScale;
               double var46 = var1.getDouble() * (double)this.highestPixelScale;
               this.transform.setTransform(var32, var41, var27, var45, var37, var46);
               this.inversedirty = true;
               break;
            case 12:
               this.effect = (Effect)var1.getObject();
               break;
            case 13:
               Path2D var29 = (Path2D)var1.getObject();
               if (this.highestPixelScale != 1.0F) {
                  TEMP_TX.setToScale((double)this.highestPixelScale, (double)this.highestPixelScale);
                  var29.transform(TEMP_TX);
               }

               this.clipStack.addLast(var29);
               break;
            case 14:
               this.resetClip(true);
               this.clipStack.removeLast();
               break;
            case 15:
               byte var28 = var1.getByte();
               switch (var28) {
                  case 0:
                     this.arctype = 0;
                     continue;
                  case 1:
                     this.arctype = 1;
                     continue;
                  case 2:
                     this.arctype = 2;
                  default:
                     continue;
               }
            case 16:
               if (var1.getByte() == 0) {
                  this.path.setWindingRule(1);
                  break;
               }

               this.path.setWindingRule(0);
               break;
            case 17:
               this.dashes = (double[])((double[])var1.getObject());
               this.stroke = null;
               break;
            case 18:
               this.dashOffset = var1.getFloat();
               this.stroke = null;
               break;
            case 19:
               this.smoothing = var1.getUByte();
               break;
            case 20:
               this.imageSmoothing = var1.getBoolean();
               break;
            case 21:
            case 22:
            case 23:
            case 24:
            case 37:
            case 38:
            case 39:
            case 49:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            default:
               throw new InternalError("Unrecognized PGCanvas token: " + var2);
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 47:
            case 48:
            case 50:
            case 51:
               boolean var25 = this.initClip();
               RenderBuf var21;
               boolean var22;
               if (var25) {
                  this.temp.validate(this.cv.g, this.tw, this.th);
                  var22 = true;
                  var21 = this.temp;
               } else if (this.blendmode != Blend.Mode.SRC_OVER) {
                  this.temp.validate(this.cv.g, this.tw, this.th);
                  var22 = true;
                  var21 = this.temp;
               } else {
                  var22 = false;
                  var21 = this.cv;
               }

               if (this.effect != null) {
                  var1.save();
                  this.handleRenderOp(var2, var1, (Graphics)null, TEMP_RECTBOUNDS);
                  RenderInput var26 = new RenderInput(var2, var1, this.transform, TEMP_RECTBOUNDS);
                  Rectangle var33 = this.applyEffectOnAintoC(var26, this.effect, this.transform, this.clipRect, CompositeMode.SRC_OVER, var21);
                  if (var21 != this.cv) {
                     TEMP_RECTBOUNDS.setBounds((float)var33.x, (float)var33.y, (float)(var33.x + var33.width), (float)(var33.y + var33.height));
                  }
               } else {
                  var24 = var21.g;
                  var24.setExtraAlpha(this.globalAlpha);
                  var24.setTransform(this.transform);
                  var24.setClipRect(this.clipRect);
                  RectBounds var34 = var21 != this.cv ? TEMP_RECTBOUNDS : null;
                  this.handleRenderOp(var2, var1, var24, var34);
                  var24.setClipRect((Rectangle)null);
               }

               if (var25) {
                  CompositeMode var31;
                  if (this.blendmode == Blend.Mode.SRC_OVER) {
                     var21 = this.cv;
                     var31 = CompositeMode.SRC_OVER;
                  } else {
                     var31 = CompositeMode.SRC;
                  }

                  if (this.clipRect != null) {
                     TEMP_RECTBOUNDS.intersectWith(this.clipRect);
                  }

                  if (!TEMP_RECTBOUNDS.isEmpty()) {
                     if (var21 == this.cv && this.cv.g instanceof MaskTextureGraphics) {
                        MaskTextureGraphics var36 = (MaskTextureGraphics)this.cv.g;
                        int var38 = (int)Math.floor((double)TEMP_RECTBOUNDS.getMinX());
                        int var40 = (int)Math.floor((double)TEMP_RECTBOUNDS.getMinY());
                        int var43 = (int)Math.ceil((double)TEMP_RECTBOUNDS.getMaxX()) - var38;
                        int var44 = (int)Math.ceil((double)TEMP_RECTBOUNDS.getMaxY()) - var40;
                        var36.drawPixelsMasked(this.temp.tex, this.clip.tex, var38, var40, var43, var44, var38, var40, var38, var40);
                     } else {
                        this.blendAthruBintoC(this.temp, Blend.Mode.SRC_IN, this.clip, TEMP_RECTBOUNDS, var31, var21);
                     }
                  }
               }

               if (this.blendmode != Blend.Mode.SRC_OVER) {
                  if (this.clipRect != null) {
                     TEMP_RECTBOUNDS.intersectWith(this.clipRect);
                  }

                  this.blendAthruBintoC(this.temp, this.blendmode, this.cv, TEMP_RECTBOUNDS, CompositeMode.SRC, this.cv);
               }

               if (var25) {
                  this.clip.tex.unlock();
               }

               if (var22) {
                  this.temp.tex.unlock();
               }
               break;
            case 40:
               this.path.reset();
               break;
            case 41:
               this.path.moveTo(var1.getFloat(), var1.getFloat());
               break;
            case 42:
               this.path.lineTo(var1.getFloat(), var1.getFloat());
               break;
            case 43:
               this.path.quadTo(var1.getFloat(), var1.getFloat(), var1.getFloat(), var1.getFloat());
               break;
            case 44:
               this.path.curveTo(var1.getFloat(), var1.getFloat(), var1.getFloat(), var1.getFloat(), var1.getFloat(), var1.getFloat());
               break;
            case 45:
               this.path.closePath();
               break;
            case 46:
               if (this.highestPixelScale != 1.0F) {
                  TEMP_TX.setToScale((double)this.highestPixelScale, (double)this.highestPixelScale);
                  this.path.transform(TEMP_TX);
               }
               break;
            case 52:
               var19 = (float)var1.getInt();
               var20 = (float)var1.getInt();
               var23 = var1.getInt();
               var24 = this.cv.g;
               var24.setExtraAlpha(1.0F);
               var24.setCompositeMode(CompositeMode.SRC);
               var24.setTransform(BaseTransform.IDENTITY_TRANSFORM);
               var19 *= this.highestPixelScale;
               var20 *= this.highestPixelScale;
               float var30 = (float)(var23 >>> 24) / 255.0F;
               float var35 = (float)(var23 >> 16 & 255) / 255.0F;
               float var39 = (float)(var23 >> 8 & 255) / 255.0F;
               float var42 = (float)(var23 & 255) / 255.0F;
               var24.setPaint(new Color(var35, var39, var42, var30));
               var24.fillQuad(var19, var20, var19 + this.highestPixelScale, var20 + this.highestPixelScale);
               var24.setCompositeMode(CompositeMode.SRC_OVER);
               break;
            case 53:
               var19 = (float)var1.getInt();
               var20 = (float)var1.getInt();
               var23 = var1.getInt();
               int var10 = var1.getInt();
               byte[] var11 = (byte[])((byte[])var1.getObject());
               Image var12 = Image.fromByteBgraPreData(var11, var23, var10);
               Graphics var13 = this.cv.g;
               ResourceFactory var14 = var13.getResourceFactory();
               Texture var15 = var14.getCachedTexture(var12, Texture.WrapMode.CLAMP_TO_EDGE);
               var13.setTransform(BaseTransform.IDENTITY_TRANSFORM);
               var13.setCompositeMode(CompositeMode.SRC);
               float var16 = var19 + (float)var23;
               float var17 = var20 + (float)var10;
               var19 *= this.highestPixelScale;
               var20 *= this.highestPixelScale;
               var16 *= this.highestPixelScale;
               var17 *= this.highestPixelScale;
               var13.drawTexture(var15, var19, var20, var16, var17, 0.0F, 0.0F, (float)var23, (float)var10);
               var15.contentsNotUseful();
               var15.unlock();
               var13.setCompositeMode(CompositeMode.SRC_OVER);
               break;
            case 60:
               Effect var7 = (Effect)var1.getObject();
               RenderBuf var8 = this.clipStack.isEmpty() ? this.cv : this.temp;
               Object var9;
               if (this.highestPixelScale != 1.0F) {
                  TEMP_TX.setToScale((double)this.highestPixelScale, (double)this.highestPixelScale);
                  var9 = TEMP_TX;
                  this.cv.input.setPixelScale(this.highestPixelScale);
               } else {
                  var9 = BaseTransform.IDENTITY_TRANSFORM;
               }

               this.applyEffectOnAintoC(this.cv.input, var7, (BaseTransform)var9, (Rectangle)null, CompositeMode.SRC, var8);
               this.cv.input.setPixelScale(1.0F);
               if (var8 != this.cv) {
                  this.blendAthruBintoC(var8, Blend.Mode.SRC_IN, this.clip, (RectBounds)null, CompositeMode.SRC, this.cv);
               }
               break;
            case 70:
               this.initAttributes();
               this.cw = this.tw;
               this.ch = this.th;
               this.clearCanvas(0, 0, this.tw, this.th);
               break;
            case 71:
               int var3 = (int)Math.ceil((double)(var1.getFloat() * this.highestPixelScale));
               int var4 = (int)Math.ceil((double)(var1.getFloat() * this.highestPixelScale));
               int var5 = Math.min(var3, this.cw);
               int var6 = Math.min(var4, this.ch);
               if (var5 < this.tw) {
                  this.clearCanvas(var5, 0, this.tw - var5, this.th);
               }

               if (var6 < this.th) {
                  this.clearCanvas(0, var6, this.tw, this.th - var6);
               }

               this.cw = var3;
               this.ch = var4;
         }
      }

   }

   public void handleRenderOp(int var1, GrowableDataBuffer var2, Graphics var3, RectBounds var4) {
      boolean var5;
      boolean var6;
      float var8;
      var5 = false;
      var6 = false;
      float var7;
      float var9;
      float var10;
      float var12;
      float var13;
      float var14;
      float var16;
      float var25;
      label173:
      switch (var1) {
         case 26:
         case 30:
            var5 = true;
         case 25:
         case 27:
         case 29:
            var7 = var2.getFloat();
            var8 = var2.getFloat();
            var9 = var2.getFloat();
            var10 = var2.getFloat();
            if (var4 != null) {
               var4.setBounds(var7, var8, var7 + var9, var8 + var10);
               var6 = true;
            }

            if (var3 != null) {
               switch (var1) {
                  case 25:
                     this.setupFill(var3);
                     var3.fillRect(var7, var8, var9, var10);
                     break label173;
                  case 26:
                     this.setupStroke(var3);
                     var3.drawRect(var7, var8, var9, var10);
                     break label173;
                  case 27:
                     var3.setCompositeMode(CompositeMode.CLEAR);
                     var3.fillRect(var7, var8, var9, var10);
                     var3.setCompositeMode(CompositeMode.SRC_OVER);
                  case 28:
                  default:
                     break label173;
                  case 29:
                     this.setupFill(var3);
                     var3.fillEllipse(var7, var8, var9, var10);
                     break label173;
                  case 30:
                     this.setupStroke(var3);
                     var3.drawEllipse(var7, var8, var9, var10);
               }
            }
            break;
         case 28:
            var7 = var2.getFloat();
            var8 = var2.getFloat();
            var9 = var2.getFloat();
            var10 = var2.getFloat();
            if (var4 != null) {
               var4.setBoundsAndSort(var7, var8, var9, var10);
               var5 = true;
               var6 = true;
            }

            if (var3 != null) {
               this.setupStroke(var3);
               var3.drawLine(var7, var8, var9, var10);
            }
            break;
         case 32:
            var5 = true;
         case 31:
            var7 = var2.getFloat();
            var8 = var2.getFloat();
            var9 = var2.getFloat();
            var10 = var2.getFloat();
            var25 = var2.getFloat();
            var12 = var2.getFloat();
            if (var4 != null) {
               var4.setBounds(var7, var8, var7 + var9, var8 + var10);
               var6 = true;
            }

            if (var3 != null) {
               if (var1 == 31) {
                  this.setupFill(var3);
                  var3.fillRoundRect(var7, var8, var9, var10, var25, var12);
               } else {
                  this.setupStroke(var3);
                  var3.drawRoundRect(var7, var8, var9, var10, var25, var12);
               }
            }
            break;
         case 33:
         case 34:
            var7 = var2.getFloat();
            var8 = var2.getFloat();
            var9 = var2.getFloat();
            var10 = var2.getFloat();
            var25 = var2.getFloat();
            var12 = var2.getFloat();
            TEMP_ARC.setArc(var7, var8, var9, var10, var25, var12, this.arctype);
            if (var1 == 33) {
               if (var4 != null) {
                  shapebounds(TEMP_ARC, var4, this.transform);
               }

               if (var3 != null) {
                  this.setupFill(var3);
                  var3.fill(TEMP_ARC);
               }
            } else {
               if (var4 != null) {
                  strokebounds(this.getStroke(), TEMP_ARC, var4, this.transform);
               }

               if (var3 != null) {
                  this.setupStroke(var3);
                  var3.draw(TEMP_ARC);
               }
            }
            break;
         case 35:
         case 36:
            var7 = var2.getFloat();
            var8 = var2.getFloat();
            var9 = var2.getFloat();
            boolean var23 = var2.getBoolean();
            String var24 = (String)var2.getObject();
            int var26 = var23 ? 2048 : 1024;
            this.textLayout.setContent(var24, this.pgfont);
            this.textLayout.setAlignment(this.align);
            this.textLayout.setDirection(var26);
            var13 = 0.0F;
            var14 = 0.0F;
            BaseBounds var27 = this.textLayout.getBounds();
            var16 = var27.getWidth();
            float var29 = var27.getHeight();
            switch (this.align) {
               case 1:
                  var13 = var16 / 2.0F;
                  break;
               case 2:
                  var13 = var16;
            }

            switch (this.baseline) {
               case 1:
                  var14 = var29 / 2.0F;
                  break;
               case 2:
                  var14 = -var27.getMinY();
                  break;
               case 3:
                  var14 = var29;
            }

            float var30 = 1.0F;
            float var19 = 0.0F;
            float var20 = var8 - var14;
            if ((double)var9 > 0.0 && var16 > var9) {
               float var21 = var9 / var16;
               if (var23) {
                  var19 = -((var7 + var9) / var21 - var13);
                  var30 = -var21;
               } else {
                  var19 = var7 / var21 - var13;
                  var30 = var21;
               }
            } else if (var23) {
               var19 = -(var7 - var13 + var16);
               var30 = -1.0F;
            } else {
               var19 = var7 - var13;
            }

            if (var4 != null) {
               this.computeTextLayoutBounds(var4, this.transform, var30, var19, var20, var1);
            }

            if (var3 != null) {
               if (var30 != 1.0F) {
                  var3.scale(var30, 1.0F);
               }

               this.ngtext.setLayoutLocation(-var19, -var20);
               RectBounds var31;
               if (var1 == 35) {
                  this.ngtext.setMode(NGShape.Mode.FILL);
                  this.ngtext.setFillPaint(this.fillPaint);
                  if (this.fillPaint.isProportional() || this.smoothing == SMOOTH_LCD) {
                     var31 = new RectBounds();
                     this.computeTextLayoutBounds(var31, BaseTransform.IDENTITY_TRANSFORM, 1.0F, var19, var20, var1);
                     this.ngtext.setContentBounds(var31);
                  }
               } else {
                  if (this.strokePaint.isProportional()) {
                     var31 = new RectBounds();
                     this.computeTextLayoutBounds(var31, BaseTransform.IDENTITY_TRANSFORM, 1.0F, var19, var20, var1);
                     this.ngtext.setContentBounds(var31);
                  }

                  this.ngtext.setMode(NGShape.Mode.STROKE);
                  this.ngtext.setDrawStroke(this.getStroke());
                  this.ngtext.setDrawPaint(this.strokePaint);
               }

               this.ngtext.setFont(this.pgfont);
               this.ngtext.setFontSmoothingType(this.smoothing);
               this.ngtext.setGlyphs(this.textLayout.getRuns());
               this.ngtext.renderContent(var3);
            }
            break;
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 49:
         default:
            throw new InternalError("Unrecognized PGCanvas rendering token: " + var1);
         case 47:
            if (var4 != null) {
               shapebounds(this.path, var4, BaseTransform.IDENTITY_TRANSFORM);
            }

            if (var3 != null) {
               this.setupFill(var3);
               var3.fill(this.untransformedPath);
            }
            break;
         case 48:
            if (var4 != null) {
               strokebounds(this.getStroke(), this.untransformedPath, var4, this.transform);
            }

            if (var3 != null) {
               this.setupStroke(var3);
               var3.draw(this.untransformedPath);
            }
            break;
         case 50:
         case 51:
            var7 = var2.getFloat();
            var8 = var2.getFloat();
            var9 = var2.getFloat();
            var10 = var2.getFloat();
            Image var11 = (Image)var2.getObject();
            float var15;
            if (var1 == 50) {
               var13 = 0.0F;
               var12 = 0.0F;
               var14 = (float)var11.getWidth();
               var15 = (float)var11.getHeight();
            } else {
               var12 = var2.getFloat();
               var13 = var2.getFloat();
               var14 = var2.getFloat();
               var15 = var2.getFloat();
               var16 = var11.getPixelScale();
               if (var16 != 1.0F) {
                  var12 *= var16;
                  var13 *= var16;
                  var14 *= var16;
                  var15 *= var16;
               }
            }

            if (var4 != null) {
               var4.setBounds(var7, var8, var7 + var9, var8 + var10);
               var6 = true;
            }

            if (var3 != null) {
               ResourceFactory var28 = var3.getResourceFactory();
               Texture var17 = var28.getCachedTexture(var11, Texture.WrapMode.CLAMP_TO_EDGE);
               boolean var18 = var17.getLinearFiltering();
               if (this.imageSmoothing != var18) {
                  var17.setLinearFiltering(this.imageSmoothing);
               }

               var3.drawTexture(var17, var7, var8, var7 + var9, var8 + var10, var12, var13, var12 + var14, var13 + var15);
               if (this.imageSmoothing != var18) {
                  var17.setLinearFiltering(var18);
               }

               var17.unlock();
            }
      }

      if (var4 != null) {
         if (var5) {
            BasicStroke var22 = this.getStroke();
            if (var22.getType() != 1) {
               var8 = var22.getLineWidth();
               if (var22.getType() == 0) {
                  var8 /= 2.0F;
               }

               var4.grow(var8, var8);
            }
         }

         if (var6) {
            txBounds(var4, this.transform);
         }
      }

   }

   void computeTextLayoutBounds(RectBounds var1, BaseTransform var2, float var3, float var4, float var5, int var6) {
      this.textLayout.getBounds((TextSpan)null, var1);
      TEMP_TX.setTransform(var2);
      TEMP_TX.scale((double)var3, 1.0);
      TEMP_TX.translate((double)var4, (double)var5);
      TEMP_TX.transform(var1, var1);
      if (var6 == 36) {
         byte var7 = 1;
         Shape var8 = this.textLayout.getShape(var7, (TextSpan)null);
         RectBounds var9 = new RectBounds();
         strokebounds(this.getStroke(), var8, var9, TEMP_TX);
         var1.unionWith(var9);
      }

   }

   static void txBounds(RectBounds var0, BaseTransform var1) {
      switch (var1.getType()) {
         case 0:
            break;
         case 1:
            float var2 = (float)var1.getMxt();
            float var3 = (float)var1.getMyt();
            var0.setBounds(var0.getMinX() + var2, var0.getMinY() + var3, var0.getMaxX() + var2, var0.getMaxY() + var3);
            break;
         default:
            BaseBounds var4 = var1.transform((BaseBounds)var0, (BaseBounds)var0);
            if (var4 != var0) {
               var0.setBounds(var4.getMinX(), var4.getMinY(), var4.getMaxX(), var4.getMaxY());
            }
      }

   }

   static void inverseTxBounds(RectBounds var0, BaseTransform var1) {
      switch (var1.getType()) {
         case 0:
            break;
         case 1:
            float var2 = (float)var1.getMxt();
            float var3 = (float)var1.getMyt();
            var0.setBounds(var0.getMinX() - var2, var0.getMinY() - var3, var0.getMaxX() - var2, var0.getMaxY() - var3);
            break;
         default:
            try {
               BaseBounds var4 = var1.inverseTransform((BaseBounds)var0, (BaseBounds)var0);
               if (var4 != var0) {
                  var0.setBounds(var4.getMinX(), var4.getMinY(), var4.getMaxX(), var4.getMaxY());
               }
            } catch (NoninvertibleTransformException var5) {
               var0.makeEmpty();
            }
      }

   }

   public void updateBounds(float var1, float var2) {
      this.tw = (int)Math.ceil((double)(var1 * this.highestPixelScale));
      this.th = (int)Math.ceil((double)(var2 * this.highestPixelScale));
      this.geometryChanged();
   }

   public boolean updateRendering(GrowableDataBuffer var1) {
      if (var1.isEmpty()) {
         GrowableDataBuffer.returnBuffer(var1);
         return this.thebuf != null;
      } else {
         boolean var2 = var1.peekByte(0) == 70;
         GrowableDataBuffer var3;
         if (!var2 && this.thebuf != null) {
            this.thebuf.append(var1);
            var3 = var1;
         } else {
            var3 = this.thebuf;
            this.thebuf = var1;
         }

         this.geometryChanged();
         if (var3 != null) {
            GrowableDataBuffer.returnBuffer(var3);
            return true;
         } else {
            return false;
         }
      }
   }

   static {
      SMOOTH_GRAY = (byte)FontSmoothingType.GRAY.ordinal();
      SMOOTH_LCD = (byte)FontSmoothingType.LCD.ordinal();
      BLENDER = new MyBlend(Blend.Mode.SRC_OVER, (Effect)null, (Effect)null);
      TEMP_COORDS = new float[6];
      TEMP_ARC = new Arc2D();
      TEMP_RECTBOUNDS = new RectBounds();
      TEMP_PATH_TX = new Affine2D();
      numCoords = new int[]{2, 2, 4, 6, 0};
      TEMP_RECT = new Rectangle();
      prcaps = new int[]{0, 1, 2};
      prjoins = new int[]{0, 1, 2};
      prbases = new int[]{VPos.TOP.ordinal(), VPos.CENTER.ordinal(), VPos.BASELINE.ordinal(), VPos.BOTTOM.ordinal()};
      TEMP_TX = new Affine2D();
   }

   static class EffectInput extends Effect {
      RTTexture tex;
      float pixelscale;

      EffectInput(RTTexture var1) {
         this.tex = var1;
         this.pixelscale = 1.0F;
      }

      public void setPixelScale(float var1) {
         this.pixelscale = var1;
      }

      public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
         PrDrawable var6 = PrDrawable.create(var1, this.tex);
         Rectangle var7 = new Rectangle(this.tex.getContentWidth(), this.tex.getContentHeight());
         var6.lock();
         ImageData var8 = new ImageData(var1, var6, var7);
         var8.setReusable(true);
         if (this.pixelscale != 1.0F || !var2.isIdentity()) {
            Affine2D var9 = new Affine2D();
            var9.scale((double)(1.0F / this.pixelscale), (double)(1.0F / this.pixelscale));
            var9.concatenate(var2);
            var8 = var8.transform(var9);
         }

         return var8;
      }

      public Effect.AccelType getAccelType(FilterContext var1) {
         throw new UnsupportedOperationException("Not supported yet.");
      }

      public BaseBounds getBounds(BaseTransform var1, Effect var2) {
         Rectangle var3 = new Rectangle(this.tex.getContentWidth(), this.tex.getContentHeight());
         return transformBounds(var1, new RectBounds(var3));
      }

      public boolean reducesOpaquePixels() {
         return false;
      }

      public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
         return null;
      }
   }

   static class MyBlend extends Blend {
      public MyBlend(Blend.Mode var1, Effect var2, Effect var3) {
         super(var1, var2, var3);
      }

      public Rectangle getResultBounds(BaseTransform var1, Rectangle var2, ImageData... var3) {
         Rectangle var4 = super.getResultBounds(var1, var2, var3);
         var4.intersectWith(var2);
         return var4;
      }
   }

   class RenderInput extends Effect {
      float x;
      float y;
      float w;
      float h;
      int token;
      GrowableDataBuffer buf;
      Affine2D savedBoundsTx = new Affine2D();

      public RenderInput(int var2, GrowableDataBuffer var3, BaseTransform var4, RectBounds var5) {
         this.token = var2;
         this.buf = var3;
         this.savedBoundsTx.setTransform(var4);
         this.x = var5.getMinX();
         this.y = var5.getMinY();
         this.w = var5.getWidth();
         this.h = var5.getHeight();
      }

      public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
         BaseBounds var6 = this.getBounds(var2, var5);
         if (var3 != null) {
            var6.intersectWith(var3);
         }

         Rectangle var7 = new Rectangle(var6);
         if (var7.width < 1) {
            var7.width = 1;
         }

         if (var7.height < 1) {
            var7.height = 1;
         }

         PrDrawable var8 = (PrDrawable)Effect.getCompatibleImage(var1, var7.width, var7.height);
         if (var8 != null) {
            Graphics var9 = var8.createGraphics();
            var9.setExtraAlpha(NGCanvas.this.globalAlpha);
            var9.translate((float)(-var7.x), (float)(-var7.y));
            if (var2 != null) {
               var9.transform(var2);
            }

            this.buf.restore();
            NGCanvas.this.handleRenderOp(this.token, this.buf, var9, (RectBounds)null);
         }

         return new ImageData(var1, var8, var7);
      }

      public Effect.AccelType getAccelType(FilterContext var1) {
         throw new UnsupportedOperationException("Not supported yet.");
      }

      public BaseBounds getBounds(BaseTransform var1, Effect var2) {
         RectBounds var3 = new RectBounds(this.x, this.y, this.x + this.w, this.y + this.h);
         if (!var1.equals(this.savedBoundsTx)) {
            NGCanvas.inverseTxBounds(var3, this.savedBoundsTx);
            NGCanvas.txBounds(var3, var1);
         }

         return var3;
      }

      public boolean reducesOpaquePixels() {
         return false;
      }

      public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
         return null;
      }
   }

   private static class PixelData {
      private IntBuffer pixels;
      private boolean validPixels;
      private int cw;
      private int ch;

      private PixelData(int var1, int var2) {
         this.pixels = null;
         this.validPixels = false;
         this.cw = var1;
         this.ch = var2;
         this.pixels = IntBuffer.allocate(var1 * var2);
      }

      private void save(RTTexture var1) {
         int var2 = var1.getContentWidth();
         int var3 = var1.getContentHeight();
         if (this.cw < var2 || this.ch < var3) {
            this.cw = var2;
            this.ch = var3;
            this.pixels = IntBuffer.allocate(this.cw * this.ch);
         }

         this.pixels.rewind();
         var1.readPixels(this.pixels);
         this.validPixels = true;
      }

      private void restore(Graphics var1, int var2, int var3) {
         if (this.validPixels) {
            Image var4 = Image.fromIntArgbPreData(this.pixels, var2, var3);
            ResourceFactory var5 = var1.getResourceFactory();
            Texture var6 = var5.createTexture(var4, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
            var1.drawTexture(var6, 0.0F, 0.0F, (float)var2, (float)var3);
            var6.dispose();
         }

      }

      // $FF: synthetic method
      PixelData(int var1, int var2, Object var3) {
         this(var1, var2);
      }
   }

   static class RenderBuf {
      final InitType init_type;
      RTTexture tex;
      Graphics g;
      EffectInput input;
      private PixelData savedPixelData = null;

      public RenderBuf(InitType var1) {
         this.init_type = var1;
      }

      public void dispose() {
         if (this.tex != null) {
            this.tex.dispose();
         }

         this.tex = null;
         this.g = null;
         this.input = null;
      }

      public boolean validate(Graphics var1, int var2, int var3) {
         int var4;
         int var5;
         boolean var6;
         if (this.tex == null) {
            var5 = 0;
            var4 = 0;
            var6 = true;
         } else {
            var4 = this.tex.getContentWidth();
            var5 = this.tex.getContentHeight();
            this.tex.lock();
            var6 = this.tex.isSurfaceLost() || var4 < var2 || var5 < var3;
         }

         if (var6) {
            RTTexture var10 = this.tex;
            ResourceFactory var8 = var1 == null ? GraphicsPipeline.getDefaultResourceFactory() : var1.getResourceFactory();
            RTTexture var9 = var8.createRTTexture(var2, var3, Texture.WrapMode.CLAMP_TO_ZERO);
            this.tex = var9;
            this.g = var9.createGraphics();
            this.input = new EffectInput(var9);
            if (var10 != null) {
               if (this.init_type == NGCanvas.InitType.PRESERVE_UPPER_LEFT) {
                  this.g.setCompositeMode(CompositeMode.SRC);
                  if (var10.isSurfaceLost()) {
                     if (this.savedPixelData != null) {
                        this.savedPixelData.restore(this.g, var4, var5);
                     }
                  } else {
                     this.g.drawTexture(var10, 0.0F, 0.0F, (float)var4, (float)var5);
                  }

                  this.g.setCompositeMode(CompositeMode.SRC_OVER);
               }

               var10.unlock();
               var10.dispose();
            }

            if (this.init_type == NGCanvas.InitType.FILL_WHITE) {
               this.g.clear(Color.WHITE);
            }

            return true;
         } else {
            if (this.g == null) {
               this.g = this.tex.createGraphics();
               if (this.g == null) {
                  this.tex.dispose();
                  ResourceFactory var7 = var1 == null ? GraphicsPipeline.getDefaultResourceFactory() : var1.getResourceFactory();
                  this.tex = var7.createRTTexture(var2, var3, Texture.WrapMode.CLAMP_TO_ZERO);
                  this.g = this.tex.createGraphics();
                  this.input = new EffectInput(this.tex);
                  if (this.savedPixelData != null) {
                     this.g.setCompositeMode(CompositeMode.SRC);
                     this.savedPixelData.restore(this.g, var2, var3);
                     this.g.setCompositeMode(CompositeMode.SRC_OVER);
                  } else if (this.init_type == NGCanvas.InitType.FILL_WHITE) {
                     this.g.clear(Color.WHITE);
                  }

                  return true;
               }
            }

            if (this.init_type == NGCanvas.InitType.CLEAR) {
               this.g.clear();
            }

            return false;
         }
      }

      private void save(int var1, int var2) {
         if (this.tex.isVolatile()) {
            if (this.savedPixelData == null) {
               this.savedPixelData = new PixelData(var1, var2);
            }

            this.savedPixelData.save(this.tex);
         }

      }
   }

   static enum InitType {
      CLEAR,
      FILL_WHITE,
      PRESERVE_UPPER_LEFT;
   }
}
