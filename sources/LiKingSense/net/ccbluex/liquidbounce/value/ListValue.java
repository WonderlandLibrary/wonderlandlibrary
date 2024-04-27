/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 *  kotlin.Metadata
 *  kotlin.collections.ArraysKt
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B#\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0006\u001a\u00020\u0002H\u0016J\u0013\u0010\u000f\u001a\u00020\t2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0002H\u0086\u0002J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016R\u0012\u0010\b\u001a\u00020\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005\u00a2\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0018"}, d2={"Lnet/ccbluex/liquidbounce/value/ListValue;", "Lnet/ccbluex/liquidbounce/value/Value;", "", "name", "values", "", "value", "(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)V", "openList", "", "getValues", "()[Ljava/lang/String;", "[Ljava/lang/String;", "changeValue", "", "contains", "string", "fromJson", "element", "Lcom/google/gson/JsonElement;", "getModes", "", "toJson", "Lcom/google/gson/JsonPrimitive;", "LiKingSense"})
public class ListValue
extends Value<String> {
    @JvmField
    public boolean openList;
    @NotNull
    private final String[] values;

    @NotNull
    public List<String> getModes() {
        return ArraysKt.toList((Object[])this.values);
    }

    public final boolean contains(@Nullable String string) {
        return Arrays.stream(this.values).anyMatch(new Predicate<String>(string){
            final /* synthetic */ String $string;

            public final boolean test(@NotNull String s) {
                Intrinsics.checkParameterIsNotNull((Object)s, (String)"s");
                return StringsKt.equals((String)s, (String)this.$string, (boolean)true);
            }
            {
                this.$string = string;
            }
        });
    }

    @Override
    public void changeValue(@NotNull String value) {
        Intrinsics.checkParameterIsNotNull((Object)value, (String)"value");
        for (String element : this.values) {
            if (!StringsKt.equals((String)element, (String)value, (boolean)true)) continue;
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
        Intrinsics.checkParameterIsNotNull((Object)element, (String)"element");
        if (element.isJsonPrimitive()) {
            String string = element.getAsString();
            Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"element.asString");
            this.changeValue(string);
        }
    }

    @NotNull
    public final String[] getValues() {
        return this.values;
    }

    public ListValue(@NotNull String name, @NotNull String[] values, @NotNull String value) {
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
        Intrinsics.checkParameterIsNotNull((Object)values, (String)"values");
        Intrinsics.checkParameterIsNotNull((Object)value, (String)"value");
        super(name, value);
        this.values = values;
        this.setValue(value);
    }
}

