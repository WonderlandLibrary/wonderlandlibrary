package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLPreElement;

public class HTMLPreElementImpl extends HTMLElementImpl implements HTMLPreElement {
   HTMLPreElementImpl(long var1) {
      super(var1);
   }

   static HTMLPreElement getImpl(long var0) {
      return (HTMLPreElement)create(var0);
   }

   public int getWidth() {
      return getWidthImpl(this.getPeer());
   }

   static native int getWidthImpl(long var0);

   public void setWidth(int var1) {
      setWidthImpl(this.getPeer(), var1);
   }

   static native void setWidthImpl(long var0, int var2);

   public boolean getWrap() {
      return getWrapImpl(this.getPeer());
   }

   static native boolean getWrapImpl(long var0);

   public void setWrap(boolean var1) {
      setWrapImpl(this.getPeer(), var1);
   }

   static native void setWrapImpl(long var0, boolean var2);
}
