/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import tk.rektsky.Client;
import tk.rektsky.module.Module;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.utils.display.ColorUtil;

public class GuiKeybind
extends GuiScreen {
    private GuiScreen previoudScreen;
    private Module module;
    private final FontRenderer font;

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)this.width / 2.0f, (float)this.height / 2.0f, 0.0f);
        GlStateManager.scale(4.0f, 4.0f, 0.0f);
        this.font.drawString("Press a key to bind " + this.module.name, -this.font.getStringWidth("Press a key to bind " + this.module.name) / 2, -this.font.FONT_HEIGHT / 2, 0xFFFFFF);
        GlStateManager.popMatrix();
        this.font.drawString("Press a DEL to delete or ESC to cancel", this.width / 2 - this.font.getStringWidth("Press a DEL to delete or ESC to cancel") / 2, this.height / 2 + this.font.FONT_HEIGHT * 2 + 10, 0xFFFFFF);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            Client.notify(new Notification.PopupMessage("Keybind", "Canceled binding " + this.module.name + " !", ColorUtil.NotificationColors.RED, 20));
            Minecraft.getMinecraft().displayGuiScreen(this.previoudScreen);
            return;
        }
        if (keyCode == 211) {
            Client.notify(new Notification.PopupMessage("Keybind", "Unbound " + this.module.name + " !", ColorUtil.NotificationColors.GREEN, 20));
            this.module.keyCode = 0;
            Minecraft.getMinecraft().displayGuiScreen(this.previoudScreen);
            return;
        }
        Client.notify(new Notification.PopupMessage("Keybind", "Bound " + this.module.name + " to " + Keyboard.getKeyName(keyCode) + " !", ColorUtil.NotificationColors.GREEN, 20));
        this.module.keyCode = keyCode;
        Minecraft.getMinecraft().displayGuiScreen(this.previoudScreen);
    }

    public GuiKeybind(GuiScreen previoudScreen, Module module) {
        this.font = Minecraft.getMinecraft().fontRendererObj;
        this.previoudScreen = previoudScreen;
        this.module = module;
    }
}

