package net.minecraft.item;

import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.block.BlockDispenser;
import net.minecraft.command.IEntitySelector;
import net.minecraft.command.IEntitySelector.ArmoredMob;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.RegistryDefaulted;
import net.minecraft.world.World;

public class ItemArmor
  extends Item
{
  private static final int[] maxDamageArray = { 11, 16, 15, 13 };
  public static final String[] EMPTY_SLOT_NAMES = { "minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots" };
  private static final IBehaviorDispenseItem dispenserBehavior = new BehaviorDefaultDispenseItem()
  {
    private static final String __OBFID = "CL_00001767";
    
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
    {
      BlockPos var3 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
      int var4 = var3.getX();
      int var5 = var3.getY();
      int var6 = var3.getZ();
      AxisAlignedBB var7 = new AxisAlignedBB(var4, var5, var6, var4 + 1, var5 + 1, var6 + 1);
      List var8 = source.getWorld().func_175647_a(EntityLivingBase.class, var7, Predicates.and(IEntitySelector.field_180132_d, new IEntitySelector.ArmoredMob(stack)));
      if (var8.size() > 0)
      {
        EntityLivingBase var9 = (EntityLivingBase)var8.get(0);
        int var10 = (var9 instanceof EntityPlayer) ? 1 : 0;
        int var11 = EntityLiving.getArmorPosition(stack);
        ItemStack var12 = stack.copy();
        var12.stackSize = 1;
        var9.setCurrentItemOrArmor(var11 - var10, var12);
        if ((var9 instanceof EntityLiving)) {
          ((EntityLiving)var9).setEquipmentDropChance(var11, 2.0F);
        }
        stack.stackSize -= 1;
        return stack;
      }
      return super.dispenseStack(source, stack);
    }
  };
  public final int armorType;
  public final int damageReduceAmount;
  public final int renderIndex;
  private final ArmorMaterial material;
  private static final String __OBFID = "CL_00001766";
  
  public ItemArmor(ArmorMaterial p_i45325_1_, int p_i45325_2_, int p_i45325_3_)
  {
    this.material = p_i45325_1_;
    this.armorType = p_i45325_3_;
    this.renderIndex = p_i45325_2_;
    this.damageReduceAmount = p_i45325_1_.getDamageReductionAmount(p_i45325_3_);
    setMaxDamage(p_i45325_1_.getDurability(p_i45325_3_));
    this.maxStackSize = 1;
    setCreativeTab(CreativeTabs.tabCombat);
    BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserBehavior);
  }
  
  public int getColorFromItemStack(ItemStack stack, int renderPass)
  {
    if (renderPass > 0) {
      return 16777215;
    }
    int var3 = getColor(stack);
    if (var3 < 0) {
      var3 = 16777215;
    }
    return var3;
  }
  
  public int getItemEnchantability()
  {
    return this.material.getEnchantability();
  }
  
  public ArmorMaterial getArmorMaterial()
  {
    return this.material;
  }
  
  public boolean hasColor(ItemStack p_82816_1_)
  {
    return !p_82816_1_.getTagCompound().hasKey("display", 10) ? false : !p_82816_1_.hasTagCompound() ? false : this.material != ArmorMaterial.LEATHER ? false : p_82816_1_.getTagCompound().getCompoundTag("display").hasKey("color", 3);
  }
  
  public int getColor(ItemStack p_82814_1_)
  {
    if (this.material != ArmorMaterial.LEATHER) {
      return -1;
    }
    NBTTagCompound var2 = p_82814_1_.getTagCompound();
    if (var2 != null)
    {
      NBTTagCompound var3 = var2.getCompoundTag("display");
      if ((var3 != null) && (var3.hasKey("color", 3))) {
        return var3.getInteger("color");
      }
    }
    return 10511680;
  }
  
  public void removeColor(ItemStack p_82815_1_)
  {
    if (this.material == ArmorMaterial.LEATHER)
    {
      NBTTagCompound var2 = p_82815_1_.getTagCompound();
      if (var2 != null)
      {
        NBTTagCompound var3 = var2.getCompoundTag("display");
        if (var3.hasKey("color")) {
          var3.removeTag("color");
        }
      }
    }
  }
  
  public void func_82813_b(ItemStack p_82813_1_, int p_82813_2_)
  {
    if (this.material != ArmorMaterial.LEATHER) {
      throw new UnsupportedOperationException("Can't dye non-leather!");
    }
    NBTTagCompound var3 = p_82813_1_.getTagCompound();
    if (var3 == null)
    {
      var3 = new NBTTagCompound();
      p_82813_1_.setTagCompound(var3);
    }
    NBTTagCompound var4 = var3.getCompoundTag("display");
    if (!var3.hasKey("display", 10)) {
      var3.setTag("display", var4);
    }
    var4.setInteger("color", p_82813_2_);
  }
  
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
  {
    return this.material.getBaseItemForRepair() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
  {
    int var4 = EntityLiving.getArmorPosition(itemStackIn) - 1;
    ItemStack var5 = playerIn.getCurrentArmor(var4);
    if (var5 == null)
    {
      playerIn.setCurrentItemOrArmor(var4, itemStackIn.copy());
      itemStackIn.stackSize = 0;
    }
    return itemStackIn;
  }
  
  public static enum ArmorMaterial
  {
    private final String field_179243_f;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private static final ArmorMaterial[] $VALUES = { LEATHER, CHAIN, IRON, GOLD, DIAMOND };
    private static final String __OBFID = "CL_00001768";
    
    private ArmorMaterial(String p_i45789_1_, int p_i45789_2_, String p_i45789_3_, int p_i45789_4_, int[] p_i45789_5_, int p_i45789_6_)
    {
      this.field_179243_f = p_i45789_3_;
      this.maxDamageFactor = p_i45789_4_;
      this.damageReductionAmountArray = p_i45789_5_;
      this.enchantability = p_i45789_6_;
    }
    
    public int getDurability(int p_78046_1_)
    {
      return ItemArmor.maxDamageArray[p_78046_1_] * this.maxDamageFactor;
    }
    
    public int getDamageReductionAmount(int p_78044_1_)
    {
      return this.damageReductionAmountArray[p_78044_1_];
    }
    
    public int getEnchantability()
    {
      return this.enchantability;
    }
    
    public Item getBaseItemForRepair()
    {
      return this == DIAMOND ? Items.diamond : this == IRON ? Items.iron_ingot : this == GOLD ? Items.gold_ingot : this == CHAIN ? Items.iron_ingot : this == LEATHER ? Items.leather : null;
    }
    
    public String func_179242_c()
    {
      return this.field_179243_f;
    }
  }
}
