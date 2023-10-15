/*     */ package org.neverhook.client.feature.impl.visual;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class ChinaHat
/*     */   extends Feature
/*     */ {
/*  22 */   public ListSetting colorMode = new ListSetting("Color", "Rainbow", () -> Boolean.valueOf(true), new String[] { "Rainbow", "Astolfo", "Client", "Custom" });
/*  23 */   public ListSetting colorModeTop = new ListSetting("Color Top", "Rainbow", () -> Boolean.valueOf(true), new String[] { "Rainbow", "Astolfo", "Client", "Custom" });
/*  24 */   public ColorSetting customColor = new ColorSetting("Custom Color", (new Color(13311)).getRGB(), () -> Boolean.valueOf(this.colorModeTop.currentMode.equals("Custom")));
/*  25 */   public ColorSetting customColorTwo = new ColorSetting("Custom Color Two", (new Color(13311)).getRGB(), () -> Boolean.valueOf(this.colorMode.currentMode.equals("Custom")));
/*  26 */   public NumberSetting heightValue = new NumberSetting("Height", 0.4F, 0.1F, 20.0F, 0.1F, () -> Boolean.valueOf(true));
/*  27 */   public NumberSetting widthValue = new NumberSetting("Width", 1.2F, 0.1F, 20.0F, 0.1F, () -> Boolean.valueOf(true));
/*  28 */   public NumberSetting point = new NumberSetting("Points", 32.0F, 1.0F, 60.0F, 1.0F, () -> Boolean.valueOf(true));
/*  29 */   public BooleanSetting hide = new BooleanSetting("Hide In First Person", true, () -> Boolean.valueOf(true));
/*     */   
/*     */   public ChinaHat() {
/*  32 */     super("ChinaHat", "Показывает китайскую шляпу", Type.Visuals);
/*  33 */     addSettings(new Setting[] { (Setting)this.colorMode, (Setting)this.colorModeTop, (Setting)this.hide, (Setting)this.widthValue, (Setting)this.heightValue, (Setting)this.point, (Setting)this.customColor, (Setting)this.customColorTwo });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender3D(EventRender3D event) {
/*  38 */     double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosX;
/*  39 */     double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosY;
/*  40 */     double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosZ;
/*  41 */     y += mc.player.getEyeHeight() + 0.2D - (mc.player.isSneaking() ? 0.25D : 0.0D);
/*     */     
/*  43 */     if (mc.gameSettings.thirdPersonView == 0 && this.hide.getBoolValue()) {
/*     */       return;
/*     */     }
/*  46 */     GL11.glPushMatrix();
/*  47 */     GL11.glEnable(3042);
/*  48 */     GL11.glDisable(3553);
/*  49 */     GL11.glEnable(2848);
/*  50 */     GL11.glBlendFunc(770, 771);
/*  51 */     GL11.glLineWidth(2.0F);
/*  52 */     GL11.glShadeModel(7425);
/*  53 */     GL11.glDisable(2884);
/*     */     
/*  55 */     GL11.glBegin(8);
/*     */     
/*  57 */     double size = (mc.player.width * this.widthValue.getNumberValue());
/*     */     
/*  59 */     for (int i = 0; i <= this.point.getNumberValue(); i++) {
/*  60 */       int customColorValue = -1;
/*  61 */       int customColorValueTop = -1;
/*  62 */       double height = this.heightValue.getNumberValue();
/*  63 */       String colorModeValue = this.colorMode.getOptions();
/*  64 */       if (colorModeValue.equalsIgnoreCase("Rainbow")) {
/*  65 */         customColorValue = PaletteHelper.rainbow(i * 10, 0.4F, 1.0F).getRGB();
/*  66 */       } else if (colorModeValue.equalsIgnoreCase("Client")) {
/*  67 */         customColorValue = ClientHelper.getClientColor().getRGB();
/*  68 */       } else if (colorModeValue.equalsIgnoreCase("Custom")) {
/*  69 */         customColorValue = this.customColorTwo.getColorValue();
/*  70 */       } else if (colorModeValue.equalsIgnoreCase("Astolfo")) {
/*  71 */         Color astolfo = PaletteHelper.astolfo(false, i);
/*  72 */         customColorValue = (new Color(astolfo.getRed(), astolfo.getGreen(), astolfo.getBlue())).getRGB();
/*     */       } 
/*     */       
/*  75 */       String colorModeValueTop = this.colorModeTop.getOptions();
/*  76 */       if (colorModeValueTop.equalsIgnoreCase("Rainbow")) {
/*  77 */         customColorValueTop = PaletteHelper.rainbow(i * 10, 0.4F, 1.0F).getRGB();
/*  78 */       } else if (colorModeValueTop.equalsIgnoreCase("Client")) {
/*  79 */         customColorValueTop = ClientHelper.getClientColor().getRGB();
/*  80 */       } else if (colorModeValueTop.equalsIgnoreCase("Custom")) {
/*  81 */         customColorValueTop = this.customColorTwo.getColorValue();
/*  82 */       } else if (colorModeValueTop.equalsIgnoreCase("Astolfo")) {
/*  83 */         Color astolfo = PaletteHelper.astolfo(false, i);
/*  84 */         customColorValueTop = (new Color(astolfo.getRed(), astolfo.getGreen(), astolfo.getBlue())).getRGB();
/*     */       } 
/*     */       
/*  87 */       Color top = new Color(customColorValueTop);
/*  88 */       RenderHelper.setColor((new Color(top.getRed(), top.getGreen(), top.getBlue(), 120)).getRGB());
/*  89 */       GL11.glVertex3d(x, y + height, z);
/*  90 */       RenderHelper.setColor(customColorValue);
/*  91 */       double posX = x - Math.sin(i * Math.PI * 2.0D / this.point.getNumberValue()) * size;
/*  92 */       double posZ = z + Math.cos(i * Math.PI * 2.0D / this.point.getNumberValue()) * size;
/*  93 */       GL11.glVertex3d(posX, y, posZ);
/*     */     } 
/*     */     
/*  96 */     GL11.glEnd();
/*     */     
/*  98 */     GL11.glShadeModel(7424);
/*  99 */     GL11.glDisable(2848);
/* 100 */     GL11.glEnable(2884);
/* 101 */     GL11.glEnable(3553);
/* 102 */     GL11.glDisable(3042);
/* 103 */     GlStateManager.resetColor();
/* 104 */     GL11.glPopMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\ChinaHat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */