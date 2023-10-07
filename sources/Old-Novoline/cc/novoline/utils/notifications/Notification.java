package cc.novoline.utils.notifications;

import cc.novoline.utils.Timer;
import net.minecraft.util.MathHelper;

public final class Notification {

    /* fields */
    private final NotificationType notificationType;
    private final String message;

    final Timer timer = new Timer();
    boolean extending;
    public final int delay;
    private String callReason;
    public double x, y;

    /* constructors */
    Notification(String message, int delay, NotificationType notificationType) {
        this.message = message;
        this.notificationType = notificationType;
        this.x = 0;
        this.y = 50;
        this.delay = delay;
        this.extending = false;
    }

    Notification(String callReason, String message, int delay, NotificationType notificationType) {
        this.message = message;
        this.notificationType = notificationType;
        this.x = 0;
        this.y = 50;
        this.delay = delay;
        this.extending = false;
        this.callReason = callReason;
    }

    /* methods */
    //region Lombok
    public NotificationType getType() {
        return this.notificationType;
    }

    public String getMessage() {
        return this.message;
    }

    public Timer getTimer() {
        return timer;
    }

    public int getDelay() {
        return delay;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public boolean isExtending() {
        return extending;
    }

    public void setExtending(boolean extending) {
        this.extending = extending;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getCount() {
        return MathHelper.clamp_float(getTimer().getCurrentMS() - getTimer().getLastMS(), 0, (float) getDelay());
    }

    public String getCallReason() {
        return callReason;
    }

    //endregion

}
