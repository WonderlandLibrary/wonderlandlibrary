package dev.rise.script.block;

import dev.rise.script.block.api.BlockData;
import dev.rise.util.InstanceAccess;
import lombok.Getter;

@Getter
public abstract class Block implements InstanceAccess {

    protected BlockData blockData;

    public Block() {
        if (this.getClass().isAnnotationPresent(BlockData.class)) {
            this.blockData = this.getClass().getAnnotation(BlockData.class);
        } else {
            throw new RuntimeException("BlockData annotation not found on " + this.getClass().getSimpleName());
        }
    }
}
