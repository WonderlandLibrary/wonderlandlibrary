package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.ArmorStandReplacement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.EndermiteReplacement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.GuardianReplacement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ReplacementRegistry1_7_6_10to1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.storage.BlockState;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.List;
import java.util.UUID;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import us.myles.ViaVersion.packets.State;

public class SpawnPackets {
  public static void register(Protocol protocol) {
    protocol.registerOutgoing(State.PLAY, 12, 12, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    UUID uuid = (UUID)packetWrapper.read(Type.UUID);
                    packetWrapper.write(Type.STRING, uuid.toString());
                    GameProfileStorage gameProfileStorage = (GameProfileStorage)packetWrapper.user().get(GameProfileStorage.class);
                    GameProfileStorage.GameProfile gameProfile = gameProfileStorage.get(uuid);
                    if (gameProfile == null) {
                      packetWrapper.write(Type.STRING, "");
                      packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(0));
                    } else {
                      packetWrapper.write(Type.STRING, (gameProfile.name.length() > 16) ? gameProfile.name.substring(0, 16) : gameProfile.name);
                      packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(gameProfile.properties.size()));
                      for (GameProfileStorage.Property property : gameProfile.properties) {
                        packetWrapper.write(Type.STRING, property.name);
                        packetWrapper.write(Type.STRING, property.value);
                        packetWrapper.write(Type.STRING, (property.signature == null) ? "" : property.signature);
                      } 
                    } 
                    if (gameProfile != null && gameProfile.gamemode == 3) {
                      int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                      PacketWrapper equipmentPacket = new PacketWrapper(4, null, packetWrapper.user());
                      equipmentPacket.write(Type.INT, Integer.valueOf(entityId));
                      equipmentPacket.write((Type)Type.SHORT, Short.valueOf((short)4));
                      equipmentPacket.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, gameProfile.getSkull());
                      PacketUtil.sendPacket(equipmentPacket, Protocol1_7_6_10TO1_8.class);
                      short i;
                      for (i = 0; i < 4; i = (short)(i + 1)) {
                        equipmentPacket = new PacketWrapper(4, null, packetWrapper.user());
                        equipmentPacket.write(Type.INT, Integer.valueOf(entityId));
                        equipmentPacket.write((Type)Type.SHORT, Short.valueOf(i));
                        equipmentPacket.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, null);
                        PacketUtil.sendPacket(equipmentPacket, Protocol1_7_6_10TO1_8.class);
                      } 
                    } 
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    tracker.addPlayer((Integer)packetWrapper.get((Type)Type.VAR_INT, 0), uuid);
                  }
                });
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map(Type.BYTE);
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map(Types1_8.METADATA_LIST, Types1_7_6_10.METADATA_LIST);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    List<Metadata> metadata = (List<Metadata>)packetWrapper.get(Types1_7_6_10.METADATA_LIST, 0);
                    MetadataRewriter.transform(Entity1_10Types.EntityType.PLAYER, metadata);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    tracker.getClientEntityTypes().put(Integer.valueOf(entityId), Entity1_10Types.EntityType.PLAYER);
                    tracker.sendMetadataBuffer(entityId);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 14, 14, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    byte typeId = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    int x = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    int y = ((Integer)packetWrapper.get(Type.INT, 1)).intValue();
                    int z = ((Integer)packetWrapper.get(Type.INT, 2)).intValue();
                    byte pitch = ((Byte)packetWrapper.get(Type.BYTE, 1)).byteValue();
                    byte yaw = ((Byte)packetWrapper.get(Type.BYTE, 2)).byteValue();
                    if (typeId == 71) {
                      switch (yaw) {
                        case -128:
                          z += 32;
                          yaw = 0;
                          break;
                        case -64:
                          x -= 32;
                          yaw = -64;
                          break;
                        case 0:
                          z -= 32;
                          yaw = Byte.MIN_VALUE;
                          break;
                        case 64:
                          x += 32;
                          yaw = 64;
                          break;
                      } 
                    } else if (typeId == 78) {
                      packetWrapper.cancel();
                      EntityTracker entityTracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                      ArmorStandReplacement armorStand = new ArmorStandReplacement(entityId, packetWrapper.user());
                      armorStand.setLocation(x / 32.0D, y / 32.0D, z / 32.0D);
                      armorStand.setYawPitch(yaw * 360.0F / 256.0F, pitch * 360.0F / 256.0F);
                      armorStand.setHeadYaw(yaw * 360.0F / 256.0F);
                      entityTracker.addEntityReplacement((EntityReplacement)armorStand);
                    } else if (typeId == 10) {
                      y += 12;
                    } 
                    packetWrapper.set(Type.BYTE, 0, Byte.valueOf(typeId));
                    packetWrapper.set(Type.INT, 0, Integer.valueOf(x));
                    packetWrapper.set(Type.INT, 1, Integer.valueOf(y));
                    packetWrapper.set(Type.INT, 2, Integer.valueOf(z));
                    packetWrapper.set(Type.BYTE, 1, Byte.valueOf(pitch));
                    packetWrapper.set(Type.BYTE, 2, Byte.valueOf(yaw));
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    Entity1_10Types.EntityType type = Entity1_10Types.getTypeFromId(typeId, true);
                    tracker.getClientEntityTypes().put(Integer.valueOf(entityId), type);
                    tracker.sendMetadataBuffer(entityId);
                    int data = ((Integer)packetWrapper.get(Type.INT, 3)).intValue();
                    if (type != null && type.isOrHasParent((EntityType)Entity1_10Types.EntityType.FALLING_BLOCK)) {
                      BlockState state = new BlockState(data & 0xFFF, data >> 12 & 0xF);
                      state = ReplacementRegistry1_7_6_10to1_8.replace(state);
                      packetWrapper.set(Type.INT, 3, Integer.valueOf(data = state.getId() | state.getData() << 16));
                    } 
                    if (data > 0) {
                      packetWrapper.passthrough((Type)Type.SHORT);
                      packetWrapper.passthrough((Type)Type.SHORT);
                      packetWrapper.passthrough((Type)Type.SHORT);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 15, 15, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.UNSIGNED_BYTE);
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            map(Types1_8.METADATA_LIST, Types1_7_6_10.METADATA_LIST);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    int typeId = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    int x = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    int y = ((Integer)packetWrapper.get(Type.INT, 1)).intValue();
                    int z = ((Integer)packetWrapper.get(Type.INT, 2)).intValue();
                    byte pitch = ((Byte)packetWrapper.get(Type.BYTE, 1)).byteValue();
                    byte yaw = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    byte headYaw = ((Byte)packetWrapper.get(Type.BYTE, 2)).byteValue();
                    if (typeId == 30) {
                      packetWrapper.cancel();
                      EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                      ArmorStandReplacement armorStand = new ArmorStandReplacement(entityId, packetWrapper.user());
                      armorStand.setLocation(x / 32.0D, y / 32.0D, z / 32.0D);
                      armorStand.setYawPitch(yaw * 360.0F / 256.0F, pitch * 360.0F / 256.0F);
                      armorStand.setHeadYaw(headYaw * 360.0F / 256.0F);
                      tracker.addEntityReplacement((EntityReplacement)armorStand);
                    } else if (typeId == 68) {
                      packetWrapper.cancel();
                      EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                      GuardianReplacement guardian = new GuardianReplacement(entityId, packetWrapper.user());
                      guardian.setLocation(x / 32.0D, y / 32.0D, z / 32.0D);
                      guardian.setYawPitch(yaw * 360.0F / 256.0F, pitch * 360.0F / 256.0F);
                      guardian.setHeadYaw(headYaw * 360.0F / 256.0F);
                      tracker.addEntityReplacement((EntityReplacement)guardian);
                    } else if (typeId == 67) {
                      packetWrapper.cancel();
                      EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                      EndermiteReplacement endermite = new EndermiteReplacement(entityId, packetWrapper.user());
                      endermite.setLocation(x / 32.0D, y / 32.0D, z / 32.0D);
                      endermite.setYawPitch(yaw * 360.0F / 256.0F, pitch * 360.0F / 256.0F);
                      endermite.setHeadYaw(headYaw * 360.0F / 256.0F);
                      tracker.addEntityReplacement((EntityReplacement)endermite);
                    } else if (typeId == 101 || typeId == 255 || typeId == -1) {
                      packetWrapper.cancel();
                    } 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    int typeId = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    tracker.getClientEntityTypes().put(Integer.valueOf(entityId), Entity1_10Types.getTypeFromId(typeId, false));
                    tracker.sendMetadataBuffer(entityId);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    List<Metadata> metadataList = (List<Metadata>)wrapper.get(Types1_7_6_10.METADATA_LIST, 0);
                    int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)wrapper.user().get(EntityTracker.class);
                    if (tracker.getEntityReplacement(entityId) != null) {
                      tracker.getEntityReplacement(entityId).updateMetadata(metadataList);
                    } else if (tracker.getClientEntityTypes().containsKey(Integer.valueOf(entityId))) {
                      MetadataRewriter.transform((Entity1_10Types.EntityType)tracker.getClientEntityTypes().get(Integer.valueOf(entityId)), metadataList);
                    } else {
                      wrapper.cancel();
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 16, 16, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Position position = (Position)packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getX()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getY()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getZ()));
                  }
                });
            map(Type.UNSIGNED_BYTE, Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    tracker.getClientEntityTypes().put(Integer.valueOf(entityId), Entity1_10Types.EntityType.PAINTING);
                    tracker.sendMetadataBuffer(entityId);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 17, 17, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map((Type)Type.SHORT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    tracker.getClientEntityTypes().put(Integer.valueOf(entityId), Entity1_10Types.EntityType.EXPERIENCE_ORB);
                    tracker.sendMetadataBuffer(entityId);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 44, 44, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    tracker.getClientEntityTypes().put(Integer.valueOf(entityId), Entity1_10Types.EntityType.LIGHTNING);
                    tracker.sendMetadataBuffer(entityId);
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\packets\SpawnPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */