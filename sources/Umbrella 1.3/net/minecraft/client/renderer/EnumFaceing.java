/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import net.minecraft.util.EnumFacing;

public enum EnumFaceing {
    DOWN("DOWN", 0, new VertexInformation(Constants.field_179176_f, Constants.field_179178_e, Constants.field_179181_a, null), new VertexInformation(Constants.field_179176_f, Constants.field_179178_e, Constants.field_179177_d, null), new VertexInformation(Constants.field_179180_c, Constants.field_179178_e, Constants.field_179177_d, null), new VertexInformation(Constants.field_179180_c, Constants.field_179178_e, Constants.field_179181_a, null)),
    UP("UP", 1, new VertexInformation(Constants.field_179176_f, Constants.field_179179_b, Constants.field_179177_d, null), new VertexInformation(Constants.field_179176_f, Constants.field_179179_b, Constants.field_179181_a, null), new VertexInformation(Constants.field_179180_c, Constants.field_179179_b, Constants.field_179181_a, null), new VertexInformation(Constants.field_179180_c, Constants.field_179179_b, Constants.field_179177_d, null)),
    NORTH("NORTH", 2, new VertexInformation(Constants.field_179180_c, Constants.field_179179_b, Constants.field_179177_d, null), new VertexInformation(Constants.field_179180_c, Constants.field_179178_e, Constants.field_179177_d, null), new VertexInformation(Constants.field_179176_f, Constants.field_179178_e, Constants.field_179177_d, null), new VertexInformation(Constants.field_179176_f, Constants.field_179179_b, Constants.field_179177_d, null)),
    SOUTH("SOUTH", 3, new VertexInformation(Constants.field_179176_f, Constants.field_179179_b, Constants.field_179181_a, null), new VertexInformation(Constants.field_179176_f, Constants.field_179178_e, Constants.field_179181_a, null), new VertexInformation(Constants.field_179180_c, Constants.field_179178_e, Constants.field_179181_a, null), new VertexInformation(Constants.field_179180_c, Constants.field_179179_b, Constants.field_179181_a, null)),
    WEST("WEST", 4, new VertexInformation(Constants.field_179176_f, Constants.field_179179_b, Constants.field_179177_d, null), new VertexInformation(Constants.field_179176_f, Constants.field_179178_e, Constants.field_179177_d, null), new VertexInformation(Constants.field_179176_f, Constants.field_179178_e, Constants.field_179181_a, null), new VertexInformation(Constants.field_179176_f, Constants.field_179179_b, Constants.field_179181_a, null)),
    EAST("EAST", 5, new VertexInformation(Constants.field_179180_c, Constants.field_179179_b, Constants.field_179181_a, null), new VertexInformation(Constants.field_179180_c, Constants.field_179178_e, Constants.field_179181_a, null), new VertexInformation(Constants.field_179180_c, Constants.field_179178_e, Constants.field_179177_d, null), new VertexInformation(Constants.field_179180_c, Constants.field_179179_b, Constants.field_179177_d, null));

    private static final EnumFaceing[] field_179029_g;
    private final VertexInformation[] field_179035_h;
    private static final EnumFaceing[] $VALUES;
    private static final String __OBFID = "CL_00002562";

    static {
        field_179029_g = new EnumFaceing[6];
        $VALUES = new EnumFaceing[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
        EnumFaceing.field_179029_g[Constants.field_179178_e] = DOWN;
        EnumFaceing.field_179029_g[Constants.field_179179_b] = UP;
        EnumFaceing.field_179029_g[Constants.field_179177_d] = NORTH;
        EnumFaceing.field_179029_g[Constants.field_179181_a] = SOUTH;
        EnumFaceing.field_179029_g[Constants.field_179176_f] = WEST;
        EnumFaceing.field_179029_g[Constants.field_179180_c] = EAST;
    }

    public static EnumFaceing func_179027_a(EnumFacing p_179027_0_) {
        return field_179029_g[p_179027_0_.getIndex()];
    }

    private EnumFaceing(String p_i46272_1_, int p_i46272_2_, VertexInformation ... p_i46272_3_) {
        this.field_179035_h = p_i46272_3_;
    }

    public VertexInformation func_179025_a(int p_179025_1_) {
        return this.field_179035_h[p_179025_1_];
    }

    public static final class Constants {
        public static final int field_179181_a = EnumFacing.SOUTH.getIndex();
        public static final int field_179179_b = EnumFacing.UP.getIndex();
        public static final int field_179180_c = EnumFacing.EAST.getIndex();
        public static final int field_179177_d = EnumFacing.NORTH.getIndex();
        public static final int field_179178_e = EnumFacing.DOWN.getIndex();
        public static final int field_179176_f = EnumFacing.WEST.getIndex();
        private static final String __OBFID = "CL_00002560";
    }

    public static class VertexInformation {
        public final int field_179184_a;
        public final int field_179182_b;
        public final int field_179183_c;
        private static final String __OBFID = "CL_00002559";

        private VertexInformation(int p_i46270_1_, int p_i46270_2_, int p_i46270_3_) {
            this.field_179184_a = p_i46270_1_;
            this.field_179182_b = p_i46270_2_;
            this.field_179183_c = p_i46270_3_;
        }

        VertexInformation(int p_i46271_1_, int p_i46271_2_, int p_i46271_3_, Object p_i46271_4_) {
            this(p_i46271_1_, p_i46271_2_, p_i46271_3_);
        }
    }
}

