/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityHorse
extends EntityAnimal
implements IInvBasic {
    private static final Predicate horseBreedingSelector = new Predicate(){
        private static final String __OBFID = "CL_00001642";

        public boolean func_179873_a(Entity p_179873_1_) {
            return p_179873_1_ instanceof EntityHorse && ((EntityHorse)p_179873_1_).func_110205_ce();
        }

        public boolean apply(Object p_apply_1_) {
            return this.func_179873_a((Entity)p_apply_1_);
        }
    };
    private static final IAttribute horseJumpStrength = new RangedAttribute(null, "horse.jumpStrength", 0.7, 0.0, 2.0).setDescription("Jump Strength").setShouldWatch(true);
    private static final String[] horseArmorTextures;
    private static final String[] field_110273_bx;
    private static final int[] armorValues;
    private static final String[] horseTextures;
    private static final String[] field_110269_bA;
    private static final String[] horseMarkingTextures;
    private static final String[] field_110292_bC;
    private int eatingHaystackCounter;
    private int openMouthCounter;
    private int jumpRearingCounter;
    public int field_110278_bp;
    public int field_110279_bq;
    protected boolean horseJumping;
    private AnimalChest horseChest;
    private boolean hasReproduced;
    protected int temper;
    protected float jumpPower;
    private boolean field_110294_bI;
    private float headLean;
    private float prevHeadLean;
    private float rearingAmount;
    private float prevRearingAmount;
    private float mouthOpenness;
    private float prevMouthOpenness;
    private int gallopTime;
    private String field_110286_bQ;
    private String[] field_110280_bR = new String[3];
    private boolean field_175508_bO = false;
    private static final String __OBFID = "CL_00001641";

    static {
        String[] arrstring = new String[4];
        arrstring[1] = "textures/entity/horse/armor/horse_armor_iron.png";
        arrstring[2] = "textures/entity/horse/armor/horse_armor_gold.png";
        arrstring[3] = "textures/entity/horse/armor/horse_armor_diamond.png";
        horseArmorTextures = arrstring;
        field_110273_bx = new String[]{"", "meo", "goo", "dio"};
        int[] arrn = new int[4];
        arrn[1] = 5;
        arrn[2] = 7;
        arrn[3] = 11;
        armorValues = arrn;
        horseTextures = new String[]{"textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png"};
        field_110269_bA = new String[]{"hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb"};
        String[] arrstring2 = new String[5];
        arrstring2[1] = "textures/entity/horse/horse_markings_white.png";
        arrstring2[2] = "textures/entity/horse/horse_markings_whitefield.png";
        arrstring2[3] = "textures/entity/horse/horse_markings_whitedots.png";
        arrstring2[4] = "textures/entity/horse/horse_markings_blackdots.png";
        horseMarkingTextures = arrstring2;
        field_110292_bC = new String[]{"", "wo_", "wmo", "wdo", "bdo"};
    }

    public EntityHorse(World worldIn) {
        super(worldIn);
        this.setSize(1.4f, 1.6f);
        this.isImmuneToFire = false;
        this.setChested(false);
        ((PathNavigateGround)this.getNavigator()).func_179690_a(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.2));
        this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.0));
        this.tasks.addTask(6, new EntityAIWander(this, 0.7));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.func_110226_cD();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, 0);
        this.dataWatcher.addObject(19, (byte)0);
        this.dataWatcher.addObject(20, 0);
        this.dataWatcher.addObject(21, String.valueOf(""));
        this.dataWatcher.addObject(22, 0);
    }

    public void setHorseType(int p_110214_1_) {
        this.dataWatcher.updateObject(19, (byte)p_110214_1_);
        this.func_110230_cF();
    }

    public int getHorseType() {
        return this.dataWatcher.getWatchableObjectByte(19);
    }

    public void setHorseVariant(int p_110235_1_) {
        this.dataWatcher.updateObject(20, p_110235_1_);
        this.func_110230_cF();
    }

    public int getHorseVariant() {
        return this.dataWatcher.getWatchableObjectInt(20);
    }

    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        int var1 = this.getHorseType();
        switch (var1) {
            default: {
                return StatCollector.translateToLocal("entity.horse.name");
            }
            case 1: {
                return StatCollector.translateToLocal("entity.donkey.name");
            }
            case 2: {
                return StatCollector.translateToLocal("entity.mule.name");
            }
            case 3: {
                return StatCollector.translateToLocal("entity.zombiehorse.name");
            }
            case 4: 
        }
        return StatCollector.translateToLocal("entity.skeletonhorse.name");
    }

    private boolean getHorseWatchableBoolean(int p_110233_1_) {
        return (this.dataWatcher.getWatchableObjectInt(16) & p_110233_1_) != 0;
    }

    private void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_) {
        int var3 = this.dataWatcher.getWatchableObjectInt(16);
        if (p_110208_2_) {
            this.dataWatcher.updateObject(16, var3 | p_110208_1_);
        } else {
            this.dataWatcher.updateObject(16, var3 & ~p_110208_1_);
        }
    }

    public boolean isAdultHorse() {
        return !this.isChild();
    }

    public boolean isTame() {
        return this.getHorseWatchableBoolean(2);
    }

    public boolean func_110253_bW() {
        return this.isAdultHorse();
    }

    public String func_152119_ch() {
        return this.dataWatcher.getWatchableObjectString(21);
    }

    public void func_152120_b(String p_152120_1_) {
        this.dataWatcher.updateObject(21, p_152120_1_);
    }

    public float getHorseSize() {
        int var1 = this.getGrowingAge();
        return var1 >= 0 ? 1.0f : 0.5f + (float)(-24000 - var1) / -24000.0f * 0.5f;
    }

    @Override
    public void setScaleForAge(boolean p_98054_1_) {
        if (p_98054_1_) {
            this.setScale(this.getHorseSize());
        } else {
            this.setScale(1.0f);
        }
    }

    public boolean isHorseJumping() {
        return this.horseJumping;
    }

    public void setHorseTamed(boolean p_110234_1_) {
        this.setHorseWatchableBoolean(2, p_110234_1_);
    }

    public void setHorseJumping(boolean p_110255_1_) {
        this.horseJumping = p_110255_1_;
    }

    @Override
    public boolean allowLeashing() {
        return !this.isUndead() && super.allowLeashing();
    }

    @Override
    protected void func_142017_o(float p_142017_1_) {
        if (p_142017_1_ > 6.0f && this.isEatingHaystack()) {
            this.setEatingHaystack(false);
        }
    }

    public boolean isChested() {
        return this.getHorseWatchableBoolean(8);
    }

    public int func_110241_cb() {
        return this.dataWatcher.getWatchableObjectInt(22);
    }

    private int getHorseArmorIndex(ItemStack p_110260_1_) {
        if (p_110260_1_ == null) {
            return 0;
        }
        Item var2 = p_110260_1_.getItem();
        return var2 == Items.iron_horse_armor ? 1 : (var2 == Items.golden_horse_armor ? 2 : (var2 == Items.diamond_horse_armor ? 3 : 0));
    }

    public boolean isEatingHaystack() {
        return this.getHorseWatchableBoolean(32);
    }

    public boolean isRearing() {
        return this.getHorseWatchableBoolean(64);
    }

    public boolean func_110205_ce() {
        return this.getHorseWatchableBoolean(16);
    }

    public boolean getHasReproduced() {
        return this.hasReproduced;
    }

    public void setHorseArmorStack(ItemStack p_146086_1_) {
        this.dataWatcher.updateObject(22, this.getHorseArmorIndex(p_146086_1_));
        this.func_110230_cF();
    }

    public void func_110242_l(boolean p_110242_1_) {
        this.setHorseWatchableBoolean(16, p_110242_1_);
    }

    public void setChested(boolean p_110207_1_) {
        this.setHorseWatchableBoolean(8, p_110207_1_);
    }

    public void setHasReproduced(boolean p_110221_1_) {
        this.hasReproduced = p_110221_1_;
    }

    public void setHorseSaddled(boolean p_110251_1_) {
        this.setHorseWatchableBoolean(4, p_110251_1_);
    }

    public int getTemper() {
        return this.temper;
    }

    public void setTemper(int p_110238_1_) {
        this.temper = p_110238_1_;
    }

    public int increaseTemper(int p_110198_1_) {
        int var2 = MathHelper.clamp_int(this.getTemper() + p_110198_1_, 0, this.getMaxTemper());
        this.setTemper(var2);
        return var2;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity var3 = source.getEntity();
        return this.riddenByEntity != null && this.riddenByEntity.equals(var3) ? false : super.attackEntityFrom(source, amount);
    }

    @Override
    public int getTotalArmorValue() {
        return armorValues[this.func_110241_cb()];
    }

    @Override
    public boolean canBePushed() {
        return this.riddenByEntity == null;
    }

    public boolean prepareChunkForSpawn() {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.posZ);
        this.worldObj.getBiomeGenForCoords(new BlockPos(var1, 0, var2));
        return true;
    }

    public void dropChests() {
        if (!this.worldObj.isRemote && this.isChested()) {
            this.dropItem(Item.getItemFromBlock(Blocks.chest), 1);
            this.setChested(false);
        }
    }

    private void func_110266_cB() {
        this.openHorseMouth();
        if (!this.isSlient()) {
            this.worldObj.playSoundAtEntity(this, "eating", 1.0f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f);
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        int var3;
        if (distance > 1.0f) {
            this.playSound("mob.horse.land", 0.4f, 1.0f);
        }
        if ((var3 = MathHelper.ceiling_float_int((distance * 0.5f - 3.0f) * damageMultiplier)) > 0) {
            Block var4;
            this.attackEntityFrom(DamageSource.fall, var3);
            if (this.riddenByEntity != null) {
                this.riddenByEntity.attackEntityFrom(DamageSource.fall, var3);
            }
            if ((var4 = this.worldObj.getBlockState(new BlockPos(this.posX, this.posY - 0.2 - (double)this.prevRotationYaw, this.posZ)).getBlock()).getMaterial() != Material.air && !this.isSlient()) {
                Block.SoundType var5 = var4.stepSound;
                this.worldObj.playSoundAtEntity(this, var5.getStepSound(), var5.getVolume() * 0.5f, var5.getFrequency() * 0.75f);
            }
        }
    }

    private int func_110225_cC() {
        int var1 = this.getHorseType();
        return this.isChested() && (var1 == 1 || var1 == 2) ? 17 : 2;
    }

    private void func_110226_cD() {
        AnimalChest var1 = this.horseChest;
        this.horseChest = new AnimalChest("HorseChest", this.func_110225_cC());
        this.horseChest.func_110133_a(this.getName());
        if (var1 != null) {
            var1.func_110132_b(this);
            int var2 = Math.min(var1.getSizeInventory(), this.horseChest.getSizeInventory());
            for (int var3 = 0; var3 < var2; ++var3) {
                ItemStack var4 = var1.getStackInSlot(var3);
                if (var4 == null) continue;
                this.horseChest.setInventorySlotContents(var3, var4.copy());
            }
        }
        this.horseChest.func_110134_a(this);
        this.func_110232_cE();
    }

    private void func_110232_cE() {
        if (!this.worldObj.isRemote) {
            this.setHorseSaddled(this.horseChest.getStackInSlot(0) != null);
            if (this.canWearArmor()) {
                this.setHorseArmorStack(this.horseChest.getStackInSlot(1));
            }
        }
    }

    @Override
    public void onInventoryChanged(InventoryBasic p_76316_1_) {
        int var2 = this.func_110241_cb();
        boolean var3 = this.isHorseSaddled();
        this.func_110232_cE();
        if (this.ticksExisted > 20) {
            if (var2 == 0 && var2 != this.func_110241_cb()) {
                this.playSound("mob.horse.armor", 0.5f, 1.0f);
            } else if (var2 != this.func_110241_cb()) {
                this.playSound("mob.horse.armor", 0.5f, 1.0f);
            }
            if (!var3 && this.isHorseSaddled()) {
                this.playSound("mob.horse.leather", 0.5f, 1.0f);
            }
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        this.prepareChunkForSpawn();
        return super.getCanSpawnHere();
    }

    protected EntityHorse getClosestHorse(Entity p_110250_1_, double p_110250_2_) {
        double var4 = Double.MAX_VALUE;
        Entity var6 = null;
        List var7 = this.worldObj.func_175674_a(p_110250_1_, p_110250_1_.getEntityBoundingBox().addCoord(p_110250_2_, p_110250_2_, p_110250_2_), horseBreedingSelector);
        for (Entity var9 : var7) {
            double var10 = var9.getDistanceSq(p_110250_1_.posX, p_110250_1_.posY, p_110250_1_.posZ);
            if (!(var10 < var4)) continue;
            var6 = var9;
            var4 = var10;
        }
        return (EntityHorse)var6;
    }

    public double getHorseJumpStrength() {
        return this.getEntityAttribute(horseJumpStrength).getAttributeValue();
    }

    @Override
    protected String getDeathSound() {
        this.openHorseMouth();
        int var1 = this.getHorseType();
        return var1 == 3 ? "mob.horse.zombie.death" : (var1 == 4 ? "mob.horse.skeleton.death" : (var1 != 1 && var1 != 2 ? "mob.horse.death" : "mob.horse.donkey.death"));
    }

    @Override
    protected Item getDropItem() {
        boolean var1 = this.rand.nextInt(4) == 0;
        int var2 = this.getHorseType();
        return var2 == 4 ? Items.bone : (var2 == 3 ? (var1 ? null : Items.rotten_flesh) : Items.leather);
    }

    @Override
    protected String getHurtSound() {
        int var1;
        this.openHorseMouth();
        if (this.rand.nextInt(3) == 0) {
            this.makeHorseRear();
        }
        return (var1 = this.getHorseType()) == 3 ? "mob.horse.zombie.hit" : (var1 == 4 ? "mob.horse.skeleton.hit" : (var1 != 1 && var1 != 2 ? "mob.horse.hit" : "mob.horse.donkey.hit"));
    }

    public boolean isHorseSaddled() {
        return this.getHorseWatchableBoolean(4);
    }

    @Override
    protected String getLivingSound() {
        int var1;
        this.openHorseMouth();
        if (this.rand.nextInt(10) == 0 && !this.isMovementBlocked()) {
            this.makeHorseRear();
        }
        return (var1 = this.getHorseType()) == 3 ? "mob.horse.zombie.idle" : (var1 == 4 ? "mob.horse.skeleton.idle" : (var1 != 1 && var1 != 2 ? "mob.horse.idle" : "mob.horse.donkey.idle"));
    }

    protected String getAngrySoundName() {
        this.openHorseMouth();
        this.makeHorseRear();
        int var1 = this.getHorseType();
        return var1 != 3 && var1 != 4 ? (var1 != 1 && var1 != 2 ? "mob.horse.angry" : "mob.horse.donkey.angry") : null;
    }

    @Override
    protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_) {
        Block.SoundType var3 = p_180429_2_.stepSound;
        if (this.worldObj.getBlockState(p_180429_1_.offsetUp()).getBlock() == Blocks.snow_layer) {
            var3 = Blocks.snow_layer.stepSound;
        }
        if (!p_180429_2_.getMaterial().isLiquid()) {
            int var4 = this.getHorseType();
            if (this.riddenByEntity != null && var4 != 1 && var4 != 2) {
                ++this.gallopTime;
                if (this.gallopTime > 5 && this.gallopTime % 3 == 0) {
                    this.playSound("mob.horse.gallop", var3.getVolume() * 0.15f, var3.getFrequency());
                    if (var4 == 0 && this.rand.nextInt(10) == 0) {
                        this.playSound("mob.horse.breathe", var3.getVolume() * 0.6f, var3.getFrequency());
                    }
                } else if (this.gallopTime <= 5) {
                    this.playSound("mob.horse.wood", var3.getVolume() * 0.15f, var3.getFrequency());
                }
            } else if (var3 == Block.soundTypeWood) {
                this.playSound("mob.horse.wood", var3.getVolume() * 0.15f, var3.getFrequency());
            } else {
                this.playSound("mob.horse.soft", var3.getVolume() * 0.15f, var3.getFrequency());
            }
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(horseJumpStrength);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(53.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.225f);
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 6;
    }

    public int getMaxTemper() {
        return 100;
    }

    @Override
    protected float getSoundVolume() {
        return 0.8f;
    }

    @Override
    public int getTalkInterval() {
        return 400;
    }

    public boolean func_110239_cn() {
        return this.getHorseType() == 0 || this.func_110241_cb() > 0;
    }

    private void func_110230_cF() {
        this.field_110286_bQ = null;
    }

    public boolean func_175507_cI() {
        return this.field_175508_bO;
    }

    private void setHorseTexturePaths() {
        int var3;
        this.field_110286_bQ = "horse/";
        this.field_110280_bR[0] = null;
        this.field_110280_bR[1] = null;
        this.field_110280_bR[2] = null;
        int var1 = this.getHorseType();
        int var2 = this.getHorseVariant();
        if (var1 == 0) {
            var3 = var2 & 0xFF;
            int var4 = (var2 & 0xFF00) >> 8;
            if (var3 >= horseTextures.length) {
                this.field_175508_bO = false;
                return;
            }
            this.field_110280_bR[0] = horseTextures[var3];
            this.field_110286_bQ = String.valueOf(this.field_110286_bQ) + field_110269_bA[var3];
            if (var4 >= horseMarkingTextures.length) {
                this.field_175508_bO = false;
                return;
            }
            this.field_110280_bR[1] = horseMarkingTextures[var4];
            this.field_110286_bQ = String.valueOf(this.field_110286_bQ) + field_110292_bC[var4];
        } else {
            this.field_110280_bR[0] = "";
            this.field_110286_bQ = String.valueOf(this.field_110286_bQ) + "_" + var1 + "_";
        }
        var3 = this.func_110241_cb();
        if (var3 >= horseArmorTextures.length) {
            this.field_175508_bO = false;
        } else {
            this.field_110280_bR[2] = horseArmorTextures[var3];
            this.field_110286_bQ = String.valueOf(this.field_110286_bQ) + field_110273_bx[var3];
            this.field_175508_bO = true;
        }
    }

    public String getHorseTexture() {
        if (this.field_110286_bQ == null) {
            this.setHorseTexturePaths();
        }
        return this.field_110286_bQ;
    }

    public String[] getVariantTexturePaths() {
        if (this.field_110286_bQ == null) {
            this.setHorseTexturePaths();
        }
        return this.field_110280_bR;
    }

    public void openGUI(EntityPlayer p_110199_1_) {
        if (!this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == p_110199_1_) && this.isTame()) {
            this.horseChest.func_110133_a(this.getName());
            p_110199_1_.displayGUIHorse(this, this.horseChest);
        }
    }

    @Override
    public boolean interact(EntityPlayer p_70085_1_) {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();
        if (var2 != null && var2.getItem() == Items.spawn_egg) {
            return super.interact(p_70085_1_);
        }
        if (!this.isTame() && this.isUndead()) {
            return false;
        }
        if (this.isTame() && this.isAdultHorse() && p_70085_1_.isSneaking()) {
            this.openGUI(p_70085_1_);
            return true;
        }
        if (this.func_110253_bW() && this.riddenByEntity != null) {
            return super.interact(p_70085_1_);
        }
        if (var2 != null) {
            boolean var3 = false;
            if (this.canWearArmor()) {
                int var4 = -1;
                if (var2.getItem() == Items.iron_horse_armor) {
                    var4 = 1;
                } else if (var2.getItem() == Items.golden_horse_armor) {
                    var4 = 2;
                } else if (var2.getItem() == Items.diamond_horse_armor) {
                    var4 = 3;
                }
                if (var4 >= 0) {
                    if (!this.isTame()) {
                        this.makeHorseRearWithSound();
                        return true;
                    }
                    this.openGUI(p_70085_1_);
                    return true;
                }
            }
            if (!var3 && !this.isUndead()) {
                float var7 = 0.0f;
                int var5 = 0;
                int var6 = 0;
                if (var2.getItem() == Items.wheat) {
                    var7 = 2.0f;
                    var5 = 20;
                    var6 = 3;
                } else if (var2.getItem() == Items.sugar) {
                    var7 = 1.0f;
                    var5 = 30;
                    var6 = 3;
                } else if (Block.getBlockFromItem(var2.getItem()) == Blocks.hay_block) {
                    var7 = 20.0f;
                    var5 = 180;
                } else if (var2.getItem() == Items.apple) {
                    var7 = 3.0f;
                    var5 = 60;
                    var6 = 3;
                } else if (var2.getItem() == Items.golden_carrot) {
                    var7 = 4.0f;
                    var5 = 60;
                    var6 = 5;
                    if (this.isTame() && this.getGrowingAge() == 0) {
                        var3 = true;
                        this.setInLove(p_70085_1_);
                    }
                } else if (var2.getItem() == Items.golden_apple) {
                    var7 = 10.0f;
                    var5 = 240;
                    var6 = 10;
                    if (this.isTame() && this.getGrowingAge() == 0) {
                        var3 = true;
                        this.setInLove(p_70085_1_);
                    }
                }
                if (this.getHealth() < this.getMaxHealth() && var7 > 0.0f) {
                    this.heal(var7);
                    var3 = true;
                }
                if (!this.isAdultHorse() && var5 > 0) {
                    this.addGrowth(var5);
                    var3 = true;
                }
                if (var6 > 0 && (var3 || !this.isTame()) && var6 < this.getMaxTemper()) {
                    var3 = true;
                    this.increaseTemper(var6);
                }
                if (var3) {
                    this.func_110266_cB();
                }
            }
            if (!this.isTame() && !var3) {
                if (var2 != null && var2.interactWithEntity(p_70085_1_, this)) {
                    return true;
                }
                this.makeHorseRearWithSound();
                return true;
            }
            if (!var3 && this.canCarryChest() && !this.isChested() && var2.getItem() == Item.getItemFromBlock(Blocks.chest)) {
                this.setChested(true);
                this.playSound("mob.chickenplop", 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
                var3 = true;
                this.func_110226_cD();
            }
            if (!var3 && this.func_110253_bW() && !this.isHorseSaddled() && var2.getItem() == Items.saddle) {
                this.openGUI(p_70085_1_);
                return true;
            }
            if (var3) {
                if (!p_70085_1_.capabilities.isCreativeMode && --var2.stackSize == 0) {
                    p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, null);
                }
                return true;
            }
        }
        if (this.func_110253_bW() && this.riddenByEntity == null) {
            if (var2 != null && var2.interactWithEntity(p_70085_1_, this)) {
                return true;
            }
            this.func_110237_h(p_70085_1_);
            return true;
        }
        return super.interact(p_70085_1_);
    }

    private void func_110237_h(EntityPlayer p_110237_1_) {
        p_110237_1_.rotationYaw = this.rotationYaw;
        p_110237_1_.rotationPitch = this.rotationPitch;
        this.setEatingHaystack(false);
        this.setRearing(false);
        if (!this.worldObj.isRemote) {
            p_110237_1_.mountEntity(this);
        }
    }

    public boolean canWearArmor() {
        return this.getHorseType() == 0;
    }

    public boolean canCarryChest() {
        int var1 = this.getHorseType();
        return var1 == 2 || var1 == 1;
    }

    @Override
    protected boolean isMovementBlocked() {
        return this.riddenByEntity != null && this.isHorseSaddled() ? true : this.isEatingHaystack() || this.isRearing();
    }

    public boolean isUndead() {
        int var1 = this.getHorseType();
        return var1 == 3 || var1 == 4;
    }

    public boolean isSterile() {
        return this.isUndead() || this.getHorseType() == 2;
    }

    @Override
    public boolean isBreedingItem(ItemStack p_70877_1_) {
        return false;
    }

    private void func_110210_cH() {
        this.field_110278_bp = 1;
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!this.worldObj.isRemote) {
            this.dropChestItems();
        }
    }

    @Override
    public void onLivingUpdate() {
        if (this.rand.nextInt(200) == 0) {
            this.func_110210_cH();
        }
        super.onLivingUpdate();
        if (!this.worldObj.isRemote) {
            EntityHorse var1;
            if (this.rand.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0f);
            }
            if (!this.isEatingHaystack() && this.riddenByEntity == null && this.rand.nextInt(300) == 0 && this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) - 1, MathHelper.floor_double(this.posZ))).getBlock() == Blocks.grass) {
                this.setEatingHaystack(true);
            }
            if (this.isEatingHaystack() && ++this.eatingHaystackCounter > 50) {
                this.eatingHaystackCounter = 0;
                this.setEatingHaystack(false);
            }
            if (this.func_110205_ce() && !this.isAdultHorse() && !this.isEatingHaystack() && (var1 = this.getClosestHorse(this, 16.0)) != null && this.getDistanceSqToEntity(var1) > 4.0) {
                this.navigator.getPathToEntityLiving(var1);
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.worldObj.isRemote && this.dataWatcher.hasObjectChanged()) {
            this.dataWatcher.func_111144_e();
            this.func_110230_cF();
        }
        if (this.openMouthCounter > 0 && ++this.openMouthCounter > 30) {
            this.openMouthCounter = 0;
            this.setHorseWatchableBoolean(128, false);
        }
        if (!this.worldObj.isRemote && this.jumpRearingCounter > 0 && ++this.jumpRearingCounter > 20) {
            this.jumpRearingCounter = 0;
            this.setRearing(false);
        }
        if (this.field_110278_bp > 0 && ++this.field_110278_bp > 8) {
            this.field_110278_bp = 0;
        }
        if (this.field_110279_bq > 0) {
            ++this.field_110279_bq;
            if (this.field_110279_bq > 300) {
                this.field_110279_bq = 0;
            }
        }
        this.prevHeadLean = this.headLean;
        if (this.isEatingHaystack()) {
            this.headLean += (1.0f - this.headLean) * 0.4f + 0.05f;
            if (this.headLean > 1.0f) {
                this.headLean = 1.0f;
            }
        } else {
            this.headLean += (0.0f - this.headLean) * 0.4f - 0.05f;
            if (this.headLean < 0.0f) {
                this.headLean = 0.0f;
            }
        }
        this.prevRearingAmount = this.rearingAmount;
        if (this.isRearing()) {
            this.headLean = 0.0f;
            this.prevHeadLean = 0.0f;
            this.rearingAmount += (1.0f - this.rearingAmount) * 0.4f + 0.05f;
            if (this.rearingAmount > 1.0f) {
                this.rearingAmount = 1.0f;
            }
        } else {
            this.field_110294_bI = false;
            this.rearingAmount += (0.8f * this.rearingAmount * this.rearingAmount * this.rearingAmount - this.rearingAmount) * 0.6f - 0.05f;
            if (this.rearingAmount < 0.0f) {
                this.rearingAmount = 0.0f;
            }
        }
        this.prevMouthOpenness = this.mouthOpenness;
        if (this.getHorseWatchableBoolean(128)) {
            this.mouthOpenness += (1.0f - this.mouthOpenness) * 0.7f + 0.05f;
            if (this.mouthOpenness > 1.0f) {
                this.mouthOpenness = 1.0f;
            }
        } else {
            this.mouthOpenness += (0.0f - this.mouthOpenness) * 0.7f - 0.05f;
            if (this.mouthOpenness < 0.0f) {
                this.mouthOpenness = 0.0f;
            }
        }
    }

    private void openHorseMouth() {
        if (!this.worldObj.isRemote) {
            this.openMouthCounter = 1;
            this.setHorseWatchableBoolean(128, true);
        }
    }

    private boolean canMate() {
        return this.riddenByEntity == null && this.ridingEntity == null && this.isTame() && this.isAdultHorse() && !this.isSterile() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
    }

    @Override
    public void setEating(boolean eating) {
        this.setHorseWatchableBoolean(32, eating);
    }

    public void setEatingHaystack(boolean p_110227_1_) {
        this.setEating(p_110227_1_);
    }

    public void setRearing(boolean p_110219_1_) {
        if (p_110219_1_) {
            this.setEatingHaystack(false);
        }
        this.setHorseWatchableBoolean(64, p_110219_1_);
    }

    private void makeHorseRear() {
        if (!this.worldObj.isRemote) {
            this.jumpRearingCounter = 1;
            this.setRearing(true);
        }
    }

    public void makeHorseRearWithSound() {
        this.makeHorseRear();
        String var1 = this.getAngrySoundName();
        if (var1 != null) {
            this.playSound(var1, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    public void dropChestItems() {
        this.dropItemsInChest(this, this.horseChest);
        this.dropChests();
    }

    private void dropItemsInChest(Entity p_110240_1_, AnimalChest p_110240_2_) {
        if (p_110240_2_ != null && !this.worldObj.isRemote) {
            for (int var3 = 0; var3 < p_110240_2_.getSizeInventory(); ++var3) {
                ItemStack var4 = p_110240_2_.getStackInSlot(var3);
                if (var4 == null) continue;
                this.entityDropItem(var4, 0.0f);
            }
        }
    }

    public boolean setTamedBy(EntityPlayer p_110263_1_) {
        this.func_152120_b(p_110263_1_.getUniqueID().toString());
        this.setHorseTamed(true);
        return true;
    }

    @Override
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase && this.isHorseSaddled()) {
            this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
            this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5f;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            p_70612_1_ = ((EntityLivingBase)this.riddenByEntity).moveStrafing * 0.5f;
            p_70612_2_ = ((EntityLivingBase)this.riddenByEntity).moveForward;
            if (p_70612_2_ <= 0.0f) {
                p_70612_2_ *= 0.25f;
                this.gallopTime = 0;
            }
            if (this.onGround && this.jumpPower == 0.0f && this.isRearing() && !this.field_110294_bI) {
                p_70612_1_ = 0.0f;
                p_70612_2_ = 0.0f;
            }
            if (this.jumpPower > 0.0f && !this.isHorseJumping() && this.onGround) {
                this.motionY = this.getHorseJumpStrength() * (double)this.jumpPower;
                if (this.isPotionActive(Potion.jump)) {
                    this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
                }
                this.setHorseJumping(true);
                this.isAirBorne = true;
                if (p_70612_2_ > 0.0f) {
                    float var3 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f);
                    float var4 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f);
                    this.motionX += (double)(-0.4f * var3 * this.jumpPower);
                    this.motionZ += (double)(0.4f * var4 * this.jumpPower);
                    this.playSound("mob.horse.jump", 0.4f, 1.0f);
                }
                this.jumpPower = 0.0f;
            }
            this.stepHeight = 1.0f;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1f;
            if (!this.worldObj.isRemote) {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
            }
            if (this.onGround) {
                this.jumpPower = 0.0f;
                this.setHorseJumping(false);
            }
            this.prevLimbSwingAmount = this.limbSwingAmount;
            double var8 = this.posX - this.prevPosX;
            double var5 = this.posZ - this.prevPosZ;
            float var7 = MathHelper.sqrt_double(var8 * var8 + var5 * var5) * 4.0f;
            if (var7 > 1.0f) {
                var7 = 1.0f;
            }
            this.limbSwingAmount += (var7 - this.limbSwingAmount) * 0.4f;
            this.limbSwing += this.limbSwingAmount;
        } else {
            this.stepHeight = 0.5f;
            this.jumpMovementFactor = 0.02f;
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("EatingHaystack", this.isEatingHaystack());
        tagCompound.setBoolean("ChestedHorse", this.isChested());
        tagCompound.setBoolean("HasReproduced", this.getHasReproduced());
        tagCompound.setBoolean("Bred", this.func_110205_ce());
        tagCompound.setInteger("Type", this.getHorseType());
        tagCompound.setInteger("Variant", this.getHorseVariant());
        tagCompound.setInteger("Temper", this.getTemper());
        tagCompound.setBoolean("Tame", this.isTame());
        tagCompound.setString("OwnerUUID", this.func_152119_ch());
        if (this.isChested()) {
            NBTTagList var2 = new NBTTagList();
            for (int var3 = 2; var3 < this.horseChest.getSizeInventory(); ++var3) {
                ItemStack var4 = this.horseChest.getStackInSlot(var3);
                if (var4 == null) continue;
                NBTTagCompound var5 = new NBTTagCompound();
                var5.setByte("Slot", (byte)var3);
                var4.writeToNBT(var5);
                var2.appendTag(var5);
            }
            tagCompound.setTag("Items", var2);
        }
        if (this.horseChest.getStackInSlot(1) != null) {
            tagCompound.setTag("ArmorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }
        if (this.horseChest.getStackInSlot(0) != null) {
            tagCompound.setTag("SaddleItem", this.horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        ItemStack var9;
        IAttributeInstance var8;
        super.readEntityFromNBT(tagCompund);
        this.setEatingHaystack(tagCompund.getBoolean("EatingHaystack"));
        this.func_110242_l(tagCompund.getBoolean("Bred"));
        this.setChested(tagCompund.getBoolean("ChestedHorse"));
        this.setHasReproduced(tagCompund.getBoolean("HasReproduced"));
        this.setHorseType(tagCompund.getInteger("Type"));
        this.setHorseVariant(tagCompund.getInteger("Variant"));
        this.setTemper(tagCompund.getInteger("Temper"));
        this.setHorseTamed(tagCompund.getBoolean("Tame"));
        String var2 = "";
        if (tagCompund.hasKey("OwnerUUID", 8)) {
            var2 = tagCompund.getString("OwnerUUID");
        } else {
            String var3 = tagCompund.getString("Owner");
            var2 = PreYggdrasilConverter.func_152719_a(var3);
        }
        if (var2.length() > 0) {
            this.func_152120_b(var2);
        }
        if ((var8 = this.getAttributeMap().getAttributeInstanceByName("Speed")) != null) {
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(var8.getBaseValue() * 0.25);
        }
        if (this.isChested()) {
            NBTTagList var4 = tagCompund.getTagList("Items", 10);
            this.func_110226_cD();
            for (int var5 = 0; var5 < var4.tagCount(); ++var5) {
                NBTTagCompound var6 = var4.getCompoundTagAt(var5);
                int var7 = var6.getByte("Slot") & 0xFF;
                if (var7 < 2 || var7 >= this.horseChest.getSizeInventory()) continue;
                this.horseChest.setInventorySlotContents(var7, ItemStack.loadItemStackFromNBT(var6));
            }
        }
        if (tagCompund.hasKey("ArmorItem", 10) && (var9 = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("ArmorItem"))) != null && EntityHorse.func_146085_a(var9.getItem())) {
            this.horseChest.setInventorySlotContents(1, var9);
        }
        if (tagCompund.hasKey("SaddleItem", 10)) {
            var9 = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("SaddleItem"));
            if (var9 != null && var9.getItem() == Items.saddle) {
                this.horseChest.setInventorySlotContents(0, var9);
            }
        } else if (tagCompund.getBoolean("Saddle")) {
            this.horseChest.setInventorySlotContents(0, new ItemStack(Items.saddle));
        }
        this.func_110232_cE();
    }

    @Override
    public boolean canMateWith(EntityAnimal p_70878_1_) {
        if (p_70878_1_ == this) {
            return false;
        }
        if (p_70878_1_.getClass() != this.getClass()) {
            return false;
        }
        EntityHorse var2 = (EntityHorse)p_70878_1_;
        if (this.canMate() && var2.canMate()) {
            int var4;
            int var3 = this.getHorseType();
            return var3 == (var4 = var2.getHorseType()) || var3 == 0 && var4 == 1 || var3 == 1 && var4 == 0;
        }
        return false;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable p_90011_1_) {
        EntityHorse var2 = (EntityHorse)p_90011_1_;
        EntityHorse var3 = new EntityHorse(this.worldObj);
        int var4 = this.getHorseType();
        int var5 = var2.getHorseType();
        int var6 = 0;
        if (var4 == var5) {
            var6 = var4;
        } else if (var4 == 0 && var5 == 1 || var4 == 1 && var5 == 0) {
            var6 = 2;
        }
        if (var6 == 0) {
            int var8 = this.rand.nextInt(9);
            int var7 = var8 < 4 ? this.getHorseVariant() & 0xFF : (var8 < 8 ? var2.getHorseVariant() & 0xFF : this.rand.nextInt(7));
            int var9 = this.rand.nextInt(5);
            var7 = var9 < 2 ? (var7 |= this.getHorseVariant() & 0xFF00) : (var9 < 4 ? (var7 |= var2.getHorseVariant() & 0xFF00) : (var7 |= this.rand.nextInt(5) << 8 & 0xFF00));
            var3.setHorseVariant(var7);
        }
        var3.setHorseType(var6);
        double var13 = this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + p_90011_1_.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + (double)this.func_110267_cL();
        var3.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(var13 / 3.0);
        double var14 = this.getEntityAttribute(horseJumpStrength).getBaseValue() + p_90011_1_.getEntityAttribute(horseJumpStrength).getBaseValue() + this.func_110245_cM();
        var3.getEntityAttribute(horseJumpStrength).setBaseValue(var14 / 3.0);
        double var11 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + p_90011_1_.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + this.func_110203_cN();
        var3.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(var11 / 3.0);
        return var3;
    }

    @Override
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
        int var8;
        IEntityLivingData p_180482_2_1 = super.func_180482_a(p_180482_1_, p_180482_2_);
        boolean var3 = false;
        int var4 = 0;
        if (p_180482_2_1 instanceof GroupData) {
            var8 = ((GroupData)p_180482_2_1).field_111107_a;
            var4 = ((GroupData)p_180482_2_1).field_111106_b & 0xFF | this.rand.nextInt(5) << 8;
        } else {
            if (this.rand.nextInt(10) == 0) {
                var8 = 1;
            } else {
                int var5 = this.rand.nextInt(7);
                int var6 = this.rand.nextInt(5);
                var8 = 0;
                var4 = var5 | var6 << 8;
            }
            p_180482_2_1 = new GroupData(var8, var4);
        }
        this.setHorseType(var8);
        this.setHorseVariant(var4);
        if (this.rand.nextInt(5) == 0) {
            this.setGrowingAge(-24000);
        }
        if (var8 != 4 && var8 != 3) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.func_110267_cL());
            if (var8 == 0) {
                this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.func_110203_cN());
            } else {
                this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.175f);
            }
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(15.0);
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2f);
        }
        if (var8 != 2 && var8 != 1) {
            this.getEntityAttribute(horseJumpStrength).setBaseValue(this.func_110245_cM());
        } else {
            this.getEntityAttribute(horseJumpStrength).setBaseValue(0.5);
        }
        this.setHealth(this.getMaxHealth());
        return p_180482_2_1;
    }

    public float getGrassEatingAmount(float p_110258_1_) {
        return this.prevHeadLean + (this.headLean - this.prevHeadLean) * p_110258_1_;
    }

    public float getRearingAmount(float p_110223_1_) {
        return this.prevRearingAmount + (this.rearingAmount - this.prevRearingAmount) * p_110223_1_;
    }

    public float func_110201_q(float p_110201_1_) {
        return this.prevMouthOpenness + (this.mouthOpenness - this.prevMouthOpenness) * p_110201_1_;
    }

    public void setJumpPower(int p_110206_1_) {
        if (this.isHorseSaddled()) {
            if (p_110206_1_ < 0) {
                p_110206_1_ = 0;
            } else {
                this.field_110294_bI = true;
                this.makeHorseRear();
            }
            this.jumpPower = p_110206_1_ >= 90 ? 1.0f : 0.4f + 0.4f * (float)p_110206_1_ / 90.0f;
        }
    }

    protected void spawnHorseParticles(boolean p_110216_1_) {
        EnumParticleTypes var2 = p_110216_1_ ? EnumParticleTypes.HEART : EnumParticleTypes.SMOKE_NORMAL;
        for (int var3 = 0; var3 < 7; ++var3) {
            double var4 = this.rand.nextGaussian() * 0.02;
            double var6 = this.rand.nextGaussian() * 0.02;
            double var8 = this.rand.nextGaussian() * 0.02;
            this.worldObj.spawnParticle(var2, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, var4, var6, var8, new int[0]);
        }
    }

    @Override
    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 7) {
            this.spawnHorseParticles(true);
        } else if (p_70103_1_ == 6) {
            this.spawnHorseParticles(false);
        } else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    @Override
    public void updateRiderPosition() {
        super.updateRiderPosition();
        if (this.prevRearingAmount > 0.0f) {
            float var1 = MathHelper.sin(this.renderYawOffset * (float)Math.PI / 180.0f);
            float var2 = MathHelper.cos(this.renderYawOffset * (float)Math.PI / 180.0f);
            float var3 = 0.7f * this.prevRearingAmount;
            float var4 = 0.15f * this.prevRearingAmount;
            this.riddenByEntity.setPosition(this.posX + (double)(var3 * var1), this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset() + (double)var4, this.posZ - (double)(var3 * var2));
            if (this.riddenByEntity instanceof EntityLivingBase) {
                ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
            }
        }
    }

    private float func_110267_cL() {
        return 15.0f + (float)this.rand.nextInt(8) + (float)this.rand.nextInt(9);
    }

    private double func_110245_cM() {
        return (double)0.4f + this.rand.nextDouble() * 0.2 + this.rand.nextDouble() * 0.2 + this.rand.nextDouble() * 0.2;
    }

    private double func_110203_cN() {
        return ((double)0.45f + this.rand.nextDouble() * 0.3 + this.rand.nextDouble() * 0.3 + this.rand.nextDouble() * 0.3) * 0.25;
    }

    public static boolean func_146085_a(Item p_146085_0_) {
        return p_146085_0_ == Items.iron_horse_armor || p_146085_0_ == Items.golden_horse_armor || p_146085_0_ == Items.diamond_horse_armor;
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return this.height;
    }

    @Override
    public boolean func_174820_d(int p_174820_1_, ItemStack p_174820_2_) {
        int var3;
        if (p_174820_1_ == 499 && this.canCarryChest()) {
            if (p_174820_2_ == null && this.isChested()) {
                this.setChested(false);
                this.func_110226_cD();
                return true;
            }
            if (p_174820_2_ != null && p_174820_2_.getItem() == Item.getItemFromBlock(Blocks.chest) && !this.isChested()) {
                this.setChested(true);
                this.func_110226_cD();
                return true;
            }
        }
        if ((var3 = p_174820_1_ - 400) >= 0 && var3 < 2 && var3 < this.horseChest.getSizeInventory()) {
            if (var3 == 0 && p_174820_2_ != null && p_174820_2_.getItem() != Items.saddle) {
                return false;
            }
            if (var3 == 1 && (p_174820_2_ != null && !EntityHorse.func_146085_a(p_174820_2_.getItem()) || !this.canWearArmor())) {
                return false;
            }
            this.horseChest.setInventorySlotContents(var3, p_174820_2_);
            this.func_110232_cE();
            return true;
        }
        int var4 = p_174820_1_ - 500 + 2;
        if (var4 >= 2 && var4 < this.horseChest.getSizeInventory()) {
            this.horseChest.setInventorySlotContents(var4, p_174820_2_);
            return true;
        }
        return false;
    }

    public static class GroupData
    implements IEntityLivingData {
        public int field_111107_a;
        public int field_111106_b;
        private static final String __OBFID = "CL_00001643";

        public GroupData(int p_i1684_1_, int p_i1684_2_) {
            this.field_111107_a = p_i1684_1_;
            this.field_111106_b = p_i1684_2_;
        }
    }
}

