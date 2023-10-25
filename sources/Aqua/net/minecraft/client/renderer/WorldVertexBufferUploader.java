package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.shaders.SVertexBuilder;
import org.lwjgl.opengl.GL11;

public class WorldVertexBufferUploader {
    public void draw(WorldRenderer p_181679_1_) {
        if (p_181679_1_.getVertexCount() > 0) {
            if (p_181679_1_.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
                p_181679_1_.quadsToTriangles();
            }
            VertexFormat vertexformat = p_181679_1_.getVertexFormat();
            int i = vertexformat.getNextOffset();
            ByteBuffer bytebuffer = p_181679_1_.getByteBuffer();
            List list = vertexformat.getElements();
            boolean flag = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
            boolean flag1 = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();
            block12: for (int j = 0; j < list.size(); ++j) {
                VertexFormatElement vertexformatelement = (VertexFormatElement)list.get(j);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                if (flag) {
                    Reflector.callVoid((Object)vertexformatelement$enumusage, (ReflectorMethod)Reflector.ForgeVertexFormatElementEnumUseage_preDraw, (Object[])new Object[]{vertexformat, j, i, bytebuffer});
                    continue;
                }
                int k = vertexformatelement.getType().getGlConstant();
                int l = vertexformatelement.getIndex();
                bytebuffer.position(vertexformat.getOffset(j));
                switch (1.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumUsage[vertexformatelement$enumusage.ordinal()]) {
                    case 1: {
                        GL11.glVertexPointer((int)vertexformatelement.getElementCount(), (int)k, (int)i, (ByteBuffer)bytebuffer);
                        GL11.glEnableClientState((int)32884);
                        continue block12;
                    }
                    case 2: {
                        OpenGlHelper.setClientActiveTexture((int)(OpenGlHelper.defaultTexUnit + l));
                        GL11.glTexCoordPointer((int)vertexformatelement.getElementCount(), (int)k, (int)i, (ByteBuffer)bytebuffer);
                        GL11.glEnableClientState((int)32888);
                        OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.defaultTexUnit);
                        continue block12;
                    }
                    case 3: {
                        GL11.glColorPointer((int)vertexformatelement.getElementCount(), (int)k, (int)i, (ByteBuffer)bytebuffer);
                        GL11.glEnableClientState((int)32886);
                        continue block12;
                    }
                    case 4: {
                        GL11.glNormalPointer((int)k, (int)i, (ByteBuffer)bytebuffer);
                        GL11.glEnableClientState((int)32885);
                    }
                }
            }
            if (p_181679_1_.isMultiTexture()) {
                p_181679_1_.drawMultiTexture();
            } else if (Config.isShaders()) {
                SVertexBuilder.drawArrays((int)p_181679_1_.getDrawMode(), (int)0, (int)p_181679_1_.getVertexCount(), (WorldRenderer)p_181679_1_);
            } else {
                GL11.glDrawArrays((int)p_181679_1_.getDrawMode(), (int)0, (int)p_181679_1_.getVertexCount());
            }
            int k1 = list.size();
            block13: for (int j1 = 0; j1 < k1; ++j1) {
                VertexFormatElement vertexformatelement1 = (VertexFormatElement)list.get(j1);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.getUsage();
                if (flag1) {
                    Reflector.callVoid((Object)vertexformatelement$enumusage1, (ReflectorMethod)Reflector.ForgeVertexFormatElementEnumUseage_postDraw, (Object[])new Object[]{vertexformat, j1, i, bytebuffer});
                    continue;
                }
                int i1 = vertexformatelement1.getIndex();
                switch (1.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumUsage[vertexformatelement$enumusage1.ordinal()]) {
                    case 1: {
                        GL11.glDisableClientState((int)32884);
                        continue block13;
                    }
                    case 2: {
                        OpenGlHelper.setClientActiveTexture((int)(OpenGlHelper.defaultTexUnit + i1));
                        GL11.glDisableClientState((int)32888);
                        OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.defaultTexUnit);
                        continue block13;
                    }
                    case 3: {
                        GL11.glDisableClientState((int)32886);
                        GlStateManager.resetColor();
                        continue block13;
                    }
                    case 4: {
                        GL11.glDisableClientState((int)32885);
                    }
                }
            }
        }
        p_181679_1_.reset();
    }
}
