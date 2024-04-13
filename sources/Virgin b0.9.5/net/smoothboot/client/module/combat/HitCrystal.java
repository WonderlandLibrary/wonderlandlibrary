package net.smoothboot.client.module.combat;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.mixin.MinecraftClientAccessor;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.BooleanSetting;
import net.smoothboot.client.module.settings.ModeSetting;
import net.smoothboot.client.util.BlockUtil;
import net.smoothboot.client.util.InventoryUtil;

public class HitCrystal extends Mod {

    String legit = "Legit";
    String blatant = "Blatant";

    public ModeSetting hitCrystalSpeed = new ModeSetting("Mode", legit, legit, blatant);
    public BooleanSetting hitCrystalMode = new BooleanSetting("Only Obsidian", false);

    public HitCrystal() {
        super("Hit Crystal", "Places obsidian and crystal when pressing right click", Category.Combat);
        addsettings(hitCrystalSpeed, hitCrystalMode);
    }

    PlayerInventory inv = mc.player.getInventory();

    @Override
    public void onTick() {
        if (mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            return;
        }
        if (swordCheck() || totemCheck()) {
            moved = false;
        }
        if (hitCrystalSpeed.isMode(blatant) && (obbyCheck() || crystalCheck())) {
            ((MinecraftClientAccessor) MinecraftClient.getInstance()).setItemUseCooldown(0);
        }
        if (!hitCrystalMode.isEnabled() && !moved && swordCheck() && rClickCheck() && mc.currentScreen == null) {
            moved = true;
            if (!placedObbyCheck())
                InventoryUtil.selectItemFromHotbar(Items.OBSIDIAN);
            else {
                moved = false;
                InventoryUtil.selectItemFromHotbar(Items.END_CRYSTAL);
            }
            return;
        }
        if (!hitCrystalMode.isEnabled() && moved && obbyCheck() && rClickCheck() && mc.currentScreen == null) {
            moved = false;
            InventoryUtil.selectItemFromHotbar(Items.END_CRYSTAL);
            return;
        }
        if (hitCrystalMode.isEnabled() && swordCheck() && mc.options.useKey.wasPressed() && mc.currentScreen == null) {
            InventoryUtil.selectItemFromHotbar(Items.OBSIDIAN);
        }
        super.onTick();
    }

    public boolean moved = false;

    public boolean rClickCheck() {
        return mc.options.useKey.isPressed();
    }

    public boolean swordCheck() {
        ItemStack getItem = mc.player.getMainHandStack();
        return (getItem.isOf(Items.NETHERITE_SWORD) || getItem.isOf(Items.DIAMOND_SWORD) || getItem.isOf(Items.GOLDEN_SWORD) || getItem.isOf(Items.IRON_SWORD) || getItem.isOf(Items.STONE_SWORD) || getItem.isOf(Items.WOODEN_SWORD));
    }

    public boolean totemCheck() {
        ItemStack getItem =mc.player.getMainHandStack();
        return (getItem.isOf(Items.TOTEM_OF_UNDYING));
    }

    public boolean obbyCheck() {
        ItemStack getItem = mc.player.getMainHandStack();
        return (getItem.isOf(Items.OBSIDIAN));
    }

    public boolean crystalCheck() {
        ItemStack getItem = mc.player.getMainHandStack();
        return (getItem.isOf(Items.END_CRYSTAL));
    }

    public boolean placedObbyCheck() {
        final HitResult cr = mc.crosshairTarget;
        if (cr instanceof BlockHitResult hit) {
        final BlockPos pos = hit.getBlockPos();
        return BlockUtil.isBlock(Blocks.OBSIDIAN, pos);
        }
        return false;
    }

}