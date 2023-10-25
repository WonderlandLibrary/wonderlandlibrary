package net.minecraft.tileentity;

public static class TileEntityBeacon.BeamSegment {
    private final float[] colors;
    private int height;

    public TileEntityBeacon.BeamSegment(float[] p_i45669_1_) {
        this.colors = p_i45669_1_;
        this.height = 1;
    }

    protected void incrementHeight() {
        ++this.height;
    }

    public float[] getColors() {
        return this.colors;
    }

    public int getHeight() {
        return this.height;
    }
}
