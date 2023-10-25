/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.lwjgl.input.Keyboard
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
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld
extends GuiScreen {
    private String field_146328_H;
    private GuiScreen parentScreen;
    private boolean field_146339_u;
    private boolean field_146338_v;
    private GuiButton btnAllowCommands;
    public String chunkProviderSettingsJson = "";
    private String field_146323_G;
    private GuiTextField field_146333_g;
    private String field_146329_I;
    private GuiButton btnMoreOptions;
    private String field_146330_J;
    private String field_146336_i;
    private String gameMode = "survival";
    private GuiButton btnGameMode;
    private GuiButton btnBonusItems;
    private boolean field_146337_w;
    private boolean allowCheats;
    private boolean field_146341_s = true;
    private GuiButton btnCustomizeType;
    private String field_175300_s;
    private boolean field_146344_y;
    private boolean field_146345_x;
    private GuiButton btnMapFeatures;
    private GuiButton btnMapType;
    private GuiTextField field_146335_h;
    private int selectedIndex;
    private static final String[] disallowedFilenames = new String[]{"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};

    public void func_146318_a(WorldInfo worldInfo) {
        this.field_146330_J = I18n.format("selectWorld.newWorld.copyOf", worldInfo.getWorldName());
        this.field_146329_I = String.valueOf(worldInfo.getSeed());
        this.selectedIndex = worldInfo.getTerrainType().getWorldTypeID();
        this.chunkProviderSettingsJson = worldInfo.getGeneratorOptions();
        this.field_146341_s = worldInfo.isMapFeaturesEnabled();
        this.allowCheats = worldInfo.areCommandsAllowed();
        if (worldInfo.isHardcoreModeEnabled()) {
            this.gameMode = "hardcore";
        } else if (worldInfo.getGameType().isSurvivalOrAdventure()) {
            this.gameMode = "survival";
        } else if (worldInfo.getGameType().isCreative()) {
            this.gameMode = "creative";
        }
    }

    @Override
    public void updateScreen() {
        this.field_146333_g.updateCursorCounter();
        this.field_146335_h.updateCursorCounter();
    }

    private boolean func_175299_g() {
        WorldType worldType = WorldType.worldTypes[this.selectedIndex];
        return worldType != null && worldType.getCanBeCreated() ? (worldType == WorldType.DEBUG_WORLD ? GuiCreateWorld.isShiftKeyDown() : true) : false;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 1) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (guiButton.id == 0) {
                this.mc.displayGuiScreen(null);
                if (this.field_146345_x) {
                    return;
                }
                this.field_146345_x = true;
                long l = new Random().nextLong();
                String string = this.field_146335_h.getText();
                if (!StringUtils.isEmpty((CharSequence)string)) {
                    try {
                        long l2 = Long.parseLong(string);
                        if (l2 != 0L) {
                            l = l2;
                        }
                    }
                    catch (NumberFormatException numberFormatException) {
                        l = string.hashCode();
                    }
                }
                WorldSettings.GameType gameType = WorldSettings.GameType.getByName(this.gameMode);
                WorldSettings worldSettings = new WorldSettings(l, gameType, this.field_146341_s, this.field_146337_w, WorldType.worldTypes[this.selectedIndex]);
                worldSettings.setWorldName(this.chunkProviderSettingsJson);
                if (this.field_146338_v && !this.field_146337_w) {
                    worldSettings.enableBonusChest();
                }
                if (this.allowCheats && !this.field_146337_w) {
                    worldSettings.enableCommands();
                }
                this.mc.launchIntegratedServer(this.field_146336_i, this.field_146333_g.getText().trim(), worldSettings);
            } else if (guiButton.id == 3) {
                this.func_146315_i();
            } else if (guiButton.id == 2) {
                if (this.gameMode.equals("survival")) {
                    if (!this.field_146339_u) {
                        this.allowCheats = false;
                    }
                    this.field_146337_w = false;
                    this.gameMode = "hardcore";
                    this.field_146337_w = true;
                    this.btnAllowCommands.enabled = false;
                    this.btnBonusItems.enabled = false;
                    this.func_146319_h();
                } else if (this.gameMode.equals("hardcore")) {
                    if (!this.field_146339_u) {
                        this.allowCheats = true;
                    }
                    this.field_146337_w = false;
                    this.gameMode = "creative";
                    this.func_146319_h();
                    this.field_146337_w = false;
                    this.btnAllowCommands.enabled = true;
                    this.btnBonusItems.enabled = true;
                } else {
                    if (!this.field_146339_u) {
                        this.allowCheats = false;
                    }
                    this.gameMode = "survival";
                    this.func_146319_h();
                    this.btnAllowCommands.enabled = true;
                    this.btnBonusItems.enabled = true;
                    this.field_146337_w = false;
                }
                this.func_146319_h();
            } else if (guiButton.id == 4) {
                this.field_146341_s = !this.field_146341_s;
                this.func_146319_h();
            } else if (guiButton.id == 7) {
                this.field_146338_v = !this.field_146338_v;
                this.func_146319_h();
            } else if (guiButton.id == 5) {
                ++this.selectedIndex;
                if (this.selectedIndex >= WorldType.worldTypes.length) {
                    this.selectedIndex = 0;
                }
                while (!this.func_175299_g()) {
                    ++this.selectedIndex;
                    if (this.selectedIndex < WorldType.worldTypes.length) continue;
                    this.selectedIndex = 0;
                }
                this.chunkProviderSettingsJson = "";
                this.func_146319_h();
                this.func_146316_a(this.field_146344_y);
            } else if (guiButton.id == 6) {
                this.field_146339_u = true;
                this.allowCheats = !this.allowCheats;
                this.func_146319_h();
            } else if (guiButton.id == 8) {
                if (WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT) {
                    this.mc.displayGuiScreen(new GuiCreateFlatWorld(this, this.chunkProviderSettingsJson));
                } else {
                    this.mc.displayGuiScreen(new GuiCustomizeWorldScreen(this, this.chunkProviderSettingsJson));
                }
            }
        }
    }

    public GuiCreateWorld(GuiScreen guiScreen) {
        this.parentScreen = guiScreen;
        this.field_146329_I = "";
        this.field_146330_J = I18n.format("selectWorld.newWorld", new Object[0]);
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        if (this.field_146344_y) {
            this.field_146335_h.mouseClicked(n, n2, n3);
        } else {
            this.field_146333_g.mouseClicked(n, n2, n3);
        }
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        if (this.field_146333_g.isFocused() && !this.field_146344_y) {
            this.field_146333_g.textboxKeyTyped(c, n);
            this.field_146330_J = this.field_146333_g.getText();
        } else if (this.field_146335_h.isFocused() && this.field_146344_y) {
            this.field_146335_h.textboxKeyTyped(c, n);
            this.field_146329_I = this.field_146335_h.getText();
        }
        if (n == 28 || n == 156) {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        ((GuiButton)this.buttonList.get((int)0)).enabled = this.field_146333_g.getText().length() > 0;
        this.func_146314_g();
    }

    private void func_146314_g() {
        this.field_146336_i = this.field_146333_g.getText().trim();
        char[] cArray = ChatAllowedCharacters.allowedCharactersArray;
        int n = ChatAllowedCharacters.allowedCharactersArray.length;
        int n2 = 0;
        while (n2 < n) {
            char c = cArray[n2];
            this.field_146336_i = this.field_146336_i.replace(c, '_');
            ++n2;
        }
        if (StringUtils.isEmpty((CharSequence)this.field_146336_i)) {
            this.field_146336_i = "World";
        }
        this.field_146336_i = GuiCreateWorld.func_146317_a(this.mc.getSaveLoader(), this.field_146336_i);
    }

    private void func_146315_i() {
        this.func_146316_a(!this.field_146344_y);
    }

    public static String func_146317_a(ISaveFormat iSaveFormat, String string) {
        string = string.replaceAll("[\\./\"]", "_");
        String[] stringArray = disallowedFilenames;
        int n = disallowedFilenames.length;
        int n2 = 0;
        while (n2 < n) {
            String string2 = stringArray[n2];
            if (string.equalsIgnoreCase(string2)) {
                string = "_" + string + "_";
            }
            ++n2;
        }
        while (iSaveFormat.getWorldInfo(string) != null) {
            string = String.valueOf(string) + "-";
        }
        return string;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    private void func_146319_h() {
        this.btnGameMode.displayString = String.valueOf(I18n.format("selectWorld.gameMode", new Object[0])) + ": " + I18n.format("selectWorld.gameMode." + this.gameMode, new Object[0]);
        this.field_146323_G = I18n.format("selectWorld.gameMode." + this.gameMode + ".line1", new Object[0]);
        this.field_146328_H = I18n.format("selectWorld.gameMode." + this.gameMode + ".line2", new Object[0]);
        this.btnMapFeatures.displayString = String.valueOf(I18n.format("selectWorld.mapFeatures", new Object[0])) + " ";
        this.btnMapFeatures.displayString = this.field_146341_s ? String.valueOf(this.btnMapFeatures.displayString) + I18n.format("options.on", new Object[0]) : String.valueOf(this.btnMapFeatures.displayString) + I18n.format("options.off", new Object[0]);
        this.btnBonusItems.displayString = String.valueOf(I18n.format("selectWorld.bonusItems", new Object[0])) + " ";
        this.btnBonusItems.displayString = this.field_146338_v && !this.field_146337_w ? String.valueOf(this.btnBonusItems.displayString) + I18n.format("options.on", new Object[0]) : String.valueOf(this.btnBonusItems.displayString) + I18n.format("options.off", new Object[0]);
        this.btnMapType.displayString = String.valueOf(I18n.format("selectWorld.mapType", new Object[0])) + " " + I18n.format(WorldType.worldTypes[this.selectedIndex].getTranslateName(), new Object[0]);
        this.btnAllowCommands.displayString = String.valueOf(I18n.format("selectWorld.allowCommands", new Object[0])) + " ";
        this.btnAllowCommands.displayString = this.allowCheats && !this.field_146337_w ? String.valueOf(this.btnAllowCommands.displayString) + I18n.format("options.on", new Object[0]) : String.valueOf(this.btnAllowCommands.displayString) + I18n.format("options.off", new Object[0]);
    }

    private void func_146316_a(boolean bl) {
        this.field_146344_y = bl;
        if (WorldType.worldTypes[this.selectedIndex] == WorldType.DEBUG_WORLD) {
            this.btnGameMode.visible = !this.field_146344_y;
            this.btnGameMode.enabled = false;
            if (this.field_175300_s == null) {
                this.field_175300_s = this.gameMode;
            }
            this.gameMode = "spectator";
            this.btnMapFeatures.visible = false;
            this.btnBonusItems.visible = false;
            this.btnMapType.visible = this.field_146344_y;
            this.btnAllowCommands.visible = false;
            this.btnCustomizeType.visible = false;
        } else {
            this.btnGameMode.visible = !this.field_146344_y;
            this.btnGameMode.enabled = true;
            if (this.field_175300_s != null) {
                this.gameMode = this.field_175300_s;
                this.field_175300_s = null;
            }
            this.btnMapFeatures.visible = this.field_146344_y && WorldType.worldTypes[this.selectedIndex] != WorldType.CUSTOMIZED;
            this.btnBonusItems.visible = this.field_146344_y;
            this.btnMapType.visible = this.field_146344_y;
            this.btnAllowCommands.visible = this.field_146344_y;
            this.btnCustomizeType.visible = this.field_146344_y && (WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT || WorldType.worldTypes[this.selectedIndex] == WorldType.CUSTOMIZED);
        }
        this.func_146319_h();
        this.btnMoreOptions.displayString = this.field_146344_y ? I18n.format("gui.done", new Object[0]) : I18n.format("selectWorld.moreWorldOptions", new Object[0]);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.create", new Object[0]), width / 2, 20, -1);
        if (this.field_146344_y) {
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterSeed", new Object[0]), width / 2 - 100, 47, -6250336);
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.seedInfo", new Object[0]), width / 2 - 100, 85, -6250336);
            if (this.btnMapFeatures.visible) {
                this.drawString(this.fontRendererObj, I18n.format("selectWorld.mapFeatures.info", new Object[0]), width / 2 - 150, 122, -6250336);
            }
            if (this.btnAllowCommands.visible) {
                this.drawString(this.fontRendererObj, I18n.format("selectWorld.allowCommands.info", new Object[0]), width / 2 - 150, 172, -6250336);
            }
            this.field_146335_h.drawTextBox();
            if (WorldType.worldTypes[this.selectedIndex].showWorldInfoNotice()) {
                this.fontRendererObj.drawSplitString(I18n.format(WorldType.worldTypes[this.selectedIndex].func_151359_c(), new Object[0]), this.btnMapType.xPosition + 2, this.btnMapType.yPosition + 22, this.btnMapType.getButtonWidth(), 0xA0A0A0);
            }
        } else {
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterName", new Object[0]), width / 2 - 100, 47, -6250336);
            this.drawString(this.fontRendererObj, String.valueOf(I18n.format("selectWorld.resultFolder", new Object[0])) + " " + this.field_146336_i, width / 2 - 100, 85, -6250336);
            this.field_146333_g.drawTextBox();
            this.drawString(this.fontRendererObj, this.field_146323_G, width / 2 - 100, 137, -6250336);
            this.drawString(this.fontRendererObj, this.field_146328_H, width / 2 - 100, 149, -6250336);
        }
        super.drawScreen(n, n2, f);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("selectWorld.create", new Object[0])));
        this.buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.btnGameMode = new GuiButton(2, width / 2 - 75, 115, 150, 20, I18n.format("selectWorld.gameMode", new Object[0]));
        this.buttonList.add(this.btnGameMode);
        this.btnMoreOptions = new GuiButton(3, width / 2 - 75, 187, 150, 20, I18n.format("selectWorld.moreWorldOptions", new Object[0]));
        this.buttonList.add(this.btnMoreOptions);
        this.btnMapFeatures = new GuiButton(4, width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.mapFeatures", new Object[0]));
        this.buttonList.add(this.btnMapFeatures);
        this.btnMapFeatures.visible = false;
        this.btnBonusItems = new GuiButton(7, width / 2 + 5, 151, 150, 20, I18n.format("selectWorld.bonusItems", new Object[0]));
        this.buttonList.add(this.btnBonusItems);
        this.btnBonusItems.visible = false;
        this.btnMapType = new GuiButton(5, width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.mapType", new Object[0]));
        this.buttonList.add(this.btnMapType);
        this.btnMapType.visible = false;
        this.btnAllowCommands = new GuiButton(6, width / 2 - 155, 151, 150, 20, I18n.format("selectWorld.allowCommands", new Object[0]));
        this.buttonList.add(this.btnAllowCommands);
        this.btnAllowCommands.visible = false;
        this.btnCustomizeType = new GuiButton(8, width / 2 + 5, 120, 150, 20, I18n.format("selectWorld.customizeType", new Object[0]));
        this.buttonList.add(this.btnCustomizeType);
        this.btnCustomizeType.visible = false;
        this.field_146333_g = new GuiTextField(9, this.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.field_146333_g.setFocused(true);
        this.field_146333_g.setText(this.field_146330_J);
        this.field_146335_h = new GuiTextField(10, this.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.field_146335_h.setText(this.field_146329_I);
        this.func_146316_a(this.field_146344_y);
        this.func_146314_g();
        this.func_146319_h();
    }
}

