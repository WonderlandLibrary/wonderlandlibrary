// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiScreen;

public class GuiDetailSettingsOF extends GuiScreen
{
    private GuiScreen prevScreen;
    protected String title;
    private GameSettings settings;
    private static GameSettings.Options[] enumOptions;
    private int lastMouseX;
    private int lastMouseY;
    private long mouseStillTime;
    
    public GuiDetailSettingsOF(final GuiScreen guiscreen, final GameSettings gamesettings) {
        this.title = "Detail Settings";
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.mouseStillTime = 0L;
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }
    
    @Override
    public void initGui() {
        int i = 0;
        for (final GameSettings.Options enumoptions : GuiDetailSettingsOF.enumOptions) {
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
            if (guibutton.id != GameSettings.Options.CLOUD_HEIGHT.ordinal()) {
                final ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                final int i = scaledresolution.getScaledWidth();
                final int j = scaledresolution.getScaledHeight();
                this.setWorldAndResolution(this.mc, i, j);
            }
        }
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
        String[] array2;
        if (btnName.equals("Clouds")) {
            final String[] array = array2 = new String[7];
            array[0] = "Clouds";
            array[1] = "  Default - as set by setting Graphics";
            array[2] = "  Fast - lower quality, faster";
            array[3] = "  Fancy - higher quality, slower";
            array[4] = "  OFF - no clouds, fastest";
            array[5] = "Fast clouds are rendered 2D.";
            array[6] = "Fancy clouds are rendered 3D.";
        }
        else if (btnName.equals("Cloud Height")) {
            final String[] array3 = array2 = new String[3];
            array3[0] = "Cloud Height";
            array3[1] = "  OFF - default height";
            array3[2] = "  100% - above world height limit";
        }
        else if (btnName.equals("Trees")) {
            final String[] array4 = array2 = new String[6];
            array4[0] = "Trees";
            array4[1] = "  Default - as set by setting Graphics";
            array4[2] = "  Fast - lower quality, faster";
            array4[3] = "  Fancy - higher quality, slower";
            array4[4] = "Fast trees have opaque leaves.";
            array4[5] = "Fancy trees have transparent leaves.";
        }
        else if (btnName.equals("Grass")) {
            final String[] array5 = array2 = new String[6];
            array5[0] = "Grass";
            array5[1] = "  Default - as set by setting Graphics";
            array5[2] = "  Fast - lower quality, faster";
            array5[3] = "  Fancy - higher quality, slower";
            array5[4] = "Fast grass uses default side texture.";
            array5[5] = "Fancy grass uses biome side texture.";
        }
        else if (btnName.equals("Dropped Items")) {
            final String[] array6 = array2 = new String[4];
            array6[0] = "Dropped Items";
            array6[1] = "  Default - as set by setting Graphics";
            array6[2] = "  Fast - 2D dropped items, faster";
            array6[3] = "  Fancy - 3D dropped items, slower";
        }
        else if (btnName.equals("Water")) {
            final String[] array7 = array2 = new String[6];
            array7[0] = "Water";
            array7[1] = "  Default - as set by setting Graphics";
            array7[2] = "  Fast  - lower quality, faster";
            array7[3] = "  Fancy - higher quality, slower";
            array7[4] = "Fast water (1 pass) has some visual artifacts";
            array7[5] = "Fancy water (2 pass) has no visual artifacts";
        }
        else if (btnName.equals("Rain & Snow")) {
            final String[] array8 = array2 = new String[7];
            array8[0] = "Rain & Snow";
            array8[1] = "  Default - as set by setting Graphics";
            array8[2] = "  Fast  - light rain/snow, faster";
            array8[3] = "  Fancy - heavy rain/snow, slower";
            array8[4] = "  OFF - no rain/snow, fastest";
            array8[5] = "When rain is OFF the splashes and rain sounds";
            array8[6] = "are still active.";
        }
        else if (btnName.equals("Sky")) {
            final String[] array9 = array2 = new String[4];
            array9[0] = "Sky";
            array9[1] = "  ON - sky is visible, slower";
            array9[2] = "  OFF  - sky is not visible, faster";
            array9[3] = "When sky is OFF the moon and sun are still visible.";
        }
        else if (btnName.equals("Sun & Moon")) {
            final String[] array10 = array2 = new String[3];
            array10[0] = "Sun & Moon";
            array10[1] = "  ON - sun and moon are visible (default)";
            array10[2] = "  OFF  - sun and moon are not visible (faster)";
        }
        else if (btnName.equals("Stars")) {
            final String[] array11 = array2 = new String[3];
            array11[0] = "Stars";
            array11[1] = "  ON - stars are visible, slower";
            array11[2] = "  OFF  - stars are not visible, faster";
        }
        else if (btnName.equals("Depth Fog")) {
            final String[] array12 = array2 = new String[3];
            array12[0] = "Depth Fog";
            array12[1] = "  ON - fog moves closer at bedrock levels (default)";
            array12[2] = "  OFF - same fog at all levels";
        }
        else if (btnName.equals("Show Capes")) {
            final String[] array13 = array2 = new String[3];
            array13[0] = "Show Capes";
            array13[1] = "  ON - show player capes (default)";
            array13[2] = "  OFF - do not show player capes";
        }
        else if (btnName.equals("Held Item Tooltips")) {
            final String[] array14 = array2 = new String[3];
            array14[0] = "Held item tooltips";
            array14[1] = "  ON - show tooltips for held items (default)";
            array14[2] = "  OFF - do not show tooltips for held items";
        }
        else if (btnName.equals("Translucent Blocks")) {
            final String[] array15 = array2 = new String[6];
            array15[0] = "Translucent Blocks";
            array15[1] = "  Fancy - correct color blending (default)";
            array15[2] = "  Fast - fast color blending (faster)";
            array15[3] = "Controls the color blending of translucent blocks";
            array15[4] = "with different color (stained glass, water, ice)";
            array15[5] = "when placed behind each other with air between them.";
        }
        else {
            array2 = null;
        }
        return array2;
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
        GuiDetailSettingsOF.enumOptions = new GameSettings.Options[] { GameSettings.Options.CLOUDS, GameSettings.Options.CLOUD_HEIGHT, GameSettings.Options.TREES, GameSettings.Options.RAIN, GameSettings.Options.SKY, GameSettings.Options.STARS, GameSettings.Options.SUN_MOON, GameSettings.Options.SHOW_CAPES, GameSettings.Options.TRANSLUCENT_BLOCKS, GameSettings.Options.HELD_ITEM_TOOLTIPS, GameSettings.Options.DROPPED_ITEMS };
    }
}
