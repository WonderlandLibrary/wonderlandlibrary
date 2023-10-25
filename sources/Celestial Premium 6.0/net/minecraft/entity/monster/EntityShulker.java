/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityShulker
extends EntityGolem
implements IMob {
    private static final UUID COVERED_ARMOR_BONUS_ID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
    private static final AttributeModifier COVERED_ARMOR_BONUS_MODIFIER = new AttributeModifier(COVERED_ARMOR_BONUS_ID, "Covered armor bonus", 20.0, 0).setSaved(false);
    protected static final DataParameter<EnumFacing> ATTACHED_FACE = EntityDataManager.createKey(EntityShulker.class, DataSerializers.FACING);
    protected static final DataParameter<Optional<BlockPos>> ATTACHED_BLOCK_POS = EntityDataManager.createKey(EntityShulker.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Byte> PEEK_TICK = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> field_190770_bw = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
    public static final EnumDyeColor field_190771_bx = EnumDyeColor.PURPLE;
    private float prevPeekAmount;
    private float peekAmount;
    private BlockPos currentAttachmentPosition;
    private int clientSideTeleportInterpolation;

    public EntityShulker(World worldIn) {
        super(worldIn);
        this.setSize(1.0f, 1.0f);
        this.prevRenderYawOffset = 180.0f;
        this.renderYawOffset = 180.0f;
        this.isImmuneToFire = true;
        this.currentAttachmentPosition = null;
        this.experienceValue = 5;
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.renderYawOffset = 180.0f;
        this.prevRenderYawOffset = 180.0f;
        this.rotationYaw = 180.0f;
        this.prevRotationYaw = 180.0f;
        this.rotationYawHead = 180.0f;
        this.prevRotationYawHead = 180.0f;
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(4, new AIAttack());
        this.tasks.addTask(7, new AIPeek());
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, true, new Class[0]));
        this.targetTasks.addTask(2, new AIAttackNearest(this));
        this.targetTasks.addTask(3, new AIDefenseAttack(this));
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHULKER_AMBIENT;
    }

    @Override
    public void playLivingSound() {
        if (!this.isClosed()) {
            super.playLivingSound();
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHULKER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.isClosed() ? SoundEvents.ENTITY_SHULKER_HURT_CLOSED : SoundEvents.ENTITY_SHULKER_HURT;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ATTACHED_FACE, EnumFacing.DOWN);
        this.dataManager.register(ATTACHED_BLOCK_POS, Optional.absent());
        this.dataManager.register(PEEK_TICK, (byte)0);
        this.dataManager.register(field_190770_bw, (byte)field_190771_bx.getMetadata());
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0);
    }

    @Override
    protected EntityBodyHelper createBodyHelper() {
        return new BodyHelper(this);
    }

    public static void registerFixesShulker(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityShulker.class);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(ATTACHED_FACE, EnumFacing.getFront(compound.getByte("AttachFace")));
        this.dataManager.set(PEEK_TICK, compound.getByte("Peek"));
        this.dataManager.set(field_190770_bw, compound.getByte("Color"));
        if (compound.hasKey("APX")) {
            int i = compound.getInteger("APX");
            int j = compound.getInteger("APY");
            int k = compound.getInteger("APZ");
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(new BlockPos(i, j, k)));
        } else {
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.absent());
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("AttachFace", (byte)this.dataManager.get(ATTACHED_FACE).getIndex());
        compound.setByte("Peek", this.dataManager.get(PEEK_TICK));
        compound.setByte("Color", this.dataManager.get(field_190770_bw));
        BlockPos blockpos = this.getAttachmentPos();
        if (blockpos != null) {
            compound.setInteger("APX", blockpos.getX());
            compound.setInteger("APY", blockpos.getY());
            compound.setInteger("APZ", blockpos.getZ());
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        BlockPos blockpos = this.dataManager.get(ATTACHED_BLOCK_POS).orNull();
        if (blockpos == null && !this.world.isRemote) {
            blockpos = new BlockPos(this);
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
        }
        if (this.isRiding()) {
            float f;
            blockpos = null;
            this.rotationYaw = f = this.getRidingEntity().rotationYaw;
            this.renderYawOffset = f;
            this.prevRenderYawOffset = f;
            this.clientSideTeleportInterpolation = 0;
        } else if (!this.world.isRemote) {
            BlockPos blockpos2;
            BlockPos blockpos1;
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            if (iblockstate.getMaterial() != Material.AIR) {
                if (iblockstate.getBlock() == Blocks.PISTON_EXTENSION) {
                    EnumFacing enumfacing = iblockstate.getValue(BlockPistonBase.FACING);
                    if (this.world.isAirBlock(blockpos.offset(enumfacing))) {
                        blockpos = blockpos.offset(enumfacing);
                        this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
                    } else {
                        this.tryTeleportToNewPosition();
                    }
                } else if (iblockstate.getBlock() == Blocks.PISTON_HEAD) {
                    EnumFacing enumfacing3 = iblockstate.getValue(BlockPistonExtension.FACING);
                    if (this.world.isAirBlock(blockpos.offset(enumfacing3))) {
                        blockpos = blockpos.offset(enumfacing3);
                        this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
                    } else {
                        this.tryTeleportToNewPosition();
                    }
                } else {
                    this.tryTeleportToNewPosition();
                }
            }
            if (!this.world.isBlockNormalCube(blockpos1 = blockpos.offset(this.getAttachmentFacing()), false)) {
                boolean flag = false;
                for (EnumFacing enumfacing1 : EnumFacing.values()) {
                    blockpos1 = blockpos.offset(enumfacing1);
                    if (!this.world.isBlockNormalCube(blockpos1, false)) continue;
                    this.dataManager.set(ATTACHED_FACE, enumfacing1);
                    flag = true;
                    break;
                }
                if (!flag) {
                    this.tryTeleportToNewPosition();
                }
            }
            if (this.world.isBlockNormalCube(blockpos2 = blockpos.offset(this.getAttachmentFacing().getOpposite()), false)) {
                this.tryTeleportToNewPosition();
            }
        }
        float f1 = (float)this.getPeekTick() * 0.01f;
        this.prevPeekAmount = this.peekAmount;
        if (this.peekAmount > f1) {
            this.peekAmount = MathHelper.clamp(this.peekAmount - 0.05f, f1, 1.0f);
        } else if (this.peekAmount < f1) {
            this.peekAmount = MathHelper.clamp(this.peekAmount + 0.05f, 0.0f, f1);
        }
        if (blockpos != null) {
            List<Entity> list;
            if (this.world.isRemote) {
                if (this.clientSideTeleportInterpolation > 0 && this.currentAttachmentPosition != null) {
                    --this.clientSideTeleportInterpolation;
                } else {
                    this.currentAttachmentPosition = blockpos;
                }
            }
            this.posX = (double)blockpos.getX() + 0.5;
            this.posY = blockpos.getY();
            this.posZ = (double)blockpos.getZ() + 0.5;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
            double d3 = 0.5 - (double)MathHelper.sin((0.5f + this.peekAmount) * (float)Math.PI) * 0.5;
            double d4 = 0.5 - (double)MathHelper.sin((0.5f + this.prevPeekAmount) * (float)Math.PI) * 0.5;
            double d5 = d3 - d4;
            double d0 = 0.0;
            double d1 = 0.0;
            double d2 = 0.0;
            EnumFacing enumfacing2 = this.getAttachmentFacing();
            switch (enumfacing2) {
                case DOWN: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0 + d3, this.posZ + 0.5));
                    d1 = d5;
                    break;
                }
                case UP: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5, this.posY - d3, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5));
                    d1 = -d5;
                    break;
                }
                case NORTH: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5 + d3));
                    d2 = d5;
                    break;
                }
                case SOUTH: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5 - d3, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5));
                    d2 = -d5;
                    break;
                }
                case WEST: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5, this.posX + 0.5 + d3, this.posY + 1.0, this.posZ + 0.5));
                    d0 = d5;
                    break;
                }
                case EAST: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5 - d3, this.posY, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5));
                    d0 = -d5;
                }
            }
            if (d5 > 0.0 && !(list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox())).isEmpty()) {
                for (Entity entity : list) {
                    if (entity instanceof EntityShulker || entity.noClip) continue;
                    entity.moveEntity(MoverType.SHULKER, d0, d1, d2);
                }
            }
        }
    }

    @Override
    public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_) {
        if (x == MoverType.SHULKER_BOX) {
            this.tryTeleportToNewPosition();
        } else {
            super.moveEntity(x, p_70091_2_, p_70091_4_, p_70091_6_);
        }
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
        if (this.dataManager != null && this.ticksExisted != 0) {
            Optional<BlockPos> optional = this.dataManager.get(ATTACHED_BLOCK_POS);
            Optional<BlockPos> optional1 = Optional.of(new BlockPos(x, y, z));
            if (!optional1.equals(optional)) {
                this.dataManager.set(ATTACHED_BLOCK_POS, optional1);
                this.dataManager.set(PEEK_TICK, (byte)0);
                this.isAirBorne = true;
            }
        }
    }

    protected boolean tryTeleportToNewPosition() {
        if (!this.isAIDisabled() && this.isEntityAlive()) {
            BlockPos blockpos = new BlockPos(this);
            for (int i = 0; i < 5; ++i) {
                BlockPos blockpos1 = blockpos.add(8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17));
                if (blockpos1.getY() <= 0 || !this.world.isAirBlock(blockpos1) || !this.world.func_191503_g(this) || !this.world.getCollisionBoxes(this, new AxisAlignedBB(blockpos1)).isEmpty()) continue;
                boolean flag = false;
                for (EnumFacing enumfacing : EnumFacing.values()) {
                    if (!this.world.isBlockNormalCube(blockpos1.offset(enumfacing), false)) continue;
                    this.dataManager.set(ATTACHED_FACE, enumfacing);
                    flag = true;
                    break;
                }
                if (!flag) continue;
                this.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1.0f, 1.0f);
                this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos1));
                this.dataManager.set(PEEK_TICK, (byte)0);
                this.setAttackTarget(null);
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevRenderYawOffset = 180.0f;
        this.renderYawOffset = 180.0f;
        this.rotationYaw = 180.0f;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        BlockPos blockpos;
        if (ATTACHED_BLOCK_POS.equals(key) && this.world.isRemote && !this.isRiding() && (blockpos = this.getAttachmentPos()) != null) {
            if (this.currentAttachmentPosition == null) {
                this.currentAttachmentPosition = blockpos;
            } else {
                this.clientSideTeleportInterpolation = 6;
            }
            this.posX = (double)blockpos.getX() + 0.5;
            this.posY = blockpos.getY();
            this.posZ = (double)blockpos.getZ() + 0.5;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
        }
        super.notifyDataManagerChange(key);
    }

    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.newPosRotationIncrements = 0;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity;
        if (this.isClosed() && (entity = source.getSourceOfDamage()) instanceof EntityArrow) {
            return false;
        }
        if (super.attackEntityFrom(source, amount)) {
            if ((double)this.getHealth() < (double)this.getMaxHealth() * 0.5 && this.rand.nextInt(4) == 0) {
                this.tryTeleportToNewPosition();
            }
            return true;
        }
        return false;
    }

    private boolean isClosed() {
        return this.getPeekTick() == 0;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.isEntityAlive() ? this.getEntityBoundingBox() : null;
    }

    public EnumFacing getAttachmentFacing() {
        return this.dataManager.get(ATTACHED_FACE);
    }

    @Nullable
    public BlockPos getAttachmentPos() {
        return this.dataManager.get(ATTACHED_BLOCK_POS).orNull();
    }

    public void setAttachmentPos(@Nullable BlockPos pos) {
        this.dataManager.set(ATTACHED_BLOCK_POS, Optional.fromNullable(pos));
    }

    public int getPeekTick() {
        return this.dataManager.get(PEEK_TICK).byteValue();
    }

    public void updateArmorModifier(int p_184691_1_) {
        if (!this.world.isRemote) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(COVERED_ARMOR_BONUS_MODIFIER);
            if (p_184691_1_ == 0) {
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(COVERED_ARMOR_BONUS_MODIFIER);
                this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0f, 1.0f);
            } else {
                this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0f, 1.0f);
            }
        }
        this.dataManager.set(PEEK_TICK, (byte)p_184691_1_);
    }

    public float getClientPeekAmount(float p_184688_1_) {
        return this.prevPeekAmount + (this.peekAmount - this.prevPeekAmount) * p_184688_1_;
    }

    public int getClientTeleportInterp() {
        return this.clientSideTeleportInterpolation;
    }

    public BlockPos getOldAttachPos() {
        return this.currentAttachmentPosition;
    }

    @Override
    public float getEyeHeight() {
        return 0.5f;
    }

    @Override
    public int getVerticalFaceSpeed() {
        return 180;
    }

    @Override
    public int getHorizontalFaceSpeed() {
        return 180;
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
    }

    @Override
    public float getCollisionBorderSize() {
        return 0.0f;
    }

    public boolean isAttachedToBlock() {
        return this.currentAttachmentPosition != null && this.getAttachmentPos() != null;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SHULKER;
    }

    public EnumDyeColor func_190769_dn() {
        return EnumDyeColor.byMetadata(this.dataManager.get(field_190770_bw).byteValue());
    }

    class BodyHelper
    extends EntityBodyHelper {
        public BodyHelper(EntityLivingBase p_i47062_2_) {
            super(p_i47062_2_);
        }

        @Override
        public void updateRenderAngles() {
        }
    }

    class AIPeek
    extends EntityAIBase {
        private int peekTime;

        private AIPeek() {
        }

        @Override
        public boolean shouldExecute() {
            return EntityShulker.this.getAttackTarget() == null && EntityShulker.this.rand.nextInt(40) == 0;
        }

        @Override
        public boolean continueExecuting() {
            return EntityShulker.this.getAttackTarget() == null && this.peekTime > 0;
        }

        @Override
        public void startExecuting() {
            this.peekTime = 20 * (1 + EntityShulker.this.rand.nextInt(3));
            EntityShulker.this.updateArmorModifier(30);
        }

        @Override
        public void resetTask() {
            if (EntityShulker.this.getAttackTarget() == null) {
                EntityShulker.this.updateArmorModifier(0);
            }
        }

        @Override
        public void updateTask() {
            --this.peekTime;
        }
    }

    static class AIDefenseAttack
    extends EntityAINearestAttackableTarget<EntityLivingBase> {
        public AIDefenseAttack(EntityShulker shulker) {
            super(shulker, EntityLivingBase.class, 10, true, false, new Predicate<EntityLivingBase>(){

                @Override
                public boolean apply(@Nullable EntityLivingBase p_apply_1_) {
                    return p_apply_1_ instanceof IMob;
                }
            });
        }

        @Override
        public boolean shouldExecute() {
            return this.taskOwner.getTeam() == null ? false : super.shouldExecute();
        }

        @Override
        protected AxisAlignedBB getTargetableArea(double targetDistance) {
            EnumFacing enumfacing = ((EntityShulker)this.taskOwner).getAttachmentFacing();
            if (enumfacing.getAxis() == EnumFacing.Axis.X) {
                return this.taskOwner.getEntityBoundingBox().expand(4.0, targetDistance, targetDistance);
            }
            return enumfacing.getAxis() == EnumFacing.Axis.Z ? this.taskOwner.getEntityBoundingBox().expand(targetDistance, targetDistance, 4.0) : this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0, targetDistance);
        }
    }

    class AIAttackNearest
    extends EntityAINearestAttackableTarget<EntityPlayer> {
        public AIAttackNearest(EntityShulker shulker) {
            super((EntityCreature)shulker, EntityPlayer.class, true);
        }

        @Override
        public boolean shouldExecute() {
            return EntityShulker.this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? false : super.shouldExecute();
        }

        @Override
        protected AxisAlignedBB getTargetableArea(double targetDistance) {
            EnumFacing enumfacing = ((EntityShulker)this.taskOwner).getAttachmentFacing();
            if (enumfacing.getAxis() == EnumFacing.Axis.X) {
                return this.taskOwner.getEntityBoundingBox().expand(4.0, targetDistance, targetDistance);
            }
            return enumfacing.getAxis() == EnumFacing.Axis.Z ? this.taskOwner.getEntityBoundingBox().expand(targetDistance, targetDistance, 4.0) : this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0, targetDistance);
        }
    }

    class AIAttack
    extends EntityAIBase {
        private int attackTime;

        public AIAttack() {
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = EntityShulker.this.getAttackTarget();
            if (entitylivingbase != null && entitylivingbase.isEntityAlive()) {
                return EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
            }
            return false;
        }

        @Override
        public void startExecuting() {
            this.attackTime = 20;
            EntityShulker.this.updateArmorModifier(100);
        }

        @Override
        public void resetTask() {
            EntityShulker.this.updateArmorModifier(0);
        }

        @Override
        public void updateTask() {
            if (EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
                --this.attackTime;
                EntityLivingBase entitylivingbase = EntityShulker.this.getAttackTarget();
                EntityShulker.this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 180.0f, 180.0f);
                double d0 = EntityShulker.this.getDistanceSqToEntity(entitylivingbase);
                if (d0 < 400.0) {
                    if (this.attackTime <= 0) {
                        this.attackTime = 20 + EntityShulker.this.rand.nextInt(10) * 20 / 2;
                        EntityShulkerBullet entityshulkerbullet = new EntityShulkerBullet(EntityShulker.this.world, EntityShulker.this, entitylivingbase, EntityShulker.this.getAttachmentFacing().getAxis());
                        EntityShulker.this.world.spawnEntityInWorld(entityshulkerbullet);
                        EntityShulker.this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 2.0f, (EntityShulker.this.rand.nextFloat() - EntityShulker.this.rand.nextFloat()) * 0.2f + 1.0f);
                    }
                } else {
                    EntityShulker.this.setAttackTarget(null);
                }
                super.updateTask();
            }
        }
    }
}

