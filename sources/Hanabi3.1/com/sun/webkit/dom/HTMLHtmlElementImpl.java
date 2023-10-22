package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLHtmlElement;

public class HTMLHtmlElementImpl extends HTMLElementImpl implements HTMLHtmlElement {
   HTMLHtmlElementImpl(long var1) {
      super(var1);
   }

   static HTMLHtmlElement getImpl(long var0) {
      return (HTMLHtmlElement)create(var0);
   }

   public String getVersion() {
      return getVersionImpl(this.getPeer());
   }

   static native String getVersionImpl(long var0);

   public void setVersion(String var1) {
      setVersionImpl(this.getPeer(), var1);
   }

   static native void setVersionImpl(long var0, String var2);

   public String getManifest() {
      return getManifestImpl(this.getPeer());
   }

   static native String getManifestImpl(long var0);

   public void setManifest(String var1) {
      setManifestImpl(this.getPeer(), var1);
   }

   static native void setManifestImpl(long var0, String var2);
}
