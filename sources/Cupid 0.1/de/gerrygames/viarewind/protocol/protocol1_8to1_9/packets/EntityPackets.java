package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ItemRewriter;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata.MetadataRewriter;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Cooldown;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Levitation;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.PlayerPosition;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.util.RelativeMoveUtil;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.Pair;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.Vector;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import us.myles.ViaVersion.api.type.types.version.Types1_9;
import us.myles.ViaVersion.packets.State;

public class EntityPackets {
  public static void register(Protocol protocol) {
    protocol.registerOutgoing(State.PLAY, 27, 26, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    byte status = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                    if (status > 23) {
                      packetWrapper.cancel();
                      return;
                    } 
                    packetWrapper.write(Type.BYTE, Byte.valueOf(status));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 37, 21, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    int relX = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    int relY = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    int relZ = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                      packetWrapper.cancel();
                      replacement.relMove(relX / 4096.0D, relY / 4096.0D, relZ / 4096.0D);
                      return;
                    } 
                    Vector[] moves = RelativeMoveUtil.calculateRelativeMoves(packetWrapper.user(), entityId, relX, relY, relZ);
                    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)moves[0].getBlockX()));
                    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)moves[0].getBlockY()));
                    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)moves[0].getBlockZ()));
                    boolean onGround = ((Boolean)packetWrapper.passthrough(Type.BOOLEAN)).booleanValue();
                    if (moves.length > 1) {
                      PacketWrapper secondPacket = new PacketWrapper(21, null, packetWrapper.user());
                      secondPacket.write((Type)Type.VAR_INT, packetWrapper.get((Type)Type.VAR_INT, 0));
                      secondPacket.write(Type.BYTE, Byte.valueOf((byte)moves[1].getBlockX()));
                      secondPacket.write(Type.BYTE, Byte.valueOf((byte)moves[1].getBlockY()));
                      secondPacket.write(Type.BYTE, Byte.valueOf((byte)moves[1].getBlockZ()));
                      secondPacket.write(Type.BOOLEAN, Boolean.valueOf(onGround));
                      PacketUtil.sendPacket(secondPacket, Protocol1_8TO1_9.class);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 38, 23, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    int relX = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    int relY = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    int relZ = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                      packetWrapper.cancel();
                      replacement.relMove(relX / 4096.0D, relY / 4096.0D, relZ / 4096.0D);
                      replacement.setYawPitch(((Byte)packetWrapper.read(Type.BYTE)).byteValue() * 360.0F / 256.0F, ((Byte)packetWrapper.read(Type.BYTE)).byteValue() * 360.0F / 256.0F);
                      return;
                    } 
                    Vector[] moves = RelativeMoveUtil.calculateRelativeMoves(packetWrapper.user(), entityId, relX, relY, relZ);
                    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)moves[0].getBlockX()));
                    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)moves[0].getBlockY()));
                    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)moves[0].getBlockZ()));
                    byte yaw = ((Byte)packetWrapper.passthrough(Type.BYTE)).byteValue();
                    byte pitch = ((Byte)packetWrapper.passthrough(Type.BYTE)).byteValue();
                    boolean onGround = ((Boolean)packetWrapper.passthrough(Type.BOOLEAN)).booleanValue();
                    Entity1_10Types.EntityType type = (Entity1_10Types.EntityType)((EntityTracker)packetWrapper.user().get(EntityTracker.class)).getClientEntityTypes().get(Integer.valueOf(entityId));
                    if (type == Entity1_10Types.EntityType.BOAT) {
                      yaw = (byte)(yaw - 64);
                      packetWrapper.set(Type.BYTE, 3, Byte.valueOf(yaw));
                    } 
                    if (moves.length > 1) {
                      PacketWrapper secondPacket = new PacketWrapper(23, null, packetWrapper.user());
                      secondPacket.write((Type)Type.VAR_INT, packetWrapper.get((Type)Type.VAR_INT, 0));
                      secondPacket.write(Type.BYTE, Byte.valueOf((byte)moves[1].getBlockX()));
                      secondPacket.write(Type.BYTE, Byte.valueOf((byte)moves[1].getBlockY()));
                      secondPacket.write(Type.BYTE, Byte.valueOf((byte)moves[1].getBlockZ()));
                      secondPacket.write(Type.BYTE, Byte.valueOf(yaw));
                      secondPacket.write(Type.BYTE, Byte.valueOf(pitch));
                      secondPacket.write(Type.BOOLEAN, Boolean.valueOf(onGround));
                      PacketUtil.sendPacket(secondPacket, Protocol1_8TO1_9.class);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 39, 22, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
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
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    Entity1_10Types.EntityType type = (Entity1_10Types.EntityType)((EntityTracker)packetWrapper.user().get(EntityTracker.class)).getClientEntityTypes().get(Integer.valueOf(entityId));
                    if (type == Entity1_10Types.EntityType.BOAT) {
                      byte yaw = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                      yaw = (byte)(yaw - 64);
                      packetWrapper.set(Type.BYTE, 0, Byte.valueOf(yaw));
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 40, 20);
    protocol.registerOutgoing(State.PLAY, 41, 24, new PacketRemapper() {
          public void registerMap() {
            create(new ValueCreator() {
                  public void write(PacketWrapper packetWrapper) throws Exception {
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    int vehicle = tracker.getVehicle(tracker.getPlayerId());
                    if (vehicle == -1)
                      packetWrapper.cancel(); 
                    packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(vehicle));
                  }
                });
            map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
            map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
            map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
            map((Type)Type.FLOAT, Protocol1_8TO1_9.DEGREES_TO_ANGLE);
            map((Type)Type.FLOAT, Protocol1_8TO1_9.DEGREES_TO_ANGLE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    if (packetWrapper.isCancelled())
                      return; 
                    PlayerPosition position = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    double x = ((Integer)packetWrapper.get(Type.INT, 0)).intValue() / 32.0D;
                    double y = ((Integer)packetWrapper.get(Type.INT, 1)).intValue() / 32.0D;
                    double z = ((Integer)packetWrapper.get(Type.INT, 2)).intValue() / 32.0D;
                    position.setPos(x, y, z);
                  }
                });
            create(new ValueCreator() {
                  public void write(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.write(Type.BOOLEAN, Boolean.valueOf(true));
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    Entity1_10Types.EntityType type = (Entity1_10Types.EntityType)((EntityTracker)packetWrapper.user().get(EntityTracker.class)).getClientEntityTypes().get(Integer.valueOf(entityId));
                    if (type == Entity1_10Types.EntityType.BOAT) {
                      byte yaw = ((Byte)packetWrapper.get(Type.BYTE, 1)).byteValue();
                      yaw = (byte)(yaw - 64);
                      packetWrapper.set(Type.BYTE, 0, Byte.valueOf(yaw));
                      int y = ((Integer)packetWrapper.get(Type.INT, 1)).intValue();
                      y += 10;
                      packetWrapper.set(Type.INT, 1, Integer.valueOf(y));
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 47, 10);
    protocol.registerOutgoing(State.PLAY, 48, 19, new PacketRemapper() {
          public void registerMap() {
            map(Type.VAR_INT_ARRAY_PRIMITIVE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    for (int entityId : (int[])packetWrapper.get(Type.VAR_INT_ARRAY_PRIMITIVE, 0))
                      tracker.removeEntity(entityId); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 49, 30, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int id = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    if (id > 23)
                      packetWrapper.cancel(); 
                    if (id == 25) {
                      if (((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue() != ((EntityTracker)packetWrapper.user().get(EntityTracker.class)).getPlayerId())
                        return; 
                      Levitation levitation = (Levitation)packetWrapper.user().get(Levitation.class);
                      levitation.setActive(false);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 52, 25, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
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
    protocol.registerOutgoing(State.PLAY, 57, 28, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Types1_9.METADATA_LIST, Types1_8.METADATA_LIST);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    List<Metadata> metadataList = (List<Metadata>)wrapper.get(Types1_8.METADATA_LIST, 0);
                    int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)wrapper.user().get(EntityTracker.class);
                    if (tracker.getClientEntityTypes().containsKey(Integer.valueOf(entityId))) {
                      MetadataRewriter.transform((Entity1_10Types.EntityType)tracker.getClientEntityTypes().get(Integer.valueOf(entityId)), metadataList);
                      if (metadataList.isEmpty())
                        wrapper.cancel(); 
                    } else {
                      tracker.addMetadataToBuffer(entityId, metadataList);
                      wrapper.cancel();
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 58, 27, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.INT);
            create(new ValueCreator() {
                  public void write(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.write(Type.BOOLEAN, Boolean.valueOf(true));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 59, 18);
    protocol.registerOutgoing(State.PLAY, 60, 4, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int slot = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    if (slot == 1) {
                      packetWrapper.cancel();
                    } else if (slot > 1) {
                      slot--;
                    } 
                    packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)slot));
                  }
                });
            map(Type.ITEM);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.set(Type.ITEM, 0, ItemRewriter.toClient((Item)packetWrapper.get(Type.ITEM, 0)));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 64, 27, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                    EntityTracker entityTracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    int vehicle = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    int count = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    ArrayList<Integer> passengers = new ArrayList<>();
                    for (int i = 0; i < count; ) {
                      passengers.add(packetWrapper.read((Type)Type.VAR_INT));
                      i++;
                    } 
                    List<Integer> oldPassengers = entityTracker.getPassengers(vehicle);
                    entityTracker.setPassengers(vehicle, passengers);
                    if (!oldPassengers.isEmpty())
                      for (Integer passenger : oldPassengers) {
                        PacketWrapper detach = new PacketWrapper(27, null, packetWrapper.user());
                        detach.write(Type.INT, passenger);
                        detach.write(Type.INT, Integer.valueOf(-1));
                        detach.write(Type.BOOLEAN, Boolean.valueOf(false));
                        PacketUtil.sendPacket(detach, Protocol1_8TO1_9.class);
                      }  
                    for (int j = 0; j < count; j++) {
                      int v = (j == 0) ? vehicle : ((Integer)passengers.get(j - 1)).intValue();
                      int p = ((Integer)passengers.get(j)).intValue();
                      PacketWrapper attach = new PacketWrapper(27, null, packetWrapper.user());
                      attach.write(Type.INT, Integer.valueOf(p));
                      attach.write(Type.INT, Integer.valueOf(v));
                      attach.write(Type.BOOLEAN, Boolean.valueOf(false));
                      PacketUtil.sendPacket(attach, Protocol1_8TO1_9.class);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 73, 13);
    protocol.registerOutgoing(State.PLAY, 74, 24, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
            map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
            map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    Entity1_10Types.EntityType type = (Entity1_10Types.EntityType)((EntityTracker)packetWrapper.user().get(EntityTracker.class)).getClientEntityTypes().get(Integer.valueOf(entityId));
                    if (type == Entity1_10Types.EntityType.BOAT) {
                      byte yaw = ((Byte)packetWrapper.get(Type.BYTE, 1)).byteValue();
                      yaw = (byte)(yaw - 64);
                      packetWrapper.set(Type.BYTE, 0, Byte.valueOf(yaw));
                      int y = ((Integer)packetWrapper.get(Type.INT, 1)).intValue();
                      y += 10;
                      packetWrapper.set(Type.INT, 1, Integer.valueOf(y));
                    } 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    ((EntityTracker)packetWrapper.user().get(EntityTracker.class)).resetEntityOffset(entityId);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                      packetWrapper.cancel();
                      int x = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                      int y = ((Integer)packetWrapper.get(Type.INT, 1)).intValue();
                      int z = ((Integer)packetWrapper.get(Type.INT, 2)).intValue();
                      int yaw = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                      int pitch = ((Byte)packetWrapper.get(Type.BYTE, 1)).byteValue();
                      replacement.setLocation(x / 32.0D, y / 32.0D, z / 32.0D);
                      replacement.setYawPitch(yaw * 360.0F / 256.0F, pitch * 360.0F / 256.0F);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 75, 32, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    boolean player = (((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue() == ((EntityTracker)packetWrapper.user().get(EntityTracker.class)).getPlayerId());
                    int size = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    int removed = 0;
                    for (int i = 0; i < size; i++) {
                      String key = (String)packetWrapper.read(Type.STRING);
                      boolean skip = !Protocol1_8TO1_9.VALID_ATTRIBUTES.contains(key);
                      double value = ((Double)packetWrapper.read(Type.DOUBLE)).doubleValue();
                      int modifiersize = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                      if (!skip) {
                        packetWrapper.write(Type.STRING, key);
                        packetWrapper.write(Type.DOUBLE, Double.valueOf(value));
                        packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(modifiersize));
                      } else {
                        removed++;
                      } 
                      ArrayList<Pair<Byte, Double>> modifiers = new ArrayList<>();
                      for (int j = 0; j < modifiersize; j++) {
                        UUID uuid = (UUID)packetWrapper.read(Type.UUID);
                        double amount = ((Double)packetWrapper.read(Type.DOUBLE)).doubleValue();
                        byte operation = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                        modifiers.add(new Pair(Byte.valueOf(operation), Double.valueOf(amount)));
                        if (!skip) {
                          packetWrapper.write(Type.UUID, uuid);
                          packetWrapper.write(Type.DOUBLE, Double.valueOf(amount));
                          packetWrapper.write(Type.BYTE, Byte.valueOf(operation));
                        } 
                      } 
                      if (player && key.equals("generic.attackSpeed"))
                        ((Cooldown)packetWrapper.user().get(Cooldown.class)).setAttackSpeed(value, modifiers); 
                    } 
                    packetWrapper.set(Type.INT, 0, Integer.valueOf(size - removed));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 76, 29, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            map(Type.BYTE);
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int id = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    if (id > 23)
                      packetWrapper.cancel(); 
                    if (id == 25) {
                      if (((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue() != ((EntityTracker)packetWrapper.user().get(EntityTracker.class)).getPlayerId())
                        return; 
                      Levitation levitation = (Levitation)packetWrapper.user().get(Levitation.class);
                      levitation.setActive(true);
                      levitation.setAmplifier(((Byte)packetWrapper.get(Type.BYTE, 1)).byteValue());
                    } 
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\packets\EntityPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */