/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityLiving
extends EntityLivingBase {
    private Entity leashedToEntity;
    private ItemStack[] equipment = new ItemStack[5];
    private boolean canPickUpLoot;
    protected PathNavigate navigator;
    private EntityLookHelper lookHelper;
    protected int experienceValue;
    protected EntityMoveHelper moveHelper;
    protected final EntityAITasks tasks;
    private EntityBodyHelper bodyHelper;
    private EntityLivingBase attackTarget;
    protected final EntityAITasks targetTasks;
    protected float[] equipmentDropChances = new float[5];
    private EntitySenses senses;
    private boolean persistenceRequired;
    private NBTTagCompound leashNBTTag;
    private boolean isLeashed;
    protected EntityJumpHelper jumpHelper;
    public int livingSoundTime;

    public void clearLeashed(boolean bl, boolean bl2) {
        if (this.isLeashed) {
            this.isLeashed = false;
            this.leashedToEntity = null;
            if (!this.worldObj.isRemote && bl2) {
                this.dropItem(Items.lead, 1);
            }
            if (!this.worldObj.isRemote && bl && this.worldObj instanceof WorldServer) {
                ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S1BPacketEntityAttach(1, this, null));
            }
        }
    }

    public EntityLivingBase getAttackTarget() {
        return this.attackTarget;
    }

    public boolean canPickUpLoot() {
        return this.canPickUpLoot;
    }

    private float updateRotation(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }

    public void eatGrassBonus() {
    }

    protected boolean interact(EntityPlayer entityPlayer) {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            this.updateLeashedState();
        }
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        Item item = this.getDropItem();
        if (item != null) {
            int n2 = this.rand.nextInt(3);
            if (n > 0) {
                n2 += this.rand.nextInt(n + 1);
            }
            int n3 = 0;
            while (n3 < n2) {
                this.dropItem(item, 1);
                ++n3;
            }
        }
    }

    @Override
    public int getMaxFallHeight() {
        if (this.getAttackTarget() == null) {
            return 3;
        }
        int n = (int)(this.getHealth() - this.getMaxHealth() * 0.33f);
        if ((n -= (3 - this.worldObj.getDifficulty().getDifficultyId()) * 4) < 0) {
            n = 0;
        }
        return n + 3;
    }

    protected boolean canDespawn() {
        return true;
    }

    @Override
    public void setCurrentItemOrArmor(int n, ItemStack itemStack) {
        this.equipment[n] = itemStack;
    }

    @Override
    protected final void updateEntityActionState() {
        ++this.entityAge;
        this.worldObj.theProfiler.startSection("checkDespawn");
        this.despawnEntity();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("sensing");
        this.senses.clearSensingCache();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("targetSelector");
        this.targetTasks.onUpdateTasks();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("goalSelector");
        this.tasks.onUpdateTasks();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("navigation");
        this.navigator.onUpdateNavigation();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("mob tick");
        this.updateAITasks();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("controls");
        this.worldObj.theProfiler.startSection("move");
        this.moveHelper.onUpdateMoveHelper();
        this.worldObj.theProfiler.endStartSection("look");
        this.lookHelper.onUpdateLook();
        this.worldObj.theProfiler.endStartSection("jump");
        this.jumpHelper.doJump();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.endSection();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        int n;
        NBTTagList nBTTagList;
        super.readEntityFromNBT(nBTTagCompound);
        if (nBTTagCompound.hasKey("CanPickUpLoot", 1)) {
            this.setCanPickUpLoot(nBTTagCompound.getBoolean("CanPickUpLoot"));
        }
        this.persistenceRequired = nBTTagCompound.getBoolean("PersistenceRequired");
        if (nBTTagCompound.hasKey("Equipment", 9)) {
            nBTTagList = nBTTagCompound.getTagList("Equipment", 10);
            n = 0;
            while (n < this.equipment.length) {
                this.equipment[n] = ItemStack.loadItemStackFromNBT(nBTTagList.getCompoundTagAt(n));
                ++n;
            }
        }
        if (nBTTagCompound.hasKey("DropChances", 9)) {
            nBTTagList = nBTTagCompound.getTagList("DropChances", 5);
            n = 0;
            while (n < nBTTagList.tagCount()) {
                this.equipmentDropChances[n] = nBTTagList.getFloatAt(n);
                ++n;
            }
        }
        this.isLeashed = nBTTagCompound.getBoolean("Leashed");
        if (this.isLeashed && nBTTagCompound.hasKey("Leash", 10)) {
            this.leashNBTTag = nBTTagCompound.getCompoundTag("Leash");
        }
        this.setNoAI(nBTTagCompound.getBoolean("NoAI"));
    }

    public void setAttackTarget(EntityLivingBase entityLivingBase) {
        this.attackTarget = entityLivingBase;
    }

    public EntityLiving(World world) {
        super(world);
        this.tasks = new EntityAITasks(world != null && world.theProfiler != null ? world.theProfiler : null);
        this.targetTasks = new EntityAITasks(world != null && world.theProfiler != null ? world.theProfiler : null);
        this.lookHelper = new EntityLookHelper(this);
        this.moveHelper = new EntityMoveHelper(this);
        this.jumpHelper = new EntityJumpHelper(this);
        this.bodyHelper = new EntityBodyHelper(this);
        this.navigator = this.getNewNavigator(world);
        this.senses = new EntitySenses(this);
        int n = 0;
        while (n < this.equipmentDropChances.length) {
            this.equipmentDropChances[n] = 0.085f;
            ++n;
        }
    }

    public float getRenderSizeModifier() {
        return 1.0f;
    }

    protected PathNavigate getNewNavigator(World world) {
        return new PathNavigateGround(this, world);
    }

    protected void despawnEntity() {
        if (this.persistenceRequired) {
            this.entityAge = 0;
        } else {
            EntityPlayer entityPlayer = this.worldObj.getClosestPlayerToEntity(this, -1.0);
            if (entityPlayer != null) {
                double d = entityPlayer.posX - this.posX;
                double d2 = entityPlayer.posY - this.posY;
                double d3 = entityPlayer.posZ - this.posZ;
                double d4 = d * d + d2 * d2 + d3 * d3;
                if (this.canDespawn() && d4 > 16384.0) {
                    this.setDead();
                }
                if (this.entityAge > 600 && this.rand.nextInt(800) == 0 && d4 > 1024.0 && this.canDespawn()) {
                    this.setDead();
                } else if (d4 < 1024.0) {
                    this.entityAge = 0;
                }
            }
        }
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyInstance, IEntityLivingData iEntityLivingData) {
        this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05, 1));
        return iEntityLivingData;
    }

    public static int getArmorPosition(ItemStack itemStack) {
        if (itemStack.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && itemStack.getItem() != Items.skull) {
            if (itemStack.getItem() instanceof ItemArmor) {
                switch (((ItemArmor)itemStack.getItem()).armorType) {
                    case 0: {
                        return 4;
                    }
                    case 1: {
                        return 3;
                    }
                    case 2: {
                        return 2;
                    }
                    case 3: {
                        return 1;
                    }
                }
            }
            return 0;
        }
        return 4;
    }

    public void playLivingSound() {
        String string = this.getLivingSound();
        if (string != null) {
            this.playSound(string, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    public void faceEntity(Entity entity, float f, float f2) {
        double d;
        double d2 = entity.posX - this.posX;
        double d3 = entity.posZ - this.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            d = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() - (this.posY + (double)this.getEyeHeight());
        } else {
            d = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (this.posY + (double)this.getEyeHeight());
        }
        double d4 = MathHelper.sqrt_double(d2 * d2 + d3 * d3);
        float f3 = (float)(MathHelper.func_181159_b(d3, d2) * 180.0 / Math.PI) - 90.0f;
        float f4 = (float)(-(MathHelper.func_181159_b(d, d4) * 180.0 / Math.PI));
        this.rotationPitch = this.updateRotation(this.rotationPitch, f4, f2);
        this.rotationYaw = this.updateRotation(this.rotationYaw, f3, f);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.worldObj.theProfiler.startSection("looting");
        if (!this.worldObj.isRemote && this.canPickUpLoot() && !this.dead && this.worldObj.getGameRules().getBoolean("mobGriefing")) {
            for (EntityItem entityItem : this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(1.0, 0.0, 1.0))) {
                if (entityItem.isDead || entityItem.getEntityItem() == null || entityItem.cannotPickup()) continue;
                this.updateEquipmentIfNeeded(entityItem);
            }
        }
        this.worldObj.theProfiler.endSection();
    }

    @Override
    public boolean replaceItemInInventory(int n, ItemStack itemStack) {
        int n2;
        if (n == 99) {
            n2 = 0;
        } else {
            n2 = n - 100 + 1;
            if (n2 < 0 || n2 >= this.equipment.length) {
                return false;
            }
        }
        if (!(itemStack == null || EntityLiving.getArmorPosition(itemStack) == n2 || n2 == 4 && itemStack.getItem() instanceof ItemBlock)) {
            return false;
        }
        this.setCurrentItemOrArmor(n2, itemStack);
        return true;
    }

    public boolean getCanSpawnHere() {
        return true;
    }

    public boolean canAttackClass(Class<? extends EntityLivingBase> clazz) {
        return clazz != EntityGhast.class;
    }

    protected void updateLeashedState() {
        if (this.leashNBTTag != null) {
            this.recreateLeash();
        }
        if (this.isLeashed) {
            if (!this.isEntityAlive()) {
                this.clearLeashed(true, true);
            }
            if (this.leashedToEntity == null || this.leashedToEntity.isDead) {
                this.clearLeashed(true, true);
            }
        }
    }

    public void setNoAI(boolean bl) {
        this.dataWatcher.updateObject(15, (byte)(bl ? (char)'\u0001' : '\u0000'));
    }

    public boolean isNotColliding() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }

    public PathNavigate getNavigator() {
        return this.navigator;
    }

    public boolean allowLeashing() {
        return !this.getLeashed() && !(this instanceof IMob);
    }

    public int getTalkInterval() {
        return 80;
    }

    public Entity getLeashedToEntity() {
        return this.leashedToEntity;
    }

    public int getVerticalFaceSpeed() {
        return 40;
    }

    @Override
    public ItemStack getEquipmentInSlot(int n) {
        return this.equipment[n];
    }

    public EntitySenses getEntitySenses() {
        return this.senses;
    }

    protected Item getDropItem() {
        return null;
    }

    public void setEquipmentDropChance(int n, float f) {
        this.equipmentDropChances[n] = f;
    }

    public boolean isAIDisabled() {
        return this.dataWatcher.getWatchableObjectByte(15) != 0;
    }

    public void setCanPickUpLoot(boolean bl) {
        this.canPickUpLoot = bl;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(15, (byte)0);
    }

    @Override
    public ItemStack getHeldItem() {
        return this.equipment[0];
    }

    public void setLeashedToEntity(Entity entity, boolean bl) {
        this.isLeashed = true;
        this.leashedToEntity = entity;
        if (!this.worldObj.isRemote && bl && this.worldObj instanceof WorldServer) {
            ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S1BPacketEntityAttach(1, this, this.leashedToEntity));
        }
    }

    public void enablePersistence() {
        this.persistenceRequired = true;
    }

    @Override
    protected void dropEquipment(boolean bl, int n) {
        int n2 = 0;
        while (n2 < this.getInventory().length) {
            boolean bl2;
            ItemStack itemStack = this.getEquipmentInSlot(n2);
            boolean bl3 = bl2 = this.equipmentDropChances[n2] > 1.0f;
            if (itemStack != null && (bl || bl2) && this.rand.nextFloat() - (float)n * 0.01f < this.equipmentDropChances[n2]) {
                if (!bl2 && itemStack.isItemStackDamageable()) {
                    int n3 = Math.max(itemStack.getMaxDamage() - 25, 1);
                    int n4 = itemStack.getMaxDamage() - this.rand.nextInt(this.rand.nextInt(n3) + 1);
                    if (n4 > n3) {
                        n4 = n3;
                    }
                    if (n4 < 1) {
                        n4 = 1;
                    }
                    itemStack.setItemDamage(n4);
                }
                this.entityDropItem(itemStack, 0.0f);
            }
            ++n2;
        }
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        if (this.rand.nextFloat() < 0.15f * difficultyInstance.getClampedAdditionalDifficulty()) {
            float f;
            int n = this.rand.nextInt(2);
            float f2 = f = this.worldObj.getDifficulty() == EnumDifficulty.HARD ? 0.1f : 0.25f;
            if (this.rand.nextFloat() < 0.095f) {
                ++n;
            }
            if (this.rand.nextFloat() < 0.095f) {
                ++n;
            }
            if (this.rand.nextFloat() < 0.095f) {
                ++n;
            }
            int n2 = 3;
            while (n2 >= 0) {
                Item item;
                ItemStack itemStack = this.getCurrentArmor(n2);
                if (n2 < 3 && this.rand.nextFloat() < f) break;
                if (itemStack == null && (item = EntityLiving.getArmorItemForSlot(n2 + 1, n)) != null) {
                    this.setCurrentItemOrArmor(n2 + 1, new ItemStack(item));
                }
                --n2;
            }
        }
    }

    public EntityLookHelper getLookHelper() {
        return this.lookHelper;
    }

    @Override
    public boolean isServerWorld() {
        return super.isServerWorld() && !this.isAIDisabled();
    }

    public void spawnExplosionParticle() {
        if (this.worldObj.isRemote) {
            int n = 0;
            while (n < 20) {
                double d = this.rand.nextGaussian() * 0.02;
                double d2 = this.rand.nextGaussian() * 0.02;
                double d3 = this.rand.nextGaussian() * 0.02;
                double d4 = 10.0;
                this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width - d * d4, this.posY + (double)(this.rand.nextFloat() * this.height) - d2 * d4, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width - d3 * d4, d, d2, d3, new int[0]);
                ++n;
            }
        } else {
            this.worldObj.setEntityState(this, (byte)20);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setBoolean("CanPickUpLoot", this.canPickUpLoot());
        nBTTagCompound.setBoolean("PersistenceRequired", this.persistenceRequired);
        NBTTagList nBTTagList = new NBTTagList();
        int n = 0;
        while (n < this.equipment.length) {
            NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
            if (this.equipment[n] != null) {
                this.equipment[n].writeToNBT(nBTTagCompound2);
            }
            nBTTagList.appendTag(nBTTagCompound2);
            ++n;
        }
        nBTTagCompound.setTag("Equipment", nBTTagList);
        NBTTagList nBTTagList2 = new NBTTagList();
        int n2 = 0;
        while (n2 < this.equipmentDropChances.length) {
            nBTTagList2.appendTag(new NBTTagFloat(this.equipmentDropChances[n2]));
            ++n2;
        }
        nBTTagCompound.setTag("DropChances", nBTTagList2);
        nBTTagCompound.setBoolean("Leashed", this.isLeashed);
        if (this.leashedToEntity != null) {
            NBTTagCompound nBTTagCompound3 = new NBTTagCompound();
            if (this.leashedToEntity instanceof EntityLivingBase) {
                nBTTagCompound3.setLong("UUIDMost", this.leashedToEntity.getUniqueID().getMostSignificantBits());
                nBTTagCompound3.setLong("UUIDLeast", this.leashedToEntity.getUniqueID().getLeastSignificantBits());
            } else if (this.leashedToEntity instanceof EntityHanging) {
                BlockPos blockPos = ((EntityHanging)this.leashedToEntity).getHangingPosition();
                nBTTagCompound3.setInteger("X", blockPos.getX());
                nBTTagCompound3.setInteger("Y", blockPos.getY());
                nBTTagCompound3.setInteger("Z", blockPos.getZ());
            }
            nBTTagCompound.setTag("Leash", nBTTagCompound3);
        }
        if (this.isAIDisabled()) {
            nBTTagCompound.setBoolean("NoAI", this.isAIDisabled());
        }
    }

    @Override
    public ItemStack getCurrentArmor(int n) {
        return this.equipment[n + 1];
    }

    public EntityJumpHelper getJumpHelper() {
        return this.jumpHelper;
    }

    public int getMaxSpawnedInChunk() {
        return 4;
    }

    @Override
    public void setAIMoveSpeed(float f) {
        super.setAIMoveSpeed(f);
        this.setMoveForward(f);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0);
    }

    protected void setEnchantmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        float f = difficultyInstance.getClampedAdditionalDifficulty();
        if (this.getHeldItem() != null && this.rand.nextFloat() < 0.25f * f) {
            EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItem(), (int)(5.0f + f * (float)this.rand.nextInt(18)));
        }
        int n = 0;
        while (n < 4) {
            ItemStack itemStack = this.getCurrentArmor(n);
            if (itemStack != null && this.rand.nextFloat() < 0.5f * f) {
                EnchantmentHelper.addRandomEnchantment(this.rand, itemStack, (int)(5.0f + f * (float)this.rand.nextInt(18)));
            }
            ++n;
        }
    }

    public void setMoveForward(float f) {
        this.moveForward = f;
    }

    private void recreateLeash() {
        if (this.isLeashed && this.leashNBTTag != null) {
            if (this.leashNBTTag.hasKey("UUIDMost", 4) && this.leashNBTTag.hasKey("UUIDLeast", 4)) {
                UUID uUID = new UUID(this.leashNBTTag.getLong("UUIDMost"), this.leashNBTTag.getLong("UUIDLeast"));
                for (EntityLivingBase entityLivingBase : this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(10.0, 10.0, 10.0))) {
                    if (!entityLivingBase.getUniqueID().equals(uUID)) continue;
                    this.leashedToEntity = entityLivingBase;
                    break;
                }
            } else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99) && this.leashNBTTag.hasKey("Z", 99)) {
                BlockPos blockPos = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
                EntityLeashKnot entityLeashKnot = EntityLeashKnot.getKnotForPosition(this.worldObj, blockPos);
                if (entityLeashKnot == null) {
                    entityLeashKnot = EntityLeashKnot.createKnot(this.worldObj, blockPos);
                }
                this.leashedToEntity = entityLeashKnot;
            } else {
                this.clearLeashed(false, true);
            }
        }
        this.leashNBTTag = null;
    }

    @Override
    public final boolean interactFirst(EntityPlayer entityPlayer) {
        if (this.getLeashed() && this.getLeashedToEntity() == entityPlayer) {
            this.clearLeashed(true, !entityPlayer.capabilities.isCreativeMode);
            return true;
        }
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        if (itemStack != null && itemStack.getItem() == Items.lead && this.allowLeashing()) {
            if (!(this instanceof EntityTameable) || !((EntityTameable)this).isTamed()) {
                this.setLeashedToEntity(entityPlayer, true);
                --itemStack.stackSize;
                return true;
            }
            if (((EntityTameable)this).isOwner(entityPlayer)) {
                this.setLeashedToEntity(entityPlayer, true);
                --itemStack.stackSize;
                return true;
            }
        }
        if (this.interact(entityPlayer)) {
            return true;
        }
        return super.interactFirst(entityPlayer);
    }

    protected boolean func_175448_a(ItemStack itemStack) {
        return true;
    }

    public EntityMoveHelper getMoveHelper() {
        return this.moveHelper;
    }

    public boolean isNoDespawnRequired() {
        return this.persistenceRequired;
    }

    @Override
    public ItemStack[] getInventory() {
        return this.equipment;
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 20) {
            this.spawnExplosionParticle();
        } else {
            super.handleStatusUpdate(by);
        }
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        this.worldObj.theProfiler.startSection("mobBaseTick");
        if (this.isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
            this.livingSoundTime = -this.getTalkInterval();
            this.playLivingSound();
        }
        this.worldObj.theProfiler.endSection();
    }

    protected void updateAITasks() {
    }

    public boolean getLeashed() {
        return this.isLeashed;
    }

    public static Item getArmorItemForSlot(int n, int n2) {
        switch (n) {
            case 4: {
                if (n2 == 0) {
                    return Items.leather_helmet;
                }
                if (n2 == 1) {
                    return Items.golden_helmet;
                }
                if (n2 == 2) {
                    return Items.chainmail_helmet;
                }
                if (n2 == 3) {
                    return Items.iron_helmet;
                }
                if (n2 == 4) {
                    return Items.diamond_helmet;
                }
            }
            case 3: {
                if (n2 == 0) {
                    return Items.leather_chestplate;
                }
                if (n2 == 1) {
                    return Items.golden_chestplate;
                }
                if (n2 == 2) {
                    return Items.chainmail_chestplate;
                }
                if (n2 == 3) {
                    return Items.iron_chestplate;
                }
                if (n2 == 4) {
                    return Items.diamond_chestplate;
                }
            }
            case 2: {
                if (n2 == 0) {
                    return Items.leather_leggings;
                }
                if (n2 == 1) {
                    return Items.golden_leggings;
                }
                if (n2 == 2) {
                    return Items.chainmail_leggings;
                }
                if (n2 == 3) {
                    return Items.iron_leggings;
                }
                if (n2 == 4) {
                    return Items.diamond_leggings;
                }
            }
            case 1: {
                if (n2 == 0) {
                    return Items.leather_boots;
                }
                if (n2 == 1) {
                    return Items.golden_boots;
                }
                if (n2 == 2) {
                    return Items.chainmail_boots;
                }
                if (n2 == 3) {
                    return Items.iron_boots;
                }
                if (n2 != 4) break;
                return Items.diamond_boots;
            }
        }
        return null;
    }

    @Override
    protected float func_110146_f(float f, float f2) {
        this.bodyHelper.updateRenderAngles();
        return f2;
    }

    protected String getLivingSound() {
        return null;
    }

    public boolean canBeSteered() {
        return false;
    }

    protected void updateEquipmentIfNeeded(EntityItem entityItem) {
        ItemStack itemStack = entityItem.getEntityItem();
        int n = EntityLiving.getArmorPosition(itemStack);
        if (n > -1) {
            Object object;
            boolean bl = true;
            ItemStack itemStack2 = this.getEquipmentInSlot(n);
            if (itemStack2 != null) {
                if (n == 0) {
                    if (itemStack.getItem() instanceof ItemSword && !(itemStack2.getItem() instanceof ItemSword)) {
                        bl = true;
                    } else if (itemStack.getItem() instanceof ItemSword && itemStack2.getItem() instanceof ItemSword) {
                        object = (ItemSword)itemStack.getItem();
                        ItemSword itemSword = (ItemSword)itemStack2.getItem();
                        bl = ((ItemSword)object).getDamageVsEntity() != itemSword.getDamageVsEntity() ? ((ItemSword)object).getDamageVsEntity() > itemSword.getDamageVsEntity() : itemStack.getMetadata() > itemStack2.getMetadata() || itemStack.hasTagCompound() && !itemStack2.hasTagCompound();
                    } else {
                        bl = itemStack.getItem() instanceof ItemBow && itemStack2.getItem() instanceof ItemBow ? itemStack.hasTagCompound() && !itemStack2.hasTagCompound() : false;
                    }
                } else if (itemStack.getItem() instanceof ItemArmor && !(itemStack2.getItem() instanceof ItemArmor)) {
                    bl = true;
                } else if (itemStack.getItem() instanceof ItemArmor && itemStack2.getItem() instanceof ItemArmor) {
                    object = (ItemArmor)itemStack.getItem();
                    ItemArmor itemArmor = (ItemArmor)itemStack2.getItem();
                    bl = ((ItemArmor)object).damageReduceAmount != itemArmor.damageReduceAmount ? ((ItemArmor)object).damageReduceAmount > itemArmor.damageReduceAmount : itemStack.getMetadata() > itemStack2.getMetadata() || itemStack.hasTagCompound() && !itemStack2.hasTagCompound();
                } else {
                    bl = false;
                }
            }
            if (bl && this.func_175448_a(itemStack)) {
                if (itemStack2 != null && this.rand.nextFloat() - 0.1f < this.equipmentDropChances[n]) {
                    this.entityDropItem(itemStack2, 0.0f);
                }
                if (itemStack.getItem() == Items.diamond && entityItem.getThrower() != null && (object = this.worldObj.getPlayerEntityByName(entityItem.getThrower())) != null) {
                    ((EntityPlayer)object).triggerAchievement(AchievementList.diamondsToYou);
                }
                this.setCurrentItemOrArmor(n, itemStack);
                this.equipmentDropChances[n] = 2.0f;
                this.persistenceRequired = true;
                this.onItemPickup(entityItem, 1);
                entityItem.setDead();
            }
        }
    }

    @Override
    protected int getExperiencePoints(EntityPlayer entityPlayer) {
        if (this.experienceValue > 0) {
            int n = this.experienceValue;
            ItemStack[] itemStackArray = this.getInventory();
            int n2 = 0;
            while (n2 < itemStackArray.length) {
                if (itemStackArray[n2] != null && this.equipmentDropChances[n2] <= 1.0f) {
                    n += 1 + this.rand.nextInt(3);
                }
                ++n2;
            }
            return n;
        }
        return this.experienceValue;
    }

    public static enum SpawnPlacementType {
        ON_GROUND,
        IN_AIR,
        IN_WATER;

    }
}

