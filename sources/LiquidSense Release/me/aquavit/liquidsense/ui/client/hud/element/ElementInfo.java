package me.aquavit.liquidsense.ui.client.hud.element;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ElementInfo {

    String name();

    boolean single() default false;

    boolean force() default false;

    boolean disableScale() default false;

    int priority() default 0;

}
