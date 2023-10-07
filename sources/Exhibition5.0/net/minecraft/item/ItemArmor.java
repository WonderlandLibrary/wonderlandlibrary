// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.init.Items;
import java.util.List;
import net.minecraft.util.BlockPos;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.IBehaviorDispenseItem;

public class ItemArmor extends Item
{
    private static final int[] maxDamageArray;
    public static final String[] EMPTY_SLOT_NAMES;
    private static final IBehaviorDispenseItem dispenserBehavior;
    public final int armorType;
    public final int damageReduceAmount;
    public final int renderIndex;
    private final ArmorMaterial material;
    private static final String __OBFID = "CL_00001766";
    
    public ItemArmor(final ArmorMaterial p_i45325_1_, final int p_i45325_2_, final int p_i45325_3_) {
        this.material = p_i45325_1_;
        this.armorType = p_i45325_3_;
        this.renderIndex = p_i45325_2_;
        this.damageReduceAmount = p_i45325_1_.getDamageReductionAmount(p_i45325_3_);
        this.setMaxDamage(p_i45325_1_.getDurability(p_i45325_3_));
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabCombat);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, ItemArmor.dispenserBehavior);
    }
    
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        if (renderPass > 0) {
            return 16777215;
        }
        int var3 = this.getColor(stack);
        if (var3 < 0) {
            var3 = 16777215;
        }
        return var3;
    }
    
    @Override
    public int getItemEnchantability() {
        return this.material.getEnchantability();
    }
    
    public ArmorMaterial getArmorMaterial() {
        return this.material;
    }
    
    public boolean hasColor(final ItemStack p_82816_1_) {
        return this.material == ArmorMaterial.LEATHER && p_82816_1_.hasTagCompound() && p_82816_1_.getTagCompound().hasKey("display", 10) && p_82816_1_.getTagCompound().getCompoundTag("display").hasKey("color", 3);
    }
    
    public int getColor(final ItemStack p_82814_1_) {
        if (this.material != ArmorMaterial.LEATHER) {
            return -1;
        }
        final NBTTagCompound var2 = p_82814_1_.getTagCompound();
        if (var2 != null) {
            final NBTTagCompound var3 = var2.getCompoundTag("display");
            if (var3 != null && var3.hasKey("color", 3)) {
                return var3.getInteger("color");
            }
        }
        return 10511680;
    }
    
    public void removeColor(final ItemStack p_82815_1_) {
        if (this.material == ArmorMaterial.LEATHER) {
            final NBTTagCompound var2 = p_82815_1_.getTagCompound();
            if (var2 != null) {
                final NBTTagCompound var3 = var2.getCompoundTag("display");
                if (var3.hasKey("color")) {
                    var3.removeTag("color");
                }
            }
        }
    }
    
    public void func_82813_b(final ItemStack p_82813_1_, final int p_82813_2_) {
        if (this.material != ArmorMaterial.LEATHER) {
            throw new UnsupportedOperationException("Can't dye non-leather!");
        }
        NBTTagCompound var3 = p_82813_1_.getTagCompound();
        if (var3 == null) {
            var3 = new NBTTagCompound();
            p_82813_1_.setTagCompound(var3);
        }
        final NBTTagCompound var4 = var3.getCompoundTag("display");
        if (!var3.hasKey("display", 10)) {
            var3.setTag("display", var4);
        }
        var4.setInteger("color", p_82813_2_);
    }
    
    @Override
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return this.material.getBaseItemForRepair() == repair.getItem() || super.getIsRepairable(toRepair, repair);
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        final int var4 = EntityLiving.getArmorPosition(itemStackIn) - 1;
        final ItemStack var5 = playerIn.getCurrentArmor(var4);
        if (var5 == null) {
            playerIn.setCurrentItemOrArmor(var4, itemStackIn.copy());
            itemStackIn.stackSize = 0;
        }
        return itemStackIn;
    }
    
    static {
        maxDamageArray = new int[] { 11, 16, 15, 13 };
        EMPTY_SLOT_NAMES = new String[] { "minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots" };
        dispenserBehavior = new BehaviorDefaultDispenseItem() {
            private static final String __OBFID = "CL_00001767";
            
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final BlockPos var3 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                final int var4 = var3.getX();
                final int var5 = var3.getY();
                final int var6 = var3.getZ();
                final AxisAlignedBB var7 = new AxisAlignedBB(var4, var5, var6, var4 + 1, var5 + 1, var6 + 1);
                final List var8 = source.getWorld().func_175647_a(EntityLivingBase.class, var7, Predicates.and(IEntitySelector.field_180132_d, (Predicate)new IEntitySelector.ArmoredMob(stack)));
                if (var8.size() > 0) {
                    final EntityLivingBase var9 = var8.get(0);
                    final int var10 = (var9 instanceof EntityPlayer) ? 1 : 0;
                    final int var11 = EntityLiving.getArmorPosition(stack);
                    final ItemStack var12 = stack.copy();
                    var12.stackSize = 1;
                    var9.setCurrentItemOrArmor(var11 - var10, var12);
                    if (var9 instanceof EntityLiving) {
                        ((EntityLiving)var9).setEquipmentDropChance(var11, 2.0f);
                    }
                    --stack.stackSize;
                    return stack;
                }
                return super.dispenseStack(source, stack);
            }
        };
    }
    
    public enum ArmorMaterial
    {
        LEATHER("LEATHER", 0, "leather", 5, new int[] { 1, 3, 2, 1 }, 15), 
        CHAIN("CHAIN", 1, "chainmail", 15, new int[] { 2, 5, 4, 1 }, 12), 
        IRON("IRON", 2, "iron", 15, new int[] { 2, 6, 5, 2 }, 9), 
        GOLD("GOLD", 3, "gold", 7, new int[] { 2, 5, 3, 1 }, 25), 
        DIAMOND("DIAMOND", 4, "diamond", 33, new int[] { 3, 8, 6, 3 }, 10);
        
        private final String field_179243_f;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;
        private static final ArmorMaterial[] $VALUES;
        private static final String __OBFID = "CL_00001768";
        
        private ArmorMaterial(final String p_i45789_1_, final int p_i45789_2_, final String p_i45789_3_, final int p_i45789_4_, final int[] p_i45789_5_, final int p_i45789_6_) {
            this.field_179243_f = p_i45789_3_;
            this.maxDamageFactor = p_i45789_4_;
            this.damageReductionAmountArray = p_i45789_5_;
            this.enchantability = p_i45789_6_;
        }
        
        public int getDurability(final int p_78046_1_) {
            return ItemArmor.maxDamageArray[p_78046_1_] * this.maxDamageFactor;
        }
        
        public int getDamageReductionAmount(final int p_78044_1_) {
            return this.damageReductionAmountArray[p_78044_1_];
        }
        
        public int getEnchantability() {
            return this.enchantability;
        }
        
        public Item getBaseItemForRepair() {
            return (this == ArmorMaterial.LEATHER) ? Items.leather : ((this == ArmorMaterial.CHAIN) ? Items.iron_ingot : ((this == ArmorMaterial.GOLD) ? Items.gold_ingot : ((this == ArmorMaterial.IRON) ? Items.iron_ingot : ((this == ArmorMaterial.DIAMOND) ? Items.diamond : null))));
        }
        
        public String func_179242_c() {
            return this.field_179243_f;
        }
        
        static {
            $VALUES = new ArmorMaterial[] { ArmorMaterial.LEATHER, ArmorMaterial.CHAIN, ArmorMaterial.IRON, ArmorMaterial.GOLD, ArmorMaterial.DIAMOND };
        }
    }
}
