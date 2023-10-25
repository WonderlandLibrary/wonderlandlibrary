package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ItemRewriter;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.BlockPlaceDestroyTracker;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.BossBarStorage;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Cooldown;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.PlayerPosition;
import de.gerrygames.viarewind.utils.ChatUtil;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.UUID;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_8;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;

public class PlayerPackets {
  public static void register(Protocol protocol) {
    protocol.registerOutgoing(State.PLAY, 6, 11);
    protocol.registerOutgoing(State.PLAY, 7, 55);
    protocol.registerOutgoing(State.PLAY, 12, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                    UUID uuid = (UUID)packetWrapper.read(Type.UUID);
                    int action = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    BossBarStorage bossBarStorage = (BossBarStorage)packetWrapper.user().get(BossBarStorage.class);
                    if (action == 0) {
                      bossBarStorage.add(uuid, ChatUtil.jsonToLegacy((JsonElement)packetWrapper.read(Type.COMPONENT)), ((Float)packetWrapper.read((Type)Type.FLOAT)).floatValue());
                      packetWrapper.read((Type)Type.VAR_INT);
                      packetWrapper.read((Type)Type.VAR_INT);
                      packetWrapper.read(Type.UNSIGNED_BYTE);
                    } else if (action == 1) {
                      bossBarStorage.remove(uuid);
                    } else if (action == 2) {
                      bossBarStorage.updateHealth(uuid, ((Float)packetWrapper.read((Type)Type.FLOAT)).floatValue());
                    } else if (action == 3) {
                      String title = ChatUtil.jsonToLegacy((JsonElement)packetWrapper.read(Type.COMPONENT));
                      bossBarStorage.updateTitle(uuid, title);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 14, 58);
    protocol.registerOutgoing(State.PLAY, 15, 2);
    protocol.registerOutgoing(State.PLAY, 23, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 24, 63, new PacketRemapper() {
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
                        packetWrapper.write(Type.ITEM, ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM)));
                        packetWrapper.write(Type.ITEM, ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM)));
                        boolean has3Items = ((Boolean)packetWrapper.passthrough(Type.BOOLEAN)).booleanValue();
                        if (has3Items)
                          packetWrapper.write(Type.ITEM, ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM))); 
                        packetWrapper.passthrough(Type.BOOLEAN);
                        packetWrapper.passthrough(Type.INT);
                        packetWrapper.passthrough(Type.INT);
                      } 
                    } else if (channel.equalsIgnoreCase("MC|BOpen")) {
                      packetWrapper.read((Type)Type.VAR_INT);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 26, 64);
    protocol.registerOutgoing(State.PLAY, 30, 43, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.FLOAT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int reason = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    if (reason == 3)
                      ((EntityTracker)packetWrapper.user().get(EntityTracker.class)).setPlayerGamemode(((Float)packetWrapper.get((Type)Type.FLOAT, 0)).intValue()); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 35, 1, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.UNSIGNED_BYTE);
            map(Type.BYTE);
            map(Type.UNSIGNED_BYTE);
            map(Type.UNSIGNED_BYTE);
            map(Type.STRING);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    tracker.setPlayerId(((Integer)packetWrapper.get(Type.INT, 0)).intValue());
                    tracker.setPlayerGamemode(((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue());
                    tracker.getClientEntityTypes().put(Integer.valueOf(tracker.getPlayerId()), Entity1_10Types.EntityType.ENTITY_HUMAN);
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
    protocol.registerOutgoing(State.PLAY, 42, 54);
    protocol.registerOutgoing(State.PLAY, 43, 57);
    protocol.registerOutgoing(State.PLAY, 45, 56);
    protocol.registerOutgoing(State.PLAY, 46, 8, new PacketRemapper() {
          public void registerMap() {
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    PlayerPosition pos = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    int teleportId = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    pos.setConfirmId(teleportId);
                    byte flags = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    double x = ((Double)packetWrapper.get(Type.DOUBLE, 0)).doubleValue();
                    double y = ((Double)packetWrapper.get(Type.DOUBLE, 1)).doubleValue();
                    double z = ((Double)packetWrapper.get(Type.DOUBLE, 2)).doubleValue();
                    float yaw = ((Float)packetWrapper.get((Type)Type.FLOAT, 0)).floatValue();
                    float pitch = ((Float)packetWrapper.get((Type)Type.FLOAT, 1)).floatValue();
                    packetWrapper.set(Type.BYTE, 0, Byte.valueOf((byte)0));
                    if (flags != 0) {
                      if ((flags & 0x1) != 0) {
                        x += pos.getPosX();
                        packetWrapper.set(Type.DOUBLE, 0, Double.valueOf(x));
                      } 
                      if ((flags & 0x2) != 0) {
                        y += pos.getPosY();
                        packetWrapper.set(Type.DOUBLE, 1, Double.valueOf(y));
                      } 
                      if ((flags & 0x4) != 0) {
                        z += pos.getPosZ();
                        packetWrapper.set(Type.DOUBLE, 2, Double.valueOf(z));
                      } 
                      if ((flags & 0x8) != 0) {
                        yaw += pos.getYaw();
                        packetWrapper.set((Type)Type.FLOAT, 0, Float.valueOf(yaw));
                      } 
                      if ((flags & 0x10) != 0) {
                        pitch += pos.getPitch();
                        packetWrapper.set((Type)Type.FLOAT, 1, Float.valueOf(pitch));
                      } 
                    } 
                    pos.setPos(x, y, z);
                    pos.setYaw(yaw);
                    pos.setPitch(pitch);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 50, 72);
    protocol.registerOutgoing(State.PLAY, 51, 7, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.UNSIGNED_BYTE);
            map(Type.UNSIGNED_BYTE);
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    ((EntityTracker)packetWrapper.user().get(EntityTracker.class)).setPlayerGamemode(((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 1)).shortValue());
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    ((BossBarStorage)packetWrapper.user().get(BossBarStorage.class)).updateLocation();
                    ((BossBarStorage)packetWrapper.user().get(BossBarStorage.class)).changeWorld();
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
    protocol.registerOutgoing(State.PLAY, 54, 67);
    protocol.registerOutgoing(State.PLAY, 55, 9);
    protocol.registerOutgoing(State.PLAY, 61, 31);
    protocol.registerOutgoing(State.PLAY, 62, 6);
    protocol.registerOutgoing(State.PLAY, 67, 5);
    protocol.registerOutgoing(State.PLAY, 69, 69);
    protocol.registerOutgoing(State.PLAY, 72, 71);
    protocol.registerIncoming(State.PLAY, 2, 1, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String msg = (String)packetWrapper.get(Type.STRING, 0);
                    if (msg.toLowerCase().startsWith("/offhand")) {
                      packetWrapper.cancel();
                      PacketWrapper swapItems = new PacketWrapper(19, null, packetWrapper.user());
                      swapItems.write((Type)Type.VAR_INT, Integer.valueOf(6));
                      swapItems.write(Type.POSITION, new Position(0, (short)0, 0));
                      swapItems.write(Type.BYTE, Byte.valueOf((byte)-1));
                      PacketUtil.sendToServer(swapItems, Protocol1_8TO1_9.class, true, true);
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 5, 15);
    protocol.registerIncoming(State.PLAY, 10, 2, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int type = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 1)).intValue();
                    if (type == 2) {
                      packetWrapper.passthrough((Type)Type.FLOAT);
                      packetWrapper.passthrough((Type)Type.FLOAT);
                      packetWrapper.passthrough((Type)Type.FLOAT);
                    } 
                    if (type == 2 || type == 0)
                      packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(0)); 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 15, 3, new PacketRemapper() {
          public void registerMap() {
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    int playerId = tracker.getPlayerId();
                    if (tracker.isInsideVehicle(playerId))
                      packetWrapper.cancel(); 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 12, 4, new PacketRemapper() {
          public void registerMap() {
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    PlayerPosition pos = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    if (pos.getConfirmId() != -1)
                      return; 
                    pos.setPos(((Double)packetWrapper.get(Type.DOUBLE, 0)).doubleValue(), ((Double)packetWrapper.get(Type.DOUBLE, 1)).doubleValue(), ((Double)packetWrapper.get(Type.DOUBLE, 2)).doubleValue());
                    pos.setOnGround(((Boolean)packetWrapper.get(Type.BOOLEAN, 0)).booleanValue());
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    ((BossBarStorage)packetWrapper.user().get(BossBarStorage.class)).updateLocation();
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 14, 5, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    PlayerPosition pos = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    if (pos.getConfirmId() != -1)
                      return; 
                    pos.setYaw(((Float)packetWrapper.get((Type)Type.FLOAT, 0)).floatValue());
                    pos.setPitch(((Float)packetWrapper.get((Type)Type.FLOAT, 1)).floatValue());
                    pos.setOnGround(((Boolean)packetWrapper.get(Type.BOOLEAN, 0)).booleanValue());
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    ((BossBarStorage)packetWrapper.user().get(BossBarStorage.class)).updateLocation();
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 13, 6, new PacketRemapper() {
          public void registerMap() {
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map(Type.DOUBLE);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    double x = ((Double)packetWrapper.get(Type.DOUBLE, 0)).doubleValue();
                    double y = ((Double)packetWrapper.get(Type.DOUBLE, 1)).doubleValue();
                    double z = ((Double)packetWrapper.get(Type.DOUBLE, 2)).doubleValue();
                    float yaw = ((Float)packetWrapper.get((Type)Type.FLOAT, 0)).floatValue();
                    float pitch = ((Float)packetWrapper.get((Type)Type.FLOAT, 1)).floatValue();
                    boolean onGround = ((Boolean)packetWrapper.get(Type.BOOLEAN, 0)).booleanValue();
                    PlayerPosition pos = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                    if (pos.getConfirmId() != -1) {
                      if (pos.getPosX() == x && pos.getPosY() == y && pos.getPosZ() == z && pos.getYaw() == yaw && pos.getPitch() == pitch) {
                        PacketWrapper confirmTeleport = packetWrapper.create(0);
                        confirmTeleport.write((Type)Type.VAR_INT, Integer.valueOf(pos.getConfirmId()));
                        PacketUtil.sendToServer(confirmTeleport, Protocol1_8TO1_9.class, true, true);
                        pos.setConfirmId(-1);
                      } 
                    } else {
                      pos.setPos(x, y, z);
                      pos.setYaw(yaw);
                      pos.setPitch(pitch);
                      pos.setOnGround(onGround);
                    } 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    ((BossBarStorage)packetWrapper.user().get(BossBarStorage.class)).updateLocation();
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 19, 7, new PacketRemapper() {
          public void registerMap() {
            map(Type.BYTE, (Type)Type.VAR_INT);
            map(Type.POSITION);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int state = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    if (state == 0) {
                      ((BlockPlaceDestroyTracker)packetWrapper.user().get(BlockPlaceDestroyTracker.class)).setMining(true);
                    } else if (state == 2) {
                      BlockPlaceDestroyTracker tracker = (BlockPlaceDestroyTracker)packetWrapper.user().get(BlockPlaceDestroyTracker.class);
                      tracker.setMining(false);
                      tracker.setLastMining(System.currentTimeMillis() + 100L);
                      ((Cooldown)packetWrapper.user().get(Cooldown.class)).setLastHit(0L);
                    } else if (state == 1) {
                      BlockPlaceDestroyTracker tracker = (BlockPlaceDestroyTracker)packetWrapper.user().get(BlockPlaceDestroyTracker.class);
                      tracker.setMining(false);
                      tracker.setLastMining(0L);
                      ((Cooldown)packetWrapper.user().get(Cooldown.class)).hit();
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 28, 8, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION);
            map(Type.BYTE, (Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read(Type.ITEM);
                  }
                });
            create(new ValueCreator() {
                  public void write(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(0));
                  }
                });
            map(Type.BYTE, Type.UNSIGNED_BYTE);
            map(Type.BYTE, Type.UNSIGNED_BYTE);
            map(Type.BYTE, Type.UNSIGNED_BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    if (((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue() == -1) {
                      packetWrapper.cancel();
                      PacketWrapper useItem = new PacketWrapper(29, null, packetWrapper.user());
                      useItem.write((Type)Type.VAR_INT, Integer.valueOf(0));
                      PacketUtil.sendToServer(useItem, Protocol1_8TO1_9.class, true, true);
                    } 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    if (((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue() != -1)
                      ((BlockPlaceDestroyTracker)packetWrapper.user().get(BlockPlaceDestroyTracker.class)).place(); 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 23, 9, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    ((Cooldown)packetWrapper.user().get(Cooldown.class)).hit();
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 26, 10, new PacketRemapper() {
          public void registerMap() {
            create(new ValueCreator() {
                  public void write(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                    final PacketWrapper delayedPacket = new PacketWrapper(26, null, packetWrapper.user());
                    delayedPacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
                    Protocol1_8TO1_9.TIMER.schedule(new TimerTask() {
                          public void run() {
                            PacketUtil.sendToServer(delayedPacket, Protocol1_8TO1_9.class);
                          }
                        }5L);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    ((BlockPlaceDestroyTracker)packetWrapper.user().get(BlockPlaceDestroyTracker.class)).updateMining();
                    ((Cooldown)packetWrapper.user().get(Cooldown.class)).hit();
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 20, 11, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int action = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 1)).intValue();
                    if (action == 6) {
                      packetWrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(7));
                    } else if (action == 0) {
                      PlayerPosition pos = (PlayerPosition)packetWrapper.user().get(PlayerPosition.class);
                      if (!pos.isOnGround()) {
                        PacketWrapper elytra = new PacketWrapper(20, null, packetWrapper.user());
                        elytra.write((Type)Type.VAR_INT, packetWrapper.get((Type)Type.VAR_INT, 0));
                        elytra.write((Type)Type.VAR_INT, Integer.valueOf(8));
                        elytra.write((Type)Type.VAR_INT, Integer.valueOf(0));
                        PacketUtil.sendToServer(elytra, Protocol1_8TO1_9.class, true, false);
                      } 
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 21, 12, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map(Type.UNSIGNED_BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    int playerId = tracker.getPlayerId();
                    int vehicle = tracker.getVehicle(playerId);
                    if (vehicle != -1 && tracker.getClientEntityTypes().get(Integer.valueOf(vehicle)) == Entity1_10Types.EntityType.BOAT) {
                      PacketWrapper steerBoat = new PacketWrapper(17, null, packetWrapper.user());
                      float left = ((Float)packetWrapper.get((Type)Type.FLOAT, 0)).floatValue();
                      float forward = ((Float)packetWrapper.get((Type)Type.FLOAT, 1)).floatValue();
                      steerBoat.write(Type.BOOLEAN, Boolean.valueOf((forward != 0.0F || left < 0.0F)));
                      steerBoat.write(Type.BOOLEAN, Boolean.valueOf((forward != 0.0F || left > 0.0F)));
                      PacketUtil.sendToServer(steerBoat, Protocol1_8TO1_9.class);
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 25, 18, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    for (int i = 0; i < 4; i++)
                      packetWrapper.write(Type.STRING, ChatUtil.jsonToLegacy((JsonElement)packetWrapper.read(Type.COMPONENT))); 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 18, 19);
    protocol.registerIncoming(State.PLAY, 1, 20, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            create(new ValueCreator() {
                  public void write(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.write(Type.BOOLEAN, Boolean.valueOf(false));
                  }
                });
            map(Type.OPTIONAL_POSITION);
          }
        });
    protocol.registerIncoming(State.PLAY, 4, 21, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            map(Type.BYTE);
            map(Type.BYTE, (Type)Type.VAR_INT);
            map(Type.BOOLEAN);
            map(Type.UNSIGNED_BYTE);
            create(new ValueCreator() {
                  public void write(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(1));
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short flags = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    PacketWrapper updateSkin = new PacketWrapper(28, null, packetWrapper.user());
                    updateSkin.write((Type)Type.VAR_INT, Integer.valueOf(((EntityTracker)packetWrapper.user().get(EntityTracker.class)).getPlayerId()));
                    ArrayList<Metadata> metadata = new ArrayList<>();
                    metadata.add(new Metadata(10, (MetaType)MetaType1_8.Byte, Byte.valueOf((byte)flags)));
                    updateSkin.write(Types1_8.METADATA_LIST, metadata);
                    PacketUtil.sendPacket(updateSkin, Protocol1_8TO1_9.class);
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 3, 22);
    protocol.registerIncoming(State.PLAY, 9, 23, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String channel = (String)packetWrapper.get(Type.STRING, 0);
                    if (channel.equalsIgnoreCase("MC|BEdit") || channel.equalsIgnoreCase("MC|BSign")) {
                      Item book = (Item)packetWrapper.passthrough(Type.ITEM);
                      book.setIdentifier(386);
                      CompoundTag tag = book.getTag();
                      if (tag.contains("pages")) {
                        ListTag pages = (ListTag)tag.get("pages");
                        for (int i = 0; i < pages.size(); i++) {
                          StringTag page = (StringTag)pages.get(i);
                          String value = page.getValue();
                          value = ChatUtil.jsonToLegacy(value);
                          page.setValue(value);
                        } 
                      } 
                    } else if (channel.equalsIgnoreCase("MC|AdvCdm")) {
                      packetWrapper.set(Type.STRING, 0, channel = "MC|AdvCmd");
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 27, 24);
    protocol.registerIncoming(State.PLAY, 22, 25);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\packets\PlayerPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */