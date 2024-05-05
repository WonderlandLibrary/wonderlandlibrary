package net.minecraft.client.renderer;

import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.util.EnumFacing;

public enum EnumFaceDirection {
    DOWN(new VertexInformation[]{new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null)}),
    UP(new VertexInformation[]{new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null)}),
    NORTH(new VertexInformation[]{new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null)}),
    SOUTH(new VertexInformation[]{new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null)}),
    WEST(new VertexInformation[]{new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null)}),
    EAST(new VertexInformation[]{new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null)});

    private static final EnumFaceDirection[] facings;
    private final VertexInformation[] vertexInfos;

    public static EnumFaceDirection getFacing(EnumFacing facing) {
        return facings[facing.getIndex()];
    }

    private EnumFaceDirection(VertexInformation[] vertexInfosIn) {
        this.vertexInfos = vertexInfosIn;
    }

    public VertexInformation getVertexInformation(int index) {
        return this.vertexInfos[index];
    }

    static {
        facings = new EnumFaceDirection[6];
        EnumFaceDirection.facings[Constants.DOWN_INDEX] = DOWN;
        EnumFaceDirection.facings[Constants.UP_INDEX] = UP;
        EnumFaceDirection.facings[Constants.NORTH_INDEX] = NORTH;
        EnumFaceDirection.facings[Constants.SOUTH_INDEX] = SOUTH;
        EnumFaceDirection.facings[Constants.WEST_INDEX] = WEST;
        EnumFaceDirection.facings[Constants.EAST_INDEX] = EAST;
    }
}
