/*     */ package org.neverhook.client.feature.impl.visual;
/*     */ 
/*     */ import com.mojang.realmsclient.gui.ChatFormatting;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.RenderItem;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import optifine.CustomColors;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.feature.impl.misc.StreamerMode;
/*     */ import org.neverhook.client.helpers.math.MathematicHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class NameTags
/*     */   extends Feature {
/*     */   public BooleanSetting armor;
/*     */   public BooleanSetting backGround;
/*     */   public NumberSetting opacity;
/*     */   public NumberSetting size;
/*  41 */   public ListSetting hpMode = new ListSetting("Health Mode", "HP", () -> Boolean.valueOf(true), new String[] { "HP", "Percentage" });
/*     */   
/*     */   public NameTags() {
/*  44 */     super("NameTags", "Показывает игроков, ник, броню и их здоровье сквозь стены", Type.Visuals);
/*  45 */     this.size = new NumberSetting("NameTags Size", 0.5F, 0.2F, 2.0F, 0.01F, () -> Boolean.valueOf(true));
/*  46 */     this.backGround = new BooleanSetting("NameTags Background", true, () -> Boolean.valueOf(true));
/*  47 */     this.opacity = new NumberSetting("Background Opacity", 120.0F, 0.0F, 255.0F, 10.0F, () -> Boolean.valueOf(this.backGround.getBoolValue()));
/*  48 */     this.armor = new BooleanSetting("Show Armor", true, () -> Boolean.valueOf(true));
/*  49 */     addSettings(new Setting[] { (Setting)this.hpMode, (Setting)this.size, (Setting)this.backGround, (Setting)this.opacity, (Setting)this.armor });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender3d(EventRender3D event) {
/*  54 */     for (EntityPlayer entity : mc.world.playerEntities) {
/*  55 */       if (entity != null) {
/*  56 */         String hp; double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - (mc.getRenderManager()).renderPosX;
/*  57 */         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - (mc.getRenderManager()).renderPosY;
/*  58 */         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - (mc.getRenderManager()).renderPosZ;
/*     */ 
/*     */         
/*  61 */         if (NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) {
/*  62 */           tag = "Protected";
/*  63 */         } else if (NeverHook.instance.friendManager.isFriend(entity.getName())) {
/*  64 */           tag = ChatFormatting.GREEN + "[F] " + ChatFormatting.RESET + entity.getDisplayName().getUnformattedText();
/*     */         } else {
/*  66 */           tag = entity.getDisplayName().getUnformattedText();
/*     */         } 
/*     */         
/*  69 */         y += (entity.isSneaking() ? 0.5F : 0.7F);
/*  70 */         float distance = Math.min(Math.max(1.2F * mc.player.getDistanceToEntity((Entity)entity) * 0.15F, 1.25F), 6.0F) * 0.015F;
/*  71 */         int health = (int)entity.getHealth();
/*  72 */         if (health <= entity.getMaxHealth() * 0.25F) {
/*  73 */           tag = tag + "§4";
/*  74 */         } else if (health <= entity.getMaxHealth() * 0.5F) {
/*  75 */           tag = tag + "§6";
/*  76 */         } else if (health <= entity.getMaxHealth() * 0.75F) {
/*  77 */           tag = tag + "§e";
/*  78 */         } else if (health <= entity.getMaxHealth()) {
/*  79 */           tag = tag + "§2";
/*     */         } 
/*     */ 
/*     */         
/*  83 */         if (this.hpMode.currentMode.equals("Percentage")) {
/*  84 */           hp = MathematicHelper.round(entity.getHealth() / entity.getMaxHealth() * 100.0F, 1) + "% ";
/*     */         } else {
/*  86 */           hp = MathematicHelper.round(entity.getHealth(), 1) + " ";
/*     */         } 
/*     */         
/*  89 */         String tag = tag + " " + hp;
/*     */         
/*  91 */         float scale = distance;
/*  92 */         scale *= this.size.getNumberValue();
/*  93 */         GL11.glPushMatrix();
/*  94 */         GL11.glTranslatef((float)x, (float)y + 1.4F, (float)z);
/*  95 */         GL11.glNormal3f(1.0F, 1.0F, 1.0F);
/*  96 */         float fixed = (mc.gameSettings.thirdPersonView == 2) ? -1.0F : 1.0F;
/*  97 */         GL11.glRotatef(-(mc.getRenderManager()).playerViewY, 0.0F, 1.0F, 0.0F);
/*  98 */         GL11.glRotatef((mc.getRenderManager()).playerViewX, fixed, 0.0F, 0.0F);
/*  99 */         GL11.glScalef(-scale, -scale, scale);
/* 100 */         GL11.glDisable(2929);
/* 101 */         int width = mc.fontRendererObj.getStringWidth(tag) / 2;
/* 102 */         GL11.glEnable(3042);
/* 103 */         GL11.glBlendFunc(770, 771);
/*     */         
/* 105 */         if (this.backGround.getBoolValue()) {
/* 106 */           RectHelper.drawRect((-width - 2), -(mc.fontRendererObj.FONT_HEIGHT + 1), (width + 2), 2.0D, PaletteHelper.getColor(0, (int)this.opacity.getNumberValue()));
/*     */         }
/*     */         
/* 109 */         mc.fontRendererObj.drawStringWithShadow(tag, (MathematicHelper.getMiddle(-width - 2, width + 2) - width), -(mc.fontRendererObj.FONT_HEIGHT - 1), Color.WHITE.getRGB());
/* 110 */         if (this.armor.getBoolValue()) {
/* 111 */           renderArmor(entity, 0, -(mc.fontRendererObj.FONT_HEIGHT + 1) - 20);
/*     */         }
/*     */         
/* 114 */         float yPotion = (float)(y - 45.0D);
/* 115 */         for (PotionEffect effectPotion : entity.getActivePotionEffects()) {
/* 116 */           GL11.glDisable(2929);
/* 117 */           Potion effect = Potion.getPotionById(CustomColors.getPotionId(effectPotion.getEffectName()));
/* 118 */           if (effect != null) {
/*     */             
/* 120 */             ChatFormatting getPotionColor = null;
/* 121 */             if (effectPotion.getDuration() < 200) {
/* 122 */               getPotionColor = ChatFormatting.RED;
/* 123 */             } else if (effectPotion.getDuration() < 400) {
/* 124 */               getPotionColor = ChatFormatting.GOLD;
/* 125 */             } else if (effectPotion.getDuration() > 400) {
/* 126 */               getPotionColor = ChatFormatting.GRAY;
/*     */             } 
/*     */             
/* 129 */             String durationString = Potion.getDurationString(effectPotion);
/*     */             
/* 131 */             String level = I18n.format(effect.getName(), new Object[0]);
/* 132 */             if (effectPotion.getAmplifier() == 1) {
/* 133 */               level = level + " " + ChatFormatting.GRAY + I18n.format("enchantment.level.2", new Object[0]) + " (" + getPotionColor + durationString + ")";
/* 134 */             } else if (effectPotion.getAmplifier() == 2) {
/* 135 */               level = level + " " + ChatFormatting.GRAY + I18n.format("enchantment.level.3", new Object[0]) + " (" + getPotionColor + durationString + ")";
/* 136 */             } else if (effectPotion.getAmplifier() == 3) {
/* 137 */               level = level + " " + ChatFormatting.GRAY + I18n.format("enchantment.level.4", new Object[0]) + " (" + getPotionColor + durationString + ")";
/*     */             } 
/*     */             
/* 140 */             mc.fontRendererObj.drawStringWithShadow(level, (MathematicHelper.getMiddle(-width - 2, width + 2) - width), yPotion, effectPotion.getPotion().getLiquidColor());
/*     */           } 
/* 142 */           yPotion -= 10.0F;
/* 143 */           GL11.glEnable(2929);
/*     */         } 
/*     */         
/* 146 */         GL11.glEnable(2929);
/* 147 */         GL11.glDisable(3042);
/* 148 */         GL11.glPopMatrix();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderArmor(EntityPlayer player, int x, int y) {
/* 154 */     InventoryPlayer items = player.inventory;
/* 155 */     ItemStack offhand = player.getHeldItemOffhand();
/* 156 */     ItemStack inHand = player.getHeldItemMainhand();
/* 157 */     ItemStack boots = items.armorItemInSlot(0);
/* 158 */     ItemStack leggings = items.armorItemInSlot(1);
/* 159 */     ItemStack body = items.armorItemInSlot(2);
/* 160 */     ItemStack helm = items.armorItemInSlot(3);
/*     */     
/* 162 */     ItemStack[] stuff = { offhand, inHand, helm, body, leggings, boots };
/* 163 */     ArrayList<ItemStack> stacks = new ArrayList<>();
/*     */     ItemStack[] array;
/* 165 */     int length = (array = stuff).length;
/*     */     
/* 167 */     for (int j = 0; j < length; j++) {
/* 168 */       ItemStack i = array[j];
/* 169 */       if (i != null) {
/* 170 */         i.getItem();
/* 171 */         stacks.add(i);
/*     */       } 
/*     */     } 
/* 174 */     int width = 18 * stacks.size() / 2;
/* 175 */     x -= width;
/* 176 */     GlStateManager.disableDepth();
/* 177 */     for (ItemStack stack : stacks) {
/* 178 */       renderItem(player, stack, x, y);
/* 179 */       x += 18;
/*     */     } 
/* 181 */     GlStateManager.enableDepth();
/*     */   }
/*     */   
/*     */   public void renderItem(EntityPlayer e, ItemStack stack, int x, int y) {
/* 185 */     if (stack != null) {
/* 186 */       RenderItem renderItem = mc.getRenderItem();
/* 187 */       GlStateManager.pushMatrix();
/* 188 */       GlStateManager.translate((x - 3), (y + 10), 0.0F);
/* 189 */       GlStateManager.popMatrix();
/*     */       
/* 191 */       renderItem.zLevel = -100.0F;
/* 192 */       GlStateManager.enableBlend();
/* 193 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 194 */       GL11.glEnable(3008);
/* 195 */       RenderHelper.enableGUIStandardItemLighting();
/* 196 */       GL11.glDisable(2929);
/* 197 */       renderItem.renderItemIntoGUI(stack, x, y);
/* 198 */       renderItem.renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y + 2, null);
/* 199 */       RenderHelper.disableStandardItemLighting();
/* 200 */       GL11.glEnable(2929);
/* 201 */       renderItem.zLevel = 0.0F;
/*     */       
/* 203 */       GlStateManager.pushMatrix();
/* 204 */       GL11.glEnable(3008);
/* 205 */       GL11.glDisable(2929);
/*     */       
/* 207 */       renderEnchant(stack, (x + 2), y - 18);
/* 208 */       GL11.glEnable(2929);
/* 209 */       RenderHelper.disableStandardItemLighting();
/*     */       
/* 211 */       GlStateManager.enableAlpha();
/* 212 */       GlStateManager.disableBlend();
/* 213 */       GlStateManager.disableLighting();
/* 214 */       GlStateManager.popMatrix();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderEnchant(ItemStack item, float x, int y) {
/* 219 */     int encY = y + 5;
/* 220 */     Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
/* 221 */     for (Enchantment enchantment : enchantments.keySet()) {
/* 222 */       int level = EnchantmentHelper.getEnchantmentLevel(enchantment, item);
/* 223 */       mc.fontRendererObj.drawStringWithShadow(String.valueOf(enchantment.getName().substring(12).charAt(0)).toUpperCase() + level, x, encY, 16777215);
/* 224 */       encY -= 12;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\NameTags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */