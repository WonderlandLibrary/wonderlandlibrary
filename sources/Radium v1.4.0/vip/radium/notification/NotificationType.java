package vip.radium.notification;

import java.awt.*;

public enum NotificationType {
    SUCCESS(new Color(0x35C152).getRGB()),
    INFO(new Color(0x379BEC).getRGB()),
    WARNING(new Color(0xD2AF20).getRGB()),
    ERROR(new Color(0xC83333).getRGB());

    private final int color;

    NotificationType(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
