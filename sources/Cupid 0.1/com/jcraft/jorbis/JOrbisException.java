package com.jcraft.jorbis;

public class JOrbisException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public JOrbisException() {}
  
  public JOrbisException(String s) {
    super("JOrbis: " + s);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\jcraft\jorbis\JOrbisException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */