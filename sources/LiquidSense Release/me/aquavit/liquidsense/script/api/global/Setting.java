package me.aquavit.liquidsense.script.api.global;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import me.aquavit.liquidsense.value.*;

/**
 * Object used by the script API to provide an idiomatic way of creating module values.
 */
public class Setting {

    /**
     * Creates a boolean value.
     * @param settingInfo JavaScript object containing information about the value.
     * @return An instance of [BoolValue]
     */
    public static BoolValue boolean2(JSObject settingInfo) {
        String name = (String) settingInfo.getMember("name");
        boolean default2 = (boolean) settingInfo.getMember("default");

        return new BoolValue(name, default2);
    }

    /**
     * Creates an integer value.
     * @param settingInfo JavaScript object containing information about the value.
     * @return An instance of [IntegerValue]
     */
    public static IntegerValue integer(JSObject settingInfo) {
        String name = (String) settingInfo.getMember("name");
        int default2 = ((Number)settingInfo.getMember("default")).intValue();
        int min = ((Number)settingInfo.getMember("min")).intValue();
        int max = ((Number)settingInfo.getMember("max")).intValue();

        return new IntegerValue(name, default2, min, max);
    }

    /**
     * Creates a float value.
     * @param settingInfo JavaScript object containing information about the value.
     * @return An instance of [FloatValue]
     */
    public static FloatValue float2(JSObject settingInfo) {
        String name = (String) settingInfo.getMember("name");
        float default2 = ((Number)settingInfo.getMember("default")).floatValue();
        float min = ((Number)settingInfo.getMember("min")).floatValue();
        float max = ((Number)settingInfo.getMember("max")).floatValue();

        return new FloatValue(name, default2, min, max);
    }

    /**
     * Creates a text value.
     * @param settingInfo JavaScript object containing information about the value.
     * @return An instance of [TextValue]
     */
    public static TextValue text(JSObject settingInfo) {
        String name = (String) settingInfo.getMember("name");
        String default2 = (String) settingInfo.getMember("default");

        return new TextValue(name, default2);
    }

    /**
     * Creates a block value.
     * @param settingInfo JavaScript object containing information about the value.
     * @return An instance of [BlockValue]
     */
    public static BlockValue block(JSObject settingInfo) {
        String name = (String) settingInfo.getMember("name");
        int default2 = ((Number)settingInfo.getMember("default")).intValue();

        return new BlockValue(name, default2);
    }

    /**
     * Creates a list value.
     * @param settingInfo JavaScript object containing information about the value.
     * @return An instance of [ListValue]
     */
    public static ListValue list(JSObject settingInfo) {
        String name = (String) settingInfo.getMember("name");
        String[] values = (String[]) ScriptUtils.convert(settingInfo.getMember("values"), String[].class);
        String default2 = (String) settingInfo.getMember("default");

        return new ListValue(name, values, default2);
    }
}
