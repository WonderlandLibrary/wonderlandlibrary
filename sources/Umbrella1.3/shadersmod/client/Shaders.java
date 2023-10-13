/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.ARBShaderObjects
 *  org.lwjgl.opengl.ARBVertexShader
 *  org.lwjgl.opengl.ContextCapabilities
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 *  org.lwjgl.opengl.GLContext
 *  org.lwjgl.util.glu.GLU
 */
package shadersmod.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import optifine.Config;
import optifine.Lang;
import optifine.PropertiesOrdered;
import optifine.Reflector;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import shadersmod.client.EnumShaderOption;
import shadersmod.client.HFNoiseTexture;
import shadersmod.client.IShaderPack;
import shadersmod.client.PropertyDefaultFastFancyOff;
import shadersmod.client.PropertyDefaultTrueFalse;
import shadersmod.client.SMath;
import shadersmod.client.ShaderOption;
import shadersmod.client.ShaderOptionProfile;
import shadersmod.client.ShaderOptionRest;
import shadersmod.client.ShaderPackDefault;
import shadersmod.client.ShaderPackFolder;
import shadersmod.client.ShaderPackNone;
import shadersmod.client.ShaderPackParser;
import shadersmod.client.ShaderPackZip;
import shadersmod.client.ShaderProfile;
import shadersmod.client.ShaderUniformFloat4;
import shadersmod.client.ShaderUniformInt;
import shadersmod.client.ShaderUtils;
import shadersmod.client.ShadersRender;
import shadersmod.client.ShadersTex;
import shadersmod.common.SMCLog;

public class Shaders {
    static Minecraft mc = Minecraft.getMinecraft();
    static EntityRenderer entityRenderer;
    public static boolean isInitializedOnce;
    public static boolean isShaderPackInitialized;
    public static ContextCapabilities capabilities;
    public static String glVersionString;
    public static String glVendorString;
    public static String glRendererString;
    public static boolean hasGlGenMipmap;
    public static boolean hasForge;
    public static int numberResetDisplayList;
    static boolean needResetModels;
    private static int renderDisplayWidth;
    private static int renderDisplayHeight;
    public static int renderWidth;
    public static int renderHeight;
    public static boolean isRenderingWorld;
    public static boolean isRenderingSky;
    public static boolean isCompositeRendered;
    public static boolean isRenderingDfb;
    public static boolean isShadowPass;
    public static boolean isSleeping;
    public static boolean isHandRendered;
    public static boolean renderItemPass1DepthMask;
    public static ItemStack itemToRender;
    static float[] sunPosition;
    static float[] moonPosition;
    static float[] shadowLightPosition;
    static float[] upPosition;
    static float[] shadowLightPositionVector;
    static float[] upPosModelView;
    static float[] sunPosModelView;
    static float[] moonPosModelView;
    private static float[] tempMat;
    static float clearColorR;
    static float clearColorG;
    static float clearColorB;
    static float skyColorR;
    static float skyColorG;
    static float skyColorB;
    static long worldTime;
    static long lastWorldTime;
    static long diffWorldTime;
    static float celestialAngle;
    static float sunAngle;
    static float shadowAngle;
    static int moonPhase;
    static long systemTime;
    static long lastSystemTime;
    static long diffSystemTime;
    static int frameCounter;
    static float frameTimeCounter;
    static int systemTimeInt32;
    static float rainStrength;
    static float wetness;
    public static float wetnessHalfLife;
    public static float drynessHalfLife;
    public static float eyeBrightnessHalflife;
    static boolean usewetness;
    static int isEyeInWater;
    static int eyeBrightness;
    static float eyeBrightnessFadeX;
    static float eyeBrightnessFadeY;
    static float eyePosY;
    static float centerDepth;
    static float centerDepthSmooth;
    static float centerDepthSmoothHalflife;
    static boolean centerDepthSmoothEnabled;
    static int superSamplingLevel;
    static boolean updateChunksErrorRecorded;
    static boolean lightmapEnabled;
    static boolean fogEnabled;
    public static int entityAttrib;
    public static int midTexCoordAttrib;
    public static int tangentAttrib;
    public static boolean useEntityAttrib;
    public static boolean useMidTexCoordAttrib;
    public static boolean useMultiTexCoord3Attrib;
    public static boolean useTangentAttrib;
    public static boolean progUseEntityAttrib;
    public static boolean progUseMidTexCoordAttrib;
    public static boolean progUseTangentAttrib;
    public static int atlasSizeX;
    public static int atlasSizeY;
    public static ShaderUniformFloat4 uniformEntityColor;
    public static ShaderUniformInt uniformEntityId;
    public static ShaderUniformInt uniformBlockEntityId;
    static double previousCameraPositionX;
    static double previousCameraPositionY;
    static double previousCameraPositionZ;
    static double cameraPositionX;
    static double cameraPositionY;
    static double cameraPositionZ;
    static int shadowPassInterval;
    public static boolean needResizeShadow;
    static int shadowMapWidth;
    static int shadowMapHeight;
    static int spShadowMapWidth;
    static int spShadowMapHeight;
    static float shadowMapFOV;
    static float shadowMapHalfPlane;
    static boolean shadowMapIsOrtho;
    static int shadowPassCounter;
    static int preShadowPassThirdPersonView;
    public static boolean shouldSkipDefaultShadow;
    static boolean waterShadowEnabled;
    static final int MaxDrawBuffers = 8;
    static final int MaxColorBuffers = 8;
    static final int MaxDepthBuffers = 3;
    static final int MaxShadowColorBuffers = 8;
    static final int MaxShadowDepthBuffers = 2;
    static int usedColorBuffers;
    static int usedDepthBuffers;
    static int usedShadowColorBuffers;
    static int usedShadowDepthBuffers;
    static int usedColorAttachs;
    static int usedDrawBuffers;
    static int dfb;
    static int sfb;
    private static int[] gbuffersFormat;
    public static int activeProgram;
    public static final int ProgramNone = 0;
    public static final int ProgramBasic = 1;
    public static final int ProgramTextured = 2;
    public static final int ProgramTexturedLit = 3;
    public static final int ProgramSkyBasic = 4;
    public static final int ProgramSkyTextured = 5;
    public static final int ProgramClouds = 6;
    public static final int ProgramTerrain = 7;
    public static final int ProgramTerrainSolid = 8;
    public static final int ProgramTerrainCutoutMip = 9;
    public static final int ProgramTerrainCutout = 10;
    public static final int ProgramDamagedBlock = 11;
    public static final int ProgramWater = 12;
    public static final int ProgramBlock = 13;
    public static final int ProgramBeaconBeam = 14;
    public static final int ProgramItem = 15;
    public static final int ProgramEntities = 16;
    public static final int ProgramArmorGlint = 17;
    public static final int ProgramSpiderEyes = 18;
    public static final int ProgramHand = 19;
    public static final int ProgramWeather = 20;
    public static final int ProgramComposite = 21;
    public static final int ProgramComposite1 = 22;
    public static final int ProgramComposite2 = 23;
    public static final int ProgramComposite3 = 24;
    public static final int ProgramComposite4 = 25;
    public static final int ProgramComposite5 = 26;
    public static final int ProgramComposite6 = 27;
    public static final int ProgramComposite7 = 28;
    public static final int ProgramFinal = 29;
    public static final int ProgramShadow = 30;
    public static final int ProgramShadowSolid = 31;
    public static final int ProgramShadowCutout = 32;
    public static final int ProgramCount = 33;
    public static final int MaxCompositePasses = 8;
    private static final String[] programNames;
    private static final int[] programBackups;
    static int[] programsID;
    private static int[] programsRef;
    private static int programIDCopyDepth;
    private static String[] programsDrawBufSettings;
    private static String newDrawBufSetting;
    static IntBuffer[] programsDrawBuffers;
    static IntBuffer activeDrawBuffers;
    private static String[] programsColorAtmSettings;
    private static String newColorAtmSetting;
    private static String activeColorAtmSettings;
    private static int[] programsCompositeMipmapSetting;
    private static int newCompositeMipmapSetting;
    private static int activeCompositeMipmapSetting;
    public static Properties loadedShaders;
    public static Properties shadersConfig;
    public static ITextureObject defaultTexture;
    public static boolean normalMapEnabled;
    public static boolean[] shadowHardwareFilteringEnabled;
    public static boolean[] shadowMipmapEnabled;
    public static boolean[] shadowFilterNearest;
    public static boolean[] shadowColorMipmapEnabled;
    public static boolean[] shadowColorFilterNearest;
    public static boolean configTweakBlockDamage;
    public static boolean configCloudShadow;
    public static float configHandDepthMul;
    public static float configRenderResMul;
    public static float configShadowResMul;
    public static int configTexMinFilB;
    public static int configTexMinFilN;
    public static int configTexMinFilS;
    public static int configTexMagFilB;
    public static int configTexMagFilN;
    public static int configTexMagFilS;
    public static boolean configShadowClipFrustrum;
    public static boolean configNormalMap;
    public static boolean configSpecularMap;
    public static PropertyDefaultTrueFalse configOldLighting;
    public static int configAntialiasingLevel;
    public static final int texMinFilRange = 3;
    public static final int texMagFilRange = 2;
    public static final String[] texMinFilDesc;
    public static final String[] texMagFilDesc;
    public static final int[] texMinFilValue;
    public static final int[] texMagFilValue;
    static IShaderPack shaderPack;
    public static boolean shaderPackLoaded;
    static File currentshader;
    static String currentshadername;
    public static String packNameNone;
    static String packNameDefault;
    static String shaderpacksdirname;
    static String optionsfilename;
    static File shadersdir;
    static File shaderpacksdir;
    static File configFile;
    static ShaderOption[] shaderPackOptions;
    static ShaderProfile[] shaderPackProfiles;
    static Map<String, ShaderOption[]> shaderPackGuiScreens;
    public static PropertyDefaultFastFancyOff shaderPackClouds;
    public static PropertyDefaultTrueFalse shaderPackOldLighting;
    public static PropertyDefaultTrueFalse shaderPackDynamicHandLight;
    private static Map<String, String> shaderPackResources;
    private static World currentWorld;
    private static List<Integer> shaderPackDimensions;
    public static final boolean enableShadersOption = true;
    private static final boolean enableShadersDebug = true;
    private static final boolean saveFinalShaders;
    public static float blockLightLevel05;
    public static float blockLightLevel06;
    public static float blockLightLevel08;
    public static float aoLevel;
    public static float blockAoLight;
    public static float sunPathRotation;
    public static float shadowAngleInterval;
    public static int fogMode;
    public static float fogColorR;
    public static float fogColorG;
    public static float fogColorB;
    public static float shadowIntervalSize;
    public static int terrainIconSize;
    public static int[] terrainTextureSize;
    private static HFNoiseTexture noiseTexture;
    private static boolean noiseTextureEnabled;
    private static int noiseTextureResolution;
    static final int[] dfbColorTexturesA;
    static final int[] colorTexturesToggle;
    static final int[] colorTextureTextureImageUnit;
    static final boolean[][] programsToggleColorTextures;
    private static final int bigBufferSize = 2196;
    private static final ByteBuffer bigBuffer;
    static final float[] faProjection;
    static final float[] faProjectionInverse;
    static final float[] faModelView;
    static final float[] faModelViewInverse;
    static final float[] faShadowProjection;
    static final float[] faShadowProjectionInverse;
    static final float[] faShadowModelView;
    static final float[] faShadowModelViewInverse;
    static final FloatBuffer projection;
    static final FloatBuffer projectionInverse;
    static final FloatBuffer modelView;
    static final FloatBuffer modelViewInverse;
    static final FloatBuffer shadowProjection;
    static final FloatBuffer shadowProjectionInverse;
    static final FloatBuffer shadowModelView;
    static final FloatBuffer shadowModelViewInverse;
    static final FloatBuffer previousProjection;
    static final FloatBuffer previousModelView;
    static final FloatBuffer tempMatrixDirectBuffer;
    static final FloatBuffer tempDirectFloatBuffer;
    static final IntBuffer dfbColorTextures;
    static final IntBuffer dfbDepthTextures;
    static final IntBuffer sfbColorTextures;
    static final IntBuffer sfbDepthTextures;
    static final IntBuffer dfbDrawBuffers;
    static final IntBuffer sfbDrawBuffers;
    static final IntBuffer drawBuffersNone;
    static final IntBuffer drawBuffersAll;
    static final IntBuffer drawBuffersClear0;
    static final IntBuffer drawBuffersClear1;
    static final IntBuffer drawBuffersClearColor;
    static final IntBuffer drawBuffersColorAtt0;
    static final IntBuffer[] drawBuffersBuffer;
    static Map<Block, Integer> mapBlockToEntityData;
    private static final Pattern gbufferFormatPattern;
    private static final Pattern gbufferMipmapEnabledPattern;
    private static final String[] formatNames;
    private static final int[] formatIds;
    private static final Pattern patternLoadEntityDataMap;
    public static int[] entityData;
    public static int entityDataIndex;

