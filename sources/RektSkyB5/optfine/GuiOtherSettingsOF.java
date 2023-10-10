/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiOtherSettingsOF
extends GuiScreen
implements GuiYesNoCallback {
    private GuiScreen prevScreen;
    protected String title = "Other Settings";
    private GameSettings settings;
    private static GameSettings.Options[] enumOptions = new GameSettings.Options[]{GameSettings.Options.LAGOMETER, GameSettings.Options.PROFILER, GameSettings.Options.WEATHER, GameSettings.Options.TIME, GameSettings.Options.USE_FULLSCREEN, GameSettings.Options.FULLSCREEN_MODE, GameSettings.Options.SHOW_FPS, GameSettings.Options.AUTOSAVE_TICKS};
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long mouseStillTime = 0L;

    public GuiOtherSettingsOF(GuiScreen p_i36_1_, GameSettings p_i36_2_) {
        this.prevScreen = p_i36_1_;
        this.settings = p_i36_2_;
    }

    @Override
    public void initGui() {
        int i2 = 0;
        for (GameSettings.Options gamesettings$options : enumOptions) {
            int j2 = this.width / 2 - 155 + i2 % 2 * 160;
            int k2 = this.height / 6 + 21 * (i2 / 2) - 10;
            if (!gamesettings$options.getEnumFloat()) {
                this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), j2, k2, gamesettings$options, this.settings.getKeyBinding(gamesettings$options)));
            } else {
                this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), j2, k2, gamesettings$options));
            }
            ++i2;
        }
        this.buttonList.add(new GuiButton(210, this.width / 2 - 100, this.height / 6 + 168 + 11 - 44, "Reset Video Settings..."));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id < 200 && button instanceof GuiOptionButton) {
                this.settings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
                button.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.prevScreen);
            }
            if (button.id == 210) {
                this.mc.gameSettings.saveOptions();
                GuiYesNo guiyesno = new GuiYesNo(this, "Reset all video settings to their default values?", "", 9999);
                this.mc.displayGuiScreen(guiyesno);
            }
            if (button.id != GameSettings.Options.CLOUD_HEIGHT.ordinal()) {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                int i2 = scaledresolution.getScaledWidth();
                int j2 = scaledresolution.getScaledHeight();
                this.setWorldAndResolution(this.mc, i2, j2);
            }
        }
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (result) {
            this.mc.gameSettings.resetSettings();
        }
        this.mc.displayGuiScreen(this);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (Math.abs(mouseX - this.lastMouseX) <= 5 && Math.abs(mouseY - this.lastMouseY) <= 5) {
            int i2 = 700;
            if (System.currentTimeMillis() >= this.mouseStillTime + (long)i2) {
                int j2 = this.width / 2 - 150;
                int k2 = this.height / 6 - 5;
                if (mouseY <= k2 + 98) {
                    k2 += 105;
                }
                int l2 = j2 + 150 + 150;
                int i1 = k2 + 84 + 10;
                GuiButton guibutton = this.getSelectedButton(mouseX, mouseY);
                if (guibutton != null) {
                    String s2 = this.getButtonName(guibutton.displayString);
                    String[] astring = this.getTooltipLines(s2);
                    if (astring == null) {
                        return;
                    }
                    this.drawGradientRect(j2, k2, l2, i1, -536870912, -536870912);
                    for (int j1 = 0; j1 < astring.length; ++j1) {
                        String s1 = astring[j1];
                        this.fontRendererObj.drawStringWithShadow(s1, j2 + 5, k2 + 5 + j1 * 11, 0xDDDDDD);
                    }
                }
            }
        } else {
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }

    private String[] getTooltipLines(String p_getTooltipLines_1_) {
        String[] stringArray;
        if (p_getTooltipLines_1_.equals("Autosave")) {
            String[] stringArray2 = new String[3];
            stringArray2[0] = "Autosave interval";
            stringArray2[1] = "Default autosave interval (2s) is NOT RECOMMENDED.";
            stringArray = stringArray2;
            stringArray2[2] = "Autosave causes the famous Lag Spike of Death.";
        } else if (p_getTooltipLines_1_.equals("Lagometer")) {
            String[] stringArray3 = new String[8];
            stringArray3[0] = "Shows the lagometer on the debug screen (F3).";
            stringArray3[1] = "* Orange - Memory garbage collection";
            stringArray3[2] = "* Cyan - Tick";
            stringArray3[3] = "* Blue - Scheduled executables";
            stringArray3[4] = "* Purple - Chunk upload";
            stringArray3[5] = "* Red - Chunk updates";
            stringArray3[6] = "* Yellow - Visibility check";
            stringArray = stringArray3;
            stringArray3[7] = "* Green - Render terrain";
        } else if (p_getTooltipLines_1_.equals("Debug Profiler")) {
            String[] stringArray4 = new String[5];
            stringArray4[0] = "Debug Profiler";
            stringArray4[1] = "  ON - debug profiler is active, slower";
            stringArray4[2] = "  OFF - debug profiler is not active, faster";
            stringArray4[3] = "The debug profiler collects and shows debug information";
            stringArray = stringArray4;
            stringArray4[4] = "when the debug screen is open (F3)";
        } else if (p_getTooltipLines_1_.equals("Time")) {
            String[] stringArray5 = new String[6];
            stringArray5[0] = "Time";
            stringArray5[1] = " Default - normal day/night cycles";
            stringArray5[2] = " Day Only - day only";
            stringArray5[3] = " Night Only - night only";
            stringArray5[4] = "The time setting is only effective in CREATIVE mode";
            stringArray = stringArray5;
            stringArray5[5] = "and for local worlds.";
        } else if (p_getTooltipLines_1_.equals("Weather")) {
            String[] stringArray6 = new String[5];
            stringArray6[0] = "Weather";
            stringArray6[1] = "  ON - weather is active, slower";
            stringArray6[2] = "  OFF - weather is not active, faster";
            stringArray6[3] = "The weather controls rain, snow and thunderstorms.";
            stringArray = stringArray6;
            stringArray6[4] = "Weather control is only possible for local worlds.";
        } else if (p_getTooltipLines_1_.equals("Fullscreen")) {
            String[] stringArray7 = new String[5];
            stringArray7[0] = "Fullscreen";
            stringArray7[1] = "  ON - use fullscreen mode";
            stringArray7[2] = "  OFF - use window mode";
            stringArray7[3] = "Fullscreen mode may be faster or slower than";
            stringArray = stringArray7;
            stringArray7[4] = "window mode, depending on the graphics card.";
        } else if (p_getTooltipLines_1_.equals("Fullscreen Mode")) {
            String[] stringArray8 = new String[5];
            stringArray8[0] = "Fullscreen mode";
            stringArray8[1] = "  Default - use desktop screen resolution, slower";
            stringArray8[2] = "  WxH - use custom screen resolution, may be faster";
            stringArray8[3] = "The selected resolution is used in fullscreen mode (F11).";
            stringArray = stringArray8;
            stringArray8[4] = "Lower resolutions should generally be faster.";
        } else if (p_getTooltipLines_1_.equals("3D Anaglyph")) {
            String[] stringArray9 = new String[1];
            stringArray = stringArray9;
            stringArray9[0] = "3D mode used with red-cyan 3D glasses.";
        } else if (p_getTooltipLines_1_.equals("Show FPS")) {
            String[] stringArray10 = new String[6];
            stringArray10[0] = "Shows compact FPS and render information";
            stringArray10[1] = "  C: - chunk renderers";
            stringArray10[2] = "  E: - rendered entities + block entities";
            stringArray10[3] = "  U: - chunk updates";
            stringArray10[4] = "The compact FPS information is only shown when the";
            stringArray = stringArray10;
            stringArray10[5] = "debug screen is not visible.";
        } else {
            stringArray = null;
        }
        return stringArray;
    }

    private String getButtonName(String p_getButtonName_1_) {
        int i2 = p_getButtonName_1_.indexOf(58);
        return i2 < 0 ? p_getButtonName_1_ : p_getButtonName_1_.substring(0, i2);
    }

    private GuiButton getSelectedButton(int p_getSelectedButton_1_, int p_getSelectedButton_2_) {
        for (int i2 = 0; i2 < this.buttonList.size(); ++i2) {
            boolean flag;
            GuiButton guibutton = (GuiButton)this.buttonList.get(i2);
            int j2 = GuiVideoSettings.getButtonWidth(guibutton);
            int k2 = GuiVideoSettings.getButtonHeight(guibutton);
            boolean bl = flag = p_getSelectedButton_1_ >= guibutton.xPosition && p_getSelectedButton_2_ >= guibutton.yPosition && p_getSelectedButton_1_ < guibutton.xPosition + j2 && p_getSelectedButton_2_ < guibutton.yPosition + k2;
            if (!flag) continue;
            return guibutton;
        }
        return null;
    }
}

