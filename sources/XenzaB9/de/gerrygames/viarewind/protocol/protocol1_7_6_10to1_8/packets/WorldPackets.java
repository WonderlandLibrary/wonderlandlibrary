// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ClientboundPackets1_7;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.WorldBorder;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import com.viaversion.viaversion.api.type.types.CustomByteType;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.util.ChatColorUtil;
import de.gerrygames.viarewind.utils.ChatUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Particle;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.chunks.ChunkPacketTransformer;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ReplacementRegistry1_7_6_10to1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Chunk1_7_10Type;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.types.Chunk1_8Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;

public class WorldPackets
{
    public static void register(final Protocol1_7_6_10TO1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final ClientWorld world = packetWrapper.user().get(ClientWorld.class);
                    final Chunk chunk = packetWrapper.read((Type<Chunk>)new Chunk1_8Type(world));
                    packetWrapper.write((Type<Chunk>)new Chunk1_7_10Type(world), chunk);
                    chunk.getSections();
                    final ChunkSection[] array;
                    int j = 0;
                    for (int length = array.length; j < length; ++j) {
                        final ChunkSection section = array[j];
                        if (section != null) {
                            for (int i = 0; i < section.getPaletteSize(); ++i) {
                                final int block = section.getPaletteEntry(i);
                                final int replacedBlock = ReplacementRegistry1_7_6_10to1_8.replace(block);
                                section.setPaletteEntry(i, replacedBlock);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.MULTI_BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.handler(packetWrapper -> {
                    final BlockChangeRecord[] records = packetWrapper.read(Type.BLOCK_CHANGE_RECORD_ARRAY);
                    packetWrapper.write(Type.SHORT, (short)records.length);
                    packetWrapper.write(Type.INT, records.length * 4);
                    final BlockChangeRecord[] array;
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final BlockChangeRecord record = array[i];
                        final short data = (short)(record.getSectionX() << 12 | record.getSectionZ() << 8 | record.getY());
                        packetWrapper.write(Type.SHORT, data);
                        final int replacedBlock = ReplacementRegistry1_7_6_10to1_8.replace(record.getBlockId());
                        packetWrapper.write(Type.SHORT, (short)replacedBlock);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final Position position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.UNSIGNED_BYTE, (short)position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                    return;
                });
                this.handler(packetWrapper -> {
                    final int data = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    final int data2 = ReplacementRegistry1_7_6_10to1_8.replace(data);
                    packetWrapper.write(Type.VAR_INT, data2 >> 4);
                    packetWrapper.write(Type.UNSIGNED_BYTE, (short)(data2 & 0xF));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.BLOCK_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final Position position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.SHORT, (short)position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                    return;
                });
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.BLOCK_BREAK_ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(packetWrapper -> {
                    final Position position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.INT, position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                    return;
                });
                this.map(Type.BYTE);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.MAP_BULK_CHUNK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(ChunkPacketTransformer::transformChunkBulk);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(packetWrapper -> {
                    final Position position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.BYTE, (byte)position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                    return;
                });
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_PARTICLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final int particleId = packetWrapper.read((Type<Integer>)Type.INT);
                    Particle particle = Particle.find(particleId);
                    if (particle == null) {
                        particle = Particle.CRIT;
                    }
                    packetWrapper.write(Type.STRING, particle.name);
                    packetWrapper.read((Type<Object>)Type.BOOLEAN);
                    return;
                });
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(packetWrapper -> {
                    String name = packetWrapper.get(Type.STRING, 0);
                    Particle particle2 = Particle.find(name);
                    if (particle2 == Particle.ICON_CRACK || particle2 == Particle.BLOCK_CRACK || particle2 == Particle.BLOCK_DUST) {
                        final int id = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                        final int data = (particle2 == Particle.ICON_CRACK) ? packetWrapper.read((Type<Integer>)Type.VAR_INT) : 0;
                        if ((id >= 256 && id <= 422) || (id >= 2256 && id <= 2267)) {
                            particle2 = Particle.ICON_CRACK;
                        }
                        else if ((id >= 0 && id <= 164) || (id >= 170 && id <= 175)) {
                            if (particle2 == Particle.ICON_CRACK) {
                                particle2 = Particle.BLOCK_CRACK;
                            }
                        }
                        else {
                            packetWrapper.cancel();
                            return;
                        }
                        name = particle2.name + "_" + id + "_" + data;
                    }
                    packetWrapper.set(Type.STRING, 0, name);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final Position position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.SHORT, (short)position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                    return;
                });
                this.handler(packetWrapper -> {
                    for (int i = 0; i < 4; ++i) {
                        final String line = packetWrapper.read(Type.STRING);
                        final String line2 = ChatUtil.jsonToLegacy(line);
                        String line3 = ChatUtil.removeUnusedColor(line2, '0');
                        if (line3.length() > 15) {
                            line3 = ChatColorUtil.stripColor(line3);
                            if (line3.length() > 15) {
                                line3 = line3.substring(0, 15);
                            }
                        }
                        packetWrapper.write(Type.STRING, line3);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.MAP_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    final int id = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    final byte scale = packetWrapper.read((Type<Byte>)Type.BYTE);
                    final int count = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    final byte[] icons = new byte[count * 4];
                    for (int i = 0; i < count; ++i) {
                        final int j = packetWrapper.read((Type<Byte>)Type.BYTE);
                        icons[i * 4] = (byte)(j >> 4 & 0xF);
                        icons[i * 4 + 1] = packetWrapper.read((Type<Byte>)Type.BYTE);
                        icons[i * 4 + 2] = packetWrapper.read((Type<Byte>)Type.BYTE);
                        icons[i * 4 + 3] = (byte)(j & 0xF);
                    }
                    final short columns = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    if (columns > 0) {
                        final short rows = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                        final short x = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                        final short z = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                        final byte[] data = packetWrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                        for (int column = 0; column < columns; ++column) {
                            final byte[] columnData = new byte[rows + 3];
                            columnData[0] = 0;
                            columnData[1] = (byte)(x + column);
                            columnData[2] = (byte)z;
                            for (int k = 0; k < rows; ++k) {
                                columnData[k + 3] = data[column + k * columns];
                            }
                            final PacketWrapper columnUpdate = PacketWrapper.create(52, null, packetWrapper.user());
                            columnUpdate.write(Type.VAR_INT, id);
                            columnUpdate.write(Type.SHORT, (short)columnData.length);
                            columnUpdate.write((Type<byte[]>)new CustomByteType(columnData.length), columnData);
                            PacketUtil.sendPacket(columnUpdate, Protocol1_7_6_10TO1_8.class, true, true);
                        }
                    }
                    if (count > 0) {
                        final byte[] iconData = new byte[count * 3 + 1];
                        iconData[0] = 1;
                        for (int l = 0; l < count; ++l) {
                            iconData[l * 3 + 1] = (byte)(icons[l * 4] << 4 | (icons[l * 4 + 3] & 0xF));
                            iconData[l * 3 + 2] = icons[l * 4 + 1];
                            iconData[l * 3 + 3] = icons[l * 4 + 2];
                        }
                        final PacketWrapper iconUpdate = PacketWrapper.create(52, null, packetWrapper.user());
                        iconUpdate.write(Type.VAR_INT, id);
                        iconUpdate.write(Type.SHORT, (short)iconData.length);
                        final CustomByteType customByteType = new CustomByteType(iconData.length);
                        iconUpdate.write((Type<byte[]>)customByteType, iconData);
                        PacketUtil.sendPacket(iconUpdate, Protocol1_7_6_10TO1_8.class, true, true);
                    }
                    final PacketWrapper scaleUpdate = PacketWrapper.create(52, null, packetWrapper.user());
                    scaleUpdate.write(Type.VAR_INT, id);
                    scaleUpdate.write(Type.SHORT, (Short)2);
                    scaleUpdate.write((Type<byte[]>)new CustomByteType(2), new byte[] { 2, scale });
                    PacketUtil.sendPacket(scaleUpdate, Protocol1_7_6_10TO1_8.class, true, true);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final Position position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.SHORT, (short)position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                    return;
                });
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT, Types1_7_6_10.COMPRESSED_NBT);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).cancelClientbound(ClientboundPackets1_8.SERVER_DIFFICULTY);
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).cancelClientbound(ClientboundPackets1_8.COMBAT_EVENT);
        ((Protocol<ClientboundPackets1_8, ClientboundPackets1_7, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.WORLD_BORDER, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final int action = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    final WorldBorder worldBorder = packetWrapper.user().get(WorldBorder.class);
                    if (action == 0) {
                        worldBorder.setSize(packetWrapper.read((Type<Double>)Type.DOUBLE));
                    }
                    else if (action == 1) {
                        worldBorder.lerpSize(packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Long>)Type.VAR_LONG));
                    }
                    else if (action == 2) {
                        worldBorder.setCenter(packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Double>)Type.DOUBLE));
                    }
                    else if (action == 3) {
                        worldBorder.init(packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Long>)Type.VAR_LONG), packetWrapper.read((Type<Integer>)Type.VAR_INT), packetWrapper.read((Type<Integer>)Type.VAR_INT), packetWrapper.read((Type<Integer>)Type.VAR_INT));
                    }
                    else if (action == 4) {
                        worldBorder.setWarningTime(packetWrapper.read((Type<Integer>)Type.VAR_INT));
                    }
                    else if (action == 5) {
                        worldBorder.setWarningBlocks(packetWrapper.read((Type<Integer>)Type.VAR_INT));
                    }
                    packetWrapper.cancel();
                });
            }
        });
    }
}
