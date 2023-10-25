/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.gui.RealmsHideableButton;
import com.mojang.realmsclient.gui.screens.RealmsBackupScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.RealmsPlayerScreen;
import com.mojang.realmsclient.gui.screens.RealmsResetWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsScreenWithCallback;
import com.mojang.realmsclient.gui.screens.RealmsSelectWorldTemplateScreen;
import com.mojang.realmsclient.gui.screens.RealmsSettingsScreen;
import com.mojang.realmsclient.gui.screens.RealmsSlotOptionsScreen;
import com.mojang.realmsclient.gui.screens.RealmsSubscriptionInfoScreen;
import com.mojang.realmsclient.util.RealmsTasks;
import com.mojang.realmsclient.util.RealmsTextureManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsMth;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsSharedConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class RealmsConfigureWorldScreen
extends RealmsScreenWithCallback<WorldTemplate> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ON_ICON_LOCATION = "realms:textures/gui/realms/on_icon.png";
    private static final String OFF_ICON_LOCATION = "realms:textures/gui/realms/off_icon.png";
    private static final String EXPIRED_ICON_LOCATION = "realms:textures/gui/realms/expired_icon.png";
    private static final String EXPIRES_SOON_ICON_LOCATION = "realms:textures/gui/realms/expires_soon_icon.png";
    private static final String SLOT_FRAME_LOCATION = "realms:textures/gui/realms/slot_frame.png";
    private static final String EMPTY_FRAME_LOCATION = "realms:textures/gui/realms/empty_frame.png";
    private String toolTip;
    private final RealmsMainScreen lastScreen;
    private RealmsServer serverData;
    private final long serverId;
    private int left_x;
    private int right_x;
    private final int default_button_width = 80;
    private final int default_button_offset = 5;
    private static final int BUTTON_BACK_ID = 0;
    private static final int BUTTON_PLAYERS_ID = 2;
    private static final int BUTTON_SETTINGS_ID = 3;
    private static final int BUTTON_SUBSCRIPTION_ID = 4;
    private static final int BUTTON_OPTIONS_ID = 5;
    private static final int BUTTON_BACKUP_ID = 6;
    private static final int BUTTON_RESET_WORLD_ID = 7;
    private static final int BUTTON_SWITCH_MINIGAME_ID = 8;
    private static final int SWITCH_SLOT_ID = 9;
    private static final int SWITCH_SLOT_ID_EMPTY = 10;
    private static final int SWITCH_SLOT_ID_RESULT = 11;
    private RealmsButton playersButton;
    private RealmsButton settingsButton;
    private RealmsButton subscriptionButton;
    private RealmsHideableButton optionsButton;
    private RealmsHideableButton backupButton;
    private RealmsHideableButton resetWorldButton;
    private RealmsHideableButton switchMinigameButton;
    private boolean stateChanged;
    private int hoveredSlot = -1;
    private int animTick;
    private int clicks;
    private boolean hoveredActiveSlot;

    public RealmsConfigureWorldScreen(RealmsMainScreen lastScreen, long serverId) {
        this.lastScreen = lastScreen;
        this.serverId = serverId;
    }

    @Override
    public void mouseEvent() {
        super.mouseEvent();
    }

    @Override
    public void init() {
        if (this.serverData == null) {
            this.fetchServerData(this.serverId);
        }
        this.left_x = this.width() / 2 - 187;
        this.right_x = this.width() / 2 + 190;
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.playersButton = RealmsConfigureWorldScreen.newButton(2, this.centerButton(0, 3), RealmsConstants.row(0), 100, 20, RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.buttons.players"));
        this.buttonsAdd(this.playersButton);
        this.settingsButton = RealmsConfigureWorldScreen.newButton(3, this.centerButton(1, 3), RealmsConstants.row(0), 100, 20, RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.buttons.settings"));
        this.buttonsAdd(this.settingsButton);
        this.subscriptionButton = RealmsConfigureWorldScreen.newButton(4, this.centerButton(2, 3), RealmsConstants.row(0), 100, 20, RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.buttons.subscription"));
        this.buttonsAdd(this.subscriptionButton);
        this.optionsButton = new RealmsHideableButton(5, this.leftButton(0), RealmsConstants.row(13) - 5, 90, 20, RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.buttons.options"));
        this.buttonsAdd(this.optionsButton);
        this.backupButton = new RealmsHideableButton(6, this.leftButton(1), RealmsConstants.row(13) - 5, 90, 20, RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.backup"));
        this.buttonsAdd(this.backupButton);
        this.resetWorldButton = new RealmsHideableButton(7, this.leftButton(2), RealmsConstants.row(13) - 5, 90, 20, RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.buttons.resetworld"));
        this.buttonsAdd(this.resetWorldButton);
        this.switchMinigameButton = new RealmsHideableButton(8, this.leftButton(0), RealmsConstants.row(13) - 5, 100, 20, RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.buttons.switchminigame"));
        this.buttonsAdd(this.switchMinigameButton);
        this.buttonsAdd(RealmsConfigureWorldScreen.newButton(0, this.right_x - 80 + 8, RealmsConstants.row(13) - 5, 70, 20, RealmsConfigureWorldScreen.getLocalizedString("gui.back")));
        this.backupButton.active(true);
        if (this.serverData == null) {
            this.hideMinigameButtons();
            this.hideRegularButtons();
            this.playersButton.active(false);
            this.settingsButton.active(false);
            this.subscriptionButton.active(false);
        } else {
            this.disableButtons();
            if (this.isMinigame()) {
                this.hideRegularButtons();
            } else {
                this.hideMinigameButtons();
            }
        }
    }

    private int leftButton(int i) {
        return this.left_x + i * 95;
    }

    private int centerButton(int i, int total) {
        return this.width() / 2 - (total * 105 - 5) / 2 + i * 105;
    }

    @Override
    public void tick() {
        ++this.animTick;
        --this.clicks;
        if (this.clicks < 0) {
            this.clicks = 0;
        }
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.toolTip = null;
        this.hoveredActiveSlot = false;
        this.hoveredSlot = -1;
        this.renderBackground();
        this.drawCenteredString(RealmsConfigureWorldScreen.getLocalizedString("mco.configure.worlds.title"), this.width() / 2, RealmsConstants.row(4), 0xFFFFFF);
        super.render(xm, ym, a);
        if (this.serverData == null) {
            this.drawCenteredString(RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.title"), this.width() / 2, 17, 0xFFFFFF);
            return;
        }
        String name = this.serverData.getName();
        int nameWidth = this.fontWidth(name);
        int nameColor = this.serverData.state == RealmsServer.State.CLOSED ? 0xA0A0A0 : 0x7FFF7F;
        int titleWidth = this.fontWidth(RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.title"));
        this.drawCenteredString(RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.title"), this.width() / 2, 12, 0xFFFFFF);
        this.drawCenteredString(name, this.width() / 2, 24, nameColor);
        int statusX = Math.min(this.centerButton(2, 3) + 80 - 11, this.width() / 2 + nameWidth / 2 + titleWidth / 2 + 10);
        this.drawServerStatus(statusX, 7, xm, ym);
        for (Map.Entry<Integer, RealmsWorldOptions> entry : this.serverData.slots.entrySet()) {
            if (entry.getValue().templateImage != null && entry.getValue().templateId != -1L) {
                this.drawSlotFrame(this.frame(entry.getKey()), RealmsConstants.row(5) + 5, xm, ym, this.serverData.activeSlot == entry.getKey() && !this.isMinigame(), entry.getValue().getSlotName(entry.getKey()), entry.getKey(), entry.getValue().templateId, entry.getValue().templateImage, entry.getValue().empty);
                continue;
            }
            this.drawSlotFrame(this.frame(entry.getKey()), RealmsConstants.row(5) + 5, xm, ym, this.serverData.activeSlot == entry.getKey() && !this.isMinigame(), entry.getValue().getSlotName(entry.getKey()), entry.getKey(), -1L, null, entry.getValue().empty);
        }
        this.drawSlotFrame(this.frame(4), RealmsConstants.row(5) + 5, xm, ym, this.isMinigame(), "Minigame", 4, -1L, null, false);
        if (this.isMinigame()) {
            this.drawString(RealmsConfigureWorldScreen.getLocalizedString("mco.configure.current.minigame") + ": " + this.serverData.getMinigameName(), this.left_x + 80 + 20 + 10, RealmsConstants.row(13), 0xFFFFFF);
        }
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, xm, ym);
        }
    }

    private int frame(int i) {
        return this.left_x + (i - 1) * 98;
    }

    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void buttonClicked(RealmsButton button) {
        if (!button.active()) {
            return;
        }
        if (button instanceof RealmsHideableButton && !((RealmsHideableButton)button).getVisible()) {
            return;
        }
        switch (button.id()) {
            case 2: {
                Realms.setScreen(new RealmsPlayerScreen(this, this.serverData));
                break;
            }
            case 3: {
                Realms.setScreen(new RealmsSettingsScreen(this, this.serverData.clone()));
                break;
            }
            case 4: {
                Realms.setScreen(new RealmsSubscriptionInfoScreen(this, this.serverData.clone(), this.lastScreen));
                break;
            }
            case 0: {
                this.backButtonClicked();
                break;
            }
            case 8: {
                Realms.setScreen(new RealmsSelectWorldTemplateScreen(this, null, RealmsServer.WorldType.MINIGAME));
                break;
            }
            case 6: {
                Realms.setScreen(new RealmsBackupScreen(this, this.serverData.clone(), this.serverData.activeSlot));
                break;
            }
            case 5: {
                Realms.setScreen(new RealmsSlotOptionsScreen(this, this.serverData.slots.get(this.serverData.activeSlot).clone(), this.serverData.worldType, this.serverData.activeSlot));
                break;
            }
            case 7: {
                Realms.setScreen(new RealmsResetWorldScreen(this, this.serverData.clone(), this.getNewScreen()));
                break;
            }
            default: {
                return;
            }
        }
    }

    @Override
    public void keyPressed(char ch, int eventKey) {
        if (eventKey == 1) {
            this.backButtonClicked();
        }
    }

    private void backButtonClicked() {
        if (this.stateChanged) {
            this.lastScreen.removeSelection();
        }
        Realms.setScreen(this.lastScreen);
    }

    private void fetchServerData(final long worldId) {
        new Thread(){

            @Override
            public void run() {
                RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    RealmsConfigureWorldScreen.this.serverData = client.getOwnWorld(worldId);
                    RealmsConfigureWorldScreen.this.disableButtons();
                    if (RealmsConfigureWorldScreen.this.isMinigame()) {
                        RealmsConfigureWorldScreen.this.showMinigameButtons();
                    } else {
                        RealmsConfigureWorldScreen.this.showRegularButtons();
                    }
                }
                catch (RealmsServiceException e) {
                    LOGGER.error("Couldn't get own world");
                    Realms.setScreen(new RealmsGenericErrorScreen(e.getMessage(), (RealmsScreen)RealmsConfigureWorldScreen.this.lastScreen));
                }
                catch (IOException ignored) {
                    LOGGER.error("Couldn't parse response getting own world");
                }
            }
        }.start();
    }

    private void disableButtons() {
        this.playersButton.active(!this.serverData.expired);
        this.settingsButton.active(!this.serverData.expired);
        this.subscriptionButton.active(true);
        this.switchMinigameButton.active(!this.serverData.expired);
        this.optionsButton.active(!this.serverData.expired);
        this.resetWorldButton.active(!this.serverData.expired);
    }

    @Override
    public void confirmResult(boolean result, int id) {
        switch (id) {
            case 9: {
                if (result) {
                    this.switchSlot();
                    break;
                }
                Realms.setScreen(this);
                break;
            }
            case 10: {
                if (result) {
                    RealmsResetWorldScreen resetWorldScreen = new RealmsResetWorldScreen(this, this.serverData, this.getNewScreen(), RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.switch.slot"), RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.switch.slot.subtitle"), 0xA0A0A0, RealmsConfigureWorldScreen.getLocalizedString("gui.cancel"));
                    resetWorldScreen.setSlot(this.hoveredSlot);
                    resetWorldScreen.setResetTitle(RealmsConfigureWorldScreen.getLocalizedString("mco.create.world.reset.title"));
                    Realms.setScreen(resetWorldScreen);
                    break;
                }
                Realms.setScreen(this);
                break;
            }
            case 11: {
                Realms.setScreen(this);
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y, int buttonNum) {
        if (buttonNum == 0) {
            this.clicks += RealmsSharedConstants.TICKS_PER_SECOND / 3 + 1;
        } else {
            return;
        }
        if (this.hoveredSlot != -1) {
            if (this.hoveredSlot < 4) {
                String line2 = RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.slot.switch.question.line1");
                String line3 = RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.slot.switch.question.line2");
                if (this.serverData.slots.get((Object)Integer.valueOf((int)this.hoveredSlot)).empty) {
                    Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, 10));
                } else {
                    Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, 9));
                }
            } else if (!this.isMinigame() && !this.serverData.expired) {
                RealmsSelectWorldTemplateScreen screen = new RealmsSelectWorldTemplateScreen(this, null, RealmsServer.WorldType.MINIGAME);
                screen.setWarning(RealmsConfigureWorldScreen.getLocalizedString("mco.minigame.world.info.line1") + "\\n" + RealmsConfigureWorldScreen.getLocalizedString("mco.minigame.world.info.line2"));
                Realms.setScreen(screen);
            }
        } else if (this.clicks >= RealmsSharedConstants.TICKS_PER_SECOND / 2 && this.hoveredActiveSlot && (this.serverData.state == RealmsServer.State.OPEN || this.serverData.state == RealmsServer.State.CLOSED)) {
            if (this.serverData.state == RealmsServer.State.OPEN) {
                this.lastScreen.play(this.serverData, new RealmsConfigureWorldScreen(this.lastScreen.newScreen(), this.serverId));
            } else {
                this.openTheWorld(true, new RealmsConfigureWorldScreen(this.lastScreen.newScreen(), this.serverId));
            }
        }
        super.mouseClicked(x, y, buttonNum);
    }

    protected void renderMousehoverTooltip(String msg, int x, int y) {
        if (msg == null) {
            return;
        }
        int rx = x + 12;
        int ry = y - 12;
        int width = this.fontWidth(msg);
        if (rx + width + 3 > this.right_x) {
            rx = rx - width - 20;
        }
        this.fillGradient(rx - 3, ry - 3, rx + width + 3, ry + 8 + 3, -1073741824, -1073741824);
        this.fontDrawShadow(msg, rx, ry, 0xFFFFFF);
    }

    private void drawServerStatus(int x, int y, int xm, int ym) {
        if (this.serverData.expired) {
            this.drawExpired(x, y, xm, ym);
        } else if (this.serverData.state == RealmsServer.State.CLOSED) {
            this.drawClose(x, y, xm, ym);
        } else if (this.serverData.state == RealmsServer.State.OPEN) {
            if (this.serverData.daysLeft < 7) {
                this.drawExpiring(x, y, xm, ym, this.serverData.daysLeft);
            } else {
                this.drawOpen(x, y, xm, ym);
            }
        }
    }

    private void drawExpired(int x, int y, int xm, int ym) {
        RealmsScreen.bind(EXPIRED_ICON_LOCATION);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 10, 28, 10.0f, 28.0f);
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 27) {
            this.toolTip = RealmsConfigureWorldScreen.getLocalizedString("mco.selectServer.expired");
        }
    }

    private void drawExpiring(int x, int y, int xm, int ym, int daysLeft) {
        RealmsScreen.bind(EXPIRES_SOON_ICON_LOCATION);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        if (this.animTick % 20 < 10) {
            RealmsScreen.blit(x, y, 0.0f, 0.0f, 10, 28, 20.0f, 28.0f);
        } else {
            RealmsScreen.blit(x, y, 10.0f, 0.0f, 10, 28, 20.0f, 28.0f);
        }
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 27) {
            this.toolTip = daysLeft <= 0 ? RealmsConfigureWorldScreen.getLocalizedString("mco.selectServer.expires.soon") : (daysLeft == 1 ? RealmsConfigureWorldScreen.getLocalizedString("mco.selectServer.expires.day") : RealmsConfigureWorldScreen.getLocalizedString("mco.selectServer.expires.days", daysLeft));
        }
    }

    private void drawOpen(int x, int y, int xm, int ym) {
        RealmsScreen.bind(ON_ICON_LOCATION);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 10, 28, 10.0f, 28.0f);
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 27) {
            this.toolTip = RealmsConfigureWorldScreen.getLocalizedString("mco.selectServer.open");
        }
    }

    private void drawClose(int x, int y, int xm, int ym) {
        RealmsScreen.bind(OFF_ICON_LOCATION);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 10, 28, 10.0f, 28.0f);
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 27) {
            this.toolTip = RealmsConfigureWorldScreen.getLocalizedString("mco.selectServer.closed");
        }
    }

    private boolean isMinigame() {
        return this.serverData != null && this.serverData.worldType.equals((Object)RealmsServer.WorldType.MINIGAME);
    }

    private void drawSlotFrame(int x, int y, int xm, int ym, boolean active, String text, int i, long imageId, String image, boolean empty) {
        if (xm >= x && xm <= x + 80 && ym >= y && ym <= y + 80 && (this.isMinigame() ? i != 4 : this.serverData.activeSlot != i) && (i != 4 || !this.serverData.expired)) {
            this.hoveredSlot = i;
            this.toolTip = RealmsConfigureWorldScreen.getLocalizedString(i == 4 ? "mco.configure.world.slot.tooltip.minigame" : "mco.configure.world.slot.tooltip");
        }
        if (xm >= x && xm <= x + 80 && ym >= y && ym <= y + 80 && (this.isMinigame() ? i == 4 : this.serverData.activeSlot == i) && !this.serverData.expired && (this.serverData.state == RealmsServer.State.OPEN || this.serverData.state == RealmsServer.State.CLOSED)) {
            this.hoveredActiveSlot = true;
            this.toolTip = RealmsConfigureWorldScreen.getLocalizedString("mco.configure.world.slot.tooltip.active");
        }
        if (empty) {
            RealmsConfigureWorldScreen.bind(EMPTY_FRAME_LOCATION);
        } else if (image != null && imageId != -1L) {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(imageId), image);
        } else if (i == 1) {
            RealmsConfigureWorldScreen.bind("textures/gui/title/background/panorama_0.png");
        } else if (i == 2) {
            RealmsConfigureWorldScreen.bind("textures/gui/title/background/panorama_2.png");
        } else if (i == 3) {
            RealmsConfigureWorldScreen.bind("textures/gui/title/background/panorama_3.png");
        } else {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(this.serverData.minigameId), this.serverData.minigameImage);
        }
        if (!active) {
            GL11.glColor4f(0.56f, 0.56f, 0.56f, 1.0f);
        } else if (active) {
            float c = 0.9f + 0.1f * RealmsMth.cos((float)this.animTick * 0.2f);
            GL11.glColor4f(c, c, c, 1.0f);
        }
        RealmsScreen.blit(x + 3, y + 3, 0.0f, 0.0f, 74, 74, 74.0f, 74.0f);
        RealmsConfigureWorldScreen.bind(SLOT_FRAME_LOCATION);
        if (this.hoveredSlot == i) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        } else if (!active) {
            GL11.glColor4f(0.56f, 0.56f, 0.56f, 1.0f);
        } else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 80, 80, 80.0f, 80.0f);
        this.drawCenteredString(text, x + 40, y + 66, 0xFFFFFF);
    }

    private void hideRegularButtons() {
        this.optionsButton.setVisible(false);
        this.backupButton.setVisible(false);
        this.resetWorldButton.setVisible(false);
    }

    private void hideMinigameButtons() {
        this.switchMinigameButton.setVisible(false);
    }

    private void showRegularButtons() {
        this.optionsButton.setVisible(true);
        this.backupButton.setVisible(true);
        this.resetWorldButton.setVisible(true);
    }

    private void showMinigameButtons() {
        this.switchMinigameButton.setVisible(true);
    }

    public void saveSlotSettings(RealmsWorldOptions options) {
        RealmsWorldOptions oldOptions = this.serverData.slots.get(this.serverData.activeSlot);
        options.templateId = oldOptions.templateId;
        options.templateImage = oldOptions.templateImage;
        RealmsClient client = RealmsClient.createRealmsClient();
        try {
            client.updateSlot(this.serverData.id, this.serverData.activeSlot, options);
            this.serverData.slots.put(this.serverData.activeSlot, options);
        }
        catch (RealmsServiceException e) {
            LOGGER.error("Couldn't save slot settings");
            Realms.setScreen(new RealmsGenericErrorScreen(e, (RealmsScreen)this));
            return;
        }
        catch (UnsupportedEncodingException ignored) {
            LOGGER.error("Couldn't save slot settings");
        }
        Realms.setScreen(this);
    }

    public void saveSettings(String name, String desc) {
        String description = desc == null || desc.trim().isEmpty() ? null : desc;
        RealmsClient client = RealmsClient.createRealmsClient();
        try {
            client.update(this.serverData.id, name, description);
            this.serverData.setName(name);
            this.serverData.setDescription(description);
        }
        catch (RealmsServiceException e) {
            LOGGER.error("Couldn't save settings");
            Realms.setScreen(new RealmsGenericErrorScreen(e, (RealmsScreen)this));
            return;
        }
        catch (UnsupportedEncodingException ignored) {
            LOGGER.error("Couldn't save settings");
        }
        Realms.setScreen(this);
    }

    public void openTheWorld(boolean join, RealmsScreen screenInCaseOfCancel) {
        RealmsTasks.OpenServerTask openServerTask = new RealmsTasks.OpenServerTask(this.serverData, this, this.lastScreen, join);
        RealmsLongRunningMcoTaskScreen openWorldLongRunningTaskScreen = new RealmsLongRunningMcoTaskScreen(screenInCaseOfCancel, openServerTask);
        openWorldLongRunningTaskScreen.start();
        Realms.setScreen(openWorldLongRunningTaskScreen);
    }

    public void closeTheWorld(RealmsScreen screenInCaseOfCancel) {
        RealmsTasks.CloseServerTask closeServerTask = new RealmsTasks.CloseServerTask(this.serverData, this);
        RealmsLongRunningMcoTaskScreen closeWorldLongRunningTaskScreen = new RealmsLongRunningMcoTaskScreen(screenInCaseOfCancel, closeServerTask);
        closeWorldLongRunningTaskScreen.start();
        Realms.setScreen(closeWorldLongRunningTaskScreen);
    }

    public void stateChanged() {
        this.stateChanged = true;
    }

    @Override
    void callback(WorldTemplate worldTemplate) {
        if (worldTemplate == null) {
            return;
        }
        if (WorldTemplate.WorldTemplateType.MINIGAME.equals((Object)worldTemplate.type)) {
            this.switchMinigame(worldTemplate);
        }
    }

    private void switchSlot() {
        RealmsTasks.SwitchSlotTask switchSlotTask = new RealmsTasks.SwitchSlotTask(this.serverData.id, this.hoveredSlot, this.getNewScreen(), 11);
        RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, switchSlotTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }

    private void switchMinigame(WorldTemplate selectedWorldTemplate) {
        RealmsTasks.SwitchMinigameTask startMinigameTask = new RealmsTasks.SwitchMinigameTask(this.serverData.id, selectedWorldTemplate, this.getNewScreen());
        RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, startMinigameTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }

    public RealmsConfigureWorldScreen getNewScreen() {
        return new RealmsConfigureWorldScreen(this.lastScreen, this.serverId);
    }
}

