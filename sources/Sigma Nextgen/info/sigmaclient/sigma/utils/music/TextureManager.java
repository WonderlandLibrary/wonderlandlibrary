package info.sigmaclient.sigma.utils.music;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import top.fl0wowp4rty.phantomshield.annotations.Native;
@Native

public class TextureManager {

	public static List<TextureImage> cache = new CopyOnWriteArrayList<TextureImage>();

	public static boolean exists(String location) {
		return get(location) != null;
	}

	public static TextureImage get(String location) {
		for(TextureImage ti : cache) {
			if (ti.location.equalsIgnoreCase(location)) {
				return ti;
			}
		}
		return null;
	}
}
