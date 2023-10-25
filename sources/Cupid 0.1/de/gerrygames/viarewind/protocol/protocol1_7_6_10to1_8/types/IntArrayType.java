package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.Type;

public class IntArrayType extends Type<int[]> {
  public IntArrayType() {
    super(int[].class);
  }
  
  public int[] read(ByteBuf byteBuf) throws Exception {
    byte size = byteBuf.readByte();
    int[] array = new int[size];
    byte i;
    for (i = 0; i < size; i = (byte)(i + 1))
      array[i] = byteBuf.readInt(); 
    return array;
  }
  
  public void write(ByteBuf byteBuf, int[] array) throws Exception {
    byteBuf.writeByte(array.length);
    for (int i : array)
      byteBuf.writeInt(i); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\types\IntArrayType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */