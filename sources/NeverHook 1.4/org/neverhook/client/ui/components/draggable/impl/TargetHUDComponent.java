/*     */ package org.neverhook.client.ui.components.draggable.impl;
/*     */ 
/*     */ import com.mojang.realmsclient.gui.ChatFormatting;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.inventory.GuiInventory;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import optifine.CustomColors;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.feature.impl.combat.KillAura;
/*     */ import org.neverhook.client.feature.impl.misc.StreamerMode;
/*     */ import org.neverhook.client.helpers.math.MathematicHelper;
/*     */ import org.neverhook.client.helpers.render.AnimationHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.ui.components.draggable.DraggableModule;
/*     */ 
/*     */ public class TargetHUDComponent
/*     */   extends DraggableModule {
/*     */   private float healthBarWidth;
/*     */   private long changeTime;
/*     */   private float displayPercent;
/*     */   private long lastUpdate;
/*     */   
/*     */   public TargetHUDComponent() {
/*  40 */     super("TargetHUDComponent", 200, 200);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  45 */     return 155;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  50 */     return 87;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(int mouseX, int mouseY) {
/*  55 */     String mode = KillAura.targetHudMode.getOptions();
/*  56 */     EntityPlayerSP entityPlayerSP = mc.player;
/*  57 */     int color = KillAura.targetHudColor.getColorValue();
/*  58 */     if (mode.equalsIgnoreCase("Astolfo")) {
/*  59 */       float x = getX(), y = getY();
/*  60 */       double healthWid = (entityPlayerSP.getHealth() / entityPlayerSP.getMaxHealth() * 120.0F);
/*  61 */       healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
/*  62 */       double check = (entityPlayerSP.getHealth() < 18.0F && entityPlayerSP.getHealth() > 1.0F) ? 8.0D : 0.0D;
/*  63 */       this.healthBarWidth = AnimationHelper.calculateCompensation((float)healthWid, this.healthBarWidth, 0L, 0.005D);
/*  64 */       RectHelper.drawRectBetter(x, y, 155.0D, 60.0D, (new Color(20, 20, 20, 200)).getRGB());
/*  65 */       if (!entityPlayerSP.getName().isEmpty()) {
/*  66 */         mc.fontRendererObj.drawStringWithShadow((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName(), x + 31.0F, y + 5.0F, -1);
/*     */       }
/*  68 */       GlStateManager.pushMatrix();
/*  69 */       GlStateManager.translate(x, y, 1.0F);
/*  70 */       GlStateManager.scale(2.5F, 2.5F, 2.5F);
/*  71 */       GlStateManager.translate(-x - 3.0F, -y - 2.0F, 1.0F);
/*  72 */       mc.fontRendererObj.drawStringWithShadow(MathematicHelper.round(entityPlayerSP.getHealth() / 2.0F, 1) + " ❤", x + 16.0F, y + 10.0F, (new Color(color)).getRGB());
/*  73 */       GlStateManager.popMatrix();
/*  74 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/*  76 */       mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, entityPlayerSP.getHeldItem(EnumHand.OFF_HAND), (int)x + 137, (int)y + 7);
/*  77 */       mc.getRenderItem().renderItemIntoGUI(entityPlayerSP.getHeldItem(EnumHand.OFF_HAND), ((int)x + 137), ((int)y + 1));
/*     */       
/*  79 */       GuiInventory.drawEntityOnScreen(x + 16.0F, y + 55.0F, 25, ((EntityPlayer)entityPlayerSP).rotationYaw, -((EntityPlayer)entityPlayerSP).rotationPitch, (EntityLivingBase)entityPlayerSP);
/*  80 */       RectHelper.drawRectBetter((x + 30.0F), (y + 48.0F), 120.0D, 8.0D, (new Color(color)).darker().darker().darker().getRGB());
/*  81 */       RectHelper.drawRectBetter((x + 30.0F), (y + 48.0F), (float)(this.healthBarWidth + check), 8.0D, (new Color(color)).darker().getRGB());
/*  82 */       RectHelper.drawRectBetter((x + 30.0F), (y + 48.0F), (float)healthWid, 8.0D, (new Color(color)).getRGB());
/*  83 */     } else if (mode.equalsIgnoreCase("Novoline Old")) {
/*  84 */       if (entityPlayerSP == null)
/*     */         return; 
/*  86 */       if (entityPlayerSP.getHealth() < 0.0F)
/*     */         return; 
/*  88 */       float x = getX(), y = getY();
/*  89 */       float healthWid = entityPlayerSP.getHealth() / entityPlayerSP.getMaxHealth() * (40 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()));
/*  90 */       healthWid = MathHelper.clamp(healthWid, 0.0F, 124.0F);
/*  91 */       this.healthBarWidth = AnimationHelper.calculateCompensation(healthWid, this.healthBarWidth, 0L, 0.0D);
/*  92 */       this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0F, 124.0F);
/*  93 */       RectHelper.drawRectBetter(x, y, (65 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()) + 25), 40.0D, (new Color(19, 19, 19, 255)).getRGB());
/*  94 */       RectHelper.drawRectBetter((x + 1.0F), (y + 1.0F), (65 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()) + 23), 38.0D, (new Color(41, 41, 41, 255)).getRGB());
/*  95 */       if (!entityPlayerSP.getName().isEmpty()) {
/*  96 */         mc.fontRendererObj.drawStringWithShadow((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName(), x + 42.0F, y + 5.0F, -1);
/*     */       }
/*  98 */       GlStateManager.pushMatrix();
/*  99 */       GlStateManager.translate(x, y, 1.0F);
/* 100 */       GlStateManager.scale(1.05F, 1.05F, 1.05F);
/* 101 */       GlStateManager.translate(-x + 24.0F, -y + 18.0F, 1.0F);
/* 102 */       String hp = MathematicHelper.round(entityPlayerSP.getHealth() / 2.0F, 1) + "";
/* 103 */       mc.fontRendererObj.drawStringWithShadow(hp, x + 17.0F, y + 10.0F, -1);
/* 104 */       mc.fontRendererObj.drawStringWithShadow(" ❤", x + mc.fontRendererObj.getStringWidth(hp) + 16.0F, y + 10.0F, (new Color(color)).getRGB());
/* 105 */       GlStateManager.popMatrix();
/* 106 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 108 */       GlStateManager.pushMatrix();
/* 109 */       GlStateManager.translate(x, y, 1.0F);
/* 110 */       GlStateManager.scale(0.8F, 0.8F, 0.8F);
/* 111 */       GlStateManager.translate(-x + 148.0F, -y + 38.0F, 1.0F);
/* 112 */       boolean stack = (entityPlayerSP.getHeldItemOffhand().isStackable() && !entityPlayerSP.getHeldItemOffhand().isEmpty());
/* 113 */       if (stack) {
/* 114 */         mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, entityPlayerSP.getHeldItem(EnumHand.OFF_HAND), (int)(x - 39.0F + mc.fontRenderer.getStringWidth(hp) - 21.0F), (int)(y - 8.0F));
/* 115 */         mc.getRenderItem().renderItemIntoGUI(entityPlayerSP.getHeldItem(EnumHand.OFF_HAND), (int)(x - 49.0F + mc.fontRenderer.getStringWidth(hp) - 21.0F), ((int)y - 5));
/*     */       } 
/* 117 */       GlStateManager.popMatrix();
/* 118 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 119 */       for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
/* 120 */         if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == entityPlayerSP) {
/* 121 */           mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
/* 122 */           float hurtPercent = getHurtPercent((EntityLivingBase)entityPlayerSP);
/* 123 */           GL11.glPushMatrix();
/* 124 */           GL11.glColor4f(1.0F, 1.0F - hurtPercent, 1.0F - hurtPercent, 1.0F);
/* 125 */           Gui.drawScaledCustomSizeModalRect(((int)x + 1), ((int)y + 1), 8.0F, 8.0F, 8.0F, 8.0F, 38.0F, 38.0F, 64.0F, 64.0F);
/* 126 */           GL11.glPopMatrix();
/* 127 */           GlStateManager.bindTexture(0);
/*     */         } 
/* 129 */         GL11.glDisable(3089);
/*     */       } 
/* 131 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()) + 9.0F), (40 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName())), 8.0D, (new Color(35, 35, 35, 255)).getRGB());
/* 132 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()) + 9.0F), (entityPlayerSP.getHealth() > 18.0F) ? healthWid : (this.healthBarWidth + 4.0F), 8.0D, (new Color(color)).darker().getRGB());
/* 133 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()) + 9.0F), healthWid, 8.0D, (new Color(color)).getRGB());
/* 134 */     } else if (mode.equalsIgnoreCase("Novoline New")) {
/* 135 */       if (entityPlayerSP == null)
/*     */         return; 
/* 137 */       if (entityPlayerSP.getHealth() < 0.0F)
/*     */         return; 
/* 139 */       float x = getX(), y = getY();
/* 140 */       float healthWid = entityPlayerSP.getHealth() / entityPlayerSP.getMaxHealth() * (40 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()));
/* 141 */       healthWid = (float)MathHelper.clamp(healthWid, 0.0D, 124.0D);
/* 142 */       this.healthBarWidth = AnimationHelper.calculateCompensation(healthWid, this.healthBarWidth, 0L, 0.0D);
/* 143 */       this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0F, 124.0F);
/* 144 */       RectHelper.drawRectBetter(x, y, (65 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()) + 25), 40.0D, (new Color(19, 19, 19, 255)).getRGB());
/* 145 */       RectHelper.drawRectBetter((x + 1.0F), (y + 1.0F), (65 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()) + 23), 38.0D, (new Color(41, 41, 41, 255)).getRGB());
/* 146 */       if (!entityPlayerSP.getName().isEmpty()) {
/* 147 */         mc.fontRendererObj.drawStringWithShadow((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName(), x + 42.0F, y + 5.0F, -1);
/*     */       }
/* 149 */       GlStateManager.pushMatrix();
/* 150 */       GlStateManager.translate(x, y, 1.0F);
/* 151 */       GlStateManager.scale(1.05F, 1.05F, 1.05F);
/* 152 */       GlStateManager.translate(-x + 24.0F, -y + 18.0F, 1.0F);
/* 153 */       GlStateManager.popMatrix();
/* 154 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 156 */       GlStateManager.pushMatrix();
/* 157 */       GlStateManager.translate(x, y, 1.0F);
/* 158 */       GlStateManager.scale(0.8F, 0.8F, 0.8F);
/* 159 */       GlStateManager.translate(-x + 148.0F, -y + 38.0F, 1.0F);
/* 160 */       boolean stack = (entityPlayerSP.getHeldItemOffhand().isStackable() && !entityPlayerSP.getHeldItemOffhand().isEmpty());
/* 161 */       if (stack) {
/* 162 */         mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, entityPlayerSP.getHeldItem(EnumHand.OFF_HAND), (int)(x - 88.0F), (int)(y - 8.0F));
/* 163 */         mc.getRenderItem().renderItemIntoGUI(entityPlayerSP.getHeldItem(EnumHand.OFF_HAND), (int)(x - 98.0F), ((int)y - 5));
/*     */       } 
/* 165 */       GlStateManager.popMatrix();
/* 166 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 167 */       for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
/* 168 */         if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == entityPlayerSP) {
/* 169 */           mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
/* 170 */           float hurtPercent = getHurtPercent((EntityLivingBase)entityPlayerSP);
/* 171 */           GL11.glPushMatrix();
/* 172 */           GL11.glColor4f(1.0F, 1.0F - hurtPercent, 1.0F - hurtPercent, 1.0F);
/* 173 */           Gui.drawScaledCustomSizeModalRect(((int)x + 1), ((int)y + 1), 8.0F, 8.0F, 8.0F, 8.0F, 38.0F, 38.0F, 64.0F, 64.0F);
/* 174 */           GL11.glPopMatrix();
/* 175 */           GlStateManager.bindTexture(0);
/*     */         } 
/* 177 */         GL11.glDisable(3089);
/*     */       } 
/* 179 */       String hp = MathematicHelper.round(entityPlayerSP.getHealth() / entityPlayerSP.getMaxHealth() * 100.0F, 1) + "%";
/* 180 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()) + 9.0F), (40 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName())), 10.0D, (new Color(35, 35, 35, 255)).getRGB());
/* 181 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()) + 9.0F), (entityPlayerSP.getHealth() > 18.0F) ? healthWid : (this.healthBarWidth + 4.0F), 10.0D, (new Color(color)).darker().getRGB());
/* 182 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()) + 9.0F), healthWid, 10.0D, (new Color(color)).getRGB());
/* 183 */       mc.fontRendererObj.drawStringWithShadow(hp, x + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName()) / 2.0F + 50.0F, y + 19.0F, -1);
/* 184 */     } else if (mode.equalsIgnoreCase("Dev")) {
/* 185 */       float x = getX(), y = getY();
/* 186 */       float x2 = getX(), y2 = getY();
/* 187 */       double healthWid = (entityPlayerSP.getHealth() / entityPlayerSP.getMaxHealth() * 120.0F);
/* 188 */       healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
/* 189 */       double check = (entityPlayerSP != null && entityPlayerSP.getHealth() < ((entityPlayerSP instanceof EntityPlayer) ? 18 : 10) && entityPlayerSP.getHealth() > 1.0F) ? 8.0D : 0.0D;
/* 190 */       this.healthBarWidth = AnimationHelper.calculateCompensation((float)healthWid, this.healthBarWidth, 0L, 0.005D);
/* 191 */       RectHelper.drawRectBetter(x, y, 145.0D, 50.0D, (new Color(23, 23, 25, 203)).getRGB());
/* 192 */       if (!entityPlayerSP.getName().isEmpty()) {
/* 193 */         mc.robotoRegularFontRender.drawStringWithShadow((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName(), (x + 37.0F), (y + 5.0F), -1);
/*     */       }
/* 195 */       GlStateManager.pushMatrix();
/* 196 */       GlStateManager.translate(x, y, 1.0F);
/* 197 */       GlStateManager.scale(1.5F, 1.5F, 1.5F);
/* 198 */       GlStateManager.translate(-x - 14.0F, -y + 14.0F, 1.0F);
/* 199 */       mc.fontRendererObj.drawStringWithShadow("§c❤", x + 16.0F, y + 10.0F, -1);
/* 200 */       GlStateManager.popMatrix();
/* 201 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 203 */       mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, entityPlayerSP.getHeldItem(EnumHand.OFF_HAND), (int)x + 125, (int)y + 7);
/* 204 */       mc.getRenderItem().renderItemIntoGUI(entityPlayerSP.getHeldItem(EnumHand.OFF_HAND), ((int)x + 125), ((int)y + 1));
/*     */       
/* 206 */       ArrayList<ItemStack> list = new ArrayList<>();
/* 207 */       for (int i = 0; i < 5; i++) {
/* 208 */         ItemStack armorSlot = entityPlayerSP.getEquipmentInSlot(i);
/* 209 */         if (armorSlot != null) {
/* 210 */           list.add(armorSlot);
/*     */         }
/*     */       } 
/* 213 */       for (ItemStack itemStack : list) {
/* 214 */         RenderHelper.enableGUIStandardItemLighting();
/* 215 */         RenderHelper.renderItem(itemStack, (int)x2 + 36, (int)(y + 16.0F));
/* 216 */         x2 += 16.0F;
/*     */       } 
/*     */       
/* 219 */       for (PotionEffect effect : entityPlayerSP.getActivePotionEffects()) {
/* 220 */         Potion potion = Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()));
/* 221 */         assert potion != null;
/* 222 */         String name = I18n.format(potion.getName(), new Object[0]);
/* 223 */         String PType = "";
/* 224 */         if (effect.getAmplifier() == 1) {
/* 225 */           name = name + " 2";
/* 226 */         } else if (effect.getAmplifier() == 2) {
/* 227 */           name = name + " 3";
/* 228 */         } else if (effect.getAmplifier() == 3) {
/* 229 */           name = name + " 4";
/*     */         } 
/* 231 */         if (effect.getDuration() < 600 && effect.getDuration() > 300) {
/* 232 */           PType = PType + " " + Potion.getDurationString(effect);
/* 233 */         } else if (effect.getDuration() < 300) {
/* 234 */           PType = PType + " " + Potion.getDurationString(effect);
/* 235 */         } else if (effect.getDuration() > 600) {
/* 236 */           PType = PType + " " + Potion.getDurationString(effect);
/*     */         } 
/* 238 */         GlStateManager.pushMatrix();
/* 239 */         GlStateManager.disableBlend();
/* 240 */         mc.fontRendererObj.drawStringWithShadow(name + ": " + ChatFormatting.GRAY + PType, x + 1.0F, y2 - 9.0F, potion.getLiquidColor());
/* 241 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 242 */         GlStateManager.popMatrix();
/* 243 */         y2 -= 10.0F;
/*     */       } 
/*     */       
/* 246 */       for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
/* 247 */         if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == entityPlayerSP) {
/* 248 */           mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
/* 249 */           float hurtPercent = getHurtPercent((EntityLivingBase)entityPlayerSP);
/* 250 */           GL11.glPushMatrix();
/* 251 */           GL11.glColor4f(1.0F, 1.0F - hurtPercent, 1.0F - hurtPercent, 1.0F);
/* 252 */           Gui.drawScaledCustomSizeModalRect(((int)x + 1), ((int)y + 1), 8.0F, 8.0F, 8.0F, 8.0F, 34.0F, 34.0F, 64.0F, 64.0F);
/* 253 */           GL11.glPopMatrix();
/* 254 */           GlStateManager.bindTexture(0);
/*     */         } 
/* 256 */         GL11.glDisable(3089);
/*     */       } 
/*     */       
/* 259 */       RectHelper.drawRectBetter((x + 18.0F), (y + 41.0F), 120.0D, 3.0D, (new Color(20, 221, 32)).darker().darker().darker().getRGB());
/* 260 */       RectHelper.drawRectBetter((x + 18.0F), (y + 41.0F), this.healthBarWidth + check, 3.0D, (new Color((new Color(255, 55, 55)).darker().getRGB())).getRGB());
/* 261 */       RectHelper.drawRectBetter((x + 18.0F), (y + 41.0F), healthWid, 3.0D, (new Color((new Color(20, 221, 32)).getRGB())).getRGB());
/* 262 */     } else if (mode.equalsIgnoreCase("Minecraft")) {
/* 263 */       if (entityPlayerSP == null)
/*     */         return; 
/* 265 */       float x = getX(), y = getY();
/* 266 */       GlStateManager.pushMatrix();
/* 267 */       RectHelper.drawOutlineRect(x - 2.0F, y - 7.0F, 155.0F, 38.0F, new Color(20, 20, 20, 255), new Color(255, 255, 255, 255));
/* 268 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 269 */       mc.fontRendererObj.drawStringWithShadow(entityPlayerSP.getName(), (getX() + 37), (getY() - 2), -1);
/* 270 */       for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
/* 271 */         if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == entityPlayerSP) {
/* 272 */           mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
/* 273 */           Gui.drawScaledCustomSizeModalRect((int)x, ((int)y - 5), 8.0F, 8.0F, 8.0F, 8.0F, 34.0F, 34.0F, 64.0F, 64.0F);
/* 274 */           GlStateManager.bindTexture(0);
/*     */         } 
/* 276 */         GL11.glDisable(3089);
/*     */       } 
/* 278 */       mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
/* 279 */       int i = 0;
/* 280 */       while (i < entityPlayerSP.getMaxHealth() / 2.0F) {
/* 281 */         mc.ingameGUI.drawTexturedModalRect((getX() + 86) - entityPlayerSP.getMaxHealth() / 2.0F * 10.0F / 2.0F + (i * 8), (getY() + 9), 16, 0, 9, 9);
/* 282 */         i++;
/*     */       } 
/* 284 */       i = 0;
/* 285 */       while (i < entityPlayerSP.getHealth() / 2.0F) {
/* 286 */         mc.ingameGUI.drawTexturedModalRect((getX() + 86) - entityPlayerSP.getMaxHealth() / 2.0F * 10.0F / 2.0F + (i * 8), (getY() + 9), 52, 0, 9, 9);
/* 287 */         i++;
/*     */       } 
/*     */       
/* 290 */       int i3 = entityPlayerSP.getTotalArmorValue();
/* 291 */       for (int k3 = 0; k3 < 10; k3++) {
/* 292 */         if (i3 > 0) {
/* 293 */           int l3 = getX() + 36 + k3 * 8;
/* 294 */           if (k3 * 2 + 1 < i3) {
/* 295 */             mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 34, 9, 9, 9);
/*     */           }
/*     */           
/* 298 */           if (k3 * 2 + 1 == i3) {
/* 299 */             mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 25, 9, 9, 9);
/*     */           }
/*     */           
/* 302 */           if (k3 * 2 + 1 > i3) {
/* 303 */             mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 16, 9, 9, 9);
/*     */           }
/*     */         } 
/*     */       } 
/* 307 */       GlStateManager.popMatrix();
/* 308 */     } else if (mode.equalsIgnoreCase("Flux")) {
/* 309 */       float x = getX(), y = getY();
/*     */       
/* 311 */       double armorWid = (entityPlayerSP.getTotalArmorValue() * 6);
/* 312 */       double healthWid = (entityPlayerSP.getHealth() / entityPlayerSP.getMaxHealth() * 120.0F);
/* 313 */       healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
/*     */       
/* 315 */       RectHelper.drawRectBetter(x, y, 125.0D, 55.0D, (new Color(39, 39, 37, 235)).getRGB());
/*     */       
/* 317 */       String pvpState = "";
/* 318 */       if (mc.player.getHealth() == entityPlayerSP.getHealth()) {
/* 319 */         pvpState = "Finish Him!";
/* 320 */       } else if (mc.player.getHealth() < entityPlayerSP.getHealth()) {
/* 321 */         pvpState = "Losing Fight";
/* 322 */       } else if (mc.player.getHealth() > entityPlayerSP.getHealth()) {
/* 323 */         pvpState = "Winning Fight";
/*     */       } 
/* 325 */       if (!entityPlayerSP.getName().isEmpty()) {
/* 326 */         mc.robotoRegularFontRender.drawStringWithShadow((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : entityPlayerSP.getName(), (x + 38.0F), (y + 6.0F), -1);
/* 327 */         mc.clickguismall.drawStringWithShadow(pvpState, (x + 38.0F), (y + 17.0F), -1);
/*     */       } 
/*     */       
/* 330 */       for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
/* 331 */         if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == entityPlayerSP) {
/* 332 */           mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
/* 333 */           float hurtPercent = getHurtPercent((EntityLivingBase)entityPlayerSP);
/* 334 */           GL11.glPushMatrix();
/* 335 */           GL11.glColor4f(1.0F, 1.0F - hurtPercent, 1.0F - hurtPercent, 1.0F);
/* 336 */           Gui.drawScaledCustomSizeModalRect(x + 1.5F, y + 1.5F, 8.0F, 8.0F, 8.0F, 8.0F, 34.0F, 34.0F, 64.0F, 64.0F);
/* 337 */           GL11.glPopMatrix();
/* 338 */           GlStateManager.bindTexture(0);
/*     */         } 
/* 340 */         GL11.glDisable(3089);
/*     */       } 
/*     */ 
/*     */       
/* 344 */       RectHelper.drawRectBetter((x + 1.5F), (y + 39.0F), 120.0D, 4.0D, (new Color(26, 28, 25, 255)).getRGB());
/* 345 */       RectHelper.drawRectBetter((x + 1.5F), (y + 39.0F), healthWid, 4.0D, (new Color(2, 145, 98, 255)).getRGB());
/*     */ 
/*     */       
/* 348 */       RectHelper.drawRectBetter((x + 1.5F), (y + 47.0F), 120.0D, 4.0D, (new Color(26, 28, 25, 255)).getRGB());
/* 349 */       RectHelper.drawRectBetter((x + 1.5F), (y + 47.0F), armorWid, 4.0D, (new Color(65, 138, 195, 255)).getRGB());
/*     */     } 
/* 351 */     super.render(mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw() {
/* 356 */     String mode = KillAura.targetHudMode.getOptions();
/* 357 */     EntityLivingBase target = KillAura.target;
/* 358 */     int color = KillAura.targetHudColor.getColorValue();
/*     */     
/* 360 */     long time = System.currentTimeMillis();
/* 361 */     float pct = (float)(time - this.lastUpdate) / 1000.0F;
/* 362 */     this.lastUpdate = System.currentTimeMillis();
/* 363 */     if (target != null) {
/* 364 */       if (this.displayPercent < 1.0F) {
/* 365 */         this.displayPercent += pct;
/*     */       }
/* 367 */       if (this.displayPercent > 1.0F) {
/* 368 */         this.displayPercent = 1.0F;
/*     */       }
/*     */     } else {
/* 371 */       if (this.displayPercent > 0.0F) {
/* 372 */         this.displayPercent -= pct;
/*     */       }
/* 374 */       if (this.displayPercent < 0.0F) {
/* 375 */         this.displayPercent = 0.0F;
/*     */       }
/*     */     } 
/*     */     
/* 379 */     if (mode.equalsIgnoreCase("Astolfo")) {
/* 380 */       float x = getX(), y = getY();
/* 381 */       double healthWid = (target.getHealth() / target.getMaxHealth() * 120.0F);
/* 382 */       healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
/* 383 */       double check = (target.getHealth() < 18.0F && target.getHealth() > 1.0F) ? 8.0D : 0.0D;
/* 384 */       this.healthBarWidth = AnimationHelper.calculateCompensation((float)healthWid, this.healthBarWidth, 0L, 0.005D);
/* 385 */       RectHelper.drawRectBetter(x, y, 155.0D, 60.0D, (new Color(20, 20, 20, 200)).getRGB());
/* 386 */       if (!target.getName().isEmpty()) {
/* 387 */         mc.fontRendererObj.drawStringWithShadow((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName(), x + 31.0F, y + 5.0F, -1);
/*     */       }
/* 389 */       GlStateManager.pushMatrix();
/* 390 */       GlStateManager.translate(x, y, 1.0F);
/* 391 */       GlStateManager.scale(2.5F, 2.5F, 2.5F);
/* 392 */       GlStateManager.translate(-x - 3.0F, -y - 2.0F, 1.0F);
/* 393 */       mc.fontRendererObj.drawStringWithShadow(MathematicHelper.round(target.getHealth() / 2.0F, 1) + " ❤", x + 16.0F, y + 10.0F, (new Color(color)).getRGB());
/* 394 */       GlStateManager.popMatrix();
/* 395 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 397 */       mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)x + 137, (int)y + 7);
/* 398 */       mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), ((int)x + 137), ((int)y + 1));
/*     */       
/* 400 */       GuiInventory.drawEntityOnScreen(x + 16.0F, y + 55.0F, 25, target.rotationYaw, -target.rotationPitch, target);
/* 401 */       RectHelper.drawRectBetter((x + 30.0F), (y + 48.0F), 120.0D, 8.0D, (new Color(color)).darker().darker().darker().getRGB());
/* 402 */       RectHelper.drawRectBetter((x + 30.0F), (y + 48.0F), this.healthBarWidth + check, 8.0D, (new Color(color)).darker().getRGB());
/* 403 */       RectHelper.drawRectBetter((x + 30.0F), (y + 48.0F), healthWid, 8.0D, (new Color(color)).getRGB());
/* 404 */     } else if (mode.equalsIgnoreCase("Small")) {
/* 405 */       float x = getX(), y = getY();
/*     */       
/* 407 */       double healthWid = (target.getHealth() / target.getMaxHealth() * 100.0F);
/* 408 */       healthWid = MathHelper.clamp(healthWid, 0.0D, 100.0D);
/* 409 */       this.healthBarWidth = AnimationHelper.calculateCompensation((float)healthWid, this.healthBarWidth, 0L, 0.005D);
/* 410 */       RectHelper.drawRectBetter(x, y, 140.0D, 34.0D, (new Color(20, 20, 20)).getRGB());
/*     */ 
/*     */ 
/*     */       
/* 414 */       if (!target.getName().isEmpty()) {
/* 415 */         mc.fontRenderer.drawStringWithShadow((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName(), (x + 38.0F), (y + 5.0F), -1);
/*     */       }
/* 417 */       mc.fontRenderer.drawStringWithShadow(mc.player.connection.getPlayerInfo(target.getName()).getResponseTime() + "ms", (x + 38.0F), (y + 15.0F), -1);
/*     */       
/* 419 */       RectHelper.drawRectBetter((x + 38.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 15.0F), (10 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName())), 8.0D, (new Color(35, 35, 35, 255)).getRGB());
/* 420 */       RectHelper.drawRectBetter((x + 38.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 15.0F), (target.getHealth() > 18.0F) ? healthWid : (this.healthBarWidth + 4.0F), 8.0D, (new Color(color)).darker().getRGB());
/* 421 */       RectHelper.drawRectBetter((x + 38.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 15.0F), healthWid, 8.0D, (new Color(color)).getRGB());
/*     */       
/* 423 */       mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)x + 137, (int)y + 7);
/* 424 */       mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), ((int)x + 137), ((int)y + 1));
/* 425 */       for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
/* 426 */         if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
/* 427 */           mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
/* 428 */           float hurtPercent = getHurtPercent(target);
/* 429 */           GL11.glPushMatrix();
/* 430 */           GL11.glColor4f(1.0F, 1.0F - hurtPercent, 1.0F - hurtPercent, 1.0F);
/* 431 */           Gui.drawScaledCustomSizeModalRect(((int)x + 1), ((int)y + 1), 8.0F, 8.0F, 8.0F, 8.0F, 32.0F, 32.0F, 64.0F, 64.0F);
/* 432 */           GL11.glPushMatrix();
/* 433 */           GlStateManager.bindTexture(0);
/*     */         } 
/* 435 */         GL11.glDisable(3089);
/*     */       } 
/* 437 */     } else if (mode.equalsIgnoreCase("Novoline Old")) {
/* 438 */       if (target == null)
/*     */         return; 
/* 440 */       if (target.getHealth() < 0.0F)
/*     */         return; 
/* 442 */       float x = getX(), y = getY();
/* 443 */       float healthWid = target.getHealth() / target.getMaxHealth() * (40 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()));
/* 444 */       healthWid = (float)MathHelper.clamp(healthWid, 0.0D, 124.0D);
/* 445 */       this.healthBarWidth = AnimationHelper.calculateCompensation(healthWid, this.healthBarWidth, 0L, 0.0D);
/* 446 */       this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0F, 124.0F);
/* 447 */       RectHelper.drawRectBetter(x, y, (65 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 25), 40.0D, (new Color(19, 19, 19, 255)).getRGB());
/* 448 */       RectHelper.drawRectBetter((x + 1.0F), (y + 1.0F), (65 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 23), 38.0D, (new Color(41, 41, 41, 255)).getRGB());
/* 449 */       if (!target.getName().isEmpty()) {
/* 450 */         mc.fontRendererObj.drawStringWithShadow((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName(), x + 42.0F, y + 5.0F, -1);
/*     */       }
/* 452 */       GlStateManager.pushMatrix();
/* 453 */       GlStateManager.translate(x, y, 1.0F);
/* 454 */       GlStateManager.scale(1.05F, 1.05F, 1.05F);
/* 455 */       GlStateManager.translate(-x + 24.0F, -y + 18.0F, 1.0F);
/* 456 */       String hp = MathematicHelper.round(target.getHealth() / 2.0F, 1) + "";
/* 457 */       mc.fontRendererObj.drawStringWithShadow(hp, x + 17.0F, y + 10.0F, -1);
/* 458 */       mc.fontRendererObj.drawStringWithShadow(" ❤", x + mc.fontRendererObj.getStringWidth(hp) + 16.0F, y + 10.0F, (new Color(color)).getRGB());
/* 459 */       GlStateManager.popMatrix();
/* 460 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 462 */       GlStateManager.pushMatrix();
/* 463 */       GlStateManager.translate(x, y, 1.0F);
/* 464 */       GlStateManager.scale(0.8F, 0.8F, 0.8F);
/* 465 */       GlStateManager.translate(-x + 148.0F, -y + 38.0F, 1.0F);
/* 466 */       boolean stack = (target.getHeldItemOffhand().isStackable() && !target.getHeldItemOffhand().isEmpty());
/* 467 */       if (stack) {
/* 468 */         mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 39.0F + mc.fontRenderer.getStringWidth(hp) - 21.0F), (int)(y - 8.0F));
/* 469 */         mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 49.0F + mc.fontRenderer.getStringWidth(hp) - 21.0F), ((int)y - 5));
/*     */       } 
/* 471 */       GlStateManager.popMatrix();
/* 472 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 473 */       for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
/* 474 */         if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
/* 475 */           mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
/* 476 */           float hurtPercent = getHurtPercent(target);
/* 477 */           GL11.glPushMatrix();
/* 478 */           GL11.glColor4f(1.0F, 1.0F - hurtPercent, 1.0F - hurtPercent, 1.0F);
/* 479 */           Gui.drawScaledCustomSizeModalRect(((int)x + 1), ((int)y + 1), 8.0F, 8.0F, 8.0F, 8.0F, 38.0F, 38.0F, 64.0F, 64.0F);
/* 480 */           GL11.glPopMatrix();
/* 481 */           GlStateManager.bindTexture(0);
/*     */         } 
/* 483 */         GL11.glDisable(3089);
/*     */       } 
/* 485 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 9.0F), (40 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName())), 8.0D, (new Color(35, 35, 35, 255)).getRGB());
/* 486 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 9.0F), (target.getHealth() > 18.0F) ? healthWid : (this.healthBarWidth + 4.0F), 8.0D, (new Color(color)).darker().getRGB());
/* 487 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 9.0F), healthWid, 8.0D, (new Color(color)).getRGB());
/* 488 */     } else if (mode.equalsIgnoreCase("Novoline New")) {
/* 489 */       if (target == null)
/*     */         return; 
/* 491 */       if (target.getHealth() < 0.0F)
/*     */         return; 
/* 493 */       float x = getX(), y = getY();
/*     */ 
/*     */       
/* 496 */       float healthWid = target.getHealth() / target.getMaxHealth() * (40 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()));
/* 497 */       healthWid = (float)MathHelper.clamp(healthWid, 0.0D, 124.0D);
/* 498 */       this.healthBarWidth = AnimationHelper.calculateCompensation(healthWid, this.healthBarWidth, 0L, 0.0D);
/* 499 */       this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0F, 124.0F);
/* 500 */       RectHelper.drawRectBetter(x, y, (65 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 25), 40.0D, (new Color(19, 19, 19, 255)).getRGB());
/* 501 */       RectHelper.drawRectBetter((x + 1.0F), (y + 1.0F), (65 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 23), 38.0D, (new Color(41, 41, 41, 255)).getRGB());
/* 502 */       if (!target.getName().isEmpty()) {
/* 503 */         mc.fontRendererObj.drawStringWithShadow((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName(), x + 42.0F, y + 5.0F, -1);
/*     */       }
/* 505 */       GlStateManager.pushMatrix();
/* 506 */       GlStateManager.translate(x, y, 1.0F);
/* 507 */       GlStateManager.scale(1.05F, 1.05F, 1.05F);
/* 508 */       GlStateManager.translate(-x + 24.0F, -y + 18.0F, 1.0F);
/* 509 */       GlStateManager.popMatrix();
/* 510 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 512 */       GlStateManager.pushMatrix();
/* 513 */       GlStateManager.translate(x, y, 1.0F);
/* 514 */       GlStateManager.scale(0.8F, 0.8F, 0.8F);
/* 515 */       GlStateManager.translate(-x + 148.0F, -y + 38.0F, 1.0F);
/* 516 */       boolean stack = (target.getHeldItemOffhand().isStackable() && !target.getHeldItemOffhand().isEmpty());
/* 517 */       if (stack) {
/* 518 */         mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 88.0F), (int)(y - 8.0F));
/* 519 */         mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 98.0F), ((int)y - 5));
/*     */       } 
/* 521 */       GlStateManager.popMatrix();
/* 522 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 523 */       for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
/* 524 */         if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
/* 525 */           mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
/* 526 */           float hurtPercent = getHurtPercent(target);
/* 527 */           GL11.glPushMatrix();
/* 528 */           GL11.glColor4f(1.0F, 1.0F - hurtPercent, 1.0F - hurtPercent, 1.0F);
/* 529 */           Gui.drawScaledCustomSizeModalRect(((int)x + 1), ((int)y + 1), 8.0F, 8.0F, 8.0F, 8.0F, 38.0F, 38.0F, 64.0F, 64.0F);
/* 530 */           GL11.glPopMatrix();
/* 531 */           GlStateManager.bindTexture(0);
/*     */         } 
/* 533 */         GL11.glDisable(3089);
/*     */       } 
/* 535 */       String hp = MathematicHelper.round(target.getHealth() / target.getMaxHealth() * 100.0F, 1) + "%";
/* 536 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 9.0F), (40 + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName())), 10.0D, (new Color(35, 35, 35, 255)).getRGB());
/* 537 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 9.0F), (target.getHealth() > 18.0F) ? healthWid : (this.healthBarWidth + 4.0F), 10.0D, (new Color(color)).darker().getRGB());
/* 538 */       RectHelper.drawRectBetter((x + 42.0F), (y + mc.fontRendererObj.getStringHeight((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) + 9.0F), healthWid, 10.0D, (new Color(color)).getRGB());
/* 539 */       mc.fontRendererObj.drawStringWithShadow(hp, x + mc.fontRendererObj.getStringWidth((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName()) / 2.0F + 50.0F, y + 19.0F, -1);
/* 540 */     } else if (mode.equalsIgnoreCase("dfgopkododfg")) {
/* 541 */       if (target == null)
/*     */         return; 
/* 543 */       float x = getX(), y = getY();
/* 544 */       GlStateManager.pushMatrix();
/* 545 */       RectHelper.drawOutlineRect(x - 2.0F, y - 7.0F, 155.0F, 38.0F, new Color(20, 20, 20, 255), new Color(255, 255, 255, 255));
/* 546 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 547 */       mc.fontRendererObj.drawStringWithShadow(target.getName(), (getX() + 37), (getY() - 2), -1);
/* 548 */       for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
/* 549 */         if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
/* 550 */           mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
/* 551 */           Gui.drawScaledCustomSizeModalRect((int)x, ((int)y - 5), 8.0F, 8.0F, 8.0F, 8.0F, 34.0F, 34.0F, 64.0F, 64.0F);
/* 552 */           GlStateManager.bindTexture(0);
/*     */         } 
/* 554 */         GL11.glDisable(3089);
/*     */       } 
/* 556 */       mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
/* 557 */       int i = 0;
/* 558 */       while (i < target.getMaxHealth() / 2.0F) {
/* 559 */         mc.ingameGUI.drawTexturedModalRect((getX() + 86) - target.getMaxHealth() / 2.0F * 10.0F / 2.0F + (i * 8), (getY() + 9), 16, 0, 9, 9);
/* 560 */         i++;
/*     */       } 
/* 562 */       i = 0;
/* 563 */       while (i < target.getHealth() / 2.0F) {
/* 564 */         mc.ingameGUI.drawTexturedModalRect((getX() + 86) - target.getMaxHealth() / 2.0F * 10.0F / 2.0F + (i * 8), (getY() + 9), 52, 0, 9, 9);
/* 565 */         i++;
/*     */       } 
/*     */       
/* 568 */       int i3 = target.getTotalArmorValue();
/* 569 */       for (int k3 = 0; k3 < 10; k3++) {
/* 570 */         if (i3 > 0) {
/* 571 */           int l3 = getX() + 36 + k3 * 8;
/* 572 */           if (k3 * 2 + 1 < i3) {
/* 573 */             mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 34, 9, 9, 9);
/*     */           }
/*     */           
/* 576 */           if (k3 * 2 + 1 == i3) {
/* 577 */             mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 25, 9, 9, 9);
/*     */           }
/*     */           
/* 580 */           if (k3 * 2 + 1 > i3) {
/* 581 */             mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 16, 9, 9, 9);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 586 */       GlStateManager.popMatrix();
/* 587 */     } else if (mode.equalsIgnoreCase("Flux")) {
/* 588 */       float x = getX(), y = getY();
/*     */       
/* 590 */       double armorWid = (target.getTotalArmorValue() * 6);
/* 591 */       double healthWid = (target.getHealth() / target.getMaxHealth() * 120.0F);
/* 592 */       healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
/*     */       
/* 594 */       RectHelper.drawRectBetter(x, y, 125.0D, 55.0D, (new Color(39, 39, 37, 235)).getRGB());
/*     */       
/* 596 */       String pvpState = "";
/* 597 */       if (mc.player.getHealth() == target.getHealth()) {
/* 598 */         pvpState = "Finish Him!";
/* 599 */       } else if (mc.player.getHealth() < target.getHealth()) {
/* 600 */         pvpState = "Losing Fight";
/* 601 */       } else if (mc.player.getHealth() > target.getHealth()) {
/* 602 */         pvpState = "Winning Fight";
/*     */       } 
/* 604 */       if (!target.getName().isEmpty()) {
/* 605 */         mc.robotoRegularFontRender.drawStringWithShadow((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) ? "Protected" : target.getName(), (x + 38.0F), (y + 6.0F), -1);
/* 606 */         mc.clickguismall.drawStringWithShadow(pvpState, (x + 38.0F), (y + 17.0F), -1);
/*     */       } 
/*     */       
/* 609 */       for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
/* 610 */         mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
/* 611 */         float hurtPercent = getHurtPercent(target);
/* 612 */         GL11.glPushMatrix();
/* 613 */         GL11.glColor4f(1.0F, 1.0F - hurtPercent, 1.0F - hurtPercent, 1.0F);
/* 614 */         Gui.drawScaledCustomSizeModalRect(x + 1.5F, y + 1.5F, 8.0F, 8.0F, 8.0F, 8.0F, 34.0F, 34.0F, 64.0F, 64.0F);
/* 615 */         GL11.glPopMatrix();
/*     */       } 
/*     */ 
/*     */       
/* 619 */       RectHelper.drawRectBetter((x + 1.5F), (y + 39.0F), 120.0D, 4.0D, (new Color(26, 28, 25, 255)).getRGB());
/* 620 */       RectHelper.drawRectBetter((x + 1.5F), (y + 39.0F), healthWid, 4.0D, (new Color(2, 145, 98, 255)).getRGB());
/*     */ 
/*     */       
/* 623 */       RectHelper.drawRectBetter((x + 1.5F), (y + 47.0F), 120.0D, 4.0D, (new Color(26, 28, 25, 255)).getRGB());
/* 624 */       RectHelper.drawRectBetter((x + 1.5F), (y + 47.0F), armorWid, 4.0D, (new Color(65, 138, 195, 255)).getRGB());
/*     */     } 
/* 626 */     super.draw();
/*     */   }
/*     */   
/*     */   public static float getRenderHurtTime(EntityLivingBase hurt) {
/* 630 */     return hurt.hurtTime - ((hurt.hurtTime != 0) ? mc.timer.renderPartialTicks : 0.0F);
/*     */   }
/*     */   
/*     */   public static float getHurtPercent(EntityLivingBase hurt) {
/* 634 */     return getRenderHurtTime(hurt) / 10.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\draggable\impl\TargetHUDComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */