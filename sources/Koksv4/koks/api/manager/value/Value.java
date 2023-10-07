package koks.api.manager.value;

import koks.Koks;
import koks.api.exceptions.CastNotPossibleException;
import koks.api.registry.module.Module;
import koks.api.utils.RenderUtil;
import koks.event.ValueChangeEvent;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Getter
@Setter
public class Value<T> {
    private final List<String> modes;
    private final Object object;
    private final String name, displayName;
    private final Field field;
    private final double minimum;
    private final double maximum;
    private final boolean colorPicker, visual;

    boolean disabled;
    T savedValue;

    private T defaultValue;

    public Value(Object object, String[] modes, String name, String displayName, Field field, double minimum, double maximum, boolean colorPicker, boolean visual) {
        this.object = object;
        if (modes != null) {
            this.modes = Arrays.asList(modes);
            this.modes.sort(Comparator.comparing(String::toString));
        } else {
            this.modes = null;
        }
        if (displayName.equalsIgnoreCase(""))
            this.displayName = null;
        else
            this.displayName = displayName;
        this.name = name;
        this.field = field;
        this.minimum = minimum;
        this.maximum = maximum;
        this.colorPicker = colorPicker;
        this.visual = visual;
    }

    public boolean isVisible() {
        if (object instanceof Module) {
            final Module module = (Module) object;
            return module.isVisible(this, getName());
        }
        return true;
    }

    public void makeDisabled() {
        if (!disabled) {
            savedValue = getValue();
            disabled = true;
        }
        if (getValue() instanceof Boolean) {
            castIfPossible("false");
        } else if (getValue() instanceof String) {
            castIfPossible("");
        } else {
            setValue(getDefaultValue());
        }
    }

    public void makeEnabled() {
        if (disabled) {
            setValue(savedValue);
            disabled = false;
        }
    }

    public boolean hasValidMode() {
        for (String mode : modes) {
            if (mode.equalsIgnoreCase(castString()))
                return true;
        }
        return false;
    }

    public void setValidMode() {
        for (String mode : modes) {
            if (mode.toLowerCase().contains(castString().toLowerCase()))
                castIfPossible(mode);
        }

        if (!hasValidMode())
            castIfPossible(modes.get(0));
    }

    public String getType() {
        if (getValue() instanceof Boolean)
            return "Checkbox";
        if (getValue() instanceof String)
            return "ComboBox";
        if (getValue() instanceof Integer)
            if (colorPicker)
                return "ColorPicker";
            else
                return "Slider";
        if (getValue() instanceof Double)
            return "Slider";
        return null;
    }

    public Module getModule() {
        return (Module) object;
    }

    public T getValue() {
        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            T value = (T) field.get(object);
            field.setAccessible(accessible);
            return value;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setValue(T value) {
        T val = getValue();

        if (Koks.isLoaded()) {
            new ValueChangeEvent(this).onFire();
        }

        if (val instanceof Double && !(value instanceof Double)) {
            throw new CastNotPossibleException(value.getClass().getSimpleName(), val.getClass().getSimpleName());
        }
        if (val instanceof Integer && !(value instanceof Integer)) {
            throw new CastNotPossibleException(value.getClass().getSimpleName(), val.getClass().getSimpleName());
        }
        if (val instanceof String && !(value instanceof String)) {
            throw new CastNotPossibleException(value.getClass().getSimpleName(), val.getClass().getSimpleName());
        }
        if (val instanceof Boolean && !(value instanceof Boolean)) {
            throw new CastNotPossibleException(value.getClass().getSimpleName(), val.getClass().getSimpleName());
        }
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            if (val instanceof Double) {
                if (((Number) val).doubleValue() > maximum) {
                    field.set(object, maximum);
                } else if (((Number) val).doubleValue() < minimum) {
                    field.set(object, minimum);
                } else {
                    field.set(object, ((Number) value).doubleValue());
                }
            } else if (val instanceof Integer) {
                if (!colorPicker && ((Number) val).intValue() > maximum) {
                    field.set(object, ((Number) maximum).intValue());
                } else if (!colorPicker && ((Number) value).intValue() < minimum) {
                    field.set(object, ((Number) minimum).intValue());
                } else {
                    field.set(object, ((Number) value).intValue());
                }
            } else {
                field.set(object, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        field.setAccessible(accessible);
    }

    public void setToDefault() {
        setValue(getDefaultValue());
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue() {
        this.defaultValue = getValue();
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void castIfPossible(String value) {
        T val = getValue();

        try {
            if (val instanceof Double) {
                setValue((T) Double.valueOf(value));
                return;
            }
            if (val instanceof Integer) {
                setValue((T) Integer.valueOf(value));
                return;
            }
        } catch (NumberFormatException ignore) {
        }
        if (val instanceof String && !value.equals("null")) {
            setValue((T) value);
            return;
        }
        if (val instanceof Boolean && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))) {
            setValue((T) Boolean.valueOf(value));
            return;
        }
    }

    public void castSavedIfPossible(String value) {
        T val = getSavedValue();

        try {
            if (val instanceof Double) {
                setSavedValue((T) Double.valueOf(value));
                return;
            }
            if (val instanceof Integer) {
                setSavedValue((T) Integer.valueOf(value));
                return;
            }
        } catch (NumberFormatException ignore) {
        }
        if (val instanceof String && !value.equals("null")) {
            setSavedValue((T) value);
            return;
        }
        if (val instanceof Boolean && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))) {
            setSavedValue((T) Boolean.valueOf(value));
            return;
        }
    }

    public int castInteger() {
        T value = getValue();
        if (value instanceof Integer) {
            return ((Number) value).intValue();
        } else {
            throw new CastNotPossibleException(value.getClass().getSimpleName(), Integer.class.getSimpleName());
        }
    }

    public double castDouble() {
        T value = getValue();
        if (value instanceof Integer || value instanceof Double) {
            return ((Number) value).doubleValue();
        } else {
            throw new CastNotPossibleException(value.getClass().getSimpleName(), Double.class.getSimpleName());
        }
    }

    public Boolean castBoolean() {
        T value = getValue();
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            throw new CastNotPossibleException(value.getClass().getSimpleName(), Boolean.class.getSimpleName());
        }
    }

    public String castString() {
        T value = getValue();
        if (value instanceof String) {
            return (String) value;
        } else {
            throw new CastNotPossibleException(value.getClass().getSimpleName(), String.class.getSimpleName());
        }
    }
}