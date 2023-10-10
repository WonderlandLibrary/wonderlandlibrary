// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_11to1_11_1;

import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_11Types;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viabackwards.protocol.protocol1_11to1_11_1.packets.ItemPackets1_11_1;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viabackwards.protocol.protocol1_11to1_11_1.packets.EntityPackets1_11_1;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_11To1_11_1 extends BackwardsProtocol<ClientboundPackets1_9_3, ClientboundPackets1_9_3, ServerboundPackets1_9_3, ServerboundPackets1_9_3>
{
    private final EntityPackets1_11_1 entityPackets;
    private final ItemRewriter itemRewriter;
    
    public Protocol1_11To1_11_1() {
        super(ClientboundPackets1_9_3.class, ClientboundPackets1_9_3.class, ServerboundPackets1_9_3.class, ServerboundPackets1_9_3.class);
        this.entityPackets = new EntityPackets1_11_1(this);
        this.itemRewriter = new ItemPackets1_11_1(this);
    }
    
    @Override
    protected void registerPackets() {
        this.entityPackets.register();
        this.itemRewriter.register();
    }
    
    @Override
    public void init(final UserConnection user) {
        if (!user.has(ClientWorld.class)) {
            user.put(new ClientWorld(user));
        }
        user.addEntityTracker(this.getClass(), new EntityTrackerBase(user, Entity1_11Types.EntityType.PLAYER));
    }
    
    @Override
    public EntityPackets1_11_1 getEntityRewriter() {
        return this.entityPackets;
    }
    
    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }
}
