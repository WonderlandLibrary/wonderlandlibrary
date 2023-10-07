package dev.rise.script.block.impl.variables;

import dev.rise.script.block.Block;

public abstract class VariableBlock extends Block {
    public abstract <T> T get();
}
