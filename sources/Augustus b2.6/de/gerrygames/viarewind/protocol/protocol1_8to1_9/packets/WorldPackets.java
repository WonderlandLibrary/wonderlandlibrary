// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import de.gerrygames.viarewind.ViaRewind;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.sound.Effect;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.List;
import java.util.ArrayList;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.BaseChunk;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.types.Chunk1_8Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.sound.SoundRemapper;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ReplacementRegistry1_8to1_9;
import com.viaversion.viaversion.api.type.types.VarIntType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.api.protocol.Protocol;

public class WorldPackets
{
    public static void register(final Protocol<ClientboundPackets1_9, ClientboundPackets1_8, ServerboundPackets1_9, ServerboundPackets1_8> protocol) {
        protocol.registerClientbound(ClientboundPackets1_9.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                final CompoundTag tag;
                String entity;
                this.handler(packetWrapper -> {
                    tag = packetWrapper.get(Type.NBT, 0);
                    if (tag != null && tag.contains("SpawnData")) {
                        entity = (String)tag.get("SpawnData").get("id").getValue();
                        tag.remove("SpawnData");
                        tag.put("entityId", new StringTag(entity));
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.BLOCK_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                final int block;
                VarIntType var_INT;
                int block2;
                final int n;
                final int i;
                this.handler(packetWrapper -> {
                    block = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (block >= 219 && block <= 234) {
                        var_INT = Type.VAR_INT;
                        block2 = 130;
                        packetWrapper.set(var_INT, n, i);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT);
                final int combined;
                final int replacedCombined;
                this.handler(packetWrapper -> {
                    combined = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    replacedCombined = ReplacementRegistry1_8to1_9.replace(combined);
                    packetWrapper.set(Type.VAR_INT, 0, replacedCombined);
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.MULTI_BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                final BlockChangeRecord[] array;
                int length;
                int i = 0;
                BlockChangeRecord record;
                int replacedCombined;
                this.handler(packetWrapper -> {
                    array = packetWrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0);
                    for (length = array.length; i < length; ++i) {
                        record = array[i];
                        replacedCombined = ReplacementRegistry1_8to1_9.replace(record.getBlockId());
                        record.setBlockId(replacedCombined);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.NAMED_SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                final String name;
                final String name2;
                this.handler(packetWrapper -> {
                    name = packetWrapper.get(Type.STRING, 0);
                    name2 = SoundRemapper.getOldName(name);
                    if (name2 == null) {
                        packetWrapper.cancel();
                    }
                    else {
                        packetWrapper.set(Type.STRING, 0, name2);
                    }
                    return;
                });
                this.map(Type.VAR_INT, Type.NOTHING);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.FLOAT);
                this.map(Type.UNSIGNED_BYTE);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.EXPLOSION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                final int count;
                int i;
                this.handler(packetWrapper -> {
                    count = packetWrapper.read((Type<Integer>)Type.INT);
                    packetWrapper.write(Type.INT, count);
                    for (i = 0; i < count; ++i) {
                        packetWrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                        packetWrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                        packetWrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                    }
                    return;
                });
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.UNLOAD_CHUNK, ClientboundPackets1_8.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int chunkX;
                final int chunkZ;
                final ClientWorld world;
                final Chunk1_8Type chunk1_8Type;
                final BaseChunk baseChunk;
                this.handler(packetWrapper -> {
                    chunkX = packetWrapper.read((Type<Integer>)Type.INT);
                    chunkZ = packetWrapper.read((Type<Integer>)Type.INT);
                    world = packetWrapper.user().get(ClientWorld.class);
                    chunk1_8Type = new Chunk1_8Type(world);
                    new BaseChunk(chunkX, chunkZ, true, false, 0, new ChunkSection[16], null, new ArrayList<CompoundTag>());
                    packetWrapper.write((Type<BaseChunk>)chunk1_8Type, baseChunk);
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: invokedynamic   BootstrapMethod #0, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
                //     6: invokevirtual   de/gerrygames/viarewind/protocol/protocol1_8to1_9/packets/WorldPackets$8.handler:(Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
                //     9: return         
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
                //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
                //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
                //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION);
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                final int id;
                final int id2;
                int replacedBlock;
                this.handler(packetWrapper -> {
                    id = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    id2 = Effect.getOldId(id);
                    if (id2 == -1) {
                        packetWrapper.cancel();
                    }
                    else {
                        packetWrapper.set(Type.INT, 0, id2);
                        if (id2 == 2001) {
                            replacedBlock = ReplacementRegistry1_8to1_9.replace(packetWrapper.get((Type<Integer>)Type.INT, 1));
                            packetWrapper.set(Type.INT, 1, replacedBlock);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.SPAWN_PARTICLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                final int type;
                this.handler(packetWrapper -> {
                    type = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    if (type > 41 && !ViaRewind.getConfig().isReplaceParticles()) {
                        packetWrapper.cancel();
                    }
                    else if (type == 42) {
                        packetWrapper.set(Type.INT, 0, 24);
                    }
                    else if (type == 43) {
                        packetWrapper.set(Type.INT, 0, 3);
                    }
                    else if (type == 44) {
                        packetWrapper.set(Type.INT, 0, 34);
                    }
                    else if (type == 45) {
                        packetWrapper.set(Type.INT, 0, 1);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.MAP_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN, Type.NOTHING);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.SOUND, ClientboundPackets1_8.NAMED_SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int soundId;
                final String sound;
                this.handler(packetWrapper -> {
                    soundId = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    sound = SoundRemapper.oldNameFromId(soundId);
                    if (sound == null) {
                        packetWrapper.cancel();
                    }
                    else {
                        packetWrapper.write(Type.STRING, sound);
                    }
                    return;
                });
                this.handler(packetWrapper -> packetWrapper.read((Type<Object>)Type.VAR_INT));
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.FLOAT);
                this.map(Type.UNSIGNED_BYTE);
            }
        });
    }
}
