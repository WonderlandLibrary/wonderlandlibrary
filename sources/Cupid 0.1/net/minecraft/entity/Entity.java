package net.minecraft.entity;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import me.rhys.base.Lite;
import me.rhys.base.event.Event;
import me.rhys.base.event.impl.player.SafeWalkEvent;
import me.rhys.base.event.impl.player.StepEvent;
import me.rhys.base.event.impl.player.StrafeEvent;
import me.rhys.base.event.impl.player.WalkOnEdgeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
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
import net.minecraft.nbt.NBTBase;
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

public abstract class Entity implements ICommandSender {
  private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
  
  private static int nextEntityID;
  
  private int entityId;
  
  public double renderDistanceWeight;
  
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
  
  public AxisAlignedBB boundingBox;
  
  public boolean onGround;
  
  public boolean isCollidedHorizontally;
  
  public boolean isCollidedVertically;
  
  public boolean isCollided;
  
  public boolean velocityChanged;
  
  protected boolean isInWeb;
  
  private boolean isOutsideBorder;
  
  public boolean isDead;
  
  public float width;
  
  public float height;
  
  public float prevDistanceWalkedModified;
  
  public float distanceWalkedModified;
  
  public float distanceWalkedOnStepModified;
  
  public float fallDistance;
  
  private int nextStepDistance;
  
  public double lastTickPosX;
  
  public double lastTickPosY;
  
  public double lastTickPosZ;
  
  public float stepHeight;
  
  public boolean noClip;
  
  public float entityCollisionReduction;
  
  protected Random rand;
  
  public int ticksExisted;
  
  public int fireResistance;
  
  private int fire;
  
  protected boolean inWater;
  
  public int hurtResistantTime;
  
  protected boolean firstUpdate;
  
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
  
  protected UUID entityUniqueID;
  
  private final CommandResultStats cmdResultStats;
  
  public int getEntityId() {
    return this.entityId;
  }
  
  public void setEntityId(int id) {
    this.entityId = id;
  }
  
