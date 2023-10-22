package javafx.scene.canvas;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.IllegalPathStateException;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.sg.prism.GrowableDataBuffer;
import com.sun.javafx.tk.Toolkit;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.geometry.NodeOrientation;
import javafx.geometry.VPos;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

public final class GraphicsContext {
   Canvas theCanvas;
   Path2D path;
   boolean pathDirty;
   State curState;
   LinkedList stateStack;
   LinkedList clipStack;
   private float[] coords = new float[6];
   private static final byte[] pgtype = new byte[]{41, 42, 43, 44, 45};
   private static final int[] numsegs = new int[]{2, 2, 4, 6, 0};
   private float[] polybuf = new float[512];
   private boolean txdirty;
   private PixelWriter writer;

   GraphicsContext(Canvas var1) {
      this.theCanvas = var1;
      this.path = new Path2D();
      this.pathDirty = true;
      this.curState = new State();
      this.stateStack = new LinkedList();
      this.clipStack = new LinkedList();
   }

   private GrowableDataBuffer getBuffer() {
      return this.theCanvas.getBuffer();
   }

   private void markPathDirty() {
      this.pathDirty = true;
   }

   private void writePath(byte var1) {
      this.updateTransform();
      GrowableDataBuffer var2 = this.getBuffer();
      if (this.pathDirty) {
         var2.putByte((byte)40);
         PathIterator var3 = this.path.getPathIterator((BaseTransform)null);

         while(!var3.isDone()) {
            int var4 = var3.currentSegment(this.coords);
            var2.putByte(pgtype[var4]);

            for(int var5 = 0; var5 < numsegs[var4]; ++var5) {
               var2.putFloat(this.coords[var5]);
            }

            var3.next();
         }

         var2.putByte((byte)46);
         this.pathDirty = false;
      }

      var2.putByte(var1);
   }

   private void writePaint(Paint var1, byte var2) {
      GrowableDataBuffer var3 = this.getBuffer();
      var3.putByte(var2);
      var3.putObject(Toolkit.getPaintAccessor().getPlatformPaint(var1));
   }

   private void writeArcType(ArcType var1) {
      byte var2;
      switch (var1) {
         case OPEN:
            var2 = 0;
            break;
         case CHORD:
            var2 = 1;
            break;
         case ROUND:
            var2 = 2;
            break;
         default:
            return;
      }

      this.writeParam(var2, (byte)15);
   }

   private void writeRectParams(GrowableDataBuffer var1, double var2, double var4, double var6, double var8, byte var10) {
      var1.putByte(var10);
      var1.putFloat((float)var2);
      var1.putFloat((float)var4);
      var1.putFloat((float)var6);
      var1.putFloat((float)var8);
   }

   private void writeOp4(double var1, double var3, double var5, double var7, byte var9) {
      this.updateTransform();
      this.writeRectParams(this.getBuffer(), var1, var3, var5, var7, var9);
   }

   private void writeOp6(double var1, double var3, double var5, double var7, double var9, double var11, byte var13) {
      this.updateTransform();
      GrowableDataBuffer var14 = this.getBuffer();
      var14.putByte(var13);
      var14.putFloat((float)var1);
      var14.putFloat((float)var3);
      var14.putFloat((float)var5);
      var14.putFloat((float)var7);
      var14.putFloat((float)var9);
      var14.putFloat((float)var11);
   }

   private void flushPolyBuf(GrowableDataBuffer var1, float[] var2, int var3, byte var4) {
      this.curState.transform.transform(var2, 0, var2, 0, var3 / 2);

      for(int var5 = 0; var5 < var3; var5 += 2) {
         var1.putByte(var4);
         var1.putFloat(var2[var5]);
         var1.putFloat(var2[var5 + 1]);
         var4 = 42;
      }

   }

   private void writePoly(double[] var1, double[] var2, int var3, boolean var4, byte var5) {
      if (var1 != null && var2 != null) {
         GrowableDataBuffer var6 = this.getBuffer();
         var6.putByte((byte)40);
         int var7 = 0;
         byte var8 = 41;

         for(int var9 = 0; var9 < var3; ++var9) {
            if (var7 >= this.polybuf.length) {
               this.flushPolyBuf(var6, this.polybuf, var7, var8);
               var7 = 0;
               var8 = 42;
            }

            this.polybuf[var7++] = (float)var1[var9];
            this.polybuf[var7++] = (float)var2[var9];
         }

         this.flushPolyBuf(var6, this.polybuf, var7, var8);
         if (var4) {
            var6.putByte((byte)45);
         }

         var6.putByte((byte)46);
         this.updateTransform();
         var6.putByte(var5);
         this.markPathDirty();
      }
   }

   private void writeImage(Image var1, double var2, double var4, double var6, double var8) {
      if (var1 != null && !(var1.getProgress() < 1.0)) {
         Object var10 = var1.impl_getPlatformImage();
         if (var10 != null) {
            this.updateTransform();
            GrowableDataBuffer var11 = this.getBuffer();
            this.writeRectParams(var11, var2, var4, var6, var8, (byte)50);
            var11.putObject(var10);
         }
      }
   }

   private void writeImage(Image var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      if (var1 != null && !(var1.getProgress() < 1.0)) {
         Object var18 = var1.impl_getPlatformImage();
         if (var18 != null) {
            this.updateTransform();
            GrowableDataBuffer var19 = this.getBuffer();
            this.writeRectParams(var19, var2, var4, var6, var8, (byte)51);
            var19.putFloat((float)var10);
            var19.putFloat((float)var12);
            var19.putFloat((float)var14);
            var19.putFloat((float)var16);
            var19.putObject(var18);
         }
      }
   }

   private void writeText(String var1, double var2, double var4, double var6, byte var8) {
      if (var1 != null) {
         this.updateTransform();
         GrowableDataBuffer var9 = this.getBuffer();
         var9.putByte(var8);
         var9.putFloat((float)var2);
         var9.putFloat((float)var4);
         var9.putFloat((float)var6);
         var9.putBoolean(this.theCanvas.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT);
         var9.putObject(var1);
      }
   }

   void writeParam(double var1, byte var3) {
      GrowableDataBuffer var4 = this.getBuffer();
      var4.putByte(var3);
      var4.putFloat((float)var1);
   }

   private void writeParam(byte var1, byte var2) {
      GrowableDataBuffer var3 = this.getBuffer();
      var3.putByte(var2);
      var3.putByte(var1);
   }

   private void updateTransform() {
      if (this.txdirty) {
         this.txdirty = false;
         GrowableDataBuffer var1 = this.getBuffer();
         var1.putByte((byte)11);
         var1.putDouble(this.curState.transform.getMxx());
         var1.putDouble(this.curState.transform.getMxy());
         var1.putDouble(this.curState.transform.getMxt());
         var1.putDouble(this.curState.transform.getMyx());
         var1.putDouble(this.curState.transform.getMyy());
         var1.putDouble(this.curState.transform.getMyt());
      }

   }

   void updateDimensions() {
      GrowableDataBuffer var1 = this.getBuffer();
      var1.putByte((byte)71);
      var1.putFloat((float)this.theCanvas.getWidth());
      var1.putFloat((float)this.theCanvas.getHeight());
   }

   private void reset() {
      GrowableDataBuffer var1 = this.getBuffer();
      if (var1.writeValuePosition() > 1024 || this.theCanvas.isRendererFallingBehind()) {
         var1.reset();
         var1.putByte((byte)70);
         this.updateDimensions();
         this.txdirty = true;
         this.pathDirty = true;
         State var2 = this.curState;
         int var3 = this.curState.numClipPaths;
         this.curState = new State();

         for(int var4 = 0; var4 < var3; ++var4) {
            Path2D var5 = (Path2D)this.clipStack.get(var4);
            var1.putByte((byte)13);
            var1.putObject(var5);
         }

         this.curState.numClipPaths = var3;
         var2.restore(this);
      }

   }

   private void resetIfCovers(Paint var1, double var2, double var4, double var6, double var8) {
      Affine2D var10 = this.curState.transform;
      if (var10.isTranslateOrIdentity()) {
         var2 += var10.getMxt();
         var4 += var10.getMyt();
         if (!(var2 > 0.0) && !(var4 > 0.0) && !(var2 + var6 < this.theCanvas.getWidth()) && !(var4 + var8 < this.theCanvas.getHeight())) {
            if (var1 != null) {
               if (this.curState.blendop != BlendMode.SRC_OVER) {
                  return;
               }

               if (!var1.isOpaque() || this.curState.globalAlpha < 1.0) {
                  return;
               }
            }

            if (this.curState.numClipPaths <= 0) {
               if (this.curState.effect == null) {
                  this.reset();
               }
            }
         }
      }
   }

   public Canvas getCanvas() {
      return this.theCanvas;
   }

   public void save() {
      this.stateStack.push(this.curState.copy());
   }

   public void restore() {
      if (!this.stateStack.isEmpty()) {
         State var1 = (State)this.stateStack.pop();
         var1.restore(this);
         this.txdirty = true;
      }

   }

   public void translate(double var1, double var3) {
      this.curState.transform.translate(var1, var3);
      this.txdirty = true;
   }

   public void scale(double var1, double var3) {
      this.curState.transform.scale(var1, var3);
      this.txdirty = true;
   }

   public void rotate(double var1) {
      this.curState.transform.rotate(Math.toRadians(var1));
      this.txdirty = true;
   }

   public void transform(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.curState.transform.concatenate(var1, var5, var9, var3, var7, var11);
      this.txdirty = true;
   }

   public void transform(Affine var1) {
      if (var1 != null) {
         this.curState.transform.concatenate(var1.getMxx(), var1.getMxy(), var1.getTx(), var1.getMyx(), var1.getMyy(), var1.getTy());
         this.txdirty = true;
      }
   }

   public void setTransform(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.curState.transform.setTransform(var1, var3, var5, var7, var9, var11);
      this.txdirty = true;
   }

   public void setTransform(Affine var1) {
      this.curState.transform.setTransform(var1.getMxx(), var1.getMyx(), var1.getMxy(), var1.getMyy(), var1.getTx(), var1.getTy());
      this.txdirty = true;
   }

   public Affine getTransform(Affine var1) {
      if (var1 == null) {
         var1 = new Affine();
      }

      var1.setMxx(this.curState.transform.getMxx());
      var1.setMxy(this.curState.transform.getMxy());
      var1.setMxz(0.0);
      var1.setTx(this.curState.transform.getMxt());
      var1.setMyx(this.curState.transform.getMyx());
      var1.setMyy(this.curState.transform.getMyy());
      var1.setMyz(0.0);
      var1.setTy(this.curState.transform.getMyt());
      var1.setMzx(0.0);
      var1.setMzy(0.0);
      var1.setMzz(1.0);
      var1.setTz(0.0);
      return var1;
   }

   public Affine getTransform() {
      return this.getTransform((Affine)null);
   }

   public void setGlobalAlpha(double var1) {
      if (this.curState.globalAlpha != var1) {
         this.curState.globalAlpha = var1;
         var1 = var1 > 1.0 ? 1.0 : (var1 < 0.0 ? 0.0 : var1);
         this.writeParam(var1, (byte)0);
      }

   }

