// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import net.minecraft.util.BlockPos;
import exhibition.event.Event;

public class EventDamageBlock extends Event
{
    private BlockPos currentBlock;
    
    public void fire(final BlockPos b) {
        this.setCurrentBlock(b);
        super.fire();
    }
    
    public BlockPos getCurrentBlock() {
        return this.currentBlock;
    }
    
    public void setCurrentBlock(final BlockPos currentBlock) {
        this.currentBlock = currentBlock;
    }
}
