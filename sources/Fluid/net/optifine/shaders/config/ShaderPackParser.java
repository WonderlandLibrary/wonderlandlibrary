// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.shaders.config;

import java.util.Hashtable;
import java.util.Collections;
import net.optifine.render.GlBlendState;
import net.optifine.render.GlAlphaState;
import net.optifine.shaders.Program;
import net.optifine.expr.ExpressionFloatArrayCached;
import net.optifine.expr.IExpressionFloatArray;
import net.optifine.expr.ExpressionFloatCached;
import net.optifine.expr.IExpressionFloat;
import net.optifine.expr.ExpressionType;
import net.optifine.shaders.uniform.ShaderExpressionResolver;
import net.optifine.shaders.uniform.UniformType;
import net.optifine.shaders.uniform.CustomUniform;
import net.optifine.expr.IExpression;
import net.optifine.shaders.uniform.CustomUniforms;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.CharArrayReader;
import java.util.LinkedHashSet;
import java.io.CharArrayWriter;
import java.io.BufferedReader;
import net.optifine.util.StrUtils;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShaderUtils;
import net.optifine.expr.ParseException;
import net.optifine.expr.IExpressionResolver;
import net.optifine.expr.ExpressionParser;
import java.util.regex.Matcher;
import net.optifine.shaders.SMCLog;
import net.optifine.expr.IExpressionBool;
import java.util.Properties;
import java.util.HashSet;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import net.minecraft.src.Config;
import java.util.Collection;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import net.optifine.shaders.IShaderPack;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ShaderPackParser
{
    private static final Pattern PATTERN_VERSION;
    private static final Pattern PATTERN_INCLUDE;
    private static final Set<String> setConstNames;
    private static final Map<String, Integer> mapAlphaFuncs;
    private static final Map<String, Integer> mapBlendFactors;
    
    static {
        PATTERN_VERSION = Pattern.compile("^\\s*#version\\s+.*$");
        PATTERN_INCLUDE = Pattern.compile("^\\s*#include\\s+\"([A-Za-z0-9_/\\.]+)\".*$");
        setConstNames = makeSetConstNames();
        mapAlphaFuncs = makeMapAlphaFuncs();
        mapBlendFactors = makeMapBlendFactors();
    }
    
    public static ShaderOption[] parseShaderPackOptions(final IShaderPack shaderPack, final String[] programNames, final List<Integer> listDimensions) {
        if (shaderPack == null) {
            return new ShaderOption[0];
        }
        final Map<String, ShaderOption> map = new HashMap<String, ShaderOption>();
        collectShaderOptions(shaderPack, "/shaders", programNames, map);
        for (final int i : listDimensions) {
            final String s = "/shaders/world" + i;
            collectShaderOptions(shaderPack, s, programNames, map);
        }
        final Collection<ShaderOption> collection = map.values();
        final ShaderOption[] ashaderoption = collection.toArray(new ShaderOption[collection.size()]);
        final Comparator<ShaderOption> comparator = new Comparator<ShaderOption>() {
            @Override
            public int compare(final ShaderOption o1, final ShaderOption o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        };
        Arrays.sort(ashaderoption, comparator);
        return ashaderoption;
    }
    
    private static void collectShaderOptions(final IShaderPack shaderPack, final String dir, final String[] programNames, final Map<String, ShaderOption> mapOptions) {
        for (int i = 0; i < programNames.length; ++i) {
            final String s = programNames[i];
            if (!s.equals("")) {
                final String s2 = String.valueOf(dir) + "/" + s + ".vsh";
                final String s3 = String.valueOf(dir) + "/" + s + ".fsh";
                collectShaderOptions(shaderPack, s2, mapOptions);
                collectShaderOptions(shaderPack, s3, mapOptions);
            }
        }
    }
    
    private static void collectShaderOptions(final IShaderPack sp, final String path, final Map<String, ShaderOption> mapOptions) {
        final String[] astring = getLines(sp, path);
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final ShaderOption shaderoption = getShaderOption(s, path);
            if (shaderoption != null && !shaderoption.getName().startsWith(ShaderMacros.getPrefixMacro()) && (!shaderoption.checkUsed() || isOptionUsed(shaderoption, astring))) {
                final String s2 = shaderoption.getName();
                final ShaderOption shaderoption2 = mapOptions.get(s2);
                if (shaderoption2 != null) {
                    if (!Config.equals(shaderoption2.getValueDefault(), shaderoption.getValueDefault())) {
                        Config.warn("Ambiguous shader option: " + shaderoption.getName());
                        Config.warn(" - in " + Config.arrayToString(shaderoption2.getPaths()) + ": " + shaderoption2.getValueDefault());
                        Config.warn(" - in " + Config.arrayToString(shaderoption.getPaths()) + ": " + shaderoption.getValueDefault());
                        shaderoption2.setEnabled(false);
                    }
                    if (shaderoption2.getDescription() == null || shaderoption2.getDescription().length() <= 0) {
                        shaderoption2.setDescription(shaderoption.getDescription());
                    }
                    shaderoption2.addPaths(shaderoption.getPaths());
                }
                else {
                    mapOptions.put(s2, shaderoption);
                }
            }
        }
    }
    
    private static boolean isOptionUsed(final ShaderOption so, final String[] lines) {
        for (int i = 0; i < lines.length; ++i) {
            final String s = lines[i];
            if (so.isUsedInLine(s)) {
                return true;
            }
        }
        return false;
    }
    
    private static String[] getLines(final IShaderPack sp, final String path) {
        try {
            final List<String> list = new ArrayList<String>();
            final String s = loadFile(path, sp, 0, list, 0);
            if (s == null) {
                return new String[0];
            }
            final ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(s.getBytes());
            final String[] astring = Config.readLines(bytearrayinputstream);
            return astring;
        }
        catch (final IOException ioexception) {
            Config.dbg(String.valueOf(ioexception.getClass().getName()) + ": " + ioexception.getMessage());
            return new String[0];
        }
    }
    
    private static ShaderOption getShaderOption(final String line, final String path) {
        ShaderOption shaderoption = null;
        if (shaderoption == null) {
            shaderoption = ShaderOptionSwitch.parseOption(line, path);
        }
        if (shaderoption == null) {
            shaderoption = ShaderOptionVariable.parseOption(line, path);
        }
        if (shaderoption != null) {
            return shaderoption;
        }
        if (shaderoption == null) {
            shaderoption = ShaderOptionSwitchConst.parseOption(line, path);
        }
        if (shaderoption == null) {
            shaderoption = ShaderOptionVariableConst.parseOption(line, path);
        }
        return (shaderoption != null && ShaderPackParser.setConstNames.contains(shaderoption.getName())) ? shaderoption : null;
    }
    
    private static Set<String> makeSetConstNames() {
        final Set<String> set = new HashSet<String>();
        set.add("shadowMapResolution");
        set.add("shadowMapFov");
        set.add("shadowDistance");
        set.add("shadowDistanceRenderMul");
        set.add("shadowIntervalSize");
        set.add("generateShadowMipmap");
        set.add("generateShadowColorMipmap");
        set.add("shadowHardwareFiltering");
        set.add("shadowHardwareFiltering0");
        set.add("shadowHardwareFiltering1");
        set.add("shadowtex0Mipmap");
        set.add("shadowtexMipmap");
        set.add("shadowtex1Mipmap");
        set.add("shadowcolor0Mipmap");
        set.add("shadowColor0Mipmap");
        set.add("shadowcolor1Mipmap");
        set.add("shadowColor1Mipmap");
        set.add("shadowtex0Nearest");
        set.add("shadowtexNearest");
        set.add("shadow0MinMagNearest");
        set.add("shadowtex1Nearest");
        set.add("shadow1MinMagNearest");
        set.add("shadowcolor0Nearest");
        set.add("shadowColor0Nearest");
        set.add("shadowColor0MinMagNearest");
        set.add("shadowcolor1Nearest");
        set.add("shadowColor1Nearest");
        set.add("shadowColor1MinMagNearest");
        set.add("wetnessHalflife");
        set.add("drynessHalflife");
        set.add("eyeBrightnessHalflife");
        set.add("centerDepthHalflife");
        set.add("sunPathRotation");
        set.add("ambientOcclusionLevel");
        set.add("superSamplingLevel");
        set.add("noiseTextureResolution");
        return set;
    }
    
    public static ShaderProfile[] parseProfiles(final Properties props, final ShaderOption[] shaderOptions) {
        final String s = "profile.";
        final List<ShaderProfile> list = new ArrayList<ShaderProfile>();
        for (final Object o : ((Hashtable<Object, V>)props).keySet()) {
            final String s2 = (String)o;
            if (s2.startsWith(s)) {
                final String s3 = s2.substring(s.length());
                props.getProperty(s2);
                final Set<String> set = new HashSet<String>();
                final ShaderProfile shaderprofile = parseProfile(s3, props, set, shaderOptions);
                if (shaderprofile == null) {
                    continue;
                }
                list.add(shaderprofile);
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        final ShaderProfile[] ashaderprofile = list.toArray(new ShaderProfile[list.size()]);
        return ashaderprofile;
    }
    
    public static Map<String, IExpressionBool> parseProgramConditions(final Properties props, final ShaderOption[] shaderOptions) {
        final String s = "program.";
        final Pattern pattern = Pattern.compile("program\\.([^.]+)\\.enabled");
        final Map<String, IExpressionBool> map = new HashMap<String, IExpressionBool>();
        for (final Object o : ((Hashtable<Object, V>)props).keySet()) {
            final String s2 = (String)o;
            final Matcher matcher = pattern.matcher(s2);
            if (matcher.matches()) {
                final String s3 = matcher.group(1);
                final String s4 = props.getProperty(s2).trim();
                final IExpressionBool iexpressionbool = parseOptionExpression(s4, shaderOptions);
                if (iexpressionbool == null) {
                    SMCLog.severe("Error parsing program condition: " + s2);
                }
                else {
                    map.put(s3, iexpressionbool);
                }
            }
        }
        return map;
    }
    
    private static IExpressionBool parseOptionExpression(final String val, final ShaderOption[] shaderOptions) {
        try {
            final ShaderOptionResolver shaderoptionresolver = new ShaderOptionResolver(shaderOptions);
            final ExpressionParser expressionparser = new ExpressionParser(shaderoptionresolver);
            final IExpressionBool iexpressionbool = expressionparser.parseBool(val);
            return iexpressionbool;
        }
        catch (final ParseException parseexception) {
            SMCLog.warning(String.valueOf(parseexception.getClass().getName()) + ": " + parseexception.getMessage());
            return null;
        }
    }
    
    public static Set<String> parseOptionSliders(final Properties props, final ShaderOption[] shaderOptions) {
        final Set<String> set = new HashSet<String>();
        final String s = props.getProperty("sliders");
        if (s == null) {
            return set;
        }
        final String[] astring = Config.tokenize(s, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s2 = astring[i];
            final ShaderOption shaderoption = ShaderUtils.getShaderOption(s2, shaderOptions);
            if (shaderoption == null) {
                Config.warn("Invalid shader option: " + s2);
            }
            else {
                set.add(s2);
            }
        }
        return set;
    }
    
    private static ShaderProfile parseProfile(final String name, final Properties props, final Set<String> parsedProfiles, final ShaderOption[] shaderOptions) {
        final String s = "profile.";
        final String s2 = String.valueOf(s) + name;
        if (parsedProfiles.contains(s2)) {
            Config.warn("[Shaders] Profile already parsed: " + name);
            return null;
        }
        parsedProfiles.add(name);
        final ShaderProfile shaderprofile = new ShaderProfile(name);
        final String s3 = props.getProperty(s2);
        final String[] astring = Config.tokenize(s3, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s4 = astring[i];
            if (s4.startsWith(s)) {
                final String s5 = s4.substring(s.length());
                final ShaderProfile shaderprofile2 = parseProfile(s5, props, parsedProfiles, shaderOptions);
                if (shaderprofile != null) {
                    shaderprofile.addOptionValues(shaderprofile2);
                    shaderprofile.addDisabledPrograms(shaderprofile2.getDisabledPrograms());
                }
            }
            else {
                final String[] astring2 = Config.tokenize(s4, ":=");
                if (astring2.length == 1) {
                    String s6 = astring2[0];
                    boolean flag = true;
                    if (s6.startsWith("!")) {
                        flag = false;
                        s6 = s6.substring(1);
                    }
                    final String s7 = "program.";
                    if (s6.startsWith(s7)) {
                        final String s8 = s6.substring(s7.length());
                        if (!Shaders.isProgramPath(s8)) {
                            Config.warn("Invalid program: " + s8 + " in profile: " + shaderprofile.getName());
                        }
                        else if (flag) {
                            shaderprofile.removeDisabledProgram(s8);
                        }
                        else {
                            shaderprofile.addDisabledProgram(s8);
                        }
                    }
                    else {
                        final ShaderOption shaderoption1 = ShaderUtils.getShaderOption(s6, shaderOptions);
                        if (!(shaderoption1 instanceof ShaderOptionSwitch)) {
                            Config.warn("[Shaders] Invalid option: " + s6);
                        }
                        else {
                            shaderprofile.addOptionValue(s6, String.valueOf(flag));
                            shaderoption1.setVisible(true);
                        }
                    }
                }
                else if (astring2.length != 2) {
                    Config.warn("[Shaders] Invalid option value: " + s4);
                }
                else {
                    final String s9 = astring2[0];
                    final String s10 = astring2[1];
                    final ShaderOption shaderoption2 = ShaderUtils.getShaderOption(s9, shaderOptions);
                    if (shaderoption2 == null) {
                        Config.warn("[Shaders] Invalid option: " + s4);
                    }
                    else if (!shaderoption2.isValidValue(s10)) {
                        Config.warn("[Shaders] Invalid value: " + s4);
                    }
                    else {
                        shaderoption2.setVisible(true);
                        shaderprofile.addOptionValue(s9, s10);
                    }
                }
            }
        }
        return shaderprofile;
    }
    
    public static Map<String, ScreenShaderOptions> parseGuiScreens(final Properties props, final ShaderProfile[] shaderProfiles, final ShaderOption[] shaderOptions) {
        final Map<String, ScreenShaderOptions> map = new HashMap<String, ScreenShaderOptions>();
        parseGuiScreen("screen", props, map, shaderProfiles, shaderOptions);
        return map.isEmpty() ? null : map;
    }
    
    private static boolean parseGuiScreen(final String key, final Properties props, final Map<String, ScreenShaderOptions> map, final ShaderProfile[] shaderProfiles, final ShaderOption[] shaderOptions) {
        final String s = props.getProperty(key);
        if (s == null) {
            return false;
        }
        final List<ShaderOption> list = new ArrayList<ShaderOption>();
        final Set<String> set = new HashSet<String>();
        final String[] astring = Config.tokenize(s, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s2 = astring[i];
            if (s2.equals("<empty>")) {
                list.add(null);
            }
            else if (set.contains(s2)) {
                Config.warn("[Shaders] Duplicate option: " + s2 + ", key: " + key);
            }
            else {
                set.add(s2);
                if (s2.equals("<profile>")) {
                    if (shaderProfiles == null) {
                        Config.warn("[Shaders] Option profile can not be used, no profiles defined: " + s2 + ", key: " + key);
                    }
                    else {
                        final ShaderOptionProfile shaderoptionprofile = new ShaderOptionProfile(shaderProfiles, shaderOptions);
                        list.add(shaderoptionprofile);
                    }
                }
                else if (s2.equals("*")) {
                    final ShaderOption shaderoption1 = new ShaderOptionRest("<rest>");
                    list.add(shaderoption1);
                }
                else if (s2.startsWith("[") && s2.endsWith("]")) {
                    final String s3 = StrUtils.removePrefixSuffix(s2, "[", "]");
                    if (!s3.matches("^[a-zA-Z0-9_]+$")) {
                        Config.warn("[Shaders] Invalid screen: " + s2 + ", key: " + key);
                    }
                    else if (!parseGuiScreen("screen." + s3, props, map, shaderProfiles, shaderOptions)) {
                        Config.warn("[Shaders] Invalid screen: " + s2 + ", key: " + key);
                    }
                    else {
                        final ShaderOptionScreen shaderoptionscreen = new ShaderOptionScreen(s3);
                        list.add(shaderoptionscreen);
                    }
                }
                else {
                    final ShaderOption shaderoption2 = ShaderUtils.getShaderOption(s2, shaderOptions);
                    if (shaderoption2 == null) {
                        Config.warn("[Shaders] Invalid option: " + s2 + ", key: " + key);
                        list.add(null);
                    }
                    else {
                        shaderoption2.setVisible(true);
                        list.add(shaderoption2);
                    }
                }
            }
        }
        final ShaderOption[] ashaderoption = list.toArray(new ShaderOption[list.size()]);
        final String s4 = props.getProperty(String.valueOf(key) + ".columns");
        final int j = Config.parseInt(s4, 2);
        final ScreenShaderOptions screenshaderoptions = new ScreenShaderOptions(key, ashaderoption, j);
        map.put(key, screenshaderoptions);
        return true;
    }
    
    public static BufferedReader resolveIncludes(final BufferedReader reader, final String filePath, final IShaderPack shaderPack, final int fileIndex, final List<String> listFiles, final int includeLevel) throws IOException {
        String s = "/";
        final int i = filePath.lastIndexOf("/");
        if (i >= 0) {
            s = filePath.substring(0, i);
        }
        final CharArrayWriter chararraywriter = new CharArrayWriter();
        int j = -1;
        final Set<ShaderMacro> set = new LinkedHashSet<ShaderMacro>();
        int k = 1;
        while (true) {
            String s2 = reader.readLine();
            if (s2 == null) {
                char[] achar = chararraywriter.toCharArray();
                if (j >= 0 && set.size() > 0) {
                    final StringBuilder stringbuilder = new StringBuilder();
                    for (final ShaderMacro shadermacro : set) {
                        stringbuilder.append("#define ");
                        stringbuilder.append(shadermacro.getName());
                        stringbuilder.append(" ");
                        stringbuilder.append(shadermacro.getValue());
                        stringbuilder.append("\n");
                    }
                    final String s3 = stringbuilder.toString();
                    final StringBuilder stringbuilder2 = new StringBuilder(new String(achar));
                    stringbuilder2.insert(j, s3);
                    final String s4 = stringbuilder2.toString();
                    achar = s4.toCharArray();
                }
                final CharArrayReader chararrayreader = new CharArrayReader(achar);
                return new BufferedReader(chararrayreader);
            }
            if (j < 0) {
                final Matcher matcher = ShaderPackParser.PATTERN_VERSION.matcher(s2);
                if (matcher.matches()) {
                    final String s5 = String.valueOf(ShaderMacros.getFixedMacroLines()) + ShaderMacros.getOptionMacroLines();
                    final String s6 = String.valueOf(s2) + "\n" + s5;
                    final String s7 = "#line " + (k + 1) + " " + fileIndex;
                    s2 = String.valueOf(s6) + s7;
                    j = chararraywriter.size() + s6.length();
                }
            }
            final Matcher matcher2 = ShaderPackParser.PATTERN_INCLUDE.matcher(s2);
            if (matcher2.matches()) {
                final String s8 = matcher2.group(1);
                final boolean flag = s8.startsWith("/");
                final String s9 = flag ? ("/shaders" + s8) : (String.valueOf(s) + "/" + s8);
                if (!listFiles.contains(s9)) {
                    listFiles.add(s9);
                }
                final int l = listFiles.indexOf(s9) + 1;
                s2 = loadFile(s9, shaderPack, l, listFiles, includeLevel);
                if (s2 == null) {
                    throw new IOException("Included file not found: " + filePath);
                }
                if (s2.endsWith("\n")) {
                    s2 = s2.substring(0, s2.length() - 1);
                }
                String s10 = "#line 1 " + l + "\n";
                if (s2.startsWith("#version ")) {
                    s10 = "";
                }
                s2 = String.valueOf(s10) + s2 + "\n" + "#line " + (k + 1) + " " + fileIndex;
            }
            if (j >= 0 && s2.contains(ShaderMacros.getPrefixMacro())) {
                final ShaderMacro[] ashadermacro = findMacros(s2, ShaderMacros.getExtensions());
                for (int i2 = 0; i2 < ashadermacro.length; ++i2) {
                    final ShaderMacro shadermacro2 = ashadermacro[i2];
                    set.add(shadermacro2);
                }
            }
            chararraywriter.write(s2);
            chararraywriter.write("\n");
            ++k;
        }
    }
    
    private static ShaderMacro[] findMacros(final String line, final ShaderMacro[] macros) {
        final List<ShaderMacro> list = new ArrayList<ShaderMacro>();
        for (int i = 0; i < macros.length; ++i) {
            final ShaderMacro shadermacro = macros[i];
            if (line.contains(shadermacro.getName())) {
                list.add(shadermacro);
            }
        }
        final ShaderMacro[] ashadermacro = list.toArray(new ShaderMacro[list.size()]);
        return ashadermacro;
    }
    
    private static String loadFile(final String filePath, final IShaderPack shaderPack, final int fileIndex, final List<String> listFiles, int includeLevel) throws IOException {
        if (includeLevel >= 10) {
            throw new IOException("#include depth exceeded: " + includeLevel + ", file: " + filePath);
        }
        ++includeLevel;
        final InputStream inputstream = shaderPack.getResourceAsStream(filePath);
        if (inputstream == null) {
            return null;
        }
        final InputStreamReader inputstreamreader = new InputStreamReader(inputstream, "ASCII");
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        bufferedreader = resolveIncludes(bufferedreader, filePath, shaderPack, fileIndex, listFiles, includeLevel);
        final CharArrayWriter chararraywriter = new CharArrayWriter();
        while (true) {
            final String s = bufferedreader.readLine();
            if (s == null) {
                break;
            }
            chararraywriter.write(s);
            chararraywriter.write("\n");
        }
        return chararraywriter.toString();
    }
    
    public static CustomUniforms parseCustomUniforms(final Properties props) {
        final String s = "uniform";
        final String s2 = "variable";
        final String s3 = String.valueOf(s) + ".";
        final String s4 = String.valueOf(s2) + ".";
        final Map<String, IExpression> map = new HashMap<String, IExpression>();
        final List<CustomUniform> list = new ArrayList<CustomUniform>();
        for (final Object o : ((Hashtable<Object, V>)props).keySet()) {
            final String s5 = (String)o;
            final String[] astring = Config.tokenize(s5, ".");
            if (astring.length == 3) {
                final String s6 = astring[0];
                final String s7 = astring[1];
                final String s8 = astring[2];
                final String s9 = props.getProperty(s5).trim();
                if (map.containsKey(s8)) {
                    SMCLog.warning("Expression already defined: " + s8);
                }
                else {
                    if (!s6.equals(s) && !s6.equals(s2)) {
                        continue;
                    }
                    SMCLog.info("Custom " + s6 + ": " + s8);
                    final CustomUniform customuniform = parseCustomUniform(s6, s8, s7, s9, map);
                    if (customuniform == null) {
                        continue;
                    }
                    map.put(s8, customuniform.getExpression());
                    if (s6.equals(s2)) {
                        continue;
                    }
                    list.add(customuniform);
                }
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        final CustomUniform[] acustomuniform = list.toArray(new CustomUniform[list.size()]);
        final CustomUniforms customuniforms = new CustomUniforms(acustomuniform, map);
        return customuniforms;
    }
    
    private static CustomUniform parseCustomUniform(final String kind, final String name, final String type, final String src, final Map<String, IExpression> mapExpressions) {
        try {
            final UniformType uniformtype = UniformType.parse(type);
            if (uniformtype == null) {
                SMCLog.warning("Unknown " + kind + " type: " + uniformtype);
                return null;
            }
            final ShaderExpressionResolver shaderexpressionresolver = new ShaderExpressionResolver(mapExpressions);
            final ExpressionParser expressionparser = new ExpressionParser(shaderexpressionresolver);
            IExpression iexpression = expressionparser.parse(src);
            final ExpressionType expressiontype = iexpression.getExpressionType();
            if (!uniformtype.matchesExpressionType(expressiontype)) {
                SMCLog.warning("Expression type does not match " + kind + " type, expression: " + expressiontype + ", " + kind + ": " + uniformtype + " " + name);
                return null;
            }
            iexpression = makeExpressionCached(iexpression);
            final CustomUniform customuniform = new CustomUniform(name, uniformtype, iexpression);
            return customuniform;
        }
        catch (final ParseException parseexception) {
            SMCLog.warning(String.valueOf(parseexception.getClass().getName()) + ": " + parseexception.getMessage());
            return null;
        }
    }
    
    private static IExpression makeExpressionCached(final IExpression expr) {
        return (expr instanceof IExpressionFloat) ? new ExpressionFloatCached((IExpressionFloat)expr) : ((expr instanceof IExpressionFloatArray) ? new ExpressionFloatArrayCached((IExpressionFloatArray)expr) : expr);
    }
    
    public static void parseAlphaStates(final Properties props) {
        for (final Object o : ((Hashtable<Object, V>)props).keySet()) {
            final String s = (String)o;
            final String[] astring = Config.tokenize(s, ".");
            if (astring.length == 2) {
                final String s2 = astring[0];
                final String s3 = astring[1];
                if (!s2.equals("alphaTest")) {
                    continue;
                }
                final Program program = Shaders.getProgram(s3);
                if (program == null) {
                    SMCLog.severe("Invalid program name: " + s3);
                }
                else {
                    final String s4 = props.getProperty(s).trim();
                    final GlAlphaState glalphastate = parseAlphaState(s4);
                    if (glalphastate == null) {
                        continue;
                    }
                    program.setAlphaState(glalphastate);
                }
            }
        }
    }
    
    private static GlAlphaState parseAlphaState(final String str) {
        final String[] astring = Config.tokenize(str, " ");
        if (astring.length == 1) {
            final String s = astring[0];
            if (s.equals("off") || s.equals("false")) {
                return new GlAlphaState(false);
            }
        }
        else if (astring.length == 2) {
            final String s2 = astring[0];
            final String s3 = astring[1];
            final Integer integer = ShaderPackParser.mapAlphaFuncs.get(s2);
            final float f = Config.parseFloat(s3, -1.0f);
            if (integer != null && f >= 0.0f) {
                return new GlAlphaState(true, integer, f);
            }
        }
        SMCLog.severe("Invalid alpha test: " + str);
        return null;
    }
    
    public static void parseBlendStates(final Properties props) {
        for (final Object o : ((Hashtable<Object, V>)props).keySet()) {
            final String s = (String)o;
            final String[] astring = Config.tokenize(s, ".");
            if (astring.length == 2) {
                final String s2 = astring[0];
                final String s3 = astring[1];
                if (!s2.equals("blend")) {
                    continue;
                }
                final Program program = Shaders.getProgram(s3);
                if (program == null) {
                    SMCLog.severe("Invalid program name: " + s3);
                }
                else {
                    final String s4 = props.getProperty(s).trim();
                    final GlBlendState glblendstate = parseBlendState(s4);
                    if (glblendstate == null) {
                        continue;
                    }
                    program.setBlendState(glblendstate);
                }
            }
        }
    }
    
    private static GlBlendState parseBlendState(final String str) {
        final String[] astring = Config.tokenize(str, " ");
        if (astring.length == 1) {
            final String s = astring[0];
            if (s.equals("off") || s.equals("false")) {
                return new GlBlendState(false);
            }
        }
        else if (astring.length == 2 || astring.length == 4) {
            final String s2 = astring[0];
            final String s3 = astring[1];
            String s4 = s2;
            String s5 = s3;
            if (astring.length == 4) {
                s4 = astring[2];
                s5 = astring[3];
            }
            final Integer integer = ShaderPackParser.mapBlendFactors.get(s2);
            final Integer integer2 = ShaderPackParser.mapBlendFactors.get(s3);
            final Integer integer3 = ShaderPackParser.mapBlendFactors.get(s4);
            final Integer integer4 = ShaderPackParser.mapBlendFactors.get(s5);
            if (integer != null && integer2 != null && integer3 != null && integer4 != null) {
                return new GlBlendState(true, integer, integer2, integer3, integer4);
            }
        }
        SMCLog.severe("Invalid blend mode: " + str);
        return null;
    }
    
    public static void parseRenderScales(final Properties props) {
        for (final Object o : ((Hashtable<Object, V>)props).keySet()) {
            final String s = (String)o;
            final String[] astring = Config.tokenize(s, ".");
            if (astring.length == 2) {
                final String s2 = astring[0];
                final String s3 = astring[1];
                if (!s2.equals("scale")) {
                    continue;
                }
                final Program program = Shaders.getProgram(s3);
                if (program == null) {
                    SMCLog.severe("Invalid program name: " + s3);
                }
                else {
                    final String s4 = props.getProperty(s).trim();
                    final RenderScale renderscale = parseRenderScale(s4);
                    if (renderscale == null) {
                        continue;
                    }
                    program.setRenderScale(renderscale);
                }
            }
        }
    }
    
    private static RenderScale parseRenderScale(final String str) {
        final String[] astring = Config.tokenize(str, " ");
        final float f = Config.parseFloat(astring[0], -1.0f);
        float f2 = 0.0f;
        float f3 = 0.0f;
        if (astring.length > 1) {
            if (astring.length != 3) {
                SMCLog.severe("Invalid render scale: " + str);
                return null;
            }
            f2 = Config.parseFloat(astring[1], -1.0f);
            f3 = Config.parseFloat(astring[2], -1.0f);
        }
        if (Config.between(f, 0.0f, 1.0f) && Config.between(f2, 0.0f, 1.0f) && Config.between(f3, 0.0f, 1.0f)) {
            return new RenderScale(f, f2, f3);
        }
        SMCLog.severe("Invalid render scale: " + str);
        return null;
    }
    
    public static void parseBuffersFlip(final Properties props) {
        for (final Object o : ((Hashtable<Object, V>)props).keySet()) {
            final String s = (String)o;
            final String[] astring = Config.tokenize(s, ".");
            if (astring.length == 3) {
                final String s2 = astring[0];
                final String s3 = astring[1];
                final String s4 = astring[2];
                if (!s2.equals("flip")) {
                    continue;
                }
                final Program program = Shaders.getProgram(s3);
                if (program == null) {
                    SMCLog.severe("Invalid program name: " + s3);
                }
                else {
                    final Boolean[] aboolean = program.getBuffersFlip();
                    final int i = Shaders.getBufferIndexFromString(s4);
                    if (i >= 0 && i < aboolean.length) {
                        final String s5 = props.getProperty(s).trim();
                        final Boolean obool = Config.parseBoolean(s5, null);
                        if (obool == null) {
                            SMCLog.severe("Invalid boolean value: " + s5);
                        }
                        else {
                            aboolean[i] = obool;
                        }
                    }
                    else {
                        SMCLog.severe("Invalid buffer name: " + s4);
                    }
                }
            }
        }
    }
    
    private static Map<String, Integer> makeMapAlphaFuncs() {
        final Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("NEVER", new Integer(512));
        map.put("LESS", new Integer(513));
        map.put("EQUAL", new Integer(514));
        map.put("LEQUAL", new Integer(515));
        map.put("GREATER", new Integer(516));
        map.put("NOTEQUAL", new Integer(517));
        map.put("GEQUAL", new Integer(518));
        map.put("ALWAYS", new Integer(519));
        return Collections.unmodifiableMap((Map<? extends String, ? extends Integer>)map);
    }
    
    private static Map<String, Integer> makeMapBlendFactors() {
        final Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("ZERO", new Integer(0));
        map.put("ONE", new Integer(1));
        map.put("SRC_COLOR", new Integer(768));
        map.put("ONE_MINUS_SRC_COLOR", new Integer(769));
        map.put("DST_COLOR", new Integer(774));
        map.put("ONE_MINUS_DST_COLOR", new Integer(775));
        map.put("SRC_ALPHA", new Integer(770));
        map.put("ONE_MINUS_SRC_ALPHA", new Integer(771));
        map.put("DST_ALPHA", new Integer(772));
        map.put("ONE_MINUS_DST_ALPHA", new Integer(773));
        map.put("CONSTANT_COLOR", new Integer(32769));
        map.put("ONE_MINUS_CONSTANT_COLOR", new Integer(32770));
        map.put("CONSTANT_ALPHA", new Integer(32771));
        map.put("ONE_MINUS_CONSTANT_ALPHA", new Integer(32772));
        map.put("SRC_ALPHA_SATURATE", new Integer(776));
        return Collections.unmodifiableMap((Map<? extends String, ? extends Integer>)map);
    }
}
