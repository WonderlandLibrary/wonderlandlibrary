package dev.rise.script.block.impl.variables.impl.bool;

import dev.rise.script.block.impl.variables.VariableBlock;

public final class OnGroundVariable extends VariableBlock {

    @Override
    public <T> T get() {
        return (T) (Boolean) mc.thePlayer.onGround;
    }
}
