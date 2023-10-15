/*     */ package org.neverhook.client.feature.impl.movement;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.motion.EventMove;
/*     */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.helpers.player.MovementHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Speed
/*     */   extends Feature
/*     */ {
/*     */   public static int stage;
/*     */   private final BooleanSetting strafing;
/*     */   private final BooleanSetting boost;
/*     */   private final List<Packet<?>> packets;
/*     */   private final List<double[]> positions;
/*  35 */   private final TimerHelper pulseTimer = new TimerHelper();
/*     */   private final BooleanSetting potionCheck;
/*  37 */   public TimerHelper timerHelper = new TimerHelper();
/*     */   public double moveSpeed;
/*     */   public ListSetting speedMode;
/*     */   public BooleanSetting autoHitDisable;
/*  41 */   public NumberSetting jumpMoveFactor = new NumberSetting("Custom Speed", 0.0265F, 0.01F, 0.1F, 0.001F, () -> Boolean.valueOf(this.speedMode.currentMode.equals("Custom")));
/*  42 */   public BooleanSetting onGround = new BooleanSetting("Ground Only", false, () -> Boolean.valueOf(this.speedMode.currentMode.equals("Custom")));
/*  43 */   public NumberSetting onGroundSpeed = new NumberSetting("Custom Ground Speed", 0.5F, 0.001F, 10.0F, 0.01F, () -> Boolean.valueOf((this.speedMode.currentMode.equals("Custom") && this.onGround.getBoolValue())));
/*  44 */   public NumberSetting motionY = new NumberSetting("Custom Y-Motion", 0.42F, 0.01F, 1.0F, 0.01F, () -> Boolean.valueOf(this.speedMode.currentMode.equals("Custom")));
/*  45 */   public BooleanSetting blink = new BooleanSetting("Blink", false, () -> Boolean.valueOf(this.speedMode.currentMode.equals("Custom")));
/*  46 */   public BooleanSetting timerExploit = new BooleanSetting("Timer Exploit", false, () -> Boolean.valueOf(!this.speedMode.currentMode.equals("Matrix Old")));
/*  47 */   public NumberSetting timer = new NumberSetting("Custom Timer", 1.0F, 0.1F, 10.0F, 0.1F, () -> Boolean.valueOf((this.speedMode.currentMode.equals("Custom") && !this.timerExploit.getBoolValue())));
/*  48 */   public NumberSetting speed = new NumberSetting("Speed", 1.0F, 0.1F, 10.0F, 0.1F, () -> Boolean.valueOf((this.speedMode.currentMode.equals("Motion") || this.speedMode.currentMode.equals("MatrixGround 6.2.2"))));
/*     */   private int boostTick;
/*     */   private boolean disableLogger;
/*     */   
/*     */   public Speed() {
/*  53 */     super("Speed", "Увеличивает вашу скорость", Type.Movement);
/*     */     
/*  55 */     this.speedMode = new ListSetting("Speed Mode", "Matrix 6.2.2", () -> Boolean.valueOf(true), new String[] { "Matrix Timer", "Matrix 6.2.2", "Matrix 6.3.0", "MatrixGround 6.2.2", "Motion", "Old Sunrise", "MatrixHop", "MatrixOnGround", "NCP LowHop", "Custom" });
/*     */     
/*  57 */     this.strafing = new BooleanSetting("Strafing", false, () -> Boolean.valueOf(true));
/*  58 */     this.boost = new BooleanSetting("GroundSpoof", true, () -> Boolean.valueOf((this.speedMode.currentMode.equals("Matrix Old") || this.speedMode.currentMode.equals("Custom"))));
/*  59 */     this.potionCheck = new BooleanSetting("Speed Potion Check", false, () -> Boolean.valueOf(this.speedMode.currentMode.equals("Custom")));
/*  60 */     this.autoHitDisable = new BooleanSetting("Auto Hit Disable", false, () -> Boolean.valueOf(true));
/*  61 */     this.packets = new ArrayList<>();
/*  62 */     this.positions = (List)new LinkedList<>();
/*  63 */     addSettings(new Setting[] { (Setting)this.speedMode, (Setting)this.strafing, (Setting)this.boost, (Setting)this.speed, (Setting)this.jumpMoveFactor, (Setting)this.motionY, (Setting)this.onGroundSpeed, (Setting)this.onGround, (Setting)this.blink, (Setting)this.timerExploit, (Setting)this.timer, (Setting)this.potionCheck, (Setting)this.autoHitDisable });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onMove(EventMove eventMove) {
/*  68 */     if (this.speedMode.currentMode.equals("NCP LowHop") && 
/*  69 */       MovementHelper.isMoving() && !mc.player.isInLiquid()) {
/*     */       
/*  71 */       mc.timer.timerSpeed = 1.09F;
/*  72 */       eventMove.setY(mc.player.motionY = 0.1D);
/*  73 */       mc.player.jumpTicks = 10;
/*  74 */       if (eventMove.getY() < 0.0D) {
/*  75 */         mc.timer.timerSpeed = 1.04F;
/*     */       }
/*     */       
/*  78 */       float speed = mc.player.isSprinting() ? 0.97F : 1.0F;
/*  79 */       double moveSpeed = (Math.max(MovementHelper.getBaseMoveSpeed(), MovementHelper.getSpeed()) * speed);
/*  80 */       MovementHelper.setEventSpeed(eventMove, moveSpeed);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/*  87 */     if (getState()) {
/*  88 */       String mode = this.speedMode.getOptions();
/*  89 */       setSuffix(mode);
/*     */       
/*  91 */       if (mode.equals("Matrix 6.3.0") && 
/*  92 */         MovementHelper.isMoving()) {
/*  93 */         if (mc.player.onGround) {
/*  94 */           this.boostTick = 11;
/*  95 */           mc.player.jump();
/*  96 */         } else if (this.boostTick < 11) {
/*  97 */           this.boostTick++;
/*     */         } else {
/*  99 */           if (this.timerHelper.hasReached(460.0F)) {
/* 100 */             mc.player.motionX *= 1.7D;
/* 101 */             mc.player.motionZ *= 1.7D;
/* 102 */             this.timerHelper.reset();
/*     */           } 
/* 104 */           this.boostTick = 0;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 109 */       if (mode.equalsIgnoreCase("Motion")) {
/* 110 */         if (mc.player.onGround && mc.gameSettings.keyBindForward.pressed) {
/* 111 */           mc.player.jump();
/*     */         }
/* 113 */         mc.player.jumpMovementFactor = this.speed.getNumberValue();
/*     */       } 
/*     */       
/* 116 */       if (mode.equalsIgnoreCase("MatrixGround 6.2.2")) {
/* 117 */         if (mc.player.onGround && mc.gameSettings.keyBindForward.pressed) {
/* 118 */           mc.player.jump();
/* 119 */           mc.player.onGround = false;
/*     */         } 
/*     */         
/* 122 */         if (mc.player.motionY > 0.0D && !mc.player.isInWater()) {
/* 123 */           mc.player.motionY -= this.speed.getNumberValue();
/*     */         }
/* 125 */       } else if (mode.equalsIgnoreCase("Custom")) {
/* 126 */         if (!mc.player.isPotionActive(MobEffects.SPEED) && this.potionCheck.getBoolValue())
/*     */           return; 
/* 128 */         if (mc.player.onGround && !this.onGround.getBoolValue() && MovementHelper.isMoving()) {
/* 129 */           if (this.boost.getBoolValue()) {
/* 130 */             mc.player.onGround = false;
/*     */           }
/* 132 */           mc.player.motionY = this.motionY.getNumberValue();
/*     */         } 
/* 134 */         if (this.strafing.getBoolValue()) {
/* 135 */           MovementHelper.strafePlayer(MovementHelper.getSpeed());
/*     */         }
/* 137 */         mc.player.jumpMovementFactor = this.jumpMoveFactor.getNumberValue();
/* 138 */         if (this.onGround.getBoolValue()) {
/* 139 */           MovementHelper.setSpeed(this.onGroundSpeed.getNumberValue());
/*     */         }
/* 141 */         if (!this.timerExploit.getBoolValue()) {
/* 142 */           mc.timer.timerSpeed = this.timer.getNumberValue();
/*     */         }
/* 144 */         if (this.timerExploit.getBoolValue()) {
/* 145 */           mc.timer.timerSpeed = (mc.player.ticksExisted % 60 > 39) ? 1000.0F : 1.0F;
/*     */         }
/* 147 */       } else if (mode.equalsIgnoreCase("Matrix 6.2.2")) {
/* 148 */         if (this.strafing.getBoolValue()) {
/* 149 */           MovementHelper.strafePlayer(MovementHelper.getSpeed());
/*     */         }
/* 151 */         double x = mc.player.posX;
/* 152 */         double y = mc.player.posY;
/* 153 */         double z = mc.player.posZ;
/* 154 */         double yaw = mc.player.rotationYaw * 0.017453292D;
/* 155 */         if (MovementHelper.isMoving() && mc.player.fallDistance < 0.1D) {
/* 156 */           ItemStack stack = mc.player.getHeldItemOffhand();
/* 157 */           if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInLiquid() || mc.player.isCollidedHorizontally) {
/*     */             return;
/*     */           }
/* 160 */           boolean isSpeed = mc.player.isPotionActive(MobEffects.SPEED);
/* 161 */           boolean isRune = (stack.getItem() == Items.FIREWORK_CHARGE && stack.getDisplayName().contains("небесных врат"));
/* 162 */           boolean isRuneAndSpeed = (stack.getItem() == Items.FIREWORK_CHARGE && stack.getDisplayName().contains("небесных врат") && mc.player.isPotionActive(MobEffects.SPEED));
/* 163 */           if (mc.player.onGround) {
/* 164 */             this.boostTick = 8;
/* 165 */             mc.player.jump();
/* 166 */           } else if (this.boostTick < 8) {
/* 167 */             mc.player.jumpMovementFactor *= 1.04F;
/* 168 */             if (this.boostTick == 0) {
/* 169 */               double motion = isRuneAndSpeed ? 1.67D : (isSpeed ? 1.7D : (isRune ? 1.7D : 1.7605D));
/* 170 */               mc.player.motionX *= motion;
/* 171 */               mc.player.motionZ *= motion;
/* 172 */               mc.player.setPosition(x - Math.sin(yaw) * 0.003D, y, z + Math.cos(yaw) * 0.003D);
/*     */             } else {
/* 174 */               mc.player.motionY -= 0.0098D;
/*     */             } 
/* 176 */             this.boostTick++;
/*     */           } else {
/* 178 */             this.boostTick = 0;
/*     */           } 
/* 180 */         } else if (mode.equalsIgnoreCase("Matrix 6.0.4")) {
/* 181 */           if (mc.player.onGround) {
/* 182 */             mc.player.jump();
/*     */           } else {
/* 184 */             if (mc.player.ticksExisted % 5 == 0) {
/* 185 */               mc.player.jumpMovementFactor = 0.0F;
/* 186 */               mc.timer.timerSpeed = 0.6F;
/*     */             } 
/*     */             
/* 189 */             if (mc.player.ticksExisted % 5 == 0) {
/* 190 */               mc.player.jumpMovementFactor = 0.28F;
/* 191 */               mc.timer.timerSpeed = 1.0F;
/*     */             } 
/*     */             
/* 194 */             if (mc.player.ticksExisted % 10 == 0) {
/* 195 */               mc.player.jumpMovementFactor = 0.38F;
/*     */             }
/*     */             
/* 198 */             if (mc.player.ticksExisted % 20 == 0) {
/* 199 */               mc.player.jumpMovementFactor = 0.35F;
/* 200 */               mc.timer.timerSpeed = 1.1F;
/*     */             } 
/*     */           } 
/*     */         } 
/* 204 */       } else if (mode.equalsIgnoreCase("Old Sunrise")) {
/* 205 */         if (mc.player.onGround) {
/* 206 */           mc.player.jump();
/*     */         } else {
/* 208 */           if (mc.player.ticksExisted % 5 == 0) {
/* 209 */             mc.player.jumpMovementFactor = 0.0F;
/* 210 */             mc.timer.timerSpeed = 0.6F;
/*     */           } 
/*     */           
/* 213 */           if (mc.player.ticksExisted % 5 == 0) {
/* 214 */             mc.player.jumpMovementFactor = 0.28F;
/* 215 */             mc.timer.timerSpeed = 1.0F;
/*     */           } 
/*     */           
/* 218 */           if (mc.player.ticksExisted % 10 == 0) {
/* 219 */             mc.player.jumpMovementFactor = 0.38F;
/*     */           }
/*     */           
/* 222 */           if (mc.player.ticksExisted % 20 == 0) {
/* 223 */             mc.player.jumpMovementFactor = 0.35F;
/* 224 */             mc.timer.timerSpeed = 1.3F;
/*     */           } 
/*     */         } 
/* 227 */       } else if (mode.equalsIgnoreCase("Matrix Timer")) {
/* 228 */         mc.gameSettings.keyBindJump.pressed = false;
/* 229 */         if (this.strafing.getBoolValue()) {
/* 230 */           MovementHelper.strafePlayer(MovementHelper.getSpeed());
/*     */         }
/* 232 */         if (MovementHelper.isMoving()) {
/* 233 */           if (mc.player.onGround) {
/* 234 */             mc.player.jump();
/*     */           }
/* 236 */           if (!mc.player.onGround && mc.player.fallDistance <= 0.1F) {
/* 237 */             mc.timer.timerSpeed = 1.21F;
/*     */           }
/* 239 */           if (mc.player.fallDistance > 0.1F && mc.player.fallDistance < 1.3F) {
/* 240 */             mc.timer.timerSpeed = 1.0F;
/*     */           }
/*     */         } 
/* 243 */       } else if (mode.equalsIgnoreCase("MatrixOnGround")) {
/*     */         
/* 245 */         if (mc.player.onGround) {
/* 246 */           mc.timer.timerSpeed = 1.05F;
/* 247 */           mc.player.setPosition(mc.player.posX, mc.player.posY + 9.9999998245167E-15D, mc.player.posZ);
/* 248 */           mc.player.motionX *= 1.5499999523162842D;
/* 249 */           mc.player.motionZ *= 1.5499999523162842D;
/* 250 */           mc.player.motionY = 0.0D;
/* 251 */           mc.player.onGround = true;
/* 252 */         } else if (mc.player.fallDistance > 0.0F) {
/* 253 */           mc.player.motionY = -10000.0D;
/*     */         } 
/* 255 */       } else if (mode.equalsIgnoreCase("MatrixHop") && 
/* 256 */         mc.gameSettings.keyBindForward.isKeyDown()) {
/* 257 */         if (mc.player.onGround) {
/* 258 */           mc.player.jump();
/* 259 */           mc.timer.timerSpeed = 1.05F;
/* 260 */           mc.player.motionX *= 1.0071D;
/* 261 */           mc.player.motionZ *= 1.0071D;
/* 262 */           mc.player.moveStrafing *= 2.0F;
/*     */         }
/* 264 */         else if (!mc.player.onGround && mc.player.fallDistance <= 0.1F) {
/* 265 */           mc.player.motionY -= 0.0098D;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {
/* 275 */     String mode = this.speedMode.getOptions();
/* 276 */     if (mode.equalsIgnoreCase("Matrix Timer")) {
/* 277 */       if (this.boost.getBoolValue()) {
/* 278 */         mc.player.onGround = false;
/*     */       }
/* 280 */       mc.gameSettings.keyBindJump.pressed = false;
/* 281 */       mc.timer.timerSpeed = 6.78F;
/* 282 */       mc.player.jumpMovementFactor *= 1.04F;
/* 283 */       if (mc.player.motionY > 0.0D && !mc.player.onGround) {
/* 284 */         mc.player.motionY -= 0.00994D;
/*     */       } else {
/* 286 */         mc.player.motionY -= 0.00995D;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdateTwo(EventUpdate event) {
/* 293 */     String mode = this.speedMode.getOptions();
/* 294 */     if (mode.equalsIgnoreCase("Matrix Timer") || (mode.equalsIgnoreCase("Custom") && this.blink.getBoolValue())) {
/* 295 */       synchronized (this.positions) {
/* 296 */         this.positions.add(new double[] { mc.player.posX, (mc.player.getEntityBoundingBox()).minY, mc.player.posZ });
/*     */       } 
/* 298 */       if (this.pulseTimer.hasReached(600.0F)) {
/* 299 */         blink();
/* 300 */         this.pulseTimer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 307 */     this.boostTick = 0;
/* 308 */     String mode = this.speedMode.getOptions();
/* 309 */     if (mode.equalsIgnoreCase("Matrix Timer")) {
/* 310 */       synchronized (this.positions) {
/* 311 */         this.positions.add(new double[] { mc.player.posX, (mc.player.getEntityBoundingBox()).minY + (mc.player.getEyeHeight() / 2.0F), mc.player.posZ });
/* 312 */         this.positions.add(new double[] { mc.player.posX, (mc.player.getEntityBoundingBox()).minY, mc.player.posZ });
/*     */       } 
/*     */     }
/* 315 */     super.onEnable();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onSendPacket(EventSendPacket event) {
/* 320 */     String mode = this.speedMode.getOptions();
/* 321 */     if (mode.equalsIgnoreCase("Matrix Timer")) {
/* 322 */       if (mc.player == null || !(event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer) || this.disableLogger) {
/*     */         return;
/*     */       }
/* 325 */       event.setCancelled(true);
/* 326 */       if (!(event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer.Position) && !(event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer.PositionRotation)) {
/*     */         return;
/*     */       }
/* 329 */       this.packets.add(event.getPacket());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void blink() {
/*     */     try {
/* 335 */       this.disableLogger = true;
/* 336 */       Iterator<Packet<?>> packetIterator = this.packets.iterator();
/* 337 */       while (packetIterator.hasNext()) {
/* 338 */         mc.player.connection.sendPacket(packetIterator.next());
/* 339 */         packetIterator.remove();
/*     */       } 
/* 341 */       this.disableLogger = false;
/* 342 */     } catch (Exception e) {
/* 343 */       e.printStackTrace();
/* 344 */       this.disableLogger = false;
/*     */     } 
/* 346 */     synchronized (this.positions) {
/* 347 */       this.positions.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 353 */     mc.timer.timerSpeed = 1.0F;
/* 354 */     mc.player.speedInAir = 0.02F;
/* 355 */     String mode = this.speedMode.getOptions();
/* 356 */     if (mode.equalsIgnoreCase("YPort")) {
/* 357 */       mc.player.motionX = 0.0D;
/* 358 */       mc.player.motionZ = 0.0D;
/*     */     } 
/* 360 */     if (mode.equalsIgnoreCase("Matrix Timer")) {
/* 361 */       blink();
/*     */     }
/* 363 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\Speed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */