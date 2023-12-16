// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.GuiScreen;
import java.util.Collection;
import java.lang.reflect.Array;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.PixelFormat;
import net.minecraft.world.WorldProvider;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.WorldServer;
import net.minecraft.util.BlockPos;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.LWJGLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.awt.Dimension;
import net.minecraft.client.renderer.RenderGlobal;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.ResourcePackRepository;
import java.util.ArrayList;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.IResource;
import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureManager;
import java.lang.reflect.Method;
import net.minecraft.client.resources.IResourceManager;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;
import java.io.InputStream;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.GL11;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import shadersmod.client.Shaders;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileDescriptor;
import java.io.PrintStream;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.DefaultResourcePack;
import org.lwjgl.opengl.DisplayMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class Config
{
    public static final String OF_NAME = "OptiFine";
    public static final String MC_VERSION = "1.8";
    public static final String OF_EDITION = "HD_U";
    public static final String OF_RELEASE = "H6";
    public static final String VERSION = "OptiFine_1.8_HD_U_H6";
    private static String newRelease;
    private static boolean notify64BitJava;
    public static String openGlVersion;
    public static String openGlRenderer;
    public static String openGlVendor;
    public static boolean fancyFogAvailable;
    public static boolean occlusionAvailable;
    private static GameSettings gameSettings;
    private static Minecraft minecraft;
    private static boolean initialized;
    private static Thread minecraftThread;
    private static DisplayMode desktopDisplayMode;
    private static int antialiasingLevel;
    private static int availableProcessors;
    public static boolean zoomMode;
    private static int texturePackClouds;
    public static boolean waterOpacityChanged;
    private static boolean fullscreenModeChecked;
    private static boolean desktopModeChecked;
    private static DefaultResourcePack defaultResourcePack;
    private static ModelManager modelManager;
    private static PrintStream systemOut;
    public static final Float DEF_ALPHA_FUNC_LEVEL;
    
    static {
        Config.newRelease = null;
        Config.notify64BitJava = false;
        Config.openGlVersion = null;
        Config.openGlRenderer = null;
        Config.openGlVendor = null;
        Config.fancyFogAvailable = false;
        Config.occlusionAvailable = false;
        Config.gameSettings = null;
        Config.minecraft = null;
        Config.initialized = false;
        Config.minecraftThread = null;
        Config.desktopDisplayMode = null;
        Config.antialiasingLevel = 0;
        Config.availableProcessors = 0;
        Config.zoomMode = false;
        Config.texturePackClouds = 0;
        Config.waterOpacityChanged = false;
        Config.fullscreenModeChecked = false;
        Config.desktopModeChecked = false;
        Config.defaultResourcePack = null;
        Config.modelManager = null;
        Config.systemOut = new PrintStream(new FileOutputStream(FileDescriptor.out));
        DEF_ALPHA_FUNC_LEVEL = 0.1f;
    }
    
    public static String getVersion() {
        return "OptiFine_1.8_HD_U_H6";
    }
    
    public static String getVersionDebug() {
        final StringBuffer sb = new StringBuffer(32);
        if (isDynamicLights()) {
            sb.append("DL: ");
            sb.append(String.valueOf(DynamicLights.getCount()));
            sb.append(", ");
        }
        sb.append("OptiFine_1.8_HD_U_H6");
        final String shaderPack = Shaders.getShaderPackName();
        if (shaderPack != null) {
            sb.append(", ");
            sb.append(shaderPack);
        }
        return sb.toString();
    }
    
    public static void initGameSettings(final GameSettings settings) {
        if (Config.gameSettings == null) {
            Config.gameSettings = settings;
            Config.minecraft = Minecraft.getMinecraft();
            Config.desktopDisplayMode = Display.getDesktopDisplayMode();
            updateAvailableProcessors();
            ReflectorForge.putLaunchBlackboard("optifine.ForgeSplashCompatible", Boolean.TRUE);
        }
    }
    
    public static void initDisplay() {
        checkInitialized();
        Config.antialiasingLevel = Config.gameSettings.ofAaLevel;
        checkDisplaySettings();
        checkDisplayMode();
        Config.minecraftThread = Thread.currentThread();
        updateThreadPriorities();
        Shaders.startup(Minecraft.getMinecraft());
    }
    
    public static void checkInitialized() {
        if (!Config.initialized && Display.isCreated()) {
            Config.initialized = true;
            checkOpenGlCaps();
            startVersionCheckThread();
        }
    }
    
    private static void checkOpenGlCaps() {
        log("");
        log(getVersion());
        log("Build: " + getBuild());
        log("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
        log("Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
        log("VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
        log("LWJGL: " + Sys.getVersion());
        Config.openGlVersion = GL11.glGetString(7938);
        Config.openGlRenderer = GL11.glGetString(7937);
        Config.openGlVendor = GL11.glGetString(7936);
        log("OpenGL: " + Config.openGlRenderer + ", version " + Config.openGlVersion + ", " + Config.openGlVendor);
        log("OpenGL Version: " + getOpenGlVersionString());
        if (!GLContext.getCapabilities().OpenGL12) {
            log("OpenGL Mipmap levels: Not available (GL12.GL_TEXTURE_MAX_LEVEL)");
        }
        if (!(Config.fancyFogAvailable = GLContext.getCapabilities().GL_NV_fog_distance)) {
            log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");
        }
        if (!(Config.occlusionAvailable = GLContext.getCapabilities().GL_ARB_occlusion_query)) {
            log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");
        }
        final int maxTexSize = Minecraft.getGLMaximumTextureSize();
        dbg("Maximum texture size: " + maxTexSize + "x" + maxTexSize);
    }
    
    private static String getBuild() {
        try {
            final InputStream e = Config.class.getResourceAsStream("/buildof.txt");
            if (e == null) {
                return null;
            }
            final String build = readLines(e)[0];
            return build;
        }
        catch (Exception var2) {
            warn(var2.getClass().getName() + ": " + var2.getMessage());
            return null;
        }
    }
    
    public static boolean isFancyFogAvailable() {
        return Config.fancyFogAvailable;
    }
    
    public static boolean isOcclusionAvailable() {
        return Config.occlusionAvailable;
    }
    
    public static String getOpenGlVersionString() {
        final int ver = getOpenGlVersion();
        final String verStr = ver / 10 + "." + ver % 10;
        return verStr;
    }
    
    private static int getOpenGlVersion() {
        return GLContext.getCapabilities().OpenGL11 ? (GLContext.getCapabilities().OpenGL12 ? (GLContext.getCapabilities().OpenGL13 ? (GLContext.getCapabilities().OpenGL14 ? (GLContext.getCapabilities().OpenGL15 ? (GLContext.getCapabilities().OpenGL20 ? (GLContext.getCapabilities().OpenGL21 ? (GLContext.getCapabilities().OpenGL30 ? (GLContext.getCapabilities().OpenGL31 ? (GLContext.getCapabilities().OpenGL32 ? (GLContext.getCapabilities().OpenGL33 ? (GLContext.getCapabilities().OpenGL40 ? 40 : 33) : 32) : 31) : 30) : 21) : 20) : 15) : 14) : 13) : 12) : 11) : 10;
    }
    
    public static void updateThreadPriorities() {
        updateAvailableProcessors();
        final boolean ELEVATED_PRIORITY = true;
        if (isSingleProcessor()) {
            if (isSmoothWorld()) {
                Config.minecraftThread.setPriority(10);
                setThreadPriority("Server thread", 1);
            }
            else {
                Config.minecraftThread.setPriority(5);
                setThreadPriority("Server thread", 5);
            }
        }
        else {
            Config.minecraftThread.setPriority(10);
            setThreadPriority("Server thread", 5);
        }
    }
    
    private static void setThreadPriority(final String prefix, final int priority) {
        try {
            final ThreadGroup e = Thread.currentThread().getThreadGroup();
            if (e == null) {
                return;
            }
            final int num = (e.activeCount() + 10) * 2;
            final Thread[] ts = new Thread[num];
            e.enumerate(ts, false);
            for (int i = 0; i < ts.length; ++i) {
                final Thread t = ts[i];
                if (t != null && t.getName().startsWith(prefix)) {
                    t.setPriority(priority);
                }
            }
        }
        catch (Throwable var7) {
            warn(String.valueOf(var7.getClass().getName()) + ": " + var7.getMessage());
        }
    }
    
    public static boolean isMinecraftThread() {
        return Thread.currentThread() == Config.minecraftThread;
    }
    
    private static void startVersionCheckThread() {
        final VersionCheckThread vct = new VersionCheckThread();
        vct.start();
    }
    
    public static boolean isMipmaps() {
        return Config.gameSettings.mipmapLevels > 0;
    }
    
    public static int getMipmapLevels() {
        return Config.gameSettings.mipmapLevels;
    }
    
    public static int getMipmapType() {
        switch (Config.gameSettings.ofMipmapType) {
            case 0: {
                return 9986;
            }
            case 1: {
                return 9986;
            }
            case 2: {
                if (isMultiTexture()) {
                    return 9985;
                }
                return 9986;
            }
            case 3: {
                if (isMultiTexture()) {
                    return 9987;
                }
                return 9986;
            }
            default: {
                return 9986;
            }
        }
    }
    
    public static boolean isUseAlphaFunc() {
        final float alphaFuncLevel = getAlphaFuncLevel();
        return alphaFuncLevel > Config.DEF_ALPHA_FUNC_LEVEL + 1.0E-5f;
    }
    
    public static float getAlphaFuncLevel() {
        return Config.DEF_ALPHA_FUNC_LEVEL;
    }
    
    public static boolean isFogFancy() {
        return isFancyFogAvailable() && Config.gameSettings.ofFogType == 2;
    }
    
    public static boolean isFogFast() {
        return Config.gameSettings.ofFogType == 1;
    }
    
    public static boolean isFogOff() {
        return Config.gameSettings.ofFogType == 3;
    }
    
    public static float getFogStart() {
        return Config.gameSettings.ofFogStart;
    }
    
    public static void dbg(final String s) {
        Config.systemOut.print("[OptiFine] ");
        Config.systemOut.println(s);
    }
    
    public static void warn(final String s) {
        Config.systemOut.print("[OptiFine] [WARN] ");
        Config.systemOut.println(s);
    }
    
    public static void error(final String s) {
        Config.systemOut.print("[OptiFine] [ERROR] ");
        Config.systemOut.println(s);
    }
    
    public static void log(final String s) {
        dbg(s);
    }
    
    public static int getUpdatesPerFrame() {
        return Config.gameSettings.ofChunkUpdates;
    }
    
    public static boolean isDynamicUpdates() {
        return Config.gameSettings.ofChunkUpdatesDynamic;
    }
    
    public static boolean isRainFancy() {
        return (Config.gameSettings.ofRain == 0) ? Config.gameSettings.fancyGraphics : (Config.gameSettings.ofRain == 2);
    }
    
    public static boolean isRainOff() {
        return Config.gameSettings.ofRain == 3;
    }
    
    public static boolean isCloudsFancy() {
        return (Config.gameSettings.ofClouds != 0) ? (Config.gameSettings.ofClouds == 2) : ((isShaders() && !Shaders.shaderPackClouds.isDefault()) ? Shaders.shaderPackClouds.isFancy() : ((Config.texturePackClouds != 0) ? (Config.texturePackClouds == 2) : Config.gameSettings.fancyGraphics));
    }
    
    public static boolean isCloudsOff() {
        return (Config.gameSettings.ofClouds != 0) ? (Config.gameSettings.ofClouds == 3) : ((isShaders() && !Shaders.shaderPackClouds.isDefault()) ? Shaders.shaderPackClouds.isOff() : (Config.texturePackClouds != 0 && Config.texturePackClouds == 3));
    }
    
    public static void updateTexturePackClouds() {
        Config.texturePackClouds = 0;
        final IResourceManager rm = getResourceManager();
        if (rm != null) {
            try {
                final InputStream e = rm.getResource(new ResourceLocation("mcpatcher/color.properties")).getInputStream();
                if (e == null) {
                    return;
                }
                final Properties props = new Properties();
                props.load(e);
                e.close();
                String cloudStr = props.getProperty("clouds");
                if (cloudStr == null) {
                    return;
                }
                dbg("Texture pack clouds: " + cloudStr);
                cloudStr = cloudStr.toLowerCase();
                if (cloudStr.equals("fast")) {
                    Config.texturePackClouds = 1;
                }
                if (cloudStr.equals("fancy")) {
                    Config.texturePackClouds = 2;
                }
                if (cloudStr.equals("off")) {
                    Config.texturePackClouds = 3;
                }
            }
            catch (Exception ex) {}
        }
    }
    
    public static void setModelManager(final ModelManager modelManager) {
        Config.modelManager = modelManager;
    }
    
    public static ModelManager getModelManager() {
        return Config.modelManager;
    }
    
    public static boolean isTreesFancy() {
        return (Config.gameSettings.ofTrees == 0) ? Config.gameSettings.fancyGraphics : (Config.gameSettings.ofTrees != 1);
    }
    
    public static boolean isTreesSmart() {
        return Config.gameSettings.ofTrees == 4;
    }
    
    public static boolean isCullFacesLeaves() {
        return (Config.gameSettings.ofTrees == 0) ? (!Config.gameSettings.fancyGraphics) : (Config.gameSettings.ofTrees == 4);
    }
    
    public static boolean isDroppedItemsFancy() {
        return (Config.gameSettings.ofDroppedItems == 0) ? Config.gameSettings.fancyGraphics : (Config.gameSettings.ofDroppedItems == 2);
    }
    
    public static int limit(final int val, final int min, final int max) {
        return (val < min) ? min : ((val > max) ? max : val);
    }
    
    public static float limit(final float val, final float min, final float max) {
        return (val < min) ? min : ((val > max) ? max : val);
    }
    
    public static double limit(final double val, final double min, final double max) {
        return (val < min) ? min : ((val > max) ? max : val);
    }
    
    public static float limitTo1(final float val) {
        return (val < 0.0f) ? 0.0f : ((val > 1.0f) ? 1.0f : val);
    }
    
    public static boolean isAnimatedWater() {
        return Config.gameSettings.ofAnimatedWater != 2;
    }
    
    public static boolean isGeneratedWater() {
        return Config.gameSettings.ofAnimatedWater == 1;
    }
    
    public static boolean isAnimatedPortal() {
        return Config.gameSettings.ofAnimatedPortal;
    }
    
    public static boolean isAnimatedLava() {
        return Config.gameSettings.ofAnimatedLava != 2;
    }
    
    public static boolean isGeneratedLava() {
        return Config.gameSettings.ofAnimatedLava == 1;
    }
    
    public static boolean isAnimatedFire() {
        return Config.gameSettings.ofAnimatedFire;
    }
    
    public static boolean isAnimatedRedstone() {
        return Config.gameSettings.ofAnimatedRedstone;
    }
    
    public static boolean isAnimatedExplosion() {
        return Config.gameSettings.ofAnimatedExplosion;
    }
    
    public static boolean isAnimatedFlame() {
        return Config.gameSettings.ofAnimatedFlame;
    }
    
    public static boolean isAnimatedSmoke() {
        return Config.gameSettings.ofAnimatedSmoke;
    }
    
    public static boolean isVoidParticles() {
        return Config.gameSettings.ofVoidParticles;
    }
    
    public static boolean isWaterParticles() {
        return Config.gameSettings.ofWaterParticles;
    }
    
    public static boolean isRainSplash() {
        return Config.gameSettings.ofRainSplash;
    }
    
    public static boolean isPortalParticles() {
        return Config.gameSettings.ofPortalParticles;
    }
    
    public static boolean isPotionParticles() {
        return Config.gameSettings.ofPotionParticles;
    }
    
    public static boolean isFireworkParticles() {
        return Config.gameSettings.ofFireworkParticles;
    }
    
    public static float getAmbientOcclusionLevel() {
        return Config.gameSettings.ofAoLevel;
    }
    
    private static Method getMethod(final Class cls, final String methodName, final Object[] params) {
        final Method[] methods = cls.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            final Method m = methods[i];
            if (m.getName().equals(methodName) && m.getParameterTypes().length == params.length) {
                return m;
            }
        }
        warn("No method found for: " + cls.getName() + "." + methodName + "(" + arrayToString(params) + ")");
        return null;
    }
    
    public static String arrayToString(final Object[] arr) {
        if (arr == null) {
            return "";
        }
        final StringBuffer buf = new StringBuffer(arr.length * 5);
        for (int i = 0; i < arr.length; ++i) {
            final Object obj = arr[i];
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(String.valueOf(obj));
        }
        return buf.toString();
    }
    
    public static String arrayToString(final int[] arr) {
        if (arr == null) {
            return "";
        }
        final StringBuffer buf = new StringBuffer(arr.length * 5);
        for (int i = 0; i < arr.length; ++i) {
            final int x = arr[i];
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(String.valueOf(x));
        }
        return buf.toString();
    }
    
    public static Minecraft getMinecraft() {
        return Config.minecraft;
    }
    
    public static TextureManager getTextureManager() {
        return Config.minecraft.getTextureManager();
    }
    
    public static IResourceManager getResourceManager() {
        return Config.minecraft.getResourceManager();
    }
    
    public static InputStream getResourceStream(final ResourceLocation location) throws IOException {
        return getResourceStream(Config.minecraft.getResourceManager(), location);
    }
    
    public static InputStream getResourceStream(final IResourceManager resourceManager, final ResourceLocation location) throws IOException {
        final IResource res = resourceManager.getResource(location);
        return (res == null) ? null : res.getInputStream();
    }
    
    public static IResource getResource(final ResourceLocation location) throws IOException {
        return Config.minecraft.getResourceManager().getResource(location);
    }
    
    public static boolean hasResource(final ResourceLocation location) {
        try {
            final IResource e = getResource(location);
            return e != null;
        }
        catch (IOException var2) {
            return false;
        }
    }
    
    public static boolean hasResource(final IResourceManager resourceManager, final ResourceLocation location) {
        try {
            final IResource e = resourceManager.getResource(location);
            return e != null;
        }
        catch (IOException var3) {
            return false;
        }
    }
    
    public static IResourcePack[] getResourcePacks() {
        final ResourcePackRepository rep = Config.minecraft.getResourcePackRepository();
        final List entries = rep.getRepositoryEntries();
        final ArrayList list = new ArrayList();
        for (final ResourcePackRepository.Entry entry : entries) {
            list.add(entry.getResourcePack());
        }
        if (rep.getResourcePackInstance() != null) {
            list.add(rep.getResourcePackInstance());
        }
        final IResourcePack[] rps2 = list.toArray(new IResourcePack[list.size()]);
        return rps2;
    }
    
    public static String getResourcePackNames() {
        if (Config.minecraft == null) {
            return "";
        }
        if (Config.minecraft.getResourcePackRepository() == null) {
            return "";
        }
        final IResourcePack[] rps = getResourcePacks();
        if (rps.length <= 0) {
            return getDefaultResourcePack().getPackName();
        }
        final String[] names = new String[rps.length];
        for (int nameStr = 0; nameStr < rps.length; ++nameStr) {
            names[nameStr] = rps[nameStr].getPackName();
        }
        final String var3 = arrayToString(names);
        return var3;
    }
    
    public static DefaultResourcePack getDefaultResourcePack() {
        if (Config.defaultResourcePack == null) {
            final Minecraft mc = Minecraft.getMinecraft();
            try {
                final Field[] repository = mc.getClass().getDeclaredFields();
                for (int i = 0; i < repository.length; ++i) {
                    final Field field = repository[i];
                    if (field.getType() == DefaultResourcePack.class) {
                        field.setAccessible(true);
                        Config.defaultResourcePack = (DefaultResourcePack)field.get(mc);
                        break;
                    }
                }
            }
            catch (Exception var4) {
                warn("Error getting default resource pack: " + var4.getClass().getName() + ": " + var4.getMessage());
            }
            if (Config.defaultResourcePack == null) {
                final ResourcePackRepository var5 = mc.getResourcePackRepository();
                if (var5 != null) {
                    Config.defaultResourcePack = (DefaultResourcePack)var5.rprDefaultResourcePack;
                }
            }
        }
        return Config.defaultResourcePack;
    }
    
    public static boolean isFromDefaultResourcePack(final ResourceLocation loc) {
        final IResourcePack rp = getDefiningResourcePack(loc);
        return rp == getDefaultResourcePack();
    }
    
    public static IResourcePack getDefiningResourcePack(final ResourceLocation loc) {
        final IResourcePack[] rps = getResourcePacks();
        for (int i = rps.length - 1; i >= 0; --i) {
            final IResourcePack rp = rps[i];
            if (rp.resourceExists(loc)) {
                return rp;
            }
        }
        if (getDefaultResourcePack().resourceExists(loc)) {
            return getDefaultResourcePack();
        }
        return null;
    }
    
    public static RenderGlobal getRenderGlobal() {
        return (Config.minecraft == null) ? null : Config.minecraft.renderGlobal;
    }
    
    public static boolean isBetterGrass() {
        return Config.gameSettings.ofBetterGrass != 3;
    }
    
    public static boolean isBetterGrassFancy() {
        return Config.gameSettings.ofBetterGrass == 2;
    }
    
    public static boolean isWeatherEnabled() {
        return Config.gameSettings.ofWeather;
    }
    
    public static boolean isSkyEnabled() {
        return Config.gameSettings.ofSky;
    }
    
    public static boolean isSunMoonEnabled() {
        return Config.gameSettings.ofSunMoon;
    }
    
    public static boolean isVignetteEnabled() {
        return (Config.gameSettings.ofVignette == 0) ? Config.gameSettings.fancyGraphics : (Config.gameSettings.ofVignette == 2);
    }
    
    public static boolean isStarsEnabled() {
        return Config.gameSettings.ofStars;
    }
    
    public static void sleep(final long ms) {
        try {
            Thread.currentThread();
            Thread.sleep(ms);
        }
        catch (InterruptedException var3) {
            var3.printStackTrace();
        }
    }
    
    public static boolean isTimeDayOnly() {
        return Config.gameSettings.ofTime == 1;
    }
    
    public static boolean isTimeDefault() {
        return Config.gameSettings.ofTime == 0;
    }
    
    public static boolean isTimeNightOnly() {
        return Config.gameSettings.ofTime == 2;
    }
    
    public static boolean isClearWater() {
        return Config.gameSettings.ofClearWater;
    }
    
    public static int getAnisotropicFilterLevel() {
        return Config.gameSettings.ofAfLevel;
    }
    
    public static boolean isAnisotropicFiltering() {
        return getAnisotropicFilterLevel() > 1;
    }
    
    public static int getAntialiasingLevel() {
        return Config.antialiasingLevel;
    }
    
    public static boolean isAntialiasing() {
        return getAntialiasingLevel() > 0;
    }
    
    public static boolean isAntialiasingConfigured() {
        return getGameSettings().ofAaLevel > 0;
    }
    
    public static boolean isMultiTexture() {
        return getAnisotropicFilterLevel() > 1 || getAntialiasingLevel() > 0;
    }
    
    public static boolean between(final int val, final int min, final int max) {
        return val >= min && val <= max;
    }
    
    public static boolean isDrippingWaterLava() {
        return Config.gameSettings.ofDrippingWaterLava;
    }
    
    public static boolean isBetterSnow() {
        return Config.gameSettings.ofBetterSnow;
    }
    
    public static Dimension getFullscreenDimension() {
        if (Config.desktopDisplayMode == null) {
            return null;
        }
        if (Config.gameSettings == null) {
            return new Dimension(Config.desktopDisplayMode.getWidth(), Config.desktopDisplayMode.getHeight());
        }
        final String dimStr = Config.gameSettings.ofFullscreenMode;
        if (dimStr.equals("Default")) {
            return new Dimension(Config.desktopDisplayMode.getWidth(), Config.desktopDisplayMode.getHeight());
        }
        final String[] dimStrs = tokenize(dimStr, " x");
        return (dimStrs.length < 2) ? new Dimension(Config.desktopDisplayMode.getWidth(), Config.desktopDisplayMode.getHeight()) : new Dimension(parseInt(dimStrs[0], -1), parseInt(dimStrs[1], -1));
    }
    
    public static int parseInt(String str, final int defVal) {
        try {
            if (str == null) {
                return defVal;
            }
            str = str.trim();
            return Integer.parseInt(str);
        }
        catch (NumberFormatException var3) {
            return defVal;
        }
    }
    
    public static float parseFloat(String str, final float defVal) {
        try {
            if (str == null) {
                return defVal;
            }
            str = str.trim();
            return Float.parseFloat(str);
        }
        catch (NumberFormatException var3) {
            return defVal;
        }
    }
    
    public static boolean parseBoolean(String str, final boolean defVal) {
        try {
            if (str == null) {
                return defVal;
            }
            str = str.trim();
            return Boolean.parseBoolean(str);
        }
        catch (NumberFormatException var3) {
            return defVal;
        }
    }
    
    public static String[] tokenize(final String str, final String delim) {
        final StringTokenizer tok = new StringTokenizer(str, delim);
        final ArrayList list = new ArrayList();
        while (tok.hasMoreTokens()) {
            final String strs = tok.nextToken();
            list.add(strs);
        }
        final String[] strs2 = list.toArray(new String[list.size()]);
        return strs2;
    }
    
    public static DisplayMode getDesktopDisplayMode() {
        return Config.desktopDisplayMode;
    }
    
    public static DisplayMode[] getFullscreenDisplayModes() {
        try {
            final DisplayMode[] e = Display.getAvailableDisplayModes();
            final ArrayList list = new ArrayList();
            for (int fsModes = 0; fsModes < e.length; ++fsModes) {
                final DisplayMode comp = e[fsModes];
                if (Config.desktopDisplayMode == null || (comp.getBitsPerPixel() == Config.desktopDisplayMode.getBitsPerPixel() && comp.getFrequency() == Config.desktopDisplayMode.getFrequency())) {
                    list.add(comp);
                }
            }
            final DisplayMode[] var5 = list.toArray(new DisplayMode[list.size()]);
            final Comparator var6 = new Comparator() {
                @Override
                public int compare(final Object o1, final Object o2) {
                    final DisplayMode dm1 = (DisplayMode)o1;
                    final DisplayMode dm2 = (DisplayMode)o2;
                    return (dm1.getWidth() != dm2.getWidth()) ? (dm2.getWidth() - dm1.getWidth()) : ((dm1.getHeight() != dm2.getHeight()) ? (dm2.getHeight() - dm1.getHeight()) : 0);
                }
            };
            Arrays.sort(var5, var6);
            return var5;
        }
        catch (Exception var7) {
            var7.printStackTrace();
            return new DisplayMode[] { Config.desktopDisplayMode };
        }
    }
    
    public static String[] getFullscreenModes() {
        final DisplayMode[] modes = getFullscreenDisplayModes();
        final String[] names = new String[modes.length];
        for (int i = 0; i < modes.length; ++i) {
            final DisplayMode mode = modes[i];
            final String name = mode.getWidth() + "x" + mode.getHeight();
            names[i] = name;
        }
        return names;
    }
    
    public static DisplayMode getDisplayMode(final Dimension dim) throws LWJGLException {
        final DisplayMode[] modes = Display.getAvailableDisplayModes();
        for (int i = 0; i < modes.length; ++i) {
            final DisplayMode dm = modes[i];
            if (dm.getWidth() == dim.width && dm.getHeight() == dim.height && (Config.desktopDisplayMode == null || (dm.getBitsPerPixel() == Config.desktopDisplayMode.getBitsPerPixel() && dm.getFrequency() == Config.desktopDisplayMode.getFrequency()))) {
                return dm;
            }
        }
        return Config.desktopDisplayMode;
    }
    
    public static boolean isAnimatedTerrain() {
        return Config.gameSettings.ofAnimatedTerrain;
    }
    
    public static boolean isAnimatedTextures() {
        return Config.gameSettings.ofAnimatedTextures;
    }
    
    public static boolean isSwampColors() {
        return Config.gameSettings.ofSwampColors;
    }
    
    public static boolean isRandomMobs() {
        return Config.gameSettings.ofRandomMobs;
    }
    
    public static void checkGlError(final String loc) {
        final int i = GL11.glGetError();
        if (i != 0) {
            final String text = GLU.gluErrorString(i);
            error("OpenGlError: " + i + " (" + text + "), at: " + loc);
        }
    }
    
    public static boolean isSmoothBiomes() {
        return Config.gameSettings.ofSmoothBiomes;
    }
    
    public static boolean isCustomColors() {
        return Config.gameSettings.ofCustomColors;
    }
    
    public static boolean isCustomSky() {
        return Config.gameSettings.ofCustomSky;
    }
    
    public static boolean isCustomFonts() {
        return Config.gameSettings.ofCustomFonts;
    }
    
    public static boolean isShowCapes() {
        return Config.gameSettings.ofShowCapes;
    }
    
    public static boolean isConnectedTextures() {
        return Config.gameSettings.ofConnectedTextures != 3;
    }
    
    public static boolean isNaturalTextures() {
        return Config.gameSettings.ofNaturalTextures;
    }
    
    public static boolean isConnectedTexturesFancy() {
        return Config.gameSettings.ofConnectedTextures == 2;
    }
    
    public static boolean isFastRender() {
        return Config.gameSettings.ofFastRender;
    }
    
    public static boolean isTranslucentBlocksFancy() {
        return (Config.gameSettings.ofTranslucentBlocks == 0) ? Config.gameSettings.fancyGraphics : (Config.gameSettings.ofTranslucentBlocks == 2);
    }
    
    public static boolean isShaders() {
        return Shaders.shaderPackLoaded;
    }
    
    public static String[] readLines(final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        return readLines(fis);
    }
    
    public static String[] readLines(final InputStream is) throws IOException {
        final ArrayList list = new ArrayList();
        final InputStreamReader isr = new InputStreamReader(is, "ASCII");
        final BufferedReader br = new BufferedReader(isr);
        while (true) {
            final String lines = br.readLine();
            if (lines == null) {
                break;
            }
            list.add(lines);
        }
        final String[] lines2 = list.toArray(new String[list.size()]);
        return lines2;
    }
    
    public static String readFile(final File file) throws IOException {
        final FileInputStream fin = new FileInputStream(file);
        return readInputStream(fin, "ASCII");
    }
    
    public static String readInputStream(final InputStream in) throws IOException {
        return readInputStream(in, "ASCII");
    }
    
    public static String readInputStream(final InputStream in, final String encoding) throws IOException {
        final InputStreamReader inr = new InputStreamReader(in, encoding);
        final BufferedReader br = new BufferedReader(inr);
        final StringBuffer sb = new StringBuffer();
        while (true) {
            final String line = br.readLine();
            if (line == null) {
                break;
            }
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static byte[] readAll(final InputStream is) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] buf = new byte[1024];
        while (true) {
            final int bytes = is.read(buf);
            if (bytes < 0) {
                break;
            }
            baos.write(buf, 0, bytes);
        }
        is.close();
        final byte[] bytes2 = baos.toByteArray();
        return bytes2;
    }
    
    public static GameSettings getGameSettings() {
        return Config.gameSettings;
    }
    
    public static String getNewRelease() {
        return Config.newRelease;
    }
    
    public static void setNewRelease(final String newRelease) {
        Config.newRelease = newRelease;
    }
    
    public static int compareRelease(final String rel1, final String rel2) {
        final String[] rels1 = splitRelease(rel1);
        final String[] rels2 = splitRelease(rel2);
        final String branch1 = rels1[0];
        final String branch2 = rels2[0];
        if (!branch1.equals(branch2)) {
            return branch1.compareTo(branch2);
        }
        final int rev1 = parseInt(rels1[1], -1);
        final int rev2 = parseInt(rels2[1], -1);
        if (rev1 != rev2) {
            return rev1 - rev2;
        }
        final String suf1 = rels1[2];
        final String suf2 = rels2[2];
        if (!suf1.equals(suf2)) {
            if (suf1.isEmpty()) {
                return 1;
            }
            if (suf2.isEmpty()) {
                return -1;
            }
        }
        return suf1.compareTo(suf2);
    }
    
    private static String[] splitRelease(final String relStr) {
        if (relStr == null || relStr.length() <= 0) {
            return new String[] { "", "", "" };
        }
        final Pattern p = Pattern.compile("([A-Z])([0-9]+)(.*)");
        final Matcher m = p.matcher(relStr);
        if (!m.matches()) {
            return new String[] { "", "", "" };
        }
        final String branch = normalize(m.group(1));
        final String revision = normalize(m.group(2));
        final String suffix = normalize(m.group(3));
        return new String[] { branch, revision, suffix };
    }
    
    public static int intHash(int x) {
        x = (x ^ 0x3D ^ x >> 16);
        x += x << 3;
        x ^= x >> 4;
        x *= 668265261;
        x ^= x >> 15;
        return x;
    }
    
    public static int getRandom(final BlockPos blockPos, final int face) {
        int rand = intHash(face + 37);
        rand = intHash(rand + blockPos.getX());
        rand = intHash(rand + blockPos.getZ());
        rand = intHash(rand + blockPos.getY());
        return rand;
    }
    
    public static WorldServer getWorldServer() {
        if (Config.minecraft == null) {
            return null;
        }
        final WorldClient world = Config.minecraft.theWorld;
        if (world == null) {
            return null;
        }
        if (!Config.minecraft.isIntegratedServerRunning()) {
            return null;
        }
        final IntegratedServer is = Config.minecraft.getIntegratedServer();
        if (is == null) {
            return null;
        }
        final WorldProvider wp = world.provider;
        if (wp == null) {
            return null;
        }
        final int wd = wp.getDimensionId();
        try {
            final WorldServer e = is.worldServerForDimension(wd);
            return e;
        }
        catch (NullPointerException var5) {
            return null;
        }
    }
    
    public static int getAvailableProcessors() {
        return Config.availableProcessors;
    }
    
    public static void updateAvailableProcessors() {
        Config.availableProcessors = Runtime.getRuntime().availableProcessors();
    }
    
    public static boolean isSingleProcessor() {
        return getAvailableProcessors() <= 1;
    }
    
    public static boolean isSmoothWorld() {
        return Config.gameSettings.ofSmoothWorld;
    }
    
    public static boolean isLazyChunkLoading() {
        return isSingleProcessor() && Config.gameSettings.ofLazyChunkLoading;
    }
    
    public static boolean isDynamicFov() {
        return Config.gameSettings.ofDynamicFov;
    }
    
    public static int getChunkViewDistance() {
        if (Config.gameSettings == null) {
            return 10;
        }
        final int chunkDistance = Config.gameSettings.renderDistanceChunks;
        return chunkDistance;
    }
    
    public static boolean equals(final Object o1, final Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }
    
    public static String normalize(final String s) {
        return (s == null) ? "" : s;
    }
    
    public static void checkDisplaySettings() {
        final int samples = getAntialiasingLevel();
        if (samples > 0) {
            final DisplayMode displayMode = Display.getDisplayMode();
            dbg("FSAA Samples: " + samples);
            try {
                Display.destroy();
                Display.setDisplayMode(displayMode);
                Display.create(new PixelFormat().withDepthBits(24).withSamples(samples));
                Display.setResizable(false);
                Display.setResizable(true);
            }
            catch (LWJGLException var15) {
                warn("Error setting FSAA: " + samples + "x");
                var15.printStackTrace();
                try {
                    Display.setDisplayMode(displayMode);
                    Display.create(new PixelFormat().withDepthBits(24));
                    Display.setResizable(false);
                    Display.setResizable(true);
                }
                catch (LWJGLException var16) {
                    var16.printStackTrace();
                    try {
                        Display.setDisplayMode(displayMode);
                        Display.create();
                        Display.setResizable(false);
                        Display.setResizable(true);
                    }
                    catch (LWJGLException var17) {
                        var17.printStackTrace();
                    }
                }
            }
            if (!Minecraft.isRunningOnMac && getDefaultResourcePack() != null) {
                InputStream var18 = null;
                InputStream var19 = null;
                try {
                    var18 = getDefaultResourcePack().func_152780_c(new ResourceLocation("icons/icon_16x16.png"));
                    var19 = getDefaultResourcePack().func_152780_c(new ResourceLocation("icons/icon_32x32.png"));
                    if (var18 != null && var19 != null) {
                        Display.setIcon(new ByteBuffer[] { readIconImage(var18), readIconImage(var19) });
                    }
                }
                catch (IOException var20) {
                    warn("Error setting window icon: " + var20.getClass().getName() + ": " + var20.getMessage());
                    return;
                }
                finally {
                    IOUtils.closeQuietly(var18);
                    IOUtils.closeQuietly(var19);
                }
                IOUtils.closeQuietly(var18);
                IOUtils.closeQuietly(var19);
            }
        }
    }
    
    private static ByteBuffer readIconImage(final InputStream is) throws IOException {
        final BufferedImage var2 = ImageIO.read(is);
        final int[] var3 = var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), null, 0, var2.getWidth());
        final ByteBuffer var4 = ByteBuffer.allocate(4 * var3.length);
        final int[] var5 = var3;
        for (int var6 = var3.length, var7 = 0; var7 < var6; ++var7) {
            final int var8 = var5[var7];
            var4.putInt(var8 << 8 | (var8 >> 24 & 0xFF));
        }
        var4.flip();
        return var4;
    }
    
    public static void checkDisplayMode() {
        try {
            if (Config.minecraft.isFullScreen()) {
                if (Config.fullscreenModeChecked) {
                    return;
                }
                Config.fullscreenModeChecked = true;
                Config.desktopModeChecked = false;
                final DisplayMode e = Display.getDisplayMode();
                final Dimension dim = getFullscreenDimension();
                if (dim == null) {
                    return;
                }
                if (e.getWidth() == dim.width && e.getHeight() == dim.height) {
                    return;
                }
                final DisplayMode newMode = getDisplayMode(dim);
                if (newMode == null) {
                    return;
                }
                Display.setDisplayMode(newMode);
                Config.minecraft.displayWidth = Display.getDisplayMode().getWidth();
                Config.minecraft.displayHeight = Display.getDisplayMode().getHeight();
                if (Config.minecraft.displayWidth <= 0) {
                    Config.minecraft.displayWidth = 1;
                }
                if (Config.minecraft.displayHeight <= 0) {
                    Config.minecraft.displayHeight = 1;
                }
                if (Config.minecraft.currentScreen != null) {
                    final ScaledResolution sr = new ScaledResolution(Config.minecraft, Config.minecraft.displayWidth, Config.minecraft.displayHeight);
                    final int sw = sr.getScaledWidth();
                    final int sh = sr.getScaledHeight();
                    Config.minecraft.currentScreen.setWorldAndResolution(Config.minecraft, sw, sh);
                }
                Config.minecraft.loadingScreen = new LoadingScreenRenderer(Config.minecraft);
                updateFramebufferSize();
                Display.setFullscreen(true);
                Config.minecraft.gameSettings.updateVSync();
                GlStateManager.func_179098_w();
            }
            else {
                if (Config.desktopModeChecked) {
                    return;
                }
                Config.desktopModeChecked = true;
                Config.fullscreenModeChecked = false;
                Config.minecraft.gameSettings.updateVSync();
                Display.update();
                GlStateManager.func_179098_w();
                Display.setResizable(false);
                Display.setResizable(true);
            }
        }
        catch (Exception var6) {
            var6.printStackTrace();
        }
    }
    
    public static void updateFramebufferSize() {
        Config.minecraft.getFramebuffer().createBindFramebuffer(Config.minecraft.displayWidth, Config.minecraft.displayHeight);
        if (Config.minecraft.entityRenderer != null) {
            Config.minecraft.entityRenderer.updateShaderGroupSize(Config.minecraft.displayWidth, Config.minecraft.displayHeight);
        }
    }
    
    public static Object[] addObjectToArray(final Object[] arr, final Object obj) {
        if (arr == null) {
            throw new NullPointerException("The given array is NULL");
        }
        final int arrLen = arr.length;
        final int newLen = arrLen + 1;
        final Object[] newArr = (Object[])Array.newInstance(arr.getClass().getComponentType(), newLen);
        System.arraycopy(arr, 0, newArr, 0, arrLen);
        newArr[arrLen] = obj;
        return newArr;
    }
    
    public static Object[] addObjectToArray(final Object[] arr, final Object obj, final int index) {
        final ArrayList list = new ArrayList((Collection<? extends E>)Arrays.asList(arr));
        list.add(index, obj);
        final Object[] newArr = (Object[])Array.newInstance(arr.getClass().getComponentType(), list.size());
        return list.toArray(newArr);
    }
    
    public static Object[] addObjectsToArray(final Object[] arr, final Object[] objs) {
        if (arr == null) {
            throw new NullPointerException("The given array is NULL");
        }
        if (objs.length == 0) {
            return arr;
        }
        final int arrLen = arr.length;
        final int newLen = arrLen + objs.length;
        final Object[] newArr = (Object[])Array.newInstance(arr.getClass().getComponentType(), newLen);
        System.arraycopy(arr, 0, newArr, 0, arrLen);
        System.arraycopy(objs, 0, newArr, arrLen, objs.length);
        return newArr;
    }
    
    public static boolean isCustomItems() {
        return Config.gameSettings.ofCustomItems;
    }
    
    public static void drawFps() {
        final Minecraft var10000 = Config.minecraft;
        final int fps = Minecraft.func_175610_ah();
        final String updates = getUpdates(Config.minecraft.debug);
        final int renderersActive = Config.minecraft.renderGlobal.getCountActiveRenderers();
        final int entities = Config.minecraft.renderGlobal.getCountEntitiesRendered();
        final int tileEntities = Config.minecraft.renderGlobal.getCountTileEntitiesRendered();
        final String fpsStr = fps + " fps, C: " + renderersActive + ", E: " + entities + "+" + tileEntities + ", U: " + updates;
        Config.minecraft.fontRendererObj.drawString(fpsStr, 2, 2, -2039584);
    }
    
    private static String getUpdates(final String str) {
        final int pos1 = str.indexOf(40);
        if (pos1 < 0) {
            return "";
        }
        final int pos2 = str.indexOf(32, pos1);
        return (pos2 < 0) ? "" : str.substring(pos1 + 1, pos2);
    }
    
    public static int getBitsOs() {
        final String progFiles86 = System.getenv("ProgramFiles(X86)");
        return (progFiles86 != null) ? 64 : 32;
    }
    
    public static int getBitsJre() {
        final String[] propNames = { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
        for (int i = 0; i < propNames.length; ++i) {
            final String propName = propNames[i];
            final String propVal = System.getProperty(propName);
            if (propVal != null && propVal.contains("64")) {
                return 64;
            }
        }
        return 32;
    }
    
    public static boolean isNotify64BitJava() {
        return Config.notify64BitJava;
    }
    
    public static void setNotify64BitJava(final boolean flag) {
        Config.notify64BitJava = flag;
    }
    
    public static boolean isConnectedModels() {
        return false;
    }
    
    public static String fillLeft(String s, final int len, final char fillChar) {
        if (s == null) {
            s = "";
        }
        if (s.length() >= len) {
            return s;
        }
        final StringBuffer buf = new StringBuffer(s);
        while (buf.length() < len - s.length()) {
            buf.append(fillChar);
        }
        return String.valueOf(buf.toString()) + s;
    }
    
    public static String fillRight(String s, final int len, final char fillChar) {
        if (s == null) {
            s = "";
        }
        if (s.length() >= len) {
            return s;
        }
        final StringBuffer buf = new StringBuffer(s);
        while (buf.length() < len) {
            buf.append(fillChar);
        }
        return buf.toString();
    }
    
    public static void showGuiMessage(final String line1, final String line2) {
        final GuiMessage gui = new GuiMessage(Config.minecraft.currentScreen, line1, line2);
        Config.minecraft.displayGuiScreen(gui);
    }
    
    public static int[] addIntToArray(final int[] intArray, final int intValue) {
        return addIntsToArray(intArray, new int[] { intValue });
    }
    
    public static int[] addIntsToArray(final int[] intArray, final int[] copyFrom) {
        if (intArray != null && copyFrom != null) {
            final int arrLen = intArray.length;
            final int newLen = arrLen + copyFrom.length;
            final int[] newArray = new int[newLen];
            System.arraycopy(intArray, 0, newArray, 0, arrLen);
            for (int index = 0; index < copyFrom.length; ++index) {
                newArray[index + arrLen] = copyFrom[index];
            }
            return newArray;
        }
        throw new NullPointerException("The given array is NULL");
    }
    
    public static DynamicTexture getMojangLogoTexture(final DynamicTexture texDefault) {
        try {
            final ResourceLocation e = new ResourceLocation("textures/gui/title/mojang.png");
            final InputStream in = getResourceStream(e);
            if (in == null) {
                return texDefault;
            }
            final BufferedImage bi = ImageIO.read(in);
            if (bi == null) {
                return texDefault;
            }
            final DynamicTexture dt = new DynamicTexture(bi);
            return dt;
        }
        catch (Exception var5) {
            warn(String.valueOf(var5.getClass().getName()) + ": " + var5.getMessage());
            return texDefault;
        }
    }
    
    public static void writeFile(final File file, final String str) throws IOException {
        final FileOutputStream fos = new FileOutputStream(file);
        final byte[] bytes = str.getBytes("ASCII");
        fos.write(bytes);
        fos.close();
    }
    
    public static TextureMap getTextureMap() {
        return getMinecraft().getTextureMapBlocks();
    }
    
    public static boolean isDynamicLights() {
        return Config.gameSettings.ofDynamicLights != 3;
    }
    
    public static boolean isDynamicLightsFast() {
        return Config.gameSettings.ofDynamicLights == 1;
    }
    
    public static boolean isDynamicHandLight() {
        return isDynamicLights() && (!isShaders() || Shaders.isDynamicHandLight());
    }
}
