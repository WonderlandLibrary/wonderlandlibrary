/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.ReportedException
 *  net.minecraft.world.EnumSkyBlock
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldType
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraft.world.chunk.storage.ExtendedBlockStorage
 *  net.minecraft.world.gen.ChunkProviderDebug
 */
package net.dev.important.injection.forge.mixins.optimize;

import net.dev.important.injection.access.IChunk;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderDebug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={Chunk.class})
public abstract class MixinChunk
implements IChunk {
    @Shadow
    @Final
    private World field_76637_e;
    @Shadow
    @Final
    private ExtendedBlockStorage[] field_76652_q;
    @Shadow
    @Final
    private int[] field_76634_f;
    @Shadow
    private boolean field_76643_l;

    @Shadow
    public abstract void func_76603_b();

    @Override
    public int getLightFor(EnumSkyBlock enumSkyBlock, int n, int n2, int n3) {
        int n4 = n & 0xF;
        int n5 = n3 & 0xF;
        ExtendedBlockStorage extendedBlockStorage = this.field_76652_q[n2 >> 4];
        return extendedBlockStorage == null ? (this.canSeeSky(n, n2, n3) ? enumSkyBlock.field_77198_c : 0) : (enumSkyBlock == EnumSkyBlock.SKY ? (this.field_76637_e.field_73011_w.func_177495_o() ? 0 : extendedBlockStorage.func_76670_c(n4, n2 & 0xF, n5)) : (enumSkyBlock == EnumSkyBlock.BLOCK ? extendedBlockStorage.func_76674_d(n4, n2 & 0xF, n5) : enumSkyBlock.field_77198_c));
    }

    @Override
    public int getLightSubtracted(int n, int n2, int n3, int n4) {
        int n5 = n & 0xF;
        int n6 = n3 & 0xF;
        ExtendedBlockStorage extendedBlockStorage = this.field_76652_q[n2 >> 4];
        if (extendedBlockStorage == null) {
            return !this.field_76637_e.field_73011_w.func_177495_o() && n4 < EnumSkyBlock.SKY.field_77198_c ? EnumSkyBlock.SKY.field_77198_c - n4 : 0;
        }
        int n7 = this.field_76637_e.field_73011_w.func_177495_o() ? 0 : extendedBlockStorage.func_76670_c(n5, n2 & 0xF, n6);
        int n8 = extendedBlockStorage.func_76674_d(n5, n2 & 0xF, n6);
        if (n8 > (n7 -= n4)) {
            n7 = n8;
        }
        return n7;
    }

    @Override
    public boolean canSeeSky(int n, int n2, int n3) {
        int n4 = n3 & 0xF;
        int n5 = n & 0xF;
        return n2 >= this.field_76634_f[n4 << 4 | n5];
    }

    @Override
    public void setLightFor(EnumSkyBlock enumSkyBlock, int n, int n2, int n3, int n4) {
        int n5 = n & 0xF;
        int n6 = n3 & 0xF;
        ExtendedBlockStorage extendedBlockStorage = this.field_76652_q[n2 >> 4];
        if (extendedBlockStorage == null) {
            ExtendedBlockStorage extendedBlockStorage2;
            this.field_76652_q[n2 >> 4] = extendedBlockStorage2 = new ExtendedBlockStorage(n2 >> 4 << 4, !this.field_76637_e.field_73011_w.func_177495_o());
            extendedBlockStorage = extendedBlockStorage2;
            this.func_76603_b();
        }
        this.field_76643_l = true;
        if (enumSkyBlock == EnumSkyBlock.SKY) {
            if (!this.field_76637_e.field_73011_w.func_177495_o()) {
                extendedBlockStorage.func_76657_c(n5, n2 & 0xF, n6, n4);
            }
        } else if (enumSkyBlock == EnumSkyBlock.BLOCK) {
            extendedBlockStorage.func_76677_d(n5, n2 & 0xF, n6, n4);
        }
    }

    @Override
    public IBlockState getBlockState(int n, int n2, int n3) {
        if (this.field_76637_e.func_175624_G() == WorldType.field_180272_g) {
            IBlockState iBlockState = null;
            if (n2 == 60) {
                iBlockState = Blocks.field_180401_cv.func_176223_P();
            }
            if (n2 == 70) {
                iBlockState = ChunkProviderDebug.func_177461_b((int)n, (int)n3);
            }
            return iBlockState == null ? Blocks.field_150350_a.func_176223_P() : iBlockState;
        }
        try {
            ExtendedBlockStorage extendedBlockStorage;
            if (n2 >= 0 && n2 >> 4 < this.field_76652_q.length && (extendedBlockStorage = this.field_76652_q[n2 >> 4]) != null) {
                int n4 = n & 0xF;
                int n5 = n2 & 0xF;
                int n6 = n3 & 0xF;
                return extendedBlockStorage.func_177485_a(n4, n5, n6);
            }
            return Blocks.field_150350_a.func_176223_P();
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.func_85055_a((Throwable)throwable, (String)"Getting block state");
            CrashReportCategory crashReportCategory = crashReport.func_85058_a("Block being got");
            crashReportCategory.func_71500_a("Location", () -> CrashReportCategory.func_180522_a((BlockPos)new BlockPos(n, n2, n3)));
            throw new ReportedException(crashReport);
        }
    }
}

