/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.entity.passive;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySheep
extends EntityAnimal {
    private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container(){

        @Override
        public boolean canInteractWith(EntityPlayer entityPlayer) {
            return false;
        }
    }, 2, 1);
    private int sheepTimer;
    private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
    private EntityAIEatGrass entityAIEatGrass = new EntityAIEatGrass(this);

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setBoolean("Sheared", this.getSheared());
        nBTTagCompound.setByte("Color", (byte)this.getFleeceColor().getMetadata());
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        if (itemStack != null && itemStack.getItem() == Items.shears && !this.getSheared() && !this.isChild()) {
            if (!this.worldObj.isRemote) {
                this.setSheared(true);
                int n = 1 + this.rand.nextInt(3);
                int n2 = 0;
                while (n2 < n) {
                    EntityItem entityItem = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getFleeceColor().getMetadata()), 1.0f);
                    entityItem.motionY += (double)(this.rand.nextFloat() * 0.05f);
                    entityItem.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f);
                    entityItem.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f);
                    ++n2;
                }
            }
            itemStack.damageItem(1, entityPlayer);
            this.playSound("mob.sheep.shear", 1.0f, 1.0f);
        }
        return super.interact(entityPlayer);
    }

    @Override
    public float getEyeHeight() {
        return 0.95f * this.height;
    }

    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }
        super.onLivingUpdate();
    }

    @Override
    protected String getDeathSound() {
        return "mob.sheep.say";
    }

    public void setSheared(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        if (bl) {
            this.dataWatcher.updateObject(16, (byte)(by | 0x10));
        } else {
            this.dataWatcher.updateObject(16, (byte)(by & 0xFFFFFFEF));
        }
    }

    static {
        DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[]{1.0f, 1.0f, 1.0f});
        DYE_TO_RGB.put(EnumDyeColor.ORANGE, new float[]{0.85f, 0.5f, 0.2f});
        DYE_TO_RGB.put(EnumDyeColor.MAGENTA, new float[]{0.7f, 0.3f, 0.85f});
        DYE_TO_RGB.put(EnumDyeColor.LIGHT_BLUE, new float[]{0.4f, 0.6f, 0.85f});
        DYE_TO_RGB.put(EnumDyeColor.YELLOW, new float[]{0.9f, 0.9f, 0.2f});
        DYE_TO_RGB.put(EnumDyeColor.LIME, new float[]{0.5f, 0.8f, 0.1f});
        DYE_TO_RGB.put(EnumDyeColor.PINK, new float[]{0.95f, 0.5f, 0.65f});
        DYE_TO_RGB.put(EnumDyeColor.GRAY, new float[]{0.3f, 0.3f, 0.3f});
        DYE_TO_RGB.put(EnumDyeColor.SILVER, new float[]{0.6f, 0.6f, 0.6f});
        DYE_TO_RGB.put(EnumDyeColor.CYAN, new float[]{0.3f, 0.5f, 0.6f});
        DYE_TO_RGB.put(EnumDyeColor.PURPLE, new float[]{0.5f, 0.25f, 0.7f});
        DYE_TO_RGB.put(EnumDyeColor.BLUE, new float[]{0.2f, 0.3f, 0.7f});
        DYE_TO_RGB.put(EnumDyeColor.BROWN, new float[]{0.4f, 0.3f, 0.2f});
        DYE_TO_RGB.put(EnumDyeColor.GREEN, new float[]{0.4f, 0.5f, 0.2f});
        DYE_TO_RGB.put(EnumDyeColor.RED, new float[]{0.6f, 0.2f, 0.2f});
        DYE_TO_RGB.put(EnumDyeColor.BLACK, new float[]{0.1f, 0.1f, 0.1f});
    }

    public static EnumDyeColor getRandomSheepColor(Random random) {
        int n = random.nextInt(100);
        return n < 5 ? EnumDyeColor.BLACK : (n < 10 ? EnumDyeColor.GRAY : (n < 15 ? EnumDyeColor.SILVER : (n < 18 ? EnumDyeColor.BROWN : (random.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE))));
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        this.playSound("mob.sheep.step", 0.15f, 1.0f);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23f);
    }

    public boolean getSheared() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x10) != 0;
    }

    public void setFleeceColor(EnumDyeColor enumDyeColor) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, (byte)(by & 0xF0 | enumDyeColor.getMetadata() & 0xF));
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyInstance, IEntityLivingData iEntityLivingData) {
        iEntityLivingData = super.onInitialSpawn(difficultyInstance, iEntityLivingData);
        this.setFleeceColor(EntitySheep.getRandomSheepColor(this.worldObj.rand));
        return iEntityLivingData;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.setSheared(nBTTagCompound.getBoolean("Sheared"));
        this.setFleeceColor(EnumDyeColor.byMetadata(nBTTagCompound.getByte("Color")));
    }

    public static float[] func_175513_a(EnumDyeColor enumDyeColor) {
        return DYE_TO_RGB.get(enumDyeColor);
    }

    public float getHeadRotationPointY(float f) {
        return this.sheepTimer <= 0 ? 0.0f : (this.sheepTimer >= 4 && this.sheepTimer <= 36 ? 1.0f : (this.sheepTimer < 4 ? ((float)this.sheepTimer - f) / 4.0f : -((float)(this.sheepTimer - 40) - f) / 4.0f));
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        if (!this.getSheared()) {
            this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getFleeceColor().getMetadata()), 0.0f);
        }
        int n2 = this.rand.nextInt(2) + 1 + this.rand.nextInt(1 + n);
        int n3 = 0;
        while (n3 < n2) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_mutton, 1);
            } else {
                this.dropItem(Items.mutton, 1);
            }
            ++n3;
        }
    }

    public EnumDyeColor getFleeceColor() {
        return EnumDyeColor.byMetadata(this.dataWatcher.getWatchableObjectByte(16) & 0xF);
    }

    public EntitySheep(World world) {
        super(world);
        this.setSize(0.9f, 1.3f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0));
        this.tasks.addTask(3, new EntityAITempt(this, 1.1, Items.wheat, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1));
        this.tasks.addTask(5, this.entityAIEatGrass);
        this.tasks.addTask(6, new EntityAIWander(this, 1.0));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.dye, 1, 0));
        this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.dye, 1, 0));
    }

    public float getHeadRotationAngleX(float f) {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
            float f2 = ((float)(this.sheepTimer - 4) - f) / 32.0f;
            return 0.62831855f + 0.2199115f * MathHelper.sin(f2 * 28.7f);
        }
        return this.sheepTimer > 0 ? 0.62831855f : this.rotationPitch / 57.295776f;
    }

    @Override
    protected Item getDropItem() {
        return Item.getItemFromBlock(Blocks.wool);
    }

    @Override
    public EntitySheep createChild(EntityAgeable entityAgeable) {
        EntitySheep entitySheep = (EntitySheep)entityAgeable;
        EntitySheep entitySheep2 = new EntitySheep(this.worldObj);
        entitySheep2.setFleeceColor(this.getDyeColorMixFromParents(this, entitySheep));
        return entitySheep2;
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 10) {
            this.sheepTimer = 40;
        } else {
            super.handleStatusUpdate(by);
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte(0));
    }

    @Override
    protected String getHurtSound() {
        return "mob.sheep.say";
    }

    @Override
    protected String getLivingSound() {
        return "mob.sheep.say";
    }

    private EnumDyeColor getDyeColorMixFromParents(EntityAnimal entityAnimal, EntityAnimal entityAnimal2) {
        int n = ((EntitySheep)entityAnimal).getFleeceColor().getDyeDamage();
        int n2 = ((EntitySheep)entityAnimal2).getFleeceColor().getDyeDamage();
        this.inventoryCrafting.getStackInSlot(0).setItemDamage(n);
        this.inventoryCrafting.getStackInSlot(1).setItemDamage(n2);
        ItemStack itemStack = CraftingManager.getInstance().findMatchingRecipe(this.inventoryCrafting, ((EntitySheep)entityAnimal).worldObj);
        int n3 = itemStack != null && itemStack.getItem() == Items.dye ? itemStack.getMetadata() : (this.worldObj.rand.nextBoolean() ? n : n2);
        return EnumDyeColor.byDyeDamage(n3);
    }

    @Override
    protected void updateAITasks() {
        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }

    @Override
    public void eatGrassBonus() {
        this.setSheared(false);
        if (this.isChild()) {
            this.addGrowth(60);
        }
    }
}

