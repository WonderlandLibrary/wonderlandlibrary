package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;

public class DocumentFragmentImpl extends NodeImpl implements DocumentFragment {
   DocumentFragmentImpl(long var1) {
      super(var1);
   }

   static DocumentFragment getImpl(long var0) {
      return (DocumentFragment)create(var0);
   }

   public HTMLCollection getChildren() {
      return HTMLCollectionImpl.getImpl(getChildrenImpl(this.getPeer()));
   }

   static native long getChildrenImpl(long var0);

   public Element getFirstElementChild() {
      return ElementImpl.getImpl(getFirstElementChildImpl(this.getPeer()));
   }

   static native long getFirstElementChildImpl(long var0);

   public Element getLastElementChild() {
      return ElementImpl.getImpl(getLastElementChildImpl(this.getPeer()));
   }

   static native long getLastElementChildImpl(long var0);

   public int getChildElementCount() {
      return getChildElementCountImpl(this.getPeer());
   }

   static native int getChildElementCountImpl(long var0);

   public Element getElementById(String var1) {
      return ElementImpl.getImpl(getElementByIdImpl(this.getPeer(), var1));
   }

   static native long getElementByIdImpl(long var0, String var2);

   public Element querySelector(String var1) throws DOMException {
      return ElementImpl.getImpl(querySelectorImpl(this.getPeer(), var1));
   }

   static native long querySelectorImpl(long var0, String var2);

   public NodeList querySelectorAll(String var1) throws DOMException {
      return NodeListImpl.getImpl(querySelectorAllImpl(this.getPeer(), var1));
   }

   static native long querySelectorAllImpl(long var0, String var2);
}
