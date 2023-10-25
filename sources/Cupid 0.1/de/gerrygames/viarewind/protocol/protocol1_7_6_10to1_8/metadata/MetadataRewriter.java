package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata;

import de.gerrygames.viarewind.ViaRewind;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ItemRewriter;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.MetaType1_7_6_10;
import de.gerrygames.viarewind.protocol.protocol1_8to1_7_6_10.metadata.MetaIndex1_8to1_7_6_10;
import java.util.ArrayList;
import java.util.List;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_8;

public class MetadataRewriter {
  public static void transform(Entity1_10Types.EntityType type, List<Metadata> list) {
    for (Metadata entry : new ArrayList(list)) {
      MetaIndex1_8to1_7_6_10 metaIndex = MetaIndex1_7_6_10to1_8.searchIndex(type, entry.getId());
      try {
        if (metaIndex == null)
          throw new Exception("Could not find valid metadata"); 
        if (metaIndex.getOldType() == MetaType1_7_6_10.NonExistent) {
          list.remove(entry);
          continue;
        } 
        Object value = entry.getValue();
        if (!value.getClass().isAssignableFrom(metaIndex.getNewType().getType().getOutputClass())) {
          list.remove(entry);
          continue;
        } 
        entry.setMetaType((MetaType)metaIndex.getOldType());
        entry.setId(metaIndex.getIndex());
        switch (metaIndex.getOldType()) {
          case Int:
            if (metaIndex.getNewType() == MetaType1_8.Byte) {
              entry.setValue(Integer.valueOf(((Byte)value).intValue()));
              if (metaIndex == MetaIndex1_8to1_7_6_10.ENTITY_AGEABLE_AGE && (
                (Integer)entry.getValue()).intValue() < 0)
                entry.setValue(Integer.valueOf(-25000)); 
            } 
            if (metaIndex.getNewType() == MetaType1_8.Short)
              entry.setValue(Integer.valueOf(((Short)value).intValue())); 
            if (metaIndex.getNewType() == MetaType1_8.Int)
              entry.setValue(value); 
            continue;
          case Byte:
            if (metaIndex.getNewType() == MetaType1_8.Int)
              entry.setValue(Byte.valueOf(((Integer)value).byteValue())); 
            if (metaIndex.getNewType() == MetaType1_8.Byte) {
              if (metaIndex == MetaIndex1_8to1_7_6_10.ITEM_FRAME_ROTATION)
                value = Byte.valueOf(Integer.valueOf(((Byte)value).byteValue() / 2).byteValue()); 
              entry.setValue(value);
            } 
            if (metaIndex == MetaIndex1_8to1_7_6_10.HUMAN_SKIN_FLAGS) {
              byte flags = ((Byte)value).byteValue();
              boolean cape = ((flags & 0x1) != 0);
              flags = (byte)(cape ? 0 : 2);
              entry.setValue(Byte.valueOf(flags));
            } 
            continue;
          case Slot:
            entry.setValue(ItemRewriter.toClient((Item)value));
            continue;
          case Float:
            entry.setValue(value);
            continue;
          case Short:
            entry.setValue(value);
            continue;
          case String:
            entry.setValue(value);
            continue;
          case Position:
            entry.setValue(value);
            continue;
        } 
        ViaRewind.getPlatform().getLogger().warning("[Out] Unhandled MetaDataType: " + metaIndex.getNewType());
        list.remove(entry);
      } catch (Exception e) {
        list.remove(entry);
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\metadata\MetadataRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */