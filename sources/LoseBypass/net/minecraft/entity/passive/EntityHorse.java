/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import java.util.Iterator;
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
    private static final Predicate<Entity> horseBreedingSelector = new Predicate<Entity>(){

        @Override
        public boolean apply(Entity p_apply_1_) {
            if (!(p_apply_1_ instanceof EntityHorse)) return false;
            if (!((EntityHorse)p_apply_1_).isBreeding()) return false;
            return true;
        }
    };
    private static final IAttribute horseJumpStrength = new RangedAttribute(null, "horse.jumpStrength", 0.7, 0.0, 2.0).setDescription("Jump Strength").setShouldWatch(true);
    private static final String[] horseArmorTextures = new String[]{null, "textures/entity/horse/armor/horse_armor_iron.png", "textures/entity/horse/armor/horse_armor_gold.png", "textures/entity/horse/armor/horse_armor_diamond.png"};
    private static final String[] HORSE_ARMOR_TEXTURES_ABBR = new String[]{"", "meo", "goo", "dio"};
    private static final int[] armorValues = new int[]{0, 5, 7, 11};
    private static final String[] horseTextures = new String[]{"textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png"};
    private static final String[] HORSE_TEXTURES_ABBR = new String[]{"hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb"};
    private static final String[] horseMarkingTextures = new String[]{null, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png"};
    private static final String[] HORSE_MARKING_TEXTURES_ABBR = new String[]{"", "wo_", "wmo", "wdo", "bdo"};
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
    private String texturePrefix;
    private String[] horseTexturesArray = new String[3];
    private boolean field_175508_bO = false;

    public EntityHorse(World worldIn) {
        super(worldIn);
        this.setSize(1.4f, 1.6f);
        this.isImmuneToFire = false;
        this.setChested(false);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.2));
        this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.0));
        this.tasks.addTask(6, new EntityAIWander(this, 0.7));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.initHorseChest();
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

    public void setHorseType(int type) {
        this.dataWatcher.updateObject(19, (byte)type);
        this.resetTexturePrefix();
    }

    public int getHorseType() {
        return this.dataWatcher.getWatchableObjectByte(19);
    }

    public void setHorseVariant(int variant) {
        this.dataWatcher.updateObject(20, variant);
        this.resetTexturePrefix();
    }

    public int getHorseVariant() {
        return this.dataWatcher.getWatchableObjectInt(20);
    }

    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        int i = this.getHorseType();
        switch (i) {
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
        if ((this.dataWatcher.getWatchableObjectInt(16) & p_110233_1_) == 0) return false;
        return true;
    }

    private void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_) {
        int i = this.dataWatcher.getWatchableObjectInt(16);
        if (p_110208_2_) {
            this.dataWatcher.updateObject(16, i | p_110208_1_);
            return;
        }
        this.dataWatcher.updateObject(16, i & ~p_110208_1_);
    }

    public boolean isAdultHorse() {
        if (this.isChild()) return false;
        return true;
    }

    public boolean isTame() {
        return this.getHorseWatchableBoolean(2);
    }

    public boolean func_110253_bW() {
        return this.isAdultHorse();
    }

    public String getOwnerId() {
        return this.dataWatcher.getWatchableObjectString(21);
    }

    public void setOwnerId(String id) {
        this.dataWatcher.updateObject(21, id);
    }

    public float getHorseSize() {
        return 0.5f;
    }

    @Override
    public void setScaleForAge(boolean p_98054_1_) {
        if (p_98054_1_) {
            this.setScale(this.getHorseSize());
            return;
        }
        this.setScale(1.0f);
    }

    public boolean isHorseJumping() {
        return this.horseJumping;
    }

    public void setHorseTamed(boolean tamed) {
        this.setHorseWatchableBoolean(2, tamed);
    }

    public void setHorseJumping(boolean jumping) {
        this.horseJumping = jumping;
    }

    @Override
    public boolean allowLeashing() {
        if (this.isUndead()) return false;
        if (!super.allowLeashing()) return false;
        return true;
    }

    @Override
    protected void func_142017_o(float p_142017_1_) {
        if (!(p_142017_1_ > 6.0f)) return;
        if (!this.isEatingHaystack()) return;
        this.setEatingHaystack(false);
    }

    public boolean isChested() {
        return this.getHorseWatchableBoolean(8);
    }

    public int getHorseArmorIndexSynced() {
        return this.dataWatcher.getWatchableObjectInt(22);
    }

    private int getHorseArmorIndex(ItemStack itemStackIn) {
        if (itemStackIn == null) {
            return 0;
        }
        Item item = itemStackIn.getItem();
        if (item == Items.iron_horse_armor) {
            return 1;
        }
        if (item == Items.golden_horse_armor) {
            return 2;
        }
        if (item != Items.diamond_horse_armor) return 0;
        return 3;
    }

    public boolean isEatingHaystack() {
        return this.getHorseWatchableBoolean(32);
    }

    public boolean isRearing() {
        return this.getHorseWatchableBoolean(64);
    }

    public boolean isBreeding() {
        return this.getHorseWatchableBoolean(16);
    }

    public boolean getHasReproduced() {
        return this.hasReproduced;
    }

    public void setHorseArmorStack(ItemStack itemStackIn) {
        this.dataWatcher.updateObject(22, this.getHorseArmorIndex(itemStackIn));
        this.resetTexturePrefix();
    }

    public void setBreeding(boolean breeding) {
        this.setHorseWatchableBoolean(16, breeding);
    }

    public void setChested(boolean chested) {
        this.setHorseWatchableBoolean(8, chested);
    }

    public void setHasReproduced(boolean hasReproducedIn) {
        this.hasReproduced = hasReproducedIn;
    }

    public void setHorseSaddled(boolean saddled) {
        this.setHorseWatchableBoolean(4, saddled);
    }

    public int getTemper() {
        return this.temper;
    }

    public void setTemper(int temperIn) {
        this.temper = temperIn;
    }

    public int increaseTemper(int p_110198_1_) {
        int i = MathHelper.clamp_int(this.getTemper() + p_110198_1_, 0, this.getMaxTemper());
        this.setTemper(i);
        return i;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (this.riddenByEntity != null && this.riddenByEntity.equals(entity)) {
            return false;
        }
        boolean bl = super.attackEntityFrom(source, amount);
        return bl;
    }

    @Override
    public int getTotalArmorValue() {
        return armorValues[this.getHorseArmorIndexSynced()];
    }

    @Override
    public boolean canBePushed() {
        if (this.riddenByEntity != null) return false;
        return true;
    }

    public boolean prepareChunkForSpawn() {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posZ);
        this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, j));
        return true;
    }

    public void dropChests() {
        if (this.worldObj.isRemote) return;
        if (!this.isChested()) return;
        this.dropItem(Item.getItemFromBlock(Blocks.chest), 1);
        this.setChested(false);
    }

    private void func_110266_cB() {
        this.openHorseMouth();
        if (this.isSilent()) return;
        this.worldObj.playSoundAtEntity(this, "eating", 1.0f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        Block block;
        int i;
        if (distance > 1.0f) {
            this.playSound("mob.horse.land", 0.4f, 1.0f);
        }
        if ((i = MathHelper.ceiling_float_int((distance * 0.5f - 3.0f) * damageMultiplier)) <= 0) return;
        this.attackEntityFrom(DamageSource.fall, i);
        if (this.riddenByEntity != null) {
            this.riddenByEntity.attackEntityFrom(DamageSource.fall, i);
        }
        if ((block = this.worldObj.getBlockState(new BlockPos(this.posX, this.posY - 0.2 - (double)this.prevRotationYaw, this.posZ)).getBlock()).getMaterial() == Material.air) return;
        if (this.isSilent()) return;
        Block.SoundType block$soundtype = block.stepSound;
        this.worldObj.playSoundAtEntity(this, block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.5f, block$soundtype.getFrequency() * 0.75f);
    }

    private int getChestSize() {
        int i = this.getHorseType();
        if (!this.isChested()) return 2;
        if (i == 1) return 17;
        if (i == 2) return 17;
        return 2;
    }

    private void initHorseChest() {
        AnimalChest animalchest = this.horseChest;
        this.horseChest = new AnimalChest("HorseChest", this.getChestSize());
        this.horseChest.setCustomName(this.getName());
        if (animalchest != null) {
            animalchest.func_110132_b(this);
            int i = Math.min(animalchest.getSizeInventory(), this.horseChest.getSizeInventory());
            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = animalchest.getStackInSlot(j);
                if (itemstack == null) continue;
                this.horseChest.setInventorySlotContents(j, itemstack.copy());
            }
        }
        this.horseChest.func_110134_a(this);
        this.updateHorseSlots();
    }

    private void updateHorseSlots() {
        if (this.worldObj.isRemote) return;
        this.setHorseSaddled(this.horseChest.getStackInSlot(0) != null);
        if (!this.canWearArmor()) return;
        this.setHorseArmorStack(this.horseChest.getStackInSlot(1));
    }

    @Override
    public void onInventoryChanged(InventoryBasic p_76316_1_) {
        int i = this.getHorseArmorIndexSynced();
        boolean flag = this.isHorseSaddled();
        this.updateHorseSlots();
        if (this.ticksExisted <= 20) return;
        if (i == 0 && i != this.getHorseArmorIndexSynced()) {
            this.playSound("mob.horse.armor", 0.5f, 1.0f);
        } else if (i != this.getHorseArmorIndexSynced()) {
            this.playSound("mob.horse.armor", 0.5f, 1.0f);
        }
        if (flag) return;
        if (!this.isHorseSaddled()) return;
        this.playSound("mob.horse.leather", 0.5f, 1.0f);
    }

    @Override
    public boolean getCanSpawnHere() {
        this.prepareChunkForSpawn();
        return super.getCanSpawnHere();
    }

    protected EntityHorse getClosestHorse(Entity entityIn, double distance) {
        double d0 = Double.MAX_VALUE;
        Entity entity = null;
        Iterator<Entity> iterator = this.worldObj.getEntitiesInAABBexcluding(entityIn, entityIn.getEntityBoundingBox().addCoord(distance, distance, distance), horseBreedingSelector).iterator();
        while (iterator.hasNext()) {
            Entity entity1 = iterator.next();
            double d1 = entity1.getDistanceSq(entityIn.posX, entityIn.posY, entityIn.posZ);
            if (!(d1 < d0)) continue;
            entity = entity1;
            d0 = d1;
        }
        return (EntityHorse)entity;
    }

    public double getHorseJumpStrength() {
        return this.getEntityAttribute(horseJumpStrength).getAttributeValue();
    }

    @Override
    protected String getDeathSound() {
        this.openHorseMouth();
        int i = this.getHorseType();
        if (i == 3) {
            return "mob.horse.zombie.death";
        }
        if (i == 4) {
            return "mob.horse.skeleton.death";
        }
        if (i == 1) return "mob.horse.donkey.death";
        if (i == 2) return "mob.horse.donkey.death";
        return "mob.horse.death";
    }

    @Override
    protected Item getDropItem() {
        Item item;
        boolean flag = this.rand.nextInt(4) == 0;
        int i = this.getHorseType();
        if (i == 4) {
            item = Items.bone;
            return item;
        }
        if (i != 3) {
            item = Items.leather;
            return item;
        }
        if (flag) {
            return null;
        }
        item = Items.rotten_flesh;
        return item;
    }

    @Override
    protected String getHurtSound() {
        int i;
        this.openHorseMouth();
        if (this.rand.nextInt(3) == 0) {
            this.makeHorseRear();
        }
        if ((i = this.getHorseType()) == 3) {
            return "mob.horse.zombie.hit";
        }
        if (i == 4) {
            return "mob.horse.skeleton.hit";
        }
        if (i == 1) return "mob.horse.donkey.hit";
        if (i == 2) return "mob.horse.donkey.hit";
        return "mob.horse.hit";
    }

    public boolean isHorseSaddled() {
        return this.getHorseWatchableBoolean(4);
    }

    @Override
    protected String getLivingSound() {
        int i;
        this.openHorseMouth();
        if (this.rand.nextInt(10) == 0 && !this.isMovementBlocked()) {
            this.makeHorseRear();
        }
        if ((i = this.getHorseType()) == 3) {
            return "mob.horse.zombie.idle";
        }
        if (i == 4) {
            return "mob.horse.skeleton.idle";
        }
        if (i == 1) return "mob.horse.donkey.idle";
        if (i == 2) return "mob.horse.donkey.idle";
        return "mob.horse.idle";
    }

    protected String getAngrySoundName() {
        this.openHorseMouth();
        this.makeHorseRear();
        int i = this.getHorseType();
        if (i == 3) return null;
        if (i == 4) return null;
        if (i == 1) return "mob.horse.donkey.angry";
        if (i == 2) return "mob.horse.donkey.angry";
        return "mob.horse.angry";
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        Block.SoundType block$soundtype = blockIn.stepSound;
        if (this.worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer) {
            block$soundtype = Blocks.snow_layer.stepSound;
        }
        if (blockIn.getMaterial().isLiquid()) return;
        int i = this.getHorseType();
        if (this.riddenByEntity != null && i != 1 && i != 2) {
            ++this.gallopTime;
            if (this.gallopTime > 5 && this.gallopTime % 3 == 0) {
                this.playSound("mob.horse.gallop", block$soundtype.getVolume() * 0.15f, block$soundtype.getFrequency());
                if (i != 0) return;
                if (this.rand.nextInt(10) != 0) return;
                this.playSound("mob.horse.breathe", block$soundtype.getVolume() * 0.6f, block$soundtype.getFrequency());
                return;
            }
            if (this.gallopTime > 5) return;
            this.playSound("mob.horse.wood", block$soundtype.getVolume() * 0.15f, block$soundtype.getFrequency());
            return;
        }
        if (block$soundtype == Block.soundTypeWood) {
            this.playSound("mob.horse.wood", block$soundtype.getVolume() * 0.15f, block$soundtype.getFrequency());
            return;
        }
        this.playSound("mob.horse.soft", block$soundtype.getVolume() * 0.15f, block$soundtype.getFrequency());
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
        if (this.getHorseType() == 0) return true;
        if (this.getHorseArmorIndexSynced() > 0) return true;
        return false;
    }

    private void resetTexturePrefix() {
        this.texturePrefix = null;
    }

    public boolean func_175507_cI() {
        return this.field_175508_bO;
    }

    private void setHorseTexturePaths() {
        this.texturePrefix = "horse/";
        this.horseTexturesArray[0] = null;
        this.horseTexturesArray[1] = null;
        this.horseTexturesArray[2] = null;
        int i = this.getHorseType();
        int j = this.getHorseVariant();
        if (i == 0) {
            int k = j & 0xFF;
            int l = (j & 0xFF00) >> 8;
            if (k >= horseTextures.length) {
                this.field_175508_bO = false;
                return;
            }
            this.horseTexturesArray[0] = horseTextures[k];
            this.texturePrefix = this.texturePrefix + HORSE_TEXTURES_ABBR[k];
            if (l >= horseMarkingTextures.length) {
                this.field_175508_bO = false;
                return;
            }
            this.horseTexturesArray[1] = horseMarkingTextures[l];
            this.texturePrefix = this.texturePrefix + HORSE_MARKING_TEXTURES_ABBR[l];
        } else {
            this.horseTexturesArray[0] = "";
            this.texturePrefix = this.texturePrefix + "_" + i + "_";
        }
        int i1 = this.getHorseArmorIndexSynced();
        if (i1 >= horseArmorTextures.length) {
            this.field_175508_bO = false;
            return;
        }
        this.horseTexturesArray[2] = horseArmorTextures[i1];
        this.texturePrefix = this.texturePrefix + HORSE_ARMOR_TEXTURES_ABBR[i1];
        this.field_175508_bO = true;
    }

    public String getHorseTexture() {
        if (this.texturePrefix != null) return this.texturePrefix;
        this.setHorseTexturePaths();
        return this.texturePrefix;
    }

    public String[] getVariantTexturePaths() {
        if (this.texturePrefix != null) return this.horseTexturesArray;
        this.setHorseTexturePaths();
        return this.horseTexturesArray;
    }

    public void openGUI(EntityPlayer playerEntity) {
        if (this.worldObj.isRemote) return;
        if (this.riddenByEntity != null) {
            if (this.riddenByEntity != playerEntity) return;
        }
        if (!this.isTame()) return;
        this.horseChest.setCustomName(this.getName());
        playerEntity.displayGUIHorse(this, this.horseChest);
    }

    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack itemstack = player.inventory.getCurrentItem();
        if (itemstack != null && itemstack.getItem() == Items.spawn_egg) {
            return super.interact(player);
        }
        if (!this.isTame() && this.isUndead()) {
            return false;
        }
        if (this.isTame() && this.isAdultHorse() && player.isSneaking()) {
            this.openGUI(player);
            return true;
        }
        if (this.func_110253_bW() && this.riddenByEntity != null) {
            return super.interact(player);
        }
        if (itemstack != null) {
            boolean flag = false;
            if (this.canWearArmor()) {
                int i = -1;
                if (itemstack.getItem() == Items.iron_horse_armor) {
                    i = 1;
                } else if (itemstack.getItem() == Items.golden_horse_armor) {
                    i = 2;
                } else if (itemstack.getItem() == Items.diamond_horse_armor) {
                    i = 3;
                }
                if (i >= 0) {
                    if (!this.isTame()) {
                        this.makeHorseRearWithSound();
                        return true;
                    }
                    this.openGUI(player);
                    return true;
                }
            }
            if (!flag && !this.isUndead()) {
                float f = 0.0f;
                int j = 0;
                int k = 0;
                if (itemstack.getItem() == Items.wheat) {
                    f = 2.0f;
                    j = 20;
                    k = 3;
                } else if (itemstack.getItem() == Items.sugar) {
                    f = 1.0f;
                    j = 30;
                    k = 3;
                } else if (Block.getBlockFromItem(itemstack.getItem()) == Blocks.hay_block) {
                    f = 20.0f;
                    j = 180;
                } else if (itemstack.getItem() == Items.apple) {
                    f = 3.0f;
                    j = 60;
                    k = 3;
                } else if (itemstack.getItem() == Items.golden_carrot) {
                    f = 4.0f;
                    j = 60;
                    k = 5;
                    if (this.isTame() && this.getGrowingAge() == 0) {
                        flag = true;
                        this.setInLove(player);
                    }
                } else if (itemstack.getItem() == Items.golden_apple) {
                    f = 10.0f;
                    j = 240;
                    k = 10;
                    if (this.isTame() && this.getGrowingAge() == 0) {
                        flag = true;
                        this.setInLove(player);
                    }
                }
                if (this.getHealth() < this.getMaxHealth() && f > 0.0f) {
                    this.heal(f);
                    flag = true;
                }
                if (!this.isAdultHorse() && j > 0) {
                    this.addGrowth(j);
                    flag = true;
                }
                if (k > 0 && (flag || !this.isTame()) && k < this.getMaxTemper()) {
                    flag = true;
                    this.increaseTemper(k);
                }
                if (flag) {
                    this.func_110266_cB();
                }
            }
            if (!this.isTame() && !flag) {
                if (itemstack != null && itemstack.interactWithEntity(player, this)) {
                    return true;
                }
                this.makeHorseRearWithSound();
                return true;
            }
            if (!flag && this.canCarryChest() && !this.isChested() && itemstack.getItem() == Item.getItemFromBlock(Blocks.chest)) {
                this.setChested(true);
                this.playSound("mob.chickenplop", 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
                flag = true;
                this.initHorseChest();
            }
            if (!flag && this.func_110253_bW() && !this.isHorseSaddled() && itemstack.getItem() == Items.saddle) {
                this.openGUI(player);
                return true;
            }
            if (flag) {
                if (player.capabilities.isCreativeMode) return true;
                if (--itemstack.stackSize != 0) return true;
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                return true;
            }
        }
        if (!this.func_110253_bW()) return super.interact(player);
        if (this.riddenByEntity != null) return super.interact(player);
        if (itemstack != null && itemstack.interactWithEntity(player, this)) {
            return true;
        }
        this.mountTo(player);
        return true;
    }

    private void mountTo(EntityPlayer player) {
        player.rotationYaw = this.rotationYaw;
        player.rotationPitch = this.rotationPitch;
        this.setEatingHaystack(false);
        this.setRearing(false);
        if (this.worldObj.isRemote) return;
        player.mountEntity(this);
    }

    public boolean canWearArmor() {
        if (this.getHorseType() != 0) return false;
        return true;
    }

    public boolean canCarryChest() {
        int i = this.getHorseType();
        if (i == 2) return true;
        if (i == 1) return true;
        return false;
    }

    @Override
    protected boolean isMovementBlocked() {
        if (this.riddenByEntity != null && this.isHorseSaddled()) {
            return true;
        }
        if (this.isEatingHaystack()) return true;
        if (this.isRearing()) return true;
        return false;
    }

    public boolean isUndead() {
        int i = this.getHorseType();
        if (i == 3) return true;
        if (i == 4) return true;
        return false;
    }

    public boolean isSterile() {
        if (this.isUndead()) return true;
        if (this.getHorseType() == 2) return true;
        return false;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    private void func_110210_cH() {
        this.field_110278_bp = 1;
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (this.worldObj.isRemote) return;
        this.dropChestItems();
    }

    @Override
    public void onLivingUpdate() {
        if (this.rand.nextInt(200) == 0) {
            this.func_110210_cH();
        }
        super.onLivingUpdate();
        if (this.worldObj.isRemote) return;
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
        if (!this.isBreeding()) return;
        if (this.isAdultHorse()) return;
        if (this.isEatingHaystack()) return;
        EntityHorse entityhorse = this.getClosestHorse(this, 16.0);
        if (entityhorse == null) return;
        if (!(this.getDistanceSqToEntity(entityhorse) > 4.0)) return;
        this.navigator.getPathToEntityLiving(entityhorse);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.worldObj.isRemote && this.dataWatcher.hasObjectChanged()) {
            this.dataWatcher.func_111144_e();
            this.resetTexturePrefix();
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
            if (!(this.mouthOpenness > 1.0f)) return;
            this.mouthOpenness = 1.0f;
            return;
        }
        this.mouthOpenness += (0.0f - this.mouthOpenness) * 0.7f - 0.05f;
        if (!(this.mouthOpenness < 0.0f)) return;
        this.mouthOpenness = 0.0f;
    }

    private void openHorseMouth() {
        if (this.worldObj.isRemote) return;
        this.openMouthCounter = 1;
        this.setHorseWatchableBoolean(128, true);
    }

    private boolean canMate() {
        if (this.riddenByEntity != null) return false;
        if (this.ridingEntity != null) return false;
        if (!this.isTame()) return false;
        if (!this.isAdultHorse()) return false;
        if (this.isSterile()) return false;
        if (!(this.getHealth() >= this.getMaxHealth())) return false;
        if (!this.isInLove()) return false;
        return true;
    }

    @Override
    public void setEating(boolean eating) {
        this.setHorseWatchableBoolean(32, eating);
    }

    public void setEatingHaystack(boolean p_110227_1_) {
        this.setEating(p_110227_1_);
    }

    public void setRearing(boolean rearing) {
        if (rearing) {
            this.setEatingHaystack(false);
        }
        this.setHorseWatchableBoolean(64, rearing);
    }

    private void makeHorseRear() {
        if (this.worldObj.isRemote) return;
        this.jumpRearingCounter = 1;
        this.setRearing(true);
    }

    public void makeHorseRearWithSound() {
        this.makeHorseRear();
        String s = this.getAngrySoundName();
        if (s == null) return;
        this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
    }

    public void dropChestItems() {
        this.dropItemsInChest(this, this.horseChest);
        this.dropChests();
    }

    private void dropItemsInChest(Entity entityIn, AnimalChest animalChestIn) {
        if (animalChestIn == null) return;
        if (this.worldObj.isRemote) return;
        int i = 0;
        while (i < animalChestIn.getSizeInventory()) {
            ItemStack itemstack = animalChestIn.getStackInSlot(i);
            if (itemstack != null) {
                this.entityDropItem(itemstack, 0.0f);
            }
            ++i;
        }
    }

    public boolean setTamedBy(EntityPlayer player) {
        this.setOwnerId(player.getUniqueID().toString());
        this.setHorseTamed(true);
        return true;
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase && this.isHorseSaddled()) {
            this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
            this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5f;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            strafe = ((EntityLivingBase)this.riddenByEntity).moveStrafing * 0.5f;
            forward = ((EntityLivingBase)this.riddenByEntity).moveForward;
            if (forward <= 0.0f) {
                forward *= 0.25f;
                this.gallopTime = 0;
            }
            if (this.onGround && this.jumpPower == 0.0f && this.isRearing() && !this.field_110294_bI) {
                strafe = 0.0f;
                forward = 0.0f;
            }
            if (this.jumpPower > 0.0f && !this.isHorseJumping() && this.onGround) {
                this.motionY = this.getHorseJumpStrength() * (double)this.jumpPower;
                if (this.isPotionActive(Potion.jump)) {
                    this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
                }
                this.setHorseJumping(true);
                this.isAirBorne = true;
                if (forward > 0.0f) {
                    float f = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f);
                    float f1 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f);
                    this.motionX += (double)(-0.4f * f * this.jumpPower);
                    this.motionZ += (double)(0.4f * f1 * this.jumpPower);
                    this.playSound("mob.horse.jump", 0.4f, 1.0f);
                }
                this.jumpPower = 0.0f;
            }
            this.stepHeight = 1.0f;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1f;
            if (!this.worldObj.isRemote) {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                super.moveEntityWithHeading(strafe, forward);
            }
            if (this.onGround) {
                this.jumpPower = 0.0f;
                this.setHorseJumping(false);
            }
            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f2 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0f;
            if (f2 > 1.0f) {
                f2 = 1.0f;
            }
            this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4f;
            this.limbSwing += this.limbSwingAmount;
            return;
        }
        this.stepHeight = 0.5f;
        this.jumpMovementFactor = 0.02f;
        super.moveEntityWithHeading(strafe, forward);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("EatingHaystack", this.isEatingHaystack());
        tagCompound.setBoolean("ChestedHorse", this.isChested());
        tagCompound.setBoolean("HasReproduced", this.getHasReproduced());
        tagCompound.setBoolean("Bred", this.isBreeding());
        tagCompound.setInteger("Type", this.getHorseType());
        tagCompound.setInteger("Variant", this.getHorseVariant());
        tagCompound.setInteger("Temper", this.getTemper());
        tagCompound.setBoolean("Tame", this.isTame());
        tagCompound.setString("OwnerUUID", this.getOwnerId());
        if (this.isChested()) {
            NBTTagList nbttaglist = new NBTTagList();
            for (int i = 2; i < this.horseChest.getSizeInventory(); ++i) {
                ItemStack itemstack = this.horseChest.getStackInSlot(i);
                if (itemstack == null) continue;
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
            tagCompound.setTag("Items", nbttaglist);
        }
        if (this.horseChest.getStackInSlot(1) != null) {
            tagCompound.setTag("ArmorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }
        if (this.horseChest.getStackInSlot(0) == null) return;
        tagCompound.setTag("SaddleItem", this.horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        ItemStack itemstack;
        IAttributeInstance iattributeinstance;
        super.readEntityFromNBT(tagCompund);
        this.setEatingHaystack(tagCompund.getBoolean("EatingHaystack"));
        this.setBreeding(tagCompund.getBoolean("Bred"));
        this.setChested(tagCompund.getBoolean("ChestedHorse"));
        this.setHasReproduced(tagCompund.getBoolean("HasReproduced"));
        this.setHorseType(tagCompund.getInteger("Type"));
        this.setHorseVariant(tagCompund.getInteger("Variant"));
        this.setTemper(tagCompund.getInteger("Temper"));
        this.setHorseTamed(tagCompund.getBoolean("Tame"));
        String s = "";
        if (tagCompund.hasKey("OwnerUUID", 8)) {
            s = tagCompund.getString("OwnerUUID");
        } else {
            String s1 = tagCompund.getString("Owner");
            s = PreYggdrasilConverter.getStringUUIDFromName(s1);
        }
        if (s.length() > 0) {
            this.setOwnerId(s);
        }
        if ((iattributeinstance = this.getAttributeMap().getAttributeInstanceByName("Speed")) != null) {
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(iattributeinstance.getBaseValue() * 0.25);
        }
        if (this.isChested()) {
            NBTTagList nbttaglist = tagCompund.getTagList("Items", 10);
            this.initHorseChest();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 0xFF;
                if (j < 2 || j >= this.horseChest.getSizeInventory()) continue;
                this.horseChest.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
            }
        }
        if (tagCompund.hasKey("ArmorItem", 10) && (itemstack = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("ArmorItem"))) != null && EntityHorse.isArmorItem(itemstack.getItem())) {
            this.horseChest.setInventorySlotContents(1, itemstack);
        }
        if (tagCompund.hasKey("SaddleItem", 10)) {
            ItemStack itemstack1 = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("SaddleItem"));
            if (itemstack1 != null && itemstack1.getItem() == Items.saddle) {
                this.horseChest.setInventorySlotContents(0, itemstack1);
            }
        } else if (tagCompund.getBoolean("Saddle")) {
            this.horseChest.setInventorySlotContents(0, new ItemStack(Items.saddle));
        }
        this.updateHorseSlots();
    }

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal) {
        int j;
        if (otherAnimal == this) {
            return false;
        }
        if (otherAnimal.getClass() != this.getClass()) {
            return false;
        }
        EntityHorse entityhorse = (EntityHorse)otherAnimal;
        if (!this.canMate()) return false;
        if (!entityhorse.canMate()) return false;
        int i = this.getHorseType();
        if (i == (j = entityhorse.getHorseType())) return true;
        if (i == 0) {
            if (j == 1) return true;
        }
        if (i != 1) return false;
        if (j != 0) return false;
        return true;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        EntityHorse entityhorse = (EntityHorse)ageable;
        EntityHorse entityhorse1 = new EntityHorse(this.worldObj);
        int i = this.getHorseType();
        int j = entityhorse.getHorseType();
        int k = 0;
        if (i == j) {
            k = i;
        } else if (i == 0 && j == 1 || i == 1 && j == 0) {
            k = 2;
        }
        if (k == 0) {
            int i1 = this.rand.nextInt(9);
            int l = i1 < 4 ? this.getHorseVariant() & 0xFF : (i1 < 8 ? entityhorse.getHorseVariant() & 0xFF : this.rand.nextInt(7));
            int j1 = this.rand.nextInt(5);
            l = j1 < 2 ? (l |= this.getHorseVariant() & 0xFF00) : (j1 < 4 ? (l |= entityhorse.getHorseVariant() & 0xFF00) : (l |= this.rand.nextInt(5) << 8 & 0xFF00));
            entityhorse1.setHorseVariant(l);
        }
        entityhorse1.setHorseType(k);
        double d1 = this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + ageable.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + (double)this.getModifiedMaxHealth();
        entityhorse1.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(d1 / 3.0);
        double d2 = this.getEntityAttribute(horseJumpStrength).getBaseValue() + ageable.getEntityAttribute(horseJumpStrength).getBaseValue() + this.getModifiedJumpStrength();
        entityhorse1.getEntityAttribute(horseJumpStrength).setBaseValue(d2 / 3.0);
        double d0 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + ageable.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + this.getModifiedMovementSpeed();
        entityhorse1.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(d0 / 3.0);
        return entityhorse1;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        int i = 0;
        int j = 0;
        if (livingdata instanceof GroupData) {
            i = ((GroupData)livingdata).horseType;
            j = ((GroupData)livingdata).horseVariant & 0xFF | this.rand.nextInt(5) << 8;
        } else {
            if (this.rand.nextInt(10) == 0) {
                i = 1;
            } else {
                int k = this.rand.nextInt(7);
                int l = this.rand.nextInt(5);
                i = 0;
                j = k | l << 8;
            }
            livingdata = new GroupData(i, j);
        }
        this.setHorseType(i);
        this.setHorseVariant(j);
        if (this.rand.nextInt(5) == 0) {
            this.setGrowingAge(-24000);
        }
        if (i != 4 && i != 3) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.getModifiedMaxHealth());
            if (i == 0) {
                this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.getModifiedMovementSpeed());
            } else {
                this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.175f);
            }
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(15.0);
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2f);
        }
        if (i != 2 && i != 1) {
            this.getEntityAttribute(horseJumpStrength).setBaseValue(this.getModifiedJumpStrength());
        } else {
            this.getEntityAttribute(horseJumpStrength).setBaseValue(0.5);
        }
        this.setHealth(this.getMaxHealth());
        return livingdata;
    }

    public float getGrassEatingAmount(float p_110258_1_) {
        return this.prevHeadLean + (this.headLean - this.prevHeadLean) * p_110258_1_;
    }

    public float getRearingAmount(float p_110223_1_) {
        return this.prevRearingAmount + (this.rearingAmount - this.prevRearingAmount) * p_110223_1_;
    }

    public float getMouthOpennessAngle(float p_110201_1_) {
        return this.prevMouthOpenness + (this.mouthOpenness - this.prevMouthOpenness) * p_110201_1_;
    }

    public void setJumpPower(int jumpPowerIn) {
        if (!this.isHorseSaddled()) return;
        if (jumpPowerIn < 0) {
            jumpPowerIn = 0;
        } else {
            this.field_110294_bI = true;
            this.makeHorseRear();
        }
        if (jumpPowerIn >= 90) {
            this.jumpPower = 1.0f;
            return;
        }
        this.jumpPower = 0.4f + 0.4f * (float)jumpPowerIn / 90.0f;
    }

    protected void spawnHorseParticles(boolean p_110216_1_) {
        EnumParticleTypes enumparticletypes = p_110216_1_ ? EnumParticleTypes.HEART : EnumParticleTypes.SMOKE_NORMAL;
        int i = 0;
        while (i < 7) {
            double d0 = this.rand.nextGaussian() * 0.02;
            double d1 = this.rand.nextGaussian() * 0.02;
            double d2 = this.rand.nextGaussian() * 0.02;
            this.worldObj.spawnParticle(enumparticletypes, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, d0, d1, d2, new int[0]);
            ++i;
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 7) {
            this.spawnHorseParticles(true);
            return;
        }
        if (id == 6) {
            this.spawnHorseParticles(false);
            return;
        }
        super.handleStatusUpdate(id);
    }

    @Override
    public void updateRiderPosition() {
        super.updateRiderPosition();
        if (!(this.prevRearingAmount > 0.0f)) return;
        float f = MathHelper.sin(this.renderYawOffset * (float)Math.PI / 180.0f);
        float f1 = MathHelper.cos(this.renderYawOffset * (float)Math.PI / 180.0f);
        float f2 = 0.7f * this.prevRearingAmount;
        float f3 = 0.15f * this.prevRearingAmount;
        this.riddenByEntity.setPosition(this.posX + (double)(f2 * f), this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset() + (double)f3, this.posZ - (double)(f2 * f1));
        if (!(this.riddenByEntity instanceof EntityLivingBase)) return;
        ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
    }

    private float getModifiedMaxHealth() {
        return 15.0f + (float)this.rand.nextInt(8) + (float)this.rand.nextInt(9);
    }

    private double getModifiedJumpStrength() {
        return (double)0.4f + this.rand.nextDouble() * 0.2 + this.rand.nextDouble() * 0.2 + this.rand.nextDouble() * 0.2;
    }

    private double getModifiedMovementSpeed() {
        return ((double)0.45f + this.rand.nextDouble() * 0.3 + this.rand.nextDouble() * 0.3 + this.rand.nextDouble() * 0.3) * 0.25;
    }

    public static boolean isArmorItem(Item p_146085_0_) {
        if (p_146085_0_ == Items.iron_horse_armor) return true;
        if (p_146085_0_ == Items.golden_horse_armor) return true;
        if (p_146085_0_ == Items.diamond_horse_armor) return true;
        return false;
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
    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        int i;
        if (inventorySlot == 499 && this.canCarryChest()) {
            if (itemStackIn == null && this.isChested()) {
                this.setChested(false);
                this.initHorseChest();
                return true;
            }
            if (itemStackIn != null && itemStackIn.getItem() == Item.getItemFromBlock(Blocks.chest) && !this.isChested()) {
                this.setChested(true);
                this.initHorseChest();
                return true;
            }
        }
        if ((i = inventorySlot - 400) >= 0 && i < 2 && i < this.horseChest.getSizeInventory()) {
            if (i == 0 && itemStackIn != null && itemStackIn.getItem() != Items.saddle) {
                return false;
            }
            if (i == 1) {
                if (itemStackIn != null) {
                    if (!EntityHorse.isArmorItem(itemStackIn.getItem())) return false;
                }
                if (!this.canWearArmor()) return false;
            }
            this.horseChest.setInventorySlotContents(i, itemStackIn);
            this.updateHorseSlots();
            return true;
        }
        int j = inventorySlot - 500 + 2;
        if (j < 2) return false;
        if (j >= this.horseChest.getSizeInventory()) return false;
        this.horseChest.setInventorySlotContents(j, itemStackIn);
        return true;
    }

    public static class GroupData
    implements IEntityLivingData {
        public int horseType;
        public int horseVariant;

        public GroupData(int type, int variant) {
            this.horseType = type;
            this.horseVariant = variant;
        }
    }
}

