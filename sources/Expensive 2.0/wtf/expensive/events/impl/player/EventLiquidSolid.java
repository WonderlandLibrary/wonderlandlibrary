package wtf.expensive.events.impl.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import wtf.expensive.events.Event;


/**
 * @author dedinside
 * @since 09.06.2023
 */
public class EventLiquidSolid extends Event  {
    private final FlowingFluidBlock blockLiquid;
    private final BlockPos pos;
    private VoxelShape collision;

    public EventLiquidSolid(FlowingFluidBlock blockLiquid, BlockPos pos) {
        this.blockLiquid = blockLiquid;
        this.pos = pos;
        this.collision = VoxelShapes.empty();
    }

    public FlowingFluidBlock getBlock() {
        return blockLiquid;
    }

    public BlockPos getPos() {
        return pos;
    }

    public VoxelShape getCollision() {
        return collision;
    }

    public void setCollision(VoxelShape collision) {
        this.collision = collision;
    }
}