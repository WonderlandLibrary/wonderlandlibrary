package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;

public class CSSPrimitiveValueImpl extends CSSValueImpl implements CSSPrimitiveValue {
   public static final int CSS_UNKNOWN = 0;
   public static final int CSS_NUMBER = 1;
   public static final int CSS_PERCENTAGE = 2;
   public static final int CSS_EMS = 3;
   public static final int CSS_EXS = 4;
   public static final int CSS_PX = 5;
   public static final int CSS_CM = 6;
   public static final int CSS_MM = 7;
   public static final int CSS_IN = 8;
   public static final int CSS_PT = 9;
   public static final int CSS_PC = 10;
   public static final int CSS_DEG = 11;
   public static final int CSS_RAD = 12;
   public static final int CSS_GRAD = 13;
   public static final int CSS_MS = 14;
   public static final int CSS_S = 15;
   public static final int CSS_HZ = 16;
   public static final int CSS_KHZ = 17;
   public static final int CSS_DIMENSION = 18;
   public static final int CSS_STRING = 19;
   public static final int CSS_URI = 20;
   public static final int CSS_IDENT = 21;
   public static final int CSS_ATTR = 22;
   public static final int CSS_COUNTER = 23;
   public static final int CSS_RECT = 24;
   public static final int CSS_RGBCOLOR = 25;
   public static final int CSS_VW = 26;
   public static final int CSS_VH = 27;
   public static final int CSS_VMIN = 28;
   public static final int CSS_VMAX = 29;

   CSSPrimitiveValueImpl(long var1) {
      super(var1);
   }

   static CSSPrimitiveValue getImpl(long var0) {
      return (CSSPrimitiveValue)create(var0);
   }

   public short getPrimitiveType() {
      return getPrimitiveTypeImpl(this.getPeer());
   }

   static native short getPrimitiveTypeImpl(long var0);

   public void setFloatValue(short var1, float var2) throws DOMException {
      setFloatValueImpl(this.getPeer(), var1, var2);
   }

   static native void setFloatValueImpl(long var0, short var2, float var3);

   public float getFloatValue(short var1) throws DOMException {
      return getFloatValueImpl(this.getPeer(), var1);
   }

   static native float getFloatValueImpl(long var0, short var2);

   public void setStringValue(short var1, String var2) throws DOMException {
      setStringValueImpl(this.getPeer(), var1, var2);
   }

   static native void setStringValueImpl(long var0, short var2, String var3);

   public String getStringValue() throws DOMException {
      return getStringValueImpl(this.getPeer());
   }

   static native String getStringValueImpl(long var0);

   public Counter getCounterValue() throws DOMException {
      return CounterImpl.getImpl(getCounterValueImpl(this.getPeer()));
   }

   static native long getCounterValueImpl(long var0);

   public Rect getRectValue() throws DOMException {
      return RectImpl.getImpl(getRectValueImpl(this.getPeer()));
   }

   static native long getRectValueImpl(long var0);

   public RGBColor getRGBColorValue() throws DOMException {
      return RGBColorImpl.getImpl(getRGBColorValueImpl(this.getPeer()));
   }

   static native long getRGBColorValueImpl(long var0);
}
