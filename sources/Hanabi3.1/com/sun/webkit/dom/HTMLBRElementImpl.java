package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLBRElement;

public class HTMLBRElementImpl extends HTMLElementImpl implements HTMLBRElement {
   HTMLBRElementImpl(long var1) {
      super(var1);
   }

   static HTMLBRElement getImpl(long var0) {
      return (HTMLBRElement)create(var0);
   }

   public String getClear() {
      return getClearImpl(this.getPeer());
   }

   static native String getClearImpl(long var0);

   public void setClear(String var1) {
      setClearImpl(this.getPeer(), var1);
   }

   static native void setClearImpl(long var0, String var2);
}
