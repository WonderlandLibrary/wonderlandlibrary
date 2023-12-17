// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.effect.EntityLightningBolt;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public class EntityCreeper extends EntityMob
{
    private static final DataParameter<Integer> STATE;
    private static final DataParameter<Boolean> POWERED;
    private static final DataParameter<Boolean> IGNITED;
    private int lastActiveTime;
    private int timeSinceIgnited;
    private int fuseTime;
    private int explosionRadius;
    private int droppedSkulls;
    
    public EntityCreeper(final World worldIn) {
        super(worldIn);
        this.fuseTime = 30;
        this.explosionRadius = 3;
        this.setSize(0.6f, 1.7f);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAICreeperSwell(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity<Object>(this, EntityOcelot.class, 6.0f, 1.0, 1.2));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0, false));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, (Class<?>[])new Class[0]));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }
    
    @Override
    public int getMaxFallHeight() {
        return (this.getAttackTarget() == null) ? 3 : (3 + (int)(this.getHealth() - 1.0f));
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        this.timeSinceIgnited += (int)(distance * 1.5f);
        if (this.timeSinceIgnited > this.fuseTime - 5) {
            this.timeSinceIgnited = this.fuseTime - 5;
        }
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityCreeper.STATE, -1);
        this.dataManager.register(EntityCreeper.POWERED, false);
        this.dataManager.register(EntityCreeper.IGNITED, false);
    }
    
    public static void registerFixesCreeper(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityCreeper.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.dataManager.get(EntityCreeper.POWERED)) {
            compound.setBoolean("powered", true);
        }
        compound.setShort("Fuse", (short)this.fuseTime);
        compound.setByte("ExplosionRadius", (byte)this.explosionRadius);
        compound.setBoolean("ignited", this.hasIgnited());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(EntityCreeper.POWERED, compound.getBoolean("powered"));
        if (compound.hasKey("Fuse", 99)) {
            this.fuseTime = compound.getShort("Fuse");
        }
        if (compound.hasKey("ExplosionRadius", 99)) {
            this.explosionRadius = compound.getByte("ExplosionRadius");
        }
        if (compound.getBoolean("ignited")) {
            this.ignite();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.isEntityAlive()) {
            this.lastActiveTime = this.timeSinceIgnited;
            if (this.hasIgnited()) {
                this.setCreeperState(1);
            }
            final int i = this.getCreeperState();
            if (i > 0 && this.timeSinceIgnited == 0) {
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 0.5f);
            }
            this.timeSinceIgnited += i;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }
            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;
                this.explode();
            }
        }
        super.onUpdate();
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_CREEPER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CREEPER_DEATH;
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        if (this.world.getGameRules().getBoolean("doMobLoot")) {
            if (cause.getTrueSource() instanceof EntitySkeleton) {
                final int i = Item.getIdFromItem(Items.RECORD_13);
                final int j = Item.getIdFromItem(Items.RECORD_WAIT);
                final int k = i + this.rand.nextInt(j - i + 1);
                this.dropItem(Item.getItemById(k), 1);
            }
            else if (cause.getTrueSource() instanceof EntityCreeper && cause.getTrueSource() != this && ((EntityCreeper)cause.getTrueSource()).getPowered() && ((EntityCreeper)cause.getTrueSource()).ableToCauseSkullDrop()) {
                ((EntityCreeper)cause.getTrueSource()).incrementDroppedSkulls();
                this.entityDropItem(new ItemStack(Items.SKULL, 1, 4), 0.0f);
            }
        }
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        return true;
    }
    
    public boolean getPowered() {
        return this.dataManager.get(EntityCreeper.POWERED);
    }
    
    public float getCreeperFlashIntensity(final float p_70831_1_) {
        return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (this.fuseTime - 2);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_CREEPER;
    }
    
    public int getCreeperState() {
        return this.dataManager.get(EntityCreeper.STATE);
    }
    
    public void setCreeperState(final int state) {
        this.dataManager.set(EntityCreeper.STATE, state);
    }
    
    @Override
    public void onStruckByLightning(final EntityLightningBolt lightningBolt) {
        super.onStruckByLightning(lightningBolt);
        this.dataManager.set(EntityCreeper.POWERED, true);
    }
    
    @Override
    protected boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0f, this.rand.nextFloat() * 0.4f + 0.8f);
            player.swingArm(hand);
            if (!this.world.isRemote) {
                this.ignite();
                itemstack.damageItem(1, player);
                return true;
            }
        }
        return super.processInteract(player, hand);
    }
    
    private void explode() {
        if (!this.world.isRemote) {
            final boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
            final float f = this.getPowered() ? 2.0f : 1.0f;
            this.dead = true;
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius * f, flag);
            this.setDead();
            this.spawnLingeringCloud();
        }
    }
    
    private void spawnLingeringCloud() {
        final Collection<PotionEffect> collection = this.getActivePotionEffects();
        if (!collection.isEmpty()) {
            final EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
            entityareaeffectcloud.setRadius(2.5f);
            entityareaeffectcloud.setRadiusOnUse(-0.5f);
            entityareaeffectcloud.setWaitTime(10);
            entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
            entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());
            for (final PotionEffect potioneffect : collection) {
                entityareaeffectcloud.addEffect(new PotionEffect(potioneffect));
            }
            this.world.spawnEntity(entityareaeffectcloud);
        }
    }
    
    public boolean hasIgnited() {
        return this.dataManager.get(EntityCreeper.IGNITED);
    }
    
    public void ignite() {
        this.dataManager.set(EntityCreeper.IGNITED, true);
    }
    
    public boolean ableToCauseSkullDrop() {
        return this.droppedSkulls < 1 && this.world.getGameRules().getBoolean("doMobLoot");
    }
    
    public void incrementDroppedSkulls() {
        ++this.droppedSkulls;
    }
    
    static {
        STATE = EntityDataManager.createKey(EntityCreeper.class, DataSerializers.VARINT);
        POWERED = EntityDataManager.createKey(EntityCreeper.class, DataSerializers.BOOLEAN);
        IGNITED = EntityDataManager.createKey(EntityCreeper.class, DataSerializers.BOOLEAN);
    }
}
