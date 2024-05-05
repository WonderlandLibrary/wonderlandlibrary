package net.minecraft.crash;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;

/*
 * Exception performing whole class analysis ignored.
 */
static final class CrashReportCategory.3
implements Callable<String> {
    final /* synthetic */ BlockPos val$pos;

    CrashReportCategory.3(BlockPos blockPos) {
        this.val$pos = blockPos;
    }

    public String call() throws Exception {
        return CrashReportCategory.getCoordinateInfo((BlockPos)this.val$pos);
    }
}
