package koks.api.registry.module;

import koks.api.Methods;
import koks.api.event.Event;
import koks.api.manager.notification.NotificationManager;
import koks.api.manager.notification.NotificationType;
import koks.api.manager.value.Value;
import koks.api.manager.value.ValueManager;
import koks.module.player.SendPublic;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * @author kroko
 * @created on 21.01.2021 : 10:01
 */

@Getter @Setter
public abstract class Module implements Methods {

    final String name, description;
    final Category category;

    boolean toggled, bypass;
    int key;

    public Module() {
        final Info info = getClass().getAnnotation(Info.class);
        name = info.name();
        description = info.description();
        category = info.category();
        key = info.key();

        try {
            ValueManager.getInstance().register(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public abstract void onEvent(Event event);
    public abstract void onEnable();
    public abstract void onDisable();
    public boolean isVisible(Value<?> value, String name) {
        return true;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        try {
            if (toggled) {
                onEnable();
            } else {
                onDisable();
            }
        }catch(Exception ignore) {}
    }

    public void toggle() {
        setToggled(!isToggled());
        NotificationManager.getInstance().addNotification(this.getName(), toggled ? "was enabled" : "was disabled", NotificationType.INFO, 2000);
        if (getWorld() != null)
            if (ModuleRegistry.getModule(SendPublic.class).isToggled())
                getPlayer().sendChatMessage(this.getName() + ": " + (toggled ? "Toggled" : "Untoggled"));
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();
        String description();
        Category category();
        int key() default 0;
    }

    public enum Category {
        COMBAT(0xFF555D),
        PLAYER(0x52EE61),
        UTILITIES(0xCCCCCC),
        MOVEMENT(0xDEE955),
        VISUAL(0xFC56FF),
        GUI(0xFF8056),
        WORLD(0x4CDDF3),
        INVIS(0x9C55FF),
        MISC(0xB5B5A5),
        DEBUG(0x3AEFB6);

        int color;

        Category(int color) {
            this.color = color;
        }

        public Color getCategoryColor() {
            return new Color(color);
        }
    }

    public interface Unsafe {

    }

    public interface NotUnToggle {

    }

    public interface NotBypass {

    }

    public interface Shader {
        void drawShaderESP(List list, boolean flag, RenderManager renderManager, float partialTicks, Entity renderViewEntity, ICamera camera, int i);
    }

    public interface AlwaysEvent {
        void alwaysEvent(Event event);
    }
}
