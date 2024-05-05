/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
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
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B%\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u0007B1\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0002\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\u000bJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0006\u001a\u00020\u0002H\u0016J\u0013\u0010\u0012\u001a\u00020\n2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0002H\u0086\u0002J\u0010\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u000e\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u0002J\u000e\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u0002J\u0012\u0010\u001e\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0018\u00010\u001fH\u0016J\u000e\u0010 \u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u0002J\u000e\u0010!\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0002J\b\u0010\"\u001a\u00020#H\u0016R\u0012\u0010\f\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005\u00a2\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000e\u00a8\u0006$"}, d2={"Lnet/ccbluex/liquidbounce/value/ListValue;", "Lnet/ccbluex/liquidbounce/value/Value;", "", "name", "values", "", "value", "(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)V", "displayable", "Lkotlin/Function0;", "", "(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function0;)V", "openList", "getValues", "()[Ljava/lang/String;", "[Ljava/lang/String;", "changeValue", "", "contains", "string", "fromJson", "element", "Lcom/google/gson/JsonElement;", "getModeGet", "i", "", "getModeListNumber", "modeName", "getModeListNumber2", "mode", "getModes", "", "indexOf", "isMode", "toJson", "Lcom/google/gson/JsonPrimitive;", "KyinoClient"})
public class ListValue
extends Value<String> {
    @JvmField
    public boolean openList;
    @NotNull
    private final String[] values;

    @Nullable
    public List<String> getModes() {
        return ArraysKt.toList(this.values);
    }

    public final int getModeListNumber2(@NotNull String mode) {
        Intrinsics.checkParameterIsNotNull(mode, "mode");
        return ArraysKt.indexOf(this.values, mode);
    }

    @Nullable
    public String getModeGet(int i) {
        return this.values[i];
    }

    public final boolean contains(@Nullable String string) {
        return Arrays.stream(this.values).anyMatch(new Predicate<String>(string){
            final /* synthetic */ String $string;

            public final boolean test(@NotNull String s) {
                Intrinsics.checkParameterIsNotNull(s, "s");
                return StringsKt.equals(s, this.$string, true);
            }
            {
                this.$string = string;
            }
        });
    }

    /*
     * WARNING - void declaration
     */
    public final int indexOf(@NotNull String mode) {
        Intrinsics.checkParameterIsNotNull(mode, "mode");
        int n = 0;
        int n2 = this.values.length;
        while (n < n2) {
            void i;
            if (StringsKt.equals(this.values[i], mode, true)) {
                return (int)i;
            }
            ++i;
        }
        return 0;
    }

    public final boolean isMode(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "string");
        return StringsKt.equals((String)this.getValue(), string, true);
    }

    @Override
    public void changeValue(@NotNull String value) {
        Intrinsics.checkParameterIsNotNull(value, "value");
        for (String element : this.values) {
            if (!StringsKt.equals(element, value, true)) continue;
            this.setValue(element);
            break;
        }
    }

    /*
     * WARNING - void declaration
     */
    public final int getModeListNumber(@NotNull String modeName) {
        Intrinsics.checkParameterIsNotNull(modeName, "modeName");
        int n = 0;
        int n2 = this.values.length;
        while (n < n2) {
            void i;
            if (Intrinsics.areEqual(this.values[i], modeName)) {
                return (int)i;
            }
            ++i;
        }
        return 0;
    }

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
            this.changeValue(string);
        }
    }

    @NotNull
    public final String[] getValues() {
        return this.values;
    }

    public ListValue(@NotNull String name, @NotNull String[] values2, @NotNull String value, @NotNull Function0<Boolean> displayable) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(values2, "values");
        Intrinsics.checkParameterIsNotNull(value, "value");
        Intrinsics.checkParameterIsNotNull(displayable, "displayable");
        super(name, value, displayable);
        this.values = values2;
        this.setValue(value);
        this.setValue(value);
    }

    public ListValue(@NotNull String name, @NotNull String[] values2, @NotNull String value) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(values2, "values");
        Intrinsics.checkParameterIsNotNull(value, "value");
        this(name, values2, value, 1.INSTANCE);
    }
}

