// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.notification;

import java.awt.Color;

public enum NotificationType
{
    SUCCESS(new Color(100, 255, 100).getRGB()), 
    INFO(new Color(5, 186, 246).getRGB()), 
    ERROR(new Color(255, 100, 100).getRGB()), 
    WARNING(new Color(255, 211, 53).getRGB());
    
    private final int color;
    
    private NotificationType(final int color) {
        this.color = color;
    }
    
    public int getColor() {
        return this.color;
    }
}
