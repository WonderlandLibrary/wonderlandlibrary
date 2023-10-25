package ru.smertnix.celestial.ui.notification;

import java.awt.*;

public enum NotificationMode {
    SUCCESS("success", new Color(0, 255, 0), "a"),
    WARNING("error", new Color(255, 128, 0), "b"),
    INFO("info",new Color(255, 255, 255), "c"),
	  BREAK("shield",new Color(255, 255, 255), "c");
	  
    private final String iconString;
    private final String titleString;
    private final Color color;

    NotificationMode(String titleString, Color color, String iconString) {
        this.titleString = titleString;
        this.color = color;
        this.iconString = iconString;
    }

    public final String getIconString() {
        return iconString;
    }
    public final Color getColor() {
        return color;
    }
    public final String getTitleString() {
        return titleString;
    }
}
