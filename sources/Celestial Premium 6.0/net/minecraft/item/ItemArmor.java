/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import com.google.common.base.Predicates;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemArmor
extends Item {
    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    public static final String[] EMPTY_SLOT_NAMES = new String[]{"minecraft:items/empty_armor_slot_boots", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_helmet"};
    public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem(){

        @Override
        protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
            ItemStack itemstack = ItemArmor.dispenseArmor(source, stack);
            return itemstack.isEmpty() ? super.dispenseStack(source, stack) : itemstack;
        }
    };
    public final EntityEquipmentSlot armorType;
    public final int damageReduceAmount;
    public final float toughness;
    public final int renderIndex;
    private final ArmorMaterial material;

    public static ItemStack dispenseArmor(IBlockSource blockSource, ItemStack stack) {
        BlockPos blockpos = blockSource.getBlockPos().offset(blockSource.getBlockState().getValue(BlockDispenser.FACING));
        List<Entity> list = blockSource.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(blockpos), Predicates.and(EntitySelectors.NOT_SPECTATING, new EntitySelectors.ArmoredMob(stack)));
        if (list.isEmpty()) {
            return ItemStack.field_190927_a;
        }
        EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(0);
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(stack);
        ItemStack itemstack = stack.splitStack(1);
        entitylivingbase.setItemStackToSlot(entityequipmentslot, itemstack);
        if (entitylivingbase instanceof EntityLiving) {
            ((EntityLiving)entitylivingbase).setDropChance(entityequipmentslot, 2.0f);
        }
        return stack;
    }

    public ItemArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        this.material = materialIn;
        this.armorType = equipmentSlotIn;
        this.renderIndex = renderIndexIn;
        this.damageReduceAmount = materialIn.getDamageReductionAmount(equipmentSlotIn);
        this.setMaxDamage(materialIn.getDurability(equipmentSlotIn));
        this.toughness = materialIn.getToughness();
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.COMBAT);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DISPENSER_BEHAVIOR);
    }

    public EntityEquipmentSlot getEquipmentSlot() {
        return this.armorType;
    }

    @Override
    public int getItemEnchantability() {
        return this.material.getEnchantability();
    }

    public ArmorMaterial getArmorMaterial() {
        return this.material;
    }

    public boolean hasColor(ItemStack stack) {
        if (this.material != ArmorMaterial.LEATHER) {
            return false;
        }
        NBTTagCompound nbttagcompound = stack.getTagCompound();
        return nbttagcompound != null && nbttagcompound.hasKey("display", 10) ? nbttagcompound.getCompoundTag("display").hasKey("color", 3) : false;
    }

    public int getColor(ItemStack stack) {
        NBTTagCompound nbttagcompound1;
        if (this.material != ArmorMaterial.LEATHER) {
            return 0xFFFFFF;
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
            nbttagcompound.setTag("display", nbttagcompound1);
        }
        nbttagcompound1.setInteger("color", color);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return this.material.getRepairItem() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
        ItemStack itemstack = worldIn.getHeldItem(playerIn);
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        ItemStack itemstack1 = worldIn.getItemStackFromSlot(entityequipmentslot);
        if (itemstack1.isEmpty()) {
            worldIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            itemstack.func_190920_e(0);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == this.armorType) {
            multimap.put(SharedMonsterAttributes.ARMOR.getAttributeUnlocalizedName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", this.damageReduceAmount, 0));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getAttributeUnlocalizedName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", this.toughness, 0));
        }
        return multimap;
    }

    public static enum ArmorMaterial {
        LEATHER("leather", 5, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f),
        CHAIN("chainmail", 15, new int[]{1, 4, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0.0f),
        IRON("iron", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0f),
        GOLD("gold", 7, new int[]{1, 3, 5, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0f),
        DIAMOND("diamond", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0f);

        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;
        private final SoundEvent soundEvent;
        private final float toughness;

        private ArmorMaterial(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountArrayIn, int enchantabilityIn, SoundEvent soundEventIn, float toughnessIn) {
            this.name = nameIn;
            this.maxDamageFactor = maxDamageFactorIn;
            this.damageReductionAmountArray = damageReductionAmountArrayIn;
            this.enchantability = enchantabilityIn;
            this.soundEvent = soundEventIn;
            this.toughness = toughnessIn;
        }

        public int getDurability(EntityEquipmentSlot armorType) {
            return MAX_DAMAGE_ARRAY[armorType.getIndex()] * this.maxDamageFactor;
        }

        public int getDamageReductionAmount(EntityEquipmentSlot armorType) {
            return this.damageReductionAmountArray[armorType.getIndex()];
        }

        public int getEnchantability() {
            return this.enchantability;
        }

        public SoundEvent getSoundEvent() {
            return this.soundEvent;
        }

        public Item getRepairItem() {
            if (this == LEATHER) {
                return Items.LEATHER;
            }
            if (this == CHAIN) {
                return Items.IRON_INGOT;
            }
            if (this == GOLD) {
                return Items.GOLD_INGOT;
            }
            if (this == IRON) {
                return Items.IRON_INGOT;
            }
            return this == DIAMOND ? Items.DIAMOND : null;
        }

        public String getName() {
            return this.name;
        }

        public float getToughness() {
            return this.toughness;
        }
    }
}

