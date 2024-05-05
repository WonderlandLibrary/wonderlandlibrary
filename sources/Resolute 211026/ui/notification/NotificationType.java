// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.notification;

import java.awt.Color;

public enum NotificationType
{
    SUCCESS("SUCCESS", 0, new Color(3522898).getRGB()), 
    INFO("INFO", 1, -8584981), 
    WARNING("WARNING", 2, new Color(13807392).getRGB()), 
    ERROR("ERROR", 3, new Color(13120307).getRGB());
    
    private final int color;
    
    private NotificationType(final String name, final int ordinal, final int color) {
        this.color = color;
    }
    
    public int getColor() {
        return this.color;
    }
}
