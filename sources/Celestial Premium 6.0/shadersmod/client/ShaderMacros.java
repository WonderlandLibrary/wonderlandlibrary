/*
 * Decompiled with CFR 0.150.
 */
package shadersmod.client;

import net.minecraft.util.Util;
import optifine.Config;
import shadersmod.client.Shaders;

public class ShaderMacros {
    private static String PREFIX_MACRO = "MC_";
    public static final String MC_VERSION = "MC_VERSION";
    public static final String MC_GL_VERSION = "MC_GL_VERSION";
    public static final String MC_GLSL_VERSION = "MC_GLSL_VERSION";
    public static final String MC_OS_WINDOWS = "MC_OS_WINDOWS";
    public static final String MC_OS_MAC = "MC_OS_MAC";
    public static final String MC_OS_LINUX = "MC_OS_LINUX";
    public static final String MC_OS_OTHER = "MC_OS_OTHER";
    public static final String MC_GL_VENDOR_ATI = "MC_GL_VENDOR_ATI";
    public static final String MC_GL_VENDOR_INTEL = "MC_GL_VENDOR_INTEL";
    public static final String MC_GL_VENDOR_NVIDIA = "MC_GL_VENDOR_NVIDIA";
    public static final String MC_GL_VENDOR_XORG = "MC_GL_VENDOR_XORG";
    public static final String MC_GL_VENDOR_OTHER = "MC_GL_VENDOR_OTHER";
    public static final String MC_GL_RENDERER_RADEON = "MC_GL_RENDERER_RADEON";
    public static final String MC_GL_RENDERER_GEFORCE = "MC_GL_RENDERER_GEFORCE";
    public static final String MC_GL_RENDERER_QUADRO = "MC_GL_RENDERER_QUADRO";
    public static final String MC_GL_RENDERER_INTEL = "MC_GL_RENDERER_INTEL";
    public static final String MC_GL_RENDERER_GALLIUM = "MC_GL_RENDERER_GALLIUM";
    public static final String MC_GL_RENDERER_MESA = "MC_GL_RENDERER_MESA";
    public static final String MC_GL_RENDERER_OTHER = "MC_GL_RENDERER_OTHER";
    public static final String MC_FXAA_LEVEL = "MC_FXAA_LEVEL";
    public static final String MC_NORMAL_MAP = "MC_NORMAL_MAP";
    public static final String MC_SPECULAR_MAP = "MC_SPECULAR_MAP";
    public static final String MC_RENDER_QUALITY = "MC_RENDER_QUALITY";
    public static final String MC_SHADOW_QUALITY = "MC_SHADOW_QUALITY";
    public static final String MC_HAND_DEPTH = "MC_HAND_DEPTH";
    public static final String MC_OLD_HAND_LIGHT = "MC_OLD_HAND_LIGHT";
    public static final String MC_OLD_LIGHTING = "MC_OLD_LIGHTING";
    private static String[] extensionMacros;

    public static String getOs() {
        Util.EnumOS util$enumos = Util.getOSType();
        switch (util$enumos) {
            case WINDOWS: {
                return MC_OS_WINDOWS;
            }
            case OSX: {
                return MC_OS_MAC;
            }
            case LINUX: {
                return MC_OS_LINUX;
            }
        }
        return MC_OS_OTHER;
    }

    public static String getVendor() {
        String s = Config.openGlVendor;
        if (s == null) {
            return MC_GL_VENDOR_OTHER;
        }
        if ((s = s.toLowerCase()).startsWith("ati")) {
            return MC_GL_VENDOR_ATI;
        }
        if (s.startsWith("intel")) {
            return MC_GL_VENDOR_INTEL;
        }
        if (s.startsWith("nvidia")) {
            return MC_GL_VENDOR_NVIDIA;
        }
        return s.startsWith("x.org") ? MC_GL_VENDOR_XORG : MC_GL_VENDOR_OTHER;
    }

