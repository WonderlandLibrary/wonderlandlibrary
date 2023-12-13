package koks.api.manager.value;

import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@SuppressWarnings("rawtypes")
public class ValueManager {

    private static ValueManager instance;

    @Getter
    private final List<Value<?>> values = new ArrayList<>();

    public void register(Object o) throws IllegalAccessException {
        if (o == null) {
            return;
        }
        Class<?> oClass = o.getClass();
        for (Field field : oClass.getDeclaredFields()) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            if (field.isAnnotationPresent(koks.api.manager.value.annotation.Value.class)) {
                final koks.api.manager.value.annotation.Value annotation = field.getAnnotation(koks.api.manager.value.annotation.Value.class);
                final Value<?> value = new Value<>(o, annotation.modes(), annotation.name(), annotation.displayName(), field, annotation.minimum(), annotation.maximum(), annotation.colorPicker(), annotation.visual());
                values.add(value);
            }
            field.setAccessible(accessible);
        }
    }

    public void setDefaults() {
        values.forEach(Value::setDefaultValue);
    }

    /**
     * This method return a value by its name.
     */
    public Value getValue(String name) {
        return values.stream().filter(value -> value.getName().equals(name)).findFirst().orElse(null);
    }

    public Value getValue(String name, Object module) {
        return values.stream().filter(value -> value.getObject().equals(module) && value.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Value getValue(String name, Class<? extends Module> module) {
        return values.stream().filter(value -> value.getObject().getClass().equals(module) && value.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * This method returns all values from a class.
     */
    public List<Value> getValues(Class<?> clazz) {
        return values.stream().filter(value -> value.getObject().getClass() == clazz).collect(Collectors.toList());
    }

    /**
     * This method returns all values from a class name.
     */
    public List<Value> getValues(String name) {
        return getValues(Objects.requireNonNull(ModuleRegistry.getModules().stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null)).getClass());
    }

    public static synchronized ValueManager getInstance() {
        if(instance == null){
            instance = new ValueManager();
        }
        return instance;
    }
}
