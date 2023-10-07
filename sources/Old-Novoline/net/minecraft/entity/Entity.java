package net.minecraft.entity;

import cc.novoline.Novoline;
import cc.novoline.events.EventManager;
import cc.novoline.events.events.StepConfirmEvent;
import cc.novoline.modules.combat.HitBox;
import cc.novoline.modules.move.Scaffold;
import cc.novoline.modules.player.Freecam;
import cc.novoline.modules.visual.Camera;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
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
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.lwjgl.input.Mouse;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class Entity implements ICommandSender {

    private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private static int nextEntityID;
    /**
     * The command result statistics for this Entity.
     */
    private final CommandResultStats cmdResultStats;
    public double renderDistanceWeight;

    /**
     * Blocks entities from spawning when they do their AABB check to make sure the spot is clear of entities that can
     * prevent spawning.
     */
    public boolean preventEntitySpawning;

    /**
     * The entity that is riding this entity
     */
    public Entity riddenByEntity;

    /**
     * The entity we are currently riding
     */
    public Entity ridingEntity;
    public boolean forceSpawn;

    /**
     * Reference to the World object.
     */
    public World worldObj;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;

    /**
     * Entity position X
     */
    public double posX;

    /**
     * Entity position Y
     */
    public double posY;

    /**
     * Entity position Z
     */
    public double posZ;

    /**
     * Entity motion X
     */
    public double motionX;

    /**
     * Entity motion Y
     */
    public double motionY;

    /**
     * Entity motion Z
     */
    public double motionZ;

    /**
     * Entity rotation Yaw
     */
    public float rotationYaw;

    /**
     * Entity rotation Pitch
     */
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    public boolean onGround;

    public float cameraRotationPitch;
    public float cameraRotationYaw;
    public float prevCameraRotationPitch;
    public float prevCameraRotationYaw;

    /**
     * True if after a move this entity has collided with something on X- or Z-axis
     */
    public boolean isCollidedHorizontally;
    /**
     * True if after a move this entity has collided with something on Y-axis
     */
    public boolean isCollidedVertically;
    /**
     * True if after a move this entity has collided with something either vertically or horizontally
     */
    public boolean isCollided;
    public boolean velocityChanged;
    /**
     * gets set by setEntityDead, so this must be the flag whether an Entity is dead (inactive may be better term)
     */
    public boolean isDead;
    /**
     * How wide this entity is considered to be
     */
    public float width;
    /**
     * How high this entity is considered to be
     */
    public float height;
    /**
     * The previous ticks distance walked multiplied by 0.6
     */
    public float prevDistanceWalkedModified;
    /**
     * The distance walked multiplied by 0.6
     */
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;
    /**
     * The entity's X coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosX;
    /**
     * The entity's Y coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosY;
    /**
     * The entity's Z coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosZ;
    /**
     * How high this entity can step up when running into a block to try to get over it (currently make note the entity
     * will always step up this amount and not just the amount needed)
     */
    public float stepHeight;
    /**
     * Whether this entity won't clip with collision or not (make note it won't disable gravity)
     */
    public boolean noClip;
    /**
     * Reduces the velocity applied by entity collisions by the specified percent.
     */
    public float entityCollisionReduction;
    /**
     * How many ticks has this entity had ran since being alive
     */
    public int ticksExisted;
    /**
     * The amount of ticks you have to stand inside of fire before be set on fire
     */
    public int fireResistance;
    /**
     * Remaining time an entity will be "immune" to further damage after being hurt.
     */
    public int hurtResistantTime;
    /**
     * Has this entity been added to the chunk its within
     */
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public int serverPosX;
    public int serverPosY;
    public int serverPosZ;
    /**
     * Render entity even if it is outside the camera frustum. Only true in EntityFish for now. Used in RenderGlobal:
     * render if ignoreFrustumCheck or in frustum.
     */
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;
    /**
     * Which dimension the player is in (-1 = the Nether, 0 = normal world)
     */
    public int dimension;
    protected boolean isInWeb;
    protected Random rand;
    /**
     * Whether this entity is currently inside of water (if it handles water movement that is)
     */
    protected boolean inWater;
    protected boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    /**
     * Whether the entity is inside a Portal
     */
    protected boolean inPortal;
    protected int portalCounter;
    protected BlockPos field_181016_an;
    protected Vec3 field_181017_ao;
    protected EnumFacing field_181018_ap;
    private UUID entityUniqueID;
    private int entityId;
    /**
     * Axis aligned bounding box.
     */
    private AxisAlignedBB boundingBox;
    private boolean isOutsideBorder;
    /**
     * The distance that has to be exceeded in order to triger a new step sound and an onEntityWalking event on a block
     */
    private int nextStepDistance;
    private int fire;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;
    private boolean invulnerable;

    public Entity(World worldIn) {
        this.entityId = nextEntityID++;
        this.renderDistanceWeight = 1.0D;
        this.boundingBox = ZERO_AABB;
        this.width = 0.6F;
        this.height = 1.8F;
        this.nextStepDistance = 1;
        this.rand = new Random();
        this.fireResistance = 1;
        this.firstUpdate = true;
        setEntityUniqueID(MathHelper.getRandomUuid(rand));
        this.cmdResultStats = new CommandResultStats();
        this.worldObj = worldIn;
        setPosition(0.0D, 0.0D, 0.0D);

        if (worldIn != null) {
            this.dimension = worldIn.provider.getDimensionId();
        }

        this.dataWatcher = new DataWatcher(this);
        dataWatcher.addObject(0, (byte) 0);
        dataWatcher.addObject(1, (short) 300);
        dataWatcher.addObject(3, (byte) 0);
        dataWatcher.addObject(2, "");
        dataWatcher.addObject(4, (byte) 0);
        entityInit();
    }

    public int getEntityID() {
        return entityId;
    }

    public void setEntityId(int id) {
        this.entityId = id;
    }

    /**
     * Called by the /kill command.
     */
    public void onKillCommand() {
        setDead();
    }

    protected abstract void entityInit();

    public DataWatcher getDataWatcher() {
        return dataWatcher;
    }

    public boolean equals(Object p_equals_1_) {
        return p_equals_1_ instanceof Entity && ((Entity) p_equals_1_).entityId == entityId;
    }

    public int hashCode() {
        return entityId;
    }

    /**
     * Keeps moving the entity up so it isn't colliding with blocks and other requirements for this entity to be spawned
     * (only actually used on players though its also on Entity)
     */
    protected void preparePlayerToSpawn() {
        if (worldObj != null) {
            while (posY > 0.0D && posY < 256.0D) {
                setPosition(posX, posY, posZ);

                if (worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) {
                    break;
                }

                ++this.posY;
            }

            this.motionX = this.motionY = this.motionZ = 0.0D;
            this.rotationPitch = 0.0F;
        }
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead() {
        this.isDead = true;
    }

    /**
     * Sets the width and height of the entity. Args: width, height
     */
    protected void setSize(float width, float height) {
        if (width != this.width || height != this.height) {
            float f = this.width;
            this.width = width;
            this.height = height;
            setEntityBoundingBox(
                    new AxisAlignedBB(getEntityBoundingBox().minX, getEntityBoundingBox().minY, getEntityBoundingBox().minZ, getEntityBoundingBox().minX + (double) this.width, getEntityBoundingBox().minY + (double) this.height, getEntityBoundingBox().minZ + (double) this.width));

            if (this.width > f && !firstUpdate && !worldObj.isRemote) {
                moveEntity(f - this.width, 0.0D, f - this.width);
            }
        }
    }

    /**
     * Sets the rotation of the entity. Args: yaw, pitch (both in degrees)
     */
    protected void setRotation(float yaw, float pitch) {
        this.rotationYaw = yaw % 360.0F;
        this.rotationPitch = pitch % 360.0F;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float f = width / 2.0F;
        float f1 = height;
        setEntityBoundingBox(
                new AxisAlignedBB(x - (double) f, y, z - (double) f, x + (double) f, y + (double) f1, z + (double) f));
    }

    /**
     * Adds 15% to the entity's yaw and subtracts 15% from the pitch. Clamps pitch from -90 to 90. Both arguments in
     * degrees.
     */
    public void setAngles(float yaw, float pitch) {
        float f = rotationPitch;
        float f1 = rotationYaw;
        Camera module = Novoline.getInstance().getModuleManager().getModule(Camera.class);
        if (!(module.isEnabled() && module.getMcLock().get() && Mouse.isButtonDown(2))) {
            this.rotationYaw = (float) ((double) rotationYaw + (double) yaw * 0.15D);
            this.rotationPitch = (float) ((double) rotationPitch - (double) pitch * 0.15D);
            this.rotationPitch = MathHelper.clamp_float(rotationPitch, -90.0F, 90.0F);
            this.prevRotationPitch += rotationPitch - f;
            this.prevRotationYaw += rotationYaw - f1;
            this.cameraRotationYaw = rotationYaw;
            this.cameraRotationPitch = rotationPitch;
        }
        this.cameraRotationPitch = (float) ((double) cameraRotationPitch - (double) pitch * 0.15D);
        this.cameraRotationPitch = MathHelper.clamp_float(cameraRotationPitch, -90.0F, 90.0F);
        this.cameraRotationYaw = (float) ((double) cameraRotationYaw + (double) yaw * 0.15D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        onEntityUpdate();
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate() {
        worldObj.theProfiler.startSection("entityBaseTick");

        if (ridingEntity != null && ridingEntity.isDead) {
            this.ridingEntity = null;
        }

        this.prevDistanceWalkedModified = distanceWalkedModified;
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;
        this.prevRotationPitch = rotationPitch;
        this.prevRotationYaw = rotationYaw;

        if (!worldObj.isRemote && worldObj instanceof WorldServer) {
            worldObj.theProfiler.startSection("portal");
            MinecraftServer minecraftserver = ((WorldServer) worldObj).getMinecraftServer();
            int i = getMaxInPortalTime();

            if (inPortal) {
                if (minecraftserver.getAllowNether()) {
                    if (ridingEntity == null && this.portalCounter++ >= i) {
                        this.portalCounter = i;
                        this.timeUntilPortal = getPortalCoolDown();
                        int j;

                        if (worldObj.provider.getDimensionId() == -1) {
                            j = 0;
                        } else {
                            j = -1;
                        }

                        travelToDimension(j);
                    }

                    this.inPortal = false;
                }
            } else {
                if (portalCounter > 0) {
                    this.portalCounter -= 4;
                }

                if (portalCounter < 0) {
                    this.portalCounter = 0;
                }
            }

            if (timeUntilPortal > 0) {
                --this.timeUntilPortal;
            }

            worldObj.theProfiler.endSection();
        }

        spawnRunningParticles();
        handleWaterMovement();

        if (worldObj.isRemote) {
            this.fire = 0;
        } else if (fire > 0) {
            if (isImmuneToFire) {
                this.fire -= 4;

                if (fire < 0) {
                    this.fire = 0;
                }
            } else {
                if (fire % 20 == 0) {
                    attackEntityFrom(DamageSource.onFire, 1.0F);
                }

                --this.fire;
            }
        }

        if (isInLava()) {
            setOnFireFromLava();
            this.fallDistance *= 0.5F;
        }

        if (posY < -64.0D) {
            kill();
        }

        if (!worldObj.isRemote) {
            setFlag(0, fire > 0);
        }

        this.firstUpdate = false;
        worldObj.theProfiler.endSection();
    }

    /**
     * Return the amount of time this entity should stay in a portal before being transported.
     */
    public int getMaxInPortalTime() {
        return 0;
    }

    /**
     * Called whenever the entity is walking inside of lava.
     */
    protected void setOnFireFromLava() {
        if (!isImmuneToFire) {
            attackEntityFrom(DamageSource.lava, 4.0F);
            setFire(15);
        }
    }

    /**
     * Sets entity to burn for x amount of seconds, cannot lower amount of existing fire.
     */
    public void setFire(int seconds) {
        int i = seconds * 20;
        i = EnchantmentProtection.getFireTimeForEntity(this, i);

        if (fire < i) {
            this.fire = i;
        }
    }

    /**
     * Removes fire from entity.
     */
    public void extinguish() {
        this.fire = 0;
    }

    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    protected void kill() {
        setDead();
    }

    /**
     * Checks if the offset position from the entity's current position is inside of liquid. Args: x, y, z
     */
    public boolean isOffsetPositionInLiquid(double x, double y, double z) {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox().offset(x, y, z);
        return isLiquidPresentInAABB(axisalignedbb);
    }

    /**
     * Determines if a liquid is present within the specified AxisAlignedBB.
     */
    private boolean isLiquidPresentInAABB(AxisAlignedBB bb) {
        return worldObj.getCollidingBoundingBoxes(this, bb).isEmpty() && !worldObj.isAnyLiquid(bb);
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    public void moveEntity(double x, double y, double z) {
        if (noClip) {
            setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
            resetPositionToBB();
        } else {
            worldObj.theProfiler.startSection("move");
            double d0 = posX;
            double d1 = posY;
            double d2 = posZ;

            if (isInWeb) {
                this.isInWeb = false;
                x *= 0.25D;
                y *= 0.05000000074505806D;
                z *= 0.25D;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            double d3 = x;
            double d4 = y;
            double d5 = z;

            Scaffold scaffold = Novoline.getInstance().getModuleManager().getModule(Scaffold.class);
            boolean safewalk = onGround && isSneaking() && this instanceof EntityPlayer || this instanceof EntityPlayerSP && scaffold.isEnabled() && scaffold.safeWalk();

            if (safewalk) {
                double d6;

                for (d6 = 0.05D; x != 0.0D && worldObj
                        .getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(x, -1.0D, 0.0D))
                        .isEmpty(); d3 = x) {
                    if (x < d6 && x >= -d6) {
                        x = 0.0D;
                    } else if (x > 0.0D) {
                        x -= d6;
                    } else {
                        x += d6;
                    }
                }

                for (; z != 0.0D && worldObj
                        .getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(0.0D, -1.0D, z))
                        .isEmpty(); d5 = z) {
                    if (z < d6 && z >= -d6) {
                        z = 0.0D;
                    } else if (z > 0.0D) {
                        z -= d6;
                    } else {
                        z += d6;
                    }
                }

                for (; x != 0.0D && z != 0.0D && worldObj
                        .getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(x, -1.0D, z))
                        .isEmpty(); d5 = z) {
                    if (x < d6 && x >= -d6) {
                        x = 0.0D;
                    } else if (x > 0.0D) {
                        x -= d6;
                    } else {
                        x += d6;
                    }

                    d3 = x;

                    if (z < d6 && z >= -d6) {
                        z = 0.0D;
                    } else if (z > 0.0D) {
                        z -= d6;
                    } else {
                        z += d6;
                    }
                }
            }

            List<AxisAlignedBB> list1 = worldObj
                    .getCollidingBoundingBoxes(this, getEntityBoundingBox().addCoord(x, y, z));
            AxisAlignedBB axisalignedbb = getEntityBoundingBox();

            for (AxisAlignedBB axisalignedbb1 : list1) {
                y = axisalignedbb1.calculateYOffset(getEntityBoundingBox(), y);
            }

            setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, y, 0.0D));
            boolean flag1 = onGround || d4 != y && d4 < 0.0D;

            for (AxisAlignedBB axisalignedbb2 : list1) {
                x = axisalignedbb2.calculateXOffset(getEntityBoundingBox(), x);
            }

            setEntityBoundingBox(getEntityBoundingBox().offset(x, 0.0D, 0.0D));

            for (AxisAlignedBB axisalignedbb13 : list1) {
                z = axisalignedbb13.calculateZOffset(getEntityBoundingBox(), z);
            }

            setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, 0.0D, z));

            if (stepHeight > 0.0F && flag1 && (d3 != x || d5 != z)) {
                double d11 = x;
                double d7 = y;
                double d8 = z;
                AxisAlignedBB axisalignedbb3 = getEntityBoundingBox();
                setEntityBoundingBox(axisalignedbb);
                y = stepHeight;
                List<AxisAlignedBB> list = worldObj
                        .getCollidingBoundingBoxes(this, getEntityBoundingBox().addCoord(d3, y, d5));
                AxisAlignedBB axisalignedbb4 = getEntityBoundingBox();
                AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(d3, 0.0D, d5);
                double d9 = y;

                for (AxisAlignedBB axisalignedbb6 : list) {
                    d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, d9, 0.0D);
                double d15 = d3;

                for (AxisAlignedBB axisalignedbb7 : list) {
                    d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15);
                }

                axisalignedbb4 = axisalignedbb4.offset(d15, 0.0D, 0.0D);
                double d16 = d5;

                for (AxisAlignedBB axisalignedbb8 : list) {
                    d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d16);
                AxisAlignedBB axisalignedbb14 = getEntityBoundingBox();
                double d17 = y;

                for (AxisAlignedBB axisalignedbb9 : list) {
                    d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17);
                }

                axisalignedbb14 = axisalignedbb14.offset(0.0D, d17, 0.0D);
                double d18 = d3;

                for (AxisAlignedBB axisalignedbb10 : list) {
                    d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18);
                }

                axisalignedbb14 = axisalignedbb14.offset(d18, 0.0D, 0.0D);
                double d19 = d5;

                for (AxisAlignedBB axisalignedbb11 : list) {
                    d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19);
                }

                axisalignedbb14 = axisalignedbb14.offset(0.0D, 0.0D, d19);
                double d20 = d15 * d15 + d16 * d16;
                double d10 = d18 * d18 + d19 * d19;

                if (d20 > d10) {
                    x = d15;
                    z = d16;
                    y = -d9;
                    setEntityBoundingBox(axisalignedbb4);
                } else {
                    x = d18;
                    z = d19;
                    y = -d17;
                    setEntityBoundingBox(axisalignedbb14);
                }

                for (AxisAlignedBB axisalignedbb12 : list) {
                    y = axisalignedbb12.calculateYOffset(getEntityBoundingBox(), y);
                }

                setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, y, 0.0D));

                if (d11 * d11 + d8 * d8 >= x * x + z * z) {
                    x = d11;
                    y = d7;
                    z = d8;
                    setEntityBoundingBox(axisalignedbb3);
                } else {
                    StepConfirmEvent eventConfirm = new StepConfirmEvent();
                    EventManager.call(eventConfirm);
                }
            }

            worldObj.theProfiler.endSection();
            worldObj.theProfiler.startSection("rest");
            resetPositionToBB();
            this.isCollidedHorizontally = d3 != x || d5 != z;
            this.isCollidedVertically = d4 != y;
            this.onGround = isCollidedVertically && d4 < 0.0D;
            this.isCollided = isCollidedHorizontally || isCollidedVertically;
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(posY - 0.20000000298023224D);
            int k = MathHelper.floor_double(posZ);
            BlockPos blockpos = new BlockPos(i, j, k);
            Block block1 = worldObj.getBlockState(blockpos).getBlock();

            if (block1.getMaterial() == Material.air) {
                Block block = worldObj.getBlockState(blockpos.down()).getBlock();

                if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate) {
                    block1 = block;
                    blockpos = blockpos.down();
                }
            }

            updateFallState(y, onGround, block1, blockpos);

            if (d3 != x) {
                this.motionX = 0.0D;
            }

            if (d5 != z) {
                this.motionZ = 0.0D;
            }

            if (d4 != y) {
                block1.onLanded(worldObj, this);
            }

            if (canTriggerWalking() && !safewalk && ridingEntity == null || this instanceof EntityPlayerSP && safewalk) {
                double d12 = posX - d0;
                double d13 = posY - d1;
                double d14 = posZ - d2;

                if (block1 != Blocks.ladder) {
                    d13 = 0.0D;
                }

                if (block1 != null && onGround) {
                    block1.onEntityCollidedWithBlock(worldObj, blockpos, this);
                }

                this.distanceWalkedModified = (float) ((double) distanceWalkedModified + (double) MathHelper
                        .sqrt_double(d12 * d12 + d14 * d14) * 0.6D);
                this.distanceWalkedOnStepModified = (float) ((double) distanceWalkedOnStepModified + (double) MathHelper
                        .sqrt_double(d12 * d12 + d13 * d13 + d14 * d14) * 0.6D);

                if (distanceWalkedOnStepModified > (float) nextStepDistance && block1
                        .getMaterial() != Material.air) {
                    this.nextStepDistance = (int) distanceWalkedOnStepModified + 1;

                    if (isInWater()) {
                        float f = MathHelper.sqrt_double(motionX * motionX * 0.20000000298023224D + motionY * motionY + motionZ * motionZ * 0.20000000298023224D) * 0.35F;

                        if (f > 1.0F) {
                            f = 1.0F;
                        }

                        playSound(getSwimSound(), f,
                                1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
                    }

                    playStepSound(blockpos, block1);
                }
            }

            try {
                doBlockCollisions();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport
                        .makeCategory("Entity being checked for collision");
                addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            boolean flag2 = isWet();

            if (worldObj.isFlammableWithin(getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D))) {
                dealFireDamage(1);

                if (!flag2) {
                    ++this.fire;

                    if (fire == 0) {
                        setFire(8);
                    }
                }
            } else if (fire <= 0) {
                this.fire = -fireResistance;
            }

            if (flag2 && fire > 0) {
                playSound("random.fizz", 0.7F, 1.6F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
                this.fire = -fireResistance;
            }

            worldObj.theProfiler.endSection();
        }
    }

    /**
     * Resets the entity's position to the center (planar) and bottom (vertical) points of its bounding box.
     */
    private void resetPositionToBB() {
        this.posX = (getEntityBoundingBox().minX + getEntityBoundingBox().maxX) / 2.0D;
        this.posY = getEntityBoundingBox().minY;
        this.posZ = (getEntityBoundingBox().minZ + getEntityBoundingBox().maxZ) / 2.0D;
    }

    protected String getSwimSound() {
        return "game.neutral.swim";
    }

    protected void doBlockCollisions() {
        BlockPos blockpos = new BlockPos(getEntityBoundingBox().minX + 0.001D, getEntityBoundingBox().minY + 0.001D, getEntityBoundingBox().minZ + 0.001D);
        BlockPos blockpos1 = new BlockPos(getEntityBoundingBox().maxX - 0.001D, getEntityBoundingBox().maxY - 0.001D, getEntityBoundingBox().maxZ - 0.001D);

        if (worldObj.isAreaLoaded(blockpos, blockpos1)) {
            for (int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                for (int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                    for (int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                        BlockPos blockpos2 = new BlockPos(i, j, k);
                        IBlockState iblockstate = worldObj.getBlockState(blockpos2);

                        try {
                            iblockstate.getBlock()
                                    .onEntityCollidedWithBlock(worldObj, blockpos2, iblockstate, this);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport
                                    .makeCrashReport(throwable, "Colliding entity with block");
                            CrashReportCategory crashreportcategory = crashreport
                                    .makeCategory("Block being collided with");
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

        if (worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer) {
            block$soundtype = Blocks.snow_layer.stepSound;
            playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15F,
                    block$soundtype.getFrequency());
        } else if (!blockIn.getMaterial().isLiquid()) {
            playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15F,
                    block$soundtype.getFrequency());
        }
    }

    public void playSound(String name, float volume, float pitch) {
        if (!isSilent()) {
            worldObj.playSoundAtEntity(this, name, volume, pitch);
        }
    }

    /**
     * @return True if this entity will not play sounds
     */
    public boolean isSilent() {
        return dataWatcher.getWatchableObjectByte(4) == 1;
    }

    /**
     * When set to true the entity will not play sounds.
     */
    public void setSilent(boolean isSilent) {
        dataWatcher.updateObject(4, (byte) (isSilent ? 1 : 0));
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return true;
    }

    protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {
        if (onGroundIn) {
            if (fallDistance > 0.0F) {
                if (blockIn != null) {
                    blockIn.onFallenUpon(worldObj, pos, this, fallDistance);
                } else {
                    fall(fallDistance, 1.0F);
                }

                this.fallDistance = 0.0F;
            }
        } else if (y < 0.0D) {
            this.fallDistance = (float) ((double) fallDistance - y);
        }
    }

    /**
     * Returns the collision bounding box for this entity
     */
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }

    /**
     * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     */
    protected void dealFireDamage(int amount) {
        if (!isImmuneToFire) {
            attackEntityFrom(DamageSource.inFire, (float) amount);
        }
    }

    public final boolean isImmuneToFire() {
        return isImmuneToFire;
    }

    public void fall(float distance, float damageMultiplier) {
        if (riddenByEntity != null) {
            riddenByEntity.fall(distance, damageMultiplier);
        }
    }

    /**
     * Checks if this entity is either in water or on an open air block in rain (used in wolves).
     */
    public boolean isWet() {
        return inWater || worldObj
                .canLightningStrike(new BlockPos(posX, posY, posZ)) || worldObj
                .canLightningStrike(new BlockPos(posX, posY + (double) height, posZ));
    }

    /**
     * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
     * true)
     */
    public boolean isInWater() {
        return inWater;
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public void handleWaterMovement() {
        if (worldObj.handleMaterialAcceleration(
                getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D),
                Material.water, this)) {
            if (!inWater && !firstUpdate) {
                resetHeight();
            }

            this.fallDistance = 0.0F;
            this.inWater = true;
            this.fire = 0;
        } else {
            this.inWater = false;
        }

    }

    /**
     * sets the players height back to normal after doing things like sleeping and dieing
     */
    protected void resetHeight() {
        float f = MathHelper.sqrt_double(motionX * motionX * 0.20000000298023224D + motionY * motionY + motionZ * motionZ * 0.20000000298023224D) * 0.2F;

        if (f > 1.0F) {
            f = 1.0F;
        }

        playSound(getSplashSound(), f, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
        float f1 = (float) MathHelper.floor_double(getEntityBoundingBox().minY);

        for (int i = 0; (float) i < 1.0F + width * 20.0F; ++i) {
            float f2 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            float f3 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX + (double) f2, f1 + 1.0F, posZ + (double) f3, motionX, motionY - (double) (rand.nextFloat() * 0.2F), motionZ);
        }

        for (int j = 0; (float) j < 1.0F + width * 20.0F; ++j) {
            float f4 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            float f5 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, posX + (double) f4, f1 + 1.0F, posZ + (double) f5, motionX, motionY, motionZ);
        }
    }

    /**
     * Attempts to create sprinting particles if the entity is sprinting and not in water.
     */
    public void spawnRunningParticles() {
        if (isSprinting() && !isInWater()) {
            createRunningParticles();
        }
    }

    protected void createRunningParticles() {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posY - 0.20000000298023224D);
        int k = MathHelper.floor_double(posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        IBlockState iblockstate = worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (block.getRenderType() != -1) {
            worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX + ((double) rand.nextFloat() - 0.5D) * (double) width, getEntityBoundingBox().minY + 0.1D, posZ + ((double) rand.nextFloat() - 0.5D) * (double) width, -motionX * 4.0D,
                    1.5D, -motionZ * 4.0D, Block.getStateId(iblockstate));
        }
    }

    protected String getSplashSound() {
        return "game.neutral.swim.splash";
    }

    /**
     * Checks if the current block the entity is within of the specified material type
     */
    public boolean isInsideOfMaterial(Material materialIn) {
        double d0 = posY + (double) getEyeHeight();
        BlockPos blockpos = new BlockPos(posX, d0, posZ);
        IBlockState iblockstate = worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (block.getMaterial() == materialIn) {
            float f = BlockLiquid
                    .getLiquidHeightPercent(iblockstate.getBlock().getMetaFromState(iblockstate)) - 0.11111111F;
            float f1 = (float) (blockpos.getY() + 1) - f;
            return d0 < (double) f1;
        } else {
            return false;
        }
    }

    public boolean isInLava() {
        return worldObj.isMaterialInBB(getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D),
                Material.lava);
    }

    /**
     * Used in both water and by flying objects
     */
    public void moveFlying(float strafe, float forward, float friction) {
        float f = strafe * strafe + forward * forward;

        if (f >= 1.0E-4F) {
            f = MathHelper.sqrt_float(f);

            if (f < 1.0F) {
                f = 1.0F;
            }

            f = friction / f;
            strafe = strafe * f;
            forward = forward * f;
            float f1 = MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F);
            float f2 = MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F);
            this.motionX += strafe * f2 - forward * f1;
            this.motionZ += forward * f2 + strafe * f1;
        }
    }

    public int getBrightnessForRender(float partialTicks) {
        BlockPos blockpos = new BlockPos(posX, posY + (double) getEyeHeight(), posZ);
        return worldObj.isBlockLoaded(blockpos) ? worldObj.getCombinedLight(blockpos, 0) : 0;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float partialTicks) {
        BlockPos blockpos = new BlockPos(posX, posY + (double) getEyeHeight(), posZ);
        return worldObj.isBlockLoaded(blockpos) ? worldObj.getLightBrightness(blockpos) : 0.0F;
    }

    /**
     * Sets the reference to the World object.
     */
    public void setWorld(World worldIn) {
        this.worldObj = worldIn;
    }

    /**
     * Sets the entity's position and rotation.
     */
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        this.prevPosX = this.posX = x;
        this.prevPosY = this.posY = y;
        this.prevPosZ = this.posZ = z;
        this.prevRotationYaw = this.rotationYaw = yaw;
        this.prevRotationPitch = this.rotationPitch = pitch;
        double d0 = prevRotationYaw - yaw;

        if (d0 < -180.0D) {
            this.prevRotationYaw += 360.0F;
        }

        if (d0 >= 180.0D) {
            this.prevRotationYaw -= 360.0F;
        }

        setPosition(posX, posY, posZ);
        setRotation(yaw, pitch);
    }

    public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn) {
        setLocationAndAngles((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, rotationYawIn,
                rotationPitchIn);
    }

    /**
     * Sets the location and Yaw/Pitch of an entity in the world
     */
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
        this.lastTickPosX = this.prevPosX = this.posX = x;
        this.lastTickPosY = this.prevPosY = this.posY = y;
        this.lastTickPosZ = this.prevPosZ = this.posZ = z;
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
        setPosition(posX, posY, posZ);
    }

    /**
     * Returns the distance to the entity. Args: entity
     */
    public float getDistanceToEntity(Entity entityIn) {
        float f = (float) (posX - entityIn.posX);
        float f1 = (float) (posY - entityIn.posY);
        float f2 = (float) (posZ - entityIn.posZ);
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    public double getDistance2D(Entity entityIn) {
        double x = Math.abs(entityIn.posX - posX);
        double z = Math.abs(entityIn.posZ - posZ);
        return Math.sqrt(x * x + z * z);
    }

    /**
     * Gets the squared distance to the position. Args: x, y, z
     */
    public double getDistanceSq(double x, double y, double z) {
        double d0 = posX - x;
        double d1 = posY - y;
        double d2 = posZ - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double getDistanceSq(BlockPos pos) {
        return pos.distanceSq(posX, posY, posZ);
    }

    public double getDistanceSqToCenter(BlockPos pos) {
        return pos.distanceSqToCenter(posX, posY, posZ);
    }

    /**
     * Gets the distance to the position. Args: x, y, z
     */
    public double getDistance(double x, double y, double z) {
        double d0 = posX - x;
        double d1 = posY - y;
        double d2 = posZ - z;
        return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double getDistanceY(double y) {
        double d1 = posY - y;
        return MathHelper.sqrt_double(d1 * d1);
    }

    public double getDistance(BlockPos pos) {
        double x = posX - pos.getX(), y = posY - pos.getY(), z = posZ - pos.getZ();
        return MathHelper.sqrt_double(x * x + y * y + z * z);
    }

    public double getDistance(double x, double z) {
        double d0 = posX - x;
        double d2 = posZ - z;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2);
    }


    /**
     * Returns the squared distance to the entity. Args: entity
     */
    public double getDistanceSqToEntity(Entity entityIn) {
        double d0 = posX - entityIn.posX;
        double d1 = posY - entityIn.posY;
        double d2 = posZ - entityIn.posZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn) {
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity entityIn) {
        if (entityIn.riddenByEntity != this && entityIn.ridingEntity != this) {
            if (!entityIn.noClip && !noClip) {
                double d0 = entityIn.posX - posX;
                double d1 = entityIn.posZ - posZ;
                double d2 = MathHelper.abs_max(d0, d1);

                if (d2 >= 0.009999999776482582D) {
                    d2 = MathHelper.sqrt_double(d2);
                    d0 = d0 / d2;
                    d1 = d1 / d2;
                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D) {
                        d3 = 1.0D;
                    }

                    d0 = d0 * d3;
                    d1 = d1 * d3;
                    d0 = d0 * 0.05000000074505806D;
                    d1 = d1 * 0.05000000074505806D;
                    d0 = d0 * (double) (1.0F - entityCollisionReduction);
                    d1 = d1 * (double) (1.0F - entityCollisionReduction);

                    if (riddenByEntity == null) {
                        addVelocity(-d0, 0.0D, -d1);
                    }

                    if (entityIn.riddenByEntity == null) {
                        entityIn.addVelocity(d0, 0.0D, d1);
                    }
                }
            }
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double x, double y, double z) {
        this.motionX += x;
        this.motionY += y;
        this.motionZ += z;
        this.isAirBorne = true;
    }

    /**
     * Sets that this entity has been attacked.
     */
    protected void setBeenAttacked() {
        this.velocityChanged = true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!isEntityInvulnerable(source)) {
            setBeenAttacked();
        }
        return false;
    }

    /**
     * interpolated look vector
     */
    public Vec3 getLook(float partialTicks) {
        return getVectorForRotation(rotationPitch, rotationYaw);
    }

    /**
     * Creates a Vec3 using the pitch and yaw of the entities rotation.
     */
    protected final Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3(f1 * f2, f3, f * f2);
    }

    public Vec3 getPositionEyes(float partialTicks) {
        if (partialTicks == 1.0F) {
            return new Vec3(posX, posY + (double) getEyeHeight(), posZ);
        } else {
            double d0 = prevPosX + (posX - prevPosX) * (double) partialTicks;
            double d1 = prevPosY + (posY - prevPosY) * (double) partialTicks + (double) getEyeHeight();
            double d2 = prevPosZ + (posZ - prevPosZ) * (double) partialTicks;
            return new Vec3(d0, d1, d2);
        }
    }

    public MovingObjectPosition rayTrace(double blockReachDistance, float partialTicks) {
        Vec3 vec3 = getPositionEyes(partialTicks);
        Vec3 vec31 = getLook(partialTicks);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance,
                vec31.zCoord * blockReachDistance);
        return worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed() {
        return false;
    }

    /**
     * Adds a value to the player score. Currently not actually used and the entity passed in does nothing. Args:
     * entity, scoreToAdd
     */
    public void addToPlayerScore(Entity entityIn, int amount) {
    }

    public boolean isInRangeToRender3d(double x, double y, double z) {
        double d0 = posX - x;
        double d1 = posY - y;
        double d2 = posZ - z;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        return isInRangeToRenderDist(d3);
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = getEntityBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * renderDistanceWeight;
        return distance < d0 * d0;
    }

    /**
     * Like writeToNBTOptional but does not check if the entity is ridden. Used for saving ridden entities with their
     * riders.
     */
    public boolean writeMountToNBT(NBTTagCompound tagCompund) {
        String s = getEntityString();

        if (!isDead && s != null) {
            tagCompund.setString("id", s);
            writeToNBT(tagCompund);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Either write this entity to the NBT tag given and return true, or return false without doing anything. If this
     * returns false the entity is not saved on disk. Ridden entities return false here as they are saved with their
     * rider.
     */
    public boolean writeToNBTOptional(NBTTagCompound tagCompund) {
        String s = getEntityString();

        if (!isDead && s != null && riddenByEntity == null) {
            tagCompund.setString("id", s);
            writeToNBT(tagCompund);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Save the entity to NBT (calls an abstract helper method to write extra data)
     */
    public void writeToNBT(NBTTagCompound tagCompund) {
        try {
            tagCompund.setTag("Pos", newDoubleNBTList(posX, posY, posZ));
            tagCompund.setTag("Motion", newDoubleNBTList(motionX, motionY, motionZ));
            tagCompund.setTag("Rotation", newFloatNBTList(rotationYaw, rotationPitch));
            tagCompund.setFloat("FallDistance", fallDistance);
            tagCompund.setShort("Fire", (short) fire);
            tagCompund.setShort("Air", (short) getAir());
            tagCompund.setBoolean("OnGround", onGround);
            tagCompund.setInteger("Dimension", dimension);
            tagCompund.setBoolean("Invulnerable", invulnerable);
            tagCompund.setInteger("PortalCooldown", timeUntilPortal);
            tagCompund.setLong("UUIDMost", getUniqueID().getMostSignificantBits());
            tagCompund.setLong("UUIDLeast", getUniqueID().getLeastSignificantBits());

            if (getCustomNameTag() != null && getCustomNameTag().length() > 0) {
                tagCompund.setString("CustomName", getCustomNameTag());
                tagCompund.setBoolean("CustomNameVisible", getAlwaysRenderNameTag());
            }

            cmdResultStats.writeStatsToNBT(tagCompund);

            if (isSilent()) {
                tagCompund.setBoolean("Silent", isSilent());
            }

            writeEntityToNBT(tagCompund);

            if (ridingEntity != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                if (ridingEntity.writeMountToNBT(nbttagcompound)) {
                    tagCompund.setTag("Riding", nbttagcompound);
                }
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
            addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Reads the entity from NBT (calls an abstract helper method to read specialized data)
     */
    public void readFromNBT(NBTTagCompound tagCompund) {
        try {
            NBTTagList nbttaglist = tagCompund.getTagList("Pos", 6);
            NBTTagList nbttaglist1 = tagCompund.getTagList("Motion", 6);
            NBTTagList nbttaglist2 = tagCompund.getTagList("Rotation", 5);
            this.motionX = nbttaglist1.getDoubleAt(0);
            this.motionY = nbttaglist1.getDoubleAt(1);
            this.motionZ = nbttaglist1.getDoubleAt(2);

            if (Math.abs(motionX) > 10.0D) {
                this.motionX = 0.0D;
            }

            if (Math.abs(motionY) > 10.0D) {
                this.motionY = 0.0D;
            }

            if (Math.abs(motionZ) > 10.0D) {
                this.motionZ = 0.0D;
            }

            this.prevPosX = this.lastTickPosX = this.posX = nbttaglist.getDoubleAt(0);
            this.prevPosY = this.lastTickPosY = this.posY = nbttaglist.getDoubleAt(1);
            this.prevPosZ = this.lastTickPosZ = this.posZ = nbttaglist.getDoubleAt(2);
            this.prevRotationYaw = this.rotationYaw = nbttaglist2.getFloatAt(0);
            this.prevRotationPitch = this.rotationPitch = nbttaglist2.getFloatAt(1);
            setRotationYawHead(rotationYaw);
            func_181013_g(rotationYaw);
            this.fallDistance = tagCompund.getFloat("FallDistance");
            this.fire = tagCompund.getShort("Fire");
            setAir(tagCompund.getShort("Air"));
            this.onGround = tagCompund.getBoolean("OnGround");
            this.dimension = tagCompund.getInteger("Dimension");
            this.invulnerable = tagCompund.getBoolean("Invulnerable");
            this.timeUntilPortal = tagCompund.getInteger("PortalCooldown");

            if (tagCompund.hasKey("UUIDMost", 4) && tagCompund.hasKey("UUIDLeast", 4)) {
                setEntityUniqueID(new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast")));
            } else if (tagCompund.hasKey("UUID", 8)) {
                setEntityUniqueID(UUID.fromString(tagCompund.getString("UUID")));
            }

            setPosition(posX, posY, posZ);
            setRotation(rotationYaw, rotationPitch);

            if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0) {
                setCustomNameTag(tagCompund.getString("CustomName"));
            }

            setAlwaysRenderNameTag(tagCompund.getBoolean("CustomNameVisible"));
            cmdResultStats.readStatsFromNBT(tagCompund);
            setSilent(tagCompund.getBoolean("Silent"));
            readEntityFromNBT(tagCompund);

            if (shouldSetPosAfterLoading()) {
                setPosition(posX, posY, posZ);
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
            addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    protected boolean shouldSetPosAfterLoading() {
        return true;
    }

    /**
     * Returns the string that identifies this Entity's class
     */
    protected final String getEntityString() {
        return EntityList.getEntityString(this);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected abstract void readEntityFromNBT(NBTTagCompound tagCompund);

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected abstract void writeEntityToNBT(NBTTagCompound tagCompound);

    public void onChunkLoad() {
    }

    /**
     * creates a NBT list from the array of doubles passed to this function
     */
    protected NBTTagList newDoubleNBTList(double... numbers) {
        NBTTagList nbttaglist = new NBTTagList();

        for (double d0 : numbers) {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }

    /**
     * Returns a new NBTTagList filled with the specified floats
     */
    protected NBTTagList newFloatNBTList(float... numbers) {
        NBTTagList nbttaglist = new NBTTagList();

        for (float f : numbers) {
            nbttaglist.appendTag(new NBTTagFloat(f));
        }

        return nbttaglist;
    }

    public EntityItem dropItem(Item itemIn, int size) {
        return dropItemWithOffset(itemIn, size, 0.0F);
    }

    public EntityItem dropItemWithOffset(Item itemIn, int size, float offsetY) {
        return entityDropItem(new ItemStack(itemIn, size, 0), offsetY);
    }

    /**
     * Drops an item at the position of the entity.
     */
    public EntityItem entityDropItem(ItemStack itemStackIn, float offsetY) {
        if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
            EntityItem entityitem = new EntityItem(worldObj, posX, posY + (double) offsetY, posZ,
                    itemStackIn);
            entityitem.setDefaultPickupDelay();
            worldObj.spawnEntityInWorld(entityitem);
            return entityitem;
        } else {
            return null;
        }
    }

    /**
     * Checks whether target entity is alive.
     */
    public boolean isEntityAlive() {
        return !isDead;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock() {
        if (!noClip && !Novoline.getInstance().getModuleManager().getModule(Freecam.class).isEnabled()) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(Integer.MIN_VALUE,
                    Integer.MIN_VALUE, Integer.MIN_VALUE);

            for (int i = 0; i < 8; ++i) {
                int j = MathHelper.floor_double(posY + (double) (((float) (i % 2) - 0.5F) * 0.1F) + (double) getEyeHeight());
                int k = MathHelper
                        .floor_double(posX + (double) (((float) ((i >> 1) % 2) - 0.5F) * width * 0.8F));
                int l = MathHelper
                        .floor_double(posZ + (double) (((float) ((i >> 2) % 2) - 0.5F) * width * 0.8F));

                if (blockpos$mutableblockpos.getX() != k || blockpos$mutableblockpos
                        .getY() != j || blockpos$mutableblockpos.getZ() != l) {
                    blockpos$mutableblockpos.func_181079_c(k, j, l);

                    if (worldObj.getBlockState(blockpos$mutableblockpos).getBlock().isVisuallyOpaque()) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    /**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer playerIn) {
        return false;
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return null;
    }

    public Entity collidedEntity() {
        return null;
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden() {
        if (ridingEntity.isDead) {
            this.ridingEntity = null;
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            onUpdate();

            if (ridingEntity != null) {
                ridingEntity.updateRiderPosition();
                this.entityRiderYawDelta += ridingEntity.rotationYaw - ridingEntity.prevRotationYaw;
                this.entityRiderPitchDelta += ridingEntity.rotationPitch - ridingEntity.prevRotationPitch;

                while (entityRiderYawDelta >= 180.0D) {
                    this.entityRiderYawDelta -= 360.0D;
                }

                while (entityRiderYawDelta < -180.0D) {
                    this.entityRiderYawDelta += 360.0D;
                }

                while (entityRiderPitchDelta >= 180.0D) {
                    this.entityRiderPitchDelta -= 360.0D;
                }

                while (entityRiderPitchDelta < -180.0D) {
                    this.entityRiderPitchDelta += 360.0D;
                }

                double d0 = entityRiderYawDelta * 0.5D;
                double d1 = entityRiderPitchDelta * 0.5D;
                float f = 10.0F;

                if (d0 > (double) f) {
                    d0 = f;
                }

                if (d0 < (double) -f) {
                    d0 = -f;
                }

                if (d1 > (double) f) {
                    d1 = f;
                }

                if (d1 < (double) -f) {
                    d1 = -f;
                }

                this.entityRiderYawDelta -= d0;
                this.entityRiderPitchDelta -= d1;
            }
        }
    }

    public void updateRiderPosition() {
        if (riddenByEntity != null) {
            riddenByEntity.setPosition(posX, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ);
        }
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset() {
        return 0.0D;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset() {
        return (double) height * 0.75D;
    }

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity entityIn) {
        this.entityRiderPitchDelta = 0.0D;
        this.entityRiderYawDelta = 0.0D;

        if (entityIn == null) {
            if (ridingEntity != null) {
                setLocationAndAngles(ridingEntity.posX, ridingEntity.getEntityBoundingBox().minY + (double) ridingEntity.height, ridingEntity.posZ, rotationYaw, rotationPitch);
                ridingEntity.riddenByEntity = null;
            }

            this.ridingEntity = null;
        } else {
            if (ridingEntity != null) {
                ridingEntity.riddenByEntity = null;
            }

            if (entityIn != null) {
                for (Entity entity = entityIn.ridingEntity; entity != null; entity = entity.ridingEntity) {
                    if (entity == this) {
                        return;
                    }
                }
            }

            this.ridingEntity = entityIn;
            entityIn.riddenByEntity = this;
        }
    }

    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements,
                                        boolean p_180426_10_) {
        setPosition(x, y, z);
        setRotation(yaw, pitch);
        List<AxisAlignedBB> list = worldObj
                .getCollidingBoundingBoxes(this, getEntityBoundingBox().contract(0.03125D, 0.0D, 0.03125D));

        if (!list.isEmpty()) {
            double d0 = 0.0D;

            for (AxisAlignedBB axisalignedbb : list) {
                if (axisalignedbb.maxY > d0) {
                    d0 = axisalignedbb.maxY;
                }
            }

            y = y + (d0 - getEntityBoundingBox().minY);
            setPosition(x, y, z);
        }
    }

    public float getCollisionBorderSize() {
        if (Novoline.getInstance().getModuleManager().getModule(HitBox.class).isEnabled()) {
            return Novoline.getInstance().getModuleManager().getModule(HitBox.class).getHitBoxSize().get();
        } else {
            return 0.1F;
        }
    }

    /**
     * returns a (normalized) vector of where this entity is looking
     */
    public Vec3 getLookVec() {
        return null;
    }

    public void func_181015_d(BlockPos p_181015_1_) {
        if (timeUntilPortal > 0) {
            this.timeUntilPortal = getPortalCoolDown();
        } else {
            if (!worldObj.isRemote && !p_181015_1_.equals(field_181016_an)) {
                this.field_181016_an = p_181015_1_;
                BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal
                        .func_181089_f(worldObj, p_181015_1_);
                double d0 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ?
                        (double) blockpattern$patternhelper.func_181117_a().getZ() :
                        (double) blockpattern$patternhelper.func_181117_a().getX();
                double d1 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? posZ : posX;
                d1 = Math.abs(MathHelper.func_181160_c(d1 - (double) (blockpattern$patternhelper.getFinger().rotateY()
                                .getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0), d0,
                        d0 - (double) blockpattern$patternhelper.func_181118_d()));
                double d2 = MathHelper
                        .func_181160_c(posY - 1.0D, blockpattern$patternhelper.func_181117_a().getY(),
                                blockpattern$patternhelper.func_181117_a().getY() - blockpattern$patternhelper
                                        .func_181119_e());
                this.field_181017_ao = new Vec3(d1, d2, 0.0D);
                this.field_181018_ap = blockpattern$patternhelper.getFinger();
            }

            this.inPortal = true;
        }
    }

    /**
     * Return the amount of cooldown before this entity can use a portal again.
     */
    public int getPortalCoolDown() {
        return 300;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    public void handleStatusUpdate(byte id) {
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation() {
    }

    /**
     * returns the inventory of this entity (only used in EntityPlayerMP it seems)
     */
    public ItemStack[] getInventory() {
        return null;
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning() {
        boolean flag = worldObj != null && worldObj.isRemote;
        return !isImmuneToFire && (fire > 0 || flag && getFlag(0));
    }

    /**
     * Returns true if the entity is riding another entity, used by render to rotate the legs to be in 'sit' position
     * for players.
     */
    public boolean isRiding() {
        return ridingEntity != null;
    }

    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking() {
        return getFlag(1);
    }

    /**
     * Sets the sneaking flag.
     */
    public void setSneaking(boolean sneaking) {
        setFlag(1, sneaking);
    }

    /**
     * Get if the Entity is sprinting.
     */
    public boolean isSprinting() {
        return getFlag(3);
    }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting) {
        setFlag(3, sprinting);
    }

    public boolean isInvisible() {
        return getFlag(5);
    }

    public void setInvisible(boolean invisible) {
        setFlag(5, invisible);
    }

    /**
     * Only used by renderer in EntityLivingBase subclasses.
     * Determines if an entity is visible or not to a specfic player, if the entity is normally invisible.
     * For EntityLivingBase subclasses, returning false when invisible will render the entity semitransparent.
     */
    public boolean isInvisibleToPlayer(EntityPlayer player) {
        return !player.isSpectator() && isInvisible();
    }

    public boolean isEating() {
        return getFlag(4);
    }

    public void setEating(boolean eating) {
        setFlag(4, eating);
    }

    /**
     * Returns true if the flag is active for the entity. Known flags: 0) is burning; 1) is sneaking; 2) is riding
     * something; 3) is sprinting; 4) is eating
     */
    protected boolean getFlag(int flag) {
        return (dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0;
    }

    /**
     * Enable or disable a entity flag, see getEntityFlag to read the know flags.
     */
    protected void setFlag(int flag, boolean set) {
        byte b0 = dataWatcher.getWatchableObjectByte(0);

        if (set) {
            dataWatcher.updateObject(0, (byte) (b0 | 1 << flag));
        } else {
            dataWatcher.updateObject(0, (byte) (b0 & ~(1 << flag)));
        }
    }

    public int getAir() {
        return dataWatcher.getWatchableObjectShort(1);
    }

    public void setAir(int air) {
        dataWatcher.updateObject(1, (short) air);
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        attackEntityFrom(DamageSource.lightningBolt, 5.0F);
        ++this.fire;

        if (fire == 0) {
            setFire(8);
        }
    }

    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLivingBase entityLivingIn) {
    }

    protected boolean pushOutOfBlocks(double x, double y, double z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        double d0 = x - (double) blockpos.getX();
        double d1 = y - (double) blockpos.getY();
        double d2 = z - (double) blockpos.getZ();
        List<AxisAlignedBB> list = worldObj.func_147461_a(getEntityBoundingBox());

        if (list.isEmpty() && !worldObj.isBlockFullCube(blockpos)) {
            return false;
        } else {
            int i = 3;
            double d3 = 9999.0D;

            if (!worldObj.isBlockFullCube(blockpos.west()) && d0 < d3) {
                d3 = d0;
                i = 0;
            }

            if (!worldObj.isBlockFullCube(blockpos.east()) && 1.0D - d0 < d3) {
                d3 = 1.0D - d0;
                i = 1;
            }

            if (!worldObj.isBlockFullCube(blockpos.up()) && 1.0D - d1 < d3) {
                d3 = 1.0D - d1;
                i = 3;
            }

            if (!worldObj.isBlockFullCube(blockpos.north()) && d2 < d3) {
                d3 = d2;
                i = 4;
            }

            if (!worldObj.isBlockFullCube(blockpos.south()) && 1.0D - d2 < d3) {
                d3 = 1.0D - d2;
                i = 5;
            }

            float f = rand.nextFloat() * 0.2F + 0.1F;

            switch (i) {
                case 0:
                    this.motionX = -f;
                    break;
                case 1:
                    this.motionX = f;
                    break;
                case 3:
                    this.motionY = f;
                    break;
                case 4:
                    this.motionZ = -f;
                    break;
                case 5:
                    this.motionZ = f;
                    break;
            }

            return true;
        }
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb() {
        this.isInWeb = true;
        this.fallDistance = 0.0F;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
    public String getName() {
        if (hasCustomName()) {
            return getCustomNameTag();
        } else {
            String s = EntityList.getEntityString(this);

            if (s == null) {
                s = "generic";
            }

            return StatCollector.translateToLocal("entity." + s + ".name");
        }
    }

    /**
     * Return the Entity parts making up this Entity (currently only for dragons)
     */
    public Entity[] getParts() {
        return null;
    }

    /**
     * Returns true if Entity argument is equal to this Entity
     */
    public boolean isEntityEqual(Entity entityIn) {
        return this == entityIn;
    }

    public float getRotationYawHead() {
        return 0.0F;
    }

    /**
     * Sets the head's yaw rotation of the entity.
     */
    public void setRotationYawHead(float rotation) {
    }

    public void func_181013_g(float p_181013_1_) {
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem() {
        return true;
    }

    /**
     * Called when a player attacks an entity. If this returns true the attack will not happen.
     */
    public boolean hitByEntity(Entity entityIn) {
        return false;
    }

    public String toString() {
        return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]",
                getClass().getCanonicalName(),
                getName(), entityId,
                worldObj == null ? "~NULL~" : worldObj.getWorldInfo().getWorldName(),
                posX, posY, posZ);
    }

    public boolean isEntityInvulnerable(DamageSource source) {
        return invulnerable && source != DamageSource.outOfWorld && !source.isCreativePlayer();
    }

    /**
     * Sets this entity's location and angles to the location and angles of the passed in entity.
     */
    public void copyLocationAndAnglesFrom(Entity entityIn) {
        setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
    }

    /**
     * Prepares this entity in new dimension by copying NBT data from entity in old dimension
     */
    public void copyDataFromOld(Entity entityIn) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        entityIn.writeToNBT(nbttagcompound);
        readFromNBT(nbttagcompound);
        this.timeUntilPortal = entityIn.timeUntilPortal;
        this.field_181016_an = entityIn.field_181016_an;
        this.field_181017_ao = entityIn.field_181017_ao;
        this.field_181018_ap = entityIn.field_181018_ap;
    }

    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public void travelToDimension(int dimensionId) {
        if (!worldObj.isRemote && !isDead) {
            worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            int i = dimension;
            WorldServer worldserver = minecraftserver.worldServerForDimension(i);
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionId);
            this.dimension = dimensionId;

            if (i == 1 && dimensionId == 1) {
                worldserver1 = minecraftserver.worldServerForDimension(0);
                this.dimension = 0;
            }

            worldObj.removeEntity(this);
            this.isDead = false;
            worldObj.theProfiler.startSection("reposition");
            minecraftserver.getConfigurationManager().transferEntityToWorld(this, i, worldserver, worldserver1);
            worldObj.theProfiler.endStartSection("reloading");
            Entity entity = EntityList.createEntityByName(EntityList.getEntityString(this), worldserver1);

            if (entity != null) {
                entity.copyDataFromOld(this);

                if (i == 1 && dimensionId == 1) {
                    BlockPos blockpos = worldObj.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
                    entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);
                }

                worldserver1.spawnEntityInWorld(entity);
            }

            this.isDead = true;
            worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            worldObj.theProfiler.endSection();
        }
    }

    /**
     * Explosion resistance of a block relative to this entity
     */
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlock().getExplosionResistance(this);
    }

    public boolean verifyExplosion(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn,
                                   float p_174816_5_) {
        return true;
    }

    /**
     * The maximum height from where the entity is alowed to jump (used in pathfinder)
     */
    public int getMaxFallHeight() {
        return 3;
    }

    public Vec3 func_181014_aG() {
        return field_181017_ao;
    }

    public EnumFacing func_181012_aH() {
        return field_181018_ap;
    }

    /**
     * Return whether this entity should NOT trigger a pressure plate or a tripwire.
     */
    public boolean doesEntityNotTriggerPressurePlate() {
        return false;
    }

    public void addEntityCrashInfo(CrashReportCategory category) {
        category.addCrashSectionCallable("Entity Type", () -> EntityList.getEntityString(Entity.this) + " (" + getClass().getCanonicalName() + ")");
        category.addCrashSection("Entity ID", entityId);
        category.addCrashSectionCallable("Entity Name", Entity.this::getName);
        category.addCrashSection("Entity's Exact location",
                String.format("%.2f, %.2f, %.2f", posX, posY, posZ));
        category.addCrashSection("Entity's Block location", CrashReportCategory
                .getCoordinateInfo(MathHelper.floor_double(posX), MathHelper.floor_double(posY),
                        MathHelper.floor_double(posZ)));
        category.addCrashSection("Entity's Momentum",
                String.format("%.2f, %.2f, %.2f", motionX, motionY, motionZ));
        category.addCrashSectionCallable("Entity's Rider", () -> riddenByEntity.toString());
        category.addCrashSectionCallable("Entity's Vehicle", () -> ridingEntity.toString());
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    public boolean canRenderOnFire() {
        return isBurning();
    }

    public UUID getUniqueID() {
        return getEntityUniqueID();
    }

    public boolean isPushedByWater() {
        return true;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Override
    public IChatComponent getDisplayName() {
        ChatComponentText chatcomponenttext = new ChatComponentText(getName());
        chatcomponenttext.getChatStyle().setChatHoverEvent(getHoverEvent());
        chatcomponenttext.getChatStyle().setInsertion(getUniqueID().toString());
        return chatcomponenttext;
    }

    public String getCustomNameTag() {
        return dataWatcher.getWatchableObjectString(2);
    }

    /**
     * Sets the custom name tag for this entity
     */
    public void setCustomNameTag(String name) {
        dataWatcher.updateObject(2, name);
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName() {
        return dataWatcher.getWatchableObjectString(2).length() > 0;
    }

    public boolean getAlwaysRenderNameTag() {
        return dataWatcher.getWatchableObjectByte(3) == 1;
    }

    public void setAlwaysRenderNameTag(boolean alwaysRenderNameTag) {
        dataWatcher.updateObject(3, (byte) (alwaysRenderNameTag ? 1 : 0));
    }

    /**
     * Sets the position of the entity and updates the 'last' variables
     */
    public void setPositionAndUpdate(double x, double y, double z) {
        setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
    }

    public boolean getAlwaysRenderNameTagForRender() {
        return getAlwaysRenderNameTag();
    }

    public void onDataWatcherUpdate(int dataID) {
    }

    public EnumFacing getHorizontalFacing() {
        return EnumFacing
                .getHorizontal(MathHelper.floor_double((double) (rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
    }

    protected HoverEvent getHoverEvent() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        String s = EntityList.getEntityString(this);
        nbttagcompound.setString("id", getUniqueID().toString());

        if (s != null) {
            nbttagcompound.setString("type", s);
        }

        nbttagcompound.setString("name", getName());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ChatComponentText(nbttagcompound.toString()));
    }

    public boolean isSpectatedByPlayer(EntityPlayerMP player) {
        return true;
    }

    public AxisAlignedBB getEntityBoundingBox() {
        return boundingBox;
    }

    public void setEntityBoundingBox(AxisAlignedBB bb) {
        this.boundingBox = bb;
    }

    public float getEyeHeight() {
        return height * 0.85F;
    }

    public boolean isOutsideBorder() {
        return isOutsideBorder;
    }

    public void setOutsideBorder(boolean outsideBorder) {
        this.isOutsideBorder = outsideBorder;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        return false;
    }

    /**
     * Send a chat message to the CommandSender
     */
    @Override
    public void addChatMessage(IChatComponent component) {
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    @Override
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return true;
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    @Override
    public BlockPos getPosition() {
        return new BlockPos(posX, posY + 0.5D, posZ);
    }

    /**
     * Get the position vector. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return 0.0D,
     * 0.0D, 0.0D
     */
    @Override
    public Vec3 getPositionVector() {
        return new Vec3(posX, posY, posZ);
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    @Override
    public World getEntityWorld() {
        return worldObj;
    }

    /**
     * Returns the entity associated with the command sender. MAY BE NULL!
     */
    @Override
    public Entity getCommandSenderEntity() {
        return this;
    }

    /**
     * Returns true if the command sender should be sent feedback about executed commands
     */
    @Override
    public boolean sendCommandFeedback() {
        return false;
    }

    @Override
    public void setCommandStat(CommandResultStats.Type type, int amount) {
        cmdResultStats.func_179672_a(this, type, amount);
    }

    public CommandResultStats getCommandStats() {
        return cmdResultStats;
    }

    public void func_174817_o(Entity entityIn) {
        cmdResultStats.func_179671_a(entityIn.getCommandStats());
    }

    public NBTTagCompound getNBTTagCompound() {
        return null;
    }

    /**
     * Called when client receives entity's NBTTagCompound from server.
     */
    public void clientUpdateEntityNBT(NBTTagCompound compound) {
    }

    /**
     * New version of interactWith that includes vector information on where precisely the player targeted.
     */
    public boolean interactAt(EntityPlayer player, Vec3 targetVec3) {
        return false;
    }

    public boolean isImmuneToExplosions() {
        return false;
    }

    protected void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            EnchantmentHelper.applyThornEnchantments((EntityLivingBase) entityIn, entityLivingBaseIn);
        }

        EnchantmentHelper.applyArthropodEnchantments(entityLivingBaseIn, entityIn);
    }

    public boolean isBlockUnder() {
        for (int i = (int) (posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(posX, i, posZ);
            if (Minecraft.getInstance().world.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }

    public double getLastTickDistance() {
        return Math.hypot(posX - prevPosX, posZ - prevPosZ);
    }

    public void resetLastTickDistance() {
        this.prevPosX = posX;
        this.prevPosZ = posZ;
    }

    public UUID getEntityUniqueID() {
        return entityUniqueID;
    }

    public void setEntityUniqueID(UUID entityUniqueID) {
        this.entityUniqueID = entityUniqueID;
    }
}
