package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLOptGroupElement;

public class HTMLOptGroupElementImpl extends HTMLElementImpl implements HTMLOptGroupElement {
   HTMLOptGroupElementImpl(long var1) {
      super(var1);
   }

   static HTMLOptGroupElement getImpl(long var0) {
      return (HTMLOptGroupElement)create(var0);
   }

   public boolean getDisabled() {
      return getDisabledImpl(this.getPeer());
   }

   static native boolean getDisabledImpl(long var0);

   public void setDisabled(boolean var1) {
      setDisabledImpl(this.getPeer(), var1);
   }

   static native void setDisabledImpl(long var0, boolean var2);

   public String getLabel() {
      return getLabelImpl(this.getPeer());
   }

   static native String getLabelImpl(long var0);

   public void setLabel(String var1) {
      setLabelImpl(this.getPeer(), var1);
   }

   static native void setLabelImpl(long var0, String var2);
}
