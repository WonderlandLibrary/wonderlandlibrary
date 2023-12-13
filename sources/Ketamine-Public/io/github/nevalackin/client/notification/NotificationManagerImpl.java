package io.github.nevalackin.client.notification;

import io.github.nevalackin.client.notification.NotificationManager;
import io.github.nevalackin.client.notification.NotificationType;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.render.game.DrawScreenEvent;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public final class NotificationManagerImpl implements NotificationManager {

    private final List<Notification> notifications = new ArrayList<>();

    public NotificationManagerImpl() {
        KetamineClient.getInstance().getEventBus().subscribe(this);
    }

    @EventLink
    private final Listener<RenderGameOverlayEvent> onDrawGameOverlay = event -> {
        this.onDraw(event.getScaledResolution());
    };

    @EventLink
    private final Listener<DrawScreenEvent> onDrawScreen = event -> {
        this.onDraw(event.getScaledResolution());
    };

    @Override
    public void add(final NotificationType type, final String title, final String body, final long duration) {
        this.notifications.add(new Notification(KetamineClient.getInstance().getFontRenderer(), title, body, duration, type));
    }

    @Override
    public void onDraw(final ScaledResolution scaledResolution) {
        int removeIndex = -1;

        glPushMatrix();
        glTranslated(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), 1);

        for (int i = 0, size = this.notifications.size(); i < size; i++) {
            final Notification notification = this.notifications.get(i);

            if (notification.isDead()) {
                removeIndex = i;
                continue;
            }

            glTranslated(0, -(notification.getHeight() + 2), 0);
            notification.render();
        }

        glPopMatrix();

        if (removeIndex != -1)
            this.notifications.remove(removeIndex);
    }
}
