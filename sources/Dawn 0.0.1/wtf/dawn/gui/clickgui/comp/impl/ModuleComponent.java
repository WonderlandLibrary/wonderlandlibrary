package wtf.dawn.gui.clickgui.comp.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import wtf.dawn.module.Module;
import wtf.dawn.ui.Screen;
import wtf.dawn.ui.annotations.Component;
import wtf.dawn.ui.components.BasicComponent;

import java.awt.*;

@Component(name = "Module Component", componentId = 2)
public class ModuleComponent extends BasicComponent {

    Module moduleParent;

    public ModuleComponent(int x, int y, int w, int h, boolean outline, int typeId, Module moduleIn) {
        super(x, y, w, h, outline, typeId);

        this.moduleParent = moduleIn;
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        super.renderComponent(mouseX, mouseY);

        if(mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
            Gui.drawRect(x + 1, y, x + width - 1, y + height, moduleParent.isEnabled() ? new Color(108, 145, 200).getRGB() : new Color(108, 145, 210).getRGB());
        } else
            Gui.drawRect(x + 1, y, x + width - 1, y + height, moduleParent.isEnabled() ? new Color(108, 145, 200).getRGB() : new Color(108, 145, 210).getRGB());

        Screen.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, moduleParent.getDisplayName().toLowerCase(), (int) (x + ((float) width / 2)), (int) (y + ((float) height / 2 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2)), moduleParent.isEnabled() ? Color.cyan.getRGB() : Color.white.getRGB());


    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {
        super.onClick(mouseX, mouseY, mouseButton);

        if(mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
            moduleParent.setEnabled(!moduleParent.isEnabled());
        }

    }
}
