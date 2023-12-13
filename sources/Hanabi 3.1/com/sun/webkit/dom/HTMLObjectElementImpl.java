package com.sun.webkit.dom;

import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLObjectElement;

public class HTMLObjectElementImpl extends HTMLElementImpl implements HTMLObjectElement {
   HTMLObjectElementImpl(long var1) {
      super(var1);
   }

   static HTMLObjectElement getImpl(long var0) {
      return (HTMLObjectElement)create(var0);
   }

   public HTMLFormElement getForm() {
      return HTMLFormElementImpl.getImpl(getFormImpl(this.getPeer()));
   }

   static native long getFormImpl(long var0);

   public String getCode() {
      return getCodeImpl(this.getPeer());
   }

   static native String getCodeImpl(long var0);

   public void setCode(String var1) {
      setCodeImpl(this.getPeer(), var1);
   }

   static native void setCodeImpl(long var0, String var2);

   public String getAlign() {
      return getAlignImpl(this.getPeer());
   }

   static native String getAlignImpl(long var0);

   public void setAlign(String var1) {
      setAlignImpl(this.getPeer(), var1);
   }

   static native void setAlignImpl(long var0, String var2);

   public String getArchive() {
      return getArchiveImpl(this.getPeer());
   }

   static native String getArchiveImpl(long var0);

   public void setArchive(String var1) {
      setArchiveImpl(this.getPeer(), var1);
   }

   static native void setArchiveImpl(long var0, String var2);

   public String getBorder() {
      return getBorderImpl(this.getPeer());
   }

   static native String getBorderImpl(long var0);

   public void setBorder(String var1) {
      setBorderImpl(this.getPeer(), var1);
   }

   static native void setBorderImpl(long var0, String var2);

   public String getCodeBase() {
      return getCodeBaseImpl(this.getPeer());
   }

   static native String getCodeBaseImpl(long var0);

   public void setCodeBase(String var1) {
      setCodeBaseImpl(this.getPeer(), var1);
   }

   static native void setCodeBaseImpl(long var0, String var2);

   public String getCodeType() {
      return getCodeTypeImpl(this.getPeer());
   }

   static native String getCodeTypeImpl(long var0);

   public void setCodeType(String var1) {
      setCodeTypeImpl(this.getPeer(), var1);
   }

   static native void setCodeTypeImpl(long var0, String var2);

   public String getData() {
      return getDataImpl(this.getPeer());
   }

   static native String getDataImpl(long var0);

   public void setData(String var1) {
      setDataImpl(this.getPeer(), var1);
   }

   static native void setDataImpl(long var0, String var2);

   public boolean getDeclare() {
      return getDeclareImpl(this.getPeer());
   }

   static native boolean getDeclareImpl(long var0);

   public void setDeclare(boolean var1) {
      setDeclareImpl(this.getPeer(), var1);
   }

   static native void setDeclareImpl(long var0, boolean var2);

   public String getHeight() {
      return getHeightImpl(this.getPeer());
   }

   static native String getHeightImpl(long var0);

   public void setHeight(String var1) {
      setHeightImpl(this.getPeer(), var1);
   }

   static native void setHeightImpl(long var0, String var2);

   public String getHspace() {
      return getHspaceImpl(this.getPeer()) + "";
   }

   static native int getHspaceImpl(long var0);

   public void setHspace(String var1) {
      setHspaceImpl(this.getPeer(), Integer.parseInt(var1));
   }

   static native void setHspaceImpl(long var0, int var2);

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public void setName(String var1) {
      setNameImpl(this.getPeer(), var1);
   }

   static native void setNameImpl(long var0, String var2);

   public String getStandby() {
      return getStandbyImpl(this.getPeer());
   }

   static native String getStandbyImpl(long var0);

   public void setStandby(String var1) {
      setStandbyImpl(this.getPeer(), var1);
   }

   static native void setStandbyImpl(long var0, String var2);

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public void setType(String var1) {
      setTypeImpl(this.getPeer(), var1);
   }

   static native void setTypeImpl(long var0, String var2);

   public String getUseMap() {
      return getUseMapImpl(this.getPeer());
   }

   static native String getUseMapImpl(long var0);

   public void setUseMap(String var1) {
      setUseMapImpl(this.getPeer(), var1);
   }

   static native void setUseMapImpl(long var0, String var2);

   public String getVspace() {
      return getVspaceImpl(this.getPeer()) + "";
   }

   static native int getVspaceImpl(long var0);

   public void setVspace(String var1) {
      setVspaceImpl(this.getPeer(), Integer.parseInt(var1));
   }

   static native void setVspaceImpl(long var0, int var2);

   public String getWidth() {
      return getWidthImpl(this.getPeer());
   }

   static native String getWidthImpl(long var0);

   public void setWidth(String var1) {
      setWidthImpl(this.getPeer(), var1);
   }

   static native void setWidthImpl(long var0, String var2);

   public boolean getWillValidate() {
      return getWillValidateImpl(this.getPeer());
   }

   static native boolean getWillValidateImpl(long var0);

   public String getValidationMessage() {
      return getValidationMessageImpl(this.getPeer());
   }

   static native String getValidationMessageImpl(long var0);

   public Document getContentDocument() {
      return DocumentImpl.getImpl(getContentDocumentImpl(this.getPeer()));
   }

   static native long getContentDocumentImpl(long var0);

   public boolean checkValidity() {
      return checkValidityImpl(this.getPeer());
   }

   static native boolean checkValidityImpl(long var0);

   public void setCustomValidity(String var1) {
      setCustomValidityImpl(this.getPeer(), var1);
   }

   static native void setCustomValidityImpl(long var0, String var2);
}
