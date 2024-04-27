/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B%\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u0007B1\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0002\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\u000bJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0006\u001a\u00020\u0002H\u0016J\u0013\u0010\u0012\u001a\u00020\n2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0002H\u0086\u0002J\u0010\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u0012\u0010\f\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005\u00a2\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0019"}, d2={"Lnet/dev/important/value/ListValue;", "Lnet/dev/important/value/Value;", "", "name", "values", "", "value", "(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)V", "displayable", "Lkotlin/Function0;", "", "(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function0;)V", "openList", "getValues", "()[Ljava/lang/String;", "[Ljava/lang/String;", "changeValue", "", "contains", "string", "fromJson", "element", "Lcom/google/gson/JsonElement;", "toJson", "Lcom/google/gson/JsonPrimitive;", "LiquidBounce"})
public class ListValue
extends Value<String> {
    @NotNull
    private final String[] values;
    @JvmField
    public boolean openList;

    public ListValue(@NotNull String name, @NotNull String[] values2, @NotNull String value, @NotNull Function0<Boolean> displayable) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(values2, "values");
        Intrinsics.checkNotNullParameter(value, "value");
        Intrinsics.checkNotNullParameter(displayable, "displayable");
        super(name, value, displayable);
        this.values = values2;
        this.setValue(value);
    }

    @NotNull
    public final String[] getValues() {
        return this.values;
    }

    public ListValue(@NotNull String name, @NotNull String[] values2, @NotNull String value) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(values2, "values");
        Intrinsics.checkNotNullParameter(value, "value");
        this(name, values2, value, 1.INSTANCE);
    }

    public final boolean contains(@Nullable String string) {
        return Arrays.stream(this.values).anyMatch(arg_0 -> ListValue.contains$lambda-0(string, arg_0));
    }

    @Override
    public void changeValue(@NotNull String value) {
        Intrinsics.checkNotNullParameter(value, "value");
        for (String element : this.values) {
            if (!StringsKt.equals(element, value, true)) continue;
            this.setValue(element);
            break;
        }
    }

    @NotNull
    public JsonPrimitive toJson() {
        return new JsonPrimitive((String)this.getValue());
    }

    @Override
    public void fromJson(@NotNull JsonElement element) {
        Intrinsics.checkNotNullParameter(element, "element");
        if (element.isJsonPrimitive()) {
            String string = element.getAsString();
            Intrinsics.checkNotNullExpressionValue(string, "element.asString");
            this.changeValue(string);
        }
    }

    private static final boolean contains$lambda-0(String $string, String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        return StringsKt.equals(s, $string, true);
    }
}

