// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.util.List;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.optifine.shaders.SVertexBuilder;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.optifine.reflect.Reflector;
import net.minecraft.src.Config;

public class WorldVertexBufferUploader
{
    public void draw(final BufferBuilder bufferBuilderIn) {
        if (bufferBuilderIn.getVertexCount() > 0) {
            if (bufferBuilderIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
                bufferBuilderIn.quadsToTriangles();
            }
            final VertexFormat vertexformat = bufferBuilderIn.getVertexFormat();
            final int i = vertexformat.getSize();
            final ByteBuffer bytebuffer = bufferBuilderIn.getByteBuffer();
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
                            GlStateManager.glVertexPointer(vertexformatelement.getElementCount(), k, i, bytebuffer);
                            GlStateManager.glEnableClientState(32884);
                            break;
                        }
                        case UV: {
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + l);
                            GlStateManager.glTexCoordPointer(vertexformatelement.getElementCount(), k, i, bytebuffer);
                            GlStateManager.glEnableClientState(32888);
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                            break;
                        }
                        case COLOR: {
                            GlStateManager.glColorPointer(vertexformatelement.getElementCount(), k, i, bytebuffer);
                            GlStateManager.glEnableClientState(32886);
                            break;
                        }
                        case NORMAL: {
                            GlStateManager.glNormalPointer(k, i, bytebuffer);
                            GlStateManager.glEnableClientState(32885);
                            break;
                        }
                    }
                }
            }
            if (bufferBuilderIn.isMultiTexture()) {
                bufferBuilderIn.drawMultiTexture();
            }
            else if (Config.isShaders()) {
                SVertexBuilder.drawArrays(bufferBuilderIn.getDrawMode(), 0, bufferBuilderIn.getVertexCount(), bufferBuilderIn);
            }
            else {
                GlStateManager.glDrawArrays(bufferBuilderIn.getDrawMode(), 0, bufferBuilderIn.getVertexCount());
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
                            GlStateManager.glDisableClientState(32884);
                            break;
                        }
                        case UV: {
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + i2);
                            GlStateManager.glDisableClientState(32888);
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                            break;
                        }
                        case COLOR: {
                            GlStateManager.glDisableClientState(32886);
                            GlStateManager.resetColor();
                            break;
                        }
                        case NORMAL: {
                            GlStateManager.glDisableClientState(32885);
                            break;
                        }
                    }
                }
            }
        }
        bufferBuilderIn.reset();
    }
}
