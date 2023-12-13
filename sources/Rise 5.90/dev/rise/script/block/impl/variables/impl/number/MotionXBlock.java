package dev.rise.script.block.impl.variables.impl.number;

import dev.rise.script.block.api.BlockData;
import dev.rise.script.block.impl.variables.VariableBlock;

@BlockData(name = "MotionX", description = "This variable returns the motion x for the player entity.")
public final class MotionXBlock extends VariableBlock {

    @Override
    public <T> T get() {
        return (T) (Double) mc.thePlayer.motionX;
    }
}
