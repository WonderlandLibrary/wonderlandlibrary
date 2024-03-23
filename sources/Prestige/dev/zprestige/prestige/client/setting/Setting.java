package dev.zprestige.prestige.client.setting;

import java.util.function.Predicate;

public abstract class Setting<T> {
    public String name;
    public String description;
    public T value;
    public Predicate<T> visible;

    public Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Setting description(String description) {
        this.description = description;
        return this;
    }

    public T getObject() {
        return value;
    }

    public void invokeValue(T value) {
        this.value = value;
    }

    public boolean visible() {
        return visible == null || visible.test(this.value);
    }

    public Setting invokeVisibility(Predicate<T> visible) {
        this.visible = visible;
        return this;
    }
}