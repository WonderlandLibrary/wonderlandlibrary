package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLHeadElement;

public class HTMLHeadElementImpl extends HTMLElementImpl implements HTMLHeadElement {
   HTMLHeadElementImpl(long var1) {
      super(var1);
   }

   static HTMLHeadElement getImpl(long var0) {
      return (HTMLHeadElement)create(var0);
   }

   public String getProfile() {
      return getProfileImpl(this.getPeer());
   }

   static native String getProfileImpl(long var0);

   public void setProfile(String var1) {
      setProfileImpl(this.getPeer(), var1);
   }

   static native void setProfileImpl(long var0, String var2);
}
