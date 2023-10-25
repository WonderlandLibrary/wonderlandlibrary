package net.minecraft.crash;

import java.util.concurrent.Callable;

static final class CrashReportCategory.2
implements Callable<String> {
    final /* synthetic */ int val$blockData;

    CrashReportCategory.2(int n) {
        this.val$blockData = n;
    }

    public String call() throws Exception {
        if (this.val$blockData < 0) {
            return "Unknown? (Got " + this.val$blockData + ")";
        }
        String s = String.format((String)"%4s", (Object[])new Object[]{Integer.toBinaryString((int)this.val$blockData)}).replace((CharSequence)" ", (CharSequence)"0");
        return String.format((String)"%1$d / 0x%1$X / 0b%2$s", (Object[])new Object[]{this.val$blockData, s});
    }
}
