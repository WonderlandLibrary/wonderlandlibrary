// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityAgeable;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import java.util.Set;

public class EntityChicken extends EntityAnimal
{
    private static final Set<Item> TEMPTATION_ITEMS;
    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta;
    public int timeUntilNextEgg;
    public boolean chickenJockey;
    
    public EntityChicken(final World worldIn) {
        super(worldIn);
        this.wingRotDelta = 1.0f;
        this.setSize(0.4f, 0.7f);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.setPathPriority(PathNodeType.WATER, 0.0f);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.4));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0, false, EntityChicken.TEMPTATION_ITEMS));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }
    
    @Override
    public float getEyeHeight() {
        return this.height;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos += (float)((this.onGround ? -1 : 4) * 0.3);
        this.destPos = MathHelper.clamp(this.destPos, 0.0f, 1.0f);
        if (!this.onGround && this.wingRotDelta < 1.0f) {
            this.wingRotDelta = 1.0f;
        }
        this.wingRotDelta *= (float)0.9;
        if (!this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.6;
        }
        this.wingRotation += this.wingRotDelta * 2.0f;
        if (!this.world.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            this.dropItem(Items.EGG, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15f, 1.0f);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_CHICKEN;
    }
    
    @Override
    public EntityChicken createChild(final EntityAgeable ageable) {
        return new EntityChicken(this.world);
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return EntityChicken.TEMPTATION_ITEMS.contains(stack.getItem());
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        return this.isChickenJockey() ? 10 : super.getExperiencePoints(player);
    }
    
    public static void registerFixesChicken(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityChicken.class);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.chickenJockey = compound.getBoolean("IsChickenJockey");
        if (compound.hasKey("EggLayTime")) {
            this.timeUntilNextEgg = compound.getInteger("EggLayTime");
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("IsChickenJockey", this.chickenJockey);
        compound.setInteger("EggLayTime", this.timeUntilNextEgg);
    }
    
    @Override
    protected boolean canDespawn() {
        return this.isChickenJockey() && !this.isBeingRidden();
    }
    
    @Override
    public void updatePassenger(final Entity passenger) {
        super.updatePassenger(passenger);
        final float f = MathHelper.sin(this.renderYawOffset * 0.017453292f);
        final float f2 = MathHelper.cos(this.renderYawOffset * 0.017453292f);
        final float f3 = 0.1f;
        final float f4 = 0.0f;
        passenger.setPosition(this.posX + 0.1f * f, this.posY + this.height * 0.5f + passenger.getYOffset() + 0.0, this.posZ - 0.1f * f2);
        if (passenger instanceof EntityLivingBase) {
            ((EntityLivingBase)passenger).renderYawOffset = this.renderYawOffset;
        }
    }
    
    public boolean isChickenJockey() {
        return this.chickenJockey;
    }
    
    public void setChickenJockey(final boolean jockey) {
        this.chickenJockey = jockey;
    }
    
    static {
        TEMPTATION_ITEMS = Sets.newHashSet((Object[])new Item[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS });
    }
}
