/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.clickgui;

import java.util.Comparator;
import net.minecraft.client.Minecraft;
import org.celestial.client.ui.clickgui.component.Component;

public class SorterHelper
implements Comparator<Component> {
    @Override
    public int compare(Component component, Component component2) {
        String s1 = component.getName();
        String s2 = component2.getName();
        int cmp = Minecraft.getMinecraft().robotoRegularFontRender.getStringWidth(s2) - Minecraft.getMinecraft().robotoRegularFontRender.getStringWidth(s1);
        return cmp != 0 ? cmp : s2.compareTo(s1);
    }
}

