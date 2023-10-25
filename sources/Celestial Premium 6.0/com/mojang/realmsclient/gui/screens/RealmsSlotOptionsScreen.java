/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsEditBox;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsSliderButton;
import org.lwjgl.input.Keyboard;

public class RealmsSlotOptionsScreen
extends RealmsScreen {
    private static final int BUTTON_CANCEL_ID = 0;
    private static final int BUTTON_DONE_ID = 1;
    private static final int BUTTON_DIFFICULTY_ID = 2;
    private static final int BUTTON_GAMEMODE_ID = 3;
    private static final int BUTTON_PVP_ID = 4;
    private static final int BUTTON_SPAWN_ANIMALS_ID = 5;
    private static final int BUTTON_SPAWN_MONSTERS_ID = 6;
    private static final int BUTTON_SPAWN_NPCS_ID = 7;
    private static final int BUTTON_SPAWN_PROTECTION_ID = 8;
    private static final int BUTTON_COMMANDBLOCKS_ID = 9;
    private static final int BUTTON_FORCE_GAMEMODE_ID = 10;
    private static final int NAME_EDIT_BOX = 11;
    private RealmsEditBox nameEdit;
    protected final RealmsConfigureWorldScreen parent;
    private int column1_x;
    private int column_width;
    private int column2_x;
    private final RealmsWorldOptions options;
    private final RealmsServer.WorldType worldType;
    private final int activeSlot;
    private int difficultyIndex;
    private int gameModeIndex;
    private Boolean pvp;
    private Boolean spawnNPCs;
    private Boolean spawnAnimals;
    private Boolean spawnMonsters;
    private Integer spawnProtection;
    private Boolean commandBlocks;
    private Boolean forceGameMode;
    private RealmsButton pvpButton;
    private RealmsButton spawnAnimalsButton;
    private RealmsButton spawnMonstersButton;
    private RealmsButton spawnNPCsButton;
    private RealmsSliderButton spawnProtectionButton;
    private RealmsButton commandBlocksButton;
    private RealmsButton forceGameModeButton;
    private boolean notNormal;
    String[] difficulties;
    String[] gameModes;
    String[][] gameModeHints;

    public RealmsSlotOptionsScreen(RealmsConfigureWorldScreen configureWorldScreen, RealmsWorldOptions options, RealmsServer.WorldType worldType, int activeSlot) {
        this.parent = configureWorldScreen;
        this.options = options;
        this.worldType = worldType;
        this.activeSlot = activeSlot;
    }

    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void tick() {
        this.nameEdit.tick();
    }

    @Override
    public void buttonClicked(RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 1: {
                this.saveSettings();
                break;
            }
            case 0: {
                Realms.setScreen(this.parent);
                break;
            }
            case 2: {
                this.difficultyIndex = (this.difficultyIndex + 1) % this.difficulties.length;
                button.msg(this.difficultyTitle());
                if (!this.worldType.equals((Object)RealmsServer.WorldType.NORMAL)) break;
                this.spawnMonstersButton.active(this.difficultyIndex != 0);
                this.spawnMonstersButton.msg(this.spawnMonstersTitle());
                break;
            }
            case 3: {
                this.gameModeIndex = (this.gameModeIndex + 1) % this.gameModes.length;
                button.msg(this.gameModeTitle());
                break;
            }
            case 4: {
                this.pvp = this.pvp == false;
                button.msg(this.pvpTitle());
                break;
            }
            case 5: {
                this.spawnAnimals = this.spawnAnimals == false;
                button.msg(this.spawnAnimalsTitle());
                break;
            }
            case 7: {
                this.spawnNPCs = this.spawnNPCs == false;
                button.msg(this.spawnNPCsTitle());
                break;
            }
            case 6: {
                this.spawnMonsters = this.spawnMonsters == false;
                button.msg(this.spawnMonstersTitle());
                break;
            }
            case 9: {
                this.commandBlocks = this.commandBlocks == false;
                button.msg(this.commandBlocksTitle());
                break;
            }
            case 10: {
                this.forceGameMode = this.forceGameMode == false;
                button.msg(this.forceGameModeTitle());
                break;
            }
            default: {
                return;
            }
        }
    }

    @Override
    public void keyPressed(char eventCharacter, int eventKey) {
        this.nameEdit.keyPressed(eventCharacter, eventKey);
        switch (eventKey) {
            case 15: {
                this.nameEdit.setFocus(!this.nameEdit.isFocused());
                break;
            }
            case 1: {
                Realms.setScreen(this.parent);
                break;
            }
            case 28: 
            case 156: {
                this.saveSettings();
                break;
            }
            default: {
                return;
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y, int buttonNum) {
        super.mouseClicked(x, y, buttonNum);
        this.nameEdit.mouseClicked(x, y, buttonNum);
    }

    @Override
    public void init() {
        this.column_width = 170;
        this.column1_x = this.width() / 2 - this.column_width * 2 / 2;
        this.column2_x = this.width() / 2 + 10;
        this.createDifficultyAndGameMode();
        this.difficultyIndex = this.options.difficulty;
        this.gameModeIndex = this.options.gameMode;
        if (this.worldType.equals((Object)RealmsServer.WorldType.NORMAL)) {
            this.pvp = this.options.pvp;
            this.spawnProtection = this.options.spawnProtection;
            this.forceGameMode = this.options.forceGameMode;
            this.spawnAnimals = this.options.spawnAnimals;
            this.spawnMonsters = this.options.spawnMonsters;
            this.spawnNPCs = this.options.spawnNPCs;
            this.commandBlocks = this.options.commandBlocks;
        } else {
            this.notNormal = true;
            this.pvp = true;
            this.spawnProtection = 0;
            this.forceGameMode = false;
            this.spawnAnimals = true;
            this.spawnMonsters = true;
            this.spawnNPCs = true;
            this.commandBlocks = true;
        }
        this.nameEdit = this.newEditBox(11, this.column1_x + 2, RealmsConstants.row(1), this.column_width - 4, 20);
        this.nameEdit.setFocus(true);
        this.nameEdit.setMaxLength(10);
        this.nameEdit.setValue(this.options.getSlotName(this.activeSlot));
        this.pvpButton = RealmsSlotOptionsScreen.newButton(4, this.column2_x, RealmsConstants.row(1), this.column_width, 20, this.pvpTitle());
        this.buttonsAdd(this.pvpButton);
        this.buttonsAdd(RealmsSlotOptionsScreen.newButton(3, this.column1_x, RealmsConstants.row(3), this.column_width, 20, this.gameModeTitle()));
        this.spawnAnimalsButton = RealmsSlotOptionsScreen.newButton(5, this.column2_x, RealmsConstants.row(3), this.column_width, 20, this.spawnAnimalsTitle());
        this.buttonsAdd(this.spawnAnimalsButton);
        this.buttonsAdd(RealmsSlotOptionsScreen.newButton(2, this.column1_x, RealmsConstants.row(5), this.column_width, 20, this.difficultyTitle()));
        this.spawnMonstersButton = RealmsSlotOptionsScreen.newButton(6, this.column2_x, RealmsConstants.row(5), this.column_width, 20, this.spawnMonstersTitle());
        this.buttonsAdd(this.spawnMonstersButton);
        this.spawnProtectionButton = new SettingsSlider(8, this.column1_x, RealmsConstants.row(7), this.column_width, 17, this.spawnProtection, 0.0f, 16.0f);
        this.buttonsAdd(this.spawnProtectionButton);
        this.spawnNPCsButton = RealmsSlotOptionsScreen.newButton(7, this.column2_x, RealmsConstants.row(7), this.column_width, 20, this.spawnNPCsTitle());
        this.buttonsAdd(this.spawnNPCsButton);
        this.forceGameModeButton = RealmsSlotOptionsScreen.newButton(10, this.column1_x, RealmsConstants.row(9), this.column_width, 20, this.forceGameModeTitle());
        this.buttonsAdd(this.forceGameModeButton);
        this.commandBlocksButton = RealmsSlotOptionsScreen.newButton(9, this.column2_x, RealmsConstants.row(9), this.column_width, 20, this.commandBlocksTitle());
        this.buttonsAdd(this.commandBlocksButton);
        if (!this.worldType.equals((Object)RealmsServer.WorldType.NORMAL)) {
            this.pvpButton.active(false);
            this.spawnAnimalsButton.active(false);
            this.spawnNPCsButton.active(false);
            this.spawnMonstersButton.active(false);
            this.spawnProtectionButton.active(false);
            this.commandBlocksButton.active(false);
            this.spawnProtectionButton.active(false);
            this.forceGameModeButton.active(false);
        }
        if (this.difficultyIndex == 0) {
            this.spawnMonstersButton.active(false);
        }
        this.buttonsAdd(RealmsSlotOptionsScreen.newButton(1, this.column1_x, RealmsConstants.row(13), this.column_width, 20, RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.buttons.done")));
        this.buttonsAdd(RealmsSlotOptionsScreen.newButton(0, this.column2_x, RealmsConstants.row(13), this.column_width, 20, RealmsSlotOptionsScreen.getLocalizedString("gui.cancel")));
    }

    private void createDifficultyAndGameMode() {
        this.difficulties = new String[]{RealmsSlotOptionsScreen.getLocalizedString("options.difficulty.peaceful"), RealmsSlotOptionsScreen.getLocalizedString("options.difficulty.easy"), RealmsSlotOptionsScreen.getLocalizedString("options.difficulty.normal"), RealmsSlotOptionsScreen.getLocalizedString("options.difficulty.hard")};
        this.gameModes = new String[]{RealmsSlotOptionsScreen.getLocalizedString("selectWorld.gameMode.survival"), RealmsSlotOptionsScreen.getLocalizedString("selectWorld.gameMode.creative"), RealmsSlotOptionsScreen.getLocalizedString("selectWorld.gameMode.adventure")};
        this.gameModeHints = new String[][]{{RealmsSlotOptionsScreen.getLocalizedString("selectWorld.gameMode.survival.line1"), RealmsSlotOptionsScreen.getLocalizedString("selectWorld.gameMode.survival.line2")}, {RealmsSlotOptionsScreen.getLocalizedString("selectWorld.gameMode.creative.line1"), RealmsSlotOptionsScreen.getLocalizedString("selectWorld.gameMode.creative.line2")}, {RealmsSlotOptionsScreen.getLocalizedString("selectWorld.gameMode.adventure.line1"), RealmsSlotOptionsScreen.getLocalizedString("selectWorld.gameMode.adventure.line2")}};
    }

    private String difficultyTitle() {
        String difficulty = RealmsSlotOptionsScreen.getLocalizedString("options.difficulty");
        return difficulty + ": " + this.difficulties[this.difficultyIndex];
    }

    private String gameModeTitle() {
        String gameMode = RealmsSlotOptionsScreen.getLocalizedString("selectWorld.gameMode");
        return gameMode + ": " + this.gameModes[this.gameModeIndex];
    }

    private String pvpTitle() {
        return RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.pvp") + ": " + RealmsSlotOptionsScreen.getLocalizedString(this.pvp != false ? "mco.configure.world.on" : "mco.configure.world.off");
    }

    private String spawnAnimalsTitle() {
        return RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.spawnAnimals") + ": " + RealmsSlotOptionsScreen.getLocalizedString(this.spawnAnimals != false ? "mco.configure.world.on" : "mco.configure.world.off");
    }

    private String spawnMonstersTitle() {
        if (this.difficultyIndex == 0) {
            return RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.spawnMonsters") + ": " + RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.off");
        }
        return RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.spawnMonsters") + ": " + RealmsSlotOptionsScreen.getLocalizedString(this.spawnMonsters != false ? "mco.configure.world.on" : "mco.configure.world.off");
    }

    private String spawnNPCsTitle() {
        return RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.spawnNPCs") + ": " + RealmsSlotOptionsScreen.getLocalizedString(this.spawnNPCs != false ? "mco.configure.world.on" : "mco.configure.world.off");
    }

    private String commandBlocksTitle() {
        return RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.commandBlocks") + ": " + RealmsSlotOptionsScreen.getLocalizedString(this.commandBlocks != false ? "mco.configure.world.on" : "mco.configure.world.off");
    }

    private String forceGameModeTitle() {
        return RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.forceGameMode") + ": " + RealmsSlotOptionsScreen.getLocalizedString(this.forceGameMode != false ? "mco.configure.world.on" : "mco.configure.world.off");
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.renderBackground();
        String slotName = RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.edit.slot.name");
        this.drawString(slotName, this.column1_x + this.column_width / 2 - this.fontWidth(slotName) / 2, RealmsConstants.row(0) - 5, 0xFFFFFF);
        this.drawCenteredString(RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.buttons.options"), this.width() / 2, 17, 0xFFFFFF);
        if (this.notNormal) {
            if (this.worldType.equals((Object)RealmsServer.WorldType.ADVENTUREMAP)) {
                this.drawCenteredString(RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.edit.subscreen.adventuremap"), this.width() / 2, 26, 0xFF0000);
            } else if (this.worldType.equals((Object)RealmsServer.WorldType.INSPIRATION)) {
                this.drawCenteredString(RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.edit.subscreen.inspiration"), this.width() / 2, 26, 0xFF0000);
            } else {
                this.drawCenteredString(RealmsSlotOptionsScreen.getLocalizedString("mco.configure.world.edit.subscreen.experience"), this.width() / 2, 26, 0xFF0000);
            }
        }
        this.nameEdit.render();
        super.render(xm, ym, a);
    }

    @Override
    public void mouseReleased(int x, int y, int buttonNum) {
        if (!this.spawnProtectionButton.active()) {
            return;
        }
        this.spawnProtectionButton.released(x, y);
    }

    @Override
    public void mouseDragged(int x, int y, int buttonNum, long delta) {
        if (!this.spawnProtectionButton.active()) {
            return;
        }
        if (x < this.column1_x + this.spawnProtectionButton.getWidth() && x > this.column1_x && y < this.spawnProtectionButton.y() + 20 && y > this.spawnProtectionButton.y()) {
            this.spawnProtectionButton.clicked(x, y);
        }
    }

    private String getSlotName() {
        if (this.nameEdit.getValue().equals(this.options.getDefaultSlotName(this.activeSlot))) {
            return "";
        }
        return this.nameEdit.getValue();
    }

    private void saveSettings() {
        if (this.worldType.equals((Object)RealmsServer.WorldType.ADVENTUREMAP) || this.worldType.equals((Object)RealmsServer.WorldType.EXPERIENCE) || this.worldType.equals((Object)RealmsServer.WorldType.INSPIRATION)) {
            this.parent.saveSlotSettings(new RealmsWorldOptions(this.options.pvp, this.options.spawnAnimals, this.options.spawnMonsters, this.options.spawnNPCs, this.options.spawnProtection, this.options.commandBlocks, this.difficultyIndex, this.gameModeIndex, this.options.forceGameMode, this.getSlotName()));
        } else {
            this.parent.saveSlotSettings(new RealmsWorldOptions(this.pvp, this.spawnAnimals, this.spawnMonsters, this.spawnNPCs, this.spawnProtection, this.commandBlocks, this.difficultyIndex, this.gameModeIndex, this.forceGameMode, this.getSlotName()));
        }
    }

    private class SettingsSlider
    extends RealmsSliderButton {
        public SettingsSlider(int id, int x, int y, int width, int steps, int currentValue, float minValue, float maxValue) {
            super(id, x, y, width, steps, currentValue, minValue, maxValue);
        }

        @Override
        public String getMessage() {
            return RealmsScreen.getLocalizedString("mco.configure.world.spawnProtection") + ": " + (RealmsSlotOptionsScreen.this.spawnProtection == 0 ? RealmsScreen.getLocalizedString("mco.configure.world.off") : RealmsSlotOptionsScreen.this.spawnProtection);
        }

        @Override
        public void clicked(float value) {
            if (!RealmsSlotOptionsScreen.this.spawnProtectionButton.active()) {
                return;
            }
            RealmsSlotOptionsScreen.this.spawnProtection = (int)value;
        }
    }
}

