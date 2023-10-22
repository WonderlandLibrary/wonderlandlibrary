package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLFontElement;

public class HTMLFontElementImpl extends HTMLElementImpl implements HTMLFontElement {
   HTMLFontElementImpl(long var1) {
      super(var1);
   }

   static HTMLFontElement getImpl(long var0) {
      return (HTMLFontElement)create(var0);
   }

   public String getColor() {
      return getColorImpl(this.getPeer());
   }

   static native String getColorImpl(long var0);

   public void setColor(String var1) {
      setColorImpl(this.getPeer(), var1);
   }

   static native void setColorImpl(long var0, String var2);

   public String getFace() {
      return getFaceImpl(this.getPeer());
   }

   static native String getFaceImpl(long var0);

   public void setFace(String var1) {
      setFaceImpl(this.getPeer(), var1);
   }

   static native void setFaceImpl(long var0, String var2);

   public String getSize() {
      return getSizeImpl(this.getPeer());
   }

   static native String getSizeImpl(long var0);

   public void setSize(String var1) {
      setSizeImpl(this.getPeer(), var1);
   }

   static native void setSizeImpl(long var0, String var2);
}
