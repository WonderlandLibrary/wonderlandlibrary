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

public class GuiQualitySettingsOF extends GuiScreen
{
    private GuiScreen prevScreen;
    protected String title;
    private GameSettings settings;
    private static GameSettings.Options[] enumOptions;
    private int lastMouseX;
    private int lastMouseY;
    private long mouseStillTime;
    
    public GuiQualitySettingsOF(final GuiScreen guiscreen, final GameSettings gamesettings) {
        this.title = "Quality Settings";
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.mouseStillTime = 0L;
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }
    
    @Override
    public void initGui() {
        int i = 0;
        for (final GameSettings.Options enumoptions : GuiQualitySettingsOF.enumOptions) {
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
                        int col = 14540253;
                        if (line.endsWith("!")) {
                            col = 16719904;
                        }
                        this.fontRendererObj.drawStringWithShadow(line, x2 + 5, y2 + 5 + i * 11, col);
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
        if (btnName.equals("Mipmap Levels")) {
            final String[] array = array2 = new String[6];
            array[0] = "Visual effect which makes distant objects look better";
            array[1] = "by smoothing the texture details";
            array[2] = "  OFF - no smoothing";
            array[3] = "  1 - minimum smoothing";
            array[4] = "  4 - maximum smoothing";
            array[5] = "This option usually does not affect the performance.";
        }
        else if (btnName.equals("Mipmap Type")) {
            final String[] array3 = array2 = new String[5];
            array3[0] = "Visual effect which makes distant objects look better";
            array3[1] = "by smoothing the texture details";
            array3[2] = "  Nearest - rough smoothing";
            array3[3] = "  Linear - fine smoothing";
            array3[4] = "This option usually does not affect the performance.";
        }
        else if (btnName.equals("Anisotropic Filtering")) {
            final String[] array4 = array2 = new String[5];
            array4[0] = "Anisotropic Filtering";
            array4[1] = " OFF - (default) standard texture detail (faster)";
            array4[2] = " 2-16 - finer details in mipmapped textures (slower)";
            array4[3] = "The Anisotropic Filtering restores details in mipmapped";
            array4[4] = "textures. Higher values may decrease the FPS.";
        }
        else if (btnName.equals("Antialiasing")) {
            final String[] array5 = array2 = new String[8];
            array5[0] = "Antialiasing";
            array5[1] = " OFF - (default) no antialiasing (faster)";
            array5[2] = " 2-16 - antialiased lines and edges (slower)";
            array5[3] = "The Antialiasing smooths jagged lines and ";
            array5[4] = "sharp color transitions.";
            array5[5] = "Higher values may substantially decrease the FPS.";
            array5[6] = "Not all levels are supported by all graphics cards.";
            array5[7] = "Effective after a RESTART!";
        }
        else if (btnName.equals("Clear Water")) {
            final String[] array6 = array2 = new String[3];
            array6[0] = "Clear Water";
            array6[1] = "  ON - clear, transparent water";
            array6[2] = "  OFF - default water";
        }
        else if (btnName.equals("Better Grass")) {
            final String[] array7 = array2 = new String[4];
            array7[0] = "Better Grass";
            array7[1] = "  OFF - default side grass texture, fastest";
            array7[2] = "  Fast - full side grass texture, slower";
            array7[3] = "  Fancy - dynamic side grass texture, slowest";
        }
        else if (btnName.equals("Better Snow")) {
            final String[] array8 = array2 = new String[5];
            array8[0] = "Better Snow";
            array8[1] = "  OFF - default snow, faster";
            array8[2] = "  ON - better snow, slower";
            array8[3] = "Shows snow under transparent blocks (fence, tall grass)";
            array8[4] = "when bordering with snow blocks";
        }
        else if (btnName.equals("Random Mobs")) {
            final String[] array9 = array2 = new String[5];
            array9[0] = "Random Mobs";
            array9[1] = "  OFF - no random mobs, faster";
            array9[2] = "  ON - random mobs, slower";
            array9[3] = "Random mobs uses random textures for the game creatures.";
            array9[4] = "It needs a texture pack which has multiple mob textures.";
        }
        else if (btnName.equals("Swamp Colors")) {
            final String[] array10 = array2 = new String[4];
            array10[0] = "Swamp Colors";
            array10[1] = "  ON - use swamp colors (default), slower";
            array10[2] = "  OFF - do not use swamp colors, faster";
            array10[3] = "The swamp colors affect grass, leaves, vines and water.";
        }
        else if (btnName.equals("Smooth Biomes")) {
            final String[] array11 = array2 = new String[6];
            array11[0] = "Smooth Biomes";
            array11[1] = "  ON - smoothing of biome borders (default), slower";
            array11[2] = "  OFF - no smoothing of biome borders, faster";
            array11[3] = "The smoothing of biome borders is done by sampling and";
            array11[4] = "averaging the color of all surrounding blocks.";
            array11[5] = "Affected are grass, leaves, vines and water.";
        }
        else if (btnName.equals("Custom Fonts")) {
            final String[] array12 = array2 = new String[5];
            array12[0] = "Custom Fonts";
            array12[1] = "  ON - uses custom fonts (default), slower";
            array12[2] = "  OFF - uses default font, faster";
            array12[3] = "The custom fonts are supplied by the current";
            array12[4] = "texture pack";
        }
        else if (btnName.equals("Custom Colors")) {
            final String[] array13 = array2 = new String[5];
            array13[0] = "Custom Colors";
            array13[1] = "  ON - uses custom colors (default), slower";
            array13[2] = "  OFF - uses default colors, faster";
            array13[3] = "The custom colors are supplied by the current";
            array13[4] = "texture pack";
        }
        else if (btnName.equals("Show Capes")) {
            final String[] array14 = array2 = new String[3];
            array14[0] = "Show Capes";
            array14[1] = "  ON - show player capes (default)";
            array14[2] = "  OFF - do not show player capes";
        }
        else if (btnName.equals("Connected Textures")) {
            final String[] array15 = array2 = new String[8];
            array15[0] = "Connected Textures";
            array15[1] = "  OFF - no connected textures (default)";
            array15[2] = "  Fast - fast connected textures";
            array15[3] = "  Fancy - fancy connected textures";
            array15[4] = "Connected textures joins the textures of glass,";
            array15[5] = "sandstone and bookshelves when placed next to";
            array15[6] = "each other. The connected textures are supplied";
            array15[7] = "by the current texture pack.";
        }
        else if (btnName.equals("Far View")) {
            final String[] array16 = array2 = new String[7];
            array16[0] = "Far View";
            array16[1] = " OFF - (default) standard view distance";
            array16[2] = " ON - 3x view distance";
            array16[3] = "Far View is very resource demanding!";
            array16[4] = "3x view distance => 9x chunks to be loaded => FPS / 9";
            array16[5] = "Standard view distances: 32, 64, 128, 256";
            array16[6] = "Far view distances: 96, 192, 384, 512";
        }
        else if (btnName.equals("Natural Textures")) {
            final String[] array17 = array2 = new String[8];
            array17[0] = "Natural Textures";
            array17[1] = "  OFF - no natural textures (default)";
            array17[2] = "  ON - use natural textures";
            array17[3] = "Natural textures remove the gridlike pattern";
            array17[4] = "created by repeating blocks of the same type.";
            array17[5] = "It uses rotated and flipped variants of the base";
            array17[6] = "block texture. The configuration for the natural";
            array17[7] = "textures is supplied by the current texture pack";
        }
        else if (btnName.equals("Custom Sky")) {
            final String[] array18 = array2 = new String[5];
            array18[0] = "Custom Sky";
            array18[1] = "  ON - custom sky textures (default), slow";
            array18[2] = "  OFF - default sky, faster";
            array18[3] = "The custom sky textures are supplied by the current";
            array18[4] = "texture pack";
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
        GuiQualitySettingsOF.enumOptions = new GameSettings.Options[] { GameSettings.Options.MIPMAP_LEVELS, GameSettings.Options.MIPMAP_TYPE, GameSettings.Options.AF_LEVEL, GameSettings.Options.AA_LEVEL, GameSettings.Options.CLEAR_WATER, GameSettings.Options.RANDOM_MOBS, GameSettings.Options.BETTER_GRASS, GameSettings.Options.BETTER_SNOW, GameSettings.Options.CUSTOM_FONTS, GameSettings.Options.CUSTOM_COLORS, GameSettings.Options.SWAMP_COLORS, GameSettings.Options.SMOOTH_BIOMES, GameSettings.Options.CONNECTED_TEXTURES, GameSettings.Options.CUSTOM_SKY };
    }
}
