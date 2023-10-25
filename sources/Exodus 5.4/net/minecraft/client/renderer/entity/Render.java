/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public abstract class Render<T extends Entity> {
    protected float shadowSize;
    protected float shadowOpaque = 1.0f;
    private static final ResourceLocation shadowTextures = new ResourceLocation("textures/misc/shadow.png");
    protected final RenderManager renderManager;

    public RenderManager getRenderManager() {
        return this.renderManager;
    }

    public void doRenderShadowAndFire(Entity entity, double d, double d2, double d3, float f, float f2) {
        if (this.renderManager.options != null) {
            double d4;
            float f3;
            if (this.renderManager.options.field_181151_V && this.shadowSize > 0.0f && !entity.isInvisible() && this.renderManager.isRenderShadow() && (f3 = (float)((1.0 - (d4 = this.renderManager.getDistanceToCamera(entity.posX, entity.posY, entity.posZ)) / 256.0) * (double)this.shadowOpaque)) > 0.0f) {
                this.renderShadow(entity, d, d2, d3, f3, f2);
            }
            if (!(!entity.canRenderOnFire() || entity instanceof EntityPlayer && ((EntityPlayer)entity).isSpectator())) {
                this.renderEntityOnFire(entity, d, d2, d3, f2);
            }
        }
    }

    protected boolean bindEntityTexture(T t) {
        ResourceLocation resourceLocation = this.getEntityTexture(t);
        if (resourceLocation == null) {
            return false;
        }
        this.bindTexture(resourceLocation);
        return true;
    }

    protected void renderName(T t, double d, double d2, double d3) {
        if (this.canRenderName(t)) {
            this.renderLivingLabel(t, ((Entity)t).getDisplayName().getFormattedText(), d, d2, d3, 64);
        }
    }

    protected void renderOffsetLivingLabel(T t, double d, double d2, double d3, String string, float f, double d4) {
        this.renderLivingLabel(t, string, d, d2, d3, 64);
    }

    private void renderEntityOnFire(Entity entity, double d, double d2, double d3, float f) {
        GlStateManager.disableLighting();
        TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureAtlasSprite = textureMap.getAtlasSprite("minecraft:blocks/fire_layer_0");
        TextureAtlasSprite textureAtlasSprite2 = textureMap.getAtlasSprite("minecraft:blocks/fire_layer_1");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)d, (float)d2, (float)d3);
        float f2 = entity.width * 1.4f;
        GlStateManager.scale(f2, f2, f2);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        float f3 = 0.5f;
        float f4 = 0.0f;
        float f5 = entity.height / f2;
        float f6 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, -0.3f + (float)((int)f5) * 0.02f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float f7 = 0.0f;
        int n = 0;
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        while (f5 > 0.0f) {
            TextureAtlasSprite textureAtlasSprite3 = n % 2 == 0 ? textureAtlasSprite : textureAtlasSprite2;
            this.bindTexture(TextureMap.locationBlocksTexture);
            float f8 = textureAtlasSprite3.getMinU();
            float f9 = textureAtlasSprite3.getMinV();
            float f10 = textureAtlasSprite3.getMaxU();
            float f11 = textureAtlasSprite3.getMaxV();
            if (n / 2 % 2 == 0) {
                float f12 = f10;
                f10 = f8;
                f8 = f12;
            }
            worldRenderer.pos(f3 - f4, 0.0f - f6, f7).tex(f10, f11).endVertex();
            worldRenderer.pos(-f3 - f4, 0.0f - f6, f7).tex(f8, f11).endVertex();
            worldRenderer.pos(-f3 - f4, 1.4f - f6, f7).tex(f8, f9).endVertex();
            worldRenderer.pos(f3 - f4, 1.4f - f6, f7).tex(f10, f9).endVertex();
            f5 -= 0.45f;
            f6 -= 0.45f;
            f3 *= 0.9f;
            f7 += 0.03f;
            ++n;
        }
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    protected Render(RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    private World getWorldFromRenderManager() {
        return this.renderManager.worldObj;
    }

    public void doRender(T t, double d, double d2, double d3, float f, float f2) {
        this.renderName(t, d, d2, d3);
    }

    protected void renderLivingLabel(T t, String string, double d, double d2, double d3, int n) {
        double d4 = ((Entity)t).getDistanceSqToEntity(this.renderManager.livingPlayer);
        if (d4 <= (double)(n * n)) {
            FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
            float f = 1.6f;
            float f2 = 0.016666668f * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)d + 0.0f, (float)d2 + ((Entity)t).height + 0.5f, (float)d3);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-f2, -f2, f2);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            int n2 = 0;
            if (string.equals("deadmau5")) {
                n2 = -10;
            }
            int n3 = fontRenderer.getStringWidth(string) / 2;
            GlStateManager.disableTexture2D();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldRenderer.pos(-n3 - 1, -1 + n2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldRenderer.pos(-n3 - 1, 8 + n2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldRenderer.pos(n3 + 1, 8 + n2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldRenderer.pos(n3 + 1, -1 + n2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, n2, 0x20FFFFFF);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, n2, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }

    private void func_180549_a(Block block, double d, double d2, double d3, BlockPos blockPos, float f, float f2, double d4, double d5, double d6) {
        if (block.isFullCube()) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            double d7 = ((double)f - (d2 - ((double)blockPos.getY() + d5)) / 2.0) * 0.5 * (double)this.getWorldFromRenderManager().getLightBrightness(blockPos);
            if (d7 >= 0.0) {
                if (d7 > 1.0) {
                    d7 = 1.0;
                }
                double d8 = (double)blockPos.getX() + block.getBlockBoundsMinX() + d4;
                double d9 = (double)blockPos.getX() + block.getBlockBoundsMaxX() + d4;
                double d10 = (double)blockPos.getY() + block.getBlockBoundsMinY() + d5 + 0.015625;
                double d11 = (double)blockPos.getZ() + block.getBlockBoundsMinZ() + d6;
                double d12 = (double)blockPos.getZ() + block.getBlockBoundsMaxZ() + d6;
                float f3 = (float)((d - d8) / 2.0 / (double)f2 + 0.5);
                float f4 = (float)((d - d9) / 2.0 / (double)f2 + 0.5);
                float f5 = (float)((d3 - d11) / 2.0 / (double)f2 + 0.5);
                float f6 = (float)((d3 - d12) / 2.0 / (double)f2 + 0.5);
                worldRenderer.pos(d8, d10, d11).tex(f3, f5).color(1.0f, 1.0f, 1.0f, (float)d7).endVertex();
                worldRenderer.pos(d8, d10, d12).tex(f3, f6).color(1.0f, 1.0f, 1.0f, (float)d7).endVertex();
                worldRenderer.pos(d9, d10, d12).tex(f4, f6).color(1.0f, 1.0f, 1.0f, (float)d7).endVertex();
                worldRenderer.pos(d9, d10, d11).tex(f4, f5).color(1.0f, 1.0f, 1.0f, (float)d7).endVertex();
            }
        }
    }

    protected abstract ResourceLocation getEntityTexture(T var1);

    public static void renderOffsetAABB(AxisAlignedBB axisAlignedBB, double d, double d2, double d3) {
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        worldRenderer.setTranslation(d, d2, d3);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_NORMAL);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        tessellator.draw();
        worldRenderer.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.enableTexture2D();
    }

    private void renderShadow(Entity entity, double d, double d2, double d3, float f, float f2) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        this.renderManager.renderEngine.bindTexture(shadowTextures);
        World world = this.getWorldFromRenderManager();
        GlStateManager.depthMask(false);
        float f3 = this.shadowSize;
        if (entity instanceof EntityLiving) {
            EntityLiving entityLiving = (EntityLiving)entity;
            f3 *= entityLiving.getRenderSizeModifier();
            if (entityLiving.isChild()) {
                f3 *= 0.5f;
            }
        }
        double d4 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f2;
        double d5 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f2;
        double d6 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f2;
        int n = MathHelper.floor_double(d4 - (double)f3);
        int n2 = MathHelper.floor_double(d4 + (double)f3);
        int n3 = MathHelper.floor_double(d5 - (double)f3);
        int n4 = MathHelper.floor_double(d5);
        int n5 = MathHelper.floor_double(d6 - (double)f3);
        int n6 = MathHelper.floor_double(d6 + (double)f3);
        double d7 = d - d4;
        double d8 = d2 - d5;
        double d9 = d3 - d6;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        for (BlockPos blockPos : BlockPos.getAllInBoxMutable(new BlockPos(n, n3, n5), new BlockPos(n2, n4, n6))) {
            Block block = world.getBlockState(blockPos.down()).getBlock();
            if (block.getRenderType() == -1 || world.getLightFromNeighbors(blockPos) <= 3) continue;
            this.func_180549_a(block, d, d2, d3, blockPos, f, f3, d7, d8, d9);
        }
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

    public void bindTexture(ResourceLocation resourceLocation) {
        this.renderManager.renderEngine.bindTexture(resourceLocation);
    }

    protected boolean canRenderName(T t) {
        return ((Entity)t).getAlwaysRenderNameTagForRender() && ((Entity)t).hasCustomName();
    }

    public FontRenderer getFontRendererFromRenderManager() {
        return this.renderManager.getFontRenderer();
    }

    public boolean shouldRender(T t, ICamera iCamera, double d, double d2, double d3) {
        AxisAlignedBB axisAlignedBB = ((Entity)t).getEntityBoundingBox();
        if (axisAlignedBB.func_181656_b() || axisAlignedBB.getAverageEdgeLength() == 0.0) {
            axisAlignedBB = new AxisAlignedBB(((Entity)t).posX - 2.0, ((Entity)t).posY - 2.0, ((Entity)t).posZ - 2.0, ((Entity)t).posX + 2.0, ((Entity)t).posY + 2.0, ((Entity)t).posZ + 2.0);
        }
        return ((Entity)t).isInRangeToRender3d(d, d2, d3) && (((Entity)t).ignoreFrustumCheck || iCamera.isBoundingBoxInFrustum(axisAlignedBB));
    }
}

