/*     */ package org.neverhook.client.feature.impl.movement;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventLiquidSolid;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.player.MovementHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class LiquidWalk
/*     */   extends Feature
/*     */ {
/*     */   public static ListSetting mode;
/*     */   public static NumberSetting speed;
/*     */   public static NumberSetting motionUp;
/*     */   public static NumberSetting boostSpeed;
/*     */   public static NumberSetting boostTicks;
/*     */   public static BooleanSetting boost;
/*     */   private final BooleanSetting speedCheck;
/*     */   public boolean start = false;
/*  29 */   public BooleanSetting groundSpoof = new BooleanSetting("GroundSpoof", true, () -> Boolean.valueOf(mode.currentMode.equals("Custom")));
/*  30 */   public BooleanSetting solid = new BooleanSetting("Solid", false, () -> Boolean.valueOf(mode.currentMode.equals("Custom")));
/*  31 */   public BooleanSetting groundSpoofExploit = new BooleanSetting("GroundSpoof Exploit", true, () -> Boolean.valueOf(mode.currentMode.equals("Custom")));
/*  32 */   public BooleanSetting useMotion = new BooleanSetting("Use Motion", false, () -> Boolean.valueOf(mode.currentMode.equals("Custom")));
/*  33 */   public NumberSetting motionY = new NumberSetting("Y-Motion", 0.42F, 0.0F, 2.0F, 0.01F, () -> Boolean.valueOf((mode.currentMode.equals("Custom") && this.useMotion.getBoolValue())));
/*  34 */   public BooleanSetting useSpeed = new BooleanSetting("Use Speed", true, () -> Boolean.valueOf(mode.currentMode.equals("Custom")));
/*  35 */   public BooleanSetting useJumpMoveFactor = new BooleanSetting("Use Jump Move Factor", true, () -> Boolean.valueOf(mode.currentMode.equals("Custom")));
/*  36 */   public BooleanSetting useTimer = new BooleanSetting("Use Timer", true, () -> Boolean.valueOf(mode.currentMode.equals("Custom")));
/*  37 */   public BooleanSetting useTimerExploit = new BooleanSetting("Timer Exploit", true, () -> Boolean.valueOf(mode.currentMode.equals("Custom")));
/*  38 */   public BooleanSetting fallDistExploit = new BooleanSetting("Fall Distance Exploit", true, () -> Boolean.valueOf(mode.currentMode.equals("Custom")));
/*  39 */   public BooleanSetting useFallDist = new BooleanSetting("Use Fall Distance", true, () -> Boolean.valueOf(mode.currentMode.equals("Custom")));
/*  40 */   public NumberSetting groundSpoofMotion = new NumberSetting("GroundSpoof Y-Motion", 0.08F, 0.0F, 1.0F, 0.001F, () -> Boolean.valueOf((mode.currentMode.equals("Custom") && this.groundSpoofExploit.getBoolValue())));
/*  41 */   public NumberSetting inLiquid = new NumberSetting("In Liquid", 0.1F, 0.0F, 1.0F, 0.001F, () -> Boolean.valueOf(mode.currentMode.equals("Custom")));
/*  42 */   public NumberSetting fallDist = new NumberSetting("Fall Distance", 0.0F, 0.0F, 10000.0F, 0.1F, () -> Boolean.valueOf((mode.currentMode.equals("Custom") && this.useFallDist.getBoolValue())));
/*  43 */   public NumberSetting jumpMoveFactor = new NumberSetting("Jump Move Factor", 0.1F, 0.0F, 10.0F, 0.001F, () -> Boolean.valueOf((mode.currentMode.equals("Custom") && this.useJumpMoveFactor.getBoolValue())));
/*  44 */   public NumberSetting timer = new NumberSetting("Timer", 1.0F, 0.001F, 15.0F, 0.001F, () -> Boolean.valueOf((mode.currentMode.equals("Custom") && this.useTimer.getBoolValue())));
/*     */   
/*     */   public LiquidWalk() {
/*  47 */     super("Liquid Walk", "Позволяет ходить по воде", Type.Movement);
/*  48 */     mode = new ListSetting("LiquidWalk Mode", "Matrix Jump", () -> Boolean.valueOf(true), new String[] { "Matrix Jump", "Dolphin", "Matrix Ground", "Matrix Zoom", "Matrix Solid", "Custom" });
/*  49 */     speed = new NumberSetting("Speed", 1.0F, 0.1F, 15.0F, 0.1F, () -> Boolean.valueOf(((!mode.currentMode.equals("Dolphin") && mode.currentMode.equals("Custom") && this.useSpeed.getBoolValue()) || mode.currentMode.equals("Matrix Ground") || mode.currentMode.equals("Matrix Solid") || mode.currentMode.equals("Matrix Jump"))));
/*  50 */     boost = new BooleanSetting("Boost", false, () -> Boolean.valueOf((mode.currentMode.equals("Matrix Ground") || mode.currentMode.equals("Custom"))));
/*  51 */     boostSpeed = new NumberSetting("Boost Speed", 1.35F, 0.1F, 4.0F, 0.01F, () -> Boolean.valueOf(((boost.getBoolValue() && mode.currentMode.equals("Custom")) || mode.currentMode.equals("Matrix Ground"))));
/*  52 */     motionUp = new NumberSetting("Motion Up", 0.42F, 0.1F, 2.0F, 0.01F, () -> Boolean.valueOf(mode.currentMode.equals("Matrix Jump")));
/*  53 */     boostTicks = new NumberSetting("Boost Ticks", 2.0F, 0.0F, 30.0F, 1.0F, () -> Boolean.valueOf(((boost.getBoolValue() && mode.currentMode.equals("Custom")) || mode.currentMode.equals("Matrix Ground"))));
/*  54 */     this.speedCheck = new BooleanSetting("Speed Potion Check", false, () -> Boolean.valueOf(true));
/*  55 */     addSettings(new Setting[] { (Setting)mode, (Setting)this.useMotion, (Setting)this.useSpeed, (Setting)this.useFallDist, (Setting)this.useJumpMoveFactor, (Setting)this.useTimer, (Setting)speed, (Setting)this.motionY, (Setting)this.timer, (Setting)this.useTimerExploit, (Setting)this.groundSpoof, (Setting)this.groundSpoofExploit, (Setting)this.groundSpoofMotion, (Setting)this.solid, (Setting)this.inLiquid, (Setting)this.fallDist, (Setting)this.fallDistExploit, (Setting)this.jumpMoveFactor, (Setting)boost, (Setting)boostSpeed, (Setting)boostTicks, (Setting)motionUp, (Setting)this.speedCheck });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onLiquidBB(EventLiquidSolid event) {
/*  60 */     if (mode.currentMode.equals("Matrix Ground") || mode.currentMode.equals("Matrix Solid") || (this.solid.getBoolValue() && mode.currentMode.equals("Custom")))
/*  61 */       event.setCancelled(!mc.gameSettings.keyBindJump.isKeyDown()); 
/*     */   }
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/*     */     BlockPos blockPosCustom;
/*     */     Block blockCustom;
/*  67 */     setSuffix(mode.getCurrentMode());
/*  68 */     BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.1D, mc.player.posZ);
/*  69 */     Block block = mc.world.getBlockState(blockPos).getBlock();
/*  70 */     if (!mc.player.isPotionActive(MobEffects.SPEED) && this.speedCheck.getBoolValue())
/*     */       return; 
/*  72 */     switch (mode.currentMode) {
/*     */       case "Matrix Jump":
/*  74 */         if (mc.player.isInLiquid() && mc.player.motionY < 0.0D) {
/*  75 */           mc.player.motionY = motionUp.getNumberValue();
/*  76 */           MovementHelper.setSpeed(speed.getNumberValue());
/*     */         } 
/*     */         break;
/*     */       case "Dolphin":
/*  80 */         if (mc.player.isInLiquid()) {
/*  81 */           mc.player.motionX *= 1.17D;
/*  82 */           mc.player.motionZ *= 1.17D;
/*     */           
/*  84 */           if (mc.player.isCollidedHorizontally) {
/*  85 */             mc.player.motionY = 0.24D; break;
/*  86 */           }  if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ)).getBlock() != Blocks.AIR)
/*  87 */             mc.player.motionY += 0.04D; 
/*     */         } 
/*     */         break;
/*     */       case "Matrix Ground":
/*  91 */         if (block instanceof net.minecraft.block.BlockLiquid) {
/*  92 */           mc.player.onGround = false;
/*  93 */           mc.player.isAirBorne = true;
/*  94 */           MovementHelper.setSpeed(boost.getBoolValue() ? ((mc.player.ticksExisted % boostTicks.getNumberValue() == 0.0F) ? speed.getNumberValue() : boostSpeed.getNumberValue()) : speed.getNumberValue());
/*  95 */           event.setPosY((mc.player.ticksExisted % 2 == 0) ? (event.getPosY() + 0.02D) : (event.getPosY() - 0.02D));
/*  96 */           event.setOnGround(false);
/*     */         } 
/*     */         break;
/*     */       case "Matrix Zoom":
/* 100 */         if (block instanceof net.minecraft.block.BlockLiquid) {
/* 101 */           MovementHelper.setSpeed(speed.getNumberValue());
/*     */         }
/*     */         
/* 104 */         if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1.0E-7D, mc.player.posZ)).getBlock() == Blocks.WATER) {
/* 105 */           mc.player.fallDistance = 0.0F;
/* 106 */           mc.player.motionX = 0.0D;
/* 107 */           mc.player.motionY = 0.08D;
/* 108 */           mc.player.jumpMovementFactor = 0.2F;
/* 109 */           mc.player.motionZ = 0.0D;
/*     */         } 
/*     */         break;
/*     */       case "Matrix Solid":
/* 113 */         if (block instanceof net.minecraft.block.BlockLiquid) {
/* 114 */           mc.player.onGround = false;
/* 115 */           mc.player.isAirBorne = true;
/* 116 */           MovementHelper.setSpeed((mc.player.ticksExisted % 2 == 0) ? speed.getNumberValue() : 0.1F);
/* 117 */           event.setPosY((mc.player.ticksExisted % 2 == 0) ? (event.getPosY() + 0.02D) : (event.getPosY() - 0.02D));
/* 118 */           event.setOnGround(false);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case "Custom":
/* 125 */         blockPosCustom = new BlockPos(mc.player.posX, mc.player.posY - this.inLiquid.getNumberValue(), mc.player.posZ);
/* 126 */         blockCustom = mc.world.getBlockState(blockPosCustom).getBlock();
/*     */         
/* 128 */         if (blockCustom instanceof net.minecraft.block.BlockLiquid) {
/*     */           
/* 130 */           if (this.useFallDist.getBoolValue()) {
/* 131 */             mc.player.fallDistance = this.fallDist.getNumberValue();
/*     */           }
/* 133 */           if (this.useJumpMoveFactor.getBoolValue()) {
/* 134 */             mc.player.jumpMovementFactor = this.jumpMoveFactor.getNumberValue();
/*     */           }
/*     */           
/* 137 */           if (this.useMotion.getBoolValue()) {
/* 138 */             mc.player.motionY = this.motionY.getNumberValue();
/*     */           }
/*     */           
/* 141 */           if (this.useTimer.getBoolValue()) {
/* 142 */             mc.timer.timerSpeed = this.timer.getNumberValue();
/*     */           }
/*     */           
/* 145 */           if (this.useTimerExploit.getBoolValue()) {
/* 146 */             mc.timer.timerSpeed = (mc.player.ticksExisted % 60 > 39) ? 1000.0F : 1.0F;
/*     */           }
/*     */           
/* 149 */           if (this.groundSpoof.getBoolValue()) {
/* 150 */             mc.player.onGround = false;
/*     */           }
/*     */           
/* 153 */           if (this.fallDistExploit.getBoolValue()) {
/* 154 */             mc.player.fallDistance = (float)(Math.random() * 1.0E-12D);
/*     */           }
/*     */           
/* 157 */           if (this.useSpeed.getBoolValue()) {
/* 158 */             MovementHelper.setSpeed(boost.getBoolValue() ? ((mc.player.ticksExisted % boostTicks.getNumberValue() == 0.0F) ? speed.getNumberValue() : boostSpeed.getNumberValue()) : speed.getNumberValue());
/*     */           }
/*     */           
/* 161 */           if (this.groundSpoofExploit.getBoolValue()) {
/* 162 */             event.setPosY((mc.player.ticksExisted % 2 == 0) ? (event.getPosY() + this.groundSpoofMotion.getNumberValue()) : (event.getPosY() - this.groundSpoofMotion.getNumberValue()));
/* 163 */             event.setOnGround(false);
/*     */           } 
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\LiquidWalk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */