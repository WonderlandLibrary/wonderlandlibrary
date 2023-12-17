// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import java.util.Random;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.EntityArrow;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundCategory;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nullable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import com.google.common.base.Optional;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public class EntityShulker extends EntityGolem implements IMob
{
    private static final UUID COVERED_ARMOR_BONUS_ID;
    private static final AttributeModifier COVERED_ARMOR_BONUS_MODIFIER;
    protected static final DataParameter<EnumFacing> ATTACHED_FACE;
    protected static final DataParameter<Optional<BlockPos>> ATTACHED_BLOCK_POS;
    protected static final DataParameter<Byte> PEEK_TICK;
    protected static final DataParameter<Byte> COLOR;
    public static final EnumDyeColor DEFAULT_COLOR;
    private float prevPeekAmount;
    private float peekAmount;
    private BlockPos currentAttachmentPosition;
    private int clientSideTeleportInterpolation;
    
    public EntityShulker(final World worldIn) {
        super(worldIn);
        this.setSize(1.0f, 1.0f);
        this.prevRenderYawOffset = 180.0f;
        this.renderYawOffset = 180.0f;
        this.isImmuneToFire = true;
        this.currentAttachmentPosition = null;
        this.experienceValue = 5;
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
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
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, (Class<?>[])new Class[0]));
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
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return this.isClosed() ? SoundEvents.ENTITY_SHULKER_HURT_CLOSED : SoundEvents.ENTITY_SHULKER_HURT;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityShulker.ATTACHED_FACE, EnumFacing.DOWN);
        this.dataManager.register(EntityShulker.ATTACHED_BLOCK_POS, (Optional<BlockPos>)Optional.absent());
        this.dataManager.register(EntityShulker.PEEK_TICK, (Byte)0);
        this.dataManager.register(EntityShulker.COLOR, (byte)EntityShulker.DEFAULT_COLOR.getMetadata());
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
    
    public static void registerFixesShulker(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityShulker.class);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(EntityShulker.ATTACHED_FACE, EnumFacing.byIndex(compound.getByte("AttachFace")));
        this.dataManager.set(EntityShulker.PEEK_TICK, compound.getByte("Peek"));
        this.dataManager.set(EntityShulker.COLOR, compound.getByte("Color"));
        if (compound.hasKey("APX")) {
            final int i = compound.getInteger("APX");
            final int j = compound.getInteger("APY");
            final int k = compound.getInteger("APZ");
            this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, (Optional<BlockPos>)Optional.of((Object)new BlockPos(i, j, k)));
        }
        else {
            this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, (Optional<BlockPos>)Optional.absent());
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("AttachFace", (byte)this.dataManager.get(EntityShulker.ATTACHED_FACE).getIndex());
        compound.setByte("Peek", this.dataManager.get(EntityShulker.PEEK_TICK));
        compound.setByte("Color", this.dataManager.get(EntityShulker.COLOR));
        final BlockPos blockpos = this.getAttachmentPos();
        if (blockpos != null) {
            compound.setInteger("APX", blockpos.getX());
            compound.setInteger("APY", blockpos.getY());
            compound.setInteger("APZ", blockpos.getZ());
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        BlockPos blockpos = (BlockPos)this.dataManager.get(EntityShulker.ATTACHED_BLOCK_POS).orNull();
        if (blockpos == null && !this.world.isRemote) {
            blockpos = new BlockPos(this);
            this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, (Optional<BlockPos>)Optional.of((Object)blockpos));
        }
        if (this.isRiding()) {
            blockpos = null;
            final float f = this.getRidingEntity().rotationYaw;
            this.rotationYaw = f;
            this.renderYawOffset = f;
            this.prevRenderYawOffset = f;
            this.clientSideTeleportInterpolation = 0;
        }
        else if (!this.world.isRemote) {
            final IBlockState iblockstate = this.world.getBlockState(blockpos);
            if (iblockstate.getMaterial() != Material.AIR) {
                if (iblockstate.getBlock() == Blocks.PISTON_EXTENSION) {
                    final EnumFacing enumfacing = iblockstate.getValue((IProperty<EnumFacing>)BlockPistonBase.FACING);
                    if (this.world.isAirBlock(blockpos.offset(enumfacing))) {
                        blockpos = blockpos.offset(enumfacing);
                        this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, (Optional<BlockPos>)Optional.of((Object)blockpos));
                    }
                    else {
                        this.tryTeleportToNewPosition();
                    }
                }
                else if (iblockstate.getBlock() == Blocks.PISTON_HEAD) {
                    final EnumFacing enumfacing2 = iblockstate.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING);
                    if (this.world.isAirBlock(blockpos.offset(enumfacing2))) {
                        blockpos = blockpos.offset(enumfacing2);
                        this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, (Optional<BlockPos>)Optional.of((Object)blockpos));
                    }
                    else {
                        this.tryTeleportToNewPosition();
                    }
                }
                else {
                    this.tryTeleportToNewPosition();
                }
            }
            BlockPos blockpos2 = blockpos.offset(this.getAttachmentFacing());
            if (!this.world.isBlockNormalCube(blockpos2, false)) {
                boolean flag = false;
                for (final EnumFacing enumfacing3 : EnumFacing.values()) {
                    blockpos2 = blockpos.offset(enumfacing3);
                    if (this.world.isBlockNormalCube(blockpos2, false)) {
                        this.dataManager.set(EntityShulker.ATTACHED_FACE, enumfacing3);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    this.tryTeleportToNewPosition();
                }
            }
            final BlockPos blockpos3 = blockpos.offset(this.getAttachmentFacing().getOpposite());
            if (this.world.isBlockNormalCube(blockpos3, false)) {
                this.tryTeleportToNewPosition();
            }
        }
        final float f2 = this.getPeekTick() * 0.01f;
        this.prevPeekAmount = this.peekAmount;
        if (this.peekAmount > f2) {
            this.peekAmount = MathHelper.clamp(this.peekAmount - 0.05f, f2, 1.0f);
        }
        else if (this.peekAmount < f2) {
            this.peekAmount = MathHelper.clamp(this.peekAmount + 0.05f, 0.0f, f2);
        }
        if (blockpos != null) {
            if (this.world.isRemote) {
                if (this.clientSideTeleportInterpolation > 0 && this.currentAttachmentPosition != null) {
                    --this.clientSideTeleportInterpolation;
                }
                else {
                    this.currentAttachmentPosition = blockpos;
                }
            }
            this.posX = blockpos.getX() + 0.5;
            this.posY = blockpos.getY();
            this.posZ = blockpos.getZ() + 0.5;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
            final double d3 = 0.5 - MathHelper.sin((0.5f + this.peekAmount) * 3.1415927f) * 0.5;
            final double d4 = 0.5 - MathHelper.sin((0.5f + this.prevPeekAmount) * 3.1415927f) * 0.5;
            final double d5 = d3 - d4;
            double d6 = 0.0;
            double d7 = 0.0;
            double d8 = 0.0;
            final EnumFacing enumfacing4 = this.getAttachmentFacing();
            switch (enumfacing4) {
                case DOWN: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0 + d3, this.posZ + 0.5));
                    d7 = d5;
                    break;
                }
                case UP: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5, this.posY - d3, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5));
                    d7 = -d5;
                    break;
                }
                case NORTH: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5 + d3));
                    d8 = d5;
                    break;
                }
                case SOUTH: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5 - d3, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5));
                    d8 = -d5;
                    break;
                }
                case WEST: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5, this.posX + 0.5 + d3, this.posY + 1.0, this.posZ + 0.5));
                    d6 = d5;
                    break;
                }
                case EAST: {
                    this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5 - d3, this.posY, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5));
                    d6 = -d5;
                    break;
                }
            }
            if (d5 > 0.0) {
                final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
                if (!list.isEmpty()) {
                    for (final Entity entity : list) {
                        if (!(entity instanceof EntityShulker) && !entity.noClip) {
                            entity.move(MoverType.SHULKER, d6, d7, d8);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void move(final MoverType type, final double x, final double y, final double z) {
        if (type == MoverType.SHULKER_BOX) {
            this.tryTeleportToNewPosition();
        }
        else {
            super.move(type, x, y, z);
        }
    }
    
    @Override
    public void setPosition(final double x, final double y, final double z) {
        super.setPosition(x, y, z);
        if (this.dataManager != null && this.ticksExisted != 0) {
            final Optional<BlockPos> optional = this.dataManager.get(EntityShulker.ATTACHED_BLOCK_POS);
            final Optional<BlockPos> optional2 = (Optional<BlockPos>)Optional.of((Object)new BlockPos(x, y, z));
            if (!optional2.equals((Object)optional)) {
                this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, optional2);
                this.dataManager.set(EntityShulker.PEEK_TICK, (Byte)0);
                this.isAirBorne = true;
            }
        }
    }
    
    protected boolean tryTeleportToNewPosition() {
        if (!this.isAIDisabled() && this.isEntityAlive()) {
            final BlockPos blockpos = new BlockPos(this);
            for (int i = 0; i < 5; ++i) {
                final BlockPos blockpos2 = blockpos.add(8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17));
                if (blockpos2.getY() > 0 && this.world.isAirBlock(blockpos2) && this.world.isInsideWorldBorder(this) && this.world.getCollisionBoxes(this, new AxisAlignedBB(blockpos2)).isEmpty()) {
                    boolean flag = false;
                    for (final EnumFacing enumfacing : EnumFacing.values()) {
                        if (this.world.isBlockNormalCube(blockpos2.offset(enumfacing), false)) {
                            this.dataManager.set(EntityShulker.ATTACHED_FACE, enumfacing);
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        this.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1.0f, 1.0f);
                        this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, (Optional<BlockPos>)Optional.of((Object)blockpos2));
                        this.dataManager.set(EntityShulker.PEEK_TICK, (Byte)0);
                        this.setAttackTarget(null);
                        return true;
                    }
                }
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
    public void notifyDataManagerChange(final DataParameter<?> key) {
        if (EntityShulker.ATTACHED_BLOCK_POS.equals(key) && this.world.isRemote && !this.isRiding()) {
            final BlockPos blockpos = this.getAttachmentPos();
            if (blockpos != null) {
                if (this.currentAttachmentPosition == null) {
                    this.currentAttachmentPosition = blockpos;
                }
                else {
                    this.clientSideTeleportInterpolation = 6;
                }
                this.posX = blockpos.getX() + 0.5;
                this.posY = blockpos.getY();
                this.posZ = blockpos.getZ() + 0.5;
                this.prevPosX = this.posX;
                this.prevPosY = this.posY;
                this.prevPosZ = this.posZ;
                this.lastTickPosX = this.posX;
                this.lastTickPosY = this.posY;
                this.lastTickPosZ = this.posZ;
            }
        }
        super.notifyDataManagerChange(key);
    }
    
    @Override
    public void setPositionAndRotationDirect(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean teleport) {
        this.newPosRotationIncrements = 0;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isClosed()) {
            final Entity entity = source.getImmediateSource();
            if (entity instanceof EntityArrow) {
                return false;
            }
        }
        if (super.attackEntityFrom(source, amount)) {
            if (this.getHealth() < this.getMaxHealth() * 0.5 && this.rand.nextInt(4) == 0) {
                this.tryTeleportToNewPosition();
            }
            return true;
        }
        return false;
    }
    
    private boolean isClosed() {
        return this.getPeekTick() == 0;
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.isEntityAlive() ? this.getEntityBoundingBox() : null;
    }
    
    public EnumFacing getAttachmentFacing() {
        return this.dataManager.get(EntityShulker.ATTACHED_FACE);
    }
    
    @Nullable
    public BlockPos getAttachmentPos() {
        return (BlockPos)this.dataManager.get(EntityShulker.ATTACHED_BLOCK_POS).orNull();
    }
    
    public void setAttachmentPos(@Nullable final BlockPos pos) {
        this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, (Optional<BlockPos>)Optional.fromNullable((Object)pos));
    }
    
    public int getPeekTick() {
        return this.dataManager.get(EntityShulker.PEEK_TICK);
    }
    
    public void updateArmorModifier(final int p_184691_1_) {
        if (!this.world.isRemote) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(EntityShulker.COVERED_ARMOR_BONUS_MODIFIER);
            if (p_184691_1_ == 0) {
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(EntityShulker.COVERED_ARMOR_BONUS_MODIFIER);
                this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0f, 1.0f);
            }
            else {
                this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0f, 1.0f);
            }
        }
        this.dataManager.set(EntityShulker.PEEK_TICK, (byte)p_184691_1_);
    }
    
    public float getClientPeekAmount(final float p_184688_1_) {
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
    public void applyEntityCollision(final Entity entityIn) {
    }
    
    @Override
    public float getCollisionBorderSize() {
        return 0.0f;
    }
    
    public boolean isAttachedToBlock() {
        return this.currentAttachmentPosition != null && this.getAttachmentPos() != null;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SHULKER;
    }
    
    public EnumDyeColor getColor() {
        return EnumDyeColor.byMetadata(this.dataManager.get(EntityShulker.COLOR));
    }
    
    static {
        COVERED_ARMOR_BONUS_ID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
        COVERED_ARMOR_BONUS_MODIFIER = new AttributeModifier(EntityShulker.COVERED_ARMOR_BONUS_ID, "Covered armor bonus", 20.0, 0).setSaved(false);
        ATTACHED_FACE = EntityDataManager.createKey(EntityShulker.class, DataSerializers.FACING);
        ATTACHED_BLOCK_POS = EntityDataManager.createKey(EntityShulker.class, DataSerializers.OPTIONAL_BLOCK_POS);
        PEEK_TICK = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
        COLOR = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
        DEFAULT_COLOR = EnumDyeColor.PURPLE;
    }
    
    class AIAttack extends EntityAIBase
    {
        private int attackTime;
        
        public AIAttack() {
            this.setMutexBits(3);
        }
        
        @Override
        public boolean shouldExecute() {
            final EntityLivingBase entitylivingbase = EntityShulker.this.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() && EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
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
                final EntityLivingBase entitylivingbase = EntityShulker.this.getAttackTarget();
                EntityShulker.this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 180.0f, 180.0f);
                final double d0 = EntityShulker.this.getDistanceSq(entitylivingbase);
                if (d0 < 400.0) {
                    if (this.attackTime <= 0) {
                        this.attackTime = 20 + EntityShulker.this.rand.nextInt(10) * 20 / 2;
                        final EntityShulkerBullet entityshulkerbullet = new EntityShulkerBullet(EntityShulker.this.world, EntityShulker.this, entitylivingbase, EntityShulker.this.getAttachmentFacing().getAxis());
                        EntityShulker.this.world.spawnEntity(entityshulkerbullet);
                        EntityShulker.this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 2.0f, (EntityShulker.this.rand.nextFloat() - EntityShulker.this.rand.nextFloat()) * 0.2f + 1.0f);
                    }
                }
                else {
                    EntityShulker.this.setAttackTarget(null);
                }
                super.updateTask();
            }
        }
    }
    
    class AIAttackNearest extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        public AIAttackNearest(final EntityShulker shulker) {
            super(shulker, EntityPlayer.class, true);
        }
        
        @Override
        public boolean shouldExecute() {
            return EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL && super.shouldExecute();
        }
        
        @Override
        protected AxisAlignedBB getTargetableArea(final double targetDistance) {
            final EnumFacing enumfacing = ((EntityShulker)this.taskOwner).getAttachmentFacing();
            if (enumfacing.getAxis() == EnumFacing.Axis.X) {
                return this.taskOwner.getEntityBoundingBox().grow(4.0, targetDistance, targetDistance);
            }
            return (enumfacing.getAxis() == EnumFacing.Axis.Z) ? this.taskOwner.getEntityBoundingBox().grow(targetDistance, targetDistance, 4.0) : this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0, targetDistance);
        }
    }
    
    static class AIDefenseAttack extends EntityAINearestAttackableTarget<EntityLivingBase>
    {
        public AIDefenseAttack(final EntityShulker shulker) {
            super(shulker, EntityLivingBase.class, 10, true, false, (com.google.common.base.Predicate<? super EntityLivingBase>)new Predicate<EntityLivingBase>() {
                public boolean apply(@Nullable final EntityLivingBase p_apply_1_) {
                    return p_apply_1_ instanceof IMob;
                }
            });
        }
        
        @Override
        public boolean shouldExecute() {
            return this.taskOwner.getTeam() != null && super.shouldExecute();
        }
        
        @Override
        protected AxisAlignedBB getTargetableArea(final double targetDistance) {
            final EnumFacing enumfacing = ((EntityShulker)this.taskOwner).getAttachmentFacing();
            if (enumfacing.getAxis() == EnumFacing.Axis.X) {
                return this.taskOwner.getEntityBoundingBox().grow(4.0, targetDistance, targetDistance);
            }
            return (enumfacing.getAxis() == EnumFacing.Axis.Z) ? this.taskOwner.getEntityBoundingBox().grow(targetDistance, targetDistance, 4.0) : this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0, targetDistance);
        }
    }
    
    class AIPeek extends EntityAIBase
    {
        private int peekTime;
        
        private AIPeek() {
        }
        
        @Override
        public boolean shouldExecute() {
            return EntityShulker.this.getAttackTarget() == null && EntityShulker.this.rand.nextInt(40) == 0;
        }
        
        @Override
        public boolean shouldContinueExecuting() {
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
    
    class BodyHelper extends EntityBodyHelper
    {
        public BodyHelper(final EntityLivingBase theEntity) {
            super(theEntity);
        }
        
        @Override
        public void updateRenderAngles() {
        }
    }
}
