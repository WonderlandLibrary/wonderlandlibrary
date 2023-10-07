package viaversion.viarewind;

import viaversion.viarewind.api.ViaRewindConfig;
import viaversion.viarewind.api.ViaRewindPlatform;

public class ViaRewind {

    private static ViaRewindPlatform platform;
    private static ViaRewindConfig config;

    public static void init(ViaRewindPlatform platform, ViaRewindConfig config) {
        ViaRewind.platform = platform;
        ViaRewind.config = config;
    }

    public static ViaRewindPlatform getPlatform() {
        return platform;
    }

    public static ViaRewindConfig getConfig() {
        return config;
    }
}
