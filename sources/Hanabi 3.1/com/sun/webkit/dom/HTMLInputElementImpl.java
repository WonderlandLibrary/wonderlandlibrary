package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

public class HTMLInputElementImpl extends HTMLElementImpl implements HTMLInputElement {
   HTMLInputElementImpl(long var1) {
      super(var1);
   }

   static HTMLInputElement getImpl(long var0) {
      return (HTMLInputElement)create(var0);
   }

   public String getAccept() {
      return getAcceptImpl(this.getPeer());
   }

   static native String getAcceptImpl(long var0);

   public void setAccept(String var1) {
      setAcceptImpl(this.getPeer(), var1);
   }

   static native void setAcceptImpl(long var0, String var2);

   public String getAlt() {
      return getAltImpl(this.getPeer());
   }

   static native String getAltImpl(long var0);

   public void setAlt(String var1) {
      setAltImpl(this.getPeer(), var1);
   }

   static native void setAltImpl(long var0, String var2);

   public String getAutocomplete() {
      return getAutocompleteImpl(this.getPeer());
   }

   static native String getAutocompleteImpl(long var0);

   public void setAutocomplete(String var1) {
      setAutocompleteImpl(this.getPeer(), var1);
   }

   static native void setAutocompleteImpl(long var0, String var2);

   public boolean getAutofocus() {
      return getAutofocusImpl(this.getPeer());
   }

   static native boolean getAutofocusImpl(long var0);

   public void setAutofocus(boolean var1) {
      setAutofocusImpl(this.getPeer(), var1);
   }

   static native void setAutofocusImpl(long var0, boolean var2);

   public boolean getDefaultChecked() {
      return getDefaultCheckedImpl(this.getPeer());
   }

   static native boolean getDefaultCheckedImpl(long var0);

   public void setDefaultChecked(boolean var1) {
      setDefaultCheckedImpl(this.getPeer(), var1);
   }

   static native void setDefaultCheckedImpl(long var0, boolean var2);

   public boolean getChecked() {
      return getCheckedImpl(this.getPeer());
   }

   static native boolean getCheckedImpl(long var0);

   public void setChecked(boolean var1) {
      setCheckedImpl(this.getPeer(), var1);
   }

   static native void setCheckedImpl(long var0, boolean var2);

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

   public String getFormAction() {
      return getFormActionImpl(this.getPeer());
   }

   static native String getFormActionImpl(long var0);

   public void setFormAction(String var1) {
      setFormActionImpl(this.getPeer(), var1);
   }

   static native void setFormActionImpl(long var0, String var2);

   public String getFormEnctype() {
      return getFormEnctypeImpl(this.getPeer());
   }

   static native String getFormEnctypeImpl(long var0);

   public void setFormEnctype(String var1) {
      setFormEnctypeImpl(this.getPeer(), var1);
   }

   static native void setFormEnctypeImpl(long var0, String var2);

   public String getFormMethod() {
      return getFormMethodImpl(this.getPeer());
   }

   static native String getFormMethodImpl(long var0);

   public void setFormMethod(String var1) {
      setFormMethodImpl(this.getPeer(), var1);
   }

   static native void setFormMethodImpl(long var0, String var2);

   public boolean getFormNoValidate() {
      return getFormNoValidateImpl(this.getPeer());
   }

   static native boolean getFormNoValidateImpl(long var0);

   public void setFormNoValidate(boolean var1) {
      setFormNoValidateImpl(this.getPeer(), var1);
   }

   static native void setFormNoValidateImpl(long var0, boolean var2);

   public String getFormTarget() {
      return getFormTargetImpl(this.getPeer());
   }

   static native String getFormTargetImpl(long var0);

   public void setFormTarget(String var1) {
      setFormTargetImpl(this.getPeer(), var1);
   }

   static native void setFormTargetImpl(long var0, String var2);

   public int getHeight() {
      return getHeightImpl(this.getPeer());
   }

   static native int getHeightImpl(long var0);

   public void setHeight(int var1) {
      setHeightImpl(this.getPeer(), var1);
   }

   static native void setHeightImpl(long var0, int var2);

   public boolean getIndeterminate() {
      return getIndeterminateImpl(this.getPeer());
   }

   static native boolean getIndeterminateImpl(long var0);

   public void setIndeterminate(boolean var1) {
      setIndeterminateImpl(this.getPeer(), var1);
   }

   static native void setIndeterminateImpl(long var0, boolean var2);

   public String getMax() {
      return getMaxImpl(this.getPeer());
   }

   static native String getMaxImpl(long var0);

   public void setMax(String var1) {
      setMaxImpl(this.getPeer(), var1);
   }

