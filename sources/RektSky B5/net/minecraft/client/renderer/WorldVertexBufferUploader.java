/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import optfine.Reflector;
import org.lwjgl.opengl.GL11;

public class WorldVertexBufferUploader {
    private static final String __OBFID = "CL_00002567";

    public void func_181679_a(WorldRenderer p_181679_1_) {
        if (p_181679_1_.getVertexCount() > 0) {
            VertexFormat vertexformat = p_181679_1_.getVertexFormat();
            int i2 = vertexformat.getNextOffset();
            ByteBuffer bytebuffer = p_181679_1_.getByteBuffer();
            List<VertexFormatElement> list = vertexformat.getElements();
            boolean flag = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
            boolean flag1 = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();
            block12: for (int j2 = 0; j2 < list.size(); ++j2) {
                VertexFormatElement vertexformatelement = list.get(j2);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                if (flag) {
                    Reflector.callVoid((Object)vertexformatelement$enumusage, Reflector.ForgeVertexFormatElementEnumUseage_preDraw, vertexformatelement, i2, bytebuffer);
                    continue;
                }
                int l2 = vertexformatelement.getType().getGlConstant();
                int k2 = vertexformatelement.getIndex();
                bytebuffer.position(vertexformat.func_181720_d(j2));
                switch (vertexformatelement$enumusage) {
                    case POSITION: {
                        GL11.glVertexPointer(vertexformatelement.getElementCount(), l2, i2, bytebuffer);
                        GL11.glEnableClientState(32884);
                        continue block12;
                    }
                    case UV: {
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + k2);
                        GL11.glTexCoordPointer(vertexformatelement.getElementCount(), l2, i2, bytebuffer);
                        GL11.glEnableClientState(32888);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        continue block12;
                    }
                    case COLOR: {
                        GL11.glColorPointer(vertexformatelement.getElementCount(), l2, i2, bytebuffer);
                        GL11.glEnableClientState(32886);
                        continue block12;
                    }
                    case NORMAL: {
                        GL11.glNormalPointer(l2, i2, bytebuffer);
                        GL11.glEnableClientState(32885);
                    }
                }
            }
            if (p_181679_1_.isMultiTexture()) {
                p_181679_1_.drawMultiTexture();
            } else {
                GL11.glDrawArrays(p_181679_1_.getDrawMode(), 0, p_181679_1_.getVertexCount());
            }
            int k1 = list.size();
            block13: for (int i1 = 0; i1 < k1; ++i1) {
                VertexFormatElement vertexformatelement1 = list.get(i1);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.getUsage();
                if (flag1) {
                    Reflector.callVoid((Object)vertexformatelement$enumusage1, Reflector.ForgeVertexFormatElementEnumUseage_postDraw, vertexformatelement1, i2, bytebuffer);
                    continue;
                }
                int j1 = vertexformatelement1.getIndex();
                switch (vertexformatelement$enumusage1) {
                    case POSITION: {
                        GL11.glDisableClientState(32884);
                        continue block13;
                    }
                    case UV: {
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + j1);
                        GL11.glDisableClientState(32888);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        continue block13;
                    }
                    case COLOR: {
                        GL11.glDisableClientState(32886);
                        GlStateManager.resetColor();
                        continue block13;
                    }
                    case NORMAL: {
                        GL11.glDisableClientState(32885);
                    }
                }
            }
        }
        p_181679_1_.reset();
    }
}

