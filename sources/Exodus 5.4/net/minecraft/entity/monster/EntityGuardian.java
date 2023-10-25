/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityGuardian
extends EntityMob {
    private int field_175479_bo;
    private EntityLivingBase targetedEntity;
    private float field_175486_bm;
    private EntityAIWander wander;
    private float field_175485_bl;
    private boolean field_175480_bp;
    private float field_175484_c;
    private float field_175482_b;
    private float field_175483_bk;

    private void setSyncedFlag(int n, boolean bl) {
        int n2 = this.dataWatcher.getWatchableObjectInt(16);
        if (bl) {
            this.dataWatcher.updateObject(16, n2 | n);
        } else {
            this.dataWatcher.updateObject(16, n2 & ~n);
        }
    }

    @Override
    public int getTalkInterval() {
        return 160;
    }

    @Override
    public float getBlockPathWeight(BlockPos blockPos) {
        return this.worldObj.getBlockState(blockPos).getBlock().getMaterial() == Material.water ? 10.0f + this.worldObj.getLightBrightness(blockPos) - 0.5f : super.getBlockPathWeight(blockPos);
    }

    @Override
    public boolean isNotColliding() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.setElder(nBTTagCompound.getBoolean("Elder"));
    }

    private boolean isSyncedFlagSet(int n) {
        return (this.dataWatcher.getWatchableObjectInt(16) & n) != 0;
    }

    private void func_175476_l(boolean bl) {
        this.setSyncedFlag(2, bl);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setBoolean("Elder", this.isElder());
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (!this.func_175472_n() && !damageSource.isMagicDamage() && damageSource.getSourceOfDamage() instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)damageSource.getSourceOfDamage();
            if (!damageSource.isExplosion()) {
                entityLivingBase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0f);
                entityLivingBase.playSound("damage.thorns", 0.5f, 1.0f);
            }
        }
        this.wander.makeUpdate();
        return super.attackEntityFrom(damageSource, f);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean func_175472_n() {
        return this.isSyncedFlagSet(2);
    }

    private void setTargetedEntity(int n) {
        this.dataWatcher.updateObject(17, n);
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.5f;
    }

    @Override
    protected PathNavigate getNewNavigator(World world) {
        return new PathNavigateSwimmer(this, world);
    }

    @Override
    public int getVerticalFaceSpeed() {
        return 180;
    }

    public int func_175464_ck() {
        return this.isElder() ? 60 : 80;
    }

    @Override
    protected String getHurtSound() {
        return !this.isInWater() ? "mob.guardian.land.hit" : (this.isElder() ? "mob.guardian.elder.hit" : "mob.guardian.hit");
    }

    public float func_175469_o(float f) {
        return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * f;
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        int n2 = this.rand.nextInt(3) + this.rand.nextInt(n + 1);
        if (n2 > 0) {
            this.entityDropItem(new ItemStack(Items.prismarine_shard, n2, 0), 1.0f);
        }
        if (this.rand.nextInt(3 + n) > 1) {
            this.entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 1.0f);
        } else if (this.rand.nextInt(3 + n) > 1) {
            this.entityDropItem(new ItemStack(Items.prismarine_crystals, 1, 0), 1.0f);
        }
        if (bl && this.isElder()) {
            this.entityDropItem(new ItemStack(Blocks.sponge, 1, 1), 1.0f);
        }
    }

    @Override
    public void moveEntityWithHeading(float f, float f2) {
        if (this.isServerWorld()) {
            if (this.isInWater()) {
                this.moveFlying(f, f2, 0.1f);
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                this.motionX *= (double)0.9f;
                this.motionY *= (double)0.9f;
                this.motionZ *= (double)0.9f;
                if (!this.func_175472_n() && this.getAttackTarget() == null) {
                    this.motionY -= 0.005;
                }
            } else {
                super.moveEntityWithHeading(f, f2);
            }
        } else {
            super.moveEntityWithHeading(f, f2);
        }
    }

    public boolean hasTargetedEntity() {
        return this.dataWatcher.getWatchableObjectInt(17) != 0;
    }

    public void setElder(boolean bl) {
        this.setSyncedFlag(4, bl);
        if (bl) {
            this.setSize(1.9975f, 1.9975f);
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3f);
            this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0);
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0);
            this.enablePersistence();
            this.wander.setExecutionChance(400);
        }
    }

    public EntityLivingBase getTargetedEntity() {
        if (!this.hasTargetedEntity()) {
            return null;
        }
        if (this.worldObj.isRemote) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            }
            Entity entity = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(17));
            if (entity instanceof EntityLivingBase) {
                this.targetedEntity = (EntityLivingBase)entity;
                return this.targetedEntity;
            }
            return null;
        }
        return this.getAttackTarget();
    }

    public EntityGuardian(World world) {
        super(world);
        this.experienceValue = 10;
        this.setSize(0.85f, 0.85f);
        this.tasks.addTask(4, new AIGuardianAttack(this));
        EntityAIMoveTowardsRestriction entityAIMoveTowardsRestriction = new EntityAIMoveTowardsRestriction(this, 1.0);
        this.tasks.addTask(5, entityAIMoveTowardsRestriction);
        this.wander = new EntityAIWander(this, 1.0, 80);
        this.tasks.addTask(7, this.wander);
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0f, 0.01f));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.wander.setMutexBits(3);
        entityAIMoveTowardsRestriction.setMutexBits(3);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityLivingBase>(this, EntityLivingBase.class, 10, true, false, new GuardianTargetSelector(this)));
        this.moveHelper = new GuardianMoveHelper(this);
        this.field_175484_c = this.field_175482_b = this.rand.nextFloat();
    }

    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            Object object;
            this.field_175484_c = this.field_175482_b;
            if (!this.isInWater()) {
                this.field_175483_bk = 2.0f;
                if (this.motionY > 0.0 && this.field_175480_bp && !this.isSilent()) {
                    this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0f, 1.0f, false);
                }
                this.field_175480_bp = this.motionY < 0.0 && this.worldObj.isBlockNormalCube(new BlockPos(this).down(), false);
            } else {
                this.field_175483_bk = this.func_175472_n() ? (this.field_175483_bk < 0.5f ? 4.0f : (this.field_175483_bk += (0.5f - this.field_175483_bk) * 0.1f)) : (this.field_175483_bk += (0.125f - this.field_175483_bk) * 0.2f);
            }
            this.field_175482_b += this.field_175483_bk;
            this.field_175486_bm = this.field_175485_bl;
            this.field_175485_bl = !this.isInWater() ? this.rand.nextFloat() : (this.func_175472_n() ? (this.field_175485_bl += (0.0f - this.field_175485_bl) * 0.25f) : (this.field_175485_bl += (1.0f - this.field_175485_bl) * 0.06f));
            if (this.func_175472_n() && this.isInWater()) {
                object = this.getLook(0.0f);
                int n = 0;
                while (n < 2) {
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5) * (double)this.width - ((Vec3)object).xCoord * 1.5, this.posY + this.rand.nextDouble() * (double)this.height - ((Vec3)object).yCoord * 1.5, this.posZ + (this.rand.nextDouble() - 0.5) * (double)this.width - ((Vec3)object).zCoord * 1.5, 0.0, 0.0, 0.0, new int[0]);
                    ++n;
                }
            }
            if (this.hasTargetedEntity()) {
                if (this.field_175479_bo < this.func_175464_ck()) {
                    ++this.field_175479_bo;
                }
                if ((object = this.getTargetedEntity()) != null) {
                    this.getLookHelper().setLookPositionWithEntity((Entity)object, 90.0f, 90.0f);
                    this.getLookHelper().onUpdateLook();
                    double d = this.func_175477_p(0.0f);
                    double d2 = ((EntityLivingBase)object).posX - this.posX;
                    double d3 = ((EntityLivingBase)object).posY + (double)(((EntityLivingBase)object).height * 0.5f) - (this.posY + (double)this.getEyeHeight());
                    double d4 = ((EntityLivingBase)object).posZ - this.posZ;
                    double d5 = Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
                    d2 /= d5;
                    d3 /= d5;
                    d4 /= d5;
                    double d6 = this.rand.nextDouble();
                    while (d6 < d5) {
                        this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d2 * (d6 += 1.8 - d + this.rand.nextDouble() * (1.7 - d)), this.posY + d3 * d6 + (double)this.getEyeHeight(), this.posZ + d4 * d6, 0.0, 0.0, 0.0, new int[0]);
                    }
                }
            }
        }
        if (this.inWater) {
            this.setAir(300);
        } else if (this.onGround) {
            this.motionY += 0.5;
            this.motionX += (double)((this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f);
            this.motionZ += (double)((this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f);
            this.rotationYaw = this.rand.nextFloat() * 360.0f;
            this.onGround = false;
            this.isAirBorne = true;
        }
        if (this.hasTargetedEntity()) {
            this.rotationYaw = this.rotationYawHead;
        }
        super.onLivingUpdate();
    }

    @Override
    protected void addRandomDrop() {
        ItemStack itemStack = WeightedRandom.getRandomItem(this.rand, EntityFishHook.func_174855_j()).getItemStack(this.rand);
        this.entityDropItem(itemStack, 1.0f);
    }

    public void setElder() {
        this.setElder(true);
        this.field_175485_bl = 1.0f;
        this.field_175486_bm = 1.0f;
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    public float func_175471_a(float f) {
        return this.field_175484_c + (this.field_175482_b - this.field_175484_c) * f;
    }

    @Override
    public void onDataWatcherUpdate(int n) {
        super.onDataWatcherUpdate(n);
        if (n == 16) {
            if (this.isElder() && this.width < 1.0f) {
                this.setSize(1.9975f, 1.9975f);
            }
        } else if (n == 17) {
            this.field_175479_bo = 0;
            this.targetedEntity = null;
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.isElder()) {
            int n = 1200;
            int n2 = 1200;
            int n3 = 6000;
            int n4 = 2;
            if ((this.ticksExisted + this.getEntityId()) % 1200 == 0) {
                Potion potion = Potion.digSlowdown;
                for (EntityPlayerMP entityPlayerMP : this.worldObj.getPlayers(EntityPlayerMP.class, new Predicate<EntityPlayerMP>(){

                    public boolean apply(EntityPlayerMP entityPlayerMP) {
                        return EntityGuardian.this.getDistanceSqToEntity(entityPlayerMP) < 2500.0 && entityPlayerMP.theItemInWorldManager.survivalOrAdventure();
                    }
                })) {
                    if (entityPlayerMP.isPotionActive(potion) && entityPlayerMP.getActivePotionEffect(potion).getAmplifier() >= 2 && entityPlayerMP.getActivePotionEffect(potion).getDuration() >= 1200) continue;
                    entityPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(10, 0.0f));
                    entityPlayerMP.addPotionEffect(new PotionEffect(potion.id, 6000, 2));
                }
            }
            if (!this.hasHome()) {
                this.setHomePosAndDistance(new BlockPos(this), 16);
            }
        }
    }

    @Override
    protected String getLivingSound() {
        return !this.isInWater() ? "mob.guardian.land.idle" : (this.isElder() ? "mob.guardian.elder.idle" : "mob.guardian.idle");
    }

    public float func_175477_p(float f) {
        return ((float)this.field_175479_bo + f) / (float)this.func_175464_ck();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, 0);
        this.dataWatcher.addObject(17, 0);
    }

    public boolean isElder() {
        return this.isSyncedFlagSet(4);
    }

    @Override
    protected String getDeathSound() {
        return !this.isInWater() ? "mob.guardian.land.death" : (this.isElder() ? "mob.guardian.elder.death" : "mob.guardian.death");
    }

    @Override
    public boolean getCanSpawnHere() {
        return (this.rand.nextInt(20) == 0 || !this.worldObj.canBlockSeeSky(new BlockPos(this))) && super.getCanSpawnHere();
    }

    static class GuardianTargetSelector
    implements Predicate<EntityLivingBase> {
        private EntityGuardian parentEntity;

        public GuardianTargetSelector(EntityGuardian entityGuardian) {
            this.parentEntity = entityGuardian;
        }

        public boolean apply(EntityLivingBase entityLivingBase) {
            return (entityLivingBase instanceof EntityPlayer || entityLivingBase instanceof EntitySquid) && entityLivingBase.getDistanceSqToEntity(this.parentEntity) > 9.0;
        }
    }

    static class AIGuardianAttack
    extends EntityAIBase {
        private EntityGuardian theEntity;
        private int tickCounter;

        @Override
        public void updateTask() {
            EntityLivingBase entityLivingBase = this.theEntity.getAttackTarget();
            this.theEntity.getNavigator().clearPathEntity();
            this.theEntity.getLookHelper().setLookPositionWithEntity(entityLivingBase, 90.0f, 90.0f);
            if (!this.theEntity.canEntityBeSeen(entityLivingBase)) {
                this.theEntity.setAttackTarget(null);
            } else {
                ++this.tickCounter;
                if (this.tickCounter == 0) {
                    this.theEntity.setTargetedEntity(this.theEntity.getAttackTarget().getEntityId());
                    this.theEntity.worldObj.setEntityState(this.theEntity, (byte)21);
                } else if (this.tickCounter >= this.theEntity.func_175464_ck()) {
                    float f = 1.0f;
                    if (this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                        f += 2.0f;
                    }
                    if (this.theEntity.isElder()) {
                        f += 2.0f;
                    }
                    entityLivingBase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.theEntity, this.theEntity), f);
                    entityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity), (float)this.theEntity.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
                    this.theEntity.setAttackTarget(null);
                } else if (this.tickCounter < 60 || this.tickCounter % 20 == 0) {
                    // empty if block
                }
                super.updateTask();
            }
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entityLivingBase = this.theEntity.getAttackTarget();
            return entityLivingBase != null && entityLivingBase.isEntityAlive();
        }

        public AIGuardianAttack(EntityGuardian entityGuardian) {
            this.theEntity = entityGuardian;
            this.setMutexBits(3);
        }

        @Override
        public boolean continueExecuting() {
            return super.continueExecuting() && (this.theEntity.isElder() || this.theEntity.getDistanceSqToEntity(this.theEntity.getAttackTarget()) > 9.0);
        }

        @Override
        public void resetTask() {
            this.theEntity.setTargetedEntity(0);
            this.theEntity.setAttackTarget(null);
            this.theEntity.wander.makeUpdate();
        }

        @Override
        public void startExecuting() {
            this.tickCounter = -10;
            this.theEntity.getNavigator().clearPathEntity();
            this.theEntity.getLookHelper().setLookPositionWithEntity(this.theEntity.getAttackTarget(), 90.0f, 90.0f);
            this.theEntity.isAirBorne = true;
        }
    }

    static class GuardianMoveHelper
    extends EntityMoveHelper {
        private EntityGuardian entityGuardian;

        public GuardianMoveHelper(EntityGuardian entityGuardian) {
            super(entityGuardian);
            this.entityGuardian = entityGuardian;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.update && !this.entityGuardian.getNavigator().noPath()) {
                double d = this.posX - this.entityGuardian.posX;
                double d2 = this.posY - this.entityGuardian.posY;
                double d3 = this.posZ - this.entityGuardian.posZ;
                double d4 = d * d + d2 * d2 + d3 * d3;
                d4 = MathHelper.sqrt_double(d4);
                d2 /= d4;
                float f = (float)(MathHelper.func_181159_b(d3, d) * 180.0 / Math.PI) - 90.0f;
                this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw = this.limitAngle(this.entityGuardian.rotationYaw, f, 30.0f);
                float f2 = (float)(this.speed * this.entityGuardian.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                this.entityGuardian.setAIMoveSpeed(this.entityGuardian.getAIMoveSpeed() + (f2 - this.entityGuardian.getAIMoveSpeed()) * 0.125f);
                double d5 = Math.sin((double)(this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5) * 0.05;
                double d6 = Math.cos(this.entityGuardian.rotationYaw * (float)Math.PI / 180.0f);
                double d7 = Math.sin(this.entityGuardian.rotationYaw * (float)Math.PI / 180.0f);
                this.entityGuardian.motionX += d5 * d6;
                this.entityGuardian.motionZ += d5 * d7;
                d5 = Math.sin((double)(this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75) * 0.05;
                this.entityGuardian.motionY += d5 * (d7 + d6) * 0.25;
                this.entityGuardian.motionY += (double)this.entityGuardian.getAIMoveSpeed() * d2 * 0.1;
                EntityLookHelper entityLookHelper = this.entityGuardian.getLookHelper();
                double d8 = this.entityGuardian.posX + d / d4 * 2.0;
                double d9 = (double)this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + d2 / d4 * 1.0;
                double d10 = this.entityGuardian.posZ + d3 / d4 * 2.0;
                double d11 = entityLookHelper.getLookPosX();
                double d12 = entityLookHelper.getLookPosY();
                double d13 = entityLookHelper.getLookPosZ();
                if (!entityLookHelper.getIsLooking()) {
                    d11 = d8;
                    d12 = d9;
                    d13 = d10;
                }
                this.entityGuardian.getLookHelper().setLookPosition(d11 + (d8 - d11) * 0.125, d12 + (d9 - d12) * 0.125, d13 + (d10 - d13) * 0.125, 10.0f, 40.0f);
                this.entityGuardian.func_175476_l(true);
            } else {
                this.entityGuardian.setAIMoveSpeed(0.0f);
                this.entityGuardian.func_175476_l(false);
            }
        }
    }
}

