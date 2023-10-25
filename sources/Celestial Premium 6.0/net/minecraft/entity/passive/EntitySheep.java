/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.passive;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySheep
extends EntityAnimal {
    private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntitySheep.class, DataSerializers.BYTE);
    private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container(){

        @Override
        public boolean canInteractWith(EntityPlayer playerIn) {
            return false;
        }
    }, 2, 1);
    private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
    private int sheepTimer;
    private EntityAIEatGrass entityAIEatGrass;

    private static float[] func_192020_c(EnumDyeColor p_192020_0_) {
        float[] afloat = p_192020_0_.func_193349_f();
        float f = 0.75f;
        return new float[]{afloat[0] * 0.75f, afloat[1] * 0.75f, afloat[2] * 0.75f};
    }

    public static float[] getDyeRgb(EnumDyeColor dyeColor) {
        return DYE_TO_RGB.get(dyeColor);
    }

    public EntitySheep(World worldIn) {
        super(worldIn);
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
        this.tasks.addTask(3, new EntityAITempt((EntityCreature)this, 1.1, Items.WHEAT, false));
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
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(DYE_COLOR, (byte)0);
    }

    @Override
    @Nullable
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
            case BLACK: 
        }
        return LootTableList.ENTITIES_SHEEP_BLACK;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 10) {
            this.sheepTimer = 40;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public float getHeadRotationPointY(float p_70894_1_) {
        if (this.sheepTimer <= 0) {
            return 0.0f;
        }
        if (this.sheepTimer >= 4 && this.sheepTimer <= 36) {
            return 1.0f;
        }
        return this.sheepTimer < 4 ? ((float)this.sheepTimer - p_70894_1_) / 4.0f : -((float)(this.sheepTimer - 40) - p_70894_1_) / 4.0f;
    }

    public float getHeadRotationAngleX(float p_70890_1_) {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
            float f = ((float)(this.sheepTimer - 4) - p_70890_1_) / 32.0f;
            return 0.62831855f + 0.2199115f * MathHelper.sin(f * 28.7f);
        }
        return this.sheepTimer > 0 ? 0.62831855f : this.rotationPitch * ((float)Math.PI / 180);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.SHEARS && !this.getSheared() && !this.isChild()) {
            if (!this.world.isRemote) {
                this.setSheared(true);
                int i = 1 + this.rand.nextInt(3);
                for (int j = 0; j < i; ++j) {
                    EntityItem entityitem = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, this.getFleeceColor().getMetadata()), 1.0f);
                    entityitem.motionY += (double)(this.rand.nextFloat() * 0.05f);
                    entityitem.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f);
                    entityitem.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f);
                }
            }
            itemstack.damageItem(1, player);
            this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);
        }
        return super.processInteract(player, hand);
    }

    public static void registerFixesSheep(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntitySheep.class);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Sheared", this.getSheared());
        compound.setByte("Color", (byte)this.getFleeceColor().getMetadata());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setSheared(compound.getBoolean("Sheared"));
        this.setFleeceColor(EnumDyeColor.byMetadata(compound.getByte("Color")));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15f, 1.0f);
    }

    public EnumDyeColor getFleeceColor() {
        return EnumDyeColor.byMetadata(this.dataManager.get(DYE_COLOR) & 0xF);
    }

    public void setFleeceColor(EnumDyeColor color) {
        byte b0 = this.dataManager.get(DYE_COLOR);
        this.dataManager.set(DYE_COLOR, (byte)(b0 & 0xF0 | color.getMetadata() & 0xF));
    }

    public boolean getSheared() {
        return (this.dataManager.get(DYE_COLOR) & 0x10) != 0;
    }

    public void setSheared(boolean sheared) {
        byte b0 = this.dataManager.get(DYE_COLOR);
        if (sheared) {
            this.dataManager.set(DYE_COLOR, (byte)(b0 | 0x10));
        } else {
            this.dataManager.set(DYE_COLOR, (byte)(b0 & 0xFFFFFFEF));
        }
    }

    public static EnumDyeColor getRandomSheepColor(Random random) {
        int i = random.nextInt(100);
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
        return random.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE;
    }

    @Override
    public EntitySheep createChild(EntityAgeable ageable) {
        EntitySheep entitysheep = (EntitySheep)ageable;
        EntitySheep entitysheep1 = new EntitySheep(this.world);
        entitysheep1.setFleeceColor(this.getDyeColorMixFromParents(this, entitysheep));
        return entitysheep1;
    }

    @Override
    public void eatGrassBonus() {
        this.setSheared(false);
        if (this.isChild()) {
            this.addGrowth(60);
        }
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setFleeceColor(EntitySheep.getRandomSheepColor(this.world.rand));
        return livingdata;
    }

    private EnumDyeColor getDyeColorMixFromParents(EntityAnimal father, EntityAnimal mother) {
        int i = ((EntitySheep)father).getFleeceColor().getDyeDamage();
        int j = ((EntitySheep)mother).getFleeceColor().getDyeDamage();
        this.inventoryCrafting.getStackInSlot(0).setItemDamage(i);
        this.inventoryCrafting.getStackInSlot(1).setItemDamage(j);
        ItemStack itemstack = CraftingManager.findMatchingRecipe(this.inventoryCrafting, ((EntitySheep)father).world);
        int k = itemstack.getItem() == Items.DYE ? itemstack.getMetadata() : (this.world.rand.nextBoolean() ? i : j);
        return EnumDyeColor.byDyeDamage(k);
    }

    @Override
    public float getEyeHeight() {
        return 0.95f * this.height;
    }

    static {
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            DYE_TO_RGB.put(enumdyecolor, EntitySheep.func_192020_c(enumdyecolor));
        }
        DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[]{0.9019608f, 0.9019608f, 0.9019608f});
    }
}

