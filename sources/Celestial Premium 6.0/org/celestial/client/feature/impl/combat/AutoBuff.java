/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.NumberSetting;

public class AutoBuff
extends Feature {
    private final NumberSetting health = new NumberSetting("Health", 15.0f, 1.0f, 20.0f, 0.5f);
    private boolean isActive;

    public AutoBuff() {
        super("AutoBuff", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043f\u044c\u0435\u0442 \u0437\u0435\u043b\u044c\u044f \u0438\u0441\u0446\u0435\u043b\u0435\u043d\u0438\u044f \u0432 \u043f\u0440\u0430\u0432\u043e\u0439 \u0440\u0443\u043a\u0435", Type.Player);
        this.addSettings(this.health);
    }

    private boolean isPotion(ItemStack itemStack) {
        return itemStack != null && !itemStack.isEmpty() && itemStack.getItem() instanceof ItemPotion;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (AutoBuff.mc.player == null || AutoBuff.mc.world == null) {
            return;
        }
        if (!(AutoBuff.mc.player.getHeldItemOffhand().getItem() instanceof ItemPotion)) {
            return;
        }
        if (this.isPotion(AutoBuff.mc.player.getHeldItemOffhand()) && AutoBuff.mc.player.getHealth() + AutoBuff.mc.player.getAbsorptionAmount() <= this.health.getCurrentValue()) {
            if (AutoBuff.mc.currentScreen != null) {
                GuiScreen guiScreen = AutoBuff.mc.currentScreen;
                guiScreen.allowUserInput = true;
            }
            this.isActive = true;
            KeyBinding.setKeyBindState(AutoBuff.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
        } else if (this.isActive) {
            KeyBinding.setKeyBindState(AutoBuff.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            this.isActive = false;
        }
    }
}

