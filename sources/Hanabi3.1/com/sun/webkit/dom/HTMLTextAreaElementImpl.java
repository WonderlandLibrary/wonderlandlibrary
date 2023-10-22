package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLTextAreaElement;

public class HTMLTextAreaElementImpl extends HTMLElementImpl implements HTMLTextAreaElement {
   HTMLTextAreaElementImpl(long var1) {
      super(var1);
   }

   static HTMLTextAreaElement getImpl(long var0) {
      return (HTMLTextAreaElement)create(var0);
   }

   public boolean getAutofocus() {
      return getAutofocusImpl(this.getPeer());
   }

   static native boolean getAutofocusImpl(long var0);

   public void setAutofocus(boolean var1) {
      setAutofocusImpl(this.getPeer(), var1);
   }

   static native void setAutofocusImpl(long var0, boolean var2);

   public String getDirName() {
      return getDirNameImpl(this.getPeer());
   }

   static native String getDirNameImpl(long var0);

   public void setDirName(String var1) {
      setDirNameImpl(this.getPeer(), var1);
   }

   static native void setDirNameImpl(long var0, String var2);

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

   public int getMaxLength() {
      return getMaxLengthImpl(this.getPeer());
   }

   static native int getMaxLengthImpl(long var0);

   public void setMaxLength(int var1) throws DOMException {
      setMaxLengthImpl(this.getPeer(), var1);
   }

   static native void setMaxLengthImpl(long var0, int var2);

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public void setName(String var1) {
      setNameImpl(this.getPeer(), var1);
   }

   static native void setNameImpl(long var0, String var2);

   public String getPlaceholder() {
      return getPlaceholderImpl(this.getPeer());
   }

   static native String getPlaceholderImpl(long var0);

   public void setPlaceholder(String var1) {
      setPlaceholderImpl(this.getPeer(), var1);
   }

   static native void setPlaceholderImpl(long var0, String var2);

   public boolean getReadOnly() {
      return getReadOnlyImpl(this.getPeer());
   }

   static native boolean getReadOnlyImpl(long var0);

   public void setReadOnly(boolean var1) {
      setReadOnlyImpl(this.getPeer(), var1);
   }

   static native void setReadOnlyImpl(long var0, boolean var2);

   public boolean getRequired() {
      return getRequiredImpl(this.getPeer());
   }

   static native boolean getRequiredImpl(long var0);

   public void setRequired(boolean var1) {
      setRequiredImpl(this.getPeer(), var1);
   }

   static native void setRequiredImpl(long var0, boolean var2);

   public int getRows() {
      return getRowsImpl(this.getPeer());
   }

   static native int getRowsImpl(long var0);

   public void setRows(int var1) {
      setRowsImpl(this.getPeer(), var1);
   }

   static native void setRowsImpl(long var0, int var2);

   public int getCols() {
      return getColsImpl(this.getPeer());
   }

   static native int getColsImpl(long var0);

   public void setCols(int var1) {
      setColsImpl(this.getPeer(), var1);
   }

   static native void setColsImpl(long var0, int var2);

   public String getWrap() {
      return getWrapImpl(this.getPeer());
   }

   static native String getWrapImpl(long var0);

   public void setWrap(String var1) {
      setWrapImpl(this.getPeer(), var1);
   }

   static native void setWrapImpl(long var0, String var2);

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public String getDefaultValue() {
      return getDefaultValueImpl(this.getPeer());
   }

   static native String getDefaultValueImpl(long var0);

   public void setDefaultValue(String var1) {
      setDefaultValueImpl(this.getPeer(), var1);
   }

   static native void setDefaultValueImpl(long var0, String var2);

   public String getValue() {
      return getValueImpl(this.getPeer());
   }

   static native String getValueImpl(long var0);

   public void setValue(String var1) {
      setValueImpl(this.getPeer(), var1);
   }

   static native void setValueImpl(long var0, String var2);

   public int getTextLength() {
      return getTextLengthImpl(this.getPeer());
   }

   static native int getTextLengthImpl(long var0);

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

   public int getSelectionStart() {
      return getSelectionStartImpl(this.getPeer());
   }

   static native int getSelectionStartImpl(long var0);

   public void setSelectionStart(int var1) {
      setSelectionStartImpl(this.getPeer(), var1);
   }

   static native void setSelectionStartImpl(long var0, int var2);

   public int getSelectionEnd() {
      return getSelectionEndImpl(this.getPeer());
   }

   static native int getSelectionEndImpl(long var0);

   public void setSelectionEnd(int var1) {
      setSelectionEndImpl(this.getPeer(), var1);
   }

   static native void setSelectionEndImpl(long var0, int var2);

   public String getSelectionDirection() {
      return getSelectionDirectionImpl(this.getPeer());
   }

   static native String getSelectionDirectionImpl(long var0);

   public void setSelectionDirection(String var1) {
      setSelectionDirectionImpl(this.getPeer(), var1);
   }

   static native void setSelectionDirectionImpl(long var0, String var2);

   public String getAccessKey() {
      return getAccessKeyImpl(this.getPeer());
   }

   static native String getAccessKeyImpl(long var0);

   public void setAccessKey(String var1) {
      setAccessKeyImpl(this.getPeer(), var1);
   }

   static native void setAccessKeyImpl(long var0, String var2);

   public String getAutocomplete() {
      return getAutocompleteImpl(this.getPeer());
   }

   static native String getAutocompleteImpl(long var0);

   public void setAutocomplete(String var1) {
      setAutocompleteImpl(this.getPeer(), var1);
   }

   static native void setAutocompleteImpl(long var0, String var2);

   public boolean checkValidity() {
      return checkValidityImpl(this.getPeer());
   }

   static native boolean checkValidityImpl(long var0);

   public void setCustomValidity(String var1) {
      setCustomValidityImpl(this.getPeer(), var1);
   }

   static native void setCustomValidityImpl(long var0, String var2);

   public void select() {
      selectImpl(this.getPeer());
   }

   static native void selectImpl(long var0);

   public void setRangeText(String var1) throws DOMException {
      setRangeTextImpl(this.getPeer(), var1);
   }

   static native void setRangeTextImpl(long var0, String var2);

   public void setRangeTextEx(String var1, int var2, int var3, String var4) throws DOMException {
      setRangeTextExImpl(this.getPeer(), var1, var2, var3, var4);
   }

   static native void setRangeTextExImpl(long var0, String var2, int var3, int var4, String var5);

   public void setSelectionRange(int var1, int var2, String var3) {
      setSelectionRangeImpl(this.getPeer(), var1, var2, var3);
   }

   static native void setSelectionRangeImpl(long var0, int var2, int var3, String var4);
}
