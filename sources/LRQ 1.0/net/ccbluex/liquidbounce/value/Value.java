/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Value<T> {
    private final String name;
    private T value;

    public final void set(T newValue) {
        if (newValue.equals(this.value)) {
            return;
        }
        T oldValue = this.get();
        try {
            this.onChange(oldValue, newValue);
            this.changeValue(newValue);
            this.onChanged(oldValue, newValue);
            LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("[ValueSystem (" + this.name + ")]: " + e.getClass().getName() + " (" + e.getMessage() + ") [" + oldValue + " >> " + newValue + ']');
        }
    }

    public final T get() {
        return this.value;
    }

    public void changeValue(T value) {
        this.value = value;
    }

    @Nullable
    public abstract JsonElement toJson();

    public abstract void fromJson(@NotNull JsonElement var1);

    protected void onChange(T oldValue, T newValue) {
    }

    protected void onChanged(T oldValue, T newValue) {
    }

    public final String getName() {
        return this.name;
    }

    public final T getValue() {
        return this.value;
    }

    public final void setValue(T t) {
        this.value = t;
    }

    public Value(String name, T value) {
        this.name = name;
        this.value = value;
    }
}

