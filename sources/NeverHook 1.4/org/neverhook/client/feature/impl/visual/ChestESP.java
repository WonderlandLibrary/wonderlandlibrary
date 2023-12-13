/*     */ package org.neverhook.client.feature.impl.visual;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import javax.vecmath.Vector3d;
/*     */ import javax.vecmath.Vector4d;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
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
/*     */ public class ChestESP
/*     */   extends Feature
/*     */ {
/*  33 */   public static ListSetting chestEspMode = new ListSetting("ChestEsp Mode", "2D", () -> Boolean.valueOf(true), new String[] { "2D", "3D", "Chams" });
/*  34 */   public static ColorSetting chamsColor = new ColorSetting("Chams Color", Color.PINK.getRGB(), () -> Boolean.valueOf(chestEspMode.currentMode.equals("Chams")));
/*  35 */   private final int black = Color.BLACK.getRGB();
/*     */   private final BooleanSetting fullBox;
/*     */   private final ColorSetting colorEsp;
/*  38 */   public ListSetting colorMode = new ListSetting("Color Box Mode", "Custom", () -> Boolean.valueOf(true), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
/*     */   
/*     */   public ChestESP() {
/*  41 */     super("ChestESP", "Показывает сундуки", Type.Visuals);
/*  42 */     this.colorEsp = new ColorSetting("ChestEsp Color", (new Color(16777215)).getRGB(), () -> Boolean.valueOf((this.colorMode.currentMode.equals("Custom") && !chestEspMode.currentMode.equals("Chams"))));
/*  43 */     this.fullBox = new BooleanSetting("Full Box", false, () -> Boolean.valueOf(chestEspMode.currentMode.equals("3D")));
/*  44 */     addSettings(new Setting[] { (Setting)chestEspMode, (Setting)this.colorMode, (Setting)this.colorEsp, (Setting)chamsColor, (Setting)this.fullBox });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender3D(EventRender3D event) {
/*  49 */     for (TileEntity entity : mc.world.loadedTileEntityList) {
/*  50 */       if (entity instanceof net.minecraft.tileentity.TileEntityChest) {
/*  51 */         BlockPos pos = entity.getPos();
/*  52 */         int color = 0;
/*  53 */         switch (this.colorMode.currentMode) {
/*     */           case "Client":
/*  55 */             color = ClientHelper.getClientColor().getRGB();
/*     */             break;
/*     */           case "Custom":
/*  58 */             color = this.colorEsp.getColorValue();
/*     */             break;
/*     */           case "Astolfo":
/*  61 */             color = PaletteHelper.astolfo(false, entity.getPos().getY()).getRGB();
/*     */             break;
/*     */           case "Rainbow":
/*  64 */             color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */             break;
/*     */         } 
/*  67 */         if (chestEspMode.currentMode.equals("3D")) {
/*  68 */           GlStateManager.pushMatrix();
/*  69 */           RenderHelper.blockEsp(pos, new Color(color), this.fullBox.getBoolValue());
/*  70 */           GlStateManager.popMatrix();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender2D(EventRender2D event) {
/*  78 */     String mode = chestEspMode.getOptions();
/*  79 */     setSuffix(mode);
/*  80 */     float partialTicks = mc.timer.renderPartialTicks;
/*  81 */     int scaleFactor = event.getResolution().getScaleFactor();
/*  82 */     double scaling = scaleFactor / Math.pow(scaleFactor, 2.0D);
/*  83 */     GlStateManager.scale(scaling, scaling, scaling);
/*  84 */     Color customColor = new Color(this.colorEsp.getColorValue());
/*  85 */     Color c = new Color(customColor.getRed(), customColor.getGreen(), customColor.getBlue());
/*  86 */     int color = 0;
/*  87 */     switch (this.colorMode.currentMode) {
/*     */       case "Client":
/*  89 */         color = ClientHelper.getClientColor().getRGB();
/*     */         break;
/*     */       case "Custom":
/*  92 */         color = c.getRGB();
/*     */         break;
/*     */       case "Astolfo":
/*  95 */         color = PaletteHelper.astolfo(false, 1).getRGB();
/*     */         break;
/*     */       case "Rainbow":
/*  98 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */     } 
/*     */     
/* 102 */     for (TileEntity entity : mc.world.loadedTileEntityList) {
/* 103 */       if (isValid(entity)) {
/* 104 */         BlockPos pos = entity.getPos();
/* 105 */         AxisAlignedBB axisAlignedBB = new AxisAlignedBB(pos);
/* 106 */         Vector3d[] vectors = { new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ) };
/* 107 */         mc.entityRenderer.setupCameraTransform(partialTicks, 0);
/* 108 */         Vector4d position = null;
/* 109 */         for (Vector3d vector : vectors) {
/* 110 */           vector = project2D(scaleFactor, vector.x - (mc.getRenderManager()).renderPosX, vector.y - (mc.getRenderManager()).renderPosY, vector.z - (mc.getRenderManager()).renderPosZ);
/* 111 */           if (vector != null && vector.z > 0.0D && vector.z < 1.0D) {
/* 112 */             if (position == null) {
/* 113 */               position = new Vector4d(vector.x, vector.y, vector.z, 0.0D);
/*     */             }
/*     */             
/* 116 */             position.x = Math.min(vector.x, position.x);
/* 117 */             position.y = Math.min(vector.y, position.y);
/* 118 */             position.z = Math.max(vector.x, position.z);
/* 119 */             position.w = Math.max(vector.y, position.w);
/*     */           } 
/*     */         } 
/*     */         
/* 123 */         if (position != null) {
/* 124 */           mc.entityRenderer.setupOverlayRendering();
/* 125 */           double posX = position.x;
/* 126 */           double posY = position.y;
/* 127 */           double endPosX = position.z;
/* 128 */           double endPosY = position.w;
/*     */           
/* 130 */           if (mode.equalsIgnoreCase("2D")) {
/* 131 */             RectHelper.drawRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, this.black);
/* 132 */             RectHelper.drawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, this.black);
/* 133 */             RectHelper.drawRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, this.black);
/* 134 */             RectHelper.drawRect(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, this.black);
/* 135 */             RectHelper.drawRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, color);
/* 136 */             RectHelper.drawRect(posX, endPosY - 0.5D, endPosX, endPosY, color);
/* 137 */             RectHelper.drawRect(posX - 0.5D, posY, endPosX, posY + 0.5D, color);
/* 138 */             RectHelper.drawRect(endPosX - 0.5D, posY, endPosX, endPosY, color);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 143 */     GL11.glEnable(2929);
/* 144 */     mc.entityRenderer.setupOverlayRendering();
/*     */   }
/*     */   
/*     */   private Vector3d project2D(int scaleFactor, double x, double y, double z) {
/* 148 */     IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
/* 149 */     FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
/* 150 */     FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
/* 151 */     FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
/* 152 */     GL11.glGetFloat(2982, modelview);
/* 153 */     GL11.glGetFloat(2983, projection);
/* 154 */     GL11.glGetInteger(2978, viewport);
/* 155 */     if (GLU.gluProject((float)x, (float)y, (float)z, modelview, projection, viewport, vector)) {
/* 156 */       return new Vector3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor), vector.get(2));
/*     */     }
/* 158 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isValid(TileEntity entity) {
/* 162 */     return entity instanceof net.minecraft.tileentity.TileEntityChest;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\ChestESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */