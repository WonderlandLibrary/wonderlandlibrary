package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLParagraphElement;

public class HTMLParagraphElementImpl extends HTMLElementImpl implements HTMLParagraphElement {
   HTMLParagraphElementImpl(long var1) {
      super(var1);
   }

   static HTMLParagraphElement getImpl(long var0) {
      return (HTMLParagraphElement)create(var0);
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
