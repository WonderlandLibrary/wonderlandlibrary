package shadersmod.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import optifine.Config;

public class ShaderPackParser {
   private static final Pattern PATTERN_VERSION = Pattern.compile("^\\s*#version\\s+.*$");
   private static final Pattern PATTERN_INCLUDE = Pattern.compile("^\\s*#include\\s+\"([A-Za-z0-9_/\\.]+)\".*$");
   private static final Set setConstNames = makeSetConstNames();

   public static ShaderOption[] parseShaderPackOptions(IShaderPack var0, String[] var1, List var2) {
      if (var0 == null) {
         return new ShaderOption[0];
      } else {
         HashMap var3 = new HashMap();
         collectShaderOptions(var0, "/shaders", var1, var3);
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            int var5 = (Integer)var4.next();
            String var6 = "/shaders/world" + var5;
            collectShaderOptions(var0, var6, var1, var3);
         }

         Collection var8 = var3.values();
         ShaderOption[] var9 = (ShaderOption[])var8.toArray(new ShaderOption[var8.size()]);
         Comparator var7 = new Comparator() {
            public int compare(ShaderOption var1, ShaderOption var2) {
               return var1.getName().compareToIgnoreCase(var2.getName());
            }

            public int compare(Object var1, Object var2) {
               return this.compare((ShaderOption)var1, (ShaderOption)var2);
            }
         };
         Arrays.sort(var9, var7);
         return var9;
      }
   }

   private static void collectShaderOptions(IShaderPack var0, String var1, String[] var2, Map var3) {
      for(int var4 = 0; var4 < var2.length; ++var4) {
         String var5 = var2[var4];
         if (!var5.equals("")) {
            String var6 = var1 + "/" + var5 + ".vsh";
            String var7 = var1 + "/" + var5 + ".fsh";
            collectShaderOptions(var0, var6, var3);
            collectShaderOptions(var0, var7, var3);
         }
      }

   }

   private static void collectShaderOptions(IShaderPack var0, String var1, Map var2) {
      String[] var3 = getLines(var0, var1);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         String var5 = var3[var4];
         ShaderOption var6 = getShaderOption(var5, var1);
         if (var6 != null && !var6.getName().startsWith(ShaderMacros.getPrefixMacro()) && (!var6.checkUsed() || var3 != false)) {
            String var7 = var6.getName();
            ShaderOption var8 = (ShaderOption)var2.get(var7);
            if (var8 != null) {
               if (!Config.equals(var8.getValueDefault(), var6.getValueDefault())) {
                  Config.warn("Ambiguous shader option: " + var6.getName());
                  Config.warn(" - in " + Config.arrayToString((Object[])var8.getPaths()) + ": " + var8.getValueDefault());
                  Config.warn(" - in " + Config.arrayToString((Object[])var6.getPaths()) + ": " + var6.getValueDefault());
                  var8.setEnabled(false);
               }

               if (var8.getDescription() == null || var8.getDescription().length() <= 0) {
                  var8.setDescription(var6.getDescription());
               }

               var8.addPaths(var6.getPaths());
            } else {
               var2.put(var7, var6);
            }
         }
      }

   }

   private static String[] getLines(IShaderPack var0, String var1) {
      try {
         ArrayList var2 = new ArrayList();
         String var3 = loadFile(var1, var0, 0, var2, 0);
         if (var3 == null) {
            return new String[0];
         } else {
            ByteArrayInputStream var4 = new ByteArrayInputStream(var3.getBytes());
            String[] var5 = Config.readLines((InputStream)var4);
            return var5;
         }
      } catch (IOException var6) {
         Config.dbg(var6.getClass().getName() + ": " + var6.getMessage());
         return new String[0];
      }
   }

   private static ShaderOption getShaderOption(String var0, String var1) {
      ShaderOption var2 = null;
      if (var2 == null) {
         var2 = ShaderOptionSwitch.parseOption(var0, var1);
      }

      if (var2 == null) {
         var2 = ShaderOptionVariable.parseOption(var0, var1);
      }

      if (var2 != null) {
         return var2;
      } else {
         if (var2 == null) {
            var2 = ShaderOptionSwitchConst.parseOption(var0, var1);
         }

         if (var2 == null) {
            var2 = ShaderOptionVariableConst.parseOption(var0, var1);
         }

         return var2 != null && setConstNames.contains(var2.getName()) ? var2 : null;
      }
   }

   private static Set makeSetConstNames() {
      HashSet var0 = new HashSet();
      var0.add("shadowMapResolution");
      var0.add("shadowDistance");
      var0.add("shadowIntervalSize");
      var0.add("generateShadowMipmap");
      var0.add("generateShadowColorMipmap");
      var0.add("shadowHardwareFiltering");
      var0.add("shadowHardwareFiltering0");
      var0.add("shadowHardwareFiltering1");
      var0.add("shadowtex0Mipmap");
      var0.add("shadowtexMipmap");
      var0.add("shadowtex1Mipmap");
      var0.add("shadowcolor0Mipmap");
      var0.add("shadowColor0Mipmap");
      var0.add("shadowcolor1Mipmap");
      var0.add("shadowColor1Mipmap");
      var0.add("shadowtex0Nearest");
      var0.add("shadowtexNearest");
      var0.add("shadow0MinMagNearest");
      var0.add("shadowtex1Nearest");
      var0.add("shadow1MinMagNearest");
      var0.add("shadowcolor0Nearest");
      var0.add("shadowColor0Nearest");
      var0.add("shadowColor0MinMagNearest");
      var0.add("shadowcolor1Nearest");
      var0.add("shadowColor1Nearest");
      var0.add("shadowColor1MinMagNearest");
      var0.add("wetnessHalflife");
      var0.add("drynessHalflife");
      var0.add("eyeBrightnessHalflife");
      var0.add("centerDepthHalflife");
      var0.add("sunPathRotation");
      var0.add("ambientOcclusionLevel");
      var0.add("superSamplingLevel");
      var0.add("noiseTextureResolution");
      return var0;
   }

   public static ShaderProfile[] parseProfiles(Properties var0, ShaderOption[] var1) {
      String var2 = "profile.";
      ArrayList var3 = new ArrayList();
      Iterator var5 = var0.keySet().iterator();

      while(var5.hasNext()) {
         Object var4 = var5.next();
         String var6 = (String)var4;
         if (var6.startsWith(var2)) {
            String var7 = var6.substring(var2.length());
            var0.getProperty(var6);
            HashSet var8 = new HashSet();
            ShaderProfile var9 = parseProfile(var7, var0, var8, var1);
            if (var9 != null) {
               var3.add(var9);
            }
         }
      }

      if (var3.size() <= 0) {
         return null;
      } else {
         ShaderProfile[] var10 = (ShaderProfile[])var3.toArray(new ShaderProfile[var3.size()]);
         return var10;
      }
   }

   private static ShaderProfile parseProfile(String var0, Properties var1, Set var2, ShaderOption[] var3) {
      String var4 = "profile.";
      String var5 = var4 + var0;
      if (var2.contains(var5)) {
         Config.warn("[Shaders] Profile already parsed: " + var0);
         return null;
      } else {
         var2.add(var0);
         ShaderProfile var6 = new ShaderProfile(var0);
         String var7 = var1.getProperty(var5);
         String[] var8 = Config.tokenize(var7, " ");

         for(int var9 = 0; var9 < var8.length; ++var9) {
            String var10 = var8[var9];
            if (var10.startsWith(var4)) {
               String var16 = var10.substring(var4.length());
               ShaderProfile var18 = parseProfile(var16, var1, var2, var3);
               if (var6 != null) {
                  var6.addOptionValues(var18);
                  var6.addDisabledPrograms(var18.getDisabledPrograms());
               }
            } else {
               String[] var11 = Config.tokenize(var10, ":=");
               String var12;
               if (var11.length == 1) {
                  var12 = var11[0];
                  boolean var17 = true;
                  if (var12.startsWith("!")) {
                     var17 = false;
                     var12 = var12.substring(1);
                  }

                  String var19 = "program.";
                  if (!var17 && var12.startsWith("program.")) {
                     String var20 = var12.substring(var19.length());
                     if (!Shaders.isProgramPath(var20)) {
                        Config.warn("Invalid program: " + var20 + " in profile: " + var6.getName());
                     } else {
                        var6.addDisabledProgram(var20);
                     }
                  } else {
                     ShaderOption var15 = ShaderUtils.getShaderOption(var12, var3);
                     if (!(var15 instanceof ShaderOptionSwitch)) {
                        Config.warn("[Shaders] Invalid option: " + var12);
                     } else {
                        var6.addOptionValue(var12, String.valueOf(var17));
                        var15.setVisible(true);
                     }
                  }
               } else if (var11.length != 2) {
                  Config.warn("[Shaders] Invalid option value: " + var10);
               } else {
                  var12 = var11[0];
                  String var13 = var11[1];
                  ShaderOption var14 = ShaderUtils.getShaderOption(var12, var3);
                  if (var14 == null) {
                     Config.warn("[Shaders] Invalid option: " + var10);
                  } else if (!var14.isValidValue(var13)) {
                     Config.warn("[Shaders] Invalid value: " + var10);
                  } else {
                     var14.setVisible(true);
                     var6.addOptionValue(var12, var13);
                  }
               }
            }
         }

         return var6;
      }
   }

   public static Map parseGuiScreens(Properties var0, ShaderProfile[] var1, ShaderOption[] var2) {
      HashMap var3 = new HashMap();
      parseGuiScreen("screen", var0, var3, var1, var2);
      return var3.isEmpty() ? null : var3;
   }

   public static BufferedReader resolveIncludes(BufferedReader var0, String var1, IShaderPack var2, int var3, List var4, int var5) throws IOException {
      String var6 = "/";
      int var7 = var1.lastIndexOf("/");
      if (var7 >= 0) {
         var6 = var1.substring(0, var7);
      }

      CharArrayWriter var8 = new CharArrayWriter();
      int var9 = -1;
      LinkedHashSet var10 = new LinkedHashSet();
      int var11 = 1;

      while(true) {
         String var12 = var0.readLine();
         String var15;
         if (var12 == null) {
            char[] var18 = var8.toCharArray();
            if (var9 >= 0 && var10.size() > 0) {
               StringBuilder var20 = new StringBuilder();
               Iterator var24 = var10.iterator();

               while(var24.hasNext()) {
                  var15 = (String)var24.next();
                  var20.append("#define ");
                  var20.append(var15);
                  var20.append("\n");
               }

               var15 = var20.toString();
               StringBuilder var25 = new StringBuilder(new String(var18));
               var25.insert(var9, var15);
               String var26 = var25.toString();
               var18 = var26.toCharArray();
            }

            CharArrayReader var21 = new CharArrayReader(var18);
            return new BufferedReader(var21);
         }

         Matcher var13;
         String var14;
         String var16;
         if (var9 < 0) {
            var13 = PATTERN_VERSION.matcher(var12);
            if (var13.matches()) {
               var14 = "#define MC_VERSION " + Config.getMinecraftVersionInt() + "\n" + "#define " + "MC_GL_VERSION" + " " + Config.getGlVersion().toInt() + "\n" + "#define " + "MC_GLSL_VERSION" + " " + Config.getGlslVersion().toInt() + "\n" + "#define " + ShaderMacros.getOs() + "\n" + "#define " + ShaderMacros.getVendor() + "\n" + "#define " + ShaderMacros.getRenderer() + "\n";
               var15 = var12 + "\n" + var14;
               var16 = "#line " + (var11 + 1) + " " + var3;
               var12 = var15 + var16;
               var9 = var8.size() + var15.length();
            }
         }

         var13 = PATTERN_INCLUDE.matcher(var12);
         if (var13.matches()) {
            var14 = var13.group(1);
            boolean var22 = var14.startsWith("/");
            var16 = var22 ? "/shaders" + var14 : var6 + "/" + var14;
            if (!var4.contains(var16)) {
               var4.add(var16);
            }

            int var17 = var4.indexOf(var16) + 1;
            var12 = loadFile(var16, var2, var17, var4, var5);
            if (var12 == null) {
               throw new IOException("Included file not found: " + var1);
            }

            if (var12.endsWith("\n")) {
               var12 = var12.substring(0, var12.length() - 1);
            }

            var12 = "#line 1 " + var17 + "\n" + var12 + "\n" + "#line " + (var11 + 1) + " " + var3;
         }

         if (var9 >= 0 && var12.contains(ShaderMacros.getPrefixMacro())) {
            String[] var19 = findExtensions(var12, ShaderMacros.getExtensions());

            for(int var23 = 0; var23 < var19.length; ++var23) {
               var16 = var19[var23];
               var10.add(var16);
            }
         }

         var8.write(var12);
         var8.write("\n");
         ++var11;
      }
   }

   private static String[] findExtensions(String var0, String[] var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3];
         if (var0.contains(var4)) {
            var2.add(var4);
         }
      }

      String[] var5 = (String[])var2.toArray(new String[var2.size()]);
      return var5;
   }

   private static String loadFile(String var0, IShaderPack var1, int var2, List var3, int var4) throws IOException {
      if (var4 >= 10) {
         throw new IOException("#include depth exceeded: " + var4 + ", file: " + var0);
      } else {
         ++var4;
         InputStream var5 = var1.getResourceAsStream(var0);
         if (var5 == null) {
            return null;
         } else {
            InputStreamReader var6 = new InputStreamReader(var5, "ASCII");
            BufferedReader var7 = new BufferedReader(var6);
            var7 = resolveIncludes(var7, var0, var1, var2, var3, var4);
            CharArrayWriter var8 = new CharArrayWriter();

            while(true) {
               String var9 = var7.readLine();
               if (var9 == null) {
                  return var8.toString();
               }

               var8.write(var9);
               var8.write("\n");
            }
         }
      }
   }
}
