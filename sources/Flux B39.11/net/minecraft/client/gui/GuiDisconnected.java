package net.minecraft.client.gui;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import today.flux.Flux;
import today.flux.gui.altMgr.Alt;
import today.flux.gui.altMgr.GuiAltMgr;
import today.flux.utility.DelayTimer;
import today.flux.utility.LoginUtils;
import today.flux.utility.ServerUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GuiDisconnected extends GuiScreen {
    private String reason;
    private IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    // Client
    private GuiButton randomAlt;
    private GuiButton removeAlt;
    private boolean doReconnect;

    boolean isHypixelBanned = false;

    public long sessionEndTime = 0;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp) {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
        String[] array;
        sessionEndTime = System.currentTimeMillis();
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the
     * equivalent of KeyListener.keyTyped(KeyEvent e). Args : character (character
     * on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when
     * the GUI is displayed and when the window resizes, the buttonList is cleared
     * beforehand.
     */
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(),
                this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100,
                this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT,
                I18n.format("gui.toMenu", new Object[0])));

        // Client
        final int y = this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT;
        final int x = this.width / 2 - 100;

        this.buttonList.add(new GuiButton(1337, x, y + 24, "Reconnect"));
        this.buttonList.add(this.randomAlt = new GuiButton(1338, x, y + 24 * 2, "Reconnect with a Random Alt"));

        randomAlt.enabled = GuiAltMgr.alts != null && GuiAltMgr.alts.size() > 0;

        if (Flux.currentAlt != null && GuiAltMgr.alts.stream().anyMatch(alt -> !alt.isCracked()
                && alt.getEmail().equals(Flux.currentAlt[0]) && alt.getPassword().equals(Flux.currentAlt[1])))
            this.buttonList.add(removeAlt = new GuiButton(9999, x, y + 24 * 4, "Delete the alt from AltManager"));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for
     * buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }

        // Client
        if (button.id == 1337) {
            this.doReconnect = true;
        }

        if (button.id == 1338) {
            if (Flux.AUTO_RECONNECT && !this.timer.hasPassed(5000)) {
                Flux.AUTO_RECONNECT = false;
                randomAlt.displayString = "Reconnect with a Random Alt";
                return;
            }

            Flux.AUTO_RECONNECT = true;
            randomAlt.enabled = false;
            new Thread(() -> {
                List<Alt> alts = GuiAltMgr.alts.stream().filter(alt1 -> !alt1.isCracked())
                        .collect(Collectors.toList());
                Collections.shuffle(alts);
                Alt alt = alts.get(0);

                String reply = LoginUtils.login(alt.getEmail(), alt.getPassword());

                if (reply == null) {
                    alt.setChecked(mc.getSession().getUsername());
                    doReconnect = true;
                } else {
                    GuiAltMgr.alts.remove(alt);

                    this.timer.reset();
                    randomAlt.enabled = true;
                }
            }).start();
        }

        if (button.id == 9999 && removeAlt != null && removeAlt.enabled) {
            doDelete();
        }
    }

    public void doDelete() {
        Alt remove = GuiAltMgr.alts.stream().filter(alt -> !alt.isCracked()
                && alt.getEmail().equals(Flux.currentAlt[0]) && alt.getPassword().equals(Flux.currentAlt[1]))
                .toArray(Alt[]::new)[0];
        GuiAltMgr.alts.remove(remove);
        removeAlt.enabled = false;
    }

    private DelayTimer timer = new DelayTimer();

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY,
     * renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Client
        if (doReconnect) {
            this.mc.displayGuiScreen(new GuiConnecting(null, this.mc, Flux.INSTANCE.lastServer));
            return;
        }

        this.drawDefaultBackground();

        long durationInMillis = sessionEndTime - Flux.playTimeStart;
        long second = (durationInMillis / 1000) % 60;
        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
        String time = String.format("%02dh %02dm %02ds", hour, minute, second);

        drawCenteredString(this.fontRendererObj, "Session Ended! Total time: " + time, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2 - 50, 0xffffffff);

        drawCenteredString(this.fontRendererObj, "Server: " + (isHypixelBanned ? "Hypixel" : ServerUtils.getServer()), width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2 - 30, 0xffffff);
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2,
                this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = this.height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null) {
            for (String s : this.multilineMessage) {
                this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (Flux.AUTO_RECONNECT && this.randomAlt.enabled) {
            if (timer.hasPassed(5000)) {
                this.randomAlt.displayString = "Reconnect with a Random Alt (0)";
                try {
                    actionPerformed(this.randomAlt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (timer.hasPassed(4000)) {
                this.randomAlt.displayString = "Reconnect with a Random Alt (1)";
            } else if (timer.hasPassed(3000)) {
                this.randomAlt.displayString = "Reconnect with a Random Alt (2)";
            } else if (timer.hasPassed(2000)) {
                this.randomAlt.displayString = "Reconnect with a Random Alt (3)";
            } else if (timer.hasPassed(1000)) {
                this.randomAlt.displayString = "Reconnect with a Random Alt (4)";
            } else {
                this.randomAlt.displayString = "Reconnect with a Random Alt (5)";
            }
        }
    }
}
