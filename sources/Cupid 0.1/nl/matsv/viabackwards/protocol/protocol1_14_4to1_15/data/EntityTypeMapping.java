package nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data;

import us.myles.ViaVersion.api.entities.Entity1_14Types;

public class EntityTypeMapping {
  public static int getOldEntityId(int entityId) {
    if (entityId == 4)
      return Entity1_14Types.EntityType.PUFFERFISH.getId(); 
    return (entityId >= 5) ? (entityId - 1) : entityId;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_14_4to1_15\data\EntityTypeMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */