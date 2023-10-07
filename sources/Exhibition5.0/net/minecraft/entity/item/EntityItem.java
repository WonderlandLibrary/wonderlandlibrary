// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.item;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.StatCollector;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.Entity;

public class EntityItem extends Entity
{
    private static final Logger logger;
    private int age;
    private int delayBeforeCanPickup;
    private int health;
    private String thrower;
    private String owner;
    public float hoverStart;
    private static final String __OBFID = "CL_00001669";
    
    public EntityItem(final World worldIn, final double x, final double y, final double z) {
        super(worldIn);
        this.health = 5;
        this.hoverStart = (float)(Math.random() * 3.141592653589793 * 2.0);
        this.setSize(0.25f, 0.25f);
        this.setPosition(x, y, z);
        this.rotationYaw = (float)(Math.random() * 360.0);
        this.motionX = (float)(Math.random() * 0.20000000298023224 - 0.10000000149011612);
        this.motionY = 0.20000000298023224;
        this.motionZ = (float)(Math.random() * 0.20000000298023224 - 0.10000000149011612);
    }
    
    public EntityItem(final World worldIn, final double x, final double y, final double z, final ItemStack stack) {
        this(worldIn, x, y, z);
        this.setEntityItemStack(stack);
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    public EntityItem(final World worldIn) {
        super(worldIn);
        this.health = 5;
        this.hoverStart = (float)(Math.random() * 3.141592653589793 * 2.0);
        this.setSize(0.25f, 0.25f);
        this.setEntityItemStack(new ItemStack(Blocks.air, 0));
    }
    
    @Override
    protected void entityInit() {
        this.getDataWatcher().addObjectByDataType(10, 5);
    }
    
    @Override
    public void onUpdate() {
        if (this.getEntityItem() == null) {
            this.setDead();
        }
        else {
            super.onUpdate();
            if (this.delayBeforeCanPickup > 0 && this.delayBeforeCanPickup != 32767) {
                --this.delayBeforeCanPickup;
            }
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033;
            this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            final boolean var1 = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;
            if (var1 || this.ticksExisted % 25 == 0) {
                if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava) {
                    this.motionY = 0.20000000298023224;
                    this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
                    this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
                    this.playSound("random.fizz", 0.4f, 2.0f + this.rand.nextFloat() * 0.4f);
                }
                if (!this.worldObj.isRemote) {
                    this.searchForOtherItemsNearby();
                }
            }
            float var2 = 0.98f;
            if (this.onGround) {
                var2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98f;
            }
            this.motionX *= var2;
            this.motionY *= 0.9800000190734863;
            this.motionZ *= var2;
            if (this.onGround) {
                this.motionY *= -0.5;
            }
            if (this.age != -32768) {
                ++this.age;
            }
            this.handleWaterMovement();
            if (!this.worldObj.isRemote && this.age >= 6000) {
                this.setDead();
            }
        }
    }
    
    private void searchForOtherItemsNearby() {
        for (final EntityItem var2 : this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(0.5, 0.0, 0.5))) {
            this.combineItems(var2);
        }
    }
    
    private boolean combineItems(final EntityItem other) {
        if (other == this) {
            return false;
        }
        if (!other.isEntityAlive() || !this.isEntityAlive()) {
            return false;
        }
        final ItemStack var2 = this.getEntityItem();
        final ItemStack var3 = other.getEntityItem();
        if (this.delayBeforeCanPickup == 32767 || other.delayBeforeCanPickup == 32767) {
            return false;
        }
        if (this.age == -32768 || other.age == -32768) {
            return false;
        }
        if (var3.getItem() != var2.getItem()) {
            return false;
        }
        if (var3.hasTagCompound() ^ var2.hasTagCompound()) {
            return false;
        }
        if (var3.hasTagCompound() && !var3.getTagCompound().equals(var2.getTagCompound())) {
            return false;
        }
        if (var3.getItem() == null) {
            return false;
        }
        if (var3.getItem().getHasSubtypes() && var3.getMetadata() != var2.getMetadata()) {
            return false;
        }
        if (var3.stackSize < var2.stackSize) {
            return other.combineItems(this);
        }
        if (var3.stackSize + var2.stackSize > var3.getMaxStackSize()) {
            return false;
        }
        final ItemStack itemStack = var3;
        itemStack.stackSize += var2.stackSize;
        other.delayBeforeCanPickup = Math.max(other.delayBeforeCanPickup, this.delayBeforeCanPickup);
        other.age = Math.min(other.age, this.age);
        other.setEntityItemStack(var3);
        this.setDead();
        return true;
    }
    
    public void setAgeToCreativeDespawnTime() {
        this.age = 4800;
    }
    
    @Override
    public boolean handleWaterMovement() {
        if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.water, this)) {
            if (!this.inWater && !this.firstUpdate) {
                this.resetHeight();
            }
            this.inWater = true;
        }
        else {
            this.inWater = false;
        }
        return this.inWater;
    }
    
    @Override
    protected void dealFireDamage(final int amount) {
        this.attackEntityFrom(DamageSource.inFire, amount);
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if (this.getEntityItem() != null && this.getEntityItem().getItem() == Items.nether_star && source.isExplosion()) {
            return false;
        }
        this.setBeenAttacked();
        this.health -= (int)amount;
        if (this.health <= 0) {
            this.setDead();
        }
        return false;
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setShort("Health", (byte)this.health);
        tagCompound.setShort("Age", (short)this.age);
        tagCompound.setShort("PickupDelay", (short)this.delayBeforeCanPickup);
        if (this.getThrower() != null) {
            tagCompound.setString("Thrower", this.thrower);
        }
        if (this.getOwner() != null) {
            tagCompound.setString("Owner", this.owner);
        }
        if (this.getEntityItem() != null) {
            tagCompound.setTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
        }
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.health = (tagCompund.getShort("Health") & 0xFF);
        this.age = tagCompund.getShort("Age");
        if (tagCompund.hasKey("PickupDelay")) {
            this.delayBeforeCanPickup = tagCompund.getShort("PickupDelay");
        }
        if (tagCompund.hasKey("Owner")) {
            this.owner = tagCompund.getString("Owner");
        }
        if (tagCompund.hasKey("Thrower")) {
            this.thrower = tagCompund.getString("Thrower");
        }
        final NBTTagCompound var2 = tagCompund.getCompoundTag("Item");
        this.setEntityItemStack(ItemStack.loadItemStackFromNBT(var2));
        if (this.getEntityItem() == null) {
            this.setDead();
        }
    }
    
    @Override
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
        if (!this.worldObj.isRemote) {
            final ItemStack var2 = this.getEntityItem();
            final int var3 = var2.stackSize;
            if (this.delayBeforeCanPickup == 0 && (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(entityIn.getName())) && entityIn.inventory.addItemStackToInventory(var2)) {
                if (var2.getItem() == Item.getItemFromBlock(Blocks.log)) {
                    entityIn.triggerAchievement(AchievementList.mineWood);
                }
                if (var2.getItem() == Item.getItemFromBlock(Blocks.log2)) {
                    entityIn.triggerAchievement(AchievementList.mineWood);
                }
                if (var2.getItem() == Items.leather) {
                    entityIn.triggerAchievement(AchievementList.killCow);
                }
                if (var2.getItem() == Items.diamond) {
                    entityIn.triggerAchievement(AchievementList.diamonds);
                }
                if (var2.getItem() == Items.blaze_rod) {
                    entityIn.triggerAchievement(AchievementList.blazeRod);
                }
                if (var2.getItem() == Items.diamond && this.getThrower() != null) {
                    final EntityPlayer var4 = this.worldObj.getPlayerEntityByName(this.getThrower());
                    if (var4 != null && var4 != entityIn) {
                        var4.triggerAchievement(AchievementList.diamondsToYou);
                    }
                }
                if (!this.isSlient()) {
                    this.worldObj.playSoundAtEntity(entityIn, "random.pop", 0.2f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                }
                entityIn.onItemPickup(this, var3);
                if (var2.stackSize <= 0) {
                    this.setDead();
                }
            }
        }
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : StatCollector.translateToLocal("item." + this.getEntityItem().getUnlocalizedName());
    }
    
    @Override
    public boolean canAttackWithItem() {
        return false;
    }
    
    @Override
    public void travelToDimension(final int dimensionId) {
        super.travelToDimension(dimensionId);
        if (!this.worldObj.isRemote) {
            this.searchForOtherItemsNearby();
        }
    }
    
    public ItemStack getEntityItem() {
        final ItemStack var1 = this.getDataWatcher().getWatchableObjectItemStack(10);
        if (var1 == null) {
            if (this.worldObj != null) {
                EntityItem.logger.error("Item entity " + this.getEntityId() + " has no item?!");
            }
            return new ItemStack(Blocks.stone);
        }
        return var1;
    }
    
    public void setEntityItemStack(final ItemStack stack) {
        this.getDataWatcher().updateObject(10, stack);
        this.getDataWatcher().setObjectWatched(10);
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    public void setOwner(final String owner) {
        this.owner = owner;
    }
    
    public String getThrower() {
        return this.thrower;
    }
    
    public void setThrower(final String thrower) {
        this.thrower = thrower;
    }
    
    public int func_174872_o() {
        return this.age;
    }
    
    public void setDefaultPickupDelay() {
        this.delayBeforeCanPickup = 10;
    }
    
    public void setNoPickupDelay() {
        this.delayBeforeCanPickup = 0;
    }
    
    public void setInfinitePickupDelay() {
        this.delayBeforeCanPickup = 32767;
    }
    
    public void setPickupDelay(final int ticks) {
        this.delayBeforeCanPickup = ticks;
    }
    
    public boolean func_174874_s() {
        return this.delayBeforeCanPickup > 0;
    }
    
    public void func_174873_u() {
        this.age = -6000;
    }
    
    public void func_174870_v() {
        this.setInfinitePickupDelay();
        this.age = 5999;
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
