package viaversion.viaversion.protocols.protocol1_15to1_14_4.metadata;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.entities.Entity1_15Types;
import viaversion.viaversion.api.entities.EntityType;
import viaversion.viaversion.api.minecraft.item.Item;
import viaversion.viaversion.api.minecraft.metadata.Metadata;
import viaversion.viaversion.api.minecraft.metadata.types.MetaType1_14;
import viaversion.viaversion.api.rewriters.MetadataRewriter;
import viaversion.viaversion.api.type.types.Particle;
import viaversion.viaversion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;
import viaversion.viaversion.protocols.protocol1_15to1_14_4.packets.EntityPackets;
import viaversion.viaversion.protocols.protocol1_15to1_14_4.packets.InventoryPackets;
import viaversion.viaversion.protocols.protocol1_15to1_14_4.storage.EntityTracker1_15;

import java.util.List;

public class MetadataRewriter1_15To1_14_4 extends MetadataRewriter {

	public MetadataRewriter1_15To1_14_4(Protocol1_15To1_14_4 protocol) {
		super(protocol, EntityTracker1_15.class);
	}

	@Override
	public void handleMetadata(int entityId, EntityType type, Metadata metadata, List<Metadata> metadatas,
							   UserConnection connection) throws Exception {
		if(metadata.getMetaType() == MetaType1_14.Slot) {
			InventoryPackets.toClient((Item) metadata.getValue());
		} else if(metadata.getMetaType() == MetaType1_14.BlockID) {
			// Convert to new block id
			int data = (int) metadata.getValue();
			metadata.setValue(protocol.getMappingData().getNewBlockStateId(data));
		}

		if(type == null) return;

		// Metadata 12 added to abstract_living
		if(metadata.getId() > 11 && type.isOrHasParent(Entity1_15Types.EntityType.LIVINGENTITY)) {
			metadata.setId(metadata.getId() + 1); //TODO is it 11 or 12? what is it for?
		}

		//NOTES:
		//new boolean with id 11 for trident, default = false, added in 19w45a
		//new boolean with id 17 for enderman

		if(type.isOrHasParent(Entity1_15Types.EntityType.WOLF)) {
			if(metadata.getId() == 18) {
				metadatas.remove(metadata);
			} else if(metadata.getId() > 18) {
				metadata.setId(metadata.getId() - 1);
			}
		} else if(type == Entity1_15Types.EntityType.AREA_EFFECT_CLOUD && metadata.getId() == 10) {
			rewriteParticle((Particle) metadata.getValue());
		}
	}

	@Override
	public int getNewEntityId(int oldId) {
		return EntityPackets.getNewEntityId(oldId);
	}

	@Override
	protected EntityType getTypeFromId(int type) {
		return Entity1_15Types.getTypeFromId(type);
	}
}
