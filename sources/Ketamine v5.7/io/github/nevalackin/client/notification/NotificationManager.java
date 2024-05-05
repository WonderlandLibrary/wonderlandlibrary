package io.github.nevalackin.client.notification;

import net.minecraft.client.gui.ScaledResolution;

public interface NotificationManager {

    void add(final NotificationType type, final String title, final String body, final long duration);

    void onDraw(final ScaledResolution scaledResolution);

}
