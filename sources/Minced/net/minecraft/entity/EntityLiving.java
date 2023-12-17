// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.IBlockAccess;
import java.util.function.BiPredicate;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.monster.IMob;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.storage.loot.LootTable;
import java.util.Random;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.WorldServer;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.item.Item;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.src.Config;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.monster.EntityGhast;
import net.optifine.reflect.Reflector;
import javax.annotation.Nullable;
import net.minecraft.pathfinding.PathNavigateGround;
import java.util.Arrays;
import com.google.common.collect.Maps;
import net.minecraft.world.World;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.pathfinding.PathNodeType;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.network.datasync.DataParameter;

public abstract class EntityLiving extends EntityLivingBase
{
    private static final DataParameter<Byte> AI_FLAGS;
    public int livingSoundTime;
    protected int experienceValue;
    private final EntityLookHelper lookHelper;
    protected EntityMoveHelper moveHelper;
    protected EntityJumpHelper jumpHelper;
    private final EntityBodyHelper bodyHelper;
    protected PathNavigate navigator;
    protected final EntityAITasks tasks;
    protected final EntityAITasks targetTasks;
    private EntityLivingBase attackTarget;
    private final EntitySenses senses;
    private final NonNullList<ItemStack> inventoryHands;
    protected float[] inventoryHandsDropChances;
    private final NonNullList<ItemStack> inventoryArmor;
    protected float[] inventoryArmorDropChances;
    private boolean canPickUpLoot;
    private boolean persistenceRequired;
    private final Map<PathNodeType, Float> mapPathPriority;
    private ResourceLocation deathLootTable;
    private long deathLootTableSeed;
    private boolean isLeashed;
    private Entity leashHolder;
    private NBTTagCompound leashNBTTag;
    private UUID teamUuid;
    private String teamUuidString;
    
    public EntityLiving(final World worldIn) {
        super(worldIn);
        this.inventoryHands = NonNullList.withSize(2, ItemStack.EMPTY);
        this.inventoryHandsDropChances = new float[2];
        this.inventoryArmor = NonNullList.withSize(4, ItemStack.EMPTY);
        this.inventoryArmorDropChances = new float[4];
        this.mapPathPriority = (Map<PathNodeType, Float>)Maps.newEnumMap((Class)PathNodeType.class);
        this.teamUuid = null;
        this.teamUuidString = null;
        this.tasks = new EntityAITasks((worldIn != null && worldIn.profiler != null) ? worldIn.profiler : null);
        this.targetTasks = new EntityAITasks((worldIn != null && worldIn.profiler != null) ? worldIn.profiler : null);
        this.lookHelper = new EntityLookHelper(this);
        this.moveHelper = new EntityMoveHelper(this);
        this.jumpHelper = new EntityJumpHelper(this);
        this.bodyHelper = this.createBodyHelper();
        this.navigator = this.createNavigator(worldIn);
        this.senses = new EntitySenses(this);
        Arrays.fill(this.inventoryArmorDropChances, 0.085f);
        Arrays.fill(this.inventoryHandsDropChances, 0.085f);
        if (worldIn != null && !worldIn.isRemote) {
            this.initEntityAI();
        }
    }
    
