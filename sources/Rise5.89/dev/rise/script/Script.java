package dev.rise.script;

import dev.rise.script.block.impl.event.EventBlock;
import dev.rise.script.block.impl.event.impl.motion.PreMotionBlock;
import dev.rise.script.block.impl.instruction.impl.movement.JumpInstruction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Script {

    private final String name, description, developer;
    private final List<EventBlock> blocks = new ArrayList<>();

    public Script(final String name, final String description, final String developer) {
        this.name = name;
        this.description = description;
        this.developer = developer;

        final EventBlock block = new PreMotionBlock();

        block.getListeners().add(new JumpInstruction());

        blocks.add(block);
    }
}
