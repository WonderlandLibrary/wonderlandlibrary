package com.sun.webkit.dom;

import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

public class CSSValueListImpl extends CSSValueImpl implements CSSValueList {
   CSSValueListImpl(long var1) {
      super(var1);
   }

   static CSSValueList getImpl(long var0) {
      return (CSSValueList)create(var0);
   }

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public CSSValue item(int var1) {
      return CSSValueImpl.getImpl(itemImpl(this.getPeer(), var1));
   }

   static native long itemImpl(long var0, int var2);
}
