package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSCharsetRule;

public class CSSCharsetRuleImpl extends CSSRuleImpl implements CSSCharsetRule {
   CSSCharsetRuleImpl(long var1) {
      super(var1);
   }

   static CSSCharsetRule getImpl(long var0) {
      return (CSSCharsetRule)create(var0);
   }

   public String getEncoding() {
      return getEncodingImpl(this.getPeer());
   }

   static native String getEncodingImpl(long var0);

   public void setEncoding(String var1) throws DOMException {
      setEncodingImpl(this.getPeer(), var1);
   }

   static native void setEncodingImpl(long var0, String var2);
}
