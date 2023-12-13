/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import optfine.VersionCheckThread;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class Config {
    public static final String OF_NAME = "OptiFine";
    public static final String MC_VERSION = "1.8.8";
    public static final String OF_EDITION = "HD_U";
    public static final String OF_RELEASE = "E1";
    public static final String VERSION = "OptiFine_1.8.8_HD_U_E1";
    private static String newRelease = null;
    private static boolean notify64BitJava = false;
    public static String openGlVersion = null;
    public static String openGlRenderer = null;
    public static String openGlVendor = null;
    public static boolean fancyFogAvailable = false;
    public static boolean occlusionAvailable = false;
    private static GameSettings gameSettings = null;
    private static Minecraft minecraft = null;
    private static boolean initialized = false;
    private static Thread minecraftThread = null;
    private static DisplayMode desktopDisplayMode = null;
    private static int antialiasingLevel = 0;
    private static int availableProcessors = 0;
    public static boolean zoomMode = false;
    private static int texturePackClouds = 0;
    public static boolean waterOpacityChanged = false;
    private static boolean fullscreenModeChecked = false;
    private static boolean desktopModeChecked = false;
    private static PrintStream systemOut = new PrintStream(new FileOutputStream(FileDescriptor.out));
    public static final Boolean DEF_FOG_FANCY = true;
    public static final Float DEF_FOG_START = Float.valueOf(0.2f);
    public static final Boolean DEF_OPTIMIZE_RENDER_DISTANCE = false;
    public static final Boolean DEF_OCCLUSION_ENABLED = false;
    public static final Integer DEF_MIPMAP_LEVEL = 0;
    public static final Integer DEF_MIPMAP_TYPE = 9984;
    public static final Float DEF_ALPHA_FUNC_LEVEL = Float.valueOf(0.1f);
    public static final Boolean DEF_LOAD_CHUNKS_FAR = false;
    public static final Integer DEF_PRELOADED_CHUNKS = 0;
    public static final Integer DEF_CHUNKS_LIMIT = 25;
    public static final Integer DEF_UPDATES_PER_FRAME = 3;
    public static final Boolean DEF_DYNAMIC_UPDATES = false;
    private static long lastActionTime = System.currentTimeMillis();

    public static String getVersion() {
        return "HD_U_E1";
    }

    public static void initGameSettings(GameSettings p_initGameSettings_0_) {
        gameSettings = p_initGameSettings_0_;
        minecraft = Minecraft.getMinecraft();
        desktopDisplayMode = Display.getDesktopDisplayMode();
        Config.updateAvailableProcessors();
    }

    public static void initDisplay() {
        Config.checkInitialized();
        antialiasingLevel = Config.gameSettings.ofAaLevel;
        Config.checkDisplaySettings();
        Config.checkDisplayMode();
        minecraftThread = Thread.currentThread();
        Config.updateThreadPriorities();
    }

    public static void checkInitialized() {
        if (!initialized && Display.isCreated()) {
            initialized = true;
            Config.checkOpenGlCaps();
            Config.startVersionCheckThread();
        }
    }

    private static void checkOpenGlCaps() {
        Config.log("");
        Config.log(Config.getVersion());
        Config.log("" + new Date());
        Config.log("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
        Config.log("Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
        Config.log("VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
        Config.log("LWJGL: " + Sys.getVersion());
        openGlVersion = GL11.glGetString(7938);
        openGlRenderer = GL11.glGetString(7937);
        openGlVendor = GL11.glGetString(7936);
        Config.log("OpenGL: " + openGlRenderer + ", version " + openGlVersion + ", " + openGlVendor);
        Config.log("OpenGL Version: " + Config.getOpenGlVersionString());
        if (!GLContext.getCapabilities().OpenGL12) {
            Config.log("OpenGL Mipmap levels: Not available (GL12.GL_TEXTURE_MAX_LEVEL)");
        }
        if (!(fancyFogAvailable = GLContext.getCapabilities().GL_NV_fog_distance)) {
            Config.log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");
        }
        if (!(occlusionAvailable = GLContext.getCapabilities().GL_ARB_occlusion_query)) {
            Config.log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");
        }
        int i2 = Minecraft.getGLMaximumTextureSize();
        Config.dbg("Maximum texture size: " + i2 + "x" + i2);
    }

    public static boolean isFancyFogAvailable() {
        return fancyFogAvailable;
    }

    public static boolean isOcclusionAvailable() {
        return occlusionAvailable;
    }

    public static String getOpenGlVersionString() {
        int i2 = Config.getOpenGlVersion();
        String s2 = "" + i2 / 10 + "." + i2 % 10;
        return s2;
    }

    private static int getOpenGlVersion() {
        return !GLContext.getCapabilities().OpenGL11 ? 10 : (!GLContext.getCapabilities().OpenGL12 ? 11 : (!GLContext.getCapabilities().OpenGL13 ? 12 : (!GLContext.getCapabilities().OpenGL14 ? 13 : (!GLContext.getCapabilities().OpenGL15 ? 14 : (!GLContext.getCapabilities().OpenGL20 ? 15 : (!GLContext.getCapabilities().OpenGL21 ? 20 : (!GLContext.getCapabilities().OpenGL30 ? 21 : (!GLContext.getCapabilities().OpenGL31 ? 30 : (!GLContext.getCapabilities().OpenGL32 ? 31 : (!GLContext.getCapabilities().OpenGL33 ? 32 : (!GLContext.getCapabilities().OpenGL40 ? 33 : 40)))))))))));
    }

    public static void updateThreadPriorities() {
        Config.updateAvailableProcessors();
        int i2 = 8;
        if (Config.isSingleProcessor()) {
            if (Config.isSmoothWorld()) {
                minecraftThread.setPriority(10);
                Config.setThreadPriority("Server thread", 1);
            } else {
                minecraftThread.setPriority(5);
                Config.setThreadPriority("Server thread", 5);
            }
        } else {
            minecraftThread.setPriority(10);
            Config.setThreadPriority("Server thread", 5);
        }
    }

    private static void setThreadPriority(String p_setThreadPriority_0_, int p_setThreadPriority_1_) {
        try {
            ThreadGroup threadgroup = Thread.currentThread().getThreadGroup();
            if (threadgroup == null) {
                return;
            }
            int i2 = (threadgroup.activeCount() + 10) * 2;
            Thread[] athread = new Thread[i2];
            threadgroup.enumerate(athread, false);
            for (int j2 = 0; j2 < athread.length; ++j2) {
                Thread thread = athread[j2];
                if (thread == null || !thread.getName().startsWith(p_setThreadPriority_0_)) continue;
                thread.setPriority(p_setThreadPriority_1_);
            }
        }
        catch (Throwable throwable) {
            Config.dbg(throwable.getClass().getName() + ": " + throwable.getMessage());
        }
    }

    public static boolean isMinecraftThread() {
        return Thread.currentThread() == minecraftThread;
    }

    private static void startVersionCheckThread() {
        VersionCheckThread versioncheckthread = new VersionCheckThread();
        versioncheckthread.start();
    }

    public static int getMipmapType() {
        if (gameSettings == null) {
            return DEF_MIPMAP_TYPE;
        }
        switch (Config.gameSettings.ofMipmapType) {
            case 0: {
                return 9986;
            }
            case 1: {
                return 9986;
            }
            case 2: {
                if (Config.isMultiTexture()) {
                    return 9985;
                }
                return 9986;
            }
            case 3: {
                if (Config.isMultiTexture()) {
                    return 9987;
                }
                return 9986;
            }
        }
        return 9986;
    }

    public static boolean isUseAlphaFunc() {
        float f2 = Config.getAlphaFuncLevel();
        return f2 > DEF_ALPHA_FUNC_LEVEL.floatValue() + 1.0E-5f;
    }

    public static float getAlphaFuncLevel() {
        return DEF_ALPHA_FUNC_LEVEL.floatValue();
    }

    public static boolean isFogFancy() {
        return !Config.isFancyFogAvailable() ? false : Config.gameSettings.ofFogType == 2;
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

    public static void dbg(String p_dbg_0_) {
        systemOut.print("[OptiFine] ");
        systemOut.println(p_dbg_0_);
    }

    public static void warn(String p_warn_0_) {
        systemOut.print("[OptiFine] [WARN] ");
        systemOut.println(p_warn_0_);
    }

    public static void error(String p_error_0_) {
        systemOut.print("[OptiFine] [ERROR] ");
        systemOut.println(p_error_0_);
    }

    public static void log(String p_log_0_) {
        Config.dbg(p_log_0_);
    }

    public static int getUpdatesPerFrame() {
        return Config.gameSettings.ofChunkUpdates;
    }

    public static boolean isDynamicUpdates() {
        return Config.gameSettings.ofChunkUpdatesDynamic;
    }

    public static boolean isRainFancy() {
        return Config.gameSettings.ofRain == 0 ? Config.gameSettings.fancyGraphics : Config.gameSettings.ofRain == 2;
    }

    public static boolean isRainOff() {
        return Config.gameSettings.ofRain == 3;
    }

    public static boolean isCloudsFancy() {
        return Config.gameSettings.ofClouds != 0 ? Config.gameSettings.ofClouds == 2 : (texturePackClouds != 0 ? texturePackClouds == 2 : Config.gameSettings.fancyGraphics);
    }

    public static boolean isCloudsOff() {
        return Config.gameSettings.ofClouds == 3;
    }

    public static void updateTexturePackClouds() {
        texturePackClouds = 0;
        IResourceManager iresourcemanager = Config.getResourceManager();
        if (iresourcemanager != null) {
            try {
                InputStream inputstream = iresourcemanager.getResource(new ResourceLocation("mcpatcher/color.properties")).getInputStream();
                if (inputstream == null) {
                    return;
                }
                Properties properties = new Properties();
                properties.load(inputstream);
                inputstream.close();
                String s2 = properties.getProperty("clouds");
                if (s2 == null) {
                    return;
                }
                Config.dbg("Texture pack clouds: " + s2);
                s2 = s2.toLowerCase();
                if (s2.equals("fast")) {
                    texturePackClouds = 1;
                }
                if (s2.equals("fancy")) {
                    texturePackClouds = 2;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static boolean isTreesFancy() {
        return Config.gameSettings.ofTrees == 0 ? Config.gameSettings.fancyGraphics : Config.gameSettings.ofTrees == 2;
    }

    public static boolean isDroppedItemsFancy() {
        return Config.gameSettings.ofDroppedItems == 0 ? Config.gameSettings.fancyGraphics : Config.gameSettings.ofDroppedItems == 2;
    }

    public static int limit(int p_limit_0_, int p_limit_1_, int p_limit_2_) {
        return p_limit_0_ < p_limit_1_ ? p_limit_1_ : (p_limit_0_ > p_limit_2_ ? p_limit_2_ : p_limit_0_);
    }

    public static float limit(float p_limit_0_, float p_limit_1_, float p_limit_2_) {
        return p_limit_0_ < p_limit_1_ ? p_limit_1_ : (p_limit_0_ > p_limit_2_ ? p_limit_2_ : p_limit_0_);
    }

    public static float limitTo1(float p_limitTo1_0_) {
        return p_limitTo1_0_ < 0.0f ? 0.0f : (p_limitTo1_0_ > 1.0f ? 1.0f : p_limitTo1_0_);
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

    private static Method getMethod(Class p_getMethod_0_, String p_getMethod_1_, Object[] p_getMethod_2_) {
        Method[] amethod = p_getMethod_0_.getMethods();
        for (int i2 = 0; i2 < amethod.length; ++i2) {
            Method method = amethod[i2];
            if (!method.getName().equals(p_getMethod_1_) || method.getParameterTypes().length != p_getMethod_2_.length) continue;
            return method;
        }
        Config.warn("No method found for: " + p_getMethod_0_.getName() + "." + p_getMethod_1_ + "(" + Config.arrayToString(p_getMethod_2_) + ")");
        return null;
    }

    public static String arrayToString(Object[] p_arrayToString_0_) {
        if (p_arrayToString_0_ == null) {
            return "";
        }
        StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);
        for (int i2 = 0; i2 < p_arrayToString_0_.length; ++i2) {
            Object object = p_arrayToString_0_[i2];
            if (i2 > 0) {
                stringbuffer.append(", ");
            }
            stringbuffer.append(String.valueOf(object));
        }
        return stringbuffer.toString();
    }

    public static String arrayToString(int[] p_arrayToString_0_) {
        if (p_arrayToString_0_ == null) {
            return "";
        }
        StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);
        for (int i2 = 0; i2 < p_arrayToString_0_.length; ++i2) {
            int j2 = p_arrayToString_0_[i2];
            if (i2 > 0) {
                stringbuffer.append(", ");
            }
            stringbuffer.append(String.valueOf(j2));
        }
        return stringbuffer.toString();
    }

    public static Minecraft getMinecraft() {
        return minecraft;
    }

    public static TextureManager getTextureManager() {
        return minecraft.getTextureManager();
    }

    public static IResourceManager getResourceManager() {
        return minecraft.getResourceManager();
    }

    public static InputStream getResourceStream(ResourceLocation p_getResourceStream_0_) throws IOException {
        return Config.getResourceStream(minecraft.getResourceManager(), p_getResourceStream_0_);
    }

    public static InputStream getResourceStream(IResourceManager p_getResourceStream_0_, ResourceLocation p_getResourceStream_1_) throws IOException {
        IResource iresource = p_getResourceStream_0_.getResource(p_getResourceStream_1_);
        return iresource == null ? null : iresource.getInputStream();
    }

    public static IResource getResource(ResourceLocation p_getResource_0_) throws IOException {
        return minecraft.getResourceManager().getResource(p_getResource_0_);
    }

    public static boolean hasResource(ResourceLocation p_hasResource_0_) {
        try {
            IResource iresource = Config.getResource(p_hasResource_0_);
            return iresource != null;
        }
        catch (IOException var2) {
            return false;
        }
    }

    public static boolean hasResource(IResourceManager p_hasResource_0_, ResourceLocation p_hasResource_1_) {
        try {
            IResource iresource = p_hasResource_0_.getResource(p_hasResource_1_);
            return iresource != null;
        }
        catch (IOException var3) {
            return false;
        }
    }

    public static IResourcePack[] getResourcePacks() {
        ResourcePackRepository resourcepackrepository = minecraft.getResourcePackRepository();
        List<ResourcePackRepository.Entry> list = resourcepackrepository.getRepositoryEntries();
        ArrayList<IResourcePack> list1 = new ArrayList<IResourcePack>();
        for (ResourcePackRepository.Entry resourcepackrepository$entry : list) {
            list1.add(resourcepackrepository$entry.getResourcePack());
        }
        IResourcePack[] airesourcepack = list1.toArray(new IResourcePack[list1.size()]);
        return airesourcepack;
    }

    public static String getResourcePackNames() {
        if (minecraft == null) {
            return "";
        }
        if (minecraft.getResourcePackRepository() == null) {
            return "";
        }
        IResourcePack[] airesourcepack = Config.getResourcePacks();
        if (airesourcepack.length <= 0) {
            return Config.getDefaultResourcePack().getPackName();
        }
        String[] astring = new String[airesourcepack.length];
        for (int i2 = 0; i2 < airesourcepack.length; ++i2) {
            astring[i2] = airesourcepack[i2].getPackName();
        }
        String s2 = Config.arrayToString(astring);
        return s2;
    }

    public static IResourcePack getDefaultResourcePack() {
        return Config.minecraft.getResourcePackRepository().rprDefaultResourcePack;
    }

    public static boolean isFromDefaultResourcePack(ResourceLocation p_isFromDefaultResourcePack_0_) {
        IResourcePack iresourcepack = Config.getDefiningResourcePack(p_isFromDefaultResourcePack_0_);
        return iresourcepack == Config.getDefaultResourcePack();
    }

    public static IResourcePack getDefiningResourcePack(ResourceLocation p_getDefiningResourcePack_0_) {
        IResourcePack[] airesourcepack = Config.getResourcePacks();
        for (int i2 = airesourcepack.length - 1; i2 >= 0; --i2) {
            IResourcePack iresourcepack = airesourcepack[i2];
            if (!iresourcepack.resourceExists(p_getDefiningResourcePack_0_)) continue;
            return iresourcepack;
        }
        if (Config.getDefaultResourcePack().resourceExists(p_getDefiningResourcePack_0_)) {
            return Config.getDefaultResourcePack();
        }
        return null;
    }

    public static RenderGlobal getRenderGlobal() {
        return minecraft == null ? null : Config.minecraft.renderGlobal;
    }

    public static int getMaxDynamicTileWidth() {
        return 64;
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
        return Config.gameSettings.ofVignette == 0 ? Config.gameSettings.fancyGraphics : Config.gameSettings.ofVignette == 2;
    }

    public static boolean isStarsEnabled() {
        return Config.gameSettings.ofStars;
    }

    public static void sleep(long p_sleep_0_) {
        try {
            Thread.currentThread();
            Thread.sleep(p_sleep_0_);
        }
        catch (InterruptedException interruptedexception) {
            interruptedexception.printStackTrace();
        }
    }

    public static boolean isTimeDayOnly() {
        return Config.gameSettings.ofTime == 1;
    }

    public static boolean isTimeDefault() {
        return Config.gameSettings.ofTime == 0 || Config.gameSettings.ofTime == 2;
    }

    public static boolean isTimeNightOnly() {
        return Config.gameSettings.ofTime == 3;
    }

    public static boolean isClearWater() {
        return Config.gameSettings.ofClearWater;
    }

    public static int getAnisotropicFilterLevel() {
        return Config.gameSettings.ofAfLevel;
    }

    public static int getAntialiasingLevel() {
        return antialiasingLevel;
    }

    public static boolean between(int p_between_0_, int p_between_1_, int p_between_2_) {
        return p_between_0_ >= p_between_1_ && p_between_0_ <= p_between_2_;
    }

    public static boolean isMultiTexture() {
        return Config.getAnisotropicFilterLevel() > 1 ? true : Config.getAntialiasingLevel() > 0;
    }

    public static boolean isDrippingWaterLava() {
        return Config.gameSettings.ofDrippingWaterLava;
    }

    public static boolean isBetterSnow() {
        return Config.gameSettings.ofBetterSnow;
    }

    public static Dimension getFullscreenDimension() {
        if (desktopDisplayMode == null) {
            return null;
        }
        if (gameSettings == null) {
            return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
        }
        String s2 = Config.gameSettings.ofFullscreenMode;
        if (s2.equals("Default")) {
            return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
        }
        String[] astring = Config.tokenize(s2, " x");
        return astring.length < 2 ? new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight()) : new Dimension(Config.parseInt(astring[0], -1), Config.parseInt(astring[1], -1));
    }

    public static int parseInt(String p_parseInt_0_, int p_parseInt_1_) {
        try {
            return p_parseInt_0_ == null ? p_parseInt_1_ : Integer.parseInt(p_parseInt_0_);
        }
        catch (NumberFormatException var3) {
            return p_parseInt_1_;
        }
    }

    public static float parseFloat(String p_parseFloat_0_, float p_parseFloat_1_) {
        try {
            return p_parseFloat_0_ == null ? p_parseFloat_1_ : Float.parseFloat(p_parseFloat_0_);
        }
        catch (NumberFormatException var3) {
            return p_parseFloat_1_;
        }
    }

    public static String[] tokenize(String p_tokenize_0_, String p_tokenize_1_) {
        StringTokenizer stringtokenizer = new StringTokenizer(p_tokenize_0_, p_tokenize_1_);
        ArrayList<String> list = new ArrayList<String>();
        while (stringtokenizer.hasMoreTokens()) {
            String s2 = stringtokenizer.nextToken();
            list.add(s2);
        }
        String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }

    public static DisplayMode getDesktopDisplayMode() {
        return desktopDisplayMode;
    }

    public static DisplayMode[] getFullscreenDisplayModes() {
        try {
            DisplayMode[] adisplaymode = Display.getAvailableDisplayModes();
            ArrayList<DisplayMode> list = new ArrayList<DisplayMode>();
            for (int i2 = 0; i2 < adisplaymode.length; ++i2) {
                DisplayMode displaymode = adisplaymode[i2];
                if (desktopDisplayMode != null && (displaymode.getBitsPerPixel() != desktopDisplayMode.getBitsPerPixel() || displaymode.getFrequency() != desktopDisplayMode.getFrequency())) continue;
                list.add(displaymode);
            }
            DisplayMode[] adisplaymode1 = list.toArray(new DisplayMode[list.size()]);
            Comparator comparator = new Comparator(){

                public int compare(Object p_compare_1_, Object p_compare_2_) {
                    DisplayMode displaymode1 = (DisplayMode)p_compare_1_;
                    DisplayMode displaymode2 = (DisplayMode)p_compare_2_;
                    return displaymode1.getWidth() != displaymode2.getWidth() ? displaymode2.getWidth() - displaymode1.getWidth() : (displaymode1.getHeight() != displaymode2.getHeight() ? displaymode2.getHeight() - displaymode1.getHeight() : 0);
                }
            };
            Arrays.sort(adisplaymode1, comparator);
            return adisplaymode1;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return new DisplayMode[]{desktopDisplayMode};
        }
    }

    public static String[] getFullscreenModes() {
        DisplayMode[] adisplaymode = Config.getFullscreenDisplayModes();
        String[] astring = new String[adisplaymode.length];
        for (int i2 = 0; i2 < adisplaymode.length; ++i2) {
            String s2;
            DisplayMode displaymode = adisplaymode[i2];
            astring[i2] = s2 = "" + displaymode.getWidth() + "x" + displaymode.getHeight();
        }
        return astring;
    }

    public static DisplayMode getDisplayMode(Dimension p_getDisplayMode_0_) throws LWJGLException {
        DisplayMode[] adisplaymode = Display.getAvailableDisplayModes();
        for (int i2 = 0; i2 < adisplaymode.length; ++i2) {
            DisplayMode displaymode = adisplaymode[i2];
            if (displaymode.getWidth() != p_getDisplayMode_0_.width || displaymode.getHeight() != p_getDisplayMode_0_.height || desktopDisplayMode != null && (displaymode.getBitsPerPixel() != desktopDisplayMode.getBitsPerPixel() || displaymode.getFrequency() != desktopDisplayMode.getFrequency())) continue;
            return displaymode;
        }
        return desktopDisplayMode;
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

    public static void checkGlError(String p_checkGlError_0_) {
        int i2 = GL11.glGetError();
        if (i2 != 0) {
            String s2 = GLU.gluErrorString(i2);
            Config.error("OpenGlError: " + i2 + " (" + s2 + "), at: " + p_checkGlError_0_);
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
        return Config.gameSettings.ofTranslucentBlocks == 0 ? Config.gameSettings.fancyGraphics : Config.gameSettings.ofTranslucentBlocks == 2;
    }

    public static String[] readLines(File p_readLines_0_) throws IOException {
        ArrayList<String> list = new ArrayList<String>();
        FileInputStream fileinputstream = new FileInputStream(p_readLines_0_);
        InputStreamReader inputstreamreader = new InputStreamReader((InputStream)fileinputstream, "ASCII");
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        while (true) {
            String s2;
            if ((s2 = bufferedreader.readLine()) == null) {
                String[] astring = list.toArray(new String[list.size()]);
                return astring;
            }
            list.add(s2);
        }
    }

    public static String readFile(File p_readFile_0_) throws IOException {
        FileInputStream fileinputstream = new FileInputStream(p_readFile_0_);
        return Config.readInputStream(fileinputstream, "ASCII");
    }

    public static String readInputStream(InputStream p_readInputStream_0_) throws IOException {
        return Config.readInputStream(p_readInputStream_0_, "ASCII");
    }

    public static String readInputStream(InputStream p_readInputStream_0_, String p_readInputStream_1_) throws IOException {
        InputStreamReader inputstreamreader = new InputStreamReader(p_readInputStream_0_, p_readInputStream_1_);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        StringBuffer stringbuffer = new StringBuffer();
        String s2;
        while ((s2 = bufferedreader.readLine()) != null) {
            stringbuffer.append(s2);
            stringbuffer.append("\n");
        }
        return stringbuffer.toString();
    }

    public static GameSettings getGameSettings() {
        return gameSettings;
    }

    public static String getNewRelease() {
        return newRelease;
    }

    public static void setNewRelease(String p_setNewRelease_0_) {
        newRelease = p_setNewRelease_0_;
    }

    public static int compareRelease(String p_compareRelease_0_, String p_compareRelease_1_) {
        int j2;
        String[] astring1;
        String s1;
        String[] astring = Config.splitRelease(p_compareRelease_0_);
        String s2 = astring[0];
        if (!s2.equals(s1 = (astring1 = Config.splitRelease(p_compareRelease_1_))[0])) {
            return s2.compareTo(s1);
        }
        int i2 = Config.parseInt(astring[1], -1);
        if (i2 != (j2 = Config.parseInt(astring1[1], -1))) {
            return i2 - j2;
        }
        String s22 = astring[2];
        String s3 = astring1[2];
        return s22.compareTo(s3);
    }

    private static String[] splitRelease(String p_splitRelease_0_) {
        if (p_splitRelease_0_ != null && p_splitRelease_0_.length() > 0) {
            int i2;
            String s2 = p_splitRelease_0_.substring(0, 1);
            if (p_splitRelease_0_.length() <= 1) {
                return new String[]{s2, "", ""};
            }
            for (i2 = 1; i2 < p_splitRelease_0_.length() && Character.isDigit(p_splitRelease_0_.charAt(i2)); ++i2) {
            }
            String s1 = p_splitRelease_0_.substring(1, i2);
            if (i2 >= p_splitRelease_0_.length()) {
                return new String[]{s2, s1, ""};
            }
            String s22 = p_splitRelease_0_.substring(i2);
            return new String[]{s2, s1, s22};
        }
        return new String[]{"", "", ""};
    }

    public static int intHash(int p_intHash_0_) {
        p_intHash_0_ = p_intHash_0_ ^ 0x3D ^ p_intHash_0_ >> 16;
        p_intHash_0_ += p_intHash_0_ << 3;
        p_intHash_0_ ^= p_intHash_0_ >> 4;
        p_intHash_0_ *= 668265261;
        p_intHash_0_ ^= p_intHash_0_ >> 15;
        return p_intHash_0_;
    }

    public static int getRandom(BlockPos p_getRandom_0_, int p_getRandom_1_) {
        int i2 = Config.intHash(p_getRandom_1_ + 37);
        i2 = Config.intHash(i2 + p_getRandom_0_.getX());
        i2 = Config.intHash(i2 + p_getRandom_0_.getZ());
        i2 = Config.intHash(i2 + p_getRandom_0_.getY());
        return i2;
    }

    public static WorldServer getWorldServer() {
        if (minecraft == null) {
            return null;
        }
        WorldClient world = Config.minecraft.theWorld;
        if (world == null) {
            return null;
        }
        if (!minecraft.isIntegratedServerRunning()) {
            return null;
        }
        IntegratedServer integratedserver = minecraft.getIntegratedServer();
        if (integratedserver == null) {
            return null;
        }
        WorldProvider worldprovider = world.provider;
        if (worldprovider == null) {
            return null;
        }
        int i2 = worldprovider.getDimensionId();
        try {
            WorldServer worldserver = integratedserver.worldServerForDimension(i2);
            return worldserver;
        }
        catch (NullPointerException var5) {
            return null;
        }
    }

    public static int getAvailableProcessors() {
        return availableProcessors;
    }

    public static void updateAvailableProcessors() {
        availableProcessors = Runtime.getRuntime().availableProcessors();
    }

    public static boolean isSingleProcessor() {
        return Config.getAvailableProcessors() <= 1;
    }

    public static boolean isSmoothWorld() {
        return Config.gameSettings.ofSmoothWorld;
    }

    public static boolean isLazyChunkLoading() {
        return !Config.isSingleProcessor() ? false : Config.gameSettings.ofLazyChunkLoading;
    }

    public static int getChunkViewDistance() {
        if (gameSettings == null) {
            return 10;
        }
        int i2 = Config.gameSettings.renderDistanceChunks;
        return i2;
    }

    public static boolean equals(Object p_equals_0_, Object p_equals_1_) {
        return p_equals_0_ == p_equals_1_ ? true : (p_equals_0_ == null ? false : p_equals_0_.equals(p_equals_1_));
    }

    public static void checkDisplaySettings() {
        if (Config.getAntialiasingLevel() > 0) {
            int i2 = Config.getAntialiasingLevel();
            DisplayMode displaymode = Display.getDisplayMode();
            Config.dbg("FSAA Samples: " + i2);
            try {
                Display.destroy();
                Display.setDisplayMode(displaymode);
                Display.create(new PixelFormat().withDepthBits(24).withSamples(i2));
                Display.setResizable(false);
                Display.setResizable(true);
            }
            catch (LWJGLException lwjglexception2) {
                Config.warn("Error setting FSAA: " + i2 + "x");
                lwjglexception2.printStackTrace();
                try {
                    Display.setDisplayMode(displaymode);
                    Display.create(new PixelFormat().withDepthBits(24));
                    Display.setResizable(false);
                    Display.setResizable(true);
                }
                catch (LWJGLException lwjglexception1) {
                    lwjglexception1.printStackTrace();
                    try {
                        Display.setDisplayMode(displaymode);
                        Display.create();
                        Display.setResizable(false);
                        Display.setResizable(true);
                    }
                    catch (LWJGLException lwjglexception) {
                        lwjglexception.printStackTrace();
                    }
                }
            }
        }
    }

    private static ByteBuffer readIconImage(File p_readIconImage_0_) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(p_readIconImage_0_);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        for (int i2 : aint) {
            bytebuffer.putInt(i2 << 8 | i2 >> 24 & 0xFF);
        }
        bytebuffer.flip();
        return bytebuffer;
    }

    public static void checkDisplayMode() {
        try {
            if (minecraft.isFullScreen()) {
                if (fullscreenModeChecked) {
                    return;
                }
                fullscreenModeChecked = true;
                desktopModeChecked = false;
                DisplayMode displaymode = Display.getDisplayMode();
                Dimension dimension = Config.getFullscreenDimension();
                if (dimension == null) {
                    return;
                }
                if (displaymode.getWidth() == dimension.width && displaymode.getHeight() == dimension.height) {
                    return;
                }
                DisplayMode displaymode1 = Config.getDisplayMode(dimension);
                if (displaymode1 == null) {
                    return;
                }
                Display.setDisplayMode(displaymode1);
                Config.minecraft.displayWidth = Display.getDisplayMode().getWidth();
                Config.minecraft.displayHeight = Display.getDisplayMode().getHeight();
                if (Config.minecraft.displayWidth <= 0) {
                    Config.minecraft.displayWidth = 1;
                }
                if (Config.minecraft.displayHeight <= 0) {
                    Config.minecraft.displayHeight = 1;
                }
                if (Config.minecraft.currentScreen != null) {
                    ScaledResolution scaledresolution = new ScaledResolution(minecraft);
                    int i2 = scaledresolution.getScaledWidth();
                    int j2 = scaledresolution.getScaledHeight();
                    Config.minecraft.currentScreen.setWorldAndResolution(minecraft, i2, j2);
                }
                Config.minecraft.loadingScreen = new LoadingScreenRenderer(minecraft);
                Config.updateFramebufferSize();
                Display.setFullscreen(true);
                Config.minecraft.gameSettings.updateVSync();
                GlStateManager.enableTexture2D();
            } else {
                if (desktopModeChecked) {
                    return;
                }
                desktopModeChecked = true;
                fullscreenModeChecked = false;
                Config.minecraft.gameSettings.updateVSync();
                Display.update();
                GlStateManager.enableTexture2D();
                Display.setResizable(false);
                Display.setResizable(true);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void updateFramebufferSize() {
        minecraft.getFramebuffer().createBindFramebuffer(Config.minecraft.displayWidth, Config.minecraft.displayHeight);
        if (Config.minecraft.entityRenderer != null) {
            Config.minecraft.entityRenderer.updateShaderGroupSize(Config.minecraft.displayWidth, Config.minecraft.displayHeight);
        }
    }

    public static Object[] addObjectToArray(Object[] p_addObjectToArray_0_, Object p_addObjectToArray_1_) {
        if (p_addObjectToArray_0_ == null) {
            throw new NullPointerException("The given array is NULL");
        }
        int i2 = p_addObjectToArray_0_.length;
        int j2 = i2 + 1;
        Object[] aobject = (Object[])Array.newInstance(p_addObjectToArray_0_.getClass().getComponentType(), j2);
        System.arraycopy(p_addObjectToArray_0_, 0, aobject, 0, i2);
        aobject[i2] = p_addObjectToArray_1_;
        return aobject;
    }

    public static Object[] addObjectsToArray(Object[] p_addObjectsToArray_0_, Object[] p_addObjectsToArray_1_) {
        if (p_addObjectsToArray_0_ == null) {
            throw new NullPointerException("The given array is NULL");
        }
        if (p_addObjectsToArray_1_.length == 0) {
            return p_addObjectsToArray_0_;
        }
        int i2 = p_addObjectsToArray_0_.length;
        int j2 = i2 + p_addObjectsToArray_1_.length;
        Object[] aobject = (Object[])Array.newInstance(p_addObjectsToArray_0_.getClass().getComponentType(), j2);
        System.arraycopy(p_addObjectsToArray_0_, 0, aobject, 0, i2);
        System.arraycopy(p_addObjectsToArray_1_, 0, aobject, i2, p_addObjectsToArray_1_.length);
        return aobject;
    }

    public static boolean isCustomItems() {
        return false;
    }

    public static boolean isActing() {
        boolean flag = Config.isActingNow();
        long i2 = System.currentTimeMillis();
        if (flag) {
            lastActionTime = i2;
            return true;
        }
        return i2 - lastActionTime < 100L;
    }

    private static boolean isActingNow() {
        return Mouse.isButtonDown(0) ? true : Mouse.isButtonDown(1);
    }

    public static void drawFps() {
        Minecraft minecraftx = minecraft;
        int i2 = Minecraft.getDebugFPS();
        String s2 = Config.getUpdates(Config.minecraft.debug);
        int j2 = Config.minecraft.renderGlobal.getCountActiveRenderers();
        int k2 = Config.minecraft.renderGlobal.getCountEntitiesRendered();
        int l2 = Config.minecraft.renderGlobal.getCountTileEntitiesRendered();
        String s1 = "" + i2 + " fps, C: " + j2 + ", E: " + k2 + "+" + l2 + ", U: " + s2;
        Config.minecraft.fontRendererObj.drawString(s1, 2, 2, -2039584);
    }

    private static String getUpdates(String p_getUpdates_0_) {
        int i2 = p_getUpdates_0_.indexOf(40);
        if (i2 < 0) {
            return "";
        }
        int j2 = p_getUpdates_0_.indexOf(32, i2);
        return j2 < 0 ? "" : p_getUpdates_0_.substring(i2 + 1, j2);
    }

    public static int getBitsOs() {
        String s2 = System.getenv("ProgramFiles(X86)");
        return s2 != null ? 64 : 32;
    }

    public static int getBitsJre() {
        String[] astring = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
        for (int i2 = 0; i2 < astring.length; ++i2) {
            String s2 = astring[i2];
            String s1 = System.getProperty(s2);
            if (s1 == null || !s1.contains("64")) continue;
            return 64;
        }
        return 32;
    }

    public static boolean isNotify64BitJava() {
        return notify64BitJava;
    }

    public static void setNotify64BitJava(boolean p_setNotify64BitJava_0_) {
        notify64BitJava = p_setNotify64BitJava_0_;
    }

    public static boolean isConnectedModels() {
        return false;
    }

    public static String fillLeft(String p_fillLeft_0_, int p_fillLeft_1_, char p_fillLeft_2_) {
        if (p_fillLeft_0_ == null) {
            p_fillLeft_0_ = "";
        }
        if (p_fillLeft_0_.length() >= p_fillLeft_1_) {
            return p_fillLeft_0_;
        }
        StringBuffer stringbuffer = new StringBuffer(p_fillLeft_0_);
        while (stringbuffer.length() < p_fillLeft_1_ - p_fillLeft_0_.length()) {
            stringbuffer.append(p_fillLeft_2_);
        }
        return stringbuffer.toString() + p_fillLeft_0_;
    }

    public static String fillRight(String p_fillRight_0_, int p_fillRight_1_, char p_fillRight_2_) {
        if (p_fillRight_0_ == null) {
            p_fillRight_0_ = "";
        }
        if (p_fillRight_0_.length() >= p_fillRight_1_) {
            return p_fillRight_0_;
        }
        StringBuffer stringbuffer = new StringBuffer(p_fillRight_0_);
        while (stringbuffer.length() < p_fillRight_1_) {
            stringbuffer.append(p_fillRight_2_);
        }
        return stringbuffer.toString();
    }
}

