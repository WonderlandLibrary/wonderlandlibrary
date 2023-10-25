package net.minecraft.crash;

import java.util.concurrent.Callable;
import net.minecraft.block.state.IBlockState;

static final class CrashReportCategory.4
implements Callable<String> {
    final /* synthetic */ IBlockState val$state;

    CrashReportCategory.4(IBlockState iBlockState) {
        this.val$state = iBlockState;
    }

    public String call() throws Exception {
        return this.val$state.toString();
    }
}
