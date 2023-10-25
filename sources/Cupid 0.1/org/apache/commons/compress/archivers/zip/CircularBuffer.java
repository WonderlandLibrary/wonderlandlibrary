package org.apache.commons.compress.archivers.zip;

class CircularBuffer {
  private final int size;
  
  private final byte[] buffer;
  
  private int readIndex;
  
  private int writeIndex;
  
  CircularBuffer(int size) {
    this.size = size;
    this.buffer = new byte[size];
  }
  
  public boolean available() {
    return (this.readIndex != this.writeIndex);
  }
  
  public void put(int value) {
    this.buffer[this.writeIndex] = (byte)value;
    this.writeIndex = (this.writeIndex + 1) % this.size;
  }
  
  public int get() {
    if (available()) {
      int value = this.buffer[this.readIndex];
      this.readIndex = (this.readIndex + 1) % this.size;
      return value & 0xFF;
    } 
    return -1;
  }
  
  public void copy(int distance, int length) {
    int pos1 = this.writeIndex - distance;
    int pos2 = pos1 + length;
    for (int i = pos1; i < pos2; i++) {
      this.buffer[this.writeIndex] = this.buffer[(i + this.size) % this.size];
      this.writeIndex = (this.writeIndex + 1) % this.size;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\CircularBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */