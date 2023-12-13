/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityRabbit
extends EntityAnimal {
    private AIAvoidEntity field_175539_bk;
    private int field_175540_bm = 0;
    private int field_175535_bn = 0;
    private boolean field_175536_bo = false;
    private boolean field_175537_bp = false;
    private int field_175538_bq = 0;
    private EnumMoveType field_175542_br = EnumMoveType.HOP;
    private int field_175541_bs = 0;
    private EntityPlayer field_175543_bt = null;
    private static final String __OBFID = "CL_00002242";

    public EntityRabbit(World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 0.7f);
        this.jumpHelper = new RabbitJumpHelper(this);
        this.moveHelper = new RabbitMoveHelper();
        ((PathNavigateGround)this.getNavigator()).func_179690_a(true);
        this.navigator.func_179678_a(2.5f);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new AIPanic(1.33));
        this.tasks.addTask(2, new EntityAITempt(this, 1.0, Items.carrot, false));
        this.tasks.addTask(3, new EntityAIMate(this, 0.8));
        this.tasks.addTask(5, new AIRaidFarm());
        this.tasks.addTask(5, new EntityAIWander(this, 0.6));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0f));
        this.field_175539_bk = new AIAvoidEntity(new Predicate(){
            private static final String __OBFID = "CL_00002241";

            public boolean func_180086_a(Entity p_180086_1_) {
                return p_180086_1_ instanceof EntityWolf;
            }

            public boolean apply(Object p_apply_1_) {
                return this.func_180086_a((Entity)p_apply_1_);
            }
        }, 16.0f, 1.33, 1.33);
        this.tasks.addTask(4, this.field_175539_bk);
        this.func_175515_b(0.0);
    }

    @Override
    protected float func_175134_bD() {
        return this.moveHelper.isUpdating() && this.moveHelper.func_179919_e() > this.posY + 0.5 ? 0.5f : this.field_175542_br.func_180074_b();
    }

    public void func_175522_a(EnumMoveType p_175522_1_) {
        this.field_175542_br = p_175522_1_;
    }

    public float func_175521_o(float p_175521_1_) {
        return this.field_175535_bn == 0 ? 0.0f : ((float)this.field_175540_bm + p_175521_1_) / (float)this.field_175535_bn;
    }

    public void func_175515_b(double p_175515_1_) {
        this.getNavigator().setSpeed(p_175515_1_);
        this.moveHelper.setMoveTo(this.moveHelper.func_179917_d(), this.moveHelper.func_179919_e(), this.moveHelper.func_179918_f(), p_175515_1_);
    }

    public void func_175519_a(boolean p_175519_1_, EnumMoveType p_175519_2_) {
        super.setJumping(p_175519_1_);
        if (!p_175519_1_) {
            if (this.field_175542_br == EnumMoveType.ATTACK) {
                this.field_175542_br = EnumMoveType.HOP;
            }
        } else {
            this.func_175515_b(1.5 * (double)p_175519_2_.func_180072_a());
            this.playSound(this.func_175516_ck(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) * 0.8f);
        }
        this.field_175536_bo = p_175519_1_;
    }

    public void func_175524_b(EnumMoveType p_175524_1_) {
        this.func_175519_a(true, p_175524_1_);
        this.field_175535_bn = p_175524_1_.func_180073_d();
        this.field_175540_bm = 0;
    }

    public boolean func_175523_cj() {
        return this.field_175536_bo;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, (byte)0);
    }

    @Override
    public void updateAITasks() {
        if (this.moveHelper.getSpeed() > 0.8) {
            this.func_175522_a(EnumMoveType.SPRINT);
        } else if (this.field_175542_br != EnumMoveType.ATTACK) {
            this.func_175522_a(EnumMoveType.HOP);
        }
        if (this.field_175538_bq > 0) {
            --this.field_175538_bq;
        }
        if (this.field_175541_bs > 0) {
            this.field_175541_bs -= this.rand.nextInt(3);
            if (this.field_175541_bs < 0) {
                this.field_175541_bs = 0;
            }
        }
        if (this.onGround) {
            RabbitJumpHelper var4;
            EntityLivingBase var1;
            if (!this.field_175537_bp) {
                this.func_175519_a(false, EnumMoveType.NONE);
                this.func_175517_cu();
            }
            if (this.func_175531_cl() == 99 && this.field_175538_bq == 0 && (var1 = this.getAttackTarget()) != null && this.getDistanceSqToEntity(var1) < 16.0) {
                this.func_175533_a(var1.posX, var1.posZ);
                this.moveHelper.setMoveTo(var1.posX, var1.posY, var1.posZ, this.moveHelper.getSpeed());
                this.func_175524_b(EnumMoveType.ATTACK);
                this.field_175537_bp = true;
            }
            if (!(var4 = (RabbitJumpHelper)this.jumpHelper).func_180067_c()) {
                if (this.moveHelper.isUpdating() && this.field_175538_bq == 0) {
                    PathEntity var2 = this.navigator.getPath();
                    Vec3 var3 = new Vec3(this.moveHelper.func_179917_d(), this.moveHelper.func_179919_e(), this.moveHelper.func_179918_f());
                    if (var2 != null && var2.getCurrentPathIndex() < var2.getCurrentPathLength()) {
                        var3 = var2.getPosition(this);
                    }
                    this.func_175533_a(var3.xCoord, var3.zCoord);
                    this.func_175524_b(this.field_175542_br);
                }
            } else if (!var4.func_180065_d()) {
                this.func_175518_cr();
            }
        }
        this.field_175537_bp = this.onGround;
    }

    public void handleLavaMovement() {
    }

    private void func_175533_a(double p_175533_1_, double p_175533_3_) {
        this.rotationYaw = (float)(Math.atan2(p_175533_3_ - this.posZ, p_175533_1_ - this.posX) * 180.0 / Math.PI) - 90.0f;
    }

    private void func_175518_cr() {
        ((RabbitJumpHelper)this.jumpHelper).func_180066_a(true);
    }

    private void func_175520_cs() {
        ((RabbitJumpHelper)this.jumpHelper).func_180066_a(false);
    }

    private void func_175530_ct() {
        this.field_175538_bq = this.func_175532_cm();
    }

    private void func_175517_cu() {
        this.func_175530_ct();
        this.func_175520_cs();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.field_175540_bm != this.field_175535_bn) {
            if (this.field_175540_bm == 0 && !this.worldObj.isRemote) {
                this.worldObj.setEntityState(this, (byte)1);
            }
            ++this.field_175540_bm;
        } else if (this.field_175535_bn != 0) {
            this.field_175540_bm = 0;
            this.field_175535_bn = 0;
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3f);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("RabbitType", this.func_175531_cl());
        tagCompound.setInteger("MoreCarrotTicks", this.field_175541_bs);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.func_175529_r(tagCompund.getInteger("RabbitType"));
        this.field_175541_bs = tagCompund.getInteger("MoreCarrotTicks");
    }

    protected String func_175516_ck() {
        return "mob.rabbit.hop";
    }

    @Override
    protected String getLivingSound() {
        return "mob.rabbit.idle";
    }

    @Override
    protected String getHurtSound() {
        return "mob.rabbit.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.rabbit.death";
    }

    @Override
    public boolean attackEntityAsMob(Entity p_70652_1_) {
        if (this.func_175531_cl() == 99) {
            this.playSound("mob.attack", 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            return p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0f);
        }
        return p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0f);
    }

    @Override
    public int getTotalArmorValue() {
        return this.func_175531_cl() == 99 ? 8 : super.getTotalArmorValue();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return this.func_180431_b(source) ? false : super.attackEntityFrom(source, amount);
    }

    @Override
    protected void addRandomArmor() {
        this.entityDropItem(new ItemStack(Items.rabbit_foot, 1), 0.0f);
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int var4;
        int var3 = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_);
        for (var4 = 0; var4 < var3; ++var4) {
            this.dropItem(Items.rabbit_hide, 1);
        }
        var3 = this.rand.nextInt(2);
        for (var4 = 0; var4 < var3; ++var4) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_rabbit, 1);
                continue;
            }
            this.dropItem(Items.rabbit, 1);
        }
    }

    private boolean func_175525_a(Item p_175525_1_) {
        return p_175525_1_ == Items.carrot || p_175525_1_ == Items.golden_carrot || p_175525_1_ == Item.getItemFromBlock(Blocks.yellow_flower);
    }

    public EntityRabbit func_175526_b(EntityAgeable p_175526_1_) {
        EntityRabbit var2 = new EntityRabbit(this.worldObj);
        if (p_175526_1_ instanceof EntityRabbit) {
            var2.func_175529_r(this.rand.nextBoolean() ? this.func_175531_cl() : ((EntityRabbit)p_175526_1_).func_175531_cl());
        }
        return var2;
    }

    @Override
    public boolean isBreedingItem(ItemStack p_70877_1_) {
        return p_70877_1_ != null && this.func_175525_a(p_70877_1_.getItem());
    }

    public int func_175531_cl() {
        return this.dataWatcher.getWatchableObjectByte(18);
    }

    public void func_175529_r(int p_175529_1_) {
        if (p_175529_1_ == 99) {
            this.tasks.removeTask(this.field_175539_bk);
            this.tasks.addTask(4, new AIEvilAttack());
            this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget((EntityCreature)this, EntityPlayer.class, true));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget((EntityCreature)this, EntityWolf.class, true));
            if (!this.hasCustomName()) {
                this.setCustomNameTag(StatCollector.translateToLocal("entity.KillerBunny.name"));
            }
        }
        this.dataWatcher.updateObject(18, (byte)p_175529_1_);
    }

    @Override
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
        IEntityLivingData p_180482_2_1 = super.func_180482_a(p_180482_1_, p_180482_2_);
        int var3 = this.rand.nextInt(6);
        boolean var4 = false;
        if (p_180482_2_1 instanceof RabbitTypeData) {
            var3 = ((RabbitTypeData)p_180482_2_1).field_179427_a;
            var4 = true;
        } else {
            p_180482_2_1 = new RabbitTypeData(var3);
        }
        this.func_175529_r(var3);
        if (var4) {
            this.setGrowingAge(-24000);
        }
        return p_180482_2_1;
    }

    private boolean func_175534_cv() {
        return this.field_175541_bs == 0;
    }

    protected int func_175532_cm() {
        return this.field_175542_br.func_180075_c();
    }

    protected void func_175528_cn() {
        this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, 0.0, 0.0, 0.0, Block.getStateId(Blocks.carrots.getStateFromMeta(7)));
        this.field_175541_bs = 100;
    }

    @Override
    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 1) {
            this.func_174808_Z();
            this.field_175535_bn = 10;
            this.field_175540_bm = 0;
        } else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    @Override
    public EntityAgeable createChild(EntityAgeable p_90011_1_) {
        return this.func_175526_b(p_90011_1_);
    }

    class AIAvoidEntity
    extends EntityAIAvoidEntity {
        private EntityRabbit field_179511_d;
        private static final String __OBFID = "CL_00002238";

        public AIAvoidEntity(Predicate p_i45865_2_, float p_i45865_3_, double p_i45865_4_, double p_i45865_6_) {
            super(EntityRabbit.this, p_i45865_2_, p_i45865_3_, p_i45865_4_, p_i45865_6_);
            this.field_179511_d = EntityRabbit.this;
        }

        @Override
        public void updateTask() {
            super.updateTask();
        }
    }

    class AIEvilAttack
    extends EntityAIAttackOnCollide {
        private static final String __OBFID = "CL_00002240";

        public AIEvilAttack() {
            super(EntityRabbit.this, EntityLivingBase.class, 1.4, true);
        }

        @Override
        protected double func_179512_a(EntityLivingBase p_179512_1_) {
            return 4.0f + p_179512_1_.width;
        }
    }

    class AIPanic
    extends EntityAIPanic {
        private EntityRabbit field_179486_b;
        private static final String __OBFID = "CL_00002234";

        public AIPanic(double p_i45861_2_) {
            super(EntityRabbit.this, p_i45861_2_);
            this.field_179486_b = EntityRabbit.this;
        }

        @Override
        public void updateTask() {
            super.updateTask();
            this.field_179486_b.func_175515_b(this.speed);
        }
    }

    class AIRaidFarm
    extends EntityAIMoveToBlock {
        private boolean field_179498_d;
        private boolean field_179499_e;
        private static final String __OBFID = "CL_00002233";

        public AIRaidFarm() {
            super(EntityRabbit.this, 0.7f, 16);
            this.field_179499_e = false;
        }

        @Override
        public boolean shouldExecute() {
            if (this.field_179496_a <= 0) {
                if (!EntityRabbit.this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                    return false;
                }
                this.field_179499_e = false;
                this.field_179498_d = EntityRabbit.this.func_175534_cv();
            }
            return super.shouldExecute();
        }

        @Override
        public boolean continueExecuting() {
            return this.field_179499_e && super.continueExecuting();
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        @Override
        public void updateTask() {
            super.updateTask();
            EntityRabbit.this.getLookHelper().setLookPosition((double)this.field_179494_b.getX() + 0.5, this.field_179494_b.getY() + 1, (double)this.field_179494_b.getZ() + 0.5, 10.0f, EntityRabbit.this.getVerticalFaceSpeed());
            if (this.func_179487_f()) {
                World var1 = EntityRabbit.this.worldObj;
                BlockPos var2 = this.field_179494_b.offsetUp();
                IBlockState var3 = var1.getBlockState(var2);
                Block var4 = var3.getBlock();
                if (this.field_179499_e && var4 instanceof BlockCarrot && (Integer)var3.getValue(BlockCarrot.AGE) == 7) {
                    var1.setBlockState(var2, Blocks.air.getDefaultState(), 2);
                    var1.destroyBlock(var2, true);
                    EntityRabbit.this.func_175528_cn();
                }
                this.field_179499_e = false;
                this.field_179496_a = 10;
            }
        }

        @Override
        protected boolean func_179488_a(World worldIn, BlockPos p_179488_2_) {
            IBlockState var4;
            Block var3 = worldIn.getBlockState(p_179488_2_).getBlock();
            if (var3 == Blocks.farmland && (var3 = (var4 = worldIn.getBlockState(p_179488_2_ = p_179488_2_.offsetUp())).getBlock()) instanceof BlockCarrot && (Integer)var4.getValue(BlockCarrot.AGE) == 7 && this.field_179498_d && !this.field_179499_e) {
                this.field_179499_e = true;
                return true;
            }
            return false;
        }
    }

    static enum EnumMoveType {
        NONE("NONE", 0, 0.0f, 0.0f, 30, 1),
        HOP("HOP", 1, 0.8f, 0.2f, 20, 10),
        STEP("STEP", 2, 1.0f, 0.45f, 14, 14),
        SPRINT("SPRINT", 3, 1.75f, 0.4f, 1, 8),
        ATTACK("ATTACK", 4, 2.0f, 0.7f, 7, 8);

        private final float field_180076_f;
        private final float field_180077_g;
        private final int field_180084_h;
        private final int field_180085_i;
        private static final EnumMoveType[] $VALUES;
        private static final String __OBFID = "CL_00002239";

        static {
            $VALUES = new EnumMoveType[]{NONE, HOP, STEP, SPRINT, ATTACK};
        }

        private EnumMoveType(String p_i45866_1_, int p_i45866_2_, float p_i45866_3_, float p_i45866_4_, int p_i45866_5_, int p_i45866_6_) {
            this.field_180076_f = p_i45866_3_;
            this.field_180077_g = p_i45866_4_;
            this.field_180084_h = p_i45866_5_;
            this.field_180085_i = p_i45866_6_;
        }

        public float func_180072_a() {
            return this.field_180076_f;
        }

        public float func_180074_b() {
            return this.field_180077_g;
        }

        public int func_180075_c() {
            return this.field_180084_h;
        }

        public int func_180073_d() {
            return this.field_180085_i;
        }
    }

    public class RabbitJumpHelper
    extends EntityJumpHelper {
        private EntityRabbit field_180070_c;
        private boolean field_180068_d;
        private static final String __OBFID = "CL_00002236";

        public RabbitJumpHelper(EntityRabbit p_i45863_2_) {
            super(p_i45863_2_);
            this.field_180068_d = false;
            this.field_180070_c = p_i45863_2_;
        }

        public boolean func_180067_c() {
            return this.isJumping;
        }

        public boolean func_180065_d() {
            return this.field_180068_d;
        }

        public void func_180066_a(boolean p_180066_1_) {
            this.field_180068_d = p_180066_1_;
        }

        @Override
        public void doJump() {
            if (this.isJumping) {
                this.field_180070_c.func_175524_b(EnumMoveType.STEP);
                this.isJumping = false;
            }
        }
    }

    class RabbitMoveHelper
    extends EntityMoveHelper {
        private EntityRabbit field_179929_g;
        private static final String __OBFID = "CL_00002235";

        public RabbitMoveHelper() {
            super(EntityRabbit.this);
            this.field_179929_g = EntityRabbit.this;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.field_179929_g.onGround && !this.field_179929_g.func_175523_cj()) {
                this.field_179929_g.func_175515_b(0.0);
            }
            super.onUpdateMoveHelper();
        }
    }

    public static class RabbitTypeData
    implements IEntityLivingData {
        public int field_179427_a;
        private static final String __OBFID = "CL_00002237";

        public RabbitTypeData(int p_i45864_1_) {
            this.field_179427_a = p_i45864_1_;
        }
    }
}

