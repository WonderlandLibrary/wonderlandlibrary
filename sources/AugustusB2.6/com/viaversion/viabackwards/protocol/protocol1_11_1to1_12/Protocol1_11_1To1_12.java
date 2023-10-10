// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_11_1to1_12;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.data.ShoulderTracker;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_12Types;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets.ChatPackets1_12;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets.SoundPackets1_12;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets.BlockItemPackets1_12;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets.EntityPackets1_12;
import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_11_1To1_12 extends BackwardsProtocol<ClientboundPackets1_12, ClientboundPackets1_9_3, ServerboundPackets1_12, ServerboundPackets1_9_3>
{
    private static final BackwardsMappings MAPPINGS;
    private final EntityPackets1_12 entityPackets;
    private final BlockItemPackets1_12 blockItemPackets;
    
    public Protocol1_11_1To1_12() {
        super(ClientboundPackets1_12.class, ClientboundPackets1_9_3.class, ServerboundPackets1_12.class, ServerboundPackets1_9_3.class);
        this.entityPackets = new EntityPackets1_12(this);
        this.blockItemPackets = new BlockItemPackets1_12(this);
    }
    
    @Override
    protected void registerPackets() {
        this.blockItemPackets.register();
        this.entityPackets.register();
        new SoundPackets1_12(this).register();
        new ChatPackets1_12(this).register();
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_12.TITLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int action;
                JsonElement component;
                this.handler(wrapper -> {
                    action = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    if (action >= 0 && action <= 2) {
                        component = wrapper.read(Type.COMPONENT);
                        wrapper.write(Type.COMPONENT, Protocol1_9To1_8.fixJson(component.toString()));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_12.ADVANCEMENTS);
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_12.UNLOCK_RECIPES);
        ((AbstractProtocol<ClientboundPackets1_12, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_12.SELECT_ADVANCEMENTS_TAB);
    }
    
    @Override
    public void init(final UserConnection user) {
        if (!user.has(ClientWorld.class)) {
            user.put(new ClientWorld(user));
        }
        user.addEntityTracker(this.getClass(), new EntityTrackerBase(user, Entity1_12Types.EntityType.PLAYER, true));
        user.put(new ShoulderTracker(user));
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_11_1To1_12.MAPPINGS;
    }
    
    @Override
    public EntityPackets1_12 getEntityRewriter() {
        return this.entityPackets;
    }
    
    @Override
    public BlockItemPackets1_12 getItemRewriter() {
        return this.blockItemPackets;
    }
    
    @Override
    public boolean hasMappingDataToLoad() {
        return true;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.11", (Class)null, true) {
            @Override
            protected boolean shouldWarnOnMissing(final String key) {
                return super.shouldWarnOnMissing(key) && !key.equals("sounds");
            }
        };
    }
}
