// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.ArrayList;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiVideoSettings;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class TooltipManager
{
    private GuiScreen guiScreen;
    private int lastMouseX;
    private int lastMouseY;
    private long mouseStillTime;
    
    public TooltipManager(final GuiScreen guiScreen) {
        this.guiScreen = null;
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.mouseStillTime = 0L;
        this.guiScreen = guiScreen;
    }
    
    public void drawTooltips(final int x, final int y, final List buttonList) {
        if (Math.abs(x - this.lastMouseX) <= 5 && Math.abs(y - this.lastMouseY) <= 5) {
            final short activateDelay = 700;
            if (System.currentTimeMillis() >= this.mouseStillTime + activateDelay) {
                final int x2 = this.guiScreen.width / 2 - 150;
                int y2 = this.guiScreen.height / 6 - 7;
                if (y <= y2 + 98) {
                    y2 += 105;
                }
                final int x3 = x2 + 150 + 150;
                final int y3 = y2 + 84 + 10;
                final GuiButton btn = this.getSelectedButton(x, y, buttonList);
                if (btn instanceof IOptionControl) {
                    final IOptionControl ctl = (IOptionControl)btn;
                    final GameSettings.Options option = ctl.getOption();
                    final String[] lines = getTooltipLines(option);
                    if (lines == null) {
                        return;
                    }
                    GuiVideoSettings.drawGradientRect(this.guiScreen, x2, y2, x3, y3, -536870912, -536870912);
                    for (int i = 0; i < lines.length; ++i) {
                        final String line = lines[i];
                        int col = 14540253;
                        if (line.endsWith("!")) {
                            col = 16719904;
                        }
                        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
                        fontRenderer.func_175063_a(line, (float)(x2 + 5), (float)(y2 + 5 + i * 11), col);
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
    
    private GuiButton getSelectedButton(final int x, final int y, final List buttonList) {
        for (int k = 0; k < buttonList.size(); ++k) {
            final GuiButton btn = buttonList.get(k);
            final int btnWidth = GuiVideoSettings.getButtonWidth(btn);
            final int btnHeight = GuiVideoSettings.getButtonHeight(btn);
            final boolean flag = x >= btn.xPosition && y >= btn.yPosition && x < btn.xPosition + btnWidth && y < btn.yPosition + btnHeight;
            if (flag) {
                return btn;
            }
        }
        return null;
    }
    
    private static String[] getTooltipLines(final GameSettings.Options option) {
        return getTooltipLines(option.getEnumString());
    }
    
    private static String[] getTooltipLines(final String key) {
        final ArrayList list = new ArrayList();
        for (int lines = 0; lines < 10; ++lines) {
            final String lineKey = String.valueOf(key) + ".tooltip." + (lines + 1);
            final String line = Lang.get(lineKey, null);
            if (line == null) {
                break;
            }
            list.add(line);
        }
        if (list.size() <= 0) {
            return null;
        }
        final String[] var5 = list.toArray(new String[list.size()]);
        return var5;
    }
}
