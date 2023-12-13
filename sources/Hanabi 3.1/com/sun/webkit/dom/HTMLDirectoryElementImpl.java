package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLDirectoryElement;

public class HTMLDirectoryElementImpl extends HTMLElementImpl implements HTMLDirectoryElement {
   HTMLDirectoryElementImpl(long var1) {
      super(var1);
   }

   static HTMLDirectoryElement getImpl(long var0) {
      return (HTMLDirectoryElement)create(var0);
   }

   public boolean getCompact() {
      return getCompactImpl(this.getPeer());
   }

   static native boolean getCompactImpl(long var0);

   public void setCompact(boolean var1) {
      setCompactImpl(this.getPeer(), var1);
   }

   static native void setCompactImpl(long var0, boolean var2);
}
