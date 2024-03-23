package dev.zprestige.prestige.client.module.impl.movement;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.util.impl.RandomUtil;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;

public class AutoJumpReset extends Module {
    public FloatSetting field266;

    public AutoJumpReset() {
        super("Auto Jump Reset", Category.Movement, "Automatically jumps when taking knockback to reduce knockback");
        field266 = setting("Chance", 50f, 0f, 100f).description("Chance to jump when taking knockback");
    }

    @EventListener
    public void event(TickEvent event) {
        if (!(getMc().currentScreen instanceof HandledScreen) && !getMc().player.isBlocking() && !getMc().player.isUsingItem() && !getMc().player.isTouchingWater() && getMc().player.getAttacker() instanceof PlayerEntity
                && getMc().player.getAttacker().isOnGround() && getMc().player.getAttacker().hurtTime > 0 && getMc().player.getAttacker().hurtTime < 10 && getMc().player.getAttacker().hurtTime == getMc().player.getAttacker().maxHurtTime - 1
                && !getMc().player.getAttacker().isOnFire() && RandomUtil.INSTANCE.getRandom().nextFloat() <= field266.getObject() / 100) {
            getMc().player.jump();
        }
    }
}
