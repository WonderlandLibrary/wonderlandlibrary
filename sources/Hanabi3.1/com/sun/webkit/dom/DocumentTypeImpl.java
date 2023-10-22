package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;

public class DocumentTypeImpl extends NodeImpl implements DocumentType {
   DocumentTypeImpl(long var1) {
      super(var1);
   }

   static DocumentType getImpl(long var0) {
      return (DocumentType)create(var0);
   }

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public NamedNodeMap getEntities() {
      return NamedNodeMapImpl.getImpl(getEntitiesImpl(this.getPeer()));
   }

   static native long getEntitiesImpl(long var0);

   public NamedNodeMap getNotations() {
      return NamedNodeMapImpl.getImpl(getNotationsImpl(this.getPeer()));
   }

   static native long getNotationsImpl(long var0);

   public String getPublicId() {
      return getPublicIdImpl(this.getPeer());
   }

   static native String getPublicIdImpl(long var0);

   public String getSystemId() {
      return getSystemIdImpl(this.getPeer());
   }

   static native String getSystemIdImpl(long var0);

   public String getInternalSubset() {
      return getInternalSubsetImpl(this.getPeer());
   }

   static native String getInternalSubsetImpl(long var0);

   public void remove() throws DOMException {
      removeImpl(this.getPeer());
   }

   static native void removeImpl(long var0);
}
