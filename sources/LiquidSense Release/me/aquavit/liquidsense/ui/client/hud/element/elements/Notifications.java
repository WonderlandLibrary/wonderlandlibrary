package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.ui.client.hud.designer.GuiHudDesigner;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.ColorType;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.FadeState;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Notification;
import org.lwjgl.opengl.GL11;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@ElementInfo(name = "Notifications", single = true, disableScale = true)
public class Notifications extends Element {

    private final Notification exampleNotification = new Notification("Notification", "This is an example notification.", ColorType.INFO,1500,500);

    public Notifications() {
        super(0, 30, 1f, new Side(Side.Horizontal.LEFT, Side.Vertical.UP));
    }

    public Stream<Notification> notification;
    @Override
    public Border drawElement() {
        notification = LiquidSense.hud.getNotifications().stream();
        int index = 0;
        for (Notification notify : notification.collect(Collectors.toList())) {
            GL11.glPushMatrix();
            if (notify.drawNotification(index)) {
                notification.collect(Collectors.toList()).add(notify);
            }
            GL11.glPopMatrix();
            if (notify.getFadeState() == FadeState.END) {
                LiquidSense.hud.notifications.remove(notify);
                --index;
            }
            ++index;
        }
        if (mc.currentScreen instanceof GuiHudDesigner) {
            if (!LiquidSense.hud.notifications.contains(this.exampleNotification)) {
                LiquidSense.hud.addNotification(this.exampleNotification);
            }
            this.exampleNotification.setFadeState(FadeState.STAY);
            this.exampleNotification.setDisplayTime(System.currentTimeMillis());
            return new Border(-this.exampleNotification.getWidth() - 21.5f, -this.exampleNotification.getHeight(), 0.5f, 0.0f);
        }
        return null;
    }
}
