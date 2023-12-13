/*
 * Decompiled with CFR 0.150.
 */
package shadersmod.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import optifine.StrUtils;
import shadersmod.client.ShaderOption;
import shadersmod.client.ShaderOptionSwitch;

public class ShaderOptionSwitchConst
extends ShaderOptionSwitch {
    private static final Pattern PATTERN_CONST = Pattern.compile("^\\s*const\\s*bool\\s*([A-Za-z0-9_]+)\\s*=\\s*(true|false)\\s*;\\s*(//.*)?$");

    public ShaderOptionSwitchConst(String name, String description, String value, String path) {
        super(name, description, value, path);
    }

    @Override
    public String getSourceLine() {
        return "const bool " + this.getName() + " = " + this.getValue() + "; // Shader option " + this.getValue();
    }

    public static ShaderOption parseOption(String line, String path) {
        Matcher m = PATTERN_CONST.matcher(line);
        if (!m.matches()) {
            return null;
        }
        String name = m.group(1);
        String value = m.group(2);
        String description = m.group(3);
        if (name != null && name.length() > 0) {
            path = StrUtils.removePrefix(path, "/shaders/");
            ShaderOptionSwitchConst so = new ShaderOptionSwitchConst(name, description, value, path);
            so.setVisible(false);
            return so;
        }
        return null;
    }

    @Override
    public boolean matchesLine(String line) {
        Matcher m = PATTERN_CONST.matcher(line);
        if (!m.matches()) {
            return false;
        }
        String defName = m.group(1);
        return defName.matches(this.getName());
    }

    @Override
    public boolean checkUsed() {
        return false;
    }
}

