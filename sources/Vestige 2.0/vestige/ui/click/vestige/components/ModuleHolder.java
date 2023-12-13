package vestige.ui.click.vestige.components;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import vestige.Vestige;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.font.FontUtil;
import vestige.font.MinecraftFontRenderer;
import vestige.impl.module.render.ClickGuiModule;
import vestige.ui.click.vestige.VestigeClickGUI;
import vestige.util.base.IMinecraft;
import vestige.util.misc.LogUtil;
import vestige.util.misc.TimerUtil;
import vestige.util.render.ColorUtil;

import java.awt.*;
import java.util.ArrayList;

import lombok.Getter;

public class ModuleHolder implements IMinecraft {

    private VestigeClickGUI parent;
    private Category category;
    
    @Getter
    private Module module;
    
    private final ArrayList<SettingHolder> settings = new ArrayList<>();
    private TimerUtil timer;
    private boolean settingsShown;
    private final double offsetY = 18;

    public ModuleHolder(VestigeClickGUI parent, Category category, Module module, TimerUtil timer) {
        this.parent = parent;
        this.category = category;
        this.module = module;
        this.timer = timer;
        
        module.getSettings().forEach(s -> settings.add(new SettingHolder(parent, s, timer)));
    }

    public double drawScreen(double x, double y, int mouseX, int mouseY, float partialTicks, boolean holdingMouseButton) {
    	MinecraftFontRenderer fr = FontUtil.product_sans;
        
    	if(module.isEnabled() && !(module instanceof ClickGuiModule)) {
    		ClickGuiModule clickGui = (ClickGuiModule) Vestige.getInstance().getModuleManager().getModule(ClickGuiModule.class);
        	
        	Color color1 = new Color((int) clickGui.red1.getCurrentValue(), (int) clickGui.green1.getCurrentValue(), (int) clickGui.blue1.getCurrentValue());
        	Color color2 = new Color((int) clickGui.red2.getCurrentValue(), (int) clickGui.green2.getCurrentValue(), (int) clickGui.blue2.getCurrentValue());
    		
    		for(double i = x; i < x + 100; i++) {
            	Gui.drawRect(x, y, x + 100, y + offsetY, ColorUtil.customColors2(color1, color2, clickGui.rainbow.isEnabled(), 3, (int) (i * (clickGui.rainbow.isEnabled() ? -0.75 : -1.5))).getRGB());
            }
    	} else {
    		Gui.drawRect(x, y, x + 100, y + offsetY, new Color(42, 42, 42).getRGB());
    	}
    	
    	if(module.getSettings().size() > 1) {
			double Xstart = x + 89;
			double Xend = x + 95;
			double Ystart = y + 7;
			double Yend = y + 12;
			Gui.drawRect(Xstart, Ystart, Xend, Ystart + 0.5, new Color(225, 225, 225).getRGB());
			Gui.drawRect(Xstart + 0.5, Ystart + 0.5, Xend - 0.5, Ystart + 1, new Color(225, 225, 225).getRGB());
			Gui.drawRect(Xstart + 1, Ystart + 1, Xend - 1, Ystart + 1.5, new Color(225, 225, 225).getRGB());
			Gui.drawRect(Xstart + 1.5, Ystart + 1.5, Xend - 1.5, Ystart + 2, new Color(225, 225, 225).getRGB());
			Gui.drawRect(Xstart + 2, Ystart + 2, Xend - 2, Ystart + 2.5, new Color(225, 225, 225).getRGB());
			Gui.drawRect(Xstart + 2.5, Ystart + 2.5, Xend - 2.5, Ystart + 3, new Color(225, 225, 225).getRGB());
		}
    	
        fr.drawStringWithShadow(module.getName(), (float) (x + 5), (float) (y + 5), new Color(220, 220, 220).getRGB());
        
        double finalY = offsetY;
        
        if(settingsShown) {
        	for(SettingHolder s : settings) {
        		finalY += s.drawScreen(x, y + finalY, mouseX, mouseY, partialTicks, holdingMouseButton);
        	}
        }
        
        return finalY;
    }

    public double mouseClicked(double x, double y, int mouseX, int mouseY, int button) {
    	boolean shouldRenderSettings = settingsShown;
    	
    	if(mouseX > x && mouseX < x + 100 && mouseY > y && mouseY < y + 18) {
    		if(button == 0 && !(module instanceof ClickGuiModule)) {
    			module.toggle();
    		} else if(button == 1) {
    			shouldRenderSettings = settingsShown = !settingsShown;
    		}
    	}
    	
    	double finalY = offsetY;
    	
    	if(shouldRenderSettings) {
    		for(SettingHolder s : settings) {
        		finalY += s.mouseClicked(x, y + finalY, mouseX, mouseY, button);
        	}
    	}
    	
        return finalY;
    }
    
    public void keyTyped(char typedChar, int keyCode) {
    	settings.forEach(s -> s.keyTyped(typedChar, keyCode));
    }

}
