package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLQuoteElement;

public class HTMLQuoteElementImpl extends HTMLElementImpl implements HTMLQuoteElement {
   HTMLQuoteElementImpl(long var1) {
      super(var1);
   }

   static HTMLQuoteElement getImpl(long var0) {
      return (HTMLQuoteElement)create(var0);
   }

   public String getCite() {
      return getCiteImpl(this.getPeer());
   }

   static native String getCiteImpl(long var0);

   public void setCite(String var1) {
      setCiteImpl(this.getPeer(), var1);
   }

   static native void setCiteImpl(long var0, String var2);
}
