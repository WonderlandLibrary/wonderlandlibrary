package net.minecraft.item.crafting;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
   private Map experienceList = Maps.newHashMap();
   private Map smeltingList = Maps.newHashMap();

   private FurnaceRecipes() {
      this.addSmeltingRecipeForBlock(Blocks.iron_ore, new ItemStack(Items.iron_ingot), 0.7F);
      this.addSmeltingRecipeForBlock(Blocks.gold_ore, new ItemStack(Items.gold_ingot), 1.0F);
      this.addSmeltingRecipeForBlock(Blocks.diamond_ore, new ItemStack(Items.diamond), 1.0F);
      this.addSmeltingRecipeForBlock(Blocks.sand, new ItemStack(Blocks.glass), 0.1F);
      this.addSmelting(Items.porkchop, new ItemStack(Items.cooked_porkchop), 0.35F);
      this.addSmelting(Items.beef, new ItemStack(Items.cooked_beef), 0.35F);
      this.addSmelting(Items.chicken, new ItemStack(Items.cooked_chicken), 0.35F);
      this.addSmelting(Items.rabbit, new ItemStack(Items.cooked_rabbit), 0.35F);
      this.addSmelting(Items.mutton, new ItemStack(Items.cooked_mutton), 0.35F);
      this.addSmeltingRecipeForBlock(Blocks.cobblestone, new ItemStack(Blocks.stone), 0.1F);
      this.addSmeltingRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.DEFAULT_META), new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.CRACKED_META), 0.1F);
      this.addSmelting(Items.clay_ball, new ItemStack(Items.brick), 0.3F);
      this.addSmeltingRecipeForBlock(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.35F);
      this.addSmeltingRecipeForBlock(Blocks.cactus, new ItemStack(Items.dye, 1, EnumDyeColor.GREEN.getDyeDamage()), 0.2F);
      this.addSmeltingRecipeForBlock(Blocks.log, new ItemStack(Items.coal, 1, 1), 0.15F);
      this.addSmeltingRecipeForBlock(Blocks.log2, new ItemStack(Items.coal, 1, 1), 0.15F);
      this.addSmeltingRecipeForBlock(Blocks.emerald_ore, new ItemStack(Items.emerald), 1.0F);
      this.addSmelting(Items.potato, new ItemStack(Items.baked_potato), 0.35F);
      this.addSmeltingRecipeForBlock(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.1F);
      this.addSmeltingRecipe(new ItemStack(Blocks.sponge, 1, 1), new ItemStack(Blocks.sponge, 1, 0), 0.15F);
      ItemFishFood.FishType[] var4;
      int var3 = (var4 = ItemFishFood.FishType.values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         ItemFishFood.FishType var1 = var4[var2];
         if (var1.canCook()) {
            this.addSmeltingRecipe(new ItemStack(Items.fish, 1, var1.getMetadata()), new ItemStack(Items.cooked_fish, 1, var1.getMetadata()), 0.35F);
         }
      }

      this.addSmeltingRecipeForBlock(Blocks.coal_ore, new ItemStack(Items.coal), 0.1F);
      this.addSmeltingRecipeForBlock(Blocks.redstone_ore, new ItemStack(Items.redstone), 0.7F);
      this.addSmeltingRecipeForBlock(Blocks.lapis_ore, new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), 0.2F);
      this.addSmeltingRecipeForBlock(Blocks.quartz_ore, new ItemStack(Items.quartz), 0.2F);
   }

   public void addSmeltingRecipeForBlock(Block var1, ItemStack var2, float var3) {
      this.addSmelting(Item.getItemFromBlock(var1), var2, var3);
   }

   public void addSmeltingRecipe(ItemStack var1, ItemStack var2, float var3) {
      this.smeltingList.put(var1, var2);
      this.experienceList.put(var2, var3);
   }

   public float getSmeltingExperience(ItemStack var1) {
      Iterator var3 = this.experienceList.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         if (var1 == (ItemStack)var2.getKey()) {
            return (Float)var2.getValue();
         }
      }

      return 0.0F;
   }

   public static FurnaceRecipes instance() {
      return smeltingBase;
   }

   public Map getSmeltingList() {
      return this.smeltingList;
   }

   public void addSmelting(Item var1, ItemStack var2, float var3) {
      this.addSmeltingRecipe(new ItemStack(var1, 1, 32767), var2, var3);
   }

   public ItemStack getSmeltingResult(ItemStack var1) {
      Iterator var3 = this.smeltingList.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         if (var1 == (ItemStack)var2.getKey()) {
            return (ItemStack)var2.getValue();
         }
      }

      return null;
   }
}
