package net.smoothboot.client.module.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.util.InventoryUtil;

public class AutoStun extends Mod {

    public AutoStun() {
        super("Auto Stun", "Automatically breaks shields with axe", Category.Combat);
    }
    @Override
    public void onTick() {
        if(mc.player ==null || mc.targetedEntity == null){
            return;
        }
        if(targetUsingSheild()){
            InventoryUtil.selectItemFromHotbar(Items.NETHERITE_AXE);
            InventoryUtil.selectItemFromHotbar(Items.DIAMOND_AXE);
            InventoryUtil.selectItemFromHotbar(Items.IRON_AXE);
            InventoryUtil.selectItemFromHotbar(Items.STONE_AXE);
            InventoryUtil.selectItemFromHotbar(Items.GOLDEN_AXE);
            InventoryUtil.selectItemFromHotbar(Items.WOODEN_AXE);
            if (!axeCheck()) {
                return;
            }
            if (mc.player.getAttackCooldownProgress(0) >= 0.25) {
                mc.interactionManager.attackEntity(mc.player,mc.targetedEntity);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    public boolean axeCheck() {
        ItemStack getItem = mc.player.getMainHandStack();
        return (getItem.isOf(Items.NETHERITE_AXE) || getItem.isOf(Items.DIAMOND_AXE) || getItem.isOf(Items.GOLDEN_AXE) || getItem.isOf(Items.IRON_AXE) || getItem.isOf(Items.STONE_AXE) || getItem.isOf(Items.WOODEN_AXE));
    }


    public boolean targetUsingSheild(){
        Entity target = mc.targetedEntity;
        if(target instanceof PlayerEntity && ((PlayerEntity) target).isUsingItem() && ((PlayerEntity) target).getActiveItem().getItem() == Items.SHIELD){
            return true;
        }
        return false;
    }


}
