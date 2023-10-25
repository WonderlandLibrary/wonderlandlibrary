package net.minecraft.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemArmor
extends Item {
    private static final int[] maxDamageArray = new int[]{11, 16, 15, 13};
    public static final String[] EMPTY_SLOT_NAMES = new String[]{"minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots"};
    private static final IBehaviorDispenseItem dispenserBehavior = new /* Unavailable Anonymous Inner Class!! */;
    public final int armorType;
    public final int damageReduceAmount;
    public final int renderIndex;
    private final ArmorMaterial material;

    public ItemArmor(ArmorMaterial material, int renderIndex, int armorType) {
        this.material = material;
        this.armorType = armorType;
        this.renderIndex = renderIndex;
        this.damageReduceAmount = material.getDamageReductionAmount(armorType);
        this.setMaxDamage(material.getDurability(armorType));
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabCombat);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)this, (Object)dispenserBehavior);
    }

    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (renderPass > 0) {
            return 0xFFFFFF;
        }
        int i = this.getColor(stack);
        if (i < 0) {
            i = 0xFFFFFF;
        }
        return i;
    }

    public int getItemEnchantability() {
        return this.material.getEnchantability();
    }

    public ArmorMaterial getArmorMaterial() {
        return this.material;
    }

    public boolean hasColor(ItemStack stack) {
        return this.material != ArmorMaterial.LEATHER ? false : (!stack.hasTagCompound() ? false : (!stack.getTagCompound().hasKey("display", 10) ? false : stack.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
    }

    public int getColor(ItemStack stack) {
        NBTTagCompound nbttagcompound1;
        if (this.material != ArmorMaterial.LEATHER) {
            return -1;
        }
        NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (nbttagcompound != null && (nbttagcompound1 = nbttagcompound.getCompoundTag("display")) != null && nbttagcompound1.hasKey("color", 3)) {
            return nbttagcompound1.getInteger("color");
        }
        return 10511680;
    }

    public void removeColor(ItemStack stack) {
        NBTTagCompound nbttagcompound1;
        NBTTagCompound nbttagcompound;
        if (this.material == ArmorMaterial.LEATHER && (nbttagcompound = stack.getTagCompound()) != null && (nbttagcompound1 = nbttagcompound.getCompoundTag("display")).hasKey("color")) {
            nbttagcompound1.removeTag("color");
        }
    }

    public void setColor(ItemStack stack, int color) {
        if (this.material != ArmorMaterial.LEATHER) {
            throw new UnsupportedOperationException("Can't dye non-leather!");
        }
        NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (nbttagcompound == null) {
            nbttagcompound = new NBTTagCompound();
            stack.setTagCompound(nbttagcompound);
        }
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
        if (!nbttagcompound.hasKey("display", 10)) {
            nbttagcompound.setTag("display", (NBTBase)nbttagcompound1);
        }
        nbttagcompound1.setInteger("color", color);
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return this.material.getRepairItem() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        int i = EntityLiving.getArmorPosition((ItemStack)itemStackIn) - 1;
        ItemStack itemstack = playerIn.getCurrentArmor(i);
        if (itemstack == null) {
            playerIn.setCurrentItemOrArmor(i, itemStackIn.copy());
            itemStackIn.stackSize = 0;
        }
        return itemStackIn;
    }

    static /* synthetic */ int[] access$000() {
        return maxDamageArray;
    }
}
