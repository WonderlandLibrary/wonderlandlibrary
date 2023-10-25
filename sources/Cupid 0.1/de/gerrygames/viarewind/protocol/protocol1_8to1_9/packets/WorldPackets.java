package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import de.gerrygames.viarewind.ViaRewind;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ReplacementRegistry1_8to1_9;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.sound.Effect;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.sound.SoundRemapper;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.types.Chunk1_8Type;
import de.gerrygames.viarewind.storage.BlockState;
import de.gerrygames.viarewind.utils.PacketUtil;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import us.myles.ViaVersion.api.minecraft.Environment;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk1_8;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.types.Chunk1_9_1_2Type;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class WorldPackets {
  public static void register(Protocol protocol) {
    protocol.registerOutgoing(State.PLAY, 8, 37);
    protocol.registerOutgoing(State.PLAY, 9, 53, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION);
            map(Type.UNSIGNED_BYTE);
            map(Type.NBT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    CompoundTag tag = (CompoundTag)packetWrapper.get(Type.NBT, 0);
                    if (tag != null && tag.contains("SpawnData")) {
                      String entity = (String)((CompoundTag)tag.get("SpawnData")).get("id").getValue();
                      tag.remove("SpawnData");
                      tag.put((Tag)new StringTag("entityId", entity));
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 10, 36, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION);
            map(Type.UNSIGNED_BYTE);
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int block = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    if (block >= 219 && block <= 234)
                      packetWrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(block = 130)); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 11, 35, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION);
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int combined = ((Integer)packetWrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    BlockState state = BlockState.rawToState(combined);
                    state = ReplacementRegistry1_8to1_9.replace(state);
                    packetWrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(BlockState.stateToRaw(state)));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 13, 65);
    protocol.registerOutgoing(State.PLAY, 16, 34, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.INT);
            map(Type.BLOCK_CHANGE_RECORD_ARRAY);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    for (BlockChangeRecord record : (BlockChangeRecord[])packetWrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
                      BlockState state = BlockState.rawToState(record.getBlockId());
                      state = ReplacementRegistry1_8to1_9.replace(state);
                      record.setBlockId(BlockState.stateToRaw(state));
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 25, 41, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String name = (String)packetWrapper.get(Type.STRING, 0);
                    name = SoundRemapper.getOldName(name);
                    if (name == null) {
                      packetWrapper.cancel();
                    } else {
                      packetWrapper.set(Type.STRING, 0, name);
                    } 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read((Type)Type.VAR_INT);
                  }
                });
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map((Type)Type.FLOAT);
            map(Type.UNSIGNED_BYTE);
          }
        });
    protocol.registerOutgoing(State.PLAY, 28, 39, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int count = ((Integer)packetWrapper.read(Type.INT)).intValue();
                    packetWrapper.write(Type.INT, Integer.valueOf(count));
                    for (int i = 0; i < count; i++) {
                      packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                      packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                      packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                    } 
                  }
                });
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
          }
        });
    protocol.registerOutgoing(State.PLAY, 29, 33, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int chunkX = ((Integer)packetWrapper.read(Type.INT)).intValue();
                    int chunkZ = ((Integer)packetWrapper.read(Type.INT)).intValue();
                    ClientWorld world = (ClientWorld)packetWrapper.user().get(ClientWorld.class);
                    packetWrapper.write((Type)new Chunk1_8Type(world), new Chunk1_8(chunkX, chunkZ));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 32, 33, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Chunk1_8 chunk1_8;
                    ClientWorld world = (ClientWorld)packetWrapper.user().get(ClientWorld.class);
                    Chunk chunk = (Chunk)packetWrapper.read((Type)new Chunk1_9_1_2Type(world));
                    for (ChunkSection section : chunk.getSections()) {
                      if (section != null)
                        for (int i = 0; i < section.getPaletteSize(); i++) {
                          int block = section.getPaletteEntry(i);
                          BlockState state = BlockState.rawToState(block);
                          state = ReplacementRegistry1_8to1_9.replace(state);
                          section.setPaletteEntry(i, BlockState.stateToRaw(state));
                        }  
                    } 
                    if (chunk.isFullChunk() && chunk.getBitmask() == 0) {
                      boolean skylight = (world.getEnvironment() == Environment.NORMAL);
                      ChunkSection[] sections = new ChunkSection[16];
                      ChunkSection section = new ChunkSection();
                      sections[0] = section;
                      section.addPaletteEntry(0);
                      if (skylight)
                        section.setSkyLight(new byte[2048]); 
                      chunk1_8 = new Chunk1_8(chunk.getX(), chunk.getZ(), true, 1, sections, chunk.getBiomeData(), chunk.getBlockEntities());
                    } 
                    packetWrapper.write((Type)new Chunk1_8Type(world), chunk1_8);
                    UserConnection user = packetWrapper.user();
                    chunk1_8.getBlockEntities().forEach(nbt -> {
                          short action;
                          if (!nbt.contains("x") || !nbt.contains("y") || !nbt.contains("z") || !nbt.contains("id"))
                            return; 
                          Position position = new Position(((Integer)nbt.get("x").getValue()).intValue(), (short)((Integer)nbt.get("y").getValue()).intValue(), ((Integer)nbt.get("z").getValue()).intValue());
                          String id = (String)nbt.get("id").getValue();
                          switch (id) {
                            case "minecraft:mob_spawner":
                              action = 1;
                              break;
                            case "minecraft:command_block":
                              action = 2;
                              break;
                            case "minecraft:beacon":
                              action = 3;
                              break;
                            case "minecraft:skull":
                              action = 4;
                              break;
                            case "minecraft:flower_pot":
                              action = 5;
                              break;
                            case "minecraft:banner":
                              action = 6;
                              break;
                            default:
                              return;
                          } 
                          PacketWrapper updateTileEntity = new PacketWrapper(9, null, user);
                          updateTileEntity.write(Type.POSITION, position);
                          updateTileEntity.write(Type.UNSIGNED_BYTE, Short.valueOf(action));
                          updateTileEntity.write(Type.NBT, nbt);
                          PacketUtil.sendPacket(updateTileEntity, Protocol1_8TO1_9.class, false, false);
                        });
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 33, 40, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.POSITION);
            map(Type.INT);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int id = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    id = Effect.getOldId(id);
                    if (id == -1) {
                      packetWrapper.cancel();
                      return;
                    } 
                    packetWrapper.set(Type.INT, 0, Integer.valueOf(id));
                    if (id == 2001) {
                      BlockState state = BlockState.rawToState(((Integer)packetWrapper.get(Type.INT, 1)).intValue());
                      state = ReplacementRegistry1_8to1_9.replace(state);
                      packetWrapper.set(Type.INT, 1, Integer.valueOf(BlockState.stateToRaw(state)));
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 34, 42, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int type = ((Integer)packetWrapper.get(Type.INT, 0)).intValue();
                    if (type > 41 && !ViaRewind.getConfig().isReplaceParticles()) {
                      packetWrapper.cancel();
                      return;
                    } 
                    if (type == 42) {
                      packetWrapper.set(Type.INT, 0, Integer.valueOf(24));
                    } else if (type == 43) {
                      packetWrapper.set(Type.INT, 0, Integer.valueOf(3));
                    } else if (type == 44) {
                      packetWrapper.set(Type.INT, 0, Integer.valueOf(34));
                    } else if (type == 45) {
                      packetWrapper.set(Type.INT, 0, Integer.valueOf(1));
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 36, 52, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read(Type.BOOLEAN);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 44, 66);
    protocol.registerOutgoing(State.PLAY, 53, 68);
    protocol.registerOutgoing(State.PLAY, 68, 3);
    protocol.registerOutgoing(State.PLAY, 70, 51);
    protocol.registerOutgoing(State.PLAY, 71, 41, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int soundId = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    String sound = SoundRemapper.oldNameFromId(soundId);
                    if (sound == null) {
                      packetWrapper.cancel();
                    } else {
                      packetWrapper.write(Type.STRING, sound);
                    } 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.read((Type)Type.VAR_INT);
                  }
                });
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map((Type)Type.FLOAT);
            map(Type.UNSIGNED_BYTE);
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\packets\WorldPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */