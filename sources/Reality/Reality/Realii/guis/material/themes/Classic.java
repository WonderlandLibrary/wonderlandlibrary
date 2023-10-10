package Reality.Realii.guis.material.themes;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import Reality.Realii.Client;
import Reality.Realii.guis.clickguisex.ModuleWindow;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.guis.material.Category;
import Reality.Realii.guis.material.Main;
import Reality.Realii.guis.material.Tab;
import Reality.Realii.guis.material.Tabs.SettingsTab;
import Reality.Realii.guis.material.button.CButton;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.mods.modules.render.ClickGui;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.render.ColorUtils;
import Reality.Realii.utils.render.RenderUtil;
import Reality.Realii.utils.render.renderManager.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

public class Classic extends Main {

	
	
    @Override
    public void initGui() {
        super.initGui();
        if (OpenGlHelper.shadersSupported) {
    		if (mc.entityRenderer.theShaderGroup != null) {
    			mc.entityRenderer.theShaderGroup.deleteShaderGroup();
    		}
    	
    		mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    	}
        
        Blist = new CButton("Modules", "client/clickgui/modules.png", 2, 4, 12, 8);
        Btheme = new CButton("Modules", "client/clickgui/theme.png", 3, 3, 11, 11);
        Bsettings = new CButton("Modules", "client/clickgui/settings.png", 3, 3, 10.5f, 10.5f);
      
        categories.clear();
        for (ModuleType mt : ModuleType.values()) {
            categories.add(new Category(mt, 0, false));
        }

    }
    
    @Override
	public void onGuiClosed() {
		
		if (mc.entityRenderer.theShaderGroup != null) {
			mc.entityRenderer.theShaderGroup.deleteShaderGroup();
			mc.entityRenderer.theShaderGroup = null;
		}
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }
    
