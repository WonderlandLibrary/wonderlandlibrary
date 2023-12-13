/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.inventory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import tk.rektsky.Client;

public class InventoryUtils {
    public static final List<Block> invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.snow_layer, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.crafting_table, Blocks.furnace, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.brown_mushroom, Blocks.red_mushroom, Blocks.red_flower, Blocks.yellow_flower, Blocks.flower_pot, Blocks.web, Blocks.tnt);

    public static double getAttackDamage(ItemStack swordIn) {
        double damage = 0.0;
        Iterator<Map.Entry<String, AttributeModifier>> iterator = swordIn.getAttributeModifiers().entries().iterator();
        if (iterator.hasNext()) {
            Map.Entry<String, AttributeModifier> entry = iterator.next();
            AttributeModifier attributemodifier = entry.getValue();
            double d0 = attributemodifier.getAmount();
            if (Objects.equals(attributemodifier.getID(), UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF"))) {
                d0 += (double)EnchantmentHelper.func_152377_a(swordIn, EnumCreatureAttribute.UNDEFINED);
            }
            double d1 = attributemodifier.getOperation() != 1 && attributemodifier.getOperation() != 2 ? d0 : d0 * 100.0;
            return d1;
        }
        return 0.0;
    }

    public static boolean isBlock(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock)itemStack.getItem()).getBlock());
    }

    public static class DamageComparator
    implements Comparator<Slot> {
        @Override
        public int compare(Slot o1, Slot o2) {
            double second;
            double first = InventoryUtils.getAttackDamage(o1.getStack());
            if (first > (second = InventoryUtils.getAttackDamage(o2.getStack()))) {
                return 1;
            }
            if (first < second) {
                return -1;
            }
            return 0;
        }
    }

    public static class ArmorComparator
    implements Comparator<Slot> {
        @Override
        public int compare(Slot s1, Slot s2) {
            ItemStack first = s1.getStack();
            ItemStack second = s2.getStack();
            if (first.getItem() instanceof ItemArmor) {
                float reduceFirst = ((ItemArmor)first.getItem()).damageReduceAmount;
                if (first.isItemEnchanted()) {
                    NBTTagList enchantments = first.getEnchantmentTagList();
                    for (int i2 = 0; i2 < enchantments.tagCount(); ++i2) {
                        if (enchantments.getCompoundTagAt(i2).getInteger("id") != 0) continue;
                        EnchantmentProtection protection = (EnchantmentProtection)Enchantment.protection;
                        reduceFirst *= (float)protection.calcModifierDamage(enchantments.getCompoundTagAt(i2).getInteger("lvl"), DamageSource.causePlayerDamage(Client.mc.thePlayer));
                    }
                }
                float reduceSecond = ((ItemArmor)second.getItem()).damageReduceAmount;
                if (second.isItemEnchanted()) {
                    NBTTagList enchantments = second.getEnchantmentTagList();
                    for (int i3 = 0; i3 < enchantments.tagCount(); ++i3) {
                        if (enchantments.getCompoundTagAt(i3).getInteger("id") != 0) continue;
                        EnchantmentProtection protection = (EnchantmentProtection)Enchantment.protection;
                        reduceSecond *= (float)protection.calcModifierDamage(enchantments.getCompoundTagAt(i3).getInteger("lvl"), DamageSource.causePlayerDamage(Client.mc.thePlayer));
                    }
                }
                return Float.compare(reduceSecond, reduceFirst);
            }
            return 0;
        }
    }
}

