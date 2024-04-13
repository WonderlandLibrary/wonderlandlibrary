package net.smoothboot.client.mixinterface;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public interface IWorld
{

    List<BlockEntityTickInvoker> getBlockEntityTickers();
    public Stream<VoxelShape> getBlockCollisionsStream(@Nullable Entity entity,
                                                       Box box);

    public default Stream<Box> getCollidingBoxes(@Nullable Entity entity,
                                                 Box box)
    {
        return getBlockCollisionsStream(entity, box)
                .flatMap(vs -> vs.getBoundingBoxes().stream())
                .filter(b -> b.intersects(box));
    }
}