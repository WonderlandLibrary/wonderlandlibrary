package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLSelectElement;

public class HTMLSelectElementImpl extends HTMLElementImpl implements HTMLSelectElement {
   HTMLSelectElementImpl(long var1) {
      super(var1);
   }

   static HTMLSelectElement getImpl(long var0) {
      return (HTMLSelectElement)create(var0);
   }

   public boolean getAutofocus() {
      return getAutofocusImpl(this.getPeer());
   }

   static native boolean getAutofocusImpl(long var0);

   public void setAutofocus(boolean var1) {
      setAutofocusImpl(this.getPeer(), var1);
   }

   static native void setAutofocusImpl(long var0, boolean var2);

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

   public boolean getMultiple() {
      return getMultipleImpl(this.getPeer());
   }

   static native boolean getMultipleImpl(long var0);

   public void setMultiple(boolean var1) {
      setMultipleImpl(this.getPeer(), var1);
   }

   static native void setMultipleImpl(long var0, boolean var2);

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public void setName(String var1) {
      setNameImpl(this.getPeer(), var1);
   }

   static native void setNameImpl(long var0, String var2);

   public boolean getRequired() {
      return getRequiredImpl(this.getPeer());
   }

   static native boolean getRequiredImpl(long var0);

   public void setRequired(boolean var1) {
      setRequiredImpl(this.getPeer(), var1);
   }

   static native void setRequiredImpl(long var0, boolean var2);

   public int getSize() {
      return getSizeImpl(this.getPeer());
   }

   static native int getSizeImpl(long var0);

   public void setSize(int var1) {
      setSizeImpl(this.getPeer(), var1);
   }

   static native void setSizeImpl(long var0, int var2);

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public HTMLOptionsCollectionImpl getOptions() {
      return HTMLOptionsCollectionImpl.getImpl(getOptionsImpl(this.getPeer()));
   }

   static native long getOptionsImpl(long var0);

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public HTMLCollection getSelectedOptions() {
      return HTMLCollectionImpl.getImpl(getSelectedOptionsImpl(this.getPeer()));
   }

   static native long getSelectedOptionsImpl(long var0);

   public int getSelectedIndex() {
      return getSelectedIndexImpl(this.getPeer());
   }

   static native int getSelectedIndexImpl(long var0);

   public void setSelectedIndex(int var1) {
      setSelectedIndexImpl(this.getPeer(), var1);
   }

   static native void setSelectedIndexImpl(long var0, int var2);

   public String getValue() {
      return getValueImpl(this.getPeer());
   }

   static native String getValueImpl(long var0);

   public void setValue(String var1) {
      setValueImpl(this.getPeer(), var1);
   }

   static native void setValueImpl(long var0, String var2);

   public boolean getWillValidate() {
      return getWillValidateImpl(this.getPeer());
   }

   static native boolean getWillValidateImpl(long var0);

   public String getValidationMessage() {
      return getValidationMessageImpl(this.getPeer());
   }

   static native String getValidationMessageImpl(long var0);

   public NodeList getLabels() {
      return NodeListImpl.getImpl(getLabelsImpl(this.getPeer()));
   }

   static native long getLabelsImpl(long var0);

   public String getAutocomplete() {
      return getAutocompleteImpl(this.getPeer());
   }

   static native String getAutocompleteImpl(long var0);

   public void setAutocomplete(String var1) {
      setAutocompleteImpl(this.getPeer(), var1);
   }

   static native void setAutocompleteImpl(long var0, String var2);

   public Node item(int var1) {
      return NodeImpl.getImpl(itemImpl(this.getPeer(), var1));
   }

   static native long itemImpl(long var0, int var2);

   public Node namedItem(String var1) {
      return NodeImpl.getImpl(namedItemImpl(this.getPeer(), var1));
   }

   static native long namedItemImpl(long var0, String var2);

   public void add(HTMLElement var1, HTMLElement var2) throws DOMException {
      addImpl(this.getPeer(), HTMLElementImpl.getPeer(var1), HTMLElementImpl.getPeer(var2));
   }

   static native void addImpl(long var0, long var2, long var4);

   public void remove(int var1) {
      removeImpl(this.getPeer(), var1);
   }

   static native void removeImpl(long var0, int var2);

   public boolean checkValidity() {
      return checkValidityImpl(this.getPeer());
   }

   static native boolean checkValidityImpl(long var0);

   public void setCustomValidity(String var1) {
      setCustomValidityImpl(this.getPeer(), var1);
   }

   static native void setCustomValidityImpl(long var0, String var2);
}
