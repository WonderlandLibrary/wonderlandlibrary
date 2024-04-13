package net.smoothboot.client.module.misc;

import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.BooleanSetting;
import net.smoothboot.client.module.settings.NumberSetting;
import net.smoothboot.client.util.BlockUtil;

public class Swap_Macro extends Mod {

    public NumberSetting macroSlot = new NumberSetting("Slot", 1, 9, 9, 1);
    public BooleanSetting backToSlot = new BooleanSetting("Back to slot", true);
    public BooleanSetting macroRclick = new BooleanSetting("Click right", true);
    public Swap_Macro() {
        super("Swap Macro", "", Category.Misc);
        addsettings(macroSlot, backToSlot, macroRclick);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
            if (mc.currentScreen != null) {
                toggle();
                return;
            }
            int store = mc.player.getInventory().selectedSlot;
            mc.player.getInventory().selectedSlot = (macroSlot.getValueInt() - 1);
            if (macroRclick.isEnabled()) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                mc.player.swingHand(Hand.MAIN_HAND);
                final HitResult cr = mc.crosshairTarget;
                if (cr instanceof BlockHitResult hit && !placeCheck()) {
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            if (backToSlot.isEnabled())
                mc.player.getInventory().selectedSlot = store;
            super.onTick();
            toggle();
        }
    }

    public boolean placeCheck() {
        final HitResult cr = mc.crosshairTarget;
        if (cr instanceof BlockHitResult hit) {
            final BlockPos pos = hit.getBlockPos();
            return BlockUtil.isBlock(Blocks.AIR, pos) || BlockUtil.isBlock(Blocks.WATER, pos) || BlockUtil.isBlock(Blocks.LAVA, pos) ;
        }
        return false;
    }


}