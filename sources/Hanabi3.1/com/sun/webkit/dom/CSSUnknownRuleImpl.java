package com.sun.webkit.dom;

import org.w3c.dom.css.CSSUnknownRule;

public class CSSUnknownRuleImpl extends CSSRuleImpl implements CSSUnknownRule {
   CSSUnknownRuleImpl(long var1) {
      super(var1);
   }

   static CSSUnknownRule getImpl(long var0) {
      return (CSSUnknownRule)create(var0);
   }
}
