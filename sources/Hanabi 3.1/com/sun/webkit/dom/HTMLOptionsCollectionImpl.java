package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLOptionElement;

public class HTMLOptionsCollectionImpl extends HTMLCollectionImpl {
   HTMLOptionsCollectionImpl(long var1) {
      super(var1);
   }

   static HTMLOptionsCollectionImpl getImpl(long var0) {
      return (HTMLOptionsCollectionImpl)create(var0);
   }

   public int getSelectedIndex() {
      return getSelectedIndexImpl(this.getPeer());
   }

   static native int getSelectedIndexImpl(long var0);

   public void setSelectedIndex(int var1) {
      setSelectedIndexImpl(this.getPeer(), var1);
   }

   static native void setSelectedIndexImpl(long var0, int var2);

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public void setLength(int var1) throws DOMException {
      setLengthImpl(this.getPeer(), var1);
   }

   static native void setLengthImpl(long var0, int var2);

   public Node namedItem(String var1) {
      return NodeImpl.getImpl(namedItemImpl(this.getPeer(), var1));
   }

   static native long namedItemImpl(long var0, String var2);

   public void add(HTMLOptionElement var1, int var2) throws DOMException {
      addImpl(this.getPeer(), HTMLOptionElementImpl.getPeer(var1), var2);
   }

   static native void addImpl(long var0, long var2, int var4);

   public Node item(int var1) {
      return NodeImpl.getImpl(itemImpl(this.getPeer(), var1));
   }

   static native long itemImpl(long var0, int var2);
}
