/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.settings.impl.NumberSetting;

public class AutoGApple
extends Feature {
    public static NumberSetting health;
    public static NumberSetting eatDelay;
    private boolean isActive;
    private final TimerHelper timerHelper = new TimerHelper();

    public AutoGApple() {
        super("AutoGApple", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0435\u0441\u0442 \u044f\u0431\u043b\u043e\u043a\u043e \u043f\u0440\u0438 \u043e\u043f\u0440\u0435\u0434\u043b\u0435\u043d\u043d\u043e\u043c \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435", Type.Combat);
        eatDelay = new NumberSetting("Eat Delay", 300.0f, 0.0f, 1000.0f, 50.0f, () -> true);
        health = new NumberSetting("Health Amount", 15.0f, 1.0f, 20.0f, 1.0f, () -> true);
        this.addSettings(health, eatDelay);
    }

    private boolean isGoldenApple(ItemStack itemStack) {
        return itemStack != null && !itemStack.isEmpty() && itemStack.getItem() instanceof ItemAppleGold;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + (int)health.getCurrentValue());
        if (AutoGApple.mc.player == null || AutoGApple.mc.world == null) {
            return;
        }
        if (!(AutoGApple.mc.player.getHeldItemOffhand().getItem() instanceof ItemAppleGold)) {
            return;
        }
        if (this.timerHelper.hasReached(eatDelay.getCurrentValue())) {
            if (this.isGoldenApple(AutoGApple.mc.player.getHeldItemOffhand()) && AutoGApple.mc.player.getHealth() + AutoGApple.mc.player.getAbsorptionAmount() <= health.getCurrentValue()) {
                if (AutoGApple.mc.currentScreen != null) {
                    GuiScreen guiScreen = AutoGApple.mc.currentScreen;
                    guiScreen.allowUserInput = true;
                }
                this.isActive = true;
                KeyBinding.setKeyBindState(AutoGApple.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            } else if (this.isActive) {
                KeyBinding.setKeyBindState(AutoGApple.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                this.isActive = false;
            }
            this.timerHelper.reset();
        }
    }
}

