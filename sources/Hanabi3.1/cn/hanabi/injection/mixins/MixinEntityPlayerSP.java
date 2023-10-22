package cn.hanabi.injection.mixins;

import cn.hanabi.Wrapper;
import cn.hanabi.events.EventJump;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventPushBlock;
import cn.hanabi.events.EventSafeWalk;
import cn.hanabi.events.EventStep;
import cn.hanabi.events.EventStrafe;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.injection.interfaces.IEntity;
import cn.hanabi.injection.interfaces.IEntityPlayerSP;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.combat.KeepSprint;
import cn.hanabi.modules.modules.ghost.Hitbox;
import cn.hanabi.modules.modules.movement.NoSlow;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityPlayerSP.class})
public class MixinEntityPlayerSP extends AbstractClientPlayer implements IEntityPlayerSP {
   private final int cacheSprintToggleTimer = 0;
   private final float cacheYaw = 0.0F;
   private final float cachePitch = 0.0F;
   @Shadow
   public MovementInput movementInput;
   @Shadow
   @Final
   public NetHandlerPlayClient sendQueue;
   @Shadow
   protected int sprintToggleTimer;
   private double cachedX;
   private double cachedY;
   private double cachedZ;
   private float cachedRotationPitch;
   private float cachedRotationYaw;
   private float cacheStrafe = 0.0F;
   private float cacheForward = 0.0F;
   @Shadow
   private boolean serverSprintState;
   @Shadow
   private boolean serverSneakState;
   @Shadow
   private double lastReportedPosX;
   @Shadow
   private double lastReportedPosY;
   @Shadow
   private double lastReportedPosZ;
   @Shadow
   private float lastReportedYaw;
   @Shadow
   private float lastReportedPitch;
   @Shadow
   private int positionUpdateTicks;

   public MixinEntityPlayerSP() {
      super((World)null, (GameProfile)null);
   }

