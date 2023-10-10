/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

public class MapGenRavine
extends MapGenBase {
    private float[] field_75046_d = new float[1024];

    /*
     * Exception decompiling
     */
    protected void func_180707_a(long p_180707_1_, int p_180707_3_, int p_180707_4_, ChunkPrimer p_180707_5_, double p_180707_6_, double p_180707_8_, double p_180707_10_, float p_180707_12_, float p_180707_13_, float p_180707_14_, int p_180707_15_, int p_180707_16_, double p_180707_17_) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[UNCONDITIONALDOLOOP]], but top level block is 6[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
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
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:306)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:159)
         *     at java.lang.Thread.run(Thread.java:748)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn) {
        if (this.rand.nextInt(50) != 0) return;
        double d0 = chunkX * 16 + this.rand.nextInt(16);
        double d1 = this.rand.nextInt(this.rand.nextInt(40) + 8) + 20;
        double d2 = chunkZ * 16 + this.rand.nextInt(16);
        int i = 1;
        int j = 0;
        while (j < i) {
            float f = this.rand.nextFloat() * (float)Math.PI * 2.0f;
            float f1 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
            float f2 = (this.rand.nextFloat() * 2.0f + this.rand.nextFloat()) * 2.0f;
            this.func_180707_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2, f2, f, f1, 0, 0, 3.0);
            ++j;
        }
    }
}

