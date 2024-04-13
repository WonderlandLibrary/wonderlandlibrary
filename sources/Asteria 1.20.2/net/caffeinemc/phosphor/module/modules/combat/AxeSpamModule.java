package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.MathUtils;
import net.caffeinemc.phosphor.mixin.MinecraftClientAccessor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class AxeSpamModule extends Module {
    private final NumberSetting delay = new NumberSetting("Delay", this, 0d, 0d, 10d, 1d);
    private final NumberSetting attackChance = new NumberSetting("Attack Chance", this, 100d, 0d, 100d, 1d);

    public AxeSpamModule() {
        super("AxeSpam", "Spams axe", Category.COMBAT);
    }

    private long startTime;
    private int tickTimer;

    @Override
    public void onEnable() {
        startTime = System.currentTimeMillis();
        tickTimer = 0;
    }

    private boolean isPassed(int millis) {
        return System.currentTimeMillis() - startTime > millis;
    }

    @EventHandler
    private void onPlayerTick(PlayerTickEvent event) {
        if (isPassed(1000)) {
            this.disable();
            return;
        }

        if (mc.currentScreen == null) {
            ItemStack mainHandStack = mc.player.getMainHandStack();

            if (mainHandStack.getItem() instanceof AxeItem) {
                if (tickTimer < delay.getIValue()) {
                    tickTimer++;
                    return;
                }

                int randomNum = MathUtils.getRandomInt(1, 100);

                if (randomNum <= attackChance.getIValue()) {
                    ((MinecraftClientAccessor) mc).callDoAttack();
                    tickTimer = 0;
                }
            }
        }
    }
}
