// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import com.google.common.collect.Maps;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.entity.EntityAgeable;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.item.EnumDyeColor;
import java.util.Map;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.network.datasync.DataParameter;

public class EntitySheep extends EntityAnimal
{
    private static final DataParameter<Byte> DYE_COLOR;
    private final InventoryCrafting inventoryCrafting;
    private static final Map<EnumDyeColor, float[]> DYE_TO_RGB;
    private int sheepTimer;
    private EntityAIEatGrass entityAIEatGrass;
    
    private static float[] createSheepColor(final EnumDyeColor p_192020_0_) {
        final float[] afloat = p_192020_0_.getColorComponentValues();
        final float f = 0.75f;
        return new float[] { afloat[0] * 0.75f, afloat[1] * 0.75f, afloat[2] * 0.75f };
    }
    
    public static float[] getDyeRgb(final EnumDyeColor dyeColor) {
        return EntitySheep.DYE_TO_RGB.get(dyeColor);
    }
    
    public EntitySheep(final World worldIn) {
        super(worldIn);
        this.inventoryCrafting = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(final EntityPlayer playerIn) {
                return false;
            }
        }, 2, 1);
        this.setSize(0.9f, 1.3f);
        this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.DYE));
        this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.DYE));
    }
    
    @Override
    protected void initEntityAI() {
        this.entityAIEatGrass = new EntityAIEatGrass(this);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0));
        this.tasks.addTask(3, new EntityAITempt(this, 1.1, Items.WHEAT, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1));
        this.tasks.addTask(5, this.entityAIEatGrass);
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }
    
    @Override
    protected void updateAITasks() {
        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }
        super.onLivingUpdate();
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntitySheep.DYE_COLOR, (Byte)0);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        if (this.getSheared()) {
            return LootTableList.ENTITIES_SHEEP;
        }
        switch (this.getFleeceColor()) {
            default: {
                return LootTableList.ENTITIES_SHEEP_WHITE;
            }
            case ORANGE: {
                return LootTableList.ENTITIES_SHEEP_ORANGE;
            }
            case MAGENTA: {
                return LootTableList.ENTITIES_SHEEP_MAGENTA;
            }
            case LIGHT_BLUE: {
                return LootTableList.ENTITIES_SHEEP_LIGHT_BLUE;
            }
            case YELLOW: {
                return LootTableList.ENTITIES_SHEEP_YELLOW;
            }
            case LIME: {
                return LootTableList.ENTITIES_SHEEP_LIME;
            }
            case PINK: {
                return LootTableList.ENTITIES_SHEEP_PINK;
            }
            case GRAY: {
                return LootTableList.ENTITIES_SHEEP_GRAY;
            }
            case SILVER: {
                return LootTableList.ENTITIES_SHEEP_SILVER;
            }
            case CYAN: {
                return LootTableList.ENTITIES_SHEEP_CYAN;
            }
            case PURPLE: {
                return LootTableList.ENTITIES_SHEEP_PURPLE;
            }
            case BLUE: {
                return LootTableList.ENTITIES_SHEEP_BLUE;
            }
            case BROWN: {
                return LootTableList.ENTITIES_SHEEP_BROWN;
            }
            case GREEN: {
                return LootTableList.ENTITIES_SHEEP_GREEN;
            }
            case RED: {
                return LootTableList.ENTITIES_SHEEP_RED;
            }
            case BLACK: {
                return LootTableList.ENTITIES_SHEEP_BLACK;
            }
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 10) {
            this.sheepTimer = 40;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    public float getHeadRotationPointY(final float p_70894_1_) {
        if (this.sheepTimer <= 0) {
            return 0.0f;
        }
        if (this.sheepTimer >= 4 && this.sheepTimer <= 36) {
            return 1.0f;
        }
        return (this.sheepTimer < 4) ? ((this.sheepTimer - p_70894_1_) / 4.0f) : (-(this.sheepTimer - 40 - p_70894_1_) / 4.0f);
    }
    
    public float getHeadRotationAngleX(final float p_70890_1_) {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
            final float f = (this.sheepTimer - 4 - p_70890_1_) / 32.0f;
            return 0.62831855f + 0.2199115f * MathHelper.sin(f * 28.7f);
        }
        return (this.sheepTimer > 0) ? 0.62831855f : (this.rotationPitch * 0.017453292f);
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.SHEARS && !this.getSheared() && !this.isChild()) {
            if (!this.world.isRemote) {
                this.setSheared(true);
                for (int i = 1 + this.rand.nextInt(3), j = 0; j < i; ++j) {
                    final EntityItem entityDropItem;
                    final EntityItem entityitem = entityDropItem = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, this.getFleeceColor().getMetadata()), 1.0f);
                    entityDropItem.motionY += this.rand.nextFloat() * 0.05f;
                    final EntityItem entityItem = entityitem;
                    entityItem.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f;
                    final EntityItem entityItem2 = entityitem;
                    entityItem2.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f;
                }
            }
            itemstack.damageItem(1, player);
            this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);
        }
        return super.processInteract(player, hand);
    }
    
    public static void registerFixesSheep(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntitySheep.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Sheared", this.getSheared());
        compound.setByte("Color", (byte)this.getFleeceColor().getMetadata());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setSheared(compound.getBoolean("Sheared"));
        this.setFleeceColor(EnumDyeColor.byMetadata(compound.getByte("Color")));
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15f, 1.0f);
    }
    
    public EnumDyeColor getFleeceColor() {
        return EnumDyeColor.byMetadata(this.dataManager.get(EntitySheep.DYE_COLOR) & 0xF);
    }
    
    public void setFleeceColor(final EnumDyeColor color) {
        final byte b0 = this.dataManager.get(EntitySheep.DYE_COLOR);
        this.dataManager.set(EntitySheep.DYE_COLOR, (byte)((b0 & 0xF0) | (color.getMetadata() & 0xF)));
    }
    
    public boolean getSheared() {
        return (this.dataManager.get(EntitySheep.DYE_COLOR) & 0x10) != 0x0;
    }
    
    public void setSheared(final boolean sheared) {
        final byte b0 = this.dataManager.get(EntitySheep.DYE_COLOR);
        if (sheared) {
            this.dataManager.set(EntitySheep.DYE_COLOR, (byte)(b0 | 0x10));
        }
        else {
            this.dataManager.set(EntitySheep.DYE_COLOR, (byte)(b0 & 0xFFFFFFEF));
        }
    }
    
    public static EnumDyeColor getRandomSheepColor(final Random random) {
        final int i = random.nextInt(100);
        if (i < 5) {
            return EnumDyeColor.BLACK;
        }
        if (i < 10) {
            return EnumDyeColor.GRAY;
        }
        if (i < 15) {
            return EnumDyeColor.SILVER;
        }
        if (i < 18) {
            return EnumDyeColor.BROWN;
        }
        return (random.nextInt(500) == 0) ? EnumDyeColor.PINK : EnumDyeColor.WHITE;
    }
    
    @Override
    public EntitySheep createChild(final EntityAgeable ageable) {
        final EntitySheep entitysheep = (EntitySheep)ageable;
        final EntitySheep entitysheep2 = new EntitySheep(this.world);
        entitysheep2.setFleeceColor(this.getDyeColorMixFromParents(this, entitysheep));
        return entitysheep2;
    }
    
    @Override
    public void eatGrassBonus() {
        this.setSheared(false);
        if (this.isChild()) {
            this.addGrowth(60);
        }
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setFleeceColor(getRandomSheepColor(this.world.rand));
        return livingdata;
    }
    
    private EnumDyeColor getDyeColorMixFromParents(final EntityAnimal father, final EntityAnimal mother) {
        final int i = ((EntitySheep)father).getFleeceColor().getDyeDamage();
        final int j = ((EntitySheep)mother).getFleeceColor().getDyeDamage();
        this.inventoryCrafting.getStackInSlot(0).setItemDamage(i);
        this.inventoryCrafting.getStackInSlot(1).setItemDamage(j);
        final ItemStack itemstack = CraftingManager.findMatchingResult(this.inventoryCrafting, ((EntitySheep)father).world);
        int k;
        if (itemstack.getItem() == Items.DYE) {
            k = itemstack.getMetadata();
        }
        else {
            k = (this.world.rand.nextBoolean() ? i : j);
        }
        return EnumDyeColor.byDyeDamage(k);
    }
    
    @Override
    public float getEyeHeight() {
        return 0.95f * this.height;
    }
    
    static {
        DYE_COLOR = EntityDataManager.createKey(EntitySheep.class, DataSerializers.BYTE);
        DYE_TO_RGB = Maps.newEnumMap((Class)EnumDyeColor.class);
        for (final EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            EntitySheep.DYE_TO_RGB.put(enumdyecolor, createSheepColor(enumdyecolor));
        }
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[] { 0.9019608f, 0.9019608f, 0.9019608f });
    }
}
