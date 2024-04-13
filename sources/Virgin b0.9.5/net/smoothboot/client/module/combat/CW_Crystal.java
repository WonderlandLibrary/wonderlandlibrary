package net.smoothboot.client.module.combat;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.mixin.MinecraftClientAccessor;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.BooleanSetting;
import net.smoothboot.client.module.settings.ModeSetting;
import net.smoothboot.client.module.settings.NumberSetting;
import net.smoothboot.client.util.BlockUtil;

public class CW_Crystal extends Mod {

    String legit = "Legit";
    String blatant = "Blatant";
    String adaptive = "Adaptive";
    String auto = "Auto";

    public NumberSetting cwSpeed = new NumberSetting("Delay", 0.00, 1.00, 0.40F, 0.01);
    public ModeSetting cwMode = new ModeSetting("Mode", legit, legit, blatant, adaptive, auto);
    public BooleanSetting workonair = new BooleanSetting("Air check", true);
    public BooleanSetting cwstoponkill = new BooleanSetting("Protect loot", true);

    public CW_Crystal() {
        super("CW Crystal", "Automatically places & breaks crystals", Category.Combat);
        addsettings(cwSpeed, cwMode, workonair, cwstoponkill);
    }

    private float delay = 0.1F;

    @Override
    public void onTick() {
        Entity target = getTarget();
        if (!mc.options.useKey.isPressed()) {
            delay = 0.1F;
        }
        if (nullCheck()) {
            return;
        }
        if (killCheck() && cwstoponkill.isEnabled()) {
            return;
        }
        if (workonair.isEnabled() && !mc.player.isOnGround()) {
            return;
        }
        if ((cwMode.isMode(blatant) || cwMode.isMode(adaptive)) && mc.player.getMainHandStack().isOf(Items.END_CRYSTAL) && mc.options.useKey.isPressed() && mc.currentScreen == null) {
            ((MinecraftClientAccessor) MinecraftClient.getInstance()).setItemUseCooldown(0);
        }
        if (cwMode.isMode(auto) && mc.player.getMainHandStack().isOf(Items.END_CRYSTAL) && mc.currentScreen == null) {
            final HitResult cr = mc.crosshairTarget;
            if (cr instanceof BlockHitResult hit && placedObbyCheck()) {
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
            if (isCrystal(target) && mc.player.getAttackCooldownProgress(0) >= cwSpeed.getValue()) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
        if (!cwMode.isMode(adaptive) && isCrystal(target) && mc.player.getMainHandStack().isOf(Items.END_CRYSTAL) && mc.options.useKey.isPressed() && mc.currentScreen == null) {
            if (mc.player.getAttackCooldownProgress(0) >= cwSpeed.getValue()) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
        if (cwMode.isMode(adaptive) && isCrystal(target) && mc.player.getMainHandStack().isOf(Items.END_CRYSTAL) && mc.options.useKey.isPressed() && mc.currentScreen == null) {
            if (mc.player.getAttackCooldownProgress(0) >= delay) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
                if (delay == 0.1F) {
                    delay = 0.40F;
                }
                delay+=0.05F;
                if (delay >= 0.9) {
                    delay = 0.1F;
                }
                else {
                    return;
                }

            }
        }
        super.onTick();
    }

    private boolean killCheck() {
    if (ItemNearby(mc.player, 8)) {
        return true;
    }
        else {
        return false;
        }
    }

    private boolean ItemNearby(Entity entity, double range) {
        Box boundingBox = new Box(
                entity.getPos().x - 12,
                entity.getPos().y - 12,
                entity.getPos().z - 12,
                entity.getPos().x + 12,
                entity.getPos().y + 12,
                entity.getPos().z + 12
        );

        for (Entity nearbyEntity : mc.world.getOtherEntities(null, boundingBox)) {
            if (nearbyEntity instanceof ItemEntity) {
                if (isLoot(((ItemEntity) nearbyEntity).getStack().getItem())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isLoot(Item item) {
        return
                item == Items.DIAMOND_SWORD ||
                item == Items.DIAMOND_PICKAXE ||
                item == Items.DIAMOND_HELMET ||
                item == Items.DIAMOND_CHESTPLATE ||
                item == Items.DIAMOND_LEGGINGS ||
                item == Items.DIAMOND_BOOTS ||
                item == Items.NETHERITE_SWORD ||
                item == Items.NETHERITE_PICKAXE ||
                item == Items.NETHERITE_HELMET ||
                item == Items.NETHERITE_CHESTPLATE ||
                item == Items.NETHERITE_LEGGINGS ||
                item == Items.NETHERITE_BOOTS ||
                item == Items.ELYTRA;
    }

    private Entity getTarget() {
        HitResult hitResult = mc.crosshairTarget;

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            return entityHitResult.getEntity();
        }

        return null;
    }

    private boolean isCrystal(Entity entity) {
        return (entity instanceof EndCrystalEntity);
    }

    public boolean placedObbyCheck() {
        final HitResult cr = mc.crosshairTarget;
        if (cr instanceof BlockHitResult hit) {
            final BlockPos pos = hit.getBlockPos();
            return BlockUtil.isBlock(Blocks.OBSIDIAN, pos) || BlockUtil.isBlock(Blocks.BEDROCK, pos) ;
        }
        return false;
    }

}