package ru.smertnix.celestial.feature.impl.misc;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.events.Event;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;

public class ElytraFix extends Feature {

    public static long delay;

    public ElytraFix() {
        super("Elytra Swap", "Автоматически свапает элитру", FeatureCategory.Util);
    }

    public void onEvent(Event event) {
        if (event instanceof EventUpdate && !Celestial.instance.featureManager.getFeature(ElytraFix.class).isEnabled()) {
            ItemStack stack = mc.player.inventory.getItemStack();
            if (stack != null && stack.getItem() instanceof ItemArmor && System.currentTimeMillis() > delay) {
                ItemArmor ia = (ItemArmor) stack.getItem();
                if (ia.armorType == EntityEquipmentSlot.CHEST
                        && mc.player.inventory.armorItemInSlot(2).getItem() == Items.ELYTRA) {
                    mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                    int nullSlot = findNullSlot();
                    boolean needDrop = nullSlot == 999;
                    if (needDrop) {
                        nullSlot = 9;
                    }
                    mc.playerController.windowClick(0, nullSlot, 1, ClickType.PICKUP, mc.player);
                    if (needDrop) {
                        mc.playerController.windowClick(0, -999, 1, ClickType.PICKUP, mc.player);
                    }
                    delay = System.currentTimeMillis() + 300;
                }
            }
        }
    }

    public static int findNullSlot() {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemAir) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return 999;
    }
}
