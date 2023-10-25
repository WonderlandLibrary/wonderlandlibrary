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
    public void func_181679_a(WorldRenderer worldRenderer) {
        if (worldRenderer.getVertexCount() > 0) {
            int n;
            Object object;
            VertexFormat vertexFormat = worldRenderer.getVertexFormat();
            int n2 = vertexFormat.getNextOffset();
            ByteBuffer byteBuffer = worldRenderer.getByteBuffer();
            List<VertexFormatElement> list = vertexFormat.getElements();
            int n3 = 0;
            while (n3 < list.size()) {
                VertexFormatElement vertexFormatElement = list.get(n3);
                object = vertexFormatElement.getUsage();
                int n4 = vertexFormatElement.getType().getGlConstant();
                n = vertexFormatElement.getIndex();
                byteBuffer.position(vertexFormat.func_181720_d(n3));
                switch (WorldVertexBufferUploader.$SWITCH_TABLE$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumUsage()[((Enum)object).ordinal()]) {
                    case 1: {
                        GL11.glVertexPointer((int)vertexFormatElement.getElementCount(), (int)n4, (int)n2, (ByteBuffer)byteBuffer);
                        GL11.glEnableClientState((int)32884);
                        break;
                    }
                    case 4: {
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + n);
                        GL11.glTexCoordPointer((int)vertexFormatElement.getElementCount(), (int)n4, (int)n2, (ByteBuffer)byteBuffer);
                        GL11.glEnableClientState((int)32888);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        break;
                    }
                    case 3: {
                        GL11.glColorPointer((int)vertexFormatElement.getElementCount(), (int)n4, (int)n2, (ByteBuffer)byteBuffer);
                        GL11.glEnableClientState((int)32886);
                        break;
                    }
                    case 2: {
                        GL11.glNormalPointer((int)n4, (int)n2, (ByteBuffer)byteBuffer);
                        GL11.glEnableClientState((int)32885);
                    }
                }
                ++n3;
            }
            GL11.glDrawArrays((int)worldRenderer.getDrawMode(), (int)0, (int)worldRenderer.getVertexCount());
            n3 = 0;
            int n5 = list.size();
            while (n3 < n5) {
                object = list.get(n3);
                VertexFormatElement.EnumUsage enumUsage = ((VertexFormatElement)object).getUsage();
                n = ((VertexFormatElement)object).getIndex();
                switch (enumUsage) {
                    case POSITION: {
                        GL11.glDisableClientState((int)32884);
                        break;
                    }
                    case UV: {
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + n);
                        GL11.glDisableClientState((int)32888);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        break;
                    }
                    case COLOR: {
                        GL11.glDisableClientState((int)32886);
                        GlStateManager.resetColor();
                        break;
                    }
                    case NORMAL: {
                        GL11.glDisableClientState((int)32885);
                    }
                }
                ++n3;
            }
        }
        worldRenderer.reset();
    }
}

