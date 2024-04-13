package dev.tenacity.config;

import com.google.gson.annotations.Expose;

public final class ConfigSetting {

    @Expose
    private final String name;

    @Expose
    private final Object value;

    public ConfigSetting(final String name, final Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}

