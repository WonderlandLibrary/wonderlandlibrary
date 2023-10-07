package viaversion.viabackwards.protocol.protocol1_12_2to1_13.data;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import viaversion.viaversion.protocols.protocol1_13to1_12_2.data.EntityTypeRewriter;

public class EntityTypeMapping {

	private static final Int2IntFunction TYPES = new Int2IntOpenHashMap();

	static {
		TYPES.defaultReturnValue(-1);

		for(Int2IntMap.Entry entry : EntityTypeRewriter.ENTITY_TYPES.int2IntEntrySet()) {
			TYPES.put(entry.getIntValue(), entry.getIntKey());
		}
	}

	public static int getOldId(int type1_13) {
		return TYPES.get(type1_13);
	}
}
