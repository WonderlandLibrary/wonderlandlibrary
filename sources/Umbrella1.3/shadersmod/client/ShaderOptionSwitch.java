/*
 * Decompiled with CFR 0.150.
 */
package shadersmod.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import optifine.Config;
import optifine.Lang;
import optifine.StrUtils;
import shadersmod.client.ShaderOption;

public class ShaderOptionSwitch
extends ShaderOption {
    private static final Pattern PATTERN_DEFINE = Pattern.compile("^\\s*(//)?\\s*#define\\s+([A-Za-z0-9_]+)\\s*(//.*)?$");
    private static final Pattern PATTERN_IFDEF = Pattern.compile("^\\s*#if(n)?def\\s+([A-Za-z0-9_]+)(\\s*)?$");

    public ShaderOptionSwitch(String name, String description, String value, String path) {
        super(name, description, value, new String[]{"true", "false"}, value, path);
    }

    @Override
    public String getSourceLine() {
        return ShaderOptionSwitch.isTrue(this.getValue()) ? "#define " + this.getName() + " // Shader option ON" : "//#define " + this.getName() + " // Shader option OFF";
    }

    @Override
    public String getValueText(String val) {
        return ShaderOptionSwitch.isTrue(val) ? Lang.getOn() : Lang.getOff();
    }

    @Override
    public String getValueColor(String val) {
        return ShaderOptionSwitch.isTrue(val) ? "\u00a7a" : "\u00a7c";
    }

    public static ShaderOption parseOption(String line, String path) {
        Matcher m = PATTERN_DEFINE.matcher(line);
        if (!m.matches()) {
            return null;
        }
        String comment = m.group(1);
        String name = m.group(2);
        String description = m.group(3);
        if (name != null && name.length() > 0) {
            boolean commented = Config.equals(comment, "//");
            boolean enabled = !commented;
            path = StrUtils.removePrefix(path, "/shaders/");
            ShaderOptionSwitch so = new ShaderOptionSwitch(name, description, String.valueOf(enabled), path);
            return so;
        }
        return null;
    }

    @Override
    public boolean matchesLine(String line) {
        Matcher m = PATTERN_DEFINE.matcher(line);
        if (!m.matches()) {
            return false;
        }
        String defName = m.group(2);
        return defName.matches(this.getName());
    }

    @Override
    public boolean checkUsed() {
        return true;
    }

    @Override
    public boolean isUsedInLine(String line) {
        String name;
        Matcher mif = PATTERN_IFDEF.matcher(line);
        return mif.matches() && (name = mif.group(2)).equals(this.getName());
    }

    public static boolean isTrue(String val) {
        return Boolean.valueOf(val);
    }
}

