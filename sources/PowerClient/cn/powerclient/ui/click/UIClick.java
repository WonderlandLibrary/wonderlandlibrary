/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.click;

import java.io.IOException;
import me.AveReborn.ui.click.buttons.UIPopUPButton;
import me.AveReborn.util.handler.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class UIClick
extends GuiScreen {
    private ClientEventHandler mouseClickedPopUpMenu = new ClientEventHandler();
    private UIPopUPButton uiPopUPButton;
    private ScaledResolution res;
    public boolean initialized;

    @Override
    public void initGui() {
        this.res = new ScaledResolution(this.mc);
        this.mouseClickedPopUpMenu = new ClientEventHandler();
        if (!this.initialized) {
            this.uiPopUPButton = new UIPopUPButton(10.0f, this.res.getScaledHeight() - 10, 6.0f, 14.0f);
            this.initialized = true;
        }
    }

    public void load() {
        if (!this.initialized) {
            Runnable run = new Runnable(){

                @Override
                public void run() {
                    UIClick.access(UIClick.this, new UIPopUPButton(10.0f, Minecraft.getMinecraft().displayHeight - 10, 6.0f, 14.0f));
                    UIClick.this.initialized = true;
                }
            };
            new Thread(run).start();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.uiPopUPButton.draw(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.uiPopUPButton.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.uiPopUPButton.mouseReleased(mouseX, mouseY);
    }

    private boolean isHovering(int mouseX, int mouseY, int x2, int y2, int x22, int y22) {
        if (mouseX >= x2 && mouseX <= x22 && mouseY >= y2 && mouseY <= y22) {
            return true;
        }
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    static void access(UIClick uIClick, UIPopUPButton uIPopUPButton) {
        uIClick.uiPopUPButton = uIPopUPButton;
    }

}

