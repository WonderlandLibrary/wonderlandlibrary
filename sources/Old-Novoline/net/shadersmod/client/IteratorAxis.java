package net.shadersmod.client;

import net.minecraft.util.BlockPos;
import net.optifine.BlockPosM;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorAxis implements Iterator<BlockPos> {

    private double yDelta;
    private double zDelta;
    private int xStart;
    private int xEnd;
    private double yStart;
    private double yEnd;
    private double zStart;
    private double zEnd;
    private int xNext;
    private double yNext;
    private double zNext;
    private BlockPosM pos = new BlockPosM(0, 0, 0);
    private boolean hasNext;

    public IteratorAxis(BlockPos posStart, BlockPos posEnd, double yDelta, double zDelta) {
        this.yDelta = yDelta;
        this.zDelta = zDelta;
        this.xStart = posStart.getX();
        this.xEnd = posEnd.getX();
        this.yStart = posStart.getY();
        this.yEnd = (double) posEnd.getY() - 0.5D;
        this.zStart = posStart.getZ();
        this.zEnd = (double) posEnd.getZ() - 0.5D;
        this.xNext = this.xStart;
        this.yNext = this.yStart;
        this.zNext = this.zStart;
        this.hasNext = this.xNext < this.xEnd && this.yNext < this.yEnd && this.zNext < this.zEnd;
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    public BlockPos next() {
        if (!this.hasNext) {
            throw new NoSuchElementException();
        } else {
            this.pos.setXyz(this.xNext, this.yNext, this.zNext);
            this.nextPos();
            this.hasNext = this.xNext < this.xEnd && this.yNext < this.yEnd && this.zNext < this.zEnd;
            return this.pos;
        }
    }

    private void nextPos() {
        ++this.zNext;

        if (this.zNext >= this.zEnd) {
            this.zNext = this.zStart;
            ++this.yNext;

            if (this.yNext >= this.yEnd) {
                this.yNext = this.yStart;
                this.yStart += this.yDelta;
                this.yEnd += this.yDelta;
                this.yNext = this.yStart;
                this.zStart += this.zDelta;
                this.zEnd += this.zDelta;
                this.zNext = this.zStart;
                ++this.xNext;
            }
        }
    }

    public void remove() {
        throw new RuntimeException("Not implemented");
    }

}
