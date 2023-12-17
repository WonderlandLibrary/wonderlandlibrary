// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import javax.annotation.Nullable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.block.SoundType;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;
import java.util.UUID;

public class EntityHorse extends AbstractHorse
{
    private static final UUID ARMOR_MODIFIER_UUID;
    private static final DataParameter<Integer> HORSE_VARIANT;
    private static final DataParameter<Integer> HORSE_ARMOR;
    private static final String[] HORSE_TEXTURES;
    private static final String[] HORSE_TEXTURES_ABBR;
    private static final String[] HORSE_MARKING_TEXTURES;
    private static final String[] HORSE_MARKING_TEXTURES_ABBR;
    private String texturePrefix;
    private final String[] horseTexturesArray;
    
    public EntityHorse(final World worldIn) {
        super(worldIn);
        this.horseTexturesArray = new String[3];
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityHorse.HORSE_VARIANT, 0);
        this.dataManager.register(EntityHorse.HORSE_ARMOR, HorseArmorType.NONE.getOrdinal());
    }
    
    public static void registerFixesHorse(final DataFixer fixer) {
        AbstractHorse.registerFixesAbstractHorse(fixer, EntityHorse.class);
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityHorse.class, new String[] { "ArmorItem" }));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getHorseVariant());
        if (!this.horseChest.getStackInSlot(1).isEmpty()) {
            compound.setTag("ArmorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setHorseVariant(compound.getInteger("Variant"));
        if (compound.hasKey("ArmorItem", 10)) {
            final ItemStack itemstack = new ItemStack(compound.getCompoundTag("ArmorItem"));
            if (!itemstack.isEmpty() && HorseArmorType.isHorseArmor(itemstack.getItem())) {
                this.horseChest.setInventorySlotContents(1, itemstack);
            }
        }
        this.updateHorseSlots();
    }
    
    public void setHorseVariant(final int variant) {
        this.dataManager.set(EntityHorse.HORSE_VARIANT, variant);
        this.resetTexturePrefix();
    }
    
    public int getHorseVariant() {
        return this.dataManager.get(EntityHorse.HORSE_VARIANT);
    }
    
    private void resetTexturePrefix() {
        this.texturePrefix = null;
    }
    
    private void setHorseTexturePaths() {
        final int i = this.getHorseVariant();
        final int j = (i & 0xFF) % 7;
        final int k = ((i & 0xFF00) >> 8) % 5;
        final HorseArmorType horsearmortype = this.getHorseArmorType();
        this.horseTexturesArray[0] = EntityHorse.HORSE_TEXTURES[j];
        this.horseTexturesArray[1] = EntityHorse.HORSE_MARKING_TEXTURES[k];
        this.horseTexturesArray[2] = horsearmortype.getTextureName();
        this.texturePrefix = "horse/" + EntityHorse.HORSE_TEXTURES_ABBR[j] + EntityHorse.HORSE_MARKING_TEXTURES_ABBR[k] + horsearmortype.getHash();
    }
    
    public String getHorseTexture() {
        if (this.texturePrefix == null) {
            this.setHorseTexturePaths();
        }
        return this.texturePrefix;
    }
    
    public String[] getVariantTexturePaths() {
        if (this.texturePrefix == null) {
            this.setHorseTexturePaths();
        }
        return this.horseTexturesArray;
    }
    
    @Override
    protected void updateHorseSlots() {
        super.updateHorseSlots();
        this.setHorseArmorStack(this.horseChest.getStackInSlot(1));
    }
    
    public void setHorseArmorStack(final ItemStack itemStackIn) {
        final HorseArmorType horsearmortype = HorseArmorType.getByItemStack(itemStackIn);
        this.dataManager.set(EntityHorse.HORSE_ARMOR, horsearmortype.getOrdinal());
        this.resetTexturePrefix();
        if (!this.world.isRemote) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(EntityHorse.ARMOR_MODIFIER_UUID);
            final int i = horsearmortype.getProtection();
            if (i != 0) {
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(new AttributeModifier(EntityHorse.ARMOR_MODIFIER_UUID, "Horse armor bonus", i, 0).setSaved(false));
            }
        }
    }
    
    public HorseArmorType getHorseArmorType() {
        return HorseArmorType.getByOrdinal(this.dataManager.get(EntityHorse.HORSE_ARMOR));
    }
    
    @Override
    public void onInventoryChanged(final IInventory invBasic) {
        final HorseArmorType horsearmortype = this.getHorseArmorType();
        super.onInventoryChanged(invBasic);
        final HorseArmorType horsearmortype2 = this.getHorseArmorType();
        if (this.ticksExisted > 20 && horsearmortype != horsearmortype2 && horsearmortype2 != HorseArmorType.NONE) {
            this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5f, 1.0f);
        }
    }
    
    @Override
    protected void playGallopSound(final SoundType p_190680_1_) {
        super.playGallopSound(p_190680_1_);
        if (this.rand.nextInt(10) == 0) {
            this.playSound(SoundEvents.ENTITY_HORSE_BREATHE, p_190680_1_.getVolume() * 0.6f, p_190680_1_.getPitch());
        }
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getModifiedMaxHealth());
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getModifiedMovementSpeed());
        this.getEntityAttribute(EntityHorse.JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.resetTexturePrefix();
        }
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.ENTITY_HORSE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ENTITY_HORSE_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        super.getHurtSound(damageSourceIn);
        return SoundEvents.ENTITY_HORSE_HURT;
    }
    
    @Override
    protected SoundEvent getAngrySound() {
        super.getAngrySound();
        return SoundEvents.ENTITY_HORSE_ANGRY;
    }
    
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_HORSE;
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        final boolean flag = !itemstack.isEmpty();
        if (flag && itemstack.getItem() == Items.SPAWN_EGG) {
            return super.processInteract(player, hand);
        }
        if (!this.isChild()) {
            if (this.isTame() && player.isSneaking()) {
                this.openGUI(player);
                return true;
            }
            if (this.isBeingRidden()) {
                return super.processInteract(player, hand);
            }
        }
        if (flag) {
            if (this.handleEating(player, itemstack)) {
                if (!player.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                return true;
            }
            if (itemstack.interactWithEntity(player, this, hand)) {
                return true;
            }
            if (!this.isTame()) {
                this.makeMad();
                return true;
            }
            final boolean flag2 = HorseArmorType.getByItemStack(itemstack) != HorseArmorType.NONE;
            final boolean flag3 = !this.isChild() && !this.isHorseSaddled() && itemstack.getItem() == Items.SADDLE;
            if (flag2 || flag3) {
                this.openGUI(player);
                return true;
            }
        }
        if (this.isChild()) {
            return super.processInteract(player, hand);
        }
        this.mountTo(player);
        return true;
    }
    
    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        return otherAnimal != this && (otherAnimal instanceof EntityDonkey || otherAnimal instanceof EntityHorse) && this.canMate() && ((AbstractHorse)otherAnimal).canMate();
    }
    
    @Override
    public EntityAgeable createChild(final EntityAgeable ageable) {
        AbstractHorse abstracthorse;
        if (ageable instanceof EntityDonkey) {
            abstracthorse = new EntityMule(this.world);
        }
        else {
            final EntityHorse entityhorse = (EntityHorse)ageable;
            abstracthorse = new EntityHorse(this.world);
            final int j = this.rand.nextInt(9);
            int i;
            if (j < 4) {
                i = (this.getHorseVariant() & 0xFF);
            }
            else if (j < 8) {
                i = (entityhorse.getHorseVariant() & 0xFF);
            }
            else {
                i = this.rand.nextInt(7);
            }
            final int k = this.rand.nextInt(5);
            if (k < 2) {
                i |= (this.getHorseVariant() & 0xFF00);
            }
            else if (k < 4) {
                i |= (entityhorse.getHorseVariant() & 0xFF00);
            }
            else {
                i |= (this.rand.nextInt(5) << 8 & 0xFF00);
            }
            ((EntityHorse)abstracthorse).setHorseVariant(i);
        }
        this.setOffspringAttributes(ageable, abstracthorse);
        return abstracthorse;
    }
    
    @Override
    public boolean wearsArmor() {
        return true;
    }
    
    @Override
    public boolean isArmor(final ItemStack stack) {
        return HorseArmorType.isHorseArmor(stack.getItem());
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        int i;
        if (livingdata instanceof GroupData) {
            i = ((GroupData)livingdata).variant;
        }
        else {
            i = this.rand.nextInt(7);
            livingdata = new GroupData(i);
        }
        this.setHorseVariant(i | this.rand.nextInt(5) << 8);
        return livingdata;
    }
    
    static {
        ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
        HORSE_VARIANT = EntityDataManager.createKey(EntityHorse.class, DataSerializers.VARINT);
        HORSE_ARMOR = EntityDataManager.createKey(EntityHorse.class, DataSerializers.VARINT);
        HORSE_TEXTURES = new String[] { "textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png" };
        HORSE_TEXTURES_ABBR = new String[] { "hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb" };
        HORSE_MARKING_TEXTURES = new String[] { null, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png" };
        HORSE_MARKING_TEXTURES_ABBR = new String[] { "", "wo_", "wmo", "wdo", "bdo" };
    }
    
    public static class GroupData implements IEntityLivingData
    {
        public int variant;
        
        public GroupData(final int variantIn) {
            this.variant = variantIn;
        }
    }
}