    protected void initEntityAI() {
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0);
    }
    
    protected PathNavigate createNavigator(final World worldIn) {
        return new PathNavigateGround(this, worldIn);
    }
    
    public float getPathPriority(final PathNodeType nodeType) {
        final Float f = this.mapPathPriority.get(nodeType);
        return (f == null) ? nodeType.getPriority() : f;
    }
    
    public void setPathPriority(final PathNodeType nodeType, final float priority) {
        this.mapPathPriority.put(nodeType, priority);
    }
    
    protected EntityBodyHelper createBodyHelper() {
        return new EntityBodyHelper(this);
    }
    
    public EntityLookHelper getLookHelper() {
        return this.lookHelper;
    }
    
    public EntityMoveHelper getMoveHelper() {
        return this.moveHelper;
    }
    
    public EntityJumpHelper getJumpHelper() {
        return this.jumpHelper;
    }
    
    public PathNavigate getNavigator() {
        return this.navigator;
    }
    
    public EntitySenses getEntitySenses() {
        return this.senses;
    }
    
    @Nullable
    public EntityLivingBase getAttackTarget() {
        return this.attackTarget;
    }
    
    public void setAttackTarget(@Nullable final EntityLivingBase entitylivingbaseIn) {
        this.attackTarget = entitylivingbaseIn;
        Reflector.callVoid(Reflector.ForgeHooks_onLivingSetAttackTarget, this, entitylivingbaseIn);
    }
    
    public boolean canAttackClass(final Class<? extends EntityLivingBase> cls) {
        return cls != EntityGhast.class;
    }
    
    public void eatGrassBonus() {
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityLiving.AI_FLAGS, (Byte)0);
    }
    
    public int getTalkInterval() {
        return 80;
    }
    
    public void playLivingSound() {
        final SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }
    }
    
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        this.world.profiler.startSection("mobBaseTick");
        if (this.isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
            this.applyEntityAI();
            this.playLivingSound();
        }
        this.world.profiler.endSection();
    }
    
    @Override
    protected void playHurtSound(final DamageSource source) {
        this.applyEntityAI();
        super.playHurtSound(source);
    }
    
    private void applyEntityAI() {
        this.livingSoundTime = -this.getTalkInterval();
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        if (this.experienceValue > 0) {
            int i = this.experienceValue;
            for (int j = 0; j < this.inventoryArmor.size(); ++j) {
                if (!this.inventoryArmor.get(j).isEmpty() && this.inventoryArmorDropChances[j] <= 1.0f) {
                    i += 1 + this.rand.nextInt(3);
                }
            }
            for (int k = 0; k < this.inventoryHands.size(); ++k) {
                if (!this.inventoryHands.get(k).isEmpty() && this.inventoryHandsDropChances[k] <= 1.0f) {
                    i += 1 + this.rand.nextInt(3);
                }
            }
            return i;
        }
        return this.experienceValue;
    }
    
    public void spawnExplosionParticle() {
        if (this.world.isRemote) {
            for (int i = 0; i < 20; ++i) {
                final double d0 = this.rand.nextGaussian() * 0.02;
                final double d2 = this.rand.nextGaussian() * 0.02;
                final double d3 = this.rand.nextGaussian() * 0.02;
                final double d4 = 10.0;
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width - d0 * 10.0, this.posY + this.rand.nextFloat() * this.height - d2 * 10.0, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width - d3 * 10.0, d0, d2, d3, new int[0]);
            }
        }
        else {
            this.world.setEntityState(this, (byte)20);
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 20) {
            this.spawnExplosionParticle();
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    @Override
    public void onUpdate() {
        if (Config.isSmoothWorld() && this.canSkipUpdate()) {
            this.onUpdateMinimal();
        }
        else {
            super.onUpdate();
            if (!this.world.isRemote) {
                this.updateLeashedState();
                if (this.ticksExisted % 5 == 0) {
                    final boolean flag = !(this.getControllingPassenger() instanceof EntityLiving);
                    final boolean flag2 = !(this.getRidingEntity() instanceof EntityBoat);
                    this.tasks.setControlFlag(1, flag);
                    this.tasks.setControlFlag(4, flag && flag2);
                    this.tasks.setControlFlag(2, flag);
                }
            }
        }
    }
    
    @Override
    protected float updateDistance(final float p_110146_1_, final float p_110146_2_) {
        this.bodyHelper.updateRenderAngles();
        return p_110146_2_;
    }
    
    @Nullable
    protected SoundEvent getAmbientSound() {
        return null;
    }
    
    @Nullable
    protected Item getDropItem() {
        return null;
    }
    
    @Override
    protected void dropFewItems(final boolean wasRecentlyHit, final int lootingModifier) {
        final Item item = this.getDropItem();
        if (item != null) {
            int i = this.rand.nextInt(3);
            if (lootingModifier > 0) {
                i += this.rand.nextInt(lootingModifier + 1);
            }
            for (int j = 0; j < i; ++j) {
                this.dropItem(item, 1);
            }
        }
    }
    
    public static void registerFixesMob(final DataFixer fixer, final Class<?> name) {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(name, new String[] { "ArmorItems", "HandItems" }));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("CanPickUpLoot", this.canPickUpLoot());
        compound.setBoolean("PersistenceRequired", this.persistenceRequired);
        final NBTTagList nbttaglist = new NBTTagList();
        for (final ItemStack itemstack : this.inventoryArmor) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            if (!itemstack.isEmpty()) {
                itemstack.writeToNBT(nbttagcompound);
            }
            nbttaglist.appendTag(nbttagcompound);
        }
        compound.setTag("ArmorItems", nbttaglist);
        final NBTTagList nbttaglist2 = new NBTTagList();
        for (final ItemStack itemstack2 : this.inventoryHands) {
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            if (!itemstack2.isEmpty()) {
                itemstack2.writeToNBT(nbttagcompound2);
            }
            nbttaglist2.appendTag(nbttagcompound2);
        }
        compound.setTag("HandItems", nbttaglist2);
        final NBTTagList nbttaglist3 = new NBTTagList();
        for (final float f : this.inventoryArmorDropChances) {
            nbttaglist3.appendTag(new NBTTagFloat(f));
        }
        compound.setTag("ArmorDropChances", nbttaglist3);
        final NBTTagList nbttaglist4 = new NBTTagList();
        for (final float f2 : this.inventoryHandsDropChances) {
            nbttaglist4.appendTag(new NBTTagFloat(f2));
        }
        compound.setTag("HandDropChances", nbttaglist4);
        compound.setBoolean("Leashed", this.isLeashed);
        if (this.leashHolder != null) {
            final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            if (this.leashHolder instanceof EntityLivingBase) {
                final UUID uuid = this.leashHolder.getUniqueID();
                nbttagcompound3.setUniqueId("UUID", uuid);
            }
            else if (this.leashHolder instanceof EntityHanging) {
                final BlockPos blockpos = ((EntityHanging)this.leashHolder).getHangingPosition();
                nbttagcompound3.setInteger("X", blockpos.getX());
                nbttagcompound3.setInteger("Y", blockpos.getY());
                nbttagcompound3.setInteger("Z", blockpos.getZ());
            }
            compound.setTag("Leash", nbttagcompound3);
        }
        compound.setBoolean("LeftHanded", this.isLeftHanded());
        if (this.deathLootTable != null) {
            compound.setString("DeathLootTable", this.deathLootTable.toString());
            if (this.deathLootTableSeed != 0L) {
                compound.setLong("DeathLootTableSeed", this.deathLootTableSeed);
            }
        }
        if (this.isAIDisabled()) {
            compound.setBoolean("NoAI", this.isAIDisabled());
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("CanPickUpLoot", 1)) {
            this.setCanPickUpLoot(compound.getBoolean("CanPickUpLoot"));
        }
        this.persistenceRequired = compound.getBoolean("PersistenceRequired");
        if (compound.hasKey("ArmorItems", 9)) {
            final NBTTagList nbttaglist = compound.getTagList("ArmorItems", 10);
            for (int i = 0; i < this.inventoryArmor.size(); ++i) {
                this.inventoryArmor.set(i, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
        }
        if (compound.hasKey("HandItems", 9)) {
            final NBTTagList nbttaglist2 = compound.getTagList("HandItems", 10);
            for (int j = 0; j < this.inventoryHands.size(); ++j) {
                this.inventoryHands.set(j, new ItemStack(nbttaglist2.getCompoundTagAt(j)));
            }
        }
        if (compound.hasKey("ArmorDropChances", 9)) {
            final NBTTagList nbttaglist3 = compound.getTagList("ArmorDropChances", 5);
            for (int k = 0; k < nbttaglist3.tagCount(); ++k) {
                this.inventoryArmorDropChances[k] = nbttaglist3.getFloatAt(k);
            }
        }
        if (compound.hasKey("HandDropChances", 9)) {
            final NBTTagList nbttaglist4 = compound.getTagList("HandDropChances", 5);
            for (int l = 0; l < nbttaglist4.tagCount(); ++l) {
                this.inventoryHandsDropChances[l] = nbttaglist4.getFloatAt(l);
            }
        }
        this.isLeashed = compound.getBoolean("Leashed");
        if (this.isLeashed && compound.hasKey("Leash", 10)) {
            this.leashNBTTag = compound.getCompoundTag("Leash");
        }
        this.setLeftHanded(compound.getBoolean("LeftHanded"));
        if (compound.hasKey("DeathLootTable", 8)) {
            this.deathLootTable = new ResourceLocation(compound.getString("DeathLootTable"));
            this.deathLootTableSeed = compound.getLong("DeathLootTableSeed");
        }
        this.setNoAI(compound.getBoolean("NoAI"));
    }
    
    @Nullable
    protected ResourceLocation getLootTable() {
        return null;
    }
    
    @Override
    protected void dropLoot(final boolean wasRecentlyHit, final int lootingModifier, final DamageSource source) {
        ResourceLocation resourcelocation = this.deathLootTable;
        if (resourcelocation == null) {
            resourcelocation = this.getLootTable();
        }
        if (resourcelocation != null) {
            final LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(resourcelocation);
            this.deathLootTable = null;
            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world).withLootedEntity(this).withDamageSource(source);
            if (wasRecentlyHit && this.attackingPlayer != null) {
                lootcontext$builder = lootcontext$builder.withPlayer(this.attackingPlayer).withLuck(this.attackingPlayer.getLuck());
            }
            for (final ItemStack itemstack : loottable.generateLootForPools((this.deathLootTableSeed == 0L) ? this.rand : new Random(this.deathLootTableSeed), lootcontext$builder.build())) {
                this.entityDropItem(itemstack, 0.0f);
            }
            this.dropEquipment(wasRecentlyHit, lootingModifier);
        }
        else {
            super.dropLoot(wasRecentlyHit, lootingModifier, source);
        }
    }
    
    public void setMoveForward(final float amount) {
        this.moveForward = amount;
    }
    
    public void setMoveVertical(final float amount) {
        this.moveVertical = amount;
    }
    
    public void setMoveStrafing(final float amount) {
        this.moveStrafing = amount;
    }
    
    @Override
    public void setAIMoveSpeed(final float speedIn) {
        super.setAIMoveSpeed(speedIn);
        this.setMoveForward(speedIn);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.world.profiler.startSection("looting");
        boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
        if (Reflector.ForgeEventFactory_getMobGriefingEvent.exists()) {
            flag = Reflector.callBoolean(Reflector.ForgeEventFactory_getMobGriefingEvent, this.world, this);
        }
        if (!this.world.isRemote && this.canPickUpLoot() && !this.dead && flag) {
            for (final EntityItem entityitem : this.world.getEntitiesWithinAABB((Class<? extends EntityItem>)EntityItem.class, this.getEntityBoundingBox().grow(1.0, 0.0, 1.0))) {
                if (!entityitem.isDead && !entityitem.getItem().isEmpty() && !entityitem.cannotPickup()) {
                    this.updateEquipmentIfNeeded(entityitem);
                }
            }
        }
        this.world.profiler.endSection();
    }
    
    protected void updateEquipmentIfNeeded(final EntityItem itemEntity) {
        final ItemStack itemstack = itemEntity.getItem();
        final EntityEquipmentSlot entityequipmentslot = getSlotForItemStack(itemstack);
        boolean flag = true;
        final ItemStack itemstack2 = this.getItemStackFromSlot(entityequipmentslot);
        if (!itemstack2.isEmpty()) {
            if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.HAND) {
                if (itemstack.getItem() instanceof ItemSword && !(itemstack2.getItem() instanceof ItemSword)) {
                    flag = true;
                }
                else if (itemstack.getItem() instanceof ItemSword && itemstack2.getItem() instanceof ItemSword) {
                    final ItemSword itemsword = (ItemSword)itemstack.getItem();
                    final ItemSword itemsword2 = (ItemSword)itemstack2.getItem();
                    if (itemsword.getAttackDamage() == itemsword2.getAttackDamage()) {
                        flag = (itemstack.getMetadata() > itemstack2.getMetadata() || (itemstack.hasTagCompound() && !itemstack2.hasTagCompound()));
                    }
                    else {
                        flag = (itemsword.getAttackDamage() > itemsword2.getAttackDamage());
                    }
                }
                else {
                    flag = (itemstack.getItem() instanceof ItemBow && itemstack2.getItem() instanceof ItemBow && itemstack.hasTagCompound() && !itemstack2.hasTagCompound());
                }
            }
            else if (itemstack.getItem() instanceof ItemArmor && !(itemstack2.getItem() instanceof ItemArmor)) {
                flag = true;
            }
            else if (itemstack.getItem() instanceof ItemArmor && itemstack2.getItem() instanceof ItemArmor && !EnchantmentHelper.hasBindingCurse(itemstack2)) {
                final ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                final ItemArmor itemarmor2 = (ItemArmor)itemstack2.getItem();
                if (itemarmor.damageReduceAmount == itemarmor2.damageReduceAmount) {
                    flag = (itemstack.getMetadata() > itemstack2.getMetadata() || (itemstack.hasTagCompound() && !itemstack2.hasTagCompound()));
                }
                else {
                    flag = (itemarmor.damageReduceAmount > itemarmor2.damageReduceAmount);
                }
            }
            else {
                flag = false;
            }
        }
        if (flag && this.canEquipItem(itemstack)) {
            double d0 = 0.0;
            switch (entityequipmentslot.getSlotType()) {
                case HAND: {
                    d0 = this.inventoryHandsDropChances[entityequipmentslot.getIndex()];
                    break;
                }
                case ARMOR: {
                    d0 = this.inventoryArmorDropChances[entityequipmentslot.getIndex()];
                    break;
                }
                default: {
                    d0 = 0.0;
                    break;
                }
            }
            if (!itemstack2.isEmpty() && this.rand.nextFloat() - 0.1f < d0) {
                this.entityDropItem(itemstack2, 0.0f);
            }
            this.setItemStackToSlot(entityequipmentslot, itemstack);
            switch (entityequipmentslot.getSlotType()) {
                case HAND: {
                    this.inventoryHandsDropChances[entityequipmentslot.getIndex()] = 2.0f;
                    break;
                }
                case ARMOR: {
                    this.inventoryArmorDropChances[entityequipmentslot.getIndex()] = 2.0f;
                    break;
                }
            }
            this.persistenceRequired = true;
            this.onItemPickup(itemEntity, itemstack.getCount());
            itemEntity.setDead();
        }
    }
    
    protected boolean canEquipItem(final ItemStack stack) {
        return true;
    }
    
    protected boolean canDespawn() {
        return true;
    }
    
    protected void despawnEntity() {
        Object object = null;
        final Object object2 = Reflector.getFieldValue(Reflector.Event_Result_DEFAULT);
        final Object object3 = Reflector.getFieldValue(Reflector.Event_Result_DENY);
        if (this.persistenceRequired) {
            this.idleTime = 0;
        }
        else if ((this.idleTime & 0x1F) == 0x1F && (object = Reflector.call(Reflector.ForgeEventFactory_canEntityDespawn, this)) != object2) {
            if (object == object3) {
                this.idleTime = 0;
            }
            else {
                this.setDead();
            }
        }
        else {
            final Entity entity = this.world.getClosestPlayerToEntity(this, -1.0);
            if (entity != null) {
                final double d0 = entity.posX - this.posX;
                final double d2 = entity.posY - this.posY;
                final double d3 = entity.posZ - this.posZ;
                final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                if (this.canDespawn() && d4 > 16384.0) {
                    this.setDead();
                }
                if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && d4 > 1024.0 && this.canDespawn()) {
                    this.setDead();
                }
                else if (d4 < 1024.0) {
                    this.idleTime = 0;
                }
            }
        }
    }
    
    @Override
    protected final void updateEntityActionState() {
        ++this.idleTime;
        this.world.profiler.startSection("checkDespawn");
        this.despawnEntity();
        this.world.profiler.endSection();
        this.world.profiler.startSection("sensing");
        this.senses.clearSensingCache();
        this.world.profiler.endSection();
        this.world.profiler.startSection("targetSelector");
        this.targetTasks.onUpdateTasks();
        this.world.profiler.endSection();
        this.world.profiler.startSection("goalSelector");
        this.tasks.onUpdateTasks();
        this.world.profiler.endSection();
        this.world.profiler.startSection("navigation");
        this.navigator.onUpdateNavigation();
        this.world.profiler.endSection();
        this.world.profiler.startSection("mob tick");
        this.updateAITasks();
        this.world.profiler.endSection();
        if (this.isRiding() && this.getRidingEntity() instanceof EntityLiving) {
            final EntityLiving entityliving = (EntityLiving)this.getRidingEntity();
            entityliving.getNavigator().setPath(this.getNavigator().getPath(), 1.5);
            entityliving.getMoveHelper().read(this.getMoveHelper());
        }
        this.world.profiler.startSection("controls");
        this.world.profiler.startSection("move");
        this.moveHelper.onUpdateMoveHelper();
        this.world.profiler.endStartSection("look");
        this.lookHelper.onUpdateLook();
        this.world.profiler.endStartSection("jump");
        this.jumpHelper.doJump();
        this.world.profiler.endSection();
        this.world.profiler.endSection();
    }
    
    protected void updateAITasks() {
    }
    
    public int getVerticalFaceSpeed() {
        return 40;
    }
    
    public int getHorizontalFaceSpeed() {
        return 10;
    }
    
    public void faceEntity(final Entity entityIn, final float maxYawIncrease, final float maxPitchIncrease) {
        final double d0 = entityIn.posX - this.posX;
        final double d2 = entityIn.posZ - this.posZ;
        double d3;
        if (entityIn instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
            d3 = entitylivingbase.posY + entitylivingbase.getEyeHeight() - (this.posY + this.getEyeHeight());
        }
        else {
            d3 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (this.posY + this.getEyeHeight());
        }
        final double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        final float f = (float)(MathHelper.atan2(d2, d0) * 57.29577951308232) - 90.0f;
        final float f2 = (float)(-(MathHelper.atan2(d3, d4) * 57.29577951308232));
        this.rotationPitch = this.updateRotation(this.rotationPitch, f2, maxPitchIncrease);
        this.rotationYaw = this.updateRotation(this.rotationYaw, f, maxYawIncrease);
    }
    
    private float updateRotation(final float angle, final float targetAngle, final float maxIncrease) {
        float f = MathHelper.wrapDegrees(targetAngle - angle);
        if (f > maxIncrease) {
            f = maxIncrease;
        }
        if (f < -maxIncrease) {
            f = -maxIncrease;
        }
        return angle + f;
    }
    
    public boolean getCanSpawnHere() {
        final IBlockState iblockstate = this.world.getBlockState(new BlockPos(this).down());
        return iblockstate.canEntitySpawn(this);
    }
    
    public boolean isNotColliding() {
        return !this.world.containsAnyLiquid(this.getEntityBoundingBox()) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this);
    }
    
    public float getRenderSizeModifier() {
        return 1.0f;
    }
    
    public int getMaxSpawnedInChunk() {
        return 4;
    }
    
    @Override
    public int getMaxFallHeight() {
        if (this.getAttackTarget() == null) {
            return 3;
        }
        int i = (int)(this.getHealth() - this.getMaxHealth() * 0.33f);
        i -= (3 - this.world.getDifficulty().getId()) * 4;
        if (i < 0) {
            i = 0;
        }
        return i + 3;
    }
    
    @Override
    public Iterable<ItemStack> getHeldEquipment() {
        return this.inventoryHands;
    }
    
    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return this.inventoryArmor;
    }
    
    @Override
    public ItemStack getItemStackFromSlot(final EntityEquipmentSlot slotIn) {
        switch (slotIn.getSlotType()) {
            case HAND: {
                return this.inventoryHands.get(slotIn.getIndex());
            }
            case ARMOR: {
                return this.inventoryArmor.get(slotIn.getIndex());
            }
            default: {
                return ItemStack.EMPTY;
            }
        }
    }
    
    @Override
    public void setItemStackToSlot(final EntityEquipmentSlot slotIn, final ItemStack stack) {
        switch (slotIn.getSlotType()) {
            case HAND: {
                this.inventoryHands.set(slotIn.getIndex(), stack);
                break;
            }
            case ARMOR: {
                this.inventoryArmor.set(slotIn.getIndex(), stack);
                break;
            }
        }
    }
    
    @Override
    protected void dropEquipment(final boolean wasRecentlyHit, final int lootingModifier) {
        for (final EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
            final ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);
            double d0 = 0.0;
            switch (entityequipmentslot.getSlotType()) {
                case HAND: {
                    d0 = this.inventoryHandsDropChances[entityequipmentslot.getIndex()];
                    break;
                }
                case ARMOR: {
                    d0 = this.inventoryArmorDropChances[entityequipmentslot.getIndex()];
                    break;
                }
                default: {
                    d0 = 0.0;
                    break;
                }
            }
            final boolean flag = d0 > 1.0;
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && (wasRecentlyHit || flag) && this.rand.nextFloat() - lootingModifier * 0.01f < d0) {
                if (!flag && itemstack.isItemStackDamageable()) {
                    itemstack.setItemDamage(itemstack.getMaxDamage() - this.rand.nextInt(1 + this.rand.nextInt(Math.max(itemstack.getMaxDamage() - 3, 1))));
                }
                this.entityDropItem(itemstack, 0.0f);
            }
        }
    }
    
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        if (this.rand.nextFloat() < 0.15f * difficulty.getClampedAdditionalDifficulty()) {
            int i = this.rand.nextInt(2);
            final float f = (this.world.getDifficulty() == EnumDifficulty.HARD) ? 0.1f : 0.25f;
            if (this.rand.nextFloat() < 0.095f) {
                ++i;
            }
            if (this.rand.nextFloat() < 0.095f) {
                ++i;
            }
            if (this.rand.nextFloat() < 0.095f) {
                ++i;
            }
            boolean flag = true;
            for (final EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
                if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                    final ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);
                    if (!flag && this.rand.nextFloat() < f) {
                        break;
                    }
                    flag = false;
                    if (itemstack.isEmpty()) {
                        final Item item = getArmorByChance(entityequipmentslot, i);
                        if (item != null) {
                            this.setItemStackToSlot(entityequipmentslot, new ItemStack(item));
                        }
                    }
                }
            }
        }
    }
    
    public static EntityEquipmentSlot getSlotForItemStack(final ItemStack stack) {
        if (Reflector.ForgeItem_getEquipmentSlot.exists()) {
            final EntityEquipmentSlot entityequipmentslot = (EntityEquipmentSlot)Reflector.call(stack.getItem(), Reflector.ForgeItem_getEquipmentSlot, stack);
            if (entityequipmentslot != null) {
                return entityequipmentslot;
            }
        }
        if (stack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN) || stack.getItem() == Items.SKULL) {
            return EntityEquipmentSlot.HEAD;
        }
        if (stack.getItem() instanceof ItemArmor) {
            return ((ItemArmor)stack.getItem()).armorType;
        }
        if (stack.getItem() == Items.ELYTRA) {
            return EntityEquipmentSlot.CHEST;
        }
        boolean flag = stack.getItem() == Items.SHIELD;
        if (Reflector.ForgeItem_isShield.exists()) {
            flag = Reflector.callBoolean(stack.getItem(), Reflector.ForgeItem_isShield, stack, null);
        }
        return flag ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND;
    }
    
    @Nullable
    public static Item getArmorByChance(final EntityEquipmentSlot slotIn, final int chance) {
        switch (slotIn) {
            case HEAD: {
                if (chance == 0) {
                    return Items.LEATHER_HELMET;
                }
                if (chance == 1) {
                    return Items.GOLDEN_HELMET;
                }
                if (chance == 2) {
                    return Items.CHAINMAIL_HELMET;
                }
                if (chance == 3) {
                    return Items.IRON_HELMET;
                }
                if (chance == 4) {
                    return Items.DIAMOND_HELMET;
                }
            }
            case CHEST: {
                if (chance == 0) {
                    return Items.LEATHER_CHESTPLATE;
                }
                if (chance == 1) {
                    return Items.GOLDEN_CHESTPLATE;
                }
                if (chance == 2) {
                    return Items.CHAINMAIL_CHESTPLATE;
                }
                if (chance == 3) {
                    return Items.IRON_CHESTPLATE;
                }
                if (chance == 4) {
                    return Items.DIAMOND_CHESTPLATE;
                }
            }
            case LEGS: {
                if (chance == 0) {
                    return Items.LEATHER_LEGGINGS;
                }
                if (chance == 1) {
                    return Items.GOLDEN_LEGGINGS;
                }
                if (chance == 2) {
                    return Items.CHAINMAIL_LEGGINGS;
                }
                if (chance == 3) {
                    return Items.IRON_LEGGINGS;
                }
                if (chance == 4) {
                    return Items.DIAMOND_LEGGINGS;
                }
            }
            case FEET: {
                if (chance == 0) {
                    return Items.LEATHER_BOOTS;
                }
                if (chance == 1) {
                    return Items.GOLDEN_BOOTS;
                }
                if (chance == 2) {
                    return Items.CHAINMAIL_BOOTS;
                }
                if (chance == 3) {
                    return Items.IRON_BOOTS;
                }
                if (chance == 4) {
                    return Items.DIAMOND_BOOTS;
                }
                break;
            }
        }
        return null;
    }
    
    protected void setEnchantmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        final float f = difficulty.getClampedAdditionalDifficulty();
        if (!this.getHeldItemMainhand().isEmpty() && this.rand.nextFloat() < 0.25f * f) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItemMainhand(), (int)(5.0f + f * this.rand.nextInt(18)), false));
        }
        for (final EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
            if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                final ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);
                if (!itemstack.isEmpty() && this.rand.nextFloat() < 0.5f * f) {
                    this.setItemStackToSlot(entityequipmentslot, EnchantmentHelper.addRandomEnchantment(this.rand, itemstack, (int)(5.0f + f * this.rand.nextInt(18)), false));
                }
            }
        }
    }
    
    @Nullable
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05, 1));
        if (this.rand.nextFloat() < 0.05f) {
            this.setLeftHanded(true);
        }
        else {
            this.setLeftHanded(false);
        }
        return livingdata;
    }
    
    public boolean canBeSteered() {
        return false;
    }
    
    public void enablePersistence() {
        this.persistenceRequired = true;
    }
    
    public void setDropChance(final EntityEquipmentSlot slotIn, final float chance) {
        switch (slotIn.getSlotType()) {
            case HAND: {
                this.inventoryHandsDropChances[slotIn.getIndex()] = chance;
                break;
            }
            case ARMOR: {
                this.inventoryArmorDropChances[slotIn.getIndex()] = chance;
                break;
            }
        }
    }
    
    public boolean canPickUpLoot() {
        return this.canPickUpLoot;
    }
    
    public void setCanPickUpLoot(final boolean canPickup) {
        this.canPickUpLoot = canPickup;
    }
    
    public boolean isNoDespawnRequired() {
        return this.persistenceRequired;
    }
    
    @Override
    public final boolean processInitialInteract(final EntityPlayer player, final EnumHand hand) {
        if (this.getLeashed() && this.getLeashHolder() == player) {
            this.clearLeashed(true, !player.capabilities.isCreativeMode);
            return true;
        }
        final ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.LEAD && this.canBeLeashedTo(player)) {
            this.setLeashHolder(player, true);
            itemstack.shrink(1);
            return true;
        }
        return this.processInteract(player, hand) || super.processInitialInteract(player, hand);
    }
    
    protected boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        return false;
    }
    
    protected void updateLeashedState() {
        if (this.leashNBTTag != null) {
            this.recreateLeash();
        }
        if (this.isLeashed) {
            if (!this.isEntityAlive()) {
                this.clearLeashed(true, true);
            }
            if (this.leashHolder == null || this.leashHolder.isDead) {
                this.clearLeashed(true, true);
            }
        }
    }
    
    public void clearLeashed(final boolean sendPacket, final boolean dropLead) {
        if (this.isLeashed) {
            this.isLeashed = false;
            this.leashHolder = null;
            if (!this.world.isRemote && dropLead) {
                this.dropItem(Items.LEAD, 1);
            }
            if (!this.world.isRemote && sendPacket && this.world instanceof WorldServer) {
                ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketEntityAttach(this, null));
            }
        }
    }
    
    public boolean canBeLeashedTo(final EntityPlayer player) {
        return !this.getLeashed() && !(this instanceof IMob);
    }
    
    public boolean getLeashed() {
        return this.isLeashed;
    }
    
    public Entity getLeashHolder() {
        return this.leashHolder;
    }
    
    public void setLeashHolder(final Entity entityIn, final boolean sendAttachNotification) {
        this.isLeashed = true;
        this.leashHolder = entityIn;
        if (!this.world.isRemote && sendAttachNotification && this.world instanceof WorldServer) {
            ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketEntityAttach(this, this.leashHolder));
        }
        if (this.isRiding()) {
            this.dismountRidingEntity();
        }
    }
    
    @Override
    public boolean startRiding(final Entity entityIn, final boolean force) {
        final boolean flag = super.startRiding(entityIn, force);
        if (flag && this.getLeashed()) {
            this.clearLeashed(true, true);
        }
        return flag;
    }
    
    private void recreateLeash() {
        if (this.isLeashed && this.leashNBTTag != null) {
            if (this.leashNBTTag.hasUniqueId("UUID")) {
                final UUID uuid = this.leashNBTTag.getUniqueId("UUID");
                for (final EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, this.getEntityBoundingBox().grow(10.0))) {
                    if (entitylivingbase.getUniqueID().equals(uuid)) {
                        this.setLeashHolder(entitylivingbase, true);
                        break;
                    }
                }
            }
            else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99) && this.leashNBTTag.hasKey("Z", 99)) {
                final BlockPos blockpos = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
                EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(this.world, blockpos);
                if (entityleashknot == null) {
                    entityleashknot = EntityLeashKnot.createKnot(this.world, blockpos);
                }
                this.setLeashHolder(entityleashknot, true);
            }
            else {
                this.clearLeashed(false, true);
            }
        }
        this.leashNBTTag = null;
    }
    
    @Override
    public boolean replaceItemInInventory(final int inventorySlot, final ItemStack itemStackIn) {
        EntityEquipmentSlot entityequipmentslot;
        if (inventorySlot == 98) {
            entityequipmentslot = EntityEquipmentSlot.MAINHAND;
        }
        else if (inventorySlot == 99) {
            entityequipmentslot = EntityEquipmentSlot.OFFHAND;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.HEAD.getIndex()) {
            entityequipmentslot = EntityEquipmentSlot.HEAD;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.CHEST.getIndex()) {
            entityequipmentslot = EntityEquipmentSlot.CHEST;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.LEGS.getIndex()) {
            entityequipmentslot = EntityEquipmentSlot.LEGS;
        }
        else {
            if (inventorySlot != 100 + EntityEquipmentSlot.FEET.getIndex()) {
                return false;
            }
            entityequipmentslot = EntityEquipmentSlot.FEET;
        }
        if (!itemStackIn.isEmpty() && !isItemStackInSlot(entityequipmentslot, itemStackIn) && entityequipmentslot != EntityEquipmentSlot.HEAD) {
            return false;
        }
        this.setItemStackToSlot(entityequipmentslot, itemStackIn);
        return true;
    }
    
    @Override
    public boolean canPassengerSteer() {
        return this.canBeSteered() && super.canPassengerSteer();
    }
    
    public static boolean isItemStackInSlot(final EntityEquipmentSlot slotIn, final ItemStack stack) {
        final EntityEquipmentSlot entityequipmentslot = getSlotForItemStack(stack);
        return entityequipmentslot == slotIn || (entityequipmentslot == EntityEquipmentSlot.MAINHAND && slotIn == EntityEquipmentSlot.OFFHAND) || (entityequipmentslot == EntityEquipmentSlot.OFFHAND && slotIn == EntityEquipmentSlot.MAINHAND);
    }
    
    @Override
    public boolean isServerWorld() {
        return super.isServerWorld() && !this.isAIDisabled();
    }
    
    public void setNoAI(final boolean disable) {
        final byte b0 = this.dataManager.get(EntityLiving.AI_FLAGS);
        this.dataManager.set(EntityLiving.AI_FLAGS, disable ? ((byte)(b0 | 0x1)) : ((byte)(b0 & 0xFFFFFFFE)));
    }
    
    public void setLeftHanded(final boolean leftHanded) {
        final byte b0 = this.dataManager.get(EntityLiving.AI_FLAGS);
        this.dataManager.set(EntityLiving.AI_FLAGS, leftHanded ? ((byte)(b0 | 0x2)) : ((byte)(b0 & 0xFFFFFFFD)));
    }
    
    public boolean isAIDisabled() {
        return (this.dataManager.get(EntityLiving.AI_FLAGS) & 0x1) != 0x0;
    }
    
    public boolean isLeftHanded() {
        return (this.dataManager.get(EntityLiving.AI_FLAGS) & 0x2) != 0x0;
    }
    
    @Override
    public EnumHandSide getPrimaryHand() {
        return this.isLeftHanded() ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
    }
    
    private boolean canSkipUpdate() {
        if (this.isChild()) {
            return false;
        }
        if (this.hurtTime > 0) {
            return false;
        }
        if (this.ticksExisted < 20) {
            return false;
        }
        final World world = this.getEntityWorld();
        if (world == null) {
            return false;
        }
        if (world.playerEntities.size() != 1) {
            return false;
        }
        final Entity entity = world.playerEntities.get(0);
        final double d0 = Math.max(Math.abs(this.posX - entity.posX) - 16.0, 0.0);
        final double d2 = Math.max(Math.abs(this.posZ - entity.posZ) - 16.0, 0.0);
        final double d3 = d0 * d0 + d2 * d2;
        return !this.isInRangeToRenderDist(d3);
    }
    
    private void onUpdateMinimal() {
        ++this.idleTime;
        if (this instanceof EntityMob) {
            final float f = this.getBrightness();
            if (f > 0.5f) {
                this.idleTime += 2;
            }
        }
        this.despawnEntity();
    }
    
    @Override
    public Team getTeam() {
        final UUID uuid = this.getUniqueID();
        if (this.teamUuid != uuid) {
            this.teamUuid = uuid;
            this.teamUuidString = uuid.toString();
        }
        return this.world.getScoreboard().getPlayersTeam(this.teamUuidString);
    }
    
    static {
        AI_FLAGS = EntityDataManager.createKey(EntityLiving.class, DataSerializers.BYTE);
    }
    
    public enum SpawnPlacementType
    {
        ON_GROUND, 
        IN_AIR, 
        IN_WATER;
        
        private final BiPredicate<IBlockAccess, BlockPos> spawnPredicate;
        
        private SpawnPlacementType() {
            this.spawnPredicate = null;
        }
        
        private SpawnPlacementType(final BiPredicate<IBlockAccess, BlockPos> p_i11_3_) {
            this.spawnPredicate = p_i11_3_;
        }
        
        public boolean canSpawnAt(final World p_canSpawnAt_1_, final BlockPos p_canSpawnAt_2_) {
            return (this.spawnPredicate != null) ? this.spawnPredicate.test(p_canSpawnAt_1_, p_canSpawnAt_2_) : WorldEntitySpawner.canCreatureTypeSpawnBody(this, p_canSpawnAt_1_, p_canSpawnAt_2_);
        }
    }
}
