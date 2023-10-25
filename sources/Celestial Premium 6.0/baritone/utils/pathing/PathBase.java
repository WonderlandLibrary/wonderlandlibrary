/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.pathing;

import baritone.Baritone;
import baritone.api.BaritoneAPI;
import baritone.api.pathing.calc.IPath;
import baritone.api.pathing.goals.Goal;
import baritone.pathing.path.CutoffPath;
import baritone.utils.BlockStateInterface;
import net.minecraft.util.math.BlockPos;

public abstract class PathBase
implements IPath {
    @Override
    public PathBase cutoffAtLoadedChunks(Object bsi0) {
        if (!((Boolean)Baritone.settings().cutoffAtLoadBoundary.value).booleanValue()) {
            return this;
        }
        BlockStateInterface bsi = (BlockStateInterface)bsi0;
        for (int i = 0; i < this.positions().size(); ++i) {
            BlockPos pos = this.positions().get(i);
            if (bsi.worldContainsLoadedChunk(pos.getX(), pos.getZ())) continue;
            return new CutoffPath(this, i);
        }
        return this;
    }

    @Override
    public PathBase staticCutoff(Goal destination) {
        int min = (Integer)BaritoneAPI.getSettings().pathCutoffMinimumLength.value;
        if (this.length() < min) {
            return this;
        }
        if (destination == null || destination.isInGoal(this.getDest())) {
            return this;
        }
        double factor = (Double)BaritoneAPI.getSettings().pathCutoffFactor.value;
        int newLength = (int)((double)(this.length() - min) * factor) + min - 1;
        return new CutoffPath(this, newLength);
    }
}

