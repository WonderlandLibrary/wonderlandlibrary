// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ClientboundPackets1_7;
import de.gerrygames.viarewind.types.VarLongType;
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
import de.gerrygames.viarewind.replacement.Replacement;
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
                final ClientWorld world;
                final Chunk chunk;
                final ChunkSection[] array;
                int length;
                int j = 0;
                ChunkSection section;
                int i;
                int block;
                int replacedBlock;
                this.handler(packetWrapper -> {
                    world = packetWrapper.user().get(ClientWorld.class);
                    chunk = packetWrapper.read((Type<Chunk>)new Chunk1_8Type(world));
                    packetWrapper.write((Type<Chunk>)new Chunk1_7_10Type(world), chunk);
                    chunk.getSections();
                    for (length = array.length; j < length; ++j) {
                        section = array[j];
                        if (section != null) {
                            for (i = 0; i < section.getPaletteSize(); ++i) {
                                block = section.getPaletteEntry(i);
                                replacedBlock = ReplacementRegistry1_7_6_10to1_8.replace(block);
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
                final BlockChangeRecord[] records;
                final BlockChangeRecord[] array;
                int length;
                int i = 0;
                BlockChangeRecord record;
                short data;
                int replacedBlock;
                this.handler(packetWrapper -> {
                    records = packetWrapper.read(Type.BLOCK_CHANGE_RECORD_ARRAY);
                    packetWrapper.write(Type.SHORT, (short)records.length);
                    packetWrapper.write(Type.INT, records.length * 4);
                    for (length = array.length; i < length; ++i) {
                        record = array[i];
                        data = (short)(record.getSectionX() << 12 | record.getSectionZ() << 8 | record.getY());
                        packetWrapper.write(Type.SHORT, data);
                        replacedBlock = ReplacementRegistry1_7_6_10to1_8.replace(record.getBlockId());
                        packetWrapper.write(Type.SHORT, (short)replacedBlock);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Position position;
                this.handler(packetWrapper -> {
                    position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.UNSIGNED_BYTE, (short)position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                    return;
                });
                final int data;
                int blockId;
                int meta;
                final Replacement replace;
                this.handler(packetWrapper -> {
                    data = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    blockId = data >> 4;
                    meta = (data & 0xF);
                    replace = ReplacementRegistry1_7_6_10to1_8.getReplacement(blockId, meta);
                    if (replace != null) {
                        blockId = replace.getId();
                        meta = replace.replaceData(meta);
                    }
                    packetWrapper.write(Type.VAR_INT, blockId);
                    packetWrapper.write(Type.UNSIGNED_BYTE, (short)meta);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.BLOCK_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Position position;
                this.handler(packetWrapper -> {
                    position = packetWrapper.read(Type.POSITION);
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
                final Position position;
                this.handler(packetWrapper -> {
                    position = packetWrapper.read(Type.POSITION);
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
                final Position position;
                this.handler(packetWrapper -> {
                    position = packetWrapper.read(Type.POSITION);
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
                final int particleId;
                Particle particle;
                this.handler(packetWrapper -> {
                    particleId = packetWrapper.read((Type<Integer>)Type.INT);
                    particle = Particle.find(particleId);
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
                String name;
                Particle particle2;
                int id;
                int data;
                this.handler(packetWrapper -> {
                    name = packetWrapper.get(Type.STRING, 0);
                    particle2 = Particle.find(name);
                    if (particle2 == Particle.ICON_CRACK || particle2 == Particle.BLOCK_CRACK || particle2 == Particle.BLOCK_DUST) {
                        id = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                        data = ((particle2 == Particle.ICON_CRACK) ? packetWrapper.read((Type<Integer>)Type.VAR_INT) : 0);
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
                final Position position;
                this.handler(packetWrapper -> {
                    position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.SHORT, (short)position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                    return;
                });
                int i;
                String line;
                String line2;
                String line3;
                this.handler(packetWrapper -> {
                    for (i = 0; i < 4; ++i) {
                        line = packetWrapper.read(Type.STRING);
                        line2 = ChatUtil.jsonToLegacy(line);
                        line3 = ChatUtil.removeUnusedColor(line2, '0');
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
                final int id;
                final byte scale;
                final int count;
                final byte[] icons;
                int i;
                int j;
                final short columns;
                short rows;
                short x;
                short z;
                byte[] data;
                int column;
                byte[] columnData;
                int k;
                PacketWrapper columnUpdate;
                byte[] iconData;
                int l;
                PacketWrapper iconUpdate;
                CustomByteType customByteType;
                final PacketWrapper scaleUpdate;
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    id = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    scale = packetWrapper.read((Type<Byte>)Type.BYTE);
                    count = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    icons = new byte[count * 4];
                    for (i = 0; i < count; ++i) {
                        j = packetWrapper.read((Type<Byte>)Type.BYTE);
                        icons[i * 4] = (byte)(j >> 4 & 0xF);
                        icons[i * 4 + 1] = packetWrapper.read((Type<Byte>)Type.BYTE);
                        icons[i * 4 + 2] = packetWrapper.read((Type<Byte>)Type.BYTE);
                        icons[i * 4 + 3] = (byte)(j & 0xF);
                    }
                    columns = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    if (columns > 0) {
                        rows = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                        x = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                        z = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                        data = packetWrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                        for (column = 0; column < columns; ++column) {
                            columnData = new byte[rows + 3];
                            columnData[0] = 0;
                            columnData[1] = (byte)(x + column);
                            columnData[2] = (byte)z;
                            for (k = 0; k < rows; ++k) {
                                columnData[k + 3] = data[column + k * columns];
                            }
                            columnUpdate = PacketWrapper.create(52, null, packetWrapper.user());
                            columnUpdate.write(Type.VAR_INT, id);
                            columnUpdate.write(Type.SHORT, (short)columnData.length);
                            columnUpdate.write((Type<byte[]>)new CustomByteType(columnData.length), columnData);
                            PacketUtil.sendPacket(columnUpdate, Protocol1_7_6_10TO1_8.class, true, true);
                        }
                    }
                    if (count > 0) {
                        iconData = new byte[count * 3 + 1];
                        iconData[0] = 1;
                        for (l = 0; l < count; ++l) {
                            iconData[l * 3 + 1] = (byte)(icons[l * 4] << 4 | (icons[l * 4 + 3] & 0xF));
                            iconData[l * 3 + 2] = icons[l * 4 + 1];
                            iconData[l * 3 + 3] = icons[l * 4 + 2];
                        }
                        iconUpdate = PacketWrapper.create(52, null, packetWrapper.user());
                        iconUpdate.write(Type.VAR_INT, id);
                        iconUpdate.write(Type.SHORT, (short)iconData.length);
                        customByteType = new CustomByteType(iconData.length);
                        iconUpdate.write((Type<byte[]>)customByteType, iconData);
                        PacketUtil.sendPacket(iconUpdate, Protocol1_7_6_10TO1_8.class, true, true);
                    }
                    scaleUpdate = PacketWrapper.create(52, null, packetWrapper.user());
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
                final Position position;
                this.handler(packetWrapper -> {
                    position = packetWrapper.read(Type.POSITION);
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
                final int action;
                final WorldBorder worldBorder;
                this.handler(packetWrapper -> {
                    action = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    worldBorder = packetWrapper.user().get(WorldBorder.class);
                    if (action == 0) {
                        worldBorder.setSize(packetWrapper.read((Type<Double>)Type.DOUBLE));
                    }
                    else if (action == 1) {
                        worldBorder.lerpSize(packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Long>)VarLongType.VAR_LONG));
                    }
                    else if (action == 2) {
                        worldBorder.setCenter(packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Double>)Type.DOUBLE));
                    }
                    else if (action == 3) {
                        worldBorder.init(packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Double>)Type.DOUBLE), packetWrapper.read((Type<Long>)VarLongType.VAR_LONG), packetWrapper.read((Type<Integer>)Type.VAR_INT), packetWrapper.read((Type<Integer>)Type.VAR_INT), packetWrapper.read((Type<Integer>)Type.VAR_INT));
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
