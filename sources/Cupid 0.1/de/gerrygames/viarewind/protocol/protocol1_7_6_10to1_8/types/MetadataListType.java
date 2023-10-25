package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.type.types.minecraft.MetaListTypeTemplate;

public class MetadataListType extends MetaListTypeTemplate {
  private MetadataType metadataType = new MetadataType();
  
  public List<Metadata> read(ByteBuf buffer) throws Exception {
    Metadata m;
    ArrayList<Metadata> list = new ArrayList<>();
    do {
      m = (Metadata)Types1_7_6_10.METADATA.read(buffer);
      if (m == null)
        continue; 
      list.add(m);
    } while (m != null);
    if (find(2, "Slot", list) != null && find(8, "Slot", list) != null)
      list.removeIf(metadata -> (metadata.getId() == 2 || metadata.getId() == 3)); 
    return list;
  }
  
  private Metadata find(int id, String type, List<Metadata> list) {
    for (Metadata metadata : list) {
      if (metadata.getId() == id && metadata.getMetaType().toString().equals(type))
        return metadata; 
    } 
    return null;
  }
  
  public void write(ByteBuf buffer, List<Metadata> metadata) throws Exception {
    for (Metadata meta : metadata)
      Types1_7_6_10.METADATA.write(buffer, meta); 
    if (metadata.isEmpty())
      Types1_7_6_10.METADATA.write(buffer, new Metadata(0, MetaType1_7_6_10.Byte, Byte.valueOf((byte)0))); 
    buffer.writeByte(127);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\types\MetadataListType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */