package com.sun.webkit.graphics;

import com.sun.prism.paint.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

public final class GraphicsDecoder {
   public static final int FILLRECT_FFFFI = 0;
   public static final int SETFILLCOLOR = 1;
   public static final int SETSTROKESTYLE = 2;
   public static final int SETSTROKECOLOR = 3;
   public static final int SETSTROKEWIDTH = 4;
   public static final int DRAWPOLYGON = 6;
   public static final int DRAWLINE = 7;
   public static final int DRAWIMAGE = 8;
   public static final int DRAWICON = 9;
   public static final int DRAWPATTERN = 10;
   public static final int TRANSLATE = 11;
   public static final int SAVESTATE = 12;
   public static final int RESTORESTATE = 13;
   public static final int CLIP_PATH = 14;
   public static final int SETCLIP_IIII = 15;
   public static final int DRAWRECT = 16;
   public static final int SETCOMPOSITE = 17;
   public static final int STROKEARC = 18;
   public static final int DRAWELLIPSE = 19;
   public static final int DRAWFOCUSRING = 20;
   public static final int SETALPHA = 21;
   public static final int BEGINTRANSPARENCYLAYER = 22;
   public static final int ENDTRANSPARENCYLAYER = 23;
   public static final int STROKE_PATH = 24;
   public static final int FILL_PATH = 25;
   public static final int GETIMAGE = 26;
   public static final int SCALE = 27;
   public static final int SETSHADOW = 28;
   public static final int DRAWSTRING = 29;
   public static final int DRAWSTRING_FAST = 31;
   public static final int DRAWWIDGET = 33;
   public static final int DRAWSCROLLBAR = 34;
   public static final int CLEARRECT_FFFF = 36;
   public static final int STROKERECT_FFFFF = 37;
   public static final int RENDERMEDIAPLAYER = 38;
   public static final int CONCATTRANSFORM_FFFFFF = 39;
   public static final int COPYREGION = 40;
   public static final int DECODERQ = 41;
   public static final int SET_TRANSFORM = 42;
   public static final int ROTATE = 43;
   public static final int RENDERMEDIACONTROL = 44;
   public static final int RENDERMEDIA_TIMETRACK = 45;
   public static final int RENDERMEDIA_VOLUMETRACK = 46;
   public static final int FILLRECT_FFFF = 47;
   public static final int FILL_ROUNDED_RECT = 48;
   public static final int SET_FILL_GRADIENT = 49;
   public static final int SET_STROKE_GRADIENT = 50;
   public static final int SET_LINE_DASH = 51;
   public static final int SET_LINE_CAP = 52;
   public static final int SET_LINE_JOIN = 53;
   public static final int SET_MITER_LIMIT = 54;
   public static final int SET_TEXT_MODE = 55;
   public static final int SET_PERSPECTIVE_TRANSFORM = 56;
   private static final Logger log = Logger.getLogger(GraphicsDecoder.class.getName());

   static void decode(WCGraphicsManager var0, WCGraphicsContext var1, BufferData var2) {
      if (var1 != null) {
         ByteBuffer var3 = var2.getBuffer();
         var3.order(ByteOrder.nativeOrder());

         while(var3.remaining() > 0) {
            int var4 = var3.getInt();
            switch (var4) {
               case 0:
                  var1.fillRect(var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), getColor(var3));
                  break;
               case 1:
                  var1.setFillColor(getColor(var3));
                  break;
               case 2:
                  var1.setStrokeStyle(var3.getInt());
                  break;
               case 3:
                  var1.setStrokeColor(getColor(var3));
                  break;
               case 4:
                  var1.setStrokeWidth(var3.getFloat());
                  break;
               case 5:
               case 26:
               case 30:
               case 32:
               case 35:
               default:
                  log.fine("ERROR. Unknown primitive found");
                  break;
               case 6:
                  var1.drawPolygon(getPath(var0, var3), var3.getInt() == -1);
                  break;
               case 7:
                  var1.drawLine(var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt());
                  break;
               case 8:
                  drawImage(var1, var0.getRef(var3.getInt()), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat());
                  break;
               case 9:
                  var1.drawIcon((WCIcon)var0.getRef(var3.getInt()), var3.getInt(), var3.getInt());
                  break;
               case 10:
                  drawPattern(var1, var0.getRef(var3.getInt()), getRectangle(var3), (WCTransform)var0.getRef(var3.getInt()), getPoint(var3), getRectangle(var3));
                  break;
               case 11:
                  var1.translate(var3.getFloat(), var3.getFloat());
                  break;
               case 12:
                  var1.saveState();
                  break;
               case 13:
                  var1.restoreState();
                  break;
               case 14:
                  var1.setClip(getPath(var0, var3), var3.getInt() > 0);
                  break;
               case 15:
                  var1.setClip(var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt());
                  break;
               case 16:
                  var1.drawRect(var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt());
                  break;
               case 17:
                  var1.setComposite(var3.getInt());
                  break;
               case 18:
                  var1.strokeArc(var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt());
                  break;
               case 19:
                  var1.drawEllipse(var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt());
                  break;
               case 20:
                  var1.drawFocusRing(var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt(), getColor(var3));
                  break;
               case 21:
                  var1.setAlpha(var3.getFloat());
                  break;
               case 22:
                  var1.beginTransparencyLayer(var3.getFloat());
                  break;
               case 23:
                  var1.endTransparencyLayer();
                  break;
               case 24:
                  var1.strokePath(getPath(var0, var3));
                  break;
               case 25:
                  var1.fillPath(getPath(var0, var3));
                  break;
               case 27:
                  var1.scale(var3.getFloat(), var3.getFloat());
                  break;
               case 28:
                  var1.setShadow(var3.getFloat(), var3.getFloat(), var3.getFloat(), getColor(var3));
                  break;
               case 29:
                  var1.drawString((WCFont)var0.getRef(var3.getInt()), var2.getString(var3.getInt()), var3.getInt() == -1, var3.getInt(), var3.getInt(), var3.getFloat(), var3.getFloat());
                  break;
               case 31:
                  var1.drawString((WCFont)var0.getRef(var3.getInt()), var2.getIntArray(var3.getInt()), var2.getFloatArray(var3.getInt()), var3.getFloat(), var3.getFloat());
                  break;
               case 33:
                  var1.drawWidget((RenderTheme)((RenderTheme)var0.getRef(var3.getInt())), var0.getRef(var3.getInt()), var3.getInt(), var3.getInt());
                  break;
               case 34:
                  var1.drawScrollbar((ScrollBarTheme)((ScrollBarTheme)var0.getRef(var3.getInt())), var0.getRef(var3.getInt()), var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt());
                  break;
               case 36:
                  var1.clearRect(var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat());
                  break;
               case 37:
                  var1.strokeRect(var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat());
                  break;
               case 38:
                  WCMediaPlayer var5 = (WCMediaPlayer)var0.getRef(var3.getInt());
                  var5.render(var1, var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt());
                  break;
               case 39:
                  var1.concatTransform(new WCTransform((double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat()));
                  break;
               case 40:
                  WCPageBackBuffer var6 = (WCPageBackBuffer)var0.getRef(var3.getInt());
                  var6.copyArea(var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt());
                  break;
               case 41:
                  WCRenderQueue var7 = (WCRenderQueue)var0.getRef(var3.getInt());
                  var7.decode(var1.getFontSmoothingType());
                  break;
               case 42:
                  var1.setTransform(new WCTransform((double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat()));
                  break;
               case 43:
                  var1.rotate(var3.getFloat());
                  break;
               case 44:
                  RenderMediaControls.paintControl(var1, var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt());
                  break;
               case 45:
                  int var8 = var3.getInt();
                  float[] var9 = new float[var8 * 2];
                  var3.asFloatBuffer().get(var9);
                  var3.position(var3.position() + var8 * 4 * 2);
                  RenderMediaControls.paintTimeSliderTrack(var1, var3.getFloat(), var3.getFloat(), var9, var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt());
                  break;
               case 46:
                  RenderMediaControls.paintVolumeTrack(var1, var3.getFloat(), var3.getInt() != 0, var3.getInt(), var3.getInt(), var3.getInt(), var3.getInt());
                  break;
               case 47:
                  var1.fillRect(var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), (Color)null);
                  break;
               case 48:
                  var1.fillRoundedRect(var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), var3.getFloat(), getColor(var3));
                  break;
               case 49:
                  var1.setFillGradient(getGradient(var1, var3));
                  break;
               case 50:
                  var1.setStrokeGradient(getGradient(var1, var3));
                  break;
               case 51:
                  var1.setLineDash(var3.getFloat(), getFloatArray(var3));
                  break;
               case 52:
                  var1.setLineCap(var3.getInt());
                  break;
               case 53:
                  var1.setLineJoin(var3.getInt());
                  break;
               case 54:
                  var1.setMiterLimit(var3.getFloat());
                  break;
               case 55:
                  var1.setTextMode(getBoolean(var3), getBoolean(var3), getBoolean(var3));
                  break;
               case 56:
                  var1.setPerspectiveTransform(new WCTransform((double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat(), (double)var3.getFloat()));
            }
         }

      }
   }

   private static void drawPattern(WCGraphicsContext var0, Object var1, WCRectangle var2, WCTransform var3, WCPoint var4, WCRectangle var5) {
      WCImage var6 = WCImage.getImage(var1);
      if (var6 != null) {
         try {
            var0.drawPattern(var6, var2, var3, var4, var5);
         } catch (OutOfMemoryError var8) {
            var8.printStackTrace();
         }
      }

   }

   private static void drawImage(WCGraphicsContext var0, Object var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      WCImage var10 = WCImage.getImage(var1);
      if (var10 != null) {
         try {
            var0.drawImage(var10, var2, var3, var4, var5, var6, var7, var8, var9);
         } catch (OutOfMemoryError var12) {
            var12.printStackTrace();
         }
      }

   }

   private static boolean getBoolean(ByteBuffer var0) {
      return 0 != var0.getInt();
   }

   private static float[] getFloatArray(ByteBuffer var0) {
      float[] var1 = new float[var0.getInt()];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = var0.getFloat();
      }

      return var1;
   }

   private static WCPath getPath(WCGraphicsManager var0, ByteBuffer var1) {
      WCPath var2 = (WCPath)var0.getRef(var1.getInt());
      var2.setWindingRule(var1.getInt());
      return var2;
   }

   private static WCPoint getPoint(ByteBuffer var0) {
      return new WCPoint(var0.getFloat(), var0.getFloat());
   }

   private static WCRectangle getRectangle(ByteBuffer var0) {
      return new WCRectangle(var0.getFloat(), var0.getFloat(), var0.getFloat(), var0.getFloat());
   }

   private static Color getColor(ByteBuffer var0) {
      return new Color(var0.getFloat(), var0.getFloat(), var0.getFloat(), var0.getFloat());
   }

   private static WCGradient getGradient(WCGraphicsContext var0, ByteBuffer var1) {
      WCPoint var2 = getPoint(var1);
      WCPoint var3 = getPoint(var1);
      WCGradient var4 = getBoolean(var1) ? var0.createRadialGradient(var2, var1.getFloat(), var3, var1.getFloat()) : var0.createLinearGradient(var2, var3);
      boolean var5 = getBoolean(var1);
      int var6 = var1.getInt();
      if (var4 != null) {
         var4.setProportional(var5);
         var4.setSpreadMethod(var6);
      }

      int var7 = var1.getInt();

      for(int var8 = 0; var8 < var7; ++var8) {
         Color var9 = getColor(var1);
         float var10 = var1.getFloat();
         if (var4 != null) {
            var4.addStop(var9, var10);
         }
      }

      return var4;
   }
}
