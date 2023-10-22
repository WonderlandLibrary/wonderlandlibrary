package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLTitleElement;

public class HTMLTitleElementImpl extends HTMLElementImpl implements HTMLTitleElement {
   HTMLTitleElementImpl(long var1) {
      super(var1);
   }

   static HTMLTitleElement getImpl(long var0) {
      return (HTMLTitleElement)create(var0);
   }

   public String getText() {
      return getTextImpl(this.getPeer());
   }

   static native String getTextImpl(long var0);

   public void setText(String var1) {
      setTextImpl(this.getPeer(), var1);
   }

   static native void setTextImpl(long var0, String var2);
}
