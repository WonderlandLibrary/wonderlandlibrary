package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import com.google.common.base.Charsets;
import de.gerrygames.viarewind.ViaRewind;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.ArmorStandReplacement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ItemRewriter;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.provider.TitleRenderProvider;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerAbilities;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerPosition;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Windows;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.ChatUtil;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.utils.Utils;
import de.gerrygames.viarewind.utils.math.AABB;
import de.gerrygames.viarewind.utils.math.Ray3d;
import de.gerrygames.viarewind.utils.math.RayTracing;
import de.gerrygames.viarewind.utils.math.Vector3d;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.UUID;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.CustomByteType;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;

public class PlayerPackets {
  public static void register(Protocol protocol) {
    protocol.registerOutgoing(State.PLAY, 1, 1, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.UNSIGNED_BYTE);
            map(Type.BYTE);
            map(Type.UNSIGNED_BYTE);
            map(Type.UNSIGNED_BYTE);
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    if (!ViaRewind.getConfig().isReplaceAdventureMode())
                      return; 
                    if (((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue() == 2)
                      packetWrapper.set(Type.UNSIGNED_BYTE, 0, Short.valueOf((short)0)); 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read(Type.BOOLEAN);
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    tracker.setGamemode(((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue());
                    tracker.setPlayerId(((Integer)packetWrapper.get(Type.INT, 0)).intValue());
                    tracker.getClientEntityTypes().put(Integer.valueOf(tracker.getPlayerId()), Entity1_10Types.EntityType.ENTITY_HUMAN);
                    tracker.setDimension(((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue());
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    ClientWorld world = (ClientWorld)packetWrapper.user().get(ClientWorld.class);
                    world.setEnvironment(((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue());
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 2, 2, new PacketRemapper() {
          public void registerMap() {
            map(Type.COMPONENT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int position = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                    if (position == 2)
                      packetWrapper.cancel(); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 5, 5, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Position position = (Position)packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getX()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getY()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getZ()));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 6, 6, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.FLOAT);
            map((Type)Type.VAR_INT, (Type)Type.SHORT);
            map((Type)Type.FLOAT);
          }
        });
    protocol.registerOutgoing(State.PLAY, 7, 7, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.UNSIGNED_BYTE);
            map(Type.UNSIGNED_BYTE);
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    if (!ViaRewind.getConfig().isReplaceAdventureMode())
                      return; 
                    if (((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 1)).shortValue() == 2)
                      packetWrapper.set(Type.UNSIGNED_BYTE, 1, Short.valueOf((short)0)); 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    tracker.setGamemode(((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 1)).shortValue());
                    if (tracker.getDimension() != ((Integer)packetWrapper.get(Type.INT, 0)).intValue()) {
                      tracker.setDimension(((Integer)packetWrapper.get(Type.INT, 0)).intValue());
                      tracker.clearEntities();
                      tracker.getClientEntityTypes().put(Integer.valueOf(tracker.getPlayerId()), Entity1_10Types.EntityType.ENTITY_HUMAN);
                    } 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    ClientWorld world = (ClientWorld)packetWrapper.user().get(ClientWorld.class);
                    world.setEnvironment(((Integer)packetWrapper.get(Type.INT, 0)).intValue());
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 8, 8, new PacketRemapper() {
          public void registerMap() {
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    playerPosition.setPositionPacketReceived(true);
                    int flags = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                    if ((flags & 0x1) == 1) {
                      double x = ((Double)packetWrapper.get(Type.DOUBLE, 0)).doubleValue();
                      x += playerPosition.getPosX();
                      packetWrapper.set(Type.DOUBLE, 0, Double.valueOf(x));
                    } 
                    double y = ((Double)packetWrapper.get(Type.DOUBLE, 1)).doubleValue();
                    if ((flags & 0x2) == 2)
                      y += playerPosition.getPosY(); 
                    playerPosition.setReceivedPosY(y);
                    y += 1.6200000047683716D;
                    packetWrapper.set(Type.DOUBLE, 1, Double.valueOf(y));
                    if ((flags & 0x4) == 4) {
                      double z = ((Double)packetWrapper.get(Type.DOUBLE, 2)).doubleValue();
                      z += playerPosition.getPosZ();
                      packetWrapper.set(Type.DOUBLE, 2, Double.valueOf(z));
                    } 
                    if ((flags & 0x8) == 8) {
                      float yaw = ((Float)packetWrapper.get((Type)Type.FLOAT, 0)).floatValue();
                      yaw += playerPosition.getYaw();
                      packetWrapper.set((Type)Type.FLOAT, 0, Float.valueOf(yaw));
                    } 
                    if ((flags & 0x10) == 16) {
                      float pitch = ((Float)packetWrapper.get((Type)Type.FLOAT, 1)).floatValue();
                      pitch += playerPosition.getPitch();
                      packetWrapper.set((Type)Type.FLOAT, 1, Float.valueOf(pitch));
                    } 
                  }
                });
            create(new ValueCreator() {
                  public void write(PacketWrapper packetWrapper) throws Exception {
                    PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    packetWrapper.write(Type.BOOLEAN, Boolean.valueOf(playerPosition.isOnGround()));
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    if (tracker.getSpectating() != tracker.getPlayerId())
                      packetWrapper.cancel(); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 31, 31, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.FLOAT);
            map((Type)Type.VAR_INT, (Type)Type.SHORT);
            map((Type)Type.VAR_INT, (Type)Type.SHORT);
          }
        });
    protocol.registerOutgoing(State.PLAY, 43, 43, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.FLOAT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int mode = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    if (mode != 3)
                      return; 
                    int gamemode = ((Float)packetWrapper.get((Type)Type.FLOAT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    if (gamemode == 3 || tracker.getGamemode() == 3) {
                      Item[] equipment;
                      UUID uuid = ((ProtocolInfo)packetWrapper.user().get(ProtocolInfo.class)).getUuid();
                      if (gamemode == 3) {
                        GameProfileStorage.GameProfile profile = ((GameProfileStorage)packetWrapper.user().get(GameProfileStorage.class)).get(uuid);
                        equipment = new Item[5];
                        equipment[4] = profile.getSkull();
                      } else {
                        equipment = tracker.getPlayerEquipment(uuid);
                        if (equipment == null)
                          equipment = new Item[5]; 
                      } 
                      for (int i = 1; i < 5; i++) {
                        PacketWrapper setSlot = new PacketWrapper(47, null, packetWrapper.user());
                        setSlot.write(Type.BYTE, Byte.valueOf((byte)0));
                        setSlot.write((Type)Type.SHORT, Short.valueOf((short)(9 - i)));
                        setSlot.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, equipment[i]);
                        PacketUtil.sendPacket(setSlot, Protocol1_7_6_10TO1_8.class);
                      } 
                    } 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int mode = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    if (mode == 3) {
                      int gamemode = ((Float)packetWrapper.get((Type)Type.FLOAT, 0)).intValue();
                      if (gamemode == 2 && ViaRewind.getConfig().isReplaceAdventureMode()) {
                        gamemode = 0;
                        packetWrapper.set((Type)Type.FLOAT, 0, Float.valueOf(0.0F));
                      } 
                      ((EntityTracker)packetWrapper.user().get(EntityTracker.class)).setGamemode(gamemode);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 54, 54, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Position position = (Position)packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getX()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getY()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getZ()));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 56, 56, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                    int action = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    int count = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    GameProfileStorage gameProfileStorage = (GameProfileStorage)packetWrapper.user().get(GameProfileStorage.class);
                    for (int i = 0; i < count; i++) {
                      UUID uuid = (UUID)packetWrapper.read(Type.UUID);
                      if (action == 0) {
                        String name = (String)packetWrapper.read(Type.STRING);
                        GameProfileStorage.GameProfile gameProfile = gameProfileStorage.get(uuid);
                        if (gameProfile == null)
                          gameProfile = gameProfileStorage.put(uuid, name); 
                        int propertyCount = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                        while (propertyCount-- > 0)
                          gameProfile.properties.add(new GameProfileStorage.Property((String)packetWrapper.read(Type.STRING), (String)packetWrapper.read(Type.STRING), ((Boolean)packetWrapper.read(Type.BOOLEAN)).booleanValue() ? (String)packetWrapper.read(Type.STRING) : null)); 
                        int gamemode = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                        int ping = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                        gameProfile.ping = ping;
                        gameProfile.gamemode = gamemode;
                        if (((Boolean)packetWrapper.read(Type.BOOLEAN)).booleanValue())
                          gameProfile.setDisplayName((String)packetWrapper.read(Type.STRING)); 
                        PacketWrapper packet = new PacketWrapper(56, null, packetWrapper.user());
                        packet.write(Type.STRING, gameProfile.name);
                        packet.write(Type.BOOLEAN, Boolean.valueOf(true));
                        packet.write((Type)Type.SHORT, Short.valueOf((short)ping));
                        PacketUtil.sendPacket(packet, Protocol1_7_6_10TO1_8.class);
                      } else if (action == 1) {
                        int gamemode = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                        GameProfileStorage.GameProfile gameProfile = gameProfileStorage.get(uuid);
                        if (gameProfile != null && gameProfile.gamemode != gamemode) {
                          if (gamemode == 3 || gameProfile.gamemode == 3) {
                            EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                            int entityId = tracker.getPlayerEntityId(uuid);
                            if (entityId != -1) {
                              Item[] equipment;
                              if (gamemode == 3) {
                                equipment = new Item[5];
                                equipment[4] = gameProfile.getSkull();
                              } else {
                                equipment = tracker.getPlayerEquipment(uuid);
                                if (equipment == null)
                                  equipment = new Item[5]; 
                              } 
                              short slot;
                              for (slot = 0; slot < 5; slot = (short)(slot + 1)) {
                                PacketWrapper equipmentPacket = new PacketWrapper(4, null, packetWrapper.user());
                                equipmentPacket.write(Type.INT, Integer.valueOf(entityId));
                                equipmentPacket.write((Type)Type.SHORT, Short.valueOf(slot));
                                equipmentPacket.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, equipment[slot]);
                                PacketUtil.sendPacket(equipmentPacket, Protocol1_7_6_10TO1_8.class);
                              } 
                            } 
                          } 
                          gameProfile.gamemode = gamemode;
                        } 
                      } else if (action == 2) {
                        int ping = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                        GameProfileStorage.GameProfile gameProfile = gameProfileStorage.get(uuid);
                        if (gameProfile != null) {
                          gameProfile.ping = ping;
                          PacketWrapper packet = new PacketWrapper(56, null, packetWrapper.user());
                          packet.write(Type.STRING, gameProfile.name);
                          packet.write(Type.BOOLEAN, Boolean.valueOf(true));
                          packet.write((Type)Type.SHORT, Short.valueOf((short)ping));
                          PacketUtil.sendPacket(packet, Protocol1_7_6_10TO1_8.class);
                        } 
                      } else if (action == 3) {
                        String displayName = ((Boolean)packetWrapper.read(Type.BOOLEAN)).booleanValue() ? (String)packetWrapper.read(Type.STRING) : null;
                        GameProfileStorage.GameProfile gameProfile = gameProfileStorage.get(uuid);
                        if (gameProfile != null && (gameProfile.displayName != null || displayName != null))
                          if ((gameProfile.displayName == null && displayName != null) || (gameProfile.displayName != null && displayName == null) || !gameProfile.displayName.equals(displayName))
                            gameProfile.setDisplayName(displayName);  
                      } else if (action == 4) {
                        GameProfileStorage.GameProfile gameProfile = gameProfileStorage.remove(uuid);
                        if (gameProfile != null) {
                          PacketWrapper packet = new PacketWrapper(56, null, packetWrapper.user());
                          packet.write(Type.STRING, gameProfile.name);
                          packet.write(Type.BOOLEAN, Boolean.valueOf(false));
                          packet.write((Type)Type.SHORT, Short.valueOf((short)gameProfile.ping));
                          PacketUtil.sendPacket(packet, Protocol1_7_6_10TO1_8.class);
                        } 
                      } 
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 57, 57, new PacketRemapper() {
          public void registerMap() {
            map(Type.BYTE);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    byte flags = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    float flySpeed = ((Float)packetWrapper.get((Type)Type.FLOAT, 0)).floatValue();
                    float walkSpeed = ((Float)packetWrapper.get((Type)Type.FLOAT, 1)).floatValue();
                    PlayerAbilities abilities = (PlayerAbilities)packetWrapper.user().get(PlayerAbilities.class);
                    abilities.setInvincible(((flags & 0x8) == 8));
                    abilities.setAllowFly(((flags & 0x4) == 4));
                    abilities.setFlying(((flags & 0x2) == 2));
                    abilities.setCreative(((flags & 0x1) == 1));
                    abilities.setFlySpeed(flySpeed);
                    abilities.setWalkSpeed(walkSpeed);
                    if (abilities.isSprinting() && abilities.isFlying())
                      packetWrapper.set((Type)Type.FLOAT, 0, Float.valueOf(abilities.getFlySpeed() * 2.0F)); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 63, 63, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String channel = (String)packetWrapper.get(Type.STRING, 0);
                    if (channel.equalsIgnoreCase("MC|TrList")) {
                      int size;
                      packetWrapper.passthrough(Type.INT);
                      if (packetWrapper.isReadable(Type.BYTE, 0)) {
                        size = ((Byte)packetWrapper.passthrough(Type.BYTE)).byteValue();
                      } else {
                        size = ((Short)packetWrapper.passthrough(Type.UNSIGNED_BYTE)).shortValue();
                      } 
                      for (int i = 0; i < size; i++) {
                        Item item = ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM));
                        packetWrapper.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, item);
                        item = ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM));
                        packetWrapper.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, item);
                        boolean has3Items = ((Boolean)packetWrapper.passthrough(Type.BOOLEAN)).booleanValue();
                        if (has3Items) {
                          item = ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM));
                          packetWrapper.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, item);
                        } 
                        packetWrapper.passthrough(Type.BOOLEAN);
                        packetWrapper.read(Type.INT);
                        packetWrapper.read(Type.INT);
                      } 
                    } else if (channel.equalsIgnoreCase("MC|Brand")) {
                      packetWrapper.write(Type.REMAINING_BYTES, ((String)packetWrapper.read(Type.STRING)).getBytes(Charsets.UTF_8));
                    } 
                    packetWrapper.cancel();
                    packetWrapper.setId(-1);
                    ByteBuf newPacketBuf = Unpooled.buffer();
                    packetWrapper.writeToBuffer(newPacketBuf);
                    PacketWrapper newWrapper = new PacketWrapper(63, newPacketBuf, packetWrapper.user());
                    newWrapper.passthrough(Type.STRING);
                    if (newPacketBuf.readableBytes() <= 32767) {
                      newWrapper.write((Type)Type.SHORT, Short.valueOf((short)newPacketBuf.readableBytes()));
                      newWrapper.send(Protocol1_7_6_10TO1_8.class, true, true);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 67, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    int entityId = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    int spectating = tracker.getSpectating();
                    if (spectating != entityId)
                      tracker.setSpectating(entityId); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 69, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                    TitleRenderProvider titleRenderProvider = (TitleRenderProvider)Via.getManager().getProviders().get(TitleRenderProvider.class);
                    if (titleRenderProvider == null)
                      return; 
                    int action = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    UUID uuid = Utils.getUUID(packetWrapper.user());
                    switch (action) {
                      case 0:
                        titleRenderProvider.setTitle(uuid, (String)packetWrapper.read(Type.STRING));
                        break;
                      case 1:
                        titleRenderProvider.setSubTitle(uuid, (String)packetWrapper.read(Type.STRING));
                        break;
                      case 2:
                        titleRenderProvider.setTimings(uuid, ((Integer)packetWrapper.read(Type.INT)).intValue(), ((Integer)packetWrapper.read(Type.INT)).intValue(), ((Integer)packetWrapper.read(Type.INT)).intValue());
                        break;
                      case 3:
                        titleRenderProvider.clear(uuid);
                        break;
                      case 4:
                        titleRenderProvider.reset(uuid);
                        break;
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 71, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 72, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 1, 1, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String msg = (String)packetWrapper.get(Type.STRING, 0);
                    int gamemode = ((EntityTracker)packetWrapper.user().get(EntityTracker.class)).getGamemode();
                    if (gamemode == 3 && msg.toLowerCase().startsWith("/stp ")) {
                      String username = msg.split(" ")[1];
                      GameProfileStorage storage = (GameProfileStorage)packetWrapper.user().get(GameProfileStorage.class);
                      GameProfileStorage.GameProfile profile = storage.get(username, true);
                      if (profile != null && profile.uuid != null) {
                        packetWrapper.cancel();
                        PacketWrapper teleportPacket = new PacketWrapper(24, null, packetWrapper.user());
                        teleportPacket.write(Type.UUID, profile.uuid);
                        PacketUtil.sendToServer(teleportPacket, Protocol1_7_6_10TO1_8.class, true, true);
                      } 
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 2, 2, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT, (Type)Type.VAR_INT);
            map(Type.BYTE, (Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int mode = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 1)).intValue();
                    if (mode != 0)
                      return; 
                    int entityId = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (!(replacement instanceof ArmorStandReplacement))
                      return; 
                    ArmorStandReplacement armorStand = (ArmorStandReplacement)replacement;
                    AABB boundingBox = armorStand.getBoundingBox();
                    PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    Vector3d pos = new Vector3d(playerPosition.getPosX(), playerPosition.getPosY() + 1.8D, playerPosition.getPosZ());
                    double yaw = Math.toRadians(playerPosition.getYaw());
                    double pitch = Math.toRadians(playerPosition.getPitch());
                    Vector3d dir = new Vector3d(-Math.cos(pitch) * Math.sin(yaw), -Math.sin(pitch), Math.cos(pitch) * Math.cos(yaw));
                    Ray3d ray = new Ray3d(pos, dir);
                    Vector3d intersection = RayTracing.trace(ray, boundingBox, 5.0D);
                    if (intersection == null)
                      return; 
                    intersection.substract(boundingBox.getMin());
                    mode = 2;
                    packetWrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(mode));
                    packetWrapper.write((Type)Type.FLOAT, Float.valueOf((float)intersection.getX()));
                    packetWrapper.write((Type)Type.FLOAT, Float.valueOf((float)intersection.getY()));
                    packetWrapper.write((Type)Type.FLOAT, Float.valueOf((float)intersection.getZ()));
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 3, 3, new PacketRemapper() {
          public void registerMap() {
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    playerPosition.setOnGround(((Boolean)packetWrapper.get(Type.BOOLEAN, 0)).booleanValue());
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 4, 4, new PacketRemapper() {
          public void registerMap() {
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read(Type.DOUBLE);
                  }
                });
            map(Type.DOUBLE);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    double x = ((Double)packetWrapper.get(Type.DOUBLE, 0)).doubleValue();
                    double feetY = ((Double)packetWrapper.get(Type.DOUBLE, 1)).doubleValue();
                    double z = ((Double)packetWrapper.get(Type.DOUBLE, 2)).doubleValue();
                    PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    if (playerPosition.isPositionPacketReceived()) {
                      playerPosition.setPositionPacketReceived(false);
                      feetY -= 0.01D;
                      packetWrapper.set(Type.DOUBLE, 1, Double.valueOf(feetY));
                    } 
                    playerPosition.setOnGround(((Boolean)packetWrapper.get(Type.BOOLEAN, 0)).booleanValue());
                    playerPosition.setPos(x, feetY, z);
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 5, 5, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    playerPosition.setYaw(((Float)packetWrapper.get((Type)Type.FLOAT, 0)).floatValue());
                    playerPosition.setPitch(((Float)packetWrapper.get((Type)Type.FLOAT, 1)).floatValue());
                    playerPosition.setOnGround(((Boolean)packetWrapper.get(Type.BOOLEAN, 0)).booleanValue());
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 6, 6, new PacketRemapper() {
          public void registerMap() {
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read(Type.DOUBLE);
                  }
                });
            map(Type.DOUBLE);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    double x = ((Double)packetWrapper.get(Type.DOUBLE, 0)).doubleValue();
                    double feetY = ((Double)packetWrapper.get(Type.DOUBLE, 1)).doubleValue();
                    double z = ((Double)packetWrapper.get(Type.DOUBLE, 2)).doubleValue();
                    float yaw = ((Float)packetWrapper.get((Type)Type.FLOAT, 0)).floatValue();
                    float pitch = ((Float)packetWrapper.get((Type)Type.FLOAT, 1)).floatValue();
                    PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    if (playerPosition.isPositionPacketReceived()) {
                      playerPosition.setPositionPacketReceived(false);
                      feetY = playerPosition.getReceivedPosY();
                      packetWrapper.set(Type.DOUBLE, 1, Double.valueOf(feetY));
                    } 
                    playerPosition.setOnGround(((Boolean)packetWrapper.get(Type.BOOLEAN, 0)).booleanValue());
                    playerPosition.setPos(x, feetY, z);
                    playerPosition.setYaw(yaw);
                    playerPosition.setPitch(pitch);
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 7, 7, new PacketRemapper() {
          public void registerMap() {
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int x = ((Integer)packetWrapper.read(Type.INT)).intValue();
                    short y = ((Short)packetWrapper.read(Type.UNSIGNED_BYTE)).shortValue();
                    int z = ((Integer)packetWrapper.read(Type.INT)).intValue();
                    packetWrapper.write(Type.POSITION, new Position(x, y, z));
                  }
                });
            map(Type.BYTE);
          }
        });
    protocol.registerIncoming(State.PLAY, 8, 8, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int x = ((Integer)packetWrapper.read(Type.INT)).intValue();
                    short y = ((Short)packetWrapper.read(Type.UNSIGNED_BYTE)).shortValue();
                    int z = ((Integer)packetWrapper.read(Type.INT)).intValue();
                    packetWrapper.write(Type.POSITION, new Position(x, y, z));
                    packetWrapper.passthrough(Type.BYTE);
                    Item item = (Item)packetWrapper.read(Types1_7_6_10.COMPRESSED_NBT_ITEM);
                    item = ItemRewriter.toServer(item);
                    packetWrapper.write(Type.ITEM, item);
                    for (int i = 0; i < 3; i++)
                      packetWrapper.passthrough(Type.BYTE); 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 10, 10, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int entityId = ((Integer)packetWrapper.read(Type.INT)).intValue();
                    int animation = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                    if (animation == 1)
                      return; 
                    packetWrapper.cancel();
                    switch (animation) {
                      case 104:
                        animation = 0;
                        break;
                      case 105:
                        animation = 1;
                        break;
                      case 3:
                        animation = 2;
                        break;
                      default:
                        return;
                    } 
                    PacketWrapper entityAction = new PacketWrapper(11, null, packetWrapper.user());
                    entityAction.write((Type)Type.VAR_INT, Integer.valueOf(entityId));
                    entityAction.write((Type)Type.VAR_INT, Integer.valueOf(animation));
                    entityAction.write((Type)Type.VAR_INT, Integer.valueOf(0));
                    PacketUtil.sendPacket(entityAction, Protocol1_7_6_10TO1_8.class, true, true);
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 11, 11, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT, (Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Byte)packetWrapper.read(Type.BYTE)).byteValue() - 1));
                  }
                });
            map(Type.INT, (Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int action = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 1)).intValue();
                    if (action == 3 || action == 4) {
                      PlayerAbilities abilities = (PlayerAbilities)packetWrapper.user().get(PlayerAbilities.class);
                      abilities.setSprinting((action == 3));
                      PacketWrapper abilitiesPacket = new PacketWrapper(57, null, packetWrapper.user());
                      abilitiesPacket.write(Type.BYTE, Byte.valueOf(abilities.getFlags()));
                      abilitiesPacket.write((Type)Type.FLOAT, Float.valueOf(abilities.isSprinting() ? (abilities.getFlySpeed() * 2.0F) : abilities.getFlySpeed()));
                      abilitiesPacket.write((Type)Type.FLOAT, Float.valueOf(abilities.getWalkSpeed()));
                      PacketUtil.sendPacket(abilitiesPacket, Protocol1_7_6_10TO1_8.class);
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 12, 12, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    boolean jump = ((Boolean)packetWrapper.read(Type.BOOLEAN)).booleanValue();
                    boolean unmount = ((Boolean)packetWrapper.read(Type.BOOLEAN)).booleanValue();
                    short flags = 0;
                    if (jump)
                      flags = (short)(flags + 1); 
                    if (unmount)
                      flags = (short)(flags + 2); 
                    packetWrapper.write(Type.UNSIGNED_BYTE, Short.valueOf(flags));
                    if (unmount) {
                      EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                      if (tracker.getSpectating() != tracker.getPlayerId()) {
                        PacketWrapper sneakPacket = new PacketWrapper(11, null, packetWrapper.user());
                        sneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(tracker.getPlayerId()));
                        sneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
                        sneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
                        PacketWrapper unsneakPacket = new PacketWrapper(11, null, packetWrapper.user());
                        unsneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(tracker.getPlayerId()));
                        unsneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(1));
                        unsneakPacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
                        PacketUtil.sendToServer(sneakPacket, Protocol1_7_6_10TO1_8.class);
                      } 
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 18, 18, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int x = ((Integer)packetWrapper.read(Type.INT)).intValue();
                    short y = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    int z = ((Integer)packetWrapper.read(Type.INT)).intValue();
                    packetWrapper.write(Type.POSITION, new Position(x, y, z));
                    for (int i = 0; i < 4; i++) {
                      String line = (String)packetWrapper.read(Type.STRING);
                      line = ChatUtil.legacyToJson(line);
                      packetWrapper.write(Type.COMPONENT, GsonUtil.getJsonParser().parse(line));
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 19, 19, new PacketRemapper() {
          public void registerMap() {
            map(Type.BYTE);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    byte flags = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    PlayerAbilities abilities = (PlayerAbilities)packetWrapper.user().get(PlayerAbilities.class);
                    abilities.setAllowFly(((flags & 0x4) == 4));
                    abilities.setFlying(((flags & 0x2) == 2));
                    packetWrapper.set((Type)Type.FLOAT, 0, Float.valueOf(abilities.getFlySpeed()));
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 20, 20, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            create(new ValueCreator() {
                  public void write(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.write(Type.OPTIONAL_POSITION, null);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String msg = (String)packetWrapper.get(Type.STRING, 0);
                    if (msg.toLowerCase().startsWith("/stp ")) {
                      packetWrapper.cancel();
                      String[] args = msg.split(" ");
                      if (args.length <= 2) {
                        String prefix = (args.length == 1) ? "" : args[1];
                        GameProfileStorage storage = (GameProfileStorage)packetWrapper.user().get(GameProfileStorage.class);
                        List<GameProfileStorage.GameProfile> profiles = storage.getAllWithPrefix(prefix, true);
                        PacketWrapper tabComplete = new PacketWrapper(58, null, packetWrapper.user());
                        tabComplete.write((Type)Type.VAR_INT, Integer.valueOf(profiles.size()));
                        for (GameProfileStorage.GameProfile profile : profiles)
                          tabComplete.write(Type.STRING, profile.name); 
                        PacketUtil.sendPacket(tabComplete, Protocol1_7_6_10TO1_8.class);
                      } 
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 21, 21, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            map(Type.BYTE);
            map(Type.BYTE);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read(Type.BYTE);
                    boolean cape = ((Boolean)packetWrapper.read(Type.BOOLEAN)).booleanValue();
                    packetWrapper.write(Type.UNSIGNED_BYTE, Short.valueOf((short)(cape ? 127 : 126)));
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 23, 23, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String channel = (String)packetWrapper.get(Type.STRING, 0);
                    int length = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    if (channel.equalsIgnoreCase("MC|ItemName")) {
                      CustomByteType customByteType = new CustomByteType(Integer.valueOf(length));
                      byte[] data = (byte[])packetWrapper.read((Type)customByteType);
                      String name = new String(data, Charsets.UTF_8);
                      ByteBuf buf = packetWrapper.user().getChannel().alloc().buffer();
                      Type.STRING.write(buf, name);
                      data = new byte[buf.readableBytes()];
                      buf.readBytes(data);
                      buf.release();
                      packetWrapper.write(Type.REMAINING_BYTES, data);
                      Windows windows = (Windows)packetWrapper.user().get(Windows.class);
                      PacketWrapper updateCost = new PacketWrapper(49, null, packetWrapper.user());
                      updateCost.write(Type.UNSIGNED_BYTE, Short.valueOf(windows.anvilId));
                      updateCost.write((Type)Type.SHORT, Short.valueOf((short)0));
                      updateCost.write((Type)Type.SHORT, Short.valueOf(windows.levelCost));
                      PacketUtil.sendPacket(updateCost, Protocol1_7_6_10TO1_8.class, true, true);
                    } else if (channel.equalsIgnoreCase("MC|BEdit") || channel.equalsIgnoreCase("MC|BSign")) {
                      Item book = (Item)packetWrapper.read(Types1_7_6_10.COMPRESSED_NBT_ITEM);
                      CompoundTag tag = book.getTag();
                      if (tag != null && tag.contains("pages")) {
                        ListTag pages = (ListTag)tag.get("pages");
                        for (int i = 0; i < pages.size(); i++) {
                          StringTag page = (StringTag)pages.get(i);
                          String value = page.getValue();
                          value = ChatUtil.legacyToJson(value);
                          page.setValue(value);
                        } 
                      } 
                      packetWrapper.write(Type.ITEM, book);
                    } else if (channel.equalsIgnoreCase("MC|Brand")) {
                      packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(length));
                    } 
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\packets\PlayerPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */