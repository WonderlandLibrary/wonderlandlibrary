package org.apache.http.protocol;

public interface HttpContext {
  public static final String RESERVED_PREFIX = "http.";
  
  Object getAttribute(String paramString);
  
  void setAttribute(String paramString, Object paramObject);
  
  Object removeAttribute(String paramString);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\protocol\HttpContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */