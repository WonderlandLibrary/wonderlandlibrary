package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFormElement;

public class HTMLFormElementImpl extends HTMLElementImpl implements HTMLFormElement {
   HTMLFormElementImpl(long var1) {
      super(var1);
   }

   static HTMLFormElement getImpl(long var0) {
      return (HTMLFormElement)create(var0);
   }

   public String getAcceptCharset() {
      return getAcceptCharsetImpl(this.getPeer());
   }

   static native String getAcceptCharsetImpl(long var0);

   public void setAcceptCharset(String var1) {
      setAcceptCharsetImpl(this.getPeer(), var1);
   }

   static native void setAcceptCharsetImpl(long var0, String var2);

   public String getAction() {
      return getActionImpl(this.getPeer());
   }

   static native String getActionImpl(long var0);

   public void setAction(String var1) {
      setActionImpl(this.getPeer(), var1);
   }

   static native void setActionImpl(long var0, String var2);

   public String getAutocomplete() {
      return getAutocompleteImpl(this.getPeer());
   }

   static native String getAutocompleteImpl(long var0);

   public void setAutocomplete(String var1) {
      setAutocompleteImpl(this.getPeer(), var1);
   }

   static native void setAutocompleteImpl(long var0, String var2);

   public String getEnctype() {
      return getEnctypeImpl(this.getPeer());
   }

   static native String getEnctypeImpl(long var0);

   public void setEnctype(String var1) {
      setEnctypeImpl(this.getPeer(), var1);
   }

   static native void setEnctypeImpl(long var0, String var2);

   public String getEncoding() {
      return getEncodingImpl(this.getPeer());
   }

   static native String getEncodingImpl(long var0);

   public void setEncoding(String var1) {
      setEncodingImpl(this.getPeer(), var1);
   }

   static native void setEncodingImpl(long var0, String var2);

   public String getMethod() {
      return getMethodImpl(this.getPeer());
   }

   static native String getMethodImpl(long var0);

   public void setMethod(String var1) {
      setMethodImpl(this.getPeer(), var1);
   }

   static native void setMethodImpl(long var0, String var2);

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public void setName(String var1) {
      setNameImpl(this.getPeer(), var1);
   }

   static native void setNameImpl(long var0, String var2);

   public boolean getNoValidate() {
      return getNoValidateImpl(this.getPeer());
   }

   static native boolean getNoValidateImpl(long var0);

   public void setNoValidate(boolean var1) {
      setNoValidateImpl(this.getPeer(), var1);
   }

   static native void setNoValidateImpl(long var0, boolean var2);

   public String getTarget() {
      return getTargetImpl(this.getPeer());
   }

   static native String getTargetImpl(long var0);

   public void setTarget(String var1) {
      setTargetImpl(this.getPeer(), var1);
   }

   static native void setTargetImpl(long var0, String var2);

   public HTMLCollection getElements() {
      return HTMLCollectionImpl.getImpl(getElementsImpl(this.getPeer()));
   }

   static native long getElementsImpl(long var0);

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public void submit() {
      submitImpl(this.getPeer());
   }

   static native void submitImpl(long var0);

   public void reset() {
      resetImpl(this.getPeer());
   }

   static native void resetImpl(long var0);

   public boolean checkValidity() {
      return checkValidityImpl(this.getPeer());
   }

   static native boolean checkValidityImpl(long var0);
}
