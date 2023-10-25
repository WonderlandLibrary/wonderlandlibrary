package net.minecraft.world.chunk;

public class NibbleArray {
  private final byte[] data;
  
  public NibbleArray() {
    this.data = new byte[2048];
  }
  
  public NibbleArray(byte[] storageArray) {
    this.data = storageArray;
    if (storageArray.length != 2048)
      throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + storageArray.length); 
  }
  
  public int get(int x, int y, int z) {
    return getFromIndex(getCoordinateIndex(x, y, z));
  }
  
  public void set(int x, int y, int z, int value) {
    setIndex(getCoordinateIndex(x, y, z), value);
  }
  
  private int getCoordinateIndex(int x, int y, int z) {
    return y << 8 | z << 4 | x;
  }
  
  public int getFromIndex(int index) {
    int i = getNibbleIndex(index);
    return isLowerNibble(index) ? (this.data[i] & 0xF) : (this.data[i] >> 4 & 0xF);
  }
  
  public void setIndex(int index, int value) {
    int i = getNibbleIndex(index);
    if (isLowerNibble(index)) {
      this.data[i] = (byte)(this.data[i] & 0xF0 | value & 0xF);
    } else {
      this.data[i] = (byte)(this.data[i] & 0xF | (value & 0xF) << 4);
    } 
  }
  
  private boolean isLowerNibble(int index) {
    return ((index & 0x1) == 0);
  }
  
  private int getNibbleIndex(int index) {
    return index >> 1;
  }
  
  public byte[] getData() {
    return this.data;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\chunk\NibbleArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */