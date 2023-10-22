package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class HTMLTableSectionElementImpl extends HTMLElementImpl implements HTMLTableSectionElement {
   HTMLTableSectionElementImpl(long var1) {
      super(var1);
   }

   static HTMLTableSectionElement getImpl(long var0) {
      return (HTMLTableSectionElement)create(var0);
   }

   public String getAlign() {
      return getAlignImpl(this.getPeer());
   }

   static native String getAlignImpl(long var0);

   public void setAlign(String var1) {
      setAlignImpl(this.getPeer(), var1);
   }

   static native void setAlignImpl(long var0, String var2);

   public String getCh() {
      return getChImpl(this.getPeer());
   }

   static native String getChImpl(long var0);

   public void setCh(String var1) {
      setChImpl(this.getPeer(), var1);
   }

   static native void setChImpl(long var0, String var2);

   public String getChOff() {
      return getChOffImpl(this.getPeer());
   }

   static native String getChOffImpl(long var0);

   public void setChOff(String var1) {
      setChOffImpl(this.getPeer(), var1);
   }

   static native void setChOffImpl(long var0, String var2);

   public String getVAlign() {
      return getVAlignImpl(this.getPeer());
   }

   static native String getVAlignImpl(long var0);

   public void setVAlign(String var1) {
      setVAlignImpl(this.getPeer(), var1);
   }

   static native void setVAlignImpl(long var0, String var2);

   public HTMLCollection getRows() {
      return HTMLCollectionImpl.getImpl(getRowsImpl(this.getPeer()));
   }

   static native long getRowsImpl(long var0);

   public HTMLElement insertRow(int var1) throws DOMException {
      return HTMLElementImpl.getImpl(insertRowImpl(this.getPeer(), var1));
   }

   static native long insertRowImpl(long var0, int var2);

   public void deleteRow(int var1) throws DOMException {
      deleteRowImpl(this.getPeer(), var1);
   }

   static native void deleteRowImpl(long var0, int var2);
}
