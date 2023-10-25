package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.PartialType;
import us.myles.ViaVersion.api.type.Type;

public class CustomStringType extends PartialType<String[], Integer> {
  public CustomStringType(Integer param) {
    super(param, String[].class);
  }
  
  public String[] read(ByteBuf buffer, Integer size) throws Exception {
    if (buffer.readableBytes() < size.intValue() / 4)
      throw new RuntimeException("Readable bytes does not match expected!"); 
    String[] array = new String[size.intValue()];
    for (int i = 0; i < size.intValue(); i++)
      array[i] = (String)Type.STRING.read(buffer); 
    return array;
  }
  
  public void write(ByteBuf buffer, Integer size, String[] strings) throws Exception {
    for (String s : strings)
      Type.STRING.write(buffer, s); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\types\CustomStringType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */