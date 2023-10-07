package dev.monsoon.module.implementation.misc;


import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.notification.Notification;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.notification.NotificationType;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.misc.Timer;
import org.lwjgl.input.Keyboard;
import dev.monsoon.module.enums.Category;

public class TakeABreak extends Module {

    public Timer timer = new Timer();

    public NumberSetting interval = new NumberSetting("Interval in minutes", 30, 5, 440, 5, this);

    public TakeABreak() {
        super("Break Reminder", Keyboard.KEY_NONE, Category.MISC);
        this.addSettings(interval);
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    public void onEvent(Event e) {
        if(e instanceof EventUpdate) {
            if(timer.hasTimeElapsed((long) (interval.getValue() * 60000), true)) {
                NotificationManager.show(new Notification(NotificationType.INFO, "Break time!", "It has been " + interval.getValue() + " minutes. Time to take a break!", 10));
            }
        }
    }
}
