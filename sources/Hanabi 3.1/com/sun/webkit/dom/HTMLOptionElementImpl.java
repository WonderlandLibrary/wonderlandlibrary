package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLOptionElement;

public class HTMLOptionElementImpl extends HTMLElementImpl implements HTMLOptionElement {
   HTMLOptionElementImpl(long var1) {
      super(var1);
   }

   static HTMLOptionElement getImpl(long var0) {
      return (HTMLOptionElement)create(var0);
   }

   public boolean getDisabled() {
      return getDisabledImpl(this.getPeer());
   }

   static native boolean getDisabledImpl(long var0);

   public void setDisabled(boolean var1) {
      setDisabledImpl(this.getPeer(), var1);
   }

   static native void setDisabledImpl(long var0, boolean var2);

   public HTMLFormElement getForm() {
      return HTMLFormElementImpl.getImpl(getFormImpl(this.getPeer()));
   }

   static native long getFormImpl(long var0);

   public String getLabel() {
      return getLabelImpl(this.getPeer());
   }

   static native String getLabelImpl(long var0);

   public void setLabel(String var1) {
      setLabelImpl(this.getPeer(), var1);
   }

   static native void setLabelImpl(long var0, String var2);

   public boolean getDefaultSelected() {
      return getDefaultSelectedImpl(this.getPeer());
   }

   static native boolean getDefaultSelectedImpl(long var0);

   public void setDefaultSelected(boolean var1) {
      setDefaultSelectedImpl(this.getPeer(), var1);
   }

   static native void setDefaultSelectedImpl(long var0, boolean var2);

   public boolean getSelected() {
      return getSelectedImpl(this.getPeer());
   }

   static native boolean getSelectedImpl(long var0);

   public void setSelected(boolean var1) {
      setSelectedImpl(this.getPeer(), var1);
   }

   static native void setSelectedImpl(long var0, boolean var2);

   public String getValue() {
      return getValueImpl(this.getPeer());
   }

   static native String getValueImpl(long var0);

   public void setValue(String var1) {
      setValueImpl(this.getPeer(), var1);
   }

   static native void setValueImpl(long var0, String var2);

   public String getText() {
      return getTextImpl(this.getPeer());
   }

   static native String getTextImpl(long var0);

   public int getIndex() {
      return getIndexImpl(this.getPeer());
   }

   static native int getIndexImpl(long var0);
}
