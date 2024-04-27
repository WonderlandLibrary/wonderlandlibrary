/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 *  kotlin.jvm.JvmField
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Arrays;
import java.util.function.Predicate;
import kotlin.jvm.JvmField;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.Nullable;

public class ListValue
extends Value<String> {
    @JvmField
    public boolean openList;
    private final String[] values;

    public final boolean contains(@Nullable String string) {
        return Arrays.stream(this.values).anyMatch(new Predicate<String>(string){
            final /* synthetic */ String $string;

            public final boolean test(String s) {
                return StringsKt.equals((String)s, (String)this.$string, (boolean)true);
            }
            {
                this.$string = string;
            }
        });
    }

    @Override
    public void changeValue(String value) {
        for (String element : this.values) {
            if (!StringsKt.equals((String)element, (String)value, (boolean)true)) continue;
            this.setValue(element);
            break;
        }
    }

    public JsonPrimitive toJson() {
        return new JsonPrimitive((String)this.getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            this.changeValue(element.getAsString());
        }
    }

    public final String[] getValues() {
        return this.values;
    }

    public ListValue(String name, String[] values, String value) {
        super(name, value);
        this.values = values;
        this.setValue(value);
    }
}

