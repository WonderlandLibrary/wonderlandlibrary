package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemPotion extends Item {
   private static final Map SUB_ITEMS_CACHE = Maps.newLinkedHashMap();
   private Map effectCache = Maps.newHashMap();

   public ItemPotion() {
      this.setMaxStackSize(1);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(CreativeTabs.tabBrewing);
   }

   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      if (var1.getMetadata() != 0) {
         if (!var3.capabilities.isCreativeMode) {
            --var1.stackSize;
         }

         var2.playSoundAtEntity(var3, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
         if (!var2.isRemote) {
            var2.spawnEntityInWorld(new EntityPotion(var2, var3, var1));
         }

         var3.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
         return var1;
      } else {
         var3.setItemInUse(var1, this.getMaxItemUseDuration(var1));
         return var1;
      }
   }

   public List getEffects(int var1) {
      List var2 = (List)this.effectCache.get(var1);
      if (var2 == null) {
         var2 = PotionHelper.getPotionEffects(var1, false);
         this.effectCache.put(var1, var2);
      }

      return var2;
   }

   public int getMaxItemUseDuration(ItemStack var1) {
      return 32;
   }

   public boolean hasEffect(ItemStack var1) {
      List var2 = this.getEffects(var1);
      return var2 != null && !var2.isEmpty();
   }

   public ItemStack onItemUseFinish(ItemStack var1, World var2, EntityPlayer var3) {
      if (!var3.capabilities.isCreativeMode) {
         --var1.stackSize;
      }

      if (!var2.isRemote) {
         List var4 = this.getEffects(var1);
         if (var4 != null) {
            Iterator var6 = var4.iterator();

            while(var6.hasNext()) {
               PotionEffect var5 = (PotionEffect)var6.next();
               var3.addPotionEffect(new PotionEffect(var5));
            }
         }
      }

      var3.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
      if (!var3.capabilities.isCreativeMode) {
         if (var1.stackSize <= 0) {
            return new ItemStack(Items.glass_bottle);
         }

         var3.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
      }

      return var1;
   }

   public boolean isEffectInstant(int var1) {
      List var2 = this.getEffects(var1);
      if (var2 != null && !var2.isEmpty()) {
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            PotionEffect var3 = (PotionEffect)var4.next();
            if (Potion.potionTypes[var3.getPotionID()].isInstant()) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public EnumAction getItemUseAction(ItemStack var1) {
      return EnumAction.DRINK;
   }

   public int getColorFromDamage(int var1) {
      return PotionHelper.getLiquidColor(var1, false);
   }

   public String getItemStackDisplayName(ItemStack var1) {
      if (var1.getMetadata() == 0) {
         return StatCollector.translateToLocal("item.emptyPotion.name").trim();
      } else {
         String var2 = "";
         if (var1.getMetadata() != 0) {
            var2 = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
         }

         List var3 = Items.potionitem.getEffects(var1);
         String var4;
         if (var3 != null && !var3.isEmpty()) {
            var4 = ((PotionEffect)var3.get(0)).getEffectName();
            var4 = var4 + ".postfix";
            return var2 + StatCollector.translateToLocal(var4).trim();
         } else {
            var4 = PotionHelper.getPotionPrefix(var1.getMetadata());
            return StatCollector.translateToLocal(var4).trim() + " " + super.getItemStackDisplayName(var1);
         }
      }
   }

   public List getEffects(ItemStack var1) {
      if (var1.hasTagCompound() && var1.getTagCompound().hasKey("CustomPotionEffects", 9)) {
         ArrayList var7 = Lists.newArrayList();
         NBTTagList var3 = var1.getTagCompound().getTagList("CustomPotionEffects", 10);

         for(int var4 = 0; var4 < var3.tagCount(); ++var4) {
            NBTTagCompound var5 = var3.getCompoundTagAt(var4);
            PotionEffect var6 = PotionEffect.readCustomPotionEffectFromNBT(var5);
            if (var6 != null) {
               var7.add(var6);
            }
         }

         return var7;
      } else {
         List var2 = (List)this.effectCache.get(var1.getMetadata());
         if (var2 == null) {
            var2 = PotionHelper.getPotionEffects(var1.getMetadata(), false);
            this.effectCache.put(var1.getMetadata(), var2);
         }

         return var2;
      }
   }

   public void getSubItems(Item var1, CreativeTabs var2, List var3) {
      super.getSubItems(var1, var2, var3);
      int var5;
      if (SUB_ITEMS_CACHE.isEmpty()) {
         for(int var4 = 0; var4 <= 15; ++var4) {
            for(var5 = 0; var5 <= 1; ++var5) {
               int var6;
               if (var5 == 0) {
                  var6 = var4 | 8192;
               } else {
                  var6 = var4 | 16384;
               }

               for(int var7 = 0; var7 <= 2; ++var7) {
                  int var8 = var6;
                  if (var7 != 0) {
                     if (var7 == 1) {
                        var8 = var6 | 32;
                     } else if (var7 == 2) {
                        var8 = var6 | 64;
                     }
                  }

                  List var9 = PotionHelper.getPotionEffects(var8, false);
                  if (var9 != null && !var9.isEmpty()) {
                     SUB_ITEMS_CACHE.put(var9, var8);
                  }
               }
            }
         }
      }

      Iterator var10 = SUB_ITEMS_CACHE.values().iterator();

      while(var10.hasNext()) {
         var5 = (Integer)var10.next();
         var3.add(new ItemStack(var1, 1, var5));
      }

   }

   public void addInformation(ItemStack var1, EntityPlayer var2, List var3, boolean var4) {
      if (var1.getMetadata() != 0) {
         List var5 = Items.potionitem.getEffects(var1);
         HashMultimap var6 = HashMultimap.create();
         Iterator var8;
         if (var5 != null && !var5.isEmpty()) {
            var8 = var5.iterator();

            while(var8.hasNext()) {
               PotionEffect var16 = (PotionEffect)var8.next();
               String var9 = StatCollector.translateToLocal(var16.getEffectName()).trim();
               Potion var10 = Potion.potionTypes[var16.getPotionID()];
               Map var11 = var10.getAttributeModifierMap();
               if (var11 != null && var11.size() > 0) {
                  Iterator var13 = var11.entrySet().iterator();

                  while(var13.hasNext()) {
                     Entry var12 = (Entry)var13.next();
                     AttributeModifier var14 = (AttributeModifier)var12.getValue();
                     AttributeModifier var15 = new AttributeModifier(var14.getName(), var10.getAttributeModifierAmount(var16.getAmplifier(), var14), var14.getOperation());
                     var6.put(((IAttribute)var12.getKey()).getAttributeUnlocalizedName(), var15);
                  }
               }

               if (var16.getAmplifier() > 0) {
                  var9 = var9 + " " + StatCollector.translateToLocal("potion.potency." + var16.getAmplifier()).trim();
               }

               if (var16.getDuration() > 20) {
                  var9 = var9 + " (" + Potion.getDurationString(var16) + ")";
               }

               if (var10.isBadEffect()) {
                  var3.add(EnumChatFormatting.RED + var9);
               } else {
                  var3.add(EnumChatFormatting.GRAY + var9);
               }
            }
         } else {
            String var7 = StatCollector.translateToLocal("potion.empty").trim();
            var3.add(EnumChatFormatting.GRAY + var7);
         }

         if (!var6.isEmpty()) {
            var3.add("");
            var3.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
            var8 = var6.entries().iterator();

            while(var8.hasNext()) {
               Entry var17 = (Entry)var8.next();
               AttributeModifier var19 = (AttributeModifier)var17.getValue();
               double var18 = var19.getAmount();
               double var20;
               if (var19.getOperation() != 1 && var19.getOperation() != 2) {
                  var20 = var19.getAmount();
               } else {
                  var20 = var19.getAmount() * 100.0D;
               }

               if (var18 > 0.0D) {
                  var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + var19.getOperation(), ItemStack.DECIMALFORMAT.format(var20), StatCollector.translateToLocal("attribute.name." + (String)var17.getKey())));
               } else if (var18 < 0.0D) {
                  var20 *= -1.0D;
                  var3.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + var19.getOperation(), ItemStack.DECIMALFORMAT.format(var20), StatCollector.translateToLocal("attribute.name." + (String)var17.getKey())));
               }
            }
         }
      }

   }

   public int getColorFromItemStack(ItemStack var1, int var2) {
      return var2 > 0 ? 16777215 : this.getColorFromDamage(var1.getMetadata());
   }
}
