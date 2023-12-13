package dev.rise.script.block.impl.variables.impl.number;

import dev.rise.script.block.api.BlockData;
import dev.rise.script.block.impl.variables.VariableBlock;

@BlockData(name = "MotionZ", description = "This variable returns the motion z for the player entity.")
public final class MotionZBlock extends VariableBlock {

    @Override
    public <T> T get() {
        return (T) (Double) mc.thePlayer.motionZ;
    }
}
