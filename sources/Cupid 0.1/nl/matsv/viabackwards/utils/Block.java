package nl.matsv.viabackwards.utils;

public class Block {
  private final int id;
  
  private final short data;
  
  public Block(int id, int data) {
    this.id = id;
    this.data = (short)data;
  }
  
  public Block(int id) {
    this.id = id;
    this.data = 0;
  }
  
  public int getId() {
    return this.id;
  }
  
  public int getData() {
    return this.data;
  }
  
  public Block withData(int data) {
    return new Block(this.id, data);
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    Block block = (Block)o;
    if (this.id != block.id)
      return false; 
    return (this.data == block.data);
  }
  
  public int hashCode() {
    int result = this.id;
    result = 31 * result + this.data;
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackward\\utils\Block.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */