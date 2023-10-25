/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityCreeper
extends EntityMob {
    private int timeSinceIgnited;
    private int explosionRadius = 3;
    private int fuseTime = 30;
    private int field_175494_bm = 0;
    private int lastActiveTime;

    @Override
    public void onStruckByLightning(EntityLightningBolt entityLightningBolt) {
        super.onStruckByLightning(entityLightningBolt);
        this.dataWatcher.updateObject(17, (byte)1);
    }

    public boolean hasIgnited() {
        return this.dataWatcher.getWatchableObjectByte(18) != 0;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return true;
    }

    @Override
    public void fall(float f, float f2) {
        super.fall(f, f2);
        this.timeSinceIgnited = (int)((float)this.timeSinceIgnited + f * 1.5f);
        if (this.timeSinceIgnited > this.fuseTime - 5) {
            this.timeSinceIgnited = this.fuseTime - 5;
        }
    }

    public boolean isAIEnabled() {
        return this.field_175494_bm < 1 && this.worldObj.getGameRules().getBoolean("doMobLoot");
    }

    private void explode() {
        if (!this.worldObj.isRemote) {
            boolean bl = this.worldObj.getGameRules().getBoolean("mobGriefing");
            float f = this.getPowered() ? 2.0f : 1.0f;
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionRadius * f, bl);
            this.setDead();
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }

    public void func_175493_co() {
        ++this.field_175494_bm;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.dataWatcher.updateObject(17, (byte)(nBTTagCompound.getBoolean("powered") ? (char)'\u0001' : '\u0000'));
        if (nBTTagCompound.hasKey("Fuse", 99)) {
            this.fuseTime = nBTTagCompound.getShort("Fuse");
        }
        if (nBTTagCompound.hasKey("ExplosionRadius", 99)) {
            this.explosionRadius = nBTTagCompound.getByte("ExplosionRadius");
        }
        if (nBTTagCompound.getBoolean("ignited")) {
            this.ignite();
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)-1);
        this.dataWatcher.addObject(17, (byte)0);
        this.dataWatcher.addObject(18, (byte)0);
    }

    @Override
    protected boolean interact(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        if (itemStack != null && itemStack.getItem() == Items.flint_and_steel) {
            this.worldObj.playSoundEffect(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "fire.ignite", 1.0f, this.rand.nextFloat() * 0.4f + 0.8f);
            entityPlayer.swingItem();
            if (!this.worldObj.isRemote) {
                this.ignite();
                itemStack.damageItem(1, entityPlayer);
                return true;
            }
        }
        return super.interact(entityPlayer);
    }

    @Override
    protected String getHurtSound() {
        return "mob.creeper.say";
    }

    public void ignite() {
        this.dataWatcher.updateObject(18, (byte)1);
    }

    @Override
    public int getMaxFallHeight() {
        return this.getAttackTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0f);
    }

    @Override
    protected String getDeathSound() {
        return "mob.creeper.death";
    }

    public boolean getPowered() {
        return this.dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public int getCreeperState() {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        if (this.dataWatcher.getWatchableObjectByte(17) == 1) {
            nBTTagCompound.setBoolean("powered", true);
        }
        nBTTagCompound.setShort("Fuse", (short)this.fuseTime);
        nBTTagCompound.setByte("ExplosionRadius", (byte)this.explosionRadius);
        nBTTagCompound.setBoolean("ignited", this.hasIgnited());
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (damageSource.getEntity() instanceof EntitySkeleton) {
            int n = Item.getIdFromItem(Items.record_13);
            int n2 = Item.getIdFromItem(Items.record_wait);
            int n3 = n + this.rand.nextInt(n2 - n + 1);
            this.dropItem(Item.getItemById(n3), 1);
        } else if (damageSource.getEntity() instanceof EntityCreeper && damageSource.getEntity() != this && ((EntityCreeper)damageSource.getEntity()).getPowered() && ((EntityCreeper)damageSource.getEntity()).isAIEnabled()) {
            ((EntityCreeper)damageSource.getEntity()).func_175493_co();
            this.entityDropItem(new ItemStack(Items.skull, 1, 4), 0.0f);
        }
    }

    public void setCreeperState(int n) {
        this.dataWatcher.updateObject(16, (byte)n);
    }

    @Override
    protected Item getDropItem() {
        return Items.gunpowder;
    }

    public float getCreeperFlashIntensity(float f) {
        return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * f) / (float)(this.fuseTime - 2);
    }

    @Override
    public void onUpdate() {
        if (this.isEntityAlive()) {
            int n;
            this.lastActiveTime = this.timeSinceIgnited;
            if (this.hasIgnited()) {
                this.setCreeperState(1);
            }
            if ((n = this.getCreeperState()) > 0 && this.timeSinceIgnited == 0) {
                this.playSound("creeper.primed", 1.0f, 0.5f);
            }
            this.timeSinceIgnited += n;
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

    public EntityCreeper(World world) {
        super(world);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAICreeperSwell(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity<EntityOcelot>(this, EntityOcelot.class, 6.0f, 1.0, 1.2));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>((EntityCreature)this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
    }
}

