/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import net.minecraft.util.EnumFacing;

public enum EnumFaceDirection {
    DOWN(new VertexInformation[]{new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX)}),
    UP(new VertexInformation[]{new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX)}),
    NORTH(new VertexInformation[]{new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX)}),
    SOUTH(new VertexInformation[]{new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX)}),
    WEST(new VertexInformation[]{new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX)}),
    EAST(new VertexInformation[]{new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX)});

    private static final EnumFaceDirection[] facings = new EnumFaceDirection[6];
    private final VertexInformation[] vertexInfos;

    private EnumFaceDirection(VertexInformation[] vertexInformationArray) {
        this.vertexInfos = vertexInformationArray;
    }

    static {
        EnumFaceDirection.facings[Constants.DOWN_INDEX] = DOWN;
        EnumFaceDirection.facings[Constants.UP_INDEX] = UP;
        EnumFaceDirection.facings[Constants.NORTH_INDEX] = NORTH;
        EnumFaceDirection.facings[Constants.SOUTH_INDEX] = SOUTH;
        EnumFaceDirection.facings[Constants.WEST_INDEX] = WEST;
        EnumFaceDirection.facings[Constants.EAST_INDEX] = EAST;
    }

    public VertexInformation func_179025_a(int n) {
        return this.vertexInfos[n];
    }

    public static EnumFaceDirection getFacing(EnumFacing enumFacing) {
        return facings[enumFacing.getIndex()];
    }

    public static class VertexInformation {
        public final int field_179184_a;
        public final int field_179182_b;
        public final int field_179183_c;

        private VertexInformation(int n, int n2, int n3) {
            this.field_179184_a = n;
            this.field_179182_b = n2;
            this.field_179183_c = n3;
        }
    }

    public static final class Constants {
        public static final int SOUTH_INDEX = EnumFacing.SOUTH.getIndex();
        public static final int EAST_INDEX;
        public static final int WEST_INDEX;
        public static final int UP_INDEX;
        public static final int NORTH_INDEX;
        public static final int DOWN_INDEX;

        static {
            UP_INDEX = EnumFacing.UP.getIndex();
            EAST_INDEX = EnumFacing.EAST.getIndex();
            NORTH_INDEX = EnumFacing.NORTH.getIndex();
            DOWN_INDEX = EnumFacing.DOWN.getIndex();
            WEST_INDEX = EnumFacing.WEST.getIndex();
        }
    }
}

