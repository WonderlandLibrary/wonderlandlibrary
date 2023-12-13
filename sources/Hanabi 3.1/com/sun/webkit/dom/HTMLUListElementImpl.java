package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLUListElement;

public class HTMLUListElementImpl extends HTMLElementImpl implements HTMLUListElement {
   HTMLUListElementImpl(long var1) {
      super(var1);
   }

   static HTMLUListElement getImpl(long var0) {
      return (HTMLUListElement)create(var0);
   }

   public boolean getCompact() {
      return getCompactImpl(this.getPeer());
   }

   static native boolean getCompactImpl(long var0);

   public void setCompact(boolean var1) {
      setCompactImpl(this.getPeer(), var1);
   }

   static native void setCompactImpl(long var0, boolean var2);

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public void setType(String var1) {
      setTypeImpl(this.getPeer(), var1);
   }

   static native void setTypeImpl(long var0, String var2);
}
