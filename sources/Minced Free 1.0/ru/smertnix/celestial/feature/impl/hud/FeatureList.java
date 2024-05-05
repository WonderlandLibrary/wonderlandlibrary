package ru.smertnix.celestial.feature.impl.hud;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import optifine.CustomColors;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.draggable.component.impl.DraggableArray;
import ru.smertnix.celestial.draggable.component.impl.DraggablePotionHUD;
import ru.smertnix.celestial.draggable.component.impl.DraggableTimer;
import ru.smertnix.celestial.draggable.component.impl.DraggableWaterMark;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.math.AnimationHelper;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.ColorUtils;
import ru.smertnix.celestial.utils.render.GLUtils;
import ru.smertnix.celestial.utils.render.GaussianBlur;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;
import ru.smertnix.celestial.utils.render.StencilUtil;
import ru.smertnix.celestial.utils.render.TextureEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FeatureList extends Feature {
    public float scale = 2;
    public static ListSetting backGroundColorMode = new ListSetting("Background Color", "Fade", () -> true, "Fade", "Rainbow-Fade", "Static",  "Astolfo");
    public NumberSetting offsetY = new NumberSetting("Offset Y", 0.7f, 0.5f, 0.8f, 0.1f, () -> true);
    public NumberSetting yStep = new NumberSetting("Step", 9, 3, 10, 0.01f, () -> true);
    public BooleanSetting noVisualModules = new BooleanSetting("No Render", false, () -> true);
    public BooleanSetting lowerCase = new BooleanSetting("Lower Case", false, () -> true);
    public static ColorSetting backGroundColor = new ColorSetting("Color", new Color(17, 17, 17, 180).getRGB(), () -> backGroundColorMode.currentMode.equals("Static") || backGroundColorMode.currentMode.equals("Rainbow-Fade"));
    public static ColorSetting oneColor = new ColorSetting("One Color", new Color(158, 13, 239).getRGB(), () -> backGroundColorMode.currentMode.equals("Fade"));
    public static ColorSetting twoColor = new ColorSetting("Second Color", new Color(158, 13, 239).darker().getRGB(), () -> backGroundColorMode.currentMode.equals("Fade") || backGroundColorMode.currentMode.equals("Fade"));


    public FeatureList() {
        super("Array List", "Показывает список включенных модулей", FeatureCategory.Render);
        addSettings(backGroundColorMode, backGroundColor, oneColor, twoColor, offsetY, noVisualModules, lowerCase);
    }
    
    
    @EventTarget
    public void RenderArray(EventRender2D event) {
    	boolean blur = false;
  	  if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
    	DraggableArray di = (DraggableArray) Celestial.instance.draggableHUD.getDraggableComponentByClass(DraggableArray.class);

    	   GlStateManager.pushMatrix();

        di.setWidth(80);
        int stringWidth;
        int blurv = 30;
        List<Feature> activeModules = Celestial.instance.featureManager.getAllFeatures();
        activeModules.sort(Comparator.comparingDouble(s -> -mc.mntsb_13.getStringWidth((lowerCase.getCurrentValue() ? s.getLabel().toLowerCase() : s.getLabel()).replaceAll(" ",""))));
       // float displayWidth = event.getResolution().getScaledWidth() * (event.getResolution().getScaleFactor() / 2F);
        float displayWidth = di.getX();
        float yPotionOffset = 2;
        for (PotionEffect potionEffect : Helper.mc.player.getActivePotionEffects()) {
            if (potionEffect.getPotion().isBeneficial()) {
                yPotionOffset = 30;
            }
            if (potionEffect.getPotion().isBadEffect()) {
                yPotionOffset = 30 * 2;
            }
        }
     //   int y = (int) (5 + yPotionOffset);
        float y = di.getY();
        float y2 = di.getY();
        float y3 = di.getY();
        ScaledResolution sr = new ScaledResolution(this.mc);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        boolean reverse = displayWidth > (float)(width / 2);
        boolean reverseY = y > (float)(height / 2);
        int yTotal = 0;
        float offset = this.offsetY.getNumberValue() + 0.1f;
        
        
        for (int i = 0; i < Celestial.instance.featureManager.getAllFeatures().size(); ++i) {
            yTotal += mc.mntsb_13.getFontHeight() + 3;
        }
        
        
        if(reverse){
        for (Feature module : activeModules) {
        	String name = (lowerCase.getCurrentValue() ? module.getLabel().toLowerCase() : module.getLabel()).replaceAll(" ", "");
            module.animYto = AnimationHelper.Move(module.animYto, (float) (module.isEnabled() ? 1 : 0), (float) (6.5f * Celestial.deltaTime()), (float) (6.5f * Celestial.deltaTime()), (float) Celestial.deltaTime());


            if (module.animYto > 0.01f) {
                if (module.getSuffix().equals("ClickGui") || noVisualModules.getBoolValue() && module.getCategory() == FeatureCategory.Render )
                    continue;
                stringWidth = this.mc.mntsb_13.getStringWidth(name) + 3;

                GlStateManager.pushMatrix();
                	  RenderUtils.drawBlurredShadow(displayWidth + 50 - mc.mntsb_13.getStringWidth(name) - 6, y - 1.8f + (float)offset, stringWidth + 6, 10, 10, RenderUtils.injectAlpha(ClientHelper.getClientColor(y, yTotal, 5), blur ? 120 : 150));


                      GL11.glTranslated(1, y, 1);
                      GL11.glTranslated(-1, -y, 1);
                      

                      if (!blur)
                      RenderUtils.drawRect(displayWidth + 50 - mc.mntsb_13.getStringWidth(name) - 6.5f, y - 0.5f + 1.2f + offset, displayWidth + 50, y + (float)offset + 9.2f, RenderUtils.injectAlpha(ClientHelper.getClientColor(y, yTotal, 5), (int) (animYto * 255)).getRGB() );
                    
                 
                    RenderUtils.drawRect(displayWidth + 49, y - 2 + offset + 2 - 0.5f, displayWidth +51.5f,  y + 9.2f + (float)offset, -1);
                
             
                    GlStateManager.popMatrix();
                y += 9.2f * module.animYto * offset;
                
                
            }
        }
        if (blur) {
       	 StencilUtil.initStencilToWrit2e();
            for (Feature module2 : activeModules) {
                module2.animYto = AnimationHelper.Move(module2.animYto, (float) (module2.isEnabled() ? 1 : 0), (float) (6.5f * Celestial.deltaTime()), (float) (6.5f * Celestial.deltaTime()), (float) Celestial.deltaTime());
                String name2 = (lowerCase.getCurrentValue() ? module2.getLabel().toLowerCase() : module2.getLabel()).replaceAll(" ", "");
                if (module2.animYto > 0.01f) {
             	   if (module2.getSuffix().equals("ClickGui") || noVisualModules.getBoolValue() && module2.getCategory() == FeatureCategory.Render )
                        continue;
                    stringWidth = this.mc.mntsb_13.getStringWidth(name2) + 3;
                    RenderUtils.drawRect(displayWidth + 50 - mc.mntsb_13.getStringWidth(name2) - 6.5f, y2 - 0.5f - 2 + 2 + offset, displayWidth + 50, y2 + (float)offset + 9.2f, RenderUtils.injectAlpha(ClientHelper.getClientColor(y2, yTotal, 5), (int) (animYto * 255)).getRGB() );
                }
                y2 += 9.2f * module2.animYto * offset;
            }
        	StencilUtil.readStencilBuffer(1);
            GaussianBlur.renderBlur(blurv);
            StencilUtil.uninitStencilBuffer();
         	  
       }
               for (Feature module2 : activeModules) {
                   module2.animYto = AnimationHelper.Move(module2.animYto, (float) (module2.isEnabled() ? 1 : 0), (float) (6.5f * Celestial.deltaTime()), (float) (6.5f * Celestial.deltaTime()), (float) Celestial.deltaTime());
                   String name2 = (lowerCase.getCurrentValue() ? module2.getLabel().toLowerCase() : module2.getLabel()).replaceAll(" ", "");
                   if (module2.animYto > 0.01f) {
                	   if (module2.getSuffix().equals("ClickGui") || noVisualModules.getBoolValue() && module2.getCategory() == FeatureCategory.Render )
                           continue;
                       stringWidth = this.mc.mntsb_13.getStringWidth(name2) + 3;
                       GL11.glTranslated(1, y, 1);
                       GL11.glTranslated(-1, -y, 1);
                       mc.mntsb_13.drawString(name2, displayWidth + 50.5f - mc.mntsb_13.getStringWidth(name2) - 4f, y3 + mc.mntsb_13.getFontHeight() + (float)offset - 1, -1);
                   }
                   y3 += 9.2f * module2.animYto * offset;
               }
        }
    if(!reverse){
        for (Feature module : activeModules) {
            module.animYto = AnimationHelper.Move(module.animYto, (float) (module.isEnabled() ? 1 : 0), (float) (6.5f * Celestial.deltaTime()), (float) (6.5f * Celestial.deltaTime()), (float) Celestial.deltaTime());
           	String name = (lowerCase.getCurrentValue() ? module.getLabel().toLowerCase() : module.getLabel()).replaceAll(" ", "");
            if (module.animYto > 0.01f) {
                if (module.getSuffix().equals("ClickGui") || noVisualModules.getBoolValue() && module.getCategory() == FeatureCategory.Render )
                    continue;
                stringWidth = this.mc.mntsb_13.getStringWidth(name) + 3;
                GlStateManager.pushMatrix();
                RenderUtils.drawBlurredShadow(displayWidth -2, y + (float)offset - 3.5f + 2, stringWidth + 5f, 10, 10, RenderUtils.injectAlpha(ClientHelper.getClientColor(y, yTotal, 10), blur ? 120 : 150));
                GL11.glTranslated(1, y, 1);
                GL11.glTranslated(-1, -y, 1);
                if (!blur)
                	RenderUtils.drawRect(displayWidth, y - 0.5f -2 + offset + 2, displayWidth + (float)stringWidth + 3.5f, y + (float)offset + 8.0f, RenderUtils.injectAlpha(ClientHelper.getClientColor(y, yTotal, 10), (int) (animYto * 255)).getRGB());
                    RenderUtils.drawRect(displayWidth - 1.5f, y - 0.5f -2 + offset + 2, displayWidth + 1 , y + (float)offset +8f,  -1);
                    GlStateManager.popMatrix();
          
            }    
        
            y += 9.2f * module.animYto * offset;
            
            }
        if (blur) {
        	 StencilUtil.initStencilToWrit2e();
             for (Feature module2 : activeModules) {
                 module2.animYto = AnimationHelper.Move(module2.animYto, (float) (module2.isEnabled() ? 1 : 0), (float) (6.5f * Celestial.deltaTime()), (float) (6.5f * Celestial.deltaTime()), (float) Celestial.deltaTime());
                 String name2 = (lowerCase.getCurrentValue() ? module2.getLabel().toLowerCase() : module2.getLabel()).replaceAll(" ", "");
                 if (module2.animYto > 0.01f) {
              	   if (module2.getSuffix().equals("ClickGui") || noVisualModules.getBoolValue() && module2.getCategory() == FeatureCategory.Render )
                         continue;
                     stringWidth = this.mc.mntsb_13.getStringWidth(name2) + 3;
                     RenderUtils.drawRect(displayWidth, y2 - 0.5f -2 + offset + 2, displayWidth + (float)stringWidth + 3.5f, y2 + (float)offset + 8.0f, -1);
                 }
                 y2 += 9.2f * module2.animYto * offset;
             }
         	StencilUtil.readStencilBuffer(1);
             GaussianBlur.renderBlur(blurv);
             StencilUtil.uninitStencilBuffer();
          	  
        }
        for (Feature module2 : activeModules) {
            module2.animYto = AnimationHelper.Move(module2.animYto, (float) (module2.isEnabled() ? 1 : 0), (float) (6.5f * Celestial.deltaTime()), (float) (6.5f * Celestial.deltaTime()), (float) Celestial.deltaTime());
            String name2 = (lowerCase.getCurrentValue() ? module2.getLabel().toLowerCase() : module2.getLabel()).replaceAll(" ", "");
            if (module2.animYto > 0.01f) {
         	   if (module2.getSuffix().equals("ClickGui") || noVisualModules.getBoolValue() && module2.getCategory() == FeatureCategory.Render )
                    continue;
                stringWidth = this.mc.mntsb_13.getStringWidth(name2) + 3;
                GL11.glTranslated(1, y, 1);
                GL11.glTranslated(-1, -y, 1);
                this.mc.mntsb_13.drawString(name2, displayWidth + 3.5f, y3 + (float)offset + 2, -1);

            }
            y3 += 9.2f * module2.animYto * offset;
        }
        }
    
    di.setHeight((int) y - di.getY());
    GL11.glTranslated(1, y, 1);
    GL11.glTranslated(-1, -y, 1);
    if (mc.player != null && mc.currentScreen instanceof GuiChat) {
    	   TextureEngine texture = new TextureEngine("celestial/images/grab.png", Celestial.scale, 65,65);
    	    texture.bind(di.getX() + di.getWidth() / 2 - 40, di.getY() + di.getHeight() / 2 - 5);
    }
    GL11.glPopMatrix();
    }
    @EventTarget
    public void Event2D(EventRender2D event) {
    	if (!isEnabled()) return;
        List<Feature> activeModules = Celestial.instance.featureManager.getAllFeatures();
        if (lowerCase.getBoolValue()) {
            activeModules.sort(Comparator.comparingDouble(s -> -mc.mntsb_15.getStringWidth(s.getLabel().replace(" ", "").toLowerCase())));
        } else {
            activeModules.sort(Comparator.comparingDouble(s -> -mc.mntsb_15.getStringWidth(s.getLabel().replace(" ", ""))));
        }
        float displayWidth = event.getResolution().getScaledWidth() * (event.getResolution().getScaleFactor() / 2F);
        float yPotionOffset = 2;
        int y = (int) (5 + yPotionOffset);
        int yTotal = 0;
        for (int i = 0; i < Celestial.instance.featureManager.getAllFeatures().size(); ++i) {
            yTotal += ClientHelper.getFontRender().getFontHeight() + 3;
        }
        for (Feature module : activeModules) {
            module.animYto = AnimationHelper.Move(module.animYto, (float) (module.isEnabled() ? 1 : 0), (float) (6.5f * Celestial.deltaTime()), (float) (6.5f * Celestial.deltaTime()), (float) Celestial.deltaTime());
            if (module.animYto > 0.01f) {
                if (module.getLabel().equals("ClickGui") || noVisualModules.getBoolValue() && module.getCategory() == FeatureCategory.Render)
                    continue;
                String mode = FeatureList.backGroundColorMode.getCurrentMode();
                GL11.glPushMatrix();
                GL11.glTranslated(1, y, 1);
                GL11.glScaled(1, 1, 1);
                GL11.glTranslated(-1, -y, 1);
                y += offsetY.getNumberValue() * 10 * module.animYto;
                GL11.glPopMatrix();
            }
        }
    }
}