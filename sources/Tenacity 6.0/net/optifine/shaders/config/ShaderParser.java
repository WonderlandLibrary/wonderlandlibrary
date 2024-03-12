// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShaderParser
{
    public static Pattern PATTERN_UNIFORM;
    public static Pattern PATTERN_ATTRIBUTE;
    public static Pattern PATTERN_CONST_INT;
    public static Pattern PATTERN_CONST_FLOAT;
    public static Pattern PATTERN_CONST_VEC4;
    public static Pattern PATTERN_CONST_BOOL;
    public static Pattern PATTERN_PROPERTY;
    public static Pattern PATTERN_EXTENSION;
    public static Pattern PATTERN_DEFERRED_FSH;
    public static Pattern PATTERN_COMPOSITE_FSH;
    public static Pattern PATTERN_FINAL_FSH;
    public static Pattern PATTERN_DRAW_BUFFERS;
    
    public static ShaderLine parseLine(final String line) {
        final Matcher matcher = ShaderParser.PATTERN_UNIFORM.matcher(line);
        if (matcher.matches()) {
            return new ShaderLine(1, matcher.group(1), "", line);
        }
        final Matcher matcher2 = ShaderParser.PATTERN_ATTRIBUTE.matcher(line);
        if (matcher2.matches()) {
            return new ShaderLine(2, matcher2.group(1), "", line);
        }
        final Matcher matcher3 = ShaderParser.PATTERN_PROPERTY.matcher(line);
        if (matcher3.matches()) {
            return new ShaderLine(6, matcher3.group(2), matcher3.group(3), line);
        }
        final Matcher matcher4 = ShaderParser.PATTERN_CONST_INT.matcher(line);
        if (matcher4.matches()) {
            return new ShaderLine(3, matcher4.group(1), matcher4.group(2), line);
        }
        final Matcher matcher5 = ShaderParser.PATTERN_CONST_FLOAT.matcher(line);
        if (matcher5.matches()) {
            return new ShaderLine(4, matcher5.group(1), matcher5.group(2), line);
        }
        final Matcher matcher6 = ShaderParser.PATTERN_CONST_BOOL.matcher(line);
        if (matcher6.matches()) {
            return new ShaderLine(5, matcher6.group(1), matcher6.group(2), line);
        }
        final Matcher matcher7 = ShaderParser.PATTERN_EXTENSION.matcher(line);
        if (matcher7.matches()) {
            return new ShaderLine(7, matcher7.group(1), matcher7.group(2), line);
        }
        final Matcher matcher8 = ShaderParser.PATTERN_CONST_VEC4.matcher(line);
        return matcher8.matches() ? new ShaderLine(8, matcher8.group(1), matcher8.group(2), line) : null;
    }
    
    public static int getIndex(final String uniform, final String prefix, final int minIndex, final int maxIndex) {
        if (uniform.length() != prefix.length() + 1) {
            return -1;
        }
        if (!uniform.startsWith(prefix)) {
            return -1;
        }
        final int i = uniform.charAt(prefix.length()) - '0';
        return (i >= minIndex && i <= maxIndex) ? i : -1;
    }
    
    public static int getShadowDepthIndex(final String uniform) {
        return uniform.equals("shadow") ? 0 : (uniform.equals("watershadow") ? 1 : getIndex(uniform, "shadowtex", 0, 1));
    }
    
    public static int getShadowColorIndex(final String uniform) {
        return uniform.equals("shadowcolor") ? 0 : getIndex(uniform, "shadowcolor", 0, 1);
    }
    
    public static int getDepthIndex(final String uniform) {
        return getIndex(uniform, "depthtex", 0, 2);
    }
    
    public static int getColorIndex(final String uniform) {
        final int i = getIndex(uniform, "gaux", 1, 4);
        return (i > 0) ? (i + 3) : getIndex(uniform, "colortex", 4, 7);
    }
    
    public static boolean isDeferred(final String filename) {
        return ShaderParser.PATTERN_DEFERRED_FSH.matcher(filename).matches();
    }
    
    public static boolean isComposite(final String filename) {
        return ShaderParser.PATTERN_COMPOSITE_FSH.matcher(filename).matches();
    }
    
    public static boolean isFinal(final String filename) {
        return ShaderParser.PATTERN_FINAL_FSH.matcher(filename).matches();
    }
    
    public static boolean isValidDrawBuffers(final String str) {
        return ShaderParser.PATTERN_DRAW_BUFFERS.matcher(str).matches();
    }
    
    static {
        ShaderParser.PATTERN_UNIFORM = Pattern.compile("\\s*uniform\\s+\\w+\\s+(\\w+).*");
        ShaderParser.PATTERN_ATTRIBUTE = Pattern.compile("\\s*attribute\\s+\\w+\\s+(\\w+).*");
        ShaderParser.PATTERN_CONST_INT = Pattern.compile("\\s*const\\s+int\\s+(\\w+)\\s*=\\s*([-+.\\w]+)\\s*;.*");
        ShaderParser.PATTERN_CONST_FLOAT = Pattern.compile("\\s*const\\s+float\\s+(\\w+)\\s*=\\s*([-+.\\w]+)\\s*;.*");
        ShaderParser.PATTERN_CONST_VEC4 = Pattern.compile("\\s*const\\s+vec4\\s+(\\w+)\\s*=\\s*(.+)\\s*;.*");
        ShaderParser.PATTERN_CONST_BOOL = Pattern.compile("\\s*const\\s+bool\\s+(\\w+)\\s*=\\s*(\\w+)\\s*;.*");
        ShaderParser.PATTERN_PROPERTY = Pattern.compile("\\s*(/\\*|//)?\\s*([A-Z]+):\\s*(\\w+)\\s*(\\*/.*|\\s*)");
        ShaderParser.PATTERN_EXTENSION = Pattern.compile("\\s*#\\s*extension\\s+(\\w+)\\s*:\\s*(\\w+).*");
        ShaderParser.PATTERN_DEFERRED_FSH = Pattern.compile(".*deferred[0-9]*\\.fsh");
        ShaderParser.PATTERN_COMPOSITE_FSH = Pattern.compile(".*composite[0-9]*\\.fsh");
        ShaderParser.PATTERN_FINAL_FSH = Pattern.compile(".*final\\.fsh");
        ShaderParser.PATTERN_DRAW_BUFFERS = Pattern.compile("[0-7N]*");
    }
}
