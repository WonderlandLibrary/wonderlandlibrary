package vip.radium.module.impl.esp;

import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.utils.render.Colors;
import vip.radium.utils.render.OGLUtils;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.Supplier;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(label = "Chams", category = ModuleCategory.ESP)
public final class Chams extends Module {
    private static Chams cached;
    // Hurt effect options
    private final Property<Boolean> hurtEffectProperty = new Property<>("Hurt Effect", true);
    private final EnumProperty<HurtEffect> hurtEffectStyleProperty = new EnumProperty<>("Hurt Style", HurtEffect.COLOR,
            hurtEffectProperty::getValue);
    private final Property<Integer> hurtEffectColorProperty = new Property<>("Hurt Color", Colors.PURPLE,
            () -> hurtEffectProperty.getValue() && hurtEffectStyleProperty.getValue() == HurtEffect.COLOR);
    // Hands options
    private final Property<Boolean> handsProperty = new Property<>("Hands", true);
    private final Property<Integer> handsColorProperty = new Property<Integer>("Hands Col", Colors.RED,
            handsProperty::getValue);
    // Model shading options
    public final Property<Boolean> occludedFlatProperty = new Property<>("Occluded Flat", true);
    public final Property<Boolean> visibleFlatProperty = new Property<>("Visible Flat", true);
    public final Property<Boolean> textureOccludedProperty = new Property<>("Tex Occluded", false);
    public final Property<Boolean> textureVisibleProperty = new Property<>("Tex Visible", false);
    // Color modes
    public final EnumProperty<ColorMode> visibleColorModeProperty = new EnumProperty<>("V-Color Mode",
            ColorMode.COLOR);
    public final EnumProperty<ColorMode> occludedColorModeProperty = new EnumProperty<>("O-Color Mode",
            ColorMode.COLOR);

    public final DoubleProperty visibleAlphaProperty = new DoubleProperty("Visible Alpha", 1.0D,
            () -> visibleColorModeProperty.isAvailable() && visibleColorModeProperty.getValue() == ColorMode.RAINBOW,
            0.0D, 1.0D, 0.1D);
    public final DoubleProperty occludedAlphaProperty = new DoubleProperty("Occluded Alpha", 0.4D,
            () -> occludedColorModeProperty.isAvailable() && occludedColorModeProperty.getValue() == ColorMode.RAINBOW,
            0.0D, 1.0D, 0.1D);
    // Color options
    public final Property<Integer> visibleColorProperty = new Property<>("V-Color", Colors.RED,
            () -> visibleColorModeProperty.isAvailable() && visibleColorModeProperty.getValue() != ColorMode.RAINBOW);
    public final Property<Integer> occludedColorProperty = new Property<>("O-Color", Colors.GREEN,
            () -> occludedColorModeProperty.isAvailable() && occludedColorModeProperty.getValue() != ColorMode.RAINBOW);
    // Secondary color options
    public final Property<Integer> secondVisibleColorProperty = new Property<>("V-Color-2", Colors.RED,
            () -> visibleColorModeProperty.isAvailable() && visibleColorModeProperty.getValue() == ColorMode.PULSING);
    public final Property<Integer> secondOccludedColorProperty = new Property<>("O-Color-2", Colors.GREEN,
            () -> occludedColorModeProperty.isAvailable() && occludedColorModeProperty.getValue() == ColorMode.PULSING);

    private final float[] hurtEffectColor = new float[4];

    public Chams() {
        cached = this;

        hurtEffectColorProperty.addValueChangeListener((oldValue, value) -> {
            float[] rgb = getRGB(value);

            hurtEffectColor[0] = rgb[0];
            hurtEffectColor[1] = rgb[1];
            hurtEffectColor[2] = rgb[2];
            hurtEffectColor[3] = rgb[3];
        });
    }

    public static boolean shouldBindTexture() {
        return cached.textureOccludedProperty.getValue() || cached.textureVisibleProperty.getValue();
    }

    public static void preRenderOccluded(boolean disableTexture, int occludedColor, boolean occludedFlat) {
        if (disableTexture)
            glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        if (occludedFlat)
            glDisable(GL_LIGHTING);
        glEnable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(0.0F, -1000000.0F);
        OpenGlHelper.setLightmapTextureCoords(1, 240.0F, 240.0F);
        glDepthMask(false);
        OGLUtils.color(occludedColor);
    }

    public static void preRenderVisible(boolean disableTexture, boolean enableTexture, int visibleColor, boolean visibleFlat, boolean occludedFlat) {
        if (enableTexture)
            glEnable(GL_TEXTURE_2D);
        else if (disableTexture)
            glDisable(GL_TEXTURE_2D);

        glDepthMask(true);
        if (occludedFlat && !visibleFlat)
            glEnable(GL_LIGHTING);
        else if (!occludedFlat && visibleFlat)
            glDisable(GL_LIGHTING);

        OGLUtils.color(visibleColor);
        glDisable(GL_POLYGON_OFFSET_FILL);
    }

    public static void postRender(boolean enableTexture, boolean visibleFlat) {
        if (visibleFlat)
            glEnable(GL_LIGHTING);
        if (enableTexture)
            glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void preHandRender() {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        OGLUtils.color(getInstance().handsColorProperty.getValue());
    }

    public static void postHandRender() {
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    private static float[] getRGB(int hex) {
        return new float[]{
                (float) (hex >> 16 & 255) / 255.0F,
                (float) (hex >> 8 & 255) / 255.0F,
                (float) (hex & 255) / 255.0F,
                (float) (hex >> 24 & 255) / 255.0F
        };
    }

    public static boolean shouldRenderHand() {
        return getInstance().isEnabled() &&
                getInstance().handsProperty.getValue();
    }

    public static boolean isChamsEnabled() {
        return getInstance().isEnabled();
    }

    public static boolean isRenderHurtEffect() {
        return getInstance().hurtEffectProperty.getValue();
    }

    public static HurtEffect getHurtEffect() {
        return getInstance().hurtEffectStyleProperty.getValue();
    }

    public static boolean isValid(EntityLivingBase entity) {
        return !entity.isInvisible() &&
                entity.isEntityAlive() &&
                entity instanceof EntityPlayer;
    }

    public static Chams getInstance() {
        if (cached != null) return cached;
        else return (cached = ModuleManager.getInstance(Chams.class));
    }

    public enum ColorMode {
        COLOR,
        RAINBOW,
        PULSING
    }

    public enum HurtEffect {
        OLD(() -> 1.0F,
                () -> 0.0F,
                () -> 0.0F,
                () -> 0.4F),
        NEW(() -> 1.0F,
                () -> 0.0F,
                () -> 0.0F,
                () -> 0.3F),
        COLOR(() -> cached.hurtEffectColor[0],
                () -> cached.hurtEffectColor[1],
                () -> cached.hurtEffectColor[2],
                () -> cached.hurtEffectColor[3]);

        private final Supplier<Float> red;
        private final Supplier<Float> green;
        private final Supplier<Float> blue;
        private final Supplier<Float> alpha;

        HurtEffect(Supplier<Float> red, Supplier<Float> g, Supplier<Float> b, Supplier<Float> a) {
            this.red = red;
            this.green = g;
            this.blue = b;
            this.alpha = a;
        }

        public float getRed() {
            return red.get();
        }

        public float getGreen() {
            return green.get();
        }

        public float getBlue() {
            return blue.get();
        }

        public float getAlpha() {
            return alpha.get();
        }
    }
}
