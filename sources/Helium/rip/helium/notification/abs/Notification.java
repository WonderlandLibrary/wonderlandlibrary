package rip.helium.notification.abs;

import rip.helium.notification.enums.NotificationType;
import rip.helium.utils.font.Fonts;

/**
 * @author antja03
 */
public class Notification {

    /*
     * The type of notification this is
     * Determines what icon is drawn
     */
    private final NotificationType
            type;

    /*
     * The text that is drawn on the notification
     */
    private final String
            title;
    private final String content;

    /*
     * The dimensions of the panel
     * Height is set by default
     * Width is set depending on the content length
     */
    private double
            width;
    private final double height = 50;

    public Notification(NotificationType type, String title, String content) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.width = 30 + Fonts.f18.getStringWidth(content);
        if (width < 150)
            width = 150;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
