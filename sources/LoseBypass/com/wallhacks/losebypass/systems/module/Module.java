/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.ColorSetting;
import com.wallhacks.losebypass.systems.setting.settings.DoubleSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import com.wallhacks.losebypass.systems.setting.settings.KeySetting;
import com.wallhacks.losebypass.systems.setting.settings.ModeSetting;
import com.wallhacks.losebypass.systems.setting.settings.StringSetting;
import com.wallhacks.losebypass.utils.MC;
import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public abstract class Module
extends SettingsHolder
implements MC {
    private final String name = this.getMod().name();
    private final String description = this.getMod().description();
    private int bind = this.getMod().bind();
    private boolean hold = this.getMod().hold();
    private boolean visible = this.getMod().visible();
    private boolean muted = this.getMod().muted();
    private boolean enabled = this.getMod().enabled();
    private boolean alwaysListening = this.getMod().alwaysListening();
    private boolean allowHold = this.getMod().allowHold();
    private Category category = this.getMod().category();

    protected Module() {
        if (!this.getIsAlwaysListening()) return;
        LoseBypass.eventBus.register(this);
    }

    public Registration getMod() {
        return this.getClass().getAnnotation(Registration.class);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
            return;
        }
        this.disable();
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getBind() {
        return this.bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isMuted() {
        return this.muted;
    }

    public String getDescription() {
        return this.description;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isHold() {
        return this.hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }

    public boolean allowHold() {
        return this.allowHold;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void enable() {
        if (this.enabled) return;
        if (!this.getIsAlwaysListening()) {
            LoseBypass.eventBus.register(this);
        }
        LoseBypass.logger.info("Enabled " + this.getName());
        this.enabled = true;
        this.onEnable();
    }

    public void disable() {
        if (!this.enabled) return;
        if (!this.getIsAlwaysListening()) {
            LoseBypass.eventBus.unregister(this);
        }
        LoseBypass.logger.info("Disabled " + this.getName());
        this.enabled = false;
        this.onDisable();
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public boolean getIsAlwaysListening() {
        return this.alwaysListening;
    }

    public BooleanSetting booleanSetting(String name, boolean value) {
        return new BooleanSetting(name, this, value);
    }

    public ColorSetting colorSetting(String name, Color value) {
        return new ColorSetting(name, this, value);
    }

    public DoubleSetting doubleSetting(String name, double value, double min, double max) {
        return new DoubleSetting(name, this, value, min, max);
    }

    public IntSetting intSetting(String name, int value, int min, int max) {
        return new IntSetting(name, this, value, min, max);
    }

    public KeySetting keySetting(String name, int value) {
        return new KeySetting(name, this, value);
    }

    public ModeSetting modeSetting(String name, String value, List<String> values) {
        return new ModeSetting(name, this, value, values);
    }

    public StringSetting modeSetting(String name, String value) {
        return new StringSetting(name, this, value);
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.TYPE})
    public static @interface Registration {
        public String name();

        public String description();

        public Category category() default Category.MISC;

        public int bind() default 0;

        public boolean enabled() default false;

        public boolean hold() default false;

        public boolean visible() default true;

        public boolean muted() default false;

        public boolean allowHold() default true;

        public boolean alwaysListening() default false;
    }

    public static enum Category {
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        RENDER("Render"),
        MISC("Misc");

        private final String name;

        private Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

