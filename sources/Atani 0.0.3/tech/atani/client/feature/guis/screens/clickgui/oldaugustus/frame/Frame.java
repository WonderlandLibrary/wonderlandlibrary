package tech.atani.client.feature.guis.screens.clickgui.oldaugustus.frame;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Mouse;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.guis.screens.clickgui.oldaugustus.frame.component.Component;
import tech.atani.client.feature.guis.screens.clickgui.oldaugustus.frame.component.impl.ModuleComponent;
import tech.atani.client.utility.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Frame extends tech.atani.client.feature.guis.screens.clickgui.oldaugustus.frame.component.Component {

    private final Category category;
    private final float moduleHeight;
    private float draggingX, draggingY;
    private boolean dragging;

    public Frame(Category category, float posX, float posY, float width, float height, float moduleHeight) {
        super(posX, posY, width, height);
        this.category = category;
        this.moduleHeight = moduleHeight;

        // The Y position in here is basically useless as the actual Y pos is overwritten in drawScreen
        float moduleY = posY + height;
        ArrayList<Module> modules = ModuleStorage.getInstance().getModules(this.category);
        FontRenderer normal = Minecraft.getMinecraft().fontRendererObj;
        modules.sort(Comparator.comparingInt(o -> normal.getStringWidthInt(((Module)o).getName())).reversed());
        for(Module module : modules) {
            this.subComponents.add(new ModuleComponent(module, posX + 2, moduleY, width - 4, moduleHeight));
            moduleY += moduleHeight;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        if(!Mouse.isButtonDown(0))
            dragging = false;
        if(dragging) {
            this.setPosY(mouseY - draggingY);
            this.setPosX(mouseX - draggingX);
        }
        RenderUtil.drawRect(getPosX() + getAddX(), getPosY() + getAddY(), getBaseWidth(), getBaseHeight(), new Color(41, 146, 222).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawTotalCenteredStringWithShadow(category.getName(), this.getPosX() + this.getBaseWidth() / 2 + getAddX(), this.getPosY() + this.getBaseHeight() / 2 + getAddY(), -1);
        float moduleY = this.getPosY() + this.getBaseHeight();
        for(tech.atani.client.feature.guis.screens.clickgui.oldaugustus.frame.component.Component component : this.subComponents) {
            if(component instanceof ModuleComponent) {
                if(((ModuleComponent)component).getModule().shouldHide())
                    continue;
                component.setPosX(this.getPosX() + 2);
                component.setPosY(moduleY + getAddY());
                component.setAddX(this.getAddX());
                component.drawScreen(mouseX, mouseY);
                moduleY += component.getFinalHeight();
            }
        }
    }

    @Override
    public float getFinalHeight() {
        float totalComponentHeight = 0;
        for(tech.atani.client.feature.guis.screens.clickgui.oldaugustus.frame.component.Component component : this.subComponents) {
            totalComponentHeight += component.getFinalHeight();
        }
        return this.getBaseHeight() + totalComponentHeight;
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isHovered(mouseX, mouseY, getPosX() + getAddX(), getPosY() + getAddY(), getBaseWidth(), getBaseHeight()) && mouseButton == 0) {
            dragging = true;
            draggingX = mouseX - getPosX();
            draggingY = mouseY - getPosY();
        }
        float moduleY = this.getPosY() + this.getBaseHeight();
        for(Component component : this.subComponents) {
            if(component instanceof ModuleComponent) {
                 if(((ModuleComponent)component).getModule().shouldHide())
                    continue;
                component.setPosY(moduleY + getAddY());
                component.mouseClick(mouseX, mouseY, mouseButton);
                moduleY += component.getFinalHeight();
            }
        }
    }

}
