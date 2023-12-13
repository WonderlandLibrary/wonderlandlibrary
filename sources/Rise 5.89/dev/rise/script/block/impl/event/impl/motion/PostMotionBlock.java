package dev.rise.script.block.impl.event.impl.motion;

import dev.rise.event.impl.motion.PostMotionEvent;
import dev.rise.script.block.api.BlockData;
import dev.rise.script.block.impl.event.EventBlock;

@BlockData(name = "onPostMotion", description = "Instructions inside this event listener will get ran when the post motion event is called.")
public final class PostMotionBlock extends EventBlock {

    public PostMotionBlock() {
        super(PostMotionEvent.class);
    }
}
