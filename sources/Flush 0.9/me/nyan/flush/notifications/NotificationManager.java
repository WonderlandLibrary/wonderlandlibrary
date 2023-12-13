package me.nyan.flush.notifications;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.ClickGui;
import me.nyan.flush.customhud.GuiConfigureHud;
import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.component.impl.Notifications;
import me.nyan.flush.event.EventManager;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventDrawGuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationManager {
    private final ArrayList<Notification> notifications = new ArrayList<>();

    public NotificationManager() {
        EventManager.register(this);
    }

    @SubscribeEvent
    public void onDrawScreen(EventDrawGuiScreen e) {
        if (e.getGuiScreen() instanceof GuiConfigureHud) {
            return;
        }

        Notifications component = (Notifications) Flush.getInstance().getCustomHud().getComponents()
                .stream()
                .filter(c -> c instanceof Notifications)
                .findFirst().orElse(null);
        float x = component == null ? e.getWidth() - getWidth() - 4 : component.getScaledX();
        float y = component == null ? e.getHeight() - getHeight() - 4 : component.getScaledY();
        if (component == null && e.getGuiScreen() instanceof ClickGui) {
            y -= 24;
        }
        drawNotifications(x, y, component != null && component.getYPosition() == Component.Position.TOP);
    }

    public void show(Notification.Type type, String title, String message, int time) {
        notifications.add(new Notification(type, title, message, time));
    }

    public void show(Notification.Type type, String title, String message) {
        notifications.add(new Notification(type, title, message));
    }

    public void drawNotifications(float x, float y, boolean top) {
        ArrayList<Notification> notifications = new ArrayList<>(getNotifications());
        if (top) {
            Collections.reverse(notifications);
        }
        float height = 0;
        for (Notification notification : notifications) {
            height += notification.getHeight() * notification.getLevel();
        }

        int offset = 0;
        for (Notification notification : notifications) {
            if (!notification.isShowing()) {
                getNotifications().remove(notification);
                return;
            }

            if (!notification.hasStarted()) {
                notification.show();
            }

            notification.draw(x, y + (top ? height - notification.getHeight() : 0), offset);
            offset++;
        }
    }

    public int getWidth() {
        float i = 0;
        if (notifications.isEmpty()) {
            return 140;
        }
        for (Notification notification : notifications) {
            if (!notification.isShowing()) {
                continue;
            }

            if (!notification.hasStarted()) {
                continue;
            }

            if (i < notification.getWidth()) {
                i = notification.getWidth();
            }
        }
        return (int) i;
    }

    public int getHeight() {
        float i = 0;
        if (notifications.isEmpty()) {
            return 40;
        }
        for (Notification notification : notifications) {
            i += notification.getHeight();
        }
        return (int) (i / notifications.size());
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }
}