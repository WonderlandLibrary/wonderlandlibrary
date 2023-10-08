package net.minecraft.client.gui;

import dev.rise.Rise;
import dev.rise.ui.mainmenu.MainMenu;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

import java.io.IOException;
import java.util.List;

public class GuiDisconnected extends GuiScreen {
    private final String reason;
    private final IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    private long timeWhenDisconnected;

    public GuiDisconnected(final GuiScreen screen, final String reasonLocalizationKey, final IChatComponent chatComp) {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey);
        this.message = chatComp;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }

    /**
     * Adds the dev.rise.ui.clickgui.impl.astolfo.buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.timeWhenDisconnected = System.currentTimeMillis();

        PlayerUtil.serverResponses.clear();
        PlayerUtil.sentEmail = false;

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu")));

        if (!Rise.INSTANCE.destructed) {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT + this.fontRendererObj.FONT_HEIGHT + (50 / 4 + this.fontRendererObj.FONT_HEIGHT), "Reconnect"));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT + this.fontRendererObj.FONT_HEIGHT + ((50 / 4 + this.fontRendererObj.FONT_HEIGHT) * 2), "Alt Manager"));
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for dev.rise.ui.clickgui.impl.astolfo.buttons)
     */
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(this.parentScreen);
                break;

            // Reconnect
            case 1:
                if (Rise.ip == null || Rise.ip.isEmpty() || Rise.port == 0 || Rise.INSTANCE.destructed) return;

                this.mc.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new MainMenu()), this.mc, Rise.ip, Rise.port));
                break;

            // Alt Manager
            case 2:
                if (Rise.INSTANCE.destructed) return;

                this.mc.displayGuiScreen(Rise.INSTANCE.getAltGUI());
                break;
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = this.height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null) {
            for (final String s : this.multilineMessage) {
                this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }

//        final long milliseconds = timeWhenDisconnected - Rise.timeJoinedServer;
//
//        final long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
//        final long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
//        final long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
//
//        String time = "Unavailable";
//
//        if (!mc.isIntegratedServerRunning()) {
//            if (minutes < 1)
//                time = seconds + "s";
//            else if (hours < 1)
//                time = minutes + "m, " + (seconds - minutes * 60) + "s";
//            else
//                time = hours + "h, " + (minutes - hours * 60) + "m, " + ((seconds - minutes * 60 - hours) + hours) + "s";
//        }
//
//        this.drawCenteredString(this.fontRendererObj, "You played for " + time + " before getting disconnected.", this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2 - 20, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
