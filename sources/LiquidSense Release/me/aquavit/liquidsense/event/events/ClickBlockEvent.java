package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class ClickBlockEvent extends Event {
    private BlockPos clickedBlock;
    private EnumFacing enumFacing;

    public ClickBlockEvent(BlockPos clickedBlock, EnumFacing enumFacing) {
        this.clickedBlock = clickedBlock;
        this.enumFacing = enumFacing;
    }

    public BlockPos getClickedBlock() {
        return this.clickedBlock;
    }

    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }
}
