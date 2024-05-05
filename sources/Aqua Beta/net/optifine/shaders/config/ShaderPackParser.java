package net.optifine.shaders.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.src.Config;
import net.optifine.expr.ExpressionFloatArrayCached;
import net.optifine.expr.ExpressionFloatCached;
import net.optifine.expr.ExpressionParser;
import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.IExpressionFloat;
import net.optifine.expr.IExpressionFloatArray;
import net.optifine.expr.IExpressionResolver;
import net.optifine.expr.ParseException;
import net.optifine.render.GlAlphaState;
import net.optifine.render.GlBlendState;
import net.optifine.shaders.IShaderPack;
import net.optifine.shaders.Program;
import net.optifine.shaders.SMCLog;
import net.optifine.shaders.ShaderUtils;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.RenderScale;
import net.optifine.shaders.config.ScreenShaderOptions;
import net.optifine.shaders.config.ShaderMacro;
import net.optifine.shaders.config.ShaderMacros;
import net.optifine.shaders.config.ShaderOption;
import net.optifine.shaders.config.ShaderOptionProfile;
import net.optifine.shaders.config.ShaderOptionResolver;
import net.optifine.shaders.config.ShaderOptionRest;
import net.optifine.shaders.config.ShaderOptionScreen;
import net.optifine.shaders.config.ShaderOptionSwitch;
import net.optifine.shaders.config.ShaderOptionSwitchConst;
import net.optifine.shaders.config.ShaderOptionVariable;
import net.optifine.shaders.config.ShaderOptionVariableConst;
import net.optifine.shaders.config.ShaderPackParser;
import net.optifine.shaders.config.ShaderProfile;
import net.optifine.shaders.uniform.CustomUniform;
import net.optifine.shaders.uniform.CustomUniforms;
import net.optifine.shaders.uniform.ShaderExpressionResolver;
import net.optifine.shaders.uniform.UniformType;
import net.optifine.util.StrUtils;

public class ShaderPackParser {
    private static final Pattern PATTERN_VERSION = Pattern.compile((String)"^\\s*#version\\s+.*$");
    private static final Pattern PATTERN_INCLUDE = Pattern.compile((String)"^\\s*#include\\s+\"([A-Za-z0-9_/\\.]+)\".*$");
    private static final Set<String> setConstNames = ShaderPackParser.makeSetConstNames();
    private static final Map<String, Integer> mapAlphaFuncs = ShaderPackParser.makeMapAlphaFuncs();
    private static final Map<String, Integer> mapBlendFactors = ShaderPackParser.makeMapBlendFactors();

    public static ShaderOption[] parseShaderPackOptions(IShaderPack shaderPack, String[] programNames, List<Integer> listDimensions) {
        if (shaderPack == null) {
            return new ShaderOption[0];
        }
        HashMap map = new HashMap();
        ShaderPackParser.collectShaderOptions(shaderPack, "/shaders", programNames, (Map<String, ShaderOption>)map);
        Iterator iterator = listDimensions.iterator();
        while (iterator.hasNext()) {
            int i = (Integer)iterator.next();
            String s = "/shaders/world" + i;
            ShaderPackParser.collectShaderOptions(shaderPack, s, programNames, (Map<String, ShaderOption>)map);
        }
        Collection collection = map.values();
        Object[] ashaderoption = (ShaderOption[])collection.toArray((Object[])new ShaderOption[collection.size()]);
        1 comparator = new /* Unavailable Anonymous Inner Class!! */;
        Arrays.sort((Object[])ashaderoption, (Comparator)comparator);
        return ashaderoption;
    }

    private static void collectShaderOptions(IShaderPack shaderPack, String dir, String[] programNames, Map<String, ShaderOption> mapOptions) {
        for (int i = 0; i < programNames.length; ++i) {
            String s = programNames[i];
            if (s.equals((Object)"")) continue;
            String s1 = dir + "/" + s + ".vsh";
            String s2 = dir + "/" + s + ".fsh";
            ShaderPackParser.collectShaderOptions(shaderPack, s1, mapOptions);
            ShaderPackParser.collectShaderOptions(shaderPack, s2, mapOptions);
        }
    }

