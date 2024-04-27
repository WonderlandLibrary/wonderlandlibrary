package me.aquavit.liquidsense.value;

import com.google.gson.JsonElement;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.render.Translate;
import me.aquavit.liquidsense.LiquidSense;

import java.util.function.BooleanSupplier;

public abstract class Value<T> {

    public final String name;
    public T value;
    public final Translate MultiBoolvalue = new Translate(0f, 0f);
    public float MultiBool;
    public final Translate listvalue = new Translate(0f, 0f);
    public float list;
    public final Translate boolvalue = new Translate(0f, 0f);
    public float boolVal;
    public final Translate floatvalue = new Translate(0f, 0f);
    public float floatVal;
    public final Translate intvalue = new Translate(0f, 0f);
    public float intVal;

    public Value(String name, T value) {
        this.name = name;
        this.value = value;
    }

    private BooleanSupplier displayableFunc = () -> true;

    public Value<T> displayable(BooleanSupplier func) {
        displayableFunc = func;
        return this;
    }

    public boolean isDisplayable() {
        return displayableFunc.getAsBoolean();
    }

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }


    /*
    private BooleanSupplier displayableFunc = () -> true;

    public Value<T> displayable(BooleanSupplier func) {
        displayableFunc = func;
        return this;
    }

    public boolean isDisplayable() {
        return displayableFunc.getAsBoolean();
    }
     */

    /*
    private Supplier<Boolean> displayableFunc = () -> true;

    public Value<T> displayable(Supplier<Boolean> func) {
        displayableFunc = func;
        return this;
    }

    public boolean isDisplayable() {
        return displayableFunc.get();
    }
     */

    public void set(T newValue) {
        if (newValue == value)
            return;
        T oldValue = get();
        try {
            onChange(oldValue, newValue);
            changeValue(newValue);
            onChanged(oldValue, newValue);
            LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.valuesConfig);
        } catch (Exception e) {
            ClientUtils.getLogger().error("[ValueSystem (" + name + ")]: " + e.getClass().getName() + " (" + e.getMessage() + ") [" + oldValue + " >> " + newValue + "]");
        }
    }

    public T get() {
        return value;
    }

    public void changeValue(T value) {
        this.value = value;
    }

    public abstract JsonElement toJson();

    public abstract void fromJson(JsonElement element);

    protected void onChange(T oldValue, T newValue) {
    }

    protected void onChanged(T oldValue, T newValue) {
    }
}

