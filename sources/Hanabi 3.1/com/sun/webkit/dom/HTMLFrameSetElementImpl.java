package com.sun.webkit.dom;

import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLFrameSetElement;

public class HTMLFrameSetElementImpl extends HTMLElementImpl implements HTMLFrameSetElement {
   HTMLFrameSetElementImpl(long var1) {
      super(var1);
   }

   static HTMLFrameSetElement getImpl(long var0) {
      return (HTMLFrameSetElement)create(var0);
   }

   public String getCols() {
      return getColsImpl(this.getPeer());
   }

   static native String getColsImpl(long var0);

   public void setCols(String var1) {
      setColsImpl(this.getPeer(), var1);
   }

   static native void setColsImpl(long var0, String var2);

   public String getRows() {
      return getRowsImpl(this.getPeer());
   }

   static native String getRowsImpl(long var0);

   public void setRows(String var1) {
      setRowsImpl(this.getPeer(), var1);
   }

   static native void setRowsImpl(long var0, String var2);

   public EventListener getOnblur() {
      return EventListenerImpl.getImpl(getOnblurImpl(this.getPeer()));
   }

   static native long getOnblurImpl(long var0);

   public void setOnblur(EventListener var1) {
      setOnblurImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnblurImpl(long var0, long var2);

   public EventListener getOnerror() {
      return EventListenerImpl.getImpl(getOnerrorImpl(this.getPeer()));
   }

   static native long getOnerrorImpl(long var0);

   public void setOnerror(EventListener var1) {
      setOnerrorImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnerrorImpl(long var0, long var2);

   public EventListener getOnfocus() {
      return EventListenerImpl.getImpl(getOnfocusImpl(this.getPeer()));
   }

   static native long getOnfocusImpl(long var0);

   public void setOnfocus(EventListener var1) {
      setOnfocusImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnfocusImpl(long var0, long var2);

   public EventListener getOnfocusin() {
      return EventListenerImpl.getImpl(getOnfocusinImpl(this.getPeer()));
   }

   static native long getOnfocusinImpl(long var0);

   public void setOnfocusin(EventListener var1) {
      setOnfocusinImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnfocusinImpl(long var0, long var2);

   public EventListener getOnfocusout() {
      return EventListenerImpl.getImpl(getOnfocusoutImpl(this.getPeer()));
   }

   static native long getOnfocusoutImpl(long var0);

   public void setOnfocusout(EventListener var1) {
      setOnfocusoutImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnfocusoutImpl(long var0, long var2);

   public EventListener getOnload() {
      return EventListenerImpl.getImpl(getOnloadImpl(this.getPeer()));
   }

   static native long getOnloadImpl(long var0);

   public void setOnload(EventListener var1) {
      setOnloadImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnloadImpl(long var0, long var2);

   public EventListener getOnresize() {
      return EventListenerImpl.getImpl(getOnresizeImpl(this.getPeer()));
   }

   static native long getOnresizeImpl(long var0);

   public void setOnresize(EventListener var1) {
      setOnresizeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnresizeImpl(long var0, long var2);

   public EventListener getOnscroll() {
      return EventListenerImpl.getImpl(getOnscrollImpl(this.getPeer()));
   }

   static native long getOnscrollImpl(long var0);

   public void setOnscroll(EventListener var1) {
      setOnscrollImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnscrollImpl(long var0, long var2);

   public EventListener getOnbeforeunload() {
      return EventListenerImpl.getImpl(getOnbeforeunloadImpl(this.getPeer()));
   }

   static native long getOnbeforeunloadImpl(long var0);

   public void setOnbeforeunload(EventListener var1) {
      setOnbeforeunloadImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnbeforeunloadImpl(long var0, long var2);

   public EventListener getOnhashchange() {
      return EventListenerImpl.getImpl(getOnhashchangeImpl(this.getPeer()));
   }

   static native long getOnhashchangeImpl(long var0);

   public void setOnhashchange(EventListener var1) {
      setOnhashchangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnhashchangeImpl(long var0, long var2);

   public EventListener getOnmessage() {
      return EventListenerImpl.getImpl(getOnmessageImpl(this.getPeer()));
   }

   static native long getOnmessageImpl(long var0);

   public void setOnmessage(EventListener var1) {
      setOnmessageImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmessageImpl(long var0, long var2);

   public EventListener getOnoffline() {
      return EventListenerImpl.getImpl(getOnofflineImpl(this.getPeer()));
   }

   static native long getOnofflineImpl(long var0);

   public void setOnoffline(EventListener var1) {
      setOnofflineImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnofflineImpl(long var0, long var2);

   public EventListener getOnonline() {
      return EventListenerImpl.getImpl(getOnonlineImpl(this.getPeer()));
   }

   static native long getOnonlineImpl(long var0);

   public void setOnonline(EventListener var1) {
      setOnonlineImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnonlineImpl(long var0, long var2);

   public EventListener getOnpagehide() {
      return EventListenerImpl.getImpl(getOnpagehideImpl(this.getPeer()));
   }

   static native long getOnpagehideImpl(long var0);

   public void setOnpagehide(EventListener var1) {
      setOnpagehideImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnpagehideImpl(long var0, long var2);

   public EventListener getOnpageshow() {
      return EventListenerImpl.getImpl(getOnpageshowImpl(this.getPeer()));
   }

   static native long getOnpageshowImpl(long var0);

   public void setOnpageshow(EventListener var1) {
      setOnpageshowImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnpageshowImpl(long var0, long var2);

   public EventListener getOnpopstate() {
      return EventListenerImpl.getImpl(getOnpopstateImpl(this.getPeer()));
   }

   static native long getOnpopstateImpl(long var0);

   public void setOnpopstate(EventListener var1) {
      setOnpopstateImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnpopstateImpl(long var0, long var2);

   public EventListener getOnstorage() {
      return EventListenerImpl.getImpl(getOnstorageImpl(this.getPeer()));
   }

   static native long getOnstorageImpl(long var0);

   public void setOnstorage(EventListener var1) {
      setOnstorageImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnstorageImpl(long var0, long var2);

   public EventListener getOnunload() {
      return EventListenerImpl.getImpl(getOnunloadImpl(this.getPeer()));
   }

   static native long getOnunloadImpl(long var0);

   public void setOnunload(EventListener var1) {
      setOnunloadImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnunloadImpl(long var0, long var2);
}
