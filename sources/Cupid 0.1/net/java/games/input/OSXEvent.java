package net.java.games.input;

class OSXEvent {
  private long type;
  
  private long cookie;
  
  private int value;
  
  private long nanos;
  
  public void set(long type, long cookie, int value, long nanos) {
    this.type = type;
    this.cookie = cookie;
    this.value = value;
    this.nanos = nanos;
  }
  
  public long getType() {
    return this.type;
  }
  
  public long getCookie() {
    return this.cookie;
  }
  
  public int getValue() {
    return this.value;
  }
  
  public long getNanos() {
    return this.nanos;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\OSXEvent.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */