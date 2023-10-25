package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.ExternalJoinGameListener;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_8;

public class EntityTracker extends StoredObject implements ExternalJoinGameListener {
  private final Map<Integer, Entity1_10Types.EntityType> clientEntityTypes = new ConcurrentHashMap<>();
  
  private final Map<Integer, List<Metadata>> metadataBuffer = new ConcurrentHashMap<>();
  
  private final Map<Integer, Integer> vehicles = new ConcurrentHashMap<>();
  
  private final Map<Integer, EntityReplacement> entityReplacements = new ConcurrentHashMap<>();
  
  private final Map<Integer, UUID> playersByEntityId = new HashMap<>();
  
  private final Map<UUID, Integer> playersByUniqueId = new HashMap<>();
  
  private final Map<UUID, Item[]> playerEquipment = (Map)new HashMap<>();
  
  private int gamemode = 0;
  
  private int playerId = -1;
  
  private int spectating = -1;
  
  private int dimension = 0;
  
  public EntityTracker(UserConnection user) {
    super(user);
  }
  
  public void removeEntity(int entityId) {
    this.clientEntityTypes.remove(Integer.valueOf(entityId));
    if (this.entityReplacements.containsKey(Integer.valueOf(entityId)))
      ((EntityReplacement)this.entityReplacements.remove(Integer.valueOf(entityId))).despawn(); 
    if (this.playersByEntityId.containsKey(Integer.valueOf(entityId)))
      this.playersByUniqueId.remove(this.playersByEntityId.remove(Integer.valueOf(entityId))); 
  }
  
  public void addPlayer(Integer entityId, UUID uuid) {
    this.playersByUniqueId.put(uuid, entityId);
    this.playersByEntityId.put(entityId, uuid);
  }
  
  public UUID getPlayerUUID(int entityId) {
    return this.playersByEntityId.get(Integer.valueOf(entityId));
  }
  
  public int getPlayerEntityId(UUID uuid) {
    return ((Integer)this.playersByUniqueId.getOrDefault(uuid, Integer.valueOf(-1))).intValue();
  }
  
  public Item[] getPlayerEquipment(UUID uuid) {
    return this.playerEquipment.get(uuid);
  }
  
