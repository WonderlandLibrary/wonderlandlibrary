package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLTableCaptionElement;

public class HTMLTableCaptionElementImpl extends HTMLElementImpl implements HTMLTableCaptionElement {
   HTMLTableCaptionElementImpl(long var1) {
      super(var1);
   }

   static HTMLTableCaptionElement getImpl(long var0) {
      return (HTMLTableCaptionElement)create(var0);
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
