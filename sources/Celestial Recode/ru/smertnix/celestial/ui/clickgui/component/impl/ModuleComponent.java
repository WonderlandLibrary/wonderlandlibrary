package ru.smertnix.celestial.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.hud.ClickGUI;
import ru.smertnix.celestial.feature.impl.misc.Optimizer;
import ru.smertnix.celestial.ui.clickgui.ClickGuiScreen;
import ru.smertnix.celestial.ui.clickgui.Panel;
import ru.smertnix.celestial.ui.clickgui.component.Component;
import ru.smertnix.celestial.ui.clickgui.component.ExpandableComponent;
import ru.smertnix.celestial.ui.components.SorterHelper;
import ru.smertnix.celestial.ui.settings.Setting;
import ru.smertnix.celestial.ui.settings.impl.*;
import ru.smertnix.celestial.utils.math.AnimationHelper;
import ru.smertnix.celestial.utils.math.TimerHelper;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;
import ru.smertnix.celestial.utils.render.StencilUtil;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javafx.animation.Interpolator;

import java.awt.*;


public final class ModuleComponent extends ExpandableComponent {
    Minecraft mc = Minecraft.getMinecraft();
    private final Feature module;
    public static TimerHelper timerHelper = new TimerHelper();
    private boolean binding;
    int alpha = 0;
    private final TimerHelper descTimer = new TimerHelper();
    public float anim;
    public ModuleComponent(ru.smertnix.celestial.ui.clickgui.component.Component parent, Feature module, int x, int y, int width, int height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.module = module;
        int propertyX = ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanSettingComponent(this, (BooleanSetting) setting, propertyX, height, width - (ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET * 2), ru.smertnix.celestial.ui.clickgui.Panel.ITEM_HEIGHT + 6));
            } else if (setting instanceof ColorSetting) {
                components.add(new ColorPickerComponent(this, (ColorSetting) setting, propertyX, height, width - (ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET * 2), ru.smertnix.celestial.ui.clickgui.Panel.ITEM_HEIGHT));
            } else if (setting instanceof NumberSetting) {
                components.add(new NumberSettingComponent(this, (NumberSetting) setting, propertyX, height, width - (ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET * 2), ru.smertnix.celestial.ui.clickgui.Panel.ITEM_HEIGHT + 5));
            } else if (setting instanceof ListSetting) {
                components.add(new ListSettingComponent(this, (ListSetting) setting, propertyX, height, width - (ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET * 2), ru.smertnix.celestial.ui.clickgui.Panel.ITEM_HEIGHT + 2));
            } else if (setting instanceof MultipleBoolSetting) {
                components.add(new MultipleBoolSettingComponent(this, (MultipleBoolSetting) setting, propertyX, height, width - (ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET * 2), ru.smertnix.celestial.ui.clickgui.Panel.ITEM_HEIGHT + 1));
            } else if (setting instanceof StringSetting) {
                components.add(new StringComponent(this, (StringSetting) setting, propertyX, height, width - (ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET * 2), ru.smertnix.celestial.ui.clickgui.Panel.ITEM_HEIGHT + 1));
            }

        }
    }

    public boolean ready = false;
    static String i = " ";

    String getI(String s) {
        if (!timerHelper.hasReached(5)) {
            return i;
        } else {
            timerHelper.reset();
        }
        if (i.length() < s.length()) {
            ready = false;
            return i += s.charAt(i.length());
        }
        ready = true;
        return i;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        components.sort(new SorterHelper());
        float x = getX();
        float y = getY() - 2;
        int width = getWidth();
        int height = getHeight();
        Color color = new Color(ClickGUI.color.getColorValue());
        Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 140);
        boolean hovered = isHovered(mouseX, mouseY);
        ScaledResolution sr = new ScaledResolution(mc);
        GlStateManager.pushMatrix();
    	GL11.glEnable(GL11.GL_SCISSOR_TEST);
    	RenderUtils.drawRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
    	RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 239);
        
    	 Color clor = new Color(ClickGUI.color.getColorValue());
         
         Color clor2 = new Color(ClickGUI.color2.getColorValue())/*.darker()*/;
         
         Color clor3 = Color.WHITE;
         float anim;
         alpha = (int) Interpolator.LINEAR.interpolate(alpha, module.isEnabled() ? 1000 : 0,  module.isEnabled() ? 0.15f : 0.5f);
         anim = alpha;
        
        components.sort(new SorterHelper());
        
        if (ClickGUI.shadow.getBoolValue()) {
        	RenderUtils.drawBlurredShadow(x - 3, y + height / 2F - 2 - 5, width + 2, isExpanded() ? getHeightWithExpand() + 1 : 15.5f, 5, new Color(0,0,0, 150));
        }
        
        RoundedUtil.drawGradientRound(x - 3, y + height / 2F - 2 - 5, width + 1, isExpanded() ? getHeightWithExpand() + 1 : 15.5f, 2, clor3, clor3,clor3,clor3);
        
        if (anim > 600) {
  		  RoundedUtil.drawGradientRound(x - 3, y + height / 2F - 2 - 5, (width + 1), (isExpanded() ? getHeightWithExpand() + 1 : 15.5f), 2, clor, clor,clor2,clor2);
  		  
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
  		GlStateManager.popMatrix();
  		
  		
  		
  		
  		
  		
  		
    		GlStateManager.pushMatrix();
    	    GL11.glEnable(GL11.GL_SCISSOR_TEST);
    	    RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 239);
    	    
    	  	StencilUtil.initStencilToWrite();
            RoundedUtil.drawRound(x - 3, y + height / 2F - 2 - 5, (width + 1), (isExpanded() ? getHeightWithExpand() + 1 : 15.5f), 2, new Color(10, 10, 10, 180));
            StencilUtil.readStencilBuffer(1);
            
            
            //  	RenderUtils.drawBlurredShadow(mouseX - (anim / 7), mouseY - (anim / 7), (anim / 7) * 2, (anim / 7) * 2, (int) 15, clor);
      //  	RenderUtils.drawCircle(mouseX, mouseY, anim / 7, clor.getRGB());
        	
        	
        	StencilUtil.uninitStencilBuffer();
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        	GlStateManager.popMatrix();
    	
    	
    	
    	
    	
    	
    	
    	
    	 GlStateManager.pushMatrix();
     	GL11.glEnable(GL11.GL_SCISSOR_TEST);
     	RenderUtils.drawRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
     	RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 239);
     	
  //   	 if (anim > 550)
     	if (module.isEnabled())
    	RoundedUtil.drawGradientHorizontal(x - 3, y + height / 2F - 2 - 5, (width + 1), (isExpanded() ? getHeightWithExpand() + 1 : 15.5f), 2, clor,clor2.darker());
     	
    		  
        if (components.size() > 0.5) {
            mc.fontRendererObj.drawString(isExpanded() ? "\u25b2" : "\u25bc", x + 93.5f-8, y + height / 2F - 3.5f, module.isEnabled() ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
        }
        
        mc.mntsb_14.drawCenteredString(binding ? "Press a key.. " : getName(), x + 53.5f-8, y + height / 2F - 2 + 1, module.isEnabled() ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
        
        if (isExpanded()) {
            int childY = Panel.ITEM_HEIGHT;
            for (ru.smertnix.celestial.ui.clickgui.component.Component child : components) {
                int cHeight = child.getHeight();
                if (child instanceof BooleanSettingComponent) {
                    BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent) child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof NumberSettingComponent) {
                    NumberSettingComponent numberSettingComponent = (NumberSettingComponent) child;
                    if (!numberSettingComponent.numberSetting.isVisible()) {
                        continue;
                    }
                }

                if (child instanceof ColorPickerComponent) {
                    ColorPickerComponent colorPickerComponent = (ColorPickerComponent) child;
                    if (!colorPickerComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ListSettingComponent) {
                    ListSettingComponent listSettingComponent = (ListSettingComponent) child;
                    if (!listSettingComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) child;
                    if (expandableComponent.isExpanded()) cHeight = expandableComponent.getHeightWithExpand();
                }
                child.setY(childY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                childY += cHeight;
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
  		GL11.glTranslated(1, y, 1);
        GL11.glTranslated(-1, -y, 1);
        
        module.anim = (float) Interpolator.LINEAR.interpolate(module.anim, hovered ? 255 : 0, 0.15f);
  		if (module.getDesc() != null && module.getDesc().length() > 8 && hovered && mouseY > 26 && mouseY < 239) {
  			RoundedUtil.drawRound(getX() + 4 +  getWidth(),getY() + 4, 2 + mc.mntsb_15.getStringWidth(module.getDesc().replaceAll("Ё","E").replaceAll("ё","e").replaceAll("�"," ")), 8, 1, new Color(50,50,50,(int) (module.anim)));
  			mc.mntsb_15.drawStringWithShadow(module.getDesc().replaceAll("Ё","E").replaceAll("ё","e").replaceAll("�"," "), getX() + 5 + getWidth(), getY() + 6, new Color(255,255,255,(int) module.anim).getRGB());
  		}
  		GlStateManager.popMatrix();
    }

    @Override
    public boolean canExpand() {
        return !components.isEmpty();
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        switch (button) {
            case 0:
                module.toggle();
                break;
            case 2:
                binding = !binding;
                break;
        }
    }

    @Override
    public void onKeyPress(int keyCode) {
        if (binding) {
            ClickGuiScreen.escapeKeyInUse = true;
            module.setBind(keyCode == Keyboard.KEY_DELETE ? Keyboard.KEY_NONE : keyCode);
            binding = false;
        }
    }

    @Override
    public int getHeightWithExpand() {
        int height = getHeight();
        if (isExpanded()) {
            for (Component child : components) {
                int cHeight = child.getHeight();
                if (child instanceof BooleanSettingComponent) {
                    BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent) child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof NumberSettingComponent) {
                    NumberSettingComponent numberSettingComponent = (NumberSettingComponent) child;
                    if (!numberSettingComponent.numberSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ColorPickerComponent) {
                    ColorPickerComponent colorPickerComponent = (ColorPickerComponent) child;
                    if (!colorPickerComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ListSettingComponent) {
                    ListSettingComponent listSettingComponent = (ListSettingComponent) child;
                    if (!listSettingComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) child;
                    if (expandableComponent.isExpanded()) cHeight = expandableComponent.getHeightWithExpand();
                }
                height += cHeight;
            }
        }
        return height;
    }

}
