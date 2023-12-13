package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLHeadingElement;

public class HTMLHeadingElementImpl extends HTMLElementImpl implements HTMLHeadingElement {
   HTMLHeadingElementImpl(long var1) {
      super(var1);
   }

   static HTMLHeadingElement getImpl(long var0) {
      return (HTMLHeadingElement)create(var0);
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