    public static String getRenderer() {
        String s = Config.openGlRenderer;
        if (s == null) {
            return MC_GL_RENDERER_OTHER;
        }
        if ((s = s.toLowerCase()).startsWith("amd")) {
            return MC_GL_RENDERER_RADEON;
        }
        if (s.startsWith("ati")) {
            return MC_GL_RENDERER_RADEON;
        }
        if (s.startsWith("radeon")) {
            return MC_GL_RENDERER_RADEON;
        }
        if (s.startsWith("gallium")) {
            return MC_GL_RENDERER_GALLIUM;
        }
        if (s.startsWith("intel")) {
            return MC_GL_RENDERER_INTEL;
        }
        if (s.startsWith("geforce")) {
            return MC_GL_RENDERER_GEFORCE;
        }
        if (s.startsWith("nvidia")) {
            return MC_GL_RENDERER_GEFORCE;
        }
        if (s.startsWith("quadro")) {
            return MC_GL_RENDERER_QUADRO;
        }
        if (s.startsWith("nvs")) {
            return MC_GL_RENDERER_QUADRO;
        }
        return s.startsWith("mesa") ? MC_GL_RENDERER_MESA : MC_GL_RENDERER_OTHER;
    }

    public static String getPrefixMacro() {
        return PREFIX_MACRO;
    }

    public static String[] getExtensions() {
        if (extensionMacros == null) {
            String[] astring = Config.getOpenGlExtensions();
            String[] astring1 = new String[astring.length];
            for (int i = 0; i < astring.length; ++i) {
                astring1[i] = PREFIX_MACRO + astring[i];
            }
            extensionMacros = astring1;
        }
        return extensionMacros;
    }

    public static String getMacroLines() {
        StringBuilder stringbuilder = new StringBuilder();
        ShaderMacros.addMacroLine(stringbuilder, MC_VERSION, Config.getInstanceVersionInt());
        ShaderMacros.addMacroLine(stringbuilder, "MC_GL_VERSION " + Config.getGlVersion().toInt());
        ShaderMacros.addMacroLine(stringbuilder, "MC_GLSL_VERSION " + Config.getGlslVersion().toInt());
        ShaderMacros.addMacroLine(stringbuilder, ShaderMacros.getOs());
        ShaderMacros.addMacroLine(stringbuilder, ShaderMacros.getVendor());
        ShaderMacros.addMacroLine(stringbuilder, ShaderMacros.getRenderer());
        if (Shaders.configAntialiasingLevel > 0) {
            ShaderMacros.addMacroLine(stringbuilder, MC_FXAA_LEVEL, Shaders.configAntialiasingLevel);
        }
        if (Shaders.configNormalMap) {
            ShaderMacros.addMacroLine(stringbuilder, MC_NORMAL_MAP);
        }
        if (Shaders.configSpecularMap) {
            ShaderMacros.addMacroLine(stringbuilder, MC_SPECULAR_MAP);
        }
        ShaderMacros.addMacroLine(stringbuilder, MC_RENDER_QUALITY, Shaders.configRenderResMul);
        ShaderMacros.addMacroLine(stringbuilder, MC_SHADOW_QUALITY, Shaders.configShadowResMul);
        ShaderMacros.addMacroLine(stringbuilder, MC_HAND_DEPTH, Shaders.configHandDepthMul);
        if (Shaders.isOldHandLight()) {
            ShaderMacros.addMacroLine(stringbuilder, MC_OLD_HAND_LIGHT);
        }
        if (Shaders.isOldLighting()) {
            ShaderMacros.addMacroLine(stringbuilder, MC_OLD_LIGHTING);
        }
        return stringbuilder.toString();
    }

    private static void addMacroLine(StringBuilder sb, String name, int value) {
        sb.append("#define ");
        sb.append(name);
        sb.append(" ");
        sb.append(value);
        sb.append("\n");
    }

    private static void addMacroLine(StringBuilder sb, String name, float value) {
        sb.append("#define ");
        sb.append(name);
        sb.append(" ");
        sb.append(value);
        sb.append("\n");
    }

    private static void addMacroLine(StringBuilder sb, String name) {
        sb.append("#define ");
        sb.append(name);
        sb.append("\n");
    }
}

