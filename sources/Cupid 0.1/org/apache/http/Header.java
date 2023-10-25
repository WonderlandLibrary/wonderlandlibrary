package org.apache.http;

public interface Header {
  String getName();
  
  String getValue();
  
  HeaderElement[] getElements() throws ParseException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\Header.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */