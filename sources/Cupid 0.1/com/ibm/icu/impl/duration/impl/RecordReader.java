package com.ibm.icu.impl.duration.impl;

interface RecordReader {
  boolean open(String paramString);
  
  boolean close();
  
  boolean bool(String paramString);
  
  boolean[] boolArray(String paramString);
  
  char character(String paramString);
  
  char[] characterArray(String paramString);
  
  byte namedIndex(String paramString, String[] paramArrayOfString);
  
  byte[] namedIndexArray(String paramString, String[] paramArrayOfString);
  
  String string(String paramString);
  
  String[] stringArray(String paramString);
  
  String[][] stringTable(String paramString);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\impl\RecordReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */