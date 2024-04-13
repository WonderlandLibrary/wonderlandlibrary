package net.smoothboot.client.module.combat;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.slot.SlotActionType;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.BooleanSetting;
import net.smoothboot.client.module.settings.NumberSetting;
import org.lwjgl.glfw.GLFW;

public class Hotbar_Refill extends Mod {

    public BooleanSetting refillPearls = new BooleanSetting("Pearls", true);
    public NumberSetting pearlSlot = new NumberSetting("Pearl slot", 1, 9, 9, 1);
    public BooleanSetting refillPots = new BooleanSetting("MiddleClick pot", true);

    public Hotbar_Refill() {
        super("Hotbar Refill", "", Category.Combat);
        addsettings(refillPearls, pearlSlot, refillPots);
    }

    private boolean isPearl(ItemStack stack) {
        return stack.getItem() == Items.ENDER_PEARL;
    }

    @Override
    public void onTick() {
        if (mc.currentScreen instanceof InventoryScreen) {
            if (refillPots.isEnabled()) {
                int potionInInventory = findPot(9, 36);
                if (potionInInventory != -1 && GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_3) == GLFW.GLFW_PRESS) {
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, potionInInventory, 1, SlotActionType.QUICK_MOVE, mc.player);
                }
            }
            if (refillPearls.isEnabled()) {
                if (findPearl > 8 && findPearl < 36 && isPearl(mc.player.getInventory().getStack(findPearl))) {
                    if (mc.player.getInventory().indexOf(new ItemStack(Items.ENDER_PEARL)) != pearlSlot.getValueInt() - 1) {
                        mc.interactionManager.clickSlot(((InventoryScreen) mc.currentScreen).getScreenHandler().syncId, pearlSlot.getValueInt() - 1 + 36, findPearl, SlotActionType.SWAP, mc.player);
                        findPearl++;
                        return;
                    }
                } else if (findPearl >= 36) {
                    findPearl = 9;
                    return;
                } else {
                    findPearl++;
                    return;
                }
            }
        }
        super.onTick();
    }

    int findPearl = mc.player.getInventory().indexOf(new ItemStack(Items.ENDER_PEARL));

    private int findPot(int startSlot, int endSlot)
    {
        for(int i = startSlot; i < endSlot; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);

            if(stack.getItem() != Items.SPLASH_POTION)
                continue;

            if(hasEffect(stack, StatusEffects.INSTANT_HEALTH))
                return i;
        }

        return -1;
    }

    private boolean hasEffect(ItemStack stack, StatusEffect effect)
    {
        for(StatusEffectInstance effectInstance : PotionUtil.getPotionEffects(stack)) {
            if(effectInstance.getEffectType() != effect)
                continue;

            return true;
        }

        return false;
    }

}