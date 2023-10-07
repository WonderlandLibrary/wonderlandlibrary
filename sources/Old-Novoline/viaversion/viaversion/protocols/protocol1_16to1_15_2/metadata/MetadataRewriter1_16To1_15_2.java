package viaversion.viaversion.protocols.protocol1_16to1_15_2.metadata;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.entities.Entity1_15Types;
import viaversion.viaversion.api.entities.Entity1_16Types;
import viaversion.viaversion.api.entities.EntityType;
import viaversion.viaversion.api.minecraft.item.Item;
import viaversion.viaversion.api.minecraft.metadata.Metadata;
import viaversion.viaversion.api.minecraft.metadata.types.MetaType1_14;
import viaversion.viaversion.api.rewriters.MetadataRewriter;
import viaversion.viaversion.api.type.types.Particle;
import viaversion.viaversion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import viaversion.viaversion.protocols.protocol1_16to1_15_2.packets.InventoryPackets;
import viaversion.viaversion.protocols.protocol1_16to1_15_2.storage.EntityTracker1_16;

import java.util.List;

public class MetadataRewriter1_16To1_15_2 extends MetadataRewriter {

    public MetadataRewriter1_16To1_15_2(Protocol1_16To1_15_2 protocol) {
        super(protocol, EntityTracker1_16.class);
        mapType(Entity1_15Types.EntityType.ZOMBIE_PIGMAN, Entity1_16Types.EntityType.ZOMBIFIED_PIGLIN);
        mapTypes(Entity1_15Types.EntityType.values(), Entity1_16Types.EntityType.class);
    }

    @Override
    public void handleMetadata(int entityId, EntityType type, Metadata metadata, List<Metadata> metadatas, UserConnection connection) throws Exception {
        if (metadata.getMetaType() == MetaType1_14.Slot) {
            InventoryPackets.toClient((Item) metadata.getValue());
        } else if (metadata.getMetaType() == MetaType1_14.BlockID) {
            int data = (int) metadata.getValue();
            metadata.setValue(protocol.getMappingData().getNewBlockStateId(data));
        }

        if (type == null) return;

        if (type == Entity1_16Types.EntityType.AREA_EFFECT_CLOUD) {
            if (metadata.getId() == 10) {
                rewriteParticle((Particle) metadata.getValue());
            }
        } else if (type.isOrHasParent(Entity1_16Types.EntityType.ABSTRACT_ARROW)) {
            if (metadata.getId() == 8) {
                metadatas.remove(metadata);
            } else if (metadata.getId() > 8) {
                metadata.setId(metadata.getId() - 1);
            }
        }
    }

    @Override
    protected EntityType getTypeFromId(int type) {
        return Entity1_16Types.getTypeFromId(type);
    }
}
