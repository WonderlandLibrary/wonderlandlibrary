package dev.rise.script.block.impl.event;

import dev.rise.event.api.Event;
import dev.rise.script.block.Block;
import dev.rise.script.block.impl.instruction.InstructionBlock;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class EventBlock extends Block {

    protected final List<InstructionBlock> listeners = new ArrayList<>();
    private final Class<? extends Event> clazz;

    public EventBlock(final Class<? extends Event> clazz) {
        this.clazz = clazz;
    }

    public void run(final Event event) {
        this.listeners.forEach(InstructionBlock::run);
    }
}
