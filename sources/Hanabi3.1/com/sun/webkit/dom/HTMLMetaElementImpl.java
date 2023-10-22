package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLMetaElement;

public class HTMLMetaElementImpl extends HTMLElementImpl implements HTMLMetaElement {
   HTMLMetaElementImpl(long var1) {
      super(var1);
   }

   static HTMLMetaElement getImpl(long var0) {
      return (HTMLMetaElement)create(var0);
   }

   public String getContent() {
      return getContentImpl(this.getPeer());
   }

   static native String getContentImpl(long var0);

   public void setContent(String var1) {
      setContentImpl(this.getPeer(), var1);
   }

   static native void setContentImpl(long var0, String var2);

   public String getHttpEquiv() {
      return getHttpEquivImpl(this.getPeer());
   }

   static native String getHttpEquivImpl(long var0);

   public void setHttpEquiv(String var1) {
      setHttpEquivImpl(this.getPeer(), var1);
   }

   static native void setHttpEquivImpl(long var0, String var2);

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public void setName(String var1) {
      setNameImpl(this.getPeer(), var1);
   }

   static native void setNameImpl(long var0, String var2);

   public String getScheme() {
      return getSchemeImpl(this.getPeer());
   }

   static native String getSchemeImpl(long var0);

   public void setScheme(String var1) {
      setSchemeImpl(this.getPeer(), var1);
   }

   static native void setSchemeImpl(long var0, String var2);
}
