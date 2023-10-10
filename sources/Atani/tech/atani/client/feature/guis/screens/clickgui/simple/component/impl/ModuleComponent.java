package tech.atani.client.feature.guis.screens.clickgui.simple.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.guis.screens.clickgui.simple.window.Window;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.value.storage.ValueStorage;
import tech.atani.client.utility.render.RenderUtil;

import java.awt.*;

public class ModuleComponent extends tech.atani.client.feature.guis.screens.clickgui.simple.component.Component {

    private final Module module;
    private GuiScreen parent;

    public ModuleComponent(Module module, float posX, float posY, float width, float height, GuiScreen parent) {
        super(posX, posY, width, height);
        this.module = module;
        this.parent = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        FontRenderer normal = FontStorage.getInstance().findFont("Roboto", 19);
        RenderUtil.drawRect(getPosX() + getAddX(), getPosY(), getBaseWidth(), getBaseHeight(), new Color(0, 0, 0, 180).getRGB());
        normal.drawTotalCenteredStringWithShadow(module.getName(), getPosX() + getBaseWidth() / 2 + getAddX(), getPosY() + getBaseHeight() / 2, !module.isEnabled() ? new Color(200, 200, 200).getRGB() : -1);
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, this.getPosX(), this.getPosY(), this.getBaseWidth(), this.getBaseHeight())) {
            switch (mouseButton) {
                case 0:
                    module.toggle();
                    break;
                case 1:
                    if(!ValueStorage.getInstance().getValues(module).isEmpty())
                        Minecraft.getMinecraft().displayGuiScreen(new Window(getModule(), parent));
                    break;
            }
        }
    }

    @Override
    public float getFinalHeight() {
        return this.getBaseHeight();
    }

    public Module getModule() {
        return module;
    }
}
