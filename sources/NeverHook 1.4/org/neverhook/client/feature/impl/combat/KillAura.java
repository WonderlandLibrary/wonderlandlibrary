/*     */ package org.neverhook.client.feature.impl.combat;
/*     */ import java.awt.Color;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketAnimation;
/*     */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*     */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*     */ import net.minecraft.network.play.client.CPacketUseEntity;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.util.glu.Cylinder;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.motion.EventJump;
/*     */ import org.neverhook.client.event.events.impl.motion.EventStrafe;
/*     */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.math.MathematicHelper;
/*     */ import org.neverhook.client.helpers.math.RotationHelper;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.helpers.player.InventoryHelper;
/*     */ import org.neverhook.client.helpers.player.KillAuraHelper;
/*     */ import org.neverhook.client.helpers.player.MovementHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.world.EntityHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ import org.neverhook.client.ui.notification.NotificationManager;
/*     */ import org.neverhook.client.ui.notification.NotificationType;
/*     */ 
/*     */ public class KillAura extends Feature {
/*  49 */   public static TimerHelper oldTimerPvp = new TimerHelper();
/*  50 */   public static TimerHelper timer = new TimerHelper();
/*     */   public static BooleanSetting players;
/*     */   public static BooleanSetting animals;
/*     */   public static BooleanSetting team;
/*     */   public static BooleanSetting invis;
/*     */   public static BooleanSetting pets;
/*     */   public static BooleanSetting monsters;
/*     */   public static BooleanSetting armorStands;
/*     */   public static BooleanSetting walls;
/*     */   public static BooleanSetting bypass;
/*     */   public static ColorSetting targetHudColor;
/*     */   public static EntityLivingBase target;
/*     */   public static NumberSetting range;
/*     */   public static NumberSetting fov;
/*     */   public static BooleanSetting onlyCrit;
/*     */   public static NumberSetting minAps;
/*     */   public static NumberSetting maxAps;
/*     */   public static NumberSetting hitChance;
/*     */   public static BooleanSetting autoDisable;
/*     */   public static BooleanSetting nakedPlayer;
/*     */   public static BooleanSetting sprinting;
/*     */   public static BooleanSetting weaponOnly;
/*     */   public static BooleanSetting usingItemCheck;
/*     */   public static BooleanSetting shieldBreaker;
/*     */   public static BooleanSetting autoBlock;
/*  75 */   public static BooleanSetting clientLook = new BooleanSetting("Client Look", false, () -> Boolean.valueOf(true));
/*     */   public static BooleanSetting visualYaw;
/*     */   public static BooleanSetting visualPitch;
/*     */   public static BooleanSetting shieldFixer;
/*  79 */   public static BooleanSetting randomRotation = new BooleanSetting("Random Rotation", true, () -> Boolean.valueOf(true));
/*  80 */   public static NumberSetting randomYaw = new NumberSetting("Random Yaw", 2.0F, 0.0F, 4.0F, 0.01F, () -> Boolean.valueOf(randomRotation.getBoolValue()));
/*  81 */   public static NumberSetting randomPitch = new NumberSetting("Random Pitch", 2.0F, 0.0F, 4.0F, 0.01F, () -> Boolean.valueOf(randomRotation.getBoolValue()));
/*     */   public static BooleanSetting shieldBlockCheck;
/*  83 */   public static BooleanSetting attackInInvetory = new BooleanSetting("Attack In Inventory", true, () -> Boolean.valueOf(true));
/*     */   public static BooleanSetting autoShieldUnPress;
/*     */   public static NumberSetting breakerDelay;
/*     */   public static NumberSetting critFallDistance;
/*  87 */   public static NumberSetting pitchValue = new NumberSetting("Pitch Value", 0.16F, -4.0F, 4.0F, 0.01F, () -> Boolean.valueOf(true));
/*  88 */   public static NumberSetting maxSpeedRotation = new NumberSetting("Max Speed Rotation", 360.0F, 0.0F, 360.0F, 1.0F, () -> Boolean.valueOf(true));
/*  89 */   public static NumberSetting minSpeedRotation = new NumberSetting("Min Speed Rotation", 360.0F, 0.0F, 360.0F, 1.0F, () -> Boolean.valueOf(true));
/*     */   public static NumberSetting attackCoolDown;
/*     */   public static BooleanSetting targetHud;
/*  92 */   public static BooleanSetting targetEsp = new BooleanSetting("Target Esp", true, () -> Boolean.valueOf(true));
/*  93 */   public static ListSetting targetEspMode = new ListSetting("Target Esp Mode", "Default", () -> Boolean.valueOf(targetEsp.getBoolValue()), new String[] { "Default", "Sims", "Jello", "Astolfo" });
/*  94 */   public static ColorSetting targetEspColor = new ColorSetting("TargetEsp Color", Color.RED.getRGB(), () -> Boolean.valueOf(targetEsp.getBoolValue()));
/*  95 */   public static NumberSetting points = new NumberSetting("Points", 32.0F, 2.0F, 64.0F, 1.0F, () -> Boolean.valueOf(targetEspMode.currentMode.equals("Astolfo")));
/*  96 */   public static NumberSetting circleRange = new NumberSetting("Circle Range", 3.0F, 0.1F, 6.0F, 0.01F, () -> Boolean.valueOf(targetEspMode.currentMode.equals("Astolfo")));
/*     */   public static ListSetting rotationMode;
/*     */   public static ListSetting targetHudMode;
/*     */   public static ListSetting rotationStrafeMode;
/*     */   public static ListSetting strafeMode;
/*     */   public static ListSetting sort;
/* 102 */   public static ListSetting swingMode = new ListSetting("Swing Mode", "Client", () -> Boolean.valueOf(true), new String[] { "Client", "None", "Server" });
/*     */   public static ListSetting clickMode;
/* 104 */   public static NumberSetting rotPredict = new NumberSetting("Rotation Predict", 0.05F, 0.0F, 10.0F, 0.001F, () -> Boolean.valueOf(true));
/* 105 */   public static BooleanSetting raycast = new BooleanSetting("Ray-Cast", true, () -> Boolean.valueOf(true));
/*     */ 
/*     */ 
/*     */   
/* 109 */   private final TimerHelper blockTimer = new TimerHelper();
/* 110 */   private final TimerHelper shieldBreakerTimer = new TimerHelper();
/*     */   
/*     */   private double circleAnim;
/*     */   
/*     */   private boolean isBlocking;
/*     */   private int changeSlotCounter;
/* 116 */   private double direction = 1.0D; private double yPos; private double progress = 0.0D;
/*     */   
/* 118 */   private float delta = 0.0F;
/*     */   
/* 120 */   private long lastMS = System.currentTimeMillis();
/* 121 */   private long lastDeltaMS = 0L;
/*     */   
/*     */   public KillAura() {
/* 124 */     super("KillAura", "Автоматически бьет сущностей вокруг тебя", Type.Combat);
/*     */     
/* 126 */     rotationMode = new ListSetting("Aim Mode", "Packet", () -> Boolean.valueOf(true), new String[] { "Packet", "None" });
/*     */     
/* 128 */     targetHudMode = new ListSetting("TargetHud Mode", "Astolfo", () -> Boolean.valueOf(targetHud.getBoolValue()), new String[] { "Astolfo", "Small", "Flux", "Novoline Old", "Novoline New" });
/*     */     
/* 130 */     rotationStrafeMode = new ListSetting("Rotation Strafe Mode", "Default", () -> Boolean.valueOf(!strafeMode.currentMode.equals("None")), new String[] { "Default", "Silent" });
/*     */     
/* 132 */     strafeMode = new ListSetting("Strafe Mode", "None", () -> Boolean.valueOf(true), new String[] { "None", "Always-F" });
/* 133 */     sort = new ListSetting("Priority", "Distance", () -> Boolean.valueOf(true), new String[] { "Distance", "Higher Armor", "Blocking Status", "Lowest Armor", "Health", "Angle", "HurtTime" });
/* 134 */     clickMode = new ListSetting("PvP Mode", "1.9", () -> Boolean.valueOf(true), new String[] { "1.9", "1.8" });
/* 135 */     visualYaw = new BooleanSetting("Visual Yaw", "Отображение визуальной ротации", true, () -> Boolean.valueOf(rotationMode.currentMode.equals("Packet")));
/* 136 */     visualPitch = new BooleanSetting("Visual Pitch", "Отображение визуальной ротации", true, () -> Boolean.valueOf(rotationMode.currentMode.equals("Packet")));
/* 137 */     fov = new NumberSetting("FOV", "Позволяет редактировать радиус в котором вы можете ударить игрока", 180.0F, 5.0F, 180.0F, 5.0F, () -> Boolean.valueOf(true));
/* 138 */     attackCoolDown = new NumberSetting("Cool Down", "Редактирует скорость удара", 0.85F, 0.1F, 1.0F, 0.01F, () -> Boolean.valueOf(clickMode.currentMode.equals("1.9")));
/* 139 */     minAps = new NumberSetting("Min CPS", "Минимальное количество кликов в секунду", 12.0F, 1.0F, 20.0F, 1.0F, () -> Boolean.valueOf(clickMode.currentMode.equals("1.8")), NumberSetting.NumberType.APS);
/* 140 */     maxAps = new NumberSetting("Max CPS", "Максимальное количество кликов в секунду", 13.0F, 1.0F, 20.0F, 1.0F, () -> Boolean.valueOf(clickMode.currentMode.equals("1.8")), NumberSetting.NumberType.APS);
/* 141 */     hitChance = new NumberSetting("HitChance", "Шанс удара", 100.0F, 1.0F, 100.0F, 5.0F, () -> Boolean.valueOf(true));
/* 142 */     range = new NumberSetting("AttackRange", "Дистанция в которой вы можете ударить игрока", 3.6F, 3.0F, 6.0F, 0.1F, () -> Boolean.valueOf(true));
/* 143 */     players = new BooleanSetting("Attack Players", "Позволяет бить игроков", true, () -> Boolean.valueOf(true));
/* 144 */     armorStands = new BooleanSetting("Attack Armor Stands", "Позволяет бить армор-стенды", true, () -> Boolean.valueOf(true));
/* 145 */     monsters = new BooleanSetting("Attack Monsters", "Позволяет бить монстров", true, () -> Boolean.valueOf(true));
/* 146 */     pets = new BooleanSetting("Attack Pets", "Позволяет бить прирученных животных", true, () -> Boolean.valueOf(true));
/* 147 */     animals = new BooleanSetting("Attack Animals", "Позволяет бить безобидных мобов", false, () -> Boolean.valueOf(true));
/* 148 */     team = new BooleanSetting("Attack Teams", "Позволяет бить тимейтов на мини-играх", false, () -> Boolean.valueOf(true));
/* 149 */     invis = new BooleanSetting("Attack Invisible", "Позволяет бить невидемых существ", true, () -> Boolean.valueOf(true));
/* 150 */     nakedPlayer = new BooleanSetting("Ignore Naked Players", "Не бьет голых игроков", false, () -> Boolean.valueOf(true));
/* 151 */     walls = new BooleanSetting("Walls", "Позволяет бить сквозь стены", true, () -> Boolean.valueOf(true));
/* 152 */     bypass = new BooleanSetting("Hit Through Walls Bypass", "Обход ударов сквозь стену", true, () -> Boolean.valueOf(true));
/* 153 */     sprinting = new BooleanSetting("Stop Sprinting", "Автоматически выключает спринт", false, () -> Boolean.valueOf(true));
/* 154 */     weaponOnly = new BooleanSetting("Weapon Only", "Позволяет бить только с оружием в руках", false, () -> Boolean.valueOf(true));
/* 155 */     usingItemCheck = new BooleanSetting("Using Item Check", "Не бьет если вы используете меч, еду и т.д", false, () -> Boolean.valueOf(true));
/* 156 */     shieldBreaker = new BooleanSetting("Break Shield", "Автоматически ломает щит сопернику", false, () -> Boolean.valueOf(true));
/* 157 */     shieldFixer = new BooleanSetting("Shield Sync Fix", "Позволяет бить игроков через щит (обход)", false, () -> Boolean.valueOf(true));
/* 158 */     breakerDelay = new NumberSetting("Breaker Delay", "Регулировка ломания щита", 100.0F, 0.0F, 600.0F, 10.0F, () -> Boolean.valueOf(shieldBreaker.getBoolValue()));
/* 159 */     autoShieldUnPress = new BooleanSetting("Auto Shield UnPress", "Автоматически отжимает щит если у соперника топор в руках", false, () -> Boolean.valueOf(true));
/* 160 */     shieldBlockCheck = new BooleanSetting("Shield Block Check", "Не бьет соперника если он прикрыт щитом", false, () -> Boolean.valueOf(true));
/* 161 */     autoBlock = new BooleanSetting("Auto Block", "Автоматически жмет пкм при ударе (нужно для 1.8 серверов)", false, () -> Boolean.valueOf(true));
/* 162 */     autoDisable = new BooleanSetting("Auto Disable", "Автоматически выключает киллаура при смерти и т.д", false, () -> Boolean.valueOf(true));
/* 163 */     onlyCrit = new BooleanSetting("Only Critical", "Бьет в нужный момент для крита", false, () -> Boolean.valueOf(true));
/* 164 */     critFallDistance = new NumberSetting("Criticals Fall Distance", "Регулировка дистанции до земли для крита", 0.2F, 0.1F, 1.0F, 0.01F, () -> Boolean.valueOf(onlyCrit.getBoolValue()));
/* 165 */     targetHud = new BooleanSetting("TargetHUD", "Отображает хп, еду, броню соперника на экране", true, () -> Boolean.valueOf(true));
/* 166 */     targetHudColor = new ColorSetting("TargetHUD Color", Color.PINK.getRGB(), () -> Boolean.valueOf((targetHud.getBoolValue() && (targetHudMode.currentMode.equals("Astolfo") || targetHudMode.currentMode.equals("Novoline Old") || targetHudMode.currentMode.equals("Novoline New")))));
/*     */     
/* 168 */     addSettings(new Setting[] { (Setting)rotationMode, (Setting)swingMode, (Setting)clickMode, (Setting)sort, (Setting)attackCoolDown, (Setting)minAps, (Setting)maxAps, (Setting)strafeMode, (Setting)rotationStrafeMode, (Setting)targetHud, (Setting)targetHudMode, (Setting)targetHudColor, (Setting)targetEsp, (Setting)targetEspMode, (Setting)targetEspColor, (Setting)circleRange, (Setting)points, (Setting)fov, (Setting)range, (Setting)hitChance, (Setting)rotPredict, (Setting)minSpeedRotation, (Setting)maxSpeedRotation, (Setting)pitchValue, (Setting)randomRotation, (Setting)randomYaw, (Setting)randomPitch, (Setting)visualYaw, (Setting)visualPitch, (Setting)clientLook, (Setting)raycast, (Setting)players, (Setting)armorStands, (Setting)monsters, (Setting)animals, (Setting)pets, (Setting)invis, (Setting)team, (Setting)nakedPlayer, (Setting)attackInInvetory, (Setting)walls, (Setting)bypass, (Setting)sprinting, (Setting)weaponOnly, (Setting)usingItemCheck, (Setting)autoShieldUnPress, (Setting)shieldBlockCheck, (Setting)shieldBreaker, (Setting)breakerDelay, (Setting)shieldFixer, (Setting)autoBlock, (Setting)autoDisable, (Setting)onlyCrit, (Setting)critFallDistance });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean canApsAttack() {
/* 175 */     int apsMultiplier = (int)(14.0F / MathematicHelper.randomizeFloat((int)maxAps.getNumberValue(), (int)minAps.getNumberValue()));
/* 176 */     if (oldTimerPvp.hasReached((50 * apsMultiplier))) {
/* 177 */       oldTimerPvp.reset();
/* 178 */       return true;
/*     */     } 
/* 180 */     return false;
/*     */   }
/*     */   
/*     */   public static Color setAlpha(Color color, int alpha) {
/* 184 */     alpha = MathHelper.clamp(alpha, 0, 255);
/* 185 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
/*     */   }
/*     */   
/*     */   public static float changer(float current, float add, float min, float max) {
/* 189 */     current += add;
/* 190 */     if (current > max) {
/* 191 */       current = max;
/*     */     }
/* 193 */     if (current < min) {
/* 194 */       current = min;
/*     */     }
/*     */     
/* 197 */     return current;
/*     */   }
/*     */   
/*     */   public static void pre3D() {
/* 201 */     GL11.glPushMatrix();
/* 202 */     GL11.glEnable(3042);
/* 203 */     GL11.glBlendFunc(770, 771);
/* 204 */     GL11.glShadeModel(7425);
/* 205 */     GL11.glDisable(3553);
/* 206 */     GL11.glEnable(2848);
/* 207 */     GL11.glDisable(2929);
/* 208 */     GL11.glDisable(2896);
/* 209 */     GL11.glDepthMask(false);
/* 210 */     GL11.glHint(3154, 4354);
/* 211 */     GL11.glDisable(2884);
/*     */   }
/*     */   
/*     */   public static void post3D() {
/* 215 */     GL11.glDepthMask(true);
/* 216 */     GL11.glEnable(2929);
/* 217 */     GL11.glDisable(2848);
/* 218 */     GL11.glEnable(3553);
/* 219 */     GL11.glDisable(3042);
/* 220 */     GL11.glPopMatrix();
/* 221 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onStrafe(EventStrafe eventStrafe) {
/* 226 */     String mode = strafeMode.getOptions();
/* 227 */     String rotStrafeMode = rotationStrafeMode.getOptions();
/* 228 */     float strafe = eventStrafe.getStrafe();
/* 229 */     float forward = eventStrafe.getForward();
/* 230 */     float friction = eventStrafe.getFriction();
/*     */     
/* 232 */     if (target.getHealth() > 0.0F && 
/* 233 */       mode.equalsIgnoreCase("Always-F")) {
/* 234 */       if (rotStrafeMode.equalsIgnoreCase("Silent")) {
/* 235 */         eventStrafe.setCancelled(true);
/* 236 */         MovementHelper.calculateSilentMove(eventStrafe, RotationHelper.Rotation.packetYaw);
/* 237 */       } else if (rotStrafeMode.equalsIgnoreCase("Default")) {
/* 238 */         eventStrafe.setCancelled(true);
/* 239 */         float f = strafe * strafe + forward * forward;
/* 240 */         if (f >= 1.0E-4F) {
/* 241 */           f = (float)(friction / Math.max(1.0D, Math.sqrt(f)));
/* 242 */           strafe *= f;
/* 243 */           forward *= f;
/* 244 */           float yawSin = MathHelper.sin(RotationHelper.Rotation.packetYaw * 3.1415927F / 180.0F);
/* 245 */           float yawCos = MathHelper.cos(RotationHelper.Rotation.packetYaw * 3.1415927F / 180.0F);
/* 246 */           mc.player.motionX += (strafe * yawCos - forward * yawSin);
/* 247 */           mc.player.motionZ += (forward * yawCos + strafe * yawSin);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onJumpMotion(EventJump eventJump) {
/* 256 */     String mode = strafeMode.getOptions();
/* 257 */     if (target == null)
/*     */       return; 
/* 259 */     if (target.getHealth() > 0.0F && 
/* 260 */       mode.equalsIgnoreCase("Always-F")) {
/* 261 */       eventJump.setYaw(RotationHelper.Rotation.packetYaw);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onPreAttack(EventPreMotion event) {
/* 269 */     if (mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer && !attackInInvetory.getBoolValue()) {
/*     */       return;
/*     */     }
/* 272 */     if (mc.player.getHealth() > 0.0F) {
/*     */ 
/*     */ 
/*     */       
/* 276 */       if (autoDisable.getBoolValue()) {
/* 277 */         KillAuraHelper.toggleOffChecks();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 282 */       String mode = rotationMode.getOptions();
/*     */       
/* 284 */       setSuffix(mode + ", " + MathematicHelper.round(range.getNumberValue(), 1));
/*     */ 
/*     */ 
/*     */       
/* 288 */       target = KillAuraHelper.getSortEntities();
/*     */ 
/*     */ 
/*     */       
/* 292 */       if (target == null) {
/*     */         return;
/*     */       }
/*     */       
/* 296 */       float yaw = rotationMode.currentMode.equals("Packet") ? RotationHelper.Rotation.packetYaw : mc.player.rotationYaw;
/* 297 */       float pitch = rotationMode.currentMode.equals("Packet") ? RotationHelper.Rotation.packetPitch : mc.player.rotationPitch;
/*     */       
/* 299 */       if (!RotationHelper.isLookingAtEntity(yaw, pitch, 0.06F, 0.06F, 0.06F, (Entity)target, range.getNumberValue()) && raycast.getBoolValue()) {
/*     */         return;
/*     */       }
/*     */       
/* 303 */       if (mc.player.isUsingItem() && usingItemCheck.getBoolValue()) {
/*     */         return;
/*     */       }
/*     */       
/* 307 */       if (!(mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword) && !(mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemAxe) && weaponOnly.getBoolValue()) {
/*     */         return;
/*     */       }
/*     */       
/* 311 */       if (target.isActiveItemStackBlocking() && target.isBlocking() && target.isHandActive() && (target.getHeldItemOffhand().getItem() instanceof net.minecraft.item.ItemShield || target.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemShield) && shieldBlockCheck.getBoolValue()) {
/*     */         return;
/*     */       }
/*     */     }
/* 315 */     else if (autoDisable.getBoolValue()) {
/* 316 */       state();
/* 317 */       NotificationManager.publicity("KillAura", "KillAura was disabled because of Death!", 3, NotificationType.INFO);
/*     */     } 
/*     */ 
/*     */     
/* 321 */     if (MovementHelper.isBlockAboveHead()) {
/* 322 */       if (mc.player.fallDistance < critFallDistance.getNumberValue() && mc.player.getCooledAttackStrength(0.8F) == 1.0F && onlyCrit.getBoolValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb) {
/*     */         return;
/*     */       }
/*     */     }
/* 326 */     else if (mc.player.fallDistance != 0.0F && onlyCrit.getBoolValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb) {
/*     */       return;
/*     */     } 
/*     */     
/* 330 */     attackEntitySuccess(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 335 */     target = null;
/* 336 */     if (this.isBlocking) {
/* 337 */       mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
/* 338 */       this.isBlocking = false;
/*     */     } 
/* 340 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onSendPacket(EventSendPacket event) {
/* 345 */     if (event.getPacket() instanceof CPacketUseEntity) {
/* 346 */       CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)event.getPacket();
/*     */       
/* 348 */       if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT) {
/* 349 */         event.setCancelled(true);
/*     */       }
/*     */       
/* 352 */       if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
/* 353 */         event.setCancelled(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {
/* 360 */     if (autoShieldUnPress.getBoolValue()) {
/* 361 */       if (target.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemAxe) {
/* 362 */         if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
/* 363 */           mc.gameSettings.keyBindUseItem.pressed = false;
/*     */         }
/*     */       } else {
/* 366 */         mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onAttackSilent(EventAttackSilent eventAttackSilent) {
/* 373 */     if (mc.player.isBlocking() && mc.player.getHeldItemOffhand().getItem() instanceof net.minecraft.item.ItemShield && shieldFixer.getBoolValue()) {
/* 374 */       mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-0.8D, -0.8D, -0.8D), EnumFacing.DOWN));
/* 375 */       mc.playerController.processRightClick((EntityPlayer)mc.player, (World)mc.world, EnumHand.OFF_HAND);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void attackEntitySuccess(EntityLivingBase target) {
/* 380 */     if (mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer && !attackInInvetory.getBoolValue())
/*     */       return; 
/* 382 */     if (target.getHealth() > 0.0F) {
/* 383 */       float attackDelay; switch (clickMode.getOptions()) {
/*     */         case "1.9":
/* 385 */           attackDelay = attackCoolDown.getNumberValue();
/* 386 */           if (mc.player.getCooledAttackStrength(attackDelay) == 1.0F && 
/* 387 */             ThreadLocalRandom.current().nextInt(100) < hitChance.getNumberValue()) {
/* 388 */             mc.playerController.attackEntity((EntityPlayer)mc.player, EntityHelper.rayCast((Entity)target, range.getNumberValue()));
/* 389 */             if (swingMode.currentMode.equals("Client")) {
/* 390 */               mc.player.swingArm(EnumHand.MAIN_HAND); break;
/* 391 */             }  if (swingMode.currentMode.equals("Server")) {
/* 392 */               mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
/*     */             }
/*     */           } 
/*     */           break;
/*     */         
/*     */         case "1.8":
/* 398 */           if (canApsAttack()) {
/* 399 */             if (this.isBlocking && autoBlock.getBoolValue() && mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword && 
/* 400 */               this.blockTimer.hasReached(100.0F)) {
/* 401 */               mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
/* 402 */               this.isBlocking = false;
/* 403 */               this.blockTimer.reset();
/*     */             } 
/*     */             
/* 406 */             if (ThreadLocalRandom.current().nextInt(100) < hitChance.getNumberValue()) {
/* 407 */               mc.playerController.attackEntity((EntityPlayer)mc.player, EntityHelper.rayCast((Entity)target, range.getNumberValue()));
/* 408 */               if (swingMode.currentMode.equals("Client")) {
/* 409 */                 mc.player.swingArm(EnumHand.MAIN_HAND); break;
/* 410 */               }  if (swingMode.currentMode.equals("Server")) {
/* 411 */                 mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
/*     */               }
/*     */             } 
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRotations(EventPreMotion event) {
/* 422 */     if (mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer && !attackInInvetory.getBoolValue()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 427 */     String mode = rotationMode.getOptions();
/*     */ 
/*     */ 
/*     */     
/* 431 */     if (target == null) {
/*     */       return;
/*     */     }
/*     */     
/* 435 */     if (target.getHealth() > 0.0F) {
/*     */ 
/*     */ 
/*     */       
/* 439 */       if (!(mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword) && !(mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemAxe) && weaponOnly.getBoolValue()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 444 */       float[] rots = RotationHelper.getRotations((Entity)target, randomRotation.getBoolValue(), maxSpeedRotation.getNumberValue(), minSpeedRotation.getNumberValue(), randomYaw.getNumberValue(), randomPitch.getNumberValue());
/* 445 */       float[] rotVisual = RotationHelper.getRotations((Entity)target, false, maxSpeedRotation.getNumberValue(), minSpeedRotation.getNumberValue(), randomYaw.getNumberValue(), randomPitch.getNumberValue());
/*     */       
/* 447 */       if (mode.equalsIgnoreCase("Packet")) {
/* 448 */         event.setYaw(rots[0]);
/* 449 */         event.setPitch(rots[1]);
/*     */         
/* 451 */         if (visualYaw.getBoolValue()) {
/* 452 */           mc.player.renderYawOffset = rots[0];
/* 453 */           mc.player.rotationYawHead = rots[0];
/*     */         } 
/* 455 */         if (visualPitch.getBoolValue()) {
/* 456 */           mc.player.rotationPitchHead = rots[1];
/*     */         }
/*     */         
/* 459 */         if (clientLook.getBoolValue()) {
/* 460 */           mc.player.rotationYaw = rotVisual[0];
/* 461 */           mc.player.rotationPitch = rotVisual[1];
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPostMotion(EventPostMotion event) {
/* 469 */     if (target == null)
/*     */       return; 
/* 471 */     if (mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword && !this.isBlocking && autoBlock.getBoolValue()) {
/* 472 */       mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
/* 473 */       this.isBlocking = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/* 479 */     if (target == null)
/*     */       return; 
/* 481 */     if (!InventoryHelper.doesHotbarHaveAxe()) {
/*     */       return;
/*     */     }
/* 484 */     float yaw = rotationMode.currentMode.equals("Packet") ? RotationHelper.Rotation.packetYaw : mc.player.rotationYaw;
/* 485 */     float pitch = rotationMode.currentMode.equals("Packet") ? RotationHelper.Rotation.packetPitch : mc.player.rotationPitch;
/* 486 */     if (shieldBreaker.getBoolValue() && (target.getHeldItemOffhand().getItem() instanceof net.minecraft.item.ItemShield || target.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemShield)) {
/* 487 */       if (target.isHandActive()) {
/* 488 */         if (target.canEntityBeSeen((Entity)mc.player) && mc.player.canEntityBeSeen((Entity)target) && RotationHelper.isLookingAtEntity(yaw, pitch, 0.2F, 0.2F, 0.2F, (Entity)target, range.getNumberValue())) {
/* 489 */           if (this.shieldBreakerTimer.hasReached(breakerDelay.getNumberValue())) {
/* 490 */             if (mc.player.inventory.currentItem != InventoryHelper.getAxe()) {
/* 491 */               mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(mc.player.inventory.currentItem = InventoryHelper.getAxe()));
/* 492 */               mc.player.resetCooldown();
/*     */             } 
/* 494 */             this.shieldBreakerTimer.reset();
/*     */           } 
/* 496 */           if (mc.player.inventory.currentItem == InventoryHelper.getAxe()) {
/* 497 */             mc.playerController.attackEntity((EntityPlayer)mc.player, EntityHelper.rayCast((Entity)target, range.getNumberValue()));
/* 498 */             mc.player.swingArm(EnumHand.MAIN_HAND);
/* 499 */             mc.player.resetCooldown();
/* 500 */             this.changeSlotCounter = -1;
/*     */           } else {
/* 502 */             this.changeSlotCounter = 0;
/*     */           } 
/*     */         } 
/* 505 */       } else if (mc.player.inventory.currentItem != InventoryHelper.getSwordAtHotbar() && this.changeSlotCounter == -1 && InventoryHelper.getSwordAtHotbar() != -1 && (!target.isBlocking() || !target.isHandActive() || !target.isActiveItemStackBlocking())) {
/* 506 */         mc.player.resetCooldown();
/* 507 */         mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(mc.player.inventory.currentItem = InventoryHelper.getSwordAtHotbar()));
/* 508 */         this.changeSlotCounter = 0;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdateA(EventUpdate event) {
/* 515 */     if (targetEspMode.currentMode.equals("Jello")) {
/* 516 */       this.delta = changer(this.delta, (target != null) ? 0.1F : -0.1F, 0.0F, 0.007843138F);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender3D(EventRender3D event) {
/* 522 */     if (target != null && targetEsp.getBoolValue()) {
/* 523 */       double x; double y; double z; double heightOffset; double yOffset; double lastY; float radius; double posX; AxisAlignedBB axisAlignedBB; double d1; double posY; double height; double posZ; double d2; Cylinder c; double d3; double d4; double deltaY; Color color; float r; float g; float b; int i; switch (targetEspMode.currentMode) {
/*     */         case "Default":
/* 525 */           x = target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosX;
/* 526 */           y = target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosY;
/* 527 */           z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosZ;
/* 528 */           heightOffset = target.getEyeHeight() + 0.35D;
/* 529 */           yOffset = target.isSneaking() ? 0.25D : 0.0D;
/* 530 */           GL11.glPushMatrix();
/* 531 */           GlStateManager.enable(3042);
/* 532 */           GlStateManager.blendFunc(770, 771);
/* 533 */           GlStateManager.translate((x -= 0.5D) + 0.5D, (y += heightOffset - yOffset) + 0.5D, (z -= 0.5D) + 0.5D);
/* 534 */           GlStateManager.rotate(-target.rotationYaw % 360.0F, 0.0F, 1.0F, 0.0F);
/* 535 */           GlStateManager.translate(-(x + 0.5D), -(y + 0.5D), -(z + 0.5D));
/* 536 */           GlStateManager.disable(3553);
/* 537 */           GlStateManager.enable(2848);
/* 538 */           GlStateManager.disable(2929);
/* 539 */           GlStateManager.depthMask(false);
/* 540 */           RenderHelper.setColor(targetEspColor.getColorValue());
/* 541 */           RenderHelper.drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 0.05D, z + 1.0D), 0.0F, 0.0F, 0.0F, 0.0F);
/* 542 */           GlStateManager.disable(2848);
/* 543 */           GlStateManager.enable(3553);
/* 544 */           GlStateManager.enable(2929);
/* 545 */           GlStateManager.depthMask(true);
/* 546 */           GlStateManager.disable(3042);
/* 547 */           GL11.glPopMatrix();
/*     */         
/*     */         case "Jello":
/* 550 */           lastY = this.yPos;
/*     */           
/* 552 */           if (this.delta > 0.0F) {
/* 553 */             if (System.currentTimeMillis() - this.lastMS >= 1000L) {
/* 554 */               this.direction = -this.direction;
/* 555 */               this.lastMS = System.currentTimeMillis();
/*     */             } 
/* 557 */             long weird = (this.direction > 0.0D) ? (System.currentTimeMillis() - this.lastMS) : (1000L - System.currentTimeMillis() - this.lastMS);
/* 558 */             this.progress = weird / 1000.0D;
/* 559 */             this.lastDeltaMS = System.currentTimeMillis() - this.lastMS;
/*     */           } else {
/* 561 */             this.lastMS = System.currentTimeMillis() - this.lastDeltaMS;
/*     */           } 
/*     */           
/* 564 */           axisAlignedBB = target.getEntityBoundingBox();
/*     */           
/* 566 */           d1 = axisAlignedBB.maxX - axisAlignedBB.minX;
/* 567 */           height = axisAlignedBB.maxY - axisAlignedBB.minY;
/* 568 */           d2 = target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks;
/* 569 */           d3 = target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks;
/* 570 */           d4 = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks;
/*     */           
/* 572 */           this.yPos = easeInOutQuart(this.progress) * height;
/*     */           
/* 574 */           deltaY = ((this.direction > 0.0D) ? (this.yPos - lastY) : (lastY - this.yPos)) * -this.direction * 5.0D;
/*     */           
/* 576 */           color = new Color(targetEspColor.getColorValue());
/* 577 */           r = color.getRed() / 255.0F;
/* 578 */           g = color.getGreen() / 255.0F;
/* 579 */           b = color.getBlue() / 255.0F;
/*     */           
/* 581 */           pre3D();
/* 582 */           GL11.glTranslated(-(mc.getRenderManager()).renderPosX, -(mc.getRenderManager()).renderPosY, -(mc.getRenderManager()).renderPosZ);
/*     */           
/* 584 */           GL11.glBegin(8);
/*     */           
/* 586 */           for (i = 0; i <= 360; i++) {
/* 587 */             double calc = i * Math.PI / 180.0D;
/* 588 */             double posX2 = d2 - Math.sin(calc) * d1;
/* 589 */             double posZ2 = d4 + Math.cos(calc) * d1;
/*     */             
/* 591 */             GL11.glColor4f(r, g, b, 0.0F);
/* 592 */             GL11.glVertex3d(posX2, d3 + this.yPos + deltaY, posZ2);
/*     */             
/* 594 */             GL11.glColor4f(r, g, b, this.delta * 255.0F);
/* 595 */             GL11.glVertex3d(posX2, d3 + this.yPos, posZ2);
/*     */           } 
/*     */           
/* 598 */           GL11.glEnd();
/*     */           
/* 600 */           drawCircle(d2, d3 + this.yPos, d4, d1, r, g, b, this.delta * 130.0F);
/*     */           
/* 602 */           post3D();
/*     */           break;
/*     */         
/*     */         case "Sims":
/* 606 */           radius = 0.15F;
/* 607 */           GL11.glPushMatrix();
/* 608 */           posX = target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks;
/* 609 */           posY = target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks;
/* 610 */           posZ = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks;
/* 611 */           GL11.glTranslated(posX, posY, posZ);
/* 612 */           GL11.glRotatef(-target.width, 0.0F, 1.0F, 0.0F);
/* 613 */           pre3D();
/* 614 */           RenderHelper.setColor(targetEspColor.getColorValue());
/* 615 */           GL11.glLineWidth(1.5F);
/* 616 */           c = new Cylinder();
/* 617 */           GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
/* 618 */           c.draw(0.0F, radius, 0.3F, 4, 1);
/* 619 */           c.setDrawStyle(100012);
/* 620 */           GL11.glTranslated(0.0D, 0.0D, 0.30000001192092896D);
/* 621 */           c.draw(radius, 0.0F, 0.3F, 4, 1);
/* 622 */           GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
/* 623 */           GL11.glTranslated(0.0D, 0.0D, -0.30000001192092896D);
/* 624 */           c.draw(0.0F, radius, 0.3F, 4, 1);
/* 625 */           GL11.glTranslated(0.0D, 0.0D, 0.30000001192092896D);
/* 626 */           c.draw(radius, 0.0F, 0.3F, 4, 1);
/* 627 */           post3D();
/* 628 */           GL11.glPopMatrix();
/*     */           break;
/*     */         case "Astolfo":
/* 631 */           if (target != null) {
/* 632 */             if (target.getHealth() > 0.0F) {
/* 633 */               this.circleAnim += 0.014999999664723873D * Minecraft.frameTime / 10.0D;
/* 634 */               RenderHelper.drawCircle3D((Entity)target, this.circleAnim + 0.001D, event.getPartialTicks(), (int)points.getNumberValue(), 4.0F, Color.black.getRGB());
/* 635 */               RenderHelper.drawCircle3D((Entity)target, this.circleAnim - 0.001D, event.getPartialTicks(), (int)points.getNumberValue(), 4.0F, Color.black.getRGB());
/* 636 */               RenderHelper.drawCircle3D((Entity)target, this.circleAnim, event.getPartialTicks(), (int)points.getNumberValue(), 2.0F, targetEspColor.getColorValue());
/* 637 */               this.circleAnim = MathHelper.clamp(this.circleAnim, 0.0D, circleRange.getNumberValue()); break;
/*     */             } 
/* 639 */             this.circleAnim = 0.0D;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawCircle(double x, double y, double z, double radius, float red, float green, float blue, float alp) {
/* 648 */     GL11.glLineWidth(3.0F);
/* 649 */     GL11.glBegin(2);
/* 650 */     GL11.glColor4f(red, green, blue, alp);
/*     */     
/* 652 */     for (int i = 0; i <= 360; i++) {
/* 653 */       double posX = x - Math.sin(i * Math.PI / 180.0D) * radius;
/* 654 */       double posZ = z + Math.cos(i * Math.PI / 180.0D) * radius;
/* 655 */       GL11.glVertex3d(posX, y, posZ);
/*     */     } 
/*     */     
/* 658 */     GL11.glEnd();
/*     */   }
/*     */   
/*     */   public static double easeInOutQuart(double x) {
/* 662 */     return (x < 0.5D) ? (8.0D * x * x * x * x) : (1.0D - Math.pow(-2.0D * x + 2.0D, 4.0D) / 2.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\KillAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */