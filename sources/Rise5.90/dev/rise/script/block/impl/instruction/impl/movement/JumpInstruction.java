package dev.rise.script.block.impl.instruction.impl.movement;

import dev.rise.script.block.api.BlockData;
import dev.rise.script.block.impl.instruction.InstructionBlock;

@BlockData(name = "Jump", description = "Invokes the jump method of the player entity")
public final class JumpInstruction extends InstructionBlock {

    @Override
    public void run() {
        mc.thePlayer.jump();
    }
}
