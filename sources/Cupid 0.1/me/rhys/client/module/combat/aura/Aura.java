package me.rhys.client.module.combat.aura;

import me.rhys.base.Lite;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.RotationUtil;
import me.rhys.base.util.Timer;
import me.rhys.base.util.entity.RayCast;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.combat.aura.modes.Single;
import me.rhys.client.module.combat.criticals.Criticals;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Aura extends Module {
  @Name("Attack Method")
  public AttackMethod attackMethod;
  
  @Name("Rounding Type")
  public RoundingType roundingType;
  
  @Name("Rotation Type")
  public RotationType rotationType;
  
  @Name("Block Type")
  public BlockMode blockMode;
  
  @Name("Unblock Type")
  public UnBlockMode unBlockMode;
  
  @Name("CPS")
  @Clamp(min = 1.0D, max = 20.0D)
  public double cps;
  
  @Name("Reach")
  @Clamp(min = 1.0D, max = 8.0D)
  public double reach;
  
  @Name("Smoothness")
  @Clamp(min = 0.0D, max = 100.0D)
  public float smoothness;
  
  @Name("AutoBlock")
  public boolean autoBlock;
  
  @Name("RayCheck")
  public boolean rayCheck;
  
  @Name("RayCast")
  public boolean rayCast;
  
  @Name("Monsters")
  public boolean monsters;
  
  @Name("Sleeping")
  public boolean sleeping;
  
  @Name("Invisibles")
  public boolean invisible;
  
  @Name("Dead Players")
  public boolean deadPlayers;
  
  @Name("KeepSprint")
  public boolean keepSprint;
  
  @Name("LockView")
  public boolean lockView;
  
  @Name("Swing")
  public boolean swing;
  
  @Name("Crack")
  public boolean crack;
  
  @Name("Crack Type")
  public CrackType crackType;
  
  @Name("Crack Size")
  @Clamp(min = 1.0D, max = 20.0D)
  public int crackSize;
  
  public EntityLivingBase target;
  
  public final Timer attackTimer;
  
  public Vec2f currentRotation;
  
  public boolean blocking;
  
  public Aura(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.attackMethod = AttackMethod.PRE;
    this.roundingType = RoundingType.MODULO;
    this.rotationType = RotationType.NORMAL;
    this.blockMode = BlockMode.NCP_INTERACT;
    this.unBlockMode = UnBlockMode.SWING;
    this.cps = 12.0D;
    this.reach = 3.0D;
    this.smoothness = 0.0F;
    this.autoBlock = false;
    this.rayCheck = true;
    this.rayCast = true;
    this.monsters = false;
    this.sleeping = true;
    this.invisible = true;
    this.deadPlayers = false;
    this.keepSprint = false;
    this.lockView = false;
    this.swing = true;
    this.crack = false;
    this.crackType = CrackType.NORMAL;
    this.crackSize = 4;
    this.attackTimer = new Timer();
    this.currentRotation = null;
    add((ModuleMode)new Single("Single", this));
  }
  
  public void onEnable() {
    this.attackTimer.reset();
  }
  
  public void onDisable() {
    this.blocking = false;
    this.target = null;
    this.currentRotation = null;
    this.mc.thePlayer.itemInUseCount = 0;
  }
  
  @EventTarget
  public void onUpdate(PlayerUpdateEvent event) {
    this.target = findTarget();
    if (this.rayCast && this.target != null && this.currentRotation != null) {
      EntityLivingBase rayCast;
      if ((rayCast = RayCast.rayCast((Entity)this.target, this.currentRotation.getVecX(), this.currentRotation.getVecY())) != null)
        this.target = rayCast; 
    } 
    if (this.target != null) {
      this.mc.thePlayer.setSprinting(this.keepSprint);
      if (!this.keepSprint)
        this.mc.gameSettings.keyBindSprint.pressed = false; 
    } else {
      this.mc.thePlayer.itemInUseCount = 0;
    } 
  }
  
  EntityLivingBase findTarget() {
    for (Entity entity : this.mc.theWorld.loadedEntityList) {
      if (entity == null || 
        !isEntityValid(entity) || !(entity instanceof EntityLivingBase))
        continue; 
      return (EntityLivingBase)entity;
    } 
    return null;
  }
  
  boolean isEntityValid(Entity entity) {
    if (this.mc.thePlayer.isEntityEqual(entity))
      return false; 
    if (this.rayCheck) {
      AxisAlignedBB targetBox = entity.getEntityBoundingBox();
      Vec2f rotation = RotationUtil.getRotations(entity);
      Vec3 origin = this.mc.thePlayer.getPositionEyes(1.0F);
      Vec3 look = entity.getVectorForRotation(rotation.y, rotation.x);
      look = origin.addVector(look.xCoord * this.reach, look.yCoord * this.reach, look.zCoord * this.reach);
      MovingObjectPosition collision = targetBox.calculateIntercept(origin, look);
      if (collision == null)
        return false; 
    } else if (this.mc.thePlayer.getDistanceToEntity(entity) >= this.reach) {
      return false;
    } 
    if (Lite.FRIEND_MANAGER.getFriend(entity.getName()) != null)
      return false; 
    if (!this.sleeping && ((EntityLivingBase)entity).isPlayerSleeping())
      return false; 
    if (!this.sleeping && ((EntityLivingBase)entity).isPlayerSleeping())
      return false; 
    if (entity instanceof net.minecraft.entity.item.EntityArmorStand)
      return false; 
    if (entity.isInvisible() && !this.invisible)
      return false; 
    if (!this.monsters && (entity instanceof net.minecraft.entity.monster.EntityMob || entity instanceof net.minecraft.entity.passive.EntityVillager))
      return false; 
    if (!this.deadPlayers && entity.isDead)
      return false; 
    return (this.monsters || entity instanceof EntityPlayer);
  }
  
  void doCritical() {
    Criticals criticals = (Criticals)Lite.MODULE_FACTORY.findByClass(Criticals.class);
    if (criticals.getData().isEnabled())
      criticals.processCriticalHit(); 
    if (this.crack)
      for (int i = 0; i < this.crackSize; i++) {
        if (this.crackType == CrackType.NORMAL)
          this.mc.thePlayer.onCriticalHit((Entity)this.target); 
        if (this.crackType == CrackType.ENCHANT)
          this.mc.thePlayer.onEnchantmentCritical((Entity)this.target); 
      }  
  }
  
  public void swing(Entity target, PlayerMotionEvent playerMotionEvent) {
    if (this.autoBlock)
      useItem(playerMotionEvent); 
    double aps = this.cps + MathUtil.randFloat(MathUtil.randFloat(1.0F, 3.0F), MathUtil.randFloat(3.0F, 5.0F));
    if (this.attackTimer.hasReached(1000.0D / aps)) {
      this.attackTimer.reset();
      if (this.autoBlock && this.unBlockMode == UnBlockMode.ATTACK && this.blocking && this.mc.thePlayer
        .isBlocking()) {
        this.blocking = false;
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
      } 
      if (this.swing)
        this.mc.thePlayer.swingItem(); 
      doCritical();
      if (this.autoBlock && this.unBlockMode == UnBlockMode.ATTACK && this.blocking && this.mc.thePlayer
        .isBlocking()) {
        this.blocking = false;
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
      } 
      this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    } 
  }
  
  void useItem(PlayerMotionEvent event) {
    if (this.autoBlock && 
      this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof net.minecraft.item.ItemSword) {
      ItemStack itemstack;
      this.blocking = true;
      switch (this.blockMode) {
        case MODULO:
          this.mc.playerController.syncCurrentPlayItem();
          itemstack = this.mc.thePlayer.getHeldItem().useItemRightClick((World)this.mc.theWorld, (EntityPlayer)this.mc.thePlayer);
          if (itemstack != this.mc.thePlayer.getHeldItem() || itemstack != null) {
            this.mc.thePlayer.inventory.mainInventory[this.mc.thePlayer.inventory.currentItem] = itemstack;
            if (itemstack.stackSize == 0)
              this.mc.thePlayer.inventory.mainInventory[this.mc.thePlayer.inventory.currentItem] = null; 
          } 
          break;
        case MODULO2:
          this.mc.playerController.syncCurrentPlayItem();
          itemstack = this.mc.thePlayer.getHeldItem().useItemRightClick((World)this.mc.theWorld, (EntityPlayer)this.mc.thePlayer);
          if (this.target instanceof EntityPlayer) {
            this.mc.playerController.interactWithEntitySendPacket((EntityPlayer)this.mc.thePlayer, (Entity)this.target);
            this.target.interactAt((EntityPlayer)this.target, new Vec3(-1.0D, -1.0D, -1.0D));
          } 
          if (itemstack != this.mc.thePlayer.getHeldItem() || itemstack != null) {
            this.mc.thePlayer.inventory.mainInventory[this.mc.thePlayer.inventory.currentItem] = itemstack;
            if (itemstack.stackSize == 0)
              this.mc.thePlayer.inventory.mainInventory[this.mc.thePlayer.inventory.currentItem] = null; 
          } 
          break;
        case ROUND:
          if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof net.minecraft.item.ItemSword)
            this.mc.thePlayer.itemInUseCount = 1; 
          break;
      } 
    } 
  }
  
  public void aimAtTarget(PlayerMotionEvent event, Entity target) {
    Vec2f rotation = getRotations(target);
    if (rotation == null)
      return; 
    if (this.smoothness > 0.0F) {
      if (this.currentRotation == null)
        this.currentRotation = new Vec2f(this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch); 
      float yaw = RotationUtil.updateYawRotation(this.currentRotation.x, rotation.x, 
          Math.max(1.0F, 180.0F * (1.0F - this.smoothness / 100.0F)));
      float pitch = RotationUtil.updatePitchRotation(this.currentRotation.y, rotation.y, 
          Math.max(1.0F, 90.0F * (1.0F - this.smoothness / 100.0F)));
      rotation.x = yaw;
      rotation.y = pitch;
      this.currentRotation = rotation;
    } 
    if (this.roundingType == RoundingType.MINECRAFT)
      rotation = RotationUtil.clampRotation(rotation); 
    event.getPosition().setRotation(rotation);
  }
  
  public Vec2f getRotations(Entity entity) {
    switch (this.rotationType) {
      case MODULO:
        return wrapRotation(RotationUtil.getNormalRotations(entity));
      case MODULO2:
        return wrapRotation(RotationUtil.getRandomizedRotations(entity));
    } 
    return null;
  }
  
  Vec2f wrapRotation(Vec2f vec2f) {
    float sensitivity, f, f2, yaw = vec2f.getVecX();
    float pitch = vec2f.getVecY();
    switch (this.roundingType) {
      case MODULO:
        sensitivity = (Minecraft.getMinecraft()).gameSettings.mouseSensitivity;
        f = sensitivity * 0.6F + 0.2F;
        f2 = f * f * f * 1.2F;
        yaw -= yaw % f2;
        pitch -= pitch % f2;
        break;
      case MODULO2:
        sensitivity = (Minecraft.getMinecraft()).gameSettings.mouseSensitivity;
        f = sensitivity * 0.6F + 0.2F;
        f2 = f * f * f * 1.2F;
        yaw -= yaw % f2 / 4.0F;
        pitch -= pitch % f2 / 4.0F;
        break;
      case ROUND:
        yaw = (float)MathUtil.preciseRound(yaw, 1);
        pitch = (float)MathUtil.preciseRound(pitch, 1);
        break;
    } 
    return new Vec2f(yaw, pitch);
  }
  
  public enum CrackType {
    ENCHANT, NORMAL;
  }
  
  public enum AttackMethod {
    PRE, POST;
  }
  
  public enum RoundingType {
    NONE, MINECRAFT, MODULO, MODULO2, ROUND;
  }
  
  public enum RotationType {
    NORMAL, RANDOM, NONE;
  }
  
  public enum BlockMode {
    NCP, NCP_INTERACT, FAKE;
  }
  
  public enum UnBlockMode {
    SWING, ATTACK;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\aura\Aura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */