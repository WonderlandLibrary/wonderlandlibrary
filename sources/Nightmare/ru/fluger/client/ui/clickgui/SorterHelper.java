// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui;

import java.awt.Component;
import java.util.Comparator;

public class SorterHelper implements Comparator<Component>
{
    @Override
    public int compare(final Component component, final Component component2) {
        final String s1 = component.getName();
        final String s2 = component2.getName();
        final int cmp = bib.z().robotoRegularFontRender.getStringWidth(s2) - bib.z().robotoRegularFontRender.getStringWidth(s1);
        return (cmp != 0) ? cmp : s2.compareTo(s1);
    }
}
