package org.dreamcore.client.feature.impl.combat;

import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.NumberSetting;

public class AutoGapple extends Feature {

    public static NumberSetting health;
    private boolean isActive;

    public AutoGapple() {
        super("AutoGApple", "Автоматически ест яблоко при опредленном здоровье", Type.Combat);
        health = new NumberSetting("Health Amount", 15, 1, 20, 1, () -> true);
        addSettings(health);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
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
        return (itemStack != null && !itemStack.isEmpty() && itemStack.getItem() instanceof ItemAppleGold);
    }
}
