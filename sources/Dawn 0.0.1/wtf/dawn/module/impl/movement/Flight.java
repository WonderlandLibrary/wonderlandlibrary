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
import wtf.dawn.settings.impl.SliderSetting;
import wtf.dawn.utils.MovementUtil;


@ModuleInfo(getName = "Flight", getDescription = "Makes you fly", getCategory = Category.Movement)
public class Flight extends Module {

    private final SliderSetting flySpeed = new SliderSetting("Speed", 3, 10, 0.1, 0.1);
    public Flight() {
        setKeyCode(Keyboard.KEY_F);
    }

    @Override
    public void onDisable() {
        NotificationManager.show(new Notification(NotificationType.DISABLED, "Disabled!", "Flight was disabled.", 1));
        super.onDisable();
    }

    @Override
    public void onEnable() {
        NotificationManager.show(new Notification(NotificationType.ENABLED, "Enabled!", "Flight was enabled.", 1));
    }

    @Override
    public void onEvent(Event<Event> e) {
        if(e instanceof EventUpdate && e.isPre()) {
            MovementUtil.strafe(flySpeed.getValue());
            if(!mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.thePlayer.motionY = 0;
            } else if(mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.thePlayer.motionY = flySpeed.getValue();
            } else if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.thePlayer.motionY = -flySpeed.getValue();
            }


        }
        super.onEvent(e);
    }
}
