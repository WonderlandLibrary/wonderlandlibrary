package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import optifine.Config;
import optifine.Reflector;
import org.lwjgl.opengl.GL11;
import shadersmod.client.SVertexBuilder;

public class WorldVertexBufferUploader {
  private static final String __OBFID = "CL_00002567";
  
  public void func_181679_a(WorldRenderer p_181679_1_) {
    if (p_181679_1_.getVertexCount() > 0) {
      VertexFormat vertexformat = p_181679_1_.getVertexFormat();
      int i = vertexformat.getNextOffset();
      ByteBuffer bytebuffer = p_181679_1_.getByteBuffer();
      List<VertexFormatElement> list = vertexformat.getElements();
      boolean flag = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
      boolean flag1 = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();
      for (int j = 0; j < list.size(); j++) {
        VertexFormatElement vertexformatelement = list.get(j);
        VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
        if (flag) {
          Reflector.callVoid(vertexformatelement$enumusage, Reflector.ForgeVertexFormatElementEnumUseage_preDraw, new Object[] { vertexformat, Integer.valueOf(j), Integer.valueOf(i), bytebuffer });
        } else {
          int l = vertexformatelement.getType().getGlConstant();
          int k = vertexformatelement.getIndex();
          bytebuffer.position(vertexformat.func_181720_d(j));
          switch (WorldVertexBufferUploader$1.field_178958_a[vertexformatelement$enumusage.ordinal()]) {
            case 1:
              GL11.glVertexPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
              GL11.glEnableClientState(32884);
              break;
            case 2:
              OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + k);
              GL11.glTexCoordPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
              GL11.glEnableClientState(32888);
              OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
              break;
            case 3:
              GL11.glColorPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
              GL11.glEnableClientState(32886);
              break;
            case 4:
              GL11.glNormalPointer(l, i, bytebuffer);
              GL11.glEnableClientState(32885);
              break;
          } 
        } 
      } 
      if (p_181679_1_.isMultiTexture()) {
        p_181679_1_.drawMultiTexture();
      } else if (Config.isShaders()) {
        SVertexBuilder.drawArrays(p_181679_1_.getDrawMode(), 0, p_181679_1_.getVertexCount(), p_181679_1_);
      } else {
        GL11.glDrawArrays(p_181679_1_.getDrawMode(), 0, p_181679_1_.getVertexCount());
      } 
      int i1 = 0;
      for (int k1 = list.size(); i1 < k1; i1++) {
        VertexFormatElement vertexformatelement1 = list.get(i1);
        VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.getUsage();
        if (flag1) {
          Reflector.callVoid(vertexformatelement$enumusage1, Reflector.ForgeVertexFormatElementEnumUseage_postDraw, new Object[] { vertexformat, Integer.valueOf(i1), Integer.valueOf(i), bytebuffer });
        } else {
          int j1 = vertexformatelement1.getIndex();
          switch (WorldVertexBufferUploader$1.field_178958_a[vertexformatelement$enumusage1.ordinal()]) {
            case 1:
              GL11.glDisableClientState(32884);
              break;
            case 2:
              OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + j1);
              GL11.glDisableClientState(32888);
              OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
              break;
            case 3:
              GL11.glDisableClientState(32886);
              GlStateManager.resetColor();
              break;
            case 4:
              GL11.glDisableClientState(32885);
              break;
          } 
        } 
      } 
    } 
    p_181679_1_.reset();
  }
  
  static final class WorldVertexBufferUploader$1 {
    static final int[] field_178958_a = new int[(VertexFormatElement.EnumUsage.values()).length];
    
    private static final String __OBFID = "CL_00002566";
    
    static {
      try {
        field_178958_a[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_178958_a[VertexFormatElement.EnumUsage.UV.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_178958_a[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_178958_a[VertexFormatElement.EnumUsage.NORMAL.ordinal()] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {}
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\WorldVertexBufferUploader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */