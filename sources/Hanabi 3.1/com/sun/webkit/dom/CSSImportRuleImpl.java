package com.sun.webkit.dom;

import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.stylesheets.MediaList;

public class CSSImportRuleImpl extends CSSRuleImpl implements CSSImportRule {
   CSSImportRuleImpl(long var1) {
      super(var1);
   }

   static CSSImportRule getImpl(long var0) {
      return (CSSImportRule)create(var0);
   }

   public String getHref() {
      return getHrefImpl(this.getPeer());
   }

   static native String getHrefImpl(long var0);

   public MediaList getMedia() {
      return MediaListImpl.getImpl(getMediaImpl(this.getPeer()));
   }

   static native long getMediaImpl(long var0);

   public CSSStyleSheet getStyleSheet() {
      return CSSStyleSheetImpl.getImpl(getStyleSheetImpl(this.getPeer()));
   }

   static native long getStyleSheetImpl(long var0);
}
