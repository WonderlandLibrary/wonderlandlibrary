package net.minecraft.client.gui;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.RandomStringUtils;
import wtf.diablo.Diablo;
import wtf.diablo.gui.AltLoginThread;
import wtf.diablo.utils.chat.ChatUtil;
import wtf.diablo.utils.render.ColorUtil;

public class GuiDisconnected extends GuiScreen {
    private String reason;
    private IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp) {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, (this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT) + 25, 203, 20, "Login with random cracked alt"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, (this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT) + 50, 203, 20, "Relog"));


    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            mc.displayGuiScreen(this.parentScreen);
        }
        if (button.id == 1) {
            AltLoginThread thread;
            String name = RandomStringUtils.random(14, true, true);
            thread = new AltLoginThread(name, "");
            thread.start();
            mc.displayGuiScreen(new GuiConnecting(this, this.mc, GuiConnecting.lastServer));

        }
        if (button.id == 2) {
            mc.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), this.mc, GuiConnecting.lastServer));
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Logged in as: " + ColorUtil.COLORRESET + Minecraft.getMinecraft().getSession().getUsername(), this.width / 2, 5, -1);
        this.drawCenteredString(this.fontRendererObj, "You lasted: " + ColorUtil.COLORRESET + Diablo.hypixelStatus.getSessionLengthString(), this.width / 2, 18, -1);
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = this.height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null) {
            for (String s : this.multilineMessage) {
                this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
