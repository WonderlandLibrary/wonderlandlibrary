// 
// Decompiled by Procyon v0.6.0
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
                this.handler(packetWrapper -> {
                    final CompoundTag tag = packetWrapper.get(Type.NBT, 0);
                    if (tag != null && tag.contains("SpawnData")) {
                        final String entity = (String)tag.get("SpawnData").get("id").getValue();
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
                this.handler(packetWrapper -> {
                    final int block = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (block >= 219 && block <= 234) {
                        final VarIntType var_INT = Type.VAR_INT;
                        final int block2 = 130;
                        final Object o;
                        final int n;
                        packetWrapper.set(var_INT, (int)o, n);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT);
                this.handler(packetWrapper -> {
                    final int combined = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final int replacedCombined = ReplacementRegistry1_8to1_9.replace(combined);
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
                this.handler(packetWrapper -> {
                    final BlockChangeRecord[] array = packetWrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final BlockChangeRecord record = array[i];
                        final int replacedCombined = ReplacementRegistry1_8to1_9.replace(record.getBlockId());
                        record.setBlockId(replacedCombined);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.NAMED_SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(packetWrapper -> {
                    final String name = packetWrapper.get(Type.STRING, 0);
                    final String name2 = SoundRemapper.getOldName(name);
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
                this.handler(packetWrapper -> {
                    final int count = packetWrapper.read((Type<Integer>)Type.INT);
                    packetWrapper.write(Type.INT, count);
                    for (int i = 0; i < count; ++i) {
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
                this.handler(packetWrapper -> {
                    final int chunkX = packetWrapper.read((Type<Integer>)Type.INT);
                    final int chunkZ = packetWrapper.read((Type<Integer>)Type.INT);
                    final ClientWorld world = packetWrapper.user().get(ClientWorld.class);
                    final Chunk1_8Type chunk1_8Type = new Chunk1_8Type(world);
                    new BaseChunk(chunkX, chunkZ, true, false, 0, new ChunkSection[16], null, new ArrayList<CompoundTag>());
                    final BaseChunk baseChunk;
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
                //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:252)
                //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:185)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.nameVariables(AstMethodBodyBuilder.java:1482)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.populateVariables(AstMethodBodyBuilder.java:1411)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1151)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:993)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:548)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:377)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:318)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:213)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
                //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
                //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:144)
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
                this.handler(packetWrapper -> {
                    final int id = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    final int id2 = Effect.getOldId(id);
                    if (id2 == -1) {
                        packetWrapper.cancel();
                    }
                    else {
                        packetWrapper.set(Type.INT, 0, id2);
                        if (id2 == 2001) {
                            final int replacedBlock3 = ReplacementRegistry1_8to1_9.replace(packetWrapper.get((Type<Integer>)Type.INT, 1));
                            packetWrapper.set(Type.INT, 1, replacedBlock3);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.SPAWN_PARTICLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(packetWrapper -> {
                    final int type = packetWrapper.get((Type<Integer>)Type.INT, 0);
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
                this.handler(packetWrapper -> {
                    final int soundId = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    final String sound = SoundRemapper.oldNameFromId(soundId);
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
