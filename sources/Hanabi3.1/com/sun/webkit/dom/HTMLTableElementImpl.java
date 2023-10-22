package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableCaptionElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class HTMLTableElementImpl extends HTMLElementImpl implements HTMLTableElement {
   HTMLTableElementImpl(long var1) {
      super(var1);
   }

   static HTMLTableElement getImpl(long var0) {
      return (HTMLTableElement)create(var0);
   }

   public HTMLTableCaptionElement getCaption() {
      return HTMLTableCaptionElementImpl.getImpl(getCaptionImpl(this.getPeer()));
   }

   static native long getCaptionImpl(long var0);

   public void setCaption(HTMLTableCaptionElement var1) throws DOMException {
      setCaptionImpl(this.getPeer(), HTMLTableCaptionElementImpl.getPeer(var1));
   }

   static native void setCaptionImpl(long var0, long var2);

   public HTMLTableSectionElement getTHead() {
      return HTMLTableSectionElementImpl.getImpl(getTHeadImpl(this.getPeer()));
   }

   static native long getTHeadImpl(long var0);

   public void setTHead(HTMLTableSectionElement var1) throws DOMException {
      setTHeadImpl(this.getPeer(), HTMLTableSectionElementImpl.getPeer(var1));
   }

   static native void setTHeadImpl(long var0, long var2);

   public HTMLTableSectionElement getTFoot() {
      return HTMLTableSectionElementImpl.getImpl(getTFootImpl(this.getPeer()));
   }

   static native long getTFootImpl(long var0);

   public void setTFoot(HTMLTableSectionElement var1) throws DOMException {
      setTFootImpl(this.getPeer(), HTMLTableSectionElementImpl.getPeer(var1));
   }

   static native void setTFootImpl(long var0, long var2);

   public HTMLCollection getRows() {
      return HTMLCollectionImpl.getImpl(getRowsImpl(this.getPeer()));
   }

   static native long getRowsImpl(long var0);

   public HTMLCollection getTBodies() {
      return HTMLCollectionImpl.getImpl(getTBodiesImpl(this.getPeer()));
   }

   static native long getTBodiesImpl(long var0);

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

   public String getBorder() {
      return getBorderImpl(this.getPeer());
   }

   static native String getBorderImpl(long var0);

   public void setBorder(String var1) {
      setBorderImpl(this.getPeer(), var1);
   }

   static native void setBorderImpl(long var0, String var2);

   public String getCellPadding() {
      return getCellPaddingImpl(this.getPeer());
   }

   static native String getCellPaddingImpl(long var0);

   public void setCellPadding(String var1) {
      setCellPaddingImpl(this.getPeer(), var1);
   }

   static native void setCellPaddingImpl(long var0, String var2);

   public String getCellSpacing() {
      return getCellSpacingImpl(this.getPeer());
   }

   static native String getCellSpacingImpl(long var0);

   public void setCellSpacing(String var1) {
      setCellSpacingImpl(this.getPeer(), var1);
   }

   static native void setCellSpacingImpl(long var0, String var2);

   public String getFrame() {
      return getFrameImpl(this.getPeer());
   }

   static native String getFrameImpl(long var0);

   public void setFrame(String var1) {
      setFrameImpl(this.getPeer(), var1);
   }

   static native void setFrameImpl(long var0, String var2);

   public String getRules() {
      return getRulesImpl(this.getPeer());
   }

   static native String getRulesImpl(long var0);

   public void setRules(String var1) {
      setRulesImpl(this.getPeer(), var1);
   }

   static native void setRulesImpl(long var0, String var2);

   public String getSummary() {
      return getSummaryImpl(this.getPeer());
   }

   static native String getSummaryImpl(long var0);

   public void setSummary(String var1) {
      setSummaryImpl(this.getPeer(), var1);
   }

   static native void setSummaryImpl(long var0, String var2);

   public String getWidth() {
      return getWidthImpl(this.getPeer());
   }

   static native String getWidthImpl(long var0);

   public void setWidth(String var1) {
      setWidthImpl(this.getPeer(), var1);
   }

   static native void setWidthImpl(long var0, String var2);

   public HTMLElement createTHead() {
      return HTMLElementImpl.getImpl(createTHeadImpl(this.getPeer()));
   }

   static native long createTHeadImpl(long var0);

   public void deleteTHead() {
      deleteTHeadImpl(this.getPeer());
   }

   static native void deleteTHeadImpl(long var0);

   public HTMLElement createTFoot() {
      return HTMLElementImpl.getImpl(createTFootImpl(this.getPeer()));
   }

   static native long createTFootImpl(long var0);

   public void deleteTFoot() {
      deleteTFootImpl(this.getPeer());
   }

   static native void deleteTFootImpl(long var0);

   public HTMLElement createTBody() {
      return HTMLElementImpl.getImpl(createTBodyImpl(this.getPeer()));
   }

   static native long createTBodyImpl(long var0);

   public HTMLElement createCaption() {
      return HTMLElementImpl.getImpl(createCaptionImpl(this.getPeer()));
   }

   static native long createCaptionImpl(long var0);

   public void deleteCaption() {
      deleteCaptionImpl(this.getPeer());
   }

   static native void deleteCaptionImpl(long var0);

   public HTMLElement insertRow(int var1) throws DOMException {
      return HTMLElementImpl.getImpl(insertRowImpl(this.getPeer(), var1));
   }

   static native long insertRowImpl(long var0, int var2);

   public void deleteRow(int var1) throws DOMException {
      deleteRowImpl(this.getPeer(), var1);
   }

   static native void deleteRowImpl(long var0, int var2);
}
