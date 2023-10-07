package viaversion.viaversion.protocols.protocol1_14_1to1_14.metadata;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.entities.Entity1_14Types;
import viaversion.viaversion.api.entities.EntityType;
import viaversion.viaversion.api.minecraft.metadata.Metadata;
import viaversion.viaversion.api.rewriters.MetadataRewriter;
import viaversion.viaversion.protocols.protocol1_14_1to1_14.Protocol1_14_1To1_14;
import viaversion.viaversion.protocols.protocol1_14_1to1_14.storage.EntityTracker1_14_1;

import java.util.List;

public class MetadataRewriter1_14_1To1_14 extends MetadataRewriter {

    public MetadataRewriter1_14_1To1_14(Protocol1_14_1To1_14 protocol) {
        super(protocol, EntityTracker1_14_1.class);
    }

    @Override
    public void handleMetadata(int entityId, EntityType type, Metadata metadata, List<Metadata> metadatas, UserConnection connection) {
        if (type == null) return;

        if (type == Entity1_14Types.EntityType.VILLAGER || type == Entity1_14Types.EntityType.WANDERING_TRADER) {
            if (metadata.getId() >= 15) {
                metadata.setId(metadata.getId() + 1);
            }
        }
    }

    @Override
    protected EntityType getTypeFromId(int type) {
        return Entity1_14Types.getTypeFromId(type);
    }
}
