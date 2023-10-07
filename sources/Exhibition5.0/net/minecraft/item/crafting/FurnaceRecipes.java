// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item.crafting;

import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import com.google.common.collect.Maps;
import java.util.Map;

public class FurnaceRecipes
{
    private static final FurnaceRecipes smeltingBase;
    private Map smeltingList;
    private Map experienceList;
    private static final String __OBFID = "CL_00000085";
    
    public static FurnaceRecipes instance() {
        return FurnaceRecipes.smeltingBase;
    }
    
    private FurnaceRecipes() {
        this.smeltingList = Maps.newHashMap();
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
        this.addSmeltingRecipeForBlock(Blocks.cactus, new ItemStack(Items.dye, 1, EnumDyeColor.GREEN.getDyeColorDamage()), 0.2f);
        this.addSmeltingRecipeForBlock(Blocks.log, new ItemStack(Items.coal, 1, 1), 0.15f);
        this.addSmeltingRecipeForBlock(Blocks.log2, new ItemStack(Items.coal, 1, 1), 0.15f);
        this.addSmeltingRecipeForBlock(Blocks.emerald_ore, new ItemStack(Items.emerald), 1.0f);
        this.addSmelting(Items.potato, new ItemStack(Items.baked_potato), 0.35f);
        this.addSmeltingRecipeForBlock(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.sponge, 1, 1), new ItemStack(Blocks.sponge, 1, 0), 0.15f);
        for (final ItemFishFood.FishType var4 : ItemFishFood.FishType.values()) {
            if (var4.getCookable()) {
                this.addSmeltingRecipe(new ItemStack(Items.fish, 1, var4.getItemDamage()), new ItemStack(Items.cooked_fish, 1, var4.getItemDamage()), 0.35f);
            }
        }
        this.addSmeltingRecipeForBlock(Blocks.coal_ore, new ItemStack(Items.coal), 0.1f);
        this.addSmeltingRecipeForBlock(Blocks.redstone_ore, new ItemStack(Items.redstone), 0.7f);
        this.addSmeltingRecipeForBlock(Blocks.lapis_ore, new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeColorDamage()), 0.2f);
        this.addSmeltingRecipeForBlock(Blocks.quartz_ore, new ItemStack(Items.quartz), 0.2f);
    }
    
    public void addSmeltingRecipeForBlock(final Block p_151393_1_, final ItemStack p_151393_2_, final float p_151393_3_) {
        this.addSmelting(Item.getItemFromBlock(p_151393_1_), p_151393_2_, p_151393_3_);
    }
    
    public void addSmelting(final Item p_151396_1_, final ItemStack p_151396_2_, final float p_151396_3_) {
        this.addSmeltingRecipe(new ItemStack(p_151396_1_, 1, 32767), p_151396_2_, p_151396_3_);
    }
    
    public void addSmeltingRecipe(final ItemStack p_151394_1_, final ItemStack p_151394_2_, final float p_151394_3_) {
        this.smeltingList.put(p_151394_1_, p_151394_2_);
        this.experienceList.put(p_151394_2_, p_151394_3_);
    }
    
    public ItemStack getSmeltingResult(final ItemStack p_151395_1_) {
        for (final Map.Entry var3 : this.smeltingList.entrySet()) {
            if (this.func_151397_a(p_151395_1_, var3.getKey())) {
                return var3.getValue();
            }
        }
        return null;
    }
    
    private boolean func_151397_a(final ItemStack p_151397_1_, final ItemStack p_151397_2_) {
        return p_151397_2_.getItem() == p_151397_1_.getItem() && (p_151397_2_.getMetadata() == 32767 || p_151397_2_.getMetadata() == p_151397_1_.getMetadata());
    }
    
    public Map getSmeltingList() {
        return this.smeltingList;
    }
    
    public float getSmeltingExperience(final ItemStack p_151398_1_) {
        for (final Map.Entry var3 : this.experienceList.entrySet()) {
            if (this.func_151397_a(p_151398_1_, var3.getKey())) {
                return var3.getValue();
            }
        }
        return 0.0f;
    }
    
    static {
        smeltingBase = new FurnaceRecipes();
    }
}
