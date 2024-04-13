package wtf.dawn.module.impl.movement;


import wtf.dawn.event.Event;
import wtf.dawn.event.events.EventUpdate;
import wtf.dawn.module.Category;
import wtf.dawn.module.ModuleInfo;
import wtf.dawn.module.Module;
import org.lwjgl.input.Keyboard;
import wtf.dawn.notifications.Notification;
import wtf.dawn.notifications.NotificationManager;
import wtf.dawn.notifications.NotificationType;


@ModuleInfo(getName = "Sprint", getDescription = "Makes you sprint", getCategory = Category.Movement)
public class Sprint extends Module {

    public Sprint() {
        setKeyCode(Keyboard.KEY_N);
    }

    @Override
    public void onDisable() {
        mc.thePlayer.setSprinting(false);
        super.onDisable();
        NotificationManager.show(new Notification(NotificationType.DISABLED, "Disabled!", "Sprint was disabled.", 1));
    }

    public void onEnable() {
        NotificationManager.show(new Notification(NotificationType.ENABLED, "Enabled!", "Sprint was enabled.", 1));
    }

    @Override
    public void onEvent(Event<Event> e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(mc.thePlayer.moveForward > 0) {
                mc.thePlayer.setSprinting(true);
            }
        }
        super.onEvent(e);
    }
}