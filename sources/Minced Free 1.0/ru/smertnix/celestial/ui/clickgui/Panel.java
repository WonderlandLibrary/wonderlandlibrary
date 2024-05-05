package ru.smertnix.celestial.ui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.hud.ClickGUI;
import ru.smertnix.celestial.ui.altmanager.alt.Server;
import ru.smertnix.celestial.ui.clickgui.component.AnimationState;
import ru.smertnix.celestial.ui.clickgui.component.Component;
import ru.smertnix.celestial.ui.clickgui.component.DraggablePanel;
import ru.smertnix.celestial.ui.clickgui.component.ExpandableComponent;
import ru.smertnix.celestial.ui.clickgui.component.impl.ModuleComponent;
import ru.smertnix.celestial.ui.config.Config;
import ru.smertnix.celestial.ui.notification.NotificationMode;
import ru.smertnix.celestial.ui.notification.NotificationRenderer;
import ru.smertnix.celestial.utils.other.ChatUtils;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;
import ru.smertnix.celestial.utils.render.StencilUtil;

import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;

import javafx.animation.Interpolator;


public final class Panel extends DraggablePanel {
    Minecraft mc = Minecraft.getMinecraft();
    public static final int HEADER_WIDTH = 107;
    public static final int X_ITEM_OFFSET = 1;
    public static final int ITEM_HEIGHT = 15;
    public static final int HEADER_HEIGHT = 17;
    public List<Feature> features;
    public FeatureCategory type;
    public AnimationState state;
    private int prevX;
    public static Config sel;
    public static float o;
    public static float anim;
    private int prevY;
    public int counter = 0;
    private boolean dragging;
    public Panel(FeatureCategory category, int x, int y) {
        super(null, category.name(), x, y, HEADER_WIDTH, HEADER_HEIGHT);
        int moduleY = HEADER_HEIGHT;
        this.state = AnimationState.STATIC;
        this.features = Celestial.instance.featureManager.getFeaturesCategory(category);
        for (Feature feature : features) {
            this.components.add(new ModuleComponent(this, feature, X_ITEM_OFFSET, moduleY, HEADER_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
            moduleY += ITEM_HEIGHT;
        }
        this.type = category;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (dragging) {
            setX(mouseX - prevX);
            setY(mouseY - prevY);
        }
        
        
        setExpanded(true);
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        int headerHeight;
        int heightWithExpand = getHeightWithExpand();
        headerHeight = (isExpanded() ? heightWithExpand : height);
        float startAlpha1 = 0.14f;
        int size1 = 25;
        float left1 = x + 1.0f;
        float right1 = x + width;
        float bottom1 = y + headerHeight - 6.0f;
        float top1 = y + headerHeight - 2.0f;
        float top2 = y + 13.0f;

        Color color = new Color(ClickGUI.color.getColorValue());

        float extendedHeight = 2;
        ScaledResolution sr = new ScaledResolution(mc);
        

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    	RenderUtils.drawRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
    	RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 250);
    	
    	if (getName().equalsIgnoreCase("Configs")) {
    		 RoundedUtil.drawRound(x - 4, 10 + 4, width + 3, 228 - 4 - 60, 4, new Color(245,245,245, 220));
    	} else {
    		 RoundedUtil.drawRound(x - 4, 10 + 4, width + 3, 228 - 4, 4, new Color(245,245,245, 220));
    	}

        if (ClickGUI.blur.getBoolValue()) {
        		if (getName().equalsIgnoreCase("Configs")) {
        			RenderUtils.drawBlurredShadow(x - 4 - 2, 10 + 2, width + 3 + 4, 228 - 60, 14, new Color(245,245,245, 100));
        			 
        		
        		} else {
        			 RenderUtils.drawBlurredShadow(x - 4 - 2, 10 + 2, width + 3 + 4, 228, 14, new Color(245,245,245, 100));
        		}
        }
        
        
        if (getName().equalsIgnoreCase("Configs")) {
        	RoundedUtil.drawRound(x - 4, 10 + 4, width + 3, 228 - 4 - 60, 4, new Color(225,225,225, 220));
        } else {
        	RoundedUtil.drawRound(x - 4, 10 + 4, width + 3, 228 - 4, 4, new Color(225,225,225, 220));
        }
        
        
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
  		GlStateManager.popMatrix();
  		
        
        
        
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    	RenderUtils.drawRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
    	RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 239);
        
    	
    	

    	/*
    	if (ClickGUI.blur.getBoolValue()) {
        	RenderUtils.drawShadow(5, 1, () -> {
            	RoundedUtil.drawRound(x - 4, 10, width + 3, 228, 4, new Color(245,245,245, 220));
            });
        }*/
    	
    	
    	                
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
  		GlStateManager.popMatrix();
  		
  		GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    	RenderUtils.drawRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
    	RenderUtils.scissorRect(0, 0, sr.getScaledWidth(), 25.5f);
    	
    	
        RenderUtils.drawBlurredShadow(x - 4 - 2, 10 + 2, width + 3 + 4, 228 - 60, 14, RenderUtils.injectAlpha(new Color(ClickGUI.color.getColorValue()), 100));

    	
        RoundedUtil.drawGradientRound(x - 4, 10, width + 3, 30, 5, new Color(ClickGUI.color.getColorValue()), new Color(ClickGUI.color2.getColorValue()), new Color(ClickGUI.color2.getColorValue()).darker(), new Color(ClickGUI.color.getColorValue()).darker());
       
		
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
  		GlStateManager.popMatrix();
  		
  		if (getName().equalsIgnoreCase("Configs")) {
        	 float offset = 0;
             ClickGuiScreen.plus.xPosition = x - 4 + 14;
             ClickGuiScreen.plus.yPosition = 10 + 4 + 228 - 90;
             
             ClickGuiScreen.minus.xPosition = x - 4 + 39;
             ClickGuiScreen.minus.yPosition = 10 + 4 + 228 - 90;
             
             ClickGuiScreen.files.xPosition = x - 4 + 64;
             ClickGuiScreen.files.yPosition = 10 + 4 + 228 - 90;
             
             ClickGuiScreen.search.xPosition = x - 4 + 14;
             ClickGuiScreen.search.yPosition = 10 + 228 - 70;
             
             RoundedUtil.drawRound(x - 4 + 14, 10 - 3 + 228 - 70, 71, 10, 1, new Color(0,0,0,50));
             int y2 = 17;
             anim = (float) Interpolator.LINEAR.interpolate(anim, o, 0.5f);
             offset = anim;
             ArrayList<Config> cfg = (ArrayList<Config>) Celestial.instance.configManager.getContents();
             cfg.sort(Comparator.comparingInt(m -> Minecraft.getMinecraft().mntsb_15.getStringWidth(((Config)m).getName())).reversed());
           	StencilUtil.initStencilToWrite();
            RoundedUtil.drawRound(x, 27, width,  138 - 4 - 13, 2, new Color(10, 10, 10, 180));
            StencilUtil.readStencilBuffer(1);
             for (Config c : cfg) {
            	 Color clor = Color.WHITE;
            	 Color clor2 = Color.WHITE;
            	 if (sel != null) {
            		 clor = c.getName().equalsIgnoreCase(sel.getName()) ? new Color(ClickGUI.color.getColorValue()) : new Color(-1);
                     clor2 = c.getName().equalsIgnoreCase(sel.getName())  ? new Color(ClickGUI.color2.getColorValue()).darker() : new Color(-1);
            	 }
                
                 RoundedUtil.drawGradientRound(x - 3 + 2, y + height / 2F - 2 - 5 + y2 + offset, width + 1 - 4, 15.5f, 2, clor, clor,clor2,clor2);
                 mc.mntsb_15.drawCenteredString(c.getName(), x + 53.5f-7, y + height / 2F - 3 + 2 + y2 + offset, Color.BLACK.getRGB());
                 if (!Mouse.isButtonDown(0)) {
                	 counter=1;
                 }
            	 if (Mouse.isButtonDown(0)) {
            		 if (counter != 0) {
            			 if (mouseY < 148 - 4 && mouseX >x - 3 + 2  && mouseY > y + height / 2F - 2 - 5 + y2 + offset && mouseX <x - 3 + 2 + width + 1 - 4 && mouseY < y + height / 2F - 2 - 5 + y2 + offset + 15.5f) {
               			  if (sel == c) {
               				  if (Celestial.instance.configManager.loadConfig(sel.getName())) {
               					  System.out.println("1");
               				  } else {
               					  System.out.println("2");
               				  }
                        	 }
               				  if (sel !=c) {
                   				  counter = 0 ;
                   			  }
               				  sel = c;
                         }
            		 }
            	 }
                 y2 += 20;
             }  	
             StencilUtil.uninitStencilBuffer();
             
             if (mouseX > x && mouseY > 24 && mouseX <  x + width && mouseY < 14 + 138 - 4) {
            	   if (Mouse.hasWheel()) {
                       int wheel = Mouse.getDWheel();
                       if (wheel > 0) {
                           this.o += 26.0;
                           if (this.o > 0.0) {
                               this.o = 0.0F;
                           }
                       } else if (wheel < 0) {
                           this.o -= 26.0;
                           if (this.o < -y2 + 130) {
                               this.o = -y2 + 130;
                           }
                       }
                   }
               }
             float x3 = ClickGuiScreen.plus.xPosition;
             float y3 = ClickGuiScreen.plus.yPosition;
             float width3 = ClickGuiScreen.plus.getButtonWidth();
             float height3 = ClickGuiScreen.plus.height;
             float x4 = ClickGuiScreen.minus.xPosition;
             float y4 = ClickGuiScreen.minus.yPosition;
             float width4 = ClickGuiScreen.minus.getButtonWidth();
             float height4 = ClickGuiScreen.minus.height;
             float x5 = ClickGuiScreen.files.xPosition;
             float y5 = ClickGuiScreen.files.yPosition;
             float width5 = ClickGuiScreen.files.getButtonWidth();
             float height5 = ClickGuiScreen.files.height;
            	 if (counter != 0 && Mouse.isButtonDown(0) && mouseX > x3 && mouseY > y3 && mouseX < x3 + width3 && mouseY < y3 + height3) {
                	 if (sel == null) {
                		 if (!ClickGuiScreen.search.getText().equalsIgnoreCase("")) {
                			   if (Celestial.instance.configManager.saveConfig(ClickGuiScreen.search.getText())) {
                              	 ChatUtils.addChatMessage(ChatFormatting.WHITE + "Конфиг " + ChatFormatting.RED + "\'" + ClickGuiScreen.search.getText() + "\'" + ChatFormatting.WHITE + " был сохранён!");
                              	 NotificationRenderer.queue("Config Debug", "Config " + ChatFormatting.RED + "\'" + ClickGuiScreen.search.getText() + "\'" + ChatFormatting.WHITE + " was saved", 4, NotificationMode.INFO);
                      			 Celestial.instance.fileManager.saveFiles();
                                   Celestial.instance.configManager.load();
                              	 ClickGuiScreen.search.setText("");
                      			 ClickGuiScreen.search.setFocused(false);
                              } else {
                              	  ChatUtils.addChatMessage(ChatFormatting.WHITE + "пон");
                              	  }
                		 }
                	 } else {
                		    if (Celestial.instance.configManager.saveConfig(sel.getName())) {
                           	 ChatUtils.addChatMessage(ChatFormatting.WHITE + "Конфиг " + ChatFormatting.RED + "\'" + sel.getName() + "\'" + ChatFormatting.WHITE + "был загружен!");
                          	 NotificationRenderer.queue("Config Debug", "Config " + ChatFormatting.RED + "\'" + sel.getName() + "\'" + ChatFormatting.WHITE + " was saved", 4, NotificationMode.INFO);

                    			 Celestial.instance.fileManager.saveFiles();
                                 Celestial.instance.configManager.load();
                            	 ClickGuiScreen.search.setText("");
                    			 ClickGuiScreen.search.setFocused(false);
                            } else {

                            	  }
                	 }
               	  counter = 0 ;
                 }
                 if (counter != 0 && Mouse.isButtonDown(0) && mouseX > x4 && mouseY > y4 && mouseX < x4 + width4 && mouseY < y4 + height4) {
                	 if (sel != null) {
                	     if (Celestial.instance.configManager.deleteConfig(sel.getName())) {
                        	 ChatUtils.addChatMessage(ChatFormatting.WHITE + "Конфиг " + ChatFormatting.RED + "\'" + sel.getName() + "\'" + ChatFormatting.WHITE + " был удален!");
                          	 NotificationRenderer.queue("Config Debug", "Config " + ChatFormatting.RED + "\'" + sel.getName() + "\'" + ChatFormatting.WHITE + " was deleted", 4, NotificationMode.INFO);

                        	 ClickGuiScreen.search.setText("");
                			 ClickGuiScreen.search.setFocused(false);
                	     } else {
                        	  }
                	 }
               	  counter = 0 ;
                 }
                 
                 if (counter != 0 && Mouse.isButtonDown(0) && mouseX > x5 && mouseY > y5 && mouseX < x5 + width5 && mouseY < y5 + height5) {
                	 File file = new File("C:\\Minced", "configs");
                     Sys.openURL(file.getAbsolutePath());
               	  counter = 0 ;
                 }
                   
                	 if (!ClickGuiScreen.search.getText().equalsIgnoreCase("")) {
                  	   mc.mntsb_16.drawString(ClickGuiScreen.search.getText() + "_", ClickGuiScreen.search.xPosition + 2, ClickGuiScreen.search.yPosition, Color.GRAY.getRGB());
                   } else {
                  	 if (!ClickGuiScreen.search.isFocused)
                  	   mc.mntsb_16.drawString("Config name...", ClickGuiScreen.search.xPosition + 4, ClickGuiScreen.search.yPosition, -1);
                   }
        }
  		
     		
        mc.mntsb_20.drawCenteredStringWithShadow(getName(), x + 53.5f - 8, 14, Color.WHITE.getRGB());

        super.drawComponent(scaledResolution, mouseX, mouseY);

        if (isExpanded()) {
            for (Component component : components) {
                component.setY(height);
                component.drawComponent(scaledResolution, mouseX, mouseY);
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                    }
                }
                height += cHeight;
            }
        }
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        if (button == 0 && !this.dragging) {
            //dragging = true;
            prevX = mouseX - getX();
            prevY = mouseY - getY();
        }
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        int y2 = 17;
        ArrayList<Config> cfg = (ArrayList<Config>) Celestial.instance.configManager.getContents();
        cfg.sort(Comparator.comparingInt(m -> Minecraft.getMinecraft().mntsb_15.getStringWidth(((Config)m).getName())).reversed());
        float offset = 0;
        offset = anim;

        for (Config c : cfg) {
        	if (button == 0 && mouseX >x - 3 + 2  && mouseY > y + height / 2F - 2 - 5 + y2 + offset && mouseX <x - 3 + 2 + width + 1 - 4 && mouseY < y + height / 2F - 2 - 5 + y2 + offset + 15.5f) {
           		sel = c;
           	}
        	y2 += 20;
        }
    }


    @Override
    public void onMouseRelease(int button) {
        super.onMouseRelease(button);
        dragging = false;
    }

    @Override
    public boolean canExpand() {
        return !features.isEmpty();
    }

    @Override
    public int getHeightWithExpand() {
        int height = getHeight();
        if (isExpanded()) {
            for (Component component : components) {
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                }
                height += cHeight;
            }
        }
        return height;
    }
}
