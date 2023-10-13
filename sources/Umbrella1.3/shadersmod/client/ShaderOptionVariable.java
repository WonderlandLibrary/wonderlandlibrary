/*
 * Decompiled with CFR 0.150.
 */
package shadersmod.client;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import optifine.Config;
import optifine.StrUtils;
import shadersmod.client.ShaderOption;

public class ShaderOptionVariable
extends ShaderOption {
    private static final Pattern PATTERN_VARIABLE = Pattern.compile("^\\s*#define\\s+([A-Za-z0-9_]+)\\s+(-?[0-9\\.]*)F?f?\\s*(//.*)?$");

    public ShaderOptionVariable(String name, String description, String value, String[] values, String path) {
        super(name, description, value, values, value, path);
        this.setVisible(this.getValues().length > 1);
    }

    @Override
    public String getSourceLine() {
        return "#define " + this.getName() + " " + this.getValue() + " // Shader option " + this.getValue();
    }

    @Override
    public String getValueColor(String val) {
        return "\u00a7a";
    }

    @Override
    public boolean matchesLine(String line) {
        Matcher m = PATTERN_VARIABLE.matcher(line);
        if (!m.matches()) {
            return false;
        }
        String defName = m.group(1);
        return defName.matches(this.getName());
    }

    public static ShaderOption parseOption(String line, String path) {
        Matcher m = PATTERN_VARIABLE.matcher(line);
        if (!m.matches()) {
            return null;
        }
        String name = m.group(1);
        String value = m.group(2);
        String description = m.group(3);
        String vals = StrUtils.getSegment(description, "[", "]");
        if (vals != null && vals.length() > 0) {
            description = description.replace(vals, "").trim();
        }
        String[] values = ShaderOptionVariable.parseValues(value, vals);
        if (name != null && name.length() > 0) {
            path = StrUtils.removePrefix(path, "/shaders/");
            ShaderOptionVariable so = new ShaderOptionVariable(name, description, value, values, path);
            return so;
        }
        return null;
    }

    public static String[] parseValues(String value, String valuesStr) {
        String[] values = new String[]{value};
        if (valuesStr == null) {
            return values;
        }
        valuesStr = valuesStr.trim();
        valuesStr = StrUtils.removePrefix(valuesStr, "[");
        valuesStr = StrUtils.removeSuffix(valuesStr, "]");
        if ((valuesStr = valuesStr.trim()).length() <= 0) {
            return values;
        }
        Object[] parts = Config.tokenize(valuesStr, " ");
        if (parts.length <= 0) {
            return values;
        }
        if (!Arrays.asList(parts).contains(value)) {
            parts = (String[])Config.addObjectToArray(parts, value, 0);
        }
        return parts;
    }
}

