package com.sun.webkit.dom;

import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.views.AbstractView;

public class HTMLIFrameElementImpl extends HTMLElementImpl implements HTMLIFrameElement {
   HTMLIFrameElementImpl(long var1) {
      super(var1);
   }

   static HTMLIFrameElement getImpl(long var0) {
      return (HTMLIFrameElement)create(var0);
   }

   public String getAlign() {
      return getAlignImpl(this.getPeer());
   }

   static native String getAlignImpl(long var0);

   public void setAlign(String var1) {
      setAlignImpl(this.getPeer(), var1);
   }

   static native void setAlignImpl(long var0, String var2);

   public String getFrameBorder() {
      return getFrameBorderImpl(this.getPeer());
   }

   static native String getFrameBorderImpl(long var0);

   public void setFrameBorder(String var1) {
      setFrameBorderImpl(this.getPeer(), var1);
   }

   static native void setFrameBorderImpl(long var0, String var2);

   public String getHeight() {
      return getHeightImpl(this.getPeer());
   }

   static native String getHeightImpl(long var0);

   public void setHeight(String var1) {
      setHeightImpl(this.getPeer(), var1);
   }

   static native void setHeightImpl(long var0, String var2);

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

   public String getSrcdoc() {
      return getSrcdocImpl(this.getPeer());
   }

   static native String getSrcdocImpl(long var0);

   public void setSrcdoc(String var1) {
      setSrcdocImpl(this.getPeer(), var1);
   }

   static native void setSrcdocImpl(long var0, String var2);

   public String getWidth() {
      return getWidthImpl(this.getPeer());
   }

   static native String getWidthImpl(long var0);

   public void setWidth(String var1) {
      setWidthImpl(this.getPeer(), var1);
   }

   static native void setWidthImpl(long var0, String var2);

   public Document getContentDocument() {
      return DocumentImpl.getImpl(getContentDocumentImpl(this.getPeer()));
   }

   static native long getContentDocumentImpl(long var0);

   public AbstractView getContentWindow() {
      return DOMWindowImpl.getImpl(getContentWindowImpl(this.getPeer()));
   }

   static native long getContentWindowImpl(long var0);
}