    static {
        isInitializedOnce = false;
        isShaderPackInitialized = false;
        hasGlGenMipmap = false;
        hasForge = false;
        numberResetDisplayList = 0;
        needResetModels = false;
        renderDisplayWidth = 0;
        renderDisplayHeight = 0;
        renderWidth = 0;
        renderHeight = 0;
        isRenderingWorld = false;
        isRenderingSky = false;
        isCompositeRendered = false;
        isRenderingDfb = false;
        isShadowPass = false;
        renderItemPass1DepthMask = false;
        sunPosition = new float[4];
        moonPosition = new float[4];
        shadowLightPosition = new float[4];
        upPosition = new float[4];
        shadowLightPositionVector = new float[4];
        upPosModelView = new float[]{0.0f, 100.0f, 0.0f, 0.0f};
        sunPosModelView = new float[]{0.0f, 100.0f, 0.0f, 0.0f};
        moonPosModelView = new float[]{0.0f, -100.0f, 0.0f, 0.0f};
        tempMat = new float[16];
        worldTime = 0L;
        lastWorldTime = 0L;
        diffWorldTime = 0L;
        celestialAngle = 0.0f;
        sunAngle = 0.0f;
        shadowAngle = 0.0f;
        moonPhase = 0;
        systemTime = 0L;
        lastSystemTime = 0L;
        diffSystemTime = 0L;
        frameCounter = 0;
        frameTimeCounter = 0.0f;
        systemTimeInt32 = 0;
        rainStrength = 0.0f;
        wetness = 0.0f;
        wetnessHalfLife = 600.0f;
        drynessHalfLife = 200.0f;
        eyeBrightnessHalflife = 10.0f;
        usewetness = false;
        isEyeInWater = 0;
        eyeBrightness = 0;
        eyeBrightnessFadeX = 0.0f;
        eyeBrightnessFadeY = 0.0f;
        eyePosY = 0.0f;
        centerDepth = 0.0f;
        centerDepthSmooth = 0.0f;
        centerDepthSmoothHalflife = 1.0f;
        centerDepthSmoothEnabled = false;
        superSamplingLevel = 1;
        updateChunksErrorRecorded = false;
        lightmapEnabled = false;
        fogEnabled = true;
        entityAttrib = 10;
        midTexCoordAttrib = 11;
        tangentAttrib = 12;
        useEntityAttrib = false;
        useMidTexCoordAttrib = false;
        useMultiTexCoord3Attrib = false;
        useTangentAttrib = false;
        progUseEntityAttrib = false;
        progUseMidTexCoordAttrib = false;
        progUseTangentAttrib = false;
        atlasSizeX = 0;
        atlasSizeY = 0;
        uniformEntityColor = new ShaderUniformFloat4("entityColor");
        uniformEntityId = new ShaderUniformInt("entityId");
        uniformBlockEntityId = new ShaderUniformInt("blockEntityId");
        shadowPassInterval = 0;
        needResizeShadow = false;
        shadowMapWidth = 1024;
        shadowMapHeight = 1024;
        spShadowMapWidth = 1024;
        spShadowMapHeight = 1024;
        shadowMapFOV = 90.0f;
        shadowMapHalfPlane = 160.0f;
        shadowMapIsOrtho = true;
        shadowPassCounter = 0;
        shouldSkipDefaultShadow = false;
        waterShadowEnabled = false;
        usedColorBuffers = 0;
        usedDepthBuffers = 0;
        usedShadowColorBuffers = 0;
        usedShadowDepthBuffers = 0;
        usedColorAttachs = 0;
        usedDrawBuffers = 0;
        dfb = 0;
        sfb = 0;
        gbuffersFormat = new int[8];
        activeProgram = 0;
        programNames = new String[]{"", "gbuffers_basic", "gbuffers_textured", "gbuffers_textured_lit", "gbuffers_skybasic", "gbuffers_skytextured", "gbuffers_clouds", "gbuffers_terrain", "gbuffers_terrain_solid", "gbuffers_terrain_cutout_mip", "gbuffers_terrain_cutout", "gbuffers_damagedblock", "gbuffers_water", "gbuffers_block", "gbuffers_beaconbeam", "gbuffers_item", "gbuffers_entities", "gbuffers_armor_glint", "gbuffers_spidereyes", "gbuffers_hand", "gbuffers_weather", "composite", "composite1", "composite2", "composite3", "composite4", "composite5", "composite6", "composite7", "final", "shadow", "shadow_solid", "shadow_cutout"};
        int[] arrn = new int[33];
        arrn[2] = 1;
        arrn[3] = 2;
        arrn[4] = 1;
        arrn[5] = 2;
        arrn[6] = 2;
        arrn[7] = 3;
        arrn[8] = 7;
        arrn[9] = 7;
        arrn[10] = 7;
        arrn[11] = 7;
        arrn[12] = 7;
        arrn[13] = 7;
        arrn[14] = 2;
        arrn[15] = 3;
        arrn[16] = 3;
        arrn[17] = 2;
        arrn[18] = 2;
        arrn[19] = 3;
        arrn[20] = 3;
        arrn[31] = 30;
        arrn[32] = 30;
        programBackups = arrn;
        programsID = new int[33];
        programsRef = new int[33];
        programIDCopyDepth = 0;
        programsDrawBufSettings = new String[33];
        newDrawBufSetting = null;
        programsDrawBuffers = new IntBuffer[33];
        activeDrawBuffers = null;
        programsColorAtmSettings = new String[33];
        newColorAtmSetting = null;
        activeColorAtmSettings = null;
        programsCompositeMipmapSetting = new int[33];
        newCompositeMipmapSetting = 0;
        activeCompositeMipmapSetting = 0;
        loadedShaders = null;
        shadersConfig = null;
        defaultTexture = null;
        normalMapEnabled = false;
        shadowHardwareFilteringEnabled = new boolean[2];
        shadowMipmapEnabled = new boolean[2];
        shadowFilterNearest = new boolean[2];
        shadowColorMipmapEnabled = new boolean[8];
        shadowColorFilterNearest = new boolean[8];
        configTweakBlockDamage = true;
        configCloudShadow = true;
        configHandDepthMul = 0.125f;
        configRenderResMul = 1.0f;
        configShadowResMul = 1.0f;
        configTexMinFilB = 0;
        configTexMinFilN = 0;
        configTexMinFilS = 0;
        configTexMagFilB = 0;
        configTexMagFilN = 0;
        configTexMagFilS = 0;
        configShadowClipFrustrum = true;
        configNormalMap = true;
        configSpecularMap = true;
        configOldLighting = new PropertyDefaultTrueFalse("oldLighting", "Classic Lighting", 2);
        configAntialiasingLevel = 0;
        texMinFilDesc = new String[]{"Nearest", "Nearest-Nearest", "Nearest-Linear"};
        texMagFilDesc = new String[]{"Nearest", "Linear"};
        texMinFilValue = new int[]{9728, 9984, 9986};
        texMagFilValue = new int[]{9728, 9729};
        shaderPack = null;
        shaderPackLoaded = false;
        packNameNone = "OFF";
        packNameDefault = "(internal)";
        shaderpacksdirname = "shaderpacks";
        optionsfilename = "optionsshaders.txt";
        shadersdir = new File(Minecraft.getMinecraft().mcDataDir, "shaders");
        shaderpacksdir = new File(Minecraft.getMinecraft().mcDataDir, shaderpacksdirname);
        configFile = new File(Minecraft.getMinecraft().mcDataDir, optionsfilename);
        shaderPackOptions = null;
        shaderPackProfiles = null;
        shaderPackGuiScreens = null;
        shaderPackClouds = new PropertyDefaultFastFancyOff("clouds", "Clouds", 0);
        shaderPackOldLighting = new PropertyDefaultTrueFalse("oldLighting", "Classic Lighting", 0);
        shaderPackDynamicHandLight = new PropertyDefaultTrueFalse("dynamicHandLight", "Dynamic Hand Light", 0);
        shaderPackResources = new HashMap<String, String>();
        currentWorld = null;
        shaderPackDimensions = new ArrayList<Integer>();
        saveFinalShaders = System.getProperty("shaders.debug.save", "false").equals("true");
        blockLightLevel05 = 0.5f;
        blockLightLevel06 = 0.6f;
        blockLightLevel08 = 0.8f;
        aoLevel = 0.8f;
        blockAoLight = 1.0f - aoLevel;
        sunPathRotation = 0.0f;
        shadowAngleInterval = 0.0f;
        fogMode = 0;
        shadowIntervalSize = 2.0f;
        terrainIconSize = 16;
        terrainTextureSize = new int[2];
        noiseTextureEnabled = false;
        noiseTextureResolution = 256;
        dfbColorTexturesA = new int[16];
        colorTexturesToggle = new int[8];
        int[] arrn2 = new int[8];
        arrn2[1] = 1;
        arrn2[2] = 2;
        arrn2[3] = 3;
        arrn2[4] = 7;
        arrn2[5] = 8;
        arrn2[6] = 9;
        arrn2[7] = 10;
        colorTextureTextureImageUnit = arrn2;
        programsToggleColorTextures = new boolean[33][8];
        bigBuffer = (ByteBuffer)BufferUtils.createByteBuffer((int)2196).limit(0);
        faProjection = new float[16];
        faProjectionInverse = new float[16];
        faModelView = new float[16];
        faModelViewInverse = new float[16];
        faShadowProjection = new float[16];
        faShadowProjectionInverse = new float[16];
        faShadowModelView = new float[16];
        faShadowModelViewInverse = new float[16];
        projection = Shaders.nextFloatBuffer(16);
        projectionInverse = Shaders.nextFloatBuffer(16);
        modelView = Shaders.nextFloatBuffer(16);
        modelViewInverse = Shaders.nextFloatBuffer(16);
        shadowProjection = Shaders.nextFloatBuffer(16);
        shadowProjectionInverse = Shaders.nextFloatBuffer(16);
        shadowModelView = Shaders.nextFloatBuffer(16);
        shadowModelViewInverse = Shaders.nextFloatBuffer(16);
        previousProjection = Shaders.nextFloatBuffer(16);
        previousModelView = Shaders.nextFloatBuffer(16);
        tempMatrixDirectBuffer = Shaders.nextFloatBuffer(16);
        tempDirectFloatBuffer = Shaders.nextFloatBuffer(16);
        dfbColorTextures = Shaders.nextIntBuffer(16);
        dfbDepthTextures = Shaders.nextIntBuffer(3);
        sfbColorTextures = Shaders.nextIntBuffer(8);
        sfbDepthTextures = Shaders.nextIntBuffer(2);
        dfbDrawBuffers = Shaders.nextIntBuffer(8);
        sfbDrawBuffers = Shaders.nextIntBuffer(8);
        drawBuffersNone = Shaders.nextIntBuffer(8);
        drawBuffersAll = Shaders.nextIntBuffer(8);
        drawBuffersClear0 = Shaders.nextIntBuffer(8);
        drawBuffersClear1 = Shaders.nextIntBuffer(8);
        drawBuffersClearColor = Shaders.nextIntBuffer(8);
        drawBuffersColorAtt0 = Shaders.nextIntBuffer(8);
        drawBuffersBuffer = Shaders.nextIntBufferArray(33, 8);
        drawBuffersNone.limit(0);
        drawBuffersColorAtt0.put(36064).position(0).limit(1);
        gbufferFormatPattern = Pattern.compile("[ \t]*const[ \t]*int[ \t]*(\\w+)Format[ \t]*=[ \t]*([RGBA81632FUI_SNORM]*)[ \t]*;.*");
        gbufferMipmapEnabledPattern = Pattern.compile("[ \t]*const[ \t]*bool[ \t]*(\\w+)MipmapEnabled[ \t]*=[ \t]*true[ \t]*;.*");
        formatNames = new String[]{"R8", "RG8", "RGB8", "RGBA8", "R8_SNORM", "RG8_SNORM", "RGB8_SNORM", "RGBA8_SNORM", "R16", "RG16", "RGB16", "RGBA16", "R16_SNORM", "RG16_SNORM", "RGB16_SNORM", "RGBA16_SNORM", "R32F", "RG32F", "RGB32F", "RGBA32F", "R32I", "RG32I", "RGB32I", "RGBA32I", "R32UI", "RG32UI", "RGB32UI", "RGBA32UI"};
        formatIds = new int[]{33321, 33323, 32849, 32856, 36756, 36757, 36758, 36759, 33322, 33324, 32852, 32859, 36760, 36761, 36762, 36763, 33326, 33328, 34837, 34836, 33333, 33339, 36227, 36226, 33334, 33340, 36209, 36208};
        patternLoadEntityDataMap = Pattern.compile("\\s*([\\w:]+)\\s*=\\s*([-]?\\d+)\\s*");
        entityData = new int[32];
        entityDataIndex = 0;
    }

    private static ByteBuffer nextByteBuffer(int size) {
        ByteBuffer buffer = bigBuffer;
        int pos = buffer.limit();
        buffer.position(pos).limit(pos + size);
        return buffer.slice();
    }

    private static IntBuffer nextIntBuffer(int size) {
        ByteBuffer buffer = bigBuffer;
        int pos = buffer.limit();
        buffer.position(pos).limit(pos + size * 4);
        return buffer.asIntBuffer();
    }

    private static FloatBuffer nextFloatBuffer(int size) {
        ByteBuffer buffer = bigBuffer;
        int pos = buffer.limit();
        buffer.position(pos).limit(pos + size * 4);
        return buffer.asFloatBuffer();
    }

    private static IntBuffer[] nextIntBufferArray(int count, int size) {
        IntBuffer[] aib = new IntBuffer[count];
        for (int i = 0; i < count; ++i) {
            aib[i] = Shaders.nextIntBuffer(size);
        }
        return aib;
    }

