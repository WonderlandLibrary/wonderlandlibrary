// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityBlaze;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import com.google.common.collect.Sets;
import net.minecraft.init.Items;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLeaves;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import java.util.List;
import net.minecraft.entity.EntityList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAILandOnOwnersShoulder;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISit;
import javax.annotation.Nullable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.SoundEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Set;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityLiving;
import com.google.common.base.Predicate;
import net.minecraft.network.datasync.DataParameter;

public class EntityParrot extends EntityShoulderRiding implements EntityFlying
{
    private static final DataParameter<Integer> VARIANT;
    private static final Predicate<EntityLiving> CAN_MIMIC;
    private static final Item DEADLY_ITEM;
    private static final Set<Item> TAME_ITEMS;
    private static final Int2ObjectMap<SoundEvent> IMITATION_SOUND_EVENTS;
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping;
    private boolean partyParrot;
    private BlockPos jukeboxPosition;
    
    public EntityParrot(final World worldIn) {
        super(worldIn);
        this.flapping = 1.0f;
        this.setSize(0.5f, 0.9f);
        this.moveHelper = new EntityFlyHelper(this);
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
        this.setVariant(this.rand.nextInt(5));
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
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4000000059604645);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224);
    }
    
    @Override
    protected PathNavigate createNavigator(final World worldIn) {
        final PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
        pathnavigateflying.setCanOpenDoors(false);
        pathnavigateflying.setCanFloat(true);
        pathnavigateflying.setCanEnterDoors(true);
        return pathnavigateflying;
    }
    
    @Override
    public float getEyeHeight() {
        return this.height * 0.6f;
    }
    
    @Override
    public void onLivingUpdate() {
        playMimicSound(this.world, this);
        if (this.jukeboxPosition == null || this.jukeboxPosition.distanceSq(this.posX, this.posY, this.posZ) > 12.0 || this.world.getBlockState(this.jukeboxPosition).getBlock() != Blocks.JUKEBOX) {
            this.partyParrot = false;
            this.jukeboxPosition = null;
        }
        super.onLivingUpdate();
        this.calculateFlapping();
    }
    
    @Override
    public void setPartying(final BlockPos pos, final boolean isPartying) {
        this.jukeboxPosition = pos;
        this.partyParrot = isPartying;
    }
    
    public boolean isPartying() {
        return this.partyParrot;
    }
    
    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)((this.onGround ? -1 : 4) * 0.3);
        this.flapSpeed = MathHelper.clamp(this.flapSpeed, 0.0f, 1.0f);
        if (!this.onGround && this.flapping < 1.0f) {
            this.flapping = 1.0f;
        }
        this.flapping *= (float)0.9;
        if (!this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.6;
        }
        this.flap += this.flapping * 2.0f;
    }
    
    private static boolean playMimicSound(final World worldIn, final Entity parrotIn) {
        if (!parrotIn.isSilent() && worldIn.rand.nextInt(50) == 0) {
            final List<EntityLiving> list = worldIn.getEntitiesWithinAABB((Class<? extends EntityLiving>)EntityLiving.class, parrotIn.getEntityBoundingBox().grow(20.0), (com.google.common.base.Predicate<? super EntityLiving>)EntityParrot.CAN_MIMIC);
            if (!list.isEmpty()) {
                final EntityLiving entityliving = list.get(worldIn.rand.nextInt(list.size()));
                if (!entityliving.isSilent()) {
                    final SoundEvent soundevent = getImitatedSound(EntityList.REGISTRY.getIDForObject(entityliving.getClass()));
                    worldIn.playSound(null, parrotIn.posX, parrotIn.posY, parrotIn.posZ, soundevent, parrotIn.getSoundCategory(), 0.7f, getPitch(worldIn.rand));
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!this.isTamed() && EntityParrot.TAME_ITEMS.contains(itemstack.getItem())) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            if (!this.isSilent()) {
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PARROT_EAT, this.getSoundCategory(), 1.0f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f);
            }
            if (!this.world.isRemote) {
                if (this.rand.nextInt(10) == 0) {
                    this.setTamedBy(player);
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte)7);
                }
                else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
        if (itemstack.getItem() == EntityParrot.DEADLY_ITEM) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            this.addPotionEffect(new PotionEffect(MobEffects.POISON, 900));
            if (player.isCreative() || !this.getIsInvulnerable()) {
                this.attackEntityFrom(DamageSource.causePlayerDamage(player), Float.MAX_VALUE);
            }
            return true;
        }
        if (!this.world.isRemote && !this.isFlying() && this.isTamed() && this.isOwner(player)) {
            this.aiSit.setSitting(!this.isSitting());
        }
        return super.processInteract(player, hand);
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return false;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        final int i = MathHelper.floor(this.posX);
        final int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        final int k = MathHelper.floor(this.posZ);
        final BlockPos blockpos = new BlockPos(i, j, k);
        final Block block = this.world.getBlockState(blockpos.down()).getBlock();
        return block instanceof BlockLeaves || block == Blocks.GRASS || block instanceof BlockLog || (block == Blocks.AIR && this.world.getLight(blockpos) > 8 && super.getCanSpawnHere());
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    protected void updateFallState(final double y, final boolean onGroundIn, final IBlockState state, final BlockPos pos) {
    }
    
    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        return false;
    }
    
    @Nullable
    @Override
    public EntityAgeable createChild(final EntityAgeable ageable) {
        return null;
    }
    
    public static void playAmbientSound(final World worldIn, final Entity parrotIn) {
        if (!parrotIn.isSilent() && !playMimicSound(worldIn, parrotIn) && worldIn.rand.nextInt(200) == 0) {
            worldIn.playSound(null, parrotIn.posX, parrotIn.posY, parrotIn.posZ, getAmbientSound(worldIn.rand), parrotIn.getSoundCategory(), 1.0f, getPitch(worldIn.rand));
        }
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0f);
    }
    
    @Nullable
    public SoundEvent getAmbientSound() {
        return getAmbientSound(this.rand);
    }
    
    private static SoundEvent getAmbientSound(final Random random) {
        if (random.nextInt(1000) == 0) {
            final List<Integer> list = new ArrayList<Integer>((Collection<? extends Integer>)EntityParrot.IMITATION_SOUND_EVENTS.keySet());
            return getImitatedSound(list.get(random.nextInt(list.size())));
        }
        return SoundEvents.ENTITY_PARROT_AMBIENT;
    }
    
    public static SoundEvent getImitatedSound(final int p_191999_0_) {
        return (SoundEvent)(EntityParrot.IMITATION_SOUND_EVENTS.containsKey(p_191999_0_) ? EntityParrot.IMITATION_SOUND_EVENTS.get(p_191999_0_) : SoundEvents.ENTITY_PARROT_AMBIENT);
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_PARROT_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15f, 1.0f);
    }
    
    @Override
    protected float playFlySound(final float p_191954_1_) {
        this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15f, 1.0f);
        return p_191954_1_ + this.flapSpeed / 2.0f;
    }
    
    @Override
    protected boolean makeFlySound() {
        return true;
    }
    
    @Override
    protected float getSoundPitch() {
        return getPitch(this.rand);
    }
    
    private static float getPitch(final Random random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f;
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
    protected void collideWithEntity(final Entity entityIn) {
        if (!(entityIn instanceof EntityPlayer)) {
            super.collideWithEntity(entityIn);
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.aiSit != null) {
            this.aiSit.setSitting(false);
        }
        return super.attackEntityFrom(source, amount);
    }
    
    public int getVariant() {
        return MathHelper.clamp(this.dataManager.get(EntityParrot.VARIANT), 0, 4);
    }
    
    public void setVariant(final int variantIn) {
        this.dataManager.set(EntityParrot.VARIANT, variantIn);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityParrot.VARIANT, 0);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_PARROT;
    }
    
    public boolean isFlying() {
        return !this.onGround;
    }
    
    static {
        VARIANT = EntityDataManager.createKey(EntityParrot.class, DataSerializers.VARINT);
        CAN_MIMIC = (Predicate)new Predicate<EntityLiving>() {
            public boolean apply(@Nullable final EntityLiving p_apply_1_) {
                return p_apply_1_ != null && EntityParrot.IMITATION_SOUND_EVENTS.containsKey(EntityList.REGISTRY.getIDForObject(p_apply_1_.getClass()));
            }
        };
        DEADLY_ITEM = Items.COOKIE;
        TAME_ITEMS = Sets.newHashSet((Object[])new Item[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS });
        (IMITATION_SOUND_EVENTS = (Int2ObjectMap)new Int2ObjectOpenHashMap(32)).put(EntityList.REGISTRY.getIDForObject(EntityBlaze.class), (Object)SoundEvents.E_PARROT_IM_BLAZE);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityCaveSpider.class), (Object)SoundEvents.E_PARROT_IM_SPIDER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityCreeper.class), (Object)SoundEvents.E_PARROT_IM_CREEPER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityElderGuardian.class), (Object)SoundEvents.E_PARROT_IM_ELDER_GUARDIAN);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityDragon.class), (Object)SoundEvents.E_PARROT_IM_ENDERDRAGON);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityEnderman.class), (Object)SoundEvents.E_PARROT_IM_ENDERMAN);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityEndermite.class), (Object)SoundEvents.E_PARROT_IM_ENDERMITE);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityEvoker.class), (Object)SoundEvents.E_PARROT_IM_EVOCATION_ILLAGER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityGhast.class), (Object)SoundEvents.E_PARROT_IM_GHAST);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityHusk.class), (Object)SoundEvents.E_PARROT_IM_HUSK);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityIllusionIllager.class), (Object)SoundEvents.E_PARROT_IM_ILLUSION_ILLAGER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityMagmaCube.class), (Object)SoundEvents.E_PARROT_IM_MAGMACUBE);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityPigZombie.class), (Object)SoundEvents.E_PARROT_IM_ZOMBIE_PIGMAN);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityPolarBear.class), (Object)SoundEvents.E_PARROT_IM_POLAR_BEAR);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityShulker.class), (Object)SoundEvents.E_PARROT_IM_SHULKER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySilverfish.class), (Object)SoundEvents.E_PARROT_IM_SILVERFISH);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySkeleton.class), (Object)SoundEvents.E_PARROT_IM_SKELETON);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySlime.class), (Object)SoundEvents.E_PARROT_IM_SLIME);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySpider.class), (Object)SoundEvents.E_PARROT_IM_SPIDER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityStray.class), (Object)SoundEvents.E_PARROT_IM_STRAY);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityVex.class), (Object)SoundEvents.E_PARROT_IM_VEX);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityVindicator.class), (Object)SoundEvents.E_PARROT_IM_VINDICATION_ILLAGER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWitch.class), (Object)SoundEvents.E_PARROT_IM_WITCH);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWither.class), (Object)SoundEvents.E_PARROT_IM_WITHER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWitherSkeleton.class), (Object)SoundEvents.E_PARROT_IM_WITHER_SKELETON);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWolf.class), (Object)SoundEvents.E_PARROT_IM_WOLF);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityZombie.class), (Object)SoundEvents.E_PARROT_IM_ZOMBIE);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityZombieVillager.class), (Object)SoundEvents.E_PARROT_IM_ZOMBIE_VILLAGER);
    }
}
