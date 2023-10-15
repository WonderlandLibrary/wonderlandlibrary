/*     */ package org.neverhook.client.helpers.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ 
/*     */ 
/*     */ public class RenderHelper
/*     */   implements Helper
/*     */ {
/*  25 */   public static Frustum frustum = new Frustum();
/*     */   
/*     */   public static void scissorRect(float x, float y, float width, double height) {
/*  28 */     ScaledResolution sr = new ScaledResolution(mc);
/*  29 */     int factor = sr.getScaleFactor();
/*  30 */     GL11.glScissor((int)(x * factor), (int)((sr.getScaledHeight() - height) * factor), (int)((width - x) * factor), (int)((height - y) * factor));
/*     */   }
/*     */   
/*     */   public static int darker(int color, float factor) {
/*  34 */     int r = (int)((color >> 16 & 0xFF) * factor);
/*  35 */     int g = (int)((color >> 8 & 0xFF) * factor);
/*  36 */     int b = (int)((color & 0xFF) * factor);
/*  37 */     int a = color >> 24 & 0xFF;
/*  38 */     return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | (a & 0xFF) << 24;
/*     */   }
/*     */   
/*     */   public static void setColor(int color) {
/*  42 */     GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(color >> 24 & 0xFF));
/*     */   }
/*     */   
/*     */   public static void setColor(Color color, float alpha) {
/*  46 */     float red = color.getRed() / 255.0F;
/*  47 */     float green = color.getGreen() / 255.0F;
/*  48 */     float blue = color.getBlue() / 255.0F;
/*  49 */     GlStateManager.color(red, green, blue, alpha);
/*     */   }
/*     */   
/*     */   public static void drawEntityBox(Entity entity, Color color, boolean fullBox, float alpha) {
/*  53 */     GlStateManager.pushMatrix();
/*  54 */     GlStateManager.blendFunc(770, 771);
/*  55 */     GL11.glEnable(3042);
/*  56 */     GlStateManager.glLineWidth(2.0F);
/*  57 */     GlStateManager.disableTexture2D();
/*  58 */     GL11.glDisable(2929);
/*  59 */     GlStateManager.depthMask(false);
/*  60 */     double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosX;
/*  61 */     double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosY;
/*  62 */     double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosZ;
/*  63 */     AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox();
/*  64 */     AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(axisAlignedBB.minX - entity.posX + x - 0.05D, axisAlignedBB.minY - entity.posY + y, axisAlignedBB.minZ - entity.posZ + z - 0.05D, axisAlignedBB.maxX - entity.posX + x + 0.05D, axisAlignedBB.maxY - entity.posY + y + 0.15D, axisAlignedBB.maxZ - entity.posZ + z + 0.05D);
/*  65 */     GlStateManager.glLineWidth(2.0F);
/*  66 */     GL11.glEnable(2848);
/*  67 */     GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha);
/*  68 */     if (fullBox) {
/*  69 */       drawColorBox(axisAlignedBB2, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha);
/*  70 */       GlStateManager.color(0.0F, 0.0F, 0.0F, 0.5F);
/*     */     } 
/*  72 */     drawSelectionBoundingBox(axisAlignedBB2);
/*  73 */     GlStateManager.glLineWidth(2.0F);
/*  74 */     GlStateManager.enableTexture2D();
/*  75 */     GL11.glEnable(2929);
/*  76 */     GlStateManager.depthMask(true);
/*  77 */     GlStateManager.disableBlend();
/*  78 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
/*  82 */     Tessellator tessellator = Tessellator.getInstance();
/*  83 */     BufferBuilder builder = tessellator.getBuffer();
/*  84 */     builder.begin(3, DefaultVertexFormats.POSITION);
/*  85 */     builder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
/*  86 */     builder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
/*  87 */     builder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
/*  88 */     builder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
/*  89 */     builder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
/*  90 */     tessellator.draw();
/*  91 */     builder.begin(3, DefaultVertexFormats.POSITION);
/*  92 */     builder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
/*  93 */     builder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
/*  94 */     builder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
/*  95 */     builder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
/*  96 */     builder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
/*  97 */     tessellator.draw();
/*  98 */     builder.begin(1, DefaultVertexFormats.POSITION);
/*  99 */     builder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
/* 100 */     builder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
/* 101 */     builder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
/* 102 */     builder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
/* 103 */     builder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
/* 104 */     builder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
/* 105 */     builder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
/* 106 */     builder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
/* 107 */     tessellator.draw();
/*     */   }
/*     */   
/*     */   public static void drawCircle3D(Entity entity, double radius, float partialTicks, int points, float width, int color) {
/* 111 */     GL11.glPushMatrix();
/* 112 */     GL11.glDisable(3553);
/* 113 */     GL11.glEnable(2848);
/* 114 */     GL11.glHint(3154, 4354);
/* 115 */     GL11.glDisable(2929);
/* 116 */     GL11.glLineWidth(width);
/* 117 */     GL11.glEnable(3042);
/* 118 */     GL11.glBlendFunc(770, 771);
/* 119 */     GL11.glDisable(2929);
/* 120 */     GL11.glBegin(3);
/* 121 */     double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - (mc.getRenderManager()).renderPosX;
/* 122 */     double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - (mc.getRenderManager()).renderPosY;
/* 123 */     double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - (mc.getRenderManager()).renderPosZ;
/* 124 */     setColor(color);
/* 125 */     for (int i = 0; i <= points; i++) {
/* 126 */       GL11.glVertex3d(x + radius * Math.cos((i * 6.2831855F / points)), y, z + radius * Math.sin((i * 6.2831855F / points)));
/*     */     }
/* 128 */     GL11.glEnd();
/* 129 */     GL11.glDepthMask(true);
/* 130 */     GL11.glDisable(3042);
/* 131 */     GL11.glEnable(2929);
/* 132 */     GL11.glDisable(2848);
/* 133 */     GL11.glEnable(2929);
/* 134 */     GL11.glEnable(3553);
/* 135 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public static void drawArrow(float x, float y, boolean up, int hexColor) {
/* 139 */     GL11.glPushMatrix();
/* 140 */     GlStateManager.scale(0.8D, 0.8D, 1.0D);
/* 141 */     GL11.glEnable(3042);
/* 142 */     GL11.glDisable(3553);
/* 143 */     GL11.glBlendFunc(770, 771);
/* 144 */     GL11.glEnable(2848);
/* 145 */     GL11.glDisable(3553);
/* 146 */     setColor(hexColor);
/* 147 */     GL11.glLineWidth(2.0F);
/* 148 */     GL11.glBegin(1);
/* 149 */     GL11.glVertex2d(x, (y + (up ? 4 : false)));
/* 150 */     GL11.glVertex2d((x + 3.0F), (y + (up ? false : 4)));
/* 151 */     GL11.glEnd();
/* 152 */     GL11.glBegin(1);
/* 153 */     GL11.glVertex2d((x + 3.0F), (y + (up ? false : 4)));
/* 154 */     GL11.glVertex2d((x + 6.0F), (y + (up ? 4 : false)));
/* 155 */     GL11.glEnd();
/* 156 */     GL11.glEnable(3553);
/* 157 */     GL11.glDisable(2848);
/* 158 */     GlStateManager.scale(2.0F, 2.0F, 1.0F);
/* 159 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, Color color) {
/* 163 */     ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getInstance());
/* 164 */     GL11.glDisable(2929);
/* 165 */     GL11.glEnable(3042);
/* 166 */     GL11.glDepthMask(false);
/* 167 */     OpenGlHelper.glBlendFunc(770, 771, 1, 0);
/* 168 */     setColor(color.getRGB());
/* 169 */     Minecraft.getInstance().getTextureManager().bindTexture(resourceLocation);
/* 170 */     Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
/* 171 */     GL11.glDepthMask(true);
/* 172 */     GL11.glDisable(3042);
/* 173 */     GL11.glEnable(2929);
/*     */   }
/*     */   
/*     */   public static void renderItem(ItemStack itemStack, int x, int y) {
/* 177 */     GlStateManager.enableBlend();
/* 178 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 179 */     GlStateManager.enableDepth();
/* 180 */     net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
/* 181 */     mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemStack, x, y);
/* 182 */     mc.getRenderItem().renderItemIntoGUI(itemStack, x, y);
/* 183 */     net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
/* 184 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 185 */     GlStateManager.disableDepth();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void drawCircle(float x, float y, float start, float end, float radius, boolean filled, Color color) {
/* 192 */     GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*     */     
/* 195 */     if (start > end) {
/* 196 */       float endOffset = end;
/* 197 */       end = start;
/* 198 */       start = endOffset;
/*     */     } 
/*     */     
/* 201 */     GlStateManager.enableBlend();
/* 202 */     GlStateManager.disableTexture2D();
/* 203 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 204 */     setColor(color.getRGB());
/* 205 */     GL11.glEnable(2848);
/* 206 */     GL11.glLineWidth(2.0F);
/* 207 */     GL11.glBegin(3); float i;
/* 208 */     for (i = end; i >= start; i -= 4.0F) {
/* 209 */       float cos = (float)(Math.cos(i * Math.PI / 180.0D) * radius * 1.0D);
/* 210 */       float sin = (float)(Math.sin(i * Math.PI / 180.0D) * radius * 1.0D);
/* 211 */       GL11.glVertex2f(x + cos, y + sin);
/*     */     } 
/* 213 */     GL11.glEnd();
/* 214 */     GL11.glDisable(2848);
/*     */     
/* 216 */     GL11.glEnable(2848);
/* 217 */     GL11.glBegin(filled ? 6 : 3);
/* 218 */     for (i = end; i >= start; i -= 4.0F) {
/* 219 */       float cos = (float)Math.cos(i * Math.PI / 180.0D) * radius;
/* 220 */       float sin = (float)Math.sin(i * Math.PI / 180.0D) * radius;
/* 221 */       GL11.glVertex2f(x + cos, y + sin);
/*     */     } 
/* 223 */     GL11.glEnd();
/* 224 */     GL11.glDisable(2848);
/* 225 */     GlStateManager.enableTexture2D();
/* 226 */     GlStateManager.disableBlend();
/*     */   }
/*     */   
/*     */   public static void drawCircle(float x, float y, float radius, boolean filled, Color color) {
/* 230 */     drawCircle(x, y, 0.0F, 360.0F, radius, filled, color);
/*     */   }
/*     */   
/*     */   public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
/* 234 */     Tessellator ts = Tessellator.getInstance();
/* 235 */     BufferBuilder buffer = ts.getBuffer();
/* 236 */     buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 237 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 238 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 239 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 240 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 241 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 242 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 243 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 244 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 245 */     ts.draw();
/* 246 */     buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 247 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 248 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 249 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 250 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 251 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 252 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 253 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 254 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 255 */     ts.draw();
/* 256 */     buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 257 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 258 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 259 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 260 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 261 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 262 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 263 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 264 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 265 */     ts.draw();
/* 266 */     buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 267 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 268 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 269 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 270 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 271 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 272 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 273 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 274 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 275 */     ts.draw();
/* 276 */     buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 277 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 278 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 279 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 280 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 281 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 282 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 283 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 284 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 285 */     ts.draw();
/* 286 */     buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 287 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 288 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 289 */     buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 290 */     buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 291 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 292 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
/* 293 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 294 */     buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
/* 295 */     ts.draw();
/*     */   }
/*     */   
/*     */   public static boolean isInViewFrustum(Entity entity) {
/* 299 */     return (isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck);
/*     */   }
/*     */   
/*     */   private static boolean isInViewFrustum(AxisAlignedBB bb) {
/* 303 */     Entity current = mc.getRenderViewEntity();
/* 304 */     if (current != null) {
/* 305 */       frustum.setPosition(current.posX, current.posY, current.posZ);
/*     */     }
/* 307 */     return frustum.isBoundingBoxInFrustum(bb);
/*     */   }
/*     */   
/*     */   public static void blockEsp(BlockPos blockPos, Color color, boolean outline) {
/* 311 */     double x = blockPos.getX() - (mc.getRenderManager()).renderPosX;
/* 312 */     double y = blockPos.getY() - (mc.getRenderManager()).renderPosY;
/* 313 */     double z = blockPos.getZ() - (mc.getRenderManager()).renderPosZ;
/* 314 */     GL11.glPushMatrix();
/* 315 */     GL11.glBlendFunc(770, 771);
/* 316 */     GL11.glEnable(3042);
/* 317 */     GL11.glLineWidth(2.0F);
/* 318 */     GL11.glDisable(3553);
/* 319 */     GL11.glDisable(2929);
/* 320 */     GL11.glDepthMask(false);
/* 321 */     GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 0.15F);
/* 322 */     drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), 0.0F, 0.0F, 0.0F, 0.0F);
/* 323 */     if (outline) {
/* 324 */       GlStateManager.color(0.0F, 0.0F, 0.0F, 0.5F);
/* 325 */       drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
/*     */     } 
/* 327 */     GL11.glLineWidth(2.0F);
/* 328 */     GL11.glEnable(3553);
/* 329 */     GL11.glEnable(2929);
/* 330 */     GL11.glDepthMask(true);
/* 331 */     GL11.glDisable(3042);
/* 332 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public static void blockEspFrame(BlockPos blockPos, float red, float green, float blue) {
/* 336 */     double x = blockPos.getX() - (mc.getRenderManager()).renderPosX;
/* 337 */     double y = blockPos.getY() - (mc.getRenderManager()).renderPosY;
/* 338 */     double z = blockPos.getZ() - (mc.getRenderManager()).renderPosZ;
/* 339 */     GL11.glBlendFunc(770, 771);
/* 340 */     GL11.glEnable(3042);
/* 341 */     GL11.glLineWidth(2.0F);
/* 342 */     GL11.glDisable(3553);
/* 343 */     GL11.glDisable(2929);
/* 344 */     GL11.glDepthMask(false);
/* 345 */     GlStateManager.color(red, green, blue, 1.0F);
/* 346 */     drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
/* 347 */     GL11.glEnable(3553);
/* 348 */     GL11.glEnable(2929);
/* 349 */     GL11.glDepthMask(true);
/* 350 */     GL11.glDisable(3042);
/*     */   }
/*     */   
/*     */   public static void drawTriangle(float x, float y, float size, float vector, int color) {
/* 354 */     GlStateManager.translate(x, y, 0.0F);
/* 355 */     GlStateManager.rotate(180.0F + vector, 0.0F, 0.0F, 1.0F);
/*     */     
/* 357 */     setColor(color);
/* 358 */     GlStateManager.enable(3042);
/* 359 */     GlStateManager.disable(3553);
/* 360 */     GlStateManager.enable(2848);
/* 361 */     GlStateManager.hint(3154, 4354);
/* 362 */     GlStateManager.blendFunc(770, 771);
/* 363 */     GlStateManager.glLineWidth(1.0F);
/* 364 */     GlStateManager.glBegin(6);
/*     */     
/* 366 */     GlStateManager.glVertex2f(0.0F, size);
/* 367 */     GlStateManager.glVertex2f(1.0F * size, -size);
/* 368 */     GlStateManager.glVertex2f(-(1.0F * size), -size);
/*     */     
/* 370 */     GlStateManager.glEnd();
/*     */     
/* 372 */     GlStateManager.glLineWidth(3.0F);
/* 373 */     GlStateManager.glBegin(2);
/* 374 */     setColor((new Color(color)).darker().getRGB());
/* 375 */     GlStateManager.glVertex2f(0.0F, size);
/* 376 */     GlStateManager.glVertex2f(1.0F * size, -size);
/* 377 */     GlStateManager.glVertex2f(-(1.0F * size), -size);
/*     */     
/* 379 */     GlStateManager.glEnd();
/*     */     
/* 381 */     GlStateManager.disable(2848);
/* 382 */     GlStateManager.enable(3553);
/* 383 */     GlStateManager.disable(3042);
/* 384 */     GlStateManager.resetColor();
/* 385 */     GlStateManager.rotate(-180.0F - vector, 0.0F, 0.0F, 1.0F);
/* 386 */     GlStateManager.translate(-x, -y, 0.0F);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\render\RenderHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */