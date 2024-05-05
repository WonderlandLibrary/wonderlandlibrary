package net.optifine.shaders.config;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.src.Config;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.ShaderOption;
import net.optifine.util.StrUtils;

public class ShaderOptionVariable
extends ShaderOption {
    private static final Pattern PATTERN_VARIABLE = Pattern.compile((String)"^\\s*#define\\s+(\\w+)\\s+(-?[0-9\\.Ff]+|\\w+)\\s*(//.*)?$");

    public ShaderOptionVariable(String name, String description, String value, String[] values, String path) {
        super(name, description, value, values, value, path);
        this.setVisible(this.getValues().length > 1);
    }

    public String getSourceLine() {
        return "#define " + this.getName() + " " + this.getValue() + " // Shader option " + this.getValue();
    }

    public String getValueText(String val) {
        String s = Shaders.translate((String)("prefix." + this.getName()), (String)"");
        String s1 = super.getValueText(val);
        String s2 = Shaders.translate((String)("suffix." + this.getName()), (String)"");
        String s3 = s + s1 + s2;
        return s3;
    }

    public String getValueColor(String val) {
        String s = val.toLowerCase();
        return !s.equals((Object)"false") && !s.equals((Object)"off") ? "\u00a7a" : "\u00a7c";
    }

    public boolean matchesLine(String line) {
        Matcher matcher = PATTERN_VARIABLE.matcher((CharSequence)line);
        if (!matcher.matches()) {
            return false;
        }
        String s = matcher.group(1);
        return s.matches(this.getName());
    }

    public static ShaderOption parseOption(String line, String path) {
        Matcher matcher = PATTERN_VARIABLE.matcher((CharSequence)line);
        if (!matcher.matches()) {
            return null;
        }
        String s = matcher.group(1);
        String s1 = matcher.group(2);
        String s2 = matcher.group(3);
        String s3 = StrUtils.getSegment((String)s2, (String)"[", (String)"]");
        if (s3 != null && s3.length() > 0) {
            s2 = s2.replace((CharSequence)s3, (CharSequence)"").trim();
        }
        String[] astring = ShaderOptionVariable.parseValues(s1, s3);
        if (s != null && s.length() > 0) {
            path = StrUtils.removePrefix((String)path, (String)"/shaders/");
            ShaderOptionVariable shaderoption = new ShaderOptionVariable(s, s2, s1, astring, path);
            return shaderoption;
        }
        return null;
    }

    public static String[] parseValues(String value, String valuesStr) {
        String[] astring = new String[]{value};
        if (valuesStr == null) {
            return astring;
        }
        valuesStr = valuesStr.trim();
        valuesStr = StrUtils.removePrefix((String)valuesStr, (String)"[");
        valuesStr = StrUtils.removeSuffix((String)valuesStr, (String)"]");
        if ((valuesStr = valuesStr.trim()).length() <= 0) {
            return astring;
        }
        Object[] astring1 = Config.tokenize((String)valuesStr, (String)" ");
        if (astring1.length <= 0) {
            return astring;
        }
        if (!Arrays.asList((Object[])astring1).contains((Object)value)) {
            astring1 = (String[])Config.addObjectToArray((Object[])astring1, (Object)value, (int)0);
        }
        return astring1;
    }
}
