/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Vec3;

public class TexturedQuad {
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;

    public TexturedQuad(PositionTextureVertex[] vertices) {
        this.vertexPositions = vertices;
        this.nVertices = vertices.length;
    }

    public TexturedQuad(PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2, int texcoordV2, float textureWidth, float textureHeight) {
        this(vertices);
        float f2 = 0.0f / textureWidth;
        float f1 = 0.0f / textureHeight;
        vertices[0] = vertices[0].setTexturePosition((float)texcoordU2 / textureWidth - f2, (float)texcoordV1 / textureHeight + f1);
        vertices[1] = vertices[1].setTexturePosition((float)texcoordU1 / textureWidth + f2, (float)texcoordV1 / textureHeight + f1);
        vertices[2] = vertices[2].setTexturePosition((float)texcoordU1 / textureWidth + f2, (float)texcoordV2 / textureHeight - f1);
        vertices[3] = vertices[3].setTexturePosition((float)texcoordU2 / textureWidth - f2, (float)texcoordV2 / textureHeight - f1);
    }

    public void flipFace() {
        PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[this.vertexPositions.length];
        for (int i2 = 0; i2 < this.vertexPositions.length; ++i2) {
            apositiontexturevertex[i2] = this.vertexPositions[this.vertexPositions.length - i2 - 1];
        }
        this.vertexPositions = apositiontexturevertex;
    }

    public void draw(WorldRenderer renderer, float scale) {
        Vec3 vec3 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[0].vector3D);
        Vec3 vec31 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[2].vector3D);
        Vec3 vec32 = vec31.crossProduct(vec3).normalize();
        float f2 = (float)vec32.xCoord;
        float f1 = (float)vec32.yCoord;
        float f22 = (float)vec32.zCoord;
        if (this.invertNormal) {
            f2 = -f2;
            f1 = -f1;
            f22 = -f22;
        }
        renderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
        for (int i2 = 0; i2 < 4; ++i2) {
            PositionTextureVertex positiontexturevertex = this.vertexPositions[i2];
            renderer.pos(positiontexturevertex.vector3D.xCoord * (double)scale, positiontexturevertex.vector3D.yCoord * (double)scale, positiontexturevertex.vector3D.zCoord * (double)scale).tex(positiontexturevertex.texturePositionX, positiontexturevertex.texturePositionY).normal(f2, f1, f22).endVertex();
        }
        Tessellator.getInstance().draw();
    }
}

