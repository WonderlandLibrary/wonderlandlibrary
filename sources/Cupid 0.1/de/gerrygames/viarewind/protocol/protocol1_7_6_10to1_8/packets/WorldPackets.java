package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.chunks.ChunkPacketTransformer;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ReplacementRegistry1_7_6_10to1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.WorldBorder;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Chunk1_7_10Type;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Particle;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.types.Chunk1_8Type;
import de.gerrygames.viarewind.storage.BlockState;
import de.gerrygames.viarewind.types.VarLongType;
import de.gerrygames.viarewind.utils.ChatUtil;
import de.gerrygames.viarewind.utils.PacketUtil;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.CustomByteType;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;

public class WorldPackets {
  public static void register(Protocol protocol) {
    protocol.registerOutgoing(State.PLAY, 33, 33, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    ClientWorld world = (ClientWorld)packetWrapper.user().get(ClientWorld.class);
                    Chunk chunk = (Chunk)packetWrapper.read((Type)new Chunk1_8Type(world));
                    packetWrapper.write((Type)new Chunk1_7_10Type(world), chunk);
                    for (ChunkSection section : chunk.getSections()) {
                      if (section != null)
                        for (int i = 0; i < section.getPaletteSize(); i++) {
                          int block = section.getPaletteEntry(i);
                          BlockState state = BlockState.rawToState(block);
                          state = ReplacementRegistry1_7_6_10to1_8.replace(state);
                          section.setPaletteEntry(i, BlockState.stateToRaw(state));
                        }  
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 34, 34, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    BlockChangeRecord[] records = (BlockChangeRecord[])packetWrapper.read(Type.BLOCK_CHANGE_RECORD_ARRAY);
                    packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)records.length));
                    packetWrapper.write(Type.INT, Integer.valueOf(records.length * 4));
                    for (BlockChangeRecord record : records) {
                      short data = (short)(record.getSectionX() << 12 | record.getSectionZ() << 8 | record.getY());
                      packetWrapper.write((Type)Type.SHORT, Short.valueOf(data));
                      BlockState state = BlockState.rawToState(record.getBlockId());
                      state = ReplacementRegistry1_7_6_10to1_8.replace(state);
                      packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)BlockState.stateToRaw(state)));
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 35, 35, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Position position = (Position)packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getX()));
                    packetWrapper.write(Type.UNSIGNED_BYTE, Short.valueOf(position.getY()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getZ()));
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int data = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    int blockId = data >> 4;
                    int meta = data & 0xF;
                    BlockState state = ReplacementRegistry1_7_6_10to1_8.replace(new BlockState(blockId, meta));
                    blockId = state.getId();
                    meta = state.getData();
                    packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(blockId));
                    packetWrapper.write(Type.UNSIGNED_BYTE, Short.valueOf((short)meta));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 36, 36, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Position position = (Position)packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getX()));
                    packetWrapper.write((Type)Type.SHORT, Short.valueOf(position.getY()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getZ()));
                  }
                });
            map(Type.UNSIGNED_BYTE);
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.VAR_INT);
          }
        });
    protocol.registerOutgoing(State.PLAY, 37, 37, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Position position = (Position)packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getX()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getY()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getZ()));
                  }
                });
            map(Type.BYTE);
          }
        });
    protocol.registerOutgoing(State.PLAY, 38, 38, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    ChunkPacketTransformer.transformChunkBulk(packetWrapper);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 40, 40, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Position position = (Position)packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getX()));
                    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)position.getY()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getZ()));
                  }
                });
            map(Type.INT);
            map(Type.BOOLEAN);
          }
        });
    protocol.registerOutgoing(State.PLAY, 42, 42, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int particleId = ((Integer)packetWrapper.read(Type.INT)).intValue();
                    Particle particle = Particle.find(particleId);
                    if (particle == null)
                      particle = Particle.CRIT; 
                    packetWrapper.write(Type.STRING, particle.name);
                    packetWrapper.read(Type.BOOLEAN);
                  }
                });
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String name = (String)packetWrapper.get(Type.STRING, 0);
                    Particle particle = Particle.find(name);
                    if (particle == Particle.ICON_CRACK || particle == Particle.BLOCK_CRACK || particle == Particle.BLOCK_DUST) {
                      int id = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                      int data = (particle == Particle.ICON_CRACK) ? ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue() : 0;
                      if ((id >= 256 && id <= 422) || (id >= 2256 && id <= 2267)) {
                        particle = Particle.ICON_CRACK;
                      } else if ((id >= 0 && id <= 164) || (id >= 170 && id <= 175)) {
                        if (particle == Particle.ICON_CRACK)
                          particle = Particle.BLOCK_CRACK; 
                      } else {
                        packetWrapper.cancel();
                        return;
                      } 
                      name = particle.name + "_" + id + "_" + data;
                    } 
                    packetWrapper.set(Type.STRING, 0, name);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 51, 51, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Position position = (Position)packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getX()));
                    packetWrapper.write((Type)Type.SHORT, Short.valueOf(position.getY()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getZ()));
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    for (int i = 0; i < 4; i++) {
                      String line = (String)packetWrapper.read(Type.STRING);
                      line = ChatUtil.jsonToLegacy(line);
                      line = ChatUtil.removeUnusedColor(line, '0');
                      if (line.length() > 15) {
                        line = ChatColor.stripColor(line);
                        if (line.length() > 15)
                          line = line.substring(0, 15); 
                      } 
                      packetWrapper.write(Type.STRING, line);
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 52, 52, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                    int id = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    byte scale = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                    int count = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    byte[] icons = new byte[count * 4];
                    for (int i = 0; i < count; i++) {
                      int j = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                      icons[i * 4] = (byte)(j >> 4 & 0xF);
                      icons[i * 4 + 1] = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                      icons[i * 4 + 2] = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                      icons[i * 4 + 3] = (byte)(j & 0xF);
                    } 
                    short columns = ((Short)packetWrapper.read(Type.UNSIGNED_BYTE)).shortValue();
                    if (columns > 0) {
                      short rows = ((Short)packetWrapper.read(Type.UNSIGNED_BYTE)).shortValue();
                      byte x = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                      byte z = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                      byte[] data = (byte[])packetWrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                      for (int column = 0; column < columns; column++) {
                        byte[] columnData = new byte[rows + 3];
                        columnData[0] = 0;
                        columnData[1] = (byte)(x + column);
                        columnData[2] = z;
                        for (int j = 0; j < rows; j++)
                          columnData[j + 3] = data[column + j * columns]; 
                        PacketWrapper columnUpdate = new PacketWrapper(52, null, packetWrapper.user());
                        columnUpdate.write((Type)Type.VAR_INT, Integer.valueOf(id));
                        columnUpdate.write((Type)Type.SHORT, Short.valueOf((short)columnData.length));
                        columnUpdate.write((Type)new CustomByteType(Integer.valueOf(columnData.length)), columnData);
                        PacketUtil.sendPacket(columnUpdate, Protocol1_7_6_10TO1_8.class, true, true);
                      } 
                    } 
                    if (count > 0) {
                      byte[] iconData = new byte[count * 3 + 1];
                      iconData[0] = 1;
                      for (int j = 0; j < count; j++) {
                        iconData[j * 3 + 1] = (byte)(icons[j * 4] << 4 | icons[j * 4 + 3] & 0xF);
                        iconData[j * 3 + 2] = icons[j * 4 + 1];
                        iconData[j * 3 + 3] = icons[j * 4 + 2];
                      } 
                      PacketWrapper iconUpdate = new PacketWrapper(52, null, packetWrapper.user());
                      iconUpdate.write((Type)Type.VAR_INT, Integer.valueOf(id));
                      iconUpdate.write((Type)Type.SHORT, Short.valueOf((short)iconData.length));
                      CustomByteType customByteType = new CustomByteType(Integer.valueOf(iconData.length));
                      iconUpdate.write((Type)customByteType, iconData);
                      PacketUtil.sendPacket(iconUpdate, Protocol1_7_6_10TO1_8.class, true, true);
                    } 
                    PacketWrapper scaleUpdate = new PacketWrapper(52, null, packetWrapper.user());
                    scaleUpdate.write((Type)Type.VAR_INT, Integer.valueOf(id));
                    scaleUpdate.write((Type)Type.SHORT, Short.valueOf((short)2));
                    scaleUpdate.write((Type)new CustomByteType(Integer.valueOf(2)), new byte[] { 2, scale });
                    PacketUtil.sendPacket(scaleUpdate, Protocol1_7_6_10TO1_8.class, true, true);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 53, 53, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Position position = (Position)packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getX()));
                    packetWrapper.write((Type)Type.SHORT, Short.valueOf(position.getY()));
                    packetWrapper.write(Type.INT, Integer.valueOf(position.getZ()));
                  }
                });
            map(Type.UNSIGNED_BYTE);
            map(Type.NBT, Types1_7_6_10.COMPRESSED_NBT);
          }
        });
    protocol.registerOutgoing(State.PLAY, 65, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 66, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 68, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int action = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    WorldBorder worldBorder = (WorldBorder)packetWrapper.user().get(WorldBorder.class);
                    if (action == 0) {
                      worldBorder.setSize(((Double)packetWrapper.read(Type.DOUBLE)).doubleValue());
                    } else if (action == 1) {
                      worldBorder.lerpSize(((Double)packetWrapper.read(Type.DOUBLE)).doubleValue(), ((Double)packetWrapper.read(Type.DOUBLE)).doubleValue(), ((Long)packetWrapper.read((Type)VarLongType.VAR_LONG)).longValue());
                    } else if (action == 2) {
                      worldBorder.setCenter(((Double)packetWrapper.read(Type.DOUBLE)).doubleValue(), ((Double)packetWrapper.read(Type.DOUBLE)).doubleValue());
                    } else if (action == 3) {
                      worldBorder.init(((Double)packetWrapper
                          .read(Type.DOUBLE)).doubleValue(), ((Double)packetWrapper.read(Type.DOUBLE)).doubleValue(), ((Double)packetWrapper
                          .read(Type.DOUBLE)).doubleValue(), ((Double)packetWrapper.read(Type.DOUBLE)).doubleValue(), ((Long)packetWrapper
                          .read((Type)VarLongType.VAR_LONG)).longValue(), ((Integer)packetWrapper
                          .read((Type)Type.VAR_INT)).intValue(), ((Integer)packetWrapper
                          .read((Type)Type.VAR_INT)).intValue(), ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue());
                    } else if (action == 4) {
                      worldBorder.setWarningTime(((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue());
                    } else if (action == 5) {
                      worldBorder.setWarningBlocks(((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue());
                    } 
                    packetWrapper.cancel();
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\packets\WorldPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */