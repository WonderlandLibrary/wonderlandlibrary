package io.netty.handler.codec.http;

import java.util.Set;

public interface Cookie extends Comparable<Cookie> {
  String getName();
  
  String getValue();
  
  void setValue(String paramString);
  
  String getDomain();
  
  void setDomain(String paramString);
  
  String getPath();
  
  void setPath(String paramString);
  
  String getComment();
  
  void setComment(String paramString);
  
  long getMaxAge();
  
  void setMaxAge(long paramLong);
  
  int getVersion();
  
  void setVersion(int paramInt);
  
  boolean isSecure();
  
  void setSecure(boolean paramBoolean);
  
  boolean isHttpOnly();
  
  void setHttpOnly(boolean paramBoolean);
  
  String getCommentUrl();
  
  void setCommentUrl(String paramString);
  
  boolean isDiscard();
  
  void setDiscard(boolean paramBoolean);
  
  Set<Integer> getPorts();
  
  void setPorts(int... paramVarArgs);
  
  void setPorts(Iterable<Integer> paramIterable);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\Cookie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */