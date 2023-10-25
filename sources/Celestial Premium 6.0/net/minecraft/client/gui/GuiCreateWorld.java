/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Random;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.gui.GuiCustomizeWorldScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld
extends GuiScreen {
    private final GuiScreen parentScreen;
    private GuiTextField worldNameField;
    private GuiTextField worldSeedField;
    private String saveDirName;
    private String gameMode = "survival";
    private String savedGameMode;
    private boolean generateStructuresEnabled = true;
    private boolean allowCheats;
    private boolean allowCheatsWasSetByUser;
    private boolean bonusChestEnabled;
    private boolean hardCoreMode;
    private boolean alreadyGenerated;
    private boolean inMoreWorldOptionsDisplay;
    private GuiButton btnGameMode;
    private GuiButton btnMoreOptions;
    private GuiButton btnMapFeatures;
    private GuiButton btnBonusItems;
    private GuiButton btnMapType;
    private GuiButton btnAllowCommands;
    private GuiButton btnCustomizeType;
    private String gameModeDesc1;
    private String gameModeDesc2;
    private String worldSeed;
    private String worldName;
    private int selectedIndex;
    public String chunkProviderSettingsJson = "";
    private static final String[] DISALLOWED_FILENAMES = new String[]{"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};

    public GuiCreateWorld(GuiScreen p_i46320_1_) {
        this.parentScreen = p_i46320_1_;
        this.worldSeed = "";
        this.worldName = I18n.format("selectWorld.newWorld", new Object[0]);
    }

    @Override
    public void updateScreen() {
        this.worldNameField.updateCursorCounter();
        this.worldSeedField.updateCursorCounter();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("selectWorld.create", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.btnGameMode = this.addButton(new GuiButton(2, this.width / 2 - 75, 115, 150, 20, I18n.format("selectWorld.gameMode", new Object[0])));
        this.btnMoreOptions = this.addButton(new GuiButton(3, this.width / 2 - 75, 187, 150, 20, I18n.format("selectWorld.moreWorldOptions", new Object[0])));
        this.btnMapFeatures = this.addButton(new GuiButton(4, this.width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.mapFeatures", new Object[0])));
        this.btnMapFeatures.visible = false;
        this.btnBonusItems = this.addButton(new GuiButton(7, this.width / 2 + 5, 151, 150, 20, I18n.format("selectWorld.bonusItems", new Object[0])));
        this.btnBonusItems.visible = false;
        this.btnMapType = this.addButton(new GuiButton(5, this.width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.mapType", new Object[0])));
        this.btnMapType.visible = false;
        this.btnAllowCommands = this.addButton(new GuiButton(6, this.width / 2 - 155, 151, 150, 20, I18n.format("selectWorld.allowCommands", new Object[0])));
        this.btnAllowCommands.visible = false;
        this.btnCustomizeType = this.addButton(new GuiButton(8, this.width / 2 + 5, 120, 150, 20, I18n.format("selectWorld.customizeType", new Object[0])));
        this.btnCustomizeType.visible = false;
        this.worldNameField = new GuiTextField(9, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.worldNameField.setFocused(true);
        this.worldNameField.setText(this.worldName);
        this.worldSeedField = new GuiTextField(10, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.worldSeedField.setText(this.worldSeed);
        this.showMoreWorldOptions(this.inMoreWorldOptionsDisplay);
        this.calcSaveDirName();
        this.updateDisplayState();
    }

    private void calcSaveDirName() {
        this.saveDirName = this.worldNameField.getText().trim();
        for (char c0 : ChatAllowedCharacters.ILLEGAL_FILE_CHARACTERS) {
            this.saveDirName = this.saveDirName.replace(c0, '_');
        }
        if (StringUtils.isEmpty(this.saveDirName)) {
            this.saveDirName = "World";
        }
        this.saveDirName = GuiCreateWorld.getUncollidingSaveDirName(this.mc.getSaveLoader(), this.saveDirName);
    }

    private void updateDisplayState() {
        this.btnGameMode.displayString = I18n.format("selectWorld.gameMode", new Object[0]) + ": " + I18n.format("selectWorld.gameMode." + this.gameMode, new Object[0]);
        this.gameModeDesc1 = I18n.format("selectWorld.gameMode." + this.gameMode + ".line1", new Object[0]);
        this.gameModeDesc2 = I18n.format("selectWorld.gameMode." + this.gameMode + ".line2", new Object[0]);
        this.btnMapFeatures.displayString = I18n.format("selectWorld.mapFeatures", new Object[0]) + " ";
        this.btnMapFeatures.displayString = this.generateStructuresEnabled ? this.btnMapFeatures.displayString + I18n.format("options.on", new Object[0]) : this.btnMapFeatures.displayString + I18n.format("options.off", new Object[0]);
        this.btnBonusItems.displayString = I18n.format("selectWorld.bonusItems", new Object[0]) + " ";
        this.btnBonusItems.displayString = this.bonusChestEnabled && !this.hardCoreMode ? this.btnBonusItems.displayString + I18n.format("options.on", new Object[0]) : this.btnBonusItems.displayString + I18n.format("options.off", new Object[0]);
        this.btnMapType.displayString = I18n.format("selectWorld.mapType", new Object[0]) + " " + I18n.format(WorldType.WORLD_TYPES[this.selectedIndex].getTranslateName(), new Object[0]);
        this.btnAllowCommands.displayString = I18n.format("selectWorld.allowCommands", new Object[0]) + " ";
        this.btnAllowCommands.displayString = this.allowCheats && !this.hardCoreMode ? this.btnAllowCommands.displayString + I18n.format("options.on", new Object[0]) : this.btnAllowCommands.displayString + I18n.format("options.off", new Object[0]);
    }

    public static String getUncollidingSaveDirName(ISaveFormat saveLoader, String name) {
        name = name.replaceAll("[\\./\"]", "_");
        for (String s : DISALLOWED_FILENAMES) {
            if (!name.equalsIgnoreCase(s)) continue;
            name = "_" + name + "_";
        }
        while (saveLoader.getWorldInfo(name) != null) {
            name = name + "-";
        }
        return name;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (button.id == 0) {
                this.mc.displayGuiScreen(null);
                if (this.alreadyGenerated) {
                    return;
                }
                this.alreadyGenerated = true;
                long i = new Random().nextLong();
                String s = this.worldSeedField.getText();
                if (!StringUtils.isEmpty(s)) {
                    try {
                        long j = Long.parseLong(s);
                        if (j != 0L) {
                            i = j;
                        }
                    }
                    catch (NumberFormatException var7) {
                        i = s.hashCode();
                    }
                }
                WorldSettings worldsettings = new WorldSettings(i, GameType.getByName(this.gameMode), this.generateStructuresEnabled, this.hardCoreMode, WorldType.WORLD_TYPES[this.selectedIndex]);
                worldsettings.setGeneratorOptions(this.chunkProviderSettingsJson);
                if (this.bonusChestEnabled && !this.hardCoreMode) {
                    worldsettings.enableBonusChest();
                }
                if (this.allowCheats && !this.hardCoreMode) {
                    worldsettings.enableCommands();
                }
                this.mc.launchIntegratedServer(this.saveDirName, this.worldNameField.getText().trim(), worldsettings);
            } else if (button.id == 3) {
                this.toggleMoreWorldOptions();
            } else if (button.id == 2) {
                if ("survival".equals(this.gameMode)) {
                    if (!this.allowCheatsWasSetByUser) {
                        this.allowCheats = false;
                    }
                    this.hardCoreMode = false;
                    this.gameMode = "hardcore";
                    this.hardCoreMode = true;
                    this.btnAllowCommands.enabled = false;
                    this.btnBonusItems.enabled = false;
                    this.updateDisplayState();
                } else if ("hardcore".equals(this.gameMode)) {
                    if (!this.allowCheatsWasSetByUser) {
                        this.allowCheats = true;
                    }
                    this.hardCoreMode = false;
                    this.gameMode = "creative";
                    this.updateDisplayState();
                    this.hardCoreMode = false;
                    this.btnAllowCommands.enabled = true;
                    this.btnBonusItems.enabled = true;
                } else {
                    if (!this.allowCheatsWasSetByUser) {
                        this.allowCheats = false;
                    }
                    this.gameMode = "survival";
                    this.updateDisplayState();
                    this.btnAllowCommands.enabled = true;
                    this.btnBonusItems.enabled = true;
                    this.hardCoreMode = false;
                }
                this.updateDisplayState();
            } else if (button.id == 4) {
                this.generateStructuresEnabled = !this.generateStructuresEnabled;
                this.updateDisplayState();
            } else if (button.id == 7) {
                this.bonusChestEnabled = !this.bonusChestEnabled;
                this.updateDisplayState();
            } else if (button.id == 5) {
                ++this.selectedIndex;
                if (this.selectedIndex >= WorldType.WORLD_TYPES.length) {
                    this.selectedIndex = 0;
                }
                while (!this.canSelectCurWorldType()) {
                    ++this.selectedIndex;
                    if (this.selectedIndex < WorldType.WORLD_TYPES.length) continue;
                    this.selectedIndex = 0;
                }
                this.chunkProviderSettingsJson = "";
                this.updateDisplayState();
                this.showMoreWorldOptions(this.inMoreWorldOptionsDisplay);
            } else if (button.id == 6) {
                this.allowCheatsWasSetByUser = true;
                this.allowCheats = !this.allowCheats;
                this.updateDisplayState();
            } else if (button.id == 8) {
                if (WorldType.WORLD_TYPES[this.selectedIndex] == WorldType.FLAT) {
                    this.mc.displayGuiScreen(new GuiCreateFlatWorld(this, this.chunkProviderSettingsJson));
                } else {
                    this.mc.displayGuiScreen(new GuiCustomizeWorldScreen(this, this.chunkProviderSettingsJson));
                }
            }
        }
    }

    private boolean canSelectCurWorldType() {
        WorldType worldtype = WorldType.WORLD_TYPES[this.selectedIndex];
        if (worldtype != null && worldtype.getCanBeCreated()) {
            return worldtype == WorldType.DEBUG_WORLD ? GuiCreateWorld.isShiftKeyDown() : true;
        }
        return false;
    }

    private void toggleMoreWorldOptions() {
        this.showMoreWorldOptions(!this.inMoreWorldOptionsDisplay);
    }

    private void showMoreWorldOptions(boolean toggle) {
        this.inMoreWorldOptionsDisplay = toggle;
        if (WorldType.WORLD_TYPES[this.selectedIndex] == WorldType.DEBUG_WORLD) {
            this.btnGameMode.visible = !this.inMoreWorldOptionsDisplay;
            this.btnGameMode.enabled = false;
            if (this.savedGameMode == null) {
                this.savedGameMode = this.gameMode;
            }
            this.gameMode = "spectator";
            this.btnMapFeatures.visible = false;
            this.btnBonusItems.visible = false;
            this.btnMapType.visible = this.inMoreWorldOptionsDisplay;
            this.btnAllowCommands.visible = false;
            this.btnCustomizeType.visible = false;
        } else {
            this.btnGameMode.visible = !this.inMoreWorldOptionsDisplay;
            this.btnGameMode.enabled = true;
            if (this.savedGameMode != null) {
                this.gameMode = this.savedGameMode;
                this.savedGameMode = null;
            }
            this.btnMapFeatures.visible = this.inMoreWorldOptionsDisplay && WorldType.WORLD_TYPES[this.selectedIndex] != WorldType.CUSTOMIZED;
            this.btnBonusItems.visible = this.inMoreWorldOptionsDisplay;
            this.btnMapType.visible = this.inMoreWorldOptionsDisplay;
            this.btnAllowCommands.visible = this.inMoreWorldOptionsDisplay;
            this.btnCustomizeType.visible = this.inMoreWorldOptionsDisplay && (WorldType.WORLD_TYPES[this.selectedIndex] == WorldType.FLAT || WorldType.WORLD_TYPES[this.selectedIndex] == WorldType.CUSTOMIZED);
        }
        this.updateDisplayState();
        this.btnMoreOptions.displayString = this.inMoreWorldOptionsDisplay ? I18n.format("gui.done", new Object[0]) : I18n.format("selectWorld.moreWorldOptions", new Object[0]);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.worldNameField.isFocused() && !this.inMoreWorldOptionsDisplay) {
            this.worldNameField.textboxKeyTyped(typedChar, keyCode);
            this.worldName = this.worldNameField.getText();
        } else if (this.worldSeedField.isFocused() && this.inMoreWorldOptionsDisplay) {
            this.worldSeedField.textboxKeyTyped(typedChar, keyCode);
            this.worldSeed = this.worldSeedField.getText();
        }
        if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        ((GuiButton)this.buttonList.get((int)0)).enabled = !this.worldNameField.getText().isEmpty();
        this.calcSaveDirName();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.inMoreWorldOptionsDisplay) {
            this.worldSeedField.mouseClicked(mouseX, mouseY, mouseButton);
        } else {
            this.worldNameField.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.create", new Object[0]), this.width / 2, 20, -1);
        if (this.inMoreWorldOptionsDisplay) {
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterSeed", new Object[0]), this.width / 2 - 100, 47, -6250336);
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.seedInfo", new Object[0]), this.width / 2 - 100, 85, -6250336);
            if (this.btnMapFeatures.visible) {
                this.drawString(this.fontRendererObj, I18n.format("selectWorld.mapFeatures.info", new Object[0]), this.width / 2 - 150, 122, -6250336);
            }
            if (this.btnAllowCommands.visible) {
                this.drawString(this.fontRendererObj, I18n.format("selectWorld.allowCommands.info", new Object[0]), this.width / 2 - 150, 172, -6250336);
            }
            this.worldSeedField.drawTextBox();
            if (WorldType.WORLD_TYPES[this.selectedIndex].showWorldInfoNotice()) {
                this.fontRendererObj.drawSplitString(I18n.format(WorldType.WORLD_TYPES[this.selectedIndex].getTranslatedInfo(), new Object[0]), this.btnMapType.xPosition + 2, this.btnMapType.yPosition + 22, this.btnMapType.getButtonWidth(), 0xA0A0A0);
            }
        } else {
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterName", new Object[0]), this.width / 2 - 100, 47, -6250336);
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.resultFolder", new Object[0]) + " " + this.saveDirName, this.width / 2 - 100, 85, -6250336);
            this.worldNameField.drawTextBox();
            this.drawString(this.fontRendererObj, this.gameModeDesc1, this.width / 2 - 100, 137, -6250336);
            this.drawString(this.fontRendererObj, this.gameModeDesc2, this.width / 2 - 100, 149, -6250336);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void recreateFromExistingWorld(WorldInfo original) {
        this.worldName = I18n.format("selectWorld.newWorld.copyOf", original.getWorldName());
        this.worldSeed = original.getSeed() + "";
        this.selectedIndex = original.getTerrainType().getWorldTypeID();
        this.chunkProviderSettingsJson = original.getGeneratorOptions();
        this.generateStructuresEnabled = original.isMapFeaturesEnabled();
        this.allowCheats = original.areCommandsAllowed();
        if (original.isHardcoreModeEnabled()) {
            this.gameMode = "hardcore";
        } else if (original.getGameType().isSurvivalOrAdventure()) {
            this.gameMode = "survival";
        } else if (original.getGameType().isCreative()) {
            this.gameMode = "creative";
        }
    }
}

