package io.github.nevalackin.client.module.render.esp;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.render.game.UpdateFramebufferSizeEvent;
import io.github.nevalackin.client.event.render.model.ChestModelRenderEvent;
import io.github.nevalackin.client.event.render.model.ModelRenderEvent;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.ColourProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.util.player.TeamsUtil;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.client.util.render.GLShader;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.shader.Framebuffer;

import java.awt.*;

import static org.lwjgl.opengl.GL20.*;

public final class Glow extends Module {

    private static final String OUTLINE_SHADER =
        "#version 120\n" +
            "\n" +
            "uniform sampler2D texture;\n" +
            "uniform vec2 texelSize;\n" +
            "\n" +
            "uniform vec4 colour;\n" +
            "uniform float radius;\n" +
            "\n" +
            "void main() {\n" +
            "    float a = 0.0;\n" +
            "    vec3 rgb = colour.rgb;\n" +
            "    float closest = 1.0;\n" +
            "    for (float x = -radius; x <= radius; x++) {\n" +
            "        for (float y = -radius; y <= radius; y++) {\n" +
            "            vec2 st = gl_TexCoord[0].st + vec2(x, y) * texelSize;\n" +
            "            vec4 smpl = texture2D(texture, st);\n" +
            "            float dist = distance(st, gl_TexCoord[0].st);\n" +
            "            if (smpl.a > 0.0 && dist < closest) {" +
            "               rgb = smpl.rgb;\n" +
            "               closest = dist;\n" +
            "            }\n" +
            "            a += smpl.a*smpl.a;\n" +
            "        }\n" +
            "    }\n" +
            "    vec4 smpl = texture2D(texture, gl_TexCoord[0].st);\n" +
            "    gl_FragColor = vec4(rgb, a * colour.a / (4.0 * radius * radius)) * (smpl.a > 0.0 ? 0.0 : 1.0);\n" +
            "}\n";

    public static final String VERTEX_SHADER =
        "#version 120 \n" +
            "\n" +
            "void main() {\n" +
            "    gl_TexCoord[0] = gl_MultiTexCoord0;\n" +
            "    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n" +
            "}";

    private static final String FILL_SHADER =
        "#version 120\n" +
            "\n" +
            "uniform sampler2D texture;\n" +
            "uniform vec4 colour;\n" +
            "\n" +
            "void main() {\n" +
            "    vec4 smpl = texture2D(texture, gl_TexCoord[0].st);\n" +
            "    gl_FragColor = vec4(colour.rgb, smpl.a > 0.0 ? colour.a : 0.0);\n" +
            "}\n";

    private final GLShader shader = new GLShader(VERTEX_SHADER, OUTLINE_SHADER) {
        @Override
        public void setupUniforms() {
            this.setupUniform("texture");
            this.setupUniform("texelSize");
            this.setupUniform("colour");
            this.setupUniform("radius");

            glUniform1i(this.getUniformLocation("texture"), 0);
        }

        @Override
        public void updateUniforms() {
            final Color colour = colourProperty.getColour();

            glUniform4f(this.getUniformLocation("colour"),
                        colour.getRed() / 255.f,
                        colour.getGreen() / 255.f,
                        colour.getBlue() / 255.f,
                        breathingProperty.getValue() ? ColourUtil.getBreathingProgress() * (colour.getAlpha() / 255.0F) : colour.getAlpha() / 255.0F);
            glUniform2f(this.getUniformLocation("texelSize"),
                        1.f / mc.displayWidth,
                        1.f / mc.displayHeight);
            glUniform1f(this.getUniformLocation("radius"), radiusProperty.getValue().floatValue());
        }
    };

    private final GLShader fillShader = new GLShader(VERTEX_SHADER, FILL_SHADER) {
        @Override
        public void setupUniforms() {
            this.setupUniform("texture");
            this.setupUniform("colour");

            glUniform1i(this.getUniformLocation("texture"), 0);
        }
    };

    private Framebuffer framebuffer;

    private final EnumProperty<ColourMode> colourModeProperty = new EnumProperty<>("Colour Mode", ColourMode.TEAMS);
    private final ColourProperty colourProperty = new ColourProperty("Colour", ColourUtil.getClientColour(),
                                                                     () -> this.colourModeProperty.getValue() == ColourMode.STATIC);
    private final ColourProperty enemyColourProperty = new ColourProperty("Enemy Colour", 0xFFFF0000,
                                                                          () -> this.colourModeProperty.getValue() == ColourMode.TEAMS);
    private final ColourProperty friendlyColourProperty = new ColourProperty("Friendly Colour", 0xFF00FF69,
                                                                             () -> this.colourModeProperty.getValue() == ColourMode.TEAMS);
    private final DoubleProperty radiusProperty = new DoubleProperty("Radius", 3, 1, 10, 1);
    private final BooleanProperty chestsProperty = new BooleanProperty("Chests", true);
    private final BooleanProperty breathingProperty = new BooleanProperty("Breathing", true);

    public Glow() {
        super("Glow", Category.RENDER, Category.SubCategory.RENDER_ESP);

        this.register(colourModeProperty, this.colourProperty, this.friendlyColourProperty, this.enemyColourProperty, this.radiusProperty, this.chestsProperty, this.breathingProperty);
    }

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

    @EventLink
    private final Listener<ChestModelRenderEvent> onRenderChestModel = event -> {
        if (this.chestsProperty.getValue()) {
            // Bind our frame buffer
            // False here means it doesn't set the viewport (which isn't required because our framebuffer is the same size as minecraft's)
            this.framebuffer.bindFramebuffer(false);
            // Apply shader
            glUseProgram(this.fillShader.getProgram());
            // Set outline colour to chest colour
            glUniform4f(this.fillShader.getUniformLocation("colour"), 1.0f, 0.6f, 0.f, 1.0f);
            // Draw chest model into frame buffer
            event.draw();
            // Disable shader
            glUseProgram(0);
            // Bind the minecraft frame buffer
            this.mc.getFramebuffer().bindFramebuffer(false);
        }
    };

    private final float[] colourBuffer = new float[4];

    private void fillColourBuffer(final int colour) {
        final int red = colour >> 16 & 0xFF;
        final int green = colour >> 8 & 0xFF;
        final int blue = colour & 0xFF;
        final int alpha = colour >> 24 & 0xFF;

        this.colourBuffer[0] = red / 255.0F;
        this.colourBuffer[1] = green / 255.0F;
        this.colourBuffer[2] = blue / 255.0F;
        this.colourBuffer[3] = alpha / 255.0F;
    }

    @EventLink(0)
    private final Listener<ModelRenderEvent> onModelRender = event -> {
        if (!event.isPre()) {
            // Bind our frame buffer
            // False here means it doesn't set the viewport (which isn't required because our framebuffer is the same size as minecraft's)
            this.framebuffer.bindFramebuffer(false);
            // Apply pixel fill shader
            glUseProgram(this.fillShader.getProgram());
            // Set to desired outline colour for entity
            this.fillColourBuffer(
                this.colourModeProperty.getValue() == ColourMode.STATIC ?
                    this.colourProperty.getValue() :
                    TeamsUtil.TeamsMode.NAME.getComparator().isOnSameTeam(this.mc.thePlayer, event.getEntity()) ?
                        this.friendlyColourProperty.getValue() :
                        this.enemyColourProperty.getValue());
            glUniform4f(this.fillShader.getUniformLocation("colour"), this.colourBuffer[0], this.colourBuffer[1], this.colourBuffer[2], this.colourBuffer[3]);
            // Draw player model & layers (armor & sword n shit)
            event.drawModel();
            // Disable rendering the enchant glint when layers are drawn (to save performance & make it less buggy)
            LayerRenderer.renderEnchantGlint = false;
            event.drawLayers();
            LayerRenderer.renderEnchantGlint = true;
            // Un-apply shader
            glUseProgram(0);
            // Bind the minecraft frame buffer
            this.mc.getFramebuffer().bindFramebuffer(false);
        }
    };

    @EventLink
    private final Listener<RenderGameOverlayEvent> onRenderGameOverlay = event -> {
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
    };

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

    private enum ColourMode {
        STATIC("Static"),
        TEAMS("Teams");

        private final String name;

        ColourMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
