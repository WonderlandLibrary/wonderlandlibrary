package io.github.nevalackin.client.module.render.model;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.render.game.UpdateFramebufferSizeEvent;
import io.github.nevalackin.client.event.render.model.ModelRenderEvent;
import io.github.nevalackin.client.event.render.model.RenderLivingEntityEvent;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.event.render.overlay.RenderHandEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.ColourProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.client.util.render.GLShader;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public final class Chams extends Module {

    private final EnumProperty<Mode> modeProperty = new EnumProperty<>("Mode", Mode.OLD);

    private final ColourProperty shaderColourProperty = new ColourProperty("Shader Colour", ColourUtil.getClientColour(),
                                                                           () -> this.modeProperty.getValue() == Mode.SHADER);

    private final BooleanProperty wallHackProperty = new BooleanProperty("Wall Hack", true);
    private final EnumProperty<Layer> layerProperty = new EnumProperty<>("Layer", Layer.VISIBLE);

    private final BooleanProperty visibleEnabledProperty = new BooleanProperty("V-Enabled", true, this::isShowingVisibleLayer);
    private final BooleanProperty visibleTexturedProperty = new BooleanProperty("V-Textured", false, this::isShowingVisibleOptions);
    private final BooleanProperty visibleFlatProperty = new BooleanProperty("V-Flat", true, this::isShowingVisibleOptions);
    private final ColourProperty visibleColourProperty = new ColourProperty("V-Colour", ColourUtil.getClientColour(), this::isShowingVisibleOptions);

    private final BooleanProperty occludedEnabledProperty = new BooleanProperty("O-Enabled", true, this::isShowingOccludedLayer);
    private final BooleanProperty occludedTexturedProperty = new BooleanProperty("O-Textured", false, this::isShowingOccludedOptions);
    private final BooleanProperty occludedFlatProperty = new BooleanProperty("O-Flat", true, this::isShowingOccludedOptions);
    private final ColourProperty occludedColourProperty = new ColourProperty("O-Colour", ColourUtil.getSecondaryColour(), this::isShowingOccludedOptions);

    private final BooleanProperty handsEnabledProperty = new BooleanProperty("Hands Enabled", true, this::isHandsLayerVisible);
    private final ColourProperty handsColourProperty = new ColourProperty("Hands Colour",
                                                                          ColourUtil.overwriteAlphaComponent(ColourUtil.getClientColour(), 0x80),
                                                                          this::isHandsOptionsVisible);
    private final BooleanProperty leftHandedProperty = new BooleanProperty("Left Handed", false,
                                                                           this::isHandsOptionsVisible);

    private static final String FILL_SHADER =
        "#version 120\n" +
            "\n" +
            "uniform sampler2D texture;\n" +
            "uniform vec2 texelSize;\n" +
            "\n" +
            "uniform vec4 colour;\n" +
            "\n" +
            "void main(void) {\n" +
            "    vec4 sample = texture2D(texture, gl_TexCoord[0].xy);\n" +
            "    gl_FragColor = vec4(colour.rgb, min(sample.a, colour.a));\n" +
            "}\n";

    public static final String VERTEX_SHADER =
        "#version 120 \n" +
            "\n" +
            "void main() {\n" +
            "    gl_TexCoord[0] = gl_MultiTexCoord0;\n" +
            "    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n" +
            "}";

    private final GLShader shader = new GLShader(VERTEX_SHADER, FILL_SHADER) {
        @Override
        public void setupUniforms() {
            this.setupUniform("texture");
            this.setupUniform("texelSize");
            this.setupUniform("colour");

            glUniform1i(this.getUniformLocation("texture"), 0);
        }

        @Override
        public void updateUniforms() {
            final Color colour = shaderColourProperty.getColour();

            glUniform2f(this.getUniformLocation("texelSize"),
                        1.f / mc.displayWidth,
                        1.f / mc.displayHeight);
            glUniform4f(this.getUniformLocation("colour"),
                        colour.getRed() / 255.f,
                        colour.getGreen() / 255.f,
                        colour.getBlue() / 255.f,
                        colour.getAlpha() / 255.f);
        }
    };

    private Framebuffer framebuffer;

    public Chams() {
        super("Chams", Category.RENDER, Category.SubCategory.RENDER_MODEL);

        this.register(this.modeProperty, this.shaderColourProperty, this.wallHackProperty, this.layerProperty,
                      this.visibleEnabledProperty, this.visibleTexturedProperty, this.visibleFlatProperty, this.visibleColourProperty,
                      this.occludedEnabledProperty, this.occludedTexturedProperty, this.occludedFlatProperty, this.occludedColourProperty,
                      this.handsEnabledProperty, this.handsColourProperty, this.leftHandedProperty);
    }

    private boolean isShowingVisibleLayer() {
        return this.modeProperty.getValue() == Mode.OLD && this.layerProperty.check() && this.layerProperty.getValue() == Layer.VISIBLE;
    }

    private boolean isHandsLayerVisible() {
        return this.layerProperty.check() && this.layerProperty.getValue() == Layer.HANDS;
    }

    private boolean isShowingOccludedLayer() {
        return this.modeProperty.getValue() == Mode.OLD && this.layerProperty.check() && this.layerProperty.getValue() == Layer.OCCLUDED;
    }

    private boolean isShowingVisibleOptions() {
        return this.isShowingVisibleLayer() && this.visibleEnabledProperty.getValue();
    }

    private boolean isShowingOccludedOptions() {
        return this.isShowingOccludedLayer() && this.occludedEnabledProperty.getValue();
    }

    private boolean isHandsOptionsVisible() {
        return this.isHandsLayerVisible() && this.handsEnabledProperty.getValue();
    }

    private boolean wasBlendEnabled;

    private boolean isTextureEnabled, isLightingEnabled;

    private void preHandsRenderCallback() {
        // Restore blend state (enabled for alpha)
        this.wasBlendEnabled = DrawUtil.glEnableBlend();
        // Fixes brightness issues
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        // Colour
        DrawUtil.glColour(this.handsColourProperty.getValue());
        // Alpha fix (so alpha slider works all the way)
        glDisable(GL_ALPHA_TEST);
        // Disable texture drawing (solid colour chams)
        glDisable(GL_TEXTURE_2D);
        // Disable lighting (flat chams)
        glDisable(GL_LIGHTING);
    }

    private void postHandsRenderCallback() {
        // Restore alpha blending
        DrawUtil.glRestoreBlend(this.wasBlendEnabled);
        // Restore alpha func
        glEnable(GL_ALPHA_TEST);
        // Restore colour
        glColor4f(1, 1, 1, 1);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Re-enabling lighting (shadows)
        glEnable(GL_LIGHTING);
    }

    @EventLink
    private final Listener<RenderHandEvent> onRenderHand = event -> {
        if (this.handsEnabledProperty.getValue()) {
            event.setBindTexture(false);
            event.setPreRenderCallback(this::preHandsRenderCallback);
            event.setPostRenderCallback(this::postHandsRenderCallback);
            event.setLeft(this.leftHandedProperty.getValue());
        }
    };

    @EventLink
    private final Listener<UpdateFramebufferSizeEvent> onFramebufferResize = event -> {
        if (this.framebuffer != null) {
            // Delete old buffers as to not cause a memory leak
            this.framebuffer.deleteFramebuffer();
        }
        // Create new framebuffer
        // False means it doesn't allocate a depth buffer which we don't need
        this.framebuffer = new Framebuffer(event.getWidth(), event.getHeight(), false);
    };

    @EventLink(4)
    private final Listener<ModelRenderEvent> onModelRender = event -> {
        if (this.validateEntity(event.getEntity())) {
            switch (this.modeProperty.getValue()) {
                case OLD:
                    if (!this.visibleEnabledProperty.getValue() && !this.occludedEnabledProperty.getValue())
                        return;

                    if (event.isPre()) {
                        // Store blend state
                        this.wasBlendEnabled = false;
                        // Store lighting and texture states
                        this.isLightingEnabled = true;
                        this.isTextureEnabled = true;
                        // Store flat states
                        // Store states
                        final boolean visibleFlat = this.visibleFlatProperty.getValue();
                        final boolean occludedFlat = this.occludedFlatProperty.getValue();
                        // Store texture states
                        final boolean visibleTexture = this.visibleTexturedProperty.getValue();
                        final boolean occludedTexture = this.occludedTexturedProperty.getValue();
                        // Fixes brightness issues
                        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                        // Store colour values
                        final int occludedColour = this.occludedColourProperty.getValue();
                        final int visibleColour = this.visibleColourProperty.getValue();
                        // When opacity of occluded or visible colour is less than max
                        if (((occludedColour >> 24) & 0xFF) < 0xFF || ((visibleColour >> 24) & 0xFF) < 0xFF) {
                            // Enable blending
                            this.wasBlendEnabled = DrawUtil.glEnableBlend();
                        }

                        if (this.occludedEnabledProperty.getValue()) {
                            if (!occludedTexture) {
                                glDisable(GL_TEXTURE_2D);
                                this.isTextureEnabled = false;
                            }

                            if (occludedFlat) {
                                glDisable(GL_LIGHTING);
                                this.isLightingEnabled = false;
                            }
                            // Make visible through walls
                            glDisable(GL_DEPTH_TEST);
                            glDepthMask(false);

                            // Set colour to occluded
                            DrawUtil.glColour(occludedColour);

                            // Draw occluded model
                            event.drawModel();

                            // Disable seeing through walls
                            glEnable(GL_DEPTH_TEST);
                            glDepthMask(true);
                        }

                        if (this.visibleEnabledProperty.getValue()) {
                            if (this.isLightingEnabled == visibleFlat) {
                                if (visibleFlat) {
                                    glDisable(GL_LIGHTING);
                                } else {
                                    glEnable(GL_LIGHTING);
                                }
                                this.isLightingEnabled = !visibleFlat;
                            }

                            if (this.isTextureEnabled != visibleTexture) {
                                if (visibleTexture) {
                                    glEnable(GL_TEXTURE_2D);
                                } else {
                                    glDisable(GL_TEXTURE_2D);
                                }
                                this.isTextureEnabled = visibleTexture;
                            }

                            // Set colour to visible
                            DrawUtil.glColour(visibleColour);
                        } else {
                            this.restore();
                        }
                    } else if (this.visibleEnabledProperty.getValue()) {
                        this.restore();
                    }
                    break;
                case SHADER:
                    if (!event.isPre()) {
                        // Bind our frame buffer
                        // False here means it doesn't set the viewport (which isn't required because our framebuffer is the same size as minecraft's)
                        this.framebuffer.bindFramebuffer(false);
                        // Draw player model & layers (armor & sword n shit)
                        event.drawModel();
                        // Disable rendering the enchant glint when layers are drawn (to save performance & make it less buggy)
                        LayerRenderer.renderEnchantGlint = false;
                        event.drawLayers();
                        LayerRenderer.renderEnchantGlint = true;
                        // Bind the minecraft frame buffer
                        this.mc.getFramebuffer().bindFramebuffer(false);
                    }
                    break;
            }
        }
    };

    @EventLink
    private final Listener<RenderGameOverlayEvent> onRenderGameOverlay = event -> {
        switch (this.modeProperty.getValue()) {
            case SHADER:
                final ScaledResolution scaledResolution = event.getScaledResolution();
                // Use the outline shader
                this.shader.use();
                // Draw our framebuffer
                DrawUtil.glDrawFramebuffer(this.framebuffer.framebufferTexture, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
                // Disable shader
                glUseProgram(0);
                // Clear our framebuffer
                this.framebuffer.framebufferClear();
                // Need to rebind minecraft framebuffer after clearing ours
                this.mc.getFramebuffer().bindFramebuffer(false);
                break;
        }
    };

    public void restore() {
        // Restore lighting
        if (!this.isLightingEnabled)
            glEnable(GL_LIGHTING);
        // Restore texture
        if (!this.isTextureEnabled)
            glEnable(GL_TEXTURE_2D);
        // Restore colour
        glColor4f(1, 1, 1, 1);
        // Restore blend state
        DrawUtil.glRestoreBlend(this.wasBlendEnabled);
    }

    private void preRenderCallback() {
        glEnable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(1, -1_000_000);
    }

    private void postRenderCallback() {
        glPolygonOffset(1, 1_000_000);
        glDisable(GL_POLYGON_OFFSET_FILL);
    }

    @EventLink
    private final Listener<RenderLivingEntityEvent> onRenderLivingEntity = event -> {
        if (this.modeProperty.getValue() == Mode.OLD &&
            this.wallHackProperty.getValue() &&
            this.validateEntity(event.getEntity())) {
            // Set render callbacks
            event.setPreRenderCallback(this::preRenderCallback);
            event.setPostRenderCallback(this::postRenderCallback);
        }
    };

    private boolean validateEntity(final EntityLivingBase entity) {
        return entity instanceof EntityPlayer && entity.isEntityAlive() && !entity.isInvisible();
    }

    @Override
    public void onEnable() {
        // Create new on enable (because UpdateFramebufferSizeEvent is not called while module is disabled)
        this.framebuffer = new Framebuffer(this.mc.displayWidth, this.mc.displayHeight, false);
    }

    @Override
    public void onDisable() {
        // Delete old buffers to avoid memory leak
        this.framebuffer.deleteFramebuffer();
    }

    private enum Mode {
        SHADER("Shader"),
        OLD("Old");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum Layer {
        VISIBLE("Visible"),
        OCCLUDED("Occluded"),
        HANDS("Hands");

        private final String name;

        Layer(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
