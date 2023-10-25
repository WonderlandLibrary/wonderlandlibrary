/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class EntitySlime
extends EntityLiving
implements IMob {
    public float squishAmount;
    private boolean wasOnGround;
    public float prevSquishFactor;
    public float squishFactor;

    @Override
    public void onCollideWithPlayer(EntityPlayer entityPlayer) {
        if (this.canDamagePlayer()) {
            this.func_175451_e(entityPlayer);
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos blockPos = new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ));
        Chunk chunk = this.worldObj.getChunkFromBlockCoords(blockPos);
        if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1) {
            return false;
        }
        if (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL) {
            BiomeGenBase biomeGenBase = this.worldObj.getBiomeGenForCoords(blockPos);
            if (biomeGenBase == BiomeGenBase.swampland && this.posY > 50.0 && this.posY < 70.0 && this.rand.nextFloat() < 0.5f && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getLightFromNeighbors(new BlockPos(this)) <= this.rand.nextInt(8)) {
                return super.getCanSpawnHere();
            }
            if (this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0) {
                return super.getCanSpawnHere();
            }
        }
        return false;
    }

    protected void alterSquishAmount() {
        this.squishAmount *= 0.6f;
    }

    @Override
    protected Item getDropItem() {
        return this.getSlimeSize() == 1 ? Items.slime_ball : null;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("Size", this.getSlimeSize() - 1);
        nBTTagCompound.setBoolean("wasOnGround", this.wasOnGround);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        int n = nBTTagCompound.getInteger("Size");
        if (n < 0) {
            n = 0;
        }
        this.setSlimeSize(n + 1);
        this.wasOnGround = nBTTagCompound.getBoolean("wasOnGround");
    }

    protected void setSlimeSize(int n) {
        this.dataWatcher.updateObject(16, (byte)n);
        this.setSize(0.51000005f * (float)n, 0.51000005f * (float)n);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(n * n);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2f + 0.1f * (float)n);
        this.setHealth(this.getMaxHealth());
        this.experienceValue = n;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)1);
    }

    @Override
    public void onUpdate() {
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0) {
            this.isDead = true;
        }
        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5f;
        this.prevSquishFactor = this.squishFactor;
        super.onUpdate();
        if (this.onGround && !this.wasOnGround) {
            int n = this.getSlimeSize();
            int n2 = 0;
            while (n2 < n * 8) {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0f;
                float f2 = this.rand.nextFloat() * 0.5f + 0.5f;
                float f3 = MathHelper.sin(f) * (float)n * 0.5f * f2;
                float f4 = MathHelper.cos(f) * (float)n * 0.5f * f2;
                World world = this.worldObj;
                EnumParticleTypes enumParticleTypes = this.getParticleType();
                double d = this.posX + (double)f3;
                double d2 = this.posZ + (double)f4;
                world.spawnParticle(enumParticleTypes, d, this.getEntityBoundingBox().minY, d2, 0.0, 0.0, 0.0, new int[0]);
                ++n2;
            }
            if (this.makesSoundOnLand()) {
                this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            }
            this.squishAmount = -0.5f;
        } else if (!this.onGround && this.wasOnGround) {
            this.squishAmount = 1.0f;
        }
        this.wasOnGround = this.onGround;
        this.alterSquishAmount();
    }

    @Override
    protected void jump() {
        this.motionY = 0.42f;
        this.isAirBorne = true;
    }

    @Override
    public int getVerticalFaceSpeed() {
        return 0;
    }

    protected boolean makesSoundOnLand() {
        return this.getSlimeSize() > 2;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyInstance, IEntityLivingData iEntityLivingData) {
        int n = this.rand.nextInt(3);
        if (n < 2 && this.rand.nextFloat() < 0.5f * difficultyInstance.getClampedAdditionalDifficulty()) {
            ++n;
        }
        int n2 = 1 << n;
        this.setSlimeSize(n2);
        return super.onInitialSpawn(difficultyInstance, iEntityLivingData);
    }

    public int getSlimeSize() {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    protected int getAttackStrength() {
        return this.getSlimeSize();
    }

    protected String getJumpSound() {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        super.applyEntityCollision(entity);
        if (entity instanceof EntityIronGolem && this.canDamagePlayer()) {
            this.func_175451_e((EntityLivingBase)entity);
        }
    }

    protected void func_175451_e(EntityLivingBase entityLivingBase) {
        int n = this.getSlimeSize();
        if (this.canEntityBeSeen(entityLivingBase) && this.getDistanceSqToEntity(entityLivingBase) < 0.6 * (double)n * 0.6 * (double)n && entityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackStrength())) {
            this.playSound("mob.attack", 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            this.applyEnchantments(this, entityLivingBase);
        }
    }

    @Override
    protected String getHurtSound() {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.SLIME;
    }

    @Override
    protected String getDeathSound() {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    protected boolean makesSoundOnJump() {
        return this.getSlimeSize() > 0;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f * (float)this.getSlimeSize();
    }

    protected EntitySlime createInstance() {
        return new EntitySlime(this.worldObj);
    }

    protected boolean canDamagePlayer() {
        return this.getSlimeSize() > 1;
    }

    @Override
    public float getEyeHeight() {
        return 0.625f * this.height;
    }

    @Override
    public void setDead() {
        int n = this.getSlimeSize();
        if (!this.worldObj.isRemote && n > 1 && this.getHealth() <= 0.0f) {
            int n2 = 2 + this.rand.nextInt(3);
            int n3 = 0;
            while (n3 < n2) {
                float f = ((float)(n3 % 2) - 0.5f) * (float)n / 4.0f;
                float f2 = ((float)(n3 / 2) - 0.5f) * (float)n / 4.0f;
                EntitySlime entitySlime = this.createInstance();
                if (this.hasCustomName()) {
                    entitySlime.setCustomNameTag(this.getCustomNameTag());
                }
                if (this.isNoDespawnRequired()) {
                    entitySlime.enablePersistence();
                }
                entitySlime.setSlimeSize(n / 2);
                entitySlime.setLocationAndAngles(this.posX + (double)f, this.posY + 0.5, this.posZ + (double)f2, this.rand.nextFloat() * 360.0f, 0.0f);
                this.worldObj.spawnEntityInWorld(entitySlime);
                ++n3;
            }
        }
        super.setDead();
    }

    @Override
    public void onDataWatcherUpdate(int n) {
        if (n == 16) {
            int n2 = this.getSlimeSize();
            this.setSize(0.51000005f * (float)n2, 0.51000005f * (float)n2);
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;
            if (this.isInWater() && this.rand.nextInt(20) == 0) {
                this.resetHeight();
            }
        }
        super.onDataWatcherUpdate(n);
    }

    protected int getJumpDelay() {
        return this.rand.nextInt(20) + 10;
    }

    public EntitySlime(World world) {
        super(world);
        this.moveHelper = new SlimeMoveHelper(this);
        this.tasks.addTask(1, new AISlimeFloat(this));
        this.tasks.addTask(2, new AISlimeAttack(this));
        this.tasks.addTask(3, new AISlimeFaceRandom(this));
        this.tasks.addTask(5, new AISlimeHop(this));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
        this.targetTasks.addTask(3, new EntityAIFindEntityNearest(this, EntityIronGolem.class));
    }

    static class AISlimeFaceRandom
    extends EntityAIBase {
        private float field_179459_b;
        private EntitySlime slime;
        private int field_179460_c;

        @Override
        public boolean shouldExecute() {
            return this.slime.getAttackTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava());
        }

        public AISlimeFaceRandom(EntitySlime entitySlime) {
            this.slime = entitySlime;
            this.setMutexBits(2);
        }

        @Override
        public void updateTask() {
            if (--this.field_179460_c <= 0) {
                this.field_179460_c = 40 + this.slime.getRNG().nextInt(60);
                this.field_179459_b = this.slime.getRNG().nextInt(360);
            }
            ((SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.field_179459_b, false);
        }
    }

    static class AISlimeFloat
    extends EntityAIBase {
        private EntitySlime slime;

        @Override
        public void updateTask() {
            if (this.slime.getRNG().nextFloat() < 0.8f) {
                this.slime.getJumpHelper().setJumping();
            }
            ((SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2);
        }

        public AISlimeFloat(EntitySlime entitySlime) {
            this.slime = entitySlime;
            this.setMutexBits(5);
            ((PathNavigateGround)entitySlime.getNavigator()).setCanSwim(true);
        }

        @Override
        public boolean shouldExecute() {
            return this.slime.isInWater() || this.slime.isInLava();
        }
    }

    static class AISlimeAttack
    extends EntityAIBase {
        private int field_179465_b;
        private EntitySlime slime;

        @Override
        public boolean continueExecuting() {
            EntityLivingBase entityLivingBase = this.slime.getAttackTarget();
            return entityLivingBase == null ? false : (!entityLivingBase.isEntityAlive() ? false : (entityLivingBase instanceof EntityPlayer && ((EntityPlayer)entityLivingBase).capabilities.disableDamage ? false : --this.field_179465_b > 0));
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entityLivingBase = this.slime.getAttackTarget();
            return entityLivingBase == null ? false : (!entityLivingBase.isEntityAlive() ? false : !(entityLivingBase instanceof EntityPlayer) || !((EntityPlayer)entityLivingBase).capabilities.disableDamage);
        }

        @Override
        public void updateTask() {
            this.slime.faceEntity(this.slime.getAttackTarget(), 10.0f, 10.0f);
            ((SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.slime.rotationYaw, this.slime.canDamagePlayer());
        }

        public AISlimeAttack(EntitySlime entitySlime) {
            this.slime = entitySlime;
            this.setMutexBits(2);
        }

        @Override
        public void startExecuting() {
            this.field_179465_b = 300;
            super.startExecuting();
        }
    }

    static class SlimeMoveHelper
    extends EntityMoveHelper {
        private float field_179922_g;
        private int field_179924_h;
        private boolean field_179923_j;
        private EntitySlime slime;

        public SlimeMoveHelper(EntitySlime entitySlime) {
            super(entitySlime);
            this.slime = entitySlime;
        }

        public void func_179920_a(float f, boolean bl) {
            this.field_179922_g = f;
            this.field_179923_j = bl;
        }

        @Override
        public void onUpdateMoveHelper() {
            this.entity.rotationYawHead = this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.field_179922_g, 30.0f);
            this.entity.renderYawOffset = this.entity.rotationYaw;
            if (!this.update) {
                this.entity.setMoveForward(0.0f);
            } else {
                this.update = false;
                if (this.entity.onGround) {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                    if (this.field_179924_h-- <= 0) {
                        this.field_179924_h = this.slime.getJumpDelay();
                        if (this.field_179923_j) {
                            this.field_179924_h /= 3;
                        }
                        this.slime.getJumpHelper().setJumping();
                        if (this.slime.makesSoundOnJump()) {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2f + 1.0f) * 0.8f);
                        }
                    } else {
                        this.slime.moveForward = 0.0f;
                        this.slime.moveStrafing = 0.0f;
                        this.entity.setAIMoveSpeed(0.0f);
                    }
                } else {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                }
            }
        }

        public void setSpeed(double d) {
            this.speed = d;
            this.update = true;
        }
    }

    static class AISlimeHop
    extends EntityAIBase {
        private EntitySlime slime;

        public AISlimeHop(EntitySlime entitySlime) {
            this.slime = entitySlime;
            this.setMutexBits(5);
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void updateTask() {
            ((SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0);
        }
    }
}

