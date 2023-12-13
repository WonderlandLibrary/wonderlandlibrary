package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableRowElement;

public class HTMLTableRowElementImpl extends HTMLElementImpl implements HTMLTableRowElement {
   HTMLTableRowElementImpl(long var1) {
      super(var1);
   }

   static HTMLTableRowElement getImpl(long var0) {
      return (HTMLTableRowElement)create(var0);
   }

   public int getRowIndex() {
      return getRowIndexImpl(this.getPeer());
   }

   static native int getRowIndexImpl(long var0);

   public int getSectionRowIndex() {
      return getSectionRowIndexImpl(this.getPeer());
   }

   static native int getSectionRowIndexImpl(long var0);

   public HTMLCollection getCells() {
      return HTMLCollectionImpl.getImpl(getCellsImpl(this.getPeer()));
   }

   static native long getCellsImpl(long var0);

   public String getAlign() {
      return getAlignImpl(this.getPeer());
   }

   static native String getAlignImpl(long var0);

   public void setAlign(String var1) {
      setAlignImpl(this.getPeer(), var1);
   }

   static native void setAlignImpl(long var0, String var2);

   public String getBgColor() {
      return getBgColorImpl(this.getPeer());
   }

   static native String getBgColorImpl(long var0);

   public void setBgColor(String var1) {
      setBgColorImpl(this.getPeer(), var1);
   }

   static native void setBgColorImpl(long var0, String var2);

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

   public HTMLElement insertCell(int var1) throws DOMException {
      return HTMLElementImpl.getImpl(insertCellImpl(this.getPeer(), var1));
   }

   static native long insertCellImpl(long var0, int var2);

   public void deleteCell(int var1) throws DOMException {
      deleteCellImpl(this.getPeer(), var1);
   }

   static native void deleteCellImpl(long var0, int var2);
}
