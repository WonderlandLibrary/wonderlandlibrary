package vestige.ui.click.simple.components;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import vestige.Vestige;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.ui.click.simple.SimpleClickGUI;
import vestige.util.base.IMinecraft;
import vestige.util.misc.LogUtil;
import vestige.util.misc.TimerUtil;

import java.awt.*;
import java.util.ArrayList;

public class ModuleHolder implements IMinecraft {

    private SimpleClickGUI parent;
    private Category category;
    private Module module;
    private final ArrayList<SettingHolder> settings = new ArrayList<>();
    private TimerUtil timer;
    private boolean settingsShown;
    private final double offsetY = 16;

    public ModuleHolder(SimpleClickGUI parent, Category category, Module module, TimerUtil timer) {
        this.parent = parent;
        this.category = category;
        this.module = module;
        this.timer = timer;
        
        module.getSettings().forEach(s -> settings.add(new SettingHolder(parent, s, timer)));
    }

    public double drawScreen(double x, double y, int mouseX, int mouseY, float partialTicks, boolean holdingMouseButton) {
    	FontRenderer fr = mc.fontRendererObj;

        Gui.drawRect(x, y, x + 100, y + offsetY, module.isEnabled() ? Vestige.getInstance().getClientColor().getRGB() : 0x90000000);
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
    		if(button == 0) {
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
