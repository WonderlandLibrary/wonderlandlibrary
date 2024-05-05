package net.minecraft.client.renderer;

public static class EnumFaceDirection.VertexInformation {
    public final int xIndex;
    public final int yIndex;
    public final int zIndex;

    private EnumFaceDirection.VertexInformation(int xIndexIn, int yIndexIn, int zIndexIn) {
        this.xIndex = xIndexIn;
        this.yIndex = yIndexIn;
        this.zIndex = zIndexIn;
    }
}
