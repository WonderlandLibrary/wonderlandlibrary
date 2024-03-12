// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.modern;

import dev.tenacity.utils.Utils;

public abstract class Panel implements Utils
{
    public float x;
    public float y;
    public float bigRecty;
    
    public abstract void initGui();
    
    public abstract void keyTyped(final char p0, final int p1);
    
    public abstract void drawScreen(final int p0, final int p1);
    
    public abstract void mouseClicked(final int p0, final int p1, final int p2);
    
    public abstract void mouseReleased(final int p0, final int p1, final int p2);
}
