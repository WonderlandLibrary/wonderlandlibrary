/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityZombie
extends EntityMob {
    private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor(this);
    private static final AttributeModifier babySpeedBoostModifier;
    private int conversionTime;
    private boolean isBreakDoorsTaskSet = false;
    private float zombieWidth = -1.0f;
    protected static final IAttribute reinforcementChance;
    private float zombieHeight;
    private static final UUID babySpeedBoostUUID;

    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild()) {
            float f = this.getBrightness(1.0f);
            BlockPos blockPos = new BlockPos(this.posX, Math.round(this.posY), this.posZ);
            if (f > 0.5f && this.rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f && this.worldObj.canSeeSky(blockPos)) {
                boolean bl = true;
                ItemStack itemStack = this.getEquipmentInSlot(4);
                if (itemStack != null) {
                    if (itemStack.isItemStackDamageable()) {
                        itemStack.setItemDamage(itemStack.getItemDamage() + this.rand.nextInt(2));
                        if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) {
                            this.renderBrokenItemStack(itemStack);
                            this.setCurrentItemOrArmor(4, null);
                        }
                    }
                    bl = false;
                }
                if (bl) {
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
    protected String getLivingSound() {
        return "mob.zombie.say";
    }

    @Override
    protected Item getDropItem() {
        return Items.rotten_flesh;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        if (this.isChild()) {
            nBTTagCompound.setBoolean("IsBaby", true);
        }
        if (this.isVillager()) {
            nBTTagCompound.setBoolean("IsVillager", true);
        }
        nBTTagCompound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        nBTTagCompound.setBoolean("CanBreakDoors", this.isBreakDoorsTaskSet());
    }

    protected int getConversionTimeBoost() {
        int n = 1;
        if (this.rand.nextFloat() < 0.01f) {
            int n2 = 0;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int n3 = (int)this.posX - 4;
            while (n3 < (int)this.posX + 4 && n2 < 14) {
                int n4 = (int)this.posY - 4;
                while (n4 < (int)this.posY + 4 && n2 < 14) {
                    int n5 = (int)this.posZ - 4;
                    while (n5 < (int)this.posZ + 4 && n2 < 14) {
                        Block block = this.worldObj.getBlockState(mutableBlockPos.func_181079_c(n3, n4, n5)).getBlock();
                        if (block == Blocks.iron_bars || block == Blocks.bed) {
                            if (this.rand.nextFloat() < 0.3f) {
                                ++n;
                            }
                            ++n2;
                        }
                        ++n5;
                    }
                    ++n4;
                }
                ++n3;
            }
        }
        return n;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected void startConversion(int n) {
        this.conversionTime = n;
        this.getDataWatcher().updateObject(14, (byte)1);
        this.removePotionEffect(Potion.weakness.id);
        this.addPotionEffect(new PotionEffect(Potion.damageBoost.id, n, Math.min(this.worldObj.getDifficulty().getDifficultyId() - 1, 0)));
        this.worldObj.setEntityState(this, (byte)16);
    }

    @Override
    public double getYOffset() {
        return this.isChild() ? 0.0 : -0.35;
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 16) {
            if (!this.isSilent()) {
                this.worldObj.playSound(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "mob.zombie.remedy", 1.0f + this.rand.nextFloat(), this.rand.nextFloat() * 0.7f + 0.3f, false);
            }
        } else {
            super.handleStatusUpdate(by);
        }
    }

    @Override
    protected final void setSize(float f, float f2) {
        boolean bl = this.zombieWidth > 0.0f && this.zombieHeight > 0.0f;
        this.zombieWidth = f;
        this.zombieHeight = f2;
        if (!bl) {
            this.multiplySize(1.0f);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (super.attackEntityFrom(damageSource, f)) {
            EntityLivingBase entityLivingBase = this.getAttackTarget();
            if (entityLivingBase == null && damageSource.getEntity() instanceof EntityLivingBase) {
                entityLivingBase = (EntityLivingBase)damageSource.getEntity();
            }
            if (entityLivingBase != null && this.worldObj.getDifficulty() == EnumDifficulty.HARD && (double)this.rand.nextFloat() < this.getEntityAttribute(reinforcementChance).getAttributeValue()) {
                int n = MathHelper.floor_double(this.posX);
                int n2 = MathHelper.floor_double(this.posY);
                int n3 = MathHelper.floor_double(this.posZ);
                EntityZombie entityZombie = new EntityZombie(this.worldObj);
                int n4 = 0;
                while (n4 < 50) {
                    int n5;
                    int n6;
                    int n7 = n + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
                    if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(n7, (n6 = n2 + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1)) - 1, n5 = n3 + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1))) && this.worldObj.getLightFromNeighbors(new BlockPos(n7, n6, n5)) < 10) {
                        entityZombie.setPosition(n7, n6, n5);
                        if (!this.worldObj.isAnyPlayerWithinRangeAt(n7, n6, n5, 7.0) && this.worldObj.checkNoEntityCollision(entityZombie.getEntityBoundingBox(), entityZombie) && this.worldObj.getCollidingBoundingBoxes(entityZombie, entityZombie.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(entityZombie.getEntityBoundingBox())) {
                            this.worldObj.spawnEntityInWorld(entityZombie);
                            entityZombie.setAttackTarget(entityLivingBase);
                            entityZombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityZombie)), null);
                            this.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05f, 0));
                            entityZombie.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05f, 0));
                            break;
                        }
                    }
                    ++n4;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.getCurrentEquippedItem();
        if (itemStack != null && itemStack.getItem() == Items.golden_apple && itemStack.getMetadata() == 0 && this.isVillager() && this.isPotionActive(Potion.weakness)) {
            if (!entityPlayer.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
            if (itemStack.stackSize <= 0) {
                entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
            }
            if (!this.worldObj.isRemote) {
                this.startConversion(this.rand.nextInt(2401) + 3600);
            }
            return true;
        }
        return false;
    }

    public void setChild(boolean bl) {
        this.getDataWatcher().updateObject(12, (byte)(bl ? 1 : 0));
        if (this.worldObj != null && !this.worldObj.isRemote) {
            IAttributeInstance iAttributeInstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            iAttributeInstance.removeModifier(babySpeedBoostModifier);
            if (bl) {
                iAttributeInstance.applyModifier(babySpeedBoostModifier);
            }
        }
        this.setChildSize(bl);
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        this.playSound("mob.zombie.step", 0.15f, 1.0f);
    }

    @Override
    protected boolean canDespawn() {
        return !this.isConverting();
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        super.setEquipmentBasedOnDifficulty(difficultyInstance);
        float f = this.rand.nextFloat();
        float f2 = this.worldObj.getDifficulty() == EnumDifficulty.HARD ? 0.05f : 0.01f;
        if (f < f2) {
            int n = this.rand.nextInt(3);
            if (n == 0) {
                this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
            } else {
                this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
            }
        }
    }

    @Override
    protected String getHurtSound() {
        return "mob.zombie.hurt";
    }

    protected final void multiplySize(float f) {
        super.setSize(this.zombieWidth * f, this.zombieHeight * f);
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
        int n = super.getTotalArmorValue() + 2;
        if (n > 20) {
            n = 20;
        }
        return n;
    }

    @Override
    protected boolean func_175448_a(ItemStack itemStack) {
        return itemStack.getItem() == Items.egg && this.isChild() && this.isRiding() ? false : super.func_175448_a(itemStack);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        if (nBTTagCompound.getBoolean("IsBaby")) {
            this.setChild(true);
        }
        if (nBTTagCompound.getBoolean("IsVillager")) {
            this.setVillager(true);
        }
        if (nBTTagCompound.hasKey("ConversionTime", 99) && nBTTagCompound.getInteger("ConversionTime") > -1) {
            this.startConversion(nBTTagCompound.getInteger("ConversionTime"));
        }
        this.setBreakDoorsAItask(nBTTagCompound.getBoolean("CanBreakDoors"));
    }

    public boolean isBreakDoorsTaskSet() {
        return this.isBreakDoorsTaskSet;
    }

    public boolean isConverting() {
        return this.getDataWatcher().getWatchableObjectByte(14) == 1;
    }

    @Override
    protected void addRandomDrop() {
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
            }
        }
    }

    public boolean isVillager() {
        return this.getDataWatcher().getWatchableObjectByte(13) == 1;
    }

    public EntityZombie(World world) {
        super(world);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
        this.setSize(0.6f, 1.95f);
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyInstance, IEntityLivingData iEntityLivingData) {
        Object object;
        iEntityLivingData = super.onInitialSpawn(difficultyInstance, iEntityLivingData);
        float f = difficultyInstance.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55f * f);
        if (iEntityLivingData == null) {
            iEntityLivingData = new GroupData(this.worldObj.rand.nextFloat() < 0.05f, this.worldObj.rand.nextFloat() < 0.05f);
        }
        if (iEntityLivingData instanceof GroupData) {
            object = (GroupData)iEntityLivingData;
            if (((GroupData)object).isVillager) {
                this.setVillager(true);
            }
            if (((GroupData)object).isChild) {
                this.setChild(true);
                if ((double)this.worldObj.rand.nextFloat() < 0.05) {
                    List<Entity> list = this.worldObj.getEntitiesWithinAABB(EntityChicken.class, this.getEntityBoundingBox().expand(5.0, 3.0, 5.0), EntitySelectors.IS_STANDALONE);
                    if (!list.isEmpty()) {
                        EntityChicken entityChicken = (EntityChicken)list.get(0);
                        entityChicken.setChickenJockey(true);
                        this.mountEntity(entityChicken);
                    }
                } else if ((double)this.worldObj.rand.nextFloat() < 0.05) {
                    EntityChicken entityChicken = new EntityChicken(this.worldObj);
                    entityChicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
                    entityChicken.onInitialSpawn(difficultyInstance, null);
                    entityChicken.setChickenJockey(true);
                    this.worldObj.spawnEntityInWorld(entityChicken);
                    this.mountEntity(entityChicken);
                }
            }
        }
        this.setBreakDoorsAItask(this.rand.nextFloat() < f * 0.1f);
        this.setEquipmentBasedOnDifficulty(difficultyInstance);
        this.setEnchantmentBasedOnDifficulty(difficultyInstance);
        if (this.getEquipmentInSlot(4) == null && ((Calendar)(object = this.worldObj.getCurrentDate())).get(2) + 1 == 10 && ((Calendar)object).get(5) == 31 && this.rand.nextFloat() < 0.25f) {
            this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1f ? Blocks.lit_pumpkin : Blocks.pumpkin));
            this.equipmentDropChances[4] = 0.0f;
        }
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * (double)0.05f, 0));
        double d = this.rand.nextDouble() * 1.5 * (double)f;
        if (d > 1.0) {
            this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d, 2));
        }
        if (this.rand.nextFloat() < f * 0.05f) {
            this.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25 + 0.5, 0));
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0 + 1.0, 2));
            this.setBreakDoorsAItask(true);
        }
        return iEntityLivingData;
    }

    protected void convertToVillager() {
        EntityVillager entityVillager = new EntityVillager(this.worldObj);
        entityVillager.copyLocationAndAnglesFrom(this);
        entityVillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityVillager)), null);
        entityVillager.setLookingForHome();
        if (this.isChild()) {
            entityVillager.setGrowingAge(-24000);
        }
        this.worldObj.removeEntity(this);
        entityVillager.setNoAI(this.isAIDisabled());
        if (this.hasCustomName()) {
            entityVillager.setCustomNameTag(this.getCustomNameTag());
            entityVillager.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
        }
        this.worldObj.spawnEntityInWorld(entityVillager);
        entityVillager.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
        this.worldObj.playAuxSFXAtEntity(null, 1017, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
    }

    @Override
    public boolean isChild() {
        return this.getDataWatcher().getWatchableObjectByte(12) == 1;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (damageSource.getEntity() instanceof EntityCreeper && !(this instanceof EntityPigZombie) && ((EntityCreeper)damageSource.getEntity()).getPowered() && ((EntityCreeper)damageSource.getEntity()).isAIEnabled()) {
            ((EntityCreeper)damageSource.getEntity()).func_175493_co();
            this.entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0f);
        }
    }

    public void setVillager(boolean bl) {
        this.getDataWatcher().updateObject(13, (byte)(bl ? 1 : 0));
    }

    @Override
    public void onUpdate() {
        if (!this.worldObj.isRemote && this.isConverting()) {
            int n = this.getConversionTimeBoost();
            this.conversionTime -= n;
            if (this.conversionTime <= 0) {
                this.convertToVillager();
            }
        }
        super.onUpdate();
    }

    protected void applyEntityAI() {
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0, true));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityIronGolem.class, 1.0, true));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, true, EntityPigZombie.class));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>((EntityCreature)this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityVillager>((EntityCreature)this, EntityVillager.class, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityIronGolem>((EntityCreature)this, EntityIronGolem.class, true));
    }

    @Override
    public float getEyeHeight() {
        float f = 1.74f;
        if (this.isChild()) {
            f = (float)((double)f - 0.81);
        }
        return f;
    }

    @Override
    protected String getDeathSound() {
        return "mob.zombie.death";
    }

    @Override
    public void onKillEntity(EntityLivingBase entityLivingBase) {
        super.onKillEntity(entityLivingBase);
        if ((this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) && entityLivingBase instanceof EntityVillager) {
            if (this.worldObj.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean()) {
                return;
            }
            EntityLiving entityLiving = (EntityLiving)entityLivingBase;
            EntityZombie entityZombie = new EntityZombie(this.worldObj);
            entityZombie.copyLocationAndAnglesFrom(entityLivingBase);
            this.worldObj.removeEntity(entityLivingBase);
            entityZombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityZombie)), null);
            entityZombie.setVillager(true);
            if (entityLivingBase.isChild()) {
                entityZombie.setChild(true);
            }
            entityZombie.setNoAI(entityLiving.isAIDisabled());
            if (entityLiving.hasCustomName()) {
                entityZombie.setCustomNameTag(entityLiving.getCustomNameTag());
                entityZombie.setAlwaysRenderNameTag(entityLiving.getAlwaysRenderNameTag());
            }
            this.worldObj.spawnEntityInWorld(entityZombie);
            this.worldObj.playAuxSFXAtEntity(null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
        }
    }

    public void setBreakDoorsAItask(boolean bl) {
        if (this.isBreakDoorsTaskSet != bl) {
            this.isBreakDoorsTaskSet = bl;
            if (bl) {
                this.tasks.addTask(1, this.breakDoor);
            } else {
                this.tasks.removeTask(this.breakDoor);
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        boolean bl = super.attackEntityAsMob(entity);
        if (bl) {
            int n = this.worldObj.getDifficulty().getDifficultyId();
            if (this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < (float)n * 0.3f) {
                entity.setFire(2 * n);
            }
        }
        return bl;
    }

    static {
        reinforcementChance = new RangedAttribute(null, "zombie.spawnReinforcements", 0.0, 0.0, 1.0).setDescription("Spawn Reinforcements Chance");
        babySpeedBoostUUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
        babySpeedBoostModifier = new AttributeModifier(babySpeedBoostUUID, "Baby speed boost", 0.5, 1);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23f);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0);
        this.getAttributeMap().registerAttribute(reinforcementChance).setBaseValue(this.rand.nextDouble() * (double)0.1f);
    }

    public void setChildSize(boolean bl) {
        this.multiplySize(bl ? 0.5f : 1.0f);
    }

    @Override
    protected int getExperiencePoints(EntityPlayer entityPlayer) {
        if (this.isChild()) {
            this.experienceValue = (int)((float)this.experienceValue * 2.5f);
        }
        return super.getExperiencePoints(entityPlayer);
    }

    class GroupData
    implements IEntityLivingData {
        public boolean isVillager = false;
        public boolean isChild = false;

        private GroupData(boolean bl, boolean bl2) {
            this.isChild = bl;
            this.isVillager = bl2;
        }
    }
}

