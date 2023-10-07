/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0004\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B?\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0002\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u0002\u0012\u0006\u0010\u000b\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u0002J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0002H\u0016J\u000e\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001aJ\b\u0010\u001b\u001a\u00020\u001cH\u0016R\u0011\u0010\u000b\u001a\u00020\u0002\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\n\u001a\u00020\u0002\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0002\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000e\u00a8\u0006\u001d"}, d2={"Lnet/ccbluex/liquidbounce/value/NumberValue;", "Lnet/ccbluex/liquidbounce/value/Value;", "", "name", "", "value", "minimum", "displayable", "Lkotlin/Function0;", "", "maximum", "inc", "(Ljava/lang/String;DDLkotlin/jvm/functions/Function0;DD)V", "getInc", "()D", "getMaximum", "getMinimum", "append", "o", "fromJson", "", "element", "Lcom/google/gson/JsonElement;", "getDouble", "set", "newValue", "", "toJson", "Lcom/google/gson/JsonPrimitive;", "KyinoClient"})
public class NumberValue
extends Value<Double> {
    private final double minimum;
    private final double maximum;
    private final double inc;

    @Override
    public final void set(@NotNull Number newValue) {
        Intrinsics.checkParameterIsNotNull(newValue, "newValue");
        this.set(newValue.doubleValue());
    }

    @NotNull
    public JsonPrimitive toJson() {
        return new JsonPrimitive((Number)this.getValue());
    }

    @Override
    public void fromJson(@NotNull JsonElement element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        if (element.isJsonPrimitive()) {
            this.setValue(element.getAsDouble());
        }
    }

    public double getDouble() {
        Object t = this.get();
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        return (double)MathKt.roundToInt(((Number)t).doubleValue() / this.inc) * this.inc;
    }

    @NotNull
    public final NumberValue append(double o) {
        this.set(((Number)this.get()).doubleValue() + o);
        return this;
    }

    public final double getMinimum() {
        return this.minimum;
    }

    public final double getMaximum() {
        return this.maximum;
    }

    public final double getInc() {
        return this.inc;
    }

    public NumberValue(@NotNull String name, double value, double minimum, @NotNull Function0<Boolean> displayable, double maximum, double inc) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(displayable, "displayable");
        super(name, value, displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.inc = inc;
    }

    public /* synthetic */ NumberValue(String string, double d, double d2, Function0 function0, double d3, double d4, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 4) != 0) {
            d2 = 0.0;
        }
        if ((n & 0x10) != 0) {
            d3 = DoubleCompanionObject.INSTANCE.getMAX_VALUE();
        }
        this(string, d, d2, function0, d3, d4);
    }
}

