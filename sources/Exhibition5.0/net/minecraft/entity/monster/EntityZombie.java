// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import java.util.Calendar;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.command.IEntitySelector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.IAttribute;

public class EntityZombie extends EntityMob
{
    protected static final IAttribute field_110186_bp;
    private static final UUID babySpeedBoostUUID;
    private static final AttributeModifier babySpeedBoostModifier;
    private final EntityAIBreakDoor field_146075_bs;
    private int conversionTime;
    private boolean field_146076_bu;
    private float field_146074_bv;
    private float field_146073_bw;
    private static final String __OBFID = "CL_00001702";
    
    public EntityZombie(final World worldIn) {
        super(worldIn);
        this.field_146075_bs = new EntityAIBreakDoor(this);
        this.field_146076_bu = false;
        this.field_146074_bv = -1.0f;
        ((PathNavigateGround)this.getNavigator()).func_179688_b(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0, false));
        this.tasks.addTask(2, this.field_175455_a);
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.func_175456_n();
        this.setSize(0.6f, 1.95f);
    }
    
    protected void func_175456_n() {
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0, true));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityIronGolem.class, 1.0, true));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0);
        this.getAttributeMap().registerAttribute(EntityZombie.field_110186_bp).setBaseValue(this.rand.nextDouble() * 0.10000000149011612);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataWatcher().addObject(12, (byte)0);
        this.getDataWatcher().addObject(13, (byte)0);
        this.getDataWatcher().addObject(14, (byte)0);
    }
    
    @Override
    public int getTotalArmorValue() {
        int var1 = super.getTotalArmorValue() + 2;
        if (var1 > 20) {
            var1 = 20;
        }
        return var1;
    }
    
    public boolean func_146072_bX() {
        return this.field_146076_bu;
    }
    
    public void func_146070_a(final boolean p_146070_1_) {
        if (this.field_146076_bu != p_146070_1_) {
            this.field_146076_bu = p_146070_1_;
            if (p_146070_1_) {
                this.tasks.addTask(1, this.field_146075_bs);
            }
            else {
                this.tasks.removeTask(this.field_146075_bs);
            }
        }
    }
    
    @Override
    public boolean isChild() {
        return this.getDataWatcher().getWatchableObjectByte(12) == 1;
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer p_70693_1_) {
        if (this.isChild()) {
            this.experienceValue *= (int)2.5f;
        }
        return super.getExperiencePoints(p_70693_1_);
    }
    
    public void setChild(final boolean p_82227_1_) {
        this.getDataWatcher().updateObject(12, (byte)(byte)(p_82227_1_ ? 1 : 0));
        if (this.worldObj != null && !this.worldObj.isRemote) {
            final IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            var2.removeModifier(EntityZombie.babySpeedBoostModifier);
            if (p_82227_1_) {
                var2.applyModifier(EntityZombie.babySpeedBoostModifier);
            }
        }
        this.func_146071_k(p_82227_1_);
    }
    
    public boolean isVillager() {
        return this.getDataWatcher().getWatchableObjectByte(13) == 1;
    }
    
    public void setVillager(final boolean p_82229_1_) {
        this.getDataWatcher().updateObject(13, (byte)(byte)(p_82229_1_ ? 1 : 0));
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild()) {
            final float var1 = this.getBrightness(1.0f);
            final BlockPos var2 = new BlockPos(this.posX, Math.round(this.posY), this.posZ);
            if (var1 > 0.5f && this.rand.nextFloat() * 30.0f < (var1 - 0.4f) * 2.0f && this.worldObj.isAgainstSky(var2)) {
                boolean var3 = true;
                final ItemStack var4 = this.getEquipmentInSlot(4);
                if (var4 != null) {
                    if (var4.isItemStackDamageable()) {
                        var4.setItemDamage(var4.getItemDamage() + this.rand.nextInt(2));
                        if (var4.getItemDamage() >= var4.getMaxDamage()) {
                            this.renderBrokenItemStack(var4);
                            this.setCurrentItemOrArmor(4, null);
                        }
                    }
                    var3 = false;
                }
                if (var3) {
                    this.setFire(8);
                }
            }
        }
        if (this.isRiding() && this.getAttackTarget() != null && this.ridingEntity instanceof EntityChicken) {
            ((EntityLiving)this.ridingEntity).getNavigator().setPath(this.getNavigator().getPath(), 1.5);
        }
        super.onLivingUpdate();
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (super.attackEntityFrom(source, amount)) {
            EntityLivingBase var3 = this.getAttackTarget();
            if (var3 == null && source.getEntity() instanceof EntityLivingBase) {
                var3 = (EntityLivingBase)source.getEntity();
            }
            if (var3 != null && this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.rand.nextFloat() < this.getEntityAttribute(EntityZombie.field_110186_bp).getAttributeValue()) {
                final int var4 = MathHelper.floor_double(this.posX);
                final int var5 = MathHelper.floor_double(this.posY);
                final int var6 = MathHelper.floor_double(this.posZ);
                final EntityZombie var7 = new EntityZombie(this.worldObj);
                for (int var8 = 0; var8 < 50; ++var8) {
                    final int var9 = var4 + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
                    final int var10 = var5 + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
                    final int var11 = var6 + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
                    if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(var9, var10 - 1, var11)) && this.worldObj.getLightFromNeighbors(new BlockPos(var9, var10, var11)) < 10) {
                        var7.setPosition(var9, var10, var11);
                        if (!this.worldObj.func_175636_b(var9, var10, var11, 7.0) && this.worldObj.checkNoEntityCollision(var7.getEntityBoundingBox(), var7) && this.worldObj.getCollidingBoundingBoxes(var7, var7.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(var7.getEntityBoundingBox())) {
                            this.worldObj.spawnEntityInWorld(var7);
                            var7.setAttackTarget(var3);
                            var7.func_180482_a(this.worldObj.getDifficultyForLocation(new BlockPos(var7)), null);
                            this.getEntityAttribute(EntityZombie.field_110186_bp).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806, 0));
                            var7.getEntityAttribute(EntityZombie.field_110186_bp).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806, 0));
                            break;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void onUpdate() {
        if (!this.worldObj.isRemote && this.isConverting()) {
            final int var1 = this.getConversionTimeBoost();
            this.conversionTime -= var1;
            if (this.conversionTime <= 0) {
                this.convertToVillager();
            }
        }
        super.onUpdate();
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity p_70652_1_) {
        final boolean var2 = super.attackEntityAsMob(p_70652_1_);
        if (var2) {
            final int var3 = this.worldObj.getDifficulty().getDifficultyId();
            if (this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < var3 * 0.3f) {
                p_70652_1_.setFire(2 * var3);
            }
        }
        return var2;
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.zombie.say";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.zombie.hurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.zombie.death";
    }
    
    @Override
    protected void func_180429_a(final BlockPos p_180429_1_, final Block p_180429_2_) {
        this.playSound("mob.zombie.step", 0.15f, 1.0f);
    }
    
    @Override
    protected Item getDropItem() {
        return Items.rotten_flesh;
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }
    
    @Override
    protected void addRandomArmor() {
        switch (this.rand.nextInt(3)) {
            case 0: {
                this.dropItem(Items.iron_ingot, 1);
                break;
            }
            case 1: {
                this.dropItem(Items.carrot, 1);
                break;
            }
            case 2: {
                this.dropItem(Items.potato, 1);
                break;
            }
        }
    }
    
    @Override
    protected void func_180481_a(final DifficultyInstance p_180481_1_) {
        super.func_180481_a(p_180481_1_);
        if (this.rand.nextFloat() < ((this.worldObj.getDifficulty() == EnumDifficulty.HARD) ? 0.05f : 0.01f)) {
            final int var2 = this.rand.nextInt(3);
            if (var2 == 0) {
                this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
            }
            else {
                this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
            }
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        if (this.isChild()) {
            tagCompound.setBoolean("IsBaby", true);
        }
        if (this.isVillager()) {
            tagCompound.setBoolean("IsVillager", true);
        }
        tagCompound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        tagCompound.setBoolean("CanBreakDoors", this.func_146072_bX());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.getBoolean("IsBaby")) {
            this.setChild(true);
        }
        if (tagCompund.getBoolean("IsVillager")) {
            this.setVillager(true);
        }
        if (tagCompund.hasKey("ConversionTime", 99) && tagCompund.getInteger("ConversionTime") > -1) {
            this.startConversion(tagCompund.getInteger("ConversionTime"));
        }
        this.func_146070_a(tagCompund.getBoolean("CanBreakDoors"));
    }
    
    @Override
    public void onKillEntity(final EntityLivingBase entityLivingIn) {
        super.onKillEntity(entityLivingIn);
        if ((this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof EntityVillager) {
            if (this.worldObj.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean()) {
                return;
            }
            final EntityZombie var2 = new EntityZombie(this.worldObj);
            var2.copyLocationAndAnglesFrom(entityLivingIn);
            this.worldObj.removeEntity(entityLivingIn);
            var2.func_180482_a(this.worldObj.getDifficultyForLocation(new BlockPos(var2)), null);
            var2.setVillager(true);
            if (entityLivingIn.isChild()) {
                var2.setChild(true);
            }
            this.worldObj.spawnEntityInWorld(var2);
            this.worldObj.playAuxSFXAtEntity(null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
        }
    }
    
    @Override
    public float getEyeHeight() {
        float var1 = 1.74f;
        if (this.isChild()) {
            var1 -= 0.81;
        }
        return var1;
    }
    
    @Override
    protected boolean func_175448_a(final ItemStack p_175448_1_) {
        return (p_175448_1_.getItem() != Items.egg || !this.isChild() || !this.isRiding()) && super.func_175448_a(p_175448_1_);
    }
    
    @Override
    public IEntityLivingData func_180482_a(final DifficultyInstance p_180482_1_, final IEntityLivingData p_180482_2_) {
        Object p_180482_2_2 = super.func_180482_a(p_180482_1_, p_180482_2_);
        final float var3 = p_180482_1_.func_180170_c();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55f * var3);
        if (p_180482_2_2 == null) {
            p_180482_2_2 = new GroupData(this.worldObj.rand.nextFloat() < 0.05f, this.worldObj.rand.nextFloat() < 0.05f, null);
        }
        if (p_180482_2_2 instanceof GroupData) {
            final GroupData var4 = (GroupData)p_180482_2_2;
            if (var4.field_142046_b) {
                this.setVillager(true);
            }
            if (var4.field_142048_a) {
                this.setChild(true);
                if (this.worldObj.rand.nextFloat() < 0.05) {
                    final List var5 = this.worldObj.func_175647_a(EntityChicken.class, this.getEntityBoundingBox().expand(5.0, 3.0, 5.0), IEntitySelector.field_152785_b);
                    if (!var5.isEmpty()) {
                        final EntityChicken var6 = var5.get(0);
                        var6.func_152117_i(true);
                        this.mountEntity(var6);
                    }
                }
                else if (this.worldObj.rand.nextFloat() < 0.05) {
                    final EntityChicken var7 = new EntityChicken(this.worldObj);
                    var7.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
                    var7.func_180482_a(p_180482_1_, null);
                    var7.func_152117_i(true);
                    this.worldObj.spawnEntityInWorld(var7);
                    this.mountEntity(var7);
                }
            }
        }
        this.func_146070_a(this.rand.nextFloat() < var3 * 0.1f);
        this.func_180481_a(p_180482_1_);
        this.func_180483_b(p_180482_1_);
        if (this.getEquipmentInSlot(4) == null) {
            final Calendar var8 = this.worldObj.getCurrentDate();
            if (var8.get(2) + 1 == 10 && var8.get(5) == 31 && this.rand.nextFloat() < 0.25f) {
                this.setCurrentItemOrArmor(4, new ItemStack((this.rand.nextFloat() < 0.1f) ? Blocks.lit_pumpkin : Blocks.pumpkin));
                this.equipmentDropChances[4] = 0.0f;
            }
        }
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806, 0));
        final double var9 = this.rand.nextDouble() * 1.5 * var3;
        if (var9 > 1.0) {
            this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random zombie-spawn bonus", var9, 2));
        }
        if (this.rand.nextFloat() < var3 * 0.05f) {
            this.getEntityAttribute(EntityZombie.field_110186_bp).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25 + 0.5, 0));
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0 + 1.0, 2));
            this.func_146070_a(true);
        }
        return (IEntityLivingData)p_180482_2_2;
    }
    
    public boolean interact(final EntityPlayer p_70085_1_) {
        final ItemStack var2 = p_70085_1_.getCurrentEquippedItem();
        if (var2 != null && var2.getItem() == Items.golden_apple && var2.getMetadata() == 0 && this.isVillager() && this.isPotionActive(Potion.weakness)) {
            if (!p_70085_1_.capabilities.isCreativeMode) {
                final ItemStack itemStack = var2;
                --itemStack.stackSize;
            }
            if (var2.stackSize <= 0) {
                p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, null);
            }
            if (!this.worldObj.isRemote) {
                this.startConversion(this.rand.nextInt(2401) + 3600);
            }
            return true;
        }
        return false;
    }
    
    protected void startConversion(final int p_82228_1_) {
        this.conversionTime = p_82228_1_;
        this.getDataWatcher().updateObject(14, (byte)1);
        this.removePotionEffect(Potion.weakness.id);
        this.addPotionEffect(new PotionEffect(Potion.damageBoost.id, p_82228_1_, Math.min(this.worldObj.getDifficulty().getDifficultyId() - 1, 0)));
        this.worldObj.setEntityState(this, (byte)16);
    }
    
    @Override
    public void handleHealthUpdate(final byte p_70103_1_) {
        if (p_70103_1_ == 16) {
            if (!this.isSlient()) {
                this.worldObj.playSound(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "mob.zombie.remedy", 1.0f + this.rand.nextFloat(), this.rand.nextFloat() * 0.7f + 0.3f, false);
            }
        }
        else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }
    
    @Override
    protected boolean canDespawn() {
        return !this.isConverting();
    }
    
    public boolean isConverting() {
        return this.getDataWatcher().getWatchableObjectByte(14) == 1;
    }
    
    protected void convertToVillager() {
        final EntityVillager var1 = new EntityVillager(this.worldObj);
        var1.copyLocationAndAnglesFrom(this);
        var1.func_180482_a(this.worldObj.getDifficultyForLocation(new BlockPos(var1)), null);
        var1.setLookingForHome();
        if (this.isChild()) {
            var1.setGrowingAge(-24000);
        }
        this.worldObj.removeEntity(this);
        this.worldObj.spawnEntityInWorld(var1);
        var1.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
        this.worldObj.playAuxSFXAtEntity(null, 1017, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
    }
    
    protected int getConversionTimeBoost() {
        int var1 = 1;
        if (this.rand.nextFloat() < 0.01f) {
            for (int var2 = 0, var3 = (int)this.posX - 4; var3 < (int)this.posX + 4 && var2 < 14; ++var3) {
                for (int var4 = (int)this.posY - 4; var4 < (int)this.posY + 4 && var2 < 14; ++var4) {
                    for (int var5 = (int)this.posZ - 4; var5 < (int)this.posZ + 4 && var2 < 14; ++var5) {
                        final Block var6 = this.worldObj.getBlockState(new BlockPos(var3, var4, var5)).getBlock();
                        if (var6 == Blocks.iron_bars || var6 == Blocks.bed) {
                            if (this.rand.nextFloat() < 0.3f) {
                                ++var1;
                            }
                            ++var2;
                        }
                    }
                }
            }
        }
        return var1;
    }
    
    public void func_146071_k(final boolean p_146071_1_) {
        this.func_146069_a(p_146071_1_ ? 0.5f : 1.0f);
    }
    
    @Override
    protected final void setSize(final float width, final float height) {
        final boolean var3 = this.field_146074_bv > 0.0f && this.field_146073_bw > 0.0f;
        this.field_146074_bv = width;
        this.field_146073_bw = height;
        if (!var3) {
            this.func_146069_a(1.0f);
        }
    }
    
    protected final void func_146069_a(final float p_146069_1_) {
        super.setSize(this.field_146074_bv * p_146069_1_, this.field_146073_bw * p_146069_1_);
    }
    
    @Override
    public double getYOffset() {
        return super.getYOffset() - 0.5;
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        if (cause.getEntity() instanceof EntityCreeper && !(this instanceof EntityPigZombie) && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled()) {
            ((EntityCreeper)cause.getEntity()).func_175493_co();
            this.entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0f);
        }
    }
    
    static {
        field_110186_bp = new RangedAttribute(null, "zombie.spawnReinforcements", 0.0, 0.0, 1.0).setDescription("Spawn Reinforcements Chance");
        babySpeedBoostUUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
        babySpeedBoostModifier = new AttributeModifier(EntityZombie.babySpeedBoostUUID, "Baby speed boost", 0.5, 1);
    }
    
    class GroupData implements IEntityLivingData
    {
        public boolean field_142048_a;
        public boolean field_142046_b;
        private static final String __OBFID = "CL_00001704";
        
        private GroupData(final boolean p_i2348_2_, final boolean p_i2348_3_) {
            this.field_142048_a = false;
            this.field_142046_b = false;
            this.field_142048_a = p_i2348_2_;
            this.field_142046_b = p_i2348_3_;
        }
        
        GroupData(final EntityZombie this$0, final boolean p_i2349_2_, final boolean p_i2349_3_, final Object p_i2349_4_) {
            this(this$0, p_i2349_2_, p_i2349_3_);
        }
    }
}
