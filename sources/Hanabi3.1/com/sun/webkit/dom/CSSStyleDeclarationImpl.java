package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

public class CSSStyleDeclarationImpl implements CSSStyleDeclaration {
   private final long peer;

   CSSStyleDeclarationImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static CSSStyleDeclaration create(long var0) {
      return var0 == 0L ? null : new CSSStyleDeclarationImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof CSSStyleDeclarationImpl && this.peer == ((CSSStyleDeclarationImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(CSSStyleDeclaration var0) {
      return var0 == null ? 0L : ((CSSStyleDeclarationImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static CSSStyleDeclaration getImpl(long var0) {
      return create(var0);
   }

   public String getCssText() {
      return getCssTextImpl(this.getPeer());
   }

   static native String getCssTextImpl(long var0);

   public void setCssText(String var1) throws DOMException {
      setCssTextImpl(this.getPeer(), var1);
   }

   static native void setCssTextImpl(long var0, String var2);

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public CSSRule getParentRule() {
      return CSSRuleImpl.getImpl(getParentRuleImpl(this.getPeer()));
   }

   static native long getParentRuleImpl(long var0);

   public String getPropertyValue(String var1) {
      return getPropertyValueImpl(this.getPeer(), var1);
   }

   static native String getPropertyValueImpl(long var0, String var2);

   public CSSValue getPropertyCSSValue(String var1) {
      return CSSValueImpl.getImpl(getPropertyCSSValueImpl(this.getPeer(), var1));
   }

   static native long getPropertyCSSValueImpl(long var0, String var2);

   public String removeProperty(String var1) throws DOMException {
      return removePropertyImpl(this.getPeer(), var1);
   }

   static native String removePropertyImpl(long var0, String var2);

   public String getPropertyPriority(String var1) {
      return getPropertyPriorityImpl(this.getPeer(), var1);
   }

   static native String getPropertyPriorityImpl(long var0, String var2);

   public void setProperty(String var1, String var2, String var3) throws DOMException {
      setPropertyImpl(this.getPeer(), var1, var2, var3);
   }

   static native void setPropertyImpl(long var0, String var2, String var3, String var4);

   public String item(int var1) {
      return itemImpl(this.getPeer(), var1);
   }

   static native String itemImpl(long var0, int var2);

   public String getPropertyShorthand(String var1) {
      return getPropertyShorthandImpl(this.getPeer(), var1);
   }

   static native String getPropertyShorthandImpl(long var0, String var2);

   public boolean isPropertyImplicit(String var1) {
      return isPropertyImplicitImpl(this.getPeer(), var1);
   }

   static native boolean isPropertyImplicitImpl(long var0, String var2);

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         CSSStyleDeclarationImpl.dispose(this.peer);
      }
   }
}
