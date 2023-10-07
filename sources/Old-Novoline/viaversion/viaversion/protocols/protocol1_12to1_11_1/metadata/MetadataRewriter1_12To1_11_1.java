package viaversion.viaversion.protocols.protocol1_12to1_11_1.metadata;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.entities.Entity1_12Types;
import viaversion.viaversion.api.entities.EntityType;
import viaversion.viaversion.api.minecraft.item.Item;
import viaversion.viaversion.api.minecraft.metadata.Metadata;
import viaversion.viaversion.api.rewriters.MetadataRewriter;
import viaversion.viaversion.protocols.protocol1_12to1_11_1.BedRewriter;
import viaversion.viaversion.protocols.protocol1_12to1_11_1.Protocol1_12To1_11_1;
import viaversion.viaversion.protocols.protocol1_12to1_11_1.storage.EntityTracker1_12;

import java.util.List;

public class MetadataRewriter1_12To1_11_1 extends MetadataRewriter {

    public MetadataRewriter1_12To1_11_1(Protocol1_12To1_11_1 protocol) {
        super(protocol, EntityTracker1_12.class);
    }

    @Override
    protected void handleMetadata(int entityId, EntityType type, Metadata metadata, List<Metadata> metadatas, UserConnection connection) {
        if (metadata.getValue() instanceof Item) {
            // Apply rewrite
            BedRewriter.toClientItem((Item) metadata.getValue());
        }

        if (type == null) return;
        // Evocation Illager aggressive property became 13
        if (type == Entity1_12Types.EntityType.EVOCATION_ILLAGER) {
            if (metadata.getId() == 12) {
                metadata.setId(13);
            }
        }
    }

    @Override
    protected EntityType getTypeFromId(int type) {
        return Entity1_12Types.getTypeFromId(type, false);
    }

    @Override
    protected EntityType getObjectTypeFromId(int type) {
        return Entity1_12Types.getTypeFromId(type, true);
    }
}
