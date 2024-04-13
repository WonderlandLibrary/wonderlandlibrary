package wtf.dawn.module.impl.movement;


import org.lwjgl.input.Keyboard;
import wtf.dawn.event.Event;
import wtf.dawn.event.events.EventUpdate;
import wtf.dawn.module.Category;
import wtf.dawn.module.Module;
import wtf.dawn.module.ModuleInfo;
import wtf.dawn.notifications.Notification;
import wtf.dawn.notifications.NotificationManager;
import wtf.dawn.notifications.NotificationType;


@ModuleInfo(getName = "HighJump", getDescription = "Makes you high long.", getCategory = Category.Movement)
public class HighJump extends Module {

    private boolean jumped;
    public HighJump() {
        setKeyCode(Keyboard.KEY_M);
    }

    @Override
    public void onDisable() {
        NotificationManager.show(new Notification(NotificationType.DISABLED, "Disabled!", "HighJump was disabled.", 1));
        super.onDisable();
    }

    @Override
    public void onEnable() {
        NotificationManager.show(new Notification(NotificationType.ENABLED, "Enabled!", "HighJump was enabled.", 1));
    }

    @Override
    public void onEvent(Event<Event> e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(mc.thePlayer.onGround) {
                jumped = true;
                mc.thePlayer.jump();
            }
            if(jumped && mc.thePlayer.fallDistance > 0.99) {
                mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 9, mc.thePlayer.posZ);
                jumped = false;
            }
        }
        super.onEvent(e);
    }
}
