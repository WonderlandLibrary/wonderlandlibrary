/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.shader.Framebuffer
 */
package net.ccbluex.liquidbounce.utils.render.blur;

import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.blur.ShaderUtil;
import net.minecraft.client.shader.Framebuffer;

public class KawaseBlur
extends MinecraftInstance {
    public static ShaderUtil kawaseDown;
    public static ShaderUtil kawaseUp;
    private static final List framebufferList;
    private static int currentIterations;
    public static Framebuffer framebuffer;

    private static void initFramebuffers(float f) {
        for (Framebuffer framebuffer : framebufferList) {
            framebuffer.func_147608_a();
        }
        framebufferList.clear();
        framebufferList.add(RenderUtils.createFrameBuffer(framebuffer));
        int n = 1;
        while ((float)n <= f) {
            Framebuffer framebuffer;
            framebuffer = new Framebuffer(KawaseBlur.minecraft.field_71443_c, KawaseBlur.minecraft.field_71440_d, false);
            framebufferList.add(RenderUtils.createFrameBuffer(framebuffer));
            ++n;
        }
    }

    private static void renderFBO(Framebuffer framebuffer, int n, ShaderUtil shaderUtil, float f) {
        framebuffer.func_147614_f();
        framebuffer.func_147610_a(true);
        shaderUtil.init();
        RenderUtils.bindTexture(n);
        shaderUtil.setUniformf("offset", f, f);
        shaderUtil.setUniformi("inTexture", 0);
        shaderUtil.setUniformf("halfpixel", 0.5f / (float)KawaseBlur.minecraft.field_71443_c, 0.5f / (float)KawaseBlur.minecraft.field_71440_d);
        ShaderUtil.drawQuads();
        shaderUtil.unload();
        framebuffer.func_147609_e();
    }

    public static void renderBlur(int n, int n2) {
        int n3;
        if (currentIterations != n) {
            KawaseBlur.initFramebuffers(n);
            currentIterations = n;
        }
        KawaseBlur.renderFBO((Framebuffer)framebufferList.get(1), KawaseBlur.minecraft.func_147110_a().field_147617_g, kawaseDown, n2);
        for (n3 = 1; n3 < n; ++n3) {
            KawaseBlur.renderFBO((Framebuffer)framebufferList.get(n3 + 1), ((Framebuffer)KawaseBlur.framebufferList.get((int)n3)).field_147617_g, kawaseDown, n2);
        }
        for (n3 = n; n3 > 1; --n3) {
            KawaseBlur.renderFBO((Framebuffer)framebufferList.get(n3 - 1), ((Framebuffer)KawaseBlur.framebufferList.get((int)n3)).field_147617_g, kawaseUp, n2);
        }
        minecraft.func_147110_a().func_147610_a(true);
        RenderUtils.bindTexture(((Framebuffer)KawaseBlur.framebufferList.get((int)1)).field_147617_g);
        kawaseUp.init();
        kawaseUp.setUniformf("offset", n2, n2);
        kawaseUp.setUniformf("halfpixel", 0.5f / (float)KawaseBlur.minecraft.field_71443_c, 0.5f / (float)KawaseBlur.minecraft.field_71440_d);
        kawaseUp.setUniformi("inTexture", 0);
        ShaderUtil.drawQuads();
        kawaseUp.unload();
    }

    public static void setupUniforms(float f) {
        kawaseDown.setUniformf("offset", f, f);
        kawaseUp.setUniformf("offset", f, f);
    }

    static {
        framebufferList = new ArrayList();
        kawaseDown = new ShaderUtil("More/shader/fragment/kawasedown.frag");
        kawaseUp = new ShaderUtil("More/shader/fragment/kawaseup.frag");
        framebuffer = new Framebuffer(1, 1, false);
    }
}

