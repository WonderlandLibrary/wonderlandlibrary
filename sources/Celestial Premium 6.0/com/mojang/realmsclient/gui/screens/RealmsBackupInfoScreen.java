/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.dto.Backup;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsSimpleScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import org.lwjgl.input.Keyboard;

public class RealmsBackupInfoScreen
extends RealmsScreen {
    private final RealmsScreen lastScreen;
    private final int BUTTON_BACK_ID = 0;
    private final Backup backup;
    private final List<String> keys = new ArrayList<String>();
    private BackupInfoList backupInfoList;
    String[] difficulties = new String[]{RealmsBackupInfoScreen.getLocalizedString("options.difficulty.peaceful"), RealmsBackupInfoScreen.getLocalizedString("options.difficulty.easy"), RealmsBackupInfoScreen.getLocalizedString("options.difficulty.normal"), RealmsBackupInfoScreen.getLocalizedString("options.difficulty.hard")};
    String[] gameModes = new String[]{RealmsBackupInfoScreen.getLocalizedString("selectWorld.gameMode.survival"), RealmsBackupInfoScreen.getLocalizedString("selectWorld.gameMode.creative"), RealmsBackupInfoScreen.getLocalizedString("selectWorld.gameMode.adventure")};

    public RealmsBackupInfoScreen(RealmsScreen lastScreen, Backup backup) {
        this.lastScreen = lastScreen;
        this.backup = backup;
        if (backup.changeList != null) {
            for (Map.Entry<String, String> entry : backup.changeList.entrySet()) {
                this.keys.add(entry.getKey());
            }
        }
    }

    @Override
    public void mouseEvent() {
        super.mouseEvent();
        this.backupInfoList.mouseEvent();
    }

    @Override
    public void tick() {
    }

    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsAdd(RealmsBackupInfoScreen.newButton(0, this.width() / 2 - 100, this.height() / 4 + 120 + 24, RealmsBackupInfoScreen.getLocalizedString("gui.back")));
        this.backupInfoList = new BackupInfoList();
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
        if (button.id() == 0) {
            Realms.setScreen(this.lastScreen);
        }
    }

    @Override
    public void keyPressed(char ch, int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.renderBackground();
        this.drawCenteredString("Changes from last backup", this.width() / 2, 10, 0xFFFFFF);
        this.backupInfoList.render(xm, ym, a);
        super.render(xm, ym, a);
    }

    private String checkForSpecificMetadata(String key, String value) {
        String k = key.toLowerCase(Locale.ROOT);
        if (k.contains("game") && k.contains("mode")) {
            return this.gameModeMetadata(value);
        }
        if (k.contains("game") && k.contains("difficulty")) {
            return this.gameDifficultyMetadata(value);
        }
        return value;
    }

    private String gameDifficultyMetadata(String value) {
        try {
            return this.difficulties[Integer.parseInt(value)];
        }
        catch (Exception ignored) {
            return "UNKNOWN";
        }
    }

    private String gameModeMetadata(String value) {
        try {
            return this.gameModes[Integer.parseInt(value)];
        }
        catch (Exception ignored) {
            return "UNKNOWN";
        }
    }

    private class BackupInfoList
    extends RealmsSimpleScrolledSelectionList {
        public BackupInfoList() {
            super(RealmsBackupInfoScreen.this.width(), RealmsBackupInfoScreen.this.height(), 32, RealmsBackupInfoScreen.this.height() - 64, 36);
        }

        @Override
        public int getItemCount() {
            return ((RealmsBackupInfoScreen)RealmsBackupInfoScreen.this).backup.changeList.size();
        }

        @Override
        public void selectItem(int item, boolean doubleClick, int xMouse, int yMouse) {
        }

        @Override
        public boolean isSelectedItem(int item) {
            return false;
        }

        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 36;
        }

        @Override
        public void renderBackground() {
        }

        @Override
        protected void renderItem(int i, int x, int y, int h, Tezzelator t, int mouseX, int mouseY) {
            String key = (String)RealmsBackupInfoScreen.this.keys.get(i);
            RealmsBackupInfoScreen.this.drawString(key, this.width() / 2 - 40, y, 0xA0A0A0);
            String metadataValue = ((RealmsBackupInfoScreen)RealmsBackupInfoScreen.this).backup.changeList.get(key);
            RealmsBackupInfoScreen.this.drawString(RealmsBackupInfoScreen.this.checkForSpecificMetadata(key, metadataValue), this.width() / 2 - 40, y + 12, 0xFFFFFF);
        }
    }
}

