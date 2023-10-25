package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase {
  private String data;
  
  public NBTTagString() {
    this.data = "";
  }
  
  public NBTTagString(String data) {
    this.data = data;
    if (data == null)
      throw new IllegalArgumentException("Empty string not allowed"); 
  }
  
  void write(DataOutput output) throws IOException {
    output.writeUTF(this.data);
  }
  
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(288L);
    this.data = input.readUTF();
    sizeTracker.read((16 * this.data.length()));
  }
  
  public byte getId() {
    return 8;
  }
  
  public String toString() {
    return "\"" + this.data.replace("\"", "\\\"") + "\"";
  }
  
  public NBTBase copy() {
    return new NBTTagString(this.data);
  }
  
  public boolean hasNoTags() {
    return this.data.isEmpty();
  }
  
  public boolean equals(Object p_equals_1_) {
    if (!super.equals(p_equals_1_))
      return false; 
    NBTTagString nbttagstring = (NBTTagString)p_equals_1_;
    return ((this.data == null && nbttagstring.data == null) || (this.data != null && this.data.equals(nbttagstring.data)));
  }
  
  public int hashCode() {
    return super.hashCode() ^ this.data.hashCode();
  }
  
  public String getString() {
    return this.data;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\nbt\NBTTagString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */