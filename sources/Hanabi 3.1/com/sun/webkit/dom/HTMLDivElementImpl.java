package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLDivElement;

public class HTMLDivElementImpl extends HTMLElementImpl implements HTMLDivElement {
   HTMLDivElementImpl(long var1) {
      super(var1);
   }

   static HTMLDivElement getImpl(long var0) {
      return (HTMLDivElement)create(var0);
   }

   public String getAlign() {
      return getAlignImpl(this.getPeer());
   }

   static native String getAlignImpl(long var0);

   public void setAlign(String var1) {
      setAlignImpl(this.getPeer(), var1);
   }

   static native void setAlignImpl(long var0, String var2);
}
