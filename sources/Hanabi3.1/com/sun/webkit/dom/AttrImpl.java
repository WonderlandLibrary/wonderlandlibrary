package com.sun.webkit.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;

public class AttrImpl extends NodeImpl implements Attr {
   AttrImpl(long var1) {
      super(var1);
   }

   static Attr getImpl(long var0) {
      return (Attr)create(var0);
   }

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public boolean getSpecified() {
      return getSpecifiedImpl(this.getPeer());
   }

   static native boolean getSpecifiedImpl(long var0);

   public String getValue() {
      return getValueImpl(this.getPeer());
   }

   static native String getValueImpl(long var0);

   public void setValue(String var1) throws DOMException {
      setValueImpl(this.getPeer(), var1);
   }

   static native void setValueImpl(long var0, String var2);

   public Element getOwnerElement() {
      return ElementImpl.getImpl(getOwnerElementImpl(this.getPeer()));
   }

   static native long getOwnerElementImpl(long var0);

   public boolean isId() {
      return isIdImpl(this.getPeer());
   }

   static native boolean isIdImpl(long var0);

   public TypeInfo getSchemaTypeInfo() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
