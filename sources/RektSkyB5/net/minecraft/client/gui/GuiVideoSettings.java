/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optfine.GuiAnimationSettingsOF;
import optfine.GuiDetailSettingsOF;
import optfine.GuiOtherSettingsOF;
import optfine.GuiPerformanceSettingsOF;
import optfine.GuiQualitySettingsOF;

public class GuiVideoSettings
extends GuiScreen {
    private GuiScreen parentGuiScreen;
    protected String screenTitle = "Video Settings";
    private GameSettings guiGameSettings;
    private boolean is64bit;
    private static GameSettings.Options[] videoOptions = new GameSettings.Options[]{GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.FOG_FANCY, GameSettings.Options.FOG_START, GameSettings.Options.ANAGLYPH};
    private static final String __OBFID = "CL_00000718";
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long mouseStillTime = 0L;

    public GuiVideoSettings(GuiScreen parentScreenIn, GameSettings gameSettingsIn) {
        this.parentGuiScreen = parentScreenIn;
        this.guiGameSettings = gameSettingsIn;
    }

    @Override
    public void initGui() {
        String[] astring;
        this.screenTitle = I18n.format("options.videoTitle", new Object[0]);
        this.buttonList.clear();
        this.is64bit = false;
        for (String s2 : astring = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"}) {
            String s1 = System.getProperty(s2);
            if (s1 == null || !s1.contains("64")) continue;
            this.is64bit = true;
            break;
        }
        boolean l2 = false;
        boolean flag = !this.is64bit;
        GameSettings.Options[] agamesettings$options = videoOptions;
        int i1 = agamesettings$options.length;
        int i2 = 0;
        for (i2 = 0; i2 < i1; ++i2) {
            GameSettings.Options gamesettings$options = agamesettings$options[i2];
            if (gamesettings$options == null) continue;
            int j2 = this.width / 2 - 155 + i2 % 2 * 160;
            int k2 = this.height / 6 + 21 * (i2 / 2) - 10;
            if (gamesettings$options.getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), j2, k2, gamesettings$options));
                continue;
            }
            this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), j2, k2, gamesettings$options, this.guiGameSettings.getKeyBinding(gamesettings$options)));
        }
        int j1 = this.height / 6 + 21 * (i2 / 2) - 10;
        int k1 = 0;
        k1 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(202, k1, j1, "Quality..."));
        k1 = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(201, k1, j1 += 21, "Details..."));
        k1 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(212, k1, j1, "Performance..."));
        k1 = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(211, k1, j1 += 21, "Animations..."));
        k1 = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(222, k1, j1, "Other..."));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            int i2 = this.guiGameSettings.guiScale;
            if (button.id < 200 && button instanceof GuiOptionButton) {
                this.guiGameSettings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
                button.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }
            if (this.guiGameSettings.guiScale != i2) {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                int j2 = scaledresolution.getScaledWidth();
                int k2 = scaledresolution.getScaledHeight();
                this.setWorldAndResolution(this.mc, j2, k2);
            }
            if (button.id == 201) {
                this.mc.gameSettings.saveOptions();
                GuiDetailSettingsOF guidetailsettingsof = new GuiDetailSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(guidetailsettingsof);
            }
            if (button.id == 202) {
                this.mc.gameSettings.saveOptions();
                GuiQualitySettingsOF guiqualitysettingsof = new GuiQualitySettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(guiqualitysettingsof);
            }
            if (button.id == 211) {
                this.mc.gameSettings.saveOptions();
                GuiAnimationSettingsOF guianimationsettingsof = new GuiAnimationSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(guianimationsettingsof);
            }
            if (button.id == 212) {
                this.mc.gameSettings.saveOptions();
                GuiPerformanceSettingsOF guiperformancesettingsof = new GuiPerformanceSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(guiperformancesettingsof);
            }
            if (button.id == 222) {
                this.mc.gameSettings.saveOptions();
                GuiOtherSettingsOF guiothersettingsof = new GuiOtherSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(guiothersettingsof);
            }
            if (button.id == GameSettings.Options.AO_LEVEL.ordinal()) {
                return;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, this.is64bit ? 20 : 5, 0xFFFFFF);
        if (this.is64bit || this.guiGameSettings.renderDistanceChunks > 8) {
            // empty if block
        }
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
        if (p_getTooltipLines_1_.equals("Graphics")) {
            String[] stringArray2 = new String[5];
            stringArray2[0] = "Visual quality";
            stringArray2[1] = "  Fast  - lower quality, faster";
            stringArray2[2] = "  Fancy - higher quality, slower";
            stringArray2[3] = "Changes the appearance of clouds, leaves, water,";
            stringArray = stringArray2;
            stringArray2[4] = "shadows and grass sides.";
        } else if (p_getTooltipLines_1_.equals("Render Distance")) {
            String[] stringArray3 = new String[8];
            stringArray3[0] = "Visible distance";
            stringArray3[1] = "  2 Tiny - 32m (fastest)";
            stringArray3[2] = "  4 Short - 64m (faster)";
            stringArray3[3] = "  8 Normal - 128m";
            stringArray3[4] = "  16 Far - 256m (slower)";
            stringArray3[5] = "  32 Extreme - 512m (slowest!)";
            stringArray3[6] = "The Extreme view distance is very resource demanding!";
            stringArray = stringArray3;
            stringArray3[7] = "Values over 16 Far are only effective in local worlds.";
        } else if (p_getTooltipLines_1_.equals("Smooth Lighting")) {
            String[] stringArray4 = new String[4];
            stringArray4[0] = "Smooth lighting";
            stringArray4[1] = "  OFF - no smooth lighting (faster)";
            stringArray4[2] = "  Minimum - simple smooth lighting (slower)";
            stringArray = stringArray4;
            stringArray4[3] = "  Maximum - complex smooth lighting (slowest)";
        } else if (p_getTooltipLines_1_.equals("Smooth Lighting Level")) {
            String[] stringArray5 = new String[4];
            stringArray5[0] = "Smooth lighting level";
            stringArray5[1] = "  OFF - no shadows";
            stringArray5[2] = "  50% - light shadows";
            stringArray = stringArray5;
            stringArray5[3] = "  100% - dark shadows";
        } else if (p_getTooltipLines_1_.equals("Max Framerate")) {
            String[] stringArray6 = new String[6];
            stringArray6[0] = "Max framerate";
            stringArray6[1] = "  VSync - limit to monitor framerate (60, 30, 20)";
            stringArray6[2] = "  5-255 - variable";
            stringArray6[3] = "  Unlimited - no limit (fastest)";
            stringArray6[4] = "The framerate limit decreases the FPS even if";
            stringArray = stringArray6;
            stringArray6[5] = "the limit value is not reached.";
        } else if (p_getTooltipLines_1_.equals("View Bobbing")) {
            String[] stringArray7 = new String[2];
            stringArray7[0] = "More realistic movement.";
            stringArray = stringArray7;
            stringArray7[1] = "When using mipmaps set it to OFF for best results.";
        } else if (p_getTooltipLines_1_.equals("GUI Scale")) {
            String[] stringArray8 = new String[2];
            stringArray8[0] = "GUI Scale";
            stringArray = stringArray8;
            stringArray8[1] = "Smaller GUI might be faster";
        } else if (p_getTooltipLines_1_.equals("Server Textures")) {
            String[] stringArray9 = new String[2];
            stringArray9[0] = "Server textures";
            stringArray = stringArray9;
            stringArray9[1] = "Use the resource pack recommended by the server";
        } else if (p_getTooltipLines_1_.equals("Advanced OpenGL")) {
            String[] stringArray10 = new String[6];
            stringArray10[0] = "Detect and render only visible geometry";
            stringArray10[1] = "  OFF - all geometry is rendered (slower)";
            stringArray10[2] = "  Fast - only visible geometry is rendered (fastest)";
            stringArray10[3] = "  Fancy - conservative, avoids visual artifacts (faster)";
            stringArray10[4] = "The option is available only if it is supported by the ";
            stringArray = stringArray10;
            stringArray10[5] = "graphic card.";
        } else if (p_getTooltipLines_1_.equals("Fog")) {
            String[] stringArray11 = new String[6];
            stringArray11[0] = "Fog type";
            stringArray11[1] = "  Fast - faster fog";
            stringArray11[2] = "  Fancy - slower fog, looks better";
            stringArray11[3] = "  OFF - no fog, fastest";
            stringArray11[4] = "The fancy fog is available only if it is supported by the ";
            stringArray = stringArray11;
            stringArray11[5] = "graphic card.";
        } else if (p_getTooltipLines_1_.equals("Fog Start")) {
            String[] stringArray12 = new String[4];
            stringArray12[0] = "Fog start";
            stringArray12[1] = "  0.2 - the fog starts near the player";
            stringArray12[2] = "  0.8 - the fog starts far from the player";
            stringArray = stringArray12;
            stringArray12[3] = "This option usually does not affect the performance.";
        } else if (p_getTooltipLines_1_.equals("Brightness")) {
            String[] stringArray13 = new String[5];
            stringArray13[0] = "Increases the brightness of darker objects";
            stringArray13[1] = "  OFF - standard brightness";
            stringArray13[2] = "  100% - maximum brightness for darker objects";
            stringArray13[3] = "This options does not change the brightness of ";
            stringArray = stringArray13;
            stringArray13[4] = "fully black objects";
        } else if (p_getTooltipLines_1_.equals("Chunk Loading")) {
            String[] stringArray14 = new String[8];
            stringArray14[0] = "Chunk Loading";
            stringArray14[1] = "  Default - unstable FPS when loading chunks";
            stringArray14[2] = "  Smooth - stable FPS";
            stringArray14[3] = "  Multi-Core - stable FPS, 3x faster world loading";
            stringArray14[4] = "Smooth and Multi-Core remove the stuttering and ";
            stringArray14[5] = "freezes caused by chunk loading.";
            stringArray14[6] = "Multi-Core can speed up 3x the world loading and";
            stringArray = stringArray14;
            stringArray14[7] = "increase FPS by using a second CPU core.";
        } else if (p_getTooltipLines_1_.equals("Alternate Blocks")) {
            String[] stringArray15 = new String[3];
            stringArray15[0] = "Alternate Blocks";
            stringArray15[1] = "Uses alternative block models for some blocks.";
            stringArray = stringArray15;
            stringArray15[2] = "Depends on the selected resource pack.";
        } else if (p_getTooltipLines_1_.equals("Use VBOs")) {
            String[] stringArray16 = new String[3];
            stringArray16[0] = "Vertex Buffer Objects";
            stringArray16[1] = "Uses an alternative rendering model which is usually";
            stringArray = stringArray16;
            stringArray16[2] = "faster (5-10%) than the default rendering.";
        } else if (p_getTooltipLines_1_.equals("3D Anaglyph")) {
            String[] stringArray17 = new String[4];
            stringArray17[0] = "3D Anaglyph";
            stringArray17[1] = "Enables a stereoscopic 3D effect using different colors";
            stringArray17[2] = "for each eye.";
            stringArray = stringArray17;
            stringArray17[3] = "Requires red-cyan glasses for proper viewing.";
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
            boolean bl = flag = p_getSelectedButton_1_ >= guibutton.xPosition && p_getSelectedButton_2_ >= guibutton.yPosition && p_getSelectedButton_1_ < guibutton.xPosition + guibutton.width && p_getSelectedButton_2_ < guibutton.yPosition + guibutton.height;
            if (!flag) continue;
            return guibutton;
        }
        return null;
    }

    public static int getButtonWidth(GuiButton p_getButtonWidth_0_) {
        return p_getButtonWidth_0_.width;
    }

    public static int getButtonHeight(GuiButton p_getButtonHeight_0_) {
        return p_getButtonHeight_0_.height;
    }
}

