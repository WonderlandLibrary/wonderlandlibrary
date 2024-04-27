/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.sponge.platform;

import com.viaversion.viaversion.configuration.AbstractViaConfig;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SpongeViaConfig
extends AbstractViaConfig {
    private static final List<String> UNSUPPORTED = Arrays.asList("anti-xray-patch", "bungee-ping-interval", "bungee-ping-save", "bungee-servers", "velocity-ping-interval", "velocity-ping-save", "velocity-servers", "quick-move-action-fix", "change-1_9-hitbox", "change-1_14-hitbox", "blockconnection-method");

    public SpongeViaConfig(File configFile) {
        super(new File(configFile, "config.yml"));
        this.reloadConfig();
    }

    @Override
    protected void handleConfig(Map<String, Object> config) {
    }

    @Override
    public List<String> getUnsupportedOptions() {
        return UNSUPPORTED;
    }
}

