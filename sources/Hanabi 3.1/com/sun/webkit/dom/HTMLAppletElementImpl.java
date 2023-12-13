package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLAppletElement;

public class HTMLAppletElementImpl extends HTMLElementImpl implements HTMLAppletElement {
   HTMLAppletElementImpl(long var1) {
      super(var1);
   }

   static HTMLAppletElement getImpl(long var0) {
      return (HTMLAppletElement)create(var0);
   }

   public String getAlign() {
      return getAlignImpl(this.getPeer());
   }

   static native String getAlignImpl(long var0);

   public void setAlign(String var1) {
      setAlignImpl(this.getPeer(), var1);
   }

   static native void setAlignImpl(long var0, String var2);

   public String getAlt() {
      return getAltImpl(this.getPeer());
   }

   static native String getAltImpl(long var0);

   public void setAlt(String var1) {
      setAltImpl(this.getPeer(), var1);
   }

   static native void setAltImpl(long var0, String var2);

   public String getArchive() {
      return getArchiveImpl(this.getPeer());
   }

   static native String getArchiveImpl(long var0);

   public void setArchive(String var1) {
      setArchiveImpl(this.getPeer(), var1);
   }

   static native void setArchiveImpl(long var0, String var2);

   public String getCode() {
      return getCodeImpl(this.getPeer());
   }

   static native String getCodeImpl(long var0);

   public void setCode(String var1) {
      setCodeImpl(this.getPeer(), var1);
   }

   static native void setCodeImpl(long var0, String var2);

   public String getCodeBase() {
      return getCodeBaseImpl(this.getPeer());
   }

   static native String getCodeBaseImpl(long var0);

   public void setCodeBase(String var1) {
      setCodeBaseImpl(this.getPeer(), var1);
   }

   static native void setCodeBaseImpl(long var0, String var2);

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

   public String getObject() {
      return getObjectImpl(this.getPeer());
   }

   static native String getObjectImpl(long var0);

   public void setObject(String var1) {
      setObjectImpl(this.getPeer(), var1);
   }

   static native void setObjectImpl(long var0, String var2);

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
}
