package com.google.gson;

public class JsonParseException extends RuntimeException {
  static final long serialVersionUID = -4086729973971783390L;
  
  public JsonParseException(String msg) {
    super(msg);
  }
  
  public JsonParseException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  public JsonParseException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\JsonParseException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */