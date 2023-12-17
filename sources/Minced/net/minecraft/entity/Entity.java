// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.network.datasync.DataSerializers;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.block.material.EnumPushReaction;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.world.Explosion;
import net.minecraft.world.Teleporter;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.scoreboard.Team;
import net.minecraft.inventory.EntityEquipmentSlot;
import com.google.common.collect.Iterables;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec2f;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.COMBAT.HitBox;
import ru.tuskevich.Minced;
import ru.tuskevich.event.events.impl.EventEntityBoundingBox;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.item.EntityBoat;
import javax.annotation.Nullable;
import net.minecraft.block.SoundType;
import net.minecraft.util.SoundEvent;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.EventMove;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Arrays;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;
import com.google.common.collect.Sets;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Lists;
import java.util.Set;
import net.minecraft.command.CommandResultStats;
import java.util.UUID;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.item.ItemStack;
import java.util.List;
import org.apache.logging.log4j.Logger;
import net.minecraft.command.ICommandSender;

public abstract class Entity implements ICommandSender
{
    private static final Logger LOGGER;
    private static final List<ItemStack> EMPTY_EQUIPMENT;
    private static final AxisAlignedBB ZERO_AABB;
    private static double renderDistanceWeight;
    private static int nextEntityID;
    public float rotationPitchHead;
    public float prevRotationPitchHead;
    private int entityId;
    public boolean preventEntitySpawning;
    private final List<Entity> riddenByEntities;
    protected int rideCooldown;
    private Entity ridingEntity;
    public boolean forceSpawn;
    public World world;
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
    public AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean collidedHorizontally;
    public boolean collidedVertically;
    public boolean collided;
    public boolean velocityChanged;
    public boolean isInWeb;
    private boolean isOutsideBorder;
    public boolean isDead;
    public float width;
    public float height;
    public float prevDistanceWalkedModified;
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;
    private int nextStepDistance;
    private float nextFlap;
    public double lastTickPosX;
    public double lastTickPosY;
    public double lastTickPosZ;
    public float stepHeight;
    public boolean noClip;
    public float entityCollisionReduction;
    protected Random rand;
    public int ticksExisted;
    private int fire;
    protected boolean inWater;
    public int hurtResistantTime;
    protected boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected EntityDataManager dataManager;
    protected static final DataParameter<Byte> FLAGS;
    private static final DataParameter<Integer> AIR;
    private static final DataParameter<String> CUSTOM_NAME;
    private static final DataParameter<Boolean> CUSTOM_NAME_VISIBLE;
    private static final DataParameter<Boolean> SILENT;
    private static final DataParameter<Boolean> NO_GRAVITY;
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public long serverPosX;
    public long serverPosY;
    public long serverPosZ;
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;
    protected boolean inPortal;
    protected int portalCounter;
    public int dimension;
    protected BlockPos lastPortalPos;
    protected Vec3d lastPortalVec;
    protected EnumFacing teleportDirection;
    private boolean invulnerable;
    protected UUID entityUniqueID;
    protected String cachedUniqueIdString;
    private final CommandResultStats cmdResultStats;
    protected boolean glowing;
    private final Set<String> tags;
    private boolean isPositionDirty;
    private final double[] pistonDeltas;
    private long pistonDeltasGameTime;
    public double predX;
    public double predY;
    public double predZ;
    
    public Entity(final World worldIn) {
        this.entityId = Entity.nextEntityID++;
        this.riddenByEntities = (List<Entity>)Lists.newArrayList();
        this.boundingBox = Entity.ZERO_AABB;
        this.width = 0.6f;
        this.height = 1.8f;
        this.nextStepDistance = 1;
        this.nextFlap = 1.0f;
        this.rand = new Random();
        this.fire = -this.getFireImmuneTicks();
        this.firstUpdate = true;
        this.entityUniqueID = MathHelper.getRandomUUID(this.rand);
        this.cachedUniqueIdString = this.entityUniqueID.toString();
        this.cmdResultStats = new CommandResultStats();
        this.tags = (Set<String>)Sets.newHashSet();
        this.pistonDeltas = new double[] { 0.0, 0.0, 0.0 };
        this.world = worldIn;
        this.setPosition(0.0, 0.0, 0.0);
        if (worldIn != null) {
            this.dimension = worldIn.provider.getDimensionType().getId();
        }
        (this.dataManager = new EntityDataManager(this)).register(Entity.FLAGS, (Byte)0);
        this.dataManager.register(Entity.AIR, 300);
        this.dataManager.register(Entity.CUSTOM_NAME_VISIBLE, false);
        this.dataManager.register(Entity.CUSTOM_NAME, "");
        this.dataManager.register(Entity.SILENT, false);
        this.dataManager.register(Entity.NO_GRAVITY, false);
        this.entityInit();
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public void setEntityId(final int id) {
        this.entityId = id;
    }
    
    public Set<String> getTags() {
        return this.tags;
    }
    
    public boolean addTag(final String tag) {
        if (this.tags.size() >= 1024) {
            return false;
        }
        this.tags.add(tag);
        return true;
    }
    
    public boolean removeTag(final String tag) {
        return this.tags.remove(tag);
    }
    
    public void onKillCommand() {
        this.setDead();
    }
    
    protected abstract void entityInit();
    
    public EntityDataManager getDataManager() {
        return this.dataManager;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return p_equals_1_ instanceof Entity && ((Entity)p_equals_1_).entityId == this.entityId;
    }
    
    @Override
    public int hashCode() {
        return this.entityId;
    }
    
    protected void preparePlayerToSpawn() {
        if (this.world != null) {
            while (this.posY > 0.0 && this.posY < 256.0) {
                this.setPosition(this.posX, this.posY, this.posZ);
                if (this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
                    break;
                }
                ++this.posY;
            }
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
            this.rotationPitch = 0.0f;
        }
    }
    
    public void setDead() {
        this.isDead = true;
    }
    
    public void setDropItemsWhenDead(final boolean dropWhenDead) {
    }
    
    protected void setSize(final float width, final float height) {
        if (width != this.width || height != this.height) {
            final float f = this.width;
            this.width = width;
            this.height = height;
            if (this.width < f) {
                final double d0 = width / 2.0;
                this.setEntityBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d0, this.posX + d0, this.posY + this.height, this.posZ + d0));
                return;
            }
            final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            this.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + this.width, axisalignedbb.minY + this.height, axisalignedbb.minZ + this.width));
            if (this.width > f && !this.firstUpdate && !this.world.isRemote) {
                this.move(MoverType.SELF, f - this.width, 0.0, f - this.width);
            }
        }
    }
    
    protected void setRotation(final float yaw, final float pitch) {
        this.rotationYaw = yaw % 360.0f;
        this.rotationPitch = pitch % 360.0f;
    }
    
    public void setPosition(final double x, final double y, final double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        final float f = this.width / 2.0f;
        final float f2 = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(x - f, y, z - f, x + f, y + f2, z + f));
    }
    
    public void turn(final float yaw, final float pitch) {
        final float f = this.rotationPitch;
        final float f2 = this.rotationYaw;
        this.rotationYaw += (float)(yaw * 0.15);
        this.rotationPitch -= (float)(pitch * 0.15);
        this.rotationPitch = MathHelper.clamp(this.rotationPitch, -90.0f, 90.0f);
        this.prevRotationPitch += this.rotationPitch - f;
        this.prevRotationYaw += this.rotationYaw - f2;
        if (this.ridingEntity != null) {
            this.ridingEntity.applyOrientationToEntity(this);
        }
    }
    
    public void onUpdate() {
        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        }
        this.onEntityUpdate();
    }
    
    public void onEntityUpdate() {
        this.world.profiler.startSection("entityBaseTick");
        if (this.isRiding() && this.getRidingEntity().isDead) {
            this.dismountRidingEntity();
        }
        if (this.rideCooldown > 0) {
            --this.rideCooldown;
        }
        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        if (!this.world.isRemote && this.world instanceof WorldServer) {
            this.world.profiler.startSection("portal");
            if (this.inPortal) {
                final MinecraftServer minecraftserver = this.world.getMinecraftServer();
                if (minecraftserver.getAllowNether()) {
                    if (!this.isRiding()) {
                        final int i = this.getMaxInPortalTime();
                        if (this.portalCounter++ >= i) {
                            this.portalCounter = i;
                            this.timeUntilPortal = this.getPortalCooldown();
                            int j;
                            if (this.world.provider.getDimensionType().getId() == -1) {
                                j = 0;
                            }
                            else {
                                j = -1;
                            }
                            this.changeDimension(j);
                        }
                    }
                    this.inPortal = false;
                }
            }
            else {
                if (this.portalCounter > 0) {
                    this.portalCounter -= 4;
                }
                if (this.portalCounter < 0) {
                    this.portalCounter = 0;
                }
            }
            this.decrementTimeUntilPortal();
            this.world.profiler.endSection();
        }
        this.spawnRunningParticles();
        this.handleWaterMovement();
        if (this.world.isRemote) {
            this.extinguish();
        }
        else if (this.fire > 0) {
            if (this.isImmuneToFire) {
                this.fire -= 4;
                if (this.fire < 0) {
                    this.extinguish();
                }
            }
            else {
                if (this.fire % 20 == 0) {
                    this.attackEntityFrom(DamageSource.ON_FIRE, 1.0f);
                }
                --this.fire;
            }
        }
        if (this.isInLava()) {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5f;
        }
        if (this.posY < -64.0) {
            this.outOfWorld();
        }
        if (!this.world.isRemote) {
            this.setFlag(0, this.fire > 0);
        }
        this.firstUpdate = false;
        this.world.profiler.endSection();
    }
    
    protected void decrementTimeUntilPortal() {
        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }
    }
    
    public int getMaxInPortalTime() {
        return 1;
    }
    
    protected void setOnFireFromLava() {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.LAVA, 4.0f);
            this.setFire(15);
        }
    }
    
    public void setFire(final int seconds) {
        int i = seconds * 20;
        if (this instanceof EntityLivingBase) {
            i = EnchantmentProtection.getFireTimeForEntity((EntityLivingBase)this, i);
        }
        if (this.fire < i) {
            this.fire = i;
        }
    }
    
    public void extinguish() {
        this.fire = 0;
    }
    
    protected void outOfWorld() {
        this.setDead();
    }
    
    public boolean isOffsetPositionInLiquid(final double x, final double y, final double z) {
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().offset(x, y, z);
        return this.isLiquidPresentInAABB(axisalignedbb);
    }
    
    private boolean isLiquidPresentInAABB(final AxisAlignedBB bb) {
        return this.world.getCollisionBoxes(this, bb).isEmpty() && !this.world.containsAnyLiquid(bb);
    }
    
    public void move(final MoverType type, double x, double y, double z) {
        final double preX = this.posX;
        final double preZ = this.posZ;
        final AxisAlignedBB before = this.getEntityBoundingBox().addCoord(0.0, 0.0, 0.0);
        if (this.noClip) {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
            this.resetPositionToBB();
        }
        else {
            if (type == MoverType.PISTON) {
                final long i = this.world.getTotalWorldTime();
                if (i != this.pistonDeltasGameTime) {
                    Arrays.fill(this.pistonDeltas, 0.0);
                    this.pistonDeltasGameTime = i;
                }
                if (x != 0.0) {
                    final int j = EnumFacing.Axis.X.ordinal();
                    final double d0 = MathHelper.clamp(x + this.pistonDeltas[j], -0.51, 0.51);
                    x = d0 - this.pistonDeltas[j];
                    this.pistonDeltas[j] = d0;
                    if (Math.abs(x) <= 9.999999747378752E-6) {
                        return;
                    }
                }
                else if (y != 0.0) {
                    final int l4 = EnumFacing.Axis.Y.ordinal();
                    final double d2 = MathHelper.clamp(y + this.pistonDeltas[l4], -0.51, 0.51);
                    y = d2 - this.pistonDeltas[l4];
                    this.pistonDeltas[l4] = d2;
                    if (Math.abs(y) <= 9.999999747378752E-6) {
                        return;
                    }
                }
                else {
                    if (z == 0.0) {
                        return;
                    }
                    final int i2 = EnumFacing.Axis.Z.ordinal();
                    final double d3 = MathHelper.clamp(z + this.pistonDeltas[i2], -0.51, 0.51);
                    z = d3 - this.pistonDeltas[i2];
                    this.pistonDeltas[i2] = d3;
                    if (Math.abs(z) <= 9.999999747378752E-6) {
                        return;
                    }
                }
            }
            this.world.profiler.startSection("move");
            final double d4 = this.posX;
            final double d5 = this.posY;
            final double d6 = this.posZ;
            if (this.isInWeb) {
                this.isInWeb = false;
                x *= 0.25;
                y *= 0.05000000074505806;
                z *= 0.25;
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            double d7 = x;
            double d8 = y;
            double d9 = z;
            if ((type == MoverType.SELF || type == MoverType.PLAYER) && this.onGround && this.isSneaking()) {
                final double d10 = 0.05;
                while (x != 0.0 && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(x, -this.stepHeight, 0.0)).isEmpty()) {
                    if (x < 0.05 && x >= -0.05) {
                        x = 0.0;
                    }
                    else if (x > 0.0) {
                        x -= 0.05;
                    }
                    else {
                        x += 0.05;
                    }
                    d7 = x;
                }
                while (z != 0.0 && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(0.0, -this.stepHeight, z)).isEmpty()) {
                    if (z < 0.05 && z >= -0.05) {
                        z = 0.0;
                    }
                    else if (z > 0.0) {
                        z -= 0.05;
                    }
                    else {
                        z += 0.05;
                    }
                    d9 = z;
                }
                while (x != 0.0 && z != 0.0 && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(x, -this.stepHeight, z)).isEmpty()) {
                    if (x < 0.05 && x >= -0.05) {
                        x = 0.0;
                    }
                    else if (x > 0.0) {
                        x -= 0.05;
                    }
                    else {
                        x += 0.05;
                    }
                    d7 = x;
                    if (z < 0.05 && z >= -0.05) {
                        z = 0.0;
                    }
                    else if (z > 0.0) {
                        z -= 0.05;
                    }
                    else {
                        z += 0.05;
                    }
                    d9 = z;
                }
            }
            List<AxisAlignedBB> list1 = this.world.getCollisionBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
            final Vec3d collisionOffset = new Vec3d(0.0, 0.0, 0.0);
            final Vec3d backUp = new Vec3d(this.posX, this.posY, this.posZ);
            boolean ignoreVertical = false;
            boolean ignoreHorizontal = false;
            final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            if (this instanceof EntityPlayerSP) {
                final double bpMX = x;
                final double bpMY = y;
                final double bpMZ = z;
                double predictX = this.posX;
                double predictY = this.posY;
                double predictZ = this.posZ;
                if (y != 0.0) {
                    for (int k = 0, m = list1.size(); k < m; ++k) {
                        y = list1.get(k).calculateYOffset(this.getEntityBoundingBox(), y);
                    }
                    predictY += y;
                }
                if (x != 0.0) {
                    for (int j2 = 0, l5 = list1.size(); j2 < l5; ++j2) {
                        x = list1.get(j2).calculateXOffset(this.getEntityBoundingBox(), x);
                    }
                    if (x != 0.0) {
                        predictX += x;
                    }
                }
                if (z != 0.0) {
                    for (int k2 = 0, i3 = list1.size(); k2 < i3; ++k2) {
                        z = list1.get(k2).calculateZOffset(this.getEntityBoundingBox(), z);
                    }
                    if (z != 0.0) {
                        predictZ += z;
                    }
                }
                boolean predictGround = false;
                final boolean flag = this.onGround || (d8 != y && d8 < 0.0);
                if (this.stepHeight > 0.0f && flag && (d7 != x || d9 != z)) {
                    final double d11 = x;
                    final double d12 = y;
                    final double d13 = z;
                    y = this.stepHeight;
                    final List<AxisAlignedBB> list2 = this.world.getCollisionBoxes(this, this.getEntityBoundingBox().addCoord(d7, y, d9));
                    AxisAlignedBB axisalignedbb2 = this.getEntityBoundingBox();
                    final AxisAlignedBB axisalignedbb3 = axisalignedbb2.addCoord(d7, 0.0, d9);
                    double d14 = y;
                    for (int j3 = 0, k3 = list2.size(); j3 < k3; ++j3) {
                        d14 = list2.get(j3).calculateYOffset(axisalignedbb3, d14);
                    }
                    axisalignedbb2 = axisalignedbb2.offset(0.0, d14, 0.0);
                    double d15 = d7;
                    for (int l6 = 0, i4 = list2.size(); l6 < i4; ++l6) {
                        d15 = list2.get(l6).calculateXOffset(axisalignedbb2, d15);
                    }
                    axisalignedbb2 = axisalignedbb2.offset(d15, 0.0, 0.0);
                    double d16 = d9;
                    for (int j4 = 0, k4 = list2.size(); j4 < k4; ++j4) {
                        d16 = list2.get(j4).calculateZOffset(axisalignedbb2, d16);
                    }
                    axisalignedbb2 = axisalignedbb2.offset(0.0, 0.0, d16);
                    AxisAlignedBB axisalignedbb4 = this.getEntityBoundingBox();
                    double d17 = y;
                    for (int l7 = 0, i5 = list2.size(); l7 < i5; ++l7) {
                        d17 = list2.get(l7).calculateYOffset(axisalignedbb4, d17);
                    }
                    axisalignedbb4 = axisalignedbb4.offset(0.0, d17, 0.0);
                    double d18 = d7;
                    for (int j5 = 0, k5 = list2.size(); j5 < k5; ++j5) {
                        d18 = list2.get(j5).calculateXOffset(axisalignedbb4, d18);
                    }
                    axisalignedbb4 = axisalignedbb4.offset(d18, 0.0, 0.0);
                    double d19 = d9;
                    for (int l8 = 0, i6 = list2.size(); l8 < i6; ++l8) {
                        d19 = list2.get(l8).calculateZOffset(axisalignedbb4, d19);
                    }
                    axisalignedbb4 = axisalignedbb4.offset(0.0, 0.0, d19);
                    final double d20 = d15 * d15 + d16 * d16;
                    final double d21 = d18 * d18 + d19 * d19;
                    if (d20 > d21) {
                        x = d15;
                        z = d16;
                        y = -d14;
                    }
                    else {
                        x = d18;
                        z = d19;
                        y = -d17;
                    }
                    for (int j6 = 0, k6 = list2.size(); j6 < k6; ++j6) {
                        y = list2.get(j6).calculateYOffset(this.getEntityBoundingBox(), y);
                    }
                    if (d11 * d11 + d13 * d13 >= x * x + z * z) {
                        x = d11;
                        y = d12;
                        z = d13;
                    }
                }
                final boolean collidedHorizontally = d7 != x || d9 != z;
                final boolean collidedVertically = d8 != y;
                predictGround = (collidedVertically && d8 < 0.0);
                x = bpMX;
                y = bpMY;
                z = bpMZ;
                final EventMove move = new EventMove(new Vec3d(this.posX, this.posY, this.posZ), new Vec3d(predictX, predictY, predictZ), new Vec3d(x, y, z), collisionOffset, predictGround, collidedHorizontally, collidedVertically, before);
                EventManager.call(move);
                ignoreVertical = move.isIgnoreVertical();
                ignoreHorizontal = move.isIgnoreHorizontal();
                x = move.motion().x;
                y = move.motion().y;
                z = move.motion().z;
                d7 = move.motion().x;
                d8 = move.motion().y;
                d9 = move.motion().z;
            }
            list1 = this.world.getCollisionBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
            if (this instanceof EntityPlayerSP) {
                final List<AxisAlignedBB> bb = this.world.getCollisionBoxes2(this, this.getEntityBoundingBox().addCoord(x, y, z).offset(collisionOffset.x, collisionOffset.y, collisionOffset.z));
                if (!bb.isEmpty() && collisionOffset.length() > 0.0) {
                    for (final AxisAlignedBB aabb : bb) {
                        AxisAlignedBB aabb2 = this.getEntityBoundingBox().offset(collisionOffset.x, collisionOffset.y, collisionOffset.z);
                        if (x != 0.0) {
                            for (int j7 = 0, l9 = bb.size(); j7 < l9; ++j7) {
                                x = bb.get(j7).calculateXOffset(aabb2, x);
                            }
                            if (x != 0.0) {
                                aabb2 = aabb2.offset(x, 0.0, 0.0);
                            }
                            d7 = x;
                        }
                        if (z != 0.0) {
                            for (int k7 = 0, i7 = bb.size(); k7 < i7; ++k7) {
                                z = bb.get(k7).calculateZOffset(aabb2, z);
                            }
                            if (z != 0.0) {
                                aabb2 = aabb2.offset(0.0, 0.0, z);
                            }
                            d9 = z;
                        }
                    }
                }
            }
            if (y != 0.0) {
                int k8 = 0;
                if (!ignoreVertical) {
                    for (int l10 = list1.size(); k8 < l10; ++k8) {
                        y = list1.get(k8).calculateYOffset(this.getEntityBoundingBox(), y);
                    }
                }
                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
            }
            if (x != 0.0) {
                int j8 = 0;
                if (!ignoreHorizontal) {
                    for (int l11 = list1.size(); j8 < l11; ++j8) {
                        x = list1.get(j8).calculateXOffset(this.getEntityBoundingBox(), x);
                    }
                }
                if (x != 0.0) {
                    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0, 0.0));
                }
            }
            if (z != 0.0) {
                int k9 = 0;
                if (!ignoreHorizontal) {
                    for (int i8 = list1.size(); k9 < i8; ++k9) {
                        z = list1.get(k9).calculateZOffset(this.getEntityBoundingBox(), z);
                    }
                }
                if (z != 0.0) {
                    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, 0.0, z));
                }
            }
            final boolean flag2 = this.onGround || (d8 != y && d8 < 0.0);
            if (this.stepHeight > 0.0f && flag2 && (d7 != x || d9 != z) && !ignoreHorizontal && !ignoreVertical) {
                final double d22 = x;
                final double d23 = y;
                final double d24 = z;
                final AxisAlignedBB axisalignedbb5 = this.getEntityBoundingBox();
                this.setEntityBoundingBox(axisalignedbb);
                y = this.stepHeight;
                final List<AxisAlignedBB> list3 = this.world.getCollisionBoxes(this, this.getEntityBoundingBox().addCoord(d7, y, d9));
                AxisAlignedBB axisalignedbb6 = this.getEntityBoundingBox();
                final AxisAlignedBB axisalignedbb7 = axisalignedbb6.addCoord(d7, 0.0, d9);
                double d25 = y;
                for (int j9 = 0, k10 = list3.size(); j9 < k10; ++j9) {
                    d25 = list3.get(j9).calculateYOffset(axisalignedbb7, d25);
                }
                axisalignedbb6 = axisalignedbb6.offset(0.0, d25, 0.0);
                double d26 = d7;
                for (int l12 = 0, i9 = list3.size(); l12 < i9; ++l12) {
                    d26 = list3.get(l12).calculateXOffset(axisalignedbb6, d26);
                }
                axisalignedbb6 = axisalignedbb6.offset(d26, 0.0, 0.0);
                double d27 = d9;
                for (int j10 = 0, k11 = list3.size(); j10 < k11; ++j10) {
                    d27 = list3.get(j10).calculateZOffset(axisalignedbb6, d27);
                }
                axisalignedbb6 = axisalignedbb6.offset(0.0, 0.0, d27);
                AxisAlignedBB axisalignedbb8 = this.getEntityBoundingBox();
                double d28 = y;
                for (int l13 = 0, i10 = list3.size(); l13 < i10; ++l13) {
                    d28 = list3.get(l13).calculateYOffset(axisalignedbb8, d28);
                }
                axisalignedbb8 = axisalignedbb8.offset(0.0, d28, 0.0);
                double d29 = d7;
                for (int j11 = 0, k12 = list3.size(); j11 < k12; ++j11) {
                    d29 = list3.get(j11).calculateXOffset(axisalignedbb8, d29);
                }
                axisalignedbb8 = axisalignedbb8.offset(d29, 0.0, 0.0);
                double d30 = d9;
                for (int l14 = 0, i11 = list3.size(); l14 < i11; ++l14) {
                    d30 = list3.get(l14).calculateZOffset(axisalignedbb8, d30);
                }
                axisalignedbb8 = axisalignedbb8.offset(0.0, 0.0, d30);
                final double d31 = d26 * d26 + d27 * d27;
                final double d32 = d29 * d29 + d30 * d30;
                if (d31 > d32) {
                    x = d26;
                    z = d27;
                    y = -d25;
                    this.setEntityBoundingBox(axisalignedbb6);
                }
                else {
                    x = d29;
                    z = d30;
                    y = -d28;
                    this.setEntityBoundingBox(axisalignedbb8);
                }
                for (int j12 = 0, k13 = list3.size(); j12 < k13; ++j12) {
                    y = list3.get(j12).calculateYOffset(this.getEntityBoundingBox(), y);
                }
                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
                if (d22 * d22 + d24 * d24 >= x * x + z * z) {
                    x = d22;
                    y = d23;
                    z = d24;
                    this.setEntityBoundingBox(axisalignedbb5);
                }
            }
            this.world.profiler.endSection();
            this.world.profiler.startSection("rest");
            this.resetPositionToBB();
            this.collidedHorizontally = (d7 != x || d9 != z);
            this.collidedVertically = (d8 != y);
            this.onGround = (this.collidedVertically && d8 < 0.0);
            this.collided = (this.collidedHorizontally || this.collidedVertically);
            if (this instanceof EntityPlayerSP) {
                final double deltaX = this.posX - preX;
                final double n = this.posZ - preZ;
            }
            final int j13 = MathHelper.floor(this.posX);
            final int i12 = MathHelper.floor(this.posY - 0.20000000298023224);
            final int k14 = MathHelper.floor(this.posZ);
            BlockPos blockpos = new BlockPos(j13, i12, k14);
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            if (iblockstate.getMaterial() == Material.AIR) {
                final BlockPos blockpos2 = blockpos.down();
                final IBlockState iblockstate2 = this.world.getBlockState(blockpos2);
                final Block block1 = iblockstate2.getBlock();
                if (block1 instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate) {
                    iblockstate = iblockstate2;
                    blockpos = blockpos2;
                }
            }
            this.updateFallState(y, this.onGround, iblockstate, blockpos);
            if (d7 != x) {
                this.motionX = 0.0;
            }
            if (d9 != z) {
                this.motionZ = 0.0;
            }
            final Block block2 = iblockstate.getBlock();
            if (d8 != y) {
                block2.onLanded(this.world, this);
            }
            if (this.canTriggerWalking() && (!this.onGround || !this.isSneaking() || !(this instanceof EntityPlayer)) && !this.isRiding()) {
                final double d33 = this.posX - d4;
                double d34 = this.posY - d5;
                final double d35 = this.posZ - d6;
                if (block2 != Blocks.LADDER) {
                    d34 = 0.0;
                }
                if (block2 != null && this.onGround) {
                    block2.onEntityWalk(this.world, blockpos, this);
                }
                this.distanceWalkedModified += (float)(MathHelper.sqrt(d33 * d33 + d35 * d35) * 0.6);
                this.distanceWalkedOnStepModified += (float)(MathHelper.sqrt(d33 * d33 + d34 * d34 + d35 * d35) * 0.6);
                if (this.distanceWalkedOnStepModified > this.nextStepDistance && iblockstate.getMaterial() != Material.AIR) {
                    this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
                    if (this.isInWater()) {
                        final Entity entity = (this.isBeingRidden() && this.getControllingPassenger() != null) ? this.getControllingPassenger() : this;
                        final float f = (entity == this) ? 0.35f : 0.4f;
                        float f2 = MathHelper.sqrt(entity.motionX * entity.motionX * 0.20000000298023224 + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ * 0.20000000298023224) * f;
                        if (f2 > 1.0f) {
                            f2 = 1.0f;
                        }
                        this.playSound(this.getSwimSound(), f2, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                    }
                    else {
                        this.playStepSound(blockpos, block2);
                    }
                }
                else if (this.distanceWalkedOnStepModified > this.nextFlap && this.makeFlySound() && iblockstate.getMaterial() == Material.AIR) {
                    this.nextFlap = this.playFlySound(this.distanceWalkedOnStepModified);
                }
            }
            try {
                this.doBlockCollisions();
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            final boolean flag3 = this.isWet();
            if (this.world.isFlammableWithin(this.getEntityBoundingBox().contract(0.001))) {
                this.dealFireDamage(1);
                if (!flag3) {
                    ++this.fire;
                    if (this.fire == 0) {
                        this.setFire(8);
                    }
                }
            }
            else if (this.fire <= 0) {
                this.fire = -this.getFireImmuneTicks();
            }
            if (flag3 && this.isBurning()) {
                this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7f, 1.6f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                this.fire = -this.getFireImmuneTicks();
            }
            this.world.profiler.endSection();
        }
    }
    
    public void resetPositionToBB() {
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0;
        this.posY = axisalignedbb.minY;
        this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0;
    }
    
    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_GENERIC_SWIM;
    }
    
    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_GENERIC_SPLASH;
    }
    
    protected void doBlockCollisions() {
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.minX + 0.001, axisalignedbb.minY + 0.001, axisalignedbb.minZ + 0.001);
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.maxX - 0.001, axisalignedbb.maxY - 0.001, axisalignedbb.maxZ - 0.001);
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos3 = BlockPos.PooledMutableBlockPos.retain();
        if (this.world.isAreaLoaded(blockpos$pooledmutableblockpos, blockpos$pooledmutableblockpos2)) {
            for (int i = blockpos$pooledmutableblockpos.getX(); i <= blockpos$pooledmutableblockpos2.getX(); ++i) {
                for (int j = blockpos$pooledmutableblockpos.getY(); j <= blockpos$pooledmutableblockpos2.getY(); ++j) {
                    for (int k = blockpos$pooledmutableblockpos.getZ(); k <= blockpos$pooledmutableblockpos2.getZ(); ++k) {
                        blockpos$pooledmutableblockpos3.setPos(i, j, k);
                        final IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos3);
                        try {
                            iblockstate.getBlock().onEntityCollision(this.world, blockpos$pooledmutableblockpos3, iblockstate, this);
                            this.onInsideBlock(iblockstate);
                        }
                        catch (Throwable throwable) {
                            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
                            CrashReportCategory.addBlockInfo(crashreportcategory, blockpos$pooledmutableblockpos3, iblockstate);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        blockpos$pooledmutableblockpos2.release();
        blockpos$pooledmutableblockpos3.release();
    }
    
    protected void onInsideBlock(final IBlockState p_191955_1_) {
    }
    
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        SoundType soundtype = blockIn.getSoundType();
        if (this.world.getBlockState(pos.up()).getBlock() == Blocks.SNOW_LAYER) {
            soundtype = Blocks.SNOW_LAYER.getSoundType();
            this.playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15f, soundtype.getPitch());
        }
        else if (!blockIn.getDefaultState().getMaterial().isLiquid()) {
            this.playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15f, soundtype.getPitch());
        }
    }
    
    protected float playFlySound(final float p_191954_1_) {
        return 0.0f;
    }
    
    protected boolean makeFlySound() {
        return false;
    }
    
    public void playSound(final SoundEvent soundIn, final float volume, final float pitch) {
        if (!this.isSilent()) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
        }
    }
    
    public boolean isSilent() {
        return this.dataManager.get(Entity.SILENT);
    }
    
    public void setSilent(final boolean isSilent) {
        this.dataManager.set(Entity.SILENT, isSilent);
    }
    
    public boolean hasNoGravity() {
        return this.dataManager.get(Entity.NO_GRAVITY);
    }
    
    public void setNoGravity(final boolean noGravity) {
        this.dataManager.set(Entity.NO_GRAVITY, noGravity);
    }
    
    protected boolean canTriggerWalking() {
        return true;
    }
    
    protected void updateFallState(final double y, final boolean onGroundIn, final IBlockState state, final BlockPos pos) {
        if (onGroundIn) {
            if (this.fallDistance > 0.0f) {
                state.getBlock().onFallenUpon(this.world, pos, this, this.fallDistance);
            }
            this.fallDistance = 0.0f;
        }
        else if (y < 0.0) {
            this.fallDistance -= (float)y;
        }
    }
    
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }
    
    protected void dealFireDamage(final int amount) {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.IN_FIRE, (float)amount);
        }
    }
    
    public final boolean isImmuneToFire() {
        return this.isImmuneToFire;
    }
    
    public void fall(final float distance, final float damageMultiplier) {
        if (this.isBeingRidden()) {
            for (final Entity entity : this.getPassengers()) {
                entity.fall(distance, damageMultiplier);
            }
        }
    }
    
    public boolean isWet() {
        if (this.inWater) {
            return true;
        }
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.posY, this.posZ);
        if (!this.world.isRainingAt(blockpos$pooledmutableblockpos) && !this.world.isRainingAt(blockpos$pooledmutableblockpos.setPos(this.posX, this.posY + this.height, this.posZ))) {
            blockpos$pooledmutableblockpos.release();
            return false;
        }
        blockpos$pooledmutableblockpos.release();
        return true;
    }
    
    public boolean isInWater() {
        return this.inWater;
    }
    
    public boolean isOverWater() {
        return this.world.handleMaterialAcceleration(this.getEntityBoundingBox().grow(0.0, -20.0, 0.0).shrink(0.001), Material.WATER, this);
    }
    
    public boolean handleWaterMovement() {
        if (this.getRidingEntity() instanceof EntityBoat) {
            this.inWater = false;
        }
        else if (this.world.handleMaterialAcceleration(this.getEntityBoundingBox().grow(0.0, -0.4000000059604645, 0.0).shrink(0.001), Material.WATER, this)) {
            if (!this.inWater && !this.firstUpdate) {
                this.doWaterSplashEffect();
            }
            this.fallDistance = 0.0f;
            this.inWater = true;
            this.extinguish();
        }
        else {
            this.inWater = false;
        }
        return this.inWater;
    }
    
    protected void doWaterSplashEffect() {
        final Entity entity = (this.isBeingRidden() && this.getControllingPassenger() != null) ? this.getControllingPassenger() : this;
        final float f = (entity == this) ? 0.2f : 0.9f;
        float f2 = MathHelper.sqrt(entity.motionX * entity.motionX * 0.20000000298023224 + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ * 0.20000000298023224) * f;
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        this.playSound(this.getSplashSound(), f2, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
        final float f3 = (float)MathHelper.floor(this.getEntityBoundingBox().minY);
        for (int i = 0; i < 1.0f + this.width * 20.0f; ++i) {
            final float f4 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            final float f5 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f4, f3 + 1.0f, this.posZ + f5, this.motionX, this.motionY - this.rand.nextFloat() * 0.2f, this.motionZ, new int[0]);
        }
        for (int j = 0; j < 1.0f + this.width * 20.0f; ++j) {
            final float f6 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            final float f7 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f6, f3 + 1.0f, this.posZ + f7, this.motionX, this.motionY, this.motionZ, new int[0]);
        }
    }
    
    public void spawnRunningParticles() {
        if (this.isSprinting() && !this.isInWater()) {
            this.createRunningParticles();
        }
    }
    
    protected void createRunningParticles() {
        final int i = MathHelper.floor(this.posX);
        final int j = MathHelper.floor(this.posY - 0.20000000298023224);
        final int k = MathHelper.floor(this.posZ);
        final BlockPos blockpos = new BlockPos(i, j, k);
        final IBlockState iblockstate = this.world.getBlockState(blockpos);
        if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5) * this.width, this.getEntityBoundingBox().minY + 0.1, this.posZ + (this.rand.nextFloat() - 0.5) * this.width, -this.motionX * 4.0, 1.5, -this.motionZ * 4.0, Block.getStateId(iblockstate));
        }
    }
    
    public boolean isInsideOfMaterial(final Material materialIn) {
        if (this.getRidingEntity() instanceof EntityBoat) {
            return false;
        }
        final double d0 = this.posY + this.getEyeHeight();
        final BlockPos blockpos = new BlockPos(this.posX, d0, this.posZ);
        final IBlockState iblockstate = this.world.getBlockState(blockpos);
        if (iblockstate.getMaterial() == materialIn) {
            final float f = BlockLiquid.getLiquidHeightPercent(iblockstate.getBlock().getMetaFromState(iblockstate)) - 0.11111111f;
            final float f2 = blockpos.getY() + 1 - f;
            final boolean flag = d0 < f2;
            return (flag || !(this instanceof EntityPlayer)) && flag;
        }
        return false;
    }
    
    public boolean isInLava() {
        return this.world.isMaterialInBB(this.getEntityBoundingBox().grow(-0.10000000149011612, -0.4000000059604645, -0.10000000149011612), Material.LAVA);
    }
    
    public void moveRelative(float strafe, float up, float forward, final float friction) {
        float f = strafe * strafe + up * up + forward * forward;
        if (f >= 1.0E-4f) {
            f = MathHelper.sqrt(f);
            if (f < 1.0f) {
                f = 1.0f;
            }
            f = friction / f;
            strafe *= f;
            up *= f;
            forward *= f;
            final float f2 = MathHelper.sin(this.rotationYaw * 0.017453292f);
            final float f3 = MathHelper.cos(this.rotationYaw * 0.017453292f);
            this.motionX += strafe * f3 - forward * f2;
            this.motionY += up;
            this.motionZ += forward * f3 + strafe * f2;
        }
    }
    
    public int getBrightnessForRender() {
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));
        if (this.world.isBlockLoaded(blockpos$mutableblockpos)) {
            blockpos$mutableblockpos.setY(MathHelper.floor(this.posY + this.getEyeHeight()));
            return this.world.getCombinedLight(blockpos$mutableblockpos, 0);
        }
        return 0;
    }
    
    public float getBrightness() {
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));
        if (this.world.isBlockLoaded(blockpos$mutableblockpos)) {
            blockpos$mutableblockpos.setY(MathHelper.floor(this.posY + this.getEyeHeight()));
            return this.world.getLightBrightness(blockpos$mutableblockpos);
        }
        return 0.0f;
    }
    
    public void setWorld(final World worldIn) {
        this.world = worldIn;
    }
    
    public void setPositionAndRotation(final double x, final double y, final double z, final float yaw, float pitch) {
        this.posX = MathHelper.clamp(x, -3.0E7, 3.0E7);
        this.posY = y;
        this.posZ = MathHelper.clamp(z, -3.0E7, 3.0E7);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        final double d0 = this.prevRotationYaw - yaw;
        if (d0 < -180.0) {
            this.prevRotationYaw += 360.0f;
        }
        if (d0 >= 180.0) {
            this.prevRotationYaw -= 360.0f;
        }
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(yaw, pitch);
    }
    
    public void moveToBlockPosAndAngles(final BlockPos pos, final float rotationYawIn, final float rotationPitchIn) {
        this.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, rotationYawIn, rotationPitchIn);
    }
    
    public void setLocationAndAngles(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
        this.setPosition(this.posX, this.posY, this.posZ);
    }
    
    public float getDistance(final Entity entityIn) {
        final float f = (float)(this.posX - entityIn.posX);
        final float f2 = (float)(this.posY - entityIn.posY);
        final float f3 = (float)(this.posZ - entityIn.posZ);
        return MathHelper.sqrt(f * f + f2 * f2 + f3 * f3);
    }
    
    public double getDistanceSq(final double x, final double y, final double z) {
        final double d0 = this.posX - x;
        final double d2 = this.posY - y;
        final double d3 = this.posZ - z;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public double getDistanceSq(final BlockPos pos) {
        return pos.distanceSq(this.posX, this.posY, this.posZ);
    }
    
    public double getDistanceSqToCenter(final BlockPos pos) {
        return pos.distanceSqToCenter(this.posX, this.posY, this.posZ);
    }
    
    public double getDistance(final double x, final double y, final double z) {
        final double d0 = this.posX - x;
        final double d2 = this.posY - y;
        final double d3 = this.posZ - z;
        return MathHelper.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
    }
    
    public double getDistanceSq(final Entity entityIn) {
        final double d0 = this.posX - entityIn.posX;
        final double d2 = this.posY - entityIn.posY;
        final double d3 = this.posZ - entityIn.posZ;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
    }
    
    public void applyEntityCollision(final Entity entityIn) {
        if (!this.isRidingSameEntity(entityIn) && !entityIn.noClip && !this.noClip) {
            double d0 = entityIn.posX - this.posX;
            double d2 = entityIn.posZ - this.posZ;
            double d3 = MathHelper.absMax(d0, d2);
            if (d3 >= 0.009999999776482582) {
                d3 = MathHelper.sqrt(d3);
                d0 /= d3;
                d2 /= d3;
                double d4 = 1.0 / d3;
                if (d4 > 1.0) {
                    d4 = 1.0;
                }
                d0 *= d4;
                d2 *= d4;
                d0 *= 0.05000000074505806;
                d2 *= 0.05000000074505806;
                d0 *= 1.0f - this.entityCollisionReduction;
                d2 *= 1.0f - this.entityCollisionReduction;
                if (!this.isBeingRidden()) {
                    this.addVelocity(-d0, 0.0, -d2);
                }
                if (!entityIn.isBeingRidden()) {
                    entityIn.addVelocity(d0, 0.0, d2);
                }
            }
        }
    }
    
    public void addVelocity(final double x, final double y, final double z) {
        this.motionX += x;
        this.motionY += y;
        this.motionZ += z;
        this.isAirBorne = true;
    }
    
    protected void markVelocityChanged() {
        this.velocityChanged = true;
    }
    
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.markVelocityChanged();
        return false;
    }
    
    public Vec3d getLook(final float partialTicks) {
        if (partialTicks == 1.0f) {
            return getVectorForRotation(this.rotationPitch, this.rotationYaw);
        }
        final float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
        final float f2 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
        return getVectorForRotation(f, f2);
    }
    
    public static final Vec3d getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3d(f2 * f3, f4, f * f3);
    }
    
    public Vec3d getPositionEyes(final float partialTicks) {
        if (partialTicks == 1.0f) {
            return new Vec3d(this.posX, this.posY + this.getEyeHeight(), this.posZ);
        }
        final double d0 = this.prevPosX + (this.posX - this.prevPosX) * partialTicks;
        final double d2 = this.prevPosY + (this.posY - this.prevPosY) * partialTicks + this.getEyeHeight();
        final double d3 = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks;
        return new Vec3d(d0, d2, d3);
    }
    
    @Nullable
    public RayTraceResult rayTrace(final double blockReachDistance, final float partialTicks) {
        final Vec3d vec3d = this.getPositionEyes(partialTicks);
        final Vec3d vec3d2 = this.getLook(partialTicks);
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * blockReachDistance, vec3d2.y * blockReachDistance, vec3d2.z * blockReachDistance);
        return this.world.rayTraceBlocks(vec3d, vec3d3, false, false, true);
    }
    
    public boolean canBeCollidedWith() {
        return false;
    }
    
    public boolean canBePushed() {
        return false;
    }
    
    public void awardKillScore(final Entity p_191956_1_, final int p_191956_2_, final DamageSource p_191956_3_) {
        if (p_191956_1_ instanceof EntityPlayerMP) {
            CriteriaTriggers.ENTITY_KILLED_PLAYER.trigger((EntityPlayerMP)p_191956_1_, this, p_191956_3_);
        }
    }
    
    public boolean isInRangeToRender3d(final double x, final double y, final double z) {
        final double d0 = this.posX - x;
        final double d2 = this.posY - y;
        final double d3 = this.posZ - z;
        final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
        return this.isInRangeToRenderDist(d4);
    }
    
    public boolean isInRangeToRenderDist(final double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength();
        if (Double.isNaN(d0)) {
            d0 = 1.0;
        }
        d0 = d0 * 64.0 * Entity.renderDistanceWeight;
        return distance < d0 * d0;
    }
    
    public boolean writeToNBTAtomically(final NBTTagCompound compound) {
        final String s = this.getEntityString();
        if (!this.isDead && s != null) {
            compound.setString("id", s);
            this.writeToNBT(compound);
            return true;
        }
        return false;
    }
    
    public boolean writeToNBTOptional(final NBTTagCompound compound) {
        final String s = this.getEntityString();
        if (!this.isDead && s != null && !this.isRiding()) {
            compound.setString("id", s);
            this.writeToNBT(compound);
            return true;
        }
        return false;
    }
    
    public static void registerFixes(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.ENTITY, new IDataWalker() {
            @Override
            public NBTTagCompound process(final IDataFixer fixer, final NBTTagCompound compound, final int versionIn) {
                if (compound.hasKey("Passengers", 9)) {
                    final NBTTagList nbttaglist = compound.getTagList("Passengers", 10);
                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        nbttaglist.set(i, fixer.process(FixTypes.ENTITY, nbttaglist.getCompoundTagAt(i), versionIn));
                    }
                }
                return compound;
            }
        });
    }
    
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        try {
            compound.setTag("Pos", this.newDoubleNBTList(this.posX, this.posY, this.posZ));
            compound.setTag("Motion", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
            compound.setTag("Rotation", this.newFloatNBTList(this.rotationYaw, this.rotationPitch));
            compound.setFloat("FallDistance", this.fallDistance);
            compound.setShort("Fire", (short)this.fire);
            compound.setShort("Air", (short)this.getAir());
            compound.setBoolean("OnGround", this.onGround);
            compound.setInteger("Dimension", this.dimension);
            compound.setBoolean("Invulnerable", this.invulnerable);
            compound.setInteger("PortalCooldown", this.timeUntilPortal);
            compound.setUniqueId("UUID", this.getUniqueID());
            if (this.hasCustomName()) {
                compound.setString("CustomName", this.getCustomNameTag());
            }
            if (this.getAlwaysRenderNameTag()) {
                compound.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
            }
            this.cmdResultStats.writeStatsToNBT(compound);
            if (this.isSilent()) {
                compound.setBoolean("Silent", this.isSilent());
            }
            if (this.hasNoGravity()) {
                compound.setBoolean("NoGravity", this.hasNoGravity());
            }
            if (this.glowing) {
                compound.setBoolean("Glowing", this.glowing);
            }
            if (!this.tags.isEmpty()) {
                final NBTTagList nbttaglist = new NBTTagList();
                for (final String s : this.tags) {
                    nbttaglist.appendTag(new NBTTagString(s));
                }
                compound.setTag("Tags", nbttaglist);
            }
            this.writeEntityToNBT(compound);
            if (this.isBeingRidden()) {
                final NBTTagList nbttaglist2 = new NBTTagList();
                for (final Entity entity : this.getPassengers()) {
                    final NBTTagCompound nbttagcompound = new NBTTagCompound();
                    if (entity.writeToNBTAtomically(nbttagcompound)) {
                        nbttaglist2.appendTag(nbttagcompound);
                    }
                }
                if (!nbttaglist2.isEmpty()) {
                    compound.setTag("Passengers", nbttaglist2);
                }
            }
            return compound;
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }
    
    public void readFromNBT(final NBTTagCompound compound) {
        try {
            final NBTTagList nbttaglist = compound.getTagList("Pos", 6);
            final NBTTagList nbttaglist2 = compound.getTagList("Motion", 6);
            final NBTTagList nbttaglist3 = compound.getTagList("Rotation", 5);
            this.motionX = nbttaglist2.getDoubleAt(0);
            this.motionY = nbttaglist2.getDoubleAt(1);
            this.motionZ = nbttaglist2.getDoubleAt(2);
            if (Math.abs(this.motionX) > 10.0) {
                this.motionX = 0.0;
            }
            if (Math.abs(this.motionY) > 10.0) {
                this.motionY = 0.0;
            }
            if (Math.abs(this.motionZ) > 10.0) {
                this.motionZ = 0.0;
            }
            this.posX = nbttaglist.getDoubleAt(0);
            this.posY = nbttaglist.getDoubleAt(1);
            this.posZ = nbttaglist.getDoubleAt(2);
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.rotationYaw = nbttaglist3.getFloatAt(0);
            this.rotationPitch = nbttaglist3.getFloatAt(1);
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
            this.setRotationYawHead(this.rotationYaw);
            this.setRenderYawOffset(this.rotationYaw);
            this.fallDistance = compound.getFloat("FallDistance");
            this.fire = compound.getShort("Fire");
            this.setAir(compound.getShort("Air"));
            this.onGround = compound.getBoolean("OnGround");
            if (compound.hasKey("Dimension")) {
                this.dimension = compound.getInteger("Dimension");
            }
            this.invulnerable = compound.getBoolean("Invulnerable");
            this.timeUntilPortal = compound.getInteger("PortalCooldown");
            if (compound.hasUniqueId("UUID")) {
                this.entityUniqueID = compound.getUniqueId("UUID");
                this.cachedUniqueIdString = this.entityUniqueID.toString();
            }
            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
            if (compound.hasKey("CustomName", 8)) {
                this.setCustomNameTag(compound.getString("CustomName"));
            }
            this.setAlwaysRenderNameTag(compound.getBoolean("CustomNameVisible"));
            this.cmdResultStats.readStatsFromNBT(compound);
            this.setSilent(compound.getBoolean("Silent"));
            this.setNoGravity(compound.getBoolean("NoGravity"));
            this.setGlowing(compound.getBoolean("Glowing"));
            if (compound.hasKey("Tags", 9)) {
                this.tags.clear();
                final NBTTagList nbttaglist4 = compound.getTagList("Tags", 8);
                for (int i = Math.min(nbttaglist4.tagCount(), 1024), j = 0; j < i; ++j) {
                    this.tags.add(nbttaglist4.getStringTagAt(j));
                }
            }
            this.readEntityFromNBT(compound);
            if (this.shouldSetPosAfterLoading()) {
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }
    
    protected boolean shouldSetPosAfterLoading() {
        return true;
    }
    
    @Nullable
    protected final String getEntityString() {
        final ResourceLocation resourcelocation = EntityList.getKey(this);
        return (resourcelocation == null) ? null : resourcelocation.toString();
    }
    
    protected abstract void readEntityFromNBT(final NBTTagCompound p0);
    
    protected abstract void writeEntityToNBT(final NBTTagCompound p0);
    
    protected NBTTagList newDoubleNBTList(final double... numbers) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final double d0 : numbers) {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }
        return nbttaglist;
    }
    
    protected NBTTagList newFloatNBTList(final float... numbers) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final float f : numbers) {
            nbttaglist.appendTag(new NBTTagFloat(f));
        }
        return nbttaglist;
    }
    
    @Nullable
    public EntityItem dropItem(final Item itemIn, final int size) {
        return this.dropItemWithOffset(itemIn, size, 0.0f);
    }
    
    @Nullable
    public EntityItem dropItemWithOffset(final Item itemIn, final int size, final float offsetY) {
        return this.entityDropItem(new ItemStack(itemIn, size, 0), offsetY);
    }
    
    @Nullable
    public EntityItem entityDropItem(final ItemStack stack, final float offsetY) {
        if (stack.isEmpty()) {
            return null;
        }
        final EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + offsetY, this.posZ, stack);
        entityitem.setDefaultPickupDelay();
        this.world.spawnEntity(entityitem);
        return entityitem;
    }
    
    public boolean isEntityAlive() {
        return !this.isDead;
    }
    
    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip) {
            return false;
        }
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        for (int i = 0; i < 8; ++i) {
            final int j = MathHelper.floor(this.posY + ((i >> 0) % 2 - 0.5f) * 0.1f + this.getEyeHeight());
            final int k = MathHelper.floor(this.posX + ((i >> 1) % 2 - 0.5f) * this.width * 0.8f);
            final int l = MathHelper.floor(this.posZ + ((i >> 2) % 2 - 0.5f) * this.width * 0.8f);
            if (blockpos$pooledmutableblockpos.getX() != k || blockpos$pooledmutableblockpos.getY() != j || blockpos$pooledmutableblockpos.getZ() != l) {
                blockpos$pooledmutableblockpos.setPos(k, j, l);
                if (this.world.getBlockState(blockpos$pooledmutableblockpos).causesSuffocation()) {
                    blockpos$pooledmutableblockpos.release();
                    return true;
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        return false;
    }
    
    public boolean processInitialInteract(final EntityPlayer player, final EnumHand hand) {
        return false;
    }
    
    @Nullable
    public AxisAlignedBB getCollisionBox(final Entity entityIn) {
        return null;
    }
    
    public void updateRidden() {
        final Entity entity = this.getRidingEntity();
        if (this.isRiding() && entity.isDead) {
            this.dismountRidingEntity();
        }
        else {
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
            this.onUpdate();
            if (this.isRiding()) {
                entity.updatePassenger(this);
            }
        }
    }
    
    public void updatePassenger(final Entity passenger) {
        if (this.isPassenger(passenger)) {
            passenger.setPosition(this.posX, this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ);
        }
    }
    
    public void applyOrientationToEntity(final Entity entityToUpdate) {
    }
    
    public double getYOffset() {
        return 0.0;
    }
    
    public double getMountedYOffset() {
        return this.height * 0.75;
    }
    
    public boolean startRiding(final Entity entityIn) {
        return this.startRiding(entityIn, false);
    }
    
    public boolean startRiding(final Entity entityIn, final boolean force) {
        for (Entity entity = entityIn; entity.ridingEntity != null; entity = entity.ridingEntity) {
            if (entity.ridingEntity == this) {
                return false;
            }
        }
        if (force || (this.canBeRidden(entityIn) && entityIn.canFitPassenger(this))) {
            if (this.isRiding()) {
                this.dismountRidingEntity();
            }
            (this.ridingEntity = entityIn).addPassenger(this);
            return true;
        }
        return false;
    }
    
    protected boolean canBeRidden(final Entity entityIn) {
        return this.rideCooldown <= 0;
    }
    
    public void removePassengers() {
        for (int i = this.riddenByEntities.size() - 1; i >= 0; --i) {
            this.riddenByEntities.get(i).dismountRidingEntity();
        }
    }
    
    public void dismountRidingEntity() {
        if (this.ridingEntity != null) {
            final Entity entity = this.ridingEntity;
            this.ridingEntity = null;
            entity.removePassenger(this);
        }
    }
    
    protected void addPassenger(final Entity passenger) {
        if (passenger.getRidingEntity() != this) {
            throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
        }
        if (!this.world.isRemote && passenger instanceof EntityPlayer && !(this.getControllingPassenger() instanceof EntityPlayer)) {
            this.riddenByEntities.add(0, passenger);
        }
        else {
            this.riddenByEntities.add(passenger);
        }
    }
    
    protected void removePassenger(final Entity passenger) {
        if (passenger.getRidingEntity() == this) {
            throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)");
        }
        this.riddenByEntities.remove(passenger);
        passenger.rideCooldown = 60;
    }
    
    protected boolean canFitPassenger(final Entity passenger) {
        return this.getPassengers().size() < 1;
    }
    
    public void setPositionAndRotationDirect(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean teleport) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }
    
    public float getCollisionBorderSize() {
        final EventEntityBoundingBox eventEntityBoundingBox = new EventEntityBoundingBox();
        EventManager.call(eventEntityBoundingBox);
        return Minced.getInstance().manager.getModule(HitBox.class).state ? eventEntityBoundingBox.getSize() : 0.0f;
    }
    
    public Vec3d getLookVec() {
        return getVectorForRotation(this.rotationPitch, this.rotationYaw);
    }
    
    public Vec2f getPitchYaw() {
        return new Vec2f(this.rotationPitch, this.rotationYaw);
    }
    
    public Vec3d getForward() {
        return Vec3d.fromPitchYaw(this.getPitchYaw());
    }
    
    public void setSizeAdvanced(final float width, final float height) {
        if (width != this.width || height != this.height) {
            final float f = this.width;
            this.width = width;
            this.height = height;
            final double d0 = width / 2.0;
            this.setEntityBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d0, this.posX + d0, this.posY + this.height, this.posZ + d0));
        }
    }
    
    public void setPortal(final BlockPos pos) {
        if (this.timeUntilPortal > 0) {
            this.timeUntilPortal = this.getPortalCooldown();
        }
        else {
            if (!this.world.isRemote && !pos.equals(this.lastPortalPos)) {
                this.lastPortalPos = new BlockPos(pos);
                final BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.PORTAL.createPatternHelper(this.world, this.lastPortalPos);
                final double d0 = (blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X) ? blockpattern$patternhelper.getFrontTopLeft().getZ() : ((double)blockpattern$patternhelper.getFrontTopLeft().getX());
                double d2 = (blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X) ? this.posZ : this.posX;
                d2 = Math.abs(MathHelper.pct(d2 - (double)((blockpattern$patternhelper.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) ? 1 : 0), d0, d0 - blockpattern$patternhelper.getWidth()));
                final double d3 = MathHelper.pct(this.posY - 1.0, blockpattern$patternhelper.getFrontTopLeft().getY(), blockpattern$patternhelper.getFrontTopLeft().getY() - blockpattern$patternhelper.getHeight());
                this.lastPortalVec = new Vec3d(d2, d3, 0.0);
                this.teleportDirection = blockpattern$patternhelper.getForwards();
            }
            this.inPortal = true;
        }
    }
    
    public int getPortalCooldown() {
        return 300;
    }
    
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }
    
    public void handleStatusUpdate(final byte id) {
    }
    
    public void performHurtAnimation() {
    }
    
    public Iterable<ItemStack> getHeldEquipment() {
        return Entity.EMPTY_EQUIPMENT;
    }
    
    public Iterable<ItemStack> getArmorInventoryList() {
        return Entity.EMPTY_EQUIPMENT;
    }
    
    public Iterable<ItemStack> getEquipmentAndArmor() {
        return (Iterable<ItemStack>)Iterables.concat((Iterable)this.getHeldEquipment(), (Iterable)this.getArmorInventoryList());
    }
    
    public void setItemStackToSlot(final EntityEquipmentSlot slotIn, final ItemStack stack) {
    }
    
    public boolean isBurning() {
        final boolean flag = this.world != null && this.world.isRemote;
        return !this.isImmuneToFire && (this.fire > 0 || (flag && this.getFlag(0)));
    }
    
    public boolean isRiding() {
        return this.getRidingEntity() != null;
    }
    
    public boolean isBeingRidden() {
        return !this.getPassengers().isEmpty();
    }
    
    public boolean isSneaking() {
        return this.getFlag(1);
    }
    
    public void setSneaking(final boolean sneaking) {
        this.setFlag(1, sneaking);
    }
    
    public boolean isSprinting() {
        return this.getFlag(3);
    }
    
    public void setSprinting(final boolean sprinting) {
        this.setFlag(3, sprinting);
    }
    
    public boolean isGlowing() {
        return this.glowing || (this.world.isRemote && this.getFlag(6));
    }
    
    public void setGlowing(final boolean glowingIn) {
        this.glowing = glowingIn;
        if (!this.world.isRemote) {
            this.setFlag(6, this.glowing);
        }
    }
    
    public boolean isInvisible() {
        return this.getFlag(5);
    }
    
    public boolean isInvisibleToPlayer(final EntityPlayer player) {
        if (player.isSpectator()) {
            return false;
        }
        final Team team = this.getTeam();
        return (team == null || player == null || player.getTeam() != team || !team.getSeeFriendlyInvisiblesEnabled()) && this.isInvisible();
    }
    
    @Nullable
    public Team getTeam() {
        return this.world.getScoreboard().getPlayersTeam(this.getCachedUniqueIdString());
    }
    
    public boolean isOnSameTeam(final Entity entityIn) {
        return this.isOnScoreboardTeam(entityIn.getTeam());
    }
    
    public boolean isOnScoreboardTeam(final Team teamIn) {
        return this.getTeam() != null && this.getTeam().isSameTeam(teamIn);
    }
    
    public void setInvisible(final boolean invisible) {
        this.setFlag(5, invisible);
    }
    
    protected boolean getFlag(final int flag) {
        return (this.dataManager.get(Entity.FLAGS) & 1 << flag) != 0x0;
    }
    
    protected void setFlag(final int flag, final boolean set) {
        final byte b0 = this.dataManager.get(Entity.FLAGS);
        if (set) {
            this.dataManager.set(Entity.FLAGS, (byte)(b0 | 1 << flag));
        }
        else {
            this.dataManager.set(Entity.FLAGS, (byte)(b0 & ~(1 << flag)));
        }
    }
    
    public int getAir() {
        return this.dataManager.get(Entity.AIR);
    }
    
    public void setAir(final int air) {
        this.dataManager.set(Entity.AIR, air);
    }
    
    public void onStruckByLightning(final EntityLightningBolt lightningBolt) {
        this.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 5.0f);
        ++this.fire;
        if (this.fire == 0) {
            this.setFire(8);
        }
    }
    
    public void onKillEntity(final EntityLivingBase entityLivingIn) {
    }
    
    protected boolean pushOutOfBlocks(final double x, final double y, final double z) {
        final BlockPos blockpos = new BlockPos(x, y, z);
        final double d0 = x - blockpos.getX();
        final double d2 = y - blockpos.getY();
        final double d3 = z - blockpos.getZ();
        if (!this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
            return false;
        }
        EnumFacing enumfacing = EnumFacing.UP;
        double d4 = Double.MAX_VALUE;
        if (!this.world.isBlockFullCube(blockpos.west()) && d0 < d4) {
            d4 = d0;
            enumfacing = EnumFacing.WEST;
        }
        if (!this.world.isBlockFullCube(blockpos.east()) && 1.0 - d0 < d4) {
            d4 = 1.0 - d0;
            enumfacing = EnumFacing.EAST;
        }
        if (!this.world.isBlockFullCube(blockpos.north()) && d3 < d4) {
            d4 = d3;
            enumfacing = EnumFacing.NORTH;
        }
        if (!this.world.isBlockFullCube(blockpos.south()) && 1.0 - d3 < d4) {
            d4 = 1.0 - d3;
            enumfacing = EnumFacing.SOUTH;
        }
        if (!this.world.isBlockFullCube(blockpos.up()) && 1.0 - d2 < d4) {
            d4 = 1.0 - d2;
            enumfacing = EnumFacing.UP;
        }
        final float f = this.rand.nextFloat() * 0.2f + 0.1f;
        final float f2 = (float)enumfacing.getAxisDirection().getOffset();
        if (enumfacing.getAxis() == EnumFacing.Axis.X) {
            this.motionX = f2 * f;
            this.motionY *= 0.75;
            this.motionZ *= 0.75;
        }
        else if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            this.motionX *= 0.75;
            this.motionY = f2 * f;
            this.motionZ *= 0.75;
        }
        else if (enumfacing.getAxis() == EnumFacing.Axis.Z) {
            this.motionX *= 0.75;
            this.motionY *= 0.75;
            this.motionZ = f2 * f;
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
        String s = EntityList.getEntityString(this);
        if (s == null) {
            s = "generic";
        }
        return I18n.translateToLocal("entity." + s + ".name");
    }
    
    @Nullable
    public Entity[] getParts() {
        return null;
    }
    
    public boolean isEntityEqual(final Entity entityIn) {
        return this == entityIn;
    }
    
    public float getRotationYawHead() {
        return 0.0f;
    }
    
    public void setRotationYawHead(final float rotation) {
    }
    
    public void setRenderYawOffset(final float offset) {
    }
    
    public boolean canBeAttackedWithItem() {
        return true;
    }
    
    public boolean hitByEntity(final Entity entityIn) {
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", this.getClass().getSimpleName(), this.getName(), this.entityId, (this.world == null) ? "~NULL~" : this.world.getWorldInfo().getWorldName(), this.posX, this.posY, this.posZ);
    }
    
    public boolean isEntityInvulnerable(final DamageSource source) {
        return this.invulnerable && source != DamageSource.OUT_OF_WORLD && !source.isCreativePlayer();
    }
    
    public boolean getIsInvulnerable() {
        return this.invulnerable;
    }
    
    public void setEntityInvulnerable(final boolean isInvulnerable) {
        this.invulnerable = isInvulnerable;
    }
    
    public void copyLocationAndAnglesFrom(final Entity entityIn) {
        this.setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
    }
    
    private void copyDataFromOld(final Entity entityIn) {
        final NBTTagCompound nbttagcompound = entityIn.writeToNBT(new NBTTagCompound());
        nbttagcompound.removeTag("Dimension");
        this.readFromNBT(nbttagcompound);
        this.timeUntilPortal = entityIn.timeUntilPortal;
        this.lastPortalPos = entityIn.lastPortalPos;
        this.lastPortalVec = entityIn.lastPortalVec;
        this.teleportDirection = entityIn.teleportDirection;
    }
    
    @Nullable
    public Entity changeDimension(final int dimensionIn) {
        if (!this.world.isRemote && !this.isDead) {
            this.world.profiler.startSection("changeDimension");
            final MinecraftServer minecraftserver = this.getServer();
            final int i = this.dimension;
            final WorldServer worldserver = minecraftserver.getWorld(i);
            WorldServer worldserver2 = minecraftserver.getWorld(dimensionIn);
            this.dimension = dimensionIn;
            if (i == 1 && dimensionIn == 1) {
                worldserver2 = minecraftserver.getWorld(0);
                this.dimension = 0;
            }
            this.world.removeEntity(this);
            this.isDead = false;
            this.world.profiler.startSection("reposition");
            BlockPos blockpos;
            if (dimensionIn == 1) {
                blockpos = worldserver2.getSpawnCoordinate();
            }
            else {
                double d0 = this.posX;
                double d2 = this.posZ;
                final double d3 = 8.0;
                if (dimensionIn == -1) {
                    d0 = MathHelper.clamp(d0 / 8.0, worldserver2.getWorldBorder().minX() + 16.0, worldserver2.getWorldBorder().maxX() - 16.0);
                    d2 = MathHelper.clamp(d2 / 8.0, worldserver2.getWorldBorder().minZ() + 16.0, worldserver2.getWorldBorder().maxZ() - 16.0);
                }
                else if (dimensionIn == 0) {
                    d0 = MathHelper.clamp(d0 * 8.0, worldserver2.getWorldBorder().minX() + 16.0, worldserver2.getWorldBorder().maxX() - 16.0);
                    d2 = MathHelper.clamp(d2 * 8.0, worldserver2.getWorldBorder().minZ() + 16.0, worldserver2.getWorldBorder().maxZ() - 16.0);
                }
                d0 = MathHelper.clamp((int)d0, -29999872, 29999872);
                d2 = MathHelper.clamp((int)d2, -29999872, 29999872);
                final float f = this.rotationYaw;
                this.setLocationAndAngles(d0, this.posY, d2, 90.0f, 0.0f);
                final Teleporter teleporter = worldserver2.getDefaultTeleporter();
                teleporter.placeInExistingPortal(this, f);
                blockpos = new BlockPos(this);
            }
            worldserver.updateEntityWithOptionalForce(this, false);
            this.world.profiler.endStartSection("reloading");
            final Entity entity = EntityList.newEntity(this.getClass(), worldserver2);
            if (entity != null) {
                entity.copyDataFromOld(this);
                if (i == 1 && dimensionIn == 1) {
                    final BlockPos blockpos2 = worldserver2.getTopSolidOrLiquidBlock(worldserver2.getSpawnPoint());
                    entity.moveToBlockPosAndAngles(blockpos2, entity.rotationYaw, entity.rotationPitch);
                }
                else {
                    entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);
                }
                final boolean flag = entity.forceSpawn;
                entity.forceSpawn = true;
                worldserver2.spawnEntity(entity);
                entity.forceSpawn = flag;
                worldserver2.updateEntityWithOptionalForce(entity, false);
            }
            this.isDead = true;
            this.world.profiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver2.resetUpdateEntityTick();
            this.world.profiler.endSection();
            return entity;
        }
        return null;
    }
    
    public boolean isNonBoss() {
        return true;
    }
    
    public float getExplosionResistance(final Explosion explosionIn, final World worldIn, final BlockPos pos, final IBlockState blockStateIn) {
        return blockStateIn.getBlock().getExplosionResistance(this);
    }
    
    public boolean canExplosionDestroyBlock(final Explosion explosionIn, final World worldIn, final BlockPos pos, final IBlockState blockStateIn, final float p_174816_5_) {
        return true;
    }
    
    public int getMaxFallHeight() {
        return 3;
    }
    
    public Vec3d getLastPortalVec() {
        return this.lastPortalVec;
    }
    
    public EnumFacing getTeleportDirection() {
        return this.teleportDirection;
    }
    
    public boolean doesEntityNotTriggerPressurePlate() {
        return false;
    }
    
    public void addEntityCrashInfo(final CrashReportCategory category) {
        category.addDetail("Entity Type", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return EntityList.getKey(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
            }
        });
        category.addCrashSection("Entity ID", this.entityId);
        category.addDetail("Entity Name", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return Entity.this.getName();
            }
        });
        category.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", this.posX, this.posY, this.posZ));
        category.addCrashSection("Entity's Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)));
        category.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", this.motionX, this.motionY, this.motionZ));
        category.addDetail("Entity's Passengers", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return Entity.this.getPassengers().toString();
            }
        });
        category.addDetail("Entity's Vehicle", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return Entity.this.getRidingEntity().toString();
            }
        });
    }
    
    public boolean canRenderOnFire() {
        return this.isBurning();
    }
    
    public void setUniqueId(final UUID uniqueIdIn) {
        this.entityUniqueID = uniqueIdIn;
        this.cachedUniqueIdString = this.entityUniqueID.toString();
    }
    
    public UUID getUniqueID() {
        return this.entityUniqueID;
    }
    
    public String getCachedUniqueIdString() {
        return this.cachedUniqueIdString;
    }
    
    public boolean isPushedByWater() {
        return true;
    }
    
    public static double getRenderDistanceWeight() {
        return Entity.renderDistanceWeight;
    }
    
    public static void setRenderDistanceWeight(final double renderDistWeight) {
        Entity.renderDistanceWeight = renderDistWeight;
    }
    
    @Override
    public ITextComponent getDisplayName() {
        final TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
        textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
        textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
        return textcomponentstring;
    }
    
    public void setCustomNameTag(final String name) {
        this.dataManager.set(Entity.CUSTOM_NAME, name);
    }
    
    public String getCustomNameTag() {
        return this.dataManager.get(Entity.CUSTOM_NAME);
    }
    
    public boolean hasCustomName() {
        return !this.dataManager.get(Entity.CUSTOM_NAME).isEmpty();
    }
    
    public void setAlwaysRenderNameTag(final boolean alwaysRenderNameTag) {
        this.dataManager.set(Entity.CUSTOM_NAME_VISIBLE, alwaysRenderNameTag);
    }
    
    public boolean getAlwaysRenderNameTag() {
        return this.dataManager.get(Entity.CUSTOM_NAME_VISIBLE);
    }
    
    public void setPositionAndUpdate(final double x, final double y, final double z) {
        this.isPositionDirty = true;
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.world.updateEntityWithOptionalForce(this, false);
    }
    
    public boolean getAlwaysRenderNameTagForRender() {
        return this.getAlwaysRenderNameTag();
    }
    
    public void notifyDataManagerChange(final DataParameter<?> key) {
    }
    
    public EnumFacing getHorizontalFacing() {
        return EnumFacing.byHorizontalIndex(MathHelper.floor(this.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3);
    }
    
    public EnumFacing getAdjustedHorizontalFacing() {
        return this.getHorizontalFacing();
    }
    
    protected HoverEvent getHoverEvent() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        final ResourceLocation resourcelocation = EntityList.getKey(this);
        nbttagcompound.setString("id", this.getCachedUniqueIdString());
        if (resourcelocation != null) {
            nbttagcompound.setString("type", resourcelocation.toString());
        }
        nbttagcompound.setString("name", this.getName());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new TextComponentString(nbttagcompound.toString()));
    }
    
    public boolean isSpectatedByPlayer(final EntityPlayerMP player) {
        return true;
    }
    
    public AxisAlignedBB getEntityBoundingBox() {
        return this.boundingBox;
    }
    
    public AxisAlignedBB getRenderBoundingBox() {
        return this.getEntityBoundingBox();
    }
    
    public void setEntityBoundingBox(final AxisAlignedBB bb) {
        this.boundingBox = bb;
    }
    
    public float getEyeHeight() {
        return this.height * 0.85f;
    }
    
    public boolean isOutsideBorder() {
        return this.isOutsideBorder;
    }
    
    public void setOutsideBorder(final boolean outsideBorder) {
        this.isOutsideBorder = outsideBorder;
    }
    
    public boolean replaceItemInInventory(final int inventorySlot, final ItemStack itemStackIn) {
        return false;
    }
    
    @Override
    public void sendMessage(final ITextComponent component) {
    }
    
    @Override
    public boolean canUseCommand(final int permLevel, final String commandName) {
        return true;
    }
    
    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY + 0.5, this.posZ);
    }
    
    @Override
    public Vec3d getPositionVector() {
        return new Vec3d(this.posX, this.posY, this.posZ);
    }
    
    @Override
    public World getEntityWorld() {
        return this.world;
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
    public void setCommandStat(final CommandResultStats.Type type, final int amount) {
        if (this.world != null && !this.world.isRemote) {
            this.cmdResultStats.setCommandStatForSender(this.world.getMinecraftServer(), this, type, amount);
        }
    }
    
    @Nullable
    @Override
    public MinecraftServer getServer() {
        return this.world.getMinecraftServer();
    }
    
    public CommandResultStats getCommandStats() {
        return this.cmdResultStats;
    }
    
    public void setCommandStats(final Entity entityIn) {
        this.cmdResultStats.addAllStats(entityIn.getCommandStats());
    }
    
    public EnumActionResult applyPlayerInteraction(final EntityPlayer player, final Vec3d vec, final EnumHand hand) {
        return EnumActionResult.PASS;
    }
    
    public boolean isImmuneToExplosions() {
        return false;
    }
    
    protected void applyEnchantments(final EntityLivingBase entityLivingBaseIn, final Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            EnchantmentHelper.applyThornEnchantments((EntityLivingBase)entityIn, entityLivingBaseIn);
        }
        EnchantmentHelper.applyArthropodEnchantments(entityLivingBaseIn, entityIn);
    }
    
    public void addTrackingPlayer(final EntityPlayerMP player) {
    }
    
    public void removeTrackingPlayer(final EntityPlayerMP player) {
    }
    
    public float getRotatedYaw(final Rotation transformRotation) {
        final float f = MathHelper.wrapDegrees(this.rotationYaw);
        switch (transformRotation) {
            case CLOCKWISE_180: {
                return f + 180.0f;
            }
            case COUNTERCLOCKWISE_90: {
                return f + 270.0f;
            }
            case CLOCKWISE_90: {
                return f + 90.0f;
            }
            default: {
                return f;
            }
        }
    }
    
    public float getMirroredYaw(final Mirror transformMirror) {
        final float f = MathHelper.wrapDegrees(this.rotationYaw);
        switch (transformMirror) {
            case LEFT_RIGHT: {
                return -f;
            }
            case FRONT_BACK: {
                return 180.0f - f;
            }
            default: {
                return f;
            }
        }
    }
    
    public boolean ignoreItemEntityData() {
        return false;
    }
    
    public boolean setPositionNonDirty() {
        final boolean flag = this.isPositionDirty;
        this.isPositionDirty = false;
        return flag;
    }
    
    @Nullable
    public Entity getControllingPassenger() {
        return null;
    }
    
    public List<Entity> getPassengers() {
        return this.riddenByEntities.isEmpty() ? Collections.emptyList() : Lists.newArrayList((Iterable)this.riddenByEntities);
    }
    
    public boolean isPassenger(final Entity entityIn) {
        for (final Entity entity : this.getPassengers()) {
            if (entity.equals(entityIn)) {
                return true;
            }
        }
        return false;
    }
    
    public Collection<Entity> getRecursivePassengers() {
        final Set<Entity> set = (Set<Entity>)Sets.newHashSet();
        this.getRecursivePassengersByType(Entity.class, set);
        return set;
    }
    
    public <T extends Entity> Collection<T> getRecursivePassengersByType(final Class<T> entityClass) {
        final Set<T> set = (Set<T>)Sets.newHashSet();
        this.getRecursivePassengersByType(entityClass, set);
        return set;
    }
    
    private <T extends Entity> void getRecursivePassengersByType(final Class<T> entityClass, final Set<T> theSet) {
        for (final Entity entity : this.getPassengers()) {
            if (entityClass.isAssignableFrom(entity.getClass())) {
                theSet.add((T)entity);
            }
            entity.getRecursivePassengersByType((Class<Entity>)entityClass, (Set<Entity>)theSet);
        }
    }
    
    public Entity getLowestRidingEntity() {
        Entity entity;
        for (entity = this; entity.isRiding(); entity = entity.getRidingEntity()) {}
        return entity;
    }
    
    public boolean isRidingSameEntity(final Entity entityIn) {
        return this.getLowestRidingEntity() == entityIn.getLowestRidingEntity();
    }
    
    public boolean isRidingOrBeingRiddenBy(final Entity entityIn) {
        for (final Entity entity : this.getPassengers()) {
            if (entity.equals(entityIn)) {
                return true;
            }
            if (entity.isRidingOrBeingRiddenBy(entityIn)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean canPassengerSteer() {
        final Entity entity = this.getControllingPassenger();
        if (entity instanceof EntityPlayer) {
            return ((EntityPlayer)entity).isUser();
        }
        return !this.world.isRemote;
    }
    
    @Nullable
    public Entity getRidingEntity() {
        return this.ridingEntity;
    }
    
    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.NORMAL;
    }
    
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }
    
    protected int getFireImmuneTicks() {
        return 1;
    }
    
    public double getDistanceSqToEntity(final Entity entityIn) {
        final double d0 = this.posX - entityIn.posX;
        final double d2 = this.posY - entityIn.posY;
        final double d3 = this.posZ - entityIn.posZ;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        EMPTY_EQUIPMENT = Collections.emptyList();
        ZERO_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Entity.renderDistanceWeight = 1.0;
        FLAGS = EntityDataManager.createKey(Entity.class, DataSerializers.BYTE);
        AIR = EntityDataManager.createKey(Entity.class, DataSerializers.VARINT);
        CUSTOM_NAME = EntityDataManager.createKey(Entity.class, DataSerializers.STRING);
        CUSTOM_NAME_VISIBLE = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
        SILENT = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
        NO_GRAVITY = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
    }
}
