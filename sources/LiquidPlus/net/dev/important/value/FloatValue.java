/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.value.Value;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0004\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B5\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002\u0012\u0006\u0010\u0007\u001a\u00020\u0002\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\u000bB/\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002\u0012\u0006\u0010\u0007\u001a\u00020\u0002\u0012\u0006\u0010\f\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\rB'\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002\u0012\u0006\u0010\u0007\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u000eB?\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0002\u0012\u0006\u0010\f\u001a\u00020\u0004\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\u000fJ\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u000e\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u001bJ\b\u0010\u001c\u001a\u00020\u001dH\u0016R\u0011\u0010\u0007\u001a\u00020\u0002\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0002\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\u001e"}, d2={"Lnet/dev/important/value/FloatValue;", "Lnet/dev/important/value/Value;", "", "name", "", "value", "minimum", "maximum", "displayable", "Lkotlin/Function0;", "", "(Ljava/lang/String;FFFLkotlin/jvm/functions/Function0;)V", "suffix", "(Ljava/lang/String;FFFLjava/lang/String;)V", "(Ljava/lang/String;FFF)V", "(Ljava/lang/String;FFFLjava/lang/String;Lkotlin/jvm/functions/Function0;)V", "getMaximum", "()F", "getMinimum", "getSuffix", "()Ljava/lang/String;", "fromJson", "", "element", "Lcom/google/gson/JsonElement;", "set", "newValue", "", "toJson", "Lcom/google/gson/JsonPrimitive;", "LiquidBounce"})
public class FloatValue
extends Value<Float> {
    private final float minimum;
    private final float maximum;
    @NotNull
    private final String suffix;

    public FloatValue(@NotNull String name, float value, float minimum, float maximum, @NotNull String suffix, @NotNull Function0<Boolean> displayable) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        Intrinsics.checkNotNullParameter(displayable, "displayable");
        super(name, Float.valueOf(value), displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.suffix = suffix;
    }

    public /* synthetic */ FloatValue(String string, float f, float f2, float f3, String string2, Function0 function0, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 4) != 0) {
            f2 = 0.0f;
        }
        if ((n & 8) != 0) {
            f3 = Float.MAX_VALUE;
        }
        this(string, f, f2, f3, string2, function0);
    }

    public final float getMinimum() {
        return this.minimum;
    }

    public final float getMaximum() {
        return this.maximum;
    }

    @NotNull
    public final String getSuffix() {
        return this.suffix;
    }

    public FloatValue(@NotNull String name, float value, float minimum, float maximum, @NotNull Function0<Boolean> displayable) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(displayable, "displayable");
        this(name, value, minimum, maximum, "", displayable);
    }

    public FloatValue(@NotNull String name, float value, float minimum, float maximum, @NotNull String suffix) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        this(name, value, minimum, maximum, suffix, 1.INSTANCE);
    }

    public FloatValue(@NotNull String name, float value, float minimum, float maximum) {
        Intrinsics.checkNotNullParameter(name, "name");
        this(name, value, minimum, maximum, 2.INSTANCE);
    }

    @Override
    public final void set(@NotNull Number newValue) {
        Intrinsics.checkNotNullParameter(newValue, "newValue");
        this.set(Float.valueOf(newValue.floatValue()));
    }

    @NotNull
    public JsonPrimitive toJson() {
        return new JsonPrimitive((Number)this.getValue());
    }

    @Override
    public void fromJson(@NotNull JsonElement element) {
        Intrinsics.checkNotNullParameter(element, "element");
        if (element.isJsonPrimitive()) {
            this.setValue(Float.valueOf(element.getAsFloat()));
        }
    }
}

