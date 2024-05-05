package tech.atani.client.feature.guis.screens.clickgui.koks.frame;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.guis.screens.clickgui.koks.component.Component;
import tech.atani.client.feature.guis.screens.clickgui.koks.component.impl.ModuleComponent;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.utility.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Frame extends Component {

    private final Category category;
    private final float moduleHeight;
    int scroll;
    public int dWheel;

    public Frame(Category category, float posX, float posY, float width, float height, float moduleHeight, GuiScreen parent) {
        super(posX, posY, width, height);
        this.category = category;
        this.moduleHeight = moduleHeight;

        // The Y position in here is basically useless as the actual Y pos is overwritten in drawScreen
        float moduleY = posY + height;
        ArrayList<Module> modules = ModuleStorage.getInstance().getModules(this.category);
        FontRenderer normal = FontStorage.getInstance().findFont("Roboto", 19);
        modules.sort(Comparator.comparingInt(o -> normal.getStringWidthInt(((Module)o).getName())).reversed());
        for(Module module : modules) {
            this.subComponents.add(new ModuleComponent(module, posX, moduleY, width, moduleHeight, parent));
            moduleY += moduleHeight;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        if(RenderUtil.isHovered(mouseX, mouseY, getPosX(), getPosY(), getBaseWidth(), 25 + 20 * 8) && ModuleStorage.getInstance().getModules(category).size() > 8) {
            if(dWheel < 0) {
                scroll -= 1;
            } else if(dWheel > 0) {
                scroll += 1;
            }
            scroll = Math.min(0, scroll);
            scroll = Math.max(-(ModuleStorage.getInstance().getModules(category).size() - 1 - 8), scroll);
        }
        RenderUtil.drawRect(getPosX() + getAddX(), getPosY() + getAddY(), getBaseWidth(), getBaseHeight(), new Color(21, 23, 24, 255).getRGB());
        FontStorage.getInstance().findFont("Roboto", 31).drawString(category.getName(), this.getPosX() + 5 + getAddX(), this.getPosY() + 10 / 2 + getAddY(), -1);
        float moduleY = this.getPosY() + this.getBaseHeight() + scroll * moduleHeight;
        for(Component component : this.subComponents) {
            if(component instanceof ModuleComponent) {
                if(((ModuleComponent)component).getModule().shouldHide())
                    continue;
                component.setPosX(component.getPosX());
                component.setPosY(moduleY + getAddY());
                component.setAddX(this.getAddX());
                if(!(moduleY < this.getPosY() + this.getBaseHeight()) && !(moduleY > this.getPosY() + this.getBaseHeight() + 8 * moduleHeight)) {
                    component.drawScreen(mouseX, mouseY);
                }
                moduleY += component.getFinalHeight();
            }
        }
    }

    @Override
    public float getFinalHeight() {
        float totalComponentHeight = 0;
        for(Component component : this.subComponents) {
            totalComponentHeight += component.getFinalHeight();
        }
        totalComponentHeight = Math.min(totalComponentHeight, 8 * moduleHeight);
        return this.getBaseHeight() + totalComponentHeight;
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        float moduleY = this.getPosY() + this.getBaseHeight() + scroll * moduleHeight;
        for(Component component : this.subComponents) {
            if(component instanceof ModuleComponent) {
                if(((ModuleComponent)component).getModule().shouldHide())
                    continue;
                component.setPosY(moduleY + getAddY());
                if(!(moduleY < this.getPosY() + this.getBaseHeight()) && !(moduleY > this.getPosY() + this.getBaseHeight() + 8 * moduleHeight)) {
                    component.mouseClick(mouseX, mouseY, mouseButton);
                }
                moduleY += component.getFinalHeight();
            }
        }
    }

}
