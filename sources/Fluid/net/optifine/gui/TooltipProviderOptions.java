// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.gui;

import java.util.List;
import net.optifine.Lang;
import java.util.ArrayList;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiButton;
import java.awt.Rectangle;
import net.minecraft.client.gui.GuiScreen;

public class TooltipProviderOptions implements TooltipProvider
{
    @Override
    public Rectangle getTooltipBounds(final GuiScreen guiScreen, final int x, final int y) {
        final int i = guiScreen.width / 2 - 150;
        int j = guiScreen.height / 6 - 7;
        if (y <= j + 98) {
            j += 105;
        }
        final int k = i + 150 + 150;
        final int l = j + 84 + 10;
        return new Rectangle(i, j, k - i, l - j);
    }
    
    @Override
    public boolean isRenderBorder() {
        return false;
    }
    
    @Override
    public String[] getTooltipLines(final GuiButton btn, final int width) {
        if (!(btn instanceof IOptionControl)) {
            return null;
        }
        final IOptionControl ioptioncontrol = (IOptionControl)btn;
        final GameSettings.Options gamesettings$options = ioptioncontrol.getOption();
        final String[] astring = getTooltipLines(gamesettings$options.getEnumString());
        return astring;
    }
    
    public static String[] getTooltipLines(final String key) {
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; ++i) {
            final String s = String.valueOf(key) + ".tooltip." + (i + 1);
            final String s2 = Lang.get(s, null);
            if (s2 == null) {
                break;
            }
            list.add(s2);
        }
        if (list.size() <= 0) {
            return null;
        }
        final String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }
}
