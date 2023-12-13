package koks.api.ncp;

import koks.api.Methods;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */
public class NCP implements Methods {

    double hAllowedDistanceBase;
    double nextFrictionHorizontal;

    public static NCP instance;

    public final double[] modDepthStrider = new double[]{
            1.0,
            0.1645 / (0.115 / 0.221) / 0.221,
            0.1995 / (0.115 / 0.221) / 0.221,
            1.0 / (0.115 / 0.221),
    };

    private int getMaxLevelArmor(final EntityPlayer player, final Enchantment enchantment) {
        if (enchantment == null) {
            return 0;
        }
        int level = 0;
        final ItemStack[] armor = player.inventory.armorInventory;
        for (int i = 0; i < armor.length; i++) {
            final ItemStack item = armor[i];
            if (item != null) {
                int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(enchantment.effectId, item);
                level = Math.max(enchantmentLevel, level);
            }
        }
        return level;
    }

    public double hDistDiff() {
        return Math.sqrt(getPlayer().motionX * getPlayer().motionX + getPlayer().motionZ * getPlayer().motionZ);
    }

    public int getDepthStriderLevel(final EntityPlayer player) {
        return Math.min(3, getMaxLevelArmor(player, Enchantment.depthStrider));
    }

    public double getAllowedHDist() {
        double hAllowedDistance = 0D;

        final boolean sfDirty = getHurtTime() != 0 && getPlayer().isAirBorne;
        double friction = 0;
        boolean useBaseModifiers = false;
        if (getPlayer().isInWeb) {
            hAllowedDistance = (0.105 / 0.221) * getPlayer().capabilities.getWalkSpeed() * 100 / 100D;
            friction = 0.0;
        } else if ((getPlayer().isInWater() || getPlayer().isInLava())) {
            hAllowedDistance = (0.115 / 0.221) * 0.221 * 100 / 100D;
            if (getPlayer().isInWater() || !getPlayer().isInLava()) {
                final int level = getDepthStriderLevel(getPlayer());
                if (level > 0) {
                    hAllowedDistance *= modDepthStrider[level];
                    useBaseModifiers = true;
                }
            }
        } else if (!sfDirty && getPlayer().onGround && getPlayer().isSneaking()) {
            hAllowedDistance = (0.13 / 0.221) * getPlayer().capabilities.getWalkSpeed() * 100 / 100D;
            friction = 0.0;
        } else if (!sfDirty && getPlayer().onGround && getPlayer().isBlocking()) {
            hAllowedDistance = (0.16 / 0.221) * getPlayer().capabilities.getWalkSpeed() * 100 / 100D;
            friction = 0.0;
        } else {
            useBaseModifiers = true;
            if (getPlayer().isSprinting()) {
                hAllowedDistance = getPlayer().capabilities.getWalkSpeed() * 100 / 100D;
            } else {
                hAllowedDistance = getPlayer().capabilities.getWalkSpeed() * 100 / 100D;
            }
            friction = 0.0;
        }
        if (useBaseModifiers) {
            if (getPlayer().isSprinting()) {
                hAllowedDistance *= 1.30000002;
            }
            final double speedAmplifier = Double.MAX_VALUE;
            if (!Double.isInfinite(speedAmplifier)) {
                hAllowedDistance *= 1.0D + 0.2D * (speedAmplifier + 1);
            }
        }

        if (getPlayer().isInWater() && getPlayer().motionY < 0) {
            hAllowedDistance *= 0.19 / (0.221 * (0.115D / 0.221));
        }

        if (getBlockUnderPlayer(0.1F) instanceof BlockPackedIce || getBlockUnderPlayer(0.1F) instanceof BlockIce) {
            hAllowedDistance *= 2.5;
        }

        this.hAllowedDistanceBase = hAllowedDistance;

        double x = Math.abs(getX() - getPlayer().lastTickPosX);
        double z = Math.abs(getZ() - getPlayer().lastTickPosZ);

        if (Math.sqrt(x * x + z * z) <= hAllowedDistance) {
            nextFrictionHorizontal = 1.0;
        }
        return hAllowedDistance;
    }

    public static NCP getInstance() {
        if(instance == null)
            instance = new NCP();
        return instance;
    }

}
