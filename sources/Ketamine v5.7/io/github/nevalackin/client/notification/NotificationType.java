package io.github.nevalackin.client.notification;

import io.github.nevalackin.client.ui.cfont.StaticallySizedImage;
import io.github.nevalackin.client.util.misc.ResourceUtil;

import javax.imageio.ImageIO;
import java.io.IOException;

public enum NotificationType {
    WARNING(0xFFFFFF00),
    SUCCESS(0xFF00FF59),
    INFO(0xFFA4E1FC),
    ERROR(0xFFFF0000);

    private StaticallySizedImage image;
    private final int colour;

    NotificationType(int colour) {
        this.colour = colour;
    }

    public int getColour() {
        return colour;
    }

    public StaticallySizedImage getImage() {
        if (this.image == null) {
            try {
                this.image = new StaticallySizedImage(ImageIO.read(ResourceUtil.getResourceStream(String.format("icons/notifications/%s.png", this.name().toLowerCase()))), true, 2);
            } catch (IOException ignored) {
                // TODO :: Error log
            }
        }

        return this.image;
    }
}
