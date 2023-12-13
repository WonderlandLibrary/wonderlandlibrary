/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui.clickgui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import tk.rektsky.gui.GuiKeybind;
import tk.rektsky.gui.clickgui.AstolfoClickGui;
import tk.rektsky.gui.clickgui.components.AstolfoButton;
import tk.rektsky.module.Module;
import tk.rektsky.utils.obf.wrapper.DisplayGuiScreen;

public class AstolfoBindButton
extends AstolfoButton {
    public Module module;

    public AstolfoBindButton(float x2, float y2, float width, float height, Module module) {
        super(x2, y2, width, height);
        this.module = module;
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), -299621581);
        String string = "Bind " + Keyboard.getKeyName(this.module.keyCode);
        float f2 = this.y + this.height / 2.0f;
        AstolfoClickGui.settingsFont.getClass();
        AstolfoClickGui.settingsFont.drawString(string, this.x + 8.0f, f2 - 9.0f / 2.0f, -1);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, boolean click, int button) {
        if (this.isHovered(mouseX, mouseY) && click) {
            DisplayGuiScreen.displayGuiScreen(new GuiKeybind(Minecraft.getMinecraft().currentScreen, this.module));
        }
    }
}

