package com.sun.webkit.dom;

import org.w3c.dom.Entity;

public class EntityImpl extends NodeImpl implements Entity {
   EntityImpl(long var1) {
      super(var1);
   }

   static Entity getImpl(long var0) {
      return (Entity)create(var0);
   }

   public String getPublicId() {
      return getPublicIdImpl(this.getPeer());
   }

   static native String getPublicIdImpl(long var0);

   public String getSystemId() {
      return getSystemIdImpl(this.getPeer());
   }

   static native String getSystemIdImpl(long var0);

   public String getNotationName() {
      return getNotationNameImpl(this.getPeer());
   }

   static native String getNotationNameImpl(long var0);

   public String getInputEncoding() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public String getXmlVersion() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public String getXmlEncoding() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