  public void onKillCommand() {
    setDead();
  }
  
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
    this.entityUniqueID = MathHelper.getRandomUuid(this.rand);
    this.cmdResultStats = new CommandResultStats();
    this.worldObj = worldIn;
    setPosition(0.0D, 0.0D, 0.0D);
    if (worldIn != null)
      this.dimension = worldIn.provider.getDimensionId(); 
    this.dataWatcher = new DataWatcher(this);
    this.dataWatcher.addObject(0, Byte.valueOf((byte)0));
    this.dataWatcher.addObject(1, Short.valueOf((short)300));
    this.dataWatcher.addObject(3, Byte.valueOf((byte)0));
    this.dataWatcher.addObject(2, "");
    this.dataWatcher.addObject(4, Byte.valueOf((byte)0));
    entityInit();
  }
  
  protected abstract void entityInit();
  
  public DataWatcher getDataWatcher() {
    return this.dataWatcher;
  }
  
  public boolean equals(Object p_equals_1_) {
    return (p_equals_1_ instanceof Entity) ? ((((Entity)p_equals_1_).entityId == this.entityId)) : false;
  }
  
  public int hashCode() {
    return this.entityId;
  }
  
  protected void preparePlayerToSpawn() {
    if (this.worldObj != null) {
      while (this.posY > 0.0D && this.posY < 256.0D) {
        setPosition(this.posX, this.posY, this.posZ);
        if (this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty())
          break; 
        this.posY++;
      } 
      this.motionX = this.motionY = this.motionZ = 0.0D;
      this.rotationPitch = 0.0F;
    } 
  }
  
  public void setDead() {
    this.isDead = true;
  }
  
  protected void setSize(float width, float height) {
    if (width != this.width || height != this.height) {
      float f = this.width;
      this.width = width;
      this.height = height;
      setEntityBoundingBox(new AxisAlignedBB((getEntityBoundingBox()).minX, (getEntityBoundingBox()).minY, (getEntityBoundingBox()).minZ, (getEntityBoundingBox()).minX + this.width, (getEntityBoundingBox()).minY + this.height, (getEntityBoundingBox()).minZ + this.width));
      if (this.width > f && !this.firstUpdate && !this.worldObj.isRemote)
        moveEntity((f - this.width), 0.0D, (f - this.width)); 
    } 
  }
  
  protected void setRotation(float yaw, float pitch) {
    this.rotationYaw = yaw % 360.0F;
    this.rotationPitch = pitch % 360.0F;
  }
  
  public void setPosition(double x, double y, double z) {
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    float f = this.width / 2.0F;
    float f1 = this.height;
    setEntityBoundingBox(new AxisAlignedBB(x - f, y, z - f, x + f, y + f1, z + f));
  }
  
  public void setAngles(float yaw, float pitch) {
    float f = this.rotationPitch;
    float f1 = this.rotationYaw;
    this.rotationYaw = (float)(this.rotationYaw + yaw * 0.15D);
    this.rotationPitch = (float)(this.rotationPitch - pitch * 0.15D);
    this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0F, 90.0F);
    this.prevRotationPitch += this.rotationPitch - f;
    this.prevRotationYaw += this.rotationYaw - f1;
  }
  
  public void onUpdate() {
    onEntityUpdate();
  }
  
  public void onEntityUpdate() {
    this.worldObj.theProfiler.startSection("entityBaseTick");
    if (this.ridingEntity != null && this.ridingEntity.isDead)
      this.ridingEntity = null; 
    this.prevDistanceWalkedModified = this.distanceWalkedModified;
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.prevRotationPitch = this.rotationPitch;
    this.prevRotationYaw = this.rotationYaw;
    if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
      this.worldObj.theProfiler.startSection("portal");
      MinecraftServer minecraftserver = ((WorldServer)this.worldObj).getMinecraftServer();
      int i = getMaxInPortalTime();
      if (this.inPortal) {
        if (minecraftserver.getAllowNether()) {
          if (this.ridingEntity == null && this.portalCounter++ >= i) {
            int j;
            this.portalCounter = i;
            this.timeUntilPortal = getPortalCooldown();
            if (this.worldObj.provider.getDimensionId() == -1) {
              j = 0;
            } else {
              j = -1;
            } 
            travelToDimension(j);
          } 
          this.inPortal = false;
        } 
      } else {
        if (this.portalCounter > 0)
          this.portalCounter -= 4; 
        if (this.portalCounter < 0)
          this.portalCounter = 0; 
      } 
      if (this.timeUntilPortal > 0)
        this.timeUntilPortal--; 
      this.worldObj.theProfiler.endSection();
    } 
    spawnRunningParticles();
    handleWaterMovement();
    if (this.worldObj.isRemote) {
      this.fire = 0;
    } else if (this.fire > 0) {
      if (this.isImmuneToFire) {
        this.fire -= 4;
        if (this.fire < 0)
          this.fire = 0; 
      } else {
        if (this.fire % 20 == 0)
          attackEntityFrom(DamageSource.onFire, 1.0F); 
        this.fire--;
      } 
    } 
    if (isInLava()) {
      setOnFireFromLava();
      this.fallDistance *= 0.5F;
    } 
    if (this.posY < -64.0D)
      kill(); 
    if (!this.worldObj.isRemote)
      setFlag(0, (this.fire > 0)); 
    this.firstUpdate = false;
    this.worldObj.theProfiler.endSection();
  }
  
  public int getMaxInPortalTime() {
    return 0;
  }
  
  protected void setOnFireFromLava() {
    if (!this.isImmuneToFire) {
      attackEntityFrom(DamageSource.lava, 4.0F);
      setFire(15);
    } 
  }
  
  public void setFire(int seconds) {
    int i = seconds * 20;
    i = EnchantmentProtection.getFireTimeForEntity(this, i);
    if (this.fire < i)
      this.fire = i; 
  }
  
  public void extinguish() {
    this.fire = 0;
  }
  
  protected void kill() {
    setDead();
  }
  
  public boolean isOffsetPositionInLiquid(double x, double y, double z) {
    AxisAlignedBB axisalignedbb = getEntityBoundingBox().offset(x, y, z);
    return isLiquidPresentInAABB(axisalignedbb);
  }
  
  private boolean isLiquidPresentInAABB(AxisAlignedBB bb) {
    return (this.worldObj.getCollidingBoundingBoxes(this, bb).isEmpty() && !this.worldObj.isAnyLiquid(bb));
  }
  
  public void moveEntity(double x, double y, double z) {
    if (this.noClip) {
      setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
      resetPositionToBB();
    } else {
      this.worldObj.theProfiler.startSection("move");
      double d0 = this.posX;
      double d1 = this.posY;
      double d2 = this.posZ;
      if (this.isInWeb) {
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
      boolean flag = (this.onGround && isSneaking() && this instanceof EntityPlayer);
      if (((Event)Lite.EVENT_BUS.call((Event)new SafeWalkEvent())).isCancelled() || flag) {
        boolean callEvent = false;
        double d6;
        for (d6 = 0.05D; x != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(x, -1.0D, 0.0D)).isEmpty(); d3 = x) {
          if (x < d6 && x >= -d6) {
            x = 0.0D;
          } else if (x > 0.0D) {
            x -= d6;
          } else {
            x += d6;
          } 
        } 
        for (; z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(0.0D, -1.0D, z)).isEmpty(); d5 = z) {
          if (z < d6 && z >= -d6) {
            z = 0.0D;
          } else if (z > 0.0D) {
            z -= d6;
          } else {
            z += d6;
          } 
        } 
        for (; x != 0.0D && z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(x, -1.0D, z)).isEmpty(); d5 = z) {
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
        if (callEvent)
          Lite.EVENT_BUS.call((Event)new WalkOnEdgeEvent()); 
      } 
      List<AxisAlignedBB> list1 = this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().addCoord(x, y, z));
      AxisAlignedBB axisalignedbb = getEntityBoundingBox();
      for (AxisAlignedBB axisalignedbb1 : list1)
        y = axisalignedbb1.calculateYOffset(getEntityBoundingBox(), y); 
      setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, y, 0.0D));
      boolean flag1 = (this.onGround || (d4 != y && d4 < 0.0D));
      for (AxisAlignedBB axisalignedbb2 : list1)
        x = axisalignedbb2.calculateXOffset(getEntityBoundingBox(), x); 
      setEntityBoundingBox(getEntityBoundingBox().offset(x, 0.0D, 0.0D));
      for (AxisAlignedBB axisalignedbb13 : list1)
        z = axisalignedbb13.calculateZOffset(getEntityBoundingBox(), z); 
      setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, 0.0D, z));
      StepEvent stepEvent = new StepEvent(this, this.stepHeight);
      stepEvent.setType(Event.Type.PRE);
      Lite.EVENT_BUS.call((Event)stepEvent);
      if (stepEvent.getStepHeight() > 0.0F && flag1 && (d3 != x || d5 != z)) {
        double d11 = x;
        double d7 = y;
        double d8 = z;
        AxisAlignedBB axisalignedbb3 = getEntityBoundingBox();
        setEntityBoundingBox(axisalignedbb);
        y = stepEvent.getStepHeight();
        List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().addCoord(d3, y, d5));
        AxisAlignedBB axisalignedbb4 = getEntityBoundingBox();
        AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(d3, 0.0D, d5);
        double d9 = y;
        for (AxisAlignedBB axisalignedbb6 : list)
          d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9); 
        axisalignedbb4 = axisalignedbb4.offset(0.0D, d9, 0.0D);
        double d15 = d3;
        for (AxisAlignedBB axisalignedbb7 : list)
          d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15); 
        axisalignedbb4 = axisalignedbb4.offset(d15, 0.0D, 0.0D);
        double d16 = d5;
        for (AxisAlignedBB axisalignedbb8 : list)
          d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16); 
        axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d16);
        AxisAlignedBB axisalignedbb14 = getEntityBoundingBox();
        double d17 = y;
        for (AxisAlignedBB axisalignedbb9 : list)
          d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17); 
        axisalignedbb14 = axisalignedbb14.offset(0.0D, d17, 0.0D);
        double d18 = d3;
        for (AxisAlignedBB axisalignedbb10 : list)
          d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18); 
        axisalignedbb14 = axisalignedbb14.offset(d18, 0.0D, 0.0D);
        double d19 = d5;
        for (AxisAlignedBB axisalignedbb11 : list)
          d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19); 
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
        for (AxisAlignedBB axisalignedbb12 : list)
          y = axisalignedbb12.calculateYOffset(getEntityBoundingBox(), y); 
        setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, y, 0.0D));
        if (d11 * d11 + d8 * d8 >= x * x + z * z) {
          x = d11;
          y = d7;
          z = d8;
          setEntityBoundingBox(axisalignedbb3);
        } 
        StepEvent stepEventPost = new StepEvent(this, 1.0F + this.stepHeight);
        stepEventPost.setType(Event.Type.POST);
        Lite.EVENT_BUS.call((Event)stepEventPost);
      } 
      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("rest");
      resetPositionToBB();
      this.isCollidedHorizontally = (d3 != x || d5 != z);
      this.isCollidedVertically = (d4 != y);
      this.onGround = (this.isCollidedVertically && d4 < 0.0D);
      this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
      int k = MathHelper.floor_double(this.posZ);
      BlockPos blockpos = new BlockPos(i, j, k);
      Block block1 = this.worldObj.getBlockState(blockpos).getBlock();
      if (block1.getMaterial() == Material.air) {
        Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();
        if (block instanceof net.minecraft.block.BlockFence || block instanceof net.minecraft.block.BlockWall || block instanceof net.minecraft.block.BlockFenceGate) {
          block1 = block;
          blockpos = blockpos.down();
        } 
      } 
      updateFallState(y, this.onGround, block1, blockpos);
      if (d3 != x)
        this.motionX = 0.0D; 
      if (d5 != z)
        this.motionZ = 0.0D; 
      if (d4 != y)
        block1.onLanded(this.worldObj, this); 
      if (canTriggerWalking() && !flag && this.ridingEntity == null) {
        double d12 = this.posX - d0;
        double d13 = this.posY - d1;
        double d14 = this.posZ - d2;
        if (block1 != Blocks.ladder)
          d13 = 0.0D; 
        if (block1 != null && this.onGround)
          block1.onEntityCollidedWithBlock(this.worldObj, blockpos, this); 
        this.distanceWalkedModified = (float)(this.distanceWalkedModified + MathHelper.sqrt_double(d12 * d12 + d14 * d14) * 0.6D);
        this.distanceWalkedOnStepModified = (float)(this.distanceWalkedOnStepModified + MathHelper.sqrt_double(d12 * d12 + d13 * d13 + d14 * d14) * 0.6D);
        if (this.distanceWalkedOnStepModified > this.nextStepDistance && block1.getMaterial() != Material.air) {
          this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
          if (isInWater()) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F;
            if (f > 1.0F)
              f = 1.0F; 
            playSound(getSwimSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
          } 
          playStepSound(blockpos, block1);
        } 
      } 
      try {
        doBlockCollisions();
      } catch (Throwable throwable) {
        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
        CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
        addEntityCrashInfo(crashreportcategory);
        throw new ReportedException(crashreport);
      } 
      boolean flag2 = isWet();
      if (this.worldObj.isFlammableWithin(getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D))) {
        dealFireDamage(1);
        if (!flag2) {
          this.fire++;
          if (this.fire == 0)
            setFire(8); 
        } 
      } else if (this.fire <= 0) {
        this.fire = -this.fireResistance;
      } 
      if (flag2 && this.fire > 0) {
        playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
        this.fire = -this.fireResistance;
      } 
      this.worldObj.theProfiler.endSection();
    } 
  }
  
  private void resetPositionToBB() {
    this.posX = ((getEntityBoundingBox()).minX + (getEntityBoundingBox()).maxX) / 2.0D;
    this.posY = (getEntityBoundingBox()).minY;
    this.posZ = ((getEntityBoundingBox()).minZ + (getEntityBoundingBox()).maxZ) / 2.0D;
  }
  
  protected String getSwimSound() {
    return "game.neutral.swim";
  }
  
  protected void doBlockCollisions() {
    BlockPos blockpos = new BlockPos((getEntityBoundingBox()).minX + 0.001D, (getEntityBoundingBox()).minY + 0.001D, (getEntityBoundingBox()).minZ + 0.001D);
    BlockPos blockpos1 = new BlockPos((getEntityBoundingBox()).maxX - 0.001D, (getEntityBoundingBox()).maxY - 0.001D, (getEntityBoundingBox()).maxZ - 0.001D);
    if (this.worldObj.isAreaLoaded(blockpos, blockpos1))
      for (int i = blockpos.getX(); i <= blockpos1.getX(); i++) {
        for (int j = blockpos.getY(); j <= blockpos1.getY(); j++) {
          for (int k = blockpos.getZ(); k <= blockpos1.getZ(); k++) {
            BlockPos blockpos2 = new BlockPos(i, j, k);
            IBlockState iblockstate = this.worldObj.getBlockState(blockpos2);
            try {
              iblockstate.getBlock().onEntityCollidedWithBlock(this.worldObj, blockpos2, iblockstate, this);
            } catch (Throwable throwable) {
              CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
              CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
              CrashReportCategory.addBlockInfo(crashreportcategory, blockpos2, iblockstate);
              throw new ReportedException(crashreport);
            } 
          } 
        } 
      }  
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    Block.SoundType block$soundtype = blockIn.stepSound;
    if (this.worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer) {
      block$soundtype = Blocks.snow_layer.stepSound;
      playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
    } else if (!blockIn.getMaterial().isLiquid()) {
      playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
    } 
  }
  
  public void playSound(String name, float volume, float pitch) {
    if (!isSilent())
      this.worldObj.playSoundAtEntity(this, name, volume, pitch); 
  }
  
  public boolean isSilent() {
    return (this.dataWatcher.getWatchableObjectByte(4) == 1);
  }
  
  public void setSilent(boolean isSilent) {
    this.dataWatcher.updateObject(4, Byte.valueOf((byte)(isSilent ? 1 : 0)));
  }
  
  protected boolean canTriggerWalking() {
    return true;
  }
  
  protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {
    if (onGroundIn) {
      if (this.fallDistance > 0.0F) {
        if (blockIn != null) {
          blockIn.onFallenUpon(this.worldObj, pos, this, this.fallDistance);
        } else {
          fall(this.fallDistance, 1.0F);
        } 
        this.fallDistance = 0.0F;
      } 
    } else if (y < 0.0D) {
      this.fallDistance = (float)(this.fallDistance - y);
    } 
  }
  
  public AxisAlignedBB getCollisionBoundingBox() {
    return null;
  }
  
  protected void dealFireDamage(int amount) {
    if (!this.isImmuneToFire)
      attackEntityFrom(DamageSource.inFire, amount); 
  }
  
  public final boolean isImmuneToFire() {
    return this.isImmuneToFire;
  }
  
  public void fall(float distance, float damageMultiplier) {
    if (this.riddenByEntity != null)
      this.riddenByEntity.fall(distance, damageMultiplier); 
  }
  
  public boolean isWet() {
    return (this.inWater || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY, this.posZ)) || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY + this.height, this.posZ)));
  }
  
  public boolean isInWater() {
    return this.inWater;
  }
  
  public boolean handleWaterMovement() {
    if (this.worldObj.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this)) {
      if (!this.inWater && !this.firstUpdate)
        resetHeight(); 
      this.fallDistance = 0.0F;
      this.inWater = true;
      this.fire = 0;
    } else {
      this.inWater = false;
    } 
    return this.inWater;
  }
  
  protected void resetHeight() {
    float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;
    if (f > 1.0F)
      f = 1.0F; 
    playSound(getSplashSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
    float f1 = MathHelper.floor_double((getEntityBoundingBox()).minY);
    for (int i = 0; i < 1.0F + this.width * 20.0F; i++) {
      float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
      float f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
      this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f2, (f1 + 1.0F), this.posZ + f3, this.motionX, this.motionY - (this.rand.nextFloat() * 0.2F), this.motionZ, new int[0]);
    } 
    for (int j = 0; j < 1.0F + this.width * 20.0F; j++) {
      float f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
      float f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
      this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f4, (f1 + 1.0F), this.posZ + f5, this.motionX, this.motionY, this.motionZ, new int[0]);
    } 
  }
  
  public void spawnRunningParticles() {
    if (isSprinting() && !isInWater())
      createRunningParticles(); 
  }
  
  protected void createRunningParticles() {
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
    int k = MathHelper.floor_double(this.posZ);
    BlockPos blockpos = new BlockPos(i, j, k);
    IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
    Block block = iblockstate.getBlock();
    if (block.getRenderType() != -1)
      this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, (getEntityBoundingBox()).minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D, new int[] { Block.getStateId(iblockstate) }); 
  }
  
  protected String getSplashSound() {
    return "game.neutral.swim.splash";
  }
  
  public boolean isInsideOfMaterial(Material materialIn) {
    double d0 = this.posY + getEyeHeight();
    BlockPos blockpos = new BlockPos(this.posX, d0, this.posZ);
    IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
    Block block = iblockstate.getBlock();
    if (block.getMaterial() == materialIn) {
      float f = BlockLiquid.getLiquidHeightPercent(iblockstate.getBlock().getMetaFromState(iblockstate)) - 0.11111111F;
      float f1 = (blockpos.getY() + 1) - f;
      boolean flag = (d0 < f1);
      return (!flag && this instanceof EntityPlayer) ? false : flag;
    } 
    return false;
  }
  
  public boolean isInLava() {
    return this.worldObj.isMaterialInBB(getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
  }
  
  public void moveFlying(float strafe, float forward, float friction) {
    StrafeEvent strafeEvent = new StrafeEvent(strafe, friction, forward);
    if (!((Event)Lite.EVENT_BUS.call((Event)strafeEvent)).isCancelled()) {
      float f = strafe * strafe + forward * forward;
      if (f >= 1.0E-4F) {
        f = MathHelper.sqrt_float(f);
        if (f < 1.0F)
          f = 1.0F; 
        f = friction / f;
        strafe *= f;
        forward *= f;
        float f1 = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F);
        float f2 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F);
        this.motionX += (strafe * f2 - forward * f1);
        this.motionZ += (forward * f2 + strafe * f1);
      } 
    } 
  }
  
  public int getBrightnessForRender(float partialTicks) {
    BlockPos blockpos = new BlockPos(this.posX, this.posY + getEyeHeight(), this.posZ);
    return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getCombinedLight(blockpos, 0) : 0;
  }
  
  public float getBrightness(float partialTicks) {
    BlockPos blockpos = new BlockPos(this.posX, this.posY + getEyeHeight(), this.posZ);
    return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getLightBrightness(blockpos) : 0.0F;
  }
  
  public void setWorld(World worldIn) {
    this.worldObj = worldIn;
  }
  
  public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
    this.prevPosX = this.posX = x;
    this.prevPosY = this.posY = y;
    this.prevPosZ = this.posZ = z;
    this.prevRotationYaw = this.rotationYaw = yaw;
    this.prevRotationPitch = this.rotationPitch = pitch;
    double d0 = (this.prevRotationYaw - yaw);
    if (d0 < -180.0D)
      this.prevRotationYaw += 360.0F; 
    if (d0 >= 180.0D)
      this.prevRotationYaw -= 360.0F; 
    setPosition(this.posX, this.posY, this.posZ);
    setRotation(yaw, pitch);
  }
  
  public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn) {
    setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, rotationYawIn, rotationPitchIn);
  }
  
  public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
    this.lastTickPosX = this.prevPosX = this.posX = x;
    this.lastTickPosY = this.prevPosY = this.posY = y;
    this.lastTickPosZ = this.prevPosZ = this.posZ = z;
    this.rotationYaw = yaw;
    this.rotationPitch = pitch;
    setPosition(this.posX, this.posY, this.posZ);
  }
  
  public float getDistanceToEntity(Entity entityIn) {
    float f = (float)(this.posX - entityIn.posX);
    float f1 = (float)(this.posY - entityIn.posY);
    float f2 = (float)(this.posZ - entityIn.posZ);
    return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
  }
  
  public double getDistanceSq(double x, double y, double z) {
    double d0 = this.posX - x;
    double d1 = this.posY - y;
    double d2 = this.posZ - z;
    return d0 * d0 + d1 * d1 + d2 * d2;
  }
  
  public double getDistanceSq(BlockPos pos) {
    return pos.distanceSq(this.posX, this.posY, this.posZ);
  }
  
  public double getDistanceSqToCenter(BlockPos pos) {
    return pos.distanceSqToCenter(this.posX, this.posY, this.posZ);
  }
  
  public double getDistance(double x, double y, double z) {
    double d0 = this.posX - x;
    double d1 = this.posY - y;
    double d2 = this.posZ - z;
    return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
  }
  
  public double getDistanceSqToEntity(Entity entityIn) {
    double d0 = this.posX - entityIn.posX;
    double d1 = this.posY - entityIn.posY;
    double d2 = this.posZ - entityIn.posZ;
    return d0 * d0 + d1 * d1 + d2 * d2;
  }
  
  public void onCollideWithPlayer(EntityPlayer entityIn) {}
  
  public void applyEntityCollision(Entity entityIn) {
    if (entityIn.riddenByEntity != this && entityIn.ridingEntity != this)
      if (!entityIn.noClip && !this.noClip) {
        double d0 = entityIn.posX - this.posX;
        double d1 = entityIn.posZ - this.posZ;
        double d2 = MathHelper.abs_max(d0, d1);
        if (d2 >= 0.009999999776482582D) {
          d2 = MathHelper.sqrt_double(d2);
          d0 /= d2;
          d1 /= d2;
          double d3 = 1.0D / d2;
          if (d3 > 1.0D)
            d3 = 1.0D; 
          d0 *= d3;
          d1 *= d3;
          d0 *= 0.05000000074505806D;
          d1 *= 0.05000000074505806D;
          d0 *= (1.0F - this.entityCollisionReduction);
          d1 *= (1.0F - this.entityCollisionReduction);
          if (this.riddenByEntity == null)
            addVelocity(-d0, 0.0D, -d1); 
          if (entityIn.riddenByEntity == null)
            entityIn.addVelocity(d0, 0.0D, d1); 
        } 
      }  
  }
  
  public void addVelocity(double x, double y, double z) {
    this.motionX += x;
    this.motionY += y;
    this.motionZ += z;
    this.isAirBorne = true;
  }
  
  protected void setBeenAttacked() {
    this.velocityChanged = true;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    setBeenAttacked();
    return false;
  }
  
  public Vec3 getLook(float partialTicks) {
    if (partialTicks == 1.0F)
      return getVectorForRotation(this.rotationPitch, this.rotationYaw); 
    float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
    float f1 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
    return getVectorForRotation(f, f1);
  }
  
  public final Vec3 getVectorForRotation(float pitch, float yaw) {
    float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
    float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
    float f2 = -MathHelper.cos(-pitch * 0.017453292F);
    float f3 = MathHelper.sin(-pitch * 0.017453292F);
    return new Vec3((f1 * f2), f3, (f * f2));
  }
  
  public Vec3 getPositionEyes(float partialTicks) {
    if (partialTicks == 1.0F)
      return new Vec3(this.posX, this.posY + getEyeHeight(), this.posZ); 
    double d0 = this.prevPosX + (this.posX - this.prevPosX) * partialTicks;
    double d1 = this.prevPosY + (this.posY - this.prevPosY) * partialTicks + getEyeHeight();
    double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks;
    return new Vec3(d0, d1, d2);
  }
  
  public MovingObjectPosition rayTrace(double blockReachDistance, float partialTicks) {
    Vec3 vec3 = getPositionEyes(partialTicks);
    Vec3 vec31 = getLook(partialTicks);
    Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
    return this.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
  }
  
  public boolean canBeCollidedWith() {
    return false;
  }
  
  public boolean canBePushed() {
    return false;
  }
  
  public void addToPlayerScore(Entity entityIn, int amount) {}
  
  public boolean isInRangeToRender3d(double x, double y, double z) {
    double d0 = this.posX - x;
    double d1 = this.posY - y;
    double d2 = this.posZ - z;
    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
    return isInRangeToRenderDist(d3);
  }
  
  public boolean isInRangeToRenderDist(double distance) {
    double d0 = getEntityBoundingBox().getAverageEdgeLength();
    if (Double.isNaN(d0))
      d0 = 1.0D; 
    d0 = d0 * 64.0D * this.renderDistanceWeight;
    return (distance < d0 * d0);
  }
  
  public boolean writeMountToNBT(NBTTagCompound tagCompund) {
    String s = getEntityString();
    if (!this.isDead && s != null) {
      tagCompund.setString("id", s);
      writeToNBT(tagCompund);
      return true;
    } 
    return false;
  }
  
  public boolean writeToNBTOptional(NBTTagCompound tagCompund) {
    String s = getEntityString();
    if (!this.isDead && s != null && this.riddenByEntity == null) {
      tagCompund.setString("id", s);
      writeToNBT(tagCompund);
      return true;
    } 
    return false;
  }
  
  public void writeToNBT(NBTTagCompound tagCompund) {
    try {
      tagCompund.setTag("Pos", (NBTBase)newDoubleNBTList(new double[] { this.posX, this.posY, this.posZ }));
      tagCompund.setTag("Motion", (NBTBase)newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
      tagCompund.setTag("Rotation", (NBTBase)newFloatNBTList(new float[] { this.rotationYaw, this.rotationPitch }));
      tagCompund.setFloat("FallDistance", this.fallDistance);
      tagCompund.setShort("Fire", (short)this.fire);
      tagCompund.setShort("Air", (short)getAir());
      tagCompund.setBoolean("OnGround", this.onGround);
      tagCompund.setInteger("Dimension", this.dimension);
      tagCompund.setBoolean("Invulnerable", this.invulnerable);
      tagCompund.setInteger("PortalCooldown", this.timeUntilPortal);
      tagCompund.setLong("UUIDMost", getUniqueID().getMostSignificantBits());
      tagCompund.setLong("UUIDLeast", getUniqueID().getLeastSignificantBits());
      if (getCustomNameTag() != null && getCustomNameTag().length() > 0) {
        tagCompund.setString("CustomName", getCustomNameTag());
        tagCompund.setBoolean("CustomNameVisible", getAlwaysRenderNameTag());
      } 
      this.cmdResultStats.writeStatsToNBT(tagCompund);
      if (isSilent())
        tagCompund.setBoolean("Silent", isSilent()); 
      writeEntityToNBT(tagCompund);
      if (this.ridingEntity != null) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        if (this.ridingEntity.writeMountToNBT(nbttagcompound))
          tagCompund.setTag("Riding", (NBTBase)nbttagcompound); 
      } 
    } catch (Throwable throwable) {
      CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
      CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
      addEntityCrashInfo(crashreportcategory);
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
      if (Math.abs(this.motionX) > 10.0D)
        this.motionX = 0.0D; 
      if (Math.abs(this.motionY) > 10.0D)
        this.motionY = 0.0D; 
      if (Math.abs(this.motionZ) > 10.0D)
        this.motionZ = 0.0D; 
      this.prevPosX = this.lastTickPosX = this.posX = nbttaglist.getDoubleAt(0);
      this.prevPosY = this.lastTickPosY = this.posY = nbttaglist.getDoubleAt(1);
      this.prevPosZ = this.lastTickPosZ = this.posZ = nbttaglist.getDoubleAt(2);
      this.prevRotationYaw = this.rotationYaw = nbttaglist2.getFloatAt(0);
      this.prevRotationPitch = this.rotationPitch = nbttaglist2.getFloatAt(1);
      setRotationYawHead(this.rotationYaw);
      func_181013_g(this.rotationYaw);
      this.fallDistance = tagCompund.getFloat("FallDistance");
      this.fire = tagCompund.getShort("Fire");
      setAir(tagCompund.getShort("Air"));
      this.onGround = tagCompund.getBoolean("OnGround");
      this.dimension = tagCompund.getInteger("Dimension");
      this.invulnerable = tagCompund.getBoolean("Invulnerable");
      this.timeUntilPortal = tagCompund.getInteger("PortalCooldown");
      if (tagCompund.hasKey("UUIDMost", 4) && tagCompund.hasKey("UUIDLeast", 4)) {
        this.entityUniqueID = new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast"));
      } else if (tagCompund.hasKey("UUID", 8)) {
        this.entityUniqueID = UUID.fromString(tagCompund.getString("UUID"));
      } 
      setPosition(this.posX, this.posY, this.posZ);
      setRotation(this.rotationYaw, this.rotationPitch);
      if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0)
        setCustomNameTag(tagCompund.getString("CustomName")); 
      setAlwaysRenderNameTag(tagCompund.getBoolean("CustomNameVisible"));
      this.cmdResultStats.readStatsFromNBT(tagCompund);
      setSilent(tagCompund.getBoolean("Silent"));
      readEntityFromNBT(tagCompund);
      if (shouldSetPosAfterLoading())
        setPosition(this.posX, this.posY, this.posZ); 
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
  
  protected final String getEntityString() {
    return EntityList.getEntityString(this);
  }
  
  protected abstract void readEntityFromNBT(NBTTagCompound paramNBTTagCompound);
  
  protected abstract void writeEntityToNBT(NBTTagCompound paramNBTTagCompound);
  
  public void onChunkLoad() {}
  
  protected NBTTagList newDoubleNBTList(double... numbers) {
    NBTTagList nbttaglist = new NBTTagList();
    for (double d0 : numbers)
      nbttaglist.appendTag((NBTBase)new NBTTagDouble(d0)); 
    return nbttaglist;
  }
  
  protected NBTTagList newFloatNBTList(float... numbers) {
    NBTTagList nbttaglist = new NBTTagList();
    for (float f : numbers)
      nbttaglist.appendTag((NBTBase)new NBTTagFloat(f)); 
    return nbttaglist;
  }
  
  public EntityItem dropItem(Item itemIn, int size) {
    return dropItemWithOffset(itemIn, size, 0.0F);
  }
  
  public EntityItem dropItemWithOffset(Item itemIn, int size, float offsetY) {
    return entityDropItem(new ItemStack(itemIn, size, 0), offsetY);
  }
  
  public EntityItem entityDropItem(ItemStack itemStackIn, float offsetY) {
    if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
      EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + offsetY, this.posZ, itemStackIn);
      entityitem.setDefaultPickupDelay();
      this.worldObj.spawnEntityInWorld((Entity)entityitem);
      return entityitem;
    } 
    return null;
  }
  
  public boolean isEntityAlive() {
    return !this.isDead;
  }
  
  public boolean isEntityInsideOpaqueBlock() {
    if (this.noClip)
      return false; 
    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(-2147483648, -2147483648, -2147483648);
    for (int i = 0; i < 8; i++) {
      int j = MathHelper.floor_double(this.posY + ((((i >> 0) % 2) - 0.5F) * 0.1F) + getEyeHeight());
      int k = MathHelper.floor_double(this.posX + ((((i >> 1) % 2) - 0.5F) * this.width * 0.8F));
      int l = MathHelper.floor_double(this.posZ + ((((i >> 2) % 2) - 0.5F) * this.width * 0.8F));
      if (blockpos$mutableblockpos.getX() != k || blockpos$mutableblockpos.getY() != j || blockpos$mutableblockpos.getZ() != l) {
        blockpos$mutableblockpos.func_181079_c(k, j, l);
        if (this.worldObj.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock().isVisuallyOpaque())
          return true; 
      } 
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
      this.motionX = 0.0D;
      this.motionY = 0.0D;
      this.motionZ = 0.0D;
      onUpdate();
      if (this.ridingEntity != null) {
        this.ridingEntity.updateRiderPosition();
        this.entityRiderYawDelta += (this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);
        for (this.entityRiderPitchDelta += (this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D);
        while (this.entityRiderYawDelta < -180.0D)
          this.entityRiderYawDelta += 360.0D; 
        while (this.entityRiderPitchDelta >= 180.0D)
          this.entityRiderPitchDelta -= 360.0D; 
        while (this.entityRiderPitchDelta < -180.0D)
          this.entityRiderPitchDelta += 360.0D; 
        double d0 = this.entityRiderYawDelta * 0.5D;
        double d1 = this.entityRiderPitchDelta * 0.5D;
        float f = 10.0F;
        if (d0 > f)
          d0 = f; 
        if (d0 < -f)
          d0 = -f; 
        if (d1 > f)
          d1 = f; 
        if (d1 < -f)
          d1 = -f; 
        this.entityRiderYawDelta -= d0;
        this.entityRiderPitchDelta -= d1;
      } 
    } 
  }
  
  public void updateRiderPosition() {
    if (this.riddenByEntity != null)
      this.riddenByEntity.setPosition(this.posX, this.posY + getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ); 
  }
  
  public double getYOffset() {
    return 0.0D;
  }
  
  public double getMountedYOffset() {
    return this.height * 0.75D;
  }
  
  public void mountEntity(Entity entityIn) {
    this.entityRiderPitchDelta = 0.0D;
    this.entityRiderYawDelta = 0.0D;
    if (entityIn == null) {
      if (this.ridingEntity != null) {
        setLocationAndAngles(this.ridingEntity.posX, (this.ridingEntity.getEntityBoundingBox()).minY + this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
        this.ridingEntity.riddenByEntity = null;
      } 
      this.ridingEntity = null;
    } else {
      if (this.ridingEntity != null)
        this.ridingEntity.riddenByEntity = null; 
      if (entityIn != null)
        for (Entity entity = entityIn.ridingEntity; entity != null; entity = entity.ridingEntity) {
          if (entity == this)
            return; 
        }  
      this.ridingEntity = entityIn;
      entityIn.riddenByEntity = this;
    } 
  }
  
  public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
    setPosition(x, y, z);
    setRotation(yaw, pitch);
    List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().contract(0.03125D, 0.0D, 0.03125D));
    if (!list.isEmpty()) {
      double d0 = 0.0D;
      for (AxisAlignedBB axisalignedbb : list) {
        if (axisalignedbb.maxY > d0)
          d0 = axisalignedbb.maxY; 
      } 
      y += d0 - (getEntityBoundingBox()).minY;
      setPosition(x, y, z);
    } 
  }
  
  public float getCollisionBorderSize() {
    return 0.1F;
  }
  
  public Vec3 getLookVec() {
    return null;
  }
  
  public void func_181015_d(BlockPos p_181015_1_) {
    if (this.timeUntilPortal > 0) {
      this.timeUntilPortal = getPortalCooldown();
    } else {
      if (!this.worldObj.isRemote && !p_181015_1_.equals(this.field_181016_an)) {
        this.field_181016_an = p_181015_1_;
        BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal.func_181089_f(this.worldObj, p_181015_1_);
        double d0 = (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? blockpattern$patternhelper.func_181117_a().getZ() : blockpattern$patternhelper.func_181117_a().getX();
        double d1 = (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? this.posZ : this.posX;
        d1 = Math.abs(MathHelper.func_181160_c(d1 - ((blockpattern$patternhelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) ? true : false), d0, d0 - blockpattern$patternhelper.func_181118_d()));
        double d2 = MathHelper.func_181160_c(this.posY - 1.0D, blockpattern$patternhelper.func_181117_a().getY(), (blockpattern$patternhelper.func_181117_a().getY() - blockpattern$patternhelper.func_181119_e()));
        this.field_181017_ao = new Vec3(d1, d2, 0.0D);
        this.field_181018_ap = blockpattern$patternhelper.getFinger();
      } 
      this.inPortal = true;
    } 
  }
  
  public int getPortalCooldown() {
    return 300;
  }
  
  public void setVelocity(double x, double y, double z) {
    this.motionX = x;
    this.motionY = y;
    this.motionZ = z;
  }
  
  public void handleStatusUpdate(byte id) {}
  
  public void performHurtAnimation() {}
  
  public ItemStack[] getInventory() {
    return null;
  }
  
  public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {}
  
  public boolean isBurning() {
    boolean flag = (this.worldObj != null && this.worldObj.isRemote);
    return (!this.isImmuneToFire && (this.fire > 0 || (flag && getFlag(0))));
  }
  
  public boolean isRiding() {
    return (this.ridingEntity != null);
  }
  
  public boolean isSneaking() {
    return getFlag(1);
  }
  
  public void setSneaking(boolean sneaking) {
    setFlag(1, sneaking);
  }
  
  public boolean isSprinting() {
    return getFlag(3);
  }
  
  public void setSprinting(boolean sprinting) {
    setFlag(3, sprinting);
  }
  
  public boolean isInvisible() {
    return getFlag(5);
  }
  
  public boolean isInvisibleToPlayer(EntityPlayer player) {
    return player.isSpectator() ? false : isInvisible();
  }
  
  public void setInvisible(boolean invisible) {
    setFlag(5, invisible);
  }
  
  public boolean isEating() {
    return getFlag(4);
  }
  
  public void setEating(boolean eating) {
    setFlag(4, eating);
  }
  
  protected boolean getFlag(int flag) {
    return ((this.dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0);
  }
  
  protected void setFlag(int flag, boolean set) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(0);
    if (set) {
      this.dataWatcher.updateObject(0, Byte.valueOf((byte)(b0 | 1 << flag)));
    } else {
      this.dataWatcher.updateObject(0, Byte.valueOf((byte)(b0 & (1 << flag ^ 0xFFFFFFFF))));
    } 
  }
  
  public int getAir() {
    return this.dataWatcher.getWatchableObjectShort(1);
  }
  
  public void setAir(int air) {
    this.dataWatcher.updateObject(1, Short.valueOf((short)air));
  }
  
  public void onStruckByLightning(EntityLightningBolt lightningBolt) {
    attackEntityFrom(DamageSource.lightningBolt, 5.0F);
    this.fire++;
    if (this.fire == 0)
      setFire(8); 
  }
  
  public void onKillEntity(EntityLivingBase entityLivingIn) {}
  
  protected boolean pushOutOfBlocks(double x, double y, double z) {
    BlockPos blockpos = new BlockPos(x, y, z);
    double d0 = x - blockpos.getX();
    double d1 = y - blockpos.getY();
    double d2 = z - blockpos.getZ();
    List<AxisAlignedBB> list = this.worldObj.func_147461_a(getEntityBoundingBox());
    if (list.isEmpty() && !this.worldObj.isBlockFullCube(blockpos))
      return false; 
    int i = 3;
    double d3 = 9999.0D;
    if (!this.worldObj.isBlockFullCube(blockpos.west()) && d0 < d3) {
      d3 = d0;
      i = 0;
    } 
    if (!this.worldObj.isBlockFullCube(blockpos.east()) && 1.0D - d0 < d3) {
      d3 = 1.0D - d0;
      i = 1;
    } 
    if (!this.worldObj.isBlockFullCube(blockpos.up()) && 1.0D - d1 < d3) {
      d3 = 1.0D - d1;
      i = 3;
    } 
    if (!this.worldObj.isBlockFullCube(blockpos.north()) && d2 < d3) {
      d3 = d2;
      i = 4;
    } 
    if (!this.worldObj.isBlockFullCube(blockpos.south()) && 1.0D - d2 < d3) {
      d3 = 1.0D - d2;
      i = 5;
    } 
    float f = this.rand.nextFloat() * 0.2F + 0.1F;
    if (i == 0)
      this.motionX = -f; 
    if (i == 1)
      this.motionX = f; 
    if (i == 3)
      this.motionY = f; 
    if (i == 4)
      this.motionZ = -f; 
    if (i == 5)
      this.motionZ = f; 
    return true;
  }
  
  public void setInWeb() {
    this.isInWeb = true;
    this.fallDistance = 0.0F;
  }
  
  public String getName() {
    if (hasCustomName())
      return getCustomNameTag(); 
    String s = EntityList.getEntityString(this);
    if (s == null)
      s = "generic"; 
    return StatCollector.translateToLocal("entity." + s + ".name");
  }
  
  public Entity[] getParts() {
    return null;
  }
  
  public boolean isEntityEqual(Entity entityIn) {
    return (this == entityIn);
  }
  
  public float getRotationYawHead() {
    return 0.0F;
  }
  
  public void setRotationYawHead(float rotation) {}
  
  public void func_181013_g(float p_181013_1_) {}
  
  public boolean canAttackWithItem() {
    return true;
  }
  
  public boolean hitByEntity(Entity entityIn) {
    return false;
  }
  
  public String toString() {
    return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", new Object[] { getClass().getSimpleName(), getName(), Integer.valueOf(this.entityId), (this.worldObj == null) ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ) });
  }
  
  public boolean isEntityInvulnerable(DamageSource source) {
    return (this.invulnerable && source != DamageSource.outOfWorld && !source.isCreativePlayer());
  }
  
  public void copyLocationAndAnglesFrom(Entity entityIn) {
    setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
  }
  
  public void copyDataFromOld(Entity entityIn) {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    entityIn.writeToNBT(nbttagcompound);
    readFromNBT(nbttagcompound);
    this.timeUntilPortal = entityIn.timeUntilPortal;
    this.field_181016_an = entityIn.field_181016_an;
    this.field_181017_ao = entityIn.field_181017_ao;
    this.field_181018_ap = entityIn.field_181018_ap;
  }
  
  public void travelToDimension(int dimensionId) {
    if (!this.worldObj.isRemote && !this.isDead) {
      this.worldObj.theProfiler.startSection("changeDimension");
      MinecraftServer minecraftserver = MinecraftServer.getServer();
      int i = this.dimension;
      WorldServer worldserver = minecraftserver.worldServerForDimension(i);
      WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionId);
      this.dimension = dimensionId;
      if (i == 1 && dimensionId == 1) {
        worldserver1 = minecraftserver.worldServerForDimension(0);
        this.dimension = 0;
      } 
      this.worldObj.removeEntity(this);
      this.isDead = false;
      this.worldObj.theProfiler.startSection("reposition");
      minecraftserver.getConfigurationManager().transferEntityToWorld(this, i, worldserver, worldserver1);
      this.worldObj.theProfiler.endStartSection("reloading");
      Entity entity = EntityList.createEntityByName(EntityList.getEntityString(this), (World)worldserver1);
      if (entity != null) {
        entity.copyDataFromOld(this);
        if (i == 1 && dimensionId == 1) {
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
    category.addCrashSectionCallable("Entity Type", new Callable<String>() {
          public String call() throws Exception {
            return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
          }
        });
    category.addCrashSection("Entity ID", Integer.valueOf(this.entityId));
    category.addCrashSectionCallable("Entity Name", new Callable<String>() {
          public String call() throws Exception {
            return Entity.this.getName();
          }
        });
    category.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ) }));
    category.addCrashSection("Entity's Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
    category.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.motionX), Double.valueOf(this.motionY), Double.valueOf(this.motionZ) }));
    category.addCrashSectionCallable("Entity's Rider", new Callable<String>() {
          public String call() throws Exception {
            return Entity.this.riddenByEntity.toString();
          }
        });
    category.addCrashSectionCallable("Entity's Vehicle", new Callable<String>() {
          public String call() throws Exception {
            return Entity.this.ridingEntity.toString();
          }
        });
  }
  
  public boolean canRenderOnFire() {
    return isBurning();
  }
  
  public UUID getUniqueID() {
    return this.entityUniqueID;
  }
  
  public boolean isPushedByWater() {
    return true;
  }
  
  public IChatComponent getDisplayName() {
    ChatComponentText chatcomponenttext = new ChatComponentText(getName());
    chatcomponenttext.getChatStyle().setChatHoverEvent(getHoverEvent());
    chatcomponenttext.getChatStyle().setInsertion(getUniqueID().toString());
    return (IChatComponent)chatcomponenttext;
  }
  
  public void setCustomNameTag(String name) {
    this.dataWatcher.updateObject(2, name);
  }
  
  public String getCustomNameTag() {
    return this.dataWatcher.getWatchableObjectString(2);
  }
  
  public boolean hasCustomName() {
    return (this.dataWatcher.getWatchableObjectString(2).length() > 0);
  }
  
  public void setAlwaysRenderNameTag(boolean alwaysRenderNameTag) {
    this.dataWatcher.updateObject(3, Byte.valueOf((byte)(alwaysRenderNameTag ? 1 : 0)));
  }
  
  public boolean getAlwaysRenderNameTag() {
    return (this.dataWatcher.getWatchableObjectByte(3) == 1);
  }
  
  public void setPositionAndUpdate(double x, double y, double z) {
    setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
  }
  
  public boolean getAlwaysRenderNameTagForRender() {
    return getAlwaysRenderNameTag();
  }
  
  public void onDataWatcherUpdate(int dataID) {}
  
  public EnumFacing getHorizontalFacing() {
    return EnumFacing.getHorizontal(MathHelper.floor_double((this.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3);
  }
  
  protected HoverEvent getHoverEvent() {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    String s = EntityList.getEntityString(this);
    nbttagcompound.setString("id", getUniqueID().toString());
    if (s != null)
      nbttagcompound.setString("type", s); 
    nbttagcompound.setString("name", getName());
    return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, (IChatComponent)new ChatComponentText(nbttagcompound.toString()));
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
    return this.height * 0.85F;
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
  
  public void addChatMessage(IChatComponent component) {}
  
  public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
    return true;
  }
  
  public BlockPos getPosition() {
    return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
  }
  
  public Vec3 getPositionVector() {
    return new Vec3(this.posX, this.posY, this.posZ);
  }
  
  public World getEntityWorld() {
    return this.worldObj;
  }
  
  public Entity getCommandSenderEntity() {
    return this;
  }
  
  public boolean sendCommandFeedback() {
    return false;
  }
  
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
  
  public void clientUpdateEntityNBT(NBTTagCompound compound) {}
  
  public boolean interactAt(EntityPlayer player, Vec3 targetVec3) {
    return false;
  }
  
  public boolean isImmuneToExplosions() {
    return false;
  }
  
  protected void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn) {
    if (entityIn instanceof EntityLivingBase)
      EnchantmentHelper.applyThornEnchantments((EntityLivingBase)entityIn, entityLivingBaseIn); 
    EnchantmentHelper.applyArthropodEnchantments(entityLivingBaseIn, entityIn);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\Entity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */