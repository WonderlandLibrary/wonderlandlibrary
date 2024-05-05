/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class EntityAIMoveToBlock
extends EntityAIBase {
    private final EntityCreature theEntity;
    private final double movementSpeed;
    protected int runDelay;
    private int timeoutCounter;
    private int field_179490_f;
    protected BlockPos destinationBlock = BlockPos.ORIGIN;
    private boolean isAboveDestination;
    private int searchLength;

    public EntityAIMoveToBlock(EntityCreature creature, double speedIn, int length) {
        this.theEntity = creature;
        this.movementSpeed = speedIn;
        this.searchLength = length;
        this.setMutexBits(5);
    }

    @Override
    public boolean shouldExecute() {
        if (this.runDelay > 0) {
            --this.runDelay;
            return false;
        }
        this.runDelay = 200 + this.theEntity.getRNG().nextInt(200);
        return this.searchForDestination();
    }

    @Override
    public boolean continueExecuting() {
        if (this.timeoutCounter < -this.field_179490_f) return false;
        if (this.timeoutCounter > 1200) return false;
        if (!this.shouldMoveTo(this.theEntity.worldObj, this.destinationBlock)) return false;
        return true;
    }

    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().tryMoveToXYZ((double)this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, (double)this.destinationBlock.getZ() + 0.5, this.movementSpeed);
        this.timeoutCounter = 0;
        this.field_179490_f = this.theEntity.getRNG().nextInt(this.theEntity.getRNG().nextInt(1200) + 1200) + 1200;
    }

    @Override
    public void resetTask() {
    }

    @Override
    public void updateTask() {
        if (this.theEntity.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;
            if (this.timeoutCounter % 40 != 0) return;
            this.theEntity.getNavigator().tryMoveToXYZ((double)this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, (double)this.destinationBlock.getZ() + 0.5, this.movementSpeed);
            return;
        }
        this.isAboveDestination = true;
        --this.timeoutCounter;
    }

    protected boolean getIsAboveDestination() {
        return this.isAboveDestination;
    }

    /*
     * Exception decompiling
     */
    private boolean searchForDestination() {
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

    protected abstract boolean shouldMoveTo(World var1, BlockPos var2);
}

