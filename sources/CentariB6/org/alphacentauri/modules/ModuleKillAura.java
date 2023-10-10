package org.alphacentauri.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.data.TargetPriority;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventAttack;
import org.alphacentauri.management.events.EventIsBot;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSent;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.LatencyUtils;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.management.util.RaycastUtils;
import org.alphacentauri.management.util.RotationUtils;
import org.alphacentauri.management.util.Timer;
import org.alphacentauri.modules.ModuleTeam;

public class ModuleKillAura extends Module implements EventListener {
   private static ModuleKillAura instance;
   private Property targetPriority = new Property(this, "TargetPriority", TargetPriority.FOVRotated);
   private Property hitRandomization = new Property(this, "Randomization", ModuleKillAura.RandomMode.NonRandom);
   private Property reach = new Property(this, "Reach", Double.valueOf(4.4D));
   private Property speed = new Property(this, "Speed", Double.valueOf(7.0D));
   private Property yawRate = new Property(this, "YawRate", Float.valueOf(40.0F));
   private Property entityRaycast = new Property(this, "EntityRaycast", Boolean.valueOf(false));
   private Property blockRaycast = new Property(this, "BlockRaycast", Boolean.valueOf(false));
   private Property autoBlock = new Property(this, "AutoBlock", Boolean.valueOf(true));
   private Property attackNeedsUnblock = new Property(this, "AttackNeedsUnblock", Boolean.valueOf(true));
   private Property fov = new Property(this, "FOV", Double.valueOf(360.0D));
   private Property multi = new Property(this, "AttackMode", ModuleKillAura.AttackMode.Single);
   private Property rot = new Property(this, "RotateMode", ModuleKillAura.RotateMode.NONE);
   private Property rdmhitmiss = new Property(this, "RandomHitMiss", Boolean.valueOf(false));
   private Property targetPlayers = new Property(this, "TargetPlayers", Boolean.valueOf(true));
   private Property targetPlayersInv = new Property(this, "TargetPlayersInv", Boolean.valueOf(true));
   private Property targetMobs = new Property(this, "TargetMobs", Boolean.valueOf(true));
   private Property targetMobsInv = new Property(this, "TargetMobsInv", Boolean.valueOf(true));
   private Property targetAnimals = new Property(this, "TargetAnimals", Boolean.valueOf(true));
   private Property targetAnimalsInv = new Property(this, "TargetAnimalsInv", Boolean.valueOf(true));
   private Property targetNPC = new Property(this, "TargetNPC", Boolean.valueOf(true));
   private Property targetNPCInv = new Property(this, "TargetNPCInv", Boolean.valueOf(true));
   public ArrayList targets = new ArrayList();
   private EntityLivingBase target;
   private boolean updatingTargets = false;
   private boolean updatingTarget = false;
   private boolean hit = false;
   private Timer hitSpeedTimer = new Timer();
   private Timer fakeRandomTimer = new Timer();
   private boolean fakeRandomBool = false;

   public ModuleKillAura() {
      super("KillAura", "Kills everything", new String[]{"killaura", "aura", "alphaaura"}, Module.Category.Combat, 12668469);
      instance = this;
   }

   public static ModuleKillAura getInstance() {
      return instance;
   }

   private boolean rotate(EntityPlayerSP player, EntityLivingBase target) {
      return this.rot.value != ModuleKillAura.RotateMode.NCP2 && this.rot.value != ModuleKillAura.RotateMode.NCP1?(this.rot.value == ModuleKillAura.RotateMode.NONE?true:(this.rot.value == ModuleKillAura.RotateMode.AAC?this.rotateAAC(player, target):false)):this.rotateNCP(player, target);
   }

