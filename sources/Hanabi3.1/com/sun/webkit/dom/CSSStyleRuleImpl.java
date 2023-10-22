package com.sun.webkit.dom;

import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

public class CSSStyleRuleImpl extends CSSRuleImpl implements CSSStyleRule {
   CSSStyleRuleImpl(long var1) {
      super(var1);
   }

   static CSSStyleRule getImpl(long var0) {
      return (CSSStyleRule)create(var0);
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
