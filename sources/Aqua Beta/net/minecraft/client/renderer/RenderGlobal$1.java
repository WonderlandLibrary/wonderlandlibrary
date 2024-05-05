package net.minecraft.client.renderer;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReportCategory;

class RenderGlobal.1
implements Callable<String> {
    final /* synthetic */ double val$xCoord;
    final /* synthetic */ double val$yCoord;
    final /* synthetic */ double val$zCoord;

    RenderGlobal.1(double d, double d2, double d3) {
        this.val$xCoord = d;
        this.val$yCoord = d2;
        this.val$zCoord = d3;
    }

    public String call() throws Exception {
        return CrashReportCategory.getCoordinateInfo((double)this.val$xCoord, (double)this.val$yCoord, (double)this.val$zCoord);
    }
}
