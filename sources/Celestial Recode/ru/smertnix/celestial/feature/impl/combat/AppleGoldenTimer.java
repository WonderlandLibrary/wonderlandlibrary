package ru.smertnix.celestial.feature.impl.combat;

import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;

import org.lwjgl.input.Mouse;

import java.awt.*;

public class AppleGoldenTimer extends Feature {
    public static boolean cooldown;
    private boolean isEated;

    public AppleGoldenTimer() {
        super("GApple Timer", "Делает задержку на кушанье яблока",FeatureCategory.Combat);
    }

    @EventTarget
    public void onUpdate(EventPreMotion eventUpdate) {
    	if (!GuiBossOverlay.pot) {
    		return;
    	}
        if (mc.player.getHeldItemOffhand().isOnFinish() || mc.player.getHeldItemMainhand().isOnFinish() && mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
            isEated = true;
        }
        if (isEated) {
            mc.player.getCooldownTracker().setCooldown(Items.GOLDEN_APPLE, 55);
            isEated = false;
        }
        if (mc.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE) && mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
            mc.gameSettings.keyBindUseItem.setPressed(false);
        } else if (Mouse.isButtonDown(1) && !(mc.currentScreen instanceof GuiContainer)) {
            mc.gameSettings.keyBindUseItem.setPressed(true);
        }
    }

    private boolean isGoldenApple(ItemStack itemStack) {
        return (itemStack != null && !itemStack.func_190926_b() && itemStack.getItem() instanceof ItemAppleGold);
    }
}
