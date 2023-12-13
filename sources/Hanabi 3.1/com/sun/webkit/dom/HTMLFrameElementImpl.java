package com.sun.webkit.dom;

import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLFrameElement;
import org.w3c.dom.views.AbstractView;

public class HTMLFrameElementImpl extends HTMLElementImpl implements HTMLFrameElement {
   HTMLFrameElementImpl(long var1) {
      super(var1);
   }

   static HTMLFrameElement getImpl(long var0) {
      return (HTMLFrameElement)create(var0);
   }

   public String getFrameBorder() {
      return getFrameBorderImpl(this.getPeer());
   }

   static native String getFrameBorderImpl(long var0);

   public void setFrameBorder(String var1) {
      setFrameBorderImpl(this.getPeer(), var1);
   }

   static native void setFrameBorderImpl(long var0, String var2);

   public String getLongDesc() {
      return getLongDescImpl(this.getPeer());
   }

   static native String getLongDescImpl(long var0);

   public void setLongDesc(String var1) {
      setLongDescImpl(this.getPeer(), var1);
   }

   static native void setLongDescImpl(long var0, String var2);

   public String getMarginHeight() {
      return getMarginHeightImpl(this.getPeer());
   }

   static native String getMarginHeightImpl(long var0);

   public void setMarginHeight(String var1) {
      setMarginHeightImpl(this.getPeer(), var1);
   }

   static native void setMarginHeightImpl(long var0, String var2);

   public String getMarginWidth() {
      return getMarginWidthImpl(this.getPeer());
   }

   static native String getMarginWidthImpl(long var0);

   public void setMarginWidth(String var1) {
      setMarginWidthImpl(this.getPeer(), var1);
   }

   static native void setMarginWidthImpl(long var0, String var2);

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public void setName(String var1) {
      setNameImpl(this.getPeer(), var1);
   }

   static native void setNameImpl(long var0, String var2);

   public boolean getNoResize() {
      return getNoResizeImpl(this.getPeer());
   }

   static native boolean getNoResizeImpl(long var0);

   public void setNoResize(boolean var1) {
      setNoResizeImpl(this.getPeer(), var1);
   }

   static native void setNoResizeImpl(long var0, boolean var2);

   public String getScrolling() {
      return getScrollingImpl(this.getPeer());
   }

   static native String getScrollingImpl(long var0);

   public void setScrolling(String var1) {
      setScrollingImpl(this.getPeer(), var1);
   }

   static native void setScrollingImpl(long var0, String var2);

   public String getSrc() {
      return getSrcImpl(this.getPeer());
   }

   static native String getSrcImpl(long var0);

   public void setSrc(String var1) {
      setSrcImpl(this.getPeer(), var1);
   }

   static native void setSrcImpl(long var0, String var2);

   public Document getContentDocument() {
      return DocumentImpl.getImpl(getContentDocumentImpl(this.getPeer()));
   }

   static native long getContentDocumentImpl(long var0);

   public AbstractView getContentWindow() {
      return DOMWindowImpl.getImpl(getContentWindowImpl(this.getPeer()));
   }

   static native long getContentWindowImpl(long var0);

   public String getLocation() {
      return getLocationImpl(this.getPeer());
   }

   static native String getLocationImpl(long var0);

   public void setLocation(String var1) {
      setLocationImpl(this.getPeer(), var1);
   }

   static native void setLocationImpl(long var0, String var2);

   public int getWidth() {
      return getWidthImpl(this.getPeer());
   }

   static native int getWidthImpl(long var0);

   public int getHeight() {
      return getHeightImpl(this.getPeer());
   }

   static native int getHeightImpl(long var0);
}
