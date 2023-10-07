package dev.rise.script.block.impl.instruction;

import dev.rise.script.block.Block;
import dev.rise.script.block.impl.variables.VariableBlock;

import java.util.Arrays;
import java.util.List;

public abstract class InstructionBlock extends Block {

    protected final List<VariableBlock> objects;

    public InstructionBlock(final VariableBlock... objects) {
        this.objects = Arrays.asList(objects);
    }

    public abstract void run();
}
