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
/*     */ import net.minecraft.entity.item.EntityItem;
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
/*     */ public class ItemESP
/*     */   extends Feature {
/*  32 */   private final int black = Color.BLACK.getRGB();
/*     */   private final BooleanSetting fullBox;
/*     */   private final ColorSetting colorEsp;
/*  35 */   public ListSetting colorMode = new ListSetting("Color Box Mode", "Custom", () -> Boolean.valueOf(true), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
/*  36 */   public ListSetting itemEspMode = new ListSetting("ItemESP Mode", "2D", () -> Boolean.valueOf(true), new String[] { "2D", "3D" });
/*  37 */   public BooleanSetting entityName = new BooleanSetting("Entity Name", true, () -> Boolean.valueOf(true));
/*     */   
/*     */   public ItemESP() {
/*  40 */     super("ItemESP", "Отображение айтемов", Type.Visuals);
/*  41 */     this.colorEsp = new ColorSetting("ESP Color", (new Color(16777215)).getRGB(), () -> Boolean.valueOf(this.colorMode.currentMode.equals("Custom")));
/*  42 */     this.fullBox = new BooleanSetting("Full Box", false, () -> Boolean.valueOf(this.itemEspMode.currentMode.equals("3D")));
/*  43 */     addSettings(new Setting[] { (Setting)this.itemEspMode, (Setting)this.colorMode, (Setting)this.colorEsp, (Setting)this.entityName, (Setting)this.fullBox });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender3D(EventRender3D event) {
/*  48 */     for (Entity item : mc.world.loadedEntityList) {
/*  49 */       if (item instanceof EntityItem) {
/*  50 */         int color = 0;
/*  51 */         switch (this.colorMode.currentMode) {
/*     */           case "Client":
/*  53 */             color = ClientHelper.getClientColor().getRGB();
/*     */             break;
/*     */           case "Custom":
/*  56 */             color = this.colorEsp.getColorValue();
/*     */             break;
/*     */           case "Astolfo":
/*  59 */             color = PaletteHelper.astolfo(false, (int)item.height).getRGB();
/*     */             break;
/*     */           case "Rainbow":
/*  62 */             color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */             break;
/*     */         } 
/*  65 */         if (this.itemEspMode.currentMode.equals("3D")) {
/*  66 */           GlStateManager.pushMatrix();
/*  67 */           RenderHelper.drawEntityBox(item, new Color(color), this.fullBox.getBoolValue(), this.fullBox.getBoolValue() ? 0.15F : 0.9F);
/*  68 */           GlStateManager.popMatrix();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender2D(EventRender2D event) {
/*  76 */     String mode = this.itemEspMode.getOptions();
/*  77 */     setSuffix(mode);
/*  78 */     float partialTicks = mc.timer.renderPartialTicks;
/*  79 */     int scaleFactor = event.getResolution().getScaleFactor();
/*  80 */     double scaling = scaleFactor / Math.pow(scaleFactor, 2.0D);
/*  81 */     GlStateManager.scale(scaling, scaling, scaling);
/*  82 */     Color customColor = new Color(this.colorEsp.getColorValue());
/*  83 */     Color c = new Color(customColor.getRed(), customColor.getGreen(), customColor.getBlue());
/*  84 */     int color = 0;
/*  85 */     switch (this.colorMode.currentMode) {
/*     */       case "Client":
/*  87 */         color = ClientHelper.getClientColor().getRGB();
/*     */         break;
/*     */       case "Custom":
/*  90 */         color = c.getRGB();
/*     */         break;
/*     */       case "Astolfo":
/*  93 */         color = PaletteHelper.astolfo(false, 1).getRGB();
/*     */         break;
/*     */       case "Rainbow":
/*  96 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */     } 
/*     */     
/* 100 */     float scale = 1.0F;
/*     */     
/* 102 */     for (Entity entity : mc.world.loadedEntityList) {
/* 103 */       if (isValid(entity) && RenderHelper.isInViewFrustum(entity)) {
/* 104 */         EntityItem entityItem = (EntityItem)entity;
/* 105 */         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks();
/* 106 */         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks();
/* 107 */         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks();
/* 108 */         AxisAlignedBB axisAlignedBB2 = entity.getEntityBoundingBox();
/* 109 */         AxisAlignedBB axisAlignedBB = new AxisAlignedBB(axisAlignedBB2.minX - entity.posX + x - 0.05D, axisAlignedBB2.minY - entity.posY + y, axisAlignedBB2.minZ - entity.posZ + z - 0.05D, axisAlignedBB2.maxX - entity.posX + x + 0.05D, axisAlignedBB2.maxY - entity.posY + y + 0.15D, axisAlignedBB2.maxZ - entity.posZ + z + 0.05D);
/* 110 */         Vector3d[] vectors = { new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ) };
/* 111 */         mc.entityRenderer.setupCameraTransform(partialTicks, 0);
/*     */         
/* 113 */         Vector4d position = null;
/* 114 */         for (Vector3d vector : vectors) {
/* 115 */           vector = project2D(scaleFactor, vector.x - (mc.getRenderManager()).renderPosX, vector.y - (mc.getRenderManager()).renderPosY, vector.z - (mc.getRenderManager()).renderPosZ);
/* 116 */           if (vector != null && vector.z > 0.0D && vector.z < 1.0D) {
/* 117 */             if (position == null)
/* 118 */               position = new Vector4d(vector.x, vector.y, vector.z, 0.0D); 
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
/*     */           
/* 133 */           if (mode.equalsIgnoreCase("2D")) {
/* 134 */             RectHelper.drawRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, this.black);
/* 135 */             RectHelper.drawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, this.black);
/* 136 */             RectHelper.drawRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, this.black);
/* 137 */             RectHelper.drawRect(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, this.black);
/* 138 */             RectHelper.drawRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, color);
/* 139 */             RectHelper.drawRect(posX, endPosY - 0.5D, endPosX, endPosY, color);
/* 140 */             RectHelper.drawRect(posX - 0.5D, posY, endPosX, posY + 0.5D, color);
/* 141 */             RectHelper.drawRect(endPosX - 0.5D, posY, endPosX, endPosY, color);
/*     */           } 
/*     */           
/* 144 */           float diff = (float)(endPosX - posX) / 2.0F;
/* 145 */           float textWidth = mc.fontRendererObj.getStringWidth(entityItem.getEntityItem().getDisplayName()) * scale;
/* 146 */           float tagX = (float)((posX + diff - (textWidth / 2.0F)) * scale);
/* 147 */           if (this.entityName.getBoolValue()) {
/* 148 */             mc.fontRendererObj.drawStringWithOutline(entityItem.getEntityItem().getDisplayName(), tagX, (float)posY - 10.0F, -1);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 153 */     GL11.glEnable(2929);
/* 154 */     mc.entityRenderer.setupOverlayRendering();
/*     */   }
/*     */   
/*     */   private Vector3d project2D(int scaleFactor, double x, double y, double z) {
/* 158 */     float xPos = (float)x;
/* 159 */     float yPos = (float)y;
/* 160 */     float zPos = (float)z;
/* 161 */     IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
/* 162 */     FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
/* 163 */     FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
/* 164 */     FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
/* 165 */     GL11.glGetFloat(2982, modelview);
/* 166 */     GL11.glGetFloat(2983, projection);
/* 167 */     GL11.glGetInteger(2978, viewport);
/* 168 */     if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector))
/* 169 */       return new Vector3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor), vector.get(2)); 
/* 170 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isValid(Entity entity) {
/* 174 */     return entity instanceof EntityItem;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\ItemESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */