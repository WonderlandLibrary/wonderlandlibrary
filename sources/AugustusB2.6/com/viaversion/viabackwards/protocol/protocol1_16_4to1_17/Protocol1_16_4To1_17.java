// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_16_4to1_17;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_17Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.storage.PingRequests;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import java.util.Iterator;
import com.viaversion.viaversion.rewriter.IdRewriteFunction;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import java.util.ArrayList;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.TagData;
import java.util.List;
import java.util.HashMap;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.util.Objects;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;
import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.packets.EntityPackets1_17;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.packets.BlockItemPackets1_17;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public final class Protocol1_16_4To1_17 extends BackwardsProtocol<ClientboundPackets1_17, ClientboundPackets1_16_2, ServerboundPackets1_17, ServerboundPackets1_16_2>
{
    public static final BackwardsMappings MAPPINGS;
    private static final int[] EMPTY_ARRAY;
    private final EntityRewriter entityRewriter;
    private BlockItemPackets1_17 blockItemPackets;
    private TranslatableRewriter translatableRewriter;
    
    public Protocol1_16_4To1_17() {
        super(ClientboundPackets1_17.class, ClientboundPackets1_16_2.class, ServerboundPackets1_17.class, ServerboundPackets1_16_2.class);
        this.entityRewriter = new EntityPackets1_17(this);
    }
    
    @Override
    protected void registerPackets() {
        final Class<Protocol1_17To1_16_4> protocolClass = Protocol1_17To1_16_4.class;
        final BackwardsMappings mappings = Protocol1_16_4To1_17.MAPPINGS;
        Objects.requireNonNull(mappings);
        this.executeAsyncAfterLoaded(protocolClass, mappings::load);
        (this.translatableRewriter = new TranslatableRewriter(this)).registerChatMessage(ClientboundPackets1_17.CHAT_MESSAGE);
        this.translatableRewriter.registerBossBar(ClientboundPackets1_17.BOSSBAR);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_17.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_17.TAB_LIST);
        this.translatableRewriter.registerOpenWindow(ClientboundPackets1_17.OPEN_WINDOW);
        this.translatableRewriter.registerPing();
        (this.blockItemPackets = new BlockItemPackets1_17(this, this.translatableRewriter)).register();
        this.entityRewriter.register();
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound(ClientboundPackets1_17.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_17.ENTITY_SOUND);
        soundRewriter.registerNamedSound(ClientboundPackets1_17.NAMED_SOUND);
        soundRewriter.registerStopSound(ClientboundPackets1_17.STOP_SOUND);
        final TagRewriter tagRewriter = new TagRewriter(this);
        ((AbstractProtocol<ClientboundPackets1_17, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_17.TAGS, new PacketRemapper() {
            @Override
            public void registerMap() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: aload_0         /* this */
                //     2: getfield        com/viaversion/viabackwards/protocol/protocol1_16_4to1_17/Protocol1_16_4To1_17$1.val$tagRewriter:Lcom/viaversion/viaversion/rewriter/TagRewriter;
                //     5: invokedynamic   BootstrapMethod #0, handle:(Lcom/viaversion/viaversion/rewriter/TagRewriter;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
                //    10: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_16_4to1_17/Protocol1_16_4To1_17$1.handler:(Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
                //    13: return         
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Could not infer any expression.
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
                //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
        new StatisticsRewriter(this).register(ClientboundPackets1_17.STATISTICS);
        ((AbstractProtocol<ClientboundPackets1_17, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_17.RESOURCE_PACK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough(Type.STRING);
                    wrapper.passthrough(Type.STRING);
                    wrapper.read((Type<Object>)Type.BOOLEAN);
                    wrapper.read(Type.OPTIONAL_COMPONENT);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_17.EXPLOSION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.handler(wrapper -> wrapper.write(Type.INT, (Integer)wrapper.read((Type<T>)Type.VAR_INT)));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_17.SPAWN_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14);
                this.handler(wrapper -> wrapper.read((Type<Object>)Type.FLOAT));
            }
        });
        ((Protocol<ClientboundPackets1_17, ClientboundPackets1_16_2, S1, S2>)this).registerClientbound(ClientboundPackets1_17.PING, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int id;
                final short shortId;
                PacketWrapper acknowledgementPacket;
                PacketWrapper pongPacket;
                this.handler(wrapper -> {
                    wrapper.cancel();
                    id = wrapper.read((Type<Integer>)Type.INT);
                    shortId = (short)id;
                    if (id == shortId && ViaBackwards.getConfig().handlePingsAsInvAcknowledgements()) {
                        wrapper.user().get(PingRequests.class).addId(shortId);
                        acknowledgementPacket = wrapper.create(ClientboundPackets1_16_2.WINDOW_CONFIRMATION);
                        acknowledgementPacket.write(Type.UNSIGNED_BYTE, (Short)0);
                        acknowledgementPacket.write(Type.SHORT, shortId);
                        acknowledgementPacket.write(Type.BOOLEAN, false);
                        acknowledgementPacket.send(Protocol1_16_4To1_17.class);
                    }
                    else {
                        pongPacket = wrapper.create(ServerboundPackets1_17.PONG);
                        pongPacket.write(Type.INT, id);
                        pongPacket.sendToServer(Protocol1_16_4To1_17.class);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_16_2>)this).registerServerbound(ServerboundPackets1_16_2.CLIENT_SETTINGS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> wrapper.write(Type.BOOLEAN, false));
            }
        });
        this.mergePacket(ClientboundPackets1_17.TITLE_TEXT, ClientboundPackets1_16_2.TITLE, 0);
        this.mergePacket(ClientboundPackets1_17.TITLE_SUBTITLE, ClientboundPackets1_16_2.TITLE, 1);
        this.mergePacket(ClientboundPackets1_17.ACTIONBAR, ClientboundPackets1_16_2.TITLE, 2);
        this.mergePacket(ClientboundPackets1_17.TITLE_TIMES, ClientboundPackets1_16_2.TITLE, 3);
        ((Protocol<ClientboundPackets1_17, ClientboundPackets1_16_2, S1, S2>)this).registerClientbound(ClientboundPackets1_17.CLEAR_TITLES, ClientboundPackets1_16_2.TITLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                        wrapper.write(Type.VAR_INT, 5);
                    }
                    else {
                        wrapper.write(Type.VAR_INT, 4);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_17.ADD_VIBRATION_SIGNAL);
    }
    
    @Override
    public void init(final UserConnection user) {
        this.addEntityTracker(user, new EntityTrackerBase(user, Entity1_17Types.PLAYER));
        user.put(new PingRequests());
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_16_4To1_17.MAPPINGS;
    }
    
    @Override
    public TranslatableRewriter getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    public void mergePacket(final ClientboundPackets1_17 newPacketType, final ClientboundPackets1_16_2 oldPacketType, final int type) {
        ((Protocol<ClientboundPackets1_17, ClientboundPackets1_16_2, S1, S2>)this).registerClientbound(newPacketType, oldPacketType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> wrapper.write(Type.VAR_INT, type));
            }
        });
    }
    
    @Override
    public EntityRewriter getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public ItemRewriter getItemRewriter() {
        return this.blockItemPackets;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.17", "1.16.2", Protocol1_17To1_16_4.class, true);
        EMPTY_ARRAY = new int[0];
    }
}
