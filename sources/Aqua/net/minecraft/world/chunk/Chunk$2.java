package net.minecraft.world.chunk;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;

class Chunk.2
implements Callable<String> {
    final /* synthetic */ BlockPos val$pos;

    Chunk.2(BlockPos blockPos) {
        this.val$pos = blockPos;
    }

    public String call() throws Exception {
        return CrashReportCategory.getCoordinateInfo((BlockPos)this.val$pos);
    }
}
