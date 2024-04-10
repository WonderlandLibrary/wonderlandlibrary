package info.sigmaclient.sigma.utils;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class VecUtils {
    public static Vector3d BlockPos2Vec3(BlockPos bp){
        return new Vector3d(bp.getX(), bp.getY(), bp.getZ());
    }
    public static Vector3d blockPosRedirection(BlockPos bp, Direction ef){
        return new Vector3d(
                bp.getX() + 0.5 + ef.getXOffset() / 2f,
                bp.getY() + 0.5 + ef.getYOffset() / 2f,
                bp.getZ() + 0.5 + ef.getZOffset() / 2f
        );
    }
}
