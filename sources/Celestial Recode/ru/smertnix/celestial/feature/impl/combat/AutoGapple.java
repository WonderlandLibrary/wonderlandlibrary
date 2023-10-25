package ru.smertnix.celestial.feature.impl.combat;

import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.Helper;

public class AutoGapple extends Feature {
    private boolean isActive;
    public static NumberSetting health;

    public AutoGapple() {
        super("Auto GApple", "Автоматически кушает геплы при определенном значении", FeatureCategory.Combat);
        health = new NumberSetting("Health", 15, 1, 20, 1, () -> true);
        addSettings(health);
    }

    @EventTarget
    public void onUpdate(EventPreMotion eventUpdate) {
        this.setSuffix("" + (int) health.getNumberValue());
        if (mc.player == null || mc.world == null)
            return;
        if (isGoldenApple(mc.player.getHeldItemOffhand()) && mc.player.getHealth() <= health.getNumberValue()) {
            isActive = true;
            mc.gameSettings.keyBindUseItem.pressed = true;
        } else if (isActive) {
            mc.gameSettings.keyBindUseItem.pressed = false;
            isActive = false;
        }
    }

    private boolean isGoldenApple(ItemStack itemStack) {
        return (itemStack != null && !itemStack.func_190926_b() && itemStack.getItem() instanceof ItemAppleGold);
    }
}
