/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsResetWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsUploadScreen;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsAnvilLevelStorageSource;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsLevelSummary;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class RealmsSelectFileToUploadScreen
extends RealmsScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int CANCEL_BUTTON = 1;
    private static final int UPLOAD_BUTTON = 2;
    private final RealmsResetWorldScreen lastScreen;
    private final long worldId;
    private final int slotId;
    private RealmsButton uploadButton;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat();
    private List<RealmsLevelSummary> levelList = new ArrayList<RealmsLevelSummary>();
    private int selectedWorld = -1;
    private WorldSelectionList worldSelectionList;
    private String worldLang;
    private String conversionLang;
    private final String[] gameModesLang = new String[4];

    public RealmsSelectFileToUploadScreen(long worldId, int slotId, RealmsResetWorldScreen lastScreen) {
        this.lastScreen = lastScreen;
        this.worldId = worldId;
        this.slotId = slotId;
    }

    private void loadLevelList() throws Exception {
        RealmsAnvilLevelStorageSource levelSource = this.getLevelStorageSource();
        this.levelList = levelSource.getLevelList();
        Collections.sort(this.levelList);
    }

    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        try {
            this.loadLevelList();
        }
        catch (Exception e) {
            LOGGER.error("Couldn't load level list", (Throwable)e);
            Realms.setScreen(new RealmsGenericErrorScreen("Unable to load worlds", e.getMessage(), this.lastScreen));
            return;
        }
        this.worldLang = RealmsSelectFileToUploadScreen.getLocalizedString("selectWorld.world");
        this.conversionLang = RealmsSelectFileToUploadScreen.getLocalizedString("selectWorld.conversion");
        this.gameModesLang[Realms.survivalId()] = RealmsSelectFileToUploadScreen.getLocalizedString("gameMode.survival");
        this.gameModesLang[Realms.creativeId()] = RealmsSelectFileToUploadScreen.getLocalizedString("gameMode.creative");
        this.gameModesLang[Realms.adventureId()] = RealmsSelectFileToUploadScreen.getLocalizedString("gameMode.adventure");
        this.gameModesLang[Realms.spectatorId()] = RealmsSelectFileToUploadScreen.getLocalizedString("gameMode.spectator");
        this.buttonsAdd(RealmsSelectFileToUploadScreen.newButton(1, this.width() / 2 + 6, this.height() - 32, 153, 20, RealmsSelectFileToUploadScreen.getLocalizedString("gui.back")));
        this.uploadButton = RealmsSelectFileToUploadScreen.newButton(2, this.width() / 2 - 154, this.height() - 32, 153, 20, RealmsSelectFileToUploadScreen.getLocalizedString("mco.upload.button.name"));
        this.buttonsAdd(this.uploadButton);
        this.uploadButton.active(this.selectedWorld >= 0 && this.selectedWorld < this.levelList.size());
        this.worldSelectionList = new WorldSelectionList();
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
        if (button.id() == 1) {
            Realms.setScreen(this.lastScreen);
        } else if (button.id() == 2) {
            this.upload();
        }
    }

    private void upload() {
        if (this.selectedWorld != -1 && !this.levelList.get(this.selectedWorld).isHardcore()) {
            RealmsLevelSummary selectedLevel = this.levelList.get(this.selectedWorld);
            Realms.setScreen(new RealmsUploadScreen(this.worldId, this.slotId, this.lastScreen, selectedLevel));
        }
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.renderBackground();
        this.worldSelectionList.render(xm, ym, a);
        this.drawCenteredString(RealmsSelectFileToUploadScreen.getLocalizedString("mco.upload.select.world.title"), this.width() / 2, 13, 0xFFFFFF);
        this.drawCenteredString(RealmsSelectFileToUploadScreen.getLocalizedString("mco.upload.select.world.subtitle"), this.width() / 2, RealmsConstants.row(-1), 0xA0A0A0);
        if (this.levelList.size() == 0) {
            this.drawCenteredString(RealmsSelectFileToUploadScreen.getLocalizedString("mco.upload.select.world.none"), this.width() / 2, this.height() / 2 - 20, 0xFFFFFF);
        }
        super.render(xm, ym, a);
    }

    @Override
    public void keyPressed(char eventCharacter, int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }

    @Override
    public void mouseEvent() {
        super.mouseEvent();
        this.worldSelectionList.mouseEvent();
    }

    @Override
    public void tick() {
        super.tick();
    }

    private class WorldSelectionList
    extends RealmsScrolledSelectionList {
        public WorldSelectionList() {
            super(RealmsSelectFileToUploadScreen.this.width(), RealmsSelectFileToUploadScreen.this.height(), RealmsConstants.row(0), RealmsSelectFileToUploadScreen.this.height() - 40, 36);
        }

        @Override
        public int getItemCount() {
            return RealmsSelectFileToUploadScreen.this.levelList.size();
        }

        @Override
        public void selectItem(int item, boolean doubleClick, int xMouse, int yMouse) {
            RealmsSelectFileToUploadScreen.this.selectedWorld = item;
            RealmsSelectFileToUploadScreen.this.uploadButton.active(RealmsSelectFileToUploadScreen.this.selectedWorld >= 0 && RealmsSelectFileToUploadScreen.this.selectedWorld < this.getItemCount() && !((RealmsLevelSummary)RealmsSelectFileToUploadScreen.this.levelList.get(RealmsSelectFileToUploadScreen.this.selectedWorld)).isHardcore());
            if (doubleClick) {
                RealmsSelectFileToUploadScreen.this.upload();
            }
        }

        @Override
        public boolean isSelectedItem(int item) {
            return item == RealmsSelectFileToUploadScreen.this.selectedWorld;
        }

        @Override
        public int getMaxPosition() {
            return RealmsSelectFileToUploadScreen.this.levelList.size() * 36;
        }

        @Override
        public void renderBackground() {
            RealmsSelectFileToUploadScreen.this.renderBackground();
        }

        @Override
        protected void renderItem(int i, int x, int y, int h, Tezzelator t, int mouseX, int mouseY) {
            RealmsLevelSummary levelSummary = (RealmsLevelSummary)RealmsSelectFileToUploadScreen.this.levelList.get(i);
            String name = levelSummary.getLevelName();
            if (name == null || name.isEmpty()) {
                name = RealmsSelectFileToUploadScreen.this.worldLang + " " + (i + 1);
            }
            String id = levelSummary.getLevelId();
            id = id + " (" + RealmsSelectFileToUploadScreen.this.DATE_FORMAT.format(new Date(levelSummary.getLastPlayed()));
            id = id + ")";
            String info = "";
            if (levelSummary.isRequiresConversion()) {
                info = RealmsSelectFileToUploadScreen.this.conversionLang + " " + info;
            } else {
                info = RealmsSelectFileToUploadScreen.this.gameModesLang[levelSummary.getGameMode()];
                if (levelSummary.isHardcore()) {
                    info = (Object)((Object)ChatFormatting.DARK_RED) + RealmsScreen.getLocalizedString("mco.upload.hardcore") + (Object)((Object)ChatFormatting.RESET);
                }
                if (levelSummary.hasCheats()) {
                    info = info + ", " + RealmsScreen.getLocalizedString("selectWorld.cheats");
                }
            }
            RealmsSelectFileToUploadScreen.this.drawString(name, x + 2, y + 1, 0xFFFFFF);
            RealmsSelectFileToUploadScreen.this.drawString(id, x + 2, y + 12, 0x808080);
            RealmsSelectFileToUploadScreen.this.drawString(info, x + 2, y + 12 + 10, 0x808080);
        }
    }
}

