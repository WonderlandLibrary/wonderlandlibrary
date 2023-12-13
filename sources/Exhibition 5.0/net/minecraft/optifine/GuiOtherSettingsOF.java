// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiScreen;

public class GuiOtherSettingsOF extends GuiScreen implements GuiYesNoCallback
{
    private GuiScreen prevScreen;
    protected String title;
    private GameSettings settings;
    private static GameSettings.Options[] enumOptions;
    private int lastMouseX;
    private int lastMouseY;
    private long mouseStillTime;
    
    public GuiOtherSettingsOF(final GuiScreen guiscreen, final GameSettings gamesettings) {
        this.title = "Other Settings";
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.mouseStillTime = 0L;
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }
    
    @Override
    public void initGui() {
        int i = 0;
        for (final GameSettings.Options enumoptions : GuiOtherSettingsOF.enumOptions) {
            final int x = this.width / 2 - 155 + i % 2 * 160;
            final int y = this.height / 6 + 21 * (i / 2) - 10;
            if (!enumoptions.getEnumFloat()) {
                this.buttonList.add(new GuiOptionButton(enumoptions.returnEnumOrdinal(), x, y, enumoptions, this.settings.getKeyBinding(enumoptions)));
            }
            else {
                this.buttonList.add(new GuiOptionSlider(enumoptions.returnEnumOrdinal(), x, y, enumoptions));
            }
            ++i;
        }
        this.buttonList.add(new GuiButton(210, this.width / 2 - 100, this.height / 6 + 168 + 11 - 44, "Reset Video Settings..."));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id < 200 && guibutton instanceof GuiOptionButton) {
                this.settings.setOptionValue(((GuiOptionButton)guibutton).returnEnumOptions(), 1);
                guibutton.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(guibutton.id));
            }
            if (guibutton.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.prevScreen);
            }
            if (guibutton.id == 210) {
                this.mc.gameSettings.saveOptions();
                final GuiYesNo scaledresolution = new GuiYesNo(this, "Reset all video settings to their default values?", "", 9999);
                this.mc.displayGuiScreen(scaledresolution);
            }
            if (guibutton.id != GameSettings.Options.CLOUD_HEIGHT.ordinal()) {
                final ScaledResolution scaledresolution2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                final int i = scaledresolution2.getScaledWidth();
                final int j = scaledresolution2.getScaledHeight();
                this.setWorldAndResolution(this.mc, i, j);
            }
        }
    }
    
    @Override
    public void confirmClicked(final boolean flag, final int i) {
        if (flag) {
            this.mc.gameSettings.resetSettings();
        }
        this.mc.displayGuiScreen(this);
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 20, 16777215);
        super.drawScreen(x, y, f);
        if (Math.abs(x - this.lastMouseX) <= 5 && Math.abs(y - this.lastMouseY) <= 5) {
            final short activateDelay = 700;
            if (System.currentTimeMillis() >= this.mouseStillTime + activateDelay) {
                final int x2 = this.width / 2 - 150;
                int y2 = this.height / 6 - 5;
                if (y <= y2 + 98) {
                    y2 += 105;
                }
                final int x3 = x2 + 150 + 150;
                final int y3 = y2 + 84 + 10;
                final GuiButton btn = this.getSelectedButton(x, y);
                if (btn != null) {
                    final String s = this.getButtonName(btn.displayString);
                    final String[] lines = this.getTooltipLines(s);
                    if (lines == null) {
                        return;
                    }
                    this.drawGradientRect(x2, y2, x3, y3, -536870912, -536870912);
                    for (int i = 0; i < lines.length; ++i) {
                        final String line = lines[i];
                        this.fontRendererObj.drawStringWithShadow(line, x2 + 5, y2 + 5 + i * 11, 14540253);
                    }
                }
            }
        }
        else {
            this.lastMouseX = x;
            this.lastMouseY = y;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }
    
    private String[] getTooltipLines(final String btnName) {
        final String[] array9;
        if (btnName.equals("Autosave")) {
            final String[] array2;
            final String[] array = array2 = new String[3];
            array[0] = "Autosave interval";
            array[1] = "Default autosave interval (2s) is NOT RECOMMENDED.";
            array[2] = "Autosave causes the famous Lag Spike of Death.";
        }
        else if (btnName.equals("Lagometer")) {
            final String[] array2;
            final String[] array3 = array2 = new String[8];
            array3[0] = "Shows the lagometer on the debug screen (F3).";
            array3[1] = "* Orange - Memory garbage collection";
            array3[2] = "* Cyan - Tick";
            array3[3] = "* Blue - Scheduled executables";
            array3[4] = "* Purple - Chunk upload";
            array3[5] = "* Red - Chunk updates";
            array3[6] = "* Yellow - Visibility check";
            array3[7] = "* Green - Render terrain";
        }
        else if (btnName.equals("Debug Profiler")) {
            final String[] array2;
            final String[] array4 = array2 = new String[5];
            array4[0] = "Debug Profiler";
            array4[1] = "  ON - debug profiler is active, slower";
            array4[2] = "  OFF - debug profiler is not active, faster";
            array4[3] = "The debug profiler collects and shows debug information";
            array4[4] = "when the debug screen is open (F3)";
        }
        else if (btnName.equals("Time")) {
            final String[] array2;
            final String[] array5 = array2 = new String[6];
            array5[0] = "Time";
            array5[1] = " Default - normal day/night cycles";
            array5[2] = " Day Only - day only";
            array5[3] = " Night Only - night only";
            array5[4] = "The time setting is only effective in CREATIVE mode";
            array5[5] = "and for local worlds.";
        }
        else if (btnName.equals("Weather")) {
            final String[] array2;
            final String[] array6 = array2 = new String[5];
            array6[0] = "Weather";
            array6[1] = "  ON - weather is active, slower";
            array6[2] = "  OFF - weather is not active, faster";
            array6[3] = "The weather controls rain, snow and thunderstorms.";
            array6[4] = "Weather control is only possible for local worlds.";
        }
        else if (btnName.equals("Fullscreen")) {
            final String[] array2;
            final String[] array7 = array2 = new String[5];
            array7[0] = "Fullscreen";
            array7[1] = "  ON - use fullscreen mode";
            array7[2] = "  OFF - use window mode";
            array7[3] = "Fullscreen mode may be faster or slower than";
            array7[4] = "window mode, depending on the graphics card.";
        }
        else if (btnName.equals("Fullscreen Mode")) {
            final String[] array2;
            final String[] array8 = array2 = new String[5];
            array8[0] = "Fullscreen mode";
            array8[1] = "  Default - use desktop screen resolution, slower";
            array8[2] = "  WxH - use custom screen resolution, may be faster";
            array8[3] = "The selected resolution is used in fullscreen mode (F11).";
            array8[4] = "Lower resolutions should generally be faster.";
        }
        else if (btnName.equals("3D Anaglyph")) {
            array9 = new String[] { "3D mode used with red-cyan 3D glasses." };
        }
        return array9;
    }
    
    private String getButtonName(final String displayString) {
        final int pos = displayString.indexOf(58);
        return (pos < 0) ? displayString : displayString.substring(0, pos);
    }
    
    private GuiButton getSelectedButton(final int i, final int j) {
        for (int k = 0; k < this.buttonList.size(); ++k) {
            final GuiButton btn = this.buttonList.get(k);
            final int btnWidth = GuiVideoSettings.getButtonWidth(btn);
            final int btnHeight = GuiVideoSettings.getButtonHeight(btn);
            final boolean flag = i >= btn.xPosition && j >= btn.yPosition && i < btn.xPosition + btnWidth && j < btn.yPosition + btnHeight;
            if (flag) {
                return btn;
            }
        }
        return null;
    }
    
    static {
        GuiOtherSettingsOF.enumOptions = new GameSettings.Options[] { GameSettings.Options.LAGOMETER, GameSettings.Options.PROFILER, GameSettings.Options.WEATHER, GameSettings.Options.TIME, GameSettings.Options.USE_FULLSCREEN, GameSettings.Options.FULLSCREEN_MODE, GameSettings.Options.AUTOSAVE_TICKS };
    }
}
