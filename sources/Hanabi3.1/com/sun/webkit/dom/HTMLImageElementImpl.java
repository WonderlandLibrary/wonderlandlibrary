package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLImageElement;

public class HTMLImageElementImpl extends HTMLElementImpl implements HTMLImageElement {
   HTMLImageElementImpl(long var1) {
      super(var1);
   }

   static HTMLImageElement getImpl(long var0) {
      return (HTMLImageElement)create(var0);
   }

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public void setName(String var1) {
      setNameImpl(this.getPeer(), var1);
   }

   static native void setNameImpl(long var0, String var2);

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

   public String getBorder() {
      return getBorderImpl(this.getPeer());
   }

   static native String getBorderImpl(long var0);

   public void setBorder(String var1) {
      setBorderImpl(this.getPeer(), var1);
   }

   static native void setBorderImpl(long var0, String var2);

   public String getCrossOrigin() {
      return getCrossOriginImpl(this.getPeer());
   }

   static native String getCrossOriginImpl(long var0);

   public void setCrossOrigin(String var1) {
      setCrossOriginImpl(this.getPeer(), var1);
   }

   static native void setCrossOriginImpl(long var0, String var2);

   public String getHeight() {
      return getHeightImpl(this.getPeer()) + "";
   }

   static native int getHeightImpl(long var0);

   public void setHeight(String var1) {
      setHeightImpl(this.getPeer(), Integer.parseInt(var1));
   }

   static native void setHeightImpl(long var0, int var2);

   public String getHspace() {
      return getHspaceImpl(this.getPeer()) + "";
   }

   static native int getHspaceImpl(long var0);

   public void setHspace(String var1) {
      setHspaceImpl(this.getPeer(), Integer.parseInt(var1));
   }

   static native void setHspaceImpl(long var0, int var2);

   public boolean getIsMap() {
      return getIsMapImpl(this.getPeer());
   }

   static native boolean getIsMapImpl(long var0);

   public void setIsMap(boolean var1) {
      setIsMapImpl(this.getPeer(), var1);
   }

   static native void setIsMapImpl(long var0, boolean var2);

   public String getLongDesc() {
      return getLongDescImpl(this.getPeer());
   }

   static native String getLongDescImpl(long var0);

   public void setLongDesc(String var1) {
      setLongDescImpl(this.getPeer(), var1);
   }

   static native void setLongDescImpl(long var0, String var2);

   public String getSrc() {
      return getSrcImpl(this.getPeer());
   }

   static native String getSrcImpl(long var0);

   public void setSrc(String var1) {
      setSrcImpl(this.getPeer(), var1);
   }

   static native void setSrcImpl(long var0, String var2);

   public String getSrcset() {
      return getSrcsetImpl(this.getPeer());
   }

   static native String getSrcsetImpl(long var0);

   public void setSrcset(String var1) {
      setSrcsetImpl(this.getPeer(), var1);
   }

   static native void setSrcsetImpl(long var0, String var2);

   public String getSizes() {
      return getSizesImpl(this.getPeer());
   }

   static native String getSizesImpl(long var0);

   public void setSizes(String var1) {
      setSizesImpl(this.getPeer(), var1);
   }

   static native void setSizesImpl(long var0, String var2);

   public String getCurrentSrc() {
      return getCurrentSrcImpl(this.getPeer());
   }

   static native String getCurrentSrcImpl(long var0);

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
      return getWidthImpl(this.getPeer()) + "";
   }

   static native int getWidthImpl(long var0);

   public void setWidth(String var1) {
      setWidthImpl(this.getPeer(), Integer.parseInt(var1));
   }

   static native void setWidthImpl(long var0, int var2);

   public boolean getComplete() {
      return getCompleteImpl(this.getPeer());
   }

   static native boolean getCompleteImpl(long var0);

   public String getLowsrc() {
      return getLowsrcImpl(this.getPeer());
   }

   static native String getLowsrcImpl(long var0);

   public void setLowsrc(String var1) {
      setLowsrcImpl(this.getPeer(), var1);
   }

   static native void setLowsrcImpl(long var0, String var2);

   public int getNaturalHeight() {
      return getNaturalHeightImpl(this.getPeer());
   }

   static native int getNaturalHeightImpl(long var0);

   public int getNaturalWidth() {
      return getNaturalWidthImpl(this.getPeer());
   }

   static native int getNaturalWidthImpl(long var0);

   public int getX() {
      return getXImpl(this.getPeer());
   }

   static native int getXImpl(long var0);

   public int getY() {
      return getYImpl(this.getPeer());
   }

   static native int getYImpl(long var0);

   public void setLowSrc(String var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public String getLowSrc() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
