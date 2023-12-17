// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.optifine.shaders.SVertexFormat;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.BufferBuilder;

public class TexturedQuad
{
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;
    
    public TexturedQuad(final PositionTextureVertex[] vertices) {
        this.vertexPositions = vertices;
        this.nVertices = vertices.length;
    }
    
    public TexturedQuad(final PositionTextureVertex[] vertices, final int texcoordU1, final int texcoordV1, final int texcoordU2, final int texcoordV2, final float textureWidth, final float textureHeight) {
        this(vertices);
        final float f = 0.0f / textureWidth;
        final float f2 = 0.0f / textureHeight;
        vertices[0] = vertices[0].setTexturePosition(texcoordU2 / textureWidth - f, texcoordV1 / textureHeight + f2);
        vertices[1] = vertices[1].setTexturePosition(texcoordU1 / textureWidth + f, texcoordV1 / textureHeight + f2);
        vertices[2] = vertices[2].setTexturePosition(texcoordU1 / textureWidth + f, texcoordV2 / textureHeight - f2);
        vertices[3] = vertices[3].setTexturePosition(texcoordU2 / textureWidth - f, texcoordV2 / textureHeight - f2);
    }
    
    public void flipFace() {
        final PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[this.vertexPositions.length];
        for (int i = 0; i < this.vertexPositions.length; ++i) {
            apositiontexturevertex[i] = this.vertexPositions[this.vertexPositions.length - i - 1];
        }
        this.vertexPositions = apositiontexturevertex;
    }
    
    public void draw(final BufferBuilder renderer, final float scale) {
        final Vec3d vec3d = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[0].vector3D);
        final Vec3d vec3d2 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[2].vector3D);
        final Vec3d vec3d3 = vec3d2.crossProduct(vec3d).normalize();
        float f = (float)vec3d3.x;
        float f2 = (float)vec3d3.y;
        float f3 = (float)vec3d3.z;
        if (this.invertNormal) {
            f = -f;
            f2 = -f2;
            f3 = -f3;
        }
        if (Config.isShaders()) {
            renderer.begin(7, SVertexFormat.defVertexFormatTextured);
        }
        else {
            renderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
        }
        for (int i = 0; i < 4; ++i) {
            final PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
            renderer.pos(positiontexturevertex.vector3D.x * scale, positiontexturevertex.vector3D.y * scale, positiontexturevertex.vector3D.z * scale).tex(positiontexturevertex.texturePositionX, positiontexturevertex.texturePositionY).normal(f, f2, f3).endVertex();
        }
        Tessellator.getInstance().draw();
    }
}
