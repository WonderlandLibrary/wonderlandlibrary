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
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0015\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b&\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\u0002\u0010\tJ\u0015\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0005\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u001bJ\u0010\u0010\u001f\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020!H&J\u000b\u0010\"\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0019J\u001d\u0010#\u001a\u00020\u001e2\u0006\u0010$\u001a\u00028\u00002\u0006\u0010%\u001a\u00028\u0000H\u0014\u00a2\u0006\u0002\u0010&J\u001d\u0010'\u001a\u00020\u001e2\u0006\u0010$\u001a\u00028\u00002\u0006\u0010%\u001a\u00028\u0000H\u0014\u00a2\u0006\u0002\u0010&J\u0013\u0010(\u001a\u00020\u001e2\u0006\u0010%\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u001bJ\n\u0010)\u001a\u0004\u0018\u00010!H&R \u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0010\"\u0004\b\u0016\u0010\u0017R\u001c\u0010\u0005\u001a\u00028\u0000X\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u001c\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001b\u00a8\u0006*"}, d2={"Lnet/ccbluex/liquidbounce/value/Value;", "T", "", "name", "", "value", "canDisplay", "Lkotlin/Function0;", "", "(Ljava/lang/String;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)V", "getCanDisplay", "()Lkotlin/jvm/functions/Function0;", "setCanDisplay", "(Lkotlin/jvm/functions/Function0;)V", "displayable", "getDisplayable", "()Z", "displayableFunc", "getName", "()Ljava/lang/String;", "textHovered", "getTextHovered", "setTextHovered", "(Z)V", "getValue", "()Ljava/lang/Object;", "setValue", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "changeValue", "", "fromJson", "element", "Lcom/google/gson/JsonElement;", "get", "onChange", "oldValue", "newValue", "(Ljava/lang/Object;Ljava/lang/Object;)V", "onChanged", "set", "toJson", "KyinoClient"})
public abstract class Value<T> {
    private boolean textHovered;
    private Function0<Boolean> displayableFunc;
    @NotNull
    private final String name;
    private T value;
    @NotNull
    private Function0<Boolean> canDisplay;

    public final boolean getTextHovered() {
        return this.textHovered;
    }

    public final void setTextHovered(boolean bl) {
        this.textHovered = bl;
    }

    public final void set(T newValue) {
        if (Intrinsics.areEqual(newValue, this.value)) {
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

    public final boolean getDisplayable() {
        return this.displayableFunc.invoke();
    }

    @Nullable
    public abstract JsonElement toJson();

    public abstract void fromJson(@NotNull JsonElement var1);

    protected void onChange(T oldValue, T newValue) {
    }

    protected void onChanged(T oldValue, T newValue) {
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final T getValue() {
        return this.value;
    }

    public final void setValue(T t) {
        this.value = t;
    }

    @NotNull
    public final Function0<Boolean> getCanDisplay() {
        return this.canDisplay;
    }

    public final void setCanDisplay(@NotNull Function0<Boolean> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "<set-?>");
        this.canDisplay = function0;
    }

    public Value(@NotNull String name, T value, @NotNull Function0<Boolean> canDisplay) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(canDisplay, "canDisplay");
        this.name = name;
        this.value = value;
        this.canDisplay = canDisplay;
        this.displayableFunc = displayableFunc.1.INSTANCE;
    }
}

