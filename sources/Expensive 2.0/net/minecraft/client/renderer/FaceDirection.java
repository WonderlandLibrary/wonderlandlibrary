package net.minecraft.client.renderer;

import net.minecraft.util.Direction;
import net.minecraft.util.Util;

public enum FaceDirection
{
    DOWN(new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX)),
    UP(new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX)),
    NORTH(new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX)),
    SOUTH(new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX)),
    WEST(new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX)),
    EAST(new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX));

    private static final FaceDirection[] FACINGS = Util.make(new FaceDirection[6], (p_209235_0_) -> {
        p_209235_0_[Constants.DOWN_INDEX] = DOWN;
        p_209235_0_[Constants.UP_INDEX] = UP;
        p_209235_0_[Constants.NORTH_INDEX] = NORTH;
        p_209235_0_[Constants.SOUTH_INDEX] = SOUTH;
        p_209235_0_[Constants.WEST_INDEX] = WEST;
        p_209235_0_[Constants.EAST_INDEX] = EAST;
    });
    private final VertexInformation[] vertexInfos;

    public static FaceDirection getFacing(Direction facing)
    {
        return FACINGS[facing.getIndex()];
    }

    private FaceDirection(VertexInformation... vertexInfosIn)
    {
        this.vertexInfos = vertexInfosIn;
    }

    public VertexInformation getVertexInformation(int index)
    {
        return this.vertexInfos[index];
    }

    public static final class Constants {
        public static final int SOUTH_INDEX = Direction.SOUTH.getIndex();
        public static final int UP_INDEX = Direction.UP.getIndex();
        public static final int EAST_INDEX = Direction.EAST.getIndex();
        public static final int NORTH_INDEX = Direction.NORTH.getIndex();
        public static final int DOWN_INDEX = Direction.DOWN.getIndex();
        public static final int WEST_INDEX = Direction.WEST.getIndex();
    }

    public static class VertexInformation {
        public final int xIndex;
        public final int yIndex;
        public final int zIndex;

        private VertexInformation(int xIndexIn, int yIndexIn, int zIndexIn)
        {
            this.xIndex = xIndexIn;
            this.yIndex = yIndexIn;
            this.zIndex = zIndexIn;
        }
    }
}
