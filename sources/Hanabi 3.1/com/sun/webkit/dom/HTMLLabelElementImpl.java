package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLLabelElement;

public class HTMLLabelElementImpl extends HTMLElementImpl implements HTMLLabelElement {
   HTMLLabelElementImpl(long var1) {
      super(var1);
   }

   static HTMLLabelElement getImpl(long var0) {
      return (HTMLLabelElement)create(var0);
   }

   public HTMLFormElement getForm() {
      return HTMLFormElementImpl.getImpl(getFormImpl(this.getPeer()));
   }

   static native long getFormImpl(long var0);

   public String getHtmlFor() {
      return getHtmlForImpl(this.getPeer());
   }

   static native String getHtmlForImpl(long var0);

   public void setHtmlFor(String var1) {
      setHtmlForImpl(this.getPeer(), var1);
   }

   static native void setHtmlForImpl(long var0, String var2);

   public HTMLElement getControl() {
      return HTMLElementImpl.getImpl(getControlImpl(this.getPeer()));
   }

   static native long getControlImpl(long var0);

   public String getAccessKey() {
      return getAccessKeyImpl(this.getPeer());
   }

   static native String getAccessKeyImpl(long var0);

   public void setAccessKey(String var1) {
      setAccessKeyImpl(this.getPeer(), var1);
   }

   static native void setAccessKeyImpl(long var0, String var2);
}
