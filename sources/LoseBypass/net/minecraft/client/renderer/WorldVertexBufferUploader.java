/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import org.lwjgl.opengl.GL11;

public class WorldVertexBufferUploader {
    public void func_181679_a(WorldRenderer p_181679_1_) {
        if (p_181679_1_.getVertexCount() > 0) {
            VertexFormat vertexformat = p_181679_1_.getVertexFormat();
            int i = vertexformat.getNextOffset();
            ByteBuffer bytebuffer = p_181679_1_.getByteBuffer();
            List<VertexFormatElement> list = vertexformat.getElements();
            block12: for (int j = 0; j < list.size(); ++j) {
                VertexFormatElement vertexformatelement = list.get(j);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                int k = vertexformatelement.getType().getGlConstant();
                int l = vertexformatelement.getIndex();
                bytebuffer.position(vertexformat.func_181720_d(j));
                switch (1.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumUsage[vertexformatelement$enumusage.ordinal()]) {
                    case 1: {
                        GL11.glVertexPointer((int)vertexformatelement.getElementCount(), (int)k, (int)i, (ByteBuffer)bytebuffer);
                        GL11.glEnableClientState((int)32884);
                        continue block12;
                    }
                    case 2: {
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + l);
                        GL11.glTexCoordPointer((int)vertexformatelement.getElementCount(), (int)k, (int)i, (ByteBuffer)bytebuffer);
                        GL11.glEnableClientState((int)32888);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
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
                        continue block12;
                    }
                }
            }
            GL11.glDrawArrays((int)p_181679_1_.getDrawMode(), (int)0, (int)p_181679_1_.getVertexCount());
            int j1 = list.size();
            block13: for (int i1 = 0; i1 < j1; ++i1) {
                VertexFormatElement vertexformatelement1 = list.get(i1);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.getUsage();
                int k1 = vertexformatelement1.getIndex();
                switch (1.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumUsage[vertexformatelement$enumusage1.ordinal()]) {
                    case 1: {
                        GL11.glDisableClientState((int)32884);
                        continue block13;
                    }
                    case 2: {
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + k1);
                        GL11.glDisableClientState((int)32888);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        continue block13;
                    }
                    case 3: {
                        GL11.glDisableClientState((int)32886);
                        GlStateManager.resetColor();
                        continue block13;
                    }
                    case 4: {
                        GL11.glDisableClientState((int)32885);
                        continue block13;
                    }
                }
            }
        }
        p_181679_1_.reset();
    }
}

