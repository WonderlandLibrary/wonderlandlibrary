package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLScriptElement;

public class HTMLScriptElementImpl extends HTMLElementImpl implements HTMLScriptElement {
   HTMLScriptElementImpl(long var1) {
      super(var1);
   }

   static HTMLScriptElement getImpl(long var0) {
      return (HTMLScriptElement)create(var0);
   }

   public String getText() {
      return getTextImpl(this.getPeer());
   }

   static native String getTextImpl(long var0);

   public void setText(String var1) {
      setTextImpl(this.getPeer(), var1);
   }

   static native void setTextImpl(long var0, String var2);

   public String getHtmlFor() {
      return getHtmlForImpl(this.getPeer());
   }

   static native String getHtmlForImpl(long var0);

   public void setHtmlFor(String var1) {
      setHtmlForImpl(this.getPeer(), var1);
   }

   static native void setHtmlForImpl(long var0, String var2);

   public String getEvent() {
      return getEventImpl(this.getPeer());
   }

   static native String getEventImpl(long var0);

   public void setEvent(String var1) {
      setEventImpl(this.getPeer(), var1);
   }

   static native void setEventImpl(long var0, String var2);

   public String getCharset() {
      return getCharsetImpl(this.getPeer());
   }

   static native String getCharsetImpl(long var0);

   public void setCharset(String var1) {
      setCharsetImpl(this.getPeer(), var1);
   }

   static native void setCharsetImpl(long var0, String var2);

   public boolean getAsync() {
      return getAsyncImpl(this.getPeer());
   }

   static native boolean getAsyncImpl(long var0);

   public void setAsync(boolean var1) {
      setAsyncImpl(this.getPeer(), var1);
   }

   static native void setAsyncImpl(long var0, boolean var2);

   public boolean getDefer() {
      return getDeferImpl(this.getPeer());
   }

   static native boolean getDeferImpl(long var0);

   public void setDefer(boolean var1) {
      setDeferImpl(this.getPeer(), var1);
   }

   static native void setDeferImpl(long var0, boolean var2);

   public String getSrc() {
      return getSrcImpl(this.getPeer());
   }

   static native String getSrcImpl(long var0);

   public void setSrc(String var1) {
      setSrcImpl(this.getPeer(), var1);
   }

   static native void setSrcImpl(long var0, String var2);

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public void setType(String var1) {
      setTypeImpl(this.getPeer(), var1);
   }

   static native void setTypeImpl(long var0, String var2);

   public String getCrossOrigin() {
      return getCrossOriginImpl(this.getPeer());
   }

   static native String getCrossOriginImpl(long var0);

   public void setCrossOrigin(String var1) {
      setCrossOriginImpl(this.getPeer(), var1);
   }

   static native void setCrossOriginImpl(long var0, String var2);
}
