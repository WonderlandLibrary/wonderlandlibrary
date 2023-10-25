/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils;

import baritone.Baritone;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class ToolSet {
    private final Map<Block, Double> breakStrengthCache = new HashMap<Block, Double>();
    private final Function<Block, Double> backendCalculation;
    private final EntityPlayerSP player;

    public ToolSet(EntityPlayerSP player) {
        this.player = player;
        if (((Boolean)Baritone.settings().considerPotionEffects.value).booleanValue()) {
            double amplifier = this.potionAmplifier();
            Function<Double, Double> amplify = x -> amplifier * x;
            this.backendCalculation = amplify.compose(this::getBestDestructionTime);
        } else {
            this.backendCalculation = this::getBestDestructionTime;
        }
    }

    public double getStrVsBlock(IBlockState state) {
        return this.breakStrengthCache.computeIfAbsent(state.getBlock(), this.backendCalculation);
    }

    private int getMaterialCost(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemTool) {
            ItemTool tool = (ItemTool)itemStack.getItem();
            return Item.ToolMaterial.valueOf(tool.getToolMaterialName()).ordinal();
        }
        return -1;
    }

    public boolean hasSilkTouch(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
    }

    public int getBestSlot(Block b, boolean preferSilkTouch) {
        return this.getBestSlot(b, preferSilkTouch, false);
    }

    public int getBestSlot(Block b, boolean preferSilkTouch, boolean pathingCalculation) {
        if (((Boolean)Baritone.settings().disableAutoTool.value).booleanValue() && pathingCalculation) {
            return this.player.inventory.currentItem;
        }
        int best = 0;
        double highestSpeed = Double.NEGATIVE_INFINITY;
        int lowestCost = Integer.MIN_VALUE;
        boolean bestSilkTouch = false;
        IBlockState blockState = b.getDefaultState();
        for (int i = 0; i < 9; ++i) {
            int cost;
            ItemStack itemStack = this.player.inventory.getStackInSlot(i);
            if (!((Boolean)Baritone.settings().useSwordToMine.value).booleanValue() && itemStack.getItem() instanceof ItemSword || ((Boolean)Baritone.settings().itemSaver.value).booleanValue() && itemStack.getItemDamage() >= itemStack.getMaxDamage() && itemStack.getMaxDamage() > 1) continue;
            double speed = ToolSet.calculateSpeedVsBlock(itemStack, blockState);
            boolean silkTouch = this.hasSilkTouch(itemStack);
            if (speed > highestSpeed) {
                highestSpeed = speed;
                best = i;
                lowestCost = this.getMaterialCost(itemStack);
                bestSilkTouch = silkTouch;
                continue;
            }
            if (speed != highestSpeed || ((cost = this.getMaterialCost(itemStack)) >= lowestCost || !silkTouch && bestSilkTouch) && (!preferSilkTouch || bestSilkTouch || !silkTouch)) continue;
            highestSpeed = speed;
            best = i;
            lowestCost = cost;
            bestSilkTouch = silkTouch;
        }
        return best;
    }

    private double getBestDestructionTime(Block b) {
        ItemStack stack = this.player.inventory.getStackInSlot(this.getBestSlot(b, false, true));
        return ToolSet.calculateSpeedVsBlock(stack, b.getDefaultState()) * this.avoidanceMultiplier(b);
    }

    private double avoidanceMultiplier(Block b) {
        return ((List)Baritone.settings().blocksToAvoidBreaking.value).contains(b) ? 0.1 : 1.0;
    }

    public static double calculateSpeedVsBlock(ItemStack item, IBlockState state) {
        int effLevel;
        float hardness = state.getBlockHardness(null, null);
        if (hardness < 0.0f) {
            return -1.0;
        }
        float speed = item.getDestroySpeed(state);
        if (speed > 1.0f && (effLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, item)) > 0 && !item.isEmpty()) {
            speed += (float)(effLevel * effLevel + 1);
        }
        speed /= hardness;
        if (state.getMaterial().isToolNotRequired() || !item.isEmpty() && item.canHarvestBlock(state)) {
            return speed / 30.0f;
        }
        return speed / 100.0f;
    }

    private double potionAmplifier() {
        double speed = 1.0;
        if (this.player.isPotionActive(MobEffects.HASTE)) {
            speed *= 1.0 + (double)(this.player.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2;
        }
        if (this.player.isPotionActive(MobEffects.MINING_FATIGUE)) {
            switch (this.player.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
                case 0: {
                    speed *= 0.3;
                    break;
                }
                case 1: {
                    speed *= 0.09;
                    break;
                }
                case 2: {
                    speed *= 0.0027;
                    break;
                }
                default: {
                    speed *= 8.1E-4;
                }
            }
        }
        return speed;
    }
}

