/*     */ package org.neverhook.client.feature.impl.ghost;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.friend.Friend;
/*     */ import org.neverhook.client.helpers.math.GCDCalcHelper;
/*     */ import org.neverhook.client.helpers.math.MathematicHelper;
/*     */ import org.neverhook.client.helpers.math.RotationHelper;
/*     */ import org.neverhook.client.helpers.world.EntityHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AimAssist
/*     */   extends Feature
/*     */ {
/*     */   public static NumberSetting range;
/*     */   public static BooleanSetting players;
/*     */   public static BooleanSetting mobs;
/*     */   public static BooleanSetting team;
/*     */   public static BooleanSetting walls;
/*     */   public static BooleanSetting invis;
/*     */   public static BooleanSetting click;
/*     */   public static NumberSetting fov;
/*     */   public static NumberSetting predict;
/*     */   public static NumberSetting rotYawSpeed;
/*     */   public static NumberSetting rotPitchSpeed;
/*     */   public static NumberSetting rotYawRandom;
/*     */   public static NumberSetting rotPitchRandom;
/*     */   public static ListSetting sort;
/*     */   public static ListSetting part;
/*     */   
/*     */   public AimAssist() {
/*  47 */     super("AimAssist", "Автоматический аим на сущностей вокруг тебя", Type.Ghost);
/*  48 */     sort = new ListSetting("AssistSort Mode", "Distance", () -> Boolean.valueOf(true), new String[] { "Distance", "Higher Armor", "Lowest Armor", "Health", "Angle", "HurtTime" });
/*  49 */     part = new ListSetting("Aim-Part Mode", "Chest", () -> Boolean.valueOf(true), new String[] { "Chest", "Head", "Leggings", "Boots" });
/*  50 */     range = new NumberSetting("Range", 4.0F, 1.0F, 10.0F, 0.1F, () -> Boolean.valueOf(true));
/*  51 */     players = new BooleanSetting("Players", true, () -> Boolean.valueOf(true));
/*  52 */     mobs = new BooleanSetting("Mobs", false, () -> Boolean.valueOf(true));
/*  53 */     team = new BooleanSetting("Team Check", false, () -> Boolean.valueOf(true));
/*  54 */     walls = new BooleanSetting("Walls", false, () -> Boolean.valueOf(true));
/*  55 */     invis = new BooleanSetting("Invisible", false, () -> Boolean.valueOf(true));
/*  56 */     click = new BooleanSetting("Click Only", false, () -> Boolean.valueOf(true));
/*  57 */     predict = new NumberSetting("Aim Predict", 0.5F, 0.5F, 5.0F, 0.1F, () -> Boolean.valueOf(true));
/*  58 */     fov = new NumberSetting("Assist FOV", 180.0F, 5.0F, 180.0F, 5.0F, () -> Boolean.valueOf(true));
/*  59 */     rotYawSpeed = new NumberSetting("Rotation Yaw Speed", 1.0F, 0.1F, 5.0F, 0.1F, () -> Boolean.valueOf(true));
/*  60 */     rotPitchSpeed = new NumberSetting("Rotation Pitch Speed", 1.0F, 0.1F, 5.0F, 0.1F, () -> Boolean.valueOf(true));
/*  61 */     rotYawRandom = new NumberSetting("Yaw Randomize", 1.0F, 0.0F, 3.0F, 0.1F, () -> Boolean.valueOf(true));
/*  62 */     rotPitchRandom = new NumberSetting("Pitch Randomize", 1.0F, 0.0F, 3.0F, 0.1F, () -> Boolean.valueOf(true));
/*  63 */     addSettings(new Setting[] { (Setting)range, (Setting)players, (Setting)mobs, (Setting)walls, (Setting)invis, (Setting)team, (Setting)click, (Setting)predict, (Setting)fov, (Setting)rotYawSpeed, (Setting)rotPitchSpeed, (Setting)rotPitchRandom, (Setting)rotYawRandom, (Setting)sort, (Setting)part });
/*     */   }
/*     */   
/*     */   public static boolean canSeeEntityAtFov(Entity entityIn, float scope) {
/*  67 */     double diffX = entityIn.posX - mc.player.posX;
/*  68 */     double diffZ = entityIn.posZ - mc.player.posZ;
/*  69 */     float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D);
/*  70 */     double difference = angleDifference(newYaw, mc.player.rotationYaw);
/*  71 */     return (difference <= scope);
/*     */   }
/*     */   
/*     */   public static double angleDifference(double a, double b) {
/*  75 */     float yaw360 = (float)(Math.abs(a - b) % 360.0D);
/*  76 */     if (yaw360 > 180.0F) {
/*  77 */       yaw360 = 360.0F - yaw360;
/*     */     }
/*  79 */     return yaw360;
/*     */   }
/*     */   
/*     */   public static EntityLivingBase getSortEntities() {
/*  83 */     List<EntityLivingBase> entity = new ArrayList<>();
/*  84 */     for (Entity e : mc.world.loadedEntityList) {
/*  85 */       if (e instanceof EntityLivingBase) {
/*  86 */         EntityLivingBase player = (EntityLivingBase)e;
/*  87 */         if (mc.player.getDistanceToEntity((Entity)player) < range.getNumberValue() && canAssist(player)) {
/*  88 */           if (player.getHealth() > 0.0F) {
/*  89 */             entity.add(player); continue;
/*     */           } 
/*  91 */           entity.remove(player);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  97 */     String sortMode = sort.getOptions();
/*     */     
/*  99 */     if (sortMode.equalsIgnoreCase("Angle")) {
/* 100 */       entity.sort((o1, o2) -> (int)(Math.abs(RotationHelper.getAngleEntity((Entity)o1) - mc.player.rotationYaw) - Math.abs(RotationHelper.getAngleEntity((Entity)o2) - mc.player.rotationYaw)));
/* 101 */     } else if (sortMode.equalsIgnoreCase("Higher Armor")) {
/* 102 */       entity.sort(Comparator.<EntityLivingBase, Comparable>comparing(EntityLivingBase::getTotalArmorValue).reversed());
/* 103 */     } else if (sortMode.equalsIgnoreCase("Lowest Armor")) {
/* 104 */       entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue));
/* 105 */     } else if (sortMode.equalsIgnoreCase("Health")) {
/* 106 */       entity.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
/* 107 */     } else if (sortMode.equalsIgnoreCase("Distance")) {
/* 108 */       entity.sort(Comparator.comparingDouble(mc.player::getDistanceToEntity));
/* 109 */     } else if (sortMode.equalsIgnoreCase("HurtTime")) {
/* 110 */       entity.sort(Comparator.<EntityLivingBase, Comparable>comparing(EntityLivingBase::getHurtTime).reversed());
/*     */     } 
/*     */     
/* 113 */     if (entity.isEmpty()) {
/* 114 */       return null;
/*     */     }
/* 116 */     return entity.get(0);
/*     */   }
/*     */   
/*     */   public static boolean canAssist(EntityLivingBase player) {
/* 120 */     for (Friend friend : NeverHook.instance.friendManager.getFriends()) {
/* 121 */       if (!player.getName().equals(friend.getName())) {
/*     */         continue;
/*     */       }
/* 124 */       return false;
/*     */     } 
/*     */     
/* 127 */     if (player instanceof net.minecraft.entity.player.EntityPlayer || player instanceof net.minecraft.entity.passive.EntityAnimal || player instanceof net.minecraft.entity.monster.EntityMob || player instanceof net.minecraft.entity.passive.EntityVillager) {
/* 128 */       if (player instanceof net.minecraft.entity.player.EntityPlayer && !players.getBoolValue()) {
/* 129 */         return false;
/*     */       }
/* 131 */       if (player instanceof net.minecraft.entity.passive.EntityAnimal && !mobs.getBoolValue()) {
/* 132 */         return false;
/*     */       }
/* 134 */       if (player instanceof net.minecraft.entity.monster.EntityMob && !mobs.getBoolValue()) {
/* 135 */         return false;
/*     */       }
/* 137 */       if (player instanceof net.minecraft.entity.passive.EntityVillager && !mobs.getBoolValue()) {
/* 138 */         return false;
/*     */       }
/*     */     } 
/* 141 */     if (player.isInvisible() && !invis.getBoolValue()) {
/* 142 */       return false;
/*     */     }
/*     */     
/* 145 */     if (!canSeeEntityAtFov((Entity)player, fov.getNumberValue() * 2.0F)) {
/* 146 */       return false;
/*     */     }
/* 148 */     if (team.getBoolValue() && EntityHelper.isTeamWithYou(player)) {
/* 149 */       return false;
/*     */     }
/* 151 */     if (!player.canEntityBeSeen((Entity)mc.player)) {
/* 152 */       return walls.getBoolValue();
/*     */     }
/* 154 */     return (player != mc.player);
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/* 159 */     EntityLivingBase entity = getSortEntities();
/* 160 */     if (entity != null && 
/* 161 */       mc.player.getDistanceToEntity((Entity)entity) <= range.getNumberValue() && 
/* 162 */       entity != mc.player) {
/* 163 */       float[] rots = getRotationsForAssist(entity);
/*     */       
/* 165 */       if (click.getBoolValue() && !mc.gameSettings.keyBindAttack.isKeyDown()) {
/*     */         return;
/*     */       }
/* 168 */       if (canAssist(entity) && entity.getHealth() > 0.0F) {
/* 169 */         mc.player.rotationYaw = rots[0];
/* 170 */         mc.player.rotationPitch = rots[1];
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private float[] getRotationsForAssist(EntityLivingBase entityIn) {
/* 178 */     float yaw = RotationHelper.updateRotation(GCDCalcHelper.getFixedRotation(mc.player.rotationYaw + MathematicHelper.randomizeFloat(-rotYawRandom.getNumberValue(), rotYawRandom.getNumberValue())), getRotation((Entity)entityIn, predict.getNumberValue())[0], rotYawSpeed.getNumberValue() * 10.0F);
/* 179 */     float pitch = RotationHelper.updateRotation(GCDCalcHelper.getFixedRotation(mc.player.rotationPitch + MathematicHelper.randomizeFloat(-rotPitchRandom.getNumberValue(), rotPitchRandom.getNumberValue())), getRotation((Entity)entityIn, predict.getNumberValue())[1], rotPitchSpeed.getNumberValue() * 10.0F);
/* 180 */     return new float[] { yaw, pitch };
/*     */   }
/*     */ 
/*     */   
/*     */   private float[] getRotation(Entity e, float predictValue) {
/* 185 */     String mode = part.getOptions();
/* 186 */     float aimPoint = 0.0F;
/* 187 */     if (mode.equalsIgnoreCase("Head")) {
/* 188 */       aimPoint = 0.0F;
/* 189 */     } else if (mode.equalsIgnoreCase("Chest")) {
/* 190 */       aimPoint = 0.5F;
/* 191 */     } else if (mode.equalsIgnoreCase("Leggings")) {
/* 192 */       aimPoint = 0.9F;
/* 193 */     } else if (mode.equalsIgnoreCase("Boots")) {
/* 194 */       aimPoint = 1.3F;
/*     */     } 
/*     */     
/* 197 */     double xDelta = e.posX + (e.posX - e.prevPosX) * predictValue - mc.player.posX - mc.player.motionX * predictValue;
/* 198 */     double zDelta = e.posZ + (e.posZ - e.prevPosZ) * predictValue - mc.player.posZ - mc.player.motionZ * predictValue;
/* 199 */     double diffY = e.posY + e.getEyeHeight() - mc.player.posY + mc.player.getEyeHeight() + aimPoint;
/*     */     
/* 201 */     double distance = MathHelper.sqrt(xDelta * xDelta + zDelta * zDelta);
/*     */     
/* 203 */     float yaw = (float)(MathHelper.atan2(zDelta, xDelta) * 180.0D / Math.PI - 90.0D) + MathematicHelper.randomizeFloat(-rotYawRandom.getNumberValue(), rotYawRandom.getNumberValue());
/* 204 */     float pitch = (float)-(MathHelper.atan2(diffY, distance) * 180.0D / Math.PI) + MathematicHelper.randomizeFloat(-rotPitchRandom.getNumberValue(), rotPitchRandom.getNumberValue());
/*     */     
/* 206 */     yaw = mc.player.rotationYaw + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
/* 207 */     pitch = mc.player.rotationPitch + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
/* 208 */     pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
/* 209 */     return new float[] { yaw, pitch };
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\ghost\AimAssist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */