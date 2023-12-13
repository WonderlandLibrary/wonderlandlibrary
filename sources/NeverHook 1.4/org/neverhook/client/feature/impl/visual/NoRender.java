/*     */ package org.neverhook.client.feature.impl.visual;
/*     */ 
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.world.EnumSkyBlock;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*     */ import org.neverhook.client.event.events.impl.render.EventRenderEntity;
/*     */ import org.neverhook.client.event.events.impl.render.EventRenderWorldLight;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ 
/*     */ public class NoRender
/*     */   extends Feature
/*     */ {
/*     */   public static BooleanSetting rain;
/*     */   public static BooleanSetting hurt;
/*     */   public static BooleanSetting pumpkin;
/*     */   public static BooleanSetting armor;
/*     */   public static BooleanSetting totem;
/*     */   public static BooleanSetting blindness;
/*     */   public static BooleanSetting cameraSmooth;
/*     */   public static BooleanSetting cameraBounds;
/*     */   public static BooleanSetting fire;
/*     */   public static BooleanSetting light;
/*     */   public static BooleanSetting fog;
/*     */   public static BooleanSetting armorStand;
/*     */   public static BooleanSetting bossBar;
/*     */   public static BooleanSetting tnt;
/*     */   public static BooleanSetting crystal;
/*     */   public static BooleanSetting fireworks;
/*     */   public static BooleanSetting swing;
/*     */   public static BooleanSetting sign;
/*     */   public static BooleanSetting frame;
/*     */   public static BooleanSetting banner;
/*     */   public static BooleanSetting glintEffect;
/*     */   public static BooleanSetting chatRect;
/*     */   
/*     */   public NoRender() {
/*  42 */     super("NoOverlay", "Убирает опредленные элементы рендера в игре", Type.Visuals);
/*  43 */     rain = new BooleanSetting("Rain", true, () -> Boolean.valueOf(true));
/*  44 */     hurt = new BooleanSetting("HurtCamera", true, () -> Boolean.valueOf(true));
/*  45 */     pumpkin = new BooleanSetting("Pumpkin", true, () -> Boolean.valueOf(true));
/*  46 */     armor = new BooleanSetting("Armor", false, () -> Boolean.valueOf(true));
/*  47 */     totem = new BooleanSetting("Totem", true, () -> Boolean.valueOf(true));
/*  48 */     blindness = new BooleanSetting("Blindness", true, () -> Boolean.valueOf(true));
/*  49 */     cameraSmooth = new BooleanSetting("Camera Smooth", true, () -> Boolean.valueOf(true));
/*  50 */     cameraBounds = new BooleanSetting("Camera Bounds", false, () -> Boolean.valueOf(true));
/*  51 */     fire = new BooleanSetting("Fire", true, () -> Boolean.valueOf(true));
/*  52 */     light = new BooleanSetting("Light", false, () -> Boolean.valueOf(true));
/*  53 */     fog = new BooleanSetting("Fog", false, () -> Boolean.valueOf(true));
/*  54 */     armorStand = new BooleanSetting("Armor Stand", false, () -> Boolean.valueOf(true));
/*  55 */     bossBar = new BooleanSetting("Boss Bar", true, () -> Boolean.valueOf(true));
/*  56 */     tnt = new BooleanSetting("Tnt", false, () -> Boolean.valueOf(true));
/*  57 */     crystal = new BooleanSetting("Crystal", false, () -> Boolean.valueOf(true));
/*  58 */     fireworks = new BooleanSetting("FireWorks", false, () -> Boolean.valueOf(true));
/*  59 */     swing = new BooleanSetting("Swing", false, () -> Boolean.valueOf(true));
/*  60 */     sign = new BooleanSetting("Sign", false, () -> Boolean.valueOf(true));
/*  61 */     frame = new BooleanSetting("Frame", false, () -> Boolean.valueOf(true));
/*  62 */     banner = new BooleanSetting("Banner", false, () -> Boolean.valueOf(true));
/*  63 */     glintEffect = new BooleanSetting("Glint Effect", false, () -> Boolean.valueOf(true));
/*  64 */     chatRect = new BooleanSetting("Chat Rect", false, () -> Boolean.valueOf(false));
/*  65 */     addSettings(new Setting[] { (Setting)rain, (Setting)hurt, (Setting)pumpkin, (Setting)armor, (Setting)totem, (Setting)blindness, (Setting)cameraSmooth, (Setting)fire, (Setting)light, (Setting)fog, (Setting)armorStand, (Setting)bossBar, (Setting)tnt, (Setting)crystal, (Setting)fireworks, (Setting)swing, (Setting)sign, (Setting)frame, (Setting)banner, (Setting)glintEffect });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onEntityRenderer(EventRenderEntity event) {
/*  70 */     if (!getState())
/*     */       return; 
/*  72 */     if (event.getEntity() != null) {
/*  73 */       if (fireworks.getBoolValue() && event.getEntity() instanceof net.minecraft.entity.item.EntityFireworkRocket) {
/*  74 */         event.setCancelled(true);
/*  75 */       } else if (crystal.getBoolValue() && event.getEntity() instanceof net.minecraft.entity.item.EntityEnderCrystal) {
/*  76 */         event.setCancelled(true);
/*  77 */       } else if (tnt.getBoolValue() && event.getEntity() instanceof net.minecraft.entity.item.EntityTNTPrimed) {
/*  78 */         event.setCancelled(true);
/*  79 */       } else if (armorStand.getBoolValue() && event.getEntity() instanceof net.minecraft.entity.item.EntityArmorStand) {
/*  80 */         event.setCancelled(true);
/*  81 */       } else if (frame.getBoolValue() && event.getEntity() instanceof net.minecraft.entity.item.EntityItemFrame) {
/*  82 */         event.setCancelled(true);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {
/*  89 */     if (banner.getBoolValue()) {
/*  90 */       for (TileEntity e : mc.world.loadedTileEntityList) {
/*  91 */         if (e instanceof net.minecraft.tileentity.TileEntityBanner) {
/*  92 */           mc.world.removeTileEntity(e.getPos());
/*     */         }
/*     */       } 
/*     */     }
/*  96 */     if (cameraSmooth.getBoolValue()) {
/*  97 */       mc.gameSettings.smoothCamera = false;
/*     */     }
/*  99 */     if (rain.getBoolValue() && mc.world.isRaining()) {
/* 100 */       mc.world.setRainStrength(0.0F);
/* 101 */       mc.world.setThunderStrength(0.0F);
/*     */     } 
/* 103 */     if ((blindness.getBoolValue() && mc.player.isPotionActive(MobEffects.BLINDNESS)) || mc.player.isPotionActive(MobEffects.NAUSEA)) {
/* 104 */       mc.player.removePotionEffect(MobEffects.NAUSEA);
/* 105 */       mc.player.removePotionEffect(MobEffects.BLINDNESS);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onWorldLight(EventRenderWorldLight event) {
/* 111 */     if (!getState())
/*     */       return; 
/* 113 */     if (light.getBoolValue() && 
/* 114 */       event.getEnumSkyBlock() == EnumSkyBlock.SKY)
/* 115 */       event.setCancelled(true); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\NoRender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */