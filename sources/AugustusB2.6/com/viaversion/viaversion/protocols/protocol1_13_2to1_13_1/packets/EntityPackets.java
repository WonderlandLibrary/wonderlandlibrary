// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.packets;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.api.type.types.version.Types1_13;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.type.types.version.Types1_13_2;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.protocol.Protocol;

public class EntityPackets
{
    public static void register(final Protocol protocol) {
        final Iterator<Metadata> iterator;
        Metadata metadata;
        final PacketHandler metaTypeHandler = wrapper -> {
            wrapper.get(Types1_13_2.METADATA_LIST, 0).iterator();
            while (iterator.hasNext()) {
                metadata = iterator.next();
                metadata.setMetaType(Types1_13_2.META_TYPES.byId(metadata.metaType().typeId()));
            }
            return;
        };
        protocol.registerClientbound(ClientboundPackets1_13.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Types1_13.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler(metaTypeHandler);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_13.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_13.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler(metaTypeHandler);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_13.ENTITY_METADATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Types1_13.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler(metaTypeHandler);
            }
        });
    }
}