    public static void loadConfig() {
        SMCLog.info("Load ShadersMod configuration.");
        try {
            if (!shaderpacksdir.exists()) {
                shaderpacksdir.mkdir();
            }
        }
        catch (Exception var8) {
            SMCLog.severe("Failed to open the shaderpacks directory: " + shaderpacksdir);
        }
        shadersConfig = new PropertiesOrdered();
        shadersConfig.setProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), "");
        if (configFile.exists()) {
            try {
                FileReader ops = new FileReader(configFile);
                shadersConfig.load(ops);
                ops.close();
            }
            catch (Exception ops) {
                // empty catch block
            }
        }
        if (!configFile.exists()) {
            try {
                Shaders.storeConfig();
            }
            catch (Exception ops) {
                // empty catch block
            }
        }
        EnumShaderOption[] var9 = EnumShaderOption.values();
        for (int i = 0; i < var9.length; ++i) {
            EnumShaderOption op = var9[i];
            String key = op.getPropertyKey();
            String def = op.getValueDefault();
            String val = shadersConfig.getProperty(key, def);
            Shaders.setEnumShaderOption(op, val);
        }
        Shaders.loadShaderPack();
    }

    private static void setEnumShaderOption(EnumShaderOption eso, String str) {
        if (str == null) {
            str = eso.getValueDefault();
        }
        switch (NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[eso.ordinal()]) {
            case 1: {
                configAntialiasingLevel = Config.parseInt(str, 0);
                break;
            }
            case 2: {
                configNormalMap = Config.parseBoolean(str, true);
                break;
            }
            case 3: {
                configSpecularMap = Config.parseBoolean(str, true);
                break;
            }
            case 4: {
                configRenderResMul = Config.parseFloat(str, 1.0f);
                break;
            }
            case 5: {
                configShadowResMul = Config.parseFloat(str, 1.0f);
                break;
            }
            case 6: {
                configHandDepthMul = Config.parseFloat(str, 0.125f);
                break;
            }
            case 7: {
                configCloudShadow = Config.parseBoolean(str, true);
                break;
            }
            case 8: {
                configOldLighting.setPropertyValue(str);
                break;
            }
            case 9: {
                currentshadername = str;
                break;
            }
            case 10: {
                configTweakBlockDamage = Config.parseBoolean(str, true);
                break;
            }
            case 11: {
                configShadowClipFrustrum = Config.parseBoolean(str, true);
                break;
            }
            case 12: {
                configTexMinFilB = Config.parseInt(str, 0);
                break;
            }
            case 13: {
                configTexMinFilN = Config.parseInt(str, 0);
                break;
            }
            case 14: {
                configTexMinFilS = Config.parseInt(str, 0);
                break;
            }
            case 15: {
                configTexMagFilB = Config.parseInt(str, 0);
                break;
            }
            case 16: {
                configTexMagFilB = Config.parseInt(str, 0);
                break;
            }
            case 17: {
                configTexMagFilB = Config.parseInt(str, 0);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown option: " + (Object)((Object)eso));
            }
        }
    }

    public static void storeConfig() {
        SMCLog.info("Save ShadersMod configuration.");
        if (shadersConfig == null) {
            shadersConfig = new PropertiesOrdered();
        }
        EnumShaderOption[] ops = EnumShaderOption.values();
        for (int ex = 0; ex < ops.length; ++ex) {
            EnumShaderOption op = ops[ex];
            String key = op.getPropertyKey();
            String val = Shaders.getEnumShaderOption(op);
            shadersConfig.setProperty(key, val);
        }
        try {
            FileWriter var6 = new FileWriter(configFile);
            shadersConfig.store(var6, null);
            var6.close();
        }
        catch (Exception var5) {
            SMCLog.severe("Error saving configuration: " + var5.getClass().getName() + ": " + var5.getMessage());
        }
    }

    public static String getEnumShaderOption(EnumShaderOption eso) {
        switch (NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[eso.ordinal()]) {
            case 1: {
                return Integer.toString(configAntialiasingLevel);
            }
            case 2: {
                return Boolean.toString(configNormalMap);
            }
            case 3: {
                return Boolean.toString(configSpecularMap);
            }
            case 4: {
                return Float.toString(configRenderResMul);
            }
            case 5: {
                return Float.toString(configShadowResMul);
            }
            case 6: {
                return Float.toString(configHandDepthMul);
            }
            case 7: {
                return Boolean.toString(configCloudShadow);
            }
            case 8: {
                return configOldLighting.getPropertyValue();
            }
            case 9: {
                return currentshadername;
            }
            case 10: {
                return Boolean.toString(configTweakBlockDamage);
            }
            case 11: {
                return Boolean.toString(configShadowClipFrustrum);
            }
            case 12: {
                return Integer.toString(configTexMinFilB);
            }
            case 13: {
                return Integer.toString(configTexMinFilN);
            }
            case 14: {
                return Integer.toString(configTexMinFilS);
            }
            case 15: {
                return Integer.toString(configTexMagFilB);
            }
            case 16: {
                return Integer.toString(configTexMagFilB);
            }
            case 17: {
                return Integer.toString(configTexMagFilB);
            }
        }
        throw new IllegalArgumentException("Unknown option: " + (Object)((Object)eso));
    }

    public static void setShaderPack(String par1name) {
        currentshadername = par1name;
        shadersConfig.setProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), par1name);
        Shaders.loadShaderPack();
    }

    public static void loadShaderPack() {
        String packName;
        boolean shaderPackLoadedPrev = shaderPackLoaded;
        boolean oldLightingPrev = Shaders.isOldLighting();
        shaderPackLoaded = false;
        if (shaderPack != null) {
            shaderPack.close();
            shaderPack = null;
            shaderPackResources.clear();
            shaderPackDimensions.clear();
            shaderPackOptions = null;
            shaderPackProfiles = null;
            shaderPackGuiScreens = null;
            shaderPackClouds.resetValue();
            shaderPackDynamicHandLight.resetValue();
            shaderPackOldLighting.resetValue();
        }
        boolean shadersBlocked = false;
        if (Config.isAntialiasing()) {
            SMCLog.info("Shaders can not be loaded, Antialiasing is enabled: " + Config.getAntialiasingLevel() + "x");
            shadersBlocked = true;
        }
        if (Config.isAnisotropicFiltering()) {
            SMCLog.info("Shaders can not be loaded, Anisotropic Filtering is enabled: " + Config.getAnisotropicFilterLevel() + "x");
            shadersBlocked = true;
        }
        if (Config.isFastRender()) {
            SMCLog.info("Shaders can not be loaded, Fast Render is enabled.");
            shadersBlocked = true;
        }
        if (!((packName = shadersConfig.getProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), packNameDefault)).isEmpty() || packName.equals(packNameNone) || shadersBlocked)) {
            if (packName.equals(packNameDefault)) {
                shaderPack = new ShaderPackDefault();
                shaderPackLoaded = true;
            } else {
                try {
                    File formatChanged = new File(shaderpacksdir, packName);
                    if (formatChanged.isDirectory()) {
                        shaderPack = new ShaderPackFolder(packName, formatChanged);
                        shaderPackLoaded = true;
                    } else if (formatChanged.isFile() && packName.toLowerCase().endsWith(".zip")) {
                        shaderPack = new ShaderPackZip(packName, formatChanged);
                        shaderPackLoaded = true;
                    }
                }
                catch (Exception formatChanged) {
                    // empty catch block
                }
            }
        }
        if (shaderPack != null) {
            SMCLog.info("Loaded shaderpack: " + Shaders.getShaderPackName());
        } else {
            SMCLog.info("No shaderpack loaded.");
            shaderPack = new ShaderPackNone();
        }
        Shaders.loadShaderPackResources();
        Shaders.loadShaderPackDimensions();
        shaderPackOptions = Shaders.loadShaderPackOptions();
        Shaders.loadShaderPackProperties();
        boolean formatChanged1 = shaderPackLoaded ^ shaderPackLoadedPrev;
        boolean oldLightingChanged = Shaders.isOldLighting() ^ oldLightingPrev;
        if (formatChanged1 || oldLightingChanged) {
            DefaultVertexFormats.updateVertexFormats();
            if (Reflector.LightUtil.exists()) {
                Reflector.LightUtil_itemConsumer.setValue(null);
                Reflector.LightUtil_tessellator.setValue(null);
            }
            Shaders.updateBlockLightLevel();
            mc.func_175603_A();
        }
    }

    private static void loadShaderPackDimensions() {
        shaderPackDimensions.clear();
        StringBuffer sb = new StringBuffer();
        for (int i = -128; i <= 128; ++i) {
            String worldDir = "/shaders/world" + i;
            if (!shaderPack.hasDirectory(worldDir)) continue;
            shaderPackDimensions.add(i);
            sb.append(" " + i);
        }
        if (sb.length() > 0) {
            Config.dbg("[Shaders] Dimensions:" + sb);
        }
    }

    private static void loadShaderPackProperties() {
        shaderPackClouds.resetValue();
        shaderPackDynamicHandLight.resetValue();
        shaderPackOldLighting.resetValue();
        if (shaderPack != null) {
            String path = "/shaders/shaders.properties";
            try {
                InputStream e = shaderPack.getResourceAsStream(path);
                if (e == null) {
                    return;
                }
                PropertiesOrdered props = new PropertiesOrdered();
                props.load(e);
                e.close();
                shaderPackClouds.loadFrom(props);
                shaderPackDynamicHandLight.loadFrom(props);
                shaderPackOldLighting.loadFrom(props);
                shaderPackProfiles = ShaderPackParser.parseProfiles(props, shaderPackOptions);
                shaderPackGuiScreens = ShaderPackParser.parseGuiScreens(props, shaderPackProfiles, shaderPackOptions);
            }
            catch (IOException var3) {
                Config.warn("[Shaders] Error reading: " + path);
            }
        }
    }

    public static ShaderOption[] getShaderPackOptions(String screenName) {
        Object[] ops = (ShaderOption[])shaderPackOptions.clone();
        if (shaderPackGuiScreens == null) {
            if (shaderPackProfiles != null) {
                ShaderOptionProfile var8 = new ShaderOptionProfile(shaderPackProfiles, (ShaderOption[])ops);
                ops = (ShaderOption[])Config.addObjectToArray(ops, var8, 0);
            }
            ops = Shaders.getVisibleOptions((ShaderOption[])ops);
            return ops;
        }
        String key = screenName != null ? "screen." + screenName : "screen";
        ShaderOption[] sos = shaderPackGuiScreens.get(key);
        if (sos == null) {
            return new ShaderOption[0];
        }
        ArrayList<ShaderOption> list = new ArrayList<ShaderOption>();
        for (int sosExp = 0; sosExp < sos.length; ++sosExp) {
            ShaderOption so = sos[sosExp];
            if (so == null) {
                list.add(null);
                continue;
            }
            if (so instanceof ShaderOptionRest) {
                ShaderOption[] restOps = Shaders.getShaderOptionsRest(shaderPackGuiScreens, (ShaderOption[])ops);
                list.addAll(Arrays.asList(restOps));
                continue;
            }
            list.add(so);
        }
        ShaderOption[] var9 = list.toArray(new ShaderOption[list.size()]);
        return var9;
    }

    private static ShaderOption[] getShaderOptionsRest(Map<String, ShaderOption[]> mapScreens, ShaderOption[] ops) {
        HashSet<String> setNames = new HashSet<String>();
        Set<String> keys = mapScreens.keySet();
        for (String sos : keys) {
            ShaderOption[] so = mapScreens.get(sos);
            for (int name = 0; name < so.length; ++name) {
                ShaderOption so1 = so[name];
                if (so1 == null) continue;
                setNames.add(so1.getName());
            }
        }
        ArrayList<ShaderOption> var9 = new ArrayList<ShaderOption>();
        for (int var10 = 0; var10 < ops.length; ++var10) {
            String var13;
            ShaderOption var12 = ops[var10];
            if (!var12.isVisible() || setNames.contains(var13 = var12.getName())) continue;
            var9.add(var12);
        }
        ShaderOption[] var11 = var9.toArray(new ShaderOption[var9.size()]);
        return var11;
    }

    public static ShaderOption getShaderOption(String name) {
        return ShaderUtils.getShaderOption(name, shaderPackOptions);
    }

    public static ShaderOption[] getShaderPackOptions() {
        return shaderPackOptions;
    }

    private static ShaderOption[] getVisibleOptions(ShaderOption[] ops) {
        ArrayList<ShaderOption> list = new ArrayList<ShaderOption>();
        for (int sos = 0; sos < ops.length; ++sos) {
            ShaderOption so = ops[sos];
            if (!so.isVisible()) continue;
            list.add(so);
        }
        ShaderOption[] var4 = list.toArray(new ShaderOption[list.size()]);
        return var4;
    }

    public static void saveShaderPackOptions() {
        Shaders.saveShaderPackOptions(shaderPackOptions, shaderPack);
    }

    private static void saveShaderPackOptions(ShaderOption[] sos, IShaderPack sp) {
        Properties props = new Properties();
        if (shaderPackOptions != null) {
            for (int e = 0; e < sos.length; ++e) {
                ShaderOption so = sos[e];
                if (!so.isChanged() || !so.isEnabled()) continue;
                props.setProperty(so.getName(), so.getValue());
            }
        }
        try {
            Shaders.saveOptionProperties(sp, props);
        }
        catch (IOException var5) {
            Config.warn("[Shaders] Error saving configuration for " + shaderPack.getName());
            var5.printStackTrace();
        }
    }

    private static void saveOptionProperties(IShaderPack sp, Properties props) throws IOException {
        String path = String.valueOf(shaderpacksdirname) + "/" + sp.getName() + ".txt";
        File propFile = new File(Minecraft.getMinecraft().mcDataDir, path);
        if (props.isEmpty()) {
            propFile.delete();
        } else {
            FileOutputStream fos = new FileOutputStream(propFile);
            props.store(fos, null);
            fos.flush();
            fos.close();
        }
    }

    private static ShaderOption[] loadShaderPackOptions() {
        try {
            ShaderOption[] e = ShaderPackParser.parseShaderPackOptions(shaderPack, programNames, shaderPackDimensions);
            Properties props = Shaders.loadOptionProperties(shaderPack);
            for (int i = 0; i < e.length; ++i) {
                ShaderOption so = e[i];
                String val = props.getProperty(so.getName());
                if (val == null) continue;
                so.resetValue();
                if (so.setValue(val)) continue;
                Config.warn("[Shaders] Invalid value, option: " + so.getName() + ", value: " + val);
            }
            return e;
        }
        catch (IOException var5) {
            Config.warn("[Shaders] Error reading configuration for " + shaderPack.getName());
            var5.printStackTrace();
            return null;
        }
    }

    private static Properties loadOptionProperties(IShaderPack sp) throws IOException {
        Properties props = new Properties();
        String path = String.valueOf(shaderpacksdirname) + "/" + sp.getName() + ".txt";
        File propFile = new File(Minecraft.getMinecraft().mcDataDir, path);
        if (propFile.exists() && propFile.isFile() && propFile.canRead()) {
            FileInputStream fis = new FileInputStream(propFile);
            props.load(fis);
            fis.close();
            return props;
        }
        return props;
    }

    public static ShaderOption[] getChangedOptions(ShaderOption[] ops) {
        ArrayList<ShaderOption> list = new ArrayList<ShaderOption>();
        for (int cops = 0; cops < ops.length; ++cops) {
            ShaderOption op = ops[cops];
            if (!op.isEnabled() || !op.isChanged()) continue;
            list.add(op);
        }
        ShaderOption[] var4 = list.toArray(new ShaderOption[list.size()]);
        return var4;
    }

    private static String applyOptions(String line, ShaderOption[] ops) {
        if (ops != null && ops.length > 0) {
            for (int i = 0; i < ops.length; ++i) {
                ShaderOption op = ops[i];
                String opName = op.getName();
                if (!op.matchesLine(line)) continue;
                line = op.getSourceLine();
                break;
            }
            return line;
        }
        return line;
    }

    static ArrayList listOfShaders() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(packNameNone);
        list.add(packNameDefault);
        try {
            if (!shaderpacksdir.exists()) {
                shaderpacksdir.mkdir();
            }
            File[] e = shaderpacksdir.listFiles();
            for (int i = 0; i < e.length; ++i) {
                File file = e[i];
                String name = file.getName();
                if (file.isDirectory()) {
                    File subDir = new File(file, "shaders");
                    if (!subDir.exists() || !subDir.isDirectory()) continue;
                    list.add(name);
                    continue;
                }
                if (!file.isFile() || !name.toLowerCase().endsWith(".zip")) continue;
                list.add(name);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return list;
    }

    static String versiontostring(int vv) {
        String vs = Integer.toString(vv);
        return String.valueOf(Integer.toString(Integer.parseInt(vs.substring(1, 3)))) + "." + Integer.toString(Integer.parseInt(vs.substring(3, 5))) + "." + Integer.toString(Integer.parseInt(vs.substring(5)));
    }

    static void checkOptifine() {
    }

    public static int checkFramebufferStatus(String location) {
        int status = EXTFramebufferObject.glCheckFramebufferStatusEXT((int)36160);
        if (status != 36053) {
            System.err.format("FramebufferStatus 0x%04X at %s\n", status, location);
        }
        return status;
    }

    public static int checkGLError(String location) {
        boolean skipPrint;
        int errorCode = GL11.glGetError();
        if (errorCode != 0 && !(skipPrint = false)) {
            if (errorCode == 1286) {
                int status = EXTFramebufferObject.glCheckFramebufferStatusEXT((int)36160);
                System.err.format("GL error 0x%04X: %s (Fb status 0x%04X) at %s\n", errorCode, GLU.gluErrorString((int)errorCode), status, location);
            } else {
                System.err.format("GL error 0x%04X: %s at %s\n", errorCode, GLU.gluErrorString((int)errorCode), location);
            }
        }
        return errorCode;
    }

    public static int checkGLError(String location, String info) {
        int errorCode = GL11.glGetError();
        if (errorCode != 0) {
            System.err.format("GL error 0x%04x: %s at %s %s\n", errorCode, GLU.gluErrorString((int)errorCode), location, info);
        }
        return errorCode;
    }

    public static int checkGLError(String location, String info1, String info2) {
        int errorCode = GL11.glGetError();
        if (errorCode != 0) {
            System.err.format("GL error 0x%04x: %s at %s %s %s\n", errorCode, GLU.gluErrorString((int)errorCode), location, info1, info2);
        }
        return errorCode;
    }

    private static void printChat(String str) {
        Shaders.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(str));
    }

    private static void printChatAndLogError(String str) {
        SMCLog.severe(str);
        Shaders.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(str));
    }

    public static void printIntBuffer(String title, IntBuffer buf) {
        StringBuilder sb = new StringBuilder(128);
        sb.append(title).append(" [pos ").append(buf.position()).append(" lim ").append(buf.limit()).append(" cap ").append(buf.capacity()).append(" :");
        int lim = buf.limit();
        for (int i = 0; i < lim; ++i) {
            sb.append(" ").append(buf.get(i));
        }
        sb.append("]");
        SMCLog.info(sb.toString());
    }

    public static void startup(Minecraft mc) {
        Shaders.checkShadersModInstalled();
        Shaders.mc = mc;
        capabilities = GLContext.getCapabilities();
        glVersionString = GL11.glGetString((int)7938);
        glVendorString = GL11.glGetString((int)7936);
        glRendererString = GL11.glGetString((int)7937);
        SMCLog.info("ShadersMod version: 2.4.12");
        SMCLog.info("OpenGL Version: " + glVersionString);
        SMCLog.info("Vendor:  " + glVendorString);
        SMCLog.info("Renderer: " + glRendererString);
        SMCLog.info("Capabilities: " + (Shaders.capabilities.OpenGL20 ? " 2.0 " : " - ") + (Shaders.capabilities.OpenGL21 ? " 2.1 " : " - ") + (Shaders.capabilities.OpenGL30 ? " 3.0 " : " - ") + (Shaders.capabilities.OpenGL32 ? " 3.2 " : " - ") + (Shaders.capabilities.OpenGL40 ? " 4.0 " : " - "));
        SMCLog.info("GL_MAX_DRAW_BUFFERS: " + GL11.glGetInteger((int)34852));
        SMCLog.info("GL_MAX_COLOR_ATTACHMENTS_EXT: " + GL11.glGetInteger((int)36063));
        SMCLog.info("GL_MAX_TEXTURE_IMAGE_UNITS: " + GL11.glGetInteger((int)34930));
        hasGlGenMipmap = Shaders.capabilities.OpenGL30;
        Shaders.loadConfig();
    }

    private static String toStringYN(boolean b) {
        return b ? "Y" : "N";
    }

    public static void updateBlockLightLevel() {
        if (Shaders.isOldLighting()) {
            blockLightLevel05 = 0.5f;
            blockLightLevel06 = 0.6f;
            blockLightLevel08 = 0.8f;
        } else {
            blockLightLevel05 = 1.0f;
            blockLightLevel06 = 1.0f;
            blockLightLevel08 = 1.0f;
        }
    }

    public static boolean isDynamicHandLight() {
        return !shaderPackDynamicHandLight.isDefault() ? shaderPackDynamicHandLight.isTrue() : true;
    }

    public static boolean isOldLighting() {
        return !configOldLighting.isDefault() ? configOldLighting.isTrue() : (!shaderPackOldLighting.isDefault() ? shaderPackOldLighting.isTrue() : true);
    }

    public static void init() {
        boolean firstInit;
        if (!isInitializedOnce) {
            isInitializedOnce = true;
            firstInit = true;
        } else {
            firstInit = false;
        }
        if (!isShaderPackInitialized) {
            int var12;
            String n;
            int maxDrawBuffers;
            Shaders.checkGLError("Shaders.init pre");
            if (Shaders.getShaderPackName() != null) {
                // empty if block
            }
            if (!Shaders.capabilities.OpenGL20) {
                Shaders.printChatAndLogError("No OpenGL 2.0");
            }
            if (!Shaders.capabilities.GL_EXT_framebuffer_object) {
                Shaders.printChatAndLogError("No EXT_framebuffer_object");
            }
            dfbDrawBuffers.position(0).limit(8);
            dfbColorTextures.position(0).limit(16);
            dfbDepthTextures.position(0).limit(3);
            sfbDrawBuffers.position(0).limit(8);
            sfbDepthTextures.position(0).limit(2);
            sfbColorTextures.position(0).limit(8);
            usedColorBuffers = 4;
            usedDepthBuffers = 1;
            usedShadowColorBuffers = 0;
            usedShadowDepthBuffers = 0;
            usedColorAttachs = 1;
            usedDrawBuffers = 1;
            Arrays.fill(gbuffersFormat, 6408);
            Arrays.fill(shadowHardwareFilteringEnabled, false);
            Arrays.fill(shadowMipmapEnabled, false);
            Arrays.fill(shadowFilterNearest, false);
            Arrays.fill(shadowColorMipmapEnabled, false);
            Arrays.fill(shadowColorFilterNearest, false);
            centerDepthSmoothEnabled = false;
            noiseTextureEnabled = false;
            sunPathRotation = 0.0f;
            shadowIntervalSize = 2.0f;
            aoLevel = 0.8f;
            blockAoLight = 1.0f - aoLevel;
            useEntityAttrib = false;
            useMidTexCoordAttrib = false;
            useMultiTexCoord3Attrib = false;
            useTangentAttrib = false;
            waterShadowEnabled = false;
            updateChunksErrorRecorded = false;
            Shaders.updateBlockLightLevel();
            ShaderProfile activeProfile = ShaderUtils.detectProfile(shaderPackProfiles, shaderPackOptions, false);
            String worldPrefix = "";
            if (currentWorld != null && shaderPackDimensions.contains(maxDrawBuffers = Shaders.currentWorld.provider.getDimensionId())) {
                worldPrefix = "world" + maxDrawBuffers + "/";
            }
            if (saveFinalShaders) {
                Shaders.clearDirectory(new File(shaderpacksdir, "debug"));
            }
            for (maxDrawBuffers = 0; maxDrawBuffers < 33; ++maxDrawBuffers) {
                int intbuf;
                String drawBuffersMap = programNames[maxDrawBuffers];
                if (drawBuffersMap.equals("")) {
                    Shaders.programsRef[maxDrawBuffers] = 0;
                    Shaders.programsID[maxDrawBuffers] = 0;
                    Shaders.programsDrawBufSettings[maxDrawBuffers] = null;
                    Shaders.programsColorAtmSettings[maxDrawBuffers] = null;
                    Shaders.programsCompositeMipmapSetting[maxDrawBuffers] = 0;
                    continue;
                }
                newDrawBufSetting = null;
                newColorAtmSetting = null;
                newCompositeMipmapSetting = 0;
                String i = String.valueOf(worldPrefix) + drawBuffersMap;
                if (activeProfile != null && activeProfile.isProgramDisabled(i)) {
                    SMCLog.info("Program disabled: " + i);
                    drawBuffersMap = "<disabled>";
                    i = String.valueOf(worldPrefix) + drawBuffersMap;
                }
                if ((intbuf = Shaders.setupProgram(maxDrawBuffers, String.valueOf(n = "/shaders/" + i) + ".vsh", String.valueOf(n) + ".fsh")) > 0) {
                    SMCLog.info("Program loaded: " + i);
                }
                Shaders.programsID[maxDrawBuffers] = Shaders.programsRef[maxDrawBuffers] = intbuf;
                Shaders.programsDrawBufSettings[maxDrawBuffers] = intbuf != 0 ? newDrawBufSetting : null;
                Shaders.programsColorAtmSettings[maxDrawBuffers] = intbuf != 0 ? newColorAtmSetting : null;
                Shaders.programsCompositeMipmapSetting[maxDrawBuffers] = intbuf != 0 ? newCompositeMipmapSetting : 0;
            }
            maxDrawBuffers = GL11.glGetInteger((int)34852);
            new HashMap();
            for (var12 = 0; var12 < 33; ++var12) {
                Arrays.fill(programsToggleColorTextures[var12], false);
                if (var12 == 29) {
                    Shaders.programsDrawBuffers[var12] = null;
                    continue;
                }
                if (programsID[var12] == 0) {
                    if (var12 == 30) {
                        Shaders.programsDrawBuffers[var12] = drawBuffersNone;
                        continue;
                    }
                    Shaders.programsDrawBuffers[var12] = drawBuffersColorAtt0;
                    continue;
                }
                n = programsDrawBufSettings[var12];
                if (n != null) {
                    IntBuffer var14 = drawBuffersBuffer[var12];
                    int numDB = n.length();
                    if (numDB > usedDrawBuffers) {
                        usedDrawBuffers = numDB;
                    }
                    if (numDB > maxDrawBuffers) {
                        numDB = maxDrawBuffers;
                    }
                    Shaders.programsDrawBuffers[var12] = var14;
                    var14.limit(numDB);
                    for (int i1 = 0; i1 < numDB; ++i1) {
                        int drawBuffer = 0;
                        if (n.length() > i1) {
                            int ca = n.charAt(i1) - 48;
                            if (var12 != 30) {
                                if (ca >= 0 && ca <= 7) {
                                    Shaders.programsToggleColorTextures[var12][ca] = true;
                                    drawBuffer = ca + 36064;
                                    if (ca > usedColorAttachs) {
                                        usedColorAttachs = ca;
                                    }
                                    if (ca > usedColorBuffers) {
                                        usedColorBuffers = ca;
                                    }
                                }
                            } else if (ca >= 0 && ca <= 1) {
                                drawBuffer = ca + 36064;
                                if (ca > usedShadowColorBuffers) {
                                    usedShadowColorBuffers = ca;
                                }
                            }
                        }
                        var14.put(i1, drawBuffer);
                    }
                    continue;
                }
                if (var12 != 30 && var12 != 31 && var12 != 32) {
                    Shaders.programsDrawBuffers[var12] = dfbDrawBuffers;
                    usedDrawBuffers = usedColorBuffers;
                    Arrays.fill(programsToggleColorTextures[var12], 0, usedColorBuffers, true);
                    continue;
                }
                Shaders.programsDrawBuffers[var12] = sfbDrawBuffers;
            }
            usedColorAttachs = usedColorBuffers;
            shadowPassInterval = usedShadowDepthBuffers > 0 ? 1 : 0;
            shouldSkipDefaultShadow = usedShadowDepthBuffers > 0;
            SMCLog.info("usedColorBuffers: " + usedColorBuffers);
            SMCLog.info("usedDepthBuffers: " + usedDepthBuffers);
            SMCLog.info("usedShadowColorBuffers: " + usedShadowColorBuffers);
            SMCLog.info("usedShadowDepthBuffers: " + usedShadowDepthBuffers);
            SMCLog.info("usedColorAttachs: " + usedColorAttachs);
            SMCLog.info("usedDrawBuffers: " + usedDrawBuffers);
            dfbDrawBuffers.position(0).limit(usedDrawBuffers);
            dfbColorTextures.position(0).limit(usedColorBuffers * 2);
            for (var12 = 0; var12 < usedDrawBuffers; ++var12) {
                dfbDrawBuffers.put(var12, 36064 + var12);
            }
            if (usedDrawBuffers > maxDrawBuffers) {
                Shaders.printChatAndLogError("[Shaders] Error: Not enough draw buffers, needed: " + usedDrawBuffers + ", available: " + maxDrawBuffers);
            }
            sfbDrawBuffers.position(0).limit(usedShadowColorBuffers);
            for (var12 = 0; var12 < usedShadowColorBuffers; ++var12) {
                sfbDrawBuffers.put(var12, 36064 + var12);
            }
            for (var12 = 0; var12 < 33; ++var12) {
                int var13 = var12;
                while (programsID[var13] == 0 && programBackups[var13] != var13) {
                    var13 = programBackups[var13];
                }
                if (var13 == var12 || var12 == 30) continue;
                Shaders.programsID[var12] = programsID[var13];
                Shaders.programsDrawBufSettings[var12] = programsDrawBufSettings[var13];
                Shaders.programsDrawBuffers[var12] = programsDrawBuffers[var13];
            }
            Shaders.resize();
            Shaders.resizeShadow();
            if (noiseTextureEnabled) {
                Shaders.setupNoiseTexture();
            }
            if (defaultTexture == null) {
                defaultTexture = ShadersTex.createDefaultTexture();
            }
            GlStateManager.pushMatrix();
            GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
            Shaders.preCelestialRotate();
            Shaders.postCelestialRotate();
            GlStateManager.popMatrix();
            isShaderPackInitialized = true;
            Shaders.loadEntityDataMap();
            Shaders.resetDisplayList();
            if (!firstInit) {
                // empty if block
            }
            Shaders.checkGLError("Shaders.init");
        }
    }

    public static void resetDisplayList() {
        ++numberResetDisplayList;
        needResetModels = true;
        SMCLog.info("Reset world renderers");
        Shaders.mc.renderGlobal.loadRenderers();
    }

    public static void resetDisplayListModels() {
        if (needResetModels) {
            needResetModels = false;
            SMCLog.info("Reset model renderers");
            for (Render ren : mc.getRenderManager().getEntityRenderMap().values()) {
                if (!(ren instanceof RendererLivingEntity)) continue;
                RendererLivingEntity rle = (RendererLivingEntity)ren;
                Shaders.resetDisplayListModel(rle.getMainModel());
            }
        }
    }

    public static void resetDisplayListModel(ModelBase model) {
        if (model != null) {
            for (Object obj : model.boxList) {
                if (!(obj instanceof ModelRenderer)) continue;
                Shaders.resetDisplayListModelRenderer((ModelRenderer)obj);
            }
        }
    }

    public static void resetDisplayListModelRenderer(ModelRenderer mrr) {
        mrr.resetDisplayList();
        if (mrr.childModels != null) {
            int n = mrr.childModels.size();
            for (int i = 0; i < n; ++i) {
                Shaders.resetDisplayListModelRenderer((ModelRenderer)mrr.childModels.get(i));
            }
        }
    }

    private static int setupProgram(int program, String vShaderPath, String fShaderPath) {
        Shaders.checkGLError("pre setupProgram");
        int programid = ARBShaderObjects.glCreateProgramObjectARB();
        Shaders.checkGLError("create");
        if (programid != 0) {
            progUseEntityAttrib = false;
            progUseMidTexCoordAttrib = false;
            progUseTangentAttrib = false;
            int vShader = Shaders.createVertShader(vShaderPath);
            int fShader = Shaders.createFragShader(fShaderPath);
            Shaders.checkGLError("create");
            if (vShader == 0 && fShader == 0) {
                ARBShaderObjects.glDeleteObjectARB((int)programid);
                programid = 0;
            } else {
                if (vShader != 0) {
                    ARBShaderObjects.glAttachObjectARB((int)programid, (int)vShader);
                    Shaders.checkGLError("attach");
                }
                if (fShader != 0) {
                    ARBShaderObjects.glAttachObjectARB((int)programid, (int)fShader);
                    Shaders.checkGLError("attach");
                }
                if (progUseEntityAttrib) {
                    ARBVertexShader.glBindAttribLocationARB((int)programid, (int)entityAttrib, (CharSequence)"mc_Entity");
                    Shaders.checkGLError("mc_Entity");
                }
                if (progUseMidTexCoordAttrib) {
                    ARBVertexShader.glBindAttribLocationARB((int)programid, (int)midTexCoordAttrib, (CharSequence)"mc_midTexCoord");
                    Shaders.checkGLError("mc_midTexCoord");
                }
                if (progUseTangentAttrib) {
                    ARBVertexShader.glBindAttribLocationARB((int)programid, (int)tangentAttrib, (CharSequence)"at_tangent");
                    Shaders.checkGLError("at_tangent");
                }
                ARBShaderObjects.glLinkProgramARB((int)programid);
                if (GL20.glGetProgrami((int)programid, (int)35714) != 1) {
                    SMCLog.severe("Error linking program: " + programid);
                }
                Shaders.printLogInfo(programid, String.valueOf(vShaderPath) + ", " + fShaderPath);
                if (vShader != 0) {
                    ARBShaderObjects.glDetachObjectARB((int)programid, (int)vShader);
                    ARBShaderObjects.glDeleteObjectARB((int)vShader);
                }
                if (fShader != 0) {
                    ARBShaderObjects.glDetachObjectARB((int)programid, (int)fShader);
                    ARBShaderObjects.glDeleteObjectARB((int)fShader);
                }
                Shaders.programsID[program] = programid;
                Shaders.useProgram(program);
                ARBShaderObjects.glValidateProgramARB((int)programid);
                Shaders.useProgram(0);
                Shaders.printLogInfo(programid, String.valueOf(vShaderPath) + ", " + fShaderPath);
                int valid = GL20.glGetProgrami((int)programid, (int)35715);
                if (valid != 1) {
                    String Q = "\"";
                    Shaders.printChatAndLogError("[Shaders] Error: Invalid program " + Q + programNames[program] + Q);
                    ARBShaderObjects.glDeleteObjectARB((int)programid);
                    programid = 0;
                }
            }
        }
        return programid;
    }

    private static int createVertShader(String filename) {
        int vertShader = ARBShaderObjects.glCreateShaderObjectARB((int)35633);
        if (vertShader == 0) {
            return 0;
        }
        StringBuilder vertexCode = new StringBuilder(131072);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(shaderPack.getResourceAsStream(filename)));
        }
        catch (Exception var8) {
            try {
                reader = new BufferedReader(new FileReader(new File(filename)));
            }
            catch (Exception var7) {
                ARBShaderObjects.glDeleteObjectARB((int)vertShader);
                return 0;
            }
        }
        ShaderOption[] activeOptions = Shaders.getChangedOptions(shaderPackOptions);
        if (reader != null) {
            try {
                String line;
                reader = ShaderPackParser.resolveIncludes(reader, filename, shaderPack, 0);
                while ((line = reader.readLine()) != null) {
                    line = Shaders.applyOptions(line, activeOptions);
                    vertexCode.append(line).append('\n');
                    if (line.matches("attribute [_a-zA-Z0-9]+ mc_Entity.*")) {
                        useEntityAttrib = true;
                        progUseEntityAttrib = true;
                        continue;
                    }
                    if (line.matches("attribute [_a-zA-Z0-9]+ mc_midTexCoord.*")) {
                        useMidTexCoordAttrib = true;
                        progUseMidTexCoordAttrib = true;
                        continue;
                    }
                    if (line.matches(".*gl_MultiTexCoord3.*")) {
                        useMultiTexCoord3Attrib = true;
                        continue;
                    }
                    if (!line.matches("attribute [_a-zA-Z0-9]+ at_tangent.*")) continue;
                    useTangentAttrib = true;
                    progUseTangentAttrib = true;
                }
                reader.close();
            }
            catch (Exception var9) {
                SMCLog.severe("Couldn't read " + filename + "!");
                var9.printStackTrace();
                ARBShaderObjects.glDeleteObjectARB((int)vertShader);
                return 0;
            }
        }
        if (saveFinalShaders) {
            Shaders.saveShader(filename, vertexCode.toString());
        }
        ARBShaderObjects.glShaderSourceARB((int)vertShader, (CharSequence)vertexCode);
        ARBShaderObjects.glCompileShaderARB((int)vertShader);
        if (GL20.glGetShaderi((int)vertShader, (int)35713) != 1) {
            SMCLog.severe("Error compiling vertex shader: " + filename);
        }
        Shaders.printShaderLogInfo(vertShader, filename);
        return vertShader;
    }

    private static int createFragShader(String filename) {
        int fragShader = ARBShaderObjects.glCreateShaderObjectARB((int)35632);
        if (fragShader == 0) {
            return 0;
        }
        StringBuilder fragCode = new StringBuilder(131072);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(shaderPack.getResourceAsStream(filename)));
        }
        catch (Exception var12) {
            try {
                reader = new BufferedReader(new FileReader(new File(filename)));
            }
            catch (Exception var11) {
                ARBShaderObjects.glDeleteObjectARB((int)fragShader);
                return 0;
            }
        }
        ShaderOption[] activeOptions = Shaders.getChangedOptions(shaderPackOptions);
        if (reader != null) {
            try {
                String line;
                reader = ShaderPackParser.resolveIncludes(reader, filename, shaderPack, 0);
                while ((line = reader.readLine()) != null) {
                    Matcher e1;
                    String[] e;
                    line = Shaders.applyOptions(line, activeOptions);
                    fragCode.append(line).append('\n');
                    if (line.matches("#version .*")) continue;
                    if (line.matches("uniform [ _a-zA-Z0-9]+ shadow;.*")) {
                        if (usedShadowDepthBuffers >= 1) continue;
                        usedShadowDepthBuffers = 1;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ watershadow;.*")) {
                        waterShadowEnabled = true;
                        if (usedShadowDepthBuffers >= 2) continue;
                        usedShadowDepthBuffers = 2;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ shadowtex0;.*")) {
                        if (usedShadowDepthBuffers >= 1) continue;
                        usedShadowDepthBuffers = 1;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ shadowtex1;.*")) {
                        if (usedShadowDepthBuffers >= 2) continue;
                        usedShadowDepthBuffers = 2;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ shadowcolor;.*")) {
                        if (usedShadowColorBuffers >= 1) continue;
                        usedShadowColorBuffers = 1;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ shadowcolor0;.*")) {
                        if (usedShadowColorBuffers >= 1) continue;
                        usedShadowColorBuffers = 1;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ shadowcolor1;.*")) {
                        if (usedShadowColorBuffers >= 2) continue;
                        usedShadowColorBuffers = 2;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ shadowcolor2;.*")) {
                        if (usedShadowColorBuffers >= 3) continue;
                        usedShadowColorBuffers = 3;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ shadowcolor3;.*")) {
                        if (usedShadowColorBuffers >= 4) continue;
                        usedShadowColorBuffers = 4;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ depthtex0;.*")) {
                        if (usedDepthBuffers >= 1) continue;
                        usedDepthBuffers = 1;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ depthtex1;.*")) {
                        if (usedDepthBuffers >= 2) continue;
                        usedDepthBuffers = 2;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ depthtex2;.*")) {
                        if (usedDepthBuffers >= 3) continue;
                        usedDepthBuffers = 3;
                        continue;
                    }
                    if (line.matches("uniform [ _a-zA-Z0-9]+ gdepth;.*")) {
                        if (gbuffersFormat[1] != 6408) continue;
                        Shaders.gbuffersFormat[1] = 34836;
                        continue;
                    }
                    if (usedColorBuffers < 5 && line.matches("uniform [ _a-zA-Z0-9]+ gaux1;.*")) {
                        usedColorBuffers = 5;
                        continue;
                    }
                    if (usedColorBuffers < 6 && line.matches("uniform [ _a-zA-Z0-9]+ gaux2;.*")) {
                        usedColorBuffers = 6;
                        continue;
                    }
                    if (usedColorBuffers < 7 && line.matches("uniform [ _a-zA-Z0-9]+ gaux3;.*")) {
                        usedColorBuffers = 7;
                        continue;
                    }
                    if (usedColorBuffers < 8 && line.matches("uniform [ _a-zA-Z0-9]+ gaux4;.*")) {
                        usedColorBuffers = 8;
                        continue;
                    }
                    if (usedColorBuffers < 5 && line.matches("uniform [ _a-zA-Z0-9]+ colortex4;.*")) {
                        usedColorBuffers = 5;
                        continue;
                    }
                    if (usedColorBuffers < 6 && line.matches("uniform [ _a-zA-Z0-9]+ colortex5;.*")) {
                        usedColorBuffers = 6;
                        continue;
                    }
                    if (usedColorBuffers < 7 && line.matches("uniform [ _a-zA-Z0-9]+ colortex6;.*")) {
                        usedColorBuffers = 7;
                        continue;
                    }
                    if (usedColorBuffers < 8 && line.matches("uniform [ _a-zA-Z0-9]+ colortex7;.*")) {
                        usedColorBuffers = 8;
                        continue;
                    }
                    if (usedColorBuffers < 8 && line.matches("uniform [ _a-zA-Z0-9]+ centerDepthSmooth;.*")) {
                        centerDepthSmoothEnabled = true;
                        continue;
                    }
                    if (line.matches("/\\* SHADOWRES:[0-9]+ \\*/.*")) {
                        e = line.split("(:| )", 4);
                        SMCLog.info("Shadow map resolution: " + e[2]);
                        spShadowMapWidth = spShadowMapHeight = Integer.parseInt(e[2]);
                        shadowMapWidth = shadowMapHeight = Math.round((float)spShadowMapWidth * configShadowResMul);
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*int[ \t]*shadowMapResolution[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        e = line.split("(=[ \t]*|;)");
                        SMCLog.info("Shadow map resolution: " + e[1]);
                        spShadowMapWidth = spShadowMapHeight = Integer.parseInt(e[1]);
                        shadowMapWidth = shadowMapHeight = Math.round((float)spShadowMapWidth * configShadowResMul);
                        continue;
                    }
                    if (line.matches("/\\* SHADOWFOV:[0-9\\.]+ \\*/.*")) {
                        e = line.split("(:| )", 4);
                        SMCLog.info("Shadow map field of view: " + e[2]);
                        shadowMapFOV = Float.parseFloat(e[2]);
                        shadowMapIsOrtho = false;
                        continue;
                    }
                    if (line.matches("/\\* SHADOWHPL:[0-9\\.]+ \\*/.*")) {
                        e = line.split("(:| )", 4);
                        SMCLog.info("Shadow map half-plane: " + e[2]);
                        shadowMapHalfPlane = Float.parseFloat(e[2]);
                        shadowMapIsOrtho = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*float[ \t]*shadowDistance[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        e = line.split("(=[ \t]*|;)");
                        SMCLog.info("Shadow map distance: " + e[1]);
                        shadowMapHalfPlane = Float.parseFloat(e[1]);
                        shadowMapIsOrtho = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*float[ \t]*shadowIntervalSize[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        e = line.split("(=[ \t]*|;)");
                        SMCLog.info("Shadow map interval size: " + e[1]);
                        shadowIntervalSize = Float.parseFloat(e[1]);
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*generateShadowMipmap[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("Generate shadow mipmap");
                        Arrays.fill(shadowMipmapEnabled, true);
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*generateShadowColorMipmap[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("Generate shadow color mipmap");
                        Arrays.fill(shadowColorMipmapEnabled, true);
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*shadowHardwareFiltering[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("Hardware shadow filtering enabled.");
                        Arrays.fill(shadowHardwareFilteringEnabled, true);
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*shadowHardwareFiltering0[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowHardwareFiltering0");
                        Shaders.shadowHardwareFilteringEnabled[0] = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*shadowHardwareFiltering1[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowHardwareFiltering1");
                        Shaders.shadowHardwareFilteringEnabled[1] = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex0Mipmap|shadowtexMipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowtex0Mipmap");
                        Shaders.shadowMipmapEnabled[0] = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex1Mipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowtex1Mipmap");
                        Shaders.shadowMipmapEnabled[1] = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor0Mipmap|shadowColor0Mipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowcolor0Mipmap");
                        Shaders.shadowColorMipmapEnabled[0] = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor1Mipmap|shadowColor1Mipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowcolor1Mipmap");
                        Shaders.shadowColorMipmapEnabled[1] = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex0Nearest|shadowtexNearest|shadow0MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowtex0Nearest");
                        Shaders.shadowFilterNearest[0] = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex1Nearest|shadow1MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowtex1Nearest");
                        Shaders.shadowFilterNearest[1] = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor0Nearest|shadowColor0Nearest|shadowColor0MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowcolor0Nearest");
                        Shaders.shadowColorFilterNearest[0] = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor1Nearest|shadowColor1Nearest|shadowColor1MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowcolor1Nearest");
                        Shaders.shadowColorFilterNearest[1] = true;
                        continue;
                    }
                    if (line.matches("/\\* WETNESSHL:[0-9\\.]+ \\*/.*")) {
                        e = line.split("(:| )", 4);
                        SMCLog.info("Wetness halflife: " + e[2]);
                        wetnessHalfLife = Float.parseFloat(e[2]);
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*float[ \t]*wetnessHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        e = line.split("(=[ \t]*|;)");
                        SMCLog.info("Wetness halflife: " + e[1]);
                        wetnessHalfLife = Float.parseFloat(e[1]);
                        continue;
                    }
                    if (line.matches("/\\* DRYNESSHL:[0-9\\.]+ \\*/.*")) {
                        e = line.split("(:| )", 4);
                        SMCLog.info("Dryness halflife: " + e[2]);
                        drynessHalfLife = Float.parseFloat(e[2]);
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*float[ \t]*drynessHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        e = line.split("(=[ \t]*|;)");
                        SMCLog.info("Dryness halflife: " + e[1]);
                        drynessHalfLife = Float.parseFloat(e[1]);
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*float[ \t]*eyeBrightnessHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        e = line.split("(=[ \t]*|;)");
                        SMCLog.info("Eye brightness halflife: " + e[1]);
                        eyeBrightnessHalflife = Float.parseFloat(e[1]);
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*float[ \t]*centerDepthHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        e = line.split("(=[ \t]*|;)");
                        SMCLog.info("Center depth halflife: " + e[1]);
                        centerDepthSmoothHalflife = Float.parseFloat(e[1]);
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*float[ \t]*sunPathRotation[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        e = line.split("(=[ \t]*|;)");
                        SMCLog.info("Sun path rotation: " + e[1]);
                        sunPathRotation = Float.parseFloat(e[1]);
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*float[ \t]*ambientOcclusionLevel[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        e = line.split("(=[ \t]*|;)");
                        SMCLog.info("AO Level: " + e[1]);
                        aoLevel = Float.parseFloat(e[1]);
                        blockAoLight = 1.0f - aoLevel;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*int[ \t]*superSamplingLevel[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        e = line.split("(=[ \t]*|;)");
                        int name1 = Integer.parseInt(e[1]);
                        if (name1 > 1) {
                            SMCLog.info("Super sampling level: " + name1 + "x");
                            superSamplingLevel = name1;
                            continue;
                        }
                        superSamplingLevel = 1;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*int[ \t]*noiseTextureResolution[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        e = line.split("(=[ \t]*|;)");
                        SMCLog.info("Noise texture enabled");
                        SMCLog.info("Noise texture resolution: " + e[1]);
                        noiseTextureResolution = Integer.parseInt(e[1]);
                        noiseTextureEnabled = true;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*int[ \t]*\\w+Format[ \t]*=[ \t]*[RGBA81632FUI_SNORM]*[ \t]*;.*")) {
                        e1 = gbufferFormatPattern.matcher(line);
                        e1.matches();
                        String name = e1.group(1);
                        String bufferindex2 = e1.group(2);
                        int bufferindex1 = Shaders.getBufferIndexFromString(name);
                        int format = Shaders.getTextureFormatFromString(bufferindex2);
                        if (bufferindex1 < 0 || format == 0) continue;
                        Shaders.gbuffersFormat[bufferindex1] = format;
                        SMCLog.info("%s format: %s", name, bufferindex2);
                        continue;
                    }
                    if (line.matches("/\\* GAUX4FORMAT:RGBA32F \\*/.*")) {
                        SMCLog.info("gaux4 format : RGB32AF");
                        Shaders.gbuffersFormat[7] = 34836;
                        continue;
                    }
                    if (line.matches("/\\* GAUX4FORMAT:RGB32F \\*/.*")) {
                        SMCLog.info("gaux4 format : RGB32F");
                        Shaders.gbuffersFormat[7] = 34837;
                        continue;
                    }
                    if (line.matches("/\\* GAUX4FORMAT:RGB16 \\*/.*")) {
                        SMCLog.info("gaux4 format : RGB16");
                        Shaders.gbuffersFormat[7] = 32852;
                        continue;
                    }
                    if (line.matches("[ \t]*const[ \t]*bool[ \t]*\\w+MipmapEnabled[ \t]*=[ \t]*true[ \t]*;.*")) {
                        if (!filename.matches(".*composite[0-9]?.fsh") && !filename.matches(".*final.fsh")) continue;
                        e1 = gbufferMipmapEnabledPattern.matcher(line);
                        e1.matches();
                        String name = e1.group(1);
                        int bufferindex = Shaders.getBufferIndexFromString(name);
                        if (bufferindex < 0) continue;
                        newCompositeMipmapSetting |= 1 << bufferindex;
                        SMCLog.info("%s mipmap enabled for %s", name, filename);
                        continue;
                    }
                    if (!line.matches("/\\* DRAWBUFFERS:[0-7N]* \\*/.*")) continue;
                    e = line.split("(:| )", 4);
                    newDrawBufSetting = e[2];
                }
                reader.close();
            }
            catch (Exception var13) {
                SMCLog.severe("Couldn't read " + filename + "!");
                var13.printStackTrace();
                ARBShaderObjects.glDeleteObjectARB((int)fragShader);
                return 0;
            }
        }
        if (saveFinalShaders) {
            Shaders.saveShader(filename, fragCode.toString());
        }
        ARBShaderObjects.glShaderSourceARB((int)fragShader, (CharSequence)fragCode);
        ARBShaderObjects.glCompileShaderARB((int)fragShader);
        if (GL20.glGetShaderi((int)fragShader, (int)35713) != 1) {
            SMCLog.severe("Error compiling fragment shader: " + filename);
        }
        Shaders.printShaderLogInfo(fragShader, filename);
        return fragShader;
    }

    private static void saveShader(String filename, String code) {
        try {
            File e = new File(shaderpacksdir, "debug/" + filename);
            e.getParentFile().mkdirs();
            Config.writeFile(e, code);
        }
        catch (IOException var3) {
            Config.warn("Error saving: " + filename);
            var3.printStackTrace();
        }
    }

    private static void clearDirectory(File dir) {
        File[] files;
        if (dir.exists() && dir.isDirectory() && (files = dir.listFiles()) != null) {
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    Shaders.clearDirectory(file);
                }
                file.delete();
            }
        }
    }

    private static boolean printLogInfo(int obj, String name) {
        IntBuffer iVal = BufferUtils.createIntBuffer((int)1);
        ARBShaderObjects.glGetObjectParameterARB((int)obj, (int)35716, (IntBuffer)iVal);
        int length = iVal.get();
        if (length > 1) {
            ByteBuffer infoLog = BufferUtils.createByteBuffer((int)length);
            iVal.flip();
            ARBShaderObjects.glGetInfoLogARB((int)obj, (IntBuffer)iVal, (ByteBuffer)infoLog);
            byte[] infoBytes = new byte[length];
            infoLog.get(infoBytes);
            if (infoBytes[length - 1] == 0) {
                infoBytes[length - 1] = 10;
            }
            String out = new String(infoBytes);
            SMCLog.info("Info log: " + name + "\n" + out);
            return false;
        }
        return true;
    }

    private static boolean printShaderLogInfo(int shader, String name) {
        IntBuffer iVal = BufferUtils.createIntBuffer((int)1);
        int length = GL20.glGetShaderi((int)shader, (int)35716);
        if (length > 1) {
            String log = GL20.glGetShaderInfoLog((int)shader, (int)length);
            SMCLog.info("Shader info log: " + name + "\n" + log);
            return false;
        }
        return true;
    }

    public static void setDrawBuffers(IntBuffer drawBuffers) {
        if (drawBuffers == null) {
            drawBuffers = drawBuffersNone;
        }
        if (activeDrawBuffers != drawBuffers) {
            activeDrawBuffers = drawBuffers;
            GL20.glDrawBuffers((IntBuffer)drawBuffers);
        }
    }

    public static void useProgram(int program) {
        Shaders.checkGLError("pre-useProgram");
        if (isShadowPass) {
            program = 30;
            if (programsID[30] == 0) {
                normalMapEnabled = false;
                return;
            }
        }
        if (activeProgram != program) {
            activeProgram = program;
            ARBShaderObjects.glUseProgramObjectARB((int)programsID[program]);
            if (programsID[program] == 0) {
                normalMapEnabled = false;
            } else {
                Block block;
                int itemID;
                Item item;
                if (Shaders.checkGLError("useProgram ", programNames[program]) != 0) {
                    Shaders.programsID[program] = 0;
                }
                IntBuffer drawBuffers = programsDrawBuffers[program];
                if (isRenderingDfb) {
                    Shaders.setDrawBuffers(drawBuffers);
                    Shaders.checkGLError(programNames[program], " draw buffers = ", programsDrawBufSettings[program]);
                }
                activeCompositeMipmapSetting = programsCompositeMipmapSetting[program];
                uniformEntityColor.setProgram(programsID[activeProgram]);
                uniformEntityId.setProgram(programsID[activeProgram]);
                uniformBlockEntityId.setProgram(programsID[activeProgram]);
                switch (program) {
                    case 1: 
                    case 2: 
                    case 3: 
                    case 4: 
                    case 5: 
                    case 6: 
                    case 7: 
                    case 8: 
                    case 9: 
                    case 10: 
                    case 11: 
                    case 12: 
                    case 13: 
                    case 16: 
                    case 18: 
                    case 19: 
                    case 20: {
                        normalMapEnabled = true;
                        Shaders.setProgramUniform1i("texture", 0);
                        Shaders.setProgramUniform1i("lightmap", 1);
                        Shaders.setProgramUniform1i("normals", 2);
                        Shaders.setProgramUniform1i("specular", 3);
                        Shaders.setProgramUniform1i("shadow", waterShadowEnabled ? 5 : 4);
                        Shaders.setProgramUniform1i("watershadow", 4);
                        Shaders.setProgramUniform1i("shadowtex0", 4);
                        Shaders.setProgramUniform1i("shadowtex1", 5);
                        Shaders.setProgramUniform1i("depthtex0", 6);
                        Shaders.setProgramUniform1i("depthtex1", 12);
                        Shaders.setProgramUniform1i("shadowcolor", 13);
                        Shaders.setProgramUniform1i("shadowcolor0", 13);
                        Shaders.setProgramUniform1i("shadowcolor1", 14);
                        Shaders.setProgramUniform1i("noisetex", 15);
                        break;
                    }
                    default: {
                        normalMapEnabled = false;
                        break;
                    }
                    case 21: 
                    case 22: 
                    case 23: 
                    case 24: 
                    case 25: 
                    case 26: 
                    case 27: 
                    case 28: 
                    case 29: {
                        normalMapEnabled = false;
                        Shaders.setProgramUniform1i("gcolor", 0);
                        Shaders.setProgramUniform1i("gdepth", 1);
                        Shaders.setProgramUniform1i("gnormal", 2);
                        Shaders.setProgramUniform1i("composite", 3);
                        Shaders.setProgramUniform1i("gaux1", 7);
                        Shaders.setProgramUniform1i("gaux2", 8);
                        Shaders.setProgramUniform1i("gaux3", 9);
                        Shaders.setProgramUniform1i("gaux4", 10);
                        Shaders.setProgramUniform1i("colortex0", 0);
                        Shaders.setProgramUniform1i("colortex1", 1);
                        Shaders.setProgramUniform1i("colortex2", 2);
                        Shaders.setProgramUniform1i("colortex3", 3);
                        Shaders.setProgramUniform1i("colortex4", 7);
                        Shaders.setProgramUniform1i("colortex5", 8);
                        Shaders.setProgramUniform1i("colortex6", 9);
                        Shaders.setProgramUniform1i("colortex7", 10);
                        Shaders.setProgramUniform1i("shadow", waterShadowEnabled ? 5 : 4);
                        Shaders.setProgramUniform1i("watershadow", 4);
                        Shaders.setProgramUniform1i("shadowtex0", 4);
                        Shaders.setProgramUniform1i("shadowtex1", 5);
                        Shaders.setProgramUniform1i("gdepthtex", 6);
                        Shaders.setProgramUniform1i("depthtex0", 6);
                        Shaders.setProgramUniform1i("depthtex1", 11);
                        Shaders.setProgramUniform1i("depthtex2", 12);
                        Shaders.setProgramUniform1i("shadowcolor", 13);
                        Shaders.setProgramUniform1i("shadowcolor0", 13);
                        Shaders.setProgramUniform1i("shadowcolor1", 14);
                        Shaders.setProgramUniform1i("noisetex", 15);
                        break;
                    }
                    case 30: 
                    case 31: 
                    case 32: {
                        Shaders.setProgramUniform1i("tex", 0);
                        Shaders.setProgramUniform1i("texture", 0);
                        Shaders.setProgramUniform1i("lightmap", 1);
                        Shaders.setProgramUniform1i("normals", 2);
                        Shaders.setProgramUniform1i("specular", 3);
                        Shaders.setProgramUniform1i("shadow", waterShadowEnabled ? 5 : 4);
                        Shaders.setProgramUniform1i("watershadow", 4);
                        Shaders.setProgramUniform1i("shadowtex0", 4);
                        Shaders.setProgramUniform1i("shadowtex1", 5);
                        Shaders.setProgramUniform1i("shadowcolor", 13);
                        Shaders.setProgramUniform1i("shadowcolor0", 13);
                        Shaders.setProgramUniform1i("shadowcolor1", 14);
                        Shaders.setProgramUniform1i("noisetex", 15);
                    }
                }
                ItemStack stack = Shaders.mc.thePlayer.getCurrentEquippedItem();
                Item item2 = item = stack != null ? stack.getItem() : null;
                if (item != null) {
                    itemID = Item.itemRegistry.getIDForObject(item);
                    block = (Block)Block.blockRegistry.getObjectById(itemID);
                } else {
                    itemID = -1;
                    block = null;
                }
                int blockLight = block != null ? block.getLightValue() : 0;
                Shaders.setProgramUniform1i("heldItemId", itemID);
                Shaders.setProgramUniform1i("heldBlockLightValue", blockLight);
                Shaders.setProgramUniform1i("fogMode", fogEnabled ? fogMode : 0);
                Shaders.setProgramUniform3f("fogColor", fogColorR, fogColorG, fogColorB);
                Shaders.setProgramUniform3f("skyColor", skyColorR, skyColorG, skyColorB);
                Shaders.setProgramUniform1i("worldTime", (int)worldTime % 24000);
                Shaders.setProgramUniform1i("moonPhase", moonPhase);
                Shaders.setProgramUniform1f("frameTimeCounter", frameTimeCounter);
                Shaders.setProgramUniform1f("sunAngle", sunAngle);
                Shaders.setProgramUniform1f("shadowAngle", shadowAngle);
                Shaders.setProgramUniform1f("rainStrength", rainStrength);
                Shaders.setProgramUniform1f("aspectRatio", (float)renderWidth / (float)renderHeight);
                Shaders.setProgramUniform1f("viewWidth", renderWidth);
                Shaders.setProgramUniform1f("viewHeight", renderHeight);
                Shaders.setProgramUniform1f("near", 0.05f);
                Shaders.setProgramUniform1f("far", Shaders.mc.gameSettings.renderDistanceChunks * 16);
                Shaders.setProgramUniform3f("sunPosition", sunPosition[0], sunPosition[1], sunPosition[2]);
                Shaders.setProgramUniform3f("moonPosition", moonPosition[0], moonPosition[1], moonPosition[2]);
                Shaders.setProgramUniform3f("shadowLightPosition", shadowLightPosition[0], shadowLightPosition[1], shadowLightPosition[2]);
                Shaders.setProgramUniform3f("upPosition", upPosition[0], upPosition[1], upPosition[2]);
                Shaders.setProgramUniform3f("previousCameraPosition", (float)previousCameraPositionX, (float)previousCameraPositionY, (float)previousCameraPositionZ);
                Shaders.setProgramUniform3f("cameraPosition", (float)cameraPositionX, (float)cameraPositionY, (float)cameraPositionZ);
                Shaders.setProgramUniformMatrix4ARB("gbufferModelView", false, modelView);
                Shaders.setProgramUniformMatrix4ARB("gbufferModelViewInverse", false, modelViewInverse);
                Shaders.setProgramUniformMatrix4ARB("gbufferPreviousProjection", false, previousProjection);
                Shaders.setProgramUniformMatrix4ARB("gbufferProjection", false, projection);
                Shaders.setProgramUniformMatrix4ARB("gbufferProjectionInverse", false, projectionInverse);
                Shaders.setProgramUniformMatrix4ARB("gbufferPreviousModelView", false, previousModelView);
                if (usedShadowDepthBuffers > 0) {
                    Shaders.setProgramUniformMatrix4ARB("shadowProjection", false, shadowProjection);
                    Shaders.setProgramUniformMatrix4ARB("shadowProjectionInverse", false, shadowProjectionInverse);
                    Shaders.setProgramUniformMatrix4ARB("shadowModelView", false, shadowModelView);
                    Shaders.setProgramUniformMatrix4ARB("shadowModelViewInverse", false, shadowModelViewInverse);
                }
                Shaders.setProgramUniform1f("wetness", wetness);
                Shaders.setProgramUniform1f("eyeAltitude", eyePosY);
                Shaders.setProgramUniform2i("eyeBrightness", eyeBrightness & 0xFFFF, eyeBrightness >> 16);
                Shaders.setProgramUniform2i("eyeBrightnessSmooth", Math.round(eyeBrightnessFadeX), Math.round(eyeBrightnessFadeY));
                Shaders.setProgramUniform2i("terrainTextureSize", terrainTextureSize[0], terrainTextureSize[1]);
                Shaders.setProgramUniform1i("terrainIconSize", terrainIconSize);
                Shaders.setProgramUniform1i("isEyeInWater", isEyeInWater);
                Shaders.setProgramUniform1i("hideGUI", Shaders.mc.gameSettings.hideGUI ? 1 : 0);
                Shaders.setProgramUniform1f("centerDepthSmooth", centerDepthSmooth);
                Shaders.setProgramUniform2i("atlasSize", atlasSizeX, atlasSizeY);
                Shaders.checkGLError("useProgram ", programNames[program]);
            }
        }
    }

    public static void setProgramUniform1i(String name, int x) {
        int gp = programsID[activeProgram];
        if (gp != 0) {
            int uniform = ARBShaderObjects.glGetUniformLocationARB((int)gp, (CharSequence)name);
            ARBShaderObjects.glUniform1iARB((int)uniform, (int)x);
            Shaders.checkGLError(programNames[activeProgram], name);
        }
    }

    public static void setProgramUniform2i(String name, int x, int y) {
        int gp = programsID[activeProgram];
        if (gp != 0) {
            int uniform = ARBShaderObjects.glGetUniformLocationARB((int)gp, (CharSequence)name);
            ARBShaderObjects.glUniform2iARB((int)uniform, (int)x, (int)y);
            Shaders.checkGLError(programNames[activeProgram], name);
        }
    }

    public static void setProgramUniform1f(String name, float x) {
        int gp = programsID[activeProgram];
        if (gp != 0) {
            int uniform = ARBShaderObjects.glGetUniformLocationARB((int)gp, (CharSequence)name);
            ARBShaderObjects.glUniform1fARB((int)uniform, (float)x);
            Shaders.checkGLError(programNames[activeProgram], name);
        }
    }

    public static void setProgramUniform3f(String name, float x, float y, float z) {
        int gp = programsID[activeProgram];
        if (gp != 0) {
            int uniform = ARBShaderObjects.glGetUniformLocationARB((int)gp, (CharSequence)name);
            ARBShaderObjects.glUniform3fARB((int)uniform, (float)x, (float)y, (float)z);
            Shaders.checkGLError(programNames[activeProgram], name);
        }
    }

    public static void setProgramUniformMatrix4ARB(String name, boolean transpose, FloatBuffer matrix) {
        int gp = programsID[activeProgram];
        if (gp != 0 && matrix != null) {
            int uniform = ARBShaderObjects.glGetUniformLocationARB((int)gp, (CharSequence)name);
            ARBShaderObjects.glUniformMatrix4ARB((int)uniform, (boolean)transpose, (FloatBuffer)matrix);
            Shaders.checkGLError(programNames[activeProgram], name);
        }
    }

    private static int getBufferIndexFromString(String name) {
        return !name.equals("colortex0") && !name.equals("gcolor") ? (!name.equals("colortex1") && !name.equals("gdepth") ? (!name.equals("colortex2") && !name.equals("gnormal") ? (!name.equals("colortex3") && !name.equals("composite") ? (!name.equals("colortex4") && !name.equals("gaux1") ? (!name.equals("colortex5") && !name.equals("gaux2") ? (!name.equals("colortex6") && !name.equals("gaux3") ? (!name.equals("colortex7") && !name.equals("gaux4") ? -1 : 7) : 6) : 5) : 4) : 3) : 2) : 1) : 0;
    }

    private static int getTextureFormatFromString(String par) {
        par = par.trim();
        for (int i = 0; i < formatNames.length; ++i) {
            String name = formatNames[i];
            if (!par.equals(name)) continue;
            return formatIds[i];
        }
        return 0;
    }

    private static void setupNoiseTexture() {
        if (noiseTexture == null) {
            noiseTexture = new HFNoiseTexture(noiseTextureResolution, noiseTextureResolution);
        }
    }

    private static void loadEntityDataMap() {
        mapBlockToEntityData = new IdentityHashMap<Block, Integer>(300);
        if (mapBlockToEntityData.isEmpty()) {
            for (ResourceLocation e : Block.blockRegistry.getKeys()) {
                Block m = (Block)Block.blockRegistry.getObject(e);
                int name = Block.blockRegistry.getIDForObject(m);
                mapBlockToEntityData.put(m, name);
            }
        }
        BufferedReader reader1 = null;
        try {
            reader1 = new BufferedReader(new InputStreamReader(shaderPack.getResourceAsStream("/mc_Entity_x.txt")));
        }
        catch (Exception e) {
            // empty catch block
        }
        if (reader1 != null) {
            try {
                String e1;
                while ((e1 = reader1.readLine()) != null) {
                    Matcher m1 = patternLoadEntityDataMap.matcher(e1);
                    if (m1.matches()) {
                        String name1 = m1.group(1);
                        String value = m1.group(2);
                        int id = Integer.parseInt(value);
                        Block block = Block.getBlockFromName(name1);
                        if (block != null) {
                            mapBlockToEntityData.put(block, id);
                            continue;
                        }
                        SMCLog.warning("Unknown block name %s", name1);
                        continue;
                    }
                    SMCLog.warning("unmatched %s\n", e1);
                }
            }
            catch (Exception var9) {
                SMCLog.warning("Error parsing mc_Entity_x.txt");
            }
        }
        if (reader1 != null) {
            try {
                reader1.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private static IntBuffer fillIntBufferZero(IntBuffer buf) {
        int limit = buf.limit();
        for (int i = buf.position(); i < limit; ++i) {
            buf.put(i, 0);
        }
        return buf;
    }

    public static void uninit() {
        if (isShaderPackInitialized) {
            Shaders.checkGLError("Shaders.uninit pre");
            for (int i = 0; i < 33; ++i) {
                if (programsRef[i] != 0) {
                    ARBShaderObjects.glDeleteObjectARB((int)programsRef[i]);
                    Shaders.checkGLError("del programRef");
                }
                Shaders.programsRef[i] = 0;
                Shaders.programsID[i] = 0;
                Shaders.programsDrawBufSettings[i] = null;
                Shaders.programsDrawBuffers[i] = null;
                Shaders.programsCompositeMipmapSetting[i] = 0;
            }
            if (dfb != 0) {
                EXTFramebufferObject.glDeleteFramebuffersEXT((int)dfb);
                dfb = 0;
                Shaders.checkGLError("del dfb");
            }
            if (sfb != 0) {
                EXTFramebufferObject.glDeleteFramebuffersEXT((int)sfb);
                sfb = 0;
                Shaders.checkGLError("del sfb");
            }
            if (dfbDepthTextures != null) {
                GlStateManager.deleteTextures(dfbDepthTextures);
                Shaders.fillIntBufferZero(dfbDepthTextures);
                Shaders.checkGLError("del dfbDepthTextures");
            }
            if (dfbColorTextures != null) {
                GlStateManager.deleteTextures(dfbColorTextures);
                Shaders.fillIntBufferZero(dfbColorTextures);
                Shaders.checkGLError("del dfbTextures");
            }
            if (sfbDepthTextures != null) {
                GlStateManager.deleteTextures(sfbDepthTextures);
                Shaders.fillIntBufferZero(sfbDepthTextures);
                Shaders.checkGLError("del shadow depth");
            }
            if (sfbColorTextures != null) {
                GlStateManager.deleteTextures(sfbColorTextures);
                Shaders.fillIntBufferZero(sfbColorTextures);
                Shaders.checkGLError("del shadow color");
            }
            if (dfbDrawBuffers != null) {
                Shaders.fillIntBufferZero(dfbDrawBuffers);
            }
            if (noiseTexture != null) {
                noiseTexture.destroy();
                noiseTexture = null;
            }
            SMCLog.info("Uninit");
            shadowPassInterval = 0;
            shouldSkipDefaultShadow = false;
            isShaderPackInitialized = false;
            Shaders.checkGLError("Shaders.uninit");
        }
    }

    public static void scheduleResize() {
        renderDisplayHeight = 0;
    }

    public static void scheduleResizeShadow() {
        needResizeShadow = true;
    }

    private static void resize() {
        renderDisplayWidth = Shaders.mc.displayWidth;
        renderDisplayHeight = Shaders.mc.displayHeight;
        renderWidth = Math.round((float)renderDisplayWidth * configRenderResMul);
        renderHeight = Math.round((float)renderDisplayHeight * configRenderResMul);
        Shaders.setupFrameBuffer();
    }

    private static void resizeShadow() {
        needResizeShadow = false;
        shadowMapWidth = Math.round((float)spShadowMapWidth * configShadowResMul);
        shadowMapHeight = Math.round((float)spShadowMapHeight * configShadowResMul);
        Shaders.setupShadowFrameBuffer();
    }

    private static void setupFrameBuffer() {
        int status;
        if (dfb != 0) {
            EXTFramebufferObject.glDeleteFramebuffersEXT((int)dfb);
            GlStateManager.deleteTextures(dfbDepthTextures);
            GlStateManager.deleteTextures(dfbColorTextures);
        }
        dfb = EXTFramebufferObject.glGenFramebuffersEXT();
        GL11.glGenTextures((IntBuffer)((IntBuffer)dfbDepthTextures.clear().limit(usedDepthBuffers)));
        GL11.glGenTextures((IntBuffer)((IntBuffer)dfbColorTextures.clear().limit(16)));
        dfbDepthTextures.position(0);
        dfbColorTextures.position(0);
        dfbColorTextures.get(dfbColorTexturesA).position(0);
        EXTFramebufferObject.glBindFramebufferEXT((int)36160, (int)dfb);
        GL20.glDrawBuffers((int)0);
        GL11.glReadBuffer((int)0);
        for (status = 0; status < usedDepthBuffers; ++status) {
            GlStateManager.func_179144_i(dfbDepthTextures.get(status));
            GL11.glTexParameteri((int)3553, (int)10242, (int)10496);
            GL11.glTexParameteri((int)3553, (int)10243, (int)10496);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
            GL11.glTexParameteri((int)3553, (int)34891, (int)6409);
            GL11.glTexImage2D((int)3553, (int)0, (int)6402, (int)renderWidth, (int)renderHeight, (int)0, (int)6402, (int)5126, null);
        }
        EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)36096, (int)3553, (int)dfbDepthTextures.get(0), (int)0);
        GL20.glDrawBuffers((IntBuffer)dfbDrawBuffers);
        GL11.glReadBuffer((int)0);
        Shaders.checkGLError("FT d");
        for (status = 0; status < usedColorBuffers; ++status) {
            GlStateManager.func_179144_i(dfbColorTexturesA[status]);
            GL11.glTexParameteri((int)3553, (int)10242, (int)10496);
            GL11.glTexParameteri((int)3553, (int)10243, (int)10496);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
            GL11.glTexImage2D((int)3553, (int)0, (int)gbuffersFormat[status], (int)renderWidth, (int)renderHeight, (int)0, (int)32993, (int)33639, null);
            EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)(36064 + status), (int)3553, (int)dfbColorTexturesA[status], (int)0);
            Shaders.checkGLError("FT c");
        }
        for (status = 0; status < usedColorBuffers; ++status) {
            GlStateManager.func_179144_i(dfbColorTexturesA[8 + status]);
            GL11.glTexParameteri((int)3553, (int)10242, (int)10496);
            GL11.glTexParameteri((int)3553, (int)10243, (int)10496);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
            GL11.glTexImage2D((int)3553, (int)0, (int)gbuffersFormat[status], (int)renderWidth, (int)renderHeight, (int)0, (int)32993, (int)33639, null);
            Shaders.checkGLError("FT ca");
        }
        status = EXTFramebufferObject.glCheckFramebufferStatusEXT((int)36160);
        if (status == 36058) {
            Shaders.printChatAndLogError("[Shaders] Error: Failed framebuffer incomplete formats");
            for (int i = 0; i < usedColorBuffers; ++i) {
                GlStateManager.func_179144_i(dfbColorTextures.get(i));
                GL11.glTexImage2D((int)3553, (int)0, (int)6408, (int)renderWidth, (int)renderHeight, (int)0, (int)32993, (int)33639, null);
                EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)(36064 + i), (int)3553, (int)dfbColorTextures.get(i), (int)0);
                Shaders.checkGLError("FT c");
            }
            status = EXTFramebufferObject.glCheckFramebufferStatusEXT((int)36160);
            if (status == 36053) {
                SMCLog.info("complete");
            }
        }
        GlStateManager.func_179144_i(0);
        if (status != 36053) {
            Shaders.printChatAndLogError("[Shaders] Error: Failed creating framebuffer! (Status " + status + ")");
        } else {
            SMCLog.info("Framebuffer created.");
        }
    }

    private static void setupShadowFrameBuffer() {
        if (usedShadowDepthBuffers != 0) {
            int filter;
            int status;
            if (sfb != 0) {
                EXTFramebufferObject.glDeleteFramebuffersEXT((int)sfb);
                GlStateManager.deleteTextures(sfbDepthTextures);
                GlStateManager.deleteTextures(sfbColorTextures);
            }
            sfb = EXTFramebufferObject.glGenFramebuffersEXT();
            EXTFramebufferObject.glBindFramebufferEXT((int)36160, (int)sfb);
            GL11.glDrawBuffer((int)0);
            GL11.glReadBuffer((int)0);
            GL11.glGenTextures((IntBuffer)((IntBuffer)sfbDepthTextures.clear().limit(usedShadowDepthBuffers)));
            GL11.glGenTextures((IntBuffer)((IntBuffer)sfbColorTextures.clear().limit(usedShadowColorBuffers)));
            sfbDepthTextures.position(0);
            sfbColorTextures.position(0);
            for (status = 0; status < usedShadowDepthBuffers; ++status) {
                GlStateManager.func_179144_i(sfbDepthTextures.get(status));
                GL11.glTexParameterf((int)3553, (int)10242, (float)10496.0f);
                GL11.glTexParameterf((int)3553, (int)10243, (float)10496.0f);
                filter = shadowFilterNearest[status] ? 9728 : 9729;
                GL11.glTexParameteri((int)3553, (int)10241, (int)filter);
                GL11.glTexParameteri((int)3553, (int)10240, (int)filter);
                if (shadowHardwareFilteringEnabled[status]) {
                    GL11.glTexParameteri((int)3553, (int)34892, (int)34894);
                }
                GL11.glTexImage2D((int)3553, (int)0, (int)6402, (int)shadowMapWidth, (int)shadowMapHeight, (int)0, (int)6402, (int)5126, null);
            }
            EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)36096, (int)3553, (int)sfbDepthTextures.get(0), (int)0);
            Shaders.checkGLError("FT sd");
            for (status = 0; status < usedShadowColorBuffers; ++status) {
                GlStateManager.func_179144_i(sfbColorTextures.get(status));
                GL11.glTexParameterf((int)3553, (int)10242, (float)10496.0f);
                GL11.glTexParameterf((int)3553, (int)10243, (float)10496.0f);
                filter = shadowColorFilterNearest[status] ? 9728 : 9729;
                GL11.glTexParameteri((int)3553, (int)10241, (int)filter);
                GL11.glTexParameteri((int)3553, (int)10240, (int)filter);
                GL11.glTexImage2D((int)3553, (int)0, (int)6408, (int)shadowMapWidth, (int)shadowMapHeight, (int)0, (int)32993, (int)33639, null);
                EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)(36064 + status), (int)3553, (int)sfbColorTextures.get(status), (int)0);
                Shaders.checkGLError("FT sc");
            }
            GlStateManager.func_179144_i(0);
            if (usedShadowColorBuffers > 0) {
                GL20.glDrawBuffers((IntBuffer)sfbDrawBuffers);
            }
            if ((status = EXTFramebufferObject.glCheckFramebufferStatusEXT((int)36160)) != 36053) {
                Shaders.printChatAndLogError("[Shaders] Error: Failed creating shadow framebuffer! (Status " + status + ")");
            } else {
                SMCLog.info("Shadow framebuffer created.");
            }
        }
    }

    public static void beginRender(Minecraft minecraft, float partialTicks, long finishTimeNano) {
        int var10;
        block16: {
            Shaders.checkGLError("pre beginRender");
            Shaders.checkWorldChanged(Shaders.mc.theWorld);
            mc = minecraft;
            Shaders.mc.mcProfiler.startSection("init");
            entityRenderer = Shaders.mc.entityRenderer;
            if (!isShaderPackInitialized) {
                try {
                    Shaders.init();
                }
                catch (IllegalStateException var7) {
                    if (!Config.normalize(var7.getMessage()).equals("Function is not supported")) break block16;
                    Shaders.printChatAndLogError("[Shaders] Error: " + var7.getMessage());
                    var7.printStackTrace();
                    Shaders.setShaderPack(packNameNone);
                    return;
                }
            }
        }
        if (Shaders.mc.displayWidth != renderDisplayWidth || Shaders.mc.displayHeight != renderDisplayHeight) {
            Shaders.resize();
        }
        if (needResizeShadow) {
            Shaders.resizeShadow();
        }
        if ((diffWorldTime = ((worldTime = Shaders.mc.theWorld.getWorldTime()) - lastWorldTime) % 24000L) < 0L) {
            diffWorldTime += 24000L;
        }
        lastWorldTime = worldTime;
        moonPhase = Shaders.mc.theWorld.getMoonPhase();
        systemTime = System.currentTimeMillis();
        if (lastSystemTime == 0L) {
            lastSystemTime = systemTime;
        }
        diffSystemTime = systemTime - lastSystemTime;
        lastSystemTime = systemTime;
        frameTimeCounter += (float)diffSystemTime * 0.001f;
        frameTimeCounter %= 3600.0f;
        rainStrength = minecraft.theWorld.getRainStrength(partialTicks);
        float renderViewEntity = (float)diffSystemTime * 0.01f;
        float i = (float)Math.exp(Math.log(0.5) * (double)renderViewEntity / (double)(wetness < rainStrength ? drynessHalfLife : wetnessHalfLife));
        wetness = wetness * i + rainStrength * (1.0f - i);
        Entity var8 = mc.func_175606_aa();
        isSleeping = var8 instanceof EntityLivingBase && ((EntityLivingBase)var8).isPlayerSleeping();
        eyePosY = (float)var8.posY * partialTicks + (float)var8.lastTickPosY * (1.0f - partialTicks);
        eyeBrightness = var8.getBrightnessForRender(partialTicks);
        i = (float)diffSystemTime * 0.01f;
        float temp2 = (float)Math.exp(Math.log(0.5) * (double)i / (double)eyeBrightnessHalflife);
        eyeBrightnessFadeX = eyeBrightnessFadeX * temp2 + (float)(eyeBrightness & 0xFFFF) * (1.0f - temp2);
        eyeBrightnessFadeY = eyeBrightnessFadeY * temp2 + (float)(eyeBrightness >> 16) * (1.0f - temp2);
        isEyeInWater = Shaders.mc.gameSettings.thirdPersonView == 0 && !isSleeping && Shaders.mc.thePlayer.isInsideOfMaterial(Material.water) ? 1 : 0;
        Vec3 var9 = Shaders.mc.theWorld.getSkyColor(mc.func_175606_aa(), partialTicks);
        skyColorR = (float)var9.xCoord;
        skyColorG = (float)var9.yCoord;
        skyColorB = (float)var9.zCoord;
        isRenderingWorld = true;
        isCompositeRendered = false;
        isHandRendered = false;
        if (usedShadowDepthBuffers >= 1) {
            GlStateManager.setActiveTexture(33988);
            GlStateManager.func_179144_i(sfbDepthTextures.get(0));
            if (usedShadowDepthBuffers >= 2) {
                GlStateManager.setActiveTexture(33989);
                GlStateManager.func_179144_i(sfbDepthTextures.get(1));
            }
        }
        GlStateManager.setActiveTexture(33984);
        for (var10 = 0; var10 < usedColorBuffers; ++var10) {
            GlStateManager.func_179144_i(dfbColorTexturesA[var10]);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
            GlStateManager.func_179144_i(dfbColorTexturesA[8 + var10]);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        }
        GlStateManager.func_179144_i(0);
        for (var10 = 0; var10 < 4 && 4 + var10 < usedColorBuffers; ++var10) {
            GlStateManager.setActiveTexture(33991 + var10);
            GlStateManager.func_179144_i(dfbColorTextures.get(4 + var10));
        }
        GlStateManager.setActiveTexture(33990);
        GlStateManager.func_179144_i(dfbDepthTextures.get(0));
        if (usedDepthBuffers >= 2) {
            GlStateManager.setActiveTexture(33995);
            GlStateManager.func_179144_i(dfbDepthTextures.get(1));
            if (usedDepthBuffers >= 3) {
                GlStateManager.setActiveTexture(33996);
                GlStateManager.func_179144_i(dfbDepthTextures.get(2));
            }
        }
        for (var10 = 0; var10 < usedShadowColorBuffers; ++var10) {
            GlStateManager.setActiveTexture(33997 + var10);
            GlStateManager.func_179144_i(sfbColorTextures.get(var10));
        }
        if (noiseTextureEnabled) {
            GlStateManager.setActiveTexture(33984 + Shaders.noiseTexture.textureUnit);
            GlStateManager.func_179144_i(noiseTexture.getID());
            GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
            GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        }
        GlStateManager.setActiveTexture(33984);
        previousCameraPositionX = cameraPositionX;
        previousCameraPositionY = cameraPositionY;
        previousCameraPositionZ = cameraPositionZ;
        previousProjection.position(0);
        projection.position(0);
        previousProjection.put(projection);
        previousProjection.position(0);
        projection.position(0);
        previousModelView.position(0);
        modelView.position(0);
        previousModelView.put(modelView);
        previousModelView.position(0);
        modelView.position(0);
        Shaders.checkGLError("beginRender");
        ShadersRender.renderShadowMap(entityRenderer, 0, partialTicks, finishTimeNano);
        Shaders.mc.mcProfiler.endSection();
        EXTFramebufferObject.glBindFramebufferEXT((int)36160, (int)dfb);
        for (var10 = 0; var10 < usedColorBuffers; ++var10) {
            Shaders.colorTexturesToggle[var10] = 0;
            EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)(36064 + var10), (int)3553, (int)dfbColorTexturesA[var10], (int)0);
        }
        Shaders.checkGLError("end beginRender");
    }

    private static void checkWorldChanged(World world) {
        if (currentWorld != world) {
            World oldWorld = currentWorld;
            currentWorld = world;
            if (oldWorld != null && world != null) {
                int dimIdOld = oldWorld.provider.getDimensionId();
                int dimIdNew = world.provider.getDimensionId();
                boolean dimShadersOld = shaderPackDimensions.contains(dimIdOld);
                boolean dimShadersNew = shaderPackDimensions.contains(dimIdNew);
                if (dimShadersOld || dimShadersNew) {
                    Shaders.uninit();
                }
            }
        }
    }

    public static void beginRenderPass(int pass, float partialTicks, long finishTimeNano) {
        if (!isShadowPass) {
            EXTFramebufferObject.glBindFramebufferEXT((int)36160, (int)dfb);
            GL11.glViewport((int)0, (int)0, (int)renderWidth, (int)renderHeight);
            activeDrawBuffers = null;
            ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
            Shaders.useProgram(2);
            Shaders.checkGLError("end beginRenderPass");
        }
    }

    public static void setViewport(int vx, int vy, int vw, int vh) {
        GlStateManager.colorMask(true, true, true, true);
        if (isShadowPass) {
            GL11.glViewport((int)0, (int)0, (int)shadowMapWidth, (int)shadowMapHeight);
        } else {
            GL11.glViewport((int)0, (int)0, (int)renderWidth, (int)renderHeight);
            EXTFramebufferObject.glBindFramebufferEXT((int)36160, (int)dfb);
            isRenderingDfb = true;
            GlStateManager.enableCull();
            GlStateManager.enableDepth();
            Shaders.setDrawBuffers(drawBuffersNone);
            Shaders.useProgram(2);
            Shaders.checkGLError("beginRenderPass");
        }
    }

    public static int setFogMode(int val) {
        fogMode = val;
        return val;
    }

    public static void setFogColor(float r, float g, float b) {
        fogColorR = r;
        fogColorG = g;
        fogColorB = b;
    }

    public static void setClearColor(float red, float green, float blue, float alpha) {
        GlStateManager.clearColor(red, green, blue, alpha);
        clearColorR = red;
        clearColorG = green;
        clearColorB = blue;
    }

    public static void clearRenderBuffer() {
        if (isShadowPass) {
            Shaders.checkGLError("shadow clear pre");
            EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)36096, (int)3553, (int)sfbDepthTextures.get(0), (int)0);
            GL11.glClearColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL20.glDrawBuffers((IntBuffer)programsDrawBuffers[30]);
            Shaders.checkFramebufferStatus("shadow clear");
            GL11.glClear((int)16640);
            Shaders.checkGLError("shadow clear");
        } else {
            Shaders.checkGLError("clear pre");
            GL20.glDrawBuffers((int)36064);
            GL11.glClear((int)16384);
            GL20.glDrawBuffers((int)36065);
            GL11.glClearColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glClear((int)16384);
            for (int i = 2; i < usedColorBuffers; ++i) {
                GL20.glDrawBuffers((int)(36064 + i));
                GL11.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
                GL11.glClear((int)16384);
            }
            Shaders.setDrawBuffers(dfbDrawBuffers);
            Shaders.checkFramebufferStatus("clear");
            Shaders.checkGLError("clear");
        }
    }

    public static void setCamera(float partialTicks) {
        Entity viewEntity = mc.func_175606_aa();
        double x = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double)partialTicks;
        double y = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double)partialTicks;
        double z = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double)partialTicks;
        cameraPositionX = x;
        cameraPositionY = y;
        cameraPositionZ = z;
        GL11.glGetFloat((int)2983, (FloatBuffer)((FloatBuffer)projection.position(0)));
        SMath.invertMat4FBFA((FloatBuffer)projectionInverse.position(0), (FloatBuffer)projection.position(0), faProjectionInverse, faProjection);
        projection.position(0);
        projectionInverse.position(0);
        GL11.glGetFloat((int)2982, (FloatBuffer)((FloatBuffer)modelView.position(0)));
        SMath.invertMat4FBFA((FloatBuffer)modelViewInverse.position(0), (FloatBuffer)modelView.position(0), faModelViewInverse, faModelView);
        modelView.position(0);
        modelViewInverse.position(0);
        Shaders.checkGLError("setCamera");
    }

    public static void setCameraShadow(float partialTicks) {
        float x1;
        float raSun;
        float angleInterval;
        Entity viewEntity = mc.func_175606_aa();
        double x = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double)partialTicks;
        double y = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double)partialTicks;
        double z = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double)partialTicks;
        cameraPositionX = x;
        cameraPositionY = y;
        cameraPositionZ = z;
        GL11.glGetFloat((int)2983, (FloatBuffer)((FloatBuffer)projection.position(0)));
        SMath.invertMat4FBFA((FloatBuffer)projectionInverse.position(0), (FloatBuffer)projection.position(0), faProjectionInverse, faProjection);
        projection.position(0);
        projectionInverse.position(0);
        GL11.glGetFloat((int)2982, (FloatBuffer)((FloatBuffer)modelView.position(0)));
        SMath.invertMat4FBFA((FloatBuffer)modelViewInverse.position(0), (FloatBuffer)modelView.position(0), faModelViewInverse, faModelView);
        modelView.position(0);
        modelViewInverse.position(0);
        GL11.glViewport((int)0, (int)0, (int)shadowMapWidth, (int)shadowMapHeight);
        GL11.glMatrixMode((int)5889);
        GL11.glLoadIdentity();
        if (shadowMapIsOrtho) {
            GL11.glOrtho((double)(-shadowMapHalfPlane), (double)shadowMapHalfPlane, (double)(-shadowMapHalfPlane), (double)shadowMapHalfPlane, (double)0.05f, (double)256.0);
        } else {
            GLU.gluPerspective((float)shadowMapFOV, (float)((float)shadowMapWidth / (float)shadowMapHeight), (float)0.05f, (float)256.0f);
        }
        GL11.glMatrixMode((int)5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)-100.0f);
        GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        celestialAngle = Shaders.mc.theWorld.getCelestialAngle(partialTicks);
        sunAngle = celestialAngle < 0.75f ? celestialAngle + 0.25f : celestialAngle - 0.75f;
        float angle = celestialAngle * -360.0f;
        float f = angleInterval = shadowAngleInterval > 0.0f ? angle % shadowAngleInterval - shadowAngleInterval * 0.5f : 0.0f;
        if ((double)sunAngle <= 0.5) {
            GL11.glRotatef((float)(angle - angleInterval), (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glRotatef((float)sunPathRotation, (float)1.0f, (float)0.0f, (float)0.0f);
            shadowAngle = sunAngle;
        } else {
            GL11.glRotatef((float)(angle + 180.0f - angleInterval), (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glRotatef((float)sunPathRotation, (float)1.0f, (float)0.0f, (float)0.0f);
            shadowAngle = sunAngle - 0.5f;
        }
        if (shadowMapIsOrtho) {
            raSun = shadowIntervalSize;
            x1 = raSun / 2.0f;
            GL11.glTranslatef((float)((float)x % raSun - x1), (float)((float)y % raSun - x1), (float)((float)z % raSun - x1));
        }
        raSun = sunAngle * ((float)Math.PI * 2);
        x1 = (float)Math.cos(raSun);
        float y1 = (float)Math.sin(raSun);
        float raTilt = sunPathRotation * ((float)Math.PI * 2);
        float x2 = x1;
        float y2 = y1 * (float)Math.cos(raTilt);
        float z2 = y1 * (float)Math.sin(raTilt);
        if ((double)sunAngle > 0.5) {
            x2 = -x1;
            y2 = -y2;
            z2 = -z2;
        }
        Shaders.shadowLightPositionVector[0] = x2;
        Shaders.shadowLightPositionVector[1] = y2;
        Shaders.shadowLightPositionVector[2] = z2;
        Shaders.shadowLightPositionVector[3] = 0.0f;
        GL11.glGetFloat((int)2983, (FloatBuffer)((FloatBuffer)shadowProjection.position(0)));
        SMath.invertMat4FBFA((FloatBuffer)shadowProjectionInverse.position(0), (FloatBuffer)shadowProjection.position(0), faShadowProjectionInverse, faShadowProjection);
        shadowProjection.position(0);
        shadowProjectionInverse.position(0);
        GL11.glGetFloat((int)2982, (FloatBuffer)((FloatBuffer)shadowModelView.position(0)));
        SMath.invertMat4FBFA((FloatBuffer)shadowModelViewInverse.position(0), (FloatBuffer)shadowModelView.position(0), faShadowModelViewInverse, faShadowModelView);
        shadowModelView.position(0);
        shadowModelViewInverse.position(0);
        Shaders.setProgramUniformMatrix4ARB("gbufferProjection", false, projection);
        Shaders.setProgramUniformMatrix4ARB("gbufferProjectionInverse", false, projectionInverse);
        Shaders.setProgramUniformMatrix4ARB("gbufferPreviousProjection", false, previousProjection);
        Shaders.setProgramUniformMatrix4ARB("gbufferModelView", false, modelView);
        Shaders.setProgramUniformMatrix4ARB("gbufferModelViewInverse", false, modelViewInverse);
        Shaders.setProgramUniformMatrix4ARB("gbufferPreviousModelView", false, previousModelView);
        Shaders.setProgramUniformMatrix4ARB("shadowProjection", false, shadowProjection);
        Shaders.setProgramUniformMatrix4ARB("shadowProjectionInverse", false, shadowProjectionInverse);
        Shaders.setProgramUniformMatrix4ARB("shadowModelView", false, shadowModelView);
        Shaders.setProgramUniformMatrix4ARB("shadowModelViewInverse", false, shadowModelViewInverse);
        Shaders.mc.gameSettings.thirdPersonView = 1;
        Shaders.checkGLError("setCamera");
    }

    public static void preCelestialRotate() {
        Shaders.setUpPosition();
        GL11.glRotatef((float)(sunPathRotation * 1.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        Shaders.checkGLError("preCelestialRotate");
    }

    public static void postCelestialRotate() {
        FloatBuffer modelView = tempMatrixDirectBuffer;
        modelView.clear();
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        modelView.get(tempMat, 0, 16);
        SMath.multiplyMat4xVec4(sunPosition, tempMat, sunPosModelView);
        SMath.multiplyMat4xVec4(moonPosition, tempMat, moonPosModelView);
        System.arraycopy(shadowAngle == sunAngle ? sunPosition : moonPosition, 0, shadowLightPosition, 0, 3);
        Shaders.checkGLError("postCelestialRotate");
    }

    public static void setUpPosition() {
        FloatBuffer modelView = tempMatrixDirectBuffer;
        modelView.clear();
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        modelView.get(tempMat, 0, 16);
        SMath.multiplyMat4xVec4(upPosition, tempMat, upPosModelView);
    }

    public static void genCompositeMipmap() {
        if (hasGlGenMipmap) {
            for (int i = 0; i < usedColorBuffers; ++i) {
                if ((activeCompositeMipmapSetting & 1 << i) == 0) continue;
                GlStateManager.setActiveTexture(33984 + colorTextureTextureImageUnit[i]);
                GL11.glTexParameteri((int)3553, (int)10241, (int)9987);
                GL30.glGenerateMipmap((int)3553);
            }
            GlStateManager.setActiveTexture(33984);
        }
    }

    public static void drawComposite() {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex3f((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex3f((float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex3f((float)1.0f, (float)1.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glEnd();
    }

    public static void renderCompositeFinal() {
        if (!isShadowPass) {
            int maskR;
            int enableAltBuffers;
            Shaders.checkGLError("pre-renderCompositeFinal");
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glMatrixMode((int)5889);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glOrtho((double)0.0, (double)1.0, (double)0.0, (double)1.0, (double)0.0, (double)1.0);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.func_179098_w();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(519);
            GlStateManager.depthMask(false);
            GlStateManager.disableLighting();
            if (usedShadowDepthBuffers >= 1) {
                GlStateManager.setActiveTexture(33988);
                GlStateManager.func_179144_i(sfbDepthTextures.get(0));
                if (usedShadowDepthBuffers >= 2) {
                    GlStateManager.setActiveTexture(33989);
                    GlStateManager.func_179144_i(sfbDepthTextures.get(1));
                }
            }
            for (enableAltBuffers = 0; enableAltBuffers < usedColorBuffers; ++enableAltBuffers) {
                GlStateManager.setActiveTexture(33984 + colorTextureTextureImageUnit[enableAltBuffers]);
                GlStateManager.func_179144_i(dfbColorTexturesA[enableAltBuffers]);
            }
            GlStateManager.setActiveTexture(33990);
            GlStateManager.func_179144_i(dfbDepthTextures.get(0));
            if (usedDepthBuffers >= 2) {
                GlStateManager.setActiveTexture(33995);
                GlStateManager.func_179144_i(dfbDepthTextures.get(1));
                if (usedDepthBuffers >= 3) {
                    GlStateManager.setActiveTexture(33996);
                    GlStateManager.func_179144_i(dfbDepthTextures.get(2));
                }
            }
            for (enableAltBuffers = 0; enableAltBuffers < usedShadowColorBuffers; ++enableAltBuffers) {
                GlStateManager.setActiveTexture(33997 + enableAltBuffers);
                GlStateManager.func_179144_i(sfbColorTextures.get(enableAltBuffers));
            }
            if (noiseTextureEnabled) {
                GlStateManager.setActiveTexture(33984 + Shaders.noiseTexture.textureUnit);
                GlStateManager.func_179144_i(noiseTexture.getID());
                GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
                GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
                GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
                GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
            }
            GlStateManager.setActiveTexture(33984);
            boolean var5 = true;
            for (maskR = 0; maskR < usedColorBuffers; ++maskR) {
                EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)(36064 + maskR), (int)3553, (int)dfbColorTexturesA[8 + maskR], (int)0);
            }
            EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)36096, (int)3553, (int)dfbDepthTextures.get(0), (int)0);
            GL20.glDrawBuffers((IntBuffer)dfbDrawBuffers);
            Shaders.checkGLError("pre-composite");
            for (maskR = 0; maskR < 8; ++maskR) {
                if (programsID[21 + maskR] == 0) continue;
                Shaders.useProgram(21 + maskR);
                Shaders.checkGLError(programNames[21 + maskR]);
                if (activeCompositeMipmapSetting != 0) {
                    Shaders.genCompositeMipmap();
                }
                Shaders.drawComposite();
                for (int i = 0; i < usedColorBuffers; ++i) {
                    if (!programsToggleColorTextures[21 + maskR][i]) continue;
                    int t0 = colorTexturesToggle[i];
                    int t1 = Shaders.colorTexturesToggle[i] = 8 - t0;
                    GlStateManager.setActiveTexture(33984 + colorTextureTextureImageUnit[i]);
                    GlStateManager.func_179144_i(dfbColorTexturesA[t1 + i]);
                    EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)(36064 + i), (int)3553, (int)dfbColorTexturesA[t0 + i], (int)0);
                }
                GlStateManager.setActiveTexture(33984);
            }
            Shaders.checkGLError("composite");
            isRenderingDfb = false;
            mc.getFramebuffer().bindFramebuffer(true);
            OpenGlHelper.func_153188_a(OpenGlHelper.field_153198_e, OpenGlHelper.field_153200_g, 3553, Shaders.mc.getFramebuffer().framebufferTexture, 0);
            GL11.glViewport((int)0, (int)0, (int)Shaders.mc.displayWidth, (int)Shaders.mc.displayHeight);
            if (EntityRenderer.anaglyphEnable) {
                boolean var6 = EntityRenderer.anaglyphField != 0;
                GlStateManager.colorMask(var6, !var6, !var6, true);
            }
            GlStateManager.depthMask(true);
            GL11.glClearColor((float)clearColorR, (float)clearColorG, (float)clearColorB, (float)1.0f);
            GL11.glClear((int)16640);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.func_179098_w();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(519);
            GlStateManager.depthMask(false);
            Shaders.checkGLError("pre-final");
            Shaders.useProgram(29);
            Shaders.checkGLError("final");
            if (activeCompositeMipmapSetting != 0) {
                Shaders.genCompositeMipmap();
            }
            Shaders.drawComposite();
            Shaders.checkGLError("renderCompositeFinal");
            isCompositeRendered = true;
            GlStateManager.enableLighting();
            GlStateManager.func_179098_w();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
            GL11.glPopMatrix();
            GL11.glMatrixMode((int)5888);
            GL11.glPopMatrix();
            Shaders.useProgram(0);
        }
    }

    public static void endRender() {
        if (isShadowPass) {
            Shaders.checkGLError("shadow endRender");
        } else {
            if (!isCompositeRendered) {
                Shaders.renderCompositeFinal();
            }
            isRenderingWorld = false;
            GlStateManager.colorMask(true, true, true, true);
            Shaders.useProgram(0);
            RenderHelper.disableStandardItemLighting();
            Shaders.checkGLError("endRender end");
        }
    }

    public static void beginSky() {
        isRenderingSky = true;
        fogEnabled = true;
        Shaders.setDrawBuffers(dfbDrawBuffers);
        Shaders.useProgram(5);
        Shaders.pushEntity(-2, 0);
    }

    public static void setSkyColor(Vec3 v3color) {
        skyColorR = (float)v3color.xCoord;
        skyColorG = (float)v3color.yCoord;
        skyColorB = (float)v3color.zCoord;
        Shaders.setProgramUniform3f("skyColor", skyColorR, skyColorG, skyColorB);
    }

    public static void drawHorizon() {
        WorldRenderer tess = Tessellator.getInstance().getWorldRenderer();
        float farDistance = Shaders.mc.gameSettings.renderDistanceChunks * 16;
        double xzq = (double)farDistance * 0.9238;
        double xzp = (double)farDistance * 0.3826;
        double xzn = -xzp;
        double xzm = -xzq;
        double top = 16.0;
        double bot = -cameraPositionY;
        tess.startDrawingQuads();
        tess.addVertex(xzn, bot, xzm);
        tess.addVertex(xzn, top, xzm);
        tess.addVertex(xzm, top, xzn);
        tess.addVertex(xzm, bot, xzn);
        tess.addVertex(xzm, bot, xzn);
        tess.addVertex(xzm, top, xzn);
        tess.addVertex(xzm, top, xzp);
        tess.addVertex(xzm, bot, xzp);
        tess.addVertex(xzm, bot, xzp);
        tess.addVertex(xzm, top, xzp);
        tess.addVertex(xzn, top, xzp);
        tess.addVertex(xzn, bot, xzp);
        tess.addVertex(xzn, bot, xzp);
        tess.addVertex(xzn, top, xzp);
        tess.addVertex(xzp, top, xzq);
        tess.addVertex(xzp, bot, xzq);
        tess.addVertex(xzp, bot, xzq);
        tess.addVertex(xzp, top, xzq);
        tess.addVertex(xzq, top, xzp);
        tess.addVertex(xzq, bot, xzp);
        tess.addVertex(xzq, bot, xzp);
        tess.addVertex(xzq, top, xzp);
        tess.addVertex(xzq, top, xzn);
        tess.addVertex(xzq, bot, xzn);
        tess.addVertex(xzq, bot, xzn);
        tess.addVertex(xzq, top, xzn);
        tess.addVertex(xzp, top, xzm);
        tess.addVertex(xzp, bot, xzm);
        tess.addVertex(xzp, bot, xzm);
        tess.addVertex(xzp, top, xzm);
        tess.addVertex(xzn, top, xzm);
        tess.addVertex(xzn, bot, xzm);
        Tessellator.getInstance().draw();
    }

    public static void preSkyList() {
        GL11.glColor3f((float)fogColorR, (float)fogColorG, (float)fogColorB);
        Shaders.drawHorizon();
        GL11.glColor3f((float)skyColorR, (float)skyColorG, (float)skyColorB);
    }

    public static void endSky() {
        isRenderingSky = false;
        Shaders.setDrawBuffers(dfbDrawBuffers);
        Shaders.useProgram(lightmapEnabled ? 3 : 2);
        Shaders.popEntity();
    }

    public static void beginUpdateChunks() {
        Shaders.checkGLError("beginUpdateChunks1");
        Shaders.checkFramebufferStatus("beginUpdateChunks1");
        if (!isShadowPass) {
            Shaders.useProgram(7);
        }
        Shaders.checkGLError("beginUpdateChunks2");
        Shaders.checkFramebufferStatus("beginUpdateChunks2");
    }

    public static void endUpdateChunks() {
        Shaders.checkGLError("endUpdateChunks1");
        Shaders.checkFramebufferStatus("endUpdateChunks1");
        if (!isShadowPass) {
            Shaders.useProgram(7);
        }
        Shaders.checkGLError("endUpdateChunks2");
        Shaders.checkFramebufferStatus("endUpdateChunks2");
    }

    public static boolean shouldRenderClouds(GameSettings gs) {
        if (!shaderPackLoaded) {
            return true;
        }
        Shaders.checkGLError("shouldRenderClouds");
        return isShadowPass ? configCloudShadow : gs.clouds;
    }

    public static void beginClouds() {
        fogEnabled = true;
        Shaders.pushEntity(-3, 0);
        Shaders.useProgram(6);
    }

    public static void endClouds() {
        Shaders.disableFog();
        Shaders.popEntity();
        Shaders.useProgram(lightmapEnabled ? 3 : 2);
    }

    public static void beginEntities() {
        if (isRenderingWorld) {
            Shaders.useProgram(16);
            Shaders.resetDisplayListModels();
        }
    }

    public static void nextEntity(Entity entity) {
        if (isRenderingWorld) {
            Shaders.useProgram(16);
            Shaders.setEntityId(entity);
        }
    }

    public static void setEntityId(Entity entity) {
        if (isRenderingWorld && !isShadowPass && uniformEntityId.isDefined()) {
            int id = EntityList.getEntityID(entity);
            uniformEntityId.setValue(id);
        }
    }

    public static void beginSpiderEyes() {
        if (isRenderingWorld && programsID[18] != programsID[0]) {
            Shaders.useProgram(18);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.0f);
            GlStateManager.blendFunc(770, 771);
        }
    }

    public static void endEntities() {
        if (isRenderingWorld) {
            Shaders.useProgram(lightmapEnabled ? 3 : 2);
        }
    }

    public static void setEntityColor(float r, float g, float b, float a) {
        if (isRenderingWorld && !isShadowPass) {
            uniformEntityColor.setValue(r, g, b, a);
        }
    }

    public static void beginLivingDamage() {
        if (isRenderingWorld) {
            ShadersTex.bindTexture(defaultTexture);
            if (!isShadowPass) {
                Shaders.setDrawBuffers(drawBuffersColorAtt0);
            }
        }
    }

    public static void endLivingDamage() {
        if (isRenderingWorld && !isShadowPass) {
            Shaders.setDrawBuffers(programsDrawBuffers[16]);
        }
    }

    public static void beginBlockEntities() {
        if (isRenderingWorld) {
            Shaders.checkGLError("beginBlockEntities");
            Shaders.useProgram(13);
        }
    }

    public static void nextBlockEntity(TileEntity tileEntity) {
        if (isRenderingWorld) {
            Shaders.checkGLError("nextBlockEntity");
            Shaders.useProgram(13);
            Shaders.setBlockEntityId(tileEntity);
        }
    }

    public static void setBlockEntityId(TileEntity tileEntity) {
        if (isRenderingWorld && !isShadowPass && uniformBlockEntityId.isDefined()) {
            Block block = tileEntity.getBlockType();
            int blockId = Block.getIdFromBlock(block);
            uniformBlockEntityId.setValue(blockId);
        }
    }

    public static void endBlockEntities() {
        if (isRenderingWorld) {
            Shaders.checkGLError("endBlockEntities");
            Shaders.useProgram(lightmapEnabled ? 3 : 2);
            ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
        }
    }

    public static void beginLitParticles() {
        Shaders.useProgram(3);
    }

    public static void beginParticles() {
        Shaders.useProgram(2);
    }

    public static void endParticles() {
        Shaders.useProgram(3);
    }

    public static void readCenterDepth() {
        if (!isShadowPass && centerDepthSmoothEnabled) {
            tempDirectFloatBuffer.clear();
            GL11.glReadPixels((int)(renderWidth / 2), (int)(renderHeight / 2), (int)1, (int)1, (int)6402, (int)5126, (FloatBuffer)tempDirectFloatBuffer);
            centerDepth = tempDirectFloatBuffer.get(0);
            float fadeScalar = (float)diffSystemTime * 0.01f;
            float fadeFactor = (float)Math.exp(Math.log(0.5) * (double)fadeScalar / (double)centerDepthSmoothHalflife);
            centerDepthSmooth = centerDepthSmooth * fadeFactor + centerDepth * (1.0f - fadeFactor);
        }
    }

    public static void beginWeather() {
        if (!isShadowPass) {
            if (usedDepthBuffers >= 3) {
                GlStateManager.setActiveTexture(33996);
                GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)renderWidth, (int)renderHeight);
                GlStateManager.setActiveTexture(33984);
            }
            GlStateManager.enableDepth();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableAlpha();
            Shaders.useProgram(20);
        }
    }

    public static void endWeather() {
        GlStateManager.disableBlend();
        Shaders.useProgram(3);
    }

    public static void preWater() {
        if (usedDepthBuffers >= 2) {
            GlStateManager.setActiveTexture(33995);
            Shaders.checkGLError("pre copy depth");
            GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)renderWidth, (int)renderHeight);
            Shaders.checkGLError("copy depth");
            GlStateManager.setActiveTexture(33984);
        }
        ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
    }

    public static void beginWater() {
        if (isRenderingWorld) {
            if (!isShadowPass) {
                Shaders.useProgram(12);
                GlStateManager.enableBlend();
                GlStateManager.depthMask(true);
            } else {
                GlStateManager.depthMask(true);
            }
        }
    }

    public static void endWater() {
        if (isRenderingWorld) {
            if (isShadowPass) {
                // empty if block
            }
            Shaders.useProgram(lightmapEnabled ? 3 : 2);
        }
    }

    public static void beginProjectRedHalo() {
        if (isRenderingWorld) {
            Shaders.useProgram(1);
        }
    }

    public static void endProjectRedHalo() {
        if (isRenderingWorld) {
            Shaders.useProgram(3);
        }
    }

    public static void applyHandDepth() {
        if ((double)configHandDepthMul != 1.0) {
            GL11.glScaled((double)1.0, (double)1.0, (double)configHandDepthMul);
        }
    }

    public static void beginHand() {
        GL11.glMatrixMode((int)5888);
        GL11.glPushMatrix();
        GL11.glMatrixMode((int)5889);
        GL11.glPushMatrix();
        GL11.glMatrixMode((int)5888);
        Shaders.useProgram(19);
        Shaders.checkGLError("beginHand");
        Shaders.checkFramebufferStatus("beginHand");
    }

    public static void endHand() {
        Shaders.checkGLError("pre endHand");
        Shaders.checkFramebufferStatus("pre endHand");
        GL11.glMatrixMode((int)5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode((int)5888);
        GL11.glPopMatrix();
        GlStateManager.blendFunc(770, 771);
        Shaders.checkGLError("endHand");
    }

    public static void beginFPOverlay() {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
    }

    public static void endFPOverlay() {
    }

    public static void glEnableWrapper(int cap) {
        GL11.glEnable((int)cap);
        if (cap == 3553) {
            Shaders.enableTexture2D();
        } else if (cap == 2912) {
            Shaders.enableFog();
        }
    }

    public static void glDisableWrapper(int cap) {
        GL11.glDisable((int)cap);
        if (cap == 3553) {
            Shaders.disableTexture2D();
        } else if (cap == 2912) {
            Shaders.disableFog();
        }
    }

    public static void sglEnableT2D(int cap) {
        GL11.glEnable((int)cap);
        Shaders.enableTexture2D();
    }

    public static void sglDisableT2D(int cap) {
        GL11.glDisable((int)cap);
        Shaders.disableTexture2D();
    }

    public static void sglEnableFog(int cap) {
        GL11.glEnable((int)cap);
        Shaders.enableFog();
    }

    public static void sglDisableFog(int cap) {
        GL11.glDisable((int)cap);
        Shaders.disableFog();
    }

    public static void enableTexture2D() {
        if (isRenderingSky) {
            Shaders.useProgram(5);
        } else if (activeProgram == 1) {
            Shaders.useProgram(lightmapEnabled ? 3 : 2);
        }
    }

    public static void disableTexture2D() {
        if (isRenderingSky) {
            Shaders.useProgram(4);
        } else if (activeProgram == 2 || activeProgram == 3) {
            Shaders.useProgram(1);
        }
    }

    public static void beginLeash() {
        Shaders.useProgram(1);
    }

    public static void endLeash() {
        Shaders.useProgram(16);
    }

    public static void enableFog() {
        fogEnabled = true;
        Shaders.setProgramUniform1i("fogMode", fogMode);
    }

    public static void disableFog() {
        fogEnabled = false;
        Shaders.setProgramUniform1i("fogMode", 0);
    }

    public static void setFog(int fogMode) {
        GlStateManager.setFog(fogMode);
        if (fogEnabled) {
            Shaders.setProgramUniform1i("fogMode", fogMode);
        }
    }

    public static void sglFogi(int pname, int param) {
        GL11.glFogi((int)pname, (int)param);
        if (pname == 2917) {
            fogMode = param;
            if (fogEnabled) {
                Shaders.setProgramUniform1i("fogMode", fogMode);
            }
        }
    }

    public static void enableLightmap() {
        lightmapEnabled = true;
        if (activeProgram == 2) {
            Shaders.useProgram(3);
        }
    }

    public static void disableLightmap() {
        lightmapEnabled = false;
        if (activeProgram == 3) {
            Shaders.useProgram(2);
        }
    }

    public static int getEntityData() {
        return entityData[entityDataIndex * 2];
    }

    public static int getEntityData2() {
        return entityData[entityDataIndex * 2 + 1];
    }

    public static int setEntityData1(int data1) {
        Shaders.entityData[Shaders.entityDataIndex * 2] = entityData[entityDataIndex * 2] & 0xFFFF | data1 << 16;
        return data1;
    }

    public static int setEntityData2(int data2) {
        Shaders.entityData[Shaders.entityDataIndex * 2 + 1] = entityData[entityDataIndex * 2 + 1] & 0xFFFF0000 | data2 & 0xFFFF;
        return data2;
    }

    public static void pushEntity(int data0, int data1) {
        Shaders.entityData[++Shaders.entityDataIndex * 2] = data0 & 0xFFFF | data1 << 16;
        Shaders.entityData[Shaders.entityDataIndex * 2 + 1] = 0;
    }

    public static void pushEntity(int data0) {
        Shaders.entityData[++Shaders.entityDataIndex * 2] = data0 & 0xFFFF;
        Shaders.entityData[Shaders.entityDataIndex * 2 + 1] = 0;
    }

    public static void pushEntity(Block block) {
        Shaders.entityData[++Shaders.entityDataIndex * 2] = Block.blockRegistry.getIDForObject(block) & 0xFFFF | block.getRenderType() << 16;
        Shaders.entityData[Shaders.entityDataIndex * 2 + 1] = 0;
    }

    public static void popEntity() {
        Shaders.entityData[Shaders.entityDataIndex * 2] = 0;
        Shaders.entityData[Shaders.entityDataIndex * 2 + 1] = 0;
        --entityDataIndex;
    }

    public static void mcProfilerEndSection() {
        Shaders.mc.mcProfiler.endSection();
    }

    public static String getShaderPackName() {
        return shaderPack == null ? null : (shaderPack instanceof ShaderPackNone ? null : shaderPack.getName());
    }

    public static void nextAntialiasingLevel() {
        configAntialiasingLevel += 2;
        if ((configAntialiasingLevel = configAntialiasingLevel / 2 * 2) > 4) {
            configAntialiasingLevel = 0;
        }
        configAntialiasingLevel = Config.limit(configAntialiasingLevel, 0, 4);
    }

    public static void checkShadersModInstalled() {
        try {
            Class<?> class_ = Class.forName("shadersmod.transform.SMCClassTransformer");
        }
        catch (Throwable var1) {
            return;
        }
        throw new RuntimeException("Shaders Mod detected. Please remove it, OptiFine has built-in support for shaders.");
    }

    public static void resourcesReloaded() {
        Shaders.loadShaderPackResources();
    }

    private static void loadShaderPackResources() {
        shaderPackResources = new HashMap<String, String>();
        if (shaderPackLoaded) {
            ArrayList<String> listFiles = new ArrayList<String>();
            String PREFIX = "/shaders/lang/";
            String EN_US = "en_US";
            String SUFFIX = ".lang";
            listFiles.add(String.valueOf(PREFIX) + EN_US + SUFFIX);
            if (!Config.getGameSettings().language.equals(EN_US)) {
                listFiles.add(String.valueOf(PREFIX) + Config.getGameSettings().language + SUFFIX);
            }
            try {
                for (String file : listFiles) {
                    InputStream in = shaderPack.getResourceAsStream(file);
                    if (in == null) continue;
                    Properties props = new Properties();
                    Lang.loadLocaleData(in, props);
                    in.close();
                    Set<Object> keys = props.keySet();
                    for (String string : keys) {
                        String value = props.getProperty(string);
                        shaderPackResources.put(string, value);
                    }
                }
            }
            catch (IOException var12) {
                var12.printStackTrace();
            }
        }
    }

    public static String translate(String key, String def) {
        String str = shaderPackResources.get(key);
        return str == null ? def : str;
    }

    public static boolean isProgramPath(String program) {
        if (program == null) {
            return false;
        }
        if (program.length() <= 0) {
            return false;
        }
        int pos = program.lastIndexOf("/");
        if (pos >= 0) {
            program = program.substring(pos + 1);
        }
        return Arrays.asList(programNames).contains(program);
    }

    static class NamelessClass341846571 {
        static final int[] $SwitchMap$shadersmod$client$EnumShaderOption = new int[EnumShaderOption.values().length];

        static {
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.ANTIALIASING.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.NORMAL_MAP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SPECULAR_MAP.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.RENDER_RES_MUL.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SHADOW_RES_MUL.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.HAND_DEPTH_MUL.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.CLOUD_SHADOW.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.OLD_LIGHTING.ordinal()] = 8;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SHADER_PACK.ordinal()] = 9;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TWEAK_BLOCK_DAMAGE.ordinal()] = 10;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SHADOW_CLIP_FRUSTRUM.ordinal()] = 11;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MIN_FIL_B.ordinal()] = 12;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MIN_FIL_N.ordinal()] = 13;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MIN_FIL_S.ordinal()] = 14;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MAG_FIL_B.ordinal()] = 15;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MAG_FIL_N.ordinal()] = 16;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass341846571.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MAG_FIL_S.ordinal()] = 17;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        NamelessClass341846571() {
        }
    }
}

