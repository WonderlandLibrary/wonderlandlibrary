/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import com.google.common.collect.Maps;
import java.util.Iterator;
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
    private Map<ItemStack, ItemStack> smeltingList = Maps.newHashMap();
    private Map<ItemStack, Float> experienceList = Maps.newHashMap();

    public static FurnaceRecipes instance() {
        return smeltingBase;
    }

    private FurnaceRecipes() {
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
        while (true) {
            if (n2 >= n) {
                this.addSmeltingRecipeForBlock(Blocks.coal_ore, new ItemStack(Items.coal), 0.1f);
                this.addSmeltingRecipeForBlock(Blocks.redstone_ore, new ItemStack(Items.redstone), 0.7f);
                this.addSmeltingRecipeForBlock(Blocks.lapis_ore, new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), 0.2f);
                this.addSmeltingRecipeForBlock(Blocks.quartz_ore, new ItemStack(Items.quartz), 0.2f);
                return;
            }
            ItemFishFood.FishType itemfishfood$fishtype = fishTypeArray[n2];
            if (itemfishfood$fishtype.canCook()) {
                this.addSmeltingRecipe(new ItemStack(Items.fish, 1, itemfishfood$fishtype.getMetadata()), new ItemStack(Items.cooked_fish, 1, itemfishfood$fishtype.getMetadata()), 0.35f);
            }
            ++n2;
        }
    }

    public void addSmeltingRecipeForBlock(Block input, ItemStack stack, float experience) {
        this.addSmelting(Item.getItemFromBlock(input), stack, experience);
    }

    public void addSmelting(Item input, ItemStack stack, float experience) {
        this.addSmeltingRecipe(new ItemStack(input, 1, Short.MAX_VALUE), stack, experience);
    }

    public void addSmeltingRecipe(ItemStack input, ItemStack stack, float experience) {
        this.smeltingList.put(input, stack);
        this.experienceList.put(stack, Float.valueOf(experience));
    }

    public ItemStack getSmeltingResult(ItemStack stack) {
        Map.Entry<ItemStack, ItemStack> entry;
        Iterator<Map.Entry<ItemStack, ItemStack>> iterator = this.smeltingList.entrySet().iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while (!this.compareItemStacks(stack, (entry = iterator.next()).getKey()));
        return entry.getValue();
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        if (stack2.getItem() != stack1.getItem()) return false;
        if (stack2.getMetadata() == Short.MAX_VALUE) return true;
        if (stack2.getMetadata() != stack1.getMetadata()) return false;
        return true;
    }

    public Map<ItemStack, ItemStack> getSmeltingList() {
        return this.smeltingList;
    }

    public float getSmeltingExperience(ItemStack stack) {
        Map.Entry<ItemStack, Float> entry;
        Iterator<Map.Entry<ItemStack, Float>> iterator = this.experienceList.entrySet().iterator();
        do {
            if (!iterator.hasNext()) return 0.0f;
        } while (!this.compareItemStacks(stack, (entry = iterator.next()).getKey()));
        return entry.getValue().floatValue();
    }
}

