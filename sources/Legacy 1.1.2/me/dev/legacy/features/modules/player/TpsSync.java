package me.dev.legacy.features.modules.player;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;

public class TpsSync extends Module {
    private static TpsSync INSTANCE = new TpsSync();
    public Setting<Boolean> attack = register(new Setting<Boolean>("Attack", Boolean.FALSE));
    public Setting<Boolean> mining = register(new Setting<Boolean>("Mine", Boolean.TRUE));

    public TpsSync() {
        super("TpsSync", "Syncs your client with the TPS.", Module.Category.PLAYER, true, false, false);
        setInstance();
    }

    public static TpsSync getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TpsSync();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}
