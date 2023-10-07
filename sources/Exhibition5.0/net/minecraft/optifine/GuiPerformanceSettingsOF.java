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

public class GuiPerformanceSettingsOF extends GuiScreen
{
    private GuiScreen prevScreen;
    protected String title;
    private GameSettings settings;
    private static GameSettings.Options[] enumOptions;
    private int lastMouseX;
    private int lastMouseY;
    private long mouseStillTime;
    
    public GuiPerformanceSettingsOF(final GuiScreen guiscreen, final GameSettings gamesettings) {
        this.title = "Performance Settings";
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.mouseStillTime = 0L;
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }
    
    @Override
    public void initGui() {
        int i = 0;
        for (final GameSettings.Options enumoptions : GuiPerformanceSettingsOF.enumOptions) {
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
        if (btnName.equals("Smooth FPS")) {
            final String[] array = array2 = new String[5];
            array[0] = "Stabilizes FPS by flushing the graphic driver buffers";
            array[1] = "  OFF - no stabilization, FPS may fluctuate";
            array[2] = "  ON - FPS stabilization";
            array[3] = "This option is graphics driver dependant and its effect";
            array[4] = "is not always visible";
        }
        else if (btnName.equals("Smooth World")) {
            final String[] array3 = array2 = new String[5];
            array3[0] = "Removes lag spikes caused by the internal server.";
            array3[1] = "  OFF - no stabilization, FPS may fluctuate";
            array3[2] = "  ON - FPS stabilization";
            array3[3] = "Stabilizes FPS by distributing the internal server load.";
            array3[4] = "Effective only for local worlds (single player).";
        }
        else if (btnName.equals("Load Far")) {
            final String[] array4 = array2 = new String[6];
            array4[0] = "Loads the world chunks at distance Far.";
            array4[1] = "Switching the render distance does not cause all chunks ";
            array4[2] = "to be loaded again.";
            array4[3] = "  OFF - world chunks loaded up to render distance";
            array4[4] = "  ON - world chunks loaded at distance Far, allows";
            array4[5] = "       fast render distance switching";
        }
        else if (btnName.equals("Preloaded Chunks")) {
            final String[] array5 = array2 = new String[6];
            array5[0] = "Defines an area in which no chunks will be loaded";
            array5[1] = "  OFF - after 5m new chunks will be loaded";
            array5[2] = "  2 - after 32m  new chunks will be loaded";
            array5[3] = "  8 - after 128m new chunks will be loaded";
            array5[4] = "Higher values need more time to load all the chunks";
            array5[5] = "and may decrease the FPS.";
        }
        else if (btnName.equals("Chunk Updates")) {
            final String[] array6 = array2 = new String[6];
            array6[0] = "Chunk updates";
            array6[1] = " 1 - (default) slower world loading, higher FPS";
            array6[2] = " 3 - faster world loading, lower FPS";
            array6[3] = " 5 - fastest world loading, lowest FPS";
            array6[4] = "Number of chunk updates per rendered frame,";
            array6[5] = "higher values may destabilize the framerate.";
        }
        else if (btnName.equals("Dynamic Updates")) {
            final String[] array7 = array2 = new String[5];
            array7[0] = "Dynamic chunk updates";
            array7[1] = " OFF - (default) standard chunk updates per frame";
            array7[2] = " ON - more updates while the player is standing still";
            array7[3] = "Dynamic updates force more chunk updates while";
            array7[4] = "the player is standing still to load the world faster.";
        }
        else if (btnName.equals("Lazy Chunk Loading")) {
            final String[] array8 = array2 = new String[7];
            array8[0] = "Lazy Chunk Loading";
            array8[1] = " OFF - default server chunk loading";
            array8[2] = " ON - lazy server chunk loading (smoother)";
            array8[3] = "Smooths the integrated server chunk loading by";
            array8[4] = "distributing the chunks over several ticks.";
            array8[5] = "Turn it OFF if parts of the world do not load correctly.";
            array8[6] = "Effective only for local worlds and single-core CPU.";
        }
        else if (btnName.equals("Fast Math")) {
            final String[] array9 = array2 = new String[5];
            array9[0] = "Fast Math";
            array9[1] = " OFF - standard math (default)";
            array9[2] = " ON - faster math";
            array9[3] = "Uses optimized sin() and cos() functions which can";
            array9[4] = "better utilize the CPU cache and increase the FPS.";
        }
        else if (btnName.equals("Fast Render")) {
            final String[] array10 = array2 = new String[7];
            array10[0] = "Fast Render";
            array10[1] = " OFF - standard rendering (default)";
            array10[2] = " ON - faster rendering";
            array10[3] = "Uses optimized rendering algorithm which decreases";
            array10[4] = "the GPU load and may substantionally increase the FPS.";
            array10[5] = "You can turn if OFF if you notice flickering textures";
            array10[6] = "on some blocks.";
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
        GuiPerformanceSettingsOF.enumOptions = new GameSettings.Options[] { GameSettings.Options.FAST_MATH, GameSettings.Options.SMOOTH_WORLD, GameSettings.Options.LOAD_FAR, GameSettings.Options.PRELOADED_CHUNKS, GameSettings.Options.CHUNK_UPDATES, GameSettings.Options.CHUNK_UPDATES_DYNAMIC, GameSettings.Options.FAST_RENDER, GameSettings.Options.LAZY_CHUNK_LOADING };
    }
}
