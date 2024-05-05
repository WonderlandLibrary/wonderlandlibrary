package tech.atani.client.feature.guis.screens.clickgui.ryu.frame;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Mouse;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.guis.screens.clickgui.ryu.component.Component;
import tech.atani.client.feature.guis.screens.clickgui.ryu.component.impl.ModuleComponent;
import tech.atani.client.utility.interfaces.ColorPalette;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Frame extends Component implements ColorPalette {

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
        FontRenderer normal = FontStorage.getInstance().findFont("Roboto", 19);
        modules.sort(Comparator.comparingInt(o -> normal.getStringWidthInt(((Module)o).getName())).reversed());
        for(Module module : modules) {
            this.subComponents.add(new ModuleComponent(module, posX, moduleY, width, moduleHeight));
            moduleY += moduleHeight;
        }
    }

    public void renderShader() {
        float moduleY = this.getPosY() + this.getBaseHeight();
        for(Component component : this.subComponents) {
            if(component instanceof ModuleComponent) {
                if(((ModuleComponent)component).getModule().shouldHide())
                    continue;
                component.setPosX(this.getPosX());
                component.setPosY(moduleY + getAddY());
                component.setAddX(this.getAddX());
                //component.drawScreen(mouseX, mouseY);
                moduleY += component.getFinalHeight();
            }
        }
        final float finalY = moduleY;
        RoundedShader.drawRoundOutline(getPosX() + getAddX(), getPosY() + getAddY(), getBaseWidth(), finalY - getPosY(), 7,  2, new Color(0, 0, 0, 0), new Color(0, 0, 0, 255));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        if(!Mouse.isButtonDown(0))
            dragging = false;
        if(dragging) {
            this.setPosY(mouseY - draggingY);
            this.setPosX(mouseX - draggingX);
        }
        float moduleY = this.getPosY() + this.getBaseHeight();
        for(Component component : this.subComponents) {
            if(component instanceof ModuleComponent) {
                if(((ModuleComponent)component).getModule().shouldHide())
                    continue;
                component.setPosX(this.getPosX());
                component.setPosY(moduleY + getAddY());
                component.setAddX(this.getAddX());
                //component.drawScreen(mouseX, mouseY);
                moduleY += component.getFinalHeight();
            }
        }
        final float finalY = moduleY;
        RoundedShader.drawRoundOutline(getPosX() + getAddX(), getPosY() + getAddY(), getBaseWidth(), moduleY - getPosY(), 7,  2, new Color(RYU), new Color(-1));
        RoundedShader.drawRoundOutline(getPosX() + getAddX(), getPosY() + getAddY() + getBaseHeight(), getBaseWidth(), moduleY - getPosY() - getBaseHeight(), 7,  2, new Color(36, 37, 41), new Color(36, 37, 41, 0));
        FontRenderer medium24 = FontStorage.getInstance().findFont("Roboto Medium", 24);

        medium24.drawString(category.getName(), this.getPosX() + 7 + getAddX(), this.getPosY() + 6 + getAddY(), -1);
        ArrayList<Module> modules2 = ModuleStorage.getInstance().getModules(category);
        modules2.removeIf(module -> module.getCategory() == Category.SERVER && !module.correctServer());
        FontStorage.getInstance().findFont("Roboto Medium", 17).drawString(ChatFormatting.GRAY.toString() + modules2.size() + "", this.getPosX() + 7 + medium24.getStringWidthInt(category.getName()) + 2 + getAddX(), this.getPosY() + 6 + getAddY(), -1);
        for(Component component : this.subComponents) {
            if(component instanceof ModuleComponent) {
                if(((ModuleComponent)component).getModule().shouldHide())
                    continue;
                component.drawScreen(mouseX, mouseY);
            }
        }
        RoundedShader.drawRoundOutline(getPosX() + getAddX(), getPosY() + getAddY(), getBaseWidth(), moduleY - getPosY(), 7,  2, new Color(0, 0, 0, 0), new Color(-1));
    }

    @Override
    public float getFinalHeight() {
        float totalComponentHeight = 0;
        for(Component component : this.subComponents) {
            if(component instanceof ModuleComponent) {
                if(((ModuleComponent)component).getModule().shouldHide())
                    continue;
            }
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
