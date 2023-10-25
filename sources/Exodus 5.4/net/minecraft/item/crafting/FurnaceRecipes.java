/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.item.crafting;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;

public class FurnaceRecipes {
    private static final FurnaceRecipes smeltingBase = new FurnaceRecipes();
    private Map<ItemStack, Float> experienceList;
    private Map<ItemStack, ItemStack> smeltingList = Maps.newHashMap();

    public float getSmeltingExperience(ItemStack itemStack) {
        for (Map.Entry<ItemStack, Float> entry : this.experienceList.entrySet()) {
            if (!this.compareItemStacks(itemStack, entry.getKey())) continue;
            return entry.getValue().floatValue();
        }
        return 0.0f;
    }

    private boolean compareItemStacks(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack2.getItem() == itemStack.getItem() && (itemStack2.getMetadata() == Short.MAX_VALUE || itemStack2.getMetadata() == itemStack.getMetadata());
    }

    private FurnaceRecipes() {
        this.experienceList = Maps.newHashMap();
        this.addSmeltingRecipeForBlock(Blocks.iron_ore, new ItemStack(Items.iron_ingot), 0.7f);
        this.addSmeltingRecipeForBlock(Blocks.gold_ore, new ItemStack(Items.gold_ingot), 1.0f);
        this.addSmeltingRecipeForBlock(Blocks.diamond_ore, new ItemStack(Items.diamond), 1.0f);
        this.addSmeltingRecipeForBlock(Blocks.sand, new ItemStack(Blocks.glass), 0.1f);
        this.addSmelting(Items.porkchop, new ItemStack(Items.cooked_porkchop), 0.35f);
        this.addSmelting(Items.beef, new ItemStack(Items.cooked_beef), 0.35f);
        this.addSmelting(Items.chicken, new ItemStack(Items.cooked_chicken), 0.35f);
        this.addSmelting(Items.rabbit, new ItemStack(Items.cooked_rabbit), 0.35f);
        this.addSmelting(Items.mutton, new ItemStack(Items.cooked_mutton), 0.35f);
        this.addSmeltingRecipeForBlock(Blocks.cobblestone, new ItemStack(Blocks.stone), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.DEFAULT_META), new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.CRACKED_META), 0.1f);
        this.addSmelting(Items.clay_ball, new ItemStack(Items.brick), 0.3f);
        this.addSmeltingRecipeForBlock(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.35f);
        this.addSmeltingRecipeForBlock(Blocks.cactus, new ItemStack(Items.dye, 1, EnumDyeColor.GREEN.getDyeDamage()), 0.2f);
        this.addSmeltingRecipeForBlock(Blocks.log, new ItemStack(Items.coal, 1, 1), 0.15f);
        this.addSmeltingRecipeForBlock(Blocks.log2, new ItemStack(Items.coal, 1, 1), 0.15f);
        this.addSmeltingRecipeForBlock(Blocks.emerald_ore, new ItemStack(Items.emerald), 1.0f);
        this.addSmelting(Items.potato, new ItemStack(Items.baked_potato), 0.35f);
        this.addSmeltingRecipeForBlock(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.sponge, 1, 1), new ItemStack(Blocks.sponge, 1, 0), 0.15f);
        ItemFishFood.FishType[] fishTypeArray = ItemFishFood.FishType.values();
        int n = fishTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            ItemFishFood.FishType fishType = fishTypeArray[n2];
            if (fishType.canCook()) {
                this.addSmeltingRecipe(new ItemStack(Items.fish, 1, fishType.getMetadata()), new ItemStack(Items.cooked_fish, 1, fishType.getMetadata()), 0.35f);
            }
            ++n2;
        }
        this.addSmeltingRecipeForBlock(Blocks.coal_ore, new ItemStack(Items.coal), 0.1f);
        this.addSmeltingRecipeForBlock(Blocks.redstone_ore, new ItemStack(Items.redstone), 0.7f);
        this.addSmeltingRecipeForBlock(Blocks.lapis_ore, new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), 0.2f);
        this.addSmeltingRecipeForBlock(Blocks.quartz_ore, new ItemStack(Items.quartz), 0.2f);
    }

    public void addSmeltingRecipeForBlock(Block block, ItemStack itemStack, float f) {
        this.addSmelting(Item.getItemFromBlock(block), itemStack, f);
    }

    public void addSmelting(Item item, ItemStack itemStack, float f) {
        this.addSmeltingRecipe(new ItemStack(item, 1, Short.MAX_VALUE), itemStack, f);
    }

    public void addSmeltingRecipe(ItemStack itemStack, ItemStack itemStack2, float f) {
        this.smeltingList.put(itemStack, itemStack2);
        this.experienceList.put(itemStack2, Float.valueOf(f));
    }

    public Map<ItemStack, ItemStack> getSmeltingList() {
        return this.smeltingList;
    }

    public static FurnaceRecipes instance() {
        return smeltingBase;
    }

    public ItemStack getSmeltingResult(ItemStack itemStack) {
        for (Map.Entry<ItemStack, ItemStack> entry : this.smeltingList.entrySet()) {
            if (!this.compareItemStacks(itemStack, entry.getKey())) continue;
            return entry.getValue();
        }
        return null;
    }
}

