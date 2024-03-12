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
        return (displaymode.getWidth() != displaymode2.getWidth()) ? (displaymode.getWidth() - displaymode2.getWidth()) : ((displaymode.getHeight() != displaymode2.getHeight()) ? (displaymode.getHeight() - displaymode2.getHeight()) : ((displaymode.getBitsPerPixel() != displaymode2.getBitsPerPixel()) ? (displaymode.getBitsPerPixel() - displaymode2.getBitsPerPixel()) : ((displaymode.getFrequency() != displaymode2.getFrequency()) ? (displaymode.getFrequency() - displaymode2.getFrequency()) : 0)));
    }
}
