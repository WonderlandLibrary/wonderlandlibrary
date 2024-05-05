package net.minecraft.client.settings;

import net.minecraft.src.Config;
import net.minecraft.util.MathHelper;

public static enum GameSettings.Options {
    INVERT_MOUSE("options.invertMouse", false, true),
    SENSITIVITY("options.sensitivity", true, false),
    FOV("options.fov", true, false, 30.0f, 110.0f, 1.0f),
    GAMMA("options.gamma", true, false),
    SATURATION("options.saturation", true, false),
    RENDER_DISTANCE("options.renderDistance", true, false, 2.0f, 16.0f, 1.0f),
    VIEW_BOBBING("options.viewBobbing", false, true),
    ANAGLYPH("options.anaglyph", false, true),
    FRAMERATE_LIMIT("options.framerateLimit", true, false, 0.0f, 260.0f, 5.0f),
    FBO_ENABLE("options.fboEnable", false, true),
    RENDER_CLOUDS("options.renderClouds", false, false),
    GRAPHICS("options.graphics", false, false),
    AMBIENT_OCCLUSION("options.ao", false, false),
    GUI_SCALE("options.guiScale", false, false),
    PARTICLES("options.particles", false, false),
    CHAT_VISIBILITY("options.chat.visibility", false, false),
    CHAT_COLOR("options.chat.color", false, true),
    CHAT_LINKS("options.chat.links", false, true),
    CHAT_OPACITY("options.chat.opacity", true, false),
    CHAT_LINKS_PROMPT("options.chat.links.prompt", false, true),
    SNOOPER_ENABLED("options.snooper", false, true),
    USE_FULLSCREEN("options.fullscreen", false, true),
    ENABLE_VSYNC("options.vsync", false, true),
    USE_VBO("options.vbo", false, true),
    TOUCHSCREEN("options.touchscreen", false, true),
    CHAT_SCALE("options.chat.scale", true, false),
    CHAT_WIDTH("options.chat.width", true, false),
    CHAT_HEIGHT_FOCUSED("options.chat.height.focused", true, false),
    CHAT_HEIGHT_UNFOCUSED("options.chat.height.unfocused", true, false),
    MIPMAP_LEVELS("options.mipmapLevels", true, false, 0.0f, 4.0f, 1.0f),
    FORCE_UNICODE_FONT("options.forceUnicodeFont", false, true),
    STREAM_BYTES_PER_PIXEL("options.stream.bytesPerPixel", true, false),
    STREAM_VOLUME_MIC("options.stream.micVolumne", true, false),
    STREAM_VOLUME_SYSTEM("options.stream.systemVolume", true, false),
    STREAM_KBPS("options.stream.kbps", true, false),
    STREAM_FPS("options.stream.fps", true, false),
    STREAM_COMPRESSION("options.stream.compression", false, false),
    STREAM_SEND_METADATA("options.stream.sendMetadata", false, true),
    STREAM_CHAT_ENABLED("options.stream.chat.enabled", false, false),
    STREAM_CHAT_USER_FILTER("options.stream.chat.userFilter", false, false),
    STREAM_MIC_TOGGLE_BEHAVIOR("options.stream.micToggleBehavior", false, false),
    BLOCK_ALTERNATIVES("options.blockAlternatives", false, true),
    REDUCED_DEBUG_INFO("options.reducedDebugInfo", false, true),
    ENTITY_SHADOWS("options.entityShadows", false, true),
    REALMS_NOTIFICATIONS("options.realmsNotifications", false, true),
    FOG_FANCY("of.options.FOG_FANCY", false, false),
    FOG_START("of.options.FOG_START", false, false),
    MIPMAP_TYPE("of.options.MIPMAP_TYPE", true, false, 0.0f, 3.0f, 1.0f),
    SMOOTH_FPS("of.options.SMOOTH_FPS", false, false),
    CLOUDS("of.options.CLOUDS", false, false),
    CLOUD_HEIGHT("of.options.CLOUD_HEIGHT", true, false),
    TREES("of.options.TREES", false, false),
    RAIN("of.options.RAIN", false, false),
    ANIMATED_WATER("of.options.ANIMATED_WATER", false, false),
    ANIMATED_LAVA("of.options.ANIMATED_LAVA", false, false),
    ANIMATED_FIRE("of.options.ANIMATED_FIRE", false, false),
    ANIMATED_PORTAL("of.options.ANIMATED_PORTAL", false, false),
    AO_LEVEL("of.options.AO_LEVEL", true, false),
    LAGOMETER("of.options.LAGOMETER", false, false),
    SHOW_FPS("of.options.SHOW_FPS", false, false),
    AUTOSAVE_TICKS("of.options.AUTOSAVE_TICKS", false, false),
    BETTER_GRASS("of.options.BETTER_GRASS", false, false),
    ANIMATED_REDSTONE("of.options.ANIMATED_REDSTONE", false, false),
    ANIMATED_EXPLOSION("of.options.ANIMATED_EXPLOSION", false, false),
    ANIMATED_FLAME("of.options.ANIMATED_FLAME", false, false),
    ANIMATED_SMOKE("of.options.ANIMATED_SMOKE", false, false),
    WEATHER("of.options.WEATHER", false, false),
    SKY("of.options.SKY", false, false),
    STARS("of.options.STARS", false, false),
    SUN_MOON("of.options.SUN_MOON", false, false),
    VIGNETTE("of.options.VIGNETTE", false, false),
    CHUNK_UPDATES("of.options.CHUNK_UPDATES", false, false),
    CHUNK_UPDATES_DYNAMIC("of.options.CHUNK_UPDATES_DYNAMIC", false, false),
    TIME("of.options.TIME", false, false),
    CLEAR_WATER("of.options.CLEAR_WATER", false, false),
    SMOOTH_WORLD("of.options.SMOOTH_WORLD", false, false),
    VOID_PARTICLES("of.options.VOID_PARTICLES", false, false),
    WATER_PARTICLES("of.options.WATER_PARTICLES", false, false),
    RAIN_SPLASH("of.options.RAIN_SPLASH", false, false),
    PORTAL_PARTICLES("of.options.PORTAL_PARTICLES", false, false),
    POTION_PARTICLES("of.options.POTION_PARTICLES", false, false),
    FIREWORK_PARTICLES("of.options.FIREWORK_PARTICLES", false, false),
    PROFILER("of.options.PROFILER", false, false),
    DRIPPING_WATER_LAVA("of.options.DRIPPING_WATER_LAVA", false, false),
    BETTER_SNOW("of.options.BETTER_SNOW", false, false),
    FULLSCREEN_MODE("of.options.FULLSCREEN_MODE", true, false, 0.0f, Config.getDisplayModes().length, 1.0f),
    ANIMATED_TERRAIN("of.options.ANIMATED_TERRAIN", false, false),
    SWAMP_COLORS("of.options.SWAMP_COLORS", false, false),
    RANDOM_ENTITIES("of.options.RANDOM_ENTITIES", false, false),
    SMOOTH_BIOMES("of.options.SMOOTH_BIOMES", false, false),
    CUSTOM_FONTS("of.options.CUSTOM_FONTS", false, false),
    CUSTOM_COLORS("of.options.CUSTOM_COLORS", false, false),
    SHOW_CAPES("of.options.SHOW_CAPES", false, false),
    CONNECTED_TEXTURES("of.options.CONNECTED_TEXTURES", false, false),
    CUSTOM_ITEMS("of.options.CUSTOM_ITEMS", false, false),
    AA_LEVEL("of.options.AA_LEVEL", true, false, 0.0f, 16.0f, 1.0f),
    AF_LEVEL("of.options.AF_LEVEL", true, false, 1.0f, 16.0f, 1.0f),
    ANIMATED_TEXTURES("of.options.ANIMATED_TEXTURES", false, false),
    NATURAL_TEXTURES("of.options.NATURAL_TEXTURES", false, false),
    EMISSIVE_TEXTURES("of.options.EMISSIVE_TEXTURES", false, false),
    HELD_ITEM_TOOLTIPS("of.options.HELD_ITEM_TOOLTIPS", false, false),
    DROPPED_ITEMS("of.options.DROPPED_ITEMS", false, false),
    LAZY_CHUNK_LOADING("of.options.LAZY_CHUNK_LOADING", false, false),
    CUSTOM_SKY("of.options.CUSTOM_SKY", false, false),
    FAST_MATH("of.options.FAST_MATH", false, false),
    FAST_RENDER("of.options.FAST_RENDER", false, false),
    TRANSLUCENT_BLOCKS("of.options.TRANSLUCENT_BLOCKS", false, false),
    DYNAMIC_FOV("of.options.DYNAMIC_FOV", false, false),
    DYNAMIC_LIGHTS("of.options.DYNAMIC_LIGHTS", false, false),
    ALTERNATE_BLOCKS("of.options.ALTERNATE_BLOCKS", false, false),
    CUSTOM_ENTITY_MODELS("of.options.CUSTOM_ENTITY_MODELS", false, false),
    ADVANCED_TOOLTIPS("of.options.ADVANCED_TOOLTIPS", false, false),
    SCREENSHOT_SIZE("of.options.SCREENSHOT_SIZE", false, false),
    CUSTOM_GUIS("of.options.CUSTOM_GUIS", false, false),
    RENDER_REGIONS("of.options.RENDER_REGIONS", false, false),
    SHOW_GL_ERRORS("of.options.SHOW_GL_ERRORS", false, false),
    SMART_ANIMATIONS("of.options.SMART_ANIMATIONS", false, false);

    private final boolean enumFloat;
    private final boolean enumBoolean;
    private final String enumString;
    private final float valueStep;
    private float valueMin;
    private float valueMax;

    public static GameSettings.Options getEnumOptions(int ordinal) {
        for (GameSettings.Options gamesettings$options : GameSettings.Options.values()) {
            if (gamesettings$options.returnEnumOrdinal() != ordinal) continue;
            return gamesettings$options;
        }
        return null;
    }

    private GameSettings.Options(String str, boolean isFloat, boolean isBoolean) {
        this(str, isFloat, isBoolean, 0.0f, 1.0f, 0.0f);
    }

    private GameSettings.Options(String str, boolean isFloat, boolean isBoolean, float valMin, float valMax, float valStep) {
        this.enumString = str;
        this.enumFloat = isFloat;
        this.enumBoolean = isBoolean;
        this.valueMin = valMin;
        this.valueMax = valMax;
        this.valueStep = valStep;
    }

    public boolean getEnumFloat() {
        return this.enumFloat;
    }

    public boolean getEnumBoolean() {
        return this.enumBoolean;
    }

    public int returnEnumOrdinal() {
        return this.ordinal();
    }

    public String getEnumString() {
        return this.enumString;
    }

    public float getValueMax() {
        return this.valueMax;
    }

    public void setValueMax(float value) {
        this.valueMax = value;
    }

    public float normalizeValue(float value) {
        return MathHelper.clamp_float((float)((this.snapToStepClamp(value) - this.valueMin) / (this.valueMax - this.valueMin)), (float)0.0f, (float)1.0f);
    }

    public float denormalizeValue(float value) {
        return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float((float)value, (float)0.0f, (float)1.0f));
    }

    public float snapToStepClamp(float value) {
        value = this.snapToStep(value);
        return MathHelper.clamp_float((float)value, (float)this.valueMin, (float)this.valueMax);
    }

    protected float snapToStep(float value) {
        if (this.valueStep > 0.0f) {
            value = this.valueStep * (float)Math.round((float)(value / this.valueStep));
        }
        return value;
    }

    static /* synthetic */ float access$000(GameSettings.Options x0) {
        return x0.valueMax;
    }

    static /* synthetic */ float access$100(GameSettings.Options x0) {
        return x0.valueMin;
    }
}
