package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ItemRewriter;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.List;
import java.util.UUID;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import us.myles.ViaVersion.packets.State;

public class EntityPackets {
  public static void register(Protocol protocol) {
    protocol.registerOutgoing(State.PLAY, 4, 4, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            map((Type)Type.SHORT);
            map(Type.ITEM, Types1_7_6_10.COMPRESSED_NBT_ITEM);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Item item = (Item)packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                    ItemRewriter.toClient(item);
                    packetWrapper.set(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0, item);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    if (((Short)packetWrapper.get((Type)Type.SHORT, 0)).shortValue() > 4)
                      packetWrapper.cancel(); 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    if (packetWrapper.isCancelled())
                      return; 
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    UUID uuid = tracker.getPlayerUUID(((Integer)packetWrapper.get(Type.INT, 0)).intValue());
                    if (uuid == null)
                      return; 
                    Item[] equipment = tracker.getPlayerEquipment(uuid);
                    if (equipment == null)
                      tracker.setPlayerEquipment(uuid, equipment = new Item[5]); 
                    equipment[((Short)packetWrapper.get((Type)Type.SHORT, 0)).shortValue()] = (Item)packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                    GameProfileStorage storage = (GameProfileStorage)packetWrapper.user().get(GameProfileStorage.class);
                    GameProfileStorage.GameProfile profile = storage.get(uuid);
                    if (profile != null && profile.gamemode == 3)
                      packetWrapper.cancel(); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 10, 10, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Position position = (Position)packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getX()));
                    packetWrapper.write(Type.UNSIGNED_BYTE, Short.valueOf(position.getY()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getZ()));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 13, 13, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            map((Type)Type.VAR_INT, Type.INT);
          }
        });
    protocol.registerOutgoing(State.PLAY, 18, 18, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
          }
        });
    protocol.registerOutgoing(State.PLAY, 19, 19, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int[] entityIds = (int[])packetWrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    for (int entityId : entityIds)
                      tracker.removeEntity(entityId); 
                    while (entityIds.length > 127) {
                      int[] entityIds2 = new int[127];
                      System.arraycopy(entityIds, 0, entityIds2, 0, 127);
                      int[] temp = new int[entityIds.length - 127];
                      System.arraycopy(entityIds, 127, temp, 0, temp.length);
                      entityIds = temp;
                      PacketWrapper destroy = new PacketWrapper(19, null, packetWrapper.user());
                      destroy.write(Types1_7_6_10.INT_ARRAY, entityIds2);
                      PacketUtil.sendPacket(destroy, Protocol1_7_6_10TO1_8.class);
                    } 
                    packetWrapper.write(Types1_7_6_10.INT_ARRAY, entityIds);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 20, 20, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
          }
        });
    protocol.registerOutgoing(State.PLAY, 21, 21, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read(Type.BOOLEAN);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                      packetWrapper.cancel();
                      int x = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                      int y = ((Byte)packetWrapper.get(Type.BYTE, 1)).byteValue();
                      int z = ((Byte)packetWrapper.get(Type.BYTE, 2)).byteValue();
                      replacement.relMove(x / 32.0D, y / 32.0D, z / 32.0D);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 22, 22, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            map(Type.BYTE);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read(Type.BOOLEAN);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                      packetWrapper.cancel();
                      int yaw = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                      int pitch = ((Byte)packetWrapper.get(Type.BYTE, 1)).byteValue();
                      replacement.setYawPitch(yaw * 360.0F / 256.0F, pitch * 360.0F / 256.0F);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 23, 23, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read(Type.BOOLEAN);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                      packetWrapper.cancel();
                      int x = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                      int y = ((Byte)packetWrapper.get(Type.BYTE, 1)).byteValue();
                      int z = ((Byte)packetWrapper.get(Type.BYTE, 2)).byteValue();
                      int yaw = ((Byte)packetWrapper.get(Type.BYTE, 3)).byteValue();
                      int pitch = ((Byte)packetWrapper.get(Type.BYTE, 4)).byteValue();
                      replacement.relMove(x / 32.0D, y / 32.0D, z / 32.0D);
                      replacement.setYawPitch(yaw * 360.0F / 256.0F, pitch * 360.0F / 256.0F);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 24, 24, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map(Type.BYTE);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read(Type.BOOLEAN);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    Entity1_10Types.EntityType type = (Entity1_10Types.EntityType)tracker.getClientEntityTypes().get(Integer.valueOf(entityId));
                    if (type == Entity1_10Types.EntityType.MINECART_ABSTRACT) {
                      int y = ((Integer)packetWrapper.get(Type.INT, 2)).intValue();
                      y += 12;
                      packetWrapper.set(Type.INT, 2, Integer.valueOf(y));
                    } 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                      packetWrapper.cancel();
                      int x = ((Integer)packetWrapper.get(Type.INT, 1)).intValue();
                      int y = ((Integer)packetWrapper.get(Type.INT, 2)).intValue();
                      int z = ((Integer)packetWrapper.get(Type.INT, 3)).intValue();
                      int yaw = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                      int pitch = ((Byte)packetWrapper.get(Type.BYTE, 1)).byteValue();
                      replacement.setLocation(x / 32.0D, y / 32.0D, z / 32.0D);
                      replacement.setYawPitch(yaw * 360.0F / 256.0F, pitch * 360.0F / 256.0F);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 25, 25, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                      packetWrapper.cancel();
                      int yaw = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                      replacement.setHeadYaw(yaw * 360.0F / 256.0F);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 27, 27, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.INT);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    boolean leash = ((Boolean)packetWrapper.get(Type.BOOLEAN, 0)).booleanValue();
                    if (leash)
                      return; 
                    int passenger = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    int vehicle = ((Integer)packetWrapper.get(Type.INT, 1)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    tracker.setPassenger(vehicle, passenger);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 28, 28, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            map(Types1_8.METADATA_LIST, Types1_7_6_10.METADATA_LIST);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    List<Metadata> metadataList = (List<Metadata>)wrapper.get(Types1_7_6_10.METADATA_LIST, 0);
                    int entityId = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)wrapper.user().get(EntityTracker.class);
                    if (tracker.getClientEntityTypes().containsKey(Integer.valueOf(entityId))) {
                      EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                      if (replacement != null) {
                        wrapper.cancel();
                        replacement.updateMetadata(metadataList);
                      } else {
                        MetadataRewriter.transform((Entity1_10Types.EntityType)tracker.getClientEntityTypes().get(Integer.valueOf(entityId)), metadataList);
                        if (metadataList.isEmpty())
                          wrapper.cancel(); 
                      } 
                    } else {
                      tracker.addMetadataToBuffer(entityId, metadataList);
                      wrapper.cancel();
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 29, 29, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            map(Type.BYTE);
            map(Type.BYTE);
            map((Type)Type.VAR_INT, (Type)Type.SHORT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read(Type.BYTE);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 30, 30, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            map(Type.BYTE);
          }
        });
    protocol.registerOutgoing(State.PLAY, 32, 32, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    if (tracker.getEntityReplacement(entityId) != null) {
                      packetWrapper.cancel();
                      return;
                    } 
                    int amount = ((Integer)packetWrapper.passthrough(Type.INT)).intValue();
                    for (int i = 0; i < amount; i++) {
                      packetWrapper.passthrough(Type.STRING);
                      packetWrapper.passthrough(Type.DOUBLE);
                      int modifierlength = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                      packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)modifierlength));
                      for (int j = 0; j < modifierlength; j++) {
                        packetWrapper.passthrough(Type.UUID);
                        packetWrapper.passthrough(Type.DOUBLE);
                        packetWrapper.passthrough(Type.BYTE);
                      } 
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 73, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\packets\EntityPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */