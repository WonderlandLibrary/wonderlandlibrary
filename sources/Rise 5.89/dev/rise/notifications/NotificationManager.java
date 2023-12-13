package dev.rise.notifications;

import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.font.CustomFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import java.util.Deque;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
public final class NotificationManager {

    private final Deque<Notification> notifications = new ArrayDeque<>();

    public void registerNotification(final String description, final long delay, final NotificationType type) {
        notifications.add(new Notification(description, delay, type));
    }

    public void registerNotification(final String description, final NotificationType type) {
        notifications.add(new Notification(description, (long) (CustomFont.getWidth(description) * 30), type));
    }

    public void registerNotification(final String description) {
        notifications.add(new Notification(description, (long) (CustomFont.getWidth(description) * 40), NotificationType.NOTIFICATION));

        /*try {
            AuthGUI.getClipboardString();
        } catch (final Throwable t) {
            for (; ; ) {

            }
        }*/
    }

    public void onRender2D(final Render2DEvent event) {
        if (!notifications.isEmpty()) {
            if (notifications.getFirst().getEnd() > System.currentTimeMillis()) {
                notifications.getFirst().y = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - 50;
                notifications.getFirst().render();
            } else {
                notifications.removeFirst();
            }
        }

        if (notifications.size() > 0) {
            int i = 0;
            try {
                for (final Notification notification : notifications) {
                    if (i == 0) {
                        i++;
                        continue;
                    }

                    notification.y = (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - 18) - (35 * (i + 1));
                    notification.render();
                    i++;
                }
            } catch (final ConcurrentModificationException ignored) {
            }
        }
    }
}
