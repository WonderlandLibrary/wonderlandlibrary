package dev.rise.script.block.impl.event.impl.motion;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.script.block.api.BlockData;
import dev.rise.script.block.impl.event.EventBlock;

@BlockData(name = "onPreMotion", description = "Instructions inside this event listener will get ran when the pre motion event is called.")
public final class PreMotionBlock extends EventBlock {

    public PreMotionBlock() {
        super(PreMotionEvent.class);
    }
}
