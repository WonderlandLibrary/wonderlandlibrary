/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub;

import java.io.IOException;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.input.Keyboard;

public class GuiChangeName
extends WrappedGuiScreen {
    private final GuiAltManager prevGui;
    private IGuiTextField name;
    private String status;

    public GuiChangeName(GuiAltManager gui) {
        this.prevGui = gui;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(1, this.representedScreen.getWidth() / 2 - 100, this.representedScreen.getHeight() / 4 + 96, "Change"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(0, this.representedScreen.getWidth() / 2 - 100, this.representedScreen.getHeight() / 4 + 120, "Back"));
        this.name = classProvider.createGuiTextField(2, Fonts.font40, this.representedScreen.getWidth() / 2 - 100, 60, 200, 20);
        this.name.setFocused(true);
        this.name.setText(mc.getSession().getUsername());
        this.name.setMaxStringLength(16);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.representedScreen.drawBackground(0);
        RenderUtils.drawRect(30, 30, this.representedScreen.getWidth() - 30, this.representedScreen.getHeight() - 30, Integer.MIN_VALUE);
        Fonts.font40.drawCenteredString("Change Name", (float)this.representedScreen.getWidth() / 2.0f, 34.0f, 0xFFFFFF);
        Fonts.font40.drawCenteredString(this.status == null ? "" : this.status, (float)this.representedScreen.getWidth() / 2.0f, (float)this.representedScreen.getHeight() / 4.0f + 84.0f, 0xFFFFFF);
        this.name.drawTextBox();
        if (this.name.getText().isEmpty() && !this.name.isFocused()) {
            Fonts.font40.drawCenteredString("\u00a77Username", (float)this.representedScreen.getWidth() / 2.0f - 74.0f, 66.0f, 0xFFFFFF);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(IGuiButton button) throws IOException {
        switch (button.getId()) {
            case 0: {
                mc.displayGuiScreen(this.prevGui.representedScreen);
                break;
            }
            case 1: {
                if (this.name.getText().isEmpty()) {
                    this.status = "\u00a7cEnter a name!";
                    return;
                }
                if (!this.name.getText().equalsIgnoreCase(mc.getSession().getUsername())) {
                    this.status = "\u00a7cJust change the upper and lower case!";
                    return;
                }
                mc.setSession(classProvider.createSession(this.name.getText(), mc.getSession().getPlayerId(), mc.getSession().getToken(), mc.getSession().getSessionType()));
                LiquidBounce.eventManager.callEvent(new SessionEvent());
                this.prevGui.status = this.status = "\u00a7aChanged name to \u00a77" + this.name.getText() + "\u00a7c.";
                mc.displayGuiScreen(this.prevGui.representedScreen);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (1 == keyCode) {
            mc.displayGuiScreen(this.prevGui.getRepresentedScreen());
            return;
        }
        if (this.name.isFocused()) {
            this.name.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.name.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        this.name.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        super.onGuiClosed();
    }
}

