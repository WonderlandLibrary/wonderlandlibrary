package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.WorldTickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.MathUtils;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;

public class AutoJumpResetModule extends Module {
    private final NumberSetting jumpResetChance = new NumberSetting("Jump Reset Chance", this, 100, 0, 100, 1);

    public AutoJumpResetModule() {
        super("AutoJumpReset", "Automatically doing jump reset", Category.COMBAT);
    }

    @EventHandler
    private void onWorldTick(WorldTickEvent event) {
        if (mc.player == null) {
            return;
        }
        if (mc.player.isBlocking()) {
            return;
        }
        if (mc.player.isUsingItem()) {
            return;
        }
        if (mc.currentScreen instanceof HandledScreen) {
            return;
        }
        if (!mc.player.isOnGround()) {
            return;
        }
        if (mc.player.maxHurtTime == 0) {
            return;
        }
        if (mc.player.hurtTime == 0) {
            return;
        }
        if (!(mc.player.getAttacker() instanceof PlayerEntity)) {
            return;
        }
        if (mc.player.isInsideWaterOrBubbleColumn()) {
            return;
        }
        if (mc.player.isInsideWall()) {
            return;
        }
        if (mc.player.isTouchingWater()) {
            return;
        }
        if (mc.player.hurtTime == mc.player.maxHurtTime - 1 && MathUtils.getRandomInt(0, 100) <= jumpResetChance.getIValue()) {
            mc.player.jump();
        }
    }
}
