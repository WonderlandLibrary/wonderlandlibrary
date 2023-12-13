package com.sun.webkit.dom;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLFormElement;

public class HTMLButtonElementImpl extends HTMLElementImpl implements HTMLButtonElement {
   HTMLButtonElementImpl(long var1) {
      super(var1);
   }

   static HTMLButtonElement getImpl(long var0) {
      return (HTMLButtonElement)create(var0);
   }

   public boolean getAutofocus() {
      return getAutofocusImpl(this.getPeer());
   }

   static native boolean getAutofocusImpl(long var0);

   public void setAutofocus(boolean var1) {
      setAutofocusImpl(this.getPeer(), var1);
   }

   static native void setAutofocusImpl(long var0, boolean var2);

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

   public String getFormAction() {
      return getFormActionImpl(this.getPeer());
   }

   static native String getFormActionImpl(long var0);

   public void setFormAction(String var1) {
      setFormActionImpl(this.getPeer(), var1);
   }

   static native void setFormActionImpl(long var0, String var2);

   public String getFormEnctype() {
      return getFormEnctypeImpl(this.getPeer());
   }

   static native String getFormEnctypeImpl(long var0);

   public void setFormEnctype(String var1) {
      setFormEnctypeImpl(this.getPeer(), var1);
   }

   static native void setFormEnctypeImpl(long var0, String var2);

   public String getFormMethod() {
      return getFormMethodImpl(this.getPeer());
   }

   static native String getFormMethodImpl(long var0);

   public void setFormMethod(String var1) {
      setFormMethodImpl(this.getPeer(), var1);
   }

   static native void setFormMethodImpl(long var0, String var2);

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public void setType(String var1) {
      setTypeImpl(this.getPeer(), var1);
   }

   static native void setTypeImpl(long var0, String var2);

   public boolean getFormNoValidate() {
      return getFormNoValidateImpl(this.getPeer());
   }

   static native boolean getFormNoValidateImpl(long var0);

   public void setFormNoValidate(boolean var1) {
      setFormNoValidateImpl(this.getPeer(), var1);
   }

   static native void setFormNoValidateImpl(long var0, boolean var2);

   public String getFormTarget() {
      return getFormTargetImpl(this.getPeer());
   }

   static native String getFormTargetImpl(long var0);

   public void setFormTarget(String var1) {
      setFormTargetImpl(this.getPeer(), var1);
   }

   static native void setFormTargetImpl(long var0, String var2);

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public void setName(String var1) {
      setNameImpl(this.getPeer(), var1);
   }

   static native void setNameImpl(long var0, String var2);

   public String getValue() {
      return getValueImpl(this.getPeer());
   }

   static native String getValueImpl(long var0);

   public void setValue(String var1) {
      setValueImpl(this.getPeer(), var1);
   }

   static native void setValueImpl(long var0, String var2);

   public boolean getWillValidate() {
      return getWillValidateImpl(this.getPeer());
   }

   static native boolean getWillValidateImpl(long var0);

   public String getValidationMessage() {
      return getValidationMessageImpl(this.getPeer());
   }

   static native String getValidationMessageImpl(long var0);

   public NodeList getLabels() {
      return NodeListImpl.getImpl(getLabelsImpl(this.getPeer()));
   }

   static native long getLabelsImpl(long var0);

   public String getAccessKey() {
      return getAccessKeyImpl(this.getPeer());
   }

   static native String getAccessKeyImpl(long var0);

   public void setAccessKey(String var1) {
      setAccessKeyImpl(this.getPeer(), var1);
   }

   static native void setAccessKeyImpl(long var0, String var2);

   public boolean checkValidity() {
      return checkValidityImpl(this.getPeer());
   }

   static native boolean checkValidityImpl(long var0);

   public void setCustomValidity(String var1) {
      setCustomValidityImpl(this.getPeer(), var1);
   }

   static native void setCustomValidityImpl(long var0, String var2);

   public void click() {
      clickImpl(this.getPeer());
   }

   static native void clickImpl(long var0);
}
