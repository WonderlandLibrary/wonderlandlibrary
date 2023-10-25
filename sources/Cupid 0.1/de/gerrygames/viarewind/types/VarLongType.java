package de.gerrygames.viarewind.types;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.Type;

public class VarLongType extends Type<Long> {
  public static final VarLongType VAR_LONG = new VarLongType();
  
  public VarLongType() {
    super("VarLong", Long.class);
  }
  
  public Long read(ByteBuf byteBuf) throws Exception {
    long i = 0L;
    int j = 0;
    while (true) {
      byte b0 = byteBuf.readByte();
      i |= ((b0 & Byte.MAX_VALUE) << j++ * 7);
      if (j > 10)
        throw new RuntimeException("VarLong too big"); 
      if ((b0 & 0x80) != 128)
        return Long.valueOf(i); 
    } 
  }
  
  public void write(ByteBuf byteBuf, Long i) throws Exception {
    while ((i.longValue() & 0xFFFFFFFFFFFFFF80L) != 0L) {
      byteBuf.writeByte((int)(i.longValue() & 0x7FL) | 0x80);
      i = Long.valueOf(i.longValue() >>> 7L);
    } 
    byteBuf.writeByte(i.intValue());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\types\VarLongType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */