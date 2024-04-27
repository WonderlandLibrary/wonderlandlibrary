/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.shader.Framebuffer
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils;

import ad.MathUtils;
import java.nio.FloatBuffer;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.tenacity.ShaderUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.normal.Utils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class BloomUtil
implements Utils {
    public static ShaderUtil gaussianBloom;
    public static Framebuffer framebuffer;

    public static void renderBlur(int sourceTexture, int radius, int offset) {
        framebuffer = RenderUtils.createFrameBuffer(framebuffer);
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a((int)516, (float)0.0f);
        GlStateManager.func_179147_l();
        int n = 0;
        FloatBuffer weightBuffer = BufferUtils.createFloatBuffer((int)256);
        for (int i = 0; i <= radius; ++i) {
            weightBuffer.put(MathUtils.calculateGaussianValue(i, radius));
        }
        weightBuffer.rewind();
        RenderUtils.setAlphaLimit(0.0f);
        framebuffer.func_147614_f();
        framebuffer.func_147610_a(true);
        gaussianBloom.init();
        BloomUtil.setupUniforms(radius, offset, 0, weightBuffer);
        RenderUtils.bindTexture(sourceTexture);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();
        framebuffer.func_147609_e();
        mc.getFramebuffer().bindFramebuffer(true);
        gaussianBloom.init();
        BloomUtil.setupUniforms(radius, 0, offset, weightBuffer);
        GL13.glActiveTexture((int)34000);
        RenderUtils.bindTexture(sourceTexture);
        GL13.glActiveTexture((int)33984);
        RenderUtils.bindTexture(BloomUtil.framebuffer.field_147617_g);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();
        GlStateManager.func_179092_a((int)516, (float)0.1f);
        GlStateManager.func_179141_d();
        GlStateManager.func_179144_i((int)0);
    }

    public static void setupUniforms(int radius, int directionX, int directionY, FloatBuffer weights) {
        ShaderUtil shaderUtil = gaussianBloom;
        String string = "inTexture";
        int[] nArray = new int[1];
        nArray.setUniformi((String)nArray, (int[])0);
        ShaderUtil shaderUtil2 = gaussianBloom;
        String string2 = "textureToCheck";
        int[] nArray2 = new int[1];
        nArray2.setUniformi((String)nArray2, (int[])0);
        gaussianBloom.setUniformf("radius", radius);
        gaussianBloom.setUniformf("texelSize", 1.0f / (float)MinecraftInstance.mc2.field_71443_c, 1.0f / (float)MinecraftInstance.mc2.field_71440_d);
        gaussianBloom.setUniformf("direction", directionX, directionY);
        GL20.glUniform1((int)gaussianBloom.getUniform("weights"), (FloatBuffer)weights);
    }

    /*
     * Exception decompiling
     */
    static {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl9 : INVOKESPECIAL - null : Stack underflow
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

