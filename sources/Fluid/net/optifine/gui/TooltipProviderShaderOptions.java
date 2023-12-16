// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.gui;

import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import net.optifine.util.StrUtils;
import java.util.List;
import net.minecraft.client.settings.GameSettings;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import net.optifine.Lang;
import net.minecraft.src.Config;
import net.optifine.shaders.config.ShaderOption;
import net.optifine.shaders.gui.GuiButtonShaderOption;
import net.minecraft.client.gui.GuiButton;

public class TooltipProviderShaderOptions extends TooltipProviderOptions
{
    @Override
    public String[] getTooltipLines(final GuiButton btn, final int width) {
        if (!(btn instanceof GuiButtonShaderOption)) {
            return null;
        }
        final GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)btn;
        final ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
        final String[] astring = this.makeTooltipLines(shaderoption, width);
        return astring;
    }
    
    private String[] makeTooltipLines(final ShaderOption so, final int width) {
        final String s = so.getNameText();
        final String s2 = Config.normalize(so.getDescriptionText()).trim();
        final String[] astring = this.splitDescription(s2);
        final GameSettings gamesettings = Config.getGameSettings();
        String s3 = null;
        if (!s.equals(so.getName()) && gamesettings.advancedItemTooltips) {
            s3 = "§8" + Lang.get("of.general.id") + ": " + so.getName();
        }
        String s4 = null;
        if (so.getPaths() != null && gamesettings.advancedItemTooltips) {
            s4 = "§8" + Lang.get("of.general.from") + ": " + Config.arrayToString(so.getPaths());
        }
        String s5 = null;
        if (so.getValueDefault() != null && gamesettings.advancedItemTooltips) {
            final String s6 = so.isEnabled() ? so.getValueText(so.getValueDefault()) : Lang.get("of.general.ambiguous");
            s5 = "§8" + Lang.getDefault() + ": " + s6;
        }
        final List<String> list = new ArrayList<String>();
        list.add(s);
        list.addAll(Arrays.asList(astring));
        if (s3 != null) {
            list.add(s3);
        }
        if (s4 != null) {
            list.add(s4);
        }
        if (s5 != null) {
            list.add(s5);
        }
        final String[] astring2 = this.makeTooltipLines(width, list);
        return astring2;
    }
    
    private String[] splitDescription(String desc) {
        if (desc.length() <= 0) {
            return new String[0];
        }
        desc = StrUtils.removePrefix(desc, "//");
        final String[] astring = desc.split("\\. ");
        for (int i = 0; i < astring.length; ++i) {
            astring[i] = "- " + astring[i].trim();
            astring[i] = StrUtils.removeSuffix(astring[i], ".");
        }
        return astring;
    }
    
    private String[] makeTooltipLines(final int width, final List<String> args) {
        final FontRenderer fontrenderer = Config.getMinecraft().fontRendererObj;
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < args.size(); ++i) {
            final String s = args.get(i);
            if (s != null && s.length() > 0) {
                for (final String s2 : fontrenderer.listFormattedStringToWidth(s, width)) {
                    list.add(s2);
                }
            }
        }
        final String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }
}
