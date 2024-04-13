package wtf.dawn.module.impl.visual;


import net.minecraft.entity.Entity;
import wtf.dawn.event.Event;
import wtf.dawn.event.events.EventUpdate;
import wtf.dawn.module.Category;
import wtf.dawn.module.ModuleInfo;
import wtf.dawn.module.Module;
import org.lwjgl.input.Keyboard;
import wtf.dawn.notifications.Notification;
import wtf.dawn.notifications.NotificationManager;
import wtf.dawn.notifications.NotificationType;


@ModuleInfo(getName = "Esp", getDescription = "Esp", getCategory = Category.Movement)
public class Esp extends Module {

    public Esp() {
        setKeyCode(Keyboard.KEY_P);
    }

    @Override
    public void onDisable() {
        NotificationManager.show(new Notification(NotificationType.DISABLED, "Disabled!", "Esp was disabled.", 1));
        super.onDisable();
    }

    @Override
    public void onEnable() {
        NotificationManager.show(new Notification(NotificationType.ENABLED, "Enabled!", "Esp was enabled.", 1));
    }

    @Override
    public void onEvent(Event<Event> e) {
        for(Entity entity : mc.theWorld.loadedEntityList) {
            
        }
        super.onEvent(e);
    }
}
