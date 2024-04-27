/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  kotlin.jvm.functions.Function0
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import kotlin.jvm.functions.Function0;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Value {
    private Function0 displayableFunc;
    private Object value;
    private final String name;

    public Value(String string, Object object) {
        this.name = string;
        this.value = object;
        this.displayableFunc = displayableFunc.1.INSTANCE;
    }

    public abstract void fromJson(@NotNull JsonElement var1);

    public final boolean getDisplayable() {
        return (Boolean)this.displayableFunc.invoke();
    }

    public final String getName() {
        return this.name;
    }

    public final Value displayable(Function0 function0) {
        this.displayableFunc = function0;
        return this;
    }

    public final Object get() {
        return this.value;
    }

    public void changeValue(Object object) {
        this.value = object;
    }

    protected void onChange(Object object, Object object2) {
    }

    public final void setValue(Object object) {
        this.value = object;
    }

    public final void set(Object object) {
        if (object.equals(this.value)) {
            return;
        }
        Object object2 = this.get();
        try {
            this.onChange(object2, object);
            this.changeValue(object);
            this.onChanged(object2, object);
            LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
        }
        catch (Exception exception) {
            ClientUtils.getLogger().error("[ValueSystem (" + this.name + ")]: " + exception.getClass().getName() + " (" + exception.getMessage() + ") [" + object2 + " >> " + object + ']');
        }
    }

    public final Object getValue() {
        return this.value;
    }

    protected void onChanged(Object object, Object object2) {
    }

    @Nullable
    public abstract JsonElement toJson();
}

