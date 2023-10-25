/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import com.mojang.realmsclient.dto.WorldDownload;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.RealmsResetWorldScreen;
import com.mojang.realmsclient.util.RealmsTasks;
import com.mojang.realmsclient.util.RealmsTextureManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsMth;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class RealmsBrokenWorldScreen
extends RealmsScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SLOT_FRAME_LOCATION = "realms:textures/gui/realms/slot_frame.png";
    private static final String EMPTY_FRAME_LOCATION = "realms:textures/gui/realms/empty_frame.png";
    private final RealmsScreen lastScreen;
    private final RealmsMainScreen mainScreen;
    private RealmsServer serverData;
    private final long serverId;
    private String title = RealmsBrokenWorldScreen.getLocalizedString("mco.brokenworld.title");
    private String message = RealmsBrokenWorldScreen.getLocalizedString("mco.brokenworld.message.line1") + "\\n" + RealmsBrokenWorldScreen.getLocalizedString("mco.brokenworld.message.line2");
    private int left_x;
    private int right_x;
    private final int default_button_width = 80;
    private final int default_button_offset = 5;
    private static final int BUTTON_BACK_ID = 0;
    private static final List<Integer> playButtonIds = Arrays.asList(1, 2, 3);
    private static final List<Integer> resetButtonIds = Arrays.asList(4, 5, 6);
    private static final List<Integer> downloadButtonIds = Arrays.asList(7, 8, 9);
    private static final List<Integer> downloadConfirmationIds = Arrays.asList(10, 11, 12);
    private final List<Integer> slotsThatHasBeenDownloaded = new ArrayList<Integer>();
    private static final int SWITCH_SLOT_ID_RESULT = 13;
    private static final int RESET_CONFIRMATION_ID = 14;
    private int animTick;

    public RealmsBrokenWorldScreen(RealmsScreen lastScreen, RealmsMainScreen mainScreen, long serverId) {
        this.lastScreen = lastScreen;
        this.mainScreen = mainScreen;
        this.serverId = serverId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void mouseEvent() {
        super.mouseEvent();
    }

    @Override
    public void init() {
        this.buttonsClear();
        this.left_x = this.width() / 2 - 150;
        this.right_x = this.width() / 2 + 190;
        this.buttonsAdd(RealmsBrokenWorldScreen.newButton(0, this.right_x - 80 + 8, RealmsConstants.row(13) - 5, 70, 20, RealmsBrokenWorldScreen.getLocalizedString("gui.back")));
        if (this.serverData == null) {
            this.fetchServerData(this.serverId);
        } else {
            this.addButtons();
        }
        Keyboard.enableRepeatEvents(true);
    }

    public void addButtons() {
        for (Map.Entry<Integer, RealmsWorldOptions> entry : this.serverData.slots.entrySet()) {
            RealmsWorldOptions slot = entry.getValue();
            boolean canPlay = entry.getKey() != this.serverData.activeSlot || this.serverData.worldType.equals((Object)RealmsServer.WorldType.MINIGAME);
            RealmsButton downloadButton = RealmsBrokenWorldScreen.newButton(canPlay ? playButtonIds.get(entry.getKey() - 1).intValue() : downloadButtonIds.get(entry.getKey() - 1).intValue(), this.getFramePositionX(entry.getKey()), RealmsConstants.row(8), 80, 20, RealmsBrokenWorldScreen.getLocalizedString(canPlay ? "mco.brokenworld.play" : "mco.brokenworld.download"));
            if (this.slotsThatHasBeenDownloaded.contains(entry.getKey())) {
                downloadButton.active(false);
                downloadButton.msg(RealmsBrokenWorldScreen.getLocalizedString("mco.brokenworld.downloaded"));
            }
            this.buttonsAdd(downloadButton);
            this.buttonsAdd(RealmsBrokenWorldScreen.newButton(resetButtonIds.get(entry.getKey() - 1), this.getFramePositionX(entry.getKey()), RealmsConstants.row(10), 80, 20, RealmsBrokenWorldScreen.getLocalizedString("mco.brokenworld.reset")));
        }
    }

    @Override
    public void tick() {
        ++this.animTick;
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.renderBackground();
        super.render(xm, ym, a);
        this.drawCenteredString(this.title, this.width() / 2, 17, 0xFFFFFF);
        String[] lines = this.message.split("\\\\n");
        for (int i = 0; i < lines.length; ++i) {
            this.drawCenteredString(lines[i], this.width() / 2, RealmsConstants.row(-1) + 3 + i * 12, 0xA0A0A0);
        }
        if (this.serverData == null) {
            return;
        }
        for (Map.Entry<Integer, RealmsWorldOptions> entry : this.serverData.slots.entrySet()) {
            if (entry.getValue().templateImage != null && entry.getValue().templateId != -1L) {
                this.drawSlotFrame(this.getFramePositionX(entry.getKey()), RealmsConstants.row(1) + 5, xm, ym, this.serverData.activeSlot == entry.getKey() && !this.isMinigame(), entry.getValue().getSlotName(entry.getKey()), entry.getKey(), entry.getValue().templateId, entry.getValue().templateImage, entry.getValue().empty);
                continue;
            }
            this.drawSlotFrame(this.getFramePositionX(entry.getKey()), RealmsConstants.row(1) + 5, xm, ym, this.serverData.activeSlot == entry.getKey() && !this.isMinigame(), entry.getValue().getSlotName(entry.getKey()), entry.getKey(), -1L, null, entry.getValue().empty);
        }
    }

    private int getFramePositionX(int i) {
        return this.left_x + (i - 1) * 110;
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
        if (playButtonIds.contains(button.id())) {
            int slot = playButtonIds.indexOf(button.id()) + 1;
            if (this.serverData.slots.get((Object)Integer.valueOf((int)slot)).empty) {
                RealmsResetWorldScreen resetWorldScreen = new RealmsResetWorldScreen(this, this.serverData, this, RealmsBrokenWorldScreen.getLocalizedString("mco.configure.world.switch.slot"), RealmsBrokenWorldScreen.getLocalizedString("mco.configure.world.switch.slot.subtitle"), 0xA0A0A0, RealmsBrokenWorldScreen.getLocalizedString("gui.cancel"));
                resetWorldScreen.setSlot(slot);
                resetWorldScreen.setResetTitle(RealmsBrokenWorldScreen.getLocalizedString("mco.create.world.reset.title"));
                resetWorldScreen.setConfirmationId(14);
                Realms.setScreen(resetWorldScreen);
            } else {
                this.switchSlot(slot);
            }
        } else if (resetButtonIds.contains(button.id())) {
            int slot = resetButtonIds.indexOf(button.id()) + 1;
            RealmsResetWorldScreen realmsResetWorldScreen = new RealmsResetWorldScreen(this, this.serverData, this);
            if (slot != this.serverData.activeSlot || this.serverData.worldType.equals((Object)RealmsServer.WorldType.MINIGAME)) {
                realmsResetWorldScreen.setSlot(slot);
            }
            realmsResetWorldScreen.setConfirmationId(14);
            Realms.setScreen(realmsResetWorldScreen);
        } else if (downloadButtonIds.contains(button.id())) {
            String line2 = RealmsBrokenWorldScreen.getLocalizedString("mco.configure.world.restore.download.question.line1");
            String line3 = RealmsBrokenWorldScreen.getLocalizedString("mco.configure.world.restore.download.question.line2");
            Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, button.id()));
        } else if (button.id() == 0) {
            this.backButtonClicked();
        }
    }

    @Override
    public void keyPressed(char ch, int eventKey) {
        if (eventKey == 1) {
            this.backButtonClicked();
        }
    }

    private void backButtonClicked() {
        Realms.setScreen(this.lastScreen);
    }

    private void fetchServerData(final long worldId) {
        new Thread(){

            @Override
            public void run() {
                RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    RealmsBrokenWorldScreen.this.serverData = client.getOwnWorld(worldId);
                    RealmsBrokenWorldScreen.this.addButtons();
                }
                catch (RealmsServiceException e) {
                    LOGGER.error("Couldn't get own world");
                    Realms.setScreen(new RealmsGenericErrorScreen(e.getMessage(), RealmsBrokenWorldScreen.this.lastScreen));
                }
                catch (IOException ignored) {
                    LOGGER.error("Couldn't parse response getting own world");
                }
            }
        }.start();
    }

    @Override
    public void confirmResult(boolean result, int id) {
        if (!result) {
            Realms.setScreen(this);
            return;
        }
        if (id == 13 || id == 14) {
            new Thread(){

                @Override
                public void run() {
                    RealmsClient client = RealmsClient.createRealmsClient();
                    if (((RealmsBrokenWorldScreen)RealmsBrokenWorldScreen.this).serverData.state.equals((Object)RealmsServer.State.CLOSED)) {
                        RealmsTasks.OpenServerTask openServerTask = new RealmsTasks.OpenServerTask(RealmsBrokenWorldScreen.this.serverData, RealmsBrokenWorldScreen.this, RealmsBrokenWorldScreen.this.lastScreen, true);
                        RealmsLongRunningMcoTaskScreen openWorldLongRunningTaskScreen = new RealmsLongRunningMcoTaskScreen(RealmsBrokenWorldScreen.this, openServerTask);
                        openWorldLongRunningTaskScreen.start();
                        Realms.setScreen(openWorldLongRunningTaskScreen);
                    } else {
                        try {
                            RealmsBrokenWorldScreen.this.mainScreen.newScreen().play(client.getOwnWorld(RealmsBrokenWorldScreen.this.serverId), RealmsBrokenWorldScreen.this);
                        }
                        catch (RealmsServiceException e) {
                            LOGGER.error("Couldn't get own world");
                            Realms.setScreen(RealmsBrokenWorldScreen.this.lastScreen);
                        }
                        catch (IOException ignored) {
                            LOGGER.error("Couldn't parse response getting own world");
                            Realms.setScreen(RealmsBrokenWorldScreen.this.lastScreen);
                        }
                    }
                }
            }.start();
        } else if (downloadButtonIds.contains(id)) {
            this.downloadWorld(downloadButtonIds.indexOf(id) + 1);
        } else if (downloadConfirmationIds.contains(id)) {
            this.slotsThatHasBeenDownloaded.add(downloadConfirmationIds.indexOf(id) + 1);
            this.buttonsClear();
            this.addButtons();
        }
    }

    private void downloadWorld(int slotId) {
        RealmsClient client = RealmsClient.createRealmsClient();
        try {
            WorldDownload worldDownload = client.download(this.serverData.id, slotId);
            RealmsDownloadLatestWorldScreen downloadScreen = new RealmsDownloadLatestWorldScreen(this, worldDownload, this.serverData.name + " (" + this.serverData.slots.get(slotId).getSlotName(slotId) + ")");
            downloadScreen.setConfirmationId(downloadConfirmationIds.get(slotId - 1));
            Realms.setScreen(downloadScreen);
        }
        catch (RealmsServiceException e) {
            LOGGER.error("Couldn't download world data");
            Realms.setScreen(new RealmsGenericErrorScreen(e, (RealmsScreen)this));
        }
    }

    private boolean isMinigame() {
        return this.serverData != null && this.serverData.worldType.equals((Object)RealmsServer.WorldType.MINIGAME);
    }

    private void drawSlotFrame(int x, int y, int xm, int ym, boolean active, String text, int i, long imageId, String image, boolean empty) {
        if (empty) {
            RealmsBrokenWorldScreen.bind(EMPTY_FRAME_LOCATION);
        } else if (image != null && imageId != -1L) {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(imageId), image);
        } else if (i == 1) {
            RealmsBrokenWorldScreen.bind("textures/gui/title/background/panorama_0.png");
        } else if (i == 2) {
            RealmsBrokenWorldScreen.bind("textures/gui/title/background/panorama_2.png");
        } else if (i == 3) {
            RealmsBrokenWorldScreen.bind("textures/gui/title/background/panorama_3.png");
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
        RealmsBrokenWorldScreen.bind(SLOT_FRAME_LOCATION);
        if (active) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            GL11.glColor4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 80, 80, 80.0f, 80.0f);
        this.drawCenteredString(text, x + 40, y + 66, 0xFFFFFF);
    }

    private void switchSlot(int id) {
        RealmsTasks.SwitchSlotTask switchSlotTask = new RealmsTasks.SwitchSlotTask(this.serverData.id, id, this, 13);
        RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, switchSlotTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }
}

