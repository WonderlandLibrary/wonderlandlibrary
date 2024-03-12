// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.notifications;

import java.awt.Color;

public enum NotificationType
{
    SUCCESS(new Color(20, 250, 90), "o"), 
    DISABLE(new Color(255, 30, 30), "p"), 
    INFO(Color.WHITE, "m"), 
    WARNING(Color.YELLOW, "r");
    
    private final Color color;
    private final String icon;
    
    public Color getColor() {
        return this.color;
    }
    
    public String getIcon() {
        return this.icon;
    }
    
    private NotificationType(final Color color, final String icon) {
        this.color = color;
        this.icon = icon;
    }
}
