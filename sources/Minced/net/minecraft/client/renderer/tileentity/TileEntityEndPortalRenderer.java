// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.shaders.ShadersRender;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.GLAllocation;
import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityEndPortal;

public class TileEntityEndPortalRenderer extends TileEntitySpecialRenderer<TileEntityEndPortal>
{
    private static final ResourceLocation END_SKY_TEXTURE;
    private static final ResourceLocation END_PORTAL_TEXTURE;
    private static final Random RANDOM;
    private static final FloatBuffer MODELVIEW;
    private static final FloatBuffer PROJECTION;
    private final FloatBuffer buffer;
    
    public TileEntityEndPortalRenderer() {
        this.buffer = GLAllocation.createDirectFloatBuffer(16);
    }
    
    @Override
    public void render(final TileEntityEndPortal te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        if (!Config.isShaders() || !ShadersRender.renderEndPortal(te, x, y, z, partialTicks, destroyStage, this.getOffset())) {
            GlStateManager.disableLighting();
            TileEntityEndPortalRenderer.RANDOM.setSeed(31100L);
            GlStateManager.getFloat(2982, TileEntityEndPortalRenderer.MODELVIEW);
            GlStateManager.getFloat(2983, TileEntityEndPortalRenderer.PROJECTION);
            final double d0 = x * x + y * y + z * z;
            final int i = this.getPasses(d0);
            final float f = this.getOffset();
            boolean flag = false;
            for (int j = 0; j < i; ++j) {
                GlStateManager.pushMatrix();
                float f2 = 2.0f / (18 - j);
                if (j == 0) {
                    this.bindTexture(TileEntityEndPortalRenderer.END_SKY_TEXTURE);
                    f2 = 0.15f;
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                }
                if (j >= 1) {
                    this.bindTexture(TileEntityEndPortalRenderer.END_PORTAL_TEXTURE);
                    flag = true;
                    Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                }
                if (j == 1) {
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                }
                GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
                GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
                GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
                GlStateManager.texGen(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.texGen(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0f, 1.0f, 0.0f, 0.0f));
                GlStateManager.texGen(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0f, 0.0f, 1.0f, 0.0f));
                GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
                GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
                GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.5f, 0.5f, 0.0f);
                GlStateManager.scale(0.5f, 0.5f, 1.0f);
                final float f3 = (float)(j + 1);
                GlStateManager.translate(17.0f / f3, (2.0f + f3 / 1.5f) * (Minecraft.getSystemTime() % 800000.0f / 800000.0f), 0.0f);
                GlStateManager.rotate((f3 * f3 * 4321.0f + f3 * 9.0f) * 2.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.scale(4.5f - f3 / 4.0f, 4.5f - f3 / 4.0f, 1.0f);
                GlStateManager.multMatrix(TileEntityEndPortalRenderer.PROJECTION);
                GlStateManager.multMatrix(TileEntityEndPortalRenderer.MODELVIEW);
                final Tessellator tessellator = Tessellator.getInstance();
                final BufferBuilder bufferbuilder = tessellator.getBuffer();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                final float f4 = (TileEntityEndPortalRenderer.RANDOM.nextFloat() * 0.5f + 0.1f) * f2;
                final float f5 = (TileEntityEndPortalRenderer.RANDOM.nextFloat() * 0.5f + 0.4f) * f2;
                final float f6 = (TileEntityEndPortalRenderer.RANDOM.nextFloat() * 0.5f + 0.5f) * f2;
                if (te.shouldRenderFace(EnumFacing.SOUTH)) {
                    bufferbuilder.pos(x, y, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x + 1.0, y, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x + 1.0, y + 1.0, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x, y + 1.0, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                }
                if (te.shouldRenderFace(EnumFacing.NORTH)) {
                    bufferbuilder.pos(x, y + 1.0, z).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x + 1.0, y + 1.0, z).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x + 1.0, y, z).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x, y, z).color(f4, f5, f6, 1.0f).endVertex();
                }
                if (te.shouldRenderFace(EnumFacing.EAST)) {
                    bufferbuilder.pos(x + 1.0, y + 1.0, z).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x + 1.0, y + 1.0, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x + 1.0, y, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x + 1.0, y, z).color(f4, f5, f6, 1.0f).endVertex();
                }
                if (te.shouldRenderFace(EnumFacing.WEST)) {
                    bufferbuilder.pos(x, y, z).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x, y, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x, y + 1.0, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x, y + 1.0, z).color(f4, f5, f6, 1.0f).endVertex();
                }
                if (te.shouldRenderFace(EnumFacing.DOWN)) {
                    bufferbuilder.pos(x, y, z).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x + 1.0, y, z).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x + 1.0, y, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x, y, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                }
                if (te.shouldRenderFace(EnumFacing.UP)) {
                    bufferbuilder.pos(x, y + f, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x + 1.0, y + f, z + 1.0).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x + 1.0, y + f, z).color(f4, f5, f6, 1.0f).endVertex();
                    bufferbuilder.pos(x, y + f, z).color(f4, f5, f6, 1.0f).endVertex();
                }
                tessellator.draw();
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
                this.bindTexture(TileEntityEndPortalRenderer.END_SKY_TEXTURE);
            }
            GlStateManager.disableBlend();
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.enableLighting();
            if (flag) {
                Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            }
        }
    }
    
    protected int getPasses(final double p_191286_1_) {
        int i;
        if (p_191286_1_ > 36864.0) {
            i = 1;
        }
        else if (p_191286_1_ > 25600.0) {
            i = 3;
        }
        else if (p_191286_1_ > 16384.0) {
            i = 5;
        }
        else if (p_191286_1_ > 9216.0) {
            i = 7;
        }
        else if (p_191286_1_ > 4096.0) {
            i = 9;
        }
        else if (p_191286_1_ > 1024.0) {
            i = 11;
        }
        else if (p_191286_1_ > 576.0) {
            i = 13;
        }
        else if (p_191286_1_ > 256.0) {
            i = 14;
        }
        else {
            i = 15;
        }
        return i;
    }
    
    protected float getOffset() {
        return 0.75f;
    }
    
    private FloatBuffer getBuffer(final float p_147525_1_, final float p_147525_2_, final float p_147525_3_, final float p_147525_4_) {
        this.buffer.clear();
        this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.buffer.flip();
        return this.buffer;
    }
    
    static {
        END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
        END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
        RANDOM = new Random(31100L);
        MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
        PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    }
}
