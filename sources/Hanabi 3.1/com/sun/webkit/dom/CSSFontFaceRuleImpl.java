package com.sun.webkit.dom;

import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSStyleDeclaration;

public class CSSFontFaceRuleImpl extends CSSRuleImpl implements CSSFontFaceRule {
   CSSFontFaceRuleImpl(long var1) {
      super(var1);
   }

   static CSSFontFaceRule getImpl(long var0) {
      return (CSSFontFaceRule)create(var0);
   }

   public CSSStyleDeclaration getStyle() {
      return CSSStyleDeclarationImpl.getImpl(getStyleImpl(this.getPeer()));
   }

   static native long getStyleImpl(long var0);
}
