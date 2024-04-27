/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.shader.Framebuffer
 */
package net.ccbluex.liquidbounce.utils.render.blur;

import java.util.List;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.blur.ShaderUtil;
import net.minecraft.client.shader.Framebuffer;

public class KawaseBlur
extends MinecraftInstance {
    private static final List<Framebuffer> framebufferList;
    public static ShaderUtil kawaseDown;
    public static ShaderUtil kawaseUp;
    public static Framebuffer framebuffer;
    private static int currentIterations;

    public static void setupUniforms(float offset) {
        kawaseDown.setUniformf("offset", offset, offset);
        kawaseUp.setUniformf("offset", offset, offset);
    }

    private static void initFramebuffers(float iterations) {
        for (Framebuffer framebuffer : framebufferList) {
            framebuffer.func_147608_a();
        }
        framebufferList.clear();
        framebufferList.add(RenderUtils.createFrameBuffer(framebuffer));
        int i = 1;
        while ((float)i <= iterations) {
            Framebuffer framebuffer;
            framebuffer = new Framebuffer(KawaseBlur.minecraft.field_71443_c, KawaseBlur.minecraft.field_71440_d, false);
            framebufferList.add(RenderUtils.createFrameBuffer(framebuffer));
            ++i;
        }
    }

    public static void renderBlur(int iterations, int offset) {
        int i;
        if (currentIterations != iterations) {
            KawaseBlur.initFramebuffers(iterations);
            currentIterations = iterations;
        }
        KawaseBlur.renderFBO(framebufferList.get(1), KawaseBlur.minecraft.func_147110_a().field_147617_g, kawaseDown, offset);
        for (i = 1; i < iterations; ++i) {
            KawaseBlur.renderFBO(framebufferList.get(i + 1), KawaseBlur.framebufferList.get((int)i).field_147617_g, kawaseDown, offset);
        }
        for (i = iterations; i > 1; --i) {
            KawaseBlur.renderFBO(framebufferList.get(i - 1), KawaseBlur.framebufferList.get((int)i).field_147617_g, kawaseUp, offset);
        }
        minecraft.func_147110_a().func_147610_a(true);
        RenderUtils.bindTexture(KawaseBlur.framebufferList.get((int)1).field_147617_g);
        kawaseUp.init();
        kawaseUp.setUniformf("offset", offset, offset);
        kawaseUp.setUniformf("halfpixel", 0.5f / (float)KawaseBlur.minecraft.field_71443_c, 0.5f / (float)KawaseBlur.minecraft.field_71440_d);
        ShaderUtil shaderUtil = kawaseUp;
        String string = "inTexture";
        int[] nArray = new int[1];
        nArray.setUniformi((String)nArray, (int[])0);
        ShaderUtil.drawQuads();
        kawaseUp.unload();
    }

    private static void renderFBO(Framebuffer framebuffer, int framebufferTexture, ShaderUtil shader, float offset) {
        framebuffer.func_147614_f();
        framebuffer.func_147610_a(true);
        shader.init();
        RenderUtils.bindTexture(framebufferTexture);
        shader.setUniformf("offset", offset, offset);
        ShaderUtil shaderUtil = shader;
        String string = "inTexture";
        int[] nArray = new int[1];
        nArray.setUniformi((String)nArray, (int[])0);
        shader.setUniformf("halfpixel", 0.5f / (float)KawaseBlur.minecraft.field_71443_c, 0.5f / (float)KawaseBlur.minecraft.field_71440_d);
        ShaderUtil.drawQuads();
        shader.unload();
        framebuffer.func_147609_e();
    }

    /*
     * Exception decompiling
     */
    static {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl18 : INVOKESPECIAL - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }
}