   public double getGlobalAlpha() {
      return this.curState.globalAlpha;
   }

   public void setGlobalBlendMode(BlendMode var1) {
      if (var1 != null && var1 != this.curState.blendop) {
         GrowableDataBuffer var2 = this.getBuffer();
         this.curState.blendop = var1;
         var2.putByte((byte)1);
         var2.putObject(Blend.impl_getToolkitMode(var1));
      }

   }

   public BlendMode getGlobalBlendMode() {
      return this.curState.blendop;
   }

   public void setFill(Paint var1) {
      if (var1 != null && this.curState.fill != var1) {
         this.curState.fill = var1;
         this.writePaint(var1, (byte)2);
      }

   }

   public Paint getFill() {
      return this.curState.fill;
   }

   public void setStroke(Paint var1) {
      if (var1 != null && this.curState.stroke != var1) {
         this.curState.stroke = var1;
         this.writePaint(var1, (byte)3);
      }

   }

   public Paint getStroke() {
      return this.curState.stroke;
   }

   public void setLineWidth(double var1) {
      if (var1 > 0.0 && var1 < Double.POSITIVE_INFINITY && this.curState.linewidth != var1) {
         this.curState.linewidth = var1;
         this.writeParam(var1, (byte)4);
      }

   }

   public double getLineWidth() {
      return this.curState.linewidth;
   }

   public void setLineCap(StrokeLineCap var1) {
      if (var1 != null && this.curState.linecap != var1) {
         byte var2;
         switch (var1) {
            case BUTT:
               var2 = 0;
               break;
            case ROUND:
               var2 = 1;
               break;
            case SQUARE:
               var2 = 2;
               break;
            default:
               return;
         }

         this.curState.linecap = var1;
         this.writeParam(var2, (byte)5);
      }

   }

   public StrokeLineCap getLineCap() {
      return this.curState.linecap;
   }

   public void setLineJoin(StrokeLineJoin var1) {
      if (var1 != null && this.curState.linejoin != var1) {
         byte var2;
         switch (var1) {
            case MITER:
               var2 = 0;
               break;
            case BEVEL:
               var2 = 2;
               break;
            case ROUND:
               var2 = 1;
               break;
            default:
               return;
         }

         this.curState.linejoin = var1;
         this.writeParam(var2, (byte)6);
      }

   }

   public StrokeLineJoin getLineJoin() {
      return this.curState.linejoin;
   }

   public void setMiterLimit(double var1) {
      if (var1 > 0.0 && var1 < Double.POSITIVE_INFINITY && this.curState.miterlimit != var1) {
         this.curState.miterlimit = var1;
         this.writeParam(var1, (byte)7);
      }

   }

   public double getMiterLimit() {
      return this.curState.miterlimit;
   }

   public void setLineDashes(double... var1) {
      if (var1 != null && var1.length != 0) {
         boolean var2 = true;
         int var3 = 0;

         while(true) {
            if (var3 >= var1.length) {
               if (var2) {
                  if (this.curState.dashes == null) {
                     return;
                  }

                  this.curState.dashes = null;
               } else {
                  var3 = var1.length;
                  if ((var3 & 1) == 0) {
                     this.curState.dashes = Arrays.copyOf(var1, var3);
                  } else {
                     this.curState.dashes = Arrays.copyOf(var1, var3 * 2);
                     System.arraycopy(var1, 0, this.curState.dashes, var3, var3);
                  }
               }
               break;
            }

            double var4 = var1[var3];
            if (!(var4 >= 0.0) || !(var4 < Double.POSITIVE_INFINITY)) {
               return;
            }

            if (var4 > 0.0) {
               var2 = false;
            }

            ++var3;
         }
      } else {
         if (this.curState.dashes == null) {
            return;
         }

         this.curState.dashes = null;
      }

      GrowableDataBuffer var6 = this.getBuffer();
      var6.putByte((byte)17);
      var6.putObject(this.curState.dashes);
   }

   public double[] getLineDashes() {
      return this.curState.dashes == null ? null : Arrays.copyOf(this.curState.dashes, this.curState.dashes.length);
   }

   public void setLineDashOffset(double var1) {
      if (var1 > Double.NEGATIVE_INFINITY && var1 < Double.POSITIVE_INFINITY) {
         this.curState.dashOffset = var1;
         this.writeParam(var1, (byte)18);
      }

   }

   public double getLineDashOffset() {
      return this.curState.dashOffset;
   }

   public void setFont(Font var1) {
      if (var1 != null && this.curState.font != var1) {
         this.curState.font = var1;
         GrowableDataBuffer var2 = this.getBuffer();
         var2.putByte((byte)8);
         var2.putObject(var1.impl_getNativeFont());
      }

   }

   public Font getFont() {
      return this.curState.font;
   }

   public void setFontSmoothingType(FontSmoothingType var1) {
      if (var1 != null && var1 != this.curState.fontsmoothing) {
         this.curState.fontsmoothing = var1;
         this.writeParam((byte)var1.ordinal(), (byte)19);
      }

   }

   public FontSmoothingType getFontSmoothingType() {
      return this.curState.fontsmoothing;
   }

   public void setTextAlign(TextAlignment var1) {
      if (var1 != null && this.curState.textalign != var1) {
         byte var2;
         switch (var1) {
            case LEFT:
               var2 = 0;
               break;
            case CENTER:
               var2 = 1;
               break;
            case RIGHT:
               var2 = 2;
               break;
            case JUSTIFY:
               var2 = 3;
               break;
            default:
               return;
         }

         this.curState.textalign = var1;
         this.writeParam(var2, (byte)9);
      }

   }

