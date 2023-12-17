// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import org.lwjgl.opengl.DisplayMode;
import java.util.Comparator;

public class DisplayModeComparator implements Comparator
{
    @Override
    public int compare(final Object o1, final Object o2) {
        final DisplayMode displaymode = (DisplayMode)o1;
        final DisplayMode displaymode2 = (DisplayMode)o2;
        if (displaymode.getWidth() != displaymode2.getWidth()) {
            return displaymode.getWidth() - displaymode2.getWidth();
        }
        if (displaymode.getHeight() != displaymode2.getHeight()) {
            return displaymode.getHeight() - displaymode2.getHeight();
        }
        if (displaymode.getBitsPerPixel() != displaymode2.getBitsPerPixel()) {
            return displaymode.getBitsPerPixel() - displaymode2.getBitsPerPixel();
        }
        return (displaymode.getFrequency() != displaymode2.getFrequency()) ? (displaymode.getFrequency() - displaymode2.getFrequency()) : 0;
    }
}
