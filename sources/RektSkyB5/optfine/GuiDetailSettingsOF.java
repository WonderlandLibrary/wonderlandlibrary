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

public class GuiDetailSettingsOF
extends GuiScreen {
    private GuiScreen prevScreen;
    protected String title = "Detail Settings";
    private GameSettings settings;
    private static GameSettings.Options[] enumOptions = new GameSettings.Options[]{GameSettings.Options.CLOUDS, GameSettings.Options.CLOUD_HEIGHT, GameSettings.Options.TREES, GameSettings.Options.RAIN, GameSettings.Options.SKY, GameSettings.Options.STARS, GameSettings.Options.SUN_MOON, GameSettings.Options.SHOW_CAPES, GameSettings.Options.TRANSLUCENT_BLOCKS, GameSettings.Options.HELD_ITEM_TOOLTIPS, GameSettings.Options.DROPPED_ITEMS, GameSettings.Options.ENTITY_SHADOWS, GameSettings.Options.VIGNETTE};
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long mouseStillTime = 0L;

    public GuiDetailSettingsOF(GuiScreen p_i35_1_, GameSettings p_i35_2_) {
        this.prevScreen = p_i35_1_;
        this.settings = p_i35_2_;
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
        if (p_getTooltipLines_1_.equals("Clouds")) {
            String[] stringArray2 = new String[7];
            stringArray2[0] = "Clouds";
            stringArray2[1] = "  Default - as set by setting Graphics";
            stringArray2[2] = "  Fast - lower quality, faster";
            stringArray2[3] = "  Fancy - higher quality, slower";
            stringArray2[4] = "  OFF - no clouds, fastest";
            stringArray2[5] = "Fast clouds are rendered 2D.";
            stringArray = stringArray2;
            stringArray2[6] = "Fancy clouds are rendered 3D.";
        } else if (p_getTooltipLines_1_.equals("Cloud Height")) {
            String[] stringArray3 = new String[3];
            stringArray3[0] = "Cloud Height";
            stringArray3[1] = "  OFF - default height";
            stringArray = stringArray3;
            stringArray3[2] = "  100% - above world height limit";
        } else if (p_getTooltipLines_1_.equals("Trees")) {
            String[] stringArray4 = new String[6];
            stringArray4[0] = "Trees";
            stringArray4[1] = "  Default - as set by setting Graphics";
            stringArray4[2] = "  Fast - lower quality, faster";
            stringArray4[3] = "  Fancy - higher quality, slower";
            stringArray4[4] = "Fast trees have opaque leaves.";
            stringArray = stringArray4;
            stringArray4[5] = "Fancy trees have transparent leaves.";
        } else if (p_getTooltipLines_1_.equals("Grass")) {
            String[] stringArray5 = new String[6];
            stringArray5[0] = "Grass";
            stringArray5[1] = "  Default - as set by setting Graphics";
            stringArray5[2] = "  Fast - lower quality, faster";
            stringArray5[3] = "  Fancy - higher quality, slower";
            stringArray5[4] = "Fast grass uses default side texture.";
            stringArray = stringArray5;
            stringArray5[5] = "Fancy grass uses biome side texture.";
        } else if (p_getTooltipLines_1_.equals("Dropped Items")) {
            String[] stringArray6 = new String[4];
            stringArray6[0] = "Dropped Items";
            stringArray6[1] = "  Default - as set by setting Graphics";
            stringArray6[2] = "  Fast - 2D dropped items, faster";
            stringArray = stringArray6;
            stringArray6[3] = "  Fancy - 3D dropped items, slower";
        } else if (p_getTooltipLines_1_.equals("Water")) {
            String[] stringArray7 = new String[6];
            stringArray7[0] = "Water";
            stringArray7[1] = "  Default - as set by setting Graphics";
            stringArray7[2] = "  Fast  - lower quality, faster";
            stringArray7[3] = "  Fancy - higher quality, slower";
            stringArray7[4] = "Fast water (1 pass) has some visual artifacts";
            stringArray = stringArray7;
            stringArray7[5] = "Fancy water (2 pass) has no visual artifacts";
        } else if (p_getTooltipLines_1_.equals("Rain & Snow")) {
            String[] stringArray8 = new String[7];
            stringArray8[0] = "Rain & Snow";
            stringArray8[1] = "  Default - as set by setting Graphics";
            stringArray8[2] = "  Fast  - light rain/snow, faster";
            stringArray8[3] = "  Fancy - heavy rain/snow, slower";
            stringArray8[4] = "  OFF - no rain/snow, fastest";
            stringArray8[5] = "When rain is OFF the splashes and rain sounds";
            stringArray = stringArray8;
            stringArray8[6] = "are still active.";
        } else if (p_getTooltipLines_1_.equals("Sky")) {
            String[] stringArray9 = new String[4];
            stringArray9[0] = "Sky";
            stringArray9[1] = "  ON - sky is visible, slower";
            stringArray9[2] = "  OFF  - sky is not visible, faster";
            stringArray = stringArray9;
            stringArray9[3] = "When sky is OFF the moon and sun are still visible.";
        } else if (p_getTooltipLines_1_.equals("Sun & Moon")) {
            String[] stringArray10 = new String[3];
            stringArray10[0] = "Sun & Moon";
            stringArray10[1] = "  ON - sun and moon are visible (default)";
            stringArray = stringArray10;
            stringArray10[2] = "  OFF  - sun and moon are not visible (faster)";
        } else if (p_getTooltipLines_1_.equals("Stars")) {
            String[] stringArray11 = new String[3];
            stringArray11[0] = "Stars";
            stringArray11[1] = "  ON - stars are visible, slower";
            stringArray = stringArray11;
            stringArray11[2] = "  OFF  - stars are not visible, faster";
        } else if (p_getTooltipLines_1_.equals("Depth Fog")) {
            String[] stringArray12 = new String[3];
            stringArray12[0] = "Depth Fog";
            stringArray12[1] = "  ON - fog moves closer at bedrock levels (default)";
            stringArray = stringArray12;
            stringArray12[2] = "  OFF - same fog at all levels";
        } else if (p_getTooltipLines_1_.equals("Show Capes")) {
            String[] stringArray13 = new String[3];
            stringArray13[0] = "Show Capes";
            stringArray13[1] = "  ON - show player capes (default)";
            stringArray = stringArray13;
            stringArray13[2] = "  OFF - do not show player capes";
        } else if (p_getTooltipLines_1_.equals("Held Item Tooltips")) {
            String[] stringArray14 = new String[3];
            stringArray14[0] = "Held item tooltips";
            stringArray14[1] = "  ON - show tooltips for held items (default)";
            stringArray = stringArray14;
            stringArray14[2] = "  OFF - do not show tooltips for held items";
        } else if (p_getTooltipLines_1_.equals("Translucent Blocks")) {
            String[] stringArray15 = new String[6];
            stringArray15[0] = "Translucent Blocks";
            stringArray15[1] = "  Fancy - correct color blending (default)";
            stringArray15[2] = "  Fast - fast color blending (faster)";
            stringArray15[3] = "Controls the color blending of translucent blocks";
            stringArray15[4] = "with different color (stained glass, water, ice)";
            stringArray = stringArray15;
            stringArray15[5] = "when placed behind each other with air between them.";
        } else if (p_getTooltipLines_1_.equals("Vignette")) {
            String[] stringArray16 = new String[8];
            stringArray16[0] = "Visual effect which slightly darkens the screen corners";
            stringArray16[1] = "  Default - as set by the setting Graphics (default)";
            stringArray16[2] = "  Fast - vignette disabled (faster)";
            stringArray16[3] = "  Fancy - vignette enabled (slower)";
            stringArray16[4] = "The vignette may have a significant effect on the FPS,";
            stringArray16[5] = "especially when playing fullscreen.";
            stringArray16[6] = "The vignette effect is very subtle and can safely";
            stringArray = stringArray16;
            stringArray16[7] = "be disabled";
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