  public void setPlayerEquipment(UUID uuid, Item[] equipment) {
    this.playerEquipment.put(uuid, equipment);
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
  
  public void addEntityReplacement(EntityReplacement entityReplacement) {
    this.entityReplacements.put(Integer.valueOf(entityReplacement.getEntityId()), entityReplacement);
  }
  
  public EntityReplacement getEntityReplacement(int entityId) {
    return this.entityReplacements.get(Integer.valueOf(entityId));
  }
  
  public List<Metadata> getBufferedMetadata(int entityId) {
    return this.metadataBuffer.get(Integer.valueOf(entityId));
  }
  
  public void sendMetadataBuffer(int entityId) {
    if (!this.metadataBuffer.containsKey(Integer.valueOf(entityId)))
      return; 
    if (this.entityReplacements.containsKey(Integer.valueOf(entityId))) {
      ((EntityReplacement)this.entityReplacements.get(Integer.valueOf(entityId))).updateMetadata(this.metadataBuffer.remove(Integer.valueOf(entityId)));
    } else {
      Entity1_10Types.EntityType type = getClientEntityTypes().get(Integer.valueOf(entityId));
      PacketWrapper wrapper = new PacketWrapper(28, null, getUser());
      wrapper.write((Type)Type.VAR_INT, Integer.valueOf(entityId));
      wrapper.write(Types1_8.METADATA_LIST, this.metadataBuffer.get(Integer.valueOf(entityId)));
      MetadataRewriter.transform(type, this.metadataBuffer.get(Integer.valueOf(entityId)));
      if (!((List)this.metadataBuffer.get(Integer.valueOf(entityId))).isEmpty())
        PacketUtil.sendPacket(wrapper, Protocol1_7_6_10TO1_8.class); 
      this.metadataBuffer.remove(Integer.valueOf(entityId));
    } 
  }
  
  public int getVehicle(int passengerId) {
    for (Map.Entry<Integer, Integer> vehicle : this.vehicles.entrySet()) {
      if (((Integer)vehicle.getValue()).intValue() == passengerId)
        return ((Integer)vehicle.getValue()).intValue(); 
    } 
    return -1;
  }
  
  public int getPassenger(int vehicleId) {
    return ((Integer)this.vehicles.getOrDefault(Integer.valueOf(vehicleId), Integer.valueOf(-1))).intValue();
  }
  
  public void setPassenger(int vehicleId, int passengerId) {
    if (vehicleId == this.spectating && this.spectating != this.playerId)
      try {
        PacketWrapper sneakPacket = new PacketWrapper(11, null, getUser());
        sneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(this.playerId));
        sneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
        sneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
        PacketWrapper unsneakPacket = new PacketWrapper(11, null, getUser());
        unsneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(this.playerId));
        unsneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(1));
        unsneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
        PacketUtil.sendToServer(sneakPacket, Protocol1_7_6_10TO1_8.class, true, true);
        setSpectating(this.playerId);
      } catch (Exception ex) {
        ex.printStackTrace();
      }  
    if (vehicleId == -1) {
      int oldVehicleId = getVehicle(passengerId);
      this.vehicles.remove(Integer.valueOf(oldVehicleId));
    } else if (passengerId == -1) {
      this.vehicles.remove(Integer.valueOf(vehicleId));
    } else {
      this.vehicles.put(Integer.valueOf(vehicleId), Integer.valueOf(passengerId));
    } 
  }
  
  public int getSpectating() {
    return this.spectating;
  }
  
  public boolean setSpectating(int spectating) {
    if (spectating != this.playerId && getPassenger(spectating) != -1) {
      PacketWrapper sneakPacket = new PacketWrapper(11, null, getUser());
      sneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(this.playerId));
      sneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
      sneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
      PacketWrapper unsneakPacket = new PacketWrapper(11, null, getUser());
      unsneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(this.playerId));
      unsneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(1));
      unsneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
      PacketUtil.sendToServer(sneakPacket, Protocol1_7_6_10TO1_8.class, true, true);
      setSpectating(this.playerId);
      return false;
    } 
    if (this.spectating != spectating && this.spectating != this.playerId) {
      PacketWrapper unmount = new PacketWrapper(27, null, getUser());
      unmount.write(Type.INT, Integer.valueOf(this.playerId));
      unmount.write(Type.INT, Integer.valueOf(-1));
      unmount.write(Type.BOOLEAN, Boolean.valueOf(false));
      PacketUtil.sendPacket(unmount, Protocol1_7_6_10TO1_8.class);
    } 
    this.spectating = spectating;
    if (spectating != this.playerId) {
      PacketWrapper mount = new PacketWrapper(27, null, getUser());
      mount.write(Type.INT, Integer.valueOf(this.playerId));
      mount.write(Type.INT, Integer.valueOf(this.spectating));
      mount.write(Type.BOOLEAN, Boolean.valueOf(false));
      PacketUtil.sendPacket(mount, Protocol1_7_6_10TO1_8.class);
    } 
    return true;
  }
  
  public int getGamemode() {
    return this.gamemode;
  }
  
  public void setGamemode(int gamemode) {
    this.gamemode = gamemode;
  }
  
  public int getPlayerId() {
    return this.playerId;
  }
  
  public void setPlayerId(int playerId) {
    if (this.playerId != -1)
      throw new IllegalStateException("playerId was already set!"); 
    this.playerId = this.spectating = playerId;
  }
  
  public void clearEntities() {
    this.clientEntityTypes.clear();
    this.entityReplacements.clear();
    this.vehicles.clear();
    this.metadataBuffer.clear();
  }
  
  public int getDimension() {
    return this.dimension;
  }
  
  public void setDimension(int dimension) {
    this.dimension = dimension;
  }
  
  public void onExternalJoinGame(int playerEntityId) {
    if (this.spectating == this.playerId)
      this.spectating = playerEntityId; 
    this.clientEntityTypes.remove(Integer.valueOf(this.playerId));
    this.playerId = playerEntityId;
    this.clientEntityTypes.put(Integer.valueOf(this.playerId), Entity1_10Types.EntityType.ENTITY_HUMAN);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\storage\EntityTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */