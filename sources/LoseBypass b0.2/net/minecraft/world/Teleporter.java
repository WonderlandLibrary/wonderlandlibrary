/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;

public class Teleporter {
    private final WorldServer worldServerInstance;
    private final Random random;
    private final LongHashMap<PortalPosition> destinationCoordinateCache = new LongHashMap();
    private final List<Long> destinationCoordinateKeys = Lists.newArrayList();

    public Teleporter(WorldServer worldIn) {
        this.worldServerInstance = worldIn;
        this.random = new Random(worldIn.getSeed());
    }

    public void placeInPortal(Entity entityIn, float rotationYaw) {
        if (this.worldServerInstance.provider.getDimensionId() != 1) {
            if (this.placeInExistingPortal(entityIn, rotationYaw)) return;
            this.makePortal(entityIn);
            this.placeInExistingPortal(entityIn, rotationYaw);
            return;
        }
        int i = MathHelper.floor_double(entityIn.posX);
        int j = MathHelper.floor_double(entityIn.posY) - 1;
        int k = MathHelper.floor_double(entityIn.posZ);
        int l = 1;
        int i1 = 0;
        int j1 = -2;
        block0: while (true) {
            if (j1 > 2) {
                entityIn.setLocationAndAngles(i, j, k, entityIn.rotationYaw, 0.0f);
                entityIn.motionZ = 0.0;
                entityIn.motionY = 0.0;
                entityIn.motionX = 0.0;
                return;
            }
            int k1 = -2;
            while (true) {
                if (k1 <= 2) {
                } else {
                    ++j1;
                    continue block0;
                }
                for (int l1 = -1; l1 < 3; ++l1) {
                    int i2 = i + k1 * l + j1 * i1;
                    int j2 = j + l1;
                    int k2 = k + k1 * i1 - j1 * l;
                    boolean flag = l1 < 0;
                    this.worldServerInstance.setBlockState(new BlockPos(i2, j2, k2), flag ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
                }
                ++k1;
            }
            break;
        }
    }

    /*
     * Unable to fully structure code
     */
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
        block21: {
            i = 128;
            d0 = -1.0;
            j = MathHelper.floor_double(entityIn.posX);
            k = MathHelper.floor_double(entityIn.posZ);
            flag = true;
            blockpos = BlockPos.ORIGIN;
            l = ChunkCoordIntPair.chunkXZ2Int(j, k);
            if (!this.destinationCoordinateCache.containsItem(l)) break block21;
            teleporter$portalposition = this.destinationCoordinateCache.getValueByKey(l);
            d0 = 0.0;
            blockpos = teleporter$portalposition;
            teleporter$portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            flag = false;
            ** GOTO lbl20
        }
        blockpos3 = new BlockPos(entityIn);
        i1 = -128;
        block0: while (true) {
            block22: {
                if (i1 <= 128) break block22;
lbl20:
                // 2 sources

                if (!(d0 >= 0.0)) return false;
                if (flag) {
                    this.destinationCoordinateCache.add(l, new PortalPosition(blockpos, this.worldServerInstance.getTotalWorldTime()));
                    this.destinationCoordinateKeys.add(l);
                }
                d5 = (double)blockpos.getX() + 0.5;
                d6 = (double)blockpos.getY() + 0.5;
                d7 = (double)blockpos.getZ() + 0.5;
                blockpattern$patternhelper = Blocks.portal.func_181089_f(this.worldServerInstance, blockpos);
                flag1 = blockpattern$patternhelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
                d2 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? (double)blockpattern$patternhelper.func_181117_a().getZ() : (double)blockpattern$patternhelper.func_181117_a().getX();
                d6 = (double)(blockpattern$patternhelper.func_181117_a().getY() + 1) - entityIn.func_181014_aG().yCoord * (double)blockpattern$patternhelper.func_181119_e();
                if (flag1) {
                    d2 += 1.0;
                }
                if (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) {
                    d7 = d2 + (1.0 - entityIn.func_181014_aG().xCoord) * (double)blockpattern$patternhelper.func_181118_d() * (double)blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
                } else {
                    d5 = d2 + (1.0 - entityIn.func_181014_aG().xCoord) * (double)blockpattern$patternhelper.func_181118_d() * (double)blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
                }
                f = 0.0f;
                f1 = 0.0f;
                f2 = 0.0f;
                f3 = 0.0f;
                if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH()) {
                    f = 1.0f;
                    f1 = 1.0f;
                } else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH().getOpposite()) {
                    f = -1.0f;
                    f1 = -1.0f;
                } else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH().rotateY()) {
                    f2 = 1.0f;
                    f3 = -1.0f;
                } else {
                    f2 = -1.0f;
                    f3 = 1.0f;
                }
                d3 = entityIn.motionX;
                d4 = entityIn.motionZ;
                entityIn.motionX = d3 * (double)f + d4 * (double)f3;
                entityIn.motionZ = d3 * (double)f2 + d4 * (double)f1;
                entityIn.rotationYaw = rotationYaw - (float)(entityIn.func_181012_aH().getOpposite().getHorizontalIndex() * 90) + (float)(blockpattern$patternhelper.getFinger().getHorizontalIndex() * 90);
                entityIn.setLocationAndAngles(d5, d6, d7, entityIn.rotationYaw, entityIn.rotationPitch);
                return true;
            }
            j1 = -128;
            while (true) {
                if (j1 <= 128) {
                    blockpos1 = blockpos3.add(i1, this.worldServerInstance.getActualHeight() - 1 - blockpos3.getY(), j1);
                } else {
                    ++i1;
                    continue block0;
                }
                while (blockpos1.getY() >= 0) {
                    blockpos2 = blockpos1.down();
                    if (this.worldServerInstance.getBlockState(blockpos1).getBlock() == Blocks.portal) {
                        while (this.worldServerInstance.getBlockState(blockpos2 = blockpos1.down()).getBlock() == Blocks.portal) {
                            blockpos1 = blockpos2;
                        }
                        d1 = blockpos1.distanceSq(blockpos3);
                        if (d0 < 0.0 || d1 < d0) {
                            d0 = d1;
                            blockpos = blockpos1;
                        }
                    }
                    blockpos1 = blockpos2;
                }
                ++j1;
            }
            break;
        }
    }

    /*
     * Exception decompiling
     */
    public boolean makePortal(Entity p_85188_1_) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[UNCONDITIONALDOLOOP]], but top level block is 2[UNCONDITIONALDOLOOP]
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

    public void removeStalePortalLocations(long worldTime) {
        if (worldTime % 100L != 0L) return;
        Iterator<Long> iterator = this.destinationCoordinateKeys.iterator();
        long i = worldTime - 300L;
        while (iterator.hasNext()) {
            Long olong = iterator.next();
            PortalPosition teleporter$portalposition = this.destinationCoordinateCache.getValueByKey(olong);
            if (teleporter$portalposition != null && teleporter$portalposition.lastUpdateTime >= i) continue;
            iterator.remove();
            this.destinationCoordinateCache.remove(olong);
        }
    }

    public class PortalPosition
    extends BlockPos {
        public long lastUpdateTime;

        public PortalPosition(BlockPos pos, long lastUpdate) {
            super(pos.getX(), pos.getY(), pos.getZ());
            this.lastUpdateTime = lastUpdate;
        }
    }
}

