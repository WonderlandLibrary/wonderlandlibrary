package com.sun.webkit.dom;

import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSStyleDeclaration;

public class CSSPageRuleImpl extends CSSRuleImpl implements CSSPageRule {
   CSSPageRuleImpl(long var1) {
      super(var1);
   }

   static CSSPageRule getImpl(long var0) {
      return (CSSPageRule)create(var0);
   }

   public String getSelectorText() {
      return getSelectorTextImpl(this.getPeer());
   }

   static native String getSelectorTextImpl(long var0);

   public void setSelectorText(String var1) {
      setSelectorTextImpl(this.getPeer(), var1);
   }

   static native void setSelectorTextImpl(long var0, String var2);

   public CSSStyleDeclaration getStyle() {
      return CSSStyleDeclarationImpl.getImpl(getStyleImpl(this.getPeer()));
   }

   static native long getStyleImpl(long var0);
}
