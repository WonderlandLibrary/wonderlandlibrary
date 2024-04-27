/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jdk.nashorn.api.scripting.JSObject
 *  jdk.nashorn.api.scripting.ScriptUtils
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmStatic
 */
package net.ccbluex.liquidbounce.script.api.global;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import net.ccbluex.liquidbounce.value.BlockValue;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public final class Setting {
    public static final Setting INSTANCE;

    @JvmStatic
    public static final IntegerValue integer(JSObject jSObject) {
        Object object = jSObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String string = (String)object;
        Object object2 = jSObject.getMember("default");
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        int n = ((Number)object2).intValue();
        Object object3 = jSObject.getMember("min");
        if (object3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        int n2 = ((Number)object3).intValue();
        Object object4 = jSObject.getMember("max");
        if (object4 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        int n3 = ((Number)object4).intValue();
        return new IntegerValue(string, n, n2, n3);
    }

    @JvmStatic
    public static final BlockValue block(JSObject jSObject) {
        Object object = jSObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String string = (String)object;
        Object object2 = jSObject.getMember("default");
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        int n = ((Number)object2).intValue();
        return new BlockValue(string, n);
    }

    private Setting() {
    }

    static {
        Setting setting;
        INSTANCE = setting = new Setting();
    }

    @JvmStatic
    public static final FloatValue float(JSObject jSObject) {
        Object object = jSObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String string = (String)object;
        Object object2 = jSObject.getMember("default");
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        float f = ((Number)object2).floatValue();
        Object object3 = jSObject.getMember("min");
        if (object3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        float f2 = ((Number)object3).floatValue();
        Object object4 = jSObject.getMember("max");
        if (object4 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        float f3 = ((Number)object4).floatValue();
        return new FloatValue(string, f, f2, f3);
    }

    @JvmStatic
    public static final ListValue list(JSObject jSObject) {
        Object object = jSObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String string = (String)object;
        Object object2 = ScriptUtils.convert((Object)jSObject.getMember("values"), String[].class);
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
        }
        String[] stringArray = (String[])object2;
        Object object3 = jSObject.getMember("default");
        if (object3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String string2 = (String)object3;
        return new ListValue(string, stringArray, string2);
    }

    @JvmStatic
    public static final BoolValue boolean(JSObject jSObject) {
        Object object = jSObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String string = (String)object;
        Object object2 = jSObject.getMember("default");
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
        }
        boolean bl = (Boolean)object2;
        return new BoolValue(string, bl);
    }

    @JvmStatic
    public static final TextValue text(JSObject jSObject) {
        Object object = jSObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String string = (String)object;
        Object object2 = jSObject.getMember("default");
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String string2 = (String)object2;
        return new TextValue(string, string2);
    }
}

