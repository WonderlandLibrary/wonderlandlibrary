package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import today.flux.Flux;
import today.flux.gui.altMgr.Alt;
import today.flux.gui.altMgr.GuiAltMgr;
import today.flux.gui.plugingui.GuiPluginMgr;
import today.flux.utility.LoginUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GuiIngameMenu extends GuiScreen {
    private static GuiButton reconnectrandom;
    private static GuiButton reconnectuncheck;
    private static boolean doReconnect;

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when
     * the GUI is displayed and when the window resizes, the buttonList is cleared
     * beforehand.
     */
    public void initGui() {
        this.buttonList.clear();
        int i = -16;
        this.buttonList.add(
                new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + i, I18n.format("menu.returnToMenu")));

        if (!this.mc.isIntegratedServerRunning()) {
            this.buttonList.get(0).displayString = I18n.format("menu.disconnect");
        }

        this.buttonList.add(
                new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + i, I18n.format("menu.returnToGame")));
        this.buttonList.add(
                new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.options")));
        GuiButton guibutton;
        this.buttonList.add(guibutton = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + i, 98, 20,
                I18n.format("menu.shareToLan")));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + i, 98, 20,
                I18n.format("gui.achievements")));
        this.buttonList
                .add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.stats")));
        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();

        this.buttonList.add(new GuiButton(69420, this.width / 2 - 100, this.height / 4 + 72 + i, "Flux Addon..."));

        // Client: Reconnect
        this.buttonList
                .add(reconnectrandom = new GuiButton(1337, 4, this.height - 48, 140, 20, "Reconnect with Random Alt"));

        List<Alt> alts = GuiAltMgr.alts.stream().filter(alt1 -> (!alt1.isCracked())).collect(Collectors.toList());
        if (alts.size() == 0) {
            reconnectrandom.enabled = false;
        }

        this.buttonList.add(
                reconnectuncheck = new GuiButton(1338, 4, this.height - 24, 160, 20, "Reconnect with Unchecked Alt"));

        List<Alt> alts2 = GuiAltMgr.alts.stream().filter(alt1 -> (!alt1.isCracked() && alt1.isUnchecked())).collect(Collectors.toList());
        if (alts2.size() == 0) {
            reconnectuncheck.enabled = false;
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for
     * buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;

            case 1:
                boolean flag = this.mc.isIntegratedServerRunning();
                //boolean flag1 = this.mc.isConnectedToRealms();
                button.enabled = false;
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld((WorldClient) null);

                if (flag) {
                    this.mc.displayGuiScreen(new GuiMainMenu());
                } else {
                    this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                }

            case 2:
            case 3:
            default:
                break;

            case 4:
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;

            case 5:
                this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
                break;

            case 6:
                this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
                break;

            case 7:
                this.mc.displayGuiScreen(new GuiShareToLan(this));
                break;
            case 1337:
                // client
                reconnectrandom.enabled = false;
                new Thread(() -> {
                    List<Alt> alts = GuiAltMgr.alts.stream().filter(alt1 -> (!alt1.isCracked()))
                            .collect(Collectors.toList());
                    Collections.shuffle(alts);
                    Alt alt = alts.get(0);

                    String error = LoginUtils.login(alt.getEmail(), alt.getPassword(), false);

                    if (error == null) {
                        alt.setChecked(mc.getSession().getUsername());

                        doReconnect = true;
                    } else {
                        reconnectrandom.enabled = true;
                    }
                }).start();
            case 1338:
                // client
                reconnectuncheck.enabled = false;
                doRandomUnchecked();
                break;

            case 69420:
                mc.displayGuiScreen(new GuiPluginMgr(this));
        }


    }

    public static void doRandomUnchecked() {
        new Thread(() -> {
            List<Alt> alts = GuiAltMgr.alts.stream().filter(alt1 -> (!alt1.isCracked() && alt1.isUnchecked()))
                    .collect(Collectors.toList());
            Collections.shuffle(alts);


            Alt alt = alts.get(0);

            String error = LoginUtils.login(alt.getEmail(), alt.getPassword(), true);

            if (error == null) {
                alt.setChecked(Minecraft.getMinecraft().getSession().getUsername());
                doReconnect = true;
            } else {
                reconnectuncheck.enabled = true;
            }
        }).start();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY,
     * renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Client
        if (doReconnect) {
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);

            this.mc.displayGuiScreen(new GuiConnecting(null, this.mc, Flux.INSTANCE.lastServer));
            this.doReconnect = false;
        }

        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game"), this.width / 2, 40, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
