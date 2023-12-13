package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLStyleElement;
import org.w3c.dom.stylesheets.StyleSheet;

public class HTMLStyleElementImpl extends HTMLElementImpl implements HTMLStyleElement {
   HTMLStyleElementImpl(long var1) {
      super(var1);
   }

   static HTMLStyleElement getImpl(long var0) {
      return (HTMLStyleElement)create(var0);
   }

   public boolean getDisabled() {
      return getDisabledImpl(this.getPeer());
   }

   static native boolean getDisabledImpl(long var0);

   public void setDisabled(boolean var1) {
      setDisabledImpl(this.getPeer(), var1);
   }

   static native void setDisabledImpl(long var0, boolean var2);

   public String getMedia() {
      return getMediaImpl(this.getPeer());
   }

   static native String getMediaImpl(long var0);

   public void setMedia(String var1) {
      setMediaImpl(this.getPeer(), var1);
   }

   static native void setMediaImpl(long var0, String var2);

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public void setType(String var1) {
      setTypeImpl(this.getPeer(), var1);
   }

   static native void setTypeImpl(long var0, String var2);

   public StyleSheet getSheet() {
      return StyleSheetImpl.getImpl(getSheetImpl(this.getPeer()));
   }

   static native long getSheetImpl(long var0);
}
