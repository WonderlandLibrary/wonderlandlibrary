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
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u0005B#\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\n\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/value/TextValue;", "Lnet/ccbluex/liquidbounce/value/Value;", "", "name", "value", "(Ljava/lang/String;Ljava/lang/String;)V", "displayable", "Lkotlin/Function0;", "", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function0;)V", "append", "o", "", "fromJson", "", "element", "Lcom/google/gson/JsonElement;", "toJson", "Lcom/google/gson/JsonPrimitive;", "KyinoClient"})
public class TextValue
extends Value<String> {
    @NotNull
    public JsonPrimitive toJson() {
        return new JsonPrimitive((String)this.getValue());
    }

    @Override
    public void fromJson(@NotNull JsonElement element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        if (element.isJsonPrimitive()) {
            String string = element.getAsString();
            Intrinsics.checkExpressionValueIsNotNull(string, "element.asString");
            this.setValue(string);
        }
    }

    @NotNull
    public final TextValue append(@NotNull Object o) {
        Intrinsics.checkParameterIsNotNull(o, "o");
        this.set((String)this.get() + o);
        return this;
    }

    public TextValue(@NotNull String name, @NotNull String value, @NotNull Function0<Boolean> displayable) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(value, "value");
        Intrinsics.checkParameterIsNotNull(displayable, "displayable");
        super(name, value, displayable);
    }

    public TextValue(@NotNull String name, @NotNull String value) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(value, "value");
        this(name, value, (Function0<Boolean>)1.INSTANCE);
    }
}

