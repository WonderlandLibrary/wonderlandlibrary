/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.arithmo.gui.click;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.arithmo.gui.click.components.MainPanel;
import me.arithmo.gui.click.ui.Menu;
import me.arithmo.gui.click.ui.Sigma;
import me.arithmo.gui.click.ui.UI;
import me.arithmo.management.animate.Opacity;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class ClickGui
extends GuiScreen {
    private MainPanel mainPanel;
    public static Menu menu;
    private List<UI> themes = new CopyOnWriteArrayList<UI>();
    private Opacity opacity = new Opacity(0);

    public List<UI> getThemes() {
        return this.themes;
    }

    public ClickGui() {
        this.themes.add(new Sigma());
        this.mainPanel = new MainPanel("Exhibition", 50.0f, 50.0f, this.themes.get(0));
        this.themes.get(0).mainConstructor(this, this.mainPanel);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution res = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.opacity.interpolate(100);
        RenderingUtil.rectangle(0.0, 0.0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(0, (int)this.opacity.getOpacity()));
        this.mainPanel.draw(mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        this.mainPanel.mouseMovedOrUp(mouseX, mouseY, mouseButton);
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        try {
            this.mainPanel.mouseClicked(mouseX, mouseY, clickedButton);
            super.mouseClicked(mouseX, mouseY, clickedButton);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        if (Keyboard.getEventKeyState()) {
            this.mainPanel.keyPressed(Keyboard.getEventKey());
        }
    }

    @Override
    public void onGuiClosed() {
        this.opacity.setOpacity(0.0f);
        this.themes.get(0).onClose();
    }
}

