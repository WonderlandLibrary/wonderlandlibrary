/*     */ package org.neverhook.client.feature.impl.visual;
/*     */ 
/*     */ import com.mojang.realmsclient.gui.ChatFormatting;
/*     */ import java.awt.Color;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.vecmath.Vector3d;
/*     */ import javax.vecmath.Vector4d;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.util.glu.GLU;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender2D;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*     */ import org.neverhook.client.event.events.impl.render.EventRenderPlayerName;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.feature.impl.player.ClipHelper;
/*     */ import org.neverhook.client.helpers.math.MathematicHelper;
/*     */ import org.neverhook.client.helpers.math.RotationHelper;
/*     */ import org.neverhook.client.helpers.misc.ChatHelper;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ import org.neverhook.client.ui.shader.shaders.EntityGlowShader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityESP
/*     */   extends Feature
/*     */ {
/*     */   public static ListSetting espMode;
/*     */   public static BooleanSetting tags;
/*     */   public static NumberSetting glowAlpha;
/*  60 */   private final int black = Color.BLACK.getRGB();
/*     */   private final ColorSetting colorEsp;
/*     */   private final ColorSetting triangleColor;
/*     */   private final BooleanSetting fullBox;
/*     */   private final BooleanSetting heathPercentage;
/*     */   private final BooleanSetting tagsBackground;
/*     */   private final BooleanSetting border;
/*  67 */   private final BooleanSetting filled = new BooleanSetting("Filled", false, () -> Boolean.valueOf(espMode.currentMode.equals("2D")));
/*  68 */   private final ColorSetting filledColor = new ColorSetting("Filled Color", -2147483648, () -> Boolean.valueOf((this.filled.getBoolValue() && espMode.currentMode.equals("2D"))));
/*  69 */   public BooleanSetting healRect = new BooleanSetting("Health Rect", true, () -> Boolean.valueOf(true));
/*     */   public BooleanSetting triangleESP;
/*  71 */   public BooleanSetting ignoreInvisible = new BooleanSetting("Ignore Invisible", true, () -> Boolean.valueOf(true));
/*  72 */   public ListSetting healcolorMode = new ListSetting("Color Health Mode", "Custom", () -> Boolean.valueOf(this.healRect.getBoolValue()), new String[] { "Astolfo", "Health", "Rainbow", "Client", "Custom" });
/*  73 */   private final ColorSetting healColor = new ColorSetting("Health Border Color", -1, () -> Boolean.valueOf(this.healcolorMode.currentMode.equals("Custom")));
/*     */   public ListSetting csgoMode;
/*  75 */   public ListSetting colorMode = new ListSetting("Color Box Mode", "Custom", () -> Boolean.valueOf((espMode.currentMode.equals("Box") || espMode.currentMode.equals("2D"))), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
/*  76 */   public ListSetting triangleMode = new ListSetting("Triangle Mode", "Custom", () -> Boolean.valueOf(this.triangleESP.getBoolValue()), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
/*     */   public NumberSetting xOffset;
/*     */   public NumberSetting yOffset;
/*     */   public NumberSetting size;
/*     */   
/*     */   public EntityESP() {
/*  82 */     super("EntityESP", "Показывает игроков, ник и их здоровье сквозь стены", Type.Visuals);
/*  83 */     espMode = new ListSetting("ESP Mode", "2D", () -> Boolean.valueOf(true), new String[] { "2D", "Box", "Glow" });
/*  84 */     this.border = new BooleanSetting("Border Rect", true, () -> Boolean.valueOf(espMode.currentMode.equals("2D")));
/*  85 */     this.csgoMode = new ListSetting("Border Mode", "Box", () -> Boolean.valueOf((espMode.currentMode.equals("2D") && this.border.getBoolValue())), new String[] { "Box", "Corner" });
/*  86 */     tags = new BooleanSetting("Render Tags", true, () -> Boolean.valueOf(espMode.currentMode.equals("2D")));
/*  87 */     this.tagsBackground = new BooleanSetting("Tags Background", true, tags::getBoolValue);
/*  88 */     this.colorEsp = new ColorSetting("ESP Color", (new Color(16777215)).getRGB(), () -> Boolean.valueOf((!this.colorMode.currentMode.equals("Client") && (espMode.currentMode.equals("2D") || espMode.currentMode.equals("Box") || espMode.currentMode.equals("Glow")))));
/*  89 */     glowAlpha = new NumberSetting("Glow Alpha", 1.0F, 0.2F, 1.0F, 0.1F, () -> Boolean.valueOf(espMode.currentMode.equals("Glow")));
/*  90 */     this.fullBox = new BooleanSetting("Full Box", false, () -> Boolean.valueOf(espMode.currentMode.equals("Box")));
/*  91 */     this.heathPercentage = new BooleanSetting("Health Percentage", false, () -> Boolean.valueOf(espMode.currentMode.equals("2D")));
/*  92 */     this.triangleESP = new BooleanSetting("Triangle ESP", true, () -> Boolean.valueOf(true));
/*  93 */     this.triangleColor = new ColorSetting("Triangle Color", Color.PINK.getRGB(), () -> Boolean.valueOf((this.triangleESP.getBoolValue() && this.triangleMode.currentMode.equals("Custom"))));
/*  94 */     this.xOffset = new NumberSetting("Triangle XOffset", 10.0F, 0.0F, 50.0F, 5.0F, () -> Boolean.valueOf(this.triangleESP.getBoolValue()));
/*  95 */     this.yOffset = new NumberSetting("Triangle YOffset", 0.0F, 0.0F, 50.0F, 5.0F, () -> Boolean.valueOf(this.triangleESP.getBoolValue()));
/*  96 */     this.size = new NumberSetting("Triangle Size", 5.0F, 0.0F, 20.0F, 1.0F, () -> Boolean.valueOf(this.triangleESP.getBoolValue()));
/*  97 */     addSettings(new Setting[] { (Setting)espMode, (Setting)this.csgoMode, (Setting)this.colorMode, (Setting)this.healcolorMode, (Setting)this.healColor, (Setting)this.colorEsp, (Setting)this.border, (Setting)this.fullBox, (Setting)this.filled, (Setting)this.filledColor, (Setting)this.healRect, (Setting)this.heathPercentage, (Setting)tags, (Setting)this.tagsBackground, (Setting)this.ignoreInvisible, (Setting)this.triangleESP, (Setting)this.triangleMode, (Setting)this.triangleColor, (Setting)this.xOffset, (Setting)this.yOffset, (Setting)this.size });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender3D(EventRender3D event3D) {
/* 102 */     if (!getState()) {
/*     */       return;
/*     */     }
/* 105 */     int color = 0;
/*     */     
/* 107 */     switch (this.colorMode.currentMode) {
/*     */       case "Client":
/* 109 */         color = ClientHelper.getClientColor().getRGB();
/*     */         break;
/*     */       case "Custom":
/* 112 */         color = this.colorEsp.getColorValue();
/*     */         break;
/*     */       case "Astolfo":
/* 115 */         color = PaletteHelper.astolfo(false, 10).getRGB();
/*     */         break;
/*     */       case "Rainbow":
/* 118 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */     } 
/*     */     
/* 122 */     EntityGlowShader framebufferShader = EntityGlowShader.GLOW_SHADER;
/*     */     
/* 124 */     if (espMode.currentMode.equals("Glow")) {
/* 125 */       mc.gameSettings.entityShadows = false;
/* 126 */       framebufferShader.renderShader(event3D.getPartialTicks());
/* 127 */       for (Entity entity : mc.world.loadedEntityList) {
/* 128 */         if (!isValid(entity) || entity instanceof net.minecraft.entity.item.EntityItem) {
/*     */           continue;
/*     */         }
/* 131 */         mc.getRenderManager().renderEntityStatic(entity, event3D.getPartialTicks(), true);
/*     */       } 
/*     */       
/* 134 */       framebufferShader.stopRenderShader(new Color(this.colorEsp.getColorValue()), 3.0F, 1.0F);
/*     */     } 
/*     */     
/* 137 */     if (espMode.currentMode.equals("Box")) {
/* 138 */       GlStateManager.pushMatrix();
/* 139 */       for (Entity entity : mc.world.loadedEntityList) {
/* 140 */         if (entity instanceof EntityPlayer && (
/* 141 */           entity != mc.player || mc.gameSettings.thirdPersonView != 0)) {
/* 142 */           RenderHelper.drawEntityBox(entity, new Color(color), this.fullBox.getBoolValue(), this.fullBox.getBoolValue() ? 0.15F : 0.9F);
/*     */         }
/*     */       } 
/*     */       
/* 146 */       GlStateManager.popMatrix();
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRenderTriangle(EventRender2D eventRender2D) {
/* 152 */     if (this.triangleESP.getBoolValue()) {
/* 153 */       int color = 0;
/* 154 */       switch (this.triangleMode.currentMode) {
/*     */         case "Client":
/* 156 */           color = ClientHelper.getClientColor().getRGB();
/*     */           break;
/*     */         case "Custom":
/* 159 */           color = this.triangleColor.getColorValue();
/*     */           break;
/*     */         case "Astolfo":
/* 162 */           color = PaletteHelper.astolfo(false, 1).getRGB();
/*     */           break;
/*     */         case "Rainbow":
/* 165 */           color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */           break;
/*     */       } 
/*     */       
/* 169 */       ScaledResolution sr = new ScaledResolution(mc);
/* 170 */       float size = 50.0F;
/* 171 */       float xOffset = sr.getScaledWidth() / 2.0F - 24.5F;
/* 172 */       float yOffset = sr.getScaledHeight() / 2.0F - 25.2F;
/* 173 */       for (EntityPlayer entity : mc.world.playerEntities) {
/* 174 */         if (this.ignoreInvisible.getBoolValue() && entity.isInvisible()) {
/*     */           continue;
/*     */         }
/* 177 */         if (entity != null && entity != mc.player) {
/* 178 */           GlStateManager.pushMatrix();
/* 179 */           GlStateManager.disableBlend();
/* 180 */           double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosX;
/* 181 */           double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosZ;
/* 182 */           double cos = Math.cos(mc.player.rotationYaw * 0.017453292519943295D);
/* 183 */           double sin = Math.sin(mc.player.rotationYaw * 0.017453292519943295D);
/* 184 */           double rotY = -(z * cos - x * sin);
/* 185 */           double rotX = -(x * cos + z * sin);
/* 186 */           if (MathHelper.sqrt(rotX * rotX + rotY * rotY) < size) {
/* 187 */             float angle = (float)(Math.atan2(rotY, rotX) * 180.0D / Math.PI);
/* 188 */             double xPos = (size / 2.0F) * Math.cos(Math.toRadians(angle)) + xOffset + (size / 2.0F);
/* 189 */             double y = (size / 2.0F) * Math.sin(Math.toRadians(angle)) + yOffset + (size / 2.0F);
/* 190 */             GlStateManager.translate(xPos, y, 0.0D);
/* 191 */             GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);
/* 192 */             GlStateManager.scale(1.5D, 1.0D, 1.0D);
/* 193 */             RenderHelper.drawTriangle(this.xOffset.getNumberValue(), this.yOffset.getNumberValue(), this.size.getNumberValue() + 0.5F, 90.0F, (new Color(color)).darker().getRGB());
/*     */           } 
/* 195 */           GlStateManager.popMatrix();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender2D(EventRender2D event) {
/* 203 */     String mode = espMode.getOptions();
/* 204 */     setSuffix(mode);
/*     */     
/* 206 */     float partialTicks = mc.timer.renderPartialTicks;
/* 207 */     int scaleFactor = event.getResolution().getScaleFactor();
/* 208 */     double scaling = scaleFactor / Math.pow(scaleFactor, 2.0D);
/* 209 */     GL11.glPushMatrix();
/* 210 */     GlStateManager.scale(scaling, scaling, scaling);
/*     */     
/* 212 */     int color = 0;
/* 213 */     switch (this.colorMode.currentMode) {
/*     */       case "Client":
/* 215 */         color = ClientHelper.getClientColor().getRGB();
/*     */         break;
/*     */       case "Custom":
/* 218 */         color = this.colorEsp.getColorValue();
/*     */         break;
/*     */       case "Astolfo":
/* 221 */         color = PaletteHelper.astolfo(false, 1).getRGB();
/*     */         break;
/*     */       case "Rainbow":
/* 224 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */     } 
/*     */     
/* 228 */     float scale = 1.0F;
/*     */     
/* 230 */     for (Entity entity : mc.world.loadedEntityList) {
/* 231 */       if (this.ignoreInvisible.getBoolValue() && entity.isInvisible()) {
/*     */         continue;
/*     */       }
/* 234 */       if (isValid(entity) && RenderHelper.isInViewFrustum(entity)) {
/* 235 */         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks();
/* 236 */         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks();
/* 237 */         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks();
/* 238 */         double width = entity.width / 1.5D;
/* 239 */         double height = entity.height + ((entity.isSneaking() || (entity == mc.player && mc.player.isSneaking())) ? -0.3D : 0.2D);
/* 240 */         AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
/* 241 */         List<Vector3d> vectors = Arrays.asList(new Vector3d[] { new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ) });
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 246 */         mc.entityRenderer.setupCameraTransform(partialTicks, 0);
/*     */         
/* 248 */         Vector4d position = null;
/* 249 */         for (Vector3d vector : vectors) {
/* 250 */           vector = vectorRender2D(scaleFactor, vector.x - (mc.getRenderManager()).renderPosX, vector.y - (mc.getRenderManager()).renderPosY, vector.z - (mc.getRenderManager()).renderPosZ);
/* 251 */           if (vector != null && vector.z > 0.0D && vector.z < 1.0D) {
/*     */             
/* 253 */             if (position == null) {
/* 254 */               position = new Vector4d(vector.x, vector.y, vector.z, 0.0D);
/*     */             }
/*     */             
/* 257 */             position.x = Math.min(vector.x, position.x);
/* 258 */             position.y = Math.min(vector.y, position.y);
/* 259 */             position.z = Math.max(vector.x, position.z);
/* 260 */             position.w = Math.max(vector.y, position.w);
/*     */           } 
/*     */         } 
/*     */         
/* 264 */         if (position != null) {
/* 265 */           mc.entityRenderer.setupOverlayRendering();
/* 266 */           double posX = position.x;
/* 267 */           double posY = position.y;
/* 268 */           double endPosX = position.z;
/* 269 */           double endPosY = position.w;
/* 270 */           if (this.border.getBoolValue()) {
/* 271 */             if (mode.equalsIgnoreCase("2D") && this.csgoMode.currentMode.equalsIgnoreCase("Box")) {
/*     */ 
/*     */               
/* 274 */               RectHelper.drawRect(posX - 0.5D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 1.0D, this.black);
/*     */ 
/*     */               
/* 277 */               RectHelper.drawRect(posX - 0.5D, endPosY - 0.5D - 1.0D, endPosX + 0.5D, endPosY + 0.5D, this.black);
/*     */ 
/*     */               
/* 280 */               RectHelper.drawRect(posX - 1.5D, posY, posX + 0.5D, endPosY + 0.5D, this.black);
/*     */ 
/*     */               
/* 283 */               RectHelper.drawRect(endPosX - 0.5D - 1.0D, posY, endPosX + 0.5D, endPosY + 0.5D, this.black);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 288 */               RectHelper.drawRect(posX - 1.0D, posY, posX + 0.5D - 0.5D, endPosY, color);
/*     */ 
/*     */               
/* 291 */               RectHelper.drawRect(posX, endPosY - 1.0D, endPosX, endPosY, color);
/*     */ 
/*     */               
/* 294 */               RectHelper.drawRect(posX - 1.0D, posY, endPosX, posY + 1.0D, color);
/*     */ 
/*     */               
/* 297 */               RectHelper.drawRect(endPosX - 1.0D, posY, endPosX, endPosY, color);
/* 298 */             } else if (mode.equalsIgnoreCase("2D") && this.csgoMode.currentMode.equalsIgnoreCase("Corner")) {
/*     */ 
/*     */               
/* 301 */               RectHelper.drawRect(posX + 1.0D, posY, posX - 1.0D, posY + (endPosY - posY) / 4.0D + 0.5D, this.black);
/*     */ 
/*     */               
/* 304 */               RectHelper.drawRect(posX - 1.0D, endPosY, posX + 1.0D, endPosY - (endPosY - posY) / 4.0D - 0.5D, this.black);
/*     */ 
/*     */               
/* 307 */               RectHelper.drawRect(posX - 1.0D, posY - 0.5D, posX + (endPosX - posX) / 3.0D, posY + 1.0D, this.black);
/*     */ 
/*     */               
/* 310 */               RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0D - 0.0D, posY - 0.5D, endPosX, posY + 1.5D, this.black);
/*     */ 
/*     */               
/* 313 */               RectHelper.drawRect(endPosX - 1.5D, posY, endPosX + 0.5D, posY + (endPosY - posY) / 4.0D + 0.5D, this.black);
/*     */ 
/*     */               
/* 316 */               RectHelper.drawRect(endPosX - 1.5D, endPosY, endPosX + 0.5D, endPosY - (endPosY - posY) / 4.0D - 0.5D, this.black);
/*     */ 
/*     */               
/* 319 */               RectHelper.drawRect(posX - 1.0D, endPosY - 1.5D, posX + (endPosX - posX) / 3.0D + 0.5D, endPosY + 0.5D, this.black);
/*     */ 
/*     */               
/* 322 */               RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0D - 0.5D, endPosY - 1.5D, endPosX + 0.5D, endPosY + 0.5D, this.black);
/*     */               
/* 324 */               RectHelper.drawRect(posX + 0.5D, posY, posX - 0.5D, posY + (endPosY - posY) / 4.0D, color);
/*     */               
/* 326 */               RectHelper.drawRect(posX + 0.5D, endPosY, posX - 0.5D, endPosY - (endPosY - posY) / 4.0D, color);
/*     */               
/* 328 */               RectHelper.drawRect(posX - 0.5D, posY, posX + (endPosX - posX) / 3.0D, posY + 1.0D, color);
/* 329 */               RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0D + 0.5D, posY, endPosX, posY + 1.0D, color);
/* 330 */               RectHelper.drawRect(endPosX - 1.0D, posY, endPosX, posY + (endPosY - posY) / 4.0D + 0.5D, color);
/* 331 */               RectHelper.drawRect(endPosX - 1.0D, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0D, color);
/* 332 */               RectHelper.drawRect(posX, endPosY - 1.0D, posX + (endPosX - posX) / 3.0D, endPosY, color);
/* 333 */               RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0D, endPosY - 1.0D, endPosX - 0.5D, endPosY, color);
/*     */             } 
/*     */           }
/* 336 */           boolean living = entity instanceof EntityLivingBase;
/* 337 */           EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
/* 338 */           float targetHP = entityLivingBase.getHealth();
/* 339 */           targetHP = MathHelper.clamp(targetHP, 0.0F, 24.0F);
/* 340 */           float maxHealth = entityLivingBase.getMaxHealth();
/* 341 */           double hpPercentage = (targetHP / maxHealth);
/* 342 */           double hpHeight2 = (endPosY - posY) * hpPercentage;
/*     */ 
/*     */ 
/*     */           
/* 346 */           if (NeverHook.instance.featureManager.getFeatureByClass(ClipHelper.class).getState()) {
/* 347 */             BlockPos playerPosY = new BlockPos(0.0D, mc.player.posY, 0.0D);
/* 348 */             BlockPos entityPosY = new BlockPos(0.0D, entity.posY, 0.0D);
/* 349 */             if (RotationHelper.isLookingAtEntity(mc.player.rotationYaw, mc.player.rotationPitch, 0.15F, 0.15F, 0.15F, entity, ClipHelper.maxDistance.getNumberValue())) {
/* 350 */               BlockPos distanceToY = new BlockPos(0.0D, (int)mc.player.posY - entity.posY, 0.0D);
/* 351 */               int findToClip = (int)entity.posY;
/* 352 */               if (!playerPosY.equals(entityPosY) && mc.gameSettings.thirdPersonView == 0) {
/* 353 */                 if (RenderHelper.isInViewFrustum(entity)) {
/* 354 */                   float diff = (float)(endPosX - posX) / 2.0F;
/* 355 */                   float textWidth = mc.fontRendererObj.getStringWidth("Target §7" + distanceToY.getY() + "m") * scale;
/* 356 */                   float tagX = (float)((posX + diff - (textWidth / 2.0F)) * scale);
/* 357 */                   mc.fontRendererObj.drawStringWithShadow("Target §7" + distanceToY.getY() + "m", tagX, (float)endPosY, -1);
/*     */                 } 
/* 359 */                 if (Mouse.isButtonDown(2)) {
/* 360 */                   mc.player.setPositionAndUpdate(mc.player.posX, findToClip, mc.player.posZ);
/* 361 */                   ChatHelper.addChatMessage("Clip to entity " + ChatFormatting.RED + entity.getName() + ChatFormatting.WHITE + " on Y " + ChatFormatting.RED + findToClip);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/* 366 */           if (targetHP > 0.0F) {
/* 367 */             String string2 = "" + MathematicHelper.round(entityLivingBase.getHealth() / entityLivingBase.getMaxHealth() * 100.0F, 1) + "%";
/* 368 */             if (living && this.heathPercentage.getBoolValue() && !espMode.currentMode.equals("Box") && 
/* 369 */               this.heathPercentage.getBoolValue()) {
/* 370 */               GlStateManager.pushMatrix();
/* 371 */               mc.fontRendererObj.drawStringWithShadow(string2, (float)posX - 36.0F, (float)((float)endPosY - hpHeight2), -1);
/* 372 */               GlStateManager.popMatrix();
/*     */             } 
/*     */ 
/*     */             
/* 376 */             if (living && this.healRect.getBoolValue() && !espMode.currentMode.equals("Box")) {
/* 377 */               int colorHeal = 0;
/*     */               
/* 379 */               switch (this.healcolorMode.currentMode) {
/*     */                 case "Client":
/* 381 */                   colorHeal = ClientHelper.getClientColor().getRGB();
/*     */                   break;
/*     */                 case "Custom":
/* 384 */                   colorHeal = this.healColor.getColorValue();
/*     */                   break;
/*     */                 case "Astolfo":
/* 387 */                   colorHeal = PaletteHelper.astolfo(false, (int)entity.height).getRGB();
/*     */                   break;
/*     */                 case "Rainbow":
/* 390 */                   colorHeal = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */                   break;
/*     */                 case "Health":
/* 393 */                   colorHeal = PaletteHelper.getHealthColor(((EntityLivingBase)entity).getHealth(), ((EntityLivingBase)entity).getMaxHealth());
/*     */                   break;
/*     */               } 
/*     */               
/* 397 */               RectHelper.drawRect(posX - 5.0D, posY - 0.5D, posX - 2.5D, endPosY + 0.5D, (new Color(0, 0, 0, 125)).getRGB());
/* 398 */               RectHelper.drawRect(posX - 4.5D, endPosY, posX - 3.0D, endPosY - hpHeight2, colorHeal);
/*     */             } 
/*     */             
/* 401 */             if (this.filled.getBoolValue()) {
/* 402 */               RectHelper.drawRect(posX, posY, endPosX, endPosY, this.filledColor.getColorValue());
/*     */             }
/*     */             
/* 405 */             if (living && tags.getBoolValue() && !NeverHook.instance.featureManager.getFeatureByClass(NameTags.class).getState()) {
/* 406 */               float scaledHeight = 10.0F;
/* 407 */               float diff = (float)(endPosX - posX) / 2.0F;
/* 408 */               String tag = entity.getDisplayName().getFormattedText();
/* 409 */               int healthXyi = (int)((EntityLivingBase)entity).getHealth();
/* 410 */               if (healthXyi <= ((EntityLivingBase)entity).getMaxHealth() * 0.25F) {
/* 411 */                 tag = tag + "§4";
/* 412 */               } else if (healthXyi <= ((EntityLivingBase)entity).getMaxHealth() * 0.5F) {
/* 413 */                 tag = tag + "§6";
/* 414 */               } else if (healthXyi <= ((EntityLivingBase)entity).getMaxHealth() * 0.75F) {
/* 415 */                 tag = tag + "§e";
/* 416 */               } else if (healthXyi <= ((EntityLivingBase)entity).getMaxHealth()) {
/* 417 */                 tag = tag + "§2";
/*     */               } 
/* 419 */               tag = tag + " " + MathematicHelper.round(((EntityLivingBase)entity).getHealth(), 0) + " ";
/*     */               
/* 421 */               float textWidth = mc.fontRendererObj.getStringWidth(tag) * scale;
/* 422 */               float tagX = (float)((posX + diff - (textWidth / 2.0F)) * scale);
/* 423 */               float tagY = (float)(posY * scale) - scaledHeight;
/* 424 */               GlStateManager.pushMatrix();
/* 425 */               GlStateManager.scale(scale, scale, scale);
/* 426 */               GlStateManager.translate(0.0F, -4.0F, 0.0F);
/* 427 */               if (this.tagsBackground.getBoolValue()) {
/* 428 */                 RectHelper.drawRect((tagX - 2.0F), (tagY - 2.0F), (tagX + textWidth * scale + 2.0F), (tagY + 9.0F), (new Color(0, 0, 0, 125)).getRGB());
/*     */               }
/*     */               
/* 431 */               mc.fontRendererObj.drawStringWithShadow(tag, tagX, tagY, Color.WHITE.getRGB());
/* 432 */               GlStateManager.popMatrix();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 439 */     GL11.glEnable(2929);
/* 440 */     GlStateManager.enableBlend();
/* 441 */     GL11.glPopMatrix();
/* 442 */     mc.entityRenderer.setupOverlayRendering();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRenderName(EventRenderPlayerName event) {
/* 447 */     if (!getState()) {
/*     */       return;
/*     */     }
/* 450 */     event.setCancelled(tags.getBoolValue());
/*     */   }
/*     */   
/*     */   private boolean isValid(Entity entity) {
/* 454 */     if (mc.gameSettings.thirdPersonView == 0 && entity == mc.player)
/* 455 */       return false; 
/* 456 */     if (entity.isDead)
/* 457 */       return false; 
/* 458 */     if (entity instanceof net.minecraft.entity.passive.EntityAnimal)
/* 459 */       return false; 
/* 460 */     if (entity instanceof EntityPlayer)
/* 461 */       return true; 
/* 462 */     if (entity instanceof net.minecraft.entity.item.EntityArmorStand)
/* 463 */       return false; 
/* 464 */     if (entity instanceof net.minecraft.entity.passive.IAnimals)
/* 465 */       return false; 
/* 466 */     if (entity instanceof net.minecraft.entity.item.EntityItemFrame)
/* 467 */       return false; 
/* 468 */     if (entity instanceof net.minecraft.entity.projectile.EntityArrow)
/* 469 */       return false; 
/* 470 */     if (entity instanceof net.minecraft.entity.item.EntityMinecart)
/* 471 */       return false; 
/* 472 */     if (entity instanceof net.minecraft.entity.item.EntityBoat)
/* 473 */       return false; 
/* 474 */     if (entity instanceof net.minecraft.entity.projectile.EntityDragonFireball)
/* 475 */       return false; 
/* 476 */     if (entity instanceof net.minecraft.entity.item.EntityXPOrb)
/* 477 */       return false; 
/* 478 */     if (entity instanceof net.minecraft.entity.item.EntityTNTPrimed)
/* 479 */       return false; 
/* 480 */     if (entity instanceof net.minecraft.entity.item.EntityExpBottle)
/* 481 */       return false; 
/* 482 */     if (entity instanceof net.minecraft.entity.effect.EntityLightningBolt)
/* 483 */       return false; 
/* 484 */     if (entity instanceof net.minecraft.entity.projectile.EntityPotion)
/* 485 */       return false; 
/* 486 */     if (entity instanceof Entity)
/* 487 */       return false; 
/* 488 */     if (entity instanceof net.minecraft.entity.monster.EntityMob || entity instanceof net.minecraft.entity.monster.EntitySlime || entity instanceof net.minecraft.entity.boss.EntityDragon || entity instanceof net.minecraft.entity.monster.EntityGolem)
/*     */     {
/* 490 */       return false; } 
/* 491 */     return (entity != mc.player);
/*     */   }
/*     */   
/*     */   private Vector3d vectorRender2D(int scaleFactor, double x, double y, double z) {
/* 495 */     float xPos = (float)x;
/* 496 */     float yPos = (float)y;
/* 497 */     float zPos = (float)z;
/* 498 */     IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
/* 499 */     FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
/* 500 */     FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
/* 501 */     FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
/* 502 */     GL11.glGetFloat(2982, modelview);
/* 503 */     GL11.glGetFloat(2983, projection);
/* 504 */     GL11.glGetInteger(2978, viewport);
/* 505 */     if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector))
/* 506 */       return new Vector3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor), vector.get(2)); 
/* 507 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\EntityESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */