package dev.rise.script.block.impl.instruction.impl.movement;

import dev.rise.script.block.api.BlockData;
import dev.rise.script.block.impl.instruction.InstructionBlock;
import dev.rise.util.player.MoveUtil;

@BlockData(name = "Strafe", description = "If given a parameter, this will set the players speed to the given number and strafe otherwise it will just strafe.")
public final class StrafeInstruction extends InstructionBlock {

    @Override
    public void run() {
        if (this.objects.size() > 0) {
            MoveUtil.strafe((double) this.objects.get(0).get());
        } else {
            MoveUtil.strafe();
        }
    }
}
