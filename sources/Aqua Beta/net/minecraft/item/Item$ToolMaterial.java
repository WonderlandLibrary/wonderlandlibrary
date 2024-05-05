package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/*
 * Exception performing whole class analysis ignored.
 */
public static enum Item.ToolMaterial {
    WOOD(0, 59, 2.0f, 0.0f, 15),
    STONE(1, 131, 4.0f, 1.0f, 5),
    IRON(2, 250, 6.0f, 2.0f, 14),
    EMERALD(3, 1561, 8.0f, 3.0f, 10),
    GOLD(0, 32, 12.0f, 0.0f, 22);

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiencyOnProperMaterial;
    private final float damageVsEntity;
    private final int enchantability;

    private Item.ToolMaterial(int harvestLevel, int maxUses, float efficiency, float damageVsEntity, int enchantability) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiencyOnProperMaterial = efficiency;
        this.damageVsEntity = damageVsEntity;
        this.enchantability = enchantability;
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public float getEfficiencyOnProperMaterial() {
        return this.efficiencyOnProperMaterial;
    }

    public float getDamageVsEntity() {
        return this.damageVsEntity;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Item getRepairItem() {
        return this == WOOD ? Item.getItemFromBlock((Block)Blocks.planks) : (this == STONE ? Item.getItemFromBlock((Block)Blocks.cobblestone) : (this == GOLD ? Items.gold_ingot : (this == IRON ? Items.iron_ingot : (this == EMERALD ? Items.diamond : null))));
    }
}
