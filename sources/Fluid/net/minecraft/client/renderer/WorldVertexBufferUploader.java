// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer;

import java.util.List;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.optifine.shaders.SVertexBuilder;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.optifine.reflect.Reflector;
import net.minecraft.src.Config;

public class WorldVertexBufferUploader
{
    public void draw(final WorldRenderer p_181679_1_) {
        if (p_181679_1_.getVertexCount() > 0) {
            if (p_181679_1_.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
                p_181679_1_.quadsToTriangles();
            }
            final VertexFormat vertexformat = p_181679_1_.getVertexFormat();
            final int i = vertexformat.getNextOffset();
            final ByteBuffer bytebuffer = p_181679_1_.getByteBuffer();
            final List<VertexFormatElement> list = vertexformat.getElements();
            final boolean flag = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
            final boolean flag2 = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();
            for (int j = 0; j < list.size(); ++j) {
                final VertexFormatElement vertexformatelement = list.get(j);
                final VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                if (flag) {
                    Reflector.callVoid(vertexformatelement$enumusage, Reflector.ForgeVertexFormatElementEnumUseage_preDraw, vertexformat, j, i, bytebuffer);
                }
                else {
                    final int k = vertexformatelement.getType().getGlConstant();
                    final int l = vertexformatelement.getIndex();
                    bytebuffer.position(vertexformat.getOffset(j));
                    switch (vertexformatelement$enumusage) {
                        case POSITION: {
                            GL11.glVertexPointer(vertexformatelement.getElementCount(), k, i, bytebuffer);
                            GL11.glEnableClientState(32884);
                            break;
                        }
                        case UV: {
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + l);
                            GL11.glTexCoordPointer(vertexformatelement.getElementCount(), k, i, bytebuffer);
                            GL11.glEnableClientState(32888);
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                            break;
                        }
                        case COLOR: {
                            GL11.glColorPointer(vertexformatelement.getElementCount(), k, i, bytebuffer);
                            GL11.glEnableClientState(32886);
                            break;
                        }
                        case NORMAL: {
                            GL11.glNormalPointer(k, i, bytebuffer);
                            GL11.glEnableClientState(32885);
                            break;
                        }
                    }
                }
            }
            if (p_181679_1_.isMultiTexture()) {
                p_181679_1_.drawMultiTexture();
            }
            else if (Config.isShaders()) {
                SVertexBuilder.drawArrays(p_181679_1_.getDrawMode(), 0, p_181679_1_.getVertexCount(), p_181679_1_);
            }
            else {
                GL11.glDrawArrays(p_181679_1_.getDrawMode(), 0, p_181679_1_.getVertexCount());
            }
            for (int j2 = 0, k2 = list.size(); j2 < k2; ++j2) {
                final VertexFormatElement vertexformatelement2 = list.get(j2);
                final VertexFormatElement.EnumUsage vertexformatelement$enumusage2 = vertexformatelement2.getUsage();
                if (flag2) {
                    Reflector.callVoid(vertexformatelement$enumusage2, Reflector.ForgeVertexFormatElementEnumUseage_postDraw, vertexformat, j2, i, bytebuffer);
                }
                else {
                    final int i2 = vertexformatelement2.getIndex();
                    switch (vertexformatelement$enumusage2) {
                        case POSITION: {
                            GL11.glDisableClientState(32884);
                            break;
                        }
                        case UV: {
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + i2);
                            GL11.glDisableClientState(32888);
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                            break;
                        }
                        case COLOR: {
                            GL11.glDisableClientState(32886);
                            GlStateManager.resetColor();
                            break;
                        }
                        case NORMAL: {
                            GL11.glDisableClientState(32885);
                            break;
                        }
                    }
                }
            }
        }
        p_181679_1_.reset();
    }
}
