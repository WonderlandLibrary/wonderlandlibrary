package com.ibm.icu.text;

abstract class CharsetRecognizer {
  abstract String getName();
  
  public String getLanguage() {
    return null;
  }
  
  abstract CharsetMatch match(CharsetDetector paramCharsetDetector);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CharsetRecognizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */