package org.apache.http.cookie;

import java.util.Date;

public interface Cookie {
  String getName();
  
  String getValue();
  
  String getComment();
  
  String getCommentURL();
  
  Date getExpiryDate();
  
  boolean isPersistent();
  
  String getDomain();
  
  String getPath();
  
  int[] getPorts();
  
  boolean isSecure();
  
  int getVersion();
  
  boolean isExpired(Date paramDate);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\cookie\Cookie.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */