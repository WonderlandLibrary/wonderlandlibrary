package com.google.gson;

public final class JsonIOException extends JsonParseException {
  private static final long serialVersionUID = 1L;
  
  public JsonIOException(String msg) {
    super(msg);
  }
  
  public JsonIOException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  public JsonIOException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\JsonIOException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */