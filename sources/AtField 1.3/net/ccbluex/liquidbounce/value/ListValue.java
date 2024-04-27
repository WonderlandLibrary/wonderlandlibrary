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

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class ListValue
extends Value {
    private float height;
    private float rotate;
    @JvmField
    public boolean openList;
    @JvmField
    public boolean open;
    private final String[] values;

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            this.changeValue(jsonElement.getAsString());
        }
    }

    @Override
    public JsonElement toJson() {
        return (JsonElement)this.toJson();
    }

    public final boolean contains(@Nullable String string) {
        return Arrays.stream(this.values).anyMatch(new Predicate(string){
            final String $string;

            public final boolean test(String string) {
                return StringsKt.equals((String)string, (String)this.$string, (boolean)true);
            }

            public boolean test(Object object) {
                return this.test((String)object);
            }

            static {
            }
            {
                this.$string = string;
            }
        });
    }

    public JsonPrimitive toJson() {
        return new JsonPrimitive((String)this.getValue());
    }

    @Override
    public void changeValue(Object object) {
        this.changeValue((String)object);
    }

    public final void setRotate(float f) {
        this.rotate = f;
    }

    public final float getHeight() {
        return this.height;
    }

    public final float getRotate() {
        return this.rotate;
    }

    public final String[] getValues() {
        return this.values;
    }

    public ListValue(String string, String[] stringArray, String string2) {
        super(string, string2);
        this.values = stringArray;
        this.height = 15.0f;
        this.setValue(string2);
    }

    public void changeValue(String string) {
        for (String string2 : this.values) {
            if (!StringsKt.equals((String)string2, (String)string, (boolean)true)) continue;
            this.setValue(string2);
            break;
        }
    }

    public final void setHeight(float f) {
        this.height = f;
    }
}