   static native void setMaxImpl(long var0, String var2);

   public int getMaxLength() {
      return getMaxLengthImpl(this.getPeer());
   }

   static native int getMaxLengthImpl(long var0);

   public void setMaxLength(int var1) throws DOMException {
      setMaxLengthImpl(this.getPeer(), var1);
   }

   static native void setMaxLengthImpl(long var0, int var2);

   public String getMin() {
      return getMinImpl(this.getPeer());
   }

   static native String getMinImpl(long var0);

   public void setMin(String var1) {
      setMinImpl(this.getPeer(), var1);
   }

   static native void setMinImpl(long var0, String var2);

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

   public String getPattern() {
      return getPatternImpl(this.getPeer());
   }

   static native String getPatternImpl(long var0);

   public void setPattern(String var1) {
      setPatternImpl(this.getPeer(), var1);
   }

   static native void setPatternImpl(long var0, String var2);

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

   public String getSize() {
      return getSizeImpl(this.getPeer()) + "";
   }

   static native String getSizeImpl(long var0);

   public void setSize(String var1) {
      setSizeImpl(this.getPeer(), var1);
   }

   static native void setSizeImpl(long var0, String var2);

   public String getSrc() {
      return getSrcImpl(this.getPeer());
   }

   static native String getSrcImpl(long var0);

   public void setSrc(String var1) {
      setSrcImpl(this.getPeer(), var1);
   }

   static native void setSrcImpl(long var0, String var2);

   public String getStep() {
      return getStepImpl(this.getPeer());
   }

   static native String getStepImpl(long var0);

   public void setStep(String var1) {
      setStepImpl(this.getPeer(), var1);
   }

   static native void setStepImpl(long var0, String var2);

   public String getType() {
      return getTypeImpl(this.getPeer());
   }

   static native String getTypeImpl(long var0);

   public void setType(String var1) {
      setTypeImpl(this.getPeer(), var1);
   }

   static native void setTypeImpl(long var0, String var2);

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

   public long getValueAsDate() {
      return getValueAsDateImpl(this.getPeer());
   }

   static native long getValueAsDateImpl(long var0);

   public void setValueAsDate(long var1) throws DOMException {
      setValueAsDateImpl(this.getPeer(), var1);
   }

   static native void setValueAsDateImpl(long var0, long var2);

   public double getValueAsNumber() {
      return getValueAsNumberImpl(this.getPeer());
   }

   static native double getValueAsNumberImpl(long var0);

   public void setValueAsNumber(double var1) throws DOMException {
      setValueAsNumberImpl(this.getPeer(), var1);
   }

   static native void setValueAsNumberImpl(long var0, double var2);

   public int getWidth() {
      return getWidthImpl(this.getPeer());
   }

   static native int getWidthImpl(long var0);

   public void setWidth(int var1) {
      setWidthImpl(this.getPeer(), var1);
   }

   static native void setWidthImpl(long var0, int var2);

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

   public String getAlign() {
      return getAlignImpl(this.getPeer());
   }

   static native String getAlignImpl(long var0);

   public void setAlign(String var1) {
      setAlignImpl(this.getPeer(), var1);
   }

   static native void setAlignImpl(long var0, String var2);

   public String getUseMap() {
      return getUseMapImpl(this.getPeer());
   }

   static native String getUseMapImpl(long var0);

   public void setUseMap(String var1) {
      setUseMapImpl(this.getPeer(), var1);
   }

   static native void setUseMapImpl(long var0, String var2);

   public boolean getIncremental() {
      return getIncrementalImpl(this.getPeer());
   }

   static native boolean getIncrementalImpl(long var0);

   public void setIncremental(boolean var1) {
      setIncrementalImpl(this.getPeer(), var1);
   }

   static native void setIncrementalImpl(long var0, boolean var2);

   public String getAccessKey() {
      return getAccessKeyImpl(this.getPeer());
   }

   static native String getAccessKeyImpl(long var0);

   public void setAccessKey(String var1) {
      setAccessKeyImpl(this.getPeer(), var1);
   }

   static native void setAccessKeyImpl(long var0, String var2);

   public void stepUp(int var1) throws DOMException {
      stepUpImpl(this.getPeer(), var1);
   }

   static native void stepUpImpl(long var0, int var2);

   public void stepDown(int var1) throws DOMException {
      stepDownImpl(this.getPeer(), var1);
   }

   static native void stepDownImpl(long var0, int var2);

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

   public void click() {
      clickImpl(this.getPeer());
   }

   static native void clickImpl(long var0);

   public void setValueForUser(String var1) {
      setValueForUserImpl(this.getPeer(), var1);
   }

   static native void setValueForUserImpl(long var0, String var2);
}
