package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata.MetadataRewriter;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.ExternalJoinGameListener;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.Vector;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_8;

public class EntityTracker extends StoredObject implements ExternalJoinGameListener {
  private final Map<Integer, List<Integer>> vehicleMap = new ConcurrentHashMap<>();
  
  private final Map<Integer, Entity1_10Types.EntityType> clientEntityTypes = new ConcurrentHashMap<>();
  
  private final Map<Integer, List<Metadata>> metadataBuffer = new ConcurrentHashMap<>();
  
  private final Map<Integer, EntityReplacement> entityReplacements = new ConcurrentHashMap<>();
  
  private final Map<Integer, Vector> entityOffsets = new ConcurrentHashMap<>();
  
  private int playerId;
  
  private int playerGamemode = 0;
  
  public EntityTracker(UserConnection user) {
    super(user);
  }
  
  public void setPlayerId(int entityId) {
    this.playerId = entityId;
  }
  
  public int getPlayerId() {
    return this.playerId;
  }
  
  public int getPlayerGamemode() {
    return this.playerGamemode;
  }
  
  public void setPlayerGamemode(int playerGamemode) {
    this.playerGamemode = playerGamemode;
  }
  
  public void removeEntity(int entityId) {
    this.vehicleMap.remove(Integer.valueOf(entityId));
    this.vehicleMap.forEach((vehicle, passengers) -> passengers.remove(Integer.valueOf(entityId)));
    this.vehicleMap.entrySet().removeIf(entry -> ((List)entry.getValue()).isEmpty());
    this.clientEntityTypes.remove(Integer.valueOf(entityId));
    this.entityOffsets.remove(Integer.valueOf(entityId));
    if (this.entityReplacements.containsKey(Integer.valueOf(entityId)))
      ((EntityReplacement)this.entityReplacements.remove(Integer.valueOf(entityId))).despawn(); 
  }
  
  public void resetEntityOffset(int entityId) {
    this.entityOffsets.remove(Integer.valueOf(entityId));
  }
  
  public Vector getEntityOffset(int entityId) {
    return this.entityOffsets.computeIfAbsent(Integer.valueOf(entityId), key -> new Vector(0, 0, 0));
  }
  
  public void addToEntityOffset(int entityId, short relX, short relY, short relZ) {
    this.entityOffsets.compute(Integer.valueOf(entityId), (key, offset) -> {
          if (offset == null)
            return new Vector(relX, relY, relZ); 
          offset.setBlockX(offset.getBlockX() + relX);
          offset.setBlockY(offset.getBlockY() + relY);
          offset.setBlockZ(offset.getBlockZ() + relZ);
          return offset;
        });
  }
  
  public void setEntityOffset(int entityId, short relX, short relY, short relZ) {
    this.entityOffsets.compute(Integer.valueOf(entityId), (key, offset) -> {
          if (offset == null)
            return new Vector(relX, relY, relZ); 
          offset.setBlockX(relX);
          offset.setBlockY(relY);
          offset.setBlockZ(relZ);
          return offset;
        });
  }
  
  public void setEntityOffset(int entityId, Vector offset) {
    this.entityOffsets.put(Integer.valueOf(entityId), offset);
  }
  
  public List<Integer> getPassengers(int entityId) {
    return this.vehicleMap.getOrDefault(Integer.valueOf(entityId), new ArrayList<>());
  }
  
  public void setPassengers(int entityId, List<Integer> passengers) {
    this.vehicleMap.put(Integer.valueOf(entityId), passengers);
  }
  
  public void addEntityReplacement(EntityReplacement entityReplacement) {
    this.entityReplacements.put(Integer.valueOf(entityReplacement.getEntityId()), entityReplacement);
  }
  
  public EntityReplacement getEntityReplacement(int entityId) {
    return this.entityReplacements.get(Integer.valueOf(entityId));
  }
  
  public Map<Integer, Entity1_10Types.EntityType> getClientEntityTypes() {
    return this.clientEntityTypes;
  }
  
  public void addMetadataToBuffer(int entityID, List<Metadata> metadataList) {
    if (this.metadataBuffer.containsKey(Integer.valueOf(entityID))) {
      ((List<Metadata>)this.metadataBuffer.get(Integer.valueOf(entityID))).addAll(metadataList);
    } else if (!metadataList.isEmpty()) {
      this.metadataBuffer.put(Integer.valueOf(entityID), metadataList);
    } 
  }
  
  public List<Metadata> getBufferedMetadata(int entityId) {
    return this.metadataBuffer.get(Integer.valueOf(entityId));
  }
  
  public boolean isInsideVehicle(int entityId) {
    for (List<Integer> vehicle : this.vehicleMap.values()) {
      if (vehicle.contains(Integer.valueOf(entityId)))
        return true; 
    } 
    return false;
  }
  
  public int getVehicle(int passenger) {
    for (Map.Entry<Integer, List<Integer>> vehicle : this.vehicleMap.entrySet()) {
      if (((List)vehicle.getValue()).contains(Integer.valueOf(passenger)))
        return ((Integer)vehicle.getKey()).intValue(); 
    } 
    return -1;
  }
  
  public boolean isPassenger(int vehicle, int passenger) {
    return (this.vehicleMap.containsKey(Integer.valueOf(vehicle)) && ((List)this.vehicleMap.get(Integer.valueOf(vehicle))).contains(Integer.valueOf(passenger)));
  }
  
  public void sendMetadataBuffer(int entityId) {
    if (!this.metadataBuffer.containsKey(Integer.valueOf(entityId)))
      return; 
    if (this.entityReplacements.containsKey(Integer.valueOf(entityId))) {
      ((EntityReplacement)this.entityReplacements.get(Integer.valueOf(entityId))).updateMetadata(this.metadataBuffer.remove(Integer.valueOf(entityId)));
    } else {
      PacketWrapper wrapper = new PacketWrapper(28, null, getUser());
      wrapper.write((Type)Type.VAR_INT, Integer.valueOf(entityId));
      wrapper.write(Types1_8.METADATA_LIST, this.metadataBuffer.get(Integer.valueOf(entityId)));
      MetadataRewriter.transform(getClientEntityTypes().get(Integer.valueOf(entityId)), this.metadataBuffer.get(Integer.valueOf(entityId)));
      if (!((List)this.metadataBuffer.get(Integer.valueOf(entityId))).isEmpty())
        try {
          wrapper.send(Protocol1_8TO1_9.class);
        } catch (Exception ex) {
          ex.printStackTrace();
        }  
      this.metadataBuffer.remove(Integer.valueOf(entityId));
    } 
  }
  
  public void onExternalJoinGame(int playerEntityId) {
    this.clientEntityTypes.remove(Integer.valueOf(this.playerId));
    this.playerId = playerEntityId;
    this.clientEntityTypes.put(Integer.valueOf(this.playerId), Entity1_10Types.EntityType.ENTITY_HUMAN);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\storage\EntityTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */