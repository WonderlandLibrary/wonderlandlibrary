// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api;

import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public abstract class BackwardsProtocol<C1 extends ClientboundPacketType, C2 extends ClientboundPacketType, S1 extends ServerboundPacketType, S2 extends ServerboundPacketType> extends AbstractProtocol<C1, C2, S1, S2>
{
    protected BackwardsProtocol() {
    }
    
    protected BackwardsProtocol(final Class<C1> oldClientboundPacketEnum, final Class<C2> clientboundPacketEnum, final Class<S1> oldServerboundPacketEnum, final Class<S2> serverboundPacketEnum) {
        super(oldClientboundPacketEnum, clientboundPacketEnum, oldServerboundPacketEnum, serverboundPacketEnum);
    }
    
    protected void executeAsyncAfterLoaded(final Class<? extends Protocol> protocolClass, final Runnable runnable) {
        Via.getManager().getProtocolManager().addMappingLoaderFuture(this.getClass(), protocolClass, runnable);
    }
    
    @Override
    public boolean hasMappingDataToLoad() {
        return false;
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return null;
    }
    
    public TranslatableRewriter getTranslatableRewriter() {
        return null;
    }
}
