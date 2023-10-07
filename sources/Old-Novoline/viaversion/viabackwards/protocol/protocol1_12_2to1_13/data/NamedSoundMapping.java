package viaversion.viabackwards.protocol.protocol1_12_2to1_13.data;

import java.util.HashMap;
import java.util.Map;
import viaversion.viaversion.protocols.protocol1_13to1_12_2.data.NamedSoundRewriter;

public class NamedSoundMapping {

	private static final Map<String, String> SOUNDS = new HashMap<>();

	static {
		NamedSoundRewriter.oldToNew.forEach((sound1_12, sound1_13) -> SOUNDS.put(sound1_13, sound1_12));
	}

	public static String getOldId(String sound1_13) {
		return SOUNDS.get(sound1_13);
	}
}