    public float smoothTrans(double current, double last) {
        return (float) (current + (last - current) / (Minecraft.getDebugFPS() / 10));
    }


    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }



    @Override
    public void drawTasksBar() {
        super.drawTasksBar();
      
        float x = 4;
        for (Tab t : tabs) {
            if (currentTab == t)
                t.render(mouseX, mouseY);

            float swidth = FontLoaders.arial16.getStringWidth(t.name) + 14;
            t.x = t.animationUtils.animate(windowX + x + animListX, t.x, drag ? 2 : 0.1F);
            new Rect(t.x, windowY + 30, swidth, 20, new Color(0, 0, 0, 0), new Runnable() {
                @Override
                public void run() {
                    if (Mouse.isButtonDown(0))
                        currentTab = t;
                }
            }).render(mouseX, mouseY);


            if (isHovered(t.x + swidth - 4, windowY + 30, t.x + swidth + 4, windowY + 50, mouseX, mouseY)) {
                FontLoaders.arial18.drawString("-", t.x + swidth - 6, windowY + 40, new Color(255, 0, 0).getRGB());
            } else {
                FontLoaders.arial18.drawString("-", t.x + swidth - 6, windowY + 40, new Color(255, 255, 255).getRGB());
            }

            if (t == currentTab) {
                FontLoaders.arial16.drawString(t.name, t.x + 2, windowY + 40, -1);
             
            } else {
                FontLoaders.arial16.drawString(t.name, t.x + 2, windowY + 40, new Color(255, 255, 255, 150).getRGB());
            }

            x += swidth;
        }

        if (Bsettings.realized) {
            Bsettings.realized = false;
            for (Tab tab: tabs){
                if (tab.name.equals("Settings")) {
                    currentTab = tab;
                    return;
                }
            }

            this.tabs.add(new SettingsTab());
        }
    }

    @Override
    public void drawList(float mouseX, float mouseY) {
        super.drawList(mouseX, mouseY);
        //if (Blist.realized) {
            animListX = listAnim.animate(140, animListX, 0.2f);
            
        if (animListX != 0) {
        	Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue(),40); 
     		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue(),40);
       	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
       	  int i23 = 0;
       	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i23 * 20), colors.getFirst(), colors.getSecond(), false);
            RenderUtil.drawRect(windowX, windowY + 30, windowX + animListX, windowY + windowHeight, c);
            RenderUtil.drawGradientSideways(windowX + animListX, windowY + 30, windowX + animListX + 3, windowY + windowHeight, new Color(50, 50, 50, 0).getRGB(), new Color(255, 255, 255, 0).getRGB());
            float dWheel = Mouse.getDWheel();



            if (dWheel > 0 && listRoll2 < 0) {
                listRoll2 += 32;
            } else if (dWheel < 0) {
                listRoll2 -= 32;
            }

            listRoll = rollAnim.animate(listRoll2, listRoll, 0.05f);

            float modsY = windowY + 35 + listRoll;


            for (Category mt : categories) {
                if (mt.show || mt.needRemove) {
                	
                    new Rect(windowX, modsY, animListX, 20, new Color(10, 10, 10, 0), new Runnable() {
                        @Override
                        public void run() {
                        }
                    }).render(mouseX, mouseY);
                    FontLoaders.arial18.drawString(mt.moduleType.name(), windowX + animListX - 130, modsY + 5, new Color(255, 255, 255).getRGB());
                    modsY += 25;
                    
                    mt.modsY2 = 0;
                    for (Module m : Client.instance.getModuleManager().getModulesInType(mt.moduleType)) {
                        new Rect(windowX, modsY + mt.modsY2, animListX, 15, new Color(10, 10, 10, 0), new Runnable() {
                            @Override
                            public void run() {
                            }
                        }).render(mouseX, mouseY);
                        if (modsY + 5 + mt.modsY2 < modsY + mt.modsY3 + 25)
                            FontLoaders.arial18.drawString(m.getName(), windowX + animListX - 120, modsY + 5 + mt.modsY2, new Color(255, 255, 255).getRGB());
                        mt.modsY2 += 20;
                    }

                    if (mt.needRemove) {
                        mt.modsY3 = mt.rollAnim2.animate(-25, mt.modsY3, 0.1f);
                        if (mt.modsY3 == -25) {
                            mt.needRemove = false;
                            mt.show = false;
                        }
                    } else {
                        mt.modsY3 = mt.rollAnim2.animate(mt.modsY2, mt.modsY3, 0.1f);
                    }

                    modsY += mt.modsY3;
                } else {
                    new Rect(windowX, modsY, animListX, 20, new Color(10, 10, 10, 0), new Runnable() {
                        @Override
                        public void run() {
                        }
                    }).render(mouseX, mouseY);
                    FontLoaders.arial18.drawString(mt.moduleType.name(), windowX + animListX - 130, modsY + 5, new Color(255, 255, 255).getRGB());
                  

                }

                modsY += 25;
            }
        }

    }

    @Override
    public void drawWindow(float mouseX, float mouseY) {
    	
    	 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
  		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
    	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
    	  int i23 = 0;
    	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i23 * 20), colors.getFirst(), colors.getSecond(), false);
    	 // RenderUtil.drawBorderedRect(x, y - 5, x + 65, y - 5 + animation, 0.5f, c.getRGB(), new Color(0, 0, 0, 0).getRGB());
    	//drawBorderedRect
        //70
     
        RenderUtil.drawBorderedRect(windowX, windowY, windowX + windowWidth, windowY + windowHeight, 2, c.getRGB() , new Color(10, 10, 10, 150).getRGB());
        if (ClickGui.LexiMode.getValue().equals("Reality")) {
        RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/Reality.png"), new Color(255, 255, 255).getRGB(), 300);
        }
        
        else  if (ClickGui.LexiMode.getValue().equals("Astolfo")) {
            RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/asstolfo.png"), new Color(255, 255, 255).getRGB(), 300);
            }
        
        else if (ClickGui.LexiMode.getValue().equals("Astolfo2")) {
            RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/astoflo4.png"), new Color(255, 255, 255).getRGB(), 300);
            }
        
        else if (ClickGui.LexiMode.getValue().equals("Astolfo3")) {
            RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/astolfo4.png"), new Color(255, 255, 255).getRGB(), 300);
            }
        else  if (ClickGui.LexiMode.getValue().equals("Astolfo4")) {
            RenderUtil.drawCustomImageAlpha(690, 300, 300, 200, new ResourceLocation("client/AnimeGirluwu/astolfo5.png"), new Color(255, 255, 255).getRGB(), 300);
            }
        else   if (ClickGui.LexiMode.getValue().equals("Ihassesdich")) {
            RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/catgirl.png"), new Color(255, 255, 255).getRGB(), 300);
            }
        else if (ClickGui.LexiMode.getValue().equals("NotHot")) {
            RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/felix.png"), new Color(255, 255, 255).getRGB(), 300);
            }
        else if (ClickGui.LexiMode.getValue().equals("Idk")) {
            RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/idk.png"), new Color(255, 255, 255).getRGB(), 300);
            }
        else  if (ClickGui.LexiMode.getValue().equals("Hideri")) {
            RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/hideri.png"), new Color(255, 255, 255).getRGB(), 300);
            }
        
        //RenderUtil.drawCustomImageAlpha(430, 110, 100, 40, new ResourceLocation("client/Real.png"), new Color(255, 255, 255).getRGB(), 300);
        
        Color clientColor12 = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue(), 0);
        new Rect(windowX, windowY, windowWidth, 30,  clientColor12, new Runnable() {
            @Override
            public void run() {
                if (!Mouse.isButtonDown(0))
                    return;
                drag = true;
                if (mouseDX == 0) {
                    mouseDX = mouseX - windowX;
                }
                if (mouseDY == 0) {
                    mouseDY = mouseY - windowY;
                }
//                mouseDX = 0;
//                mouseDY = 0;
            }
        }).render(mouseX, mouseY);
    }
}
