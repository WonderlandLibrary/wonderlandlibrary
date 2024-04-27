/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.shader.Framebuffer
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils.render.tenacity;

import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.tenacity.GLShader;
import net.ccbluex.liquidbounce.utils.render.tenacity.RenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL20;

public class BloomUtil {
    private static final String OUTLINE_SHADER = "#version 120\n\nuniform sampler2D texture;\nuniform vec2 texelSize;\n\nuniform vec4 colour;\nuniform float radius;\n\nvoid main() {\n    float a = 0.0;\n    vec3 rgb = colour.rgb;\n    float closest = 1.0;\n    for (float x = -radius; x <= radius; x++) {\n        for (float y = -radius; y <= radius; y++) {\n            vec2 st = gl_TexCoord[0].st + vec2(x, y) * texelSize;\n            vec4 smpl = texture2D(texture, st);\n            float dist = distance(st, gl_TexCoord[0].st);\n            if (smpl.a > 0.0 && dist < closest) {               rgb = smpl.rgb;\n               closest = dist;\n            }\n            a += smpl.a*smpl.a;\n        }\n    }\n    vec4 smpl = texture2D(texture, gl_TexCoord[0].st);\n    gl_FragColor = vec4(rgb, a * colour.a / (4.0 * radius * radius)) * (smpl.a > 0.0 ? 0.0 : 1.0);\n}\n";
    private static final String VERTEX_SHADER = "#version 120 \n\nvoid main() {\n    gl_TexCoord[0] = gl_MultiTexCoord0;\n    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n}";
    private static final GLShader shader = new GLShader("#version 120 \n\nvoid main() {\n    gl_TexCoord[0] = gl_MultiTexCoord0;\n    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n}", "#version 120\n\nuniform sampler2D texture;\nuniform vec2 texelSize;\n\nuniform vec4 colour;\nuniform float radius;\n\nvoid main() {\n    float a = 0.0;\n    vec3 rgb = colour.rgb;\n    float closest = 1.0;\n    for (float x = -radius; x <= radius; x++) {\n        for (float y = -radius; y <= radius; y++) {\n            vec2 st = gl_TexCoord[0].st + vec2(x, y) * texelSize;\n            vec4 smpl = texture2D(texture, st);\n            float dist = distance(st, gl_TexCoord[0].st);\n            if (smpl.a > 0.0 && dist < closest) {               rgb = smpl.rgb;\n               closest = dist;\n            }\n            a += smpl.a*smpl.a;\n        }\n    }\n    vec4 smpl = texture2D(texture, gl_TexCoord[0].st);\n    gl_FragColor = vec4(rgb, a * colour.a / (4.0 * radius * radius)) * (smpl.a > 0.0 ? 0.0 : 1.0);\n}\n"){

        @Override
        public void setupUniforms() {
            this.setupUniform("texture");
            this.setupUniform("texelSize");
            this.setupUniform("colour");
            this.setupUniform("radius");
            GL20.glUniform1i((int)this.getUniformLocation("texture"), (int)0);
        }

        @Override
        public void updateUniforms() {
            GL20.glUniform4f((int)this.getUniformLocation("colour"), (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL20.glUniform2f((int)this.getUniformLocation("texelSize"), (float)(1.0f / (float)Minecraft.func_71410_x().field_71443_c), (float)(1.0f / (float)Minecraft.func_71410_x().field_71440_d));
            GL20.glUniform1f((int)this.getUniformLocation("radius"), (float)5.0f);
        }
    };
    public static boolean disableBloom;
    private static Framebuffer framebuffer;
    private static final List<RenderCallback> renders;

    private BloomUtil() {
    }

    public static void bloom(RenderCallback render) {
        if (disableBloom) {
            return;
        }
        renders.add(render);
    }

    public static void drawAndBloom(RenderCallback render) {
        render.render();
        if (disableBloom) {
            return;
        }
        renders.add(render);
    }

    public static void onRenderGameOverlay(ScaledResolution scaledResolution, Framebuffer mcFramebuffer) {
        if (framebuffer == null) {
            return;
        }
        framebuffer.func_147610_a(false);
        for (RenderCallback callback : renders) {
            callback.render();
        }
        renders.clear();
        mcFramebuffer.func_147610_a(false);
        shader.use();
        RenderUtils.glDrawFramebuffer(BloomUtil.framebuffer.field_147617_g, scaledResolution.func_78326_a(), scaledResolution.func_78328_b());
        GL20.glUseProgram((int)0);
        framebuffer.func_147614_f();
        mcFramebuffer.func_147610_a(false);
    }

    public static void onFrameBufferResize(int width, int height) {
        if (framebuffer != null) {
            framebuffer.func_147608_a();
        }
        framebuffer = new Framebuffer(width, height, false);
    }

    static {
        renders = new ArrayList<RenderCallback>();
    }
}

