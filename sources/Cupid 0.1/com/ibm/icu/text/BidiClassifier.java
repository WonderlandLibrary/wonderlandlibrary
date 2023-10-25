package com.ibm.icu.text;

public class BidiClassifier {
  protected Object context;
  
  public BidiClassifier(Object context) {
    this.context = context;
  }
  
  public void setContext(Object context) {
    this.context = context;
  }
  
  public Object getContext() {
    return this.context;
  }
  
  public int classify(int c) {
    return 19;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\BidiClassifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */