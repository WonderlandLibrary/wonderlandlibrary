package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLBaseElement;

public class HTMLBaseElementImpl extends HTMLElementImpl implements HTMLBaseElement {
   HTMLBaseElementImpl(long var1) {
      super(var1);
   }

   static HTMLBaseElement getImpl(long var0) {
      return (HTMLBaseElement)create(var0);
   }

   public String getHref() {
      return getHrefImpl(this.getPeer());
   }

   static native String getHrefImpl(long var0);

   public void setHref(String var1) {
      setHrefImpl(this.getPeer(), var1);
   }

   static native void setHrefImpl(long var0, String var2);

   public String getTarget() {
      return getTargetImpl(this.getPeer());
   }

   static native String getTargetImpl(long var0);

   public void setTarget(String var1) {
      setTargetImpl(this.getPeer(), var1);
   }

   static native void setTargetImpl(long var0, String var2);
}