   public void updatePossibleTargets() {
      if(!this.updatingTargets) {
         this.updatingTargets = true;
         AC.getThreadPool().execute(() -> {
            try {
               List<Entity> loadedEntityList = AC.getMC().getWorld().loadedEntityList;
               List<Entity> entityList = new ArrayList(loadedEntityList.size());
               entityList.addAll(loadedEntityList);
               ArrayList<EntityLivingBase> targetable = (ArrayList)entityList.stream().filter((entity) -> {
                  return entity instanceof EntityLivingBase;
               }).map((entity) -> {
                  return (EntityLivingBase)entity;
               }).collect(Collectors.toCollection(ArrayList::<init>));
               ArrayList<EntityLivingBase> selected_targets = new ArrayList();

               for(EntityLivingBase base : targetable) {
                  if((this.multi.value != ModuleKillAura.AttackMode.HurtTime || base.hurtTime <= 4) && !((EventIsBot)(new EventIsBot(base)).fire()).isBot()) {
                     if(base instanceof EntityPlayer && !(base instanceof EntityPlayerSP)) {
                        if(!AC.getFriendManager().isFriend(base.getName()) && !ModuleTeam.isinTeam((EntityPlayer)base)) {
                           if(base.isInvisible()) {
                              if(((Boolean)this.targetPlayersInv.value).booleanValue()) {
                                 selected_targets.add(base);
                              }
                           } else if(((Boolean)this.targetPlayers.value).booleanValue()) {
                              selected_targets.add(base);
                           }
                        }
                     } else if(!(base instanceof EntityMob) && !(base instanceof EntitySlime)) {
                        if(base instanceof EntityAnimal) {
                           if(base.isInvisible()) {
                              if(((Boolean)this.targetAnimalsInv.value).booleanValue()) {
                                 selected_targets.add(base);
                              }
                           } else if(((Boolean)this.targetAnimals.value).booleanValue()) {
                              selected_targets.add(base);
                           }
                        } else if(base instanceof EntityVillager) {
                           if(base.isInvisible()) {
                              if(((Boolean)this.targetNPCInv.value).booleanValue()) {
                                 selected_targets.add(base);
                              }
                           } else if(((Boolean)this.targetNPC.value).booleanValue()) {
                              selected_targets.add(base);
                           }
                        }
                     } else if(base.isInvisible()) {
                        if(((Boolean)this.targetMobsInv.value).booleanValue()) {
                           selected_targets.add(base);
                        }
                     } else if(((Boolean)this.targetMobs.value).booleanValue()) {
                        selected_targets.add(base);
                     }
                  }
               }

               this.targets = selected_targets;
            } catch (Exception var7) {
               ;
            }

            this.updatingTargets = false;
         });
      }
   }

   public float getDistFromMouseSq(EntityLivingBase entity) {
      EntityPlayerSP player = AC.getMC().getPlayer();
      double diffX = entity.posX - player.posX;
      double diffY = entity.posY + (double)entity.getEyeHeight() * 0.9D - (player.posY + (double)player.getEyeHeight());
      double diffZ = entity.posZ - player.posZ;
      double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
      float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
      float[] neededRotations = new float[]{player.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - player.rotationYaw), player.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - player.rotationPitch)};
      float neededYaw = player.rotationYaw - neededRotations[0];
      float neededPitch = player.rotationPitch - neededRotations[1];
      return neededYaw * neededYaw + neededPitch * neededPitch;
   }

   public void setEnabledSilent(boolean enabled) {
      this.hitSpeedTimer.reset();
      super.setEnabledSilent(enabled);
   }

   public void setBypass(AntiCheat ac) {
      super.setBypass(ac);
      if(ac == AntiCheat.NCP) {
         this.yawRate.value = Float.valueOf(40.0F);
         this.entityRaycast.value = Boolean.valueOf(false);
         this.blockRaycast.value = Boolean.valueOf(false);
         this.hitRandomization.value = ModuleKillAura.RandomMode.NonRandom;
         this.rot.value = ModuleKillAura.RotateMode.NCP1;
      } else if(ac == AntiCheat.Spartan) {
         this.yawRate.value = Float.valueOf(75.0F);
         this.entityRaycast.value = Boolean.valueOf(false);
         this.blockRaycast.value = Boolean.valueOf(true);
         this.rot.value = ModuleKillAura.RotateMode.NCP1;
      } else if(ac == AntiCheat.Reflex) {
         this.yawRate.value = Float.valueOf(40.0F);
         this.entityRaycast.value = Boolean.valueOf(true);
         this.blockRaycast.value = Boolean.valueOf(false);
         this.rot.value = ModuleKillAura.RotateMode.NCP2;
      } else if(ac == AntiCheat.AAC) {
         this.yawRate.value = Float.valueOf(38.0F);
         this.entityRaycast.value = Boolean.valueOf(true);
         this.blockRaycast.value = Boolean.valueOf(false);
         this.hitRandomization.value = ModuleKillAura.RandomMode.FakeRandom;
         this.rot.value = ModuleKillAura.RotateMode.AAC;
      }

   }

   private void attack(EntityPlayerSP player, EntityLivingBase target) {
      if(target != null) {
         MovingObjectPosition ray = null;
         if(((Boolean)this.blockRaycast.value).booleanValue() || ((Boolean)this.entityRaycast.value).booleanValue()) {
            ray = RaycastUtils.rayCast(player, target.posX, target.posY + (double)target.getEyeHeight() * 0.7D, target.posZ);
         }

         if(!((Boolean)this.blockRaycast.value).booleanValue() || player.canEntityBeSeen(target)) {
            if(((Boolean)this.entityRaycast.value).booleanValue() && ray != null) {
               Entity entityHit = ray.entityHit;
               if(entityHit instanceof EntityLivingBase) {
                  target = (EntityLivingBase)entityHit;
                  if(target instanceof EntityPlayer && AC.getFriendManager().isFriend(target.getName())) {
                     return;
                  }
               }
            }

            boolean blocking = player.isBlocking() && ((Boolean)this.attackNeedsUnblock.value).booleanValue();
            if(blocking) {
               player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            } else if(player.isUsingItem() && ((Boolean)this.attackNeedsUnblock.value).booleanValue()) {
               return;
            }

            if(!((EventAttack)(new EventAttack(target, false)).fire()).isCancelled() && (!((Boolean)this.rdmhitmiss.value).booleanValue() || AC.getRandom().nextBoolean())) {
               player.sendQueue.addToSendQueue(new C02PacketUseEntity(target, net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK));
            }

            player.swingItem();
            if(blocking) {
               player.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(player.getHeldItem()));
            }

         }
      }
   }

   private void doHit() {
      EntityPlayerSP player = AC.getMC().getPlayer();
      if(this.hitRandomization.value == ModuleKillAura.RandomMode.NonRandom) {
         int hitDelay = (int)(1000.0D / ((Double)this.speed.value).doubleValue());

         while(this.hitSpeedTimer.hasMSPassed((long)hitDelay)) {
            this.hitSpeedTimer.subtract(hitDelay);
            if(this.hit) {
               if(this.canHit(player, this.target)) {
                  this.attack(player, this.target);
               }

               this.hit = false;
            }
         }
      } else if(this.hitRandomization.value == ModuleKillAura.RandomMode.TrueRandom) {
         if((double)AC.getRandom().nextFloat() < ((Double)this.speed.value).doubleValue() / 20.0D && this.hit) {
            if(this.canHit(player, this.target)) {
               this.attack(player, this.target);
            }

            this.hit = false;
         }

         int hitDelay = (int)(1000.0D / ((Double)this.speed.value).doubleValue());

         while(this.hitSpeedTimer.hasMSPassed((long)hitDelay)) {
            this.hitSpeedTimer.subtract(hitDelay);
         }
      } else if(this.hitRandomization.value == ModuleKillAura.RandomMode.FakeRandom) {
         if(this.fakeRandomTimer.hasMSPassed(1000L)) {
            this.fakeRandomBool = !this.fakeRandomBool;
            this.fakeRandomTimer.reset();
         }

         double speed = this.fakeRandomBool?((Double)this.speed.value).doubleValue():((Double)this.speed.value).doubleValue() - 1.0D;
         int hitDelay = (int)(1000.0D / speed);

         while(this.hitSpeedTimer.hasMSPassed((long)hitDelay)) {
            this.hitSpeedTimer.subtract(hitDelay);
            if(this.hit) {
               if(this.canHit(player, this.target)) {
                  this.attack(player, this.target);
               }

               this.hit = false;
            }
         }
      }

   }

   private boolean rotateNCP(EntityPlayerSP player, EntityLivingBase target) {
      double diffX = target.posX - player.posX;
      double diffY = target.posY + (double)target.getEyeHeight() * 0.9D - (player.posY + (double)player.getEyeHeight());
      double diffZ = target.posZ - player.posZ;
      double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
      float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
      float[] neededRotations = new float[]{(RotationUtils.isSet()?RotationUtils.getYaw():player.rotationYaw) + MathHelper.wrapAngleTo180_float(yaw - (RotationUtils.isSet()?RotationUtils.getYaw():player.rotationYaw)), (RotationUtils.isSet()?RotationUtils.getPitch():player.rotationPitch) + MathHelper.wrapAngleTo180_float(pitch - (RotationUtils.isSet()?RotationUtils.getPitch():player.rotationPitch))};
      float[] rlyneed = (float[])neededRotations.clone();
      float d0 = 0.0F - (RotationUtils.isSet()?RotationUtils.getLastYaw():player.rotationYaw);
      float d0y = neededRotations[0] + d0;
      boolean rotateRight = d0y > 0.0F;
      if(rotateRight) {
         neededRotations[0] = (RotationUtils.isSet()?RotationUtils.getLastYaw():player.rotationYaw) + Math.min(Math.abs(0.0F - d0y), ((Float)this.yawRate.value).floatValue());
      } else {
         neededRotations[0] = (RotationUtils.isSet()?RotationUtils.getLastYaw():player.rotationYaw) - Math.min(Math.abs(0.0F - d0y), ((Float)this.yawRate.value).floatValue());
      }

      RotationUtils.set(this, neededRotations[0], neededRotations[1]);
      return neededRotations[0] == rlyneed[0] && neededRotations[1] == rlyneed[1];
   }

   private boolean canHit(EntityPlayerSP player, EntityLivingBase target) {
      return target == null?false:((this.getBypass() == AntiCheat.AAC || this.getBypass() == AntiCheat.Spartan) && player.getHealth() <= 0.0F?false:(this.getBypass() == AntiCheat.Spartan && AC.getMC().currentScreen != null?false:target.getHealth() > 0.0F && target.getDistanceSqToEntity(player) <= ((Double)this.reach.value).doubleValue() * ((Double)this.reach.value).doubleValue() && (((Double)this.fov.value).doubleValue() >= 360.0D || (double)this.getDistFromMouseSq(target) < ((Double)this.fov.value).doubleValue() * ((Double)this.fov.value).doubleValue()) && (!player.isRiding() || player.ridingEntity.getEntityId() != target.getEntityId())));
   }

   private void updateTarget() {
      if(!this.updatingTarget) {
         this.updatingTarget = true;
         AC.getThreadPool().execute(() -> {
            try {
               ArrayList<EntityLivingBase> toCopy = this.targets;
               ArrayList<EntityLivingBase> targets = new ArrayList();
               EntityPlayerSP player = AC.getMC().getPlayer();
               targets.addAll(toCopy);
               if(this.targetPriority.value == TargetPriority.Dist) {
                  double nearestDist = Double.MAX_VALUE;
                  EntityLivingBase nearestEntity = null;

                  for(EntityLivingBase target : targets) {
                     if(this.canHit(player, target)) {
                        double dist = target.getDistanceSqToEntity(player);
                        if(dist < nearestDist) {
                           nearestEntity = target;
                           nearestDist = dist;
                        }
                     }
                  }

                  this.target = nearestEntity;
               } else if(this.targetPriority.value == TargetPriority.Health) {
                  double lowestHealth = Double.MAX_VALUE;
                  EntityLivingBase entity = null;

                  for(EntityLivingBase target : targets) {
                     if(this.canHit(player, target)) {
                        if(entity == null) {
                           entity = target;
                        } else if((double)target.getHealth() < lowestHealth) {
                           entity = target;
                        }
                     }
                  }

                  this.target = entity;
               } else if(this.targetPriority.value != TargetPriority.FOVPlayer && RotationUtils.isSet()) {
                  if(this.targetPriority.value == TargetPriority.FOVRotated) {
                     float nearest = Float.MAX_VALUE;
                     EntityLivingBase nearestEntity = null;

                     for(EntityLivingBase entity : targets) {
                        if(this.canHit(player, entity)) {
                           double diffX = entity.posX - player.posX;
                           double diffY = entity.posY + (double)entity.getEyeHeight() * 0.9D - (player.posY + (double)player.getEyeHeight());
                           double diffZ = entity.posZ - player.posZ;
                           double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
                           float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
                           float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
                           float[] neededRotations = new float[]{RotationUtils.getYaw() + MathHelper.wrapAngleTo180_float(yaw - RotationUtils.getYaw()), RotationUtils.getPitch() + MathHelper.wrapAngleTo180_float(pitch - RotationUtils.getPitch())};
                           float neededYaw = RotationUtils.getYaw() - neededRotations[0];
                           float neededPitch = RotationUtils.getPitch() - neededRotations[1];
                           float distanceFromMouse = neededYaw * neededYaw + neededPitch * neededPitch;
                           if(distanceFromMouse < nearest) {
                              nearest = distanceFromMouse;
                              nearestEntity = entity;
                           }
                        }
                     }

                     this.target = nearestEntity;
                  }
               } else {
                  float nearest = Float.MAX_VALUE;
                  EntityLivingBase nearestEntity = null;

                  for(EntityLivingBase entity : targets) {
                     if(this.canHit(player, entity)) {
                        float distanceFromMouse = this.getDistFromMouseSq(entity);
                        if(distanceFromMouse < nearest) {
                           nearest = distanceFromMouse;
                           nearestEntity = entity;
                        }
                     }
                  }

                  this.target = nearestEntity;
               }
            } catch (Exception var22) {
               ;
            }

            this.updatingTarget = false;
         });
      }
   }

   private boolean rotateAAC(EntityPlayerSP player, EntityLivingBase target) {
      double[] moveToLoc = MoveUtils.getMoveToLoc(target, 2 + LatencyUtils.getLatencyTicks());
      double hitY;
      if(target.posY > player.posY + (double)player.getEyeHeight()) {
         hitY = target.posY + (double)(AC.getRandom().nextFloat() / 5.0F);
      } else if(target.posY + (double)target.getEyeHeight() < player.posY + (double)player.getEyeHeight()) {
         hitY = target.posY + (double)target.getEyeHeight() - (double)(AC.getRandom().nextFloat() / 5.0F);
      } else {
         hitY = player.posY + (double)player.getEyeHeight() * 0.75D;
      }

      double diffX = moveToLoc[0] - player.posX;
      double diffY = hitY - (player.posY + (double)player.getEyeHeight());
      double diffZ = moveToLoc[2] - player.posZ;
      double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
      float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
      float[] neededRotations = new float[]{(RotationUtils.isSet()?RotationUtils.getYaw():player.rotationYaw) + MathHelper.wrapAngleTo180_float(yaw - (RotationUtils.isSet()?RotationUtils.getYaw():player.rotationYaw)), (RotationUtils.isSet()?RotationUtils.getPitch():player.rotationPitch) + MathHelper.wrapAngleTo180_float(pitch - (RotationUtils.isSet()?RotationUtils.getPitch():player.rotationPitch))};
      float[] rlyneed = (float[])neededRotations.clone();
      float d0 = 0.0F - (RotationUtils.isSet()?RotationUtils.getLastYaw():player.rotationYaw);
      float d0y = neededRotations[0] + d0;
      boolean rotateRight = d0y > 0.0F;
      if(rotateRight) {
         neededRotations[0] = (RotationUtils.isSet()?RotationUtils.getLastYaw():player.rotationYaw) + Math.min(Math.abs(0.0F - d0y), ((Float)this.yawRate.value).floatValue());
      } else {
         neededRotations[0] = (RotationUtils.isSet()?RotationUtils.getLastYaw():player.rotationYaw) - Math.min(Math.abs(0.0F - d0y), ((Float)this.yawRate.value).floatValue());
      }

      RotationUtils.set(this, neededRotations[0], neededRotations[1]);
      boolean rotated = neededRotations[0] == rlyneed[0] && neededRotations[1] == rlyneed[1];
      if(!rotated && AC.getRandom().nextBoolean()) {
         player.swingItem();
      }

      return rotated;
   }

   public void onEvent(Event event) {
      if(this.multi.value == ModuleKillAura.AttackMode.MultiAura) {
         if(event instanceof EventTick) {
            this.updatePossibleTargets();
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(this.hitRandomization.value == ModuleKillAura.RandomMode.NonRandom) {
               int hitDelay = (int)(1000.0D / ((Double)this.speed.value).doubleValue());

               while(this.hitSpeedTimer.hasMSPassed((long)hitDelay)) {
                  this.hitSpeedTimer.subtract(hitDelay);
                  this.targets.stream().filter((entity) -> {
                     return this.canHit(player, entity);
                  }).forEach((entity) -> {
                     this.attack(player, entity);
                  });
               }
            } else if(this.hitRandomization.value == ModuleKillAura.RandomMode.TrueRandom) {
               if((double)AC.getRandom().nextFloat() < ((Double)this.speed.value).doubleValue() / 20.0D && this.hit) {
                  this.targets.stream().filter((entity) -> {
                     return this.canHit(player, entity);
                  }).forEach((entity) -> {
                     this.attack(player, entity);
                  });
               }

               int hitDelay = (int)(1000.0D / ((Double)this.speed.value).doubleValue());

               while(this.hitSpeedTimer.hasMSPassed((long)hitDelay)) {
                  this.hitSpeedTimer.subtract(hitDelay);
               }
            } else if(this.hitRandomization.value == ModuleKillAura.RandomMode.FakeRandom) {
               if(this.fakeRandomTimer.hasMSPassed(1000L)) {
                  this.fakeRandomBool = !this.fakeRandomBool;
                  this.fakeRandomTimer.reset();
               }

               double speed = this.fakeRandomBool?((Double)this.speed.value).doubleValue():((Double)this.speed.value).doubleValue() - 1.0D;
               int hitDelay = (int)(1000.0D / speed);

               while(this.hitSpeedTimer.hasMSPassed((long)hitDelay)) {
                  this.hitSpeedTimer.subtract(hitDelay);
                  this.targets.stream().filter((entity) -> {
                     return this.canHit(player, entity);
                  }).forEach((entity) -> {
                     this.attack(player, entity);
                  });
               }
            }
         }
      } else if(event instanceof EventTick) {
         this.updatePossibleTargets();
         this.updateTarget();
         if(this.target != null) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(this.canHit(player, this.target) && this.rotate(player, this.target)) {
               this.hit = true;
               if(this.rot.value != ModuleKillAura.RotateMode.NCP2) {
                  this.doHit();
               }

               if(((Boolean)this.autoBlock.value).booleanValue() && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                  AC.getMC().playerController.sendUseItem(player, player.worldObj, player.getCurrentEquippedItem());
               }
            }
         } else {
            int hitDelay = (int)(1000.0D / ((Double)this.speed.value).doubleValue());

            while(this.hitSpeedTimer.hasMSPassed((long)hitDelay)) {
               this.hitSpeedTimer.subtract(hitDelay);
            }
         }

         if(this.target != null && !AC.isGhost()) {
            try {
               AC.getMC().effectRenderer.emitParticleAtEntity(this.target, EnumParticleTypes.CRIT);
            } catch (Exception var6) {
               System.out.println("Rare crashing case prevented.");
            }
         }
      } else if(event instanceof EventPacketSent && ((EventPacketSent)event).getPacket() instanceof C03PacketPlayer) {
         if(this.rot.value != ModuleKillAura.RotateMode.NCP2) {
            return;
         }

         this.doHit();
      }

   }

   public static enum AttackMode {
      Single,
      MultiAura,
      HurtTime;
   }

   public static enum RandomMode {
      NonRandom,
      TrueRandom,
      FakeRandom;
   }

   public static enum RotateMode {
      NONE,
      NCP1,
      NCP2,
      AAC;
   }
}
