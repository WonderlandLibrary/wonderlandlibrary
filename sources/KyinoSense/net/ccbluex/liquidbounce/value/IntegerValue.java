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
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.flux.Translate;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0004\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B5\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002\u0012\u0006\u0010\u0007\u001a\u00020\u0002\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\u000bB/\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002\u0012\u0006\u0010\u0007\u001a\u00020\u0002\u0012\u0006\u0010\f\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\rB'\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002\u0012\u0006\u0010\u0007\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u000eB?\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0002\u0012\u0006\u0010\f\u001a\u00020\u0004\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\u000fJ\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J\u000e\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u001fJ\b\u0010 \u001a\u00020!H\u0016R\u0011\u0010\u0007\u001a\u00020\u0002\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0002\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006\""}, d2={"Lnet/ccbluex/liquidbounce/value/IntegerValue;", "Lnet/ccbluex/liquidbounce/value/Value;", "", "name", "", "value", "minimum", "maximum", "displayable", "Lkotlin/Function0;", "", "(Ljava/lang/String;IIILkotlin/jvm/functions/Function0;)V", "suffix", "(Ljava/lang/String;IIILjava/lang/String;)V", "(Ljava/lang/String;III)V", "(Ljava/lang/String;IIILjava/lang/String;Lkotlin/jvm/functions/Function0;)V", "getMaximum", "()I", "getMinimum", "getSuffix", "()Ljava/lang/String;", "translate", "Lnet/ccbluex/liquidbounce/ui/client/clickgui/style/styles/flux/Translate;", "getTranslate", "()Lnet/ccbluex/liquidbounce/ui/client/clickgui/style/styles/flux/Translate;", "fromJson", "", "element", "Lcom/google/gson/JsonElement;", "set", "newValue", "", "toJson", "Lcom/google/gson/JsonPrimitive;", "KyinoClient"})
public class IntegerValue
extends Value<Integer> {
    @NotNull
    private final Translate translate;
    private final int minimum;
    private final int maximum;
    @NotNull
    private final String suffix;

    @NotNull
    public final Translate getTranslate() {
        return this.translate;
    }

    @Override
    public final void set(@NotNull Number newValue) {
        Intrinsics.checkParameterIsNotNull(newValue, "newValue");
        this.set(newValue.intValue());
    }

    @NotNull
    public JsonPrimitive toJson() {
        return new JsonPrimitive((Number)this.getValue());
    }

    @Override
    public void fromJson(@NotNull JsonElement element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        if (element.isJsonPrimitive()) {
            this.setValue(element.getAsInt());
        }
    }

    public final int getMinimum() {
        return this.minimum;
    }

    public final int getMaximum() {
        return this.maximum;
    }

    @NotNull
    public final String getSuffix() {
        return this.suffix;
    }

    public IntegerValue(@NotNull String name, int value, int minimum, int maximum, @NotNull String suffix, @NotNull Function0<Boolean> displayable) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        Intrinsics.checkParameterIsNotNull(displayable, "displayable");
        super(name, value, displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.suffix = suffix;
        this.translate = new Translate(0.0f, 0.0f);
    }

    public /* synthetic */ IntegerValue(String string, int n, int n2, int n3, String string2, Function0 function0, int n4, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n4 & 4) != 0) {
            n2 = 0;
        }
        if ((n4 & 8) != 0) {
            n3 = Integer.MAX_VALUE;
        }
        this(string, n, n2, n3, string2, function0);
    }

    public IntegerValue(@NotNull String name, int value, int minimum, int maximum, @NotNull Function0<Boolean> displayable) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(displayable, "displayable");
        this(name, value, minimum, maximum, "", displayable);
    }

    public IntegerValue(@NotNull String name, int value, int minimum, int maximum, @NotNull String suffix) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        this(name, value, minimum, maximum, suffix, 1.INSTANCE);
    }

    public IntegerValue(@NotNull String name, int value, int minimum, int maximum) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        this(name, value, minimum, maximum, 2.INSTANCE);
    }
}

