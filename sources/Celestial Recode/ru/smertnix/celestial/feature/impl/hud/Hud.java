package ru.smertnix.celestial.feature.impl.hud;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import optifine.CustomColors;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.draggable.component.impl.*;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.player.NameProtect;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.math.AnimationHelper;
import ru.smertnix.celestial.utils.math.MathematicHelper;
import ru.smertnix.celestial.utils.math.TPSUtils;
import ru.smertnix.celestial.utils.movement.MovementUtils;
import ru.smertnix.celestial.utils.other.NameUtils;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.ColorUtils;
import ru.smertnix.celestial.utils.render.GLUtils;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;
import ru.smertnix.celestial.utils.render.TextureEngine;

import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Hud extends Feature {
    public static BooleanSetting waterMark = new BooleanSetting("Armor HUD", true, () -> true);
    public static BooleanSetting coords = new BooleanSetting("Cords", true, () -> true);
    
        public static BooleanSetting user_info = new BooleanSetting("User Info", true, () -> true);

    public static BooleanSetting potions = new BooleanSetting("Potion HUD", true, () -> true);
    private double cooldownBarWidth;
    private double hurttimeBarWidth;
    public float scale = 2;

    public Hud() {
        super("HUD", "Худ клиента", FeatureCategory.Render);
        addSettings(waterMark,  potions, coords);
    }

    @EventTarget
    public void onRender(EventRender2D eventRender2D) {	

  	  if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
                DraggableWaterMark dwm = (DraggableWaterMark) Celestial.instance.draggableHUD.getDraggableComponentByClass(DraggableWaterMark.class);
                dwm.setWidth(100);
                dwm.setHeight(35);
                String server;
                if (this.mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    assert (this.mc.getCurrentServerData() != null);
                    server = this.mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                String name = mc.getSession().getUsername();
                String fps = "FPS: " + Minecraft.getDebugFPS();
                String text = name + "     " + server + "     " + fps;
                ScaledResolution sr = new ScaledResolution(this.mc);
                
                boolean reverse = dwm.getX() > (float)(sr.getScaledWidth() / 2);

                if (!reverse) {
                	RenderUtils.drawShadow(10, 1, () -> {
                    	RoundedUtil.drawGradientRound(dwm.getX() + 10, dwm.getY() - 3, 70 * 1.1f, 20 * 1.1f, 2.5f * 1.1f, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor().darker(), new Color(-1), new Color(-1));

                    	 RoundedUtil.drawGradientRound(dwm.getX() + 1, dwm.getY() - 4, 22 * 1.1f, 21 * 1.1f, 11 * 1.1f, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor().darker(), new Color(-1), new Color(-1));
                    });
                    
                    
                    RoundedUtil.drawGradientRound(dwm.getX() + 10, dwm.getY() - 3, 70 * 1.1f, 20 * 1.1f, 2.5f * 1.1f, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor().darker(), new Color(-1), new Color(-1));

                    
                    RoundedUtil.drawGradientRound(dwm.getX(), dwm.getY() - 5, 23 * 1.1f, 23 * 1.1f, 11 * 1.1f, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor().darker(), new Color(-1), new Color(-1));
                	
                	
                    	


                    mc.mntsb_18.drawStringWithShadow(" ", dwm.getX() + 25 * 1.1f, (float) dwm.getY() , -1);
                    
                    RenderUtils.drawImage(new ResourceLocation("celestial/images/atomwhite.png"), dwm.getX() + (3 - 2 + 0.5f) * 1.1f, dwm.getY() + (-2 + 0.5f - 2) * 1.1f, (17 + 3.5f) * 1.1f, (17 + 3.5f) * 1.1f, new Color(0));

                    RenderUtils.drawImage(new ResourceLocation("celestial/images/atomwhite.png"), dwm.getX() + (3 - 2 + 0.5f) * 1.1f,dwm.getY() + (-2 + 0.5f - 2) * 1.1f, (17 + 3) * 1.1f, (17 + 3) * 1.1f, ClientHelper.getClientColor().darker());
                	mc.mntsb_18.drawStringWithShadow(" ", dwm.getX() + 25 * 1.1f, (float) dwm.getY() , -1);
                    
                    
                    
                    
                    	//RenderUtils.drawRect(dwm.getX(), dwm.getY() - 1, (float) dwm.getX() + width + 2.0f, dwm.getY() + 12, new Color(5, 5, 5, 145).getRGB());
                    //RenderUtils.drawGradientSideways(dwm.getX(), (float) dwm.getY() - 1.1f, (float) dwm.getX() + width + 2.0f, (float) dwm.getY() + 0.1f, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().darker().getRGB());
                   // RenderUtils.drawImage(new ResourceLocation("celestial/images/atom.png"), dwm.getX() + 3 - 2 + 0.5f, dwm.getY() - 2 + 0.5f - 2, 17 + 4, 17 + 4, new Color(0));

                    String celka = "C";
                    
                    if (mc.player.ticksExisted % 100 <= 10) {
                    	celka = "elestial C";
                    } else if (mc.player.ticksExisted % 100 <= 20) {
                    	celka = "lestial Ce";
                    } else if (mc.player.ticksExisted % 100 <= 30) {
                    	celka = "estial Cel";
                    } else if (mc.player.ticksExisted % 100 <= 40) {
                    	celka = "stial Cele";
                    } else if (mc.player.ticksExisted % 100 <= 50) {
                    	celka = "tial Celes";
                    } else if (mc.player.ticksExisted % 100 <= 60) {
                    	celka = "ial Celest";
                    } else if (mc.player.ticksExisted % 100 <= 70) {
                    	celka = "al Celesti";
                    } else if (mc.player.ticksExisted % 100 <= 80) {
                    	celka = "l Celestia";
                    } else if (mc.player.ticksExisted % 100 <= 90) {
                    	celka = " Celestial";
                    } else if (mc.player.ticksExisted % 100 <= 100) {
                    	celka = "Celestial ";
                    } else {
                    	celka = "Celestial";
                    }
                    
                    
                    	mc.mntsb_18.drawStringWithShadow("Minced", dwm.getX() + 25 * 1.1f, (float) dwm.getY() , -1);
                    
                    	
                    	
                    	mc.mntsb_16.drawStringWithShadow("UID: " + TextFormatting.GRAY + "1", dwm.getX() + 25 * 1.1f, (float) dwm.getY() + 10 * 1.1f, -1);
                        
                    	
                    	
                    	

                } else {
                	RenderUtils.drawShadow(10, 1, () -> {
                    	RoundedUtil.drawGradientRound(dwm.getX() + 10, dwm.getY() - 3, 70 * 1.1f, 20 * 1.1f, 2.5f * 1.1f, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor().darker(), new Color(-1), new Color(-1));

                    	 RoundedUtil.drawGradientRound(dwm.getX() + 1 + 70, dwm.getY() - 4, 22 * 1.1f, 21 * 1.1f, 11 * 1.1f, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor().darker(), new Color(-1), new Color(-1));
                    });
                    
                    
                    RoundedUtil.drawGradientRound(dwm.getX() + 10, dwm.getY() - 3, 70 * 1.1f, 20 * 1.1f, 2.5f * 1.1f, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor().darker(), new Color(-1), new Color(-1));

                    
                    RoundedUtil.drawGradientRound(dwm.getX() + 70, dwm.getY() - 5, 23 * 1.1f, 23 * 1.1f, 11 * 1.1f, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor().darker(), new Color(-1), new Color(-1));
                    
                    	
                    	//RenderUtils.drawRect(dwm.getX(), dwm.getY() - 1, (float) dwm.getX() + width + 2.0f, dwm.getY() + 12, new Color(5, 5, 5, 145).getRGB());
                    //RenderUtils.drawGradientSideways(dwm.getX(), (float) dwm.getY() - 1.1f, (float) dwm.getX() + width + 2.0f, (float) dwm.getY() + 0.1f, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().darker().getRGB());
                   // RenderUtils.drawImage(new ResourceLocation("celestial/images/atom.png"), dwm.getX() + 3 - 2 + 0.5f, dwm.getY() - 2 + 0.5f - 2, 17 + 4, 17 + 4, new Color(0));
                    RenderUtils.drawImage(new ResourceLocation("celestial/images/atomwhite.png"), dwm.getX() + 70 + (3 - 2 + 0.5f) * 1.1f, dwm.getY() + (-2 + 0.5f - 2) * 1.1f, (17 + 3.5f) * 1.1f, (17 + 3.5f) * 1.1f, new Color(0));

                    RenderUtils.drawImage(new ResourceLocation("celestial/images/atomwhite.png"), dwm.getX() + 70 + (3 - 2 + 0.5f) * 1.1f,dwm.getY() + (-2 + 0.5f - 2) * 1.1f, (17 + 3) * 1.1f, (17 + 3) * 1.1f, ClientHelper.getClientColor().darker());
                   
                   
                    mc.mntsb_18.drawStringWithShadow("Minced", dwm.getX() + 25 * 1.1f - 10, (float) dwm.getY() , -1);
                    mc.mntsb_16.drawStringWithShadow("UID: " + TextFormatting.GRAY + "1", dwm.getX() + 25 * 1.1f - 10, (float) dwm.getY() + 10 * 1.1f, -1);
            
                }

                if (mc.player != null && mc.currentScreen instanceof GuiChat) {
                TextureEngine texture = new TextureEngine("celestial/images/grab.png", Celestial.scale, 50,50);
                texture.bind(dwm.getX() - 68 / 2 + dwm.getWidth() / 2 + 10, dwm.getY() -  68/ 2 + dwm.getHeight() - 5);
                }
                
                
                
            	if (!(mc.currentScreen instanceof GuiChat)) {
            	mc.mntsb_15.drawString("BPS: " + TextFormatting.GRAY + String.format("%.2f", Float.valueOf("" + calculateBPS())), 2, sr.getScaledHeight() - ((coords.getBoolValue()) ? 15 : 7), -1);
            	}
            	
            	
            	mc.mntsb_15.drawString("FPS: " + TextFormatting.GRAY + mc.getDebugFPS(), sr.getScaledWidth() - 2 - mc.mntsb_15.getStringWidth("FPS: " + mc.getDebugFPS()), sr.getScaledHeight() - (mc.isSingleplayer() ? ((mc.currentScreen instanceof GuiChat) ? 19 : 7) : ((mc.currentScreen instanceof GuiChat) ? 28 : 15)), -1);
            	
            	if (!mc.isSingleplayer()) {
                	mc.mntsb_15.drawString("Ping: " + TextFormatting.GRAY + (int) mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime(), sr.getScaledWidth() - 2 - mc.mntsb_15.getStringWidth("Ping: " + TextFormatting.GRAY + (int) mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime()) - 10, sr.getScaledHeight() - ((mc.currentScreen instanceof GuiChat) ? 20 : 7), -1);
            	
                	NetworkPlayerInfo networkPlayerInfoIn = mc.player.connection.getPlayerInfo(mc.player.getUniqueID());
                	
                	
                	 GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                     this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
                     int i = 0;
                     int j;

                     if (networkPlayerInfoIn.getResponseTime() < 0) {
                         j = 5;
                     } else if (networkPlayerInfoIn.getResponseTime() < 150) {
                         j = 0;
                     } else if (networkPlayerInfoIn.getResponseTime() < 300) {
                         j = 1;
                     } else if (networkPlayerInfoIn.getResponseTime() < 600) {
                         j = 2;
                     } else if (networkPlayerInfoIn.getResponseTime() < 1000) {
                         j = 3;
                     } else {
                         j = 4;
                     }
                     float x = sr.getScaledWidth() - 11;
                     float y = sr.getScaledHeight() - 10 - ((mc.currentScreen instanceof GuiChat) ? 14 : 0);
                     
                     float f = 0.00390625F;
                     float f1 = 0.00390625F;
                     int zLevel = 0;
                     float width = 10;
                     float height = 8;
                     float textureX = 0;
                     float textureY = 176 + j * 8;
                     Tessellator tessellator = Tessellator.getInstance();
                     BufferBuilder bufferbuilder = tessellator.getBuffer();
                     bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                     bufferbuilder.pos((double)(x + 0), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
                     bufferbuilder.pos((double)(x + width), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
                     bufferbuilder.pos((double)(x + width), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
                     bufferbuilder.pos((double)(x + 0), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
                     tessellator.draw();
            	}
            	
        if (coords.getBoolValue()) {
        	if (!(mc.currentScreen instanceof GuiChat)) {

        	mc.mntsb_15.drawString("XYZ: " + TextFormatting.GRAY + (int) mc.player.posX + " " +  (int) mc.player.posY + " " +  (int) mc.player.posZ, 2, sr.getScaledHeight() - ((mc.currentScreen instanceof GuiChat) ? 20 : 7), -1);
    	}
        }
        if (waterMark.getBoolValue()) {
        	RenderItem itemRender;
        	itemRender = mc.getMinecraft().getRenderItem();
        	GlStateManager.enableTexture2D();
            ScaledResolution resolution = new ScaledResolution(mc);
            int i = resolution.getScaledWidth() / 2;
            int iteration = 0;
            int y = resolution.getScaledHeight() - 55 - (mc.player.isInsideOfMaterial(Material.WATER) ? 10 : 0 + (mc.player != null && mc.currentScreen instanceof GuiChat ? 14 : 0));
            
            GlStateManager.enableTexture2D();
            for (ItemStack is : mc.player.inventory.armorInventory) {
                ++iteration;
                if (is.func_190926_b()) continue;
                int x = i - 70 + (9 - iteration) * 16;
                GlStateManager.enableDepth();
                itemRender.zLevel = 200.0f;
                itemRender.renderItemAndEffectIntoGUI(is, x, y);
                itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, is, x, y, "");
                itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                String s = is.func_190916_E() > 1 ? is.func_190916_E() + "" : "";
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        if (potions.getBoolValue()) {
            DraggablePotionHUD dph = (DraggablePotionHUD) Celestial.instance.draggableHUD.getDraggableComponentByClass(DraggablePotionHUD.class);
            dph.setWidth(200);
            dph.setHeight(35);
            int xOff = 21;
            int yOff = 14;
            int counter = 16;

            Collection<PotionEffect> collection = mc.player.getActivePotionEffects();

            {
                GlStateManager.color(1F, 1F, 1F, 1F);
                GlStateManager.disableLighting();
                int listOffset = 27;
                List<PotionEffect> potions = new ArrayList<>(mc.player.getActivePotionEffects());
                potions.sort(Comparator.comparingDouble(effect -> mc.fontRendererObj.getStringWidth((Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName()))));

                for (PotionEffect potion : potions) {
                	Potion effect = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
                    
                	assert effect != null;

                	String level = I18n.format(effect.getName());
                if (potion.getAmplifier() == 1) {
                    level = level + " " + I18n.format("enchantment.level.2");
                } else if (potion.getAmplifier() == 2) {
                    level = level + " " + I18n.format("enchantment.level.3");
                } else if (potion.getAmplifier() == 3) {
                    level = level + " " + I18n.format("enchantment.level.4");
                }
                if (dph.getX() < sr.getScaledWidth() / 2) {
                	RenderUtils.drawBlurredShadow(dph.getX() + xOff - 25,(dph.getY() + counter) - yOff - 7, dph.getX() + xOff + 5 + mc.mntsb_17.getStringWidth(level) - (dph.getX() + xOff - 25), (dph.getY() - 1 + counter) - yOff + 18 - ((dph.getY() + counter) - yOff - 7), 10,  new Color(25,25,25));
                    
                    RoundedUtil.drawRound(dph.getX() + xOff - 25,(dph.getY() + counter) - yOff - 7, dph.getX() - (dph.getX() + xOff - 25) + xOff + mc.mntsb_17.getStringWidth(level) + 5, (dph.getY() - 1 - ((dph.getY() + counter) - yOff - 7) + counter) - yOff + 18, 1, new Color(25,25,25, 150));
                    
                    GlStateManager.color(1F, 1F, 1F, 1F);



                    int getPotionColor = -1;
                    if ((potion.getDuration() < 200)) {
                        getPotionColor = new Color(215, 59, 59).getRGB();
                    } else if (potion.getDuration() < 400) {
                        getPotionColor = new Color(231, 143, 32).getRGB();
                    } else if (potion.getDuration() > 400) {
                        getPotionColor = new Color(172, 171, 171).getRGB();
                    }

                    String durationString = Potion.getDurationString(potion);

                    mc.mntsb_17.drawStringWithShadow(level, dph.getX() + xOff, (dph.getY() + counter) - yOff - 1, -1);
                    
                    if (effect.hasStatusIcon()) {
                        mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                        int statusIconIndex = effect.getStatusIconIndex();
                        new Gui().drawTexturedModalRect((float) ((dph.getX() + xOff) - 21), (dph.getY() + counter) - yOff - 4, statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
                    }
                    
                    
                    mc.mntsb_13.drawStringWithShadow(durationString, dph.getX() + xOff, (dph.getY() + counter + 8) - yOff, getPotionColor);
                    
                    
                    counter += listOffset;
                    if (mc.player != null && mc.currentScreen instanceof GuiChat) {
                      	TextureEngine texture1 = new TextureEngine("celestial/images/grab.png", Celestial.scale, 65,65);
                          texture1.bind(dph.getX() + dph.getWidth() / 2 - 45, dph.getY() + dph.getHeight() / 2 - 20);
                      } else {
                      if (mc.player != null && mc.currentScreen instanceof GuiChat) {
                      	TextureEngine texture1 = new TextureEngine("celestial/images/grab.png", Celestial.scale, 65,65);
                          texture1.bind(dph.getX() + dph.getWidth() / 2 - 5, dph.getY() + dph.getHeight() / 2 - 20);
                      }}
                } else {
                	RenderUtils.drawBlurredShadow(dph.getX() + 100 - mc.mntsb_17.getStringWidth(level) + xOff - 26,(dph.getY() + counter) - yOff - 7, dph.getX() + xOff + 5 + mc.mntsb_17.getStringWidth(level) - (dph.getX() + xOff - 26), (dph.getY() - 1 + counter) - yOff + 18 - ((dph.getY() + counter) - yOff - 7), 10,  new Color(25,25,25));
                    
                    RoundedUtil.drawRound(dph.getX() + 100 - mc.mntsb_17.getStringWidth(level) + xOff - 26,(dph.getY() + counter) - yOff - 7, dph.getX() - (dph.getX() + xOff - 26) + xOff + mc.mntsb_17.getStringWidth(level) + 5, (dph.getY() - 1 - ((dph.getY() + counter) - yOff - 7) + counter) - yOff + 18, 1, new Color(25,25,25, 150));
                    
                    GlStateManager.color(1F, 1F, 1F, 1F);



                    int getPotionColor = -1;
                    if ((potion.getDuration() < 200)) {
                        getPotionColor = new Color(215, 59, 59).getRGB();
                    } else if (potion.getDuration() < 400) {
                        getPotionColor = new Color(231, 143, 32).getRGB();
                    } else if (potion.getDuration() > 400) {
                        getPotionColor = new Color(172, 171, 171).getRGB();
                    }

                    String durationString = Potion.getDurationString(potion);

                    mc.mntsb_17.drawStringWithShadow(level, dph.getX() + 100 - 21 - mc.mntsb_17.getStringWidth(level) + xOff, (dph.getY() + counter) - yOff - 1, -1);
                    
                    if (effect.hasStatusIcon()) {
                        mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                        int statusIconIndex = effect.getStatusIconIndex();
                        new Gui().drawTexturedModalRect((float) ((dph.getX() + 100 + xOff) - 16), (dph.getY() + counter) - yOff - 4, statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
                    }
                    
                    
                    mc.mntsb_13.drawStringWithShadow(durationString, dph.getX() + 100 - 21 - mc.mntsb_13.getStringWidth(durationString) + xOff, (dph.getY() + counter + 8) - yOff, getPotionColor);
                    
                    
                    counter += listOffset;
                    if (mc.player != null && mc.currentScreen instanceof GuiChat) {
                      	TextureEngine texture1 = new TextureEngine("celestial/images/grab.png", Celestial.scale, 65,65);
                          texture1.bind(dph.getX() + dph.getWidth() / 2 - 45, dph.getY() + dph.getHeight() / 2 - 10);
                      } else {
                      if (mc.player != null && mc.currentScreen instanceof GuiChat) {
                      	TextureEngine texture1 = new TextureEngine("celestial/images/grab.png", Celestial.scale, 65,65);
                          texture1.bind(dph.getX() + dph.getWidth() / 2 - 5, dph.getY() + dph.getHeight() / 2 - 10);
                      }}
                }
                }
                if (dph.getX() < sr.getScaledWidth() / 2) {
                	if (mc.player.getActivePotionEffects() != null) {
                	}
                    dph.setHeight(counter + 8);
                }
            }
        }
    }

    private double calculateBPS() {
        double bps = (Math.hypot(mc.player.posX - mc.player.prevPosX, mc.player.posZ - mc.player.prevPosZ) * mc.timer.timerSpeed) * 20;
        return Math.round(bps * 100.0) / 100.0;
    }


    @Override
    public void onEnable() {
        super.onEnable();
    }
}
