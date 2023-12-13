/*     */ package org.neverhook.client.feature.impl.visual;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import javax.vecmath.Vector3d;
/*     */ import javax.vecmath.Vector4d;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.util.glu.GLU;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender2D;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MobESP
/*     */   extends Feature
/*     */ {
/*  35 */   private final int black = Color.BLACK.getRGB();
/*     */   private final BooleanSetting fullBox;
/*     */   private final ColorSetting colorEsp;
/*  38 */   public ListSetting colorMode = new ListSetting("Color Box Mode", "Custom", () -> Boolean.valueOf(true), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
/*  39 */   public ListSetting itemEspMode = new ListSetting("ItemESP Mode", "2D", () -> Boolean.valueOf(true), new String[] { "2D", "3D" });
/*     */   
/*     */   public MobESP() {
/*  42 */     super("MobESP", "Показывает мобов", Type.Visuals);
/*  43 */     this.colorEsp = new ColorSetting("ESP Color", (new Color(16777215)).getRGB(), () -> Boolean.valueOf(this.colorMode.currentMode.equals("Custom")));
/*  44 */     this.fullBox = new BooleanSetting("Full Box", false, () -> Boolean.valueOf(this.itemEspMode.currentMode.equals("3D")));
/*  45 */     addSettings(new Setting[] { (Setting)this.itemEspMode, (Setting)this.colorMode, (Setting)this.colorEsp, (Setting)this.fullBox });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender3D(EventRender3D event) {
/*  50 */     for (Entity entity : mc.world.loadedEntityList) {
/*  51 */       if (entity instanceof net.minecraft.entity.passive.EntityVillager || entity instanceof net.minecraft.entity.monster.EntityZombie || entity instanceof net.minecraft.entity.passive.EntityAnimal) {
/*  52 */         int color = 0;
/*  53 */         switch (this.colorMode.currentMode) {
/*     */           case "Client":
/*  55 */             color = ClientHelper.getClientColor().getRGB();
/*     */             break;
/*     */           case "Custom":
/*  58 */             color = this.colorEsp.getColorValue();
/*     */             break;
/*     */           case "Astolfo":
/*  61 */             color = PaletteHelper.astolfo(false, (int)entity.height).getRGB();
/*     */             break;
/*     */           case "Rainbow":
/*  64 */             color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */             break;
/*     */         } 
/*  67 */         if (this.itemEspMode.currentMode.equals("3D")) {
/*  68 */           GlStateManager.pushMatrix();
/*  69 */           RenderHelper.drawEntityBox(entity, new Color(color), this.fullBox.getBoolValue(), this.fullBox.getBoolValue() ? 0.15F : 0.9F);
/*  70 */           GlStateManager.popMatrix();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender2D(EventRender2D event) {
/*  78 */     String mode = this.itemEspMode.getOptions();
/*  79 */     setSuffix(mode);
/*  80 */     float partialTicks = mc.timer.renderPartialTicks;
/*  81 */     GlStateManager.pushMatrix();
/*  82 */     int scaleFactor = event.getResolution().getScaleFactor();
/*  83 */     double scaling = scaleFactor / Math.pow(scaleFactor, 2.0D);
/*  84 */     GlStateManager.scale(scaling, scaling, scaling);
/*  85 */     Color onecolor = new Color(this.colorEsp.getColorValue());
/*  86 */     Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
/*  87 */     int color = 0;
/*  88 */     switch (this.colorMode.currentMode) {
/*     */       case "Client":
/*  90 */         color = ClientHelper.getClientColor().getRGB();
/*     */         break;
/*     */       case "Custom":
/*  93 */         color = c.getRGB();
/*     */         break;
/*     */       case "Astolfo":
/*  96 */         color = PaletteHelper.astolfo(false, 1).getRGB();
/*     */         break;
/*     */       case "Rainbow":
/*  99 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */     } 
/*     */     
/* 103 */     for (Entity entity : mc.world.loadedEntityList) {
/* 104 */       if (isValid(entity) && RenderHelper.isInViewFrustum(entity)) {
/* 105 */         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks();
/* 106 */         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks();
/* 107 */         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks();
/* 108 */         AxisAlignedBB axisAlignedBB2 = entity.getEntityBoundingBox();
/* 109 */         AxisAlignedBB axisAlignedBB = new AxisAlignedBB(axisAlignedBB2.minX - entity.posX + x - 0.05D, axisAlignedBB2.minY - entity.posY + y, axisAlignedBB2.minZ - entity.posZ + z - 0.05D, axisAlignedBB2.maxX - entity.posX + x + 0.05D, axisAlignedBB2.maxY - entity.posY + y + 0.15D, axisAlignedBB2.maxZ - entity.posZ + z + 0.05D);
/* 110 */         Vector3d[] vectors = { new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ) };
/* 111 */         mc.entityRenderer.setupCameraTransform(partialTicks, 0);
/* 112 */         Vector4d position = null;
/* 113 */         for (Vector3d vector : vectors) {
/* 114 */           vector = project2D(scaleFactor, vector.x - (mc.getRenderManager()).renderPosX, vector.y - (mc.getRenderManager()).renderPosY, vector.z - (mc.getRenderManager()).renderPosZ);
/* 115 */           if (vector != null && vector.z > 0.0D && vector.z < 1.0D) {
/* 116 */             if (position == null) {
/* 117 */               position = new Vector4d(vector.x, vector.y, vector.z, 0.0D);
/*     */             }
/* 119 */             position.x = Math.min(vector.x, position.x);
/* 120 */             position.y = Math.min(vector.y, position.y);
/* 121 */             position.z = Math.max(vector.x, position.z);
/* 122 */             position.w = Math.max(vector.y, position.w);
/*     */           } 
/*     */         } 
/*     */         
/* 126 */         if (position != null) {
/* 127 */           mc.entityRenderer.setupOverlayRendering();
/* 128 */           double posX = position.x;
/* 129 */           double posY = position.y;
/* 130 */           double endPosX = position.z;
/* 131 */           double endPosY = position.w;
/* 132 */           if (mode.equalsIgnoreCase("2D")) {
/* 133 */             RectHelper.drawRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, this.black);
/* 134 */             RectHelper.drawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, this.black);
/* 135 */             RectHelper.drawRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, this.black);
/* 136 */             RectHelper.drawRect(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, this.black);
/* 137 */             RectHelper.drawRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, color);
/* 138 */             RectHelper.drawRect(posX, endPosY - 0.5D, endPosX, endPosY, color);
/* 139 */             RectHelper.drawRect(posX - 0.5D, posY, endPosX, posY + 0.5D, color);
/* 140 */             RectHelper.drawRect(endPosX - 0.5D, posY, endPosX, endPosY, color);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 145 */     GlStateManager.popMatrix();
/* 146 */     mc.entityRenderer.setupOverlayRendering();
/*     */   }
/*     */   
/*     */   private Vector3d project2D(int scaleFactor, double x, double y, double z) {
/* 150 */     float xPos = (float)x;
/* 151 */     float yPos = (float)y;
/* 152 */     float zPos = (float)z;
/* 153 */     IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
/* 154 */     FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
/* 155 */     FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
/* 156 */     FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
/* 157 */     GL11.glGetFloat(2982, modelview);
/* 158 */     GL11.glGetFloat(2983, projection);
/* 159 */     GL11.glGetInteger(2978, viewport);
/* 160 */     if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector))
/* 161 */       return new Vector3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor), vector.get(2)); 
/* 162 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isValid(Entity entity) {
/* 166 */     return (entity instanceof net.minecraft.entity.passive.EntityVillager || entity instanceof net.minecraft.entity.monster.EntityWitch || entity instanceof net.minecraft.entity.passive.EntityBat || entity instanceof net.minecraft.entity.monster.EntitySpider || entity instanceof net.minecraft.entity.monster.EntitySkeleton || entity instanceof net.minecraft.entity.monster.EntityCreeper || entity instanceof net.minecraft.entity.monster.EntityShulker || entity instanceof net.minecraft.entity.monster.EntityGhast || entity instanceof net.minecraft.entity.monster.EntityZombie || entity instanceof net.minecraft.entity.passive.EntityAnimal);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\MobESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */