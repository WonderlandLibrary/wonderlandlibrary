/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.events.EventSafeWalk;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.client.Minecraft;
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
    protected boolean isInWeb;
    public float rotationYaw;
    private static int nextEntityID;
    public float prevRotationYaw;
    public boolean addedToChunk;
    public AxisAlignedBB boundingBox;
    public float distanceWalkedOnStepModified;
    public float stepHeight;
    public int fireResistance = 1;
    public double prevPosY;
    protected Vec3 field_181017_ao;
    public boolean isCollidedHorizontally;
    protected EnumFacing field_181018_ap;
    public double motionZ;
    protected boolean isImmuneToFire;
    public double posZ;
    protected BlockPos field_181016_an;
    private double entityRiderYawDelta;
    public boolean noClip;
    protected boolean inWater;
    public int chunkCoordX;
    public boolean velocityChanged;
    private int fire;
    public float fallDistance;
    private double entityRiderPitchDelta;
    public boolean ignoreFrustumCheck;
    public boolean forceSpawn;
    public double renderDistanceWeight = 1.0;
    public int ticksExisted;
    public double prevPosZ;
    public float entityCollisionReduction;
    public int serverPosY;
    public boolean isDead;
    public boolean onGround;
    public boolean isAirBorne;
    public double posY;
    private int entityId = nextEntityID++;
    protected UUID entityUniqueID;
    public int hurtResistantTime;
    public int serverPosX;
    public int chunkCoordZ;
    public int chunkCoordY;
    public double lastTickPosX;
    public boolean isCollidedVertically;
    public float prevRotationPitch;
    public double motionY;
    public Entity ridingEntity;
    private int nextStepDistance = 1;
    public double motionX;
    public boolean preventEntitySpawning;
    public float distanceWalkedModified;
    protected int portalCounter;
    private boolean isOutsideBorder;
    protected Random rand;
    public float prevDistanceWalkedModified;
    public double posX;
    public boolean isCollided;
    private boolean invulnerable;
    public float width = 0.6f;
    private final CommandResultStats cmdResultStats;
    public double lastTickPosZ;
    public double prevPosX;
    private static final AxisAlignedBB ZERO_AABB;
    public int dimension;
    public float rotationPitch;
    public int timeUntilPortal;
    public int serverPosZ;
    public Entity riddenByEntity;
    public World worldObj;
    protected boolean firstUpdate = true;
    protected DataWatcher dataWatcher;
    public double lastTickPosY;
    public float height = 1.8f;
    protected boolean inPortal;

    public int getEntityId() {
        return this.entityId;
    }

    public void setOutsideBorder(boolean bl) {
        this.isOutsideBorder = bl;
    }

    @Override
    public void addChatMessage(IChatComponent iChatComponent) {
    }

    public void clientUpdateEntityNBT(NBTTagCompound nBTTagCompound) {
    }

    public boolean isOutsideBorder() {
        return this.isOutsideBorder;
    }

    protected void setFlag(int n, boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(0);
        if (bl) {
            this.dataWatcher.updateObject(0, (byte)(by | 1 << n));
        } else {
            this.dataWatcher.updateObject(0, (byte)(by & ~(1 << n)));
        }
    }

    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        String string = EntityList.getEntityString(this);
        if (string == null) {
            string = "generic";
        }
        return StatCollector.translateToLocal("entity." + string + ".name");
    }

    public void moveToBlockPosAndAngles(BlockPos blockPos, float f, float f2) {
        this.setLocationAndAngles((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5, f, f2);
    }

    public String toString() {
        return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", this.getClass().getSimpleName(), this.getName(), this.entityId, this.worldObj == null ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), this.posX, this.posY, this.posZ);
    }

    public double getDistanceSqToCenter(BlockPos blockPos) {
        return blockPos.distanceSqToCenter(this.posX, this.posY, this.posZ);
    }

    protected void doBlockCollisions() {
        BlockPos blockPos = new BlockPos(this.getEntityBoundingBox().minX + 0.001, this.getEntityBoundingBox().minY + 0.001, this.getEntityBoundingBox().minZ + 0.001);
        BlockPos blockPos2 = new BlockPos(this.getEntityBoundingBox().maxX - 0.001, this.getEntityBoundingBox().maxY - 0.001, this.getEntityBoundingBox().maxZ - 0.001);
        if (this.worldObj.isAreaLoaded(blockPos, blockPos2)) {
            int n = blockPos.getX();
            while (n <= blockPos2.getX()) {
                int n2 = blockPos.getY();
                while (n2 <= blockPos2.getY()) {
                    int n3 = blockPos.getZ();
                    while (n3 <= blockPos2.getZ()) {
                        BlockPos blockPos3 = new BlockPos(n, n2, n3);
                        IBlockState iBlockState = this.worldObj.getBlockState(blockPos3);
                        try {
                            iBlockState.getBlock().onEntityCollidedWithBlock(this.worldObj, blockPos3, iBlockState, this);
                        }
                        catch (Throwable throwable) {
                            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                            CrashReportCategory crashReportCategory = crashReport.makeCategory("Block being collided with");
                            CrashReportCategory.addBlockInfo(crashReportCategory, blockPos3, iBlockState);
                            throw new ReportedException(crashReport);
                        }
                        ++n3;
                    }
                    ++n2;
                }
                ++n;
            }
        }
    }

    public void setSprinting(boolean bl) {
        this.setFlag(3, bl);
    }

    public AxisAlignedBB getCollisionBox(Entity entity) {
        return null;
    }

    public void travelToDimension(int n) {
        if (!this.worldObj.isRemote && !this.isDead) {
            this.worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftServer = MinecraftServer.getServer();
            int n2 = this.dimension;
            WorldServer worldServer = minecraftServer.worldServerForDimension(n2);
            WorldServer worldServer2 = minecraftServer.worldServerForDimension(n);
            this.dimension = n;
            if (n2 == 1 && n == 1) {
                worldServer2 = minecraftServer.worldServerForDimension(0);
                this.dimension = 0;
            }
            this.worldObj.removeEntity(this);
            this.isDead = false;
            this.worldObj.theProfiler.startSection("reposition");
            minecraftServer.getConfigurationManager().transferEntityToWorld(this, n2, worldServer, worldServer2);
            this.worldObj.theProfiler.endStartSection("reloading");
            Entity entity = EntityList.createEntityByName(EntityList.getEntityString(this), worldServer2);
            if (entity != null) {
                entity.copyDataFromOld(this);
                if (n2 == 1 && n == 1) {
                    BlockPos blockPos = this.worldObj.getTopSolidOrLiquidBlock(worldServer2.getSpawnPoint());
                    entity.moveToBlockPosAndAngles(blockPos, entity.rotationYaw, entity.rotationPitch);
                }
                worldServer2.spawnEntityInWorld(entity);
            }
            this.isDead = true;
            this.worldObj.theProfiler.endSection();
            worldServer.resetUpdateEntityTick();
            worldServer2.resetUpdateEntityTick();
            this.worldObj.theProfiler.endSection();
        }
    }

    public double getDistanceSq(BlockPos blockPos) {
        return blockPos.distanceSq(this.posX, this.posY, this.posZ);
    }

    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        try {
            NBTTagList nBTTagList = nBTTagCompound.getTagList("Pos", 6);
            NBTTagList nBTTagList2 = nBTTagCompound.getTagList("Motion", 6);
            NBTTagList nBTTagList3 = nBTTagCompound.getTagList("Rotation", 5);
            this.motionX = nBTTagList2.getDoubleAt(0);
            this.motionY = nBTTagList2.getDoubleAt(1);
            this.motionZ = nBTTagList2.getDoubleAt(2);
            if (Math.abs(this.motionX) > 10.0) {
                this.motionX = 0.0;
            }
            if (Math.abs(this.motionY) > 10.0) {
                this.motionY = 0.0;
            }
            if (Math.abs(this.motionZ) > 10.0) {
                this.motionZ = 0.0;
            }
            this.lastTickPosX = this.posX = nBTTagList.getDoubleAt(0);
            this.prevPosX = this.posX;
            this.lastTickPosY = this.posY = nBTTagList.getDoubleAt(1);
            this.prevPosY = this.posY;
            this.lastTickPosZ = this.posZ = nBTTagList.getDoubleAt(2);
            this.prevPosZ = this.posZ;
            this.prevRotationYaw = this.rotationYaw = nBTTagList3.getFloatAt(0);
            this.prevRotationPitch = this.rotationPitch = nBTTagList3.getFloatAt(1);
            this.setRotationYawHead(this.rotationYaw);
            this.func_181013_g(this.rotationYaw);
            this.fallDistance = nBTTagCompound.getFloat("FallDistance");
            this.fire = nBTTagCompound.getShort("Fire");
            this.setAir(nBTTagCompound.getShort("Air"));
            this.onGround = nBTTagCompound.getBoolean("OnGround");
            this.dimension = nBTTagCompound.getInteger("Dimension");
            this.invulnerable = nBTTagCompound.getBoolean("Invulnerable");
            this.timeUntilPortal = nBTTagCompound.getInteger("PortalCooldown");
            if (nBTTagCompound.hasKey("UUIDMost", 4) && nBTTagCompound.hasKey("UUIDLeast", 4)) {
                this.entityUniqueID = new UUID(nBTTagCompound.getLong("UUIDMost"), nBTTagCompound.getLong("UUIDLeast"));
            } else if (nBTTagCompound.hasKey("UUID", 8)) {
                this.entityUniqueID = UUID.fromString(nBTTagCompound.getString("UUID"));
            }
            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
            if (nBTTagCompound.hasKey("CustomName", 8) && nBTTagCompound.getString("CustomName").length() > 0) {
                this.setCustomNameTag(nBTTagCompound.getString("CustomName"));
            }
            this.setAlwaysRenderNameTag(nBTTagCompound.getBoolean("CustomNameVisible"));
            this.cmdResultStats.readStatsFromNBT(nBTTagCompound);
            this.setSilent(nBTTagCompound.getBoolean("Silent"));
            this.readEntityFromNBT(nBTTagCompound);
            if (this.shouldSetPosAfterLoading()) {
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Entity being loaded");
            this.addEntityCrashInfo(crashReportCategory);
            throw new ReportedException(crashReport);
        }
    }

    public boolean isWet() {
        return this.inWater || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY, this.posZ)) || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY + (double)this.height, this.posZ));
    }

    public void setEating(boolean bl) {
        this.setFlag(4, bl);
    }

    protected NBTTagList newFloatNBTList(float ... fArray) {
        NBTTagList nBTTagList = new NBTTagList();
        float[] fArray2 = fArray;
        int n = fArray.length;
        int n2 = 0;
        while (n2 < n) {
            float f = fArray2[n2];
            nBTTagList.appendTag(new NBTTagFloat(f));
            ++n2;
        }
        return nBTTagList;
    }

    public void playSound(String string, float f, float f2) {
        if (!this.isSilent()) {
            this.worldObj.playSoundAtEntity(this, string, f, f2);
        }
    }

    public boolean isEntityInvulnerable(DamageSource damageSource) {
        return this.invulnerable && damageSource != DamageSource.outOfWorld && !damageSource.isCreativePlayer();
    }

    public boolean writeMountToNBT(NBTTagCompound nBTTagCompound) {
        String string = this.getEntityString();
        if (!this.isDead && string != null) {
            nBTTagCompound.setString("id", string);
            this.writeToNBT(nBTTagCompound);
            return true;
        }
        return false;
    }

    public void moveEntity(double d, double d2, double d3) {
        if (this.noClip) {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(d, d2, d3));
            this.resetPositionToBB();
        } else {
            Block block;
            Object object;
            Object object2;
            this.worldObj.theProfiler.startSection("move");
            double d4 = this.posX;
            double d5 = this.posY;
            double d6 = this.posZ;
            if (this.isInWeb) {
                this.isInWeb = false;
                d *= 0.25;
                d2 *= (double)0.05f;
                d3 *= 0.25;
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            double d7 = d;
            double d8 = d2;
            double d9 = d3;
            boolean bl = this.onGround && this.isSneaking() && this instanceof EntityPlayer;
            EventSafeWalk eventSafeWalk = new EventSafeWalk();
            eventSafeWalk.call();
            Exodus.onEvent(eventSafeWalk);
            if (bl || eventSafeWalk.getSafeWalk() && this.onGround && this instanceof EntityPlayer) {
                double d10 = 0.05;
                while (d != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(d, -1.0, 0.0)).isEmpty()) {
                    d = d < d10 && d >= -d10 ? 0.0 : (d > 0.0 ? (d -= d10) : (d += d10));
                    d7 = d;
                }
                while (d3 != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0, -1.0, d3)).isEmpty()) {
                    d3 = d3 < d10 && d3 >= -d10 ? 0.0 : (d3 > 0.0 ? (d3 -= d10) : (d3 += d10));
                    d9 = d3;
                }
                while (d != 0.0 && d3 != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(d, -1.0, d3)).isEmpty()) {
                    d = d < d10 && d >= -d10 ? 0.0 : (d > 0.0 ? (d -= d10) : (d += d10));
                    d7 = d;
                    d3 = d3 < d10 && d3 >= -d10 ? 0.0 : (d3 > 0.0 ? (d3 -= d10) : (d3 += d10));
                    d9 = d3;
                }
            }
            List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(d, d2, d3));
            AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox();
            for (AxisAlignedBB axisAlignedBB2 : list) {
                d2 = axisAlignedBB2.calculateYOffset(this.getEntityBoundingBox(), d2);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, d2, 0.0));
            boolean bl2 = this.onGround || d8 != d2 && d8 < 0.0;
            for (AxisAlignedBB n : list) {
                d = n.calculateXOffset(this.getEntityBoundingBox(), d);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(d, 0.0, 0.0));
            for (AxisAlignedBB axisAlignedBB3 : list) {
                d3 = axisAlignedBB3.calculateZOffset(this.getEntityBoundingBox(), d3);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, 0.0, d3));
            if (this.stepHeight > 0.0f && bl2 && (d7 != d || d9 != d3)) {
                AxisAlignedBB axisAlignedBB4;
                double d10 = d;
                double d11 = d2;
                double d12 = d3;
                object2 = this.getEntityBoundingBox();
                this.setEntityBoundingBox(axisAlignedBB);
                d2 = this.stepHeight;
                object = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(d7, d2, d9));
                AxisAlignedBB axisAlignedBB5 = this.getEntityBoundingBox();
                AxisAlignedBB axisAlignedBB6 = axisAlignedBB5.addCoord(d7, 0.0, d9);
                double d13 = d2;
                Iterator<AxisAlignedBB> iterator = object.iterator();
                while (iterator.hasNext()) {
                    AxisAlignedBB axisAlignedBB7 = iterator.next();
                    d13 = axisAlignedBB7.calculateYOffset(axisAlignedBB6, d13);
                }
                axisAlignedBB5 = axisAlignedBB5.offset(0.0, d13, 0.0);
                double d14 = d7;
                Iterator<AxisAlignedBB> iterator2 = object.iterator();
                while (iterator2.hasNext()) {
                    AxisAlignedBB axisAlignedBB8 = iterator2.next();
                    d14 = axisAlignedBB8.calculateXOffset(axisAlignedBB5, d14);
                }
                axisAlignedBB5 = axisAlignedBB5.offset(d14, 0.0, 0.0);
                double d15 = d9;
                Iterator<AxisAlignedBB> iterator3 = object.iterator();
                while (iterator3.hasNext()) {
                    axisAlignedBB4 = iterator3.next();
                    d15 = axisAlignedBB4.calculateZOffset(axisAlignedBB5, d15);
                }
                axisAlignedBB5 = axisAlignedBB5.offset(0.0, 0.0, d15);
                axisAlignedBB4 = this.getEntityBoundingBox();
                double d16 = d2;
                Iterator<AxisAlignedBB> iterator4 = object.iterator();
                while (iterator4.hasNext()) {
                    AxisAlignedBB axisAlignedBB9 = iterator4.next();
                    d16 = axisAlignedBB9.calculateYOffset(axisAlignedBB4, d16);
                }
                axisAlignedBB4 = axisAlignedBB4.offset(0.0, d16, 0.0);
                double d17 = d7;
                Iterator iterator5 = object.iterator();
                while (iterator5.hasNext()) {
                    AxisAlignedBB axisAlignedBB10 = (AxisAlignedBB)iterator5.next();
                    d17 = axisAlignedBB10.calculateXOffset(axisAlignedBB4, d17);
                }
                axisAlignedBB4 = axisAlignedBB4.offset(d17, 0.0, 0.0);
                double d18 = d9;
                Iterator iterator6 = object.iterator();
                while (iterator6.hasNext()) {
                    AxisAlignedBB axisAlignedBB11 = (AxisAlignedBB)iterator6.next();
                    d18 = axisAlignedBB11.calculateZOffset(axisAlignedBB4, d18);
                }
                axisAlignedBB4 = axisAlignedBB4.offset(0.0, 0.0, d18);
                double d19 = d14 * d14 + d15 * d15;
                double d20 = d17 * d17 + d18 * d18;
                if (d19 > d20) {
                    d = d14;
                    d3 = d15;
                    d2 = -d13;
                    this.setEntityBoundingBox(axisAlignedBB5);
                } else {
                    d = d17;
                    d3 = d18;
                    d2 = -d16;
                    this.setEntityBoundingBox(axisAlignedBB4);
                }
                Iterator iterator7 = object.iterator();
                while (iterator7.hasNext()) {
                    AxisAlignedBB axisAlignedBB12 = (AxisAlignedBB)iterator7.next();
                    d2 = axisAlignedBB12.calculateYOffset(this.getEntityBoundingBox(), d2);
                }
                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, d2, 0.0));
                if (d10 * d10 + d12 * d12 >= d * d + d3 * d3) {
                    d = d10;
                    d2 = d11;
                    d3 = d12;
                    this.setEntityBoundingBox((AxisAlignedBB)object2);
                }
            }
            this.worldObj.theProfiler.endSection();
            this.worldObj.theProfiler.startSection("rest");
            this.resetPositionToBB();
            this.isCollidedHorizontally = d7 != d || d9 != d3;
            this.isCollidedVertically = d8 != d2;
            this.onGround = this.isCollidedVertically && d8 < 0.0;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            int n = MathHelper.floor_double(this.posX);
            int n2 = MathHelper.floor_double(this.posY - (double)0.2f);
            int n3 = MathHelper.floor_double(this.posZ);
            BlockPos blockPos = new BlockPos(n, n2, n3);
            Block block2 = this.worldObj.getBlockState(blockPos).getBlock();
            if (block2.getMaterial() == Material.air && ((block = this.worldObj.getBlockState(blockPos.down()).getBlock()) instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate)) {
                block2 = block;
                blockPos = blockPos.down();
            }
            this.updateFallState(d2, this.onGround, block2, blockPos);
            if (d7 != d) {
                this.motionX = 0.0;
            }
            if (d9 != d3) {
                this.motionZ = 0.0;
            }
            if (d8 != d2) {
                block2.onLanded(this.worldObj, this);
            }
            if (this.canTriggerWalking() && !bl && this.ridingEntity == null) {
                double d21 = this.posX - d4;
                double d22 = this.posY - d5;
                double d23 = this.posZ - d6;
                if (block2 != Blocks.ladder) {
                    d22 = 0.0;
                }
                if (block2 != null && this.onGround) {
                    block2.onEntityCollidedWithBlock(this.worldObj, blockPos, this);
                }
                this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d21 * d21 + d23 * d23) * 0.6);
                this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt_double(d21 * d21 + d22 * d22 + d23 * d23) * 0.6);
                if (this.distanceWalkedOnStepModified > (float)this.nextStepDistance && block2.getMaterial() != Material.air) {
                    this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
                    if (this.isInWater()) {
                        float f = MathHelper.sqrt_double(this.motionX * this.motionX * (double)0.2f + this.motionY * this.motionY + this.motionZ * this.motionZ * (double)0.2f) * 0.35f;
                        if (f > 1.0f) {
                            f = 1.0f;
                        }
                        this.playSound(this.getSwimSound(), f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                    }
                    this.playStepSound(blockPos, block2);
                }
            }
            try {
                this.doBlockCollisions();
            }
            catch (Throwable throwable) {
                object2 = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                object = ((CrashReport)object2).makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo((CrashReportCategory)object);
                throw new ReportedException((CrashReport)object2);
            }
            boolean bl3 = this.isWet();
            if (this.worldObj.isFlammableWithin(this.getEntityBoundingBox().contract(0.001, 0.001, 0.001))) {
                this.dealFireDamage(1);
                if (!bl3) {
                    ++this.fire;
                    if (this.fire == 0) {
                        this.setFire(8);
                    }
                }
            } else if (this.fire <= 0) {
                this.fire = -this.fireResistance;
            }
            if (bl3 && this.fire > 0) {
                this.playSound("random.fizz", 0.7f, 1.6f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                this.fire = -this.fireResistance;
            }
            this.worldObj.theProfiler.endSection();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(int n, String string) {
        return true;
    }

    public boolean canRenderOnFire() {
        return this.isBurning();
    }

    public int hashCode() {
        return this.entityId;
    }

    protected abstract void readEntityFromNBT(NBTTagCompound var1);

    public boolean isInLava() {
        return this.worldObj.isMaterialInBB(this.getEntityBoundingBox().expand(-0.1f, -0.4f, -0.1f), Material.lava);
    }

    public int getBrightnessForRender(float f) {
        BlockPos blockPos = new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        return this.worldObj.isBlockLoaded(blockPos) ? this.worldObj.getCombinedLight(blockPos, 0) : 0;
    }

    public void setInvisible(boolean bl) {
        this.setFlag(5, bl);
    }

    public float getRotationYawHead() {
        return 0.0f;
    }

    public void setServerAngles(float f, float f2) {
        Minecraft.getMinecraft();
        float f3 = Minecraft.thePlayer.serverSidePitch;
        Minecraft.getMinecraft();
        float f4 = Minecraft.thePlayer.serverSideYaw;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.thePlayer.serverSideYaw = (float)((double)Minecraft.thePlayer.serverSideYaw + (double)f * 0.15);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.thePlayer.serverSidePitch = (float)((double)Minecraft.thePlayer.serverSidePitch - (double)f2 * 0.15);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.thePlayer.serverSidePitch = MathHelper.clamp_float(Minecraft.thePlayer.serverSidePitch, -90.0f, 90.0f);
        Minecraft.getMinecraft();
        float f5 = Minecraft.thePlayer.prevServerSidePitch;
        Minecraft.getMinecraft();
        Minecraft.thePlayer.prevServerSidePitch = f5 + (Minecraft.thePlayer.serverSidePitch - f3);
        Minecraft.getMinecraft();
        float f6 = Minecraft.thePlayer.prevServerSideYaw;
        Minecraft.getMinecraft();
        Minecraft.thePlayer.prevServerSideYaw = f6 + (Minecraft.thePlayer.serverSideYaw - f4);
    }

    public boolean getAlwaysRenderNameTag() {
        return this.dataWatcher.getWatchableObjectByte(3) == 1;
    }

    public boolean isOffsetPositionInLiquid(double d, double d2, double d3) {
        AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox().offset(d, d2, d3);
        return this.isLiquidPresentInAABB(axisAlignedBB);
    }

    protected void dealFireDamage(int n) {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.inFire, n);
        }
    }

    public void setSilent(boolean bl) {
        this.dataWatcher.updateObject(4, (byte)(bl ? 1 : 0));
    }

    protected void kill() {
        this.setDead();
    }

    public void handleStatusUpdate(byte by) {
    }

    public boolean canBePushed() {
        return false;
    }

    protected void setSize(float f, float f2) {
        if (f != this.width || f2 != this.height) {
            float f3 = this.width;
            this.width = f;
            this.height = f2;
            this.setEntityBoundingBox(new AxisAlignedBB(this.getEntityBoundingBox().minX, this.getEntityBoundingBox().minY, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().minX + (double)this.width, this.getEntityBoundingBox().minY + (double)this.height, this.getEntityBoundingBox().minZ + (double)this.width));
            if (this.width > f3 && !this.firstUpdate && !this.worldObj.isRemote) {
                this.moveEntity(f3 - this.width, 0.0, f3 - this.width);
            }
        }
    }

    public void setVelocity(double d, double d2, double d3) {
        this.motionX = d;
        this.motionY = d2;
        this.motionZ = d3;
    }

    public boolean isInWater() {
        return this.inWater;
    }

    public void onUpdate() {
        this.onEntityUpdate();
    }

    @Override
    public IChatComponent getDisplayName() {
        ChatComponentText chatComponentText = new ChatComponentText(this.getName());
        chatComponentText.getChatStyle().setChatHoverEvent(this.getHoverEvent());
        chatComponentText.getChatStyle().setInsertion(this.getUniqueID().toString());
        return chatComponentText;
    }

    public void setCustomNameTag(String string) {
        this.dataWatcher.updateObject(2, string);
    }

    public Vec3 getLookVec() {
        return null;
    }

    public boolean isInvisible() {
        return this.getFlag(5);
    }

    public int getMaxInPortalTime() {
        return 0;
    }

    protected void updateFallState(double d, boolean bl, Block block, BlockPos blockPos) {
        if (bl) {
            if (this.fallDistance > 0.0f) {
                if (block != null) {
                    block.onFallenUpon(this.worldObj, blockPos, this, this.fallDistance);
                } else {
                    this.fall(this.fallDistance, 1.0f);
                }
                this.fallDistance = 0.0f;
            }
        } else if (d < 0.0) {
            this.fallDistance = (float)((double)this.fallDistance - d);
        }
    }

    public void applyEntityCollision(Entity entity) {
        double d;
        double d2;
        double d3;
        if (entity.riddenByEntity != this && entity.ridingEntity != this && !entity.noClip && !this.noClip && (d3 = MathHelper.abs_max(d2 = entity.posX - this.posX, d = entity.posZ - this.posZ)) >= (double)0.01f) {
            d3 = MathHelper.sqrt_double(d3);
            d2 /= d3;
            d /= d3;
            double d4 = 1.0 / d3;
            if (d4 > 1.0) {
                d4 = 1.0;
            }
            d2 *= d4;
            d *= d4;
            d2 *= (double)0.05f;
            d *= (double)0.05f;
            d2 *= (double)(1.0f - this.entityCollisionReduction);
            d *= (double)(1.0f - this.entityCollisionReduction);
            if (this.riddenByEntity == null) {
                this.addVelocity(-d2, 0.0, -d);
            }
            if (entity.riddenByEntity == null) {
                entity.addVelocity(d2, 0.0, d);
            }
        }
    }

    protected String getSplashSound() {
        return "game.neutral.swim.splash";
    }

    public boolean isEntityEqual(Entity entity) {
        return this == entity;
    }

    public void copyDataFromOld(Entity entity) {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        entity.writeToNBT(nBTTagCompound);
        this.readFromNBT(nBTTagCompound);
        this.timeUntilPortal = entity.timeUntilPortal;
        this.field_181016_an = entity.field_181016_an;
        this.field_181017_ao = entity.field_181017_ao;
        this.field_181018_ap = entity.field_181018_ap;
    }

    public void mountEntity(Entity entity) {
        this.entityRiderPitchDelta = 0.0;
        this.entityRiderYawDelta = 0.0;
        if (entity == null) {
            if (this.ridingEntity != null) {
                this.setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.getEntityBoundingBox().minY + (double)this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
                this.ridingEntity.riddenByEntity = null;
            }
            this.ridingEntity = null;
        } else {
            if (this.ridingEntity != null) {
                this.ridingEntity.riddenByEntity = null;
            }
            if (entity != null) {
                Entity entity2 = entity.ridingEntity;
                while (entity2 != null) {
                    if (entity2 == this) {
                        return;
                    }
                    entity2 = entity2.ridingEntity;
                }
            }
            this.ridingEntity = entity;
            entity.riddenByEntity = this;
        }
    }

    protected final Vec3 getVectorForRotation(float f, float f2) {
        float f3 = MathHelper.cos(-f2 * ((float)Math.PI / 180) - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * ((float)Math.PI / 180) - (float)Math.PI);
        float f5 = -MathHelper.cos(-f * ((float)Math.PI / 180));
        float f6 = MathHelper.sin(-f * ((float)Math.PI / 180));
        return new Vec3(f4 * f5, f6, f3 * f5);
    }

    public void setFire(int n) {
        int n2 = n * 20;
        if (this.fire < (n2 = EnchantmentProtection.getFireTimeForEntity(this, n2))) {
            this.fire = n2;
        }
    }

    public boolean interactAt(EntityPlayer entityPlayer, Vec3 vec3) {
        return false;
    }

    public void setLocationAndAngles(double d, double d2, double d3, float f, float f2) {
        this.prevPosX = this.posX = d;
        this.lastTickPosX = this.posX;
        this.prevPosY = this.posY = d2;
        this.lastTickPosY = this.posY;
        this.prevPosZ = this.posZ = d3;
        this.lastTickPosZ = this.posZ;
        this.rotationYaw = f;
        this.rotationPitch = f2;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    protected HoverEvent getHoverEvent() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        String string = EntityList.getEntityString(this);
        nBTTagCompound.setString("id", this.getUniqueID().toString());
        if (string != null) {
            nBTTagCompound.setString("type", string);
        }
        nBTTagCompound.setString("name", this.getName());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ChatComponentText(nBTTagCompound.toString()));
    }

    public int getPortalCooldown() {
        return 300;
    }

    private boolean isLiquidPresentInAABB(AxisAlignedBB axisAlignedBB) {
        return this.worldObj.getCollidingBoundingBoxes(this, axisAlignedBB).isEmpty() && !this.worldObj.isAnyLiquid(axisAlignedBB);
    }

    @Override
    public Vec3 getPositionVector() {
        return new Vec3(this.posX, this.posY, this.posZ);
    }

    @Override
    public boolean sendCommandFeedback() {
        return false;
    }

    public boolean isInRangeToRender3d(double d, double d2, double d3) {
        double d4 = this.posX - d;
        double d5 = this.posY - d2;
        double d6 = this.posZ - d3;
        double d7 = d4 * d4 + d5 * d5 + d6 * d6;
        return this.isInRangeToRenderDist(d7);
    }

    public MovingObjectPosition rayTrace(double d, float f) {
        Vec3 vec3 = this.getPositionEyes(f);
        Vec3 vec32 = this.getLook(f);
        Vec3 vec33 = vec3.addVector(vec32.xCoord * d, vec32.yCoord * d, vec32.zCoord * d);
        return this.worldObj.rayTraceBlocks(vec3, vec33, false, false, true);
    }

    public void moveFlying(float f, float f2, float f3) {
        float f4 = f * f + f2 * f2;
        if (f4 >= 1.0E-4f) {
            if ((f4 = MathHelper.sqrt_float(f4)) < 1.0f) {
                f4 = 1.0f;
            }
            f4 = f3 / f4;
            float f5 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f);
            float f6 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f);
            this.motionX += (double)((f *= f4) * f6 - (f2 *= f4) * f5);
            this.motionZ += (double)(f2 * f6 + f * f5);
        }
    }

    protected void playStepSound(BlockPos blockPos, Block block) {
        Block.SoundType soundType = block.stepSound;
        if (this.worldObj.getBlockState(blockPos.up()).getBlock() == Blocks.snow_layer) {
            soundType = Blocks.snow_layer.stepSound;
            this.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15f, soundType.getFrequency());
        } else if (!block.getMaterial().isLiquid()) {
            this.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15f, soundType.getFrequency());
        }
    }

    public double getDistanceSq(double d, double d2, double d3) {
        double d4 = this.posX - d;
        double d5 = this.posY - d2;
        double d6 = this.posZ - d3;
        return d4 * d4 + d5 * d5 + d6 * d6;
    }

    protected String getSwimSound() {
        return "game.neutral.swim";
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return false;
    }

    public boolean replaceItemInInventory(int n, ItemStack itemStack) {
        return false;
    }

    public boolean isImmuneToExplosions() {
        return false;
    }

    public Entity[] getParts() {
        return null;
    }

    public void fall(float f, float f2) {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.fall(f, f2);
        }
    }

    public boolean verifyExplosion(Explosion explosion, World world, BlockPos blockPos, IBlockState iBlockState, float f) {
        return true;
    }

    protected final String getEntityString() {
        return EntityList.getEntityString(this);
    }

    protected abstract void writeEntityToNBT(NBTTagCompound var1);

    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        this.setBeenAttacked();
        return false;
    }

    public void func_181015_d(BlockPos blockPos) {
        if (this.timeUntilPortal > 0) {
            this.timeUntilPortal = this.getPortalCooldown();
        } else {
            if (!this.worldObj.isRemote && !blockPos.equals(this.field_181016_an)) {
                this.field_181016_an = blockPos;
                BlockPattern.PatternHelper patternHelper = Blocks.portal.func_181089_f(this.worldObj, blockPos);
                double d = patternHelper.getFinger().getAxis() == EnumFacing.Axis.X ? (double)patternHelper.func_181117_a().getZ() : (double)patternHelper.func_181117_a().getX();
                double d2 = patternHelper.getFinger().getAxis() == EnumFacing.Axis.X ? this.posZ : this.posX;
                d2 = Math.abs(MathHelper.func_181160_c(d2 - (double)(patternHelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0), d, d - (double)patternHelper.func_181118_d()));
                double d3 = MathHelper.func_181160_c(this.posY - 1.0, patternHelper.func_181117_a().getY(), patternHelper.func_181117_a().getY() - patternHelper.func_181119_e());
                this.field_181017_ao = new Vec3(d2, d3, 0.0);
                this.field_181018_ap = patternHelper.getFinger();
            }
            this.inPortal = true;
        }
    }

    static {
        ZERO_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    protected abstract void entityInit();

    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY + 0.5, this.posZ);
    }

    public void onStruckByLightning(EntityLightningBolt entityLightningBolt) {
        this.attackEntityFrom(DamageSource.lightningBolt, 5.0f);
        ++this.fire;
        if (this.fire == 0) {
            this.setFire(8);
        }
    }

    protected boolean getFlag(int n) {
        return (this.dataWatcher.getWatchableObjectByte(0) & 1 << n) != 0;
    }

    public void setInWeb() {
        this.isInWeb = true;
        this.fallDistance = 0.0f;
    }

    public void setPosition(double d, double d2, double d3) {
        this.posX = d;
        this.posY = d2;
        this.posZ = d3;
        float f = this.width / 2.0f;
        float f2 = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(d - (double)f, d2, d3 - (double)f, d + (double)f, d2 + (double)f2, d3 + (double)f));
    }

    public EnumFacing func_181012_aH() {
        return this.field_181018_ap;
    }

    public boolean isEating() {
        return this.getFlag(4);
    }

    public double getMountedYOffset() {
        return (double)this.height * 0.75;
    }

    public EnumFacing getHorizontalFacing() {
        return EnumFacing.getHorizontal(MathHelper.floor_double((double)(this.rotationYaw * 4.0f / 360.0f) + 0.5) & 3);
    }

    public void onDataWatcherUpdate(int n) {
    }

    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        try {
            NBTTagCompound nBTTagCompound2;
            nBTTagCompound.setTag("Pos", this.newDoubleNBTList(this.posX, this.posY, this.posZ));
            nBTTagCompound.setTag("Motion", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
            nBTTagCompound.setTag("Rotation", this.newFloatNBTList(this.rotationYaw, this.rotationPitch));
            nBTTagCompound.setFloat("FallDistance", this.fallDistance);
            nBTTagCompound.setShort("Fire", (short)this.fire);
            nBTTagCompound.setShort("Air", (short)this.getAir());
            nBTTagCompound.setBoolean("OnGround", this.onGround);
            nBTTagCompound.setInteger("Dimension", this.dimension);
            nBTTagCompound.setBoolean("Invulnerable", this.invulnerable);
            nBTTagCompound.setInteger("PortalCooldown", this.timeUntilPortal);
            nBTTagCompound.setLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
            nBTTagCompound.setLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());
            if (this.getCustomNameTag() != null && this.getCustomNameTag().length() > 0) {
                nBTTagCompound.setString("CustomName", this.getCustomNameTag());
                nBTTagCompound.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
            }
            this.cmdResultStats.writeStatsToNBT(nBTTagCompound);
            if (this.isSilent()) {
                nBTTagCompound.setBoolean("Silent", this.isSilent());
            }
            this.writeEntityToNBT(nBTTagCompound);
            if (this.ridingEntity != null && this.ridingEntity.writeMountToNBT(nBTTagCompound2 = new NBTTagCompound())) {
                nBTTagCompound.setTag("Riding", nBTTagCompound2);
            }
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Entity being saved");
            this.addEntityCrashInfo(crashReportCategory);
            throw new ReportedException(crashReport);
        }
    }

    protected void setRotation(float f, float f2) {
        this.rotationYaw = f % 360.0f;
        this.rotationPitch = f2 % 360.0f;
    }

    protected void setBeenAttacked() {
        this.velocityChanged = true;
    }

    public void onCollideWithPlayer(EntityPlayer entityPlayer) {
    }

    public void setAir(int n) {
        this.dataWatcher.updateObject(1, (short)n);
    }

    public Vec3 getPositionEyes(float f) {
        if (f == 1.0f) {
            return new Vec3(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        }
        double d = this.prevPosX + (this.posX - this.prevPosX) * (double)f;
        double d2 = this.prevPosY + (this.posY - this.prevPosY) * (double)f + (double)this.getEyeHeight();
        double d3 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f;
        return new Vec3(d, d2, d3);
    }

    public ItemStack[] getInventory() {
        return null;
    }

    public boolean isSpectatedByPlayer(EntityPlayerMP entityPlayerMP) {
        return true;
    }

    public NBTTagCompound getNBTTagCompound() {
        return null;
    }

    @Override
    public World getEntityWorld() {
        return this.worldObj;
    }

    public void setPositionAndRotation2(double d, double d2, double d3, float f, float f2, int n, boolean bl) {
        this.setPosition(d, d2, d3);
        this.setRotation(f, f2);
        List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().contract(0.03125, 0.0, 0.03125));
        if (!list.isEmpty()) {
            double d4 = 0.0;
            for (AxisAlignedBB axisAlignedBB : list) {
                if (!(axisAlignedBB.maxY > d4)) continue;
                d4 = axisAlignedBB.maxY;
            }
            this.setPosition(d, d2 += d4 - this.getEntityBoundingBox().minY, d3);
        }
    }

    protected void resetHeight() {
        float f;
        float f2;
        float f3 = MathHelper.sqrt_double(this.motionX * this.motionX * (double)0.2f + this.motionY * this.motionY + this.motionZ * this.motionZ * (double)0.2f) * 0.2f;
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        this.playSound(this.getSplashSound(), f3, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
        float f4 = MathHelper.floor_double(this.getEntityBoundingBox().minY);
        int n = 0;
        while ((float)n < 1.0f + this.width * 20.0f) {
            f2 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            f = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (double)f2, (double)(f4 + 1.0f), this.posZ + (double)f, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2f), this.motionZ, new int[0]);
            ++n;
        }
        n = 0;
        while ((float)n < 1.0f + this.width * 20.0f) {
            f2 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            f = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double)f2, (double)(f4 + 1.0f), this.posZ + (double)f, this.motionX, this.motionY, this.motionZ, new int[0]);
            ++n;
        }
    }

    public EntityItem dropItemWithOffset(Item item, int n, float f) {
        return this.entityDropItem(new ItemStack(item, n, 0), f);
    }

    public boolean hasCustomName() {
        return this.dataWatcher.getWatchableObjectString(2).length() > 0;
    }

    protected boolean canTriggerWalking() {
        return true;
    }

    public void onKillCommand() {
        this.setDead();
    }

    public Entity(World world) {
        this.boundingBox = ZERO_AABB;
        this.rand = new Random();
        this.entityUniqueID = MathHelper.getRandomUuid(this.rand);
        this.cmdResultStats = new CommandResultStats();
        this.worldObj = world;
        this.setPosition(0.0, 0.0, 0.0);
        if (world != null) {
            this.dimension = world.provider.getDimensionId();
        }
        this.dataWatcher = new DataWatcher(this);
        this.dataWatcher.addObject(0, (byte)0);
        this.dataWatcher.addObject(1, (short)300);
        this.dataWatcher.addObject(3, (byte)0);
        this.dataWatcher.addObject(2, "");
        this.dataWatcher.addObject(4, (byte)0);
        this.entityInit();
    }

    public boolean writeToNBTOptional(NBTTagCompound nBTTagCompound) {
        String string = this.getEntityString();
        if (!this.isDead && string != null && this.riddenByEntity == null) {
            nBTTagCompound.setString("id", string);
            this.writeToNBT(nBTTagCompound);
            return true;
        }
        return false;
    }

    public boolean getAlwaysRenderNameTagForRender() {
        return this.getAlwaysRenderNameTag();
    }

    public double getYOffset() {
        return 0.0;
    }

    public boolean isPushedByWater() {
        return true;
    }

    public void copyLocationAndAnglesFrom(Entity entity) {
        this.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
    }

    public void setPositionAndUpdate(double d, double d2, double d3) {
        this.setLocationAndAngles(d, d2, d3, this.rotationYaw, this.rotationPitch);
    }

    public void setPositionAndRotation(double d, double d2, double d3, float f, float f2) {
        this.prevPosX = this.posX = d;
        this.prevPosY = this.posY = d2;
        this.prevPosZ = this.posZ = d3;
        this.prevRotationYaw = this.rotationYaw = f;
        this.prevRotationPitch = this.rotationPitch = f2;
        double d4 = this.prevRotationYaw - f;
        if (d4 < -180.0) {
            this.prevRotationYaw += 360.0f;
        }
        if (d4 >= 180.0) {
            this.prevRotationYaw -= 360.0f;
        }
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(f, f2);
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
                double d = this.entityRiderYawDelta * 0.5;
                double d2 = this.entityRiderPitchDelta * 0.5;
                float f = 10.0f;
                if (d > (double)f) {
                    d = f;
                }
                if (d < (double)(-f)) {
                    d = -f;
                }
                if (d2 > (double)f) {
                    d2 = f;
                }
                if (d2 < (double)(-f)) {
                    d2 = -f;
                }
                this.entityRiderYawDelta -= d;
                this.entityRiderPitchDelta -= d2;
            }
        }
    }

    public float getBrightness(float f) {
        BlockPos blockPos = new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        return this.worldObj.isBlockLoaded(blockPos) ? this.worldObj.getLightBrightness(blockPos) : 0.0f;
    }

    public void onKillEntity(EntityLivingBase entityLivingBase) {
    }

    public Vec3 func_181014_aG() {
        return this.field_181017_ao;
    }

    private void resetPositionToBB() {
        this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
        this.posY = this.getEntityBoundingBox().minY;
        this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;
    }

    public void func_174817_o(Entity entity) {
        this.cmdResultStats.func_179671_a(entity.getCommandStats());
    }

    public void setAngles(float f, float f2) {
        float f3 = this.rotationPitch;
        float f4 = this.rotationYaw;
        this.rotationYaw = (float)((double)this.rotationYaw + (double)f * 0.15);
        this.rotationPitch = (float)((double)this.rotationPitch - (double)f2 * 0.15);
        this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0f, 90.0f);
        this.prevRotationPitch += this.rotationPitch - f3;
        this.prevRotationYaw += this.rotationYaw - f4;
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

    public EntityItem entityDropItem(ItemStack itemStack, float f) {
        if (itemStack.stackSize != 0 && itemStack.getItem() != null) {
            EntityItem entityItem = new EntityItem(this.worldObj, this.posX, this.posY + (double)f, this.posZ, itemStack);
            entityItem.setDefaultPickupDelay();
            this.worldObj.spawnEntityInWorld(entityItem);
            return entityItem;
        }
        return null;
    }

    public void extinguish() {
        this.fire = 0;
    }

    @Override
    public void setCommandStat(CommandResultStats.Type type, int n) {
        this.cmdResultStats.func_179672_a(this, type, n);
    }

    protected void createRunningParticles() {
        int n;
        int n2;
        int n3 = MathHelper.floor_double(this.posX);
        BlockPos blockPos = new BlockPos(n3, n2 = MathHelper.floor_double(this.posY - (double)0.2f), n = MathHelper.floor_double(this.posZ));
        IBlockState iBlockState = this.worldObj.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        if (block.getRenderType() != -1) {
            this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5) * (double)this.width, this.getEntityBoundingBox().minY + 0.1, this.posZ + ((double)this.rand.nextFloat() - 0.5) * (double)this.width, -this.motionX * 4.0, 1.5, -this.motionZ * 4.0, Block.getStateId(iBlockState));
        }
    }

    public boolean isInsideOfMaterial(Material material) {
        double d = this.posY + (double)this.getEyeHeight();
        BlockPos blockPos = new BlockPos(this.posX, d, this.posZ);
        IBlockState iBlockState = this.worldObj.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        if (block.getMaterial() == material) {
            float f = BlockLiquid.getLiquidHeightPercent(iBlockState.getBlock().getMetaFromState(iBlockState)) - 0.11111111f;
            float f2 = (float)(blockPos.getY() + 1) - f;
            boolean bl = d < (double)f2;
            return !bl && this instanceof EntityPlayer ? false : bl;
        }
        return false;
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
            MinecraftServer minecraftServer = ((WorldServer)this.worldObj).getMinecraftServer();
            int n = this.getMaxInPortalTime();
            if (this.inPortal) {
                if (minecraftServer.getAllowNether()) {
                    if (this.ridingEntity == null && this.portalCounter++ >= n) {
                        this.portalCounter = n;
                        this.timeUntilPortal = this.getPortalCooldown();
                        int n2 = this.worldObj.provider.getDimensionId() == -1 ? 0 : -1;
                        this.travelToDimension(n2);
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

    public Vec3 getLook(float f) {
        if (f == 1.0f) {
            return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
        }
        float f2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * f;
        float f3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * f;
        return this.getVectorForRotation(f2, f3);
    }

    public DataWatcher getDataWatcher() {
        return this.dataWatcher;
    }

    public float getExplosionResistance(Explosion explosion, World world, BlockPos blockPos, IBlockState iBlockState) {
        return iBlockState.getBlock().getExplosionResistance(this);
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public void setEntityBoundingBox(AxisAlignedBB axisAlignedBB) {
        this.boundingBox = axisAlignedBB;
    }

    public void performHurtAnimation() {
    }

    public boolean canAttackWithItem() {
        return true;
    }

    @Override
    public Entity getCommandSenderEntity() {
        return this;
    }

    public boolean isInvisibleToPlayer(EntityPlayer entityPlayer) {
        return entityPlayer.isSpectator() ? false : this.isInvisible();
    }

    protected boolean pushOutOfBlocks(double d, double d2, double d3) {
        BlockPos blockPos = new BlockPos(d, d2, d3);
        double d4 = d - (double)blockPos.getX();
        double d5 = d2 - (double)blockPos.getY();
        double d6 = d3 - (double)blockPos.getZ();
        List<AxisAlignedBB> list = this.worldObj.func_147461_a(this.getEntityBoundingBox());
        if (list.isEmpty() && !this.worldObj.isBlockFullCube(blockPos)) {
            return false;
        }
        int n = 3;
        double d7 = 9999.0;
        if (!this.worldObj.isBlockFullCube(blockPos.west()) && d4 < d7) {
            d7 = d4;
            n = 0;
        }
        if (!this.worldObj.isBlockFullCube(blockPos.east()) && 1.0 - d4 < d7) {
            d7 = 1.0 - d4;
            n = 1;
        }
        if (!this.worldObj.isBlockFullCube(blockPos.up()) && 1.0 - d5 < d7) {
            d7 = 1.0 - d5;
            n = 3;
        }
        if (!this.worldObj.isBlockFullCube(blockPos.north()) && d6 < d7) {
            d7 = d6;
            n = 4;
        }
        if (!this.worldObj.isBlockFullCube(blockPos.south()) && 1.0 - d6 < d7) {
            d7 = 1.0 - d6;
            n = 5;
        }
        float f = this.rand.nextFloat() * 0.2f + 0.1f;
        if (n == 0) {
            this.motionX = -f;
        }
        if (n == 1) {
            this.motionX = f;
        }
        if (n == 3) {
            this.motionY = f;
        }
        if (n == 4) {
            this.motionZ = -f;
        }
        if (n == 5) {
            this.motionZ = f;
        }
        return true;
    }

    public UUID getUniqueID() {
        return this.entityUniqueID;
    }

    public boolean hitByEntity(Entity entity) {
        return false;
    }

    public void addVelocity(double d, double d2, double d3) {
        this.motionX += d;
        this.motionY += d2;
        this.motionZ += d3;
        this.isAirBorne = true;
    }

    protected void applyEnchantments(EntityLivingBase entityLivingBase, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            EnchantmentHelper.applyThornEnchantments((EntityLivingBase)entity, entityLivingBase);
        }
        EnchantmentHelper.applyArthropodEnchantments(entityLivingBase, entity);
    }

    public void setSneaking(boolean bl) {
        this.setFlag(1, bl);
    }

    public void spawnRunningParticles() {
        if (this.isSprinting() && !this.isInWater()) {
            this.createRunningParticles();
        }
    }

    protected boolean shouldSetPosAfterLoading() {
        return true;
    }

    public String getCustomNameTag() {
        return this.dataWatcher.getWatchableObjectString(2);
    }

    public void func_181013_g(float f) {
    }

    public boolean isBurning() {
        boolean bl;
        boolean bl2 = bl = this.worldObj != null && this.worldObj.isRemote;
        return !this.isImmuneToFire && (this.fire > 0 || bl && this.getFlag(0));
    }

    public void setRotationYawHead(float f) {
    }

    public boolean interactFirst(EntityPlayer entityPlayer) {
        return false;
    }

    protected NBTTagList newDoubleNBTList(double ... dArray) {
        NBTTagList nBTTagList = new NBTTagList();
        double[] dArray2 = dArray;
        int n = dArray.length;
        int n2 = 0;
        while (n2 < n) {
            double d = dArray2[n2];
            nBTTagList.appendTag(new NBTTagDouble(d));
            ++n2;
        }
        return nBTTagList;
    }

    public void addToPlayerScore(Entity entity, int n) {
    }

    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }

    public EntityItem dropItem(Item item, int n) {
        return this.dropItemWithOffset(item, n, 0.0f);
    }

    public void addEntityCrashInfo(CrashReportCategory crashReportCategory) {
        crashReportCategory.addCrashSectionCallable("Entity Type", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return String.valueOf(EntityList.getEntityString(Entity.this)) + " (" + Entity.this.getClass().getCanonicalName() + ")";
            }
        });
        crashReportCategory.addCrashSection("Entity ID", this.entityId);
        crashReportCategory.addCrashSectionCallable("Entity Name", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return Entity.this.getName();
            }
        });
        crashReportCategory.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", this.posX, this.posY, this.posZ));
        crashReportCategory.addCrashSection("Entity's Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
        crashReportCategory.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", this.motionX, this.motionY, this.motionZ));
        crashReportCategory.addCrashSectionCallable("Entity's Rider", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return Entity.this.riddenByEntity.toString();
            }
        });
        crashReportCategory.addCrashSectionCallable("Entity's Vehicle", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return Entity.this.ridingEntity.toString();
            }
        });
    }

    public void setWorld(World world) {
        this.worldObj = world;
    }

    public boolean equals(Object object) {
        return object instanceof Entity ? ((Entity)object).entityId == this.entityId : false;
    }

    public void setEntityId(int n) {
        this.entityId = n;
    }

    public int getAir() {
        return this.dataWatcher.getWatchableObjectShort(1);
    }

    public boolean isSneaking() {
        return this.getFlag(1);
    }

    public void setAlwaysRenderNameTag(boolean bl) {
        this.dataWatcher.updateObject(3, (byte)(bl ? 1 : 0));
    }

    public float getDistanceToEntity(Entity entity) {
        float f = (float)(this.posX - entity.posX);
        float f2 = (float)(this.posY - entity.posY);
        float f3 = (float)(this.posZ - entity.posZ);
        return MathHelper.sqrt_float(f * f + f2 * f2 + f3 * f3);
    }

    public float getCollisionBorderSize() {
        return 0.1f;
    }

    public boolean isRiding() {
        return this.ridingEntity != null;
    }

    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
        }
    }

    public void setDead() {
        this.isDead = true;
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

    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip) {
            return false;
        }
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        int n = 0;
        while (n < 8) {
            int n2 = MathHelper.floor_double(this.posY + (double)(((float)((n >> 0) % 2) - 0.5f) * 0.1f) + (double)this.getEyeHeight());
            int n3 = MathHelper.floor_double(this.posX + (double)(((float)((n >> 1) % 2) - 0.5f) * this.width * 0.8f));
            int n4 = MathHelper.floor_double(this.posZ + (double)(((float)((n >> 2) % 2) - 0.5f) * this.width * 0.8f));
            if (mutableBlockPos.getX() != n3 || mutableBlockPos.getY() != n2 || mutableBlockPos.getZ() != n4) {
                mutableBlockPos.func_181079_c(n3, n2, n4);
                if (this.worldObj.getBlockState(mutableBlockPos).getBlock().isVisuallyOpaque()) {
                    return true;
                }
            }
            ++n;
        }
        return false;
    }

    public CommandResultStats getCommandStats() {
        return this.cmdResultStats;
    }

    public boolean isInRangeToRenderDist(double d) {
        double d2 = this.getEntityBoundingBox().getAverageEdgeLength();
        if (Double.isNaN(d2)) {
            d2 = 1.0;
        }
        return d < (d2 = d2 * 64.0 * this.renderDistanceWeight) * d2;
    }

    public boolean isEntityAlive() {
        return !this.isDead;
    }

    protected void setOnFireFromLava() {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.lava, 4.0f);
            this.setFire(15);
        }
    }

    public int getMaxFallHeight() {
        return 3;
    }

    public boolean isSprinting() {
        return this.getFlag(3);
    }

    public AxisAlignedBB getEntityBoundingBox() {
        return this.boundingBox;
    }

    public float getEyeHeight() {
        return this.height * 0.85f;
    }

    public void setCurrentItemOrArmor(int n, ItemStack itemStack) {
    }

    public boolean isSilent() {
        return this.dataWatcher.getWatchableObjectByte(4) == 1;
    }

    public double getDistanceSqToEntity(Entity entity) {
        double d = this.posX - entity.posX;
        double d2 = this.posY - entity.posY;
        double d3 = this.posZ - entity.posZ;
        return d * d + d2 * d2 + d3 * d3;
    }

    public void onChunkLoad() {
    }

    public double getDistance(double d, double d2, double d3) {
        double d4 = this.posX - d;
        double d5 = this.posY - d2;
        double d6 = this.posZ - d3;
        return MathHelper.sqrt_double(d4 * d4 + d5 * d5 + d6 * d6);
    }

    public final boolean isImmuneToFire() {
        return this.isImmuneToFire;
    }
}

