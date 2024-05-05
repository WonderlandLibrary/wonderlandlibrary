package net.caffeinemc.phosphor.module.modules.misc;

import net.caffeinemc.phosphor.api.util.FakePlayerEntity;
import net.caffeinemc.phosphor.module.Module;

public class FakePlayerModule extends Module {
    public FakePlayerModule() {
        super("FakePlayer", "Spawns fake player", Category.MISC);
    }

    private FakePlayerEntity fakePlayer;

    @Override
    public void enable() {
        if (mc.player == null)
            return;
        super.enable();
    }

    @Override
    public void onEnable() {
        fakePlayer = new FakePlayerEntity(mc.player, "Radium", 20f, true);
        fakePlayer.spawn();
    }

    @Override
    public void onDisable() {
        fakePlayer.despawn();
    }
}
