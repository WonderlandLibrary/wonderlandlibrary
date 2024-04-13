package net.smoothboot.client.module.combat;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.mixin.MinecraftClientAccessor;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.ModeSetting;
import net.smoothboot.client.module.settings.NumberSetting;
import net.smoothboot.client.util.BlockUtil;
import net.smoothboot.client.util.InventoryUtil;

public class Anchor_Macro extends Mod {

    String legit = "Legit";
    String blatant = "Blatant";

    public NumberSetting anchorBackSlot = new NumberSetting("Back to slot", 1, 9, 9, 1);
    public ModeSetting anchorMode = new ModeSetting("Mode", legit, legit, blatant);
    public Anchor_Macro() {
        super("Anchor Macro", "", Category.Combat);
        addsettings(anchorBackSlot, anchorMode);
    }
    private boolean charged;

    @Override
    public void onTick() {
        if (mc.player.isUsingItem()) {
            return;
        }
        if ((placedAnchor() && isGlowstone()) && anchorMode.isMode(blatant)) ((MinecraftClientAccessor) MinecraftClient.getInstance()).setItemUseCooldown(0);
        if (charged) {
            final HitResult target = mc.crosshairTarget;
            if (target instanceof BlockHitResult hit) {
                final BlockPos pos = hit.getBlockPos();
                if (BlockUtil.isAnchorCharged(pos) && placedAnchor()) {
                    mc.player.getInventory().selectedSlot = (anchorBackSlot.getValueInt() - 1);
                    charged = false;
                }
            }
        }
        if (!rClickCheck()) {
            return;
        }
        if (placedAnchor()) {
            final HitResult target = mc.crosshairTarget;
            if (target instanceof BlockHitResult hit) {
                final BlockPos pos = hit.getBlockPos();
                if (!BlockUtil.isAnchorCharged(pos)) {
                    InventoryUtil.selectItemFromHotbar(Items.GLOWSTONE);
                    charged = true;
                    return;
                }
            }
        }


        super.onTick();
    }

    public boolean rClickCheck() {
        return mc.options.useKey.isPressed();
    }

    private boolean placedAnchor() {
        final HitResult cr = mc.crosshairTarget;
        if (cr instanceof BlockHitResult hit) {
            final BlockPos pos = hit.getBlockPos();
            return BlockUtil.isBlock(Blocks.RESPAWN_ANCHOR, pos);
        }
        return false;
    }

    public boolean isAnchor() {
        ItemStack getItem = mc.player.getMainHandStack();
        return (getItem.isOf(Items.RESPAWN_ANCHOR));
    }

    public boolean isGlowstone() {
        ItemStack getItem = mc.player.getMainHandStack();
        return (getItem.isOf(Items.GLOWSTONE));
    }

}