/*     */ package org.neverhook.client.feature.impl.visual;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class Trails
/*     */   extends Feature
/*     */ {
/*     */   public ListSetting colorMode;
/*     */   public ColorSetting customColor;
/*     */   public NumberSetting width;
/*     */   public NumberSetting height;
/*     */   public NumberSetting sizeToRemove;
/*  29 */   public ArrayList<Point> points = new ArrayList<>();
/*  30 */   public float pointCount = 0.0F;
/*     */   
/*     */   public Trails() {
/*  33 */     super("Trails", "Оставляет линию ходьбы", Type.Visuals);
/*  34 */     this.colorMode = new ListSetting("Color", "Rainbow", () -> Boolean.valueOf(true), new String[] { "Rainbow", "Astolfo", "Client", "Custom" });
/*  35 */     this.customColor = new ColorSetting("Custom Color", (new Color(16777215)).getRGB(), () -> Boolean.valueOf(this.colorMode.currentMode.equals("Custom")));
/*  36 */     this.width = new NumberSetting("Width", 2.0F, 1.0F, 6.0F, 1.0F, () -> Boolean.valueOf(true));
/*  37 */     this.height = new NumberSetting("Height", 0.0F, 0.0F, 6.0F, 0.01F, () -> Boolean.valueOf(true));
/*  38 */     this.sizeToRemove = new NumberSetting("Time", 150.0F, 10.0F, 300.0F, 10.0F, () -> Boolean.valueOf(true));
/*  39 */     addSettings(new Setting[] { (Setting)this.colorMode, (Setting)this.customColor, (Setting)this.width, (Setting)this.height, (Setting)this.sizeToRemove });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender3D(EventRender3D event) {
/*  44 */     String colorModeValue = this.colorMode.getOptions();
/*     */     
/*  46 */     if (!getState()) {
/*     */       return;
/*     */     }
/*  49 */     double x = (mc.getRenderManager()).renderPosX;
/*  50 */     double y = (mc.getRenderManager()).renderPosY;
/*  51 */     double z = (mc.getRenderManager()).renderPosZ;
/*     */     
/*  53 */     GL11.glPushMatrix();
/*  54 */     GL11.glEnable(3042);
/*  55 */     GL11.glEnable(2929);
/*  56 */     GL11.glDisable(3553);
/*  57 */     GL11.glEnable(2848);
/*  58 */     GL11.glHint(3154, 4354);
/*  59 */     GL11.glLineWidth(this.width.getNumberValue());
/*  60 */     GL11.glBlendFunc(770, 771);
/*     */     
/*  62 */     GL11.glBegin(3);
/*     */     
/*  64 */     float size = 2000.0F;
/*  65 */     long fade = System.currentTimeMillis() - (long)size;
/*  66 */     for (Point point : this.points) {
/*     */       
/*  68 */       float offset = (float)(point.getTime() - fade) / size;
/*  69 */       if (offset < 0.0F || offset > 1.0F) {
/*  70 */         this.pointCount = this.points.indexOf(point);
/*     */         continue;
/*     */       } 
/*  73 */       float alpha = offset;
/*     */       
/*  75 */       Color customColorValue = Color.white;
/*  76 */       if (colorModeValue.equalsIgnoreCase("Rainbow")) {
/*  77 */         customColorValue = PaletteHelper.rainbow((int)x * 300, 1.0F, 1.0F);
/*  78 */       } else if (colorModeValue.equalsIgnoreCase("Client")) {
/*  79 */         customColorValue = ClientHelper.getClientColor();
/*  80 */       } else if (colorModeValue.equalsIgnoreCase("Custom")) {
/*  81 */         customColorValue = new Color(this.customColor.getColorValue());
/*  82 */       } else if (colorModeValue.equalsIgnoreCase("Astolfo")) {
/*  83 */         customColorValue = PaletteHelper.astolfo(false, (int)alpha);
/*     */       } 
/*     */       
/*  86 */       RenderHelper.setColor(customColorValue, alpha);
/*  87 */       GL11.glVertex3d(point.getX() - x, point.getY() - y + 1.0D, point.getZ() - z);
/*  88 */       GL11.glVertex3d(point.getX() - x, point.getY() - y + 0.01D, point.getZ() - z);
/*     */     } 
/*     */     
/*  91 */     GL11.glEnd();
/*     */     
/*  93 */     GL11.glDepthMask(true);
/*  94 */     GL11.glDisable(3042);
/*  95 */     GL11.glDisable(2848);
/*  96 */     GL11.glEnable(3553);
/*  97 */     GL11.glDisable(2929);
/*  98 */     GlStateManager.resetColor();
/*  99 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void sendPacket(EventSendPacket event) {
/* 104 */     if (!getState())
/*     */       return; 
/* 106 */     if (event.getPacket() instanceof CPacketPlayer) {
/* 107 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 108 */       this.points.add(new Point(packet.getX(mc.player.posX), (packet.getY(mc.player.posY) < 0.0D) ? mc.player.posY : packet.getY(mc.player.posY), packet.getZ(mc.player.posZ), System.currentTimeMillis()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 114 */     this.points.clear();
/* 115 */     this.pointCount = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 120 */     this.points.clear();
/* 121 */     this.pointCount = 0.0F;
/*     */   }
/*     */   
/*     */   public static class Point
/*     */   {
/*     */     public double x;
/*     */     public double y;
/*     */     public double z;
/*     */     public long time;
/*     */     
/*     */     public Point(double x, double y, double z, long time) {
/* 132 */       this.x = x;
/* 133 */       this.y = y;
/* 134 */       this.z = z;
/* 135 */       this.time = time;
/*     */     }
/*     */     
/*     */     public double getX() {
/* 139 */       return this.x;
/*     */     }
/*     */     
/*     */     public double getY() {
/* 143 */       return this.y;
/*     */     }
/*     */     
/*     */     public double getZ() {
/* 147 */       return this.z;
/*     */     }
/*     */     
/*     */     public long getTime() {
/* 151 */       return this.time;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\Trails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */