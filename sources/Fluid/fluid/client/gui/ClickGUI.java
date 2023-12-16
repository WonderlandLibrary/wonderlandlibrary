// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.gui;

import java.util.Iterator;
import fluid.client.mods.GuiMod;
import fluid.client.Client;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class ClickGUI extends GuiScreen
{
    public GuiScreen prevScreen;
    public boolean clicked;
    
    public ClickGUI(final GuiScreen prevScreen) {
        this.clicked = false;
        this.prevScreen = prevScreen;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(1, this.width / 2 + 105, this.height / 2 - 145, 15, 15, "X"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 40, this.height / 2 - 145, 60, 15, "Hud Editor"));
        super.initGui();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawScreen();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.prevScreen);
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiConfigScreen());
        }
        super.actionPerformed(button);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.drawScreen(mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public void drawScreen(final int mouseX, final int mouseY) {
        Gui.drawRect(this.width / 2 - 125, this.height / 2 + 150, this.width / 2 + 125, this.height / 2 - 150, new Color(0, 0, 0, 200).getRGB());
        Gui.drawRect(this.width / 2 - 125, this.height / 2 - 130, this.width / 2 + 125, this.height / 2 - 129, new Color(128, 128, 128, 255).getRGB());
        int row = 0;
        int col = 0;
        for (final GuiMod mod : Client.INSTANCE.modManager.guiModList) {
            Gui.drawRect(this.width / 2 - 115 + col * 90, this.height / 2 - 120 + row * 60, this.width / 2 - 60 + col * 90, this.height / 2 - 100 + row * 60, new Color(0, 0, 0, 255).getRGB());
            final int centerX = (this.width / 2 - 115 + col * 90 + (this.width / 2 - 60) + col * 90) / 2;
            final int centerY = (this.height / 2 - 120 + row * 60 + this.height / 2 - 100 + row * 60) / 2;
            this.drawCenteredString(this.mc.fontRendererObj, mod.name, centerX, centerY, -1);
            final int color = mod.enabled ? new Color(0, 182, 18).getRGB() : new Color(139, 0, 0).getRGB();
            Gui.drawRect(this.width / 2 - 115 + col * 90, this.height / 2 - 100 + row * 50, this.width / 2 - 60 + col * 90, this.height / 2 - 90 + row * 50, color);
            if (mouseX >= this.width / 2 - 115 + col * 90 && mouseX <= this.width / 2 - 60 + col * 90 && mouseY >= this.height / 2 - 100 + row * 50 && mouseY <= this.height / 2 - 90 + row * 50) {
                mod.toggle();
            }
            if (++col == 3) {
                ++row;
                col = 0;
            }
        }
    }
    
    public void drawScreen() {
        Gui.drawRect(this.width / 2 - 125, this.height / 2 + 150, this.width / 2 + 125, this.height / 2 - 150, new Color(0, 0, 0, 200).getRGB());
        Gui.drawRect(this.width / 2 - 125, this.height / 2 - 130, this.width / 2 + 125, this.height / 2 - 129, new Color(128, 128, 128, 255).getRGB());
        int row = 0;
        int col = 0;
        for (final GuiMod mod : Client.INSTANCE.modManager.guiModList) {
            Gui.drawRect(this.width / 2 - 115 + col * 90, this.height / 2 - 120 + row * 50, this.width / 2 - 60 + col * 90, this.height / 2 - 100 + row * 50, new Color(0, 0, 0, 255).getRGB());
            final int centerX = (this.width / 2 - 115 + col * 90 + (this.width / 2 - 60) + col * 90) / 2;
            final int centerY = (this.height / 2 - 120 + row * 50 + this.height / 2 - 100 + row * 50) / 2;
            this.drawCenteredString(this.mc.fontRendererObj, mod.name, centerX, centerY, -1);
            final int color = mod.enabled ? new Color(0, 182, 18).getRGB() : new Color(139, 0, 0).getRGB();
            Gui.drawRect(this.width / 2 - 115 + col * 90, this.height / 2 - 100 + row * 50, this.width / 2 - 60 + col * 90, this.height / 2 - 90 + row * 50, color);
            if (++col == 3) {
                ++row;
                col = 0;
            }
        }
    }
    
    @Override
    public void onGuiClosed() {
        try {
            Client.INSTANCE.configManager.save();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        super.onGuiClosed();
    }
}
