package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

public class CSSStyleSheetImpl extends StyleSheetImpl implements CSSStyleSheet {
   CSSStyleSheetImpl(long var1) {
      super(var1);
   }

   static CSSStyleSheet getImpl(long var0) {
      return (CSSStyleSheet)create(var0);
   }

   public CSSRule getOwnerRule() {
      return CSSRuleImpl.getImpl(getOwnerRuleImpl(this.getPeer()));
   }

   static native long getOwnerRuleImpl(long var0);

   public CSSRuleList getCssRules() {
      return CSSRuleListImpl.getImpl(getCssRulesImpl(this.getPeer()));
   }

   static native long getCssRulesImpl(long var0);

   public CSSRuleList getRules() {
      return CSSRuleListImpl.getImpl(getRulesImpl(this.getPeer()));
   }

   static native long getRulesImpl(long var0);

   public int insertRule(String var1, int var2) throws DOMException {
      return insertRuleImpl(this.getPeer(), var1, var2);
   }

   static native int insertRuleImpl(long var0, String var2, int var3);

   public void deleteRule(int var1) throws DOMException {
      deleteRuleImpl(this.getPeer(), var1);
   }

   static native void deleteRuleImpl(long var0, int var2);

   public int addRule(String var1, String var2, int var3) throws DOMException {
      return addRuleImpl(this.getPeer(), var1, var2, var3);
   }

   static native int addRuleImpl(long var0, String var2, String var3, int var4);

   public void removeRule(int var1) throws DOMException {
      removeRuleImpl(this.getPeer(), var1);
   }

   static native void removeRuleImpl(long var0, int var2);
}
