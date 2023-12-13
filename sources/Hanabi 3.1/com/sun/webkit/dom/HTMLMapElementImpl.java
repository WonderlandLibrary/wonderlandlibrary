package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLMapElement;

public class HTMLMapElementImpl extends HTMLElementImpl implements HTMLMapElement {
   HTMLMapElementImpl(long var1) {
      super(var1);
   }

   static HTMLMapElement getImpl(long var0) {
      return (HTMLMapElement)create(var0);
   }

   public HTMLCollection getAreas() {
      return HTMLCollectionImpl.getImpl(getAreasImpl(this.getPeer()));
   }

   static native long getAreasImpl(long var0);

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public void setName(String var1) {
      setNameImpl(this.getPeer(), var1);
   }

   static native void setNameImpl(long var0, String var2);
}
