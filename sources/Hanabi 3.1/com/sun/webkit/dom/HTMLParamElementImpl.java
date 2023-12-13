package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLParamElement;

public class HTMLParamElementImpl extends HTMLElementImpl implements HTMLParamElement {
   HTMLParamElementImpl(long var1) {
      super(var1);
   }

   static HTMLParamElement getImpl(long var0) {
      return (HTMLParamElement)create(var0);
   }

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public void setName(String var1) {
      setNameImpl(this.getPeer(), var1);
   }

   static native void setNameImpl(long var0, String var2);

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public void setType(String var1) {
      setTypeImpl(this.getPeer(), var1);
   }

   static native void setTypeImpl(long var0, String var2);

   public String getValue() {
      return getValueImpl(this.getPeer());
   }

   static native String getValueImpl(long var0);

   public void setValue(String var1) {
      setValueImpl(this.getPeer(), var1);
   }

   static native void setValueImpl(long var0, String var2);

   public String getValueType() {
      return getValueTypeImpl(this.getPeer());
   }

   static native String getValueTypeImpl(long var0);

   public void setValueType(String var1) {
      setValueTypeImpl(this.getPeer(), var1);
   }

   static native void setValueTypeImpl(long var0, String var2);
}