   public TextAlignment getTextAlign() {
      return this.curState.textalign;
   }

   public void setTextBaseline(VPos var1) {
      if (var1 != null && this.curState.textbaseline != var1) {
         byte var2;
         switch (var1) {
            case TOP:
               var2 = 0;
               break;
            case CENTER:
               var2 = 1;
               break;
            case BASELINE:
               var2 = 2;
               break;
            case BOTTOM:
               var2 = 3;
               break;
            default:
               return;
         }

         this.curState.textbaseline = var1;
         this.writeParam(var2, (byte)10);
      }

   }

   public VPos getTextBaseline() {
      return this.curState.textbaseline;
   }

   public void fillText(String var1, double var2, double var4) {
      this.writeText(var1, var2, var4, 0.0, (byte)35);
   }

   public void strokeText(String var1, double var2, double var4) {
      this.writeText(var1, var2, var4, 0.0, (byte)36);
   }

   public void fillText(String var1, double var2, double var4, double var6) {
      if (!(var6 <= 0.0)) {
         this.writeText(var1, var2, var4, var6, (byte)35);
      }
   }

   public void strokeText(String var1, double var2, double var4, double var6) {
      if (!(var6 <= 0.0)) {
         this.writeText(var1, var2, var4, var6, (byte)36);
      }
   }

   public void setFillRule(FillRule var1) {
      if (var1 != null && this.curState.fillRule != var1) {
         byte var2;
         if (var1 == FillRule.EVEN_ODD) {
            var2 = 1;
         } else {
            var2 = 0;
         }

         this.curState.fillRule = var1;
         this.writeParam(var2, (byte)16);
      }

   }

   public FillRule getFillRule() {
      return this.curState.fillRule;
   }

   public void setImageSmoothing(boolean var1) {
      if (this.curState.imageSmoothing != var1) {
         this.curState.imageSmoothing = var1;
         GrowableDataBuffer var2 = this.getBuffer();
         var2.putByte((byte)20);
         var2.putBoolean(this.curState.imageSmoothing);
      }

   }

   public boolean isImageSmoothing() {
      return this.curState.imageSmoothing;
   }

   public void beginPath() {
      this.path.reset();
      this.markPathDirty();
   }

   public void moveTo(double var1, double var3) {
      this.coords[0] = (float)var1;
      this.coords[1] = (float)var3;
      this.curState.transform.transform(this.coords, 0, this.coords, 0, 1);
      this.path.moveTo(this.coords[0], this.coords[1]);
      this.markPathDirty();
   }

   public void lineTo(double var1, double var3) {
      this.coords[0] = (float)var1;
      this.coords[1] = (float)var3;
      this.curState.transform.transform(this.coords, 0, this.coords, 0, 1);
      if (this.path.getNumCommands() == 0) {
         this.path.moveTo(this.coords[0], this.coords[1]);
      }

      this.path.lineTo(this.coords[0], this.coords[1]);
      this.markPathDirty();
   }

   public void quadraticCurveTo(double var1, double var3, double var5, double var7) {
      this.coords[0] = (float)var1;
      this.coords[1] = (float)var3;
      this.coords[2] = (float)var5;
      this.coords[3] = (float)var7;
      this.curState.transform.transform(this.coords, 0, this.coords, 0, 2);
      if (this.path.getNumCommands() == 0) {
         this.path.moveTo(this.coords[0], this.coords[1]);
      }

      this.path.quadTo(this.coords[0], this.coords[1], this.coords[2], this.coords[3]);
      this.markPathDirty();
   }

   public void bezierCurveTo(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.coords[0] = (float)var1;
      this.coords[1] = (float)var3;
      this.coords[2] = (float)var5;
      this.coords[3] = (float)var7;
      this.coords[4] = (float)var9;
      this.coords[5] = (float)var11;
      this.curState.transform.transform(this.coords, 0, this.coords, 0, 3);
      if (this.path.getNumCommands() == 0) {
         this.path.moveTo(this.coords[0], this.coords[1]);
      }

      this.path.curveTo(this.coords[0], this.coords[1], this.coords[2], this.coords[3], this.coords[4], this.coords[5]);
      this.markPathDirty();
   }

   public void arcTo(double var1, double var3, double var5, double var7, double var9) {
      if (this.path.getNumCommands() == 0) {
         this.moveTo(var1, var3);
         this.lineTo(var1, var3);
      } else if (!this.tryArcTo((float)var1, (float)var3, (float)var5, (float)var7, (float)var9)) {
         this.lineTo(var1, var3);
      }

   }

   private static double lenSq(double var0, double var2, double var4, double var6) {
      var4 -= var0;
      var6 -= var2;
      return var4 * var4 + var6 * var6;
   }

   private boolean tryArcTo(float var1, float var2, float var3, float var4, float var5) {
      float var6;
      float var7;
      if (this.curState.transform.isTranslateOrIdentity()) {
         var6 = (float)((double)this.path.getCurrentX() - this.curState.transform.getMxt());
         var7 = (float)((double)this.path.getCurrentY() - this.curState.transform.getMyt());
      } else {
         this.coords[0] = this.path.getCurrentX();
         this.coords[1] = this.path.getCurrentY();

         try {
            this.curState.transform.inverseTransform(this.coords, 0, this.coords, 0, 1);
         } catch (NoninvertibleTransformException var73) {
            return false;
         }

         var6 = this.coords[0];
         var7 = this.coords[1];
      }

      double var8 = lenSq((double)var6, (double)var7, (double)var1, (double)var2);
      double var10 = lenSq((double)var1, (double)var2, (double)var3, (double)var4);
      double var12 = lenSq((double)var6, (double)var7, (double)var3, (double)var4);
      double var14 = Math.sqrt(var8);
      double var16 = Math.sqrt(var10);
      double var18 = var8 + var10 - var12;
      double var20 = 2.0 * var14 * var16;
      if (var20 != 0.0 && !(var5 <= 0.0F)) {
         double var22 = var18 / var20;
         double var24 = 1.0 + var22;
         if (var24 == 0.0) {
            return false;
         } else {
            double var26 = (1.0 - var22) / var24;
            double var28 = (double)var5 / Math.sqrt(var26);
            double var30 = (double)var1 + var28 / var14 * (double)(var6 - var1);
            double var32 = (double)var2 + var28 / var14 * (double)(var7 - var2);
            double var34 = (double)var1 + var28 / var16 * (double)(var3 - var1);
            double var36 = (double)var2 + var28 / var16 * (double)(var4 - var2);
            double var38 = (var30 + var34) / 2.0;
            double var40 = (var32 + var36) / 2.0;
            double var42 = lenSq(var38, var40, (double)var1, (double)var2);
            if (var42 == 0.0) {
               return false;
            } else {
               double var44 = lenSq(var38, var40, var30, var32) / var42;
               double var46 = var38 + (var38 - (double)var1) * var44;
               double var48 = var40 + (var40 - (double)var2) * var44;
               if (var46 == var46 && var48 == var48) {
                  if (var30 != (double)var6 || var32 != (double)var7) {
                     this.lineTo(var30, var32);
                  }

                  double var50 = Math.sqrt((1.0 - var22) / 2.0);
                  boolean var52 = (var32 - var48) * (var34 - var46) > (var36 - var48) * (var30 - var46);
                  double var53;
                  double var55;
                  double var57;
                  double var59;
                  double var61;
                  double var63;
                  if (var22 <= 0.0) {
                     var53 = Math.sqrt((1.0 + var22) / 2.0);
                     var55 = 1.3333333333333333 * var53 / (1.0 + var50);
                     if (var52) {
                        var55 = -var55;
                     }

                     var57 = var30 - var55 * (var32 - var48);
                     var59 = var32 + var55 * (var30 - var46);
                     var61 = var34 + var55 * (var36 - var48);
                     var63 = var36 - var55 * (var34 - var46);
                     this.bezierCurveTo(var57, var59, var61, var63, var34, var36);
                  } else {
                     var53 = Math.sqrt((1.0 - var50) / 2.0);
                     var55 = Math.sqrt((1.0 + var50) / 2.0);
                     var57 = 1.3333333333333333 * var53 / (1.0 + var55);
                     if (var52) {
                        var57 = -var57;
                     }

                     var59 = (double)var5 / Math.sqrt(var42);
                     var61 = var46 + ((double)var1 - var38) * var59;
                     var63 = var48 + ((double)var2 - var40) * var59;
                     double var65 = var30 - var57 * (var32 - var48);
                     double var67 = var32 + var57 * (var30 - var46);
                     double var69 = var61 + var57 * (var63 - var48);
                     double var71 = var63 - var57 * (var61 - var46);
                     this.bezierCurveTo(var65, var67, var69, var71, var61, var63);
                     var65 = var61 - var57 * (var63 - var48);
                     var67 = var63 + var57 * (var61 - var46);
                     var69 = var34 + var57 * (var36 - var48);
                     var71 = var36 - var57 * (var34 - var46);
                     this.bezierCurveTo(var65, var67, var69, var71, var34, var36);
                  }

                  return true;
               } else {
                  return false;
               }
            }
         }
      } else {
         return false;
      }
   }

   public void arc(double var1, double var3, double var5, double var7, double var9, double var11) {
      Arc2D var13 = new Arc2D((float)(var1 - var5), (float)(var3 - var7), (float)(var5 * 2.0), (float)(var7 * 2.0), (float)var9, (float)var11, 0);
      this.path.append(var13.getPathIterator(this.curState.transform), true);
      this.markPathDirty();
   }

   public void rect(double var1, double var3, double var5, double var7) {
      this.coords[0] = (float)var1;
      this.coords[1] = (float)var3;
      this.coords[2] = (float)var5;
      this.coords[3] = 0.0F;
      this.coords[4] = 0.0F;
      this.coords[5] = (float)var7;
      this.curState.transform.deltaTransform(this.coords, 0, this.coords, 0, 3);
      float var9 = this.coords[0] + (float)this.curState.transform.getMxt();
      float var10 = this.coords[1] + (float)this.curState.transform.getMyt();
      float var11 = this.coords[2];
      float var12 = this.coords[3];
      float var13 = this.coords[4];
      float var14 = this.coords[5];
      this.path.moveTo(var9, var10);
      this.path.lineTo(var9 + var11, var10 + var12);
      this.path.lineTo(var9 + var11 + var13, var10 + var12 + var14);
      this.path.lineTo(var9 + var13, var10 + var14);
      this.path.closePath();
      this.markPathDirty();
   }

