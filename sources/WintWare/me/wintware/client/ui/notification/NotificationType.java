/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.ui.notification;

import java.awt.Color;

public enum NotificationType {
    SUCCESS(new Color(6348946).getRGB()),
    INFO(new Color(255, 255, 255).getRGB()),
    DISABLE(new Color(255, 0, 0).getRGB()),
    WARNING(new Color(16752943).getRGB());

    private final int color;

    NotificationType(int color) {
        this.color = color;
    }

    public final int getColor() {
        return this.color;
    }
}

