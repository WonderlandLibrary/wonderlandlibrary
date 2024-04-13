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


@ModuleInfo(getName = "LongJump", getDescription = "Makes you jump long.", getCategory = Category.Movement)
public class LongJump extends Module {

    private int ticks;
    public LongJump() {
        setKeyCode(Keyboard.KEY_Z);
    }

    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
        NotificationManager.show(new Notification(NotificationType.DISABLED, "Disabled!", "LongJump was disabled.", 1));
        super.onDisable();
    }

    @Override
    public void onEnable() {
        NotificationManager.show(new Notification(NotificationType.ENABLED, "Enabled!", "LongJump was enabled.", 1));
    }

    @Override
    public void onEvent(Event<Event> e) {
        if(e instanceof EventUpdate && e.isPre()) {
            // moveutil uwu;3 i add tmrw and make bmc longjump!
            mc.thePlayer.speedInAir = 0.05F;
            if(mc.thePlayer.onGround)
                mc.thePlayer.jump();
        }
        super.onEvent(e);
    }
}
