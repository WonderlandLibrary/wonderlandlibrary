package me.earth.earthhack.impl.util.minecraft;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.core.ducks.IWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.world.World.MAX_ENTITY_RADIUS;

public class WorldUtil implements Globals
{
    public static <T extends Entity> List<T> getEntitiesWithinAABB(Class <? extends T > classEntity, AxisAlignedBB bb)
    {
        return getEntitiesWithinAABB(classEntity, bb, EntitySelectors.NOT_SPECTATING);
    }

    @SuppressWarnings({"ConstantConditions", "Guava"})
    public static <T extends Entity> List<T> getEntitiesWithinAABB(Class <? extends T > clazz, AxisAlignedBB aabb, @Nullable Predicate<? super T > filter)
    {
        int j2 = MathHelper.floor((aabb.minX - MAX_ENTITY_RADIUS) / 16.0D);
        int k2 = MathHelper.ceil((aabb.maxX + MAX_ENTITY_RADIUS) / 16.0D);
        int l2 = MathHelper.floor((aabb.minZ - MAX_ENTITY_RADIUS) / 16.0D);
        int i3 = MathHelper.ceil((aabb.maxZ + MAX_ENTITY_RADIUS) / 16.0D);
        List<T> list = Lists.newArrayList();

        for (int j3 = j2; j3 < k2; ++j3)
        {
            for (int k3 = l2; k3 < i3; ++k3)
            {
                if (((IWorld) mc.world).isChunkLoaded(j3, k3, true))
                {
                    mc.world.getChunk(j3, k3).getEntitiesOfTypeWithinAABB(clazz, aabb, list, filter);
                }
            }
        }

        return list;
    }

}
