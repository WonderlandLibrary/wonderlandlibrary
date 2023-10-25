package com.sun.jna;

public class LastErrorException extends RuntimeException {
  private int errorCode;
  
  private static String formatMessage(int code) {
    return Platform.isWindows() ? ("GetLastError() returned " + code) : ("errno was " + code);
  }
  
  private static String parseMessage(String m) {
    try {
      return formatMessage(Integer.parseInt(m));
    } catch (NumberFormatException e) {
      return m;
    } 
  }
  
  public LastErrorException(String msg) {
    super(parseMessage(msg.trim()));
    try {
      if (msg.startsWith("["))
        msg = msg.substring(1, msg.indexOf("]")); 
      this.errorCode = Integer.parseInt(msg);
    } catch (NumberFormatException e) {
      this.errorCode = -1;
    } 
  }
  
  public int getErrorCode() {
    return this.errorCode;
  }
  
  public LastErrorException(int code) {
    super(formatMessage(code));
    this.errorCode = code;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\LastErrorException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */