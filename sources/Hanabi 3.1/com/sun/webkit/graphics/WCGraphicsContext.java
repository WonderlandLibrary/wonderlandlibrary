package com.sun.webkit.graphics;

import com.sun.prism.paint.Color;
import java.nio.ByteBuffer;

public abstract class WCGraphicsContext {
   public static final int COMPOSITE_CLEAR = 0;
   public static final int COMPOSITE_COPY = 1;
   public static final int COMPOSITE_SOURCE_OVER = 2;
   public static final int COMPOSITE_SOURCE_IN = 3;
   public static final int COMPOSITE_SOURCE_OUT = 4;
   public static final int COMPOSITE_SOURCE_ATOP = 5;
   public static final int COMPOSITE_DESTINATION_OVER = 6;
   public static final int COMPOSITE_DESTINATION_IN = 7;
   public static final int COMPOSITE_DESTINATION_OUT = 8;
   public static final int COMPOSITE_DESTINATION_ATOP = 9;
   public static final int COMPOSITE_XOR = 10;
   public static final int COMPOSITE_PLUS_DARKER = 11;
   public static final int COMPOSITE_HIGHLIGHT = 12;
   public static final int COMPOSITE_PLUS_LIGHTER = 13;

   public abstract void fillRect(float var1, float var2, float var3, float var4, Color var5);

   public abstract void clearRect(float var1, float var2, float var3, float var4);

   public abstract void setFillColor(Color var1);

   public abstract void setFillGradient(WCGradient var1);

   public abstract void fillRoundedRect(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, Color var13);

   public abstract void setTextMode(boolean var1, boolean var2, boolean var3);

   public abstract void setFontSmoothingType(int var1);

   public abstract int getFontSmoothingType();

   public abstract void setStrokeStyle(int var1);

   public abstract void setStrokeColor(Color var1);

   public abstract void setStrokeWidth(float var1);

   public abstract void setStrokeGradient(WCGradient var1);

   public abstract void setLineDash(float var1, float... var2);

   public abstract void setLineCap(int var1);

   public abstract void setLineJoin(int var1);

   public abstract void setMiterLimit(float var1);

   public abstract void drawPolygon(WCPath var1, boolean var2);

   public abstract void drawLine(int var1, int var2, int var3, int var4);

   public abstract void drawImage(WCImage var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9);

   public abstract void drawIcon(WCIcon var1, int var2, int var3);

   public abstract void drawPattern(WCImage var1, WCRectangle var2, WCTransform var3, WCPoint var4, WCRectangle var5);

   public abstract void drawBitmapImage(ByteBuffer var1, int var2, int var3, int var4, int var5);

   public abstract void translate(float var1, float var2);

   public abstract void scale(float var1, float var2);

   public abstract void rotate(float var1);

   public abstract void setPerspectiveTransform(WCTransform var1);

   public abstract void setTransform(WCTransform var1);

   public abstract WCTransform getTransform();

   public abstract void concatTransform(WCTransform var1);

   public abstract void saveState();

   public abstract void restoreState();

   public abstract void setClip(WCPath var1, boolean var2);

   public abstract void setClip(int var1, int var2, int var3, int var4);

   public abstract void setClip(WCRectangle var1);

   public abstract WCRectangle getClip();

   public abstract void drawRect(int var1, int var2, int var3, int var4);

   public abstract void setComposite(int var1);

   public abstract void strokeArc(int var1, int var2, int var3, int var4, int var5, int var6);

   public abstract void drawEllipse(int var1, int var2, int var3, int var4);

   public abstract void drawFocusRing(int var1, int var2, int var3, int var4, Color var5);

   public abstract void setAlpha(float var1);

   public abstract float getAlpha();

   public abstract void beginTransparencyLayer(float var1);

   public abstract void endTransparencyLayer();

   public abstract void strokePath(WCPath var1);

   public abstract void strokeRect(float var1, float var2, float var3, float var4, float var5);

   public abstract void fillPath(WCPath var1);

   public abstract void setShadow(float var1, float var2, float var3, Color var4);

   public abstract void drawString(WCFont var1, String var2, boolean var3, int var4, int var5, float var6, float var7);

   public abstract void drawString(WCFont var1, int[] var2, float[] var3, float var4, float var5);

   public abstract void drawWidget(RenderTheme var1, Ref var2, int var3, int var4);

   public abstract void drawScrollbar(ScrollBarTheme var1, Ref var2, int var3, int var4, int var5, int var6);

   public abstract WCImage getImage();

   public abstract Object getPlatformGraphics();

   public abstract WCGradient createLinearGradient(WCPoint var1, WCPoint var2);

   public abstract WCGradient createRadialGradient(WCPoint var1, float var2, WCPoint var3, float var4);

   public abstract void flush();

   public abstract void dispose();
}
