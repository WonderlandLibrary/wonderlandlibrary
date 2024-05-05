package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
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
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;

    public EntitySlime(World worldIn) {
        super(worldIn);
        this.moveHelper = new SlimeMoveHelper(this);
        this.tasks.addTask(1, (EntityAIBase)new AISlimeFloat(this));
        this.tasks.addTask(2, (EntityAIBase)new AISlimeAttack(this));
        this.tasks.addTask(3, (EntityAIBase)new AISlimeFaceRandom(this));
        this.tasks.addTask(5, (EntityAIBase)new AISlimeHop(this));
        this.targetTasks.addTask(1, (EntityAIBase)new EntityAIFindEntityNearestPlayer((EntityLiving)this));
        this.targetTasks.addTask(3, (EntityAIBase)new EntityAIFindEntityNearest((EntityLiving)this, EntityIronGolem.class));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Object)1);
    }

    protected void setSlimeSize(int size) {
        this.dataWatcher.updateObject(16, (Object)((byte)size));
        this.setSize(0.51000005f * (float)size, 0.51000005f * (float)size);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)(size * size));
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)(0.2f + 0.1f * (float)size));
        this.setHealth(this.getMaxHealth());
        this.experienceValue = size;
    }

    public int getSlimeSize() {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Size", this.getSlimeSize() - 1);
        tagCompound.setBoolean("wasOnGround", this.wasOnGround);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        int i = tagCompund.getInteger("Size");
        if (i < 0) {
            i = 0;
        }
        this.setSlimeSize(i + 1);
        this.wasOnGround = tagCompund.getBoolean("wasOnGround");
    }

    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.SLIME;
    }

    protected String getJumpSound() {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    public void onUpdate() {
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0) {
            this.isDead = true;
        }
        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5f;
        this.prevSquishFactor = this.squishFactor;
        super.onUpdate();
        if (this.onGround && !this.wasOnGround) {
            int i = this.getSlimeSize();
            for (int j = 0; j < i * 8; ++j) {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0f;
                float f1 = this.rand.nextFloat() * 0.5f + 0.5f;
                float f2 = MathHelper.sin((float)f) * (float)i * 0.5f * f1;
                float f3 = MathHelper.cos((float)f) * (float)i * 0.5f * f1;
                World world = this.worldObj;
                EnumParticleTypes enumparticletypes = this.getParticleType();
                double d0 = this.posX + (double)f2;
                double d1 = this.posZ + (double)f3;
                world.spawnParticle(enumparticletypes, d0, this.getEntityBoundingBox().minY, d1, 0.0, 0.0, 0.0, new int[0]);
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

    protected void alterSquishAmount() {
        this.squishAmount *= 0.6f;
    }

    protected int getJumpDelay() {
        return this.rand.nextInt(20) + 10;
    }

    protected EntitySlime createInstance() {
        return new EntitySlime(this.worldObj);
    }

    public void onDataWatcherUpdate(int dataID) {
        if (dataID == 16) {
            int i = this.getSlimeSize();
            this.setSize(0.51000005f * (float)i, 0.51000005f * (float)i);
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;
            if (this.isInWater() && this.rand.nextInt(20) == 0) {
                this.resetHeight();
            }
        }
        super.onDataWatcherUpdate(dataID);
    }

    public void setDead() {
        int i = this.getSlimeSize();
        if (!this.worldObj.isRemote && i > 1 && this.getHealth() <= 0.0f) {
            int j = 2 + this.rand.nextInt(3);
            for (int k = 0; k < j; ++k) {
                float f = ((float)(k % 2) - 0.5f) * (float)i / 4.0f;
                float f1 = ((float)(k / 2) - 0.5f) * (float)i / 4.0f;
                EntitySlime entityslime = this.createInstance();
                if (this.hasCustomName()) {
                    entityslime.setCustomNameTag(this.getCustomNameTag());
                }
                if (this.isNoDespawnRequired()) {
                    entityslime.enablePersistence();
                }
                entityslime.setSlimeSize(i / 2);
                entityslime.setLocationAndAngles(this.posX + (double)f, this.posY + 0.5, this.posZ + (double)f1, this.rand.nextFloat() * 360.0f, 0.0f);
                this.worldObj.spawnEntityInWorld((Entity)entityslime);
            }
        }
        super.setDead();
    }

    public void applyEntityCollision(Entity entityIn) {
        super.applyEntityCollision(entityIn);
        if (entityIn instanceof EntityIronGolem && this.canDamagePlayer()) {
            this.func_175451_e((EntityLivingBase)entityIn);
        }
    }

    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (this.canDamagePlayer()) {
            this.func_175451_e((EntityLivingBase)entityIn);
        }
    }

    protected void func_175451_e(EntityLivingBase p_175451_1_) {
        int i = this.getSlimeSize();
        if (this.canEntityBeSeen((Entity)p_175451_1_) && this.getDistanceSqToEntity((Entity)p_175451_1_) < 0.6 * (double)i * 0.6 * (double)i && p_175451_1_.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), (float)this.getAttackStrength())) {
            this.playSound("mob.attack", 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            this.applyEnchantments((EntityLivingBase)this, (Entity)p_175451_1_);
        }
    }

    public float getEyeHeight() {
        return 0.625f * this.height;
    }

    protected boolean canDamagePlayer() {
        return this.getSlimeSize() > 1;
    }

    protected int getAttackStrength() {
        return this.getSlimeSize();
    }

    protected String getHurtSound() {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    protected String getDeathSound() {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    protected Item getDropItem() {
        return this.getSlimeSize() == 1 ? Items.slime_ball : null;
    }

    public boolean getCanSpawnHere() {
        BlockPos blockpos = new BlockPos(MathHelper.floor_double((double)this.posX), 0, MathHelper.floor_double((double)this.posZ));
        Chunk chunk = this.worldObj.getChunkFromBlockCoords(blockpos);
        if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1) {
            return false;
        }
        if (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL) {
            BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos);
            if (biomegenbase == BiomeGenBase.swampland && this.posY > 50.0 && this.posY < 70.0 && this.rand.nextFloat() < 0.5f && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getLightFromNeighbors(new BlockPos((Entity)this)) <= this.rand.nextInt(8)) {
                return super.getCanSpawnHere();
            }
            if (this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0) {
                return super.getCanSpawnHere();
            }
        }
        return false;
    }

    protected float getSoundVolume() {
        return 0.4f * (float)this.getSlimeSize();
    }

    public int getVerticalFaceSpeed() {
        return 0;
    }

    protected boolean makesSoundOnJump() {
        return this.getSlimeSize() > 0;
    }

    protected boolean makesSoundOnLand() {
        return this.getSlimeSize() > 2;
    }

    protected void jump() {
        this.motionY = 0.42f;
        this.isAirBorne = true;
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        int i = this.rand.nextInt(3);
        if (i < 2 && this.rand.nextFloat() < 0.5f * difficulty.getClampedAdditionalDifficulty()) {
            ++i;
        }
        int j = 1 << i;
        this.setSlimeSize(j);
        return super.onInitialSpawn(difficulty, livingdata);
    }
}
