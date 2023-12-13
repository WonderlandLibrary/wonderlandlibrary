package best.azura.client.impl.ui.notification;

import best.azura.client.api.ui.notification.Notification;
import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventRenderScreen;

import java.util.ArrayList;

public class NotificationManager {
    private ArrayList<Notification> pendingNotifications = new ArrayList<>();
    private ArrayList<Notification> currentNotifications = new ArrayList<>();

    public NotificationManager() {
        Client.INSTANCE.getEventBus().register(this);
    }

    @EventHandler
    public final Listener<EventRenderScreen> eventRenderScreenListener = e -> render();

    public void addToQueue(Notification n) {
        this.pendingNotifications.add(n);
    }

    public void render() {
        update();
        if (currentNotifications.isEmpty()) return;
        double yOffset = 0;
        for (Notification n : currentNotifications) {
            n.render();
            n.yOffset = yOffset;
            yOffset += 70*n.animation;
        }
    }

    private void update() {
        ArrayList<Notification> nextPending = new ArrayList<>();
        if (!pendingNotifications.isEmpty()) {
            for (Notification n : pendingNotifications) {
                if (currentNotifications.size() >= 10) {
                    nextPending.add(n);
                    continue;
                }
                currentNotifications.add(n);
                n.reset();
            }
            pendingNotifications = nextPending;
        }
        clearFinished();
    }

    private void clearFinished() {
        ArrayList<Notification> nextCurrent = new ArrayList<>();
        if (currentNotifications.isEmpty()) return;
        for (Notification n : currentNotifications) if (!n.isAnimationFinished()) nextCurrent.add(n);
        currentNotifications = nextCurrent;
    }
}
