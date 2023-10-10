package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public final class ItemStack {
   private boolean canDestroyCacheResult;
   public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.###");
   private Item item;
   private int itemDamage;
   public int stackSize;
   private Block canDestroyCacheBlock;
   private EntityItemFrame itemFrame;
   private boolean canPlaceOnCacheResult;
   private NBTTagCompound stackTagCompound;
   private Block canPlaceOnCacheBlock;
   public int animationsToGo;

   public int getMaxDamage() {
      return this.item.getMaxDamage();
   }

   public static ItemStack loadItemStackFromNBT(NBTTagCompound var0) {
      ItemStack var1 = new ItemStack();
      var1.readFromNBT(var0);
      return var1.getItem() != null ? var1 : null;
   }

   public ItemStack(Block var1) {
      this((Block)var1, 1);
   }

   public void readFromNBT(NBTTagCompound var1) {
      if (var1.hasKey("id", 8)) {
         this.item = Item.getByNameOrId(var1.getString("id"));
      } else {
         this.item = Item.getItemById(var1.getShort("id"));
      }

      this.stackSize = var1.getByte("Count");
      this.itemDamage = var1.getShort("Damage");
      if (this.itemDamage < 0) {
         this.itemDamage = 0;
      }

      if (var1.hasKey("tag", 10)) {
         this.stackTagCompound = var1.getCompoundTag("tag");
         if (this.item != null) {
            this.item.updateItemStackNBT(this.stackTagCompound);
         }
      }

   }

   public void hitEntity(EntityLivingBase var1, EntityPlayer var2) {
      boolean var3 = this.item.hitEntity(this, var1, var2);
      if (var3) {
         var2.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
      }

   }

   public void clearCustomName() {
      if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10)) {
         NBTTagCompound var1 = this.stackTagCompound.getCompoundTag("display");
         var1.removeTag("Name");
         if (var1.hasNoTags()) {
            this.stackTagCompound.removeTag("display");
            if (this.stackTagCompound.hasNoTags()) {
               this.setTagCompound((NBTTagCompound)null);
            }
         }
      }

   }

   public static boolean areItemStackTagsEqual(ItemStack var0, ItemStack var1) {
      return var0 == null && var1 == null ? true : (var0 != null && var1 != null ? (var0.stackTagCompound == null && var1.stackTagCompound != null ? false : var0.stackTagCompound == null || var0.stackTagCompound.equals(var1.stackTagCompound)) : false);
   }

   public NBTTagCompound getSubCompound(String var1, boolean var2) {
      if (this.stackTagCompound != null && this.stackTagCompound.hasKey(var1, 10)) {
         return this.stackTagCompound.getCompoundTag(var1);
      } else if (var2) {
         NBTTagCompound var3 = new NBTTagCompound();
         this.setTagInfo(var1, var3);
         return var3;
      } else {
         return null;
      }
   }

   public int getMaxStackSize() {
      return this.getItem().getItemStackLimit();
   }

   public static boolean areItemStacksEqual(ItemStack var0, ItemStack var1) {
      return var0 == null && var1 == null ? true : (var0 != null && var1 != null ? var0.isItemStackEqual(var1) : false);
   }

   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      ResourceLocation var2 = (ResourceLocation)Item.itemRegistry.getNameForObject(this.item);
      var1.setString("id", var2 == null ? "minecraft:air" : var2.toString());
      var1.setByte("Count", (byte)this.stackSize);
      var1.setShort("Damage", (short)this.itemDamage);
      if (this.stackTagCompound != null) {
         var1.setTag("tag", this.stackTagCompound);
      }

      return var1;
   }

   private ItemStack() {
      this.canDestroyCacheBlock = null;
      this.canDestroyCacheResult = false;
      this.canPlaceOnCacheBlock = null;
      this.canPlaceOnCacheResult = false;
   }

   public boolean isItemEqual(ItemStack var1) {
      return var1 != null && this.item == var1.item && this.itemDamage == var1.itemDamage;
   }

   public boolean hasEffect() {
      return this.getItem().hasEffect(this);
   }

   public ItemStack splitStack(int var1) {
      ItemStack var2 = new ItemStack(this.item, var1, this.itemDamage);
      if (this.stackTagCompound != null) {
         var2.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
      }

      this.stackSize -= var1;
      return var2;
   }

   public boolean canDestroy(Block var1) {
      if (var1 == this.canDestroyCacheBlock) {
         return this.canDestroyCacheResult;
      } else {
         this.canDestroyCacheBlock = var1;
         if (this != null && this.stackTagCompound.hasKey("CanDestroy", 9)) {
            NBTTagList var2 = this.stackTagCompound.getTagList("CanDestroy", 8);

            for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
               Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));
               if (var4 == var1) {
                  this.canDestroyCacheResult = true;
                  return true;
               }
            }
         }

         this.canDestroyCacheResult = false;
         return false;
      }
   }

   public boolean canHarvestBlock(Block var1) {
      return this.item.canHarvestBlock(var1);
   }

   public void onCrafting(World var1, EntityPlayer var2, int var3) {
      var2.addStat(StatList.objectCraftStats[Item.getIdFromItem(this.item)], var3);
      this.item.onCreated(this, var1, var2);
   }

   public boolean isOnItemFrame() {
      return this.itemFrame != null;
   }

   public EnumAction getItemUseAction() {
      return this.getItem().getItemUseAction(this);
   }

   public boolean onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumFacing var4, float var5, float var6, float var7) {
      boolean var8 = this.getItem().onItemUse(this, var1, var2, var3, var4, var5, var6, var7);
      if (var8) {
         var1.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
      }

      return var8;
   }

   public static ItemStack copyItemStack(ItemStack var0) {
      return var0 == null ? null : var0.copy();
   }

   public boolean getHasSubtypes() {
      return this.item.getHasSubtypes();
   }

   public ItemStack useItemRightClick(World var1, EntityPlayer var2) {
      return this.getItem().onItemRightClick(this, var1, var2);
   }

   public void setItem(Item var1) {
      this.item = var1;
   }

   public boolean interactWithEntity(EntityPlayer var1, EntityLivingBase var2) {
      return this.item.itemInteractionForEntity(this, var1, var2);
   }

   public ItemStack(Block var1, int var2) {
      this((Block)var1, var2, 0);
   }

   public int getItemDamage() {
      return this.itemDamage;
   }

   public void setRepairCost(int var1) {
      if (this != null) {
         this.stackTagCompound = new NBTTagCompound();
      }

      this.stackTagCompound.setInteger("RepairCost", var1);
   }

   public void onBlockDestroyed(World var1, Block var2, BlockPos var3, EntityPlayer var4) {
      boolean var5 = this.item.onBlockDestroyed(this, var1, var2, var3, var4);
      if (var5) {
         var4.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
      }

   }

   public ItemStack copy() {
      ItemStack var1 = new ItemStack(this.item, this.stackSize, this.itemDamage);
      if (this.stackTagCompound != null) {
         var1.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
      }

      return var1;
   }

   public EntityItemFrame getItemFrame() {
      return this.itemFrame;
   }

   public void onPlayerStoppedUsing(World var1, EntityPlayer var2, int var3) {
      this.getItem().onPlayerStoppedUsing(this, var1, var2, var3);
   }

   public ItemStack(Block var1, int var2, int var3) {
      this(Item.getItemFromBlock(var1), var2, var3);
   }

   public IChatComponent getChatComponent() {
      ChatComponentText var1 = new ChatComponentText(this.getDisplayName());
      if (this == null) {
         var1.getChatStyle().setItalic(true);
      }

      IChatComponent var2 = (new ChatComponentText("[")).appendSibling(var1).appendText("]");
      if (this.item != null) {
         NBTTagCompound var3 = new NBTTagCompound();
         this.writeToNBT(var3);
         var2.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(var3.toString())));
         var2.getChatStyle().setColor(this.getRarity().rarityColor);
      }

      return var2;
   }

   public ItemStack(Item var1) {
      this((Item)var1, 1);
   }

   public boolean getIsItemStackEqual(ItemStack var1) {
      return this.isItemStackEqual(var1);
   }

   public NBTTagList getEnchantmentTagList() {
      return this.stackTagCompound == null ? null : this.stackTagCompound.getTagList("ench", 10);
   }

   public Multimap getAttributeModifiers() {
      Object var1;
      if (this != null && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
         var1 = HashMultimap.create();
         NBTTagList var2 = this.stackTagCompound.getTagList("AttributeModifiers", 10);

         for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            AttributeModifier var5 = SharedMonsterAttributes.readAttributeModifierFromNBT(var4);
            if (var5 != null && var5.getID().getLeastSignificantBits() != 0L && var5.getID().getMostSignificantBits() != 0L) {
               ((Multimap)var1).put(var4.getString("AttributeName"), var5);
            }
         }
      } else {
         var1 = this.getItem().getItemAttributeModifiers();
      }

      return (Multimap)var1;
   }

   public EnumRarity getRarity() {
      return this.getItem().getRarity(this);
   }

   public int getMaxItemUseDuration() {
      return this.getItem().getMaxItemUseDuration(this);
   }

   public List getTooltip(EntityPlayer var1, boolean var2) {
      ArrayList var3 = Lists.newArrayList();
      String var4 = this.getDisplayName();
      if (this == null) {
         var4 = EnumChatFormatting.ITALIC + var4;
      }

      var4 = var4 + EnumChatFormatting.RESET;
      if (var2) {
         String var5 = "";
         if (var4.length() > 0) {
            var4 = var4 + " (";
            var5 = ")";
         }

         int var6 = Item.getIdFromItem(this.item);
         if (this.getHasSubtypes()) {
            var4 = var4 + String.format("#%04d/%d%s", var6, this.itemDamage, var5);
         } else {
            var4 = var4 + String.format("#%04d%s", var6, var5);
         }
      } else if (this == null && this.item == Items.filled_map) {
         var4 = var4 + " #" + this.itemDamage;
      }

      var3.add(var4);
      int var14 = 0;
      if (this != null && this.stackTagCompound.hasKey("HideFlags", 99)) {
         var14 = this.stackTagCompound.getInteger("HideFlags");
      }

      if ((var14 & 32) == 0) {
         this.item.addInformation(this, var1, var3, var2);
      }

      NBTTagList var18;
      int var20;
      if (this != null) {
         if ((var14 & 1) == 0) {
            NBTTagList var15 = this.getEnchantmentTagList();
            if (var15 != null) {
               for(int var7 = 0; var7 < var15.tagCount(); ++var7) {
                  short var8 = var15.getCompoundTagAt(var7).getShort("id");
                  short var9 = var15.getCompoundTagAt(var7).getShort("lvl");
                  if (Enchantment.getEnchantmentById(var8) != null) {
                     var3.add(Enchantment.getEnchantmentById(var8).getTranslatedName(var9));
                  }
               }
            }
         }

         if (this.stackTagCompound.hasKey("display", 10)) {
            NBTTagCompound var16 = this.stackTagCompound.getCompoundTag("display");
            if (var16.hasKey("color", 3)) {
               if (var2) {
                  var3.add("Color: #" + Integer.toHexString(var16.getInteger("color")).toUpperCase());
               } else {
                  var3.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("item.dyed"));
               }
            }

            if (var16.getTagId("Lore") == 9) {
               var18 = var16.getTagList("Lore", 8);
               if (var18.tagCount() > 0) {
                  for(var20 = 0; var20 < var18.tagCount(); ++var20) {
                     var3.add("" + EnumChatFormatting.DARK_PURPLE + EnumChatFormatting.ITALIC + var18.getStringTagAt(var20));
                  }
               }
            }
         }
      }

      Multimap var17 = this.getAttributeModifiers();
      if (!var17.isEmpty() && (var14 & 2) == 0) {
         var3.add("");
         Iterator var21 = var17.entries().iterator();

         while(var21.hasNext()) {
            Entry var19 = (Entry)var21.next();
            AttributeModifier var22 = (AttributeModifier)var19.getValue();
            double var10 = var22.getAmount();
            if (var22.getID() == Item.itemModifierUUID) {
               var10 += (double)EnchantmentHelper.func_152377_a(this, EnumCreatureAttribute.UNDEFINED);
            }

            double var12;
            if (var22.getOperation() != 1 && var22.getOperation() != 2) {
               var12 = var10;
            } else {
               var12 = var10 * 100.0D;
            }

            if (var10 > 0.0D) {
               var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + var22.getOperation(), DECIMALFORMAT.format(var12), StatCollector.translateToLocal("attribute.name." + (String)var19.getKey())));
            } else if (var10 < 0.0D) {
               var12 *= -1.0D;
               var3.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + var22.getOperation(), DECIMALFORMAT.format(var12), StatCollector.translateToLocal("attribute.name." + (String)var19.getKey())));
            }
         }
      }

      if (this != null && this.getTagCompound().getBoolean("Unbreakable") && (var14 & 4) == 0) {
         var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("item.unbreakable"));
      }

      Block var23;
      if (this != null && this.stackTagCompound.hasKey("CanDestroy", 9) && (var14 & 8) == 0) {
         var18 = this.stackTagCompound.getTagList("CanDestroy", 8);
         if (var18.tagCount() > 0) {
            var3.add("");
            var3.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canBreak"));

            for(var20 = 0; var20 < var18.tagCount(); ++var20) {
               var23 = Block.getBlockFromName(var18.getStringTagAt(var20));
               if (var23 != null) {
                  var3.add(EnumChatFormatting.DARK_GRAY + var23.getLocalizedName());
               } else {
                  var3.add(EnumChatFormatting.DARK_GRAY + "missingno");
               }
            }
         }
      }

      if (this != null && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (var14 & 16) == 0) {
         var18 = this.stackTagCompound.getTagList("CanPlaceOn", 8);
         if (var18.tagCount() > 0) {
            var3.add("");
            var3.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canPlace"));

            for(var20 = 0; var20 < var18.tagCount(); ++var20) {
               var23 = Block.getBlockFromName(var18.getStringTagAt(var20));
               if (var23 != null) {
                  var3.add(EnumChatFormatting.DARK_GRAY + var23.getLocalizedName());
               } else {
                  var3.add(EnumChatFormatting.DARK_GRAY + "missingno");
               }
            }
         }
      }

      if (var2) {
         if (this != false) {
            var3.add("Durability: " + (this.getMaxDamage() - this.getItemDamage()) + " / " + this.getMaxDamage());
         }

         var3.add(EnumChatFormatting.DARK_GRAY + ((ResourceLocation)Item.itemRegistry.getNameForObject(this.item)).toString());
         if (this != null) {
            var3.add(EnumChatFormatting.DARK_GRAY + "NBT: " + this.getTagCompound().getKeySet().size() + " tag(s)");
         }
      }

      return var3;
   }

   public boolean canEditBlocks() {
      return this.getItem().canItemEditBlocks();
   }

   public ItemStack(Item var1, int var2) {
      this((Item)var1, var2, 0);
   }

   public int getMetadata() {
      return this.itemDamage;
   }

   public Item getItem() {
      return this.item;
   }

   public NBTTagCompound getTagCompound() {
      return this.stackTagCompound;
   }

   private boolean isItemStackEqual(ItemStack var1) {
      return this.stackSize != var1.stackSize ? false : (this.item != var1.item ? false : (this.itemDamage != var1.itemDamage ? false : (this.stackTagCompound == null && var1.stackTagCompound != null ? false : this.stackTagCompound == null || this.stackTagCompound.equals(var1.stackTagCompound))));
   }

   public static boolean areItemsEqual(ItemStack var0, ItemStack var1) {
      return var0 == null && var1 == null ? true : (var0 != null && var1 != null ? var0.isItemEqual(var1) : false);
   }

   public String getUnlocalizedName() {
      return this.item.getUnlocalizedName(this);
   }

   public float getStrVsBlock(Block var1) {
      return this.getItem().getStrVsBlock(this, var1);
   }

   public void damageItem(int var1, EntityLivingBase var2) {
      if ((!(var2 instanceof EntityPlayer) || !((EntityPlayer)var2).capabilities.isCreativeMode) && this == null && var2.getRNG() != false) {
         var2.renderBrokenItemStack(this);
         --this.stackSize;
         if (var2 instanceof EntityPlayer) {
            EntityPlayer var3 = (EntityPlayer)var2;
            var3.triggerAchievement(StatList.objectBreakStats[Item.getIdFromItem(this.item)]);
            if (this.stackSize == 0 && this.getItem() instanceof ItemBow) {
               var3.destroyCurrentEquippedItem();
            }
         }

         if (this.stackSize < 0) {
            this.stackSize = 0;
         }

         this.itemDamage = 0;
      }

   }

   public void setItemDamage(int var1) {
      this.itemDamage = var1;
      if (this.itemDamage < 0) {
         this.itemDamage = 0;
      }

   }

   public int getRepairCost() {
      return this != null && this.stackTagCompound.hasKey("RepairCost", 3) ? this.stackTagCompound.getInteger("RepairCost") : 0;
   }

   public boolean isStackable() {
      return this.getMaxStackSize() > 1 && (this != null || this != false);
   }

   public boolean isItemEnchantable() {
      return !this.getItem().isItemTool(this) ? false : this == null;
   }

   public ItemStack setStackDisplayName(String var1) {
      if (this.stackTagCompound == null) {
         this.stackTagCompound = new NBTTagCompound();
      }

      if (!this.stackTagCompound.hasKey("display", 10)) {
         this.stackTagCompound.setTag("display", new NBTTagCompound());
      }

      this.stackTagCompound.getCompoundTag("display").setString("Name", var1);
      return this;
   }

   public ItemStack(Item var1, int var2, int var3) {
      this.canDestroyCacheBlock = null;
      this.canDestroyCacheResult = false;
      this.canPlaceOnCacheBlock = null;
      this.canPlaceOnCacheResult = false;
      this.item = var1;
      this.stackSize = var2;
      this.itemDamage = var3;
      if (this.itemDamage < 0) {
         this.itemDamage = 0;
      }

   }

   public void setTagCompound(NBTTagCompound var1) {
      this.stackTagCompound = var1;
   }

   public ItemStack onItemUseFinish(World var1, EntityPlayer var2) {
      return this.getItem().onItemUseFinish(this, var1, var2);
   }

   public String toString() {
      return this.stackSize + "x" + this.item.getUnlocalizedName() + "@" + this.itemDamage;
   }

   public boolean canPlaceOn(Block var1) {
      if (var1 == this.canPlaceOnCacheBlock) {
         return this.canPlaceOnCacheResult;
      } else {
         this.canPlaceOnCacheBlock = var1;
         if (this != null && this.stackTagCompound.hasKey("CanPlaceOn", 9)) {
            NBTTagList var2 = this.stackTagCompound.getTagList("CanPlaceOn", 8);

            for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
               Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));
               if (var4 == var1) {
                  this.canPlaceOnCacheResult = true;
                  return true;
               }
            }
         }

         this.canPlaceOnCacheResult = false;
         return false;
      }
   }

   public void addEnchantment(Enchantment var1, int var2) {
      if (this.stackTagCompound == null) {
         this.setTagCompound(new NBTTagCompound());
      }

      if (!this.stackTagCompound.hasKey("ench", 9)) {
         this.stackTagCompound.setTag("ench", new NBTTagList());
      }

      NBTTagList var3 = this.stackTagCompound.getTagList("ench", 10);
      NBTTagCompound var4 = new NBTTagCompound();
      var4.setShort("id", (short)var1.effectId);
      var4.setShort("lvl", (short)((byte)var2));
      var3.appendTag(var4);
   }

   public void setItemFrame(EntityItemFrame var1) {
      this.itemFrame = var1;
   }

   public void setTagInfo(String var1, NBTBase var2) {
      if (this.stackTagCompound == null) {
         this.setTagCompound(new NBTTagCompound());
      }

      this.stackTagCompound.setTag(var1, var2);
   }

   public void updateAnimation(World var1, Entity var2, int var3, boolean var4) {
      if (this.animationsToGo > 0) {
         --this.animationsToGo;
      }

      this.item.onUpdate(this, var1, var2, var3, var4);
   }

   public String getDisplayName() {
      String var1 = this.getItem().getItemStackDisplayName(this);
      if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10)) {
         NBTTagCompound var2 = this.stackTagCompound.getCompoundTag("display");
         if (var2.hasKey("Name", 8)) {
            var1 = var2.getString("Name");
         }
      }

      return var1;
   }
}