    private static void collectShaderOptions(IShaderPack sp, String path, Map<String, ShaderOption> mapOptions) {
        String[] astring = ShaderPackParser.getLines(sp, path);
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            ShaderOption shaderoption = ShaderPackParser.getShaderOption(s, path);
            if (shaderoption == null || shaderoption.getName().startsWith(ShaderMacros.getPrefixMacro()) || shaderoption.checkUsed() && !ShaderPackParser.isOptionUsed(shaderoption, astring)) continue;
            String s1 = shaderoption.getName();
            ShaderOption shaderoption1 = (ShaderOption)mapOptions.get((Object)s1);
            if (shaderoption1 != null) {
                if (!Config.equals((Object)shaderoption1.getValueDefault(), (Object)shaderoption.getValueDefault())) {
                    Config.warn((String)("Ambiguous shader option: " + shaderoption.getName()));
                    Config.warn((String)(" - in " + Config.arrayToString((Object[])shaderoption1.getPaths()) + ": " + shaderoption1.getValueDefault()));
                    Config.warn((String)(" - in " + Config.arrayToString((Object[])shaderoption.getPaths()) + ": " + shaderoption.getValueDefault()));
                    shaderoption1.setEnabled(false);
                }
                if (shaderoption1.getDescription() == null || shaderoption1.getDescription().length() <= 0) {
                    shaderoption1.setDescription(shaderoption.getDescription());
                }
                shaderoption1.addPaths(shaderoption.getPaths());
                continue;
            }
            mapOptions.put((Object)s1, (Object)shaderoption);
        }
    }

    private static boolean isOptionUsed(ShaderOption so, String[] lines) {
        for (int i = 0; i < lines.length; ++i) {
            String s = lines[i];
            if (!so.isUsedInLine(s)) continue;
            return true;
        }
        return false;
    }

    private static String[] getLines(IShaderPack sp, String path) {
        try {
            ArrayList list = new ArrayList();
            String s = ShaderPackParser.loadFile(path, sp, 0, (List<String>)list, 0);
            if (s == null) {
                return new String[0];
            }
            ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(s.getBytes());
            String[] astring = Config.readLines((InputStream)bytearrayinputstream);
            return astring;
        }
        catch (IOException ioexception) {
            Config.dbg((String)(ioexception.getClass().getName() + ": " + ioexception.getMessage()));
            return new String[0];
        }
    }

    private static ShaderOption getShaderOption(String line, String path) {
        ShaderOption shaderoption = null;
        if (shaderoption == null) {
            shaderoption = ShaderOptionSwitch.parseOption((String)line, (String)path);
        }
        if (shaderoption == null) {
            shaderoption = ShaderOptionVariable.parseOption((String)line, (String)path);
        }
        if (shaderoption != null) {
            return shaderoption;
        }
        if (shaderoption == null) {
            shaderoption = ShaderOptionSwitchConst.parseOption((String)line, (String)path);
        }
        if (shaderoption == null) {
            shaderoption = ShaderOptionVariableConst.parseOption((String)line, (String)path);
        }
        return shaderoption != null && setConstNames.contains((Object)shaderoption.getName()) ? shaderoption : null;
    }

    private static Set<String> makeSetConstNames() {
        HashSet set = new HashSet();
        set.add((Object)"shadowMapResolution");
        set.add((Object)"shadowMapFov");
        set.add((Object)"shadowDistance");
        set.add((Object)"shadowDistanceRenderMul");
        set.add((Object)"shadowIntervalSize");
        set.add((Object)"generateShadowMipmap");
        set.add((Object)"generateShadowColorMipmap");
        set.add((Object)"shadowHardwareFiltering");
        set.add((Object)"shadowHardwareFiltering0");
        set.add((Object)"shadowHardwareFiltering1");
        set.add((Object)"shadowtex0Mipmap");
        set.add((Object)"shadowtexMipmap");
        set.add((Object)"shadowtex1Mipmap");
        set.add((Object)"shadowcolor0Mipmap");
        set.add((Object)"shadowColor0Mipmap");
        set.add((Object)"shadowcolor1Mipmap");
        set.add((Object)"shadowColor1Mipmap");
        set.add((Object)"shadowtex0Nearest");
        set.add((Object)"shadowtexNearest");
        set.add((Object)"shadow0MinMagNearest");
        set.add((Object)"shadowtex1Nearest");
        set.add((Object)"shadow1MinMagNearest");
        set.add((Object)"shadowcolor0Nearest");
        set.add((Object)"shadowColor0Nearest");
        set.add((Object)"shadowColor0MinMagNearest");
        set.add((Object)"shadowcolor1Nearest");
        set.add((Object)"shadowColor1Nearest");
        set.add((Object)"shadowColor1MinMagNearest");
        set.add((Object)"wetnessHalflife");
        set.add((Object)"drynessHalflife");
        set.add((Object)"eyeBrightnessHalflife");
        set.add((Object)"centerDepthHalflife");
        set.add((Object)"sunPathRotation");
        set.add((Object)"ambientOcclusionLevel");
        set.add((Object)"superSamplingLevel");
        set.add((Object)"noiseTextureResolution");
        return set;
    }

    public static ShaderProfile[] parseProfiles(Properties props, ShaderOption[] shaderOptions) {
        String s = "profile.";
        ArrayList list = new ArrayList();
        for (String s1 : props.keySet()) {
            if (!s1.startsWith(s)) continue;
            String s2 = s1.substring(s.length());
            props.getProperty(s1);
            HashSet set = new HashSet();
            ShaderProfile shaderprofile = ShaderPackParser.parseProfile(s2, props, (Set<String>)set, shaderOptions);
            if (shaderprofile == null) continue;
            list.add((Object)shaderprofile);
        }
        if (list.size() <= 0) {
            return null;
        }
        ShaderProfile[] ashaderprofile = (ShaderProfile[])list.toArray((Object[])new ShaderProfile[list.size()]);
        return ashaderprofile;
    }

    public static Map<String, IExpressionBool> parseProgramConditions(Properties props, ShaderOption[] shaderOptions) {
        String s = "program.";
        Pattern pattern = Pattern.compile((String)"program\\.([^.]+)\\.enabled");
        HashMap map = new HashMap();
        for (String s1 : props.keySet()) {
            Matcher matcher = pattern.matcher((CharSequence)s1);
            if (!matcher.matches()) continue;
            String s2 = matcher.group(1);
            String s3 = props.getProperty(s1).trim();
            IExpressionBool iexpressionbool = ShaderPackParser.parseOptionExpression(s3, shaderOptions);
            if (iexpressionbool == null) {
                SMCLog.severe((String)("Error parsing program condition: " + s1));
                continue;
            }
            map.put((Object)s2, (Object)iexpressionbool);
        }
        return map;
    }

    private static IExpressionBool parseOptionExpression(String val, ShaderOption[] shaderOptions) {
        try {
            ShaderOptionResolver shaderoptionresolver = new ShaderOptionResolver(shaderOptions);
            ExpressionParser expressionparser = new ExpressionParser((IExpressionResolver)shaderoptionresolver);
            IExpressionBool iexpressionbool = expressionparser.parseBool(val);
            return iexpressionbool;
        }
        catch (ParseException parseexception) {
            SMCLog.warning((String)(parseexception.getClass().getName() + ": " + parseexception.getMessage()));
            return null;
        }
    }

    public static Set<String> parseOptionSliders(Properties props, ShaderOption[] shaderOptions) {
        HashSet set = new HashSet();
        String s = props.getProperty("sliders");
        if (s == null) {
            return set;
        }
        String[] astring = Config.tokenize((String)s, (String)" ");
        for (int i = 0; i < astring.length; ++i) {
            String s1 = astring[i];
            ShaderOption shaderoption = ShaderUtils.getShaderOption((String)s1, (ShaderOption[])shaderOptions);
            if (shaderoption == null) {
                Config.warn((String)("Invalid shader option: " + s1));
                continue;
            }
            set.add((Object)s1);
        }
        return set;
    }

    private static ShaderProfile parseProfile(String name, Properties props, Set<String> parsedProfiles, ShaderOption[] shaderOptions) {
        String s = "profile.";
        String s1 = s + name;
        if (parsedProfiles.contains((Object)s1)) {
            Config.warn((String)("[Shaders] Profile already parsed: " + name));
            return null;
        }
        parsedProfiles.add((Object)name);
        ShaderProfile shaderprofile = new ShaderProfile(name);
        String s2 = props.getProperty(s1);
        String[] astring = Config.tokenize((String)s2, (String)" ");
        for (int i = 0; i < astring.length; ++i) {
            String s3 = astring[i];
            if (s3.startsWith(s)) {
                String s4 = s3.substring(s.length());
                ShaderProfile shaderprofile1 = ShaderPackParser.parseProfile(s4, props, parsedProfiles, shaderOptions);
                if (shaderprofile == null) continue;
                shaderprofile.addOptionValues(shaderprofile1);
                shaderprofile.addDisabledPrograms(shaderprofile1.getDisabledPrograms());
                continue;
            }
            String[] astring1 = Config.tokenize((String)s3, (String)":=");
            if (astring1.length == 1) {
                String s5;
                String s7 = astring1[0];
                boolean flag = true;
                if (s7.startsWith("!")) {
                    flag = false;
                    s7 = s7.substring(1);
                }
                if (s7.startsWith(s5 = "program.")) {
                    String s6 = s7.substring(s5.length());
                    if (!Shaders.isProgramPath((String)s6)) {
                        Config.warn((String)("Invalid program: " + s6 + " in profile: " + shaderprofile.getName()));
                        continue;
                    }
                    if (flag) {
                        shaderprofile.removeDisabledProgram(s6);
                        continue;
                    }
                    shaderprofile.addDisabledProgram(s6);
                    continue;
                }
                ShaderOption shaderoption1 = ShaderUtils.getShaderOption((String)s7, (ShaderOption[])shaderOptions);
                if (!(shaderoption1 instanceof ShaderOptionSwitch)) {
                    Config.warn((String)("[Shaders] Invalid option: " + s7));
                    continue;
                }
                shaderprofile.addOptionValue(s7, String.valueOf((boolean)flag));
                shaderoption1.setVisible(true);
                continue;
            }
            if (astring1.length != 2) {
                Config.warn((String)("[Shaders] Invalid option value: " + s3));
                continue;
            }
            String s8 = astring1[0];
            String s9 = astring1[1];
            ShaderOption shaderoption = ShaderUtils.getShaderOption((String)s8, (ShaderOption[])shaderOptions);
            if (shaderoption == null) {
                Config.warn((String)("[Shaders] Invalid option: " + s3));
                continue;
            }
            if (!shaderoption.isValidValue(s9)) {
                Config.warn((String)("[Shaders] Invalid value: " + s3));
                continue;
            }
            shaderoption.setVisible(true);
            shaderprofile.addOptionValue(s8, s9);
        }
        return shaderprofile;
    }

    public static Map<String, ScreenShaderOptions> parseGuiScreens(Properties props, ShaderProfile[] shaderProfiles, ShaderOption[] shaderOptions) {
        HashMap map = new HashMap();
        ShaderPackParser.parseGuiScreen("screen", props, (Map<String, ScreenShaderOptions>)map, shaderProfiles, shaderOptions);
        return map.isEmpty() ? null : map;
    }

    private static boolean parseGuiScreen(String key, Properties props, Map<String, ScreenShaderOptions> map, ShaderProfile[] shaderProfiles, ShaderOption[] shaderOptions) {
        String s = props.getProperty(key);
        if (s == null) {
            return false;
        }
        ArrayList list = new ArrayList();
        HashSet set = new HashSet();
        String[] astring = Config.tokenize((String)s, (String)" ");
        for (int i = 0; i < astring.length; ++i) {
            String s1 = astring[i];
            if (s1.equals((Object)"<empty>")) {
                list.add((Object)null);
                continue;
            }
            if (set.contains((Object)s1)) {
                Config.warn((String)("[Shaders] Duplicate option: " + s1 + ", key: " + key));
                continue;
            }
            set.add((Object)s1);
            if (s1.equals((Object)"<profile>")) {
                if (shaderProfiles == null) {
                    Config.warn((String)("[Shaders] Option profile can not be used, no profiles defined: " + s1 + ", key: " + key));
                    continue;
                }
                ShaderOptionProfile shaderoptionprofile = new ShaderOptionProfile(shaderProfiles, shaderOptions);
                list.add((Object)shaderoptionprofile);
                continue;
            }
            if (s1.equals((Object)"*")) {
                ShaderOptionRest shaderoption1 = new ShaderOptionRest("<rest>");
                list.add((Object)shaderoption1);
                continue;
            }
            if (s1.startsWith("[") && s1.endsWith("]")) {
                String s3 = StrUtils.removePrefixSuffix((String)s1, (String)"[", (String)"]");
                if (!s3.matches("^[a-zA-Z0-9_]+$")) {
                    Config.warn((String)("[Shaders] Invalid screen: " + s1 + ", key: " + key));
                    continue;
                }
                if (!ShaderPackParser.parseGuiScreen("screen." + s3, props, map, shaderProfiles, shaderOptions)) {
                    Config.warn((String)("[Shaders] Invalid screen: " + s1 + ", key: " + key));
                    continue;
                }
                ShaderOptionScreen shaderoptionscreen = new ShaderOptionScreen(s3);
                list.add((Object)shaderoptionscreen);
                continue;
            }
            ShaderOption shaderoption = ShaderUtils.getShaderOption((String)s1, (ShaderOption[])shaderOptions);
            if (shaderoption == null) {
                Config.warn((String)("[Shaders] Invalid option: " + s1 + ", key: " + key));
                list.add((Object)null);
                continue;
            }
            shaderoption.setVisible(true);
            list.add((Object)shaderoption);
        }
        ShaderOption[] ashaderoption = (ShaderOption[])list.toArray((Object[])new ShaderOption[list.size()]);
        String s2 = props.getProperty(key + ".columns");
        int j = Config.parseInt((String)s2, (int)2);
        ScreenShaderOptions screenshaderoptions = new ScreenShaderOptions(key, ashaderoption, j);
        map.put((Object)key, (Object)screenshaderoptions);
        return true;
    }

    public static BufferedReader resolveIncludes(BufferedReader reader, String filePath, IShaderPack shaderPack, int fileIndex, List<String> listFiles, int includeLevel) throws IOException {
        String s = "/";
        int i = filePath.lastIndexOf("/");
        if (i >= 0) {
            s = filePath.substring(0, i);
        }
        CharArrayWriter chararraywriter = new CharArrayWriter();
        int j = -1;
        LinkedHashSet set = new LinkedHashSet();
        int k = 1;
        while (true) {
            Matcher matcher1;
            Matcher matcher;
            String s1;
            if ((s1 = reader.readLine()) == null) {
                char[] achar = chararraywriter.toCharArray();
                if (j >= 0 && set.size() > 0) {
                    StringBuilder stringbuilder = new StringBuilder();
                    for (ShaderMacro shadermacro : set) {
                        stringbuilder.append("#define ");
                        stringbuilder.append(shadermacro.getName());
                        stringbuilder.append(" ");
                        stringbuilder.append(shadermacro.getValue());
                        stringbuilder.append("\n");
                    }
                    String s7 = stringbuilder.toString();
                    StringBuilder stringbuilder1 = new StringBuilder(new String(achar));
                    stringbuilder1.insert(j, s7);
                    String s9 = stringbuilder1.toString();
                    achar = s9.toCharArray();
                }
                CharArrayReader chararrayreader = new CharArrayReader(achar);
                return new BufferedReader((Reader)chararrayreader);
            }
            if (j < 0 && (matcher = PATTERN_VERSION.matcher((CharSequence)s1)).matches()) {
                String s2 = ShaderMacros.getFixedMacroLines() + ShaderMacros.getOptionMacroLines();
                String s3 = s1 + "\n" + s2;
                String s4 = "#line " + (k + 1) + " " + fileIndex;
                s1 = s3 + s4;
                j = chararraywriter.size() + s3.length();
            }
            if ((matcher1 = PATTERN_INCLUDE.matcher((CharSequence)s1)).matches()) {
                int l;
                String s8;
                String s6 = matcher1.group(1);
                boolean flag = s6.startsWith("/");
                String string = s8 = flag ? "/shaders" + s6 : s + "/" + s6;
                if (!listFiles.contains((Object)s8)) {
                    listFiles.add((Object)s8);
                }
                if ((s1 = ShaderPackParser.loadFile(s8, shaderPack, l = listFiles.indexOf((Object)s8) + 1, listFiles, includeLevel)) == null) {
                    throw new IOException("Included file not found: " + filePath);
                }
                if (s1.endsWith("\n")) {
                    s1 = s1.substring(0, s1.length() - 1);
                }
                String s5 = "#line 1 " + l + "\n";
                if (s1.startsWith("#version ")) {
                    s5 = "";
                }
                s1 = s5 + s1 + "\n#line " + (k + 1) + " " + fileIndex;
            }
            if (j >= 0 && s1.contains((CharSequence)ShaderMacros.getPrefixMacro())) {
                ShaderMacro[] ashadermacro = ShaderPackParser.findMacros(s1, ShaderMacros.getExtensions());
                for (int i1 = 0; i1 < ashadermacro.length; ++i1) {
                    ShaderMacro shadermacro1 = ashadermacro[i1];
                    set.add((Object)shadermacro1);
                }
            }
            chararraywriter.write(s1);
            chararraywriter.write("\n");
            ++k;
        }
    }

    private static ShaderMacro[] findMacros(String line, ShaderMacro[] macros) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < macros.length; ++i) {
            ShaderMacro shadermacro = macros[i];
            if (!line.contains((CharSequence)shadermacro.getName())) continue;
            list.add((Object)shadermacro);
        }
        ShaderMacro[] ashadermacro = (ShaderMacro[])list.toArray((Object[])new ShaderMacro[list.size()]);
        return ashadermacro;
    }

    private static String loadFile(String filePath, IShaderPack shaderPack, int fileIndex, List<String> listFiles, int includeLevel) throws IOException {
        if (includeLevel >= 10) {
            throw new IOException("#include depth exceeded: " + includeLevel + ", file: " + filePath);
        }
        ++includeLevel;
        InputStream inputstream = shaderPack.getResourceAsStream(filePath);
        if (inputstream == null) {
            return null;
        }
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream, "ASCII");
        BufferedReader bufferedreader = new BufferedReader((Reader)inputstreamreader);
        bufferedreader = ShaderPackParser.resolveIncludes(bufferedreader, filePath, shaderPack, fileIndex, listFiles, includeLevel);
        CharArrayWriter chararraywriter = new CharArrayWriter();
        String s;
        while ((s = bufferedreader.readLine()) != null) {
            chararraywriter.write(s);
            chararraywriter.write("\n");
        }
        return chararraywriter.toString();
    }

    public static CustomUniforms parseCustomUniforms(Properties props) {
        String s = "uniform";
        String s1 = "variable";
        String s2 = s + ".";
        String s3 = s1 + ".";
        HashMap map = new HashMap();
        ArrayList list = new ArrayList();
        for (String s4 : props.keySet()) {
            String[] astring = Config.tokenize((String)s4, (String)".");
            if (astring.length != 3) continue;
            String s5 = astring[0];
            String s6 = astring[1];
            String s7 = astring[2];
            String s8 = props.getProperty(s4).trim();
            if (map.containsKey((Object)s7)) {
                SMCLog.warning((String)("Expression already defined: " + s7));
                continue;
            }
            if (!s5.equals((Object)s) && !s5.equals((Object)s1)) continue;
            SMCLog.info((String)("Custom " + s5 + ": " + s7));
            CustomUniform customuniform = ShaderPackParser.parseCustomUniform(s5, s7, s6, s8, (Map<String, IExpression>)map);
            if (customuniform == null) continue;
            map.put((Object)s7, (Object)customuniform.getExpression());
            if (s5.equals((Object)s1)) continue;
            list.add((Object)customuniform);
        }
        if (list.size() <= 0) {
            return null;
        }
        CustomUniform[] acustomuniform = (CustomUniform[])list.toArray((Object[])new CustomUniform[list.size()]);
        CustomUniforms customuniforms = new CustomUniforms(acustomuniform, (Map)map);
        return customuniforms;
    }

    private static CustomUniform parseCustomUniform(String kind, String name, String type, String src, Map<String, IExpression> mapExpressions) {
        try {
            UniformType uniformtype = UniformType.parse((String)type);
            if (uniformtype == null) {
                SMCLog.warning((String)("Unknown " + kind + " type: " + uniformtype));
                return null;
            }
            ShaderExpressionResolver shaderexpressionresolver = new ShaderExpressionResolver(mapExpressions);
            ExpressionParser expressionparser = new ExpressionParser((IExpressionResolver)shaderexpressionresolver);
            IExpression iexpression = expressionparser.parse(src);
            ExpressionType expressiontype = iexpression.getExpressionType();
            if (!uniformtype.matchesExpressionType(expressiontype)) {
                SMCLog.warning((String)("Expression type does not match " + kind + " type, expression: " + expressiontype + ", " + kind + ": " + uniformtype + " " + name));
                return null;
            }
            iexpression = ShaderPackParser.makeExpressionCached(iexpression);
            CustomUniform customuniform = new CustomUniform(name, uniformtype, iexpression);
            return customuniform;
        }
        catch (ParseException parseexception) {
            SMCLog.warning((String)(parseexception.getClass().getName() + ": " + parseexception.getMessage()));
            return null;
        }
    }

    private static IExpression makeExpressionCached(IExpression expr) {
        return expr instanceof IExpressionFloat ? new ExpressionFloatCached((IExpressionFloat)expr) : (expr instanceof IExpressionFloatArray ? new ExpressionFloatArrayCached((IExpressionFloatArray)expr) : expr);
    }

    public static void parseAlphaStates(Properties props) {
        for (String s : props.keySet()) {
            String[] astring = Config.tokenize((String)s, (String)".");
            if (astring.length != 2) continue;
            String s1 = astring[0];
            String s2 = astring[1];
            if (!s1.equals((Object)"alphaTest")) continue;
            Program program = Shaders.getProgram((String)s2);
            if (program == null) {
                SMCLog.severe((String)("Invalid program name: " + s2));
                continue;
            }
            String s3 = props.getProperty(s).trim();
            GlAlphaState glalphastate = ShaderPackParser.parseAlphaState(s3);
            if (glalphastate == null) continue;
            program.setAlphaState(glalphastate);
        }
    }

    private static GlAlphaState parseAlphaState(String str) {
        String[] astring = Config.tokenize((String)str, (String)" ");
        if (astring.length == 1) {
            String s = astring[0];
            if (s.equals((Object)"off") || s.equals((Object)"false")) {
                return new GlAlphaState(false);
            }
        } else if (astring.length == 2) {
            String s2 = astring[0];
            String s1 = astring[1];
            Integer integer = (Integer)mapAlphaFuncs.get((Object)s2);
            float f = Config.parseFloat((String)s1, (float)-1.0f);
            if (integer != null && f >= 0.0f) {
                return new GlAlphaState(true, integer.intValue(), f);
            }
        }
        SMCLog.severe((String)("Invalid alpha test: " + str));
        return null;
    }

    public static void parseBlendStates(Properties props) {
        for (String s : props.keySet()) {
            String[] astring = Config.tokenize((String)s, (String)".");
            if (astring.length != 2) continue;
            String s1 = astring[0];
            String s2 = astring[1];
            if (!s1.equals((Object)"blend")) continue;
            Program program = Shaders.getProgram((String)s2);
            if (program == null) {
                SMCLog.severe((String)("Invalid program name: " + s2));
                continue;
            }
            String s3 = props.getProperty(s).trim();
            GlBlendState glblendstate = ShaderPackParser.parseBlendState(s3);
            if (glblendstate == null) continue;
            program.setBlendState(glblendstate);
        }
    }

    private static GlBlendState parseBlendState(String str) {
        String[] astring = Config.tokenize((String)str, (String)" ");
        if (astring.length == 1) {
            String s = astring[0];
            if (s.equals((Object)"off") || s.equals((Object)"false")) {
                return new GlBlendState(false);
            }
        } else if (astring.length == 2 || astring.length == 4) {
            String s4 = astring[0];
            String s1 = astring[1];
            String s2 = s4;
            String s3 = s1;
            if (astring.length == 4) {
                s2 = astring[2];
                s3 = astring[3];
            }
            Integer integer = (Integer)mapBlendFactors.get((Object)s4);
            Integer integer1 = (Integer)mapBlendFactors.get((Object)s1);
            Integer integer2 = (Integer)mapBlendFactors.get((Object)s2);
            Integer integer3 = (Integer)mapBlendFactors.get((Object)s3);
            if (integer != null && integer1 != null && integer2 != null && integer3 != null) {
                return new GlBlendState(true, integer.intValue(), integer1.intValue(), integer2.intValue(), integer3.intValue());
            }
        }
        SMCLog.severe((String)("Invalid blend mode: " + str));
        return null;
    }

    public static void parseRenderScales(Properties props) {
        for (String s : props.keySet()) {
            String[] astring = Config.tokenize((String)s, (String)".");
            if (astring.length != 2) continue;
            String s1 = astring[0];
            String s2 = astring[1];
            if (!s1.equals((Object)"scale")) continue;
            Program program = Shaders.getProgram((String)s2);
            if (program == null) {
                SMCLog.severe((String)("Invalid program name: " + s2));
                continue;
            }
            String s3 = props.getProperty(s).trim();
            RenderScale renderscale = ShaderPackParser.parseRenderScale(s3);
            if (renderscale == null) continue;
            program.setRenderScale(renderscale);
        }
    }

    private static RenderScale parseRenderScale(String str) {
        String[] astring = Config.tokenize((String)str, (String)" ");
        float f = Config.parseFloat((String)astring[0], (float)-1.0f);
        float f1 = 0.0f;
        float f2 = 0.0f;
        if (astring.length > 1) {
            if (astring.length != 3) {
                SMCLog.severe((String)("Invalid render scale: " + str));
                return null;
            }
            f1 = Config.parseFloat((String)astring[1], (float)-1.0f);
            f2 = Config.parseFloat((String)astring[2], (float)-1.0f);
        }
        if (Config.between((float)f, (float)0.0f, (float)1.0f) && Config.between((float)f1, (float)0.0f, (float)1.0f) && Config.between((float)f2, (float)0.0f, (float)1.0f)) {
            return new RenderScale(f, f1, f2);
        }
        SMCLog.severe((String)("Invalid render scale: " + str));
        return null;
    }

    public static void parseBuffersFlip(Properties props) {
        for (String s : props.keySet()) {
            String[] astring = Config.tokenize((String)s, (String)".");
            if (astring.length != 3) continue;
            String s1 = astring[0];
            String s2 = astring[1];
            String s3 = astring[2];
            if (!s1.equals((Object)"flip")) continue;
            Program program = Shaders.getProgram((String)s2);
            if (program == null) {
                SMCLog.severe((String)("Invalid program name: " + s2));
                continue;
            }
            Boolean[] aboolean = program.getBuffersFlip();
            int i = Shaders.getBufferIndexFromString((String)s3);
            if (i >= 0 && i < aboolean.length) {
                String s4 = props.getProperty(s).trim();
                Boolean obool = Config.parseBoolean((String)s4, (Boolean)null);
                if (obool == null) {
                    SMCLog.severe((String)("Invalid boolean value: " + s4));
                    continue;
                }
                aboolean[i] = obool;
                continue;
            }
            SMCLog.severe((String)("Invalid buffer name: " + s3));
        }
    }

    private static Map<String, Integer> makeMapAlphaFuncs() {
        HashMap map = new HashMap();
        map.put((Object)"NEVER", (Object)new Integer(512));
        map.put((Object)"LESS", (Object)new Integer(513));
        map.put((Object)"EQUAL", (Object)new Integer(514));
        map.put((Object)"LEQUAL", (Object)new Integer(515));
        map.put((Object)"GREATER", (Object)new Integer(516));
        map.put((Object)"NOTEQUAL", (Object)new Integer(517));
        map.put((Object)"GEQUAL", (Object)new Integer(518));
        map.put((Object)"ALWAYS", (Object)new Integer(519));
        return Collections.unmodifiableMap((Map)map);
    }

    private static Map<String, Integer> makeMapBlendFactors() {
        HashMap map = new HashMap();
        map.put((Object)"ZERO", (Object)new Integer(0));
        map.put((Object)"ONE", (Object)new Integer(1));
        map.put((Object)"SRC_COLOR", (Object)new Integer(768));
        map.put((Object)"ONE_MINUS_SRC_COLOR", (Object)new Integer(769));
        map.put((Object)"DST_COLOR", (Object)new Integer(774));
        map.put((Object)"ONE_MINUS_DST_COLOR", (Object)new Integer(775));
        map.put((Object)"SRC_ALPHA", (Object)new Integer(770));
        map.put((Object)"ONE_MINUS_SRC_ALPHA", (Object)new Integer(771));
        map.put((Object)"DST_ALPHA", (Object)new Integer(772));
        map.put((Object)"ONE_MINUS_DST_ALPHA", (Object)new Integer(773));
        map.put((Object)"CONSTANT_COLOR", (Object)new Integer(32769));
        map.put((Object)"ONE_MINUS_CONSTANT_COLOR", (Object)new Integer(32770));
        map.put((Object)"CONSTANT_ALPHA", (Object)new Integer(32771));
        map.put((Object)"ONE_MINUS_CONSTANT_ALPHA", (Object)new Integer(32772));
        map.put((Object)"SRC_ALPHA_SATURATE", (Object)new Integer(776));
        return Collections.unmodifiableMap((Map)map);
    }
}
