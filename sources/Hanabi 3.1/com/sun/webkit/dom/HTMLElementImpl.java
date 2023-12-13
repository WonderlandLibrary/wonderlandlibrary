package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;

public class HTMLElementImpl extends ElementImpl implements HTMLElement {
   HTMLElementImpl(long var1) {
      super(var1);
   }

   static HTMLElement getImpl(long var0) {
      return (HTMLElement)create(var0);
   }

   public String getId() {
      return getIdImpl(this.getPeer());
   }

   static native String getIdImpl(long var0);

   public void setId(String var1) {
      setIdImpl(this.getPeer(), var1);
   }

   static native void setIdImpl(long var0, String var2);

   public String getTitle() {
      return getTitleImpl(this.getPeer());
   }

   static native String getTitleImpl(long var0);

   public void setTitle(String var1) {
      setTitleImpl(this.getPeer(), var1);
   }

   static native void setTitleImpl(long var0, String var2);

   public String getLang() {
      return getLangImpl(this.getPeer());
   }

   static native String getLangImpl(long var0);

   public void setLang(String var1) {
      setLangImpl(this.getPeer(), var1);
   }

   static native void setLangImpl(long var0, String var2);

   public boolean getTranslate() {
      return getTranslateImpl(this.getPeer());
   }

   static native boolean getTranslateImpl(long var0);

   public void setTranslate(boolean var1) {
      setTranslateImpl(this.getPeer(), var1);
   }

   static native void setTranslateImpl(long var0, boolean var2);

   public String getDir() {
      return getDirImpl(this.getPeer());
   }

   static native String getDirImpl(long var0);

   public void setDir(String var1) {
      setDirImpl(this.getPeer(), var1);
   }

   static native void setDirImpl(long var0, String var2);

   public int getTabIndex() {
      return getTabIndexImpl(this.getPeer());
   }

   static native int getTabIndexImpl(long var0);

   public void setTabIndex(int var1) {
      setTabIndexImpl(this.getPeer(), var1);
   }

   static native void setTabIndexImpl(long var0, int var2);

   public boolean getDraggable() {
      return getDraggableImpl(this.getPeer());
   }

   static native boolean getDraggableImpl(long var0);

   public void setDraggable(boolean var1) {
      setDraggableImpl(this.getPeer(), var1);
   }

   static native void setDraggableImpl(long var0, boolean var2);

   public String getWebkitdropzone() {
      return getWebkitdropzoneImpl(this.getPeer());
   }

   static native String getWebkitdropzoneImpl(long var0);

   public void setWebkitdropzone(String var1) {
      setWebkitdropzoneImpl(this.getPeer(), var1);
   }

   static native void setWebkitdropzoneImpl(long var0, String var2);

   public boolean getHidden() {
      return getHiddenImpl(this.getPeer());
   }

   static native boolean getHiddenImpl(long var0);

   public void setHidden(boolean var1) {
      setHiddenImpl(this.getPeer(), var1);
   }

   static native void setHiddenImpl(long var0, boolean var2);

   public String getAccessKey() {
      return getAccessKeyImpl(this.getPeer());
   }

   static native String getAccessKeyImpl(long var0);

   public void setAccessKey(String var1) {
      setAccessKeyImpl(this.getPeer(), var1);
   }

   static native void setAccessKeyImpl(long var0, String var2);

   public String getInnerText() {
      return getInnerTextImpl(this.getPeer());
   }

   static native String getInnerTextImpl(long var0);

   public void setInnerText(String var1) throws DOMException {
      setInnerTextImpl(this.getPeer(), var1);
   }

   static native void setInnerTextImpl(long var0, String var2);

   public String getOuterText() {
      return getOuterTextImpl(this.getPeer());
   }

   static native String getOuterTextImpl(long var0);

   public void setOuterText(String var1) throws DOMException {
      setOuterTextImpl(this.getPeer(), var1);
   }

   static native void setOuterTextImpl(long var0, String var2);

   public HTMLCollection getChildren() {
      return HTMLCollectionImpl.getImpl(getChildrenImpl(this.getPeer()));
   }

   static native long getChildrenImpl(long var0);

   public String getContentEditable() {
      return getContentEditableImpl(this.getPeer());
   }

   static native String getContentEditableImpl(long var0);

   public void setContentEditable(String var1) throws DOMException {
      setContentEditableImpl(this.getPeer(), var1);
   }

   static native void setContentEditableImpl(long var0, String var2);

   public boolean getIsContentEditable() {
      return getIsContentEditableImpl(this.getPeer());
   }

   static native boolean getIsContentEditableImpl(long var0);

   public boolean getSpellcheck() {
      return getSpellcheckImpl(this.getPeer());
   }

   static native boolean getSpellcheckImpl(long var0);

   public void setSpellcheck(boolean var1) {
      setSpellcheckImpl(this.getPeer(), var1);
   }

   static native void setSpellcheckImpl(long var0, boolean var2);

   public String getTitleDisplayString() {
      return getTitleDisplayStringImpl(this.getPeer());
   }

   static native String getTitleDisplayStringImpl(long var0);

   public Element insertAdjacentElement(String var1, Element var2) throws DOMException {
      return ElementImpl.getImpl(insertAdjacentElementImpl(this.getPeer(), var1, ElementImpl.getPeer(var2)));
   }

   static native long insertAdjacentElementImpl(long var0, String var2, long var3);

   public void insertAdjacentHTML(String var1, String var2) throws DOMException {
      insertAdjacentHTMLImpl(this.getPeer(), var1, var2);
   }

   static native void insertAdjacentHTMLImpl(long var0, String var2, String var3);

   public void insertAdjacentText(String var1, String var2) throws DOMException {
      insertAdjacentTextImpl(this.getPeer(), var1, var2);
   }

   static native void insertAdjacentTextImpl(long var0, String var2, String var3);

   public void click() {
      clickImpl(this.getPeer());
   }

   static native void clickImpl(long var0);
}
