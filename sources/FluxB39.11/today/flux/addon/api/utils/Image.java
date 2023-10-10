package today.flux.addon.api.utils;

import today.flux.utility.Base64ImageLocation;

import java.util.HashMap;
import java.util.Map;

public class Image {
    public static Map<String, Base64ImageLocation> maps = new HashMap<>();

    public final Base64ImageLocation location;

    public Image(String base64) {
        if (maps.containsKey(base64)) {
            location = maps.get(base64);
        } else {
            location = new Base64ImageLocation(base64);
            maps.put(base64, location);
        }
    }
}
