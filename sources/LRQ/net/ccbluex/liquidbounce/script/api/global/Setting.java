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
    public static final BoolValue boolean(JSObject settingInfo) {
        Object object = settingInfo.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String name = (String)object;
        Object object2 = settingInfo.getMember("default");
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
        }
        boolean bl = (Boolean)object2;
        return new BoolValue(name, bl);
    }

    @JvmStatic
    public static final IntegerValue integer(JSObject settingInfo) {
        Object object = settingInfo.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String name = (String)object;
        Object object2 = settingInfo.getMember("default");
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        int n = ((Number)object2).intValue();
        Object object3 = settingInfo.getMember("min");
        if (object3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        int min = ((Number)object3).intValue();
        Object object4 = settingInfo.getMember("max");
        if (object4 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        int max = ((Number)object4).intValue();
        return new IntegerValue(name, n, min, max);
    }

    @JvmStatic
    public static final FloatValue float(JSObject settingInfo) {
        Object object = settingInfo.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String name = (String)object;
        Object object2 = settingInfo.getMember("default");
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        float f = ((Number)object2).floatValue();
        Object object3 = settingInfo.getMember("min");
        if (object3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        float min = ((Number)object3).floatValue();
        Object object4 = settingInfo.getMember("max");
        if (object4 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        float max = ((Number)object4).floatValue();
        return new FloatValue(name, f, min, max);
    }

    @JvmStatic
    public static final TextValue text(JSObject settingInfo) {
        Object object = settingInfo.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String name = (String)object;
        Object object2 = settingInfo.getMember("default");
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String string = (String)object2;
        return new TextValue(name, string);
    }

    @JvmStatic
    public static final BlockValue block(JSObject settingInfo) {
        Object object = settingInfo.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String name = (String)object;
        Object object2 = settingInfo.getMember("default");
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        int n = ((Number)object2).intValue();
        return new BlockValue(name, n);
    }

    @JvmStatic
    public static final ListValue list(JSObject settingInfo) {
        Object object = settingInfo.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String name = (String)object;
        Object object2 = ScriptUtils.convert((Object)settingInfo.getMember("values"), String[].class);
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
        }
        String[] values = (String[])object2;
        Object object3 = settingInfo.getMember("default");
        if (object3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        String string = (String)object3;
        return new ListValue(name, values, string);
    }

    private Setting() {
    }

    static {
        Setting setting;
        INSTANCE = setting = new Setting();
    }
}

