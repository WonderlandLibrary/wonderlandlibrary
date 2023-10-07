// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.Minecraft;
import net.minecraft.optifine.BlockPosM;
import net.minecraft.item.ItemBlock;
import java.util.UUID;
import net.minecraft.entity.monster.IMob;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.MathHelper;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.BlockPos;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.Item;
import net.minecraft.optifine.Config;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.optifine.Reflector;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntityLookHelper;

public abstract class EntityLiving extends EntityLivingBase
{
    public int livingSoundTime;
    protected int experienceValue;
    private EntityLookHelper lookHelper;
    protected EntityMoveHelper moveHelper;
    protected EntityJumpHelper jumpHelper;
    private EntityBodyHelper bodyHelper;
    protected PathNavigate navigator;
    protected final EntityAITasks tasks;
    protected final EntityAITasks targetTasks;
    private EntityLivingBase attackTarget;
    private EntitySenses senses;
    private ItemStack[] equipment;
    protected float[] equipmentDropChances;
    private boolean canPickUpLoot;
    private boolean persistenceRequired;
    private boolean isLeashed;
    private Entity leashedToEntity;
    private NBTTagCompound leashNBTTag;
    private static final String __OBFID = "CL_00001550";
    
    public EntityLiving(final World worldIn) {
        super(worldIn);
        this.equipment = new ItemStack[5];
        this.equipmentDropChances = new float[5];
        this.tasks = new EntityAITasks((worldIn != null && worldIn.theProfiler != null) ? worldIn.theProfiler : null);
        this.targetTasks = new EntityAITasks((worldIn != null && worldIn.theProfiler != null) ? worldIn.theProfiler : null);
        this.lookHelper = new EntityLookHelper(this);
        this.moveHelper = new EntityMoveHelper(this);
        this.jumpHelper = new EntityJumpHelper(this);
        this.bodyHelper = new EntityBodyHelper(this);
        this.navigator = this.func_175447_b(worldIn);
        this.senses = new EntitySenses(this);
        for (int var2 = 0; var2 < this.equipmentDropChances.length; ++var2) {
            this.equipmentDropChances[var2] = 0.085f;
        }
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0);
    }
    
    protected PathNavigate func_175447_b(final World worldIn) {
        return new PathNavigateGround(this, worldIn);
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
    
    public EntityLivingBase getAttackTarget() {
        return this.attackTarget;
    }
    
    public void setAttackTarget(final EntityLivingBase p_70624_1_) {
        this.attackTarget = p_70624_1_;
        Reflector.callVoid(Reflector.ForgeHooks_onLivingSetAttackTarget, this, p_70624_1_);
    }
    
    public boolean canAttackClass(final Class p_70686_1_) {
        return p_70686_1_ != EntityGhast.class;
    }
    
    public void eatGrassBonus() {
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(15, (byte)0);
    }
    
    public int getTalkInterval() {
        return 80;
    }
    
    public void playLivingSound() {
        final String var1 = this.getLivingSound();
        if (var1 != null) {
            this.playSound(var1, this.getSoundVolume(), this.getSoundPitch());
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
    
    @Override
    protected int getExperiencePoints(final EntityPlayer p_70693_1_) {
        if (this.experienceValue > 0) {
            int var2 = this.experienceValue;
            final ItemStack[] var3 = this.getInventory();
            for (int var4 = 0; var4 < var3.length; ++var4) {
                if (var3[var4] != null && this.equipmentDropChances[var4] <= 1.0f) {
                    var2 += 1 + this.rand.nextInt(3);
                }
            }
            return var2;
        }
        return this.experienceValue;
    }
    
    public void spawnExplosionParticle() {
        if (this.worldObj.isRemote) {
            for (int var1 = 0; var1 < 20; ++var1) {
                final double var2 = this.rand.nextGaussian() * 0.02;
                final double var3 = this.rand.nextGaussian() * 0.02;
                final double var4 = this.rand.nextGaussian() * 0.02;
                final double var5 = 10.0;
                this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width - var2 * var5, this.posY + this.rand.nextFloat() * this.height - var3 * var5, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width - var4 * var5, var2, var3, var4, new int[0]);
            }
        }
        else {
            this.worldObj.setEntityState(this, (byte)20);
        }
    }
    
    @Override
    public void handleHealthUpdate(final byte p_70103_1_) {
        if (p_70103_1_ == 20) {
            this.spawnExplosionParticle();
        }
        else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }
    
    @Override
    public void onUpdate() {
        if (Config.isSmoothWorld() && this.canSkipUpdate()) {
            this.onUpdateMinimal();
        }
        else {
            super.onUpdate();
            if (!this.worldObj.isRemote) {
                this.updateLeashedState();
            }
        }
    }
    
    @Override
    protected float func_110146_f(final float p_110146_1_, final float p_110146_2_) {
        this.bodyHelper.updateRenderAngles();
        return p_110146_2_;
    }
    
    protected String getLivingSound() {
        return null;
    }
    
    protected Item getDropItem() {
        return null;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        final Item var3 = this.getDropItem();
        if (var3 != null) {
            int var4 = this.rand.nextInt(3);
            if (p_70628_2_ > 0) {
                var4 += this.rand.nextInt(p_70628_2_ + 1);
            }
            for (int var5 = 0; var5 < var4; ++var5) {
                this.dropItem(var3, 1);
            }
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("CanPickUpLoot", this.canPickUpLoot());
        tagCompound.setBoolean("PersistenceRequired", this.persistenceRequired);
        final NBTTagList var2 = new NBTTagList();
        for (final ItemStack element : this.equipment) {
            final NBTTagCompound var3 = new NBTTagCompound();
            if (element != null) {
                element.writeToNBT(var3);
            }
            var2.appendTag(var3);
        }
        tagCompound.setTag("Equipment", var2);
        final NBTTagList var4 = new NBTTagList();
        for (final float equipmentDropChance : this.equipmentDropChances) {
            var4.appendTag(new NBTTagFloat(equipmentDropChance));
        }
        tagCompound.setTag("DropChances", var4);
        tagCompound.setBoolean("Leashed", this.isLeashed);
        if (this.leashedToEntity != null) {
            final NBTTagCompound var3 = new NBTTagCompound();
            if (this.leashedToEntity instanceof EntityLivingBase) {
                var3.setLong("UUIDMost", this.leashedToEntity.getUniqueID().getMostSignificantBits());
                var3.setLong("UUIDLeast", this.leashedToEntity.getUniqueID().getLeastSignificantBits());
            }
            else if (this.leashedToEntity instanceof EntityHanging) {
                final BlockPos var5 = ((EntityHanging)this.leashedToEntity).func_174857_n();
                var3.setInteger("X", var5.getX());
                var3.setInteger("Y", var5.getY());
                var3.setInteger("Z", var5.getZ());
            }
            tagCompound.setTag("Leash", var3);
        }
        if (this.isAIDisabled()) {
            tagCompound.setBoolean("NoAI", this.isAIDisabled());
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("CanPickUpLoot", 1)) {
            this.setCanPickUpLoot(tagCompund.getBoolean("CanPickUpLoot"));
        }
        this.persistenceRequired = tagCompund.getBoolean("PersistenceRequired");
        if (tagCompund.hasKey("Equipment", 9)) {
            final NBTTagList var2 = tagCompund.getTagList("Equipment", 10);
            for (int var3 = 0; var3 < this.equipment.length; ++var3) {
                this.equipment[var3] = ItemStack.loadItemStackFromNBT(var2.getCompoundTagAt(var3));
            }
        }
        if (tagCompund.hasKey("DropChances", 9)) {
            final NBTTagList var2 = tagCompund.getTagList("DropChances", 5);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                this.equipmentDropChances[var3] = var2.getFloat(var3);
            }
        }
        this.isLeashed = tagCompund.getBoolean("Leashed");
        if (this.isLeashed && tagCompund.hasKey("Leash", 10)) {
            this.leashNBTTag = tagCompund.getCompoundTag("Leash");
        }
        this.setNoAI(tagCompund.getBoolean("NoAI"));
    }
    
    public void setMoveForward(final float p_70657_1_) {
        this.moveForward = p_70657_1_;
    }
    
    @Override
    public void setAIMoveSpeed(final float p_70659_1_) {
        super.setAIMoveSpeed(p_70659_1_);
        this.setMoveForward(p_70659_1_);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.worldObj.theProfiler.startSection("looting");
        if (!this.worldObj.isRemote && this.canPickUpLoot() && !this.dead && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
            final List var1 = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(1.0, 0.0, 1.0));
            for (final EntityItem var3 : var1) {
                if (!var3.isDead && var3.getEntityItem() != null && !var3.func_174874_s()) {
                    this.func_175445_a(var3);
                }
            }
        }
        this.worldObj.theProfiler.endSection();
    }
    
    protected void func_175445_a(final EntityItem p_175445_1_) {
        final ItemStack var2 = p_175445_1_.getEntityItem();
        final int var3 = getArmorPosition(var2);
        if (var3 > -1) {
            boolean var4 = true;
            final ItemStack var5 = this.getEquipmentInSlot(var3);
            if (var5 != null) {
                if (var3 == 0) {
                    if (var2.getItem() instanceof ItemSword && !(var5.getItem() instanceof ItemSword)) {
                        var4 = true;
                    }
                    else if (var2.getItem() instanceof ItemSword && var5.getItem() instanceof ItemSword) {
                        final ItemSword var6 = (ItemSword)var2.getItem();
                        final ItemSword var7 = (ItemSword)var5.getItem();
                        if (var6.getDamageGiven() == var7.getDamageGiven()) {
                            var4 = (var2.getMetadata() > var5.getMetadata() || (var2.hasTagCompound() && !var5.hasTagCompound()));
                        }
                        else {
                            var4 = (var6.getDamageGiven() > var7.getDamageGiven());
                        }
                    }
                    else {
                        var4 = (var2.getItem() instanceof ItemBow && var5.getItem() instanceof ItemBow && var2.hasTagCompound() && !var5.hasTagCompound());
                    }
                }
                else if (var2.getItem() instanceof ItemArmor && !(var5.getItem() instanceof ItemArmor)) {
                    var4 = true;
                }
                else if (var2.getItem() instanceof ItemArmor && var5.getItem() instanceof ItemArmor) {
                    final ItemArmor var8 = (ItemArmor)var2.getItem();
                    final ItemArmor var9 = (ItemArmor)var5.getItem();
                    if (var8.damageReduceAmount == var9.damageReduceAmount) {
                        var4 = (var2.getMetadata() > var5.getMetadata() || (var2.hasTagCompound() && !var5.hasTagCompound()));
                    }
                    else {
                        var4 = (var8.damageReduceAmount > var9.damageReduceAmount);
                    }
                }
                else {
                    var4 = false;
                }
            }
            if (var4 && this.func_175448_a(var2)) {
                if (var5 != null && this.rand.nextFloat() - 0.1f < this.equipmentDropChances[var3]) {
                    this.entityDropItem(var5, 0.0f);
                }
                if (var2.getItem() == Items.diamond && p_175445_1_.getThrower() != null) {
                    final EntityPlayer var10 = this.worldObj.getPlayerEntityByName(p_175445_1_.getThrower());
                    if (var10 != null) {
                        var10.triggerAchievement(AchievementList.diamondsToYou);
                    }
                }
                this.setCurrentItemOrArmor(var3, var2);
                this.equipmentDropChances[var3] = 2.0f;
                this.persistenceRequired = true;
                this.onItemPickup(p_175445_1_, 1);
                p_175445_1_.setDead();
            }
        }
    }
    
    protected boolean func_175448_a(final ItemStack p_175448_1_) {
        return true;
    }
    
    protected boolean canDespawn() {
        return true;
    }
    
    protected void despawnEntity() {
        Object result = null;
        final Object Result_DEFAULT = Reflector.getFieldValue(Reflector.Event_Result_DEFAULT);
        final Object Result_DENY = Reflector.getFieldValue(Reflector.Event_Result_DENY);
        if (this.persistenceRequired) {
            this.entityAge = 0;
        }
        else if ((this.entityAge & 0x1F) == 0x1F && (result = Reflector.call(Reflector.ForgeEventFactory_canEntityDespawn, this)) != Result_DEFAULT) {
            if (result == Result_DENY) {
                this.entityAge = 0;
            }
            else {
                this.setDead();
            }
        }
        else {
            final EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, -1.0);
            if (var1 != null) {
                final double var2 = var1.posX - this.posX;
                final double var3 = var1.posY - this.posY;
                final double var4 = var1.posZ - this.posZ;
                final double var5 = var2 * var2 + var3 * var3 + var4 * var4;
                if (this.canDespawn() && var5 > 16384.0) {
                    this.setDead();
                }
                if (this.entityAge > 600 && this.rand.nextInt(800) == 0 && var5 > 1024.0 && this.canDespawn()) {
                    this.setDead();
                }
                else if (var5 < 1024.0) {
                    this.entityAge = 0;
                }
            }
        }
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
    
    protected void updateAITasks() {
    }
    
    public int getVerticalFaceSpeed() {
        return 40;
    }
    
    public void faceEntity(final Entity p_70625_1_, final float p_70625_2_, final float p_70625_3_) {
        final double var4 = p_70625_1_.posX - this.posX;
        final double var5 = p_70625_1_.posZ - this.posZ;
        double var7;
        if (p_70625_1_ instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase)p_70625_1_;
            var7 = var6.posY + var6.getEyeHeight() - (this.posY + this.getEyeHeight());
        }
        else {
            var7 = (p_70625_1_.getEntityBoundingBox().minY + p_70625_1_.getEntityBoundingBox().maxY) / 2.0 - (this.posY + this.getEyeHeight());
        }
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var9 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var10 = (float)(-(Math.atan2(var7, var8) * 180.0 / 3.141592653589793));
        this.rotationPitch = this.updateRotation(this.rotationPitch, var10, p_70625_3_);
        this.rotationYaw = this.updateRotation(this.rotationYaw, var9, p_70625_2_);
    }
    
    private float updateRotation(final float p_70663_1_, final float p_70663_2_, final float p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }
        return p_70663_1_ + var4;
    }
    
    public boolean getCanSpawnHere() {
        return true;
    }
    
    public boolean handleLavaMovement() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
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
        int var1 = (int)(this.getHealth() - this.getMaxHealth() * 0.33f);
        var1 -= (3 - this.worldObj.getDifficulty().getDifficultyId()) * 4;
        if (var1 < 0) {
            var1 = 0;
        }
        return var1 + 3;
    }
    
    @Override
    public ItemStack getHeldItem() {
        return this.equipment[0];
    }
    
    @Override
    public ItemStack getEquipmentInSlot(final int slotIn) {
        return this.equipment[slotIn];
    }
    
    @Override
    public ItemStack getCurrentArmor(final int slotIn) {
        return this.equipment[slotIn + 1];
    }
    
    @Override
    public void setCurrentItemOrArmor(final int slotIn, final ItemStack stack) {
        this.equipment[slotIn] = stack;
    }
    
    @Override
    public ItemStack[] getInventory() {
        return this.equipment;
    }
    
    @Override
    protected void dropEquipment(final boolean p_82160_1_, final int p_82160_2_) {
        for (int var3 = 0; var3 < this.getInventory().length; ++var3) {
            final ItemStack var4 = this.getEquipmentInSlot(var3);
            final boolean var5 = this.equipmentDropChances[var3] > 1.0f;
            if (var4 != null && (p_82160_1_ || var5) && this.rand.nextFloat() - p_82160_2_ * 0.01f < this.equipmentDropChances[var3]) {
                if (!var5 && var4.isItemStackDamageable()) {
                    final int var6 = Math.max(var4.getMaxDamage() - 25, 1);
                    int var7 = var4.getMaxDamage() - this.rand.nextInt(this.rand.nextInt(var6) + 1);
                    if (var7 > var6) {
                        var7 = var6;
                    }
                    if (var7 < 1) {
                        var7 = 1;
                    }
                    var4.setItemDamage(var7);
                }
                this.entityDropItem(var4, 0.0f);
            }
        }
    }
    
    protected void func_180481_a(final DifficultyInstance p_180481_1_) {
        if (this.rand.nextFloat() < 0.15f * p_180481_1_.func_180170_c()) {
            int var2 = this.rand.nextInt(2);
            final float var3 = (this.worldObj.getDifficulty() == EnumDifficulty.HARD) ? 0.1f : 0.25f;
            if (this.rand.nextFloat() < 0.095f) {
                ++var2;
            }
            if (this.rand.nextFloat() < 0.095f) {
                ++var2;
            }
            if (this.rand.nextFloat() < 0.095f) {
                ++var2;
            }
            for (int var4 = 3; var4 >= 0; --var4) {
                final ItemStack var5 = this.getCurrentArmor(var4);
                if (var4 < 3 && this.rand.nextFloat() < var3) {
                    break;
                }
                if (var5 == null) {
                    final Item var6 = getArmorItemForSlot(var4 + 1, var2);
                    if (var6 != null) {
                        this.setCurrentItemOrArmor(var4 + 1, new ItemStack(var6));
                    }
                }
            }
        }
    }
    
    public static int getArmorPosition(final ItemStack p_82159_0_) {
        if (p_82159_0_.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && p_82159_0_.getItem() != Items.skull) {
            if (p_82159_0_.getItem() instanceof ItemArmor) {
                switch (((ItemArmor)p_82159_0_.getItem()).armorType) {
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
    
    public static Item getArmorItemForSlot(final int armorSlot, final int itemTier) {
        switch (armorSlot) {
            case 4: {
                if (itemTier == 0) {
                    return Items.leather_helmet;
                }
                if (itemTier == 1) {
                    return Items.golden_helmet;
                }
                if (itemTier == 2) {
                    return Items.chainmail_helmet;
                }
                if (itemTier == 3) {
                    return Items.iron_helmet;
                }
                if (itemTier == 4) {
                    return Items.diamond_helmet;
                }
            }
            case 3: {
                if (itemTier == 0) {
                    return Items.leather_chestplate;
                }
                if (itemTier == 1) {
                    return Items.golden_chestplate;
                }
                if (itemTier == 2) {
                    return Items.chainmail_chestplate;
                }
                if (itemTier == 3) {
                    return Items.iron_chestplate;
                }
                if (itemTier == 4) {
                    return Items.diamond_chestplate;
                }
            }
            case 2: {
                if (itemTier == 0) {
                    return Items.leather_leggings;
                }
                if (itemTier == 1) {
                    return Items.golden_leggings;
                }
                if (itemTier == 2) {
                    return Items.chainmail_leggings;
                }
                if (itemTier == 3) {
                    return Items.iron_leggings;
                }
                if (itemTier == 4) {
                    return Items.diamond_leggings;
                }
            }
            case 1: {
                if (itemTier == 0) {
                    return Items.leather_boots;
                }
                if (itemTier == 1) {
                    return Items.golden_boots;
                }
                if (itemTier == 2) {
                    return Items.chainmail_boots;
                }
                if (itemTier == 3) {
                    return Items.iron_boots;
                }
                if (itemTier == 4) {
                    return Items.diamond_boots;
                }
                break;
            }
        }
        return null;
    }
    
    protected void func_180483_b(final DifficultyInstance p_180483_1_) {
        final float var2 = p_180483_1_.func_180170_c();
        if (this.getHeldItem() != null && this.rand.nextFloat() < 0.25f * var2) {
            EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItem(), (int)(5.0f + var2 * this.rand.nextInt(18)));
        }
        for (int var3 = 0; var3 < 4; ++var3) {
            final ItemStack var4 = this.getCurrentArmor(var3);
            if (var4 != null && this.rand.nextFloat() < 0.5f * var2) {
                EnchantmentHelper.addRandomEnchantment(this.rand, var4, (int)(5.0f + var2 * this.rand.nextInt(18)));
            }
        }
    }
    
    public IEntityLivingData func_180482_a(final DifficultyInstance p_180482_1_, final IEntityLivingData p_180482_2_) {
        this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05, 1));
        return p_180482_2_;
    }
    
    public boolean canBeSteered() {
        return false;
    }
    
    public void enablePersistence() {
        this.persistenceRequired = true;
    }
    
    public void setEquipmentDropChance(final int p_96120_1_, final float p_96120_2_) {
        this.equipmentDropChances[p_96120_1_] = p_96120_2_;
    }
    
    public boolean canPickUpLoot() {
        return this.canPickUpLoot;
    }
    
    public void setCanPickUpLoot(final boolean p_98053_1_) {
        this.canPickUpLoot = p_98053_1_;
    }
    
    public boolean isNoDespawnRequired() {
        return this.persistenceRequired;
    }
    
    @Override
    public final boolean interactFirst(final EntityPlayer playerIn) {
        if (this.getLeashed() && this.getLeashedToEntity() == playerIn) {
            this.clearLeashed(true, !playerIn.capabilities.isCreativeMode);
            return true;
        }
        final ItemStack var2 = playerIn.inventory.getCurrentItem();
        if (var2 != null && var2.getItem() == Items.lead && this.allowLeashing()) {
            if (!(this instanceof EntityTameable) || !((EntityTameable)this).isTamed()) {
                this.setLeashedToEntity(playerIn, true);
                final ItemStack itemStack = var2;
                --itemStack.stackSize;
                return true;
            }
            if (((EntityTameable)this).func_152114_e(playerIn)) {
                this.setLeashedToEntity(playerIn, true);
                final ItemStack itemStack2 = var2;
                --itemStack2.stackSize;
                return true;
            }
        }
        return this.interact(playerIn) || super.interactFirst(playerIn);
    }
    
    protected boolean interact(final EntityPlayer player) {
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
            if (this.leashedToEntity == null || this.leashedToEntity.isDead) {
                this.clearLeashed(true, true);
            }
        }
    }
    
    public void clearLeashed(final boolean p_110160_1_, final boolean p_110160_2_) {
        if (this.isLeashed) {
            this.isLeashed = false;
            this.leashedToEntity = null;
            if (!this.worldObj.isRemote && p_110160_2_) {
                this.dropItem(Items.lead, 1);
            }
            if (!this.worldObj.isRemote && p_110160_1_ && this.worldObj instanceof WorldServer) {
                ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S1BPacketEntityAttach(1, this, null));
            }
        }
    }
    
    public boolean allowLeashing() {
        return !this.getLeashed() && !(this instanceof IMob);
    }
    
    public boolean getLeashed() {
        return this.isLeashed;
    }
    
    public Entity getLeashedToEntity() {
        return this.leashedToEntity;
    }
    
    public void setLeashedToEntity(final Entity entityIn, final boolean sendAttachNotification) {
        this.isLeashed = true;
        this.leashedToEntity = entityIn;
        if (!this.worldObj.isRemote && sendAttachNotification && this.worldObj instanceof WorldServer) {
            ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S1BPacketEntityAttach(1, this, this.leashedToEntity));
        }
    }
    
    private void recreateLeash() {
        if (this.isLeashed && this.leashNBTTag != null) {
            if (this.leashNBTTag.hasKey("UUIDMost", 4) && this.leashNBTTag.hasKey("UUIDLeast", 4)) {
                final UUID var11 = new UUID(this.leashNBTTag.getLong("UUIDMost"), this.leashNBTTag.getLong("UUIDLeast"));
                final List var12 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(10.0, 10.0, 10.0));
                for (final EntityLivingBase var14 : var12) {
                    if (var14.getUniqueID().equals(var11)) {
                        this.leashedToEntity = var14;
                        break;
                    }
                }
            }
            else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99) && this.leashNBTTag.hasKey("Z", 99)) {
                final BlockPos var15 = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
                EntityLeashKnot var16 = EntityLeashKnot.func_174863_b(this.worldObj, var15);
                if (var16 == null) {
                    var16 = EntityLeashKnot.func_174862_a(this.worldObj, var15);
                }
                this.leashedToEntity = var16;
            }
            else {
                this.clearLeashed(false, true);
            }
        }
        this.leashNBTTag = null;
    }
    
    @Override
    public boolean func_174820_d(final int p_174820_1_, final ItemStack p_174820_2_) {
        int var3;
        if (p_174820_1_ == 99) {
            var3 = 0;
        }
        else {
            var3 = p_174820_1_ - 100 + 1;
            if (var3 < 0 || var3 >= this.equipment.length) {
                return false;
            }
        }
        if (p_174820_2_ != null && getArmorPosition(p_174820_2_) != var3 && (var3 != 4 || !(p_174820_2_.getItem() instanceof ItemBlock))) {
            return false;
        }
        this.setCurrentItemOrArmor(var3, p_174820_2_);
        return true;
    }
    
    @Override
    public boolean isServerWorld() {
        return super.isServerWorld() && !this.isAIDisabled();
    }
    
    protected void setNoAI(final boolean p_94061_1_) {
        this.dataWatcher.updateObject(15, (byte)(byte)(p_94061_1_ ? 1 : 0));
    }
    
    private boolean isAIDisabled() {
        return this.dataWatcher.getWatchableObjectByte(15) != 0;
    }
    
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip) {
            return false;
        }
        final BlockPosM posM = new BlockPosM(0, 0, 0);
        for (int var1 = 0; var1 < 8; ++var1) {
            final double var2 = this.posX + ((var1 >> 0) % 2 - 0.5f) * this.width * 0.8f;
            final double var3 = this.posY + ((var1 >> 1) % 2 - 0.5f) * 0.1f;
            final double var4 = this.posZ + ((var1 >> 2) % 2 - 0.5f) * this.width * 0.8f;
            posM.setXyz(var2, var3 + this.getEyeHeight(), var4);
            if (this.worldObj.getBlockState(posM).getBlock().isVisuallyOpaque()) {
                return true;
            }
        }
        return false;
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
        final Minecraft mc = Config.getMinecraft();
        final WorldClient world = mc.theWorld;
        if (world == null) {
            return false;
        }
        if (world.playerEntities.size() != 1) {
            return false;
        }
        final Entity player = world.playerEntities.get(0);
        final double dx = Math.abs(this.posX - player.posX) - 16.0;
        final double dz = Math.abs(this.posZ - player.posZ) - 16.0;
        final double distSq = dx * dx + dz * dz;
        return !this.isInRangeToRenderDist(distSq);
    }
    
    private void onUpdateMinimal() {
        ++this.entityAge;
        if (this instanceof EntityMob) {
            final float brightness = this.getBrightness(1.0f);
            if (brightness > 0.5f) {
                this.entityAge += 2;
            }
        }
        this.despawnEntity();
    }
    
    public enum SpawnPlacementType
    {
        ON_GROUND("ON_GROUND", 0, "ON_GROUND", 0), 
        IN_AIR("IN_AIR", 1, "IN_AIR", 1), 
        IN_WATER("IN_WATER", 2, "IN_WATER", 2);
        
        private static final SpawnPlacementType[] $VALUES;
        private static final String __OBFID = "CL_00002255";
        private static final SpawnPlacementType[] $VALUES$;
        
        private SpawnPlacementType(final String p_i46429_1_, final int p_i46429_2_, final String p_i45893_1_, final int p_i45893_2_) {
        }
        
        static {
            $VALUES = new SpawnPlacementType[] { SpawnPlacementType.ON_GROUND, SpawnPlacementType.IN_AIR, SpawnPlacementType.IN_WATER };
            $VALUES$ = new SpawnPlacementType[] { SpawnPlacementType.ON_GROUND, SpawnPlacementType.IN_AIR, SpawnPlacementType.IN_WATER };
        }
    }
}
