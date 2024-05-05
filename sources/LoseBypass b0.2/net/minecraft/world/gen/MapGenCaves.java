/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

public class MapGenCaves
extends MapGenBase {
    protected void func_180703_a(long p_180703_1_, int p_180703_3_, int p_180703_4_, ChunkPrimer p_180703_5_, double p_180703_6_, double p_180703_8_, double p_180703_10_) {
        this.func_180702_a(p_180703_1_, p_180703_3_, p_180703_4_, p_180703_5_, p_180703_6_, p_180703_8_, p_180703_10_, 1.0f + this.rand.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5);
    }

    /*
     * Exception decompiling
     */
    protected void func_180702_a(long p_180702_1_, int p_180702_3_, int p_180702_4_, ChunkPrimer p_180702_5_, double p_180702_6_, double p_180702_8_, double p_180702_10_, float p_180702_12_, float p_180702_13_, float p_180702_14_, int p_180702_15_, int p_180702_16_, double p_180702_17_) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[UNCONDITIONALDOLOOP]], but top level block is 5[UNCONDITIONALDOLOOP]
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

    protected boolean func_175793_a(IBlockState p_175793_1_, IBlockState p_175793_2_) {
        if (p_175793_1_.getBlock() == Blocks.stone) {
            return true;
        }
        if (p_175793_1_.getBlock() == Blocks.dirt) {
            return true;
        }
        if (p_175793_1_.getBlock() == Blocks.grass) {
            return true;
        }
        if (p_175793_1_.getBlock() == Blocks.hardened_clay) {
            return true;
        }
        if (p_175793_1_.getBlock() == Blocks.stained_hardened_clay) {
            return true;
        }
        if (p_175793_1_.getBlock() == Blocks.sandstone) {
            return true;
        }
        if (p_175793_1_.getBlock() == Blocks.red_sandstone) {
            return true;
        }
        if (p_175793_1_.getBlock() == Blocks.mycelium) {
            return true;
        }
        if (p_175793_1_.getBlock() == Blocks.snow_layer) {
            return true;
        }
        if (p_175793_1_.getBlock() != Blocks.sand) {
            if (p_175793_1_.getBlock() != Blocks.gravel) return false;
        }
        if (p_175793_2_.getBlock().getMaterial() == Material.water) return false;
        return true;
    }

    @Override
    protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn) {
        int i = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);
        if (this.rand.nextInt(7) != 0) {
            i = 0;
        }
        int j = 0;
        while (j < i) {
            double d0 = chunkX * 16 + this.rand.nextInt(16);
            double d1 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            double d2 = chunkZ * 16 + this.rand.nextInt(16);
            int k = 1;
            if (this.rand.nextInt(4) == 0) {
                this.func_180703_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2);
                k += this.rand.nextInt(4);
            }
            for (int l = 0; l < k; ++l) {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0f;
                float f1 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                float f2 = this.rand.nextFloat() * 2.0f + this.rand.nextFloat();
                if (this.rand.nextInt(10) == 0) {
                    f2 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0f + 1.0f;
                }
                this.func_180702_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2, f2, f, f1, 0, 0, 1.0);
            }
            ++j;
        }
    }
}

