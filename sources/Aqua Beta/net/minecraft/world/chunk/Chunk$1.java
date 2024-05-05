package net.minecraft.world.chunk;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;

class Chunk.1
implements Callable<String> {
    final /* synthetic */ int val$x;
    final /* synthetic */ int val$y;
    final /* synthetic */ int val$z;

    Chunk.1(int n, int n2, int n3) {
        this.val$x = n;
        this.val$y = n2;
        this.val$z = n3;
    }

    public String call() throws Exception {
        return CrashReportCategory.getCoordinateInfo((BlockPos)new BlockPos(Chunk.this.xPosition * 16 + this.val$x, this.val$y, Chunk.this.zPosition * 16 + this.val$z));
    }
}
