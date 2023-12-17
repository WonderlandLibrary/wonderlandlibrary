// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.init.SoundEvents;
import net.minecraft.init.Items;
import net.minecraft.util.SoundEvent;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import com.google.common.collect.Multimap;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.creativetab.CreativeTabs;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLiving;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import java.util.UUID;

public class ItemArmor extends Item
{
    private static final int[] MAX_DAMAGE_ARRAY;
    private static final UUID[] ARMOR_MODIFIERS;
    public static final String[] EMPTY_SLOT_NAMES;
    public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR;
    public final EntityEquipmentSlot armorType;
    public final int damageReduceAmount;
    public final float toughness;
    public final int renderIndex;
    private final ArmorMaterial material;
    
    public static ItemStack dispenseArmor(final IBlockSource blockSource, final ItemStack stack) {
        final BlockPos blockpos = blockSource.getBlockPos().offset(blockSource.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING));
        final List<EntityLivingBase> list = blockSource.getWorld().getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, new AxisAlignedBB(blockpos), (com.google.common.base.Predicate<? super EntityLivingBase>)Predicates.and((Predicate)EntitySelectors.NOT_SPECTATING, (Predicate)new EntitySelectors.ArmoredMob(stack)));
        if (list.isEmpty()) {
            return ItemStack.EMPTY;
        }
        final EntityLivingBase entitylivingbase = list.get(0);
        final EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(stack);
        final ItemStack itemstack = stack.splitStack(1);
        entitylivingbase.setItemStackToSlot(entityequipmentslot, itemstack);
        if (entitylivingbase instanceof EntityLiving) {
            ((EntityLiving)entitylivingbase).setDropChance(entityequipmentslot, 2.0f);
        }
        return stack;
    }
    
    public ItemArmor(final ArmorMaterial materialIn, final int renderIndexIn, final EntityEquipmentSlot equipmentSlotIn) {
        this.material = materialIn;
        this.armorType = equipmentSlotIn;
        this.renderIndex = renderIndexIn;
        this.damageReduceAmount = materialIn.getDamageReductionAmount(equipmentSlotIn);
        this.setMaxDamage(materialIn.getDurability(equipmentSlotIn));
        this.toughness = materialIn.getToughness();
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.COMBAT);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
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
    
    public boolean hasColor(final ItemStack stack) {
        if (this.material != ArmorMaterial.LEATHER) {
            return false;
        }
        final NBTTagCompound nbttagcompound = stack.getTagCompound();
        return nbttagcompound != null && nbttagcompound.hasKey("display", 10) && nbttagcompound.getCompoundTag("display").hasKey("color", 3);
    }
    
    public int getColor(final ItemStack stack) {
        if (this.material != ArmorMaterial.LEATHER) {
            return 16777215;
        }
        final NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (nbttagcompound != null) {
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("display");
            if (nbttagcompound2 != null && nbttagcompound2.hasKey("color", 3)) {
                return nbttagcompound2.getInteger("color");
            }
        }
        return 10511680;
    }
    
    public void removeColor(final ItemStack stack) {
        if (this.material == ArmorMaterial.LEATHER) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            if (nbttagcompound != null) {
                final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("display");
                if (nbttagcompound2.hasKey("color")) {
                    nbttagcompound2.removeTag("color");
                }
            }
        }
    }
    
    public void setColor(final ItemStack stack, final int color) {
        if (this.material != ArmorMaterial.LEATHER) {
            throw new UnsupportedOperationException("Can't dye non-leather!");
        }
        NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (nbttagcompound == null) {
            nbttagcompound = new NBTTagCompound();
            stack.setTagCompound(nbttagcompound);
        }
        final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("display");
        if (!nbttagcompound.hasKey("display", 10)) {
            nbttagcompound.setTag("display", nbttagcompound2);
        }
        nbttagcompound2.setInteger("color", color);
    }
    
    @Override
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return this.material.getRepairItem() == repair.getItem() || super.getIsRepairable(toRepair, repair);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        final EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        final ItemStack itemstack2 = playerIn.getItemStackFromSlot(entityequipmentslot);
        if (itemstack2.isEmpty()) {
            playerIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            itemstack.setCount(0);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
    
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(final EntityEquipmentSlot equipmentSlot) {
        final Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == this.armorType) {
            multimap.put((Object)SharedMonsterAttributes.ARMOR.getName(), (Object)new AttributeModifier(ItemArmor.ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", this.damageReduceAmount, 0));
            multimap.put((Object)SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), (Object)new AttributeModifier(ItemArmor.ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", this.toughness, 0));
        }
        return multimap;
    }
    
    static {
        MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
        ARMOR_MODIFIERS = new UUID[] { UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150") };
        EMPTY_SLOT_NAMES = new String[] { "minecraft:items/empty_armor_slot_boots", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_helmet" };
        DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final ItemStack itemstack = ItemArmor.dispenseArmor(source, stack);
                return itemstack.isEmpty() ? super.dispenseStack(source, stack) : itemstack;
            }
        };
    }
    
    public enum ArmorMaterial
    {
        LEATHER("leather", 5, new int[] { 1, 2, 3, 1 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f), 
        CHAIN("chainmail", 15, new int[] { 1, 4, 5, 2 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0.0f), 
        IRON("iron", 15, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0f), 
        GOLD("gold", 7, new int[] { 1, 3, 5, 2 }, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0f), 
        DIAMOND("diamond", 33, new int[] { 3, 6, 8, 3 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0f);
        
        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;
        private final SoundEvent soundEvent;
        private final float toughness;
        
        private ArmorMaterial(final String nameIn, final int maxDamageFactorIn, final int[] damageReductionAmountArrayIn, final int enchantabilityIn, final SoundEvent soundEventIn, final float toughnessIn) {
            this.name = nameIn;
            this.maxDamageFactor = maxDamageFactorIn;
            this.damageReductionAmountArray = damageReductionAmountArrayIn;
            this.enchantability = enchantabilityIn;
            this.soundEvent = soundEventIn;
            this.toughness = toughnessIn;
        }
        
        public int getDurability(final EntityEquipmentSlot armorType) {
            return ItemArmor.MAX_DAMAGE_ARRAY[armorType.getIndex()] * this.maxDamageFactor;
        }
        
        public int getDamageReductionAmount(final EntityEquipmentSlot armorType) {
            return this.damageReductionAmountArray[armorType.getIndex()];
        }
        
        public int getEnchantability() {
            return this.enchantability;
        }
        
        public SoundEvent getSoundEvent() {
            return this.soundEvent;
        }
        
        public Item getRepairItem() {
            if (this == ArmorMaterial.LEATHER) {
                return Items.LEATHER;
            }
            if (this == ArmorMaterial.CHAIN) {
                return Items.IRON_INGOT;
            }
            if (this == ArmorMaterial.GOLD) {
                return Items.GOLD_INGOT;
            }
            if (this == ArmorMaterial.IRON) {
                return Items.IRON_INGOT;
            }
            return (this == ArmorMaterial.DIAMOND) ? Items.DIAMOND : null;
        }
        
        public String getName() {
            return this.name;
        }
        
        public float getToughness() {
            return this.toughness;
        }
    }
}
