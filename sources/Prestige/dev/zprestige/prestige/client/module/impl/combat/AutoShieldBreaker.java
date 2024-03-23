package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.MoveEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoShieldBreaker extends Module {

    public AutoShieldBreaker() {
        super("Auto Shield Breaker", Category.Combat, "Automatically breaks shields by silently attacking them");
    }

    @EventListener
    public void event(MoveEvent event) {
        if (getMc().targetedEntity != null && usingShield()) {
            InventoryUtil.INSTANCE.setCurrentSlot(getAxe());
            getMc().interactionManager.attackEntity(getMc().player, getMc().targetedEntity);
            getMc().player.swingHand(Hand.MAIN_HAND);
        }
    }

   private int getAxe() {
        for (int i = 0; i < 9; ++i) {
            if (getMc().player.getInventory().getStack(i).getItem() instanceof AxeItem) {
                return i;
            }
        }
        return -1;
    }

    private boolean usingShield() {
        return getMc().targetedEntity instanceof PlayerEntity player && player.isUsingItem() && player.getActiveItem().getItem() == Items.SHIELD;
    }
}