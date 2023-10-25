/*
 * Decompiled with CFR 0.150.
 */
package baritone.api;

import baritone.BaritoneProvider;
import baritone.api.IBaritoneProvider;
import baritone.api.Settings;

public final class BaritoneAPI {
    private static final IBaritoneProvider provider;
    private static final Settings settings;

    public static IBaritoneProvider getProvider() {
        return provider;
    }

    public static Settings getSettings() {
        return settings;
    }

    static {
        settings = new Settings();
        provider = new BaritoneProvider();
    }
}

