package com.canon.majik.api.event.eventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
    Priority getPriority() default Priority.DEFAULT;

}
