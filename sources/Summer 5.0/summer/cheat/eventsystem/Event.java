package summer.cheat.eventsystem;

import summer.base.manager.config.Cheats;

import java.lang.reflect.InvocationTargetException;


/**
 * Created by Hexeption on 18/12/2016.
 */
public abstract class Event {

    private boolean cancelled;
    public byte type;
    private static double x;
    private static double y;
    private static double z;
    private boolean onGround;
    private static float pitch;
    private static float yaw;
    private float lastPitch;
    private float lastYaw;
    private Stage stage;
    private static double lastX;
    private static double lastY;
    private static double lastZ;

    public enum State {
        PRE("PRE", 0),

        POST("POST", 1);

        private State(final String string, final int number) {

        }
    }

    public Event call() {

        this.cancelled = false;
        call(this);
        return this;
    }

    public boolean isCancelled() {

        return cancelled;
    }

    public void setCancelled(boolean cancelled) {

        this.cancelled = cancelled;
    }

    private static final void call(final Event event) {

        final ArrayHelper<Data> dataList = EventManager.get(event.getClass());

        if (dataList != null) {
            for (final Data data : dataList) {
                boolean canUse = true;
                if (data.source instanceof Cheats) {
                    Cheats m = (Cheats) data.source;
                    if (!m.isToggled() || m.getName().equalsIgnoreCase("AntiUntrusted")) {
                        canUse = false;
                    }
                }
                if (canUse) {
                    try {
                        data.target.invoke(data.source, event);
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    }
                }
            }

            for (final Data data : dataList) {
                if (data.source instanceof Cheats) {
                    Cheats m = (Cheats) data.source;
                    if (m.getName().equalsIgnoreCase("AntiUntrusted") && m.isToggled()) {
                        try {
                            data.target.invoke(data.source, event);
                        } catch (IllegalAccessException e) {
                        } catch (InvocationTargetException e) {
                        }
                    }
                }
            }
        }
    }

    public byte getType() {
        return this.type;
    }

    public void setType(final byte type) {
        this.type = type;
    }

    public boolean isPre() {
        if (stage.equals(Stage.PRE)) {
            return true;
        } else {
            return false;
        }
    }
}
