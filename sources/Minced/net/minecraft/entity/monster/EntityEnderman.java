// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.util.math.RayTraceResult;
import java.util.Random;
import net.minecraft.util.math.MathHelper;
import com.google.common.base.Function;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import com.google.common.collect.Sets;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Optional;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.block.Block;
import java.util.Set;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public class EntityEnderman extends EntityMob
{
    private static final UUID ATTACKING_SPEED_BOOST_ID;
    private static final AttributeModifier ATTACKING_SPEED_BOOST;
    private static final Set<Block> CARRIABLE_BLOCKS;
    private static final DataParameter<Optional<IBlockState>> CARRIED_BLOCK;
    private static final DataParameter<Boolean> SCREAMING;
    private int lastCreepySound;
    private int targetChangeTime;
    
    public EntityEnderman(final World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 2.9f);
        this.stepHeight = 1.0f;
        this.setPathPriority(PathNodeType.WATER, -1.0f);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0, false));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0, 0.0f));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(10, new AIPlaceBlock(this));
        this.tasks.addTask(11, new AITakeBlock(this));
        this.targetTasks.addTask(1, new AIFindPlayer(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, (Class<?>[])new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityEndermite.class, 10, true, false, (com.google.common.base.Predicate<?>)new Predicate<EntityEndermite>() {
            public boolean apply(@Nullable final EntityEndermite p_apply_1_) {
                return p_apply_1_.isSpawnedByPlayer();
            }
        }));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0);
    }
    
    @Override
    public void setAttackTarget(@Nullable final EntityLivingBase entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (entitylivingbaseIn == null) {
            this.targetChangeTime = 0;
            this.dataManager.set(EntityEnderman.SCREAMING, false);
            iattributeinstance.removeModifier(EntityEnderman.ATTACKING_SPEED_BOOST);
        }
        else {
            this.targetChangeTime = this.ticksExisted;
            this.dataManager.set(EntityEnderman.SCREAMING, true);
            if (!iattributeinstance.hasModifier(EntityEnderman.ATTACKING_SPEED_BOOST)) {
                iattributeinstance.applyModifier(EntityEnderman.ATTACKING_SPEED_BOOST);
            }
        }
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityEnderman.CARRIED_BLOCK, (Optional<IBlockState>)Optional.absent());
        this.dataManager.register(EntityEnderman.SCREAMING, false);
    }
    
    public void playEndermanSound() {
        if (this.ticksExisted >= this.lastCreepySound + 400) {
            this.lastCreepySound = this.ticksExisted;
            if (!this.isSilent()) {
                this.world.playSound(this.posX, this.posY + this.getEyeHeight(), this.posZ, SoundEvents.ENTITY_ENDERMEN_STARE, this.getSoundCategory(), 2.5f, 1.0f, false);
            }
        }
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        if (EntityEnderman.SCREAMING.equals(key) && this.isScreaming() && this.world.isRemote) {
            this.playEndermanSound();
        }
        super.notifyDataManagerChange(key);
    }
    
    public static void registerFixesEnderman(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityEnderman.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        final IBlockState iblockstate = this.getHeldBlockState();
        if (iblockstate != null) {
            compound.setShort("carried", (short)Block.getIdFromBlock(iblockstate.getBlock()));
            compound.setShort("carriedData", (short)iblockstate.getBlock().getMetaFromState(iblockstate));
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        IBlockState iblockstate;
        if (compound.hasKey("carried", 8)) {
            iblockstate = Block.getBlockFromName(compound.getString("carried")).getStateFromMeta(compound.getShort("carriedData") & 0xFFFF);
        }
        else {
            iblockstate = Block.getBlockById(compound.getShort("carried")).getStateFromMeta(compound.getShort("carriedData") & 0xFFFF);
        }
        if (iblockstate == null || iblockstate.getBlock() == null || iblockstate.getMaterial() == Material.AIR) {
            iblockstate = null;
        }
        this.setHeldBlockState(iblockstate);
    }
    
    private boolean shouldAttackPlayer(final EntityPlayer player) {
        final ItemStack itemstack = player.inventory.armorInventory.get(3);
        if (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
            return false;
        }
        final Vec3d vec3d = player.getLook(1.0f).normalize();
        Vec3d vec3d2 = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY + this.getEyeHeight() - (player.posY + player.getEyeHeight()), this.posZ - player.posZ);
        final double d0 = vec3d2.length();
        vec3d2 = vec3d2.normalize();
        final double d2 = vec3d.dotProduct(vec3d2);
        return d2 > 1.0 - 0.025 / d0 && player.canEntityBeSeen(this);
    }
    
    @Override
    public float getEyeHeight() {
        return 2.55f;
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25, this.posZ + (this.rand.nextDouble() - 0.5) * this.width, (this.rand.nextDouble() - 0.5) * 2.0, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5) * 2.0, new int[0]);
            }
        }
        this.isJumping = false;
        super.onLivingUpdate();
    }
    
    @Override
    protected void updateAITasks() {
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0f);
        }
        if (this.world.isDaytime() && this.ticksExisted >= this.targetChangeTime + 600) {
            final float f = this.getBrightness();
            if (f > 0.5f && this.world.canSeeSky(new BlockPos(this)) && this.rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f) {
                this.setAttackTarget(null);
                this.teleportRandomly();
            }
        }
        super.updateAITasks();
    }
    
    protected boolean teleportRandomly() {
        final double d0 = this.posX + (this.rand.nextDouble() - 0.5) * 64.0;
        final double d2 = this.posY + (this.rand.nextInt(64) - 32);
        final double d3 = this.posZ + (this.rand.nextDouble() - 0.5) * 64.0;
        return this.teleportTo(d0, d2, d3);
    }
    
    protected boolean teleportToEntity(final Entity p_70816_1_) {
        Vec3d vec3d = new Vec3d(this.posX - p_70816_1_.posX, this.getEntityBoundingBox().minY + this.height / 2.0f - p_70816_1_.posY + p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
        vec3d = vec3d.normalize();
        final double d0 = 16.0;
        final double d2 = this.posX + (this.rand.nextDouble() - 0.5) * 8.0 - vec3d.x * 16.0;
        final double d3 = this.posY + (this.rand.nextInt(16) - 8) - vec3d.y * 16.0;
        final double d4 = this.posZ + (this.rand.nextDouble() - 0.5) * 8.0 - vec3d.z * 16.0;
        return this.teleportTo(d2, d3, d4);
    }
    
    private boolean teleportTo(final double x, final double y, final double z) {
        final boolean flag = this.attemptTeleport(x, y, z);
        if (flag) {
            this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0f, 1.0f);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
        }
        return flag;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ENDERMEN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMEN_DEATH;
    }
    
    @Override
    protected void dropEquipment(final boolean wasRecentlyHit, final int lootingModifier) {
        super.dropEquipment(wasRecentlyHit, lootingModifier);
        final IBlockState iblockstate = this.getHeldBlockState();
        if (iblockstate != null) {
            final Item item = Item.getItemFromBlock(iblockstate.getBlock());
            final int i = item.getHasSubtypes() ? iblockstate.getBlock().getMetaFromState(iblockstate) : 0;
            this.entityDropItem(new ItemStack(item, 1, i), 0.0f);
        }
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ENDERMAN;
    }
    
    public void setHeldBlockState(@Nullable final IBlockState state) {
        this.dataManager.set(EntityEnderman.CARRIED_BLOCK, (Optional<IBlockState>)Optional.fromNullable((Object)state));
    }
    
    @Nullable
    public IBlockState getHeldBlockState() {
        return (IBlockState)this.dataManager.get(EntityEnderman.CARRIED_BLOCK).orNull();
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (source instanceof EntityDamageSourceIndirect) {
            for (int i = 0; i < 64; ++i) {
                if (this.teleportRandomly()) {
                    return true;
                }
            }
            return false;
        }
        final boolean flag = super.attackEntityFrom(source, amount);
        if (source.isUnblockable() && this.rand.nextInt(10) != 0) {
            this.teleportRandomly();
        }
        return flag;
    }
    
    public boolean isScreaming() {
        return this.dataManager.get(EntityEnderman.SCREAMING);
    }
    
    static {
        ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
        ATTACKING_SPEED_BOOST = new AttributeModifier(EntityEnderman.ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.15000000596046448, 0).setSaved(false);
        CARRIABLE_BLOCKS = Sets.newIdentityHashSet();
        CARRIED_BLOCK = EntityDataManager.createKey(EntityEnderman.class, DataSerializers.OPTIONAL_BLOCK_STATE);
        SCREAMING = EntityDataManager.createKey(EntityEnderman.class, DataSerializers.BOOLEAN);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.GRASS);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.DIRT);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.SAND);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.GRAVEL);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.YELLOW_FLOWER);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.RED_FLOWER);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.BROWN_MUSHROOM);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.RED_MUSHROOM);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.TNT);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.CACTUS);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.CLAY);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.PUMPKIN);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.MELON_BLOCK);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.MYCELIUM);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.NETHERRACK);
    }
    
    static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        private final EntityEnderman enderman;
        private EntityPlayer player;
        private int aggroTime;
        private int teleportTime;
        
        public AIFindPlayer(final EntityEnderman p_i45842_1_) {
            super(p_i45842_1_, EntityPlayer.class, false);
            this.enderman = p_i45842_1_;
        }
        
        @Override
        public boolean shouldExecute() {
            final double d0 = this.getTargetDistance();
            this.player = this.enderman.world.getNearestAttackablePlayer(this.enderman.posX, this.enderman.posY, this.enderman.posZ, d0, d0, null, (Predicate<EntityPlayer>)new Predicate<EntityPlayer>() {
                public boolean apply(@Nullable final EntityPlayer p_apply_1_) {
                    return p_apply_1_ != null && AIFindPlayer.this.enderman.shouldAttackPlayer(p_apply_1_);
                }
            });
            return this.player != null;
        }
        
        @Override
        public void startExecuting() {
            this.aggroTime = 5;
            this.teleportTime = 0;
        }
        
        @Override
        public void resetTask() {
            this.player = null;
            super.resetTask();
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            if (this.player == null) {
                return (this.targetEntity != null && ((EntityPlayer)this.targetEntity).isEntityAlive()) || super.shouldContinueExecuting();
            }
            if (!this.enderman.shouldAttackPlayer(this.player)) {
                return false;
            }
            this.enderman.faceEntity(this.player, 10.0f, 10.0f);
            return true;
        }
        
        @Override
        public void updateTask() {
            if (this.player != null) {
                if (--this.aggroTime <= 0) {
                    this.targetEntity = (T)this.player;
                    this.player = null;
                    super.startExecuting();
                }
            }
            else {
                if (this.targetEntity != null) {
                    if (this.enderman.shouldAttackPlayer((EntityPlayer)this.targetEntity)) {
                        if (((EntityPlayer)this.targetEntity).getDistanceSq(this.enderman) < 16.0) {
                            this.enderman.teleportRandomly();
                        }
                        this.teleportTime = 0;
                    }
                    else if (((EntityPlayer)this.targetEntity).getDistanceSq(this.enderman) > 256.0 && this.teleportTime++ >= 30 && this.enderman.teleportToEntity(this.targetEntity)) {
                        this.teleportTime = 0;
                    }
                }
                super.updateTask();
            }
        }
    }
    
    static class AIPlaceBlock extends EntityAIBase
    {
        private final EntityEnderman enderman;
        
        public AIPlaceBlock(final EntityEnderman p_i45843_1_) {
            this.enderman = p_i45843_1_;
        }
        
        @Override
        public boolean shouldExecute() {
            return this.enderman.getHeldBlockState() != null && this.enderman.world.getGameRules().getBoolean("mobGriefing") && this.enderman.getRNG().nextInt(2000) == 0;
        }
        
        @Override
        public void updateTask() {
            final Random random = this.enderman.getRNG();
            final World world = this.enderman.world;
            final int i = MathHelper.floor(this.enderman.posX - 1.0 + random.nextDouble() * 2.0);
            final int j = MathHelper.floor(this.enderman.posY + random.nextDouble() * 2.0);
            final int k = MathHelper.floor(this.enderman.posZ - 1.0 + random.nextDouble() * 2.0);
            final BlockPos blockpos = new BlockPos(i, j, k);
            final IBlockState iblockstate = world.getBlockState(blockpos);
            final IBlockState iblockstate2 = world.getBlockState(blockpos.down());
            final IBlockState iblockstate3 = this.enderman.getHeldBlockState();
            if (iblockstate3 != null && this.canPlaceBlock(world, blockpos, iblockstate3.getBlock(), iblockstate, iblockstate2)) {
                world.setBlockState(blockpos, iblockstate3, 3);
                this.enderman.setHeldBlockState(null);
            }
        }
        
        private boolean canPlaceBlock(final World p_188518_1_, final BlockPos p_188518_2_, final Block p_188518_3_, final IBlockState p_188518_4_, final IBlockState p_188518_5_) {
            return p_188518_3_.canPlaceBlockAt(p_188518_1_, p_188518_2_) && p_188518_4_.getMaterial() == Material.AIR && p_188518_5_.getMaterial() != Material.AIR && p_188518_5_.isFullCube();
        }
    }
    
    static class AITakeBlock extends EntityAIBase
    {
        private final EntityEnderman enderman;
        
        public AITakeBlock(final EntityEnderman p_i45841_1_) {
            this.enderman = p_i45841_1_;
        }
        
        @Override
        public boolean shouldExecute() {
            return this.enderman.getHeldBlockState() == null && this.enderman.world.getGameRules().getBoolean("mobGriefing") && this.enderman.getRNG().nextInt(20) == 0;
        }
        
        @Override
        public void updateTask() {
            final Random random = this.enderman.getRNG();
            final World world = this.enderman.world;
            final int i = MathHelper.floor(this.enderman.posX - 2.0 + random.nextDouble() * 4.0);
            final int j = MathHelper.floor(this.enderman.posY + random.nextDouble() * 3.0);
            final int k = MathHelper.floor(this.enderman.posZ - 2.0 + random.nextDouble() * 4.0);
            final BlockPos blockpos = new BlockPos(i, j, k);
            final IBlockState iblockstate = world.getBlockState(blockpos);
            final Block block = iblockstate.getBlock();
            final RayTraceResult raytraceresult = world.rayTraceBlocks(new Vec3d(MathHelper.floor(this.enderman.posX) + 0.5f, j + 0.5f, MathHelper.floor(this.enderman.posZ) + 0.5f), new Vec3d(i + 0.5f, j + 0.5f, k + 0.5f), false, true, false);
            final boolean flag = raytraceresult != null && raytraceresult.getBlockPos().equals(blockpos);
            if (EntityEnderman.CARRIABLE_BLOCKS.contains(block) && flag) {
                this.enderman.setHeldBlockState(iblockstate);
                world.setBlockToAir(blockpos);
            }
        }
    }
}
