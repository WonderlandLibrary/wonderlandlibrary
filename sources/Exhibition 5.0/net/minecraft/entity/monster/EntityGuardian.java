// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.DamageSource;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import java.util.Iterator;
import java.util.List;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.EntityLivingBase;

public class EntityGuardian extends EntityMob
{
    private float field_175482_b;
    private float field_175484_c;
    private float field_175483_bk;
    private float field_175485_bl;
    private float field_175486_bm;
    private EntityLivingBase field_175478_bn;
    private int field_175479_bo;
    private boolean field_175480_bp;
    private EntityAIWander field_175481_bq;
    private static final String __OBFID = "CL_00002213";
    
    public EntityGuardian(final World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
        this.setSize(0.85f, 0.85f);
        this.tasks.addTask(4, new AIGuardianAttack());
        final EntityAIMoveTowardsRestriction var2;
        this.tasks.addTask(5, var2 = new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, this.field_175481_bq = new EntityAIWander(this, 1.0, 80));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0f, 0.01f));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.field_175481_bq.setMutexBits(3);
        var2.setMutexBits(3);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, (Predicate)new GuardianTargetSelector()));
        this.moveHelper = new GuardianMoveHelper();
        final float nextFloat = this.rand.nextFloat();
        this.field_175482_b = nextFloat;
        this.field_175484_c = nextFloat;
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
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.func_175467_a(tagCompund.getBoolean("Elder"));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Elder", this.func_175461_cl());
    }
    
    @Override
    protected PathNavigate func_175447_b(final World worldIn) {
        return new PathNavigateSwimmer(this, worldIn);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, 0);
        this.dataWatcher.addObject(17, 0);
    }
    
    private boolean func_175468_a(final int p_175468_1_) {
        return (this.dataWatcher.getWatchableObjectInt(16) & p_175468_1_) != 0x0;
    }
    
    private void func_175473_a(final int p_175473_1_, final boolean p_175473_2_) {
        final int var3 = this.dataWatcher.getWatchableObjectInt(16);
        if (p_175473_2_) {
            this.dataWatcher.updateObject(16, var3 | p_175473_1_);
        }
        else {
            this.dataWatcher.updateObject(16, var3 & ~p_175473_1_);
        }
    }
    
    public boolean func_175472_n() {
        return this.func_175468_a(2);
    }
    
    private void func_175476_l(final boolean p_175476_1_) {
        this.func_175473_a(2, p_175476_1_);
    }
    
    public int func_175464_ck() {
        return this.func_175461_cl() ? 60 : 80;
    }
    
    public boolean func_175461_cl() {
        return this.func_175468_a(4);
    }
    
    public void func_175467_a(final boolean p_175467_1_) {
        this.func_175473_a(4, p_175467_1_);
        if (p_175467_1_) {
            this.setSize(1.9975f, 1.9975f);
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896);
            this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0);
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0);
            this.enablePersistence();
            this.field_175481_bq.func_179479_b(400);
        }
    }
    
    public void func_175465_cm() {
        this.func_175467_a(true);
        final float n = 1.0f;
        this.field_175485_bl = n;
        this.field_175486_bm = n;
    }
    
    private void func_175463_b(final int p_175463_1_) {
        this.dataWatcher.updateObject(17, p_175463_1_);
    }
    
    public boolean func_175474_cn() {
        return this.dataWatcher.getWatchableObjectInt(17) != 0;
    }
    
    public EntityLivingBase func_175466_co() {
        if (!this.func_175474_cn()) {
            return null;
        }
        if (!this.worldObj.isRemote) {
            return this.getAttackTarget();
        }
        if (this.field_175478_bn != null) {
            return this.field_175478_bn;
        }
        final Entity var1 = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(17));
        if (var1 instanceof EntityLivingBase) {
            return this.field_175478_bn = (EntityLivingBase)var1;
        }
        return null;
    }
    
    @Override
    public void func_145781_i(final int p_145781_1_) {
        super.func_145781_i(p_145781_1_);
        if (p_145781_1_ == 16) {
            if (this.func_175461_cl() && this.width < 1.0f) {
                this.setSize(1.9975f, 1.9975f);
            }
        }
        else if (p_145781_1_ == 17) {
            this.field_175479_bo = 0;
            this.field_175478_bn = null;
        }
    }
    
    @Override
    public int getTalkInterval() {
        return 160;
    }
    
    @Override
    protected String getLivingSound() {
        return this.isInWater() ? (this.func_175461_cl() ? "mob.guardian.elder.idle" : "mob.guardian.idle") : "mob.guardian.land.idle";
    }
    
    @Override
    protected String getHurtSound() {
        return this.isInWater() ? (this.func_175461_cl() ? "mob.guardian.elder.hit" : "mob.guardian.hit") : "mob.guardian.land.hit";
    }
    
    @Override
    protected String getDeathSound() {
        return this.isInWater() ? (this.func_175461_cl() ? "mob.guardian.elder.death" : "mob.guardian.death") : "mob.guardian.land.death";
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    public float getEyeHeight() {
        return this.height * 0.5f;
    }
    
    @Override
    public float func_180484_a(final BlockPos p_180484_1_) {
        return (this.worldObj.getBlockState(p_180484_1_).getBlock().getMaterial() == Material.water) ? (10.0f + this.worldObj.getLightBrightness(p_180484_1_) - 0.5f) : super.func_180484_a(p_180484_1_);
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            this.field_175484_c = this.field_175482_b;
            if (!this.isInWater()) {
                this.field_175483_bk = 2.0f;
                if (this.motionY > 0.0 && this.field_175480_bp && !this.isSlient()) {
                    this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0f, 1.0f, false);
                }
                this.field_175480_bp = (this.motionY < 0.0 && this.worldObj.func_175677_d(new BlockPos(this).offsetDown(), false));
            }
            else if (this.func_175472_n()) {
                if (this.field_175483_bk < 0.5f) {
                    this.field_175483_bk = 4.0f;
                }
                else {
                    this.field_175483_bk += (0.5f - this.field_175483_bk) * 0.1f;
                }
            }
            else {
                this.field_175483_bk += (0.125f - this.field_175483_bk) * 0.2f;
            }
            this.field_175482_b += this.field_175483_bk;
            this.field_175486_bm = this.field_175485_bl;
            if (!this.isInWater()) {
                this.field_175485_bl = this.rand.nextFloat();
            }
            else if (this.func_175472_n()) {
                this.field_175485_bl += (0.0f - this.field_175485_bl) * 0.25f;
            }
            else {
                this.field_175485_bl += (1.0f - this.field_175485_bl) * 0.06f;
            }
            if (this.func_175472_n() && this.isInWater()) {
                final Vec3 var1 = this.getLook(0.0f);
                for (int var2 = 0; var2 < 2; ++var2) {
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5) * this.width - var1.xCoord * 1.5, this.posY + this.rand.nextDouble() * this.height - var1.yCoord * 1.5, this.posZ + (this.rand.nextDouble() - 0.5) * this.width - var1.zCoord * 1.5, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (this.func_175474_cn()) {
                if (this.field_175479_bo < this.func_175464_ck()) {
                    ++this.field_175479_bo;
                }
                final EntityLivingBase var3 = this.func_175466_co();
                if (var3 != null) {
                    this.getLookHelper().setLookPositionWithEntity(var3, 90.0f, 90.0f);
                    this.getLookHelper().onUpdateLook();
                    final double var4 = this.func_175477_p(0.0f);
                    double var5 = var3.posX - this.posX;
                    double var6 = var3.posY + var3.height * 0.5f - (this.posY + this.getEyeHeight());
                    double var7 = var3.posZ - this.posZ;
                    final double var8 = Math.sqrt(var5 * var5 + var6 * var6 + var7 * var7);
                    var5 /= var8;
                    var6 /= var8;
                    var7 /= var8;
                    double var9 = this.rand.nextDouble();
                    while (var9 < var8) {
                        var9 += 1.8 - var4 + this.rand.nextDouble() * (1.7 - var4);
                        this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + var5 * var9, this.posY + var6 * var9 + this.getEyeHeight(), this.posZ + var7 * var9, 0.0, 0.0, 0.0, new int[0]);
                    }
                }
            }
        }
        if (this.inWater) {
            this.setAir(300);
        }
        else if (this.onGround) {
            this.motionY += 0.5;
            this.motionX += (this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f;
            this.motionZ += (this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f;
            this.rotationYaw = this.rand.nextFloat() * 360.0f;
            this.onGround = false;
            this.isAirBorne = true;
        }
        if (this.func_175474_cn()) {
            this.rotationYaw = this.rotationYawHead;
        }
        super.onLivingUpdate();
    }
    
    public float func_175471_a(final float p_175471_1_) {
        return this.field_175484_c + (this.field_175482_b - this.field_175484_c) * p_175471_1_;
    }
    
    public float func_175469_o(final float p_175469_1_) {
        return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * p_175469_1_;
    }
    
    public float func_175477_p(final float p_175477_1_) {
        return (this.field_175479_bo + p_175477_1_) / this.func_175464_ck();
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.func_175461_cl()) {
            final boolean var1 = true;
            final boolean var2 = true;
            final boolean var3 = true;
            final boolean var4 = true;
            if ((this.ticksExisted + this.getEntityId()) % 1200 == 0) {
                final Potion var5 = Potion.digSlowdown;
                final List var6 = this.worldObj.func_175661_b(EntityPlayerMP.class, (Predicate)new Predicate() {
                    private static final String __OBFID = "CL_00002212";
                    
                    public boolean func_179913_a(final EntityPlayerMP p_179913_1_) {
                        return EntityGuardian.this.getDistanceSqToEntity(p_179913_1_) < 2500.0 && p_179913_1_.theItemInWorldManager.func_180239_c();
                    }
                    
                    public boolean apply(final Object p_apply_1_) {
                        return this.func_179913_a((EntityPlayerMP)p_apply_1_);
                    }
                });
                for (final EntityPlayerMP var8 : var6) {
                    if (!var8.isPotionActive(var5) || var8.getActivePotionEffect(var5).getAmplifier() < 2 || var8.getActivePotionEffect(var5).getDuration() < 1200) {
                        var8.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(10, 0.0f));
                        var8.addPotionEffect(new PotionEffect(var5.id, 6000, 2));
                    }
                }
            }
            if (!this.hasHome()) {
                this.func_175449_a(new BlockPos(this), 16);
            }
        }
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        final int var3 = this.rand.nextInt(3) + this.rand.nextInt(p_70628_2_ + 1);
        if (var3 > 0) {
            this.entityDropItem(new ItemStack(Items.prismarine_shard, var3, 0), 1.0f);
        }
        if (this.rand.nextInt(3 + p_70628_2_) > 1) {
            this.entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getItemDamage()), 1.0f);
        }
        else if (this.rand.nextInt(3 + p_70628_2_) > 1) {
            this.entityDropItem(new ItemStack(Items.prismarine_crystals, 1, 0), 1.0f);
        }
        if (p_70628_1_ && this.func_175461_cl()) {
            this.entityDropItem(new ItemStack(Blocks.sponge, 1, 1), 1.0f);
        }
    }
    
    @Override
    protected void addRandomArmor() {
        final ItemStack var1 = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, EntityFishHook.func_174855_j())).getItemStack(this.rand);
        this.entityDropItem(var1, 1.0f);
    }
    
    @Override
    protected boolean isValidLightLevel() {
        return true;
    }
    
    @Override
    public boolean handleLavaMovement() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty();
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return (this.rand.nextInt(20) == 0 || !this.worldObj.canBlockSeeSky(new BlockPos(this))) && super.getCanSpawnHere();
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (!this.func_175472_n() && !source.isMagicDamage() && source.getSourceOfDamage() instanceof EntityLivingBase) {
            final EntityLivingBase var3 = (EntityLivingBase)source.getSourceOfDamage();
            if (!source.isExplosion()) {
                var3.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0f);
                var3.playSound("damage.thorns", 0.5f, 1.0f);
            }
        }
        this.field_175481_bq.func_179480_f();
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public int getVerticalFaceSpeed() {
        return 180;
    }
    
    @Override
    public void moveEntityWithHeading(final float p_70612_1_, final float p_70612_2_) {
        if (this.isServerWorld()) {
            if (this.isInWater()) {
                this.moveFlying(p_70612_1_, p_70612_2_, 0.1f);
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                this.motionX *= 0.8999999761581421;
                this.motionY *= 0.8999999761581421;
                this.motionZ *= 0.8999999761581421;
                if (!this.func_175472_n() && this.getAttackTarget() == null) {
                    this.motionY -= 0.005;
                }
            }
            else {
                super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
            }
        }
        else {
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
        }
    }
    
    class AIGuardianAttack extends EntityAIBase
    {
        private EntityGuardian field_179456_a;
        private int field_179455_b;
        private static final String __OBFID = "CL_00002211";
        
        public AIGuardianAttack() {
            this.field_179456_a = EntityGuardian.this;
            this.setMutexBits(3);
        }
        
        @Override
        public boolean shouldExecute() {
            final EntityLivingBase var1 = this.field_179456_a.getAttackTarget();
            return var1 != null && var1.isEntityAlive();
        }
        
        @Override
        public boolean continueExecuting() {
            return super.continueExecuting() && (this.field_179456_a.func_175461_cl() || this.field_179456_a.getDistanceSqToEntity(this.field_179456_a.getAttackTarget()) > 9.0);
        }
        
        @Override
        public void startExecuting() {
            this.field_179455_b = -10;
            this.field_179456_a.getNavigator().clearPathEntity();
            this.field_179456_a.getLookHelper().setLookPositionWithEntity(this.field_179456_a.getAttackTarget(), 90.0f, 90.0f);
            this.field_179456_a.isAirBorne = true;
        }
        
        @Override
        public void resetTask() {
            this.field_179456_a.func_175463_b(0);
            this.field_179456_a.setAttackTarget(null);
            this.field_179456_a.field_175481_bq.func_179480_f();
        }
        
        @Override
        public void updateTask() {
            final EntityLivingBase var1 = this.field_179456_a.getAttackTarget();
            this.field_179456_a.getNavigator().clearPathEntity();
            this.field_179456_a.getLookHelper().setLookPositionWithEntity(var1, 90.0f, 90.0f);
            if (!this.field_179456_a.canEntityBeSeen(var1)) {
                this.field_179456_a.setAttackTarget(null);
            }
            else {
                ++this.field_179455_b;
                if (this.field_179455_b == 0) {
                    this.field_179456_a.func_175463_b(this.field_179456_a.getAttackTarget().getEntityId());
                    this.field_179456_a.worldObj.setEntityState(this.field_179456_a, (byte)21);
                }
                else if (this.field_179455_b >= this.field_179456_a.func_175464_ck()) {
                    float var2 = 1.0f;
                    if (this.field_179456_a.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                        var2 += 2.0f;
                    }
                    if (this.field_179456_a.func_175461_cl()) {
                        var2 += 2.0f;
                    }
                    var1.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.field_179456_a, this.field_179456_a), var2);
                    var1.attackEntityFrom(DamageSource.causeMobDamage(this.field_179456_a), (float)this.field_179456_a.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
                    this.field_179456_a.setAttackTarget(null);
                }
                else if (this.field_179455_b < 60 || this.field_179455_b % 20 == 0) {}
                super.updateTask();
            }
        }
    }
    
    class GuardianMoveHelper extends EntityMoveHelper
    {
        private EntityGuardian field_179930_g;
        private static final String __OBFID = "CL_00002209";
        
        public GuardianMoveHelper() {
            super(EntityGuardian.this);
            this.field_179930_g = EntityGuardian.this;
        }
        
        @Override
        public void onUpdateMoveHelper() {
            if (this.update && !this.field_179930_g.getNavigator().noPath()) {
                final double var1 = this.posX - this.field_179930_g.posX;
                double var2 = this.posY - this.field_179930_g.posY;
                final double var3 = this.posZ - this.field_179930_g.posZ;
                double var4 = var1 * var1 + var2 * var2 + var3 * var3;
                var4 = MathHelper.sqrt_double(var4);
                var2 /= var4;
                final float var5 = (float)(Math.atan2(var3, var1) * 180.0 / 3.141592653589793) - 90.0f;
                this.field_179930_g.rotationYaw = this.limitAngle(this.field_179930_g.rotationYaw, var5, 30.0f);
                this.field_179930_g.renderYawOffset = this.field_179930_g.rotationYaw;
                final float var6 = (float)(this.speed * this.field_179930_g.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                this.field_179930_g.setAIMoveSpeed(this.field_179930_g.getAIMoveSpeed() + (var6 - this.field_179930_g.getAIMoveSpeed()) * 0.125f);
                double var7 = Math.sin((this.field_179930_g.ticksExisted + this.field_179930_g.getEntityId()) * 0.5) * 0.05;
                final double var8 = Math.cos(this.field_179930_g.rotationYaw * 3.1415927f / 180.0f);
                final double var9 = Math.sin(this.field_179930_g.rotationYaw * 3.1415927f / 180.0f);
                final EntityGuardian field_179930_g = this.field_179930_g;
                field_179930_g.motionX += var7 * var8;
                final EntityGuardian field_179930_g2 = this.field_179930_g;
                field_179930_g2.motionZ += var7 * var9;
                var7 = Math.sin((this.field_179930_g.ticksExisted + this.field_179930_g.getEntityId()) * 0.75) * 0.05;
                final EntityGuardian field_179930_g3 = this.field_179930_g;
                field_179930_g3.motionY += var7 * (var9 + var8) * 0.25;
                final EntityGuardian field_179930_g4 = this.field_179930_g;
                field_179930_g4.motionY += this.field_179930_g.getAIMoveSpeed() * var2 * 0.1;
                final EntityLookHelper var10 = this.field_179930_g.getLookHelper();
                final double var11 = this.field_179930_g.posX + var1 / var4 * 2.0;
                final double var12 = this.field_179930_g.getEyeHeight() + this.field_179930_g.posY + var2 / var4 * 1.0;
                final double var13 = this.field_179930_g.posZ + var3 / var4 * 2.0;
                double var14 = var10.func_180423_e();
                double var15 = var10.func_180422_f();
                double var16 = var10.func_180421_g();
                if (!var10.func_180424_b()) {
                    var14 = var11;
                    var15 = var12;
                    var16 = var13;
                }
                this.field_179930_g.getLookHelper().setLookPosition(var14 + (var11 - var14) * 0.125, var15 + (var12 - var15) * 0.125, var16 + (var13 - var16) * 0.125, 10.0f, 40.0f);
                this.field_179930_g.func_175476_l(true);
            }
            else {
                this.field_179930_g.setAIMoveSpeed(0.0f);
                this.field_179930_g.func_175476_l(false);
            }
        }
    }
    
    class GuardianTargetSelector implements Predicate
    {
        private EntityGuardian field_179916_a;
        private static final String __OBFID = "CL_00002210";
        
        GuardianTargetSelector() {
            this.field_179916_a = EntityGuardian.this;
        }
        
        public boolean func_179915_a(final EntityLivingBase p_179915_1_) {
            return (p_179915_1_ instanceof EntityPlayer || p_179915_1_ instanceof EntitySquid) && p_179915_1_.getDistanceSqToEntity(this.field_179916_a) > 9.0;
        }
        
        public boolean apply(final Object p_apply_1_) {
            return this.func_179915_a((EntityLivingBase)p_apply_1_);
        }
    }
}
