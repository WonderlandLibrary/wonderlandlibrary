package viaversion.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import com.github.steveice10.opennbt.tag.builtin.ByteTag;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import viaversion.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider.BackwardsBlockEntityHandler;
import viaversion.viaversion.api.data.UserConnection;

public class SkullHandler implements BackwardsBlockEntityHandler {

	private static final int SKULL_START = 5447;

	@Override
	public CompoundTag transform(UserConnection user, int blockId, CompoundTag tag) {
		int diff = blockId - SKULL_START;
		int pos = diff % 20;
		byte type = (byte) Math.floor(diff / 20f);

		// Set type
		tag.put(new ByteTag("SkullType", type));

		// Remove wall skulls
		if(pos < 4) {
			return tag;
		}

		// Add rotation for normal skulls
		tag.put(new ByteTag("Rot", (byte) ((pos - 4) & 255)));

		return tag;
	}
}
