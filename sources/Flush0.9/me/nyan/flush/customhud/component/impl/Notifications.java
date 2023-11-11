package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.customhud.GuiConfigureHud;
import me.nyan.flush.customhud.component.Component;

public class Notifications extends Component {
    @Override
    public void onAdded() {
    }

    @Override
    public void draw(float x, float y) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiConfigureHud)) {
            return;
        }

        flush.getNotificationManager().drawNotifications(x, y, getYPosition() == Component.Position.TOP);
    }

    @Override
    public int width() {
        return flush.getNotificationManager().getWidth();
    }

    @Override
    public int height() {
        return flush.getNotificationManager().getHeight();
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.NORMAL;
    }
}