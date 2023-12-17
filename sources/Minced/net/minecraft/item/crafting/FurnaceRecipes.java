// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import java.util.Map;

public class FurnaceRecipes
{
    private static final FurnaceRecipes SMELTING_BASE;
    private final Map<ItemStack, ItemStack> smeltingList;
    private final Map<ItemStack, Float> experienceList;
    
    public static FurnaceRecipes instance() {
        return FurnaceRecipes.SMELTING_BASE;
    }
    
    private FurnaceRecipes() {
        this.smeltingList = (Map<ItemStack, ItemStack>)Maps.newHashMap();
        this.experienceList = (Map<ItemStack, Float>)Maps.newHashMap();
        this.addSmeltingRecipeForBlock(Blocks.IRON_ORE, new ItemStack(Items.IRON_INGOT), 0.7f);
        this.addSmeltingRecipeForBlock(Blocks.GOLD_ORE, new ItemStack(Items.GOLD_INGOT), 1.0f);
        this.addSmeltingRecipeForBlock(Blocks.DIAMOND_ORE, new ItemStack(Items.DIAMOND), 1.0f);
        this.addSmeltingRecipeForBlock(Blocks.SAND, new ItemStack(Blocks.GLASS), 0.1f);
        this.addSmelting(Items.PORKCHOP, new ItemStack(Items.COOKED_PORKCHOP), 0.35f);
        this.addSmelting(Items.BEEF, new ItemStack(Items.COOKED_BEEF), 0.35f);
        this.addSmelting(Items.CHICKEN, new ItemStack(Items.COOKED_CHICKEN), 0.35f);
        this.addSmelting(Items.RABBIT, new ItemStack(Items.COOKED_RABBIT), 0.35f);
        this.addSmelting(Items.MUTTON, new ItemStack(Items.COOKED_MUTTON), 0.35f);
        this.addSmeltingRecipeForBlock(Blocks.COBBLESTONE, new ItemStack(Blocks.STONE), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.DEFAULT_META), new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.CRACKED_META), 0.1f);
        this.addSmelting(Items.CLAY_BALL, new ItemStack(Items.BRICK), 0.3f);
        this.addSmeltingRecipeForBlock(Blocks.CLAY, new ItemStack(Blocks.HARDENED_CLAY), 0.35f);
        this.addSmeltingRecipeForBlock(Blocks.CACTUS, new ItemStack(Items.DYE, 1, EnumDyeColor.GREEN.getDyeDamage()), 0.2f);
        this.addSmeltingRecipeForBlock(Blocks.LOG, new ItemStack(Items.COAL, 1, 1), 0.15f);
        this.addSmeltingRecipeForBlock(Blocks.LOG2, new ItemStack(Items.COAL, 1, 1), 0.15f);
        this.addSmeltingRecipeForBlock(Blocks.EMERALD_ORE, new ItemStack(Items.EMERALD), 1.0f);
        this.addSmelting(Items.POTATO, new ItemStack(Items.BAKED_POTATO), 0.35f);
        this.addSmeltingRecipeForBlock(Blocks.NETHERRACK, new ItemStack(Items.NETHERBRICK), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.SPONGE, 1, 1), new ItemStack(Blocks.SPONGE, 1, 0), 0.15f);
        this.addSmelting(Items.CHORUS_FRUIT, new ItemStack(Items.CHORUS_FRUIT_POPPED), 0.1f);
        for (final ItemFishFood.FishType itemfishfood$fishtype : ItemFishFood.FishType.values()) {
            if (itemfishfood$fishtype.canCook()) {
                this.addSmeltingRecipe(new ItemStack(Items.FISH, 1, itemfishfood$fishtype.getMetadata()), new ItemStack(Items.COOKED_FISH, 1, itemfishfood$fishtype.getMetadata()), 0.35f);
            }
        }
        this.addSmeltingRecipeForBlock(Blocks.COAL_ORE, new ItemStack(Items.COAL), 0.1f);
        this.addSmeltingRecipeForBlock(Blocks.REDSTONE_ORE, new ItemStack(Items.REDSTONE), 0.7f);
        this.addSmeltingRecipeForBlock(Blocks.LAPIS_ORE, new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), 0.2f);
        this.addSmeltingRecipeForBlock(Blocks.QUARTZ_ORE, new ItemStack(Items.QUARTZ), 0.2f);
        this.addSmelting(Items.CHAINMAIL_HELMET, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.CHAINMAIL_CHESTPLATE, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.CHAINMAIL_LEGGINGS, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.CHAINMAIL_BOOTS, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.IRON_PICKAXE, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.IRON_SHOVEL, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.IRON_AXE, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.IRON_HOE, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.IRON_SWORD, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.IRON_HELMET, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.IRON_CHESTPLATE, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.IRON_LEGGINGS, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.IRON_BOOTS, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.IRON_HORSE_ARMOR, new ItemStack(Items.IRON_NUGGET), 0.1f);
        this.addSmelting(Items.GOLDEN_PICKAXE, new ItemStack(Items.GOLD_NUGGET), 0.1f);
        this.addSmelting(Items.GOLDEN_SHOVEL, new ItemStack(Items.GOLD_NUGGET), 0.1f);
        this.addSmelting(Items.GOLDEN_AXE, new ItemStack(Items.GOLD_NUGGET), 0.1f);
        this.addSmelting(Items.GOLDEN_HOE, new ItemStack(Items.GOLD_NUGGET), 0.1f);
        this.addSmelting(Items.GOLDEN_SWORD, new ItemStack(Items.GOLD_NUGGET), 0.1f);
        this.addSmelting(Items.GOLDEN_HELMET, new ItemStack(Items.GOLD_NUGGET), 0.1f);
        this.addSmelting(Items.GOLDEN_CHESTPLATE, new ItemStack(Items.GOLD_NUGGET), 0.1f);
        this.addSmelting(Items.GOLDEN_LEGGINGS, new ItemStack(Items.GOLD_NUGGET), 0.1f);
        this.addSmelting(Items.GOLDEN_BOOTS, new ItemStack(Items.GOLD_NUGGET), 0.1f);
        this.addSmelting(Items.GOLDEN_HORSE_ARMOR, new ItemStack(Items.GOLD_NUGGET), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.WHITE.getMetadata()), new ItemStack(Blocks.WHITE_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.ORANGE.getMetadata()), new ItemStack(Blocks.ORANGE_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.MAGENTA.getMetadata()), new ItemStack(Blocks.MAGENTA_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.LIGHT_BLUE.getMetadata()), new ItemStack(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.YELLOW.getMetadata()), new ItemStack(Blocks.YELLOW_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.LIME.getMetadata()), new ItemStack(Blocks.LIME_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.PINK.getMetadata()), new ItemStack(Blocks.PINK_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.GRAY.getMetadata()), new ItemStack(Blocks.GRAY_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.SILVER.getMetadata()), new ItemStack(Blocks.SILVER_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.CYAN.getMetadata()), new ItemStack(Blocks.CYAN_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.PURPLE.getMetadata()), new ItemStack(Blocks.PURPLE_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BLUE.getMetadata()), new ItemStack(Blocks.BLUE_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BROWN.getMetadata()), new ItemStack(Blocks.BROWN_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.GREEN.getMetadata()), new ItemStack(Blocks.GREEN_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.RED.getMetadata()), new ItemStack(Blocks.RED_GLAZED_TERRACOTTA), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BLACK.getMetadata()), new ItemStack(Blocks.BLACK_GLAZED_TERRACOTTA), 0.1f);
    }
    
    public void addSmeltingRecipeForBlock(final Block input, final ItemStack stack, final float experience) {
        this.addSmelting(Item.getItemFromBlock(input), stack, experience);
    }
    
    public void addSmelting(final Item input, final ItemStack stack, final float experience) {
        this.addSmeltingRecipe(new ItemStack(input, 1, 32767), stack, experience);
    }
    
    public void addSmeltingRecipe(final ItemStack input, final ItemStack stack, final float experience) {
        this.smeltingList.put(input, stack);
        this.experienceList.put(stack, experience);
    }
    
    public ItemStack getSmeltingResult(final ItemStack stack) {
        for (final Map.Entry<ItemStack, ItemStack> entry : this.smeltingList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue();
            }
        }
        return ItemStack.EMPTY;
    }
    
    private boolean compareItemStacks(final ItemStack stack1, final ItemStack stack2) {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }
    
    public Map<ItemStack, ItemStack> getSmeltingList() {
        return this.smeltingList;
    }
    
    public float getSmeltingExperience(final ItemStack stack) {
        for (final Map.Entry<ItemStack, Float> entry : this.experienceList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0.0f;
    }
    
    static {
        SMELTING_BASE = new FurnaceRecipes();
    }
}
