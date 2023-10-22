package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLOListElement;

public class HTMLOListElementImpl extends HTMLElementImpl implements HTMLOListElement {
   HTMLOListElementImpl(long var1) {
      super(var1);
   }

   static HTMLOListElement getImpl(long var0) {
      return (HTMLOListElement)create(var0);
   }

   public boolean getCompact() {
      return getCompactImpl(this.getPeer());
   }

   static native boolean getCompactImpl(long var0);

   public void setCompact(boolean var1) {
      setCompactImpl(this.getPeer(), var1);
   }

   static native void setCompactImpl(long var0, boolean var2);

   public int getStart() {
      return getStartImpl(this.getPeer());
   }

   static native int getStartImpl(long var0);

   public void setStart(int var1) {
      setStartImpl(this.getPeer(), var1);
   }

   static native void setStartImpl(long var0, int var2);

   public boolean getReversed() {
      return getReversedImpl(this.getPeer());
   }

   static native boolean getReversedImpl(long var0);

   public void setReversed(boolean var1) {
      setReversedImpl(this.getPeer(), var1);
   }

   static native void setReversedImpl(long var0, boolean var2);

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public void setType(String var1) {
      setTypeImpl(this.getPeer(), var1);
   }

   static native void setTypeImpl(long var0, String var2);
}
