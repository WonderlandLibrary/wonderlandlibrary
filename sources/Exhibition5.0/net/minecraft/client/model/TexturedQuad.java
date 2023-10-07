// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.util.Vec3;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

public class TexturedQuad
{
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;
    private static final String __OBFID = "CL_00000850";
    
    public TexturedQuad(final PositionTextureVertex[] p_i46364_1_) {
        this.vertexPositions = p_i46364_1_;
        this.nVertices = p_i46364_1_.length;
    }
    
    public TexturedQuad(final PositionTextureVertex[] p_i1153_1_, final int p_i1153_2_, final int p_i1153_3_, final int p_i1153_4_, final int p_i1153_5_, final float p_i1153_6_, final float p_i1153_7_) {
        this(p_i1153_1_);
        final float var8 = 0.0f / p_i1153_6_;
        final float var9 = 0.0f / p_i1153_7_;
        p_i1153_1_[0] = p_i1153_1_[0].setTexturePosition(p_i1153_4_ / p_i1153_6_ - var8, p_i1153_3_ / p_i1153_7_ + var9);
        p_i1153_1_[1] = p_i1153_1_[1].setTexturePosition(p_i1153_2_ / p_i1153_6_ + var8, p_i1153_3_ / p_i1153_7_ + var9);
        p_i1153_1_[2] = p_i1153_1_[2].setTexturePosition(p_i1153_2_ / p_i1153_6_ + var8, p_i1153_5_ / p_i1153_7_ - var9);
        p_i1153_1_[3] = p_i1153_1_[3].setTexturePosition(p_i1153_4_ / p_i1153_6_ - var8, p_i1153_5_ / p_i1153_7_ - var9);
    }
    
    public void flipFace() {
        final PositionTextureVertex[] var1 = new PositionTextureVertex[this.vertexPositions.length];
        for (int var2 = 0; var2 < this.vertexPositions.length; ++var2) {
            var1[var2] = this.vertexPositions[this.vertexPositions.length - var2 - 1];
        }
        this.vertexPositions = var1;
    }
    
    public void func_178765_a(final WorldRenderer p_178765_1_, final float p_178765_2_) {
        final Vec3 var3 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[0].vector3D);
        final Vec3 var4 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[2].vector3D);
        final Vec3 var5 = var4.crossProduct(var3).normalize();
        p_178765_1_.startDrawingQuads();
        if (this.invertNormal) {
            p_178765_1_.func_178980_d(-(float)var5.xCoord, -(float)var5.yCoord, -(float)var5.zCoord);
        }
        else {
            p_178765_1_.func_178980_d((float)var5.xCoord, (float)var5.yCoord, (float)var5.zCoord);
        }
        for (int var6 = 0; var6 < 4; ++var6) {
            final PositionTextureVertex var7 = this.vertexPositions[var6];
            p_178765_1_.addVertexWithUV(var7.vector3D.xCoord * p_178765_2_, var7.vector3D.yCoord * p_178765_2_, var7.vector3D.zCoord * p_178765_2_, var7.texturePositionX, var7.texturePositionY);
        }
        Tessellator.getInstance().draw();
    }
}
