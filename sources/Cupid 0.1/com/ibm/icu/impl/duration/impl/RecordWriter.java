package com.ibm.icu.impl.duration.impl;

interface RecordWriter {
  boolean open(String paramString);
  
  boolean close();
  
  void bool(String paramString, boolean paramBoolean);
  
  void boolArray(String paramString, boolean[] paramArrayOfboolean);
  
  void character(String paramString, char paramChar);
  
  void characterArray(String paramString, char[] paramArrayOfchar);
  
  void namedIndex(String paramString, String[] paramArrayOfString, int paramInt);
  
  void namedIndexArray(String paramString, String[] paramArrayOfString, byte[] paramArrayOfbyte);
  
  void string(String paramString1, String paramString2);
  
  void stringArray(String paramString, String[] paramArrayOfString);
  
  void stringTable(String paramString, String[][] paramArrayOfString);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\impl\RecordWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */