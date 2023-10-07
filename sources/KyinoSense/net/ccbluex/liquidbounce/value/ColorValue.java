/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0004\n\u0002\b\u0003\b\u0016\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u0006B#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\nJ\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0006\u0010\u0010\u001a\u00020\tJ\b\u0010\u0011\u001a\u00020\u0002H\u0016J\b\u0010\u0012\u001a\u00020\tH\u0016J\u000e\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\tJ\n\u0010\u0017\u001a\u0004\u0018\u00010\u000fH\u0016R\u000e\u0010\u000b\u001a\u00020\tX\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lnet/ccbluex/liquidbounce/value/ColorValue;", "Lnet/ccbluex/liquidbounce/value/Value;", "", "name", "", "value", "(Ljava/lang/String;I)V", "displayable", "Lkotlin/Function0;", "", "(Ljava/lang/String;ILkotlin/jvm/functions/Function0;)V", "Expanded", "fromJson", "", "element", "Lcom/google/gson/JsonElement;", "getExpanded", "getValue", "isExpanded", "set", "newValue", "", "setExpanded", "toJson", "KyinoClient"})
public class ColorValue
extends Value<Integer> {
    private final boolean Expanded;

    public boolean isExpanded() {
        return this.Expanded;
    }

    public final boolean getExpanded() {
        return this.Expanded;
    }

    public final boolean setExpanded(boolean set) {
        return this.Expanded;
    }

    @Override
    public final void set(@NotNull Number newValue) {
        Intrinsics.checkParameterIsNotNull(newValue, "newValue");
        this.set(newValue.intValue());
    }

    @Override
    public int getValue() {
        return ((Number)super.get()).intValue();
    }

    @Override
    @Nullable
    public JsonElement toJson() {
        JsonObject valueObject = new JsonObject();
        valueObject.addProperty("red", (Number)this.getValue());
        valueObject.addProperty("green", (Number)this.getValue());
        valueObject.addProperty("blue", (Number)this.getValue());
        valueObject.addProperty("alpha", (Number)this.getValue());
        return (JsonElement)valueObject;
    }

    @Override
    public void fromJson(@NotNull JsonElement element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        if (element.isJsonPrimitive()) {
            this.setValue(element.getAsInt());
        }
    }

    public ColorValue(@NotNull String name, int value, @NotNull Function0<Boolean> displayable) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(displayable, "displayable");
        super(name, value, displayable);
    }

    public ColorValue(@NotNull String name, int value) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        this(name, value, (Function0<Boolean>)1.INSTANCE);
    }
}

