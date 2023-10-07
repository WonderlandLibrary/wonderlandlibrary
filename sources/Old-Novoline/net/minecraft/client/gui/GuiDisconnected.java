package net.minecraft.client.gui;

import cc.novoline.Novoline;
import cc.novoline.gui.screen.alt.repository.Alt;
import cc.novoline.gui.screen.alt.repository.AltRepositoryGUI;
import cc.novoline.gui.screen.alt.repository.credential.AlteningAltCredential;
import com.thealtening.api.TheAlteningException;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static cc.novoline.utils.notifications.NotificationType.ERROR;
import static cc.novoline.utils.notifications.NotificationType.WARNING;

public class GuiDisconnected extends GuiScreen {

    private final String reason;
    private final IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp) {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey);
        this.message = chatComp;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    private GuiButton randomAltButton;
    private GuiButton generateAndLogInButton;
    private GuiButton reconnectButton;
    private ServerData lastServer;

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        Novoline novoline = Novoline.getInstance();
        novoline.getNotificationManager().getNotifications().clear();
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.getHeight();

        final int x = this.width / 2 - 100;
        final int y = this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.getHeight();

        this.buttonList.add(new GuiButton(0, x, y, I18n.format("gui.toMenu")));
        this.buttonList.add(reconnectButton = new GuiButton(1, x, y + 20, "Reconnect"));
        this.buttonList.add(new GuiButton(2, x, y + 40, 100, 20, "Remove Alt"));
        this.buttonList.add(randomAltButton = new GuiButton(3, this.width / 2, y + 40, 100, 20, "Random Alt"));
        this.buttonList.add(generateAndLogInButton = new GuiButton(4, x, y + 60, "Generate and Reconnect"));

        this.lastServer = mc.getNovoline().getLastConnectedServer();
        reconnectButton.enabled = lastServer != null;

        String banMessage = this.multilineMessage.get(0);
        if (banMessage.startsWith("§r§cYou are temporarily banned for ")) {
            String[] firstSplit = banMessage.split("§r§cYou are temporarily banned for ");
            if (firstSplit.length > 1) {
                String[] secondSplit = firstSplit[1].split("§r");
                if (secondSplit.length > 1) {
                    String complete = secondSplit[1].replaceAll("§[a-f]", "");
                    String replaced = complete.replace("d", "").replace("h", "").replace("m", "");
                    String[] thirdSplit = replaced.split(" ");
                    String days = thirdSplit[0];
                    String hours = thirdSplit[1];
                    String minutes = thirdSplit[2];
                    long daysMillis = TimeUnit.DAYS.toMillis(Integer.parseInt(days));
                    long hoursMillis = TimeUnit.HOURS.toMillis(Integer.parseInt(hours));
                    long minutesMillis = TimeUnit.MINUTES.toMillis(Integer.parseInt(minutes));

                    long completeMillis = System.currentTimeMillis() + daysMillis + hoursMillis + minutesMillis;
                    if (novoline.getAltRepositoryGUI().getCurrentAlt().getUnbanDate() == 0)
                        novoline.getNotificationManager().pop("Alt banned!", "Marked " + novoline.getAltRepositoryGUI().getCurrentAlt().getPlayer().getName() + " as banned for " + complete, 3000, WARNING);
                    novoline.getAltRepositoryGUI().getCurrentAlt().setUnbanDate(completeMillis);
                }
            }
        } else if (banMessage.startsWith("§r§cYou are permanently banned")) {
            if (novoline.getAltRepositoryGUI().getCurrentAlt().getUnbanDate() == 0L)
                novoline.getNotificationManager().pop("Alt banned!", "Marked " + novoline.getAltRepositoryGUI().getCurrentAlt().getPlayer().getName() + " as permanently banned", 3000, WARNING);
            novoline.getAltRepositoryGUI().getCurrentAlt().setUnbanDate(-1);
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        final Novoline novoline = this.mc.getNovoline();
        final AltRepositoryGUI repository = novoline.getAltRepositoryGUI();

        switch (button.id) {
            case 0:
                mc.displayGuiScreen(this.parentScreen);
                break;

            case 1: /* reconnect */
                reconnect();
                break;

            case 2: /* remove alt */
                if (repository.getCurrentAlt() != null) {
                    repository.removeAlt(repository.getCurrentAlt());
                }

                break;

            case 3: /* random alt */
                final Alt randomAlt = repository.getRandomAlt();

                if (randomAlt == null) {
                    Novoline.getInstance().getNotificationManager().pop("No alts in repository!", ERROR);
                    return;
                }

                setEnabledLogInButtons(false);

                randomAlt.logIn(true).whenComplete(($, t) -> {
                    setEnabledLogInButtons(true);

                    if (t != null) {
                        Novoline.getLogger().warn("An unexpected error occurred while logging into an alt!", t);
                        Novoline.getInstance().getNotificationManager().pop("Unexpected error occurred!", ERROR);
                    }
                });

                break;

            case 4: /* generate alt and reconnect */
                generateAndLogin(true, novoline, repository);
                break;
        }
    }

    private void generateAndLogin(boolean reconnect, @NonNull Novoline novoline, @NonNull AltRepositoryGUI repository) {
        final String tokenContent = repository.getTokenContent();

        if (StringUtils.isBlank(tokenContent)) {
            Novoline.getInstance().getNotificationManager().pop("No TheAltening token!", ERROR);
            return;
        }

        novoline.getDataRetriever().updateKey(tokenContent);

        setEnabledLogInButtons(false);

        CompletableFuture.runAsync(() -> {
            novoline.generateAlteningAlt().whenCompleteAsync((account, throwable) -> {
                if (throwable != null) {
                    Novoline.getLogger().warn("An error occurred while generating an account!", throwable);

                    if (throwable instanceof TheAlteningException) {
                        Novoline.getInstance().getNotificationManager().pop("Invalid TheAltening token!", ERROR);
                    } else {
                        Novoline.getLogger().warn("An unexpected error occurred while generating an alt!", throwable);
                        Novoline.getInstance().getNotificationManager().pop("Unexpected error occurred!", ERROR);
                    }

                    setEnabledLogInButtons(true);
                    return;
                }

                final AlteningAltCredential altCredential = new AlteningAltCredential(account.getToken(), account.getUsername(), account.getInfo());
                final Alt alt = repository.addAlt(altCredential);

                if (alt != null) {
                    try {
                        alt.logIn(false).get();
                    } catch (Throwable t) {
                        Novoline.getLogger().warn("An unexpected error occurred while logging into an alt!", t);
                        Novoline.getInstance().getNotificationManager().pop("Unexpected error occurred!", ERROR);
                    }
                }

                setEnabledLogInButtons(true);
            });
        }, ForkJoinPool.commonPool()).whenComplete((aVoid, throwable) -> {
            if (reconnect) reconnect();
        });
    }

    private void reconnect() {
        mc.displayGuiScreen(new GuiConnecting(parentScreen, mc, lastServer));
    }

    private void setEnabledLogInButtons(boolean b) {
        this.reconnectButton.enabled = b;
        this.generateAndLogInButton.enabled = b;
        this.randomAltButton.enabled = b;
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.getHeight() * 2, 11184810);

        int i = this.height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null) {
            for (String s : this.multilineMessage) {
                this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
                i += this.fontRendererObj.getHeight();
            }
        }

        final int y = this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.getHeight();

        if (mc.session != null && lastServer != null) {
            this.fontRendererObj.drawString("Playing on: " + mc.session.getUsername() + " | " + lastServer.serverIP, this.width / 2 - fontRendererObj.getStringWidth("Playing on: " + mc.session.getUsername() + " | " + lastServer.serverIP) / 2, y + 85, 0xffffffff);
        }


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
