package net.minecraft.client.model;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.Vec3;
import optifine.Config;
import shadersmod.client.SVertexBuilder;

public class TexturedQuad
{
  public PositionTextureVertex[] vertexPositions;
  public int nVertices;
  private boolean invertNormal;
  private static final String __OBFID = "CL_00000850";
  
  public TexturedQuad(PositionTextureVertex[] vertices)
  {
    this.vertexPositions = vertices;
    this.nVertices = vertices.length;
  }
  
  public TexturedQuad(PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2, int texcoordV2, float textureWidth, float textureHeight)
  {
    this(vertices);
    float var8 = 0.0F / textureWidth;
    float var9 = 0.0F / textureHeight;
    vertices[0] = vertices[0].setTexturePosition(texcoordU2 / textureWidth - var8, texcoordV1 / textureHeight + var9);
    vertices[1] = vertices[1].setTexturePosition(texcoordU1 / textureWidth + var8, texcoordV1 / textureHeight + var9);
    vertices[2] = vertices[2].setTexturePosition(texcoordU1 / textureWidth + var8, texcoordV2 / textureHeight - var9);
    vertices[3] = vertices[3].setTexturePosition(texcoordU2 / textureWidth - var8, texcoordV2 / textureHeight - var9);
  }
  
  public void flipFace()
  {
    PositionTextureVertex[] var1 = new PositionTextureVertex[this.vertexPositions.length];
    for (int var2 = 0; var2 < this.vertexPositions.length; var2++) {
      var1[var2] = this.vertexPositions[(this.vertexPositions.length - var2 - 1)];
    }
    this.vertexPositions = var1;
  }
  
  public void func_178765_a(WorldRenderer renderer, float scale)
  {
    Vec3 var3 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[0].vector3D);
    Vec3 var4 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[2].vector3D);
    Vec3 var5 = var4.crossProduct(var3).normalize();
    renderer.startDrawingQuads();
    if (Config.isShaders()) {
      SVertexBuilder.startTexturedQuad(renderer);
    }
    if (this.invertNormal) {
      renderer.func_178980_d(-(float)var5.xCoord, -(float)var5.yCoord, -(float)var5.zCoord);
    } else {
      renderer.func_178980_d((float)var5.xCoord, (float)var5.yCoord, (float)var5.zCoord);
    }
    for (int var6 = 0; var6 < 4; var6++)
    {
      PositionTextureVertex var7 = this.vertexPositions[var6];
      renderer.addVertexWithUV(var7.vector3D.xCoord * scale, var7.vector3D.yCoord * scale, var7.vector3D.zCoord * scale, var7.texturePositionX, var7.texturePositionY);
    }
    Tessellator.getInstance().draw();
  }
}
