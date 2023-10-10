/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class Entity
implements ICommandSender {
    private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    private static int nextEntityID;
    private int entityId = nextEntityID++;
    public double renderDistanceWeight = 1.0;
    public boolean preventEntitySpawning;
    public Entity riddenByEntity;
    public Entity ridingEntity;
    public boolean forceSpawn;
    public World worldObj;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;
    public double posX;
    public double posY;
    public double posZ;
    public double motionX;
    public double motionY;
    public double motionZ;
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    private AxisAlignedBB boundingBox = ZERO_AABB;
    public boolean onGround;
    public boolean isCollidedHorizontally;
    public boolean isCollidedVertically;
    public boolean isCollided;
    public boolean velocityChanged;
    public boolean isInWeb;
    private boolean isOutsideBorder;
    public boolean isDead;
    public float width = 0.6f;
    public float height = 1.8f;
    public float prevDistanceWalkedModified;
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;
    private int nextStepDistance = 1;
    public double lastTickPosX;
    public double lastTickPosY;
    public double lastTickPosZ;
    public float stepHeight;
    public boolean noClip;
    public float entityCollisionReduction;
    protected Random rand = new Random();
    public int ticksExisted;
    public int fireResistance = 1;
    private int fire;
    protected boolean inWater;
    public int hurtResistantTime;
    protected boolean firstUpdate = true;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public int serverPosX;
    public int serverPosY;
    public int serverPosZ;
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;
    protected boolean inPortal;
    protected int portalCounter;
    public int dimension;
    protected BlockPos field_181016_an;
    protected Vec3 field_181017_ao;
    protected EnumFacing field_181018_ap;
    private boolean invulnerable;
    protected UUID entityUniqueID = MathHelper.getRandomUuid(this.rand);
    private final CommandResultStats cmdResultStats = new CommandResultStats();

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int id) {
        this.entityId = id;
    }

    public void onKillCommand() {
        this.setDead();
    }

    public Entity(World worldIn) {
        this.worldObj = worldIn;
        this.setPosition(0.0, 0.0, 0.0);
        if (worldIn != null) {
            this.dimension = worldIn.provider.getDimensionId();
        }
        this.dataWatcher = new DataWatcher(this);
        this.dataWatcher.addObject(0, (byte)0);
        this.dataWatcher.addObject(1, (short)300);
        this.dataWatcher.addObject(3, (byte)0);
        this.dataWatcher.addObject(2, "");
        this.dataWatcher.addObject(4, (byte)0);
        this.entityInit();
    }

    protected abstract void entityInit();

    public DataWatcher getDataWatcher() {
        return this.dataWatcher;
    }

    public boolean equals(Object p_equals_1_) {
        return p_equals_1_ instanceof Entity ? ((Entity)p_equals_1_).entityId == this.entityId : false;
    }

    public int hashCode() {
        return this.entityId;
    }

    protected void preparePlayerToSpawn() {
        if (this.worldObj != null) {
            while (this.posY > 0.0 && this.posY < 256.0) {
                this.setPosition(this.posX, this.posY, this.posZ);
                if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty()) break;
                this.posY += 1.0;
            }
            this.motionZ = 0.0;
            this.motionY = 0.0;
            this.motionX = 0.0;
            this.rotationPitch = 0.0f;
        }
    }

    public void setDead() {
        this.isDead = true;
    }

    protected void setSize(float width, float height) {
        if (width != this.width || height != this.height) {
            float f2 = this.width;
            this.width = width;
            this.height = height;
            this.setEntityBoundingBox(new AxisAlignedBB(this.getEntityBoundingBox().minX, this.getEntityBoundingBox().minY, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().minX + (double)this.width, this.getEntityBoundingBox().minY + (double)this.height, this.getEntityBoundingBox().minZ + (double)this.width));
            if (this.width > f2 && !this.firstUpdate && !this.worldObj.isRemote) {
                this.moveEntity(f2 - this.width, 0.0, f2 - this.width);
            }
        }
    }

    protected void setRotation(float yaw, float pitch) {
        this.rotationYaw = yaw % 360.0f;
        this.rotationPitch = pitch % 360.0f;
    }

    public void setPosition(double x2, double y2, double z2) {
        this.posX = x2;
        this.posY = y2;
        this.posZ = z2;
        float f2 = this.width / 2.0f;
        float f1 = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(x2 - (double)f2, y2, z2 - (double)f2, x2 + (double)f2, y2 + (double)f1, z2 + (double)f2));
    }

    public void setAngles(float yaw, float pitch) {
        float f2 = this.rotationPitch;
        float f1 = this.rotationYaw;
        this.rotationYaw = (float)((double)this.rotationYaw + (double)yaw * 0.15);
        this.rotationPitch = (float)((double)this.rotationPitch - (double)pitch * 0.15);
        this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0f, 90.0f);
        this.prevRotationPitch += this.rotationPitch - f2;
        this.prevRotationYaw += this.rotationYaw - f1;
    }

    public void onUpdate() {
        this.onEntityUpdate();
    }

    public void onEntityUpdate() {
        this.worldObj.theProfiler.startSection("entityBaseTick");
        if (this.ridingEntity != null && this.ridingEntity.isDead) {
            this.ridingEntity = null;
        }
        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
            this.worldObj.theProfiler.startSection("portal");
            MinecraftServer minecraftserver = ((WorldServer)this.worldObj).getMinecraftServer();
            int i2 = this.getMaxInPortalTime();
            if (this.inPortal) {
                if (minecraftserver.getAllowNether()) {
                    if (this.ridingEntity == null && this.portalCounter++ >= i2) {
                        this.portalCounter = i2;
                        this.timeUntilPortal = this.getPortalCooldown();
                        int j2 = this.worldObj.provider.getDimensionId() == -1 ? 0 : -1;
                        this.travelToDimension(j2);
                    }
                    this.inPortal = false;
                }
            } else {
                if (this.portalCounter > 0) {
                    this.portalCounter -= 4;
                }
                if (this.portalCounter < 0) {
                    this.portalCounter = 0;
                }
            }
            if (this.timeUntilPortal > 0) {
                --this.timeUntilPortal;
            }
            this.worldObj.theProfiler.endSection();
        }
        this.spawnRunningParticles();
        this.handleWaterMovement();
        if (this.worldObj.isRemote) {
            this.fire = 0;
        } else if (this.fire > 0) {
            if (this.isImmuneToFire) {
                this.fire -= 4;
                if (this.fire < 0) {
                    this.fire = 0;
                }
            } else {
                if (this.fire % 20 == 0) {
                    this.attackEntityFrom(DamageSource.onFire, 1.0f);
                }
                --this.fire;
            }
        }
        if (this.isInLava()) {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5f;
        }
        if (this.posY < -64.0) {
            this.kill();
        }
        if (!this.worldObj.isRemote) {
            this.setFlag(0, this.fire > 0);
        }
        this.firstUpdate = false;
        this.worldObj.theProfiler.endSection();
    }

    public int getMaxInPortalTime() {
        return 0;
    }

    protected void setOnFireFromLava() {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.lava, 4.0f);
            this.setFire(15);
        }
    }

    public void setFire(int seconds) {
        int i2 = seconds * 20;
        if (this.fire < (i2 = EnchantmentProtection.getFireTimeForEntity(this, i2))) {
            this.fire = i2;
        }
    }

    public void extinguish() {
        this.fire = 0;
    }

    protected void kill() {
        this.setDead();
    }

    public boolean isOffsetPositionInLiquid(double x2, double y2, double z2) {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().offset(x2, y2, z2);
        return this.isLiquidPresentInAABB(axisalignedbb);
    }

    private boolean isLiquidPresentInAABB(AxisAlignedBB bb) {
        return this.worldObj.getCollidingBoundingBoxes(this, bb).isEmpty() && !this.worldObj.isAnyLiquid(bb);
    }

    public void moveEntity(double x2, double y2, double z2) {
        if (this.noClip) {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x2, y2, z2));
            this.resetPositionToBB();
        } else {
            Block block;
            boolean flag;
            this.worldObj.theProfiler.startSection("move");
            double d0 = this.posX;
            double d1 = this.posY;
            double d2 = this.posZ;
            if (this.isInWeb) {
                this.isInWeb = false;
                x2 *= 0.25;
                y2 *= (double)0.05f;
                z2 *= 0.25;
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            double d3 = x2;
            double d4 = y2;
            double d5 = z2;
            boolean bl = flag = this.onGround && this.isSneaking() && this instanceof EntityPlayer;
            if (flag) {
                double d6 = 0.05;
                while (x2 != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x2, -1.0, 0.0)).isEmpty()) {
                    x2 = x2 < d6 && x2 >= -d6 ? 0.0 : (x2 > 0.0 ? (x2 -= d6) : (x2 += d6));
                    d3 = x2;
                }
                while (z2 != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0, -1.0, z2)).isEmpty()) {
                    z2 = z2 < d6 && z2 >= -d6 ? 0.0 : (z2 > 0.0 ? (z2 -= d6) : (z2 += d6));
                    d5 = z2;
                }
                while (x2 != 0.0 && z2 != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x2, -1.0, z2)).isEmpty()) {
                    x2 = x2 < d6 && x2 >= -d6 ? 0.0 : (x2 > 0.0 ? (x2 -= d6) : (x2 += d6));
                    d3 = x2;
                    z2 = z2 < d6 && z2 >= -d6 ? 0.0 : (z2 > 0.0 ? (z2 -= d6) : (z2 += d6));
                    d5 = z2;
                }
            }
            List<AxisAlignedBB> list1 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(x2, y2, z2));
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            for (AxisAlignedBB axisAlignedBB : list1) {
                y2 = axisAlignedBB.calculateYOffset(this.getEntityBoundingBox(), y2);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y2, 0.0));
            boolean flag1 = this.onGround || d4 != y2 && d4 < 0.0;
            for (AxisAlignedBB axisalignedbb2 : list1) {
                x2 = axisalignedbb2.calculateXOffset(this.getEntityBoundingBox(), x2);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x2, 0.0, 0.0));
            for (AxisAlignedBB axisalignedbb13 : list1) {
                z2 = axisalignedbb13.calculateZOffset(this.getEntityBoundingBox(), z2);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, 0.0, z2));
            if (this.stepHeight > 0.0f && flag1 && (d3 != x2 || d5 != z2)) {
                double d6 = x2;
                double d7 = y2;
                double d8 = z2;
                AxisAlignedBB axisalignedbb3 = this.getEntityBoundingBox();
                this.setEntityBoundingBox(axisalignedbb);
                y2 = this.stepHeight;
                List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(d3, y2, d5));
                AxisAlignedBB axisalignedbb4 = this.getEntityBoundingBox();
                AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(d3, 0.0, d5);
                double d9 = y2;
                for (AxisAlignedBB axisalignedbb6 : list) {
                    d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9);
                }
                axisalignedbb4 = axisalignedbb4.offset(0.0, d9, 0.0);
                double d15 = d3;
                for (AxisAlignedBB axisalignedbb7 : list) {
                    d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15);
                }
                axisalignedbb4 = axisalignedbb4.offset(d15, 0.0, 0.0);
                double d16 = d5;
                for (AxisAlignedBB axisalignedbb8 : list) {
                    d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16);
                }
                axisalignedbb4 = axisalignedbb4.offset(0.0, 0.0, d16);
                AxisAlignedBB axisalignedbb14 = this.getEntityBoundingBox();
                double d17 = y2;
                for (AxisAlignedBB axisalignedbb9 : list) {
                    d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17);
                }
                axisalignedbb14 = axisalignedbb14.offset(0.0, d17, 0.0);
                double d18 = d3;
                for (AxisAlignedBB axisalignedbb10 : list) {
                    d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18);
                }
                axisalignedbb14 = axisalignedbb14.offset(d18, 0.0, 0.0);
                double d19 = d5;
                for (AxisAlignedBB axisalignedbb11 : list) {
                    d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19);
                }
                axisalignedbb14 = axisalignedbb14.offset(0.0, 0.0, d19);
                double d20 = d15 * d15 + d16 * d16;
                double d10 = d18 * d18 + d19 * d19;
                if (d20 > d10) {
                    x2 = d15;
                    z2 = d16;
                    y2 = -d9;
                    this.setEntityBoundingBox(axisalignedbb4);
                } else {
                    x2 = d18;
                    z2 = d19;
                    y2 = -d17;
                    this.setEntityBoundingBox(axisalignedbb14);
                }
                for (AxisAlignedBB axisalignedbb12 : list) {
                    y2 = axisalignedbb12.calculateYOffset(this.getEntityBoundingBox(), y2);
                }
                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y2, 0.0));
                if (d6 * d6 + d8 * d8 >= x2 * x2 + z2 * z2) {
                    x2 = d6;
                    y2 = d7;
                    z2 = d8;
                    this.setEntityBoundingBox(axisalignedbb3);
                }
            }
            this.worldObj.theProfiler.endSection();
            this.worldObj.theProfiler.startSection("rest");
            this.resetPositionToBB();
            this.isCollidedHorizontally = d3 != x2 || d5 != z2;
            this.isCollidedVertically = d4 != y2;
            this.onGround = this.isCollidedVertically && d4 < 0.0;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            int n2 = MathHelper.floor_double(this.posX);
            int j2 = MathHelper.floor_double(this.posY - (double)0.2f);
            int k2 = MathHelper.floor_double(this.posZ);
            BlockPos blockpos = new BlockPos(n2, j2, k2);
            Block block1 = this.worldObj.getBlockState(blockpos).getBlock();
            if (block1.getMaterial() == Material.air && ((block = this.worldObj.getBlockState(blockpos.down()).getBlock()) instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate)) {
                block1 = block;
                blockpos = blockpos.down();
            }
            this.updateFallState(y2, this.onGround, block1, blockpos);
            if (d3 != x2) {
                this.motionX = 0.0;
            }
            if (d5 != z2) {
                this.motionZ = 0.0;
            }
            if (d4 != y2) {
                block1.onLanded(this.worldObj, this);
            }
            if (this.canTriggerWalking() && !flag && this.ridingEntity == null) {
                double d12 = this.posX - d0;
                double d13 = this.posY - d1;
                double d14 = this.posZ - d2;
                if (block1 != Blocks.ladder) {
                    d13 = 0.0;
                }
                if (block1 != null && this.onGround) {
                    block1.onEntityCollidedWithBlock(this.worldObj, blockpos, this);
                }
                this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d12 * d12 + d14 * d14) * 0.6);
                this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt_double(d12 * d12 + d13 * d13 + d14 * d14) * 0.6);
                if (this.distanceWalkedOnStepModified > (float)this.nextStepDistance && block1.getMaterial() != Material.air) {
                    this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
                    if (this.isInWater()) {
                        float f2 = MathHelper.sqrt_double(this.motionX * this.motionX * (double)0.2f + this.motionY * this.motionY + this.motionZ * this.motionZ * (double)0.2f) * 0.35f;
                        if (f2 > 1.0f) {
                            f2 = 1.0f;
                        }
                        this.playSound(this.getSwimSound(), f2, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                    }
                    this.playStepSound(blockpos, block1);
                }
            }
            try {
                this.doBlockCollisions();
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            boolean flag2 = this.isWet();
            if (this.worldObj.isFlammableWithin(this.getEntityBoundingBox().contract(0.001, 0.001, 0.001))) {
                this.dealFireDamage(1);
                if (!flag2) {
                    ++this.fire;
                    if (this.fire == 0) {
                        this.setFire(8);
                    }
                }
            } else if (this.fire <= 0) {
                this.fire = -this.fireResistance;
            }
            if (flag2 && this.fire > 0) {
                this.playSound("random.fizz", 0.7f, 1.6f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                this.fire = -this.fireResistance;
            }
            this.worldObj.theProfiler.endSection();
        }
    }

    private void resetPositionToBB() {
        this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
        this.posY = this.getEntityBoundingBox().minY;
        this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;
    }

    protected String getSwimSound() {
        return "game.neutral.swim";
    }

    protected void doBlockCollisions() {
        BlockPos blockpos = new BlockPos(this.getEntityBoundingBox().minX + 0.001, this.getEntityBoundingBox().minY + 0.001, this.getEntityBoundingBox().minZ + 0.001);
        BlockPos blockpos1 = new BlockPos(this.getEntityBoundingBox().maxX - 0.001, this.getEntityBoundingBox().maxY - 0.001, this.getEntityBoundingBox().maxZ - 0.001);
        if (this.worldObj.isAreaLoaded(blockpos, blockpos1)) {
            for (int i2 = blockpos.getX(); i2 <= blockpos1.getX(); ++i2) {
                for (int j2 = blockpos.getY(); j2 <= blockpos1.getY(); ++j2) {
                    for (int k2 = blockpos.getZ(); k2 <= blockpos1.getZ(); ++k2) {
                        BlockPos blockpos2 = new BlockPos(i2, j2, k2);
                        IBlockState iblockstate = this.worldObj.getBlockState(blockpos2);
                        try {
                            iblockstate.getBlock().onEntityCollidedWithBlock(this.worldObj, blockpos2, iblockstate, this);
                            continue;
                        }
                        catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
                            CrashReportCategory.addBlockInfo(crashreportcategory, blockpos2, iblockstate);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        Block.SoundType block$soundtype = blockIn.stepSound;
        if (this.worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer) {
            block$soundtype = Blocks.snow_layer.stepSound;
            this.playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15f, block$soundtype.getFrequency());
        } else if (!blockIn.getMaterial().isLiquid()) {
            this.playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15f, block$soundtype.getFrequency());
        }
    }

    public void playSound(String name, float volume, float pitch) {
        if (!this.isSilent()) {
            this.worldObj.playSoundAtEntity(this, name, volume, pitch);
        }
    }

    public boolean isSilent() {
        return this.dataWatcher.getWatchableObjectByte(4) == 1;
    }

    public void setSilent(boolean isSilent) {
        this.dataWatcher.updateObject(4, (byte)(isSilent ? 1 : 0));
    }

    protected boolean canTriggerWalking() {
        return true;
    }

    protected void updateFallState(double y2, boolean onGroundIn, Block blockIn, BlockPos pos) {
        if (onGroundIn) {
            if (this.fallDistance > 0.0f) {
                if (blockIn != null) {
                    blockIn.onFallenUpon(this.worldObj, pos, this, this.fallDistance);
                } else {
                    this.fall(this.fallDistance, 1.0f);
                }
                this.fallDistance = 0.0f;
            }
        } else if (y2 < 0.0) {
            this.fallDistance = (float)((double)this.fallDistance - y2);
        }
    }

    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }

    protected void dealFireDamage(int amount) {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.inFire, amount);
        }
    }

    public final boolean isImmuneToFire() {
        return this.isImmuneToFire;
    }

    public void fall(float distance, float damageMultiplier) {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.fall(distance, damageMultiplier);
        }
    }

    public boolean isWet() {
        return this.inWater || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY, this.posZ)) || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY + (double)this.height, this.posZ));
    }

    public boolean isInWater() {
        return this.inWater;
    }

    public boolean handleWaterMovement() {
        if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0, -0.4f, 0.0).contract(0.001, 0.001, 0.001), Material.water, this)) {
            if (!this.inWater && !this.firstUpdate) {
                this.resetHeight();
            }
            this.fallDistance = 0.0f;
            this.inWater = true;
            this.fire = 0;
        } else {
            this.inWater = false;
        }
        return this.inWater;
    }

    protected void resetHeight() {
        float f2 = MathHelper.sqrt_double(this.motionX * this.motionX * (double)0.2f + this.motionY * this.motionY + this.motionZ * this.motionZ * (double)0.2f) * 0.2f;
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        this.playSound(this.getSplashSound(), f2, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
        float f1 = MathHelper.floor_double(this.getEntityBoundingBox().minY);
        int i2 = 0;
        while ((float)i2 < 1.0f + this.width * 20.0f) {
            float f22 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            float f3 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (double)f22, (double)(f1 + 1.0f), this.posZ + (double)f3, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2f), this.motionZ, new int[0]);
            ++i2;
        }
        int j2 = 0;
        while ((float)j2 < 1.0f + this.width * 20.0f) {
            float f4 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            float f5 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double)f4, (double)(f1 + 1.0f), this.posZ + (double)f5, this.motionX, this.motionY, this.motionZ, new int[0]);
            ++j2;
        }
    }

    public void spawnRunningParticles() {
        if (this.isSprinting() && !this.isInWater()) {
            this.createRunningParticles();
        }
    }

    protected void createRunningParticles() {
        int k2;
        int j2;
        int i2 = MathHelper.floor_double(this.posX);
        BlockPos blockpos = new BlockPos(i2, j2 = MathHelper.floor_double(this.posY - (double)0.2f), k2 = MathHelper.floor_double(this.posZ));
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block.getRenderType() != -1) {
            this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5) * (double)this.width, this.getEntityBoundingBox().minY + 0.1, this.posZ + ((double)this.rand.nextFloat() - 0.5) * (double)this.width, -this.motionX * 4.0, 1.5, -this.motionZ * 4.0, Block.getStateId(iblockstate));
        }
    }

    protected String getSplashSound() {
        return "game.neutral.swim.splash";
    }

    public boolean isInsideOfMaterial(Material materialIn) {
        double d0 = this.posY + (double)this.getEyeHeight();
        BlockPos blockpos = new BlockPos(this.posX, d0, this.posZ);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block.getMaterial() == materialIn) {
            float f2 = BlockLiquid.getLiquidHeightPercent(iblockstate.getBlock().getMetaFromState(iblockstate)) - 0.11111111f;
            float f1 = (float)(blockpos.getY() + 1) - f2;
            boolean flag = d0 < (double)f1;
            return !flag && this instanceof EntityPlayer ? false : flag;
        }
        return false;
    }

    public boolean isInLava() {
        return this.worldObj.isMaterialInBB(this.getEntityBoundingBox().expand(-0.1f, -0.4f, -0.1f), Material.lava);
    }

    public void moveFlying(float strafe, float forward, float friction) {
        float f2 = strafe * strafe + forward * forward;
        if (f2 >= 1.0E-4f) {
            if ((f2 = MathHelper.sqrt_float(f2)) < 1.0f) {
                f2 = 1.0f;
            }
            f2 = friction / f2;
            float f1 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f);
            float f22 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f);
            this.motionX += (double)((strafe *= f2) * f22 - (forward *= f2) * f1);
            this.motionZ += (double)(forward * f22 + strafe * f1);
        }
    }

    public int getBrightnessForRender(float partialTicks) {
        BlockPos blockpos = new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getCombinedLight(blockpos, 0) : 0;
    }

    public float getBrightness(float partialTicks) {
        BlockPos blockpos = new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getLightBrightness(blockpos) : 0.0f;
    }

    public void setWorld(World worldIn) {
        this.worldObj = worldIn;
    }

    public void setPositionAndRotation(double x2, double y2, double z2, float yaw, float pitch) {
        this.prevPosX = this.posX = x2;
        this.prevPosY = this.posY = y2;
        this.prevPosZ = this.posZ = z2;
        this.prevRotationYaw = this.rotationYaw = yaw;
        this.prevRotationPitch = this.rotationPitch = pitch;
        double d0 = this.prevRotationYaw - yaw;
        if (d0 < -180.0) {
            this.prevRotationYaw += 360.0f;
        }
        if (d0 >= 180.0) {
            this.prevRotationYaw -= 360.0f;
        }
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(yaw, pitch);
    }

    public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn) {
        this.setLocationAndAngles((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, rotationYawIn, rotationPitchIn);
    }

    public void setLocationAndAngles(double x2, double y2, double z2, float yaw, float pitch) {
        this.prevPosX = this.posX = x2;
        this.lastTickPosX = this.posX;
        this.prevPosY = this.posY = y2;
        this.lastTickPosY = this.posY;
        this.prevPosZ = this.posZ = z2;
        this.lastTickPosZ = this.posZ;
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public float getDistanceToEntity(Entity entityIn) {
        float f2 = (float)(this.posX - entityIn.posX);
        float f1 = (float)(this.posY - entityIn.posY);
        float f22 = (float)(this.posZ - entityIn.posZ);
        return MathHelper.sqrt_float(f2 * f2 + f1 * f1 + f22 * f22);
    }

    public double getDistanceSq(double x2, double y2, double z2) {
        double d0 = this.posX - x2;
        double d1 = this.posY - y2;
        double d2 = this.posZ - z2;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double getDistanceSq(BlockPos pos) {
        return pos.distanceSq(this.posX, this.posY, this.posZ);
    }

    public double getDistanceSqToCenter(BlockPos pos) {
        return pos.distanceSqToCenter(this.posX, this.posY, this.posZ);
    }

    public double getDistance(double x2, double y2, double z2) {
        double d0 = this.posX - x2;
        double d1 = this.posY - y2;
        double d2 = this.posZ - z2;
        return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double getDistanceSqToEntity(Entity entityIn) {
        double d0 = this.posX - entityIn.posX;
        double d1 = this.posY - entityIn.posY;
        double d2 = this.posZ - entityIn.posZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void onCollideWithPlayer(EntityPlayer entityIn) {
    }

    public void applyEntityCollision(Entity entityIn) {
        double d1;
        double d0;
        double d2;
        if (entityIn.riddenByEntity != this && entityIn.ridingEntity != this && !entityIn.noClip && !this.noClip && (d2 = MathHelper.abs_max(d0 = entityIn.posX - this.posX, d1 = entityIn.posZ - this.posZ)) >= (double)0.01f) {
            d2 = MathHelper.sqrt_double(d2);
            d0 /= d2;
            d1 /= d2;
            double d3 = 1.0 / d2;
            if (d3 > 1.0) {
                d3 = 1.0;
            }
            d0 *= d3;
            d1 *= d3;
            d0 *= (double)0.05f;
            d1 *= (double)0.05f;
            d0 *= (double)(1.0f - this.entityCollisionReduction);
            d1 *= (double)(1.0f - this.entityCollisionReduction);
            if (this.riddenByEntity == null) {
                this.addVelocity(-d0, 0.0, -d1);
            }
            if (entityIn.riddenByEntity == null) {
                entityIn.addVelocity(d0, 0.0, d1);
            }
        }
    }

    public void addVelocity(double x2, double y2, double z2) {
        this.motionX += x2;
        this.motionY += y2;
        this.motionZ += z2;
        this.isAirBorne = true;
    }

    protected void setBeenAttacked() {
        this.velocityChanged = true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.setBeenAttacked();
        return false;
    }

    public Vec3 getLook(float partialTicks) {
        if (partialTicks == 1.0f) {
            return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
        }
        float f2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
        float f1 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
        return this.getVectorForRotation(f2, f1);
    }

    protected final Vec3 getVectorForRotation(float pitch, float yaw) {
        float f2 = MathHelper.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f22 = -MathHelper.cos(-pitch * ((float)Math.PI / 180));
        float f3 = MathHelper.sin(-pitch * ((float)Math.PI / 180));
        return new Vec3(f1 * f22, f3, f2 * f22);
    }

    public Vec3 getPositionEyes(float partialTicks) {
        if (partialTicks == 1.0f) {
            return new Vec3(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        }
        double d0 = this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks;
        double d1 = this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks + (double)this.getEyeHeight();
        double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks;
        return new Vec3(d0, d1, d2);
    }

    public MovingObjectPosition rayTrace(double blockReachDistance, float partialTicks) {
        Vec3 vec3 = this.getPositionEyes(partialTicks);
        Vec3 vec31 = this.getLook(partialTicks);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
        return this.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean canBePushed() {
        return false;
    }

    public void addToPlayerScore(Entity entityIn, int amount) {
    }

    public boolean isInRangeToRender3d(double x2, double y2, double z2) {
        double d0 = this.posX - x2;
        double d1 = this.posY - y2;
        double d2 = this.posZ - z2;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        return this.isInRangeToRenderDist(d3);
    }

    public boolean isInRangeToRenderDist(double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength();
        if (Double.isNaN(d0)) {
            d0 = 1.0;
        }
        return distance < (d0 = d0 * 64.0 * this.renderDistanceWeight) * d0;
    }

    public boolean writeMountToNBT(NBTTagCompound tagCompund) {
        String s2 = this.getEntityString();
        if (!this.isDead && s2 != null) {
            tagCompund.setString("id", s2);
            this.writeToNBT(tagCompund);
            return true;
        }
        return false;
    }

    public boolean writeToNBTOptional(NBTTagCompound tagCompund) {
        String s2 = this.getEntityString();
        if (!this.isDead && s2 != null && this.riddenByEntity == null) {
            tagCompund.setString("id", s2);
            this.writeToNBT(tagCompund);
            return true;
        }
        return false;
    }

    public void writeToNBT(NBTTagCompound tagCompund) {
        try {
            NBTTagCompound nbttagcompound;
            tagCompund.setTag("Pos", this.newDoubleNBTList(this.posX, this.posY, this.posZ));
            tagCompund.setTag("Motion", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
            tagCompund.setTag("Rotation", this.newFloatNBTList(this.rotationYaw, this.rotationPitch));
            tagCompund.setFloat("FallDistance", this.fallDistance);
            tagCompund.setShort("Fire", (short)this.fire);
            tagCompund.setShort("Air", (short)this.getAir());
            tagCompund.setBoolean("OnGround", this.onGround);
            tagCompund.setInteger("Dimension", this.dimension);
            tagCompund.setBoolean("Invulnerable", this.invulnerable);
            tagCompund.setInteger("PortalCooldown", this.timeUntilPortal);
            tagCompund.setLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
            tagCompund.setLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());
            if (this.getCustomNameTag() != null && this.getCustomNameTag().length() > 0) {
                tagCompund.setString("CustomName", this.getCustomNameTag());
                tagCompund.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
            }
            this.cmdResultStats.writeStatsToNBT(tagCompund);
            if (this.isSilent()) {
                tagCompund.setBoolean("Silent", this.isSilent());
            }
            this.writeEntityToNBT(tagCompund);
            if (this.ridingEntity != null && this.ridingEntity.writeMountToNBT(nbttagcompound = new NBTTagCompound())) {
                tagCompund.setTag("Riding", nbttagcompound);
            }
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    public void readFromNBT(NBTTagCompound tagCompund) {
        try {
            NBTTagList nbttaglist = tagCompund.getTagList("Pos", 6);
            NBTTagList nbttaglist1 = tagCompund.getTagList("Motion", 6);
            NBTTagList nbttaglist2 = tagCompund.getTagList("Rotation", 5);
            this.motionX = nbttaglist1.getDoubleAt(0);
            this.motionY = nbttaglist1.getDoubleAt(1);
            this.motionZ = nbttaglist1.getDoubleAt(2);
            if (Math.abs(this.motionX) > 10.0) {
                this.motionX = 0.0;
            }
            if (Math.abs(this.motionY) > 10.0) {
                this.motionY = 0.0;
            }
            if (Math.abs(this.motionZ) > 10.0) {
                this.motionZ = 0.0;
            }
            this.lastTickPosX = this.posX = nbttaglist.getDoubleAt(0);
            this.prevPosX = this.posX;
            this.lastTickPosY = this.posY = nbttaglist.getDoubleAt(1);
            this.prevPosY = this.posY;
            this.lastTickPosZ = this.posZ = nbttaglist.getDoubleAt(2);
            this.prevPosZ = this.posZ;
            this.prevRotationYaw = this.rotationYaw = nbttaglist2.getFloatAt(0);
            this.prevRotationPitch = this.rotationPitch = nbttaglist2.getFloatAt(1);
            this.setRotationYawHead(this.rotationYaw);
            this.func_181013_g(this.rotationYaw);
            this.fallDistance = tagCompund.getFloat("FallDistance");
            this.fire = tagCompund.getShort("Fire");
            this.setAir(tagCompund.getShort("Air"));
            this.onGround = tagCompund.getBoolean("OnGround");
            this.dimension = tagCompund.getInteger("Dimension");
            this.invulnerable = tagCompund.getBoolean("Invulnerable");
            this.timeUntilPortal = tagCompund.getInteger("PortalCooldown");
            if (tagCompund.hasKey("UUIDMost", 4) && tagCompund.hasKey("UUIDLeast", 4)) {
                this.entityUniqueID = new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast"));
            } else if (tagCompund.hasKey("UUID", 8)) {
                this.entityUniqueID = UUID.fromString(tagCompund.getString("UUID"));
            }
            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
            if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0) {
                this.setCustomNameTag(tagCompund.getString("CustomName"));
            }
            this.setAlwaysRenderNameTag(tagCompund.getBoolean("CustomNameVisible"));
            this.cmdResultStats.readStatsFromNBT(tagCompund);
            this.setSilent(tagCompund.getBoolean("Silent"));
            this.readEntityFromNBT(tagCompund);
            if (this.shouldSetPosAfterLoading()) {
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    protected boolean shouldSetPosAfterLoading() {
        return true;
    }

    protected final String getEntityString() {
        return EntityList.getEntityString(this);
    }

    protected abstract void readEntityFromNBT(NBTTagCompound var1);

    protected abstract void writeEntityToNBT(NBTTagCompound var1);

    public void onChunkLoad() {
    }

    protected NBTTagList newDoubleNBTList(double ... numbers) {
        NBTTagList nbttaglist = new NBTTagList();
        for (double d0 : numbers) {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }
        return nbttaglist;
    }

    protected NBTTagList newFloatNBTList(float ... numbers) {
        NBTTagList nbttaglist = new NBTTagList();
        for (float f2 : numbers) {
            nbttaglist.appendTag(new NBTTagFloat(f2));
        }
        return nbttaglist;
    }

    public EntityItem dropItem(Item itemIn, int size) {
        return this.dropItemWithOffset(itemIn, size, 0.0f);
    }

    public EntityItem dropItemWithOffset(Item itemIn, int size, float offsetY) {
        return this.entityDropItem(new ItemStack(itemIn, size, 0), offsetY);
    }

    public EntityItem entityDropItem(ItemStack itemStackIn, float offsetY) {
        if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
            EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + (double)offsetY, this.posZ, itemStackIn);
            entityitem.setDefaultPickupDelay();
            this.worldObj.spawnEntityInWorld(entityitem);
            return entityitem;
        }
        return null;
    }

    public boolean isEntityAlive() {
        return !this.isDead;
    }

    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip) {
            return false;
        }
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (int i2 = 0; i2 < 8; ++i2) {
            int j2 = MathHelper.floor_double(this.posY + (double)(((float)((i2 >> 0) % 2) - 0.5f) * 0.1f) + (double)this.getEyeHeight());
            int k2 = MathHelper.floor_double(this.posX + (double)(((float)((i2 >> 1) % 2) - 0.5f) * this.width * 0.8f));
            int l2 = MathHelper.floor_double(this.posZ + (double)(((float)((i2 >> 2) % 2) - 0.5f) * this.width * 0.8f));
            if (blockpos$mutableblockpos.getX() == k2 && blockpos$mutableblockpos.getY() == j2 && blockpos$mutableblockpos.getZ() == l2) continue;
            blockpos$mutableblockpos.func_181079_c(k2, j2, l2);
            if (!this.worldObj.getBlockState(blockpos$mutableblockpos).getBlock().isVisuallyOpaque()) continue;
            return true;
        }
        return false;
    }

    public boolean interactFirst(EntityPlayer playerIn) {
        return false;
    }

    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return null;
    }

    public void updateRidden() {
        if (this.ridingEntity.isDead) {
            this.ridingEntity = null;
        } else {
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
            this.onUpdate();
            if (this.ridingEntity != null) {
                this.ridingEntity.updateRiderPosition();
                this.entityRiderYawDelta += (double)(this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);
                this.entityRiderPitchDelta += (double)(this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch);
                while (this.entityRiderYawDelta >= 180.0) {
                    this.entityRiderYawDelta -= 360.0;
                }
                while (this.entityRiderYawDelta < -180.0) {
                    this.entityRiderYawDelta += 360.0;
                }
                while (this.entityRiderPitchDelta >= 180.0) {
                    this.entityRiderPitchDelta -= 360.0;
                }
                while (this.entityRiderPitchDelta < -180.0) {
                    this.entityRiderPitchDelta += 360.0;
                }
                double d0 = this.entityRiderYawDelta * 0.5;
                double d1 = this.entityRiderPitchDelta * 0.5;
                float f2 = 10.0f;
                if (d0 > (double)f2) {
                    d0 = f2;
                }
                if (d0 < (double)(-f2)) {
                    d0 = -f2;
                }
                if (d1 > (double)f2) {
                    d1 = f2;
                }
                if (d1 < (double)(-f2)) {
                    d1 = -f2;
                }
                this.entityRiderYawDelta -= d0;
                this.entityRiderPitchDelta -= d1;
            }
        }
    }

    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
        }
    }

    public double getYOffset() {
        return 0.0;
    }

    public double getMountedYOffset() {
        return (double)this.height * 0.75;
    }

    public void mountEntity(Entity entityIn) {
        this.entityRiderPitchDelta = 0.0;
        this.entityRiderYawDelta = 0.0;
        if (entityIn == null) {
            if (this.ridingEntity != null) {
                this.setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.getEntityBoundingBox().minY + (double)this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
                this.ridingEntity.riddenByEntity = null;
            }
            this.ridingEntity = null;
        } else {
            if (this.ridingEntity != null) {
                this.ridingEntity.riddenByEntity = null;
            }
            if (entityIn != null) {
                Entity entity = entityIn.ridingEntity;
                while (entity != null) {
                    if (entity == this) {
                        return;
                    }
                    entity = entity.ridingEntity;
                }
            }
            this.ridingEntity = entityIn;
            entityIn.riddenByEntity = this;
        }
    }

    public void setPositionAndRotation2(double x2, double y2, double z2, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
        this.setPosition(x2, y2, z2);
        this.setRotation(yaw, pitch);
        List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().contract(0.03125, 0.0, 0.03125));
        if (!list.isEmpty()) {
            double d0 = 0.0;
            for (AxisAlignedBB axisalignedbb : list) {
                if (!(axisalignedbb.maxY > d0)) continue;
                d0 = axisalignedbb.maxY;
            }
            this.setPosition(x2, y2 += d0 - this.getEntityBoundingBox().minY, z2);
        }
    }

    public float getCollisionBorderSize() {
        return 0.1f;
    }

    public Vec3 getLookVec() {
        return null;
    }

    public void func_181015_d(BlockPos p_181015_1_) {
        if (this.timeUntilPortal > 0) {
            this.timeUntilPortal = this.getPortalCooldown();
        } else {
            if (!this.worldObj.isRemote && !p_181015_1_.equals(this.field_181016_an)) {
                this.field_181016_an = p_181015_1_;
                BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal.func_181089_f(this.worldObj, p_181015_1_);
                double d0 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? (double)blockpattern$patternhelper.func_181117_a().getZ() : (double)blockpattern$patternhelper.func_181117_a().getX();
                double d1 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? this.posZ : this.posX;
                d1 = Math.abs(MathHelper.func_181160_c(d1 - (double)(blockpattern$patternhelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0), d0, d0 - (double)blockpattern$patternhelper.func_181118_d()));
                double d2 = MathHelper.func_181160_c(this.posY - 1.0, blockpattern$patternhelper.func_181117_a().getY(), blockpattern$patternhelper.func_181117_a().getY() - blockpattern$patternhelper.func_181119_e());
                this.field_181017_ao = new Vec3(d1, d2, 0.0);
                this.field_181018_ap = blockpattern$patternhelper.getFinger();
            }
            this.inPortal = true;
        }
    }

    public int getPortalCooldown() {
        return 300;
    }

    public void setVelocity(double x2, double y2, double z2) {
        this.motionX = x2;
        this.motionY = y2;
        this.motionZ = z2;
    }

    public void handleStatusUpdate(byte id) {
    }

    public void performHurtAnimation() {
    }

    public ItemStack[] getInventory() {
        return null;
    }

    public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
    }

    public boolean isBurning() {
        boolean flag = this.worldObj != null && this.worldObj.isRemote;
        return !this.isImmuneToFire && (this.fire > 0 || flag && this.getFlag(0));
    }

    public boolean isRiding() {
        return this.ridingEntity != null;
    }

    public boolean isSneaking() {
        return this.getFlag(1);
    }

    public void setSneaking(boolean sneaking) {
        this.setFlag(1, sneaking);
    }

    public boolean isSprinting() {
        return this.getFlag(3);
    }

    public void setSprinting(boolean sprinting) {
        this.setFlag(3, sprinting);
    }

    public boolean isInvisible() {
        return this.getFlag(5);
    }

    public boolean isInvisibleToPlayer(EntityPlayer player) {
        return player.isSpectator() ? false : this.isInvisible();
    }

    public void setInvisible(boolean invisible) {
        this.setFlag(5, invisible);
    }

    public boolean isEating() {
        return this.getFlag(4);
    }

    public void setEating(boolean eating) {
        this.setFlag(4, eating);
    }

    protected boolean getFlag(int flag) {
        return (this.dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0;
    }

    protected void setFlag(int flag, boolean set) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(0);
        if (set) {
            this.dataWatcher.updateObject(0, (byte)(b0 | 1 << flag));
        } else {
            this.dataWatcher.updateObject(0, (byte)(b0 & ~(1 << flag)));
        }
    }

    public int getAir() {
        return this.dataWatcher.getWatchableObjectShort(1);
    }

    public void setAir(int air) {
        this.dataWatcher.updateObject(1, (short)air);
    }

    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        this.attackEntityFrom(DamageSource.lightningBolt, 5.0f);
        ++this.fire;
        if (this.fire == 0) {
            this.setFire(8);
        }
    }

    public void onKillEntity(EntityLivingBase entityLivingIn) {
    }

    protected boolean pushOutOfBlocks(double x2, double y2, double z2) {
        BlockPos blockpos = new BlockPos(x2, y2, z2);
        double d0 = x2 - (double)blockpos.getX();
        double d1 = y2 - (double)blockpos.getY();
        double d2 = z2 - (double)blockpos.getZ();
        List<AxisAlignedBB> list = this.worldObj.func_147461_a(this.getEntityBoundingBox());
        if (list.isEmpty() && !this.worldObj.isBlockFullCube(blockpos)) {
            return false;
        }
        int i2 = 3;
        double d3 = 9999.0;
        if (!this.worldObj.isBlockFullCube(blockpos.west()) && d0 < d3) {
            d3 = d0;
            i2 = 0;
        }
        if (!this.worldObj.isBlockFullCube(blockpos.east()) && 1.0 - d0 < d3) {
            d3 = 1.0 - d0;
            i2 = 1;
        }
        if (!this.worldObj.isBlockFullCube(blockpos.up()) && 1.0 - d1 < d3) {
            d3 = 1.0 - d1;
            i2 = 3;
        }
        if (!this.worldObj.isBlockFullCube(blockpos.north()) && d2 < d3) {
            d3 = d2;
            i2 = 4;
        }
        if (!this.worldObj.isBlockFullCube(blockpos.south()) && 1.0 - d2 < d3) {
            d3 = 1.0 - d2;
            i2 = 5;
        }
        float f2 = this.rand.nextFloat() * 0.2f + 0.1f;
        if (i2 == 0) {
            this.motionX = -f2;
        }
        if (i2 == 1) {
            this.motionX = f2;
        }
        if (i2 == 3) {
            this.motionY = f2;
        }
        if (i2 == 4) {
            this.motionZ = -f2;
        }
        if (i2 == 5) {
            this.motionZ = f2;
        }
        return true;
    }

    public void setInWeb() {
        this.isInWeb = true;
        this.fallDistance = 0.0f;
    }

    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        String s2 = EntityList.getEntityString(this);
        if (s2 == null) {
            s2 = "generic";
        }
        return StatCollector.translateToLocal("entity." + s2 + ".name");
    }

    public Entity[] getParts() {
        return null;
    }

    public boolean isEntityEqual(Entity entityIn) {
        return this == entityIn;
    }

    public float getRotationYawHead() {
        return 0.0f;
    }

    public void setRotationYawHead(float rotation) {
    }

    public void func_181013_g(float p_181013_1_) {
    }

    public boolean canAttackWithItem() {
        return true;
    }

    public boolean hitByEntity(Entity entityIn) {
        return false;
    }

    public String toString() {
        return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", this.getClass().getSimpleName(), this.getName(), this.entityId, this.worldObj == null ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), this.posX, this.posY, this.posZ);
    }

    public boolean isEntityInvulnerable(DamageSource source) {
        return this.invulnerable && source != DamageSource.outOfWorld && !source.isCreativePlayer();
    }

    public void copyLocationAndAnglesFrom(Entity entityIn) {
        this.setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
    }

    public void copyDataFromOld(Entity entityIn) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        entityIn.writeToNBT(nbttagcompound);
        this.readFromNBT(nbttagcompound);
        this.timeUntilPortal = entityIn.timeUntilPortal;
        this.field_181016_an = entityIn.field_181016_an;
        this.field_181017_ao = entityIn.field_181017_ao;
        this.field_181018_ap = entityIn.field_181018_ap;
    }

    public void travelToDimension(int dimensionId) {
        if (!this.worldObj.isRemote && !this.isDead) {
            this.worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            int i2 = this.dimension;
            WorldServer worldserver = minecraftserver.worldServerForDimension(i2);
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionId);
            this.dimension = dimensionId;
            if (i2 == 1 && dimensionId == 1) {
                worldserver1 = minecraftserver.worldServerForDimension(0);
                this.dimension = 0;
            }
            this.worldObj.removeEntity(this);
            this.isDead = false;
            this.worldObj.theProfiler.startSection("reposition");
            minecraftserver.getConfigurationManager().transferEntityToWorld(this, i2, worldserver, worldserver1);
            this.worldObj.theProfiler.endStartSection("reloading");
            Entity entity = EntityList.createEntityByName(EntityList.getEntityString(this), worldserver1);
            if (entity != null) {
                entity.copyDataFromOld(this);
                if (i2 == 1 && dimensionId == 1) {
                    BlockPos blockpos = this.worldObj.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
                    entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);
                }
                worldserver1.spawnEntityInWorld(entity);
            }
            this.isDead = true;
            this.worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            this.worldObj.theProfiler.endSection();
        }
    }

    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlock().getExplosionResistance(this);
    }

    public boolean verifyExplosion(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_) {
        return true;
    }

    public int getMaxFallHeight() {
        return 3;
    }

    public Vec3 func_181014_aG() {
        return this.field_181017_ao;
    }

    public EnumFacing func_181012_aH() {
        return this.field_181018_ap;
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return false;
    }

    public void addEntityCrashInfo(CrashReportCategory category) {
        category.addCrashSectionCallable("Entity Type", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
            }
        });
        category.addCrashSection("Entity ID", this.entityId);
        category.addCrashSectionCallable("Entity Name", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return Entity.this.getName();
            }
        });
        category.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", this.posX, this.posY, this.posZ));
        category.addCrashSection("Entity's Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
        category.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", this.motionX, this.motionY, this.motionZ));
        category.addCrashSectionCallable("Entity's Rider", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return Entity.this.riddenByEntity.toString();
            }
        });
        category.addCrashSectionCallable("Entity's Vehicle", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return Entity.this.ridingEntity.toString();
            }
        });
    }

    public boolean canRenderOnFire() {
        return this.isBurning();
    }

    public UUID getUniqueID() {
        return this.entityUniqueID;
    }

    public boolean isPushedByWater() {
        return true;
    }

    @Override
    public IChatComponent getDisplayName() {
        ChatComponentText chatcomponenttext = new ChatComponentText(this.getName());
        chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
        chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
        return chatcomponenttext;
    }

    public void setCustomNameTag(String name) {
        this.dataWatcher.updateObject(2, name);
    }

    public String getCustomNameTag() {
        return this.dataWatcher.getWatchableObjectString(2);
    }

    public boolean hasCustomName() {
        return this.dataWatcher.getWatchableObjectString(2).length() > 0;
    }

    public void setAlwaysRenderNameTag(boolean alwaysRenderNameTag) {
        this.dataWatcher.updateObject(3, (byte)(alwaysRenderNameTag ? 1 : 0));
    }

    public boolean getAlwaysRenderNameTag() {
        return this.dataWatcher.getWatchableObjectByte(3) == 1;
    }

    public void setPositionAndUpdate(double x2, double y2, double z2) {
        this.setLocationAndAngles(x2, y2, z2, this.rotationYaw, this.rotationPitch);
    }

    public boolean getAlwaysRenderNameTagForRender() {
        return this.getAlwaysRenderNameTag();
    }

    public void onDataWatcherUpdate(int dataID) {
    }

    public EnumFacing getHorizontalFacing() {
        return EnumFacing.getHorizontal(MathHelper.floor_double((double)(this.rotationYaw * 4.0f / 360.0f) + 0.5) & 3);
    }

    protected HoverEvent getHoverEvent() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        String s2 = EntityList.getEntityString(this);
        nbttagcompound.setString("id", this.getUniqueID().toString());
        if (s2 != null) {
            nbttagcompound.setString("type", s2);
        }
        nbttagcompound.setString("name", this.getName());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ChatComponentText(nbttagcompound.toString()));
    }

    public boolean isSpectatedByPlayer(EntityPlayerMP player) {
        return true;
    }

    public AxisAlignedBB getEntityBoundingBox() {
        return this.boundingBox;
    }

    public void setEntityBoundingBox(AxisAlignedBB bb) {
        this.boundingBox = bb;
    }

    public float getEyeHeight() {
        return this.height * 0.85f;
    }

    public boolean isOutsideBorder() {
        return this.isOutsideBorder;
    }

    public void setOutsideBorder(boolean outsideBorder) {
        this.isOutsideBorder = outsideBorder;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        return false;
    }

    @Override
    public void addChatMessage(IChatComponent component) {
    }

    @Override
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return true;
    }

    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY + 0.5, this.posZ);
    }

    @Override
    public Vec3 getPositionVector() {
        return new Vec3(this.posX, this.posY, this.posZ);
    }

    @Override
    public World getEntityWorld() {
        return this.worldObj;
    }

    @Override
    public Entity getCommandSenderEntity() {
        return this;
    }

    @Override
    public boolean sendCommandFeedback() {
        return false;
    }

    @Override
    public void setCommandStat(CommandResultStats.Type type, int amount) {
        this.cmdResultStats.func_179672_a(this, type, amount);
    }

    public CommandResultStats getCommandStats() {
        return this.cmdResultStats;
    }

    public void func_174817_o(Entity entityIn) {
        this.cmdResultStats.func_179671_a(entityIn.getCommandStats());
    }

    public NBTTagCompound getNBTTagCompound() {
        return null;
    }

    public void clientUpdateEntityNBT(NBTTagCompound compound) {
    }

    public boolean interactAt(EntityPlayer player, Vec3 targetVec3) {
        return false;
    }

    public boolean isImmuneToExplosions() {
        return false;
    }

    protected void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            EnchantmentHelper.applyThornEnchantments((EntityLivingBase)entityIn, entityLivingBaseIn);
        }
        EnchantmentHelper.applyArthropodEnchantments(entityLivingBaseIn, entityIn);
    }
}

