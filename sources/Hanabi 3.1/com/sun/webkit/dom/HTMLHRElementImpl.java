package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLHRElement;

public class HTMLHRElementImpl extends HTMLElementImpl implements HTMLHRElement {
   HTMLHRElementImpl(long var1) {
      super(var1);
   }

   static HTMLHRElement getImpl(long var0) {
      return (HTMLHRElement)create(var0);
   }

   public String getAlign() {
      return getAlignImpl(this.getPeer());
   }

   static native String getAlignImpl(long var0);

   public void setAlign(String var1) {
      setAlignImpl(this.getPeer(), var1);
   }

   static native void setAlignImpl(long var0, String var2);

   public boolean getNoShade() {
      return getNoShadeImpl(this.getPeer());
   }

   static native boolean getNoShadeImpl(long var0);

   public void setNoShade(boolean var1) {
      setNoShadeImpl(this.getPeer(), var1);
   }

   static native void setNoShadeImpl(long var0, boolean var2);

   public String getSize() {
      return getSizeImpl(this.getPeer());
   }

   static native String getSizeImpl(long var0);

   public void setSize(String var1) {
      setSizeImpl(this.getPeer(), var1);
   }

   static native void setSizeImpl(long var0, String var2);

   public String getWidth() {
      return getWidthImpl(this.getPeer());
   }

   static native String getWidthImpl(long var0);

   public void setWidth(String var1) {
      setWidthImpl(this.getPeer(), var1);
   }

   static native void setWidthImpl(long var0, String var2);
}
