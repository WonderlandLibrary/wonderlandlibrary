package net.minecraft.crash;

import java.util.concurrent.Callable;
import net.minecraft.block.Block;

static final class CrashReportCategory.1
implements Callable<String> {
    final /* synthetic */ int val$i;
    final /* synthetic */ Block val$blockIn;

    CrashReportCategory.1(int n, Block block) {
        this.val$i = n;
        this.val$blockIn = block;
    }

    public String call() throws Exception {
        try {
            return String.format((String)"ID #%d (%s // %s)", (Object[])new Object[]{this.val$i, this.val$blockIn.getUnlocalizedName(), this.val$blockIn.getClass().getCanonicalName()});
        }
        catch (Throwable var2) {
            return "ID #" + this.val$i;
        }
    }
}