   @Inject(
      method = {"onUpdate"},
      at = {@At("HEAD")}
   )
   public void eventUpdate(CallbackInfo info) {
      if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
         EventUpdate event = new EventUpdate();
         EventManager.call(event);
      }

   }

   @Overwrite
   public void onUpdateWalkingPlayer() {
      boolean flag = this.isSprinting();
      EventPreMotion pre = new EventPreMotion(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
      EventManager.call(pre);
      if (pre.cancel) {
         EventPostMotion post = new EventPostMotion(pre.pitch);
         EventManager.call(post);
      } else {
         if (flag != this.serverSprintState) {
            if (Wrapper.canSendMotionPacket) {
               if (flag) {
                  this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, Action.START_SPRINTING));
               } else {
                  this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, Action.STOP_SPRINTING));
               }
            }

            this.serverSprintState = flag;
         }

         boolean flag1 = this.isSneaking();
         if (flag1 != this.serverSneakState) {
            if (Wrapper.canSendMotionPacket) {
               if (flag1) {
                  this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, Action.START_SNEAKING));
               } else {
                  this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, Action.STOP_SNEAKING));
               }
            }

            this.serverSneakState = flag1;
         }

         if (this.isCurrentViewEntity()) {
            double x = pre.getX();
            double y = pre.getY();
            double z = pre.getZ();
            float yaw = pre.getYaw();
            float pitch = pre.getPitch();
            boolean ground = pre.isOnGround();
            double d0 = x - this.lastReportedPosX;
            double d1 = y - this.lastReportedPosY;
            double d2 = z - this.lastReportedPosZ;
            double d3 = (double)(yaw - this.lastReportedYaw);
            double d4 = (double)(pitch - this.lastReportedPitch);
            boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4 || this.positionUpdateTicks >= 20;
            boolean flag3 = d3 != 0.0 || d4 != 0.0;
            if (!pre.isCancelled()) {
               if (flag2 && flag3) {
                  this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, yaw, pitch, ground));
               } else if (flag2) {
                  this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, ground));
               } else if (flag3) {
                  this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, ground));
               } else if (!ModManager.getModule("LessPacket").isEnabled()) {
                  this.sendQueue.addToSendQueue(new C03PacketPlayer(ground));
               }
            }

            ++this.positionUpdateTicks;
            if (flag2) {
               this.lastReportedPosX = x;
               this.lastReportedPosY = y;
               this.lastReportedPosZ = z;
               this.positionUpdateTicks = 0;
            }

            if (flag3) {
               this.lastReportedYaw = yaw;
               this.lastReportedPitch = pitch;
            }

            EventPostMotion post2 = new EventPostMotion(pre.pitch);
            EventManager.call(post2);
         }

      }
   }

   @Inject(
      method = {"onLivingUpdate"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/MovementInput;updatePlayerMoveState()V",
   shift = At.Shift.AFTER
)}
   )
   private void onNoSlowEnable(CallbackInfo callbackInfo) {
      if (!this.isSlow()) {
         if (((NoSlow)ModManager.getModule(NoSlow.class)).isEnabled()) {
            this.cacheStrafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
            this.cacheForward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
         }

      }
   }

   @Inject(
      method = {"onLivingUpdate"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/entity/EntityPlayerSP;pushOutOfBlocks(DDD)Z",
   shift = At.Shift.BEFORE
)}
   )
   public void onToggledTimerZero(CallbackInfo callbackInfo) {
      if (!this.isSlow()) {
         if (((NoSlow)ModManager.getModule(NoSlow.class)).isEnabled()) {
            Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe = this.cacheStrafe;
            Minecraft.getMinecraft().thePlayer.movementInput.moveForward = this.cacheForward;
         }

      }
   }

   public final boolean isSlow() {
      return !Minecraft.getMinecraft().thePlayer.isUsingItem() || Minecraft.getMinecraft().thePlayer.isRiding();
   }

   @Shadow
   protected void sendHorseJump() {
   }

   @Shadow
   protected boolean isCurrentViewEntity() {
      return false;
   }

   @Shadow
   private boolean isHeadspaceFree(BlockPos pos, int height) {
      return false;
   }

   @Overwrite
   protected boolean pushOutOfBlocks(double x, double y, double z) {
      EventPushBlock event = new EventPushBlock();
      EventManager.call(event);
      if (!this.noClip && !event.isCancelled()) {
         BlockPos blockpos = new BlockPos(x, y, z);
         double d0 = x - (double)blockpos.getX();
         double d1 = z - (double)blockpos.getZ();
         int entHeight = Math.max((int)Math.ceil((double)this.height), 1);
         boolean inTranslucentBlock = !this.isHeadspaceFree(blockpos, entHeight);
         if (inTranslucentBlock) {
            int i = -1;
            double d2 = 9999.0;
            if (this.isHeadspaceFree(blockpos.west(), entHeight) && d0 < d2) {
               d2 = d0;
               i = 0;
            }

            if (this.isHeadspaceFree(blockpos.east(), entHeight) && 1.0 - d0 < d2) {
               d2 = 1.0 - d0;
               i = 1;
            }

            if (this.isHeadspaceFree(blockpos.north(), entHeight) && d1 < d2) {
               d2 = d1;
               i = 4;
            }

            if (this.isHeadspaceFree(blockpos.south(), entHeight) && 1.0 - d1 < d2) {
               d2 = 1.0 - d1;
               i = 5;
            }

            float f = 0.1F;
            if (i == 0) {
               this.motionX = (double)(-f);
            }

            if (i == 1) {
               this.motionX = (double)f;
            }

            if (i == 4) {
               this.motionZ = (double)(-f);
            }

            if (i == 5) {
               this.motionZ = (double)f;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
      if (ForgeHooks.onPlayerAttackTarget(this, targetEntity)) {
         if (targetEntity.canAttackWithItem() && !targetEntity.hitByEntity(this)) {
            float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
            int i = 0;
            float f1 = 0.0F;
            if (targetEntity instanceof EntityLivingBase) {
               f1 = EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
            } else {
               f1 = EnchantmentHelper.func_152377_a(this.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
            }

            i += EnchantmentHelper.getKnockbackModifier(this);
            if (this.isSprinting()) {
               ++i;
            }

            if (f > 0.0F || f1 > 0.0F) {
               boolean flag = this.fallDistance > 0.0F && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(Potion.blindness) && this.ridingEntity == null && targetEntity instanceof EntityLivingBase;
               if (flag && f > 0.0F) {
                  f *= 1.5F;
               }

               f += f1;
               boolean flag1 = false;
               int j = EnchantmentHelper.getFireAspectModifier(this);
               if (targetEntity instanceof EntityLivingBase && j > 0 && !targetEntity.isBurning()) {
                  flag1 = true;
                  targetEntity.setFire(1);
               }

               double d0 = targetEntity.motionX;
               double d1 = targetEntity.motionY;
               double d2 = targetEntity.motionZ;
               boolean flag2 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), f);
               if (flag2) {
                  if (i > 0) {
                     targetEntity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F), 0.1, (double)(MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F));
                     if (!((KeepSprint)ModManager.getModule(KeepSprint.class)).getState()) {
                        this.motionX *= 0.6;
                        this.motionZ *= 0.6;
                        this.setSprinting(false);
                     }
                  }

                  if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
                     ((EntityPlayerMP)targetEntity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(targetEntity));
                     targetEntity.velocityChanged = false;
                     targetEntity.motionX = d0;
                     targetEntity.motionY = d1;
                     targetEntity.motionZ = d2;
                  }

                  if (flag) {
                     this.onCriticalHit(targetEntity);
                  }

                  if (f1 > 0.0F) {
                     this.onEnchantmentCritical(targetEntity);
                  }

                  if (f >= 18.0F) {
                     this.triggerAchievement(AchievementList.overkill);
                  }

                  this.setLastAttacker(targetEntity);
                  if (targetEntity instanceof EntityLivingBase) {
                     EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, this);
                  }

                  EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
                  ItemStack itemstack = this.getCurrentEquippedItem();
                  Entity entity = targetEntity;
                  if (targetEntity instanceof EntityDragonPart) {
                     IEntityMultiPart ientitymultipart = ((EntityDragonPart)targetEntity).entityDragonObj;
                     if (ientitymultipart instanceof EntityLivingBase) {
                        entity = (EntityLivingBase)ientitymultipart;
                     }
                  }

                  if (itemstack != null && entity instanceof EntityLivingBase) {
                     itemstack.hitEntity((EntityLivingBase)entity, this);
                     if (itemstack.stackSize <= 0) {
                        this.destroyCurrentEquippedItem();
                     }
                  }

                  if (targetEntity instanceof EntityLivingBase) {
                     this.addStat(StatList.damageDealtStat, Math.round(f * 10.0F));
                     if (j > 0) {
                        targetEntity.setFire(j * 4);
                     }
                  }

                  this.addExhaustion(0.3F);
               } else if (flag1) {
                  targetEntity.extinguish();
               }
            }
         }

      }
   }

   public float getCollisionBorderSize() {
      Hitbox hitBox = (Hitbox)ModManager.getModule(Hitbox.class);
      return hitBox.isEnabled() ? Hitbox.getSize() : super.getCollisionBorderSize();
   }

   public void moveFlying(float strafe, float forward, float friction) {
      float yaw = this.rotationYaw;
      EventStrafe event = new EventStrafe(forward, strafe, friction, yaw);
      EventManager.call(event);
      if (!event.cancelled) {
         strafe = event.strafe;
         forward = event.forward;
         friction = event.friction;
         yaw = event.yaw;
         float f = strafe * strafe + forward * forward;
         if (f >= 1.0E-4F) {
            f = MathHelper.sqrt_float(f);
            if (f < 1.0F) {
               f = 1.0F;
            }

            f = friction / f;
            strafe *= f;
            forward *= f;
            float f1 = MathHelper.sin(yaw * 3.1415927F / 180.0F);
            float f2 = MathHelper.cos(yaw * 3.1415927F / 180.0F);
            this.motionX += (double)(strafe * f2 - forward * f1);
            this.motionZ += (double)(forward * f2 + strafe * f1);
         }

      }
   }

   public void moveEntity(double x, double y, double z) {
      EventMove event = new EventMove(x, y, z);
      EventManager.call(event);
      this.onMoveEntity(event.getX(), event.getY(), event.getZ());
   }

   public void onMoveEntity(double x, double y, double z) {
      if (this.noClip) {
         this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
         this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
         this.posY = this.getEntityBoundingBox().minY;
         this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;
      } else {
         this.worldObj.theProfiler.startSection("move");
         double d0 = this.posX;
         double d1 = this.posY;
         double d2 = this.posZ;
         if (this.isInWeb) {
            this.isInWeb = false;
            x *= 0.25;
            y *= 0.05000000074505806;
            z *= 0.25;
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
         }

         EventSafeWalk event = new EventSafeWalk(false);
         EventManager.call(event);
         double d3 = x;
         double d4 = y;
         double d5 = z;
         boolean flag = this.onGround && this.isSneaking() && !ModManager.getModule("Scaffold").isEnabled() && this instanceof EntityPlayer;
         boolean safeMode = this.onGround && event.getSafe() && this instanceof EntityPlayer;
         if (flag || safeMode) {
            double d6;
            for(d6 = 0.05; x != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0, 0.0)).isEmpty(); d3 = x) {
               if (x < d6 && x >= -d6) {
                  x = 0.0;
               } else if (x > 0.0) {
                  x -= d6;
               } else {
                  x += d6;
               }
            }

            for(; z != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0, -1.0, z)).isEmpty(); d5 = z) {
               if (z < d6 && z >= -d6) {
                  z = 0.0;
               } else if (z > 0.0) {
                  z -= d6;
               } else {
                  z += d6;
               }
            }

            for(; x != 0.0 && z != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0, z)).isEmpty(); d5 = z) {
               if (x < d6 && x >= -d6) {
                  x = 0.0;
               } else if (x > 0.0) {
                  x -= d6;
               } else {
                  x += d6;
               }

               d3 = x;
               if (z < d6 && z >= -d6) {
                  z = 0.0;
               } else if (z > 0.0) {
                  z -= d6;
               } else {
                  z += d6;
               }
            }
         }

         List list1 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
         AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();

         AxisAlignedBB axisalignedbb1;
         for(Iterator var24 = list1.iterator(); var24.hasNext(); y = axisalignedbb1.calculateYOffset(this.getEntityBoundingBox(), y)) {
            axisalignedbb1 = (AxisAlignedBB)var24.next();
         }

         this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
         boolean flag1 = this.onGround || d4 != y && d4 < 0.0;

         AxisAlignedBB axisalignedbb13;
         Iterator var58;
         for(var58 = list1.iterator(); var58.hasNext(); x = axisalignedbb13.calculateXOffset(this.getEntityBoundingBox(), x)) {
            axisalignedbb13 = (AxisAlignedBB)var58.next();
         }

         this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0, 0.0));

         for(var58 = list1.iterator(); var58.hasNext(); z = axisalignedbb13.calculateZOffset(this.getEntityBoundingBox(), z)) {
            axisalignedbb13 = (AxisAlignedBB)var58.next();
         }

         this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, 0.0, z));
         double d8;
         if (this.stepHeight > 0.0F && flag1 && (d3 != x || d5 != z)) {
            EventStep es = new EventStep(EventType.PRE, this.stepHeight);
            EventManager.call(es);
            double d11 = x;
            double d7 = y;
            d8 = z;
            AxisAlignedBB axisalignedbb3 = this.getEntityBoundingBox();
            this.setEntityBoundingBox(axisalignedbb);
            y = (double)this.stepHeight;
            List list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(d3, y, d5));
            AxisAlignedBB axisalignedbb4 = this.getEntityBoundingBox();
            AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(d3, 0.0, d5);
            double d9 = y;

            AxisAlignedBB axisalignedbb6;
            for(Iterator var38 = list.iterator(); var38.hasNext(); d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9)) {
               axisalignedbb6 = (AxisAlignedBB)var38.next();
            }

            axisalignedbb4 = axisalignedbb4.offset(0.0, d9, 0.0);
            double d15 = d3;

            AxisAlignedBB axisalignedbb7;
            for(Iterator var40 = list.iterator(); var40.hasNext(); d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15)) {
               axisalignedbb7 = (AxisAlignedBB)var40.next();
            }

            axisalignedbb4 = axisalignedbb4.offset(d15, 0.0, 0.0);
            double d16 = d5;

            AxisAlignedBB axisalignedbb8;
            for(Iterator var42 = list.iterator(); var42.hasNext(); d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16)) {
               axisalignedbb8 = (AxisAlignedBB)var42.next();
            }

            axisalignedbb4 = axisalignedbb4.offset(0.0, 0.0, d16);
            AxisAlignedBB axisalignedbb14 = this.getEntityBoundingBox();
            double d17 = y;

            AxisAlignedBB axisalignedbb9;
            for(Iterator var45 = list.iterator(); var45.hasNext(); d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17)) {
               axisalignedbb9 = (AxisAlignedBB)var45.next();
            }

            axisalignedbb14 = axisalignedbb14.offset(0.0, d17, 0.0);
            double d18 = d3;

            AxisAlignedBB axisalignedbb10;
            for(Iterator var47 = list.iterator(); var47.hasNext(); d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18)) {
               axisalignedbb10 = (AxisAlignedBB)var47.next();
            }

            axisalignedbb14 = axisalignedbb14.offset(d18, 0.0, 0.0);
            double d19 = d5;

            AxisAlignedBB axisalignedbb11;
            for(Iterator var49 = list.iterator(); var49.hasNext(); d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19)) {
               axisalignedbb11 = (AxisAlignedBB)var49.next();
            }

            axisalignedbb14 = axisalignedbb14.offset(0.0, 0.0, d19);
            double d20 = d15 * d15 + d16 * d16;
            double d10 = d18 * d18 + d19 * d19;
            if (d20 > d10) {
               x = d15;
               z = d16;
               y = -d9;
               this.setEntityBoundingBox(axisalignedbb4);
            } else {
               x = d18;
               z = d19;
               y = -d17;
               this.setEntityBoundingBox(axisalignedbb14);
            }

            AxisAlignedBB axisalignedbb12;
            for(Iterator var53 = list.iterator(); var53.hasNext(); y = axisalignedbb12.calculateYOffset(this.getEntityBoundingBox(), y)) {
               axisalignedbb12 = (AxisAlignedBB)var53.next();
            }

            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
            if (d11 * d11 + d8 * d8 >= x * x + z * z) {
               x = d11;
               y = d7;
               z = d8;
               this.setEntityBoundingBox(axisalignedbb3);
            } else {
               EventStep post = new EventStep(EventType.POST, this.stepHeight);
               EventManager.call(post);
            }
         }

         this.worldObj.theProfiler.endSection();
         this.worldObj.theProfiler.startSection("rest");
         this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
         this.posY = this.getEntityBoundingBox().minY;
         this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;
         this.isCollidedHorizontally = d3 != x || d5 != z;
         this.isCollidedVertically = d4 != y;
         this.onGround = this.isCollidedVertically && d4 < 0.0;
         this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
         int i = MathHelper.floor_double(this.posX);
         int j = MathHelper.floor_double(this.posY - 0.20000000298023224);
         int k = MathHelper.floor_double(this.posZ);
         BlockPos blockpos = new BlockPos(i, j, k);
         Block block1 = this.worldObj.getBlockState(blockpos).getBlock();
         if (block1.getMaterial() == Material.air) {
            Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();
            if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate) {
               block1 = block;
               blockpos = blockpos.down();
            }
         }

         this.updateFallState(y, this.onGround, block1, blockpos);
         if (d3 != x) {
            this.motionX = 0.0;
         }

         if (d5 != z) {
            this.motionZ = 0.0;
         }

         if (d4 != y) {
            block1.onLanded(this.worldObj, this);
         }

         if (this.canTriggerWalking() && !flag && this.ridingEntity == null) {
            d8 = this.posX - d0;
            double d13 = this.posY - d1;
            double d14 = this.posZ - d2;
            if (block1 != Blocks.ladder) {
               d13 = 0.0;
            }

            if (block1 != null && this.onGround) {
               block1.onEntityCollidedWithBlock(this.worldObj, blockpos, this);
            }

            this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d8 * d8 + d14 * d14) * 0.6);
            this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt_double(d8 * d8 + d13 * d13 + d14 * d14) * 0.6);
            if (this.distanceWalkedOnStepModified > (float)((IEntity)this).getNextStepDistance() && block1.getMaterial() != Material.air) {
               ((IEntity)this).setNextStepDistance((int)this.distanceWalkedOnStepModified + 1);
               if (this.isInWater()) {
                  float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224 + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224) * 0.35F;
                  if (f > 1.0F) {
                     f = 1.0F;
                  }

                  this.playSound(this.getSwimSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
               }

               this.playStepSound(blockpos, block1);
            }
         }

         try {
            this.doBlockCollisions();
         } catch (Throwable var55) {
            CrashReport crashreport = CrashReport.makeCrashReport(var55, "Checking entity block collision");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
         }

         boolean flag2 = this.isWet();
         IEntity ent = (IEntity)this;
         if (this.worldObj.isFlammableWithin(this.getEntityBoundingBox().contract(0.001, 0.001, 0.001))) {
            this.dealFireDamage(1);
            if (!flag2) {
               ent.setFire(ent.getFire() + 1);
               if (ent.getFire() == 0) {
                  this.setFire(8);
               }
            }
         } else if (ent.getFire() <= 0) {
            ent.setFire(-this.fireResistance);
         }

         if (flag2 && ent.getFire() > 0) {
            this.playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
            ent.setFire(-this.fireResistance);
         }

         this.worldObj.theProfiler.endSection();
      }

   }

   public void jump() {
      EventJump jumpEvent = new EventJump(this.rotationYaw);
      EventManager.call(jumpEvent);
      if (!jumpEvent.isCancelled()) {
         this.motionY = (double)this.getJumpUpwardsMotion();
         if (this.isPotionActive(Potion.jump)) {
            this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
         }

         if (this.isSprinting()) {
            float f = jumpEvent.yaw * 0.017453292F;
            this.motionX -= (double)(MathHelper.sin(f) * 0.2F);
            this.motionZ += (double)(MathHelper.cos(f) * 0.2F);
         }

         this.isAirBorne = true;
         this.triggerAchievement(StatList.jumpStat);
      }
   }

   public boolean moving() {
      return (double)this.moveForward > 0.0 | (double)this.moveStrafing > 0.0;
   }

   public float getSpeed() {
      return (float)Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
   }

   public void setSpeed(double speed) {
      this.motionX = (double)(-MathHelper.sin(this.getDirection())) * speed;
      this.motionZ = (double)MathHelper.cos(this.getDirection()) * speed;
   }

   public float getDirection() {
      float yaw = this.rotationYaw;
      float forward = this.moveForward;
      float strafe = this.moveStrafing;
      yaw += (float)(forward < 0.0F ? 180 : 0);
      if (strafe < 0.0F) {
         yaw += forward < 0.0F ? -45.0F : (forward == 0.0F ? 90.0F : 45.0F);
      }

      if (strafe > 0.0F) {
         yaw -= forward < 0.0F ? -45.0F : (forward == 0.0F ? 90.0F : 45.0F);
      }

      return yaw * 0.017453292F;
   }

   public void setYaw(double yaw) {
      this.rotationYaw = (float)yaw;
   }

   public void setPitch(double pitch) {
      this.rotationPitch = (float)pitch;
   }

   public void setMoveSpeed(EventMove event, double speed) {
      double forward = (double)this.movementInput.moveForward;
      double strafe = (double)this.movementInput.moveStrafe;
      float yaw = this.rotationYaw;
      if (forward == 0.0 && strafe == 0.0) {
         event.setX(0.0);
         event.setZ(0.0);
      } else {
         if (forward != 0.0) {
            if (strafe > 0.0) {
               yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
               yaw += (float)(forward > 0.0 ? 45 : -45);
            }

            strafe = 0.0;
            if (forward > 0.0) {
               forward = 1.0;
            } else if (forward < 0.0) {
               forward = -1.0;
            }
         }

         event.setX(forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))));
         event.setZ(forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))));
      }

   }

   public void setLastReportedPosY(double f) {
      this.lastReportedPosY = f;
   }
}
