/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 */
package net.minecraft.item;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.World;

public class ItemArmor
extends Item {
    public final int renderIndex;
    private static final IBehaviorDispenseItem dispenserBehavior;
    private final ArmorMaterial material;
    public static final String[] EMPTY_SLOT_NAMES;
    public final int armorType;
    private static final int[] maxDamageArray;
    public final int damageReduceAmount;

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        int n = EntityLiving.getArmorPosition(itemStack) - 1;
        ItemStack itemStack2 = entityPlayer.getCurrentArmor(n);
        if (itemStack2 == null) {
            entityPlayer.setCurrentItemOrArmor(n, itemStack.copy());
            itemStack.stackSize = 0;
        }
        return itemStack;
    }

    @Override
    public int getItemEnchantability() {
        return this.material.getEnchantability();
    }

    public void setColor(ItemStack itemStack, int n) {
        if (this.material != ArmorMaterial.LEATHER) {
            throw new UnsupportedOperationException("Can't dye non-leather!");
        }
        NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
        if (nBTTagCompound == null) {
            nBTTagCompound = new NBTTagCompound();
            itemStack.setTagCompound(nBTTagCompound);
        }
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("display");
        if (!nBTTagCompound.hasKey("display", 10)) {
            nBTTagCompound.setTag("display", nBTTagCompound2);
        }
        nBTTagCompound2.setInteger("color", n);
    }

    public int getColor(ItemStack itemStack) {
        NBTTagCompound nBTTagCompound;
        if (this.material != ArmorMaterial.LEATHER) {
            return -1;
        }
        NBTTagCompound nBTTagCompound2 = itemStack.getTagCompound();
        if (nBTTagCompound2 != null && (nBTTagCompound = nBTTagCompound2.getCompoundTag("display")) != null && nBTTagCompound.hasKey("color", 3)) {
            return nBTTagCompound.getInteger("color");
        }
        return 10511680;
    }

    public boolean hasColor(ItemStack itemStack) {
        return this.material != ArmorMaterial.LEATHER ? false : (!itemStack.hasTagCompound() ? false : (!itemStack.getTagCompound().hasKey("display", 10) ? false : itemStack.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack itemStack2) {
        return this.material.getRepairItem() == itemStack2.getItem() ? true : super.getIsRepairable(itemStack, itemStack2);
    }

    public ArmorMaterial getArmorMaterial() {
        return this.material;
    }

    public ItemArmor(ArmorMaterial armorMaterial, int n, int n2) {
        this.material = armorMaterial;
        this.armorType = n2;
        this.renderIndex = n;
        this.damageReduceAmount = armorMaterial.getDamageReductionAmount(n2);
        this.setMaxDamage(armorMaterial.getDurability(n2));
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabCombat);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserBehavior);
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int n) {
        if (n > 0) {
            return 0xFFFFFF;
        }
        int n2 = this.getColor(itemStack);
        if (n2 < 0) {
            n2 = 0xFFFFFF;
        }
        return n2;
    }

    static {
        maxDamageArray = new int[]{11, 16, 15, 13};
        EMPTY_SLOT_NAMES = new String[]{"minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots"};
        dispenserBehavior = new BehaviorDefaultDispenseItem(){

            @Override
            protected ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                BlockPos blockPos = iBlockSource.getBlockPos().offset(BlockDispenser.getFacing(iBlockSource.getBlockMetadata()));
                int n = blockPos.getX();
                int n2 = blockPos.getY();
                int n3 = blockPos.getZ();
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(n, n2, n3, n + 1, n2 + 1, n3 + 1);
                List<EntityLivingBase> list = iBlockSource.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, axisAlignedBB, Predicates.and(EntitySelectors.NOT_SPECTATING, (Predicate)new EntitySelectors.ArmoredMob(itemStack)));
                if (list.size() > 0) {
                    EntityLivingBase entityLivingBase = list.get(0);
                    int n4 = entityLivingBase instanceof EntityPlayer ? 1 : 0;
                    int n5 = EntityLiving.getArmorPosition(itemStack);
                    ItemStack itemStack2 = itemStack.copy();
                    itemStack2.stackSize = 1;
                    entityLivingBase.setCurrentItemOrArmor(n5 - n4, itemStack2);
                    if (entityLivingBase instanceof EntityLiving) {
                        ((EntityLiving)entityLivingBase).setEquipmentDropChance(n5, 2.0f);
                    }
                    --itemStack.stackSize;
                    return itemStack;
                }
                return super.dispenseStack(iBlockSource, itemStack);
            }
        };
    }

    public void removeColor(ItemStack itemStack) {
        NBTTagCompound nBTTagCompound;
        NBTTagCompound nBTTagCompound2;
        if (this.material == ArmorMaterial.LEATHER && (nBTTagCompound2 = itemStack.getTagCompound()) != null && (nBTTagCompound = nBTTagCompound2.getCompoundTag("display")).hasKey("color")) {
            nBTTagCompound.removeTag("color");
        }
    }

    public static enum ArmorMaterial {
        LEATHER("leather", 5, new int[]{1, 3, 2, 1}, 15),
        CHAIN("chainmail", 15, new int[]{2, 5, 4, 1}, 12),
        IRON("iron", 15, new int[]{2, 6, 5, 2}, 9),
        GOLD("gold", 7, new int[]{2, 5, 3, 1}, 25),
        DIAMOND("diamond", 33, new int[]{3, 8, 6, 3}, 10);

        private final int maxDamageFactor;
        private final String name;
        private final int[] damageReductionAmountArray;
        private final int enchantability;

        public Item getRepairItem() {
            return this == LEATHER ? Items.leather : (this == CHAIN ? Items.iron_ingot : (this == GOLD ? Items.gold_ingot : (this == IRON ? Items.iron_ingot : (this == DIAMOND ? Items.diamond : null))));
        }

        public String getName() {
            return this.name;
        }

        public int getDamageReductionAmount(int n) {
            return this.damageReductionAmountArray[n];
        }

        public int getDurability(int n) {
            return maxDamageArray[n] * this.maxDamageFactor;
        }

        private ArmorMaterial(String string2, int n2, int[] nArray, int n3) {
            this.name = string2;
            this.maxDamageFactor = n2;
            this.damageReductionAmountArray = nArray;
            this.enchantability = n3;
        }

        public int getEnchantability() {
            return this.enchantability;
        }
    }
}

