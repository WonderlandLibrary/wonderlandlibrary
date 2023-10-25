/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.process;

import baritone.api.process.IBaritoneProcess;
import net.minecraft.util.math.BlockPos;

public interface IFarmProcess
extends IBaritoneProcess {
    public void farm(int var1, BlockPos var2);

    default public void farm() {
        this.farm(0, null);
    }

    default public void farm(int range) {
        this.farm(range, null);
    }
}

