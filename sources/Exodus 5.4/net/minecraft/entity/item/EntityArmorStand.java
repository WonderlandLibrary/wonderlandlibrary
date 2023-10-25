/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
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
    private boolean canInteract;
    private int disabledSlots;
    private Rotations leftArmRotation;
    private Rotations headRotation;
    private static final Rotations DEFAULT_LEFTLEG_ROTATION;
    private long punchCooldown;
    private final ItemStack[] contents = new ItemStack[5];
    private static final Rotations DEFAULT_LEFTARM_ROTATION;
    private Rotations rightArmRotation;
    private Rotations bodyRotation;
    private static final Rotations DEFAULT_HEAD_ROTATION;
    private static final Rotations DEFAULT_RIGHTARM_ROTATION;
    private Rotations leftLegRotation;
    private boolean field_181028_bj;
    private static final Rotations DEFAULT_RIGHTLEG_ROTATION;
    private static final Rotations DEFAULT_BODY_ROTATION;
    private Rotations rightLegRotation;

    private void setNoBasePlate(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(10);
        by = bl ? (byte)(by | 8) : (byte)(by & 0xFFFFFFF7);
        this.dataWatcher.updateObject(10, by);
    }

    @Override
    public void onUpdate() {
        Rotations rotations;
        Rotations rotations2;
        Rotations rotations3;
        Rotations rotations4;
        Rotations rotations5;
        super.onUpdate();
        Rotations rotations6 = this.dataWatcher.getWatchableObjectRotations(11);
        if (!this.headRotation.equals(rotations6)) {
            this.setHeadRotation(rotations6);
        }
        if (!this.bodyRotation.equals(rotations5 = this.dataWatcher.getWatchableObjectRotations(12))) {
            this.setBodyRotation(rotations5);
        }
        if (!this.leftArmRotation.equals(rotations4 = this.dataWatcher.getWatchableObjectRotations(13))) {
            this.setLeftArmRotation(rotations4);
        }
        if (!this.rightArmRotation.equals(rotations3 = this.dataWatcher.getWatchableObjectRotations(14))) {
            this.setRightArmRotation(rotations3);
        }
        if (!this.leftLegRotation.equals(rotations2 = this.dataWatcher.getWatchableObjectRotations(15))) {
            this.setLeftLegRotation(rotations2);
        }
        if (!this.rightLegRotation.equals(rotations = this.dataWatcher.getWatchableObjectRotations(16))) {
            this.setRightLegRotation(rotations);
        }
        boolean bl = this.func_181026_s();
        if (!this.field_181028_bj && bl) {
            this.func_181550_a(false);
        } else {
            if (!this.field_181028_bj || bl) {
                return;
            }
            this.func_181550_a(true);
        }
        this.field_181028_bj = bl;
    }

    public void setLeftLegRotation(Rotations rotations) {
        this.leftLegRotation = rotations;
        this.dataWatcher.updateObject(15, rotations);
    }

    @Override
    public boolean isChild() {
        return this.isSmall();
    }

    @Override
    public boolean isInRangeToRenderDist(double d) {
        double d2 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        if (Double.isNaN(d2) || d2 == 0.0) {
            d2 = 4.0;
        }
        return d < (d2 *= 64.0) * d2;
    }

    @Override
    protected void collideWithNearbyEntities() {
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
        if (list != null && !list.isEmpty()) {
            int n = 0;
            while (n < list.size()) {
                Entity entity = list.get(n);
                if (entity instanceof EntityMinecart && ((EntityMinecart)entity).getMinecartType() == EntityMinecart.EnumMinecartType.RIDEABLE && this.getDistanceSqToEntity(entity) <= 0.2) {
                    entity.applyEntityCollision(this);
                }
                ++n;
            }
        }
    }

    @Override
    public void setInvisible(boolean bl) {
        this.canInteract = bl;
        super.setInvisible(bl);
    }

    public boolean hasNoBasePlate() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 8) != 0;
    }

    private void func_175422_a(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = this.contents[n];
        if (!(itemStack != null && (this.disabledSlots & 1 << n + 8) != 0 || itemStack == null && (this.disabledSlots & 1 << n + 16) != 0)) {
            int n2 = entityPlayer.inventory.currentItem;
            ItemStack itemStack2 = entityPlayer.inventory.getStackInSlot(n2);
            if (entityPlayer.capabilities.isCreativeMode && (itemStack == null || itemStack.getItem() == Item.getItemFromBlock(Blocks.air)) && itemStack2 != null) {
                ItemStack itemStack3 = itemStack2.copy();
                itemStack3.stackSize = 1;
                this.setCurrentItemOrArmor(n, itemStack3);
            } else if (itemStack2 != null && itemStack2.stackSize > 1) {
                if (itemStack == null) {
                    ItemStack itemStack4 = itemStack2.copy();
                    itemStack4.stackSize = 1;
                    this.setCurrentItemOrArmor(n, itemStack4);
                    --itemStack2.stackSize;
                }
            } else {
                this.setCurrentItemOrArmor(n, itemStack2);
                entityPlayer.inventory.setInventorySlotContents(n2, itemStack);
            }
        }
    }

    public void setLeftArmRotation(Rotations rotations) {
        this.leftArmRotation = rotations;
        this.dataWatcher.updateObject(13, rotations);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(10, (byte)0);
        this.dataWatcher.addObject(11, DEFAULT_HEAD_ROTATION);
        this.dataWatcher.addObject(12, DEFAULT_BODY_ROTATION);
        this.dataWatcher.addObject(13, DEFAULT_LEFTARM_ROTATION);
        this.dataWatcher.addObject(14, DEFAULT_RIGHTARM_ROTATION);
        this.dataWatcher.addObject(15, DEFAULT_LEFTLEG_ROTATION);
        this.dataWatcher.addObject(16, DEFAULT_RIGHTLEG_ROTATION);
    }

    private NBTTagCompound readPoseFromNBT() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation)) {
            nBTTagCompound.setTag("Head", this.headRotation.writeToNBT());
        }
        if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation)) {
            nBTTagCompound.setTag("Body", this.bodyRotation.writeToNBT());
        }
        if (!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation)) {
            nBTTagCompound.setTag("LeftArm", this.leftArmRotation.writeToNBT());
        }
        if (!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation)) {
            nBTTagCompound.setTag("RightArm", this.rightArmRotation.writeToNBT());
        }
        if (!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation)) {
            nBTTagCompound.setTag("LeftLeg", this.leftLegRotation.writeToNBT());
        }
        if (!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation)) {
            nBTTagCompound.setTag("RightLeg", this.rightLegRotation.writeToNBT());
        }
        return nBTTagCompound;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        NBTTagList nBTTagList = new NBTTagList();
        int n = 0;
        while (n < this.contents.length) {
            NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
            if (this.contents[n] != null) {
                this.contents[n].writeToNBT(nBTTagCompound2);
            }
            nBTTagList.appendTag(nBTTagCompound2);
            ++n;
        }
        nBTTagCompound.setTag("Equipment", nBTTagList);
        if (this.getAlwaysRenderNameTag() && (this.getCustomNameTag() == null || this.getCustomNameTag().length() == 0)) {
            nBTTagCompound.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
        }
        nBTTagCompound.setBoolean("Invisible", this.isInvisible());
        nBTTagCompound.setBoolean("Small", this.isSmall());
        nBTTagCompound.setBoolean("ShowArms", this.getShowArms());
        nBTTagCompound.setInteger("DisabledSlots", this.disabledSlots);
        nBTTagCompound.setBoolean("NoGravity", this.hasNoGravity());
        nBTTagCompound.setBoolean("NoBasePlate", this.hasNoBasePlate());
        if (this.func_181026_s()) {
            nBTTagCompound.setBoolean("Marker", this.func_181026_s());
        }
        nBTTagCompound.setTag("Pose", this.readPoseFromNBT());
    }

    public void setRightLegRotation(Rotations rotations) {
        this.rightLegRotation = rotations;
        this.dataWatcher.updateObject(16, rotations);
    }

    private void damageArmorStand(float f) {
        float f2 = this.getHealth();
        if ((f2 -= f) <= 0.5f) {
            this.dropContents();
            this.setDead();
        } else {
            this.setHealth(f2);
        }
    }

    @Override
    protected void updatePotionMetadata() {
        this.setInvisible(this.canInteract);
    }

    @Override
    public boolean isServerWorld() {
        return super.isServerWorld() && !this.hasNoGravity();
    }

    public boolean isSmall() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 1) != 0;
    }

    public Rotations getBodyRotation() {
        return this.bodyRotation;
    }

    @Override
    public ItemStack[] getInventory() {
        return this.contents;
    }

    static {
        DEFAULT_HEAD_ROTATION = new Rotations(0.0f, 0.0f, 0.0f);
        DEFAULT_BODY_ROTATION = new Rotations(0.0f, 0.0f, 0.0f);
        DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0f, 0.0f, -10.0f);
        DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0f, 0.0f, 10.0f);
        DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0f, 0.0f, -1.0f);
        DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0f, 0.0f, 1.0f);
    }

    private void dropContents() {
        int n = 0;
        while (n < this.contents.length) {
            if (this.contents[n] != null && this.contents[n].stackSize > 0) {
                if (this.contents[n] != null) {
                    Block.spawnAsEntity(this.worldObj, new BlockPos(this).up(), this.contents[n]);
                }
                this.contents[n] = null;
            }
            ++n;
        }
    }

    public Rotations getHeadRotation() {
        return this.headRotation;
    }

    public EntityArmorStand(World world) {
        super(world);
        this.headRotation = DEFAULT_HEAD_ROTATION;
        this.bodyRotation = DEFAULT_BODY_ROTATION;
        this.leftArmRotation = DEFAULT_LEFTARM_ROTATION;
        this.rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
        this.leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
        this.rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
        this.setSilent(true);
        this.noClip = this.hasNoGravity();
        this.setSize(0.5f, 1.975f);
    }

    @Override
    protected float func_110146_f(float f, float f2) {
        this.prevRenderYawOffset = this.prevRotationYaw;
        this.renderYawOffset = this.rotationYaw;
        return 0.0f;
    }

    public Rotations getLeftArmRotation() {
        return this.leftArmRotation;
    }

    @Override
    public boolean replaceItemInInventory(int n, ItemStack itemStack) {
        int n2;
        if (n == 99) {
            n2 = 0;
        } else {
            n2 = n - 100 + 1;
            if (n2 < 0 || n2 >= this.contents.length) {
                return false;
            }
        }
        if (!(itemStack == null || EntityLiving.getArmorPosition(itemStack) == n2 || n2 == 4 && itemStack.getItem() instanceof ItemBlock)) {
            return false;
        }
        this.setCurrentItemOrArmor(n2, itemStack);
        return true;
    }

    @Override
    public ItemStack getEquipmentInSlot(int n) {
        return this.contents[n];
    }

    public boolean func_181026_s() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 0x10) != 0;
    }

    private void dropBlock() {
        Block.spawnAsEntity(this.worldObj, new BlockPos(this), new ItemStack(Items.armor_stand));
        this.dropContents();
    }

    public Rotations getLeftLegRotation() {
        return this.leftLegRotation;
    }

    public EntityArmorStand(World world, double d, double d2, double d3) {
        this(world);
        this.setPosition(d, d2, d3);
    }

    @Override
    public void setCurrentItemOrArmor(int n, ItemStack itemStack) {
        this.contents[n] = itemStack;
    }

    @Override
    public ItemStack getCurrentArmor(int n) {
        return this.contents[n + 1];
    }

    @Override
    public void moveEntityWithHeading(float f, float f2) {
        if (!this.hasNoGravity()) {
            super.moveEntityWithHeading(f, f2);
        }
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return this.isInvisible();
    }

    private void func_181027_m(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(10);
        by = bl ? (byte)(by | 0x10) : (byte)(by & 0xFFFFFFEF);
        this.dataWatcher.updateObject(10, by);
    }

    public boolean hasNoGravity() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 2) != 0;
    }

    @Override
    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith() && !this.func_181026_s();
    }

    private void setShowArms(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(10);
        by = bl ? (byte)(by | 4) : (byte)(by & 0xFFFFFFFB);
        this.dataWatcher.updateObject(10, by);
    }

    private void func_181550_a(boolean bl) {
        double d = this.posX;
        double d2 = this.posY;
        double d3 = this.posZ;
        if (bl) {
            this.setSize(0.5f, 1.975f);
        } else {
            this.setSize(0.0f, 0.0f);
        }
        this.setPosition(d, d2, d3);
    }

    private void playParticles() {
        if (this.worldObj instanceof WorldServer) {
            ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double)this.height / 1.5, this.posZ, 10, (double)(this.width / 4.0f), (double)(this.height / 4.0f), (double)(this.width / 4.0f), 0.05, Block.getStateId(Blocks.planks.getDefaultState()));
        }
    }

    public void setRightArmRotation(Rotations rotations) {
        this.rightArmRotation = rotations;
        this.dataWatcher.updateObject(14, rotations);
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public boolean interactAt(EntityPlayer var1_1, Vec3 var2_2) {
        block20: {
            block22: {
                block23: {
                    block21: {
                        if (this.func_181026_s()) {
                            return false;
                        }
                        if (this.worldObj.isRemote || var1_1.isSpectator()) break block20;
                        var3_3 = 0;
                        var4_4 = var1_1.getCurrentEquippedItem();
                        v0 = var5_5 = var4_4 != null;
                        if (var5_5 && var4_4.getItem() instanceof ItemArmor) {
                            var6_6 = (ItemArmor)var4_4.getItem();
                            if (var6_6.armorType == 3) {
                                var3_3 = 1;
                            } else if (var6_6.armorType == 2) {
                                var3_3 = 2;
                            } else if (var6_6.armorType == 1) {
                                var3_3 = 3;
                            } else if (var6_6.armorType == 0) {
                                var3_3 = 4;
                            }
                        }
                        if (var5_5 && (var4_4.getItem() == Items.skull || var4_4.getItem() == Item.getItemFromBlock(Blocks.pumpkin))) {
                            var3_3 = 4;
                        }
                        var6_7 = 0.1;
                        var8_8 = 0.9;
                        var10_9 = 0.4;
                        var12_10 = 1.6;
                        var14_11 = 0;
                        var15_12 = this.isSmall();
                        v1 = var16_13 = var15_12 != false ? var2_2.yCoord * 2.0 : var2_2.yCoord;
                        if (!(var16_13 >= 0.1)) break block21;
                        v2 = var15_12 != false ? 0.8 : 0.45;
                        if (!(var16_13 < 0.1 + v2) || this.contents[1] == null) break block21;
                        var14_11 = 1;
                        break block22;
                    }
                    v3 = var15_12 != false ? 0.3 : 0.0;
                    if (!(var16_13 >= 0.9 + v3)) break block23;
                    v4 = var15_12 != false ? 1.0 : 0.7;
                    if (!(var16_13 < 0.9 + v4) || this.contents[3] == null) break block23;
                    var14_11 = 3;
                    break block22;
                }
                if (!(var16_13 >= 0.4)) ** GOTO lbl-1000
                v5 = var15_12 != false ? 1.0 : 0.8;
                if (var16_13 < 0.4 + v5 && this.contents[2] != null) {
                    var14_11 = 2;
                } else if (var16_13 >= 1.6 && this.contents[4] != null) {
                    var14_11 = 4;
                }
            }
            v6 = var18_14 = this.contents[var14_11] != null;
            if ((this.disabledSlots & 1 << var14_11) != 0 || (this.disabledSlots & 1 << var3_3) != 0) {
                var14_11 = var3_3;
                if ((this.disabledSlots & 1 << var3_3) != 0) {
                    if ((this.disabledSlots & 1) != 0) {
                        return true;
                    }
                    var14_11 = 0;
                }
            }
            if (var5_5 && var3_3 == 0 && !this.getShowArms()) {
                return true;
            }
            if (var5_5) {
                this.func_175422_a(var1_1, var3_3);
            } else if (var18_14) {
                this.func_175422_a(var1_1, var14_11);
            }
            return true;
        }
        return true;
    }

    public Rotations getRightArmRotation() {
        return this.rightArmRotation;
    }

    @Override
    public float getEyeHeight() {
        return this.isChild() ? this.height * 0.5f : this.height * 0.9f;
    }

    public void setBodyRotation(Rotations rotations) {
        this.bodyRotation = rotations;
        this.dataWatcher.updateObject(12, rotations);
    }

    public boolean getShowArms() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 4) != 0;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        NBTBase nBTBase;
        super.readEntityFromNBT(nBTTagCompound);
        if (nBTTagCompound.hasKey("Equipment", 9)) {
            nBTBase = nBTTagCompound.getTagList("Equipment", 10);
            int n = 0;
            while (n < this.contents.length) {
                this.contents[n] = ItemStack.loadItemStackFromNBT(nBTBase.getCompoundTagAt(n));
                ++n;
            }
        }
        this.setInvisible(nBTTagCompound.getBoolean("Invisible"));
        this.setSmall(nBTTagCompound.getBoolean("Small"));
        this.setShowArms(nBTTagCompound.getBoolean("ShowArms"));
        this.disabledSlots = nBTTagCompound.getInteger("DisabledSlots");
        this.setNoGravity(nBTTagCompound.getBoolean("NoGravity"));
        this.setNoBasePlate(nBTTagCompound.getBoolean("NoBasePlate"));
        this.func_181027_m(nBTTagCompound.getBoolean("Marker"));
        this.field_181028_bj = !this.func_181026_s();
        this.noClip = this.hasNoGravity();
        nBTBase = nBTTagCompound.getCompoundTag("Pose");
        this.writePoseToNBT((NBTTagCompound)nBTBase);
    }

    private void setSmall(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(10);
        by = bl ? (byte)(by | 1) : (byte)(by & 0xFFFFFFFE);
        this.dataWatcher.updateObject(10, by);
    }

    @Override
    public ItemStack getHeldItem() {
        return this.contents[0];
    }

    public void setHeadRotation(Rotations rotations) {
        this.headRotation = rotations;
        this.dataWatcher.updateObject(11, rotations);
    }

    private void writePoseToNBT(NBTTagCompound nBTTagCompound) {
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Head", 5);
        if (nBTTagList.tagCount() > 0) {
            this.setHeadRotation(new Rotations(nBTTagList));
        } else {
            this.setHeadRotation(DEFAULT_HEAD_ROTATION);
        }
        NBTTagList nBTTagList2 = nBTTagCompound.getTagList("Body", 5);
        if (nBTTagList2.tagCount() > 0) {
            this.setBodyRotation(new Rotations(nBTTagList2));
        } else {
            this.setBodyRotation(DEFAULT_BODY_ROTATION);
        }
        NBTTagList nBTTagList3 = nBTTagCompound.getTagList("LeftArm", 5);
        if (nBTTagList3.tagCount() > 0) {
            this.setLeftArmRotation(new Rotations(nBTTagList3));
        } else {
            this.setLeftArmRotation(DEFAULT_LEFTARM_ROTATION);
        }
        NBTTagList nBTTagList4 = nBTTagCompound.getTagList("RightArm", 5);
        if (nBTTagList4.tagCount() > 0) {
            this.setRightArmRotation(new Rotations(nBTTagList4));
        } else {
            this.setRightArmRotation(DEFAULT_RIGHTARM_ROTATION);
        }
        NBTTagList nBTTagList5 = nBTTagCompound.getTagList("LeftLeg", 5);
        if (nBTTagList5.tagCount() > 0) {
            this.setLeftLegRotation(new Rotations(nBTTagList5));
        } else {
            this.setLeftLegRotation(DEFAULT_LEFTLEG_ROTATION);
        }
        NBTTagList nBTTagList6 = nBTTagCompound.getTagList("RightLeg", 5);
        if (nBTTagList6.tagCount() > 0) {
            this.setRightLegRotation(new Rotations(nBTTagList6));
        } else {
            this.setRightLegRotation(DEFAULT_RIGHTLEG_ROTATION);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.worldObj.isRemote) {
            return false;
        }
        if (DamageSource.outOfWorld.equals(damageSource)) {
            this.setDead();
            return false;
        }
        if (!(this.isEntityInvulnerable(damageSource) || this.canInteract || this.func_181026_s())) {
            if (damageSource.isExplosion()) {
                this.dropContents();
                this.setDead();
                return false;
            }
            if (DamageSource.inFire.equals(damageSource)) {
                if (!this.isBurning()) {
                    this.setFire(5);
                } else {
                    this.damageArmorStand(0.15f);
                }
                return false;
            }
            if (DamageSource.onFire.equals(damageSource) && this.getHealth() > 0.5f) {
                this.damageArmorStand(4.0f);
                return false;
            }
            boolean bl = "arrow".equals(damageSource.getDamageType());
            boolean bl2 = "player".equals(damageSource.getDamageType());
            if (!bl2 && !bl) {
                return false;
            }
            if (damageSource.getSourceOfDamage() instanceof EntityArrow) {
                damageSource.getSourceOfDamage().setDead();
            }
            if (damageSource.getEntity() instanceof EntityPlayer && !((EntityPlayer)damageSource.getEntity()).capabilities.allowEdit) {
                return false;
            }
            if (damageSource.isCreativePlayer()) {
                this.playParticles();
                this.setDead();
                return false;
            }
            long l = this.worldObj.getTotalWorldTime();
            if (l - this.punchCooldown > 5L && !bl) {
                this.punchCooldown = l;
            } else {
                this.dropBlock();
                this.playParticles();
                this.setDead();
            }
            return false;
        }
        return false;
    }

    public Rotations getRightLegRotation() {
        return this.rightLegRotation;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
    }

    private void setNoGravity(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(10);
        by = bl ? (byte)(by | 2) : (byte)(by & 0xFFFFFFFD);
        this.dataWatcher.updateObject(10, by);
    }

    @Override
    public void onKillCommand() {
        this.setDead();
    }
}

