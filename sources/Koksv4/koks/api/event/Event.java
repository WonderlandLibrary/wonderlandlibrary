package koks.api.event;

import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.MapData;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kroko
 * @created on 21.01.2021 : 10:02
 */
public class Event {
    @Getter @Setter
    boolean canceled;

    public <T extends Event> T onFire() {
        EventHandling.priorityModules.forEach(module -> {
            if(module.isToggled() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null)
                module.onEvent(this);
            if(module instanceof Module.AlwaysEvent)
                ((Module.AlwaysEvent) module).alwaysEvent(this);
        });
        return (T) this;
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        Priority priority() default Priority.NORMAL;
    }

    public enum Priority {
        LOW, NORMAL, HIGH, EXTREME;
    }
}
