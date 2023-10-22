package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLModElement;

public class HTMLModElementImpl extends HTMLElementImpl implements HTMLModElement {
   HTMLModElementImpl(long var1) {
      super(var1);
   }

   static HTMLModElement getImpl(long var0) {
      return (HTMLModElement)create(var0);
   }

   public String getCite() {
      return getCiteImpl(this.getPeer());
   }

   static native String getCiteImpl(long var0);

   public void setCite(String var1) {
      setCiteImpl(this.getPeer(), var1);
   }

   static native void setCiteImpl(long var0, String var2);

   public String getDateTime() {
      return getDateTimeImpl(this.getPeer());
   }

   static native String getDateTimeImpl(long var0);

   public void setDateTime(String var1) {
      setDateTimeImpl(this.getPeer(), var1);
   }

   static native void setDateTimeImpl(long var0, String var2);
}
