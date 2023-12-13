package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLFieldSetElement;
import org.w3c.dom.html.HTMLFormElement;

public class HTMLFieldSetElementImpl extends HTMLElementImpl implements HTMLFieldSetElement {
   HTMLFieldSetElementImpl(long var1) {
      super(var1);
   }

   static HTMLFieldSetElement getImpl(long var0) {
      return (HTMLFieldSetElement)create(var0);
   }

   public boolean getDisabled() {
      return getDisabledImpl(this.getPeer());
   }

   static native boolean getDisabledImpl(long var0);

   public void setDisabled(boolean var1) {
      setDisabledImpl(this.getPeer(), var1);
   }

   static native void setDisabledImpl(long var0, boolean var2);

   public HTMLFormElement getForm() {
      return HTMLFormElementImpl.getImpl(getFormImpl(this.getPeer()));
   }

   static native long getFormImpl(long var0);

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

   public boolean getWillValidate() {
      return getWillValidateImpl(this.getPeer());
   }

   static native boolean getWillValidateImpl(long var0);

   public String getValidationMessage() {
      return getValidationMessageImpl(this.getPeer());
   }

   static native String getValidationMessageImpl(long var0);

   public boolean checkValidity() {
      return checkValidityImpl(this.getPeer());
   }

   static native boolean checkValidityImpl(long var0);

   public void setCustomValidity(String var1) {
      setCustomValidityImpl(this.getPeer(), var1);
   }

   static native void setCustomValidityImpl(long var0, String var2);
}
