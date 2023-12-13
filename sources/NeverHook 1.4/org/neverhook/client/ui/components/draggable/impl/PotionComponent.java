/*     */ package org.neverhook.client.ui.components.draggable.impl;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import optifine.CustomColors;
/*     */ import org.neverhook.client.feature.impl.hud.HUD;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.ui.components.draggable.DraggableModule;
/*     */ import org.neverhook.client.ui.font.MCFontRenderer;
/*     */ 
/*     */ public class PotionComponent extends DraggableModule {
/*  21 */   protected Gui gui = new Gui();
/*     */   
/*     */   public PotionComponent() {
/*  24 */     super("PotionComponent", 2, 320);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  29 */     return 100;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  34 */     return 150;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(int mouseX, int mouseY) {
/*  39 */     int xOff = 21;
/*  40 */     int yOff = 14;
/*  41 */     int counter = 16;
/*     */     
/*  43 */     Collection<PotionEffect> collection = mc.player.getActivePotionEffects();
/*     */     
/*  45 */     if (collection.isEmpty()) {
/*  46 */       this.drag.setCanRender(false);
/*     */     } else {
/*  48 */       this.drag.setCanRender(true);
/*  49 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  50 */       GlStateManager.disableLighting();
/*  51 */       int listOffset = 23;
/*  52 */       if (collection.size() > 5) {
/*  53 */         listOffset = 132 / (collection.size() - 1);
/*     */       }
/*  55 */       List<PotionEffect> potions = new ArrayList<>(mc.player.getActivePotionEffects());
/*  56 */       potions.sort(Comparator.comparingDouble(effect -> mc.fontRendererObj.getStringWidth(((Potion)Objects.<Potion>requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName())))).getName())));
/*     */       
/*  58 */       for (PotionEffect potion : potions) {
/*  59 */         Potion effect = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
/*  60 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */         
/*  62 */         assert effect != null;
/*  63 */         if (effect.hasStatusIcon() && HUD.potionIcons.getBoolValue()) {
/*  64 */           mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
/*  65 */           int statusIconIndex = effect.getStatusIconIndex();
/*  66 */           this.gui.drawTexturedModalRect((getX() + xOff - 20), (getY() + counter - yOff), statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
/*     */         } 
/*     */         
/*  69 */         String level = I18n.format(effect.getName(), new Object[0]);
/*  70 */         if (potion.getAmplifier() == 1) {
/*  71 */           level = level + " " + I18n.format("enchantment.level.2", new Object[0]);
/*  72 */         } else if (potion.getAmplifier() == 2) {
/*  73 */           level = level + " " + I18n.format("enchantment.level.3", new Object[0]);
/*  74 */         } else if (potion.getAmplifier() == 3) {
/*  75 */           level = level + " " + I18n.format("enchantment.level.4", new Object[0]);
/*     */         } 
/*     */         
/*  78 */         int getPotionColor = -1;
/*  79 */         if (potion.getDuration() < 200) {
/*  80 */           getPotionColor = (new Color(215, 59, 59)).getRGB();
/*  81 */         } else if (potion.getDuration() < 400) {
/*  82 */           getPotionColor = (new Color(231, 143, 32)).getRGB();
/*  83 */         } else if (potion.getDuration() > 400) {
/*  84 */           getPotionColor = (new Color(172, 171, 171)).getRGB();
/*     */         } 
/*     */         
/*  87 */         String durationString = Potion.getDurationString(potion);
/*     */         
/*  89 */         if (HUD.font.currentMode.equals("Minecraft")) {
/*  90 */           MCFontRenderer.drawStringWithOutline(mc.fontRendererObj, level, (getX() + xOff), (getY() + counter - yOff), -1);
/*  91 */           MCFontRenderer.drawStringWithOutline(mc.fontRendererObj, durationString, (getX() + xOff), (getY() + counter + 10 - yOff), HUD.potionTimeColor.getBoolValue() ? getPotionColor : -1);
/*     */         } else {
/*  93 */           ClientHelper.getFontRender().drawStringWithOutline(level, (getX() + xOff), (getY() + counter - yOff), -1);
/*  94 */           ClientHelper.getFontRender().drawStringWithOutline(durationString, (getX() + xOff), (getY() + counter + 10 - yOff), HUD.potionTimeColor.getBoolValue() ? getPotionColor : -1);
/*     */         } 
/*  96 */         counter += listOffset;
/*     */       } 
/*  98 */       super.draw();
/*     */     } 
/* 100 */     super.render(mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw() {
/* 105 */     if (HUD.potion.getBoolValue()) {
/* 106 */       int xOff = 21;
/* 107 */       int yOff = 14;
/* 108 */       int counter = 16;
/*     */       
/* 110 */       Collection<PotionEffect> collection = mc.player.getActivePotionEffects();
/*     */       
/* 112 */       if (collection.isEmpty()) {
/* 113 */         this.drag.setCanRender(false);
/*     */       } else {
/* 115 */         this.drag.setCanRender(true);
/* 116 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 117 */         GlStateManager.disableLighting();
/* 118 */         int listOffset = 23;
/* 119 */         if (collection.size() > 5) {
/* 120 */           listOffset = 132 / (collection.size() - 1);
/*     */         }
/* 122 */         List<PotionEffect> potions = new ArrayList<>(mc.player.getActivePotionEffects());
/* 123 */         potions.sort(Comparator.comparingDouble(effect -> mc.fontRendererObj.getStringWidth(((Potion)Objects.<Potion>requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName())))).getName())));
/*     */         
/* 125 */         for (PotionEffect potion : potions) {
/* 126 */           Potion effect = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
/* 127 */           GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */           
/* 129 */           assert effect != null;
/* 130 */           if (effect.hasStatusIcon() && HUD.potionIcons.getBoolValue()) {
/* 131 */             mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
/* 132 */             int statusIconIndex = effect.getStatusIconIndex();
/* 133 */             this.gui.drawTexturedModalRect((getX() + xOff - 20), (getY() + counter - yOff), statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
/*     */           } 
/*     */           
/* 136 */           String level = I18n.format(effect.getName(), new Object[0]);
/* 137 */           if (potion.getAmplifier() == 1) {
/* 138 */             level = level + " " + I18n.format("enchantment.level.2", new Object[0]);
/* 139 */           } else if (potion.getAmplifier() == 2) {
/* 140 */             level = level + " " + I18n.format("enchantment.level.3", new Object[0]);
/* 141 */           } else if (potion.getAmplifier() == 3) {
/* 142 */             level = level + " " + I18n.format("enchantment.level.4", new Object[0]);
/*     */           } 
/*     */           
/* 145 */           int getPotionColor = -1;
/* 146 */           if (potion.getDuration() < 200) {
/* 147 */             getPotionColor = (new Color(215, 59, 59)).getRGB();
/* 148 */           } else if (potion.getDuration() < 400) {
/* 149 */             getPotionColor = (new Color(231, 143, 32)).getRGB();
/* 150 */           } else if (potion.getDuration() > 400) {
/* 151 */             getPotionColor = (new Color(172, 171, 171)).getRGB();
/*     */           } 
/*     */           
/* 154 */           String durationString = Potion.getDurationString(potion);
/*     */           
/* 156 */           if (HUD.font.currentMode.equals("Minecraft")) {
/* 157 */             MCFontRenderer.drawStringWithOutline(mc.fontRendererObj, level, (getX() + xOff), (getY() + counter - yOff), -1);
/* 158 */             MCFontRenderer.drawStringWithOutline(mc.fontRendererObj, durationString, (getX() + xOff), (getY() + counter + 10 - yOff), HUD.potionTimeColor.getBoolValue() ? getPotionColor : -1);
/*     */           } else {
/* 160 */             ClientHelper.getFontRender().drawStringWithOutline(level, (getX() + xOff), (getY() + counter - yOff), -1);
/* 161 */             ClientHelper.getFontRender().drawStringWithOutline(durationString, (getX() + xOff), (getY() + counter + 10 - yOff), HUD.potionTimeColor.getBoolValue() ? getPotionColor : -1);
/*     */           } 
/* 163 */           counter += listOffset;
/*     */         } 
/*     */       } 
/* 166 */       super.draw();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\draggable\impl\PotionComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */