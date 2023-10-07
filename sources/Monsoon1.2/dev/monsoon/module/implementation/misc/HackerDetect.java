package dev.monsoon.module.implementation.misc;


import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.notification.Notification;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.notification.NotificationType;
import dev.monsoon.module.setting.impl.BooleanSetting;
import dev.monsoon.util.misc.ServerUtil;
import dev.monsoon.util.misc.Timer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import dev.monsoon.module.enums.Category;

public class HackerDetect extends Module {

    public Timer delay = new Timer();

    public BooleanSetting report = new BooleanSetting("Report", false, this);
    public BooleanSetting alert = new BooleanSetting("Alert", true, this);

    public HackerDetect() {
        super("HackerDetect", Keyboard.KEY_NONE, Category.MISC);
        this.addSettings(report,alert);
    }

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            //NotificationManager.pendingNotifications.clear();
            for(Object o : mc.theWorld.loadedEntityList) {
                if(o instanceof EntityPlayer) {
                    EntityPlayer e = (EntityPlayer) o;
                    if(!e.isHacker && e != mc.thePlayer) {
                        if (e.fallDistance > 3 && e.onGround && !e.capabilities.isCreativeMode) {
                            this.alertHacker(e, "NoFall");
                        }
                        if (e.capabilities.isFlying && !e.capabilities.isCreativeMode /*&& e.getHealth() < 19*/) {
                            this.alertHacker(e, "Flight (Creative)");
                        }
                        if ((e.motionY == 0 && e.isAirBorne) && !e.capabilities.isCreativeMode /*&& e.getHealth() < 19*/) {
                            this.alertHacker(e, "Flight (Airwalk)");
                        }
                    }
                }
            }
        }
    }

    private void alertHacker(EntityPlayer hacker, String detection) {
        if(alert.isEnabled()) {
            NotificationManager.show(new Notification(NotificationType.WARNING, "Hacker Detect", hacker.getName() + " was detected for " + detection + ".", 2));
        }
        if(report.isEnabled()) {
            if(ServerUtil.isHypixel()) {
                mc.thePlayer.sendChatMessage("/report " + hacker.getName() + " hacking fly");
            }
        }
        hacker.isHacker = true;
    }
}
