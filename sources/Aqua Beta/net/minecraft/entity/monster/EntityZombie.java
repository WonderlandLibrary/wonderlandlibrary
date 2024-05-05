package net.minecraft.entity.monster;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
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
import net.minecraft.entity.ai.EntityAIBase;
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
import net.minecraft.entity.monster.EntityZombie;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityZombie
extends EntityMob {
    protected static final IAttribute reinforcementChance = new RangedAttribute((IAttribute)null, "zombie.spawnReinforcements", 0.0, 0.0, 1.0).setDescription("Spawn Reinforcements Chance");
    private static final UUID babySpeedBoostUUID = UUID.fromString((String)"B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier babySpeedBoostModifier = new AttributeModifier(babySpeedBoostUUID, "Baby speed boost", 0.5, 1);
    private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor((EntityLiving)this);
    private int conversionTime;
    private boolean isBreakDoorsTaskSet = false;
    private float zombieWidth = -1.0f;
    private float zombieHeight;

    public EntityZombie(World worldIn) {
        super(worldIn);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackOnCollide((EntityCreature)this, EntityPlayer.class, 1.0, false));
        this.tasks.addTask(5, (EntityAIBase)new EntityAIMoveTowardsRestriction((EntityCreature)this, 1.0));
        this.tasks.addTask(7, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0));
        this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.applyEntityAI();
        this.setSize(0.6f, 1.95f);
    }

    protected void applyEntityAI() {
        this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide((EntityCreature)this, EntityVillager.class, 1.0, true));
        this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide((EntityCreature)this, EntityIronGolem.class, 1.0, true));
        this.tasks.addTask(6, (EntityAIBase)new EntityAIMoveThroughVillage((EntityCreature)this, 1.0, false));
        this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, true, new Class[]{EntityPigZombie.class}));
        this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityVillager.class, false));
        this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityIronGolem.class, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)0.23f);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0);
        this.getAttributeMap().registerAttribute(reinforcementChance).setBaseValue(this.rand.nextDouble() * (double)0.1f);
    }

    protected void entityInit() {
        super.entityInit();
        this.getDataWatcher().addObject(12, (Object)0);
        this.getDataWatcher().addObject(13, (Object)0);
        this.getDataWatcher().addObject(14, (Object)0);
    }

    public int getTotalArmorValue() {
        int i = super.getTotalArmorValue() + 2;
        if (i > 20) {
            i = 20;
        }
        return i;
    }

    public boolean isBreakDoorsTaskSet() {
        return this.isBreakDoorsTaskSet;
    }

    public void setBreakDoorsAItask(boolean par1) {
        if (this.isBreakDoorsTaskSet != par1) {
            this.isBreakDoorsTaskSet = par1;
            if (par1) {
                this.tasks.addTask(1, (EntityAIBase)this.breakDoor);
            } else {
                this.tasks.removeTask((EntityAIBase)this.breakDoor);
            }
        }
    }

    public boolean isChild() {
        return this.getDataWatcher().getWatchableObjectByte(12) == 1;
    }

    protected int getExperiencePoints(EntityPlayer player) {
        if (this.isChild()) {
            this.experienceValue = (int)((float)this.experienceValue * 2.5f);
        }
        return super.getExperiencePoints(player);
    }

    public void setChild(boolean childZombie) {
        this.getDataWatcher().updateObject(12, (Object)((byte)(childZombie ? 1 : 0)));
        if (this.worldObj != null && !this.worldObj.isRemote) {
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            iattributeinstance.removeModifier(babySpeedBoostModifier);
            if (childZombie) {
                iattributeinstance.applyModifier(babySpeedBoostModifier);
            }
        }
        this.setChildSize(childZombie);
    }

    public boolean isVillager() {
        return this.getDataWatcher().getWatchableObjectByte(13) == 1;
    }

    public void setVillager(boolean villager) {
        this.getDataWatcher().updateObject(13, (Object)((byte)(villager ? 1 : 0)));
    }

    public void onLivingUpdate() {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild()) {
            float f = this.getBrightness(1.0f);
            BlockPos blockpos = new BlockPos(this.posX, (double)Math.round((double)this.posY), this.posZ);
            if (f > 0.5f && this.rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f && this.worldObj.canSeeSky(blockpos)) {
                boolean flag = true;
                ItemStack itemstack = this.getEquipmentInSlot(4);
                if (itemstack != null) {
                    if (itemstack.isItemStackDamageable()) {
                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
                            this.renderBrokenItemStack(itemstack);
                            this.setCurrentItemOrArmor(4, null);
                        }
                    }
                    flag = false;
                }
                if (flag) {
                    this.setFire(8);
                }
            }
        }
        if (this.isRiding() && this.getAttackTarget() != null && this.ridingEntity instanceof EntityChicken) {
            ((EntityLiving)this.ridingEntity).getNavigator().setPath(this.getNavigator().getPath(), 1.5);
        }
        super.onLivingUpdate();
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (super.attackEntityFrom(source, amount)) {
            EntityLivingBase entitylivingbase = this.getAttackTarget();
            if (entitylivingbase == null && source.getEntity() instanceof EntityLivingBase) {
                entitylivingbase = (EntityLivingBase)source.getEntity();
            }
            if (entitylivingbase != null && this.worldObj.getDifficulty() == EnumDifficulty.HARD && (double)this.rand.nextFloat() < this.getEntityAttribute(reinforcementChance).getAttributeValue()) {
                int i = MathHelper.floor_double((double)this.posX);
                int j = MathHelper.floor_double((double)this.posY);
                int k = MathHelper.floor_double((double)this.posZ);
                EntityZombie entityzombie = new EntityZombie(this.worldObj);
                for (int l = 0; l < 50; ++l) {
                    int k1;
                    int j1;
                    int i1 = i + MathHelper.getRandomIntegerInRange((Random)this.rand, (int)7, (int)40) * MathHelper.getRandomIntegerInRange((Random)this.rand, (int)-1, (int)1);
                    if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)this.worldObj, (BlockPos)new BlockPos(i1, (j1 = j + MathHelper.getRandomIntegerInRange((Random)this.rand, (int)7, (int)40) * MathHelper.getRandomIntegerInRange((Random)this.rand, (int)-1, (int)1)) - 1, k1 = k + MathHelper.getRandomIntegerInRange((Random)this.rand, (int)7, (int)40) * MathHelper.getRandomIntegerInRange((Random)this.rand, (int)-1, (int)1))) || this.worldObj.getLightFromNeighbors(new BlockPos(i1, j1, k1)) >= 10) continue;
                    entityzombie.setPosition(i1, j1, k1);
                    if (this.worldObj.isAnyPlayerWithinRangeAt((double)i1, (double)j1, (double)k1, 7.0) || !this.worldObj.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), (Entity)entityzombie) || !this.worldObj.getCollidingBoundingBoxes((Entity)entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() || this.worldObj.isAnyLiquid(entityzombie.getEntityBoundingBox())) continue;
                    this.worldObj.spawnEntityInWorld((Entity)entityzombie);
                    entityzombie.setAttackTarget(entitylivingbase);
                    entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entityzombie)), null);
                    this.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", (double)-0.05f, 0));
                    entityzombie.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", (double)-0.05f, 0));
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public void onUpdate() {
        if (!this.worldObj.isRemote && this.isConverting()) {
            int i = this.getConversionTimeBoost();
            this.conversionTime -= i;
            if (this.conversionTime <= 0) {
                this.convertToVillager();
            }
        }
        super.onUpdate();
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag) {
            int i = this.worldObj.getDifficulty().getDifficultyId();
            if (this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < (float)i * 0.3f) {
                entityIn.setFire(2 * i);
            }
        }
        return flag;
    }

    protected String getLivingSound() {
        return "mob.zombie.say";
    }

    protected String getHurtSound() {
        return "mob.zombie.hurt";
    }

    protected String getDeathSound() {
        return "mob.zombie.death";
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound("mob.zombie.step", 0.15f, 1.0f);
    }

    protected Item getDropItem() {
        return Items.rotten_flesh;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

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

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        float f = this.rand.nextFloat();
        float f2 = this.worldObj.getDifficulty() == EnumDifficulty.HARD ? 0.05f : 0.01f;
        if (f < f2) {
            int i = this.rand.nextInt(3);
            if (i == 0) {
                this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
            } else {
                this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        if (this.isChild()) {
            tagCompound.setBoolean("IsBaby", true);
        }
        if (this.isVillager()) {
            tagCompound.setBoolean("IsVillager", true);
        }
        tagCompound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        tagCompound.setBoolean("CanBreakDoors", this.isBreakDoorsTaskSet());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
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
        this.setBreakDoorsAItask(tagCompund.getBoolean("CanBreakDoors"));
    }

    public void onKillEntity(EntityLivingBase entityLivingIn) {
        super.onKillEntity(entityLivingIn);
        if ((this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof EntityVillager) {
            if (this.worldObj.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean()) {
                return;
            }
            EntityLiving entityliving = (EntityLiving)entityLivingIn;
            EntityZombie entityzombie = new EntityZombie(this.worldObj);
            entityzombie.copyLocationAndAnglesFrom((Entity)entityLivingIn);
            this.worldObj.removeEntity((Entity)entityLivingIn);
            entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entityzombie)), null);
            entityzombie.setVillager(true);
            if (entityLivingIn.isChild()) {
                entityzombie.setChild(true);
            }
            entityzombie.setNoAI(entityliving.isAIDisabled());
            if (entityliving.hasCustomName()) {
                entityzombie.setCustomNameTag(entityliving.getCustomNameTag());
                entityzombie.setAlwaysRenderNameTag(entityliving.getAlwaysRenderNameTag());
            }
            this.worldObj.spawnEntityInWorld((Entity)entityzombie);
            this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
        }
    }

    public float getEyeHeight() {
        float f = 1.74f;
        if (this.isChild()) {
            f = (float)((double)f - 0.81);
        }
        return f;
    }

    protected boolean func_175448_a(ItemStack stack) {
        return stack.getItem() == Items.egg && this.isChild() && this.isRiding() ? false : super.func_175448_a(stack);
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        Calendar calendar;
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        float f = difficulty.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55f * f);
        if (livingdata == null) {
            livingdata = new GroupData(this, this.worldObj.rand.nextFloat() < 0.05f, this.worldObj.rand.nextFloat() < 0.05f, null);
        }
        if (livingdata instanceof GroupData) {
            GroupData entityzombie$groupdata = (GroupData)livingdata;
            if (entityzombie$groupdata.isVillager) {
                this.setVillager(true);
            }
            if (entityzombie$groupdata.isChild) {
                this.setChild(true);
                if ((double)this.worldObj.rand.nextFloat() < 0.05) {
                    List list = this.worldObj.getEntitiesWithinAABB(EntityChicken.class, this.getEntityBoundingBox().expand(5.0, 3.0, 5.0), EntitySelectors.IS_STANDALONE);
                    if (!list.isEmpty()) {
                        EntityChicken entitychicken = (EntityChicken)list.get(0);
                        entitychicken.setChickenJockey(true);
                        this.mountEntity((Entity)entitychicken);
                    }
                } else if ((double)this.worldObj.rand.nextFloat() < 0.05) {
                    EntityChicken entitychicken1 = new EntityChicken(this.worldObj);
                    entitychicken1.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
                    entitychicken1.onInitialSpawn(difficulty, (IEntityLivingData)null);
                    entitychicken1.setChickenJockey(true);
                    this.worldObj.spawnEntityInWorld((Entity)entitychicken1);
                    this.mountEntity((Entity)entitychicken1);
                }
            }
        }
        this.setBreakDoorsAItask(this.rand.nextFloat() < f * 0.1f);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        if (this.getEquipmentInSlot(4) == null && (calendar = this.worldObj.getCurrentDate()).get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25f) {
            this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1f ? Blocks.lit_pumpkin : Blocks.pumpkin));
            this.equipmentDropChances[4] = 0.0f;
        }
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * (double)0.05f, 0));
        double d0 = this.rand.nextDouble() * 1.5 * (double)f;
        if (d0 > 1.0) {
            this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }
        if (this.rand.nextFloat() < f * 0.05f) {
            this.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25 + 0.5, 0));
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0 + 1.0, 2));
            this.setBreakDoorsAItask(true);
        }
        return livingdata;
    }

    public boolean interact(EntityPlayer player) {
        ItemStack itemstack = player.getCurrentEquippedItem();
        if (itemstack != null && itemstack.getItem() == Items.golden_apple && itemstack.getMetadata() == 0 && this.isVillager() && this.isPotionActive(Potion.weakness)) {
            if (!player.capabilities.isCreativeMode) {
                --itemstack.stackSize;
            }
            if (itemstack.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            }
            if (!this.worldObj.isRemote) {
                this.startConversion(this.rand.nextInt(2401) + 3600);
            }
            return true;
        }
        return false;
    }

    protected void startConversion(int ticks) {
        this.conversionTime = ticks;
        this.getDataWatcher().updateObject(14, (Object)1);
        this.removePotionEffect(Potion.weakness.id);
        this.addPotionEffect(new PotionEffect(Potion.damageBoost.id, ticks, Math.min((int)(this.worldObj.getDifficulty().getDifficultyId() - 1), (int)0)));
        this.worldObj.setEntityState((Entity)this, (byte)16);
    }

    public void handleStatusUpdate(byte id) {
        if (id == 16) {
            if (!this.isSilent()) {
                this.worldObj.playSound(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "mob.zombie.remedy", 1.0f + this.rand.nextFloat(), this.rand.nextFloat() * 0.7f + 0.3f, false);
            }
        } else {
            super.handleStatusUpdate(id);
        }
    }

    protected boolean canDespawn() {
        return !this.isConverting();
    }

    public boolean isConverting() {
        return this.getDataWatcher().getWatchableObjectByte(14) == 1;
    }

    protected void convertToVillager() {
        EntityVillager entityvillager = new EntityVillager(this.worldObj);
        entityvillager.copyLocationAndAnglesFrom((Entity)this);
        entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entityvillager)), (IEntityLivingData)null);
        entityvillager.setLookingForHome();
        if (this.isChild()) {
            entityvillager.setGrowingAge(-24000);
        }
        this.worldObj.removeEntity((Entity)this);
        entityvillager.setNoAI(this.isAIDisabled());
        if (this.hasCustomName()) {
            entityvillager.setCustomNameTag(this.getCustomNameTag());
            entityvillager.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
        }
        this.worldObj.spawnEntityInWorld((Entity)entityvillager);
        entityvillager.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
        this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
    }

    protected int getConversionTimeBoost() {
        int i = 1;
        if (this.rand.nextFloat() < 0.01f) {
            int j = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k = (int)this.posX - 4; k < (int)this.posX + 4 && j < 14; ++k) {
                for (int l = (int)this.posY - 4; l < (int)this.posY + 4 && j < 14; ++l) {
                    for (int i1 = (int)this.posZ - 4; i1 < (int)this.posZ + 4 && j < 14; ++i1) {
                        Block block = this.worldObj.getBlockState((BlockPos)blockpos$mutableblockpos.set(k, l, i1)).getBlock();
                        if (block != Blocks.iron_bars && block != Blocks.bed) continue;
                        if (this.rand.nextFloat() < 0.3f) {
                            ++i;
                        }
                        ++j;
                    }
                }
            }
        }
        return i;
    }

    public void setChildSize(boolean isChild) {
        this.multiplySize(isChild ? 0.5f : 1.0f);
    }

    protected final void setSize(float width, float height) {
        boolean flag = this.zombieWidth > 0.0f && this.zombieHeight > 0.0f;
        this.zombieWidth = width;
        this.zombieHeight = height;
        if (!flag) {
            this.multiplySize(1.0f);
        }
    }

    protected final void multiplySize(float size) {
        super.setSize(this.zombieWidth * size, this.zombieHeight * size);
    }

    public double getYOffset() {
        return this.isChild() ? 0.0 : -0.35;
    }

    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (cause.getEntity() instanceof EntityCreeper && !(this instanceof EntityPigZombie) && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled()) {
            ((EntityCreeper)cause.getEntity()).func_175493_co();
            this.entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0f);
        }
    }
}
