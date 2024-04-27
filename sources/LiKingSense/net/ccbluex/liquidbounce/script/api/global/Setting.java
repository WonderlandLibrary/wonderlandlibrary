/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jdk.nashorn.api.scripting.JSObject
 *  jdk.nashorn.api.scripting.ScriptUtils
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.script.api.global;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.value.BlockValue;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0011"}, d2={"Lnet/ccbluex/liquidbounce/script/api/global/Setting;", "", "()V", "block", "Lnet/ccbluex/liquidbounce/value/BlockValue;", "settingInfo", "Ljdk/nashorn/api/scripting/JSObject;", "boolean", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "float", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "integer", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "list", "Lnet/ccbluex/liquidbounce/value/ListValue;", "text", "Lnet/ccbluex/liquidbounce/value/TextValue;", "LiKingSense"})
public final class Setting {
    public static final Setting INSTANCE;

    @JvmStatic
    @NotNull
    public static final BoolValue boolean(@NotNull JSObject settingInfo) {
        Intrinsics.checkParameterIsNotNull((Object)settingInfo, (String)"settingInfo");
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
    @NotNull
    public static final IntegerValue integer(@NotNull JSObject settingInfo) {
        Intrinsics.checkParameterIsNotNull((Object)settingInfo, (String)"settingInfo");
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
    @NotNull
    public static final FloatValue float(@NotNull JSObject settingInfo) {
        Intrinsics.checkParameterIsNotNull((Object)settingInfo, (String)"settingInfo");
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
    @NotNull
    public static final TextValue text(@NotNull JSObject settingInfo) {
        Intrinsics.checkParameterIsNotNull((Object)settingInfo, (String)"settingInfo");
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
    @NotNull
    public static final BlockValue block(@NotNull JSObject settingInfo) {
        Intrinsics.checkParameterIsNotNull((Object)settingInfo, (String)"settingInfo");
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
    @NotNull
    public static final ListValue list(@NotNull JSObject settingInfo) {
        Intrinsics.checkParameterIsNotNull((Object)settingInfo, (String)"settingInfo");
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

