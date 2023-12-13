package com.sun.webkit.dom;

import org.w3c.dom.EntityReference;

public class EntityReferenceImpl extends NodeImpl implements EntityReference {
   EntityReferenceImpl(long var1) {
      super(var1);
   }

   static EntityReference getImpl(long var0) {
      return (EntityReference)create(var0);
   }
}
