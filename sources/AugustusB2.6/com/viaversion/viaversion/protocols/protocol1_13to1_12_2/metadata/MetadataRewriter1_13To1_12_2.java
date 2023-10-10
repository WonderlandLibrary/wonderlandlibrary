// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.metadata;

import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.EntityTypeRewriter;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.ParticleRewriter;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets.WorldPackets;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.api.type.types.version.Types1_13;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.rewriter.EntityRewriter;

public class MetadataRewriter1_13To1_12_2 extends EntityRewriter<Protocol1_13To1_12_2>
{
    public MetadataRewriter1_13To1_12_2(final Protocol1_13To1_12_2 protocol) {
        super(protocol);
    }
    
    @Override
    protected void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) throws Exception {
        if (metadata.metaType().typeId() > 4) {
            metadata.setMetaType(Types1_13.META_TYPES.byId(metadata.metaType().typeId() + 1));
        }
        else {
            metadata.setMetaType(Types1_13.META_TYPES.byId(metadata.metaType().typeId()));
        }
        if (metadata.id() == 2) {
            if (metadata.getValue() != null && !((String)metadata.getValue()).isEmpty()) {
                metadata.setTypeAndValue(Types1_13.META_TYPES.optionalComponentType, ChatRewriter.legacyTextToJson((String)metadata.getValue()));
            }
            else {
                metadata.setTypeAndValue(Types1_13.META_TYPES.optionalComponentType, null);
            }
        }
        if (type == Entity1_13Types.EntityType.ENDERMAN && metadata.id() == 12) {
            final int stateId = (int)metadata.getValue();
            final int id = stateId & 0xFFF;
            final int data = stateId >> 12 & 0xF;
            metadata.setValue(id << 4 | (data & 0xF));
        }
        if (metadata.metaType() == Types1_13.META_TYPES.itemType) {
            metadata.setMetaType(Types1_13.META_TYPES.itemType);
            ((Protocol1_13To1_12_2)this.protocol).getItemRewriter().handleItemToClient((Item)metadata.getValue());
        }
        else if (metadata.metaType() == Types1_13.META_TYPES.blockStateType) {
            metadata.setValue(WorldPackets.toNewId((int)metadata.getValue()));
        }
        if (type == null) {
            return;
        }
        if (type == Entity1_13Types.EntityType.WOLF && metadata.id() == 17) {
            metadata.setValue(15 - (int)metadata.getValue());
        }
        if (type.isOrHasParent(Entity1_13Types.EntityType.ZOMBIE) && metadata.id() > 14) {
            metadata.setId(metadata.id() + 1);
        }
        if (type.isOrHasParent(Entity1_13Types.EntityType.MINECART_ABSTRACT) && metadata.id() == 9) {
            final int oldId = (int)metadata.getValue();
            final int combined = (oldId & 0xFFF) << 4 | (oldId >> 12 & 0xF);
            final int newId = WorldPackets.toNewId(combined);
            metadata.setValue(newId);
        }
        if (type == Entity1_13Types.EntityType.AREA_EFFECT_CLOUD) {
            if (metadata.id() == 9) {
                final int particleId = (int)metadata.getValue();
                final Metadata parameter1Meta = this.metaByIndex(10, metadatas);
                final Metadata parameter2Meta = this.metaByIndex(11, metadatas);
                final int parameter1 = (int)((parameter1Meta != null) ? parameter1Meta.getValue() : 0);
                final int parameter2 = (int)((parameter2Meta != null) ? parameter2Meta.getValue() : 0);
                final Particle particle = ParticleRewriter.rewriteParticle(particleId, new Integer[] { parameter1, parameter2 });
                if (particle != null && particle.getId() != -1) {
                    metadatas.add(new Metadata(9, Types1_13.META_TYPES.particleType, particle));
                }
            }
            if (metadata.id() >= 9) {
                metadatas.remove(metadata);
            }
        }
        if (metadata.id() == 0) {
            metadata.setValue((byte)((byte)metadata.getValue() & 0xFFFFFFEF));
        }
    }
    
    @Override
    public int newEntityId(final int id) {
        return EntityTypeRewriter.getNewId(id);
    }
    
    @Override
    public EntityType typeFromId(final int type) {
        return Entity1_13Types.getTypeFromId(type, false);
    }
    
    @Override
    public EntityType objectTypeFromId(final int type) {
        return Entity1_13Types.getTypeFromId(type, true);
    }
}
