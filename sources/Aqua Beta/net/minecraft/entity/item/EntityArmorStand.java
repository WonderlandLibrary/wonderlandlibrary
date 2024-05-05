package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotations;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityArmorStand
extends EntityLivingBase {
    private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0f, 0.0f, 0.0f);
    private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0f, 0.0f, 0.0f);
    private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0f, 0.0f, -10.0f);
    private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0f, 0.0f, 10.0f);
    private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0f, 0.0f, -1.0f);
    private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0f, 0.0f, 1.0f);
    private final ItemStack[] contents = new ItemStack[5];
    private boolean canInteract;
    private long punchCooldown;
    private int disabledSlots;
    private boolean field_181028_bj;
    private Rotations headRotation = DEFAULT_HEAD_ROTATION;
    private Rotations bodyRotation = DEFAULT_BODY_ROTATION;
    private Rotations leftArmRotation = DEFAULT_LEFTARM_ROTATION;
    private Rotations rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
    private Rotations leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
    private Rotations rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;

    public EntityArmorStand(World worldIn) {
        super(worldIn);
        this.setSilent(true);
        this.noClip = this.hasNoGravity();
        this.setSize(0.5f, 1.975f);
    }

    public EntityArmorStand(World worldIn, double posX, double posY, double posZ) {
        this(worldIn);
        this.setPosition(posX, posY, posZ);
    }

    public boolean isServerWorld() {
        return super.isServerWorld() && !this.hasNoGravity();
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(10, (Object)0);
        this.dataWatcher.addObject(11, (Object)DEFAULT_HEAD_ROTATION);
        this.dataWatcher.addObject(12, (Object)DEFAULT_BODY_ROTATION);
        this.dataWatcher.addObject(13, (Object)DEFAULT_LEFTARM_ROTATION);
        this.dataWatcher.addObject(14, (Object)DEFAULT_RIGHTARM_ROTATION);
        this.dataWatcher.addObject(15, (Object)DEFAULT_LEFTLEG_ROTATION);
        this.dataWatcher.addObject(16, (Object)DEFAULT_RIGHTLEG_ROTATION);
    }

    public ItemStack getHeldItem() {
        return this.contents[0];
    }

    public ItemStack getEquipmentInSlot(int slotIn) {
        return this.contents[slotIn];
    }

    public ItemStack getCurrentArmor(int slotIn) {
        return this.contents[slotIn + 1];
    }

    public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
        this.contents[slotIn] = stack;
    }

    public ItemStack[] getInventory() {
        return this.contents;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        int i;
        if (inventorySlot == 99) {
            i = 0;
        } else {
            i = inventorySlot - 100 + 1;
            if (i < 0 || i >= this.contents.length) {
                return false;
            }
        }
        if (!(itemStackIn == null || EntityLiving.getArmorPosition((ItemStack)itemStackIn) == i || i == 4 && itemStackIn.getItem() instanceof ItemBlock)) {
            return false;
        }
        this.setCurrentItemOrArmor(i, itemStackIn);
        return true;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.contents.length; ++i) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            if (this.contents[i] != null) {
                this.contents[i].writeToNBT(nbttagcompound);
            }
            nbttaglist.appendTag((NBTBase)nbttagcompound);
        }
        tagCompound.setTag("Equipment", (NBTBase)nbttaglist);
        if (this.getAlwaysRenderNameTag() && (this.getCustomNameTag() == null || this.getCustomNameTag().length() == 0)) {
            tagCompound.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
        }
        tagCompound.setBoolean("Invisible", this.isInvisible());
        tagCompound.setBoolean("Small", this.isSmall());
        tagCompound.setBoolean("ShowArms", this.getShowArms());
        tagCompound.setInteger("DisabledSlots", this.disabledSlots);
        tagCompound.setBoolean("NoGravity", this.hasNoGravity());
        tagCompound.setBoolean("NoBasePlate", this.hasNoBasePlate());
        if (this.hasMarker()) {
            tagCompound.setBoolean("Marker", this.hasMarker());
        }
        tagCompound.setTag("Pose", (NBTBase)this.readPoseFromNBT());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("Equipment", 9)) {
            NBTTagList nbttaglist = tagCompund.getTagList("Equipment", 10);
            for (int i = 0; i < this.contents.length; ++i) {
                this.contents[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbttaglist.getCompoundTagAt(i));
            }
        }
        this.setInvisible(tagCompund.getBoolean("Invisible"));
        this.setSmall(tagCompund.getBoolean("Small"));
        this.setShowArms(tagCompund.getBoolean("ShowArms"));
        this.disabledSlots = tagCompund.getInteger("DisabledSlots");
        this.setNoGravity(tagCompund.getBoolean("NoGravity"));
        this.setNoBasePlate(tagCompund.getBoolean("NoBasePlate"));
        this.setMarker(tagCompund.getBoolean("Marker"));
        this.field_181028_bj = !this.hasMarker();
        this.noClip = this.hasNoGravity();
        NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Pose");
        this.writePoseToNBT(nbttagcompound);
    }

    private void writePoseToNBT(NBTTagCompound tagCompound) {
        NBTTagList nbttaglist = tagCompound.getTagList("Head", 5);
        if (nbttaglist.tagCount() > 0) {
            this.setHeadRotation(new Rotations(nbttaglist));
        } else {
            this.setHeadRotation(DEFAULT_HEAD_ROTATION);
        }
        NBTTagList nbttaglist1 = tagCompound.getTagList("Body", 5);
        if (nbttaglist1.tagCount() > 0) {
            this.setBodyRotation(new Rotations(nbttaglist1));
        } else {
            this.setBodyRotation(DEFAULT_BODY_ROTATION);
        }
        NBTTagList nbttaglist2 = tagCompound.getTagList("LeftArm", 5);
        if (nbttaglist2.tagCount() > 0) {
            this.setLeftArmRotation(new Rotations(nbttaglist2));
        } else {
            this.setLeftArmRotation(DEFAULT_LEFTARM_ROTATION);
        }
        NBTTagList nbttaglist3 = tagCompound.getTagList("RightArm", 5);
        if (nbttaglist3.tagCount() > 0) {
            this.setRightArmRotation(new Rotations(nbttaglist3));
        } else {
            this.setRightArmRotation(DEFAULT_RIGHTARM_ROTATION);
        }
        NBTTagList nbttaglist4 = tagCompound.getTagList("LeftLeg", 5);
        if (nbttaglist4.tagCount() > 0) {
            this.setLeftLegRotation(new Rotations(nbttaglist4));
        } else {
            this.setLeftLegRotation(DEFAULT_LEFTLEG_ROTATION);
        }
        NBTTagList nbttaglist5 = tagCompound.getTagList("RightLeg", 5);
        if (nbttaglist5.tagCount() > 0) {
            this.setRightLegRotation(new Rotations(nbttaglist5));
        } else {
            this.setRightLegRotation(DEFAULT_RIGHTLEG_ROTATION);
        }
    }

    private NBTTagCompound readPoseFromNBT() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        if (!DEFAULT_HEAD_ROTATION.equals((Object)this.headRotation)) {
            nbttagcompound.setTag("Head", (NBTBase)this.headRotation.writeToNBT());
        }
        if (!DEFAULT_BODY_ROTATION.equals((Object)this.bodyRotation)) {
            nbttagcompound.setTag("Body", (NBTBase)this.bodyRotation.writeToNBT());
        }
        if (!DEFAULT_LEFTARM_ROTATION.equals((Object)this.leftArmRotation)) {
            nbttagcompound.setTag("LeftArm", (NBTBase)this.leftArmRotation.writeToNBT());
        }
        if (!DEFAULT_RIGHTARM_ROTATION.equals((Object)this.rightArmRotation)) {
            nbttagcompound.setTag("RightArm", (NBTBase)this.rightArmRotation.writeToNBT());
        }
        if (!DEFAULT_LEFTLEG_ROTATION.equals((Object)this.leftLegRotation)) {
            nbttagcompound.setTag("LeftLeg", (NBTBase)this.leftLegRotation.writeToNBT());
        }
        if (!DEFAULT_RIGHTLEG_ROTATION.equals((Object)this.rightLegRotation)) {
            nbttagcompound.setTag("RightLeg", (NBTBase)this.rightLegRotation.writeToNBT());
        }
        return nbttagcompound;
    }

    public boolean canBePushed() {
        return false;
    }

    protected void collideWithEntity(Entity entityIn) {
    }

    protected void collideWithNearbyEntities() {
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)this, this.getEntityBoundingBox());
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity)list.get(i);
                if (!(entity instanceof EntityMinecart) || ((EntityMinecart)entity).getMinecartType() != EntityMinecart.EnumMinecartType.RIDEABLE || !(this.getDistanceSqToEntity(entity) <= 0.2)) continue;
                entity.applyEntityCollision((Entity)this);
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    public boolean interactAt(EntityPlayer player, Vec3 targetVec3) {
        block20: {
            block22: {
                block23: {
                    block21: {
                        if (this.hasMarker()) {
                            return false;
                        }
                        if (this.worldObj.isRemote || player.isSpectator()) break block20;
                        i = 0;
                        itemstack = player.getCurrentEquippedItem();
                        v0 = flag = itemstack != null;
                        if (flag && itemstack.getItem() instanceof ItemArmor) {
                            itemarmor = (ItemArmor)itemstack.getItem();
                            if (itemarmor.armorType == 3) {
                                i = 1;
                            } else if (itemarmor.armorType == 2) {
                                i = 2;
                            } else if (itemarmor.armorType == 1) {
                                i = 3;
                            } else if (itemarmor.armorType == 0) {
                                i = 4;
                            }
                        }
                        if (flag && (itemstack.getItem() == Items.skull || itemstack.getItem() == Item.getItemFromBlock((Block)Blocks.pumpkin))) {
                            i = 4;
                        }
                        d4 = 0.1;
                        d0 = 0.9;
                        d1 = 0.4;
                        d2 = 1.6;
                        j = 0;
                        flag1 = this.isSmall();
                        v1 = d3 = flag1 != false ? targetVec3.yCoord * 2.0 : targetVec3.yCoord;
                        if (!(d3 >= 0.1)) break block21;
                        v2 = flag1 != false ? 0.8 : 0.45;
                        if (!(d3 < 0.1 + v2) || this.contents[1] == null) break block21;
                        j = 1;
                        break block22;
                    }
                    v3 = flag1 != false ? 0.3 : 0.0;
                    if (!(d3 >= 0.9 + v3)) break block23;
                    v4 = flag1 != false ? 1.0 : 0.7;
                    if (!(d3 < 0.9 + v4) || this.contents[3] == null) break block23;
                    j = 3;
                    break block22;
                }
                if (!(d3 >= 0.4)) ** GOTO lbl-1000
                v5 = flag1 != false ? 1.0 : 0.8;
                if (d3 < 0.4 + v5 && this.contents[2] != null) {
                    j = 2;
                } else if (d3 >= 1.6 && this.contents[4] != null) {
                    j = 4;
                }
            }
            v6 = flag2 = this.contents[j] != null;
            if ((this.disabledSlots & 1 << j) != 0 || (this.disabledSlots & 1 << i) != 0) {
                j = i;
                if ((this.disabledSlots & 1 << i) != 0) {
                    if ((this.disabledSlots & 1) != 0) {
                        return true;
                    }
                    j = 0;
                }
            }
            if (flag && i == 0 && !this.getShowArms()) {
                return true;
            }
            if (flag) {
                this.func_175422_a(player, i);
            } else if (flag2) {
                this.func_175422_a(player, j);
            }
            return true;
        }
        return true;
    }

    private void func_175422_a(EntityPlayer p_175422_1_, int p_175422_2_) {
        ItemStack itemstack = this.contents[p_175422_2_];
        if (!(itemstack != null && (this.disabledSlots & 1 << p_175422_2_ + 8) != 0 || itemstack == null && (this.disabledSlots & 1 << p_175422_2_ + 16) != 0)) {
            int i = p_175422_1_.inventory.currentItem;
            ItemStack itemstack1 = p_175422_1_.inventory.getStackInSlot(i);
            if (p_175422_1_.capabilities.isCreativeMode && (itemstack == null || itemstack.getItem() == Item.getItemFromBlock((Block)Blocks.air)) && itemstack1 != null) {
                ItemStack itemstack3 = itemstack1.copy();
                itemstack3.stackSize = 1;
                this.setCurrentItemOrArmor(p_175422_2_, itemstack3);
            } else if (itemstack1 != null && itemstack1.stackSize > 1) {
                if (itemstack == null) {
                    ItemStack itemstack2 = itemstack1.copy();
                    itemstack2.stackSize = 1;
                    this.setCurrentItemOrArmor(p_175422_2_, itemstack2);
                    --itemstack1.stackSize;
                }
            } else {
                this.setCurrentItemOrArmor(p_175422_2_, itemstack1);
                p_175422_1_.inventory.setInventorySlotContents(i, itemstack);
            }
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.worldObj.isRemote) {
            return false;
        }
        if (DamageSource.outOfWorld.equals((Object)source)) {
            this.setDead();
            return false;
        }
        if (!(this.isEntityInvulnerable(source) || this.canInteract || this.hasMarker())) {
            if (source.isExplosion()) {
                this.dropContents();
                this.setDead();
                return false;
            }
            if (DamageSource.inFire.equals((Object)source)) {
                if (!this.isBurning()) {
                    this.setFire(5);
                } else {
                    this.damageArmorStand(0.15f);
                }
                return false;
            }
            if (DamageSource.onFire.equals((Object)source) && this.getHealth() > 0.5f) {
                this.damageArmorStand(4.0f);
                return false;
            }
            boolean flag = "arrow".equals((Object)source.getDamageType());
            boolean flag1 = "player".equals((Object)source.getDamageType());
            if (!flag1 && !flag) {
                return false;
            }
            if (source.getSourceOfDamage() instanceof EntityArrow) {
                source.getSourceOfDamage().setDead();
            }
            if (source.getEntity() instanceof EntityPlayer && !((EntityPlayer)source.getEntity()).capabilities.allowEdit) {
                return false;
            }
            if (source.isCreativePlayer()) {
                this.playParticles();
                this.setDead();
                return false;
            }
            long i = this.worldObj.getTotalWorldTime();
            if (i - this.punchCooldown > 5L && !flag) {
                this.punchCooldown = i;
            } else {
                this.dropBlock();
                this.playParticles();
                this.setDead();
            }
            return false;
        }
        return false;
    }

    public boolean isInRangeToRenderDist(double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        if (Double.isNaN((double)d0) || d0 == 0.0) {
            d0 = 4.0;
        }
        return distance < (d0 *= 64.0) * d0;
    }

    private void playParticles() {
        if (this.worldObj instanceof WorldServer) {
            ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double)this.height / 1.5, this.posZ, 10, (double)(this.width / 4.0f), (double)(this.height / 4.0f), (double)(this.width / 4.0f), 0.05, new int[]{Block.getStateId((IBlockState)Blocks.planks.getDefaultState())});
        }
    }

    private void damageArmorStand(float p_175406_1_) {
        float f = this.getHealth();
        if ((f -= p_175406_1_) <= 0.5f) {
            this.dropContents();
            this.setDead();
        } else {
            this.setHealth(f);
        }
    }

    private void dropBlock() {
        Block.spawnAsEntity((World)this.worldObj, (BlockPos)new BlockPos((Entity)this), (ItemStack)new ItemStack((Item)Items.armor_stand));
        this.dropContents();
    }

    private void dropContents() {
        for (int i = 0; i < this.contents.length; ++i) {
            if (this.contents[i] == null || this.contents[i].stackSize <= 0) continue;
            if (this.contents[i] != null) {
                Block.spawnAsEntity((World)this.worldObj, (BlockPos)new BlockPos((Entity)this).up(), (ItemStack)this.contents[i]);
            }
            this.contents[i] = null;
        }
    }

    protected float updateDistance(float p_110146_1_, float p_110146_2_) {
        this.prevRenderYawOffset = this.prevRotationYaw;
        this.renderYawOffset = this.rotationYaw;
        return 0.0f;
    }

    public float getEyeHeight() {
        return this.isChild() ? this.height * 0.5f : this.height * 0.9f;
    }

    public void moveEntityWithHeading(float strafe, float forward) {
        if (!this.hasNoGravity()) {
            super.moveEntityWithHeading(strafe, forward);
        }
    }

    public void onUpdate() {
        Rotations rotations5;
        Rotations rotations4;
        Rotations rotations3;
        Rotations rotations2;
        Rotations rotations1;
        super.onUpdate();
        Rotations rotations = this.dataWatcher.getWatchableObjectRotations(11);
        if (!this.headRotation.equals((Object)rotations)) {
            this.setHeadRotation(rotations);
        }
        if (!this.bodyRotation.equals((Object)(rotations1 = this.dataWatcher.getWatchableObjectRotations(12)))) {
            this.setBodyRotation(rotations1);
        }
        if (!this.leftArmRotation.equals((Object)(rotations2 = this.dataWatcher.getWatchableObjectRotations(13)))) {
            this.setLeftArmRotation(rotations2);
        }
        if (!this.rightArmRotation.equals((Object)(rotations3 = this.dataWatcher.getWatchableObjectRotations(14)))) {
            this.setRightArmRotation(rotations3);
        }
        if (!this.leftLegRotation.equals((Object)(rotations4 = this.dataWatcher.getWatchableObjectRotations(15)))) {
            this.setLeftLegRotation(rotations4);
        }
        if (!this.rightLegRotation.equals((Object)(rotations5 = this.dataWatcher.getWatchableObjectRotations(16)))) {
            this.setRightLegRotation(rotations5);
        }
        boolean flag = this.hasMarker();
        if (!this.field_181028_bj && flag) {
            this.func_181550_a(false);
        } else {
            if (!this.field_181028_bj || flag) {
                return;
            }
            this.func_181550_a(true);
        }
        this.field_181028_bj = flag;
    }

    private void func_181550_a(boolean p_181550_1_) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        if (p_181550_1_) {
            this.setSize(0.5f, 1.975f);
        } else {
            this.setSize(0.0f, 0.0f);
        }
        this.setPosition(d0, d1, d2);
    }

    protected void updatePotionMetadata() {
        this.setInvisible(this.canInteract);
    }

    public void setInvisible(boolean invisible) {
        this.canInteract = invisible;
        super.setInvisible(invisible);
    }

    public boolean isChild() {
        return this.isSmall();
    }

    public void onKillCommand() {
        this.setDead();
    }

    public boolean isImmuneToExplosions() {
        return this.isInvisible();
    }

    private void setSmall(boolean p_175420_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);
        b0 = p_175420_1_ ? (byte)(b0 | 1) : (byte)(b0 & 0xFFFFFFFE);
        this.dataWatcher.updateObject(10, (Object)b0);
    }

    public boolean isSmall() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 1) != 0;
    }

    private void setNoGravity(boolean p_175425_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);
        b0 = p_175425_1_ ? (byte)(b0 | 2) : (byte)(b0 & 0xFFFFFFFD);
        this.dataWatcher.updateObject(10, (Object)b0);
    }

    public boolean hasNoGravity() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 2) != 0;
    }

    private void setShowArms(boolean p_175413_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);
        b0 = p_175413_1_ ? (byte)(b0 | 4) : (byte)(b0 & 0xFFFFFFFB);
        this.dataWatcher.updateObject(10, (Object)b0);
    }

    public boolean getShowArms() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 4) != 0;
    }

    private void setNoBasePlate(boolean p_175426_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);
        b0 = p_175426_1_ ? (byte)(b0 | 8) : (byte)(b0 & 0xFFFFFFF7);
        this.dataWatcher.updateObject(10, (Object)b0);
    }

    public boolean hasNoBasePlate() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 8) != 0;
    }

    private void setMarker(boolean p_181027_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);
        b0 = p_181027_1_ ? (byte)(b0 | 0x10) : (byte)(b0 & 0xFFFFFFEF);
        this.dataWatcher.updateObject(10, (Object)b0);
    }

    public boolean hasMarker() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 0x10) != 0;
    }

    public void setHeadRotation(Rotations p_175415_1_) {
        this.headRotation = p_175415_1_;
        this.dataWatcher.updateObject(11, (Object)p_175415_1_);
    }

    public void setBodyRotation(Rotations p_175424_1_) {
        this.bodyRotation = p_175424_1_;
        this.dataWatcher.updateObject(12, (Object)p_175424_1_);
    }

    public void setLeftArmRotation(Rotations p_175405_1_) {
        this.leftArmRotation = p_175405_1_;
        this.dataWatcher.updateObject(13, (Object)p_175405_1_);
    }

    public void setRightArmRotation(Rotations p_175428_1_) {
        this.rightArmRotation = p_175428_1_;
        this.dataWatcher.updateObject(14, (Object)p_175428_1_);
    }

    public void setLeftLegRotation(Rotations p_175417_1_) {
        this.leftLegRotation = p_175417_1_;
        this.dataWatcher.updateObject(15, (Object)p_175417_1_);
    }

    public void setRightLegRotation(Rotations p_175427_1_) {
        this.rightLegRotation = p_175427_1_;
        this.dataWatcher.updateObject(16, (Object)p_175427_1_);
    }

    public Rotations getHeadRotation() {
        return this.headRotation;
    }

    public Rotations getBodyRotation() {
        return this.bodyRotation;
    }

    public Rotations getLeftArmRotation() {
        return this.leftArmRotation;
    }

    public Rotations getRightArmRotation() {
        return this.rightArmRotation;
    }

    public Rotations getLeftLegRotation() {
        return this.leftLegRotation;
    }

    public Rotations getRightLegRotation() {
        return this.rightLegRotation;
    }

    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith() && !this.hasMarker();
    }
}
