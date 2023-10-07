package koks.api.registry.credits;

import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Getter @Setter
public class Credits {

    final String name, link;
    final String[] helped;

    public Credits() {
        final Info info = this.getClass().getAnnotation(Info.class);
        this.name = info.discordTag();
        this.link = info.link();
        this.helped = info.helped();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String discordTag();
        String link() default "";
        String[] helped();
    }
}
