package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLMenuElement;

public class HTMLMenuElementImpl extends HTMLElementImpl implements HTMLMenuElement {
   HTMLMenuElementImpl(long var1) {
      super(var1);
   }

   static HTMLMenuElement getImpl(long var0) {
      return (HTMLMenuElement)create(var0);
   }

   public boolean getCompact() {
      return getCompactImpl(this.getPeer());
   }

   static native boolean getCompactImpl(long var0);

   public void setCompact(boolean var1) {
      setCompactImpl(this.getPeer(), var1);
   }

   static native void setCompactImpl(long var0, boolean var2);
}