   public void appendSVGPath(String var1) {
      if (var1 != null) {
         boolean var2 = true;
         boolean var3 = true;
         int var4 = 0;

         label60:
         while(var4 < var1.length()) {
            switch (var1.charAt(var4)) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
                  ++var4;
                  break;
               case 'M':
                  var3 = false;
                  var2 = false;
                  break label60;
               case 'm':
                  if (this.path.getNumCommands() == 0) {
                     var2 = false;
                  }

                  var3 = false;
               default:
                  break label60;
            }
         }

         Path2D var10 = new Path2D();
         if (var2 && this.path.getNumCommands() > 0) {
            float var5;
            float var6;
            if (this.curState.transform.isTranslateOrIdentity()) {
               var5 = (float)((double)this.path.getCurrentX() - this.curState.transform.getMxt());
               var6 = (float)((double)this.path.getCurrentY() - this.curState.transform.getMyt());
            } else {
               this.coords[0] = this.path.getCurrentX();
               this.coords[1] = this.path.getCurrentY();

               try {
                  this.curState.transform.inverseTransform(this.coords, 0, this.coords, 0, 1);
               } catch (NoninvertibleTransformException var9) {
               }

               var5 = this.coords[0];
               var6 = this.coords[1];
            }

            var10.moveTo(var5, var6);
         } else {
            var3 = false;
         }

         try {
            var10.appendSVGPath(var1);
            PathIterator var11 = var10.getPathIterator(this.curState.transform);
            if (var3) {
               var11.next();
            }

            this.path.append(var11, false);
         } catch (IllegalPathStateException | IllegalArgumentException var8) {
         }

      }
   }

   public void closePath() {
      if (this.path.getNumCommands() > 0) {
         this.path.closePath();
         this.markPathDirty();
      }

   }

   public void fill() {
      this.writePath((byte)47);
   }

   public void stroke() {
      this.writePath((byte)48);
   }

   public void clip() {
      Path2D var1 = new Path2D(this.path);
      this.clipStack.addLast(var1);
      ++this.curState.numClipPaths;
      GrowableDataBuffer var2 = this.getBuffer();
      var2.putByte((byte)13);
      var2.putObject(var1);
   }

   public boolean isPointInPath(double var1, double var3) {
      return this.path.contains((float)var1, (float)var3);
   }

   public void clearRect(double var1, double var3, double var5, double var7) {
      if (var5 != 0.0 && var7 != 0.0) {
         this.resetIfCovers((Paint)null, var1, var3, var5, var7);
         this.writeOp4(var1, var3, var5, var7, (byte)27);
      }

   }

   public void fillRect(double var1, double var3, double var5, double var7) {
      if (var5 != 0.0 && var7 != 0.0) {
         this.resetIfCovers(this.curState.fill, var1, var3, var5, var7);
         this.writeOp4(var1, var3, var5, var7, (byte)25);
      }

   }

   public void strokeRect(double var1, double var3, double var5, double var7) {
      if (var5 != 0.0 || var7 != 0.0) {
         this.writeOp4(var1, var3, var5, var7, (byte)26);
      }

   }

   public void fillOval(double var1, double var3, double var5, double var7) {
      if (var5 != 0.0 && var7 != 0.0) {
         this.writeOp4(var1, var3, var5, var7, (byte)29);
      }

   }

   public void strokeOval(double var1, double var3, double var5, double var7) {
      if (var5 != 0.0 || var7 != 0.0) {
         this.writeOp4(var1, var3, var5, var7, (byte)30);
      }

   }

   public void fillArc(double var1, double var3, double var5, double var7, double var9, double var11, ArcType var13) {
      if (var5 != 0.0 && var7 != 0.0 && var13 != null) {
         this.writeArcType(var13);
         this.writeOp6(var1, var3, var5, var7, var9, var11, (byte)33);
      }

   }

   public void strokeArc(double var1, double var3, double var5, double var7, double var9, double var11, ArcType var13) {
      if (var5 != 0.0 && var7 != 0.0 && var13 != null) {
         this.writeArcType(var13);
         this.writeOp6(var1, var3, var5, var7, var9, var11, (byte)34);
      }

   }

   public void fillRoundRect(double var1, double var3, double var5, double var7, double var9, double var11) {
      if (var5 != 0.0 && var7 != 0.0) {
         this.writeOp6(var1, var3, var5, var7, var9, var11, (byte)31);
      }

   }

   public void strokeRoundRect(double var1, double var3, double var5, double var7, double var9, double var11) {
      if (var5 != 0.0 && var7 != 0.0) {
         this.writeOp6(var1, var3, var5, var7, var9, var11, (byte)32);
      }

   }

   public void strokeLine(double var1, double var3, double var5, double var7) {
      this.writeOp4(var1, var3, var5, var7, (byte)28);
   }

   public void fillPolygon(double[] var1, double[] var2, int var3) {
      if (var3 >= 3) {
         this.writePoly(var1, var2, var3, true, (byte)47);
      }

   }

   public void strokePolygon(double[] var1, double[] var2, int var3) {
      if (var3 >= 2) {
         this.writePoly(var1, var2, var3, true, (byte)48);
      }

   }

   public void strokePolyline(double[] var1, double[] var2, int var3) {
      if (var3 >= 2) {
         this.writePoly(var1, var2, var3, false, (byte)48);
      }

   }

   public void drawImage(Image var1, double var2, double var4) {
      if (var1 != null) {
         double var6 = var1.getWidth();
         double var8 = var1.getHeight();
         this.writeImage(var1, var2, var4, var6, var8);
      }
   }

   public void drawImage(Image var1, double var2, double var4, double var6, double var8) {
      this.writeImage(var1, var2, var4, var6, var8);
   }

   public void drawImage(Image var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      this.writeImage(var1, var10, var12, var14, var16, var2, var4, var6, var8);
   }

   public PixelWriter getPixelWriter() {
      if (this.writer == null) {
         this.writer = new PixelWriter() {
            public PixelFormat getPixelFormat() {
               return PixelFormat.getByteBgraPreInstance();
            }

            private BytePixelSetter getSetter() {
               return ByteBgraPre.setter;
            }

            public void setArgb(int var1, int var2, int var3) {
               GrowableDataBuffer var4 = GraphicsContext.this.getBuffer();
               var4.putByte((byte)52);
               var4.putInt(var1);
               var4.putInt(var2);
               var4.putInt(var3);
            }

            public void setColor(int var1, int var2, Color var3) {
               if (var3 == null) {
                  throw new NullPointerException("Color cannot be null");
               } else {
                  int var4 = (int)Math.round(var3.getOpacity() * 255.0);
                  int var5 = (int)Math.round(var3.getRed() * 255.0);
                  int var6 = (int)Math.round(var3.getGreen() * 255.0);
                  int var7 = (int)Math.round(var3.getBlue() * 255.0);
                  this.setArgb(var1, var2, var4 << 24 | var5 << 16 | var6 << 8 | var7);
               }
            }

            private void writePixelBuffer(int var1, int var2, int var3, int var4, byte[] var5) {
               GrowableDataBuffer var6 = GraphicsContext.this.getBuffer();
               var6.putByte((byte)53);
               var6.putInt(var1);
               var6.putInt(var2);
               var6.putInt(var3);
               var6.putInt(var4);
               var6.putObject(var5);
            }

            private int[] checkBounds(int var1, int var2, int var3, int var4, PixelFormat var5, int var6) {
               int var7 = (int)Math.ceil(GraphicsContext.this.theCanvas.getWidth());
               int var8 = (int)Math.ceil(GraphicsContext.this.theCanvas.getHeight());
               if (var1 >= 0 && var2 >= 0 && var1 + var3 <= var7 && var2 + var4 <= var8) {
                  return null;
               } else {
                  int var9 = 0;
                  if (var1 < 0) {
                     var3 += var1;
                     if (var3 < 0) {
                        return null;
                     }

                     if (var5 != null) {
                        switch (var5.getType()) {
                           case BYTE_BGRA:
                           case BYTE_BGRA_PRE:
                              var9 -= var1 * 4;
                              break;
                           case BYTE_RGB:
                              var9 -= var1 * 3;
                              break;
                           case BYTE_INDEXED:
                           case INT_ARGB:
                           case INT_ARGB_PRE:
                              var9 -= var1;
                              break;
                           default:
                              throw new InternalError("unknown Pixel Format");
                        }
                     }

                     var1 = 0;
                  }

                  if (var2 < 0) {
                     var4 += var2;
                     if (var4 < 0) {
                        return null;
                     }

                     var9 -= var2 * var6;
                     var2 = 0;
                  }

                  if (var1 + var3 > var7) {
                     var3 = var7 - var1;
                     if (var3 < 0) {
                        return null;
                     }
                  }

                  if (var2 + var4 > var8) {
                     var4 = var8 - var2;
                     if (var4 < 0) {
                        return null;
                     }
                  }

                  return new int[]{var1, var2, var3, var4, var9};
               }
            }

            public void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, Buffer var6, int var7) {
               if (var5 == null) {
                  throw new NullPointerException("PixelFormat cannot be null");
               } else if (var6 == null) {
                  throw new NullPointerException("Buffer cannot be null");
               } else if (var3 > 0 && var4 > 0) {
                  int var8 = var6.position();
                  int[] var9 = this.checkBounds(var1, var2, var3, var4, var5, var7);
                  if (var9 != null) {
                     var1 = var9[0];
                     var2 = var9[1];
                     var3 = var9[2];
                     var4 = var9[3];
                     var8 += var9[4];
                  }

                  byte[] var10 = new byte[var3 * var4 * 4];
                  ByteBuffer var11 = ByteBuffer.wrap(var10);
                  PixelGetter var12 = PixelUtils.getGetter(var5);
                  PixelConverter var13 = PixelUtils.getConverter(var12, this.getSetter());
                  var13.convert(var6, var8, var7, var11, 0, var3 * 4, var3, var4);
                  this.writePixelBuffer(var1, var2, var3, var4, var10);
               }
            }

            public void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, byte[] var6, int var7, int var8) {
               if (var5 == null) {
                  throw new NullPointerException("PixelFormat cannot be null");
               } else if (var6 == null) {
                  throw new NullPointerException("Buffer cannot be null");
               } else if (var3 > 0 && var4 > 0) {
                  int[] var9 = this.checkBounds(var1, var2, var3, var4, var5, var8);
                  if (var9 != null) {
                     var1 = var9[0];
                     var2 = var9[1];
                     var3 = var9[2];
                     var4 = var9[3];
                     var7 += var9[4];
                  }

                  byte[] var10 = new byte[var3 * var4 * 4];
                  BytePixelGetter var11 = PixelUtils.getByteGetter(var5);
                  ByteToBytePixelConverter var12 = PixelUtils.getB2BConverter(var11, this.getSetter());
                  var12.convert((byte[])var6, var7, var8, (byte[])var10, 0, var3 * 4, var3, var4);
                  this.writePixelBuffer(var1, var2, var3, var4, var10);
               }
            }

            public void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, int[] var6, int var7, int var8) {
               if (var5 == null) {
                  throw new NullPointerException("PixelFormat cannot be null");
               } else if (var6 == null) {
                  throw new NullPointerException("Buffer cannot be null");
               } else if (var3 > 0 && var4 > 0) {
                  int[] var9 = this.checkBounds(var1, var2, var3, var4, var5, var8);
                  if (var9 != null) {
                     var1 = var9[0];
                     var2 = var9[1];
                     var3 = var9[2];
                     var4 = var9[3];
                     var7 += var9[4];
                  }

                  byte[] var10 = new byte[var3 * var4 * 4];
                  IntPixelGetter var11 = PixelUtils.getIntGetter(var5);
                  IntToBytePixelConverter var12 = PixelUtils.getI2BConverter(var11, this.getSetter());
                  var12.convert((int[])var6, var7, var8, (byte[])var10, 0, var3 * 4, var3, var4);
                  this.writePixelBuffer(var1, var2, var3, var4, var10);
               }
            }

            public void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7) {
               if (var5 == null) {
                  throw new NullPointerException("Reader cannot be null");
               } else if (var3 > 0 && var4 > 0) {
                  int[] var8 = this.checkBounds(var1, var2, var3, var4, (PixelFormat)null, 0);
                  if (var8 != null) {
                     int var9 = var8[0];
                     int var10 = var8[1];
                     var6 += var9 - var1;
                     var7 += var10 - var2;
                     var1 = var9;
                     var2 = var10;
                     var3 = var8[2];
                     var4 = var8[3];
                  }

                  byte[] var11 = new byte[var3 * var4 * 4];
                  var5.getPixels(var6, var7, var3, var4, PixelFormat.getByteBgraPreInstance(), (byte[])var11, 0, var3 * 4);
                  this.writePixelBuffer(var1, var2, var3, var4, var11);
               }
            }
         };
      }

      return this.writer;
   }

   public void setEffect(Effect var1) {
      GrowableDataBuffer var2 = this.getBuffer();
      var2.putByte((byte)12);
      if (var1 == null) {
         this.curState.effect = null;
         var2.putObject((Object)null);
      } else {
         this.curState.effect = var1.impl_copy();
         this.curState.effect.impl_sync();
         var2.putObject(this.curState.effect.impl_getImpl());
      }

   }

   public Effect getEffect(Effect var1) {
      return this.curState.effect == null ? null : this.curState.effect.impl_copy();
   }

   public void applyEffect(Effect var1) {
      if (var1 != null) {
         GrowableDataBuffer var2 = this.getBuffer();
         var2.putByte((byte)60);
         Effect var3 = var1.impl_copy();
         var3.impl_sync();
         var2.putObject(var3.impl_getImpl());
      }
   }

   static class State {
      double globalAlpha;
      BlendMode blendop;
      Affine2D transform;
      Paint fill;
      Paint stroke;
      double linewidth;
      StrokeLineCap linecap;
      StrokeLineJoin linejoin;
      double miterlimit;
      double[] dashes;
      double dashOffset;
      int numClipPaths;
      Font font;
      FontSmoothingType fontsmoothing;
      TextAlignment textalign;
      VPos textbaseline;
      Effect effect;
      FillRule fillRule;
      boolean imageSmoothing = true;

      State() {
         this.init();
      }

      final void init() {
         this.set(1.0, BlendMode.SRC_OVER, new Affine2D(), Color.BLACK, Color.BLACK, 1.0, StrokeLineCap.SQUARE, StrokeLineJoin.MITER, 10.0, (double[])null, 0.0, 0, Font.getDefault(), FontSmoothingType.GRAY, TextAlignment.LEFT, VPos.BASELINE, (Effect)null, FillRule.NON_ZERO, true);
      }

      State(State var1) {
         this.set(var1.globalAlpha, var1.blendop, new Affine2D(var1.transform), var1.fill, var1.stroke, var1.linewidth, var1.linecap, var1.linejoin, var1.miterlimit, var1.dashes, var1.dashOffset, var1.numClipPaths, var1.font, var1.fontsmoothing, var1.textalign, var1.textbaseline, var1.effect, var1.fillRule, var1.imageSmoothing);
      }

      final void set(double var1, BlendMode var3, Affine2D var4, Paint var5, Paint var6, double var7, StrokeLineCap var9, StrokeLineJoin var10, double var11, double[] var13, double var14, int var16, Font var17, FontSmoothingType var18, TextAlignment var19, VPos var20, Effect var21, FillRule var22, boolean var23) {
         this.globalAlpha = var1;
         this.blendop = var3;
         this.transform = var4;
         this.fill = var5;
         this.stroke = var6;
         this.linewidth = var7;
         this.linecap = var9;
         this.linejoin = var10;
         this.miterlimit = var11;
         this.dashes = var13;
         this.dashOffset = var14;
         this.numClipPaths = var16;
         this.font = var17;
         this.fontsmoothing = var18;
         this.textalign = var19;
         this.textbaseline = var20;
         this.effect = var21;
         this.fillRule = var22;
         this.imageSmoothing = var23;
      }

      State copy() {
         return new State(this);
      }

      void restore(GraphicsContext var1) {
         var1.setGlobalAlpha(this.globalAlpha);
         var1.setGlobalBlendMode(this.blendop);
         var1.setTransform(this.transform.getMxx(), this.transform.getMyx(), this.transform.getMxy(), this.transform.getMyy(), this.transform.getMxt(), this.transform.getMyt());
         var1.setFill(this.fill);
         var1.setStroke(this.stroke);
         var1.setLineWidth(this.linewidth);
         var1.setLineCap(this.linecap);
         var1.setLineJoin(this.linejoin);
         var1.setMiterLimit(this.miterlimit);
         var1.setLineDashes(this.dashes);
         var1.setLineDashOffset(this.dashOffset);
         GrowableDataBuffer var2 = var1.getBuffer();

         while(var1.curState.numClipPaths > this.numClipPaths) {
            --var1.curState.numClipPaths;
            var1.clipStack.removeLast();
            var2.putByte((byte)14);
         }

         var1.setFillRule(this.fillRule);
         var1.setFont(this.font);
         var1.setFontSmoothingType(this.fontsmoothing);
         var1.setTextAlign(this.textalign);
         var1.setTextBaseline(this.textbaseline);
         var1.setEffect(this.effect);
         var1.setImageSmoothing(this.imageSmoothing);
      }
   }
}
