/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiQualitySettingsOF
extends GuiScreen {
    private GuiScreen prevScreen;
    protected String title = "Quality Settings";
    private GameSettings settings;
    private static GameSettings.Options[] enumOptions = new GameSettings.Options[]{GameSettings.Options.MIPMAP_LEVELS, GameSettings.Options.MIPMAP_TYPE, GameSettings.Options.AF_LEVEL, GameSettings.Options.AA_LEVEL, GameSettings.Options.CLEAR_WATER, GameSettings.Options.RANDOM_MOBS, GameSettings.Options.BETTER_GRASS, GameSettings.Options.BETTER_SNOW, GameSettings.Options.CUSTOM_FONTS, GameSettings.Options.CUSTOM_COLORS, GameSettings.Options.SWAMP_COLORS, GameSettings.Options.SMOOTH_BIOMES, GameSettings.Options.CONNECTED_TEXTURES, GameSettings.Options.NATURAL_TEXTURES, GameSettings.Options.CUSTOM_SKY};
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long mouseStillTime = 0L;

    public GuiQualitySettingsOF(GuiScreen p_i38_1_, GameSettings p_i38_2_) {
        this.prevScreen = p_i38_1_;
        this.settings = p_i38_2_;
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
            if (button.id != GameSettings.Options.CLOUD_HEIGHT.ordinal()) {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                int i2 = scaledresolution.getScaledWidth();
                int j2 = scaledresolution.getScaledHeight();
                this.setWorldAndResolution(this.mc, i2, j2);
            }
        }
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
                        int k1 = 0xDDDDDD;
                        if (s1.endsWith("!")) {
                            k1 = 0xFF2020;
                        }
                        this.fontRendererObj.drawStringWithShadow(s1, j2 + 5, k2 + 5 + j1 * 11, k1);
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
        if (p_getTooltipLines_1_.equals("Mipmap Levels")) {
            String[] stringArray2 = new String[6];
            stringArray2[0] = "Visual effect which makes distant objects look better";
            stringArray2[1] = "by smoothing the texture details";
            stringArray2[2] = "  OFF - no smoothing";
            stringArray2[3] = "  1 - minimum smoothing";
            stringArray2[4] = "  4 - maximum smoothing";
            stringArray = stringArray2;
            stringArray2[5] = "This option usually does not affect the performance.";
        } else if (p_getTooltipLines_1_.equals("Mipmap Type")) {
            String[] stringArray3 = new String[6];
            stringArray3[0] = "Visual effect which makes distant objects look better";
            stringArray3[1] = "by smoothing the texture details";
            stringArray3[2] = "  Nearest - rough smoothing (fastest)";
            stringArray3[3] = "  Linear - normal smoothing";
            stringArray3[4] = "  Bilinear - fine smoothing";
            stringArray = stringArray3;
            stringArray3[5] = "  Trilinear - finest smoothing (slowest)";
        } else if (p_getTooltipLines_1_.equals("Anisotropic Filtering")) {
            String[] stringArray4 = new String[6];
            stringArray4[0] = "Anisotropic Filtering";
            stringArray4[1] = " OFF - (default) standard texture detail (faster)";
            stringArray4[2] = " 2-16 - finer details in mipmapped textures (slower)";
            stringArray4[3] = "The Anisotropic Filtering restores details in";
            stringArray4[4] = "mipmapped textures.";
            stringArray = stringArray4;
            stringArray4[5] = "When enabled it may substantially decrease the FPS.";
        } else if (p_getTooltipLines_1_.equals("Antialiasing")) {
            String[] stringArray5 = new String[8];
            stringArray5[0] = "Antialiasing";
            stringArray5[1] = " OFF - (default) no antialiasing (faster)";
            stringArray5[2] = " 2-16 - antialiased lines and edges (slower)";
            stringArray5[3] = "The Antialiasing smooths jagged lines and ";
            stringArray5[4] = "sharp color transitions.";
            stringArray5[5] = "When enabled it may substantially decrease the FPS.";
            stringArray5[6] = "Not all levels are supported by all graphics cards.";
            stringArray = stringArray5;
            stringArray5[7] = "Effective after a RESTART!";
        } else if (p_getTooltipLines_1_.equals("Clear Water")) {
            String[] stringArray6 = new String[3];
            stringArray6[0] = "Clear Water";
            stringArray6[1] = "  ON - clear, transparent water";
            stringArray = stringArray6;
            stringArray6[2] = "  OFF - default water";
        } else if (p_getTooltipLines_1_.equals("Better Grass")) {
            String[] stringArray7 = new String[4];
            stringArray7[0] = "Better Grass";
            stringArray7[1] = "  OFF - default side grass texture, fastest";
            stringArray7[2] = "  Fast - full side grass texture, slower";
            stringArray = stringArray7;
            stringArray7[3] = "  Fancy - dynamic side grass texture, slowest";
        } else if (p_getTooltipLines_1_.equals("Better Snow")) {
            String[] stringArray8 = new String[5];
            stringArray8[0] = "Better Snow";
            stringArray8[1] = "  OFF - default snow, faster";
            stringArray8[2] = "  ON - better snow, slower";
            stringArray8[3] = "Shows snow under transparent blocks (fence, tall grass)";
            stringArray = stringArray8;
            stringArray8[4] = "when bordering with snow blocks";
        } else if (p_getTooltipLines_1_.equals("Random Mobs")) {
            String[] stringArray9 = new String[5];
            stringArray9[0] = "Random Mobs";
            stringArray9[1] = "  OFF - no random mobs, faster";
            stringArray9[2] = "  ON - random mobs, slower";
            stringArray9[3] = "Random mobs uses random textures for the game creatures.";
            stringArray = stringArray9;
            stringArray9[4] = "It needs a texture pack which has multiple mob textures.";
        } else if (p_getTooltipLines_1_.equals("Swamp Colors")) {
            String[] stringArray10 = new String[4];
            stringArray10[0] = "Swamp Colors";
            stringArray10[1] = "  ON - use swamp colors (default), slower";
            stringArray10[2] = "  OFF - do not use swamp colors, faster";
            stringArray = stringArray10;
            stringArray10[3] = "The swamp colors affect grass, leaves, vines and water.";
        } else if (p_getTooltipLines_1_.equals("Smooth Biomes")) {
            String[] stringArray11 = new String[6];
            stringArray11[0] = "Smooth Biomes";
            stringArray11[1] = "  ON - smoothing of biome borders (default), slower";
            stringArray11[2] = "  OFF - no smoothing of biome borders, faster";
            stringArray11[3] = "The smoothing of biome borders is done by sampling and";
            stringArray11[4] = "averaging the color of all surrounding blocks.";
            stringArray = stringArray11;
            stringArray11[5] = "Affected are grass, leaves, vines and water.";
        } else if (p_getTooltipLines_1_.equals("Custom Fonts")) {
            String[] stringArray12 = new String[5];
            stringArray12[0] = "Custom Fonts";
            stringArray12[1] = "  ON - uses custom fonts (default), slower";
            stringArray12[2] = "  OFF - uses default font, faster";
            stringArray12[3] = "The custom fonts are supplied by the current";
            stringArray = stringArray12;
            stringArray12[4] = "texture pack";
        } else if (p_getTooltipLines_1_.equals("Custom Colors")) {
            String[] stringArray13 = new String[5];
            stringArray13[0] = "Custom Colors";
            stringArray13[1] = "  ON - uses custom colors (default), slower";
            stringArray13[2] = "  OFF - uses default colors, faster";
            stringArray13[3] = "The custom colors are supplied by the current";
            stringArray = stringArray13;
            stringArray13[4] = "texture pack";
        } else if (p_getTooltipLines_1_.equals("Show Capes")) {
            String[] stringArray14 = new String[3];
            stringArray14[0] = "Show Capes";
            stringArray14[1] = "  ON - show player capes (default)";
            stringArray = stringArray14;
            stringArray14[2] = "  OFF - do not show player capes";
        } else if (p_getTooltipLines_1_.equals("Connected Textures")) {
            String[] stringArray15 = new String[8];
            stringArray15[0] = "Connected Textures";
            stringArray15[1] = "  OFF - no connected textures (default)";
            stringArray15[2] = "  Fast - fast connected textures";
            stringArray15[3] = "  Fancy - fancy connected textures";
            stringArray15[4] = "Connected textures joins the textures of glass,";
            stringArray15[5] = "sandstone and bookshelves when placed next to";
            stringArray15[6] = "each other. The connected textures are supplied";
            stringArray = stringArray15;
            stringArray15[7] = "by the current texture pack.";
        } else if (p_getTooltipLines_1_.equals("Far View")) {
            String[] stringArray16 = new String[7];
            stringArray16[0] = "Far View";
            stringArray16[1] = " OFF - (default) standard view distance";
            stringArray16[2] = " ON - 3x view distance";
            stringArray16[3] = "Far View is very resource demanding!";
            stringArray16[4] = "3x view distance => 9x chunks to be loaded => FPS / 9";
            stringArray16[5] = "Standard view distances: 32, 64, 128, 256";
            stringArray = stringArray16;
            stringArray16[6] = "Far view distances: 96, 192, 384, 512";
        } else if (p_getTooltipLines_1_.equals("Natural Textures")) {
            String[] stringArray17 = new String[8];
            stringArray17[0] = "Natural Textures";
            stringArray17[1] = "  OFF - no natural textures (default)";
            stringArray17[2] = "  ON - use natural textures";
            stringArray17[3] = "Natural textures remove the gridlike pattern";
            stringArray17[4] = "created by repeating blocks of the same type.";
            stringArray17[5] = "It uses rotated and flipped variants of the base";
            stringArray17[6] = "block texture. The configuration for the natural";
            stringArray = stringArray17;
            stringArray17[7] = "textures is supplied by the current texture pack";
        } else if (p_getTooltipLines_1_.equals("Custom Sky")) {
            String[] stringArray18 = new String[5];
            stringArray18[0] = "Custom Sky";
            stringArray18[1] = "  ON - custom sky textures (default), slow";
            stringArray18[2] = "  OFF - default sky, faster";
            stringArray18[3] = "The custom sky textures are supplied by the current";
            stringArray = stringArray18;
            stringArray18[4] = "texture pack";
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

