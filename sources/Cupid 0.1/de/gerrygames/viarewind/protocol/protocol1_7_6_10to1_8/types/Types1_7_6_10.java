package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import java.util.List;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.type.Type;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;

public class Types1_7_6_10 {
  public static final Type<CompoundTag> COMPRESSED_NBT = new CompressedNBTType();
  
  public static final Type<Item[]> ITEM_ARRAY = new ItemArrayType(false);
  
  public static final Type<Item[]> COMPRESSED_NBT_ITEM_ARRAY = new ItemArrayType(true);
  
  public static final Type<Item> ITEM = new ItemType(false);
  
  public static final Type<Item> COMPRESSED_NBT_ITEM = new ItemType(true);
  
  public static final Type<List<Metadata>> METADATA_LIST = (Type<List<Metadata>>)new MetadataListType();
  
  public static final Type<Metadata> METADATA = (Type<Metadata>)new MetadataType();
  
  public static final Type<CompoundTag> NBT = new NBTType();
  
  public static final Type<int[]> INT_ARRAY = new IntArrayType();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\types\Types1_7_6_10.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */