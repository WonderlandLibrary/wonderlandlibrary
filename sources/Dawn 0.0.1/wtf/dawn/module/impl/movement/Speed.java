package wtf.dawn.module.impl.movement;


import  org.lwjgl.input.Keyboard;
import wtf.dawn.event.Event;
import wtf.dawn.event.events.EventUpdate;
import wtf.dawn.module.Category;
import wtf.dawn.module.Module;
import wtf.dawn.module.ModuleInfo;
import wtf.dawn.notifications.Notification;
import wtf.dawn.notifications.NotificationManager;
import wtf.dawn.notifications.NotificationType;
import wtf.dawn.settings.impl.ModeSetting;
import wtf.dawn.settings.impl.SliderSetting;
import wtf.dawn.utils.MovementUtil;




@ModuleInfo(getName = "Speed", getDescription = "Makes you fast.", getCategory = Category.Movement)
public class Speed extends Module {
    private int ticks;
    public Speed() {
        setKeyCode(Keyboard.KEY_V);
    }

    // idk how settings
    private String mode = "WatchDog";

    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
        NotificationManager.show(new Notification(NotificationType.DISABLED, "Disabled!", "Speed was disabled.", 1));
        super.onDisable();
    }

    public void onEnable() {
        NotificationManager.show(new Notification(NotificationType.ENABLED, "Enabled!", "Speed was enabled.", 1));
    }

    @Override
    public void onEvent(Event<Event> e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(mc.thePlayer.onGround) {
                ticks = 0;
            } else {
                ticks++;
            }
            switch(mode) {
                case "Vulcan":
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    }
                    mc.gameSettings.keyBindJump.setKeyCode(Keyboard.KEY_SPACE);

                    if(ticks > 6) {
                        mc.thePlayer.motionY -= 0.065F;
                    }

                    if(mc.thePlayer.onGround) {
                        MovementUtil.strafe(0.48 + MovementUtil.getDefaultSpeed() - 0.2875F);
                    }
                    break;
                case "WatchDog":
                    if(mc.thePlayer.onGround) {
                        MovementUtil.strafe(MovementUtil.getDefaultSpeed() + 0.05);
                        mc.thePlayer.jump();
                    }
                    mc.thePlayer.motionY -= 0.0004;
                    if(mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0) {
                        mc.thePlayer.motionX *= 0.1;
                        mc.thePlayer.motionZ *= 0.1;
                    }
                    break;
            }
        }
        super.onEvent(e);
    }

}