/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 */
package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.ai.EntityAILandOnOwnersShoulder;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityParrot
extends EntityShoulderRiding
implements EntityFlying {
    private static final DataParameter<Integer> field_192013_bG = EntityDataManager.createKey(EntityParrot.class, DataSerializers.VARINT);
    private static final Predicate<EntityLiving> field_192014_bH = new Predicate<EntityLiving>(){

        @Override
        public boolean apply(@Nullable EntityLiving p_apply_1_) {
            return p_apply_1_ != null && field_192017_bK.containsKey(EntityList.REGISTRY.getIDForObject(p_apply_1_.getClass()));
        }
    };
    private static final Item field_192015_bI = Items.COOKIE;
    private static final Set<Item> field_192016_bJ = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    private static final Int2ObjectMap<SoundEvent> field_192017_bK = new Int2ObjectOpenHashMap(32);
    public float field_192008_bB;
    public float field_192009_bC;
    public float field_192010_bD;
    public float field_192011_bE;
    public float field_192012_bF = 1.0f;
    private boolean field_192018_bL;
    private BlockPos field_192019_bM;

    public EntityParrot(World p_i47411_1_) {
        super(p_i47411_1_);
        this.setSize(0.5f, 0.9f);
        this.moveHelper = new EntityFlyHelper(this);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.func_191997_m(this.rand.nextInt(5));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(0, new EntityAIPanic(this, 1.25));
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(2, new EntityAIFollowOwnerFlying(this, 1.0, 5.0f, 1.0f));
        this.tasks.addTask(2, new EntityAIWanderAvoidWaterFlying(this, 1.0));
        this.tasks.addTask(3, new EntityAILandOnOwnersShoulder(this));
        this.tasks.addTask(3, new EntityAIFollow(this, 1.0, 3.0f, 7.0f));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.field_193334_e);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.field_193334_e).setBaseValue(0.4f);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2f);
    }

    @Override
    protected PathNavigate getNewNavigator(World worldIn) {
        PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
        pathnavigateflying.func_192879_a(false);
        pathnavigateflying.func_192877_c(true);
        pathnavigateflying.func_192878_b(true);
        return pathnavigateflying;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.6f;
    }

    @Override
    public void onLivingUpdate() {
        EntityParrot.func_192006_b(this.world, this);
        if (this.field_192019_bM == null || this.field_192019_bM.distanceSq(this.posX, this.posY, this.posZ) > 12.0 || this.world.getBlockState(this.field_192019_bM).getBlock() != Blocks.JUKEBOX) {
            this.field_192018_bL = false;
            this.field_192019_bM = null;
        }
        super.onLivingUpdate();
        this.func_192001_dv();
    }

    @Override
    public void func_191987_a(BlockPos p_191987_1_, boolean p_191987_2_) {
        this.field_192019_bM = p_191987_1_;
        this.field_192018_bL = p_191987_2_;
    }

    public boolean func_192004_dr() {
        return this.field_192018_bL;
    }

    private void func_192001_dv() {
        this.field_192011_bE = this.field_192008_bB;
        this.field_192010_bD = this.field_192009_bC;
        this.field_192009_bC = (float)((double)this.field_192009_bC + (double)(this.onGround ? -1 : 4) * 0.3);
        this.field_192009_bC = MathHelper.clamp(this.field_192009_bC, 0.0f, 1.0f);
        if (!this.onGround && this.field_192012_bF < 1.0f) {
            this.field_192012_bF = 1.0f;
        }
        this.field_192012_bF = (float)((double)this.field_192012_bF * 0.9);
        if (!this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.6;
        }
        this.field_192008_bB += this.field_192012_bF * 2.0f;
    }

    private static boolean func_192006_b(World p_192006_0_, Entity p_192006_1_) {
        if (!p_192006_1_.isSilent() && p_192006_0_.rand.nextInt(50) == 0) {
            EntityLiving entityliving;
            List<EntityLiving> list = p_192006_0_.getEntitiesWithinAABB(EntityLiving.class, p_192006_1_.getEntityBoundingBox().expandXyz(20.0), field_192014_bH);
            if (!list.isEmpty() && !(entityliving = list.get(p_192006_0_.rand.nextInt(list.size()))).isSilent()) {
                SoundEvent soundevent = EntityParrot.func_191999_g(EntityList.REGISTRY.getIDForObject(entityliving.getClass()));
                p_192006_0_.playSound(null, p_192006_1_.posX, p_192006_1_.posY, p_192006_1_.posZ, soundevent, p_192006_1_.getSoundCategory(), 0.7f, EntityParrot.func_192000_b(p_192006_0_.rand));
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (!this.isTamed() && field_192016_bJ.contains(itemstack.getItem())) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            if (!this.isSilent()) {
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.field_192797_eu, this.getSoundCategory(), 1.0f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f);
            }
            if (!this.world.isRemote) {
                if (this.rand.nextInt(10) == 0) {
                    this.func_193101_c(player);
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte)7);
                } else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
        if (itemstack.getItem() == field_192015_bI) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            this.addPotionEffect(new PotionEffect(MobEffects.POISON, 900));
            if (player.isCreative() || !this.func_190530_aW()) {
                this.attackEntityFrom(DamageSource.causePlayerDamage(player), Float.MAX_VALUE);
            }
            return true;
        }
        if (!this.world.isRemote && !this.func_192002_a() && this.isTamed() && this.isOwner(player)) {
            this.aiSit.setSitting(!this.isSitting());
        }
        return super.processInteract(player, hand);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean getCanSpawnHere() {
        int k;
        int j;
        int i = MathHelper.floor(this.posX);
        BlockPos blockpos = new BlockPos(i, j = MathHelper.floor(this.getEntityBoundingBox().minY), k = MathHelper.floor(this.posZ));
        Block block = this.world.getBlockState(blockpos.down()).getBlock();
        return block instanceof BlockLeaves || block == Blocks.GRASS || block instanceof BlockLog || block == Blocks.AIR && this.world.getLight(blockpos) > 8 && super.getCanSpawnHere();
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal) {
        return false;
    }

    @Override
    @Nullable
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    public static void func_192005_a(World p_192005_0_, Entity p_192005_1_) {
        if (!p_192005_1_.isSilent() && !EntityParrot.func_192006_b(p_192005_0_, p_192005_1_) && p_192005_0_.rand.nextInt(200) == 0) {
            p_192005_0_.playSound(null, p_192005_1_.posX, p_192005_1_.posY, p_192005_1_.posZ, EntityParrot.func_192003_a(p_192005_0_.rand), p_192005_1_.getSoundCategory(), 1.0f, EntityParrot.func_192000_b(p_192005_0_.rand));
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0f);
    }

    @Override
    @Nullable
    public SoundEvent getAmbientSound() {
        return EntityParrot.func_192003_a(this.rand);
    }

    private static SoundEvent func_192003_a(Random p_192003_0_) {
        if (p_192003_0_.nextInt(1000) == 0) {
            ArrayList list = new ArrayList(field_192017_bK.keySet());
            return EntityParrot.func_191999_g((Integer)list.get(p_192003_0_.nextInt(list.size())));
        }
        return SoundEvents.field_192792_ep;
    }

    public static SoundEvent func_191999_g(int p_191999_0_) {
        return field_192017_bK.containsKey(p_191999_0_) ? (SoundEvent)field_192017_bK.get(p_191999_0_) : SoundEvents.field_192792_ep;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.field_192794_er;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.field_192793_eq;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.field_192795_es, 0.15f, 1.0f);
    }

    @Override
    protected float func_191954_d(float p_191954_1_) {
        this.playSound(SoundEvents.field_192796_et, 0.15f, 1.0f);
        return p_191954_1_ + this.field_192009_bC / 2.0f;
    }

    @Override
    protected boolean func_191957_ae() {
        return true;
    }

    @Override
    protected float getSoundPitch() {
        return EntityParrot.func_192000_b(this.rand);
    }

    private static float func_192000_b(Random p_192000_0_) {
        return (p_192000_0_.nextFloat() - p_192000_0_.nextFloat()) * 0.2f + 1.0f;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityPlayer)) {
            super.collideWithEntity(entityIn);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.aiSit != null) {
            this.aiSit.setSitting(false);
        }
        return super.attackEntityFrom(source, amount);
    }

    public int func_191998_ds() {
        return MathHelper.clamp(this.dataManager.get(field_192013_bG), 0, 4);
    }

    public void func_191997_m(int p_191997_1_) {
        this.dataManager.set(field_192013_bG, p_191997_1_);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(field_192013_bG, 0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.func_191998_ds());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.func_191997_m(compound.getInteger("Variant"));
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.field_192561_ax;
    }

    public boolean func_192002_a() {
        return !this.onGround;
    }

    static {
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityBlaze.class), (Object)SoundEvents.field_193791_eM);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityCaveSpider.class), (Object)SoundEvents.field_193813_fc);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityCreeper.class), (Object)SoundEvents.field_193792_eN);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityElderGuardian.class), (Object)SoundEvents.field_193793_eO);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityDragon.class), (Object)SoundEvents.field_193794_eP);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityEnderman.class), (Object)SoundEvents.field_193795_eQ);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityEndermite.class), (Object)SoundEvents.field_193796_eR);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityEvoker.class), (Object)SoundEvents.field_193797_eS);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityGhast.class), (Object)SoundEvents.field_193798_eT);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityHusk.class), (Object)SoundEvents.field_193799_eU);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityIllusionIllager.class), (Object)SoundEvents.field_193800_eV);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityMagmaCube.class), (Object)SoundEvents.field_193801_eW);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityPigZombie.class), (Object)SoundEvents.field_193822_fl);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityPolarBear.class), (Object)SoundEvents.field_193802_eX);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityShulker.class), (Object)SoundEvents.field_193803_eY);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntitySilverfish.class), (Object)SoundEvents.field_193804_eZ);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntitySkeleton.class), (Object)SoundEvents.field_193811_fa);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntitySlime.class), (Object)SoundEvents.field_193812_fb);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntitySpider.class), (Object)SoundEvents.field_193813_fc);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityStray.class), (Object)SoundEvents.field_193814_fd);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityVex.class), (Object)SoundEvents.field_193815_fe);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityVindicator.class), (Object)SoundEvents.field_193816_ff);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityWitch.class), (Object)SoundEvents.field_193817_fg);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityWither.class), (Object)SoundEvents.field_193818_fh);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityWitherSkeleton.class), (Object)SoundEvents.field_193819_fi);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityWolf.class), (Object)SoundEvents.field_193820_fj);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityZombie.class), (Object)SoundEvents.field_193821_fk);
        field_192017_bK.put(EntityList.REGISTRY.getIDForObject(EntityZombieVillager.class), (Object)SoundEvents.field_193823_fm);
    }
}

