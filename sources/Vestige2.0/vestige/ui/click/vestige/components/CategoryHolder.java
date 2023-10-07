package vestige.ui.click.vestige.components;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import vestige.Vestige;
import vestige.api.module.Category;
import vestige.font.FontUtil;
import vestige.font.MinecraftFontRenderer;
import vestige.impl.module.render.ClickGuiModule;
import vestige.ui.click.vestige.VestigeClickGUI;
import vestige.util.base.IMinecraft;
import vestige.util.misc.LogUtil;
import vestige.util.misc.TimerUtil;
import vestige.util.render.ColorUtil;

import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;

public class CategoryHolder implements IMinecraft {

    private VestigeClickGUI parent;
    private Category category;
    private final ArrayList<ModuleHolder> modules = new ArrayList<>();
    private TimerUtil timer;
    
    private double x, y;
    private int lastMouseX, lastMouseY;
    
    private boolean holdingCategory;
    
    public CategoryHolder(VestigeClickGUI parent, Category category, TimerUtil timer, double x, double y) {
        this.parent = parent;
        this.category = category;
        this.timer = timer;
        this.x = x;
        this.y = y;

        Vestige.getInstance().getModuleManager().getModules().stream().filter(
                m -> m.getCategory() == category
        ).forEach(
                m -> modules.add(new ModuleHolder(parent, category, m, timer))
        );
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks, boolean holdingMouseButton) {
    	MinecraftFontRenderer fr = FontUtil.product_sans;
    	
    	if(parent.isHoldingMouseButton() && holdingCategory) {
    		x += mouseX - lastMouseX;
    		y += mouseY - lastMouseY;
    	} else {
    		holdingCategory = false;
    	}
    	
    	double renderX = x;
    	double renderY = y;
        
    	ClickGuiModule clickGui = (ClickGuiModule) Vestige.getInstance().getModuleManager().getModule(ClickGuiModule.class);
    	
    	Color color1 = new Color((int) clickGui.red1.getCurrentValue(), (int) clickGui.green1.getCurrentValue(), (int) clickGui.blue1.getCurrentValue());
    	Color color2 = new Color((int) clickGui.red2.getCurrentValue(), (int) clickGui.green2.getCurrentValue(), (int) clickGui.blue2.getCurrentValue());
    	
        for(double i = renderX; i < renderX + 100; i++) {
        	//Gui.drawRect(renderX, renderY, renderX + 100, renderY + 20, ColorUtil.getVestigeColors2(3, (int) (i * -1.5)).getRGB());
        	Gui.drawRect(renderX, renderY, renderX + 100, renderY + 20, ColorUtil.customColors2(color1, color2, clickGui.rainbow.isEnabled(), 3, (int) (i * (clickGui.rainbow.isEnabled() ? -0.75 : -1.5))).getRGB());
        }

        fr.drawStringWithShadow(category.getName(), (float) (x + 50 - fr.getStringWidth(category.getName()) / 2), (float) (y + 6), -1);

        renderY += 20;
        
        boolean firstModule = true;
        
        for (ModuleHolder m : modules) {
        	double previousY = renderY;
        	
        	renderY += m.drawScreen(renderX, renderY, mouseX, mouseY, partialTicks, holdingMouseButton);
            if(firstModule) {
            	if(m.getModule().isEnabled()) {
            		parent.drawGradientRect((int) renderX, (int) previousY, (int) renderX + 100, (int) previousY + 3, 0x60000000, 0x05000000);
            	}
            	firstModule = false;
            }
        }
        
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
    	double renderX = x;
    	double renderY = y;
    	renderY += 20;
        
        holdingCategory = mouseX > x && mouseX < x + 100 && mouseY > y && mouseY < y + 20;
        
        for(ModuleHolder m : modules) {
            renderY += m.mouseClicked(renderX, renderY, mouseX, mouseY, button);
        }
    }
    
    public void keyTyped(char typedChar, int keyCode) {
    	modules.forEach(m -> m.keyTyped(typedChar, keyCode));
    }
    
}
