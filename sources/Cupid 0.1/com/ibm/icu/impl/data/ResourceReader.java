package com.ibm.icu.impl.data;

import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.PatternProps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ResourceReader {
  private BufferedReader reader;
  
  private String resourceName;
  
  private String encoding;
  
  private Class<?> root;
  
  private int lineNo;
  
  public ResourceReader(String resourceName, String encoding) throws UnsupportedEncodingException {
    this(ICUData.class, "data/" + resourceName, encoding);
  }
  
  public ResourceReader(String resourceName) {
    this(ICUData.class, "data/" + resourceName);
  }
  
  public ResourceReader(Class<?> rootClass, String resourceName, String encoding) throws UnsupportedEncodingException {
    this.root = rootClass;
    this.resourceName = resourceName;
    this.encoding = encoding;
    this.lineNo = -1;
    _reset();
  }
  
  public ResourceReader(InputStream is, String resourceName, String encoding) {
    this.root = null;
    this.resourceName = resourceName;
    this.encoding = encoding;
    this.lineNo = -1;
    try {
      InputStreamReader isr = (encoding == null) ? new InputStreamReader(is) : new InputStreamReader(is, encoding);
      this.reader = new BufferedReader(isr);
      this.lineNo = 0;
    } catch (UnsupportedEncodingException e) {}
  }
  
  public ResourceReader(InputStream is, String resourceName) {
    this(is, resourceName, (String)null);
  }
  
  public ResourceReader(Class<?> rootClass, String resourceName) {
    this.root = rootClass;
    this.resourceName = resourceName;
    this.encoding = null;
    this.lineNo = -1;
    try {
      _reset();
    } catch (UnsupportedEncodingException e) {}
  }
  
  public String readLine() throws IOException {
    if (this.lineNo == 0) {
      this.lineNo++;
      String line = this.reader.readLine();
      if (line.charAt(0) == '￯' || line.charAt(0) == '﻿')
        line = line.substring(1); 
      return line;
    } 
    this.lineNo++;
    return this.reader.readLine();
  }
  
  public String readLineSkippingComments(boolean trim) throws IOException {
    String line;
    int pos;
    while (true) {
      line = readLine();
      if (line == null)
        return line; 
      pos = PatternProps.skipWhiteSpace(line, 0);
      if (pos == line.length() || line.charAt(pos) == '#')
        continue; 
      break;
    } 
    if (trim)
      line = line.substring(pos); 
    return line;
  }
  
  public String readLineSkippingComments() throws IOException {
    return readLineSkippingComments(false);
  }
  
  public int getLineNumber() {
    return this.lineNo;
  }
  
  public String describePosition() {
    return this.resourceName + ':' + this.lineNo;
  }
  
  public void reset() {
    try {
      _reset();
    } catch (UnsupportedEncodingException e) {}
  }
  
  private void _reset() throws UnsupportedEncodingException {
    if (this.lineNo == 0)
      return; 
    InputStream is = ICUData.getStream(this.root, this.resourceName);
    if (is == null)
      throw new IllegalArgumentException("Can't open " + this.resourceName); 
    InputStreamReader isr = (this.encoding == null) ? new InputStreamReader(is) : new InputStreamReader(is, this.encoding);
    this.reader = new BufferedReader(isr);
    this.lineNo = 0;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\ResourceReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */