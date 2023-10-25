package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

public class StringBuilderWriter extends Writer implements Serializable {
  private static final long serialVersionUID = -146927496096066153L;
  
  private final StringBuilder builder;
  
  public StringBuilderWriter() {
    this.builder = new StringBuilder();
  }
  
  public StringBuilderWriter(int capacity) {
    this.builder = new StringBuilder(capacity);
  }
  
  public StringBuilderWriter(StringBuilder builder) {
    this.builder = (builder != null) ? builder : new StringBuilder();
  }
  
  public Writer append(char value) {
    this.builder.append(value);
    return this;
  }
  
  public Writer append(CharSequence value) {
    this.builder.append(value);
    return this;
  }
  
  public Writer append(CharSequence value, int start, int end) {
    this.builder.append(value, start, end);
    return this;
  }
  
  public void close() {}
  
  public void flush() {}
  
  public void write(String value) {
    if (value != null)
      this.builder.append(value); 
  }
  
  public void write(char[] value, int offset, int length) {
    if (value != null)
      this.builder.append(value, offset, length); 
  }
  
  public StringBuilder getBuilder() {
    return this.builder;
  }
  
  public String toString() {
    return this.builder.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\StringBuilderWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */