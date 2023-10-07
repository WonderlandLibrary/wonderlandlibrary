package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

@AllArgsConstructor @Getter @Setter
public class LiquidBoundingBoxEvent extends Event {
    AxisAlignedBB axisAlignedBB;
    BlockPos blockPos;
    BlockLiquid block;
}
