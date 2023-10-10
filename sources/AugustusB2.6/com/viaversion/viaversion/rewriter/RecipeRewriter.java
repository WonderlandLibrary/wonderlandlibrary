// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.HashMap;
import java.util.Map;
import com.viaversion.viaversion.api.protocol.Protocol;

public abstract class RecipeRewriter
{
    protected final Protocol protocol;
    protected final Map<String, RecipeConsumer> recipeHandlers;
    
    protected RecipeRewriter(final Protocol protocol) {
        this.recipeHandlers = new HashMap<String, RecipeConsumer>();
        this.protocol = protocol;
    }
    
    public void handle(final PacketWrapper wrapper, final String type) throws Exception {
        final RecipeConsumer handler = this.recipeHandlers.get(type);
        if (handler != null) {
            handler.accept(wrapper);
        }
    }
    
    public void registerDefaultHandler(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                int size;
                int i;
                String type;
                String id;
                this.handler(wrapper -> {
                    for (size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        type = wrapper.passthrough(Type.STRING).replace("minecraft:", "");
                        id = wrapper.passthrough(Type.STRING);
                        RecipeRewriter.this.handle(wrapper, type);
                    }
                });
            }
        });
    }
    
    protected void rewrite(final Item item) {
        if (this.protocol.getItemRewriter() != null) {
            this.protocol.getItemRewriter().handleItemToClient(item);
        }
    }
    
    @FunctionalInterface
    public interface RecipeConsumer
    {
        void accept(final PacketWrapper p0) throws Exception;
    }
}
