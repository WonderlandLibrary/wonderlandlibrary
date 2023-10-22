package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLTableColElement;

public class HTMLTableColElementImpl extends HTMLElementImpl implements HTMLTableColElement {
   HTMLTableColElementImpl(long var1) {
      super(var1);
   }

   static HTMLTableColElement getImpl(long var0) {
      return (HTMLTableColElement)create(var0);
   }

   public String getAlign() {
      return getAlignImpl(this.getPeer());
   }

   static native String getAlignImpl(long var0);

   public void setAlign(String var1) {
      setAlignImpl(this.getPeer(), var1);
   }

   static native void setAlignImpl(long var0, String var2);

   public String getCh() {
      return getChImpl(this.getPeer());
   }

   static native String getChImpl(long var0);

   public void setCh(String var1) {
      setChImpl(this.getPeer(), var1);
   }

   static native void setChImpl(long var0, String var2);

   public String getChOff() {
      return getChOffImpl(this.getPeer());
   }

   static native String getChOffImpl(long var0);

   public void setChOff(String var1) {
      setChOffImpl(this.getPeer(), var1);
   }

   static native void setChOffImpl(long var0, String var2);

   public int getSpan() {
      return getSpanImpl(this.getPeer());
   }

   static native int getSpanImpl(long var0);

   public void setSpan(int var1) {
      setSpanImpl(this.getPeer(), var1);
   }

   static native void setSpanImpl(long var0, int var2);

   public String getVAlign() {
      return getVAlignImpl(this.getPeer());
   }

   static native String getVAlignImpl(long var0);

   public void setVAlign(String var1) {
      setVAlignImpl(this.getPeer(), var1);
   }

   static native void setVAlignImpl(long var0, String var2);

   public String getWidth() {
      return getWidthImpl(this.getPeer());
   }

   static native String getWidthImpl(long var0);

   public void setWidth(String var1) {
      setWidthImpl(this.getPeer(), var1);
   }

   static native void setWidthImpl(long var0, String var2);
}
