package vestige.ui.click.simple.components;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import vestige.Vestige;
import vestige.api.module.Category;
import vestige.ui.click.simple.SimpleClickGUI;
import vestige.util.base.IMinecraft;
import vestige.util.misc.TimerUtil;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class CategoryHolder implements IMinecraft {

    private SimpleClickGUI parent;
    private Category category;
    private final ArrayList<ModuleHolder> modules = new ArrayList<>();
    private TimerUtil timer;

    public CategoryHolder(SimpleClickGUI parent, Category category, TimerUtil timer) {
        this.parent = parent;
        this.category = category;
        this.timer = timer;

        Vestige.getInstance().getModuleManager().getModules().stream().filter(
                m -> m.getCategory() == category
        ).forEach(
                m -> modules.add(new ModuleHolder(parent, category, m, timer))
        );
    }

    public void drawScreen(double x, double y, int mouseX, int mouseY, float partialTicks, boolean holdingMouseButton) {
    	FontRenderer fr = mc.fontRendererObj;

        Gui.drawRect(x, y, x + 100, y + 18, Vestige.getInstance().getClientColor().getRGB());

        fr.drawStringWithShadow(category.getName(), (float) (x + 50 - fr.getStringWidth(category.getName()) / 2), (float) (y + 5), -1);

        y += 18;
        
        if (!modules.isEmpty()) {
        	//parent.drawGradientRect((int) x, (int) y, (int) (x + 100), (int) y + 3, 0x60000000, 0x05000000);
        }

        for (ModuleHolder m : modules) {
            y += m.drawScreen(x, y, mouseX, mouseY, partialTicks, holdingMouseButton);
        }
    }

    public void mouseClicked(double x, double y, int mouseX, int mouseY, int button) {
        y += 18;
        for(ModuleHolder m : modules) {
            y += m.mouseClicked(x, y, mouseX, mouseY, button);
        }
    }
    
    public void keyTyped(char typedChar, int keyCode) {
    	modules.forEach(m -> m.keyTyped(typedChar, keyCode));
    }

}
