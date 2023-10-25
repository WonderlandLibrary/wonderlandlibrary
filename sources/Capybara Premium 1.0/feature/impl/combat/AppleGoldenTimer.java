package fun.expensive.client.feature.impl.combat;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.expensive.client.feature.Feature;
import fun.expensive.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class AppleGoldenTimer extends Feature {
    public static boolean cooldown;
    private boolean isEated;
    public static BooleanSetting smart = new BooleanSetting("Smart", false, () -> true);


    public AppleGoldenTimer() {
        super("AppleGoldenTimer", FeatureCategory.Combat);
        addSettings(smart);
    }

    @EventTarget
    public void onUpdate(EventPreMotion eventUpdate) {
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
