// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.optifine.GuiOtherSettingsOF;
import net.minecraft.optifine.GuiPerformanceSettingsOF;
import net.minecraft.optifine.GuiAnimationSettingsOF;
import net.minecraft.optifine.GuiQualitySettingsOF;
import net.minecraft.optifine.GuiDetailSettingsOF;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiVideoSettings extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    protected String screenTitle;
    private GameSettings guiGameSettings;
    private boolean is64bit;
    private static GameSettings.Options[] videoOptions;
    private static final String __OBFID = "CL_00000718";
    private int lastMouseX;
    private int lastMouseY;
    private long mouseStillTime;
    
    public GuiVideoSettings(final GuiScreen par1GuiScreen, final GameSettings par2GameSettings) {
        this.screenTitle = "Video Settings";
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.mouseStillTime = 0L;
        this.parentGuiScreen = par1GuiScreen;
        this.guiGameSettings = par2GameSettings;
    }
    
    @Override
    public void initGui() {
        this.screenTitle = I18n.format("options.videoTitle", new Object[0]);
        this.buttonList.clear();
        this.is64bit = false;
        final String[] var2;
        final String[] var1 = var2 = new String[] { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
        for (int var3 = var1.length, var4 = 0; var4 < var3; ++var4) {
            final String var5 = var2[var4];
            final String var6 = System.getProperty(var5);
            if (var6 != null && var6.contains("64")) {
                this.is64bit = true;
                break;
            }
        }
        final boolean var7 = false;
        final boolean var8 = !this.is64bit;
        final GameSettings.Options[] var9 = GuiVideoSettings.videoOptions;
        final int var10 = var9.length;
        final boolean var11 = false;
        int var12;
        for (var12 = 0; var12 < var10; ++var12) {
            final GameSettings.Options y = var9[var12];
            final int x = this.width / 2 - 155 + var12 % 2 * 160;
            final int y2 = this.height / 6 + 21 * (var12 / 2) - 10;
            if (y.getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(y.returnEnumOrdinal(), x, y2, y));
            }
            else {
                this.buttonList.add(new GuiOptionButton(y.returnEnumOrdinal(), x, y2, y, this.guiGameSettings.getKeyBinding(y)));
            }
        }
        int var13 = this.height / 6 + 21 * (var12 / 2) - 10;
        final boolean var14 = false;
        int x = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(202, x, var13, "Quality..."));
        var13 += 21;
        x = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(201, x, var13, "Details..."));
        x = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(212, x, var13, "Performance..."));
        var13 += 21;
        x = this.width / 2 - 155 + 0;
        this.buttonList.add(new GuiOptionButton(211, x, var13, "Animations..."));
        x = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(222, x, var13, "Other..."));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton par1GuiButton) throws IOException {
        if (par1GuiButton.enabled) {
            final int var2 = this.guiGameSettings.guiScale;
            if (par1GuiButton.id < 200 && par1GuiButton instanceof GuiOptionButton) {
                this.guiGameSettings.setOptionValue(((GuiOptionButton)par1GuiButton).returnEnumOptions(), 1);
                par1GuiButton.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(par1GuiButton.id));
            }
            if (par1GuiButton.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }
            if (this.guiGameSettings.guiScale != var2) {
                final ScaledResolution scr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                final int var3 = scr.getScaledWidth();
                final int var4 = scr.getScaledHeight();
                this.setWorldAndResolution(this.mc, var3, var4);
            }
            if (par1GuiButton.id == 201) {
                this.mc.gameSettings.saveOptions();
                final GuiDetailSettingsOF scr2 = new GuiDetailSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr2);
            }
            if (par1GuiButton.id == 202) {
                this.mc.gameSettings.saveOptions();
                final GuiQualitySettingsOF scr3 = new GuiQualitySettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr3);
            }
            if (par1GuiButton.id == 211) {
                this.mc.gameSettings.saveOptions();
                final GuiAnimationSettingsOF scr4 = new GuiAnimationSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr4);
            }
            if (par1GuiButton.id == 212) {
                this.mc.gameSettings.saveOptions();
                final GuiPerformanceSettingsOF scr5 = new GuiPerformanceSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr5);
            }
            if (par1GuiButton.id == 222) {
                this.mc.gameSettings.saveOptions();
                final GuiOtherSettingsOF scr6 = new GuiOtherSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr6);
            }
            if (par1GuiButton.id == GameSettings.Options.AO_LEVEL.ordinal()) {
                return;
            }
        }
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float z) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, this.is64bit ? 20 : 5, 16777215);
        if (this.is64bit || this.guiGameSettings.renderDistanceChunks > 8) {}
        super.drawScreen(x, y, z);
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
        if (btnName.equals("Graphics")) {
            final String[] array = array2 = new String[5];
            array[0] = "Visual quality";
            array[1] = "  Fast  - lower quality, faster";
            array[2] = "  Fancy - higher quality, slower";
            array[3] = "Changes the appearance of clouds, leaves, water,";
            array[4] = "shadows and grass sides.";
        }
        else if (btnName.equals("Render Distance")) {
            final String[] array3 = array2 = new String[8];
            array3[0] = "Visible distance";
            array3[1] = "  2 Tiny - 32m (fastest)";
            array3[2] = "  4 Short - 64m (faster)";
            array3[3] = "  8 Normal - 128m";
            array3[4] = "  16 Far - 256m (slower)";
            array3[5] = "  32 Extreme - 512m (slowest!)";
            array3[6] = "The Extreme view distance is very resource demanding!";
            array3[7] = "Values over 16 Far are only effective in local worlds.";
        }
        else if (btnName.equals("Smooth Lighting")) {
            final String[] array4 = array2 = new String[4];
            array4[0] = "Smooth lighting";
            array4[1] = "  OFF - no smooth lighting (faster)";
            array4[2] = "  Minimum - simple smooth lighting (slower)";
            array4[3] = "  Maximum - complex smooth lighting (slowest)";
        }
        else if (btnName.equals("Smooth Lighting Level")) {
            final String[] array5 = array2 = new String[4];
            array5[0] = "Smooth lighting level";
            array5[1] = "  OFF - no smooth lighting (faster)";
            array5[2] = "  1% - light smooth lighting (slower)";
            array5[3] = "  100% - dark smooth lighting (slower)";
        }
        else if (btnName.equals("Max Framerate")) {
            final String[] array6 = array2 = new String[6];
            array6[0] = "Max framerate";
            array6[1] = "  VSync - limit to monitor framerate (60, 30, 20)";
            array6[2] = "  5-255 - variable";
            array6[3] = "  Unlimited - no limit (fastest)";
            array6[4] = "The framerate limit decreases the FPS even if";
            array6[5] = "the limit value is not reached.";
        }
        else if (btnName.equals("View Bobbing")) {
            final String[] array7 = array2 = new String[2];
            array7[0] = "More realistic movement.";
            array7[1] = "When using mipmaps set it to OFF for best results.";
        }
        else if (btnName.equals("GUI Scale")) {
            final String[] array8 = array2 = new String[2];
            array8[0] = "GUI Scale";
            array8[1] = "Smaller GUI might be faster";
        }
        else if (btnName.equals("Server Textures")) {
            final String[] array9 = array2 = new String[2];
            array9[0] = "Server textures";
            array9[1] = "Use the resource pack recommended by the server";
        }
        else if (btnName.equals("Advanced OpenGL")) {
            final String[] array10 = array2 = new String[6];
            array10[0] = "Detect and render only visible geometry";
            array10[1] = "  OFF - all geometry is rendered (slower)";
            array10[2] = "  Fast - only visible geometry is rendered (fastest)";
            array10[3] = "  Fancy - conservative, avoids visual artifacts (faster)";
            array10[4] = "The option is available only if it is supported by the ";
            array10[5] = "graphic card.";
        }
        else if (btnName.equals("Fog")) {
            final String[] array11 = array2 = new String[6];
            array11[0] = "Fog type";
            array11[1] = "  Fast - faster fog";
            array11[2] = "  Fancy - slower fog, looks better";
            array11[3] = "  OFF - no fog, fastest";
            array11[4] = "The fancy fog is available only if it is supported by the ";
            array11[5] = "graphic card.";
        }
        else if (btnName.equals("Fog Start")) {
            final String[] array12 = array2 = new String[4];
            array12[0] = "Fog start";
            array12[1] = "  0.2 - the fog starts near the player";
            array12[2] = "  0.8 - the fog starts far from the player";
            array12[3] = "This option usually does not affect the performance.";
        }
        else if (btnName.equals("Brightness")) {
            final String[] array13 = array2 = new String[5];
            array13[0] = "Increases the brightness of darker objects";
            array13[1] = "  OFF - standard brightness";
            array13[2] = "  100% - maximum brightness for darker objects";
            array13[3] = "This options does not change the brightness of ";
            array13[4] = "fully black objects";
        }
        else if (btnName.equals("Chunk Loading")) {
            final String[] array14 = array2 = new String[8];
            array14[0] = "Chunk Loading";
            array14[1] = "  Default - unstable FPS when loading chunks";
            array14[2] = "  Smooth - stable FPS";
            array14[3] = "  Multi-Core - stable FPS, 3x faster world loading";
            array14[4] = "Smooth and Multi-Core remove the stuttering and ";
            array14[5] = "freezes caused by chunk loading.";
            array14[6] = "Multi-Core can speed up 3x the world loading and";
            array14[7] = "increase FPS by using a second CPU core.";
        }
        else if (btnName.equals("Alternate Blocks")) {
            final String[] array15 = array2 = new String[3];
            array15[0] = "Alternate Blocks";
            array15[1] = "Uses alternative block models for some blocks.";
            array15[2] = "Depends on the selected resource pack.";
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
            final boolean flag = i >= btn.xPosition && j >= btn.yPosition && i < btn.xPosition + btn.width && j < btn.yPosition + btn.height;
            if (flag) {
                return btn;
            }
        }
        return null;
    }
    
    public static int getButtonWidth(final GuiButton btn) {
        return btn.width;
    }
    
    public static int getButtonHeight(final GuiButton btn) {
        return btn.height;
    }
    
    static {
        GuiVideoSettings.videoOptions = new GameSettings.Options[] { GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.FOG_FANCY, GameSettings.Options.FOG_START, GameSettings.Options.ANAGLYPH };
    }
}
