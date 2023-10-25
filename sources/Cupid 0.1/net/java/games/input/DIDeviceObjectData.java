package net.java.games.input;

final class DIDeviceObjectData {
  private int format_offset;
  
  private int data;
  
  private int millis;
  
  private int sequence;
  
  public final void set(int format_offset, int data, int millis, int sequence) {
    this.format_offset = format_offset;
    this.data = data;
    this.millis = millis;
    this.sequence = sequence;
  }
  
  public final void set(DIDeviceObjectData other) {
    set(other.format_offset, other.data, other.millis, other.sequence);
  }
  
  public final int getData() {
    return this.data;
  }
  
  public final int getFormatOffset() {
    return this.format_offset;
  }
  
  public final long getNanos() {
    return this.millis * 1000000L;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\DIDeviceObjectData.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */