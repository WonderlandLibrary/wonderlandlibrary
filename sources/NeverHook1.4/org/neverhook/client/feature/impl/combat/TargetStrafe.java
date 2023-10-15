/*     */ package org.neverhook.client.feature.impl.combat;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class TargetStrafe extends Feature {
/*     */   public static NumberSetting range;
/*     */   public static NumberSetting spd;
/*     */   public static NumberSetting boostValue;
/*     */   public static NumberSetting boostTicks;
/*     */   public static NumberSetting speedIfUsing;
/*     */   public static BooleanSetting reversed;
/*     */   public static NumberSetting reversedDistance;
/*     */   public static BooleanSetting boost;
/*     */   public static BooleanSetting autoJump;
/*     */   public static BooleanSetting smartStrafe;
/*     */   public static BooleanSetting usingItemCheck;
/*     */   public static BooleanSetting autoShift;
/*  33 */   public BooleanSetting trajectory = new BooleanSetting("Trajectory", true, () -> Boolean.valueOf(true));
/*  34 */   public BooleanSetting me = new BooleanSetting("Me", true, () -> Boolean.valueOf(this.trajectory.getBoolValue()));
/*  35 */   public NumberSetting width = new NumberSetting("Width", 2.0F, 1.0F, 6.0F, 0.1F, () -> Boolean.valueOf(this.trajectory.getBoolValue()));
/*  36 */   private float wrap = 0.0F;
/*     */   private boolean switchDir = true;
/*     */   
/*     */   public TargetStrafe() {
/*  40 */     super("TargetStrafe", "Стрейфит вокруг сущностей", Type.Movement);
/*  41 */     spd = new NumberSetting("Strafe Speed", 0.23F, 0.1F, 2.0F, 0.01F, () -> Boolean.valueOf(true));
/*  42 */     range = new NumberSetting("Strafe Distance", 2.4F, 0.1F, 6.0F, 0.1F, () -> Boolean.valueOf(true));
/*  43 */     boost = new BooleanSetting("DamageBoost", false, () -> Boolean.valueOf(true));
/*  44 */     boostValue = new NumberSetting("Boost Value", 0.5F, 0.1F, 4.0F, 0.01F, boost::getBoolValue);
/*  45 */     boostTicks = new NumberSetting("Boost Ticks", 8.0F, 0.0F, 9.0F, 1.0F, boost::getBoolValue);
/*  46 */     reversed = new BooleanSetting("Reversed", false, () -> Boolean.valueOf(true));
/*  47 */     reversedDistance = new NumberSetting("Reversed Distance", 3.0F, 1.0F, 6.0F, 0.1F, () -> Boolean.valueOf(reversed.getBoolValue()));
/*  48 */     autoJump = new BooleanSetting("AutoJump", true, () -> Boolean.valueOf(true));
/*  49 */     autoShift = new BooleanSetting("AutoShift", false, () -> Boolean.valueOf(true));
/*  50 */     smartStrafe = new BooleanSetting("Smart Strafe", true, () -> Boolean.valueOf(true));
/*  51 */     usingItemCheck = new BooleanSetting("Using Item Check", false, () -> Boolean.valueOf(true));
/*  52 */     speedIfUsing = new NumberSetting("Speed if using", 0.1F, 0.1F, 2.0F, 0.01F, usingItemCheck::getBoolValue);
/*  53 */     addSettings(new Setting[] { (Setting)boost, (Setting)boostTicks, (Setting)boostValue, (Setting)this.trajectory, (Setting)this.me, (Setting)this.width, (Setting)reversed, (Setting)reversedDistance, (Setting)autoJump, (Setting)autoShift, (Setting)usingItemCheck, (Setting)speedIfUsing, (Setting)smartStrafe, (Setting)spd, (Setting)range });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  58 */     this.wrap = 0.0F;
/*  59 */     this.switchDir = true;
/*  60 */     super.onEnable();
/*     */   }
/*     */   
/*     */   public boolean needToSwitch(double x, double z) {
/*  64 */     if (mc.player.isCollidedHorizontally || mc.gameSettings.keyBindLeft.isPressed() || mc.gameSettings.keyBindRight.isPressed()) {
/*  65 */       return true;
/*     */     }
/*  67 */     for (int i = (int)(mc.player.posY + 4.0D); i >= 0; ) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  73 */       BlockPos playerPos = new BlockPos(x, i, z);
/*  74 */       if (!mc.world.getBlockState(playerPos).getBlock().equals(Blocks.LAVA) && 
/*  75 */         !mc.world.getBlockState(playerPos).getBlock().equals(Blocks.FIRE)) {
/*     */ 
/*     */ 
/*     */         
/*  79 */         if (mc.world.isAirBlock(playerPos)) { i--; continue; }
/*  80 */          return false;
/*     */       }  return true;
/*  82 */     }  return true;
/*     */   }
/*     */   
/*     */   private float toDegree(double x, double z) {
/*  86 */     return (float)(Math.atan2(z - mc.player.posZ, x - mc.player.posX) * 180.0D / Math.PI) - 90.0F;
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {
/*  91 */     if (KillAura.target == null)
/*     */       return; 
/*  93 */     if (mc.player.getDistanceToEntity((Entity)KillAura.target) <= KillAura.range.getNumberValue() && 
/*  94 */       autoShift.getBoolValue()) {
/*  95 */       mc.gameSettings.keyBindSneak.setPressed((getState() && KillAura.target != null && mc.player.fallDistance > KillAura.critFallDistance.getNumberValue() + 0.1D));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onRender3D(EventRender3D event) {
/* 102 */     if (this.trajectory.getBoolValue() && KillAura.target != null) {
/* 103 */       RenderHelper.drawCircle3D((Entity)KillAura.target, range.getNumberValue() - 0.00625D, event.getPartialTicks(), 15, this.width.getNumberValue(), (new Color(0, 0, 0)).getRGB());
/* 104 */       RenderHelper.drawCircle3D((Entity)KillAura.target, range.getNumberValue() + 0.00625D, event.getPartialTicks(), 15, this.width.getNumberValue(), (new Color(0, 0, 0)).getRGB());
/* 105 */       RenderHelper.drawCircle3D((Entity)KillAura.target, range.getNumberValue(), event.getPartialTicks(), 15, 2.0F, -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/* 111 */     if (KillAura.target == null)
/*     */       return; 
/* 113 */     setSuffix("" + range.getNumberValue());
/* 114 */     if (mc.player.getDistanceToEntity((Entity)KillAura.target) <= KillAura.range.getNumberValue()) {
/* 115 */       if (KillAura.target.getHealth() > 0.0F && 
/* 116 */         autoJump.getBoolValue() && NeverHook.instance.featureManager.getFeatureByClass(KillAura.class).getState() && NeverHook.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() && 
/* 117 */         mc.player.onGround) {
/* 118 */         mc.player.jump();
/*     */       }
/*     */ 
/*     */       
/* 122 */       if (KillAura.target.getHealth() > 0.0F) {
/* 123 */         EntityLivingBase target = KillAura.target;
/* 124 */         if (target == null || mc.player.ticksExisted < 20)
/*     */           return; 
/* 126 */         float speed = (mc.player.hurtTime > boostTicks.getNumberValue() && boost.getBoolValue() && !mc.player.onGround) ? boostValue.getNumberValue() : (((mc.player.isUsingItem() || mc.gameSettings.keyBindUseItem.isKeyDown()) && usingItemCheck.getBoolValue()) ? speedIfUsing.getNumberValue() : spd.getNumberValue());
/* 127 */         this.wrap = (float)Math.atan2(mc.player.posZ - target.posZ, mc.player.posX - target.posX);
/* 128 */         this.wrap += this.switchDir ? (speed / mc.player.getDistanceToEntity((Entity)target)) : -(speed / mc.player.getDistanceToEntity((Entity)target));
/* 129 */         double x = target.posX + range.getNumberValue() * Math.cos(this.wrap);
/* 130 */         double z = target.posZ + range.getNumberValue() * Math.sin(this.wrap);
/* 131 */         if (smartStrafe.getBoolValue() && needToSwitch(x, z)) {
/* 132 */           this.switchDir = !this.switchDir;
/* 133 */           this.wrap += 2.0F * (this.switchDir ? (speed / mc.player.getDistanceToEntity((Entity)target)) : -(speed / mc.player.getDistanceToEntity((Entity)target)));
/* 134 */           x = target.posX + range.getNumberValue() * Math.cos(this.wrap);
/* 135 */           z = target.posZ + range.getNumberValue() * Math.sin(this.wrap);
/*     */         } 
/* 137 */         if (mc.player.hurtTime > boostTicks.getNumberValue() && boost.getBoolValue() && !mc.player.onGround) {
/* 138 */           mc.player.jumpMovementFactor *= 60.0F;
/*     */         }
/* 140 */         float searchValue = (NeverHook.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() && reversed.getBoolValue() && mc.player.getDistanceToEntity((Entity)KillAura.target) < reversedDistance.getNumberValue()) ? -90.0F : 0.0F;
/* 141 */         float reversedValue = (!mc.gameSettings.keyBindLeft.isKeyDown() && !mc.gameSettings.keyBindRight.isKeyDown() && !mc.player.isCollidedHorizontally) ? searchValue : 0.0F;
/* 142 */         mc.player.motionX = speed * -Math.sin((float)Math.toRadians(toDegree(x + reversedValue, z + reversedValue)));
/* 143 */         mc.player.motionZ = speed * Math.cos((float)Math.toRadians(toDegree(x + reversedValue, z + reversedValue)));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\TargetStrafe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */