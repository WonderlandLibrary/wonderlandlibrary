// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.font;

import java.awt.Color;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

public interface AbstractFontRenderer
{
    @Exclude({ Strategy.NAME_REMAPPING })
    float getStringWidth(final String p0);
    
    int drawStringWithShadow(final String p0, final float p1, final float p2, final int p3);
    
    @Exclude({ Strategy.NAME_REMAPPING })
    void drawStringWithShadow(final String p0, final float p1, final float p2, final Color p3);
    
    int drawCenteredString(final String p0, final float p1, final float p2, final int p3);
    
    @Exclude({ Strategy.NAME_REMAPPING })
    void drawCenteredString(final String p0, final float p1, final float p2, final Color p3);
    
    String trimStringToWidth(final String p0, final int p1);
    
    String trimStringToWidth(final String p0, final int p1, final boolean p2);
    
    int drawString(final String p0, final float p1, final float p2, final int p3, final boolean p4);
    
    @Exclude({ Strategy.NAME_REMAPPING })
    void drawString(final String p0, final float p1, final float p2, final Color p3);
    
    int drawString(final String p0, final float p1, final float p2, final int p3);
    
    @Exclude({ Strategy.NAME_REMAPPING })
    float getMiddleOfBox(final float p0);
    
    @Exclude({ Strategy.NAME_REMAPPING })
    int getHeight();
}
