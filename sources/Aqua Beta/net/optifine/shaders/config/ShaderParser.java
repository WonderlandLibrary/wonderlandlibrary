package net.optifine.shaders.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.optifine.shaders.config.ShaderLine;

public class ShaderParser {
    public static Pattern PATTERN_UNIFORM = Pattern.compile((String)"\\s*uniform\\s+\\w+\\s+(\\w+).*");
    public static Pattern PATTERN_ATTRIBUTE = Pattern.compile((String)"\\s*attribute\\s+\\w+\\s+(\\w+).*");
    public static Pattern PATTERN_CONST_INT = Pattern.compile((String)"\\s*const\\s+int\\s+(\\w+)\\s*=\\s*([-+.\\w]+)\\s*;.*");
    public static Pattern PATTERN_CONST_FLOAT = Pattern.compile((String)"\\s*const\\s+float\\s+(\\w+)\\s*=\\s*([-+.\\w]+)\\s*;.*");
    public static Pattern PATTERN_CONST_VEC4 = Pattern.compile((String)"\\s*const\\s+vec4\\s+(\\w+)\\s*=\\s*(.+)\\s*;.*");
    public static Pattern PATTERN_CONST_BOOL = Pattern.compile((String)"\\s*const\\s+bool\\s+(\\w+)\\s*=\\s*(\\w+)\\s*;.*");
    public static Pattern PATTERN_PROPERTY = Pattern.compile((String)"\\s*(/\\*|//)?\\s*([A-Z]+):\\s*(\\w+)\\s*(\\*/.*|\\s*)");
    public static Pattern PATTERN_EXTENSION = Pattern.compile((String)"\\s*#\\s*extension\\s+(\\w+)\\s*:\\s*(\\w+).*");
    public static Pattern PATTERN_DEFERRED_FSH = Pattern.compile((String)".*deferred[0-9]*\\.fsh");
    public static Pattern PATTERN_COMPOSITE_FSH = Pattern.compile((String)".*composite[0-9]*\\.fsh");
    public static Pattern PATTERN_FINAL_FSH = Pattern.compile((String)".*final\\.fsh");
    public static Pattern PATTERN_DRAW_BUFFERS = Pattern.compile((String)"[0-7N]*");

    public static ShaderLine parseLine(String line) {
        Matcher matcher = PATTERN_UNIFORM.matcher((CharSequence)line);
        if (matcher.matches()) {
            return new ShaderLine(1, matcher.group(1), "", line);
        }
        Matcher matcher1 = PATTERN_ATTRIBUTE.matcher((CharSequence)line);
        if (matcher1.matches()) {
            return new ShaderLine(2, matcher1.group(1), "", line);
        }
        Matcher matcher2 = PATTERN_PROPERTY.matcher((CharSequence)line);
        if (matcher2.matches()) {
            return new ShaderLine(6, matcher2.group(2), matcher2.group(3), line);
        }
        Matcher matcher3 = PATTERN_CONST_INT.matcher((CharSequence)line);
        if (matcher3.matches()) {
            return new ShaderLine(3, matcher3.group(1), matcher3.group(2), line);
        }
        Matcher matcher4 = PATTERN_CONST_FLOAT.matcher((CharSequence)line);
        if (matcher4.matches()) {
            return new ShaderLine(4, matcher4.group(1), matcher4.group(2), line);
        }
        Matcher matcher5 = PATTERN_CONST_BOOL.matcher((CharSequence)line);
        if (matcher5.matches()) {
            return new ShaderLine(5, matcher5.group(1), matcher5.group(2), line);
        }
        Matcher matcher6 = PATTERN_EXTENSION.matcher((CharSequence)line);
        if (matcher6.matches()) {
            return new ShaderLine(7, matcher6.group(1), matcher6.group(2), line);
        }
        Matcher matcher7 = PATTERN_CONST_VEC4.matcher((CharSequence)line);
        return matcher7.matches() ? new ShaderLine(8, matcher7.group(1), matcher7.group(2), line) : null;
    }

    public static int getIndex(String uniform, String prefix, int minIndex, int maxIndex) {
        if (uniform.length() != prefix.length() + 1) {
            return -1;
        }
        if (!uniform.startsWith(prefix)) {
            return -1;
        }
        int i = uniform.charAt(prefix.length()) - 48;
        return i >= minIndex && i <= maxIndex ? i : -1;
    }

    public static int getShadowDepthIndex(String uniform) {
        return uniform.equals((Object)"shadow") ? 0 : (uniform.equals((Object)"watershadow") ? 1 : ShaderParser.getIndex(uniform, "shadowtex", 0, 1));
    }

    public static int getShadowColorIndex(String uniform) {
        return uniform.equals((Object)"shadowcolor") ? 0 : ShaderParser.getIndex(uniform, "shadowcolor", 0, 1);
    }

    public static int getDepthIndex(String uniform) {
        return ShaderParser.getIndex(uniform, "depthtex", 0, 2);
    }

    public static int getColorIndex(String uniform) {
        int i = ShaderParser.getIndex(uniform, "gaux", 1, 4);
        return i > 0 ? i + 3 : ShaderParser.getIndex(uniform, "colortex", 4, 7);
    }

    public static boolean isDeferred(String filename) {
        return PATTERN_DEFERRED_FSH.matcher((CharSequence)filename).matches();
    }

    public static boolean isComposite(String filename) {
        return PATTERN_COMPOSITE_FSH.matcher((CharSequence)filename).matches();
    }

    public static boolean isFinal(String filename) {
        return PATTERN_FINAL_FSH.matcher((CharSequence)filename).matches();
    }

    public static boolean isValidDrawBuffers(String str) {
        return PATTERN_DRAW_BUFFERS.matcher((CharSequence)str).matches();
    }
}
