package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.stylesheets.MediaList;

public class CSSMediaRuleImpl extends CSSRuleImpl implements CSSMediaRule {
   CSSMediaRuleImpl(long var1) {
      super(var1);
   }

   static CSSMediaRule getImpl(long var0) {
      return (CSSMediaRule)create(var0);
   }

   public MediaList getMedia() {
      return MediaListImpl.getImpl(getMediaImpl(this.getPeer()));
   }

   static native long getMediaImpl(long var0);

   public CSSRuleList getCssRules() {
      return CSSRuleListImpl.getImpl(getCssRulesImpl(this.getPeer()));
   }

   static native long getCssRulesImpl(long var0);

   public int insertRule(String var1, int var2) throws DOMException {
      return insertRuleImpl(this.getPeer(), var1, var2);
   }

   static native int insertRuleImpl(long var0, String var2, int var3);

   public void deleteRule(int var1) throws DOMException {
      deleteRuleImpl(this.getPeer(), var1);
   }

   static native void deleteRuleImpl(long var0, int var2);
}
