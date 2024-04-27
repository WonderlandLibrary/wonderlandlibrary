/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.FadeState;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Notifications", single=true)
public final class Notifications
extends Element {
    private final Notification exampleNotification;

    public Notifications(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 0.0;
        }
        if ((n & 2) != 0) {
            d2 = 0.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = new Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN);
        }
        this(d, d2, f, side);
    }

    public Notifications() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    public Notifications(double d, double d2, float f, Side side) {
        super(d, d2, f, side);
        this.exampleNotification = new Notification("Notification", "This is example", NotifyType.INFO, 0, 0, 24, null);
    }

    @Override
    public Border drawElement() {
        int n = 0;
        List list = new ArrayList();
        n = 0;
        for (Object object : (Iterable)LiquidBounce.INSTANCE.getHud().getNotifications()) {
            GL11.glPushMatrix();
            if (((Notification)object).drawNotification(n, (float)this.getRenderX(), (float)this.getRenderY(), this.getScale())) {
                list.add(object);
            }
            GL11.glPopMatrix();
            ++n;
        }
        for (Notification notification : list) {
            LiquidBounce.INSTANCE.getHud().getNotifications().remove(notification);
        }
        if (MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            if (!LiquidBounce.INSTANCE.getHud().getNotifications().contains(this.exampleNotification)) {
                LiquidBounce.INSTANCE.getHud().addNotification(this.exampleNotification);
            }
            this.exampleNotification.setFadeState(FadeState.STAY);
            this.exampleNotification.setDisplayTime(System.currentTimeMillis());
            return new Border(-this.exampleNotification.getWidth(), -((float)this.exampleNotification.getHeight()), 0.0f, 0.0f);
        }
        return null;
    }

    public final void drawBoarderBlur() {
    }
}

