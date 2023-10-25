package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.type.types.minecraft.MetaTypeTemplate;

public class MetadataType extends MetaTypeTemplate {
  public Metadata read(ByteBuf buffer) throws Exception {
    byte item = buffer.readByte();
    if (item == Byte.MAX_VALUE)
      return null; 
    int typeID = (item & 0xE0) >> 5;
    MetaType1_7_6_10 type = MetaType1_7_6_10.byId(typeID);
    int id = item & 0x1F;
    return new Metadata(id, type, type.getType().read(buffer));
  }
  
  public void write(ByteBuf buffer, Metadata meta) throws Exception {
    int item = (meta.getMetaType().getTypeID() << 5 | meta.getId() & 0x1F) & 0xFF;
    buffer.writeByte(item);
    meta.getMetaType().getType().write(buffer, meta.getValue());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\types\MetadataType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */