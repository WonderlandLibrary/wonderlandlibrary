package net.minecraft.client.renderer.block.model;

public class BlockFaceUV {
    public float[] uvs;
    public final int rotation;

    public BlockFaceUV(float[] uvsIn, int rotationIn) {
        this.uvs = uvsIn;
        this.rotation = rotationIn;
    }

    public float func_178348_a(int p_178348_1_) {
        if (this.uvs == null) {
            throw new NullPointerException("uvs");
        }
        int i = this.func_178347_d(p_178348_1_);
        return i != 0 && i != 1 ? this.uvs[2] : this.uvs[0];
    }

    public float func_178346_b(int p_178346_1_) {
        if (this.uvs == null) {
            throw new NullPointerException("uvs");
        }
        int i = this.func_178347_d(p_178346_1_);
        return i != 0 && i != 3 ? this.uvs[3] : this.uvs[1];
    }

    private int func_178347_d(int p_178347_1_) {
        return (p_178347_1_ + this.rotation / 90) % 4;
    }

    public int func_178345_c(int p_178345_1_) {
        return (p_178345_1_ + (4 - this.rotation / 90)) % 4;
    }

    public void setUvs(float[] uvsIn) {
        if (this.uvs == null) {
            this.uvs = uvsIn;
        }
    }
}
