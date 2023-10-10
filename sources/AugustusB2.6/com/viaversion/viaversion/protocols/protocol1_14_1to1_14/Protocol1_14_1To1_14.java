// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_14_1to1_14;

import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.protocol1_14_1to1_14.packets.EntityPackets;
import com.viaversion.viaversion.protocols.protocol1_14_1to1_14.metadata.MetadataRewriter1_14_1To1_14;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_14_1To1_14 extends AbstractProtocol<ClientboundPackets1_14, ClientboundPackets1_14, ServerboundPackets1_14, ServerboundPackets1_14>
{
    private final EntityRewriter metadataRewriter;
    
    public Protocol1_14_1To1_14() {
        super(ClientboundPackets1_14.class, ClientboundPackets1_14.class, ServerboundPackets1_14.class, ServerboundPackets1_14.class);
        this.metadataRewriter = new MetadataRewriter1_14_1To1_14(this);
    }
    
    @Override
    protected void registerPackets() {
        this.metadataRewriter.register();
        EntityPackets.register(this);
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.addEntityTracker(this.getClass(), new EntityTrackerBase(userConnection, Entity1_14Types.PLAYER));
    }
    
    @Override
    public EntityRewriter getEntityRewriter() {
        return this.metadataRewriter;
    }
}
