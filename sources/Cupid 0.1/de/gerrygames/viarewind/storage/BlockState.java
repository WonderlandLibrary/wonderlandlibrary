package de.gerrygames.viarewind.storage;

public class BlockState {
  private int id;
  
  private int data;
  
  public BlockState(int id, int data) {
    this.id = id;
    this.data = data;
  }
  
  public static BlockState rawToState(int raw) {
    return new BlockState(raw >> 4, raw & 0xF);
  }
  
  public static int stateToRaw(BlockState state) {
    return state.getId() << 4 | state.getData() & 0xF;
  }
  
  public int getId() {
    return this.id;
  }
  
  public int getData() {
    return this.data;
  }
  
  public String toString() {
    return "BlockState{id: " + this.id + ", data: " + this.data + "}";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\storage\BlockState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */