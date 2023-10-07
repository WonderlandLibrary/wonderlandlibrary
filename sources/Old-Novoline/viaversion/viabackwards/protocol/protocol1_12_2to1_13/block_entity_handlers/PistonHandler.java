package viaversion.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import viaversion.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.data.MappingDataLoader;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;

public class PistonHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler {

	private final Map<String, Integer> pistonIds = new HashMap<>();

	public PistonHandler() {
		if(Via.getConfig().isServersideBlockConnections()) {
			Map<String, Integer> keyToId = ConnectionData.keyToId;

			for(Map.Entry<String, Integer> entry : keyToId.entrySet()) {
				if(!entry.getKey().contains("piston")) continue;

				addEntries(entry.getKey(), entry.getValue());
			}
		} else {
			JsonObject mappings = MappingDataLoader.getMappingsCache().get("mapping-1.13.json").getAsJsonObject("blockstates");
			for(Map.Entry<String, JsonElement> blockState : mappings.entrySet()) {
				String key = blockState.getValue().getAsString();
				if(!key.contains("piston")) continue;

				addEntries(key, Integer.parseInt(blockState.getKey()));
			}
		}
	}

	// There doesn't seem to be a nicer way around it :(
	private void addEntries(String data, int id) {
		id = Protocol1_12_2To1_13.MAPPINGS.getNewBlockStateId(id);
		pistonIds.put(data, id);

		String substring = data.substring(10);
		if(!substring.startsWith("piston") && !substring.startsWith("sticky_piston")) return;

		// Swap properties and add them to the map
		String[] split = data.substring(0, data.length() - 1).split("\\[");
		String[] properties = split[1].split(",");
		data = split[0] + "[" + properties[1] + "," + properties[0] + "]";
		pistonIds.put(data, id);
	}

	@Override
	public CompoundTag transform(UserConnection user, int blockId, CompoundTag tag) {
		CompoundTag blockState = tag.get("blockState");
		if(blockState == null) return tag;

		String dataFromTag = getDataFromTag(blockState);
		if(dataFromTag == null) return tag;

		Integer id = pistonIds.get(dataFromTag);
		if(id == null) {
			//TODO see why this could be null and if this is bad
			return tag;
		}

		tag.put(new IntTag("blockId", id >> 4));
		tag.put(new IntTag("blockData", id & 15));
		return tag;
	}

	// The type hasn't actually been updated in the blockstorage, so we need to construct it
	private String getDataFromTag(CompoundTag tag) {
		StringTag name = tag.get("Name");
		if(name == null) return null;

		CompoundTag properties = tag.get("Properties");
		if(properties == null) return name.getValue();

		StringJoiner joiner = new StringJoiner(",", name.getValue() + "[", "]");

		for(Tag property : properties) {
			if(!(property instanceof StringTag)) continue;
			joiner.add(property.getName() + "=" + ((StringTag) property).getValue());
		}

		return joiner.toString();
	}
}
