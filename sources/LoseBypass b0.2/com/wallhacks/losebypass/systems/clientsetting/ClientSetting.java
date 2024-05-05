/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.clientsetting;

import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.utils.MC;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class ClientSetting
extends SettingsHolder
implements MC {
    private final String name = this.getMod().name();
    private final String description = this.getMod().description();
    private final boolean safe = this.getMod().safe();

    public Registration getMod() {
        return this.getClass().getAnnotation(Registration.class);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.TYPE})
    public static @interface Registration {
        public String name();

        public String description();

        public boolean safe() default true;
    }
}

